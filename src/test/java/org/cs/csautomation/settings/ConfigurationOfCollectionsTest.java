/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.ArrayList;
import java.util.List;
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

public class ConfigurationOfCollectionsTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private FrameLocators             locator;
    private CSPortalHeader            csPortalHeader;
    private CSGuiToolbarHorizontal    guiToolbarHorizontal;
    private String                    collectionConfigSheet = "ConfigurationOfCollections";
    private SearchPage                searchPage;
    private SoftAssert                softAssertion;
    private CSPortalWidget            portalWidget;
    private IProductPopup             productPopUp;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPopupDivPim             popupDiv;

    /**
     * This is the test method which drives the usecase, ie. create & configure
     * collection from system references.
     * 
     * @param searchAreaLabel String object - user defined label for search
     *            area, passed through excel sheet
     * @param productFolder - String instance - label of products folder
     * @param productLabel - String instance - label of product
     */
    @Test(dataProvider = "configurationOfCollections", priority = 1)
    public void testConfigurationOfCollections(String searchAreaLabel,
            String productFolder, String productLabel) {
        try {
            createTestData(productFolder, productLabel);
            openSearchPortal();
            configurationOfCollections(searchAreaLabel, productFolder);
            verifyTabs();
            addAttributesToCollection(searchAreaLabel);
            softAssertion.assertAll();
            CSLogger.info("Configuration of collections test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testConfigurationOfCollections",
                    e);
            Assert.fail("Automation error : testConfigurationOfCollections", e);
        }
    }

    /**
     * This is the test method which drives the usecase, ie. verify the newly
     * created search area & search plugin
     * 
     * @param searchAreaLabel String object - user defined label for search
     *            area, passed through excel sheet
     */
    @Test(dataProvider = "configurationOfCollections", priority = 2)
    public void testVerifySearchPluginInListViewOfCollections(
            String searchAreaLabel, String productFolder, String productLabel) {
        try {
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, locator);
            WebElement searchPlugin = browserDriver.findElement(By
                    .xpath("//td[contains(text(),'" + searchAreaLabel + "')]"));
            verifyPresenceOfElement(searchPlugin, searchAreaLabel);
            softAssertion.assertAll();
            CSLogger.info(
                    "Verify search plugin in list view of collections test completed");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerifySearchPluginInListViewOfCollections",
                    e);
            Assert.fail(
                    "Automation error : testVerifySearchPluginInListViewOfCollections",
                    e);
        }
    }

    /**
     * This method will add attributes to collection
     */
    private void addAttributesToCollection(String searchAreaLabel) {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        searchPage.clkWebElement(waitForReload,
                searchPage.getTabProductConfiguration());
        searchPage.clkWebElement(waitForReload,
                searchPage.getSecListAttributes());
        searchPage.clkWebElement(waitForReload,
                searchPage.getImgToAddAttrToCollection());
        CSUtility.tempMethodForThreadSleep(2000);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDailogLeftSectionInCollectionsAttributes(
                        waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                searchPage.getTxtAttributesFolder());
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogCenterSectionInCollectionsAttributes(
                        waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarAddAllValues());
        CSLogger.info("Clicked on add all values button");
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmToAddAllValues()));
        searchPage.clkWebElement(waitForReload, searchPage.getBtnYes());
        CSLogger.info("clicked on option yes");
        CSUtility.tempMethodForThreadSleep(2000);
        popupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        CSUtility.tempMethodForThreadSleep(2000);
        searchPage.clkWebElement(waitForReload,
                searchPage.getBtnSaveOpenSearch());
        CSLogger.info("successfully added attribute into collection");
        CSUtility.tempMethodForThreadSleep(2000);
        accessCollectionUrl(searchAreaLabel);
    }

    /**
     * This method will access collections url after adding attribute to it
     */
    private void accessCollectionUrl(String searchAreaLabel) {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        searchPage.clkWebElement(waitForReload, searchPage.getTabProperties());
        CSUtility.tempMethodForThreadSleep(2000);
        String classAttribute = searchPage.getTabGeneralInCollections()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload, searchPage.getSecGeneral());
            CSLogger.info("clicked on General section");
        }
        CSUtility.waitForElementToBeClickable(waitForReload,
                searchPage.getUrl());
        searchPage.clkWebElement(waitForReload, searchPage.getUrl());
        CSUtility.tempMethodForThreadSleep(2000);
        ArrayList<String> tabs = new ArrayList<String>(
                browserDriver.getWindowHandles());
        browserDriver.switchTo().window(tabs.get(1));
        CSUtility.tempMethodForThreadSleep(2000);
        List<WebElement> collectionElements = browserDriver
                .findElements(By.xpath("//div[@class='CSListViewInner"
                        + "Container']/div[1]/div[2]/ul/li"));
        String searchResultText = searchPage.getResultsMetaText().getText();
        if (searchResultText.equals("0 Results in " + searchAreaLabel)) {
            boolean searchResults = checkWhetherConfiguredAttrDataIsDisplayed(
                    collectionElements);
            softAssertion.assertFalse(searchResults,
                    "Error. Configured attribute data is being displayed "
                            + "even though results text shows '0 Results'");
        } else {
            CSLogger.info("Configured attribute data is being displayed in "
                    + "list view & detail view.");
            verifyViewEditDownloadOption(collectionElements);
        }
        browserDriver.close();
        browserDriver.switchTo().window(tabs.get(0));
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method will verify view edit and download option in detailed view
     *
     * @param collectionElements - list of collections
     */
    private void
            verifyViewEditDownloadOption(List<WebElement> collectionElements) {
        WebElement collection = collectionElements.get(0);
        searchPage.clkWebElement(waitForReload, collection);
        verifyPresenceOfElement(searchPage.getLinkViewCollection(),
                "link view collection");
        verifyPresenceOfElement(searchPage.getLinkEditCollection(),
                "link edit collection");
        verifyPresenceOfElement(searchPage.getLinkDownloadCollection(),
                "link download collection");
    }

    /**
     * This method will check whether search results are displayed or not, when
     * result text shows '0 Results'
     * 
     * @param collectionElements - list of WebElements - searched results
     * @return boolean - true if search results are displayed
     * @return boolean - false if search results are not displayed
     * 
     */
    private boolean checkWhetherConfiguredAttrDataIsDisplayed(
            List<WebElement> collectionElements) {
        WebElement searchedElement = collectionElements.get(0);
        String attrClass = searchedElement.getAttribute("class");
        if (attrClass.contains("CSListViewItem dummy")) {
            softAssertion.assertFalse(
                    attrClass.contains("CSListViewItem dummy"),
                    "Error. Configured attribute data is not being displayed.");
            CSLogger.error(
                    "Error. Configured attribute data is not being displayed.");
            return false;
        } else {
            CSLogger.info("Configured attribute data is being displayed in "
                    + "list view & detail view.");
            return true;
        }
    }

    /**
     * This method will verify product configuration, channel configuration &
     * file configuration tabs
     */
    private void verifyTabs() {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        verifyPresenceOfElement(searchPage.getTabProductConfiguration(),
                "product configuration tab");
        verifyPresenceOfElement(searchPage.getTabChannelConfiguration(),
                "channel configuration tab");
        verifyPresenceOfElement(searchPage.getTabFileConfiguration(),
                "file configuration tab");
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
     * This method will configure collections
     * 
     * @param searchLabel - String instance - Search label
     * @param productFolder - String instance - product folder name
     */
    private void configurationOfCollections(String searchLabel,
            String productFolder) {
        searchPage.clkWebElement(waitForReload, searchPage.getBtnCollections());
        CSLogger.info("Clicked on option Collections");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarCreateNew());
        CSLogger.info("Clicked on create new button");
        CSUtility.tempMethodForThreadSleep(2000);
        verifyPropertiesPane();
        verifyGeneralSection();
        searchPage.clkWebElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        CSLogger.info("clicked on Search Area text box");
        searchPage.enterTextIntoTextbox(waitForReload,
                searchPage.getLblOpenSearchArea(), searchLabel);
        CSLogger.info("Entered text in Search Area");
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
//        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
//                browserDriver, locator);

        String classAttribute = searchPage.getTabGeneralInCollections()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload, searchPage.getSecGeneral());
            CSLogger.info("clicked on General section");
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getUrlRow());
        CSLogger.info("fetched URL row");
        verifyGeneralSectionUrl();
        verifyPresenceOfElement(searchPage.getSelectedItems(),
                "'Selected Items' row");
        verifyPresenceOfElement(searchPage.getSectionActions(),
                "'Actions' Section");
        verifyPresenceOfElement(searchPage.getSectionEmail(),
                "'E-mail' section");
        verifyPresenceOfElement(searchPage.getTabProductConfiguration(),
                "'Product Configuration' tab");
        verifyPresenceOfElement(searchPage.getTabChannelConfiguration(),
                "'Channel configuration' tab");
        verifyPresenceOfElement(searchPage.getTabFileConfiguration(),
                "'File configuration' tab");
        verifyPresenceOfElement(searchPage.getTabActivityLog(),
                "'Activity log' tab");
        addElementsToSelectedItemsField(productFolder);
    }

    /**
     * This method will add data to collection
     * 
     * @param productFolder - String instance - label of product Folder
     */
    private void addElementsToSelectedItemsField(String productFolder) {
        searchPage.clkWebElement(waitForReload, searchPage.addElement());
        CSUtility.tempMethodForThreadSleep(2000);
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
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmToAddAllValues()));
        searchPage.clkWebElement(waitForReload, searchPage.getBtnYes());
        CSLogger.info("clicked on option yes");
        CSUtility.tempMethodForThreadSleep(2000);
        popupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("successfully added data into collection");
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method will verify general section of collection
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
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method will verify properties pane for collection
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
     * This method will verify whether an element is present or not
     * 
     * @param wait - WebDriverWait instance
     * @param element - WebElement instance
     * @param label - String instance - label of element
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
     * Verifies the general section URL of shares.
     */
    private void verifyGeneralSectionUrl() {
        String currentUrl = browserDriver.getCurrentUrl();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getUrl());
        String actualGeneralSectionUrl = searchPage.getUrl()
                .getAttribute("href");
        String expectedGeneralSectionUrl = currentUrl + "share/"
                + searchPage.getLblOpenKeyValue().getText();
        if (actualGeneralSectionUrl.equals(expectedGeneralSectionUrl)) {
            CSLogger.info("General section URL verified successfully");
        } else {
            CSLogger.error("Invalid general section URL");
            softAssertion.fail("Invalid general section URL");
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
        portalWidget = new CSPortalWidget(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNode = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        popupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }

    /**
     * This data provider contains data for configuration of collections
     * 
     * @return array of collection configuration data
     */
    @DataProvider(name = "configurationOfCollections")
    public Object[][] getDataForConfigurationOfCollections() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                collectionConfigSheet);
    }
}
