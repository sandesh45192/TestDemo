/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.List;
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
 * This class contains the test method for verification of selection mode in
 * everywhere share
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfSelectionModeInEverywhereShareTest
        extends AbstractTest {

    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private CSPortalHeader         csPortalHeader;
    private CSGuiToolbarHorizontal guiToolbarHorizontal;
    private SearchPage             searchPage;
    private SoftAssert             softAssertion;
    private CSPortalWidget         portalWidget;
    private String                 searchBoxTextSheet = "SearchDataInEverywhereShare";

    /**
     * This is the test method which drives the use-case of verifying the
     * selection mode in everywhere share
     * 
     */
    @Test(dataProvider = "searchDataInEverywhereShare")
    public void testVerificationOfSelectionModeInEverywhereShare(
            String searchBoxText) {
        try {
            searchInEverywhereShare(searchBoxText);
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
                verifySelectionMode();
            }
            softAssertion.assertAll();
            CSLogger.info(
                    "Verification of selection mode in everywhere share test "
                            + "completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerificationOfSelectionMode"
                    + "InEverywhereShare", e);
            Assert.fail("Automation error : testVerificationOfSelectionMode"
                    + "InEverywhereShare", e);
        }
    }

    /**
     * This method will verify whether selection mode is enabled & all items are
     * in de-selected mode or not
     */
    private void verifySelectionMode() {
        verifyPresenceOfElement(waitForReload,
                searchPage.getChkboxSelectionMode(),
                "Selection mode 'check box'");
        verifyPresenceOfElement(waitForReload,
                searchPage.getLinkIconSelectionMode(),
                "Selection mode 'Link icon'");
        verifySelectionModeEnabled();
        List<WebElement> selectedItems = browserDriver.findElements(
                By.xpath("//div[@class='CSListViewSectionContainer']/"
                        + "ul//li[@class='CSListViewItem selected']"));
        int countOfSelectedItems = selectedItems.size();
        if (countOfSelectedItems == 0) {
            softAssertion.assertTrue(countOfSelectedItems == 0);
            CSLogger.info("All items are in de-selected mode as expected.");
        } else {
            softAssertion.assertTrue(countOfSelectedItems == 0,
                    "Error! Some items might be selected. Not all items "
                            + "are in de-selected mode.");
            CSLogger.error("Error! Some items might be selected. Not all items "
                    + "are in de-selected mode.");
        }
    }

    /**
     * This method will verify whether selection mode is enabled or not
     */
    private void verifySelectionModeEnabled() {
        boolean chkBox = searchPage.getChkboxSelectionMode().isDisplayed();
        boolean linkIcon = searchPage.getLinkIconSelectionMode().isDisplayed();
        if (chkBox && linkIcon) {
            softAssertion.assertTrue(chkBox && linkIcon);
            CSLogger.info(
                    "Selection mode is enabled, as check boxes & link icons "
                            + "are visible");
        } else {
            softAssertion.assertTrue(chkBox && linkIcon,
                    "Error! Selection mode is not enabled, as either or both "
                            + "check boxes & link icons are not visible.");
            CSLogger.error(
                    "Error! Selection mode is not enabled, as either or both "
                            + "check boxes & link icons are not visible.");
        }
    }

    /**
     * This method will search for data in everywhere share
     * 
     * @param searchBoxText - String instance - keyword using which we'll search
     *            for data
     */
    private void searchInEverywhereShare(String searchBoxText) {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        enterTextInSearchBox(searchPage.getSearchField(), searchBoxText);
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);",
                searchPage.getComboBoxOptionEverywhere());
        searchPage.clkWebElement(waitForReload,
                searchPage.getComboBoxOptionEverywhere());
        CSUtility.tempMethodForThreadSleep(3000);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSLogger.info("clicked on search button of search page");
        CSUtility.tempMethodForThreadSleep(5000);
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
     * This method will verify whether any particular element is displayed or
     * not
     * 
     * @param wait - web driver wait
     * @param element - web element
     * @param label - label of element, which is to be verified
     */
    private void verifyPresenceOfElement(WebDriverWait wait, WebElement element,
            String label) {
        CSUtility.waitForVisibilityOfElement(wait, element);
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
        waitForReload = new WebDriverWait(browserDriver, 30);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        softAssertion = new SoftAssert();
        portalWidget = new CSPortalWidget(browserDriver);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        enableEverywhereSearch();
    }

    /**
     * This data provider returns sheet which contains text to be entered into
     * search box
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
