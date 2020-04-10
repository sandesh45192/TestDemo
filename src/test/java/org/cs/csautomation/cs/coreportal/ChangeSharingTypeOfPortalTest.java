/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.coreportal;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class change the sharing type of portal and verifies it.
 * 
 * @author CSAutomation Team
 *
 */
public class ChangeSharingTypeOfPortalTest extends AbstractTest {

    private CSPortalHeader   csPortalHeader;
    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private IProductPopup    productPopUp;
    private LoginPage        loginPage;
    private ChoosePortalPage choosePortalPage;
    private String           changeSharingTypePortalTest = "ChangeSharingTypePortal";

    /**
     * This test method Change the sharing of portal and verify
     * 
     * @param typeSharingNoOther String Object containing sharing type with no
     *            other
     * @param typeSharingOther String Object containing sharing type with others
     * @param portalUserName String object containing portal UserName
     * @param portalPassword String object containing portal password
     * @param portalName String object containing portal name
     * @param adminUsername String object containing admin username
     * @param adminPassword String object containing admin password
     */
    @Test(dataProvider = "ChangeSharingPortalData")
    public void testChangeSharingPortal(String typeSharingNoOther,
            String typeSharingOther, String portalUserName,
            String portalPassword, String portalName, String adminUsername,
            String adminPassword) {
        try {
            clkOnPortalSetting();
            clkOnSharing();
            setSharingConfig(typeSharingNoOther);
            CSUtility.tempMethodForThreadSleep(1000);
            logout();
            verifyNoPortalContent(portalName, adminUsername, adminPassword,
                    false);
            loginAsCreatedUserNewPortal(portalUserName, portalPassword,
                    portalName);
            clkOnPortalSetting();
            clkOnSharing();
            setSharingConfig(typeSharingOther);
            CSUtility.tempMethodForThreadSleep(1000);
            logout();
            verifyNoPortalContent(portalName, adminUsername, adminPassword,
                    true);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testChangeSharingPortal", e);
            Assert.fail("Automation error : testChangeSharingPortal", e);
        }
    }

    /**
     * This method click on portal Setting
     * 
     */
    private void clkOnPortalSetting() {
        clkPortalLinkOptions();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getCsGuiPopupPortalSettings()));
        productPopUp.getCsGuiPopupPortalSettings().click();
        CSLogger.info("Clicked on Portal Setting");
    }

    /**
     * This method click on layout option
     * 
     */
    private void clkOnSharing() {
        traverseToCsPortalWindowFrame();
        browserDriver
                .findElement(
                        By.xpath("(//div[@class='CSPortalGuiPanelIcon'])[3]"))
                .click();
        CSLogger.info("Clicked on Sharing Option");
    }

    /**
     * This method maximize window frame
     * 
     */
    private void traverseToCsPortalWindowFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
    }

    /**
     * This method set the sharing configuration of portal
     * 
     * @param typeSharingNoOther String Object containing sharing type with no
     *            other
     */
    private void setSharingConfig(String typeSharingNoOther) {
        traverseToCsPortalWindowFrame();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getSelectTypeSharing());
        Select select = new Select(choosePortalPage.getSelectTypeSharing());
        select.selectByVisibleText(typeSharingNoOther);
        choosePortalPage.clkBtnSave();
        CSLogger.info("Clicked on save button");
        CSLogger.info("Edit Portal Configuration");
    }

    /**
     * This method verifies sharing configuration of portal with no others
     * 
     * @param portalName String object containing portal name
     * @param adminUsername String object containing admin username
     * @param adminPassword String object containing admin password
     * @param value Boolean
     */
    public void verifyNoPortalContent(String portalName, String adminUsername,
            String adminPassword, Boolean value) {
        chooseLanguage("English");
        loginPage.enterUsername(adminUsername);
        loginPage.enterPassword(adminPassword);
        loginPage.clkLoginButton();
        int portals = browserDriver
                .findElements(
                        By.xpath("//select[@id='CSPortalLoginSelect']//option"))
                .size();
        ArrayList<String> allOptions = new ArrayList<>();
        for (int row = 1; row < portals; row++) {
            allOptions.add(browserDriver
                    .findElements(By.xpath(
                            "//select[@id='CSPortalLoginSelect']//option"))
                    .get(row).getText());
        }
        if (value) {
            if (allOptions.contains(portalName)) {
                CSLogger.info("Sharing for other portal passed");
            } else {
                Assert.fail(portalName + " is not present in the dropdown");
            }
        } else {
            if (!allOptions.contains(portalName)) {
                CSLogger.info("No Sharing for other portal passed");
            } else {
                CSLogger.error(portalName + " is present in the dropdown");
                Assert.fail(portalName + " is present in the dropdown");
            }
        }
    }

    /**
     * This method login as newly created user portal
     * 
     * @param portalUserName String object containing portal UserName
     * @param portalPassword String object containing portal password
     * @param portalName String object containing portal name
     */
    public void loginAsCreatedUserNewPortal(String portalUserName,
            String portalPassword, String portalName) {
        chooseLanguage("English");
        loginPage.enterUsername(portalUserName);
        loginPage.enterPassword(portalPassword);
        loginPage.getDrodwnPortalSelect().click();
        Select portal = new Select(loginPage.getDrodwnPortalSelect());
        portal.selectByVisibleText(portalName);
        CSLogger.info(portalName + " is Selected");
        loginPage.clkLoginButton();
    }

    /**
     * This method logout from current user
     * 
     */
    private void logout() {
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
     * This method click on portal link option
     * 
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * This method choose the login language
     * 
     * @param language String contain login language
     */
    private void chooseLanguage(String language) {
        Select loginLanguage = new Select(loginPage.getDrpdwnLanguage());
        loginLanguage.selectByVisibleText(language);
        CSLogger.info(language + " language has been chosen");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains
     * sharingTypeNoOther,sharingTypeOther,portalUserName,poralPassword,
     * portalName,adminUserName,adminPassword,
     * 
     * @return changeSharingTypePortalTest
     */
    @DataProvider(name = "ChangeSharingPortalData")
    public Object[] changeSharingPortal() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("corePortalTestCases"),
                changeSharingTypePortalTest);
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
        choosePortalPage = new ChoosePortalPage(browserDriver);
    }
}
