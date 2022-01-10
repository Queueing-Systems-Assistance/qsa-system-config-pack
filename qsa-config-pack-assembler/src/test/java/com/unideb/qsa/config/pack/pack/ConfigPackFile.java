package com.unideb.qsa.config.pack.pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents single configuration file.
 */
public final class ConfigPackFile {

    private static final String CONFIG_ROOT_DIRECTORY = "config";
    private static final String NOT_A_FILE_ERROR = "Config %s is not a file.";
    private static final String FILE_CANNOT_READ = "File cannot read";

    private final File configPath;
    private final File rootPath;

    private String content;

    public ConfigPackFile(File configPath) throws IllegalArgumentException {
        this.configPath = configPath;
        rootPath = getRootPath(configPath);
    }

    /**
     * Returns the content of the config file.
     * @return content of the config file.
     */
    public String getContent() {
        if (content == null) {
            if (!configPath.isFile()) {
                throw new IllegalArgumentException(String.format(NOT_A_FILE_ERROR, configPath));
            }
            try {
                content = Files.readString(configPath.toPath());
            } catch (IOException e) {
                throw new RuntimeException(FILE_CANNOT_READ, e);
            }
        }
        return content;
    }

    /**
     * Get a config path.
     * @return a file of the config path
     */
    public File getConfigPath() {
        return configPath;
    }

    /**
     * Get a config root path.
     * @return a file of the config root path (the path does not contain the config file itself)
     */
    public File getRootPath() {
        return rootPath;
    }

    /**
     * Get a config file name.
     * @return the config file name.
     */
    public String getFileName() {
        return configPath.getName();
    }

    private File getRootPath(File configPath) {
        List<File> list = new ArrayList<>(Collections.singletonList(configPath));
        IntStream.iterate(0, index -> !CONFIG_ROOT_DIRECTORY.equals(list.get(index).getName()), index -> index + 1)
                 .forEach(index -> list.add(list.get(index).getParentFile()));
        return list.get(list.size() - 1).getParentFile();
    }
}
