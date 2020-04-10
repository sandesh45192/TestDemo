/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SynchronizeMamFolderTest extends AbstractTest {

    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private MamStudioVolumesNode mamStudioVolumesNode;
    private IVolumePopup         demoVolumePopup;
    private String               demoFolderSheetName = "CreateVolumeFolder";
    private CSPortalHeader       csPortalHeader;

    /**
     * This test case synchronizes the created demo volume folder
     * 
     * @param demoVolumeFolderName contains the name of demo volume folder
     * @param label contains the name of the label
     * @param volume name contains the name of the volume
     * @param folderName contains the name of the folder
     * @param subfolder contains the name of the sub folder to be synchronized
     *            under already created test folder
     */
    @Test(priority = 1, dataProvider = "synchronizeMamFolder")
    public void testSynchronizeMamFolder(String testFolderName, String label,
            String volumeName, String folderName, String subFolder) {
        try {
            expandCreatedVolumeNode(volumeName);
            WebElement createdVolumeName = browserDriver
                    .findElement(By.linkText(testFolderName));
            CSUtility.scrollUpOrDownToElement(createdVolumeName, browserDriver);
            CSUtility.rightClickTreeNode(waitForReload, createdVolumeName,
                    browserDriver);
            clkProcessOption();
            clkSynchronizeFolderOption();
            handleAlertBox();
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method expands created volume node
     * 
     * @param volumeName contains the name of the volume
     */
    private void expandCreatedVolumeNode(String volumeName) {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader.clkBtnMedia(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        CSUtility.tempMethodForThreadSleep(1000);
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);

    }

    /**
     * This method clicks on process option
     */
    private void clkProcessOption() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        performProcessClick();
    }

    /**
     * This method performs click on process option
     */
    private void performProcessClick() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getProcessOption()));
        demoVolumePopup.getProcessOption().click();
        CSLogger.info(
                "Clicked on process after right clicking on the demo volume"
                        + " folder");
    }

    /**
     * This method clicks on synchronize folder option
     */
    private void clkSynchronizeFolderOption() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(locator.getCsPopupDivMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPopupDivFramevMam()));
        performFolderClick();
    }

    /**
     * This method performs click on folder
     */
    private void performFolderClick() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getClkSynchronizedFolder()));
        mamStudioVolumesNode.getClkSynchronizedFolder().click();
        CSLogger.info("Clicked on synchronize folder button");
    }

    /**
     * This method handles the alert box that pops up after clicking on
     * synchronize folder
     */
    private void handleAlertBox() {
        try {
            Alert alertBox = null;
            alertBox = browserDriver.switchTo().alert();
            String getAlertText = browserDriver.switchTo().alert().getText();
            if (getAlertText.equalsIgnoreCase(
                    "Synchronization was Completed Successfully (Previews may"
                            + " be generated in the background)")) {
                CSUtility.tempMethodForThreadSleep(1000);
                alertBox.accept();
                CSLogger.info("Folder has synchronized successfully.");
            } else
                Assert.fail("Synchronization of demo volume folder failed");
        } catch (Exception e) {
            CSLogger.error(
                    "Could  not handle alertBox after clicking on synchronize"
                            + "folder option");
        }
    }

    /**
     * This data provider returns sheet which contains name of demo volume
     * folder and the demo volume file which has been created under demo volume
     * folder
     * 
     * @return demoFolderSheetName contains the name of the sheet
     */
    @DataProvider(name = "synchronizeMamFolder")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                demoFolderSheetName);
    }

    /**
     * This test initializes the resources used to drive the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);

    }
}
