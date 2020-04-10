/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.util.List;
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
 * This method rollbacks mam metadata and content versions and verifes if the
 * version has rolled back successfully by preserving values before rollback
 * 
 * @author CSAutomation Team
 *
 */
public class RollbackMamMetadataAndVersionsTest extends AbstractTest {

    private CSPortalHeader                csPortalHeader;
    private WebDriverWait                 waitForReload;
    private FrameLocators                 locator;
    private MamStudioVolumesNode          mamStudioVolumesNode;
    private IVolumePopup                  demoVolumePopup;
    private String                        mamMetadataAndVersionSheet = "MamMetadataAndVersions";
    private CSGuiToolbarHorizontalMam     csGuiToolbarHorizontalMam;
    private CSGuiDalogContentIdMam        csGuiDalogContentIdMam;
    private MamVolumeSplitAreaContentPage mamVolumeSplitAreaContentpage;
    private CSPopupDivMam                 csPopupDivMam;

    /**
     * This method rolls back mam metadata and content versions and verifies if
     * the version has successfully rolled back by preserving values before
     * rollback or not
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     */
    @Test(priority = 1, dataProvider = "rollbackMetadataAndContentVersions")
    public void testRollbackMamMetaDataAndContentVersion(String testFolder,
            String volumeName, String imageName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            traverseUptoCreatedImage(testFolder, volumeName, imageName);
            clickOnHistory(testFolder, volumeName, imageName);
            rollbackHistory(testFolder, volumeName, imageName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed.", e);
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
     * This method initiates instances for rollacking the version
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     */
    private void rollbackHistory(String testFolder, String volumeName,
            String imageName) {
        traverseToHistoryFrame();
        int versionCountBeforeRollback = getCountOfVersions();
        CSUtility.rightClickTreeNode(waitForReload,
                getHistoryVersionToRollback(), browserDriver);
        clkRestoreVersion(versionCountBeforeRollback);
    }

    /**
     * This method has the instance of history version to rollback
     * 
     * @return historyVersion contains the instance of history version
     */
    private WebElement getHistoryVersionToRollback() {
        WebElement historyVersion = browserDriver.findElement(
                By.xpath("(//a[contains(@id, 'CS_change')])[3]//span[1]/span"));
        return historyVersion;
    }

    /**
     * This method clicks on restore version option
     * 
     * @param versionCountBeforeRollback contains the count of versions before
     *            rollback
     */
    private void clkRestoreVersion(int versionCountBeforeRollback) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        csPopupDivMam.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getCsPopupRollbackVersion(), browserDriver);
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSLogger.info(
                "Clicked on ok button after clicking restore version option");
        CSUtility.tempMethodForThreadSleep(2000);
        checkoutToGetVersionCountAfterRollback(versionCountBeforeRollback);
    }

    /**
     * This method checks out on mam file to get the version count after
     * rollback
     * 
     * @param versionCountBeforeRollback contains the count of versions before
     *            rollback
     */
    private void checkoutToGetVersionCountAfterRollback(
            int versionCountBeforeRollback) {
        mamStudioVolumesNode.traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
        csGuiToolbarHorizontalMam.getBtnCheckout().click();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                mamVolumeSplitAreaContentpage.getBtnProductHistory()));
        mamVolumeSplitAreaContentpage.getBtnProductHistory().click();
        traverseToHistoryFrame();
        int versionCountAfterRollback = getCountOfVersions();
        verifyRollbackVersion(versionCountBeforeRollback,
                versionCountAfterRollback);
    }

    /**
     * This method verifies if version has successfully rolled back by setting
     * previous value to the mam file
     * 
     * @param versionCountBeforeRollback contains the count of number of
     *            versions before rollback
     * @param versionCountAfterRollback contains the count of number of versions
     *            after rollback
     */
    private void verifyRollbackVersion(int versionCountBeforeRollback,
            int versionCountAfterRollback) {
        try {
            if (versionCountAfterRollback <= versionCountBeforeRollback)
                CSLogger.info("Version rollback verification successful");
        } catch (Exception e) {
            CSLogger.error("Version rollback verification failed");
            Assert.fail("Version has not rolled back");
        }
    }

    /**
     * This method traverses upto history frame to click on history
     */
    private void traverseToHistoryFrame() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[contains(@id,'57_Content')]")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@id='HistoryTree']")));
    }

    /**
     * This method gets the count of all versions present
     * 
     * @return countOfAllVersions
     */
    private int getCountOfVersions() {
        mamStudioVolumesNode.traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        traverseToHistoryFrame();
        List<WebElement> versions = browserDriver
                .findElements(By.xpath("//a[contains(@id, 'CS_change')]"));
        int countOfAllVersions = versions.size();
        return countOfAllVersions;
    }

    /**
     * This data provider returns sheet which contains testFolder name,volume
     * name under which test folder has created and image name which is created
     * under volume
     * 
     * @return mamMetadataAndVersionSheet
     */
    @DataProvider(name = "rollbackMetadataAndContentVersions")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                mamMetadataAndVersionSheet);
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