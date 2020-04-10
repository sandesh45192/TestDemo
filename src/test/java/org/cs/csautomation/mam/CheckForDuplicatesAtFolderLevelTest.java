/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import org.cs.csautomation.cs.mam.CSGuiToolbarHorizontalMam;
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
 * This method checks for duplicates at folder level in test 1 and checks for
 * duplicate files in test 2
 * 
 * @author CSAutomation Team
 *
 */
public class CheckForDuplicatesAtFolderLevelTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private FrameLocators             locator;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private IVolumePopup              demoVolumePopup;
    private CSGuiToolbarHorizontalMam csGuiToolbarHorizontalMam;
    private String                    uploadFileDemoSheet = "UploadFileSheet";
    private ArrayList<String>         list;
    private SoftAssert                softAssert;

    /**
     * This method checks for duplicates at folder level
     * 
     * @param testFolderName contains the name of the test folder
     * @param imagePath contains the path of the image
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
     */
    @Test(priority = 1, dataProvider = "checkFolderLevelDuplicates")
    public void testCheckForDuplicatesAtFolderLevel(String testFolderName,
            String imagePath, String imageName, String volumeName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            imagePath = getFilePath(imagePath);
            csPortalHeader.clkBtnMedia(waitForReload);
            testUploadFile(testFolderName, imagePath, imageName, volumeName);
            traverseToUploadedImage(testFolderName, imageName, volumeName);
            duplicateUploadedImage();
            clkCheckForDuplicatesOption(testFolderName, volumeName);
            verifyPresenceOfDuplicates(testFolderName, volumeName, imageName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Could not run the test case", e);
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
     * This method uploads the file
     * 
     * @param testFolderName contains the name of the test folder
     * @param imagePath contains the path of the image to be uploaded
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
     */
    private void testUploadFile(String testFolderName, String imagePath,
            String imageName, String volumeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolderName));
        CSUtility.rightClickTreeNode(waitForReload, createdVolumeName,
                browserDriver);
        traverseToPopupDivFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getClkUploadNewFile()));
        mamStudioVolumesNode.getClkUploadNewFile().click();
        CSLogger.info("Clicked on the upload new file option");
        try {
            addImageFromDialogWindow(testFolderName, imagePath, imageName,
                    volumeName);
        } catch (Exception e) {
            CSLogger.error("Could not upload file to the test folder");
        }
    }

    /**
     * This method traverses to the pop up div frame
     */
    private void traverseToPopupDivFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
    }

    /**
     * This method adds image from dialog window
     * 
     * @param demoVolumeFolderName contains the name of the volume folder
     * @param imagePath contains the path of the image
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
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
            uploadImage(imagePath);
            clkCloseButton();
        } catch (Exception e) {
            CSLogger.error("could  not assigned image" + e);
            Assert.fail("Image couldnt be assigned.");
        }
    }

    /**
     * This method clicks on the close button after uploading of image to the
     * folder
     */
    private void clkCloseButton() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getClkCloseAfterUpload()));
        mamStudioVolumesNode.getClkCloseAfterUpload().click();
        CSLogger.info("Image assigned successfully.");
    }

    /**
     * This method uploads image to the test folder
     * 
     * @param imagePath contains the path of the image
     */
    private void uploadImage(String imagePath) throws Exception {
        try {
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
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    mamStudioVolumesNode.getUploadButtonImage()));
            mamStudioVolumesNode.getUploadButtonImage().click();
        } catch (Exception e) {
            CSLogger.error("Could not uload an image", e);
            Assert.fail("Could not upload image", e);
        }
    }

    /**
     * This method traverses upto uploaded image
     * 
     * @param testFolderName contains the name of the test folder
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
     */
    private void traverseToUploadedImage(String testFolderName,
            String imageName, String volumeName) {
        WebElement createdVolumeName = traverseToCreatedVolume(testFolderName,
                volumeName);
        createdVolumeName.click();
        WebElement uploadedImage = browserDriver
                .findElement(By.linkText(imageName));
        rightClkUploadedImage(uploadedImage);
    }

    /**
     * This method right clicks on uploaded image
     * 
     * @param uploadedImage contains the instance of uploaded image
     */
    private void rightClkUploadedImage(WebElement uploadedImage) {
        CSUtility.rightClickTreeNode(waitForReload, uploadedImage,
                browserDriver);
    }

    /**
     * This method traverses to created volume
     * 
     * @param testFolderName contains the test folder name
     * @param volumeName contains the name of volume
     * @return instance of created volume name in webelement form
     */
    private WebElement traverseToCreatedVolume(String testFolderName,
            String volumeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolderName));
        return createdVolumeName;
    }

    /**
     * This method performs functionality of duplicating uploaded image
     */
    private void duplicateUploadedImage() {
        clkObjectOption();
        CSLogger.info("clicked on object option");
        clkDuplicate();
    }

    /**
     * This method clicks on object option
     */
    private void clkObjectOption() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getObjectOption()));
        demoVolumePopup.getObjectOption().click();
    }

    /**
     * This method clicks on duplicate option
     */
    private void clkDuplicate() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(
                    ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(locator.getCsPopupDivMam()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPopupDivFramevMam()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getDuplicateOption()));
            demoVolumePopup.getDuplicateOption().click();
            CSLogger.info("Clicked on duplicate option");
        } catch (Exception e) {
            CSLogger.error("Could not click on the duplicate option");
        }
    }

    /**
     * This method clicks checkforduplicates option
     * 
     * @param testFolderName contains the name of the test folder
     * @param volumeName contains volume name
     */
    private void clkCheckForDuplicatesOption(String testFolderName,
            String volumeName) {
        WebElement createdVolume = traverseToCreatedVolume(testFolderName,
                volumeName);
        rightClkCreatedVolume(createdVolume);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getProcessOption()));
        demoVolumePopup.getProcessOption().click();
        CSLogger.info("Clicked process option");
        performClkOnCheckForDuplicates();
    }

    /**
     * This method performs click on check for duplicates option
     */
    private void performClkOnCheckForDuplicates() {
        traverseToFrameInObjectPopup();
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCheckForDuplicatesOption()));
        demoVolumePopup.getCheckForDuplicatesOption().click();
        CSLogger.info("Clicked on check for duplicates option");
    }

    /**
     * This method traverses to frame in pop up for clicking object option
     */
    private void traverseToFrameInObjectPopup() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(locator.getCsPopupDivMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPopupDivFramevMam()));
    }

    /**
     * This method right clicks on created volume
     * 
     * @param createdVolume contains the created volume
     */
    private void rightClkCreatedVolume(WebElement createdVolume) {
        CSUtility.rightClickTreeNode(waitForReload, createdVolume,
                browserDriver);
    }

    /**
     * This method verifies presence of duplicates
     * 
     * @param testFolderName contains the test folder name
     * @param volumeName contains the volume name
     * @param imageName contains image name
     */
    private void verifyPresenceOfDuplicates(String testFolderName,
            String volumeName, String imageName) {
        try {
            traverseToRightSectionPane();
            Integer totalRows = getNumberOfRows();
            getCellValues(totalRows);
            if (list.size() >= 2) {
                String duplicateImage = "Copy of " + imageName;
                if (list.contains(duplicateImage)) {
                    CSLogger.info(
                            "Duplicate image is present in right section pane");
                } else {
                    CSLogger.info(
                            "Duplicate image is not present in right section pane ");
                }
            }
        } catch (Exception e) {
            CSLogger.error("Could not verify presence of duplicates");
            Assert.fail("Verification failed for presence of duplicates");
        }
    }

    /**
     * This method get cell values in string format and adds to the list
     * 
     * @param totalRows contains the instance of total rows as integer
     */
    private void getCellValues(int totalRows) {
        for (int iRowCount = 3; iRowCount <= totalRows; iRowCount++) {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                            + iRowCount + "]/td[5]")));
            String cellValue = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[5]"))
                    .getText();
            list.add(cellValue);
        }
    }

    /**
     * This method returns number of rows
     * 
     * @return totalrows
     */
    private int getNumberOfRows() {
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='hidewrap CSAdminList']")));
        WebElement productInfoTable = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) productInfoTable
                .findElements(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody/tr"));
        Integer totalRows = new Integer(rows.size());
        return totalRows;
    }

    /**
     * This method traverses to right section pane upto main frame
     */
    private void traverseToRightSectionPane() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrame_192MamStudio()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalBodyFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method checks for duplicates for file
     * 
     * @param testFolderName contains the test folder name
     * @param imagePath contains the path of the image
     * @param imageName contains the name of the image
     * @param volumeName contains the name of the volume
     */
    @Test(priority = 2, dataProvider = "checkFolderLevelDuplicates")
    public void testCheckForDuplicatesForFile(String testFolderName,
            String imagePath, String imageName, String volumeName) {
        try {
            WebElement createdVolume = traverseToCreatedVolume(testFolderName,
                    volumeName);
            createdVolume.click();
            CSLogger.info("Clicked on created volume");
            openImageInEditorView(imageName);
            clkCheckForDuplicatesViaTools();
            verifyDuplicateFiles(imageName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This methd opens image in editor view
     * 
     * @param imageName contains name of image
     */
    private void openImageInEditorView(String imageName) {
        WebElement image = browserDriver.findElement(By.linkText(imageName));
        rightClkUploadedImage(image);
        clkObjectOption();
        clkViewAsEditor();
    }

    /**
     * This method clicks viewaseditor option
     */
    private void clkViewAsEditor() {
        traverseToFrameInObjectPopup();
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getViewFolderAsEditor()));
        demoVolumePopup.getViewFolderAsEditor().click();
        CSLogger.info("Image has opened in the editor view");
    }

    /**
     * This method clicks on check for duplicates option via tools option
     */
    private void clkCheckForDuplicatesViaTools() {
        traverseToRightSectionPane();
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontalMam.getBtnMoreOptions()));
        csGuiToolbarHorizontalMam.getBtnMoreOptions().click();
        traverseToPopupDivFrame();
        clkToolsOption();
        clkCheckForDuplicates();
    }

    /**
     * This method clicks checkforduplicates option
     */
    private void clkCheckForDuplicates() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(
                    ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(locator.getCsPopupDivMam()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPopupDivFramevMam()));
            waitForReload.until(ExpectedConditions.visibilityOf(
                    demoVolumePopup.getCheckForDuplicatesOption()));
            demoVolumePopup.getCheckForDuplicatesOption().click();
            CSLogger.info("Clicked on check for duplicates option via tools");
        } catch (Exception e) {
            CSLogger.error("Could not click on check for duplicates option");
            softAssert.fail("Verification failed");
        }
    }

    /**
     * This method clicks on tools option
     */
    private void clkToolsOption() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getToolsOption()));
        demoVolumePopup.getToolsOption().click();
    }

    /**
     * This method verifies duplicates of files
     * 
     * @param imageName contains the name of the image
     */
    private void verifyDuplicateFiles(String imageName) {
        try {
            traverseToRightmostPane();
            Integer totalRows = getNumberOfRows();
            getCellValues(totalRows);
            if (list.size() >= 1) {
                String duplicateFile = "Copy of " + imageName;
                if (list.contains(duplicateFile)) {
                    CSLogger.info(
                            "Duplicate file is present in the rightmost pane");
                } else {
                    CSLogger.info(
                            "Duplicate file is not present in the rightmost pane");
                }
            }
        } catch (Exception e) {
            CSLogger.error("Duplicate file is not present ");
            softAssert.fail("Duplicate file is not present");
        }
    }

    /**
     * This method traverses to rightmost pane
     */
    private void traverseToRightmostPane() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrame_192MamStudio()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalBodyFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getMainFrameMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getItemControlFrame()));
        CSLogger.info("Traversed upto rightmost pane");
    }

    @DataProvider(name = "checkFolderLevelDuplicates")
    public Object[] UploadFileTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                uploadFileDemoSheet);
    }

    /**
     * This method initializes all resources required to drive the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        list = new ArrayList<>();
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        csGuiToolbarHorizontalMam = new CSGuiToolbarHorizontalMam(
                browserDriver);
        softAssert = new SoftAssert();
    }
}
