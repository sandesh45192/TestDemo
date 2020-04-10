/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.pim;

import java.util.ArrayList;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to edit product for this test creates a
 * product at runtime and assigns a class which should have all required
 * attributes that needs to edited.
 * 
 * @author CSAutomation Team
 *
 */
public class EditProductTest extends AbstractTest {
    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private IProductPopup             productPopup;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private String                    dataSheetName = "EditProduct";

    /**
     * This test method checks whether different attributes are editable or not
     * 
     * @param productName
     *            String parameter which contains name of the of the product
     * @param textAttribute
     *            Attribute's name and value pair and type of single line text
     *            attribute eg.singleLineTextAttr1:ABC
     * @param languageAttribute
     *            Attribute's name and value pair and type of single line text
     *            attribute
     * @param searchAttribute
     *            Attribute's name and value pair and type of single line text
     *            attribute
     * @param inheritanceAttribute
     *            Attribute's name and value pair and type of single line text
     *            attribute
     * @param requiredAttribute
     *            Attribute's name and value pair and type of single line text
     *            attribute
     * @param referenceAttr1
     *            attribute's name and reference attribute type pair for
     *            eg.CsAutomationLabel1PIM:PIM
     * @param referenceMAMAttr
     *            attribute's name and reference attribute type pair
     * @param referenceCoreAttr
     *            attribute's name and reference attribute type pair
     */
    @Test(priority = 1, dataProvider = "EditProductTestData")
    public void testEditProductFolder(String productName, String textAttribute,
            String languageAttribute, String searchAttribute,
            String inheritanceAttribute, String requiredAttribute,
            String referencePIMAttr, String referenceMAMAttr,
            String referenceCoreAttr, String className, String textAttrTabName,
            String referenceAttrTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 80);
            switchToPimAndExpandProductTree(waitForReload);
            createProduct(productName,
                    pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                    productPopup.getCsGuiPopupMenuCreateNew());
            assignClassToProduct(productName, className);
            checkOutProduct(productName);
            goToRightSectionWindow(waitForReload, browserDriver);
            checkTextFieldsEditableOrNot(productName, textAttribute,
                    textAttrTabName, browserDriver);
            checkTextFieldsEditableOrNot(productName, languageAttribute,
                    textAttrTabName, browserDriver);
            checkTextFieldsEditableOrNot(productName, searchAttribute,
                    textAttrTabName, browserDriver);
            checkTextFieldsEditableOrNot(productName, inheritanceAttribute,
                    textAttrTabName, browserDriver);
            checkTextFieldsEditableOrNot(productName, requiredAttribute,
                    textAttrTabName, browserDriver);
            checkReferenceFieldsEditableOrNot(productName, referencePIMAttr,
                    referenceAttrTabName, waitForReload, browserDriver);
            checkReferenceFieldsEditableOrNot(productName, referenceMAMAttr,
                    referenceAttrTabName, waitForReload, browserDriver);
            checkReferenceFieldsEditableOrNot(productName, referenceCoreAttr,
                    referenceAttrTabName, waitForReload, browserDriver);
            checkInProduct();
        } catch (Exception e) {
            CSLogger.debug("Automation Error in Edit Product  test ", e);
            Assert.fail("Automation Error in Edit Product  test", e);
        }
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName
     *            String object containing name of product it can be child
     *            product or parent product
     * @param nodeElement
     *            WebElement of either products node or parent product
     * @param popupMenuOption
     *            WebElement containing menu option.
     */
    private void createProduct(String productName, WebElement nodeElement,
            WebElement popupMenuOption) {
        CSUtility.rightClickTreeNode(waitForReload, nodeElement, browserDriver);
        productPopup.selectPopupDivMenu(waitForReload, popupMenuOption,
                browserDriver);
        productPopup.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered value " + productName + " in the textfield.");
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimStudioTreeSection();
    }

    /***
     * This method performs checkout operation on product folder
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     */
    public void checkOutProduct(String productFolderName) {
        clkOnProductFolder(productFolderName);
        doProductCheckOut();
    }

    /**
     * This method clicks on product folder
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     */
    public void clkOnProductFolder(String productFolderName) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(productFolderName)));
        browserDriver.findElement(By.linkText(productFolderName)).click();
        CSLogger.info("Clicked on " + productFolderName);
    }

    /**
     * This method checks whether the text type of attribute are editable or not
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param attributeNameValuePair
     *            Attribute's name and value pair separated with colon and type
     *            of single line text attribute eg.singleLineTextAttr1:ABC
     */
    public void checkTextFieldsEditableOrNot(String productFolderName,
            String attributeNameValuePair, String productTabName,
            WebDriver browserDriver) {
        String[] textAttributeNameValueData = attributeNameValuePair.split(",");
        String[] textattributeNameValue = null;
        for (int i = 0; i < textAttributeNameValueData.length; i++) {
            textattributeNameValue = textAttributeNameValueData[i].split(":");
            checkProductTextFiledEditableOrNot(textattributeNameValue[0],
                    textattributeNameValue[1], productTabName, browserDriver);
        }
    }

    /**
     * This method checks whether the reference type of attribute are editable
     * or not
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param referenceAttributeNameValuePair
     *            Attribute's name and value pair separated with colon and type
     *            of single line text attribute eg.singleLineTextAttr1:ABC
     */
    public void checkReferenceFieldsEditableOrNot(String productFolderName,
            String referenceAttributeNameValuePair, String referenceTabName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        String[] referenceAttributeNameValueData = referenceAttributeNameValuePair
                .split(",");
        String[] referenceAttributeNameValue = null;
        for (int i = 0; i < referenceAttributeNameValueData.length; i++) {
            referenceAttributeNameValue = referenceAttributeNameValueData[i]
                    .split(":");
            handleAssignWindows(referenceAttributeNameValue[0],
                    referenceAttributeNameValue[1], productFolderName,
                    referenceTabName, waitForReload, browserDriver);
        }
    }

    /**
     * This method performs checkout operation on the product view page.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void doProductCheckOut() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        WebElement viewInfoTable = browserDriver.findElement(By.xpath(
                "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr"));
        ArrayList<WebElement> getFirstRow = (ArrayList<WebElement>) viewInfoTable
                .findElements(By.xpath("//td/a/img"));
        if ((getFirstRow.get(0).getAttribute("src")).endsWith("checkout.gif")) {
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCheckOut(waitForReload);
        } else if (getFirstRow.get(0).getAttribute("src")
                .endsWith("checkin.gif")) {
            CSLogger.info("Product already in checkout state");
        }
    }

    /**
     * This method checks whether the text fields of products are editable or
     * not and also performs verification with assert statements
     * 
     * @param attributeName
     *            String type that contains name of the attribute
     * @param attributeValue
     *            String type that contains value to be entered in text
     *            attribute
     */
    public void checkProductTextFiledEditableOrNot(String attributeName,
            String attributeValue, String productTabName,
            WebDriver browserDriver) {
        String requiredAttribute = checkRequiredAttribute(productTabName,
                browserDriver);
        if (requiredAttribute.equals(attributeName)) {
            clearDataFromTextField(attributeName, attributeValue,
                    productTabName);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            validateAlertBoxPresenceForRequiredAttribute();
            enterDataInTextField(attributeName, attributeValue, productTabName);
        } else {
            clearDataFromTextField(attributeName, attributeValue,
                    productTabName);
            enterDataInTextField(attributeName, attributeValue, productTabName);
            verifyTextFieldEditableOrNot(attributeName, attributeValue,
                    productTabName);
        }
    }

    /**
     * This methods enters data in text fields
     * 
     * @param attributeName
     *            String type that contains name of the attribute
     * @param attributeData
     *            String type that contains value to be entered in text
     *            attribute
     */
    public void enterDataInTextField(String attributeName, String attributeData,
            String productTabName) {
        clkOnTextField(attributeName, productTabName);
        browserDriver
                .findElement(By.xpath(
                        "//table/tbody/tr[contains(@cs_name,'" + attributeName
                                + "')]/td[2]/input[contains(@id,'PdmarticleCS_ItemAttribute')]"))
                .sendKeys(attributeData);
    }

    /**
     * This method gets the data that was entered in text attribute for
     * verification
     * 
     * @param attributeName
     *            String type that contains name of the attribute
     * @return value in the text field
     */
    public String getEnteredTextData(String attributeName,
            String productTabName) {
        clkOnTextField(attributeName, productTabName);
        return browserDriver
                .findElement(By.xpath(
                        "//table/tbody/tr[contains(@cs_name,'" + attributeName
                                + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                                + "ItemAttribute')]"))
                .getAttribute("value");
    }

    /**
     * This method performs operation of clicking on text fields
     * 
     * @param attributeName
     *            String type that contains name of the attribute
     */
    public void clkOnTextField(String attributeName, String productTabName) {
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]")));
        Actions actions = new Actions(browserDriver);
        WebElement elementLocator = browserDriver.findElement(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]"));
        actions.doubleClick(elementLocator).perform();
    }

    /**
     * This method performs operation of clearing the data from text fields
     * 
     * @param attributeName
     *            String type that contains name of the attribute
     * @param attributeData
     *            String type that contains value to be entered in text
     *            attribute
     */
    public void clearDataFromTextField(String attributeName,
            String attributeData, String productTabName) {
        clkOnTextField(attributeName, productTabName);
        browserDriver.findElement(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/input[contains(@id,'PdmarticleCS_"
                        + "ItemAttribute')]"))
                .clear();
        CSLogger.info("Cleared data from text fields");
    }

    /**
     * This method handles assigning data to different reference type of
     * attribute
     * 
     * @param attributeName
     *            String type that contains name of the attribute
     * @param attributeType
     *            String of reference attribute for eg.PIM,MAM,CORE
     * @param productFolderName
     *            String parameter that contains name of the product folder
     */
    public void handleAssignWindows(String attributeName, String attributeType,
            String productFolderName, String referenceTabName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        clickOnTheGivenProductTab(referenceTabName, waitForReload,
                browserDriver);
        switch (attributeType) {
        case "PIM":
            referenceToPimProduct(attributeName, productFolderName, false,
                    waitForReload, browserDriver);
            referenceToPimProduct(attributeName, productFolderName, true,
                    waitForReload, browserDriver);
            break;
        case "MAM":
            referenceToMamFile(productFolderName, attributeName, false,
                    waitForReload, browserDriver);
            referenceToMamFile(productFolderName, attributeName, true,
                    waitForReload, browserDriver);
            break;
        case "CORE":
            referenceToCoreUser(productFolderName, attributeName, false,
                    waitForReload, browserDriver);
            referenceToCoreUser(productFolderName, attributeName, true,
                    waitForReload, browserDriver);
            break;
        default:
            CSLogger.error("No reference attribute type found");
            break;
        }
    }

    /**
     * Performs usecase of assigning product to reference to pim attribute type
     * 
     * @param attributeName
     *            Contains name of attribute
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param pressOkay
     *            Boolean parameter that contains true and false values
     */
    public void referenceToPimProduct(String attributeName,
            String productFolderName, Boolean pressOkay,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        clkOnCSGuiSelectionListAdd(attributeName, waitForReload, browserDriver);
        selectReferenceToPimProductType(productFolderName, pressOkay,
                attributeName, waitForReload, browserDriver);
    }

    /**
     * This method selects product that needs to be assigned to referenceToPim
     * attribute
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param pressOkay
     *            Boolean parameter that contains true and false values
     * @param attributeName
     *            Contains name of attribute
     */
    public void selectReferenceToPimProductType(String productFolderName,
            Boolean pressOkay, String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSLogger.info("Opened product folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + productFolderName + "')]")));
        WebElement pimProduct = browserDriver.findElement(By
                .xpath("//span[contains(text(),'" + productFolderName + "')]"));
        Actions doubleClickAction = new Actions(browserDriver);
        doubleClickAction.doubleClick(pimProduct).perform();
        CSLogger.info("Double clicked on product ");
        CSUtility.tempMethodForThreadSleep(5000);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        if (pressOkay) {
            verifyPresenceOfAssignedProduct(productFolderName, attributeName,
                    waitForReload);
        } else {
            verifyAbsenceOfAssignedProduct(attributeName, waitForReload);
        }
    }

    /**
     * Checks whether the product is assigned to the attribute or not
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param attributeName
     *            Contains name of attribute
     */
    public void verifyPresenceOfAssignedProduct(String productFolderName,
            String attributeName, WebDriverWait waitForReload) {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//tr[contains(@cs_name,'" + attributeName
                            + "')]/td[2]/div/div/div/table/tbody/tr/td[2]/div/a")));
            Assert.assertEquals(productFolderName,
                    browserDriver.findElement(
                            By.xpath("//tr[contains(@cs_name,'" + attributeName
                                    + "')]/td[2]/div/div/div/table/tbody/tr/td"
                                    + "[2]/div/a"))
                            .getText());
            CSLogger.info("Product assigned properly after clicking ok on Data "
                    + "Selection Dialog window");
        } catch (Exception e) {
            CSLogger.error(
                    "Error while assigning product to Reference To Pim after "
                            + "clicking ok of Data Selection Dialog window",
                    e);
            Assert.fail(
                    "Error while assigning product to Reference To Pim after "
                            + "clicking ok of Data Selection Dialog window");
        }
    }

    /**
     * Verifies that after clicking on cancel button the product is not assigned
     * to that attribute
     * 
     * @param attributeName
     *            Contains name of attribute
     */
    public void verifyAbsenceOfAssignedProduct(String attributeName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        int pimProductAssigned = browserDriver.findElements(
                By.xpath("//tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td[2]"
                        + "/div/a"))
                .size();
        if (pimProductAssigned != 0) {
            CSLogger.error(
                    "Error while assigning Product by clicking on cancel of "
                            + "Data Selection Dialog window test case failed");
            Assert.fail(
                    "Error while assigning Product by clicking on cancel of "
                            + "Data Selection Dialog window test case failed");
        } else {
            CSLogger.info(
                    "Product was not assigned by clicking on cancel of Data"
                            + " Selection Dialog window test case passed");
        }
    }

    /**
     * This method clicks on plus
     * 
     * @param attributeName
     *            Contains name of attribute
     */
    public void clkOnCSGuiSelectionListAdd(String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/div/div[2]/img")));
        Actions actions = new Actions(browserDriver);
        WebElement elementLocator = browserDriver
                .findElement(By.xpath("//table/tbody/tr[contains(@cs_name,'"
                        + attributeName + "')]/td[2]/div/div[2]/img"));
        actions.moveToElement(elementLocator).build().perform();
        elementLocator.click();
    }

    /**
     * Assigns Mam file to Mam attribute
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param attributeName
     *            Contains name of attribute
     * @param pressOkay
     *            Boolean parameter that contains true and false values
     */
    public void referenceToMamFile(String productFolderName,
            String attributeName, Boolean pressOkay,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        clkOnCSGuiSelectionListAdd(attributeName, waitForReload, browserDriver);
        selectReferenceToMamProductType(productFolderName, pressOkay,
                attributeName, waitForReload, browserDriver);
    }

    /**
     * Clicks on plus of Mam attribute
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param pressOkay
     *            Boolean parameter that contains true and false values
     * @param attributeName
     *            Contains name of attribute
     */
    public void selectReferenceToMamProductType(String productFolderName,
            Boolean pressOkay, String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSLogger.info("Opened File folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonFilesFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillFileFoldersleftFrames(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText("Demo")));
        browserDriver.findElement(By.linkText("Demo")).click();
        selectionDialogWindowInstance
                .clkBtnCSGuiToolbarListFileSubFolders(waitForReload);
        clickOnThumbnailView();
        waitForReload.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("(//img[@class='CSMamFileThumb_DummyImage'])[1]")));
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[@class='CSMamFileThumb_Title'])[1]")));
        String assignedImageName = browserDriver
                .findElement(
                        By.xpath("(//div[@class='CSMamFileThumb_Title'])[1]"))
                .getText();
        WebElement selectFirstImage = browserDriver.findElement(
                By.xpath("(//img[@class='CSMamFileThumb_DummyImage'])[1]"));
        selectFirstImage.click();
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        if (pressOkay) {
            verifyPresenceOfAssignedMamFile(assignedImageName, attributeName,
                    waitForReload);
        } else {
            verifyAbsenceOfAssignedMamFile(attributeName, waitForReload);
        }
    }

    /**
     * Checks whether the Mam file is assigned to the attribute after clicking
     * on ok.
     * 
     * @param assignedFirstImage
     *            Image name that was assigned
     * @param attributeName
     *            Contains name of attribute
     */
    public void verifyPresenceOfAssignedMamFile(String assignedFirstImage,
            String attributeName, WebDriverWait waitForReload) {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//td[@class='CSGuiThumbListData CSGuiThumbListData"
                            + "Single']/div/a")));
            Assert.assertEquals(assignedFirstImage,
                    browserDriver
                            .findElement(By.xpath(
                                    "//tr[contains(@cs_name,'" + attributeName
                                            + "')]/td[2]/div/div/div/table/tbody/tr/"
                                            + "td[2]/div/a"))
                            .getText());
            CSLogger.info("Selected file  assigned to reference attribute "
                    + "successfully.");
        } catch (Exception e) {
            CSLogger.error(
                    "Error while assigning file to Reference To Mam after "
                            + "clicking ok of Data Selection Dialog window",
                    e);
            Assert.fail("Error while assigning file to Reference To Mam after "
                    + "clicking ok of Data Selection Dialog window");
        }
    }

    /**
     * Checks whether the Mam file is not assigned to the attribute after
     * clicking on cancel
     * 
     * @param attributeName
     *            Contains name of attribute
     */
    public void verifyAbsenceOfAssignedMamFile(String attributeName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        int mamFileAssigned = browserDriver.findElements(
                By.xpath("//tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/div/div/div/table/tbody/tr/td[2]"
                        + "/div/a"))
                .size();
        if (mamFileAssigned != 0) {
            CSLogger.error(
                    "Error while assigning File by clicking on cancel of File "
                            + "Selection Dialog window test case failed");
        } else {
            CSLogger.info("File was not assigned by clicking on cancel of File "
                    + "Selection Dialog window test case passed");
        }
    }

    /**
     * Assigns a reference of core user
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param attributeName
     *            Contains name of attribute
     * @param pressOkay
     *            Boolean parameter that contains true and false values
     */
    public void referenceToCoreUser(String productFolderName,
            String attributeName, Boolean pressOkay,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        clkOnCSGuiSelectionListAdd(attributeName, waitForReload, browserDriver);
        selectReferenceToCoreUserType(productFolderName, pressOkay,
                attributeName, waitForReload, browserDriver);
    }

    /**
     * Selects reference of core user type
     * 
     * @param productFolderName
     *            String parameter that contains name of the product folder
     * @param pressOkay
     *            Boolean parameter that contains true and false values
     * @param attributeName
     *            Contains name of attribute
     */
    public void selectReferenceToCoreUserType(String productFolderName,
            Boolean pressOkay, String attributeName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSLogger.info("Opened User  folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonUserFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillUserFoldersleftFrames(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText("Users")));
        browserDriver.findElement(By.linkText("Users")).click();
        CSLogger.info("Clicked on user tree");
        selectionDialogWindowInstance
                .clkBtnCSGuiToolbarListUserSubFolders(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("(//tr[@class='odd']/td[2])[1]")));
        String assignedUserName = browserDriver
                .findElement(By.xpath("(//tr[@class='odd']/td[2])[1]"))
                .getText();
        browserDriver.findElement(By.xpath("(//tr[@class='odd']/td[2])[1]"))
                .click();
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(3000);
        if (pressOkay) {
            verifyPresenceOfAssignedCoreUser(assignedUserName, attributeName,
                    waitForReload);
        } else {
            verifyAbsenceOfAssignedCoreUser(attributeName, waitForReload);
        }
    }

    /**
     * Checks whether the User is assigned to the attribute or not after
     * clicking on ok
     * 
     * @param assignedUserName
     *            Name of user that needs to be assigned
     * @param attributeName
     *            Contains name of attribute
     */
    public void verifyPresenceOfAssignedCoreUser(String assignedUserName,
            String attributeName, WebDriverWait waitForReload) {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("(//a[@class='CSGuiThumbListDataText']/span)[1]")));
            String expectedUserName = browserDriver
                    .findElement(By
                            .xpath("(//a[@class='CSGuiThumbListDataText']/span)[1]"))
                    .getText();
            String[] spiltUserName = expectedUserName.split(":");
            Assert.assertEquals(assignedUserName, spiltUserName[1].trim());
            CSLogger.info("Selected user  assigned to Reference To Core User "
                    + "successfully.");
        } catch (Exception e) {
            CSLogger.error(
                    "Error while assigning user to Reference To Core User after"
                            + " clicking ok of Data Selection Dialog window",
                    e);
            Assert.fail(
                    "Error while assigning user to Reference To Core User after"
                            + " clicking ok of Data Selection Dialog window");
        }
    }

    /**
     * Checks whether the User is not assigned to the attribute or not after
     * clicking on cancel button
     * 
     * @param attributeName
     *            Contains name of attribute
     */
    public void verifyAbsenceOfAssignedCoreUser(String attributeName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        int coreUserExist = browserDriver
                .findElements(
                        By.xpath("//span[contains(text(),'Username: admin')]"))
                .size();
        if (coreUserExist != 0) {
            CSLogger.error(
                    "Error while assigning User by clicking on cancel of Data "
                            + "Selection Dialog window test case failed");
            Assert.fail(
                    "Error while assigning User by clicking on cancel of Data "
                            + "Selection Dialog window test case failed");
        } else {
            CSLogger.info("User was not assigned by clicking on cancel of Data "
                    + "Selection Dialog window test case passed");
        }
    }

    /**
     * This method checks whether the current attribute is required attribute if
     * yes then first clicks on save button handles the pop up and then enters
     * the data in the required attribute field
     * 
     * @return requiredAttributeName
     */
    public String checkRequiredAttribute(String productTabName,
            WebDriver browserDriver) {
        clickOnTheGivenProductTab(productTabName, waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@class='CSGuiEditorLabelRequired']/..")));
        String getRequiredAttribute = browserDriver
                .findElement(By
                        .xpath("//span[@class='CSGuiEditorLabelRequired']/.."))
                .getText();
        String requiredAttributeName = getRequiredAttribute.substring(0,
                getRequiredAttribute.length() - 1);
        return requiredAttributeName;
    }

    /**
     * This method validates that when there is no value filled in required
     * attribute and if we click on save the pop up appears or not
     */
    public void validateAlertBoxPresenceForRequiredAttribute() {
        Alert alertBox = null;
        try {
            alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
            if (alertBox != null) {
                alertBox.accept();
            }
        } catch (NoAlertPresentException e) {
            CSLogger.error(
                    "No Alert appeared while data is not filled in required "
                            + "attribute field");
            Assert.fail(
                    "No Alert appeared while data is not filled in required "
                            + "attribute field");
        }
    }

    /**
     * Verifies whether the text field are editable or not
     * 
     * @param attributeName
     *            Contains name of attribute
     * @param attributeData
     *            Value of the attribute
     */
    public void verifyTextFieldEditableOrNot(String attributeName,
            String attributeData, String productTabName) {
        String verificationData = getEnteredTextData(attributeName,
                productTabName);
        Assert.assertEquals(attributeData, verificationData);
    }

    /**
     * This method performs checkin operation on product folder
     */
    public void checkInProduct() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        WebElement viewInfoTable = browserDriver.findElement(By.xpath(
                "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr"));
        ArrayList<WebElement> getFirstRow = (ArrayList<WebElement>) viewInfoTable
                .findElements(By.xpath("//td/a/img"));
        if ((getFirstRow.get(0).getAttribute("src")).endsWith("checkin.gif")) {
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCheckIn(waitForReload);
        } else if (getFirstRow.get(0).getAttribute("src")
                .endsWith("checkout.gif")) {
            CSLogger.info("Product already in checkin state");
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
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
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
    private void assignClassToProduct(String parentProductName,
            String className) {
        doubleClkOnParentProduct(parentProductName);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        selectClassFromDataSelectionDialogWindow(className);
        checkInProduct();
    }

    /**
     * Performs operation of double clicking on parent product
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     */
    private void doubleClkOnParentProduct(String parentProductName) {
        try {
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
            goToRightSectionWindow(waitForReload, browserDriver);
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
     * Returns the created product folder webElement
     * 
     * @param productFolderName
     *            String containing name of parent product folder
     * @return WebElement CreatedProductFolder
     */
    private WebElement getCreatedProductFolder(String productFolderName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productFolderName)));
        return browserDriver.findElement(By.linkText(productFolderName));
    }

    /**
     * Switches till spiltAreaMain frame
     */
    public void goToRightSectionWindow(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName
     *            String name of the tab in the product view.
     * @param waitForReload
     */
    public void clickOnTheGivenProductTab(String productTabName,
            WebDriverWait waitForReload, WebDriver browserDriver) {
        goToRightSectionWindow(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * Checks whether view is set to thumb nail if not clicks on tool bar button
     * thumb nail else does not performs any action.
     */
    private void clickOnThumbnailView() {
        try {
            csGuiToolbarHorizontalInstance.clkBtnThumbnailView(waitForReload);
        } catch (Exception e) {
            CSLogger.info("Thumbnail view is alredy set");
        }
    }

    /**
     * This is a data provider method contains array of product name and
     * attributes
     * 
     * @return Array String method contains array of product name and attributes
     */
    @DataProvider(name = "EditProductTestData")
    public Object[][] getEditProductData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"), dataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
    }
}
