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
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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
 * This class contains the test methods to create a parent product folder and
 * two child products during runtime and performs merge content operation to
 * replace non-master object data with master object(child products) data and
 * also verify the result
 * 
 * 
 * @author CSAutomation Team
 *
 */
public class MergeProductContentReplaceTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim             csPopupDiv;
    private SoftAssert                softAssertion;
    private CSGuiListFooter           CSGuiListFooterInstance;
    private String                    mainWindowHandle;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private IProductPopup             productPopup;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private String                    mergeProductContentCompLangSheetName = "MergeProductCompareLanguage";
    private String                    mergeProductContentReplaceSheetName  = ""
            + "MergeProductContentReplace";

    /**
     * This test method creates a parent product folder and two child products
     * during runtime and performs merge content operation to replace non-master
     * object data with master object(child products) data and also verify the
     * result
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param className String Object containing name of the class
     * @param productTextTyprAttrName String object containing name of the text
     *            attribute
     * @param firstChildProductName String Object containing the child product
     *            name.
     * @param firstChildAttrValue Contains text attribute value for first child
     *            product
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param secondChildAttrValue Contains text attribute value for second
     *            child product
     * @param productTabName String Object containing name of the product tab
     */
    @Test(priority = 1, dataProvider = "mergeProductContentReplaceTestData")
    public void testMergeProductContentReplace(String parentProductName,
            String className, String productTextTyprAttrName,
            String firstChildProductName, String firstChildAttrValue,
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
            assignValuesToChildProductAttributes(productTextTyprAttrName,
                    firstChildAttrValue, waitForReload, parentProductName,
                    firstChildProductName, productTabName);
            assignValuesToChildProductAttributes(productTextTyprAttrName,
                    secondChildAttrValue, waitForReload, parentProductName,
                    secondChildProductName, productTabName);
            selectChildProductsToMergeContent(parentProductName);
            mainWindowHandle = performMergeContentOperation();
            switchToMergeContentWindow();
            performReplaceContentOperation(false, productTextTyprAttrName);
            verifyReplacedData(secondChildAttrValue, false,
                    productTextTyprAttrName);
            performReplaceContentOperation(true, productTextTyprAttrName);
            verifyReplacedData(secondChildAttrValue, true,
                    productTextTyprAttrName);
            closeMergeContentWindowTab();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : testMergeProductContentReplace", e);
            Assert.fail("Automation error in : testMergeProductContentReplace",
                    e);
        }
    }

    /**
     * This test method creates a parent product folder and two child products
     * during runtime and performs merge content operation to change language of
     * master object and replace non-master object data with master object(child
     * products) data and also verify the result
     * 
     * @param compLangProductName String Object containing the product name
     *            under which child product will be created.
     * @param className String Object containing name of the class
     * @param attributeFolderName String Object containing attribute folder name
     * @param langDepAttrName String object containing language dependent
     *            attribute name
     * @param firstChildProductName String Object containing the child product
     *            name.
     * @param firstChildAttrValue Contains text attribute value for first child
     *            product
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param secondChildAttrValue Contains text attribute value for second
     *            child product
     * @param productTabName String Object containing name of the product tab
     * @param masterDefaultLanguage String Object containing language of master
     *            object
     */
    @Test(priority = 2, dataProvider = "mergeProductContentCompLangTestData")
    public void testMergeProductContentCompareLanguage(
            String compLangProductName, String className,
            String attributeFolderName, String langDepAttrName,
            String firstChildProductName, String firstChildAttrValue,
            String secondChildProductName, String secondChildAttrValue,
            String productTabName, String masterDefaultLanguage,
            String masterChangedLanguage) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandProductTree(waitForReload);
            createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                    compLangProductName);
            CSLogger.info("Created parent product.");
            createChildProduct(compLangProductName, firstChildProductName);
            createChildProduct(compLangProductName, secondChildProductName);
            enableLanguageDependentAttrField(attributeFolderName,
                    langDepAttrName);
            goToPimProductTreeSection();
            assignClassToParentProduct(compLangProductName, className);
            assignValuesToChildProductAttributes(langDepAttrName,
                    firstChildAttrValue, waitForReload, compLangProductName,
                    firstChildProductName, productTabName);
            assignValuesToChildProductAttributes(langDepAttrName,
                    secondChildAttrValue, waitForReload, compLangProductName,
                    secondChildProductName, productTabName);
            selectChildProductsToMergeContent(compLangProductName);
            mainWindowHandle = performMergeContentOperation();
            switchToMergeContentWindow();
            changeLanguageOfMasterObject(masterChangedLanguage);
            performReplaceContentOperation(false, langDepAttrName);
            verifyReplacedData(secondChildAttrValue, false, langDepAttrName);
            performReplaceContentOperation(true, langDepAttrName);
            verifyReplacedData(secondChildAttrValue, true, langDepAttrName);
            changeLanguageOfMasterObject(masterDefaultLanguage);
            verifyDefaultLanguageData(firstChildAttrValue, langDepAttrName);
            closeMergeContentWindowTab();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : testMergeProductContentCompareLanguage",
                    e);
            Assert.fail(
                    "Automation error in : testMergeProductContentCompareLanguage",
                    e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandProductTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload WebDriverWait Object
     * @param pimStudioProductsNode PimStudioProductsNode Object
     * @param parentProductName String Object containing the value for parent
     *            product Name.
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
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param childProductName String Object containing the child product name.
     * @param waitForReload WebDriverWait Object
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
     * @param productName String object containing name of product it can be
     *            child product or parent product
     * @param nodeElement WebElement of either products node or parent product
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
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimProductTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * Assigns the given class to the parent product folder
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param className String object containing name of the class
     */
    public void assignClassToParentProduct(String parentProductName,
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
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     */
    public void doubleClkOnParentProduct(String parentProductName) {
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
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String name of the tab in the product view.
     * @param waitForReload WebDriverWait Object
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
     * selects the class of given classname that will be assigned to the parent
     * product folder
     * 
     * @param className String object containing name of the class
     */
    public void selectClassFromDataSelectionDialogWindow(String className) {
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
     * Switches till spiltAreaMain frame
     */
    public void goToProductsRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method initiate the process of assigning values to attributes of
     * child products and also checks In the child product .
     * 
     * @param productTextTyprAttrName String name of Single line type of
     *            attribute
     * @param productTextTyprAttrValue String value for single line type of
     *            attribute
     * @param waitForReload WebDriverWait Object
     * @param productFolderName String contains name of product folder
     * @param childProductName String contains name of child product
     * @param productTabName String contains name of product tab
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
     * Enters the given values into text fields of child product text attribute
     * 
     * @param productTextTyprAttrName String name of single line attribute.
     * @param textAttrValue String value for single line attribute
     * @param waitForReload WebDriverWait Object
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
        element.sendKeys(textAttrValue);
        CSLogger.info(
                "Inserted " + textAttrValue + " as value in the text field.");
    }

    /**
     * This method clicks on Parent product folder
     * 
     * @param productFolderName String Object containing product folder name.
     */
    public void clkOnParentProductFolder(String productFolderName) {
        try {
            CSUtility.tempMethodForThreadSleep(3000);
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
     * @param productFolderName String containing name of parent product folder
     * @return WebElement CreatedProductFolder
     */
    public WebElement getCreatedProductFolder(String productFolderName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productFolderName)));
        return browserDriver.findElement(By.linkText(productFolderName));
    }

    /**
     * Clicks on child product
     * 
     * @param childProductName String object containing name of the child
     *            product
     */
    public void clkOnChildProduct(String childProductName) {
        getCreatedProductFolder(childProductName).click();
        CSLogger.info("Clicked on child product");
    }

    /**
     * Returns the created product folder webElement
     * 
     * @param productFolderName String containing name of parent product folder
     * @return WebElement CreatedProductFolder
     */
    public WebElement getChildProduct(String childProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(childProductName)));
        return browserDriver.findElement(By.linkText(childProductName));
    }

    /**
     * This method performs check in operation on child product
     * 
     * @param childProductName String contains name of child product
     */
    public void checkInProduct(String childProductName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//a[@class='GuiEditorSelected'][contains(text(),'"
                                + childProductName + "')]")));
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCheckIn(waitForReload);
        CSLogger.info("Checked In child product");
    }

    /**
     * This method selects the child product so that merge content can be
     * performed
     * 
     * @param productFolderName String Object containing the product name under
     *            which child product will be created.
     */
    public void selectChildProductsToMergeContent(String productFolderName) {
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
     * This method selects the merge content option from the marked drop down
     * and also returns the current window handle
     * 
     * @return windowHandle
     */
    public String performMergeContentOperation() {
        String windowHandle = selectMergeContentOption();
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        CSLogger.info("clicked on ok of pop up for merge content operation");
        return windowHandle;
    }

    /**
     * Selects the drop down option of merge content and returns current window
     * handle
     * 
     * @return windowHandle
     */
    public String selectMergeContentOption() {
        goToProductsRightSectionWindow();
        String windowHandle = browserDriver.getWindowHandle();
        CSGuiListFooterInstance.selectOptionFromDrpDwnMassUpdateSelector(
                waitForReload, "Merge Content");
        return windowHandle;
    }

    /**
     * verifies whether a new window open ups with the list of selected products
     * 
     * @return merge content window handle
     */
    public void switchToMergeContentWindow() {
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
     * switch to current window handle.
     */
    public void switchToCurrentWindowHandle() {
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
     * Returns merge content window handle
     */
    public String getMergeContentWindowHandle() {
        String mergeContentWindowHandle = browserDriver.getWindowHandle();
        return mergeContentWindowHandle;
    }

    /**
     * Performs operation to replace master object(child product) data with
     * non-master object data
     * 
     * @param isPressOkay Boolean object containing true or false values
     */
    public void performReplaceContentOperation(Boolean isPressOkay,
            String productTextTyprAttrName) {
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(By.xpath(
                        "(//div[contains(text(),'" + productTextTyprAttrName
                                + "')])" + "[1]/parent::td/parent::tr/td[3]")),
                browserDriver);
        CSUtility.tempMethodForThreadSleep(3000);
        WebElement nonMasterChildProductDataField = browserDriver.findElement(
                By.xpath("(//div[contains(text(),'" + productTextTyprAttrName
                        + "')])[1]/parent" + "::td/parent::tr/td[3]"));
        nonMasterChildProductDataField.click();
        CSLogger.info("Clicked on  non master child product data field");
        if (isPressOkay) {
            clkOnMergeButton();
            handleConfirmWindowDialogBox(isPressOkay);
        } else {
            clkOnReplaceButton(productTextTyprAttrName);
            clkOnMergeButton();
            handleConfirmWindowDialogBox(isPressOkay);
        }
    }

    /**
     * Clicks on replace button from merge content window
     */
    public void clkOnReplaceButton(String productTextTyprAttrName) {
        WebElement replaceButton = browserDriver
                .findElement(By.xpath("(//div[contains(text(),'"
                        + productTextTyprAttrName + "')])[1]/"
                        + "parent::td/parent::tr/td[3]/parent::tr/td[contains"
                        + "(@class,'diffText')]"
                        + "/div/div[@data='replaceAttributeValues']/img"));
        replaceButton.click();
        CSLogger.info("Clicked on replace button");
    }

    /**
     * Clicks on merge button from merge content window
     */
    public void clkOnMergeButton() {
        WebElement mergeButton = browserDriver
                .findElement(By.xpath("//button[@data-button-action='merge']"));
        mergeButton.click();
        CSLogger.info("Clicked on merge button");
    }

    /**
     * Handles Ok and Cancel operations of pop up window
     * 
     * @param isPressOkay Boolean object containing true or false values
     */
    public void handleConfirmWindowDialogBox(Boolean isPressOkay) {
        if (isPressOkay) {
            CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                    .findElement(By.xpath("//span[contains(text(),'OK')]")));
            browserDriver.findElement(By.xpath("//span[contains(text(),'OK')]"))
                    .click();

            CSLogger.info("Clicked on Ok button of popup");
        } else {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(
                            By.xpath("//span[contains(text(),'Cancel')]")));
            browserDriver
                    .findElement(
                            By.xpath("//span[contains(text(),'Cancel')" + "]"))
                    .click();
            CSLogger.info("Clicked on Cancel button of popup");
        }
    }

    /**
     * Verifies that if Ok is selected then the contents of master object is
     * replaced with contents of non-master object and if cancel is selected the
     * contents are not replaced
     * 
     * @param nonMasterObjectData text attribute value of second child product
     * @param isPressOkay Boolean object containing true or false values
     */
    public void verifyReplacedData(String nonMasterObjectData,
            Boolean isPressOkay, String productTextTyprAttrName) {
        CSUtility.tempMethodForThreadSleep(3000);
        if (isPressOkay) {
            CSUtility.scrollUpOrDownToElement(
                    browserDriver.findElement(By.xpath(
                            "(//div[contains(text(),'" + productTextTyprAttrName
                                    + "')])[1]/parent::td/parent"
                                    + "::tr/td[2]/div/div")),
                    browserDriver);
            WebElement masterObjectDataElement = browserDriver
                    .findElement(By.xpath("(//div[contains(text(),'"
                            + productTextTyprAttrName + "')])[1]"
                            + "/parent::td/parent::tr/td[2]/div/div"));
            CSUtility.tempMethodForThreadSleep(1000);
            String masterObjectData = masterObjectDataElement.getText();
            if (masterObjectData.equals(nonMasterObjectData)) {
                CSLogger.info(
                        "After clicking 'Ok', Master object data  replaced "
                                + "successfully with the Non-Master data object"
                                + " : teststep passed");
            } else {
                CSLogger.error(
                        "After clicking 'Ok', Master object data  didn't "
                                + "replaced with the Non-Master data object : "
                                + "teststep failed");
                softAssertion
                        .fail("After clicking 'Ok', Master object data  didn't "
                                + "replaced with the Non-Master data object "
                                + "teststep failed");
            }
        } else {
            CSUtility.scrollUpOrDownToElement(
                    browserDriver.findElement(By.xpath(
                            "(//div[contains(text(),'" + productTextTyprAttrName
                                    + "')])[1]/parent::td/parent"
                                    + "::tr/td[2]/div/div")),
                    browserDriver);
            WebElement masterObjectDataElement = browserDriver
                    .findElement(By.xpath("(//div[contains(text(),'"
                            + productTextTyprAttrName + "')])[1]"
                            + "/parent::td/parent::tr/td[2]/div/div"));
            String masterObjectData = masterObjectDataElement.getText();
            if (masterObjectData.equals(nonMasterObjectData)) {
                CSLogger.error(
                        "After clicking 'Cancel', Master object data should not"
                                + "  replace with the Non-Master data object "
                                + "teststep has error: teststep failed");
                softAssertion
                        .fail("After clicking 'Cancel', Master object data  "
                                + "should not replace with the Non-Master data "
                                + "object teststep has error: teststep failed");
            } else {
                CSLogger.info(
                        "'After clicking 'Cancel', Master object data didn't "
                                + "replace with the Non-Master data object' : "
                                + "teststep passed");
            }
        }
    }

    /**
     * This method closes the merge content window tab and switches to main
     * window
     */
    public void closeMergeContentWindowTab() {
        browserDriver.close();
        browserDriver.switchTo().window(mainWindowHandle);
        CSLogger.info(
                "Closed merge content window tab and switched to main window");
    }

    /**
     * This method enables the language dependent attribute field.
     * 
     * @param attributeFolderName String Object containing attribute folder name
     * @param attributeName String object containing attribute name
     */
    public void enableLanguageDependentAttrField(String attributeFolderName,
            String attributeName) {
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(attributeFolderName)));
        WebElement attributeFolder = browserDriver
                .findElement(By.linkText(attributeFolderName));
        attributeFolder.click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(attributeName)));
        WebElement langDependentAttr = browserDriver
                .findElement(By.linkText(attributeName));
        langDependentAttr.click();
        CSLogger.info("Clicked on language dependent attribute");
        goToProductsRightSectionWindow();
        csGuiDialogContentIdInstance
                .checkCbPdmarticleconfigurationLanguageDependent(waitForReload);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method changes language of master object with language passed as
     * argument
     * 
     * @param language
     */
    public void changeLanguageOfMasterObject(String language) {
        CSUtility.tempMethodForThreadSleep(2000);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("(//select)[1]")));
        WebElement masterObjectLang = browserDriver
                .findElement(By.xpath("(//select)[1]"));
        masterObjectLang.click();
        Select select = new Select(masterObjectLang);
        select.selectByVisibleText(language);
        CSLogger.info("Master object language changed to " + language);
    }

    /**
     * Verifies that after changing the master product to default language"
     * original data of master object display's
     * 
     * @param masterOriginalData text attribute value of master child product
     *            when default language is selected
     * @param productTextTypeAttrName language dependent text attribute name
     */
    public void verifyDefaultLanguageData(String masterOriginalData,
            String productTextTypeAttrName) {
        CSUtility.tempMethodForThreadSleep(5000);
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(By
                        .xpath("//tr[contains(@class,'CSGItemComparatorAttribute"
                                + "Data')]/td[contains(@title,'"
                                + productTextTypeAttrName + "')]//"
                                + "following-sibling::td[contains(@class,'CSGIte"
                                + "mComparatorMasterTD')]/div/div[contains(@clas"
                                + "s,'CSGItemComparatorItemAttributeOriginal')]")),
                browserDriver);
        WebElement masterObjectDataElement = browserDriver.findElement(By
                .xpath("//tr[contains(@class,'CSGItemComparatorAttributeData')]/td[cont"
                        + "ains(@title,'" + productTextTypeAttrName
                        + "')]//following-sibling::td[contains("
                        + "@class,'CSGItemComparatorMasterTD')]/div/div[contains(@class,"
                        + "'CSGItemComparatorItemAttributeOriginal')]"));
        String masterObjectData = masterObjectDataElement.getText();

        if (masterObjectData.equals(masterOriginalData)) {
            CSLogger.info(
                    "After changing the master product to default language"
                            + " original data of master object displayed"
                            + " : teststep passed");
        } else {
            CSLogger.error(
                    "After changing the master product to default language "
                            + "original data of master object didn't "
                            + "displayed : teststep failed");
            softAssertion.fail("After changing the master product to default "
                    + "language original data of master object didn't "
                    + "displayed teststep failed");
        }
    }

    /**
     * This is a data provider method contains array of parent product folder
     * and two child product names and attribute names and values,product tab
     * name.
     * 
     * @return Array
     */
    @DataProvider(name = "mergeProductContentReplaceTestData")
    public Object[][] getmergeProductContentReplaceData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                mergeProductContentReplaceSheetName);
    }

    /**
     * This is a data provider method contains array of parent product folder
     * and two child product names along with attribute names and values and
     * product tab name,language of master object.
     * 
     * @return Array
     */
    @DataProvider(name = "mergeProductContentCompLangTestData")
    public Object[][] getmergeProductContentCompareLangData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                mergeProductContentCompLangSheetName);
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
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
    }

}