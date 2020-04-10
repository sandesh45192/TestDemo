/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.login;

import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to handle the test cases related to the
 * login use cases.
 * 
 * @author CSAutomation Team
 *
 */
public class LoginTest extends AbstractTest {

    private LoginPage     loginPage;
    private WebDriverWait waitForReload;
    private String        LoginSheetName = "Logintest";

    /**
     * This method test the login credentials supplied to it via DataProvider
     * method 'CredentailsTestData'. This method also depends on method
     * 'verifyPresenceOfElements'
     * 
     * @param username String Username to test.
     * @param password String password to test.
     * @param result String Expected result of the login attempt.
     */
    @Test(
            dependsOnMethods = { "verifyPresenceOfElements" },
            dataProvider = "CredentailsTestData")
    public void testLogin(String username, String password, String portal,
            String language, String result) {
        waitForReload = new WebDriverWait(browserDriver, 180);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(loginPage.getDrpdwnLanguage()));
        Select languageDrpdwn = new Select(loginPage.getDrpdwnLanguage());
        languageDrpdwn.selectByVisibleText(language);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clkLoginButton();
        Alert alertBox = null;
        if (result.contains("Fail")) {
            try {
                alertBox = getAlertBox();
                if (alertBox.getText().startsWith("Login failed")) {
                    CSLogger.info("Detected Alert box with text :"
                            + alertBox.getText());
                    alertBox.accept();
                }
            } catch (NoAlertPresentException e) {
                CSLogger.error("Alert not found : ", e);
                Assert.fail("Login use case with test data \"" + username
                        + "\" and \"" + password + "\" failed !");
            }
        } else {
            try {
                alertBox = getAlertBox();
                if (alertBox != null) {
                    CSLogger.error("Detected Alert box with text :"
                            + alertBox.getText());
                    Assert.fail("Login use case with test data \"" + username
                            + "\" and \"" + password + "\" failed !");
                    alertBox.accept();
                }
            } catch (NoAlertPresentException e) {
                CSLogger.info("Alert not found : ", e);
            }
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(loginPage.getDrodwnPortalSelect()));
            Select portalDrpdwn = new Select(loginPage.getDrodwnPortalSelect());
            portalDrpdwn.selectByVisibleText(portal);
            CSLogger.info(
                    "Selected Portal is : " + loginPage.getSelectedPortal());
            CSLogger.info(
                    "Selected Portal is : " + loginPage.getSelectedLanguage());
            loginPage.clkLoginButton();
        }
    }

    /**
     * This method returns the alert box.
     * 
     * @return Alert Instance of Alert class representing the DOM alert box.
     */
    private Alert getAlertBox() {
        Alert alertBox;
        CSUtility.tempMethodForThreadSleep(1000);
        try {
            alertBox = browserDriver.switchTo().alert();
            return alertBox;
        } catch (Exception e) {
            alertBox = null;
            return alertBox;
        }
    }

    /**
     * This method checks if elements required for login are present or not.
     */
    @Test
    public void verifyPresenceOfElements() {
        browserDriver.get(config.getUrl());
        int elementCount = 0;
        elementCount += (loginPage.getBtnLoginButton() == null ? 0 : 1);
        elementCount += (loginPage.getTxtPassword() == null ? 0 : 1);
        elementCount += (loginPage.getTxtUsername() == null ? 0 : 1);
        elementCount += (loginPage.getDrodwnPortalSelect() == null ? 0 : 1);
        elementCount += (loginPage.getDrpdwnLanguage() == null ? 0 : 1);
        elementCount += (loginPage.getBtnLoginButton() == null ? 0 : 1);
        if (elementCount != 6) {
            Assert.fail("Elements not present");
            CSLogger.error("Login elements are not present.");
        }
        CSLogger.info("Login elements are present.");
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of credentials
     */
    @DataProvider(name = "CredentailsTestData")
    public Object[][] getCredentailsTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("loginTestCases"), LoginSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        loginPage = new LoginPage(browserDriver);
        CSLogger.info("Initialized the POM object for Login page.");
    }

}
