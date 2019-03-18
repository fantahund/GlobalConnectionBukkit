package de.cubeside.connection;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GlobalDataEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final GlobalServer source;
    private final GlobalPlayer targetPlayer;

    private final String channel;
    private final byte[] data;

    public GlobalDataEvent(GlobalServer source, GlobalPlayer targetPlayer, String channel, byte[] data) {
        this.source = source;
        this.targetPlayer = targetPlayer;
        this.channel = channel;
        this.data = data;
    }

    public GlobalServer getSource() {
        return source;
    }

    public GlobalPlayer getTargetPlayer() {
        return targetPlayer;
    }

    public String getChannel() {
        return channel;
    }

    public InputStream getData() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
