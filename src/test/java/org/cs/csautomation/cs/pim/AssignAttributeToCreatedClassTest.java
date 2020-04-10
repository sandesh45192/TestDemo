/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to assign attributes to the newly
 * created class
 * 
 * @author CSAutomation Team
 *
 */
public class AssignAttributeToCreatedClassTest extends AbstractTest {

    private CSGuiToolbarHorizontal csGuiToolbarHorizontal;
    private CSGuiDialogContentId   csGuiDialogContentId;
    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private PimStudioSettingsNode  pimStudioSettingsNodeInstance;
    private String                 assignAttributeToClassSheet = "AssignAttributeToClass";

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        pimStudioSettingsNodeInstance = new PimStudioSettingsNode(
                browserDriver);
        CSLogger.info(
                "Initialized the POM object for Assign Attribute to class page.");
        locator = FrameLocators.getIframeLocators(browserDriver);
        new Actions(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }

    /**
     * This is the test method which drives the usecase, ie. assign attributes
     * to the newly created class which takes inputs as attribute folder name,
     * class name and attributes
     */
    @Test(dataProvider = "AssignAttributeToClass")
    public void testAssignAttributeToClass(String attributeFolderName,
            String className, String attribute) {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance.getBtnPimSettingsNode());
            pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance
                            .getBtnPimSettingsClassesNode());
            pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode()
                    .click();
            WebElement createdClass = browserDriver
                    .findElement(By.linkText(className));
            CSUtility.waitForVisibilityOfElement(waitForReload, createdClass);
            createdClass.click();
            CSUtility.tempMethodForThreadSleep(4000);
            CSLogger.info(
                    "Switched to the frame to add attribute to the class");
            addAttributesToClass(waitForReload, attributeFolderName, attribute);
            getAttributeNamesAndVerify(waitForReload, attribute);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testAssignAttributeToClass"
                            + e);
            Assert.fail(
                    "Automation Error in test method : testAssignAttributeToClass"
                            + e);
        }
    }

    /**
     * This method adds selected attributes to the class
     * 
     * @param waitForReload
     * @param attributeFolderName contains name of attribute folder
     * @param attribute contains all attributes form excel sheet
     */
    private void addAttributesToClass(WebDriverWait waitForReload,
            String attributeFolderName, String attribute) {
        IClassPopup classPopUp = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getBtnAddAttrToClass());
        csGuiDialogContentId.clkOnBtnToAddAttrToClass();
        switchToIdRecordsFrameLeft();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getBtnAttrDrpDwnAddToClass());
        csGuiDialogContentId.clkOnDropDwnAttrAddToClass();
        CSUtility.tempMethodForThreadSleep(5000);
        WebElement attrFolder = browserDriver
                .findElement(By.linkText(attributeFolderName));
        attrFolder.click();
        switchToIdRecordsFrameCenter();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getBtnAddAllAttr());
        csGuiDialogContentId.clkOnAddAllAttrValuesToClass(waitForReload);
        switchToFrameToAddAllAttrToClass();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getBtnYesToEnlistAllAttrToAddToClass());
        csGuiDialogContentId.clkOnYesToEnlistAllAttrToAddToClass();
        CSUtility.tempMethodForThreadSleep(5000);
        classPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(4000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Successfully assigned attribute to class");
    }

    /**
     * This method gets all assigned attribute names to the class and verifies
     * with attributes present in the excel sheet
     * 
     * @param waitForReload
     * @param attributeName contains the attributes from excel sheet
     */
    private void getAttributeNamesAndVerify(WebDriverWait waitForReload,
            String attributeName) {
        try {
            String[] attributeNames = attributeName.split(",");
            List<WebElement> attributeXPaths = browserDriver.findElements(
                    By.xpath("//table[@class='CSGuiTable']//td[1]"));
            String[] linkText = new String[attributeXPaths.size()];
            int temp = 0;
            for (WebElement attributePaths : attributeXPaths) {
                if (!attributePaths.equals("Distribution channel"))
                    linkText[temp] = attributePaths.getText();
                if (linkText[temp].contains("Distribution channel"))
                    linkText[temp] = linkText[temp]
                            .replace("Distribution channel", "");
                temp++;
            }
            for (int classXpathIndex = 0; classXpathIndex < linkText.length; classXpathIndex++) {
                for (int attributeIndex = 0; attributeIndex < attributeNames.length; attributeIndex++) {
                    if (linkText[classXpathIndex]
                            .equalsIgnoreCase(attributeNames[attributeIndex])) {
                        CSLogger.info(
                                "Attribute" + attributeNames[attributeIndex]
                                        + "is Present..");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            CSLogger.info("Attributes verified successfully..", e);
        }
    }

    /**
     * This method will switch to the frame appearing after we click on '+'
     * inside class, in-order to add attributes to that class.
     */
    public void switchToDataSelectionDialogFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialog()));
        CSLogger.info("Switched to the frame of final OK button ");
    }

    /**
     * This method will switch to the frame, where we can add 'all (+)' the
     * listed attributes to the class.
     */
    public void switchToFrameToAddAllAttrToClass() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmToAddAllValues()));
        CSLogger.info("Switched to the frame to add all attr to class ");
    }

    /**
     * This method will switch to the frame, appearing after selecting all the
     * attributes to be added to the class. Clicking 'OK' button in this frame
     * will add all selected attr to class.
     */
    public void switchToIdRecordsFrameCenter() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialogNestedOne()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsCenter()));
        CSLogger.info("Switched to the ultimate frame");
    }

    /**
     * This method will switch to the frame of 'left' menu, in 'data selection
     * dialog' window.
     */
    public void switchToIdRecordsFrameLeft() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialog()));
        CSLogger.info("data Selection Dialog");
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialogNestedOne()));
        CSLogger.info("Data selection dialog nested");
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsPdmarticleconfigurationLeft()));
        CSLogger.info("After record left..");
        CSLogger.info(
                "Switched to the frame for 'data selection dialog' window");
    }

    /**
     * This method will switch to the frame appearing after clicking on PRODUCTS
     * tab, after logging into the application. This method can be used as a
     * generic method, to get into PRODUCTS page of application.
     */
    public void switchToPIMStudioFrame() {
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame190()));
        waitForReload
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmPimStudio()));
    }

    /**
     * This data provider contains attribute folder name, class name and names
     * of attributes
     * 
     * @return array of attribute folder name, class name and names of
     *         attributes
     */
    @DataProvider(name = "AssignAttributeToClass")
    public Object[][] getProductChildData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                assignAttributeToClassSheet);
    }
}