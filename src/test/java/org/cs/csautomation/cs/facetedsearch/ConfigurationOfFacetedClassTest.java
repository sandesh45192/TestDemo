/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies creation of class for
 * Faceted Search
 * 
 * @author CSAutomation Team
 */
public class ConfigurationOfFacetedClassTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private PimStudioSettingsNode  pimStudioSettingsNodeInstance;
    private CSPortalHeader         csPortalHeaderInstance;
    private IClassPopup            classPopUp;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSGuiDialogContentId   csGuiDialogContentIdInstance;
    private Actions                action;
    private String                 configurationFacetedClassSheet = "ConfigurationFacetedClass";

    /**
     * This method verifies creation of class for faceted search.
     * 
     * @param className String object contains class name
     * @param attrFolder String object contains attribute folder name
     * @param attrName String object contains attribute name
     */
    @Test(priority = 1, dataProvider = "configurationOfFacetedClass")
    public void testConfigurationOfFacetedClass(String className,
            String attrFolder, String attrName) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createClass(className);
            verifyCreatedClass(className);
            enableShowInCategory(className);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testConfigurationOfFacetedClass",
                    e);
            Assert.fail("Automation Error : testConfigurationOfFacetedClass",
                    e);
        }
    }

    /**
     * This method assign attribute to class and verifies it.
     * 
     * @param className String object contains class name
     * @param attrFolder String object contains attribute folder name
     * @param attrName String object contains attribute name
     */
    @Test(priority = 2, dataProvider = "configurationOfFacetedClass")
    public void testAssignFacetedAttributesToFacetedClass(String className,
            String attrFolder, String attrName) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            dragAndDropAttrToClass(attrFolder, className);
            verifyAttributeInClass(className, attrName);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testConfigurationOfFacetedClass",
                    e);
            Assert.fail("Automation Error : testConfigurationOfFacetedClass",
                    e);
        }
    }

    /**
     * This method verifies assigned attribute to class .
     * 
     * @param className String object contains class name
     * @param attrFolder String object contains attribute folder name
     * @param attrName String object contains attribute name
     */
    private void verifyAttributeInClass(String className, String attrName) {
        String[] attributeArray = attrName.split(",");
        List<String> attributeList = Arrays.asList(attributeArray);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        WebElement elementOfClass = browserDriver
                .findElement(By.linkText(className));
        elementOfClass.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        int rowCount = browserDriver
                .findElements(
                        By.xpath("//table[@class='CSGuiTable']//tbody//tr"))
                .size();
        List<String> attrLable = new ArrayList<String>();
        WebElement rowElement = null;
        for (int row = 2; row <= rowCount; row++) {
            rowElement = browserDriver.findElement(
                    By.xpath("//table[@class='CSGuiTable']//tbody//tr[" + row
                            + "]//td[1]"));
            attrLable.add(rowElement.getText());
        }
        if (attributeList.containsAll(attrLable)) {
            CSLogger.info("Attribute is added to class successfully");
        } else {
            CSLogger.error("Attribute is not added");
            Assert.fail("Attribute is not added");
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method drag and drop attribute to class
     * 
     * @param folderName String object attribute folder name
     * @param className String object contains class name
     */
    private void dragAndDropAttrToClass(String folderName, String className) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode().click();
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement attrFolderToDragDrop = browserDriver
                .findElement(By.linkText(folderName));
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        action.dragAndDrop(attrFolderToDragDrop, classNameToDragDrop).build()
                .perform();
        CSLogger.info("Drag and Drop attribute to class");
    }

    /**
     * This method enables the check box Show in Category
     * 
     * @param className String object contains class name
     */
    private void enableShowInCategory(String className) {
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        WebElement elementOfClass = browserDriver
                .findElement(By.linkText(className));
        elementOfClass.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentIdInstance
                .checkCbPdmarticleconfigurationIsCategoryClass(waitForReload);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method verifies the creation of class
     * 
     * @param className String object contains class name
     */
    private void verifyCreatedClass(String className) {
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        WebElement nameOfClass = browserDriver
                .findElement(By.linkText(className));
        Assert.assertEquals(className, nameOfClass.getText());
    }

    /**
     * This method create class.
     * 
     * @param className String object contains class name
     */
    private void createClass(String className) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode(),
                browserDriver);
        classPopUp.selectPopupDivMenu(waitForReload,
                classPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        classPopUp.enterValueInDialogue(waitForReload, className);
        classPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Class Created");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains ClassName
     * 
     * @return configurationFacetedClass
     */
    @DataProvider(name = "configurationOfFacetedClass")
    public Object[] configurationOfFacetedClass() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                configurationFacetedClassSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        action = new Actions(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
    }
}
