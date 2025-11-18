package net.justsunnit.fern;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;

import net.justsunnit.fern.DataTypes.PlayerDirectory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class Fern  {
    /**
     * Checks if the player has the specified permission.
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param player the player to check permission for
     * @param permission the permission node to check
     * @return true if player has permission, false if not
     */
    public static boolean check(@NotNull ServerPlayerEntity player,@NotNull String permission) {
        return FernServerInit.data.checkPermission(player.getUuidAsString(), permission);
    }
    
    /**
     * Checks if the player has the specified permission.
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param source the server command source to check permission for
     * @param permission the permission node to check
     * @return true if player has permission, false if not
     */
    public static boolean check(@NotNull ServerCommandSource source,@NotNull String permission) {
        if(!source.isExecutedByPlayer()) return true;
        return FernServerInit.data.checkPermission(source.getPlayer().getUuidAsString(), permission);
    }

    /**
     * Checks if the player has the specified permission.
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param uuid the uuid of the player to check permission for
     * @param permission the permission node to check
     * @return true if player has permission, false if not
     */
    public static boolean check(@NotNull String uuid,@NotNull String permission) {
        return FernServerInit.data.checkPermission(uuid, permission);
    }
    
    /**
     * Checks if the player has the specified permission.
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param profile the game profile of the player to check permission for (For possibly offline players)
     * @param permission the permission node to check
     * @return true if player has permission, false if not
     */
    public static boolean check(@NotNull GameProfile profile,@NotNull String permission) {
        return FernServerInit.data.checkPermission(profile.id().toString(), permission);
    }


    /**
     * Checks if the player has the specified permission level 
     * Example permission format: "(ModId).(Group(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param player the player to check permission for
     * @param groupName the permission group to check
     * @return true if player has permission, false if not
     */
    public static boolean checkGroup(@NotNull ServerPlayerEntity player,@NotNull String groupName) {
        return FernServerInit.data.checkGroupPermission(groupName, player.getUuidAsString());
    }
    
    /**
     * Checks if the player has the specified permission level
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param source the server command source to check permission for
     * @param groupName the permission group to check
     * @return true if player has permission, false if not
     */
    public static boolean checkGroup(@NotNull ServerCommandSource source,@NotNull String groupName) {
        if(!source.isExecutedByPlayer()) return true;
        return FernServerInit.data.checkGroupPermission(groupName, source.getPlayer().getUuidAsString());
    }

    /**
     * Checks if the player has the specified permission level
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param uuid the uuid of the player to check permission for
     * @param groupName the permission group to check
     * @return true if player has permission, false if not
     */
    public static boolean checkGroup(@NotNull String uuid,@NotNull String groupName) {
        return FernServerInit.data.checkGroupPermission(groupName, uuid);
    }

    /**
     * Checks if the player has the specified permission level
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param profile the game profile of the player to check permission for (For possibly offline players)
     * @param groupName the permission group to check
     * @return true if player has permission, false if not
     */
    public static boolean checkGroup(@NotNull GameProfile profile,@NotNull String groupName) {
        return FernServerInit.data.checkGroupPermission(groupName, profile.id().toString());
    }

    /**
     * gives the player the specified permission.
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param player the player to give the permission to
     * @param permission the permission group to check
     */
    public static void addPermission(@NotNull ServerPlayerEntity player, @NotNull String permission) {
        FernServerInit.data.addPermission(player.getUuidAsString(), permission, player.getName().getLiteralString());
    }

    /**
     * gives the player the specified permission.
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param profile the game profile of the player to give the permission to (For possibly offline players)
     * @param permission the permission group to check
     */
    public static void addPermission(@NotNull GameProfile profile, @NotNull String permission) {
        FernServerInit.data.addPermission(profile.id().toString(), permission, profile.name());
    }

    /**
     * gives the player the specified permission.
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param source the server command source to give the permission to
     * @param permission the permission group to check
     */
    public static void addPermission(@NotNull ServerCommandSource source, @NotNull String permission) {
        if(!source.isExecutedByPlayer()) return;
        FernServerInit.data.addPermission(source.getPlayer().getUuidAsString(), permission, source.getPlayer().getName().getLiteralString());
    }

    /**
     * checks if the player has the specified permission. (For use is command registration)
     * Example permission format: "(ModId).(BasePermissionLevel(Owner/Admin/Mod/Member)).(SpecificPermission)"
     * @param permission the permission group to check
     */
    public static Predicate<ServerCommandSource> require(@NotNull String permission) {
        return source -> check(source, permission);
    }

    /**
     * gets the player's username from their UUID
     * @param uuid the uuid of the player
     * @return the player's username
     */
    public static String getPlayerUserFromUUID(@NotNull String uuid) {
        return PlayerDirectory.getUsername(uuid);
    }

    /**
     * gets the player's UUID from their username
     * @param playerUser the username of the player
     * @return the player's UUID
     */
    public static String getUUIDFromPlayerUser(@NotNull String playerUser) {
        return PlayerDirectory.getUUID(playerUser);
    }
}