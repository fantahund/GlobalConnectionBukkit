package de.cubeside.connection;

import de.cubeside.connection.event.GlobalDataEvent;
import de.cubeside.connection.event.GlobalPlayerDisconnectedEvent;
import de.cubeside.connection.event.GlobalPlayerJoinedEvent;
import de.cubeside.connection.event.GlobalServerConnectedEvent;
import de.cubeside.connection.event.GlobalServerDisconnectedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class GlobalClientBukkit extends GlobalClient implements Listener {
    private final ConnectionPlugin plugin;

    public GlobalClientBukkit(ConnectionPlugin connectionPlugin, String host, int port, String account, String password) {
        super(host, port, account, password, false, connectionPlugin.getLogger());
        plugin = connectionPlugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startThread();
        for (Player p : connectionPlugin.getServer().getOnlinePlayers()) {
            onPlayerOnline(p.getUniqueId(), p.getName(), System.currentTimeMillis());
        }
    }

    @Override
    protected void runInMainThread(Runnable r) {
        plugin.getServer().getScheduler().runTask(plugin, r);
    }

    @Override
    protected void processData(GlobalServer source, String channel, GlobalPlayer targetPlayer, GlobalServer targetServer, byte[] data) {
        plugin.getServer().getPluginManager().callEvent(new GlobalDataEvent(source, targetPlayer, channel, data));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        onPlayerOnline(e.getPlayer().getUniqueId(), e.getPlayer().getName(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        onPlayerOffline(e.getPlayer().getUniqueId());
    }

    @Override
    protected void onPlayerJoined(GlobalServer server, GlobalPlayer player, boolean joinedTheNetwork) {
        plugin.getServer().getPluginManager().callEvent(new GlobalPlayerJoinedEvent(server, player, joinedTheNetwork));
    }

    @Override
    protected void onPlayerDisconnected(GlobalServer server, GlobalPlayer player, boolean leftTheNetwork) {
        plugin.getServer().getPluginManager().callEvent(new GlobalPlayerDisconnectedEvent(server, player, leftTheNetwork));
    }

    @Override
    protected void onServerConnected(GlobalServer server) {
        plugin.getServer().getPluginManager().callEvent(new GlobalServerConnectedEvent(server));
    }

    @Override
    protected void onServerDisconnected(GlobalServer server) {
        plugin.getServer().getPluginManager().callEvent(new GlobalServerDisconnectedEvent(server));
    }
}