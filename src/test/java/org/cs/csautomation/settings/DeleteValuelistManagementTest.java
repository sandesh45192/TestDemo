/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.ValuelistManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DeleteValuelistManagementTest extends AbstractTest {

    private ValuelistManagementPage valueListNodeInstance;
    private CSPortalHeader          csPortalHeader;
    private FrameLocators           iframeLocatorsInstance;
    private WebDriverWait           waitForReload;
    public String                   deleteValuelistMassEditSheetName  = "DeleteValuelistMassEdit";
    public String                   deleteValuelistListViewSheetName  = "DeleteValuelistListView";
    private CSPortalWidget          portalWidget;
    private WebElement              label                             = null;
    private boolean                 status                            = false;
    private CSGuiToolbarHorizontal  guiToolbarHorizontal;
    private CSPopupDivSettings      popupDivSettings;
    private SoftAssert              softAssertion;
    public String                   validationMessageDeleteInListView = "Do you "
            + "really want to delete this item?";
    public String                   validationMessageDeleteInMassView = "Delete: "
            + "Do you really want to execute this action "
            + "recursively on 1 elements and ALL subelements?";
    Alert                           alertBox                          = null;

    /**
     * This method is used to perform the Delete valuelist from the List view on
     * Mass edit option.
     * 
     * @Param designation String contains the valuelist name
     * 
     * @Param category String contains category under which valuelist present
     */
    @Test(priority = 1, dataProvider = "DeleteValuelistMassEditTestData")
    public void deleteValuelistFromMassEditing(String designation,
            String category) throws Exception {
        waitForReload = new WebDriverWait(browserDriver, 30);
        try {
            accessValueList();
            deleteValueList(designation, "mass delete");
            verifyDeletedValuelist(designation);
            softAssertion.assertAll();
            browserDriver.navigate().refresh();
            browserDriver.manage().timeouts().pageLoadTimeout(10,
                    TimeUnit.SECONDS);
            CSLogger.info("page refresh");
            CSLogger.info(
                    "Delete value list from mass editing test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error deleteValuelistFromMassEditing: ",
                    e);
            Assert.fail("Automation error : deleteValuelistFromMassEditing", e);
        }
    }

    /**
     * This method is used to perform the Delete valuelist from the List view on
     * right click option.
     * 
     * @Param designation String contains the valuelist name
     * 
     * @Param category String contains category under which valuelist present
     */
    @Test(priority = 2, dataProvider = "DeleteValuelistListViewTestData")
    public void deleteValuelistFromListView(String designation, String category)
            throws Exception {
        waitForReload = new WebDriverWait(browserDriver, 30);
        try {
            accessValueList();
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, iframeLocatorsInstance);
            enterDataInFilterBar(designation);
            deleteValueList(designation, "list delete");
            verifyDeletedValuelist(designation);
            softAssertion.assertAll();
            browserDriver.navigate().refresh();
            browserDriver.manage().timeouts().pageLoadTimeout(10,
                    TimeUnit.SECONDS);
            CSLogger.info("page refresh");
            CSLogger.info("Delete value list from list view test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error deleteValuelistFromListView: ", e);
            Assert.fail("Automation error : deleteValuelistFromListView", e);
        }
    }

    /**
     * This method will delete value list
     * 
     * @param valuelistName - String instance - Label of value list to be
     *            deleted
     * @param operation - String instance - type of delete to be executed i.e.
     *            either mass delete or list delete
     */
    private void deleteValueList(String valuelistName, String operation) {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        if (operation.equals("mass delete")) {
            enterDataInFilterBar(valuelistName);
            valueListNodeInstance.clkWebElement(waitForReload,
                    valueListNodeInstance.getChkboxToSelectValueList());
            Select selectByValue = new Select(browserDriver.findElement(
                    By.xpath("//select[@id='massUpdateSelector']")));
            selectByValue.selectByValue("deletemarked");
            CSUtility.tempMethodForThreadSleep(2000);
            validateAlertBox(false, operation);
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, iframeLocatorsInstance);
            selectByValue.selectByValue("deletemarked");
            validateAlertBox(true, operation);
            CSUtility.tempMethodForThreadSleep(2000);
            disableFilterBar();

            CSUtility.tempMethodForThreadSleep(2000);
        } else if (operation.equals("list delete")) {
            CSUtility.tempMethodForThreadSleep(2000);
            WebElement valueList = browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']/tbody"
                            + "//tr[contains(@onclick,'return cs_edit')]/td[3]"));
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.waitForVisibilityOfElement(waitForReload, valueList);
            clkContextDeleteInListView(valueList);
            validateAlertBox(false, operation);
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, iframeLocatorsInstance);
            clkContextDeleteInListView(valueList);
            validateAlertBox(true, operation);
            CSUtility.tempMethodForThreadSleep(2000);
            disableFilterBar();
            CSUtility.tempMethodForThreadSleep(2000);
        } else {
            CSLogger.error("Error. Value list has not been created. "
                    + "Create a value list before deleting it.");
        }

    }

    /**
     * This method will click on context delete in list view for value list
     */
    private void clkContextDeleteInListView(WebElement label) {
        CSUtility.rightClickTreeNode(waitForReload, label, browserDriver);
        popupDivSettings.selectPopupDivMenu(waitForReload,
                popupDivSettings.getCtxObject(), browserDriver);
        CSLogger.info("Clicked on context Object from context menu of share");
        popupDivSettings.selectPopupDivSubMenu(waitForReload,
                popupDivSettings.getCtxDelete(), browserDriver);
    }

    /**
     * Enters the data in filter bar.
     *
     * @param waitForReload WebDriverWait object.
     * @param valuelistName String object contains data to be entered in filter
     *            bar.
     */
    public void enterDataInFilterBar(String valuelistName) {
        CSUtility.tempMethodForThreadSleep(1000);
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//img[contains(@src,'nofilter.gif')]/.."))
                .size();
        if (chkPresenceOfNoFilter == 0) {
            int chkPresenceOfTextbox = browserDriver
                    .findElements(By.xpath("//input[@id='Filter_Label']"))
                    .size();
            if (chkPresenceOfTextbox >= 0) {
                WebElement btnFilter = browserDriver.findElement(By
                        .xpath("//img[contains(@src,'filter.gif')]/parent::a"));
                CSUtility.waitForVisibilityOfElement(waitForReload, btnFilter);
                btnFilter.click();
                CSUtility.tempMethodForThreadSleep(1000);
                guiToolbarHorizontal.getTxtFilterBar().clear();
                guiToolbarHorizontal.getTxtFilterBar().sendKeys(valuelistName);
                CSLogger.info("Entered value list label " + valuelistName
                        + " in filter bar");
                guiToolbarHorizontal.getTxtFilterBar().sendKeys(Keys.ENTER);
            }
        }
    }

    /**
     * This method will disable the filter bar after performing the use case
     */
    private void disableFilterBar() {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//a/img[contains(@src,'nofilter.gif')]"))
                .size();
        if (chkPresenceOfNoFilter != 0) {
            valueListNodeInstance.clkWebElement(waitForReload,
                    guiToolbarHorizontal.getbtnNoFilter());
            valueListNodeInstance.clkWebElement(waitForReload,
                    guiToolbarHorizontal.getBtnFilter());
            CSUtility.tempMethodForThreadSleep(2000);
        }

    }

    /**
     * This method will return all the value lists from list view
     * 
     * @return list - containing all the value lists
     */
    private List<WebElement> getList() {
        List<WebElement> list = browserDriver.findElements(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody"
                        + "//tr[contains(@onclick,'return cs_edit')]/td[3]"));
        return list;
    }

    /**
     * This method is used to handle the Ok and Cancel alert box deletion of
     * valuelist values from both list and mass operation
     * 
     * @Param isOkey Boolean True and false for OK and Cancel button
     * @param operation - String instance - type of delete to be executed i.e.
     *            either mass delete or list delete
     */
    public void validateAlertBox(Boolean isOkay, String operation) {
        alertBox = getAlertBox();
        if (operation.equals("mass delete")) {
            softAssertion.assertEquals(alertBox.getText(),
                    validationMessageDeleteInMassView,
                    "Error! Validation of alert box message failed");
        }
        if (operation.equals("list delete")) {
            softAssertion.assertEquals(alertBox.getText(),
                    validationMessageDeleteInListView,
                    "Error! Validation of alert box message failed");
        }
        if (isOkay) {
            alertBox.accept();
            CSUtility.tempMethodForThreadSleep(2000);
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, iframeLocatorsInstance);
            valueListNodeInstance.clkWebElement(waitForReload,
                    guiToolbarHorizontal.getBtnCSGuiToolbarRefresh());
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("Clicked on OK of alert box");
        } else {
            alertBox.dismiss();
            CSLogger.info("Clicked on CANCEL of alert box");
        }
    }

    /**
     * This method traverse till the Valuelist from the settings of system
     * preferences and double clicks on valuelist to open the list view
     */
    public void accessValueList() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        CSLogger.info("Clicked on the setting tab and traversed till"
                + " valuelist in tree");
        Actions action = new Actions(browserDriver);
        action.doubleClick(valueListNodeInstance.getBtnValueListTreeNode())
                .build().perform();
        CSLogger.info("Clicked on value list node");
    }

    /**
     * This method returns the alert box.
     * 
     * @return Alert Instance of Alert class representing the DOM alert box.
     */
    private Alert getAlertBox() {
        Alert alertBox;
        CSUtility.tempMethodForThreadSleep(2000);
        alertBox = browserDriver.switchTo().alert();
        return alertBox;
    }

    /**
     * This method will verify whether value list is successfully deleted or not
     * 
     * @param valuelistName - String instance - Label of value list to be
     *            verified for deletion
     */
    private void verifyDeletedValuelist(String valuelistName) {
        accessValueList();
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        List<WebElement> list = getList();
        for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            label = list.get(listIndex);
            if (label.getText().equals(valuelistName)) {
                softAssertion.assertFalse(label.getText().equals(valuelistName),
                        "Error. Valuelist is not deleted from list.");
                CSLogger.error("Error. Valuelist is not deleted from list.");
                status = false;
                browserDriver.navigate().refresh();
                browserDriver.manage().timeouts().pageLoadTimeout(10,
                        TimeUnit.SECONDS);
                CSLogger.info("page refresh");
                break;
            }
        }
        if (status == true) {
            CSLogger.info("Valuelist is successfully deleted.");
            browserDriver.navigate().refresh();
            browserDriver.manage().timeouts().pageLoadTimeout(10,
                    TimeUnit.SECONDS);
            CSLogger.info("page refresh");
        }
    }

    /**
     * It returns the array containing data for creation of Valuelist sheet
     * contains the String fields of Designation, Category, Description
     * 
     * @return Array of the valuelist required fields
     */
    @DataProvider(name = "DeleteValuelistMassEditTestData")
    public Object[][] getValuelistMassEditTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteValuelistMassEditSheetName);
    }

    /**
     * It returns the array containing data for creation of Valuelist sheet
     * contains the String fields of Designation, Category
     * 
     * @return Array of the value list required fields
     */
    @DataProvider(name = "DeleteValuelistListViewTestData")
    public Object[][] getValuelistListViewTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteValuelistListViewSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResources() {
        valueListNodeInstance = SettingsLeftSectionMenubar
                .getValuelistSettingNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
        popupDivSettings = new CSPopupDivSettings(browserDriver);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        softAssertion = new SoftAssert();
    }

}
