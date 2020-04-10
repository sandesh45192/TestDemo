/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.awt.AWTException;
import java.awt.Robot;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimProductSplitareaContentRightPage;
import org.cs.csautomation.cs.pom.PimProductTabsPage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to for Product history Version deletion.
 * 
 * @author CSAutomation Team
 *
 */
public class ProductHistoryDeletionTest extends AbstractTest {

    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPopupDivPim             csPopupDiv;
    private PimProductTabsPage        pimProductTabs;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontal;
    private PimStudioSettingsNode     pimStudioSettingsNode;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private String                    testSheetName = "ProductHistoryDeletion";

    /**
     * This method test the history version deletion test case.
     * 
     * @param testData Array of String containing the test Data from Data
     *            driven.
     */
    @Test(dataProvider = "productTestData")
    public void createProduct(String... testData) {

        String parentProductName, childProductName, productClassName,
                productTabName, productRefTyprAttrName, productRefTyprAttrValue,
                productTextTyprAttrName, productTextTyprAttrValue;
        try {
            parentProductName = testData[0];
            childProductName = testData[1];
            productClassName = testData[2];
            productTabName = testData[3];
            productRefTyprAttrName = testData[4];
            productTextTyprAttrName = testData[5];
            productTextTyprAttrValue = testData[6];
            productRefTyprAttrValue = parentProductName;
        } catch (ArrayIndexOutOfBoundsException e) {
            CSLogger.debug("Missing parameter in the test method.", e);
            Assert.fail("Missing parameter in the test method.", e);
            return;
        }
        try {
            WebDriverWait waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPIMandExpandProductTree(waitForReload);

            // create parent product
            createParentProduct(waitForReload, pimStudioProductsNode,
                    parentProductName);
            CSLogger.info("Created parent product.");

            // create child product and assign class
            createChildProduct(parentProductName, childProductName,
                    waitForReload, productClassName);
            CSLogger.info("Created child product.");
            pimStudioProductsNode.expandNodesByProductName(waitForReload,
                    childProductName);

            // Traverse to given tab in product view
            clickOnTheGivenProductTab(productTabName, waitForReload);

            // Assign values to attributes
            assignValuesToAttributes(productRefTyprAttrName,
                    productRefTyprAttrValue, productTextTyprAttrName,
                    productTextTyprAttrValue, waitForReload);

            // doing false deletion
            goToProductView(waitForReload);
            doProductCheckOut(waitForReload);
            goToProductHistorySection(waitForReload);
            deleteProductHistory(waitForReload, false);

            // Read values post deletion
            goToProductView(waitForReload);
            checkAttributeValues(productRefTyprAttrName,
                    productTextTyprAttrName, productRefTyprAttrValue,
                    productTextTyprAttrValue, waitForReload);

            // doing true deletion
            waitForReload.until(
                    ExpectedConditions.frameToBeAvailableAndSwitchToIt(1));
            deleteProductHistory(waitForReload, true);

            // Read values post deletion
            goToProductView(waitForReload);
            checkAttributeValues(productRefTyprAttrName,
                    productTextTyprAttrName, productRefTyprAttrValue,
                    productTextTyprAttrValue, waitForReload);

            // checking if history got deleted
            waitForReload.until(
                    ExpectedConditions.frameToBeAvailableAndSwitchToIt(1));
            goToHistoryFrame(waitForReload);
            verifyVersionDeletion(waitForReload);

        } catch (Exception e) {
            CSLogger.debug("Exception occurred :", e);
            Assert.fail("Exception occurred :", e);
        }
    }

    /**
     * This method traverse to the history section of product.
     * 
     * @param waitForReload
     */
    private void goToProductHistorySection(WebDriverWait waitForReload) {
        PimProductSplitareaContentRightPage pimProductSplitareaContentRight = pimProductTabs
                .clickOnSpliareaContentRight();
        waitForReload
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(1));
        pimProductSplitareaContentRight.clickOnProductHistory();
    }

    /**
     * This method initiate the process of assigning values to attributes.
     * 
     * @param productRefTyprAttrName String name of Reference type of attribute
     * @param productRefTyprAttrValue String value for reference type of
     *            attribute
     * @param productTextTyprAttrName String name of Single line type of
     *            attribute
     * @param productTextTyprAttrValue String value for single line type of
     *            attribute
     * @param waitForReload
     */
    private void assignValuesToAttributes(String productRefTyprAttrName,
            String productRefTyprAttrValue, String productTextTyprAttrName,
            String productTextTyprAttrValue, WebDriverWait waitForReload) {
        clickOnTheReferenceAttributeField(productRefTyprAttrName,
                waitForReload);
        addReferenceToReferenceAttribute(waitForReload,
                productRefTyprAttrValue);
        goToProductView(waitForReload);
        addTextInTextField(productTextTyprAttrName, waitForReload,
                productTextTyprAttrValue);
        doProductCheckIn(waitForReload);
    }

    /**
     * This method retrieves values from product view and compare them with
     * provided values.
     * 
     * @param productRefTyprAttrName
     * @param productTextTyprAttrName
     * @param productRefTyprAttrValue
     * @param productTextTyprAttrValue
     * @param waitForReload
     */
    private void checkAttributeValues(String productRefTyprAttrName,
            String productTextTyprAttrName, String productRefTyprAttrValue,
            String productTextTyprAttrValue, WebDriverWait waitForReload) {
        CSUtility.tempMethodForThreadSleep(2000);
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[contains(@cs_name, '" + productRefTyprAttrName
                        + "')]/td[2]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]/div[1]/a[1]")));
        String referenceLabel = browserDriver
                .findElement(By.xpath(
                        "//tr[contains(@cs_name, '" + productRefTyprAttrName
                                + "')]/td[2]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]/div[1]/a[1]"))
                .getText();
        String textLabel = browserDriver
                .findElement(By.xpath("//tr[contains(@cs_name, '"
                        + productTextTyprAttrName + "')]/td[2]/input[1]"))
                .getAttribute("value");
        CSLogger.info("Retrieved values of atribtues");
        if (!(referenceLabel.equals(productRefTyprAttrValue)
                && textLabel.equals(productTextTyprAttrValue))) {
            Assert.fail("Attribute values did not matched.");
        }
    }

    /**
     * @param productTextTyprAttrName String name of single line attribute.
     * @param textAttrValue String value for single line attribute
     * @param waitForReload
     */
    private void addTextInTextField(String productTextTyprAttrName,
            WebDriverWait waitForReload, String textAttrValue) {
        WebElement element;
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
     * This method selects the product to be referred for reference type of
     * attribute.
     * 
     * @param productToRefer String name of the product to be referred
     * @param waitForReload
     */
    private void addReferenceToReferenceAttribute(WebDriverWait waitForReload,
            String productToRefer) {
        WebElement element;
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        SelectionDialogWindow selectionDialogWindow = new SelectionDialogWindow(
                browserDriver);
        selectionDialogWindow.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        waitForReload
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@id='idRecordsPdmarticle_Left']")));
        waitForReload.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "(//span[contains(text(), '" + productToRefer + "')])[1]")));
        element = browserDriver.findElement(By.xpath(
                "(//span[contains(text(), '" + productToRefer + "')])[1]"));
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(element).perform();
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Selected " + productToRefer
                + "as value for reference type attribute.");
    }

    /**
     * This method clicks on the reference type of attribute.
     * 
     * @param productRefTyprAttrName String name of the reference type of
     *            attribute.
     * @param waitForReload
     */
    private void clickOnTheReferenceAttributeField(
            String productRefTyprAttrName, WebDriverWait waitForReload) {
        WebElement element;
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[contains(@cs_name,'" + productRefTyprAttrName
                        + "')]/td[2]/div[1]/div[2]/img[1]")));
        element = browserDriver.findElement(By.xpath("//tr[contains(@cs_name,'"
                + productRefTyprAttrName + "')]/td[2]/div[1]/div[2]/img[1]"));
        element.click();
        CSLogger.info("Clicked on the reference attribute field.");
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String name of the tab in the product view.
     * @param waitForReload
     */
    private void clickOnTheGivenProductTab(String productTabName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * This method asserts if version still exits after deletion.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void verifyVersionDeletion(WebDriverWait waitForReload) {
        try {
            WebElement element = getDeletedHistoryVersion();
            waitForReload.until(ExpectedConditions.stalenessOf(element));
            element = getDeletedHistoryVersion();
            if (element == null) {
                CSLogger.info("History version deleted.");
            } else {
                CSLogger.error("History version didn't get deleted.");
                Assert.fail("History version didn't get deleted.");
            }
        } catch (Exception e) {
            CSLogger.info("History version deleted.");
        }
    }

    /**
     * This method returns the second version from History section.
     * 
     * @return element WebElement Object referring the deleted version.
     */
    private WebElement getDeletedHistoryVersion() {
        WebElement element;
        element = browserDriver
                .findElement(By.xpath("(//a[contains(@id, 'CS_change')])[2]"));

        return element;
    }

    /**
     * This method performs checkin operation on the product view page.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void doProductCheckIn(WebDriverWait waitForReload) {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarCheckIn(waitForReload);
        CSLogger.info("Product checkedIn");
        moveMouseUsingRobot();
    }

    /**
     * This method performs checkout operation on the product view page.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void doProductCheckOut(WebDriverWait waitForReload) {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarCheckOut(waitForReload);
        CSLogger.info("Product checkedOut");
        moveMouseUsingRobot();
    }

    /**
     * This method perform ok or cancel operation on the alert box depending
     * upon the value of the parameter isDelete.
     * 
     * @param waitForReload WebDriverWait Object
     * @param isDelete Boolean value for deletion
     */
    private void deleteProductHistory(WebDriverWait waitForReload,
            Boolean isDelete) {
        Alert alert;
        rightClickOnSecondVersion(waitForReload);
        alert = getAlertBox(waitForReload);
        if (isDelete) {
            alert.accept();
            CSLogger.info("Accepted the alert dialouge box.");
        } else {
            alert.dismiss();
            CSLogger.info("Dismissed the alert dialouge box.");
        }
    }

    /**
     * This method traverse to the product view page.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToProductView(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
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
            String childProductName, WebDriverWait waitForReload,
            String productClassName) {
        WebElement element = pimStudioProductsNode
                .expandNodesByProductName(waitForReload, parentProductName);
        CSUtility.rightClickTreeNode(waitForReload, element, browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuNewChild(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, childProductName);
        CSLogger.info(
                "Entered value " + childProductName + " in the textfield.");
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimProductTreeSection(waitForReload);
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(parentProductName));
        createdProductFolder.click();
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioSettingsNode.getBtnPimSettingsNode()));
        pimStudioSettingsNode.getBtnPimSettingsNode().click();
        CSLogger.info("Clicked on Settings Node");
        traverseToClassToDragAndDrop(waitForReload, createdProductFolder,
                childProductName, productClassName);
        performClick(waitForReload, "Extend");
        goToPimProductTreeSection(waitForReload);
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimProductTreeSection(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This test method which traverses upto the newly created class and
     * performs drag and drop on the newly created product folder
     * 
     * @param waitForReload contains the time in ms to wait on any element if
     *            needed
     * @param nameOfProduct contains name of the already created product in
     *            string format
     * @param nameOfClass contains name of class contains the name of the class
     * @param createdProductFolder contains name of already created product
     *            folder as webElement
     */
    public void traverseToClassToDragAndDrop(WebDriverWait waitForReload,
            WebElement createdProductFolder, String nameOfProduct,
            String nameOfClass) {
        try {
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioSettingsNode.getBtnPimSettingsClassesNode()));
            pimStudioSettingsNode.getBtnPimSettingsClassesNode().click();
            CSLogger.info("Clicked on Class Folder");
            waitForReload.until(ExpectedConditions
                    .visibilityOfElementLocated(By.linkText(nameOfClass)));
            WebElement className = browserDriver
                    .findElement(By.linkText(nameOfClass));
            CSUtility.tempMethodForThreadSleep(3000);
            className.click();
            waitForReload.until(ExpectedConditions
                    .visibilityOfElementLocated(By.linkText(nameOfProduct)));
            createdProductFolder = browserDriver
                    .findElement(By.linkText(nameOfProduct));
            Actions action = new Actions(browserDriver);
            Action dragDrop = action
                    .dragAndDrop(className, createdProductFolder).build();
            dragDrop.perform();
        } catch (Exception e) {
            CSLogger.error("Dragged and Dropped class to Product::" + e);
        }
    }

    /**
     * This method performs click on either extend or cancel or replace button
     * after drag and drop Cancel will cancel the functionality extend inherits
     * class to already existing classes in products Replace replaces already
     * existing classes with new one.
     * 
     * @param waitForReload contains time in ms to wait on any element if needed
     * @param selectButton contains button names Cancel ,Extend and Replace
     */
    public void performClick(WebDriverWait waitForReload, String selectButton) {
        IProductPopup productPopUp = new CSPopupDivPim(browserDriver);
        browserDriver.switchTo().defaultContent();
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        if (selectButton.equals("Cancel")) {
            waitForReload.until(ExpectedConditions.visibilityOf(
                    productPopUp.getBtnCsGuiModalDialogCancelButton()));
            productPopUp.getBtnCsGuiModalDialogCancelButton();
        } else if (selectButton.equals("Extend")) {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDialogContentId.getBtnExtend()));
            csGuiDialogContentId.getBtnExtend().click();
        } else {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDialogContentId.getBtnReplace()));
            csGuiDialogContentId.getBtnReplace().click();
        }
        CSLogger.info(
                "Performed click after dragging the class to the product");
    }

    /**
     * This method performs context click on the second version in the history
     * section.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void rightClickOnSecondVersion(WebDriverWait waitForReload) {
        WebElement element;
        goToHistoryFrame(waitForReload);
        element = getDeletedHistoryVersion();
        CSUtility.rightClickTreeNode(waitForReload, element, browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuDeleteVersion(), browserDriver);
    }

    /**
     * This method traverse to the frame which contains all the versions of the
     * product.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToHistoryFrame(WebDriverWait waitForReload) {
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("(//iframe[contains(@id, 'itemControl_frm_')])[4]")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@id='HistoryTree']")));
        CSLogger.info("Switched to History iFrame.");
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPIMandExpandProductTree(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
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
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, parentProductName);
        CSLogger.info(
                "Entered value " + parentProductName + " in the textfield.");
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimProductTreeSection(waitForReload);
    }

    /**
     * This method uses Robot class to move the mouse cursor to coordinates
     * x=500, y=500.
     */
    private void moveMouseUsingRobot() {
        try {
            new Robot().mouseMove(500, 500);
            CSLogger.info("Moved mouse to x=500, y=500");
        } catch (AWTException e) {
            CSLogger.fatal("Mouse not moveable after check-in/out", e);
        }
    }

    /**
     * This method returns the alert box.
     * 
     * @return Alert Instance of Alert class representing the DOM alert box.
     */
    private Alert getAlertBox(WebDriverWait waitForReload) {
        Alert alertBox;
        waitForReload.until(ExpectedConditions.alertIsPresent());
        alertBox = browserDriver.switchTo().alert();
        CSLogger.info("Switched to alert dialouge box.");

        return alertBox;
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of credentials
     */
    @DataProvider(name = "productTestData")
    public Object[][] getCredentailsTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"), testSheetName);

    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        pimProductTabs = new PimProductTabsPage(browserDriver);
        pimStudioSettingsNode = new PimStudioSettingsNode(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
    }
}
