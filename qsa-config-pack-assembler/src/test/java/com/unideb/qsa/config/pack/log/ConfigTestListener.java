package com.unideb.qsa.config.pack.log;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Logging listener when the test runs. Whenever a test starts/ends or skipped, it will be logged into the console.
 */
public final class ConfigTestListener implements ITestListener {

    private static final String VALIDATION_STARTED = "Started Configuration Pack Validator";
    private static final String VALIDATION_ENDED = "Configuration Pack Validator Finished";
    private static final String TEST_STARTED = "Test [START] [%s]";
    private static final String TEST_SKIPPED = "Test [SKIPPED] [%s]";
    private static final String EMPTY_LINE = "";

    @Override
    public void onStart(ITestContext context) {
        Log.logInfo(VALIDATION_STARTED);
        Log.logInfo(EMPTY_LINE);
    }

    @Override
    public void onFinish(ITestContext context) {
        Log.logInfo(EMPTY_LINE);
        Log.logInfo(VALIDATION_ENDED);
    }

    @Override
    public void onTestStart(ITestResult result) {
        Log.logInfo(String.format(TEST_STARTED, result.getName()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Log.logWarning(String.format(TEST_SKIPPED, result.getName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }
}
