/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.elasticsearch;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
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
 * This class contains the test methods which creates the Pre-requisites data
 * for elastic search uses.
 * 
 * @author CSAutomation Team
 */
public class AttributeConfigurationTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private PimStudioSettingsNode  pimStudioSettingsNodeInstance;
    private CSPortalHeader         csPortalHeaderInstance;
    private Actions                action;
    private IAttributePopup        attributePopup;
    private CSGuiDialogContentId   csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private FrameLocators          locator;
    private SelectionDialogWindow  selectionDialogWindow;
    private String                 attributeConfigTestData = "AttributeConfig";
    private String                 refSearchOption         = "Advanced Search";
    private String                 searchOption            = "Simple and Advanced Search";
    private String                 inheritance             = "Inheritance from parent nodes";
    private String                 inheritanceTable        = "Additive inheritance from parent nodes";

    /**
     * This method creates the Pre-requisites data for elastic search uses.
     * 
     * @param folderName String object contains attribute folder name
     * @param technicalName String object contains technical name of attributes
     * @param attributeValue String object contains attribute value name
     * @param fieldType String object contains attribute types
     */
    @Test(dataProvider = "attributeConfigurationDataSheet")
    public void testAttributeConfiguration(String folderName,
            String technicalName, String attributeValue, String fieldType) {
        try {
            String[] attributeArray = technicalName.split(",");
            String[] attrValueArray = attributeValue.split(",");
            String[] fieldTypeArray = fieldType.split(",");
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
            createAttributeFolder(folderName);
            for (int index = 0; index < fieldTypeArray.length; index++) {
                createVariousAttributeFields(folderName, attributeArray[index],
                        attrValueArray[index], fieldTypeArray[index]);
            }
            CSLogger.info("attribute configuration test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testAttributeConfiguration", e);
            Assert.fail("Automation Error : testAttributeConfiguration", e);
        }
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
        WebElement attrFolder = browserDriver
                .findElement(By.linkText(folderName));
        CSUtility.waitForVisibilityOfElement(waitForReload, attrFolder);
        attrFolder.click();
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdAttribute);
        createdAttribute.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnCSTypeLabel());
        csGuiDialogContentIdInstance.getBtnCSTypeLabel().click();
        CSUtility.tempMethodForThreadSleep(1000);
        selectAttributeType(attrValue, fieldType);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        sendTextToEelment(csGuiDialogContentIdInstance
                .getTxtPdmarticleconfigurationLabel(), attributeName);
        if (attrValue.contains("Selection Field")) {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiDialogContentIdInstance.getDrpDwnValueList());
            csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                    csGuiDialogContentIdInstance.getDrpDwnValueList(),
                    "PIM / Color");
            getAlertBox(true);
        }
        if (attrValue.contains("Table")) {
            addAttribueFolder(folderName);
        }
        sendTextToEelment(
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationExternalKey(),
                attributeName);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsInherited());
        if (fieldType.contains("Special")) {
            csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                    csGuiDialogContentIdInstance
                            .getDdPdmarticleconfigurationIsInherited(),
                    inheritanceTable);
        } else {
            csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                    csGuiDialogContentIdInstance
                            .getDdPdmarticleconfigurationIsInherited(),
                    inheritance);
        }
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsSearchable());
        if (fieldType.contains("Reference") || fieldType.contains("Special")) {
            csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                    csGuiDialogContentIdInstance
                            .getDdPdmarticleconfigurationIsSearchable(),
                    refSearchOption);
        } else {
            csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                    csGuiDialogContentIdInstance
                            .getDdPdmarticleconfigurationIsSearchable(),
                    searchOption);
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method add attribute folder
     * 
     * @param folderName String object contains attribute folder name
     */
    private void addAttribueFolder(String folderName) {
        csGuiDialogContentIdInstance.clkOnClassTable(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogLeftSection(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                selectionDialogWindow.getDrpDwnAttributes());
        selectionDialogWindow.getDrpDwnAttributes().click();
        WebElement attributeElement = browserDriver
                .findElement(By.linkText(folderName));
        action.doubleClick(attributeElement).build().perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
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
     * This method send the text data to the WebElement
     * 
     * @param txtelement WebElement object where the text data is to send
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
                config.getExcelSheetPath("elasticSearchTestCases"),
                attributeConfigTestData);
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
        selectionDialogWindow = new SelectionDialogWindow(browserDriver);
        CSUtility.switchToStudioList(waitForReload, browserDriver);
    }
}
