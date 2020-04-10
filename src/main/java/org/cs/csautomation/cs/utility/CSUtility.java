/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This is a Utility class and contains methods which are used to by other
 * classes in the framework.
 * 
 * @author CSAutomation Team
 *
 */
public class CSUtility {

    /**
     * This method helps to wait element for input milliseconds
     * 
     * @param millis
     */
    public static void tempMethodForThreadSleep(int millis) {
        try {
            CSLogger.info("Thread.sleep for " + millis + " milliseconds.");
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            CSLogger.error("Interrupted Exception : ", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * This method captures screenshots
     * 
     * @param testMethodName
     * @param driver
     * @param path
     */

    public static void captureScreenShots(String testMethodName,
            WebDriver driver, String path) {
        TakesScreenshot screenShot = ((TakesScreenshot) driver);
        try {
            File srcFile = screenShot.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(new CSUtility()
                    .buildScreenshotFilePath(path, testMethodName)));
        } catch (Exception e) {
            CSLogger.fatal("Screenshot Failed :", e);
        }
    }

    /**
     * returns current date
     * 
     * @return
     */
    public String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * This method captures screenshots using Robot class
     * 
     * @param testMethodName
     * @param path
     * @return
     */
    public static String captureScreenshotWithRobot(String testMethodName,
            String path) {
        try {
            Robot robotClassObject = new Robot();
            Rectangle screenSize = new Rectangle(
                    Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage tmp = robotClassObject
                    .createScreenCapture(screenSize);

            path = new CSUtility().buildScreenshotFilePath(path,
                    testMethodName);
            File screenshotFile = new File(path);
            FileUtils.touch(screenshotFile);
            // To copy temp image in to permanent file
            ImageIO.write(tmp, "png", screenshotFile);
        } catch (Exception e) {
            CSLogger.fatal("Screenshot using Robot Failed :", e);
        }

        return path;
    }

    /**
     * this method helps to build file path required for screenshot
     * 
     * @param path
     * @param testMethodName
     * @return
     */
    private String buildScreenshotFilePath(String path, String testMethodName) {
        path = System.getProperty("user.dir") + "\\" + path + getCurrentDate()
                + "\\temp";
        String fileName = testMethodName + "_" + UUID.randomUUID() + ".png";
        return path + fileName;
    }

    /**
     * this method switches to default frame
     * 
     * @param browserDriverInstance
     */
    public static void switchToDefaultFrame(WebDriver browserDriverInstance) {
        browserDriverInstance.switchTo().defaultContent();
        CSLogger.info("Switched till default frame");
    }

    /**
     * This method performs right click operation on given product.
     * 
     * @param waitForReload WebDriverWait object.
     * @param rightClickElement WebElement on which right click operation will
     *            be performed.
     * @param browserDriverInstance WebDriver object.
     */
    public static void rightClickTreeNode(WebDriverWait waitForReload,
            WebElement rightClickElement, WebDriver browserDriverInstance) {
        CSUtility.tempMethodForThreadSleep(1000);
        waitForVisibilityOfElement(waitForReload, rightClickElement);
        Actions rightClickTreeNode = new Actions(browserDriverInstance);
        rightClickTreeNode.contextClick(rightClickElement).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("Right Clicked On Tree node");
    }

    /**
     * This method helps to traverse all frames upto left section PIM Studio
     * 
     * @param waitForReload
     * @param browserDriverInstance
     */
    public static void traverseToPimStudio(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrame190()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrmTree()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmPimStudio()));
        CSLogger.info("Traversed till Pim Setting Node  & Pim Studio Frame");
    }

    /**
     * This method traverses through all frames upto Mam studio in Media tab
     * 
     * @param waitforReload
     * @param browserDriverInstance
     */
    public static void travereToMamStudio(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrame_192MamStudio()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalBodyFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrmTree()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrameMamStudio()));
    }

    /**
     * This method helps to traverse upto main frame and switch in right section
     * pane
     * 
     * @param waitForReload
     * @param browserDriverInstance
     */
    public static void traverseToSettingsConfiguration(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrame190()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traversed till Settings Configuration");
    }

    /**
     * This method helps to traverse upto main frame and switch in right section
     * pane
     * 
     * @param waitForReload
     * @param browserDriverInstance
     */
    public static void traverseToSettingsConfigurationMam(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrame_192MamStudio()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalBodyFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traversed till Settings Configuration in Mam.");
    }

    /**
     * This method waits till the element is visible
     * 
     * @param waitForReload
     * @param element
     */
    public static void waitForVisibilityOfElement(WebDriverWait waitForReload,
            WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * This method switch the till SpiltArea Left Frame
     * 
     * @param waitForReload
     * @param browserDriverInstance
     */
    public static void switchToSplitAreaFrameLeft(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameLeft()));
        CSLogger.info("Traversed till SpiltArea Left Frame");
    }

    /**
     * This method scrolls down or up to specified webElement
     * 
     * @param scrollToElement type of webElement
     * @param driver browser driver Instance
     */
    public static void scrollUpOrDownToElement(WebElement scrollToElement,
            WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", scrollToElement);
        CSLogger.info("Scroll to element");
    }

    /**
     * This method returns the alert box.
     * 
     * @return Alert Instance of Alert class representing the DOM alert box.
     */
    public static Alert getAlertBox(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        Alert alertBox;
        waitForReload.until(ExpectedConditions.alertIsPresent());
        alertBox = browserDriver.switchTo().alert();
        CSLogger.info("Switched to alert dialogue box.");
        return alertBox;
    }

    /**
     * Switches till CsPortalWindowContent frame.
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance Instance of webDriver
     */
    public static void traverseToCsPortalWindowContent(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Traversed to CsPortalWindowContent ");
    }

    /**
     * This method waits till the element is clickable.
     * 
     * @param waitForReload WebDriverWait object
     * @param element
     * 
     */
    public static void waitForElementToBeClickable(WebDriverWait waitForReload,
            WebElement element) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * This method traverse to attribute mapping frame
     * 
     * @param waitForReload object waits to reload the driver.
     * @param browserDriver object of browser driver
     * @param iframeLocatorsInstance object of frame locator class
     */
    public static void traverseToAttributeMapping(WebDriverWait waitForReload,
            WebDriver browserDriver, FrameLocators iframeLocatorsInstance) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmAttributeMapping()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traverse to Attribute Mapping");
    }

    /**
     * This method traverse to edit frame
     * 
     * @param waitForReload object waits to reload the driver.
     * @param browserDriver object of browser driver
     * @param iframeLocatorsInstance object of frame locator class
     */
    public static void traverseToEditWindowAttributeMapping(
            WebDriverWait waitForReload, WebDriver browserDriver,
            FrameLocators iframeLocatorsInstance) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmAttributeMapping()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEdit()));
        CSLogger.info("Traverse to Attribute Mapping Edit Window");
    }

    /**
     * This method returns the list header index of given header
     * 
     * @param headerName String object contains header name i.e label,ID
     * 
     * @param browserDriver WebDriver object.
     * 
     * @return integer value i.e index of header.
     */
    public static int getListHeaderIndex(String headerName,
            WebDriver browserDriver) {
        List<WebElement> listHeaders = browserDriver.findElements(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr/th"
                        + "[contains(@class,'ListHeader')]/a"));
        int headerIndex = 0;
        for (Iterator<WebElement> iterator = listHeaders.iterator(); iterator
                .hasNext();) {
            WebElement element = iterator.next();
            if (!(element.getText().isEmpty())) {
                if (element.getText().equals(headerName)) {
                    headerIndex++;
                    break;
                } else {
                    headerIndex++;
                }
            }
        }
        return headerIndex + 1;
    }

    /**
     * This method traverse till the system preferences under the setting tab.
     * it will traverse some common frames of left side pane which is used under
     * the setting tab. Traverse topFrame->frame_127 -> StudioWidgetPane
     * 
     * @param waitForReload object waits to reload the driver.
     * 
     * @param browserDriver object of browser driver
     * 
     * @param iframeLocatorsInstance object of frame locator class
     * 
     */
    public static void traversetoSystemPreferences(WebDriverWait waitForReload,
            WebDriver browserDriver, FrameLocators iframeLocatorsInstance) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getsettingsRightPaneMainFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getStudioWidgetPaneContentFrame()));

    }

    /**
     * This method traverse till the system preferences Right pane frame. this
     * frame is used till to find the save button or any editor at right side it
     * will traverse some common frames of left side and then switch to right
     * side under the setting tab. Traverse topFrame->frame_127->
     * StudioWidgetPane->main
     * 
     * @param waitForReload object waits to reload the driver.
     * 
     * @param browserDriver object of browser driver
     * 
     * @param iframeLocatorsInstance object of frame locator class
     */
    public static void traverseToSystemPrefRightPaneFrames(
            WebDriverWait waitForReload, WebDriver browserDriver,
            FrameLocators iframeLocatorsInstance) {
        CSUtility.traversetoSystemPreferences(waitForReload, browserDriver,
                iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traverse to System Pref right pane frame.");
    }

    /**
     * This method will switch to studio list from new list view in PIM products
     */
    public static void switchToStudioList(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        CSPortalHeader csPortalHeaderInstance = new CSPortalHeader(
                browserDriver);
        PimStudioProductsNodePage pimStudioProductsNode = new PimStudioProductsNodePage(
                browserDriver);
        CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSUtility.tempMethodForThreadSleep(2000);
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("clicked on products node in PIM studio");
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        List<WebElement> btnStudioList = browserDriver.findElements(By.xpath(
                "//button[@data-original-title='Switch to Studio List']"));
        int countBtnStudioList = btnStudioList.size();
        if (countBtnStudioList != 0) {
            csGuiToolbarHorizontalInstance.getBtnSwitchToStudioList().click();
            CSLogger.info("switched to studio list view");
        } else {
            CSLogger.info("Already switched to studio list view");
        }
    }

}