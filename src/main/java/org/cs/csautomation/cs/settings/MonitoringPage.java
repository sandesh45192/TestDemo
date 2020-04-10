/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains all xpaths related to Monitoring Page
 * 
 * @author CSAutomation Team
 *
 */
public class MonitoringPage {

    public MonitoringPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Settings node page POM elements");
    }

    @FindBy(xpath = "//span[@id='Monitoring@0']")
    private WebElement monitoringNode;

    @FindBy(xpath = "//span[@id='Monitoring~Logs@0']")
    private WebElement logsNode;

    /**
     * This method returns the instance of logs node under monitoring
     * 
     * @return logsNode
     */
    public WebElement getLogsNode() {
        return logsNode;
    }

    /**
     * This method returns the instance of Monitoring node
     * 
     * @return monitoringNode
     */
    public WebElement getMonitoringNode() {
        return monitoringNode;
    }

    /**
     * This method clicks on Monitoring node under settings tab in left pane
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkMonitoringNode(WebDriverWait waitForReload) {
        waitForReload
                .until(ExpectedConditions.visibilityOf(getMonitoringNode()));
        getMonitoringNode().click();
        CSLogger.info("Clicked on Monitoring Node");
    }

    /**
     * This method clicks on logs node under Monitoring
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkLogsNode(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getLogsNode()));
        getLogsNode().click();
        CSLogger.info("Clicked on logs node under monitoring node");

    }
}
