/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import org.cs.csautomation.cs.mam.CSGuiDalogContentIdMam;
import org.cs.csautomation.cs.mam.CSGuiToolbarHorizontalMam;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioSettingsNode;
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
 * This class verifies inheritance of class in demo volume folder and child
 * folder created under it,Deletes class from inherited section , Clicks on add
 * again option to add deleted class and Deletes class from parent folder
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyInheritanceOfClassTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private FrameLocators             locator;
    private IVolumePopup              demoVolumePopup;
    private String                    verifySectionsSheet = "VerifySections";
    private CSGuiDalogContentIdMam    csGuiDalogContentIdMam;
    private CSGuiToolbarHorizontalMam csGuiToolbarHorizontalMam;
    private MamStudioSettingsNode     mamStudioSettingsNode;

    /**
     * This test case performs the functionality of verifying the inherited
     * section of classes
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the demo volume
     *            folder which is been created for inheritance
     */
    @Test(priority = 1, dataProvider = "verifySections")
    public void testVerifyInheritedSection(String demoVolumeFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 50);
            csPortalHeader.clkBtnMedia(waitForReload);
            createClassForChildFolder(testFolder, assignClassToSubfolder,
                    volumeName);
            createDemoVolumeFolder(testFolder, volumeName);
            verifyInheritanceOfClass(testFolder, className, volumeName);
            createChildFolderUnderTestFolder(testFolder, folderForInheritance,
                    className, volumeName);
        } catch (Exception e) {
            CSLogger.debug("Automation error in testVerifyCategoryTreeCheckbox",
                    e);
            Assert.fail(
                    "Automation error in testVerifyCategoryTreeCheckbox" + e);
        }
    }

    /**
     * This method creates child folder under already created demo volume folder
     * 
     * @param testFolder contains the name of the test demo volume folder
     * @param demoFolderForInheritance contains name of the demo volume folder
     * @param className contains the name of the class
     */
    private void createChildFolderUnderTestFolder(String testFolder,
            String folderForInheritance, String className, String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement mainDemoFolder = browserDriver
                    .findElement(By.linkText(testFolder));
            CSUtility.rightClickTreeNode(waitForReload, mainDemoFolder,
                    browserDriver);
            demoVolumePopup.selectPopupDivMenu(waitForReload,
                    demoVolumePopup.getCreateNewFolder(), browserDriver);
            demoVolumePopup.enterValueInDialogueMamStudio(waitForReload,
                    folderForInheritance);
            demoVolumePopup.askBoxWindowOperationMamStudio(waitForReload, true,
                    browserDriver);
            verifyAssignedClassInChildFolder(testFolder, folderForInheritance,
                    className, volumeName);
        } catch (Exception e) {
            CSLogger.error("Child creation and verification under "
                    + "already created" + " demo volume folder failed", e);
        }
    }

    /**
     * This method verifies if class has been assigned to the child folder or
     * not
     * 
     * @param testFolder contains the name of the test folder
     * @param demoFolderForInheritance contains the name of the demo volume
     *            folder
     * @param className contains the name of the class
     */
    private void verifyAssignedClassInChildFolder(String testFolder,
            String folderForInheritance, String className, String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement mainDemoFolder = browserDriver
                    .findElement(By.linkText(testFolder));
            mainDemoFolder.click();
            WebElement subFolderInTestFolder = browserDriver
                    .findElement(By.linkText(folderForInheritance));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(subFolderInTestFolder).perform();
            traverseUptoMainFrame();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
            csGuiToolbarHorizontalMam.getBtnCheckout().click();
            CSLogger.info("Clicked on checkout button..");
            WebElement verifyClass = browserDriver
                    .findElement(By.linkText(className));
            Assert.assertEquals(className, verifyClass.getText());
            CSLogger.info("Class assigned in child folder");
        } catch (Exception e) {
            CSLogger.error("Verification of assigned class failed", e);
            Assert.fail("Class has not been assigned", e);
        }
    }

    /**
     * This method creates demo volume test folder
     * 
     * @param testFolder contains the name of the test folder to be created
     *            under demo
     */
    private void createDemoVolumeFolder(String testFolder, String volumeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        WebElement nameOfVolume = mamStudioVolumesNode
                .expandNodesByVolumesName(waitForReload, volumeName);
        CSUtility.rightClickTreeNode(waitForReload, nameOfVolume,
                browserDriver);
        demoVolumePopup.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getCreateNewFolder(), browserDriver);
        demoVolumePopup.enterValueInDialogueMamStudio(waitForReload,
                testFolder);
        demoVolumePopup.askBoxWindowOperationMamStudio(waitForReload, true,
                browserDriver);
        CSLogger.info("Demo volume folder created successfully.");
        checkoutNewlyCreatedFolder(testFolder, volumeName);
    }

    /**
     * This method performs checkout of newly created demo volume folder
     * 
     * @param testFolder contains the name of the test folder
     */
    private void checkoutNewlyCreatedFolder(String testFolder,
            String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement createdVolumeFolder = browserDriver
                    .findElement(By.linkText(testFolder));
            CSUtility.rightClickTreeNode(waitForReload, createdVolumeFolder,
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
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiToolbarHorizontalMam.getCheckoutOption()));
            csGuiToolbarHorizontalMam.getCheckoutOption().click();

            CSLogger.info("Clicked on checkout option");
            handleAskBoxForCheckout();
        } catch (Exception e) {
            CSLogger.error(
                    "Failed to click on checkout option for demo volume folder"
                            + e);
        }
    }

    /**
     * This method handles ask box after checkout
     */
    private void handleAskBoxForCheckout() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//input[3]")))
                .click();
        CSLogger.info(
                "Clicked on recursive button of ask box window of checkout");
    }

    /**
     * This method verifies if class has successfully assigned to the test
     * folder or not
     * 
     * @param testFolder contains the name of the test folder
     * @param className contains the class name
     */
    private void verifyInheritanceOfClass(String testFolder, String className,
            String volumeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolder));
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(createdVolumeName).perform();
        CSLogger.info("Double clicked on demo volume folder");
        addClassToDemoVolumeFolder(testFolder, className);
        verifyAssignedClass(testFolder, className, volumeName);
    }

    /**
     * This method verifies if class has assigned to the test demo volume folder
     * or not
     * 
     * @param testFolder contains the name of the test demo volume folder
     * @param className contains the name of the class
     */
    private void verifyAssignedClass(String testFolder, String className,
            String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement createdVolumeName = browserDriver
                    .findElement(By.linkText(testFolder));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(createdVolumeName).perform();
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiDalogContentIdMam.getBtnPropertiesMam()));
            csGuiDalogContentIdMam.getBtnPropertiesMam().click();
            CSLogger.info("Clicked on properties tab of demo volume folder");
            traverseUptoMainFrame();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
            csGuiToolbarHorizontalMam.getBtnCheckout().click();
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement assignedClass = browserDriver.findElement(By.xpath(
                    "//div[@name='CSGuiVisibilityComponent']/div/div[2]/div/tab"
                            + "le/tbody/tr[2]/td[2]/span[contains(text(),'"
                            + className + "')]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, assignedClass);
            Assert.assertEquals(className, assignedClass.getText());
        } catch (Exception e) {
            CSLogger.error("Failed to verify assigned class" + e);
        }
    }

    /**
     * This method adds class to already created demo volume folder by clicking
     * plus button
     * 
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     */
    private void addClassToDemoVolumeFolder(String testFolder,
            String className) {
        try {
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiDalogContentIdMam.getBtnPropertiesMam()));
            csGuiDalogContentIdMam.getBtnPropertiesMam().click();
            CSLogger.info("Clicked on properties tab of demo volume folder");
            clickOnPlusButtonOfClasses(testFolder, className);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrame_frmf3dMam()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getLeftRecordFrameMam()));
            WebElement nameOfClass = browserDriver
                    .findElement(By.linkText(className));
            waitForReload.until(ExpectedConditions.visibilityOf(nameOfClass));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(nameOfClass).perform();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            waitForReload.until(ExpectedConditions.visibilityOf(
                    demoVolumePopup.getBtnCsGuiModalDialogOkButton()));
            demoVolumePopup.getBtnCsGuiModalDialogOkButton().click();
            traverseUptoMainFrame();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckIn()));
            csGuiToolbarHorizontalMam.getBtnCheckIn().click();
            CSUtility.tempMethodForThreadSleep(5000);
            CSLogger.info("Clicked on check in button");
            CSLogger.info("Class has added successfully to demo volume folder");
        } catch (Exception e) {
            CSLogger.error("Failed to add class under demo volume folder", e);
        }
    }

    /**
     * This method clicks on plus button of classes to add class to the demo
     * volume folder
     * 
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     */
    private void clickOnPlusButtonOfClasses(String testFolder,
            String className) {
        try {
            traverseUptoMainFrame();
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiDalogContentIdMam.getClkPlusToAddClassMam()));
            CSUtility.scrollUpOrDownToElement(
                    csGuiDalogContentIdMam.getClkPlusToAddClassMam(),
                    browserDriver);
            CSUtility.tempMethodForThreadSleep(5000);
            csGuiDalogContentIdMam.getClkPlusToAddClassMam().click();
            CSLogger.info("Clicked on plus button to add class");
        } catch (Exception e) {
            CSLogger.debug("Failed to click on plus button to add class" + e);
        }
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
     * This test case verifies selected section of classes
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the demo volume
     *            folder under test folder for inheritance
     * @param assignClassToSubfolder contains the name of the class to be
     *            assigned to the subfolder
     */
    @Test(priority = 2, dataProvider = "verifySections")
    public void testVerifySelectedSection(String testFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            addClassToChildFolder(testFolder, folderForInheritance,
                    assignClassToSubfolder, volumeName);
            verifyAddedClassToChildFolder(testFolder, folderForInheritance,
                    assignClassToSubfolder, volumeName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed");
        }
    }

    /**
     * This method creates class to assign to child folder
     * 
     * @param testFolder contians the name of the test folder
     * @param assignClassToSubfolder contains the name of the class to be
     *            assigned to the child folder
     */
    private void createClassForChildFolder(String testFolder,
            String assignClassToSubfolder, String volumeName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
            mamStudioSettingsNode.getSettingsNodeMam().click();
            CSLogger.info("Clicked on Settings node");
            CSUtility.scrollUpOrDownToElement(
                    mamStudioSettingsNode.getClassNode(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.rightClickTreeNode(waitForReload,
                    mamStudioSettingsNode.getClassNode(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getCsPopupDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getClkCreateNew()));
            mamStudioSettingsNode.getClkCreateNew().click();
            CSLogger.info("Clicked on create new button.");
            FrameLocators iframeLocatorsInstance = FrameLocators
                    .getIframeLocators(browserDriver);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    iframeLocatorsInstance.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            iframeLocatorsInstance.getCsPortalWindowFrame()));
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    demoVolumePopup.getTxtCsGuiModalDialogFormName()));
            CSUtility.tempMethodForThreadSleep(1000);
            demoVolumePopup.getTxtCsGuiModalDialogFormName()
                    .sendKeys(assignClassToSubfolder);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    demoVolumePopup.getBtnCsGuiModalDialogOkButton()));
            demoVolumePopup.getBtnCsGuiModalDialogOkButton().click();
            CSLogger.info("Clicked on ok button to create class");
        } catch (Exception e) {
            CSLogger.debug("Could not created class ", e);
        }
    }

    /**
     * This method adds class to the subfolder under testfolder by clicking on
     * plus button
     * 
     * @param testFolder contains the name of the test folder
     * @param demoFolderForInheritance contains the name of the demo volume
     *            folder which is created under test folder
     * @param classToAssignToSubfolder
     */
    private void addClassToChildFolder(String testFolder,
            String folderForInheritance, String assignClassToSubfolder,
            String volumeName) {
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckIn()));
            csGuiToolbarHorizontalMam.getBtnCheckIn().click();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
            csGuiToolbarHorizontalMam.getBtnCheckout().click();
            clickOnPlusButtonOfClasses(testFolder, assignClassToSubfolder);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrame_frmf3dMam()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getLeftRecordFrameMam()));
            WebElement nameOfClass = browserDriver
                    .findElement(By.linkText(assignClassToSubfolder));
            waitForReload.until(ExpectedConditions.visibilityOf(nameOfClass));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(nameOfClass).perform();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            waitForReload.until(ExpectedConditions.visibilityOf(
                    demoVolumePopup.getBtnCsGuiModalDialogOkButton()));
            demoVolumePopup.getBtnCsGuiModalDialogOkButton().click();
            traverseUptoMainFrame();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckIn()));
            csGuiToolbarHorizontalMam.getBtnCheckIn().click();
            CSLogger.info("Clicked on check in button");
            CSLogger.info("Class has added successfully to child folder");
        } catch (Exception e) {
            CSLogger.debug("Could not add class to child folder", e);
        }
    }

    /**
     * This method verifes added classes to the child folder
     * 
     * @param testFolder contains the name of the test folder
     * @param demoFolderForInheritance contains the name of the demo volume
     *            folder
     * @param assignClassToSubfolder contians name of the class to be assigned
     *            to the created folder
     */
    private void verifyAddedClassToChildFolder(String testFolder,
            String folderForInheritance, String assignClassToSubfolder,
            String volumeName) {
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
            csGuiToolbarHorizontalMam.getBtnCheckout().click();
            traverseUptoMainFrame();
            WebElement assignedClass = browserDriver
                    .findElement(By.linkText(assignClassToSubfolder));
            Assert.assertEquals(assignClassToSubfolder,
                    assignedClass.getText());
            CSLogger.info("verified added class to child folder");
        } catch (Exception e) {
            CSLogger.error(
                    "Could not verify the added class to the child folder");
        }
    }

    /**
     * This method verifies deleted section of the demo volume folder
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     *            name
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the demo volume
     *            folder for inheritance
     * @param assignClassToSubfolder contains the name of the class that has
     *            been assigned to the subfolders
     */
    @Test(priority = 3, dataProvider = "verifySections")
    public void testVerifyDeletedSection(String demoVolumeFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            clickOnInheritedSection();
            removeClassFromInheritedSection(className);
            verifyRemovedClassDeletedSection(demoVolumeFolderName, testFolder,
                    className, folderForInheritance, assignClassToSubfolder,
                    volumeName);
        } catch (Exception e) {
            CSLogger.debug("Test failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method verifies if class has deleted from parent folder
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     *            name
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the demo folder for
     *            inheritance
     * @param assignClassToSubfolder contains the name of the class to be
     *            assigned to the subfolder
     */
    private void verifyDeletedClassFromParentFolder(String demoVolumeFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement mainDemoFolder = browserDriver
                    .findElement(By.linkText(testFolder));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(mainDemoFolder).perform();
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiDalogContentIdMam.getBtnPropertiesMam()));
            csGuiDalogContentIdMam.getBtnPropertiesMam().click();
            CSLogger.info("Clicked on properties tab of demo volume folder");
            traverseUptoMainFrame();
            int countOfDeletedClass = browserDriver.findElements(By.xpath(
                    "//div[@class='sectioncontent']/div/table/tbody/tr[2]/td[2]/span[contains(text(),'"
                            + className + "')]"))
                    .size();
            if (countOfDeletedClass == 0) {
                CSLogger.info("Class deleted successfully");
            } else {
                CSLogger.error("Failed to delete class");
            }
        } catch (Exception e) {
            CSLogger.info("Test case verified successfully", e);
        }
    }

    /**
     * This method deletes class from parent folder
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the demo volume
     *            folder for inheritance
     * @param assignClassToSubfolder contains the name of the class that has
     *            assigned to the subfolder
     */
    private void deleteClassFromParentFolder(String demoVolumeFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement mainDemoFolder = browserDriver
                    .findElement(By.linkText(testFolder));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(mainDemoFolder).perform();
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiDalogContentIdMam.getBtnPropertiesMam()));
            csGuiDalogContentIdMam.getBtnPropertiesMam().click();
            CSLogger.info("Clicked on properties tab of demo volume folder");
            traverseUptoMainFrame();
            WebElement assignedClass = browserDriver.findElement(By.xpath(
                    "//div[@name='CSGuiVisibilityComponent']/div/div[2]/div/table/tbody/tr[2]/td[2]/span[contains(text(),'"
                            + className + "')]"));
            waitForReload.until(ExpectedConditions.visibilityOf(assignedClass));
            assignedClass.click();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(
                    ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getClkRemove()));
            demoVolumePopup.getClkRemove().click();
            CSUtility.getAlertBox(waitForReload, browserDriver).accept();
            CSLogger.info("Class removed.");
        } catch (Exception e) {
            CSLogger.error("Failed to delete class from parent folder" + e);
        }
    }

    /**
     * This method verifies class has removed from deleted section
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     *            name
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the folder created
     *            for verifying inheritance
     * @param assignClassToSubfolder contains the name of the class which has
     *            assigned to subfolder
     */
    private void verifyRemovedClassDeletedSection(String demoVolumeFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement mainDemoFolder = browserDriver
                    .findElement(By.linkText(testFolder));
            mainDemoFolder.click();
            WebElement subFolderInTestFolder = browserDriver
                    .findElement(By.linkText(folderForInheritance));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(subFolderInTestFolder).perform();
            traverseUptoMainFrame();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDalogContentIdMam.getClkDelete()));
            csGuiDalogContentIdMam.getClkDelete().click();
            WebElement nameOfAssignedClass = browserDriver.findElement(By.xpath(
                    "//div[@class='sectioncontent']/div/table/tbody/tr[2]/td[2]/span[contains(text(),'"
                            + className + "')]"));
            waitForReload.until(
                    ExpectedConditions.visibilityOf(nameOfAssignedClass));
            Assert.assertEquals(className, nameOfAssignedClass.getText());
        } catch (Exception e) {
            CSLogger.error("Could not remove classes from the deleted section ",
                    e);
            Assert.fail("Could not remove classes from the deleted section ",
                    e);
        }
    }

    /**
     * This method removes class from inherited section of the demo volume
     * folder
     * 
     * @param className contains the name of the class
     */
    private void removeClassFromInheritedSection(String className) {
        try {
            WebElement nameOfAssignedClass = browserDriver.findElement(By.xpath(
                    "//div[@class='sectioncontent']/div/div/table/tbody/tr[2]/td[2]/span[contains(text(),'"
                            + className + "')]"));
            waitForReload.until(
                    ExpectedConditions.visibilityOf(nameOfAssignedClass));
            nameOfAssignedClass.click();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(
                    ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getClkRemove()));
            demoVolumePopup.getClkRemove().click();
            CSLogger.info("Class removed.");
        } catch (Exception e) {
            CSLogger.error("Failed to remove class from inherited section", e);
        }
    }

    /**
     * This method clicks on the inherited section of the class
     */
    private void clickOnInheritedSection() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDalogContentIdMam.getSectionInherited()));
        csGuiDalogContentIdMam.getSectionInherited().click();
        CSLogger.info("Clicked on inherited section of class");
    }

    /**
     * This method clicks and verifies add again option of class in deleted
     * section
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the demo folder for
     *            inheritance
     * @param assignClassToSubfolder contains the name of the class to be
     *            assigned to the subfolder
     */
    @Test(priority = 4, dataProvider = "verifySections")
    public void testVerifyAddAgainSection(String testFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            clickOnAddAgainOption(testFolderName, testFolder, className,
                    folderForInheritance, assignClassToSubfolder, volumeName);
            verifyAddAgainOption(testFolderName, testFolder, className,
                    folderForInheritance, assignClassToSubfolder, volumeName);
            deleteClassFromParentFolder(testFolderName, testFolder, className,
                    folderForInheritance, assignClassToSubfolder, volumeName);
            verifyDeletedClassFromParentFolder(testFolderName, testFolder,
                    className, folderForInheritance, assignClassToSubfolder,
                    volumeName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method verifies add again option of the deleted class
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the demo folder for
     *            inheritance
     * @param assignClassToSubfolder contains the name of the class to be
     *            assigned to the subfolder
     */
    private void verifyAddAgainOption(String demoVolumeFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement mainDemoFolder = browserDriver
                    .findElement(By.linkText(testFolder));
            mainDemoFolder.click();
            CSLogger.info("Clicked on main demo volume folder");
            WebElement subFolderInTestFolder = browserDriver
                    .findElement(By.linkText(folderForInheritance));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(subFolderInTestFolder).perform();
            traverseUptoMainFrame();
            clickOnInheritedSection();
            int countAssignedClass = browserDriver.findElements(By.xpath(
                    "//table[@class='CSGuiTable']/tbody/tr/td[2]/span[contains(text(),'"
                            + className + "')]"))
                    .size();
            if (countAssignedClass == 1) {
                CSLogger.info("Verified add again option");
            } else {
                CSLogger.error("Failed to verify add again option");
            }
        } catch (Exception e) {
            CSLogger.debug("Could not verify add again option.", e);
            Assert.fail("Could not verify add again option ", e);
        }
    }

    /**
     * This method clicks on add again option to add class to the demo volume
     * folder
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param testFolder contains the name of the test folder
     * @param className contains the name of the class
     * @param demoFolderForInheritance contains the name of the demo volume
     *            folder for inheritance
     * @param assignClassToSubfolder contains the name of the class to be
     *            assigned to the subfolder
     */
    private void clickOnAddAgainOption(String demoVolumeFolderName,
            String testFolder, String className, String folderForInheritance,
            String assignClassToSubfolder, String volumeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            mamStudioVolumesNode.getBtnMamVolumesNode().click();
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    volumeName);
            WebElement mainDemoFolder = browserDriver
                    .findElement(By.linkText(testFolder));
            mainDemoFolder.click();
            WebElement subFolderInTestFolder = browserDriver
                    .findElement(By.linkText(folderForInheritance));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(subFolderInTestFolder).perform();
            traverseUptoMainFrame();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDalogContentIdMam.getClkDelete()));
            csGuiDalogContentIdMam.getClkDelete().click();
            WebElement assignedClass = browserDriver.findElement(By.xpath(
                    "//div[@name='CSGuiVisibilityComponent']/div[3]/div[2]/div/table/tbody/tr[2]/td[2]/span[contains(text(),'"
                            + className + "')]"));
            assignedClass.click();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(
                    ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(demoVolumePopup.getClkAddAgain()));
            demoVolumePopup.getClkAddAgain().click();
            CSLogger.info("Clicked on add again option");
        } catch (Exception e) {
            CSLogger.error("Failed to click on add again option", e);
        }
    }

    /**
     * this data provider returns sheet data to the test method of class which
     * contains demo volume folder name ,test folder to be created which is also
     * demo volume folder ,class name and demo volume folder name for
     * inheritance
     * 
     * @return verifySectionsSheet
     */
    @DataProvider(name = "verifySections")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                verifySectionsSheet);
    }

    /**
     * this method initializes the resources for executing the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        mamStudioSettingsNode = new MamStudioSettingsNode(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csGuiDalogContentIdMam = new CSGuiDalogContentIdMam(browserDriver);
        csGuiToolbarHorizontalMam = new CSGuiToolbarHorizontalMam(
                browserDriver);
    }
}
