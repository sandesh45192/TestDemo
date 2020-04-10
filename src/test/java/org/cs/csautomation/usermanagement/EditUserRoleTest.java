/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class edits the user role that has been created in the test case
 * 
 * @author CSAutomation Team
 *
 */
public class EditUserRoleTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private UserManagementPage   userManagementPage;
    private String               editRoleSheet = "EditRoleSheet";
    private IUserManagementPopup userManagementPopup;
    private CSPortalWidget       csPortalWidgetInstance;
    private FrameLocators        iframeLocatorsInstance;

    /**
     * This test case creates and edits the user role
     * 
     * @param roleName contains the name of the user role
     */
    @Test(priority = 1, dataProvider = "editUserRole")
    public void testEditUserRole(String roleName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            performUseCase(roleName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method performs the use case of creating and editing the user role
     * 
     * @param roleName
     */
    private void performUseCase(String roleName) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkRoles();
        editRole(roleName);
    }

    /**
     * This method creates new role
     * 
     * @param roleName contains name of Role to be created
     */
    private void editRole(String roleName) {
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
            CSLogger.debug(
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
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        enableCheckbox();
        createAndEditRole(roleName);
    }

    /**
     * This method edits Role name
     * 
     * @param roleName contains the name of Role to be edited as String
     */
    private void createAndEditRole(String roleName) {
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(userManagementPage.getTxtEditRoleName()));
            userManagementPage.getTxtEditRoleName().click();
            userManagementPage.getTxtEditRoleName().clear();
            userManagementPage.getTxtEditRoleName().sendKeys(roleName);
            saveContents(roleName);
        } catch (Exception e) {
            CSLogger.debug("Could not edit role name " + e);
        }
    }

    /**
     * This method saves contents i.e entered role name and checkboxes that have
     * been enabled
     * 
     * @param roleName contains the name of created Role to be saved
     */
    private void saveContents(String roleName) {
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(userManagementPage.getBtnSave()));
            userManagementPage.getBtnSave().click();
            CSLogger.info(
                    "Clicked on save button to save rolename and checkboxes.");
            refreshRoles(roleName);
        } catch (Exception e) {
            Assert.fail("Could  not perform functionality of save button", e);
        }
    }

    /**
     * This method refreshes the created Role
     * 
     * @param roleName contains the name of the created Role
     */
    private void refreshRoles(String roleName) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
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
    }

    /**
     * This method enables checkbox of entering Role name if disabled
     */
    private void enableCheckbox() {
        try {
            WebElement checkBox = waitForReload.until(ExpectedConditions
                    .visibilityOf(userManagementPage.getEditRoleCheckbox()));
            if (!checkBox.isSelected()) {
                userManagementPage.getEditRoleCheckbox().click();
                CSLogger.info("Checkbox has enabled ");
            } else {
                CSLogger.info("Checkbox is already enabled ");
            }
        } catch (Exception e) {
            CSLogger.debug("Could not check if role is editable or not" + e);

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
            CSLogger.debug(
                    "Unable to click on Roles node under User Management");
        }
    }

    /**
     * This data provider returns the data in a sheet which contains the name of
     * the role
     * 
     * @return editRoleSheet
     */
    @DataProvider(name = "editUserRole")
    public Object[] editUserRole() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                editRoleSheet);
    }

    /**
     * This method initializes resources to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
    }
}
