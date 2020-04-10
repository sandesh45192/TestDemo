/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.elasticsearch;

import java.util.concurrent.TimeUnit;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which perform the search operation after
 * deleting the product using mass editing.
 * 
 * @author CSAutomation Team
 */
public class MassDeletionAndSearchOfDataTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private IProductPopup             productPopUp;
    private FlexTablePage             flexTablePage;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private String                    massDeletionAndSearchOfDataTestData = "MassDeletionAndSearchOfData";

    /**
     * This method verifies search operation result after deleting the product
     * using mass editing.
     * 
     * @param productsName String object contains product name
     * @param childProduct String object contains child product name
     * @param className String object contains class name
     * @param attributeName String object contains attribute name
     * @param textData String object contains text data to search
     */
    @Test(dataProvider = "massDeletionAndSearchOfDataSheet")
    public void testMassDeletionAndSearchOfData(String productName,
            String childProduct, String className, String attributeName,
            String textData) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createProduct(productName);
            createChildProduct(productName, childProduct);
            dragAndDropClassToProduct(className, productName);
            addDataInAttributeField(productName, childProduct, attributeName,
                    textData);
            chkInAndChkoutProduct(productName,
                    productPopUp.getCsGuiPopupCheckIn());
            CSUtility.tempMethodForThreadSleep(2000);
            chkInAndChkoutProduct(productName,
                    productPopUp.getCsGuiPopupCheckOut());
            deleteChildProduct(productName, childProduct);
            verifySearchForDeletedChild(productName, childProduct, textData);
            CSLogger.info("Mass deletion and search of data test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testMassDeletionAndSearchOfData",
                    e);
            Assert.fail("Automation Error : testMassDeletionAndSearchOfData",
                    e);
        }
    }

    /**
     * This method verifies search operation result for deleted the child
     * product
     * 
     * @param productsName String object contains product name
     * @param childProduct String object contains child product name
     * @param textData String object contains text data to search
     */
    private void verifySearchForDeletedChild(String productName,
            String childProduct, String textData) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("clicked on products node in PIM studio");
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement createdParentProduct = browserDriver
                .findElement(By.linkText(productName));
        createdParentProduct.click();
        goToRightSectionWindow();
        csGuiToolbarHorizontalInstance.getTxtSearchBox().click();
        CSUtility.tempMethodForThreadSleep(3000);
        csGuiToolbarHorizontalInstance.getTxtSearchBox().sendKeys(textData);
        CSUtility.tempMethodForThreadSleep(3000);
        csGuiToolbarHorizontalInstance.getTxtSearchBox().sendKeys(Keys.ENTER);
        CSUtility.tempMethodForThreadSleep(10000);
        int elementCount = browserDriver
                .findElements(By.xpath(
                        "//table[@class='hidewrap CSAdminList']//tbody//tr[3]"))
                .size();
        System.out.println("ELEMENT COUNT = " + elementCount);
        if (elementCount == 0) {
            CSLogger.info("Table is empty test case pass.");
        } else {
            CSLogger.error("Product is present in the Table test case fail.");
            Assert.fail("Product is present in the Table test case fail.");
        }
    }

    /**
     * This method Check In and check out the product.
     * 
     * @param productsName String object contains product name
     */
    private void chkInAndChkoutProduct(String productName, WebElement element) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToChkIn = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.rightClickTreeNode(waitForReload, productToChkIn,
                browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuObject(), browserDriver);
        productPopUp.selectPopupDivSubMenu(waitForReload, element,
                browserDriver);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        csGuiDialogContentIdInstance.clkOnBtnRecursive(waitForReload);
    }

    /**
     * This method add data in attribute field of product
     * 
     * @param productsName String object contains product name
     * @param childProduct String object contains child product name
     * @param className String object contains class name
     * @param attributeName String object contains attribute name
     * @param textData String object contains text data to search
     */
    private void addDataInAttributeField(String productName,
            String childProduct, String attributeName, String textData) {
        String[] childProductArray = childProduct.split(",");
        for (int index = 0; index < childProductArray.length - 1; index++) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            pimStudioProductsNode.clickOnNodeProducts();
            WebElement productParent = browserDriver
                    .findElement(By.linkText(productName));
            waitForReload.until(ExpectedConditions.visibilityOf(productParent));
            productParent.click();
            WebElement productChild = browserDriver
                    .findElement(By.linkText(childProductArray[index]));
            waitForReload.until(ExpectedConditions.visibilityOf(productChild));
            action.doubleClick(productChild).build().perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            flexTablePage.clkOnDataTab(waitForReload);
            WebElement attrElement = browserDriver
                    .findElement(By.xpath("//tr[contains(@cs_name,'"
                            + attributeName + "')]//td[2]//input"));
            CSUtility.waitForVisibilityOfElement(waitForReload, attrElement);
            attrElement.clear();
            attrElement.sendKeys(textData);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
        }
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
     * This method create child product
     * 
     * @param productsName String object contains product name
     * @param childProduct String object contains child product name
     */
    private void createChildProduct(String productName, String childProduct) {
        String[] childProductArray = childProduct.split(",");
        for (String childName : childProductArray) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            pimStudioProductsNode.clickOnNodeProducts();
            WebElement createdParentProduct = browserDriver
                    .findElement(By.linkText(productName));
            CSUtility.rightClickTreeNode(waitForReload, createdParentProduct,
                    browserDriver);
            productPopUp.selectPopupDivMenu(waitForReload,
                    productPopUp.getCsGuiPopupMenuNewChild(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            productPopUp.enterValueInDialogue(waitForReload, childName);
            productPopUp.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
        }
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
     * This method delete the child product fron table
     * 
     * @param productsName String object contains product name
     * @param childProduct String object contains child product name
     */
    private void deleteChildProduct(String productName, String childProduct) {
        WebElement chkBox = null;
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement createdParentProduct = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                createdParentProduct);
        createdParentProduct.click();
        CSUtility.tempMethodForThreadSleep(2000);
        goToRightSectionWindow();
        int elementCount = browserDriver
                .findElements(By.xpath(
                        "//table[@class='hidewrap CSAdminList']//tbody//tr"))
                .size();
        for (int row = 3; row < elementCount; row++) {
            chkBox = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr["
                            + row + "]//td[1]//input"));
            CSUtility.waitForVisibilityOfElement(waitForReload, chkBox);
            chkBox.click();
        }
        chkBox = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']//tbody//tr[3]//td[4]"));
        CSUtility.rightClickTreeNode(waitForReload, chkBox, browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getDeleteMenu(), browserDriver);
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        alertBox.accept();
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("child products successfully deleted");
        browserDriver.navigate().refresh();
        browserDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        CSLogger.info("page refresh");
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToRightSectionWindow() {
        System.out.println("after delete");
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Product Name,Child Product Name,Class Name,Attribute Name, Text
     * Data
     * 
     * @return massDeletionAndSearchOfDataTestData
     */
    @DataProvider(name = "massDeletionAndSearchOfDataSheet")
    public Object[] MassDeletionAndSearchOfData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("elasticSearchTestCases"),
                massDeletionAndSearchOfDataTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 120);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        action = new Actions(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        CSUtility.switchToStudioList(waitForReload, browserDriver);
    }
}
