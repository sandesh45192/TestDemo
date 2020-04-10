/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies selecte option for the
 * faceted attribute
 * 
 * @author CSAutomation Team
 */
public class SelectFacetedAttributeFromSearchSettingTest extends AbstractTest {

    private CSPortalHeader         csPortalHeader;
    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSPortalWidget         portalWidget;
    private SearchPage             searchPage;
    private String                 secProducts = "Products";
    private String                 secFiles    = "Files";
    private String                 secChannels = "Channels";

    /**
     * This Method verifies selecte option for the faceted attribute
     * 
     */
    @Test
    private void testSelectFacetedAttributeFromSearchSetting() {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToSettingNodeOfSearchPortal();
            TraversingForSettingsModule.traversetoSystemPreferences(
                    waitForReload, browserDriver, locator);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameMain()));
            searchPage.clkWebElement(waitForReload,
                    searchPage.getShareSearchPortal());
            CSUtility.tempMethodForThreadSleep(1000);
            selectFacetedAttributeFromSection(secProducts);
            selectFacetedAttributeFromSection(secFiles);
            selectFacetedAttributeFromSection(secChannels);
            CSUtility.tempMethodForThreadSleep(3000);
            CSLogger.info("Select Faceted Attribute from search setting Completed.");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error : testSelectFacetedAttributeFromSearch"
                            + "Setting",
                    e);
            Assert.fail("Automation error : testSelectFacetedAttributeFrom"
                    + "SearchSetting", e);
        }
    }

    /**
     * This method select the facete attribute from section
     * 
     * @param sectionName String object contains name of section
     */
    private void selectFacetedAttributeFromSection(String sectionName) {
        String path = "//div[text()=' " + sectionName + "']/..";
        String extendedPath = path + "/..";
        WebElement selectionTitleParent = browserDriver
                .findElement(By.xpath(extendedPath));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                selectionTitleParent);
        if (selectionTitleParent.getAttribute("class").contains("closed")) {
            browserDriver.findElement(By.xpath(path)).click();
        }
        WebElement btnSelectAll = browserDriver
                .findElement(By.xpath("//div[@cs_name='" + sectionName
                        + "']//tr[@cs_name='Facet Attributes']//td[@class='"
                        + "EditbuilderCenter']//input"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnSelectAll);
        btnSelectAll.click();
        CSLogger.info("Click on button select all");
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        verifySelectedOptions(sectionName);
    }

    /**
     * This method verifies all the facete attributes are selected from section
     * 
     * @param sectionName String object contains name of section
     */
    private void verifySelectedOptions(String sectionName) {
        int countOfOptions = browserDriver
                .findElements(By.xpath("//div[@cs_name='" + sectionName
                        + "']//tr[@cs_name='Facet Attributes']//select"
                        + "[contains(@id,'SearchItemFacetAttributesNotSelected"
                        + "')]/option"))
                .size();
        if (countOfOptions == 0) {
            CSLogger.info(
                    "All Options in section " + sectionName + " are selected");
        } else {
            CSLogger.error("All Options in section " + sectionName
                    + " are not selected");
            Assert.fail("All Options in section " + sectionName
                    + " are not selected");
        }
    }

    /**
     * This method traverse to shares in system preferences
     */
    private void goToSettingNodeOfSearchPortal() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload, searchPage.getSearchNode());
        searchPage.clkWebElement(waitForReload,
                searchPage.getNodeSearchPortal());
        searchPage.clkWebElement(waitForReload,
                searchPage.getSearchPortalSettingsNode());
        CSLogger.info("Clicked on Setting Node of Search Portal");
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
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
        searchPage = new SearchPage(browserDriver);
    }
}
