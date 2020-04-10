/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.elasticsearch;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.FlexTablePage;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which creates the Pre-requisites data
 * for elastic search uses.
 * 
 * @author CSAutomation Team
 */
public class CreateProductsAndAddDataTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private IProductPopup             productPopUp;
    private Actions                   action;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private IClassPopup               classPopUp;
    private FlexTablePage             flexTablePage;
    private String                    createProductAndAddDataTestData = "CreateProductAndAddData";

    /**
     * This method creates the Pre-requisites data for elastic search uses.
     * 
     * @param attrFolder String object contains attribute folder name
     * @param className String object contains class name
     * @param productToAdd String object contains product name
     * @param ProductTree String object contains products name
     * @param technicalName String object contains name of attribute
     * @param fieldType String object contains name of attribute type
     * @param slText String object contains single line text
     * @param numText String object contains number text
     * @param selectionType String object contains select option
     */
    @Test(dataProvider = "createProductAndAddDataSheet")
    public void testCreateProductsAndAddData(String attrFolder,
            String className, String productToAdd, String ProductTree,
            String technicalName, String fieldType, String slText,
            String numText, String selectionType) {
        try {
            String[] arrayOfProduct = ProductTree.split(",");
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createClass(className);
            dragAndDropAttrToClass(attrFolder, className);
            createProduct(productToAdd);
            createProductTree(className, ProductTree);
            addDataInProduct(productToAdd, arrayOfProduct[0], technicalName,
                    fieldType, slText, numText, selectionType);
            ChkInAndChkoutProduct(arrayOfProduct[0]);
            CSLogger.info("create products & add data test completed");
            CSUtility.tempMethodForThreadSleep(15000);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testCreateProductsAndAddData",
                    e);
            Assert.fail("Automation Error : testCreateProductsAndAddData", e);
        }
    }

    /**
     * This method Check In and check out the product.
     * 
     * @param productName
     */
    private void ChkInAndChkoutProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToChkIn = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.rightClickTreeNode(waitForReload, productToChkIn,
                browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuObject(), browserDriver);
        productPopUp.selectPopupDivSubMenu(waitForReload,
                productPopUp.getCsGuiPopupCheckIn(), browserDriver);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        csGuiDialogContentIdInstance.clkOnBtnRecursive(waitForReload);
        CSUtility.tempMethodForThreadSleep(5000);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToChkOut = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.rightClickTreeNode(waitForReload, productToChkOut,
                browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuObject(), browserDriver);
        productPopUp.selectPopupDivSubMenu(waitForReload,
                productPopUp.getCsGuiPopupCheckOut(), browserDriver);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        csGuiDialogContentIdInstance.clkOnBtnRecursive(waitForReload);
        CSLogger.info("Product check In And check Out");
    }

    /**
     * This method creates the Pre-requisites data for elastic search uses.
     * 
     * @param productToAdd String object contains product name
     * @param productName String object contains product name
     * @param technicalName String object contains name of attribute
     * @param fieldType String object contains name of attribute type
     * @param slText String object contains single line text
     * @param numText String object contains number text
     * @param selectionType String object contains select option
     */
    private void addDataInProduct(String productToAdd, String productName,
            String technicalName, String fieldType, String slText,
            String numText, String selectionType) {
        String[] attributeArray = technicalName.split(",");
        String[] fieldTypeArray = fieldType.split(",");
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToOpen = browserDriver
                .findElement(By.linkText(productName));
        action.doubleClick(productToOpen).build().perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        flexTablePage.clkOnDataTab(waitForReload);
        addDataInTableAttribute(productToAdd, technicalName, fieldType, slText,
                numText, selectionType, "Main");
        for (int count = 0; count < fieldTypeArray.length; count++) {
            if (fieldTypeArray[count].contains("Special")) {
                WebElement textElement = browserDriver.findElement(By.xpath(
                        "//tr[contains(@cs_name,'" + attributeArray[count]
                                + "')]" + "//td[2]//iframe[1]"));
                waitForReload.until(ExpectedConditions
                        .frameToBeAvailableAndSwitchToIt(textElement));
                csGuiToolbarHorizontalInstance
                        .clkBtnCSGuiToolbarCreateNew(waitForReload);
                CSUtility.tempMethodForThreadSleep(1000);
                TraverseSelectionDialogFrames
                        .traverseToCsPortalWindowContentFrame(waitForReload,
                                browserDriver);
                CSUtility.tempMethodForThreadSleep(1000);
                addDataInTableAttribute(productToAdd, technicalName, fieldType,
                        slText, numText, selectionType, "Sub");
                CSUtility.tempMethodForThreadSleep(1000);
                csGuiToolbarHorizontalInstance
                        .clkBtnCSGuiToolbarSave(waitForReload);
                CSUtility.switchToDefaultFrame(browserDriver);
                flexTablePage.clkOnBtnWindowClose(waitForReload);
            }
        }
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Data Entered in the Product.");
    }

    /**
     * This method creates the Pre-requisites data for elastic search uses.
     * 
     * @param productToAdd String object contains product name
     * @param technicalName String object contains name of attribute
     * @param fieldType String object contains name of attribute type
     * @param slText String object contains single line text
     * @param numText String object contains number text
     * @param selectionType String object contains select option
     * @param window String object contains window name
     */
    private void addDataInTableAttribute(String productToAdd,
            String technicalName, String fieldType, String slText,
            String numText, String selectionType, String window) {
        String[] attributeArray = technicalName.split(",");
        String[] fieldTypeArray = fieldType.split(",");
        WebElement textElement = null;
        String path = null;
        WebElement attrElement = null;
        for (int count = 0; count < fieldTypeArray.length; count++) {
            path = "//tr[contains(@cs_name,'" + attributeArray[count] + "')]";
            attrElement = browserDriver.findElement(By.xpath(path));
            CSUtility.waitForVisibilityOfElement(waitForReload, attrElement);
            switch (fieldTypeArray[count]) {
            case "Text":
                textElement = browserDriver
                        .findElement(By.xpath(path + "//td[2]//input"));
                sendTextToEelment(textElement, slText);
                break;
            case "Reference":
                textElement = browserDriver
                        .findElement(By.xpath(path + "//td[2]//div[2]//img"));
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        textElement);
                textElement.click();
                if (window.equals("Main")) {
                    TraverseSelectionDialogFrames
                            .traverseTillProductsFolderRightPane(waitForReload,
                                    browserDriver);
                    addProduct(productToAdd);
                    CSUtility.traverseToSettingsConfiguration(waitForReload,
                            browserDriver);
                } else {
                    TraverseSelectionDialogFrames
                            .traverseTillSubProductsFolderRightPane(
                                    waitForReload, browserDriver);
                    addProduct(productToAdd);
                    TraverseSelectionDialogFrames
                            .traverseToCsPortalWindowContentFrame(waitForReload,
                                    browserDriver);
                }
                break;
            case "Number":
                textElement = browserDriver
                        .findElement(By.xpath(path + "//td[2]//input"));
                sendTextToEelment(textElement, numText);
                break;
            case "Time":
                textElement = browserDriver.findElement(By.xpath(path
                        + "//td[2]//nobr[1]//div[1]//img[contains(@src,'pixel.gif')]"));
                textElement.click();
                if (window.equals("Main")) {
                    TraverseSelectionDialogFrames
                            .traverseToCsPortalWindowContentFrame(waitForReload,
                                    browserDriver);
                    csGuiDialogContentIdInstance.clkOnBtnNow(waitForReload);
                    CSUtility.traverseToSettingsConfiguration(waitForReload,
                            browserDriver);
                } else {
                    TraverseSelectionDialogFrames
                            .traverseToSubCsPortalWindowContentFrame(
                                    waitForReload, browserDriver);
                    csGuiDialogContentIdInstance.clkOnBtnNow(waitForReload);
                    TraverseSelectionDialogFrames
                            .traverseToCsPortalWindowContentFrame(waitForReload,
                                    browserDriver);
                }
                break;
            case "Selection":
                textElement = browserDriver.findElement(
                        By.xpath(path + "//td[2]//div[1]//select[1]"));
                Select select = new Select(textElement);
                select.selectByVisibleText(selectionType);
                break;
            default:
                break;
            }
        }
    }

    /**
     * This method add the product to the reference attribute
     * 
     * @param productToAdd String object contains product name
     */
    private void addProduct(String productToAdd) {
        WebElement selectProduct = browserDriver
                .findElement(By.linkText(productToAdd));
        action.doubleClick(selectProduct).build().perform();
        CSLogger.info(productToAdd + " product is added.");
    }

    /**
     * This method send the text data to the WebElement
     * 
     * @param txtelement WebElement object where the text data is to send
     * @param text String object contains text data
     */
    private void sendTextToEelment(WebElement txtelement, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, txtelement);
        txtelement.clear();
        txtelement.sendKeys(text);
        CSLogger.info(text + " data is sent.");
    }

    /**
     * This method create product in tree structure
     * 
     * @param className String object contains class name
     * @param ProductTree String object contains Product name
     */
    private void createProductTree(String className, String ProductTree) {
        String[] arrayOfProduct = ProductTree.split(",");
        createProduct(arrayOfProduct[0]);
        for (int count = 1; count < arrayOfProduct.length; count++) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            WebElement parentNode = browserDriver
                    .findElement(By.linkText(arrayOfProduct[count - 1]));
            CSUtility.rightClickTreeNode(waitForReload, parentNode,
                    browserDriver);
            productPopUp.selectPopupDivMenu(waitForReload,
                    productPopUp.getCsGuiPopupMenuNewChild(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            productPopUp.enterValueInDialogue(waitForReload,
                    arrayOfProduct[count]);
            productPopUp.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            CSLogger.info("Product Created");
        }
        dragAndDropClassToProduct(className, arrayOfProduct[0]);
    }

    /**
     * This method create product.
     * 
     * @param productName String object contains product name
     */
    private void createProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, productName);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Product Created");
    }

    /**
     * This method drag and drop class to product.
     * 
     * @param className String object contains class name
     * @param productName String object contains product name
     */
    private void dragAndDropClassToProduct(String className,
            String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToDragDrop = browserDriver
                .findElement(By.linkText(productName));
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode().click();
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        action.dragAndDrop(classNameToDragDrop, productToDragDrop).build()
                .perform();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentIdInstance.getBtnReplace()));
        csGuiDialogContentIdInstance.getBtnReplace().click();
        CSLogger.info("Drag and Drop class to Product");
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
     * contains ClassName,Product for Reference,ProductTree,
     * AttributeTechnicalName,FieldType,SingleLineText,Number,Selection
     * 
     * @return createProductAndAddDataTestData
     */
    @DataProvider(name = "createProductAndAddDataSheet")
    public Object[] createProductAndAddData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("elasticSearchTestCases"),
                createProductAndAddDataTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        action = new Actions(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        CSUtility.switchToStudioList(waitForReload, browserDriver);
    }
}
