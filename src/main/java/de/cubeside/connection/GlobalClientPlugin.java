package de.cubeside.connection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalClientPlugin extends JavaPlugin {
    private GlobalClientBukkit globalClient;
    private PlayerMessageAPI messageAPI;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        globalClient = new GlobalClientBukkit(this);
        reconnectClient();
        messageAPI = new PlayerMessageImplementation(this);
        getServer().getServicesManager().register(ConnectionAPI.class, globalClient, this, ServicePriority.Normal);
        getServer().getServicesManager().register(PlayerMessageAPI.class, messageAPI, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        if (globalClient != null) {
            globalClient.shutdown();
        }
        globalClient = null;
        messageAPI = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("globalclient.reload")) {
            return false;
        }
        reloadConfig();
        reconnectClient();
        return true;
    }

    public void reconnectClient() {
        String account = getConfig().getString("client.account");
        String password = getConfig().getString("client.password");
        String host = getConfig().getString("server.host");
        int port = getConfig().getInt("server.port");
        globalClient.setServer(host, port, account, password);
    }

    public ConnectionAPI getConnectionAPI() {
        return globalClient;
    }

    public PlayerMessageAPI getMessageAPI() {
        return messageAPI;
    }
}
