package de.cubeside.connection.event;

import de.cubeside.connection.GlobalPlayer;
import de.cubeside.connection.GlobalServer;
import org.bukkit.event.HandlerList;

public class GlobalPlayerDisconnectedEvent extends GlobalPlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final boolean leftTheNetwork;

    public GlobalPlayerDisconnectedEvent(GlobalServer server, GlobalPlayer player, boolean leftTheNetwork) {
        super(server, player);
        this.leftTheNetwork = leftTheNetwork;
    }

    public boolean hasJustLeftTheNetwork() {
        return leftTheNetwork;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
