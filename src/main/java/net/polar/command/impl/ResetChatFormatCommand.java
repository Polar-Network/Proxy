package net.polar.command.impl;

import com.velocitypowered.api.command.CommandSource;
import net.polar.Proxy;
import net.polar.command.PolarCommand;
import net.polar.data.Rank;
import com.velocitypowered.api.proxy.Player;
import net.polar.utils.Utils;

public class ResetChatFormatCommand extends PolarCommand {

    public ResetChatFormatCommand() {
        super(Rank.LEGEND, "resetchatformat", "resetformat");
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (!(source instanceof Player player)) {
            source.sendMessage(Utils.color("<red>You must be a player to use this command!"));
            return;
        }
        Proxy.getInstance().getDatabase().fromCache(player).resetChatFormat();
    }
}
