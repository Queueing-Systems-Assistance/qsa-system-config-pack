package com.unideb.qsa.config.pack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Converts a config pack of separate files to a single config pack file.
 */
public class AssemblerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssemblerApplication.class);

    private static final String CONFIG_LOCATION_PATTERN = "classpath*:/config/**/*";
    private static final String RESULT_FILE = "build" + File.separator + "config-pack.json";

    private static final AssemblerApplication AGGREGATOR = new AssemblerApplication();
    private static final PathMatchingResourcePatternResolver PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    private final JsonObject configPack = new JsonObject();

    /**
     * Entry point to the assembler.
     * @param args arguments
     * @throws IOException when cannot read/write configs
     */
    public static void main(String... args) throws IOException {
        AGGREGATOR.assemble(PATTERN_RESOLVER.getResources(CONFIG_LOCATION_PATTERN));
        AGGREGATOR.save();
    }

    private void assemble(Resource[] resources) {
        configPack.add("config", loadConfigs(resources));
    }

    private void save() throws IOException {
        LOGGER.info("Writing config pack to [{}]", AssemblerApplication.RESULT_FILE);
        Files.write(Paths.get(AssemblerApplication.RESULT_FILE), configPack.toString().getBytes(Charsets.UTF_8));
    }

    private JsonArray loadConfigs(Resource[] resources) {
        JsonArray jsonArray = new JsonArray();
        Arrays.stream(resources)
              .filter(Resource::isReadable)
              .forEach(resource -> {
                  try (InputStream inputStream = resource.getInputStream()) {
                      String content = IOUtils.toString(inputStream, Charsets.UTF_8);
                      JsonElement jsonElement = JsonParser.parseString(content);
                      jsonArray.add(jsonElement);
                  } catch (IOException e) {
                      throw new RuntimeException(e);
                  }
              });
        return jsonArray;
    }
}

