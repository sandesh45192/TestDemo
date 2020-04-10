/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import java.util.List;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains test method to deletes an automation.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteAutomationTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private AutomationsPage        automationsPageInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private FrameLocators          iframeLocatorsInstance;
    private IAutomationNodePopup   automationNodePopup;
    private IAutomationPopup       automationPopup;
    private String                 alertText;
    private SoftAssert             softAssertion;
    private String                 dataSheetName = "DeleteAutomation";

    /**
     * This test method deletes the given automation.
     * 
     * @param firstAutomationName String object contains name of automation to
     *            be deleted from mid pane.
     * @param deleteFromListViewAutomationName String object contains name of
     *            automation to be deleted from list view.
     * @param deleteFromTreeViewAutomationName String object contains name of
     *            automation to be deleted from tree view.
     */
    @Test(dataProvider = "deleteAutomationData")
    public void testDeleteAutomation(String firstAutomationName,
            String deleteFromListViewAutomationName,
            String deleteFromTreeViewAutomationName) {
        waitForReload = new WebDriverWait(browserDriver, 60);
        goToAutomationsNode();
        checkAutomationExists(firstAutomationName);
        deleteAutomationFromMidPane(firstAutomationName, false);
        deleteAutomationFromMidPane(firstAutomationName, true);
        checkAutomationExists(deleteFromListViewAutomationName);
        deleteautomationFromListView(deleteFromListViewAutomationName, false);
        deleteautomationFromListView(deleteFromListViewAutomationName, true);
        traverseToAutomationTreeView();
        checkAutomationExists(deleteFromTreeViewAutomationName);
        deleteAutomationFromTreeView(deleteFromTreeViewAutomationName, false);
        deleteAutomationFromTreeView(deleteFromTreeViewAutomationName, true);
        softAssertion.assertAll();
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
     * Checks whether the given automation exists.
     * 
     * @param automationName String object contains name of automation.
     */
    private void checkAutomationExists(String automationName) {
        try {
            clkOnAutomation(automationName);
            CSUtility.tempMethodForThreadSleep(1000);
            CSLogger.info("Automation " + automationName + " exists");
        } catch (Exception e) {
            CSLogger.error("Automation does not exists", e);
            Assert.fail("Automation does not exists");
        }
    }

    /**
     * This method clicks on given automation.
     * 
     * @param automationName String object contains name of automation.
     */
    private void clkOnAutomation(String automationName) {
        getAutomation(automationName).click();
        CSLogger.info("Clicked on automation : " + automationName);
    }

    /**
     * Returns the WebElement of given automation.
     * 
     * @param automationName String object contains name of automation.
     * @return WebElement of automation.
     */
    private WebElement getAutomation(String automationName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(automationName)));
        WebElement automation = browserDriver
                .findElement(By.linkText(automationName));
        return automation;
    }

    /**
     * This method deletes the given automation from mid pane.
     * 
     * @param automationName String object contains name of automation.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete automation or not.
     */
    private void deleteAutomationFromMidPane(String automationName,
            Boolean isPressOkay) {
        traverseToAutomationTreeView();
        String automationId = getAutomationId(automationName);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//span[@class='ToolBarCaption'][contains(text(),"
                                + "'Edit " + "Automation')]")));
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        automationPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csGuiToolbarHorizontalInstance
                        .getDrpDwnCSGuiToolbarAutomation());
        automationPopup.selectPopupDivSubMenu(waitForReload,
                automationPopup.getCsPopupDelete(), browserDriver);
        alertText = "Do you really want to delete the item indicated in the "
                + "following: ID " + automationId + "?";
        performAlertBoxOperation(isPressOkay, alertText, "mid pane");
        verifyDeletedAutomationFromTreeView(automationName, isPressOkay);
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
     * Verifies whether the given automation is deleted from list view.
     * 
     * @param automationName String object contains name of automation.
     * @param isDeleted Boolean parameter contains true or false value i.e
     *            whether the automation is deleted or not.
     */
    private void verifyDeletedAutomationFromListView(String automationName,
            Boolean isDeleted) {
        CSUtility.tempMethodForThreadSleep(1000);
        List<WebElement> automationExists = browserDriver.findElements(
                By.xpath("//td[contains(text(),'" + automationName + "')]"));
        verifyDeletedAutomation(automationName, isDeleted, automationExists);
    }

    /**
     * This method returns the ID of the automation passed as argument
     * 
     * @param automationName String containing name of automation.
     * @return automation Id String object contains automation ID.
     */
    private String getAutomationId(String automationName) {
        traverseToAutomationTreeView();
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(
                browserDriver.findElement(By.linkText(automationName)))
                .perform();
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By
                        .xpath("//span[@class='ToolBarCaption'][contains(text(),'"
                                + "Edit Automation')]")));
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        String automationId = browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
        return automationId;
    }

    /**
     * This method deletes the given automation from list view.
     * 
     * @param automationName String object contains name of automation.
     * @param automationTypeName String object contains name of automation type.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete automation or not.
     * 
     */
    private void deleteautomationFromListView(String automationName,
            Boolean isPressOkay) {
        traverseToAutomationTreeView();
        automationsPageInstance.clkOnAutomationsNode(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        traverseToAutomationCenterPane();
        WebElement automation = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + automationName + "')]"));
        CSUtility.waitForElementToBeClickable(waitForReload, automation);
        CSUtility.rightClickTreeNode(waitForReload, automation, browserDriver);
        automationPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                automationPopup.getCsPopupObject());
        automationPopup.selectPopupDivSubMenu(waitForReload,
                automationPopup.getCsPopupDelete(), browserDriver);
        alertText = "Do you really want to delete this item?";
        performAlertBoxOperation(isPressOkay, alertText, "list view");
        traverseToAutomationCenterPane();
        verifyDeletedAutomationFromListView(automationName, isPressOkay);
    }

    /**
     * Switches frames of system preferences center pane located under setting's
     * tab.
     */
    private void traverseToAutomationCenterPane() {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * This method deletes the given automation from tree view.
     * 
     * @param automationName String object contains name of automation.
     * @param isPressOkay Boolean parameter contains true or false value i.e
     *            whether to delete automation or not.
     * 
     */
    private void deleteAutomationFromTreeView(String automationName,
            Boolean isPressOkay) {
        traverseToAutomationTreeView();
        CSUtility.rightClickTreeNode(waitForReload,
                getAutomation(automationName), browserDriver);
        automationPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                automationPopup.getCsPopupObject());
        automationPopup.selectPopupDivSubMenu(waitForReload,
                automationPopup.getCsPopupDelete(), browserDriver);
        String alertText = "Do you really want to delete this item?";
        performAlertBoxOperation(isPressOkay, alertText, "tree view");
        verifyDeletedAutomationFromTreeView(automationName, isPressOkay);
    }

    /**
     * Verifies whether the given automation is deleted.
     * 
     * @param automationName String object contains name of automation.
     * @param isDeleted Boolean parameter contains true or false values i.e
     *            whether to delete automation or not.
     */
    private void verifyDeletedAutomationFromTreeView(String automationName,
            Boolean isDeleted) {
        traverseToAutomationTreeView();
        CSUtility.rightClickTreeNode(waitForReload,
                automationsPageInstance.getBtnAutomationsNode(), browserDriver);
        automationNodePopup.selectPopupDivMenu(waitForReload,
                automationNodePopup.getCsPopupRefresh(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        traverseToAutomationTreeView();
        List<WebElement> automationExists = browserDriver
                .findElements(By.linkText(automationName));
        verifyDeletedAutomation(automationName, isDeleted, automationExists);
    }

    /**
     * This method verifies whether automation is deleted.
     * 
     * @param automationName String object contains name of automation to be
     *            deleted.
     * @param isDeleted Boolean parameter contains true or false value i.e
     *            whether to delete automation or not.
     * @param automationExists Contains list of WebElement.
     */
    private void verifyDeletedAutomation(String automationName,
            Boolean isDeleted, List<WebElement> automationExists) {
        if (isDeleted) {
            if (automationExists.isEmpty()) {
                CSLogger.info("When clicked on ok of alert automation : "
                        + automationName
                        + " deleted successfully : test step passed");
            } else {
                CSLogger.error("When clicked on ok of alert automation "
                        + automationName + " didn't delete " + ": "
                        + "teststep failed");
                softAssertion.fail(
                        "When clicked ok of alert automation " + automationName
                                + " didn't " + "delete : test step failed");
            }
        } else {
            if (automationExists.isEmpty()) {
                CSLogger.error("When clicked cancel of alert automation "
                        + automationName + " deleted : " + "teststep failed");
                softAssertion.fail("When clicked cancel of alert automation "
                        + automationName + " deleted : " + "teststep failed");
            } else {
                CSLogger.info("When clicked on cancel of alert automation "
                        + automationName + " didn't "
                        + " delete : test step passed");
            }
        }
    }

    /**
     * This is a data provider method that contains data to delete automation.
     * 
     * @return Array
     */
    @DataProvider(name = "deleteAutomationData")
    public Object[][] getDeleteAutomationData() {
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
        automationPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        automationsPageInstance = SettingsLeftSectionMenubar
                .getAutomationsNode(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        automationNodePopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
        CSPopupDivSettings.getCSPopupDivLocators(browserDriver);

    }

}
