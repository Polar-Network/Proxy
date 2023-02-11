package net.polar.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.polar.command.PolarCommand;
import net.polar.utils.Utils;

public class DiscordCommand extends PolarCommand {

    public DiscordCommand() {super("discord", "dc");}

    @Override
    public void run(CommandSource source, String[] args) {
        if (!(source instanceof Player player)) {
            source.sendMessage(Utils.color("<red>You must be a player to use this command!"));
            return;
        }
        player.sendMessage(Utils.color(
                "<#7289DA><click:open_url:https://discord.gg/CbC5tCAKsv>Click me to join our Discord!"
        ));
    }
}
