package de.cubeside.connection.event;

import de.cubeside.connection.GlobalPlayer;
import de.cubeside.connection.GlobalServer;
import org.bukkit.event.HandlerList;

public class GlobalPlayerJoinedEvent extends GlobalPlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final boolean joinedTheNetwork;

    public GlobalPlayerJoinedEvent(GlobalServer server, GlobalPlayer player, boolean joinedTheNetwork) {
        super(server, player);
        this.joinedTheNetwork = joinedTheNetwork;
    }

    public boolean hasJustJoinedTheNetwork() {
        return joinedTheNetwork;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
