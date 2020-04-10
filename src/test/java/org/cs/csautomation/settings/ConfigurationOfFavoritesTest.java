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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test method to configure favorites in search portal
 * 
 * @author CSAutomation Team
 *
 */
public class ConfigurationOfFavoritesTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private FrameLocators             locator;
    private CSPortalHeader            csPortalHeader;
    private CSGuiToolbarHorizontal    guiToolbarHorizontal;
    private String                    favoriteConfigSheet = "ConfigurationOfFavorites";
    private SearchPage                searchPage;
    private SoftAssert                softAssertion;
    private CSPopupDivPim             popupDiv;
    private CSPortalWidget            portalWidget;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private IProductPopup             productPopUp;

    /**
     * This is the test method which drives the use case, ie. create share &
     * configure the search plugin 'favorites'
     * 
     * @param searchAreaLabel - String object - user defined label for search
     *            area, passed through excel sheet
     * @param favoriteElement - String instance - favorite label for product
     * @param productFolder - String instance - label of product folder
     * @param productLabel - String instance - label of product
     */
    @Test(dataProvider = "configurationOfFavorites", priority = 1)
    public void testConfigurationOfFavorites(String searchAreaLabel,
            String favoriteElement, String productFolder, String productLabel) {
        try {
            createTestData(favoriteElement, productFolder, productLabel);
            openSearchPortal();
            configurationOfFavorites(searchAreaLabel, favoriteElement);
            softAssertion.assertAll();
            CSUtility.switchToDefaultFrame(browserDriver);
            CSLogger.info("‘Configuration of Favorites’ test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testConfigurationOfFavorites",
                    e);
            Assert.fail("Automation error : testConfigurationOfFavorites", e);
        }
    }

    /**
     * This is the test method which drives the usecase, ie. verify search
     * plugin in list view of favorites
     * 
     * @param searchAreaLabel - String object - user defined label for search
     *            area, passed through excel sheet *
     * @param favoriteElement - String instance - favorite label for product
     */
    @Test(dataProvider = "configurationOfFavorites", priority = 2)
    public void testVerifySearchPluginInListViewOfFavorites(
            String searchAreaLabel, String favoriteElement,
            String productFolder, String productLabel) {
        try {
            browserDriver.navigate().refresh();
            CSUtility.tempMethodForThreadSleep(2000);
            openSearchPortal();
            searchPage.clkWebElement(waitForReload, searchPage.getBtnShares());
            CSLogger.info("Clicked on option Shares");
            CSUtility.tempMethodForThreadSleep(2000);
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, locator);
            WebElement searchPlugin = browserDriver.findElement(By
                    .xpath("//td[contains(text(),'" + searchAreaLabel + "')]"));
            verifyPresenceOfElement(searchPlugin, searchAreaLabel);
            softAssertion.assertAll();
            CSLogger.info(
                    "'Verify Search Plugin In List View Of Favorites' test completed");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerifySearchPluginInListViewOfFavorites",
                    e);
            Assert.fail(
                    "Automation error : testVerifySearchPluginInListViewOfFavorites",
                    e);
        }
    }

    /**
     * This method will create data for test i.e. PIM products folder & a
     * product inside that folder
     * 
     * @param productFolder - String instance - label of products folder
     * @param productLabel - String instance - label of product
     * @param favoriteElement- String instance - favorite label for product
     * 
     */
    private void createTestData(String favoriteElement, String productFolder,
            String productLabel) {
        createProductFolder(productFolder);
        addProductsInsideFolder(favoriteElement, productFolder, productLabel);
        addProductsToFavorite(favoriteElement, productFolder, productLabel);
    }

    /**
     * This method will tag product as a favorite
     * 
     * @param favoriteElement - String instance - label of products folder
     * @param productFolder - String instance - label of products folder
     */
    private void addProductsToFavorite(String favoriteElement,
            String productFolder, String productLabel) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode());
        CSLogger.info("Clicked on products node");
        searchPage.clkWebElement(waitForReload,
                getProductsFolder(productFolder));
        CSLogger.info("clicked on products folder");
        CSUtility.tempMethodForThreadSleep(4000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        String[] favorite = favoriteElement.split(",");
        for (String favoriteLabel : favorite) {
            searchPage.clkWebElement(waitForReload,
                    searchPage.getChkboxSelectAllProductsFromFolder());
            CSLogger.info("clicked on checkbox to add all the products from "
                    + "product folder into favorites");
            CSUtility.tempMethodForThreadSleep(2000);
            searchPage.clkWebElement(waitForReload,
                    guiToolbarHorizontal.getBtnShowMoreOption());
            switchFramesAndClickWebElement(locator.getFrmCsPopupDivFrame(),
                    searchPage.getDrpDwnFavoritesInPimShowMore());
            switchFramesAndClickWebElement(locator.getFrmCsPopupDivSubFrame(),
                    searchPage.getDrpDwnAddFavorite());
            switchFramesAndClickWebElement(locator.getFrmCreateNewClass(),
                    searchPage.getLblFavoriteProduct());
            enterTextInSearchBox(searchPage.getLblFavoriteProduct(),
                    favoriteLabel);
            switchFramesAndClickWebElement(locator.getFrmCSPortalWindow(),
                    searchPage.getBtnOkOpenSearch());
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("successfully added a product as favorite");
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
        }
    }

    /**
     * This method will switch frames & click on web element
     * 
     * @param frame - WebElement instance
     * @param elementToBeClicked - WebElement instance
     */
    private void switchFramesAndClickWebElement(WebElement frame,
            WebElement elementToBeClicked) {
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
        searchPage.clkWebElement(waitForReload, elementToBeClicked);
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
     * This method will create products inside product folder
     * 
     * @param productFolder - String instance - product folder name
     * @param productLabel - String instance - product name
     */
    private void addProductsInsideFolder(String favoriteElement,
            String productFolder, String productLabel) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode());
        CSLogger.info("Clicked on products node");
        String[] label = productLabel.split(",");
        for (String productName : label) {
            createProduct(productFolder, productName);
        }
    }

    /**
     * This method will create multiple products inside product folder
     * 
     * @param productFolder - String instance - label of product folder
     * @param productLabel - String instance - label of product
     */
    private void createProduct(String productFolder, String productLabel) {
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
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
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
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
        CSLogger.info("Clicked on option 'Search Portal'");
    }

    /**
     * This method will create & configure a new favorite
     * 
     * @param searchLabel - user defined label for search area, passed through
     *            excel sheet
     * @param favoriteElement - String instance - label of favorite element
     */
    private void configurationOfFavorites(String searchLabel,
            String favoriteElement) {
        searchPage.clkWebElement(waitForReload, searchPage.getBtnShares());
        CSLogger.info("Clicked on option 'Shares'");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarCreateNew());
        guiToolbarHorizontal.getBtnCSGuiToolbarCreateNew().click();
        CSLogger.info("Clicked on 'create new' button");
        CSUtility.tempMethodForThreadSleep(2000);
        verifyPropertiesPane();
        verifyGeneralSection();
        searchPage.clkWebElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        CSLogger.info("clicked on 'Search Area' text box");
        searchPage.enterTextIntoTextbox(waitForReload,
                searchPage.getLblOpenSearchArea(), searchLabel);
        CSLogger.info("Entered text in 'Search Area'");
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnSearchPlugin());
        searchPage.clkWebElement(waitForReload,
                searchPage.getSearchPluginFavorites());
        CSLogger.info("Selected search plugin 'Favorites'");
        verifyPropertiesInNewlyCreatedFavorite();
        addFavorites(favoriteElement);
    }

    /**
     * This method will add elements as 'favorite' in search plugin
     * 
     * @param favoriteElement - String instance - label of favorite element
     */
    private void addFavorites(String favoriteElement) {
        String classAttributeForDataFilter = searchPage
                .getTabDataFilterInFavorites().getAttribute("class");
        if (classAttributeForDataFilter
                .equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload,
                    searchPage.getSecDataFilterFavorites());
            CSLogger.info("clicked on 'data filter' section");
        }
        searchPage.clkWebElement(waitForReload, searchPage.addElement());
        CSLogger.info("clicked on plus to add favorites");
        traverseToFrmDataSelectionDialog();
        CSUtility.tempMethodForThreadSleep(2000);
        String[] favorite = favoriteElement.split(",");
        for (String favoriteLabel : favorite) {
            WebElement favo = browserDriver.findElement(
                    By.xpath("//td[contains(text(),'" + favoriteLabel + "')]"));
            CSUtility.tempMethodForThreadSleep(2000);
            searchPage.clkWebElement(waitForReload, favo);
        }
        CSUtility.tempMethodForThreadSleep(2000);
        popupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        CSUtility.tempMethodForThreadSleep(2000);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method will traverse to the frame of 'Data selection dialog' for
     * favorites
     */
    private void traverseToFrmDataSelectionDialog() {
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFavoritesDataSelectionDailog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsCenter()));
    }

    /**
     * This method will verify properties in newly created share.
     */
    private void verifyPropertiesInNewlyCreatedFavorite() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        verifyPresenceOfElement(searchPage.getTabGeneralInFavorites(),
                "Tab 'General' for search plugin 'favorites'");
        verifyPresenceOfElement(searchPage.getSecSearchSettings(),
                "'Search Settings' section");
        verifyPresenceOfElement(searchPage.getSecDataFilter(),
                "'Data Filter' section");
        CSLogger.info(
                "Verified properties in newly created search plugin 'Favorite'");
    }

    /**
     * This method will verify the 'General' section of share
     */
    private void verifyGeneralSection() {
        String classAttribute = searchPage.getTabGeneral()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload, searchPage.getSecGeneral());
            CSLogger.info("clicked on 'General' section");
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
        verifyPresenceOfElement(searchPage.getSectionOpenKey(),
                "'Open key' section");
        CSLogger.info("'General' section verified.");
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * The below method will verify 'Properties' pane
     */
    private void verifyPropertiesPane() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        CSUtility.tempMethodForThreadSleep(2000);
        verifyPresenceOfElement(searchPage.getTabProperties(),
                "'Properties' pane");
        verifyPresenceOfElement(searchPage.getBtnActivityLog(),
                "'Activity log' pane");
        verifyPresenceOfElement(searchPage.getSecGeneral(),
                "'General' section");
        verifyPresenceOfElement(searchPage.getSecSearchSettings(),
                "'Search settings' section");
        CSLogger.info("'Properties' pane verified.");
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
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 180);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        softAssertion = new SoftAssert();
        popupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
        pimStudioProductsNode = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }

    /**
     * This data provider contains label for search area & the element which is
     * to be configured as a favorite
     * 
     * @return favoriteConfigSheet - array of data for configuration of shares
     */
    @DataProvider(name = "configurationOfFavorites")
    public Object[][] getDataForConfigurationOfFavorites() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                favoriteConfigSheet);
    }
}
