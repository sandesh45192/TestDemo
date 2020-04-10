/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * 
 * This class contains the test methods to performs simple search operation with
 * searchable text and non-searchable text and also verifies the result
 * 
 * @author CSAutomation Team
 */
public class SimpleSearchTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private String                    simpleSearchTestData = "SimpleSearch";

    /**
     * This method creates performs simple search operation with searchable text
     * and non-searchable text and also verifies the result
     * 
     * @param searchData String object contains Searchable text data
     * @param nonSearchData String object contains Non-Searchable text data
     * @param productName String object contains product name
     */
    @Test(dataProvider = "SimpleSearchDataSheet")
    public void testSimpleSearch(String searchData, String nonSearchData,
            String productName) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            searchData(searchData);
            verificationSearchableData(productName);
            searchData(nonSearchData);
            verifyNonSearchableData();
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testSimpleSearch", e);
            Assert.fail("Automation Error : testSimpleSearch", e);
        }
    }

    /**
     * This method verifies the result for non-searchable data
     * 
     */
    private void verifyNonSearchableData() {
        goToRightSectionWindow();
        int countTableElement = browserDriver
                .findElements(By
                        .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr[3]"))
                .size();
        if (countTableElement == 0) {
            CSLogger.info("Test case Non searchable Data is successful.");
        } else {
            CSLogger.info("Test case Non searchable Data is not successful.");
            Assert.fail("Test case Non searchable Data is not successful.");
        }
    }

    /**
     * This method verifies the result for searchable data
     * 
     * @param productName String object contains product name
     */
    private void verificationSearchableData(String productName) {
        String[] productArray = productName.split(",");
        goToRightSectionWindow();
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='hidewrap CSAdminList']")));
        CSUtility.tempMethodForThreadSleep(2000);
        List<WebElement> elementLabel = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']//tbody/tr/td[6]"));
        CSUtility.tempMethodForThreadSleep(1000);
        List<String> labelList = new ArrayList<String>();
        for (WebElement element : elementLabel) {
            CSUtility.tempMethodForThreadSleep(1000);
            labelList.add(element.getText());
        }
        for (String name : productArray) {
            if (labelList.contains(name)) {
                CSLogger.info("Verification pass for Product Name " + name);
            } else {
                CSLogger.error("Verification fail for Product Name " + name);
                Assert.fail("Verification fail for Product Name " + name);
                CSUtility.tempMethodForThreadSleep(2000);
            }
        }
    }

    /**
     * This method perform the search operation
     * 
     * @param dataToSearch String object contains text data to search
     */
    private void searchData(String dataToSearch) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        action.doubleClick(pimStudioProductsNode.getBtnPimProductsNode())
                .build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        goToRightSectionWindow();
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//button[@data-original-title='Switch to Studio List']"));
        int listSize = list.size();
        if (listSize != 0) {
            csGuiToolbarHorizontalInstance.getBtnSwitchToStudioList().click();
            CSLogger.info("clicked on switch to studio list button");
        }
        csGuiToolbarHorizontalInstance.getTxtSearchBox().click();
        csGuiToolbarHorizontalInstance.getTxtSearchBox().clear();
        csGuiToolbarHorizontalInstance.getTxtSearchBox().sendKeys(dataToSearch);
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.getTxtSearchBox().sendKeys(Keys.ENTER);
        CSUtility.tempMethodForThreadSleep(5000);
        CSLogger.info("Search Complete for data " + dataToSearch);
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Searchable Data,Nonsearchable Data,Product Name
     * 
     * @return simpleSearchTestData
     */
    @DataProvider(name = "SimpleSearchDataSheet")
    public Object[] simpleSearch() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("elasticSearchTestCases"),
                simpleSearchTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 120);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        action = new Actions(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
    }
}
