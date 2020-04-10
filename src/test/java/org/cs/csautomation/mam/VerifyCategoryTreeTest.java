/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.util.ArrayList;
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
import org.testng.asserts.SoftAssert;

/**
 * This method verifies category tree test to class
 * 
 * @author CS Automation Team
 *
 */
public class VerifyCategoryTreeTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private MamStudioSettingsNode     mamStudioSettingsNode;
    private FrameLocators             locator;
    private IVolumePopup              demoVolumePopup;
    private String                    categoryTreeSheetName = "CategoryTree";
    private CSGuiDalogContentIdMam    csGuiDalogContentIdMam;
    private CSGuiToolbarHorizontalMam csGuiToolbarHorizontalMam;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private SoftAssert                softAssertion;
    private Actions                   actions;

    @Test(priority = 1, dataProvider = "categoryTreeCheckBoxTestData")
    public void testVerifyCategoryTreeCheckbox(String className,
            String testClass, String productTabname) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnMedia(waitForReload);
            createTestClass(testClass);
            enableShowCategoryTreeCheckbox(className, productTabname);
            verifyCategoryTreeCheckboxEnabledOrNot();
            enableShowCategoryTreeCheckbox(testClass, productTabname);
            verifyCategoryTreeCheckboxEnabledOrNot();
        } catch (Exception e) {
            CSLogger.debug("Automation error in testVerifyCategoryTreeCheckbox",
                    e);
            Assert.fail(
                    "Automation error in testVerifyCategoryTreeCheckbox" + e);
        }
    }

    /**
     * This method creates the empty test class to enable and verify show in
     * category tree option
     * 
     * @param testClass contains the name of the test class
     */
    private void createTestClass(String testClass) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
            mamStudioSettingsNode.getSettingsNodeMam().click();
            CSLogger.info("Clicked on Settings node");
            CSUtility.scrollUpOrDownToElement(
                    mamStudioSettingsNode.getClassNode(), browserDriver);
            CSUtility.rightClickTreeNode(waitForReload,
                    mamStudioSettingsNode.getClassNode(), browserDriver);
            CSLogger.info("Right clicked on the classes node");
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
                    .sendKeys(testClass);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    demoVolumePopup.getBtnCsGuiModalDialogOkButton()));
            demoVolumePopup.getBtnCsGuiModalDialogOkButton().click();
            CSLogger.info("Clicked on ok button to create class");
            CSLogger.info("Class created successfully");
        } catch (Exception e) {
            CSLogger.error("class creation failed", e);
        }
    }

    /**
     * This method enables show in category tree checkbox for given class
     * 
     * @param className contains the name of the class
     */
    private void enableShowCategoryTreeCheckbox(String className,
            String productTabname) {
        try {
            clickOnClassNode();
            WebElement nameOfClass = browserDriver
                    .findElement(By.linkText(className));
            CSUtility.waitForVisibilityOfElement(waitForReload, nameOfClass);
            actions.doubleClick(nameOfClass).build().perform();
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            clickOnTheGivenProductTab(productTabname, waitForReload);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiDalogContentIdMam.getShowInCategoryTreeMam()));
            csGuiDalogContentIdMam.getShowInCategoryTreeMam().click();
            CSUtility.tempMethodForThreadSleep(2000);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnSaveMam()));
            csGuiToolbarHorizontalMam.getBtnSaveMam().click();
            CSLogger.info("Show in category tree checkbox enabled");
        } catch (Exception e) {
            CSLogger.error("failed to enable checkbox", e);
            softAssertion.fail("Failed to click checkbox");
        }
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String name of the tab in the product view.
     * @param waitForReload
     */
    private void clickOnTheGivenProductTab(String productTabName,
            WebDriverWait waitForReload) {
        mamStudioVolumesNode.traverseUptoMainFrame();
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * This method traverses and clicks on classes node under settings in left
     * section pane of mam
     */
    private void clickOnClassNode() {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioSettingsNode.getSettingsNodeMam()));
            mamStudioSettingsNode.getSettingsNodeMam().click();
            CSLogger.info("Clicked on Settings node");
            mamStudioSettingsNode.clkClassNode(waitForReload);
            CSLogger.info("Traversed till classes node in left section pane");
        } catch (Exception e) {
            CSLogger.debug("Traversing upto class node is failed");
        }
    }

    /**
     * This method verifies if category tree checkbox is enabled or not
     */
    private void verifyCategoryTreeCheckboxEnabledOrNot() {
        try {
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            String categoryTreeCheckboxValue = csGuiDalogContentIdMam
                    .getShowInCategoryTreeMam().getAttribute("class");
            Assert.assertEquals(categoryTreeCheckboxValue,
                    "CSGuiEditorCheckboxContainer On Enabled GuiEditorCheckbox");
        } catch (Exception e) {
            CSLogger.debug("Error in verifying category tree checkbox");
        }
    }

    /**
     * This test case verifies if classes are present in file categories after
     * checking category tree checkbox and verifes if classes are absent after
     * unchecking the checkbox of category tree
     * 
     * @param className contains the name of the class
     * @param testClass contains the name of the test class
     */
    @Test(priority = 2, dataProvider = "categoryTreeCheckBoxTestData")
    public void testVerifyFileCategories(String className, String testClass,
            String productTabname) {
        try {
            browserDriver.navigate().refresh();
            CSUtility.tempMethodForThreadSleep(1000);
            clickOnFileCategories();
            traverseToFileCategories();
            checkIfClassIsPresent(className, true);
            checkIfClassIsPresent(testClass, true);
            CSUtility.tempMethodForThreadSleep(3000);
            uncheckTheCheckBoxOfClasses(className, productTabname);
            browserDriver.navigate().refresh();
            checkIfClassNotPresent(className, false);
            browserDriver.navigate().refresh();
            CSUtility.tempMethodForThreadSleep(1000);
            clickOnMamStudio();
            CSUtility.tempMethodForThreadSleep(5000);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method unchecks the category tree checkbox of class
     * 
     * @param className contains the name of the class
     */
    private void uncheckTheCheckBoxOfClasses(String className,
            String productTabname) {
        try {
            clickOnMamStudio();
            clickOnClassNode();
            WebElement nameOfClass = browserDriver
                    .findElement(By.linkText(className));
            CSUtility.waitForVisibilityOfElement(waitForReload, nameOfClass);
            actions.doubleClick(nameOfClass).build().perform();
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            clickOnTheGivenProductTab(productTabname, waitForReload);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    csGuiDalogContentIdMam.getShowInCategoryTreeMam()));
            csGuiDalogContentIdMam.getShowInCategoryTreeMam().click();
            CSUtility.tempMethodForThreadSleep(2000);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiToolbarHorizontalMam.getBtnSaveMam()));
            csGuiToolbarHorizontalMam.getBtnSaveMam().click();
        } catch (Exception e) {
            CSLogger.debug("Failed to uncheck the checkbox of classes , either"
                    + " it is already enabled " + "or exception occured" + e);
        }
    }

    /**
     * This method traverses and clicks on the mam studio in left section pane
     */
    private void clickOnMamStudio() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrame_192MamStudio()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getModuleContentFrameMam()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
        waitForReload
                .until(ExpectedConditions.visibilityOf(locator.getMamStudio()));
        locator.getMamStudio().click();
        CSLogger.info("Clicked on mam studio");
    }

    /**
     * This method checks if assigned class is present or not
     * 
     * @param className contains the name of the class
     * @param isPresent boolean variable to indicate if class is present or not
     */
    private void checkIfClassIsPresent(String className, Boolean isPresent) {
        try {

            WebElement classInfoTable = browserDriver
                    .findElement(By.xpath("//table[@class='Tree']"));
            ArrayList<WebElement> list = (ArrayList<WebElement>) classInfoTable
                    .findElements(By.xpath(
                            "//span[@id='0div0']/table/tbody/tr/td/span/table/tbody/tr/td[2]/a/span"));
            Boolean doExist = false;
            for (WebElement row : list) {
                if (row.getText().equals(className)) {
                    doExist = true;
                }
            }
            if (isPresent) {
                if (doExist) {
                    CSLogger.info("Class is present . test case verified");
                } else {
                    CSLogger.info(
                            "Class is not present. Test case is not verified.");
                }
            } else {
                if (doExist) {
                    CSLogger.info(
                            "Class is present . test case is not verified");
                    softAssertion.fail(
                            "Class is present even when show in category is disabled, test case is not verified");
                } else {
                    CSLogger.info(
                            "Class is not present when show in category is disable. Test case verified.");
                }
            }
        } catch (Exception e) {
            CSLogger.debug("Classes are not present in the tree view");
            softAssertion.fail(
                    "Verification of assigned classes in list view failed", e);
        }
    }

    /**
     * This method clicks on file categories option beside mam studio under
     * media
     */
    private void clickOnFileCategories() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrame_192MamStudio()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalBodyFrame()));
            waitForReload.until(ExpectedConditions
                    .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getFileCategoriesMam()));
            locator.getFileCategoriesMam().click();
            CSLogger.info("Clicked on file categories in mam");
        } catch (Exception e) {
            CSLogger.debug("Failed to click on file categories", e);
        }
    }

    public void traverseToFileCategories() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrame_192MamStudio()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalBodyFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrameMamFile()));
        CSLogger.info("Traversed till left section in file categories");
    }

    public void checkIfClassNotPresent(String className, Boolean isPresent) {
        clickOnFileCategories();
        traverseToFileCategories();
        checkIfClassIsPresent(className, isPresent);
    }

    /**
     * this data provider returns sheet data to the test method of class which
     * contains the name of class and test class which needs to be created
     * 
     * @return categoryTreeSheetName
     */
    @DataProvider(name = "categoryTreeCheckBoxTestData")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                categoryTreeSheetName);
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
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        mamStudioSettingsNode = new MamStudioSettingsNode(browserDriver);
        csGuiDalogContentIdMam = new CSGuiDalogContentIdMam(browserDriver);
        csGuiToolbarHorizontalMam = new CSGuiToolbarHorizontalMam(
                browserDriver);
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        softAssertion = new SoftAssert();
        actions = new Actions(browserDriver);
    }
}
