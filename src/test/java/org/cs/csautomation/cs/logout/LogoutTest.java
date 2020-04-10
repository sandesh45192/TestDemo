/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.logout;

import org.cs.csautomation.cs.pom.LogoutPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to handle the test cases related to the
 * logout use cases.
 * 
 * @author CSAutomation Team
 *
 */

public class LogoutTest extends AbstractTest {

    LogoutPage            logoutPage;
    private WebDriverWait waitForReload;
    private FrameLocators frameLocaters;

    /**
     * This method verifies that the browser is on login page .
     * 
     * Check the portal option present on the page.
     */
    @Test
    public void testLogOut() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            frameLocaters.getFrmTopFrame()));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    logoutPage.getPortalLinkOption());
            if (browserDriver.findElements(By.id("CSPortalLinkPortalOptions"))
                    .size() != 0) {
                CSLogger.info("Login page found Successfully");
            } else {
                Assert.fail("Login page not found");
            }
            performLogout();
        } catch (Exception e) {
            CSLogger.debug("Automation error : testLogOut",e);
            CSLogger.error("Automation error : testLogOut", e);
        }
    }

    /**
     * This method Click on logout and logout the page .
     * 
     * This method is depends on the 'checkLogin()'.
     */
    private void performLogout() {
        logoutPage.clickPortalLink();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(frameLocaters.getFrmCsPopupDivFrame()));
        browserDriver.switchTo().frame(frameLocaters.getFrmCsPopupDivFrame());
        logoutPage.clkLogoutButton();
        verifyLogout();
    }

    /**
     * This method checks whether the logout is successful or not.
     * 
     * Uses the login button to check.
     */
    public void verifyLogout() {
        try {
            if (logoutPage.getBtnLoginButton().isEnabled()) {
                CSLogger.info("Logout Successful");
            } else {
                Assert.fail("Logout is unsuccessful");
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error", e);
        }
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        logoutPage = new LogoutPage(browserDriver);
        frameLocaters = new FrameLocators(browserDriver);
        CSLogger.info("Initialized the POM object for Login page.");
    }

}
