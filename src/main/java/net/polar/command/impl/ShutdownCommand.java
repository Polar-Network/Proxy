package net.polar.command.impl;

import com.velocitypowered.api.command.CommandSource;
import net.polar.Proxy;
import net.polar.command.PolarCommand;
import net.polar.data.Rank;
import net.polar.utils.Utils;

public final class ShutdownCommand extends PolarCommand {

    public ShutdownCommand() {
        super(Rank.ADMINISTRATOR, "shutdown", "stop");
    }

    @Override
    public void run(CommandSource source, String[] args) {
        source.sendMessage(Utils.color("<red>Shutting down..."));
        Proxy.getInstance().getServer().shutdown();
    }
}
