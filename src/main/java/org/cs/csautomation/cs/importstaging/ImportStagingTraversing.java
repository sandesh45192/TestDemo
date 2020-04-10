package org.cs.csautomation.cs.importstaging;
/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * Class contains traversing common methods 
 * @author CSAutomation Team
 */
public class ImportStagingTraversing {
    public FrameLocators iframeLocatorsInstance;
    
    public ImportStagingTraversing(WebDriver browserDriver){
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
    }
    
    /*
     * This method is used to traverse the frames till the middle pane of the 
     * import job.
     * This is used to traverse till Import database to create new job of middle
     * pane
     * @param waitForReload
     * @param browserDriverInstance
     */
    public void traverseToImportMiddlePane(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        traverseToCommonFrames(waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        CSLogger.info("Traversed till Import staging middle pane List view");
    }
    
    /*
     * This method is used to traverse to the common frames
     * @param waitForReload
     * @param browserDriverInstance
     */
    public void traverseToCommonFrames(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrame190()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getImportStagingStudioWidgetFrame()));
         waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
    }
    
    /*
     * used to traverse to the config fields of the Edit frames
     * @param waitForReload
     * @param browserDriverInstance
     */
    public void traverseToConfigFields(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        traverseToCommonFrames(waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEdit())); 
    }

}
