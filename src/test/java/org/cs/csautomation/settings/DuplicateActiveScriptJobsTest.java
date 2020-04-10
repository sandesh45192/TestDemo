/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to verify the Duplication of Active
 * Script jobs.
 * 
 * @author CSAutomation Team
 *
 */
public class DuplicateActiveScriptJobsTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private String         duplicateASSheet = "DuplicateAS";
    private FrameLocators  locator;
    private CSPortalHeader csPortalHeader;
    private ActiveJobsPage activeJobsPage;
    private ISettingsPopup settingPopup;

    /**
     * This method verifies the duplication of active scripts jobs
     * 
     * @param jobLabel String object contains job label
     * @param jobCategory String object contains job category
     * @param option String object contains option to duplicate active script
     *            jobs
     */
    @Test(dataProvider = "DuplicateActiveJobsData")
    public void testDuplicateActiveScriptJobs(String jobLabels,
            String jobCategory, String option) {
        try {
            createActiveScript(jobLabels, jobCategory);
            duplicateJobs(jobLabels, option);
            verifyDupicateTest(jobLabels);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDuplicateActiveScriptJobs",
                    e);
            Assert.fail("Automation error : testDuplicateActiveScriptJobs", e);
        }
    }

    /**
     * This method Create the Active Script
     * 
     * @param jobLabel String object contains job label
     * @param jobCategory String object contains job category
     */
    private void createActiveScript(String jobLabels, String jobCategory) {
        traverseToPreset();         
        CSUtility.tempMethodForThreadSleep(1000);
        String labels[] = jobLabels.split(",");
        for (String joblabel : labels) {
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, locator);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPage.getBtnCreateNewActiveJob());
            activeJobsPage
                    .clkWebElement(activeJobsPage.getBtnCreateNewActiveJob());
            CSLogger.info("clicked on createNew for active job");
            CSUtility.tempMethodForThreadSleep(1000);
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                    browserDriver, locator);
            activeJobsPage.enterTextIntoTextbox(waitForReload,
                    activeJobsPage.getTxtNewActiveScriptLabel(), joblabel);
            CSLogger.info("Entered the label for active job");
            activeJobsPage.enterTextIntoTextbox(waitForReload,
                    activeJobsPage.getTxtNewActiveScriptCategory(),
                    jobCategory);
            CSLogger.info("Entered the category for active job");
            activeJobsPage.clkWebElement(activeJobsPage.getBtnSaveActiveJob());
            CSLogger.info("clicked on save button for active job");
            CSLogger.info("New active job successfully created");
        }
    }

    /**
     * This method traverse to Preset node
     */
    private void traverseToPreset() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        CSLogger.info("clicked on setting header");        	
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
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
     * This method duplicate the Active jobs via right click or from bottom
     * dropdown list
     * 
     * @param jobLabel String object contains job label
     * @param option String object contains option to duplicate active script
     *            jobs
     */
    private void duplicateJobs(String jobLabels, String option) {
        String labels[] = jobLabels.split(",");
        int count = countOfScripts();
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        for (int row = 3; row < count; row++) {
            String getLabel = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr["
                            + row + "]//td[2]"))
                    .getText();
            if (getLabel.equals(labels[0]) || getLabel.equals(labels[1])) {
                browserDriver.findElement(By.xpath(
                        "//table[@class='hidewrap CSAdminList']//tbody//tr["
                                + row + "]//td[1]"))
                        .click();
            }
        }
        if (option.equals("RightClick")) {
            WebElement element = browserDriver.findElement(By.xpath(
                    "//table[@class='hidewrap CSAdminList']//tbody//tr[3]//td[1]"));
            CSUtility.rightClickTreeNode(waitForReload, element, browserDriver);
            settingPopup.selectPopupDivMenu(waitForReload,
                    settingPopup.getDuplicateMenu(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            alert.accept();
            CSLogger.info("Duplicate created by right click");
        } else {
            Select bottomDropdown = new Select(browserDriver.findElement(
                    By.xpath("//select[@id='massUpdateSelector']")));
            bottomDropdown.selectByVisibleText("Duplicate");
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            alert.accept();
            CSLogger.info("Duplicate created by bottom drop down");
        }
    }

    /**
     * This method verifies the duplication of the active Script jobs
     * 
     * @param jobLabel String object contains job label
     */
    private void verifyDupicateTest(String jobLabels) {
        String labels[] = jobLabels.split(",");
        int count = countOfScripts();
        int flag = 0;
        for (String label : labels) {
            flag = 0;
            for (int row = 3; row < count; row++) {
                String duplicateLabel = browserDriver.findElement(By.xpath(
                        "//table[@class='hidewrap CSAdminList']//tbody//tr["
                                + row + "]//td[2]"))
                        .getText();
                if (duplicateLabel.equals(label)) {
                    flag += 1;
                }
            }
            if (flag >= 2) {
                CSLogger.info(
                        "Duplicate Verify for label " + label + " is Passed");
            } else {
                CSLogger.error(
                        "Duplicate Verify for label " + label + " is failed");
                Assert.fail(
                        "Duplicate Verify for label " + label + " is failed");
            }
        }
    }

    /**
     * This method count the Scripts present inside the table
     * 
     * @return jobsCount number of jobs present in table
     */
    private int countOfScripts() {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        WebElement table = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, table);
        int jobsCount = table.findElements(By.tagName("tr")).size();
        return jobsCount;
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains label, category and option to duplicate Active job
     * 
     * @return editPoralConfigurationDataSheet
     */
    @DataProvider(name = "DuplicateActiveJobsData")
    public Object[] duplicateActiveScriptsJobs() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                duplicateASSheet);
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
