/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.settings.RegisterUserPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class verifies the alert message which pops up after sending email and
 * password fields blank
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyBlankDataTest extends AbstractTest {

    private CSPortalHeader   csPortalHeader;
    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private IProductPopup    productPopUp;
    private LoginPage        loginPage;
    private RegisterUserPage registerUserPage;
    private SoftAssert       softAssert;

    /**
     * This test verifies the alert message which pops up after sending email
     * and password fields blank
     */
    @Test(priority = 1)
    public void testVerifyBlankData() {
        try {
            loginPage.clkSignUp(waitForReload);
            clearFields();
            clkRegister();
            registerUserPage.clkBtnCancel(waitForReload);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method clicks Register button
     */
    private void clkRegister() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(registerUserPage.getBtnRegister()));
        registerUserPage.getBtnRegister().click();
        CSLogger.info("Clicked on Register button");
        verifyAlertMessage();
    }

    /**
     * This method clear fields of mail and password
     */
    private void clearFields() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        clearField(registerUserPage.getTxtRegisterMailInput(),
                registerUserPage.getTxtRegisterPasswordInput());
    }

    /**
     * This method clears mail and password field
     * 
     * @param txtRegisterMailInput contains instance of text box for mail input
     * @param txtRegisterPasswordInput contains instance of text box for
     *            password input
     */
    private void clearField(WebElement txtRegisterMailInput,
            WebElement txtRegisterPasswordInput) {
        waitForReload
                .until(ExpectedConditions.visibilityOf(txtRegisterMailInput));
        waitForReload.until(
                ExpectedConditions.visibilityOf(txtRegisterPasswordInput));
        txtRegisterMailInput.clear();
        txtRegisterPasswordInput.clear();
    }

    /**
     * This method verifies alert message after registering blank data
     */
    private void verifyAlertMessage() {
        try {
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            String alertText = alert.getText();
            if (alertText.equals("Please enter an email address")) {
                alert.accept();
                CSLogger.info("Clicked on ok of pop up");
            } else {
                CSLogger.info("Could not handle Alert");
            }
        } catch (Exception e) {
            CSLogger.info("could not handle alert", e);
            softAssert.fail("Could not handle assert");
        }
    }

    /**
     * This method performs log out from admin
     */
    private void logoutFromApplication() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getCsGuiPopupLogout()));
        productPopUp.getCsGuiPopupLogout().click();
        CSLogger.info("Logged out successfully ");
    }

    /**
     * This method performs logout from application
     */
    @BeforeMethod
    private void logout() {
        logoutFromApplication();
    }

    /**
     * This method initializes all the resources required to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        loginPage = new LoginPage(browserDriver);
        registerUserPage = new RegisterUserPage(browserDriver);
        softAssert = new SoftAssert();
    }
}
