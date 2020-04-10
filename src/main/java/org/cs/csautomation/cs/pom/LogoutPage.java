/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogoutPage {

    @FindBy(id = "CSPortalLinkPortalOptions")
    private WebElement portalLinkOptions;

    @FindBy(xpath = "//td[contains(text(),'Logout')]")
    private WebElement btnLogout;

    @FindBy(id = "login")
    private WebElement btnLogin;

    /**
     * Parameterized constructor. This method sets the browser driver instance
     * for this page.
     * 
     * @param paramDriver
     */
    public LogoutPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Login page POM elements.");
    }

    /**
     * Returns the element shows the portal options
     * 
     * @return portalLinkOptions WebElement of logout button popup
     */
    public WebElement getPortalLinkOption() {
        return portalLinkOptions;
    }

    /**
     * Returns the element for logout Button
     * 
     * @return btnLogout WebElement of logout Button
     */
    public WebElement getBtnLogoutButton() {
        return btnLogout;
    }

    /**
     * Returns the element for login Button
     * 
     * @return btnLogin WebElement login Button of login page.S
     */
    public WebElement getBtnLoginButton() {
        return btnLogin;
    }

    /**
     * Click on the logout popup portal options
     */
    public void clickPortalLink() {
        getPortalLinkOption().click();
        CSLogger.info("Clicked on the Portal Option.");
    }

    /**
     * Click on logout Button
     */
    public void clkLogoutButton() {
        getBtnLogoutButton().click();
        CSLogger.info("Clicked on the logout button.");
    }

}
