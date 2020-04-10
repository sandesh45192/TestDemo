/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * 
 * This class contains the test methods to create a attribute folder along with
 * text attribute which then is assigned to a newly created class and is
 * assigned to a product folder which is created with two child products during
 * runtime and performs simple search operation with searchable text and
 * non-searchable text and also verifies the result
 * 
 * 
 * @author CSAutomation Team
 *
 */
public class VerifySimpleSearchTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim             csPopupDiv;
    private SoftAssert                softAssertion;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private IProductPopup             productPopup;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private IAttributePopup           attributePopup;
    private IClassPopup               classPopup;
    private ModalDialogPopupWindow    modalDialogPopupWindowInstance;

    private String                    dataSheetName = "VerifySimpleSearch";

    /**
     * This test method performs simple search operation and also verifies the
     * result
     * 
     * @param attributeFolderName
     *            String object containing attribute folder name.
     * @param textAttributeName
     *            String object containing text attribute name.
     * @param className
     *            String object containing name of the class
     * @param parentProductName
     *            String object containing name of parent product folder
     * @param firstChildProductName
     *            String object containing name of first child product
     * @param secondChildProductName
     *            String object containing name of second child product
     * @param searchOption
     *            String object containing search option i.e Simple Search
     * @param searchableTextData
     *            String object containing text data that will be added in some
     *            child product and searched while performing simple search
     * @param nonSearchableTextData
     *            String object containing text data that is not present in any
     *            child product
     * @param productTabName
     *            String object containing name of the product tab
     */
    @Test(priority = 1, dataProvider = "simpleSearchTestData")
    public void testVerifySimpleSearch(String attributeFolderName,
            String textAttributeName, String className,
            String parentProductName, String firstChildProductName,
            String secondChildProductName, String searchOption,
            String searchableTextData, String nonSearchableTextData,
            String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandSettingsTree(waitForReload);
            createAttributeFolder(attributeFolderName);
            createSearchableAttribute(attributeFolderName, textAttributeName,
                    searchOption);
            createClass(className);
            assignAttrFolderToClass(className, attributeFolderName);
            createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                    parentProductName);
            createChildProduct(parentProductName, firstChildProductName);
            createChildProduct(parentProductName, secondChildProductName);
            assignClassToParentProduct(parentProductName, className);
            String childProductId = assignValuesToChildProductAttributes(
                    textAttributeName, searchableTextData, waitForReload,
                    parentProductName, firstChildProductName, productTabName);
            performSimpleSearchOperation(parentProductName, searchableTextData,
                    nonSearchableTextData, childProductId, true);
            performSimpleSearchOperation(parentProductName, searchableTextData,
                    nonSearchableTextData, childProductId, false);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testVerifySimpleSearch",
                    e);
            Assert.fail(
                    "Automation error in test method : testVerifySimpleSearch",
                    e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void switchToPimAndExpandSettingsTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * This method creates a attribute folder of name passed as argument
     * 
     * @param attributeFolderName
     *            String object containing name of attribute folder
     */
    private void createAttributeFolder(String attributeFolderName) {
        goToPimStudioTreeSection();
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
        CSLogger.info(
                " Created attribute folder named : " + attributeFolderName);
    }

    /**
     * Creates a text attribute in given attribute folder and sets the
     * searchable attribute field with option passed as argument
     * 
     * @param attributeFolderName
     *            String object containing name of attribute folder
     * @param txtAttrName
     *            String object containing text attribute name
     * @param searchOption
     *            String object containing search option i.e Simple Search
     */
    private void createSearchableAttribute(String attributeFolderName,
            String txtAttrName, String searchOption) {
        createTextAttribute(attributeFolderName, txtAttrName);
        goToPimStudioTreeSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(txtAttrName)));
        WebElement textAttribute = browserDriver
                .findElement(By.linkText(txtAttrName));
        textAttribute.click();
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
    }

    /**
     * This method creates a single line text attribute
     * 
     * @param attributeFolderName
     *            String object containing name of attribute folder
     * @param txtAttrName
     *            String object containing text attribute name
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
     * Switches till spiltAreaMain frame
     */
    private void goToRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method creates a given class.
     * 
     * @param className
     *            String object containing name of class
     */
    private void createClass(String className) {
        goToPimStudioTreeSection();
        CSUtility.scrollUpOrDownToElement(
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode(),
                browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode(),
                browserDriver);
        CSLogger.info("Right clicked on Classes Node");
        attributePopup.selectPopupDivMenu(waitForReload,
                classPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSLogger.info("Selected Create New Option");
        attributePopup.enterValueInDialogue(waitForReload, className);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSLogger.info(" Created class named : " + className);
    }

    /**
     * Assigns attribute folder to the given class
     * 
     * @param className
     *            String object containing name of class
     * @param attributeFolderName
     *            String object containing name of attribute folder
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
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Attribute folder " + attributeFolderName
                + "assigned to class: " + className + "successfully");
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     * @param pimStudioProductsNode
     *            PimStudioProductsNode Object
     * @param parentProductName
     *            String Object containing the value for parent product Name.
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
     * This method creates the child product with the supplied name and the
     * supplied parent product name.
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param childProductName
     *            String Object containing the child product name.
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void createChildProduct(String parentProductName,
            String childProductName) {
        WebElement element = pimStudioProductsNodeInstance
                .expandNodesByProductName(waitForReload, parentProductName);
        createProduct(childProductName, element,
                csPopupDiv.getCsGuiPopupMenuNewChild());
        CSLogger.info("Created new child product");
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName
     *            String object containing name of product it can be child
     *            product or parent product
     * @param nodeElement
     *            WebElement of either products node or parent product
     * @param popupMenuOption
     *            WebElement containing menu option.
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
     * Assigns the given class to the parent product folder
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param className
     *            String object containing name of the class
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
     * Performs operation of double clicking on parent product
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
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
     * @param productFolderName
     *            String containing name of parent product folder
     * @return WebElement CreatedProductFolder
     */
    public WebElement getCreatedProductFolder(String productFolderName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productFolderName)));
        return browserDriver.findElement(By.linkText(productFolderName));
    }

    /**
     * selects the class of given classname that will be assigned to the parent
     * product folder
     * 
     * @param className
     *            String object containing name of the class
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
     * This method initiate the process of assigning values to attributes of
     * child products and also checks In the child product .
     * 
     * @param productTextTypeAttrName
     *            String name of Single line type of attribute
     * @param productTextTypeAttrValue
     *            String value for single line type of attribute
     * @param waitForReload
     *            WebDriverWait Object
     * @param productFolderName
     *            String contains name of product folder
     * @param childProductName
     *            String contains name of child product
     * @param productTabName
     *            String contains name of product tab
     */
    private String assignValuesToChildProductAttributes(
            String productTextTypeAttrName, String productTextTypeAttrValue,
            WebDriverWait waitForReload, String productFolderName,
            String childProductName, String productTabName) {
        goToPimStudioTreeSection();
        clkOnParentProductFolder(productFolderName);
        String childProductId = getChildProductId(childProductName);
        goToRightSectionWindow();
        clickOnTheGivenProductTab(productTabName, waitForReload);
        addTextInTextField(productTextTypeAttrName, waitForReload,
                productTextTypeAttrValue, childProductName);
        checkInProduct(childProductName);
        return childProductId;
    }

    /**
     * This method clicks on Parent product folder
     * 
     * @param productFolderName
     *            String Object containing product folder name.
     */
    public void clkOnParentProductFolder(String productFolderName) {
        try {
            CSUtility.tempMethodForThreadSleep(3000);
            getCreatedProductFolder(productFolderName).click();
            CSLogger.info("Clicked on parent product folder");
        } catch (Exception e) {
            CSLogger.error("Parent folder not found", e);
            Assert.fail("Parent folder not found");
        }
    }

    /**
     * Clicks on child product
     * 
     * @param childProductName
     *            String object containing name of the child product
     */
    public void clkOnChildProduct(String childProductName) {
        getCreatedProductFolder(childProductName).click();
        CSLogger.info("Clicked on child product");
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName
     *            String name of the tab in the product view.
     * @param waitForReload
     *            WebDriverWait Object
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
     * /** Enters the given values into text fields of child product text
     * attribute
     * 
     * @param productTextTypeAttrName
     *            String name of single line attribute.
     * @param textAttrValue
     *            String value for single line attribute
     * @param waitForReload
     *            WebDriverWait Object
     * @param childProductName
     *            String object containing name of child product
     */
    private void addTextInTextField(String productTextTypeAttrName,
            WebDriverWait waitForReload, String textAttrValue,
            String childProductName) {
        WebElement element;
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//a[@class='GuiEditorSelected'][contains(text(),'"
                                + childProductName + "')]")));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//tr[contains(@cs_name,'"
                        + productTextTypeAttrName + "')]/td[2]/input[1]")));
        element = browserDriver.findElement(By.xpath("//tr[contains(@cs_name,'"
                + productTextTypeAttrName + "')]/td[2]/input[1]"));
        element.sendKeys(textAttrValue);
        CSLogger.info(
                "Inserted " + textAttrValue + " as value in the text field.");
    }

    /**
     * This method performs check in operation on child product
     * 
     * @param childProductName
     *            String contains name of child product
     */
    public void checkInProduct(String childProductName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//a[@class='GuiEditorSelected'][contains(text(),'"
                                + childProductName + "')]")));
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCheckIn(waitForReload);
        CSLogger.info("Checked In child product");
    }

    /**
     * Performs simple search operation and also verifies the result
     * 
     * @param productFolderName
     *            String object containing name of parent product folder
     * @param searchableTextData
     *            String object containing text data that will be added in some
     *            child product and searched while performing simple search
     * @param nonSearchableTextData
     *            String object containing text data that is not present in any
     *            child product
     * @param childProductId
     *            String object containing Id of child product
     * @param searchableText
     *            Contains boolean values true or false
     */
    private void performSimpleSearchOperation(String productFolderName,
            String searchableTextData, String nonSearchableTextData,
            String childProductId, Boolean searchableText) {
        goToProductsNode();
        clkOnParentProductFolder(productFolderName);
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions.elementToBeClickable(By
                .xpath("//table[@class='CSGuiList ']/tbody/tr[3]/td[contains(@cl"
                        + "ass,'CSGuiListFooter')]/table/tbody/tr/td[@class='_ri"
                        + "ght'][contains(text(),'Elements')]")));
        CSLogger.info("Waiting completed for footer");
        CSUtility.tempMethodForThreadSleep(2000);
        if (searchableText) {
            csGuiToolbarHorizontalInstance.enterTextInSearchBox(waitForReload,
                    searchableTextData);
            waitForReload.until(ExpectedConditions.elementToBeClickable(By
                    .xpath("//table[@class='CSGuiList ']/tbody/tr[3]/td[@class="
                            + "'CSGuiListFooter']/table/tbody/tr/td[2][@class='_"
                            + "right'][contains(text(),'Results')]")));
            CSLogger.info("Waiting completed for search results");
        } else {
            csGuiToolbarHorizontalInstance.enterTextInSearchBox(waitForReload,
                    nonSearchableTextData);
            waitForReload.until(ExpectedConditions.elementToBeClickable(By
                    .xpath("//table[@class='CSGuiList ']/tbody/tr[3]/td[contains"
                            + "(@class,'CSGuiListFooter')]/table/tbody/tr/td[@c"
                            + "lass='_right'][contains(text(),'Results')]")));
            CSLogger.info("Waiting completed for search results");
        }
        verifySimpleSearchResult(childProductId, searchableTextData,
                nonSearchableTextData, searchableText);
    }

    /**
     * Scrolls to pim products node
     */
    private void goToProductsNode() {
        goToPimStudioTreeSection();
        CSUtility.scrollUpOrDownToElement(
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
    }

    /**
     * Verifies that parent product is found if it contains search text data and
     * no products is found if text data is not present in any product
     * 
     * @param childProductId
     *            String object containing Id of child product
     * @param searchableTextData
     *            String object containing text data that will be added in some
     *            child product and searched while performing simple search
     * @param nonSearchableTextData
     *            String object containing text data that is not present in any
     *            child product
     * @param searchableText
     *            Contains boolean values true or false
     */
    private void verifySimpleSearchResult(String childProductId,
            String searchableTextData, String nonSearchableTextData,
            Boolean searchableText) {
        CSUtility.tempMethodForThreadSleep(4000);
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'External')]")));
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='hidewrap CSAdminList']")));
        CSLogger.info("Waiting completed for table data");
        WebElement resultTableData = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));

        if (searchableText) {
            int searchResult = resultTableData
                    .findElements(
                            By.xpath("//tr[@id='" + childProductId + "']"))
                    .size();
            if (searchResult != 0) {
                CSLogger.info("Product found with search text "
                        + searchableTextData + ": teststep passed");
            } else {
                CSLogger.error("Product not found with search text "
                        + searchableTextData + ": teststep failed");
                softAssertion.fail("Product not found with search text "
                        + searchableTextData + ": teststep failed");
            }
        } else {
            int searchResult = resultTableData
                    .findElements(By.xpath("//tbody/tr[@id]")).size();
            if (searchResult > 2) {
                CSLogger.error("Product found with non searchable text "
                        + nonSearchableTextData + ": teststep failed");
                softAssertion.fail("No Product found with non searchable text "
                        + nonSearchableTextData + ": teststep failed");
            } else {
                CSLogger.info("No Product found with search text "
                        + nonSearchableTextData + ": teststep passed");
            }
        }
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param childProductName
     *            String containing name of child product
     * @return child product Id String object containing Id of child product
     */
    private String getChildProductId(String childProductName) {
        goToPimStudioTreeSection();
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(
                browserDriver.findElement(By.linkText(childProductName)))
                .perform();
        goToRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        String childProductId = browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
        return childProductId;
    }

    /**
     * This is a data provider method.
     * 
     * @return Array
     */
    @DataProvider(name = "simpleSearchTestData")
    public Object[][] getSwitchMergeToDistributePageContentData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"), dataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        softAssertion = new SoftAssert();
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        classPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
    }
}
