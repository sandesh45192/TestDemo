/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
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
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
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
 * This class contains the test method to configure shares in search portal
 * 
 * @author CSAutomation Team
 *
 */
public class ConfigurationOfSharesTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private FrameLocators             locator;
    private CSPortalHeader            csPortalHeader;
    private CSGuiToolbarHorizontal    guiToolbarHorizontal;
    private String                    sharesConfigSheet = "ConfigurationOfShares";
    private SearchPage                searchPage;
    private SoftAssert                softAssertion;
    private CSPopupDivPim             popupDiv;
    private CSPortalWidget            portalWidget;
    private IProductPopup             productPopUp;
    private PimStudioProductsNodePage pimStudioProductsNode;

    /**
     * This is the test method which drives the usecase, ie. create share &
     * configure the search plugin.
     * 
     * @param searchAreaLabel String object - user defined label for search
     *            area, passed through excel sheet
     * @param productFolder - String instance - label of products folder
     * @param productLabel - String instance - label of product
     */
    @Test(dataProvider = "configurationOfShares", priority = 1)
    public void testConfigurationOfShares(String searchAreaLabel,
            String productFolder, String productLabel) {
        try {
            createTestData(productFolder, productLabel);
            openSearchPortal();
            configurationOfShares(searchAreaLabel, productFolder);
            softAssertion.assertAll();
            CSUtility.switchToDefaultFrame(browserDriver);
            CSLogger.info("Configuration of shares test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testConfigurationOfShares", e);
            Assert.fail("Automation error : testConfigurationOfShares", e);
        }
    }

    /**
     * This is the test method which drives the usecase, ie. verify the newly
     * created search area & search plugin
     * 
     * @param searchAreaLabel String object - user defined label for search
     *            area, passed through excel sheet
     */
    @Test(dataProvider = "configurationOfShares", priority = 2)
    public void testVerifySearchPluginInListViewOfCategories(
            String searchAreaLabel, String productFolder, String productLabel) {
        try {
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, locator);
            WebElement searchArea = browserDriver.findElement(By
                    .xpath("//td[contains(text(),'" + searchAreaLabel + "')]"));
            verifyPresenceOfElement(searchArea, searchAreaLabel);
            softAssertion.assertAll();
            CSLogger.info(
                    "Verify search plugin in list view of categories test completed");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerifySearchPluginInListViewOfCategories",
                    e);
            Assert.fail(
                    "Automation error : testVerifySearchPluginInListViewOfCategories",
                    e);
        }
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
        clickOnSearch();
        clickOnSearchPortal();
    }

    /**
     * This method will click on 'Search Portal' dropdown in 'settings-system
     * preferences-search'
     */
    private void clickOnSearchPortal() {
        searchPage.clkWebElement(waitForReload,
                searchPage.getNodeSearchPortal());
        CSLogger.info("Clicked on option Search Portal");
    }

    /**
     * This method will click on 'search' dropdown in 'settings-system
     * preferences'
     */
    private void clickOnSearch() {
        searchPage.clkWebElement(waitForReload, searchPage.getSearchNode());
        CSLogger.info("Clicked on node search");
    }

    /**
     * This method will create & configure a new share
     * 
     * @param searchLabel - user defined label for search area, passed through
     *            excel sheet
     */
    private void configurationOfShares(String searchLabel,
            String productFolder) {
        searchPage.clkWebElement(waitForReload, searchPage.getBtnShares());
        CSLogger.info("Clicked on option Shares");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarCreateNew());
        CSLogger.info("Clicked on create new button");
        verifyPropertiesPane();
        verifyGeneralSection();
        searchPage.clkWebElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        CSLogger.info("clicked on Search Area text box");
        searchPage.enterTextIntoTextbox(waitForReload,
                searchPage.getLblOpenSearchArea(), searchLabel);
        CSLogger.info("Entered text in Search Area");
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnSearchPlugin());
        CSLogger.info("search plugin drop down list visible");
        searchPage.clkWebElement(waitForReload, searchPage.getProductsPlugin());
        verifyPropertiesInNewlyCreatedShare();
        addRootFolder(productFolder);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This is test case specific frame traversing, to add all values from root
     * folder into the newly created share.
     */
    private void traverseToFrmToAddAllRootFolderValues() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmToAddAllValues()));
        CSLogger.info(
                "switched to the frame to add all the values in root folder");
    }

    /**
     * This method will select the root folder to be added into the share
     */
    private void addRootFolder(String productFolder) {
        String classAttributeForDataFilter = searchPage.getTabDataFilter()
                .getAttribute("class");
        if (classAttributeForDataFilter
                .equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload,
                    searchPage.getSecDataFilter());
            CSLogger.info("clicked on data filter section");
        }
        verifyPresenceOfElement(searchPage.getOptionSeletionOfParentNodes(),
                "'SeletionOfParentNodes'");
        searchPage.clkWebElement(waitForReload, searchPage.addElement());
        CSUtility.tempMethodForThreadSleep(3000);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogLeftSectionOpenSearch(
                        waitForReload, browserDriver);
        WebElement createdProductsFolder = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + productFolder + "')]"));
        Actions action = new Actions(browserDriver);
        action.doubleClick(createdProductsFolder).perform();
        CSLogger.info("double clicked on root folder");
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogCenterSectionOpenSearch(
                        waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarAddAllValues());
        CSLogger.info("Clicked on add all values button");
        CSUtility.tempMethodForThreadSleep(3000);
        traverseToFrmToAddAllRootFolderValues();
        searchPage.clkWebElement(waitForReload, searchPage.getBtnYes());
        CSLogger.info("clicked on option yes");
        CSUtility.tempMethodForThreadSleep(3000);
        popupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method will verify properties in newly created share.
     */
    private void verifyPropertiesInNewlyCreatedShare() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        verifyPresenceOfElement(searchPage.getSecSearchSettings(),
                "'Search Settings' section");
        verifyPresenceOfElement(searchPage.getSecDataFilter(),
                "'Data Filter' section");
        verifyPresenceOfElement(searchPage.getSecListAttributes(),
                "'List Attributes' section");
        verifyPresenceOfElement(searchPage.getSecPreviewAttributes(),
                "'Preview Attributes' section");
        verifyPresenceOfElement(searchPage.getSecActions(),
                "'Actions' section");
        CSLogger.info("Verified properties in newly created share");
    }

    /**
     * This method will verify the 'General' section of share
     */
    private void verifyGeneralSection() {
        String classAttribute = searchPage.getTabGeneral()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload, searchPage.getSecGeneral());
            CSLogger.info("clicked on General section");
        }
        verifyPresenceOfElement(searchPage.getTxtSearchArea(),
                "'Search Area' section");
        verifyPresenceOfElement(searchPage.getTxtSearchPlugin(),
                "'Search plugin' section");
        verifyPresenceOfElement(searchPage.getCalShareFrom(),
                "'Share from' calendar");
        verifyPresenceOfElement(searchPage.getCalShareUntil(),
                "'Share until' calendar");
        verifyPresenceOfElement(searchPage.getDrpDwnAllowAccessBy(),
                "'Allow Access By' dropdown list");
        verifyPresenceOfElement(searchPage.getSharedByNode(),
                "'Shared by' node");
        verifyPresenceOfElement(searchPage.getOwnerNode(), "'Owner' node");
        verifyPresenceOfElement(searchPage.getRunAsUserNode(),
                "'Run as user' node");
        verifyPresenceOfElement(searchPage.getChkboxTextDeactivated(),
                "'Deactivated' toggle");
        verifyPresenceOfElement(searchPage.getChkboxTextHideInOpenSearch(),
                "'Hide in open search' toggle");
        verifyPresenceOfElement(searchPage.getSectionOpenKey(),
                "'Open key' section");
        CSLogger.info("General section verified.");
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * The below method will verify 'Properties' pane
     */
    private void verifyPropertiesPane() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        verifyPresenceOfElement(searchPage.getTabProperties(),
                "'Properties' pane");
        verifyPresenceOfElement(searchPage.getBtnActivityLog(),
                "'Activity log' pane");
        verifyPresenceOfElement(searchPage.getSecGeneral(),
                "'General' section");
        verifyPresenceOfElement(searchPage.getSecSearchSettings(),
                "'Search settings' section");
        CSLogger.info("Properties pane verified.");
    }

    /**
     * This generic method will verify whether any particular element is
     * displayed or not
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
     * This method will create data for test i.e. PIM products folder & a
     * product inside that folder
     * 
     * @param productFolder - String instance - label of products folder
     * @param productLabel - String instance - label of product
     */
    private void createTestData(String productFolder, String productLabel) {
        createProductFolder(productFolder);
        addProductsInsideFolder(productFolder, productLabel);
    }

    /**
     * This method will create products inside product folder
     * 
     * @param productFolder - String instance - product folder name
     * @param productLabel - String instance - product name
     */
    private void addProductsInsideFolder(String productFolder,
            String productLabel) {
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode());
        CSLogger.info("Clicked on products node");
        CSUtility.rightClickTreeNode(waitForReload,
                getProductsFolder(productFolder), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuNewChild(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        enterTextInSearchBox(popupDiv.getTxtCsGuiModalDialogFormName(),
                productLabel);
        CSLogger.info("Entered product name in text box");
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSLogger.info("product is successfully created");
    }

    /**
     * This method will create products folder
     * 
     * @param productFolder - String instance - product folder name
     */
    private void createProductFolder(String productFolder) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopUp.enterValueInDialogue(waitForReload, productFolder);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Products folder is successfully created");
    }

    /**
     * This method will return the products folder
     * 
     * @param productFolderName - String instance - label of product folder
     * @return productsFolder
     */
    private WebElement getProductsFolder(String productFolderName) {
        WebElement productsFolder = browserDriver.findElement(By
                .xpath("//span[contains(text(),'" + productFolderName + "')]"));
        return productsFolder;
    }

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        softAssertion = new SoftAssert();
        popupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNode = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
    }

    /**
     * This data provider contains data for shares configuration
     * 
     * @return array of data for shares configuration
     */
    @DataProvider(name = "configurationOfShares")
    public Object[][] getDataForConfigurationOfShares() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                sharesConfigSheet);
    }
}
