package de.cubeside.connection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class GlobalClientBukkit extends GlobalClient implements Listener {
    private final ConnectionPlugin plugin;

    public GlobalClientBukkit(ConnectionPlugin connectionPlugin, String host, int port, String account, String password) {
        super(host, port, account, password, false);
        plugin = connectionPlugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startThread();
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
}