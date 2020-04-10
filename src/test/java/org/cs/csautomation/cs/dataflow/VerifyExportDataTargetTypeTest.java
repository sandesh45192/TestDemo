
package org.cs.csautomation.cs.dataflow;

/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioTree;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiListFooter;
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
import org.cs.csautomation.cs.utility.CSPopupDivMam;
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
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * This class contains test method to verify different data target type option
 * of data flow export.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyExportDataTargetTypeTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSPortalWidget            csPortalWidgetInstance;
    private ActiveJobsPage            activeJobsPageInstance;
    private FrameLocators             iframeLocatorsInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private Actions                   performAction;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim             csPopupDivPim;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private SoftAssert                softAssertion;
    private String                    alertText;
    private CSGuiListFooter           csGuiListFooterInstance;
    private String                    fileLocation;
    private ArrayList<String>         actualHeaders;
    private ArrayList<String>         actualData;
    private String                    createExportTestSheet = "DataTargetTypeOptions";
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private IAttributePopup           attributePopup;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private IClassPopup               classPopUp;
    private IProductPopup             productPopup;
    private Alert                     alertBox;
    private ModalDialogPopupWindow    modalDialogPopupWindowInstance;
    private HashMap<String, String>   exportData;
    private String                    referenceProductId;
    private String                    exportedFileName;
    private MamStudioVolumesNode      mamStudioVolumesNodeInstance;
    private IVolumePopup              volumePopup;
    private String                    templateFilePath;
    private String                    templateFileName      = "ExportTemplate.xlsx";
    private ArrayList<String>         expectedData;
    private String[]                  exportedZipFiles      = new String[4];
    static int                        count;
    private XSSFWorkbook              excelWorkBook;
    private XSSFSheet                 excelSheet            = null;
    private int                       expectedJobCount;
    private int                       actualJobCount;

    /**
     * This test method verifies 'Data Target Type' options.
     * 
     * @param testData
     *            Array of String contains the test data.
     */
    @Test(dataProvider = "exportTestData")
    public void testVerifyDataTargetTypeOptions(String... testData) {
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
                    exportData.get("secondChildProductName"),
                    exportData.get("volumeFolderName"),
                    exportData.get("mamFolderName"), templateFilePath);
            createExportActiveScript();
            configureExportActiveScript(exportData.get("exportLabel"),
                    exportData.get("exportCategory"));
            verifyDataTargetTypeOptions(
                    spiltData(exportData.get("dataTargetTypeFields")));
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method : "
                    + "testVerifyDataTargetTypeOptions", e);
            Assert.fail("Automation error in test method : "
                    + "testVerifyDataTargetTypeOptions", e);
        }
    }

    /**
     * This test method verifies the export of one XLSX file with all record.
     */
    @Test(priority = 1, dependsOnMethods = "testVerifyDataTargetTypeOptions")
    public void testVerifyExportExcelFileWithAllRecordsOption() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            verifyAllRecordsOptionDefaultFields();
            appendFileExtension();
            verifyExportInNewFileOption("In new File");
            verifyExportInCopyOfTemplateFileOption("In Copy of a Template File",
                    exportData.get("worksheetName"));
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : "
                            + "testVerifyExportExcelFileWithAllRecordsOption",
                    e);
            Assert.fail(
                    "Automation error in test method : "
                            + "testVerifyExportExcelFileWithAllRecordsOption",
                    e);
        }
    }

    /**
     * This test export's XLSX file with one tab per record
     */
    @Test(priority = 2, dependsOnMethods = "testVerifyDataTargetTypeOptions")
    public void testVerifyExportExcelFileWithOneTabPerRecordOption() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            verifyOneTabPerRecordOptionDefaultFields();
            appendFileExtension();
            clickOnTemplateFileButton();
            selectExportTemplateFile(exportData.get("volumeFolderName"),
                    exportData.get("mamFolderName"));
            executeExportScript();
            verifyOneTabPerRecordExcelFile();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : "
                            + "testVerifyExportExcelFileWithOneTabPerRecordOption",
                    e);
            Assert.fail(
                    "Automation error in test method : "
                            + "testVerifyExportExcelFileWithOneTabPerRecordOption",
                    e);
        }
    }

    /**
     * This test method export's XLSX file with one file per record.
     */
    @Test(priority = 3, dependsOnMethods = "testVerifyDataTargetTypeOptions")
    public void testVerifyExportExcelFileWithOneFilePerRecordOption() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            verifyOneFilePerRecordOptionDefaultFields();
            appendFileExtension();
            clickOnTemplateFileButton();
            selectExportTemplateFile(exportData.get("volumeFolderName"),
                    exportData.get("mamFolderName"));
            executeExportScript();
            checkFilesExportedPerRecordExists();
            verifyOneFilePerRecordZipFolderFiles();
            verifyOneFilePerRecordFiles();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : "
                            + "testVerifyExportExcelFileWithOneFilePerRecordOption",
                    e);
            Assert.fail(
                    "Automation error in test method : "
                            + "testVerifyExportExcelFileWithOneFilePerRecordOption",
                    e);
        }
    }

    /**
     * Verifies the default fields of data target section when data target type
     * option is selected as 'Excel - one XLSX file with one tab per record'.
     */
    private void verifyOneTabPerRecordOptionDefaultFields() {
        traverseToScriptConfiguration();
        clkOnGivenTab("Properties");
        selectDrpDwnOption(activeJobsPageInstance.getDrpDwnDataTargetType(),
                "Excel - one XLSX file with one tab per record");
        CSUtility.tempMethodForThreadSleep(1000);
        List<WebElement> dataTargetFields = browserDriver.findElements(By.xpath(
                "//div[@id='content__sections::Data Target']/table/tbody/tr/td[1]"));
        verifyFields(dataTargetFields,
                spiltData(exportData.get("oneTabPerRecordOptionFields")),
                "data target type while option is selected as 'Excel - one "
                        + "XLSX file with one tab per record'");
    }

    /**
     * Clicks on template file button.
     */
    private void clickOnTemplateFileButton() {
        activeJobsPageInstance.clkOnGivenWebElement(waitForReload,
                activeJobsPageInstance.getBtnTemplateFile());
        activeJobsPageInstance.clkOnGivenWebElement(waitForReload,
                activeJobsPageInstance.getBtnTemplateFile());
    }

    /**
     * Replaces the filename extension from CSV to XLSX.
     */
    private void appendFileExtension() {
        enterTextIntoTextbox(activeJobsPageInstance.getTxtDataTargetFileName(),
                exportData.get("fileName").replace("csv", "xlsx"));
    }

    /**
     * Verifies the default fields of data target section when data target type
     * option is selected as 'Tagged Template - one file per record with
     * replaced {ATTRIBUTE} fields'.
     */
    private void verifyOneFilePerRecordOptionDefaultFields() {
        traverseToScriptConfiguration();
        clkOnGivenTab("Properties");
        selectDrpDwnOption(activeJobsPageInstance.getDrpDwnDataTargetType(),
                "Tagged Template - one file per record with replaced {ATTRIBUTE}"
                        + " fields");
        List<WebElement> defaultFields = browserDriver.findElements(By
                .xpath("//div[@id='content__sections::Data Target']/table/tbody"
                        + "/tr/td" + "[1]"));
        verifyFields(defaultFields,
                spiltData(exportData.get("OneFilePerRecordOptionFields")),
                "data target type while option is selected as 'Tagged Template"
                        + " - one file "
                        + "per record with replaced {ATTRIBUTE} fields'");
    }

    /**
     * Verifies the default fields of data target section when data target type
     * option is selected as 'Excel - one XLSX file with all records'.
     */
    private void verifyAllRecordsOptionDefaultFields() {
        selectDrpDwnOption(activeJobsPageInstance.getDrpDwnDataTargetType(),
                "Excel - one XLSX file with all records");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPageInstance.getTxtDataTargetFileName());
        String fileName = activeJobsPageInstance.getTxtDataTargetFileName()
                .getAttribute("defaultvalue");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPageInstance.getDrpDwnActivescriptFileGeneration());
        String dataTargetGeneration = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActivescriptFileGeneratio"
                        + "n')]/option[@selected]"))
                .getText();
        if (exportData.get("fileName").equals(fileName) && exportData
                .get("defaultFileGeneration").equals(dataTargetGeneration)) {
            CSLogger.info(
                    "Verified default fields of data target section while data "
                            + "target type option is selected as 'Excel - one "
                            + "XLSX file" + " with all records'");
        } else {
            CSLogger.error(
                    "Verification of default fields of data target section "
                            + "failed while data "
                            + "target type option is selected as 'Excel - one "
                            + "XLSX file" + " with all records'");
            softAssertion
                    .fail("Verification of default fields of data target section "
                            + "failed while data "
                            + "target type option is selected as 'Excel - one "
                            + "XLSX file" + " with all records'");
        }
    }

    /**
     * Verifies that the selected root folder is exported in a new XLSX file.
     * 
     * @param fileGenerationOption
     *            String contains 'Generation' option value.
     */
    private void verifyExportInNewFileOption(String fileGenerationOption) {
        selectDrpDwnOption(
                activeJobsPageInstance.getDrpDwnActivescriptFileGeneration(),
                fileGenerationOption);
        saveExportActiveScript();
        executeExportScript();
        checkPerRecordExcelFileExists();
        verifyExportedFileData(false, fileLocation + exportedFileName);
    }

    /**
     * Verifies that the selected root folder is exported in a template file.
     * 
     * @param fileGenerationOption
     *            String contains 'Generation' option value.
     * @param worksheetName
     *            String object contains worksheet name.
     */
    private void verifyExportInCopyOfTemplateFileOption(
            String fileGenerationOption, String worksheetName) {
        traverseToScriptConfiguration();
        clkOnGivenTab("Properties");
        selectDrpDwnOption(
                activeJobsPageInstance.getDrpDwnActivescriptFileGeneration(),
                fileGenerationOption);
        CSUtility.tempMethodForThreadSleep(2000);
        List<WebElement> defaultFields = browserDriver.findElements(By.xpath(
                "//div[@id='content__sections::Data Target']/table/tbody/tr/td[1]"));
        verifyFields(defaultFields,
                spiltData(exportData.get("generationField")), "Data Target");
        activeJobsPageInstance.clkOnGivenWebElement(waitForReload,
                activeJobsPageInstance.getBtnTemplateFile());
        selectExportTemplateFile(exportData.get("volumeFolderName"),
                exportData.get("mamFolderName"));
        traverseToScriptConfiguration();
        enterTextIntoTextbox(activeJobsPageInstance.getTxtWorksheet(),
                worksheetName);
        activeJobsPageInstance.checkGivenCheckBox(waitForReload,
                activeJobsPageInstance.getCbAddHeadline());
        saveExportActiveScript();
        executeExportScript();
        checkPerRecordExcelFileExists();
        verifyExportedFileData(true, fileLocation + exportedFileName);
    }

    /**
     * Executes the export script and prints the logs into logger file.
     */
    private void executeExportScript() {
        executeExportActiveScript();
        verifyActiveScriptJob();
        printActiveScriptLogs();
    }

    /**
     * Selects the template file.
     * 
     * @param volumeFolderName
     *            String object contains volume folder name.
     * @param importFolderName
     *            String object contains import folder name.
     */
    private void selectExportTemplateFile(String volumeFolderName,
            String importFolderName) {
        clkBtnControlPaneButtonFilesFolder();
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogFile(
                waitForReload, browserDriver);
        clkOnGivenVolumeFolder(volumeFolderName);
        clkOnGivenVolumeFolder(importFolderName);
        clkOnGivenVolumeFolder(importFolderName);
        TraverseSelectionDialogFrames
                .traverseTillDataSelectionDialogFileCenterPane(waitForReload,
                        browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//table[@class='hidewrap CSAdminList']")));
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement templateFile = browserDriver.findElement(
                By.xpath("//tr[@id='HeaderBarTop']/following-sibling::tr/td"
                        + "[contains(text(),'" + templateFileName + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, templateFile);
        templateFile.click();
        modalDialogPopupWindowInstance
                .handlePopupDataSelectionDialog(waitForReload, true);
        traverseToScriptConfiguration();
        saveExportActiveScript();
    }

    /**
     * This method clicks on file folder from the file selection dialog window
     */
    public void clkBtnControlPaneButtonFilesFolder() {
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        WebElement fileFolder = browserDriver
                .findElement(By.xpath("//div[@class='ControlPaneButton'][1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, fileFolder);
        fileFolder.click();
        CSLogger.info("Clicked on Control Pane Button Files Folder");
    }

    /**
     * Clicks on given volume folder.
     * 
     * @param volumeFolderName
     *            String object contains name of volume folder.
     */
    private void clkOnGivenVolumeFolder(String volumeFolderName) {
        WebElement volumesFolder = browserDriver
                .findElement(By.linkText(volumeFolderName));
        CSUtility.scrollUpOrDownToElement(volumesFolder, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload, volumesFolder);
        volumesFolder.click();
        CSLogger.info("Clicked on volume : " + volumeFolderName + " folder");
    }

    /**
     * This method traverse to the PIM module from home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void switchToPimAndExpandProductsTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     * @param pimStudioProductsNode
     *            PimStudioProductsNode Object
     * @param parentProductName
     *            String Object containing the value for parent product Name.
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
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method creates the child product with the supplied name and the
     * supplied parent product name.
     * 
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param childProductName
     *            String Object containing the child product name.
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
     * @param productName
     *            String object containing name of product it can be child
     *            product or parent product
     * @param nodeElement
     *            WebElement of either products node or parent product
     * @param popupMenuOption
     *            WebElement containing menu option.
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
     * @param exportLabel
     *            String object contains export label.
     * @param exportCategory
     *            String object contains export category.
     */
    private void configureExportActiveScript(String exportLabel,
            String exportCategory) {
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
        configureDataSource(exportData.get("dataSourceType"),
                exportData.get("dataSelectionType"),
                exportData.get("parentProductName"),
                exportData.get("selectionLayer"),
                exportData.get("defaultLanguage"), exportData.get("startCount"),
                exportData.get("maxCount"), exportData.get("batchSize"));
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
     * @param dataSourceFields
     *            String object contains data source section default fields.
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
     * @param dataSourceType
     *            String object contains data source type.
     * @param dataSelectionType
     *            String object contains data selection type.
     * @param rootFolderName
     *            String object contains name of product folder to be exported.
     * @param selectionLayer
     *            String object contains selection layer.
     * @param defaultLanguage
     *            String object contains language.
     * @param startCount
     *            String object contains start count.
     * @param maxCount
     *            String object contains max count.
     * @param batchSize
     *            String object contains batch size of export.
     */
    private void configureDataSource(String dataSourceType,
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
     * @param productFolderName
     *            String object contains product folder name.
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
     * @param attributeFolder
     *            String object contains attribute folder name.
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
     * @param attributeFolder
     *            String object contains attribute folder name.
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
     * Configures the actual data to be exported.
     * 
     */
    private void readAllRecordeExcelFileActualData() {
        actualData = new ArrayList<String>();
        actualHeaders = new ArrayList<>();
        ArrayList<String> productData = null;
        String exportedAttributes[] = exportData.get("attributesName")
                .split(",");
        actualHeaders.add("Label");
        for (int i = 0; i < exportedAttributes.length; i++) {
            actualHeaders.add(exportedAttributes[i]);
        }
        actualData.addAll(actualHeaders);
        String testData[] = getProductData();
        for (int i = 0; i < testData.length; i++) {
            String spiltedData[] = testData[i].split(",");
            String multiTextData = "<p>" + spiltedData[2] + "</p>";
            String refToPimData = "Pdmarticle:" + referenceProductId;
            productData = new ArrayList<String>(Arrays.asList(spiltedData));
            productData.set(2, multiTextData);
            productData.add(3, refToPimData);
            actualData.addAll(productData);
        }
    }

    /**
     * Returns the test data of product.
     * 
     * @return String array contains product data.
     */
    private String[] getProductData() {
        ArrayList<String> productData = new ArrayList<String>();
        productData.add(exportData.get("parentProductData"));
        productData.add(exportData.get("firstChildProductData"));
        productData.add(exportData.get("secondChildProductData"));
        Object[] tempData = productData.toArray();
        return Arrays.copyOf(tempData, tempData.length, String[].class);
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param productName
     *            String containing name of product
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
     * Verifies the data target type option fields.
     * 
     * @param dataTargetType
     *            String object contains data target type.
     * @param fileName
     *            String object contains file name.
     * @param csvdelimiter
     *            String object contains CSV delimiter.
     * @param csvEnclosure
     *            String object contains CSV enclosure.
     * @param csvEncoding
     *            String object contains CSV encoding.
     */
    private void verifyDataTargetTypeOptions(String dataTargetTypeFields[]) {
        traverseToScriptConfiguration();
        try {
            activeJobsPageInstance
                    .clkWebElement(activeJobsPageInstance.getSecDataTarget());
        } catch (Exception e) {
            CSLogger.error("Data target section not found : test step failed");
            Assert.fail("Data target section not found : test step failed");
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPageInstance.getDrpDwnDataTargetType());
        activeJobsPageInstance.clkWebElement(
                activeJobsPageInstance.getDrpDwnDataTargetType());
        for (int i = 1; i < dataTargetTypeFields.length + 1; i++) {
            WebElement element = browserDriver.findElement(By
                    .xpath("//select[contains(@id,'ActivescriptExportLoaderType"
                            + "')]/option[" + i + "]"));
            if (!(element.getText().equals(dataTargetTypeFields[i - 1]))) {
                CSLogger.error("Option " + dataTargetTypeFields[i - 1]
                        + " not found under data target type drop down list ");
                softAssertion.fail("option " + dataTargetTypeFields[i - 1]
                        + " not found under data target type drop down list ");
            }
        }
    }

    /**
     * Executes the configured export script.
     */
    private void executeExportActiveScript() {
        try {
            expectedJobCount = getJobCount();
            traverseToScriptConfiguration();
            clkOnGivenTab("Properties");
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarRunActiveScript(waitForReload);
            CSUtility.tempMethodForThreadSleep(1000);
            alertText = "Are you sure to run this ActiveScript?";
            alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
            if (!(alertBox.getText().contains(alertText))) {
                CSLogger.error(
                        "Alert box saying : " + alertText + " does not appear");
                softAssertion.fail(
                        "Alert box saying : " + alertText + " does not appear");
            }
            alertBox.accept();
        } catch (Exception e) {
            CSLogger.error("Error while running the active script", e);
            Assert.fail("Error while running the active script", e);
        }
    }

    /**
     * Verifies whether job is created and also verifies the menu bar fields of
     * created job.
     */
    private void verifyActiveScriptJob() {
        verifyJobCreation();
        int jobStatus = browserDriver.findElements(
                By.xpath("//tr[@id='HeaderBarTop']/following-sibling::tr"
                        + "[1]/td[2]/div/img[contains(@src,'circle_check"
                        + ".svg')]"))
                .size();
        if (jobStatus != 0) {
            CSLogger.info("Job executed successfully no error logs found "
                    + ": test step passed");
        } else {
            CSLogger.error(
                    "Execution of job failed error log found : test step "
                            + "failed");
            softAssertion.fail("Execution of job failed error log found : "
                    + "test step failed");
        }
        WebElement createdJob = browserDriver.findElement(
                By.xpath("//tr[@id='HeaderBarTop']/following-sibling::tr[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdJob);
        createdJob.click();
        CSLogger.info("Clicked on created job");
        CSUtility.tempMethodForThreadSleep(3000);
        verifyLogMenuBarFields();
    }

    /**
     * Verifies the menu bar fields of created job.
     */
    private void verifyLogMenuBarFields() {
        traverseToScriptConfiguration();
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmShowingStatusOfRunningActiveScript()));
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//table[@id='ActiveScriptLogTable']")));
        int timeMenuBarField = browserDriver
                .findElements(By.xpath("//th[@class='Time']")).size();
        int diffMenuBarField = browserDriver
                .findElements(By.xpath("//th[@class='Diff']")).size();
        int totalMenuBarField = browserDriver
                .findElements(By.xpath("//th[@class='Total']")).size();
        String defaultStatus = browserDriver
                .findElement(By.xpath("//select[@id='ActiveScriptVisibility']"
                        + "/option[@selected]"))
                .getText();
        String defaultMessagesCount = browserDriver
                .findElement(By.xpath("//select[@id='ActiveScriptMessageCount']"
                        + "/option[@selected]"))
                .getText();
        if (defaultStatus.equals("Level") && timeMenuBarField != 0
                && diffMenuBarField != 0 && totalMenuBarField != 0
                && defaultMessagesCount.equals("Last 50 Messages")) {
            CSLogger.info(
                    "Logs menu bar fields verified successfully : test step "
                            + "passed");
        } else {
            CSLogger.error(
                    "Logs menu bar fields verification failed : test step "
                            + "failed");
            softAssertion
                    .fail("Logs menu bar fields verification failed : test step "
                            + "failed");
        }
    }

    /**
     * Prints the active script job logs to logger file.
     * 
     */
    private void printActiveScriptLogs() {
        WebElement logTable = browserDriver
                .findElement(By.xpath("//table[@id='ActiveScriptLogTable']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, logTable);
        List<WebElement> activeJobLogs = browserDriver.findElements(
                By.xpath("//table[@id='ActiveScriptLogTable']/tbody/tr"));
        CSLogger.info("Time" + "\t" + "Total" + "\t" + "Diff" + "\t"
                + "All Messages");
        for (WebElement cell : activeJobLogs) {
            CSLogger.info(cell.getText());
        }
    }

    /**
     * Verifies whether job is created.
     */
    private void verifyJobCreation() {
        actualJobCount = getJobCount();
        if (actualJobCount == expectedJobCount + 1) {
            CSLogger.info(
                    "Active script job created successfully : test step passed");
        } else {
            CSLogger.error(
                    "Active script job creation failed : test step failed");
            Assert.fail("Active script job creation failed : test step failed");
        }
    }

    /**
     * Returns the count of number of jobs on jobs tab.
     * 
     * @return
     */
    private int getJobCount() {
        clkOnGivenTab("Jobs");
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmJobsState()));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiListFooterInstance.getLblElementCount());
        String elementsCount = csGuiListFooterInstance.getLblElementCount()
                .getText();
        return Integer.parseInt(
                elementsCount.substring(0, elementsCount.indexOf(' ')));
    }

    /**
     * Downloads the exported file from active script job logs.
     */
    private void downloadExportedFile() {
        CSUtility.tempMethodForThreadSleep(3000);
        WebElement downloadFileLink = browserDriver
                .findElement(By.xpath("//a[@class='result-link']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, downloadFileLink);
        downloadFileLink.click();
        exportedFileName = downloadFileLink.getText();
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * Verifies the exported XLSX data file while data target type option is
     * selected as 'Excel - one XLSX file with all records'.
     * 
     * @throws IOException
     */
    private void verifyExportedFileData(Boolean isCopyOfTemplateFile,
            String filePath) {
        Object[] exportedProductData;
        Object[] actualProductData;
        actualData = new ArrayList<>();
        expectedData = new ArrayList<>();
        readAllRecordeExcelFileActualData();
        expectedData = getAllRecordExportedExcelFileData(isCopyOfTemplateFile,
                filePath);
        exportedProductData = expectedData.toArray();
        actualProductData = actualData.toArray();
        compareData(exportedProductData, actualProductData, exportedFileName,
                "one XLSX file with all records ");
        actualData.clear();
        expectedData.clear();
    }

    /**
     * Reads data from exported XLSX file
     * 
     * @return ArrayList<String> contains data from excel file.
     */
    private ArrayList<String> getAllRecordExportedExcelFileData(
            Boolean isCopyOfTemplateFile, String filePath) {
        ArrayList<String> exportedData = new ArrayList<>();
        try {
            excelWorkBook = new XSSFWorkbook(
                    new FileInputStream(new File(filePath)));
            if (isCopyOfTemplateFile) {
                int expectedExportedFileSheet = Integer
                        .parseInt(exportData.get("templateSheetCount")) + 1;
                if (verifyExportedSheetExists(excelWorkBook,
                        expectedExportedFileSheet)) {
                    if (excelWorkBook
                            .getNumberOfSheets() == expectedExportedFileSheet) {
                        excelSheet = excelWorkBook
                                .getSheet(exportData.get("worksheetName"));
                    } else {
                        CSLogger.error(
                                "Number of sheets are not correct in template file");
                        softAssertion.fail(
                                "Number of sheets are not correct in template file");
                    }
                } else {
                    CSLogger.error("Could not find sheet "
                            + exportData.get("worksheetName")
                            + " in template file");
                    Assert.fail("Could not find sheet "
                            + exportData.get("worksheetName")
                            + " in template file");
                }
            } else {
                excelSheet = excelWorkBook.getSheetAt(0);
            }
            Iterator<Row> rowIterator = excelSheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    exportedData.add(cell.getStringCellValue());
                }
            }
            excelWorkBook.close();
        } catch (Exception e) {
            CSLogger.error("Error while reading excel file", e);
            Assert.fail("Error while reading excel file", e);
        }
        return exportedData;
    }

    /**
     * Verifies whether worksheet exists in exported file
     * 
     * @param excelWorkBook
     *            Instance of XSSFWorkbook
     * @param expectedExportedFileSheet
     *            Integer value contains number of count of sheet to be present
     *            in exported sheet.
     * @return Boolean value according to presence of sheet.
     */
    private boolean verifyExportedSheetExists(XSSFWorkbook excelWorkBook,
            int expectedExportedFileSheet) {
        ArrayList<String> sheetNames = new ArrayList<>();
        for (int i = 0; i < expectedExportedFileSheet; i++) {
            sheetNames.add(excelWorkBook.getSheetName(i));
        }
        if (sheetNames.contains(exportData.get("worksheetName"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies the exported files and ZIP folder exists.
     */
    private void checkPerRecordExcelFileExists() {
        downloadExportedFile();
        checkGivenFileExists();
    }

    /**
     * Checks whether the exported file exists.
     */
    private void checkGivenFileExists() {
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
     * This method creates the prerequisite configuration data for export test.
     * 
     * @param className
     *            String object containing the name of the class to assign the
     *            test product.
     * @param attributeFolder
     *            String object contains attribute folder name.
     * @param attributeName
     *            String object contains attribute name.
     * @param attributeType
     *            String object contains attribute field.
     * @param attributeValue
     *            String object contains attribute value.
     * @param productName
     *            String object contains product name.
     * @param firstChild
     *            String object contains first child name.
     * @param secondChild
     *            String object contains second child name.
     * @param volumeFolderName
     *            String object contains volume folder name.
     * @param mamFolderName
     *            String object contains MAM folder name.
     * @param filePath
     *            String object contains file path.
     */
    private void createPrerequisiteData(String className,
            String attributeFolder, String attributeName, String attributeType,
            String attributeValue, String productName, String firstChild,
            String secondChild, String volumeFolderName, String mamFolderName,
            String filePath) {
        String[] attributeArray = attributeName.split(",");
        String[] attributeValueArray = attributeValue.split(",");
        String[] attributeTypeArray = attributeType.split(",");
        uploadTemplateFile(volumeFolderName, mamFolderName, filePath);
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
     * Switches to pim and expands settings tree.
     */
    private void switchToPimAndExpandSettingsTree() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * This method create attribute folder.
     * 
     * @param attributeFolderName
     *            String object contains attribute folder name
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
     * @param folderName
     *            String object contains attribute folder name
     * @param attributeName
     *            String object contains name of attribute * @param
     * @param attributeType
     *            String object contains attribute types
     * @param attributeValue
     *            String object contains attribute value name
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
     * @param folderName
     *            String object contains attribute folder.
     */
    private void clkOnGivenAttributeFolder(String folderName) {
        WebElement attributeFolder = getAttributeFolder(folderName);
        attributeFolder.click();
        CSLogger.info("Clicked on " + folderName + " attribute folder");
    }

    /**
     * This method create single line text type attribute.
     * 
     * @param attributeFolder
     *            String object contains attribute folder name
     * @param attributeName
     *            String object contains attribute type
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
     * @param className
     *            String object contains class name
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
     * @param attributeType
     *            String object contains attribute type.
     * @param attributeValueString
     *            object contains attribute value.
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
     * @param element
     *            WebElement object where the text data is to send
     * @param text
     *            String object contains text data
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
     * @param attributeFolderName
     *            String object attribute folder name
     * @param className
     *            String object contains class name
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
     * @param parentProductName
     *            String Object containing the product name under which child
     *            product will be created.
     * @param className
     *            String object containing name of the class
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
     * @param productName
     *            String Object containing the product name under which child
     *            product will be created.
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
     * @param className
     *            String object containing name of the class.
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
     * @param productName
     *            String object contains name of product.
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
     * @param productName
     *            String object contains product name.
     * @param productTabName
     *            String object contains product tab name.
     * @param sltAttrName
     *            String object contains single line text attribute name.
     * @param sltAttrValue
     *            String object contains single line text attribute value.
     * @param multiAttrName
     *            String object contains multi-line formatted attribute name.
     * @param multiAttrValue
     *            String object contains multi-line formatted attribute value.
     * @param refToPimAttrName
     *            String object contains reference to PIM attribute name.
     * @param pimReference
     *            String object contains reference product name.
     * @param valueListAttrName
     *            String object contains value list attribute name.
     * @param valueListAttrValue
     *            String object contains value list attribute value.
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
     * @param multiAttrName
     *            String object contains multi-line formatted attribute name.
     * @param multiAttrValue
     *            String object contains multi-line formatted attribute value.
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
     * @param tabName
     *            String object contains tab.
     */
    private void clkOnGivenTab(String tabName) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//nobr[contains(text(),'" + tabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By
                .xpath("//nobr[contains(text(),'" + tabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the " + tabName + " tab");
    }

    /**
     * This method reads the test data from sheet.
     * 
     * @param testData
     *            String contains test data.
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
                "dataTargetType", "fileName", "defaultFileGeneration",
                "dataTargetTypeFields", "volumeFolderName", "mamFolderName",
                "generationField", "worksheetName", "templateSheetCount",
                "oneTabPerRecordOptionFields", "OneFilePerRecordOptionFields" };
        try {
            for (int index = 0; index < sheetData.length; index++) {
                exportData.put(sheetData[index], testData[index]);
            }
        } catch (

        Exception e) {
            CSLogger.debug("Error in  method : readTestDataFromSheet", e);
            Assert.fail("Error in  method : readTestDataFromSheet", e);
            return;
        }
    }

    /**
     * Spilt's the given data separated from comma.
     * 
     * @param data
     *            String object contains data separated by comma.
     * @return String array of data.
     */
    private String[] spiltData(String data) {
        return data.split(",");
    }

    /**
     * This method verifies different section default fields.
     * 
     * @param tableData
     *            WebElement contains list of elements
     * @param fields
     *            String object contains array of fields to be verified.
     * @param section
     *            String object specifies the section. i.e data target,data
     *            source,data transformation.
     */
    private void verifyFields(List<WebElement> tableData, String[] fields,
            String section) {
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
     * @param drpDwnElement
     *            Drop down WebElement.
     * @param option
     *            String object contains option to be selected.
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
     * Switches to Media tab and expands the given volume folder and uploads the
     * given file.
     * 
     * @param volumeFolderName
     *            String object contains name of volume folder.
     * @param folderName
     *            String object contains name of MAM folder.
     * @param filePath
     *            String object contains file path of file to be imported.
     */
    private void uploadTemplateFile(String volumeFolderName, String folderName,
            String filePath) {
        csPortalHeaderInstance.clkBtnMedia(waitForReload);
        clkOnVolumesNode();
        createMAMFolder(volumeFolderName, folderName);
        uploadExportTemplateFile(folderName, filePath);
    }

    /**
     * Performs operation of clicking on volumes node.
     */
    private void clkOnVolumesNode() {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOf(
                mamStudioVolumesNodeInstance.getBtnMamVolumesNode()));
        mamStudioVolumesNodeInstance.getBtnMamVolumesNode().click();
        CSLogger.info("Clicked on volumes node");
    }

    /**
     * Created a volume folder.
     * 
     * @param volumefolderName
     *            String object contains name of volume folder under which the
     *            given volume folder will be created.
     * @param folderName
     *            String object contains name of MAM folder
     */
    private void createMAMFolder(String volumefolderName, String folderName) {
        mamStudioVolumesNodeInstance.expandNodesByVolumesName(waitForReload,
                volumefolderName);
        CSUtility.rightClickTreeNode(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + volumefolderName + "')]")),
                browserDriver);
        volumePopup.selectPopupDivMenu(waitForReload,
                volumePopup.getCreateNewFolder(), browserDriver);
        volumePopup.enterValueInDialogueMamStudio(waitForReload, folderName);
        volumePopup.askBoxWindowOperationMamStudio(waitForReload, true,
                browserDriver);
        CSLogger.info("Folder : " + folderName + " created successfully");
    }

    /**
     * Uploads the given file.
     * 
     * @param folderName
     *            String object contains name of volume folder under which file
     *            is to be uploaded
     * @param filePath
     *            String object path of file to be imported.
     */
    private void uploadExportTemplateFile(String folderName, String filePath) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            WebElement volumeFolder = browserDriver
                    .findElement(By.linkText(folderName));
            CSUtility.waitForVisibilityOfElement(waitForReload, volumeFolder);
            browserDriver.findElement(By.linkText(folderName))
                    .sendKeys(filePath);
            CSUtility.rightClickTreeNode(waitForReload, volumeFolder,
                    browserDriver);
            volumePopup.selectPopupDivMenu(waitForReload,
                    volumePopup.getClkUploadNewFile(), browserDriver);
            traverseFramesToUploadFile();
            mamStudioVolumesNodeInstance.clkOnBrowseFileToUpload(waitForReload);
            selectTemplateFile(filePath);
            mamStudioVolumesNodeInstance.clkOnUploadButtonImage(waitForReload);
            CSUtility.tempMethodForThreadSleep(2000);
            mamStudioVolumesNodeInstance
                    .clkOnBtnCloseAfterUpload(waitForReload);
            CSLogger.info("File uploaded successfully");
        } catch (Exception e) {
            CSLogger.error("File upload failed", e);
            Assert.fail("File upload failed", e);
        }
    }

    /**
     * Traverse the frames to upload MAM file.
     */
    private void traverseFramesToUploadFile() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
    }

    /**
     * This method add the file to the clip board and clicks on enter.
     * 
     * @param filePath
     *            String contains path of file.
     * 
     */
    private void selectTemplateFile(String filePath) throws Exception {
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
    }

    /**
     * Reads the data from exported XLSX file.
     * 
     * @param sheetName
     *            String object contains sheetName.
     * @param filePath
     *            String object contains file path.
     * @return ArrayList<String> contains exported data.
     */
    private ArrayList<String> readDataFromExcelFile(String sheetName,
            String filePath) {
        ArrayList<String> excelData = new ArrayList<>();
        try {
            excelWorkBook = new XSSFWorkbook(
                    new FileInputStream(new File(filePath)));
            excelSheet = excelWorkBook.getSheet(sheetName);
            Iterator<Row> rowIterator = excelSheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (!(cell.getStringCellValue().isEmpty())) {
                        excelData.add(cell.getStringCellValue());
                    }
                }
            }
            excelWorkBook.close();
        } catch (Exception e) {
            CSLogger.error("Error while reading from file : " + filePath, e);
            Assert.fail("Error while reading from file : " + filePath, e);
        }
        return excelData;
    }

    /**
     * Verifies XLSX file is exported with per record on different tab.
     */
    private void verifyOneTabPerRecordExcelFile() {
        Object[] exportedProductData;
        Object[] actualProductData;
        checkPerRecordExcelFileExists();
        actualData = new ArrayList<>();
        expectedData = new ArrayList<>();
        String productData[] = getProductData();
        for (int productCount = 0; productCount < productData.length; productCount++) {
            String sheetName = getExpectedTemplateData(productData,
                    productCount);
            actualData = readDataFromExcelFile(sheetName,
                    fileLocation + exportedFileName);
            exportedProductData = expectedData.toArray();
            actualProductData = actualData.toArray();
            compareData(exportedProductData, actualProductData,
                    exportedFileName, "one tab per record");
            expectedData.clear();
            actualData.clear();
        }
    }

    /**
     * Returns the test product data.
     * 
     * @param productData
     *            String array contains product data
     * @param productCount
     *            Integer value contains count of number of product to be
     *            exported.
     * @return
     */
    private String getExpectedTemplateData(String[] productData,
            int productCount) {
        String spiltedData[] = spiltData(productData[productCount]);
        for (int j = 0; j < spiltedData.length; j++) {
            expectedData.add(spiltedData[j]);
        }
        expectedData.add(1, "Teaser");
        expectedData.add(3, "Description");
        expectedData.add(5, "Color");
        expectedData.add(7, "Accessories");
        expectedData.add(8, "Pdmarticle:" + referenceProductId);
        return spiltedData[0];
    }

    /**
     * Checks whether the per record files exists.
     */
    private void checkFilesExportedPerRecordExists() {
        CSUtility.tempMethodForThreadSleep(3000);
        for (int index = 1; index < 5; index++) {
            WebElement downloadFileLink = browserDriver.findElement(
                    By.xpath("(//a[@target='_blank'])[" + index + "]"));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    downloadFileLink);
            downloadFileLink.click();
            CSLogger.info("Clicked on download file link");
            CSUtility.tempMethodForThreadSleep(4000);
            exportedFileName = downloadFileLink.getText();
            exportedZipFiles[index - 1] = exportedFileName;
            CSUtility.tempMethodForThreadSleep(3000);
            checkGivenFileExists();
        }
    }

    /**
     * Verifies the ZIP folder contains per record XLSX files.
     */
    private void verifyOneFilePerRecordZipFolderFiles() {
        Object[] exportedProductData;
        Object[] actualProductData;
        File[] fileNames = null;
        expectedData = new ArrayList<>();
        actualData = new ArrayList<>();
        try {
            ZipFile zipFolder = new ZipFile(fileLocation + exportedZipFiles[0]);
            zipFolder.extractAll(fileLocation + "UnzippedFolder");
            File unzippedFolder = new File(fileLocation + "UnzippedFolder");
            File nestedJobFolder = new File(
                    unzippedFolder.listFiles()[0].getPath());
            fileNames = nestedJobFolder.listFiles();
            String productData[] = getProductData();
            for (int productCount = 0; productCount < productData.length; productCount++) {
                String sheetName = getExpectedTemplateData(productData,
                        productCount);
                actualData = readDataFromExcelFile(sheetName,
                        fileNames[productCount].getPath());
                exportedProductData = expectedData.toArray();
                actualProductData = actualData.toArray();
                compareData(exportedProductData, actualProductData,
                        fileNames[productCount].getPath(),
                        "one file per record");
                expectedData.clear();
                actualData.clear();
            }
        } catch (ZipException e) {
            CSLogger.error("Error while verifying exported zip folder", e);
            softAssertion.fail("Error while verifying exported zip folder", e);
        }
        /*
         * To do - verification of last excel file which contains all product
         * data is remaining because excel file is corrupted.
         */
    }

    /**
     * Compares the expected and actual data and accordingly prints the log.
     * 
     * @param exportedProductData
     *            Object contains exported product data.
     * @param actualProductData
     *            Object contains actual product data.
     * @param fileName
     *            String object contains file name.
     * @param section
     *            String object contains description for logs.
     */
    private void compareData(Object[] exportedProductData,
            Object[] actualProductData, String fileName, String section) {
        for (int index = 0; index < actualProductData.length; index++) {
            if (!(actualProductData[index]
                    .equals(exportedProductData[index]))) {
                CSLogger.error(actualProductData[index]
                        + " product content not found in exported file"
                        + fileName);
                softAssertion.fail(actualProductData[index]
                        + " product content not found in exported file "
                        + fileName);
            }
        }
        if (expectedData.equals(actualData)) {
            CSLogger.info("Excel file " + fileName
                    + " successfully exported with " + section + " data");
        } else {
            CSLogger.error("Data export " + section + " failed ,please "
                    + "check exported " + fileName + " file content");
            softAssertion.fail("Data export " + section + " failed ,please "
                    + "check exported " + fileName + " file content");
        }
    }

    /**
     * Verifies that one XLSX file per record is exported.
     */
    private void verifyOneFilePerRecordFiles() {
        Object[] exportedProductData;
        Object[] actualProductData;
        expectedData = new ArrayList<>();
        actualData = new ArrayList<>();
        count = exportedZipFiles.length - 1;
        String productData[] = getProductData();
        for (int productCount = 0; productCount < productData.length; productCount++) {
            String sheetName = getExpectedTemplateData(productData,
                    productCount);
            actualData = readDataFromExcelFile(sheetName,
                    fileLocation + exportedZipFiles[count]);
            exportedProductData = expectedData.toArray();
            actualProductData = actualData.toArray();
            compareData(exportedProductData, actualProductData,
                    exportedZipFiles[count],
                    "one file per record data of zip export folder");
            expectedData.clear();
            actualData.clear();
            count--;
        }
    }

    /**
     * This is a data provider method returns test data i.e
     * ParentProductName,FirstChildProductName, SecondChildProductName,
     * AttributeFolderName, AttributesName, AttributeType, AttributeValue,
     * ClassName, ParentProductData, FirstChildProductData,
     * SecondChildProductData, "ProductTab, RefToPimAttrName, ExportLabel,
     * ExportCategory, DataSourceSectionFields, DataSourceType,
     * DataSelectionType, SelectionLayer, DefaultLanguage, StartCount,
     * "MaxCount, BatchSize, DataTargetType, FileName, DefaultFileGeneration,
     * DataTargetTypeFields, VolumeFolderName, MamFolderName, GenerationField,
     * WorksheetName, TemplateSheetCount, OneTabPerRecordOptionFields,
     * OneFilePerRecordOptionFields
     * 
     * @return Array String array consisting of export data
     */

    @DataProvider(name = "exportTestData")
    public Object[][] getExportTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("dataflowExportModuleTestCases"),
                createExportTestSheet);
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
        performAction = new Actions(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        csPopupDivPim = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        csGuiListFooterInstance = new CSGuiListFooter(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
        mamStudioVolumesNodeInstance = MamStudioTree
                .getMamStudioVolumesNodeInstance(browserDriver);
        volumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        templateFilePath = config.getDataflowTestDataFolder()
                + templateFileName;
    }
}
