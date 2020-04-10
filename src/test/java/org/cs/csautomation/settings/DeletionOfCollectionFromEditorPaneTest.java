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
 * This class contains the test method to delete collection from editor pane
 * 
 * @author CSAutomation Team
 *
 */
public class DeletionOfCollectionFromEditorPaneTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private CSPortalHeader         csPortalHeader;
    private CSGuiToolbarHorizontal guiToolbarHorizontal;
    private String                 collectionConfigSheet = "DeleteCollectionsFromEditorPane";
    private SearchPage             searchPage;
    private SoftAssert             softAssertion;
    private CSPortalWidget         portalWidget;
    private CSPopupDivSettings     popupDivSettings;
    private WebElement             label                 = null;
    private boolean                status                = false;

    /**
     * This is the test method which drives the use case i.e. deletion of
     * collection from editor pane
     * 
     * @param labelCreateCollection - String instance - Label of collection to
     *            be created for deletion
     * @param labelDeleteCollection - String instance - Label of collection to
     *            be deleted
     */
    @Test(dataProvider = "deletionOfCollectionFromEditorPane")
    public void testDeletionOfCollectionsFromEditorPane(
            String labelCreateCollection, String labelDeleteCollection) {
        try {
            openSearchPortal();
            createCollectionForDeleting(labelCreateCollection);
            deleteCollection(labelDeleteCollection);
            verifyDeletedCollection(labelDeleteCollection);
            softAssertion.assertAll();
            CSLogger.info(
                    "‘Deletion of collections from editor pane’ test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testDeletionOfCollectionsFromEditorPane",
                    e);
            Assert.fail(
                    "Automation error : testDeletionOfCollectionsFromEditorPane",
                    e);
        }
    }

    /**
     * This method will create a collection for deletion
     * 
     * @param labelCreateCollection - String instance - label of collection to
     *            be deleted
     */
    private void createCollectionForDeleting(String labelCreateCollection) {
        searchPage.clkWebElement(waitForReload, searchPage.getBtnCollections());
        CSLogger.info("Clicked on 'Collections'");
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
                searchPage.getLblOpenSearchArea(), labelCreateCollection);
        CSLogger.info("Entered text in 'Search Area'");
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Created collection for deleting");
    }

    /**
     * This method will delete the collection from configuration
     * 
     * @param labelDeleteCollection - String instance - Label of collection to
     *            be deleted
     */
    private void deleteCollection(String labelDeleteCollection) {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnCollections());
        CSLogger.info("Clicked on 'Collections'");
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        List<WebElement> list = getList();
        for (int shareIndex = 0; shareIndex < list.size(); shareIndex++) {
            label = list.get(shareIndex);
            if (label.getText().equals(labelDeleteCollection)) {
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
            CSLogger.info("refreshed the page after deleting the collection");
        } else {
            CSLogger.error("Error. Collection has not been created. "
                    + "Create a collection before deleting it.");
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
     * This method will verify whether collection is successfully deleted or not
     * 
     * @param labelDeleteCollection - String instance - Label of collection to
     *            be verified for deletion
     */
    private void verifyDeletedCollection(String labelDeleteCollection) {
        List<WebElement> list = getList();
        for (int shareIndex = 0; shareIndex < list.size(); shareIndex++) {
            label = list.get(shareIndex);
            if (label.getText().equals(labelDeleteCollection)) {
                softAssertion.assertFalse(
                        label.getText().equals(labelDeleteCollection),
                        "Error. Collection is not deleted from list.");
                CSLogger.error("Error. Collection is not deleted from list.");
                status = false;
                break;
            }
        }
        if (status == true) {
            CSLogger.info("Collection is successfully deleted.");
            browserDriver.navigate().refresh();
            browserDriver.manage().timeouts().pageLoadTimeout(10,
                    TimeUnit.SECONDS);
            CSLogger.info("page refresh");
        }
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
     * This data provider contains label of collections to be created first &
     * then deleted
     * 
     * @return array of collection labels to be created first & then deleted
     */
    @DataProvider(name = "deletionOfCollectionFromEditorPane")
    public Object[][] getDataForDeletionOfCollections() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                collectionConfigSheet);
    }
}
