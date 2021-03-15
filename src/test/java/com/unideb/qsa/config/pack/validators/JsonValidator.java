package com.unideb.qsa.config.pack.validators;

import java.io.StringReader;
import java.util.Collection;
import java.util.Map;

import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import com.unideb.qsa.config.pack.log.Log;
import com.unideb.qsa.config.pack.pack.ConfigPackFile;

/**
 * Validates config files, if they are in a valid JSON structure or not.
 */
public class JsonValidator {

    private static final String INVALID_JSON = "Invalid JSON";
    private static final String NOT_VALID_JSON = "-->\t'%s' is not valid JSON.";

    /**
     * Validate if the config has a valid JSON structure.
     * @param configPackFiles config packs
     */
    public void validate(Collection<ConfigPackFile> configPackFiles) {
        configPackFiles.forEach(this::validate);
    }

    private void validate(ConfigPackFile configPackFile) {
        JsonReader jsonReader = new JsonReader(new StringReader(configPackFile.getContent()));
        jsonReader.setLenient(false);
        try {
            Streams.parse(jsonReader);
        } catch (JsonParseException e) {
            Log.logFail(INVALID_JSON, Map.of(String.format(NOT_VALID_JSON, configPackFile.getConfigPath().getPath()), e));
        }
    }
}
