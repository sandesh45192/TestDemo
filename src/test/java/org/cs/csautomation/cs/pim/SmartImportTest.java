/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SmartImportWindowPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to test the PIM Smart Import
 * configurations.
 * 
 * @author CSAutomation Team
 *
 */
public class SmartImportTest extends AbstractTest {

    private CSGuiToolbarHorizontal    csGuiToolbarHorizontal;
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPopupDivPim             csPopupDiv;
    private PimStudioSettingsNode     pimStudioSettingsNode;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private SmartImportWindowPage     importWindowPage;
    private FrameLocators             iframeLocatorsInstance;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private IAttributePopup           attributePopup;
    private WebDriverWait             waitForReload;
    private Actions                   action;
    private IClassPopup               classPopUp;
    private IProductPopup             productPopUp;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private IVolumePopup              demoVolumePopup;
    private String                    importFileName       = "AutomationSmartImport.xlsx";
    private String                    smartImportSheetName = "SmartImportConfiguration";
    private String                    downloadLocation;
    private String[][]                importFileData;
    private String[]                  standardAttr;
    private String                    productName          = "Automation test Product";
    private String                    importSheetName      = "Import";
    private String                    productClassName;

    /**
     * This method test the import file for Smart import.
     * 
     * @param externalKey String object containing the external key for import
     *            product
     * @param classId String object containing the class external key.
     * @param name String object containing the name for the import product.
     * @param pimProduct String object containing the Pim product name.
     * @param mamFile String object containing the MAM file name.
     * @param filePath String oject contains MAM file path
     * @param valueListValue String object containing the value list value ID.
     * @param refToMamAttr String object containing the name of mam ref
     *            attribute.
     * @param refToPimAttr String object containing the name of pim ref
     *            attribute.
     * @param valueListAttr String object containing the name of value list
     *            attribute.
     * @param className String object containing the name of the class to assign
     *            the test product.
     * @param attrFolder String object contains attribute folder name
     * @param attributeName String object contains attribute name
     * @param attributeValue String object contains attribute value
     * @param attributefield String object contains attribute field
     */
    @Test(
            dataProvider = "SmartImportConfigTest",
            groups = { "Smart Import Config test" })
    public void testSmartImportFile(String externalKey, String classId,
            String name, String pimProduct, String mamFile, String filePath,
            String numberUnitValue, String numberSuffixValue,
            String valueListValue, String refToMamAttr, String refToPimAttr,
            String numberUnitAttr, String numberSuffixAttr,
            String valueListAttr, String className, String attrFolder,
            String attributeName, String attributeValue,
            String attributefield) {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            createPreRequisiteData(classId, className, attrFolder,
                    attributeName, attributeValue, attributefield);
            String refToMamValue = getMAMFileId(mamFile, filePath);
            String refToPimValue = getPimProductId(pimProduct);
            initializeRequiredData(externalKey, classId, name, refToMamValue,
                    refToPimValue, numberUnitValue, numberSuffixValue,
                    valueListValue, refToMamAttr, refToPimAttr, numberUnitAttr,
                    numberSuffixAttr, valueListAttr, className);
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(importSheetName);
            Row headerRow = sheet.createRow(0);
            Row dataRow = sheet.createRow(1);
            int columnCount = -1;
            for (String[] importFileRow : importFileData) {
                Cell headerCell = headerRow.createCell(++columnCount);
                Cell dataCell = dataRow.createCell(columnCount);
                headerCell.setCellValue(importFileRow[0]);
                dataCell.setCellValue(importFileRow[1]);
            }
            try (FileOutputStream outputStream = new FileOutputStream(
                    downloadLocation + importFileName)) {
                workbook.write(outputStream);
            } catch (IOException e) {
                CSLogger.debug("Import File creation failed", e);
                Assert.fail("Import File creation failed");
            }
            workbook.close();
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method :testSmartImportFile ",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * This method create product and return it's id
     * 
     * @param pimProduct String object containing the Pim product name
     * @return id String contains id of product
     */
    private String getPimProductId(String pimProduct) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        createProduct(pimProduct);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productElement = browserDriver
                .findElement(By.linkText(pimProduct));
        productElement.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getTxtAttributeId());
        String pimProductId = csGuiDialogContentId.getTxtAttributeId()
                .getAttribute("value");
        return pimProductId;
    }

    /**
     * This method create product.
     * 
     * @param pimProduct String object contains product name
     */
    private void createProduct(String pimProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, pimProduct);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Product Created");
    }

    /**
     * This method upload file to MAM an get it's id
     * 
     * @param mamFile String object containing the MAM file name.
     * @param filePath String oject contains MAM file path
     * @return id String contains id of MAM file
     * @throws Exception
     */
    private String getMAMFileId(String mamFile, String filePath)
            throws Exception {
        filePath = getFilePath(filePath);
        csPortalHeaderInstance.clkBtnMedia(waitForReload);
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        WebElement demoNode = browserDriver.findElement(By.linkText("Demo"));
        CSUtility.rightClickTreeNode(waitForReload, demoNode, browserDriver);
        demoVolumePopup.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getClkUploadNewFile(), browserDriver);
        addFileFromDialogWindow(filePath);
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        WebElement fileName = browserDriver.findElement(By.linkText(mamFile));
        fileName.click();
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getTxtAttributeId());
        String mamFileId = csGuiDialogContentId.getTxtAttributeId()
                .getAttribute("value");
        return mamFileId;
    }

    /**
     * This method uploads File to the dialog window
     * 
     * @param filePath contains the path of the xml file wherever the file has
     *            stored in computer drive
     */
    private void addFileFromDialogWindow(String filePath) throws Exception {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                mamStudioVolumesNode.getBrowseFileToUpload()));
        mamStudioVolumesNode.getBrowseFileToUpload().click();
        addFilePath(filePath);
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                mamStudioVolumesNode.getUploadButtonImage()));
        mamStudioVolumesNode.getUploadButtonImage().click();
        CSUtility.tempMethodForThreadSleep(2000);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getClkCloseAfterUpload()));
        mamStudioVolumesNode.getClkCloseAfterUpload().click();
        CSLogger.info("File assigned successfully.");
    }

    /**
     * This method adds image to the clipboard and then to the test folder by
     * clicking enter
     * 
     * @param filePath contains path of the xls file
     * 
     */
    private void addFilePath(String filePath) throws Exception {
        StringSelection select = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(select,
                null);
        CSUtility.tempMethodForThreadSleep(2000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        CSUtility.tempMethodForThreadSleep(2000);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        CSLogger.info("Image added to the test folder");
    }

    /**
     * This method get the file path of current system
     * 
     * @param filePath String object contains path from sheet
     * @return path String contains current system file path
     */
    private String getFilePath(String filePath) {
        String currentPath = System.getProperty("user.dir");
        String path = currentPath + filePath;
        return path;
    }

    /**
     * This method create the pre-requisit data for import test
     * 
     * @param classId String object containing the class external key.
     * @param className String object containing the name of the class to assign
     *            the test product.
     * @param attrFolder String object contains attribute folder name
     * @param attributeName String object contains attribute name
     * @param attributeValue String object contains attribute value
     * @param attributefield String object contains attribute field
     */
    private void createPreRequisiteData(String classId, String className,
            String attrFolder, String attributeName, String attributeValue,
            String attributefield) {
        String[] attributeArray = attributeName.split(",");
        String[] attrValueArray = attributeValue.split(",");
        String[] fieldTypeArray = attributefield.split(",");
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        createAttributeFolder(attrFolder);
        for (int index = 0; index < fieldTypeArray.length; index++) {
            createVariousAttributeFields(attrFolder, attributeArray[index],
                    attrValueArray[index], fieldTypeArray[index]);
        }
        createClass(classId, className);
        dragAndDropAttrToClass(attrFolder, className);
    }

    /**
     * This method drag and drop attribute to class
     * 
     * @param folderName String object attribute folder name
     * @param className String object contains class name
     */
    private void dragAndDropAttrToClass(String folderName, String className) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode().click();
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement attrFolderToDragDrop = browserDriver
                .findElement(By.linkText(folderName));
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        action.dragAndDrop(attrFolderToDragDrop, classNameToDragDrop).build()
                .perform();
        CSLogger.info("Drag and Drop attribute to class");
    }

    /**
     * This method create class.
     * 
     * @param className String object contains class name
     */
    private void createClass(String classId, String className) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode(),
                browserDriver);
        classPopUp.selectPopupDivMenu(waitForReload,
                classPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        classPopUp.enterValueInDialogue(waitForReload, className);
        classPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Class Created");
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        WebElement classElment = browserDriver
                .findElement(By.linkText(className));
        classElment.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        sendTextToEelment(csGuiDialogContentIdInstance
                .getTxtPdmarticleconfigurationName(), classId);
        sendTextToEelment(csGuiDialogContentIdInstance
                .getTxtPdmarticleconfigurationLabel(), className);
        sendTextToEelment(csGuiDialogContentIdInstance
                .getTxtPdmarticleconfigurationExternalKey(), classId);
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
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
        CSUtility.tempMethodForThreadSleep(20000);
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
        sendTextToEelment(
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationExternalKey(),
                attributeName);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentIdInstance
                        .getDdPdmarticleconfigurationIsInherited());
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
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
     * This method select the attribute type and assign it to the attribute
     * 
     * @param attributeValue String object contains attribute value name
     * @param fieldType String object contains attribute types
     */
    private void selectAttributeType(String attrValue, String fieldType) {
        String fieldName = null;
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
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
        CSUtility.tempMethodForThreadSleep(4000);
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
     * This method initializes the data members with the values received from
     * data providers.
     * 
     * @param externalKey String object containing the external key for import
     *            product
     * @param classId String object containing the class external key.
     * @param name String object containing the name for the import product.
     * @param refToMamValue String object containing the mam product ID.
     * @param refToPimValue String object containing the pim product ID.
     * @param valueListValue String object containing the value list value ID.
     * @param refToMamAttr String object containing the name of mam ref
     *            attribute.
     * @param refToPimAttr String object containing the name of pim ref
     *            attribute.
     * @param valueListAttr String object containing the name of value list
     *            attribute.
     * @param className String object containing the name of the class to assign
     *            the test product.
     */
    private void initializeRequiredData(String externalKey, String classId,
            String name, String refToMamValue, String refToPimValue,
            String numberUnitValue, String numberSuffixValue,
            String valueListValue, String refToMamAttr, String refToPimAttr,
            String numberUnitAttr, String numberSuffixAttr,
            String valueListAttr, String className) {
        standardAttr = new String[] { "External Key", "Classes", "Label",
                refToMamAttr, refToPimAttr, numberUnitAttr, numberSuffixAttr,
                valueListAttr };
        importFileData = new String[][] { { "External key", externalKey },
                { "Class", classId }, { "Name", name },
                { "Ref to MAM", refToMamValue },
                { "Ref to PIM", refToPimValue },
                { "Number Unit", numberUnitValue },
                { "Number Suffix", numberSuffixValue },
                { "Value List", valueListValue } };
        downloadLocation = new String(
                System.getProperty("user.home") + "\\Downloads\\");
        productClassName = className;
    }

    /**
     * Test method to test Smart import configuration for PIM.
     */
    @Test(
            dependsOnMethods = { "testSmartImportFile" },
            groups = { "Smart Import Config test" })
    public void testConfigurationOfSmartImportForProducts() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            switchToPIM(waitForReload);
            expandProductTree();
            createProduct();
            switchToPIM(waitForReload);
            assignClassToTestProduct();
            productContextClick();
            doProductCheckIn();
            createChildProduct();
            selectSmartImport();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            iframeLocatorsInstance.getFrmCsGuiDialog()));
            WebElement uploadFileElement = importWindowPage
                    .getUplaoadImportFile();
            uploadFileElement.sendKeys(downloadLocation + importFileName);
            importWindowPage.getDdImportLanguage().sendKeys("English");
            importWindowPage.getDdMappingSet().sendKeys("Sample");
            verifySmartImportConfigWindow();
            importWindowPage.getBtnNext().click();
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method : testConfigurationOfSmartImportForProducts",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * Test method to test Smart import mapping for PIM.
     */
    @Test(
            dependsOnMethods = { "testConfigurationOfSmartImportForProducts" },
            groups = { "Smart Import Config test" })
    public void testMappingOfSmartImportForProducts() {
        try {
            verifySmartImportMappingWindow();
            for (int index = 0; index < standardAttr.length; index++) {
                dragDropMapping(importFileData[index][0], standardAttr[index]);
                CSUtility.tempMethodForThreadSleep(1000);
            }
            verifyMappings();
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method :testMappingOfSmartImportForProducts",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * This method test smart import mapping combination.
     */
    @Test(
            dependsOnMethods = { "testMappingOfSmartImportForProducts" },
            groups = { "Smart Import Config test" })
    public void testMappingCombinationOfSmartImportForProducts() {
        try {
            dragDropMapping(importFileData[0][0], "$Name");
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement editedMapping = browserDriver.findElement(
                    By.xpath("//div[contains(text(),'$Name, $External key')]"));
            Assert.assertNotNull(editedMapping,
                    "Additional Scenarios- Editing mapping Failed !");
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method : testMappingCombinationOfSmartImportForProducts",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * Test method to test Smart import with number type attributes.
     */
    @Test(
            dependsOnMethods = {
                    "testMappingCombinationOfSmartImportForProducts" },
            groups = { "Smart Import Config test" })
    public void testSmartImportDryImport() {
        try {
            importWindowPage.getBtnDryImport().click();
            CSLogger.info("Clicked on the dry import.");
            CSUtility.tempMethodForThreadSleep(4000);
            verifyImport();
            importWindowPage.getBtnBackToImportMapping().click();
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method : testSmartImportDryImport",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * Test method to test Smart import with number type attributes.
     */
    @Test(
            dependsOnMethods = { "testSmartImportDryImport" },
            groups = { "Smart Import Config test" })
    public void testSmartImportNumberAttributes() {
        try {
            importWindowPage.getBtnImport().click();
            CSUtility.tempMethodForThreadSleep(3000);
            verifyImport();
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method :testSmartImportNumberAttributes",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * This method test the store and delete use case for smart import mapping.
     */
    @Test(
            dependsOnMethods = { "testSmartImportNumberAttributes" },
            groups = { "Smart Import Config test" })
    public void testStoreAndDeleteMappingOfSmartImportForProducts() {
        try {
            String mappingName = "QA Mapping";
            importWindowPage.getBtnBackToImportMapping().click();
            CSUtility.tempMethodForThreadSleep(1000);
            importWindowPage.getBtnStoreMapping().click();
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    importWindowPage.getInputStoreMappingName()));
            importWindowPage.getInputStoreMappingName().sendKeys(mappingName);
            CSUtility.tempMethodForThreadSleep(1000);
            importWindowPage.getBtnStoreMappingOk().click();
            CSLogger.info("Saved mapping");
            CSUtility.switchToDefaultFrame(browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            importWindowPage.getBtnClose().click();
            goToSmartImport();
            configureSmartImportWindowFields(mappingName);
            verifyMappings();
            importWindowPage.getBtnImport().click();
            CSUtility.tempMethodForThreadSleep(3000);
            verifyImport();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.switchToDefaultFrame(browserDriver);
            importWindowPage.getBtnClose().click();
            CSLogger.info("Closed smart import window.");
            goToSmartImport();
            importWindowPage.getDdMappingSet().sendKeys(mappingName);
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement btnDeleteMapping = browserDriver
                    .findElement(By.xpath("//div[text()='" + mappingName
                            + "']/parent::div/div/button"));
            btnDeleteMapping.click();
            WebElement lblConfirmationBox = browserDriver.findElement(By
                    .xpath("//div[contains(text(),'Do you really want to delete "
                            + "the mapping set')]"));
            Assert.assertNotNull(lblConfirmationBox,
                    "Delete Pre-Confimation not found !");
            importWindowPage.getBtnStoreMappingOk().click();
            CSLogger.info("Deleted mapping.");
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.switchToDefaultFrame(browserDriver);
            importWindowPage.getBtnClose().click();
            CSLogger.info("Closed smart import window.");
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method :testStoreAndDeleteMappingOfSmartImportForProducts",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * Test method to test Smart import with number type attributes.
     */
    @Test(
            dependsOnMethods = {
                    "testStoreAndDeleteMappingOfSmartImportForProducts" },
            groups = { "Smart Import Config test" })
    public void testSmartImportProducts() {
        try {
            clickOnProduct(productName);
            selectRefresh();
            CSUtility.tempMethodForThreadSleep(2000);
            clickOnProduct(importFileData[2][1]);
            CSUtility.tempMethodForThreadSleep(2000);
            clickOnTheGivenProductTab("Data", waitForReload);
            WebElement RefToMamElement = browserDriver
                    .findElement(By.xpath("//tr[contains(@cs_name,'"
                            + standardAttr[3] + "')]/td[2]/div/div/div/input"));
            Assert.assertTrue(
                    RefToMamElement.getAttribute("value").contains(
                            "\"FileID\":\"" + importFileData[3][1] + "\""),
                    "Wrong value for MAM reference.");
            WebElement RefToPimElement = browserDriver
                    .findElement(By.xpath("//tr[contains(@cs_name,'"
                            + standardAttr[4] + "')]/td[2]/div/div/div/input"));
            Assert.assertTrue(
                    RefToPimElement.getAttribute("value").contains(
                            "\"RecordID\":\"" + importFileData[4][1] + "\""),
                    "Wrong value for PIM reference.");
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method :testSmartImportProducts",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * Test method to test Smart import with update and delete case.
     */
    @Test(
            dependsOnMethods = { "testSmartImportProducts" },
            groups = { "Smart Import Config test" })
    public void testUpdateAndDeleteScenario() {
        try {
            clickOnProduct("New Element");
            clickOnTheGivenProductTab("Data", waitForReload);
            updateImportFile();
            goToSmartImport();
            configureSmartImportWindowFields("Sample");
            CSUtility.tempMethodForThreadSleep(1000);
            for (int index = 0; index < standardAttr.length; index++) {
                if (importFileData[index][0].contains("Number")) {
                    continue;
                }
                dragDropMapping(importFileData[index][0], standardAttr[index]);
                CSUtility.tempMethodForThreadSleep(1000);
            }
            CSUtility.tempMethodForThreadSleep(4000);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    importWindowPage.getBtnImport());
            importWindowPage.getBtnImport().click();
            CSUtility.tempMethodForThreadSleep(5000);
            verifyImport();
            CSUtility.switchToDefaultFrame(browserDriver);
            importWindowPage.getBtnClose().click();
        } catch (Exception e) {
            CSLogger.debug(
                    "Exception occurred in test method :testUpdateAndDeleteScenario",
                    e);
            Assert.fail("Exception occurred, test failed. Please check logger "
                    + "for more detail");
        }
    }

    /**
     * This mehtod updates the import file for smart import with the changes for
     * update and delete case.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidFormatException
     */
    private void updateImportFile()
            throws FileNotFoundException, IOException, InvalidFormatException {
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiToolbarHorizontal.getLblProductId());
        String productId = csGuiToolbarHorizontal.getLblProductId()
                .getAttribute("value");
        String excelFilePath = downloadLocation + importFileName;
        importFileName = "AutomationSmartImportUpdateDelete.xlsx";
        FileInputStream inputStream = new FileInputStream(
                new File(excelFilePath));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet importSheet = workbook.getSheetAt(0);
        Cell pimAttrValue = importSheet.getRow(1).getCell(4);
        pimAttrValue.setCellValue(productId);
        Cell mamAttrValue = importSheet.getRow(1).getCell(3);
        mamAttrValue.setCellValue("");
        inputStream.close();
        FileOutputStream outputStream = new FileOutputStream(
                downloadLocation + importFileName);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    /**
     * This method performs click action on provided product name.
     * 
     * @param productOfName String object containing the name of the product to
     *            be clicked.
     */
    private void clickOnProduct(String productOfName) {
        switchToPIM(waitForReload);
        expandProductTree();
        pimStudioProductsNode.expandNodesByProductName(waitForReload,
                productOfName);
    }

    /**
     * This method traverse to the provided tab in the product view.
     * 
     * @param productTabName String name of the tab in the product view.
     * @param waitForReload
     */
    private void clickOnTheGivenProductTab(String productTabName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//nobr[contains(text(),'"
                        + productTabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By.xpath(
                "//nobr[contains(text(),'" + productTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the text attribute field.");
    }

    /**
     * Method to fill up smart import mapping window with values.
     * 
     * @param mappingName String object containing the name of mapping to
     *            select.
     * @throws AWTException Exception object for Robot class.
     */
    private void configureSmartImportWindowFields(String mappingName)
            throws AWTException {
        WebElement uploadFileElement = importWindowPage.getUplaoadImportFile();
        uploadFileElement.sendKeys(downloadLocation + importFileName);
        importWindowPage.getDdImportLanguage().sendKeys("English");
        pressEnterKey();
        importWindowPage.getDdMappingSet().sendKeys(mappingName);
        pressEnterKey();
        importWindowPage.getBtnNext().click();
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method verifies status of the smart import execution.
     */
    private void verifyImport() {
        CSUtility.tempMethodForThreadSleep(5000);
        Assert.assertEquals(
                importWindowPage.getLblStatusBarFigure().getText().trim(),
                "100 %");
        String importStatusMsg[] = importWindowPage.getLblImportStatus()
                .getText().split(",");
        int length = importStatusMsg.length;
        if (!(importStatusMsg[length - 1].contains("0"))
                || !(importStatusMsg[length - 2].contains("0"))) {
            CSLogger.error(
                    "Smart import failed, noticed 1 or more warnings/errors.");
            Assert.fail(
                    "Smart import failed, noticed 1 or more warnings/errors.");
        }
        CSLogger.info("Verified Import.");
    }

    /**
     * This method executes Enter key press event.
     * 
     * @throws AWTException Exception object for Robot class.
     */
    private void pressEnterKey() throws AWTException {
        CSUtility.tempMethodForThreadSleep(1000);
        Robot robotInstance = new Robot();
        robotInstance.keyPress(KeyEvent.VK_ENTER);
        robotInstance.keyRelease(KeyEvent.VK_ENTER);
        CSLogger.info("Pressed Enter Key");
    }

    /**
     * This method traverse to the smart import window.
     */
    private void goToSmartImport() {
        switchToPIM(waitForReload);
        expandProductTree();
        selectSmartImport();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsGuiDialog()));
        CSLogger.info("Clicked on pim smart import option.");
    }

    /**
     * This method verifies the smart import mapping.
     */
    private void verifyMappings() {
        WebElement importElement;
        WebElement classElement;
        for (int index = 0; index < standardAttr.length; index++) {
            importElement = browserDriver.findElement(
                    By.xpath("//div[contains(text(),'" + standardAttr[index]
                            + "') and @class='attributeName']"));
            classElement = browserDriver.findElement(By
                    .xpath("//div[contains(text(),'" + importFileData[index][0]
                            + "') and @class='valueString']"));
            if (importElement == null || classElement == null) {
                CSLogger.error("Pim Smart Import Mapping Failed.");
                Assert.fail("Pim Smart Import Mapping Failed.");
            }
        }
        CSLogger.info("Verified smart import Mappings.");
    }

    /**
     * Method to create the test product upon which smart import will be
     * triggered.
     */
    private void createProduct() {
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered " + productName + " in the input box.");
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("clicked ok on the input box.");
    }

    /**
     * Method to select the smart import from context menu post context click.
     */
    private void selectSmartImport() {
        productContextClick();
        csPopupDiv.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csPopupDiv.getCsGuiPopupProcess());
        CSLogger.info("Hover on the process option in context click.");
        csPopupDiv.selectPopupDivSubMenu(waitForReload,
                csPopupDiv.getCsGuiPopupSmartImport(), browserDriver);
    }

    /**
     * Method to select the smart import from context menu post context click.
     */
    private void selectRefresh() {
        productContextClick();
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getcsGuiPopupMenuRefresh(), browserDriver);
        CSLogger.info("Selected refresh from context menu.");
    }

    /**
     * Method to create a dummy child product under the test product.
     */
    private void createChildProduct() {
        switchToPIM(waitForReload);
        productContextClick();
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuNewChild(), browserDriver);
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info(
                "Created child product with default name via context option.");
    }

    /**
     * Method to perform checkIn on the test product vis context menu.
     */
    private void doProductCheckIn() {
        csPopupDiv.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csPopupDiv.getCsGuiPopupMenuObject());
        csPopupDiv.selectPopupDivSubMenu(waitForReload,
                csPopupDiv.getCsGuiPopupCheckIn(), browserDriver);
        CSLogger.info("Selected checkIn from product context option.");
    }

    /**
     * Method to initiate the assignment of class to test product via drag drop.
     */
    private void assignClassToTestProduct() {
        traverseToClassToDragAndDrop(waitForReload, productName,
                productClassName);
        performClick(waitForReload);
        CSLogger.info("Assigned class " + productClassName + " to product "
                + productName);
    }

    /**
     * Method to verify the smart import configuration.
     */
    public void verifySmartImportConfigWindow() {
        try {
            Select sddSheet = new Select(importWindowPage.getSddSheet());
            String sheetName = sddSheet.getFirstSelectedOption().getText();
            String className = importWindowPage.getSddConfiguration().getText();
            String rootNode = importWindowPage.getSddRootNode().getText();
            Assert.assertEquals(importSheetName, sheetName.trim());
            Assert.assertNotNull(className);
            if (!(rootNode.contains(productName))) {
                throw new Exception("Class name/root node not present.");
            }
        } catch (Exception e) {
            CSLogger.error(
                    "verification of Smart Import Configuration Window failed",
                    e);
            Assert.fail(
                    "verification of Smart Import Configuration Window failed");
        }
        CSLogger.info("Verified smart import window.");
    }

    /**
     * Method to verify the smart import mapping.
     */
    public void verifySmartImportMappingWindow() {
        try {
            importWindowPage.getLblMandatoryFields().getText();
            importWindowPage.getLblDefaultFields().getText();
            importWindowPage.getBtnDryRun().getText();
            importWindowPage.getBtnImport().getText();
        } catch (Exception e) {
            CSLogger.error("Required web elements not present .", e);
            Assert.fail("Required web elements not present .");
        }
    }

    /**
     * Method to perform the mapping(via drag and drop) of attribute
     * 
     * @param dragTag String object containing the name of attribute from left
     *            pane of mapping window.
     * @param dropTag String object containing the name of attribute from right
     *            pane of mapping window.
     */
    private void dragDropMapping(String dragTag, String dropTag) {
        WebElement dragElement = browserDriver.findElement(
                By.xpath(".//div[text()='" + dragTag + "']/../.."));
        WebElement dropElement = browserDriver.findElement(
                By.xpath(".//div[text()='" + dropTag + "']/../.."));
        scrollElementToView(dragElement);
        scrollElementToView(dropElement);
        Actions action = new Actions(browserDriver);
        Action dragDrop = action.clickAndHold(dragElement)
                .moveToElement(dropElement).release(dropElement).build();
        dragDrop.perform();
        CSLogger.info("Mapped attribtue " + dragTag + "with " + dragTag);
    }

    /**
     * Method to scroll a element into the view via javascript
     * 
     * @param dragElement WebElement object to be scrolled
     */
    private void scrollElementToView(WebElement dragElement) {
        JavascriptExecutor jse = (JavascriptExecutor) browserDriver;
        jse.executeScript("arguments[0].scrollIntoView(true);", dragElement);
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Scrolled to element.");
    }

    /**
     * Method to perform the context click on the test product.
     */
    private void productContextClick() {
        WebElement productElement;
        switchToPIM(waitForReload);
        productElement = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + productName + "')]"));
        CSUtility.rightClickTreeNode(waitForReload, productElement,
                browserDriver);
        CSLogger.info("Context clicked on the product");
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPIM(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * Method to expand the PIM product tree.
     */
    private void expandProductTree() {
        pimStudioProductsNode.clickOnNodeProducts();
    }

    /**
     * This method performs click on replace button after drag and drop, Replace
     * replaces already existing classes with new one.
     * 
     * @param waitForReload contains time in ms to wait on any element if needed
     * 
     */
    public void performClick(WebDriverWait waitForReload) {
        browserDriver.switchTo().defaultContent();
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentId.getBtnReplace()));
        csGuiDialogContentId.getBtnReplace().click();
        CSLogger.info(
                "Performed click after dragging the class to the product");
    }

    /**
     * This test method which traverses upto the newly created class and
     * performs drag and drop on the newly created product folder
     * 
     * @param waitForReload contains the time in ms to wait on any element if
     *            needed
     * @param nameOfProduct contains name of the already created product in
     *            string format
     * @param nameOfClass contains name of class contains the name of the class
     * 
     */
    public void traverseToClassToDragAndDrop(WebDriverWait waitForReload,
            String nameOfProduct, String nameOfClass) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                pimStudioSettingsNode.getBtnPimSettingsNode()));
        pimStudioSettingsNode.getBtnPimSettingsNode().click();
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioSettingsNode.getBtnPimClassesNode()));
        pimStudioSettingsNode.getBtnPimClassesNode().click();
        CSLogger.info("Clicked on Class Folder");
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(nameOfClass)));
        WebElement className = browserDriver.findElement(
                By.xpath(".//span[contains(text(),'" + nameOfClass + "')]"));
        CSUtility.tempMethodForThreadSleep(3000);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(nameOfProduct)));
        WebElement createdProductFolder = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + productName + "')]"));
        Actions action = new Actions(browserDriver);
        Action dragDrop = action.clickAndHold(className)
                .moveToElement(createdProductFolder)
                .release(createdProductFolder).build();
        dragDrop.perform();
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of test data.
     */
    @DataProvider(name = "SmartImportConfigTest")
    public Object[][] getCredentailsTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                smartImportSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        CSLogger.info("Initialized the POM object for Login page.");
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        pimStudioSettingsNode = new PimStudioSettingsNode(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        importWindowPage = new SmartImportWindowPage(browserDriver);
        iframeLocatorsInstance = new FrameLocators(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        action = new Actions(browserDriver);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
    }

}
