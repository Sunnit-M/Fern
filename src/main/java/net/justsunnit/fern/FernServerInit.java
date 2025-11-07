package net.justsunnit.fern;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.justsunnit.fern.DataTypes.BaseDataType;
import net.justsunnit.fern.DataTypes.CompressedPermissionInPlayer;
import net.justsunnit.fern.Utility.ConfigHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FernServerInit implements DedicatedServerModInitializer {
    public static final String MOD_ID = "fern";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static BaseDataType data;


    @Override
    public void onInitializeServer() {
        ConfigHandler.load();
        switch (ConfigHandler.dataType){
            case 0:
                data = new CompressedPermissionInPlayer();
                data.load();
                LOGGER.info("[Fern] Using Data Type: (0)");
        }
    }
}
