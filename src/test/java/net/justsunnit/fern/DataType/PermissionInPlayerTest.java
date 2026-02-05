package net.justsunnit.fern.DataType;

import net.justsunnit.fern.DataTypes.BaseDataType;
import net.justsunnit.fern.DataTypes.PermissionInPlayer;
import net.justsunnit.fern.Fern;
import net.justsunnit.fern.FernServerInit;
import net.justsunnit.fern.Utility.ConfigHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
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
        data.addPermission("000000000000000xsd", "haha.balls.kaboom", "TimmyTuffTest");
        data.removePermission("fedcba9876543210", "logs.view");
        data.write();
        String[] expected = """
        {
          "1234567890abcdef": {
            "playerUser": "JohnTest",
            "permissions": [
              "test.group.permission",
              "test.group.permit",
              "admin.kick",
              "admin.ban",
              "chat.color",
              "world.edit"
            ]
          },
          "abcdef1234567890": {
            "playerUser": "JaneDoe",
            "permissions": [
              "another.permission.node",
              "test.group.permission",
              "chat.mute",
              "chat.unmute",
              "economy.pay",
              "economy.balance"
            ]
          },
          "000000000000000xsd": {
            "playerUser": "TimmyTuffTest",
            "permissions": [
              "haha.balls.kaboom"
            ]
          },
          "fedcba9876543210": {
            "playerUser": "ModUser",
            "permissions": [
              "moderator.warn",
              "moderator.kick",
              "moderator.mute",
              "moderator.unmute"
            ]
          },
          "0011223344556677": {
            "playerUser": "BuilderOne",
            "permissions": [
              "builder.place",
              "builder.break",
              "builder.rotate",
              "builder.copy"
            ]
          },
          "8899aabbccddeeff": {
            "playerUser": "CasualPlayer",
            "permissions": [
              "player.home.set",
              "player.home.tp",
              "player.spawn",
              "player.warp"
            ]
          }
        }        
        """.lines().toArray(String[]::new);
        try {
            String[] actual = Files.readAllLines(BaseDataType.baseData.toPath()).toArray(String[]::new);
            Assertions.assertArrayEquals(expected, actual);
        } catch (IOException e) {
            throw new AssertionError("Failed To load data");
        }
    }

    @Test
    void PortToJackson() {

    }
}
