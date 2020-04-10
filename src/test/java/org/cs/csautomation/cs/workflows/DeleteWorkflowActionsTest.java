/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IWorkflowActionsPopup;
import org.cs.csautomation.cs.settings.IWorkflowsPopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.WorkflowsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains test method to delete workflow actions.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteWorkflowActionsTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private WorkflowsPage          workflowsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private SoftAssert             softAssertion;
    private IWorkflowActionsPopup  workflowActionsPopup;
    private Actions                performAction;
    private IWorkflowsPopup        workflowsPopup;
    private String                 alertText;
    private String                 dataSheetName = "DeleteWorkflowActions";
    private CSPopupDivPim          csPopupDivInstance;

    /**
     * This test method deletes workflow actions.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowType String object contains name of workflow type.
     * @param workflowStates String object contains name of workflow states.
     * @param workflowActions String object contains name of workflow actions.
     * @param workflowTabName String object contains name of workflow tab.
     */
    @Test(dataProvider = "deleteWorkflowActionsData")
    public void testDeleteWorkflowActionsTest(String workflowName,
            String workflowType, String workflowStates, String workflowActions,
            String workflowTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            String[] workflowState = workflowStates.split(",");
            String[] workflowAction = workflowActions.split(",");
            checkWorkflowExists(workflowName, workflowType);
            for (int workflowStateCount = 0; workflowStateCount < workflowState.length; workflowStateCount++) {
                checkWorkflowStateExists(workflowName,
                        workflowState[workflowStateCount]);
            }
            for (int workflowActionCount = 0; workflowActionCount < workflowAction.length; workflowActionCount++) {
                checkWorkflowActionsExists(workflowName, workflowTabName,
                        workflowAction[workflowActionCount]);
            }
            deleteWorkflowActionFromMidPane(workflowName, workflowState[0],
                    workflowTabName, workflowAction[0], false);
            deleteWorkflowActionFromMidPane(workflowName, workflowState[0],
                    workflowTabName, workflowAction[0], true);
            deleteWorkflowActionsFromTreeView(workflowName, workflowState[1],
                    workflowAction[1], false);
            deleteWorkflowActionsFromTreeView(workflowName, workflowState[1],
                    workflowAction[1], true);
            deleteWorkflowActionsFromEditActionWindow(workflowName,
                    workflowState[2], workflowTabName, workflowAction[2],
                    false);
            deleteWorkflowActionsFromEditActionWindow(workflowName,
                    workflowState[2], workflowTabName, workflowAction[2], true);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : testDeleteWorkflowActionsTest ", e);
            Assert.fail(
                    "Automation error in : " + "testDeleteWorkflowActionsTest ",
                    e);
        }
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
            CSLogger.error("Workflow do not exists", e);
            Assert.fail("Workflow do not exists");
        }
    }

    /**
     * This method traverses till workflow node under settings tab.
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
     * This method clicks on given workflow type.
     * 
     * @param workflowTypeName String object contains type of workflow.
     */
    private void clkOnWorkflowType(String workflowTypeName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowTypeName)));
        WebElement workflowType = browserDriver
                .findElement(By.linkText(workflowTypeName));
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

    /**
     * Returns the WebElement of given workflow.
     * 
     * @param workflowName String object contains name of workflow.
     * @return WebElement of workflow.
     */
    private WebElement getWorkflow(String workflowName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowName)));
        WebElement workflow = browserDriver
                .findElement(By.linkText(workflowName));
        return workflow;
    }

    /**
     * Verifies that workflow state are created and exists.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state.
     */
    private void checkWorkflowStateExists(String workflowName,
            String workflowStateName) {
        try {
            String workflowId = getWorkflowId(workflowName);
            clkOnWorkflowState(workflowId, workflowStateName);
            CSLogger.info("Workflow state : " + workflowStateName + " exists ");
        } catch (Exception e) {
            CSLogger.error("Workflow state : " + workflowStateName
                    + " does not exists", e);
            Assert.fail("Workflow state : " + workflowStateName
                    + " does not exists");
        }
    }

    /**
     * This method returns the ID of the workflow passed as argument.
     * 
     * @param workflowName String object contains name of workflow.
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
        String workflowId = getEditorInputId();
        return workflowId;
    }

    /**
     * This method will return the id from workflow action editor window's title
     * bar
     * 
     * @return inputId
     */
    private String getEditorInputId() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        String inputId = browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
        return inputId;
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
     * This method verifies that the workflow actions exists.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTabName String object contains name of workflow tab.
     * @param workflowActionName String object contains name of workflow
     *            actions.
     */
    private void checkWorkflowActionsExists(String workflowName,
            String workflowTabName, String workflowActionName) {
        try {
            goToWorkflowActions(workflowName, workflowTabName);
            CSUtility.waitForElementToBeClickable(waitForReload,
                    browserDriver.findElement(By
                            .xpath("//a[@class='ActionLabel'][contains(text(),'"
                                    + workflowActionName + "')]")));
            CSLogger.info(
                    "Workflow action : " + workflowActionName + " exists ");
        } catch (Exception e) {
            CSLogger.error("Workflow action : " + workflowActionName
                    + " does not exists", e);
            Assert.fail("Workflow action : " + workflowActionName
                    + " does not exists");
        }
    }

    /**
     * Traverses to workflow actions flowchart under diagram tab.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTabName String object contains name of workflow tab.
     */
    private void goToWorkflowActions(String workflowName,
            String workflowTabName) {
        traverseToWorkflowTreeView();
        clkOnWorkflow(workflowName);
        CSUtility.tempMethodForThreadSleep(3000);
        traverseToWorkflowCenterPane();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//span[@class='ToolBarCaption'][contains(text(),'"
                                + workflowName + "')]")));
        clickOnWorkflowTab(workflowTabName, waitForReload);
        traverseToFlowChart();
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(
                        By.xpath("//div[@id='container-content']/table")),
                browserDriver);
    }

    /**
     * Traverse frames of system preferences right section located under
     * settings tab.
     */
    private void traverseToWorkflowCenterPane() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * Traverse frames of system preferences left pane tree located under
     * settings tab.
     */
    private void traverseToWorkflowTreeView() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * Traverses to ViewWorkflow frame.
     */
    private void traverseToFlowChart() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmViewWorkflow()));
        CSLogger.info("Traversed till ViewWorkflow frame ");
    }

    /**
     * This method clicks on provided tab name.
     * 
     * @param tabName String object contains name of the tab.
     * @param waitForReload WebDriverWait object.
     */
    private void clickOnWorkflowTab(String tabName,
            WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath("//nobr[contains(text(),'"
                        + tabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By
                .xpath("//nobr[contains(text(),'" + tabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on " + tabName + " tab ");
    }

    /**
     * This method deletes the given workflow action under diagram tab from mid
     * pane i.e from flow chart of workflow.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state
     *            under which workflow action to be deleted.
     * @param workflowTabName String object contains name of workflow tab.
     * @param workflowActionName String object contains name of workflow action
     *            to be deleted.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow action or not.
     */
    private void deleteWorkflowActionFromMidPane(String workflowName,
            String workflowStateName, String workflowTabName,
            String workflowActionName, Boolean isPressOkay) {
        goToWorkflowActions(workflowName, workflowTabName);
        WebElement workflowAction = browserDriver.findElement(
                By.xpath("//a[contains(text(),'" + workflowActionName + "')]"));
        CSUtility.waitForElementToBeClickable(waitForReload, workflowAction);
        performAction.moveToElement(workflowAction).perform();
        performAction.contextClick().perform();
        workflowActionsPopup.selectPopupDivMenu(waitForReload,
                workflowActionsPopup.getCsPopupMenuDeleteAction(),
                browserDriver);
        alertText = "Do you really want to delete this item?";
        performAlertBoxOperation(isPressOkay, alertText, "mid pane");
        verifyDeletedWorkflowActionFromFlowchart(workflowActionName,
                workflowTabName, isPressOkay);
    }

    /**
     * Verifies whether the given workflow action is deleted from flowchart of
     * workflow.
     * 
     * @param workflowActionName - String instance - name of workflow action
     * @param workflowTabName - String instance - name of workflow tab name
     * @param isDeleted - Boolean value
     */
    private void verifyDeletedWorkflowActionFromFlowchart(
            String workflowActionName, String workflowTabName,
            Boolean isDeleted) {
        traverseToWorkflowCenterPane();
        traverseToFlowChart();
        CSUtility.tempMethodForThreadSleep(1000);
        List<WebElement> workflowActionExists = browserDriver.findElements(
                By.xpath("//a[contains(text(),'" + workflowActionName + "')]"));
        verifyDeletedWorkflowAction(workflowActionName, isDeleted,
                workflowActionExists);
    }

    /**
     * This method whether workflow action is deleted.
     * 
     * @param workflowActionName String object contains name of workflow action
     *            to be deleted.
     * @param isDeleted Boolean parameter contains true or false value i.e
     *            whether to delete workflow action or not.
     * @param workflowActionExists contains list of WebElement.
     */
    private void verifyDeletedWorkflowAction(String workflowActionName,
            Boolean isDeleted, List<WebElement> workflowActionExists) {
        if (isDeleted) {
            if (workflowActionExists.isEmpty()) {
                CSLogger.info("When clicked on ok of alert workflow action : "
                        + workflowActionName
                        + " deleted successfully : teststep passed");
            } else {
                CSLogger.error("When clicked on ok of alert workflow action "
                        + workflowActionName + "didn't delete " + ": "
                        + "teststep failed");
                softAssertion
                        .fail("When clicked on ok of alert workflow action "
                                + workflowActionName + "didn't delete " + ": "
                                + "teststep failed");
            }
        } else {
            if (workflowActionExists.isEmpty()) {
                CSLogger.error("When clicked cancel of alert workflow action "
                        + workflowActionName + "deleted : "
                        + "teststep failed");
                softAssertion
                        .fail("When clicked cancel of alert workflow action "
                                + workflowActionName + "deleted : "
                                + "teststep failed");
            } else {
                CSLogger.info("When clicked cancel of alert workflow action "
                        + workflowActionName + "deleted : "
                        + "teststep failed");
            }
        }
    }

    /**
     * * Handles the alert box operations i.e clicks on ok or cancel of alert
     * box.
     * 
     * @param isPressOkay Boolean parameter contains true or false values.
     * @param alertText String object contains title text of alert box.
     * @param comment String object contains logger comments.
     */
    private void performAlertBoxOperation(Boolean isPressOkay, String alertText,
            String comment) {
        Alert getAlertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (!(getAlertBox.getText().equals(alertText))) {
            CSLogger.error(
                    "While deleting automation from " + comment
                    + " found alert text : " + getAlertBox.getText()
                    + " instead of " + alertText);
            softAssertion.fail("While deleting automation from " + comment
                    + " found alert text : " + getAlertBox.getText()
                    + " instead of " + alertText);
        }
        if (isPressOkay) {
            getAlertBox.accept();
            CSLogger.info("Clicked on ok of alert box");
        } else {
            getAlertBox.dismiss();
            CSLogger.info("Clicked on cancel of alert box");
        }
    }

    /**
     * This method deletes the given workflow actions from tree view.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state
     *            under which workflow action to be deleted.
     * @param workflowActionName String object contains name of workflow action
     *            to be deleted.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow action or not.
     * 
     */
    private void deleteWorkflowActionsFromTreeView(String workflowName,
            String workflowStateName, String workflowActionName,
            Boolean isPressOkay) {
        traverseToWorkflowTreeView();
        clkOnWorkflow(workflowName);
        String workflowId = getWorkflowId(workflowName);
        clkOnWorkflowState(workflowId, workflowStateName);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + workflowActionName + "')]")));
        WebElement workflowAction = browserDriver.findElement(By.xpath(
                "//span[contains(text(),'" + workflowActionName + "')]"));
        CSUtility.rightClickTreeNode(waitForReload, workflowAction,
                browserDriver);
        workflowActionsPopup.selectPopupDivMenu(waitForReload,
                workflowActionsPopup.getCsPopupMenuDeleteAction(),
                browserDriver);
        String alertText = "Do you really want to delete this item?";
        performAlertBoxOperation(isPressOkay, alertText, "tree view");
        verifyDeletedWorkflowActionFromTreeView(workflowName, workflowStateName,
                workflowActionName, workflowAction, isPressOkay);
    }

    /**
     * This method deletes the given workflow actions from 'Edit Action' window.
     * 
     * @param workflowName String object contains name of workflow.
     * 
     * @param workflowStateName String object contains name of workflow state
     *            under which workflow action to be deleted.
     * @param workflowTabName String object contains name of workflow tab.
     * @param workflowActionName String object contains name of workflow action
     *            to be deleted.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow action or not.
     * 
     */
    private void deleteWorkflowActionsFromEditActionWindow(String workflowName,
            String workflowStateName, String workflowTabName,
            String workflowActionName, Boolean isPressOkay) {
        goToWorkflowActions(workflowName, workflowTabName);
        WebElement workflowAction = browserDriver.findElement(
                By.xpath("//a[@class='ActionLabel'][contains(text(),'"
                        + workflowActionName + "')]"));
        String parentWindow = browserDriver.getWindowHandle();
        workflowAction.click();
        CSUtility.tempMethodForThreadSleep(1000);
        String workflowActionWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(workflowActionWindow);
        String workflowActionId = getEditorInputId();
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        csPopupDivInstance.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csGuiToolbarHorizontalInstance.getDrpDwnCSGuiToolbarActions());
        csPopupDivInstance.selectPopupDivSubMenu(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarDelete(),
                browserDriver);
        alertText = "Do you really want to delete the item "
                + "indicated in the following: ID " + workflowActionId + "?";
        performAlertBoxOperation(isPressOkay, alertText, "edit action window");
        if (!(isPressOkay)) {
            browserDriver.close();
        }
        browserDriver.switchTo().window(parentWindow);
        verifyDeletedWorkflowActionFromFlowchart(workflowActionName,
                workflowTabName, isPressOkay);
    }

    /**
     * Verifies whether the workflow actions is deleted from tree.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state.
     * @param workflowActionName String object contains name of workflow action.
     * @param isDeleted Boolean parameter contains true or false values i.e
     *            whether to delete workflow action or not.
     */
    private void verifyDeletedWorkflowActionFromTreeView(String workflowName,
            String workflowStateName, String workflowActionName,
            WebElement workflowAction, Boolean isDeleted) {
        traverseToWorkflowTreeView();
        CSUtility.rightClickTreeNode(waitForReload, getWorkflow(workflowName),
                browserDriver);
        workflowsPopup.selectPopupDivMenu(waitForReload,
                workflowsPopup.getCsPopupRefresh(), browserDriver);
        String workflowId = getWorkflowId(workflowName);
        clkOnWorkflowState(workflowId, workflowStateName);
        CSUtility.tempMethodForThreadSleep(1000);
        List<WebElement> workflowActionExists = browserDriver
                .findElements(By.linkText(workflowActionName));
        verifyDeletedWorkflowAction(workflowActionName, isDeleted,
                workflowActionExists);
    }

    /**
     * This is a data provider method that contains data to delete workflow
     * actions.
     * 
     * @return Array
     */
    @DataProvider(name = "deleteWorkflowActionsData")
    public Object[][] getDeleteWorkflowActionsData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("workflowModuleTestCases"),
                dataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        workflowsPageInstance = SettingsLeftSectionMenubar
                .getWorkflowsNode(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        softAssertion = new SoftAssert();
        workflowActionsPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        performAction = new Actions(browserDriver);
        workflowsPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        csPopupDivInstance = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }
}
