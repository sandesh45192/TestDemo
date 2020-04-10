/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.coreportal;

import java.util.List;
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class Edit the Portal configuration
 * 
 * @author CSAutomation Team
 *
 */
public class EditPortalConfigurationTest extends AbstractTest {

    private CSPortalHeader   csPortalHeader;
    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private IProductPopup    productPopUp;
    private ChoosePortalPage choosePortalPage;
    private String           editPoralConfigurationTest = "EditPortalConfiguration";

    /**
     * This test method edit the portal configuration
     * 
     * @param skin String skin of portal header
     */
    @Test(priority = 1, dataProvider = "EditPortalData")
    public void testEditPortalConfig(String skin) {
        try {
            clkOnPortalSetting();
            clkOnLayout();
            setPortalConfiguration(skin);
            CSUtility.tempMethodForThreadSleep(1000);
            verifyPortalConfiguration(skin);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testEditPortalConfig", e);
            Assert.fail("Automation error : testEditPortalConfig", e);
        }
    }

    /**
     * This test method Perform Lock portal configuration
     * 
     */
    @Test(priority = 2)
    public void lockPortalConfiguration() {
        try {
            clkPortalLinkOptions();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(
                    ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions.visibilityOf(
                    productPopUp.getCsGuiPopupLockAndUnlockPortal()));
            productPopUp.getCsGuiPopupLockAndUnlockPortal().click();
            CSLogger.info("Clicked on Lock and Unlock Portal");
            verifyLockAndUnlock(true);
        } catch (Exception e) {
            CSLogger.debug("Automation error : lockPortalConfiguration", e);
            Assert.fail("Automation error : lockPortalConfiguration", e);
        }
    }

    /**
     * This test method Perform Unlock portal configuration
     * 
     */
    @Test(priority = 3)
    public void unlockPortalConfiguration() {
        try {
            clkPortalLinkOptions();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(
                    ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions.visibilityOf(
                    productPopUp.getCsGuiPopupLockAndUnlockPortal()));
            productPopUp.getCsGuiPopupLockAndUnlockPortal().click();
            CSLogger.info("Clicked on Portal Setting");
            verifyLockAndUnlock(false);
        } catch (Exception e) {
            CSLogger.debug("Automation error : unlockPortalConfiguration", e);
            Assert.fail("Automation error : unlockPortalConfiguration", e);
        }
    }

    /**
     * This method click on portal Setting
     * 
     */
    public void clkOnPortalSetting() {
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
     * This method set the portal configuration
     * 
     * @param skin String skin of portal header
     */
    private void setPortalConfiguration(String skin) {
        traverseToCsPortalWindowFrame();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getselectSkin());
        Select select = new Select(choosePortalPage.getselectSkin());
        select.selectByVisibleText(skin);
        WebElement checkBox = browserDriver.findElement(By.xpath(
                "//input[@id='CSPortalGuiPortalControllerHidePortalHelpLink']"));
        if (checkBox.getAttribute("value").equals("0")) {
            browserDriver.findElement(By.xpath(
                    "//input[@id='CSPortalGuiPortalControllerHidePortalHelpLink_Input']"))
                    .click();
        }
        choosePortalPage.clkBtnSave();
        CSLogger.info("Clicked on save button");
        CSLogger.info("Edit Portal Configuration");
    }

    /**
     * This method click on layout option
     * 
     */
    private void clkOnLayout() {
        traverseToCsPortalWindowFrame();
        browserDriver
                .findElement(
                        By.xpath("(//div[@class='CSPortalGuiPanelIcon'])[2]"))
                .click();
        CSLogger.info("Clicked on Layout Option");
    }

    /**
     * This method maximize widget window
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
     * This method verifies edit portal configuration
     * 
     * @param skin String contains skin of portal header
     */
    private void verifyPortalConfiguration(String skin) {
        String chkString = null;
        int flag = 0;
        String portal = "/skins/" + skin + "/skin.css";
        clkPortalLinkOptions();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        List<WebElement> portalHelp = browserDriver
                .findElements(By.xpath("//td[contains(text(),'Help')]"));
        if (portalHelp.isEmpty()) {
            CSLogger.info("Portal Help is not Present");
        } else {
            CSLogger.error("Portal Help is Present");
            Assert.fail("Portal Help is Present");
        }
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        browserDriver.findElement(By.xpath("//div[@id='CSPortalPortalLogo']"))
                .click();
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(
                        "//div[contains(@id,'CSPortalWidgetContent')]//iframe[1]")));
        List<WebElement> allLinks = browserDriver
                .findElements(By.tagName("link"));
        for (WebElement link : allLinks) {
            chkString = link.getAttribute("href");
            if (chkString.contains(portal)) {
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            CSLogger.info("Verification of skin " + skin + " is passed");
        } else {
            CSLogger.error("Verification of skin " + skin + " is fail");
            Assert.fail("Verification of skin " + skin + " is fail");
        }
    }

    /**
     * This method verifies the Lock and Unlock test
     * 
     * @param value Boolean
     */
    private void verifyLockAndUnlock(Boolean value) {
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.tempMethodForThreadSleep(5000);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        WebElement btnPlus = browserDriver
                .findElement(By.xpath("/html[1]/body[1]"));
        if (value) {
            if (btnPlus.getAttribute("class")
                    .equals("CSPortal Win gc74 gc  SingleTab viewmode")) {
                CSLogger.info("Test Lock is Successful");
            } else {
                CSLogger.error("Test Lock is not Successful");
                Assert.fail("Test Lock is not Successful");
            }
        } else {
            if (btnPlus.getAttribute("class")
                    .equals("CSPortal Win gc74 gc  SingleTab editmode")) {
                CSLogger.info("Test Unlock is Successful");
            } else {
                CSLogger.error("Test Unlock is not Successful");
                Assert.fail("Test Unlock is not Successful");
            }
        }
    }

    /**
     * This method Click on Portal Link Option
     * 
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains skin of portal header
     * 
     * @return editPoralConfigurationDataSheet
     */
    @DataProvider(name = "EditPortalData")
    public Object[] EditTab() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("corePortalTestCases"),
                editPoralConfigurationTest);
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
        choosePortalPage = new ChoosePortalPage(browserDriver);
    }
}
