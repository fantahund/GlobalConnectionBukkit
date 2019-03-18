package de.cubeside.connection;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class ConnectionPlugin extends JavaPlugin {
    private GlobalClient globalClient;
    private PlayerMessageAPI messageAPI;

    @Override
    public void onEnable() {
        globalClient = new GlobalClientBukkit(this, null, 0, null, null);
        messageAPI = new PlayerMessageImplementation(this);
        getServer().getServicesManager().register(ConnectionAPI.class, globalClient, this, ServicePriority.Normal);
        getServer().getServicesManager().register(PlayerMessageAPI.class, messageAPI, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        if (globalClient != null) {
            globalClient.shutdown();
            globalClient = null;
        }
    }

    public ConnectionAPI getConnectionAPI() {
        return globalClient;
    }

    public PlayerMessageAPI getMessageAPI() {
        return messageAPI;
    }
}
