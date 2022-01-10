package com.unideb.qsa.config.pack.retriever;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.unideb.qsa.domain.context.ConfigPack;
import com.unideb.qsa.domain.deserializer.JsonDeserializerHelper;

/**
 * Handler for AWS Lambda calls.
 */
public class EventHandler implements RequestHandler<String, List<ConfigPack>> {

    @Override
    public List<ConfigPack> handleRequest(String input, Context context) {
        var path = getConfigPath();
        try {
            String content = Files.lines(path).collect(Collectors.joining("\n"));
            return List.of(JsonDeserializerHelper.deserializeToConfigPack(content));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read config pack", e);
        }
    }

    private Path getConfigPath() {
        return Paths.get("config-pack.json");
    }

}
