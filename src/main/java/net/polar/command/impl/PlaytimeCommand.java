package net.polar.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.polar.Proxy;
import net.polar.command.PolarCommand;
import net.polar.data.GlobalPlayerInfo;
import net.polar.utils.Utils;

public class PlaytimeCommand extends PolarCommand {

    public PlaytimeCommand() {
        super("playtime", "ptime");
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (!(source instanceof Player player)) {
            source.sendMessage(Utils.color("<red>You must be a player to use this command!"));
            return;
        }
        GlobalPlayerInfo info = Proxy.getInstance().getDatabase().fromCache(player);
        info.appendPlayTime();
        player.sendMessage(Utils.color("<green>Your playtime is: " + info.formatPlaytime()));
    }
}
