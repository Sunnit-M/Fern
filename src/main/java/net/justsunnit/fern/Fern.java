package net.justsunnit.fern;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class Fern  {
    public static boolean check(@NotNull ServerPlayerEntity player,@NotNull String permission) {
        return FernServerInit.data.checkPermission(player.getUuidAsString(), permission);
    }

    public static boolean check(@NotNull ServerCommandSource source,@NotNull String permission) {
        if(!source.isExecutedByPlayer()) return true;
        return FernServerInit.data.checkPermission(source.getPlayer().getUuidAsString(), permission);
    }

    public static boolean check(@NotNull String uuid,@NotNull String permission) {
        return FernServerInit.data.checkPermission(uuid, permission);
    }

    public static boolean check(@NotNull GameProfile profile,@NotNull String permission) {
        return FernServerInit.data.checkPermission(profile.id().toString(), permission);
    }

    public static boolean checkGroup(@NotNull ServerPlayerEntity player,@NotNull String groupName) {
        return FernServerInit.data.checkGroupPermission(groupName, player.getUuidAsString());
    }

    public static boolean checkGroup(@NotNull ServerCommandSource source,@NotNull String groupName) {
        if(!source.isExecutedByPlayer()) return true;
        return FernServerInit.data.checkGroupPermission(groupName, source.getPlayer().getUuidAsString());
    }

    public static boolean checkGroup(@NotNull String uuid,@NotNull String groupName) {
        return FernServerInit.data.checkGroupPermission(groupName, uuid);
    }

    public static boolean checkGroup(@NotNull GameProfile profile,@NotNull String groupName) {
        return FernServerInit.data.checkGroupPermission(groupName, profile.id().toString());
    }

    public static void setPermission(@NotNull ServerPlayerEntity player, @NotNull String permission) {
        FernServerInit.data.addPermission(player.getUuidAsString(), permission, player.getName().getLiteralString());
    }

    public static void setPermission(@NotNull GameProfile profile, @NotNull String permission) {
        FernServerInit.data.addPermission(profile.id().toString(), permission, profile.name());
    }

    public static void setPermission(@NotNull ServerCommandSource source, @NotNull String permission) {
        if(!source.isExecutedByPlayer()) return;
        FernServerInit.data.addPermission(source.getPlayer().getUuidAsString(), permission, source.getPlayer().getName().getLiteralString());
    }

    public static Predicate<ServerCommandSource> require(@NotNull String permission) {
        return source -> check(source, permission);
    }
}