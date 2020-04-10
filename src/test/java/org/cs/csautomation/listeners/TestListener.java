/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.listeners;

import java.util.Arrays;
import org.cs.csautomation.cs.utility.ApplicationConfiguration;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.listeners.adapters.TestAdapter;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * TestNG Test Listener class extending the Adapter class.
 * 
 * @author CSAutomation Team
 *
 */
public class TestListener extends TestAdapter {

    private ExtentReports    extentReportInstance;
    private ExtentTest       extentTestInstance;
    ApplicationConfiguration config;

    /**
     * Invoked after the test class is instantiated and before any configuration
     * method is called.
     */
    @Override
    public void onStart(ITestContext context) {
        ISuite suiteInstance = context.getSuite();
        extentReportInstance = (ExtentReports) suiteInstance
                .getAttribute("extentReport");
        config = (ApplicationConfiguration) suiteInstance
                .getAttribute("configurations");
    }

    /**
     * Invoked after all the tests have run and all their Configuration methods
     * have been called.
     */
    @Override
    public void onFinish(ITestContext context) {
        extentReportInstance.endTest(extentTestInstance);
    }

    /**
     * Invoked each time before a test will be invoked. The
     * <code>ITestResult</code> is only partially filled with the references to
     * class, method, start millis and status.
     *
     * @param result the partially filled <code>ITestResult</code>
     * @see ITestResult#STARTED
     */
    @Override
    public void onTestStart(ITestResult result) {
        extentTestInstance = extentReportInstance
                .startTest(result.getMethod().getMethodName());
        extentTestInstance.log(LogStatus.INFO,
                result.getMethod().getMethodName() + "test is started");
    }

    /**
     * Invoked each time a test succeeds.
     *
     * @param result <code>ITestResult</code> containing information about the
     *            run test
     * @see ITestResult#SUCCESS
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        extentTestInstance.log(LogStatus.PASS,
                result.getMethod().getMethodName() + "test is passed");
    }

    /**
     * Invoked each time a test fails.
     *
     * @param result <code>ITestResult</code> containing information about the
     *            run test
     * @see ITestResult#FAILURE
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String path = config.getScreenshotPath();
        String screenshotPath = CSUtility
                .captureScreenshotWithRobot(result.getName(), path);
        extentTestInstance.log(LogStatus.FAIL,
                result.getMethod().getMethodName()
                        + "test is failed, parameters : "
                        + Arrays.toString(result.getParameters()));
        extentTestInstance.log(LogStatus.FAIL,
                result.getMethod().getMethodName(),
                extentTestInstance.addScreenCapture(screenshotPath));
    }

    /**
     * Invoked each time a test is skipped.
     *
     * @param result <code>ITestResult</code> containing information about the
     *            run test
     * @see ITestResult#SKIP
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        extentTestInstance.log(LogStatus.SKIP,
                result.getMethod().getMethodName() + "test is skipped");
    }
}
