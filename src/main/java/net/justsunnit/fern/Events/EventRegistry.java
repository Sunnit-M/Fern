package net.justsunnit.fern.Events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventRegistry {
    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register(PlayerJoinEvent::run);
    }
}
