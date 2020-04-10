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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class deletes the created mam file objects in volumes node
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteMamFileObjectsTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private MamStudioVolumesNode mamStudioVolumesNode;
    private IVolumePopup         demoVolumePopup;
    private String               deleteMamFileObjectsSheet = "DeleteMamFileObjects";
    private SoftAssert           softAssert;

    /**
     * This test case deletes the mam file objects in volumes node
     * 
     * @param testFolderName contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param folderName contains the name of the folder which has created under
     *            volume
     * @param subFolderUnderMainFolder contains the name of the subfolder which
     *            is created under main folder
     * @param imageName contains the name of the image which has assigned to the
     *            volume folder
     */
    @Test(priority = 1, dataProvider = "deleteMamFileObjects")
    public void testDeleteMamFileObjects(String testFolderName,
            String volumeName, String folderName,
            String subFolderUnderMainFolder, String imageName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            createVolume(waitForReload, testFolderName, true, volumeName);
            createNewFile(waitForReload, testFolderName, true, volumeName,
                    subFolderUnderMainFolder, folderName);
            deleteMamFile(testFolderName, volumeName, folderName,
                    subFolderUnderMainFolder, imageName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method creates volume under volumes node
     * 
     * @param waitForReload waits for the element to reload
     * @param testFolderName contains the name of the test folder
     * @param chooseOptionForCreateVolume contains boolean value to choose for
     *            creating volume
     * @param volumeName contains the name of the volume
     */
    private void createVolume(WebDriverWait waitForReload,
            String testFolderName, boolean chooseOptionForCreateVolume,
            String volumeName) {
        try {
            clickVolumesNode();
            WebElement nameOfVolume = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, volumeName);
            waitForReload.until(ExpectedConditions.visibilityOf(nameOfVolume));
            CSUtility.rightClickTreeNode(waitForReload, nameOfVolume,
                    browserDriver);
            demoVolumePopup.selectPopupDivMenu(waitForReload,
                    demoVolumePopup.getCreateNewFolder(), browserDriver);
            demoVolumePopup.enterValueInDialogueMamStudio(waitForReload,
                    testFolderName);
            demoVolumePopup.askBoxWindowOperationMamStudio(waitForReload,
                    chooseOptionForCreateVolume, browserDriver);
            CSLogger.info("Volume created successfully.");
        } catch (Exception e) {
            CSLogger.error("Failed to create volume. " + e);
        }
    }

    /**
     * This method clicks traverses and clicks on volumes node
     */
    private void clickVolumesNode() {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
    }

    /**
     * This method creates new file under demo folder
     * 
     * @param waitForReload waits for an element to reload
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param chooseOptionToSelectImage contains boolean value to choose for
     *            creating file
     * @param volumeName contains the name of the folder
     * @param subFolderUnderMainFolder contains the name of the subfolder which
     *            has created under main volume folder
     * @param folderName contains volume folder name
     */
    private void createNewFile(WebDriverWait waitForReload,
            String demoVolumeFolderName, boolean chooseOptionToSelectImage,
            String volumeName, String subFolderUnderMainFolder,
            String folderName) {
        try {
            clickVolumesNode();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            rightClkCreatedVolume(demoVolumeFolderName);
            clkParentFolderToAddImageDialogWindow(waitForReload, volumeName,
                    subFolderUnderMainFolder, folderName);
            addImageToVolumeFolder(waitForReload, chooseOptionToSelectImage);
        } catch (Exception e) {
            CSLogger.error("New file is not created." + e);
        }
    }

    /**
     * This method right clicks on created volume
     * 
     * @param demoVolumeFolderName contains name of volume
     */
    private void rightClkCreatedVolume(String demoVolumeFolderName) {
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(demoVolumeFolderName));
        CSUtility.rightClickTreeNode(waitForReload, createdVolumeName,
                browserDriver);
    }

    /**
     * This method clicks on parent folder after opening dialog window to add
     * image to the volume folder
     * 
     * @param waitForReload waits for an element to reload
     * @param volumeName contains the name of the volume
     * @param subfolderUnderDemoFolder contains the name of the subfolder which
     *            has created under demo volume folder
     * @param folderName contains the name of the demo folder
     */
    private void clkParentFolderToAddImageDialogWindow(
            WebDriverWait waitForReload, String volumeName,
            String subfolderUnderDemoFolder, String folderName) {
        try {
            clickCreateNewFile();
            traverseUptoLeftFrame();
            WebElement nameOfVolume = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, volumeName);
            waitForReload.until(ExpectedConditions.visibilityOf(nameOfVolume));
            nameOfVolume.click();
            WebElement nameOfFolder = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, folderName);
            waitForReload.until(ExpectedConditions.visibilityOf(nameOfFolder));
            nameOfFolder.click();
            WebElement nameOfSubFolder = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload,
                            subfolderUnderDemoFolder);
            waitForReload
                    .until(ExpectedConditions.visibilityOf(nameOfSubFolder));
            nameOfSubFolder.click();
            CSUtility.tempMethodForThreadSleep(1000);
            nameOfSubFolder.click();
            CSLogger.info("Clicked on parent folder in Dialog window");
        } catch (Exception e) {
            CSLogger.error(
                    "Could not click on parent folder in Dialog window" + e);
        }
    }

    /**
     * This method clicks on create new file option
     */
    private void clickCreateNewFile() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCreateNewFile()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(demoVolumePopup.getCreateNewFile()));
        demoVolumePopup.getCreateNewFile().click();
        CSLogger.info("Clicked on create new file option");
    }

    /**
     * This method traverses upto left frame in dialog window while adding image
     * to the folder
     */
    private void traverseUptoLeftFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getLeftSecFrameInDialogWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getfoldersLeftFrame()));
    }

    /**
     * This method traverses upto center frame after opening dialog window to
     * add image
     */
    private void traverseUptoCenterFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getLeftSecFrameInDialogWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFoldersCenterFrame()));
    }

    /**
     * This method adds image to the volume folder
     * 
     * @param waitForReload waits for an element to reload
     * @param chooseOptionToSelectImage contains boolean value to choose if
     *            image should get added to the folder or not
     */
    private void addImageToVolumeFolder(WebDriverWait waitForReload,
            boolean chooseOptionToSelectImage) {
        try {
            traverseUptoCenterFrame();
            selectImageToAdd();
            demoVolumePopup.askBoxWindowOperation(waitForReload,
                    chooseOptionToSelectImage, browserDriver);
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("Image is added to the demo volume folder");
        } catch (Exception e) {
            CSLogger.error("Image is Not added to the demo volume folder", e);
            Assert.fail("Could not add image to the folder", e);
        }
    }

    /**
     * This method finds the instance of the image which has to be added under
     * demo folder
     */
    private void selectImageToAdd() {
        try {
            WebElement imageToSelectUnderVolumeFolder = browserDriver
                    .findElement(By.xpath(
                            "(//div[@class='CSMamFileThumb']/div/img[contains(@src,'pixel.gif')])[1]"));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(imageToSelectUnderVolumeFolder));
            imageToSelectUnderVolumeFolder.click();
            CSLogger.info(
                    "Image has selected successfully to add to volume folder");
        } catch (Exception e) {
            CSLogger.error("Failed to find image to add to the volume folder",
                    e);
        }
    }

    /**
     * This method deletes mam file from the volume folder
     * 
     * @param testFolderName contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param folderName contains the name of the folder
     * @param subFolderUnderMainFolder contains the name of the subfolder
     * @param imageName contains the name of the image
     */
    private void deleteMamFile(String testFolderName, String volumeName,
            String folderName, String subFolderUnderMainFolder,
            String imageName) {
        CSUtility.tempMethodForThreadSleep(2000);
        rightClickCreatedImage(volumeName, imageName);
        verifyDeletedMamFile(testFolderName, volumeName, imageName);
    }

    /**
     * This method right clicks on created image under volume folder
     * 
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     */
    private void rightClickCreatedImage(String volumeName, String imageName) {
        clickVolumesNode();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdImage = browserDriver
                .findElement(By.linkText(imageName));
        CSUtility.rightClickTreeNode(waitForReload, createdImage,
                browserDriver);
        deleteFile();
    }

    /**
     * This file deletes the assigned mam file to the volume folder
     */
    private void deleteFile() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getObjectOption()));
        demoVolumePopup.getObjectOption().click();
        CSLogger.info("Clicked on object option");
        clickDeleteOption();
    }

    /**
     * This method clicks on delete option after clicking on the object optin of
     * image
     */
    private void clickDeleteOption() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPopupDivMam()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPopupDivFramevMam()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getCsPopupDelete()));
            demoVolumePopup.getCsPopupDelete().click();
            handleAlertAfterDelete();
        } catch (Exception e) {
            CSLogger.debug("Failed to click on delte option" + e);
            softAssert.fail("Failed to click on delete option");
        }
    }

    /**
     * This method handles alert box after clicking delete option
     */
    private void handleAlertAfterDelete() {
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
    }

    /**
     * This method verifies if mam file has deleted from the volume folder or
     * not
     * 
     * @param testFolderName contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     */
    private void verifyDeletedMamFile(String testFolderName, String volumeName,
            String imageName) {
        try {
            clickVolumesNode();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement folderName = browserDriver
                    .findElement(By.linkText(testFolderName));
            folderName.click();
            WebElement image = browserDriver
                    .findElement(By.linkText(imageName));
            if (image.getText().equals(imageName)) {
                CSLogger.error("Failed to delete mam file from created folder");
                Assert.fail("Could not delete mam file");
            }
        } catch (Exception e) {
            CSLogger.error(
                    "Test case verified.Mam file has successfully deleted");
        }
    }

    /**
     * This data provider returns the sheet which contains the testfolder,
     * volume name , foldername,subfolderUnderMainFolder and image Name
     * 
     * @return deleteMamFileObjectSheet
     */
    @DataProvider(name = "deleteMamFileObjects")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                deleteMamFileObjectsSheet);
    }

    /**
     * This method initializes the resources for executing the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        softAssert = new SoftAssert();
    }
}
