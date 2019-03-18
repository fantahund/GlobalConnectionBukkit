package de.cubeside.connection;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

class PlayerMessageImplementation implements PlayerMessageAPI, Listener {

    private ConnectionPlugin plugin;

    private final static String CHANNEL = "GlobalClient.chat";

    public PlayerMessageImplementation(ConnectionPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGlobalDataEvent(GlobalDataEvent e) {
        if (e.getChannel().equals(CHANNEL)) {
            DataInputStream dis = new DataInputStream(e.getData());
            try {
                GlobalPlayer target = e.getTargetPlayer();
                if (target != null) {
                    Player player = plugin.getServer().getPlayer(target.getUniqueId());
                    if (player != null) {
                        int type = dis.readByte();
                        if (type == 1) {
                            String message = dis.readUTF();
                            player.sendMessage(message);
                        } else if (type == 2) {
                            BaseComponent[] message = ComponentSerializer.parse(dis.readUTF());
                            player.spigot().sendMessage(message);
                        } else if (type == 3) {
                            String message = dis.readUTF();
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                        } else if (type == 4) {
                            String title = dis.readUTF();
                            String subtitle = dis.readUTF();
                            int fadeInTicks = dis.readInt();
                            int durationTicks = dis.readInt();
                            int fadeOutTicks = dis.readInt();
                            player.sendTitle(title, subtitle, fadeInTicks, durationTicks, fadeOutTicks);
                        }
                    }
                }
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not parse MessageAPI message", ex);
            }
        }
    }

    @Override
    public void sendMessage(GlobalPlayer player, String message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeByte(1);
            dos.writeUTF(message);
            dos.close();
        } catch (IOException ex) {
            throw new Error("impossible");
        }
        player.sendData(CHANNEL, baos.toByteArray());
        Player p = plugin.getServer().getPlayer(player.getUniqueId());
        if (p != null) {
            p.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(GlobalPlayer player, BaseComponent... message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeByte(2);
            dos.writeUTF(ComponentSerializer.toString(message));
            dos.close();
        } catch (IOException ex) {
            throw new Error("impossible");
        }
        player.sendData(CHANNEL, baos.toByteArray());
        Player p = plugin.getServer().getPlayer(player.getUniqueId());
        if (p != null) {
            p.spigot().sendMessage(message);
        }
    }

    @Override
    public void sendActionBarMessage(GlobalPlayer player, String message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeByte(3);
            dos.writeUTF(message);
            dos.close();
        } catch (IOException ex) {
            throw new Error("impossible");
        }
        player.sendData(CHANNEL, baos.toByteArray());
        Player p = plugin.getServer().getPlayer(player.getUniqueId());
        if (p != null) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }
    }

    @Override
    public void sendTitleBarMessage(GlobalPlayer player, String title, String subtitle, int fadeInTicks, int durationTicks, int fadeOutTicks) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeByte(4);
            dos.writeUTF(title);
            dos.writeUTF(subtitle);
            dos.writeInt(fadeInTicks);
            dos.writeInt(durationTicks);
            dos.writeInt(fadeOutTicks);
            dos.close();
        } catch (IOException ex) {
            throw new Error("impossible");
        }
        player.sendData(CHANNEL, baos.toByteArray());
        Player p = plugin.getServer().getPlayer(player.getUniqueId());
        if (p != null) {
            p.sendTitle(title, subtitle, fadeInTicks, durationTicks, fadeOutTicks);
        }
    }

}
