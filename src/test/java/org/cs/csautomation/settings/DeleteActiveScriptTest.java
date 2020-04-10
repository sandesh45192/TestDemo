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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to verify the Delete of Active Scripts.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteActiveScriptTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private String         deleteActiveScriptSheet = "DeleteActiveScript";
    private FrameLocators  locator;
    private CSPortalHeader csPortalHeader;
    private ActiveJobsPage activeJobsPage;
    private ISettingsPopup settingPopup;
    
    

    /**
     * This method verifies the test case related to deletion of active scripts
     * 
     * @param jobLabel String object contains job label
     * @param jobCategory String object contains job category
     * @param option String object contains option Name to delete active script
     */
    @Test(dataProvider = "DeleteActiveScriptsData")
    public void testDeleteActiveScript(String jobLabels, String jobCategory,
            String option) {
        try {
            traverseToPreset();             
            CSUtility.tempMethodForThreadSleep(1000);
            if (option.equalsIgnoreCase("menubar")) {            	
                createActiveScript(jobLabels, jobCategory);
                deleteScriptMenuBar(jobLabels);
                verifyDeleteActiveScriptByMenuBar(jobLabels);
            } else {
                String labels[] = jobLabels.split(",");
                for (String joblabel : labels) {
                    createActiveScript(joblabel, jobCategory);
                }
                deleteScriptByMultiSelect(jobLabels, option);
                verifyDeteleAciveScriptByMultiSelect(jobLabels);
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteActiveScript", e);
            Assert.fail("Automation error : testDeleteActiveScript", e);
        }
    }

    /**
     * This method Create the Active Script
     * 
     * @param jobLabel String object contains job label
     * @param jobCategory String object contains job category
     */
    private void createActiveScript(String jobLabel, String jobCategory) {
    	TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
        waitForReload, browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getBtnCreateNewActiveJob());
        activeJobsPage.clkWebElement(activeJobsPage.getBtnCreateNewActiveJob());
        CSLogger.info("clicked on createNew for active job");
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        activeJobsPage.enterTextIntoTextbox(waitForReload,
                activeJobsPage.getTxtNewActiveScriptLabel(), jobLabel);
        CSLogger.info("Entered the label for active job");
        activeJobsPage.enterTextIntoTextbox(waitForReload,
                activeJobsPage.getTxtNewActiveScriptCategory(), jobCategory);
        CSLogger.info("Entered the category for active job");
        activeJobsPage.clkWebElement(activeJobsPage.getBtnSaveActiveJob());
        CSLogger.info("clicked on save button for active job");
        CSLogger.info("New active job successfully created");
    }

    /**
     * This method traverse to Preset node
     */
    private void traverseToPreset() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        CSLogger.info("clicked on setting header");
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);		
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
     * This method delete the Active script via selecting the delete icon at
     * MenuBar
     * 
     * @param jobLabel String object contains job label
     */
    private void deleteScriptMenuBar(String jobLabels) {
        int count = countOfScripts();        
        for (int row = 3; row < count; row++) {
            String getLabel = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr["
                            + row + "]//td[2]"))
                    .getText();            
            if (getLabel.equals(jobLabels)) {            	
                browserDriver.findElement(By.xpath(
                        "//table[@class='hidewrap CSAdminList']//tbody//tr["
                                + row + "]"))
                        .click();                
            }
        }        
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);  
		        /* new code for show more option.
		         * added new xpath and getter method for getbtnShowMoreOption in activeJobsPage
		         * added new xpath for getActiveStriptPopup in settingPopup.
		        
		        */
                CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getbtnShowMoreOption());         
                activeJobsPage.getbtnShowMoreOption().click();                
                settingPopup.selectPopupDivMenu(waitForReload, settingPopup.getActiveScriptPopup(), browserDriver);  
//                CSUtility.switchToDefaultFrame(browserDriver);
              CSUtility.tempMethodForThreadSleep(1000);
                settingPopup.selectPopupDivSubMenu(waitForReload, settingPopup.getObjectDeleteSubMenu(), browserDriver);
              CSUtility.tempMethodForThreadSleep(3000);
//                waitForReload.until(
//                        ExpectedConditions.elementToBeClickable(getCsPopupDivSub()));
//                waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//                		locator.getFrmCsPopupDivFrame()));
//                waitForReload
//                .until(ExpectedConditions.elementToBeClickable(settingPopup.getActiveScriptPopup()));
//                settingPopup.getActiveScriptPopup().click();
                
//                CSUtility.switchToDefaultFrame(browserDriver);
//                waitForReload.until(
//                        ExpectedConditions.elementToBeClickable(getCsPopupDivSub()));
//                waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//                		locator.getFrmCsPopupDivSubFrame()));
//                CSLogger.info("Switched to cs pop div sub frame");
//                waitForReload
//                        .until(ExpectedConditions.visibilityOf(settingPopup.getObjectDeleteSubMenu()));
//                settingPopup.getObjectDeleteSubMenu().click();
				/* ***Old code
				 * CSUtility.waitForVisibilityOfElement(waitForReload,
				 * activeJobsPage.getbtnDeleteActiveScript());
				 * activeJobsPage.getbtnDeleteActiveScript().click();
				 */                               
                
                Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
    		    alert.accept();                 
		        CSUtility.tempMethodForThreadSleep(1000);
		        CSLogger.info("Delete Script from MenuBar");
    }

    /**
     * This method Select Multiple Scripts and delete the selected scripts
     * 
     * @param jobLabel String object contains job label
     * @param option String object contains option to duplicate active script
     *            jobs
     */
    private void deleteScriptByMultiSelect(String jobLabels, String option) {
        String labels[] = jobLabels.split(",");
        int count = countOfScripts();
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
        if (option.equalsIgnoreCase("rightclick")) {
            WebElement element = browserDriver.findElement(By.xpath(
                    "//table[@class='hidewrap CSAdminList']//tbody//tr[3]//td[1]"));
            CSUtility.rightClickTreeNode(waitForReload, element, browserDriver);
            settingPopup.selectPopupDivMenu(waitForReload,
                    settingPopup.getDeleteMenu(), browserDriver);
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            alert.accept();
            CSLogger.info("Delete created by right click");
        } else {
            Select bottomDropdown = new Select(browserDriver.findElement(
                    By.xpath("//select[@id='massUpdateSelector']")));
            bottomDropdown.selectByVisibleText("Delete");
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            alert.accept();
            CSLogger.info("Delete created by bottom drop down");
        }
    }

    /**
     * This method verifies the Delete Active Script via MultiPle Selection
     * 
     * @param jobLabel String object contains job label
     */
    private void verifyDeteleAciveScriptByMultiSelect(String jobLabels) {
        int count = countOfScripts();
        String labels[] = jobLabels.split(",");
        ArrayList<String> scriptsLabel = new ArrayList<String>();
        for (int row = 3; row < count; row++) {
            scriptsLabel.add(browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr["
                            + row + "]//td[2]"))
                    .getText());
        }
        for (String labelScript : labels) {
            if (!scriptsLabel.contains(labelScript)) {
                CSLogger.info("Delete Successful for label " + labelScript);
            } else {
                CSLogger.error("Delete fail for label " + labelScript);
                Assert.fail("Delete fail for label " + labelScript);
            }
        }
    }

    /**
     * This method verifies the Delete Active Script via delete icon at MenuBar
     * 
     * @param jobLabel String object contains job label
     */
    private void verifyDeleteActiveScriptByMenuBar(String jobLabel) {
        int count = countOfScripts();
        ArrayList<String> scriptsLabel = new ArrayList<String>();
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        for (int row = 3; row < count; row++) {
            scriptsLabel.add(browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr["
                            + row + "]//td[2]"))
                    .getText());
        }
        if (!scriptsLabel.contains(jobLabel)) {        	
            CSLogger.info("Delete Successful for label " + jobLabel);
        } else {        	
            CSLogger.error("Delete fail for label " + jobLabel);
            Assert.fail("Delete fail for label " + jobLabel);
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
     * contains label, category and option to delete Active job
     * 
     * @return editPoralConfigurationDataSheet
     */
    @DataProvider(name = "DeleteActiveScriptsData")
    public Object[] deleteActiveScriptsJobs() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteActiveScriptSheet);
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
