/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class edits the created access level
 * 
 * @author CSAutomation Team
 *
 */
public class EditAccessLevelTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private UserManagementPage          userManagementPage;
    private String                      editAccessLevelSheet = "EditAccessLevelSheet";
    private IUserManagementPopup        userManagementPopup;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSPortalWidget              csPortalWidgetInstance;
    private LoginPage                   loginPage;

    /**
     * This method edits the already created access level
     * 
     * @param accessLevelName contains name of access level
     * @param editedName contains name of access level to be edited
     */
    @Test(priority = 1, dataProvider = "editAccessLevel")
    public void testEditAccessLevel(String accessLevelName, String editedName,
            String username, String password) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            loginAsAdmin(username, password);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            performUseCase(accessLevelName, editedName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    private void loginAsAdmin(String username, String password) {
        loginPage.getSelectedLanguage();
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        CSLogger.info("Entered username and password");
        loginPage.clkLoginButton();
        CSUtility.tempMethodForThreadSleep(2000);
        loginPage.clkLoginButton();
        CSLogger.info("Clicked on Login button ");
    }

    /**
     * This method edits the name of created access level and verifies the
     * edited name
     * 
     * @param accessLevelName contains name of access level
     * @param editedName contains name of access level to be edited
     */
    private void performUseCase(String accessLevelName, String editedName) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkAccessRights();
        rightClkCreatedAccessRights(accessLevelName);
        editName(editedName);
        verifyEditedName(editedName);
    }

    /**
     * This method clicks on Access Rights node
     */
    private void clkAccessRights() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getAccessRightsNode()));
        userManagementPage.getAccessRightsNode().click();
        CSLogger.info("Clicked on Access Rights node");
    }

    /**
     * This method right clicks on created access right
     * 
     * @param accessLevelName contains access level name
     */
    private void rightClkCreatedAccessRights(String accessLevelName) {
        WebElement accessLevel = browserDriver
                .findElement(By.linkText(accessLevelName));
        CSUtility.rightClickTreeNode(waitForReload, accessLevel, browserDriver);
    }

    /**
     * This method edits the access level name
     * 
     * @param editedName contains name to be edited
     */
    private void editName(String editedName) {
        try {
            clkEdit();
            traversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(userManagementPage.getTxtAccessLevelName()));
            userManagementPage.getTxtAccessLevelName().clear();
            userManagementPage.getTxtAccessLevelName().sendKeys(editedName);
            clkSave();
        } catch (Exception e) {
            CSLogger.debug("Could not edit the access level name", e);
        }
    }

    /**
     * This method clicks on save button
     */
    private void clkSave() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getBtnSave()));
        userManagementPage.getBtnSave().click();
        CSLogger.info("Clicked on Save button");
    }

    /**
     * This method clicks on edit option
     */
    private void clkEdit() {
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPopup.getCsPopupEdit()));
        userManagementPopup.getCsPopupEdit().click();
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Clicked on edit option");
    }

    /**
     * This method verifies the edited name
     * 
     * @param editedName contains the name of access level that has been edited
     */
    private void verifyEditedName(String editedName) {
        try {
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkAccessRights();
            WebElement editedAccessLevelName = browserDriver
                    .findElement(By.linkText(editedName));
            Assert.assertEquals(editedName, editedAccessLevelName.getText());
            CSLogger.info("Access level name successfully changed");
        } catch (Exception e) {
            CSLogger.debug("Verification failed " + e);
        }
    }

    /**
     * This data provider returns the sheet data which contains access level
     * name and access level name to be edited
     * 
     * @return editAccessLevelSheet
     */
    @DataProvider(name = "editAccessLevel")
    public Object[] editAccessLevel() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                editAccessLevelSheet);
    }

    /**
     * This method is used to initialize all the resources used to drive the
     * test case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        loginPage = new LoginPage(browserDriver);
    }
}
