/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

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
 * This class contains the test methods which verifies usage Of Clear Button
 * 
 * @author CSAutomation Team
 */
public class UsageOfClearButtonTest extends AbstractTest {

    private CSPortalHeader csPortalHeader;
    private WebDriverWait  waitForReload;
    private SearchPage     searchPage;
    private String         usageOfClearButtonSheet = "UsageOfClearButton";

    /**
     * This method verifies usage Of Clear Button
     * 
     * @param searchData String object contains search data
     * @param comboBoxData String object contains combo Box name
     * @param filterData String object contains filter data
     */
    @Test(dataProvider = "usageOfClearButton")
    public void testUsageOfClearButton(String searchData, String comboBoxData,
            String filterData) {
        try {
            String[] dataArray = filterData.split(":");
            csPortalHeader.clkSearchHeader(waitForReload);
            TraversingForSearchModule.frameTraversingForCollectionsPopup(
                    waitForReload, browserDriver);
            preformSearchOperationOnFacetedSearch(searchData, comboBoxData);
            filterSearchData(dataArray[1]);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    searchPage.getResultsMetaText());
            String text = searchPage.getResultsMetaText().getText();
            int resultcount = getResult(text);
            performClearOperation(dataArray[0]);
            verifyUsageOfClearButton(resultcount);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testUsageOfClearButton", e);
            Assert.fail("Automation Error : testUsageOfClearButton", e);
        }
    }

    /**
     * This method verifies the usages of clear button
     * 
     * @param filteredResult String object contains filter result
     */
    private void verifyUsageOfClearButton(int filteredResult) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getResultsMetaText());
        String text = searchPage.getResultsMetaText().getText();
        int currentResult = getResult(text);
        if (currentResult >= filteredResult) {
            CSLogger.info("Usage of clear button verified");
        } else {
            CSLogger.error("Verification fail for usags of clear button");
            Assert.fail("Verification fail for usags of clear button");
        }
    }

    /**
     * This method get the result of product count from the text
     * 
     * @param text String object contains result of search operation
     * @return count contains count of search result
     */
    private int getResult(String text) {
        String[] textArray = text.split(" ");
        int count = Integer.parseInt(textArray[0]);
        return count;
    }

    /**
     * This method perform clear operation on attribute
     * 
     * @param attrName String object contains attr name
     */
    private void performClearOperation(String attrName) {
        WebElement btnClear = browserDriver
                .findElement(By.xpath("//div[contains(text(),'" + attrName
                        + "')]/../div[contains(text(),'Reset')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnClear);
        btnClear.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("Clicked on clear button of attribute " + attrName);
    }

    /**
     * This method filter the search result
     * 
     * @param filterData String object contains attribute data to filter
     */
    private void filterSearchData(String filterData) {
        WebElement inputTextElement = browserDriver.findElement(
                By.xpath("//input[@placeholder='Type or click here']"));
        sendTextToElement(inputTextElement, filterData);
        WebElement valueSuggession = browserDriver.findElement(
                By.xpath("//div[contains(text(),'" + filterData + "')]"));
        valueSuggession.click();
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method perform searching operation on faceted search
     * 
     * @param searchData String object contains search data
     * @param comboBoxData String object contains share folder name
     */
    private void preformSearchOperationOnFacetedSearch(String searchData,
            String comboBoxData) {
        sendTextToElement(searchPage.getSearchField(), searchData);
        sendTextToElement(searchPage.getTxtBoxSearchComboBox(), comboBoxData);
        WebElement suggestionElement = browserDriver.findElement(
                By.xpath("//div[contains(text(),'" + comboBoxData + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, suggestionElement);
        suggestionElement.click();
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSUtility.tempMethodForThreadSleep(3000);
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
     * @return usageOfClearButtonSheet
     */
    @DataProvider(name = "usageOfClearButton")
    public Object[] usageOfClearButton() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                usageOfClearButtonSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        searchPage = new SearchPage(browserDriver);
    }
}
