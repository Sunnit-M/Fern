package net.justsunnit.fern.DataType;

import net.justsunnit.fern.DataTypes.BaseDataType;
import net.justsunnit.fern.DataTypes.PermissionInPlayer;
import net.justsunnit.fern.FernServerInit;
import net.justsunnit.fern.Utility.ConfigHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class PermissionInPlayerTest {
    BaseDataType data = new PermissionInPlayer();

    @BeforeEach
    void setUp() {
        data = new PermissionInPlayer();
        ConfigHandler.reload();

        data.addPermission("1234567890abcdef", "test.group.permission", "JohnTest");
        data.addPermission("1234567890abcdef", "test.group.permit", "JohnTest");
        data.addPermission("1234567890abcdef", "admin.kick", "JohnTest");
        data.addPermission("1234567890abcdef", "admin.ban", "JohnTest");
        data.addPermission("1234567890abcdef", "chat.color", "JohnTest");
        data.addPermission("1234567890abcdef", "world.edit", "JohnTest");

        data.addPermission("abcdef1234567890", "another.permission.node", "JaneDoe");
        data.addPermission("abcdef1234567890", "test.group.permission", "JaneDoe");
        data.addPermission("abcdef1234567890", "chat.mute", "JaneDoe");
        data.addPermission("abcdef1234567890", "chat.unmute", "JaneDoe");
        data.addPermission("abcdef1234567890", "economy.pay", "JaneDoe");
        data.addPermission("abcdef1234567890", "economy.balance", "JaneDoe");

        data.addPermission("fedcba9876543210", "moderator.warn", "ModUser");
        data.addPermission("fedcba9876543210", "moderator.kick", "ModUser");
        data.addPermission("fedcba9876543210", "moderator.mute", "ModUser");
        data.addPermission("fedcba9876543210", "moderator.unmute", "ModUser");
        data.addPermission("fedcba9876543210", "logs.view", "ModUser");

        data.addPermission("0011223344556677", "builder.place", "BuilderOne");
        data.addPermission("0011223344556677", "builder.break", "BuilderOne");
        data.addPermission("0011223344556677", "builder.rotate", "BuilderOne");
        data.addPermission("0011223344556677", "builder.copy", "BuilderOne");

        data.addPermission("8899aabbccddeeff", "player.home.set", "CasualPlayer");
        data.addPermission("8899aabbccddeeff", "player.home.tp", "CasualPlayer");
        data.addPermission("8899aabbccddeeff", "player.spawn", "CasualPlayer");
        data.addPermission("8899aabbccddeeff", "player.warp", "CasualPlayer");
        long before = System.currentTimeMillis();
        data.write();
        long after = System.currentTimeMillis();
        FernServerInit.LOGGER.info("Data write completed in " + (after - before) + " ms");
    }

    @Test
    void permissionManipulationTest() {
        try {
            List<String> lines = Files.readAllLines(BaseDataType.baseData.toPath());
            for (String line : lines) {
                FernServerInit.LOGGER.info(line);
            }
        } catch (IOException e) {
            throw new AssertionError("Failed to read config file for testing.");
        };

    }
}
