/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import org.cs.csautomation.cs.settings.ActiveJobsPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to verify active job with reports
 * 
 * @author CSAutomation Team
 */
public class VerifyJobsWithReportsTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private FrameLocators  locator;
    private CSPortalHeader csPortalHeader;
    private String         ActiveScriptSheet = "CreateActiveScript";
    private ActiveJobsPage activeJobsPage;
    private SoftAssert     softAssertion;

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        CSLogger.info("Initialized the POM object for "
                + "CreateNewActiveScriptTest page");
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 180);
        activeJobsPage = SettingsLeftSectionMenubar
                .getActiveJobsNode(browserDriver);
        softAssertion = new SoftAssert();
    }

    /**
     * This test method will create a new active job, which is a pre-requisite
     * for running TC-315.
     * 
     * Test method takes input from the data provider.
     * 
     * @param activeJobLabel
     *            - user defined label for new active job
     * @param activeJobCategory
     *            - user defined category for new active job
     * 
     */
    @Test(dataProvider = "createNewActiveScriptData")
    public void testCreateNewActiveJob(String activeJobLabel,
            String activeJobCategory) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            CSLogger.info("clicked on setting header");
            TraversingForSettingsModule.traversetoSystemPreferences(
                    waitForReload, browserDriver, locator);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPage.getSystemPreferences());
            activeJobsPage.clkWebElement(activeJobsPage.getSystemPreferences());
            CSLogger.info("clicked on system preferences");
            TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                    waitForReload, browserDriver, locator);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPage.getNodeActiveJobs());
            activeJobsPage.clkWebElement(activeJobsPage.getNodeActiveJobs());
            CSLogger.info("clicked on active jobs");
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPage.getBtnPresets());
            Actions action = new Actions(browserDriver);
            action.doubleClick(activeJobsPage.getBtnPresets()).build()
                    .perform();
            CSLogger.info("clicked on presets");        
            CSUtility.tempMethodForThreadSleep(1000);
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, locator);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPage.getBtnCreateNewActiveJob());
            activeJobsPage
                    .clkWebElement(activeJobsPage.getBtnCreateNewActiveJob());
            CSLogger.info("clicked on createNew for active job");
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                    browserDriver, locator);
            activeJobsPage.enterTextIntoTextbox(waitForReload,
                    activeJobsPage.getTxtNewActiveScriptLabel(),
                    activeJobLabel);
            CSLogger.info("Entered the label for active job");
            activeJobsPage.clkWebElement(activeJobsPage.getBtnSaveActiveJob());
            CSLogger.info("clicked on save button for active job");
            waitForReload.until(ExpectedConditions.visibilityOf(
                    activeJobsPage.getTxtNewActiveScriptCategory()));
            activeJobsPage.enterTextIntoTextbox(waitForReload,
                    activeJobsPage.getTxtNewActiveScriptCategory(),
                    activeJobCategory);
            CSLogger.info("Entered the category for active job");
            waitForReload.until(ExpectedConditions.visibilityOf(
                    activeJobsPage.getDrpDwnSelectActiveScript()));
            activeJobsPage.getDrpDwnSelectActiveScript().click();
            CSLogger.info("clicked on drop down menu "
                    + "for selecting script for active job");
            activeJobsPage.clkWebElement(
                    activeJobsPage.getOptionHouseKeepingScript());
            CSLogger.info("selected Housekeepingscript option from drop down");
            CSLogger.info("NEW ACTIVE JOB SUCCESSFULLY CREATED.");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : ", e);
        }
    }

    /**
     * This test method is for TC-315 'verify jobs with reports'
     * 
     * Here we will verify following functionalities viz. pause button,
     * interruption button, generation of logs, graphs & php script for recently
     * ran script.
     * 
     * @param activeJobLabel
     *            - user defined label for new active job
     * @param activeJobCategory
     *            - user defined category for new active job
     */
    @Test(dependsOnMethods = "testCreateNewActiveJob", dataProvider = "createNewActiveScriptData")
    private void testVerifyJobsWithReports(String activeJobLabel,
            String activeJobCategory) {
        try {
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                    browserDriver, locator);
            activeJobsPage.runTheActiveScript(waitForReload, browserDriver);
            verifyPlayPauseButton();
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                    browserDriver, locator);
            activeJobsPage.runTheActiveScript(waitForReload, browserDriver);
            verifyInterruptionButton();
            verifyLogs(activeJobLabel);
            verifyReport(activeJobLabel);
            verifyPhpScript();
            softAssertion.assertAll();
            CSUtility.switchToDefaultFrame(browserDriver);
            CSLogger.info("Test case execution completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : ", e);
        }
    }

    /**
     * This method will verify & assert whether php script is getting generated
     * or not, for selected active script
     */
    private void verifyPhpScript() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnScript());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnScript());
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmForPhpScript()));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getPhpScript());
        boolean phpScript = activeJobsPage.getPhpScript().isDisplayed();
        if (phpScript) {
            CSLogger.info("PHP script is displayed for active script. "
                    + "Successfully verified presence of php script!");
        } else {
            CSLogger.error("ERROR! php script is not displayed for "
                    + "selected active script!");
        }
        softAssertion.assertTrue(phpScript,
                "ERROR! php script is not displayed for "
                        + "selected active script!");
    }

    /**
     * This method will verify & assert the generation of report for active
     * script run
     * 
     * @param activeJobLabel
     *            - user defined label for new active job
     */
    private void verifyReport(String activeJobLabel) {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnReport());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnReport());
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmReport()));
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement scriptReportTitle = browserDriver.findElement(
                By.xpath("//div[contains(text(),'" + activeJobLabel + "')]"));
        boolean reportTitleVisibility = scriptReportTitle.isDisplayed();
        if (reportTitleVisibility) {
            CSLogger.info("Report is getting generated");
            CSUtility.tempMethodForThreadSleep(2000);
            WebElement activeScriptJobTitle = browserDriver.findElement(
                    By.xpath("//div[contains(text(),'ActiveScript \""
                            + activeJobLabel + "\" during last 10 Minutes')]"));
            boolean activeScriptJobTitleVisibility = activeScriptJobTitle
                    .isDisplayed();
            if (activeScriptJobTitleVisibility) {
                CSLogger.info("Graphs are getting generated. "
                        + "Graphical representation of logs is successful!");
            } else {
                CSLogger.error(
                        "ERROR! Graphs are not getting generated for reports!");
            }
            softAssertion.assertTrue(activeScriptJobTitleVisibility,
                    "ERROR! Graphs are not getting generated for reports!");
        } else {
            CSLogger.error("ERROR! Report is not getting generated!");
        }
        softAssertion.assertTrue(reportTitleVisibility,
                "ERROR! Report is not getting generated!");
    }

    /**
     * This method will verify & assert whether logs are getting generated or
     * not
     * 
     * @param activeJobLabel
     *            - user defined label for new active job
     */
    private void verifyLogs(String activeJobLabel) {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnJobs());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnJobs());
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmJobsState()));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getImgStateIcon());
        activeJobsPage.clkWebElement(activeJobsPage.getImgStateIcon());
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getJobId());
        String activeJobID = activeJobsPage.getJobId().getText();
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmShowingStatusOfRunningActiveScript()));
        CSLogger.info("switched to frame 'bottom', showing status of "
                + "running active script");
        WebElement logsTitle = browserDriver.findElement(
                By.xpath("//nobr[contains(text(),'" + activeJobID + "')]"));
        boolean logsVisibility = logsTitle.isDisplayed();
        if (logsVisibility) {
            CSLogger.info(
                    "Logs are getting generated & are displayed in 'jobs' tab");
        } else {
            CSLogger.error("ERROR! Logs are not getting generated!");
        }
        softAssertion.assertTrue(logsVisibility,
                "ERROR! Logs are not getting generated!");
    }

    /**
     * This method will verify & assert the functionality of 'interruption'
     * button for script
     */
    private void verifyInterruptionButton() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmShowingStatusOfRunningActiveScript()));
        CSLogger.info("switched to frame 'bottom', showing status of "
                + "running active script");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnInterruption());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnInterruption());
        CSLogger.info("clicked on interruption button");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getTxtMsgInterruptionRequested());
        boolean interruptionMessageVisibility = activeJobsPage
                .getTxtMsgInterruptionRequested().isDisplayed();
        if (interruptionMessageVisibility) {
            CSLogger.info("Interruption requested!");
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPage.getTxtMsgInterruptionAccepted());
            boolean jobTerminationMsg = activeJobsPage
                    .getTxtMsgInterruptionAccepted().isDisplayed();
            if (jobTerminationMsg) {
                CSLogger.info("Interruption accepted");
                CSLogger.info("Script terminated. Interruption button working "
                        + "properly. Verified interruption "
                        + "button successfully");
            } else {
                CSLogger.info("Error! Script did not get terminated! "
                        + "Interruption button is not working properly!");
            }
            softAssertion.assertTrue(jobTerminationMsg,
                    "Error! Script did not get terminated! "
                            + "Interruption button is not working properly!");
        } else {
            CSLogger.info("Error! Interruption cannot be requested! "
                    + "Interruption button is not working properly!");
        }
        softAssertion.assertTrue(interruptionMessageVisibility,
                "Error! Interruption button is not working properly!");
    }

    /**
     * This method will verify & assert the functionality of script 'pause'
     * button
     */
    private void verifyPlayPauseButton() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmShowingStatusOfRunningActiveScript()));
        CSLogger.info("switched to frame 'bottom', showing status of "
                + "running active script");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnPauseScriptRun());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnPauseScriptRun());
        CSLogger.info("clicked on pause button");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getTxtMsgPauseRequested());
        boolean pauseMessageVisibility = activeJobsPage
                .getTxtMsgPauseRequested().isDisplayed();
        if (pauseMessageVisibility) {
            CSLogger.info(
                    "Script is getting paused. Pause button working properly. "
                            + "Verified pause button successfully!");
        } else {
            CSLogger.error("Error! Pause button is not working properly!");
        }
        String pauseButtonStatus = "Script run did not pause! "
                + "Error in pause button!";
        softAssertion.assertTrue(pauseMessageVisibility, pauseButtonStatus);
    }

    /**
     * This data provider returns active job label and active job category.
     * 
     * @return array of active job data
     * 
     */
    @DataProvider(name = "createNewActiveScriptData")
    public Object[][] CreateNewActiveScriptData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                ActiveScriptSheet);
    }
}
