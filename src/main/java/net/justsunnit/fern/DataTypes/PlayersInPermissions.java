package net.justsunnit.fern.DataTypes;

import com.fasterxml.jackson.core.*;
import net.justsunnit.fern.FernServerInit;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public PlayersInPermissions(PermissionInPlayer data){
        this.data = new HashMap<>();
        data.data.forEach((UUID, pData) -> {
            for(String permission : pData.permissions){
                if(this.data.containsKey(permission)){
                    ArrayList<PlayerData> players = this.data.get(permission);
                    players.add(new PlayerData(pData.playerUser, UUID));
                }
                else{
                    ArrayList<PlayerData> players = new ArrayList<>();
                    players.add(new PlayerData(pData.playerUser, UUID));
                    this.data.put(permission, players);
                }
            }
        });
    }

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
        baseData.getParentFile().mkdirs();
        switch (FernServerInit.loaderType){
            case gson:
                try(FileWriter writer = new FileWriter(baseData)){
                    gson.toJson(this.data, writer);
                } catch (Exception e) {
                    FernServerInit.LOGGER.error("[Fern] Failed to write data file!");
                }
                break;
            case jackson:
                try(JsonGenerator gen = jacksonFactory.createGenerator(baseData, JsonEncoding.UTF8)){
                    gen.writeStartObject();
                    for(String permission : data.keySet()){
                        gen.writeArrayFieldStart(permission);
                        for(PlayerData pData : data.get(permission)){
                            gen.writeStartObject();
                            gen.writeStringField("playerUser", pData.playerUser);
                            gen.writeStringField("UUID", pData.UUID);
                            gen.writeEndObject();
                        }
                        gen.writeEndArray();
                    }
                    gen.writeEndObject();
                } catch (IOException e) {
                    FernServerInit.LOGGER.error("[Fern] Failed to write data file!");
                }
        }

    }

    @Override
    public void load() {
        if (!baseData.exists()) {
            emptyWrite();
        } else {
            switch (FernServerInit.loaderType) {
                case gson:
                    try (FileReader reader = new FileReader(baseData)) {
                        PlayersInPermissions data = gson.fromJson(reader, PlayersInPermissions.class);
                        this.data = data.data;
                    } catch (Exception e) {
                        FernServerInit.LOGGER.error("[Fern] Failed to read data file!");
                    }
                    break;
                case jackson:
                    try (JsonParser p = jacksonFactory.createParser(baseData)) {
                        HashMap<String, ArrayList<PlayerData>> data = new HashMap<>();
                        String currentPerm = null;
                        ArrayList<PlayerData> currentPlayers = null;
                        PlayerData currentPlayer = null;
                        int objectIndex = 0;
                        boolean inArray = false;
                        while (p.nextToken() != null) {
                            JsonToken currentToken = p.currentToken();
                            switch (currentToken) {
                                case JsonToken.START_OBJECT:
                                    objectIndex++;
                                    break;
                                case JsonToken.START_ARRAY:
                                    inArray = true;
                                    break;
                                case JsonToken.END_ARRAY:
                                    inArray = false;
                                    data.put(currentPerm, currentPlayers);
                                    currentPerm = null;
                                    currentPlayers = null;
                                    break;
                                case JsonToken.FIELD_NAME:
                                    if (!inArray) {
                                        currentPerm = p.getCurrentName();
                                    } else if (objectIndex == 2) {
                                        switch (p.getCurrentName()) {
                                            case "playerUser":
                                                p.nextToken();
                                                if (currentPlayer == null) {
                                                    currentPlayer = new PlayerData(p.getValueAsString(), null);
                                                } else {
                                                    currentPlayer.playerUser = p.getValueAsString();
                                                }
                                            case "UUID":
                                                p.nextToken();
                                                if (currentPlayer == null) {
                                                    currentPlayer = new PlayerData(null, p.getValueAsString());
                                                } else {
                                                    currentPlayer.UUID = p.getValueAsString();
                                                }
                                        }

                                    }
                                    break;
                                case JsonToken.END_OBJECT:
                                    objectIndex--;
                                    if (objectIndex == 0) {
                                        this.data = data;
                                        break;
                                    }
                                    else if (objectIndex == 1) {
                                        if (currentPlayer != null) {
                                            if (currentPlayers == null) {
                                                currentPlayers = new ArrayList<>();
                                                currentPlayers.add(currentPlayer);
                                            }
                                        }
                                        currentPlayer = null;
                                    }
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public void emptyWrite() {
        baseData.getParentFile().mkdirs();
        switch (FernServerInit.loaderType){
            case gson:
                try (FileWriter writer = new FileWriter(baseData)) {
                    PlayersInPermissions data = new PlayersInPermissions();
                    this.data = data.data;
                    gson.toJson(data, writer);
                } catch (Exception e) {
                    FernServerInit.LOGGER.error("[Fern] Failed to create data file!");
                }
                break;
            case jackson:
                try (JsonGenerator gen = jacksonFactory.createGenerator(baseData, JsonEncoding.UTF8)){
                    PlayersInPermissions data = new PlayersInPermissions();
                    this.data = data.data;
                    gen.writeStartObject();
                    gen.writeEndObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
        try (FileWriter writer = new FileWriter(baseData)) {
            PlayersInPermissions data = new PlayersInPermissions();
            this.data = data.data;
            gson.toJson(data, writer);
        } catch (Exception e) {
            FernServerInit.LOGGER.error("[Fern] Failed to create data file!");
        }
    }

    @Override
    public boolean checkPermission(String UUID, String permission) {
        checkFile();
        if(data.containsKey(permission)){
            if(data.get(permission).stream().anyMatch(pd -> pd.UUID.equals(UUID))) {return true;}
        }
        if(data.containsKey("*")){
            if(data.get("*").stream().anyMatch(pd -> pd.UUID.equals(UUID))) {return true;}
        }
        if(data.containsKey("-*")){
            if(data.get("-*").stream().anyMatch(pd -> pd.UUID.equals(UUID))) {return true;}
        }
        if(data.containsKey("-" + permission)){
            if(data.get("-" + permission).stream().anyMatch(pd -> pd.UUID.equals(UUID))) {return true;}
        }
        String[] permissionParts = permission.split("\\.");
        StringBuilder wildcardPermission = new StringBuilder();
        for (int i = permissionParts.length - 1; i >= 0; i--) {
            wildcardPermission.append(permissionParts[i]);
            if(data.containsKey(wildcardPermission.toString() + ".*")){
                if(data.get(wildcardPermission.toString() + ".*").stream().anyMatch(pd -> pd.UUID.equals(UUID))) {return true;}
            }
            if(data.containsKey("-" + wildcardPermission.toString() + ".*")){
                if(data.get("-" + wildcardPermission.toString() + ".*").stream().anyMatch(pd -> pd.UUID.equals(UUID))) {return true;}
            }
            wildcardPermission.append(".");
        }
        return false;
    }

    @Deprecated
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
