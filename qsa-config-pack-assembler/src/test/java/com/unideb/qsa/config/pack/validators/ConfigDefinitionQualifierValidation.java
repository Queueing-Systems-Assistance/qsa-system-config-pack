package com.unideb.qsa.config.pack.validators;

import java.util.Map;

import com.unideb.qsa.config.pack.log.Log;
import com.unideb.qsa.config.pack.pack.ConfigPack;
import com.unideb.qsa.config.pack.pack.ConfigPackFile;
import com.unideb.qsa.domain.context.ConfigDefinition;

/**
 * Validates config definitions based on their qualifiers.
 */
public class ConfigDefinitionQualifierValidation {

    private static final String QUALIFIER_ERROR = "Qualifier %s %s in [%s] is different from the expected value [%s]";
    private static final String INVALID_QUALIFIER = "CONFIG DEFINITION - INVALID QUALIFIER";
    private static final String CONFIG_QUALIFIER_NAME = "name";

    /**
     * Validate if the config has qualifiers and if not, then no qualifier is used to resolve the value.
     * @param configPack config pack
     */
    public void validate(ConfigPack configPack) {
        configPack.getConfigDefinitions()
                  .entrySet()
                  .stream()
                  .filter(entry -> !isDefaultConfigDefinition(entry.getValue()))
                  .forEach(this::validateConfigPackQualifier);
    }

    private void validateConfigPackQualifier(Map.Entry<ConfigPackFile, ConfigDefinition> entry) {
        ConfigPackFile configPackFile = entry.getKey();
        ConfigDefinition configDefinition = entry.getValue();
        failTestKey(configPackFile, configDefinition);
        failTestValue(configPackFile, configDefinition);
    }

    private void failTestValue(ConfigPackFile configPackFile, ConfigDefinition configDefinition) {
        String qualifierValue = configPackFile.getConfigPath().getParentFile().getName();
        if (!configDefinition.getQualifiers().containsValue(qualifierValue)) {
            Log.logFail(INVALID_QUALIFIER, String.format(QUALIFIER_ERROR,
                    "key value",
                    configDefinition.getQualifiers().values(),
                    configPackFile.getRootPath().toURI().relativize(configPackFile.getConfigPath().toURI()).getPath(),
                    qualifierValue));
        }
    }

    private void failTestKey(ConfigPackFile configPackFile, ConfigDefinition configDefinition) {
        if (!configDefinition.getQualifiers().containsKey(CONFIG_QUALIFIER_NAME)) {
            Log.logFail(INVALID_QUALIFIER, String.format(QUALIFIER_ERROR,
                    "key",
                    configDefinition.getQualifiers().keySet().iterator().next(),
                    configPackFile.getRootPath().toURI().relativize(configPackFile.getConfigPath().toURI()).getPath(),
                    CONFIG_QUALIFIER_NAME));
        }
    }

    private boolean isDefaultConfigDefinition(ConfigDefinition configDefinition) {
        return configDefinition.getQualifiers().isEmpty();
    }
}
