/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SettingsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This method performs use case of setting and verifying set mail
 * 
 * @author CSAutomation Team
 *
 */
public class VerifySaveSettingsOptionTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private TraversingForSettingsModule traversingForSettingsModule;
    private String                      saveSettingsOptionSheet = "SettingsOptionSheet";
    private SettingsPage                settingsPage;
    private CSPortalWidget              csPortalWidgetInstance;

    /**
     * This method performs use case of setting and verifying set mail
     * 
     * @param emailToCheckSaveFunctionality emailToCheckSaveFunctionality
     *            contains mail as String
     */
    @Test(priority = 1, dataProvider = "saveSettingsOption")
    public void testSaveSettingsOption(String emailToCheckSaveFunctionality) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            performUseCase(emailToCheckSaveFunctionality);
        } catch (Exception e) {
            CSLogger.error("Test case for save settings option failed", e);
            Assert.fail("Test case for save settings option failed", e);
        }
    }

    /**
     * This method performs use case of setting and verifying set mail
     * 
     * @param emailToCheckSaveFunctionality contains mail as String
     */
    private void performUseCase(String emailToCheckSaveFunctionality) {
        clkSettingsInLeftPane();
        clkCore();
        CSUtility.tempMethodForThreadSleep(1000);
        clkBtnPortal(settingsPage.getBtnPortal());
        chkFunctionalityOfSaveButton(emailToCheckSaveFunctionality);
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
     * This method checks functionality of save button
     * 
     * @param emailToCheckSaveFunctionality contains mail in String format
     */
    private void
            chkFunctionalityOfSaveButton(String emailToCheckSaveFunctionality) {
        setEmailToTxtField(emailToCheckSaveFunctionality);
        verifySetEmail(emailToCheckSaveFunctionality);
    }

    /**
     * This method sets email to txt field
     * 
     * @param emailToCheckSaveFunctionality contains mail in String format
     */
    private void setEmailToTxtField(String emailToCheckSaveFunctionality) {
        waitForReload.until(ExpectedConditions.visibilityOf(
                settingsPage.getTxtRegistrationNotificationEmail()));
        settingsPage.getTxtRegistrationNotificationEmail()
                .sendKeys(Keys.DELETE);
        settingsPage.getTxtRegistrationNotificationEmail().clear();
        settingsPage.getTxtRegistrationNotificationEmail()
                .sendKeys(emailToCheckSaveFunctionality);
        clkSave();
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
     * This method verifies if email has set or not
     * 
     * @param emailToCheckSaveFunctionality contains email address as String
     */
    private void verifySetEmail(String emailToCheckSaveFunctionality) {
        try {
            traversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(settingsPage.getTxtRegistrationEmail()));
            String value = settingsPage.getTxtRegistrationEmail()
                    .getAttribute("value");
            Assert.assertEquals(emailToCheckSaveFunctionality, value);
            CSLogger.info("Verification of mail is successful");
        } catch (Exception e) {
            CSLogger.error("Could not verify if mail has set.", e);
            Assert.fail("Could not verify if mail has set.", e);
        }
    }

    /**
     * This data provider returns the sheet data which contains registration
     * mail
     * 
     * @return saveSettingsOptionSheet
     * 
     */
    @DataProvider(name = "saveSettingsOption")
    public Object[] saveSettingsOption() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                saveSettingsOptionSheet);
    }

    /**
     * This method initializes resources to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        settingsPage = new SettingsPage(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
    }
}
