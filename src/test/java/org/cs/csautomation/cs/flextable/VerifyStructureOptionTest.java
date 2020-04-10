/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods which verifies structure Option test
 * uses.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyStructureOptionTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private Actions                action;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSPortalHeader         csPortalHeader;
    private FrameLocators          locator;
    private FlexTablePage          flexTablePage;
    private SoftAssert             softasssert;
    private String                 structureOperationTestData = "StructureOperator";

    /**
     * This method verifies structure Option test uses.
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param mergeCol String object contains merge column data
     * @param mergeRow String object contains merge row data
     * @param colLimit String object contains number to limit column
     * @param rowLimit String object contains number to limit row
     */
    @Test(dataProvider = "StructureOperatorDataSheet")
    public void testVerifyStructureOption(String templateName, String tempType,
            String mergeCol, String mergeRow, String colLimit,
            String rowLimit) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToCreatedFlexTemplate(templateName, tempType);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            flexTablePage.clkOnDataPane(waitForReload);
            addColumn();
            addRow();
            flexTablePage.clkOnStructurePane(waitForReload);
            hideEmptyRowsAndColumns();
            MergeAdjacentCell(mergeCol, mergeRow);
            limitRowsAndColumns(colLimit, rowLimit);
            softasssert.assertAll();
            CSLogger.info("verify structure option test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerifyStructureOption", e);
            Assert.fail("Automation Error : testVerifyStructureOption", e);
        }
    }

    /**
     * This method merge the adjacent cell having same data.
     * 
     * @param mergeCol String object contains merge column data
     * @param mergeRow String object contains merge row data
     */
    private void MergeAdjacentCell(String mergeCol, String mergeRow) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnBtnMergeEqualCols(waitForReload);
        flexTablePage.clkOnBtnMergeEqualRow(waitForReload);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        verifyMergeCell(mergeCol, mergeRow);
    }

    /**
     * This method verifies the merged cell data.
     * 
     * @param mergeCol String object contains merge column data
     * @param mergeRow String object contains merge row data
     */
    private void verifyMergeCell(String mergeCol, String mergeRow) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        WebElement elementMergeRow = browserDriver
                .findElement(By.xpath("//td[@class='MergedRow']"));
        WebElement elementMergeCol = browserDriver
                .findElement(By.xpath("//td[@class='MergedCol']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, elementMergeCol);
        String txtMergeCol = elementMergeCol.getText();
        CSUtility.waitForVisibilityOfElement(waitForReload, elementMergeRow);
        String txtMergeRow = elementMergeRow.getText();
        if (txtMergeCol.equals(mergeCol)) {
            CSLogger.info("Merge Column Pass for " + txtMergeCol);
        } else {
            CSLogger.error("Merge Column fail for " + txtMergeCol);
            softasssert.fail("Merge Column fail for " + txtMergeCol);
        }
        if (txtMergeRow.equals(mergeRow)) {
            CSLogger.info("Merge Row Pass for " + txtMergeCol);
        } else {
            CSLogger.error("Merge Row fail for " + txtMergeCol);
            softasssert.fail("Merge Row fail for " + txtMergeCol);
        }
    }

    /**
     * This method limits the number of row and column.
     * 
     * @param colLimit String object contains number to limit column
     * @param rowLimit String object contains number to limit row
     */
    private void limitRowsAndColumns(String colLimit, String rowLimit) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.setColumnLimit(waitForReload, colLimit);
        flexTablePage.setRowLimit(waitForReload, rowLimit);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        verifyLimitRowsAndColumns(colLimit, rowLimit);
    }

    /**
     * This method verifies the limited number of row and column test.
     * 
     * @param colLimit String object contains number to limit column
     * @param rowLimit String object contains number to limit row
     */
    private void verifyLimitRowsAndColumns(String colLimit, String rowLimit) {
        int limitCol = Integer.parseInt(colLimit);
        int limitRow = Integer.parseInt(rowLimit);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        int numberOfColumn = getNumberOfColumns();
        int numberOfRows = getNumberOfRows();
        if (numberOfColumn == limitCol) {
            CSLogger.info("Column Limits Successful");
        } else {
            CSLogger.error("Column Limits Fail");
            softasssert.fail("Column Limits Fail");
        }
        if (numberOfRows == limitRow) {
            CSLogger.info("Row Limits Successful");
        } else {
            CSLogger.error("Row Limits Fail");
            softasssert.fail("Row Limits Fail");
        }
    }

    /**
     * This Method Hides the empty rows and Columns
     * 
     */
    private void hideEmptyRowsAndColumns() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        int numberOfColumn = getNumberOfColumns();
        int numberOfRows = getNumberOfRows();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnBtnHideEmptyRow(waitForReload);
        flexTablePage.clkOnBtnHideEmptyCols(waitForReload);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        VerifyHiddenRowAndColumn(numberOfColumn, numberOfRows);
    }

    /**
     * This Method verifies the hide empty rows and Columns test
     * 
     */
    private void VerifyHiddenRowAndColumn(int numberOfColumn,
            int numberOfRows) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        int latestNumberOfColumn = getNumberOfColumns();
        int latestNumberOfRows = getNumberOfRows();
        if (latestNumberOfColumn < numberOfColumn) {
            CSLogger.info("Hide Column Successful");
        } else {
            CSLogger.error("Hide Column Fail");
            softasssert.fail("Hide Column Fail");
        }
        if (latestNumberOfRows < numberOfRows) {
            CSLogger.info("Hide Row Successful");
        } else {
            CSLogger.error("Hide Row Fail");
            softasssert.fail("Hide Row Fail");
        }
    }

    /**
     * This method get the number of columns present in table
     * 
     * @return numberOfColumn integer value number of columns
     */
    private int getNumberOfColumns() {
        int numberOfColumn = browserDriver.findElements(By.xpath(
                "//table[@class='CSFlexTable FlexTable']//tbody//tr[1]//td"))
                .size();
        return numberOfColumn;
    }

    /**
     * This method get the number of rows present in table
     * 
     * @return numberOfRows integer value number of rows
     */
    private int getNumberOfRows() {
        int numberOfRows = browserDriver
                .findElements(By.xpath(
                        "//table[@class='CSFlexTable FlexTable']//tbody//tr"))
                .size();
        return numberOfRows;
    }

    /**
     * This method traverses to created Flex Template
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     */
    private void goToCreatedFlexTemplate(String templateName, String tempType) {
        goToNodeFlexTablePreset();
        WebElement freeNode = browserDriver.findElement(By.linkText(tempType));
        CSUtility.waitForVisibilityOfElement(waitForReload, freeNode);
        freeNode.click();
        WebElement nameOfTemplate = browserDriver
                .findElement(By.linkText(templateName));
        nameOfTemplate.click();
    }

    /**
     * This method adds new Column in Table.
     * 
     */
    private void addColumn() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//div[contains(@class,'GridAddImagesContainer')]")));
        int numberOfColumn = browserDriver.findElements(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div"))
                .size();
        WebElement imageContainer = browserDriver.findElement(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div["
                        + numberOfColumn + "]"));
        action.moveToElement(imageContainer).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement btnAddColumn = browserDriver.findElement(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div["
                        + numberOfColumn
                        + "]//img[contains(@src,'src=plus')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnAddColumn);
        btnAddColumn.click();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method adds new row in Table.
     * 
     */
    private void addRow() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//ul[contains(@class,'Grid')]")));
        int numberOfRow = browserDriver
                .findElements(By.xpath("//ul[contains(@class,'Grid')]//li"))
                .size();
        WebElement actionContainer = browserDriver
                .findElement(By.xpath("//ul[contains(@class,'Grid')]//li["
                        + numberOfRow + "]//div[2]"));
        action.moveToElement(actionContainer).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement btnAddRow = browserDriver.findElement(
                By.xpath("//ul[contains(@class,'Grid')]//li[" + numberOfRow
                        + "]//div[2]//img[contains(@src,'src=plus')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnAddRow);
        btnAddRow.click();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method traverse to the preset node of flex table
     * 
     */
    private void goToNodeFlexTablePreset() {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        flexTablePage.clkOnFlexTableNode(waitForReload);
        flexTablePage.clkOnPresetsNode(waitForReload);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains TemplateName,TemplateType,MergeColumn,MergeRow,LimitColumnTo,
     * LimitRowTo
     * 
     * @return structureOperationTestData
     */
    @DataProvider(name = "StructureOperatorDataSheet")
    public Object[] structureOperatorTemplate() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                structureOperationTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        action = new Actions(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        softasssert = new SoftAssert();
    }
}
