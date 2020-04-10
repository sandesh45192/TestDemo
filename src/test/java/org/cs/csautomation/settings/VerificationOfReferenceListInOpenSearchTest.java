/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
 * This class contains test method to verify references list in open search
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfReferenceListInOpenSearchTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeader;
    private SearchPage                searchPage;
    private SoftAssert                softAssertion;
    private FrameLocators             locator;
    private String                    verificationOfReferenceListSheet = "VerificationOfReferenceList";
    private CSGuiToolbarHorizontal    guiToolbarHorizontal;
    private PimStudioSettingsNode     pimStudioSettingsNode;
    private CSPopupDivPim             popDivPim;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private IProductPopup             productPopUp;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalWidget            portalWidget;

    /**
     * This is the test method which drives the use case i.e. Verification of
     * reference list in open search
     * 
     * @param attrFolderName - String instance - label of attribute folder
     * @param lblRefToPimAttr - String instance - label of ref to pim attribute
     * @param lblForClass - String instance - label of class
     * @param productFolderName - String instance - label of product folder
     * @param lblProductReference - String instance - label of reference
     * @param lblUnreferencedProduct - String instance - label of unreferenced
     *            product
     * @param lblReferencedProduct - String instance - label of referenced
     *            product
     */
    @Test(dataProvider = "CreateDataForVerifyingReferenceListInOpenSearch")
    public void testVerificationOfReferenceListInOpenSearch(
            String attrFolderName, String lblRefToPimAttr, String lblForClass,
            String productFolderName, String lblProductReference,
            String lblUnreferencedProduct, String lblReferencedProduct) {
        try {
            createTestData(attrFolderName, lblRefToPimAttr, lblForClass,
                    productFolderName, lblProductReference,
                    lblUnreferencedProduct, lblReferencedProduct);
            addAttributeToProductConfigurations(attrFolderName);
            searchForData(lblUnreferencedProduct,
                    searchPage.getComboBoxOptionEverywhere());
            verifySearchResults();
            browserDriver.navigate().refresh();
            CSUtility.tempMethodForThreadSleep(2000);
            searchForData(lblReferencedProduct,
                    searchPage.getComboBoxOptionEverywhere());
            verifySearchResults();
            softAssertion.assertAll();
            CSLogger.info(
                    "Verification of reference list in open search test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerificationOfReferenceListInOpenSearch",
                    e);
            Assert.fail(
                    "Automation error : testVerificationOfReferenceListInOpenSearch",
                    e);
        }
    }

    /**
     * This method will verify search results
     */
    private void verifySearchResults() {
        String searchResult = searchPage.getResultsMetaText().getText();
        if (searchResult.equals("0 Results in Everywhere")) {
            softAssertion.assertFalse(
                    searchResult.equals("0 Results in Everywhere"),
                    "Error! Search Results are not being displayed!Either "
                            + "search keyword is wrong OR data related to "
                            + "searched keyword does not exist.");
            CSLogger.error("Error! Search Results are not being displayed! "
                    + "Either search keyword is wrong OR data related to "
                    + "searched keyword does not exist.");
        } else {
            softAssertion.assertFalse(
                    searchResult.equals("0 Results in Everywhere"));
            CSLogger.info("Search results are being displayed as expected.");
            enableAndVerifySelectionMode();
        }
    }

    /**
     * This method will enable the selection mode & verify whether all items are
     * in de-selected mode or not
     */
    private void enableAndVerifySelectionMode() {
        CSLogger.info("clicked on select mode button.");
        verifyPresenceOfElement(searchPage.getChkboxSelectionMode(),
                "Selection mode 'check box'");
        verifyPresenceOfElement(searchPage.getLinkIconSelectionMode(),
                "Selection mode 'Link icon'");
        verifySelectionModeEnabled();
        List<WebElement> selectedItems = browserDriver.findElements(
                By.xpath("//div[@class='CSListViewSectionContainer']/"
                        + "ul//li[@class='CSListViewItem selected']"));
        int countOfSelectedItems = selectedItems.size();
        if (countOfSelectedItems == 0) {
            softAssertion.assertTrue(countOfSelectedItems == 0);
            CSLogger.info("All items are in de-selected mode as expected.");
        } else {
            softAssertion.assertTrue(countOfSelectedItems == 0,
                    "Error! Some items might be selected. Not all items "
                            + "are in de-selected mode.");
            CSLogger.error("Error! Some items might be selected. Not all items "
                    + "are in de-selected mode.");
        }
    }

    /**
     * This method will verify whether any particular element is displayed or
     * not
     * 
     * @param wait - web driver wait
     * @param element - web element
     * @param label - label of element, which is to be verified
     */
    private void verifyPresenceOfElement(WebElement element, String label) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        boolean presence = element.isDisplayed();
        softAssertion.assertTrue(presence, label
                + " is not being displayed. Expected behaviour not being met!");
        if (presence) {
            CSLogger.info(label + " is displayed, as expected");
        } else {
            CSLogger.error(label + " is not being displayed. "
                    + "Expected behaviour not being met!");
        }
    }

    /**
     * This method will verify whether selection mode is enabled or not
     */
    private void verifySelectionModeEnabled() {
        boolean chkBox = searchPage.getChkboxSelectionMode().isDisplayed();
        boolean linkIcon = searchPage.getLinkIconSelectionMode().isDisplayed();
        if (chkBox && linkIcon) {
            softAssertion.assertTrue(chkBox && linkIcon);
            CSLogger.info(
                    "Selection mode is enabled, as check boxes & link icons "
                            + "are visible");

        } else {
            softAssertion.assertTrue(chkBox && linkIcon,
                    "Error! Selection mode is not enabled, as either or both "
                            + "check boxes & link icons are not visible.");
            CSLogger.error(
                    "Error! Selection mode is not enabled, as either or both "
                            + "check boxes & link icons are not visible.");
        }
    }

    /**
     * This method will search for data in 'Everywhere' option from search combo
     * box
     * 
     * @param searchBoxText - String instance - keyword using which we'll search
     *            for data
     * @param comboBoxOption - WebElement instance - WebElement of combo box
     *            option
     */
    private void searchForData(String searchBoxText,
            WebElement comboBoxOption) {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        enterTextInSearchBox(searchPage.getSearchField(), searchBoxText);
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);", comboBoxOption);
        searchPage.clkWebElement(waitForReload, comboBoxOption);
        CSUtility.tempMethodForThreadSleep(3000);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSLogger.info("clicked on search button of search page");
        CSUtility.tempMethodForThreadSleep(5000);
    }

    /**
     * This method will add attribute to product configuration
     * 
     * @param attrFolderName - String instance - label of attribute folder
     */
    private void addAttributeToProductConfigurations(String attrFolderName) {
        openSearchPortal();
        searchPage.clkWebElement(waitForReload,
                searchPage.getSearchPortalSettingsNode());
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        searchPage.clkWebElement(waitForReload, searchPage.getCoreNode());
        searchPage.clkWebElement(waitForReload,
                searchPage.getShareSearchPortal());
        CSLogger.info("clicked on search portal inside core");
        CSUtility.tempMethodForThreadSleep(2000);
        searchPage.clkWebElement(waitForReload,
                searchPage.getImgAddRefAttrToSearchPortal());
        CSUtility.tempMethodForThreadSleep(5000);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDailogLeftSectionInCollectionsAttributes(
                        waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                searchPage.getTxtAttributesFolder());
        WebElement createdAttrFolder = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + attrFolderName + "')]"));
        searchPage.clkWebElement(waitForReload, createdAttrFolder);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDailogCenterSectionInSearchPortalSettings(
                        waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnAddAllAttr());
        TraversingForSearchModule.traverseToFrmToAddAllRootFolderValues(waitForReload,browserDriver);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnYes());
        CSLogger.info("clicked on option yes");
        CSUtility.tempMethodForThreadSleep(2000);
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(5000);
    }

    /**
     * This method will open 'search portal'
     */
    private void openSearchPortal() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload, searchPage.getSearchNode());
        CSLogger.info("Clicked on search node");
        searchPage.clkWebElement(waitForReload,
                searchPage.getNodeSearchPortal());
        CSLogger.info("Clicked on option Search Portal");
    }

    /**
     * This method will create test data
     * 
     * @param attrFolderName - String instance - label of attribute folder
     * @param lblRefToPimAttr - String instance - label of ref to pim attribute
     * @param lblForClass - String instance - label of class
     * @param productFolderName - String instance - label of product folder
     * @param lblProductReference - String instance - label of reference
     * @param lblUnreferencedProduct - String instance - label of unreferenced
     *            product
     * @param lblReferencedProduct - String instance - label of referenced
     *            product
     */
    private void createTestData(String attrFolderName, String lblRefToPimAttr,
            String lblForClass, String productFolderName,
            String lblProductReference, String lblUnreferencedProduct,
            String lblReferencedProduct) {
        createAttributeFolderAndAddReferenceAttribute(attrFolderName,
                lblRefToPimAttr);
        createClassAndAssignReferenceAttribute(lblForClass, attrFolderName,
                lblRefToPimAttr);
        createProducts(productFolderName, lblProductReference,
                lblUnreferencedProduct, lblReferencedProduct, lblForClass);
    }

    /**
     * This method will create products, assign earlier created class to
     * products & add referenced product using reference to PIM product
     * attribute
     * 
     * @param lblForClass - String instance - label of class
     * @param productFolderName - String instance - label of product folder
     * @param lblProductReference - String instance - label of reference
     * @param lblUnreferencedProduct - String instance - label of unreferenced
     *            product
     * @param lblReferencedProduct - String instance - label of referenced
     *            product
     */
    private void createProducts(String productFolderName,
            String lblProductReference, String lblUnreferencedProduct,
            String lblReferencedProduct, String lblForClass) {
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        createProductsFolder(productFolderName);
        createProductsInsideFolder(attributePopup, productFolderName,
                lblProductReference, false, lblForClass);
        createProductsInsideFolder(attributePopup, productFolderName,
                lblUnreferencedProduct, false, lblForClass);
        createProductsInsideFolder(attributePopup, productFolderName,
                lblReferencedProduct, true, lblForClass);
    }

    /**
     * This method will create products inside products folder
     * 
     * @param lblForClass - String instance - label of class
     * @param productFolderName - String instance - label of product folder
     * @param lblProductReference - String instance - label of reference
     * @param lblUnreferencedProduct - String instance - label of unreferenced
     *            product
     * @param lblReferencedProduct - String instance - label of referenced
     *            product
     */
    private void createProductsInsideFolder(IAttributePopup attributePopup,
            String productFolderName, String productName,
            boolean addReferenceProduct, String lblForClass) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode());
        CSLogger.info("Clicked on products node");
        /*
         * Pass the product folder name through data provider in below method
         */
        CSUtility.rightClickTreeNode(waitForReload,
                getWebElement(productFolderName), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuNewChild(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        enterTextInSearchBox(popDivPim.getTxtCsGuiModalDialogFormName(),
                productName);
        CSLogger.info("Entered product name in text box");
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSLogger.info("product is successfully created");
        if (addReferenceProduct) {
            accessChild(productName, productFolderName);
            assignRefToPimProductAttrToChild(attributePopup, lblForClass);
            addPimReferenceToProduct(productName, attributePopup,
                    productFolderName);
        }
    }

    /**
     * This method will add reference to pim attribute to product
     * 
     * @param productName - String instance - label of product
     * @param attributePopup - IAttributePopup instance
     * @param productFolderName - String instance - label of product folder
     */
    private void addPimReferenceToProduct(String productName,
            IAttributePopup attributePopup, String productFolderName) {
        accessChild(productName, productFolderName);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getTabData());
        searchPage.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnReferenceToPimProduct());
        CSUtility.tempMethodForThreadSleep(2000);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDailogLeftSectionInSettingsHeader(
                        waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                getWebElement(productFolderName));
        CSUtility.tempMethodForThreadSleep(2000);
        searchPage.clkWebElement(waitForReload, getWebElement(productName));
        CSUtility.tempMethodForThreadSleep(2000);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("added reference to pim product");
    }

    /**
     * 
     * This method will assign class with ref to pim attribute to the product
     * 
     * @param attributePopup - IAttributePopup instance
     * @param lblForClass - String instance - label of class
     */
    private void assignRefToPimProductAttrToChild(
            IAttributePopup attributePopup, String lblForClass) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getTabProperties());
        searchPage.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getAddClassToProductByPlus());
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogLeftSection(
                waitForReload, browserDriver);
        Actions action = new Actions(browserDriver);
        action.doubleClick(getWebElement(lblForClass)).perform();
        CSLogger.info("Double clicked on class");
        CSUtility.tempMethodForThreadSleep(2000);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("successfully assigned class to referenced product");
    }

    /**
     * This method will access the child i.e. product
     * 
     * @param productName - String instance - label of product
     * @param productFolderName - String instance - label of product folder
     */
    private void accessChild(String productName, String productFolderName) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode());
        searchPage.clkWebElement(waitForReload,
                getWebElement(productFolderName));
        CSUtility.tempMethodForThreadSleep(2000);
        searchPage.clkWebElement(waitForReload, getWebElement(productName));
    }

    /**
     * this method will return the created class
     * 
     * @param lblForClass - String instance - label for class
     * @return createdClass
     */
    private WebElement getWebElement(String element) {
        WebElement locatedElement = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + element + "')]"));
        return locatedElement;
    }

    /**
     * This method will create a folder for products
     * 
     * @param productFolderName - String instance - label of product folder
     */
    private void createProductsFolder(String productFolderName) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopUp.enterValueInDialogue(waitForReload, productFolderName);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Products folder is successfully created");
    }

    /**
     * This method will create a new class & assign reference attribute to it
     * 
     * @param lblForClass - String instance - label for class
     * @param attrFolderName - String instance - label for folder
     * @param lblRefToPimAttr - String instance - label of ref to pim attribute
     */
    private void createClassAndAssignReferenceAttribute(String lblForClass,
            String attrFolderName, String lblRefToPimAttr) {
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        createClass(attributePopup, lblForClass);
        assignReferenceAttributeToClass(attributePopup, lblForClass,
                attrFolderName);
    }

    /**
     * This method will assign reference to pim attribute to created class
     * 
     * @param attributePopup - IAttributePopup instance - used to locate
     *            elements in attribute pop-up
     * 
     * @param attributePopup - IAttributePopup instance
     * @param lblForClass - String instance - label for class
     * @param attrFolderName - String instance - label for attributes folder
     */
    private void assignReferenceAttributeToClass(IAttributePopup attributePopup,
            String lblForClass, String attrFolderName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsNode());
        CSLogger.info("Clicked on pim settings node");
        searchPage.clkWebElement(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsClassesNode());
        WebElement createdClass = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + lblForClass + "')]"));
        searchPage.clkWebElement(waitForReload, createdClass);
        CSLogger.info("clicked on created class");
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnAddAttrToClass());
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogLeftSection(
                waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnAttrDrpDwnAddToClass());
        WebElement createdAttributeFolder = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + attrFolderName + "')]"));
        searchPage.clkWebElement(waitForReload, createdAttributeFolder);
        CSUtility.tempMethodForThreadSleep(2000);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogCenterSection(waitForReload,
                        browserDriver);
        searchPage.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnAddAllAttr());
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmToAddAllValues()));
        searchPage.clkWebElement(waitForReload, csGuiDialogContentIdInstance
                .getBtnYesToEnlistAllAttrToAddToClass());
        CSUtility.tempMethodForThreadSleep(2000);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Successfully assigned attribute to class");
    }

    /**
     * This method will create a new class
     * 
     * @param attributePopup - IAttributePopup instance - used to locate
     *            elements in attribute pop-up
     * 
     * @param lblForClass - String instance - label for class
     */
    private void createClass(IAttributePopup attributePopup,
            String lblForClass) {
        CSUtility.switchToDefaultFrame(browserDriver);
        locator.switchToTopFrame(waitForReload);
        searchPage.clkWebElement(waitForReload,
                csPortalHeader.getBtnProducts());
        CSLogger.info("clicked on Products header");
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsNode());
        CSLogger.info("Clicked on pim settings node");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsClassesNode());
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsClassesNode(),
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        enterTextInSearchBox(popDivPim.getTxtCsGuiModalDialogFormName(),
                lblForClass);
        CSLogger.info("Entered class name in text box");
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSLogger.info("class successfully created");
    }

    /**
     * This method will create reference attribute
     * 
     * @param attrFolderName - String instance - label of attribute folder
     * @param lblRefToPimAttr - String instance - label of ref to pim attribute
     */
    private void createAttributeFolderAndAddReferenceAttribute(
            String attrFolderName, String lblRefToPimAttr) {
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        createAttributeFolder(attributePopup, attrFolderName);
        createAndAddRefToPimAttribute(attributePopup, attrFolderName,
                lblRefToPimAttr);
    }

    /**
     * This method will create attribute of type 'Reference To PIM Product'
     * 
     * @param attributePopup - IAttributePopup instance - used to locate
     *            elements in attribute pop-up
     * @param attrFolderName - String instance - label of attribute folder
     * @param lblRefToPimAttr - String instance - label of ref to pim attribute
     */
    private void createAndAddRefToPimAttribute(IAttributePopup attributePopup,
            String attrFolderName, String lblRefToPimAttr) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsAttributeNode());
        WebElement attributeFolder = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + attrFolderName + "')]"));
        CSUtility.rightClickTreeNode(waitForReload, attributeFolder,
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        enterTextInSearchBox(searchPage.getTxtBoxAttributeName(),
                lblRefToPimAttr);
        searchPage.clkWebElement(waitForReload,
                searchPage.getTxtBoxAttributeType());
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSubCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmLeftAttributeTypeWindow()));
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnCrossReference());
        CSLogger.info("clicked on cross reference");
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnReferenceToPimProduct());
        CSLogger.info("clicked on reference to pim product");
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSubCsPortalWindowContent()));
        searchPage.clkWebElement(waitForReload,
                popDivPim.getBtnCsGuiModalDialogOkButton());
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSLogger.info("clicked on OK of alert box");
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * This method will create attribute folder
     * 
     * @param attributePopup - IAttributePopup instance - used to locate
     *            elements in attribute pop-up
     * 
     * @param attrFolderName - String instance - attribute folder label
     */
    private void createAttributeFolder(IAttributePopup attributePopup,
            String attrFolderName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        locator.switchToTopFrame(waitForReload);
        searchPage.clkWebElement(waitForReload,
                csPortalHeader.getBtnProducts());
        CSLogger.info("clicked on Products header");
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsNode());
        CSLogger.info("Clicked on pim settings node");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsAttributeNode());
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsAttributeNode(),
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuNewFolder(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        enterTextInSearchBox(popDivPim.getTxtCsGuiModalDialogFormName(),
                attrFolderName);
        CSLogger.info("Entered attribute folder name in text box");
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSLogger.info("Attribute folder successfully created");
    }

    /**
     * This method will enter text in search text box
     * 
     * @param searchBoxText - String instance - keyword using which we'll search
     *            for data
     * @param element - WebElement instance
     */
    private void enterTextInSearchBox(WebElement element,
            String searchBoxText) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.clear();
        element.sendKeys(searchBoxText);
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method initializes all the required instances for test
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 30);
        softAssertion = new SoftAssert();
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        pimStudioSettingsNode = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        popDivPim = new CSPopupDivPim(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNode = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
    }

    /**
     * This data provider contains data for verifying reference list in open
     * search
     * 
     * @return array of reference lists
     */

    @DataProvider(name = "CreateDataForVerifyingReferenceListInOpenSearch")
    public Object[][] getDataForVerificationOfReferenceList() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                verificationOfReferenceListSheet);
    }
}
