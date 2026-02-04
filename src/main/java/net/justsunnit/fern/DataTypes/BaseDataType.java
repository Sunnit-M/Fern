package net.justsunnit.fern.DataTypes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Path;

public interface BaseDataType {
    Path dataFolder = FabricLoader.getInstance().getConfigDir().resolve("fern_data");
    File baseData = dataFolder.resolve("data.json").toFile();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    void addPermission(String UUID, String permission, String PlayerUser);
    void removePermission(String UUID, String permission);
    void setPlayerUser(String UUID, String playerUser);
    void write();
    void load();
    void emptyWrite();

    boolean checkPermission(String UUID, String permission);
    @Deprecated
    boolean checkGroupPermission(String groupName, String UUID);

    default void checkFile(){
        if(!baseData.exists()){
            load();
        }
    }
}
