/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.FlexTablePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies Invert Orientation Option
 * test uses.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyInvertOrientationOptionTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeader;
    private FrameLocators          locator;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private FlexTablePage          flexTablePage;
    private String                 invertOrientationOptionTestData = "InvertOrientationOption";

    /**
     * This method verifies structure Option test uses.
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     */
    @Test(dataProvider = "InvertOrientationOptionDataSheet")
    public void testVerifyInvertOrientationOption(String templateName,
            String templateType) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToCreatedFlexTemplate(templateName, templateType);
            ArrayList<String> firstRowData = getDataFromFirstRow();
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            flexTablePage.clkOnBtnOrientationInverted(waitForReload);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            CSUtility.tempMethodForThreadSleep(1000);
            verifyInvertedTable(firstRowData);
            CSLogger.info("verify invert orientation option test completed");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerifyInvertOrientationOption", e);
            Assert.fail("Automation Error : testVerifyInvertOrientationOption",
                    e);
        }
    }

    /**
     * This method get the data from the first row of table
     * 
     * @return rowData array list contains data from first row
     */
    private ArrayList<String> getDataFromFirstRow() {
        ArrayList<String> rowData = new ArrayList<String>();
        String rowText = null;
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        int numberOfColumns = browserDriver.findElements(By.xpath(
                "//table[@class='CSFlexTable FlexTable']//thead//tr//th"))
                .size();
        WebElement colElement = null;
        for (int column = 1; column <= numberOfColumns; column++) {
            colElement = browserDriver.findElement(By.xpath(
                    "//table[@class='CSFlexTable FlexTable']//thead//tr//th["
                            + column + "]"));
            rowText = colElement.getText();
            rowData.add(rowText);
        }
        return rowData;
    }

    /**
     * This method verifies the Inverte Orientation Option on table
     * 
     * @param firstRowData array list contains data from first row
     */
    private void verifyInvertedTable(ArrayList<String> firstRowData) {
        String firstColumnText = null;
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        int numberOfRow = browserDriver
                .findElements(By.xpath(
                        "//table[@class='CSFlexTable FlexTable']//tbody//tr"))
                .size();
        for (int row = 1; row <= numberOfRow; row++) {
            WebElement firstColumnElement = browserDriver.findElement(By
                    .xpath("//table[@class='CSFlexTable FlexTable']//tbody//tr["
                            + row + "]//td[1]"));
            firstColumnText = firstColumnElement.getText();
            if (firstColumnText.equals(firstRowData.get(row - 1))) {
                CSLogger.info("Verified pass of Row to Column for "
                        + firstColumnText);
            } else {
                CSLogger.error("Verification fail of Row to Column for "
                        + firstColumnText);
                Assert.fail("Verification fail of Row to Column for "
                        + firstColumnText);
            }
        }
    }

    /**
     * This method traverses to created Flex Template
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     */
    private void goToCreatedFlexTemplate(String templateName,
            String templateType) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement freeNode = browserDriver
                .findElement(By.linkText(templateType));
        CSUtility.waitForVisibilityOfElement(waitForReload, freeNode);
        freeNode.click();
        WebElement nameOfTemplate = browserDriver
                .findElement(By.linkText(templateName));
        nameOfTemplate.click();
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnDataPane(waitForReload);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains TemplateName,TemplateType
     * 
     * @return structureOperationTestData
     */
    @DataProvider(name = "InvertOrientationOptionDataSheet")
    public Object[] invertOrientationOptionTemplate() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                invertOrientationOptionTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }
}
