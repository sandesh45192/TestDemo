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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class tests and verifies the resizing of general navigation window
 * 
 * @author CSAutomation Team
 *
 */
public class GeneralNavigationSignUpDialogTest extends AbstractTest {

    private WebDriverWait    waitForReload;
    private LoginPage        loginPage;
    private RegisterUserPage registerUserPage;
    private String           generalNavigationSignUpSheet = "GeneralNavigationSignUp";

    /**
     * This method tests and verifies the resizing of general navigation window
     * 
     * @param xOffset contains x offset value
     * @param yOffset contains y offset value
     */
    @Test(priority = 1, dataProvider = "generalNavigationSignUp")
    public void testGeneralNavigationSignUpDialog(String xOffset,
            String yOffset) {
        try {
            int xCoordinate = Integer.valueOf(xOffset);
            int yCoordinate = Integer.valueOf(yOffset);
            loginPage.clkSignUp(waitForReload);
            resizeGeneralNavigationWindow(xCoordinate, yCoordinate);
            registerUserPage.clkBtnClose(waitForReload);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method resizes general navigation window
     * 
     * @param xCoordinate contains x offset value
     * @param yCoordinate contains y offset value
     */
    private void resizeGeneralNavigationWindow(int xCoordinate,
            int yCoordinate) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(registerUserPage.getPortalRegisterUser()));
        int[] getWidthAndHeight = getOffsets();
        Actions actionsResize = new Actions(browserDriver);
        actionsResize.dragAndDropBy(registerUserPage.getDragIcon(), xCoordinate,
                yCoordinate).perform();
        verifyResizedWindow(getWidthAndHeight);
    }

    /**
     * This method verifies if window has bee resized or not
     * 
     * @param getWidthAndHeight array containing width and height of the
     *            navigation window
     */
    private void verifyResizedWindow(int[] getWidthAndHeight) {
        int[] newWidthAndHeight = getOffsets();
        boolean status = false;
        if (getWidthAndHeight[0] < newWidthAndHeight[0]
                && getWidthAndHeight[1] < newWidthAndHeight[1]) {
            CSLogger.info("Window has resized");
        } else {
            CSLogger.info("Could not resize window");
        }
    }

    /**
     * This method returns offset array which gets width and height at runtime
     * of navigation window
     * 
     * @return getWidthAndHeight
     */
    private int[] getOffsets() {
        String pixels = registerUserPage.getPortalRegisterUser()
                .getAttribute("style");
        String[] getPixels = pixels.split(";");
        String width = getPixels[0].trim();
        String height = getPixels[1].trim();
        width = width.replace("width: ", "").replace("px", "");
        height = height.replace("height: ", "").replace("px", "");
        int getWidth = Integer.valueOf(width);
        int getHeight = Integer.valueOf(height);
        int[] getWidthAndHeight = new int[] { getWidth, getHeight };
        return getWidthAndHeight;
    }

    /**
     * This method returns the sheet data containing x and y offsets
     * 
     * @return generalNavigationSignUpSheet
     */
    @DataProvider(name = "generalNavigationSignUp")
    public Object[] verifyPassword() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                generalNavigationSignUpSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        loginPage = new LoginPage(browserDriver);
        registerUserPage = new RegisterUserPage(browserDriver);
    }
}
