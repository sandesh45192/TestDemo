/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IWorkflowsPopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.WorkflowsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains test methods to create a workflow states.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateWorkflowStatesTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private WorkflowsPage          workflowsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private Actions                performAction;
    private SoftAssert             softAssertion;
    private IWorkflowsPopup        workflowsPopup;
    private String                 createWorkflowStateSheetName = "CreateWorkflowState";

    /**
     * This test method depends on 'testCreateNewWorkflow',and creates states of
     * workflow.
     * 
     * @param workflowName String object contains name of the workflow.
     * @param workflowStateName String object specifies name of the state of
     *            workflow.
     * @param workflowStateColor String object specifies color of states.
     * @param workflowStatePosition String object specifies position of state.
     * @param workflowStateDescription String object contains description about
     *            the state of workflow.
     * @param numberOfStates String object specifies number of workflow states
     *            to be created.
     */
    @Test(dataProvider = "createWorkflowStateTestData")
    public void testCreateWorkflowState(String workflowName,
            String workflowType, String workflowStateName,
            String workflowStateColor, String workflowStatePosition,
            String workflowStateDescription, String numberOfStates) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            checkWorkflowExists(workflowName, workflowType);
            doubleClkOnWorkflow(workflowName);
            int currentPosition = Integer.parseInt(workflowStatePosition);
            for (int stateCount = 1; stateCount <= Integer
                    .parseInt(numberOfStates); stateCount++) {
                createWorkflowState(workflowName,
                        workflowStateName + stateCount, workflowStateColor,
                        Integer.toString(currentPosition),
                        workflowStateDescription);
                currentPosition = currentPosition + 10;
            }
            verifyCreationOfWorkflowState(workflowName, workflowStateName,
                    numberOfStates);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testCreateNewWorkflowState",
                    e);
            Assert.fail("Automation error in : testCreateNewWorkflowState", e);
        }
    }

    /**
     * This method performs double click operation on workflow.
     * 
     * @param workflowName String object contains name of workflow.
     */
    private void doubleClkOnWorkflow(String workflowName) {
        performAction.doubleClick(getWorkflow(workflowName)).perform();
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * Checks whether the created workflow exists.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains type of workflow.
     */
    private void checkWorkflowExists(String workflowName,
            String workflowTypeName) {
        try {
            goToWorkflowNode();
            clkOnWorkflowType(workflowTypeName);
            clkOnWorkflow(workflowName);
            CSUtility.tempMethodForThreadSleep(1000);
            CSLogger.info("Workflow " + workflowName + " exists");
        } catch (Exception e) {
            CSLogger.error("Workflow does not exist", e);
            Assert.fail("Workflow does not exist");
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
     * This method clicks on given workflow type.
     * 
     * @param workflowTypeName String object contains type of workflow.
     */
    private void clkOnWorkflowType(String workflowTypeName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + workflowTypeName + "')]")));
        WebElement workflowType = browserDriver.findElement(By
                .xpath("//span[contains(text(),'" + workflowTypeName + "')]"));
        workflowType.click();
        CSLogger.info("Clicked on workflow type : " + workflowTypeName);
    }

    /**
     * This method clicks on given workflow.
     * 
     * @param workflowName String object contains name of workflow.
     */
    private void clkOnWorkflow(String workflowName) {
        getWorkflow(workflowName).click();
        CSLogger.info("Clicked on workflow : " + workflowName);
    }

    private WebElement getWorkflow(String workflowName) {
        // CSUtility.waitForElementToBeClickable(waitForReload,
        // browserDriver.findElement(By.linkText(workflowName)));
        // WebElement workflow = browserDriver
        // .findElement(By.linkText(workflowName));

        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + workflowName + "')]")));
        WebElement workflow = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + workflowName + "')]"));

        return workflow;
    }

    /**
     * Creates states of workflow.
     * 
     * @param workflowStateName String object contains name of the workflow.
     * @param workflowStateColor String object specifies color of states.
     * @param workflowStatePosition String object specifies position of state.
     * @param workflowStateDescription String object contains description about
     *            the state of workflow.
     */
    private void createWorkflowState(String workflowName,
            String workflowStateName, String workflowStateColor,
            String workflowStatePosition, String workflowStateDescription) {
        try {
            String parentWindow = browserDriver.getWindowHandle();
            traverseToWorkflowCenterPane();
            CSUtility.waitForElementToBeClickable(waitForReload,
                    browserDriver
                            .findElement(By.xpath("//span[contains(text(),'"
                                    + workflowName + "')]")));
            traverseToCreateNewStateToolbarButton();
            clkOnNewStateToolbarButton();
            String newWorkflowStateWindow = (String) (browserDriver
                    .getWindowHandles().toArray())[1];
            browserDriver.switchTo().window(newWorkflowStateWindow);
            CSUtility.waitForElementToBeClickable(waitForReload,
                    browserDriver.findElement(By.xpath(
                            "//textarea[contains(@id,'StateDescription')]")));
            WebElement stateName = browserDriver.findElement(
                    By.xpath("//input[contains(@id,'StateStateName')]"));
            WebElement stateColor = browserDriver.findElement(
                    By.xpath("//select[contains(@id,'StateColorID')]"));
            WebElement statePosition = browserDriver.findElement(
                    By.xpath("//input[contains(@id,'StateStateType')]"));
            WebElement stateDescription = browserDriver.findElement(
                    By.xpath("//textarea[contains(@id,'StateDescription')]"));
            configureWorkflowState(workflowStateName, workflowStateColor,
                    workflowStatePosition, workflowStateDescription, stateName,
                    stateColor, statePosition, stateDescription);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            browserDriver.close();
            browserDriver.switchTo().window(parentWindow);
        } catch (NoSuchWindowException e) {
            CSLogger.error("A window 'Add new WorkFlow Action' didn't pop up.",
                    e);
            Assert.fail("A window 'Add new WorkFlow Action' didn't pop up.");
        }
    }

    /**
     * Clicks on new state tool bar button.
     */
    private void clkOnNewStateToolbarButton() {
        CSUtility.waitForElementToBeClickable(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarNewState());
        csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarNewState().click();
        CSLogger.info("Clicked on new state tool bar button");
    }

    /**
     * Enters workflow state information for eg. workflow state name,start
     * position,color and description.
     * 
     * @param workflowStateName String object contains name of the workflow.
     * @param workflowStateColor String object specifies color of states.
     * @param workflowStatePosition String object specifies position of state.
     * @param workflowStateDescription String object contains description about
     *            the state of workflow.
     * @param stateName WebElement of workflow state name.
     * @param stateColor WebElement of workflow state color.
     * @param statePosition WebElement of workflow state position.
     * @param stateDescription WebElement of workflow state description.
     */
    private void configureWorkflowState(String workflowStateName,
            String workflowStateColor, String workflowStatePosition,
            String workflowStateDescription, WebElement stateName,
            WebElement stateColor, WebElement statePosition,
            WebElement stateDescription) {
        enterNewStateDetails(stateName, workflowStateName);
        selectWorkflowStateColor(stateColor, workflowStateColor);
        enterNewStateDetails(statePosition, workflowStatePosition);
        enterNewStateDetails(stateDescription, workflowStateDescription);
    }

    /**
     * This method enters workflow state information for eg. workflow state
     * name,start position and description.
     * 
     * @param stateElement WebElement of workflow state.
     * @param stateInfo String object contains information about the workflow
     *            state.
     */
    private void enterNewStateDetails(WebElement stateElement,
            String stateInfo) {
        CSUtility.waitForElementToBeClickable(waitForReload, stateElement);
        stateElement.click();
        stateElement.clear();
        stateElement.sendKeys(stateInfo);
        CSLogger.info("Entered workflow state detail as : " + stateInfo);
    }

    /**
     * Selects Workflow state color.
     * 
     * @param stateColor WebElement of workflow state color.
     * @param valueOfDrpDwn String object contains color of workflow state.
     */
    private void selectWorkflowStateColor(WebElement stateColor,
            String valueOfDrpDwn) {
        stateColor.click();
        Select element = new Select(stateColor);
        element.selectByVisibleText(valueOfDrpDwn);
        CSLogger.info("Drop down option " + valueOfDrpDwn + " selected");
    }

    /**
     * Traverses till SplitAreaFrame.
     */
    private void traverseToCreateNewStateToolbarButton() {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, iframeLocatorsInstance);
        iframeLocatorsInstance.switchToSplitAreaFrame(waitForReload);
    }

    /**
     * This method verifies whether workflow state are created.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state.
     * @param numberOfStates String object contains number of workflow created.
     */
    private void verifyCreationOfWorkflowState(String workflowName,
            String workflowStateName, String numberOfStates) {
        traverseToWorkflowTreeView();
        clkOnWorkflow(workflowName);
        traverseToWorkflowTreeView();
        CSUtility.rightClickTreeNode(waitForReload, getWorkflow(workflowName),
                browserDriver);
        workflowsPopup.selectPopupDivMenu(waitForReload,
                workflowsPopup.getCsPopupRefresh(), browserDriver);
        String workflowId = getWorkflowId(workflowName);
        try {
            for (int stateCount = 1; stateCount <= Integer
                    .parseInt(numberOfStates); stateCount++) {
                clkOnWorkflowState(workflowId, workflowStateName);
                CSLogger.info(
                        "Workflow state : " + workflowStateName + stateCount
                                + " created successfully : teststep passed");
            }
        } catch (Exception e) {
            CSLogger.error("Workflow state : " + workflowStateName
                    + " not created : teststep failed");
            softAssertion.fail("Workflow state : " + workflowStateName
                    + " not created : teststep failed");
        }
    }

    /**
     * This method returns the ID of the workflow passed as argument
     * 
     * @param workflowName String containing name of workflow.
     * @return workflow Id String object contains workflow ID.
     */
    private String getWorkflowId(String workflowName) {
        traverseToWorkflowTreeView();
        performAction
                .doubleClick(
                        browserDriver.findElement(By.linkText(workflowName)))
                .perform();
        traverseToWorkflowCenterPane();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + workflowName + "')]")));
        CSUtility.tempMethodForThreadSleep(5000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        String workflowId = browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
        return workflowId;
    }

    /**
     * Switches frames of system preferences center pane located under setting's
     * tab.
     */
    private void traverseToWorkflowCenterPane() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * This methods clicks on given workflow state.
     * 
     * @param workflowId String object contains Id of workflow.
     * @param workflowStateName String object contains workflow state name.
     */
    private void clkOnWorkflowState(String workflowId,
            String workflowStateName) {
        traverseToWorkflowTreeView();
        WebElement workflowState = browserDriver.findElement(
                By.xpath("//span[@id='0divWorkflows@Workflow_" + workflowId
                        + "']/span/table/tbody/tr/td/span/table/tbody/tr/td[2]"
                        + "/a/span/span[contains(text(),'" + workflowStateName
                        + "')]"));
        CSUtility.waitForElementToBeClickable(waitForReload, workflowState);
        workflowState.click();
        CSLogger.info("Clicked on workflow State : " + workflowStateName);
    }

    /**
     * This is a data provider method that contains data to create workflow
     * states.
     * 
     * @return Array
     */
    @DataProvider(name = "createWorkflowStateTestData")
    public Object[][] getWorkflowStateData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("workflowModuleTestCases"),
                createWorkflowStateSheetName);
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
        workflowsPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
    }

}
