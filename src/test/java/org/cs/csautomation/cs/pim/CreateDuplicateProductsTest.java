/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.pim;

import java.util.List;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to Create duplicate products folder for
 * this test to run a parent product folder and a child to that product folder
 * is created at runtime
 * 
 * @author CSAutomation Team
 *
 */
public class CreateDuplicateProductsTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private IProductPopup             productPopup;
    private String                    CreateDuplicateProductsSheetName = "CreateDuplicateProducts";
    private SoftAssert                softAssertion;

    /**
     * This test method creates duplicate products which takes two inputs from
     * data provider the product folder name and child product
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     * @param childProductName
     *            String containing name of the child product
     */
    @Test(dataProvider = "CreateDuplicateProductsData")
    public void testCreateDuplicateProducts(String parentProductFolderName,
            String childProductName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 80);
            createParentProductFolder(parentProductFolderName);
            createChildProduct(parentProductFolderName, childProductName, true);
            selectDuplicateOptionOfParentProductFolder(parentProductFolderName,
                    "Cancel", childProductName);
            selectDuplicateOptionOfParentProductFolder(parentProductFolderName,
                    "Recursive", childProductName);
            selectDuplicateOptionOfParentProductFolder(parentProductFolderName,
                    "NonRecursive", childProductName);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testCreateDuplicateProducts ",
                    e);
            Assert.fail(
                    "Automation Error in test method : testCreateDuplicateProducts ",
                    e);
        }
    }

    /**
     * This method creates a parent product folder
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     */
    private void createParentProductFolder(String parentProductFolderName) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        CSLogger.info("Right clicked on product node");
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSLogger.info("Selected New Folder Option");
        productPopup.enterValueInDialogue(waitForReload,
                parentProductFolderName);
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * This method created a child product
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     * @param childProductName
     *            String containing name of the child product
     * @param pressOkay
     *            Boolean value to ie. true to clicks on OK of pop while
     *            creating child product
     */
    private void createChildProduct(String parentProductFolderName,
            String childProductName, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getCreatedParentProductFolder(parentProductFolderName));
        CSUtility.rightClickTreeNode(waitForReload,
                getCreatedParentProductFolder(parentProductFolderName),
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuNewChild(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, childProductName);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
    }

    /**
     * This method selects duplicate option by right clicking on product folder
     * and handles three options i.e Cancel,Recursive,Non-Recursive for
     * duplicating parent product folder
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     * @param userInput
     *            String value specifying options cancel,Recursive,Non-Recursive
     *            while creating duplicate products
     * @param childProductName
     *            String containing name of the child product
     */
    private void selectDuplicateOptionOfParentProductFolder(
            String parentProductFolderName, String userInput,
            String childProductName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                getCreatedParentProductFolder(parentProductFolderName),
                browserDriver);
        productPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupMenuObject());
        productPopup.selectPopupDivSubMenu(waitForReload,
                productPopup.getCsGuiPopupMenuSubMenuDuplicate(),
                browserDriver);
        productPopup.askBoxWindowOperationDuplicateProducts(waitForReload,
                userInput, browserDriver);
        if (userInput.equals("Cancel")) {
            verifyDuplicateCancelOption(parentProductFolderName);
        } else if (userInput.equals("Recursive")) {
            verifyDuplicateRecursiveOption(parentProductFolderName,
                    childProductName);
        } else {
            verifyDuplicateNonRecursiveOption(parentProductFolderName,
                    childProductName);
        }
    }

    /**
     * Gets created parent product folder
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     * @return WebElement of created parent product folder
     */
    private WebElement getCreatedParentProductFolder(
            String parentProductFolderName) {
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.linkText(parentProductFolderName)));
        return browserDriver.findElement(By.linkText(parentProductFolderName));
    }

    /**
     * This method verifies whether after selecting cancel option no duplicate
     * products should be created
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     */
    private void verifyDuplicateCancelOption(String parentProductFolderName) {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            List<WebElement> duplicateProductFolder = browserDriver
                    .findElements(
                            By.linkText("Copy of " + parentProductFolderName));
            Assert.assertEquals(duplicateProductFolder.isEmpty(), true);
            CSLogger.info(
                    "Verification passed for cancel option of duplicate product");
        } catch (Exception e) {
            CSLogger.error(
                    "Verification failed for cancel option of duplicate product",
                    e);
            softAssertion.fail(
                    "Verification failed for cancel option of duplicate product",
                    e);
        }
    }

    /**
     * This method verifies that after selecting Recursive option selected
     * product along with its children should be duplicated properly
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     * @param childProductName
     *            String containing name of the child product
     */
    private void verifyDuplicateRecursiveOption(String parentProductFolderName,
            String childProductName) {
        try {
            verifyProductCreation(parentProductFolderName);
            browserDriver
                    .findElement(
                            By.linkText("Copy of " + parentProductFolderName))
                    .click();
            browserDriver
                    .findElement(
                            By.linkText("Copy of " + parentProductFolderName))
                    .click();
            CSUtility.tempMethodForThreadSleep(3000);
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            int labelHeaderIndex = CSUtility.getListHeaderIndex("Label",
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr[3]/"
                            + "td[" + labelHeaderIndex + "]")));
            Assert.assertEquals(childProductName,
                    browserDriver
                            .findElement(By
                                    .xpath("//table[@class='hidewrap CSAdminList"
                                            + "']/tbody/tr[3]/" + "td["
                                            + labelHeaderIndex + "]"))
                            .getText());
            CSLogger.info(
                    "Verification passed for Recursive option of duplicate "
                            + "product");
        } catch (Exception e) {
            CSLogger.error(
                    "Verification failed for Recursive option of duplicate "
                            + "product",
                    e);
            softAssertion
                    .fail("Verification failed for Recursive option of duplicate "
                            + "product");
        }
    }

    /**
     * This method verifies that after selecting Non-Recursive option selected
     * product should be duplicated properly and not its child should not be
     * duplicated
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     * @param childProductName
     *            String containing name of the child product
     */
    private void verifyDuplicateNonRecursiveOption(
            String parentProductFolderName, String childProductName) {
        try {
            doubleClkParentProductFolder(parentProductFolderName);
            verifyProductCreation(parentProductFolderName);
            browserDriver
                    .findElement(
                            By.linkText("Copy of " + parentProductFolderName))
                    .click();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            int labelHeaderIndex = CSUtility.getListHeaderIndex("Label",
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr[3]/"
                            + "td[" + labelHeaderIndex + "]")));
            Assert.assertEquals("Copy of " + parentProductFolderName,
                    browserDriver
                            .findElement(By.xpath("//table[@class='hidewrap "
                                    + "CSAdminList']/tbody/tr[3]/" + "td["
                                    + labelHeaderIndex + "]"))
                            .getText());
            CSLogger.info(
                    "Verification passed for Non Recursive option of duplicate"
                            + " product");
        } catch (Exception e) {
            CSLogger.error(
                    "Verification failed for Non Recursive option of duplicate "
                            + "product",
                    e);
            Assert.fail(
                    "Verification failed for Non Recursive option of duplicate"
                            + " product");
        }
    }

    /**
     * Checks the creation of products
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     */
    private void verifyProductCreation(String parentProductFolderName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        List<WebElement> duplicateProductFolder = browserDriver.findElements(
                By.linkText("Copy of " + parentProductFolderName));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.linkText("Copy of " + parentProductFolderName)));
        Assert.assertEquals(duplicateProductFolder.isEmpty(), !true);
    }

    /**
     * This method double clicks on parent product folder
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     */
    private void doubleClkParentProductFolder(String parentProductFolderName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(
                getCreatedParentProductFolder(parentProductFolderName))
                .perform();
    }

    /**
     * Data provider method contains array of parent product folder name and
     * child product name
     * 
     * @return Array String method contains array of product name and attributes
     */
    @DataProvider(name = "CreateDuplicateProductsData")
    public Object[][] getCreateDuplicateProductsData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                CreateDuplicateProductsSheetName);
    }

    @BeforeMethod
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
    }
}
