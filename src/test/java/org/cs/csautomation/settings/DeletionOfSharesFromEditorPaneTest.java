/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
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
 * This class contains the test method to delete shares from editor pane
 * 
 * @author CSAutomation Team
 *
 */
public class DeletionOfSharesFromEditorPaneTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private CSPortalHeader         csPortalHeader;
    private CSGuiToolbarHorizontal guiToolbarHorizontal;
    private String                 sharesConfigSheet = "DeletionOfSharesFromEditorPane";
    private SearchPage             searchPage;
    private SoftAssert             softAssertion;
    private CSPortalWidget         portalWidget;
    private CSPopupDivSettings     popupDivSettings;
    private WebElement             label             = null;
    private boolean                status            = false;

    /**
     * This is the test method which drives the use case i.e. deletion of shares
     * from editor pane
     * 
     * @param labelCreateShare - String instance - Label of share to be created
     *            for deletion
     * @param labelDeleteShare - String instance - Label of share to be deleted
     */
    @Test(dataProvider = "deletionOfSharesFromEditorPane")
    public void testDeletionOfSharesFromEditorPane(String labelCreateShare,
            String labelDeleteShare) {
        try {
            openSearchPortal();
            createShareForDeleting(labelCreateShare);
            deleteShare(labelDeleteShare);
            verifyDeletedShare(labelDeleteShare);
            softAssertion.assertAll();
            CSLogger.info(
                    "‘Deletion of shares from editor pane’ test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testDeletionOfSharesFromEditorPane", e);
            Assert.fail("Automation error : testDeletionOfSharesFromEditorPane",
                    e);
        }
    }

    /**
     * This method will create a share for deleting
     * 
     * @param labelCreateShare - String instance - Label of share to be created
     */
    private void createShareForDeleting(String labelCreateShare) {
        searchPage.clkWebElement(waitForReload, searchPage.getBtnShares());
        CSLogger.info("Clicked on option 'Shares'");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        guiToolbarHorizontal.clkBtnCSGuiToolbarCreateNew(waitForReload);
        CSLogger.info("Clicked on 'create new' button");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        String classAttribute = searchPage.getTabGeneral()
                .getAttribute("class");
        if (classAttribute.equalsIgnoreCase("section section_closed")) {
            searchPage.clkWebElement(waitForReload, searchPage.getSecGeneral());
            CSLogger.info("clicked on 'General' section");
        }
        searchPage.clkWebElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        CSLogger.info("clicked on 'Search Area' text box");
        searchPage.enterTextIntoTextbox(waitForReload,
                searchPage.getLblOpenSearchArea(), labelCreateShare);
        CSLogger.info("Entered text in 'Search Area'");
        searchPage.clkWebElement(waitForReload,
                searchPage.getDrpDwnSearchPlugin());
        CSLogger.info("search plugin drop down list visible");
        searchPage.clkWebElement(waitForReload, searchPage.getProductsPlugin());
        CSLogger.info("Created share for deleting.");
        browserDriver.navigate().refresh();
        browserDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        CSLogger.info("page refresh");
    }

    /**
     * This method will delete the share from configuration
     * 
     * @param labelDeleteShare - String instance - Label of share to be deleted
     */
    private void deleteShare(String labelDeleteShare) {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnShares());
        CSLogger.info("Clicked on 'Shares'");
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        List<WebElement> list = getList();
        for (int shareIndex = 0; shareIndex < list.size(); shareIndex++) {
            label = list.get(shareIndex);
            if (label.getText().equals(labelDeleteShare)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            searchPage.clkWebElement(waitForReload, label);
            CSUtility.tempMethodForThreadSleep(2000);
            TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                    browserDriver, locator);
            guiToolbarHorizontal.clkBtnShowMoreOption(waitForReload);
            popupDivSettings.selectPopupDivMenu(waitForReload,
                    guiToolbarHorizontal.getBtnSearchPortal(), browserDriver);
            popupDivSettings.selectPopupDivSubMenu(waitForReload,
                    searchPage.getCtxDelete(), browserDriver);
            CSUtility.getAlertBox(waitForReload, browserDriver).accept();
            CSLogger.info("clicked on OK of alert box");
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, locator);
            searchPage.clkWebElement(waitForReload,
                    guiToolbarHorizontal.getBtnCSGuiToolbarRefresh());
            CSLogger.info("refreshed the page after deleting the share");
        } else {
            CSLogger.error("Error. Share has not been created. "
                    + "Create a share before deleting it.");
        }

    }

    /**
     * This method will verify whether share is successfully deleted or not
     * 
     * @param labelDeleteShare - String instance - Label of share to be verified
     *            for deletion
     */
    private void verifyDeletedShare(String labelDeleteShare) {
        List<WebElement> list = getList();
        for (int shareIndex = 0; shareIndex < list.size(); shareIndex++) {
            label = list.get(shareIndex);
            if (label.getText().equals(labelDeleteShare)) {
                softAssertion.assertFalse(
                        label.getText().equals(labelDeleteShare),
                        "Error. Share is not deleted from list.");
                CSLogger.error("Error. Share is not deleted from list.");
                status = false;
                break;
            }
        }
        if (status == true) {
            CSLogger.info("Share is successfully deleted.");
            browserDriver.navigate().refresh();
            browserDriver.manage().timeouts().pageLoadTimeout(10,
                    TimeUnit.SECONDS);
            CSLogger.info("page refresh");
        }
    }

    /**
     * This method will return all the collections from configuration
     * 
     * @return list - containing collections from configuration
     */
    private List<WebElement> getList() {
        List<WebElement> list = browserDriver.findElements(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody"
                        + "//tr[contains(@onclick,'return cs_edit')]/td[3]"));
        return list;
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
        clickOnSearch();
        clickOnSearchPortal();
    }

    /**
     * This method will click on 'Search Portal' drop down in 'settings-system
     * preferences-search'
     */
    private void clickOnSearchPortal() {
        searchPage.clkWebElement(waitForReload,
                searchPage.getNodeSearchPortal());
        CSLogger.info("Clicked on option 'Search Portal'");
    }

    /**
     * This method will click on 'search' drop down in 'settings-system
     * preferences'
     */
    private void clickOnSearch() {
        searchPage.clkWebElement(waitForReload, searchPage.getSearchNode());
        CSLogger.info("Clicked on node 'search'");
    }

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 180);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        softAssertion = new SoftAssert();
        portalWidget = new CSPortalWidget(browserDriver);
        popupDivSettings = new CSPopupDivSettings(browserDriver);
    }

    /**
     * This data provider contains label of shares to be created first & then
     * deleted
     * 
     * @return array of share labels to be created first & then deleted
     */
    @DataProvider(name = "deletionOfSharesFromEditorPane")
    public Object[][] getDataForDeletionOfShares() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                sharesConfigSheet);
    }
}
