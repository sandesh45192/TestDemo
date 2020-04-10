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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class Edit the widget configuration
 * 
 * @author CSAutomation Team
 *
 */
public class EditWidgetConfigurationTest extends AbstractTest {

    private WebDriverWait    waitForReload;
    private FrameLocators    locator;
    private ChoosePortalPage choosePortalPage;
    private Actions          actions;
    private String           editWidgetConfigurationTest = "EditWidgetConfiguration";

    /**
     * This test method Edit widget Configuration
     * 
     * @param widgetName String Object containing widget name
     * @param widgetHeight String Object containing widget height
     * @param widgetColor String object containing widget color
     */
    @Test(dataProvider = "EditWidgetData")
    public void testEditwidgetCongifuration(String widgetName,
            String widgetHeight, String widgetColor) {
        try {
            clkWidgetOptions();
            clkIconLayoutOptions();
            setTitleOfWidget(widgetName);
            setWidgetHeight(widgetHeight);
            setWidgetColor(widgetColor);
            traverseToCsPortalWindowFrame();
            choosePortalPage.clkBtnApply();
            CSLogger.info("Clicked on Apply button");
            choosePortalPage.clkBtnSave();
            CSLogger.info("Clicked on save button");
            verfyEditWidget(widgetName, widgetHeight, widgetColor);
        } catch (Exception e) {
            CSLogger.info("Automation error", e);
            Assert.fail("Automation error", e);
        }
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
     * This method mouse hover to the element
     * 
     * @param element WebElement
     */
    private void moveToWebElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    /**
     * This method click on layout option
     * 
     */
    private void clkIconLayoutOptions() {
        traverseToCsPortalWindowFrame();
        choosePortalPage.clkIconLayoutOptions();
        CSLogger.info("Clicked on layout options ");
    }

    /**
     * This test method set widget title
     * 
     * @param title String Object containing widget name
     */
    public void setTitleOfWidget(String title) {
        traverseToCsPortalWindowFrame();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getTextWidgetTitle());
        choosePortalPage.getTextWidgetTitle().clear();
        choosePortalPage.getTextWidgetTitle().sendKeys(title);
        CSLogger.info("Set Title to widget");
    }

    /**
     * This test method set widget height
     * 
     * @param widgetHeight String Object containing widget height
     */
    public void setWidgetHeight(String widgetHeight) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getSelectWidgetHeight());
        Select height = new Select(choosePortalPage.getSelectWidgetHeight());
        height.selectByVisibleText(widgetHeight);
        CSLogger.info("Set height of widget");
    }

    /**
     * This test method set widget color
     * 
     * @param widgetColor String object containing widget color
     */
    public void setWidgetColor(String widgetColor) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getWidgetColor());
        choosePortalPage.getWidgetColor().click();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        WebElement elementColor = browserDriver.findElement(
                By.xpath("//a[contains(@ondblclick,'" + widgetColor + "')]"));
        elementColor.click();
        choosePortalPage.getBtnOKColorConfigurator().click();
        CSLogger.info("Set color of widget");
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
     * This test method verifies Edit widget Configuration
     * 
     * @param widgetTitle String Object containing widget name
     * @param height String Object containing widget height
     * @param color String object containing widget color
     */
    public void verfyEditWidget(String widgetTitle, String height,
            String color) {
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.tempMethodForThreadSleep(5000);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(browserDriver.findElement(By.xpath(
                        "/html[1]/body[1]/div[4]/table[1]/tbody[1]/tr[1]/td[2]/div[1]/div[2]/div[1]"))));
        WebElement selection = browserDriver.findElement(By.xpath(
                "/html[1]/body[1]/div[4]/table[1]/tbody[1]/tr[1]/td[2]/div[1]/div[2]/div[1]"));
        if (height.contains(selection.getAttribute("minwidth"))) {
            CSLogger.info("height verified successful");
        } else {
            Assert.fail("height does not match");
        }
        WebElement verifycolor = browserDriver.findElement(
                By.xpath("//div[contains(@id,'CSPortalWidgetTitlebar')]"));
        WebElement colorid = (WebElement) ((JavascriptExecutor) browserDriver)
                .executeScript("return arguments[0];", verifycolor);
        if (color.equalsIgnoreCase(colorid.getAttribute("oldclass"))) {
            CSLogger.info("color verified successful");
        } else {
            Assert.fail("color does not match");
        }
        WebElement title = browserDriver.findElement(
                By.xpath("//span[contains(@id,'CSPortalWidgetTitle')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, title);
        if (title.getText().equals(widgetTitle)) {
            CSLogger.info("Title verified successful");
        } else {
            Assert.fail("Title does not match");
        }
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Widget name, widget height, widget color
     * 
     * @return createUserGroupAndUsersheet
     */
    @DataProvider(name = "EditWidgetData")
    public Object[] EditWidget() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("corePortalTestCases"),
                editWidgetConfigurationTest);
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
