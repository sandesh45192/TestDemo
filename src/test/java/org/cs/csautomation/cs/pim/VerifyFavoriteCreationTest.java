/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IFavoritesPopup;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.PimStudioFavoritesNodePage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to assign attributes to the newly
 * created class
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyFavoriteCreationTest extends AbstractTest {

    private WebDriverWait              waitForReload;
    private PimStudioProductsNodePage  pimStudioProductsNode;
    private PimStudioFavoritesNodePage pimStudioFavoritesNode;
    private CSPortalHeader             csPortalHeader;
    private CSGuiToolbarHorizontal     csGuiToolbarHorizontalInstance;
    private String                     verifyFavoriteCreationSheet = "VerifyFavoriteCreation";

    /**
     * This test method creates a favorite folder during runtime and performs
     * searching via a label and create a favorite And also verify the result.
     * 
     * @param productFolderName
     *            String Object containing the product name
     * @param scearchLabel
     *            String Object containing name of the label to be search
     * @param favoriteLabelName
     *            String object containing name of the folder of favorite
     * @param verifyLabels
     *            String Object containing the child product name which is use
     *            for verification.
     */
    @Test(dataProvider = "FavoriteCreationData")
    public void testVerifyFavoriteCreation(String productFolderName,
            String scearchLabel, String favoriteLabelName,
            String verifyLabels) {
        try {
            searchLabel(productFolderName, scearchLabel, favoriteLabelName,
                    false);
            searchLabel(productFolderName, scearchLabel, favoriteLabelName,
                    true);
            verifyFavoriteTestcase(favoriteLabelName, verifyLabels);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testVerifyFavoriteCreation ",
                    e);
            Assert.fail(
                    "Automation error in test method : testVerifyFavoriteCreation ",
                    e);
        }
    }

    /**
     * This method perform the search operation
     * 
     * @param productFolderName
     *            String
     * @param scearchLabel
     *            String
     * @param favoriteLabelName
     *            String
     * @param pressOkay
     *            Boolean
     */
    public void searchLabel(String productFolderName, String scearchLabel,
            String favoriteLabelName, Boolean pressOkay) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(productFolderName));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                createdProductFolder);
        Actions performAction = new Actions(browserDriver);
        CSUtility.scrollUpOrDownToElement(createdProductFolder, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        performAction.doubleClick(createdProductFolder).perform();
        createdProductFolder.click();
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.enterTextInSearchBox(waitForReload,
                scearchLabel);
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Scearch by label " + scearchLabel + " is passsed");
        createFavorite(favoriteLabelName, pressOkay);
    }

    /**
     * This method perform creation of favorite folder.
     * 
     * @param favoriteLabelName
     *            String
     * @param pressOkay
     *            Boolean
     */
    public void createFavorite(String favoriteLabelName, Boolean pressOkay) {
        handlefavoritePopups();
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        WebElement labelFavorite = browserDriver.findElement(
                By.xpath("//input[contains(@id,'RecordBookmarkName')]"));
        waitForReload
                .until(ExpectedConditions.elementToBeClickable(labelFavorite));
        labelFavorite.sendKeys(favoriteLabelName);
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement bthShowInTree = browserDriver.findElement(
                By.xpath("//span[contains(@id,'RecordBookmarkShowInTree')]"));
        if (bthShowInTree.isEnabled()) {
            CSLogger.info("Button Show In Tree is Enable.");
        } else {
            Assert.fail("Button Show In Tree is not Enable.");
        }
        WebElement bthShowInMenu = browserDriver.findElement(
                By.xpath("//span[contains(@id,'RecordBookmarkShowInMenu')]"));
        if (bthShowInMenu.isEnabled()) {
            bthShowInMenu.click();
        } else {
            Assert.fail("Button Show In Menu is already Enabled.");
        }
        if (pressOkay) {
            browserDriver
                    .findElement(By
                            .xpath("//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_OK']"))
                    .click();
            verifyCreationOfFavoriteFolder(favoriteLabelName, pressOkay);
        } else {
            browserDriver
                    .findElement(By
                            .xpath("//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL']"))
                    .click();
            verifyCreationOfFavoriteFolder(favoriteLabelName, pressOkay);
        }
    }

    /**
     * This method handle the Popups related to the favorite creation.
     * 
     */
    public void handlefavoritePopups() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        IMoreOptionPopup moreOptionPopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnShowMoreOption());
        csGuiToolbarHorizontalInstance.getBtnShowMoreOption().click();
        CSLogger.info("Clicked on Show More Options");
        moreOptionPopup.selectPopupDivMenu(waitForReload,
                moreOptionPopup.getcsGuiPopupFavorites(), browserDriver);
        moreOptionPopup.selectPopupDivSubMenu(waitForReload,
                moreOptionPopup.getcsGuiPopupSubAddFavorites(), browserDriver);
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
    }

    /**
     * This method verifies the favorite creation test case.
     * 
     * @param favoriteLabelName
     *            String
     * @param verifyLabels
     *            String
     */
    public void verifyFavoriteTestcase(String FavoriteLabelName,
            String verifyLabels) {
        String labels[] = verifyLabels.split(",");
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioFavoritesNode.clkBtnPimFavoritesNode(waitForReload);
        pimStudioFavoritesNode.getbtnPimFavoritesNode().click();
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement createdFavorite = browserDriver
                .findElement(By.linkText(FavoriteLabelName));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdFavorite);
        createdFavorite.click();
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement table = browserDriver
                .findElement(By.id("CSGuiListbuilderTable"));
        CSUtility.waitForVisibilityOfElement(waitForReload, table);
        int lblCount = table.findElements(By.tagName("tr")).size();
        int index = 0;
        WebElement labelText = null;
        CSUtility.tempMethodForThreadSleep(1000);
        int labelHeaderIndex = CSUtility.getListHeaderIndex("Label",
                browserDriver);
        for (int row = 3; row <= lblCount; row++) {
            labelText = browserDriver.findElement(By.xpath(
                    "//table[@class='hidewrap CSAdminList']" + "/tbody[1]/tr["
                            + row + "]/td[" + labelHeaderIndex + "]"));
            if (labelText.getText().equals(labels[index])) {
                CSLogger.info(
                        "Verification is passed for label " + labels[index]);
                index += 1;
            } else {
                Assert.fail("Verification fail for label " + labels[index]);
            }
        }
    }

    /**
     * This method verifies the creation of favorite folder.
     * 
     * @param favoriteLabelName
     *            String
     * @param pressOkay
     *            Boolean
     */
    private void verifyCreationOfFavoriteFolder(String favoriteLabelName,
            Boolean pressOkay) {
        IFavoritesPopup FavoritePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        pimStudioFavoritesNode.rightclkBtnPimFavoritesNode(waitForReload);
        FavoritePopup.selectPopupDivMenu(waitForReload,
                FavoritePopup.getcsGuiPopupMenuRefresh(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        List<WebElement> favoriteFolder = browserDriver
                .findElements(By.linkText(favoriteLabelName));
        Assert.assertEquals(favoriteFolder.isEmpty(), !pressOkay);
        CSUtility.switchToDefaultFrame(browserDriver);
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of credentials
     */
    @DataProvider(name = "FavoriteCreationData")
    public Object[][] getLabelsTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                verifyFavoriteCreationSheet);
    }

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        pimStudioFavoritesNode = new PimStudioFavoritesNodePage(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        new Actions(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }
}
