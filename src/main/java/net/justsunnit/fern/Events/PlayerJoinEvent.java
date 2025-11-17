package net.justsunnit.fern.Events;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.justsunnit.fern.DataTypes.PlayerDirectory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

class PlayerJoinEvent {
    public static void run(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        PlayerDirectory.addPlayer(serverPlayNetworkHandler.getPlayer().getUuidAsString(), serverPlayNetworkHandler.getPlayer().getName().getLiteralString());
    }
}