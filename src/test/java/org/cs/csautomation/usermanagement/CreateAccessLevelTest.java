/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
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
 * This class creates new access level under access rights tab and two sub
 * folders under newly created access level
 * 
 * @author CSAutomation Team
 *
 */
public class CreateAccessLevelTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private UserManagementPage          userManagementPage;
    private String                      createAccessLevelSheet = "CreateAccessLevelSheet";
    private IUserManagementPopup        userManagementPopup;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSPortalWidget              csPortalWidgetInstance;

    /**
     * This test case creates new access level and two subfolders under newly
     * created access level
     * 
     * @param accessLevelName contains the name of the access level
     * @param subfolder contains the name of the subfolder
     */
    @Test(priority = 1, dataProvider = "createAccessLevel")
    public void testCreateAccessLevel(String accessLevelName,
            String subfolder) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            String[] subfolderNames = subfolder.split(",");
            createAccessLevel(accessLevelName);
            for (int i = 0; i < subfolderNames.length; i++) {
                createSubfolder(accessLevelName, subfolderNames[i]);
                TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                        waitForReload, browserDriver, locator);
                csPortalWidgetInstance
                        .clkOnSystemPreferencesIcon(waitForReload);
                traversingForSettingsModule
                        .traverseToUserManagement(waitForReload, browserDriver);
                userManagementPage.clkUserManagement(waitForReload);
                clkAccessRights();
                WebElement levelName = browserDriver
                        .findElement(By.linkText(accessLevelName));
                verifyCreatedChild(levelName, subfolderNames[i]);
            }
        } catch (Exception e) {
            CSLogger.info("Failed to execute test case " + e);
        }
    }

    /**
     * This method creates access level
     * 
     * @param accessLevelName contains the name of the access level
     */
    private void createAccessLevel(String accessLevelName) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        traversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkAccessRights();
        clkCreateAccessLevel(accessLevelName);
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
     * This method clicks on create new access level option
     * 
     * @param accessLevelName contains the name of the access level to be
     *            created
     */
    private void clkCreateAccessLevel(String accessLevelName) {
        rightClkElement(userManagementPage.getAccessRightsNode());
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupNewAccessLevel(), browserDriver);
        CSLogger.info("Clicked on create new access level ");
        userManagementPopup.handlePopup(waitForReload, accessLevelName,
                browserDriver);
        userManagementPopup.clkOkOfPopup(waitForReload);
        verifyCreatedAccessLevel(accessLevelName);
    }

    /**
     * This method verifies access level has been created or not
     * 
     * @param accessLevelName contains the name of the created access level
     */
    private void verifyCreatedAccessLevel(String accessLevelName) {
        try {
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkAccessRights();
            WebElement levelName = browserDriver
                    .findElement(By.linkText(accessLevelName));
            Assert.assertEquals(accessLevelName, levelName.getText());
        } catch (Exception e) {
            CSLogger.info("Verification failed for created access level");
        }
    }

    /**
     * This method creates subfolder under new access level
     * 
     * @param accessLevelName contains the name of the access level
     * @param subfolder contains the name of the subfolder to be created
     */
    private void createSubfolder(String accessLevelName, String subfolder) {
        traversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkAccessRights();
        WebElement levelName = browserDriver
                .findElement(By.linkText(accessLevelName));
        rightClkElement(levelName);
        clkNewChild(subfolder);
    }

    /**
     * This method right clicks on webelement
     * 
     * @param levelName contains the name of the access level
     */
    private void rightClkElement(WebElement levelName) {
        CSUtility.rightClickTreeNode(waitForReload, levelName, browserDriver);
        CSLogger.info("Right clicked on created level");
    }

    /**
     * This method clicks on new child option in pop up for creating new child
     * under new access level
     * 
     * @param subfolder contains the name of the subfolder to be created
     */
    private void clkNewChild(String subfolder) {
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupNewChild(), browserDriver);
        CSLogger.info("Clicked on new child option ");
        userManagementPopup.handlePopup(waitForReload, subfolder,
                browserDriver);
        userManagementPopup.clkOkOfPopup(waitForReload);
    }

    /**
     * This method verifies the created child under access level
     * 
     * @param levelName contains name of newly created access level as
     *            WebElement
     * @param subfolder contains teh name of the subfolder that has been created
     *            under access level
     */
    private void verifyCreatedChild(WebElement levelName, String subfolder) {
        waitForReload.until(ExpectedConditions.visibilityOf(levelName));
        levelName.click();
        WebElement createdSubfolder = browserDriver
                .findElement(By.linkText(subfolder));
        Assert.assertEquals(subfolder, createdSubfolder.getText());
    }

    /**
     * This data provider returns sheet data which contains access level name
     * and subfolders to be created under access level
     * 
     * @return createAccessLevelSheet
     */
    @DataProvider(name = "createAccessLevel")
    public Object[] UploadFileTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                createAccessLevelSheet);
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
    }
}