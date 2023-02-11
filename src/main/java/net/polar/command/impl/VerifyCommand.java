package net.polar.command.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.polar.Proxy;
import net.polar.command.PolarCommand;
import net.polar.data.Database;
import net.polar.utils.Utils;

public class VerifyCommand extends PolarCommand {

    public VerifyCommand() {
        super("verify", "discordverify");
    }


    @Override
    public void run(CommandSource source, String[] args) {
        if (!(source instanceof Player player)) {
            source.sendMessage(Utils.color("<red>You must be a player to use this command!"));
            return;
        }
        Database db = Proxy.getInstance().getDatabase();
        if (db.isVerified(player)) {
            player.sendMessage(Utils.color("<red>You are already verified!"));
            return;
        }
        db.hasRequestedVerification(player).thenAccept(requested -> {
            if (requested) {
                player.sendMessage(Utils.color("<red>You have already requested verification!"));
                return;
            }
            String random = Utils.generateRandomString(16);
            db.pushToVerification(player, random);
            player.sendMessage(Utils.color("<green>Started verification process! Head over to our Discord server and type <#7289DA>/verify <green>and paste the code below!"));
            player.sendMessage(Utils.color("<#7289DA><click:copy_to_clipboard:" + random + ">Click me to copy the code! Do not share this code with anyone!"));

        });
    }

}
