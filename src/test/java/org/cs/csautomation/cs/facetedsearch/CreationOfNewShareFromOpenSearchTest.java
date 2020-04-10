/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies created new share from
 * open search
 * 
 * @author CSAutomation Team
 */
public class CreationOfNewShareFromOpenSearchTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private FrameLocators          iframeLocatorsInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private SearchPage             searchPage;
    private String                 createNewShareSheet = "CreateNewShare";

    /**
     * This method verifies created new share from open search
     * 
     * @param searchAreaData String object contains search area name
     * @param searchPlugin String object contains search plugin name
     */
    @Test(dataProvider = "CreateNewShare")
    public void testCreationOfNewShareFromOpenSearch(String searchAreaData,
            String searchPlugin) {
        try {
            csPortalHeaderInstance.clkSearchHeader(waitForReload);
            String mainWindow = browserDriver.getWindowHandle();
            TraversingForSearchModule.frameTraversingForCollectionsPopup(
                    waitForReload, browserDriver);
            searchPage.clkWebElement(waitForReload, searchPage.getBtnOptions());
            searchPage.clkWebElement(waitForReload,
                    searchPage.getBtnConfigureShares());
            CSUtility.tempMethodForThreadSleep(1000);
            ArrayList<String> tabs = new ArrayList<String>(
                    browserDriver.getWindowHandles());
            browserDriver.switchTo().window(tabs.get(1));
            createNewShare(searchAreaData, searchPlugin);
            verifyCreatedShare(searchAreaData);
            browserDriver.close();
            browserDriver.switchTo().window(mainWindow);
            CSLogger.info("Switch to Open Search");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testCreationOfNewShareFromOpenSearch",
                    e);
            Assert.fail(
                    "Automation Error : testCreationOfNewShareFromOpenSearch",
                    e);
        }
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
        traverseToMainFrame();
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
     * This method create the new share from open search
     * 
     * @param searchAreaData String object contains search area name
     * @param searchPlugin String object contains search plugin name
     */
    private void createNewShare(String searchAreaData, String searchPlugin) {
        traverseToMainFrame();
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCreateNew(waitForReload);
        traverseToEditframe();
        String path = "//div[contains(text(),'General')]/..";
        WebElement parentSection = browserDriver
                .findElement(By.xpath(path + "/.."));
        CSUtility.waitForVisibilityOfElement(waitForReload, parentSection);
        if (parentSection.getAttribute("class").contains("closed")) {
            WebElement sectionElement = browserDriver
                    .findElement(By.xpath(path));
            sectionElement.click();
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                searchPage.getLblOpenSearchArea());
        searchPage.getLblOpenSearchArea().sendKeys(searchAreaData);
        Select plugin = new Select(searchPage.getDrpDwnSearchPlugin());
        plugin.selectByVisibleText(searchPlugin);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method traverse to edit window
     */
    private void traverseToEditframe() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEdit()));
        CSLogger.info("Traverse to Edit frame");
    }

    /**
     * This method traverse to main window
     */
    private void traverseToMainFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traverse to Main frame");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Search Area Data,Search Plugin
     * 
     * @return createNewShareSheet
     */
    @DataProvider(name = "CreateNewShare")
    public Object[] createNewShare() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                createNewShareSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        iframeLocatorsInstance = new FrameLocators(browserDriver);
    }
}
