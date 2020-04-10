/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.List;
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
 * collection
 * 
 * @author CSAutomation Team
 *
 */
public class SearchingAnyItemInSelectedCollectionTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private CSPortalHeader csPortalHeader;
    private SearchPage     searchPage;
    private SoftAssert     softAssertion;
    private String         searchBoxTextSheet = "SearchAnyItemInCollection";

    /**
     * This is the test method which drives the use-case of searching any item
     * in selected collection
     * 
     * @param searchBoxText - String instance - keyword used to search for data
     *            in share
     * @param collectionLabel - String instance - label of collection to be
     *            selected in search combo box
     */
    @Test(dataProvider = "searchingAnyItemInSelectedCollection")
    public void testSearchingAnyItemInSelectedCollection(String searchBoxText,
            String collectionLabel) {
        try {
            browserDriver.navigate().refresh();
            CSUtility.tempMethodForThreadSleep(2000);
            searchForData(searchBoxText,
                    searchPage.getComboBoxOptionMoreForCollections(),
                    collectionLabel);
            List<WebElement> collectionSearchElements = browserDriver
                    .findElements(By.xpath("//div[@class='CSListViewInner"
                            + "Container']/div[1]/div[2]/ul/li"));
            int countCollectionSearchElements = collectionSearchElements.size();
            if (countCollectionSearchElements == 0) {
                softAssertion.assertFalse(countCollectionSearchElements == 0,
                        "Error! Search Results are not being displayed! Either "
                                + "search keyword is wrong OR data related to "
                                + "searched keyword does not exist.");
                CSLogger.error("Error! Search Results are not being displayed! "
                        + "Either search keyword is wrong OR data related to "
                        + "searched keyword does not exist.");
            } else if (countCollectionSearchElements == 1) {
                WebElement searchedElement = collectionSearchElements.get(0);
                String attrClass = searchedElement.getAttribute("class");
                if (attrClass.contains("CSListViewItem dummy")) {
                    softAssertion.assertFalse(
                            countCollectionSearchElements == 1,
                            "Error! Search Results are not being displayed! "
                                    + "Either search keyword is wrong OR data "
                                    + "related to searched keyword does not "
                                    + "exist.");
                    CSLogger.error(
                            "Error! Search Results are not being displayed! "
                                    + "Either search keyword is wrong OR data "
                                    + "related to searched keyword does not "
                                    + "exist.");
                } else {
                    softAssertion
                            .assertTrue(countCollectionSearchElements == 1);
                    CSLogger.info(
                            "Search results are being displayed as expected.");
                }
            } else {
                softAssertion.assertTrue(countCollectionSearchElements > 1);
                CSLogger.info(
                        "Search results are being displayed as expected.");
            }
            softAssertion.assertAll();
            CSLogger.info(
                    "Searching any item in selected collection test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testSearchingAnyItemInSelectedCollection",
                    e);
            Assert.fail(
                    "Automation error : testSearchingAnyItemInSelectedCollection",
                    e);
        }
    }

    /**
     * This method will search for data in specific collection
     * 
     * @param searchBoxText - String instance - keyword using which we'll search
     *            for data
     * @param comboBoxOption - WebElement instance - WebElement for combo box
     *            option
     * @param collectionLabel - String instance - label of user defined
     *            collection
     */
    private void searchForData(String searchBoxText, WebElement comboBoxOption,
            String collectionLabel) {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        enterTextInSearchBox(searchPage.getSearchField(), searchBoxText);
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);",
                searchPage.getComboBoxOptionCollections());
        List<WebElement> comboBoxElements = browserDriver.findElements(By.xpath(
                "//div[contains(@data-json,'{\"id\":\"CSShareOpenSearchArea')]"));
        int comboBoxElementsCount = comboBoxElements.size();
        if (comboBoxElementsCount >= 5) {
            ((JavascriptExecutor) browserDriver).executeScript(
                    "arguments[0].scrollIntoView(true);", comboBoxOption);
            searchPage.clkWebElement(waitForReload, comboBoxOption);
        }
        WebElement collection = browserDriver.findElement(
                By.xpath("//div[contains(text(),'" + collectionLabel + "')]"));
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);", collection);
        searchPage.clkWebElement(waitForReload, collection);
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
     * @param element - WebElement instance
     */
    private void enterTextInSearchBox(WebElement element,
            String searchBoxText) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.clear();
        element.sendKeys(searchBoxText);
        CSUtility.tempMethodForThreadSleep(3000);
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
     * This data provider contains text to be entered into search box
     * 
     * @return array of search box text
     */
    @DataProvider(name = "searchingAnyItemInSelectedCollection")
    public Object[][] getDataForSearchBoxSuggestions() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                searchBoxTextSheet);
    }
}
