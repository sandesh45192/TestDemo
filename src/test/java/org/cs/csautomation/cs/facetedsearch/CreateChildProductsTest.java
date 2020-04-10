/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies creation child product
 * 
 * @author CSAutomation Team
 */
public class CreateChildProductsTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private IProductPopup             productPopUp;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private String                    createChildProductSheet = "CreateChildProduct";

    /**
     * This method verifies creation of child product.
     * 
     * @param className String object contains class name
     * @param productFolder String object contains product folder name
     * @param paneTitle String object contains pane Title
     * @param childProduct String object contains child product name
     * @param subChild String object contains sub child product folder name
     */
    @Test(dataProvider = "createChildProduct")
    public void testCreateChildProducts(String className, String productFolder,
            String paneTitle, String childProduct, String subChild) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createChildProduct(className, productFolder, paneTitle, childProduct,
                    subChild);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testCreateChildProducts", e);
            Assert.fail("Automation Error : testCreateChildProducts", e);
        }
    }

    /**
     * This method create child product.
     * 
     * @param className String object contains class name
     * @param productFolder String object contains product folder name
     * @param paneTitle String object contains pane title
     * @param childProduct String object contains child product name
     * @param subChild String object contains sub child product folder name
     */
    private void createChildProduct(String className, String productFolder,
            String paneTitle, String childProduct, String subChild) {
        String[] childArray = childProduct.split(",");
        String[] subChildArray = subChild.split(",");
        for (int index = 0; index < childArray.length; index++) {
            createChildProduct(productFolder, childArray[index]);
            verifyCreatedChild(className, productFolder, paneTitle,
                    childArray[index]);
            createSubChildProduct(productFolder, childArray[index],
                    subChildArray[index]);
            verifyCreatedChild(className, productFolder, paneTitle,
                    subChildArray[index]);
        }
    }

    /**
     * This method verifies the Created child product
     * 
     * @param className String object contains class name
     * @param productFolder String object contains product folder name
     * @param paneTitle String object contains pane title
     * @param childProduct String object contains child product name
     */
    private void verifyCreatedChild(String className, String productFolder,
            String paneTitle, String childProduct) {
        List<String> nameOfClasses = new ArrayList<String>();
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        clkOnElementByLinkText(productFolder);
        clkOnElementByLinkText(childProduct);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentIdInstance.clkElement(waitForReload,
                csGuiDialogContentIdInstance.getTabProperties());
        WebElement paneElement = browserDriver.findElement(
                By.xpath("//nobr[contains(text(),'" + paneTitle + "')]"));
        Assert.assertTrue(paneElement.isDisplayed());
        String label=csGuiDialogContentIdInstance.getTxtProductLabel().getAttribute("value");
        if(label.equals(childProduct)) {
            CSLogger.info("Verfication successful for label "+childProduct );
        }else {
            CSLogger.error("Verfication fail for label "+childProduct );
            Assert.fail("Verfication fail for label "+childProduct );
        }
        List<WebElement> inheritClasses = browserDriver.findElements(
                By.xpath("//table[@class='CSGuiTable']//tbody//tr//td[2]"));
        for (WebElement classes : inheritClasses) {
            nameOfClasses.add(classes.getText());
        }
        if (nameOfClasses.contains(className)) {
            CSLogger.info(
                    "Class " + className + "is inherited from parent Product");
        } else {
            CSLogger.error("Class " + className
                    + "is not present in inherited section");
        }
    }

    /**
     * This method create sub child product
     * 
     * @param productFolder String object contains product folder name
     * @param childProduct String object contains child product name
     * @param subChild String object contains sub child product folder name
     */
    private void createSubChildProduct(String productFolder,
            String childProduct, String subChild) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        clkOnElementByLinkText(productFolder);
        WebElement childproductElement = browserDriver
                .findElement(By.linkText(childProduct));
        CSUtility.rightClickTreeNode(waitForReload, childproductElement,
                browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuNewChild(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, subChild);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Sub Child Product is created " + subChild);
    }

    /**
     * This method create child product
     * 
     * @param productFolder String object contains product folder name
     * @param childProduct String object contains child product name
     */
    private void createChildProduct(String productFolder, String childProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productElement = browserDriver
                .findElement(By.linkText(productFolder));
        CSUtility.rightClickTreeNode(waitForReload, productElement,
                browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuNewChild(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, childProduct);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Child Product is created " + childProduct);
    }
    
    /**
     * This method click on element by link Text
     * 
     * @param textValue String object contains text of element
     */
    private void clkOnElementByLinkText(String textValue) {
        WebElement element = browserDriver
                .findElement(By.linkText(textValue));
        element.click();
        CSLogger.info("Click on element "+textValue);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Class Name,Product Name,Pane Name,ChildProduct,SubChild Product
     * 
     * @return createChildProductSheet
     */
    @DataProvider(name = "createChildProduct")
    public Object[] createChildProduct() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                createChildProductSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
    }
}
