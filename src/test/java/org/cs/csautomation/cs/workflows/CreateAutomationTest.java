/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.workflows;

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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains test methods to create a automation.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateAutomationTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private AutomationsPage        automationsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private IAutomationNodePopup   automationNodePopup;
    private String                 dataSheetName = "CreateAutomation";

    /**
     * This test method creates a automation.
     * 
     * @param automationName
     *            String object contains name of automation to be created.
     * @param automationDescription
     *            String object contains automation description.
     * @param autoRunFrequencyOption
     *            String object contains auto run frequency to be set for
     *            automation.
     * @param standardActionOption
     *            String object contains standard action to be selected for
     *            automation.
     */
    @Test(dataProvider = "createAutomationTestData")
    public void testCreateAutomation(String automationName,
            String automationDescription, String autoRunFrequency,
            String standardAction) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 50);
            goToAutomationsNode();
            createNewAutomation();
            configureAutomation(automationName, automationDescription,
                    autoRunFrequency, standardAction);
            saveAutomation();
            verifyCreatedAutomation(automationName);
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testCreateAutomation", e);
            Assert.fail("Automation error in : testCreateAutomation", e);
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
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
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
    private void verifyCreatedAutomation(String automationName) {
        traverseToAutomationTreeView();
        CSUtility.rightClickTreeNode(waitForReload,
                automationsPageInstance.getBtnAutomationsNode(), browserDriver);
        automationNodePopup.selectPopupDivMenu(waitForReload,
                automationNodePopup.getCsPopupRefresh(), browserDriver);
        try {
            traverseToAutomationTreeView();
            browserDriver.findElement(By
                    .xpath("//span[contains(@id,'Automations@')][contains(text(),'"
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
     * This is a data provider method that contains data to create automation.
     * 
     * @return Array
     */
    @DataProvider(name = "createAutomationTestData")
    public Object[][] getCreateAutomationData() {
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
    }
}
