package org.db.apicore.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class Reporter {

    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

    // Start a new test (called from your local project @BeforeMethod)
    public static void startTest(String testName) {
        ExtentTest test = ExtentReportHandler.createTest(testName);
        currentTest.set(test);
    }

    // Retrieve the current test
    private static ExtentTest getTest() {
        return currentTest.get();
    }

    // Logging helpers
    public static void info(String message) {
        getTest().log(Status.INFO, message);
    }

    public static void pass(String message) {
        getTest().log(Status.PASS, message);
    }

    public static void fail(String message) {
        getTest().log(Status.FAIL, message);
    }

    public static void warning(String message) {
        getTest().log(Status.WARNING, message);
    }

    // Cleanup after test
    public static void endTest() {
        currentTest.remove();
    }
}
