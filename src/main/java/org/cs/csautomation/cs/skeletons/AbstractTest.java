/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.skeletons;

import org.cs.csautomation.cs.datadriven.DataDrivenScript;
import org.cs.csautomation.cs.utility.ApplicationConfiguration;
import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * This is the abstract class which will act as root class for every test class,
 * providing needed resources.
 * 
 * @author CSAutomation Team
 *
 */
public abstract class AbstractTest {

    protected WebDriver                browserDriver;
    protected ApplicationConfiguration config;
    protected DataDrivenScript         DrivenScript;

    /**
     * This method perform operations required before the test begins.
     * 
     * @param context Context Instance
     */
    @BeforeClass
    public void preProcessor(ITestContext context) {
        ISuite suite = context.getSuite();
        browserDriver = (WebDriver) suite.getAttribute("driverForBrowser");
        DrivenScript = new DataDrivenScript();
        config = (ApplicationConfiguration) suite
                .getAttribute("configurations");
        CSLogger.setClass(this);
        CSLogger.startTestCase(this.getClass().getSimpleName());
    }

    /**
     * This method perform operations required after the test of a class have
     * ended.
     */
    @AfterClass
    public void postProcessor() {
        CSLogger.endTestCase(this.getClass().getSimpleName());
    }

    /**
     * Returns the webdriver instance.
     * 
     * @return browserDriver instance
     */
    public WebDriver getBrowserDriver() {
        return browserDriver;
    }

    /**
     * Returns the ApplicationConfiguration Instance.
     * 
     * @return config ApplicationConfiguration
     */
    public ApplicationConfiguration getConfig() {
        return config;
    }

    /**
     * This is returns the data driven class to read the data from the xlsx file
     * 
     * @return object of data driven
     */
    public DataDrivenScript getExcelDataDriven() {
        return DrivenScript;
    }
}
