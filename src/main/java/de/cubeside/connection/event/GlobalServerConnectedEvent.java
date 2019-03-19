package de.cubeside.connection.event;

import de.cubeside.connection.GlobalServer;
import org.bukkit.event.HandlerList;

public class GlobalServerConnectedEvent extends GlobalServerEvent {
    private static final HandlerList handlers = new HandlerList();

    public GlobalServerConnectedEvent(GlobalServer server) {
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
