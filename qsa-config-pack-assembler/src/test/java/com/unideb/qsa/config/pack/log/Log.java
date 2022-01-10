package com.unideb.qsa.config.pack.log;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;

/**
 * Class for console logging.
 */
public final class Log {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String EMPTY_LINE = "";

    private Log() {
    }

    private static void write(String heading, String color) {
        Reporter.log(color, true);
        Reporter.log(heading, true);
        Reporter.log(ANSI_RESET, true);
    }

    private static void write(String heading, Collection<String> messages, String color) {
        Reporter.log(color, true);
        Reporter.log(heading, true);
        messages.forEach(message -> Reporter.log(message, true));
        Reporter.log(ANSI_RESET, true);
    }

    /**
     * Logs with info level.
     * @param text text
     */
    public static void logInfo(String text) {
        Reporter.log(ANSI_GREEN + text + ANSI_RESET, true);
    }

    /**
     * Logs with warning level.
     * @param text text
     */
    public static void logWarning(String text) {
        Reporter.log(ANSI_BLUE + text + ANSI_RESET, true);
    }

    /**
     * Logs with warning level.
     * @param heading  heading text
     * @param warnings warnings as a collection
     */
    public static void logWarning(String heading, Collection<String> warnings) {
        write(heading, warnings, ANSI_BLUE);
    }

    /**
     * Logs with fail level.
     * @param heading   heading text
     * @param throwable exception
     */
    public static void logFail(String heading, Throwable throwable) {
        write(heading, List.of(throwable.getMessage()), ANSI_RED);
        Assert.fail();
    }

    /**
     * Logs with fail level.
     * @param heading heading text
     * @param error   error text
     */
    public static void logFail(String heading, String error) {
        write(heading, ANSI_RED);
        Reporter.log(error, true);
        Assert.fail();
    }

    /**
     * Logs with fail level.
     * @param heading heading text
     * @param errors  errors as a map (key should be the error text and value the exception)
     */
    public static void logFail(String heading, Map<String, Throwable> errors) {
        Reporter.log(ANSI_RED + heading + EMPTY_LINE, true);
        errors.forEach((key, value) -> {
            Reporter.log(key, true);
            Reporter.log(value.getMessage(), true);
        });
        Reporter.log(ANSI_RESET, true);
        Assert.fail();
    }
}
