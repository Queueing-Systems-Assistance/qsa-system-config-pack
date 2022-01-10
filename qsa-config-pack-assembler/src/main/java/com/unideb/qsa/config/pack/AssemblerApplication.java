package com.unideb.qsa.config.pack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Converts a config pack of separate files to a single config pack file.
 */
public class AssemblerApplication {

    private static final String CONFIG_LOCATION_PATTERN = "classpath*:/config/**/*";
    private static final String RESULT_FILE = "build" + File.separator + "config-pack.json";

    private static final AssemblerApplication AGGREGATOR = new AssemblerApplication();
    private static final PathMatchingResourcePatternResolver PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    /**
     * Entry point to the assembler.
     * @param args arguments
     * @throws IOException when cannot read/write configs
     */
    public static void main(String... args) throws IOException {
        Resource[] resources = PATTERN_RESOLVER.getResources(CONFIG_LOCATION_PATTERN);
        JsonObject configPack = AGGREGATOR.assemble(resources);
        AGGREGATOR.save(configPack);
    }

    private JsonObject assemble(Resource[] resources) {
        var configPack = new JsonObject();
        configPack.add("config", loadConfigs(resources));
        return configPack;
    }

    private void save(JsonObject configPack) throws IOException {
        Files.writeString(Paths.get(RESULT_FILE), configPack.toString(), StandardCharsets.UTF_8);
    }

    private JsonArray loadConfigs(Resource[] resources) {
        return Arrays.stream(resources)
                     .filter(Resource::isReadable)
                     .map(this::getPath)
                     .map(this::getContent)
                     .map(JsonParser::parseString)
                     .collect(JsonArray::new, JsonArray::add, JsonArray::add);
    }

    private String getContent(Path path) {
        try {
            return String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot read path [%s]", path), e);
        }
    }

    private Path getPath(Resource resource) {
        try {
            return Paths.get(resource.getURI());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot get path for resource [%s]", resource.getFilename()), e);
        }
    }
}
