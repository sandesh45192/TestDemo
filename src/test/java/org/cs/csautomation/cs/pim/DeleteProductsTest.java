/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create the product, verify the
 * product and to delete the created product..
 * 
 * @author CSAutomation Team
 */
public class DeleteProductsTest extends AbstractTest {

    private PimStudioProductsNodePage pimStudioProductsNode;
    IProductPopup                     productPopUp;
    private FrameLocators             locator;
    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private IClassPopup               productPopup;
    private String                    deleteProductSheetName = "DeleteProduct";

    /**
     * This method is used to initializes required instances for test.
     */
    @BeforeClass()
    public void initialization() {
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 180);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
    }

    /**
     * This test method is used to create Product, verify product is created and
     * then delete the product and again verify product is deleted.
     * 
     * @param productName String object containing name of the product.
     */
    @Test(dataProvider = "deleteProductTestData")
    public void testDeleteProduct(String productLabelName) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            createProduct(waitForReload, productLabelName);
            checkPresenceOfProduct(waitForReload, productLabelName, false);
            performDeleteCase(productLabelName, false);
            performDeleteCase(productLabelName, true);
        } catch (Exception e) {
            CSLogger.debug("Failed to Delete the product: ", e);
            Assert.fail("Failed to Delete the product", e);
        }
    }

    /**
     * This method is used to create a new Product.
     * 
     * @param nameOfProduct String object containing name of the created
     *            product.
     */
    private void createProduct(WebDriverWait waitForReload,
            String nameOfProduct) {
        try {
            clkProductFolder(waitForReload);
            CSUtility.rightClickTreeNode(waitForReload,
                    pimStudioProductsNode.getBtnPimProductsNode(),
                    browserDriver);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            pimStudioProductsNode.getproductPopUpFrame()));
            pimStudioProductsNode.getClkCreateNew().click();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmDataSelectionDialog()));
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    pimStudioProductsNode.getUserElement()));
            CSUtility.tempMethodForThreadSleep(1000);
            pimStudioProductsNode.getUserElement().sendKeys(nameOfProduct);
            productPopUp.getBtnCsGuiModalDialogOkButton().click();
        } catch (Exception e) {
            CSLogger.error("Unable to Create new product", e);
            Assert.fail("Failed to create product");
        }
    }

    /**
     * This method is used to click on the 'Products' folder.
     * 
     * @param waitForReload WebDriverWait Object.
     * 
     */
    private void clkProductFolder(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("Clicked on Products Folder");
    }

    /**
     * This method is used to verify when product is present in the tree.
     * 
     * @param productName String object containing name of the product.
     * @param ok Boolean to press true for absence of element false for presence
     *            of element.
     */
    private void checkPresenceOfProduct(WebDriverWait waitForReload,
            String productName, boolean deleteProduct) {
        clkProductFolder(waitForReload);
        List<WebElement> productNames = browserDriver
                .findElements(By.linkText(productName));
        if (deleteProduct) {
            if (productNames.isEmpty()) {
                CSLogger.info("The product named as " + productName
                        + " is not Present");
            } else {
                Assert.fail(
                        "The product named as " + productName + " is present");
            }
        } else {
            if (!productName.isEmpty()) {
                CSLogger.info(
                        "The product named as " + productName + " is Present");
            } else {
                Assert.fail("The product named as " + productName
                        + " is not present");
            }
        }
    }

    /**
     * This method is used to delete the product from the tree.
     * 
     * @param productName String object containing name of the product.
     * @param ok Boolean to press Ok to click on accept button or cancel to
     *            click on cancel button on alert box.
     */
    private void performDeleteCase(String productName, Boolean deleteProduct) {
        clkProductFolder(waitForReload);
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.rightClickTreeNode(waitForReload, createdProductFolder,
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuObject(), browserDriver);
        productPopup.selectPopupDivSubMenu(waitForReload,
                productPopup.getDeleteMenu(), browserDriver);
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (deleteProduct) {
            alertBox.accept();
            CSLogger.info("Click on OK to alert and product is deleted.");
            checkPresenceOfProduct(waitForReload, productName, deleteProduct);
        } else {
            alertBox.dismiss();
            CSLogger.info(
                    "Click on Cancel to alert and product is not deleted.");
            checkPresenceOfProduct(waitForReload, productName, deleteProduct);
        }
    }

    @DataProvider(name = "deleteProductTestData")
    public Object[][] getProductNameTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                deleteProductSheetName);
    }

}
