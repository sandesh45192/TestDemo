/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.login;

import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class performs the version verification of CS system
 * 
 * @author CSAutomation Team
 *
 */
public class VersionVerificationTest extends AbstractTest {

    private LoginPage     loginPage;
    private WebDriverWait waitForReload;

    /**
     * This test method verifies the version of CS system
     */
    @Test
    public void testVerifyVersion() {
        try {
            browserDriver.get(config.getUrl());
            String version = config.getCsVersion();
            performVersionVerifcation(version);
        } catch (Exception e) {
            CSLogger.debug("Failed to verify version ", e);
            Assert.fail("Test case for version verification failed", e);
        }
    }

    /**
     * This test performs the version verification
     * 
     * @param version contains String value of version from the properties file
     */
    private void performVersionVerifcation(String version) {
        WebElement csVersion = waitForReload.until(
                ExpectedConditions.visibilityOf(loginPage.getCsVersion()));
        String verifyVersion = csVersion.getText();
        if (verifyVersion.contains(version)) {
            CSLogger.info("Version verified successfully");
        } else {
            CSLogger.error("Could not verify the cs system version");
        }
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        loginPage = new LoginPage(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        CSLogger.info("Initialized the POM object for Login page.");
    }
}
