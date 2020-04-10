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
 * This test class creates and verifies class
 * 
 * @author CSAutomation team
 */
public class ClassCreationTest extends AbstractTest {

    private WebDriverWait         waitForReload;
    private FrameLocators         locator;
    private MamStudioSettingsNode mamStudioSettingsNode;
    private IVolumePopup          demoVolumePopup;
    private String                createClassSheetName = "CreateClassSheetMam";
    private CSPortalHeader        csPortalHeader;

    /**
     * This test method drives the use case of class creation which takes class
     * name input from the excel fileF
     * 
     * @param className contains the name of the class
     */
    @Test(priority = 1, dataProvider = "CreateClass")
    public void testCreateClassTest(String className) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            clkSettingsNode();
            clkClassesNode();
            sendClassName(className);
            clkOkToCreateClass();
            CSLogger.info("Clicked on ok button to create class");
            verifyAddedClasses(waitForReload, className);
        } catch (Exception e) {
            CSLogger.debug("class created successfully",e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method sends class name from file for creation of class
     * 
     * @param className
     */
    private void sendClassName(String className) {
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                demoVolumePopup.getTxtCsGuiModalDialogFormName()));
        CSUtility.tempMethodForThreadSleep(1000);
        demoVolumePopup.getTxtCsGuiModalDialogFormName().sendKeys(className);
    }

    /**
     * This method clicks on ok of pop up to create class
     */
    private void clkOkToCreateClass() {
        waitForReload.until(ExpectedConditions.visibilityOf(
                demoVolumePopup.getBtnCsGuiModalDialogOkButton()));
        demoVolumePopup.getBtnCsGuiModalDialogOkButton().click();
        CSLogger.info("Clicked on OK of pop up to create class");
    }

    /**
     * This method clicks on classes node
     */
    private void clkClassesNode() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                mamStudioSettingsNode.getClassNode());
        CSUtility.rightClickTreeNode(waitForReload,
                mamStudioSettingsNode.getClassNode(), browserDriver);
        CSLogger.info("Clicked on classes node");
        clkCreateNew();
    }

    /**
     * This method clicks on settings node
     */
    private void clkSettingsNode() {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
        mamStudioSettingsNode.getSettingsNodeMam().click();
        CSLogger.info("Clicked on Settings node");
    }

    /**
     * This method clicks on create new option for creating class
     */
    private void clkCreateNew() {
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
     * This method verifies if class has added successfully to the classes node
     * or not
     * 
     * @param waitForReload waits for the element to reload
     * @param className contains the name of the class
     */
    private void verifyAddedClasses(WebDriverWait waitForReload,
            String className) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
            mamStudioSettingsNode.getSettingsNodeMam().click();
            CSLogger.info("Clicked on Settings node");
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getClassNode()));
            mamStudioSettingsNode.clkClassNode(waitForReload);
            WebElement nameOfClass = browserDriver
                    .findElement(By.linkText(className));
            Assert.assertEquals(nameOfClass.getText(), className);
        } catch (Exception e) {
            CSLogger.error("Could not verify added classes", e);
            Assert.fail("Could not verify the added classes", e);
        }
    }

    /**
     * This data provider returns sheet containing name of class to be creates
     * 
     * @return createClassSheetName
     */
    @DataProvider(name = "CreateClass")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                createClassSheetName);
    }

    /**
     * This test initializes the resources required to drive the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        mamStudioSettingsNode = new MamStudioSettingsNode(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
    }
}