/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.ColorManagementPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create and verify of the colors
 * 
 * @author CSAutomation Team
 */
public class CreateColorManagementTest extends AbstractTest {

    private ColorManagementPage    colorNodeInstance;
    private CSPortalHeader         csPortalHeader;
    private FrameLocators          locator;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    Alert                          alertBox             = null;
    WebDriverWait                  waitForReload;
    String                         validationMessage    = "Label: An entry "
            + "is required";
    String                         chkboxOffClassName   = "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox";
    String                         createColorSheetName = "Create Color";
    private CSPortalWidget         portalWidget;

    /*
     * This method written for validation of the Color field. It will traverse
     * till the Color tree node and click on ADD option and click on save button
     * it gives popup message for validation
     */
    @Test(priority = 1)
    public void createColorRequiredFieldDialog() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            clickOnColorAndCreateNew();
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                    browserDriver, locator);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            CSLogger.info(
                    "Traverse in the Right pane and click on the save button");
            alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
            Assert.assertEquals(alertBox.getText(), validationMessage);
            CSLogger.info("Valuelist validation done for Designation");
            alertBox.accept();
        } catch (Exception e) {
            CSLogger.debug("Automation Error : ", e);
            Assert.fail("Valuelist Validation Failed");
        }
    }

    /*
     * This method written for creation of the Color field. It will traverse
     * till the Color tree node and click on ADD option and selected colors and
     * click on save button
     * 
     * @param colorName String contains the name of the color
     * 
     * @param color String contains the hex code of the color
     * 
     * @param textColor contains the hex code of the text color
     */
    @Test(priority = 2, dataProvider = "CreateColorTestData")
    public void editColor(String colorName, String color, String textColor) {
        try {
            clickOnColorAndCreateNew();
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                    browserDriver, locator);
            CSLogger.info(
                    "Traverse in the Right pane and click on the save button");
            addTextBoxValue(colorNodeInstance.getColorLabel(), colorName);
            CSLogger.info("Added color label");
            selectColorValue(color, colorNodeInstance.getColorPicker());
            CSLogger.info("Selected color");
            traverseToEditFrame();
            selectColorValue(textColor, colorNodeInstance.getColorTextPicker());
            CSLogger.info("Selected Text color");
            traverseToEditFrame();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    colorNodeInstance.getActivebutton());
            if (colorNodeInstance.getActivebutton().getAttribute("class")
                    .equals(chkboxOffClassName)) {
                colorNodeInstance.getActivebutton().click();
                CSLogger.info(
                        "Checked that language dependance is active or not");
            }
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            verificationOfCreatedColor(colorName);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : ", e);
            Assert.fail("color creation Failed");
        }
    }

    /*
     * This method is used to traverse in the right pane till the Edit frame for
     * creation of new color field
     */
    public void traverseToEditFrame() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmEditForActiveScript()));
    }

    /*
     * This method is used to select the color from the color Popup. It clicks
     * on the color fields and opens the color window and gives the color
     * selection as per the colors hex code it will select color and clicks on
     * OK button
     * 
     * @param colorCode string contains the hex color code
     * 
     * @param element WebElement of the color field
     */
    public void selectColorValue(String colorCode, WebElement element) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        WebElement getColorValue = browserDriver.findElement(By.cssSelector(
                "img[style='background-color: " + colorCode + ";']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, getColorValue);
        getColorValue.click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                colorNodeInstance.getColorPickerPopUpOKButton());
        colorNodeInstance.getColorPickerPopUpOKButton().click();
    }

    /*
     * This method clicks on the Color Node of the settings and in middle pane
     * header menu it will clicks on create new and opens the create new window
     * in right pane
     */
    public void clickOnColorAndCreateNew() {
        clickOnColorsTree();
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew());
        csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew().click();
        CSLogger.info("clicked on create new button");
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /*
     * This method is will traverse till the settings ->system preferences->
     * colors and click on the color node . It will opens the middle pane of the
     * list view
     */
    public void clickOnColorsTree() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                colorNodeInstance.getColorTreeNode());
        colorNodeInstance.getColorTreeNode().click();
        CSLogger.info("clicked on color node is settings tree");
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /*
     * This method is used to enter the values into the text box
     * 
     * @param WebElement element of the text box
     * 
     * @param String sTextValue value which needs to add
     */
    public void addTextBoxValue(WebElement element, String sTextValue) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.sendKeys(sTextValue);
    }

    /*
     * This method is used to verify the created Color From the list view
     * 
     * @param String colorName contains the label of the color
     */
    public void verificationOfCreatedColor(String colorName) {
        clickOnColorsTree();
        Integer totalRowsForNoProductsAssignedToClass = getListviewTableTotalRows()
                - 1;
        List<String> listValuelistName = new ArrayList<>();
        for (int iRowCount = 3; iRowCount <= totalRowsForNoProductsAssignedToClass; iRowCount++) {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[3]")));
            String sCellValue = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[3]"))
                    .getText();
            listValuelistName.add(sCellValue);
        }
        boolean retval = listValuelistName.contains(colorName);
        if (retval) {
            CSLogger.info("Color Created successfully : " + colorName);
        } else {
            Assert.fail("color not created under the Colors");
        }
    }

    /*
     * This method will switch control to the Middle pane of the list view and
     * traverse till the Listview table and get the total number of rows
     * 
     * @return Integer Total no of rows of list view.
     */
    public Integer getListviewTableTotalRows() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='hidewrap CSAdminList']")));
        WebElement productInfoTable = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) productInfoTable
                .findElements(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody[1]/tr"));
        Integer totalRowsForNoProductsAssignedToClass = new Integer(
                rows.size());
        return totalRowsForNoProductsAssignedToClass;
    }

    /*
     * It returns the array containing data for creation of color sheet contains
     * the String fields of Designation, color code, text color code
     * 
     * @return Array of the color required fields
     */
    @DataProvider(name = "CreateColorTestData")
    public Object[][] getValuelistListViewTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                createColorSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResources() {
        colorNodeInstance = SettingsLeftSectionMenubar
                .getColorsSettingNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
    }

}
