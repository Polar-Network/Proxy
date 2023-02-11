package net.polar.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.polar.command.PolarCommand;
import net.polar.utils.Utils;

public final class PingCommand extends PolarCommand {

    public PingCommand() {
        super("ping", "latency");
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (source instanceof Player player) {
            player.sendMessage(Utils.color("<green>Your ping is <white>" + player.getPing() + "ms"));
        }
        else {
            source.sendMessage(Utils.color("<red>Console does not have ping!"));
        }
    }
}
