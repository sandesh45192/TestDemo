/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.dashboard;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.google.common.collect.ObjectArrays;

/**
 * This class adds ,edits,filters,and sorts tiles on the dashboard
 * 
 * @author CSAutomation Team
 *
 */
public class AddTileTest extends AbstractTest {

    private CSPortalHeader csPortalHeader;
    private WebDriverWait  waitForReload;
    private String         addTileSheet = "AddTile";
    private DashboardPage  dashboardPage;
    private Actions        actions;
    private SoftAssert     softAssert;

    /**
     * This method adds tile on the dashboard
     * 
     * @param tileName contains name of tile
     * @param tileType contains type of tile
     * @param favoritesFolder contains name of favorites folder
     * @param editTileName contains name of tile to be edited
     * @param duplicateTileName contains name of tile to be duplicated
     * @param multipleTilesAdminMode contains names of tiles in admin mode
     * @param multipleTilesUserMode contains name of tiles in user mode
     * @param offset contains offset values
     * @param workflowTileName contains workflow tile name
     * @param filterTilesNames contains names of tiles
     * @param tileCategoryNames contains names of catgory of tile
     */
    @Test(priority = 1, dataProvider = "addTileTest")
    public void testAddTile(String tileName, String tileType,
            String favoritesFolder, String editTileName,
            String duplicateTileName, String multipleTilesAdminMode,
            String multipleTilesUserMode, String offset,
            String workflowTileName, String filterTilesNames,
            String tileCategoryNames) {
        try {
            csPortalHeader.clkBtnHome(waitForReload);
            selectMode(dashboardPage.getBtnAdministrator());
            addNewTile(tileType, tileName, favoritesFolder);
            verifyConfiguredTile(tileName);
        } catch (Exception e) {
            CSLogger.debug("Failed to add and verify tile on the dashboard", e);
            Assert.fail("Failed to add and verify tile on the dashboard", e);
        }
    }

    /**
     * This method verifies the added tile on the dashboard
     * 
     * @param tileName contains name of tile
     */
    private void verifyConfiguredTile(String tileName) {
        boolean status = false;
        status = getStatusOfTile(status, tileName);
        if (status == true) {
            CSLogger.info("Configured tile is present");
        } else {
            CSLogger.error("Failed to create and verify tile");
        }
    }

    /**
     * This method gets status of tile i.e if tile is present on dashboard or
     * not
     * 
     * @paaram status contains initial boolean value
     * @param tileName contains name of tile
     * @return status
     */
    private boolean getStatusOfTile(boolean status, String tileName) {
        List<WebElement> list = getTile();
        if (list.size() >= 1) {
            for (Iterator<WebElement> iterator = list.iterator(); iterator
                    .hasNext();) {
                WebElement listElement = (WebElement) iterator.next();
                if (listElement.getText().equals(tileName)) {
                    status = true;
                    break;
                }
            }
            if (status == true) {
                CSLogger.info(tileName + "  is present");
            } else {
                CSLogger.error(tileName + "  is not present ");
            }
        } else {
            CSLogger.error("Tiles are absent");
        }
        return status;
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
     * @param tileType String contains type of tile
     * @param tileName string contains name of tile
     * @param favoritesFolder contains name of favorites folder
     */
    private void addNewTile(String tileType, String tileName,
            String favoritesFolder) {
        try {
            dashboardPage.clkElement(waitForReload, dashboardPage.getBtnPlus());
            CSUtility.tempMethodForThreadSleep(5000);
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
     * This method eits and verifies the edited tile on the dashboard
     * 
     * @param tileName contains name of tile
     * @param tileType contains type of tile
     * @param favoritesFolder contains name of favorites folder
     * @param editTileName contains name of tile to be edited
     * @param duplicateTileName contains name of tile to be duplicated
     * @param multipleTilesAdminMode contains names of tiles in admin mode
     * @param multipleTilesUserMode contains name of tiles in user mode
     * @param offset contains offset values
     * @param workflowTileName contains workflow tile name
     * @param filterTilesNames contains names of tiles
     * @param tileCategoryNames contains names of catgory of tile
     */
    @Test(
            priority = 2,
            dependsOnMethods = { "testAddTile" },
            dataProvider = "addTileTest")
    public void testEditTile(String tileName, String tileType,
            String favoritesFolder, String editTileName,
            String duplicateTileName, String multipleTiles,
            String multipleTilesUserMode, String offset,
            String workflowTileName, String filterTileNames,
            String tileCategoryNames) {
        try {
            clkButtonInTile(tileName, dashboardPage.getBtnSettingsTile());
            verifyClickabilityOfTypeAndViewField();
            performEditOperation(editTileName);
            verifyConfiguredTile(editTileName);
            CSUtility.tempMethodForThreadSleep(2000);
            duplicateTile(editTileName, duplicateTileName);
            CSUtility.tempMethodForThreadSleep(2000);
            verifyTilesInCurrentUserMode(dashboardPage.getBtnCurrentUser(),
                    editTileName, duplicateTileName);
        } catch (Exception e) {
            CSLogger.debug("Failed to edit tile on the dashboard", e);
            Assert.fail("Failed to edit tile on the dashboard", e);
        }
    }

    /**
     * This method verifies tiles in current user mode
     * 
     * @param mode contains name of mode
     * @param editTileName contains name of edited tile name
     * @param duplicateTileName contains name of duplicate tile name
     */
    private void verifyTilesInCurrentUserMode(WebElement mode,
            String editTileName, String duplicateTileName) {
        switchTheMode(mode);
        getStatusOfTile(false, editTileName);
        getStatusOfTile(false, duplicateTileName);
    }

    /**
     * This method switches to current user mode
     * 
     * @param mode contains name of mode to be switched too
     */
    private void switchTheMode(WebElement mode) {
        lockDashboard();
        CSUtility.tempMethodForThreadSleep(3000);
        unlockDashboard();
        dashboardPage.clkElement(waitForReload, mode);
        CSLogger.info("Clicked on mode");
    }

    /**
     * This method duplicates tile
     * 
     * @param editTileName name of tile to be duplicated
     * @param duplicateTileName duplicate tile name
     */
    private void duplicateTile(String editTileName, String duplicateTileName) {
        clkButtonInTile(editTileName, dashboardPage.getBtnDuplicateTile());
        verifyClickabilityOfTypeAndViewField();
        performEditOperation(duplicateTileName);
        verifyConfiguredTile(duplicateTileName);
    }

    /**
     * This method performs the edit operation
     * 
     * @param editTileName contains edited file name
     */
    private void performEditOperation(String editTileName) {
        enterDetails(editTileName, dashboardPage.getTxtTileCaption());
        clkOk();
    }

    /**
     * This method verifies clickability of type and view field
     */
    private void verifyClickabilityOfTypeAndViewField() {
        selectDrpDwnElement(dashboardPage.getDrpDwnType());
        selectDrpDwnElement(dashboardPage.getDrpDwnView());
    }

    /**
     * This method selects drop down element
     * 
     * @param selectElement contains element to be selected
     */
    private void selectDrpDwnElement(WebElement selectElement) {
        Select select = new Select(selectElement);
        selectElement.click();
        List<WebElement> allOptions = select.getOptions();
        if (allOptions.size() >= 1) {
            CSLogger.info("The selected element is clickable");
        } else {
            CSLogger.error("Element is not clickable");
        }
    }

    /**
     * This method clicks the button present on the tile
     * 
     * @param tileName contains name of tile
     * @param selectButton contains select button
     */
    private void clkButtonInTile(String tileName, WebElement selectButton) {
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
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.waitForVisibilityOfElement(waitForReload, selectButton);
            actions.moveToElement(selectButton).click().build().perform();
            CSLogger.info("Clicked on settings button on tile");
        }
    }

    /**
     * This method verifies sorting of tiles in the admin node
     * 
     * @param tileName contains name of tile
     * @param tileType contains type of tile
     * @param favoritesFolder contains name of favorites folder
     * @param editTileName contains name of tile to be edited
     * @param duplicateTileName contains name of tile to be duplicated
     * @param multipleTilesAdminMode contains names of tiles in admin mode
     * @param multipleTilesUserMode contains name of tiles in user mode
     * @param offset contains offset values
     * @param workflowTileName contains workflow tile name
     * @param filterTilesNames contains names of tiles
     * @param tileCategoryNames contains names of catgory of tile
     */
    @Test(
            priority = 3,
            dependsOnMethods = { "testEditTile" },
            dataProvider = "addTileTest")
    public void testVerifySortingOfTilesAdminMode(String tileName,
            String tileType, String favoritesFolder, String editTileName,
            String duplicateTileName, String multipleTilesAdminMode,
            String multipleTilesUserMode, String offset,
            String workflowTileName, String filterTileNames,
            String tileCategoryNames) {
        try {
            String[] tilesAdminMode = multipleTilesAdminMode.split(",");
            String[] tilesUserMode = multipleTilesUserMode.split(",");
            String[] combineAdminAndUserModeTiles = ObjectArrays
                    .concat(tilesAdminMode, tilesUserMode, String.class);
            CSUtility.tempMethodForThreadSleep(2000);
            lockDashboard();
            CSUtility.tempMethodForThreadSleep(3000);
            createMultipleTilesInAdminMode(tileType, favoritesFolder,
                    tilesAdminMode);
            lockDashboard();
            CSUtility.tempMethodForThreadSleep(3000);
            createMultipleTilesCurrentUserMode(tileType, favoritesFolder,
                    tilesUserMode);
            verifyPresenceOfAllTilesInUserMode(combineAdminAndUserModeTiles);
        } catch (Exception e) {
            CSLogger.debug("Failed to verify sorting of tile on the dashboard",
                    e);
            Assert.fail("Failed to verify sorting of tile on the dashboard", e);
        }
    }

    /**
     * This method verifies presence of all the tiles in user mode
     * 
     * @param combineAdminAndUserModeTiles contains elements of user mode and
     *            admin mode tiles
     */
    private void verifyPresenceOfAllTilesInUserMode(
            String[] combineAdminAndUserModeTiles) {
        boolean status = false;
        for (int tileIndex = 0; tileIndex < combineAdminAndUserModeTiles.length; tileIndex++) {
            getStatusOfTile(status, combineAdminAndUserModeTiles[tileIndex]);
        }
    }

    /**
     * This method creates multiple tiles in the current user mode
     * 
     * @param tileType contains name of tile type
     * @param favoritesFolder contains name of favorites folder
     * @param tilesUserMode contains names of tiles in current user mode
     */
    private void createMultipleTilesCurrentUserMode(String tileType,
            String favoritesFolder, String[] tilesUserMode) {
        selectMode(dashboardPage.getBtnCurrentUser());
        for (int tileIndex = 0; tileIndex < tilesUserMode.length; tileIndex++) {
            addNewTile(tileType, tilesUserMode[tileIndex], favoritesFolder);
            verifySortingOrder(tilesUserMode[tileIndex]);
        }
        getSequenceOfUserModeTiles();
    }

    private List<WebElement> getSequenceOfUserModeTiles() {
        List<WebElement> list = getTile();
        return list;
    }

    /**
     * This method creates multiple tiles in the admin mode
     * 
     * @param tileType contains name of tile type
     * @param favoritesFolder contains name of favorites folder
     * @param tilesUserMode contains names of tiles in current user mode
     */
    private void createMultipleTilesInAdminMode(String tileType,
            String favoritesFolder, String[] tiles) {
        selectMode(dashboardPage.getBtnAdministrator());
        for (int tileIndex = 0; tileIndex < tiles.length; tileIndex++) {
            addNewTile(tileType, tiles[tileIndex], favoritesFolder);
            verifySortingOrder(tiles[tileIndex]);
        }
        getSequenceOfAdminModeTiles();
    }

    /**
     * This method gets the sequence of admin mode tiles
     * 
     * @return list
     */
    private List<WebElement> getSequenceOfAdminModeTiles() {
        List<WebElement> list = getTile();
        return list;
    }

    /**
     * This method verifies the sorting order fo tiles
     * 
     * @param tileName contains name of tile
     */
    private void verifySortingOrder(String tileName) {
        WebElement getFirstLocation = browserDriver.findElement(
                By.xpath("(//div[@class='dashboardTileTitle']/div)[1]"));
        if (getFirstLocation.getText().equals(tileName)) {
            CSLogger.info("Firstly created tile  " + tileName
                    + " is present at first location");
        } else {
            CSLogger.error("Tile is not present at first location");

        }
    }

    /**
     * This method verifies sorting of tiles in the current user mode
     * 
     * @param tileName contains name of tile
     * @param tileType contains type of tile
     * @param favoritesFolder contains name of favorites folder
     * @param editTileName contains name of tile to be edited
     * @param duplicateTileName contains name of tile to be duplicated
     * @param multipleTilesAdminMode contains names of tiles in admin mode
     * @param multipleTilesUserMode contains name of tiles in user mode
     * @param offset contains offset values
     * @param workflowTileName contains workflow tile name
     * @param filterTilesNames contains names of tiles
     * @param tileCategoryNames contains names of catgory of tile
     */
    @Test(
            priority = 4,
            dependsOnMethods = { "testVerifySortingOfTilesAdminMode" },
            dataProvider = "addTileTest")
    public void testVerifySortingOfTilesCurrentUserMode(String tileName,
            String tileType, String favoritesFolder, String editTileName,
            String duplicateTileName, String multipleTilesAdminMode,
            String multipleTilesUserMode, String offset,
            String workflowTileName, String filterTileNames,
            String tileCategoryNames) {
        try {
            String[] tilesAdminMode = multipleTilesAdminMode.split(",");
            String[] tilesUserMode = multipleTilesUserMode.split(",");
            ArrayList<String> allTiles = new ArrayList<String>(
                    Arrays.asList(tilesUserMode));
            allTiles.addAll((Arrays.asList(tilesAdminMode)));
            allTiles.add(duplicateTileName);
            allTiles.add(editTileName);
            String updatedPositionTile = changePositionOfTileInUserMode(
                    multipleTilesUserMode, offset);
            CSUtility.tempMethodForThreadSleep(2000);
            String[] arrayOfTilesAfterPositionChange = getValues(allTiles);

            changeHeightOfTileInUserModeOnce(updatedPositionTile,
                    dashboardPage.getBtnCurrentUser());
            verifyHeightChange(updatedPositionTile);
            String[] arrayOfTilesAfterHeightChange = getValues(allTiles);
            boolean chkSortingOrder = Arrays.equals(
                    arrayOfTilesAfterPositionChange,
                    arrayOfTilesAfterHeightChange);
            if (chkSortingOrder) {
                CSLogger.info("Sequence is same");
            } else {
                CSLogger.error("sequences are different");
            }
        } catch (Exception e) {
            CSLogger.debug("Failed to add and verify tile on the dashboard", e);
            Assert.fail("Failed to add and verify tile on the dashboard", e);
        }
    }

    /**
     * This method returns the string array of list values which gets input of
     * list of tiles
     * 
     * @param allTiles contains tiles
     * @return listValues
     */
    private String[] getValues(List<String> allTiles) {
        List<WebElement> list = getTile();
        CSUtility.tempMethodForThreadSleep(2000);
        String[] listValues = new String[list.size()];
        for (int listIndex = 0; listIndex < allTiles.size(); listIndex++) {
            WebElement element = list.get(listIndex);
            listValues[listIndex] = element.getText();
        }
        return listValues;
    }

    /**
     * This method verifies height change of tile
     * 
     * @param tile1 contains tile name
     */
    private void verifyHeightChange(String tile1) {
        WebElement tileName = browserDriver.findElement(By.xpath(
                "//div[@class='dashboardTileTitle']/div[contains(text(),'"
                        + tile1 + "')]/../../../.."));
        waitForReload.until(ExpectedConditions.visibilityOf(tileName));
        String getClassValue = tileName.getAttribute("class");
        if (getClassValue.contains("dashboardTileWrapper1x2")) {
            CSLogger.info("Height has increased once");
            CSUtility.tempMethodForThreadSleep(2000);
        } else {
            CSLogger.error("Unable to increase height");
            softAssert.fail("Failed to increase and verify height");
        }
    }

    /**
     * This method changes height of tile in the user mode once
     * 
     * @param tile1 contains name of tile
     * @param mode contains mode
     */
    private void changeHeightOfTileInUserModeOnce(String tile1,
            WebElement mode) {
        switchTheMode(dashboardPage.getBtnCurrentUser());
        WebElement selectButton = browserDriver
                .findElement(By.xpath("//div[contains(text(),'" + tile1
                        + "')]/../../../div[3]/div/button[@name='GrowHeight']"));
        clkButtonInTile(tile1, selectButton);
    }

    /**
     * This method changes position of tile
     * 
     * @param multipleTilesUserMode contains tiles in user mode
     * @param offset contains offset values
     * @return tile
     */
    private String changePositionOfTileInUserMode(String multipleTilesUserMode,
            String offset) {
        String[] offsetValue = offset.split("&");
        String xOffset = offsetValue[0];
        String yOffset = offsetValue[1];
        String[] userModeTiles = multipleTilesUserMode.split(",");
        String tile1 = userModeTiles[0];
        String tile2 = userModeTiles[1];
        WebElement sourceTile = getTileName(tile2);
        WebElement targetTile = getTileName(tile1);
        sourceTile.click();
        actions.clickAndHold(sourceTile).build().perform();
        actions.moveToElement(targetTile, Integer.parseInt(yOffset),
                Integer.parseInt(xOffset)).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        actions.release().build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        verifySortingOrder(tile1);
        CSLogger.info("Target tile has been assigned to the required position");
        return tile1;
    }

    /**
     * This method gets tile name from the list
     * 
     * @param tileName contains name of tile
     * @return listElement
     */
    private WebElement getTileName(String tileName) {
        List<WebElement> list = getTile();
        WebElement listElement = null;
        for (Iterator<WebElement> iterator = list.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(tileName)) {
                break;
            }
        }
        return listElement;
    }

    /**
     * This method verifies the loading of tiles in the current user modes
     * 
     * @param tileName contains name of tile
     * @param tileType contains type of tile
     * @param favoritesFolder contains name of favorites folder
     * @param editTileName contains name of tile to be edited
     * @param duplicateTileName contains name of tile to be duplicated
     * @param multipleTilesAdminMode contains names of tiles in admin mode
     * @param multipleTilesUserMode contains name of tiles in user mode
     * @param offset contains offset values
     * @param workflowTileName contains workflow tile name
     * @param filterTilesNames contains names of tiles
     * @param tileCategoryNames contains names of catgory of tile
     */
    @Test(
            priority = 5,
            dependsOnMethods = { "testVerifySortingOfTilesCurrentUserMode" },
            dataProvider = "addTileTest")
    public void testVerifyLoadingOfTilesCurrentUserMode(String tileName,
            String tileType, String favoritesFolder, String editTileName,
            String duplicateTileName, String multipleTilesAdminMode,
            String multipleTilesUserMode, String offset,
            String workflowTileName, String filterTileNames,
            String tileCategoryNames) {
        try {
            switchTheMode(dashboardPage.getBtnAdministrator());
            CSUtility.tempMethodForThreadSleep(2000);
            addNewTile(tileType, workflowTileName, favoritesFolder);
            verifySortingOrder(workflowTileName);
            verifyPresenceOfTileInUserMode();
        } catch (Exception e) {
            CSLogger.debug(
                    "Failed to add and verify loading of tile on the dashboard",
                    e);
            Assert.fail(
                    "Failed to add and verify loading of tile on the dashboard",
                    e);
        }
    }

    /**
     * This method verifies the presence of tile in user mode
     */
    private void verifyPresenceOfTileInUserMode() {
        List<WebElement> adminModeList = getTile();
        List<String> adminModeListString = getStringValues(adminModeList);
        switchTheMode(dashboardPage.getBtnCurrentUser());
        List<WebElement> userModeList = getTile();
        List<String> userModeListString = getStringValues(userModeList);
        boolean chkPresence = userModeListString
                .containsAll(adminModeListString);
        if (chkPresence) {
            CSLogger.info("All admin tiles are peresent in the user mode");
        } else {
            CSLogger.error("Admin tiles are not present in current user mode ");
            softAssert.fail("Verification for loading of tiles failed");
        }
    }

    /**
     * This method gets the string values of list of webelement
     * 
     * @param adminModeList contains list to be converted to string values
     * @return adminModeListString
     */
    private List<String> getStringValues(List<WebElement> adminModeList) {
        List<String> adminModeListString = new ArrayList<String>();
        for (int tileIndex = 0; tileIndex < adminModeList.size(); tileIndex++) {
            WebElement tileName = adminModeList.get(tileIndex);
            adminModeListString.add(tileName.getText());
        }
        return adminModeListString;
    }

    /**
     * This method verifies the filetering of tiles
     * 
     * @param tileName contains name of tile
     * @param tileType contains type of tile
     * @param favoritesFolder contains name of favorites folder
     * @param editTileName contains name of tile to be edited
     * @param duplicateTileName contains name of tile to be duplicated
     * @param multipleTilesAdminMode contains names of tiles in admin mode
     * @param multipleTilesUserMode contains name of tiles in user mode
     * @param offset contains offset values
     * @param workflowTileName contains workflow tile name
     * @param filterTilesNames contains names of tiles
     * @param tileCategoryNames contains names of catgory of tile
     */
    @Test(priority = 6, dataProvider = "addTileTest")
    public void testVerifyFilteringOfTiles(String tileName, String tileType,
            String favoritesFolder, String editTileName,
            String duplicateTileName, String multipleTilesAdminMode,
            String multipleTilesUserMode, String offset,
            String workflowTileName, String filterTileNames,
            String tileCategoryNames) {
        String[] tilesAdminMode = multipleTilesAdminMode.split(",");
        String[] filterTiles = filterTileNames.split(",");
        try {
            switchTheMode(dashboardPage.getBtnAdministrator());
            CSUtility.tempMethodForThreadSleep(2000);

            String[] tileCategories = tileCategoryNames.split(",");
            performUseCase(tileType, tileName, favoritesFolder, filterTiles,
                    tileCategories);
            ArrayList<String> allAdminTileNames = new ArrayList<String>(
                    Arrays.asList(tilesAdminMode));
            allAdminTileNames.addAll((Arrays.asList(tilesAdminMode)));
            allAdminTileNames.addAll((Arrays.asList(filterTiles)));
            allAdminTileNames.add(workflowTileName);
            allAdminTileNames.add(duplicateTileName);
            allAdminTileNames.add(editTileName);
            getTilesFromAdminMode(allAdminTileNames);
            getTilesFromUserMode(allAdminTileNames);
        } catch (Exception e) {
            CSLogger.debug("Failed to filter and verify tiles", e);
            Assert.fail("Failed to filter and verify tiles", e);
        }
    }

    /**
     * This method gets tiles from user mode
     * 
     * @param allAdminTileNames contains admin tile names
     */
    private void getTilesFromUserMode(ArrayList<String> allAdminTileNames) {
        switchTheMode(dashboardPage.getBtnCurrentUser());
        List<WebElement> tileNames = getTile();
        List<String> tileNamesString = getStringValues(tileNames);
        if (tileNamesString.containsAll(allAdminTileNames)) {
            CSLogger.info(
                    "All Tiles of admin are present in current user mode");
            lockDashboard();
            CSUtility.tempMethodForThreadSleep(2000);
        } else {
            CSLogger.error(
                    "Tiles of Admin node are not present in the current user mode");
            softAssert.fail(
                    "Tiles of Admin node are not present in the current user mode");
        }
    }

    /**
     * This method performs the use case of filtering of tiles
     * 
     * @param tileType contains tile type
     * @param tileName contains tile name
     * @param favoritesFolder contains name of favorites folder
     * @param filterTiles contains tile names
     * @param tileCategories contains tile category names
     */
    private void performUseCase(String tileType, String tileName,
            String favoritesFolder, String[] filterTiles,
            String[] tileCategories) {
        // add tile1 in category1
        addFilterTile(filterTiles[0], tileType, favoritesFolder,
                tileCategories[0]);
        CSUtility.tempMethodForThreadSleep(3000);
        // add different tile2 in same category1
        addFilterTile(filterTiles[1], tileType, favoritesFolder,
                tileCategories[0]);
        CSUtility.tempMethodForThreadSleep(2000);
        // add same tile2 as first in different category2
        customizeTileFromSettingsIcon(filterTiles[1], tileCategories[1],
                favoritesFolder);
        // duplicate tile2 which has been assigned to category2
        CSUtility.tempMethodForThreadSleep(2000);
        duplicateTile(filterTiles[1], filterTiles[2]);
        CSUtility.tempMethodForThreadSleep(2000);
        addFilterTile(filterTiles[3], tileType, favoritesFolder,
                tileCategories[2]);
    }

    /**
     * This method gets tiles from admin node
     * 
     * @param adminTiles contains admin tiles
     */
    private void getTilesFromAdminMode(List<String> adminTiles) {
        clkBtnAll();
        List<WebElement> tileNames = getTile();
        List<String> tileNamesString = getStringValues(tileNames);
        if (adminTiles.containsAll(tileNamesString)) {
            CSLogger.info(
                    "All Tiles are present in All sections of the admin  ");
        } else {
            CSLogger.error("Tiles are missing ");
            softAssert.fail("Tiles are missing ");
        }
    }

    /**
     * This method clicks All button in the admin mode
     */
    private void clkBtnAll() {
        WebElement btnAll = browserDriver
                .findElement(By.xpath("//span[contains(text(),'All')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnAll);
        btnAll.click();
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method customizes tile from the settings icon on tile
     * 
     * @param tileName contains name of tile
     * @param tileCategory contains tile category
     * @param favoritesFolder contains name of favorites folder
     */
    private void customizeTileFromSettingsIcon(String tileName,
            String tileCategory, String favoritesFolder) {
        clkCreatedCategory(tileCategory);
        CSUtility.tempMethodForThreadSleep(3000);
        clkButtonInTile(tileName, dashboardPage.getBtnSettingsTile());
        CSUtility.tempMethodForThreadSleep(2000);
        enterDetails(tileName, dashboardPage.getTxtTileCaption());
        enterDetailsInCategoryTextbox(tileCategory, favoritesFolder);
        verifyAddedTileToCategory(tileCategory, tileName);
    }

    /**
     * This method adds tile in the administrator mode
     * 
     * @param tileName contains name of tile
     * @param tileType contains type of tile
     * @param favoritesFolder contains favorites folder name
     * @param tileCategory contains tile category
     */
    private void addFilterTile(String tileName, String tileType,
            String favoritesFolder, String tileCategory) {
        dashboardPage.clkElement(waitForReload, dashboardPage.getBtnPlus());
        configureTile(tileType, tileName, favoritesFolder, tileCategory);
        verifyAddedTileToCategory(tileCategory, tileName);
    }

    /**
     * This method verifies added tile to category
     * 
     * @param tileCategory contains category name in which tile should be added
     * @param tileName contains name of tile
     */
    private void verifyAddedTileToCategory(String tileCategory,
            String tileName) {
        clkCreatedCategory(tileCategory);
        getStatusOfTile(false, tileName);
        CSLogger.info(tileName + "  is present in " + tileCategory);
    }

    /**
     * This method clicks on created category
     * 
     * @param tileCategory contains name of tile category
     */
    private void clkCreatedCategory(String tileCategory) {
        int countCategoryName = browserDriver.findElements(By
                .xpath("//div[@class='btn-group']/button/span[contains(text(),'"
                        + tileCategory + "')]"))
                .size();
        if (countCategoryName == 1) {
            WebElement categoryName = browserDriver.findElement(By.xpath(
                    "//div[@class='btn-group']/button/span[contains(text(),'"
                            + tileCategory + "')]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, categoryName);
            categoryName.click();
            CSLogger.info("Clicked on the created category");
        }
    }

    /**
     * This method configures the tile
     * 
     * @param tileType contains tile type
     * @param tileName contains tile name
     * @param favoritesFolder contains favorites folder
     * @param tileCategory contains tile category
     */
    private void configureTile(String tileType, String tileName,
            String favoritesFolder, String tileCategory) {
        dashboardPage.clkElement(waitForReload, dashboardPage.getTabNewTile());
        CSLogger.info("Clicked on new tile tab");
        CSUtility.tempMethodForThreadSleep(3000);
        selectTileType(tileType);
        enterDetails(tileName, dashboardPage.getTxtTileCaption());
        CSUtility.tempMethodForThreadSleep(1000);
        enterDetailsInCategoryTextbox(tileCategory, favoritesFolder);
    }

    /**
     * This method enters the details in category text box
     * 
     * @param tileCategory contains name of tile category
     * @param favoritesFolder contains favorites folder name
     */
    private void enterDetailsInCategoryTextbox(String tileCategory,
            String favoritesFolder) {
        try {
            WebElement txtCategory = browserDriver
                    .findElement(By.xpath("//input[@placeholder='Category']"));
            waitForReload.until(ExpectedConditions.visibilityOf(txtCategory));
            CSUtility.tempMethodForThreadSleep(2000);
            txtCategory.sendKeys(tileCategory);
            CSUtility.tempMethodForThreadSleep(2000);
            selectFavoritesFolder(favoritesFolder);
        } catch (Exception e) {
            enterDetailsByRemovingExistingCategory(tileCategory,
                    favoritesFolder);
        }
    }

    /**
     * This method method enters details in category textbox by removing
     * existing category names
     * 
     * @param tileCategory contains name of tile category
     * @param favoritesFolder contains favorites folder name
     */
    private void enterDetailsByRemovingExistingCategory(String tileCategory,
            String favoritesFolder) {
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement txtCategory = browserDriver
                .findElement(By.xpath("//div[contains(@id,'CSComboBoxView')]"
                        + "//input[@class='form-control CSTextFieldView']"));
        CSUtility.waitForElementToBeClickable(waitForReload, txtCategory);
        txtCategory.click();
        CSUtility.tempMethodForThreadSleep(2000);
        txtCategory.sendKeys(Keys.BACK_SPACE);
        txtCategory.sendKeys(tileCategory);
        CSUtility.tempMethodForThreadSleep(2000);
        selectFavoritesFolder(favoritesFolder);
    }

    /**
     * This data provider returns sheet which contains tile name,tile type and
     * favorites folder name
     * 
     * @return addTileSheet
     */
    @DataProvider(name = "addTileTest")
    public Object[][] addTile() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("dashboardModuleTestCases"),
                addTileSheet);
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
        actions = new Actions(browserDriver);
        softAssert = new SoftAssert();
    }
}
