/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.io.File;
import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.ActiveJobsPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
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
 * This class contains the test methods which is going to create new active job,
 * verify the created active job, run the active job, verify the run active job,
 * export the active job as excel by clicking on right click option and mass
 * option and then verify the exported Excel File.
 * 
 * @author CSAutomation Team
 */
public class ExportActiveJobsAsExcelTest extends AbstractTest{
    private WebDriverWait  waitForReload;
    private CSPortalHeader csPortalHeader;
    private FrameLocators  locator;
    private ActiveJobsPage activeJobsPage;
    private String         activeJobExportAsExcelSheetName = "ActiveObjectExportAsExcel";

    @BeforeClass
    public void initialization() {
        waitForReload = new WebDriverWait(browserDriver, 30);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        activeJobsPage = SettingsLeftSectionMenubar
                .getActiveJobsNode(browserDriver);
    }

    /**
     * This test method is used to create Active Jobs, verify created Active Job
     * and then Export Active Job As Excel by right click and by mass option and
     * the verify exported file.
     * 
     * @param activeJobLabelName String object containing Job Name.
     */
    @Test(dataProvider = "insertLabelTestData")
    public void testExportActiveJobAsExcel(String activeJobLabelName) {
        try {
            clickOnActiveJobsTreeNode(waitForReload);
            createActiveJob(waitForReload, activeJobLabelName);
            checkPresenceOfJob(waitForReload, activeJobLabelName, false);
            exportExcelByRightClick(waitForReload);
            exportExcelWithMassAction(waitForReload);
        } catch (Exception e) {
            CSLogger.debug("Failed To Export Job By Excel", e);
            Assert.fail("Failed To Export Job By Excel");
        }
    }

    /*
     * This method is used to click on Active jobs label displayed under tree
     * node.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void clickOnActiveJobsTreeNode(WebDriverWait waitForReload) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        CSLogger.info("clicked on setting Tab");
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload, browserDriver,
                locator);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(waitForReload,
                browserDriver, locator);
        activeJobsPage.getNodeActiveJobs().click();
        CSLogger.info("clicked on active jobs tree node");
    }

    /*
     * This method is used to create new active job and name is fetched from
     * excel sheet.
     * 
     * @param activeJobLabel String object containing Job Name.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void createActiveJob(WebDriverWait waitForReload,
            String activeJobLabel) {
        try {
            traverseToPreset(waitForReload);
            CSUtility.tempMethodForThreadSleep(3000);
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
            CSLogger.info("Successfully created new Active Job");
            waitForReload.until(ExpectedConditions.visibilityOf(
                    activeJobsPage.getDrpDwnSelectActiveScript()));
            activeJobsPage.getDrpDwnSelectActiveScript().click();
            CSLogger.info("clicked on drop down menu for selecting script for "
                    + "active job");
            activeJobsPage
                    .clkWebElement(activeJobsPage.getOptionSayHelloScript());
            CSLogger.info("selected 'Say Hello' option from drop down");
            for (int i = 0; i <= 2; i++) {
                activeJobsPage.getBtnRunActiveScript().click();
                Alert alert = CSUtility.getAlertBox(waitForReload,
                        browserDriver);
                alert.accept();
            }
            CSLogger.info("Active Job is Runned Three times");
        } catch (Exception e) {
            CSLogger.error("Failed to create New Active Job.", e);
        }
    }

    /*
     * This Method is used to traverse the Preset Label displayed under active
     * job tree node.
     * 
     * @param waitForReload WebDriverWait Object
     */

    private void traverseToPreset(WebDriverWait waitForReload) {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(waitForReload,
                browserDriver, locator);
        Actions action = new Actions(browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        action.doubleClick(activeJobsPage.getBtnPresets()).build().perform();
        CSLogger.info("Clicked on Presets button in Tree node.");
    }

    /*
     * This method is used to verify whether craeted active job is preset under
     * tree node after creation and verify once it is deleted should not display
     * under tree node.
     * 
     * @param waitForReload WebDriverWait Object
     * 
     * @param activeJobName String object containing Job Name displayed under
     * tree node.
     */
    private void checkPresenceOfJob(WebDriverWait waitForReload,
            String activeJobName, boolean deleteActiveJob) {
        traverseToPreset(waitForReload);
        List<WebElement> activejobNames = browserDriver
                .findElements(By.linkText(activeJobName));
        if (deleteActiveJob) {
            if (activejobNames.isEmpty()) {
                CSLogger.info("The active job named as " + activeJobName
                        + " is not Present");
            } else {
                Assert.fail("The active job named as " + activeJobName
                        + " is present");
            }
        } else {
            if (!activejobNames.isEmpty()) {
                CSLogger.info("The active job named as " + activeJobName
                        + " is Present");
                browserDriver.findElement(By.linkText(activeJobName)).click();
            } else {
                Assert.fail("The active job named as " + activeJobName
                        + " is not present");
            }
        }
    }

    /*
     * This method is used to export the Active job by clicking as right click
     * and Export as Excel option.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void exportExcelByRightClick(WebDriverWait waitForReload) {
        traverseToJobTab(waitForReload);
        activeJobsPage.selectAllChkBox().click();
        checkCreatedRun(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        Actions actions = new Actions(browserDriver);
        WebElement runedJobs = activeJobsPage.getImgSuccessfulScriptRun();
        actions.contextClick(runedJobs).build().perform();
        CSLogger.info("Right click to select Export as Excel Option");
        CSUtility.tempMethodForThreadSleep(2000);
        browserDriver.switchTo().defaultContent();
        browserDriver.switchTo().frame(locator.getFrmCsPopupDivFrame());
        activeJobsPage.exportAsExcel().click();
        CSLogger.info(
                "Clicked on option 'Export as Excel' displayed after clicking"
                        + "on right click option");
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSUtility.tempMethodForThreadSleep(2000);
        popupWindowAfterExportAndVerifyExportedExcel(runedJobs);
        CSLogger.info(
                "Exported active job By clicking on right Click option and"
                        + " export as Excel");
    }

    /**
     * This method is used to Export the active job by mass option.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void exportExcelWithMassAction(WebDriverWait waitForReload) {
        traverseToJobTab(waitForReload);
        activeJobsPage.selectAllChkBox().click();
        WebElement massoption = activeJobsPage.massUpdateSelector();
        Select Marked = new Select(massoption);
        Marked.selectByIndex(4);
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("Exported By mass option");
        popupWindowAfterExportAndVerifyExportedExcel(massoption);
    }

    /**
     * This method is used to close the pop up window display after export as
     * Excel and simultaneously used to get exported file name and compare the
     * file name which is download in drive. 
     * 
     * @ param WebElement exportedFileName contains exported file name object.
     */
    private void popupWindowAfterExportAndVerifyExportedExcel(
            WebElement exportedFileName) {
        String parentHandle = browserDriver.getWindowHandle();
        for (String winHandle : browserDriver.getWindowHandles()) {
            browserDriver.switchTo().window(winHandle);
        }
        exportedFileName = browserDriver
                .findElement(By.xpath("/html[1]/body[1]/div[2]/a[1]"));
        String fileName = exportedFileName.getText();
        String folderName = System.getProperty("user.home") + "\\Downloads"
                + "\\";
        File dir = new File(folderName);
        File[] dirContents = dir.listFiles();
        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().equals(fileName)) {
                CSLogger.info(
                        "The Exported Excel File Named as " + fileName + "Found");
            } else {
                CSLogger.info("The Exported Excel File Named as " + fileName
                        + "Not Found");
            }
        }
        browserDriver.close();
        browserDriver.switchTo().window(parentHandle);
    }

    /**
     * This method is used to traverse to frame of 'Jobs' tab and traverse to
     * frame where 'select all' checkbox is present.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void traverseToJobTab(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getMainFrameMam()));
        activeJobsPage.getBtnJobs().click();
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmJobsState()));
        CSLogger.info("Traversed to frames and Clicked to Job Tab name.");
    }

    /**
     * This method is used to verify the active job that is run.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void checkCreatedRun(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getImgSuccessfulScriptRun());
        boolean runJobs = activeJobsPage.getImgSuccessfulScriptRun()
                .isDisplayed();
        Assert.assertTrue(runJobs, " job is not run sucessfully!");
        CSLogger.info("Job Runned and executed Successfully");
    }

    @DataProvider(name = "insertLabelTestData")
    public Object[][] insertLabelTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                activeJobExportAsExcelSheetName);
    }
}

