package net.polar.data;

import com.google.gson.annotations.Expose;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public final class GlobalPlayerInfo {


    private static final String DEFAULT_CHAT_FORMAT = "<prefix> <name> <suffix> <message>";


    @Expose private String lastKnownUsername;
    @Expose private Rank rank;
    @Expose  private String chatFormat;
    @Expose private long playTime;
    private long startedPlayingAt = System.currentTimeMillis();
    @Expose private long lastSeen;
    @Expose private long firstSeen;
    @Expose private String lastSeenServer;
    @Expose private long discordId;
    @Expose private List<UUID> friends;
    @Expose private List<UUID> ignored;

    public GlobalPlayerInfo(
            @NotNull String lastKnownUsername,
            @NotNull Rank rank,
            @NotNull String chatFormat,
            long playTime, long lastSeen, long firstSeen,
            @NotNull String lastSeenServer,
            long discordId, @NotNull List<UUID> friends, @NotNull List<UUID> ignored) {
        this.lastKnownUsername = lastKnownUsername;
        this.rank = rank;
        this.chatFormat = chatFormat;
        this.playTime = playTime;
        this.lastSeen = lastSeen;
        this.firstSeen = firstSeen;
        this.lastSeenServer = lastSeenServer;
        this.discordId = discordId;
        this.friends = friends;
        this.ignored = ignored;
    }

    public GlobalPlayerInfo(@NotNull String lastKnownUsername) {
        this(lastKnownUsername, Rank.MEMBER,
                DEFAULT_CHAT_FORMAT,
                0L, System.currentTimeMillis(), System.currentTimeMillis(),
                "none",
                0L, List.of(), List.of()
        );
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public void setChatFormat(String chatFormat) {
        this.chatFormat = chatFormat;
    }

    public void resetChatFormat() {
        this.chatFormat = DEFAULT_CHAT_FORMAT;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public void appendPlayTime() {
        this.playTime += System.currentTimeMillis() - startedPlayingAt;
        this.startedPlayingAt = System.currentTimeMillis();
    }

    public String formatPlaytime() {
        long days = playTime / 86400000;
        long hours = (playTime % 86400000) / 3600000;
        long minutes = ((playTime % 86400000) % 3600000) / 60000;
        long seconds = (((playTime % 86400000) % 3600000) % 60000) / 1000;
        return String.format("<gold>%02d <green>Days<white>, <gold>%02d <green>Hours<white>, <gold>%02d <green>Minutes<white>, <gold>%02d <green>Seconds", days, hours, minutes, seconds);
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public long getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(long firstSeen) {
        this.firstSeen = firstSeen;
    }

    public String getLastSeenServer() {
        return lastSeenServer;
    }

    public void setLastSeenServer(String lastSeenServer) {
        this.lastSeenServer = lastSeenServer;
    }

    public long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(long discordId) {
        this.discordId = discordId;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public void addFriend(UUID friend) {
        this.friends.add(friend);
    }

    public void removeFriend(UUID friend) {
        this.friends.remove(friend);
    }

    public List<UUID> getIgnored() {
        return ignored;
    }

    public void addIgnored(UUID ignored) {
        this.ignored.add(ignored);
    }

    public void removeIgnored(UUID ignored) {
        this.ignored.remove(ignored);
    }

    public String getLastKnownUsername() {
        return lastKnownUsername;
    }

    public void setLastKnownUsername(String lastKnownUsername) {
        this.lastKnownUsername = lastKnownUsername;
    }

    public Document toDocumentFrom(@NotNull UUID uuid) {
        return new Document("uuid", uuid.toString())
                .append("lastKnownUsername", lastKnownUsername)
                .append("rank", rank.name())
                .append("chatFormat", chatFormat)
                .append("playTime", playTime)
                .append("lastSeen", lastSeen)
                .append("firstSeen", firstSeen)
                .append("lastSeenServer", lastSeenServer)
                .append("discordId", discordId)
                .append("friends", friends)
                .append("ignored", ignored);
    }

}
