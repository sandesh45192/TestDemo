/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import org.cs.csautomation.cs.mam.CSGuiDalogContentIdMam;
import org.cs.csautomation.cs.mam.CSGuiToolbarHorizontalMam;
import org.cs.csautomation.cs.mam.IVolumePopup;
/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to handle the test cases related to the
 * creation of MAM file objects
 * 
 * @author CSAutomation Team
 *
 */
public class CreateAndEditMamFileObjectsTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private FrameLocators             locator;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private IVolumePopup              demoVolumePopup;
    private String                    demoFolderSheetName = "CreateVolumeFolder";
    private CSGuiToolbarHorizontalMam csGuiToolbarHorizontalMam;
    private CSGuiDalogContentIdMam    csGuiDalogContentIdMam;

    /**
     * This method created the volume under Demo folder
     * 
     * @param testFolderName contains the folder name from excel sheet to
     *            created volume folder
     * @param label contains the label to be changed in existing property
     * @param volumeName contains the name of the volume
     * @param folderName contains the name of the folder
     **/
    @Test(priority = 1, dataProvider = "createDemoVolumetData")
    public void testCreateMamFileObjects(String testFolderName, String label,
            String volumeName, String folderName,
            String subFolderUnderMainFolder) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            createVolume(waitForReload, testFolderName, false, volumeName);
            createVolume(waitForReload, testFolderName, true, volumeName);
            verifyCreatedVolume(waitForReload, testFolderName, volumeName);
            createNewFile(waitForReload, testFolderName, false, volumeName,
                    subFolderUnderMainFolder, folderName);
            createNewFile(waitForReload, testFolderName, true, volumeName,
                    subFolderUnderMainFolder, folderName);
        } catch (Exception e) {
            CSLogger.debug("Eror" + e);
            Assert.fail("Test case failed", e);
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
     */
    private void createVolume(WebDriverWait waitForReload,
            String testFolderName, boolean chooseOptionForCreateVolume,
            String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            WebElement nameOfVolume = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, volumeName);
            rightClickVolumeNode(nameOfVolume);
            handleOkOfPopup(testFolderName, chooseOptionForCreateVolume);
        } catch (Exception e) {
            CSLogger.debug("Failed to create volume. " + e);
        }
    }

    /**
     * This method handles ok of pop up and creates volume
     * 
     * @param testFolderName contains the name of the folder
     * @param chooseOptionForCreateVolume contains boolean value to opt for
     *            cancel and ok options
     */
    private void handleOkOfPopup(String testFolderName,
            boolean chooseOptionForCreateVolume) {
        demoVolumePopup.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getCreateNewFolder(), browserDriver);
        demoVolumePopup.enterValueInDialogueMamStudio(waitForReload,
                testFolderName);
        demoVolumePopup.askBoxWindowOperationMamStudio(waitForReload,
                chooseOptionForCreateVolume, browserDriver);
        CSLogger.info("Volume created successfully.");
    }

    /**
     * This method right clicks on volume node
     * 
     * @param nameOfVolume contains name of volume
     */
    private void rightClickVolumeNode(WebElement nameOfVolume) {
        waitForReload.until(ExpectedConditions.visibilityOf(nameOfVolume));
        CSUtility.rightClickTreeNode(waitForReload, nameOfVolume,
                browserDriver);
    }

    /**
     * This method verifies if the created volume is present or not.
     * 
     * @param waitForReload waits for element to load
     * @param demoVolumeFolderName contains name of demo volume folder
     */
    private void verifyCreatedVolume(WebDriverWait waitForReload,
            String testFolderName, String volumeName) {
        try {
            traverseUptoCreatedVolume(volumeName);
            WebElement createdVolumeName = browserDriver
                    .findElement(By.linkText(testFolderName));
            String volumeNameString = createdVolumeName.getText();
            if (volumeNameString.equalsIgnoreCase(testFolderName))
                CSLogger.info("Volume " + volumeName
                        + " is present under Demo folder.");
            else {
                CSLogger.info("Volume " + volumeName + " is not present.");
                Assert.fail("Volume" + volumeName + " is not present");
            }
        } catch (Exception e) {
            CSLogger.debug(
                    "Verification failed . Created Volume is not present under "
                            + "Demo volume folder",
                    e);
        }
    }

    private void traverseUptoCreatedVolume(String volumeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
    }

    /**
     * this method created new file under newly created demo volume folder
     * 
     * @param waitForReload waits for the element to load
     * @param demoVolumeFolderName contains the name of demo volume folder
     */
    private void createNewFile(WebDriverWait waitForReload,
            String demoVolumeFolderName, boolean chooseOptionToSelectImage,
            String volumeName, String subFolderUnderMainFolder,
            String folderName) {
        try {
            traverseUptoCreatedVolume(volumeName);
            WebElement createdVolumeName = browserDriver
                    .findElement(By.linkText(demoVolumeFolderName));
            CSUtility.rightClickTreeNode(waitForReload, createdVolumeName,
                    browserDriver);
            clkParentFolderToAddImageDialogWindow(waitForReload, volumeName,
                    subFolderUnderMainFolder, folderName);
            addImageToDemoVolumeFolder(waitForReload,
                    chooseOptionToSelectImage);
        } catch (Exception e) {
            CSLogger.error("New file is not created." + e);
        }
    }

    /**
     * this method clicks on parent folder from which we are selecting image to
     * add to the demo volume folder
     * 
     * @param waitForReload waits for loading elements
     */
    private void clkParentFolderToAddImageDialogWindow(
            WebDriverWait waitForReload, String volumeName,
            String subfolderUnderDemoFolder, String folderName) {
        try {
            clkCreateNewFile();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getLeftSecFrameInDialogWindow()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getfoldersLeftFrame()));
            WebElement nameOfVolume = mamStudioVolumesNode
                    .expandNodesByVolumesName(waitForReload, volumeName);
            waitForReload.until(ExpectedConditions.visibilityOf(nameOfVolume));
            nameOfVolume.click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    folderName);
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    subfolderUnderDemoFolder);
            CSLogger.info("Clicked on parent folder in Dialog window");
        } catch (Exception e) {
            CSLogger.debug(
                    "Could not click on parent folder in Dialog window" + e);
        }
    }

    /**
     * This method clicks on create new file option
     * 
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
        CSLogger.info("Clicked on create new file option");
    }

    /**
     * this method adds image to the newly created demo volume folder
     * 
     * @param chooseOptionToSelectImage contains boolean value true/false for
     *            choosing ok or cancel for adding image to demo volume folder
     * @param waitForReload waits for the visibility of element
     */
    private void addImageToDemoVolumeFolder(WebDriverWait waitForReload,
            boolean chooseOptionToSelectImage) {
        try {
            addImage();
            CSUtility.tempMethodForThreadSleep(3000);
            demoVolumePopup.askBoxWindowOperation(waitForReload,
                    chooseOptionToSelectImage, browserDriver);
            CSUtility.tempMethodForThreadSleep(4000);
            CSLogger.info("Image is added to the demo volume folder");
        } catch (Exception e) {
            CSLogger.error("Image is Not added to the demo volume folder");
        }
    }

    private void addImage() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getLeftSecFrameInDialogWindow()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFoldersCenterFrame()));
            WebElement imageToSelectUnderVolumeFolder = browserDriver
                    .findElement(By.xpath(
                            "(//div[@class='CSMamFileThumb']/div/img[contains(@src,'pixel.gif')])[1]"));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(imageToSelectUnderVolumeFolder));
            imageToSelectUnderVolumeFolder.click();
            CSLogger.info("Clicked on the image to add under volume folder");
        } catch (Exception e) {
            CSLogger.error("Could not click on image" + e);
            Assert.fail("Failed to assign image to the volume folder" + e);
        }
    }

    /**
     * This test edits and verifies if provided label to existing label has
     * changed or not
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param label contains the name of the label
     */
    @Test(priority = 2, dataProvider = "createDemoVolumetData")
    public void testEditMamFileObjects(String testFolderName, String label,
            String volumeName, String folderName,
            String subFolderUnderMainFolder) {
        try {
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.switchToDefaultFrame(browserDriver);
            traverseUptoCreatedVolume(volumeName);
            WebElement createdVolumeName = browserDriver
                    .findElement(By.linkText(testFolderName));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    createdVolumeName);
            createdVolumeName.click();
            waitForReload.until(ExpectedConditions.visibilityOf(
                    mamStudioVolumesNode.getCreatedFileUnderDemoFolder()));
            mamStudioVolumesNode.getCreatedFileUnderDemoFolder().click();
            traverseUptoMainFrame();
            clkCheckOut();
            clkPropertiesTab();
            sendLabelToEdit(label);
            clkCheckIn();
            verifyChangedLabel(label);
        } catch (Exception e) {
            CSLogger.debug("Editing failed", e);
            Assert.fail("Test case failed", e);
        }
    }

    private void sendLabelToEdit(String label) {
        Actions actions = new Actions(browserDriver);
        WebElement elementLocator = browserDriver
                .findElement(By.xpath("//input[contains(@id,'MamfileLabel')]"));
        actions.doubleClick(elementLocator).perform();
        elementLocator.clear();
        elementLocator.sendKeys(label);
    }

    /**
     * This method performs check in
     */
    private void clkCheckIn() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckIn()));
        csGuiToolbarHorizontalMam.getBtnCheckIn().click();
        CSLogger.info("Clicked on CheckIn button ");
        CSUtility.tempMethodForThreadSleep(4000);
    }

    /**
     * This method clicks on properties tab
     */
    private void clkPropertiesTab() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDalogContentIdMam.getBtnPropertiesMam()));
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                csGuiDalogContentIdMam.getBtnPropertiesMam()));
        csGuiDalogContentIdMam.getBtnPropertiesMam().click();
        CSLogger.info("Clicked on properties tab");
    }

    /**
     * This method clicks on checkout button
     */
    private void clkCheckOut() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
        csGuiToolbarHorizontalMam.getBtnCheckout().click();
        CSLogger.info("Clicked on Checkout button ");
    }

    /**
     * This method traverses upto main frame in right section pane
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
     * This method verifies if label sent from csv has changed successfully or
     * not
     * 
     * @param label contains label name that has changed
     */
    private void verifyChangedLabel(String label) {
        try {
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            WebElement checkIfPresent = browserDriver
                    .findElement(By.linkText(label));
            Assert.assertEquals(checkIfPresent.getText(), label);
            CSLogger.info("label has successfully changed");
        } catch (Exception e) {
            CSLogger.error("Verification failed." + e);
            Assert.fail("label verification failed");
        }
    }

    /**
     * this data provider returns sheet data to the test method of class which
     * contains the name of the volume to be created under demo folder
     * 
     * @return demoFolderSheetName
     */
    @DataProvider(name = "createDemoVolumetData")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                demoFolderSheetName);
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
        csGuiToolbarHorizontalMam = new CSGuiToolbarHorizontalMam(
                browserDriver);
        csGuiDalogContentIdMam = new CSGuiDalogContentIdMam(browserDriver);
    }
}
