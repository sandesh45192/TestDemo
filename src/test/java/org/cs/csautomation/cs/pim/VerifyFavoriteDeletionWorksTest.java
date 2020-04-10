/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IFavoritesPopup;
import org.cs.csautomation.cs.pom.PimStudioFavoritesNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
public class VerifyFavoriteDeletionWorksTest extends AbstractTest {

    private WebDriverWait              waitForReload;
    private PimStudioFavoritesNodePage pimStudioFavoritesNode;
    private CSPortalHeader             csPortalHeader;
    private CSGuiToolbarHorizontal     csGuiToolbarHorizontalInstance;
    private String                     publishFavoriteOptionSheetName = "PublishFavoriteOption";

    /**
     * This test method delete the created favorite folder
     * 
     * @param favoriteFolderName
     *            String Object containing the favorite folder name
     * @param viewFolderName
     *            String Object containing name of the folder in channel
     * @param productTabName
     *            String object containing name of the tab of folder
     */
    @Test(dataProvider = "publishFavoriteOptionTestData")
    public void testDeletFavorite(String favoriteFolderName,
            String viewFolderName, String productTabName) {
        try {
            deletFavoriteFolder(favoriteFolderName, false);
            deletFavoriteFolder(favoriteFolderName, true);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testDeletFavorite", e);
            Assert.fail("Automation error in test method : testDeletFavorite",
                    e);
        }
    }

    /**
     * This method delete favorite folder from the favorite view
     * 
     * @param favoriteFolderName
     *            String favorite folder name
     * @param pressOkay
     *            Boolean button OK or cancel
     */
    public void deletFavoriteFolder(String favoriteFolderName,
            Boolean pressOkay) {
        Actions actions = new Actions(browserDriver);
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.scrollUpOrDownToElement(
                pimStudioFavoritesNode.getbtnPimFavoritesNode(), browserDriver);
        actions.doubleClick(pimStudioFavoritesNode.getbtnPimFavoritesNode())
                .perform();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement element = browserDriver
                .findElement(By.xpath("//iframe[@id='main']"));
        waitForReload.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnShowMoreOption());
        WebElement table = browserDriver
                .findElement(By.id("CSGuiListbuilderTable"));
        int productCount = table.findElements(By.tagName("tr")).size();
        WebElement favoriteFolder = null;
        String folder = null;
        for (int row = 3; row < productCount; row++) {
            favoriteFolder = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + row + "]/td[2]"));
            folder = favoriteFolder.getText();
            if (folder.equals(favoriteFolderName)) {
                CSUtility.rightClickTreeNode(waitForReload, favoriteFolder,
                        browserDriver);
                IFavoritesPopup favoritePopup = CSPopupDivPim
                        .getCSPopupDivLocators(browserDriver);
                favoritePopup.hoverOnCsGuiPopupMenu(browserDriver,
                        waitForReload, favoritePopup.getCsGuiPopupMenuObject());
                favoritePopup.selectPopupDivSubMenu(waitForReload,
                        favoritePopup.getDeleteMenu(), browserDriver);
                CSUtility.tempMethodForThreadSleep(1000);
                Alert alertBox = CSUtility.getAlertBox(waitForReload,
                        browserDriver);
                if (pressOkay) {
                    alertBox.accept();
                    CSLogger.info("Click on OK to alert.");
                    verifyDeleteFavoriteFolder(favoriteFolderName, pressOkay);
                } else {
                    alertBox.dismiss();
                    CSLogger.info("Click on Cancel to alert.");
                    verifyDeleteFavoriteFolder(favoriteFolderName, pressOkay);
                }
                break;
            }
        }
    }

    /**
     * This method verifies the delete test case is successful
     * 
     * @param favoriteFolderName
     *            String favorite folder name
     * @param pressOkay
     *            Boolean button OK or cancel
     */
    public void verifyDeleteFavoriteFolder(String favoriteFolderName,
            Boolean pressOkay) {
        CSLogger.info("Verifing the Delete Favorite Test.");
        IFavoritesPopup FavoritePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioFavoritesNode.rightclkBtnPimFavoritesNode(waitForReload);
        FavoritePopup.selectPopupDivMenu(waitForReload,
                FavoritePopup.getcsGuiPopupMenuRefresh(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        List<WebElement> favoriteFolder = browserDriver
                .findElements(By.linkText(favoriteFolderName));
        if (pressOkay) {
            if (favoriteFolder.isEmpty()) {
                CSLogger.info("Delete Favorite is passed");
            } else {
                Assert.fail("Folder is present in Favorite node.");
            }
        } else {
            if (!favoriteFolder.isEmpty()) {
                CSLogger.info("Delete Favorite Cancel is passed");
            } else {
                Assert.fail("Folder is not present in Favorite node.");
            }
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
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
    }
}
