/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create new pim attribute.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateNewPimAttributeTest extends AbstractTest {

    private WebDriverWait         waitForReload;
    private CSGuiDialogContentId  csGuiDialogContentIdInstance;
    private String                createPimAttributeSheet = "CreatePimAttribute";
    private CSPortalHeader        csPortalHeaderInstance;
    private PimStudioSettingsNode pimStudioSettingsNodeInstance;

    /**
     * This the the test method which drives the usecase
     */
    @Test(dataProvider = "newPimAttributeTestData")
    public void testCreateNewPimAttributeTest(String folderName,
            String attributeName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandSettingsTree(waitForReload);
            clkOnAttributesNode();
            performUsecase(waitForReload, attributeName, false, folderName);
            performUsecase(waitForReload, attributeName, true, folderName);
            clickOnNewlyCreatedAttribute(attributeName);
            checkExpectedBehaviour(waitForReload, attributeName);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testCreateNewPimAttributeTest "
                            + e);
            Assert.fail(
                    "Automation Error in test method : testCreateNewPimAttributeTest "
                            + e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandSettingsTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * Clicks on attributes node in pim studio
     */
    public void clkOnAttributesNode() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode());
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
    }

    /**
     * This method performs the testcase
     * 
     * @param waitForReload it is object of WebDriverWait contains time which is
     *            used to wait for particular element to reload
     * @param userInputFolderName it is string value which contains value of new
     *            pim attribute
     * @param pressOkay contains value true or false
     */
    public void performUsecase(WebDriverWait waitForReload,
            String userInputFolderName, Boolean pressOkay, String folderName) {
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(folderName));
        CSUtility.rightClickTreeNode(waitForReload, createdAttributeFolder,
                browserDriver);
        CSLogger.info("Right Clicked On Newly Created Attribute Folder");
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSLogger.info("Clicked on Create New submenu");
        attributePopup.enterValueInUserInputDialogue(waitForReload,
                userInputFolderName);
        attributePopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        validateCreationOfNewAttribute(waitForReload, pressOkay, folderName,
                userInputFolderName);
        CSUtility.switchToDefaultFrame(browserDriver);
    }

    /**
     * This method will validate whether attribute creation is valid or not
     * 
     * @param waitForReload it is object of WebDriverWait contains time which is
     *            used to wait for particular element to reload
     * @param pressOkay contains value true or false
     */
    public void validateCreationOfNewAttribute(WebDriverWait waitForReload,
            Boolean pressOkay, String folderName, String attributeName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement parentAttributeFolder = browserDriver
                .findElement(By.linkText(folderName));
        parentAttributeFolder.click();
        List<WebElement> childAttribute = browserDriver
                .findElements(By.linkText(attributeName));
        Assert.assertEquals(childAttribute.isEmpty(), !pressOkay);
    }

    /**
     * This method clicks on newly created Attribute
     * 
     * @param attributeName it is string value which contains name of attribute
     */
    public void clickOnNewlyCreatedAttribute(String attributeName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement newlyCreatedAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        newlyCreatedAttribute.click();
        CSLogger.info("Clicked on newly created attribute");
    }

    /**
     * This method will check the default configuration of attribute are proper
     * or not it uses some assert statement for validations.
     * 
     * @param waitForReload it is object of WebDriverWait contains time which is
     *            used to wait for particular element to reload
     * 
     */
    public void checkExpectedBehaviour(WebDriverWait waitForReload,
            String attributeName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        final String attributeType = getValueOfElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnCSTypeLabel());
        final String technicalAttributeName = getValueOfElement(waitForReload,
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationName());
        final String attributeLabel = getValueOfElement(waitForReload,
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationLabel());
        final String sortOrder = getValueOfElement(waitForReload,
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationGuiSortOrder());
        final String requiredAttribute = getValueOfSelectedDropDownOption(
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsRequired());
        final String inheritance = getValueOfSelectedDropDownOption(
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsInherited());
        final String search = getValueOfSelectedDropDownOption(
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsSearchable());
        if (attributeType.equals("Single-line Text")
                && technicalAttributeName.equals(attributeName)
                && attributeLabel.equals("") && sortOrder.equals("0")
                && requiredAttribute.equals("Never")
                && inheritance.equals("No Inheritance")
                && search.equals("Not searchable")) {
            CSLogger.info(
                    "Created New Attribute with proper default configuration "
                            + "values");
        } else {
            Assert.fail("The Default Configuration fileds of newly created"
                    + " attribute are improper");
            CSLogger.error(" Newly Created Attribute has  improper default"
                    + " configuration values ");
        }
    }

    /**
     * This method returns the value of any specified attribute
     * 
     * @param element type of webElement
     * @param attribute contains string like class or id
     * @return value of that specified attribute
     */
    public static String getAttributeValue(WebElement element,
            String attribute) {
        return element.getAttribute(attribute);
    }

    /**
     * This method gets value of element
     * 
     * @param waitForReload it is WebDriverWait object
     * @param element type of webElement
     * @return value of that specified attribute
     */
    public static String getValueOfElement(WebDriverWait waitForReload,
            WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        return getAttributeValue(element, "value");
    }

    /**
     * This method returns the first option from the drop down list
     * 
     * @param element type of webElement
     * @return text of first selected option from drop down
     */
    public static String getValueOfSelectedDropDownOption(WebElement element) {
        Select select = new Select(element);
        WebElement option = select.getFirstSelectedOption();
        return option.getText();
    }

    @DataProvider(name = "newPimAttributeTestData")
    public Object[][] getPimAttributeTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                createPimAttributeSheet);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod()
    public void initializeResource() {
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
    }
}
