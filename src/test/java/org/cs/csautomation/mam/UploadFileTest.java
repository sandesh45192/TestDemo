/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
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

/**
 * This class uploads file from computer's local directory to the demo volume
 * folder
 * 
 * @author CSAutomation team
 *
 */
public class UploadFileTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private MamStudioVolumesNode mamStudioVolumesNode;
    private IVolumePopup         demoVolumePopup;
    private String               uploadFileDemoSheet = "UploadFileSheet";

    /**
     * This test uploads file from local directory to the demo volume folder
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param imagePath contains the path of the image
     * @param imageName contains the name of the image
     */
    @Test(priority = 1, dataProvider = "uploadFileTest")
    public void testUploadfileTest(String testFolderName, String imagePath,
            String imageName, String volumeName) throws Exception {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            imagePath = getFilePath(imagePath);
            csPortalHeader.clkBtnMedia(waitForReload);
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            CSLogger.info("Clcked on volumes node");
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement createdVolumeName = browserDriver
                    .findElement(By.linkText(testFolderName));
            CSUtility.scrollUpOrDownToElement(createdVolumeName, browserDriver);
            CSUtility.rightClickTreeNode(waitForReload, createdVolumeName,
                    browserDriver);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getCsPopupDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getClkUploadNewFile()));
            mamStudioVolumesNode.getClkUploadNewFile().click();
            CSLogger.info("Clicked on the upload new file option");
            addImageFromDialogWindow(testFolderName, imagePath, imageName,
                    volumeName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method get the file path of current system
     * 
     * @param filePath String object contains path from sheet
     * @return path String contains current file path
     */
    private String getFilePath(String filePath){
        String currentPath = System.getProperty("user.dir");
        String path=currentPath+filePath;
        return path;
    }

    /**
     * This method uploads image to the dialog window
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param imagePath contains the path of the image wherever the image has
     *            stored in computer drive
     * @param imageName contains the name of the image
     * 
     */
    private void addImageFromDialogWindow(String demoVolumeFolderName,
            String imagePath, String imageName, String volumeName)
            throws Exception {
        CSUtility.switchToDefaultFrame(browserDriver);
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    mamStudioVolumesNode.getBrowseFileToUpload()));
            mamStudioVolumesNode.getBrowseFileToUpload().click();
            addImage(imagePath);

            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    mamStudioVolumesNode.getUploadButtonImage()));
            mamStudioVolumesNode.getUploadButtonImage().click();
            CSUtility.tempMethodForThreadSleep(2000);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    mamStudioVolumesNode.getClkCloseAfterUpload()));
            mamStudioVolumesNode.getClkCloseAfterUpload().click();
            CSLogger.info("Image assigned successfully.");
            verifyAssignedImage(demoVolumeFolderName, imageName, volumeName);
        } catch (Exception e) {
            CSLogger.error("could  not assigned image" + e);
            Assert.fail("Image couldnt be assigned." + e);
        }
    }

    /**
     * This method adds image to the clipboard and then to the test folder by
     * clicking enter
     * 
     * @param imagePath contains path of the image
     * 
     */
    private void addImage(String imagePath) throws Exception {
        StringSelection select = new StringSelection(imagePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(select,
                null);
        CSUtility.tempMethodForThreadSleep(2000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        CSUtility.tempMethodForThreadSleep(2000);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        CSLogger.info("Image added to the test folder");
    }

    /**
     * This test method verifies if image has assigned successfully to the demo
     * volume folder
     * 
     * @param imageName contains name of image
     */
    private void verifyAssignedImage(String testFolderName, String imageName,
            String volumeName) {
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
            if (uploadedImage != null) {
                CSLogger.info("Image uploaded successfully");
            } else {
                CSLogger.info("Image upload unsuccessful.");
                Assert.fail("Image upload failed.");
            }
        } catch (Exception e) {
            CSLogger.error("Could not verify assigned image", e);
            Assert.fail("Image upload failed.");
        }
    }

    /**
     * This data provider returns sheet which contains name of demo volume
     * folder and the demo volume file which has been created under demo volume
     * folder
     * 
     * @return demoFolderSheetName contains the name of the sheet
     */
    @DataProvider(name = "uploadFileTest")
    public Object[] UploadFileTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                uploadFileDemoSheet);
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
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
    }
}
