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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to handle the test cases related to the
 * Short By Standard Attributes use cases.
 * 
 * @author CSAutomation Team
 *
 */
public class SortingByStandardAttibutesTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimProductFilterPage      pimProductFilter;
    private CSPortalHeader            csPortalHeader;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private String                    ascending  = "Ascending";
    private String                    descending = "Descending";

    /**
     * This method perform the operation on short by ascending using Standard
     * Attributes. It also verifies the short by ascending is successful.
     * 
     */
    @Test
    public void testShortByAscending() {
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
            action.doubleClick(pimStudioProductsNode.getBtnPimProductsNode())
                    .perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            setShortByConfig(waitForReload, ascending);
            verifyShortByTestCase(ascending);
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
     * This method perform the operation on short by descending using Standard
     * Attributes. It also verifies the short by descending is successful.
     * 
     */
    @Test
    public void testShortByDescending() {
        try {
            Actions action = new Actions(browserDriver);
            csPortalHeader.clkBtnProducts(waitForReload);
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            action.doubleClick(pimStudioProductsNode.getBtnPimProductsNode())
                    .perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            setShortByConfig(waitForReload, descending);
            verifyShortByTestCase(descending);
            pimProductFilter.getBtnOpenAdvanceSearch().click();
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
     * This method set the parameter for short by using ID.
     *
     * @param waitForReload.
     * @param short by string.
     */
    public void setShortByConfig(WebDriverWait waitForReload, String shortBy) {
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
                currentId = Integer.parseInt(browserDriver
                        .findElement(By
                                .xpath("//table[@class='hidewrap CSAdminList']"
                                        + "/tbody[1]/tr[" + row + "]/td["
                                        + idHeaderIndex + "]"))
                        .getText());
                recordIds.add(currentId);
            }
            if (shortBy.equalsIgnoreCase(ascending)) {
                chkShortByAscending(lblCount, recordIds);
            } else {
                chkShortByDescending(lblCount, recordIds);
            }
            CSLogger.info("Short By verified.");
        } else {
            CSLogger.error("Products cannot be short.");
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
