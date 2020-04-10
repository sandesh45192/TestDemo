/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies creation of product and
 * assign class to product by drag and drop
 * 
 * @author CSAutomation Team
 */
public class AssigningFacetedClassToProductFolderTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private IProductPopup             productPopUp;
    private Actions                   action;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private IClassPopup               classPopUp;
    private String                    assignClassToProductSheet = "AssignClassToProduct";

    /**
     * This method verifies creation of product.
     * 
     * @param createdClass String object contains class name
     * @param newClass String object contains attribute name
     * @param productFolder String object contains product folder name
     */
    @Test(priority = 1, dataProvider = "assignClassToProduct")
    public void testAssigningFacetedClassToProductFolder(String createdClass,
            String newClass, String productFolder) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createProduct(productFolder);
            verifyCreatedProduct(productFolder);
            createClass(newClass);
            dragAndDropClassToProduct(newClass, productFolder, "replace");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testAssigningFacetedClassToProductFolder",
                    e);
            Assert.fail(
                    "Automation Error : testAssigningFacetedClassToProductFolder",
                    e);
        }
    }

    /**
     * This method verifies the assign of class to product vai drag and drop
     * 
     * @param className String object contains class name
     * @param attrName String object contains attribute name
     * @param productFolder String object contains product folder name
     */
    @Test(priority = 2, dataProvider = "assignClassToProduct")
    public void testAssigningClassByDragDrop(String createdClass,
            String newClass, String productFolder) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            dragAndDropClassToProduct(createdClass, productFolder, "cancel");
            dragAndDropClassToProduct(createdClass, productFolder, "extend");
            verifyDragAndDrop(createdClass, newClass, productFolder, "extend");
            dragAndDropClassToProduct(createdClass, productFolder, "replace");
            verifyDragAndDrop(createdClass, newClass, productFolder, "replace");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testAssigningClassByDragDrop",
                    e);
            Assert.fail("Automation Error : testAssigningClassByDragDrop", e);
        }
    }

    /**
     * This method verifies the assign of class to product vai drag and drop
     * 
     * @param className String object contains class name
     * @param attrName String object contains attribute name
     * @param productFolder String object contains product folder name
     * @param executeAction String object contains action to perform on window
     */
    private void verifyDragAndDrop(String createdClass, String newClass,
            String productFolder, String executeAction) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productElement = browserDriver
                .findElement(By.linkText(productFolder));
        productElement.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentIdInstance.clkElement(waitForReload,
                csGuiDialogContentIdInstance.getTabProperties());
        int rowCount = browserDriver
                .findElements(
                        By.xpath("//table[@class='CSGuiTable']//tbody//tr"))
                .size();
        List<String> nameofClasses = new ArrayList<String>();
        WebElement rowElement = null;
        for (int row = 2; row <= rowCount; row++) {
            rowElement = browserDriver.findElement(
                    By.xpath("//table[@class='CSGuiTable']//tbody//tr[" + row
                            + "]//td[2]"));
            nameofClasses.add(rowElement.getText());
        }
        if (executeAction.equals("replace")) {
            if (!nameofClasses.contains(newClass)) {
                CSLogger.info("Previous class is replace successfully");
            } else {
                CSLogger.error("Previous class is not replace");
                Assert.fail("Previous class is not replace");
            }
        } else {
            if (nameofClasses.contains(newClass)
                    && nameofClasses.contains(createdClass)) {
                CSLogger.info("Class is successfully added to product");
            } else {
                CSLogger.error("Class is not added to product");
                Assert.fail("Class is not added to product");
            }
        }
    }

    /**
     * This method drag and drop class to product.
     * 
     * @param className String object contains class name
     * @param productName String object contains product name
     * @param executeAction String object contains action to perform on window
     */
    private void dragAndDropClassToProduct(String className, String productName,
            String executeAction) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToDragDrop = browserDriver
                .findElement(By.linkText(productName));
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsClassesNode(waitForReload);
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        action.dragAndDrop(classNameToDragDrop, productToDragDrop).build()
                .perform();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        if (executeAction.equals("replace")) {
            csGuiDialogContentIdInstance.clkElement(waitForReload,
                    csGuiDialogContentIdInstance.getBtnReplace());
        } else if (executeAction.equals("extend")) {
            csGuiDialogContentIdInstance.clkElement(waitForReload,
                    csGuiDialogContentIdInstance.getBtnExtend());
        } else {
            productPopUp.askBoxWindowOperation(waitForReload, false,
                    browserDriver);
        }
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
     * This method verifies the creation of product
     * 
     * @param productFolder String object contains product folder name
     */
    private void verifyCreatedProduct(String productFolder) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productElement = browserDriver
                .findElement(By.linkText(productFolder));
        Assert.assertEquals(productFolder, productElement.getText());
        productElement.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        Assert.assertTrue(csGuiToolbarHorizontalInstance
                .getBtnCSGuiToolbarCheckIn().isDisplayed());
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
     * This data provider returns the sheet data to run the test case which
     * contains Created Class,New Class,Product Name
     * 
     * @return assignClassToProductSheet
     */
    @DataProvider(name = "assignClassToProduct")
    public Object[] assignClassToProduct() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                assignClassToProductSheet);
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
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        action = new Actions(browserDriver);
    }
}
