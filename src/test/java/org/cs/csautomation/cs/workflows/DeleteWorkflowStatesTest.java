/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IWorkflowStatesPopup;
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
 * This class contains test method to delete workflow states.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteWorkflowStatesTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private WorkflowsPage          workflowsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private SoftAssert             softAssertion;
    private Actions                performAction;
    private IWorkflowsPopup        workflowsPopup;
    private String                 alertText;
    private String                 deleteStateAlertText;
    private IWorkflowStatesPopup   workflowStatesPopup;
    private String                 dataSheetName = "DeleteWorkflowStates";
    private CSPopupDivPim          csPopupDivInstance;
    private String                 workflowStateId;

    /**
     * This test method deletes the given workflow states.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowType String object contains name of workflow type
     * @param workflowStates String object contains names of workflow states
     * @param workflowTabName String object contains name of workflow tab.
     */
    @Test(dataProvider = "deleteWorkflowStatesData")
    public void testDeleteWorkflowStates(String workflowName,
            String workflowType, String workflowStates,
            String workflowTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            String[] workflowState = workflowStates.split(",");
            checkWorkflowExists(workflowName, workflowType);
            for (int workflowStateCount = 0; workflowStateCount < workflowState.length; workflowStateCount++) {
                checkWorkflowStateExists(workflowName,
                        workflowState[workflowStateCount]);
            }
            deleteWorkflowStateFromMidPane(workflowName, workflowState[0],
                    workflowTabName, false);
            deleteWorkflowStateFromMidPane(workflowName, workflowState[0],
                    workflowTabName, true);
            deleteWorkflowStatesFromTreeView(workflowName, workflowState[1],
                    false);
            deleteWorkflowStatesFromTreeView(workflowName, workflowState[1],
                    true);
            deleteWorkflowStatesFromEditStateWindow(workflowName,
                    workflowState[2], workflowTabName, false);
            deleteWorkflowStatesFromEditStateWindow(workflowName,
                    workflowState[2], workflowTabName, true);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testDeleteWorkflowStates ",
                    e);
            Assert.fail("Automation error in : testDeleteWorkflowStates ", e);
        }
    }

    /**
     * Checks whether the given workflow exists.
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
     * Verifies that given workflow state are created and exists.
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
     * This methods clicks on given workflow state.
     * 
     * @param workflowId String object contains Id of workflow.
     * @param workflowStateName String object contains workflow state name.
     */
    private void clkOnWorkflowState(String workflowId,
            String workflowStateName) {
        WebElement workflowState = getWorkflowState(workflowId,
                workflowStateName);
        workflowState.click();
        CSLogger.info("Clicked on workflow State : " + workflowStateName);
    }

    /**
     * This method returns the workflow state's WebElement .
     * 
     * @param workflowId String object contains workflow ID.
     * @param workflowStateName String object contains name of workflow state.
     * @return WebElement of workflow state.
     */
    private WebElement getWorkflowState(String workflowId,
            String workflowStateName) {
        traverseToWorkflowTreeView();
        CSUtility.tempMethodForThreadSleep(3000);
        WebElement workflowState = browserDriver.findElement(
                By.xpath("//span[@id='0divWorkflows@Workflow_" + workflowId
                        + "']/span/table/tbody/tr/td/span/table/tbody/tr/td[2]"
                        + "/a/span/span[contains(text(),'" + workflowStateName
                        + "')]"));
        CSUtility.waitForElementToBeClickable(waitForReload, workflowState);
        return workflowState;
    }

    /**
     * This method deletes the given workflow state under diagram tab from mid
     * pane i.e from flow chart of workflow.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state to
     *            be deleted.
     * @param workflowTabName String object contains name of workflow tab.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow state or not.
     */
    private void deleteWorkflowStateFromMidPane(String workflowName,
            String workflowStateName, String workflowTabName,
            Boolean isPressOkay) {
        goToWorkflowState(workflowName, workflowTabName);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath("//td[contains(text(),'"
                        + workflowStateName + "')]/../../..")));
        WebElement workflowState = browserDriver.findElement(By.xpath(
                "//td[contains(text(),'" + workflowStateName + "')]/../../.."));
        performAction.moveToElement(workflowState).perform();
        performAction.contextClick().perform();
        workflowStatesPopup.selectPopupDivMenu(waitForReload,
                workflowStatesPopup.getCsPopupMenuDeleteState(), browserDriver);
        alertText = "Do you really want to delete this item?";
        performAlertBoxOperation(isPressOkay, alertText, "mid pane");
        verifyDeletedWorkflowStateFromFlowchart(workflowName, workflowStateName,
                workflowTabName, isPressOkay);
    }

    /**
     * Verifies whether the given workflow state is deleted from flowchart of
     * workflow
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state.
     * @param workflowTabName String object contains name of workflow tab.
     * @param isDeleted Boolean parameter contains true or false values.
     */
    private void verifyDeletedWorkflowStateFromFlowchart(String workflowName,
            String workflowStateName, String workflowTabName,
            Boolean isDeleted) {
        CSUtility.tempMethodForThreadSleep(1000);
        traverseToWorkflowCenterPane();
        traverseToFlowChart();
        List<WebElement> workflowStateExists = browserDriver
                .findElements(By.xpath("//td[contains(text(),'"
                        + workflowStateName + "')]/../../.."));
        verifyDeletedWorkflowState(workflowStateName, isDeleted,
                workflowStateExists);
    }

    /**
     * Traverses to workflow states flowchart under diagram tab.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTabName String object contains name of workflow tab.
     */
    private void goToWorkflowState(String workflowName,
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
     * Switches frames of system preferences center pane located under setting's
     * tab.
     */
    private void traverseToWorkflowCenterPane() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
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
            CSLogger.error("While deleting automation from " + comment
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
     * Traverses to ViewWorkflow frame.
     */
    private void traverseToFlowChart() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmViewWorkflow()));
        CSLogger.info("Traversed till ViewWorkflow frame ");
    }

    /**
     * Verifies whether the workflow states is deleted.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state.
     * @param isDeleted Boolean parameter contains true or false values i.e
     *            whether to delete workflow state or not.
     */
    private void verifyDeletedWorkflowStateFromTreeView(String workflowName,
            String workflowStateName, Boolean isDeleted) {
        traverseToWorkflowTreeView();
        CSUtility.rightClickTreeNode(waitForReload, getWorkflow(workflowName),
                browserDriver);
        workflowsPopup.selectPopupDivMenu(waitForReload,
                workflowsPopup.getCsPopupRefresh(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        String workflowId = getWorkflowId(workflowName);
        traverseToWorkflowTreeView();
        List<WebElement> workflowStateExists = browserDriver.findElements(
                By.xpath("//span[@id='0divWorkflows@Workflow_" + workflowId
                        + "']/span/table/tbody/tr/td/span/table/tbody/tr/td[2]"
                        + "/a/span/span[contains(text(),'" + workflowStateName
                        + "')]"));
        verifyDeletedWorkflowState(workflowStateName, isDeleted,
                workflowStateExists);
    }

    /**
     * This method whether workflow state is deleted.
     * 
     * @param workflowStateName String object contains name of workflow state to
     *            be deleted.
     * @param isDeleted Boolean parameter contains true or false value i.e
     *            whether to delete workflow state or not.
     * @param workflowStateExists contains list of WebElement.
     */
    private void verifyDeletedWorkflowState(String workflowStateName,
            Boolean isDeleted, List<WebElement> workflowStateExists) {
        if (isDeleted) {
            if (workflowStateExists.isEmpty()) {
                CSLogger.info("When clicked on ok of alert workflow state : "
                        + workflowStateName
                        + " deleted successfully : test step passed");
            } else {
                CSLogger.error("When clicked on ok of alert workflow state "
                        + workflowStateName + "didn't delete " + ": "
                        + "test step failed");
                softAssertion.fail("When clicked ok of alert workflow state "
                        + workflowStateName + "didn't "
                        + "delete : test step failed");
            }
        } else {
            if (workflowStateExists.isEmpty()) {
                CSLogger.error("When clicked cancel of alert workflow state "
                        + workflowStateName + "deleted : "
                        + "test step failed");
                softAssertion
                        .fail("When clicked cancel of alert workflow state "
                                + workflowStateName + "deleted : "
                                + "test step failed");
            } else {
                CSLogger.info("When clicked on cancel of alert workflow state "
                        + workflowStateName + "didn't "
                        + "delete : test step passed");
            }
        }
    }

    /**
     * This method deletes the given workflow state from tree view.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state to
     *            be deleted.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow state or not.
     * 
     */
    private void deleteWorkflowStatesFromTreeView(String workflowName,
            String workflowStateName, Boolean isPressOkay) {
        traverseToWorkflowTreeView();
        clkOnWorkflow(workflowName);
        String workflowId = getWorkflowId(workflowName);
        WebElement workflowState = getWorkflowState(workflowId,
                workflowStateName);
        CSUtility.rightClickTreeNode(waitForReload, workflowState,
                browserDriver);
        workflowStatesPopup.selectPopupDivMenu(waitForReload,
                workflowStatesPopup.getCsPopupMenuDeleteState(), browserDriver);
        String alertText = "Do you really want to delete this item?";
        performAlertBoxOperation(isPressOkay, alertText, "tree view");
        verifyDeletedWorkflowStateFromTreeView(workflowName, workflowStateName,
                isPressOkay);
    }

    /**
     * This method deletes the given workflow state from 'Edit Workflow State'
     * window.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state to
     *            be deleted.
     * @param workflowTabName String object contains name of workflow tab.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow state or not.
     * 
     */
    private void deleteWorkflowStatesFromEditStateWindow(String workflowName,
            String workflowStateName, String workflowTabName,
            Boolean isPressOkay) {
        String workflowId = getWorkflowId(workflowName);
        WebElement workflowState = getWorkflowState(workflowId,
                workflowStateName);
        performAction.doubleClick(workflowState).perform();
        CSUtility.waitForVisibilityOfElement(waitForReload, workflowState);
        CSUtility.tempMethodForThreadSleep(5000);
        traverseToWorkflowCenterPane();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        String workflowStateId = browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
        goToWorkflowState(workflowName, workflowTabName);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath("//td[contains(text(),'"
                        + workflowStateName + "')]/../../..")));
        WebElement windowWorkflowState = browserDriver.findElement(By.xpath(
                "//td[contains(text(),'" + workflowStateName + "')]/../../.."));
        String parentWindow = browserDriver.getWindowHandle();
        windowWorkflowState.click();
        CSUtility.tempMethodForThreadSleep(1000);
        String editWorkflowStateWindow = (String) (browserDriver
                .getWindowHandles().toArray())[1];
        browserDriver.switchTo().window(editWorkflowStateWindow);
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        csPopupDivInstance.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csGuiToolbarHorizontalInstance.getDrpDwnCSGuiToolbarState());
        csPopupDivInstance.selectPopupDivSubMenu(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarDelete(),
                browserDriver);
        deleteStateAlertText = "Do you really want to delete the item "
                + "indicated in the following: " + workflowStateName;
        CSUtility.tempMethodForThreadSleep(5000);
        performAlertBoxOperation(isPressOkay, deleteStateAlertText,
                "edit state window");
        if (!(isPressOkay)) {
            browserDriver.close();
        }
        browserDriver.switchTo().window(parentWindow);
        verifyDeletedWorkflowStateFromFlowchart(workflowName, workflowStateName,
                workflowTabName, isPressOkay);
    }

    /**
     * This is a data provider method that contains data to delete workflow
     * states.
     * 
     * @return Array
     */
    @DataProvider(name = "deleteWorkflowStatesData")
    public Object[][] getDeleteWorkflowStatesData() {
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
        performAction = new Actions(browserDriver);
        workflowsPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        workflowStatesPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        csPopupDivInstance = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }
}
