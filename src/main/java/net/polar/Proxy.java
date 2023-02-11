package net.polar;


import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;
import net.polar.command.PolarCommand;
import net.polar.command.impl.*;
import net.polar.data.Database;
import net.polar.listeners.ChatListener;
import net.polar.listeners.JoinQuitListener;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Plugin(
    id = "proxy",
    name = "Polar Proxy",
    version = "1.0.0",
    description = "A proxy core for PolarNetwork"
)
public final class Proxy {

    private static Proxy instance;

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final File configFile;
    private final Toml config;

    private final Database database;

    @Inject
    public Proxy(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.configFile = new File(dataDirectory.toFile(), "config.toml");
        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdirs();
                Files.copy(Proxy.class.getClassLoader().getResourceAsStream("config.toml"), configFile.toPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = new Toml().read(configFile);
        this.database = new Database(config.getString("database"));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;
        registerListeners(
            new ChatListener(),
            new JoinQuitListener()
        );
        registerCommands(
            new GrantCommand(),
            new PingCommand(),
            new ShutdownCommand(),
            new ResetChatFormatCommand(),
            new SetChatFormatCommand(),
            new PlaytimeCommand(),
            new DiscordCommand(),
            new FriendCommand(),
            new VerifyCommand()
        );
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        database.close();
    }

    public Scheduler.TaskBuilder buildTask(Runnable runnable) {
        return server.getScheduler().buildTask(this, runnable);
    }

    public static Proxy getInstance() {return instance;}
    public ProxyServer getServer() {return server;}
    public Logger getLogger() {return logger;}
    public Database getDatabase() {return database;}
    public Path getDataDirectory() {return dataDirectory;}

    public Toml getConfig() {return config;}

    private void registerListeners(Object... listeners) {
        Arrays.stream(listeners).forEach(listener -> server.getEventManager().register(this, listener));
    }

    private void registerCommands(PolarCommand... commands) {
        Arrays.stream(commands).forEach(command -> server.getCommandManager().register(command.getName(), command, command.getAliases()));
    }

}
