/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import org.cs.csautomation.cs.skeletons.AbstractTest;
import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.settings.SettingsPage;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
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
 * This class performs forgot password functionality
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyForgotPasswordTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private TraversingForSettingsModule traversingForSettingsModule;
    private String                      verifyForgotPasswordSheet = "VerifyForgotPasswordSheet";
    private SettingsPage                settingsPage;
    private IProductPopup               productPopUp;
    private LoginPage                   loginPage;
    private UserManagementPage          userManagementPage;
    private IUserManagementPopup        userManagementPopup;
    private String                      winHandleBeforeSwitching;
    private ChoosePortalPage            choosePortalPage;
    private CSPortalWidget              csPortalWidgetInstance;

    /**
     * This test performs forgot password functionality
     * 
     * @param roleName contains role name
     * @param userName contains username
     * @param password contains password
     * @param wrongPasswordToLogin contains wrong password to login
     * @param emailInProfileTab contains email in profile tab
     * @param mode contains mode
     * @param wrongRecoveryMail contains wrong recovery mail
     * @param existingMailInSystem contains mail in system
     * @param language contains language
     */
    @Test(priority = 1, dataProvider = "verifyForgotPasswordWorks")
    public void testVerifyForgotPassword(String roleName, String userName,
            String password, String wrongPasswordToLogin,
            String emailInProfileTab, String mode, String wrongRecoveryMail,
            String existingMailInSystem, String language) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            CSUtility.tempMethodForThreadSleep(2000);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            // execute pre-requisites required to drive the test case
            executePre_Requisites(roleName, userName, password,
                    emailInProfileTab, mode);
            // execute test case
            logoutFromApplication();
            loginWithWrongPassword(userName, wrongPasswordToLogin);
            handleForgotPasswordLink(wrongRecoveryMail, emailInProfileTab,
                    existingMailInSystem);
            loginWithGeneratedPassword(userName, language);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method logs in with generated password
     * 
     * @param userName contains username
     * @param language contains language
     */
    private void loginWithGeneratedPassword(String userName, String language) {
        String generatedPassword = null;
        WebElement getPassword = browserDriver.findElement(
                By.xpath("//html//body//table/tbody/tr[7]/td/pre/p[3]"));
        waitForReload.until(ExpectedConditions.visibilityOf(getPassword));
        String password = (getPassword.getText()).split("\n")[1];
        if (password.contains("Password: ")) {
            generatedPassword = password.replace("Password: ", "");
        }
        loginWithNewPassword(userName, generatedPassword, language);
    }

    /**
     * This method logs in with the new password
     * 
     * @param userName contains user name in string format
     * @param generatedPassword contains generated password as string
     * @param language contains language
     */
    private void loginWithNewPassword(String userName, String generatedPassword,
            String language) {
        clkLinkInDebugOutputWindow();
        browserDriver.switchTo().window(winHandleBeforeSwitching);
        CSUtility.switchToDefaultFrame(browserDriver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(generatedPassword);

        CSUtility.tempMethodForThreadSleep(5000);
        loginPage.clkLoginButton();
        acceptTermsOfUse();
        CSUtility.tempMethodForThreadSleep(2000);
        loginPage.getSelectedLanguage();
        loginPage.clkLoginButton();
    }

    /**
     * This method clicks on Terms of use while loggin in with new user
     */
    private void acceptTermsOfUse() {
        choosePortalPage.clkBtnAccept();
    }

    /**
     * This method clicks link in debug output window
     */
    private void clkLinkInDebugOutputWindow() {
        WebElement applicationLink = browserDriver.findElement(
                By.xpath("//html//body//table/tbody/tr[7]/td/pre/p[5]/a"));
        waitForReload.until(ExpectedConditions.visibilityOf(applicationLink));
        applicationLink.click();
        CSLogger.info("Clicked on Application Link");
    }

    /**
     * This method executes all pre-requisites to drive the test case
     * 
     * @param roleName contains role name
     * @param userName contains user name
     * @param password contains password
     * @param email contains email
     * @param mode cotains mode
     */
    private void executePre_Requisites(String roleName, String userName,
            String password, String email, String mode) {
        createUserRole(roleName);
        createUser(userName, password);
        addRoleAndAccessRight(roleName, email);
        setDebugModeInSettings(mode);
        deactivateRegistrationLinkCheckbox();
    }

    /**
     * This method checks and deactivates registration link checkbox
     */
    private void deactivateRegistrationLinkCheckbox() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
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
     * This method clicks on core button
     */
    private void clkCore() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        settingsPage.clkOnBtnCore(waitForReload);
    }

    /**
     * This method click on portal button under core
     */
    private void clkBtnPortal(WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        settingsPage.getBtnPortal().click();
        CSLogger.info("Clicked on Portal button");
    }

    /**
     * This method sets mode to debug mode in Settings
     * 
     * @param mode contains mode
     */
    private void setDebugModeInSettings(String mode) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule
                .traverseToNodesInLeftPaneOfSystemPreferences(waitForReload,
                        browserDriver);
        settingsPage.clkOnBtnSettingsNode(waitForReload);
        clkCore();
        waitForReload.until(ExpectedConditions
                .visibilityOf(settingsPage.getBtnEmailAndMessages()));
        settingsPage.getBtnEmailAndMessages().click();
        CSLogger.info("Clicked on Email And Messages");
        Select selectMode = new Select(browserDriver.findElement(
                By.xpath("(//select[@class='GuiEditorInput '])[5]")));
        selectMode.selectByVisibleText(mode);
    }

    /**
     * This method add role and access right
     * 
     * @param roleName contains role name as String
     * @param email contains email as String
     */
    private void addRoleAndAccessRight(String roleName, String email) {
        clkRightsTab(roleName);
        traverseToFormLabelAdd(roleName, email);
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
    private void traverseToFormLabelAdd(String rolename, String email) {
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
        assignRoleAndRight(rolename, email);
    }

    /**
     * This method assignes role and right
     * 
     * @param rolename contains the name of role
     * @param email contains email
     */
    private void assignRoleAndRight(String rolename, String email) {
        for (String winHandle : browserDriver.getWindowHandles()) {
            browserDriver.switchTo().window(winHandle);
        }
        Select role = new Select(browserDriver
                .findElement(By.xpath("//select[contains(@name,'RoleID')]")));
        role.selectByVisibleText(rolename);
        userManagementPopup.clkOkOfPopup(waitForReload);
        CSLogger.info("Clicked on OK of pop up");
        browserDriver.switchTo().window(winHandleBeforeSwitching);
        setEmailInProfileTab(email);
        clkSave();
    }

    /**
     * This method sets email in the profile tab
     * 
     * @param email contains email
     */
    private void setEmailInProfileTab(String email) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getProfileTab()));
        userManagementPage.getProfileTab().click();
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getTxtEmailProfileTab()));
        userManagementPage.getTxtEmailProfileTab().clear();
        CSUtility.tempMethodForThreadSleep(1000);
        userManagementPage.getTxtEmailProfileTab().sendKeys(email);
    }

    /**
     * This method creates user
     * 
     * @param userName contains user name
     * @param password contains password
     */
    private void createUser(String userName, String password) {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        clkUsers();
        userManagementPage.getUsersNode().click();
        clkCreateNewUser();
        clkAdministrationTab(userName, password);
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
     * This method clicks on create new option
     */
    private void clkCreateNewUser() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
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
        userManagementPopup.clkOkOfPopup(waitForReload);
        CSLogger.info("User created.");
    }

    /**
     * This method saves contents by clicking on save button
     */
    private void saveContents() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        userManagementPage.clkBtnSave();
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
     * This method clicks Users tab
     */
    private void clkUsers() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUsersNode()));
        userManagementPage.getUsersNode().click();
        CSLogger.info("Clicked on Users node");
    }

    /**
     * This method creates user role
     * 
     * @param roleName
     */
    private void createUserRole(String roleName) {
        clkRoles();
        clkCreateNewRoles();
        enterRoleNameAndEnableCheckbox(roleName);
    }

    /**
     * This method enters role name and enables checkbox
     * 
     * @param roleName
     */
    private void enterRoleNameAndEnableCheckbox(String roleName) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
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
     * This method clicks create new option for creating new Role
     */
    private void clkCreateNewRoles() {
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
            CSLogger.error("Could not edit role name " + e);
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
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
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
     * This method logs in with wrong password
     * 
     * @param userName contains user name
     * @param wrongPasswordToLogin contains wrong password for logging in
     */
    private void loginWithWrongPassword(String userName,
            String wrongPasswordToLogin) {
        loginPage.enterUsername(userName);
        loginPage.enterPassword(wrongPasswordToLogin);
        CSLogger.info("Entered username and password");
        loginPage.clkLoginButton();
        // handleAlert();
    }

    /**
     * This method handles alert box
     */
    private void handleAlert() {
        Alert alert = getAlert();
        String alertText = alert.getText();
        if (alertText
                .equals("Login failed. Username or password is incorrect.")) {
            alert.accept();
            CSLogger.info("Clicked on OK of alert");
        } else {
            CSLogger.info("Could not handle alert");
        }
    }

    /**
     * This method returns the instance of Alert box in alert variable
     * 
     * @return alert
     */
    private Alert getAlert() {
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        return alert;
    }

    /**
     * This method handles forgot password link
     * 
     * @param wrongRecoveryMail contains wrong recovery mail
     * @param userMailInProfile contains user mail from profile tab
     * @param existingMailInSystem contains existing mail in the system
     */
    private void handleForgotPasswordLink(String wrongRecoveryMail,
            String userMailInProfile, String existingMailInSystem) {
        WebElement btnAccept = browserDriver.findElement(By
                .xpath("//button[@id='CSPortalLoginTermsOfUseButtonAccept']"));
        btnAccept.click();
        setMail(wrongRecoveryMail);
        CSLogger.info("Clicked on Send Password link");
        handlePopUpForWrongPassword();
        sendCorrectRecoveryMail(userMailInProfile, existingMailInSystem);
    }

    /**
     * This method sets mail
     * 
     * @param mail contains String containing mail address
     */
    private void setMail(String mail) {
        winHandleBeforeSwitching = browserDriver.getWindowHandle();
        loginPage.clkForgotPasswordLink(waitForReload);
        waitForReload.until(ExpectedConditions
                .visibilityOf(loginPage.getTxtLoginLostPasswordInput()));
        loginPage.getTxtLoginLostPasswordInput().clear();
        loginPage.getTxtLoginLostPasswordInput().sendKeys(mail);
        waitForReload.until(ExpectedConditions
                .visibilityOf(loginPage.getBtnLoginLostPassword()));
        loginPage.getBtnLoginLostPassword().click();
    }

    /**
     * This method sends correct recovery mail
     * 
     * @param userMailInProfile contains user defined mail from profile tab
     * @param existingMailInSystem contains existing mail in the system
     */
    private void sendCorrectRecoveryMail(String userMailInProfile,
            String existingMailInSystem) {
        try {
            setMail(userMailInProfile);
            Alert alert = getAlert();
            alert.accept();
            CSLogger.info("Clicked on Ok of pop up");
            for (String winHandle : browserDriver.getWindowHandles()) {
                browserDriver.switchTo().window(winHandle);
            }
            verifyEmailDebugOutput(existingMailInSystem, userMailInProfile);
        } catch (Exception e) {
            CSLogger.error("Failed to accept alert", e);
        }
    }

    /**
     * This method verifies subject , user defined and existing mail from the
     * email debug output window
     * 
     * @param existingMailInSystem contains existing mail in the system
     * @param userMailInProfile contains user defined mail
     */
    private void verifyEmailDebugOutput(String existingMailInSystem,
            String userMailInProfile) {
        try {
            // verify Subject
            CSUtility.switchToDefaultFrame(browserDriver);
            WebElement subject = browserDriver.findElement(
                    By.xpath("(//html//body//table/tbody/tr[8]/td)[2]"));
            waitForReload.until(ExpectedConditions.visibilityOf(subject));
            if (subject.getText().equals(
                    "CS Portal Account Notification for User Automation User at CSLive")) {
                CSLogger.info("Subject verified.");
            } else {
                CSLogger.info("Subject is not present.");
            }
            // verify set mail and existing mail
            WebElement existingMail = browserDriver.findElement(
                    By.xpath("(//html//body//table/tbody/tr/td[2])[1]"));
            waitForReload.until(ExpectedConditions.visibilityOf(existingMail));
            if (existingMailInSystem.equals(existingMail.getText())) {
                CSLogger.info("Existing mail is present");
                Assert.assertEquals(existingMailInSystem,
                        existingMail.getText());
            } else {
                CSLogger.info("Existing mail is absent from the pop up window");
            }
            // verify UserMail
            WebElement userMail = browserDriver.findElement(
                    By.xpath("(//html//body//table/tbody/tr/td[2])[2]"));
            waitForReload.until(ExpectedConditions.visibilityOf(userMail));
            if (userMailInProfile.equals(userMail.getText())) {
                CSLogger.info("User mail in profile is present");
                Assert.assertEquals(userMailInProfile, userMail.getText());
            } else {
                CSLogger.info("User mail is not present");
            }
        } catch (Exception e) {
            CSLogger.debug("Verification failed.", e);
            Assert.fail("Verification failed.", e);
        }
    }

    /**
     * This method handles pop up comes after entering wrong password
     */
    private void handlePopUpForWrongPassword() {
        Alert alert = getAlert();
        String alertText = alert.getText();
        if (alertText.equals("User Does Not Exist!")) {
            alert.accept();
            CSLogger.info("Alert accepted by clicking OK");
        } else {
            CSLogger.info("Could not handle pop up for wrong password");
        }
    }

    /**
     * This data provider returns sheet data which contains user name , password
     * ,wrong password ,email address to provide in profile tab,mode(debug)
     * ,wrong recovery mail address,existing mail in the system ,language
     * 
     * @return verifyForgotPasswordSheet
     */
    @DataProvider(name = "verifyForgotPasswordWorks")
    public Object[] verifyForgotPassword() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                verifyForgotPasswordSheet);
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
        choosePortalPage = new ChoosePortalPage(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
    }
}
