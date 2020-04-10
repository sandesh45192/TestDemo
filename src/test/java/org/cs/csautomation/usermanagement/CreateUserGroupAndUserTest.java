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
 * This class creates user group , user and assigns already created access
 * rights and roles to the created user group
 * 
 * @author CSAutomation Team
 *
 */
public class CreateUserGroupAndUserTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private UserManagementPage   userManagementPage;
    private String               createUserGroupAndUserSheet = "CreateUserGroupAndUserSheet";
    private IUserManagementPopup userManagementPopup;
    private String               winHandleBeforeSwitching;
    private CSPortalWidget       csPortalWidgetInstance;

    /**
     * This method creates user group, user and assigns already created access
     * rights to the created user group
     * 
     * @param userGroup contains the name of the user group
     * @param roleName contains the name of the role to be assigned to the user
     *            group
     * @param accessRight contains the access rights that has to be assigned to
     *            the user group
     * @param username contains username
     * @param password contains password
     * @param assignedSubfolder contains subfolder to be assigned to the created
     *            user
     */
    @Test(priority = 1, dataProvider = "userGroupAndUser")
    public void testCreateUserGroupAndUser(String userGroup, String roleName,
            String accessRight, String username, String password,
            String assignedSubfolder) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            createUserGroup(userGroup);
            AddRoleAndAccessRight(userGroup, roleName, accessRight);
            createUsernameAndPassword(userGroup, username, password);
            verifyAddedRightsToUserGroup(username, password, accessRight,
                    assignedSubfolder);
        } catch (Exception e) {
            CSLogger.debug("Failed to execute test case", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method creates the new user group
     * 
     * @param userGroup contains the name of the user group to be created
     */
    private void createUserGroup(String userGroup) {
        clkUsers();
        clkNewGroupOption(userGroup);
    }

    /**
     * This method clicks Users tab
     */
    private void clkUsers() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUsersNode()));
        userManagementPage.getUsersNode().click();
        CSLogger.info("Clicked on Users node");
    }

    /**
     * This method clicks on new group option to create the new user group
     * 
     * @param userGroup contains the name of the user group to be created
     */
    private void clkNewGroupOption(String userGroup) {
        rightClkElement(userManagementPage.getUsersNode());
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupNewGroup(), browserDriver);
        userManagementPopup.handlePopup(waitForReload, userGroup,
                browserDriver);
        userManagementPopup.clkOkOfPopup(waitForReload);
        verifyCreatedUserGroup(userGroup);
    }

    /**
     * This method performs the right click on the element
     * 
     * @param usersNode contains the user node
     */
    private void rightClkElement(WebElement usersNode) {
        CSUtility.rightClickTreeNode(waitForReload, usersNode, browserDriver);
        CSLogger.info("Right clicked on Node ");
    }

    /**
     * This method verifies if the user group has been created or not
     * 
     * @param userGroup contains the name of the created user group
     */
    private void verifyCreatedUserGroup(String userGroup) {
        try {
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkUsers();
            WebElement createdUserGroup = browserDriver
                    .findElement(By.linkText(userGroup));
            Assert.assertEquals(userGroup, createdUserGroup.getText());
            CSLogger.info("Created user group present ");
        } catch (Exception e) {
            CSLogger.info("Verification failed for created user group");
        }
    }

    /**
     * This method adds role and access rights to the created user group
     * 
     * @param userGroup contains the user group name
     * @param roleName contains the name of the role
     * @param accessRight contains the name of the access right to be assigned
     *            to the created user group
     */
    private void AddRoleAndAccessRight(String userGroup, String roleName,
            String accessRight) {
        WebElement createdUserGroup = traverseUptoCreatedUserGroup(userGroup);
        editUserGroup(createdUserGroup);
        traverseUptoRightsTab();
        addRoleAndRight(roleName, accessRight);

    }

    /**
     * This method traverses upto the created user group
     * 
     * @param userGroup contains the name of the user group
     * @return createdUserGroup
     */
    private WebElement traverseUptoCreatedUserGroup(String userGroup) {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkUsers();
        WebElement createdUserGroup = browserDriver
                .findElement(By.linkText(userGroup));
        return createdUserGroup;
    }

    /**
     * This method edits the created user group
     * 
     * @param createdUserGroup contains name of the user group in web element
     *            form
     */
    private void editUserGroup(WebElement createdUserGroup) {
        CSUtility.rightClickTreeNode(waitForReload, createdUserGroup,
                browserDriver);
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
     * This method traverses upto Rights tab
     */
    private void traverseUptoRightsTab() {
        traverseToMainFrame();
        clkRightsTab();
        traverseToFormLabelAdd();
    }

    /**
     * This method clicks on the rights tab
     */
    private void clkRightsTab() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getRightsTab()));
        userManagementPage.getRightsTab().click();
        CSLogger.info("Clicked on the Rights tab");
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
     * This method traverses upto label Add to add Role and Access Right
     */
    private void traverseToFormLabelAdd() {
        winHandleBeforeSwitching = browserDriver.getWindowHandle();
        traverseToMainFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFrameSettings()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getFormLabelAdd()));
        userManagementPage.getFormLabelAdd().click();
    }

    /**
     * This method adds role and access right to the created user
     * 
     * @param roleName contains the role name
     * @param accessRight contains access right
     */
    private void addRoleAndRight(String roleName, String accessRight) {
        for (String winHandle : browserDriver.getWindowHandles()) {
            browserDriver.switchTo().window(winHandle);
        }
        Select role = new Select(browserDriver
                .findElement(By.xpath("//select[contains(@name,'RoleID')]")));
        role.selectByVisibleText(roleName);
        Select right = new Select(browserDriver
                .findElement(By.xpath("//select[contains(@name,'RubricID')]")));
        List<WebElement> list = right.getOptions();
        right.selectByIndex(list.size() - 1);
        clkOk();
        browserDriver.switchTo().window(winHandleBeforeSwitching);
        clkSave();
    }

    /**
     * This method clicks on save button
     */
    private void clkSave() {
        traverseToMainFrame();
        userManagementPage.clkBtnSave();
        CSLogger.info("Clicked on save button");
    }

    /**
     * This method creates new username and password in the administration tab
     * for created user group
     * 
     * @param userGroup contains the name of the user group
     * @param username contains username
     * @param password contains password
     */
    private void createUsernameAndPassword(String userGroup, String username,
            String password) {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
        WebElement createdUserGroup = traverseUptoCreatedUserGroup(userGroup);
        createdUserGroup.click();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        clkCreateNew();
        clkAdministrationTab(username, password);
    }

    /**
     * This method clicks on create new option
     */
    private void clkCreateNew() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getBtnCreateNew()));
        userManagementPage.getBtnCreateNew().click();
    }

    /**
     * This method clicks on administration tab
     * 
     * @param username contains username
     * @param password contains password
     */
    private void clkAdministrationTab(String username, String password) {
        traverseToMainFrame();
        traverseToEditFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getAdministrationTab()));
        userManagementPage.getAdministrationTab().click();
        enterUsername(username);
        enterPassword(password);
        saveContents();
        handlePopupToConfirmPassword(password);
    }

    /**
     * This method traverses to edit frame
     */
    private void traverseToEditFrame() {
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
    }

    /**
     * This method saves contents by clicking on save button
     */
    private void saveContents() {
        traverseToMainFrame();
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
        userManagementPage.clkBtnSave();

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
     * @param password
     */
    private void handlePopupToConfirmPassword(String password) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPopup.getUserInput()));
        userManagementPopup.getUserInput().sendKeys(password);
        clkOk();
    }

    /**
     * This method clicks on Ok of pop up
     */
    private void clkOk() {
        userManagementPopup.clkOkOfPopup(waitForReload);
        CSLogger.info("Clicked on OK of pop up");
    }

    /**
     * This method verifies added rights to the user group
     * 
     * @param username contains the username
     * @param password contains password
     * @param accessRight contains access right
     * @param assignedSubfolder contains name of subfolder to be assigned to the
     *            user group
     */
    private void verifyAddedRightsToUserGroup(String username, String password,
            String accessRight, String assignedSubfolder) {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            traverseToMainFrame();
            traverseToEditFrame();
            clkRightsTab();
            String verifyRights = traverseToAssignedAccessRight(accessRight,
                    assignedSubfolder);
            if (verifyRights.contains(accessRight)
                    && (verifyRights.contains(assignedSubfolder))) {
                CSLogger.info(
                        "Access rights have been assigned successfully to the created user");
                Assert.assertEquals(accessRight + "/" + assignedSubfolder,
                        verifyRights);
            } else {
                CSLogger.info(
                        "Could not verify assigned access rights to the created user ");
            }
        } catch (Exception e) {
            CSLogger.info(
                    "Could not verify assigned access rights to the created user");
            Assert.fail(
                    "Could not verify assigned access rights to the created user"
                            + e);
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
        traverseToMainFrame();
        traverseToEditFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFrameSettings()));
        WebElement element = browserDriver.findElement(
                By.xpath("//html//body//table/tbody/tr[2]/td[3]/a"));
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        String verifyRights = element.getText();
        return verifyRights;
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains user group , role name , access right name ,username , password
     * and subfolder name
     * 
     * @return createUserGroupAndUsersheet
     */
    @DataProvider(name = "userGroupAndUser")
    public Object[] CreateUserGroupAndUser() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                createUserGroupAndUserSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */

    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
    }
}
