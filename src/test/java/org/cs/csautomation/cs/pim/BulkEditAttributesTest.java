/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to verify test bulk edit works for
 * attributes
 * 
 * @author CSAutomation Team
 *
 */
public class BulkEditAttributesTest extends AbstractTest {
    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiDialogContentId   csGuiDialogContentIdInstance;
    private PimStudioSettingsNode  pimStudioSettingsNodeInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private IAttributePopup        attributePopup;
    private CSGuiListFooter        CSGuiListFooterInstance;

    private String                 bulkEditAttributesSheetName = "BulkEditAttributes";

    /**
     * This test method verifies that bulk editing works for attributes This
     * method bulk edits two attribute fields i.e pane title and section title
     * of two attributes passed as arguments.
     * 
     * @param attributeFolderName
     *            String Object containing attribute folder name.
     * @param textAttrOne
     *            String Object containing single line text attribute name.
     * @param textAttrTwo
     *            String Object containing single line text attribute name.
     * @param paneTitle
     *            String Object containing bulk edit value for pane title field
     * @param sectionTitle
     *            String Object containing bulk edit value for section title
     *            field
     */
    @Test(dataProvider = "bulkEditAttributesTestData")
    public void testVerifyBulkEdititngWorksForAttributes(
            String attributeFolderName, String textAttrOne, String textAttrTwo,
            String paneTitle, String sectionTitle) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 80);
            switchToPimAndExpandSettingsTree(waitForReload);
            createAttributeFolder(attributeFolderName);
            createAttributeInAttributeFolder(attributeFolderName, textAttrOne);
            createAttributeInAttributeFolder(attributeFolderName, textAttrTwo);
            selectAttributesForMassEditingOperation(attributeFolderName,
                    textAttrOne, textAttrTwo);
            performMassEditingOperation(false, paneTitle, sectionTitle);
            performMassEditingOperation(true, paneTitle, sectionTitle);
            verifyMassEditedAttributeValues(textAttrOne, textAttrTwo, paneTitle,
                    sectionTitle);
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method :"
                    + "testVerifyBulkEdititngWorksForAttributes: ", e);
            Assert.fail("Automation error in test method :"
                    + "testVerifyBulkEdititngWorksForAttributes: ", e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the settings tree.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void switchToPimAndExpandSettingsTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * This method clicks on attribute folder
     * 
     * @param attributeFolderName
     *            String Object containing attribute folder name.
     */
    private void clkOnAttributeFolder(String attributeFolderName) {
        getAttributeFolder(attributeFolderName).click();
    }

    /**
     * This method creates new attribute folder
     * 
     * @param attributeFolderName
     *            String Object containing attribute folder name.
     */
    private void createAttributeFolder(String attributeFolderName) {
        traverseToAttributeTreeFrames();
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode(),
                browserDriver);
        CSLogger.info("Right clicked on Attribute Node");
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuNewFolder(), browserDriver);
        CSLogger.info("Selected New Folder Option");
        attributePopup.enterValueInDialogue(waitForReload, attributeFolderName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * This method creates a single line text attribute under attribute folder
     * 
     * @param attributeFolderName
     *            String Object containing attribute folder name.
     * @param attributeName
     *            String Object containing attribute name.
     */
    private void createAttributeInAttributeFolder(String attributeFolderName,
            String attributeName) {
        traverseToAttributeTreeFrames();
        CSUtility.rightClickTreeNode(waitForReload,
                getAttributeFolder(attributeFolderName), browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSLogger.info("Selected New Folder Option");
        attributePopup.enterValueInUserInputDialogue(waitForReload,
                attributeName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        setAttributeLabel(attributeFolderName, attributeName, attributeName);
    }

    /**
     * Returns WebElement of attribute folder
     * 
     * @param attributeFolderName
     *            String Object containing attribute folder name.
     * @return WebElement attributeFolderName
     */
    private WebElement getAttributeFolder(String attributeFolderName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(attributeFolderName)));
        return browserDriver.findElement(By.linkText(attributeFolderName));
    }

    /**
     * Switches the frame till pim studio frame
     */
    private void traverseToAttributeTreeFrames() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method selects attributes from right section for bulk editing
     * 
     * @param attributeFolderName
     *            String Object containing attribute folder name.
     * @param textTypeAttrOne
     *            String Object containing single line text attribute name.
     * @param textTypeAttrTwo
     *            String Object containing single line text attribute name.
     */
    private void selectAttributesForMassEditingOperation(
            String attributeFolderName, String textTypeAttrOne,
            String textTypeAttrTwo) {
        traverseToAttributeTreeFrames();
        clkOnAttributeFolder(attributeFolderName);
        String attributeIdOne = getAttributeId(textTypeAttrOne);
        String attributeIdTwo = getAttributeId(textTypeAttrTwo);
        traverseToAttributeTreeFrames();
        clkOnAttributeFolder(attributeFolderName);
        clkOnAttributeSelectBox(attributeIdOne);
        clkOnAttributeSelectBox(attributeIdTwo);
    }

    /**
     * Clicks on selectBox of specified attributeId
     * 
     * @param attributeId
     *            String object containing attribute Id
     */
    private void clkOnAttributeSelectBox(String attributeId) {
        goToAttributeRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//table[contains(@class,'CSAdminList')]")));
        WebElement selectBox = browserDriver.findElement(
                By.xpath("//input[contains(@value,'" + attributeId + "')]"));
        selectBox.click();
    }

    /**
     * Switches the frame till spiltAreaMain Frame
     */
    private void goToAttributeRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * Selects the drop down option of mass editing and returns current window
     * handle
     * 
     * @return windowHandle
     */
    private String selectMassEditingOption() {
        goToAttributeRightSectionWindow();
        String windowHandle = browserDriver.getWindowHandle();
        CSGuiListFooterInstance.selectOptionFromDrpDwnMassUpdateSelector(
                waitForReload, "Mass Editing");
        return windowHandle;
    }

    /**
     * This method sets the attribute label with value passed as argument
     * 
     * @param attributeFolderName
     *            String Object containing attribute folder name.
     * @param attributeName
     *            String Object containing attribute name.
     * @param attributeLabelValue
     *            String Object containing label for attribute
     */
    private void setAttributeLabel(String attributeFolderName,
            String attributeName, String attributeLabelValue) {
        traverseToAttributeTreeFrames();
        clkOnAttributeFolder(attributeFolderName);
        getAttribute(attributeName).click();
        goToAttributeRightSectionWindow();
        enterValueInTextFiled(
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationLabel(),
                attributeLabelValue);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Attribute label set with value " + attributeLabelValue);
    }

    /**
     * Returns the WebElement of specified attribute
     * 
     * @param attributeName
     *            String Object containing attribute name.
     * @return WebElement
     */
    private WebElement getAttribute(String attributeName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(attributeName)));
        return browserDriver.findElement(By.linkText(attributeName));
    }

    /**
     * This method returns the ID of the attribute passed as argument
     * 
     * @param attributeName
     *            String containing name of attribute
     * @return attribute Id String Object containing attribute Id
     */
    private String getAttributeId(String attributeName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(
                browserDriver.findElement(By.linkText(attributeName)))
                .perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        return browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
    }

    /**
     * This method performs mass editing operation on attributes
     * 
     * @param isPressOkay
     *            Contains boolean value
     * @param attrValueOne
     *            Contains mass editing value
     * @param attrValueTwo
     *            Contains mass editing value
     */
    private void performMassEditingOperation(Boolean isPressOkay,
            String attrValueOne, String attrValueTwo) {
        String windowHandleBeforeEditingWindow = selectMassEditingOption();
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (isPressOkay) {
            alert.accept();
            CSLogger.info("clicked on ok of pop up for mass editing operation");
            goToAttributeRightSectionWindow();
            editAttributesFromEditorWindow(windowHandleBeforeEditingWindow,
                    attrValueOne, attrValueTwo);
        } else {
            alert.dismiss();
            CSLogger.info(
                    "clicked on cancel of pop up for mass editing operation");
        }
    }

    /**
     * Mass edits some fields of attributes i.e pane title and section title
     * fieldsF
     * 
     * @param windowHandleBeforeEditingWindow
     * @param attrValueOne
     *            String object containing value for mass editing attribute
     *            fields
     * @param attrValueTwo
     *            String object containing value for mass editing attribute
     *            fields
     */
    private void editAttributesFromEditorWindow(
            String windowHandleBeforeEditingWindow, String attrValueOne,
            String attrValueTwo) {
        switchToCurrentWindowHandle();
        enterValueInTextFiled(
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationSectionTitle(),
                attrValueTwo);
        enterValueInTextFiled(
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationPaneTitle(),
                attrValueOne);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        browserDriver.close();
        browserDriver.switchTo().window(windowHandleBeforeEditingWindow);
    }

    /**
     * switch to current window handle.
     */
    private void switchToCurrentWindowHandle() {
        try {
            for (String windowHandle : browserDriver.getWindowHandles()) {
                browserDriver.switchTo().window(windowHandle);
            }
            CSLogger.info("Switched to new window handle");
        } catch (Exception e) {
            CSLogger.error("Error while switching to new window handle ");
        }
    }

    /**
     * Enters text in text field of attributes specified as arguments
     * 
     * @param txtAttribute
     *            WebElement attribute
     * @param txtAttributeValue
     *            String object containing value that needs to be entered in
     *            text attribute
     */
    private void enterValueInTextFiled(WebElement txtAttribute,
            String txtAttributeValue) {
        txtAttribute.click();
        txtAttribute.clear();
        txtAttribute.sendKeys(txtAttributeValue);
    }

    /**
     * Verifies whether mass editing operation on attributes passed or Failed
     * 
     * @param attributeNameOne
     *            String Object containing single line text attribute name.
     * @param attributeNameTwo
     *            String Object containing single line text attribute name.
     * @param actualPaneTitleFieldValue
     *            value of attribute pane title field passed from sheet
     * @param actualMassEditedSectionTitleValue
     *            value of attribute section title passed from sheet
     */
    private void verifyMassEditedAttributeValues(String attributeNameOne,
            String attributeNameTwo, String actualPaneTitleFieldValue,
            String actualMassEditedSectionTitleValue) {
        Boolean isVerifiedAttrOneValues = getEditedAttributeFieldValues(
                attributeNameOne, actualPaneTitleFieldValue,
                actualMassEditedSectionTitleValue);
        Boolean isVerifiedAttrTwoValues = getEditedAttributeFieldValues(
                attributeNameTwo, actualPaneTitleFieldValue,
                actualMassEditedSectionTitleValue);
        if (isVerifiedAttrOneValues && isVerifiedAttrTwoValues) {
            CSLogger.info(
                    "Selected attributes  modified successfully mass editing "
                            + "operation passed : testcase passed");
        } else {
            CSLogger.error(
                    "Selected attributes  modification failed mass editing "
                            + "operation failed : testcase failed");
            Assert.fail("Selected attributes  modification failed mass editing "
                    + "operation failed : testcase failed");
        }
    }

    /**
     * verifies whether attributes modified successfully
     * 
     * @param attributeNameOne
     *            String Object containing single line text attribute name.
     * @param actualPaneTitleFieldValue
     *            value of attribute pane title field passed from sheet
     * @param actualSectionTitleValue
     *            value of attribute section title passed from sheet
     * @return boolean value true or false
     */
    private boolean getEditedAttributeFieldValues(String attributeNameOne,
            String actualPaneTitleFieldValue, String actualSectionTitleValue) {
        traverseToAttributeTreeFrames();
        getAttribute(attributeNameOne).click();
        goToAttributeRightSectionWindow();
        csGuiDialogContentIdInstance.getTxtPdmarticleconfigurationPaneTitle()
                .click();
        String expectedMassEditedPaneTitleFieldValue = csGuiDialogContentIdInstance
                .getTxtPdmarticleconfigurationPaneTitle().getAttribute("value");
        csGuiDialogContentIdInstance.getTxtPdmarticleconfigurationSectionTitle()
                .click();
        String expectedMassEditedSectionTitleValue = csGuiDialogContentIdInstance
                .getTxtPdmarticleconfigurationSectionTitle()
                .getAttribute("value");

        if (expectedMassEditedPaneTitleFieldValue
                .equals(actualPaneTitleFieldValue)
                && expectedMassEditedSectionTitleValue
                        .equals(actualSectionTitleValue)) {
            return true;
        } else
            return false;
    }

    /**
     * This is a data provider method contains array of attribute folder name
     * ,attribute names ,pane title and section title field values
     * 
     * @return Array
     */
    @DataProvider(name = "bulkEditAttributesTestData")
    public Object[][] getBulkEditAttributesData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                bulkEditAttributesSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        pimStudioSettingsNodeInstance = new PimStudioSettingsNode(
                browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        CSGuiListFooterInstance = new CSGuiListFooter(browserDriver);
    }
}
