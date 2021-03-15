package com.unideb.qsa.config.pack.pack;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.unideb.qsa.config.pack.log.Log;
import com.unideb.qsa.domain.context.ConfigDefinition;
import com.unideb.qsa.domain.deserializer.JsonDeserializerHelper;

/**
 * This class is differs from the {@link com.unideb.qsa.domain.context.ConfigPack}, because for testing it's easier to contain the config file and the parsed
 * content in the same class.
 */
public final class ConfigPack {

    private static final String DESERIALIZATION_FAILED = "DESERIALIZATION FAILED";
    private static final String CANNOT_PARSE_CONFIG_FILE = "Cannot parse [%s] config file.";
    private static final String NEVER_THROWN = "Never thrown";

    private final Map<ConfigPackFile, ConfigDefinition> configDefinitions;

    public ConfigPack(Collection<ConfigPackFile> configFiles) {
        configDefinitions = createConfigDefinitions(configFiles);
    }

    /**
     * Get the config definitions.
     * @return a map where the key is the config file, and the corresponding value is the parsed config definition
     */
    public Map<ConfigPackFile, ConfigDefinition> getConfigDefinitions() {
        return configDefinitions;
    }

    private Map<ConfigPackFile, ConfigDefinition> createConfigDefinitions(Collection<ConfigPackFile> configFiles) {
        return configFiles.stream().collect(Collectors.toMap(configPackFile -> configPackFile, this::deserializeJsonToConfigDefinition));
    }

    private ConfigDefinition deserializeJsonToConfigDefinition(ConfigPackFile configPackFile) {
        try {
            return JsonDeserializerHelper.deserializeToConfigDefinition(configPackFile.getContent());
        } catch (Exception e) {
            Log.logFail(DESERIALIZATION_FAILED, String.format(CANNOT_PARSE_CONFIG_FILE, configPackFile.getFileName()));
        }
        throw new IllegalStateException(NEVER_THROWN);
    }
}
