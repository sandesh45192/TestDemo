/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * When clicked on usages button a usages window pop's up this class contains
 * WebElements of that usage page and methods that performs actions on
 * WebElements.
 * 
 * @author CSAutomation Team
 *
 */
public class UsagesPage {

    private WebDriver  browserDriverInstance;

    @FindBy(xpath = "//td[contains(text(),'Reference to')]")
    private WebElement referenceToTree;

    @FindBy(xpath = "//td[contains(text(),'No usage in')]")
    private WebElement noUsageInTree;

    @FindBy(xpath = "//td[contains(text(),'Usage in')]")
    private WebElement usageInTree;

    @FindBy(xpath = "(//a[contains(text(),'Product')])[3]")
    private WebElement referenceToTreeProductSubMenu;

    @FindBy(xpath = "(//a[contains(text(),'File')])[3]")
    private WebElement referenceToTreeFileSubMenu;

    @FindBy(xpath = "(//a[contains(text(),'User')])[3]")
    private WebElement referenceToTreeUserSubMenu;

    @FindBy(xpath = "(//a[contains(text(),'Product')])[1]")
    private WebElement usageInTreeProductSubMenu;

    public UsagesPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info(
                "Initialized  Selection Dialog Window page POM elements.");
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
     * Returns the WebElement referenceToTree.
     * 
     * @return referenceToTree
     */
    public WebElement getReferenceToTree() {
        return referenceToTree;
    }

    /**
     * Returns the WebElement referenceToTree
     * 
     * @return
     */
    public WebElement getNoUsageInTree() {
        return noUsageInTree;
    }

    /**
     * Returns the WebElement referenceToTree
     * 
     * @return
     */
    public WebElement getUsageInTree() {
        return usageInTree;
    }

    /**
     * Returns the WebElement referenceToTree
     * 
     * @return
     */
    public WebElement getReferenceToTreeProductSubMenu() {
        return referenceToTreeProductSubMenu;
    }

    /**
     * Returns the WebElement referenceToTreeFileSubMenu
     * 
     * @return referenceToTreeFileSubMenu
     */
    public WebElement getReferenceToTreeFileSubMenu() {
        return referenceToTreeFileSubMenu;
    }

    /**
     * Returns the WebElement referenceToTreeUserSubMenu
     * 
     * @return referenceToTreeUserSubMenu
     */
    public WebElement getReferenceToTreeUserSubMenu() {
        return referenceToTreeUserSubMenu;
    }

    /**
     * Returns the WebElement usageInTreeProductSubMenu
     * 
     * @return usageInTreeProductSubMenu
     */
    public WebElement getUsageInTreeProductSubMenu() {
        return usageInTreeProductSubMenu;
    }

    /**
     * Clicks on 'Reference To' tree node.
     * 
     * @param waitForReload
     *            this is webDriverWait object.
     */
    public void clkOnReferenceToMenu(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getReferenceToTree());
        getReferenceToTree().click();
        CSLogger.info("Clicked on 'Reference To' tree node");
    }

    /**
     * Clicks on 'No Usages In' tree node.
     * 
     * @param waitForReload
     *            this is webDriverWait object.
     */
    public void clkOnNoUsageInMenu(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getNoUsageInTree());
        getNoUsageInTree().click();
        CSLogger.info("Clicked on 'No Usages In' tree node");
    }

    /**
     * Clicks on 'Usages In' tree node.
     * 
     * @param waitForReload
     *            this is webDriverWait object.
     */
    public void clkOnUsageInMenu(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getUsageInTree());
        getUsageInTree().click();
        CSLogger.info("Clicked on 'Usages In' tree node");
    }

    /**
     * Clicks on File submenu of 'Reference To' tree node.
     * 
     * @param waitForReload
     *            this is webDriverWait object.
     */
    public void clkOnReferenceToTreeFileSubMenu(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getReferenceToTreeFileSubMenu());
        getReferenceToTreeFileSubMenu().click();
        CSLogger.info("Clicked on File submenu of 'Reference To' tree node");
    }

    /**
     * Clicks on User submenu of 'Reference To' tree node.
     * 
     * @param waitForReload
     *            this is webDriverWait object.
     */
    public void clkOnReferenceToTreeUserSubMenu(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getReferenceToTreeUserSubMenu());
        getReferenceToTreeUserSubMenu().click();
        CSLogger.info("Clicked on User submenu of 'Reference To' tree node");
    }

    /**
     * Clicks on Product submenu of 'Reference To' tree node.
     * 
     * @param waitForReload
     *            this is webDriverWait object.
     */
    public void clkOnReferenceToTreeProductSubMenu(
            WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getReferenceToTreeProductSubMenu());
        getReferenceToTreeProductSubMenu().click();
        CSLogger.info("Clicked on Product submenu of 'Reference To' tree node");
    }

    /**
     * Clicks on Product submenu of 'Usage In' tree node.
     * 
     * @param waitForReload
     *            this is webDriverWait object.
     */
    public void clkOnUsageInTreeProductSubMenu(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getUsageInTreeProductSubMenu());
        getUsageInTreeProductSubMenu().click();
        CSLogger.info("Clicked on Product submenu of 'Usage In' tree node");
    }
}
