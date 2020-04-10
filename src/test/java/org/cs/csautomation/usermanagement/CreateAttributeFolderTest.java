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
 * This class creates attribute folder under attributes node
 * 
 * @author CSAutomation Team
 *
 */
public class CreateAttributeFolderTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private UserManagementPage          userManagementPage;
    private String                      createAttributeFolderSheet = "CreateAttributeFolderSheet";
    private IUserManagementPopup        userManagementPopup;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSPortalWidget              csPortalWidgetInstance;
    private FrameLocators               iframeLocatorInstance;

    /**
     * This test method creates Attribute folder under attributes node
     * 
     * @param attributeFolderName contains name of attribute folder
     */
    @Test(priority = 1, dataProvider = "attributeFolderCreation")
    public void testCreateAttributeFolder(String attributeFolderName) {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        createAttributeFolder(attributeFolderName, false);
        createAttributeFolder(attributeFolderName, true);
        verifyCreatedAttributeFolder(attributeFolderName);
    }

    /**
     * This method creates new attribute folder
     * 
     * @param attributeFolderName contains the attribute folder name to be
     *            created
     */
    private void createAttributeFolder(String attributeFolderName,
            boolean status) {
        traversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        rightClkAttributesNode();
        clkNewFolderOption(attributeFolderName);
        if (status == false) {
            clkCancel();
        } else if (status == true) {
            clkOk();
            CSLogger.info("Attribute folder successfully created");
        }
    }

    /**
     * This method right clicks on Attributes Node
     */
    private void rightClkAttributesNode() {
        CSUtility.rightClickTreeNode(waitForReload,
                userManagementPage.getUserManagementAttributes(),
                browserDriver);
    }

    /**
     * This method clicks on New folder option after right clicking attributes
     * node
     */
    private void clkNewFolderOption(String attributeFolderName) {
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupNewFolder(), browserDriver);
        userManagementPopup.handlePopup(waitForReload, attributeFolderName,
                browserDriver);
    }

    /**
     * This method clicks on Cancel of pop up
     */
    private void clkCancel() {
        userManagementPopup.clkCancelOfPopup(waitForReload);
    }

    /**
     * This method clicks on OK of pop up
     */
    private void clkOk() {
        userManagementPopup.clkOkOfPopup(waitForReload);
    }

    /**
     * This method verifies if attribute folder has created under Attributes
     * folder
     */
    private void verifyCreatedAttributeFolder(String attributeFolderName) {
        try {
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkAttributes();
            WebElement attributeFolder = browserDriver
                    .findElement(By.linkText(attributeFolderName));
            Assert.assertEquals(attributeFolderName, attributeFolder.getText());
        } catch (Exception e) {
            CSLogger.info("Attribute Creation failed " + e);
        }
    }

    /**
     * This method clicks on Attributes node
     */
    private void clkAttributes() {
        waitForReload.until(ExpectedConditions.visibilityOf(
                userManagementPage.getUserManagementAttributes()));
        userManagementPage.getUserManagementAttributes().click();
        CSLogger.info("Clicked on Attributes node ");
    }

    /**
     * This data provider returns sheet data which contains attribute folder
     * name
     * 
     * @return createAttributeFolderSheet
     */
    @DataProvider(name = "attributeFolderCreation")
    public Object[] CreateUserGroupAndUser() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                createAttributeFolderSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */

    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorInstance = FrameLocators.getIframeLocators(browserDriver);
    }
}
