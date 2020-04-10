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
 * This class contains the test method for searching any item in shares
 * 
 * @author CSAutomation Team
 *
 */
public class SearchingAnyItemInSharesTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private CSPortalHeader csPortalHeader;
    private SearchPage     searchPage;
    private SoftAssert     softAssertion;
    private String         searchBoxTextSheet = "SearchAnyItemInShares";

    /**
     * This is the test method which drives the use-case of searching any item
     * in shares
     * 
     * @param searchBoxText - String instance - keyword used to search for data
     *            in share
     * @param shareLabel - String instance - label of share to be selected in
     *            search combo box
     */
    @Test(dataProvider = "searchAnyItemInShares")
    public void testSearchingAnyItemInShares(String searchBoxText,
            String shareLabel) {
        try {
            searchForData(searchBoxText,
                    searchPage.getComboBoxOptionMoreForShares(), shareLabel);

            List<WebElement> shareSearchElements = browserDriver
                    .findElements(By.xpath("//div[@class='CSListViewInner"
                            + "Container']/div[1]/div[2]/ul/li"));
            int countShareSearchElements = shareSearchElements.size();
            if (countShareSearchElements == 0) {
                softAssertion.assertFalse(countShareSearchElements == 0,
                        "Error! Search Results are not being displayed! Either "
                                + "search keyword is wrong OR data related to "
                                + "searched keyword does not exist.");
                CSLogger.error("Error! Search Results are not being displayed! "
                        + "Either search keyword is wrong OR data related to "
                        + "searched keyword does not exist.");
            } else if (countShareSearchElements == 1) {
                WebElement searchedElement = shareSearchElements.get(0);
                String attrClass = searchedElement.getAttribute("class");
                if (attrClass.contains("CSListViewItem dummy")) {
                    softAssertion.assertFalse(countShareSearchElements == 1,
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
                    softAssertion.assertTrue(countShareSearchElements == 1);
                    CSLogger.info(
                            "Search results are being displayed as expected.");
                }
            } else {
                softAssertion.assertTrue(countShareSearchElements > 1);
                CSLogger.info(
                        "Search results are being displayed as expected.");
            }
            softAssertion.assertAll();
            CSLogger.info("Searching any item in shares test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testSearchingAnyItemInShares",
                    e);
            Assert.fail("Automation error : testSearchingAnyItemInShares", e);
        }
    }

    /**
     * This method will search for data in specific a specific share
     * 
     * @param searchBoxText - String instance - keyword using which we'll search
     *            for data
     * @param comboBoxOption - WebElement instance - WebElement for combo box
     *            option
     * @param shareLabel - String instance - label of user defined share
     */
    private void searchForData(String searchBoxText, WebElement comboBoxOption,
            String shareLabel) {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        enterTextInSearchBox(searchPage.getSearchField(), searchBoxText);
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);",
                searchPage.getComboBoxOptionShares());
        List<WebElement> comboBoxElements = browserDriver.findElements(By.xpath(
                "//div[contains(@data-json,'{\"id\":\"ProductOpenSearchArea')]"));
        int comboBoxElementsCount = comboBoxElements.size();
        if (comboBoxElementsCount >= 6) {
            ((JavascriptExecutor) browserDriver).executeScript(
                    "arguments[0].scrollIntoView(true);", comboBoxOption);
            searchPage.clkWebElement(waitForReload, comboBoxOption);
        }
        WebElement share = browserDriver.findElement(
                By.xpath("//div[contains(text(),'" + shareLabel + "')]"));
        ((JavascriptExecutor) browserDriver)
                .executeScript("arguments[0].scrollIntoView(true);", share);
        searchPage.clkWebElement(waitForReload, share);
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
    @DataProvider(name = "searchAnyItemInShares")
    public Object[][] getDataForSearchBoxSuggestions() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                searchBoxTextSheet);
    }
}
