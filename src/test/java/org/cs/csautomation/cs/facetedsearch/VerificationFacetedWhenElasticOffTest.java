/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.ElasticSearchPage;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.settings.SettingsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies faceted search when
 * elastic search is off
 * 
 * @author CSAutomation Team
 */
public class VerificationFacetedWhenElasticOffTest extends AbstractTest {

    private CSPortalHeader         csPortalHeader;
    private WebDriverWait          waitForReload;
    private SettingsPage           settingPage;
    private FrameLocators          locator;
    private SearchPage             searchPage;
    private ElasticSearchPage      elasticSearchPage;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private String                 messageToVerify = "Facetted Search is not "
            + "available as long as ElasticSearch is not enabled";

    /**
     * This Method verifies the faceted search when elastic search is off
     * 
     */
    @Test
    private void testVerificationFaceteWhenElasticOff() {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            traverseToNodeExportFramework();
            disableElasticSearch();
            performSearchOperation();
            verifySearchResult();
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            traverseToNodeExportFramework();
            enableElasticSearch();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error : testVerificationFaceteWhenElasticOff",
                    e);
            Assert.fail(
                    "Automation error : testVerificationFaceteWhenElasticOff",
                    e);
        }
    }

    private void enableElasticSearch() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkOnNodeElasticSearchDetails(waitForReload);
        WebElement selectionTitleParent = browserDriver.findElement(By.xpath(
                "//div[contains(text(),'ElasticSearch Operation')]/../.."));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                selectionTitleParent);
        if (selectionTitleParent.getAttribute("class").contains("closed")) {
            browserDriver.findElement(By.xpath(
                    "//div[contains(text(),'ElasticSearch Operation')]/.."))
                    .click();
        }
        String valueElasticSearchEnabled = getAttributeOfElement(
                elasticSearchPage.getChkBoxElasticSearchEnabled());
        if (valueElasticSearchEnabled.contains("Off")) {
            elasticSearchPage.getChkBoxElasticSearchEnabled().click();
            // Alert alertBox = CSUtility.getAlertBox(waitForReload,
            // browserDriver);
            // alertBox.accept();
        } else {
            CSLogger.info("Button is already in ON State");
        }
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method verifies the search result
     * 
     */
    private void verifySearchResult() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getCSPopupMessageInfoBox());
        String textMesage = searchPage.getCSPopupMessageInfoBox().getText();
        if (textMesage.contains(messageToVerify)) {
            CSLogger.info("Popup message is verified");
        } else {
            CSLogger.error("Popup message not verified.");
            Assert.fail("Popup message not verified.");
        }
        if (searchPage.getResultsMetaText().isDisplayed()) {
            CSLogger.info("Search result is displayed");
        } else {
            CSLogger.error("Search result is displayed");
            Assert.fail("Search result is displayed");
        }
        CSUtility.tempMethodForThreadSleep(20000);
        if (!searchPage.getSecCategories().isDisplayed()) {
            CSLogger.info("Categories is not displayed");
        } else {
            CSLogger.error("Categories is displayed");
            Assert.fail("Categories is displayed");
        }
    }

    /**
     * This method perform the seach operation
     * 
     */
    private void performSearchOperation() {
        csPortalHeader.clkSearchHeader(waitForReload);
        TraversingForSearchModule.frameTraversingForCollectionsPopup(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getSearchField());
        searchPage.getSearchField().clear();
        searchPage.getSearchField().sendKeys("*");
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method disable the elastic search button and verifies its status
     * 
     */
    private void disableElasticSearch() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkOnNodeElasticSearchDetails(waitForReload);
        WebElement selectionTitleParent = browserDriver.findElement(By.xpath(
                "//div[contains(text(),'ElasticSearch Operation')]/../.."));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                selectionTitleParent);
        if (selectionTitleParent.getAttribute("class").contains("closed")) {
            browserDriver.findElement(By.xpath(
                    "//div[contains(text(),'ElasticSearch Operation')]/.."))
                    .click();
        }
        String valueElasticSearchEnabled = getAttributeOfElement(
                elasticSearchPage.getChkBoxElasticSearchEnabled());
        if (valueElasticSearchEnabled.contains("Off")) {
            CSLogger.info("Button is already in off State");
        } else {
            elasticSearchPage.getChkBoxElasticSearchEnabled().click();
            Alert alertBox = CSUtility.getAlertBox(waitForReload,
                    browserDriver);
            alertBox.accept();
        }
        CSUtility.tempMethodForThreadSleep(2000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method get the attribute of element
     * 
     * @param element WebElement object
     * @return attributeValue String object contains attribute value.
     */
    private String getAttributeOfElement(WebElement element) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        String attributeValue = element.getAttribute("class");
        return attributeValue;
    }

    /**
     * This method traverse to node export frame work.
     * 
     */
    private void traverseToNodeExportFramework() {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
        // CSUtility.waitForVisibilityOfElement(waitForReload,
        // activeJobsPage.getSystemPreferences());
        // activeJobsPage.clkWebElement(activeJobsPage.getSystemPreferences());
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        settingPage.clkOnBtnSettingsNode(waitForReload);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        elasticSearchPage.clkOnNodeESA(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        elasticSearchPage.clkOnNodeExportFramework(waitForReload);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        settingPage = new SettingsPage(browserDriver);
        elasticSearchPage = new ElasticSearchPage(browserDriver);
        searchPage = new SearchPage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }
}
