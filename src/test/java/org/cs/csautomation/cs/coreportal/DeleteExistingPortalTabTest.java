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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class Delete existing portal tab
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteExistingPortalTabTest extends AbstractTest {

    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private ChoosePortalPage choosePortalPage;
    private Actions          actions;
    private String           deleteTab = "DeleteTab";

    /**
     * This method delete portal Tab
     * 
     * @author CSAutomation Team
     *
     */
    @Test(dataProvider = "DeleteTabData")
    public void testDeleteExistingPortalTab(String tabName) {
        try {
            deleteTab(tabName, false);
            deleteTab(tabName, true);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteExistingPortalTab", e);
            Assert.fail("Automation error : testDeleteExistingPortalTab", e);
        }
    }

    /**
     * This test method delete tab
     * 
     * @param pressKey Boolean to click cancel or oK
     */
    public void deleteTab(String tabName, Boolean pressKey) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        actions.moveToElement(browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + tabName + "')]"))).build()
                .perform();
        browserDriver
                .findElement(By
                        .xpath("//div[contains(@id,'CSPortalTabCloseButton')]"))
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
     * This test method delete Tab
     * 
     * @param pressKey Boolean to click cancel or oK
     */
    private void verifyDeleteWidget(Boolean pressKey) {
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        WebElement element = browserDriver.findElement(
                By.xpath("(//div[@id='main']//div[1]//ul[1]//li)[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        if (pressKey) {
            if (element.getAttribute("class").contains("virtual")) {
                CSLogger.info("Verification Delete Tab Successful");
            } else {
                CSLogger.error("Verification Delete Tab fail");
                Assert.fail("Verification Delete Tab fail");
            }
        } else {
            if (element.getAttribute("class")
                    .contains("CSPortalTabButton edit selected")) {
                CSLogger.info("Verification click on cancel Successful");
            } else {
                CSLogger.error("Verification click on cancelt fail");
                Assert.fail("Verification click on cancelt fail");
            }
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
     * This data provider returns the sheet data to run the test case which
     * contains name of portal tab
     * 
     * @return deleteTab
     */
    @DataProvider(name = "DeleteTabData")
    public Object[] deleteTab() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("corePortalTestCases"), deleteTab);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        actions = new Actions(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        choosePortalPage = new ChoosePortalPage(browserDriver);
    }
}
