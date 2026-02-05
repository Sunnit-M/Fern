package net.justsunnit.fern.Utility;

import net.justsunnit.fern.Fern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ConfigHandlerTest {
    @Test
    void loadTest() {
        try(FileWriter writer = new FileWriter(ConfigHandler.configFile)){
            writer.write("""
                    // Only chnage the port to fields as other fields are managed by the mod.
                    {
                      "portDataTypeTo": 0,
                      "currentDatatype": 0,
                      "currentLoaderType": "gson",
                      "portLoaderTypeTo": "gson"
                    }
                    """);
        } catch (IOException e) {
            throw new AssertionError("Failed to set up config file for testing.");
        }
        ConfigHandler.load();
        Assertions.assertEquals(0, ConfigHandler.dataType);
        Assertions.assertEquals(0, ConfigHandler.portDataTypeTo);
        Assertions.assertEquals(Fern.LOADER_TYPE.gson, ConfigHandler.loaderType);
        Assertions.assertEquals(Fern.LOADER_TYPE.gson, ConfigHandler.portLoaderTypeTo);

        try(FileWriter writer = new FileWriter(ConfigHandler.configFile)){
            writer.write("""
                    // Only chnage the port to fields as other fields are managed by the mod.
                    {
                      "portDataTypeTo": 1,
                      "currentDatatype": 0,
                      "currentLoaderType": "gson",
                      "portLoaderTypeTo": "jackson"
                    }
                    """);
        } catch (IOException e) {
            throw new AssertionError("Failed to set up config file for testing.");
        }
        ConfigHandler.reload();
        Assertions.assertEquals(0, ConfigHandler.dataType);
        Assertions.assertEquals(1, ConfigHandler.portDataTypeTo);
        Assertions.assertEquals(Fern.LOADER_TYPE.gson, ConfigHandler.loaderType);
        Assertions.assertEquals(Fern.LOADER_TYPE.jackson, ConfigHandler.portLoaderTypeTo);
    }

    @Test
    void saveTest() {
        ConfigHandler.dataType = 1;
        ConfigHandler.portDataTypeTo = 0;
        ConfigHandler.loaderType = Fern.LOADER_TYPE.jackson;
        ConfigHandler.portLoaderTypeTo = Fern.LOADER_TYPE.gson;
        ConfigHandler.save();
        try {
            List<String> lines = Files.readAllLines(ConfigHandler.configFile.toPath());
            Assertions.assertEquals("// Only chnage the port to fields as other fields are managed by the mod.",lines.getFirst().trim());
            Assertions.assertEquals("{", lines.get(1).trim());
            Assertions.assertEquals("\"portDataTypeTo\": 0,", lines.get(2).trim());
            Assertions.assertEquals("\"currentDatatype\": 1,", lines.get(3).trim());
            Assertions.assertEquals("\"currentLoaderType\": \"jackson\",", lines.get(4).trim());
            Assertions.assertEquals("\"portLoaderTypeTo\": \"gson\"", lines.get(5).trim());
            Assertions.assertEquals("}", lines.get(6).trim());
        } catch (IOException e) {
            throw new AssertionError("Failed to read config file for testing.");
        }
    }
}
