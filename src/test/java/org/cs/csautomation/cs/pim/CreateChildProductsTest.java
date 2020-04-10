/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
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
 * This class contains the test which creates child products in product folder.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateChildProductsTest extends AbstractTest {

    private PimStudioProductsNodePage pimStudioProductsNode;
    private WebDriverWait             waitForReload;
    private String                    createChildSheetName = "CreateChild";
    private FrameLocators             locator;
    private CSPortalHeader            csPortalHeader;
    private IProductPopup             productPopup;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontal;

    /**
     * This test method which drives the usecase of creating child products
     * under the parent folder
     * 
     * @param productname which contains name of parent product
     * @param productchildname which contains name of the child products
     */
    @Test(dataProvider = "ProductChildData")
    public void testCreateChildProduct(String productName, String productChild,
            String nameOfClass) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnProducts(waitForReload);
            createChildInProductFolder(productName, productChild, nameOfClass);
            verifyCreatedChild(productName, productChild, nameOfClass);
            // editChildProduct(productName, productChild, editChild);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testCreateChildProduct",
                    e);
            Assert.fail(
                    "Automation error in test method : testCreateChildProduct",
                    e);
        }
    }

    /**
     * This test method which drives the usecase of creating child products
     * under the parent folder
     * 
     * @param productName contains name of the product
     * @param productFirstChildName name of first child product
     * @param nameOfClass contains name of class
     */
    public void createChildInProductFolder(String productName,
            String productChild, String nameOfClass) {
        traverseToProduct(productName);
        rightClickProduct(productName);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuNewChild(), browserDriver);
        clickOkOfPopup(productChild);
    }

    /**
     * This method clicks on ok of pop up
     * 
     * @param productChild after creating child product
     */
    private void clickOkOfPopup(String productChild) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(pimStudioProductsNode.getUserElement()));
        WebElement element = browserDriver.findElement(
                By.xpath("//form[@id='CSGuiModalDialogFormName']"));
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getUserElement()));
        CSUtility.tempMethodForThreadSleep(1000);
        pimStudioProductsNode.getUserElement().sendKeys(productChild);
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopup.getBtnCsGuiModalDialogOkButton()));
        productPopup.getBtnCsGuiModalDialogOkButton().click();
        CSLogger.info("Child created.");
    }

    /**
     * This method traverses to product
     * 
     * @param productName contains the name of the product
     */
    private void traverseToProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("Clicked on products node");
    }

    private void verifyCreatedChild(String productName, String productChild,
            String nameOfClass) {
        try {
            traverseToProduct(productName);
            WebElement product = browserDriver
                    .findElement(By.linkText(productName));
            product.click();
            WebElement childProduct = browserDriver
                    .findElement(By.linkText(productChild));
            Assert.assertEquals(productChild, childProduct.getText());
            CSLogger.info("Child product is present.");
        } catch (Exception e) {
            CSLogger.info("Child product is not present .");
            Assert.fail("Child product is not present .");
        }
    }

    /**
     * This method performs right click on product
     * 
     * @param productName contains the name of the product
     */
    private void rightClickProduct(String productName) {
        try {
            WebElement createdProductFolder = browserDriver
                    .findElement(By.linkText(productName));
            CSUtility.rightClickTreeNode(waitForReload, createdProductFolder,
                    browserDriver);
        } catch (Exception e) {
            CSLogger.info("Could not right click on created product", e);
        }
    }

    /**
     * This method clicks on createNewchild option
     * 
     * @param waitForReload waits for an element to reload
     * @param productChild contains the name of the product child
     */
    public void createNewChild(WebDriverWait waitForReload,
            String productChild) {
        try {
            productPopup.selectPopupDivMenu(waitForReload,
                    productPopup.getCsGuiPopupMenuNewChild(), browserDriver);
            productPopup.enterValueInDialogue(waitForReload, productChild);
            productPopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
        } catch (Exception e) {
            CSLogger.fatal("Failed to create new child", e);
        }
    }

    private void editChildProduct(String productName, String productChild,
            String editChild) {
        traverseToProduct(productName);
        WebElement childProduct = browserDriver
                .findElement(By.linkText(productChild));
        childProduct.click();
        editLabel(editChild);
    }

    private void editLabel(String editChild) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentId.clkBtnProperties(waitForReload);
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentId.getTxtProductLabel()));
        WebElement childLabel = doubleClick(
                csGuiDialogContentId.getTxtProductLabel());
        childLabel.clear();
        childLabel.sendKeys(editChild);
        clkSaveButton();
    }

    private void clkSaveButton() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontal.getBtnCSGuiToolbarSave()));
        csGuiToolbarHorizontal.getBtnCSGuiToolbarSave().click();
        CSLogger.info("Clicked on save button");
    }

    private WebElement doubleClick(WebElement childLabel) {
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(childLabel).build().perform();
        CSLogger.info("Double clicked on label textbox");
        return childLabel;
    }

    /**
     * this data provider returns product folder name and child product folder
     * names and Class
     * 
     * @return sheet containing name of product
     */
    @DataProvider(name = "ProductChildData")
    public Object[][] getProductChildData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                createChildSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
    }
}
