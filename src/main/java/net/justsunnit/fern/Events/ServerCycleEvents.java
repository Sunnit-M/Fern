package net.justsunnit.fern.Events;

import net.justsunnit.fern.FernServerInit;
import net.justsunnit.fern.Utility.ConfigHandler;
import net.minecraft.server.MinecraftServer;

public class ServerCycleEvents {
    public static void Stopped(MinecraftServer minecraftServer) {
        FernServerInit.data.write();
        ConfigHandler.save();
    }
}
