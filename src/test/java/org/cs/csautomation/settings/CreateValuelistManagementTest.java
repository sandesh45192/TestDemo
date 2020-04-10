/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.concurrent.TimeUnit;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.settings.ValuelistManagementPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create and edit the views.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateValuelistManagementTest extends AbstractTest {

    private ValuelistManagementPage valueListNodeInstance;
    private CSPortalHeader          csPortalHeader;
    private FrameLocators           iframeLocatorsInstance;
    WebDriverWait                   waitForReload;
    private CSGuiToolbarHorizontal  csGuiToolbarHorizontalInstance;
    Alert                           alertBox               = null;
    String                          validationMessage      = "Label:"
            + " An entry is required";
    String                          chkboxOffClassName     = "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox";
    public String                   editValuelistSheetName = "EditValuelist";
    private CSPortalWidget          portalWidget;

    /*
     * This method written for validation of the Valuelist field. It will
     * traverse till the Valuelist tree node and right click and click on ADD
     * option and click on save button it gives popup message for validation
     */
    @Test(priority = 1)
    public void createValuelistwithRequiredFieldDialog()
            throws InterruptedException {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, iframeLocatorsInstance);
            portalWidget.clkOnSystemPreferencesIcon(waitForReload);
            TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                    waitForReload, browserDriver, iframeLocatorsInstance);
            CSLogger.info("Clicked on the setting tab and travers till"
                    + " valielist in tree");
            CSUtility.rightClickTreeNode(waitForReload,
                    valueListNodeInstance.getBtnValueListTreeNode(),
                    browserDriver);
            IAttributePopup attributePopup = CSPopupDivPim
                    .getCSPopupDivLocators(browserDriver);
            attributePopup.selectPopupDivMenu(waitForReload,
                    attributePopup.getCsGuiPopupMenuAddValuelist(),
                    browserDriver);
            CSLogger.info("Right clicked on valuelist and clicked on ADD"
                    + " option");
            TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                    waitForReload, browserDriver, iframeLocatorsInstance);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            alertBox = getAlertBox();
            Assert.assertEquals(alertBox.getText(), validationMessage);
            CSLogger.info("Valuelist validation done for Designation");
            alertBox.accept();
            CSLogger.info("clicked on OK of alert box");
            CSLogger.info(
                    "Create value lists with required field dialog test completed!");
            browserDriver.navigate().refresh();
            browserDriver.manage().timeouts().pageLoadTimeout(10,
                    TimeUnit.SECONDS);
            CSLogger.info("page refresh");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : ", e);
            Assert.fail("Valuelist Validation Failed");
        }
    }

    /*
     * This test case is to create the valuelist. It will traverse to the
     * settings valuelist right click on the valuelist node and click ADD option
     * it will open the editor view in right pane and add values in the
     * Designation, Category and Description. Clicks on save button.
     * 
     * @param String designation contains the label of the valuelist
     * 
     * @param String category contains the parent of the valuelist
     * 
     * @param String description contains the description value
     */
    @Test(priority = 2, dataProvider = "editValuelistValuesTestData")
    public void editValuelistValues(String designation, String category,
            String description) throws InterruptedException {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, iframeLocatorsInstance);
            portalWidget.clkOnSystemPreferencesIcon(waitForReload);
            TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                    waitForReload, browserDriver, iframeLocatorsInstance);
            CSLogger.info("Clicked on the setting tab and travers till"
                    + " valielist in tree");
            CSUtility.rightClickTreeNode(waitForReload,
                    valueListNodeInstance.getBtnValueListTreeNode(),
                    browserDriver);
            IAttributePopup attributePopup = CSPopupDivPim
                    .getCSPopupDivLocators(browserDriver);
            attributePopup.selectPopupDivMenu(waitForReload,
                    attributePopup.getCsGuiPopupMenuAddValuelist(),
                    browserDriver);
            CSLogger.info("Right clicked on valuelist and clicked on ADD"
                    + " option for edit valuelist");
            TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                    waitForReload, browserDriver, iframeLocatorsInstance);
            addTextBoxValue(valueListNodeInstance.getValueRangeDesignation(),
                    designation);
            addTextBoxValue(valueListNodeInstance.getValueRangeCategory(),
                    category);
            addTextBoxValue(valueListNodeInstance.getValueRangeDescription(),
                    description);
            CSLogger.info("Added values for category, label, description");
            if (valueListNodeInstance.getLanguageDependentcheckbox()
                    .getAttribute("class") == chkboxOffClassName) {
                valueListNodeInstance.getLanguageDependentcheckbox().click();
                CSLogger.info(
                        "Checked that language dependace is active or not");
            }
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            verifyValuelistValues(waitForReload, designation, category);
            CSLogger.info("Edit value list values test completed!");
            browserDriver.navigate().refresh();
            browserDriver.manage().timeouts().pageLoadTimeout(10,
                    TimeUnit.SECONDS);
            CSLogger.info("page refresh");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : ", e);
        }
    }

    /*
     * Verify the category is created under the valuelist and the designation
     * valuelist is created under the mentioned category or not.
     * 
     * @param waitForReload object to wait for reload
     * 
     * @param designation String contains the Label of the valuelist
     * 
     * @param category String contains value under which the valuelist should
     * gets created
     */
    private void verifyValuelistValues(WebDriverWait waitForReload,
            String designation, String category) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                valueListNodeInstance.getBtnValueListTreeNode());
        valueListNodeInstance.getBtnValueListTreeNode().click();
        WebElement valuelistCategory = browserDriver
                .findElement(By.linkText(category));
        Assert.assertEquals(valuelistCategory.getText(), category);
        CSLogger.info("Verified the valuelist Category node.");
        browserDriver.findElement(By.linkText(category)).click();
        WebElement valuelistChild = browserDriver
                .findElement(By.linkText(designation));
        Assert.assertEquals(valuelistChild.getText(), designation);
        CSLogger.info("verified the Valuelist value");
    }

    /*
     * Method which waits for the visibility of the element and enters values in
     * the text box
     */
    private void addTextBoxValue(WebElement element, String sTextValue)
            throws InterruptedException {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.sendKeys(sTextValue);
    }

    /**
     * This method returns the alert box.
     * 
     * @return Alert Instance of Alert class representing the DOM alert box.
     */
    private Alert getAlertBox() {
        Alert alertBox;
        CSUtility.tempMethodForThreadSleep(1000);
        alertBox = browserDriver.switchTo().alert();
        return alertBox;
    }

    /*
     * It returns the array containing data for creation of Valuelist sheet
     * contains the String fields of Designation, Category, Description
     * 
     * @return Array of the valuelist required fields
     */
    @DataProvider(name = "editValuelistValuesTestData")
    public Object[][] getValuelistTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                editValuelistSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResources() {
        valueListNodeInstance = SettingsLeftSectionMenubar
                .getValuelistSettingNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
    }

}
