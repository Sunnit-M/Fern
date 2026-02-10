package net.justsunnit.fern.Utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.justsunnit.fern.Fern;
import net.justsunnit.fern.FernServerInit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigHandler {
    public static File configFile = FabricLoader.getInstance().getConfigDir().resolve("fern.json").toFile();
    public static int dataType = 0;
    public static int portDataTypeTo = 0;
    public static Fern.LOADER_TYPE loaderType = Fern.LOADER_TYPE.gson;
    public static Fern.LOADER_TYPE portLoaderTypeTo = Fern.LOADER_TYPE.gson;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static class Config {
        public Config(int dataType, int portDataTypeTo, Fern.LOADER_TYPE currentLoaderType, Fern.LOADER_TYPE portLoaderTypeTo) {
            this.portDataTypeTo = portDataTypeTo;
            this.currentDatatype = dataType;
            this.currentLoaderType = currentLoaderType;
            this.portLoaderTypeTo = portLoaderTypeTo;
        }

        public int portDataTypeTo;
        public int currentDatatype;

        public Fern.LOADER_TYPE currentLoaderType;
        public Fern.LOADER_TYPE portLoaderTypeTo;
    }


    private static Config config = new Config(0, 0, Fern.LOADER_TYPE.gson, Fern.LOADER_TYPE.gson);

    public static void load() {
        if(!configFile.exists()) {
            FernServerInit.LOGGER.warn("[Fern] Config file not found, creating default config.");
            save();
        }

        try(JsonReader reader = new JsonReader(new FileReader(configFile))){
            reader.setStrictness(Strictness.LENIENT);
            config = gson.fromJson(reader, Config.class);
            FernServerInit.LOGGER.info("[Fern] Config file loaded successfully.");
        } catch (Exception e){
            FernServerInit.LOGGER.error("[Fern] Failed to load config file!");
            e.printStackTrace();
        }

        dataType = config.currentDatatype;
        portDataTypeTo = config.portDataTypeTo;
        loaderType = config.currentLoaderType;
        portLoaderTypeTo = config.portLoaderTypeTo;
    }

    public static void save() {
        config = new Config(dataType, portDataTypeTo, loaderType, portLoaderTypeTo);
        try(FileWriter writer = new FileWriter(configFile)){
            writer.write("// Only chnage the port to fields as other fields are managed by the mod.\n");
            gson.toJson(config, writer);
        } catch (Exception e) {
            FernServerInit.LOGGER.error("[Fern] Failed to save config file!");
        }
    }

    public static void reload() {
        load();
    }
}
