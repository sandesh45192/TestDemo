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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test method to verify suggestions list in combo box
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfSuggestionListInComboBoxTest extends AbstractTest {

    private WebDriverWait  waitForReload;
    private CSPortalHeader csPortalHeader;
    private SearchPage     searchPage;
    private SoftAssert     softAssertion;
    private String         searchComboBoxSuggestionsSheet = "SearchComboBoxSuggestions";

    /**
     * This is the test method which drives the usecase ie. Verification of
     * suggestions list in combo box
     * 
     * @param String comboBoxText - text to be entered into the combo box
     * 
     */
    @Test(dataProvider = "searchComboBoxSuggestions")
    public void
            testVerificationOfSuggestionListInComboBox(String comboBoxText) {
        try {
            csPortalHeader.clkSearchHeader(waitForReload);
            TraversingForSearchModule.frameTraversingForCollectionsPopup(
                    waitForReload, browserDriver);
            enterTextInSearchComboBox(searchPage.getTxtBoxSearchComboBox(),
                    comboBoxText);
            CSUtility.tempMethodForThreadSleep(4000);
            List<WebElement> comboBoxSuggestions = browserDriver.findElements(By
                    .xpath("//div[contains(@class,'ms-res-ctn dropdown-menu')]"
                            + "//div[contains(@class,'ms-res-item')]/div"
                            + "//div[contains(text(),'" + comboBoxText
                            + "')]"));
            int comboBoxSuggestionsCount = comboBoxSuggestions.size();
            if (comboBoxSuggestionsCount == 0) {
                softAssertion.assertFalse(comboBoxSuggestionsCount == 0,
                        "Suggestions list is not being displayed for entered text in search combo box!");
                CSLogger.error(
                        "Suggestions list is not being displayed for entered text in search combo box!");
            } else {
                softAssertion.assertTrue(comboBoxSuggestionsCount > 0);
                CSLogger.info(
                        "Suggestions list is displayed for entered text in search combo box");
            }
            softAssertion.assertAll();
            CSLogger.info(
                    "‘Verification of suggestions list in combo box’ test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerificationOfSuggestionListInComboBox",
                    e);
            Assert.fail(
                    "Automation error : testVerificationOfSuggestionListInComboBox",
                    e);
        }
    }

    /**
     * This method will enter text in search combo box
     * 
     * @param element - WebElement instance
     * @param comboBoxText - String instance - text to be entered into search
     *            combo box
     */
    private void enterTextInSearchComboBox(WebElement element,
            String comboBoxText) {
        CSUtility.waitForElementToBeClickable(waitForReload, element);
        searchPage.clkWebElement(waitForReload, element);
        element.clear();
        CSLogger.info("cleared the content of search combo box");
        CSUtility.tempMethodForThreadSleep(2000);
        element.sendKeys(comboBoxText);
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 180);
        softAssertion = new SoftAssert();
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
    }

    /**
     * This data provider contains text to be entered into search combo box
     * 
     * @return array of search combo box text
     */
    @DataProvider(name = "searchComboBoxSuggestions")
    public Object[][] getDataForSearchComboBoxSuggestions() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                searchComboBoxSuggestionsSheet);
    }
}
