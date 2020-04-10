/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.workflows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.AutomationsPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IAutomationNodePopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains test method to verify that CS Automation can be executed
 * automatically.
 * 
 * @author CSAutomation Team
 *
 */
public class AutomaticExecutionOfAutomationTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private AutomationsPage        automationsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private IAutomationNodePopup   automationNodePopup;
    private SoftAssert             softAssertion;
    private String                 dataSheetName = 
            "AutomaticExecutionOfAutomation";

    /**
     * This test methods verifies that CS Automation can be executed
     * automatically.
     * 
     * @param automationName
     *            String object contains name of automation.
     * @param automationDescription
     *            String object contains automation description.
     * @param autoRunFrequency
     *            String object contains auto run frequency to be set for
     *            automation.
     * @param standardAction
     *            String object contains standard action to be selected for
     *            automation.
     */
    @Test(dataProvider = "executeAutomationData")
    public void testAutomationExecutionOfAutomation(String automationName,
            String automationDescription, String autoRunFrequency,
            String standardAction) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 50);
            goToAutomationsNode();
            createNewAutomation();
            configureAutomation(automationName, automationDescription,
                    autoRunFrequency, standardAction);
            verifyCreationOfAutomation(automationName);
            traverseToAutomationRightPane();
            String lastRunValueBeforeExecution = getLastRunValue();
            saveAutomation();
            verifyAutomationExecutedAutomatically(lastRunValueBeforeExecution);
            setAutoRunFrequencyToManual();
            printLogEntry(automationName);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method : "
                    + "testAutomationExecutionOfAutomation", e);
            Assert.fail("Automation error in test method : "
                    + "testAutomationExecutionOfAutomation", e);
        }
    }

    /**
     * Traverses to automation's right pane frames.
     */
    private void traverseToAutomationRightPane() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * This method clicks on automation node under settings tab.
     */
    private void goToAutomationsNode() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        traverseToAutomationTreeView();
        automationsPageInstance.clkOnAutomationsNode(waitForReload);
    }

    /**
     * Switches frames of system preferences left pane tree located under
     * setting's tab.
     */
    private void traverseToAutomationTreeView() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * Selects 'Create New' popup option by right clicking on automation node to
     * create new automation.
     */
    private void createNewAutomation() {
        CSUtility.rightClickTreeNode(waitForReload,
                automationsPageInstance.getBtnAutomationsNode(), browserDriver);
        automationNodePopup.selectPopupDivMenu(waitForReload,
                automationNodePopup.getCsPopupCreateNew(), browserDriver);
    }

    /**
     * Enters automation information for eg. automation name,description,auto
     * run frequency and standard automation action.
     * 
     * @param automationName
     *            String object contains name of automation.
     * @param automationDescription
     *            String object contains automation description.
     * @param autoRunFrequencyOption
     *            String object contains auto run frequency to be set for
     *            automation.
     * @param standardActionOption
     *            String object contains standard action to be selected for
     *            automation.
     */
    private void configureAutomation(String automationName,
            String automationDescription, String autoRunFrequencyOption,
            String standardActionOption) {
        traverseToAutomationRightPane();
        automationsPageInstance.enterAutomationDetails(automationName,
                automationsPageInstance.getTxtAutomationName(), waitForReload);
        automationsPageInstance.enterAutomationDetails(automationDescription,
                automationsPageInstance.getTxtAutomationDescription(),
                waitForReload);
        automationsPageInstance.selectAutoRunFrequency(autoRunFrequencyOption);
        automationsPageInstance.selectStandardAction(standardActionOption);
        CSLogger.info("Automation configured  successfully.");
    }

    /**
     * Saves the configured automation.
     */
    private void saveAutomation() {
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * Verifies whether automation is created.
     * 
     * @param automationName
     *            String object contains name of automation.
     */
    private void verifyCreationOfAutomation(String automationName) {
        traverseToAutomationTreeView();
        CSUtility.rightClickTreeNode(waitForReload,
                automationsPageInstance.getBtnAutomationsNode(), browserDriver);
        automationNodePopup.selectPopupDivMenu(waitForReload,
                automationNodePopup.getCsPopupRefresh(), browserDriver);
        try {
            traverseToAutomationTreeView();
            browserDriver
                    .findElement(By.xpath("//span[contains(@id,'Automations@')]"
                            + "[contains(text(),'" + automationName + "')]"));
            CSLogger.info("Automation created successfully : test step passed");
        } catch (Exception e) {
            CSLogger.error("Automation " + automationName
                    + " Automation failed : test step failed", e);
            Assert.fail("Automation " + automationName
                    + " creation failed : test step failed");
        }
    }

    /**
     * Returns the 'Last Run' attribute value.
     * 
     * @return String attribute value.
     */
    private String getLastRunValue() {
        WebElement lastRunElement = browserDriver.findElement(
                By.xpath("//span[contains(@id,'AutomationAutoRunLast')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, lastRunElement);
        return lastRunElement.getText();
    }

    /**
     * Verifies that once automation is saved the automation executes
     * automatically after every 30 seconds.
     * 
     * @param lastRunValueBeforeExecution
     *            String contains 'last run' attribute value before execution of
     *            automation.
     */
    private void verifyAutomationExecutedAutomatically(
            String lastRunValueBeforeExecution) {
        // Wait for 30 seconds because auto run frequency is set to 30 seconds
        CSUtility.tempMethodForThreadSleep(30000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarRefresh(waitForReload);
        String lastRunValueAfterExecution = getLastRunValue();
        String localDate = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDate.now()).toString().replace("/", "-");
        if (lastRunValueBeforeExecution.equals("Never")
                && lastRunValueAfterExecution.contains(localDate)) {
            CSLogger.info(
                    "Automation executed automatically : test step passed");
        } else {
            CSLogger.error(
                    "Automatic execution of automation failed : test step "
                            + "failed");
            softAssertion
                    .fail("Automatic execution of automation failed : test "
                            + "step " + "failed");
        }
    }

    /**
     * Sets the 'Auto Run Frequency' to 'Manual'.
     * 
     */
    private void setAutoRunFrequencyToManual() {
        automationsPageInstance.selectAutoRunFrequency("Manual");
        saveAutomation();
    }

    /**
     * Prints the log entries of automation to logger file.
     * 
     * @param automationName
     *            String object contains name of automation.
     */
    private void printLogEntry(String automationName) {
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(By.id("AttributeRow_Log")),
                browserDriver);
        WebElement logEntry = browserDriver.findElement(
                By.xpath("//textarea[contains(@id,'AutomationLog')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, logEntry);
        if (logEntry.getText().isEmpty()) {
            CSLogger.info(
                    "No log entry found for automation : " + automationName);
        } else {
            CSLogger.info("Log entry for automation : " + automationName
                    + " is " + "\n" + logEntry.getText().trim());
        }
    }

    /**
     * This is a data provider method that contains data to execute a
     * automation.
     * 
     * @return Array
     */
    @DataProvider(name = "executeAutomationData")
    public Object[][] getExecuteAutomationData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("workflowModuleTestCases"),
                dataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        automationsPageInstance = SettingsLeftSectionMenubar
                .getAutomationsNode(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        automationNodePopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
    }
}
