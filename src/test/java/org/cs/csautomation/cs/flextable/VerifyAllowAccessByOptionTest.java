/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.FlexTablePage;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
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
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods which verifies Allow Access By Option
 * test uses.
 * 
 * @author CSAutomation Team
 */
public class VerifyAllowAccessByOptionTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private IProductPopup             productPopUp;
    private Actions                   action;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSPortalHeader            csPortalHeader;
    private FrameLocators             locator;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private FlexTablePage             flexTablePage;
    private UserManagementPage        userManagementPage;
    private IUserManagementPopup      userManagementPopup;
    private String                    winHandleBeforeSwitching;
    private LoginPage                 loginPage;
    private SoftAssert                softasssert;
    private String                    allowAccessByOptionTestData = "AllowAccessByOption";

    /**
     * This method verifies Allow Access By Option test uses.
     * 
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     * @param roleName String object contains role name assign to user
     * @param accessRight String object contains rights given to user
     * @param username String object contains user name
     * @param password String object contains password
     * @param language String object contains language used at time of login
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     * @param allowAccessToUser String object contains user name to assign
     *            access
     * @param adminUserName String object contains admin username
     * @param adminPassword String object contains admin Password
     */
    @Test(dataProvider = "AllowAccessByOptionDataSheet")
    public void testVerifyAllowAccessByOption(String className,
            String flexProduct, String roleName, String accessRight,
            String username, String password, String language,
            String templateName, String templateType, String allowAccessToUser,
            String adminUserName, String adminPassword) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createProduct(flexProduct);
            dragAndDropClassToProduct(className, flexProduct);
            createUser(username, password, roleName, accessRight);
            allowAccessToAllUser(templateName, templateType);
            verifyAccessToAllUser(flexProduct, username, password, language,
                    templateName);
            loginAsAdmin(language, adminUserName, adminPassword);
            allowAccessToSpecificUser(templateName, templateType,
                    allowAccessToUser);
            verifyAccessToSpecificUser(flexProduct, username, password,
                    language, templateName, allowAccessToUser);
            loginAsAdmin(language, adminUserName, adminPassword);
            softasssert.assertAll();
            CSLogger.info("verify allow access by option test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerifyAllowAccessByOption",
                    e);
            Assert.fail("Automation Error : testVerifyAllowAccessByOption", e);
        }
    }

    /**
     * This method verifies Access allowed to Specific user.
     * 
     * @param flexProduct String object contains product name
     * @param username String object contains user name
     * @param password String object contains password
     * @param language String object contains language used at time of login
     * @param templateName String object contains template name
     * @param allowAccessToUser String object contains user name to assign
     *            access
     */
    private void verifyAccessToSpecificUser(String flexProduct, String username,
            String password, String language, String templateName,
            String allowAccessToUser) {
        String[] nameOfUser = username.split(",");
        for (String name : nameOfUser) {
            logoutFromCurrentUser();
            loginAsCreatedUser(name, password, language);
            loginPage.clkLoginButton();
            CSUtility.tempMethodForThreadSleep(2000);
            loginPage.clkLoginButton();
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            getConfigureTableDialogBox(flexProduct);
            if (name.equals(allowAccessToUser)) {
                chkPresenceOfTemplate(templateName, true);
            } else {
                chkPresenceOfTemplate(templateName, false);
            }
        }
    }

    /**
     * This method login as admin.
     * 
     * @param language String object contains language used at time of login
     * @param adminUserName String object contains admin username
     * @param adminPassword String object contains admin Password
     */
    private void loginAsAdmin(String language, String adminUserName,
            String adminPassword) {
        logoutFromCurrentUser();
        loginAsCreatedUser(adminUserName, adminPassword, language);
        loginPage.clkLoginButton();
        CSUtility.tempMethodForThreadSleep(1000);
        loginPage.clkLoginButton();
        CSLogger.info("Login as user Admin");
    }

    /**
     * This method verifies Access allowed to all users.
     * 
     * @param flexProduct String object contains product name
     * @param username String object contains user name
     * @param password String object contains password
     * @param language String object contains language used at time of login
     * @param templateName String object contains template name
     */
    private void verifyAccessToAllUser(String flexProduct, String username,
            String password, String language, String templateName) {
        String[] nameOfUser = username.split(",");
        for (String name : nameOfUser) {
            logoutFromCurrentUser();
            loginAsCreatedUser(name, password, language);
            loginPage.clkLoginButton();
            clkAcceptBth();
            CSUtility.tempMethodForThreadSleep(2000);
            loginPage.clkLoginButton();
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            getConfigureTableDialogBox(flexProduct);
            chkPresenceOfTemplate(templateName, true);
        }
    }

    /**
     * This method verifies the presence of template in drop down list
     * 
     * @param templateName String object contains template name
     * @param tempPresent boolean object contains condition for presence of
     *            template
     */
    private void chkPresenceOfTemplate(String templateName,
            Boolean tempPresent) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnPresetId());
        int presetIds = browserDriver
                .findElements(By.xpath("//select[@id='PresetID']//option"))
                .size();
        ArrayList<String> allOptions = new ArrayList<>();
        for (int row = 1; row < presetIds; row++) {
            allOptions.add(browserDriver
                    .findElements(By.xpath("//select[@id='PresetID']//option"))
                    .get(row).getText());
        }
        if (tempPresent) {
            if (!allOptions.contains(templateName)) {
                softasssert.fail(templateName + " has displayed");
            } else {
                CSLogger.info(
                        templateName + " has not displayed test case pass.");
            }
        } else {
            if (allOptions.contains(templateName)) {
                softasssert.fail(templateName + " has displayed");
            } else {
                CSLogger.info(templateName + " has displayed test case pass.");
            }
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getBtnOk());
        flexTablePage.getBtnOk().click();
    }

    /**
     * This method get the dialog box configuration table.
     * 
     * @param flexProduct String object contains product name
     */
    private void getConfigureTableDialogBox(String flexProduct) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement flexTableProduct = browserDriver
                .findElement(By.linkText(flexProduct));
        flexTableProduct.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        checkInProduct();
        checkOutProduct();
        flexTablePage.clkOnDataTab(waitForReload);
        WebElement rightSectionTable = browserDriver.findElement(By.xpath(
                "(//table[@class='Flex Standard FlexTable']/thead/tr/th)[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, rightSectionTable);
        action.moveToElement(rightSectionTable).perform();
        CSUtility.tempMethodForThreadSleep(1000);
        flexTablePage.getBtnFlexSetting().click();
        CSLogger.info("Clicked on add button");
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
    }

    /**
     * This method performs checkout operation on product folder
     * 
     */
    private void checkOutProduct() {
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        csGuiToolbarHorizontalInstance.clkBtnProductCheckOut(waitForReload);
        CSLogger.info("Product is checked out");
    }

    /**
     * This method performs checkin operation on product folder
     */
    public void checkInProduct() {
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        csGuiToolbarHorizontalInstance.clkBtnProductCheckIn(waitForReload);
        CSLogger.info("Product is checked In");
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
     * This method logout from current user
     * 
     */
    private void logoutFromCurrentUser() {
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
     * This method click portal link option
     * 
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * This method Allow Access to specific user.
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     * @param allowAccessToUser String object contains user name to assign
     *            access
     */
    private void allowAccessToSpecificUser(String templateName,
            String templateType, String allowAccessToUser) {
        WebElement elementUser = null;
        String userName = null;
        goToGeneralPane(templateName, templateType);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexPresetUserAccess());
        Select userAccess = new Select(
                flexTablePage.getDrpDwnFlexPresetUserAccess());
        userAccess.selectByVisibleText("The following Users");
        CSUtility.tempMethodForThreadSleep(2000);
        flexTablePage.clkOnBtnAddChooserContainer(waitForReload);
        TraverseSelectionDialogFrames.traverseTillUserFoldersleftFrames(
                waitForReload, browserDriver);
        WebElement userNode = browserDriver
                .findElement(By.xpath("//span[@id='User@0']"));
        clkElement(userNode);
        TraverseSelectionDialogFrames.traverseTillUserFoldersCenterPane(
                waitForReload, browserDriver);
        int count = browserDriver
                .findElements(By.xpath(
                        "//table[@class='hidewrap CSAdminList']//tbody//tr"))
                .size();
        for (int row = 3; row <= count; row++) {
            elementUser = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr["
                            + row + "]//td[2]"));
            userName = elementUser.getText();
            if (userName.equals(allowAccessToUser)) {
                action.doubleClick(elementUser).build().perform();
                break;
            }
        }
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method Allow Access to all users.
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     */
    private void allowAccessToAllUser(String templateName,
            String templateType) {
        goToGeneralPane(templateName, templateType);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexPresetUserAccess());
        Select userAccess = new Select(
                flexTablePage.getDrpDwnFlexPresetUserAccess());
        userAccess.selectByVisibleText("All Users");
        userManagementPage.clkBtnSave();
    }

    /**
     * This method traverse to general pane section.
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     */
    private void goToGeneralPane(String templateName, String templateType) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement freeNode = browserDriver
                .findElement(By.linkText(templateType));
        clkElement(freeNode);
        WebElement nameOfTemplate = browserDriver
                .findElement(By.linkText(templateName));
        clkElement(nameOfTemplate);
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnGeneralPane(waitForReload);
    }

    /**
     * This method clicks on input element
     * 
     * @param element contains web element to be clicked
     */
    public void clkElement(WebElement element) {
        CSUtility.waitForElementToBeClickable(waitForReload, element);
        element.click();
    }

    /**
     * This method creates the new user group
     * 
     * @param userGroup contains the name of the user group to be created
     */
    private void createUser(String username, String password, String roleName,
            String accessRight) {
        String[] nameOfUser = username.split(",");
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        for (String name : nameOfUser) {
            traverseToSystemPreferences();
            traverseToUserManagement();
            clkUsers();
            createUsernameAndPassword(name, password);
            traverseUptoRightsTab();
            addRoleAndRight(roleName, accessRight);
        }
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
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Clicked on save button");
    }

    /**
     * This method traverses upto Rights tab
     * 
     */
    private void traverseUptoRightsTab() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        clkRightsTab();
        traverseToFormLabelAdd();
    }

    /**
     * This method clicks on the rights tab
     * 
     */
    private void clkRightsTab() {
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
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
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmFrameSettings()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getFormLabelAdd()));
        userManagementPage.getFormLabelAdd().click();
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
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        clkCreateNew();
        clkAdministrationTab(username, password);
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
     * This method saves contents by clicking on save button
     * 
     */
    private void saveContents() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
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
    }

    /**
     * This method traverses to user management after clicking system
     * preferences
     * 
     */
    private void traverseToUserManagement() {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
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
     * This method create product.
     * 
     * @param productName String object contains product name
     */
    private void createProduct(String flexProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, flexProduct);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Product Created");
    }

    /**
     * This method drag and drop class to product.
     * 
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     */
    private void dragAndDropClassToProduct(String className,
            String flexProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToDragDrop = browserDriver
                .findElement(By.linkText(flexProduct));
        clkOnPimSettingsTree();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode().click();
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        action.dragAndDrop(classNameToDragDrop, productToDragDrop).build()
                .perform();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentIdInstance.getBtnReplace()));
        csGuiDialogContentIdInstance.getBtnReplace().click();
        CSLogger.info("Drag and Drop class to Product");
    }

    /**
     * Clicks on PIM settings tree node
     */
    private void clkOnPimSettingsTree() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOf(
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode()));
        CSUtility.scrollUpOrDownToElement(
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode(),
                browserDriver);
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        CSLogger.info("Clicked on pim settings tree");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains ClassName,FlexTableProductName,RoleName,AccessRightName,Username
     * ,Password,Language,TemplateName,TemplateType,AllowAccessToUser,
     * AdminUserName,AdminPassword
     * 
     * @return allowAccessByOptionTestData
     */
    @DataProvider(name = "AllowAccessByOptionDataSheet")
    public Object[] allowAccessByOption() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                allowAccessByOptionTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        action = new Actions(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        loginPage = new LoginPage(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        softasssert = new SoftAssert();
    }
}
