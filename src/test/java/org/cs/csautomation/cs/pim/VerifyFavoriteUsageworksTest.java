/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioFavoritesNodePage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class VerifyFavoriteUsageworksTest extends AbstractTest {

    private WebDriverWait              waitForReload;
    private PimStudioProductsNodePage  pimStudioProductsNode;
    private PimStudioFavoritesNodePage pimStudioFavoritesNode;
    private CSPortalHeader             csPortalHeader;
    private String                     FavotireUsagesWorkSheetName = "FavotireUsagesWork";

    /**
     * This test method make changes in product folder and verifies the same
     * same changes should be done in created favorite folder.
     * 
     * @param productFolderName String Object containing the product folder name
     * @param childToBeRename String Object containing name of the child to be
     *            renamed
     * @param renameChild String object containing name of the new child
     * @param createNewChild String object containing name of the new child
     * @param favoriteLabelName String object containing name of the favorite
     *            folder
     * @param verifyRenameLabels String object containing names of the rename
     *            child
     * @param verifyAddedLabels String object containing names of the new child
     */
    @Test(dataProvider = "favoriteUsagesWorkData")
    public void testVerifyFavoriteUsageworks(String productFolderName,
            String childToBeRename, String renameChild, String createNewChild,
            String favoriteLabelName, String verifyRenameLabels,
            String verifyAddedLabels) {
        try {
            renameChild(productFolderName, childToBeRename, renameChild);
            verifyFavoriteTestcase(favoriteLabelName, verifyRenameLabels);
            createNewChild(productFolderName, createNewChild);
            verifyFavoriteTestcase(favoriteLabelName, verifyAddedLabels);
        } catch (Exception e) {
            CSLogger.error(
                    "Automation error in test method testVerifyFavoriteUsageworks: ",
                    e);
            Assert.fail(
                    "Automation error in test method testVerifyFavoriteUsageworks: ",
                    e);
        }
    }

    /**
     * This method rename the existing child
     * 
     * @param productFolderName String product folder name
     * @param childToBeRename String child old name
     * @param createNewChild String child new name
     */
    public void renameChild(String productFolderName, String childToBeRename,
            String renameChild) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productFolderName)));
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(productFolderName));
        CSUtility.scrollUpOrDownToElement(createdProductFolder, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        createdProductFolder.click();
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement createdchild = browserDriver
                .findElement(By.linkText(childToBeRename));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdchild);
        CSUtility.rightClickTreeNode(waitForReload, createdchild,
                browserDriver);
        IProductPopup productPopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuObject(), browserDriver);
        productPopup.selectPopupDivSubMenu(waitForReload,
                productPopup.getCsGuiPopupMenuSubMenuRename(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopup.enterValueInDialogue(waitForReload, renameChild);
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        verifyCreation(productFolderName, renameChild);
    }

    /**
     * This method create the new child
     * 
     * @param productFolderName String product folder name
     * @param createNewChild String new child name
     */
    public void createNewChild(String productFolderName,
            String createNewChild) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(productFolderName));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                createdProductFolder);
        CSUtility.rightClickTreeNode(waitForReload, createdProductFolder,
                browserDriver);
        IProductPopup productPopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuNewChild(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, createNewChild);
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        verifyCreation(productFolderName, createNewChild);
    }

    /**
     * This method verifies the changes in the product child verifies the
     * renamed child verifies newly created child
     * 
     * @param productFolderName String product folder name
     * @param createNewChild String new child name
     */
    public void verifyCreation(String productFolderName,
            String createNewChild) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(productFolderName));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                createdProductFolder);
        createdProductFolder.click();
        List<WebElement> productChilds = browserDriver
                .findElements(By.linkText(createNewChild));
        if (productChilds.isEmpty()) {
            Assert.fail("Child not found");
        } else {
            CSLogger.info("Found new Child");
        }
        CSUtility.switchToDefaultFrame(browserDriver);
    }

    /**
     * This method verified the changes should be present in created favorite
     * folder
     * 
     * @param favoriteLabelName String favorite folder name
     * @param verifyLabels String labels containing child names
     */
    public void verifyFavoriteTestcase(String favoriteLabelName,
            String verifyLabels) {
        String labels[] = verifyLabels.split(",");
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioFavoritesNode.clkBtnPimFavoritesNode(waitForReload);
        pimStudioFavoritesNode.getbtnPimFavoritesNode().click();
        WebElement createdFavorite = browserDriver
                .findElement(By.linkText(favoriteLabelName));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdFavorite);
        createdFavorite.click();
        CSUtility.tempMethodForThreadSleep(4000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement table = browserDriver
                .findElement(By.id("CSGuiListbuilderTable"));
        int lblCount = table.findElements(By.tagName("tr")).size();
        int index = 0;
        WebElement labelText = null;
        int labelHeaderIndex = CSUtility.getListHeaderIndex("Label",
                browserDriver);
        for (int row = 3; row <= lblCount; row++) {
            CSUtility.tempMethodForThreadSleep(3000);
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
     * This is a data provider method contains array of product name and
     * attributes
     * 
     * @return Array String method contains array of product name and attributes
     */
    @DataProvider(name = "favoriteUsagesWorkData")
    public Object[][] getEditProductFolderData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                FavotireUsagesWorkSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResource() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        pimStudioFavoritesNode = new PimStudioFavoritesNodePage(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
    }
}
