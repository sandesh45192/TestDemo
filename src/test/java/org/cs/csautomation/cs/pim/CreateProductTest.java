/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to handle the test cases related to the
 * creation of Products.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateProductTest extends AbstractTest {

    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private String                    productSheetName = "CreateProduct";
    private IProductPopup             productPopUp;
    private SoftAssert                softAssertion;

    /**
     * This method creates the product.
     * 
     * @param productName
     *            String object contains name of the product to be created.
     */
    @Test(dataProvider = "createProductData")
    public void testCreateProduct(String productName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            csPortalHeader.clkBtnProducts(waitForReload);
            createProduct(productName, false);
            createProduct(productName, true);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testCreateProduct", e);
            Assert.fail("Automation error in test method : testCreateProduct",
                    e);
        }
    }

    /**
     * Creates the given product and also verifies the product creation.
     * 
     * @param productName
     *            String object contains name of product.
     * @param pressOkay
     *            Boolean parameter contains true or false values.
     */
    private void createProduct(String productName, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopUp.enterValueInDialogue(waitForReload, productName);
        productPopUp.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        verifyProductCreation(productName, pressOkay);
    }

    /**
     * Verifies whether product is created.
     * 
     * @param productName
     *            String object contains name of product.
     * @param pressOkay
     *            Boolean parameter contains true or false values.
     */
    private void verifyProductCreation(String productName, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        int productExists = browserDriver.findElements(By.linkText(productName))
                .size();
        if (pressOkay) {
            if (productExists != 0) {
                CSLogger.info("Product " + productName
                        + " created successfully :  test step passed");
            } else {
                CSLogger.error("Product " + productName
                        + " creation failed : test step failed");
                softAssertion
                        .fail("Product creation failed : test step failed");
            }
        } else {
            if (productExists != 0) {
                CSLogger.error("Product " + productName
                        + " not created when clicked on " + "cancel: test step "
                        + "failed");
                softAssertion.fail(
                        "Product " + productName + " not created when clicked"
                                + " on cancel: test step failed");
            } else {
                CSLogger.info(
                        "No product created when clicked on cancel :  test step"
                                + " passed");
            }
        }
    }

    /**
     * This data provider returns array of productName.
     * 
     * @return array of productfolderName and ClassName
     */
    @DataProvider(name = "createProductData")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                productSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        pimStudioProductsNode = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
    }

}
