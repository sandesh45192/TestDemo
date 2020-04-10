/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.coreportal;

import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class Edit the Tab configuration
 * 
 * @author CSAutomation Team
 *
 */
public class EditTabConfigurationTest extends AbstractTest {

    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private ChoosePortalPage choosePortalPage;
    private String           editTabConfigurationTest = "EditTabConfiguration";

    /**
     * This test method Edit Tab Configuration
     * 
     */
    @Test(dataProvider = "EditTabData")
    public void testEditTabConfiguration(String tabNewName,String tabColumn) {
        try {
            clkOnTab();
            editTabconfig(tabNewName,tabColumn);
            verifyEditTabConfig(tabNewName,tabColumn);
        } catch (Exception e) {
            CSLogger.info("Automation error", e);
            Assert.fail("Automation error", e);
        }
    }

    /**
     * This method Tab icon of portal
     * 
     */
    public void clkOnTab() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getPortalTab());
        choosePortalPage.getPortalTab().click();
        CSLogger.info("Clicked on portal tab");
    }

    /**
     * This method edit tab configuration
     * 
     */
    public void editTabconfig(String tabNewName,String tabColumn) {
        traverseToCsPortalWindowFrame();
        WebElement tabLayout = browserDriver.findElement(
                By.id("title_CSPortalTabControllerPane::Tab Layout"));
        waitForReload.until(ExpectedConditions.visibilityOf(tabLayout));
        WebElement parent = (WebElement) ((JavascriptExecutor) browserDriver)
                .executeScript("return arguments[0].parentNode;", tabLayout);
        if (parent.getAttribute("class").contains("closed")) {
            browserDriver
                    .findElement(
                            By.xpath("//div[contains(text(),'Tab Layout')]"))
                    .click();
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getTabTitle());
        choosePortalPage.getTabTitle().clear();
        choosePortalPage.getTabTitle().sendKeys(tabNewName);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getselectTabColumn());
        Select column = new Select(choosePortalPage.getselectTabColumn());
        column.selectByVisibleText(tabColumn);
        choosePortalPage.clkBtnApply();
        CSLogger.info("Clicked on Apply button");
        choosePortalPage.clkBtnSave();
        CSLogger.info("Clicked on save button");
    }

    /**
     * This method verifies the edit tab is successful
     * 
     */
    public void verifyEditTabConfig(String tabNewName,String tabColumn) {
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.tempMethodForThreadSleep(5000);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmTopFrame()));
        WebElement tabName = browserDriver.findElement(
                By.xpath("//span[contains(@id,'CSPortalTabButtonTitle')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(tabName));
        if (tabNewName.equalsIgnoreCase(tabName.getText())) {
            CSLogger.info("Tab name verified");
        } else {
            Assert.fail("Tab name not verified");
        }
        int list = browserDriver
                .findElements(
                        By.xpath("//table[contains(@class,'CSPortalMain')]"
                                + "/tbody/tr/td[2]/div/div/div/div/span"))
                .size();
        int columnCount = Integer.parseInt(tabColumn);
        if (list == columnCount) {
            CSLogger.info("Number of column verified");
        } else {
            Assert.fail("Number of column not verified");
        }
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
     * This data provider returns the sheet data to run the test case which
     * contains Tab new name and column count 
     * 
     * @return createUserGroupAndUsersheet
     */
    @DataProvider(name = "EditTabData")
    public Object[] EditTab() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("corePortalTestCases"),
                editTabConfigurationTest);
    }
    
    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 30);
        locator = FrameLocators.getIframeLocators(browserDriver);
        choosePortalPage = new ChoosePortalPage(browserDriver);
    }
}
