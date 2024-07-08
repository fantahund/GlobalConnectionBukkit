package de.cubeside.connection;

import de.iani.playerUUIDCache.PlayerUUIDCache;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class GlobalClientPlugin extends JavaPlugin {
    private GlobalClientBukkit globalClient;
    private PlayerMessageAPI messageAPI;
    private PlayerPropertiesAPI propertiesAPI;
    private Logger logger;
    private static Thread mainThead;
    public PlayerUUIDCache playerUUIDCache;

    public GlobalClientPlugin(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
    }

    @Override
    public void onEnable() {
        mainThead = Thread.currentThread();
        logger = Logger.getLogger("GlobalClient");
        getConfiguration().load();

        playerUUIDCache = (PlayerUUIDCache) this.getServer().getPluginManager().getPlugin("PlayerUUIDCache");

        globalClient = new GlobalClientBukkit(this);
        reconnectClient();
        getServer().getServicesManager().register(ConnectionAPI.class, globalClient, this, ServicePriority.Normal);

        messageAPI = new PlayerMessageImplementation(this);
        getServer().getServicesManager().register(PlayerMessageAPI.class, messageAPI, this, ServicePriority.Normal);

        propertiesAPI = new PlayerPropertiesImplementation(this);
        getServer().getServicesManager().register(PlayerPropertiesAPI.class, propertiesAPI, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        if (globalClient != null) {
            globalClient.shutdown();
        }
        globalClient = null;
        messageAPI = null;
        propertiesAPI = null;
    }

    public void reconnectClient() {
        String account = getConfiguration().getString("client.account");
        String password = getConfiguration().getString("client.password");
        String host = getConfiguration().getString("server.host");
        int port = getConfiguration().getInt("server.port", 25701);
        globalClient.setServer(host, port, account, password);
    }

    public ConnectionAPI getConnectionAPI() {
        return globalClient;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isPrimaryThread() {
        return mainThead.equals(Thread.currentThread());
    }

    public PlayerUUIDCache getPlayerUUIDCache() {
        return playerUUIDCache;
    }
}
