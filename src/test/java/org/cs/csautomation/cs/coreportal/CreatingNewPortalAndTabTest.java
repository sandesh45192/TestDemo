/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.coreportal;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class creates user and assigns already created access rights and roles
 * to the created user group
 * 
 * @author CSAutomation Team
 *
 */
public class CreatingNewPortalAndTabTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private IProductPopup        productPopUp;
    private UserManagementPage   userManagementPage;
    private IUserManagementPopup userManagementPopup;
    private String               winHandleBeforeSwitching;
    private LoginPage            loginPage;
    private ChoosePortalPage     choosePortalPage;
    private String               creatingNewPortalTest = "CreatingNewPortalSheet";

    /**
     * This method creates user group, user and assigns all the rights as admin
     * 
     * @param roleName String contains the name of the role to be assigned to
     *            the user group
     * @param accessRight String contains the access rights that has to be
     *            assigned to the user group
     * @param username String contains username
     * @param password String contains password
     * @param language String contains language at input
     * @param portalName String contains name of new portal
     * @param createNewPortalOption String option create new portal
     * @param tabName String Name of new tab
     */
    @Test(priority = 1, dataProvider = "creatingNewPortalData")
    public void testCreatingNewPortal(String roleName, String accessRight,
            String username, String password, String language,
            String portalName, String createNewPortalOption, String tabName) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            createUser();
            createUsernameAndPassword(username, password);
            verifyCreationofUser(username);
            traverseUptoRightsTab();
            addRoleAndRight(roleName, accessRight);
            verifyAddedroleAndAccessRight(accessRight);
            logoutAsAdmin();
            loginAsCreatedUser(username, password, language);
            createNewPortal(createNewPortalOption);
            clkCreate();
            clkCreateNewTab(portalName);
            verfyNewPortal();
        } catch (Exception e) {
            CSLogger.debug("Automation error : testCreatingNewPortal", e);
            Assert.fail("Automation error : testCreatingNewPortal", e);
        }
    }

    /**
     * This method create the new tab in the new created portal
     * 
     * @param roleName String contains the name of the role to be assigned to
     *            the user group
     * @param accessRight String contains the access rights that has to be
     *            assigned to the user group
     * @param username String contains username
     * @param password String contains password
     * @param language String contains language at input
     * @param portalName String contains name of new portal
     * @param createNewPortalOption String option create new portal
     * @param tabName String Name of new tab
     */
    @Test(priority = 2, dataProvider = "creatingNewPortalData")
    public void testCreatingNewPortaltab(String roleName, String accessRight,
            String username, String password, String language,
            String portalName, String createNewPortalOption, String tabName) {
        try {
            addTabInCreatedPortal(tabName);
            verifyInsertedColumnsInCreatedTab();
        } catch (Exception e) {
            CSLogger.debug("Automation error : testCreatingNewPortaltab", e);
            Assert.fail("Automation error : testCreatingNewPortaltab", e);
        }
    }

    /**
     * This method creates the new user group
     * 
     * @param userGroup contains the name of the user group to be created
     */
    private void createUser() {
        traverseToSystemPreferences();
        traverseToUserManagement();
        clkUsers();
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
        right.selectByVisibleText(accessRight);
        clkOk();
        browserDriver.switchTo().window(winHandleBeforeSwitching);
        clkSave();
    }

    /**
     * This method clicks on save button
     * 
     */
    private void clkSave() {
        traverseToMainFrame();
        traverseToEditFrame();
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
    private void createUsernameAndPassword(String username, String password) {
        traverseToSystemPreferences();
        traverseToMainFrame();
        clkCreateNew();
        clkAdministrationTab(username, password);
    }

    /**
     * This method clicks Users tab
     */
    private void clkUsers() {
        Actions action = new Actions(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUsersNode()));
        action.doubleClick(userManagementPage.getUsersNode()).perform();
        CSLogger.info("Clicked on Users node");
    }

    /**
     * This method traverses to System Preferences in left section pane
     * 
     */
    private void traverseToSystemPreferences() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getsettingsRightPaneMainFrame()));
        clkSystemPreferences();
    }

    /**
     * This method traverses upto Rights tab
     * 
     */
    private void traverseUptoRightsTab() {
        traverseToMainFrame();
        clkRightsTab();
        traverseToFormLabelAdd();
    }

    /**
     * This method clicks on the rights tab
     * 
     */
    private void clkRightsTab() {
        traverseToEditFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getRightsTab()));
        userManagementPage.getRightsTab().click();
        CSLogger.info("Clicked on the Rights tab");
    }

    /**
     * This method traverses upto label Add to add Role and Access Right
     * 
     */
    private void traverseToFormLabelAdd() {
        winHandleBeforeSwitching = browserDriver.getWindowHandle();
        traverseToMainFrame();
        traverseToEditFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFrameSettings()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getFormLabelAdd()));
        userManagementPage.getFormLabelAdd().click();
    }

    /**
     * This method clicks on the System Preferences
     * 
     */
    private void clkSystemPreferences() {
        try {
            waitForReload.until(ExpectedConditions.visibilityOf(
                    userManagementPage.getSystemPreferencesNode()));
            userManagementPage.getSystemPreferencesNode().click();
            CSLogger.info("Clicked on System Preferences");
        } catch (Exception e) {
            CSLogger.info("Failed to click on System preferences ");
        }
    }

    /**
     * This method traverses to user management after clicking system
     * preferences
     * 
     */
    private void traverseToUserManagement() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getsettingsRightPaneMainFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
        clkUserManagement();
    }

    /**
     * This method clicks userManagement tab
     * 
     */
    private void clkUserManagement() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUserManagement()));
        userManagementPage.getUserManagement().click();
        CSLogger.info("Clicked on User Management");
    }

    /**
     * This method traverses upto main frame
     * 
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
     * This method clicks on create new option
     * 
     */
    private void clkCreateNew() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getBtnCreateNew()));
        userManagementPage.getBtnCreateNew().click();
    }

    /**
     * This method traverses to edit frame
     * 
     */
    private void traverseToEditFrame() {
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
    }

    /**
     * This method saves contents by clicking on save button
     * 
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
                .visibilityOf(locator.getCsPortalWindowMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPopup.getUserInput()));
        userManagementPopup.getUserInput().sendKeys(password);
        clkOk();
    }

    /**
     * This method clicks on Ok of pop up
     * 
     */
    private void clkOk() {
        userManagementPopup.clkOkOfPopup(waitForReload);
        CSLogger.info("Clicked on OK of pop up");
    }

    /**
     * This method verifies if Role and Access Right has been assigned
     * successfully to the user group
     * 
     * @param accessRightName contains the access right name that has been
     *            assigned
     * @param subfolderName contains the subfolder name that has been assigned
     */
    private void verifyAddedroleAndAccessRight(String accessRightName) {
        try {
            String verifyRights = traverseToAssignedAccessRight();
            if (verifyRights.contains(accessRightName)) {
                CSLogger.info(
                        "Access rights have been assigned successfully to the created user");
            } else {
                CSLogger.error(
                        "Could not verify assigned access rights to the created user ");
                Assert.fail(
                        "Could not verify assigned access rights to the created user ");
            }
        } catch (Exception e) {
            CSLogger.error(
                    "Could not verify assigned access rights to the created user");
        }
    }

    /**
     * This method verifies creation of user is successful
     * 
     * @param username String name of new user
     */
    public void verifyCreationofUser(String username) {
        traverseToMainFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        WebElement table = browserDriver
                .findElement(By.id("CSGuiListbuilderTable"));
        int lblCount = table.findElements(By.tagName("tr")).size();
        String text = null;
        List<String> userNameText = new ArrayList<String>();
        for (int row = 3; row < lblCount; row++) {
            text = browserDriver
                    .findElement(
                            By.xpath("//table[@class='hidewrap CSAdminList']"
                                    + "/tbody[1]/tr[" + row + "]/td[4]"))
                    .getText();
            userNameText.add(text);
        }
        if (userNameText.contains(username)) {
            CSLogger.info("Creation of User is passed.");
        } else {
            CSLogger.error("User is not created. ");
            Assert.fail("User is not created. ");
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
    private String traverseToAssignedAccessRight() {
        traverseToMainFrame();
        traverseToEditFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFrameSettings()));
        WebElement element = browserDriver
                .findElement(By.xpath("//html//body//table/tbody/tr[2]/td[3]"));
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        String verifyRights = element.getText();
        return verifyRights;
    }

    /**
     * This method logout as admin
     * 
     */
    private void logoutAsAdmin() {
        clkPortalLinkOptions();
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
     * This method login as created user
     * 
     * @param username String contains username
     * @param password String contains password
     */
    private void loginAsCreatedUser(String username, String password,
            String language) {
        chooseLanguage(language);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        CSLogger.info("Entered Username and password");
    }

    /**
     * This method create new portal option
     * 
     * @param createNewPortalOption String option create new portal
     */
    private void createNewPortal(String createNewPortalOption) {
        loginPage.clkLoginButton();
        clkAcceptBth();
        clkCreateNewPortal(createNewPortalOption);
    }

    /**
     * This method click accept button
     * 
     */
    public void clkAcceptBth() {
        WebElement btnAccept = browserDriver
                .findElement(By.id("CSPortalLoginTermsOfUseButtonAccept"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnAccept);
        btnAccept.click();
        CSLogger.info("Clicked on Accepted Button");
    }

    /**
     * This method click new portal option
     * 
     * @param createNewPortalOption String option create new portal
     */
    private void clkCreateNewPortal(String createNewPortalOptionn) {
        Select newPortal = new Select(loginPage.getDrodwnPortalSelect());
        newPortal.selectByVisibleText(createNewPortalOptionn);
        CSLogger.info("Create New Portal option selected");
        verifyCreatePortalBtn();
    }

    /**
     * This method click on create new tab
     * 
     * @param portalName String new portal name
     */
    private void clkCreateNewTab(String portalName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        choosePortalPage.clkCreateNewTab();
        CSLogger.info("Clicked on Create new tab");
        enterTitle(portalName);
    }

    /**
     * This method click on create button
     * 
     */
    private void clkCreate() {
        loginPage.clkBtnCreate();
        CSLogger.info("Clicked on Create Button on Login Page");
    }

    /**
     * This method verifies change login button to Create button test case
     * 
     */
    public void verifyCreatePortalBtn() {
        WebElement create = browserDriver
                .findElement(By.xpath("//input[@value='Create']"));
        if (create.isEnabled()) {
            CSLogger.info("Found Create Button");
        } else {
            Assert.fail("Create Button Not Found");
        }
    }

    /**
     * This method choose language while login
     * 
     * @param language String language login
     */
    private void chooseLanguage(String language) {
        Select loginLanguage = new Select(loginPage.getDrpdwnLanguage());
        loginLanguage.selectByVisibleText(language);
        CSLogger.info(language + " language has been chosen");
    }

    /**
     * This method click portal link option
     * 
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * This method enter the title of new portal
     * 
     * @param portalName String name of new portal
     */
    private void enterTitle(String portalName) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getTxtTitle()));
        choosePortalPage.getTxtTitle().clear();
        choosePortalPage.getTxtTitle().sendKeys(portalName);
        clkInsert();
    }

    /**
     * This method click button insert
     * 
     */
    private void clkInsert() {
        choosePortalPage.clkBtnInsert(waitForReload);
        CSLogger.info("Clicked on Insert to create new portal");
    }

    /**
     * This method verifies new portal open successfully
     * 
     */
    public void verfyNewPortal() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmTopFrame()));
        if (choosePortalPage.getTabAdd().isEnabled()) {
            CSLogger.info("New Portal has been created");
        } else {
            CSLogger.error("New folder verification fail");
            Assert.fail("New folder verification fail");
        }
    }

    /**
     * This method add the new tab in the created folder
     * 
     * @param tabName String new tab name
     */
    private void addTabInCreatedPortal(String tabName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmTopFrame()));
        choosePortalPage.clkBtnAddTab(waitForReload);
        clkCreateNew(tabName);
    }

    /**
     * This method click on create new for create new tab
     * 
     * @param tabName String new tab name
     */
    private void clkCreateNew(String tabName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getCreateNewInTabTemplate()));
        choosePortalPage.getCreateNewInTabTemplate().click();
        CSLogger.info(
                "Clicked on create new to add tab in newly created portal");
        addTabName(tabName);
    }

    /**
     * This method add the tag name
     * 
     * @param tabName String new tab name
     */
    private void addTabName(String tabName) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getTxtTitle()));
        choosePortalPage.getTxtTitle().clear();
        choosePortalPage.getTxtTitle().sendKeys(tabName);
        choosePortalPage.clkBtnInsert(waitForReload);
        CSLogger.info("Tab inserted successfully in new portal ");
    }

    /**
     * This method verifies the inserted column in the created tab
     * 
     */
    private void verifyInsertedColumnsInCreatedTab() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            CSUtility.tempMethodForThreadSleep(5000);
            waitForReload.until(ExpectedConditions
                    .frameToBeAvailableAndSwitchToIt(locator.getFrmTopFrame()));
            int list = browserDriver.findElements(By.xpath(
                    "//table[contains(@class,'CSPortalMain')]/tbody/tr/td[2]/div/div/div/div/span"))
                    .size();
            if (list >= 3) {
                CSLogger.info("Tab created in new portal");
            } else {
                CSLogger.info("could not create tab in  newly created portal");
            }
        } catch (Exception e) {
            CSLogger.error("Verification failed" + e);
            Assert.fail("Could not verify created tab", e);
        }
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains role name , access right name ,username ,
     * password,language,Portal Name,Create New Portal and Tab Name
     * 
     * @return createUserGroupAndUsersheet
     */
    @DataProvider(name = "creatingNewPortalData")
    public Object[] creatingNewPortal() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("corePortalTestCases"),
                creatingNewPortalTest);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        loginPage = new LoginPage(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        choosePortalPage = new ChoosePortalPage(browserDriver);
    }
}
