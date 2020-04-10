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
import org.testng.annotations.Test;

/**
 * This class creates New Widget and assign pim studio to widget
 * 
 * @author CSAutomation Team
 *
 */
public class CreateNewWidgetAndConfigTest extends AbstractTest {

    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private ChoosePortalPage choosePortalPage;
    private Actions          actions;

    /**
     * This Test method Create new widget tab and verifies
     * 
     */
    @Test(priority = 1)
    public void testCreateNewWidget() {
        try {
            clkPlusToAddWidget();
            clkBySuite();
            clkCore();
            clkCorePortalStudio();
            clkInsert();
            verifyConfigurationMessageForInsertingWidget();
        } catch (Exception e) {
            CSLogger.info("Automation error", e);
            Assert.fail("Automation error", e);
        }
    }

    /**
     * This Test method assign the Pim Studio to widget and verifies
     * 
     */
    @Test(priority = 2)
    public void testConfigurationOfNewWidget() {
        try {
            clkWidgetOptions();
            clkSecuredOptions();
            addPimStudioFromWidgetOptions();
            verifyAddPimStudio();
        } catch (Exception e) {
            CSLogger.info("Automation error", e);
            Assert.fail("Automation error", e);
        }
    }

    /**
     * This method click plus icon to add widget
     * 
     */
    private void clkPlusToAddWidget() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getBtnPlusToAddWidget()));
        choosePortalPage.getBtnPlusToAddWidget().click();
        CSLogger.info(
                "Clicked on Plus button to add Widget to newly created tab in new portal");
    }

    /**
     * This method click on shuit menu
     * 
     */
    private void clkBySuite() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        choosePortalPage.clkBysuiteMenu();
        CSLogger.info("Clicked on By Suite menu");
    }

    /**
     * This method core on shuit menu
     * 
     */
    private void clkCore() {
        choosePortalPage.clkCoreSubMenu();
        CSLogger.info("Clicked on Core Submenu");
    }

    /**
     * This method click on core portal studio
     * 
     */
    private void clkCorePortalStudio() {
        choosePortalPage.clkCorePortalStudio();
        CSLogger.info("Clicked on Core Portal Studio");
    }

    /**
     * This method click on insert button
     * 
     */
    private void clkInsert() {
        choosePortalPage.clkBtnInsert(waitForReload);
        CSLogger.info("Clicked on Insert to create new portal");
    }

    /**
     * This method verifies the Configuration message for insert widget
     * 
     */
    private void verifyConfigurationMessageForInsertingWidget() {
        traverseToWidgetFrame();
        WebElement configurationMessage = waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getConfigurationMessage()));
        String message = configurationMessage.getText();
        if (message.equals("Configuration Required")) {
            CSLogger.info(
                    "Configuration Required message successfully verified");
        } else {
            CSLogger.info("Verification failed.");
            Assert.fail("Verification failed for configuration message.");
        }
    }

    /**
     * This method traverse to widget frame
     * 
     */
    private void traverseToWidgetFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmPortalWidgetContentFrame()));
    }

    /**
     * This method mouse hover to the element
     * 
     * @param element WebElement
     */
    private void moveToWebElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    /**
     * This method click on widget option
     * 
     */
    private void clkWidgetOptions() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getPortalWidgetDragArea()));
        moveToWebElement(choosePortalPage.getPortalWidgetDragArea());
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getBtnPortalWidget()));
        moveToWebElement(choosePortalPage.getBtnPortalWidget());
        choosePortalPage.getBtnPortalWidget().click();
    }

    /**
     * This method click on secured option
     * 
     */
    private void clkSecuredOptions() {
        traverseToCsPortalWindowFrame();
        choosePortalPage.clkIconSecuredOptions();
        CSLogger.info("Clicked on Secured options ");
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
     * This method add pim studio from widget option
     * 
     */
    private void addPimStudioFromWidgetOptions() {
        traverseToCsPortalWindowFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getPimStudioOption()));
        actions.doubleClick(choosePortalPage.getPimStudioOption()).build()
                .perform();
        CSLogger.info("Double clicked on PIM studio");
        choosePortalPage.clkBtnApply();
        CSLogger.info("Clicked on Apply button");
        choosePortalPage.clkBtnSave();
        CSLogger.info("Clicked on save button");
        CSLogger.info("PIM studio loaded ");
    }

    /**
     * This method verifies the add pim studio to widget test
     * 
     */
    public void verifyAddPimStudio() {
        navigateToCreatedProduct();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getBtnPimProductsNode());
        String text = choosePortalPage.getBtnPimProductsNode().getText();
        if (text.equals("Product")) {
            CSLogger.info("Verification of Pim studio Successful");
        } else {
            CSLogger.error("Verification of Pim studio fail");
            Assert.fail("Verification of Pim studio fail");
        }
    }

    /**
     * This method traverse to the pimstudio node
     * 
     */
    private void navigateToCreatedProduct() {
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.tempMethodForThreadSleep(5000);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@frameborder='no']")));
        waitForReload.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(
                        "(//table[@class='CSPortalMain']/tbody/tr/td[2]/div/div/div/div/div/iframe)[1]")));
        waitForReload.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(
                        "//iframe[@id='StudioWidgetPane_4947d77df0891edef6b84be5fc695d2a_Content']")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@name='frmTree']")));
        waitForReload.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(
                        "//iframe[@id='frm_4947d77df0891edef6b84be5fc695d2a']")));
        CSLogger.info("Navigated to pim studio");
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
