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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class creates class in user management and verifies if class has
 * successfully created
 * 
 * @author CSAutomation Team
 *
 */
public class CreateClassTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private UserManagementPage          userManagementPage;
    private String                      createClassSheet = "CreateClassSheet";
    private IUserManagementPopup        userManagementPopup;
    private TraversingForSettingsModule traversingForSettingsModule;
    private FrameLocators               iframeLocatorsInstance;
    private CSPortalWidget              csPortalWidgetInstance;

    /**
     * This test method creates class under classes folder and verifies if class
     * has successfully assigned or not
     * 
     * @param className contains the class name
     */
    @Test(priority = 1, dataProvider = "createClass")
    public void testCreateClass(String className) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, iframeLocatorsInstance);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            createClass(className, false);
            createClass(className, true);
            verifyCreatedClass(className);
        } catch (Exception e) {
            CSLogger.debug("Test case Failed" + e);
            Assert.fail("Test case failed");
        }
    }

    /**
     * This method performs the operations for creating class
     * 
     * @param className contains the class name
     * @param status contains boolean value
     */
    private void createClass(String className, boolean status) {
        try {
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkClasses();
            rightClkClassesNode();
            clkCreateNew(className);
            if (status == false) {
                clkCancel();
            } else if (status == true) {
                clkOk();
            }
        } catch (Exception e) {
            CSLogger.debug("Class creation failed" + e);
        }
    }

    /**
     * This method clicks on Ok
     */
    private void clkOk() {
        userManagementPopup.clkOkOfPopup(waitForReload);
    }

    /**
     * This method clicks on Cancel
     */
    private void clkCancel() {
        userManagementPopup.clkCancelOfPopup(waitForReload);
    }

    /**
     * This method right clicks on classes node
     */
    private void rightClkClassesNode() {
        CSUtility.rightClickTreeNode(waitForReload,
                userManagementPage.getClassesNode(), browserDriver);
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
     * This method clicks on create new option
     * 
     * @param className contains class name
     */
    private void clkCreateNew(String className) {
        CSUtility.switchToDefaultFrame(browserDriver);
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupCreateNew(), browserDriver);
        userManagementPopup.handlePopup(waitForReload, className,
                browserDriver);
    }

    /**
     * This method verifies the created class under Classes folder
     * 
     * @param className contains the name of the class
     */
    private void verifyCreatedClass(String className) {
        try {
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkClasses();
            WebElement createdClass = browserDriver
                    .findElement(By.linkText(className));
            Assert.assertEquals(className, createdClass.getText());
            CSLogger.info("Class has successfully created ");
        } catch (Exception e) {
            CSLogger.debug("Could not verify the created class" + e);
            Assert.fail("Verification failed");
        }
    }

    /**
     * This data provider returns data sheet which contains class names to be
     * created
     * 
     * @return createClassSheet
     */
    @DataProvider(name = "createClass")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                createClassSheet);
    }

    /**
     * This method initializes resources to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
    }
}
