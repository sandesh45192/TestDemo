/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import java.util.List;
/**
 * This class contains test method to delete workflows.
 * 
 * @author CSAutomation Team
 *
 */
import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains test method to delete workflow.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteWorkflowTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private WorkflowsPage          workflowsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private SoftAssert             softAssertion;
    private IWorkflowsPopup        workflowsPopup;
    private String                 alertText;
    private CSGuiListFooter        CSGuiListFooterInstance;
    private Actions                performAction;
    private CSPopupDivPim          csPopupDivInstance;
    private String                 dataSheetName = "DeleteWorkflow";

    /**
     * This test method deletes the workflow.
     * 
     * @param workflowName String object contains array of workflow name to be
     *            deleted separated by comma.
     * @param workflowType String object contains name of workflow type.
     */
    @Test(dataProvider = "deleteWorkflowData")
    public void testDeleteWorkflow(String workflowName, String workflowType) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            String workflow[] = workflowName.split(",");
            goToWorkflowNode();
            for (int workflowCount = 0; workflowCount < workflow.length; workflowCount++) {
                checkWorkflowExists(workflow[workflowCount], workflowType);
            }
            deleteWorkflowFromMidPane(workflow[0], workflowType, false);
            deleteWorkflowFromMidPane(workflow[0], workflowType, true);
            deleteWorkflowByMassEditingOption(workflow[1], workflowType, false);
            deleteWorkflowByMassEditingOption(workflow[1], workflowType, true);
            deleteWorkflowFromTreeView(workflow[2], workflowType, false);
            deleteWorkflowFromTreeView(workflow[2], workflowType, true);
            deleteWorkflowFromListView(workflow[3], workflowType, false);
            deleteWorkflowFromListView(workflow[3], workflowType, true);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testDeleteWorkflow ", e);
            Assert.fail("Automation error in : testDeleteWorkflow ", e);
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
            workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
            clkOnWorkflowType(workflowTypeName);
            clkOnWorkflow(workflowName);
            CSUtility.tempMethodForThreadSleep(1000);
            CSLogger.info("Workflow " + workflowName + " exists");
        } catch (Exception e) {
            CSLogger.error("Workflow does not exists", e);
            Assert.fail("Workflow does not exists");
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
        getWorkflowType(workflowTypeName).click();
        CSLogger.info("Clicked on workflow type : " + workflowTypeName);
    }

    /**
     * This method returns the WebElement of workflow type.
     * 
     * @param workflowTypeName String object contains name of workflow type.
     * @return WebElement of workflow type.
     */
    private WebElement getWorkflowType(String workflowTypeName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowTypeName)));
        WebElement workflowType = browserDriver
                .findElement(By.linkText(workflowTypeName));
        return workflowType;
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
     * This method deletes the given workflow from mid pane.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains name of workflow type.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow or not.
     */
    private void deleteWorkflowFromMidPane(String workflowName,
            String workflowTypeName, Boolean isPressOkay) {
        traverseToWorkflowTreeView();
        clkOnWorkflowType(workflowTypeName);
        performAction.doubleClick(getWorkflow(workflowName)).perform();
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//span[@class='ToolBarCaption'][contains"
                                + "(text(),'" + workflowName + "')]")));
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        csPopupDivInstance.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csGuiToolbarHorizontalInstance.getDrpDwnCSGuiToolbarWorkflow());
        csPopupDivInstance.selectPopupDivSubMenu(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarDelete(),
                browserDriver);
        alertText = "Do you really want to delete the item indicated in the "
                + "following: " + workflowName + "?";
        performAlertBoxOperation(isPressOkay, alertText, "mid pane");
        verifyDeletedWorkflowFromTreeView(workflowName, workflowTypeName,
                isPressOkay);
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
     * This method deletes the given workflow from workflow tree view.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains name of workflow type.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow or not.
     * 
     */
    private void deleteWorkflowFromTreeView(String workflowName,
            String workflowTypeName, Boolean isPressOkay) {
        traverseToWorkflowTreeView();
        CSUtility.rightClickTreeNode(waitForReload, getWorkflow(workflowName),
                browserDriver);
        workflowsPopup.selectPopupDivMenu(waitForReload,
                workflowsPopup.getCsPopupMenuDeleteWorkflow(), browserDriver);
        String alertText = "Do you really want to delete this item?";
        performAlertBoxOperation(isPressOkay, alertText, "tree view");
        verifyDeletedWorkflowFromTreeView(workflowName, workflowTypeName,
                isPressOkay);
    }

    /**
     * This method deletes the given workflow from list view.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains name of workflow type.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow or not.
     * 
     */
    private void deleteWorkflowFromListView(String workflowName,
            String workflowTypeName, Boolean isPressOkay) {
        traverseToWorkflowTreeView();
        clkOnWorkflowType(workflowTypeName);
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//td[contains(text(),'" + workflowName + "')]")));
        WebElement workflow = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + workflowName + "')]"));
        workflow.click();
        CSLogger.info("Clicked on worklow : " + workflowName);
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, iframeLocatorsInstance);
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        csPopupDivInstance.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csGuiToolbarHorizontalInstance.getDrpDwnCSGuiToolbarWorkflow());
        csPopupDivInstance.selectPopupDivSubMenu(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarDelete(),
                browserDriver);
        alertText = "Do you really want to delete this item?";
        performAlertBoxOperation(isPressOkay, alertText, "list view");
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        verifyDeletedWorkflowFromListView(workflowName, isPressOkay);
    }

    /**
     * This method deletes the given workflow by performing mass editing
     * operation on workflow.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains name of workflow type.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete workflow or not.
     */
    private void deleteWorkflowByMassEditingOption(String workflowName,
            String workflowTypeName, Boolean isPressOkay) {
        traverseToWorkflowTreeView();
        WebElement workflowType = browserDriver.findElement(By
                .xpath("//span[contains(text(),'" + workflowTypeName + "')]"));
        performAction.doubleClick(workflowType).build().perform();
        CSLogger.info("double clicked on PIM Studio Products workflow type");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//td[contains(text(),'" + workflowName + "')]")));
        WebElement workflowCheckBox = browserDriver
                .findElement(By.xpath("//td[contains(text(),'" + workflowName
                        + "')]/preceding-sibling::td/input"));
        workflowCheckBox.click();
        CSLogger.info("Clicked on checkbox of " + workflowName);
        CSGuiListFooterInstance.clkOnDrpDwnMassUpdateSelector(waitForReload);
        CSGuiListFooterInstance.selectOptionFromDrpDwnMassUpdateSelector(
                waitForReload, "Delete");
        alertText = "Delete: Do you really want to execute "
                + "this action recursively on 1 elements and ALL subelements?";
        performAlertBoxOperation(isPressOkay, alertText, "mass editing option");
        verifyDeletedWorkflowByMassEditOption(workflowName, isPressOkay);
    }

    /**
     * This method verifies whether the workflow is deleted from list view after
     * mass editing operation is performed.
     * 
     * @param workflowName String object contains name of workflow.
     * @param isDeleted Boolean parameter contains true or false values.
     */
    private void verifyDeletedWorkflowByMassEditOption(String workflowName,
            Boolean isDeleted) {
        List<WebElement> workflowExists = browserDriver.findElements(
                By.xpath("//td/a[contains(text(),'" + workflowName + "')]"));
        verifyDeletedWorkflow(workflowName, isDeleted, workflowExists);
    }

    /**
     * This method verifies whether the workflow is deleted from list view after
     * delete operation is performed.
     * 
     * @param workflowName String object contains name of workflow.
     * @param isDeleted Boolean parameter contains true or false values.
     */
    private void verifyDeletedWorkflowFromListView(String workflowName,
            Boolean isDeleted) {
        List<WebElement> workflowExists = browserDriver.findElements(
                By.xpath("//td[contains(text(),'" + workflowName + "')]"));
        verifyDeletedWorkflow(workflowName, isDeleted, workflowExists);
    }

    /**
     * Verifies whether the given workflow is deleted.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains name of workflow type.
     * @param isDeleted Boolean parameter contains true or false values i.e
     *            whether to delete workflow or not.
     */
    private void verifyDeletedWorkflowFromTreeView(String workflowName,
            String workflowTypeName, Boolean isDeleted) {
        traverseToWorkflowTreeView();
        CSUtility.rightClickTreeNode(waitForReload,
                getWorkflowType(workflowTypeName), browserDriver);
        workflowsPopup.selectPopupDivMenu(waitForReload,
                workflowsPopup.getCsPopupRefresh(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        traverseToWorkflowTreeView();
        List<WebElement> workflowExists = browserDriver
                .findElements(By.linkText(workflowName));
        verifyDeletedWorkflow(workflowName, isDeleted, workflowExists);
    }

    /**
     * This method whether workflow is deleted.
     * 
     * @param workflowName String object contains name of workflow to be
     *            deleted.
     * @param isDeleted Boolean parameter contains true or false value i.e
     *            whether to delete workflow or not.
     * @param workflowExists contains list of WebElement.
     */
    private void verifyDeletedWorkflow(String workflowName, Boolean isDeleted,
            List<WebElement> workflowExists) {
        if (isDeleted) {
            if (workflowExists.isEmpty()) {
                CSLogger.info(
                        "When clicked on ok of alert workflow : " + workflowName
                                + " deleted successfully : teststep passed");
            } else {
                CSLogger.error(
                        "When clicked on ok of alert workflow " + workflowName
                                + "didn't delete " + ": " + "teststep failed");
                softAssertion.fail(
                        "When clicked on ok of alert workflow " + workflowName
                                + "didn't delete " + ": " + "teststep failed");
            }
        } else {
            if (workflowExists.isEmpty()) {
                CSLogger.error("When clicked cancel of alert workflow "
                        + workflowName + "deleted : " + "teststep failed");
                softAssertion.fail("When clicked cancel of alert workflow "
                        + workflowName + "deleted : " + "teststep failed");
            } else {
                CSLogger.info("When clicked on cancel of alert workflow "
                        + workflowName + "didn't "
                        + "delete : teststep passed");
            }
        }
    }

    /**
     * This is a data provider method that contains data to delete workflow.
     * 
     * @return Array
     */
    @DataProvider(name = "deleteWorkflowData")
    public Object[][] getDeleteWorkflowData() {
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
        workflowsPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        CSGuiListFooterInstance = new CSGuiListFooter(browserDriver);
        performAction = new Actions(browserDriver);
        csPopupDivInstance = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }
}
