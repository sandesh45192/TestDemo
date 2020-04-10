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
 * This class contains the test methods to create new active script job
 * 
 * @author CSAutomation Team
 */
public class CreateAndVerifyHousekeepingScriptTest extends AbstractTest {

    private WebDriverWait waitForReload;
    private FrameLocators locator;
    private CSPortalHeader csPortalHeader;
    private String ActiveScriptSheet = "CreateActiveScript";
    private ActiveJobsPage activeJobsPage;
    private SoftAssert softAssertion;

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        CSLogger.info(
                "Initialized the POM object for CreateNewActiveScriptTest page");
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 180);
        activeJobsPage = SettingsLeftSectionMenubar
                .getActiveJobsNode(browserDriver);
        softAssertion = new SoftAssert();
    }

    /**
     * This test method will create new active job. Test method takes input from
     * the data provider.
     * 
     * @param activeJobLabel
     *            - user defined label for new active job
     * @param activeJobCategory
     *            - user defined category for new active job
     * 
     */
    @Test(dataProvider = "createNewActiveScriptData")
    public void testCreateHousekeepingScript(String activeJobLabel,
            String activeJobCategory) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            CSLogger.info("clicked on setting header");
            TraversingForSettingsModule.traversetoSystemPreferences(waitForReload, browserDriver,
                    locator);			
            TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(waitForReload,
                    browserDriver, locator);
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
            TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                    browserDriver, locator);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPage.getBtnCreateNewActiveJob());
            activeJobsPage
                    .clkWebElement(activeJobsPage.getBtnCreateNewActiveJob());
            CSLogger.info("clicked on createNew for active job");
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver,
                    locator);
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
            activeJobsPage.clkWebElement(
                    activeJobsPage.getDrpDwnSelectActiveScript());
            CSLogger.info("clicked on drop down menu "
                    + "for selecting script for active job");
            activeJobsPage.clkWebElement(
                    activeJobsPage.getOptionHouseKeepingScript());
            CSLogger.info("selected Housekeepingscript option from drop down");
            CSLogger.info("TEST CASE EXECUTION COMPLETED! "
                    + "NEW ACTIVE JOB SUCCESSFULLY CREATED.");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : ", e);
        }
    }

    /**
     * This method is for TC-312 'verify housekeeping script'
     * 
     * Here we verify whether logs are getting deleted/cleared or not, based on
     * time duration given by the user.
     */
    @Test(dependsOnMethods = "testCreateHousekeepingScript")
    private void testVerifyHousekeepingScriptActiveJob() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        waitForReload.until(ExpectedConditions
                .visibilityOf(activeJobsPage.getDrpDwnDeleteOldLogs()));
        activeJobsPage.clkWebElement(activeJobsPage.getDrpDwnDeleteOldLogs());
        CSLogger.info("Expanded 'delete old logs' section");
        changeTimeDurationForDeleteOldLogs();
        waitForReload.until(ExpectedConditions
                .visibilityOf(activeJobsPage.getDrpDwnDeleteOldCacheFiles()));
        activeJobsPage
                .clkWebElement(activeJobsPage.getDrpDwnDeleteOldCacheFiles());
        CSLogger.info("Expanded 'delete old cache files' section");
        changeTimeDurationForDeleteOldCacheFiles();
        activeJobsPage.saveTheActiveScript(waitForReload,
                "DELETE OLD LOGS & DELETE OLD CACHE FILES");
        activeJobsPage.runTheActiveScript(waitForReload, browserDriver);
        
        verifyStatusOfActiveScriptRun(
                "DELETE OLD LOGS & DELETE OLD CACHE FILES");
        waitForReload.until(ExpectedConditions
                .visibilityOf(activeJobsPage.getDrpDwnForgottenCheckins()));
        activeJobsPage
                .clkWebElement(activeJobsPage.getDrpDwnForgottenCheckins());
        CSLogger.info("Expanded 'Forgotten Checkins' section");
        changeTimeDurationForForgottenCheckins();
        activeJobsPage.saveTheActiveScript(waitForReload,
                "FORGOTTEN CHECK-INS");
        activeJobsPage.runTheActiveScript(waitForReload, browserDriver);
        verifyStatusOfActiveScriptRun("FORGOTTEN CHECK-INS");
        waitForReload.until(ExpectedConditions
                .visibilityOf(activeJobsPage.getDrpDwnBackup()));
        activeJobsPage.clkWebElement(activeJobsPage.getDrpDwnBackup());
        CSLogger.info("Expanded 'Backup' section");
        changeTimeDurationForBackup();
        activeJobsPage.saveTheActiveScript(waitForReload, "BACKUP");
        activeJobsPage.runTheActiveScript(waitForReload, browserDriver);
        verifyStatusOfActiveScriptRun("BACKUP");
        CSUtility.tempMethodForThreadSleep(2000);
        softAssertion.assertAll();
        
        CSUtility.switchToDefaultFrame(browserDriver);
        CSLogger.info("Test case execution completed");
    }

    /**
     * This method will verify the status of recently run active script.
     * 
     * @param logsCategory
     */
    private void verifyStatusOfActiveScriptRun(String logsCategory) {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmShowingStatusOfRunningActiveScript()));
        CSLogger.info("switched to frame 'bottom', showing status of "
                + "running active script");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getScriptRunStatus());
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnJobs());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnJobs());
        CSLogger.info("clicked on Jobs button");
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmJobsState()));
               CSUtility.waitForVisibilityOfElement(waitForReload,
            		   activeJobsPage.getBtnRefreshJobsStatus());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnRefreshJobsStatus());
        CSLogger.info("clicked on refresh button in active job status");
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmJobsState()));
        String jobStatus = getJobStatus();      
        if (jobStatus.contains("check.svg")) {
                     CSLogger.info("Test Case successful");
        } else {
        boolean failedScriptRun = activeJobsPage.getImgFailedActiveScriptRun()
                .isDisplayed();
        String scriptRunStatus = "HOUSEKEEPING SCRIPT RUN FAILED FOR '"
                + logsCategory + "' DUE TO SOME ERROR! "
                + "PLEASE CHECK IN LOGS!";
        if (failedScriptRun) {
            CSLogger.error("HOUSEKEEPING SCRIPT RUN FAILED FOR '" + logsCategory
                    + "' DUE TO SOME ERROR! " + "PLEASE CHECK IN LOGS!");
        } else {
            CSLogger.info("SUCCESSFULLY RAN THE HOUSEKEEPING SCRIPT FOR '"
                    + logsCategory + "'.");
        }
        softAssertion.assertFalse(failedScriptRun, scriptRunStatus);
        }
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnProperties());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnProperties());
        CSLogger.info("Test case execution completed");
    }
   
    private String getJobStatus() {
    	WebElement element = browserDriver
		  .findElement(By.xpath("//div[@class='status']/img"));
		String jobStatus = element.getAttribute("src");
		return jobStatus;
    }
    
    /**
     * This method will change the time duration for logs under 'Delete Old
     * Cache Files'
     */
    private void changeTimeDurationForDeleteOldCacheFiles() {
        changeTimeDuration(waitForReload, activeJobsPage.getDrpDwnCacheFiles(),
                activeJobsPage.getTimeDurationForCacheFiles());
    }

    /**
     * This method will change the time duration for logs under 'Delete Old
     * Logs'
     */
    private void changeTimeDurationForDeleteOldLogs() {
        changeTimeDuration(waitForReload, activeJobsPage.getDrpDwnSystemLog(),
                activeJobsPage.getTimeDurationForSystemLog());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnStatusMonitorLog(),
                activeJobsPage.getTimeDurationForStatusMonitorLog());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnActiveScriptLog(),
                activeJobsPage.getTimeDurationForActiveScriptLog());
        changeTimeDuration(waitForReload, activeJobsPage.getDrpDwnRestLog(),
                activeJobsPage.getTimeDurationForRestLog());
        changeTimeDuration(waitForReload, activeJobsPage.getDrpDwnCometLog(),
                activeJobsPage.getTimeDurationForCometLog());
        changeTimeDuration(waitForReload, activeJobsPage.getDrpDwnWsdlLog(),
                activeJobsPage.getTimeDurationForWsdlLog());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnSmartDocumentLog(),
                activeJobsPage.getTimeDurationForSmartDocumentlLog());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnUserSessionVisitLog(),
                activeJobsPage.getTimeDurationForUserSessionVisitLog());
        changeTimeDuration(waitForReload, activeJobsPage.getDrpDwnDeletionLog(),
                activeJobsPage.getTimeDurationForDeletionLog());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnScriptPerformanceLog(),
                activeJobsPage.getTimeDurationForScriptPerformanceLog());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnQueryPerformanceLog(),
                activeJobsPage.getTimeDurationForQueryPerformanceLog());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnFileDownloadLog(),
                activeJobsPage.getTimeDurationForFileDownloadLog());
        changeTimeDuration(waitForReload, activeJobsPage.getDrpDwnUserLog(),
                activeJobsPage.getTimeDurationForUserLog());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnActiveScriptJob(),
                activeJobsPage.getTimeDurationForActiveScriptJob());
    }

    /**
     * This method will change the time duration for logs under 'Forgotten
     * Checkins'
     */
    private void changeTimeDurationForForgottenCheckins() {
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnCheckinPimProducts(),
                activeJobsPage.getTimeDurationForCheckinPimProducts());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnCheckinPimViews(),
                activeJobsPage.getTimeDurationForCheckinPimViews());
        changeTimeDuration(waitForReload,
                activeJobsPage.getDrpDwnCheckinMamFiles(),
                activeJobsPage.getTimeDurationForCheckinMamFiles());
    }

    /**
     * This method will change the time duration for 'Backup Files'
     */
    private void changeTimeDurationForBackup() {
        changeTimeDuration(waitForReload, activeJobsPage.getDrpDwnBackupFiles(),
                activeJobsPage.getTimeDurationForBackupFiles());
    }

    /**
     * This generic method contains the logic for changing the time duration for
     * any log!
     * 
     * @param waitForReload
     *            - WebDriver wait
     * @param drpDwnHeader
     *            - header text for drop down menus
     * @param timeDuration
     *            - time duration for logs
     */
    private void changeTimeDuration(WebDriverWait waitForReload,
            WebElement drpDwnHeader, WebElement timeDuration) {
        CSUtility.waitForVisibilityOfElement(waitForReload, drpDwnHeader);
        activeJobsPage.clkWebElement(drpDwnHeader);
        activeJobsPage.clkWebElement(timeDuration);
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
