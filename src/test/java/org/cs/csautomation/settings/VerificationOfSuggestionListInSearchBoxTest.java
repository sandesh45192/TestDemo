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
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test method to verify suggestions list in search box
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfSuggestionListInSearchBoxTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private CSPortalHeader         csPortalHeader;
    private CSGuiToolbarHorizontal guiToolbarHorizontal;
    private String                 searchSuggestionsSheet = "searchSuggestionsData";
    private SearchPage             searchPage;
    private SoftAssert             softAssertion;
    private CSPortalWidget         portalWidget;

    /**
     * This is the test method which drives the usecase i.e. Verification of
     * suggestions list in search box
     * 
     * @param String searchBoxText - Text which is to be entered into search box
     */
    @Test(dataProvider = "searchBoxSuggestions")
    public void
            testVerificationOfSuggestionListInSearchBox(String searchBoxText) {
        try {
            csPortalHeader.clkSearchHeader(waitForReload);
            TraversingForSearchModule.frameTraversingForCollectionsPopup(
                    waitForReload, browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    searchPage.getSearchField());
            searchPage.getSearchField().clear();
            CSUtility.tempMethodForThreadSleep(3000);
            searchPage.getSearchField().sendKeys(searchBoxText);
            CSUtility.tempMethodForThreadSleep(4000);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(
                            By.xpath("//div[@id='listViewContainerSuggestions']"
                                    + "/div/div[1]/div[3]/div[1]/div[2]/ul/li")));
            List<WebElement> searchSuggestions = browserDriver.findElements(
                    By.xpath("//div[@id='listViewContainerSuggestions']"
                            + "/div/div[1]/div[3]/div[1]/div[2]/ul/li"));
            int searchSuggestionsCount = searchSuggestions.size();
            if (searchSuggestionsCount == 0) {
                softAssertion.assertFalse(searchSuggestionsCount == 0,
                        "Suggestions list is not being displayed for "
                                + "entered text in search box!");
                CSLogger.error("Suggestions list is not being displayed for "
                        + "entered text in search box!");
            } else {
                softAssertion.assertTrue(searchSuggestionsCount > 0);
                CSLogger.info("Suggestions list is displayed for entered text "
                        + "in search box");
            }
            softAssertion.assertAll();
            CSLogger.info(
                    "‘Verification of suggestions list in search box’ test "
                            + "completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerificationOfSuggestionListInSearchBox",
                    e);
            Assert.fail(
                    "Automation error : testVerificationOfSuggestionListInSearchBox",
                    e);
        }
    }

    /**
     * This method will turn on the toggle 'Show Search Suggestions' in global
     * configuration
     */
    private void enableSearchSuggestionListInGlobalConfig() {
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
                searchPage.getChkboxShowSearchSuggestions());
        String attrClassValueShowSearchSuggestions = searchPage
                .getChkboxShowSearchSuggestions().getAttribute("class");
        if (attrClassValueShowSearchSuggestions.equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox")) {
            searchPage.clkWebElement(waitForReload,
                    searchPage.getChkboxShowSearchSuggestions());
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info(
                    "toggle 'Show Search Suggestions' was off and now turned on");
        } else {
            CSLogger.info(
                    "toggle 'Show Search Suggestions' is already turned on");
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
     * This data provider contains text to be entered into search box
     * 
     * @return array of search box text
     */
    @DataProvider(name = "searchBoxSuggestions")
    public Object[][] getDataForSearchBoxSuggestions() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                searchSuggestionsSheet);
    }

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 180);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        softAssertion = new SoftAssert();
        portalWidget = new CSPortalWidget(browserDriver);
        CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        enableSearchSuggestionListInGlobalConfig();
    }
}
