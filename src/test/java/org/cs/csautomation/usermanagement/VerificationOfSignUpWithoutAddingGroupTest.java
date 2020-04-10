/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.settings.RegisterUserPage;
import org.cs.csautomation.cs.settings.SettingsPage;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class performs verification of sign up feature without adding user group
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfSignUpWithoutAddingGroupTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private TraversingForSettingsModule traversingForSettingsModule;
    private SettingsPage                settingsPage;
    private IProductPopup               productPopUp;
    private LoginPage                   loginPage;
    private UserManagementPage          userManagementPage;
    private IUserManagementPopup        userManagementPopup;
    private String                      winHandleBeforeSwitching;
    private RegisterUserPage            registerUserPage;
    private ChoosePortalPage            choosePortalPage;
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private String                      signUpWithoutAddingGroupSheet = "SignUpWithoutAddingGroupSheet";

    /**
     * This method performs verification of sign up feature without adding user
     * group
     * 
     * @param username contains user name
     * @param password contains password
     * @param language contains language
     * @param userGroup contains user group name
     * @param testRole contains test role name
     * @param newUserGroup contains new user gruop
     * @param mode contains mode
     * @param mail contains mail address
     * @param registerMail contains registered mail address
     * @param registerPassword contains password
     */
    @Test(priority = 1, dataProvider = "signUpWithoutAddingGroup")
    public void testVerificationOFSignUpWithoutAddingGroup(String username,
            String password, String language, String userGroup, String testRole,
            String mode, String mail, String registerMail,
            String registerPassword, String assignedRights) {
        try {
            logoutFromApplication();
            loginAsAdmin(username, password, language);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
            performUseCase(userGroup, username, password, language);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method performs log out from admin
     */
    private void logoutFromApplication() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getCsGuiPopupLogout()));
        productPopUp.getCsGuiPopupLogout().click();
        CSLogger.info("Logged out successfully ");
    }

    /**
     * This method logs in as admin
     * 
     * @param username contains user name
     * @param password contains password
     * @param language contains language
     */
    private void loginAsAdmin(String username, String password,
            String language) {
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
     * This method performs the user case by verification of sign up feature
     * without adding user group
     * 
     * @param userGroup contains user group name
     * @param username contains user name
     * @param password contains password
     * @param language contains language
     */
    private void performUseCase(String userGroup, String username,
            String password, String language) {
        clkSettingsInLeftPane();
        clkCore();
        clkBtnPortal(settingsPage.getBtnPortal());
        saveWithoutAddingUserGroup(userGroup);
        clkSave();
        logoutFromApplication();
        loginPage.clkSignUp(waitForReload);
        verifyMessage();
        loginAsAdmin(username, password, language);
        addUserGroup(userGroup);
        verifyPresenceOfUserGroup(userGroup);
        clkSave();
    }

    /**
     * This method adds user group
     * 
     * @param userGroup contains user group
     */
    private void addUserGroup(String userGroup) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
        clkSettingsInLeftPane();
        // clkCore();
        clkBtnPortal(settingsPage.getBtnPortal());
        clkPlusToAddUserGroup();
        clkUsersInLeftPane();
        doubleClickUserGroup(userGroup);
    }

    /**
     * This method clicks on user in left pane
     */
    private void clkUsersInLeftPane() {
        traverseToLeftRecordsUserFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(settingsPage.getUsersMenuInLeftPane()));
        settingsPage.getUsersMenuInLeftPane().click();
        CSLogger.info("Clicked on Users in left pane");
    }

    /**
     * This method traverses to left records user frame
     */
    private void traverseToLeftRecordsUserFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmTabbedPane()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmRecordsUserLeft()));
    }

    /**
     * This method clicks on save button
     */
    private void clkSave() {
        waitForReload.until(
                ExpectedConditions.visibilityOf(settingsPage.getBtnSave()));
        settingsPage.getBtnSave().click();
        CSLogger.info("Clicked on Save button");
    }

    /**
     * This method clicks on settings in left pane
     */
    private void clkSettingsInLeftPane() {
        traversingForSettingsModule
                .traverseToNodesInLeftPaneOfSystemPreferences(waitForReload,
                        browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(settingsPage.getBtnSettingsNode()));
        settingsPage.getBtnSettingsNode().click();
        CSLogger.info("Clicked on Settings node in left section");
    }

    /**
     * This method clicks on core button
     */
    private void clkCore() {
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        settingsPage.clkOnBtnCore(waitForReload);
    }

    /**
     * This method clicks on portal button under core
     */
    private void clkBtnPortal(WebElement element) {
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        settingsPage.getBtnPortal().click();
        CSLogger.info("Clicked on Portal button");
    }

    /**
     * This method clicks save without adding user group
     * 
     * @param userGroup contains user group
     */
    private void saveWithoutAddingUserGroup(String userGroup) {
        try {
            WebElement verifyAbsenceOfUserGroup = browserDriver.findElement(
                    By.xpath(" //table[@class='CSGuiTable']/tbody/tr/td[2]"));
            if (verifyAbsenceOfUserGroup.getText().equals(userGroup)) {
                CSLogger.info("User group should  not be present");
            }
        } catch (Exception e) {
            CSLogger.info("UserGroup is not present. Verified");
        }
    }

    /**
     * This method clicks on plus
     */
    private void clkPlusToAddUserGroup() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getBtnPlusToAddUserGroup()));
        userManagementPage.getBtnPlusToAddUserGroup().click();
    }

    /**
     * This method double clicks on user group
     * 
     * @param userGroup contains user group
     */
    private void doubleClickUserGroup(String userGroup) {
        traversesToDataSelection();
        WebElement assignUserGroup = browserDriver.findElement(By.xpath(
                ("(//table[@class='treeline']/tbody/tr/td[2]/a/span)[5]")));
        Actions action = new Actions(browserDriver);
        action.doubleClick(assignUserGroup).build().perform();
        CSLogger.info("Double clicked on User group to add");
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        clkSave();
    }
    
    /**
     * This method verifies presence of user group
     * 
     * @param userGroup contains user group
     */
    private void verifyPresenceOfUserGroup(String userGroup) {
        try {
            WebElement verifyUserGroup = browserDriver.findElement(
                    By.xpath("//table[@class='CSGuiTable']/tbody/tr/td[2]"));
            Assert.assertEquals(userGroup, verifyUserGroup.getText());
            CSLogger.info("User group is present");
        } catch (Exception e) {
            CSLogger.debug("Verification failed", e);
            Assert.fail("Verification failed", e);
        }
    }

    /**
     * This method verifies message after clicking sign up link
     */
    private void verifyMessage() {
        try {
            Alert alertBox = CSUtility.getAlertBox(waitForReload,
                    browserDriver);
            String alertMessage = alertBox.getText();
            Assert.assertEquals(alertMessage,
                    "Registration is not possible at the moment: Standard user "
                            + "group is not configured properly.");
            alertBox.accept();
            CSLogger.info("Message verified");
        } catch (Exception e) {
            CSLogger.debug("Verification failed.", e);
            softAssert.fail("Could not accept alert box", e);
        }
    }

    /**
     * This method logs in via registered user
     * 
     * @param username contains user name
     * @param password contains password
     * @param language contains language
     * @param userGroup contains user group
     * @param testRole contains role name
     * @param newUserGroup contains new user group name
     * @param mode contains mode
     * @param mail contains mail address
     * @param registerMail contains registered mail address
     * @param registerPassword contains password
     */
    @Test(priority = 2, dataProvider = "signUpWithoutAddingGroup")
    public void testVerifySignUpFeature(String username, String password,
            String language, String userGroup, String testRole, String mode,
            String mail, String registerMail, String registerPassword,
            String assignedRights) {
        try {
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            executePrerequisites(testRole, mode, mail);
            logoutFromApplication();
            loginPage.clkSignUp(waitForReload);
            winHandleBeforeSwitching = browserDriver.getWindowHandle();
            registerUser(registerMail, registerPassword);
            loginViaRegistrationLink(registerMail, registerPassword);
            logoutFromApplication();
            loginAsAdmin(username, password, language);
            verifyCreatedUserInUserGroup(userGroup, registerMail, language,
                    assignedRights);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method verifies created user group in the user group
     * 
     * @param userGroup contains user group
     * @param registerMail contains mail address
     * @param language contains language
     * @param rights contains rights
     */
    private void verifyCreatedUserInUserGroup(String userGroup,
            String registerMail, String language, String rights) {
        try {
            boolean status = false;
            WebElement userElement = null;
            WebElement createdUserGroup = traverseUptoCreatedUserGroup(
                    userGroup);
            waitForReload
                    .until(ExpectedConditions.visibilityOf(createdUserGroup));
            createdUserGroup.click();
            createdUserGroup.click();
            TraversingForSettingsModule
                    .traverseToFrameOfSettingsSplitAreaFrameMain(waitForReload,
                            browserDriver, locator);
            List<WebElement> usersList = browserDriver.findElements(By.xpath(
                    "//table[@class='hidewrap CSAdminList']/tbody/tr/td[4]"));
            for (Iterator<WebElement> iterator = usersList.iterator(); iterator
                    .hasNext();) {
                userElement = (WebElement) iterator.next();
                String user = userElement.getText();
                if (user.equals(registerMail)) {
                    status = true;
                    break;
                }
            }
            if (status == true) {
                CSLogger.info("User has been registered");
                verifyTabContentsOfRegisteredUser(userElement, userGroup,
                        registerMail, language, rights);
            } else {
                CSLogger.info("Unable to register user");
            }
        } catch (Exception e) {
            CSLogger.error("Unable to register user" + e);
            Assert.fail("Unable to register user", e);
        }
    }

    /**
     * This method verifies contents in overview tab, administration tab and
     * rights tab
     * 
     * @param userElement contains webElement
     * @param userGroup contains user group
     * @param registerMail contains mail address
     * @param language contains language
     * @param rights contains rights
     */
    private void verifyTabContentsOfRegisteredUser(WebElement userElement,
            String userGroup, String registerMail, String language,
            String rights) {
        waitForReload.until(ExpectedConditions.visibilityOf(userElement));
        userElement.click();
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        traversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        verifyOverviewTabContents(registerMail);
        verifyAdministrationTabContents(language);
        verifyAccessRightsFromRightsTab(rights);
    }

    /**
     * This method verifies access rights assigned to user group from rights tab
     * 
     * @param rights contains assigned right
     */
    private void verifyAccessRightsFromRightsTab(String rights) {
        userManagementPage.clkRightsTab(waitForReload);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFrameSettings()));
        WebElement userRights = browserDriver.findElement(
                By.xpath("//html//body//table/tbody/tr[2]/td[3]/a"));
        String verifyRights = userRights.getText();
        if (verifyRights.contains(rights)) {
            CSLogger.info("Access Rights are present");
        } else {
            CSLogger.info("Access rights are not present");
        }
    }

    private void verifyOverviewTabContents(String registerMail) {
        try {
            userManagementPage.clkOverviewTab(waitForReload);
            String username = userManagementPage.getUsernameOverviewTab()
                    .getText();
            String user = username.replace("User Name: ", "");
            Assert.assertEquals(registerMail, user);
            CSLogger.info("Registered mail is present in overview tab");
        } catch (Exception e) {
            CSLogger.debug("Could not verify registered mail in overview tab");
            Assert.fail("Could not verify registered mail in overview tab", e);
        }
    }

    private void verifyAdministrationTabContents(String language) {
        try {
            userManagementPage.clkAdministrationTab(waitForReload);
            CSUtility.tempMethodForThreadSleep(2000);
            Select getUserLanguage = new Select(browserDriver.findElement(
                    By.xpath("//select[contains(@id,'UserLanguage')]")));
            String userLanguage = getUserLanguage.getFirstSelectedOption()
                    .getText();
            Assert.assertEquals(language, userLanguage);
        } catch (Exception e) {
            CSLogger.debug("Could not verify language in adminitration tab");
            Assert.fail("Could not verify language in adminitration tab", e);
        }
    }

    /**
     * This method logs in via registration link
     * 
     * @param registerMail contains mail address
     * @param registerPassword contains password
     */
    private void loginViaRegistrationLink(String registerMail,
            String registerPassword) {
        // code won't work if sleep has removed. Switching to different widows
        // takes time
        CSUtility.tempMethodForThreadSleep(3000);
        for (String winHandle : browserDriver.getWindowHandles()) {
            browserDriver.switchTo().window(winHandle);
        }
        CSUtility.tempMethodForThreadSleep(3000);
        WebElement getActivationLink = browserDriver.findElement(
                By.xpath("//html//body//table/tbody/tr[7]/td/pre/p/a"));
        waitForReload.until(ExpectedConditions.visibilityOf(getActivationLink));
        getActivationLink.click();
        CSLogger.info("Clicked on activation link");
        CSUtility.tempMethodForThreadSleep(5000);
        ArrayList<String> newTab1 = new ArrayList<String>(
                browserDriver.getWindowHandles());
        CSUtility.tempMethodForThreadSleep(2000);
        browserDriver.switchTo().window(newTab1.get(2));
        CSUtility.tempMethodForThreadSleep(5000);
        loginWithNewlyRegisteredUser(registerMail, registerPassword);
        acceptTermsOfUse();
        CSUtility.tempMethodForThreadSleep(2000);
        loginPage.clkLoginButton();
    }

    /**
     * This method clicks on Terms of use while logging in in with new user
     */
    private void acceptTermsOfUse() {
        choosePortalPage.clkBtnAccept();
    }

    /**
     * This method logs in with newly registered user
     * 
     * @param registerMail contains mail
     * @param password contains password
     */
    private void loginWithNewlyRegisteredUser(String registerMail,
            String password) {
        CSUtility.switchToDefaultFrame(browserDriver);
        loginPage.enterUsername(registerMail);
        loginPage.enterPassword(password);
        loginPage.clkLoginButton();
    }

    /**
     * This method registers user with given mail and password
     * 
     * @param registerMail contains mail address
     * @param registerPassword contains password
     */
    private void registerUser(String registerMail, String registerPassword) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(registerUserPage.getTxtRegisterMailInput()));
        registerUserPage.getTxtRegisterMailInput().sendKeys(registerMail);
        waitForReload.until(ExpectedConditions
                .visibilityOf(registerUserPage.getTxtPassword()));
        registerUserPage.getTxtPassword().sendKeys(registerPassword);
        clkRegister();
    }

    /**
     * This method clicks on Register button
     */
    private void clkRegister() {
        registerUserPage.clkBtnRegister(waitForReload);
        handleAlert();
    }

    /**
     * This method handles alert
     */
    private void handleAlert() {
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        CSLogger.info("Clicked on ok of pop up");
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method executes the pre-requisites to drive the test case
     * 
     * @param testRole contains role name
     * @param newUserGroup contains new user group name as String
     * @param mode contains mode
     * @param mail contains mail address
     */
    private void executePrerequisites(String testRole, String mode,
            String mail) {
        clkRoles();
        createNewRole(testRole);
        CSUtility.tempMethodForThreadSleep(2000);
        traverseToCreatedRole(testRole);
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        clkCore();
        enableCreatePortalCheckbox();
        enableFewRights();
        setDebugModeInSettings(mode);
        deactivateRegistrationLinkCheckbox();
        verifyNotificationMailAddress(mail);
    }

    /**
     * This method verifies notification mail address
     * 
     * @param mail contains mail
     */
    private void verifyNotificationMailAddress(String mail) {
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement testEmail = browserDriver.findElement(By.xpath(
                "//input[contains(@id,'Optionproxyportal:RegistrationNotificationEmail')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(testEmail));
        String value = testEmail.getAttribute("value");
        if (value != null) {
            CSLogger.info("Field is not empty");
        } else {
            testEmail.sendKeys(mail);
        }
    }

    /**
     * This method deactivates registration link checkbox if checked
     */
    private void deactivateRegistrationLinkCheckbox() {
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        clkCore();
        clkBtnPortal(settingsPage.getBtnPortal());
        WebElement registrationLinkCheckbox = waitForReload
                .until(ExpectedConditions.visibilityOf(
                        settingsPage.getHideRegistrationLinkCheckbox()));
        String valueOfRegistrationLink = registrationLinkCheckbox
                .getAttribute("class");
        if (!valueOfRegistrationLink.equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox")) {
            registrationLinkCheckbox.click();
        } else {
            CSLogger.info(
                    "Hide registration link checkbox is already disabled");
        }
    }

    /**
     * This method sets mode option to debug mode in Settings
     * 
     * @param mode contains mode
     */
    private void setDebugModeInSettings(String mode) {
        traversingForSettingsModule
                .traverseToNodesInLeftPaneOfSystemPreferences(waitForReload,
                        browserDriver);
        settingsPage.clkOnBtnSettingsNode(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        clkCore();
        waitForReload.until(ExpectedConditions
                .visibilityOf(settingsPage.getBtnEmailAndMessages()));
        settingsPage.getBtnEmailAndMessages().click();
        CSLogger.info("Clicked on Email And Messages");
        Select selectMode = new Select(browserDriver.findElement(
                By.xpath("(//select[@class='GuiEditorInput '])[5]")));
        selectMode.selectByVisibleText(mode);
        CSLogger.info("Selected mode");
    }

    /**
     * This method traverses upto the created user group
     * 
     * @param userGroup contains the name of the user group
     * @return createdUserGroup
     */
    private WebElement traverseUptoCreatedUserGroup(String userGroup) {
        traversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkUsers();
        WebElement createdUserGroup = browserDriver
                .findElement(By.linkText(userGroup));
        return createdUserGroup;
    }

    /**
     * This method enables create portal checkbox
     */
    private void enableCreatePortalCheckbox() {
        WebElement btnPortalUnderRole = browserDriver
                .findElement(By.xpath("(//div[contains(text(),'Portal')])[1]"));
        waitForReload
                .until(ExpectedConditions.visibilityOf(btnPortalUnderRole));
        btnPortalUnderRole.click();
        CSLogger.info("Clicked on Portal button under core in roles");
        WebElement createPortalCheckbox = browserDriver.findElement(
                By.xpath("//input[contains(@id,'Roleportal:create:portal')]"));
        if (!createPortalCheckbox.isSelected()) {
            createPortalCheckbox.click();
            CSLogger.info("Clicked on checkbox");
        } else {
            CSLogger.info("Checkbox is already enabled.");
        }
    }

    /**
     * This method enables few rights
     */
    private void enableFewRights() {
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement WidgetBetaSize = browserDriver.findElement(
                By.xpath("//input[contains(@id,'Roleportal:beta:widget')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(WidgetBetaSize));
        if (WidgetBetaSize.isSelected() == true) {
            WidgetBetaSize.click();
            clkSave();
        } else {
            CSLogger.info("Already disabled");
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
                    "Unable to click on Roles node under User Management");
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
     * This method enters role name and enables checkbox for assigning set of
     * roles
     * 
     * @param roleName contains the name of the Role to be created
     */
    private void enterRoleNameAndEnableCheckbox(String roleName) {
        traversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        enableCheckbox();
        editRoleName(roleName);
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
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
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
     * This method traverses to created role
     * 
     * @param testRole contains name of role as string
     */
    private void traverseToCreatedRole(String testRole) {
        traversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkRoles();
        WebElement createdRole = browserDriver
                .findElement(By.linkText(testRole));
        createdRole.click();
        CSLogger.info("Clicked on created role");
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
     * This method traverses to Data selection dialogue
     */
    public void traversesToDataSelection() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialog()));
        waitForReload.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(
                        "//iframe[@id='frm_8f9bfe9d1345237cb3b2b205864da075']")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsUserLeft()));
    }
    /**
     * This data provider returns sheet data which contains user name ,
     * password,language,user group,role name ,new user group name
     * ,debugMode,mailAddress,registeredMail ,password ,access right
     * 
     * @return signUpWithoutAddingGroupSheet
     */
    @DataProvider(name = "signUpWithoutAddingGroup")
    public Object[] verifyPassword() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                signUpWithoutAddingGroupSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        loginPage = new LoginPage(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        settingsPage = new SettingsPage(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = new CSPopupDivSettings(browserDriver);
        registerUserPage = new RegisterUserPage(browserDriver);
        choosePortalPage = new ChoosePortalPage(browserDriver);
        softAssert = new SoftAssert();
        csPortalWidget = new CSPortalWidget(browserDriver);
    }
}
