/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.util.List;
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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class performs drag and drop of class to the mam file
 * 
 * @author CSAutomation Team
 *
 */
public class DragDropClassToMamFileTest extends AbstractTest {

    private FrameLocators          locator;
    private MamStudioVolumesNode   mamStudioVolumesNode;
    private CSPortalHeader         csPortalHeader;
    private WebDriverWait          waitForReload;
    private MamStudioSettingsNode  mamStudioSettingsNode;
    private IVolumePopup           demoVolumePopup;
    private String                 dragDropClassSheetMam = "DragDropClassToMam";
    private CSGuiDalogContentIdMam csGuiDalogContentIdMam;

    /**
     * This test case performs drag and drop class to mam file.
     * 
     * @param className contains the name of the class
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param classForReplace contains the name of the class for replace
     * @param volume name contains the name of the volume
     */
    @Test(priority = 1, dataProvider = "DragDropClassToMam")
    public void testDragDropClassToMamFile(String className,
            String testFolderName, String classForReplace, String volumeName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            dragAndDropClassToMamFolder(className, testFolderName, volumeName);
            performClickForDragDrop(waitForReload, "Cancel");
            dragAndDropClassToMamFolder(className, testFolderName, volumeName);
            performClickForDragDrop(waitForReload, "Extend");
            verifyExtendedClass(waitForReload, className, testFolderName,
                    volumeName);
            createClassForReplaceFunction(classForReplace);
            dragAndDropClassToMamFolderReplace(testFolderName, classForReplace,
                    volumeName);
            performClickForDragDrop(waitForReload, "Replace");
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method assigns class to demo volume folder which implements extend
     * and cancel functionalities while assigning class via drag and drop
     * 
     * @param className contains the name of the class to be dragged and dropped
     *            on demo volume folder
     * @param demoVolumeFolderName contains the name of the demo volume folder
     *            to which class should be assigned
     * 
     * @param contains the name of the volume
     */
    private void dragAndDropClassToMamFolder(String className,
            String testFolderName, String volumeName) {
        try {
            WebElement createdVolumeName = clkCreatedVolume(testFolderName,
                    volumeName);
            WebElement nameOfClass = clkClassToDragAndDrop(className);
            performDragDrop(nameOfClass, createdVolumeName);
        } catch (Exception e) {
            CSLogger.error(
                    "Couldnt drag and drop class to demo volume folder" + e);
        }
    }

    private WebElement clkClassToDragAndDrop(String className) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
        mamStudioSettingsNode.getSettingsNodeMam().click();
        mamStudioSettingsNode.clkSettingsAttributeNode(waitForReload);
        mamStudioSettingsNode.clkClassNode(waitForReload);
        WebElement nameOfClass = browserDriver
                .findElement(By.linkText(className));
        nameOfClass.click();
        CSLogger.info("Clicked on class folder.");
        return nameOfClass;
    }

    /**
     * This method clicks on created volume
     * 
     * @param testFolderName contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @return createdVolumeName
     */
    private WebElement clkCreatedVolume(String testFolderName,
            String volumeName) {
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
        return createdVolumeName;
    }

    /**
     * This method assigns class to demo volume folder via drag and drop
     * 
     * @param demoVolumeFolderName contains the name of the demo volume folder
     * @param classForReplace contains the name of the class to be replaced
     * @param volumeName contains the name of the volume
     */
    private void dragAndDropClassToMamFolderReplace(String testFolderName,
            String classForReplace, String volumeName) {
        try {
            WebElement createdVolumeName = clkCreatedVolume(testFolderName,
                    volumeName);
            CSLogger.info("Clicked on  volume folder");
            clkClassNode();
            WebElement nameOfClass = browserDriver
                    .findElement(By.linkText(classForReplace));
            nameOfClass.click();
            CSLogger.info("Clicked on class folder.");
            performDragDrop(nameOfClass, createdVolumeName);
        } catch (Exception e) {
            CSLogger.error(
                    "Drag drop functionality with replace option is failed"
                            + e);
        }
    }

    /**
     * This method performs drag drop of class onto product
     * 
     * @param nameOfClass contains the name of the class
     * @param createdVolumeName contains the name of the volume
     */
    private void performDragDrop(WebElement nameOfClass,
            WebElement createdVolumeName) {
        Actions action = new Actions(browserDriver);
        CSUtility.scrollUpOrDownToElement(nameOfClass, browserDriver);
        Action dragDrop = action.dragAndDrop(nameOfClass, createdVolumeName)
                .build();
        dragDrop.perform();
    }

    /**
     * This method clicks on class node
     */
    private void clkClassNode() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
        mamStudioSettingsNode.getSettingsNodeMam().click();
        mamStudioSettingsNode.clkSettingsAttributeNode(waitForReload);
        mamStudioSettingsNode.clkClassNode(waitForReload);
        CSLogger.info("Clicked on class node");
    }

    /**
     * This method creates class for implementing replace function.Separate
     * class need to be created to assign to the demo volume folder using
     * replace option
     * 
     * @param classForReplace contains the name of the class which needs to be
     *            replaced in already created demo volume folder
     */
    private void createClassForReplaceFunction(String classForReplace) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
            mamStudioSettingsNode.getSettingsNodeMam().click();
            CSLogger.info("Clicked on Settings node");
            clkCreateNew();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    demoVolumePopup.getTxtCsGuiModalDialogFormName()));
            CSUtility.tempMethodForThreadSleep(1000);
            demoVolumePopup.getTxtCsGuiModalDialogFormName()
                    .sendKeys(classForReplace);
            clkOkToCreateClass();
        } catch (Exception e) {
            CSLogger.debug(
                    "Failed to create class for implementing replace function",
                    e);
        }
    }

    /**
     * This method clicks on ok to create class
     */
    private void clkOkToCreateClass() {
        waitForReload.until(ExpectedConditions.visibilityOf(
                demoVolumePopup.getBtnCsGuiModalDialogOkButton()));
        demoVolumePopup.getBtnCsGuiModalDialogOkButton().click();
        CSLogger.info("Clicked on ok button to create class");
    }

    /**
     * This method clicks on create new option
     */
    private void clkCreateNew() {
        CSUtility.rightClickTreeNode(waitForReload,
                mamStudioSettingsNode.getClassNode(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioSettingsNode.getClkCreateNew()));
        mamStudioSettingsNode.getClkCreateNew().click();
        CSLogger.info("Clicked on create new button.");
    }

    /**
     * This method performs click on either Extend or Cancel or Replace
     * 
     * @param waitForReload waits for an element to reload
     * @param selectOption contains string either extend or cancel which needs
     *            to be clicked
     */
    private void performClickForDragDrop(WebDriverWait waitForReload,
            String selectOption) {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPortalWindowFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalWindowFrame()));
            if (selectOption.equals("Cancel")) {
                waitForReload.until(ExpectedConditions.visibilityOf(
                        demoVolumePopup.getBtnCsGuiModalDialogCancelButton()));
                demoVolumePopup.getBtnCsGuiModalDialogCancelButton().click();
            } else {
                if (selectOption.equals("Extend")) {
                    waitForReload.until(ExpectedConditions
                            .visibilityOf(demoVolumePopup.getBtnExtendMam()));
                    demoVolumePopup.getBtnExtendMam().click();
                    CSLogger.info("Clicked on Extend button");
                } else {
                    waitForReload.until(ExpectedConditions
                            .visibilityOf(demoVolumePopup.getBtnReplaceMam()));
                    demoVolumePopup.getBtnReplaceMam().click();
                }
            }
        } catch (Exception e) {
            CSLogger.error("Could  not perform click");
        }
    }

    /**
     * This method verifies if class has been extended to demo volume folder or
     * not
     * 
     * @param waitForReload waits for an element to reload
     * @param className contains the name of the class
     * @param test folder name contains the folder name
     * @param volume name contains the name of the volume
     */
    private void verifyExtendedClass(WebDriverWait waitForReload,
            String className, String testFolderName, String volumeName) {
        try {
            openVolumeInEditorForm(testFolderName, volumeName);
            traverseUptoMainFrame();
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiDalogContentIdMam.getBtnPropertiesMam()));
            csGuiDalogContentIdMam.getBtnPropertiesMam().click();
            CSUtility.tempMethodForThreadSleep(5000);
            List<WebElement> rows = browserDriver.findElements(By
                    .xpath("(//table[@class='CSGuiTable'])[1]/tbody/tr/td[2]"));
            int flag = 0;
            String[] linkText = new String[rows.size()];
            int temp = 0;
            for (WebElement path : rows) {
                if (!rows.equals("Name"))
                    linkText[temp] = path.getText();
                if (linkText[temp].contains("Name"))
                    linkText[temp] = linkText[temp].replace("Name", "");
                if (linkText[temp].equals(className)) {
                    flag = 1;
                } else
                    flag = 0;
                temp++;
            }
            if (flag == 1)
                CSLogger.info(className + " is present");
            else
                CSLogger.info("Class is not present");
        } catch (Exception e) {
            CSLogger.error("Could not verify the extended class", e);
            Assert.fail("Could not verify extended class", e);
        }
    }

    private void openVolumeInEditorForm(String testFolderName,
            String volumeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolderName));
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(createdVolumeName).build().perform();
        CSLogger.info("Performed double click on volume node");
    }

    /**
     * This method traverses all frames upto main right section pane in mam
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
     * This data provider returns sheet which contains class name , demo volume
     * folder name , class and volume nameto be replaced
     * 
     * @return dragDropClassSheetMam contains the name of the sheet
     */
    @DataProvider(name = "DragDropClassToMam")
    public Object[] CreateAttributeFolderData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                dragDropClassSheetMam);
    }

    /**
     * This test initializes the resources required to drive the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        mamStudioSettingsNode = new MamStudioSettingsNode(browserDriver);
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        new CSGuiToolbarHorizontalMam(browserDriver);
        csGuiDalogContentIdMam = new CSGuiDalogContentIdMam(browserDriver);
    }
}
