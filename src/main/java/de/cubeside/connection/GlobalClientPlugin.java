package de.cubeside.connection;

import de.iani.playerUUIDCache.PlayerUUIDCache;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.logging.Logger;

public class GlobalClientPlugin extends JavaPlugin {
    private GlobalClientBukkit globalClient;
    private PlayerMessageAPI messageAPI;
    private PlayerPropertiesAPI propertiesAPI;
    private Logger logger;
    private static Thread mainThead;
    public PlayerUUIDCache playerUUIDCache;
    private static GlobalClientPlugin instance;

    public GlobalClientPlugin(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        this.instance = instance;
    }

    @Override
    public void onEnable() {
        mainThead = Thread.currentThread();
        logger = Logger.getLogger("GlobalClient");
        getConfiguration().save();
        getConfiguration().load();
        if (getConfiguration().getString("client.account") == null) {
            Configuration config = getConfiguration();
            config.setProperty("client.account", "CHANGEME");
            config.setProperty("client.password", "CHANGEME");
            config.setProperty("server.host", "localhost");
            config.setProperty("server.port", "25701");
            config.save();
            getConfiguration().load();
        }


        playerUUIDCache = (PlayerUUIDCache) this.getServer().getPluginManager().getPlugin("PlayerUUIDCache");

        globalClient = new GlobalClientBukkit(this);
        reconnectClient();
        //TODO REGISTER getServer().getServicesManager().register(ConnectionAPI.class, globalClient, this, ServicePriority.Normal);

        messageAPI = new PlayerMessageImplementation(this);
        //TODO REGISTER getServer().getServicesManager().register(PlayerMessageAPI.class, messageAPI, this, ServicePriority.Normal);

        propertiesAPI = new PlayerPropertiesImplementation(this);
        //TODO REGISTER getServer().getServicesManager().register(PlayerPropertiesAPI.class, propertiesAPI, this, ServicePriority.Normal);
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
        System.out.println(account);
        System.out.println(password);
        System.out.println(host);
        System.out.println(port);
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
    
    public static GlobalClientPlugin getInstance() {
        return instance;
    }
}
