package net.justsunnit.fern.DataTypes;

import net.justsunnit.fern.Fern;
import net.justsunnit.fern.FernServerInit;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PermissionInPlayer implements BaseDataType {
    public static class playerData {
        public String playerUser;
        public ArrayList<String> permissions;

        public playerData(String playerUser, ArrayList<String> permissions) {
            this.playerUser = playerUser;
            this.permissions = permissions;
        }
    }

    public HashMap<String, playerData> data;

    public PermissionInPlayer() {
        this.data = new HashMap<>();
    }

    public PermissionInPlayer(PlayersInPermissions data){
        this.data = new HashMap<>();
        data.data.forEach((permission, players) -> {
            for(PlayersInPermissions.PlayerData pData : players){
                if(this.data.containsKey(pData.UUID)){
                    playerData existingData = this.data.get(pData.UUID);
                    if(!existingData.permissions.contains(permission)){
                        existingData.permissions.add(permission);
                    }
                }
                else{
                    this.data.put(pData.UUID, new playerData(pData.playerUser, new ArrayList<>(List.of(permission))));
                }
            }
        });
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
    }

    @Override
    public boolean checkPermission(String UUID, String permission) {
        checkFile();
        if(data.containsKey(UUID)){
            playerData pData = data.get(UUID);
            return (pData.permissions.contains(permission) || (pData.permissions.contains("*") && !pData.permissions.contains("-" + permission)));
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
    }

    @Override
    public void setPlayerUser(String UUID, String playerUser) {
        checkFile();
        if(data.containsKey(UUID)){
            playerData pData = data.get(UUID);
            pData.playerUser = playerUser;
        }
        else{
            playerData pData = new playerData(playerUser, new ArrayList<>(List.of()));
            data.put(UUID, pData);
        }
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
            emptyWrite();
        } else {
            try (FileReader reader = new FileReader(baseData)) {
                PermissionInPlayer data = gson.fromJson(reader, PermissionInPlayer.class);
                this.data = data.data;
            } catch (Exception e) {
                FernServerInit.LOGGER.error("[Fern] Failed to read data file!");
            }
        }
    }

    @Override
    public void emptyWrite() {
        baseData.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(baseData)) {
            PermissionInPlayer data = new PermissionInPlayer();
            this.data = data.data;
            gson.toJson(data, writer);
        } catch (Exception e) {
            FernServerInit.LOGGER.error("[Fern] Failed to create data file!");
            e.printStackTrace();
        }
    }
}
