/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
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
 * This class creates and deletes user roles using three different methods
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteUserRoleTest extends AbstractTest {

    private CSPortalHeader         csPortalHeader;
    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private UserManagementPage     userManagementPage;
    private String                 deleteUserRoleSheet = "DeleteUserRoleSheet";
    private IUserManagementPopup   userManagementPopup;
    private CSPortalWidget         csPortalWidgetInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontal;

    /**
     * This test method creates and deletes user roles by three different
     * methods
     * 
     * @param userRoleNames contains user role names from sheet
     */
    @Test(priority = 1, dataProvider = "deleteUserRole")
    public void testDeleteUserRoleTest(String userRoleNames) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            String[] userRoles = userRoleNames.split(",");
            String userRole1 = userRoles[0];
            String userRole2 = userRoles[1];
            String userRole3 = userRoles[2];
            for (int userRoleIndex = 0; userRoleIndex < userRoles.length; userRoleIndex++) {
                createUserRoles(userRoles[userRoleIndex]);
            }
            performUseCase(userRole1, userRole2, userRole3);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method deletes user roles by three methods and verifies if the user
     * roles got deleted
     * 
     * @param userRole1 contains name of first user role
     * @param userRole2 contains name of second user role
     * @param userRole3 contains name of the third user role
     */
    private void performUseCase(String userRole1, String userRole2,
            String userRole3) {
        deleteRoleFromTreeView(userRole1, false);
        deleteRoleFromTreeView(userRole1, true);
        verifyDeletedRoleFromTreeView(userRole1);
        deleteRoleFromListView(userRole2, false);
        deleteRoleFromListView(userRole2, true);
        verifyDeletedRoleFromListAndMassEdit(userRole2);
        deleteRoleFromMassEditOption(userRole3, false);
        deleteRoleFromMassEditOption(userRole3, true);
        verifyDeletedRoleFromListAndMassEdit(userRole3);
    }

    /**
     * This method deletes role from mass edit option
     * 
     * @param userRole3 contains the name of the user role
     * @param alertboxStatus contains the boolean value
     */
    private void deleteRoleFromMassEditOption(String userRole3,
            boolean alertboxStatus) {
        WebElement listElement = null;
        boolean status = false;
        try {
            traverseUptoRightSectionPane();
            findElementsInList(userRole3);
            List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                    "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
            if (listLabels.size() != 0) {
                for (int listIndex = 0; listIndex < listLabels
                        .size(); listIndex++) {
                    listElement = listLabels.get(listIndex);
                    if (listElement.getText().equals(userRole3)) {
                        status = true;
                        break;
                    }
                }
                if (status == true) {
                    listElement.click();
                    WebElement chkbox = browserDriver.findElement(
                            By.xpath("//td[contains(text(),'" + userRole3
                                    + "')]/../td/input[@type='checkbox']"));
                    CSUtility.waitForVisibilityOfElement(waitForReload, chkbox);
                    chkbox.click();
                    CSUtility.tempMethodForThreadSleep(2000);
                    selectDeleteOption(alertboxStatus);
                }
            }

        } catch (Exception e) {
            CSLogger.debug("Could not delete role ", e);
        }
    }

    /**
     * This method selects delete option from the drop down
     * 
     * @param alertboxStatus contains boolean value to click on ok and cancel
     *            for deleting
     */
    private void selectDeleteOption(boolean alertboxStatus) {
        Select deleteOption = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        deleteOption.selectByVisibleText("Delete");
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (alertboxStatus == false) {
            alert.dismiss();
            CSLogger.info("Clicked on cancel ");
        } else if (alertboxStatus == true) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            CSUtility.tempMethodForThreadSleep(4000);
        }
    }

    /**
     * This method traverses upto right section pane
     */
    private void traverseUptoRightSectionPane() {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkRoles();
        clkRoles();
        CSUtility.tempMethodForThreadSleep(3000);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method finds elements from the list view
     * 
     * @return list containing all elements from the list view
     */
    private void findElementsInList(String userRole2) {
        CSUtility.tempMethodForThreadSleep(1000);
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//img[contains(@src,'nofilter.gif')]/.."))
                .size();
        if (chkPresenceOfNoFilter == 0) {
            int chkPresenceOfTextbox = browserDriver
                    .findElements(By.xpath("//input[@id='Filter_RoleName']"))
                    .size();
            if (chkPresenceOfTextbox != 0) {
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        csGuiToolbarHorizontal.getBtnFilter());
                csGuiToolbarHorizontal.getBtnFilter().click();
                WebElement txtFilterBar = browserDriver.findElement(
                        By.xpath("//input[@id='Filter_RoleName']"));
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        txtFilterBar);
                CSUtility.tempMethodForThreadSleep(1000);
                txtFilterBar.clear();
                txtFilterBar.sendKeys(userRole2);
                CSLogger.info("Entered text  " + userRole2 + " in filter bar");
                txtFilterBar.sendKeys(Keys.ENTER);
            }
        }
    }

    /**
     * This method deletes role from tree view
     * 
     * @param userRole contains the name of the user role in String format
     * @param alertboxStatus contains boolean value
     */
    private void deleteRoleFromTreeView(String userRole,
            boolean alertboxStatus) {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkRoles();
        clkCreatedUserRole(userRole);
        clkDelete();
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (alertboxStatus == false) {
            alertBox.dismiss();
            CSLogger.info("Clicked on cancel of pop up");
        } else if (alertboxStatus == true) {
            alertBox.accept();
            CSLogger.info("Clicked on ok of pop up");
        }
    }

    /**
     * This method clicks on created role
     * 
     * @param userRoleName contains name of user role
     */
    private void clkCreatedUserRole(String userRoleName) {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkRoles();
        WebElement userRole = browserDriver
                .findElement(By.linkText(userRoleName));
        waitForReload.until(ExpectedConditions.visibilityOf(userRole));
        userRole.click();
        CSUtility.rightClickTreeNode(waitForReload, userRole, browserDriver);
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupObject(), browserDriver);
    }

    /**
     * This method clicks on delete option from pop up after right click
     */
    private void clkDelete() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getFrmCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivSubFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPopup.getCsPopupDelete()));
        userManagementPopup.getCsPopupDelete().click();
        CSLogger.info("Clicked on delete option");
    }

    /**
     * This method creates the user roles
     * 
     * @param roleName contains the role name
     */
    private void createUserRoles(String roleName) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkRoles();
        createNewRole(roleName);
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
            CSLogger.debug(
                    "Exception occured while clicking on create new option");
        }
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
        CSUtility.tempMethodForThreadSleep(2000);
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
     * This method right clicks on Roles node
     */
    private void rightClickRoles() {
        CSUtility.rightClickTreeNode(waitForReload,
                userManagementPage.getUserManagementRoles(), browserDriver);
    }

    /**
     * This method enables checkbox of entering Role name if disabled
     */
    private void enableCheckbox() {
        WebElement checkBox = waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getEditRoleCheckbox()));
        if (!checkBox.isSelected()) {
            userManagementPage.getEditRoleCheckbox().click();
            CSLogger.info("Checkbox has enabled ");
        } else {
            CSLogger.info("Checkbox is already enabled ");
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
     * Verifies deleted role from the tree view
     * 
     * @param userRole1 contains name of role
     */
    private void verifyDeletedRoleFromTreeView(String userRole1) {
        try {
            TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkRoles();
            WebElement userRole = browserDriver
                    .findElement(By.linkText(userRole1));
            if (userRole.getText().equals(userRole1)) {
                CSLogger.error(
                        "Could not delete user role . Verification failed.");
            }
        } catch (Exception e) {
            CSLogger.info("Verified. Level is successfully deleted.");
        }
    }

    /**
     * This method deletes the role from the list view
     * 
     * @param userRole2 contains the name of user role
     * @param alertboxStatus contains boolean value
     */
    private void deleteRoleFromListView(String userRole2,
            boolean alertboxStatus) {
        try {
            traverseUptoRightSectionPane();
            findElementsInList(userRole2);
            WebElement listElement = performClickOnTranslationLabelMidPane(
                    userRole2);
            if (listElement.getText().equals(userRole2)) {
                CSUtility.rightClickTreeNode(waitForReload, listElement,
                        browserDriver);
                userManagementPopup.selectPopupDivMenu(waitForReload,
                        userManagementPopup.getCsPopupObject(), browserDriver);
                userManagementPopup.selectPopupDivSubMenu(waitForReload,
                        userManagementPopup.getCsPopupDelete(), browserDriver);
                Alert alert = CSUtility.getAlertBox(waitForReload,
                        browserDriver);
                if (alertboxStatus == false) {
                    alert.dismiss();
                    CSLogger.info("Clicked on cancel of pop up");
                }
                if (alertboxStatus == true) {
                    alert.accept();
                    CSLogger.info("Clicked on ok of pop up");
                }

            }
        } catch (Exception e) {
            CSLogger.debug("Could not delete role from list view", e);
        }
    }

    /**
     * This method perfoms click on the translation Lable from the mid pane
     * 
     * @param translationLable contains label to be searched
     */
    private WebElement
            performClickOnTranslationLabelMidPane(String accessLevel2) {
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
        for (Iterator<WebElement> iterator = listLabels.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(accessLevel2)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSUtility.tempMethodForThreadSleep(4000);
            CSUtility.waitForVisibilityOfElement(waitForReload, listElement);
            listElement.click();
            CSLogger.info("clicked on translation label in the mid pane");
        } else {
            CSLogger.error(
                    "Could not find the translation label name in the mid pane");
        }
        return listElement;
    }

    /**
     * This method verifies deleted role from list view
     * 
     * @param userRole2 contains the name of user role
     */
    private void verifyDeletedRoleFromListAndMassEdit(String userRole2) {
        try {
            boolean status = false;
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            traverseUptoRightSectionPane();
            findElementsInList(userRole2);
            List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                    "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
            if (listLabels.size() != 0) {
                for (int listIndex = 0; listIndex < listLabels
                        .size(); listIndex++) {
                    WebElement listElement = listLabels.get(listIndex);
                    if (listElement.getText().equals(userRole2)) {
                        status = true;
                        disableFilterBar();
                        break;
                    }
                }
                if (status == true) {
                    Assert.fail("Verification failed for the element  ");
                } else {
                    CSLogger.info("Deleted access level   successfully");
                }
            } else {
                CSLogger.info("Element deleted successfully");
            }
            disableFilterBar();
        } catch (Exception e) {
            CSLogger.debug("Verification failed. " + e);
        }
    }

    /**
     * This method disables the filter bar which had been opened for searching
     * the translation label from the mid pane
     */
    private void disableFilterBar() {
        traverseUptoRightSectionPane();
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//a/img[contains(@src,'nofilter.gif')]"))
                .size();
        if (chkPresenceOfNoFilter != 0) {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontal.getbtnNoFilter());
            csGuiToolbarHorizontal.getbtnNoFilter().click();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontal.getBtnFilter());
            csGuiToolbarHorizontal.getBtnFilter().click();
            CSUtility.tempMethodForThreadSleep(1000);
        }
    }

    /**
     * This data provider returns sheet data which contains user role names
     * 
     * @return deleteUserRoleSheet
     */
    @DataProvider(name = "deleteUserRole")
    public Object[] DeleteAccessLevelSheetData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                deleteUserRoleSheet);
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
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
    }
}
