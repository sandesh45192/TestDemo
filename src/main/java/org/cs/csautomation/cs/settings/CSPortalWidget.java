/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains WebElements and methods of CSPortalWidget.
 * 
 * @author CSAutomation Team
 *
 */
public class CSPortalWidget {

    private WebDriver  browserDriverInstance;

    @FindBy(
            xpath = "//li[@id='StudioWidgetPane_e20aa82bb06b2cd4551ff728f5d58a2e_Title']//div[@class='CSPortalGuiPanelIcon']")
    private WebElement btnSystemPreferences;

    @FindBy(xpath = "//div[contains(@style,'signup.svg')]/..")
    private WebElement btnProjectManager;
    
    @FindBy(xpath = "//div[contains(@style,'globe.svg')]")
    private WebElement btnTranslationManager;

    /**
     * This method returns the webelement translation Manager
     * 
     * @return btnTranslationManager
     */
    public WebElement getBtnTranslationManager() {
        return btnTranslationManager;
    }

    public CSPortalWidget(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Settings node page POM elements");
    }

    /**
     * Sets instance of browserDriver.
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * This method clicks on translation manager icon
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkOnTranslationManagerIcon(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnTranslationManager());
        getBtnTranslationManager().click();
        CSLogger.info("Clicked on Translation manager icon");
    }

    /**
     * Returns the WebElement of Project Manager.
     * 
     * @return WebElement btnProjectManager.
     */
    public WebElement getBtnProjectManager() {
        return btnProjectManager;
    }

    /**
     * Returns the WebElement of System preferences.
     * 
     * @return WebElement btnSystemPreferences.
     */
    public WebElement getBtnSystemPreferences() {
        return btnSystemPreferences;
    }

    /**
     * This methods performs click operation on System Preferences Icon.
     * 
     * @param waitForReload WebDriverWait Object.
     */
    public void clkOnSystemPreferencesIcon(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnSystemPreferences());
        getBtnSystemPreferences().click();
        CSLogger.info("Clicked on System Preferences icon.");
    }

    /**
     * This methods performs click operation on System Preferences Icon.
     * 
     * @param waitForReload WebDriverWait Object.
     */
    public void clkOnProjectManagerIcon(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnProjectManager());
        getBtnProjectManager().click();
        CSLogger.info("Clicked on project manager icon.");
    }
}
