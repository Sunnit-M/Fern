package net.justsunnit.fern.DataTypes;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.justsunnit.fern.FernServerInit;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class PlayerDirectory {
    private static final Gson gson = new Gson();
    private static final File file = FabricLoader.getInstance().getConfigDir().resolve("fern_directory.json").toFile();
    public static HashMap<String,String> directory = new HashMap<>();

    public static void addPlayer(String UUID, String playerUser){
        checkFile();

        if(directory.containsKey(UUID)){
            FernServerInit.LOGGER.info("[Fern] Player with UUID " + UUID + " already exists in directory, updating username.");
        }
        directory.put(UUID, playerUser);

        write();
    }

    private static void write(){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (Exception e) {
                FernServerInit.LOGGER.error("[Fern] Failed to create player directory file!");
                return;
            }
        }
        try(FileWriter writer = new FileWriter(file)){
            gson.toJson(directory, writer);
        } catch (Exception e) {
            FernServerInit.LOGGER.error("[Fern] Failed to write player directory file!");
        }
    }

    private static void load(){
        if(!file.exists()){
            write();
        }
        try(java.io.FileReader reader = new java.io.FileReader(file)){
            directory = gson.fromJson(reader, HashMap.class);
        } catch (Exception e) {
            FernServerInit.LOGGER.error("[Fern] Failed to load player directory file!");
        }
    }

    private static void checkFile(){
        if(!file.exists() || directory.isEmpty()){
            load();
        }
    }
}
