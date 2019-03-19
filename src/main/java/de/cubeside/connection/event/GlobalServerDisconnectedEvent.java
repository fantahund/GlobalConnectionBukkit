package de.cubeside.connection.event;

import de.cubeside.connection.GlobalServer;
import org.bukkit.event.HandlerList;

public class GlobalServerDisconnectedEvent extends GlobalServerEvent {
    private static final HandlerList handlers = new HandlerList();

    public GlobalServerDisconnectedEvent(GlobalServer server) {
        super(server);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
