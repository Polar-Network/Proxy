package net.polar.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.polar.Proxy;
import net.polar.command.PolarCommand;
import net.polar.data.Rank;
import net.polar.utils.Utils;

import java.util.Arrays;

public class SetChatFormatCommand extends PolarCommand {

    public SetChatFormatCommand() {
        super(Rank.LEGEND, "chatformat", "setchatformat", "setformat", "format");
    }

    @Override
    public void run(CommandSource source, String[] args1) {
        if (!(source instanceof Player player)) {
            source.sendMessage(Utils.color("<red>You must be a player to use this command!"));
            return;
        }
        String[] args = args1;
        if (args.length == 0) {
            player.sendMessage(Utils.color("<red>Usage: /setchatformat <format>. Usable arguments are <gold><name> <prefix> <message> <suffix>"));
            return;
        }
        String format = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        if (format.length() > 50) {
            player.sendMessage(Utils.color("<red>Chat format cannot be longer than 50 characters!"));
            return;
        }
        if (
                !format.contains("<name>")
                || !format.contains("<message>")
                || !format.contains("<prefix>")
                || !format.contains("<suffix>")) {
            player.sendMessage(Utils.color("<red>Usage: /setchatformat <format>. Usable arguments are <gold><name> <prefix> <message> <suffix>"));
            return;
        }
        player.sendMessage(Utils.color("<green>Chat format set to: " + format));
        Proxy.getInstance().getDatabase().fromCache(player).setChatFormat(format);
    }
}
