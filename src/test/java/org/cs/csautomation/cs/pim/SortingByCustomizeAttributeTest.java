/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimProductFilterPage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to handle the test cases related to the
 * Short By Standard Attributes use cases.
 * 
 * @author CSAutomation Team
 *
 */
public class SortingByCustomizeAttributeTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeader;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private PimProductFilterPage      pimProductFilter;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private ModalDialogPopupWindow    modalDialogPopupWindowInstance;
    private IProductPopup             productPopup;
    private IAttributePopup           attributePopup;
    private IClassPopup               classPopup;
    private CSPopupDivPim             csPopupDiv;
    private String                    ascending                 = "Ascending";
    private String                    descending                = "Descending";
    private String                    sortbyCustomizeddataSheet = "SortbyCustomizedAttr";

    /**
     * This method create the data required to perform sort by test case.
     * 
     * @param attributeFolderName String object containing name of attribute
     *            folder
     * @param textAttributeName String text type attribute name.
     * @param className String class name.
     * @param parentProductName String parent product name.
     * @param childProductName String child product name.
     * @param searchOption String search option to be assign to the attribute.
     * @param searchableTextData String text data use for search operation.
     * @param operatorOptions String option to be select while performing search
     *            operation
     * @param productTabName String name of product tab.
     */
    @Test(dataProvider = "SortbyTestData")
    public void testSortByCustomizedAttr(String attributeFolderName,
            String textAttributeName, String className,
            String parentProductName, String childProductName,
            String searchOption, String searchableTextData,
            String operatorOptions, String productTabName) {
        try {
            String attributeID = createSortbyCustomizedData(attributeFolderName,
                    textAttributeName, className, parentProductName,
                    childProductName, searchOption, searchableTextData,
                    productTabName);
            sortByAscending(textAttributeName, attributeID, operatorOptions,
                    searchableTextData);
            sortByDescending(textAttributeName, attributeID, operatorOptions,
                    searchableTextData);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testSortByCustomizedAttr",
                    e);
            Assert.fail(
                    "Automation error in test method : testSortByCustomizedAttr",
                    e);
        }
    }

    /**
     * This method create the data required to perform sort by test case.
     * 
     * @param attributeFolderName String object containing name of attribute
     *            folder
     * @param textAttributeName String text type attribute name.
     * @param className String class name.
     * @param parentProductName String parent product name.
     * @param childProductName String child product name.
     * @param searchOption String search option to be assign to the attribute.
     * @param searchableTextData String text data use for search operation.
     * @param productTabName String name of product tab.
     * @return attributeID String Attribute ID.
     */
    public String createSortbyCustomizedData(String attributeFolderName,
            String textAttributeName, String className,
            String parentProductName, String childProductName,
            String searchOption, String searchableTextData,
            String productTabName) {
        switchToPimAndExpandSettingsTree(waitForReload);
        createAttributeFolder(attributeFolderName);
        String attributeID = createSearchableAttribute(attributeFolderName,
                textAttributeName, searchOption);
        createClass(className);
        assignAttrFolderToClass(className, attributeFolderName);
        createParentProduct(waitForReload, pimStudioProductsNode,
                parentProductName);
        createChildProduct(parentProductName, childProductName);
        assignClassToParentProduct(parentProductName, className);
        assignValuesToChildProductAttributes(textAttributeName,
                searchableTextData, waitForReload, parentProductName,
                childProductName, productTabName);
        return attributeID;
    }

    /**
     * This method creates a attribute folder of name passed as argument
     * 
     * @param attributeFolderName String object containing name of attribute
     *            folder
     */
    private void createAttributeFolder(String attributeFolderName) {
        try {
            goToPimStudioTreeSection();
            CSUtility
                    .rightClickTreeNode(waitForReload,
                            pimStudioSettingsNodeInstance
                                    .getBtnPimSettingsAttributeNode(),
                            browserDriver);
            CSLogger.info("Right clicked on Attribute Node");
            attributePopup.selectPopupDivMenu(waitForReload,
                    attributePopup.getCsGuiPopupMenuNewFolder(), browserDriver);
            CSLogger.info("Selected New Folder Option");
            attributePopup.enterValueInDialogue(waitForReload,
                    attributeFolderName);
            attributePopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            goToPimStudioTreeSection();
            verifiesCreationOfFolder(attributeFolderName);
            CSLogger.info(
                    " Created attribute folder named : " + attributeFolderName);
        } catch (Exception e) {
            CSLogger.error("Automation error", e);
        }
    }

    /**
     * Creates a text attribute in given attribute folder and sets the search
     * able attribute field with option passed as argument
     * 
     * @param attributeFolderName String object containing name of attribute
     *            folder
     * @param txtAttrName String object containing text attribute name
     * @param searchOption String object containing search option i.e Simple
     *            Search
     * @return attributeId String Attribute ID
     */
    private String createSearchableAttribute(String attributeFolderName,
            String txtAttrName, String searchOption) {
        createTextAttribute(attributeFolderName, txtAttrName);
        goToPimStudioTreeSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(txtAttrName)));
        WebElement textAttribute = browserDriver
                .findElement(By.linkText(txtAttrName));
        textAttribute.click();
        String attributeId = getAttributeId(txtAttrName);
        CSLogger.info("Clicked on text attribute " + txtAttrName);
        goToRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsSearchable());
        csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsSearchable(),
                searchOption);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Text attribute with searchable option as " + searchOption
                + " created");
        return attributeId;
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method returns the ID of the attribute passed as argument
     * 
     * @param attributeName String containing name of child product
     * @return attribute Id
     */
    private String getAttributeId(String attributeName) {
        goToPimStudioTreeSection();
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
     * This method creates a single line text attribute
     * 
     * @param attributeFolderName String object containing name of attribute
     *            folder
     * @param txtAttrName String object containing text attribute name
     */
    private void createTextAttribute(String attributeFolderName,
            String txtAttrName) {
        goToPimStudioTreeSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(attributeFolderName)));
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(attributeFolderName));
        CSUtility.rightClickTreeNode(waitForReload, createdAttributeFolder,
                browserDriver);
        CSLogger.info("Right Clicked On Newly Created Attribute Folder");
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSLogger.info("Clicked on Create New submenu");
        attributePopup.enterValueInUserInputDialogue(waitForReload,
                txtAttrName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSLogger.info("Text attribute " + txtAttrName + " created");
    }

    /**
     * This method creates a given class.
     * 
     * @param className String object containing name of class
     */
    private void createClass(String className) {
        try {
            goToPimStudioTreeSection();
            CSUtility.scrollUpOrDownToElement(pimStudioSettingsNodeInstance
                    .getBtnPimSettingsClassesNode(), browserDriver);
            CSUtility
                    .rightClickTreeNode(waitForReload,
                            pimStudioSettingsNodeInstance
                                    .getBtnPimSettingsClassesNode(),
                            browserDriver);
            CSLogger.info("Right clicked on Classes Node");
            attributePopup.selectPopupDivMenu(waitForReload,
                    classPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
            CSLogger.info("Selected Create New Option");
            attributePopup.enterValueInDialogue(waitForReload, className);
            attributePopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            goToPimStudioTreeSection();
            verifiesCreationOfFolder(className);
            CSLogger.info(" Created class named : " + className);
        } catch (Exception e) {
            CSLogger.error("Automation error", e);
        }
    }

    /**
     * Assigns attribute folder to the given class
     * 
     * @param className String object containing name of class
     * @param attributeFolderName String object containing name of attribute
     *            folder
     */
    private void assignAttrFolderToClass(String className,
            String attributeFolderName) {
        goToPimStudioTreeSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(className)));
        WebElement pimStudioClass = browserDriver
                .findElement(By.linkText(className));
        pimStudioClass.click();
        goToRightSectionWindow();
        csGuiDialogContentIdInstance.clkOnBtnToAddAttrToClass();
        CSLogger.info("Opened product folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogLeftSection(
                waitForReload, browserDriver);
        selectionDialogWindowInstance.getDrpDwnAttributes().click();
        CSLogger.info(
                "Clicked on Attributes tree node from Data Selection Dialog Window");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + attributeFolderName + "')]")));
        WebElement attributeFolder = browserDriver.findElement(By.xpath(
                "//span[contains(text(),'" + attributeFolderName + "')]"));
        attributeFolder.click();
        CSLogger.info("Clicked on attribute folder");
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogCenterSection(waitForReload,
                        browserDriver);
        csGuiDialogContentIdInstance
                .clkOnAddAllAttrValuesToClass(waitForReload);
        modalDialogPopupWindowInstance
                .handleMessageModalDialogWindow(waitForReload, true);
        CSUtility.tempMethodForThreadSleep(3000);
        classPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='CSGuiToolbarHorizontal']")));
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Attribute folder " + attributeFolderName
                + "assigned to class: " + className + "successfully");
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandSettingsTree(WebDriverWait waitForReload) {
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload WebDriverWait Object
     * @param pimStudioProductsNode PimStudioProductsNode Object
     * @param parentProductName String Object containing the value for parent
     *            product Name.
     */
    private void createParentProduct(WebDriverWait waitForReload,
            PimStudioProductsNodePage pimStudioProductsNode,
            String parentProductName) {
        goToProductsNode();
        createProduct(parentProductName,
                pimStudioProductsNode.getBtnPimProductsNode(),
                csPopupDiv.getCsGuiPopupMenuCreateNew());
        CSLogger.info("Created Parent product");
    }

    /**
     * Scrolls to pim products node
     */
    private void goToProductsNode() {
        goToPimStudioTreeSection();
        CSUtility.scrollUpOrDownToElement(
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
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
        try {
            CSUtility.rightClickTreeNode(waitForReload, nodeElement,
                    browserDriver);
            csPopupDiv.selectPopupDivMenu(waitForReload, popupMenuOption,
                    browserDriver);
            csPopupDiv.enterValueInDialogue(waitForReload, productName);
            CSLogger.info(
                    "Entered value " + productName + " in the textfield.");
            csPopupDiv.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            goToPimStudioTreeSection();
            verifiesCreationOfFolder(productName);
            goToPimStudioTreeSection();
        } catch (Exception e) {
            CSLogger.error("Automation error", e);
        }
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
        String childName[] = childProductName.split(",");
        for (String name : childName) {
            WebElement element = pimStudioProductsNode
                    .expandNodesByProductName(waitForReload, parentProductName);
            createProduct(name, element,
                    csPopupDiv.getCsGuiPopupMenuNewChild());
            CSLogger.info("Created new child product");
        }
    }

    public void verifiesCreationOfFolder(String userInputFolderName) {
        List<WebElement> folder = browserDriver
                .findElements(By.linkText(userInputFolderName));
        if (!folder.isEmpty()) {
            CSLogger.info("Folder is created");
        } else {
            Assert.fail("Folder not found");
        }
        CSUtility.switchToDefaultFrame(browserDriver);
    }

    /**
     * This method initiate the process of assigning values to attributes of
     * child products and also checks In the child product .
     * 
     * @param productTextTypeAttrName String name of Single line type of
     *            attribute
     * @param productTextTypeAttrValue String value for single line type of
     *            attribute
     * @param waitForReload WebDriverWait Object
     * @param productFolderName String contains name of product folder
     * @param childProductName String contains name of child product
     * @param productTabName String contains name of product tab
     */
    private void assignValuesToChildProductAttributes(
            String productTextTypeAttrName, String productTextTypeAttrValue,
            WebDriverWait waitForReload, String productFolderName,
            String childProductName, String productTabName) {
        String childName[] = childProductName.split(",");
        for (String name : childName) {
            goToPimStudioTreeSection();
            clkOnParentProductFolder(productFolderName, name);
            goToRightSectionWindow();
            clickOnTheGivenProductTab(productTabName, waitForReload);
            CSUtility.tempMethodForThreadSleep(1000);
            addTextInTextField(productTextTypeAttrName, waitForReload,
                    productTextTypeAttrValue, name);
        }
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String name of the tab in the product view.
     * @param waitForReload WebDriverWait Object
     */
    private void clickOnTheGivenProductTab(String productTabName,
            WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * This method add text in text field of child products.
     * 
     * @param productTextTypeAttrName String Text type attribute name.
     * @param waitForReload WebDriverWait.
     * @param textAttrValue String Text to be enter at field.
     * @param childProductName String name of the child product.
     */
    private void addTextInTextField(String productTextTypeAttrName,
            WebDriverWait waitForReload, String textAttrValue,
            String childProductName) {
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table/tbody/tr[contains(@cs_name,'"
                        + productTextTypeAttrName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]")));
        Actions actions = new Actions(browserDriver);
        WebElement elementLocator = browserDriver.findElement(By.xpath(
                "//table/tbody/tr[contains(@cs_name,'" + productTextTypeAttrName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]"));
        actions.doubleClick(elementLocator).perform();
        browserDriver
                .findElement(By.xpath("//table/tbody/tr[contains(@cs_name,'"
                        + productTextTypeAttrName + "')]/td[2]/input"
                        + "[contains(@id,'PdmarticleCS_ItemAttribute')]"))
                .sendKeys(textAttrValue);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info(
                "Inserted " + textAttrValue + " as value in the text field.");
    }

    /**
     * This method clicks on Parent product folder
     * 
     * @param productFolderName String Object containing product folder name.
     */
    public void clkOnParentProductFolder(String productFolderName,
            String childProductName) {
        try {
            CSUtility.tempMethodForThreadSleep(3000);
            getCreatedProductFolder(productFolderName).click();
            getCreatedProductFolder(childProductName).click();
            CSLogger.info("Clicked on parent product folder");
        } catch (Exception e) {
            CSLogger.error("Parent folder not found", e);
            Assert.fail("Parent folder not found");
        }
    }

    /**
     * Assigns the given class to the parent product folder
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param className String object containing name of the class
     */
    public void assignClassToParentProduct(String parentProductName,
            String className) {
        doubleClkOnParentProduct(parentProductName);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        selectClassFromDataSelectionDialogWindow(className);
    }

    /**
     * selects the class of given classname that will be assigned to the parent
     * product folder
     * 
     * @param className String object containing name of the class
     */
    public void selectClassFromDataSelectionDialogWindow(String className) {
        try {
            selectionDialogWindowInstance.clkBtnControlPaneButtonUserFolder(
                    waitForReload, browserDriver);
            TraverseSelectionDialogFrames
                    .traverseToDataSelectionDialogLeftSection(waitForReload,
                            browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(By.linkText(className)));
            Actions action = new Actions(browserDriver);
            action.doubleClick(
                    browserDriver.findElement(By.linkText(className))).build()
                    .perform();
            productPopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            goToRightSectionWindow();
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            CSLogger.info("Class : " + className + "assigned to product folder "
                    + "successfully");
        } catch (Exception e) {
            CSLogger.error("Class not found", e);
            Assert.fail("Class not found");
        }
    }

    /**
     * Performs operation of double clicking on parent product
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     */
    public void doubleClkOnParentProduct(String parentProductName) {
        try {
            WebElement parentProduct = getCreatedProductFolder(
                    parentProductName);
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(By.linkText(parentProductName)));
            Actions action = new Actions(browserDriver);
            action.doubleClick(parentProduct).build().perform();
        } catch (Exception e) {
            CSLogger.error("Parent product folder not found", e);
            Assert.fail("Parent product folder not found");
        }
    }

    /**
     * Returns the created product folder webElement
     * 
     * @param productFolderName String containing name of parent product folder
     * @return WebElement CreatedProductFolder
     */
    public WebElement getCreatedProductFolder(String productFolderName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productFolderName)));
        return browserDriver.findElement(By.linkText(productFolderName));
    }

    /**
     * This method perform shorting on Customize Attribute in Ascending order.
     * 
     * @param textAttributeName String attribute name.
     * @param attributeID String attribute ID.
     * @param operatorOptions String operator option.
     * @param searchableTextData String text to be search.
     */
    public void sortByAscending(String textAttributeName, String attributeID,
            String operatorOptions, String searchableTextData) {
        try {
            Actions action = new Actions(browserDriver);
            csPortalHeader.clkBtnProducts(waitForReload);
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance
                            .getBtnPimSettingsAttributeNode());
            action.doubleClick(pimStudioSettingsNodeInstance
                    .getBtnPimSettingsAttributeNode()).perform();
            CSUtility.tempMethodForThreadSleep(1000);
            action.doubleClick(pimStudioProductsNode.getBtnPimProductsNode())
                    .perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            setSortByConfig(waitForReload, attributeID, textAttributeName,
                    operatorOptions, ascending, searchableTextData);
            verifySortByTestCase(ascending);
        } catch (Exception e) {
            Assert.fail("Automation error", e);
        }
    }

    /**
     * This method perform shorting on Customize Attribute in Descending order.
     * 
     * @param textAttributeName String attribute name.
     * @param attributeID String attribute ID.
     * @param operatorOptions String operator option.
     * @param searchableTextData String text to be search.
     */
    public void sortByDescending(String textAttributeName, String attributeID,
            String operatorOptions, String searchableTextData) {
        try {
            Actions action = new Actions(browserDriver);
            csPortalHeader.clkBtnProducts(waitForReload);
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            action.doubleClick(pimStudioProductsNode.getBtnPimProductsNode())
                    .perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            setSortByConfig(waitForReload, attributeID, textAttributeName,
                    operatorOptions, descending, searchableTextData);
            verifySortByTestCase(descending);
        } catch (Exception e) {
            Assert.fail("Automation error", e);
        }
    }

    /**
     * This method set the parameter for short by using ID.
     *
     * @param waitForReload.
     * @param short by string.
     */
    public void setSortByConfig(WebDriverWait waitForReload, String attributeID,
            String textAttributeName, String operatorOptions, String shortBy,
            String searchableTextData) {
        String textAttribute = textAttributeName + " " + "(" + attributeID
                + ")";
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getBtnCloseAdvanceSearch());
        pimProductFilter.getBtnCloseAdvanceSearch().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddAttribute());
        Select selAttribute = new Select(pimProductFilter.getSddAttribute());
        selAttribute.selectByVisibleText(textAttribute);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddOperator());
        Select selOperator = new Select(pimProductFilter.getSddOperator());
        selOperator.selectByVisibleText(operatorOptions);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getTxtValue());
        pimProductFilter.getTxtValue().sendKeys(searchableTextData);
        pimProductFilter.getBtnPlusOnShortBy().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddShortByAttributes());
        Select selShortByAttributes = new Select(
                pimProductFilter.getSddShortByAttributes());
        selShortByAttributes.selectByVisibleText("ID");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddValuesAseDec());
        Select selValuesAseDec = new Select(
                pimProductFilter.getSddValuesAseDec());
        if (shortBy.equalsIgnoreCase(ascending)) {
            selValuesAseDec.selectByVisibleText("Ascending");
        } else {
            selValuesAseDec.selectByVisibleText("Descending");
        }
        pimProductFilter.getBtnSearch().click();
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method check the short by Test case. using the ID of product.
     * 
     * @param shortBy String short by ascending or descending.
     */
    public void verifySortByTestCase(String shortBy) {
        int currentId;
        int lblCount = getProductsCount();
        int idHeaderIndex = CSUtility.getListHeaderIndex("ID", browserDriver);
        if (lblCount > 1) {
            ArrayList<Integer> recordIds = new ArrayList<>();
            for (int row = 3; row <= lblCount; row++) {
                currentId = Integer.parseInt(browserDriver
                        .findElement(By
                                .xpath("//table[@class='hidewrap CSAdminList']"
                                        + "/tbody[1]/tr[" + row + "]/td["
                                        + idHeaderIndex + "]"))
                        .getText());
                recordIds.add(currentId);
            }
            if (shortBy.equalsIgnoreCase(ascending)) {
                chkSortByAscending(lblCount, recordIds);
            } else {
                chkSortByDescending(lblCount, recordIds);
            }
            CSLogger.info(
                    "Customized Attribute Short By " + shortBy + " verified.");
        } else {
            CSLogger.info("Products cannot be short.");
        }
    }

    /**
     * This method check the short by Ascending. using the ID of product.
     * 
     * @param lblcount Integer number of rows.
     */
    public void chkSortByAscending(int lblCount, ArrayList<Integer> recordIds) {
        for (int index = 0; index < recordIds.size();) {
            if (recordIds.get(index) > recordIds.get(index++)) {
                Assert.fail("Short by Ascending is not successful");
            }
        }
    }

    /**
     * This method check the short by Descending. using the ID of product.
     * 
     * @param lblcount Integer number of rows.
     */
    public void chkSortByDescending(int lblCount,
            ArrayList<Integer> recordIds) {
        for (int index = 0; index < recordIds.size();) {
            if (recordIds.get(index) < recordIds.get(index++)) {
                Assert.fail("Short by Ascending is not successful");
            }
        }
    }

    /**
     * This method count the product present. It search the product rows.
     *
     * @return count of the product present.
     */
    public int getProductsCount() {
        WebElement table = browserDriver
                .findElement(By.id("CSGuiListbuilderTable"));
        int productCount = table.findElements(By.tagName("tr")).size();
        return productCount;
    }

    /**
     * This is a data provider method.
     * 
     * @return Array
     */
    @DataProvider(name = "SortbyTestData")
    public Object[][] getSwitchMergeToDistributePageContentData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                sortbyCustomizeddataSheet);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod()
    public void initializeResource() {
        waitForReload = new WebDriverWait(browserDriver, 30);
        csPortalHeader = new CSPortalHeader(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        classPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
        pimProductFilter = new PimProductFilterPage(browserDriver);
    }
}
