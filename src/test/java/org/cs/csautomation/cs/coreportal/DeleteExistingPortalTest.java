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
 * This class Delete Existing portal
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteExistingPortalTest extends AbstractTest {

    private CSPortalHeader   csPortalHeader;
    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private IProductPopup    productPopUp;
    private LoginPage        loginPage;
    private ChoosePortalPage choosePortalPage;
    private String           deletePortalTest = "DeletePortal";

    /**
     * This test Delete the existing portal and verify
     * 
     * @param portalName String object containing portal name
     * @param portalUserName String object containing portal UserName
     * @param portalPassword String object containing portal password
     */
    @Test(dataProvider = "DeletePortalData")
    public void testDeleteExistingPortal(String portalName,
            String portalUsername, String portalPassword) {
        try {
            clkOnPortalSetting();
            clkOnDeleteButton();
            clkOnPopupWindow(false);
            closeWindowPortalSetting();
            CSUtility.tempMethodForThreadSleep(1000);
            clkOnPortalSetting();
            clkOnDeleteButton();
            clkOnPopupWindow(true);
            CSUtility.tempMethodForThreadSleep(1000);
            verifyDeletePortal(portalName, portalUsername, portalPassword);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteExistingPortal", e);
            Assert.fail("Automation error : testDeleteExistingPortal", e);
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
     * This method click on delete button
     * 
     */
    private void clkOnDeleteButton() {
        traverseToCsPortalWindowFrame();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getBtnDelete());
        choosePortalPage.getBtnDelete().click();
        CSLogger.info("Clicked on Delete Button");
    }

    /**
     * This method click on OK or cancel button of popup window
     * 
     * @param pressKey Boolean
     */
    private void clkOnPopupWindow(Boolean pressKey) {
        if (pressKey) {
            traverseToCsPortalPopupWindowFrame();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    choosePortalPage.getBtnOK());
            choosePortalPage.getBtnOK().click();
            CSLogger.info("Clicked on OK Button");
        } else {
            traverseToCsPortalPopupWindowFrame();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    choosePortalPage.getBtnCancel());
            choosePortalPage.getBtnCancel().click();
            CSLogger.info("Clicked on Cancel Button");
        }
    }

    /**
     * This method close the window portal setting
     * 
     */
    private void closeWindowPortalSetting() {
        traverseToCsPortalWindowFrame();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getBtnClose());
        choosePortalPage.getBtnClose().click();
        CSLogger.info("Clicked on Closed Button");
    }

    /**
     * This method traverse to portal Popup window frame
     * 
     */
    private void traverseToCsPortalPopupWindowFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(browserDriver.findElement(
                        By.xpath("/html[1]/body[1]/div[9]/div[2]/iframe[1]"))));
        CSLogger.info("Traverse to the Popup Window Frame");
    }

    /**
     * This method portal setting window frame
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
     * This method verifies the delete portal test
     * 
     * @param portalName String object containing portal name
     * @param portalUsername String object containing portal UserName
     * @param portalPassword String object containing portal password
     */
    private void verifyDeletePortal(String portalName, String portalUsername,
            String portalPassword) {
        try {
            chooseLanguage("English");
            loginPage.enterUsername(portalUsername);
            loginPage.enterPassword(portalPassword);
            loginPage.clkLoginButton();
            int portals = browserDriver
                    .findElements(By.xpath(
                            "//select[@id='CSPortalLoginSelect']//option"))
                    .size();
            ArrayList<String> allOptions = new ArrayList<>();
            for (int row = 1; row < portals; row++) {
                allOptions.add(browserDriver
                        .findElements(By.xpath(
                                "//select[@id='CSPortalLoginSelect']//option"))
                        .get(row).getText());
            }
            if (allOptions.contains(portalName)) {
                Assert.fail(portalName + " is present in the dropdown");
            } else {
                CSLogger.info("Delete Existing is portal passed");
            }
        } catch (Exception e) {
            CSLogger.error("Verification fail of delete Portal", e);
        }
    }

    /**
     * This Method click on Portal Link Options
     * 
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * 
     * @param language String login language
     */
    private void chooseLanguage(String language) {
        Select loginLanguage = new Select(loginPage.getDrpdwnLanguage());
        loginLanguage.selectByVisibleText(language);
        CSLogger.info(language + " language has been chosen");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains portalName,portalUserName,poralPassword
     * 
     * @return deletePortalTest
     */
    @DataProvider(name = "DeletePortalData")
    public Object[] deletePortal() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("corePortalTestCases"),
                deletePortalTest);
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
