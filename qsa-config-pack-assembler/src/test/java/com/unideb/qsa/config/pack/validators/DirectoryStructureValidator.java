package com.unideb.qsa.config.pack.validators;

import java.io.File;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.unideb.qsa.config.pack.log.Log;
import com.unideb.qsa.config.pack.pack.ConfigPackFile;

/**
 * Validates config definitions based on their location.
 */
public class DirectoryStructureValidator {

    private static final String ROOT_DIRECTORY_ERROR = "[%s] can contain only [%s] directories, but [%s] found";
    private static final String ALLOWED_MAIN_DIRECTORY = "config";
    private static final String INVALID_ROOT_DIRECTORIES = "INVALID ROOT DIRECTORIES";

    /**
     * Validate if the config is in the right location.
     * @param configDefinitionFiles config definitions
     */
    public void validate(Collection<ConfigPackFile> configDefinitionFiles) {
        getRootDirectories(configDefinitionFiles).forEach(this::validateChildrenFiles);
    }

    private void validateChildrenFiles(File root) {
        Stream.of(Objects.requireNonNull(root.listFiles()))
              .filter(file -> !file.isDirectory() || !file.getName().equals(ALLOWED_MAIN_DIRECTORY))
              .map(File::getName)
              .forEach(name -> Log.logFail(INVALID_ROOT_DIRECTORIES, String.format(ROOT_DIRECTORY_ERROR, root, ALLOWED_MAIN_DIRECTORY, name)));
    }

    private Collection<File> getRootDirectories(Collection<ConfigPackFile> configDefinitionFiles) {
        return configDefinitionFiles.stream()
                                    .map(ConfigPackFile::getRootPath)
                                    .collect(Collectors.toList());
    }
}
