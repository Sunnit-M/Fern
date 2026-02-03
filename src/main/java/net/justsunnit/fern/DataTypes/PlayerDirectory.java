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
        FernServerInit.data.setPlayerUser(UUID, playerUser);

        write();
    }

    public static String getUsername(String UUID){
        checkFile();
        if(directory.containsKey(UUID)){
            return directory.get(UUID);
        }
        FernServerInit.LOGGER.error("[Fern] Player with UUID " + UUID + " not found in directory.");
        return null;
    }

    public static String getUUID(String playerUser){
        checkFile();
        return directory.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(playerUser))
                .map(HashMap.Entry::getKey)
                .findFirst()
                .orElseGet(() -> {
                    FernServerInit.LOGGER.error("[Fern] Player with username " + playerUser + " not found in directory.");
                    return null;
                });
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
