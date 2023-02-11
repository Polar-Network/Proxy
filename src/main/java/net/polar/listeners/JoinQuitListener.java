package net.polar.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import net.polar.Proxy;

public final class JoinQuitListener {

    @Subscribe
    public void onProxyConnect(PlayerChooseInitialServerEvent event) {
        Proxy.getInstance().getDatabase().initPlayer(event.getPlayer());
    }

    @Subscribe
    public void onServerChange(ServerConnectedEvent event) {
        var info = Proxy.getInstance().getDatabase().fromCache(event.getPlayer());
        info.setLastSeenServer(event.getServer().getServerInfo().getName());
    }


    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Proxy.getInstance().getDatabase().removePlayer(event.getPlayer());
    }


}
