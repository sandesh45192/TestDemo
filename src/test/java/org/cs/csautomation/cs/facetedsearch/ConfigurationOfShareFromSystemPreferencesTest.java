/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods which verifies faceted search when
 * elastic search is On
 * 
 * @author CSAutomation Team
 */
public class ConfigurationOfShareFromSystemPreferencesTest
        extends AbstractTest {

    private CSPortalHeader         csPortalHeader;
    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private SearchPage             searchPage;
    private CSPortalWidget         portalWidget;
    private SoftAssert             softAssert;
    private CSPopupDivPim          popupDiv;
    private CSGuiDialogContentId   csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private String                 shareFromSystemPreferenceSheet = "ShareFromSystemPreference";
    private String                 sectionGeneral                = "General";
    private String                 sectionSearchSetting          = "Search settings";
    private String                 sectionDataFilter             = "Data Filter";
    private String                 sectionListAttributes         = "List attributes";
    private String                 sectionPreviewAttributes      = "Preview Attributes";
    private String                 sectionActions                = "Actions";

    /**
     * This method verifies attribute mapping in class.
     * 
     * @param searchAreaData String object contains search area name
     * @param searchPlugin String object contains search plugin name
     * @param selectionType String object contains selection type of share
     * @param productFolder String object contains product folder name
     */
    @Test(dataProvider = "shareFromSystemPreference")
    public void testConfigurationOfShareFromSystemPreferences(
            String searchAreaData, String searchPlugin, String selectionType,
            String productFolder) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToShares();
            CSUtility.tempMethodForThreadSleep(2000);
            createNewShare(searchAreaData, searchPlugin);
            selectRootFolder(selectionType, productFolder);
            verifyCreatedShare(searchAreaData);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testConfigurationOfShareFrom"
                    + "SystemPreferences", e);
            Assert.fail("Automation Error : testConfigurationOfShareFrom"
                    + "SystemPreferences", e);
        }
    }

    /**
     * This method select root folder.
     * 
     * @param selectionType String object contains selection type of share
     * @param productFolder String object contains product folder name
     */
    private void selectRootFolder(String selectionType, String productFolder) {
        openSection(sectionDataFilter);
        selectVaueFromDrpDwn(searchPage.getDrpDwnSelectionType(), selectionType);
        searchPage.clkWebElement(waitForReload, searchPage.addElement());
        CSUtility.tempMethodForThreadSleep(1000);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogLeftSectionOpenSearch(
                        waitForReload, browserDriver);
        WebElement createdProductsFolder = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + productFolder + "')]"));
        Actions action = new Actions(browserDriver);
        action.doubleClick(createdProductsFolder).build().perform();
        CSLogger.info("double clicked on root folder");
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogCenterSectionOpenSearch(
                        waitForReload, browserDriver);
        searchPage.clkWebElement(waitForReload, csGuiToolbarHorizontalInstance
                .getBtnCSGuiToolbarAddAllValues());
        CSLogger.info("Clicked on add all values button");
        CSUtility.tempMethodForThreadSleep(3000);
        TraversingForSearchModule.traverseToFrmToAddAllRootFolderValues(waitForReload,browserDriver);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnYes());
        CSLogger.info("clicked on option yes");
        CSUtility.tempMethodForThreadSleep(1000);
        popupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload, searchPage.getChkBoxShowFolders());
        if(searchPage.getChkBoxShowFolders().getAttribute("class").contains("Off")) {
            searchPage.getChkBoxShowFolders().click(); 
            CSLogger.info("Clicked on Show folders");
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method verifies the created share from open search
     * 
     * @param searchAreaData String object contains search area name
     */
    private void verifyCreatedShare(String searchAreaData) {
        int column = 0;
        String textTitleBar = null;
        List<String> sharesInTable = new ArrayList<String>();
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        List<WebElement> titleBarElement = browserDriver.findElements(
                By.xpath("//table[@class='hidewrap CSAdminList']//tbody//tr"
                        + "[@class='TitleBar']//th"));
        for (int index = 0; index < titleBarElement.size(); index++) {
            textTitleBar = titleBarElement.get(index).getText();
            if (textTitleBar.equals("Search Area")) {
                column = index + 1;
                break;
            }
        }
        List<WebElement> textShareElement = browserDriver.findElements(By
                .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr//td["
                        + column + "]"));
        for (WebElement shareText : textShareElement) {
            sharesInTable.add(shareText.getText());
        }
        if (sharesInTable.contains(searchAreaData)) {
            CSLogger.info("Share created Successfully");
        } else {
            CSLogger.error(
                    "Verification fail for created share" + searchAreaData);
            Assert.fail("Verification fail for created share" + searchAreaData);
        }
    }

    /**
     * This method create new share
     * 
     * @param searchAreaData String object contains search area name
     * @param searchPlugin String object contains search plugin name
     */
    private void createNewShare(String searchAreaData, String searchPlugin) {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew());
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, locator);
        verifySections(sectionGeneral);
        verifySections(sectionSearchSetting);
        openSection(sectionGeneral);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        searchPage.getLblOpenSearchArea().sendKeys(searchAreaData);
        selectVaueFromDrpDwn(searchPage.getDrpDwnSearchPlugin(),searchPlugin);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        verifySections(sectionDataFilter);
        verifySections(sectionListAttributes);
        verifySections(sectionPreviewAttributes);
        verifySections(sectionActions);
    }

    /**
     * This method select the value from drop down
     * 
     * @param selector WebElement object contains elemnet of drop down
     * @param value String object contains value to select
     */
    private void selectVaueFromDrpDwn(WebElement selector, String value) {
        CSUtility.waitForVisibilityOfElement(waitForReload, selector);
        csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(selector,
                value);
    }

    /**
     * This method verifies the sections
     * 
     * @param sectionName String object contains section name
     */
    private void verifySections(String sectionName) {
        WebElement sectionElement = browserDriver.findElement(
                By.xpath("//div[contains(text(),'" + sectionName + "')]"));
        if (sectionElement.isDisplayed()) {
            CSLogger.info("Section " + sectionName + "is verified");
        } else {
            CSLogger.error("Verification fail for section " + sectionName);
            softAssert.fail("Verification fail for section " + sectionName);
        }
    }

    /**
     * This method open the section
     * 
     * @param sectionName String object contains name of section
     */
    private void openSection(String sectionName) {
        String path = "//div[text()=' " + sectionName + "']/..";
        String extendedPath = path + "/..";
        WebElement selectionTitleParent = browserDriver
                .findElement(By.xpath(extendedPath));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                selectionTitleParent);
        if (selectionTitleParent.getAttribute("class").contains("closed")) {
            browserDriver.findElement(By.xpath(path)).click();
        }
    }

    /**
     * This method traverse to shares in system preferences
     */
    private void goToShares() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload, searchPage.getSearchNode());
        searchPage.clkWebElement(waitForReload,
                searchPage.getNodeSearchPortal());
        searchPage.clkWebElement(waitForReload, searchPage.getBtnShares());
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Search Area Data,Search Plugin,Selection Type,Product Name
     * 
     * @return shareFromSystemPreferenceSheet
     */
    @DataProvider(name = "shareFromSystemPreference")
    public Object[] shareFromSystemPreference() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                shareFromSystemPreferenceSheet);
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
        searchPage = new SearchPage(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        popupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        softAssert = new SoftAssert();
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
    }
}
