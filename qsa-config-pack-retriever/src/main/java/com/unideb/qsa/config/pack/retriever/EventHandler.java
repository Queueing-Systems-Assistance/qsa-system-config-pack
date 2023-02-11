package com.unideb.qsa.config.pack.retriever;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Handler for AWS Lambda calls.
 */
public class EventHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        var path = getConfigPath();
        try {
            return Files.lines(path).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read config pack", e);
        }
    }

    private Path getConfigPath() {
        return Paths.get("config-pack.json");
    }

}
