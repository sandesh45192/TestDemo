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
 * This method deletes the access level
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteAccessLevelTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private UserManagementPage          userManagementPage;
    private String                      deleteAccessLevelSheet = "DeleteAccessLevelSheet";
    private IUserManagementPopup        userManagementPopup;
    private CSPortalWidget              csPortalWidgetInstance;
    private FrameLocators               iframeLocatorsInstance;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;

    /**
     * This method creates and deletes access levels in three different ways
     * 
     * @param accessLevelName contains the name of the access level
     */
    @Test(priority = 1, dataProvider = "deleteAccessLevel")
    public void testDeleteAccessLevel(String accessLevelName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            String[] accessLevels = accessLevelName.split(",");
            String accessLevel1 = accessLevels[0];
            String accessLevel2 = accessLevels[1];
            String accessLevel3 = accessLevels[2];
            for (int accessLevelIndex = 0; accessLevelIndex < accessLevels.length; accessLevelIndex++) {
                createAccessLevel(accessLevels[accessLevelIndex]);
            }
            performUseCase(accessLevel1, accessLevel2, accessLevel3);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method deletes the three created access level in three different
     * ways
     * 
     * @param accessLevel1 contains the name of access level
     * @param accessLevel2 contains the name of access level
     * @param accessLevel3 contains the name of access level
     */
    private void performUseCase(String accessLevel1, String accessLevel2,
            String accessLevel3) {
        deleteLevelFromTreeView(accessLevel1, false);
        deleteLevelFromTreeView(accessLevel1, true);
        verifyDeletedLevelFromTreeView(accessLevel1);
        deleteLevelFromListView(accessLevel2, false);
        deleteLevelFromListView(accessLevel2, true);
        verifyDeletedLevelFromListAndMassEdit(accessLevel2);
        deleteLevelFromMassEditOption(accessLevel3, false);
        deleteLevelFromMassEditOption(accessLevel3, true);
        verifyDeletedLevelFromListAndMassEdit(accessLevel3);
    }

    /**
     * This method creates access level
     * 
     * @param accessLevelName contains the name of the access level
     */
    private void createAccessLevel(String accessLevelName) {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkAccessRights();
        clkCreateAccessLevel(accessLevelName);
    }

    /**
     * This method clicks on create new access level option
     * 
     * @param accessLevelName contains the name of the access level to be
     *            created
     */
    private void clkCreateAccessLevel(String accessLevelName) {
        rightClkElement(userManagementPage.getAccessRightsNode());
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupNewAccessLevel(), browserDriver);
        CSLogger.info("Clicked on create new access level ");
        userManagementPopup.handlePopup(waitForReload, accessLevelName,
                browserDriver);
        userManagementPopup.clkOkOfPopup(waitForReload);
    }

    /**
     * This method right clicks on webelement
     * 
     * @param levelName contains the name of the access level
     */
    private void rightClkElement(WebElement levelName) {
        CSUtility.rightClickTreeNode(waitForReload, levelName, browserDriver);
        CSLogger.info("Right clicked on created level");
    }

    /**
     * This method clicks on Access Rights node
     */
    private void clkAccessRights() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getAccessRightsNode()));
        userManagementPage.getAccessRightsNode().click();
        CSLogger.info("Clicked on Access Rights node");
    }

    /**
     * This method deletes access level from tree view
     * 
     * @param accessLevelName contains the name of the access level
     * @param alertboxStatus contains boolean value to click cancel and ok
     *            option resp.
     */
    private void deleteLevelFromTreeView(String accessLevelName,
            boolean alertboxStatus) {
        clkCreatedAccessLevel(accessLevelName);
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
     * This method clicks on created access level
     * 
     * @param accessLevelName contains the name of the access level
     */
    private void clkCreatedAccessLevel(String accessLevelName) {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkAccessRights();
        WebElement accessLevel = browserDriver
                .findElement(By.linkText(accessLevelName));
        accessLevel.click();
        CSUtility.rightClickTreeNode(waitForReload, accessLevel, browserDriver);
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
     * This method verifies if access level has successfully gotten deleted from
     * tree view or not
     * 
     * @param accessLevel1 contains the name of the access level
     */
    private void verifyDeletedLevelFromTreeView(String accessLevel1) {
        try {
            TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkAccessRights();
            WebElement accessLevel = browserDriver
                    .findElement(By.linkText(accessLevel1));
            if (accessLevel.getText().equals(accessLevel1)) {
                CSLogger.info("Could not delete access level");
            }
        } catch (Exception e) {
            CSLogger.info("Verified.Access level successfully deleted");
        }
    }

    /**
     * This method deletes access level from list view which opens in right pane
     * after clicking the respective access level
     * 
     * @param accessLevel2 contains the name of the access level
     * @param alertboxStatus contains boolean value
     */
    private void deleteLevelFromListView(String accessLevel2,
            boolean alertboxStatus) {
        traverseUptoRightSectionPane();
        findElementsInList(accessLevel2);
        WebElement listElement = performClickOnTranslationLabelMidPane(
                accessLevel2);
        if (listElement.getText().equals(accessLevel2)) {
            CSUtility.rightClickTreeNode(waitForReload, listElement,
                    browserDriver);
            userManagementPopup.selectPopupDivMenu(waitForReload,
                    userManagementPopup.getCsPopupObject(), browserDriver);
            userManagementPopup.selectPopupDivSubMenu(waitForReload,
                    userManagementPopup.getCsPopupDelete(), browserDriver);
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            if (alertboxStatus == false) {
                alert.dismiss();
                CSLogger.info("Clicked on cancel of pop up");
            }
            if (alertboxStatus == true) {
                alert.accept();
                CSLogger.info("Clicked on ok of pop up");
            }
        }
    }

    /**
     * This method verifies if access level has successfully deleted from list
     * view or not
     * 
     * @param accessLevel2 contains the name of the access level
     */
    private void verifyDeletedLevelFromListAndMassEdit(String accessLevel2) {
        try {
            boolean status = false;
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, iframeLocatorsInstance);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            traverseUptoRightSectionPane();
            findElementsInList(accessLevel2);
            List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                    "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
            if (listLabels.size() != 0) {
                for (int listIndex = 0; listIndex < listLabels
                        .size(); listIndex++) {
                    WebElement listElement = listLabels.get(listIndex);
                    if (listElement.getText().equals(accessLevel2)) {
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
     * This method finds elements from the list view
     * 
     * @return list containing all elements from the list view
     */
    private void findElementsInList(String accessLevel2) {
        CSUtility.tempMethodForThreadSleep(1000);
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//img[contains(@src,'nofilter.gif')]/.."))
                .size();
        if (chkPresenceOfNoFilter == 0) {
            int chkPresenceOfTextbox = browserDriver
                    .findElements(By.xpath("//input[@id='Filter_RubricName']"))
                    .size();
            if (chkPresenceOfTextbox != 0) {
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        csGuiToolbarHorizontal.getBtnFilter());
                csGuiToolbarHorizontal.getBtnFilter().click();
                WebElement txtFilterBar = browserDriver.findElement(
                        By.xpath("//input[@id='Filter_RubricName']"));
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        txtFilterBar);
                CSUtility.tempMethodForThreadSleep(1000);
                txtFilterBar.clear();
                txtFilterBar.sendKeys(accessLevel2);
                CSLogger.info(
                        "Entered text  " + accessLevel2 + " in filter bar");
                txtFilterBar.sendKeys(Keys.ENTER);
            }
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
     * This method deletes access level from mass edit option
     * 
     * @param accessLevel3 contains name of the access level
     * @param alertboxStatus contains boolean value
     */
    private void deleteLevelFromMassEditOption(String accessLevel3,
            boolean alertboxStatus) {
        WebElement listElement = null;
        boolean status = false;
        try {
            traverseUptoRightSectionPane();
            findElementsInList(accessLevel3);
            List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                    "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
            if (listLabels.size() != 0) {
                for (int listIndex = 0; listIndex < listLabels
                        .size(); listIndex++) {
                    listElement = listLabels.get(listIndex);
                    if (listElement.getText().equals(accessLevel3)) {
                        status = true;
                        break;
                    }
                }
                if (status == true) {
                    listElement.click();
                    WebElement chkbox = browserDriver.findElement(
                            By.xpath("//td/a[contains(text(),'" + accessLevel3
                                    + "')]/../../td/input[@type='checkbox']"));
                    CSUtility.waitForVisibilityOfElement(waitForReload, chkbox);
                    chkbox.click();
                    CSUtility.tempMethodForThreadSleep(2000);
                    selectDeleteOption(alertboxStatus);
                }
            }

        } catch (Exception e) {
            CSLogger.debug(
                    "Could not delete access level from mass edit option", e);
        }
    }

    /**
     * This method selects delete option from the drop down
     * 
     * @param status contains boolean value to click on ok and cancel for
     *            deleting
     */
    private void selectDeleteOption(boolean status) {
        Select deleteOption = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        deleteOption.selectByVisibleText("Delete");
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (status == false) {
            alert.dismiss();
            CSLogger.info("Clicked on cancel ");
        } else if (status == true) {
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
        clkAccessRights();
        clkAccessRights();
        CSUtility.tempMethodForThreadSleep(3000);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method returns the sheet data which contains access level names
     * 
     * @return deleteAccessLevelSheet
     */
    @DataProvider(name = "deleteAccessLevel")
    public Object[] DeleteAccessLevelSheetData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                deleteAccessLevelSheet);
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
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
    }
}
