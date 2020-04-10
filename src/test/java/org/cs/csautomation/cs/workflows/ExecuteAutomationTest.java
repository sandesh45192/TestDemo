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
import org.cs.csautomation.cs.settings.IAutomationPopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains test methods to executes a automation by selecting run
 * now option from tree view and mid pane.
 * 
 * @author CSAutomation Team
 *
 */
public class ExecuteAutomationTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private AutomationsPage        automationsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private IAutomationNodePopup   automationNodePopup;
    private IAutomationPopup       automationPopup;
    private SoftAssert             softAssertion;
    private String                 dataSheetName = "ExecuteAutomation";

    /**
     * This test method executes a automation by selecting run now option from
     * tree view and mid pane.
     * 
     * @param automationName String object contains name of automation.
     * @param automationDescription String object contains automation
     *            description.
     * @param autoRunFrequency String object contains auto run frequency to be
     *            set for automation.
     * @param standardAction String object contains standard actionto be
     *            selected for automation.
     */
    @Test(dataProvider = "executeAutomationData")
    public void testExecuteAutomation(String automationName,
            String automationDescription, String autoRunFrequency,
            String standardAction) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            goToAutomationsNode();
            createNewAutomation();
            configureAutomation(automationName, automationDescription,
                    autoRunFrequency, standardAction);
            saveAutomation();
            verifyCreationOfAutomation(automationName);
            executeAutomationFromTreeView(automationName);
            verifyLogEntryOfAutomation(automationName);
            executeAutomationFromMidPane(automationName);
            verifyLogEntryOfAutomation(automationName);
            printLogEntry(automationName);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : testExecuteAutomationFromTreeView",
                    e);
            Assert.fail(
                    "Automation error in : testExecuteAutomationFromTreeView",
                    e);
        }
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
     * @param automationName String object contains name of automation.
     * @param automationDescription String object contains automation
     *            description.
     * @param autoRunFrequencyOption String object contains auto run frequency
     *            to be set for automation.
     * @param standardActionOption String object contains standard action to be
     *            selected for automation.
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
     * Traverses to automation's right pane frames.
     */
    private void traverseToAutomationRightPane() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
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
     * @param automationName String object contains name of automation.
     */
    private void verifyCreationOfAutomation(String automationName) {
        traverseToAutomationTreeView();
        CSUtility.rightClickTreeNode(waitForReload,
                automationsPageInstance.getBtnAutomationsNode(), browserDriver);
        automationNodePopup.selectPopupDivMenu(waitForReload,
                automationNodePopup.getCsPopupRefresh(), browserDriver);
        try {
            traverseToAutomationTreeView();
            browserDriver.findElement(By.xpath(
                    "//span[contains(@id,'Automations@')][contains(text(),'"
                            + automationName + "')]"));
            CSLogger.info("Automation created successfully : teststep passed");
        } catch (Exception e) {
            CSLogger.error("Automation " + automationName
                    + " Automation failed : teststep failed", e);
            Assert.fail("Automation " + automationName
                    + " creation failed : teststep failed");
        }
    }

    /**
     * Executes the given automation by performing right click operation on
     * given automation and selecting run now option.
     * 
     * @param automationName String object contains name of automation to be
     *            executed.
     */
    private void executeAutomationFromTreeView(String automationName) {
        traverseToAutomationTreeView();
        CSUtility.rightClickTreeNode(waitForReload,
                getAutomation(automationName), browserDriver);
        automationPopup.selectPopupDivMenu(waitForReload,
                automationPopup.getCsPopupMenuRunNow(), browserDriver);
        CSUtility.tempMethodForThreadSleep(5000);
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        String alertText = alertBox.getText();
        alertBox.accept();
        CSLogger.info("Clicked on ok of alert box saying : " + alertText);
    }

    /**
     * Returns the WebElement of given automation
     * 
     * @param automationName String object contains name of automation
     * @return WebElement of created automation.
     */
    private WebElement getAutomation(String automationName) {
        WebElement createdAutomation = browserDriver.findElement(By
                .xpath("//span[contains(@id,'Automations@')][contains(text(),'"
                        + automationName + "')]/../.."));
        CSUtility.waitForElementToBeClickable(waitForReload, createdAutomation);
        return createdAutomation;
    }

    /**
     * Verifies the log entries of an automation.
     */
    private void verifyLogEntryOfAutomation(String automationName) {
        traverseToAutomationRightPane();
        // csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarRefresh(waitForReload);
        WebElement logEntry = getLogEntryElement();
        if (logEntry.getText().isEmpty()) {
            CSLogger.error("No log entry found : testcase failed");
            softAssertion.fail("No log entry found : testcase failed");
        } else {
            String logEntryText = logEntry.getText().trim();
            String localDate = DateTimeFormatter.ofPattern("dd/MM/yy")
                    .format(LocalDate.now()).toString().replace("/", ".");
            if (logEntryText.startsWith("<admin")
                    && logEntryText.endsWith("</admin>")
                    && logEntryText.contains(localDate)) {
                CSLogger.info(
                        "Log entry verified successfully : teststep passed");
            } else {
                CSLogger.error(
                        "Log entry verification failed : teststep failed");
                Assert.fail("Log entry verification failed : teststep failed");
            }
        }
    }

    private WebElement getLogEntryElement() {
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(By.id("AttributeRow_Log")),
                browserDriver);
        WebElement logEntry = browserDriver.findElement(
                By.xpath("//textarea[contains(@id,'AutomationLog')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, logEntry);
        return logEntry;
    }

    /**
     * Executes the given automation by selecting run now option of given
     * automation from mid pane.
     * 
     * @param automationName String object contains name of automation to be
     *            executed.
     */
    private void executeAutomationFromMidPane(String automationName) {
        traverseToAutomationTreeView();
        getAutomation(automationName).click();
        traverseToAutomationRightPane();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[@class='ToolBarCaption'][contains(text(),"
                                + "'Edit Automation')]")));
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarRunNow(waitForReload);
    }

    /**
     * Prints the log entries of automation to logger file.
     * 
     * @param automationName String object contains name of automation.
     */
    private void printLogEntry(String automationName) {
        WebElement logEntry = getLogEntryElement();
        if (!(logEntry.getText().isEmpty())) {
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
        automationPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
    }
}
