/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IFavoritesPopup;
import org.cs.csautomation.cs.pom.PimStudioChannelsNode;
import org.cs.csautomation.cs.pom.PimStudioFavoritesNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to edit existing product folder for this
 * test to run a product folder should be present with attributes assigned to it
 * 
 * @author CSAutomation Team
 *
 */
public class PublishFavoriteOptionTest extends AbstractTest {

    private WebDriverWait              waitForReload;
    private PimStudioChannelsNode      pimStudioChannelsNodeInstance;
    private PimStudioFavoritesNodePage pimStudioFavoritesNode;
    private CSPortalHeader             csPortalHeader;
    private String                     publishFavoriteOptionSheetName = "PublishFavoriteOption";

    /**
     * This test method Publish the Favorite folder which will display in
     * channel folder show in favorite dropdown.
     * 
     * @param favoriteFolderName String Object containing the favorite folder
     *            name
     * @param viewFolderName String Object containing name of the folder in
     *            channel
     * @param favoriteLabelName String object containing name of the tab of
     *            folder
     */
    @Test(priority = 1, dataProvider = "publishFavoriteOptionTestData")
    public void testPublishFavoriteOption(String favoriteFolderName,
            String viewFolderName, String productTabName) {
        try {
            publishFavoriteFolder(favoriteFolderName);
            gotochannelsProperties(viewFolderName, productTabName);
            verifyPublishFavorites(favoriteFolderName);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method: testPublishFavoriteOption: ",
                    e);
            Assert.fail(
                    "Autimation error in test method :testPublishFavoriteOption",
                    e);
        }
    }

    /**
     * This test method cancel Publish the Favorite folder which will remover it
     * from channel folder show in favorite dropdown.
     * 
     * @param favoriteFolderName String Object containing the favorite folder
     *            name
     * @param viewFolderName String Object containing name of the folder in
     *            channel
     * @param favoriteLabelName String object containing name of the tab of
     *            folder
     */
    @Test(priority = 2, dataProvider = "publishFavoriteOptionTestData")
    public void testCancelPublishFavoriteOption(String favoriteFolderName,
            String viewFolderName, String productTabName) {
        try {
            cancelPublishFavoriteFolder(favoriteFolderName);
            gotochannelsProperties(viewFolderName, productTabName);
            verifyCancelPublishFavorites(favoriteFolderName);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method: testCancelPublishFavoriteOption",
                    e);
            Assert.fail(
                    "Automation error in test method: testCancelPublishFavoriteOption: ",
                    e);
        }
    }

    /**
     * This method perform publish favorite folder.
     * 
     * @param favoriteFolderName String
     */
    public void publishFavoriteFolder(String favoriteFolderName) {
        IFavoritesPopup favoritePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioFavoritesNode.clkBtnPimFavoritesNode(waitForReload);
        pimStudioFavoritesNode.rightclkFavoritesFolder(waitForReload,
                favoriteFolderName);
        favoritePopup.selectPopupDivMenu(waitForReload,
                favoritePopup.getcsGuiPopupPublish(), browserDriver);
    }

    /**
     * This method perform cancel publish favorite folder.
     * 
     * @param favoriteFolderName String favorite folder name
     */
    public void cancelPublishFavoriteFolder(String favoriteFolderName) {
        IFavoritesPopup favoritePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioFavoritesNode.clkBtnPimFavoritesNode(waitForReload);
        pimStudioFavoritesNode.rightclkFavoritesFolder(waitForReload,
                favoriteFolderName);
        favoritePopup.selectPopupDivMenu(waitForReload,
                favoritePopup.getcsGuiPopupCancelPublish(), browserDriver);
    }

    /**
     * This method select the folder in channel and go to the show in favorite
     * dropdown.
     * 
     * @param viewFolderName String folder under channel
     * @param productTabName String tab name
     */
    public void gotochannelsProperties(String viewFolderName,
            String productTabName) {
        Actions action = new Actions(browserDriver);
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioChannelsNodeInstance.clkBtnPimChannelsNode(waitForReload);
        WebElement createdViewFolder = browserDriver
                .findElement(By.linkText(viewFolderName));
        action.doubleClick(createdViewFolder).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement tabName = browserDriver.findElement(By.xpath("//nobr[contains(text(),'Properties')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, tabName);
        tabName.click();
        WebElement selection = browserDriver
                .findElement(By.id("title_9fc2d28c_sections::Selection"));
        CSUtility.waitForVisibilityOfElement(waitForReload, selection);
        WebElement parent = (WebElement) ((JavascriptExecutor) browserDriver)
                .executeScript("return arguments[0].parentNode;", selection);
        if (parent.getAttribute("class").contains("closed")) {
            selection.click();
            CSLogger.info("Clicked on selection");
        } else {
            CSLogger.info("selection is opened");
        }
        WebElement selectByFavorite = browserDriver.findElement(
                By.xpath("//select[contains(@id,'PdmarticlestructureSelection"
                        + "CSBookmarkID')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, selectByFavorite);
        selectByFavorite.click();
        CSLogger.info("Reached to Channels Properties.");
    }

    /**
     * This method verify the favorite folder is present in show in favorite
     * dropdown.
     * 
     * @param favoriteFolderName String favorite name
     */
    public void verifyPublishFavorites(String favoriteFolderName) {
        int Options = browserDriver
                .findElements(By.xpath("//select[contains(@id,"
                        + "'PdmarticlestructureSelectionCSBookmarkID')]//option"))
                .size();
        ArrayList<String> allOptions = new ArrayList<>();
        for (int row = 1; row < Options; row++) {
            allOptions.add(browserDriver
                    .findElements(By.xpath("//select[contains(@id,"
                            + "'PdmarticlestructureSelectionCSBookmarkID')]//option"))
                    .get(row).getText());
        }
        if (allOptions.contains(favoriteFolderName)) {
            CSLogger.info("Publish Favorite Folder " + favoriteFolderName
                    + " is passsed");
        } else {
            Assert.fail(favoriteFolderName + " is present in the dropdown");
        }
    }

    /**
     * This method verify the favorite folder is not present in show in favorite
     * dropdown.
     * 
     * @param favoriteFolderName String favorite name
     */
    public void verifyCancelPublishFavorites(String favoriteFolderName) {
        int Options = browserDriver
                .findElements(By.xpath("//select[contains(@id,"
                        + "'PdmarticlestructureSelectionCSBookmarkID')]//option"))
                .size();
        ArrayList<String> allOptions = new ArrayList<>();
        for (int row = 1; row < Options; row++) {
            allOptions.add(browserDriver
                    .findElements(By.xpath("//select[contains(@id,"
                            + "'PdmarticlestructureSelectionCSBookmarkID')]//option"))
                    .get(row).getText());
        }
        if (allOptions.contains(favoriteFolderName)) {
            Assert.fail(favoriteFolderName + " is not present in the dropdown");
        } else {
            CSLogger.info("Cancel Publish Favorite Folder " + favoriteFolderName
                    + " is passsed");
        }
    }

    /**
     * This is a data provider method contains array of product name and
     * attributes
     * 
     * @return Array String method contains array of product name and attributes
     */
    @DataProvider(name = "publishFavoriteOptionTestData")
    public Object[][] getEditProductFolderData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                publishFavoriteOptionSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResource() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        pimStudioFavoritesNode = new PimStudioFavoritesNodePage(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        pimStudioChannelsNodeInstance = new PimStudioChannelsNode(
                browserDriver);
    }
}
