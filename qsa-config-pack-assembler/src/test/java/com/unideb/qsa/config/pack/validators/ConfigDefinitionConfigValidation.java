package com.unideb.qsa.config.pack.validators;

import java.util.Map;

import com.unideb.qsa.config.pack.log.Log;
import com.unideb.qsa.config.pack.pack.ConfigPack;
import com.unideb.qsa.config.pack.pack.ConfigPackFile;
import com.unideb.qsa.domain.context.ConfigDefinition;

/**
 * Validates config definitions based on their names.
 */
public class ConfigDefinitionConfigValidation {

    private static final String CONFIG_ERROR = "Config name [%s] in [%s] is different from the file name [%s]";
    private static final String INVALID_CONFIG = "CONFIG DEFINITION - INVALID CONFIG";

    /**
     * Validate if the config has the right name.
     * @param configPack config pack
     */
    public void validate(ConfigPack configPack) {
        configPack.getConfigDefinitions()
                  .entrySet()
                  .forEach(this::validateConfigValue);
    }

    private void validateConfigValue(Map.Entry<ConfigPackFile, ConfigDefinition> entry) {
        ConfigPackFile configPackFile = entry.getKey();
        ConfigDefinition configDefinition = entry.getValue();
        if (!configPackFile.getFileName().startsWith(configDefinition.getName())) {
            failTest(configPackFile, configDefinition);
        }
    }

    private void failTest(ConfigPackFile configPackFile, ConfigDefinition configDefinition) {
        Log.logFail(INVALID_CONFIG, String.format(CONFIG_ERROR,
                configDefinition.getName(),
                configPackFile.getRootPath().toURI().relativize(configPackFile.getConfigPath().toURI()).getPath(),
                configPackFile.getFileName()));
    }
}
