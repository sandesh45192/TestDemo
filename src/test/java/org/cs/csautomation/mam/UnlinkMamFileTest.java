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

/**
 * This class performs the functionality of unlinking mam file which is created
 * under volume folder
 * 
 * @author CSAutomation Team
 *
 */
public class UnlinkMamFileTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private MamStudioVolumesNode mamStudioVolumesNode;
    private IVolumePopup         demoVolumePopup;
    private String               unlinkMamFileSheet = "UnlinkMamFile";

    /**
     * This method unlinks the mam file which has created link under volume
     * folder
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param createdLink contains the name of the created link
     * @param unlinkedFileName contains the name of link after unlink operation
     */
    @Test(priority = 1, dataProvider = "unlinkMamFile")
    public void testUnlinkMamFile(String testFolder, String volumeName,
            String createdLink, String unlinkedFileName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            WebElement link = traverseUptoCreatedLink(testFolder, volumeName,
                    createdLink, unlinkedFileName);
            performRightClickOnLink(link);
            removeLink();
            verifyFileUnlinking(testFolder, volumeName, createdLink,
                    unlinkedFileName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method traverses up to created link to unlink the link under volume
     * folder
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param createdLink contains the name of the created link
     * @param unlinkedFileName contains the name of unlinked file
     * @return fileLink
     */
    private WebElement traverseUptoCreatedLink(String testFolder,
            String volumeName, String createdLink, String unlinkedFileName) {
        traverseUptoCreatedVolume(testFolder, volumeName);
        WebElement fileLink = browserDriver.findElement(By.xpath(
                "//img[contains(@src,'link')]/following-sibling::span/span[contains(text(),'"
                        + createdLink + "')]"));
        return fileLink;
    }

    /**
     * This method performs right click on the created link to unlink
     * 
     * @param link contains the instance of web element link
     */
    private void performRightClickOnLink(WebElement link) {
        CSUtility.rightClickTreeNode(waitForReload, link, browserDriver);
    }

    /**
     * This method removes the link from created link file
     */
    private void removeLink() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getObjectOption()));
        demoVolumePopup.getObjectOption().click();
        CSLogger.info("Clicked on object option");
        clickRemoveLinkOption();
    }
    
    /**
     * This method clicks on the remove link option after clicking on object
     * option in the pop up
     */
    
    private void clickRemoveLinkOption() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(locator.getCsPopupDivMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPopupDivFramevMam()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupRemoveLink()));
        demoVolumePopup.getCsPopupRemoveLink().click();
        CSLogger.info("Clicked on Remove Link option");
    }
    
    
    /**
     * This method traverses up to created volume
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     */
    private void traverseUptoCreatedVolume(String testFolder,
            String volumeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolder));
        createdVolumeName.click();
    }

    /**
     * This method verifies if file has unlinked successfully or not
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param createdLink contains the name of the created link
     * @param unlinkedFileName contains the name of the file which generates
     *            after unlinking
     */
    private void verifyFileUnlinking(String testFolder, String volumeName,
            String createdLink, String unlinkedFileName) {
        try {
            traverseUptoCreatedVolume(testFolder, volumeName);
            WebElement link = browserDriver
                    .findElement(By.linkText(unlinkedFileName));
            Assert.assertEquals(unlinkedFileName, link.getText());
        } catch (Exception e) {
            CSLogger.error("Verification failed",e);
            Assert.fail("Verification failed", e);
        }
    }

    /**
     * This data provider returns the sheet data which contains test folder,
     * volume name,created link and unlinked file name
     * 
     * @return unlinkMamFileSheet
     */
    @DataProvider(name = "unlinkMamFile")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                unlinkMamFileSheet);
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
    }
}
