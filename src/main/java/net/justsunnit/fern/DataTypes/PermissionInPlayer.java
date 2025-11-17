package net.justsunnit.fern.DataTypes;

import net.justsunnit.fern.FernServerInit;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompressedPermissionInPlayer implements BaseDataType {
    public static class playerData {
        public String playerUser;
        public List<String> permissions;

        public playerData(String playerUser, List<String> permissions) {
            this.playerUser = playerUser;
            this.permissions = permissions;
        }
    }

    public HashMap<String, playerData> data;

    public CompressedPermissionInPlayer() {
        this.data = new HashMap<>();
    }
    
    @Override
    public void addPermission(String UUID, String permission, String PlayerUser) {
        // One File, UUID { User , Perms:[] }
        checkFile();
        if(data.containsKey(UUID)){
            playerData pData = data.get(UUID);
            if(!pData.permissions.contains(permission)){
                pData.permissions.add(permission);
            }
        }

        write();
    }

    @Override
    public boolean checkPermission(String UUID, String permission) {
        checkFile();
        if(data.containsKey(UUID)){
            playerData pData = data.get(UUID);
            return pData.permissions.contains(permission);
        }
        return false;
    }

    @Override
    public boolean checkGroupPermission(String groupName, String UUID) {
        checkFile();
        if(data.containsKey(UUID)){
            playerData pData = data.get(UUID);
            return pData.permissions.stream().anyMatch(s -> s.startsWith(groupName));
        }
        return false;
    }


    @Override
    public void removePermission(String UUID, String permission) {
        checkFile();
        if(data.containsKey(UUID)){
            playerData pData = data.get(UUID);
            pData.permissions.remove(permission);
        }

        write();
    }

    @Override
    public void setPlayerUser(String UUID, String playerUser) {
        checkFile();
        if(data.containsKey(UUID)){
            playerData pData = data.get(UUID);
            pData.playerUser = playerUser;
        }
        else{
            playerData pData = new playerData(playerUser, List.of());
            data.put(UUID, pData);
        }

        write();
    }

    @Override
    public void write() {
        try(FileWriter writer = new FileWriter(baseData)){
            gson.toJson(this.data, writer);
        } catch (Exception e) {
            FernServerInit.LOGGER.error("[Fern] Failed to write data file!");
        }
    }

    @Override
    public void load() {
        if (!baseData.exists()) {
            try (FileWriter writer = new FileWriter(baseData)) {
                CompressedPermissionInPlayer data = new CompressedPermissionInPlayer();
                gson.toJson(data, writer);
            } catch (Exception e) {
                FernServerInit.LOGGER.error("[Fern] Failed to create data file!");
            }
        } else {
            try (FileReader reader = new FileReader(baseData)) {
                CompressedPermissionInPlayer data = gson.fromJson(reader, CompressedPermissionInPlayer.class);
            } catch (Exception e) {
                FernServerInit.LOGGER.error("[Fern] Failed to read data file!");
            }
        }
    }
}
