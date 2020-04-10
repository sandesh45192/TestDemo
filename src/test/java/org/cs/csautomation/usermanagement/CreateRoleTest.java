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
 * This class creates Role under User Management tab
 */
public class CreateRoleTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private UserManagementPage          userManagementPage;
    private String                      createRoleSheet = "CreateRoleSheet";
    private IUserManagementPopup        userManagementPopup;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSPortalWidget              csPortalWidgetInstance;

    /**
     * This method creates new role under User Management tab
     * 
     * @param roleName contains the name of Role to be created
     */
    @Test(priority = 1, dataProvider = "createRole")
    public void testCreateRole(String roleName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkRoles();
            createNewRole(roleName);
        } catch (Exception e) {
            CSLogger.info("Failed to execute the test case " + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method clicks on Roles
     */
    private void clkRoles() {
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(userManagementPage.getUserManagementRoles()));
            userManagementPage.getUserManagementRoles().click();
            CSLogger.info("Clicked on Roles under User Management");
        } catch (Exception e) {
            CSLogger.info(
                    "Unbale to click on Roles node under User Management");
        }
    }

    /**
     * This method creates new role
     * 
     * @param roleName contains name of Role to be created
     */
    private void createNewRole(String roleName) {
        clkCreateNew();
        enterRoleNameAndEnableCheckbox(roleName);
    }

    /**
     * This method clicks create new option for creating new Role
     */
    private void clkCreateNew() {
        try {
            rightClickRoles();
            userManagementPopup.selectPopupDivMenu(waitForReload,
                    userManagementPopup.getCsPopupCreateNew(), browserDriver);
            CSLogger.info("Clicked on create new option");
        } catch (Exception e) {
            CSLogger.info(
                    "Exception occured while clicking on create new option");
        }
    }

    /**
     * This method right clicks on Roles node
     */
    private void rightClickRoles() {
        CSUtility.rightClickTreeNode(waitForReload,
                userManagementPage.getUserManagementRoles(), browserDriver);
    }

    /**
     * This method enters role name and enables checkbox for assigning set of
     * roles
     * 
     * @param roleName contains the name of the Role to be created
     */
    private void enterRoleNameAndEnableCheckbox(String roleName) {
        traverseToMainFrame();
        enableCheckbox();
        editRoleName(roleName);
    }

    /**
     * This method traverses upto main frame
     */
    private void traverseToMainFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traversed upto main frame ");
    }

    /**
     * This method enables checkbox of entering Role name if disabled
     */
    private void enableCheckbox() {
        int status = 0;
        WebElement checkBox = waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getEditRoleCheckbox()));
        if (!checkBox.isSelected()) {
            status = 1;
        } else {
            status = 0;
        }
        if (status == 1) {
            userManagementPage.getEditRoleCheckbox().click();
            CSLogger.info("Checkbox has enabled ");
        }
        if (status == 0) {
            CSLogger.info("Checkbox is already enabled ");
        }
    }

    /**
     * This method edits Role name
     * 
     * @param roleName contains the name of Role to be edited
     */
    private void editRoleName(String roleName) {
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(userManagementPage.getTxtEditRoleName()));
            userManagementPage.getTxtEditRoleName().click();
            userManagementPage.getTxtEditRoleName().clear();
            userManagementPage.getTxtEditRoleName().sendKeys(roleName);
            saveContents(roleName);
        } catch (Exception e) {
            CSLogger.info("Could not edit role name " + e);
        }
    }

    /**
     * This method saves contents i.e entered role name and checkboxes that have
     * been enabled
     * 
     * @param roleName contains the name of created Role to be saved
     */
    private void saveContents(String roleName) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getBtnSave()));
        userManagementPage.getBtnSave().click();
        CSLogger.info(
                "Clicked on save button to save rolename and checkboxes.");
        refreshRoles(roleName);
    }

    /**
     * This method refreshes the created Role
     * 
     * @param roleName contains the name of the created Role
     */
    private void refreshRoles(String roleName) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        traversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        rightClickRoles();
        clkRefresh(roleName);
    }

    /**
     * This method clicks on refresh option of pop up
     * 
     * @param roleName contains the name of the Role
     */
    private void clkRefresh(String roleName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupRefresh(), browserDriver);
        CSLogger.info("Clicked on Refresh option of pop up");
        verifyCreatedRole(roleName);
    }

    /**
     * This method verifies if Role has created or not
     * 
     * @param roleName contains the name of the Role that has been created
     */
    private void verifyCreatedRole(String roleName) {
        try {
            CSUtility.tempMethodForThreadSleep(5000);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkRoles();
            WebElement createdRole = browserDriver
                    .findElement(By.linkText(roleName));
            Assert.assertEquals(roleName, createdRole.getText());
        } catch (Exception e) {
            CSLogger.info("Created Role is not present under Roles " + e);
        }
    }

    /**
     * This data provider returns sheet data which contains the name of the Role
     * to be created
     * 
     * @return createRoleSheet
     */
    @DataProvider(name = "createRole")
    public Object[] UploadFileTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                createRoleSheet);
    }

    /**
     * This method initializes resources to drive the test case
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
