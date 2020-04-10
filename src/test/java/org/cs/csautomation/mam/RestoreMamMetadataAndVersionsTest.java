/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import org.cs.csautomation.cs.mam.CSGuiDalogContentIdMam;
import org.cs.csautomation.cs.mam.CSGuiToolbarHorizontalMam;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.mam.MamVolumeSplitAreaContentPage;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This test class restores metadata and content versions by right clicking on
 * restore version option in the pop
 * 
 * @author CSAutomation Team
 *
 */
public class RestoreMamMetadataAndVersionsTest extends AbstractTest {

    private CSPortalHeader                csPortalHeader;
    private WebDriverWait                 waitForReload;
    private FrameLocators                 locator;
    private MamStudioVolumesNode          mamStudioVolumesNode;
    private IVolumePopup                  demoVolumePopup;
    private String                        mamMetadataRestoreVersionSheet = "RestoreMamMetadataAndVersions";
    private CSGuiToolbarHorizontalMam     csGuiToolbarHorizontalMam;
    private CSGuiDalogContentIdMam        csGuiDalogContentIdMam;
    private MamVolumeSplitAreaContentPage mamVolumeSplitAreaContentpage;
    private CSPopupDivMam                 csPopupDivMam;

    /**
     * This test case restores the mam metadata and content versions by clicking
     * on restore version option in pop up
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     * @param externalKey contains the external key
     * @param modifiedExternalKey contains the name of the modified external key
     */
    @Test(priority = 1, dataProvider = "deleteMetadataAndContentVersions")
    public void testRestoreMamMetaDataAndContentVersion(String testFolder,
            String volumeName, String imageName, String externalKey,
            String modifiedExternalKey) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            traverseUptoCreatedImage(testFolder, volumeName, imageName);
            clickOnHistory(testFolder, volumeName, imageName);
            checkInCheckOutOnExternalKey(externalKey, modifiedExternalKey);
            traverseToHistory();
            restoreVersion(testFolder, volumeName, imageName);
            verifyRestoredVersion(externalKey);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method verifies if version has restored or not
     * 
     * @param externalKey contains the string to verify with obtained external
     *            key value
     */
    private void verifyRestoredVersion(String externalKey) {
        mamStudioVolumesNode.traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDalogContentIdMam.getBtnPropertiesMam()));
        csGuiDalogContentIdMam.getBtnPropertiesMam().click();
        CSUtility.tempMethodForThreadSleep(2000);
        String keyValueAfterRestore = waitForReload
                .until(ExpectedConditions
                        .visibilityOf(getExternalKeyTextBoxReference()))
                .getAttribute("value");
        if (keyValueAfterRestore.equals(externalKey)) {
            CSLogger.info("Verification of restored version is successful");
        } else {
            CSLogger.error("Could not verify restored version");
            Assert.fail("Failed to verify restored version");
        }
    }

    /**
     * This method traverses upto created image under test folder
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     */
    private void traverseUptoCreatedImage(String testFolder, String volumeName,
            String imageName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolder));
        createdVolumeName.click();
        WebElement uploadedImage = browserDriver
                .findElement(By.linkText(imageName));
        uploadedImage.click();
        CSLogger.info("Clicked on uploaded image");
    }

    /**
     * This method clicks on the history tab in the rightmost pane
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     */
    private void clickOnHistory(String testFolder, String volumeName,
            String imageName) {
        mamStudioVolumesNode.traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDalogContentIdMam.getBtnPropertiesMam()));
        csGuiDalogContentIdMam.getBtnPropertiesMam().click();
        CSUtility.tempMethodForThreadSleep(3000);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                mamVolumeSplitAreaContentpage.getBtnProductHistory()));
        mamVolumeSplitAreaContentpage.getBtnProductHistory().click();
        CSLogger.info("Clicked on Hisory.");

    }

    /**
     * This method performs check in and checkout on external key
     * 
     * @param externalKey contains the name of the external key
     * @param modifiedExternalKey contains the name of the external key which
     *            has been sent as modified
     */
    private void checkInCheckOutOnExternalKey(String externalKey,
            String modifiedExternalKey) {
        setExternalKey(externalKey, modifiedExternalKey);
        performCheckInCheckOut();
        editExternalKey(modifiedExternalKey);
        performCheckInCheckOut();
    }

    /**
     * This method sets the external key in the extenal key textbox
     * 
     * @param externalKey contains the string as original key before restore
     * @param modifiedExternalKey contains the name of the modified external key
     */
    private void setExternalKey(String externalKey,
            String modifiedExternalKey) {
        mamStudioVolumesNode.traverseUptoMainFrame();
        WebElement externalKeyTxtBoxReference = getExternalKeyTextBoxReference();
        waitForReload.until(
                ExpectedConditions.visibilityOf(externalKeyTxtBoxReference));
        externalKeyTxtBoxReference.clear();
        externalKeyTxtBoxReference.sendKeys(externalKey);
    }

    /**
     * This method checks in and checks out the image
     */
    private void performCheckInCheckOut() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckIn()));
        csGuiToolbarHorizontalMam.getBtnCheckIn().click();
        CSLogger.info("Checked in");
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
        csGuiToolbarHorizontalMam.getBtnCheckout().click();
        CSLogger.info("Checked out");
    }

    /**
     * This method edits the external key from original value to modified value
     * 
     * @param modifiedExternalKey contains the name of the modified external key
     *            edited after setting up original external key
     */
    private void editExternalKey(String modifiedExternalKey) {
        WebElement editedExternalKey = getExternalKeyTextBoxReference();
        waitForReload.until(ExpectedConditions.visibilityOf(editedExternalKey));
        editedExternalKey.clear();
        editedExternalKey.sendKeys(modifiedExternalKey);
    }

    /**
     * This method gets the reference of external key textbox
     * 
     * @return externalKeyTextBoxReference
     */
    private WebElement getExternalKeyTextBoxReference() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDalogContentIdMam.getExternalKeyTxtBox()));
        return csGuiDalogContentIdMam.getExternalKeyTxtBox();
    }

    /**
     * This method traverses upto History option
     */
    private void traverseToHistory() {
        mamStudioVolumesNode.traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                mamVolumeSplitAreaContentpage.getBtnProductHistory()));
        mamVolumeSplitAreaContentpage.getBtnProductHistory().click();
        CSLogger.info("Clicked on Hisory.");
    }

    /**
     * This method restores the version by right clicking on the any sample
     * version created
     * 
     * @param testFolder contains the name of the test folder under which image
     *            has been assigned
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     */
    private void restoreVersion(String testFolder, String volumeName,
            String imageName) {
        traverseToHistoryFrame();
        WebElement historyVersion = getHistoryVersionToRestore();
        CSUtility.rightClickTreeNode(waitForReload, historyVersion,
                browserDriver);
        clkRestorVersion();
    }

    /**
     * This method performs the click operation on the restored version option
     */
    private void clkRestorVersion() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        csPopupDivMam.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getRestoreVersionOption(), browserDriver);
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSLogger.info("Clicked on ok of pop up after clicking restore version");

    }

    /**
     * This method returns the history version which is to be restored
     * 
     * @return historyVersion
     */
    private WebElement getHistoryVersionToRestore() {
        WebElement historyVersion = browserDriver.findElement(
                By.xpath("(//a[contains(@id, 'CS_change')])[3]//span[1]/span"));
        return historyVersion;
    }

    /**
     * This method traverses to history frame to click on history option
     */
    private void traverseToHistoryFrame() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[contains(@id,'57_Content')]")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@id='HistoryTree']")));
    }

    /**
     * This data provider returns the sheet data which contains test folder name
     * ,volume name ,external key value to set before restore, modified external
     * key value
     * 
     * @return mamMetadataAndVersionSheet
     */
    @DataProvider(name = "deleteMetadataAndContentVersions")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                mamMetadataRestoreVersionSheet);
    }

    /**
     * This method initializes all the resources required to drive test case
     * 
     */
    @BeforeClass
    public void initializeResources() {
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        csGuiToolbarHorizontalMam = new CSGuiToolbarHorizontalMam(
                browserDriver);
        csGuiDalogContentIdMam = new CSGuiDalogContentIdMam(browserDriver);
        mamVolumeSplitAreaContentpage = new MamVolumeSplitAreaContentPage(
                browserDriver);
        csPopupDivMam = new CSPopupDivMam(browserDriver);
    }
}
