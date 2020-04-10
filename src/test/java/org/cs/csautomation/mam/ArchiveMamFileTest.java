/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import org.cs.csautomation.cs.mam.CSGuiDalogContentIdMam;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioSettingsNode;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
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
 * This test class archives mam file and restore back mam file from the archived
 * state
 * 
 * @author CSAutomation Team
 *
 */
public class ArchiveMamFileTest extends AbstractTest {

    private CSPortalHeader         csPortalHeader;
    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private MamStudioVolumesNode   mamStudioVolumesNode;
    private IVolumePopup           demoVolumePopup;
    private CSGuiDalogContentIdMam csGuiDalogContentIdMam;
    private MamStudioSettingsNode  mamStudioSettingsNode;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontal;
    private String                 archiveFileSheet = "ArchiveFileSheet";

    /**
     * This test case performs functionality of mam file archive
     * 
     * @param testFolderName contains the name of the demo volume folder name
     * @param imagePath contains the path of the image
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
     */
    @Test(priority = 1, dataProvider = "archiveFileTest")
    public void testArchiveFile(String testFolderName, String imagePath,
            String imageName, String volumeName) throws Exception {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            imagePath = getFilePath(imagePath);
            csPortalHeader.clkBtnMedia(waitForReload);
            String archivePath = getArchivePath(testFolderName, imageName,
                    volumeName);
            setArchivePath(archivePath);
            archiveMamFile(testFolderName, imageName, volumeName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method get the file path of current system
     * 
     * @param filePath String object contains path from sheet
     * @return path String contains current file path
     */
    private String getFilePath(String filePath) {
        String currentPath = System.getProperty("user.dir");
        String path = currentPath + filePath;
        return path;
    }

    /**
     * This method set the archive path
     * 
     * @param archivePath string object contains contains archive path
     */
    private void setArchivePath(String archivePath) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
        mamStudioSettingsNode.getSettingsNodeMam().click();
        mamStudioSettingsNode.clkSettingsArchiveNode(waitForReload);
        mamStudioSettingsNode.clkSettingsStandardArchiveNode(waitForReload);
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDalogContentIdMam.getTxtArchiveBackupPath());
        csGuiDalogContentIdMam.getTxtArchiveBackupPath().clear();
        csGuiDalogContentIdMam.getTxtArchiveBackupPath().sendKeys(archivePath);
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Archive path is set");
    }

    /**
     * This method get the archive path
     * 
     * @param testFolderName String object contains the name of the demo volume
     *            folder
     * @param imageName String object contains the name of the image
     * @param volumeName String object contains the name of the volume
     * @return path String instance contains archive path
     */
    private String getArchivePath(String testFolderName, String imageName,
            String volumeName) {
        String path = null;
        clickCreatedVolume(testFolderName, volumeName);
        WebElement uploadedImage = browserDriver
                .findElement(By.linkText(imageName));
        uploadedImage.click();
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDalogContentIdMam.getBtnPropertiesMam()));
        csGuiDalogContentIdMam.getBtnPropertiesMam().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                mamStudioVolumesNode.getFileLocalPath());
        String text = mamStudioVolumesNode.getFileLocalPath().getText();
        path = constuctTextToArchivePath(text);
        return path;
    }

    /**
     * This method construct text to archive path
     * 
     * @param localFilePath String object contains text data
     * @return path String instance contains archive path
     */
    private String constuctTextToArchivePath(String localFilePath) {
        String path = localFilePath.substring(0,
                localFilePath.indexOf("volumes"));
        path = path + "/volumes/Archiv";
        return path;
    }

    /**
     * This method archives the mam file which has been created under demo
     * volume folder
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
     */
    private void archiveMamFile(String testFolderName, String imageName,
            String volumeName) {
        clickCreatedVolume(testFolderName, volumeName);
        clickObjectOption(imageName);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(locator.getCsPopupDivMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPopupDivFramevMam()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getArchiveOption()));
        demoVolumePopup.getArchiveOption().click();
        CSLogger.info("Clicked on archive option");
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSLogger.info("File has archived");
    }

    /**
     * This method clicks on object option
     * 
     * @param imageName contains image name
     */
    private void clickObjectOption(String imageName) {
        try {
            WebElement uploadedImage = browserDriver
                    .findElement(By.linkText(imageName));
            CSUtility.rightClickTreeNode(waitForReload, uploadedImage,
                    browserDriver);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getCsPopupDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getObjectOption()));
            demoVolumePopup.getObjectOption().click();
            CSLogger.info("Clicked on object option");
        } catch (Exception e) {
            CSLogger.error("Could not click on object option");
        }
    }

    /**
     * This method click on created vloume foler
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
     */
    private void clickCreatedVolume(String testFolderName, String volumeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolderName));
        createdVolumeName.click();
        CSLogger.info("Clicked on  volume folder");
    }

    /**
     * This test case restores the archive file
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param imagePath contains the path of the image
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
     */
    @Test(priority = 2, dataProvider = "archiveFileTest")
    public void testRestoreArchiveFile(String testFolderName, String imagePath,
            String imageName, String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement createdVolumeName = browserDriver
                    .findElement(By.linkText(testFolderName));
            createdVolumeName.click();
            WebElement uploadedImage = browserDriver
                    .findElement(By.linkText(imageName));
            CSUtility.rightClickTreeNode(waitForReload, uploadedImage,
                    browserDriver);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getCsPopupDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getObjectOption()));
            demoVolumePopup.getObjectOption().click();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getCsPopupDiv()));
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(locator.getCsPopupDivMam()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPopupDivFramevMam()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getRestoreArchiveOption()));
            demoVolumePopup.getRestoreArchiveOption().click();
            CSLogger.info("Clicked on restore archive option");
        } catch (Exception e) {
            CSLogger.debug("Could not click on restore option" + e);
            Assert.fail("Failed to click on restore option" + e);
        }
    }

    /**
     * This data provider returns sheet which contains name of the demo volume
     * folder, image path , image name and name of the volume
     * 
     * @return archiveFileSheet contains the name of the sheet
     */
    @DataProvider(name = "archiveFileTest")
    public Object[] UploadFileTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                archiveFileSheet);
    }

    /**
     * This method initializes required resources to drive the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        csGuiDalogContentIdMam = new CSGuiDalogContentIdMam(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        mamStudioSettingsNode = new MamStudioSettingsNode(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
    }
}
