package com.unideb.qsa.config.pack.validators.schema;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringEscapeUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.unideb.qsa.config.pack.log.Log;
import com.unideb.qsa.config.pack.pack.ConfigPackFile;

/**
 * Validates config json files against their schema.
 */
public final class JsonSchemaBasedValidator {

    private static final String UNABLE_TO_READ_CONFIG_SCHEMA = "Unable to read config schema";
    private static final String SCHEMA_VALIDATION = "JSON Schema Validation";

    /**
     * Validate config files based on the given schema.
     * @param configFiles configs
     * @param schemaPath  schema.
     */
    public void validate(Collection<ConfigPackFile> configFiles, String schemaPath) {
        configFiles.stream()
                   .map(configFile -> configFileAgainstSchemaValidator(configFile, getSchemaByPath(schemaPath)))
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .forEach(stringThrowableMap -> Log.logFail(SCHEMA_VALIDATION, stringThrowableMap));
    }

    private Optional<Map<String, Throwable>> configFileAgainstSchemaValidator(ConfigPackFile configPackFile, Schema schema) {
        Optional<Map<String, Throwable>> result = Optional.empty();
        try {
            String fileContent = configPackFile.getContent();
            String unescapedFileContent = StringEscapeUtils.builder(new EscapedUnicodeToUnescapedUnicodeTranslator()).escape(fileContent).toString();
            JSONObject jsonConfig = new JSONObject(new JSONTokener(unescapedFileContent));
            schema.validate(jsonConfig);
        } catch (ValidationException | JSONException e) {
            result = Optional.of(Map.of(configPackFile.getFileName(), e));
        }
        return result;
    }

    private Schema getSchemaByPath(String schemaPath) {
        try (InputStream inputStream = new PathMatchingResourcePatternResolver().getResource(schemaPath).getInputStream()) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            return SchemaLoader.load(rawSchema);
        } catch (IOException e) {
            Log.logFail(UNABLE_TO_READ_CONFIG_SCHEMA, e);
        }
        throw new RuntimeException(UNABLE_TO_READ_CONFIG_SCHEMA);
    }
}
