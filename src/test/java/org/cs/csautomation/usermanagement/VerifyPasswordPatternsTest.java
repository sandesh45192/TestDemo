/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class sets maximum and minimum lengths of password and verifies the
 * authenticity of different types of user entered passwords
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyPasswordPatternsTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private SettingsPage         settingsPage;
    private String               verifyPasswordSheet = "VerifyPasswordSheet";
    private UserManagementPage   userManagementPage;
    private String               winHandleBeforeSwitching;
    private IUserManagementPopup userManagementPopup;
    private IProductPopup        productPopUp;
    private LoginPage            loginPage;
    private ChoosePortalPage     choosePortalPage;
    private CSPortalWidget       csPortalWidgetInstance;
    private SoftAssert           softAssert;

    /**
     * This test method sets maximum and minimum lengths of passoword ad
     * verifies the authenticity of different types of user entered passwords
     * 
     * @param minPasswordLength contains min password length
     * @param maxPasswordLength contains maximum password length
     * @param passwordPatternMessage contains password pattern message
     * @param existingUser contains name of existing user
     * @param singleDigitPassword contains single digit password
     * @param numericPassword contains numeric password
     * @param lowerCaseAlphaPassword contains password in lower case alphabets
     * @param upperCaseAlphaPassword contains password in upper case alphabets
     * @param specialCharPassword contains password in special characters
     * @param passwordLessThanMinLength contains password less than minimum
     *            length
     * @param passwordMoreThanMaxLength contains password more than maximum
     *            length
     * @param passwordWithExactMinLength contains password with exact minimum
     *            length
     * @param passwordWithExactMaxLength contains password with exact maximum
     *            length
     * @param languageForLogin contains language for login
     */
    @Test(priority = 1, dataProvider = "verifyPasswordData")
    public void testVerifyPasswordPatters(String minPasswordLength,
            String maxPasswordLength, String passwordPatternMessage,
            String existingUser, String singleDigitPassword,
            String numericPassword, String lowerCaseAlphaPassword,
            String upperCaseAlphaPassword, String specialCharPassword,
            String passwordLessThanMinLength, String passwordMoreThanMaxLength,
            String passwordWithExactMinLength,
            String passwordWithExactMaxLength, String languageForLogin) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            performUseCase(minPasswordLength, maxPasswordLength,
                    passwordPatternMessage);
            openExistingUser(existingUser);
            clkAdministration();
            verifyPasswords(singleDigitPassword, numericPassword,
                    lowerCaseAlphaPassword, upperCaseAlphaPassword,
                    specialCharPassword, passwordLessThanMinLength,
                    passwordMoreThanMaxLength, passwordWithExactMinLength,
                    passwordWithExactMaxLength);
            logoutFromApplication();
            loginAsCreatedUser(existingUser, passwordWithExactMaxLength,
                    languageForLogin);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method performs the use case
     * 
     * @param minPasswordLength contains minimum password length
     * @param maxPasswordLength contains maximum password length
     * @param passwordPatternMessage contains password pattern message to set
     *            which comes while handling alert box
     */
    private void performUseCase(String minPasswordLength,
            String maxPasswordLength, String passwordPatternMessage) {
        clkAuthenticationNode();
        setFields(minPasswordLength, maxPasswordLength);
        WebElement lowerCasePassChkbox = waitForReload.until(ExpectedConditions
                .visibilityOf(settingsPage.getLowerCasePasswordCheckbox()));
        String lowerCaseValue = getAttributeValue(lowerCasePassChkbox);
        WebElement upperCasePassChkbox = waitForReload.until(ExpectedConditions
                .visibilityOf(settingsPage.getUpperCasePasswordCheckbox()));
        String upperCaseValue = getAttributeValue(upperCasePassChkbox);
        WebElement passNumbersChkbox = waitForReload.until(ExpectedConditions
                .visibilityOf(settingsPage.getPasswordNumbersCheckbox()));
        String passNumbersValue = getAttributeValue(passNumbersChkbox);
        WebElement specialCharPassChkbox = waitForReload
                .until(ExpectedConditions.visibilityOf(
                        settingsPage.getPasswordSpecialCharCheckbox()));
        String specialCharValue = getAttributeValue(specialCharPassChkbox);
        enableCheckboxes(lowerCasePassChkbox, lowerCaseValue,
                upperCasePassChkbox, upperCaseValue, passNumbersChkbox,
                passNumbersValue, specialCharPassChkbox, specialCharValue);
        setPasswordPatternMessage(passwordPatternMessage);
        verifySetFields(lowerCaseValue, upperCaseValue, passNumbersValue,
                specialCharValue, passwordPatternMessage);
    }

    /**
     * This method gets attribute
     * 
     * @param element contains web element
     * @return attribute value in string format of class
     */
    private String getAttributeValue(WebElement element) {
        return element.getAttribute("class");
    }

    /**
     * This method verifies set fields
     * 
     * @param lowerCaseValue contains lower case value
     * @param upperCaseValue contains upper case value
     * @param passNumbersValue contains number value as password
     * @param specialCharValue contains special character instance
     * @param passwordPatternMessage contains password pattern message
     */
    private void verifySetFields(String lowerCaseValue, String upperCaseValue,
            String passNumbersValue, String specialCharValue,
            String passwordPatternMessage) {
        try {
            String chkboxEnableMessage = "CSGuiEditorCheckboxContainer On Enabled GuiEditorCheckbox";
            if (lowerCaseValue.equals(chkboxEnableMessage)
                    && upperCaseValue.equals(chkboxEnableMessage)
                    && passNumbersValue.equals(chkboxEnableMessage)
                    && specialCharValue.equals(chkboxEnableMessage)) {
                CSLogger.info("Checkboxes are on");
            } else {
                CSLogger.info("Could not enable checkboxes");
            }
            verifyPatternMessage(passwordPatternMessage);
        } catch (Exception e) {
            CSLogger.debug("Verification failed ", e);
            softAssert.fail("Verification failed ", e);
        }
    }

    /**
     * This method verifies password pattern message
     * 
     * @param passwordPatternMessage contains password pattern message
     */
    private void verifyPatternMessage(String passwordPatternMessage) {
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(settingsPage.getTxtPasswordPatternMessage()));
            String patternMessage = settingsPage.getTxtPasswordPatternMessage()
                    .getText();
            if (patternMessage.equals(passwordPatternMessage)) {
                CSLogger.info("Pattern message has been set successfully");
            }
        } catch (Exception e) {
            CSLogger.debug("Verification for setting message pattern failed.",
                    e);
            softAssert.fail("Verification Failed", e);
        }
    }

    /**
     * This method enables checkboxes
     * 
     * @param lowerCasePassChkbox contains lower case password checkbox
     * @param lowerCaseValue contains value for lower case checkbox
     * @param upperCasePassChkbox contains upper case password checkbox
     * @param upperCaseValue contains upper case value upper case checkbox
     * @param passNumbersChkbox contains number checkbox instance
     * @param passNumbersValue contains value for number checkbox
     * @param specialCharPassChkbox contains special character checkbox
     * @param specialCharValue contains special char value
     */
    private void enableCheckboxes(WebElement lowerCasePassChkbox,
            String lowerCaseValue, WebElement upperCasePassChkbox,
            String upperCaseValue, WebElement passNumbersChkbox,
            String passNumbersValue, WebElement specialCharPassChkbox,
            String specialCharValue) {
        enableCheckbox(lowerCaseValue, lowerCasePassChkbox);
        enableCheckbox(upperCaseValue, upperCasePassChkbox);
        enableCheckbox(passNumbersValue, passNumbersChkbox);
        enableCheckbox(specialCharValue, specialCharPassChkbox);
    }

    /**
     * This method enables checkbox
     * 
     * @param value contains runtime value of checkbox
     * @param element contains webelement of checkbox
     */
    public void enableCheckbox(String value, WebElement element) {
        CSUtility.tempMethodForThreadSleep(1000);
        String chkboxDisabledMessage = "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox";
        if (value.equals(chkboxDisabledMessage)) {
            element.click();
        } else {
            CSLogger.info("Checkbox is already enabled");
        }
    }

    /**
     * This method sets password pattern message
     * 
     * @param passwordPatternMessage
     */
    private void setPasswordPatternMessage(String passwordPatternMessage) {
        enterValueInField(passwordPatternMessage,
                settingsPage.getTxtPasswordPatternMessage());
        clkSave();
        CSUtility.tempMethodForThreadSleep(3000);
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
     * This method clicks on authentication node
     */
    private void clkAuthenticationNode() {
        clkSettingsInLeftPane();
        clkCore();
        settingsPage.clkBtnAuthentication(waitForReload);
        CSLogger.info("Clicked on Authentication");
    }

    /**
     * This method clicks on settings ini left pane
     */
    private void clkSettingsInLeftPane() {
        traverseToSettingsNode();
        waitForReload.until(ExpectedConditions
                .visibilityOf(settingsPage.getBtnSettingsNode()));
        settingsPage.getBtnSettingsNode().click();
        CSLogger.info("Clicked on Settings node in left section");
    }

    /**
     * This method traverses upto settings node in left section
     */
    private void traverseToSettingsNode() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getsettingsRightPaneMainFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
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
     * This method sets field values in respective fields
     * 
     * @param minPasswordLength contains minimum password length
     * @param maxPasswordLength contains maximum password length
     */
    private void setFields(String minPasswordLength, String maxPasswordLength) {
        enterValueInField(minPasswordLength,
                settingsPage.getTxtMinPasswordLength());
        enterValueInField(maxPasswordLength,
                settingsPage.getTxtMaxPasswordLength());
    }

    public void enterValueInField(String value, WebElement field) {
        waitForReload.until(ExpectedConditions.visibilityOf(field));
        field.clear();
        field.sendKeys(value);
    }

    /**
     * This method opens existing user
     * 
     * @param existingUser contains name of existing user
     */
    private void openExistingUser(String existingUser) {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkUsers();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[4]"));
        for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            String user = list.get(listIndex).getText();
            if (user.equals(existingUser)) {
                list.get(listIndex).click();
                list.get(listIndex).click();
                CSLogger.info("Clicked on existing user");
            }
        }
    }

    /**
     * This method clicks Users tab
     */
    private void clkUsers() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUsersNode()));
        userManagementPage.getUsersNode().click();
        userManagementPage.getUsersNode().click();
        CSLogger.info("Clicked on Users node");
    }

    /**
     * This method traverses to administration
     */
    private void traverseToAdministration() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
    }

    /**
     * This method clicks administration tab in right section after opening
     * existing user
     */
    private void clkAdministration() {
        traverseToAdministration();
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getAdministrationTab()));
        userManagementPage.getAdministrationTab().click();
        CSLogger.info("Clicked on Administration tab");
    }

    /**
     * This method verifies all the passwords
     * 
     * @param singleDigitPassword contains single digit password
     * @param numericPassword contains numeric password
     * @param lowerCaseAlphaPassword contains lower case alphabet password
     * @param upperCaseAlphaPassword contains upper case alphabet password
     * @param specialCharPassword contains special char password
     * @param passwordLessThanMinLength contains password less than minimum
     *            length
     * @param passwordMoreThanMaxLength contains password more than maximum
     *            length
     * @param passwordWithExactMinLength contains password with exact minimum
     *            length
     * @param passwordWithExactMaxLength contains password with exact maximum
     *            length
     */
    private void verifyPasswords(String singleDigitPassword,
            String numericPassword, String lowerCaseAlphaPassword,
            String upperCaseAlphaPassword, String specialCharPassword,
            String passwordLessThanMinLength, String passwordMoreThanMaxLength,
            String passwordWithExactMinLength,
            String passwordWithExactMaxLength) {
        try {
            enterPasswordAndVerifyMessage(singleDigitPassword,
                    "Single Digit password verification");
            enterPasswordAndVerifyMessage(numericPassword,
                    "Numeric password verification");
            enterPasswordAndVerifyMessage(lowerCaseAlphaPassword,
                    "Lower password verification");
            enterPasswordAndVerifyMessage(upperCaseAlphaPassword,
                    "Upper password verification");
            enterPasswordAndVerifyMessage(specialCharPassword,
                    "Special password verification");
            enterPasswordAndVerifyMessage(passwordLessThanMinLength,
                    "MinLength password verification");
            enterPasswordAndVerifyMessage(passwordMoreThanMaxLength,
                    "MaxLength password verification");
            verifyPasswordWithExactMinLength(passwordWithExactMinLength);
            verifyPasswordWithExactMaxLength(passwordWithExactMaxLength);
        } catch (Exception e) {
            CSLogger.error("Verification failed." + e);
            softAssert.fail("Verification failed", e);
        }
    }

    /**
     * This method verifies password with exact Minimum length
     * 
     * @param passwordWithExactMaxLength contains characters with exact Min
     *            length
     */
    private void verifyPasswordWithExactMinLength(
            String passwordWithExactMinLength) {
        winHandleBeforeSwitching = browserDriver.getWindowHandle();
        saveContentsAndHandlePopup(passwordWithExactMinLength);
        browserDriver.switchTo().window(winHandleBeforeSwitching);
    }

    private void saveContentsAndHandlePopup(String value) {
        enterPassword(value);
        saveContents();
        handlePopupToConfirmPassword(value);

    }

    /**
     * This method verifies password with exact maximum length
     * 
     * @param passwordWithExactMaxLength contains characters with exact maximum
     *            length
     */
    private void verifyPasswordWithExactMaxLength(
            String passwordWithExactMaxLength) {
        clkAdministration();
        winHandleBeforeSwitching = browserDriver.getWindowHandle();
        saveContentsAndHandlePopup(passwordWithExactMaxLength);
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
        userManagementPopup.clkOkOfPopup(waitForReload);
        CSLogger.info("Clicked on OK of pop up");
        browserDriver.switchTo().window(winHandleBeforeSwitching);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
    }

    /**
     * This method enters password ,save contents and verifies alert message
     * 
     * @param element contains element in string format which contains password
     *            from their respective methods
     */
    public void enterPasswordAndVerifyMessage(String element,
            String verificationMessage) {
        enterPassword(element);
        saveContents();
        verifyAlertMessage(verificationMessage);
    }

    /**
     * This method verifies text of alert message is equal to set message in
     * pattern message field
     */
    private void verifyAlertMessage(String verificationMessage) {
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        String alertMessage = alert.getText();
        if (alertMessage.equals(
                "Administration / Password: The defined password pattern does not match the entered password.")) {
            CSLogger.info(verificationMessage + "Verified successfully");
            alert.accept();
        } else {
            CSLogger.debug(verificationMessage + "Verification failed.");
            softAssert.fail("Verification failed.");
        }
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
     * This method saves contents by clicking on save button
     */
    private void saveContents() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
        userManagementPage.clkBtnSave();
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
     * This method performs login as created user
     * 
     * @param existingUser contains existing user
     * @param passwordWithExactMaxLength contains password with exact max length
     * @param language contains language to be set during login
     */
    private void loginAsCreatedUser(String existingUser,
            String passwordWithExactMaxLength, String language) {
        chooseLanguage(language);
        loginPage.enterUsername(existingUser);
        loginPage.enterPassword(passwordWithExactMaxLength);
        CSLogger.info("Entered username and password");
        loginPage.clkLoginButton();
        CSLogger.info("Clicked on Login button ");
        choosePortalPage.clkBtnAccept();
        CSUtility.tempMethodForThreadSleep(3000);
        loginPage.clkLoginButton();
        CSLogger.info("Clicked on Login button after accepting terms of use");
    }

    /**
     * This method chooses language while logging in
     * 
     * @param language contains language to be set
     */
    private void chooseLanguage(String language) {
        Select loginLanguage = new Select(browserDriver
                .findElement(By.xpath("//select[@id='loginLanguage']")));
        loginLanguage.selectByVisibleText(language);
        CSLogger.info(language + " language has been chosen");
    }

    /**
     * This data provider returns data from sheet which contains password
     * minimum length,password of maximum length,password pattern
     * message,existing user,single digit password,numeric password,lower case
     * alphabet password,upper case alphabet password,special character
     * password,password less than minimum length,password more than maximum
     * length,password with exact minimum length,password with exact maximum
     * length
     * 
     * @return verifyPasswordSheet
     */
    @DataProvider(name = "verifyPasswordData")
    public Object[] verifyPassword() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                verifyPasswordSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */

    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        settingsPage = new SettingsPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        loginPage = new LoginPage(browserDriver);
        choosePortalPage = new ChoosePortalPage(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
    }
}