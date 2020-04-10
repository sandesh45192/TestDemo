/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.awt.AWTException;
import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.CSPortalWindow;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to move or link products
 * 
 * @author CSAutomation Team
 *
 */
public class MoveLinkProductTest extends AbstractTest {

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
    private CSPortalWindow            csPortalWindowInstance;
    private SettingsPage              settingsPageInstance;
    private FrameLocators             iframeLocatorsInstance;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private String                    dataSheetName = "MoveOrLinkProduct";

    /**
     * This test method performs to move child products and link it to other
     * products with different action i.e Copy selection,Move
     * selection,Reference to pim.
     * 
     * @param moveParentProductName String Object contains product name under
     *            which child product will be created.
     * @param linkProductName String Object contains name of product.
     * @param movechildProductName String Object contains name of child
     *            products.
     * @param firstReferenceProduct String Object contains name of product that
     *            will be refered in another product by reference attribute.
     * @param secondReferenceProduct String Object contains name of product that
     *            will be refered in another product by reference attribute.
     * @param attributeFolderName String Object contains name of attribute
     *            folder.
     * @param pimReferenceAttrName String Object contains name of pim reference
     *            attribute.
     * @param attributeType String Object contains type of reference attribute
     *            i.e Reference to PIM Product
     * @param className String Object contains name of class.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param parentProdSettings contains the product names
     * @param childProdSettings contains the child names
     * 
     */
    @Test(priority = 1, dataProvider = "moveOrLinkTestData")
    public void testMoveLinkProduct(String moveParentProductName,
            String linkProductName, String movechildProductName,
            String firstReferenceProduct, String secondReferenceProduct,
            String attributeFolderName, String pimReferenceAttrName,
            String attributeType, String className, String productTabName,
            String parentProdSettings, String childProdSettings) {
        try {
            switchToPimAndExpandSettingsTree(waitForReload);
            createAttributeFolder(attributeFolderName);
            createReferenceToPimAttribute(attributeFolderName,
                    pimReferenceAttrName, attributeType);
            createClass(className);
            assignAttrFolderToClass(className, attributeFolderName);
            createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                    moveParentProductName);
            ArrayList<String> childProducts = new ArrayList<String>();
            for (int i = 1; i < 7; i++) {
                childProducts.add(movechildProductName + i);
                createChildProduct(moveParentProductName,
                        childProducts.get(i - 1));
            }
            createProduct(firstReferenceProduct,
                    pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                    csPopupDiv.getCsGuiPopupMenuCreateNew());
            assignClassToParentProduct(moveParentProductName, className);
            assignPimReferenceToParentProduct(moveParentProductName,
                    productTabName, pimReferenceAttrName);
            createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                    linkProductName);
            createProduct(secondReferenceProduct,
                    pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                    csPopupDiv.getCsGuiPopupMenuCreateNew());
            assignClassToParentProduct(linkProductName, className);
            assignPimReferenceToParentProduct(linkProductName, productTabName,
                    pimReferenceAttrName);
            enableExtendedDragDropCheckbox();
            refreshPage();
            switchToPimAndExpandProductTree(waitForReload);
            performMoveSelectionTests(moveParentProductName, linkProductName,
                    firstReferenceProduct, secondReferenceProduct,
                    pimReferenceAttrName, productTabName, childProducts);
            performCopySelectionTests(moveParentProductName, linkProductName,
                    firstReferenceProduct, secondReferenceProduct,
                    pimReferenceAttrName, productTabName, childProducts);

            performReferenceToPimTests(moveParentProductName, linkProductName,
                    pimReferenceAttrName, productTabName, childProducts);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testMoveOrLinkProduct", e);
            Assert.fail("Automation error in: testMoveOrLinkProduct");
        }
    }

    /**
     * Performs move selection tests.
     * 
     * @param moveParentProductName String Object contains name of parent
     *            product.
     * @param linkProductName String Object contains name of product.
     * @param firstReferenceProduct String contains name of reference attribute.
     * @param secondReferenceProduct String contains name of reference
     *            attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param childProducts List that holds names of child products.
     */
    private void performMoveSelectionTests(String moveParentProductName,
            String linkProductName, String firstReferenceProduct,
            String secondReferenceProduct, String pimReferenceAttrName,
            String productTabName, ArrayList<String> childProducts) {
        performMoveSelectionAction(moveParentProductName, childProducts.get(0),
                linkProductName, "Move selection",
                "Use inherited values of new parent folder.", false,
                secondReferenceProduct, pimReferenceAttrName, productTabName);
        performMoveSelectionAction(moveParentProductName, childProducts.get(0),
                linkProductName, "Move selection",
                "Use inherited values of new parent folder.", true,
                secondReferenceProduct, pimReferenceAttrName, productTabName);
        performMoveSelectionAction(moveParentProductName, childProducts.get(1),
                linkProductName, "Move selection", "Transfer inherited values",
                false, firstReferenceProduct, pimReferenceAttrName,
                productTabName);
        performMoveSelectionAction(moveParentProductName, childProducts.get(1),
                linkProductName, "Move selection", "Transfer inherited values",
                true, firstReferenceProduct, pimReferenceAttrName,
                productTabName);
    }

    /**
     * Performs copy selection tests.
     * 
     * @param moveParentProductName String Object contains name of parent
     *            product.
     * @param linkProductName String Object contains name of product.
     * @param firstReferenceProduct String contains name of reference attribute.
     * @param secondReferenceProduct String contains name of reference
     *            attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param childProducts List that holds names of child products.
     */
    private void performCopySelectionTests(String moveParentProductName,
            String linkProductName, String firstReferenceProduct,
            String secondReferenceProduct, String pimReferenceAttrName,
            String productTabName, ArrayList<String> childProducts) {
        performCopySelectionAction(moveParentProductName, childProducts.get(2),
                linkProductName, "Copy selection",
                "Use inherited values of new parent folder.", false,
                secondReferenceProduct, pimReferenceAttrName, productTabName);
        performCopySelectionAction(moveParentProductName, childProducts.get(2),
                linkProductName, "Copy selection",
                "Use inherited values of new parent folder.", true,
                secondReferenceProduct, pimReferenceAttrName, productTabName);
        performCopySelectionAction(moveParentProductName, childProducts.get(3),
                linkProductName, "Copy selection", "Transfer inherited values",
                false, firstReferenceProduct, pimReferenceAttrName,
                productTabName);
        performCopySelectionAction(moveParentProductName, childProducts.get(3),
                linkProductName, "Copy selection", "Transfer inherited values",
                true, firstReferenceProduct, pimReferenceAttrName,
                productTabName);
    }

    /**
     * Performs 'reference to pim' selection tests.
     * 
     * @param moveParentProductName String Object contains name of parent
     *            product.
     * @param linkProductName String Object contains name of product.
     * @param pimReferenceAttrName String contains name of reference attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param childProducts List that holds names of child products.
     */
    private void performReferenceToPimTests(String moveParentProductName,
            String linkProductName, String pimReferenceAttrName,
            String productTabName, ArrayList<String> childProducts) {
        performReferenceToPimAction(moveParentProductName, childProducts.get(4),
                linkProductName, "Reference with refToPim", "Set reference",
                false, childProducts.get(4), pimReferenceAttrName,
                productTabName, childProducts.get(4));
        performReferenceToPimAction(moveParentProductName, childProducts.get(4),
                linkProductName, "Reference with refToPim", "Set reference",
                true, childProducts.get(4), pimReferenceAttrName,
                productTabName, childProducts.get(4));
        performReferenceToPimAction(moveParentProductName, childProducts.get(5),
                linkProductName, "Reference with refToPim", "Add reference",
                false, childProducts.get(5), pimReferenceAttrName,
                productTabName, childProducts.get(4));
        performReferenceToPimAction(moveParentProductName, childProducts.get(5),
                linkProductName, "Reference with refToPim", "Add reference",
                true, childProducts.get(5), pimReferenceAttrName,
                productTabName, childProducts.get(4));
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
     * @param attributeFolderName String Object containing name of attribute
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
     * Creates a text attribute in given attribute folder and sets the
     * searchable attribute field with option passed as argument.
     * 
     * @param attributeFolderName String Object containing name of attribute
     *            folder.
     * @param txtAttrName String Object containing text attribute name.
     * @param attributeType String Object contains type of reference attribute
     *            i.e Reference to PIM Product.
     */
    private void createReferenceToPimAttribute(String attributeFolderName,
            String txtAttrName, String attributeType) {
        createAttribute(attributeFolderName, txtAttrName);
        goToPimStudioTreeSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(txtAttrName)));
        WebElement textAttribute = browserDriver
                .findElement(By.linkText(txtAttrName));
        textAttribute.click();
        CSLogger.info("Clicked on text attribute " + txtAttrName);
        goToRightSectionWindow();
        csGuiDialogContentIdInstance.clkBtnCSTypeLabel(waitForReload);
        selectAttributeType(
                csPortalWindowInstance.getAttributeType(attributeType),
                attributeType);
        goToRightSectionWindow();
        setInheritanceOptionOfReferenceAttribute(csGuiDialogContentIdInstance
                .getdropDownAttributeType("Inheritance"),
                "Inheritance from parent nodes");
        scrollUp();
        enableMultipleSelectionOptionOfReferenceAttribute();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Reference attribute  "
                + " created with inheritance option as 'Inheritance from parent "
                + "nodes' and enabled multiple selection option");
    }

    private void scrollUp() {
        WebElement element = browserDriver.findElement(
                By.xpath("//span[contains(text(),'Attribute Type')]"));
        JavascriptExecutor js = (JavascriptExecutor) browserDriver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    /**
     * This method creates a single line text attribute.
     * 
     * @param attributeFolderName String Object containing name of attribute
     *            folder.
     * @param txtAttrName String Object containing text attribute name.
     */
    private void createAttribute(String attributeFolderName,
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
     * This method creates a given class.
     * 
     * @param className String Object containing name of class.
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
     * Assigns attribute folder to the given class.
     * 
     * @param className String Object containing name of class.
     * @param attributeFolderName String Object containing name of attribute
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
        CSUtility.tempMethodForThreadSleep(3000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Attribute folder " + attributeFolderName
                + "assigned to class: " + className + "successfully");
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload WebDriverWait Object.
     * @param pimStudioProductsNode PimStudioProductsNode Object.
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
     * This method creates the child product with the supplied name and the
     * supplied parent product name.
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param childProductName String Object containing the child product name.
     */
    private void createChildProduct(String parentProductName,
            String childProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(parentProductName)));
        WebElement element = pimStudioProductsNodeInstance
                .expandNodesByProductName(waitForReload, parentProductName);
        createProduct(childProductName, element,
                csPopupDiv.getCsGuiPopupMenuNewChild());
        CSLogger.info("Created new child product");
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName String object containing name of product it can be
     *            child product or parent product.
     * @param nodeElement WebElement of either products node or parent product.
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
     * Scrolls to pim products node.
     */
    private void goToProductsNode() {
        goToPimStudioTreeSection();
        CSUtility.scrollUpOrDownToElement(
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * Assigns the given class to the parent product folder.
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param className String object containing name of the class.
     */
    private void assignClassToParentProduct(String parentProductName,
            String className) {
        doubleClkOnProduct(parentProductName);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        selectClassFromDataSelectionDialogWindow(className);
    }

    /**
     * Performs operation of double clicking on parent product.
     * 
     * @param ProductName String Object contains the product name.
     * 
     */
    private void doubleClkOnProduct(String productName) {
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
     * selects the class of given classname that will be assigned to the parent
     * product folder.
     * 
     * @param className String object containing name of the class.
     */
    private void selectClassFromDataSelectionDialogWindow(String className) {
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
            CSUtility.tempMethodForThreadSleep(1000);
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
     * Returns the WebElement of product.
     * 
     * @param productName String containing name of either product folder or
     *            product.
     * @return WebElement Product
     */
    private WebElement getProduct(String productName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productName)));
        return browserDriver.findElement(By.linkText(productName));
    }

    /**
     * clicks on attribute type Cross Reference.
     * 
     * @param attributeType type of attribute for eg dropdown or cross reference
     *            type.
     * @param technicalName contains string value for technical attribute name.
     */
    private void selectAttributeType(WebElement attributeType,
            String technicalName) {
        csPortalWindowInstance
                .clkDdCSPortalGuiListCrossReference(waitForReload);
        csPortalWindowInstance.selectCrossReferenceAttributeType(waitForReload,
                attributeType, technicalName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        Alert alertBox = null;
        try {
            alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
            if (alertBox != null) {
                alertBox.accept();
            }
        } catch (NoAlertPresentException e) {
            CSLogger.error("Assert fail due to dialogue popup doesn't appear");
            softAssertion
                    .fail("A dialog saying: 'Are you sure you want change the "
                            + "type of this attribute?' Data already saved in this"
                            + " attribute may become invalid with 'OK' and 'Cancel' "
                            + "button does not popup");
        }
    }

    /**
     * Selects passed option for inheritance field.
     * 
     * @param inheritanceAttr Specifies type of field i.e Inheritance.
     * @param inheritDataOption Specifies type of inheritance that should be
     *            selected eg. Inheritance from parent node.
     */
    private void setInheritanceOptionOfReferenceAttribute(
            WebElement inheritanceAttr, String inheritDataOption) {
        csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                inheritanceAttr, inheritDataOption);
    }

    /**
     * Assigns the referenceToPim type if attribute to parent product
     * 
     * @param productName String Object contains name of product.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param pimRefAttrName String Object contains name of reference attribute.
     */
    private void assignPimReferenceToParentProduct(String productName,
            String productTabName, String pimRefAttrName) {
        try {
            goToProductsNode();
            doubleClkOnProduct(productName);
            goToRightSectionWindow();
            clickOnTheGivenProductTab(productTabName, waitForReload);
            clkOnCSGuiSelectionListAdd(pimRefAttrName, waitForReload,
                    browserDriver);
            selectReferenceToPimProductType(productName, true, pimRefAttrName,
                    waitForReload, browserDriver);
            CSUtility.tempMethodForThreadSleep(2000);
            goToRightSectionWindow();
            waitForReload.until(ExpectedConditions.elementToBeClickable(By
                    .xpath("//span/a[@class='GuiEditorSelected'][contains(text(),'"
                            + productName + "')]")));
            CSUtility.tempMethodForThreadSleep(3000);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
        } catch (Exception e) {
            CSLogger.error("Reference not assigned", e);
            Assert.fail("Reference not assigned");
        }
    }

    /**
     * This method selects product that needs to be assigned to referenceToPim
     * attribute
     * 
     * @param productName String parameter that contains name of the product
     *            folder.
     * @param pressOkay Boolean parameter that contains true and false values.
     * @param attributeName String Object contains name of attribute.
     * @param waitForReload WebDriverWait Object.
     * @param browserDriver Instance of browserDriver.
     */
    private void selectReferenceToPimProductType(String productName,
            Boolean pressOkay, String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSLogger.info("Opened product folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + productName + "')]")));
        WebElement pimProduct = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + productName + "')]"));
        Actions action = new Actions(browserDriver);
        action.doubleClick(pimProduct).perform();
        CSLogger.info("Double clicked on product ");
        CSUtility.tempMethodForThreadSleep(5000);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        CSLogger.info("pim reference assigned to reference attribute");
    }

    /**
     * This method clicks on plus.
     * 
     * @param attributeName String Object contains name of attribute.
     * @param waitForReload WebDriverWait Object.
     * @param browserDriver Instance of browserDriver.
     */
    private void clkOnCSGuiSelectionListAdd(String attributeName,
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
        CSLogger.info("Clicked on + for assigning reference");
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
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
     * This method enables PimStudioUsageButton and MamStudioUsageButton
     */
    private void enableExtendedDragDropCheckbox() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        settingsPageInstance.clkOnBtnSettingsNode(waitForReload);
        traverseToSettingsRightSectionFrames();
        settingsPageInstance.clkOnBtnPim(waitForReload);
        settingsPageInstance.clkOnBtnPimSubTreeStudio(waitForReload);
        settingsPageInstance.clkOnBtnPimUserInterface(waitForReload);
        settingsPageInstance.expandDrpDwnUserInterfaceTree(waitForReload);
        settingsPageInstance.checkExtendedDragDrop(waitForReload);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method switches the frame till main frame
     */
    private void traverseToSettingsRightSectionFrames() {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Switched till main frame");
    }

    /**
     * Moves one product and drop it on another product.
     * 
     * @param moveProductFolder String Object contains name of parent product.
     * @param moveChildProductName String Object contains name of child product.
     * @param linkProductName String Object contains name of product.
     */
    private void performMoveProductOperation(String moveProductFolder,
            String moveChildProductName, String linkProductName) {
        WebElement sourceProductFolder = getProduct(moveProductFolder);
        sourceProductFolder.click();
        CSUtility.tempMethodForThreadSleep(1000);
        Actions action = new Actions(browserDriver);
        WebElement moveChildProduct = getProduct(moveChildProductName);
        WebElement linkProduct = getProduct(linkProductName);
        action.dragAndDrop(moveChildProduct, linkProduct).perform();
        CSLogger.info("Dragged product " + moveChildProductName
                + "and dropped it on " + linkProductName);
    }

    /**
     * Performs operation on "Move Product" Dialog box.
     * 
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     * @param pressOkay Contains boolean values either true or false.
     * @param childProductName String Object contains name of child product.
     */
    private void handleMoveProductDialogBox(String actionOption,
            String optionValue, Boolean pressOkay, String childProductName) {
        CSUtility.traverseToCsPortalWindowContent(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//select[contains(@id,'RecordMethod')]")));
        WebElement actionElement = browserDriver.findElement(
                By.xpath("//select[contains(@id,'RecordMethod')]"));
        selectValueFromDrpDwnElement(actionElement, actionOption);
        WebElement optionElement = browserDriver.findElement(
                By.xpath("//select[contains(@id,'RecordBehavior')]"));
        selectValueFromDrpDwnElement(optionElement, optionValue);
        if (pressOkay) {
            browserDriver
                    .findElement(
                            By.className("CSGUI_MODALDIALOG_INPUT_BUTTON_OK"))
                    .click();
            handleAlertDialog(childProductName, actionOption, optionValue);
        } else {
            browserDriver
                    .findElement(By
                            .className("CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL"))
                    .click();
        }
    }

    /**
     * Selects drop down option
     * 
     * @param drpdwnElement this is type of drop down webElement
     * @param valueOfDrpDwn text value in drop down webElement
     */
    private void selectValueFromDrpDwnElement(WebElement drpdwnElement,
            String valueOfDrpDwn) {
        drpdwnElement.click();
        Select element = new Select(drpdwnElement);
        element.selectByVisibleText(valueOfDrpDwn);
        CSLogger.info("Drop down option " + valueOfDrpDwn + " selected");
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
     * Performs the 'move selection' actions.
     * 
     * @param moveProductFolder String Object contains name of parent product.
     * @param moveChildProductName String Object contains name of product.
     * @param linkProductName String Object contains name of product.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     * @param pressOkay Contains boolean values i.e true or false.
     * @param expectedAttachedReference String contains name of expected
     *            attached reference attribute.
     * @param referenceAttributeName String contains name of reference
     *            attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     */
    private void performMoveSelectionAction(String moveProductFolder,
            String moveChildProductName, String linkProductName,
            String actionOption, String optionValue, Boolean pressOkay,
            String expectedAttachedReference, String referenceAttributeName,
            String productTabName) {
        goToPimStudioTreeSection();
        performMoveProductOperation(moveProductFolder, moveChildProductName,
                linkProductName);
        handleMoveProductDialogBox(actionOption, optionValue, pressOkay,
                moveChildProductName);
        verifyProductMoved(moveProductFolder, linkProductName,
                moveChildProductName, pressOkay, expectedAttachedReference,
                referenceAttributeName, productTabName, actionOption,
                optionValue);
    }

    /**
     * Performs the 'copy selection' actions.
     * 
     * @param moveProductFolder String Object contains name of parent product.
     * @param copyChildProductName String Object contains name of product.
     * @param linkProductName String Object contains name of product.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     * @param pressOkay Contains boolean values i.e true or false.
     * @param expectedAttachedReference String contains name of expected
     *            attached reference attribute.
     * @param referenceAttributeName String contains name of reference
     *            attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     */
    private void performCopySelectionAction(String moveProductFolder,
            String copyChildProductName, String linkProductName,
            String actionOption, String optionValue, Boolean pressOkay,
            String expectedAttachedReference, String referenceAttributeName,
            String productTabName) {
        goToPimStudioTreeSection();
        CSUtility.tempMethodForThreadSleep(1000);
        performMoveProductOperation(moveProductFolder, copyChildProductName,
                linkProductName);
        handleMoveProductDialogBox(actionOption, optionValue, pressOkay,
                copyChildProductName);
        verifyProductCopied(linkProductName, copyChildProductName, pressOkay,
                expectedAttachedReference, referenceAttributeName,
                productTabName, actionOption, optionValue);
    }

    /**
     * Performs the 'Reference To Pim' actions.
     * 
     * @param moveProductFolder String Object contains name of parent product.
     * @param copyChildProductName String Object contains name of product.
     * @param linkProductName String Object contains name of product.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     * @param pressOkay Contains boolean values i.e true or false.
     * @param expectedAttachedReference String contains name of expected
     *            attached reference attribute.
     * @param referenceAttributeName String contains name of reference
     *            attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param existingAttachedReferece
     */
    private void performReferenceToPimAction(String moveProductFolder,
            String copyChildProductName, String linkProductName,
            String actionOption, String optionValue, Boolean pressOkay,
            String expectedAttachedReference, String referenceAttributeName,
            String productTabName, String existingAttachedReferece) {
        goToPimStudioTreeSection();
        CSUtility.tempMethodForThreadSleep(1000);
        performMoveProductOperation(moveProductFolder, copyChildProductName,
                linkProductName);
        handleMoveProductDialogBox(actionOption, optionValue, pressOkay,
                copyChildProductName);
        if (optionValue.equals("Set reference")) {
            verifySetReference(linkProductName, productTabName,
                    expectedAttachedReference, referenceAttributeName,
                    actionOption, optionValue, pressOkay);
        } else {
            verifyAddReference(linkProductName, productTabName,
                    existingAttachedReferece, expectedAttachedReference,
                    referenceAttributeName, actionOption, optionValue,
                    pressOkay);
        }
    }

    /**
     * Handles alert dialog actions.
     * 
     * @param movedProduct String object contains name of product.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     */
    private void handleAlertDialog(String movedProduct, String actionOption,
            String optionValue) {
        Alert alert = null;
        alert = getAlertBox();
        if (alert != null) {
            alert.accept();
            CSLogger.info("Clicked on ok of alert pop up");
        } else {
            CSLogger.error("While dialog Action is '" + actionOption
                    + "' and option is '" + optionValue
                    + "' Alert pop up does not appears : teststep failed");
            softAssertion.fail("While dialog Action is '" + actionOption
                    + "' and option is '" + optionValue
                    + "' Alert pop up does not appears : teststep failed");
        }
    }

    /**
     * This method returns the alert box.
     * 
     * @return Alert Instance of Alert class representing the DOM alert box.
     */
    private Alert getAlertBox() {
        try {
            Alert alertBox;
            CSUtility.tempMethodForThreadSleep(5000);
            alertBox = browserDriver.switchTo().alert();
            CSLogger.info("Switched to alert dialouge box.");
            return alertBox;
        } catch (NoAlertPresentException e) {
            return null;
        }
    }

    /**
     * Performs verification whether product moved.
     * 
     * @param parentProduct String Object contains name of product.
     * @param childProduct String Object contains name of child product.
     * @param moveChildProduct String Object contains name of product.
     * @param pressOkay Contains boolean values i.e true or false.
     * @param expectedAttachedReference String contains name of expected
     *            attached reference attribute.
     * @param referenceAttributeName String contains name of reference
     *            attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     */
    private void verifyProductMoved(String parentProduct, String childProduct,
            String moveChildProduct, Boolean pressOkay,
            String expectedAttachedReference, String referenceAttributeName,
            String productTabName, String actionOption, String optionValue) {
        goToPimStudioTreeSection();
        CSUtility.tempMethodForThreadSleep(5000);
        int productMoved = browserDriver.findElements(
                By.xpath("//span[contains(text(),' " + parentProduct
                        + "')]/../../../../../../../../../../../following-siblin"
                        + "g::span[1]/span/table/tbody/tr/td/span/table/tbody/tr"
                        + "/td[2]/a/span/span[contains(text(),'" + childProduct
                        + "')]"))
                .size();
        if (pressOkay) {
            if (productMoved != 0) {
                CSLogger.error(
                        "Product not moved when clicked on ok : teststep failed");
                softAssertion.fail(
                        "Product not moved when clicked on ok : teststep failed");
            } else {
                CSLogger.info(
                        "Product moved successfully when clicked on ok : teststep passed");
            }
            verifyInheritedValuesOfNewParentProduct(moveChildProduct,
                    expectedAttachedReference, referenceAttributeName,
                    productTabName, actionOption, optionValue);
        } else {
            if (productMoved != 0) {
                CSLogger.error(
                        "Product moved when clicked on cancel : teststep failed");
                softAssertion.fail(
                        "Product moved when clicked on cancel : teststep failed");
            } else {
                CSLogger.info(
                        "Product not moved when clicked on cancel : teststep passed");
            }
        }
    }

    /**
     * Verifies that moved product inherites the values of parent product.
     * 
     * @param childProductName String Object contains name of child product.
     * @param expectedAttachedReference String contains name of expected
     *            attached reference attribute.
     * @param referenceAttributeName String contains name of reference
     *            attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     */
    private void verifyInheritedValuesOfNewParentProduct(
            String childProductName, String expectedAttachedReference,
            String referenceAttributeName, String productTabName,
            String actionOption, String optionValue) {
        try {
            goToPimStudioTreeSection();
            WebElement childProduct = getProduct(childProductName);
            childProduct.click();
            goToRightSectionWindow();
            CSUtility.tempMethodForThreadSleep(1000);
            clickOnTheGivenProductTab(productTabName, waitForReload);
            CSUtility.tempMethodForThreadSleep(1000);
            int valueInherited = browserDriver.findElements(
                    By.xpath("//tr[contains(@cs_name,'" + referenceAttributeName
                            + "')]/td[2]/div/div/div/table/tbody/tr/td[2]/div/a"
                            + "[contains(text(),'" + expectedAttachedReference
                            + "')]"))
                    .size();
            if (valueInherited != 0) {
                CSLogger.info(
                        "Child product has proper inherited  reference attribute"
                                + " values while dialog Action " + "is '"
                                + actionOption + "' and option is '"
                                + optionValue + ": teststep passed");
            } else {
                CSLogger.error(
                        "Child product does not have proper inherited reference"
                                + " attribute values while dialog Action "
                                + "is '" + actionOption + "' and option is '"
                                + optionValue + ": teststep failed");
                softAssertion
                        .fail("Child product does not have proper inherited"
                                + " reference attribute values while dialog "
                                + "Action " + "is '" + actionOption
                                + "' and option is '" + optionValue
                                + ": teststep failed");
            }
        } catch (Exception e) {
            CSLogger.error("The editor window does not open when clicked on "
                    + "child product", e);
            softAssertion
                    .fail("The editor window does not open when clicked on "
                            + "child product");
        }
    }

    /**
     * Verifies the Copy operation.
     * 
     * @param linkProduct String Object contains name of product.
     * @param childProduct String Object contains name of child product.
     * @param pressOkay Contains boolean values i.e true or false
     * @param expectedAttachedReference String contains name of expected
     *            attached reference attribute.
     * @param referenceAttributeName String contains name of reference
     *            attribute.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     */
    private void verifyProductCopied(String linkProduct, String childProduct,
            Boolean pressOkay, String expectedAttachedReference,
            String referenceAttributeName, String productTabName,
            String actionOption, String optionValue) {
        goToPimStudioTreeSection();
        int productCopied = browserDriver
                .findElements(By.xpath("//span[contains(text(),' " + linkProduct
                        + "')]/../../../../../../../../../../../following-siblin"
                        + "g::span[1]/span/table/tbody/tr/td/span/table/tbody/tr"
                        + "/td[2]/a/span/span[contains(text(),'" + "Copy of "
                        + childProduct + "')]"))
                .size();
        if (pressOkay) {
            if (productCopied != 0) {
                CSLogger.info(
                        "Product copied successfully when clicked on ok : "
                                + "teststep passed");
            } else {
                CSLogger.error(
                        "Product not copied when clicked on ok : teststep failed");
                softAssertion.fail(
                        "Product not copied when clicked on ok : teststep failed");
            }
            String copiedChildProductName = "Copy of " + childProduct;
            verifyInheritedValuesOfNewParentProduct(copiedChildProductName,
                    expectedAttachedReference, referenceAttributeName,
                    productTabName, actionOption, optionValue);
        } else {
            if (productCopied != 0) {
                CSLogger.error(
                        "Product copied when clicked on cancel : teststep failed");
                softAssertion.fail(
                        "Product copied when clicked on cancel : teststep failed");
            } else {
                CSLogger.info(
                        "Product not copied when clicked on cancel : teststep passed");
            }
        }
    }

    /**
     * Verifies the usecase when 'Set Reference' option is selected from dialog
     * box.
     * 
     * @param linkProductName String Object contains name of product.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param expectedAttachedReference String contains name of expected
     *            attached reference attribute.
     * @param referenceAttributeName String contains name of reference
     *            attribute.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     * @param pressOkay Contains boolean values true or false.
     */
    private void verifySetReference(String linkProductName,
            String productTabName, String expectedAttachedReference,
            String referenceAttributeName, String actionOption,
            String optionValue, Boolean pressOkay) {
        goToReferenceAttributeTab(linkProductName, productTabName);
        int productReferenceSet = browserDriver.findElements(
                By.xpath("//tr[contains(@cs_name,'" + referenceAttributeName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td[2]/div/a[contains(text(),'"
                        + expectedAttachedReference + "')]"))
                .size();
        if (pressOkay) {
            if (productReferenceSet != 0) {
                CSLogger.info(" While dialog Action " + "is '" + actionOption
                        + "' and option is '" + optionValue
                        + "' and clicked on OK of 'Move Product' dialog ,"
                        + "child product added as reference in 'Reference to PIM' "
                        + "attribute of parent folder : teststep passed");
            } else {
                CSLogger.error("While dialog Action " + "is '" + actionOption
                        + "' and option is '" + optionValue
                        + "' and clicked on OK of 'Move Product' dialog ,child "
                        + "product not added as reference in 'Reference to PIM'"
                        + " attribute of parent folder : teststep failed");
                softAssertion.fail("While dialog Action " + "is '"
                        + actionOption + "' and option is '" + optionValue
                        + "' and clicked on OK of 'Move Product' dialog ,child "
                        + "product not added as reference in 'Reference to PIM'"
                        + " attribute of parent folder : teststep failed");
            }
        } else {
            if (productReferenceSet != 0) {
                CSLogger.error("While dialog Action " + "is '" + actionOption
                        + "' and option is '" + optionValue
                        + "child product not added as reference in 'Reference to PIM'"
                        + " attribute of parent folder : teststep failed");
                softAssertion.fail(" While dialog Action " + "is '"
                        + actionOption + "' and option is '" + optionValue
                        + "child product not added as reference in 'Reference to PIM' "
                        + "attribute of parent folder : teststep failed");
            } else {
                CSLogger.info("While dialog Action " + "is '" + actionOption
                        + "' and option is '" + optionValue
                        + "child product not added as reference in 'Reference to PIM' "
                        + "attribute of parent folder : teststep passed");
            }
        }
    }

    /**
     * Verifies the usecase when 'Set Reference' option is selected from dialog
     * box.
     * 
     * @param linkProductName String Object contains name of product.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param existingAttachedReferece String contains name of existing attached
     *            reference attribute.
     * @param expectedAttachedReference String contains name of expected
     *            attached reference attribute.
     * @param referenceAttributeName String contains name of reference
     *            attribute.
     * @param actionOption Specifies option from "Move product" dialog box i.e
     *            'Copy Selection','Move Selection'.
     * @param optionValue Specifies option from "Move product" dialog box i.e
     *            'Use inherited values of new parent folder','Transfer
     *            inherited values'.
     * @param pressOkay Boolean Parameter contains true or false values.
     */
    private void verifyAddReference(String linkProductName,
            String productTabName, String existingAttachedReferece,
            String expectedAttachedReference, String referenceAttributeName,
            String actionOption, String optionValue, Boolean pressOkay) {
        goToReferenceAttributeTab(linkProductName, productTabName);
        int existingAttachedReference = browserDriver.findElements(
                By.xpath("//tr[contains(@cs_name,'" + referenceAttributeName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td[2]/div/a[contains(text(),'"
                        + existingAttachedReferece + "')]"))
                .size();

        int addedProductReference = browserDriver.findElements(
                By.xpath("//tr[contains(@cs_name,'" + referenceAttributeName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td[2]/div/a[contains(text(),'"
                        + expectedAttachedReference + "')]"))
                .size();

        if (pressOkay) {
            if (addedProductReference != 0 && existingAttachedReference != 0) {
                CSLogger.info(" While dialog Action " + "is '" + actionOption
                        + "' and option is '" + optionValue
                        + "' and clicked on OK of 'Move Product' dialog ,"
                        + "child product added as reference in 'Reference to PIM' "
                        + "attribute of parent folder : teststep passed");
            } else {
                CSLogger.error("While dialog Action " + "is '" + actionOption
                        + "' and option is '" + optionValue
                        + "' and clicked on OK of 'Move Product' dialog ,child "
                        + "product not added as reference in 'Reference to PIM'"
                        + " attribute of parent folder : teststep failed");
                softAssertion.fail("While dialog Action " + "is '"
                        + actionOption + "' and option is '" + optionValue
                        + "' and clicked on OK of 'Move Product' dialog ,child "
                        + "product not added as reference in 'Reference to PIM'"
                        + " attribute of parent folder : teststep failed");
            }
        } else {
            if (addedProductReference != 0 && existingAttachedReference == 0) {
                CSLogger.error("While dialog Action " + "is '" + actionOption
                        + "' and option is '" + optionValue
                        + "child product not added as reference in 'Reference to PIM'"
                        + " attribute of parent folder : teststep failed");
                softAssertion.fail(" While dialog Action " + "is '"
                        + actionOption + "' and option is '" + optionValue
                        + "child product not added as reference in 'Reference to PIM' "
                        + "attribute of parent folder : teststep failed");
            } else {
                CSLogger.info("While dialog Action " + "is '" + actionOption
                        + "' and option is '" + optionValue
                        + "child product not added as reference in 'Reference to PIM' "
                        + "attribute of parent folder : teststep passed");
            }
        }
    }

    /**
     * Clicks on tab that holds all assigned reference attributes.
     * 
     * @param linkProductName String Object contains name of product.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     */
    private void goToReferenceAttributeTab(String linkProductName,
            String productTabName) {
        goToPimStudioTreeSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(linkProductName)));
        WebElement linkProduct = getProduct(linkProductName);
        Actions action = new Actions(browserDriver);
        action.doubleClick(linkProduct).perform();
        goToRightSectionWindow();
        clickOnTheGivenProductTab(productTabName, waitForReload);
    }

    /**
     * Enables the 'Multiple Selection' option of reference attribute
     */
    private void enableMultipleSelectionOptionOfReferenceAttribute() {
        csGuiDialogContentIdInstance.checkCbMultipleSelection(waitForReload);
    }

    /**
     * This test method performs to move child products and link it to other
     * products with different action i.e Copy selection,Move
     * selection,Reference to pim.
     * 
     * @param moveParentProductName String Object contains product name under
     *            which child product will be created.
     * @param linkProductName String Object contains name of product.
     * @param movechildProductName String Object contains name of child
     *            products.
     * @param firstReferenceProduct String Object contains name of product that
     *            will be refered in another product by reference attribute.
     * @param secondReferenceProduct String Object contains name of product that
     *            will be refered in another product by reference attribute.
     * @param attributeFolderName String Object contains name of attribute
     *            folder.
     * @param pimReferenceAttrName String Object contains name of pim reference
     *            attribute.
     * @param attributeType String Object contains type of reference attribute
     *            i.e Reference to PIM Product
     * @param className String Object contains name of class.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param parentProdSettings contains the product names
     * @param childProdSettings contains the child names
     * 
     */
    @Test(priority = 2, dataProvider = "moveOrLinkTestData")
    public void testSettingsOff(String moveParentProductName,
            String linkProductName, String movechildProductName,
            String firstReferenceProduct, String secondReferenceProduct,
            String attributeFolderName, String pimReferenceAttrName,
            String attributeType, String className, String productTabName,
            String parentProdSettings, String childProdSettings) {
        try {
            String[] parentProducts = parentProdSettings.split(",");
            String[] childProducts = childProdSettings.split(",");
            executePrerequisites(parentProducts, childProducts);
            perfomUseCase(parentProducts[0], childProducts[0],
                    parentProducts[1], childProducts[1]);
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testMoveOrLinkProduct", e);
            Assert.fail("Automation error in: testMoveOrLinkProduct");
        }
    }

    private void perfomUseCase(String parentProduct1, String childProduct1,
            String parentProduct2, String childProduct2) {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
        disableExtendedDragDropCheckbox();
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.tempMethodForThreadSleep(5000);
        browserDriver.navigate().refresh();
        CSLogger.info("Refreshed the page");
        switchToPimAndExpandProductTree(waitForReload);
        performMoveProductOperation(parentProduct1, childProduct1,
                parentProduct2);
        verifyPresenceOfChildUnderProductNode(parentProduct2, childProduct1);

    }

    /**
     * This method verifies the presence of child product under product node
     * 
     * @param parentProduct2 contains name of parent product
     * @param childProduct1 contains name of child product
     * 
     */
    private void verifyPresenceOfChildUnderProductNode(String parentProduct2,
            String childProduct1) {
        goToPimStudioTreeSection();
        WebElement productName = getProduct(parentProduct2);
        productName.click();
        WebElement childName = getProduct(childProduct1);
        if (childName.getText().equals(childProduct1)) {
            CSLogger.info(
                    "Child product has successfully moved to Parent Product"
                            + " without dialog box");
        } else {
            CSLogger.error(
                    "Dialog box is present. Child product is not present after"
                            + " move operation without dialog box");
            softAssertion.fail(
                    "Child product is not present under parent product without "
                            + "dialog box.Dialog box is present");
        }
    }

    /**
     * This method executes the prerequisites for test case
     * 
     * @param parentProducts contains the string array containing parent product
     *            names
     * @param childProducts contains the string array containing child product
     *            names
     */
    private void executePrerequisites(String[] parentProducts,
            String[] childProducts) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                parentProducts[0]);
        createChildProduct(parentProducts[0], childProducts[0]);
        createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                parentProducts[1]);
        createChildProduct(parentProducts[1], childProducts[1]);
    }

    private void disableExtendedDragDropCheckbox() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        settingsPageInstance.clkOnBtnSettingsNode(waitForReload);
        traverseToSettingsRightSectionFrames();
        settingsPageInstance.clkOnBtnPim(waitForReload);
        settingsPageInstance.clkOnBtnPimSubTreeStudio(waitForReload);
        settingsPageInstance.clkOnBtnPimUserInterface(waitForReload);
        settingsPageInstance.expandDrpDwnUserInterfaceTree(waitForReload);
        settingsPageInstance.uncheckExtendedDragDrop(waitForReload);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This test method performs to move child products and link it to other
     * products with different action i.e Copy selection,Move
     * selection,Reference to pim.
     * 
     * @param moveParentProductName String Object contains product name under
     *            which child product will be created.
     * @param linkProductName String Object contains name of product.
     * @param movechildProductName String Object contains name of child
     *            products.
     * @param firstReferenceProduct String Object contains name of product that
     *            will be refered in another product by reference attribute.
     * @param secondReferenceProduct String Object contains name of product that
     *            will be refered in another product by reference attribute.
     * @param attributeFolderName String Object contains name of attribute
     *            folder.
     * @param pimReferenceAttrName String Object contains name of pim reference
     *            attribute.
     * @param attributeType String Object contains type of reference attribute
     *            i.e Reference to PIM Product
     * @param className String Object contains name of class.
     * @param productTabName String Object contains name of product tab in which
     *            reference attributes will be listed.
     * @param parentProdSettings contains the product names
     * @param childProdSettings contains the child names
     * 
     */
    @Test(priority = 3, dataProvider = "moveOrLinkTestData")
    public void testSettingsOffWithShiftKey(String moveParentProductName,
            String linkProductName, String movechildProductName,
            String firstReferenceProduct, String secondReferenceProduct,
            String attributeFolderName, String pimReferenceAttrName,
            String attributeType, String className, String productTabName,
            String parentProdSettings, String childProdSettings) {
        try {
            String[] parentProducts = parentProdSettings.split(",");
            String[] childProducts = childProdSettings.split(",");
            perfomUseCaseWithShiftKey(parentProducts[0], childProducts[0],
                    parentProducts[1], childProducts[1]);
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testMoveOrLinkProduct", e);
            Assert.fail("Automation error in: testMoveOrLinkProduct");
        }
    }

    /**
     * This method performs the use case with the shift key
     * 
     * @param parentProduct1 contains name of parent product 1
     * @param childProduct1 contains name of child product 1
     * @param ParentPrduct2 contains name of parent product 2
     * @param childProduct2 contains name of child product 1
     */
    private void perfomUseCaseWithShiftKey(String parentProduct1,
            String childProduct1, String ParentPrduct2, String childProduct2) {
        try {
            goToPimStudioTreeSection();
            WebElement sourceProductFolder = getProduct(ParentPrduct2);
            sourceProductFolder.click();
            CSUtility.tempMethodForThreadSleep(1000);
            Actions action = new Actions(browserDriver);
            WebElement moveChildProduct = getProduct(childProduct2);
            WebElement linkProduct = getProduct(parentProduct1);
            action.keyDown(Keys.SHIFT);
            Action drag = action.clickAndHold(moveChildProduct)
                    .moveToElement(linkProduct).build();
            drag.perform();
            Action drop = action.release().build();
            drop.perform();
            action.keyUp(Keys.SHIFT);
            checkPresenceOfPopup("Move selection",
                    "Use inherited values of new parent folder.");
        } catch (Exception e) {
            CSLogger.error("Test case failed for verification using shift key");
            softAssertion
                    .fail("Test case failed for verification using shift key");
        }
    }

    /**
     * This method checks the presence of pop up after drag drop
     * 
     * @param actionOption contains action value
     * @param optionValue contains option value
     */
    private void checkPresenceOfPopup(String actionOption, String optionValue) {
        try {
            CSUtility.traverseToCsPortalWindowContent(waitForReload,
                    browserDriver);
            WebElement actionElement = browserDriver.findElement(
                    By.xpath("//select[contains(@id,'RecordMethod')]"));
            selectValueFromDrpDwnElement(actionElement, actionOption);
            WebElement optionElement = browserDriver.findElement(
                    By.xpath("//select[contains(@id,'RecordBehavior')]"));
            selectValueFromDrpDwnElement(optionElement, optionValue);
            browserDriver
                    .findElement(
                            By.className("CSGUI_MODALDIALOG_INPUT_BUTTON_OK"))
                    .click();
            CSLogger.info(
                    "Verified. The action and option are selectable from the drop down");
        } catch (Exception e) {
            CSLogger.error("Dialog box did not appear . Test case failed");
            softAssertion.fail("Dialog box did not appear . Test case failed");
        }
    }
    
    private void refreshPage() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.tempMethodForThreadSleep(5000);
        browserDriver.navigate().refresh();
        CSLogger.info("Refreshed the page");

    }

    /**
     * This is a data provider method.
     * 
     * @return Array
     */
    @DataProvider(name = "moveOrLinkTestData")
    public Object[][] getMoveOrLinkTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"), dataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        waitForReload = new WebDriverWait(browserDriver, 180);
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
        csPortalWindowInstance = new CSPortalWindow(browserDriver);
        settingsPageInstance = new SettingsPage(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
    }
}
