/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.util.Iterator;
import java.util.List;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This method creates link of mam file under volume folder and verifies if the
 * link has been created or not
 * 
 * @author CSAutomation Team
 *
 */
public class LinkCreationOfMamFileTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private MamStudioVolumesNode mamStudioVolumesNode;
    private IVolumePopup         demoVolumePopup;
    private String               mamFileLinkCreationSheet = "MamFileLinkCreation";

    /**
     * This method performs link creation of mam file which has assigned under
     * volume folder
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     * @param createdLink contains the name of created link
     */
    @Test(priority = 1, dataProvider = "mamFileLinkCreation")
    public void testMamFileLinkCreation(String testFolder, String volumeName,
            String imageName, String createdLink) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            traverseUptoCreatedImage(testFolder, volumeName, imageName);
            WebElement uploadedImage = browserDriver
                    .findElement(By.linkText(imageName));
            performRightClickOnCreatedImage(uploadedImage);
            createLink();
            verifyCreatedLink(testFolder, volumeName, imageName, createdLink);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method performs the right click on the uploaded image under volume
     * folder
     * 
     * @param uploadedImage contains the name of the uploaded image
     */
    private void performRightClickOnCreatedImage(WebElement uploadedImage) {
        CSUtility.rightClickTreeNode(waitForReload, uploadedImage,
                browserDriver);
    }

    /**
     * This method traverses up to created image to create the link of the image
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     * @return
     */
    private void traverseUptoCreatedImage(String testFolder, String volumeName,
            String imageName) {
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
     * This method clicks on object option to create link
     */
    private void createLink() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getObjectOption()));
        demoVolumePopup.getObjectOption().click();
        CSLogger.info("Clicked on object option");
        clickCreateLinkOption();
    }

    /**
     * This method clicks on the create link option after clicking on object
     * option in the pop up
     */
    private void clickCreateLinkOption() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(locator.getCsPopupDivMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPopupDivFramevMam()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupCreateLink()));
        demoVolumePopup.getCsPopupCreateLink().click();
        CSLogger.info("Clicked on Create Link option");
    }

    /**
     * This method verifies if the link has created or not
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     * @param createdLink contains the name of the created link
     */
    private void verifyCreatedLink(String testFolder, String volumeName,
            String imageName, String createdLink) {
        try {
            boolean status = false;
            traverseUptoCreatedImage(testFolder, volumeName, imageName);
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            WebElement createdVolumeName = browserDriver
                    .findElement(By.linkText(testFolder));
            createdVolumeName.click();
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            WebElement listElement = null;
            List<WebElement> list = browserDriver.findElements(By.xpath(
                    "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
            for (Iterator<WebElement> iterator = list.iterator(); iterator
                    .hasNext();) {
                listElement = (WebElement) iterator.next();
                if (listElement.getText().equals(createdLink)) {
                    status = true;
                    break;
                }
            }
            if (status == true) {
                CSLogger.info("Link created successfully");
            } else {
                CSLogger.error("Verification failed.");
                Assert.fail("Created Link could not be verifed");
            }
        } catch (Exception e) {
            CSLogger.debug("Verification failed.", e);
            Assert.fail("Created Link could not be verifed", e);
        }
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param productName String containing name of product (can be parent
     *            product or duplicated product)
     * @return product Id
     */
    public String getCreatedLinkId(String createdImageLink) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(browserDriver.findElement(By.xpath(
                "(//span[contains(text(),'" + createdImageLink + "')])[2]")))
                .perform();
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))));
        return browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
    }

    /**
     * This data provider returns sheet which contains testFolder name,volume
     * name under which test folder has created and image name which is created
     * under volume and created link name
     * 
     * @return mamMetadataAndVersionSheet
     */
    @DataProvider(name = "mamFileLinkCreation")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                mamFileLinkCreationSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */
    @BeforeClass
    public void initializeResources() {
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
    }
}