/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.CSPortalWindow;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.pom.UsagesPage;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
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
 * This class contains the test methods to verify usages control works for
 * product.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyControlUsagesTest extends AbstractTest {

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
    private UsagesPage                usagesPageInstance;
    private String                    dataDrivenSheetName = "VerifyControlUsages";
    private IMoreOptionPopup          moreOptionPopup;

    /**
     * This test method verifies whether usages for products works
     * 
     * @param attributeFolderName String Object containing attribute folder
     *            name.
     * @param firstReferenceAttribute String Object contains name and type of
     *            reference attribute eg refToPim:Reference to PIM product .
     * @param secondReferenceAttribute String Object contains name and type of
     *            reference attribute.
     * @param className String Object contains name of class.
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param childProductName String Object containing child product name.
     * @param productTabName tring Object containing name of the tab.
     */
    @Test(priority = 1, dataProvider = "verifyUsagesTestData")
    public void testVerifyControlUsages(String attributeFolderName,
            String firstReferenceAttribute, String secondReferenceAttribute, String className,
            String parentProductName, String childProductName,
            String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandSettingsTree(waitForReload);
            createAttributeFolder(attributeFolderName);
            createDifferentReferenceAttributes(attributeFolderName,
                    firstReferenceAttribute.split(":")[0],
                    firstReferenceAttribute.split(":")[1], productTabName);
            createDifferentReferenceAttributes(attributeFolderName,
                    secondReferenceAttribute.split(":")[0],
                    secondReferenceAttribute.split(":")[1], productTabName);
            createClass(className);
            assignAttrFolderToClass(className, attributeFolderName);
            String parentProductId = createParentProduct(waitForReload,
                    pimStudioProductsNodeInstance, parentProductName);
            goToPimStudioTreeSection();
            createChildProduct(parentProductName, childProductName);
            assignClassToParentProduct(parentProductName, className);
            assignReferencesToAttributes(parentProductName,
                    firstReferenceAttribute, productTabName);
            assignReferencesToAttributes(parentProductName,
                    secondReferenceAttribute, productTabName);
            goToRightSectionWindow();
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            String assignedProduct = getAssignedProductName(
                    firstReferenceAttribute.split(":")[0]);
            String assignedMamFile = getAssignedMamFileName(
                    secondReferenceAttribute.split(":")[0]);
            enablePimStudioAndMamStudioUsageButton();
            switchToPimAndExpandProductTree(waitForReload);
            openProductFolderInEditor(parentProductName);
            String parentWindow = verifyUsageControlWindow(parentProductName,
                    assignedProduct, assignedMamFile);
            performAssignedReferenceUsageOperation(parentProductId, "Product",
                    assignedProduct,
                    usagesPageInstance.getReferenceToTreeProductSubMenu(),
                    moreOptionPopup.getCtxMoreOptionProduct());
            performAssignedReferenceUsageOperation(parentProductId, "File",
                    assignedMamFile,
                    usagesPageInstance.getReferenceToTreeFileSubMenu(),
                    moreOptionPopup.getCtxMoreOptionFile());
            browserDriver.close();
            browserDriver.switchTo().window(parentWindow);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in testVerifyControlUsages", e);
            Assert.fail("Automation error in testVerifyControlUsages");
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
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
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
     * This method creates a attribute folder of name passed as argument
     * 
     * @param attributeFolderName String object containing name of attribute
     *            folder
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
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * Creates a text attribute in given attribute folder and sets the
     * searchable attribute field with option passed as argument
     * 
     * @param attributeFolderName String object containing name of attribute
     *            folder
     * @param referenceAttrName String object containing text attribute name
     * 
     */
    private void createDifferentReferenceAttributes(String attributeFolderName,
            String referenceAttrName, String referenceAttributeType,
            String productTabName) {
        createAttribute(attributeFolderName, referenceAttrName);
        goToPimStudioTreeSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(referenceAttrName)));
        WebElement textAttribute = browserDriver
                .findElement(By.linkText(referenceAttrName));
        textAttribute.click();
        CSLogger.info("Clicked on text attribute " + referenceAttrName);
        goToRightSectionWindow();
        csGuiDialogContentIdInstance.clkBtnCSTypeLabel(waitForReload);
        selectAttributeType(
                csPortalWindowInstance.getAttributeType(referenceAttributeType),
                referenceAttributeType);
        goToRightSectionWindow();
        csGuiDialogContentIdInstance.enterDataForTextAttributeFiled(
                waitForReload,
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationPaneTitle(),
                productTabName);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Attribute named " + referenceAttrName
                + " created of type " + referenceAttributeType);
    }

    /**
     * This method creates a single line text attribute
     * 
     * @param attributeFolderName String object containing name of attribute
     *            folder
     * @param txtAttrName String object containing text attribute name
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
     * clicks on attribute type Cross Reference.
     * 
     * @param attributeType type of attribute for eg dropdown or cross reference
     *            type
     * @param technicalName contains string value for technical attribute name
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
     * This method creates a given class.
     * 
     * @param className String object containing name of class
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
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Attribute folder " + attributeFolderName
                + "assigned to class: " + className + "successfully");
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload WebDriverWait Object
     * @param pimStudioProductsNode PimStudioProductsNode Object
     * @param parentProductName String Object containing the value for parent
     *            product Name.
     */
    private String createParentProduct(WebDriverWait waitForReload,
            PimStudioProductsNodePage pimStudioProductsNode,
            String parentProductName) {
        goToProductsNode();
        createProduct(parentProductName,
                pimStudioProductsNode.getBtnPimProductsNode(),
                csPopupDiv.getCsGuiPopupMenuCreateNew());
        CSLogger.info("Created Parent product");
        String parentProductId = getParentProductId(parentProductName);
        return parentProductId;
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
        WebElement element = pimStudioProductsNodeInstance
                .expandNodesByProductName(waitForReload, parentProductName);
        createProduct(childProductName, element,
                csPopupDiv.getCsGuiPopupMenuNewChild());
        CSLogger.info("Created new child product");
    }

    /**
     * Assigns the given class to the parent product folder
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param className String object containing name of the class
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
     * Performs operation of double clicking on parent product
     * 
     * @param ProductName String Object contains the product name
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
     * Returns the WebElement of product
     * 
     * @param productName String containing name of either product folder or
     *            product
     * @return WebElement Product
     */
    private WebElement getProduct(String productName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productName)));
        return browserDriver.findElement(By.linkText(productName));
    }

    /**
     * selects the class of given classname that will be assigned to the parent
     * product folder
     * 
     * @param className String object containing name of the class
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
     * * This method checks whether the reference type of attribute are editable
     * or not
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param referenceAttributeNameValuePair Attribute's name and value pair
     *            separated with colon and type of single line text attribute
     *            eg.singleLineTextAttr1:ABC
     * @param referenceTabName String Object contains tab name
     */
    private void assignReferencesToAttributes(String productFolderName,
            String referenceAttributeNameValuePair, String referenceTabName) {
        String[] referenceAttributeNameValueData = referenceAttributeNameValuePair
                .split(",");
        String[] referenceAttributeNameValue = null;
        for (int i = 0; i < referenceAttributeNameValueData.length; i++) {
            referenceAttributeNameValue = referenceAttributeNameValueData[i]
                    .split(":");
            handleAssignWindows(referenceAttributeNameValue[0],
                    referenceAttributeNameValue[1], productFolderName,
                    referenceTabName);
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
     * @param referenceTabName String Object contains tab name
     */
    private void handleAssignWindows(String attributeName, String attributeType,
            String productFolderName, String referenceTabName) {
        clickOnTheGivenProductTab(referenceTabName);
        switch (attributeType) {
        case "Reference to PIM Product":
            referenceToPimProduct(attributeName, productFolderName, false);
            referenceToPimProduct(attributeName, productFolderName, true);
            break;
        case "Reference to MAM File":
            referenceToMamFile(productFolderName, attributeName, false);
            referenceToMamFile(productFolderName, attributeName, true);
            break;
        case "Reference to CORE User":
            referenceToCoreUser(productFolderName, attributeName, false);
            referenceToCoreUser(productFolderName, attributeName, true);
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
     * 
     */
    private void clickOnTheGivenProductTab(String productTabName) {
        goToRightSectionWindow();
        CSUtility.tempMethodForThreadSleep(3000);
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
     * @param attributeName String Object contains name of attribute
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param pressOkay Boolean parameter that contains true and false values
     *            * @param referenceTabName String Object contains tab name
     */
    private void referenceToPimProduct(String attributeName,
            String productFolderName, Boolean pressOkay) {
        clkOnCSGuiSelectionListAdd(attributeName);
        selectReferenceToPimProductType(productFolderName, pressOkay,
                attributeName, waitForReload, browserDriver);
    }

    /**
     * This method clicks on plus
     * 
     * @param attributeName Contains name of attribute
     */
    private void clkOnCSGuiSelectionListAdd(String attributeName) {
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
     * @param attributeName String Object contains name of attribute
     * @param referenceTabName String Object contains tab name
     * @param waitForReload WebDriverWait Object
     * @param browserDriver Instance of browerDriver
     */
    private void selectReferenceToPimProductType(String productFolderName,
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
        pimProduct.click();
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
     * Checks whether the product is assigned to the attribute or not
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param attributeName String Object contains name of attribute
     * @param waitForReload WebDriverWait Object
     * @param browserDriver Instance of browerDriver
     */
    private void verifyPresenceOfAssignedProduct(String productFolderName,
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
     * Verifies that after clicking on cancel button the product is not assigned
     * to that attribute
     * 
     * @param attributeName String Object contains name of attribute
     * @param waitForReload WebDriverWait Object
     * 
     */
    private void verifyAbsenceOfAssignedProduct(String attributeName,
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
     * Assigns Mam file to Mam attribute
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param attributeName Contains name of attribute
     * @param pressOkay Boolean parameter that contains true and false values
     */
    private void referenceToMamFile(String productFolderName,
            String attributeName, Boolean pressOkay) {
        clkOnCSGuiSelectionListAdd(attributeName);
        selectReferenceToMamProductType(productFolderName, pressOkay,
                attributeName);
    }

    /**
     * Clicks on plus of Mam attribute
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param pressOkay Boolean parameter that contains true and false values
     * @param attributeName String Object contains name of attribute
     * @param waitForReload WebDriverWait Object
     * @param browserDriver Instance of browerDriver
     */
    private void selectReferenceToMamProductType(String productFolderName,
            Boolean pressOkay, String attributeName) {
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
     * @param attributeName String Object contains name of attribute
     * @param waitForReload WebDriverWait Object
     * 
     */
    private void verifyPresenceOfAssignedMamFile(String assignedFirstImage,
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
     * @param waitForReload WebDriverWait Object
     * 
     */
    private void verifyAbsenceOfAssignedMamFile(String attributeName,
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
    private void referenceToCoreUser(String productFolderName,
            String attributeName, Boolean pressOkay) {
        clkOnCSGuiSelectionListAdd(attributeName);
        selectReferenceToCoreUserType(productFolderName, pressOkay,
                attributeName);
    }

    /**
     * Selects reference of core user type
     * 
     * @param productFolderName String parameter that contains name of the
     *            product folder
     * @param pressOkay Boolean parameter that contains true and false values
     * @param attributeName Contains name of attribute
     */
    private void selectReferenceToCoreUserType(String productFolderName,
            Boolean pressOkay, String attributeName) {
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
        CSUtility.tempMethodForThreadSleep(5000);
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
     * @param assignedUserName String Object contains name of user that needs to
     *            be assigned.
     * @param attributeName String Object contains name of attribute.
     * @param waitForReload WebDriverWait Object.
     * @param waitForReload WebDriverWait Object
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
                            .xpath("(//a[@class='CSGuiThumbListDataText']/span)[2]"))
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
     * Checks whether the User is not assigned to the attribute or not after
     * clicking on cancel button
     * 
     * @param attributeName String parameter contains name of attribute.
     * @param waitForReload WebDriverWait Object.
     */
    private void verifyAbsenceOfAssignedCoreUser(String attributeName,
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
     * Selects the view folder as editor sub menu option of products
     * 
     * @param productName String Object contains name of product
     */
    private void openProductFolderInEditor(String productName) {
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement parentProduct = getProduct(productName);
        CSUtility.rightClickTreeNode(waitForReload, parentProduct,
                browserDriver);
        productPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupMenuObject());
        productPopup.selectPopupDivSubMenu(waitForReload,
                productPopup.getCsGuiPopupMenuSubMenuViewFolderAsEditor(),
                browserDriver);
    }

    /**
     * Verifies the usage control window
     * 
     * @param parentProductName String Object contains the product name
     * @param assignedProductName Name of the product assigned to reference
     *            attribute
     * @param assignedFileName Name of the file assigned to reference attribute
     * @param assignedCoreUserName Name of the User assigned to reference
     *            attribute
     * @return parentWindow
     */
//    private String verifyUsageControlWindow(String parentProductName,
//            String assignedProductName, String assignedFileName,
//            String assignedCoreUserName) 
    private String verifyUsageControlWindow(String parentProductName,
            String assignedProductName, String assignedFileName) {
        goToRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//a[contains(text(),'" + parentProductName + "')]")));
        goToRightSectionWindow();
        goToUsageOption(moreOptionPopup.getCtxMoreOptionProduct());
        moreOptionPopup.selectPopupDivSubMenu(waitForReload,
                moreOptionPopup.getCtxUsages(), browserDriver);
        String parentWindow = browserDriver.getWindowHandle();
        switchToUsagesWindow();
        traverseToUsagesPageLeftSection();
        verifyTabsInUsagesWindow();
        usagesPageInstance.clkOnReferenceToMenu(waitForReload);
        verifyAssignedProduct(assignedProductName);
        verifyAssignedFile(assignedFileName);
//        verifyAssignedCoreUser(assignedCoreUserName);
        return parentWindow;
    }

    /**
     * Verifies that in usage window the assigned product display's
     * 
     * @param assignedProductName Name of the product assigned to reference
     *            attribute
     */
    private void verifyAssignedProduct(String assignedProductName) {
        usagesPageInstance.clkOnReferenceToTreeProductSubMenu(waitForReload);
        traverseToUsagesPageCenterSection();
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//table[@class='CSGuiList ']")));
        WebElement getTableData = browserDriver.findElement(By.xpath(
                "//table[@class='CSGuiList ']/tbody/tr[2]/td/div/div/div/table/tbody/tr[@class]/td[3]"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) getTableData
                .findElements(By.xpath("//td"));
        int count = 0;
        for (WebElement row : rows) {
            if (row.getText().contains(assignedProductName)) {
                CSLogger.info(
                        "In 'Product' tab - Assigned product is  listed : "
                                + "teststep passed");
                count++;
            }
            if (count != 0)
                break;
        }
        if (count == 0) {
            CSLogger.error(
                    "In 'Product' tab - Assigned product is not listed : "
                            + "teststep failed");
            softAssertion
                    .fail("In 'Product' tab - Assigned product is not listed : "
                            + "teststep failed");
        }
    }

    /**
     * Verifies that in usage window the assigned file display's.
     * 
     * @param assignedFileName Name of the file assigned to reference attribute.
     */
    private void verifyAssignedFile(String assignedFileName) {
        traverseToUsagesPageLeftSection();
        usagesPageInstance.clkOnReferenceToTreeFileSubMenu(waitForReload);
        traverseToUsagesPageCenterSection();
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//table[@class='CSGuiList ']")));
        WebElement getTableData = browserDriver.findElement(By.xpath(
                "//table[@class='CSGuiList ']/tbody/tr[2]/td/div/div/div/table/tbody/tr[@class]/td[3]"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) getTableData
                .findElements(By.xpath("//td"));
        int count = 0;
        for (WebElement row : rows) {
            if (row.getText().contains(assignedFileName)) {
                CSLogger.info("In 'File' tab - Assigned File is  listed : "
                        + "teststep passed");
                count++;
            }
            if (count != 0)
                break;
        }
        if (count == 0) {
            CSLogger.error("In 'File' tab - Assigned File is not listed : "
                    + "teststep failed");
            softAssertion.fail("In 'File' tab - Assigned File is not listed : "
                    + "teststep failed");
        }
    }

    /**
     * Switches till frmleft frame.
     */
    private void traverseToUsagesPageLeftSection() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameLeft()));
        CSLogger.info("Traversed till frmleft frame");
    }

    /**
     * Switches till frmCenter frame.
     */
    private void traverseToUsagesPageCenterSection() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameFrmCenter()));
        CSLogger.info("Traversed till frmCenter frame");
    }

    /**
     * Switches the control to usages window
     */
    private void switchToUsagesWindow() {
        String usageWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(usageWindow);
    }

    /**
     * Verifies that usages control window display's three tabs: Usage in,No
     * Usage in & Reference to.
     */
    private void verifyTabsInUsagesWindow() {
        if ((usagesPageInstance.getReferenceToTree()).isDisplayed()
                && (usagesPageInstance.getNoUsageInTree()).isDisplayed()
                && (usagesPageInstance.getUsageInTree()).isDisplayed()) {
            CSLogger.info(
                    "Usages control window  opened with three tabs: Usage in,"
                            + "No Usage in & Reference to :teststep passed ");
        } else {
            CSLogger.error(
                    "Error in test step - Usages control window  opened with "
                            + "three tabs: Usage in,"
                            + "No Usage in & Reference to :teststep failed ");
            softAssertion
                    .fail("Error in test step - Usages control window  opened with three tabs: Usage in,"
                            + "No Usage in & Reference to :teststep failed ");
        }
    }

    /**
     * Returns the name of file that is assigned to Reference to MAM file
     * attribute.
     * 
     * @param mamReferenceAttrName String contains name of 'Reference to MAM
     *            file' attribute
     * @return assignedFile
     */
    private String getAssignedMamFileName(String mamReferenceAttrName) {
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//td[@class='CSGuiThumbListData CSGuiThumbListData"
                        + "Single']/div/a")));
        String assignedFileName = browserDriver.findElement(
                By.xpath("//tr[contains(@cs_name,'" + mamReferenceAttrName
                        + "')]/td[2]/div/div/div/table/tbody/tr/"
                        + "td[2]/div/a"))
                .getText();
        return assignedFileName;
    }

    /**
     * Returns the name of product that is assigned to 'Reference to Pim
     * Product' attribute.
     * 
     * @param pimReferenceAttrName String contains name of 'Reference to Pim
     *            Product' attribute.
     * @return assignedProduct
     */
    private String getAssignedProductName(String pimReferenceAttrName) {
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[contains(@cs_name,'" + pimReferenceAttrName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td[2]/div/a")));

        String assignedProductName = browserDriver.findElement(
                By.xpath("//tr[contains(@cs_name,'" + pimReferenceAttrName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td"
                        + "[2]/div/a"))
                .getText();
        return assignedProductName;
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
        CSUtility.scrollUpOrDownToElement(settingsPageInstance.getBtnCore(), browserDriver);
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
     * Performs operation on usage window
     * 
     * @param parentProductId String Object contains created product ID.
     * @param subMenuOption String object contains sub menu option i.e
     *            Product,User,File.
     * @param assignedProductReference String Object contains assigned
     *            reference.
     * @param referenceType String object contains reference type.
     */
    public void performAssignedReferenceUsageOperation(String parentProductId,
            String subMenuOption, String assignedProductReference,
            WebElement referenceType, WebElement elementType) {
        traverseToUsagesPageLeftSection();
        referenceType.click();
        traverseToUsagesPageCenterSection();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//span[@class='ToolBarCaption'][contains(text(),'"
                                + subMenuOption + "')]")));
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//table[@class='CSGuiList ']")));
        if (subMenuOption.equals("Product") || subMenuOption.equals("User")) {
            WebElement getTableData = browserDriver.findElement(By.xpath(
                    "//table[@class='CSGuiList ']/tbody/tr[2]//td/div/div/div"));
            ArrayList<WebElement> rows = (ArrayList<WebElement>) getTableData
                    .findElements(By.xpath("//table/tbody/tr/td[3]"));
            int count = 0;
            for (WebElement row : rows) {
                if (row.getText().contains(assignedProductReference)) {
                    row.click();
                    count++;
                }
                if (count != 0)
                    break;
            }
        } else if (subMenuOption.equals("File")) {
            WebElement getTableData = browserDriver.findElement(By.xpath(
                    "//table[@class='CSGuiList ']/tbody/tr[2]//td/div/div/div"));
            ArrayList<WebElement> rows = (ArrayList<WebElement>) getTableData
                    .findElements(By.xpath("//table/tbody/tr/td[32]"));
            int count = 0;
            for (WebElement row : rows) {
                if (row.getText().contains(assignedProductReference)) {
                    row.click();
                    count++;
                }
                if (count != 0)
                    break;
            }
        }
        String usageParentWindow = browserDriver.getWindowHandle();
        if (checkUsagesButtonExists(elementType)) {
            String usageWindow = (String) (browserDriver.getWindowHandles()
                    .toArray())[2];
            browserDriver.switchTo().window(usageWindow);
            traverseToUsagesPageLeftSection();
            usagesPageInstance.clkOnUsageInMenu(waitForReload);
            usagesPageInstance.clkOnUsageInTreeProductSubMenu(waitForReload);
            traverseToUsagesPageCenterSection();
            int assignedReference = findAssignedProductReference(
                    parentProductId, assignedProductReference);
            if (assignedReference != 0) {
                CSLogger.info("When submenu " + subMenuOption
                        + " is selected , Product is  listed in the 'Usages' of "
                        + "assigned references : teststep passed");
            } else {
                CSLogger.error("When submenu " + subMenuOption
                        + " is selected , Product is not listed in the 'Usages' of "
                        + "assigned references : teststep failed");
                softAssertion.fail("When submenu " + subMenuOption
                        + " is selected , Product is not listed in the 'Usages' of "
                        + "assigned references : teststep failed");
            }
            browserDriver.close();
            browserDriver.switchTo().window(usageParentWindow);
        } else {
            softAssertion.fail("Usage option not found for " + subMenuOption);
            CSLogger.error("Usage option not found for " + subMenuOption);
        }
        CSUtility.switchToDefaultFrame(browserDriver);
        WebElement crossButton = browserDriver
                .findElement(By.xpath("//a[@class='CSPortalWindowCloser']"));
        crossButton.click();
        CSLogger.info("Clicked on cross button of window");
    }

    private int findAssignedProductReference(String parentProductId,
            String assignedProductReference) {
        enterTextInSearchBox(parentProductId);
        CSUtility.tempMethodForThreadSleep(1000);
        int assignedReference = browserDriver
                .findElements(By.xpath("//tr[@id='" + parentProductId + "']"))
                .size();
        return assignedReference;
    }

    private void enterTextInSearchBox(String searchText) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiToolbarHorizontalInstance.getTxtSearchBox());
        csGuiToolbarHorizontalInstance.getTxtSearchBox().click();
        csGuiToolbarHorizontalInstance.getTxtSearchBox().clear();
        csGuiToolbarHorizontalInstance.getTxtSearchBox().sendKeys(searchText);
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.getTxtSearchBox().sendKeys(Keys.ENTER);
        CSLogger.info("Entered text  " + searchText + "in search box");
    }

    /**
     * Checks whether usage button exists
     * 
     * @return
     */
    private Boolean checkUsagesButtonExists(WebElement elementType) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@name,'CSGuiDialog')]/div[2]")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSUtility.tempMethodForThreadSleep(1000);
        goToUsageOption(elementType);
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("CSPopupDiv_SUB")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
        int usageButtonExists = browserDriver
                .findElements(By.xpath("//td[@title='Usages']/..")).size();
        if (usageButtonExists != 0) {
            browserDriver.findElement(By.xpath("//td[@title='Usages']/.."))
                    .click();
            return true;
        } else
            return false;
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param parentProductName String containing name of parent product
     * @return parent product Id
     */
    private String getParentProductId(String parentProductName) {
        goToPimStudioTreeSection();
        Actions actions = new Actions(browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.linkText(parentProductName)));
        actions.doubleClick(
                browserDriver.findElement(By.linkText(parentProductName)))
                .perform();
        goToRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        return browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
    }

    /**
     * Selects the usages option.
     */
    private void goToUsageOption(WebElement elementType) {
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        moreOptionPopup.selectPopupDivMenu(waitForReload, elementType,
                browserDriver);
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of credentials
     */
    @DataProvider(name = "verifyUsagesTestData")
    public Object[][] getVerifyUsagesTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                dataDrivenSheetName);
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
        csPortalWindowInstance = new CSPortalWindow(browserDriver);
        settingsPageInstance = new SettingsPage(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        usagesPageInstance = new UsagesPage(browserDriver);
        moreOptionPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);

    }

}
