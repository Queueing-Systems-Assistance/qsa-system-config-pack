package com.unideb.qsa.config.pack.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.unideb.qsa.config.pack.log.Log;
import com.unideb.qsa.config.pack.pack.ConfigPack;
import com.unideb.qsa.domain.context.ConfigDefinition;

/**
 * Validates config definitions based on their values.
 */
public class ConfigDefinitionDefaultValueValidation {

    private static final String NO_DEFAULT_VALUE_WARN = "NO DEFAULT value found for [%s] config, under [%s]";
    private static final String MISSING_DEFAULT_VALUES = "WARNING: Missing Default Values";

    /**
     * Validate if the config has a default value. If the default value cannot be found, warning with the config is logged.
     * @param configPack config pack
     */
    public void validate(ConfigPack configPack) {
        Collection<ConfigDefinition> defaultConfigDefinitions = getDefaultConfigDefinitions(configPack.getConfigDefinitions().values());
        Collection<String> missingDefaultValues = configPack
                .getConfigDefinitions()
                .values()
                .stream()
                .filter(configDefinition -> !isDefaultConfigDefinition(configDefinition))
                .filter(configDefinition -> !hasConfigDefinitionDefaultValue(configDefinition))
                .filter(configDefinition -> !hasConfigDefinitionDefaultConfigDefinition(defaultConfigDefinitions, configDefinition))
                .map(configDefinition -> String.format(NO_DEFAULT_VALUE_WARN, configDefinition.getName(), configDefinition.getQualifiers()))
                .collect(Collectors.toCollection(ArrayList::new));
        if (!missingDefaultValues.isEmpty()) {
            Log.logWarning(MISSING_DEFAULT_VALUES, missingDefaultValues);
        }
    }

    private boolean hasConfigDefinitionDefaultValue(ConfigDefinition configDefinition) {
        return configDefinition
                .getConfigValues()
                .stream()
                .anyMatch(configValue -> configValue.getQualifiers().isEmpty());
    }

    private boolean isDefaultConfigDefinition(ConfigDefinition configDefinition) {
        return configDefinition.getQualifiers().isEmpty();
    }

    private Collection<ConfigDefinition> getDefaultConfigDefinitions(Collection<ConfigDefinition> configPackConfigDefinitions) {
        return configPackConfigDefinitions
                .stream()
                .filter(this::isDefaultConfigDefinition)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean hasConfigDefinitionDefaultConfigDefinition(Collection<ConfigDefinition> defaultDefinitions, ConfigDefinition configDefinition) {
        return defaultDefinitions
                .stream()
                .map(ConfigDefinition::getName)
                .map(defaultDefinitionName -> defaultDefinitionName.equals(configDefinition.getName()))
                .filter(contains -> contains)
                .findAny()
                .orElse(false);
    }
}
