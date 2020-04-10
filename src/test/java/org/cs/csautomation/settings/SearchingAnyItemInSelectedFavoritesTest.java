/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test method for searching any item in selected
 * favorites
 * 
 * @author CSAutomation Team
 *
 */
public class SearchingAnyItemInSelectedFavoritesTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private CSPortalHeader csPortalHeader;
    private SearchPage     searchPage;
    private SoftAssert     softAssertion;
    private String         searchBoxTextSheet = "SearchAnyItemInFavorites";

    /**
     * This is the test method which drives the use-case of searching any item
     * in selected favorites
     * 
     * @param searchBoxText - String instance - keyword used to search for data
     *            in share
     * @param favoritesLabel - String instance - label of favorites to be
     *            selected in search combo box
     */
    @Test(dataProvider = "searchingAnyItemInSelectedFavorites")
    public void testSearchingAnyItemInSelectedFavorites(String searchBoxText,
            String favoritesLabel) {
        try {
            browserDriver.navigate().refresh();
            CSUtility.tempMethodForThreadSleep(2000);
            searchForData(searchBoxText,
                    searchPage.getComboBoxOptionMoreForFavorites(),
                    favoritesLabel);
            List<WebElement> favoritesSearchElements = browserDriver
                    .findElements(By.xpath("//div[@class='CSListViewInner"
                            + "Container']/div[1]/div[2]/ul/li"));
            String searchResultText = searchPage.getResultsMetaText().getText();
            if (searchResultText.equals("0 Results in " + favoritesLabel)) {
                boolean searchResults = checkWhetherSearchResultsAreDisplayed(
                        favoritesSearchElements);
                softAssertion.assertFalse(searchResults,
                        "Error. Search results are being displayed even though "
                                + "results text shows '0 Results'");
            } else {
                CSLogger.info(
                        "Search results are being displayed as expected.");
            }
            softAssertion.assertAll();
            browserDriver.navigate().refresh();
            browserDriver.manage().timeouts().pageLoadTimeout(10,
                    TimeUnit.SECONDS);
            CSLogger.info(
                    "Searching any item in selected favorites test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testSearchingAnyItemInSelectedFavorites",
                    e);
            Assert.fail(
                    "Automation error : testSearchingAnyItemInSelectedFavorites",
                    e);
        }
    }

    /**
     * This method will check whether search results are displayed or not, when
     * result text shows '0 Results'
     * 
     * @param favoritesSearchElements - list of WebElements - searched results
     * @return boolean - true if search results are displayed
     * @return boolean - false if search results are not displayed
     * 
     */
    private boolean checkWhetherSearchResultsAreDisplayed(
            List<WebElement> favoritesSearchElements) {
        WebElement searchedElement = favoritesSearchElements.get(0);
        String attrClass = searchedElement.getAttribute("class");
        if (attrClass.contains("CSListViewItem dummy")) {
            softAssertion.assertFalse(
                    attrClass.contains("CSListViewItem dummy"),
                    "Error! Search Results are not being displayed! "
                            + "Either search keyword is wrong OR data "
                            + "related to searched keyword does not "
                            + "exist.");
            CSLogger.error("Error! Search Results are not being displayed! "
                    + "Either search keyword is wrong OR data "
                    + "related to searched keyword does not exist.");
            return false;
        } else {
            CSLogger.info("Search results are being displayed as expected.");
            return true;
        }
    }

    /**
     * This method will search for data in specific favorite
     * 
     * @param searchBoxText - String instance - keyword using which we'll search
     *            for data
     * @param comboBoxOption - WebElement instance - WebElement for combo box
     *            option
     * @param favoritesLabel - String instance - label of favorite
     */
    private void searchForData(String searchBoxText, WebElement comboBoxOption,
            String favoritesLabel) {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        enterTextInSearchBox(searchPage.getSearchField(), searchBoxText);
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);",
                searchPage.getComboBoxOptionFavorites());
        List<WebElement> comboBoxElements = browserDriver.findElements(
                By.xpath("//div[contains(@data-json,'{\"id\":\"Bookmark')]"));
        int comboBoxElementsCount = comboBoxElements.size();
        if (comboBoxElementsCount >= 5) {
            ((JavascriptExecutor) browserDriver).executeScript(
                    "arguments[0].scrollIntoView(true);", comboBoxOption);
            searchPage.clkWebElement(waitForReload, comboBoxOption);
        }
        WebElement favorite = browserDriver.findElement(
                By.xpath("//div[contains(text(),'" + favoritesLabel + "')]"));
        ((JavascriptExecutor) browserDriver)
                .executeScript("arguments[0].scrollIntoView(true);", favorite);
        searchPage.clkWebElement(waitForReload, favorite);
        CSUtility.tempMethodForThreadSleep(3000);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSLogger.info("clicked on search button of search page");
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method will enter text in search text box
     * 
     * @param searchBoxText - String instance - keyword using which we'll search
     *            for data
     * @param element - WebElement instance - element for text box
     */
    private void enterTextInSearchBox(WebElement element,
            String searchBoxText) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.clear();
        element.sendKeys(searchBoxText);
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        softAssertion = new SoftAssert();
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
    }

    /**
     * This data provider contains text to be entered into search box and label
     * of favorite in which the element is to be searched for
     * 
     * @return array of search box text & label of favorite element
     */
    @DataProvider(name = "searchingAnyItemInSelectedFavorites")
    public Object[][] getDataForSearchBoxSuggestions() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                searchBoxTextSheet);
    }
}
