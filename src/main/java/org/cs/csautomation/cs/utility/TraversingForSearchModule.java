/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains common methods for frame traversing in open search module
 * 
 * @author CSAutomation Team
 *
 */
public class TraversingForSearchModule {

    /**
     * This method will traverse frames to access elements in search header
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains browser driver instance
     */
    public static void frameTraversingForCollectionsPopup(
            WebDriverWait waitForReload, WebDriver browserDriver) {
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame197()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSearchFrameWidget197()));
        CSLogger.info("traversed till frame FrameWidget197");
    }

    public static void traverseToSettingsConfigurationToAddProductToFavorites(
            WebDriverWait waitForReload, WebDriver browserDriver) {
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame190()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmCsPortalBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        CSLogger.info(
                "switched to frame of checkbox to add product to favorite");
    }

    /**
     * This is test case specific frame traversing, to add all values from root
     * folder into the newly created share.
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains browser driver instance
     */
    public static void traverseToFrmToAddAllRootFolderValues(
            WebDriverWait waitForReload, WebDriver browserDriver) {
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmToAddAllValues()));
        CSLogger.info(
                "switched to the frame to add all the values in root folder");
    }
}
