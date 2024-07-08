package de.cubeside.connection.event;

import de.cubeside.connection.GlobalServer;
import org.bukkit.event.Event;

public abstract class GlobalServerEvent extends Event {
    private final GlobalServer server;

    public GlobalServerEvent(GlobalServer server) {
        super(Type.CUSTOM_EVENT);
        this.server = server;
    }

    public GlobalServer getServer() {
        return server;
    }
}
