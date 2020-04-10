/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
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
 * three child products during runtime and performs merge content operation on
 * child products and verify the result
 * 
 * 
 * @author CSAutomation Team
 *
 */
public class MergeProductContentTest extends AbstractTest {

    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim             csPopupDiv;
    private SoftAssert                softAssertion;
    private CSGuiListFooter           CSGuiListFooterInstance;
    public WebDriverWait              waitForReload;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;

    private String                    mergeProductContentSheetName = "MergeProductContent";

    /**
     * This test methods to creates a parent product folder and three child
     * products during runtime and performs merge content operation on child
     * products and verify the result
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param firstChildProductName String Object containing the child product
     *            name.
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param thirdChildProductName String Object containing the child product
     *            name.
     */
    @Test(priority = 1, dataProvider = "mergeProductContentTestData")
    public void testMergeProductContent(String parentProductName,
            String firstChildProductName, String secondChildProductName,
            String thirdChildProductName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandProductTree(waitForReload);
            createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                    parentProductName);
            CSLogger.info("Created parent product.");
            createChildProduct(parentProductName, firstChildProductName);
            createChildProduct(parentProductName, secondChildProductName);
            createChildProduct(parentProductName, thirdChildProductName);
            selectChildProductsToMergeContent(parentProductName,
                    firstChildProductName, secondChildProductName,
                    thirdChildProductName);
            String windowHandle = performMergeContentOperation();
            verifyMergeContentWindow(windowHandle, firstChildProductName,
                    secondChildProductName, thirdChildProductName);
            closeMergeContentWindowTab(windowHandle);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation Error in testMergeProductContent", e);
            Assert.fail("Automation Error in testMergeProductContent", e);
        }
    }

    /**
     * This test methods defines a child product as new master object
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param firstChildProductName String Object containing the child product
     *            name.
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param thirdChildProductName String Object containing the child product
     *            name.
     */
    @Test(priority = 2, dataProvider = "mergeProductContentTestData")
    public void testDefineNewMasterMergeContent(String parentProductName,
            String firstChildProductName, String secondChildProductName,
            String thirdChildProductName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            selectChildProductsToMergeContent(parentProductName,
                    firstChildProductName, secondChildProductName,
                    thirdChildProductName);
            String mainWindowHandle = performMergeContentOperation();
            checkMergeContentWindow();
            String mergeContentWindowHandle = getMergeContentWindowHandle();
            verifyMasterObjectInCheckedOutState(secondChildProductName);
            switchToMainWindowTab(mainWindowHandle);
            verifyMasterObjectInCheckedInState(parentProductName,
                    firstChildProductName, secondChildProductName,
                    thirdChildProductName, mergeContentWindowHandle);
            defineNewMasterObject(secondChildProductName);
            closeMergeContentWindowTab(mainWindowHandle);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation Error in : testDefineNewMaster", e);
            Assert.fail("Automation Error in : testDefineNewMaster", e);
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
     * Returns the created product folder webElement
     * 
     * @param productFolderName String containing name of parent product folder
     * @return WebElement CreatedProductFolder
     */
    private WebElement getCreatedProductFolder(String productFolderName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(productFolderName)));
        return browserDriver.findElement(By.linkText(productFolderName));
    }

    /**
     * This method clicks on attribute folder
     * 
     * @param productFolderName String Object containing product folder name.
     */
    private void clkOnParentProductFolder(String productFolderName) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.linkText(productFolderName)));
        getCreatedProductFolder(productFolderName).click();
    }

    /**
     * This method selects the child product so that merger content can be
     * performed
     * 
     * @param productFolderName
     * @param childProductOne String Object containing the child product name.
     * @param childProductTwo String Object containing the child product name.
     * @param childProductThree String Object containing the child product name.
     */
    private void selectChildProductsToMergeContent(String productFolderName,
            String childProductOne, String childProductTwo,
            String childProductThree) {
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        goToProductsRightSectionWindow();
        String childProductIdOne = getChildProductId(childProductOne);
        String childProductIdTwo = getChildProductId(childProductTwo);
        String childProductIdThree = getChildProductId(childProductThree);
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        goToProductsRightSectionWindow();
        clkOnSelectBoxOfChildProduct(childProductIdOne);
        clkOnSelectBoxOfChildProduct(childProductIdTwo);
        clkOnSelectBoxOfChildProduct(childProductIdThree);
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToProductsRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method clicks on the selectbox of the child product
     * 
     * @param childProductId String object containing Id of the child product
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
     * This method returns the ID of the product passed as argument
     * 
     * @param childProductName String containing name of child product
     * @return child product Id
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
     * This method verifies whether a new window open ups with the list of
     * selected products and the first selected product is the master (first
     * product column) and all other listed products attribute cells are marked
     * as red if values are different e.g. Product label and tabs in product are
     * represented as sections.
     * 
     * @param windowHandle String Object containing window handle
     * @param fristChildProductName String Object containing the child product
     *            name.
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param thirdChildProductName String Object containing the child product
     *            name.
     */
    private void verifyMergeContentWindow(String windowHandle,
            String fristChildProductName, String secondChildProductName,
            String thirdChildProductName) {
        checkMergeContentWindow();
        checkIfFirstProductMarkedAsMaster(fristChildProductName);
        checkDifferentAttributeValuesMarkedInRed(secondChildProductName,
                thirdChildProductName);
        checkProductTab();
    }

    /**
     * verifies whether a new window open ups with the list of selected products
     */
    private void checkMergeContentWindow() {
        try {
            goToProductsRightSectionWindow();
            switchToCurrentWindowHandle();
        } catch (NoSuchWindowException e) {
            CSLogger.error(
                    "A new window didn't opened up with the list of selected "
                            + "products after selecting merge content option",
                    e);
            softAssertion.fail("A new window didn't opened up with the list of "
                    + "selected "
                    + "products after selecting merge content option");
        }
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
            CSLogger.error("Error while switching to new window handle ");
        }
    }

    /**
     * Checks whether the first selected product is the master (first product
     * column)
     * 
     * @param fristChildProductName String Object containing the child product
     *            name.
     */
    private void
            checkIfFirstProductMarkedAsMaster(String fristChildProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//div[@class='CSGItemComparatorItemToolbar']/div"
                                + "/div/"
                                + "img[contains(@src,'radio-checked.svg')]//"
                                + "parent::div/"
                                + "parent::div/parent::div/parent::div/preceding"
                                + "-sibling" + "::div/div[1]/span")));
        WebElement getMasterProduct = browserDriver.findElement(By
                .xpath("//div[@class='CSGItemComparatorItemToolbar']/div/div/img"
                        + "[contains(@src,'radio-checked.svg')]//parent::div/"
                        + "parent::"
                        + "div/parent::div/parent::div/preceding-sibling::div/"
                        + "div[1]/" + "span"));
        String getMasterProductName = getMasterProduct.getText();
        if (getMasterProductName.equals(fristChildProductName))
            CSLogger.info(
                    "first product is marked as master product teststep passed");
        else {
            CSLogger.error(
                    "first product is not marked as master product teststep "
                            + "failed");
            softAssertion.fail("first product is not marked as master product "
                    + "teststep failed");
        }
    }

    /**
     * Verifies whether the attribute cells are marked as red if values are
     * different e.g. Product label
     * 
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param thirdChildProductName String Object containing the child product
     *            name.
     */
    private void checkDifferentAttributeValuesMarkedInRed(
            String secondChildProductName, String thirdChildProductName) {
        String getColorOfSecondProduct = browserDriver
                .findElement(By.xpath(
                        "//div[contains(text(),'" + secondChildProductName
                                + "')]/parent::div/parent::td"))
                .getCssValue("background-color");
        String getColorOfThirdProduct = browserDriver
                .findElement(By.xpath("//div[contains(text(),'"
                        + thirdChildProductName + "')]/parent::div/parent::td"))
                .getCssValue("background-color");
        if ((getColorOfSecondProduct.equals("rgba(255, 0, 0, 0.2)"))
                && (getColorOfThirdProduct.equals("rgba(255, 0, 0, 0.2)"))) {
            CSLogger.info(
                    " Products attribute cells are marked as red if values are "
                            + "different teststep passed");
        } else {
            CSLogger.error(
                    "Products attribute cells are not marked as red if values "
                            + "are different teststep passed");
            softAssertion
                    .fail("Different values of attributes are not marked in red "
                            + "teststep passed");
        }
    }

    /**
     * Verifies whether tabs in product are represented as sections.
     */
    private void checkProductTab() {
        int presenceOfProductTab = browserDriver
                .findElements(By.xpath("//div[contains(text(),'Properties')]"))
                .size();
        if (presenceOfProductTab != 0)
            CSLogger.info(
                    "Tabs in product are represented as sections. teststep "
                            + "passed");
        else {
            CSLogger.error(
                    "Tabs in product are not represented as sections teststep "
                            + "failed ");
            softAssertion
                    .fail("Tabs in product are not represented as sections "
                            + "teststep failed");
        }
    }

    /**
     * This method gets the tooltip text of child product
     * 
     * @return toolTipText
     */
    private String getToolTipOfMasterObjectButton(String childProductName) {
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//div[@class='CSGItemComparatorItemContent']/div"
                                + "/span[contains(text(),'" + childProductName
                                + "')" + "]/parent::div/parent::div/following-"
                                + "sibling::div/div[2]/div/div/img[cont"
                                + "ains(@src,'radio-unchecked.svg')]")));
        WebElement getNonMasterProduct = browserDriver.findElement(
                By.xpath("//div[@class='CSGItemComparatorItemContent']/div/span"
                        + "[contains(text(),'" + childProductName
                        + "')]/parent::div"
                        + "/parent::div/following-sibling::div/div[2]/div/div/img"
                        + "[contains(@src,'radio-unchecked.svg')]"));
        Actions hover = new Actions(browserDriver);
        hover.moveToElement(getNonMasterProduct).build().perform();
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//div[@id='CSTooltipContainer']")));
        String toolTipText = browserDriver
                .findElement(By.xpath("//div[@id='CSTooltipContainer']/div"))
                .getText();

        return toolTipText;
    }

    /**
     * Verifies that when product is in 'Checked-Out' state, Define as Master
     * Object not possible. Object is checked-out' tooltip display's
     */
    private void verifyMasterObjectInCheckedOutState(String childProductName) {
        String checkoutStateToolTip = getToolTipOfMasterObjectButton(
                childProductName);
        if (checkoutStateToolTip.equals(
                "Define as Master Object not possible. Object is checked-out"))
            CSLogger.info(
                    "When  product is in 'Checked-Out' state, Define as Master "
                            + "Object not possible.Object is checked-out' tooltip  "
                            + "display's teststep passed");
        else {
            CSLogger.error(
                    "When  product is in 'Checked-Out' state, Define as Master "
                            + "Object not possible. Object is checked-out' "
                            + "tooltip should didn't display teststep failed");
            softAssertion
                    .fail("When  product is in 'Checked-Out' state, Define as "
                            + "Master Object not possible. Object is checked-"
                            + "out' tooltip should didn't display teststep "
                            + "failed");
        }
    }

    /**
     * Switches the window to main window tab
     * 
     * @param windowHandle
     */
    private void switchToMainWindowTab(String windowHandle) {
        browserDriver.switchTo().window(windowHandle);
        CSLogger.info("Switched to main window tab");
    }

    /**
     * Closes the merge content window tab and switches back to main window tab
     * 
     * @param windowHandle
     */
    private void closeMergeContentWindowTab(String windowHandle) {
        browserDriver.close();
        browserDriver.switchTo().window(windowHandle);
        CSLogger.info(
                "Closed merge content window tab and switches to main window "
                        + "tab");
    }

    /**
     * Check's In a child product which is not a master object
     * 
     * @param childProductName String Object containing the child product name.
     */
    private void checkInNonMasterChildProduct(String childProductName) {
        goToPimProductTreeSection();
        getChildProduct(childProductName).click();
        checkInProduct();
    }

    /**
     * This method performs check in operation on product folder
     */
    public void checkInProduct() {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCheckIn());
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCheckIn(waitForReload);
        } catch (Exception e) {
            CSLogger.info("Product already in check In state");
        }
    }

    /**
     * Returns WebElement of a child product
     * 
     * @param childProductName
     * @return WebElement of a child product
     */
    private WebElement getChildProduct(String childProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(childProductName)));
        return browserDriver.findElement(By.linkText(childProductName));
    }

    /**
     * Verifies that when product is in 'Checked-In' state, Define as master
     * object.Object is checked-In' tooltip display's
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param firstChildProductName String Object containing the child product
     *            name.
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param thirdChildProductName String Object containing the child product
     *            name.
     * @param mergeContentWindowHandle String Object containing window handle of
     *            merge content window
     */
    private void verifyMasterObjectInCheckedInState(String productFolderName,
            String childProductOne, String childProductTwo,
            String childProductThree, String mergeContentWindowHandle) {
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        checkInNonMasterChildProduct(childProductTwo);
        switchToMergeContentWindowTab(mergeContentWindowHandle);
        clkOnRefreshButton();
        String checkInStateToolTip = getToolTipOfMasterObjectButton(
                childProductTwo);
        if (checkInStateToolTip.equals("Define as master object"))
            CSLogger.info(
                    "When  product is in 'Checked-In' state, Define as master "
                            + "object.Object is checked-In' tooltip  display's teststep"
                            + "passed");
        else {
            CSLogger.error(
                    "When  product is in 'Checked-In' state, Define as master "
                            + "object.Object is checked-In' tooltip should didn't "
                            + "display teststep failed");
            softAssertion
                    .fail("When product is in 'Checked-In' state, Define as "
                            + "master object.Object is checked-In' tooltip "
                            + "should didn't display teststep failed");
        }
    }

    /**
     * Clicks on refresh button from the merge content window
     */
    private void clkOnRefreshButton() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//div[@data='update']")));
        browserDriver.findElement(By.xpath("//div[@data='update']")).click();
    }

    /**
     * Defines a child product as new master object
     * 
     * @param newMasterObject
     */
    private void defineNewMasterObject(String newMasterObject) {
        clkOnDefineAsMasterObject(newMasterObject);
        verifyNewMasterObject(newMasterObject);
    }

    /**
     * Clicks on radio button of define as new master object
     * 
     * @param newMasterObject
     */
    private void clkOnDefineAsMasterObject(String newMasterObject) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//div[@class='CSGItemComparatorItemContent']"
                                + "/div/span" + "[contains(text(),'"
                                + newMasterObject + "')]/parent::div/parent::"
                                + "div/following-sibling::div/div[2]/div/div"
                                + "/img[contains(@src,'radio-unchecked.svg')]")));
        WebElement getNonMasterProduct = browserDriver.findElement(
                By.xpath("//div[@class='CSGItemComparatorItemContent']/div/span"
                        + "[contains(text(),'" + newMasterObject
                        + "')]/parent::"
                        + "div/parent::div/following-sibling::div/div[2]/div/div"
                        + "/img[contains(@src,'radio-unchecked.svg')]"));
        getNonMasterProduct.click();
        CSLogger.info("New master object defined");
    }

    /**
     * Verifies whether the child product is defined correctly as master object
     * 
     * @param newMasterObject
     */
    private void verifyNewMasterObject(String newMasterObject) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//div[@class='CSGItemComparatorItemToolbar']/div"
                                + "/div/"
                                + "img[contains(@src,'radio-checked.svg')]//"
                                + "parent::div/"
                                + "parent::div/parent::div/parent::div/preceding"
                                + "-sibling" + "::div/div[1]/span")));
        WebElement getMasterProduct = browserDriver.findElement(By
                .xpath("//div[@class='CSGItemComparatorItemToolbar']/div/div/img"
                        + "[contains(@src,'radio-checked.svg')]//parent::div/"
                        + "parent::"
                        + "div/parent::div/parent::div/preceding-sibling::div/"
                        + "div[1]/" + "span"));
        String getMasterProductName = getMasterProduct.getText();
        if (getMasterProductName.equals(newMasterObject))
            CSLogger.info(
                    "Defined new master object successfully teststep passed");
        else {
            CSLogger.error(
                    "Defining new master object failed,  teststep " + "failed");
            softAssertion.fail(
                    "Defining new master object failed,  teststep " + "failed");
        }
    }

    /**
     * Returns merge content window handle
     */
    private String getMergeContentWindowHandle() {
        String mergeContentWindowHandle = browserDriver.getWindowHandle();
        return mergeContentWindowHandle;
    }

    /**
     * Switches to merge content window tab
     * 
     * @param mergeContentWindowHandle
     */
    private void
            switchToMergeContentWindowTab(String mergeContentWindowHandle) {
        browserDriver.switchTo().window(mergeContentWindowHandle);
    }

    /**
     * This is a data provider method contains array of parent product folder
     * and three child product names.
     * 
     * @return Array
     */
    @DataProvider(name = "mergeProductContentTestData")
    public Object[][] getmergeProductContentData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                mergeProductContentSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        softAssertion = new SoftAssert();
        CSGuiListFooterInstance = new CSGuiListFooter(browserDriver);
        softAssertion = new SoftAssert();
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
    }
}
