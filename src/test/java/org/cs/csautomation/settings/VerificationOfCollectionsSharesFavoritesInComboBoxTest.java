/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test method to verify Collections Shares Favorites in
 * combo box
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfCollectionsSharesFavoritesInComboBoxTest
        extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeader;
    private SearchPage             searchPage;
    private SoftAssert             softAssertion;
    private CSPortalWidget         portalWidget;
    private FrameLocators          locator;
    private String                 collectionsSharesFavoritesSheet = "VerifyCollectionsSharesFavorite";
    private CSGuiToolbarHorizontal guiToolbarHorizontal;
    private CSPopupDivPim          popupDiv;

    /**
     * This is the test method which drives the usecase ie. Verification of
     * Collections Shares Favorites In Combo Box
     * 
     * @param collectionToHide - String instance - label of collection which is
     *            to be created to check hide functionality
     * @param collectionToDeactivate - String instance - label of collection
     *            which is to be created to check deactivate functionality
     * @param shareLabel - String instance - label of share to be created as
     *            test data
     * @param collectionLabel - String instance - label of collection to be
     *            created as test data
     * @param favoriteLabel - String instance - label of favorite to be created
     *            as test data
     * @param dataSetSize - integer value - number of data sets to be created
     *            for test data. Includes number of collections, shares &
     *            favorites to be created
     */
    @Test(dataProvider = "verificationOfCollectionsSharesFavoritesInComboBox")
    public void testVerificationOfCollectionsSharesFavoritesInComboBox(
            String collectionToHide, String collectionToDeactivate,
            String shareLabel, String collectionLabel, String favoriteLabel,
            String dataSize, String favoriteElement) {
        try {
            Integer i = Integer.valueOf(dataSize);
            int dataSetSize = i.intValue();
            for (int dataSets = 0; dataSets < dataSetSize; dataSets++) {
                createTestData(shareLabel, collectionLabel, favoriteLabel);
            }
            createFavorites(favoriteLabel, favoriteElement);
            createCollections(collectionToHide);
            createCollections(collectionToDeactivate);
            openSearchComboBox();
            verifyElementsInComboBoxDropDownList(
                    "//div[contains(@data-json,'{\"id\":\"CSShareOpenSearchArea')]",
                    searchPage.getComboBoxOptionCollections(),
                    searchPage.getComboBoxOptionMoreForCollections(),
                    "Collections");
            verifyElementsInComboBoxDropDownList(
                    "//div[contains(@data-json,'{\"id\":\"Bookmark')]",
                    searchPage.getComboBoxOptionFavorites(),
                    searchPage.getComboBoxOptionMoreForFavorites(),
                    "Favorites");
            verifySharesInComboBoxDropDownList(
                    "//div[contains(@data-json,'{\"id\":\"ProductOpenSearchArea')]",
                    "//div[contains(@data-json,'{\"id\":\"FileOpenSearchArea')]",
                    searchPage.getComboBoxOptionShares(),
                    searchPage.getComboBoxOptionMoreForShares(), "Shares");
            verifyRemainingElementsByClickingOnMore(
                    "//div[contains(@data-json,'{\"id\":\"CSShareOpenSearchArea')]",
                    searchPage.getComboBoxOptionMoreForCollections(),
                    "Collections");
            verifyRemainingElementsByClickingOnMore(
                    "//div[contains(@data-json,'{\"id\":\"Bookmark')]",
                    searchPage.getComboBoxOptionMoreForFavorites(),
                    "Favorites");
            verifyRemainingSharesByClickingOnMore(
                    "//div[contains(@data-json,'{\"id\":\"ProductOpenSearchArea')]",
                    "//div[contains(@data-json,'{\"id\":\"FileOpenSearchArea')]",
                    searchPage.getComboBoxOptionMoreForShares(), "Shares");
            hideCollectionAndVerifyComboBox(collectionToHide);
            deactivateCollectionAndVerifyComboBox(collectionToDeactivate);
            verifyListForHiddenAndDeactivatedCollections(collectionToHide,
                    collectionToDeactivate);
            softAssertion.assertAll();
            CSLogger.info(
                    "Verification of collections shares favorites in combo "
                            + "box test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerificationOfCollections"
                    + "SharesFavoritesInComboBox", e);
            Assert.fail("Automation error : testVerificationOfCollections"
                    + "SharesFavoritesInComboBox", e);
        }
    }

    /**
     * This method will create test data
     * 
     * @param shareLabel- String instance - label of share to be created as test
     *            data
     * @param collectionLabel- String instance - label of collection to be
     *            created as test data
     * @param favoriteLabel- String instance - label of favorite to be created
     *            as test data
     */
    private void createTestData(String shareLabel, String collectionLabel,
            String favoriteLabel) {
        createShares(shareLabel);
        createCollections(collectionLabel);
    }

    /**
     * This method will create shares, to be used as test data
     * 
     * @param favoriteLabel- String instance - label of collection to be created
     */
    private void createFavorites(String favoriteLabel, String favoriteElement) {
        openSearchPortal();
        searchPage.clkWebElement(waitForReload, searchPage.getBtnShares());
        CSLogger.info("Clicked on option Shares");
        CSUtility.tempMethodForThreadSleep(3000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarCreateNew());
        CSLogger.info("Clicked on create new button");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        String classAttribute = searchPage.getTabGeneral()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload, searchPage.getSecGeneral());
            CSLogger.info("clicked on General section");
        }
        searchPage.clkWebElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        CSLogger.info("clicked on Search Area text box");
        searchPage.enterTextIntoTextbox(waitForReload,
                searchPage.getLblOpenSearchArea(), favoriteLabel);
        CSLogger.info("Entered text in Search Area");
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnSearchPlugin());
        searchPage.clkWebElement(waitForReload,
                searchPage.getSearchPluginFavorites());
        CSLogger.info("Favorite is successfully created");
        addFavorites(favoriteElement);
    }

    /**
     * This method will add elements as 'favorite' in search plugin
     * 
     * @param favoriteElement - String instance - label of favorite element
     */
    private void addFavorites(String favoriteElement) {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        String classAttributeForDataFilter = searchPage
                .getTabDataFilterInFavorites().getAttribute("class");
        if (classAttributeForDataFilter
                .equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload,
                    searchPage.getTabDataFilterInFavorites());
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
     * This method will create shares, to be used as test data
     * 
     * @param collectionLabel- String instance - label of collection to be
     *            created
     */
    private void createCollections(String collectionLabel) {
        openSearchPortal();
        searchPage.clkWebElement(waitForReload, searchPage.getBtnCollections());
        CSLogger.info("Clicked on option Collections");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarCreateNew());
        CSLogger.info("Clicked on create new button");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        String classAttribute = searchPage.getTabGeneral()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload, searchPage.getSecGeneral());
            CSLogger.info("clicked on General section");
        }
        searchPage.clkWebElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        CSLogger.info("clicked on Search Area text box");
        searchPage.enterTextIntoTextbox(waitForReload,
                searchPage.getLblOpenSearchArea(), collectionLabel);
        CSLogger.info("Entered text in Search Area");
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Collection is successfully created");
    }

    /**
     * This method will create shares, to be used as test data
     * 
     * @param shareLabel - String instance - label of share to be created
     */
    private void createShares(String shareLabel) {
        openSearchPortal();
        searchPage.clkWebElement(waitForReload, searchPage.getBtnShares());
        CSLogger.info("Clicked on option Shares");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarCreateNew());
        CSLogger.info("Clicked on create new button");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        String classAttribute = searchPage.getTabGeneral()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload, searchPage.getSecGeneral());
            CSLogger.info("clicked on General section");
        }
        searchPage.clkWebElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        CSLogger.info("clicked on Search Area text box");
        searchPage.enterTextIntoTextbox(waitForReload,
                searchPage.getLblOpenSearchArea(), shareLabel);
        CSLogger.info("Entered text in Search Area");
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnSearchPlugin());
        CSLogger.info("search plugin drop down list visible");
        searchPage.clkWebElement(waitForReload, searchPage.getProductsPlugin());
        CSLogger.info("Share is successfully created");
    }

    /**
     * This method will deactivate the collection and verify whether the
     * deactivated collection is excluded from combo box or not
     * 
     * @param collectionToDeactivate - String instance - label of collection,
     *            which is to be deactivated
     */
    private void deactivateCollectionAndVerifyComboBox(
            String collectionToDeactivate) {
        traverseToGeneralSectionInCollectionsConfiguration(
                collectionToDeactivate);
        changePropertyInGeneralSection(searchPage.getChkboxDeactivated(),
                "Deactivated");
        verifyComboBoxForHiddenOrDeactivatedElement(collectionToDeactivate,
                "deactivating",
                "//div[contains(@data-json,'{\"id\":\"CSShareOpenSearchArea')]",
                searchPage.getComboBoxOptionCollections());
    }

    /**
     * This method will change the property in general section, in properties
     * tab, in collections configuration
     * 
     * @param property - WebElement instance - property which is to be
     *            configured
     * @param propertyLabel - String instance - label of property which is to be
     *            configured
     */
    private void changePropertyInGeneralSection(WebElement property,
            String propertyLabel) {
        CSUtility.waitForElementToBeClickable(waitForReload, property);
        String attrClassValue = property.getAttribute("class");
        if (attrClassValue.equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox")) {
            searchPage.clkWebElement(waitForReload, property);
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info(
                    "toggle '" + propertyLabel + "' was off and now turned on");
        } else {
            CSLogger.info(
                    "toggle '" + propertyLabel + "' is already turned on");
        }
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.waitForElementToBeClickable(waitForReload,
                guiToolbarHorizontal.getBtnCSGuiToolbarSave());
        browserDriver.navigate().refresh();
        browserDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        CSLogger.info("page refresh");
    }

    /**
     * This method will traverse to the general section in properties tab in
     * collections configuration
     * 
     * @param collection - String instance - label of collection
     */
    private void traverseToGeneralSectionInCollectionsConfiguration(
            String collection) {
        openSearchPortal();
        searchPage.clkWebElement(waitForReload, searchPage.getBtnCollections());
        CSLogger.info("Clicked on option Collections");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//td[contains(text(),'" + collection + "')]")));
        WebElement collectionElement = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + collection + "')]"));
        searchPage.clkWebElement(waitForReload, collectionElement);
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        searchPage.clkWebElement(waitForReload, searchPage.getTabProperties());
        String classAttribute = searchPage.getTabGeneralInCollections()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload,
                    searchPage.getTabGeneralInCollections());
            CSLogger.info("clicked on General section in collections");
        }
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method will open the drop-down menu in search combo box
     */
    private void openSearchComboBox() {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getSearchComboBox());
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        CSLogger.info("clicked on combo box drop down arrow");
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method will verify various elements in search combo box drop down
     * list
     * 
     * @param listElementsXpath - String instance - xpath of element which is to
     *            be verified in combo box
     * @param searchComboBoxElement - WebElement instance - element which is to
     *            be searched for, in combo box
     * @param elementForOptionMore - WebElement instance - element of option
     *            'more'
     * @param label - String instance - text Collections or Share or Favorites
     */
    private void verifyElementsInComboBoxDropDownList(String listElementsXpath,
            WebElement searchComboBoxElement, WebElement elementForOptionMore,
            String label) {
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);", searchComboBoxElement);
        verifyPresenceOfElement(searchComboBoxElement,
                "Combo box option '" + label + "'");
        List<WebElement> comboBoxElements = browserDriver
                .findElements(By.xpath(listElementsXpath));
        int comboBoxElementsCount = comboBoxElements.size();
        if (comboBoxElementsCount != 5) {
            softAssertion.assertFalse(comboBoxElementsCount != 5,
                    "Error. " + label + " count is " + comboBoxElementsCount
                            + ". Only 5 " + label + " should be displayed.");
            CSLogger.error(
                    "Error. " + label + " count is " + comboBoxElementsCount
                            + ". Only 5 " + label + " should be displayed.");
        } else {
            CSLogger.info("Only 5 " + label + " are displayed, as expected!");
            verifyPresenceOfElement(elementForOptionMore,
                    "'More' option after 5 " + label);
        }
    }

    /**
     * This method will verify shares in search combo box drop down list
     * 
     * @param listElementsXpath1 - String instance - xpath of products
     * @param listElementsXpath2 - String instance - xpath of files
     * @param searchComboBoxElement - WebElement instance - element which is to
     *            be searched for, in combo box
     * @param elementForOptionMore - WebElement instance - element of option
     *            'more'
     * @param label - String instance - text Share
     */
    private void verifySharesInComboBoxDropDownList(String listElementsXpath1,
            String listElementsXpath2, WebElement searchComboBoxElement,
            WebElement elementForOptionMore, String label) {
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);", searchComboBoxElement);
        verifyPresenceOfElement(searchComboBoxElement,
                "Combo box option '" + label + "'");
        List<WebElement> fileShares = browserDriver
                .findElements(By.xpath(listElementsXpath1));
        List<WebElement> productShares = browserDriver
                .findElements(By.xpath(listElementsXpath2));
        int filesCount = fileShares.size();
        int sharesCount = productShares.size();
        if (filesCount + sharesCount != 5) {
            softAssertion.assertFalse(filesCount + sharesCount != 5,
                    "Error. " + label + " count is " + filesCount + sharesCount
                            + ". Only 5 " + label + " should be displayed.");
            CSLogger.error(
                    "Error. " + label + " count is " + filesCount + sharesCount
                            + ". Only 5 " + label + " should be displayed.");
        } else {
            CSLogger.info("Only 5 " + label + " are displayed, as expected!");
            verifyPresenceOfElement(elementForOptionMore,
                    "'More' option after 5 " + label);
        }
    }

    /**
     * This method will verify collections appearing after clicking on option
     * 'more'
     * 
     * @param listElementsXpath - String instance - xpath of element which is to
     *            be verified in combo box
     * @param elementForOptionMore - WebElement instance - element of option
     *            'more'
     * @param label - String instance - text Collections or Share or Favorites
     */
    private void verifyRemainingElementsByClickingOnMore(
            String listElementsXpath, WebElement elementForOptionMore,
            String label) {
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);", elementForOptionMore);
        searchPage.clkWebElement(waitForReload, elementForOptionMore);
        CSUtility.tempMethodForThreadSleep(2000);
        List<WebElement> comboBoxElements = browserDriver
                .findElements(By.xpath(listElementsXpath));
        int comboBoxElementsCount = comboBoxElements.size();
        if (comboBoxElementsCount >= 6) {
            CSLogger.info("Remaining " + label + " elements are displayed "
                    + "after clicking on 'more' option, as expected");
            reopenComboBoxAndVerifyHidingOfAdditionalElements(listElementsXpath,
                    elementForOptionMore, label);
        } else {
            softAssertion.assertTrue(comboBoxElementsCount >= 6,
                    "Error. Remaining " + label + " elements are not displayed "
                            + "after clicking on 'more' option. " + "Only "
                            + comboBoxElementsCount + " " + label + " "
                            + "are displayed.");
            CSLogger.error(
                    "Error. Remaining " + label + " elements are not displayed "
                            + "after clicking on 'more' option. " + "Only "
                            + comboBoxElementsCount + " " + label + " "
                            + "are displayed.");

        }

    }

    /**
     * This method will verify shares appearing after clicking on option 'more'
     * 
     * @param listElementsXpath1 - String instance - xpath of products
     * @param listElementsXpath2 - String instance - xpath of files
     * @param elementForOptionMore - WebElement instance - element of option
     *            'more'
     * @param label - String instance - text Share
     */
    private void verifyRemainingSharesByClickingOnMore(
            String listElementsXpath1, String listElementsXpath2,
            WebElement elementForOptionMore, String label) {
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);", elementForOptionMore);
        searchPage.clkWebElement(waitForReload, elementForOptionMore);
        CSUtility.tempMethodForThreadSleep(2000);
        List<WebElement> productShares = browserDriver
                .findElements(By.xpath(listElementsXpath1));
        List<WebElement> fileShares = browserDriver
                .findElements(By.xpath(listElementsXpath2));
        int filesCount = fileShares.size();
        int productsCount = productShares.size();
        if (filesCount + productsCount >= 6) {
            CSLogger.info("Remaining " + label + " elements are displayed "
                    + "after clicking on 'more' option, as expected");
            reopenComboBoxAndVerifyHidingOfAdditionalShares(listElementsXpath1,
                    listElementsXpath2, elementForOptionMore, label);
        } else {
            softAssertion.assertTrue(filesCount + productsCount >= 6,
                    "Error. Remaining " + label + " elements are not displayed "
                            + "after clicking on 'more' option. " + "Only "
                            + filesCount + productsCount + " " + label + " "
                            + "are displayed.");
            CSLogger.error("Error. Remaining " + label
                    + " elements are not displayed "
                    + "after clicking on 'more' option. " + "Only " + filesCount
                    + productsCount + " " + label + " " + "are displayed.");

        }

    }

    /**
     * This method will close and then re-open the search combo box. More option
     * should be displayed after re-opening the search combo box and
     * additionally opened elements should hide again.
     * 
     * @param listElementsXpath1 - String instance - xpath of products
     * @param listElementsXpath2 - String instance - xpath of files
     * @param elementForOptionMore - WebElement instance - element of option
     *            'more'
     * @param label - String instance - text Share
     */
    private void reopenComboBoxAndVerifyHidingOfAdditionalShares(
            String listElementsXpath1, String listElementsXpath2,
            WebElement elementForOptionMore, String label) {
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        CSLogger.info("Closed search combo box dropdown");
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        CSLogger.info("re-opened the search combo box");
        List<WebElement> productShares = browserDriver
                .findElements(By.xpath(listElementsXpath1));
        List<WebElement> fileShares = browserDriver
                .findElements(By.xpath(listElementsXpath2));
        int filesCount = fileShares.size();
        int productsCount = productShares.size();
        if (filesCount + productsCount == 5) {
            softAssertion.assertTrue(filesCount + productsCount == 5);
            ((JavascriptExecutor) browserDriver).executeScript(
                    "arguments[0].scrollIntoView(true);", elementForOptionMore);
            verifyPresenceOfElement(elementForOptionMore,
                    "combo box option 'more' for " + label);
            CSLogger.info(
                    "Additionally loaded elements are getting hidden after "
                            + "re-opening the search combo box & 'more' option "
                            + "is displayed again, as expected.");
        } else {
            softAssertion.assertTrue(filesCount + productsCount == 5,
                    "Error. Additionally loaded elements are not getting hidden "
                            + "after re-opening the search combo box");
        }
    }

    /**
     * This method will close and then re-open the search combo box. More option
     * should be displayed after re-opening the search combo box and
     * additionally opened elements should hide again.
     * 
     * @param listElementsXpath - String instance - xpath of element which is to
     *            be verified in combo box
     * @param elementForOptionMore - WebElement instance - element of option
     *            'more'
     * @param label - String instance - text Collections or Share or Favorites
     */
    private void reopenComboBoxAndVerifyHidingOfAdditionalElements(
            String listElementsXpath, WebElement elementForOptionMore,
            String label) {
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        CSLogger.info("Closed search combo box dropdown");
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        CSLogger.info("re-opened the search combo box");
        List<WebElement> Elements = browserDriver
                .findElements(By.xpath(listElementsXpath));
        int ElementsCount = Elements.size();
        if (ElementsCount == 5) {
            softAssertion.assertTrue(ElementsCount == 5);
            ((JavascriptExecutor) browserDriver).executeScript(
                    "arguments[0].scrollIntoView(true);", elementForOptionMore);
            verifyPresenceOfElement(elementForOptionMore,
                    "combo box option 'more' for " + label);
            CSLogger.info(
                    "Additionally loaded elements are getting hidden after "
                            + "re-opening the search combo box & 'more' option "
                            + "is displayed again, as expected.");
        } else {
            softAssertion.assertTrue(ElementsCount == 5,
                    "Error. Additionally loaded elements are not getting hidden "
                            + "after re-opening the search combo box");
        }
    }

    /**
     * This method will hide the collection and verify whether the hidden
     * collection is excluded from combo box list or not
     * 
     * @param collectionToHide - String instance - label of collection to be
     *            hidden
     */
    private void hideCollectionAndVerifyComboBox(String collectionToHide) {
        traverseToGeneralSectionInCollectionsConfiguration(collectionToHide);
        changePropertyInGeneralSection(searchPage.getChkboxHideInOpenSearch(),
                "Hide in OpenSearch");
        verifyComboBoxForHiddenOrDeactivatedElement(collectionToHide, "hiding",
                "//div[contains(@data-json,'{\"id\":\"CSShareOpenSearchArea')]",
                searchPage.getComboBoxOptionCollections());
    }

    /**
     * This method will verify combo box for hidden or deactivated collections
     * 
     * @param collectionLabel - String instance - label of hidden or deactivated
     *            collection
     * @param actionLabel - String instance - text hiding or deactivating
     * @param listElementsXpath - String instance - xpath of element which is to
     *            be verified in combo box
     * @param searchComboBoxElement - WebElement instance - element which is to
     *            be verified in combo box
     */
    private void verifyComboBoxForHiddenOrDeactivatedElement(
            String collectionLabel, String actionLabel,
            String listElementsXpath, WebElement searchComboBoxElement) {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        CSLogger.info("traversed to frame of collections pop-up");
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        CSLogger.info("clicked on combo box drop-down arrow");
        CSUtility.tempMethodForThreadSleep(2000);
        if (searchPage.getComboBoxOptionMoreForCollections().isDisplayed()) {
            searchPage.clkWebElement(waitForReload,
                    searchPage.getComboBoxOptionMoreForCollections());
            checkForHiddenDeactivatedElementInComboBox(collectionLabel,
                    actionLabel);
        } else {
            checkForHiddenDeactivatedElementInComboBox(collectionLabel,
                    actionLabel);
        }
    }

    /**
     * This method will look for hidden or deactivated elements in combo box
     * 
     * @param collectionLabel - String instance - label of collection which is
     *            hidden or deactivated
     * @param actionLabel - String instance - text hidden or deactivated
     */
    private void checkForHiddenDeactivatedElementInComboBox(
            String collectionLabel, String actionLabel) {
        List<WebElement> collectionElements = browserDriver.findElements(
                By.xpath("//div[contains(text(),'" + collectionLabel + "')]"));
        if (collectionElements.size() == 0) {
            softAssertion.assertTrue(collectionElements.size() == 0);
            CSLogger.info("As expected, collection is not displayed in "
                    + "search combo box after " + actionLabel + " it");
        } else {
            softAssertion.assertTrue(collectionElements.size() == 0,
                    "Error! Collection is still visible in search combo box "
                            + "even after " + actionLabel + " it.");
            CSLogger.error(
                    "Error! Collection is still visible in search combo box "
                            + "even after " + actionLabel + " it.");
        }
    }

    /**
     * This method will check whether hidden and deactivated collections are
     * visible in add to collections list or not
     * 
     * @param collection - String instance - label of collection to be verified
     * @param actionLabel - String instance - text hiding or deactivating
     */
    private void verifyListForHiddenAndDeactivatedCollections(
            String collectionToHide, String collectionToDeactivate) {
        searchForData("*", searchPage.getComboBoxOptionEverywhere());
        searchPage.clkWebElement(waitForReload,
                searchPage.getChkboxSelectionMode());
        searchPage.clkWebElement(waitForReload,
                searchPage.getBtnCollectionPopup());
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnAddToCollection());
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnListForExistingCollection());
        CSUtility.tempMethodForThreadSleep(2000);
        checkCollectionInList(collectionToHide, "hiding");
        checkCollectionInList(collectionToDeactivate, "deactivating");
    }

    /**
     * This method will check for hidden or deactivated collections in 'Add To
     * Collections' list
     * 
     * @param collection - String instance - label of collection which is to be
     *            checked
     * @param actionLabel - String instance - text hiding or deactivated
     */
    private void checkCollectionInList(String collection, String actionLabel) {
        List<WebElement> listElements = browserDriver.findElements(
                By.xpath("//option[contains(text(),'" + collection + "')]"));
        if (listElements.size() == 0) {
            softAssertion.assertTrue(listElements.size() == 0);
            CSLogger.info("As expected, collection is not displayed in "
                    + "add to collection list after " + actionLabel + " it");
        } else {
            softAssertion.assertTrue(listElements.size() == 0,
                    "Error! Collection is visible in add to collection list "
                            + "even after " + actionLabel + " it.");
            CSLogger.error(
                    "Error! Collection is visible in add to collection list "
                            + "even after " + actionLabel + " it.");
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
        CSUtility.tempMethodForThreadSleep(2000);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSLogger.info("clicked on search button of search page");
        CSUtility.tempMethodForThreadSleep(2000);
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
     * This method will verify whether any particular element is displayed or
     * not
     * 
     * @param wait - WebDriverWait instance - time duration to wait before
     *            performing any action
     * @param element - WebElement instance - element whose presence is to be
     *            checked
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
     * This method initializes all the required instances for test
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 180);
        softAssertion = new SoftAssert();
        portalWidget = new CSPortalWidget(browserDriver);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        popupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }

    /**
     * This data provider contains data of collection, shares, favorites
     * 
     * @return array of collection, shares, favorites data
     */

    @DataProvider(name = "verificationOfCollectionsSharesFavoritesInComboBox")
    public Object[][]
            getDataForVerificationCollectionsSharesFavoritesInComboBox() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                collectionsSharesFavoritesSheet);
    }
}
