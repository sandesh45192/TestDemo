/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.elasticsearch;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.importstaging.ImportStagingMainPage;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.PimProductFilterPage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which perform the Advance Search
 * Opreation on the Import data.
 * 
 * @author CSAutomation Team
 */
public class MassImportAndAdvanceSearchingTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private FrameLocators             iframeLocatorsInstance;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private ImportStagingMainPage     importStagingMainPage;
    private PimProductFilterPage      pimProductFilter;
    private String                    searchImportDataTestData = "SearchImportData";
    private IMoreOptionPopup          moreOptionsPopup;

    /**
     * This method verifies search operation for import data
     * 
     * @param filePath String object contains path of file
     * @param productsName String object contains product name
     * @param textData String object contains text data to search
     */
    @Test(dataProvider = "searchImportDataSheet")
    public void testMassImportAndAdvanceSearching(String filePath,
            String productName, String textData) {
        try {
            filePath = getFilePath(filePath);
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            importFileToPIM(filePath);
            verifyImport(productName);
            searchData(textData);
            verifySearchResult(productName);
            CSLogger.info("mass import & advance search test completed");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testMassImportAndAdvanceSearching", e);
            Assert.fail("Automation Error : testMassImportAndAdvanceSearching",
                    e);
        }
    }

    /**
     * This method get the file path of current system
     * 
     * @param filePath String object contains path from sheet
     * @return path String contains current file path
     */
    private String getFilePath(String filePath) {
        String currentPath = System.getProperty("user.dir");
        String path = currentPath + filePath;
        return path;
    }

    /**
     * This method verifies the result for searchable data
     * 
     * @param productName String object contains product name
     */
    private void verifySearchResult(String productName) {
        List<WebElement> elementLabel = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']//tbody//tr//td[6]"));
        List<String> labelList = new ArrayList<String>();
        for (WebElement element : elementLabel) {
            labelList.add(element.getText());
        }
        if (labelList.contains(productName)) {
            CSLogger.info("Verification pass for Product Name " + productName);
        } else {
            CSLogger.error("Verification fail for Product Name " + productName);
            Assert.fail("Verification fail for Product Name " + productName);
        }
    }

    /**
     * This method search the data
     * 
     * @param textData String object contains text data to search
     */
    private void searchData(String textData) {
        getProductNode();
        pimProductFilter.clkBtnCloseAdvanceSearch(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddAttribute());
        Select selAttribute = new Select(pimProductFilter.getSddAttribute());
        selAttribute.selectByVisibleText("External Key");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddOperator());
        Select selOperator = new Select(pimProductFilter.getSddOperator());
        selOperator.selectByVisibleText("Contains");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getTxtValue());
        pimProductFilter.getTxtValue().clear();
        pimProductFilter.getTxtValue().sendKeys(textData);
        pimProductFilter.clkBtnSearch(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This verifies the import product
     * 
     * @param productName String object contains product name
     */
    private void verifyImport(String productsName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        List<WebElement> productElement = (ArrayList<WebElement>) browserDriver
                .findElements(By.linkText(productsName));
        if (!productElement.isEmpty()) {
            CSLogger.info("Product Import Verified");
        } else {
            CSLogger.error("Product Import not Verified");
            Assert.fail("Product Import not Verified");
        }
    }

    /**
     * This method import file
     * 
     * @param filePath String object contains path of file
     */
    private void importFileToPIM(String filePath) throws Exception {
        getProductNode();
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        moreOptionsPopup.selectPopupDivMenu(waitForReload,
                moreOptionsPopup.getCtxMoreOptionsProducts(), browserDriver);
        moreOptionsPopup.selectPopupDivSubMenu(waitForReload,
                moreOptionsPopup.getCtxImportCsv(), browserDriver);
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        mamStudioVolumesNode.clkOnBrowseFileToUpload(waitForReload);
        addFilePath(filePath);
        mamStudioVolumesNode.clkOnUploadButtonImage(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        mamStudioVolumesNode.clkOnBtnCloseAfterUpload(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        handelImportOverViewWindow();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("File import successfully");
    }

    /**
     * This method handel window import over view
     * 
     */
    private void handelImportOverViewWindow() {
        ArrayList<String> tabs = new ArrayList<String>(
                browserDriver.getWindowHandles());
        browserDriver.switchTo().window(tabs.get(1));
        browserDriver.manage().window().maximize();
        importStagingMainPage.clickOkButton(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        importStagingMainPage.clickOkButton(waitForReload);
        browserDriver.switchTo().window(tabs.get(0));
        browserDriver.navigate().refresh();
    }

    /**
     * This method adds test folder by clicking enter
     * 
     * @param filePath contains path of the xls file
     */
    private void addFilePath(String filePath) throws Exception {
        StringSelection select = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(select,
                null);
        CSUtility.tempMethodForThreadSleep(2000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        CSUtility.tempMethodForThreadSleep(2000);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        CSLogger.info("Image added to the test folder");
    }

    /**
     * This method traverse to product node of PIM
     * 
     */
    private void getProductNode() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        action.doubleClick(pimStudioProductsNode.getBtnPimProductsNode())
                .build().perform();
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains
     * 
     * @return searchImportDataTestData
     */
    @DataProvider(name = "searchImportDataSheet")
    public Object[] MassImportAndAdvanceSearching() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("elasticSearchTestCases"),
                searchImportDataTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 120);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        action = new Actions(browserDriver);
        iframeLocatorsInstance = new FrameLocators(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        importStagingMainPage = new ImportStagingMainPage(browserDriver);
        pimProductFilter = new PimProductFilterPage(browserDriver);
        moreOptionsPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        CSUtility.switchToStudioList(waitForReload, browserDriver);
    }
}
