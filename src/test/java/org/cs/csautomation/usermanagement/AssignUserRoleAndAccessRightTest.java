/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import java.util.List;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class creates new user group by clicking on plus icon in the topmost
 * corner and assigns Role and Access Right to the created user group
 * 
 * @author CSAutomation Team
 *
 */
public class AssignUserRoleAndAccessRightTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private UserManagementPage          userManagementPage;
    private String                      assignUserRoleAndAccessRightSheet = "AssignRoleAndAccessRightSheet";
    private String                      winHandleBeforeSwitching;
    private TraversingForSettingsModule traversingForSettingsModule;
    private IUserManagementPopup        userManagementPopup;
    private CSPortalWidget              csPortalWidgetInstance;

    /**
     * This method creates new user group by clicking on plus icon in the
     * topmost corner and assigns Role and Access Right to the created user
     * group
     * 
     * @param userGroup contains the user group name
     * @param rolename contains the rolename
     * @param accessRightName contains the access right name
     * @param username contains the username
     * @param password contains the password
     * @param subfolderName contains the subfolder name
     */
    @Test(priority = 1, dataProvider = "assignRoleAndAccessRight")
    public void testAssignUserRoleAndAccessRight(String rolename,
            String accessRightName, String username, String password,
            String subfolderName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            createNewUserGroup();
            createUsernameAndPassword(username, password);
            addRoleAndAccessRight(rolename, accessRightName);
            verifyAddedroleAndAccessRight(accessRightName, subfolderName);
        } catch (Exception e) {
            CSLogger.info("Test Case failed" + e);
            Assert.fail("Test Case failed.");
        }
    }

    /**
     * This method creates new user group
     */
    private void createNewUserGroup() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkUsers();
        clkCreateNewButton();
    }

    /**
     * This method clicks Users tab
     */
    private void clkUsers() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUsersNode()));
        userManagementPage.getUsersNode().click();
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUsersNode()));
        userManagementPage.getUsersNode().click();
        CSLogger.info("Clicked on Users node");
    }

    /**
     * This method clicks on the create new button
     */
    private void clkCreateNewButton() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getBtnCreateNew()));
        userManagementPage.getBtnCreateNew().click();
        CSLogger.info("Clicked on the create new button ");
    }

    /**
     * This method creates username and password in the administration tab
     * 
     * @param username contains the username in the string format
     * @param password contains the password in the string format
     */
    private void createUsernameAndPassword(String username, String password) {
        winHandleBeforeSwitching = browserDriver.getWindowHandle();
        clkAdministrationTab(username, password);
    }

    /**
     * This method clicks on Administration tab to enter username and password
     * 
     * @param username contains the username in the String format
     * @param password contains the password in the String format
     */
    private void clkAdministrationTab(String username, String password) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getAdministrationTab()));
        userManagementPage.getAdministrationTab().click();
        enterUsername(username);
        enterPassword(password);
        saveContents();
        handlePopupToConfirmPassword(password);
    }

    /**
     * This method saves contents by clicking on save button
     */
    private void saveContents() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
        userManagementPage.clkBtnSave();
        CSLogger.info("Clicked on Save button ");
    }

    /**
     * This method enters username in the administration tab in the username
     * field
     * 
     * @param username contains username
     */
    private void enterUsername(String username) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getTxtUsername()));
        clearTxtField(userManagementPage.getTxtUsername());
        userManagementPage.getTxtUsername().sendKeys(username);
        CSLogger.info("Entered username");
    }

    /**
     * This method enters password in the administration tab in password field
     * 
     * @param password contains password
     */
    private void enterPassword(String password) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getTxtPassword()));
        clearTxtField(userManagementPage.getTxtPassword());
        userManagementPage.getTxtPassword().sendKeys(password);
        CSLogger.info("Entered password");
    }

    /**
     * This method clears text username and password field
     * 
     * @param element contains the field to be cleared
     */
    private void clearTxtField(WebElement element) {
        element.sendKeys(Keys.DELETE);
        element.clear();
    }

    /**
     * This method handles the pop up of confirm password field
     * 
     * @param password contains the password in the string format
     */
    private void handlePopupToConfirmPassword(String password) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPopup.getUserInput()));
        userManagementPopup.getUserInput().sendKeys(password);
        clkOk();
        browserDriver.switchTo().window(winHandleBeforeSwitching);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
    }

    /**
     * This method adds Role and Access Right
     * 
     * @param rolename contains the role name
     * @param accessRightName contains the access right name
     */
    private void addRoleAndAccessRight(String rolename,
            String accessRightName) {
        clkRightsTab(rolename);
        traverseToFormLabelAdd(rolename);
    }

    /**
     * This method clicks on Rights tab
     * 
     * @param rolename constains the role name
     */
    private void clkRightsTab(String rolename) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getRightsTab()));
        userManagementPage.getRightsTab().click();
        CSLogger.info("Clicked on the Rights tab");
    }

    /**
     * This method traverses upto label Add to add Role and Access Right
     */
    private void traverseToFormLabelAdd(String rolename) {
        winHandleBeforeSwitching = browserDriver.getWindowHandle();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFrameSettings()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getFormLabelAdd()));
        userManagementPage.getFormLabelAdd().click();
        CSLogger.info("Clicked on Add ");
        assignRoleAndRight(rolename);
    }

    /**
     * This method assigns Role and AccessRight to the newly created user group
     * 
     * @param rolename contains the role name
     */
    private void assignRoleAndRight(String rolename) {
        for (String winHandle : browserDriver.getWindowHandles()) {
            browserDriver.switchTo().window(winHandle);
        }
        Select role = new Select(browserDriver
                .findElement(By.xpath("//select[contains(@name,'RoleID')]")));
        role.selectByVisibleText(rolename);
        Select right = new Select(browserDriver
                .findElement(By.xpath("//select[contains(@name,'RubricID')]")));
        List<WebElement> list = right.getOptions();
        right.selectByIndex(list.size() - 2);
        clkOk();
        browserDriver.switchTo().window(winHandleBeforeSwitching);
        clkSave();
    }

    /**
     * This method clicks on Ok of pop up
     */
    private void clkOk() {
        userManagementPopup.clkOkOfPopup(waitForReload);
        CSLogger.info("Clicked on OK of pop up");
    }

    /**
     * This method clicks on save button
     */
    private void clkSave() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        userManagementPage.clkBtnSave();
        CSLogger.info("Clicked on save button ");
    }

    /**
     * This method verifies if Role and Access Right has been assigned
     * successfully to the user group
     * 
     * @param accessRightName contains the access right name that has been
     *            assigned
     * @param subfolderName contains the subfolder name that has been assigned
     */
    private void verifyAddedroleAndAccessRight(String accessRightName,
            String subfolderName) {
        try {
            String verifyRights = traverseToAssignedAccessRight(accessRightName,
                    subfolderName);
            if (verifyRights.contains(accessRightName)
                    && (verifyRights.contains(subfolderName))) {
                CSLogger.info(
                        "Access rights have been assigned successfully to the created user");
                Assert.assertEquals(accessRightName + "/" + subfolderName,
                        verifyRights);
            } else {
                Assert.fail(
                        "Could not verify assigned access rights to the created user ");
            }
        } catch (Exception e) {
            CSLogger.info(
                    "Could not verify assigned access rights to the created user");
        }
    }

    /**
     * This method traverses to the assigned access right to user group for
     * verification
     * 
     * @param accessRight contains access right that has been assigned
     * @param assignedSubfolder contains name of subfolder that has been
     *            assigned
     * @return verifyRights It contains assigned access right and subfolder in
     *         the string format
     */
    private String traverseToAssignedAccessRight(String accessRight,
            String assignedSubfolder) {
        CSUtility.switchToDefaultFrame(browserDriver);
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFrameSettings()));
        WebElement element = browserDriver.findElement(
                By.xpath("//html//body//table/tbody/tr[2]/td[3]/a"));
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        String verifyRights = element.getText();
        return verifyRights;
    }

    /**
     * This data provider returns the data in the sheet
     * assingUserRoleAndAccessRight which contains role name, access right
     * name,username , password and subfolder name that has to be assigned to
     * the user group
     * 
     * 
     * @return
     */
    @DataProvider(name = "assignRoleAndAccessRight")
    public Object[] CreateUserGroupAndUser() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                assignUserRoleAndAccessRightSheet);
    }

    /**
     * This method initializes all the resources used to drive the use case
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
