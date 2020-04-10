/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
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
 * This class contains the test methods to creates a parent product folder and
 * two child products during runtime and performs merge content operation on
 * child products and verifies that merge content page is switched to distribute
 * content page
 * 
 * 
 * @author CSAutomation Team
 *
 */
public class SwitchMergeToDistributeContentPageTest extends AbstractTest {

    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim                csPopupDiv;
    private SoftAssert                softAssertion;
    private CSGuiListFooter           CSGuiListFooterInstance;
    private WebDriverWait             waitForReload;
    private String                    mainWindowHandle;
    private String                    dataSheetName = "SwitchMergeContentPage";

    /**
     * This test methods to creates a parent product folder and two child
     * products during runtime and performs merge content operation on child
     * products and verifies that merge content page is switched to distribute
     * content page
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param firstChildProductName
     *            String Object containing the child product name.
     * @param secondChildProductName
     *            String Object containing the child product name.
     */
    @Test(priority = 1, dataProvider = "switchMergeToDistributeContentPageTestData")
    public void testSwitchMergeToDistributeContentPage(String parentProductName,
            String firstChildProductName, String secondChildProductName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandProductTree(waitForReload);
            createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                    parentProductName);
            CSLogger.info("Created parent product.");
            createChildProduct(parentProductName, firstChildProductName);
            createChildProduct(parentProductName, secondChildProductName);
            selectChildProductsToMergeContent(parentProductName,
                    firstChildProductName, secondChildProductName);
            mainWindowHandle = performMergeContentOperation();
            switchToMergeContentWindow();
            switchToDistributeContentPage();
            closeDistributeContentWindowTab();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : "
                    + "testSwitchMergeToDistributeContentPage ", e);
            Assert.fail("Automation error in : "
                    + "testSwitchMergeToDistributeContentPage ", e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void switchToPimAndExpandProductTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     * @param pimStudioProductsNode
     *            PimStudioProductsNode Object
     * @param parentProductName
     *            String Object containing the value for parent product Name.
     */
    private void createParentProduct(WebDriverWait waitForReload,
            PimStudioProductsNodePage pimStudioProductsNode,
            String parentProductName) {
        createProduct(parentProductName,
                pimStudioProductsNode.getBtnPimProductsNode(),
                csPopupDiv.getCsGuiPopupMenuCreateNew());
        CSLogger.info("Created Parent product");

    }

    /**
     * This method creates the child product with the supplied name and the
     * supplied parent product name.
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param childProductName
     *            String Object containing the child product name.
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void createChildProduct(String parentProductName,
            String childProductName) {
        WebElement element = pimStudioProductsNodeInstance
                .expandNodesByProductName(waitForReload, parentProductName);
        createProduct(childProductName, element,
                csPopupDiv.getCsGuiPopupMenuNewChild());
        CSLogger.info("Created new child product");
        goToPimProductTreeSection();
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName
     *            String object containing name of product it can be child
     *            product or parent product
     * @param nodeElement
     *            WebElement of either products node or parent product
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
        goToPimProductTreeSection();
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToPimProductTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method selects the child product so that merge content operation can
     * be performed
     * 
     * @param productFolderName
     * @param firstchildProduct
     *            String Object containing the child product name.
     * @param secondChildProduct
     *            String Object containing the child product name.
     * @param childProductThree
     *            String Object containing the child product name.
     */
    private void selectChildProductsToMergeContent(String productFolderName,
            String firstchildProduct, String secondChildProduct) {
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        goToProductsRightSectionWindow();
        String firstChildProductId = getChildProductId(firstchildProduct);
        String secondChildProductId = getChildProductId(secondChildProduct);
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        goToProductsRightSectionWindow();
        clkOnSelectBoxOfChildProduct(firstChildProductId);
        clkOnSelectBoxOfChildProduct(secondChildProductId);
    }

    /**
     * This method clicks on the selectbox of the child product
     * 
     * @param childProductId
     *            String object containing Id of the child product
     */
    private void clkOnSelectBoxOfChildProduct(String childProductId) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//table[contains(@class,'CSAdminList')]")));
        WebElement selectBox = browserDriver.findElement(
                By.xpath("//input[contains(@value,'" + childProductId + "')]"));
        selectBox.click();
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param childProductName
     *            String containing name of child product
     * @return child product Id String containing child product Id
     */
    private String getChildProductId(String childProductName) {
        goToPimProductTreeSection();
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(
                browserDriver.findElement(By.linkText(childProductName)))
                .perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        return browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
    }

    /**
     * This method selects the merge content option from the marked drop down
     * and also returns the current window handle
     * 
     * @return windowHandle
     */
    private String performMergeContentOperation() {
        String windowHandle = selectMergeContentOption();
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        CSLogger.info("clicked on ok of pop up for merge content operation");
        return windowHandle;
    }

    /**
     * This method clicks on Parent product folder
     * 
     * @param productFolderName
     *            String Object containing product folder name.
     */
    private void clkOnParentProductFolder(String productFolderName) {
        try {
            getCreatedProductFolder(productFolderName).click();
            CSLogger.info("Clicked on parent product folder");
        } catch (Exception e) {
            CSLogger.error("Parent folder not found", e);
            Assert.fail("Parent folder not found");
        }
    }

    /**
     * Returns the created product folder webElement
     * 
     * @param productFolderName
     *            String containing name of parent product folder
     * @return WebElement CreatedProductFolder
     */
    private WebElement getCreatedProductFolder(String productFolderName) {
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productFolderName)));
        return browserDriver.findElement(By.linkText(productFolderName));
    }

    /**
     * verifies whether a new window open ups with the list of selected products
     * 
     * @return merge content window handle
     */
    private void switchToMergeContentWindow() {
        try {
            goToProductsRightSectionWindow();
            switchToCurrentWindowHandle();
        } catch (NoSuchWindowException e) {
            CSLogger.error(
                    "A new window didn't opened up with the list of selected "
                            + "products after selecting merge content option",
                    e);
            Assert.fail("A new window didn't opened up with the list of "
                    + "selected "
                    + "products after selecting merge content option");
        }
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToProductsRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * Selects the drop down option of merge content and returns current window
     * handle
     * 
     * @return windowHandle
     */
    private String selectMergeContentOption() {
        goToProductsRightSectionWindow();
        String windowHandle = browserDriver.getWindowHandle();
        CSGuiListFooterInstance.selectOptionFromDrpDwnMassUpdateSelector(
                waitForReload, "Merge Content");
        return windowHandle;
    }

    /**
     * switch to current window handle.
     */
    private void switchToCurrentWindowHandle() {
        try {
            for (String windowHandle : browserDriver.getWindowHandles()) {
                browserDriver.switchTo().window(windowHandle);
            }
            CSLogger.info("Switched to new window handle");
        } catch (Exception e) {
            CSLogger.error("Error while switching to new window handle ", e);
        }
    }

    /**
     * Returns switch mode WebElement
     * 
     * @return WebElement
     */
    private WebElement getSwitchModeButton() {
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//div[@data='switchMode']")));
        return browserDriver.findElement(By.xpath("//div[@data='switchMode']"));
    }

    /**
     * Clicks on Distribute content icon
     */
    private void clkOnDistributeContentButton() {
        getSwitchModeButton().click();
        CSLogger.info("Clicked on distribute content page");
    }

    /**
     * Switches the Merge content page to distribute content page
     */
    private void switchToDistributeContentPage() {
        clkOnDistributeContentButton();
        performPageSwitchingVerification();
    }

    /**
     * Verifies that page is switched from merge content to distribue content
     * page
     */
    private void performPageSwitchingVerification() {
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//div[@data='switchMode']/span")));
        String getTextActiveContentPage = browserDriver
                .findElement(By.xpath("//div[@data='switchMode']/span"))
                .getText();
        if (getTextActiveContentPage.equals("Merge Content")) {
            CSLogger.info(
                    "Merge content page switched to Distribute content page :"
                            + " teststep passed");
        } else {
            CSLogger.error(
                    "Error while switching from merge content page to distribute"
                            + " content page");
            softAssertion
                    .fail("Error while switching from merge content page to"
                            + " distribute content page : teststep failed");
        }
    }

    /**
     * This method closes the merge content window tab and switches to main
     * window
     */
    private void closeDistributeContentWindowTab() {
        browserDriver.close();
        browserDriver.switchTo().window(mainWindowHandle);
        CSLogger.info(
                "Closed distribute content window tab and switched to main window");
    }

    /**
     * This is a data provider method contains array of parent product folder
     * and two child product names.
     * 
     * @return Array
     */
    @DataProvider(name = "switchMergeToDistributeContentPageTestData")
    public Object[][] getSwitchMergeToDistributePageContentData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"), dataSheetName);
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
        CSGuiListFooterInstance = new CSGuiListFooter(browserDriver);
        softAssertion = new SoftAssert();
    }
}
