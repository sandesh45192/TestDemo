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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver browserDriverInstance;

    public LoginPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Login page POM elements.");
    }

    @FindBy(id = "CSPortalLoginSelect")
    private WebElement drodwnPortalSelect;

    @FindBy(id = "CSPortalLoginUserID")
    private WebElement txtUsername;

    @FindBy(id = "CSPortalLoginPassword")
    private WebElement txtPassword;

    @FindBy(id = "login")
    private WebElement btnLoginButton;

    @FindBy(id = "loginLanguage")
    private WebElement drpdwnLanguage;

    @FindBy(xpath = "//option[contains(text(),'Create New Portal')]")
    private WebElement createNewPortal;

    @FindBy(xpath = "//input[@value='Create']")
    private WebElement btnCreate;

    @FindBy(xpath = "//span[@id='CSPortalLoginForgotPassword']")
    private WebElement forgotPasswordLink;

    @FindBy(xpath = "//input[@id='CSPortalLoginLostPasswordInput']")
    private WebElement txtLoginLostPasswordInput;

    @FindBy(xpath = "//input[@id='CSPortalLoginLostPasswordButton']")
    private WebElement btnLoginLostPassword;

    @FindBy(xpath = "//span[@id='CSPortalLoginRegisterLink']")
    private WebElement signUpLink;

    @FindBy(xpath = "//span[contains(text(),'CS19.0')]")
    private WebElement csVersion;

    /**
     * This method returns the instance of login lost password button
     * 
     * @return btnLoginLostPassword
     */
    public WebElement getBtnLoginLostPassword() {
        return btnLoginLostPassword;
    }

    /**
     * This method returns the instance of sign up link
     * 
     * @return signUpLink
     */
    public WebElement getSignUpLink() {
        return signUpLink;
    }

    /**
     * This method returns the instance of LoginLostPassword input
     * 
     * @return txtLoginLostPasswordInput
     */
    public WebElement getTxtLoginLostPasswordInput() {
        return txtLoginLostPasswordInput;
    }

    /**
     * This method clicks on create new option for creating new portal
     */
    public void clkBtnCreate() {
        getBtnCreate().click();
        CSLogger.info("Clicked on Create button to create new portal");
    }

    /**
     * This method returns the instance of Forget Password link
     */
    public WebElement getForgotPasswordLink() {
        return forgotPasswordLink;
    }

    /**
     * This method clicks on the forgot password link * @param waitForReload
     * waits for an element to reload
     */
    public void clkForgotPasswordLink(WebDriverWait waitForReload) {
        waitForReload.until(
                ExpectedConditions.visibilityOf(getForgotPasswordLink()));
        getForgotPasswordLink().click();
        CSLogger.info("Clicked on Forgot Password link");
    }

    /**
     * This method sets the provided username in the username text field.
     * 
     * @param username String value containing the username
     */
    public void enterUsername(String username) {
        getTxtUsername().clear();
        getTxtUsername().sendKeys(username);
        CSLogger.info("Entered username " + username);
    }

    /**
     * This method sets the provided password in the password text field.
     * 
     * @param password String value containing the password
     */
    public void enterPassword(String password) {
        getTxtPassword().clear();
        getTxtPassword().sendKeys(password);
        CSLogger.info("Entered password " + password);
    }

    /**
     * This method perform click operation on the login button.
     */
    public void clkLoginButton() {
        getBtnLoginButton().click();
        CSLogger.info("Clicked on the login button.");
    }

    public void clkSignUp(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getSignUpLink()));
        getSignUpLink().click();
        CSLogger.info("Clicked on sign up link");
    }

    /**
     * This method retrieves the selected option from the dropdown box.
     * 
     * @return String value containing the the selected language
     */
    public String getSelectedLanguage() {
        Select portalSelection = new Select(getDrpdwnLanguage());
        return portalSelection.getFirstSelectedOption().getText();
    }

    /**
     * This method retrieves the selected option from the dropdown box.
     * 
     * @return String value containing the the selected portal
     */
    public String getSelectedPortal() {
        Select portalSelection = new Select(getDrodwnPortalSelect());
        return portalSelection.getFirstSelectedOption().getText();
    }

    /**
     * This method returns the instance of Create button to create new portal
     * 
     * @return btnCreate
     */
    public WebElement getBtnCreate() {
        return btnCreate;
    }

    /**
     * Getter for txtUsername
     * 
     * @return WebElement
     */
    public WebElement getTxtUsername() {
        return txtUsername;
    }

    /**
     * This method returns the instance of create new portal
     * 
     * @return createNewPortal
     */
    public WebElement getCreateNewPortal() {
        return createNewPortal;
    }

    /**
     * Getter for txtPassword
     * 
     * @return WebElement
     */
    public WebElement getTxtPassword() {
        return txtPassword;
    }

    /**
     * Getter for btnLoginButton
     * 
     * @return WebElement
     */
    public WebElement getBtnLoginButton() {
        return btnLoginButton;
    }

    /**
     * Getter for drpdwnLanguage
     * 
     * @return WebElement
     */
    public WebElement getDrpdwnLanguage() {
        return drpdwnLanguage;
    }

    /**
     * Getter for browserDriverInstance
     * 
     * @return WebDriver
     */
    private WebDriver getBrowserDriverInstance() {
        return browserDriverInstance;
    }

    /**
     * Setter for browserDriverInstance
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * Getter for drpdwnPortalSelect
     * 
     * @return WebElement
     */
    public WebElement getDrodwnPortalSelect() {
        return drodwnPortalSelect;
    }

    /**
     * This method returns the instance of cs version
     * 
     * @return csVersion
     */
    public WebElement getCsVersion() {
        return csVersion;
    }

}
