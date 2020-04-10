/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import org.cs.csautomation.cs.settings.UserManagementPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author CSAutomation Team
 *
 */
public class TraversingForSettingsModule {

    /**
     * This method clicks on the System Preferences
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains browser driver instance
     */
    public static void clkSystemPreferences(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        try {
            UserManagementPage userManagementPage = new UserManagementPage(
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    userManagementPage.getSystemPreferencesNode()));
            userManagementPage.getSystemPreferencesNode().click();
            CSLogger.info("Clicked on System Preferences");
        } catch (Exception e) {
            CSLogger.info("Failed to click on System preferences ");
        }
    }

    /**
     * This method traverses to user management after clicking system
     * preferences
     * 
     * @param waitForReload
     *            waits for an element to reload
     * @param browserDriver
     *            contains browser driver instance
     */
    public static void traverseToUserManagement(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getsettingsRightPaneMainFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
    }

    /**
     * This method traverses to main frame in translation manager
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains instance of WebDriver
     */
    public void traverseToMainFrameTranslationManager(
            WebDriverWait waitForReload, WebDriver browserDriver) {
        CSUtility.switchToDefaultFrame(browserDriver);
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traversed upto main frame ");
    }

    /**
     * This method traverses upto main frame
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains browser driver instance
     */
    public static void traverseToMainFrame(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        CSUtility.switchToDefaultFrame(browserDriver);
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traversed upto main frame ");
    }

    /**
     * This method traverses to edit frame
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains browser driver instance
     */
    public static void traverseToEditFrame(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
    }

    /**
     * This method traverses to nodes in left pane in translation Manager
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains instance of WebDriver
     */
    public static void traverseToNodesInLeftPaneTranslationManager(
            WebDriverWait waitForReload, WebDriver browserDriver) {
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getsettingsRightPaneMainFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTranslationManager()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
    }

    /**
     * This method traverses to Node In Left Pane
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains browser driver instance
     */
    public static void traverseToNodesInLeftPaneOfSystemPreferences(
            WebDriverWait waitForReload, WebDriver browserDriver) {
            traverseToUserManagement(waitForReload, browserDriver);
    }

    /**
     * This method traverses to project manager frame
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains browser driver instance
     */
    public void traverseToEditTemplate(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        traverseToCsPortalWidgetFrames(waitForReload, browserDriver,
                iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmStudioWidgetPaneContentProjectManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrmTree()));
        CSLogger.info("traverse to project manager pane");
    }

    /**
     * This method traverse to edit window of Template
     * 
     * @param element contains the element to be waited on
     * @param browserDriverInstance contains instance of browser driver
     */
    public void traverseToEditFrameProjectManager(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseToCsPortalWidgetFrames(waitForReload, browserDriverInstance,
                iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmStudioWidgetPaneContentProjectManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traverse to edit window template");
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
        traversetoSystemPreferences(waitForReload, browserDriver,
                iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traversed till SplitAreaFrameMain");
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
        CSLogger.info("Traversed till StudioWidgetPaneContentFrame");
    }

    /**
     * This method traverse to edit window of Task
     * 
     * @param element contains the element to be waited on
     * @param browserDriverInstance contains instance of browser driver
     */
    public void traverseToEditFrameTask(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseToCsPortalWidgetFrames(waitForReload, browserDriverInstance,
                iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmStudioWidgetPaneContentProjectManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEdit()));
        CSLogger.info("Traverse to edit window task");
    }

    /**
     * This method traverse to edit window of Task
     * 
     * @param element contains the element to be waited on
     * @param browserDriverInstance contains instance of browser driver
     */
    public void traverseToBrowseProjectManager(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators locator = FrameLocators
                .getIframeLocators(browserDriverInstance);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmbrowse()));
    }

    /**
     * This method will traverse to the frame of dropdown list on the left of
     * page, once you enter into SETTINGS header
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriver instance of webDriver
     * @param iframeLocatorsInstance instance of FrameLocators class
     */
    public static void traverseToFrameOfSettingsLeftPaneTree(
            WebDriverWait waitForReload, WebDriver browserDriver,
            FrameLocators iframeLocatorsInstance) {
        traversetoSystemPreferences(waitForReload, browserDriver,
                iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrmTree()));
        CSLogger.info("switched to the frame, frmTree");
    }

    /**
     * The below frame switching is specifically for 'Active Job' test cases.
     * This method will traverse to the frame, where we locate the element to
     * create new active script
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriver instance of webDriver
     * @param iframeLocatorsInstance instance of FrameLocators class
     */
    public static void traverseToFrameOfSettingsSplitAreaFrameMain(
            WebDriverWait waitForReload, WebDriver browserDriver,
            FrameLocators iframeLocatorsInstance) {
        traversetoSystemPreferences(waitForReload, browserDriver,
                iframeLocatorsInstance);
        iframeLocatorsInstance.switchToSplitAreaFrame(waitForReload);
        iframeLocatorsInstance.switchToSplitAreaFrame(waitForReload);
    }

    /**
     * The below frame switching is specifically for 'Active Job' test cases.
     * This method will traverse to the frame, where we enter the label,
     * category & select the script for active job
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriver instance of webDriver
     * @param iframeLocatorsInstance instance of FrameLocators class
     */
    public static void traverseToFrameEdit(WebDriverWait waitForReload,
            WebDriver browserDriver, FrameLocators iframeLocatorsInstance) {
        traversetoSystemPreferences(waitForReload, browserDriver,
                iframeLocatorsInstance);
        iframeLocatorsInstance.switchToSplitAreaFrame(waitForReload);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEditForActiveScript()));
        CSLogger.info("Switched to frame edit");
    }

    /**
     * Traverses frames of CsPortalWidget.
     */
    public static void traverseToCsPortalWidgetFrames(
            WebDriverWait waitForReload, WebDriver browserDriver,
            FrameLocators iframeLocatorsInstance) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getsettingsRightPaneMainFrame()));
    }

    /**
     * This method traverse up to Task Section
     * 
     */
    public void traverseToTaskSection(WebDriverWait waitForReload,
            WebDriver browserDriver, FrameLocators iframeLocatorsInstance) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmcsSideBarBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTasks()));
        CSLogger.info("Traverse to Task Section.");
    }

    /**
     * This method traverse up to setting configuration project manager
     * 
     */
    public void traverseToSettingConfigurationProjectManager(
            WebDriverWait waitForReload, WebDriver browserDriver,
            FrameLocators iframeLocatorsInstance) {
        traverseToCsPortalWidgetFrames(waitForReload, browserDriver,
                iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmStudioWidgetPaneContentProjectManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
    }
}
