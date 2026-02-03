package net.justsunnit.fern.DataTypes;

import net.justsunnit.fern.FernServerInit;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayersInPermissions implements BaseDataType {
    public static class PlayerData {
        String playerUser;
        String UUID;

        public PlayerData(String playerUser, String UUID) {
            this.playerUser = playerUser;
            this.UUID = UUID;
        }
    }

    public HashMap<String, ArrayList<PlayerData>> data;
    public PlayersInPermissions() { this.data = new HashMap<>(); }

    @Override
    public void addPermission(String UUID, String permission, String PlayerUser) {
        checkFile();
        if(data.containsKey(permission)){
            data.get(permission).add(new PlayerData(PlayerUser, UUID));
        }
        else{
            ArrayList<PlayerData> pData = new ArrayList<>();
            pData.add(new PlayerData(PlayerUser, UUID));
            data.put(permission, pData);
        }

        write();
    }

    @Override
    public void removePermission(String UUID, String permission) {
        checkFile();
        if(data.containsKey(permission)){
            ArrayList<PlayerData> pData = data.get(permission);
            pData.removeIf(playerData -> playerData.UUID.equals(UUID));
        }
    }

    @Override
    public void setPlayerUser(String UUID, String playerUser) {
        checkFile();
        data.values().stream().flatMap(ArrayList::stream).forEach(playerData -> {
            if(playerData.UUID.equals(UUID)){
                playerData.playerUser = playerUser;
            }
        });
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
                PlayersInPermissions data = new PlayersInPermissions();
                this.data = data.data;
                gson.toJson(data, writer);
            } catch (Exception e) {
                FernServerInit.LOGGER.error("[Fern] Failed to create data file!");
            }
        } else {
            try (FileReader reader = new FileReader(baseData)) {
                PlayersInPermissions data = gson.fromJson(reader, PlayersInPermissions.class);
                this.data = data.data;
            } catch (Exception e) {
                FernServerInit.LOGGER.error("[Fern] Failed to read data file!");
            }
        }
    }

    @Override
    public boolean checkPermission(String UUID, String permission) {
        checkFile();
        if(data.containsKey(permission)){
            ArrayList<PlayerData> pData = data.get(permission);
            return pData.stream().anyMatch(playerData -> playerData.UUID.equals(UUID));
        }
        return false;
    }

    @Override
    public boolean checkGroupPermission(String groupName, String UUID) {
        checkFile();
        for (String permission : data.keySet()) {
            if (permission.startsWith(groupName)) {
                ArrayList<PlayerData> pData = data.get(permission);
                if (pData.stream().anyMatch(playerData -> playerData.UUID.equals(UUID))) {
                    return true;
                }
            }
        }
        return false;
    }
}
