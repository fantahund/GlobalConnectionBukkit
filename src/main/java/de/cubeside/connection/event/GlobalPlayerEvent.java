package de.cubeside.connection.event;

import de.cubeside.connection.GlobalPlayer;
import de.cubeside.connection.GlobalServer;
import org.bukkit.event.Event;

public abstract class GlobalPlayerEvent extends Event {
    private final GlobalServer server;
    private final GlobalPlayer player;

    public GlobalPlayerEvent(String event, GlobalServer server, GlobalPlayer player) {
        super(event);
        this.server = server;
        this.player = player;
    }

    public GlobalServer getServer() {
        return server;
    }

    public GlobalPlayer getPlayer() {
        return player;
    }
}
