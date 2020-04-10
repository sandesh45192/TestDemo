/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.coreportal;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class Delete the widget.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteExistingWidgetTest extends AbstractTest {

    private CSPortalHeader   csPortalHeader;
    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private LoginPage        loginPage;
    private Actions          actions;
    private ChoosePortalPage choosePortalPage;
    private String           deleteWidget = "DeleteWidget";

    /**
     * This test method delete the widget
     * 
     * @param portalUserName String object containing portal UserName
     * @param portalPassword String object containing portal password
     * @param portalName String object containing portal name
     */
    @Test(dataProvider = "deleteWidgetData")
    public void testDeleteWidget(String portalUserName, String portalPassword,
            String portalName) {
        try {
            loginAsCreatedUser(portalUserName, portalPassword);
            createNewPortal(portalName);
            unlockportal();
            deleteWidget(false);
            deleteWidget(true);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteWidget", e);
            Assert.fail("Automation error : testDeleteWidget", e);
        }
    }

    /**
     * This test method delete widget
     * 
     * @param pressKey Boolean to click cancel or oK
     */
    public void deleteWidget(Boolean pressKey) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        actions.moveToElement(browserDriver.findElement(
                By.xpath("//div[contains(@id,'CSPortalWidgetTitlebar')]")))
                .build().perform();
        browserDriver
                .findElement(
                        By.xpath("//div[@class='CSPortalWidgetButtonClose']"))
                .click();
        if (pressKey) {
            traverseToCsPortalWindowFrame();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    choosePortalPage.getBtnOK());
            choosePortalPage.getBtnOK().click();
            CSLogger.info("Clicked on OK Button");
            verifyDeleteWidget(pressKey);
        } else {
            traverseToCsPortalWindowFrame();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    choosePortalPage.getBtnCancel());
            choosePortalPage.getBtnCancel().click();
            CSLogger.info("Clicked on Cancel Button");
            verifyDeleteWidget(pressKey);
        }
    }

    /**
     * This method traverse to portal window frame
     * 
     */
    private void traverseToCsPortalWindowFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowWidget()));
    }

    /**
     * This test method delete widget
     * 
     * @param pressKey Boolean to click cancel or oK
     */
    public void verifyDeleteWidget(Boolean pressKey) {
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        WebElement element = browserDriver.findElement(By.xpath(
                "(//div[contains(@id,'CSPortalTabContent')]//div[1]//div)[2]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        if (pressKey) {
            if (element.getAttribute("class")
                    .contains("CSPortalTabColFooter")) {
                CSLogger.info("Verification Delete widget Successful");
            } else {
                CSLogger.error("Verification Delete widget fail");
                Assert.fail("Verification Delete widget fail");
            }
        } else {
            if (element.getAttribute("class").contains("CSPortalWidget")) {
                CSLogger.info("Verification click on cancel Successful");
            } else {
                CSLogger.error("Verification click on cancelt fail");
                Assert.fail("Verification click on cancelt fail");
            }
        }
    }

    /**
     * This test method login as newly created user
     * 
     * @param portalUserName String object containing portal UserName
     * @param portalPassword String object containing portal password
     */
    private void loginAsCreatedUser(String portalUserName,
            String portalPassword) {
        chooseLanguage("English");
        loginPage.enterUsername(portalUserName);
        loginPage.enterPassword(portalPassword);
        CSLogger.info("Entered Username and password");
    }

    /**
     * This test method choose the login language
     * 
     * @param language String login language
     */
    private void chooseLanguage(String language) {
        Select loginLanguage = new Select(browserDriver
                .findElement(By.xpath("//select[@id='loginLanguage']")));
        loginLanguage.selectByVisibleText(language);
        CSLogger.info(language + " language has been chosen");
    }

    /**
     * This test method select created new portal
     * 
     * @param createNewPortalOption String portal name
     */
    private void createNewPortal(String createNewPortalOption) {
        loginPage.clkLoginButton();
        clkCreatedNewPortal(createNewPortalOption);
    }

    /**
     * This test method click no created new portal
     * 
     * @param createNewPortalOption String portal name
     */
    private void clkCreatedNewPortal(String createNewPortalOptionn) {
        Select newPortal = new Select(browserDriver
                .findElement(By.xpath("//select[@id='CSPortalLoginSelect']")));
        newPortal.selectByVisibleText(createNewPortalOptionn);
        CSLogger.info("Create New Portal option selected");
        loginPage.clkLoginButton();
    }

    /**
     * This method click on lock and Unlock portal
     * 
     */
    private void unlockportal() {
        clkPortalLinkOptions();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        WebElement lock = browserDriver.findElement(
                By.xpath("//td[contains(text(),'Lock & Unlock Portal')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(lock));
        lock.click();
    }

    /**
     * This method click on portal link
     * 
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains portalUserName,poralPassword,portalName
     * 
     * @return deleteWidget
     */
    @DataProvider(name = "deleteWidgetData")
    public Object[] deletewidgetPortal() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("corePortalTestCases"), deleteWidget);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        loginPage = new LoginPage(browserDriver);
        actions = new Actions(browserDriver);
        choosePortalPage = new ChoosePortalPage(browserDriver);
    }
}
