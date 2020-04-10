/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to create and edit a subchild product.
 *
 * @author CSAutomation Team
 *
 */
public class EditSubChildProductTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim             csPopupDiv;
    private SoftAssert                softAssertion;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    public EditProductTest            editProductTestInstance;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private IProductPopup             productPopup;
    private String                    createSubchildProductSheetName = "CreateSubchildProduct";
    private String                    editSubchildProductSheetName   = "EditSubchildProduct";
    private FrameLocators             iframeLocatorsInstance;

    /**
     * This test method verifies the attributes data under respective panes of
     * child product
     * 
     * @param productName String parameter containing name of the product under
     *            which child product will be created
     * @param childProductName String parameter containing name of the child
     *            product
     * @param subchildProductName String parameter containing name of the sub
     *            child product
     * @param textAttributeName String parameter containing name of text
     *            attribute i.e single line text attribute
     * @param languageAttributeName String parameter containing name of language
     *            type attribute
     * @param searchAttributeName String parameter containing name of search
     *            type attribute
     * @param inheritanceAttributeName String parameter containing name of
     *            inheritance type attribute
     * @param inheritanceAttributeValue String parameter containing data of
     *            inheritance attribute
     * @param requiredAttributeName String parameter containing name of required
     *            type attribute
     * @param className String parameter containing name of class that will be
     *            assigned to product
     * @param textAttributeTabName String parameter containing name of pane
     *            holding text attributes i.e Data
     * @param referenceAttributeTabName String parameter containing name of pane
     *            holding reference attributes
     */
    @Test(priority = 1, dataProvider = "createSubchildProductTestData")
    public void testVerifyPaneAndAttributesOfChildProduct(String productName,
            String childProductName, String subchildProductName,
            String textAttributeName, String languageAttributeName,
            String searchAttributeName, String inheritanceAttributeName,
            String inheritanceAttributeValue, String requiredAttributeName,
            String className, String textAttributeTabName,
            String referenceAttributeTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandProductTree(waitForReload);
            createChildProduct(productName, childProductName);
            selectNewlyCreatedChildProduct(childProductName);
            goToRightSectionWindow();
            verifyPropertiesPaneFields(className, childProductName);
            verifyDataPaneFields(textAttributeTabName, textAttributeName,
                    languageAttributeName, searchAttributeName,
                    inheritanceAttributeName, inheritanceAttributeValue,
                    requiredAttributeName);
            verifyReferencesPaneFields(referenceAttributeTabName);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test : testVerifyPaneTitleFieldsOfChildProduct",
                    e);
            Assert.fail(
                    "Automation Error in test : testVerifyPaneTitleFieldsOfChildProduct",
                    e);
        }
    }

    /**
     * This test method creates a subchild product and also verifies the
     * inherited class of both child and subchild product
     * 
     * @param productName String parameter containing name of the product under
     *            which child product will be created
     * @param childProductName String parameter containing name of the child
     *            product
     * @param subchildProductName String parameter containing name of the sub
     *            child product
     * @param textAttributeName String parameter containing name of text
     *            attribute i.e single line text attribute
     * @param languageAttributeName String parameter containing name of language
     *            type attribute
     * @param searchAttributeName String parameter containing name of search
     *            type attribute
     * @param inheritanceAttributeName String parameter containing name of
     *            inheritance type attribute
     * @param inheritanceAttributeValue String parameter containing data of
     *            inheritance attribute
     * @param requiredAttributeName String parameter containing name of required
     *            type attribute
     * @param className String parameter containing name of class that will be
     *            assigned to product
     * @param textAttributeTabName String parameter containing name of pane
     *            holding text attributes i.e Data
     * @param referenceAttributeTabName String parameter containing name of pane
     *            holding reference attributes
     */
    @Test(
            priority = 2,
            dataProvider = "createSubchildProductTestData",
            dependsOnMethods = "testVerifyPaneAndAttributesOfChildProduct")
    public void testCreateSubchildProduct(String productName,
            String childProductName, String subchildProductName,
            String textAttributeName, String languageAttributeName,
            String searchAttributeName, String inheritanceAttributeName,
            String inheritanceAttributeValue, String requiredAttributeName,
            String className, String textAttributeTabName,
            String referenceAttributeTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            createSubchildProduct(productName, childProductName,
                    subchildProductName);
            verifyClassesFieldOfChildProduct(childProductName, className);
            verifyClassesFieldOfSubchildProduct(subchildProductName, className);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test : testCreateSubchildProduct", e);
            Assert.fail("Automation Error in test : testCreateSubchildProduct",
                    e);
        }
    }

    /**
     * This test method edits required,language,reference attributes of sub
     * child product
     * 
     * @param productName String parameter containing name of the product under
     *            which child product will be created
     * @param childProductName String parameter containing name of the child
     *            product
     * @param subchildProductName String parameter containing name of the sub
     *            child product
     * @param requiredAttributeName String parameter containing name of required
     *            attribute
     * @param requiredAttributeValue String parameter containing data of
     *            required attribute
     * @param inheritanceAttributeName String parameter containing name of
     *            inheritance attribute
     * @param inheritedAttributeValue String parameter contains inherited data
     *            of parent product for inheritance attribute
     * @param changedInheritanceAttributeValue String parameter contains data
     *            that is edited for inheritance attribute
     * @param languageAttributeName String parameter containing data of language
     *            attribute
     * @param changedLanguage String parameter containing name of language that
     *            will be selected during runtime
     * @param defaultLanguageData String parameter containing data of default
     *            language selected
     * @param changedLanguageData String parameter containing data of language
     *            that is selected while editing
     * @param pimReferenceAttribute String parameter contains name and value of
     *            PIM reference type attribute i.e referenceAttributeName:Value
     * @param mamReferenceAttribute String parameter contains name and value of
     *            MAM reference type attribute i.e referenceAttributeName:Value
     * @param coreReferenceAttribute String parameter contains name and value of
     *            core reference type attribute i.e referenceAttributeName:Value
     * @param textAttributeTabName String parameter containing name of pane
     *            holding text attributes i.e Data
     * @param referenceAttributeTabName String parameter containing name of pane
     *            holding reference attributes
     */
    @Test(
            priority = 3,
            dataProvider = "EditSubchildProductTestData",
            dependsOnMethods = "testCreateSubchildProduct")
    public void testEditSubchildProductAttributes(String productName,
            String childProductName, String subchildProductName,
            String requiredAttributeName, String requiredAttributeValue,
            String inheritanceAttributeName, String inheritedAttributeValue,
            String changedInheritanceAttributeValue,
            String languageAttributeName, String changedLanguage,
            String defaultLanguageData, String changedLanguageData,
            String pimReferenceAttribute, String mamReferenceAttribute,
            String coreReferenceAttribute, String textAttributeTabName,
            String referenceAttributeTabName, String defaultLanguage) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            goToRightSectionWindow();
            clickOnTheGivenProductTab(textAttributeTabName, waitForReload);
            editRequiredAttribute(textAttributeTabName, requiredAttributeName,
                    requiredAttributeValue);
            editInheritanceAttribute(subchildProductName,
                    inheritanceAttributeName, inheritedAttributeValue,
                    changedInheritanceAttributeValue);
            editLanguageAttribute(childProductName, subchildProductName,
                    changedLanguage, languageAttributeName, defaultLanguageData,
                    changedLanguageData);
            checkOutProduct(subchildProductName);
            checkReferenceFieldsEditableOrNot(productName,
                    pimReferenceAttribute, referenceAttributeTabName,
                    waitForReload, browserDriver);
            checkReferenceFieldsEditableOrNot("EditProduct",
                    mamReferenceAttribute, referenceAttributeTabName,
                    waitForReload, browserDriver);
            checkReferenceFieldsEditableOrNot("EditProduct",
                    coreReferenceAttribute, referenceAttributeTabName,
                    waitForReload, browserDriver);
            checkInProduct(subchildProductName);
            setDefaultLanguage(defaultLanguage);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation Error in test : "
                    + "testEditSubchildProductAttributes", e);
            Assert.fail("Automation Error in test : "
                    + "testEditSubchildProductAttributes", e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandProductTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method creates the child product with the supplied name and the
     * supplied parent product name.
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param childProductName String Object containing the child product name.
     * @param waitForReload WebDriverWait Object
     */
    private void createChildProduct(String parentProductName,
            String childProductName) {
        try {
            WebElement element = pimStudioProductsNodeInstance
                    .expandNodesByProductName(waitForReload, parentProductName);
            createProduct(childProductName, element,
                    csPopupDiv.getCsGuiPopupMenuNewChild());
            CSLogger.info("Created new child product");
        } catch (Exception e) {
            Assert.fail("Parent product not found");
            CSLogger.error("Parent product not found", e);
        }
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName String object containing name of product it can be
     *            child product or parent product
     * @param nodeElement WebElement of either products node or parent product
     * @param popupMenuOption WebElement containing menu option.
     */
    private void createProduct(String productName, WebElement nodeElement,
            WebElement popupMenuOption) {
        CSUtility.rightClickTreeNode(waitForReload, nodeElement, browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload, popupMenuOption,
                browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered value " + productName + " in the textfield.");
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimStudioTreeSection();
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * Selects newly created child product
     * 
     * @param childProductName String parameter contains name of child product
     */
    private void selectNewlyCreatedChildProduct(String childProductName) {
        goToPimStudioTreeSection();
        WebElement childProduct = getProduct(childProductName);
        childProduct.click();
        CSLogger.info("Clicked on child product");
    }

    /**
     * This method verifies that in Properties pane,Label is defined as product
     * name and class is inherited class from parent folder.
     * 
     * @param className String parameter contains name of class
     * @param childProductName String parameter contains name of child product
     */
    private void verifyPropertiesPaneFields(String className,
            String childProductName) {
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance.clkOnProductLabel(waitForReload);
        CSLogger.info("Clicked on product label from properties pane");
        String label = csGuiDialogContentIdInstance.getTxtProductLabel()
                .getAttribute("defaultvalue");
        int inheritedClass = browserDriver
                .findElements(By
                        .xpath("//span[contains(text(),'" + className + "')]"))
                .size();
        if (label.equals(childProductName) && inheritedClass != 0) {
            CSLogger.info(
                    "In Properties pane: Label is defined as product name i.e "
                            + childProductName
                            + "and class is inherited class from parent folder i.e "
                            + className + "teststep passed");
        } else {
            CSLogger.error(
                    "Error in teststep : In Properties pane : Label should be "
                            + "defined and class should be the inherited class from "
                            + "parent folder :teststep failed");
            softAssertion
                    .fail("Error in teststep : In Properties pane : Label should be "
                            + "defined and class should be the inherited class from "
                            + "parent folder :teststep failed");
        }
    }

    /**
     * Gets the product passed as parameter
     * 
     * @param productName String parameter contains name of product to return
     * @return WebElement of product
     */
    public WebElement getProduct(String productName) {
        CSUtility.scrollUpOrDownToElement(
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(productName)));
        return browserDriver.findElement(By.linkText(productName));
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method verifies that in Data pane text, Language, Required,
     * Searchable attribute fields are empty and Inheritance attribute should
     * contain inherited value of parent which should be in grey color.
     * 
     * @param textAttributeTabName String parameter containing name of pane
     *            holding text attributes i.e Data
     * @param textAttribute String parameter containing name of text attribute
     *            i.e single line text attribute
     * @param languageAttribute String parameter containing name of language
     *            type attribute
     * @param searchAttribute String parameter containing name of search type
     *            attribute
     * @param inheritanceAttribute String parameter containing name of
     *            inheritance type attribute
     * @param inheritanceAttributeValue String parameter containing data of
     *            inheritance attribute
     * @param inheritanceAttrValue String parameter containing data of
     *            inheritance attribute
     * @param requiredAttribute String parameter containing data of required
     *            attribute
     */
    private void verifyDataPaneFields(String textAttrTabName,
            String textAttribute, String languageAttribute,
            String searchAttribute, String inheritanceAttribute,
            String inheritanceAttrValue, String requiredAttribute) {
        clickOnTheGivenProductTab(textAttrTabName, waitForReload);
        String textAttributeValue = getAttributeValue(textAttribute);
        String languageAttributeValue = getAttributeValue(languageAttribute);
        String searchAttributeValue = getAttributeValue(searchAttribute);
        String inheritanceAttributeValue = getAttributeValue(
                inheritanceAttribute);
        String requiredAttributeValue = getAttributeValue(requiredAttribute);
        String getInheritedAttrTextColor = getAttributeTextColor(
                inheritanceAttribute);
        if (textAttributeValue.equals("") && languageAttributeValue.equals("")
                && searchAttributeValue.equals("")
                && requiredAttributeValue.equals("")
                && inheritanceAttributeValue.equals(inheritanceAttrValue)
                && getInheritedAttrTextColor.equals("rgba(136, 136, 136, 1)")) {
            CSLogger.info(
                    "In Data pane text, Language, Required, Searchable attribute "
                            + "fileds are empty and Inheritance should be the value "
                            + inheritanceAttrValue
                            + "which is in grey color : teststep passed ");
        } else {
            CSLogger.error(
                    "Error in teststep:In Data pane text, Language, Required, "
                            + "Searchable attribute fileds are empty and Inheritance "
                            + "should be the value " + inheritanceAttrValue
                            + "which is in grey color : teststep failed ");
            softAssertion
                    .fail("Error in teststep:In Data pane text, Language, Required, "
                            + "Searchable attribute fileds are empty and Inheritance "
                            + "should be the value " + inheritanceAttrValue
                            + "which is in grey color : teststep failed ");
        }
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String name of the tab in the product view.
     * @param waitForReload
     */
    private void clickOnTheGivenProductTab(String productTabName,
            WebDriverWait waitForReload) {
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * Gets the attribute value
     * 
     * @param attributeName String parameter containing name of attribute
     * @return
     */
    private String getAttributeValue(String attributeName) {
        clkOnTextField(attributeName);
        WebElement elementLocator = getTextFieldElement(attributeName);
        return elementLocator.getAttribute("value");
    }

    /**
     * Gets attribute border-color of text
     * 
     * @param attributeName String parameter containing name of attribute
     * @return
     */
    private String getAttributeTextColor(String attributeName) {
        WebElement elementLocator = getTextFieldElement(attributeName);
        return elementLocator.getCssValue("color");
    }

    /**
     * Gets attribute color of text
     * 
     * @param attributeName String parameter containing name of attribute
     * @return
     */
    private String getModifiedAttributeTextColor(String attributeName) {
        WebElement elementLocator = getTextFieldElement(attributeName);
        return elementLocator.getCssValue("color");
    }

    /**
     * This method performs operation of clicking on text fields
     * 
     * @param attributeName String type that contains name of the attribute
     */
    public void clkOnTextField(String attributeName) {
        WebElement elementLocator = getTextFieldElement(attributeName);
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(elementLocator).perform();
    }

    /**
     * Returns the text field WebElement
     * 
     * @param attributeName String parameter containing name of attribute
     * @return text field WebElement
     */
    private WebElement getTextFieldElement(String attributeName) {
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]")));
        return browserDriver.findElement(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]"));
    }

    /**
     * Verifies that in Reference pane all fields are empty
     * 
     * @param referenceAttrTabName String parameter contains name of pane or tab
     */
    private void verifyReferencesPaneFields(String referenceAttrTabName) {
        clickOnTheGivenProductTab(referenceAttrTabName, waitForReload);
        int assignedReference = browserDriver
                .findElements(By.xpath("//table[@class='CSGuiThumbList']"))
                .size();
        if (assignedReference != 0) {
            CSLogger.error(
                    "Error in teststep : In Reference pane - All fields should "
                            + "be empty  : teststep failed");
            softAssertion
                    .fail("Error in teststep : In Reference pane - All fields "
                            + "should be empty (no inheritance defined). : teststep failed");
        } else {
            CSLogger.info(
                    "In Reference pane - All Reference fields are empty : "
                            + "teststep passed");
        }
    }

    /**
     * Created a sub child product and also performs verification of creation
     * 
     * @param productName String parameter containing name of the product under
     *            which child product will be created
     * @param childProductName String parameter containing name of the child
     *            product
     * @param subchildProductName String parameter containing name of the sub
     *            child product
     */
    private void createSubchildProduct(String productName,
            String childProductName, String subchildProductName) {
        goToPimStudioTreeSection();
        WebElement childProduct = getProduct(childProductName);
        createProduct(subchildProductName, childProduct,
                csPopupDiv.getCsGuiPopupMenuNewChild());
        CSLogger.info("Created new sub child product");
        verifyCreationOfSubchildProduct(productName, subchildProductName);
    }

    /**
     * Performs verification of creation of sub child product
     * 
     * @param productName String parameter containing name of the product under
     *            which child product will be created
     * @param subchildProductName String parameter containing name of the sub
     *            child products
     */
    private void verifyCreationOfSubchildProduct(String productName,
            String subchildProductName) {
        goToPimStudioTreeSection();
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getProduct(productName)));
        CSUtility.tempMethodForThreadSleep(1000);
        int existenceOfSubchildProduct = browserDriver
                .findElements(By.linkText(subchildProductName)).size();
        if (existenceOfSubchildProduct != 0) {
            CSLogger.info("Subchild product created : verification passed");
        } else {
            CSLogger.error(
                    "Subchild product creation failed : verification failed");
            softAssertion.fail(
                    "Subchild product creation failed : verification failed");
        }
    }

    /**
     * Verifies that class is present or assigned under properties tab
     * 
     * @param productName String parameter contains name of product
     * @param className String parameter contains name of class
     * @return boolean value
     */
    private int verifyClassesFiled(String productName, String className) {
        goToPimStudioTreeSection();
        WebElement product = getProduct(productName);
        Actions actions = new Actions(browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        actions.doubleClick(product).perform();
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        int inheritedClass = browserDriver
                .findElements(By
                        .xpath("//span[contains(text(),'" + className + "')]"))
                .size();
        return inheritedClass;
    }

    /**
     * Verifies that class is displayed in inherited section of child product
     * 
     * @param childProductName String parameter contains name of child product
     * @param className String parameter contains name of class
     */
    private void verifyClassesFieldOfChildProduct(String childProductName,
            String className) {
        int inheritedClass = verifyClassesFiled(childProductName, className);
        if (inheritedClass != 0) {
            CSLogger.info("Class which is selected in parent i.e " + className
                    + " displayed in 'Inherited' section of child products : "
                    + "teststep passed");
        } else {
            CSLogger.error(
                    "Error in teststep : Class which is selected in parent i.e "
                            + className + " should be displayed in 'Inherited' "
                            + "section of child products : teststep failed");
            softAssertion
                    .fail("Error in teststep : Class which is selected in parent"
                            + " i.e " + className + " should be displayed in "
                            + "'Inherited' section of child products"
                            + " : teststep failed");
        }
    }

    /**
     * Verifies that class is displayed in inherited section of sub child
     * product
     * 
     * @param subChildProductName String parameter containing name of the sub
     *            child product
     * @param className String parameter contains name of class
     */
    private void verifyClassesFieldOfSubchildProduct(String subChildProductName,
            String className) {
        int inheritedClass = verifyClassesFiled(subChildProductName, className);
        if (inheritedClass != 0) {
            CSLogger.info("Class which is selected in parent i.e " + className
                    + " displayed in 'Inherited' section of sub child products : "
                    + "teststep passed");
        } else {
            CSLogger.error(
                    "Error in teststep : Class which is selected in parent i.e "
                            + className + " should be displayed in 'Inherited' "
                            + "section of sub child products : teststep failed");
            softAssertion
                    .fail("Error in teststep : Class which is selected in parent"
                            + " i.e " + className + " should be displayed in "
                            + "'Inherited' section of sub child products"
                            + " : teststep failed");
        }
    }

    /**
     * Edits required attribute
     * 
     * @param textAttributeTabName String parameter containing name of pane
     *            holding text attributes i.e Data
     * @param requiredAttribute String parameter containing name of required
     *            attribute
     * @param requiredAttributeData String parameter containing required
     *            attribute data
     */
    private void editRequiredAttribute(String textAttributeTabName,
            String requiredAttribute, String requiredAttributeData) {
        handleAlertDialog(textAttributeTabName, requiredAttribute);
        insertValueInRequiredField(requiredAttribute, requiredAttributeData);
    }

    /**
     * Checks that In 'Data' pane Required attribute is empty,and also handles
     * required attribute popup.
     * 
     * @param textAttributeTabName String parameter containing name of pane
     *            holding text attributes i.e Data
     * @param requiredAttribute String parameter containing name of required
     *            attribute
     */
    private void handleAlertDialog(String textAttributeTabName,
            String requiredAttribute) {
        String requiredAttributeValue = getAttributeValue(requiredAttribute);
        if (requiredAttributeValue.equals("")) {
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            String alertDialogText = alert.getText();
            if (alertDialogText.contains("An entry is required")) {
                alert.accept();
                CSLogger.info(
                        "When required attribute is empty and when clicked on"
                                + " save button the Alert message pops up: "
                                + textAttributeTabName + "/" + requiredAttribute
                                + " *: An entry is required : teststep passed");
            } else {
                CSLogger.error(
                        "When required attribute is empty and when clicked on"
                                + " save button the Alert message saying  "
                                + textAttributeTabName + "/" + requiredAttribute
                                + " *: An entry is required dosent't   pops up: "
                                + "teststep failed");
                softAssertion
                        .fail("When required attribute is empty and when clicked "
                                + "on save button the Alert message saying  "
                                + textAttributeTabName + "/" + requiredAttribute
                                + " *: An entry is required dosent't pops up: "
                                + "teststep failed");
            }
        } else {
            CSLogger.error("Required field must be empty : teststep failed");
            softAssertion
                    .fail("Required field must be empty : teststep failed");
        }
    }

    /**
     * Inserts data in required attribute
     * 
     * @param requiredAttribute String parameter containing name of required
     *            attribute
     * @param requiredAttributeData String parameter containing required
     *            attribute data
     */
    private void insertValueInRequiredField(String requiredAttribute,
            String requiredAttributeData) {
        enterDataInTextField(requiredAttribute, requiredAttributeData);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This methods enters data in text fields
     * 
     * @param attributeName String type that contains name of the attribute
     * @param attributeData String type that contains value to be entered in
     *            text attribute
     */
    public void enterDataInTextField(String attributeName,
            String attributeData) {
        clkOnTextField(attributeName);
        WebElement textField = getTextFieldElement(attributeName);
        textField.sendKeys(attributeData);
        CSLogger.info("Entered data in text field : " + attributeName
                + "with value " + attributeData);
    }

    /**
     * Edits inheritance attribute
     * 
     * @param subchildProductName
     * @param inheritanceAttribute
     * @param inheritanceAttrValue
     * @param changedInheritanceAttrData
     */
    private void editInheritanceAttribute(String subchildProductName,
            String inheritanceAttribute, String inheritanceAttrValue,
            String changedInheritanceAttrData) {
        goToRightSectionWindow();
        clearDataFromInheritanceField(inheritanceAttribute);
        checkInProduct(subchildProductName);
        verifyInheritedAtrrValue(subchildProductName, inheritanceAttribute,
                inheritanceAttrValue, changedInheritanceAttrData);
        insertValueInInheritanceField(subchildProductName, inheritanceAttribute,
                changedInheritanceAttrData);
        String getModifiedTextAttrColor = getModifiedAttributeTextColor(
                inheritanceAttribute);
        if (getModifiedTextAttrColor.equals("rgba(0, 0, 0, 1)")) {
            CSLogger.info(
                    "Values entered in child attribute displayed in Black color : teststep passed");
        } else {
            CSLogger.error(
                    "Values entered in child attribute didn't appear in Black color : teststep failed");
            softAssertion.fail(
                    "Values entered in child attribute didn't appear in Black color : teststep failed");
        }
    }

    /**
     * Clears data from inheritance attribute
     * 
     * @param inheritanceAttributeName
     */
    private void
            clearDataFromInheritanceField(String inheritanceAttributeName) {
        clkOnTextField(inheritanceAttributeName);
        WebElement inheritanceAttribute = getTextFieldElement(
                inheritanceAttributeName);
        inheritanceAttribute.clear();
    }

    /**
     * This method performs check in operation on child product
     * 
     * @param childProductName String contains name of child product
     */
    public void checkInProduct(String subchildProductName) {
        goToRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//a[@class='GuiEditorSelected'][contains(text(),'"
                                + subchildProductName + "')]")));
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCheckIn(waitForReload);
        CSLogger.info("Checked In sub child product");
    }

    /**
     * Verifies the inherited data from parent and also that data appears in
     * grey color
     * 
     * @param subchildProductName String parameter containing name of the sub
     *            child product
     * @param inheritanceAttribute tring parameter containing name of
     *            inheritance type attribute
     * @param inheritanceAttributeData String parameter containing data of
     *            inheritance attribute
     * @param changedInheritanceAttrData
     */
    private void verifyInheritedAtrrValue(String subchildProductName,
            String inheritanceAttribute, String inheritanceAttributeData,
            String changedInheritanceAttrData) {
        checkOutProduct(subchildProductName);
        CSUtility.tempMethodForThreadSleep(5000);
        String getInheritedAttrTextColor = getAttributeTextColor(
                inheritanceAttribute);
        String inheritedAttributeValue = getAttributeValue(
                inheritanceAttribute);
        if (inheritedAttributeValue.equals(inheritanceAttributeData)
                && getInheritedAttrTextColor.equals("rgba(136, 136, 136, 1)")) {
            CSLogger.info(
                    "Parent attribute value is inherited and is displayed in grey "
                            + ": teststep passed");
        } else {
            CSLogger.error(
                    "Error in teststep Parent attribute value is inherited and "
                            + "is displayed in grey :teststep failed");
            softAssertion.fail("Error in teststep Parent attribute value is "
                    + "inherited and is displayed in grey : teststep failed");
        }
    }

    /**
     * This method performs check out operation on child product
     * 
     * @param childProductName String contains name of child product
     */
    private void checkOutProduct(String subchildProductName) {
        goToRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//a[@class='GuiEditorSelected'][contains(text(),'"
                                + subchildProductName + "')]")));
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCheckOut(waitForReload);
        CSLogger.info("Checked Out sub child product");
    }

    /**
     * Inserts value in inheritance attribute
     * 
     * @param subchildProductName
     * @param inheritanceAttribute
     * @param inheritanceAttributeData
     */
    private void insertValueInInheritanceField(String subchildProductName,
            String inheritanceAttribute, String inheritanceAttributeData) {
        enterDataInTextField(inheritanceAttribute, inheritanceAttributeData);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * Edits language attribute
     * 
     * @param childProduct
     * @param subchildProduct
     * @param language
     * @param languageAttribute
     * @param defaultLanguageData String parameter containing data of default
     *            language selected
     * @param changedLanguageData String parameter containing data of language
     *            that is selected while editing
     */
    private void editLanguageAttribute(String childProduct,
            String subchildProduct, String language, String languageAttribute,
            String defaultLanguageAttrData, String changedLangaugeAttrData) {
        setDefaultLanguageData(subchildProduct, languageAttribute,
                defaultLanguageAttrData);
        setChangedLanguageData(childProduct, subchildProduct, language,
                languageAttribute, defaultLanguageAttrData,
                changedLangaugeAttrData);
        verifyLanguageSpecificData(subchildProduct, defaultLanguageAttrData,
                changedLangaugeAttrData);
    }

    /**
     * Enters default language data in language attribute
     * 
     * @param subchildProduct String parameter containing name of the sub child
     *            product
     * @param languageAttribute String parameter containing name of language
     *            attribute
     * @param defaultLanguageAttrData String parameter containing data of
     *            default language selected
     */
    private void setDefaultLanguageData(String subchildProduct,
            String languageAttribute, String defaultLanguageAttrData) {
        enterDataInTextField(languageAttribute, defaultLanguageAttrData);
        checkInProduct(subchildProduct);
    }

    /**
     * After changing the langauge enters data in language attribute
     * 
     * @param childProduct String parameter containing name of the child product
     * @param subchildProduct String parameter containing name of the sub child
     *            product
     * @param language String parameter containing name of language that will be
     *            selected during runtime
     * @param languageAttribute String parameter containing data of language
     *            attribute
     * @param defaultLanguageData String parameter containing data of default
     *            language selected
     * @param changedLanguageData String parameter containing data of language
     *            that is selected while editing
     */
    private void setChangedLanguageData(String childProduct,
            String subchildProduct, String language, String languageAttribute,
            String defaultLanguageAttrData, String changedLangaugeAttrData) {
        checkOutProduct(subchildProduct);
        changeLanguageOfProduct(language);
        goToRightSectionWindow();
        enterDataInTextField(languageAttribute, changedLangaugeAttrData);
        checkInProduct(subchildProduct);
    }

    /**
     * Performs operation of changing the language of product
     * 
     * @param language String parameter containing name of language that will be
     *            selected during runtime
     */
    public void changeLanguageOfProduct(String language) {
        csGuiToolbarHorizontalInstance.clkOnBtnChangeLanguage(waitForReload);
        selectDataLanguage(waitForReload, language, browserDriver);
    }

    /**
     * Verifies that language specific data is dispalyed
     * 
     * @param subchildProduct String parameter contains name of sub child
     *            product
     * @param defaultLanguageData String parameter containing data of default
     *            language selected
     * @param changedLanguageData String parameter containing data of language
     *            that is selected while editing
     */
    private void verifyLanguageSpecificData(String subchildProduct,
            String defaultLanguageData, String changedLanguageData) {
        waitUntilPageIsClickable(subchildProduct);
        int defaultLanguageDataExists = browserDriver.findElements(By.xpath(
                "//span[contains(text(),'" + changedLanguageData + "')]"))
                .size();
        int changedLanguageDataExists = browserDriver
                .findElements(By.xpath(
                        "//td[contains(text(),'" + defaultLanguageData + "')]"))
                .size();
        if (defaultLanguageDataExists != 0 && changedLanguageDataExists != 0) {
            CSLogger.info(
                    "Data changes are visible according to respective language selected : teststep passed");
        } else {
            CSLogger.error(
                    "Error in step 'Data changes are visible according to "
                            + "respective language selected' : teststep failed");
            softAssertion
                    .fail("Error in step 'Data changes are visible according to "
                            + "respective language selected' : teststep failed");
        }
    }

    /**
     * Waits until page becomes active for operation
     * 
     * @param subchildProduct String parameter contains name of sub child
     *            product
     */
    private void waitUntilPageIsClickable(String subchildProduct) {
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(browserDriver.findElement(By.xpath(
                        "//a[contains(text(),'" + subchildProduct + "')]"))));
    }

    /**
     * This method checks whether the reference type of attribute are editable
     * or not
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param referenceAttributeNameValuePair Attribute's name and value pair
     *            separated with colon and type of single line text attribute
     *            eg.singleLineTextAttr1:ABC
     */
    public void checkReferenceFieldsEditableOrNot(String productFolderName,
            String referenceAttributeNameValuePair, String referenceTabName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        String[] referenceAttributeNameValueData = referenceAttributeNameValuePair
                .split(",");
        String[] referenceAttributeNameValue = null;
        for (int i = 0; i < referenceAttributeNameValueData.length; i++) {
            referenceAttributeNameValue = referenceAttributeNameValueData[i]
                    .split(":");
            handleAssignWindows(referenceAttributeNameValue[0],
                    referenceAttributeNameValue[1], productFolderName,
                    referenceTabName, waitForReload, browserDriver);
        }
    }

    /**
     * This method handles assigning data to different reference type of
     * attribute
     * 
     * @param attributeName String type that contains name of the attribute
     * @param attributeType String of reference attribute for eg.PIM,MAM,CORE
     * @param productFolderName String parameter that contains name of the
     *            product folder
     */
    public void handleAssignWindows(String attributeName, String attributeType,
            String productFolderName, String referenceTabName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        clickOnTheGivenProductTab(referenceTabName, waitForReload,
                browserDriver);
        switch (attributeType) {
        case "PIM":
            referenceToPimProduct(attributeName, productFolderName, false,
                    waitForReload, browserDriver);
            referenceToPimProduct(attributeName, productFolderName, true,
                    waitForReload, browserDriver);
            break;
        case "MAM":
            referenceToMamFile(productFolderName, attributeName, false,
                    waitForReload, browserDriver);
            referenceToMamFile(productFolderName, attributeName, true,
                    waitForReload, browserDriver);
            break;
        case "CORE":
            referenceToCoreUser(productFolderName, attributeName, false,
                    waitForReload, browserDriver);
            referenceToCoreUser(productFolderName, attributeName, true,
                    waitForReload, browserDriver);
            break;
        default:
            CSLogger.error("No reference attribute type found");
            break;
        }
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String name of the tab in the product view.
     * @param waitForReload WebDriverWait object
     */
    public void clickOnTheGivenProductTab(String productTabName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * Performs usecase of assigning product to reference to pim attribute type
     * 
     * @param attributeName Contains name of attribute
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param pressOkay Boolean parameter that contains true and false values
     */
    public void referenceToPimProduct(String attributeName,
            String productFolderName, Boolean pressOkay,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        clkOnCSGuiSelectionListAdd(attributeName, waitForReload, browserDriver);
        selectReferenceToPimProductType(productFolderName, pressOkay,
                attributeName, waitForReload, browserDriver);
    }

    /**
     * This method clicks on plus
     * 
     * @param attributeName Contains name of attribute
     */
    public void clkOnCSGuiSelectionListAdd(String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/div/div[2]/img")));
        Actions actions = new Actions(browserDriver);
        WebElement elementLocator = browserDriver
                .findElement(By.xpath("//table/tbody/tr[contains(@cs_name,'"
                        + attributeName + "')]/td[2]/div/div[2]/img"));
        actions.moveToElement(elementLocator).build().perform();
        elementLocator.click();
    }

    /**
     * This method selects product that needs to be assigned to referenceToPim
     * attribute
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param pressOkay Boolean parameter that contains true and false values
     * @param attributeName Contains name of attribute
     */
    public void selectReferenceToPimProductType(String productFolderName,
            Boolean pressOkay, String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSLogger.info("Opened product folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + productFolderName + "')]")));
        WebElement pimProduct = browserDriver.findElement(By
                .xpath("//span[contains(text(),'" + productFolderName + "')]"));
        Actions doubleClickAction = new Actions(browserDriver);
        doubleClickAction.doubleClick(pimProduct).perform();
        CSLogger.info("Double clicked on product ");
        CSUtility.tempMethodForThreadSleep(5000);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        if (pressOkay) {
            verifyPresenceOfAssignedProduct(productFolderName, attributeName,
                    waitForReload);
        } else {
            verifyAbsenceOfAssignedProduct(attributeName, waitForReload);
        }
    }

    /**
     * Assigns Mam file to Mam attribute
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param attributeName Contains name of attribute
     * @param pressOkay Boolean parameter that contains true and false values
     */
    public void referenceToMamFile(String productFolderName,
            String attributeName, Boolean pressOkay,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        clkOnCSGuiSelectionListAdd(attributeName, waitForReload, browserDriver);
        selectReferenceToMamProductType(productFolderName, pressOkay,
                attributeName, waitForReload, browserDriver);
    }

    /**
     * Checks whether the product is assigned to the attribute or not
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param attributeName Contains name of attribute
     */
    public void verifyPresenceOfAssignedProduct(String productFolderName,
            String attributeName, WebDriverWait waitForReload) {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//tr[contains(@cs_name,'" + attributeName
                            + "')]/td[2]/div/div/div/table/tbody/tr/td[2]/div/a")));
            Assert.assertEquals(productFolderName,
                    browserDriver.findElement(
                            By.xpath("//tr[contains(@cs_name,'" + attributeName
                                    + "')]/td[2]/div/div/div/table/tbody/tr/td"
                                    + "[2]/div/a"))
                            .getText());
            CSLogger.info("Product assigned properly after clicking ok on Data "
                    + "Selection Dialog window");
        } catch (Exception e) {
            CSLogger.error(
                    "Error while assigning product to Reference To Pim after "
                            + "clicking ok of Data Selection Dialog window",
                    e);
            Assert.fail(
                    "Error while assigning product to Reference To Pim after "
                            + "clicking ok of Data Selection Dialog window");
        }
    }

    /**
     * Clicks on plus of Mam attribute
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param pressOkay Boolean parameter that contains true and false values
     * @param attributeName Contains name of attribute
     */
    public void selectReferenceToMamProductType(String productFolderName,
            Boolean pressOkay, String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSLogger.info("Opened File folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonFilesFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillFileFoldersleftFrames(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText("Demo")));
        browserDriver.findElement(By.linkText("Demo")).click();
        selectionDialogWindowInstance
                .clkBtnCSGuiToolbarListFileSubFolders(waitForReload);
        waitForReload.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("(//img[@class='CSMamFileThumb_DummyImage'])[1]")));
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[@class='CSMamFileThumb_Title'])[1]")));
        String assignedImageName = browserDriver
                .findElement(
                        By.xpath("(//div[@class='CSMamFileThumb_Title'])[1]"))
                .getText();
        WebElement selectFirstImage = browserDriver.findElement(
                By.xpath("(//img[@class='CSMamFileThumb_DummyImage'])[1]"));
        selectFirstImage.click();
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        if (pressOkay) {
            verifyPresenceOfAssignedMamFile(assignedImageName, attributeName,
                    waitForReload);
        } else {
            verifyAbsenceOfAssignedMamFile(attributeName, waitForReload);
        }
    }

    /**
     * Checks whether the Mam file is assigned to the attribute after clicking
     * on ok.
     * 
     * @param assignedFirstImage Image name that was assigned
     * @param attributeName Contains name of attribute
     */
    public void verifyPresenceOfAssignedMamFile(String assignedFirstImage,
            String attributeName, WebDriverWait waitForReload) {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//td[@class='CSGuiThumbListData CSGuiThumbListData"
                            + "Single']/div/a")));
            Assert.assertEquals(assignedFirstImage,
                    browserDriver
                            .findElement(By.xpath(
                                    "//tr[contains(@cs_name,'" + attributeName
                                            + "')]/td[2]/div/div/div/table/tbody/tr/"
                                            + "td[2]/div/a"))
                            .getText());
            CSLogger.info("Selected file  assigned to reference attribute "
                    + "successfully.");
        } catch (Exception e) {
            CSLogger.error(
                    "Error while assigning file to Reference To Mam after "
                            + "clicking ok of Data Selection Dialog window",
                    e);
            Assert.fail("Error while assigning file to Reference To Mam after "
                    + "clicking ok of Data Selection Dialog window");
        }
    }

    /**
     * Checks whether the Mam file is not assigned to the attribute after
     * clicking on cancel
     * 
     * @param attributeName Contains name of attribute
     */
    public void verifyAbsenceOfAssignedMamFile(String attributeName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        int mamFileAssigned = browserDriver.findElements(
                By.xpath("//tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td[2]"
                        + "/div/a"))
                .size();
        if (mamFileAssigned != 0) {
            CSLogger.error(
                    "Error while assigning File by clicking on cancel of File "
                            + "Selection Dialog window test case failed");
        } else {
            CSLogger.info("File was not assigned by clicking on cancel of File "
                    + "Selection Dialog window test case passed");
        }
    }

    /**
     * Assigns a reference of core user
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param attributeName Contains name of attribute
     * @param pressOkay Boolean parameter that contains true and false values
     */
    public void referenceToCoreUser(String productFolderName,
            String attributeName, Boolean pressOkay,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        clkOnCSGuiSelectionListAdd(attributeName, waitForReload, browserDriver);
        selectReferenceToCoreUserType(productFolderName, pressOkay,
                attributeName, waitForReload, browserDriver);
    }

    /**
     * Selects reference of core user type
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param pressOkay Boolean parameter that contains true and false values
     * @param attributeName Contains name of attribute
     */
    public void selectReferenceToCoreUserType(String productFolderName,
            Boolean pressOkay, String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSLogger.info("Opened User  folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonUserFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillUserFoldersleftFrames(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText("Users")));
        browserDriver.findElement(By.linkText("Users")).click();
        CSLogger.info("Clicked on user tree");
        selectionDialogWindowInstance
                .clkBtnCSGuiToolbarListUserSubFolders(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("(//tr[@class='odd']/td[2])[1]")));
        String assignedUserName = browserDriver
                .findElement(By.xpath("(//tr[@class='odd']/td[2])[1]"))
                .getText();
        browserDriver.findElement(By.xpath("(//tr[@class='odd']/td[2])[1]"))
                .click();
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(3000);
        if (pressOkay) {
            verifyPresenceOfAssignedCoreUser(assignedUserName, attributeName,
                    waitForReload);
        } else {
            verifyAbsenceOfAssignedCoreUser(attributeName, waitForReload);
        }
    }

    /**
     * Checks whether the User is assigned to the attribute or not after
     * clicking on ok
     * 
     * @param assignedUserName Name of user that needs to be assigned
     * @param attributeName Contains name of attribute
     */
    public void verifyPresenceOfAssignedCoreUser(String assignedUserName,
            String attributeName, WebDriverWait waitForReload) {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("(//a[@class='CSGuiThumbListDataText']/span)[1]")));
            String expectedUserName = browserDriver
                    .findElement(By
                            .xpath("(//a[@class='CSGuiThumbListDataText']/span)[1]"))
                    .getText();
            String[] spiltUserName = expectedUserName.split(":");
            Assert.assertEquals(assignedUserName, spiltUserName[1].trim());
            CSLogger.info("Selected user  assigned to Reference To Core User "
                    + "successfully.");
        } catch (Exception e) {
            CSLogger.error(
                    "Error while assigning user to Reference To Core User after"
                            + " clicking ok of Data Selection Dialog window",
                    e);
            Assert.fail(
                    "Error while assigning user to Reference To Core User after"
                            + " clicking ok of Data Selection Dialog window");
        }
    }

    /**
     * Verifies that after clicking on cancel button the product is not assigned
     * to that attribute
     * 
     * @param attributeName Contains name of attribute
     */
    public void verifyAbsenceOfAssignedProduct(String attributeName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        int pimProductAssigned = browserDriver.findElements(
                By.xpath("//tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td[2]"
                        + "/div/a"))
                .size();
        if (pimProductAssigned != 0) {
            CSLogger.error(
                    "Error while assigning Product by clicking on cancel of "
                            + "Data Selection Dialog window test case failed");
            Assert.fail(
                    "Error while assigning Product by clicking on cancel of "
                            + "Data Selection Dialog window test case failed");
        } else {
            CSLogger.info(
                    "Product was not assigned by clicking on cancel of Data"
                            + " Selection Dialog window test case passed");
        }
    }

    /**
     * Checks whether the User is not assigned to the attribute or not after
     * clicking on cancel button
     * 
     * @param attributeName Contains name of attribute
     */
    public void verifyAbsenceOfAssignedCoreUser(String attributeName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        int coreUserExist = browserDriver
                .findElements(
                        By.xpath("//span[contains(text(),'Username: admin')]"))
                .size();
        if (coreUserExist != 0) {
            CSLogger.error(
                    "Error while assigning User by clicking on cancel of Data "
                            + "Selection Dialog window test case failed");
            Assert.fail(
                    "Error while assigning User by clicking on cancel of Data "
                            + "Selection Dialog window test case failed");
        } else {
            CSLogger.info("User was not assigned by clicking on cancel of Data "
                    + "Selection Dialog window test case passed");
        }
    }

    /**
     * Selects the default language.
     * 
     * @param defaultLanguage String object contains default language.
     */
    private void setDefaultLanguage(String defaultLanguage) {
        changeLanguageOfProduct(defaultLanguage);
    }

    /**
     * Selects the language passed as parameter
     * 
     * @param waitForReload WebDriverWait object
     * @param language String containing name of language
     * @param browserDriverInstance WebDriver instance
     */
    public void selectDataLanguage(WebDriverWait waitForReload, String language,
            WebDriver browserDriverInstance) {
        csPopupDiv.hoverOnCsGuiPopupMenu(browserDriverInstance, waitForReload,
                csPopupDiv.getCsGuiPopupDataLanguage());
        traverseToPopupSubFrame();
        WebElement dataLanguage = browserDriverInstance.findElement(
                By.xpath("//td[@title='" + language + "']/parent::tr"));
        dataLanguage.click();
        CSLogger.info("Clicked on data language " + language);
    }

    /**
     * Traverses the frames to select the data language.
     */
    public void traverseToPopupSubFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(csPopupDiv.getCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
        CSLogger.info("Traversed till data language frames");
    }

    /**
     * This is a data provider method.
     * 
     */
    @DataProvider(name = "createSubchildProductTestData")
    public Object[][] getCreateSubchildProductData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                createSubchildProductSheetName);
    }

    /**
     * This is a data provider method.
     * 
     */
    @DataProvider(name = "EditSubchildProductTestData")
    public Object[][] geteditSubchildProductData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                editSubchildProductSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        softAssertion = new SoftAssert();
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
    }

}
