/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TraversingForDashboardModule {

    /**
     * This method traverses to the dashboard page elements
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains instance of browserDriver
     */
    public static void traverseToDashboardPage(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        FrameLocators locator = FrameLocators.getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame_234()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrameWidget()));
    }
}
