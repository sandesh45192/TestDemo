/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioSettingsNode;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class creates and verifies attribute folder
 * 
 * @author CSAutomation team
 *
 */
public class CreateAttributeFolderTest extends AbstractTest {

    private WebDriverWait         waitForReload;
    private MamStudioSettingsNode mamStudioSettingsNode;
    private IVolumePopup          demoVolumePopup;
    private String                createAttributeFolderSheetName = "CreateAttributeFolderMam";
    private CSPortalHeader        csPortalHeader;

    /**
     * This test case creates attribute folder under Attributes tab
     * 
     * @param attributeFolderName
     */
    @Test(priority = 1, dataProvider = "CreateAttributeFolder")
    public void testCreateAttributeFolder(String attributeFolderName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            performFunctionality(waitForReload, false, attributeFolderName);
            performFunctionality(waitForReload, true, attributeFolderName);
            verifyAddedAttributeFolder(attributeFolderName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method creates the new attribute folder under Attributes tab
     * 
     * @param waitForReload waits for element to reload
     * @param pressOk contains boolean value
     * @param attributeFolderName contains the name of the attribute folder
     */
    private void performFunctionality(WebDriverWait waitForReload,
            boolean pressOk, String attributeFolderName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
            mamStudioSettingsNode.getSettingsNodeMam().click();
            CSLogger.info("Clicked on settings node");
            ceateAttributeFolder(attributeFolderName, pressOk);
        } catch (Exception e) {
            CSLogger.debug("Attribute folder creation failed.");
        }
    }

    /**
     * This method creates attribute folder
     * 
     * @param attributeFolderName contains name of attribute folder
     * @param pressOk contains boolean value
     */
    private void ceateAttributeFolder(String attributeFolderName,
            boolean pressOk) {
        waitForReload.until(ExpectedConditions.visibilityOf(
                mamStudioSettingsNode.getBtnSettingsAttributeNode()));
        CSUtility.rightClickTreeNode(waitForReload,
                mamStudioSettingsNode.getBtnSettingsAttributeNode(),
                browserDriver);
        demoVolumePopup.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getCreateNewFolder(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        demoVolumePopup.enterValueInDialogue(waitForReload,
                attributeFolderName);
        demoVolumePopup.askBoxWindowOperation(waitForReload, pressOk,
                browserDriver);
        CSLogger.info("Attribute created successfully.");
    }

    /**
     * This method verifies if attribute folder is present
     * 
     * @param attributeFolderName contains the name of the attribute folder
     */
    private void verifyAddedAttributeFolder(String attributeFolderName) {
        try {
            clkAttributeNode();
            WebElement attributeFolder = browserDriver
                    .findElement(By.linkText(attributeFolderName));
            Assert.assertEquals(attributeFolder.getText(), attributeFolderName);
        } catch (Exception e) {
            CSLogger.error("Couldn't verify added attribute folder.", e);
            Assert.fail("Could not verify added attribute folder.", e);
        }
    }

    /**
     * This method clicks on attribute node
     */
    private void clkAttributeNode() {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
        mamStudioSettingsNode.getSettingsNodeMam().click();
        mamStudioSettingsNode.clkSettingsAttributeNode(waitForReload);
        CSLogger.info("Clicked on attribute node");
    }

    /**
     * This data provider returns data from sheet createAttributeFolderSheetName
     * which contains attributeFolder name
     * 
     * @return createAttributeFolderSheetName
     */
    @DataProvider(name = "CreateAttributeFolder")
    public Object[] CreateAttributeFolderData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                createAttributeFolderSheetName);
    }

    /**
     * This test initializes the resources required to drive the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        mamStudioSettingsNode = new MamStudioSettingsNode(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
    }
}
