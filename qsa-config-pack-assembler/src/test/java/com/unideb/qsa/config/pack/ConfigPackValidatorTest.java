package com.unideb.qsa.config.pack;

import java.util.Collection;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.unideb.qsa.config.pack.log.ConfigTestListener;
import com.unideb.qsa.config.pack.pack.ConfigPack;
import com.unideb.qsa.config.pack.pack.ConfigPackFile;
import com.unideb.qsa.config.pack.pack.ConfigPackFileLoader;
import com.unideb.qsa.config.pack.validators.ConfigDefinitionConfigValidation;
import com.unideb.qsa.config.pack.validators.ConfigDefinitionDefaultValueValidation;
import com.unideb.qsa.config.pack.validators.ConfigDefinitionQualifierValidation;
import com.unideb.qsa.config.pack.validators.DirectoryStructureValidator;
import com.unideb.qsa.config.pack.validators.JsonValidator;
import com.unideb.qsa.config.pack.validators.schema.JsonSchemaBasedValidator;

/**
 * Config packs should extend this class as part of the test phase - this will validate the config pack.
 */
@Listeners(ConfigTestListener.class)
public abstract class ConfigPackValidatorTest {

    private static final String CONFIG_SCHEMA_PATH = "/config-schema.json";
    private static final String CONFIG_LOCATION_PATTERN = "classpath*:/config/**/*";

    private final JsonSchemaBasedValidator jsonSchemaBasedValidator = new JsonSchemaBasedValidator();
    private final JsonValidator jsonValidator = new JsonValidator();
    private final ConfigDefinitionDefaultValueValidation configDefinitionDefaultValueValidation = new ConfigDefinitionDefaultValueValidation();
    private final ConfigDefinitionConfigValidation configDefinitionConfigValidation = new ConfigDefinitionConfigValidation();
    private final ConfigDefinitionQualifierValidation configDefinitionQualifierValidation = new ConfigDefinitionQualifierValidation();
    private final DirectoryStructureValidator directoryStructureValidator = new DirectoryStructureValidator();
    private ConfigPack configPack;
    private Collection<ConfigPackFile> configFiles;

    @Test
    public void readConfigurationFiles() {
        configFiles = new ConfigPackFileLoader(CONFIG_LOCATION_PATTERN).getConfigFiles();
    }

    @Test(dependsOnMethods = "readConfigurationFiles")
    public void jsonSyntaxValidation() {
        jsonValidator.validate(configFiles);
    }

    @Test(dependsOnMethods = "jsonSyntaxValidation")
    public void schemaValidation() {
        jsonSchemaBasedValidator.validate(configFiles, CONFIG_SCHEMA_PATH);
    }

    @Test(dependsOnMethods = "schemaValidation")
    public void deserializableValidation() {
        configPack = new ConfigPack(configFiles);
    }

    @Test(dependsOnMethods = "deserializableValidation")
    public void configDefinitionDefaultValueValidation() {
        configDefinitionDefaultValueValidation.validate(configPack);
    }

    @Test(dependsOnMethods = "configDefinitionDefaultValueValidation")
    public void configDefinitionConfigValidation() {
        configDefinitionConfigValidation.validate(configPack);
    }

    @Test(dependsOnMethods = "configDefinitionConfigValidation")
    public void configDefinitionQualifierValidation() {
        configDefinitionQualifierValidation.validate(configPack);
    }

    @Test(dependsOnMethods = "configDefinitionQualifierValidation")
    public void rootDirectoryValidation() {
        directoryStructureValidator.validate(configFiles);
    }
}
