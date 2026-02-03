package net.justsunnit.fern.Events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventRegistry {
    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register(PlayerJoinEvent::run);
        ServerLifecycleEvents.SERVER_STOPPING.register(ServerCycleEvents::Stopped);
    }
}
