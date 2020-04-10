/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
 * For this testcase to run a class should be assigned with a attribute folder
 * which should contain a 'single line text'attribute that should be already
 * present and created.
 * 
 * This class contains test method creates a parent product folder and two child
 * products during runtime and performs compare version operation by editing
 * master object data and sets the version of master object to latest version
 * 
 * @author CSAutomation Team
 *
 */
public class DistributeProductContentCompareTest extends AbstractTest {
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim                csPopupDiv;
    private SoftAssert                softAssertion;
    private CSGuiListFooter           CSGuiListFooterInstance;
    private WebDriverWait             waitForReload;
    private String                    mainWindowHandle;
    private String                    distributeContentWindowHandle;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private IProductPopup             productPopup;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private String                    dataSheetName =
            "DistributeProductCompareVersion";

    /**
     * This test method creates a parent product folder and two child products
     * during runtime and performs compare version operation by editing master
     * object data and sets the version of master object to latest version
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param className
     *            String Object containing name of the class
     * @param productTextTyprAttrName
     *            String object containing name of the text attribute
     * @param firstChildProductName
     *            String Object containing the child product name.
     * @param firstChildAttrValue
     *            Contains text attribute value for first child product
     * @param changedFirstChildAttrValue
     *            Contains text attribute value for latest version
     * @param secondChildProductName
     *            String Object containing the child product name.
     * @param secondChildAttrValue
     *            Contains text attribute value for second child product
     * @param productTabName
     *            String Object containing name of the product tab
     * 
     */
    @Test(priority = 1, dataProvider = "compareVersionTestData")
    public void testDistributeProductContentCompareVersions(
            String parentProductName, String className,
            String productTextTypeAttrName, String firstChildProductName,
            String firstChildAttrValue, String changedFirstChildAttrValue,
            String secondChildProductName, String secondChildAttrValue,
            String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandProductTree(waitForReload);
            createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                    parentProductName);
            CSLogger.info("Created parent product.");
            createChildProduct(parentProductName, firstChildProductName);
            createChildProduct(parentProductName, secondChildProductName);
            assignClassToParentProduct(parentProductName, className);
            assignValuesToChildProductAttributes(productTextTypeAttrName,
                    firstChildAttrValue, waitForReload, parentProductName,
                    firstChildProductName, productTabName);
            assignValuesToChildProductAttributes(productTextTypeAttrName,
                    secondChildAttrValue, waitForReload, parentProductName,
                    secondChildProductName, productTabName);
            selectChildProductsToDistributeContent(parentProductName);
            mainWindowHandle = performDistributeContentOperation();
            distributeContentWindowHandle = switchToDistributeContentWindow();
            performCompareVersionOperation(parentProductName,
                    productTextTypeAttrName, firstChildProductName,
                    changedFirstChildAttrValue, secondChildProductName,
                    secondChildAttrValue, productTabName);
            compareProductDataWithLatestVersion(productTextTypeAttrName,
                    changedFirstChildAttrValue);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : "
                    + "testDistributeProductContentCompareVersions",
                    e);
            Assert.fail(
                    "Automation error in : "
                    + "testDistributeProductContentCompareVersions",
                    e);
        }
    }

    /**
     * This test method clicks on compare object button of non-Master object and
     * verifies the result
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param className
     *            String Object containing name of the class
     * @param productTextTyprAttrName
     *            String object containing name of the text attribute
     * @param firstChildProductName
     *            String Object containing the child product name.
     * @param firstChildAttrValue
     *            Contains text attribute value for first child product
     * @param changedFirstChildAttrValue
     *            Contains text attribute value for latest version
     * @param secondChildProductName
     *            String Object containing the child product name.
     * @param secondChildAttrValue
     *            Contains text attribute value for second child product
     * @param productTabName
     *            String Object containing name of the product tab
     */
    @Test(priority = 2, dataProvider = "compareVersionTestData")
    public void testDistributeProductContentCompareObject(
            String parentProductName, String className,
            String productTextTypeAttrName, String firstChildProductName,
            String firstChildAttrValue, String changedFirstChildAttrValue,
            String secondChildProductName, String secondChildAttrValue,
            String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            checkDistributeContentWindowExists();
            clkOnNonMasterCompareObjectButton(secondChildProductName);
            verifyComparison(waitForReload, firstChildProductName,
                    secondChildProductName);
            closeDistributeContentWindowTab();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method :"
                    + "testDistributeProductContentCompareObject", e);
            Assert.fail("Automation error in test method :"
                    + "testDistributeProductContentCompareObject", e);
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
     * Assigns the given class to the parent product folder
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param className
     *            String object containing name of the class
     */
    private void assignClassToParentProduct(String parentProductName,
            String className) {
        doubleClkOnParentProduct(parentProductName);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        selectClassFromDataSelectionDialogWindow(className);
    }

    /**
     * performs operation of double clicking on parent product
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     */
    private void doubleClkOnParentProduct(String parentProductName) {
        try {
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement parentProduct = getCreatedProductFolder(
                    parentProductName);
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(By.linkText(parentProductName)));
            Actions action = new Actions(browserDriver);
            action.doubleClick(parentProduct).build().perform();
        } catch (Exception e) {
            CSLogger.error("Parent product folder not found", e);
            Assert.fail("Parent product folder not found");
        }
    }

    /**
     * selects the class of given classname that will be assigned to the parent
     * product folder
     * 
     * @param className
     *            String object containing name of the class
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
            goToProductsRightSectionWindow();
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
     * 
     * This method initiate the process of assigning values to attributes of
     * child products and also checks In the child product .
     * 
     * @param productTextTyprAttrName
     *            String name of Single line type of attribute
     * @param productTextTyprAttrValue
     *            String value for single line type of attribute
     * @param waitForReload
     *            WebDriverWait Object
     * @param productFolderName
     *            String contains name of product folder
     * @param childProductName
     *            String contains name of child product
     * @param productTabName
     *            String contains name of product tab
     */
    private void assignValuesToChildProductAttributes(
            String productTextTyprAttrName, String productTextTyprAttrValue,
            WebDriverWait waitForReload, String productFolderName,
            String childProductName, String productTabName) {
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        clkOnChildProduct(childProductName);
        goToProductsRightSectionWindow();
        clickOnTheGivenProductTab(productTabName, waitForReload);
        addTextInTextField(productTextTyprAttrName, waitForReload,
                productTextTyprAttrValue, childProductName);
        checkInProduct(childProductName);
    }

    /**
     * This method clicks on Parent product folder
     * 
     * @param productFolderName
     *            String Object containing product folder name.
     */
    private void clkOnParentProductFolder(String productFolderName) {
        try {
            CSUtility.tempMethodForThreadSleep(2000);
            WebElement parentProduct = getCreatedProductFolder(
                    productFolderName);
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(By.linkText(productFolderName)));
            parentProduct.click();
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
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(By.linkText(productFolderName)),
                browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productFolderName)));
        return browserDriver.findElement(By.linkText(productFolderName));
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
     * Clicks on child product
     * 
     * @param childProductName
     *            String object containing name of the child product
     */
    private void clkOnChildProduct(String childProductName) {
        getCreatedProductFolder(childProductName).click();
        CSLogger.info("Clicked on child product");
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToProductsRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName
     *            String name of the tab in the product view.
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void clickOnTheGivenProductTab(String productTabName,
            WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * Enters the given values into text fields of child product text attribute
     * 
     * @param productTextTyprAttrName
     *            String name of single line attribute.
     * @param textAttrValue
     *            String value for single line attribute
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void addTextInTextField(String productTextTyprAttrName,
            WebDriverWait waitForReload, String textAttrValue,
            String childProductName) {
        WebElement element;
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//a[@class='GuiEditorSelected'][contains(text(),'"
                                + childProductName + "')]")));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//tr[contains(@cs_name,'"
                        + productTextTyprAttrName + "')]/td[2]/input[1]")));
        element = browserDriver.findElement(By.xpath("//tr[contains(@cs_name,'"
                + productTextTyprAttrName + "')]/td[2]/input[1]"));
        element.clear();
        element.sendKeys(textAttrValue);
        CSLogger.info(
                "Inserted " + textAttrValue + " as value in the text field.");
    }

    /**
     * This method performs check in operation on child product
     * 
     * @param childProductName
     *            String contains name of child product
     */
    private void checkInProduct(String childProductName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//a[@class='GuiEditorSelected'][contains(text(),'"
                                + childProductName + "')]")));
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCheckIn(waitForReload);
        CSLogger.info("Checked In child product");
    }

    /**
     * This method selects the child product so that distribute content can be
     * performed
     * 
     * @param productFolderName
     *            String Object containing the product name under which child
     *            product will be created.
     */
    private void selectChildProductsToDistributeContent(
            String productFolderName) {
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        goToProductsRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.id("CSBuilderMarkAllActionTop")));
        WebElement selectBox = browserDriver
                .findElement(By.id("CSBuilderMarkAllActionTop"));
        selectBox.click();
    }

    /**
     * This method selects the distribute content option from the marked drop
     * down and also returns the current window handle
     * 
     * @return windowHandle
     */
    private String performDistributeContentOperation() {
        String windowHandle = selectDistributeContentOption();
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        CSLogger.info(
                "clicked on ok of pop up for distribute content operation");
        return windowHandle;
    }

    /**
     * verifies whether a new window open ups with the list of selected products
     * 
     * @return distribute content window handle
     */
    private String switchToDistributeContentWindow() {
        try {
            goToProductsRightSectionWindow();
            switchToCurrentWindowHandle();
        } catch (NoSuchWindowException e) {
            CSLogger.error(
                    "A new window didn't opened up with the list of selected "
                            + "products after selecting distribute content option",
                    e);
            Assert.fail("A new window didn't opened up with the list of "
                    + "selected "
                    + "products after selecting distribute content option");
        }
        return browserDriver.getWindowHandle();
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
     * Selects the drop down option of distribute content and returns current
     * window handle
     * 
     * @return windowHandle
     */
    private String selectDistributeContentOption() {
        goToProductsRightSectionWindow();
        String windowHandle = browserDriver.getWindowHandle();
        CSGuiListFooterInstance.selectOptionFromDrpDwnMassUpdateSelector(
                waitForReload, "Distribute Content");
        return windowHandle;
    }

    /**
     * This method switched to main window tab
     */
    private void switchToMainWindowTab() {
        browserDriver.switchTo().window(mainWindowHandle);
    }

    /**
     * This method edit values of child products and sets master object to
     * latest version
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param productTextTyprAttrName
     *            String object containing name of the text attribute
     * @param firstChildProductName
     *            String Object containing the child product name.
     * @param firstChildAttrValue
     *            Contains text attribute value for first child product
     * @param secondChildProductName
     *            String Object containing the child product name.
     * @param secondChildAttrValue
     *            Contains text attribute value for second child product
     * @param productTabName
     *            String Object containing name of the product tab
     * 
     */
    private void performCompareVersionOperation(String productFolderName,
            String productTextTypeAttrName, String firstChildProductName,
            String firstChildProductTextTypeAttrValue,
            String secondChildProductName,
            String secondChildProductTextTyprAttrValue, String productTabName) {
        switchToMainWindowTab();
        switchToPimAndExpandProductTree(waitForReload);
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        editValuesOfChildProductAttributes(productTextTypeAttrName,
                firstChildProductTextTypeAttrValue, productFolderName,
                firstChildProductName, productTabName);
        goToPimProductTreeSection();
        editValuesOfChildProductAttributes(productTextTypeAttrName,
                secondChildProductTextTyprAttrValue, productFolderName,
                secondChildProductName, productTabName);
        switchToDistributeContentWindowTab();
        clkOnRefreshButton();
        selectLatestVersionOfProduct(firstChildProductName);
    }

    /**
     * This method edit values of child products
     * 
     * @param productTextTypeAttrName
     *            Child product text attribute name
     * @param productTextTypeAttrValue
     *            Child product text attribute value
     * @param productFolderName
     *            String object containing product folder name
     * @param childProductName
     *            String object containing child product name
     * @param productTabName
     *            String object containing product tab name
     */
    private void editValuesOfChildProductAttributes(
            String productTextTypeAttrName, String productTextTypeAttrValue,
            String productFolderName, String childProductName,
            String productTabName) {
        clkOnChildProduct(childProductName);
        goToProductsRightSectionWindow();
        checkOutChildProduct(childProductName);
        clickOnTheGivenProductTab(productTabName, waitForReload);
        addTextInTextField(productTextTypeAttrName, waitForReload,
                productTextTypeAttrValue, childProductName);
        checkInProduct(childProductName);
    }

    /**
     * This method performs check out operation on child product
     * 
     * @param childProductName
     *            String contains name of child product
     */
    private void checkOutChildProduct(String childProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//a[@class='GuiEditorSelected'][contains(text(),'"
                                + childProductName + "')]")));
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCheckOut(waitForReload);
        CSLogger.info("Checked Out child product");
    }

    /**
     * Switches to distribute content window tab
     */
    private void switchToDistributeContentWindowTab() {
        browserDriver.switchTo().window(distributeContentWindowHandle);
        CSLogger.info("Switched to distribute content window tab");
    }

    /**
     * Clicks on distribute button from distribute content window
     */
    private void clkOnRefreshButton() {
        WebElement distributeButton = browserDriver
                .findElement(By.xpath("//span[contains(text(),'Refresh')]"));
        distributeButton.click();
        CSLogger.info("Clicked on Refresh button");
    }

    /**
     * Selects latest version of child product passed as arguments
     * 
     * @param childProductName
     *            String object containing child product name
     */
    private void selectLatestVersionOfProduct(String childProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + childProductName + "')]/parent::div/parent::"
                        + "div/following-sibling::div/div/select")));
        WebElement versionDrpDwn = browserDriver
                .findElement(By.xpath("//span[contains(text(),'"
                        + childProductName + "')]/parent::div/parent::"
                        + "div/following-sibling::div/div/select"));
        versionDrpDwn.click();
        versionDrpDwn.sendKeys(Keys.ARROW_UP);
        versionDrpDwn.click();
        CSLogger.info("Latest version selected");
    }

    /**
     * Verifies that the content of master object changes with latest version
     * data
     * 
     * @param productTextTypeAttrName
     *            String object containing text type attribute name
     * @param changedProductData
     *            String object containing changed value for verifying latest
     *            version attribute value
     */
    private void compareProductDataWithLatestVersion(
            String productTextTypeAttrName, String changedProductData) {
        clkOnShowDiffOnlyButton();
        verifyLatestVersionProductData(productTextTypeAttrName,
                changedProductData);
    }

    /**
     * Performs click operation on 'show diff only' button
     */
    private void clkOnShowDiffOnlyButton() {
        CSUtility.tempMethodForThreadSleep(3000);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//div[@data='viewDiffs']")));
        browserDriver.findElement(By.xpath("//div[@data='viewDiffs']")).click();
        CSLogger.info("Clicked on Show Diff only button");
    }

    /**
     * Verifies that master object's attribute data changes according to latest
     * version
     * 
     * @param productTextTypeAttrName
     *            String object containing text type attribute name
     * @param changedProductData
     *            String object containing changed value for verifying latest
     *            version attribute value
     */
    private void verifyLatestVersionProductData(String productTextTypeAttrName,
            String changedProductData) {
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(By.xpath("(//div[contains(text(),'"
                        + productTextTypeAttrName + "')])[1]/parent::td/parent"
                        + "::tr/td[2]/div/div")),
                browserDriver);
        WebElement masterObjectDataElement = browserDriver.findElement(
                By.xpath("(//div[contains(text(),'" + productTextTypeAttrName
                        + "')])[1]" + "/parent::td/parent::tr/td[2]/div/div"));
        String masterObjectData = masterObjectDataElement.getText();
        if (masterObjectData.equals(changedProductData)) {
            CSLogger.info("Product data changed according to latest version : "
                    + "teststep passed");
        } else {
            CSLogger.error(
                    "Product data didn't changed according to latest version"
                            + "teststep failed");
            softAssertion.fail("Product data didn't changed according to latest"
                    + " version teststep failed");
        }
    }

    /**
     * Verifies whether distribute content window exists and open
     */
    private void checkDistributeContentWindowExists() {
        String currentWindow = browserDriver.getWindowHandle();
        if (distributeContentWindowHandle.equals(currentWindow)) {
            CSLogger.info("Distribute content window exists");
        } else {
            CSLogger.error("Distribute content window is not open");
            Assert.fail("Distribute content window is not open");
        }
    }

    /**
     * Performs operation of clicking on non master compare object button
     * 
     * @param nonMasterProduct
     *            String object containing name of Non-master object(child
     *            product)
     */
    private void clkOnNonMasterCompareObjectButton(String nonMasterProduct) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//span[contains(text(),'" + nonMasterProduct
                                + "')]/parent::div/parent::div/parent"
                                + "::div/div[2]/div[2]/div/div[4]/img")));
        WebElement compareObjectButton = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + nonMasterProduct
                        + "')]/parent::div/parent::div/parent"
                        + "::div/div[2]/div[2]/div/div[4]/img"));
        compareObjectButton.click();
        CSLogger.info("Clicked on master object's child name : "
                + nonMasterProduct + " compare object button ");
    }

    /**
     * When compare object operation on Non-master object is performed verifies
     * that Non-master object differences are displayed in green color and
     * master object differences are displayed in red color" with
     * strike-through.
     */
    private void verifyComparison(WebDriverWait waitForReload,
            String masterObjectName, String nonMasterObjectName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//div[@class='CSGItemComparatorItemAttributeDiff"
                                + "']/span[contains(text(),'"
                                + nonMasterObjectName + "')]")));

        String nonMasterObjectLabelDifferences = browserDriver
                .findElement(By
                        .xpath("//div[@class='CSGItemComparatorItemAttributeDiff"
                                + "']/span[contains(text(),'"
                                + nonMasterObjectName + "')]"))
                .getAttribute("style");
        String masterObjectLabelDifferences = browserDriver.findElement(
                By.xpath("//div[@class='CSGItemComparatorItemAttributeDiff"
                        + "']/span[contains(text(),'" + masterObjectName
                        + "')]"))
                .getAttribute("style");

        if (nonMasterObjectLabelDifferences.contains("green")
                && masterObjectLabelDifferences.contains("line-through")) {
            CSLogger.info(
                    "Non master object differences displayed in green color and "
                            + "master object differences displayed in red color"
                            + " with strike-through. teststep : passed");
        } else {
            CSLogger.error(
                    "Non master object differences should display in green color "
                            + "and master object differences should display in "
                            + "red color with strike-through.teststep failed");
            softAssertion
                    .fail("Non master object differences should display in green "
                            + "color and master object differences should "
                            + "display in red color with strike-through.teststep "
                            + "failed");
        }
    }

    /**
     * Closes Distribute content window tab
     */
    private void closeDistributeContentWindowTab() {
        browserDriver.close();
        browserDriver.switchTo().window(mainWindowHandle);
        CSLogger.info(
                "Closed distribute content window tab and switched to main window");
    }

    /**
     * This is a data provider method contains array of parent product folder
     * and two child product names and attribute values and product tab name.
     * 
     * @return Array
     */
    @DataProvider(name = "compareVersionTestData")
    public Object[][] getDistributeProductContentCompVersionData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                dataSheetName);
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
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }
}
