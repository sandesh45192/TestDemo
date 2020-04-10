/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import org.cs.csautomation.cs.settings.IConfigPortalPopup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CSPopupDivConfigPortal implements IConfigPortalPopup {

    private static CSPopupDivConfigPortal locators;

    @FindBy(xpath = "//td[contains(text(),'admin')]")
    private WebElement                    ctxAdmin;

    @FindBy(xpath = "//td/img[contains(@src,'globe.svg')]")
    private WebElement                    ctxLanguageSettings;

    public CSPopupDivConfigPortal(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
    }

    /**
     * This method returns the instance of language settings
     * 
     * @return the ctxLanguageSettings
     */
    public WebElement getCtxLanguageSettings() {
        return ctxLanguageSettings;
    }

    /**
     * This method clicks on language settings option
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkCtxLanguageSettings(WebDriverWait waitForReload) {
        waitForReload.until(
                ExpectedConditions.visibilityOf(getCtxLanguageSettings()));
        getCtxLanguageSettings().click();
        CSLogger.info("Clicked on language settings ");
    }

    /**
     * This method click on Admin from configuration portal
     * 
     * @param waitForReload waits for an element to relad
     */
    public void clkCtxAdmin(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getCtxAdmin()));
        getCtxAdmin().click();
        CSLogger.info("Clicked on Admin from configuration portal ");
    }

    public static CSPopupDivConfigPortal
            getCSPopupDivConfigPortalLocators(WebDriver browserDriverInstance) {
        if (locators == null) {
            locators = new CSPopupDivConfigPortal(browserDriverInstance);
        }
        return locators;
    }

    /**
     * This method returns the instance of Admin
     * 
     * @return csGuiPopupAdmin
     */
    public WebElement getCtxAdmin() {
        return ctxAdmin;
    }
}
