
package org.cs.csautomation.mam;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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
 * This class downloads assigned image to folder in the computers directory
 * 
 * @author CSAutomation Team
 *
 */
public class DownloadFromFileWorksTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private MamStudioVolumesNode mamStudioVolumesNode;
    private IVolumePopup         demoVolumePopup;
    private String               downloadFileDataSheet = "DownloadFileData";

    /**
     * This test case downloads the image assigned to the folder into the
     * computer's directory
     * 
     * @param folderName contains the name of the folder
     * @param volumeName contains the name of the volume
     * @param testFolder contains the name of the test folder
     * @param subFolder contains the name of the subfolder created under folder
     * @param imageName contains the name of the image
     */
    @Test(priority = 1, dataProvider = "downloadFile")
    public void testDownloadFromFileWorks(String folderName, String volumeName,
            String testFolder, String subFolder, String imageName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            createFolder(waitForReload, folderName, true, volumeName);
            createNewFile(waitForReload, folderName, true, volumeName,
                    testFolder, subFolder, imageName);
            downloadFile(waitForReload, folderName, volumeName);
            verifyIfFileHasDownloaded(folderName, imageName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed ", e);
            Assert.fail("Test case failed");
        }
    }

    /**
     * This method created the volume under demo folder of volumes tab
     * 
     * @param waitForReload waits for element to reload
     * @param demoVolumeFolderName contains the name of folder which is taken
     *            from excel sheet
     * @param chooseOptionForCreateVolume contains boolean value for clicking on
     *            cancel and ok buttons
     * @param contains the name of the volume created
     */
    private void createFolder(WebDriverWait waitForReload,
            String testFolderName, boolean chooseOptionForCreateVolume,
            String volumeName) {
        try {
            clkVolumesNode();
            WebElement nameOfVolume = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, volumeName);
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
     * This method traverses upto volumes node
     */
    private void clkVolumesNode() {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        CSLogger.info("Traversed upto volumes node");
    }

    /**
     * This method downloads the file under demo volume folder in zip format
     * into the system
     * 
     * @param waitForReload waits for element to reload
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param volumeName contains the name of the created volume
     */
    private void downloadFile(WebDriverWait waitForReload,
            String testFolderName, String volumeName) {
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
        CSLogger.info("Right clicked on demo volume folder");
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getProcessOption()));
        demoVolumePopup.getProcessOption().click();
        CSLogger.info(
                "Clicked on process after right clicking on the demo volume"
                        + " folder");
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(locator.getCsPopupDivMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPopupDivFramevMam()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getDownloadAsZipOption()));
        demoVolumePopup.getDownloadAsZipOption().click();
        CSUtility.tempMethodForThreadSleep(3000);
        CSLogger.info("Clicked on download as zip option");
    }

    /**
     * Thsi method creates new file under already created folder
     * 
     * @param waitForReload waits for an element to reload
     * @param testFolderName contains the name of the test folder
     * @param chooseOptionToSelectImage keeps boolean value to select ok or
     *            cancel for creating new file
     * @param volumeName contains the name of the volume
     * @param testFolder contains the name of the test folder
     * @param subFolder contains the name of the subfolder
     * @param imageName contains the name of the image
     */
    private void createNewFile(WebDriverWait waitForReload,
            String testFolderName, boolean chooseOptionToSelectImage,
            String volumeName, String testFolder, String subFolder,
            String imageName) {
        try {
            clkVolumesNode();
            WebElement createdFolderName = browserDriver
                    .findElement(By.linkText(testFolderName));
            CSUtility.rightClickTreeNode(waitForReload, createdFolderName,
                    browserDriver);
            clkParentFolderToAddImageDialogWindow(waitForReload, volumeName,
                    testFolder, subFolder);
            addImageToFolder(waitForReload, chooseOptionToSelectImage,
                    imageName);
        } catch (Exception e) {
            CSLogger.error("New file is not created." + e);
        }
    }

    /**
     * This method clicks on parent folder from which we are selecting image to
     * add to the demo volume folder
     * 
     * @param waitForReload waits for loading elements
     * @param volumeName contains the name of the volume
     * @param testFolder contains the name of the test folder
     * @param contains the name of the subfolder
     */
    private void clkParentFolderToAddImageDialogWindow(
            WebDriverWait waitForReload, String volumeName, String testFolder,
            String subFolder) {
        try {
            clkCreateNewFile();
            traverseUptoLeftFrame();
            WebElement nameOfVolume = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, volumeName);
            waitForReload.until(ExpectedConditions.visibilityOf(nameOfVolume));
            nameOfVolume.click();
            WebElement nameOfFolder = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, testFolder);
            nameOfFolder.click();
            WebElement nameOfSubFolder = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, subFolder);
            nameOfSubFolder.click();
            CSLogger.info("Clicked on parent folder in Dialog window");
        } catch (Exception e) {
            CSLogger.error(
                    "Could not click on parent folder in Dialog window" + e);
        }
    }

    /**
     * This method traverses upto left frame
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
     * This method clicks on create new file option
     */
    private void clkCreateNewFile() {
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
    }

    /**
     * This method adds image to the newly created demo volume folder
     * 
     * @param chooseOptionToSelectImage contains boolean value true/false for
     *            choosing ok or cancel for adding image to demo volume folder
     * @param waitForReload waits for the visibility of element
     * @param imageName contains the name of the image
     */
    private void addImageToFolder(WebDriverWait waitForReload,
            boolean chooseOptionToSelectImage, String imageName) {
        try {
            traverseUptoCenterFrame();
            WebElement imageToSelectUnderVolumeFolder = browserDriver
                    .findElement(By.xpath(
                            "//span[contains(text(),'" + imageName + "')]"));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(imageToSelectUnderVolumeFolder));
            imageToSelectUnderVolumeFolder.click();
            CSUtility.tempMethodForThreadSleep(3000);
            demoVolumePopup.askBoxWindowOperation(waitForReload,
                    chooseOptionToSelectImage, browserDriver);
            CSUtility.tempMethodForThreadSleep(4000);
            CSLogger.info("Image is added to the demo volume folder");
        } catch (Exception e) {
            CSLogger.error("Image is Not added to the demo volume folder");
        }
    }

    /**
     * This method traverses upto center frame
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
     * This method verifies if image has successfully downloaded in the
     * downloads folder
     * 
     * @param folderName contains the name of the folder in which image has
     *            assigned
     * @param imageName contains the name of the image to be assigned
     */
    private void verifyIfFileHasDownloaded(String folderName,
            String imageName) {
        folderName = System.getProperty("user.home") + "\\Downloads" + "\\"
                + folderName + ".zip";
        try {
            File file = new File(folderName);
            if (file.exists()) {
                ZipFile zipFile = new ZipFile(folderName);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                    if (zipEntry.equals(imageName))
                        CSLogger.info("File is present");
                }
                zipFile.close();
            } else
                CSLogger.info("File is not present");
        } catch (Exception e) {
            CSLogger.debug("Verification of presence of folders  failed:" + e);
            Assert.fail("File is not present");
        }
    }

    /**
     * This data provider returns the sheet download file data sheet which
     * contains folder name,volume name ,subfolder name,test folder name and
     * image name
     * 
     * @return downloadFileDataSheet
     */
    @DataProvider(name = "downloadFile")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                downloadFileDataSheet);
    }

    /**
     * This method initializes all the resources required to run the test cases
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
