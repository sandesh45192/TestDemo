
package org.cs.csautomation.cs.dataflow;

/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.settings.ActiveJobsPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contain the test method to verify export script can be executed
 * from PIM studio.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyExecutionFromPimStudioExportTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSPortalWidget            csPortalWidgetInstance;
    private ActiveJobsPage            activeJobsPageInstance;
    private FrameLocators             iframeLocatorsInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private Actions                   performAction;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private HashMap<String, String>   exportData;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private CSPopupDivPim             csPopupDivPim;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private SoftAssert                softAssertion;
    private String                    fileLocation;
    private ArrayList<String>         actualHeaders;
    private String                    testDataSheet = "verifyExportFromPimStudio";
    private IAttributePopup           attributePopup;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private IClassPopup               classPopUp;
    private IProductPopup             productPopup;
    private Alert                     alertBox;
    private ModalDialogPopupWindow    modalDialogPopupWindowInstance;
    private String                    referenceProductId;
    private String                    exportedFileName;
    private ArrayList<String>         actualData;
    private ArrayList<String>         expectedData;
    private XSSFWorkbook              excelWorkBook;
    private XSSFSheet                 excelSheet    = null;

    /**
     * This test method executes the export script from PIM studio.
     * 
     * @param testData Array of String containing the test Data from Data
     *            driven.
     */
    @Test(dataProvider = "exportPimStudioTestData")
    public void testVerifyExecutionFromPimStudioExport(String... testData) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            readTestDataFromSheet(testData);
            createPrerequisiteData(exportData.get("className"),
                    exportData.get("attributeFolderName"),
                    exportData.get("attributesName"),
                    exportData.get("attributeType"),
                    exportData.get("attributeValue"),
                    exportData.get("parentProductName"),
                    exportData.get("firstChildProductName"),
                    exportData.get("secondChildProductName"));
            configureExportActiveScript(exportData.get("exportLabel"),
                    exportData.get("exportCategory"));
            verifyExecutionFromPimStudioExport(
                    exportData.get("allowAccessOption"),
                    exportData.get("exportLabel"));
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.info("Automation error in test method "
                    + "testVerifyExecutionFromPimStudioExport", e);
            Assert.fail("Automation error in test method "
                    + "testVerifyExecutionFromPimStudioExport", e);
        }
    }

    /**
     * This method reads the test data from sheet.
     * 
     * @param testData String contains test data.
     */
    private void readTestDataFromSheet(String[] testData) {
        exportData = new HashMap<>();
        String sheetData[] = { "parentProductName", "firstChildProductName",
                "secondChildProductName", "attributeFolderName",
                "attributesName", "attributeType", "attributeValue",
                "className", "parentProductData", "firstChildProductData",
                "secondChildProductData", "productTab", "refToPimAttrName",
                "exportLabel", "exportCategory", "dataSourceSectionFields",
                "dataSourceType", "dataSelectionType", "selectionLayer",
                "defaultLanguage", "startCount", "maxCount", "batchSize",
                "filename", "fileGeneration", "dataTargetTypeFields",
                "volumeFolderName", "mamFolderName", "allowAccessOption" };
        try {
            for (int index = 0; index < sheetData.length; index++) {
                exportData.put(sheetData[index], testData[index]);
            }
        } catch (

        Exception e) {
            CSLogger.debug("Error while reading test data", e);
            Assert.fail("Error while reading test data", e);
            return;
        }
    }

    /**
     * This method creates the prerequisite configuration data for export test.
     * 
     * @param className String object containing the name of the class to assign
     *            the test product.
     * @param attributeFolder String object contains attribute folder name.
     * @param attributeName String object contains attribute name.
     * @param attributeType String object contains attribute field.
     * @param attributeValue String object contains attribute value.
     * @param productName String object contains product name.
     * @param firstChild String object contains first child name.
     * @param secondChild String object contains second child name.
     */
    private void createPrerequisiteData(String className,
            String attributeFolder, String attributeName, String attributeType,
            String attributeValue, String productName, String firstChild,
            String secondChild) {
        String[] attributeArray = attributeName.split(",");
        String[] attributeValueArray = attributeValue.split(",");
        String[] attributeTypeArray = attributeType.split(",");
        switchToPimAndExpandSettingsTree();
        createAttributeFolder(attributeFolder);
        createSingleLineTextAttribute(attributeFolder, attributeArray[0]);
        for (int index = 0; index < attributeTypeArray.length; index++) {
            createVariousAttributeFields(attributeFolder,
                    attributeArray[index + 1], attributeTypeArray[index],
                    attributeValueArray[index]);
        }
        createClass(className);
        dragAndDropAttrFolderToClass(attributeFolder, className);
        switchToPimAndExpandProductsTree(waitForReload);
        createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                productName);
        createChildProduct(productName, firstChild);
        createChildProduct(productName, secondChild);
        assignClassToParentProduct(productName, className);
        goToPimStudioTreeSection();
        createProduct(exportData.get("refToPimAttrName"),
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                productPopup.getCsGuiPopupMenuCreateNew());
        referenceProductId = getProductId(exportData.get("refToPimAttrName"));
        String testData[] = { exportData.get("parentProductData"),
                exportData.get("firstChildProductData"),
                exportData.get("secondChildProductData") };
        for (int index = 0; index < testData.length; index++) {
            String productData[] = spiltData(testData[index]);
            setConfigDataToBeExported(productData[0],
                    exportData.get("productTab"),
                    exportData.get("attributesName").split(",")[0],
                    productData[1],
                    exportData.get("attributesName").split(",")[1],
                    productData[2],
                    exportData.get("attributesName").split(",")[2],
                    exportData.get("refToPimAttrName"),
                    exportData.get("attributesName").split(",")[3],
                    productData[3]);
        }
    }

    /**
     * Switches to PIM and expands settings tree.
     */
    private void switchToPimAndExpandSettingsTree() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * This method create attribute folder.
     * 
     * @param attributeFolderName String object contains attribute folder name
     */
    private void createAttributeFolder(String attributeFolderName) {
        goToPimStudioTreeSection();
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode(),
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuNewFolder(), browserDriver);
        attributePopup.enterValueInDialogue(waitForReload, attributeFolderName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * This method creates various attribute fields.
     * 
     * @param folderName String object contains attribute folder name
     * @param attributeName String object contains name of attribute
     * @param attributeType String object contains attribute types
     * @param attributeValue String object contains attribute value name
     * 
     */
    private void createVariousAttributeFields(String folderName,
            String attributeName, String attributeType, String attributeValue) {
        goToPimStudioTreeSection();
        clkOnGivenAttributeFolder(folderName);
        goToRightSectionWindow();
        String parentWindow = browserDriver.getWindowHandle();
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCreateNew(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        String childWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(childWindow);
        CSLogger.info("Switched to child window");
        CSUtility.switchToDefaultFrame(browserDriver);
        csGuiDialogContentIdInstance
                .enterDataForTextAttributeField(waitForReload,
                        csGuiDialogContentIdInstance
                                .getTxtPdmarticleconfigurationName(),
                        attributeName);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        JavascriptExecutor js = (JavascriptExecutor) browserDriver;
        js.executeScript("window.scrollBy(0,-350)", "");
        CSUtility.tempMethodForThreadSleep(4000);
        csGuiDialogContentIdInstance.clkBtnCSTypeLabel(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        selectAttributeType(attributeType, attributeValue);
        csGuiDialogContentIdInstance
                .enterDataForTextAttributeField(waitForReload,
                        csGuiDialogContentIdInstance
                                .getTxtPdmarticleconfigurationLabel(),
                        attributeName);
        if (attributeValue.contains("Selection Field")) {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiDialogContentIdInstance.getDrpDwnValueList());
            csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(
                    csGuiDialogContentIdInstance.getDrpDwnValueList(),
                    "PIM / Color");
            alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
            alertBox.accept();
            CSLogger.info("Clicked on OK of alert box");
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        browserDriver.close();
        browserDriver.switchTo().window(parentWindow);
    }

    /**
     * Clicks on given attribute folder.
     * 
     * @param folderName String object contains attribute folder.
     */
    private void clkOnGivenAttributeFolder(String folderName) {
        WebElement attributeFolder = getAttributeFolder(folderName);
        attributeFolder.click();
        CSLogger.info("Clicked on " + folderName + " attribute folder");
    }

    /**
     * This method create single line text type attribute.
     * 
     * @param attributeFolder String object contains attribute folder name
     * @param attributeName String object contains attribute type
     */
    private void createSingleLineTextAttribute(String attributeFolder,
            String attributeName) {
        goToPimStudioTreeSection();
        WebElement createdAttributeFolder = getAttributeFolder(attributeFolder);
        CSUtility.rightClickTreeNode(waitForReload, createdAttributeFolder,
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        attributePopup.enterValueInUserInputDialogue(waitForReload,
                attributeName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        goToPimStudioTreeSection();
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdAttribute);
        createdAttribute.click();
        goToRightSectionWindow();
        csGuiDialogContentIdInstance
                .enterDataForTextAttributeField(waitForReload,
                        csGuiDialogContentIdInstance
                                .getTxtPdmarticleconfigurationLabel(),
                        attributeName);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method create given class.
     * 
     * @param className String object contains class name
     */
    private void createClass(String className) {
        goToPimStudioTreeSection();
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode(),
                browserDriver);
        classPopUp.selectPopupDivMenu(waitForReload,
                classPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        classPopUp.enterValueInDialogue(waitForReload, className);
        classPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("class  " + className + " created");
        goToPimStudioTreeSection();
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        WebElement classElment = browserDriver
                .findElement(By.linkText(className));
        classElment.click();
        goToRightSectionWindow();
        enterTextIntoTextbox(csGuiDialogContentIdInstance
                .getTxtPdmarticleconfigurationLabel(), className);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method select the attribute type and assign it to the attribute
     * 
     * @param attributeType String object contains attribute type.
     * @param attributeValueString object contains attribute value.
     */
    private void selectAttributeType(String attributeType,
            String attributeValue) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSUtility.switchToSplitAreaFrameLeft(waitForReload, browserDriver);
        WebElement element = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + attributeType + "')]/.."));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        if (!(element.getAttribute("class").contains("open"))) {
            element.click();
        }
        WebElement attrValueElement = browserDriver.findElement(
                By.xpath("//a[contains(.,'" + attributeValue + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, attrValueElement);
        performAction.doubleClick(attrValueElement).perform();
        alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        alertBox.accept();
        CSLogger.info("Clicked on Ok of alertbox.");
    }

    /**
     * This method send the text data to the WebElement
     * 
     * @param element WebElement object where the text data is to send
     * @param text String object contains text data
     */
    private void enterTextIntoTextbox(WebElement element, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.clear();
        element.sendKeys(text);
        CSLogger.info("Entered text " + text + " into text box");
    }

    /**
     * This method drag and drop attribute to class
     * 
     * @param attributeFolderName String object attribute folder name
     * @param className String object contains class name
     */
    private void dragAndDropAttrFolderToClass(String attributeFolderName,
            String className) {
        goToPimStudioTreeSection();
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement attributeFolderToDragDrop = browserDriver
                .findElement(By.linkText(attributeFolderName));
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        performAction
                .dragAndDrop(attributeFolderToDragDrop, classNameToDragDrop)
                .build().perform();
        CSLogger.info("Dragged attribute folder " + attributeFolderName
                + " and dropped it on class " + className);
    }

    /**
     * Assigns the given class to the parent product folder
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param className String object containing name of the class
     */
    public void assignClassToParentProduct(String parentProductName,
            String className) {
        doubleClkOnProduct(parentProductName);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        selectClassFromDataSelectionDialogWindow(className);
    }

    /**
     * Performs operation of double clicking on parent product
     * 
     * @param productName String Object containing the product name under which
     *            child product will be created.
     */
    public void doubleClkOnProduct(String productName) {
        try {
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement parentProduct = getCreatedProduct(productName);
            CSUtility.waitForVisibilityOfElement(waitForReload, parentProduct);
            performAction.doubleClick(parentProduct).build().perform();
        } catch (Exception e) {
            CSLogger.error(" product " + productName + " not found", e);
            Assert.fail(" product  " + productName + " not found");
        }
    }

    /**
     * Selects the given class that will be assigned to the parent product
     * folder
     * 
     * @param className String object containing name of the class.
     */
    public void selectClassFromDataSelectionDialogWindow(String className) {
        try {
            selectionDialogWindowInstance.clkBtnControlPaneButtonUserFolder(
                    waitForReload, browserDriver);
            TraverseSelectionDialogFrames
                    .traverseToDataSelectionDialogLeftSection(waitForReload,
                            browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(By.linkText(className)));
            performAction
                    .doubleClick(
                            browserDriver.findElement(By.linkText(className)))
                    .build().perform();
            productPopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            goToRightSectionWindow();
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            CSLogger.info("Class : " + className + "assigned to product folder "
                    + "successfully");
        } catch (Exception e) {
            CSLogger.error("Class not found", e);
            Assert.fail("Class not found");
        }
    }

    /**
     * Returns the WebElement of product.
     * 
     * @param productName String object contains name of product.
     * @return WebElement of product.
     */
    public WebElement getCreatedProduct(String productName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productName)));
        return browserDriver.findElement(By.linkText(productName));
    }

    /**
     * Fills up the parent and child product data to be exported.
     * 
     * @param productName String object contains product name.
     * @param productTabName String object contains product tab name.
     * @param sltAttrName String object contains single line text attribute
     *            name.
     * @param sltAttrValue String object contains single line text attribute
     *            value.
     * @param multiAttrName String object contains multi-line formatted
     *            attribute name.
     * @param multiAttrValue String object contains multi-line formatted
     *            attribute value.
     * @param refToPimAttrName String object contains reference to PIM attribute
     *            name.
     * @param pimReference String object contains reference product name.
     * @param valueListAttrName String object contains value list attribute
     *            name.
     * @param valueListAttrValue String object contains value list attribute
     *            value.
     */
    private void setConfigDataToBeExported(String productName,
            String productTabName, String sltAttrName, String sltAttrValue,
            String multiAttrName, String multiAttrValue,
            String refToPimAttrName, String pimReference,
            String valueListAttrName, String valueListAttrValue) {
        goToPimStudioTreeSection();
        doubleClkOnProduct(productName);
        goToRightSectionWindow();
        clkOnGivenTab(productTabName);
        WebElement sltAttrElement = browserDriver.findElement(By.xpath(
                "//tr[contains(@cs_name,'" + sltAttrName + "')]/td[2]/input"));
        enterTextIntoTextbox(sltAttrElement, sltAttrValue);
        CSUtility.tempMethodForThreadSleep(2000);
        editMultiLineTextAttribute(multiAttrName, multiAttrValue);
        goToRightSectionWindow();
        WebElement refToPimAttrElement = browserDriver
                .findElement(By.xpath("//tr[contains(@cs_name,'"
                        + refToPimAttrName + "')]/td[2]/div/div[2]/img"));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                refToPimAttrElement);
        refToPimAttrElement.click();
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        performAction
                .doubleClick(
                        browserDriver.findElement(By.linkText(pimReference)))
                .perform();
        goToRightSectionWindow();
        WebElement valueListAttrElement = browserDriver.findElement(
                By.xpath("//tr[contains(@cs_name,'" + valueListAttrName
                        + "')]/td[2]/div/select/option[contains(text(),'"
                        + valueListAttrValue + "')]"));
        CSUtility.waitForElementToBeClickable(waitForReload,
                valueListAttrElement);
        valueListAttrElement.click();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCheckIn(waitForReload);
    }

    /**
     * Sets data into multi line formatted text attribute.
     * 
     * @param multiAttrName String object contains multi-line formatted
     *            attribute name.
     * @param multiAttrValue String object contains multi-line formatted
     *            attribute value.
     */
    private void editMultiLineTextAttribute(String multiAttrName,
            String multiAttrValue) {
        List<WebElement> element = browserDriver
                .findElements(By.xpath("//table/tbody/tr[contains(@cs_name,'"
                        + multiAttrName + "')]/td[2]/span"));
        performAction.doubleClick(element.get(0)).perform();
        performAction.doubleClick(element.get(0)).perform();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//table/tbody/tr[contains(@cs_name,'" + multiAttrName
                        + "')]/td[2]/span/div/div/div/iframe")));
        WebElement attributeElement = browserDriver
                .findElement(By.xpath("//html/body"));
        CSUtility.waitForVisibilityOfElement(waitForReload, attributeElement);
        attributeElement.click();
        attributeElement.sendKeys(multiAttrValue);
        CSLogger.info("Multi line text attribute set as " + multiAttrValue);
    }

    /**
     * Clicks on given product tab.
     * 
     * @param tabName String object contains tab.
     */
    private void clkOnGivenTab(String tabName) {
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//nobr[contains(text(),'" + tabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By
                .xpath("//nobr[contains(text(),'" + tabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the " + tabName + " tab");
    }

    /**
     * Spilt's the given data separated from comma.
     * 
     * @param data String object contains data separated by comma.
     * @return String array of data.
     */
    private String[] spiltData(String data) {
        return data.split(",");
    }

    /**
     * This method verifies different section default fields.
     * 
     * @param tableData WebElement contains list of elements
     * @param fields String object contains array of fields to be verified.
     * @param section String object specifies the section. i.e data target,data
     *            source,data transformation.
     */
    private void verifyFields(List<WebElement> tableData, String[] fields,
            String section) {
        CSUtility.tempMethodForThreadSleep(1000);
        ArrayList<String> cellData = new ArrayList<>();
        for (WebElement cell : tableData) {
            cellData.add(cell.getText());
        }
        for (int index = 0; index < fields.length; index++) {

            if (!cellData.contains(fields[index])) {
                CSLogger.error("Field : " + fields[index] + " not found under "
                        + section + " section");
                Assert.fail("Field : " + fields[index] + " not found under "
                        + section + " section");
            }
        }
    }

    /**
     * Selects the given option from drop down.
     * 
     * @param drpDwnElement Drop down WebElement.
     * @param option String object contains option to be selected.
     */
    public void selectDrpDwnOption(WebElement drpDwnElement, String option) {
        CSUtility.waitForVisibilityOfElement(waitForReload, drpDwnElement);
        CSUtility.scrollUpOrDownToElement(drpDwnElement, browserDriver);
        drpDwnElement.click();
        CSUtility.tempMethodForThreadSleep(1000);
        Select element = new Select(drpDwnElement);
        element.selectByVisibleText(option);
        CSLogger.info("Drop down option " + option + " selected");
    }

    /**
     * This method traverse to the PIM module from home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandProductsTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload WebDriverWait Object
     * @param pimStudioProductsNode PimStudioProductsNode Object
     * @param parentProductName String Object containing the value for parent
     *            product Name.
     */
    private void createParentProduct(WebDriverWait waitForReload,
            PimStudioProductsNodePage pimStudioProductsNode,
            String parentProductName) {
        goToPimStudioTreeSection();
        createProduct(parentProductName,
                pimStudioProductsNode.getBtnPimProductsNode(),
                csPopupDivPim.getCsGuiPopupMenuCreateNew());
        CSLogger.info("Created Parent product");
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method creates the child product with the supplied name and the
     * supplied parent product name.
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param childProductName String Object containing the child product name.
     */
    private void createChildProduct(String parentProductName,
            String childProductName) {
        WebElement element = pimStudioProductsNodeInstance
                .expandNodesByProductName(waitForReload, parentProductName);
        createProduct(childProductName, element,
                csPopupDivPim.getCsGuiPopupMenuNewChild());
        CSLogger.info("Created new child product");
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName String object containing name of product it can be
     *            child product or parent product
     * @param nodeElement WebElement of either products node or parent product
     * @param popupMenuOption WebElement containing menu option.
     */
    private void createProduct(String productName, WebElement nodeElement,
            WebElement popupMenuOption) {
        CSUtility.rightClickTreeNode(waitForReload, nodeElement, browserDriver);
        csPopupDivPim.selectPopupDivMenu(waitForReload, popupMenuOption,
                browserDriver);
        csPopupDivPim.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered value " + productName + " in the textfield.");
        csPopupDivPim.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimStudioTreeSection();
    }

    /**
     * Performs operation of clicking on system preference icon.
     */
    private void goToSystemPreferencesIcon() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
    }

    /**
     * Creates the export active script.
     */
    private void createExportActiveScript() {
        goToSystemPreferencesIcon();
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        performAction.doubleClick(activeJobsPageInstance.getNodeActiveJobs())
                .perform();
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csGuiToolbarHorizontalInstance.clkOnWebElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew(),
                "create new");
    }

    /**
     * Configures the export script and data source section.
     * 
     * @param exportLabel String object contains export label.
     * @param exportCategory String object contains export category.
     */
    private void configureExportActiveScript(String exportLabel,
            String exportCategory) {
        createExportActiveScript();
        try {
            traverseToScriptConfiguration();
            activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                    activeJobsPageInstance.getTxtNewActiveScriptLabel(),
                    exportLabel);
            activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                    activeJobsPageInstance.getTxtNewActiveScriptCategory(),
                    exportCategory);
            activeJobsPageInstance.selectActiveScriptOption("Data Flow Export");
        } catch (Exception e) {
            CSLogger.error(
                    "Active script window does not contain fields 'Label', "
                            + "'Category' and 'Script' : test step failed",
                    e);
            Assert.fail("Active script window does not contain fields 'Label', "
                    + "'Category' and 'Script' : test step failed", e);
        }
        configureDataSourceSection(exportData.get("dataSourceType"),
                exportData.get("dataSelectionType"),
                exportData.get("parentProductName"),
                exportData.get("selectionLayer"),
                exportData.get("defaultLanguage"), exportData.get("startCount"),
                exportData.get("maxCount"), exportData.get("batchSize"));
        configureDataTargetSection(exportData.get("fileGeneration"),
                spiltData(exportData.get("dataTargetTypeFields")));
    }

    /**
     * Configures the data target section of export script.
     * 
     * @param fileGenerationOption String object contains file generation
     *            option.
     * @param dataTargetFields String object contains data target fields.
     */
    private void configureDataTargetSection(String fileGenerationOption,
            String dataTargetFields[]) {
        verifyDataTargetSectionFields(dataTargetFields);
        enterTextIntoTextbox(activeJobsPageInstance.getTxtDataTargetFileName(),
                exportData.get("filename"));
        selectDrpDwnOption(
                activeJobsPageInstance.getDrpDwnActivescriptFileGeneration(),
                fileGenerationOption);
        saveExportActiveScript();
    }

    /**
     * Clicks on save button to save the configured export active script.
     */
    private void saveExportActiveScript() {
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * Verifies the fields of data source section.
     * 
     * @param dataSourceFields String object contains data source section
     *            default fields.
     */
    private void verifyDataSourceSectionFields(String[] dataSourceFields) {
        try {
            activeJobsPageInstance
                    .clkWebElement(activeJobsPageInstance.getSecDataSource());
        } catch (Exception e) {
            CSLogger.error("Data source section not found : test step failed");
            Assert.fail("Data source section not found : test step failed");
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//div[@id='content__sections::Data Source']")));
        List<WebElement> defaultFields = browserDriver.findElements(By
                .xpath("//div[@id='content__sections::Data Source']/table/tbody/tr/td"
                        + "[1]"));
        verifyFields(defaultFields, dataSourceFields, "data source");
    }

    /**
     * Configure the data source section of export script.
     * 
     * @param dataSourceType String object contains data source type.
     * @param dataSelectionType String object contains data selection type.
     * @param rootFolderName String object contains name of product folder to be
     *            exported.
     * @param selectionLayer String object contains selection layer.
     * @param defaultLanguage String object contains language.
     * @param startCount String object contains start count.
     * @param maxCount String object contains max count.
     * @param batchSize String object contains batch size of export.
     */
    private void configureDataSourceSection(String dataSourceType,
            String dataSelectionType, String rootFolderName,
            String selectionLayer, String defaultLanguage, String startCount,
            String maxCount, String batchSize) {
        verifyDataSourceSectionFields(
                exportData.get("dataSourceSectionFields").split(","));
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnDataSourceType(),
                dataSourceType);
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnDataSelectionType(),
                dataSelectionType);
        selectRootFolder(rootFolderName);
        traverseToScriptConfiguration();
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnSelectionLayer(),
                selectionLayer);
        selectAttributeToBeExported(exportData.get("attributeFolderName"));
        traverseToScriptConfiguration();
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnSelectDefaultLanguage(),
                defaultLanguage);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtStartCount(), startCount);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtMaxCount(), maxCount);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtBatchSize(), batchSize);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * Switches the frame till edit frames.
     */
    private void traverseToScriptConfiguration() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
    }

    /**
     * Selects the root folder to be exported.
     * 
     * @param productFolderName String object contains product folder name.
     */
    private void selectRootFolder(String productFolderName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPageInstance.getCtnSelectRootFolders());
        activeJobsPageInstance.clkWebElement(
                activeJobsPageInstance.getCtnSelectRootFolders());
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogProductTree(
                waitForReload, browserDriver);
        WebElement rootFolder = browserDriver.findElement(By
                .xpath("//span[contains(text(),'" + productFolderName + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, rootFolder);
        performAction.doubleClick(rootFolder).perform();
        csPopupDivPim.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * Selects the attributes to be exported.
     * 
     * @param attributeFolder String object contains attribute folder name.
     */
    private void selectAttributeToBeExported(String attributeFolder) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By
                        .xpath("(//div[@class='CSGuiSelectionImgChooserContainer"
                                + "']/img)" + "[1]")));
        WebElement attributePlus = browserDriver.findElement(By.xpath(
                "(//div[@class='CSGuiSelectionImgChooserContainer']/img)[1]"));
        attributePlus.click();
        CSLogger.info("Clicked on '+' to add attributes");
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmDataSelectionDialogNestedOne()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmIdRecordsPdmarticleconfigurationBottom()));
        WebElement labelAttribute = browserDriver
                .findElement(By.xpath("//td[contains(text(),'Label')]"));
        CSUtility.waitForElementToBeClickable(waitForReload, labelAttribute);
        labelAttribute.click();
        CSLogger.info("Standard attribute label selected");
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogLeftSection(
                waitForReload, browserDriver);
        selectionDialogWindowInstance.clkOnGivenWebElement(waitForReload,
                selectionDialogWindowInstance.getDrpDwnAttributes());
        clkOnGivenAttributeFolder(attributeFolder);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogCenterSection(waitForReload,
                        browserDriver);
        csGuiToolbarHorizontalInstance.clkOnWebElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarAddAllValues(),
                "add all values");
        modalDialogPopupWindowInstance
                .handleMessageModalDialogWindow(waitForReload, true);
        CSUtility.tempMethodForThreadSleep(2000);
        modalDialogPopupWindowInstance.askBoxWindowOperation(waitForReload,
                true, browserDriver);
    }

    /**
     * Returns the attribute folder element.
     * 
     * @param attributeFolder String object contains attribute folder name.
     * @return WebElement attribute folder.
     */
    private WebElement getAttributeFolder(String attributeFolder) {
        WebElement attributeFolderElement = browserDriver
                .findElement(By.linkText(attributeFolder));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                attributeFolderElement);
        return attributeFolderElement;
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param productName String containing name of product
     */
    private String getProductId(String productName) {
        goToPimStudioTreeSection();
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(browserDriver.findElement(By.linkText(productName)))
                .perform();
        goToRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        String productId = browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
        return productId;
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * Executes the export script from PIM studio.
     * 
     * @param allowAccessOption String object contains allow access option.
     * @param exportScriptName String object contains export script name.
     */
    private void verifyExecutionFromPimStudioExport(String allowAccessOption,
            String exportScriptName) {
        activeJobsPageInstance.checkContextSensitiveCheckBox();
        setAllowAccessByField("Automation", allowAccessOption);
        executeScriptFromPimStudioTree(exportScriptName, false, "Properties",
                "Automation");
        executeScriptFromPimStudioTree(exportScriptName, true, "Properties",
                "Automation");
    }

    /**
     * Sets the Allow Access By field to given allow access option.
     * 
     * @param automationTabName String object contains tab name.
     * @param allowAccessOption String object contains allow access option.
     */
    private void setAllowAccessByField(String automationTabName,
            String allowAccessOption) {
        clkOnGivenTab(automationTabName);
        WebElement drpDwnAllowAccess = browserDriver.findElement(By
                .xpath("//select[contains(@id,'ActivescriptUserAccessType')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, drpDwnAllowAccess);
        selectDrpDwnOption(drpDwnAllowAccess, allowAccessOption);
        saveExportActiveScript();
    }

    /**
     * This method executes the export script from PIM studio.
     * 
     * @param scriptName String object contains export script name.
     * @param isPressOkay Boolean parameter specifies whether to run the script
     *            or not.
     * @param propertiesScriptTab String object contains tab name.
     * @param automationScriptTab String object contains automation tab name.
     */
    private void executeScriptFromPimStudioTree(String scriptName,
            Boolean isPressOkay, String propertiesScriptTab,
            String automationScriptTab) {
        String parentWindow = browserDriver.getWindowHandle();
        switchToProductsPage();
        goToPimStudioTreeSection();
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        CSUtility.rightClickTreeNode(waitForReload, getTargetProduct(),
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getcsGuiPopupMenuRefresh(), browserDriver);
        CSUtility.tempMethodForThreadSleep(3000);
        goToPimStudioTreeSection();
        CSUtility.rightClickTreeNode(waitForReload, getTargetProduct(),
                browserDriver);
        productPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupProcess());
        productPopup.hoverOnCsGuiPopupSubMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupExecute());
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCSPopupDivSubSubFrame()));
        WebElement scriptOption = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + scriptName + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, scriptOption);
        scriptOption.click();
        CSLogger.info("Select script option: " + scriptName + " from popup");
        verifyElementsOfExecuteWindow(propertiesScriptTab, automationScriptTab);
        if (isPressOkay) {
            performActionsOnWindows(parentWindow);
            verifyExportedExcelFileData();
        } else {
            WebElement cancelButton = browserDriver.findElement(By.xpath(
                    "//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL']"));
            clkOnGivenButton(cancelButton, "Cancel");
            Set<String> executeWindow = browserDriver.getWindowHandles();
            if (executeWindow.size() == 0) {
                CSLogger.error(
                        "Export script executed when clicked on cancel button");
                softAssertion.fail(
                        "Export script executed when clicked on cancel button");
            }
        }
    }

    /**
     * Perform actions on export script windows.
     * 
     * @param parentWindow String object contains parent window handle.
     */
    private void performActionsOnWindows(String parentWindow) {
        WebElement okButton = browserDriver.findElement(By
                .xpath("//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_OK']"));
        clkOnGivenButton(okButton, "OK");
        CSUtility.tempMethodForThreadSleep(3000);
        String childWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(childWindow);
        CSLogger.info("switched to child window");
        CSUtility.switchToDefaultFrame(browserDriver);
        WebElement moreDetailsButton = browserDriver
                .findElement(By.xpath("//input[@value='More Details']"));
        clkOnGivenButton(moreDetailsButton, "More Details");
        CSUtility.tempMethodForThreadSleep(2000);
        String subChildWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(subChildWindow);
        CSLogger.info("Switched to subchild window");
        printActiveScriptLogs();
        downloadExportedFile();
        WebElement lessDetailsButton = browserDriver
                .findElement(By.xpath("//input[@value='Less Details']"));
        clkOnGivenButton(lessDetailsButton, "Less Details");
        CSUtility.tempMethodForThreadSleep(2000);
        browserDriver.switchTo().window(
                (String) (browserDriver.getWindowHandles().toArray())[1]);
        browserDriver.close();
        browserDriver.switchTo().window(parentWindow);
        CSLogger.info("Switched to parent window");
    }

    /**
     * Downloads the exported file from active script job logs.
     */
    private void downloadExportedFile() {
        WebElement downloadFileLink = browserDriver
                .findElement(By.xpath("//a[@class='result-link']"));
        downloadFileLink.click();
        exportedFileName = downloadFileLink.getText();
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * Prints the active script job logs to logger file.
     * 
     */
    private void printActiveScriptLogs() {
        WebElement logTable = browserDriver
                .findElement(By.xpath("//table[@id='ActiveScriptLogTable']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, logTable);
        WebElement numOfMessages = browserDriver
                .findElement(By.id("ActiveScriptMessageCount"));
        CSUtility.waitForVisibilityOfElement(waitForReload, numOfMessages);
        selectDrpDwnOption(numOfMessages, "All Messages");
        List<WebElement> activeJobLogs = browserDriver.findElements(
                By.xpath("//table[@id='ActiveScriptLogTable']/tbody/tr"));
        CSLogger.info("Time" + "\t" + "Total" + "\t" + "Diff" + "\t"
                + "All Messages");
        for (WebElement cell : activeJobLogs) {
            CSLogger.info(cell.getText());
        }
    }

    /**
     * Clicks on given button
     * 
     * @param button WebElement of button to be clicked.
     * @param label String object contains label.
     */
    private void clkOnGivenButton(WebElement button, String label) {
        CSUtility.waitForElementToBeClickable(waitForReload, button);
        button.click();
        CSLogger.info("Clicked on " + label + " button ");
    }

    /**
     * Clicks on products header.
     */
    private void switchToProductsPage() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
    }

    /**
     * Verifies elements on execute window.
     * 
     * @param propertiesScriptTab String object contains tab name.
     * @param automationScriptTab String object contains tab name.
     */
    private void verifyElementsOfExecuteWindow(String propertiesScriptTab,
            String automationScriptTab) {
        traverseToExecuteWindowFrames();
        int presetElementExists = verifyExecuteWindowDefaultFields();
        verifyMoreOptionsButton(propertiesScriptTab, automationScriptTab,
                presetElementExists);
        verifyLessOptionsButton(propertiesScriptTab, automationScriptTab);
    }

    /**
     * Traverses frames of execute window.
     */
    private void traverseToExecuteWindowFrames() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCSPortalWindow()));
        CSLogger.info("Traversed till CSPortalWindow frame");
    }

    /**
     * Verifies the fields when clicked on less option button.
     * 
     * @param propertiesScriptTab String object contains tab name.
     * @param automationScriptTab String object contains tab name.
     */
    private void verifyLessOptionsButton(String propertiesScriptTab,
            String automationScriptTab) {
        WebElement lessOptionsButton = browserDriver
                .findElement(By.xpath("//input[@value='Less Options']"));
        clkOnGivenButton(lessOptionsButton, "Less Options");
        verifyHiddenTabs(propertiesScriptTab, automationScriptTab);
        verifyExecuteWindowDefaultFields();
    }

    /**
     * Verifies that when less option button is clicked the properties and
     * automation tab should hide.
     * 
     * @param propertiesScriptTab String object contains properties tab name.
     * @param automationScriptTab String object contains automation tab name.
     */
    private void verifyHiddenTabs(String propertiesScriptTab,
            String automationScriptTab) {
        int propertiesTab = browserDriver.findElements(By.xpath(
                "//nobr[contains(text(),'" + propertiesScriptTab + "')]"))
                .size();
        int automationTab = browserDriver.findElements(By.xpath(
                "//nobr[contains(text(),'" + automationScriptTab + "')]"))
                .size();
        if (propertiesTab != 0 && automationTab != 0) {
            CSLogger.error(
                    "Tabs didn't hide when clicked on less option button : "
                            + "verification failed");
            softAssertion
                    .fail("Tabs didn't hide when clicked on less option button"
                            + " : " + "verification failed");
        }
    }

    /**
     * Verifies the fields when clicked on more option button.
     * 
     * @param propertiesScriptTab String object contains tab name.
     * @param automationScriptTab String object contains tab name.
     * @param presetElementExists Integer parameter holds integer value to check
     *            whether element exists.
     */
    private void verifyMoreOptionsButton(String propertiesScriptTab,
            String automationScriptTab, int presetElementExists) {
        WebElement moreOptions = browserDriver
                .findElement(By.xpath("//input[@value='More Options']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, moreOptions);
        moreOptions.click();
        CSLogger.info("Clicked on more options button");
        clkOnGivenTab(propertiesScriptTab);
        int contextSensitiveElement = browserDriver
                .findElements(By
                        .xpath("//span[contains(text(),'Context Sensitive')]"))
                .size();
        int onlineHelpElement = browserDriver
                .findElements(By.xpath("//div[contains(text(),'Online Help')]"))
                .size();
        if (!(presetElementExists != 0 && contextSensitiveElement != 0
                && onlineHelpElement != 0)) {
            CSLogger.error(
                    "Fields not found on " + propertiesScriptTab + " tab");
            softAssertion.fail(
                    "Fields not found on " + propertiesScriptTab + " tab");
        }
        try {
            clkOnGivenSection(activeJobsPageInstance.getSecDataSource(),
                    "Data Source");
            clkOnGivenSection(activeJobsPageInstance.getSecDataTarget(),
                    "Data Target");
            clkOnGivenSection(activeJobsPageInstance.getSecDataTransformation(),
                    "Data Transformation");
        } catch (Exception e) {
            CSLogger.error(
                    "Section not found on" + propertiesScriptTab + " tab", e);
            softAssertion.fail(
                    "Section not found on" + propertiesScriptTab + " tab", e);
        }
        clkOnGivenTab(automationScriptTab);
        List<WebElement> rows = browserDriver.findElements(By.xpath(
                "//div[@cs_name='Automation']/div/table/tbody//tr/td/table/tbody/tr"));
        int allowAccessByElementExists = browserDriver
                .findElements(By.xpath("//tr[@cs_name='Allow Access by']"))
                .size();
        if (!(rows.size() == 1 && allowAccessByElementExists != 0)) {
            CSLogger.error("Only allow access by field should display on "
                    + automationScriptTab + " tab : verification failed");
            softAssertion.fail("Only allow access by field should display on "
                    + automationScriptTab + " tab : verification failed");
        }
    }

    /**
     * Verifies the execute window default fields.
     * 
     * @return Integer presetElementExists - contains integer value.
     */
    private int verifyExecuteWindowDefaultFields() {
        WebElement windowTitleElement = browserDriver.findElement(By
                .xpath("//td[@class='ListHeader CSGUI_MODALDIALOG_SUBTITLE']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, windowTitleElement);
        softAssertion.assertEquals(windowTitleElement.getText(),
                "Chose your execution options:");
        int presetElementExists = browserDriver
                .findElements(By.xpath("//tr[@cs_name='Preset']")).size();
        int dataSourceTypeElementExists = browserDriver
                .findElements(By.xpath("//tr[@cs_name='Data Source Type']"))
                .size();
        int rootFolderElement = browserDriver
                .findElements(By.xpath("//tr[@cs_name='Root Folders']")).size();
        if (!(presetElementExists != 0 && dataSourceTypeElementExists != 0
                && rootFolderElement != 0)) {
            CSLogger.error("Elements not found on execute window");
            softAssertion.fail("Elements not found on execute window");
        }
        return presetElementExists;
    }

    /**
     * Returns the target product element.
     * 
     * @return WebElement targetProduct
     */
    private WebElement getTargetProduct() {
        WebElement targetProduct = browserDriver
                .findElement(By.linkText(exportData.get("parentProductName")));
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload, targetProduct);
        return targetProduct;
    }

    /**
     * Clicks on given section.
     * 
     * @param section WebElement of section field.
     * @param sectionName String object contains name of section.
     */
    private void clkOnGivenSection(WebElement section, String sectionName) {
        CSUtility.scrollUpOrDownToElement(section, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload, section);
        if (section.getAttribute("class").contains("section_open")) {
            CSLogger.info("Section " + sectionName + "is already expanded");
        } else {
            section.click();
            CSLogger.info("Expanded" + sectionName);
        }
    }

    /**
     * Verifies the data of exported XLSX file.
     */
    private void verifyExportedExcelFileData() {
        checkExportedFileExists();
        Object[] exportedProductData;
        Object[] actualProductData;
        actualData = new ArrayList<>();
        expectedData = new ArrayList<>();
        readActualProductData();
        readExportedDataFromExcelFile(fileLocation + exportedFileName);
        exportedProductData = expectedData.toArray();
        actualProductData = actualData.toArray();
        for (int index = 0; index < actualProductData.length; index++) {
            if (!(actualProductData[index]
                    .equals(exportedProductData[index]))) {
                CSLogger.error(actualProductData[index]
                        + " product content not found in exported file"
                        + exportedFileName);
                softAssertion.fail(actualProductData[index]
                        + " product content not found in exported file "
                        + exportedFileName);
            }
        }
        if (expectedData.equals(actualData)) {
            CSLogger.info("Excel file " + exportedFileName
                    + " successfully exported ");
        } else {
            CSLogger.error("Data export failed ,please " + "check exported "
                    + exportedFileName + " file content");
            softAssertion.fail("Data export failed ,please " + "check exported "
                    + exportedFileName + " file content");
        }
    }

    /**
     * Checks whether the exported file exists.
     */
    private void checkExportedFileExists() {
        fileLocation = System.getProperty("user.home") + "\\Downloads\\";
        File exportedFile = new File(fileLocation + exportedFileName);
        if (!exportedFile.exists()) {
            CSLogger.error("Exported file doesnt exist in download folder. ["
                    + fileLocation + exportedFileName + "]");
            Assert.fail("Exported file doesnt exist in download folder. ["
                    + fileLocation + exportedFileName + "]");
        }
    }

    /**
     * Configures the actual data to be exported.
     */
    private void readActualProductData() {
        actualHeaders = new ArrayList<>();
        ArrayList<String> productData = null;
        String exportedAttributes[] = spiltData(
                exportData.get("attributesName"));
        actualHeaders.add("Label");
        for (int i = 0; i < exportedAttributes.length; i++) {
            actualHeaders.add(exportedAttributes[i]);
        }
        actualData.addAll(actualHeaders);
        String testData[] = { exportData.get("parentProductData"),
                exportData.get("firstChildProductData"),
                exportData.get("secondChildProductData") };
        for (int i = 0; i < testData.length; i++) {
            String spilttedData[] = spiltData(testData[i]);
            String multiTextData = "<p>" + spilttedData[2] + "</p>";
            String refToPimData = "Pdmarticle:" + referenceProductId;
            productData = new ArrayList<String>(Arrays.asList(spilttedData));
            productData.set(2, multiTextData);
            productData.add(3, refToPimData);
            actualData.addAll(productData);
        }
    }

    /**
     * Reads the data from exported XLSX file.
     * 
     * @param sheetName String object contains sheetName.
     * @param filePath String object contains file path.
     * @return ArrayList<String> contains exported data.
     */
    private void readExportedDataFromExcelFile(String filePath) {
        try {
            excelWorkBook = new XSSFWorkbook(
                    new FileInputStream(new File(filePath)));
            excelSheet = excelWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = excelSheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (!(cell.getStringCellValue().isEmpty())) {
                        expectedData.add(cell.getStringCellValue());
                    }
                }
            }
            excelWorkBook.close();
        } catch (Exception e) {
            CSLogger.error("Error while reading from file : " + filePath, e);
            Assert.fail("Error while reading from file : " + filePath, e);
        }

    }

    /**
     * Verifies the fields of data source section.
     * 
     * @param dataSourceFields String object contains data source section
     *            default fields.
     */
    private void verifyDataTargetSectionFields(String[] dataTargetFields) {
        try {
            activeJobsPageInstance
                    .clkWebElement(activeJobsPageInstance.getSecDataTarget());
        } catch (Exception e) {
            CSLogger.error("Data target section not found test step failed");
            Assert.fail("Data target section not found test step failed");
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//div[@id='content__sections::Data Target']")));
        selectDrpDwnOption(activeJobsPageInstance.getDrpDwnDataTargetType(),
                "Excel - one XLSX file with all records");
        List<WebElement> defaultFields = browserDriver.findElements(By
                .xpath("//div[@id='content__sections::Data Target']/table/tbody/tr/td"
                        + "[1]"));
        verifyFields(defaultFields, dataTargetFields, "data Target");
    }

    /**
     * This data provider method returns test data i.e
     * ParentProductName,FirstChildProductName, SecondChildProductName,
     * AttributeFolderName, AttributesName, AttributeType, AttributeValue,
     * ClassName, ParentProductData, FirstChildProductData,
     * SecondChildProductData, ProductTab, RefToPimAttrName, ExportLabel,
     * ExportCategory, DataSourceSectionFields, DataSourceType,
     * DataSelectionType, SelectionLayer, DefaultLanguage, StartCount, MaxCount,
     * BatchSize,Filename,FileGeneration,DataTargetFields,VolumeFolderName,
     * MamFolder,AllowAccessField
     * 
     * @return Array String array consisting of export data
     */

    @DataProvider(name = "exportPimStudioTestData")
    public Object[][] getExportTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("dataflowExportModuleTestCases"),
                testDataSheet);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        activeJobsPageInstance = SettingsLeftSectionMenubar
                .getActiveJobsNode(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        performAction = new Actions(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        csPopupDivPim = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        csPopupDivPim = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
    }
}
