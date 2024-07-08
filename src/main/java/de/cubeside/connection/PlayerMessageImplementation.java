package de.cubeside.connection;

import de.cubeside.connection.event.GlobalDataEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import de.cubeside.connection.event.GlobalDataListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

class PlayerMessageImplementation extends GlobalDataListener implements PlayerMessageAPI {

    private final static int MESSAGE_CHAT = 1;

    private GlobalClientPlugin plugin;

    private final static String CHANNEL = "GlobalClient.chat";

    public PlayerMessageImplementation(GlobalClientPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT,this, Event.Priority.Normal, plugin);
    }


    public void onGlobalData(GlobalDataEvent e) {
        if (e.getChannel().equals(CHANNEL)) {
            DataInputStream dis = new DataInputStream(e.getData());
            try {
                GlobalPlayer target = e.getTargetPlayer();
                if (target != null) {
                    Player player = plugin.getServer().getPlayer(target.getName());
                    if (player != null) {
                        int type = dis.readByte();
                        if (type == MESSAGE_CHAT) {
                            String message = dis.readUTF();
                            player.sendMessage(message);
                        }
                    }
                }
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not parse PlayerMessage message", ex);
            }
        }
    }

    @Override
    public void sendMessage(GlobalPlayer player, String message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeByte(MESSAGE_CHAT);
            dos.writeUTF(message);
            dos.close();
        } catch (IOException ex) {
            throw new Error("impossible");
        }
        player.sendData(CHANNEL, baos.toByteArray());
        Player p = plugin.getServer().getPlayer(player.getName());
        if (p != null) {
            p.sendMessage(message);
        }
    }
}
