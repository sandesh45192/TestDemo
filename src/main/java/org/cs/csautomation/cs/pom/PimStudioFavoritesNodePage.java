/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains the test methods to handle the test cases related to the
 * login use cases.
 * 
 * @author CSAutomation Team
 *
 */
public class PimStudioFavoritesNodePage {

    private WebDriver  browserDriverInstance;

    @FindBy(id = "favorites@0")
    private WebElement btnPimFavoritesNode;

    /**
     * Returns the WebElement btnPimFavoritesNode
     * 
     * @return btnPimFavoritesNode PimStudioFavoritesNode
     */
    public WebElement getbtnPimFavoritesNode() {
        return btnPimFavoritesNode;
    }

    /**
     * Parameterized constructor. This method sets the browser driver instance
     * for this page.
     * 
     * @param paramDriver
     */
    public PimStudioFavoritesNodePage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized PimStudioSettingsNode page POM elements.");
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * Clicks on Pim Studio Favorites Node or webElement btnPimFavoritesNode
     * 
     * @param waitForReload
     *            this is webDriverWait object
     */
    public void clkBtnPimFavoritesNode(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriverInstance);
        waitForReload.until(
                ExpectedConditions.visibilityOf(getbtnPimFavoritesNode()));
        getbtnPimFavoritesNode().click();
        CSLogger.info("Clicked on Pim favorites node");
    }

    /**
     * Right click on the button favorite Node
     * 
     * @param waitForReload
     *            this is webDriverWait object
     */
    public void rightclkBtnPimFavoritesNode(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriverInstance);
        WebElement FavoriteNode = browserDriverInstance.findElement(
                By.xpath("//a[contains(@id,'favorites@0_ANCHOR')]"));
        CSUtility.scrollUpOrDownToElement(FavoriteNode, browserDriverInstance);
        CSUtility.rightClickTreeNode(waitForReload, FavoriteNode,
                browserDriverInstance);
        CSLogger.info("Right clicked on Pim favorites node");
    }

    /**
     * Right click on the folder under favorite node
     * 
     * @param waitForReload
     *            this is webDriverWait object
     * @param FavoriteFolderName
     *            String favorite folder name
     */
    public void rightclkFavoritesFolder(WebDriverWait waitForReload,
            String FavoriteFolderName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriverInstance);
        waitForReload.until(
                ExpectedConditions.visibilityOf(getbtnPimFavoritesNode()));
        getbtnPimFavoritesNode().click();
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(FavoriteFolderName)));
        WebElement createdFavorite = browserDriverInstance
                .findElement(By.linkText(FavoriteFolderName));
        CSUtility.rightClickTreeNode(waitForReload, createdFavorite,
                browserDriverInstance);
    }

}
