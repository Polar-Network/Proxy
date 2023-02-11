package net.polar.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.velocitypowered.api.proxy.Player;
import net.polar.Proxy;
import net.polar.utils.Utils;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class Database {

    private static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> globalCollection;

    private final Map<Player, GlobalPlayerInfo> cache = new ConcurrentHashMap<>();


    public Database(@NotNull String host) {
        this.client = MongoClients.create(host);
        this.database = client.getDatabase("polardb");
        this.globalCollection = database.getCollection("global");
    }

    public void initPlayer(@NotNull Player player) {
        Document document = new Document("uuid", player.getUniqueId().toString());
        if (globalCollection.find(document).first() == null) {
            GlobalPlayerInfo info = new GlobalPlayerInfo(player.getUsername());
            updatePlayerInfo(player, info);
            return;
        }
        Document result = globalCollection.find(document).first();
        result.remove("_id");
        result.remove("uuid");
        GlobalPlayerInfo info = GSON.fromJson(result.toJson(), GlobalPlayerInfo.class);
        cache.put(player, info);
    }

    public @Nullable GlobalPlayerInfo getDatabaseEntry(@NotNull UUID uuid) {
        Document document = new Document("uuid", uuid.toString());
        if (globalCollection.find(document).first() == null) {
            return null;
        }
        Document result = globalCollection.find(document).first();
        result.remove("_id");
        result.remove("uuid");
        GlobalPlayerInfo info = GSON.fromJson(result.toJson(), GlobalPlayerInfo.class);
        return info;
    }

    public void updatePlayerInfo(@NotNull Player player, @NotNull GlobalPlayerInfo info) {
        cache.put(player, info);

        Proxy.getInstance().buildTask(() -> {
            globalCollection.replaceOne(new Document("uuid", player.getUniqueId().toString()), info.toDocumentFrom(player.getUniqueId()), new ReplaceOptions().upsert(true));
        }).schedule();
    }


    public void syncCacheWithDb(@NotNull Player player) {
        GlobalPlayerInfo info = fromCache(player);
        Proxy.getInstance().buildTask(() -> {
            globalCollection.replaceOne(new Document("uuid", player.getUniqueId().toString()), info.toDocumentFrom(player.getUniqueId()), new ReplaceOptions().upsert(true));
        }).schedule();
    }


    public void removePlayer(@NotNull Player player) {
        syncCacheWithDb(player);
        cache.remove(player);
    }

    public GlobalPlayerInfo fromCache(@NotNull Player player) {
        return cache.get(player);
    }

    public CompletableFuture<Boolean> hasRequestedVerification(@NotNull Player player)  {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Proxy.getInstance().buildTask(() -> {
            MongoCollection<Document> verification = database.getCollection("verification");
            Document document = new Document("uuid", player.getUniqueId().toString());
            if (verification.find(document).first() == null) {
                future.complete(false);
                return;
            }
            future.complete(true);
        }).schedule();
        return future;
    }

    public boolean isVerified(@NotNull Player player) {
        return fromCache(player).getDiscordId() != 0L;
    }

    public void pushToVerification(@NotNull Player player, @NotNull String code) {
        Proxy.getInstance().buildTask(() -> {
            MongoCollection<Document> verification = database.getCollection("verification");
            verification.replaceOne(new Document("uuid", player.getUniqueId().toString()), new Document("uuid", player.getUniqueId().toString()).append("code", code), new ReplaceOptions().upsert(true));
        }).schedule();
        Proxy.getInstance().buildTask(() -> {
            MongoCollection<Document> verification = database.getCollection("verification");
            if (verification.find(new Document("uuid", player.getUniqueId().toString())).first() == null) {return;}
            verification.deleteOne(new Document("uuid", player.getUniqueId().toString()));
            if (Proxy.getInstance().getServer().getAllPlayers().contains(player)) {
                player.sendMessage(Utils.color("<red>You have failed to verify in time."));
            }
        }).delay(Duration.ofMinutes(5)).schedule();
    }


    public void close() {
        client.close();
    }

}
