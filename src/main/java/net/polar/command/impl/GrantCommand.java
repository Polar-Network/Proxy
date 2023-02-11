package net.polar.command.impl;


import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.polar.Proxy;
import net.polar.command.PolarCommand;
import net.polar.data.Rank;
import net.polar.utils.Utils;

import java.util.Arrays;
import java.util.List;

public final class GrantCommand extends PolarCommand {

    public GrantCommand() {
        super(Rank.OWNER, "grant", "rank");
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (args.length != 2) {
            source.sendMessage(Utils.color("<red>Invalid Usage: <white>/grant <player> <rank>"));
            return;
        }

        String playerString = args[0];
        Rank rank = Rank.valueOf(args[1].toUpperCase());
        Player player = Proxy.getInstance().getServer().getAllPlayers().stream().filter(p -> p.getUsername().equalsIgnoreCase(playerString)).findFirst().orElse(null);
        if (player == null) {
            source.sendMessage(Utils.color("<red>Player not found. They must be online for this to work."));
            return;
        }
        Proxy.getInstance().getDatabase().fromCache(player).setRank(rank);
        Proxy.getInstance().getDatabase().syncCacheWithDb(player);
        player.sendMessage(Utils.color("<green>You have been granted the rank <white>" + rank.name()));
        source.sendMessage(Utils.color("<green>Successfully granted <white>" + player.getUsername() + " <green>the rank <white>" + rank.name()));

    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length > 1) {
            return Arrays.stream(Rank.values()).map(Rank::name).toList();
        }
        return List.of();
    }
}
