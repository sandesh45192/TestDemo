/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.HashMap;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
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
 * This class contains the test methods which verifies edited data in child
 * product
 * 
 * @author CSAutomation Team
 */
public class EditingChildProductAttributesTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private Actions                   action;
    private String                    editChildProductSheet = "EditChildProduct";

    /**
     * This method verifies edited data in child product.
     * 
     * @param productFolder String object contains product folder name
     * @param childProduct String object contains child product name
     * @param subChildProduct String object contains sub child name
     * @param folderData String object contains data for product folder
     * @param childData String object contains data for product child
     * @param subChildData String object contains data for product sub child
     * @param paneTitle String object contains pane title
     * @param sectionTitle String object contains section title
     */
    @Test(dataProvider = "editChildProduct")
    public void testEditingChildProductAttribute(String productFolder,
            String childProduct, String subChildProduct, String folderData,
            String childData, String subChildData, String paneTitle,
            String sectionTitle) {
        try {
            HashMap<String, String> hashMapToSelectAttr = sendDataInHashMap(
                    folderData, "Attribute type");
            HashMap<String, String> hashMapForFolderData = sendDataInHashMap(
                    folderData, "Data");
            HashMap<
                    String,
                    String> hashMapForChildProductData = sendDataInHashMap(
                            childData, "Data");
            HashMap<
                    String,
                    String> hashMapForSubChildProductData = sendDataInHashMap(
                            subChildData, "Data");
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            goToFacetedSecion(productFolder, paneTitle, sectionTitle);
            CSUtility.tempMethodForThreadSleep(1000);
            assignDataToAttribute(hashMapToSelectAttr, hashMapForFolderData, 1);
            assignDataToChildProduct(productFolder, hashMapToSelectAttr,
                    hashMapForChildProductData, childProduct, paneTitle,
                    sectionTitle);
            assignDataToSubChildProduct(productFolder, childProduct,
                    hashMapToSelectAttr, hashMapForSubChildProductData,
                    subChildProduct, paneTitle, sectionTitle);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testEditingChildProductAttribute", e);
            Assert.fail("Automation Error : testEditingChildProductAttribute",
                    e);
        }
    }

    /**
     * This method assign data to sub child product
     * 
     * @param productFolder String object contains product folder name
     * @param childProduct String object contains child product name
     * @param listAttrName List object contains list of attributes
     * @param hashMapToSelectAttr HashMAp object contians attribute name as key
     *            and attribute type as value
     * @param hashMapForAttrData HashMAp object contians attribute name as key
     *            and data value
     * @param subChildProduct String object contains sub child name
     * @param paneTitle String object contains pane title
     * @param sectionTitle String object contains section title
     */
    private void assignDataToSubChildProduct(String productFolder,
            String childProduct, HashMap<String, String> hashMapToSelectAttr,
            HashMap<String, String> hashMapForAttrData, String subChildProduct,
            String paneTitle, String sectionTitle) {
        String[] subChildProductArray = subChildProduct.split(",");
        String[] childProductArray = childProduct.split(",");
        int appendData = 1;
        for (int index = 0; index < subChildProductArray.length; index++) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            pimStudioProductsNode.clickOnNodeProducts();
            clkOnElementByLinkText(productFolder);
            clkOnElementByLinkText(childProductArray[index]);
            WebElement subChildproductElement = browserDriver
                    .findElement(By.linkText(subChildProductArray[index]));
            action.doubleClick(subChildproductElement).build().perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            WebElement paneElement = browserDriver.findElement(
                    By.xpath("//nobr[contains(text(),'" + paneTitle + "')]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, paneElement);
            paneElement.click();
            WebElement selectionTitleParent = browserDriver
                    .findElement(By.xpath("//div[contains(text(),'"
                            + sectionTitle + "')]/../.."));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    selectionTitleParent);
            if (selectionTitleParent.getAttribute("class").contains("closed")) {
                browserDriver.findElement(By.xpath(
                        "//div[contains(text(),'" + sectionTitle + "')]/.."))
                        .click();
            }
            assignDataToAttribute(hashMapToSelectAttr, hashMapForAttrData,
                    appendData);
            CSLogger.info(
                    "Data is save In Product " + subChildProductArray[index]);
            appendData += 1;
            CSUtility.tempMethodForThreadSleep(1000);
        }
    }

    /**
     * This method assign data to child product
     * 
     * @param productFolder String object contains product folder name
     * @param listAttrName List object contains list of attributes
     * @param hashMapToSelectAttr HashMAp object contians attribute name as key
     *            and attribute type as value
     * @param hashMapForAttrData HashMAp object contians attribute name as key
     *            and data value
     * @param childProduct String object contains child product name
     * @param paneTitle String object contains pane title
     * @param sectionTitle String object contains section title
     */
    private void assignDataToChildProduct(String productFolder,

            HashMap<String, String> hashMapToSelectAttr,
            HashMap<String, String> hashMapForAttrData, String childProduct,
            String paneTitle, String sectionTitle) {
        String[] childProductArray = childProduct.split(",");
        int appendData = 1;
        for (String childName : childProductArray) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            pimStudioProductsNode.clickOnNodeProducts();
            clkOnElementByLinkText(productFolder);
            WebElement childproductElement = browserDriver
                    .findElement(By.linkText(childName));
            action.doubleClick(childproductElement).build().perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            WebElement paneElement = browserDriver.findElement(
                    By.xpath("//nobr[contains(text(),'" + paneTitle + "')]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, paneElement);
            paneElement.click();
            WebElement selectionTitleParent = browserDriver
                    .findElement(By.xpath("//div[contains(text(),'"
                            + sectionTitle + "')]/../.."));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    selectionTitleParent);
            if (selectionTitleParent.getAttribute("class").contains("closed")) {
                browserDriver.findElement(By.xpath(
                        "//div[contains(text(),'" + sectionTitle + "')]/.."))
                        .click();
            }
            assignDataToAttribute(hashMapToSelectAttr, hashMapForAttrData,
                    appendData);
            CSLogger.info("Data is save In Product " + childName);
            appendData += 1;
            CSUtility.tempMethodForThreadSleep(1000);
        }
    }

    /**
     * This method assign attribute data to product
     * 
     * @param productFolder String object contains product folder name
     * @param listAttrName List object contains list of attributes
     * @param hashMapToSelectAttr HashMAp object contians attribute name as key
     *            and attribute type as value
     * @param hashMapForAttrData HashMAp object contians attribute name as key
     *            and data value
     * @param appendData integer veriable use to create the different data in
     *            text and number type field
     */
    private void assignDataToAttribute(
            HashMap<String, String> hashMapToSelectAttr,
            HashMap<String, String> hashMapForAttrData, int appendData) {
        String attrType = null;
        String attrData = null;
        String path = null;
        int number = 0;
        WebElement attrElement = null;
        String[] dataInArray = new String[10];
        for (String attrName : hashMapToSelectAttr.keySet()) {
            attrType = hashMapToSelectAttr.get(attrName);
            attrData = hashMapForAttrData.get(attrName);
            path = "//tr[contains(@cs_name,'" + attrName + "')]";
            switch (attrType) {
            case "Single-line Text":
                enterTextData(path, attrData + Integer.toString(appendData));
                break;
            case "Checkbox":
                CSUtility.traverseToSettingsConfiguration(waitForReload,
                        browserDriver);
                path = path
                        + "//span[contains(@id,'PdmarticleCS_ItemAttribute')]";
                attrElement = browserDriver.findElement(By.xpath(path));
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        attrElement);
                if (attrElement.getAttribute("class").contains("Off")) {
                    attrElement.click();
                }
                break;
            case "Simple Selection Field":
                enterTextData(path, attrData + Integer.toString(appendData));
                break;
            case "Selection Field":
                selectDrpDwnData(path, attrData);
                break;
            case "Combination of number and selection field":
                dataInArray = attrData.split(";");
                enterTextData(path, dataInArray[0]);
                selectDrpDwnData(path, dataInArray[1]);
                break;
            case "Currency":
                number = Integer.parseInt(attrData) * appendData;
                attrData = Integer.toString(number);
                enterTextData(path, attrData);
                break;
            case "Number":
                number = Integer.parseInt(attrData) * appendData;
                attrData = Integer.toString(number);
                enterTextData(path, attrData);
                break;
            case "Number with Suffix":
                enterTextData(path, attrData);
                break;
            case "Per mil":
                enterTextData(path, attrData);
                break;
            case "Number with unit of measurement":
                enterTextData(path, attrData);
                break;
            default:
                CSLogger.info(
                        attrType + " attribute type is not present in code.");
                break;
            }
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCheckIn(waitForReload);
    }

    /**
     * This method select the data for drop down type attribute
     * 
     * @param path String object contains xpath of the attribute
     * @param attrData String object contains attribute data
     */
    private void selectDrpDwnData(String path, String attrData) {
        WebElement attrElement = null;
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        path = path + "//select[contains(@id,'PdmarticleCS_ItemAttribute')]";
        attrElement = browserDriver.findElement(By.xpath(path));
        CSUtility.waitForVisibilityOfElement(waitForReload, attrElement);
        selectElementFromDrpDwn(attrElement, attrData);
    }

    /**
     * This method enter the data for text type attribute
     * 
     * @param path String object contains xpath of the attribute
     * @param attrData String object contains attribute data
     */
    private void enterTextData(String path, String attrData) {
        WebElement attrElement = null;
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        path = path + "//input[contains(@id,'PdmarticleCS_ItemAttribute')]";
        attrElement = browserDriver.findElement(By.xpath(path));
        CSUtility.waitForVisibilityOfElement(waitForReload, attrElement);
        sendTextToEelment(attrElement, attrData);
    }

    /**
     * This method click on element by link Text
     * 
     * @param textValue String object contains text of element
     */
    private void clkOnElementByLinkText(String textValue) {
        WebElement element = browserDriver.findElement(By.linkText(textValue));
        element.click();
        CSLogger.info("Click on element " + textValue);
    }

    /**
     * This convert the array into HashMap
     * 
     * @param folderData String object contains folder data
     * @param type String object contains type of hash map
     * @return productTableData HashMap contain HAsMap data
     */
    private HashMap<String, String> sendDataInHashMap(String folderData,
            String type) {
        String dataArray[] = folderData.split(",");
        HashMap<
                String,
                String> productTableData = new HashMap<String, String>();
        String attributeNameArray[] = null;
        String attributeTypeArray[] = null;
        for (int index = 0; index < dataArray.length; index++) {
            attributeNameArray = dataArray[index].split(":");
            attributeTypeArray = attributeNameArray[1].split("=");
            if (type.equals("Data")) {
                productTableData.put(attributeNameArray[0],
                        attributeTypeArray[1]);
            }
            if (type.equals("Attribute type")) {
                productTableData.put(attributeNameArray[0],
                        attributeTypeArray[0]);
            }
        }
        return productTableData;
    }

    /**
     * This method traverse to the faceted section of product.
     * 
     * @param productFolder String object contains product folder name
     * @param paneName String object contains pane name
     * @param sectionName String object contains section name
     */
    private void goToFacetedSecion(String productFolder, String paneTitle,
            String sectionTitle) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productElement = browserDriver
                .findElement(By.linkText(productFolder));
        action.doubleClick(productElement).build().perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement paneElement = browserDriver.findElement(
                By.xpath("//nobr[contains(text(),'" + paneTitle + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, paneElement);
        paneElement.click();
        WebElement selectionTitleParent = browserDriver.findElement(By
                .xpath("//div[contains(text(),'" + sectionTitle + "')]/../.."));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                selectionTitleParent);
        if (selectionTitleParent.getAttribute("class").contains("closed")) {
            browserDriver.findElement(By
                    .xpath("//div[contains(text(),'" + sectionTitle + "')]/.."))
                    .click();
        }
    }

    /**
     * This method select the element from drop down
     * 
     * @param element WebElement contains drop down element
     * @param value string object contains value to select
     */
    private void selectElementFromDrpDwn(WebElement element, String value) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        Select elementValue = new Select(element);
        elementValue.selectByVisibleText(value);
    }

    /**
     * This method send the text data to the WebElement
     * 
     * @param txtelement WebElement object contains element of text input
     * @param text String object contains text data
     */
    private void sendTextToEelment(WebElement txtelement, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, txtelement);
        txtelement.clear();
        txtelement.sendKeys(text);
        CSLogger.info(text + " data is sent.");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Product Name,ChildProduct,SubChild Product,Product Folder Data,
     * Child Product Data,SubChild Product Data,Pane Name,Section Name
     * 
     * @return editChildProductSheet
     */
    @DataProvider(name = "editChildProduct")
    public Object[] editChildProduct() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                editChildProductSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        action = new Actions(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
    }
}
