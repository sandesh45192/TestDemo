/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which Configur the attribute for the
 * Faceted Search
 * 
 * @author CSAutomation Team
 */
public class ConfigurationOfattributesForFacetedSearchTest
        extends AbstractTest {

    private WebDriverWait          waitForReload;
    private PimStudioSettingsNode  pimStudioSettingsNodeInstance;
    private CSPortalHeader         csPortalHeaderInstance;
    private Actions                action;
    private IAttributePopup        attributePopup;
    private CSGuiDialogContentId   csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private FrameLocators          locator;
    private String                 attributesForFacetedSearch = "AttributesForFacetedSearch";
    private String                 inheritance                = "No Inheritance";
    private String                 searchOption               = "Simple and Advanced Search";
    private String                 facettedSearch             = "Yes";
    private String                 facetConcatenator          = "OR Concatenator";
    private String                 unit                       = "Rs";
    private String                 measuringUnit              = "Length";
    private String                 selectField                = "PIM / Color";

    /**
     * This method creates the attributes required for faceted search.
     * 
     * @param folderName String object contains attribute folder name
     * @param attributeArray String object contains attribute data
     */
    @Test(dataProvider = "attributeConfigurationDataSheet")
    public void testAttributeConfigurationForFacetedSearch(String folderName,
            String attributeArray) {
        try {
            List<String> listAttribute = convertToList(attributeArray,
                    "Technical Name");
            List<String> listAttrValue = convertToList(attributeArray, "Value");
            List<String> listAttrField = convertToList(attributeArray, "Field");
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
            createAttributeFolder(folderName);
            for (int index = 0; index < listAttribute.size(); index++) {
                createVariousAttributeFields(folderName,
                        listAttribute.get(index), listAttrValue.get(index),
                        listAttrField.get(index));
            }
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testAttributeConfigurationForFacetedSearch",
                    e);
            Assert.fail(
                    "Automation Error : testAttributeConfigurationForFacetedSearch",
                    e);
        }
    }

    /**
     * This method convert the array to list according to required data
     * 
     * @param attributeArray string Object contains attribute data
     * @param type String object content name of type of attribute data
     * @return listArray list contains data related to attribute
     */
    private List<String> convertToList(String attributeArray, String type) {
        List<String> listArray = new ArrayList<String>();
        String[] arrayName = null;
        String[] arrayValue = null;
        String[] attributeArrray = attributeArray.split(",");
        for (int index = 0; index < attributeArrray.length; index++) {
            arrayName = attributeArrray[index].split(":");
            arrayValue = arrayName[1].split("=");
            if (type.equals("Technical Name")) {
                listArray.add(arrayName[0]);
            }
            if (type.equals("Value")) {
                listArray.add(arrayValue[0]);
            }
            if (type.equals("Field")) {
                listArray.add(arrayValue[1]);
            }
        }
        return listArray;
    }

    /**
     * This method creates the various attribute fields.
     * 
     * @param folderName String object contains attribute folder name
     * @param attributeName String object contains name of attribute
     * @param attributeValue String object contains attribute value name
     * @param fieldType String object contains attribute types
     */
    private void createVariousAttributeFields(String folderName,
            String attributeName, String attrValue, String fieldType) {
        createSingleLineAttribute(folderName, attributeName);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
        clkOnElementByLinkText(folderName);
        clkOnElementByLinkText(attributeName);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnCSTypeLabel());
        csGuiDialogContentIdInstance.getBtnCSTypeLabel().click();
        CSUtility.tempMethodForThreadSleep(1000);
        selectAttributeType(attrValue, fieldType);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        if (attrValue.equals("Selection Field")) {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiDialogContentIdInstance.getDrpDwnValueList());
            csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                    csGuiDialogContentIdInstance.getDrpDwnValueList(),
                    selectField);
            getAlertBox(true);
        }
        if (attrValue.equals("Number with unit of measurement")) {
            selectVaueFromDrpDwn(csGuiDialogContentIdInstance.getDrpDwnUnit(),
                    measuringUnit);
        }
        if (attrValue.equals("Number with Suffix")) {
            sendTextToEelment(csGuiDialogContentIdInstance.getTxtUnit(), unit);
        }
        if (attrValue.equals("Combination of number and selection field")) {
            selectVaueFromDrpDwn(
                    csGuiDialogContentIdInstance.getDrpDwnValueRange(),
                    selectField);
        }
        sendTextToEelment(csGuiDialogContentIdInstance
                .getTxtPdmarticleconfigurationGuiSortOrder(), "0");
        if (attrValue.equals("Checkbox")) {
            CSLogger.info("For Checkbox attribute inheritance is not present.");
        } else {
            selectVaueFromDrpDwn(
                    csGuiDialogContentIdInstance
                            .getDdPdmarticleconfigurationIsInherited(),
                    inheritance);
        }
        selectVaueFromDrpDwn(
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsSearchable(),
                searchOption);
        selectVaueFromDrpDwn(
                csGuiDialogContentIdInstance.getDrpDwnFacetedSearch(),
                facettedSearch);
        CSUtility.tempMethodForThreadSleep(2000);
        sendTextToEelment(csGuiDialogContentIdInstance.getTxtFacetLabel(),
                attributeName);
        selectVaueFromDrpDwn(
                csGuiDialogContentIdInstance.getDrpDwnFacetConcatenator(),
                facetConcatenator);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method select the attribute type and assign it to the attribute
     * 
     * @param attributeValue String object contains attribute value name
     * @param fieldType String object contains attribute types
     */
    private void selectAttributeType(String attrValue, String fieldType) {
        String fieldName = null;
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        CSUtility.switchToSplitAreaFrameLeft(waitForReload, browserDriver);
        WebElement element = browserDriver
                .findElement(By.xpath("//td[contains(text(),'Text')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        List<WebElement> fieldTypeDrpDwn = browserDriver.findElements(
                By.xpath("//table[@name='CSPortalGuiList']//tbody//tr//td[1]"));
        for (WebElement fieldElement : fieldTypeDrpDwn) {
            fieldName = fieldElement.getText();
            if (fieldName.contains(fieldType)) {
                fieldElement.click();
                break;
            }
        }
        WebElement attrValueElement = browserDriver
                .findElement(By.linkText(attrValue));
        CSUtility.waitForVisibilityOfElement(waitForReload, attrValueElement);
        action.doubleClick(attrValueElement).build().perform();
        getAlertBox(true);
    }

    /**
     * This method select the value from drop down
     * 
     * @param selector WebElement object contains elemnet of drop down
     * @param value String object contains value to select
     */
    private void selectVaueFromDrpDwn(WebElement selector, String value) {
        CSUtility.waitForVisibilityOfElement(waitForReload, selector);
        csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(selector,
                value);
    }

    /**
     * This method click on the element by link text
     * 
     * @param elementText String object contains element text
     */
    private void clkOnElementByLinkText(String elementText) {
        WebElement element = browserDriver
                .findElement(By.linkText(elementText));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
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
     * This method click the button in alert box
     * 
     * @param pressKey boolean object
     */
    private void getAlertBox(Boolean pressKey) {
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (pressKey) {
            alertBox.accept();
            CSLogger.info("Clicked on Ok of alertbox.");
        } else {
            alertBox.dismiss();
            CSLogger.info("Clicked on Cancel of alertbox.");
        }
    }

    /**
     * This method create single line type attribute
     * 
     * @param folderName String object contains attribute folder name
     * @param technicalName String object contains attribute type
     */
    private void createSingleLineAttribute(String folderName,
            String technicalName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(folderName));
        CSUtility.rightClickTreeNode(waitForReload, createdAttributeFolder,
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        attributePopup.enterValueInUserInputDialogue(waitForReload,
                technicalName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * This method create attribute folder.
     * 
     * @param attrFolderName String object contains attribute folder name
     */
    private void createAttributeFolder(String attrFolderName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode(),
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuNewFolder(), browserDriver);
        attributePopup.enterValueInDialogue(waitForReload, attrFolderName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains FolderName,AttributeTechnicalName,AttributeValue,FieldType
     * 
     * @return attributeConfigTestData
     */
    @DataProvider(name = "attributeConfigurationDataSheet")
    public Object[] attributeConfigurationTemplate() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                attributesForFacetedSearch);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        action = new Actions(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
    }
}
