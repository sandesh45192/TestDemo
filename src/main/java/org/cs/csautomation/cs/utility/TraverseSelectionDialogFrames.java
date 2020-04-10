
package org.cs.csautomation.cs.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TraverseSelectionDialogFrames {

    /**
     * Switches the frames till CsPortalWindowContent frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseToCsPortalWindowContentFrame(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Traversed till CsPortalWindowContent frame");
    }

    /**
     * Switches the frames till FrmSpiltAreaFramePdmarticleconfigurationLeft
     * frame
     * 
     * @param waitForReload this is WebDriverWait object
     * @param browserDriverInstance this is instance of browser
     */
    public static void traverseToDataSelectionDialogLeftSection(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsGuiDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmProductClassAttribute()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmSpiltAreaFramePdmarticleconfigurationLeft()));
        CSLogger.info(
                "Traversed till SpiltAreaFramePdmarticleconfigurationLeft frame");
    }

    /**
     * This method switches the frames till IdRecordsPdmarticleLeft frame
     * 
     * @param waitForReload this is WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillProductsFolderRightPane(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmProductsFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsPdmarticleLeft()));
        CSLogger.info("Traverse till IdRecordsPdmarticleLeft frame");
    }

    /**
     * This method switches the frames till IdFoldersLeft frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillFileFoldersleftFrames(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseTillFileFoldersFrame(waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdFoldersLeft()));
        CSLogger.info("Traverse till IdFoldersLeft frame");
    }

    /**
     * This method switches the frames till IdFoldersCenter frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillFileFoldersCenterPane(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseTillFileFoldersFrame(waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdFoldersCenter()));
        CSLogger.info("Traverse till IdFoldersCenter frame");
    }

    /**
     * This method switches the frames till file folders frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillFileFoldersFrame(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFileFoldersFrame()));
        CSLogger.info("Traversed till file folders frames");
    }

    /**
     * This method switches the frames till IdRecordsUserLeft frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillUserFoldersleftFrames(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseTillUserFoldersFrame(waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsUserLeft()));
        CSLogger.info("Traverse till IdRecordsUserLeft frame");
    }

    /**
     * This method switches the frames till User folders frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillUserFoldersFrame(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmUserFolderFrame()));
        CSLogger.info("Traversed till User folders frames");
    }

    /**
     * This method switches the frames till IdRecordsCenter frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillUserFoldersCenterPane(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseTillUserFoldersFrame(waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsCenter()));
        CSLogger.info("Traverse till IdRecordsCenter frame");
    }

    /**
     * Traverse the frames till FrmIdRecordsCenter frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseToDataSelectionDialogCenterSection(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseToCsPortalWindowContentFrame(waitForReload,
                browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmProductClassAttribute()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsCenter()));
    }

    /**
     * Traverses the frames till center section of data selection dialog window,
     * for search portal
     * 
     * @param waitForReload - WebDriverWait object
     * @param browserDriverInstance - instance of browser
     */
    public static void traverseToDataSelectionDialogCenterSectionOpenSearch(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmProductsFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsCenter()));
        CSLogger.info("Traversed to frame IdRecordsCenter for open search");
    }

    /**
     * Traverses the frames till data selection dialog window, for search portal
     * 
     * @param waitForReload - WebDriverWait object
     * @param browserDriverInstance - instance of browser
     */
    public static void traverseToDataSelectionDialogLeftSectionOpenSearch(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsGuiDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsGuiDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsPdmarticleLeft()));
        CSLogger.info(
                "Traversed till SpiltAreaFramePdmarticleconfigurationLeft frame");
    }

    /**
     * Traverses the frames till Bottom section of data selection dialog window,
     * 
     * @param waitForReload - WebDriverWait object
     * @param browserDriverInstance - instance of browser
     */
    public static void traverseToDataSelectionDialogBottomSection(
            WebDriverWait waitForReload, WebDriver browserDriverInstance,
            FrameLocators iframeLocatorsInstance) {
        traverseToCsPortalWindowContentFrame(waitForReload,
                browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmDataSelectionDialogNestedOne()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmIdRecordsPdmarticleconfigurationBottom()));
        CSLogger.info("Traversed to frame IdRecordsBottm");
    }

    /**
     * Switches the frames till FrmSpiltAreaFramePdmarticleconfigurationLeft
     * frame
     * 
     * @param waitForReload this is WebDriverWait object
     * @param browserDriverInstance this is instance of browser
     */
    public static void traverseToDataSelectionDialogProductTree(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsGuiDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmProductsFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsPdmarticleLeft()));
        CSLogger.info(
                "Traversed till data selection dialog product tree  frames");
    }

    /**
     * Switches the frames till Sub CsPortalWindowContent frame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseToSubCsPortalWindowContentFrame(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSubCsPortalWindowContent()));
        CSLogger.info("Traversed till Sub CsPortalWindowContent frame");
    }

    /**
     * This method switches the frames till Sub IdRecordsPdmarticleLeft frame
     * 
     * @param waitForReload this is WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillSubProductsFolderRightPane(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        TraverseSelectionDialogFrames.traverseToSubCsPortalWindowContentFrame(
                waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmProductsFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsPdmarticleLeft()));
        CSLogger.info("Traverse till Sub IdRecordsPdmarticleLeft frame");
    }

    /**
     * Traversed till data selection dialog left most file folder.
     * 
     * @param waitForReload this is WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseToDataSelectionDialogFile(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseToCsPortalWindowContentFrame(waitForReload,
                browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmfileFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecorsMamFileLeft()));
        CSLogger.info(
                "Traversed till data selection dialog left most file folder");
    }

    /**
     * Traversed till data selection dialog center pane when 'File' folder is
     * selected.
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseTillDataSelectionDialogFileCenterPane(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseToCsPortalWindowContentFrame(waitForReload,
                browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmfileFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsCenter()));
        CSLogger.info("Traverse till IdFoldersCenter frame");
    }
    
    /**
     * Switches the frames till FrmSpiltAreaFramePdmarticleconfigurationLeft
     * frame
     * 
     * @param waitForReload
     *            this is WebDriverWait object
     * @param browserDriverInstance
     *            this is instance of browser
     */
    public static void traverseToDataSelectionDialogChannelTree(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseToCsPortalWindowContentFrame(waitForReload, browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmChannelsMidFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsPdmarticleStructureLeft()));
        CSLogger.info(
                "Traversed till data selection dialog product tree  frames");
    }

    /**
     * This method will traverse to data selection dialog to add ref to pim
     * product
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void traverseToDataSelectionDailogLeftSectionInSettingsHeader(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators locator = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCSPortalWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmProductsFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsPdmarticleLeft()));
        CSLogger.info("Traverse till Sub IdRecordsPdmarticleLeft frame");

    }

    /**
     * This method will traverse to data selection dialog to add ref to pim
     * product
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void
            traverseToDataSelectionDailogLeftSectionInSearchPortalSettings(
                    WebDriverWait waitForReload,
                    WebDriver browserDriverInstance) {
        FrameLocators locator = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCSPortalWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmProductClassAttribute()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsPdmarticleLeft()));
        CSLogger.info("Traverse till Sub IdRecordsPdmarticleLeft frame");

    }

    /**
     * This method will traverse to data selection dialog to add attribute to
     * collections
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void
            traverseToDataSelectionDailogLeftSectionInCollectionsAttributes(
                    WebDriverWait waitForReload,
                    WebDriver browserDriverInstance) {
        FrameLocators locator = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCSPortalWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmProductClassAttribute()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsPdmarticleconfigurationLeft()));
        CSLogger.info("Traverse till Sub IdRecordsPdmarticleLeft frame");

    }

    /**
     * Traverses the frames till center section of data selection dialog window,
     * for search portal
     * 
     * @param waitForReload - WebDriverWait object
     * @param browserDriverInstance - instance of browser
     */
    public static void
            traverseToDataSelectionDialogCenterSectionInCollectionsAttributes(
                    WebDriverWait waitForReload,
                    WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmCSPortalWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmProductClassAttribute()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsCenter()));
        CSLogger.info(
                "Traversed to frame IdRecordsCenter to add attribute to collections");
    }

    /**
     * This method will traverse to data selection dialog to add ref to pim
     * product
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance instance of browser
     */
    public static void
            traverseToDataSelectionDailogCenterSectionInSearchPortalSettings(
                    WebDriverWait waitForReload,
                    WebDriver browserDriverInstance) {
        FrameLocators locator = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCSPortalWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmProductClassAttribute()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsCenter()));
        CSLogger.info("Traverse till Sub ID records center frame");

    }

}
