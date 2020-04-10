/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies searching operation with
 * faceted class
 * 
 * @author CSAutomation Team
 */
public class FilteringOfSearchResultWithClassesTest extends AbstractTest {

    private CSPortalHeader csPortalHeader;
    private WebDriverWait  waitForReload;
    private SearchPage     searchPage;
    private String         searchingWithClassesSheet = "SearchingWithClasses";

    /**
     * This method verifies searching operation with faceted class
     * 
     * @param searchData String object contains search data
     * @param className String object contains class name
     * @param searchResult String object contains search result
     */
    @Test(dataProvider = "searchingWithClasses")
    public void testFilteringOfSearchResultWithClasses(String searchData,
            String className, String searchResult) {
        try {
            csPortalHeader.clkSearchHeader(waitForReload);
            TraversingForSearchModule.frameTraversingForCollectionsPopup(
                    waitForReload, browserDriver);
            preformSearchOpertionOnFacetedSearch(searchData);
            verifySearchResult();
            filterSearchWithClass(className);
            verifySearchProductList(searchResult);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testFilteringOfSearchResultWithClasses",
                    e);
            Assert.fail(
                    "Automation Error : testFilteringOfSearchResultWithClasses",
                    e);
        }
    }

    /**
     * This method verifies list of product after performing search operation
     * 
     * @param searchResult String object contains search result
     */
    private void verifySearchProductList(String searchResult) {
        String[] searchResultArray = searchResult.split(",");
        List<String> searchResultList = Arrays.asList(searchResultArray);
        List<WebElement> resultElement = browserDriver.findElements(By.xpath(
                "//ul[@class='CSListView SearchCSListView multipleSelection']"
                        + "//li//div//div[3]//div[2]//div//div//h1"));
        List<String> productsName = new ArrayList<String>();
        for (WebElement txtResult : resultElement) {
            CSUtility.scrollUpOrDownToElement(txtResult, browserDriver);
            productsName.add(txtResult.getText());
        }
        if (productsName.containsAll(searchResultList)) {
            CSLogger.info("Search result is display successfully");
        } else {
            CSLogger.error("Search result is incorrect");
            Assert.fail("Search result is incorrect");
        }
    }

    /**
     * This method filter the search result
     * 
     * @param className String object contains class name
     */
    private void filterSearchWithClass(String className) {
        WebElement elementClass = browserDriver.findElement(
                By.xpath("//a[contains(text(),'" + className + "')]"));
        CSUtility.scrollUpOrDownToElement(elementClass, browserDriver);
        elementClass.click();
        CSUtility.tempMethodForThreadSleep(3000);
        CSLogger.info("Search with faceted class");
    }

    /**
     * This method verifies search result
     * 
     */
    private void verifySearchResult() {
        if (searchPage.getResultsMetaText().isDisplayed()) {
            CSLogger.info("Search result is displayed");
        } else {
            CSLogger.error("Search result is displayed");
            Assert.fail("Search result is displayed");
        }
        if (searchPage.getSecCategories().isDisplayed()) {
            CSLogger.info("Categories is displayed");
        } else {
            CSLogger.error("Categories is not displayed");
            Assert.fail("Categories is not displayed");
        }
    }

    /**
     * This method perform searching operation on faceted search
     * 
     * @param searchData String object contains search data
     */
    private void preformSearchOpertionOnFacetedSearch(String searchData) {
        sendTextToElement(searchPage.getSearchField(), searchData);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSUtility.tempMethodForThreadSleep(5000);
        CSLogger.info("Perform search operation.");
    }

    /**
     * This method send the text data to the WebElement
     * 
     * @param txtElement WebElement object contains element of text input
     * @param text String object contains text data
     */
    private void sendTextToElement(WebElement txtElement, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, txtElement);
        txtElement.clear();
        txtElement.sendKeys(text);
        CSLogger.info(text + " data is sent.");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Search Data,Class Name,Search Result
     * 
     * @return searchingWithClassesSheet
     */
    @DataProvider(name = "searchingWithClasses")
    public Object[] searchingWithClasses() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                searchingWithClassesSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 30);
        csPortalHeader = new CSPortalHeader(browserDriver);
        searchPage = new SearchPage(browserDriver);
    }
}
