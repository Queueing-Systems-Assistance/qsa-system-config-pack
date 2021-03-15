package com.unideb.qsa.config.pack.pack;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.unideb.qsa.config.pack.log.Log;

/**
 * Loads the configs from the given location.
 */
public final class ConfigPackFileLoader {

    private static final PathMatchingResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final String CANNOT_LOAD_CONFIGS = "Cannot load configs";

    private final String configLocationPatter;

    public ConfigPackFileLoader(String configLocationPatter) {
        this.configLocationPatter = configLocationPatter;
    }

    /**
     * Get configuration files.
     * @return a collection with config files.
     */
    public Collection<ConfigPackFile> getConfigFiles() {
        return createConfigPackFiles(configLocationPatter);
    }

    private Collection<ConfigPackFile> createConfigPackFiles(String locationPattern) {
        Collection<Resource> configPackResources = findResources(locationPattern);
        return toConfigurationFiles(configPackResources);
    }

    private Collection<Resource> findResources(String locationPattern) {
        Collection<Resource> result = List.of();
        try {
            Resource[] resourceArray = RESOURCE_RESOLVER.getResources(locationPattern);
            result = List.of(resourceArray);
        } catch (IOException e) {
            Log.logFail(CANNOT_LOAD_CONFIGS, e);
        }
        return result;
    }

    private Collection<ConfigPackFile> toConfigurationFiles(Collection<Resource> resources) {
        return resources.stream()
                        .map(this::mapToFile)
                        .filter(File::isFile)
                        .map(ConfigPackFile::new)
                        .collect(Collectors.toList());
    }

    private File mapToFile(final Resource resource) {
        try {
            return new File(resource.getURL().getFile());
        } catch (IOException e) {
            Log.logFail(CANNOT_LOAD_CONFIGS, e);
        }
        throw new RuntimeException(CANNOT_LOAD_CONFIGS);
    }
}
