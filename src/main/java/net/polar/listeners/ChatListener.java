package net.polar.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.polar.Proxy;
import net.polar.data.GlobalPlayerInfo;
import net.polar.utils.Utils;

public final class ChatListener {

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        event.setResult(PlayerChatEvent.ChatResult.denied());
        final Player player = event.getPlayer();

        final GlobalPlayerInfo info = Proxy.getInstance().getDatabase().fromCache(player);
        String format = info.getChatFormat()
                .replace("<prefix>", info.getRank().surroundPrefix())
                .replace("<name>", info.getRank().surroundString(player.getUsername()))
                .replace("<message>", event.getMessage())
                .replace(" <suffix>", ""); // TODO: Suffixes
        player.getCurrentServer().get().getServer().sendMessage(Utils.color(format));
    }


}
