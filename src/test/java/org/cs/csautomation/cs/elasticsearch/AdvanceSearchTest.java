/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * 
 * This class contains the test methods to performs Advance search operation
 * using different operators
 * 
 * @author CSAutomation Team
 */
public class AdvanceSearchTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimProductFilterPage      pimProductFilter;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private String                    advanceSearchTestData = "AdvanceSearch";

    /**
     * This method verifies the availablity of option advance search
     * 
     */
    @Test(priority = 1)
    public void testAdvanceSearch() {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            getProductNode();
            verifyAdvanceSearchOption();
            pimProductFilter.clkBtnOpenAdvanceSearch(waitForReload);
            CSLogger.info("advance search test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testAdvanceSearch", e);
            Assert.fail("Automation Error : testAdvanceSearch", e);
        }
    }

    /**
     * This method verifies the Advance search optaion using different operators
     * 
     * @param attrFolder String object contains attribute folder name
     * @param attrName String object contains attribute name
     * @param serachOperator String object contains search operator
     * @param searchData String object contains search data
     * @param productsName String object contains product name
     */
    @Test(priority = 2, dataProvider = "AdvanceSearchDataSheet")
    public void testAdvanceSearchUsingOperetors(String attrFolder,
            String attrName, String serachOperator, String searchData,
            String productsName) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            String attrID = getArrtibuteID(attrFolder, attrName);
            String customizeAttr = attrName + " (" + attrID + ")";
            searchUsingOperators(customizeAttr, serachOperator, searchData,
                    productsName);
            CSLogger.info("test advance search using operators completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testAdvanceSearchUsingOperetors",
                    e);
            Assert.fail("Automation Error : testAdvanceSearchUsingOperetors",
                    e);
        }
    }

    /**
     * This method perform Advance search optaion using different operators
     * 
     * @param customizeAttr String object contains attribute name and id
     * @param serachOperator String object contains search operator
     * @param searchData String object contains search data
     * @param productsName String object contains product name
     */
    private void searchUsingOperators(String customizeAttr,
            String serachOperator, String searchData, String productsName) {
        String[] operatorArray = serachOperator.split(",");
        for (String operatorName : operatorArray) {
            getProductNode();
            pimProductFilter.clkBtnCloseAdvanceSearch(waitForReload);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimProductFilter.getSddAttribute());
            Select selAttribute = new Select(
                    pimProductFilter.getSddAttribute());
            selAttribute.selectByVisibleText(customizeAttr);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimProductFilter.getSddOperator());
            Select selOperator = new Select(pimProductFilter.getSddOperator());
            selOperator.selectByVisibleText(operatorName);
            switch (operatorName) {
            case "Contains":
                sendValueText(searchData);
                verifySearchData(productsName, true);
                CSLogger.info("Verification complete for operator 'contain'.");
                break;
            case "Is":
                sendValueText(searchData);
                verifySearchData(productsName, true);
                CSLogger.info("Verification complete for operator 'Is'.");
                break;
            case "Is not":
                sendValueText(searchData);
                verifySearchData(productsName, false);
                CSLogger.info("Verification complete for operator 'Is not'.");
                break;
            case "Is empty":
                pimProductFilter.clkBtnSearch(waitForReload);
                CSUtility.tempMethodForThreadSleep(3000);
                verifySearchData(productsName, false);
                CSLogger.info("Verification complete for operator 'Is empty'.");
                break;
            case "Is not empty":
                pimProductFilter.clkBtnSearch(waitForReload);
                CSUtility.tempMethodForThreadSleep(3000);
                verifySearchData(productsName, true);
                CSLogger.info(
                        "Verification complete for operator 'Is not empty'.");
                break;
            default:
                break;
            }
        }
    }

    /**
     * This method verifies the search data from the table
     * 
     * @param productsName String object contains product name
     * @param status boolean object
     */
    private void verifySearchData(String productsName, Boolean status) {
        String[] productArray = productsName.split(",");
        List<WebElement> elementLabel = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']//tbody//tr//td[6]"));
        List<String> labelList = new ArrayList<String>();
        for (WebElement element : elementLabel) {
            labelList.add(element.getText());
        }
        for (String name : productArray) {
            if (status) {
                if (labelList.contains(name)) {
                    CSLogger.info("Verification pass for Product Name " + name);
                } else {
                    CSLogger.error(
                            "Verification fail for Product Name " + name);
                    Assert.fail("Verification fail for Product Name " + name);
                }
            } else {
                if (!labelList.contains(name)) {
                    CSLogger.info("Verification pass for Product Name " + name);
                } else {
                    CSLogger.error(
                            "Verification fail for Product Name " + name);
                    Assert.fail("Verification fail for Product Name " + name);
                }
            }
        }
        pimProductFilter.clkBtnOpenAdvanceSearch(waitForReload);
    }

    /**
     * This method send the text value
     * 
     * @param searchData String object contains text value
     */
    private void sendValueText(String searchData) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimProductFilter.getTxtValue());
        pimProductFilter.getTxtValue().clear();
        pimProductFilter.getTxtValue().sendKeys(searchData);
        pimProductFilter.clkBtnSearch(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method get the previously created attribute Id
     * 
     * @param attrFolder String object contains attribute folder name
     * @param attrName String object contains attribute name
     * @return id contains Id of attribute
     */
    private String getArrtibuteID(String attrFolder, String attrName) {
        String id = null;
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(attrFolder));
        createdAttributeFolder.click();
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(attrName));
        action.doubleClick(createdAttribute).build().perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getTxtAttributeId());
        id = csGuiDialogContentId.getTxtAttributeId().getAttribute("value");
        return id;
    }

    /**
     * This method verifies the presence of advance search option and attribute
     * Drop Down list
     * 
     */
    private void verifyAdvanceSearchOption() {
        pimProductFilter.clkBtnCloseAdvanceSearch(waitForReload);
        int attrDrpDwncount = browserDriver
                .findElements(By.xpath("//select[@name='attribute']")).size();
        if (attrDrpDwncount != 0) {
            CSLogger.info("Drop Down list for Attribute is present");
        } else {
            CSLogger.error("Drop Down list for Attribute is not present");
            Assert.fail("Drop Down list for Attribute is not present");
        }
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
     * contains FolderName,AttributeTechnicalName,Search Operator,Search Data,
     * Product Name
     * 
     * @return advanceSearchTestData
     */
    @DataProvider(name = "AdvanceSearchDataSheet")
    public Object[] AdvanceSearch() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("elasticSearchTestCases"),
                advanceSearchTestData);
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
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        pimProductFilter = new PimProductFilterPage(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        CSUtility.switchToStudioList(waitForReload, browserDriver);
    }
}
