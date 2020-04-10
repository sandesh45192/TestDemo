/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.settings.RegisterUserPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class verifies registration of mail and incorrect password
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyValidEmailAndIncorrectPasswordTest extends AbstractTest {

    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private LoginPage        loginPage;
    private RegisterUserPage registerUserPage;
    private SoftAssert       softAssert;
    private String           verifyMailAndIncorrectPasswordSheet = "VerifyMailAndIncorrectPassword";

    /**
     * This method verifies registration of mail and incorrect password
     * 
     * @param mailAddress contains mail address
     * @param password contains password
     */
    @Test(priority = 1, dataProvider = "verifyMailAndIncorrectPassword")
    public void testVerifyMailAndIncorrectPassword(String mailAddress,
            String password) {
        try {
            loginPage.clkSignUp(waitForReload);
            registerWithIncorrectPassword(mailAddress, password);
            verifyAlertMessage();
            registerUserPage.clkBtnCancel(waitForReload);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed ", e);
        }
    }

    /**
     * This method registers using existing mail and password
     * 
     * @param emailAddress contains mail address
     * @param password contains password
     */
    private void registerWithIncorrectPassword(String emailAddress,
            String password) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(registerUserPage.getTxtRegisterMailInput()));
        enterInput(registerUserPage.getTxtRegisterMailInput(), emailAddress);
        waitForReload.until(ExpectedConditions
                .visibilityOf(registerUserPage.getTxtRegisterPasswordInput()));
        enterInput(registerUserPage.getTxtRegisterPasswordInput(), password);
        registerUserPage.clkBtnRegister(waitForReload);
    }

    /**
     * This method verifies alert message after registering blank data
     */
    private void verifyAlertMessage() {
        try {
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            String alertText = alert.getText();
            // Here if password is not set, message will be -- "Wrong Password"
            if (alertText.equals(
                    "The defined password pattern does not match the entered password.")) {
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
     * This method enters username and passwords by clearing text fields
     * 
     * @param element contains instance of an element
     * @param input contains input
     */
    private void enterInput(WebElement element, String input) {
        element.clear();
        element.sendKeys(input);
        CSLogger.info("Entered input");
    }

    /**
     * This data provider returns the sheet data which contains email address
     * and password
     * 
     * @return verifyMailAndIncorrectPasswordSheet
     */
    @DataProvider(name = "verifyMailAndIncorrectPassword")
    public Object[] verifyEmail() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                verifyMailAndIncorrectPasswordSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        loginPage = new LoginPage(browserDriver);
        registerUserPage = new RegisterUserPage(browserDriver);
        softAssert = new SoftAssert();
    }
}
