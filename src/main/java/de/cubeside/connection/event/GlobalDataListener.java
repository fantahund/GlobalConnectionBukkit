package de.cubeside.connection.event;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class GlobalDataListener extends CustomEventListener implements Listener {
    public GlobalDataListener() {

    }

    public void onGlobalPlayerDisconnected(GlobalPlayerDisconnectedEvent event) {

    }

    public void onGlobalPlayer(GlobalPlayerEvent event) {

    }

    public void onGlobalPlayerJoined(GlobalPlayerJoinedEvent event) {

    }

    public void onGlobalServerConnected(GlobalServerConnectedEvent event) {

    }

    public void onGlobalServerDisconnected(GlobalServerDisconnectedEvent event) {

    }

    public void onGlobalData(GlobalDataEvent event) {

    }

    public void onGlobalPlayerPropertyChanged(GlobalPlayerPropertyChangedEvent event) {

    }

    @Override
    public void onCustomEvent(Event event) {
        if (event instanceof GlobalPlayerPropertyChangedEvent) {
            onGlobalPlayerPropertyChanged((GlobalPlayerPropertyChangedEvent) event);
        }

        if (event instanceof GlobalDataEvent) {
            onGlobalData((GlobalDataEvent) event);
        }

        if (event instanceof GlobalServerDisconnectedEvent) {
            onGlobalServerDisconnected((GlobalServerDisconnectedEvent) event);
        }

        if (event instanceof GlobalServerConnectedEvent) {
            onGlobalServerConnected((GlobalServerConnectedEvent) event);
        }

        if (event instanceof GlobalPlayerJoinedEvent) {
            onGlobalPlayerJoined((GlobalPlayerJoinedEvent) event);
        }

        if (event instanceof GlobalPlayerEvent) {
            onGlobalPlayer((GlobalPlayerEvent) event);
        }

        if (event instanceof GlobalPlayerDisconnectedEvent) {
            onGlobalPlayerDisconnected((GlobalPlayerDisconnectedEvent) event);
        }
    }
}
