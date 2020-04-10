/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.dashboard;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.dashboard.DashboardPage;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraversingForDashboardModule;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This test class performs the deletion operation of tile on dashboard
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteTileTest extends AbstractTest {

    private CSPortalHeader csPortalHeader;
    private WebDriverWait  waitForReload;
    private String         deleteTileSheet = "DeleteTile";
    private DashboardPage  dashboardPage;

    /**
     * This method deletes the tile on the dashboard
     * 
     * @param delAdminTile contains tile name
     * @param delUserModeTile contains tile name
     * @param tileType contains type of tile
     * @param favoritesFolder contains name of favorites folder
     */
    @Test(priority = 1, dataProvider = "deleteTest")
    public void testDeleteTile(String delAdminTile, String delUserModeTile,
            String tileType, String favoritesFolder) {
        try {
            csPortalHeader.clkBtnHome(waitForReload);
            checkIfDashboardIsLocked();
            selectMode(dashboardPage.getBtnAdministrator());
            // add tile in admin mode
            addNewTile(tileType, delAdminTile, favoritesFolder);
            verifyPresenceOfDeleteOptionInAdminMode(delAdminTile);
            verifyPresenceOfDeleteOptionInUserMode(delAdminTile);
            // add tile in current user mode
            addNewTile(tileType, delUserModeTile, favoritesFolder);
            verifyPresenceOfDeleteOptionForUserModeTile(delUserModeTile);
            deleteTile(delUserModeTile, false);
            clkTileToBeDeleted(delUserModeTile);
            deleteTile(delUserModeTile, true);
            CSUtility.tempMethodForThreadSleep(2000);
            verifyDeletedTile(delUserModeTile);
        } catch (Exception e) {
            CSLogger.debug("Test case for deletion of tile failed", e);
            Assert.fail("Test case failed");
        }
    }

    /**
     * Checks if dashbaord is locked otherwise lock it
     */
    private void checkIfDashboardIsLocked() {
        int lockToggle = browserDriver
                .findElements(By.xpath("//button[@name='unlocktoggle']"))
                .size();
        if (lockToggle == 0) {
            lockDashboard();
            CSUtility.tempMethodForThreadSleep(2000);
        } else {
            CSLogger.info("Dashboard is already locked");
        }
    }

    /**
     * This method clicks on tile to be deleted
     * 
     * @param tileName contains name of tile
     */
    private void clkTileToBeDeleted(String tileName) {
        List<WebElement> list = getTile();
        boolean status = false;
        WebElement listElement = null;
        for (Iterator<WebElement> iterator = list.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(tileName)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSUtility.tempMethodForThreadSleep(2000);
            listElement.click();
        } else {
            CSLogger.error("Tile not found");
        }
    }

    /**
     * This method deletes the tile
     * 
     * @param delUserModeTile contains tile name
     */
    private void verifyDeletedTile(String delUserModeTile) {
        boolean status = false;
        List<WebElement> list = getTile();
        for (Iterator<WebElement> iterator = list.iterator(); iterator
                .hasNext();) {
            WebElement webElement = (WebElement) iterator.next();
            if (webElement.getText().equals(delUserModeTile)) {
                status = true;
                break;

            }
        }
        if (status == true) {
            CSLogger.error("Test case failed");
        } else {
            CSLogger.info("Test case passed");
        }
    }

    /**
     * This method deletes tile
     * 
     * @param tileName contains name of tile
     * @param status contains status true or false
     */
    private void deleteTile(String tileName, boolean status) {
        WebElement btnDelete = browserDriver
                .findElement(By.xpath("//div[contains(text(),'" + tileName
                        + "')]/../../div/button[@name='DeleteButton']//span[@class='button-icon left']//span"));
        waitForReload.until(ExpectedConditions.visibilityOf(btnDelete));
        btnDelete.click();
        CSUtility.tempMethodForThreadSleep(2000);
        if (status == false) {
            dashboardPage.clkElement(waitForReload,
                    dashboardPage.getBtnCancelOfPopup());
        } else {
            if (status == true) {
                dashboardPage.clkElement(waitForReload,
                        dashboardPage.getBtnOkOfPopup());
            }
        }
    }

    /**
     * This method verifies the presence of delete option for user mode tile
     * 
     * @param tileName contains name of tile
     */
    private void verifyPresenceOfDeleteOptionForUserModeTile(String tileName) {
        int btnCount = getCount(tileName);
        if (btnCount == 1) {
            CSLogger.info("Verified.Delete button is present in user mode ");
        } else {
            CSLogger.error("Delete button is not present");
        }
    }

    /**
     * This method gets count of delete button on tile
     * 
     * @param tileName contains name of tile
     * @return btnCount
     */
    private int getCount(String tileName) {
        int btnCount = 0;
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> list = getTile();
        for (Iterator<WebElement> iterator = list.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(tileName)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            listElement.click();
            CSUtility.tempMethodForThreadSleep(2000);
            btnCount = browserDriver
                    .findElements(By.xpath("//div[contains(text(),'" + tileName
                            + "')]/../../div/button[@name='DeleteButton']//span[@class='button-icon left']//span"))
                    .size();
        }
        return btnCount;
    }

    /**
     * This method verifies the presence of delete option in user mode
     * 
     * @param tileName contains name of tile
     */
    private void verifyPresenceOfDeleteOptionInUserMode(String tileName) {
        lockDashboard();
        CSUtility.tempMethodForThreadSleep(2000);
        selectMode(dashboardPage.getBtnCurrentUser());
        int btnCount = getCount(tileName);
        if (btnCount == 0) {
            CSLogger.info("Verified. Admin tile is not deletable in user mode");
        } else {
            CSLogger.error(
                    "Delete button of admin tile should not present in the current user mode ");
        }
    }

    /**
     * This method unlocks the dashboard from the home page
     */
    private void lockDashboard() {
        TraversingForDashboardModule.traverseToDashboardPage(waitForReload,
                browserDriver);
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getBtnLockToggle());
        CSLogger.info("Cliced on Lock toggle button");
    }

    /**
     * This method verifies the presence of delete option in admin mode
     * 
     * @param tileName contains tile name
     */
    private void verifyPresenceOfDeleteOptionInAdminMode(String tileName) {
        int btnCount = getCount(tileName);
        if (btnCount == 1) {
            CSLogger.info(
                    "Verified.Delete button is present on tile in admin mode");
        } else {
            CSLogger.error("Delete button is not present");
        }
    }

    /**
     * This method gets tiles from the dashboard
     * 
     * @return list
     */
    private List<WebElement> getTile() {
        List<WebElement> list = browserDriver
                .findElements(By.xpath("//div[@class='dashboardTileTitle']"));
        return list;
    }

    /**
     * This method adds new tile
     * 
     * @param tileType contains tile type
     * @param tileName contains tile name
     * @param favoritesFolder contains favorties folder
     */
    private void addNewTile(String tileType, String tileName,
            String favoritesFolder) {
        try {
            dashboardPage.clkElement(waitForReload, dashboardPage.getBtnPlus());
            configureNewTile(tileType, tileName, favoritesFolder);
            CSUtility.tempMethodForThreadSleep(2000);
        } catch (Exception e) {
            CSLogger.error("Could not add tile", e);
            Assert.fail("Failed to add tile", e);
        }
    }

    /**
     * This method contains all required methods to configure new tile
     * 
     * @param tileType String contains type of tile
     * @param tileName string contains name of tile
     * @param favoritesFolder contains name of favorites folder
     */
    private void configureNewTile(String tileType, String tileName,
            String favoritesFolder) {
        dashboardPage.clkElement(waitForReload, dashboardPage.getTabNewTile());
        CSLogger.info("Clicked on new tile tab");
        CSUtility.tempMethodForThreadSleep(3000);
        selectTileType(tileType);
        enterDetails(tileName, dashboardPage.getTxtTileCaption());
        selectFavoritesFolder(favoritesFolder);
    }

    /**
     * This method selects the type of tile
     * 
     * @param tileType string contains tile type
     */
    private void selectTileType(String tileType) {
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> list = browserDriver
                .findElements(By.xpath("//ul[@class='CSListView']/li"));
        for (Iterator<WebElement> iterator = list.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(tileType)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            listElement.click();
            CSLogger.info("Clicked on tile type");
            clkInsertAndConfigure();
        } else {
            CSLogger.error("Could not find the required tile type");
        }
    }

    /**
     * This method selects the favorites folder
     * 
     * @param favoritesFolder String contains name of favorites folder
     */
    private void selectFavoritesFolder(String favoritesFolder) {
        enterDetails(favoritesFolder, dashboardPage.getTxtSelectFavorites());
        Robot robot = null;
        try {
            robot = new Robot();
            CSUtility.tempMethodForThreadSleep(1000);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            clkOk();
        } catch (Exception e) {
            CSLogger.error("Failed to initialize Robot class");
        }
    }

    /**
     * This method clicks on ok button
     */
    private void clkOk() {
        JavascriptExecutor executorObject = (JavascriptExecutor) browserDriver;
        waitForReload.until(
                ExpectedConditions.visibilityOf(dashboardPage.getBtnOk()));
        executorObject.executeScript("arguments[0].click()",
                dashboardPage.getBtnOk());
        CSLogger.info("Clicked on Ok button");
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method enters details in the textbox
     * 
     * @param textName contains text to be set in textbox
     * @param element contains web element of textbox
     */
    private void enterDetails(String textName, WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        if (!element.equals(dashboardPage.getTxtSelectFavorites())) {
            element.clear();
        }
        element.sendKeys(textName);
        CSLogger.info("Entered the name  " + textName);
    }

    /**
     * This method clicks on insert and configure button
     */
    private void clkInsertAndConfigure() {
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getBtnInsertAndConfigure());
        CSLogger.info("Clicked on insert and configure button");
    }

    /**
     * This method selects the Administrator mode after unlocking the dashboard
     */
    private void selectMode(WebElement mode) {
        unlockDashboard();
        dashboardPage.clkElement(waitForReload, mode);
        CSLogger.info("Switched to the mode");
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method unlocks the dashboard from the home page
     */
    private void unlockDashboard() {
        TraversingForDashboardModule.traverseToDashboardPage(waitForReload,
                browserDriver);
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getBtnUnlockToggle());
        CSLogger.info("Clicked on Lock toggle button");
    }

    /**
     * Returns the sheet whcih contains tile name to be created in admin mode,
     * tile to be created in current user mode,tile type and favorites folder
     * name
     * 
     * @return
     */
    @DataProvider(name = "deleteTest")
    public Object[][] deleTileTest() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("dashboardModuleTestCases"),
                deleteTileSheet);
    }

    /**
     * This method is used to initialize all the resources used to drive the
     * test case
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        dashboardPage = new DashboardPage(browserDriver);
    }
}
