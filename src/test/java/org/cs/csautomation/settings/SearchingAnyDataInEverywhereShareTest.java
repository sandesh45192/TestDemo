/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test method for searching any data in everywhere
 * share
 * 
 * @author CSAutomation Team
 *
 */
public class SearchingAnyDataInEverywhereShareTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private CSPortalHeader         csPortalHeader;
    private CSGuiToolbarHorizontal guiToolbarHorizontal;
    private SearchPage             searchPage;
    private SoftAssert             softAssertion;
    private CSPortalWidget         portalWidget;
    private String                 searchBoxTextSheet = "SearchDataInEverywhereShare";

    /**
     * This is the test method which drives the use-case of searching any data
     * in everywhere share
     * 
     */
    @Test(dataProvider = "searchDataInEverywhereShare")
    public void testSearchingAnyDataInEverywhereShare(String searchBoxText) {
        try {
            searchForData(searchBoxText,
                    searchPage.getComboBoxOptionEverywhere());
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
                CSLogger.info(
                        "Search results are being displayed as expected.");
            }
            softAssertion.assertAll();
            CSLogger.info(
                    "Searching any data in everywhere share test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testSearchingAnyDataInEverywhereShare",
                    e);
            Assert.fail(
                    "Automation error : testSearchingAnyDataInEverywhereShare",
                    e);
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
        CSUtility.tempMethodForThreadSleep(3000);
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
     * This method will enable the toggle for 'activate search in everywhere'
     */
    private void enableEverywhereSearch() {
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
        CSUtility.waitForElementToBeClickable(waitForReload,
                searchPage.getChkboxEverywhereSearch());
        String attrClassValueEverywhereSearch = searchPage
                .getChkboxEverywhereSearch().getAttribute("class");
        if (attrClassValueEverywhereSearch.equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox")) {
            searchPage.getChkboxEverywhereSearch().click();
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info(
                    "toggle 'activate search in everywhere' was off and now turned on");
        } else {
            CSLogger.info(
                    "toggle 'activate search in everywhere' is already turned on");
        }
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        browserDriver.navigate().refresh();
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
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        softAssertion = new SoftAssert();
        portalWidget = new CSPortalWidget(browserDriver);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        enableEverywhereSearch();
    }

    /**
     * This data provider contains text to be entered into search box
     * 
     * @return array of search box text
     */
    @DataProvider(name = "searchDataInEverywhereShare")
    public Object[][] getDataForSearchBoxSuggestions() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                searchBoxTextSheet);
    }
}
