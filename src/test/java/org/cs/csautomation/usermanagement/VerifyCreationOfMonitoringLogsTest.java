/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.MonitoringPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This test case checks if logs have been created in list view Under monitoring
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyCreationOfMonitoringLogsTest extends AbstractTest {

    private CSPortalHeader csPortalHeader;
    private WebDriverWait  waitForReload;
    private MonitoringPage monitoringPage;
    private FrameLocators  locator;

    /**
     * This test case checks if logs have been created in list view Under
     * Monitoring
     */
    @Test(priority = 1)
    public void testCreationOfMonitoringLogs() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            clkLogsInMonitoring();
            verifyLogsCreation();
        } catch (Exception e) {
            CSLogger.debug("Test case failed ", e);
            Assert.fail("Test case failed.");
        }
    }

    /**
     * This method clicks on Logs Node Under Monitoring
     */
    private void clkLogsInMonitoring() {
        TraversingForSettingsModule
                .traverseToNodesInLeftPaneOfSystemPreferences(waitForReload,
                        browserDriver);
        monitoringPage.clkMonitoringNode(waitForReload);
        monitoringPage.clkLogsNode(waitForReload);
        monitoringPage.getLogsNode().click();
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method verifies if logs have created or not
     */
    private void verifyLogsCreation() {
        try {
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameMain()));
            List<WebElement> list = browserDriver.findElements(By.xpath(
                    "//table[@class='hidewrap CSAdminList']/tbody/tr/td[4]"));
            if (list.size() >= 1) {
                CSLogger.info("Logs have been created.");
            }
        } catch (Exception e) {
            CSLogger.debug("Logs have not been created.", e);
            Assert.fail("Logs have not been created" + e);
        }
    }

    /**
     * This method initializes resources to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        monitoringPage = new MonitoringPage(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
    }
}
