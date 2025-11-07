package net.justsunnit.fern.Utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.justsunnit.fern.Fern;
import net.justsunnit.fern.FernServerInit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigHandler {
    public static File configFile = FabricLoader.getInstance().getConfigDir().resolve("fern.json").toFile();

    // 0 - One File, UUID { User , Perms:[] }
    // 1 - One File, Each Perm { Player1, Player2 }
    // 2 - Each Player One File { Perms:[] }
    // 3 - Each Perm One File { Player1, Player2 }

    public static int dataType = 0;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static class Config {
        public int dataType = 0;
    }


    private static Config config = new Config();

    public static void load() {
        if(!configFile.exists()) {
            FernServerInit.LOGGER.warn("[Fern] Config file not found, creating default config.");
            save();
        }

        try(FileReader reader = new FileReader(configFile)){
            config = gson.fromJson(reader, Config.class);
            FernServerInit.LOGGER.info("[Fern] Config file loaded successfully.");
        } catch (Exception e){
            FernServerInit.LOGGER.error("[Fern] Failed to load config file!");
            e.printStackTrace();
        }

        dataType = config.dataType;
    }

    public static void save() {
        try(FileWriter writer = new FileWriter(configFile)){
            gson.toJson(config, writer);
        } catch (Exception e) {
            FernServerInit.LOGGER.error("[Fern] Failed to save config file!");
        }
    }

    public static void reload() {
        load();
    }
}
