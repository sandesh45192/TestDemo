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
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test method to verify search field and combo box
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfSearchFieldAndComboBoxTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private CSPortalHeader         csPortalHeader;
    private CSGuiToolbarHorizontal guiToolbarHorizontal;
    private SearchPage             searchPage;
    private SoftAssert             softAssertion;
    private CSPortalWidget         portalWidget;

    /**
     * This is the test method which drives the usecase ie. Verification of
     * search field and combo box
     * 
     */
    @Test
    public void testVerificationOfSearchFieldAndComboBox() {
        try {
            enableEverywhereSearch();
            verifySearchFieldAndComboBox();
            verifySortOrderInComboBox();
            softAssertion.assertAll();
            CSLogger.info(
                    "‘Verification of search field and combo box’ test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerificationOfSearchFieldAndComboBox",
                    e);
            Assert.fail(
                    "Automation error : testVerificationOfSearchFieldAndComboBox",
                    e);
        }
    }

    /**
     * This method will verify the sort order in combo box
     */
    private void verifySortOrderInComboBox() {
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
                "CSGuiEditorCheckboxContainer On Enabled GuiEditorCheckbox")) {
            searchPage.getChkboxEverywhereSearch().click();
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("toggle 'activate search in everywhere' turned off");
        } else {
            CSLogger.info(
                    "toggle 'activate search in everywhere' is already turned off");
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getChkboxAllSharesSearch());
        String attrClassValueAllSharesSearch = searchPage
                .getChkboxAllSharesSearch().getAttribute("class");
        if (attrClassValueAllSharesSearch.equals(
                "CSGuiEditorCheckboxContainer On Enabled GuiEditorCheckbox")) {
            searchPage.getChkboxAllSharesSearch().click();
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("toggle 'activate search in all shares' turned off");
        } else {
            CSLogger.info(
                    "toggle 'activate search in all shares' is already turned off");
        }
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        browserDriver.navigate().refresh();
        CSUtility.tempMethodForThreadSleep(2000);
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getSearchComboBox());
        searchPage.clkWebElement(waitForReload, searchPage.getDrpDwnComboBox());
        CSLogger.info("clicked on combo box drop down arrow");
        CSUtility.tempMethodForThreadSleep(2000);
        verifyElementsInComboBox();
    }

    /**
     * This method will verify the search field and combo box
     */
    private void verifySearchFieldAndComboBox() {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        verifyPresenceOfElement(waitForReload, searchPage.getSearchField(),
                "search field");
        verifyPresenceOfElement(waitForReload, searchPage.getSearchComboBox(),
                "search combo box");
        String searchComboBoxText = searchPage.getSearchComboBoxDefaultText()
                .getText();
        if (searchComboBoxText.equals("Everywhere")) {
            CSLogger.info(
                    "search combo box is displayed with 'Everywhere' share selected in it");
        } else {
            softAssertion.assertTrue(searchComboBoxText.equals("Everywhere"),
                    "'Everywhere' share is not selected in search combo box!");
        }
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
     * This method will verify the elements in combo box drop down menu
     */
    private void verifyElementsInComboBox() {
        verifyPresenceOfElement(waitForReload,
                searchPage.getComboBoxOptionCollections(),
                "Combo box option 'Collections'");
        verifyPresenceOfElement(waitForReload,
                searchPage.getComboBoxOptionShares(),
                "Combo box option 'Shares'");
        verifyPresenceOfElement(waitForReload,
                searchPage.getComboBoxOptionFavorites(),
                "Combo box option 'Favorites'");
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
                + " is not being displayed. Expected behaviour not being met");
        if (presence) {
            CSLogger.info(label + " is displayed, as expected");
        } else {
            CSLogger.error(label + " is not being displayed. "
                    + "Expected behaviour not being met");
        }
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
        CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
    }
}
