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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class VerifyInvalidEmailAddressTest extends AbstractTest {

    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private LoginPage        loginPage;
    private RegisterUserPage registerUserPage;
    private SoftAssert       softAssert;
    private String           verifyInvalidEmailSheet = "VerifyInvalidEmail";

    /**
     * This class verifies invalid email address
     * 
     * @param emailAddress contains email address
     */
    @Test(priority = 1, dataProvider = "verifyInvalidEmail")
    public void testVerifyInvalidEmailAddress(String emailAddress) {
        try {
            loginPage.clkSignUp(waitForReload);
            loginWithInvalidEmail(emailAddress);
            verifyAlertMessage();
            registerUserPage.clkBtnCancel(waitForReload);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method verifies alert message after registering blank data
     */
    private void verifyAlertMessage() {
        try {
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            String alertText = alert.getText();
            if (alertText.equals("Mail Address is Invalid!")) {
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
     * This method logs in with invalid mail
     * 
     * @param emailAddress contains mail address
     */
    private void loginWithInvalidEmail(String emailAddress) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(registerUserPage.getTxtRegisterMailInput()));
        registerUserPage.getTxtRegisterMailInput().clear();
        registerUserPage.getTxtRegisterMailInput().sendKeys(emailAddress);
        CSLogger.info("Entered username");
        clearPasswordField();
        registerUserPage.clkBtnRegister(waitForReload);
    }

    /**
     * This method clears password field
     */
    private void clearPasswordField() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(registerUserPage.getTxtRegisterPasswordInput()));
        registerUserPage.getTxtRegisterPasswordInput().clear();
    }

    /**
     * This data provider returns the sheet data which contains email address
     * 
     * @return
     */
    @DataProvider(name = "verifyInvalidEmail")
    public Object[] verifyEmail() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                verifyInvalidEmailSheet);
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
