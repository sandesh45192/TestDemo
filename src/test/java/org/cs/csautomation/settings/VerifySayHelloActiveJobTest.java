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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create new active script job
 * 
 * @author CSAutomation Team
 */
public class VerifySayHelloActiveJobTest extends AbstractTest {

    private WebDriverWait waitForReload;
    private FrameLocators locator;
    private CSPortalHeader csPortalHeader;
    private String ActiveScriptSheet = "VerifySayHelloActiveJob";
    private ActiveJobsPage activeJobsPage;
    
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
    }

    /**
     * This test method will create a new active job, assign 'Say Hello' script
     * to it and print the text, number of times equal to the count. Test method
     * takes input from the data provider.
     * 
     * @param activeJobLabel
     *            - user defined label for new active job
     * @param activeJobCategory
     *            - user defined category for new active job
     * @param sayHelloText
     *            - user defined text, which is to be printed
     */
    @Test(dataProvider = "createNewActiveScriptData")
    public void testVerifySayHelloActiveJob(String activeJobLabel,
            String activeJobCategory, String sayHelloCount,
            String sayHelloText) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            CSLogger.info("clicked on setting header");
            TraversingForSettingsModule.traversetoSystemPreferences(waitForReload, browserDriver,
                    locator);			
            TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(waitForReload,
                    browserDriver, locator);
            CSUtility.tempMethodForThreadSleep(5000);
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
            TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                    browserDriver, locator);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPage.getBtnCreateNewActiveJob());
            activeJobsPage
                    .clkWebElement(activeJobsPage.getBtnCreateNewActiveJob());
            CSLogger.info("clicked on createNew for active job");
            CSUtility.tempMethodForThreadSleep(3000);
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver,
                    locator);
            CSUtility.tempMethodForThreadSleep(3000);
            activeJobsPage.enterTextIntoTextbox(waitForReload,
                    activeJobsPage.getTxtNewActiveScriptLabel(),
                    activeJobLabel);            
            CSLogger.info("Entered the label for active job");
            activeJobsPage.clkWebElement(activeJobsPage.getBtnSaveActiveJob());
            CSLogger.info("clicked on save button for active job");
            CSLogger.info("NEW ACTIVE JOB SUCCESSFULLY CREATED");
            activeJobsPage.enterTextIntoTextbox(waitForReload,
                    activeJobsPage.getTxtNewActiveScriptCategory(),
                    activeJobCategory);
            CSLogger.info("Entered the category for active job");
            waitForReload.until(ExpectedConditions.visibilityOf(
                    activeJobsPage.getDrpDwnSelectActiveScript()));
            activeJobsPage.getDrpDwnSelectActiveScript().click();
            CSLogger.info("clicked on drop down menu "
                    + "for selecting script for active job");
            activeJobsPage
                    .clkWebElement(activeJobsPage.getOptionSayHelloScript());
            CSLogger.info("selected 'Say Hello' option from drop down");
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver,
                    locator);
            activeJobsPage.enterTextIntoTextbox(waitForReload,
                    activeJobsPage.getTxtCountForSayHelloScript(),
                    sayHelloCount);
            CSLogger.info("Entered the 'count' for Say Hello script");
            activeJobsPage.enterTextIntoTextbox(waitForReload,
                    activeJobsPage.getTxtWhatToSay(), sayHelloText);
            CSLogger.info(
                    "Entered the text for 'What to say' for Say Hello script");
            activeJobsPage.saveTheActiveScript(waitForReload, "log");
            CSLogger.info("successfully saved the script after entering "
                    + "the count and text for 'Say Hello' script");
            activeJobsPage.runTheActiveScript(waitForReload, browserDriver);
            verifyStatusOfActiveScriptRun();
        } catch (Exception e) {
            CSLogger.debug("Automation Error : ", e);
        }
    }

    /**
     * This method will verify whether say hello script text is displayed or
     * not. Also, it will verify whether the text is displayed, number of times
     * equal to count.
     */
    private void verifyStatusOfActiveScriptRun() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmShowingStatusOfRunningActiveScript()));
        CSLogger.info("switched to frame 'bottom', showing status of "
                + "running active script");
        boolean correctSayHelloCount = activeJobsPage
                .getSayHelloScriptRunStatus().isDisplayed();
        Assert.assertTrue(correctSayHelloCount,
                "Error! Say hello text was NOT displayed as per the count. "
                        + "Please rectify.");
        CSLogger.info("Say hello text is successfully displayed, "
                + "number of times equal to the count. ");
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
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getImgSuccessfulScriptRun());
        boolean SuccessfulSayHelloScriptRun = activeJobsPage
                .getImgSuccessfulScriptRun().isDisplayed();
        Assert.assertTrue(SuccessfulSayHelloScriptRun,
                "Error! 'Say Hello' script run was NOT successful. "
                        + "Please rectify!");
        CSLogger.info("Say Hello script successfully executed.");
    }

    /**
     * This method will traverse to the frame of 'system preferences' symbol,
     * leftmost vertical frame, once you enter into SETTINGS header
     */
    @SuppressWarnings("unused")
    private void traverseToFrameOfCSPortalGUIPaneTab() {
        CSUtility.switchToDefaultFrame(browserDriver);
        locator.switchToTopFrame(waitForReload);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        CSLogger.info("switched to the frame, frame_127");
    }

    /**
     * This data provider returns active job label, active job category and
     * sayHelloText
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
