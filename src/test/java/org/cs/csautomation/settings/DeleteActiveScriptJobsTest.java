/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.ActiveJobsPage;
import org.cs.csautomation.cs.settings.ISettingsPopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to verify delete Active Script jobs.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteActiveScriptJobsTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private String         deleteActiveScriptJobs = "DeleteActiveScriptJobs";
    private FrameLocators  locator;
    private CSPortalHeader csPortalHeader;
    private ActiveJobsPage activeJobsPage;
    private ISettingsPopup settingPopup;

    /**
     * This method verifies the deletion of active scripts jobs
     * 
     * @param jobLabel String object contains job label
     * @param jobCategory String object contains job category
     * @param script String object contains jobs script name
     * @param count String object contains job count
     */
    @Test(dataProvider = "DeleteActiveJobsData")
    public void testDeleteActiveJobs(String jobLabel, String jobCategory,
            String script, String count) {
        try {
            createActiveScript(jobLabel, jobCategory, script, count);
            runActiveScript();
            deleteJobsByRightClick();
            deleteJobsByDropdownList();
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteActiveJobs", e);
            Assert.fail("Automation error : testDeleteActiveJobs", e);
        }
    }

    /**
     * This method Create the Active Script
     * 
     * @param jobLabel String object contains job label
     * @param jobCategory String object contains job category
     * @param script String object contains jobs script name
     * @param count String object contains job count
     */
    private void createActiveScript(String jobLabel, String jobCategory,
            String script, String count) {
        traverseToPreset();        
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnCreateNewActiveJob());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnCreateNewActiveJob());
        CSLogger.info("clicked on createNew for active job");
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        activeJobsPage.enterTextIntoTextbox(waitForReload,
                activeJobsPage.getTxtNewActiveScriptLabel(), jobLabel);
        CSLogger.info("Entered the label for active job");
        activeJobsPage.enterTextIntoTextbox(waitForReload,
                activeJobsPage.getTxtNewActiveScriptCategory(), jobCategory);
        CSLogger.info("Entered the category for active job");
        waitForReload.until(ExpectedConditions
                .visibilityOf(activeJobsPage.getDrpDwnSelectActiveScript()));
        activeJobsPage.getDrpDwnSelectActiveScript().click();
        activeJobsPage.clkWebElement(activeJobsPage.getOptionSayHelloScript());
        CSLogger.info("selected 'Say Hello' option from drop down");
        activeJobsPage.enterTextIntoTextbox(waitForReload,
                activeJobsPage.getTxtCountForSayHelloScript(), count);
        CSLogger.info("Entered the 'count' for Say Hello script");
        activeJobsPage.clkWebElement(activeJobsPage.getBtnSaveActiveJob());
        CSLogger.info("clicked on save button for active job");
        CSLogger.info("New active job successfully created");
    }

    /**
     * This method create the Active jobs
     */
    private void runActiveScript() {
        for (int i = 0; i <= 7; i++) {
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver,
                    locator);
            activeJobsPage.runTheActiveScript(waitForReload, browserDriver);
            waitForReload.until(
                    ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator
                            .getFrmShowingStatusOfRunningActiveScript()));
            CSLogger.info("switched to frame 'bottom', showing status of "
                    + "running active script");
            boolean correctSayHelloCount = activeJobsPage
                    .getSayHelloScriptRunStatus().isDisplayed();
            Assert.assertTrue(correctSayHelloCount,
                    "Error! Say hello text was NOT displayed as per the count. "
                            + "Please rectify.");
            CSLogger.info("Say hello text is successfully displayed, "
                    + "number of times equal to the count. ");
        }
    }

    /**
     * This method will delete the job by right click
     */
    private void deleteJobsByRightClick() {
        goToFrameJobs();
        ArrayList<String> recordIds = new ArrayList<String>();
        for (int row = 3; row <= 5; row++) {
            browserDriver.findElement(By.xpath(
                    "//div[@id='CSGuiListbuilderTable']//table//tbody//tr["
                            + row + "]//td[1]"))
                    .click();
            recordIds.add(browserDriver.findElement(By.xpath(
                    "//div[@id='CSGuiListbuilderTable']//table//tbody//tr["
                            + row + "]//td[3]"))
                    .getText());
        }
        WebElement element = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']//tbody//tr[3]//td[1]"));
        CSUtility.rightClickTreeNode(waitForReload, element, browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getDeleteMenu(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        verifydelete(recordIds);
    }

    /**
     * This method will delete the jobs by dropdown at the bottom
     */
    private void deleteJobsByDropdownList() {
        goToFrameJobs();
        ArrayList<String> recordsId = new ArrayList<String>();
        for (int row = 3; row <= 5; row++) {
            browserDriver.findElement(By.xpath(
                    "//div[@id='CSGuiListbuilderTable']//table//tbody//tr["
                            + row + "]//td[1]"))
                    .click();
            recordsId.add(browserDriver.findElement(By.xpath(
                    "//div[@id='CSGuiListbuilderTable']//table//tbody//tr["
                            + row + "]//td[3]"))
                    .getText());
        }
        Select bottomDropdown = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        bottomDropdown.selectByVisibleText("Delete");
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        verifydelete(recordsId);
    }

    /**
     * This method will traverse to Preset node
     */
    private void traverseToPreset() {
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
        action.doubleClick(activeJobsPage.getBtnPresets()).build().perform();
        CSLogger.info("clicked on presets");
    }

    /**
     * This method verifies the delete test
     * 
     * @param recordIds ArrayList object contains list of Id
     */
    private void verifydelete(ArrayList<String> recordIds) {
        goToFrameJobs();
        WebElement table = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, table);
        int jobsCount = table.findElements(By.tagName("tr")).size();
        ArrayList<String> latestRecordIds = new ArrayList<String>();
        for (int row = 3; row < jobsCount; row++) {
            latestRecordIds.add(browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr["
                            + row + "]//td[3]"))
                    .getText());
        }
        for (String deletedId : recordIds) {
            if (!latestRecordIds.contains(deletedId)) {
                CSLogger.info("Delete Successful for id " + deletedId);
            } else {
                CSLogger.error("Delete fail for id " + deletedId);
                Assert.fail("Delete fail for id " + deletedId);
            }
        }
    }

    /**
     * This method will traverse to jobs frame
     */
    private void goToFrameJobs() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload, browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnJobs());
        activeJobsPage.getBtnJobs().click();
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmJobsState()));
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//div[@id='CSGuiListbuilderTable']")));
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains label, category and script name of Active job
     * 
     * @return editPoralConfigurationDataSheet
     */
    @DataProvider(name = "DeleteActiveJobsData")
    public Object[] deleteAvtiveJobs() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteActiveScriptJobs);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 30);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        activeJobsPage = SettingsLeftSectionMenubar
                .getActiveJobsNode(browserDriver);
        settingPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }
}
