package net.justsunnit.fern.DataTypes;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import net.justsunnit.fern.Fern;
import net.justsunnit.fern.FernServerInit;
import net.justsunnit.fern.Utility.ConfigHandler;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
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
        else {
            playerData pData = new playerData(PlayerUser, new ArrayList<>(List.of(permission)));
            data.put(UUID, pData);
        }
    }

    @Override
    public boolean checkPermission(String UUID, String permission) {
        checkFile();
        if(data.containsKey(UUID)){
            playerData pData = data.get(UUID);
            ArrayList<String> perms = pData.permissions;

            if(perms.contains("*")){
                return true;
            }

            if(perms.contains("-*")){
                return false;
            }

            if(perms.contains("-" + permission)){
                return false;
            }

            if(perms.contains(permission)){
                return true;
            }

            String[] permissionParts = permission.split("\\.");
            StringBuilder wildcardPermission = new StringBuilder();
            for (int i = permissionParts.length - 1; i >= 0; i--) {
                wildcardPermission.append(permissionParts[i]);
                if(perms.contains(wildcardPermission.toString() + ".*")){
                    return true;
                }
                wildcardPermission.append(".");
            }
        }
        return false;
    }

    @Deprecated
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
        baseData.getParentFile().mkdirs();
        switch (ConfigHandler.loaderType) {
            case gson:
                try (FileWriter writer = new FileWriter(baseData)) {
                    gson.toJson(this.data, writer);
                } catch (Exception e) {
                    FernServerInit.LOGGER.error("[Fern] Failed to write data file!");
                }
                break;
            case jackson:
                try (JsonGenerator gen = jacksonFactory.createGenerator(baseData, JsonEncoding.UTF8)) {
                    gen.setPrettyPrinter(new DefaultPrettyPrinter());
                    gen.writeStartObject();
                    for (String UUID : data.keySet()) {
                        playerData pData = data.get(UUID);
                        gen.writeFieldName(UUID);
                        gen.writeStartObject();
                        gen.writeStringField("playerUser", pData.playerUser);
                        gen.writeFieldName("permissions");
                        gen.writeStartArray();
                        for (String permission : pData.permissions) {
                            gen.writeString(permission);
                        }
                        gen.writeEndArray();
                        gen.writeEndObject();
                    }
                    gen.writeEndObject();
                    gen.close();
                }
                catch (Exception e){
                    FernServerInit.LOGGER.error("[Fern] Failed to write data file with Jackson!");
                }
        }
    }

    @Override
    public void load() {
        if (!baseData.exists()) {
            emptyWrite();
        } else {
            switch (ConfigHandler.loaderType) {
                case jackson:
                    try (JsonParser p = jacksonFactory.createParser(baseData)) {
                        int objectIndex = 0;
                        boolean inArray = false;
                        String currentUUID = null;
                        String currentPlayerUser = null;
                        ArrayList<String> currentPermissions = new ArrayList<>();

                        HashMap<String, playerData> data = new PermissionInPlayer().data;
                        while(p.nextToken() != null){
                            JsonToken currentToken = p.currentToken();
                            switch (currentToken){
                                case JsonToken.START_OBJECT:
                                    objectIndex++;
                                    break;
                                case JsonToken.START_ARRAY:
                                    inArray = true;
                                    break;
                                case JsonToken.END_ARRAY:
                                    inArray = false;
                                    break;
                                case JsonToken.FIELD_NAME:
                                    if(!inArray){
                                        p.nextToken();
                                        currentUUID = p.getValueAsString();
                                    }
                                    break;
                                case JsonToken.VALUE_STRING:
                                    if(inArray){
                                        currentPermissions.add(p.getValueAsString());
                                    }
                                    else{
                                        currentPlayerUser = p.getValueAsString();
                                    }
                                    break;
                                case JsonToken.END_OBJECT:
                                    objectIndex--;
                                    if(objectIndex == 1){
                                        if(currentUUID != null && currentPlayerUser != null && currentPermissions != null){
                                            this.data = new PermissionInPlayer().data;
                                            break;
                                        }
                                        data.put(currentUUID, new playerData(currentPlayerUser, currentPermissions));
                                        currentPermissions = new ArrayList<>();
                                        currentUUID = null;
                                        currentPlayerUser = null;
                                        break;
                                    }
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        FernServerInit.LOGGER.error("[Fern] Failed to read data file with Jackson!");
                    }
                    break;
                case gson:
                    try (FileReader reader = new FileReader(baseData)) {
                        PermissionInPlayer data = gson.fromJson(reader, PermissionInPlayer.class);
                        this.data = data.data;
                    } catch (Exception e) {
                        FernServerInit.LOGGER.error("[Fern] Failed to read data file!");
                    }
                    break;
            }
        }
    }

    @Override
    public void emptyWrite() {
        baseData.getParentFile().mkdirs();
        switch (ConfigHandler.portLoaderTypeTo) {
            case gson:
                try (FileWriter writer = new FileWriter(baseData)) {
                    gson.toJson(data, writer);
                    ConfigHandler.loaderType = Fern.LOADER_TYPE.gson;
                    ConfigHandler.save();
                } catch (Exception e) {
                    FernServerInit.LOGGER.error("[Fern] Failed to create data file!");
                    e.printStackTrace();
                }
                break;
            case jackson:
                try (JsonGenerator gen = jacksonFactory.createGenerator(baseData, JsonEncoding.UTF8)) {
                    gen.writeStartObject();
                    gen.writeEndObject();
                    gen.close();
                    ConfigHandler.loaderType = Fern.LOADER_TYPE.jackson;
                    ConfigHandler.save();
                } catch (Exception e) {
                    FernServerInit.LOGGER.error("[Fern] Failed to create data file!");
                    e.printStackTrace();
                }
                break;
        }
    }
}
