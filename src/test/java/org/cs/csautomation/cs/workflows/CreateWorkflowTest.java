/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IWorkflowsNodePopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.WorkflowsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains test methods to create a workflow and its states.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateWorkflowTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private WorkflowsPage          workflowsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private IWorkflowsNodePopup    workflowsNodePopup;
    private Actions                performAction;
    private SoftAssert             softAssertion;
    private String                 createWorkflowSheetName = "CreateWorkflow";

    /**
     * This test method creates a workflow.
     * 
     * @param workflowName String object contains name of the workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowState String object contains position of start state.
     * @param workflowDescription String object contains description about the
     *            workflow.
     */
    @Test(dataProvider = "createWorkflowTestData")
    public void testCreateWorkflow(String workflowName, String workflowType,
            String workflowState, String workflowDescription) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            goToWorkflowNode();
            createNewWorkflow();
            configureWorkflow(workflowName, workflowType, workflowState,
                    workflowDescription);
            saveWorkflow();
            verifyCreationOfWorkflow(workflowName, workflowType);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testCreateNewWorkflow", e);
            Assert.fail("Automation error in : testCreateNewWorkflow", e);
        }
    }

    /**
     * This method clicks on workflow node under settings tab.
     */
    private void goToWorkflowNode() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        traverseToWorkflowTreeView();
        workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
    }

    /**
     * Switches frames of system preferences left pane tree located under
     * setting's tab.
     */
    private void traverseToWorkflowTreeView() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * Selects 'New Workflow' popup option by right clicking on workflow node to
     * create new workflow.
     */
    private void createNewWorkflow() {
        CSUtility.rightClickTreeNode(waitForReload,
                workflowsPageInstance.getBtnWorkflowsNode(), browserDriver);
        workflowsNodePopup.selectPopupDivMenu(waitForReload,
                workflowsNodePopup.getCsPopupMenuNewWorkflows(), browserDriver);
    }

    /**
     * Enters workflow information for eg. workflow name,start state,workflow
     * type, and description.
     * 
     * @param workflowName String object contains name of the workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowState String object contains position of start state.
     * @param workflowDescription String object contains description about the
     *            workflow.
     */
    private void configureWorkflow(String workflowName, String workflowType,
            String workflowState, String workflowDescription) {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        workflowsPageInstance.enterWorkflowDetails(workflowName,
                workflowsPageInstance.getTxtWorkflowName(), waitForReload);
        workflowsPageInstance.selectWorkflowType(workflowType);
        workflowsPageInstance.enterWorkflowDetails(workflowType,
                workflowsPageInstance.getDrpDwnWorkflowType(), waitForReload);
        workflowsPageInstance.enterWorkflowDetails(workflowDescription,
                workflowsPageInstance.getTxtWorkflowDescription(),
                waitForReload);
        CSLogger.info("Workflow configured  successfully.");
    }

    /**
     * Saves the configured workflow and workflow state.
     */
    private void saveWorkflow() {
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * Clicks on newly created workflow.
     * 
     * @param workflowName String object contains name of the workflow.
     */
    private void clkOnCreatedWorkflow(String workflowName) {
        traverseToWorkflowTreeView();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowName)));
        WebElement createdWorkflow = browserDriver
                .findElement(By.linkText(workflowName));
        performAction.doubleClick(createdWorkflow).perform();
    }

    /**
     * This method verifies whether the workflow is created.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains name of workflow type.
     */
    private void verifyCreationOfWorkflow(String workflowName,
            String workflowTypeName) {
        traverseToWorkflowTreeView();
        CSUtility.rightClickTreeNode(waitForReload,
                getWorkflowType(workflowTypeName), browserDriver);
        workflowsNodePopup.selectPopupDivMenu(waitForReload,
                workflowsNodePopup.getCsPopupRefresh(), browserDriver);
        try {
            clkOnCreatedWorkflow(workflowName);
            CSLogger.info("Workflow " + workflowName
                    + " created successfully : teststep passed");
        } catch (Exception e) {
            CSLogger.error("Workflow " + workflowName
                    + " creation failed : teststep failed");
            softAssertion.fail("Workflow " + workflowName
                    + " creation failed : teststep failed");
        }
    }

    /**
     * Returns the WebElement of workflow type.
     * 
     * @param workflowTypeName String object contains name of workflow type.
     * @return WebElement of workflow type.
     */
    private WebElement getWorkflowType(String workflowTypeName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + workflowTypeName + "')]")));
        // WebElement workflowType = browserDriver
        // .findElement(By.linkText(workflowTypeName));
        WebElement workflowType = browserDriver.findElement(By
                .xpath("//span[contains(text(),'" + workflowTypeName + "')]"));
        return workflowType;
    }

    /**
     * This is a data provider method that contains data to create workflow.
     * 
     * @return Array
     */
    @DataProvider(name = "createWorkflowTestData")
    public Object[][] getWorkflowData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("workflowModuleTestCases"),
                createWorkflowSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        workflowsPageInstance = SettingsLeftSectionMenubar
                .getWorkflowsNode(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        performAction = new Actions(browserDriver);
        workflowsNodePopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
    }
}
