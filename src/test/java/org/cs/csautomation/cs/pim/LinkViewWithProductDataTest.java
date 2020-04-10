/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimProductUsagesWindow;
import org.cs.csautomation.cs.pom.PimStudioChannelsNode;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
import org.cs.csautomation.cs.settings.SettingsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to create view of product and handle the
 * 'reference product' dialog with different options and also verify the
 * property fields and selection fields of the created view of product.
 * 
 * @author CSAutomation Team
 *
 */
public class LinkViewWithProductDataTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private PimStudioChannelsNode     pimStudioChannelsNodeInstance;
    private IProductPopup             productPopup;
    private SoftAssert                softAssertion;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private FrameLocators             iframeLocatorsInstance;
    private SettingsPage              settingsPageInstance;
    private PimProductUsagesWindow    pimProductUsagesWindowInstance;
    private IAttributePopup           attributePopup;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private IClassPopup               classPopup;
    private ModalDialogPopupWindow    modalDialogPopupWindowInstance;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private String                    linkViewWithProductDataSheetName = "linkViewWithProductData";
    private IMoreOptionPopup          moreOptionPopup;

    /**
     * This method creates view of product.
     * 
     * @param productName String contains name of product
     * @param viewName String contains name of the view or channel
     * @param selectedVersion String specifying option values for selected
     *            Version field in reference product dialog
     * @param createRecursively String specifying option values for create
     *            Recursively field in reference product dialog
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     * @param className String contains name of the class
     * @param productTabName String object contains name of product tab
     * @param attributeName String object contains name of attribute folder
     * @param orginalAttributeValue String object contains value of attribute
     * @param editedAttributeValue String object contains value of attribute
     * @param attributeFolderName String object contains name of attribute
     *            folder
     * @param inheritMode String object contains inherit mode
     */
    @Test(priority = 1, dataProvider = "linkViewWithProductDataTestData")
    public void testLinkViewWithProductData(String productName, String viewName,
            String selectedVersion, String createRecursively,
            String selectionType, String className, String productTabName,
            String attributeName, String orginalAttributeValue,
            String editedAttributeValue, String attributeFolderName,
            String inheritMode) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 80);
            switchToPimAndExpandSettingsTree(waitForReload);
            createAttributeFolder(attributeFolderName);
            createSingleLineAttribute(attributeFolderName, attributeName);
            setInheritanceOptionForTextAttribute(attributeName, inheritMode);
            createClass(className);
            assignAttrFolderToClass(className, attributeFolderName);
            createPimProduct(waitForReload, pimStudioProductsNodeInstance,
                    productName);
            assignClassToProduct(productName, className);
            assignTextToProduct(productName, productTabName, attributeName,
                    orginalAttributeValue);
            verifyReferenceProductDialog(productName, viewName, selectedVersion,
                    createRecursively, selectionType);
            createViewOfProduct(productName, viewName, false, selectedVersion,
                    createRecursively, selectionType, className);
            createViewOfProduct(productName, viewName, true, selectedVersion,
                    createRecursively, selectionType, className);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation Error in : testLinkViewWithProductData",
                    e);
            Assert.fail("Automation Error in : testLinkViewWithProductData");
        }
    }

    /**
     * This test method edits the created view and also checks whether the
     * original products data remains unchanged
     * 
     * @param productName String contains name of product
     * @param viewName String contains name of the view or channel
     * @param selectedVersion String specifying option values for selected
     *            Version field in reference product dialog
     * @param createRecursively String specifying option values for create
     *            Recursively field in reference product dialog
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     * @param className String contains name of the class
     * @param productTabName String object contains name of product tab
     * @param attributeName String object contains name of attribute folder
     * @param orginalAttributeValue String object contains value of attribute
     * @param editedAttributeValue String object contains value of attribute
     * @param attributeFolderName String object contains name of attribute
     *            folder
     * @param inheritMode String object contains inherit mode
     */
    @Test(
            priority = 2,
            dataProvider = "linkViewWithProductDataTestData",
            dependsOnMethods = "testLinkViewWithProductData")
    public void testEditViews(String productName, String viewName,
            String selectedVersion, String createRecursively,
            String selectionType, String className, String productTabName,
            String attributeName, String orginalAttributeValue,
            String editedAttributeValue, String attributeFolderName,
            String inheritMode) {
        try {
            clickOnProductPane(productTabName);
            checkInheritedBackgroundGreyColor(attributeName);
            doProductCheckOut();
            hoverOnInheritedValues(productName, attributeName);
            editIheritedValuesOfAttribute(editedAttributeValue, productName,
                    attributeName);
            checkOriginalUnchangeProductData(productName, attributeName,
                    orginalAttributeValue, productTabName);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testEditViews", e);
            Assert.fail("Automation error : testEditViews");
        }
    }

    /**
     * This test method Verify whether control usage in views works
     * 
     * @param productName String contains name of product
     * @param viewName String contains name of the view or channel
     * @param selectedVersion String specifying option values for selected
     *            Version field in reference product dialog
     * @param createRecursively String specifying option values for create
     *            Recursively field in reference product dialog
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     * @param className String contains name of the class
     * @param productTabName String object contains name of product tab
     * @param attributeName String object contains name of attribute folder
     * @param orginalAttributeValue String object contains value of attribute
     * @param editedAttributeValue String object contains value of attribute
     * @param attributeFolderName String object contains name of attribute
     *            folder
     * @param inheritMode String object contains inherit mode
     */
    @Test(
            priority = 3,
            dataProvider = "linkViewWithProductDataTestData",
            dependsOnMethods = "testLinkViewWithProductData")
    public void testVerifyControlUsageInViewsWork(String productName,
            String viewName, String selectedVersion, String createRecursively,
            String selectionType, String className, String productTabName,
            String attributeName, String orginalAttributeValue,
            String editedAttributeValue, String attributeFolderName,
            String inheritMode) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 80);
            enablePimStudioAndMamStudioUsageButton();
            String createdViewOfProductId = getCreatedViewOfProductId(viewName,
                    productName);
            handleUsageWindow(viewName, productName, createdViewOfProductId);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in : testVerifyControlUsageInViewsWork",
                    e);
            Assert.fail(
                    "Automation Error in : testVerifyControlUsageInViewsWork");
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object.
     */
    private void switchToPimAndExpandSettingsTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * This method creates a attribute folder of name passed as argument.
     * 
     * @param attributeFolderName String object contains name of attribute
     *            folder.
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
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload WebDriverWait Object.
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This creates a single line text attribute.
     * 
     * @param attributeName String Object contains name of attribute.
     */
    public void createSingleLineAttribute(String attributeFolderName,
            String attributeName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
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
                attributeName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSLogger.info("Text attribute created successfully");
    }

    /**
     * This method creates a given class.
     * 
     * @param className String object contains name of class.
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
     * Sets the inheritance option for text attribute.
     * 
     * @param attributeName String object contains attribute name.
     * @param inheriteMode String object contains inherit mode.
     */
    private void setInheritanceOptionForTextAttribute(String attributeName,
            String inheriteMode) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(attributeName)));
        WebElement textAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        textAttribute.click();
        goToRightSectionWindow();
        csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsInherited(),
                inheriteMode);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Inheritance Selected");
    }

    /**
     * Assigns attribute folder to the given class.
     * 
     * @param className String object contains name of class.
     * @param attributeFolderName String object contains name of attribute
     *            folder.
     */
    private void assignAttrFolderToClass(String className,
            String attributeFolderName) {
        goToPimStudioTreeSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(className)));
        WebElement pimStudioClass = browserDriver
                .findElement(By.linkText(className));
        pimStudioClass.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
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
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='CSGuiToolbarHorizontal']")));
        CSUtility.tempMethodForThreadSleep(3000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Attribute folder " + attributeFolderName
                + "assigned to class: " + className + "successfully");
    }

    /**
     * This method creates the product with supplied name.
     * 
     * @param waitForReload WebDriverWait Object
     * @param pimStudioProductsNode PimStudioProductsNode Object
     * @param productName String Object contains the value for product Name.
     */
    private void createPimProduct(WebDriverWait waitForReload,
            PimStudioProductsNodePage pimStudioProductsNode,
            String productName) {
        goToProductsNode();
        createProduct(productName,
                pimStudioProductsNode.getBtnPimProductsNode(),
                productPopup.getCsGuiPopupMenuCreateNew());
        CSLogger.info("Product created successfully");
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName String object contains name of product
     * @param nodeElement WebElement of product
     * @param popupMenuOption WebElement contains menu option.
     */
    private void createProduct(String productName, WebElement nodeElement,
            WebElement popupMenuOption) {
        try {
            CSUtility.rightClickTreeNode(waitForReload, nodeElement,
                    browserDriver);
            productPopup.selectPopupDivMenu(waitForReload, popupMenuOption,
                    browserDriver);
            productPopup.enterValueInDialogue(waitForReload, productName);
            CSLogger.info(
                    "Entered value " + productName + " in the textfield.");
            productPopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
        } catch (Exception e) {
            CSLogger.error("Automation error", e);
        }
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
     * Assigns the given class to the product.
     * 
     * @param productName String Object contains the product name.
     * @param className String object contains name of the class.
     */
    public void assignClassToProduct(String productName, String className) {
        doubleClkOnProduct(productName);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        selectClassFromDataSelectionDialogWindow(className);
    }

    /**
     * Assigns the given text to product
     * 
     * @param productName String contains name of product
     * @param productTabName String contains name of product tab
     * @param attributeName String contains name of attribute
     * @param orginalAttributeValue String contains text to be assigned
     */
    private void assignTextToProduct(String productName, String productTabName,
            String attributeName, String orginalAttributeValue) {
        Actions action = new Actions(browserDriver);
        goToPimStudioTreeSection();
        clkOnPimProductsTree();
        WebElement product = browserDriver
                .findElement(By.linkText(productName));
        action.doubleClick(product).build().perform();
        clickOnProductPane(productTabName);
        CSUtility.tempMethodForThreadSleep(1000);
        enterDataInTextField(attributeName, orginalAttributeValue,
                productTabName);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Text Send to the product and save");
    }

    /**
     * This methods enters data in text fields
     * 
     * @param attributeName String type that contains name of the attribute
     * @param attributeData String type that contains value to be entered in
     *            text attribute
     */
    public void enterDataInTextField(String attributeName, String attributeData,
            String productTabName) {
        clkOnTextField(attributeName, productTabName);
        browserDriver
                .findElement(By.xpath(
                        "//table/tbody/tr[contains(@cs_name,'" + attributeName
                                + "')]/td[2]/input[contains(@id,'PdmarticleCS_ItemAttribute')]"))
                .sendKeys(attributeData);
    }

    /**
     * This method performs operation of clicking on text fields
     * 
     * @param attributeName String type that contains name of the attribute
     */
    public void clkOnTextField(String attributeName, String productTabName) {
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]")));
        Actions actions = new Actions(browserDriver);
        WebElement elementLocator = browserDriver.findElement(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]"));
        actions.doubleClick(elementLocator).perform();
    }

    /**
     * Performs double click operation on product.
     * 
     * @param ProductName String Object contains the product name.
     * 
     */
    public void doubleClkOnProduct(String productName) {
        try {
            goToPimStudioTreeSection();
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement product = getProduct(productName);
            Actions action = new Actions(browserDriver);
            action.doubleClick(product).perform();
        } catch (Exception e) {
            CSLogger.error(
                    "product not found for performing double click operation",
                    e);
            softAssertion.fail(
                    "product not found for performing double click operation");
        }
    }

    /**
     * Returns the webElement of product.
     * 
     * @param productName String contains name of product.
     * @return WebElement Product
     */
    public WebElement getProduct(String productName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productName)));
        return browserDriver.findElement(By.linkText(productName));
    }

    /**
     * selects the class of given class name that will be assigned to the
     * product.
     * 
     * @param className String object contains name of the class.
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
            CSLogger.info("Class : " + className + "assigned to product "
                    + "successfully");
        } catch (Exception e) {
            CSLogger.error("Class not found", e);
            Assert.fail("Class not found");
        }
    }

    /**
     * Returns WebElement of created product.
     * 
     * @param productName String contains name of product.
     * @return WebElement CreatedProduct
     */
    public WebElement getCreatedProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(productName)));
        return browserDriver.findElement(By.linkText(productName));
    }

    /**
     * This method performs operation of clicking on pim products Tree node
     */
    public void clkOnPimProductsTree() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioProductsNodeInstance.traverseToPimProductsNode(waitForReload,
                browserDriver);
        CSUtility.scrollUpOrDownToElement(
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * Clicks on created product.
     * 
     * @param productName String contains name of product.
     */
    public void clkOnCreatedProduct(String productName) {
        clkOnPimProductsTree();
        getCreatedProduct(productName).click();
        CSLogger.info("Clicked on " + productName);
    }

    /**
     * Double clicks on product.
     * 
     * @param productName
     */
    public void doubleClickOnProduct(String productName) {
        clkOnPimProductsTree();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getCreatedProduct(productName));
        Actions doubleClick = new Actions(browserDriver);
        doubleClick.doubleClick(getCreatedProduct(productName)).build()
                .perform();
        CSLogger.info("Double clicked on element" + productName);
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * Returns the webElement of Created view
     * 
     * @param viewName String contains name of the view
     * @return webElement created view
     */
    public WebElement getCreatedView(String viewName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(viewName)));
        return browserDriver.findElement(By.linkText(viewName));
    }

    /**
     * This method clicks on created view
     * 
     * @param viewName String contains name of the view
     */
    public void clkOnCreatedView(String viewName) {
        getCreatedView(viewName).click();
        CSLogger.info("Clicked on " + viewName);
    }

    /**
     * This method clicks on Pim Channels tree node
     */
    public void clkOnPimChannelsTree() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioChannelsNodeInstance.traverseToPimChannelsNode(waitForReload);
        CSUtility.scrollUpOrDownToElement(
                pimStudioChannelsNodeInstance.getBtnPimChannelsNode(),
                browserDriver);
        pimStudioChannelsNodeInstance.clkBtnPimChannelsNode(waitForReload);
    }

    /**
     * This method drags the product and drops it on view
     * 
     * @param productName String contains name of product
     * @param viewName String contains name of the view
     */
    public void dragAndDropProductOntoView(String productName,
            String viewName) {
        clkOnPimProductsTree();
        CSUtility.tempMethodForThreadSleep(1000);
        clkOnPimChannelsTree();
        WebElement createdProduct = getCreatedProduct(productName);
        WebElement createdView = getCreatedView(viewName);
        Actions dragAndDropAction = new Actions(browserDriver);
        dragAndDropAction.dragAndDrop(createdProduct, createdView).build()
                .perform();
        CSLogger.info("dropped product on view");
    }

    /**
     * This method performs action of creating view of product when clicked on
     * ok and also checks the expected behavior and when clicked on cancel of
     * reference product dialog the view should not be created
     * 
     * @param productName String contains name of product
     * @param pressOkay Boolean values contains true or false
     * @param viewName String contains name of the view
     * @param selectedVersion String specifying option values for selected
     *            Version field in reference product dialog
     * @param createRecursively String specifying option values for create
     *            Recursively field in reference product dialog
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     * @param className String contains name of the class
     */
    public void handleDialogReferenceProduct(String productName,
            Boolean pressOkay, String viewName, String selectedVersion,
            String createRecursively, String selectionType, String className) {
        selectDialogOptionsOfReferenceProduct(selectedVersion,
                createRecursively, selectionType);
        askBoxWindowOperationForReferenceProductDialog(pressOkay);
        String viewId = getIds(viewName);
        String productId = getIds(productName);
        if (pressOkay) {
            verifyViewOfProductCreated(viewId, productName);
            verifyEditorWindowValues(productName, viewName, viewId,
                    selectionType, className, productId);
        } else {
            verifyViewOfProductNotCreated(viewId, productName);
        }
    }

    /**
     * This method checks the default fields of reference product dialog
     * 
     * @param productName String contains name of product
     * @param selectedVersion String specifying option values for selected
     *            Version field in reference product dialog
     * @param createRecursively String specifying option values for create
     *            Recursively field in reference product dialog
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     */
    public void verifyDefaultSelectedOptionOfDialogReferenceProduct(
            String productName, String selectedVersion,
            String createRecursively, String selectionType) {
        traverseToCsPortalWindowContent();
        String referenceProductDialogName = getDialogTitle();
        String referenceProductDialoglabel = getDialogLabel();
        String referenceProductDialogSelectedVersion = getDefaultSelectedValueFromDropDown(
                productPopup.getDdCsGuiDialogSelectedVerion());
        String referenceProductDialogCreateRecursively = getDefaultSelectedValueFromDropDown(
                productPopup.getDdCsGuiDialogCreateRecursively());
        String referenceProductDialogSelectionType = getDefaultValueOfSelectionTypeOption();
        Boolean referenceProductEnhanceItemProduct = handleEnhanceItemProductOption();
        getDefaultValueOfSelectionTypeOption();
        if (referenceProductDialogName.equals("Reference Product")
                && referenceProductDialoglabel.equals(productName)
                && referenceProductDialogSelectedVersion.equals(selectedVersion)
                && referenceProductDialogCreateRecursively
                        .equals(createRecursively)
                && referenceProductDialogSelectionType.equals(selectionType)
                && referenceProductEnhanceItemProduct) {
            CSLogger.info(
                    "Reference product dialog options all verified testcase "
                            + "passed");
        } else {
            CSLogger.error(
                    "Reference product dialog options verification failed ");
            softAssertion.fail(
                    "Reference product dialog options verification failed");
        }
    }

    /**
     * This method handles reference product dialog's Selection Type Option and
     * returns its default selected option
     * 
     * @return
     */
    public String getDefaultValueOfSelectionTypeOption() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//select[contains(@id,'RecorditemstructureSelection')]"
                                + "")));
        browserDriver.findElement(By.xpath(
                "//select[contains(@id,'" + "RecorditemstructureSelection')]"))
                .click();
        String defaultSelectedValue = browserDriver
                .findElement(By.xpath("//select[contains(@id,'"
                        + "RecorditemstructureSelection')]"))
                .getAttribute("defaultvalue");
        String defaultSelectedOption = browserDriver.findElement(By
                .xpath("//select[contains(@id,'RecorditemstructureSelection')]"
                        + "/option[@value='" + defaultSelectedValue + "']"))
                .getText();
        return defaultSelectedOption;
    }

    /**
     * Selects drop down options of various fields, selects that option that is
     * passed from data driven data
     * 
     * @param selectedVersion String specifying option values for selected
     *            Version field in reference product dialog
     * @param createRecursively String specifying option values for create
     *            Recursively field in reference product dialog
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     */
    public void selectDialogOptionsOfReferenceProduct(String selectedVersion,
            String createRecursively, String selectionType) {
        selectDrpDownOptionByVisibleText(
                productPopup.getDdCsGuiDialogSelectedVerion(), selectedVersion);
        selectDrpDownOptionByVisibleText(
                productPopup.getDdCsGuiDialogCreateRecursively(),
                createRecursively);
        selectDrpDownOptionByVisibleText(
                productPopup.getDdCsGuiDialogSelectionType(), selectionType);
    }

    /**
     * Selects drop down option by visibile text of drop down
     * 
     * @param element drop down WebElement
     * @param optionValue option that needs to be selected from drop down
     *            options
     */
    public void selectDrpDownOptionByVisibleText(WebElement element,
            String optionValue) {
        Select selectedOption = new Select(element);
        element.click();
        selectedOption.selectByVisibleText(optionValue);
    }

    /**
     * This method gets the label of 'reference product' dialog
     * 
     * @return label value of the reference product dialog
     */
    public String getDialogLabel() {
        return productPopup.getLblCsGuiDialogLabel().getAttribute("value");
    }

    /**
     * This method gets the Title of 'reference product' dialog
     * 
     * @return the Title of 'reference product' dialog
     */
    public String getDialogTitle() {
        return productPopup.getLblCsGuiModalDialogTitle().getText();
    }

    /**
     * Switches the frame till CSPortalWindowContent frame
     */
    public void traverseToCsPortalWindowContent() {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.className("CSPortalWindow")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Swicted till  CSPortalWindowContent frame");
    }

    /**
     * Gets the default selected value of drop down fields from reference
     * product dialog
     * 
     * @param element drop down webElement
     * @return selected option of drop down element
     */
    public String getDefaultSelectedValueFromDropDown(WebElement element) {
        String selectedOption = new Select(element).getFirstSelectedOption()
                .getText();
        return selectedOption;
    }

    /**
     * Checks whether the Enhance Item Product option is enabled or not in
     * reference product dialog
     * 
     * @return true if Enhance Item product option is enabled false otherwise
     */
    public Boolean handleEnhanceItemProductOption() {
        traverseToCsPortalWindowContent();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                productPopup.getCbCsGuiDialogEnhanceItemProduct());
        // productPopup.getCbCsGuiDialogEnhanceItemProduct().click();
        Boolean isEnhanceItemProductOptionEnabled = productPopup
                .getCbCsGuiDialogEnhanceItemProduct().getAttribute("class")
                .contains("On");
        if (isEnhanceItemProductOptionEnabled) {
            CSLogger.info("Enhance Item Product Option is enabled");
            return true;
        } else {
            CSLogger.error("Enhance Item Product Option is disabled "
                    + "which is error");
            softAssertion.fail("Enhance Item Product Option is disabled "
                    + "which is error");
            return false;
        }
    }

    /**
     * Verifies the 'Reference Product' Dialog.
     * 
     * @param productName String contains name of product
     * @param viewName String contains name of the view or channel
     * @param pressOkay Boolean Parameter contains true or false values
     * @param selectedVersion String specifying option values for selected
     *            Version field in reference product dialog
     * @param createRecursively String specifying option values for create
     *            Recursively field in reference product dialog
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     */
    private void verifyReferenceProductDialog(String productName,
            String viewName, String selectedVersion, String createRecursively,
            String selectionType) {
        dragAndDropProductOntoView(productName, viewName);
        verifyDefaultSelectedOptionOfDialogReferenceProduct(productName,
                selectedVersion, createRecursively, selectionType);
        askBoxWindowOperationForReferenceProductDialog(false);
    }

    /**
     * This method creates view of dragged product on view
     * 
     * @param productName String contains name of product folder
     * @param viewName String contains name of the view or channel
     * @param pressOkay
     * @param selectedVersion String specifying option values for selected
     *            Version field in reference product dialog
     * @param createRecursively String specifying option values for create
     *            Recursively field in reference product dialog
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     * @param className String Object contains name of class
     */
    public void createViewOfProduct(String productName, String viewName,
            Boolean pressOkay, String selectedVersion, String createRecursively,
            String selectionType, String className) {
        dragAndDropProductOntoView(productName, viewName);
        traverseToCsPortalWindowContent();
        handleDialogReferenceProduct(productName, pressOkay, viewName,
                selectedVersion, createRecursively, selectionType, className);
    }

    /**
     * This method selects ok or cancel of 'reference product' dialog depending
     * upon the argument ie false or true
     * 
     * @param pressOkay Boolean value contains true or false values
     */
    public void
            askBoxWindowOperationForReferenceProductDialog(Boolean pressOkay) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To Cs Portal Window Content Frame");
        if (pressOkay) {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("CSGUI_MODALDIALOG_INPUT_BUTTON_OK")));
            browserDriver
                    .findElement(
                            By.className("CSGUI_MODALDIALOG_INPUT_BUTTON_OK"))
                    .click();
            CSLogger.info("Clicked on OK Of Reference Product Popup ");
        } else {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL")));
            browserDriver
                    .findElement(By
                            .className("CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL"))
                    .click();
            CSLogger.info("Clicked on Cancel of Reference Product Popup");
        }
    }

    /**
     * This method gets the Id of product or view
     * 
     * @param components contains name of the product or view
     * @return Id of product or view
     */
    public String getIds(String component) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(browserDriver.findElement(By.linkText(component)))
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
     * This method verifies that A dialog 'Reference Product' should closed
     * without creating view of the selected product when clicked on cancel
     * button.
     * 
     * @param viewId String contains Id of the view
     * @param productName String contains name of product
     */
    public void verifyViewOfProductNotCreated(String viewId,
            String productName) {
        int isViewOfProductCreated = browserDriver.findElements(
                By.xpath("//span[contains(@id,'Structures@" + viewId
                        + "')]/span/table/tbody/tr/td/span/table/tbody/tr/td[2]"
                        + "/a/span/span[contains(text(),'" + productName
                        + "')]"))
                .size();
        if (isViewOfProductCreated != 0) {
            CSLogger.error(
                    "Teststep : 'Dialog Reference Product should closed without"
                            + " creating view of the selected product' failed");
            softAssertion
                    .fail("Teststep : 'Dialog Reference Product should closed "
                            + "without creating view of the selected product' "
                            + "failed");
        } else {
            CSLogger.info(
                    "Teststep : 'Dialog Reference Product should closed without"
                            + "creating view of the selected product' passed");
        }
    }

    /**
     * This method performs verification that after clicking ok button of
     * reference product dialog the view of product is created
     * 
     * @param viewId String contains Id of view
     * @param productName String contains name of product
     */
    public void verifyViewOfProductCreated(String viewId, String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        int isViewOfProductCreated = browserDriver.findElements(
                By.xpath("//span[contains(@id,'Structures@" + viewId
                        + "')]/span/table/tbody/tr/td/span/table/tbody/tr/td[2]"
                        + "/a/span/span[contains(text(),'" + productName
                        + "')]"))
                .size();

        if (isViewOfProductCreated != 0) {
            CSLogger.info(
                    "Teststep : 'Dialog should be closed and a new entry should"
                            + " be added below the created view with the"
                            + " provided label" + "of the product' passed");
        } else {
            CSLogger.error(
                    "Teststep : 'Dialog should be closed and a new entry should"
                            + " be added below the created view with the "
                            + "provided label " + "of the product' failed");
            softAssertion
                    .fail("Teststep : 'Dialog should be closed and a new entry "
                            + "should be added below the created view with the "
                            + "provided label of the product' failed");
        }
    }

    /**
     * Verifies the property fields and selection field values of the newly
     * created view of product
     * 
     * @param productName String contains name of product
     * @param viewName String contains name of the view or channel
     * @param viewId
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     * @param className String contains name of the class
     * @param productId String contains Id of the product
     */
    public void verifyEditorWindowValues(String productName, String viewName,
            String viewId, String selectionType, String className,
            String productId) {
        CSUtility.rightClickTreeNode(waitForReload,
                browserDriver.findElement(
                        By.xpath("//span[contains(@id,'Structures@" + viewId
                                + "')]/span/table/tbody/tr/td/span/table/tbody"
                                + "/tr/td[2]/a/span/span[contains(text(),'"
                                + productName + "')]")),
                browserDriver);
        productPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupMenuObject());
        productPopup.selectPopupDivSubMenu(waitForReload,
                productPopup.getCsGuiPopupMenuSubMenuViewFolderAsEditor(),
                browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        checkInProduct();
        editorWindowPropertiesFields(className, productName, productId);
        switch (selectionType) {
        case "Selected element only":
            break;
        case "Selected element and sub elements":
            editorWindowSelectionFields(selectionType, productName, productId);
            break;
        case "All sub elements":
            break;
        case "Only end nodes":
            break;
        default:
            CSLogger.fatal("No option selected from selection type");
            break;
        }
    }

    /**
     * Verifies Editor window property fields values
     * 
     * @param className
     * @param productName String contains name of product
     * @param productId String contains Id of the product
     */
    public void editorWindowPropertiesFields(String className,
            String productName, String productId) {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            try {
                String assignedProductClasses = browserDriver
                        .findElement(By.xpath("//div/input[contains(@id,'"
                                + "Pdmarticlestructure"
                                + "ExtendedItemConfiguration')]/following-"
                                + "sibling::div/table/tbody/tr[2]/td[2]/span"
                                + ""))
                        .getText();
                String assignedExtensionOf = browserDriver.findElement(
                        By.xpath("//tr[@id='AttributeRow_PdmarticleID']/td[2]"
                                + "/span"))
                        .getText();
                String expectedExtensionOfField = productName + " " + "("
                        + productId + ")";

                if (className.equals(assignedProductClasses)
                        && expectedExtensionOfField
                                .equals(assignedExtensionOf)) {
                    CSLogger.info(
                            "Properties pane editor window fields verification "
                                    + "passed");
                } else {
                    CSLogger.error(
                            "Properties pane editor window fields verification"
                                    + "failed");
                    softAssertion
                            .fail("Properties pane editor window fields verification"
                                    + "failed");
                }
            } catch (NoSuchElementException e) {
                CSLogger.error(
                        "Properties pane editor window fields verification failed : No Product classes found");
                softAssertion.fail(
                        "Properties pane editor window fields verification failed : No Product classes found");
            }
        } catch (Exception e) {
            CSLogger.error(
                    "Properties pane editor window fields verification failed ",
                    e);
            softAssertion.fail(
                    "Properties pane editor window fields verification failed ");
        }
    }

    /**
     * Verifies Selection fields values
     * 
     * @param selectionType String specifying option values for selection Type
     *            field in reference product dialog
     * @param productName String contains name of product
     * @param productId String contains Id of the product
     */
    public void editorWindowSelectionFields(String selectionType,
            String productName, String productId) {
        csGuiDialogContentIdInstance.clkDrpDwnSelectionTitle(waitForReload);
        CSUtility.scrollUpOrDownToElement(browserDriver.findElement(By.xpath(
                "//tr[@id='AttributeRow_SelectionRootIDs']" + "/td[2]/span")),
                browserDriver);
        String assignedSelectionOfParentNodes = browserDriver.findElement(
                By.xpath("//tr[@id='AttributeRow_SelectionRootIDs']"
                        + "/td[2]/span"))
                .getText();
        CSUtility.scrollUpOrDownToElement(browserDriver.findElement(By.xpath(
                "//tr[@id='AttributeRow_SelectionType']/td[2]" + "/span")),
                browserDriver);
        String assignedSelectionType = browserDriver.findElement(By.xpath(
                "//tr[@id='AttributeRow_SelectionType']/td[2]" + "/span"))
                .getText();

        String productNameWithId = productName + " " + "(" + productId + ")";

        if (productNameWithId.equals(assignedSelectionOfParentNodes)
                && selectionType.equals(assignedSelectionType)) {
            CSLogger.info(
                    "Editor window selection field values verified : passed");
        } else {
            CSLogger.error(
                    "Editor window selection field values verified : failed");
            softAssertion.fail(
                    "Editor window selection field values verified : failed");
        }
    }

    /**
     * This method performs check in operation on product.
     */
    public void checkInProduct() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        WebElement viewInfoTable = browserDriver.findElement(By.xpath(
                "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr"));
        ArrayList<WebElement> getFirstRow = (ArrayList<WebElement>) viewInfoTable
                .findElements(By.xpath("//td/a/img"));
        if ((getFirstRow.get(0).getAttribute("src")).endsWith("checkin.gif")) {
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCheckIn(waitForReload);
        } else if (getFirstRow.get(0).getAttribute("src")
                .endsWith("checkout.gif")) {
            CSLogger.info("Product already in checkin state");
        }
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String name of the tab in the product view.
     */
    private void clickOnProductPane(String productTabName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the " + productTabName + "tab");
    }

    /**
     * This method checks that values inserted in product should visible in view
     * and marked as inherited (grey color and background).
     */
    public void checkInheritedBackgroundGreyColor(String attributeName) {
        String greyBackgroundColor = browserDriver
                .findElement(By.xpath("//table/tbody/tr[contains(@cs_name,'"
                        + attributeName + "')]/td[2]/span"))
                .getCssValue("color");
        if (greyBackgroundColor.contains("rgba(136, 136, 136, 1)")) {
            CSLogger.info(
                    "Inherited values are displayed in grey : teststep passed");
        } else {
            CSLogger.error(
                    "Inherited values does not displayed in grey : teststep "
                            + "failed");
            softAssertion
                    .fail("Inherited values does not displayed in grey : teststep "
                            + "failed");
        }
    }

    /**
     * This method hovers on the inherited values of created view of product
     * 
     * @param productName String name of the tab in the product view.
     */
    public void hoverOnInheritedValues(String productName,
            String attributeName) {
        WebElement attributeToHover = getAttributeOfCreatedProductView(
                attributeName);
        Actions actionMoveToElement = new Actions(browserDriver);
        actionMoveToElement.moveToElement(attributeToHover).build().perform();
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.id("CSTooltipContainer")));
        String getAttributeHoverData = browserDriver
                .findElement(By.xpath("//div[@id='CSTooltipText']")).getText();
        if (getAttributeHoverData.contains(productName)) {
            CSLogger.info("Tooltip shows from " + productName
                    + " product and attribute this value should be inherited "
                    + "from teststep : passed");
        } else {
            CSLogger.error("Tooltip does not shows inherited values from"
                    + productName + "teststep : failed");
            softAssertion.fail("Tooltip does not shows inherited values from"
                    + productName + "teststep : failed");
        }
    }

    /**
     * This method overwrites those inherited values and insert any other data.
     * 
     * @param editData String contains edited text value of attribute
     * @param productName String name of the tab in the product view.
     */
    public void editIheritedValuesOfAttribute(String editData,
            String productName, String attributeName) {
        try {
            WebElement editInheritedAttribute = getAttributeOfCreatedProductView(
                    attributeName);
            editInheritedAttribute.click();
            editInheritedAttribute.clear();
            editInheritedAttribute.sendKeys(editData);
            checkInProduct();
            CSLogger.info(
                    "Changed value saved and view  checked-in successfully "
                            + "teststep : passed");
        } catch (Exception e) {
            CSLogger.error(
                    "Changed value didn't saved and view didn't checked-in "
                            + "successfully teststep : failed",
                    e);
        }
    }

    /**
     * Gets the attribute of created view of product
     * 
     * @param attributeName name of the attribute
     * @return WebElement of attribute in created view of product
     */
    public WebElement getAttributeOfCreatedProductView(String attributeName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//table/tbody/tr[contains(@cs_name,'" + attributeName
                                + "')]/td[2]/input[contains(@id,'"
                                + "PdmarticlestructureCS_ItemAttribute')]")));
        WebElement element = browserDriver.findElement(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/input[contains(@id,'PdmarticlestructureCS_"
                        + "ItemAttribute')]"));
        return element;
    }

    /**
     * Gets the attribute of original product
     * 
     * @param attributeName name of the attribute
     * @return WebElement of attribute in original product
     */
    public WebElement getAttributeOfOriginalProduct(String attributeName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//table/tbody/tr[contains(@cs_name,'" + attributeName
                                + "')]/td[2]/input[contains(@id,'"
                                + "PdmarticleCS_ItemAttribute')]")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//table/tbody/tr[contains(@cs_name,'" + attributeName + "')]"
                        + "/td[2]/input[contains(@id,'PdmarticleCS_ItemAttribute')]"));
        return element;
    }

    /**
     * This method performs checkout operation on the product view page.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void doProductCheckOut() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        WebElement viewInfoTable = browserDriver.findElement(By.xpath(
                "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr"));
        ArrayList<WebElement> getFirstRow = (ArrayList<WebElement>) viewInfoTable
                .findElements(By.xpath("//td/a/img"));
        if ((getFirstRow.get(0).getAttribute("src")).endsWith("checkout.gif")) {
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCheckOut(waitForReload);
        } else if (getFirstRow.get(0).getAttribute("src")
                .endsWith("checkin.gif")) {
            CSLogger.info("Product already in checkout state");
        }
    }

    /**
     * This method checks that after editing the values in created view of
     * product the original product retain its values
     * 
     * @param productName String name of the tab in the product view.
     * @param productAttributeData original attribute data
     */
    public void checkOriginalUnchangeProductData(String productName,
            String attributeName, String productAttributeData,
            String productTabName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        doubleClickOnProduct(productName);
        doProductCheckOut();
        clickOnProductPane(productTabName);
        WebElement originalAttribute = getAttributeOfOriginalProduct(
                attributeName);
        originalAttribute.click();
        String originalProductAttributeData = originalAttribute
                .getAttribute("value");
        if (productAttributeData.equals(originalProductAttributeData)) {
            CSLogger.info(
                    "Original values of product did not be changed teststep : "
                            + "passed");
        } else {
            CSLogger.error(
                    "Original values of product changed teststep : failed");
            softAssertion.fail(
                    "Original values of product changed teststep : failed");
        }
    }

    /**
     * This method enables PimStudioUsageButton and MamStudioUsageButton
     */
    public void enablePimStudioAndMamStudioUsageButton() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        settingsPageInstance.clkOnBtnSettingsNode(waitForReload);
        traverseToSettingsRightSectionFrames();
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.scrollUpOrDownToElement(settingsPageInstance.getBtnCore(),
                browserDriver);
        settingsPageInstance.clkOnBtnCore(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        settingsPageInstance.clkOnBtnUsages(waitForReload);
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(
                        By.xpath("//td[contains(text(),'CORE Usages')]")),
                browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        settingsPageInstance.checkPimStudioUsageButton(waitForReload);
        settingsPageInstance.checkMamStudioUsageButton(waitForReload);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method performs operation on usages window when clicked on usage
     * toolbar
     * 
     * @param viewName String contains name of the view
     * @param productName String name of the tab in the product view.
     * @param createdViewOfProductId product Id of the created view of product
     */
    public void handleUsageWindow(String viewName, String productName,
            String createdViewOfProductId) {
        doubleClickOnProduct(productName);
        String windowHandleBeforeUsageWindow = browserDriver.getWindowHandle();
        selectUsageOption();
        switchToCurrentWindowHandle();
        traverseToUsagesWindowLeftSectionFrames();
        pimProductUsagesWindowInstance
                .clkOnCsPortalGuiListUsagesInTree(waitForReload);
        pimProductUsagesWindowInstance
                .clkOnLblCsPortalGuiListChannel(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        traverseToUsagesWindowCenterFrames();
        pimProductUsagesWindowInstance.enterTextInSearchInput(waitForReload,
                createdViewOfProductId);
        verifyCreatedViewNameInUsageWindow(productName, createdViewOfProductId);
        browserDriver.close();
        browserDriver.switchTo().window(windowHandleBeforeUsageWindow);
    }

    /**
     * Selects the usages option.
     */
    private void selectUsageOption() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        moreOptionPopup.selectPopupDivMenu(waitForReload,
                moreOptionPopup.getCtxMoreOptionProduct(), browserDriver);
        moreOptionPopup.selectPopupDivSubMenu(waitForReload,
                moreOptionPopup.getCtxUsages(), browserDriver);
    }

    /**
     * This method switches the frame till main frame
     */
    public void traverseToSettingsRightSectionFrames() {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Switched till main frame");
    }

    /**
     * This method traverse till usages wibdow left section frames
     */
    public void traverseToUsagesWindowLeftSectionFrames() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameLeft()));
        CSLogger.info("Traverse to usages wibdow left section frames");
    }

    /**
     * Switches to new window for eg if any product is clicked and opened or
     * view is selected
     */
    public void switchToCurrentWindowHandle() {
        for (String winHandle : browserDriver.getWindowHandles()) {
            browserDriver.switchTo().window(winHandle);
        }
        CSLogger.info("Switched to new window handle");
    }

    /**
     * Gets product ID of the created view of product
     * 
     * @param viewName String contains name of the view
     * @param productName String name of the tab in the product view.
     * @return createdViewOfProductId
     */
    public String getCreatedViewOfProductId(String viewName,
            String productName) {
        clkOnPimChannelsTree();
        String viewId = getIds(viewName);
        String createdViewOfProductId = getIdOfCreatedViewOfProduct(viewId);
        return createdViewOfProductId;
    }

    /**
     * Returns the Id of created view of product
     * 
     * @param viewId String contains view ID
     * @return Id of created view of product
     */
    public String getIdOfCreatedViewOfProduct(String viewId) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver
                        .findElement(By.xpath("//span[contains(@id,'Structures@"
                                + viewId + "')]/span/table/tbody/tr/td/span")));
        WebElement createdViewOfProduct = browserDriver
                .findElement(By.xpath("//span[contains(@id,'Structures@"
                        + viewId + "')]/span/table/tbody/tr/td/span"));
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(createdViewOfProduct).perform();
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
     * Traverse till FrmSplitAreaFrameFrmCenter frame
     */
    public void traverseToUsagesWindowCenterFrames() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameFrmCenter()));
        CSLogger.info("Switched till FrmSplitAreaFrameFrmCenter frame");
    }

    /**
     * Checks whether the created view of product name appears in usage window
     * 
     * @param productName String name of the tab in the product view.
     * @param createdViewOfProductId String contains Id of created view of
     *            product
     */
    public void verifyCreatedViewNameInUsageWindow(String productName,
            String createdViewOfProductId) {
        CSUtility.tempMethodForThreadSleep(3000);
        traverseToUsagesWindowCenterFrames();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//table[contains(@class,'CSAdminList')]")));
        CSUtility.tempMethodForThreadSleep(1000);
        int checkNameOfCreatedViewInUsageWindow = browserDriver
                .findElements(By.id(createdViewOfProductId)).size();
        if (checkNameOfCreatedViewInUsageWindow != 0) {
            CSLogger.info(
                    "Name of  created view displays in usage window teststep : "
                            + "passed");
        } else {
            CSLogger.error(
                    "Name of  created view does not appears in usage window "
                            + "teststep :failed");
            softAssertion
                    .fail("Name of  created view does not appears in usage window "
                            + "teststep :failed");
        }
    }

    /**
     * This is a data provider method contains array of product name,class
     * name,reference product dialog option drop down fields values for e.g
     * selection type,create recursively,version
     * 
     * @return Array
     */
    @DataProvider(name = "linkViewWithProductDataTestData")
    public Object[][] getlinkViewWithProductData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                linkViewWithProductDataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
        pimStudioChannelsNodeInstance = new PimStudioChannelsNode(
                browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        settingsPageInstance = new SettingsPage(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        pimProductUsagesWindowInstance = new PimProductUsagesWindow(
                browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        classPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        moreOptionPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }

}
