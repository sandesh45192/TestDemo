/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.util.ArrayList;
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
 * This class deletes mamMetadata and versions by clicking on delete version and
 * delete older versions option with verification
 * 
 * @author CSAutomation team
 *
 */
public class DeleteMamMetaDataAndVersionsTest extends AbstractTest {

    private CSPortalHeader                csPortalHeader;
    private WebDriverWait                 waitForReload;
    private FrameLocators                 locator;
    private MamStudioVolumesNode          mamStudioVolumesNode;
    private IVolumePopup                  demoVolumePopup;
    private String                        mamMetadataAndVersionSheet = "DeleteMamMetadataAndVersions";
    private CSGuiToolbarHorizontalMam     csGuiToolbarHorizontalMam;
    private CSGuiDalogContentIdMam        csGuiDalogContentIdMam;
    private MamVolumeSplitAreaContentPage mamVolumeSplitAreaContentpage;
    private CSPopupDivMam                 csPopupDivMam;

    /**
     * This method deletes mam metadata and content version
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     */
    @Test(priority = 1, dataProvider = "deleteMetadataAndContentVersions")
    public void testDeleteMamMetaDataAndContentVersion(String testFolder,
            String volumeName, String imageName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            traverseUptoCreatedImage(testFolder, volumeName, imageName);
            checkOutImage();
            clickOnHistory(testFolder, volumeName, imageName);
            deleteHistory(testFolder, volumeName, imageName);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in testDeleteMamMetaDataAndContentVersion",e);
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
        CSUtility.tempMethodForThreadSleep(3000);
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                browserDriver.findElement(By.linkText(testFolder))));
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolder));
        createdVolumeName.click();
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                browserDriver.findElement(By.linkText(imageName))));
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
        traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDalogContentIdMam.getBtnPropertiesMam()));
        csGuiDalogContentIdMam.getBtnPropertiesMam().click();
        CSUtility.tempMethodForThreadSleep(3000);
        waitForReload.until(ExpectedConditions.visibilityOf(
                csGuiDalogContentIdMam.getSplitAreaBorderRight()));
        csGuiDalogContentIdMam.getSplitAreaBorderRight().click();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                mamVolumeSplitAreaContentpage.getBtnProductHistory()));
        mamVolumeSplitAreaContentpage.getBtnProductHistory().click();
        CSLogger.info("Clicked on Hisory.");
    }

    /**
     * This method deletes history of selected version
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the images
     */
    private void deleteHistory(String testFolder, String volumeName,
            String imageName) {
        traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        traverseToHistoryFrame();
        int versionCountBeforeDelete = getCountOfVersions();
        WebElement historyVersion = getHistoryVersionToDelete();
        CSUtility.rightClickTreeNode(waitForReload, historyVersion,
                browserDriver);
        clkDeleteVersion(versionCountBeforeDelete);
    }

    /**
     * This method returns the history version to delete
     * 
     * @return history version
     */
    private WebElement getHistoryVersionToDelete() {
        WebElement historyVersion = browserDriver.findElement(
                By.xpath("(//a[contains(@id, 'CS_change')])[2]//span[1]/span"));
        return historyVersion;
    }

    /**
     * This method clicks on delete version option
     * 
     * @param versionCountBeforeDelete contains the integer value of count of
     *            versions before deletion
     */
    private void clkDeleteVersion(int versionCountBeforeDelete) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        csPopupDivMam.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getDeleteVersionOption(), browserDriver);
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSLogger.info(
                "Clicked on ok button after clicking delete version option");
        CSUtility.tempMethodForThreadSleep(3000);
        int versionCountAfterDelete = getCountOfVersions();
        verifyDeletedVersion(versionCountBeforeDelete, versionCountAfterDelete);
    }

    /**
     * This method verifies version has deleted or not
     * 
     * @param versionCountBeforeDelete contains the count of number of versions
     *            before delete
     * @param versionCountAfterDelete contains the count of number of versions
     *            after delete
     */
    private void verifyDeletedVersion(int versionCountBeforeDelete,
            int versionCountAfterDelete) {
        try {
            traverseUptoMainFrame();
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsSideBarBodyFrame()));
            traverseToHistoryFrame();
            if (versionCountAfterDelete == versionCountBeforeDelete - 1) {
                CSLogger.info("History version successfully deleted");
            } else {
                CSLogger.info("History version not deleted");
                Assert.fail("Failed to delete history version");
            }
        } catch (Exception e) {
            CSLogger.debug("History version is not deleted");
        }
    }

    /**
     * This method gets the count of all versions present
     * 
     * @return countOfAllVersions
     */
    private int getCountOfVersions() {
        traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        traverseToHistoryFrame();
//        WebElement element = getHistoryVersionToDelete();
        List<WebElement> versions = browserDriver
                .findElements(By.xpath("//a[contains(@id, 'CS_change')]"));
        int countOfAllVersions = versions.size();
        return countOfAllVersions;
    }

    /**
     * This method traverses up to history frame to click on history
     */
    private void traverseToHistoryFrame() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[contains(@id,'57_Content')]")));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@id='HistoryTree']")));
    }

    /**
     * This method traverses up to main frame in right section pane
     */
    private void traverseUptoMainFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrame_192MamStudio()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getModuleContentFrameMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method deletes MAM metadata and performs delete older versions
     * operation
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the volume name
     * @param imageName contains the name of the image
     */
    @Test(priority = 2, dataProvider = "deleteMetadataAndContentVersions")
    public void testDeleteMamMetaDataAndOlderVersions(String testFolder,
            String volumeName, String imageName) {
        // pre-requisite is to check-in and checkout three times to create
        // history
        performCheckinCheckout();
        traverseUptoCreatedImage(testFolder, volumeName, imageName);
        traverseUptoHistory();
        deleteOldVersions();
    }

    /**
     * This method traverses upto History
     */
    private void traverseUptoHistory() {
        traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                mamVolumeSplitAreaContentpage.getBtnProductHistory()));
        mamVolumeSplitAreaContentpage.getBtnProductHistory().click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("Traversed upto Hisory.");
    }

    /*
     * This method performs check in and checkout three times before performing
     * delete old versions operation
     */
    private void performCheckinCheckout() {
        traverseUptoMainFrame();
        for (int i = 0; i < 3; i++) {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckIn()));
            csGuiToolbarHorizontalMam.getBtnCheckIn().click();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
            csGuiToolbarHorizontalMam.getBtnCheckout().click();
        }
    }

    /**
     * This method deletes old versions
     */
    private void deleteOldVersions() {
        int countBeforeDelete = getCountOfVersions();
        CSUtility.rightClickTreeNode(waitForReload, getHistoryVersionToDelete(),
                browserDriver);
        clkDeleteOldVersions(countBeforeDelete);
    }

    /**
     * This method clicks on delete old versions option afer right clicking on
     * version
     * 
     * @param countBeforeDelete contains count of versions before delete
     */
    private void clkDeleteOldVersions(int countBeforeDelete) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getDeleteOldVersionsOption()));
        demoVolumePopup.getDeleteOldVersionsOption().click();
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSLogger.info("Clicked on ok button after deleting old version");
        CSUtility.tempMethodForThreadSleep(2000);
        int countAfterDelete = getCountOfVersions();
        CSUtility.tempMethodForThreadSleep(3000);
        verifyDeletedOlderVersions(countBeforeDelete, countAfterDelete);
    }

    /**
     * This method verifies if older versions have been deleted or not
     * 
     * @param countBeforeDelete contains integer value of number of versions
     *            before deleting
     * @param countAfterDelete contains integer value of number of versions
     *            after deleting old versions
     */
    private void verifyDeletedOlderVersions(int countBeforeDelete,
            int countAfterDelete) {
        try {
            if (countAfterDelete < countBeforeDelete) {
                if (countAfterDelete > 1 && countAfterDelete <= 2)
                    CSLogger.info(
                            "Verification of older versions is successful");
            } else {
                CSLogger.info("Verification failed");
                Assert.fail("Verification of older versions failed");
            }
        } catch (Exception e) {
            CSLogger.debug("Verification failed");
        }
    }

    /**
     * This method performs checkout operation.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void checkOutImage() {
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        WebElement viewInfoTable = browserDriver.findElement(By.xpath(
                "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr"));
        ArrayList<WebElement> getFirstRow = (ArrayList<
                                                       WebElement>) viewInfoTable
                                                               .findElements(By
                                                                       .xpath("//td/a/img"));
        if ((getFirstRow.get(0).getAttribute("src")).endsWith("checkout.gif")) {
            csGuiToolbarHorizontalMam.getBtnCheckout().click();
            CSLogger.info("checked out image");
        } else if (getFirstRow.get(0).getAttribute("src")
                .endsWith("checkin.gif")) {
            CSLogger.info("Image already in checkout state");
        }
    }

    /**
     * This data provider returns sheet which contains testFolder name,volume
     * name under which test folder has created and image name which is created
     * under volume
     * 
     * @return mamMetadataAndVersionSheet
     */
    @DataProvider(name = "deleteMetadataAndContentVersions")
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