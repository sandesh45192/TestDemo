/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.util.ArrayList;
import org.cs.csautomation.cs.mam.CSGuiDalogContentIdMam;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class edits the created mam file link which is created from uploaded
 * file under volume folder
 * 
 * @author CSAutomation Team
 *
 */
public class EditMamFileLinkTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private FrameLocators             locator;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private IVolumePopup              demoVolumePopup;
    private String                    editFileLinkSheet = "EditFileLink";
    private CSGuiToolbarHorizontalMam csGuiToolbarHorizontalMam;
    private CSGuiDalogContentIdMam    csGuiDalogContentIdMam;
    private SoftAssert                softAssert;

    /**
     * This method creates the mam file link which is created from uploaded file
     * under volume folder
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the volume name
     * @param imageName contains the name of the image
     * @param createdLink contains the name of the created link
     * @param keywordField contains the name of the keyword field to update
     */
    @Test(priority = 1, dataProvider = "editFileLink")
    public void testEditMamFile(String testFolder, String volumeName,
            String imageName, String createdLink, String keywordField,
            String productTabname) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            WebElement link = traverseUptoCreatedLink(testFolder, volumeName,
                    imageName, createdLink);
            performRightClickOnLink(link);
            editLink(keywordField, productTabname);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method traverses upto created link under volume folder
     * 
     * @param testFolder contains the name of the test folder
     * @param volumeName contains the name of the volume
     * @param imageName contains the name of the image
     * @param createdLink contains the name of the created link
     * @return fileLink which contains name of created link file
     */
    private WebElement traverseUptoCreatedLink(String testFolder,
            String volumeName, String imageName, String createdLink) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                volumeName);
        WebElement createdVolumeName = browserDriver
                .findElement(By.linkText(testFolder));
        createdVolumeName.click();
        WebElement fileLink = browserDriver.findElement(By.xpath(
                "//img[contains(@src,'link')]/following-sibling::span/span[contains(text(),'"
                        + imageName + "')]"));
        return fileLink;
    }

    /**
     * This method edits the keyword field which has created under volume folder
     * 
     * @param keywordField contains the contents to be updated in keyword field
     */
    private void editLink(String keywordField, String productTabname) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getObjectOption()));
        demoVolumePopup.getObjectOption().click();
        CSLogger.info("Clicked on object option");
        clickEditLinkOption();
        editParameters(keywordField, productTabname);
    }

    /**
     * This method clicks on edit link option after clicking on the object
     * option
     */
    private void clickEditLinkOption() {
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
                    .visibilityOf(demoVolumePopup.getEditOption()));
            demoVolumePopup.getEditOption().click();
            CSLogger.info("Clicked on edit option");
        } catch (Exception e) {
            CSLogger.error("Could not click on edit link option");
            softAssert.fail("Could not click on edit link option", e);
        }
    }

    /**
     * This method edit parameters in the keywords field
     * 
     * @param keywordField contains the contents to be updated in the keyword
     *            field
     */
    private void editParameters(String keywordField, String productTabname) {
        mamStudioVolumesNode.traverseUptoMainFrame();
        checkOutImage();
        clickOnTheGivenProductTab(productTabname, waitForReload);
        setDataToKeywordField(keywordField);
    }

    private void clickOnTheGivenProductTab(String productTabname,
            WebDriverWait waitForReload) {
        mamStudioVolumesNode.traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//nobr[contains(text(),'" + productTabname + "')]")));
        WebElement element = browserDriver.findElement(
                By.xpath("//nobr[contains(text(),'" + productTabname + "')]"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * This method sets data to the keyword field
     * 
     * @param keywordField contains the contents to be updated in the keyword
     *            field
     */
    private void setDataToKeywordField(String keywordField) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDalogContentIdMam.getTxtKeywordField()));
        csGuiDalogContentIdMam.getTxtKeywordField().sendKeys(Keys.DELETE);
        csGuiDalogContentIdMam.getTxtKeywordField().clear();
        csGuiDalogContentIdMam.getTxtKeywordField().sendKeys(keywordField);
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckIn()));
        csGuiToolbarHorizontalMam.getBtnCheckIn().click();
        verifyKeywordField(keywordField);
    }

    /**
     * This method verifies if data has set to the keyword field
     * 
     * @param keywordField contains the contents to be updated
     */
    private void verifyKeywordField(String keywordField) {
        try {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnCheckout()));
            csGuiToolbarHorizontalMam.getBtnCheckout().click();
            String updatedKeywordField = waitForReload
                    .until(ExpectedConditions.visibilityOf(
                            csGuiDalogContentIdMam.getTxtKeywordField()))
                    .getText();
            if (updatedKeywordField.equals(keywordField)) {
                CSLogger.info(
                        "Verification of editing link file attribute is successful");
            } else {
                CSLogger.error("Verification failed.");
                Assert.fail("Could not verify updated link file attribute");
            }
        } catch (Exception e) {
            CSLogger.error(
                    "Verifcation of updation of keyword field has failed.");
        }
    }

    /**
     * This method performs the right click on the created link
     * 
     * @param link contains the instance of weblement link
     */
    private void performRightClickOnLink(WebElement link) {
        CSUtility.rightClickTreeNode(waitForReload, link, browserDriver);
    }

    /**
     * This method performs checkout operation
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void checkOutImage() {
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        WebElement getTableData = browserDriver.findElement(By.xpath(
                "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr"));
        ArrayList<WebElement> getFirstRow = (ArrayList<WebElement>) getTableData
                .findElements(By.xpath("//td/a/img"));
        if ((getFirstRow.get(0).getAttribute("src")).endsWith("checkout.gif")) {
            csGuiToolbarHorizontalMam.getBtnCheckout().click();
        } else if (getFirstRow.get(0).getAttribute("src")
                .endsWith("checkin.gif")) {
            CSLogger.info("File already in checkout state");
        }
    }

    @DataProvider(name = "editFileLink")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                editFileLinkSheet);
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
        csGuiToolbarHorizontalMam = new CSGuiToolbarHorizontalMam(
                browserDriver);
        csGuiDalogContentIdMam = new CSGuiDalogContentIdMam(browserDriver);
    }
}
