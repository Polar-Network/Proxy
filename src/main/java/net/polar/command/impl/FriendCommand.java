package net.polar.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.polar.Proxy;
import net.polar.command.PolarCommand;
import net.polar.data.GlobalPlayerInfo;
import net.polar.utils.Utils;

import java.time.Duration;
import java.util.*;

public class FriendCommand extends PolarCommand {

    // Player, List of players who have sent them a friend request
    private final Map<Player, Set<UUID>> friendRequests = new HashMap<>();

    public FriendCommand() {
        super("friend", "f", "friends");
        addArgument(1, List.of("add", "remove", "list", "accept", "deny"));
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (!(source instanceof Player player)) {
            source.sendMessage(Utils.color("<red>Only players can use this command!"));
            return;
        }
        String action = args[0].toLowerCase();
        if (action.equals("list")) {
            player.sendMessage(Utils.color("<green>Fetching friends..."));
            Proxy.getInstance().buildTask(() -> {
                List<UUID> friends = Proxy.getInstance().getDatabase().fromCache(player).getFriends();
                StringBuilder builder = new StringBuilder();
                builder.append("<green>Your friends: \n");
                for (UUID uuid : friends) {
                    GlobalPlayerInfo info = Proxy.getInstance().getDatabase().getDatabaseEntry(uuid);
                    if (info == null) builder.append("<red>Unknown");
                    else {
                        boolean onlineStatus = Proxy.getInstance().getServer().getPlayer(uuid).isPresent();
                        String onlineStatusMsg = onlineStatus ? "<green>Online" : "<red>Offline";
                        builder.append("<#ADF3FD> - <#084CFB>").append(info.getLastKnownUsername()).append(" ").append(onlineStatusMsg).append("\n");
                    }
                }
                player.sendMessage(Utils.color(builder.toString()));
            }).schedule();
            return;
        }

        Player target = Proxy.getInstance().getServer().getAllPlayers().stream().filter(p -> p.getUsername().equalsIgnoreCase(args[1])).findFirst().orElse(null);
        if (target == null) {
            player.sendMessage(Utils.color("<red>Player not found! They must be online to add them as a friend!"));
            return;
        }
        switch (action) {

            case "add" -> {
                List<UUID> friends = Proxy.getInstance().getDatabase().fromCache(player).getFriends();
                if (friends.contains(target.getUniqueId())) {
                    player.sendMessage(Utils.color("<red>You are already friends with this player!"));
                    return;
                }
                if (friends.size() >= 100) {
                    player.sendMessage(Utils.color("<red>You have reached the maximum amount of friends!"));
                    return;
                }
                Set<UUID> reqs = friendRequests.get(target);
                if (reqs == null) reqs = new HashSet<>();
                reqs.add(player.getUniqueId());
                friendRequests.put(target, reqs);
                player.sendMessage(Utils.color("<green>You have sent a friend request to " + target.getUsername()));
                target.sendMessage(Utils.color(
                        "<gold>" + player.getUsername() + " has sent you a friend request! You have 60 seconds to <green><click:run_command:/friend accept " + player.getUsername() + "><hover:show_text:'<gold>Click to accept!'><green>accept</green></click><gold> or to <red><click:run_command:/friend deny " + player.getUsername() + "><hover:show_text:'<gold>Click to deny!'><red>deny</red></click><gold>!"
                ));
                Proxy.getInstance().buildTask(() -> {
                    if (friendRequests.get(target) == null) return;
                    friendRequests.get(target).remove(player.getUniqueId());
                    if (friendRequests.get(target).isEmpty()) {
                        friendRequests.remove(target);
                    }
                }).delay(Duration.ofSeconds(60)).schedule();
            }

            case "remove" -> {
                List<UUID> friends = Proxy.getInstance().getDatabase().fromCache(player).getFriends();
                if (!friends.contains(target.getUniqueId())) {
                    player.sendMessage(Utils.color("<red>You are not friends with this player!"));
                    return;
                }
                Proxy.getInstance().getDatabase().fromCache(player).removeFriend(target.getUniqueId());
                Proxy.getInstance().getDatabase().fromCache(target).removeFriend(player.getUniqueId());
                player.sendMessage(Utils.color("<green>You are no longer friends with " + target.getUsername()));
                target.sendMessage(Utils.color("<red>" + player.getUsername() + " has removed you as a friend!"));
            }

            case "accept" -> {
                Set<UUID> reqs = friendRequests.get(player);
                if (reqs == null || !reqs.contains(target.getUniqueId())) {
                    player.sendMessage(Utils.color("<red>You have no friend requests from this player!"));
                    return;
                }
                reqs.remove(target.getUniqueId());
                Proxy.getInstance().getDatabase().fromCache(player).addFriend(target.getUniqueId());
                Proxy.getInstance().getDatabase().fromCache(target).addFriend(player.getUniqueId());
                player.sendMessage(Utils.color("<green>You are now friends with " + target.getUsername()));
                target.sendMessage(Utils.color("<green>" + player.getUsername() + " has accepted your friend request!"));
            }

            case "deny" -> {
                Set<UUID> reqs = friendRequests.get(player);
                if (reqs == null || !reqs.contains(target.getUniqueId())) {
                    player.sendMessage(Utils.color("<red>You have no friend requests from this player!"));
                    return;
                }
                reqs.remove(target.getUniqueId());
                player.sendMessage(Utils.color("<red>You have denied the friend request from " + target.getUsername()));
                target.sendMessage(Utils.color("<red>" + player.getUsername() + " has denied your friend request!"));
            }
            default -> player.sendMessage(Utils.color("<red>/friend <add|remove|list|accept|deny> <player>"));
        }
    }

}
