
package org.cs.csautomation.listeners.adapters;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * This is an Abstract class which act as an Adapter class for Test Listener.
 * 
 * @author CSAutomation Team
 *
 */
public abstract class TestAdapter implements ITestListener {

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
        /*
         * This method will be given definition in future as and when required.
         */
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
        /*
         * This method will be given definition in future as and when required.
         */
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
        /*
         * This method will be given definition in future as and when required.
         */
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
        /*
         * This method will be given definition in future as and when required.
         */
    }

    /**
     * Invoked each time a method fails but has been annotated with
     * successPercentage and this failure still keeps it within the success
     * percentage requested.
     *
     * @param result <code>ITestResult</code> containing information about the
     *            run test
     * @see ITestResult#SUCCESS_PERCENTAGE_FAILURE
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        /*
         * This method will be given definition in future as and when required.
         */
    }

    /**
     * Invoked after the test class is instantiated and before any configuration
     * method is called.
     */
    @Override
    public void onStart(ITestContext context) {
        /*
         * This method will be given definition in future as and when required.
         */
    }

    /**
     * Invoked after all the tests have run and all their Configuration methods
     * have been called.
     */
    @Override
    public void onFinish(ITestContext context) {
        /*
         * This method will be given definition in future as and when required.
         */
    }

}
