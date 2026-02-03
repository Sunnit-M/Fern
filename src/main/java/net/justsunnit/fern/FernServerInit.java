package net.justsunnit.fern;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.justsunnit.fern.DataTypes.BaseDataType;
import net.justsunnit.fern.DataTypes.PermissionInPlayer;
import net.justsunnit.fern.DataTypes.PlayersInPermissions;
import net.justsunnit.fern.Events.EventRegistry;
import net.justsunnit.fern.Utility.ConfigHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FernServerInit implements DedicatedServerModInitializer {
    public static final String MOD_ID = "fern";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Fern.LOADER_TYPE loaderType;

    public static BaseDataType data;


    @Override
    public void onInitializeServer() {
        ConfigHandler.load();
        EventRegistry.registerEvents();
        switch (ConfigHandler.dataType){
            case 0:
                data = new PermissionInPlayer();
                data.load();
                LOGGER.info("[Fern] Using Data Type: (0)");
            case 1:
                data = new PlayersInPermissions();
                data.load();
                LOGGER.info("[Fern] Using Data Type: (1)");
        }

        if (ConfigHandler.dataType != ConfigHandler.portDataTypeTo) {
            portDataType(ConfigHandler.portDataTypeTo);
            ConfigHandler.dataType = ConfigHandler.portDataTypeTo;
            ConfigHandler.save();
        }
    }

    public static void portDataType(int newType){
        switch (newType){
            case 0:
                data = new PermissionInPlayer((PlayersInPermissions) data);
                LOGGER.info("[Fern] Porting Data To Type: (0)");
            case 1:
                data = new PlayersInPermissions((PermissionInPlayer) data);
                LOGGER.info("[Fern] Porting Data To Type: (1)");
        }
        data.write();
    }
}
