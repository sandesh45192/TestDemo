/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimProductFilterPage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to handle the test cases related to the
 * Short By Filter Data use cases.
 * 
 * @author CSAutomation Team
 *
 */
public class SortByFilterDataTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimProductFilterPage      pimProductFilter;
    private CSPortalHeader            csPortalHeader;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private String                    ShortByFilterSheetName = "ShortByFilterContent";
    private String                    ascending              = "Ascending";
    private String                    descending             = "Descending";

    /**
     * This method gets the product label supplied to it via DataProvider method
     * 'getLabelsTestData'. This method perform the operation on short by
     * ascending. It also verifies the short by ascending is successful.
     * 
     * @param label String Label to Filter.
     */
    @Test(dataProvider = "LabelsTestData")
    public void testShortByAscending(String label) {
        String labels[] = label.split(",");
        try {
            Actions action = new Actions(browserDriver);
            csPortalHeader.clkBtnProducts(waitForReload);
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance
                            .getBtnPimSettingsAttributeNode());
            action.doubleClick(pimStudioSettingsNodeInstance
                    .getBtnPimSettingsAttributeNode()).perform();
            CSUtility.tempMethodForThreadSleep(1000);
            for (int countLabel = 0; countLabel < labels.length; countLabel++) {
                csPortalHeader.clkBtnProducts(waitForReload);
                CSUtility.traverseToPimStudio(waitForReload, browserDriver);
                action.doubleClick(
                        pimStudioProductsNode.getBtnPimProductsNode())
                        .perform();
                CSUtility.tempMethodForThreadSleep(1000);
                CSUtility.traverseToSettingsConfiguration(waitForReload,
                        browserDriver);
                if (!pimProductFilter.getTopFilterBar().isDisplayed()) {
                    pimProductFilter.getBtnFilterButton().click();
                    CSLogger.info("Clicked on Filter Button.");
                }
                setShortByFilterConfig(waitForReload, ascending);
                pimProductFilter.enterLabelFilter(labels[countLabel]);
                CSLogger.info(
                        "Verifying short test for label " + labels[countLabel]);
                CSUtility.tempMethodForThreadSleep(4000);
                verifyShortByTestCase(ascending);
            }
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testShortByAscending",
                    e);
            Assert.fail(
                    "Automation error in test method : testShortByAscending",
                    e);
        }
    }

    /**
     * This method gets the product label supplied to it via DataProvider method
     * 'getLabelsTestData'. This method perform the operation on short by
     * descending. It also verifies the short by descending is successful.
     * 
     * @param label String Label to Filter.
     */
    @Test(dataProvider = "LabelsTestData")
    public void testShortByDescending(String label) {
        String labels[] = label.split(",");
        Actions action = new Actions(browserDriver);
        try {
            for (int countLabel = 0; countLabel < labels.length; countLabel++) {
                csPortalHeader.clkBtnProducts(waitForReload);
                CSUtility.traverseToPimStudio(waitForReload, browserDriver);
                action.doubleClick(
                        pimStudioProductsNode.getBtnPimProductsNode())
                        .perform();
                CSUtility.traverseToSettingsConfiguration(waitForReload,
                        browserDriver);
                if (!pimProductFilter.getTopFilterBar().isDisplayed()) {
                    pimProductFilter.getBtnFilterButton().click();
                    CSLogger.info("Clicked on Filter Button.");
                }
                setShortByFilterConfig(waitForReload, descending);
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        pimProductFilter.getTxtLabelFilter());
                pimProductFilter.enterLabelFilter(labels[countLabel]);
                CSLogger.info(
                        "Verifying short test for label " + labels[countLabel]);
                verifyShortByTestCase(descending);
            }
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testShortByDescending",
                    e);
            Assert.fail(
                    "Automation error in test method : testShortByDescending",
                    e);
        }
    }

    /**
     * This method check the short by Test case. using the ID of product.
     * 
     * @param shortBy String short by ascending or descending.
     */
    public void verifyShortByTestCase(String shortBy) {
        int currentId;
        int lblCount = getProductsCount();
        int idHeaderIndex = CSUtility.getListHeaderIndex("ID", browserDriver);
        if (lblCount > 1) {
            ArrayList<Integer> recordIds = new ArrayList<>();
            for (int row = 3; row <= lblCount; row++) {
                CSUtility.tempMethodForThreadSleep(2000);
                waitForReload
                        .until(ExpectedConditions.visibilityOfElementLocated(By
                                .xpath("//table[@class='hidewrap CSAdminList']"
                                        + "/tbody[1]/tr[" + row + "]/td["
                                        + idHeaderIndex + "]")));
                currentId = Integer.parseInt(browserDriver
                        .findElement(By
                                .xpath("//table[@class='hidewrap CSAdminList']"
                                        + "/tbody[1]/tr[" + row + "]/td["
                                        + idHeaderIndex + "]"))
                        .getText());
                recordIds.add(currentId);
            }
            if (shortBy.equalsIgnoreCase("ascending")) {
                chkShortByAscending(lblCount, recordIds);
            } else {
                chkShortByDescending(lblCount, recordIds);
            }
            CSLogger.info("Short By verified.");
        } else {
            CSLogger.info("Products cannot be short.");
        }
    }

    /**
     * This method check the short by Ascending. using the ID of product.
     * 
     * @param lblcount Integer number of rows.
     */
    public void chkShortByAscending(int lblCount,
            ArrayList<Integer> recordIds) {
        for (int index = 0; index < recordIds.size();) {
            if (recordIds.get(index) > recordIds.get(index++)) {
                Assert.fail("Short by Ascending is not successful");
            }
        }
    }

    /**
     * This method check the short by Descending. using the ID of product.
     * 
     * @param lblcount Integer number of rows.
     */
    public void chkShortByDescending(int lblCount,
            ArrayList<Integer> recordIds) {
        for (int index = 0; index < recordIds.size();) {
            if (recordIds.get(index) < recordIds.get(index++)) {
                Assert.fail("Short by Ascending is not successful");
            }
        }
    }

    /**
     * This method count the product present. It search the product rows.
     *
     * @return count of the product present.
     */
    public int getProductsCount() {
        WebElement table = browserDriver
                .findElement(By.id("CSGuiListbuilderTable"));
        int productCount = table.findElements(By.tagName("tr")).size();
        return productCount;
    }

    /**
     * This method set the parameter for short by using ID.
     *
     * @param waitForReload.
     * @param short by string.
     */
    public void setShortByFilterConfig(WebDriverWait waitForReload,
            String shortBy) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getBtnCloseAdvanceSearch());
        pimProductFilter.getBtnCloseAdvanceSearch().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddAttribute());
        Select selAttribute = new Select(pimProductFilter.getSddAttribute());
        selAttribute.selectByVisibleText("ID");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddOperator());
        Select selOperator = new Select(pimProductFilter.getSddOperator());
        selOperator.selectByVisibleText("Is not empty");
        pimProductFilter.getBtnPlusOnShortBy().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddShortByAttributes());
        Select selShortByAttributes = new Select(
                pimProductFilter.getSddShortByAttributes());
        selShortByAttributes.selectByVisibleText("ID");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getSddValuesAseDec());
        Select selValuesAseDec = new Select(
                pimProductFilter.getSddValuesAseDec());
        if (shortBy.equalsIgnoreCase(ascending)) {
            selValuesAseDec.selectByVisibleText("Ascending");
        } else {
            selValuesAseDec.selectByVisibleText("Descending");
        }
        pimProductFilter.getBtnSearch().click();
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of credentials
     */
    @DataProvider(name = "LabelsTestData")
    public Object[][] getLabelsTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                ShortByFilterSheetName);
    }

    /**
     * This method will close all the configuration used for short by test.
     * 
     */
    @AfterClass()
    public void closeAdvanceSearch() {
        if (pimProductFilter.getBtnRemoveFilterButton().isDisplayed()) {
            pimProductFilter.getBtnRemoveFilterButton().click();
        }
        pimProductFilter.getBtnFilterButton().click();
        pimProductFilter.getBtnOpenAdvanceSearch().click();
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod()
    public void initializeResource() {
        waitForReload = new WebDriverWait(browserDriver, 30);
        csPortalHeader = new CSPortalHeader(browserDriver);
        pimProductFilter = new PimProductFilterPage(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
    }
}
