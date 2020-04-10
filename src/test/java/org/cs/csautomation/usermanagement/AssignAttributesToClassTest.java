/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class assigns attribute to class and verifies if assigned attribute is
 * present or not
 * 
 * @author CSAutomation Team
 *
 */
public class AssignAttributesToClassTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private UserManagementPage          userManagementPage;
    private String                      assignAttributeToClassSheet = "AssignAttributeToClass";
    private IUserManagementPopup        userManagementPopup;
    private TraversingForSettingsModule traversingForSettingsModule;
    private FrameLocators               locator;
    private Actions                     actions;
    private CSPortalWidget              csPortalWidgetInstance;
    private SoftAssert                  softAssert;

    /**
     * This method assigns attributes to class and verifies if assigned
     * attribute is present
     * 
     * @param attributeFolderName contains name of attribute folder
     * @param className contains name of class
     * @param attributeName contains name of attribute
     * @param userGroup contains user group
     */
    @Test(priority = 1, dataProvider = "assignAttributeToClass")
    public void testAssignAttributesToClassTest(String attributeFolderName,
            String className, String attributeName, String userGroup) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            verifyCreatedAttributeFolder(attributeFolderName, className,
                    attributeName);
            verifyCreatedAttribute(className, attributeName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test Case failed.");
        }
    }

    /**
     * This data provider performs drag and drop of class to created user group
     * 
     * @param attributeFolderName contains name of attribute
     * @param className contains name of class
     * @param attributeName contains attribute name
     * @param userGroup contains user group
     */
    @Test(priority = 2, dataProvider = "assignAttributeToClass")
    public void testDragDropClassToUserGroup(String attributeFolderName,
            String className, String attributeName, String userGroup) {
        try {
            performDragAndDrop(className, userGroup);
            performClick("Cancel");
            performDragAndDrop(className, userGroup);
            performClick("Extend");
            performDragAndDrop(className, userGroup);
            performClick("Replace");
            verifyAttributePresenceInDataPane(attributeName, userGroup);
        } catch (Exception e) {
            CSLogger.debug("Test case failed");
            Assert.fail("Could not assign class to user group");
        }
    }

    /**
     * This method clicks on Attributes node
     */
    private WebElement clkAttributes(String attributeFolderName) {
        waitForReload.until(ExpectedConditions.visibilityOf(
                userManagementPage.getUserManagementAttributes()));
        userManagementPage.getUserManagementAttributes().click();
        CSLogger.info("Clicked on Attributes node ");
        WebElement attributeFolder = browserDriver
                .findElement(By.linkText(attributeFolderName));
        return attributeFolder;
    }

    /**
     * This method assigns attribute to class
     * 
     * @param attributeFolderName contains attribute folder name
     * @param className contains class name
     * @param attributeName contains attribute name
     * @param status contains boolean value
     */
    private void assignAttributeToClass(String attributeFolderName,
            String className, String attributeName, boolean status) {
        TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkCreatedClass(className);
        clkPlusToAssignAttribute();
        clkAttributesNode();
        WebElement attributeFolder = browserDriver
                .findElement(By.linkText(attributeFolderName));
        attributeFolder.click();
        CSLogger.info("Clicked on Attribute folder under Attributes node");
        traverseToCenterFrame();
        clkCreatedAttribute(attributeName);
        if (!status) {
            clkCancel();
        } else if (status) {
            clkOk();
        }
    }

    /**
     * This method clicks cancel
     */
    private void clkCancel() {
        traverseToPortalWindowContentFrame();
        userManagementPopup.clkCancelOfPopup(waitForReload);
    }

    /**
     * This method clicks ok button
     */
    private void clkOk() {
        traverseToPortalWindowContentFrame();
        userManagementPopup.clkOkOfPopup(waitForReload);
        clkSave();
    }

    /**
     * This method clicks on save button
     */
    private void clkSave() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                userManagementPage.getBtnSave());
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(userManagementPage.getBtnSave()));
        userManagementPage.getBtnSave().click();
        CSLogger.info("Clicked on Save button to add attribute");
    }

    /**
     * This method traverses to Portal window content frame
     */
    private void traverseToPortalWindowContentFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
    }

    /**
     * This method clicks created class
     * 
     * @param className contains name of class
     */
    private void clkCreatedClass(String className) {
        clkClasses();
        WebElement nameOfClass = browserDriver
                .findElement(By.linkText(className));
        nameOfClass.click();
        CSLogger.info("Clicked on created class");
    }

    /**
     * This method verifies created attribute folder
     * 
     * @param attributeFolderName contains attribute folder name
     * @param className contains name of class
     * @param attributeName contains name of attribute
     */
    private void verifyCreatedAttributeFolder(String attributeFolderName,
            String className, String attributeName) {
        try {
            TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            WebElement attributeFolder = clkAttributes(attributeFolderName);
            if (attributeFolder.getText().equals(attributeFolderName)) {
                assignAttributeToClass(attributeFolderName, className,
                        attributeName, false);
                assignAttributeToClass(attributeFolderName, className,
                        attributeName, true);
            } else {
                Assert.fail("Attribute folder is not present. ");
            }
        } catch (Exception e) {
            CSLogger.debug("Verification of created attribute failed", e);
        }
    }

    /**
     * This method clicks classes node
     */
    private void clkClasses() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getClassesNode()));
        userManagementPage.getClassesNode().click();
        CSLogger.info("Clicked on Classes node ");
    }

    /**
     * This method traverses to center frame in data selection dialog
     */
    private void traverseToCenterFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabBodyUserManagement()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsCenter()));
        CSLogger.info("Traversed till record center frame");
    }

    /**
     * This method clicks on plus button to add attribute
     */
    private void clkPlusToAssignAttribute() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                userManagementPage.getBtnPlusToAddAttribute());
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                userManagementPage.getBtnPlusToAddAttribute()));
        userManagementPage.getBtnPlusToAddAttribute().click();
        CSLogger.info("Clicked on Plus to assign attribute");

    }

    /**
     * This method clicks on Attributes node
     */
    private void clkAttributesNode() {
        traverseToLeftRecordFrames();
        waitForReload.until(ExpectedConditions.visibilityOf(
                userManagementPage.getAttributesTabInDialogWindow()));
        userManagementPage.getAttributesTabInDialogWindow().click();
        CSLogger.info("Clicked on attributes tab in left section");
    }

    /**
     * This method traverses to left record frame in data selection dialog
     * window
     */
    private void traverseToLeftRecordFrames() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabBodyUserManagement()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsUserConfigurationLeft()));
        CSLogger.info("Traversed till left records frame");
    }

    /**
     * This method clicks on created attribute
     * 
     * @param attributeName contains attribute name
     */
    private void clkCreatedAttribute(String attributeName) {
        WebElement createdAttribute = browserDriver.findElement(By.xpath(
                "//table[contains(@class,'CSGuiList')]/tbody/tr/td/div/div/div/table/tbody/tr/td[3]"));
        createdAttribute.click();
        CSLogger.info("Clicked on created attribute");
    }

    /**
     * This method verifies created attribute
     * 
     * @param className contains name of class
     * @param attributeName contains attribute name
     */
    private void verifyCreatedAttribute(String className,
            String attributeName) {
        try {
            TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkClasses();
            clkCreatedClass(className);
            CSUtility.tempMethodForThreadSleep(2000);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            WebElement attribute = browserDriver.findElement(
                    By.xpath("//table[@class='CSGuiTable']/tbody/tr[2]/td[1]"));
            Assert.assertEquals(attributeName, attribute.getText());
        } catch (Exception e) {
            CSLogger.debug("Verification of created attribute failed", e);
        }
    }

    /**
     * This method performs drag and drop
     * 
     * @param className contains name of class
     * @param userGroup contains name of created user group
     */
    private void performDragAndDrop(String className, String userGroup) {
        try {
            TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkClasses();
            WebElement createdClass = browserDriver
                    .findElement(By.linkText(className));
            clkUsers();
            WebElement createdUserGroup = browserDriver
                    .findElement(By.linkText(userGroup));
            actions.dragAndDrop(createdClass, createdUserGroup).build()
                    .perform();
            CSLogger.info("Drag and drop successful");
        } catch (Exception e) {
            CSLogger.debug("Failed to perform drag and drop" + e);
        }
    }

    /**
     * This method performs clicks on button based on input
     * 
     * @param selectButton contains string Cancel,Extend and Replace
     */
    private void performClick(String selectButton) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        if (selectButton.equals("Cancel")) {
            waitForReload.until(ExpectedConditions.visibilityOf(
                    userManagementPage.getBtnCancelUserManagement()));
            userManagementPage.getBtnCancelUserManagement().click();
            CSLogger.info("Clicked on Cancel");
        } else if (selectButton.equals("Extend")) {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(userManagementPage.getBtnExtend()));
            userManagementPage.getBtnExtend().click();
            CSLogger.info("Clicked on Extend");
        } else {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(userManagementPage.getBtnReplace()));
            userManagementPage.getBtnReplace().click();
            CSLogger.info("Clicked on Replace");
        }
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
     * This method verifies if attribute is present in data pane of user group
     * 
     * @param attributeName contains the attribute name
     * @param userGroup contains created user group name
     */
    private void verifyAttributePresenceInDataPane(String attributeName,
            String userGroup) {
        try {
            TraversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkUsers();
            clkCreatedUserGroup(userGroup);
            clkDataPane();
            WebElement attribute = browserDriver.findElement(By
                    .xpath("//tr[contains(@cs_name,'" + attributeName + "')]"));
            String verifyAttribute = attribute.getText();
            Assert.assertEquals(attributeName, verifyAttribute);
            CSLogger.info("Attribute is present");
        } catch (Exception e) {
            CSLogger.debug("Verification failed", e);
            softAssert.fail("Verification failed");
        }
    }

    /**
     * This method clicks on created user group
     * 
     * @param userGroup contains user group
     */
    private void clkCreatedUserGroup(String userGroup) {
        WebElement createdUserGroup = browserDriver
                .findElement(By.linkText(userGroup));
        waitForReload.until(
                ExpectedConditions.elementToBeClickable(createdUserGroup));
        CSUtility.rightClickTreeNode(waitForReload, createdUserGroup,
                browserDriver);
        clkEdit();
    }

    /**
     * This method clicks on Edit of pop up
     */
    private void clkEdit() {
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
     * This method clicks on data pane
     */
    private void clkDataPane() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getTabbedPaneTitleData()));
        userManagementPage.getTabbedPaneTitleData().click();
    }

    /**
     * This data provider returns the data sheet which contains attribute folder
     * name,class name ,attribute name, and created user group
     * 
     * @return assignAttributeToClassSheet
     */
    @DataProvider(name = "assignAttributeToClass")
    public Object[] AssignAttributesToClass() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                assignAttributeToClassSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        locator = FrameLocators.getIframeLocators(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        actions = new Actions(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        softAssert = new SoftAssert();
    }
}
