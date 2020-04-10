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
import org.cs.csautomation.cs.pom.PimProductSplitareaContentRightPage;
import org.cs.csautomation.cs.pom.PimProductTabsPage;
import org.cs.csautomation.cs.pom.PimStudioChannelsNode;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This test method contains methods to restore,rollback view versions.
 * 
 * @author CSAutomation Team
 *
 */
public class RestoreViewVersionsTest extends AbstractTest {

    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPopupDivPim             csPopupDiv;
    private PimProductTabsPage        pimProductTabs;
    private PimStudioSettingsNode     pimStudioSettingsNode;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private PimStudioChannelsNode     pimStudioChannelsNodeInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private IProductPopup             productPopup;
    private SoftAssert                softAssertion;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    public WebDriverWait              waitForReload;
    private FrameLocators             iframeLocatorsInstance;
    private String                    restoreViewVersionSheetName = "RestoreViewVersion";

    /**
     * This test method restores view version.
     * 
     * @param productName
     *            String Object contains name of product
     * @param productClassName
     *            String Object contains name of class
     * @param productTabName
     *            String Object contains name of the tab
     * @param productRefTypeAttributeName
     *            String Object contains name of the reference to core user
     *            attribute
     * @param coreUserGroupName
     *            String Object contains name of the user group
     * @param coreUserName
     *            String Object contains name of the user
     * @param ViewName
     *            String Object contains name of the view
     */
    @Test(priority = 1, dataProvider = "restoreViewVersionTestData")
    public void testRestoreViewVersions(String productName,
            String productClassName, String productTabName,
            String productRefTypeAttributeName, String coreUserGroupName,
            String coreUserName, String ViewName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPIMandExpandProductTree(waitForReload);
            createProduct(waitForReload, pimStudioProductsNode, productName);
            CSLogger.info("Product created successfully");
            assignClassToProduct(productName, productClassName);
            dragAndDropProductOntoView(productName, ViewName);
            clkOnProductPaneOfCreatedViewOfProduct(productName, ViewName,
                    productTabName);
            assignReferenceToReferenceAttribute(productRefTypeAttributeName,
                    coreUserGroupName, coreUserName);
            doProductCheckOut();
            goToProductHistorySection(waitForReload);
            restoreViewVersion(waitForReload, false, coreUserName,
                    productRefTypeAttributeName);
            goToProductHistorySection(waitForReload);
            restoreViewVersion(waitForReload, true, coreUserName,
                    productRefTypeAttributeName);
            checkCurrentVersionStatus();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testRestoreViewVersions",
                    e);
            Assert.fail(
                    "Automation Error in test method : testRestoreViewVersions",
                    e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void switchToPIMandExpandProductTree(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
    }

    /**
     * This method creates the product with supplied name.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     * @param pimStudioProductsNode
     *            PimStudioProductsNode Object
     * @param productName
     *            String Object contains the value for product
     */
    private void createProduct(WebDriverWait waitForReload,
            PimStudioProductsNodePage pimStudioProductsNode,
            String productName) {
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered value " + productName + " in the textfield.");
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimProductTreeSection(waitForReload);
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToPimProductTreeSection(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method assigns class to product by performing drag drop action
     * 
     * @param productName
     *            String Object contains name of product
     * @param productClassName
     *            String Object contains name of class
     */
    public void assignClassToProduct(String productName,
            String productClassName) {
        goToPimProductTreeSection(waitForReload);
        WebElement createdProduct = browserDriver
                .findElement(By.linkText(productName));
        createdProduct.click();
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioSettingsNode.getBtnPimSettingsNode()));
        pimStudioSettingsNode.getBtnPimSettingsNode().click();
        CSLogger.info("Clicked on Settings Node");
        traverseToClassToDragAndDrop(waitForReload, productName,
                productClassName);
        performClick(waitForReload, "Extend");
        goToPimProductTreeSection(waitForReload);
    }

    /**
     * This test method which traverses upto the newly created class and
     * performs drag and drop on the newly created product
     * 
     * @param waitForReload
     *            contains the time in ms to wait on any element if needed
     * @param productName
     *            contains name of already created product
     * @param nameOfClass
     *            contains name of class contains the name of the class
     */
    public void traverseToClassToDragAndDrop(WebDriverWait waitForReload,
            String productName, String nameOfClass) {
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
                    .visibilityOfElementLocated(By.linkText(productName)));
            WebElement createdProduct = browserDriver
                    .findElement(By.linkText(productName));
            Actions action = new Actions(browserDriver);
            Action dragDrop = action.dragAndDrop(className, createdProduct)
                    .build();
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
     * @param waitForReload
     *            contains time in ms to wait on any element if needed
     * @param selectButton
     *            contains button names Cancel ,Extend and Replace
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
     * This method drags the product and drops it on view
     * 
     * @param productName
     *            String contains name of product
     * @param viewName
     *            String contains name of the view
     */
    public void dragAndDropProductOntoView(String productName,
            String viewName) {
        clkOnPimProductsTree();
        CSUtility.tempMethodForThreadSleep(1000);
        clkOnPimChannelsTree();
        WebElement createdProduct = getCreatedProduct(productName);
        WebElement createdView = getCreatedView(viewName);
        Actions dragAndDropAction = new Actions(browserDriver);
        dragAndDropAction.dragAndDrop(createdProduct, createdView).build()
                .perform();
        CSLogger.info("dropped product on view");
        handleEnhanceItemProductOption();
        askBoxWindowOperationForReferenceProductDialog(true);
    }

    /**
     * This method performs operation of clicking on pim products Tree node
     */
    public void clkOnPimProductsTree() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioProductsNodeInstance.traverseToPimProductsNode(waitForReload,
                browserDriver);
        CSUtility.scrollUpOrDownToElement(
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method clicks on Pim Channels tree node
     */
    public void clkOnPimChannelsTree() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioChannelsNodeInstance.traverseToPimChannelsNode(waitForReload);
        CSUtility.scrollUpOrDownToElement(
                pimStudioChannelsNodeInstance.getBtnPimChannelsNode(),
                browserDriver);
        pimStudioChannelsNodeInstance.clkBtnPimChannelsNode(waitForReload);
    }

    /**
     * Returns the created product webElement
     * 
     * @param productName
     *            String contains name of product
     * @return WebElement CreatedProduct
     */
    public WebElement getCreatedProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(productName)));
        return browserDriver.findElement(By.linkText(productName));
    }

    /**
     * Returns the webElement of Created view
     * 
     * @param viewName
     *            String contains name of the view
     * @return webElement created view
     */
    public WebElement getCreatedView(String viewName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(viewName)));
        return browserDriver.findElement(By.linkText(viewName));
    }

    /**
     * Checks whether the Enhance Item Product option is enabled or not in
     * reference product dialog
     * 
     * @return true if Enhance Item product option is enabled false otherwise
     */
    public Boolean handleEnhanceItemProductOption() {
        traverseToCsPortalWindowContent();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                productPopup.getCbCsGuiDialogEnhanceItemProduct());
        Boolean isEnhanceItemProductOptionDisable = productPopup
                .getCbCsGuiDialogEnhanceItemProduct().getAttribute("class")
                .equals("CSGuiEditorCheckboxContainer Off "
                        + "Enabled GuiEditorCheckbox");
        if (isEnhanceItemProductOptionDisable) {
            CSLogger.error("Enabled Enhance Item Product Option is disabled "
                    + "which is error");
            softAssertion
                    .fail("Enabled Enhance Item Product Option is disabled "
                            + "which is error");
            productPopup.getCbCsGuiDialogEnhanceItemProduct().click();
            CSLogger.info(
                    "Enabled Item Enhance product option manually because by "
                            + "default it is not ON");
            return false;
        } else {
            CSLogger.info("Enhance Item Product Option is enabled");
            return true;
        }
    }

    /**
     * Switches the frame till CSPortalWindowContent frame
     */
    public void traverseToCsPortalWindowContent() {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.className("CSPortalWindow")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Swicted till  CSPortalWindowContent frame");
    }

    /**
     * This method selects ok or cancel of 'reference product' dialog depending
     * upon the argument ie false or true
     * 
     * @param pressOkay
     *            Boolean value contains true or false values
     */
    public void askBoxWindowOperationForReferenceProductDialog(
            Boolean pressOkay) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To Cs Portal Window Content Frame");
        if (pressOkay) {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("CSGUI_MODALDIALOG_INPUT_BUTTON_OK")));
            browserDriver
                    .findElement(
                            By.className("CSGUI_MODALDIALOG_INPUT_BUTTON_OK"))
                    .click();
            CSLogger.info("Clicked on OK Of Reference Product Popup ");
        } else {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL")));
            browserDriver
                    .findElement(By
                            .className("CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL"))
                    .click();
            CSLogger.info("Clicked on Cancel of Reference Product Popup");
        }
    }

    /**
     * This method perform click operation on created view of product
     * 
     * @param productName
     *            String contains name of product
     * @param viewId
     *            String contains name of view
     */
    public void clickOnCreatedViewOfProduct(String productName, String viewId,
            String viewName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(viewName)));
        browserDriver.findElement(By.linkText(viewName)).click();
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(
                        By.xpath("//span[contains(@id,'0divStructures@" + viewId
                                + "')]/span/table/tbody/tr/td/span/table/tbody"
                                + "/tr/td[2]/a/span/span[contains(text(),'"
                                + productName + "')]")),
                browserDriver);
        WebElement createdViewOfProduct = browserDriver.findElement(
                By.xpath("//span[contains(@id,'0divStructures@" + viewId
                        + "')]/span/table/tbody/tr/td/span/table/tbody/tr/td[2]"
                        + "/a/span/span[contains(text(),'" + productName
                        + "')]"));
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(createdViewOfProduct).perform();
        CSLogger.info("Clicked on created view of product");
    }

    /**
     * clicks on product's specified tab of created view of product
     * 
     * @param productName
     *            String contains name of product
     * @param viewName
     *            String contains name of view
     * @param productTabNames
     *            name of the tab
     */
    public void clkOnProductPaneOfCreatedViewOfProduct(String productName,
            String viewName, String productTabNames) {
        traverseToChannelFrames();
        String viewId = getIds(viewName);
        clickOnCreatedViewOfProduct(productName, viewId, viewName);
        clickOnProductPane(productTabNames);
    }

    /**
     * Switches the frames till pim studio frame
     */
    public void traverseToChannelFrames() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method gets the Id of product or view
     * 
     * @param components
     *            contains name of the product or view
     * @return Id of product or view
     */
    public String getIds(String component) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(browserDriver.findElement(By.linkText(component)))
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
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName
     *            String name of the tab in the product view.
     */
    private void clickOnProductPane(String productTabName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the " + productTabName + "tab");
    }

    /**
     * This method assigns user reference to core user reference attribute
     * 
     * @param assignedAttributeToProduct
     *            String object contains reference attribute name
     * @param userGroupName
     *            String object contains user group name
     * @param userName
     *            String object contains user name
     */
    public void assignReferenceToReferenceAttribute(
            String assignedAttributeToProduct, String userGroupName,
            String userName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//tr[contains(@cs_name,'"
                        + assignedAttributeToProduct + "')]")));

        int referenceAttributeExists = browserDriver
                .findElements(By.xpath(
                        "//tr[contains(@cs_name,'" + assignedAttributeToProduct
                                + "')]//td[2]/div/div[2]/img[@class='CSGuiSelectionListAdd']"))
                .size();

        if (referenceAttributeExists != 0) {
            CSLogger.info("Reference attribute exists ");
            WebElement referenceAttribute = browserDriver.findElement(By.xpath(
                    "//tr[contains(@cs_name,'" + assignedAttributeToProduct
                            + "')]//td[2]/div/div[2]/img[@class='CSGuiSelectionListAdd']"));
            referenceAttribute.click();
            TraverseSelectionDialogFrames.traverseTillUserFoldersleftFrames(
                    waitForReload, browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(By.id("User@0")));
            WebElement userTree = browserDriver.findElement(By.id("User@0"));
            userTree.click();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver
                            .findElement(By.xpath("//span[contains(text(),'"
                                    + userGroupName + " Users')]")));
            WebElement userGroupToBeSelected = browserDriver
                    .findElement(By.xpath("//span[contains(text(),'"
                            + userGroupName + " Users')]"));
            userGroupToBeSelected.click();
            TraverseSelectionDialogFrames.traverseTillUserFoldersCenterPane(
                    waitForReload, browserDriver);
            selectCoreUser(userName);
            productPopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            checkInProduct();
        } else {
            CSLogger.error("Reference attribute does not exists");
            Assert.fail("Reference attribute does not exists");
        }
    }

    /**
     * This method selects user from list of user
     * 
     * @param userToBeSelected
     *            name of the user to be selected
     */
    public void selectCoreUser(String userToBeSelected) {
        try {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//table[@class='hidewrap CSAdminList']")));
            WebElement viewCoreUserInfoTable = browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']"));
            ArrayList<WebElement> cells = (ArrayList<WebElement>) viewCoreUserInfoTable
                    .findElements(By.xpath("//tr[@id]/td[2]"));
            for (WebElement userName : cells) {
                if (userName.getText().equals(userToBeSelected)) {
                    userName.click();
                }
            }
        } catch (Exception e) {
            CSLogger.error("Error while selecting user");
        }
    }

    /**
     * This method performs check in operation on product
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
     * This method traverse to the history section of product.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToProductHistorySection(WebDriverWait waitForReload) {
        PimProductSplitareaContentRightPage pimProductSplitareaContentRight = pimProductTabs
                .clickOnSpliareaContentRight();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsSideBarBodyFrame()));
        pimProductSplitareaContentRight.clickOnProductHistory();
    }

    /**
     * This method perform ok or cancel operation on the alert box depending
     * upon the value of the parameter isDelete.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     * @param isDelete
     *            Boolean value for deletion
     */
    private void restoreViewVersion(WebDriverWait waitForReload,
            Boolean isRestore, String assignedUserName,
            String assignedAttributeToProduct) {
        Alert alert;
        rightClickOnSecondVersion(waitForReload, "restore");
        alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (isRestore) {
            alert.accept();
            CSLogger.info("Accepted the alert dialouge box.");
            verifyViewRestoredToPreviousVersion(assignedAttributeToProduct);
        } else {
            alert.dismiss();
            CSLogger.info("Dismissed the alert dialouge box.");
            verifyViewNotRestoreToPreviousVersion(assignedUserName);
        }
    }

    /**
     * This method performs context click on the second version in the history
     * section.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void rightClickOnSecondVersion(WebDriverWait waitForReload,
            String option) {
        WebElement element;
        goToHistoryFrame(waitForReload);
        if (option.equals("restore")) {
            element = getSecondHistoryVersion();
            CSUtility.rightClickTreeNode(waitForReload, element, browserDriver);
            csPopupDiv.selectPopupDivMenu(waitForReload,
                    csPopupDiv.getCsGuiPopupMenuRestoreVersion(),
                    browserDriver);
            CSLogger.info("Selected version Restore Option.");
        } else {
            element = getFirstHistoryVersionAfterCurrentVersion();
            CSUtility.rightClickTreeNode(waitForReload, element, browserDriver);
            csPopupDiv.selectPopupDivMenu(waitForReload,
                    csPopupDiv.getCsGuiPopupMenuRollbackVersion(),
                    browserDriver);
            CSLogger.info("Selected version Rollback Option.");
        }
    }

    /**
     * This method traverse to the frame which contains all the versions of the
     * product.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToHistoryFrame(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("(//iframe[contains(@id, 'itemControl_frm_')])[4]")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@id='HistoryTree']")));
        CSLogger.info("Switched to History iFrame.");
    }

    /**
     * This method returns the second entry from the current version from
     * History section.
     * 
     * @return element WebElement Object referring the deleted version.
     */
    private WebElement getSecondHistoryVersion() {
        WebElement element;
        element = browserDriver
                .findElement(By.xpath("(//a[contains(@id, 'CS_change')])[3]"));

        return element;
    }

    /**
     * This method verifies that view version is not restored after clicking on
     * cancel of popup
     * 
     * @param assignedUserName
     *            String object contains name of the user
     */
    public void verifyViewNotRestoreToPreviousVersion(String assignedUserName) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        int assignedReferenceExist = browserDriver
                .findElements(By.xpath(
                        "//span[contains(text(),'" + assignedUserName + "')]"))
                .size();
        if (assignedReferenceExist != 0) {
            CSLogger.info("After clicking on cancel the view didnt restore to "
                    + "previous version test step passed");

        } else {
            CSLogger.error(
                    "Error in step after clicking on cancel the view should not"
                            + " restore to previous version test step failed");
            softAssertion
                    .fail("Error in step after clicking on cancel the view  "
                            + "should not restore to previous version test step "
                            + "failed");
        }
    }

    /**
     * This method verifies that view version is restored after clicking on OK
     * of pop up.
     * 
     * @param assignedAttributeToProduct
     *            String object contains reference attribute name
     */
    public void verifyViewRestoredToPreviousVersion(
            String assignedAttributeToProduct) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        try {
            browserDriver.findElement(By.xpath("//tr[contains(@cs_name,'"
                    + assignedAttributeToProduct
                    + "')]//td[2]/div/div[2]/img[@class='CSGuiSelectionListAdd']"));
            CSLogger.info("After clicking on Ok the view restored to previous "
                    + "version test step passed");
        } catch (NoSuchElementException e) {
            CSLogger.error(
                    "After clicking on Ok the view  didnt restored to previous "
                            + "version test step failed");
            softAssertion
                    .fail("After clicking on Ok the view  didnt restored to "
                            + "previous version test step failed");
        }
    }

    /**
     * This method checks that after view is stored in current version a entry
     * contains label restored version is present.
     */
    public void checkCurrentVersionStatus() {
        try {
            goToProductHistorySection(waitForReload);
            goToHistoryFrame(waitForReload);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(By.linkText("Current Version")));
            WebElement currentVersion = browserDriver
                    .findElement(By.linkText("Current Version"));
            currentVersion.click();
            CSLogger.info("Clicked on Current version");
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(
                            By.xpath("//span[contains(text(),'restore')]")));
            CSLogger.info(
                    "Found label restored version  in current version entry "
                            + "test step passed");
        } catch (Exception e) {
            CSLogger.error(
                    "label restored version didnt display in current version "
                            + "entry");
            softAssertion
                    .fail("label restored version didnt display in current "
                            + "version entry");
        }
    }

    /**
     * This method test the rollback's view version.
     */
    @Test(priority = 2, dependsOnMethods = "testRestoreViewVersions")
    public void testRollbackViewVersions() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            doProductRollback(waitForReload, false);
            verifyVersionRollback(waitForReload, false);
            goToProductView(waitForReload);
            doProductRollback(waitForReload, true);
            verifyVersionRollback(waitForReload, true);
            goToProductView(waitForReload);
            verifyCheckoutButton();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testRollbackViewVersions",
                    e);
            Assert.fail(
                    "Automation Error in test method : testRollbackViewVersions");
        }
    }

    /**
     * This method performs context click and selects rollback option based on
     * the value of parameter isRollback.
     * 
     * @param waitForReload
     * @param isRollback
     */
    private void doProductRollback(WebDriverWait waitForReload,
            boolean isRollback) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        goToProductHistorySection(waitForReload);
        Alert alert;
        rightClickOnSecondVersion(waitForReload, "rollback");
        alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (isRollback) {
            alert.accept();
            CSLogger.info("clicked on ok of pop up for rollback");
        } else {
            alert.dismiss();
            CSLogger.info("clicked on cancel of pop up for rollback");
        }
    }

    /**
     * This method asserts if version still exits after deletion.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void verifyVersionRollback(WebDriverWait waitForReload,
            boolean isRollback) {
        if (isRollback) {
            int isVersionRollback = browserDriver
                    .findElements(By.linkText("Current Version")).size();
            if (isVersionRollback != 0) {
                CSLogger.error(
                        "After clicking  OK the version didn't rollbacked "
                                + "test step " + "failed");
                softAssertion
                        .fail("After clicking  OK the version didn't rollbacked "
                                + "test step " + "failed");
            } else {
                CSLogger.info(
                        "After clicking  OK the version  rollbacked successfully "
                                + "test step passed");
            }
        } else {
            goToProductView(waitForReload);
            goToProductHistorySection(waitForReload);
            goToHistoryFrame(waitForReload);
            CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                    .findElement(By.xpath("//div[@class='GuiTree']")));
            int isVersionRollback = browserDriver
                    .findElements(By.linkText("Current Version")).size();
            if (isVersionRollback != 0) {
                CSLogger.info(
                        "After clicking cancel the version  didn't rollbacked "
                                + " test step passed");
            } else {
                CSLogger.error(
                        "Error in step 'After clicking  cancel the version "
                                + "didn't rollbacked'  test step failed ");
                softAssertion
                        .fail("Error in step 'After clicking cancel the version "
                                + "didn't rollbacked' test step failed");
            }
        }
    }

    /**
     * This method gets the first history version after current version
     * 
     * @return
     */
    private WebElement getFirstHistoryVersionAfterCurrentVersion() {
        WebElement element;
        element = browserDriver
                .findElement(By.xpath("(//a[contains(@id, 'CS_change')])[2]"));

        return element;
    }

    /**
     * This method checks if the checkout button is present or not.
     */
    private void verifyCheckoutButton() {
        try {
            WebElement element = null;
            element = csGuiToolbarHorizontalInstance
                    .getBtnCSGuiToolbarCheckOut();
        } catch (NoSuchElementException e) {
            Assert.fail("History Rollback failed.");
            CSLogger.info("History Rollback failed : ", e);
        }
    }

    /**
     * This method traverse to the product view page.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToProductView(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This is a data provider method contains array of product name,class
     * name,user group name,user name,reference to core user attribute
     * name,product tab name,view name
     * 
     * @return Array
     */
    @DataProvider(name = "restoreViewVersionTestData")
    public Object[][] getrestoreViewVersionData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                restoreViewVersionSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        pimProductTabs = new PimProductTabsPage(browserDriver);
        pimStudioSettingsNode = new PimStudioSettingsNode(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
        pimStudioChannelsNodeInstance = new PimStudioChannelsNode(
                browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
    }
}
