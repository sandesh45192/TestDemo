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
 * This class contains the test methods which verifies the Advance search
 * operation on Table type attribute
 * 
 * @author CSAutomation Team
 */
public class AdvanceSearchOfTableTypeAttributeTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimProductFilterPage      pimProductFilter;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private String                    advanceSearchOfTableAttrTestData = "AdvanceSearchOfTableAttr";

    /**
     * This method verifies the Advance search operation on Table type attribute
     * 
     * @param attrFolder String object contains attribute folder name
     * @param attrName String object contains attribute name
     * @param searchAttr String object contains attribute name to search
     * @param TextSerachData String object contains text data to search
     * @param selectionSearchData String object contains selection type data to
     *            search
     * @param productsName String object contains product name
     */
    @Test(dataProvider = "advanceSearchOfTableAttrDataSheet")
    public void testAdvanceSearchOfTableTypeAttribute(String attrFolder,
            String attrName, String searchAttr, String textSearchData,
            String selectionSearchData, String productsName) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            String attrID = getArrtibuteID(attrFolder, attrName);
            String customizeAttr = attrName + " (" + attrID + ")";
            searchDataOfDifferentAttr(customizeAttr, searchAttr, textSearchData,
                    selectionSearchData, productsName);
            CSLogger.info(
                    "advance search of table type attribute test completed");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testAdvanceSearchOfTableTypeAttribute",
                    e);
            Assert.fail(
                    "Automation Error : testAdvanceSearchOfTableTypeAttribute",
                    e);
        }
    }

    /**
     * This method verifies the Advance search option using different operators
     * 
     * @param customizeAttr String object contains attribute name and id
     * @param searchAttr String object contains attribute name to search
     * @param TextSerachData String object contains text data to search
     * @param selectionSearchData String object contains selection type data to
     *            search
     * @param productsName String object contains product name
     */
    private void searchDataOfDifferentAttr(String customizeAttr,
            String searchAttr, String textSearchData,
            String selectionSearchData, String productsName) {
        String[] searchAttrArray = searchAttr.split(",");
        for (String attributeToSearch : searchAttrArray) {
            getProductNode();
            pimProductFilter.clkBtnCloseAdvanceSearch(waitForReload);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimProductFilter.getSddAttribute());
            Select selAttribute = new Select(
                    pimProductFilter.getSddAttribute());
            selAttribute.selectByVisibleText(customizeAttr);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimProductFilter.getDrpDwnSubSearchAttribute());
            Select selectSubAttribute = new Select(
                    pimProductFilter.getDrpDwnSubSearchAttribute());
            selectSubAttribute.selectByVisibleText(attributeToSearch);
            switch (attributeToSearch) {
            case "AttrNum":
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        pimProductFilter.getTxtValue());
                pimProductFilter.getTxtValue().clear();
                pimProductFilter.getTxtValue().sendKeys(textSearchData);
                pimProductFilter.clkBtnSearch(waitForReload);
                CSUtility.tempMethodForThreadSleep(3000);
                CSLogger.info("Verification complete for Attribute AttrNum.");
                verificationSearchData(productsName);
                break;
            case "AttrSelection":
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        pimProductFilter.getDrpDwnSubSearchAttribute());
                Select selectValue = new Select(
                        pimProductFilter.getSddValuesAseDec());
                selectValue.selectByVisibleText(selectionSearchData);
                pimProductFilter.clkBtnSearch(waitForReload);
                CSUtility.tempMethodForThreadSleep(3000);
                verificationSearchData(productsName);
                CSLogger.info(
                        "Verification complete for Attribute AttrSelection.");
                break;
            default:
                break;
            }
        }
    }

    /**
     * This method verifies the result for searched data
     * 
     * @param productName String object contains product name
     */
    private void verificationSearchData(String productName) {
        String[] productArray = productName.split(",");
        List<WebElement> elementLabel = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']//tbody//tr//td[6]"));
        List<String> labelList = new ArrayList<String>();
        for (WebElement element : elementLabel) {
            labelList.add(element.getText());
        }
        for (String name : productArray) {
            if (labelList.contains(name)) {
                CSLogger.info("Verification pass for Product Name " + name);
            } else {
                CSLogger.error("Verification fail for Product Name " + name);
                Assert.fail("Verification fail for Product Name " + name);
            }
        }
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
     * contains FolderName,AttributeTechnicalName,Search Attribute, Text Search
     * Data,Selection Search Data,Product Name
     * 
     * @return advanceSearchOfTableAttrTestData
     */
    @DataProvider(name = "advanceSearchOfTableAttrDataSheet")
    public Object[] AdvanceSearchOfTableAttr() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("elasticSearchTestCases"),
                advanceSearchOfTableAttrTestData);
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
