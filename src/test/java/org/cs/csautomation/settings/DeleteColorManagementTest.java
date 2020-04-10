/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.ColorManagementPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to delete and verify of the colors
 * 
 * @author CSAutomation Team
 */

public class DeleteColorManagementTest extends AbstractTest {

    private ColorManagementPage    colorNodeInstance;
    private CSPortalHeader         csPortalHeader;
    private FrameLocators          iframeLocatorsInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSPortalWidget         portalWidget;
    private CSPopupDivSettings     popupDivSettings;
    private SoftAssert             softAssert;
    Alert                          alertBox                      = null;
    WebDriverWait                  waitForReload;
    String                         validationMessage             = "Delete: Do "
            + "you really want to execute this action recursively on 1 elements"
            + " and ALL subelements?";
    String                         chkboxOffClassName            = "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox";
    String                         deleteColorMassEditTestData   = "DeleteColorMassEdit";
    String                         deleteColorListViewTestData   = "DeleteColorListView";
    String                         deleteColorEditorViewTestData = "DeleteColorEditorView";
    String                         editorviewMessage             = "Do you"
            + " really want to delete the item indicated in the following: ";

    /**
     * This method will delete the color from the mass edit delete option
     * 
     * @param colorName String contains the name of the color
     */
    @Test(priority = 1, dataProvider = "DeleteColorMassEditTestData")
    public void DeleteColorFromMassEdit(String colorName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            performDeleteUsecase(colorName, false, "Mass");
            performDeleteUsecase(colorName, true, "Mass");
            softAssert.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation Error DeleteColorFromMassEdit: ", e);
            Assert.fail("color creation Failed");
        }
    }

    /**
     * This method will delete the color from the list view right click delete
     * option
     * 
     * @param colorName String contains the name of the color
     */
    @Test(priority = 2, dataProvider = "DeleteColorListViewTestData")
    public void DeleteColorFromListView(String colorName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 30);
            performDeleteUsecase(colorName, false, "List");
            performDeleteUsecase(colorName, true, "List");
        } catch (Exception e) {
            CSLogger.debug("Automation Error DeleteColorListViewTestData: ", e);
            Assert.fail("color creation Failed");
        }
    }

    /**
     * This method will delete the color from the editor view delete option
     * 
     * @param colorName String contains the name of the color
     */
    @Test(priority = 3, dataProvider = "DeleteColorEditorViewTestData")
    public void DeleteColorFromEditorView(String colorName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 30);
            performDeleteUsecase(colorName, false, "Editor");
            performDeleteUsecase(colorName, true, "Editor");

        } catch (Exception e) {
            CSLogger.debug("Automation Error DeleteColorFromEditorView : ", e);
            Assert.fail("color creation Failed");
        }
    }

    /**
     * this method contains the actual method for delete usecase
     * 
     * @param colorName String contains the name of the deleted color
     * 
     * @param isOkey is operation to ok and cancel button
     * 
     * @param operation to delete the color from mass, list or editor view
     */
    public void performDeleteUsecase(String colorName, boolean isOkey,
            String operation) {
        clickOnColorsTree();
        getTableMatchingRow(colorName, operation, isOkey);
        if (isOkey) {
            verifyDeletedColor(colorName);
        }
        CSLogger.info("Color is deleted from the Mass edit");
    }

    /**
     * This method is will traverse till the settings ->system preferences->
     * colors and click on the color node . It will opens the middle pane of the
     * list view
     */
    public void clickOnColorsTree() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                colorNodeInstance.getColorTreeNode());
        colorNodeInstance.getColorTreeNode().click();
        CSLogger.info("clicked on color node is settings tree");
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * Traverse to the middle colors list view and find the matching row and
     * clicked or right clicked and perform the delete options depends upon the
     * operation
     * 
     * @param colorName string contains the colors name that has to deleted
     * 
     * @Param operation string contains value Mass or List operation type
     * 
     * @Param isOkey Boolean value for Ok and cancel button
     */
    public void getTableMatchingRow(String colorName, String operation,
            Boolean isOkey) {
        Integer totalRowsForNoProductsAssignedToClass = getListviewTableTotalRows()
                - 1;
        for (int iRowCount = 3; iRowCount <= totalRowsForNoProductsAssignedToClass; iRowCount++) {
            String colorId = null;
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[3]")));
            String sCellValue = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[3]"))
                    .getText();
            if (sCellValue.equalsIgnoreCase(colorName)) {
                waitForReload.until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath(
                                "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                        + iRowCount + "]/td[1]/input[1]")));
                WebElement colorElement = browserDriver.findElement(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                + iRowCount + "]/td[1]/input[1]"));
                if (operation.equals("Mass")) {
                    colorElement.click();
                    Select selectByValue = new Select(browserDriver.findElement(
                            By.xpath("//select[@id='massUpdateSelector']")));
                    selectByValue.selectByValue("deletemarked");
                    CSLogger.info(
                            "selected the color checkbox and mass delete");
                    getAlertBoxValidation(colorId, isOkey, operation,
                            colorName);
                    break;
                }
                if (operation == "List") {
                    colorElement.click();
                    CSUtility.rightClickTreeNode(waitForReload,
                            browserDriver.findElement(By.xpath(
                                    "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                            + iRowCount + "]/td[1]/input[1]")),
                            browserDriver);
                    IAttributePopup attributePopup = CSPopupDivPim
                            .getCSPopupDivLocators(browserDriver);
                    attributePopup.selectPopupDivMenu(waitForReload,
                            attributePopup.getDeleteMenu(), browserDriver);
                    CSLogger.info("Right click and select delete option");
                    getAlertBoxValidation(colorId, isOkey, operation,
                            colorName);
                    break;
                }
                if (operation == "Editor") {
                    browserDriver.findElement(By.xpath(
                            "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                    + iRowCount + "]/td[3]"))
                            .click();
                    TraversingForSettingsModule
                            .traverseToSystemPrefRightPaneFrames(waitForReload,
                                    browserDriver, iframeLocatorsInstance);
                    waitForReload.until(
                            ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                                    iframeLocatorsInstance
                                            .getFrmEditForActiveScript()));
                    CSLogger.info("clicked on the colorname and switched into "
                            + "editorview");

                    CSUtility.waitForVisibilityOfElement(waitForReload,
                            csGuiToolbarHorizontalInstance.getEditorViewId());

                    colorId = csGuiToolbarHorizontalInstance.getEditorViewId()
                            .getAttribute("value");

                    csGuiToolbarHorizontalInstance
                            .clkBtnShowMoreOption(waitForReload);
                    popupDivSettings.selectPopupDivMenu(waitForReload,
                            csGuiToolbarHorizontalInstance.getBtnColor(),
                            browserDriver);
                    popupDivSettings
                            .selectPopupDivSubMenu(waitForReload,
                                    csGuiToolbarHorizontalInstance
                                            .getBtnCSGuiToolbarDelete(),
                                    browserDriver);
                    getAlertBoxValidation(colorId, isOkey, operation,
                            colorName);
                    break;
                }
            }
        }
    }

    /**
     * This method will switch control to the Middle pane of the list view and
     * traverse till the List view table and get the total number of rows
     */
    public Integer getListviewTableTotalRows() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='hidewrap CSAdminList']")));
        WebElement productInfoTable = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) productInfoTable
                .findElements(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody[1]/tr"));
        Integer totalRowsForNoProductsAssignedToClass = new Integer(
                rows.size());
        CSLogger.info("get the number of rows from the table list :"
                + totalRowsForNoProductsAssignedToClass);
        return totalRowsForNoProductsAssignedToClass;
    }

    /**
     * This method is used to handle the Ok and Cancel alert box deletion of
     * colors values from both list and mass operation
     * 
     * @Param isOkey Boolean True and false for OK and Cancel button
     * 
     * @param operation string contains which operation need to perform for
     *            delete (mass delete, editor delete, list right click delete)
     * 
     * @param colorName string which color needs to delete
     */
    public void getAlertBoxValidation(String colorId, Boolean isOkey,
            String operation, String colorName) {
        alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (operation == "Editor") {
            String deleteMessage = editorviewMessage + colorName + " " + colorId
                    + "?";
            softAssert.assertEquals(alertBox.getText(), deleteMessage);
        } else {
            softAssert.assertEquals(alertBox.getText(), validationMessage);
        }
        if (isOkey) {
            alertBox.accept();
            CSLogger.info("Color delete cancel validation for list view");
        } else {
            alertBox.dismiss();
            CSLogger.info("Color delete OK validation for list view");
        }
    }

    /**
     * This method is used to check the deleted value from the Color list view.
     * It will traverse till the Color tree node clicked on the Color and get
     * text by using the linked test. If it will get the null values then values
     * gets deleted properly
     * 
     * @param colorName String name of the Color
     */
    public void verifyDeletedColor(String colorName) {
        clickOnColorsTree();
        List<String> listcolorName = new ArrayList<String>();
        Integer totalRowsForNoProductsAssignedToClass = getListviewTableTotalRows()
                - 1;
        for (int iRowCount = 3; iRowCount <= totalRowsForNoProductsAssignedToClass; iRowCount++) {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[3]")));
            String sCellValue = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[3]"))
                    .getText();
            listcolorName.add(sCellValue);
        }
        boolean retval = listcolorName.contains(colorName);
        if (!retval) {
            CSLogger.info("Clors values deleted properly : " + colorName);
        } else {
            Assert.fail("Elements not deleted from the color list");
        }
    }

    /**
     * It returns the array containing data for creation of colors sheet
     * contains the String fields of color Name
     * 
     * @return Array of the colors required fields
     */
    @DataProvider(name = "DeleteColorEditorViewTestData")
    public Object[][] getColorsEditorViewTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteColorEditorViewTestData);
    }

    /**
     * It returns the array containing data for creation of colors sheet
     * contains the String fields of Designation, Category
     * 
     * @return Array of the colors required fields
     */
    @DataProvider(name = "DeleteColorMassEditTestData")
    public Object[][] getColorsMassEditViewTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteColorMassEditTestData);
    }

    /**
     * It returns the array containing data for creation of colors sheet
     * contains the String fields of Designation, Category
     * 
     * @return Array of the colors required fields
     */
    @DataProvider(name = "DeleteColorListViewTestData")
    public Object[][] getColorsListViewTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteColorListViewTestData);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResources() {
        colorNodeInstance = SettingsLeftSectionMenubar
                .getColorsSettingNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
        popupDivSettings = new CSPopupDivSettings(browserDriver);
        softAssert = new SoftAssert();
    }
}
