package net.justsunnit.fern.Events;

import net.justsunnit.fern.FernServerInit;
import net.minecraft.server.MinecraftServer;

public class ServerCycleEvents {
    public static void Stopped(MinecraftServer minecraftServer) {
        FernServerInit.data.write();
    }
}
