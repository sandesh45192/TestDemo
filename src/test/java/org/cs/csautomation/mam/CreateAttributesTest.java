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
 * This test class creates and verifies attribute under already created
 * attribute folder
 * 
 * @author CSAutomation team
 *
 */
public class CreateAttributesTest extends AbstractTest {

    private WebDriverWait         waitForReload;
    private MamStudioSettingsNode mamStudioSettingsNode;
    private IVolumePopup          demoVolumePopup;
    private String                createAttributesSheetName = "CreateAttributesMam";
    private CSPortalHeader        csPortalHeader;

    /**
     * This test driver the test case of test attribute creation with input
     * parameters as attribute folder name and attribute name to be created
     * under newly created attribute folder
     * 
     */
    @Test(priority = 1, dataProvider = "CreateAttributes")
    public void testCreateAttributes(String attributeFolderName,
            String attributeName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            performCreateAttributeTest(waitForReload, attributeFolderName,
                    attributeName, false);
            performCreateAttributeTest(waitForReload, attributeFolderName,
                    attributeName, true);
            verifyCreatedAttributes(waitForReload, attributeFolderName,
                    attributeName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method created attribute under newly created attribute folder
     * 
     * @param waitForReload waits for loading an element
     * @param attributeFolderName contains the name of the attribute folder
     * @param attributeName contains the name of the attribute
     * @param pressOk contains the instance of ok and cancel
     */
    private void performCreateAttributeTest(WebDriverWait waitForReload,
            String attributeFolderName, String attributeName, boolean pressOk) {
        try {
            clkAttributeNode();
            WebElement attributeFolder = browserDriver
                    .findElement(By.linkText(attributeFolderName));
            CSUtility.rightClickTreeNode(waitForReload, attributeFolder,
                    browserDriver);
            demoVolumePopup.selectPopupDivMenu(waitForReload,
                    demoVolumePopup.getCreateNewMamAttribute(), browserDriver);
            demoVolumePopup.enterValueInUserInputDialogue(waitForReload,
                    attributeName);
            demoVolumePopup.askBoxWindowOperation(waitForReload, pressOk,
                    browserDriver);
            CSLogger.info("Attribute creation successful.");
        } catch (Exception e) {
            CSLogger.error("Attribute creation failed.", e);
        }
    }

    /**
     * This method clicks on attribute node
     * 
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
     * This method verifies if attribute is present under newly created attrbute
     * folder
     * 
     * @param waitForReload waits for reloading element
     * @param attributeFolderName contains the name of the attribute folder
     * @param attributeName contains the name of the attribute
     */
    private void verifyCreatedAttributes(WebDriverWait waitForReload2,
            String attributeFolderName, String attributeName) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
            mamStudioSettingsNode.getSettingsNodeMam().click();
            mamStudioSettingsNode.getBtnSettingsAttributeNode();
            WebElement attributeFolder = browserDriver
                    .findElement(By.linkText(attributeFolderName));
            attributeFolder.click();
            WebElement createdAttribute = browserDriver
                    .findElement(By.linkText(attributeName));
            Assert.assertEquals(createdAttribute.getText(), attributeName);
        } catch (Exception e) {
            CSLogger.error("Verification of created attributes failed.", e);
            Assert.fail("Could not verify attribute");
        }
    }

    /**
     * This data provider returns the data of sheet which contains attribute
     * folder name and attribute name to be created
     */
    @DataProvider(name = "CreateAttributes")
    public Object[] CreateAttributeFolderData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                createAttributesSheetName);
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
