/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimProductFilterPage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to handle the test cases related to the
 * Filter use cases.
 * 
 * @author CSAutomation Team
 */
public class ProductsFilterTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeader;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private PimProductFilterPage      pimProductFilter;
    public String                     filterSheetName = "FilterProductLabelContent";

    /**
     * This method gets the product label supplied to it via DataProvider method
     * 'getLabelsTestData'. This method perform the operation on simple search.
     * It also verifies the searching is successful.
     * 
     * @param label String Label to Filter.
     */
    @Test(priority = 1, dataProvider = "LabelsTestData")
    public void testFilterTestStandardAttribute(String label) {
        String labels[] = label.split(",");
        try {
            for (int countLabel = 0; countLabel < labels.length; countLabel++) {
                CSLogger.info("Enter in customised attributes ");
                clkBtnFilter();
                CSUtility.tempMethodForThreadSleep(2000);
                pimProductFilter.enterTxtToSearch(labels[countLabel]);
                CSUtility.tempMethodForThreadSleep(1000);
                verifyFilter(labels[countLabel]);
            }
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testFilterTestStandardAttribute",
                    e);
            Assert.fail(
                    "Automation error in test method : testFilterTestStandardAttribute",
                    e);
        }
    }

    /**
     * This method gets the product label supplied to it via DataProvider method
     * 'getLabelsTestData'. This method perform the operation on filer using
     * product label. It also verifies the filter is successful.
     * 
     * @param label String Label to Filter.
     */
    @Test(priority = 2, dataProvider = "LabelsTestData")
    public void testFilterTestCustomisedAttribute(String label) {
        String labels[] = label.split(",");
        try {
            for (int countLabel = 0; countLabel < labels.length; countLabel++) {
                clkBtnFilter();
                CSUtility.tempMethodForThreadSleep(2000);
                pimProductFilter.enterLabelFilter(labels[countLabel]);
                verifyFilter(labels[countLabel]);
                pimProductFilter.getTxtLabelFilter().clear();
            }
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testFilterTestCustomisedAttribute",
                    e);
            Assert.fail(
                    "Automation error in test method :testFilterTestCustomisedAttribute",
                    e);
        }
    }

    /**
     * This method click on the filter button.
     */
    public void clkBtnFilter() {
        Actions action = new Actions(browserDriver);
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        action.doubleClick(pimStudioProductsNode.getBtnPimProductsNode())
                .perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getBtnFilterButton());
        if (!pimProductFilter.getTopFilterBar().isDisplayed()) {
            pimProductFilter.getBtnFilterButton().click();
            CSLogger.info("Clicked on Filter Button.");
            CSUtility.tempMethodForThreadSleep(1000);
            verifyPresenceOfProducts();
        }
    }

    /**
     * This method verifies that the product folders are present under Products.
     */
    public void verifyPresenceOfProducts() {
        try {
            int productsCount = getProductsCount();
            if (productsCount != 0) {
                CSLogger.info("Products are Present");
            } else {
                Assert.fail("No Products are Present");
            }
        } catch (Exception e) {
            CSLogger.error("Automation error", e);
            Assert.fail("Automation error", e);
        }
    }

    /**
     * This method check the filter case is execute successfully using the
     * label.
     * 
     * @param label String Label to Filter.
     */
    public void verifyFilter(String label) {
        int labelHeaderIndex = CSUtility.getListHeaderIndex("Label",
                browserDriver);
        CSLogger.info("Verifying filter test.");
        try {
            int lblCount = getProductsCount();
            int lblMatched = 0;
            for (int row = 3; row <= lblCount + 2; row++) {
                waitForReload
                        .until(ExpectedConditions.visibilityOfElementLocated(By
                                .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                        + row + "]/td[" + labelHeaderIndex
                                        + "]")));
                if (browserDriver
                        .findElement(By
                                .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                        + row + "]/td[" + labelHeaderIndex
                                        + "]"))
                        .getText().contains(label)) {
                    lblMatched += 1;
                }
            }
            if ((lblCount) == lblMatched) {
                CSLogger.info(
                        "Filter use case by label " + label + " is passsed");
            } else {
                Assert.fail("Filter use case by label " + label + " fails");
            }
        } catch (Exception e) {
            CSLogger.error("Automation error", e);
            Assert.fail("Automation error",e);
        }
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of label of products
     */
    @DataProvider(name = "LabelsTestData")
    public Object[][] getLabelsTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                filterSheetName);
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
        return productCount - 2;
    }

    /**
     * This method will close all the configuration used for filter test.
     * 
     */
    @AfterClass()
    public void closeFilter() {
        if (pimProductFilter.getBtnRemoveFilterButton().isDisplayed()) {
            pimProductFilter.getBtnRemoveFilterButton().click();
        }
        pimProductFilter.getBtnFilterButton().click();
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
    }
}
