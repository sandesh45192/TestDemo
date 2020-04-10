/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains xpaths and respective methods to drive the test cases
 * related to register page
 * 
 * @author CSAutamtion Team
 *
 */
public class RegisterUserPage {

    public RegisterUserPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized   Settings node page POM elements");
    }

    @FindBy(xpath = "//input[@id='CSPortalLoginRegisterEmailInput']")
    private WebElement txtRegisterMailInput;

    @FindBy(xpath = "//input[@id='CSPortalLoginRegisterPasswordInput']")
    private WebElement txtRegisterPasswordInput;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement txtPassword;

    @FindBy(xpath = "(//a[@class='CSPortalGuiToolbarButtonContent'])[1]")
    private WebElement btnRegister;

    @FindBy(xpath = "//a[contains(text(),'Cancel')]")
    private WebElement btnCancel;

    @FindBy(xpath = "//div[@name='CSPortalRegisterUser']")
    private WebElement portalRegisterUser;

    @FindBy(xpath = "//div[5]//div[4]")
    private WebElement dragIcon;

    @FindBy(xpath = "//a[@class='CSPortalWindowCloser']")
    private WebElement btnClose;

    /**
     * This method returns the instance of instance variable btnClose
     * 
     * @return btnClose
     */
    public WebElement getBtnClose() {
        return btnClose;
    }

    /**
     * This method returns the instance of drag icon data member
     * 
     * @return dragIcon
     */
    public WebElement getDragIcon() {
        return dragIcon;
    }

    /**
     * This method returns portal register user
     * 
     * @return portalRegisterUser
     */
    public WebElement getPortalRegisterUser() {
        return portalRegisterUser;
    }

    /**
     * This method returns the instance of mail input textbox
     * 
     * @return
     */
    public WebElement getTxtRegisterPasswordInput() {
        return txtRegisterPasswordInput;
    }

    /**
     * This method returns the instance of cancel button
     * 
     * @return
     */
    public WebElement getBtnCancel() {
        return btnCancel;
    }

    /**
     * This method returns the instance of register button
     * 
     * @return btnRegister
     */
    public WebElement getBtnRegister() {
        return btnRegister;
    }

    /**
     * This method returns the instance of password text field
     * 
     * @return txtPassword
     */
    public WebElement getTxtPassword() {
        return txtPassword;
    }

    /**
     * This method returns the instance of mail input textbox
     * 
     * @return
     */
    public WebElement getTxtRegisterMailInput() {
        return txtRegisterMailInput;
    }

    /**
     * This method clicks on Cancel button
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnCancel(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getBtnCancel()));
        getBtnCancel().click();
        CSLogger.info("Clicked on Cancel button");
    }

    /**
     * This method clicks on Register button
     * 
     * @param waitForReload
     */
    public void clkBtnRegister(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getBtnRegister()));
        getBtnRegister().click();
        CSLogger.info("Clicked on Register button");
    }

    /**
     * This method clicks on the close button
     * 
     * @param waitForReload waits for an element to relaod
     */
    public void clkBtnClose(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getBtnClose()));
        getBtnClose().click();
        CSLogger.info("Clicked on close button");

    }
}
