/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
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
 * This class contains the test method to verify buttons in landing page
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfButtonsInLandingPageTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private CSPortalHeader csPortalHeader;
    private SearchPage     searchPage;
    private SoftAssert     softAssertion;
    private String         collectionConfigSheet = "VerifyButtonsInLandingPage";

    /**
     * This is the test method which drives the use case i.e. Verification of
     * buttons in landing page
     * 
     * @param collectionLabel - String instance - label of collection for which
     *            buttons are to be verified
     */
    @Test(dataProvider = "verificationOfButtonsInLandingPage")
    public void testVerificationOfButtonsInLandingPage(String collectionLabel) {
        try {
            csPortalHeader.clkSearchHeader(waitForReload);
            clickOnCollections(collectionLabel);
            verifyCollectionResults();
            verifyOptions();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerificationOfButtonsInLandingPage",
                    e);
            Assert.fail(
                    "Automation error : testVerificationOfButtonsInLandingPage",
                    e);
        }
    }

    /**
     * This method will verify the elements in Options tab
     */
    private void verifyOptions() {
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnOptions());
        CSLogger.info("clicked on options button");
        CSUtility.tempMethodForThreadSleep(2000);
        verifyElementsInOptions();
        CSLogger.info("Verification of options menu completed.");
    }

    /**
     * This method will verify the elements in selected Collection
     */
    private void verifyCollectionResults() {
        ArrayList<String> tabs = new ArrayList<String>(
                browserDriver.getWindowHandles());
        browserDriver.switchTo().window(tabs.get(1));
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getResultsMetaText());
        String resultText = searchPage.getResultsMetaText().getText();
        boolean result = resultText.equalsIgnoreCase("0 Results in");
        if (result) {
            softAssertion.assertFalse(result);
            CSLogger.error(
                    "Either collection results are not being displayed properly "
                            + "OR collection does not have any data");
        } else {
            softAssertion.assertFalse(result);
            CSLogger.info("Collection results are being displayed as expected "
                    + "in new tab");
        }
        CSUtility.tempMethodForThreadSleep(2000);
        browserDriver.close();
        browserDriver.switchTo().window(tabs.get(0));
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method will click on collection 'People'
     * 
     * @param collectionLabel - String instance - label of collection for which
     *            buttons are to be verified
     */
    private void selectCollection(String collectionLabel) {
        ((JavascriptExecutor) browserDriver).executeScript(
                "arguments[0].scrollIntoView(true);",
                searchPage.getPopupCollectionPeople());
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(searchPage.getPopupCollectionPeople()));
        searchPage.clkWebElement(waitForReload,
                searchPage.getPopupCollectionPeople());
        CSLogger.info("selected collection from collections pop-up");
    }

    /**
     * This method will click on Collections tab
     * 
     * @param collectionLabel - String instance - label of collection for which
     *            buttons are to be verified
     */
    private void clickOnCollections(String collectionLabel) {
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload,
                searchPage.getBtnCollectionPopup());
        CSLogger.info("clicked on collection pop-up");
        CSUtility.tempMethodForThreadSleep(2000);
        selectCollection(collectionLabel);
    }

    /**
     * This method will verify the elements in 'Options' menu
     */
    private void verifyElementsInOptions() {
        verifyPresenceOfElement(searchPage.getFederatedSearchConnector(),
                "'Federated Search Connector' option");
        verifyPresenceOfElement(searchPage.getConfigureSearch(),
                "'Configure search' option");
        verifyPresenceOfElement(searchPage.getMicrosoftSharepointConnector(),
                "'Microsoft sharepoint connector' option");
        verifyPresenceOfElement(searchPage.getSapNetweaverConnector(),
                "'SAP netweaver connector' option");
        verifyPresenceOfElement(searchPage.getAtlassianConfluenceConnector(),
                "'Atlassian confluence connector' option");
        verifyPresenceOfElement(searchPage.getContentservConnector(),
                "'CONTENTSERV connector' option");
        verifyPresenceOfElement(searchPage.getConfigureShares(),
                "'configure shares' option");
        verifyPresenceOfElement(searchPage.getSearchHelp(),
                "'Search help' option");
    }

    /**
     * This generic method will verify whether an element is present in webpage
     * or not
     * 
     * @param wait - WebDriverWait instance
     * @param element - WebElement
     * @param label - String, describing the WebElement
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
        waitForReload = new WebDriverWait(browserDriver, 60);
        softAssertion = new SoftAssert();
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        FrameLocators.getIframeLocators(browserDriver);
    }

    /**
     * This data provider contains data for verification of buttons in landing
     * page
     * 
     * @return array of collection results
     */
    @DataProvider(name = "verificationOfButtonsInLandingPage")
    public Object[][] getDataForVerificationOfButtonsInLandingPage() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                collectionConfigSheet);
    }
}
