
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioTree;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
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
import org.openqa.selenium.Keys;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class contain the test method to verify import script can be executed
 * from PIM studio.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyExecutionFromPimStudioImportTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSPortalWidget            csPortalWidgetInstance;
    private ActiveJobsPage            activeJobsPageInstance;
    private FrameLocators             iframeLocatorsInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private Actions                   performAction;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private IProductPopup             productPopup;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private SoftAssert                softAssertion;
    private String                    alertText;
    private CSGuiListFooter           csGuiListFooterInstance;
    private MamStudioVolumesNode      mamStudioVolumesNodeInstance;
    private IVolumePopup              volumePopup;
    private ModalDialogPopupWindow    modalDialogPopupWindowInstance;
    private HashMap<String, String>   fileData;
    private ArrayList<String>         fileheader;
    private String                    scriptConfigTestSheet = "VerifyExecutionFromPimStudio";
    private HashMap<String, String>   importData;
    private ArrayList<String>         resultData;
    private String                    createDataFilePath;
    private String                    createUpdateFilePath;
    private int                       expectedJobCount;
    private int                       actualJobCount;

    /**
     * This test method executes the import script from PIM studio.
     * 
     * @param testData Array of String containing the test Data from Data
     *            driven.
     */
    @Test(dataProvider = "importScriptConfigTestData")
    public void testVerifyExecutionFromPimStudioImport(String... testData) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            readTestDataFromSheet(testData);
            checkFileExistence();
            executeImportScript(importData.get("targetFolderName"),
                    importData.get("volumeFolderName"),
                    importData.get("importFolderName"),
                    importData.get("importScriptName"));
            verifyExecutionFromPimStudioImport(
                    importData.get("importScriptName"),
                    "Update known and create unknown objects",
                    importData.get("propertiesScriptTab"),
                    importData.get("automationScriptTab"),
                    importData.get("userAccess"),
                    getFileNameFromPath(createUpdateFilePath),
                    importData.get("volumeFolderName"),
                    importData.get("importFolderName"));
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method: "
                    + "testVerifyExecutionFromPimStudioImport", e);
            Assert.fail("Automation error in test method: "
                    + "testVerifyExecutionFromPimStudioImport", e);
        }
    }

    /**
     * Checks whether import files exists.
     */
    private void checkFileExistence() {
        checkConfiguredFileExists(createDataFilePath);
        checkConfiguredFileExists(createUpdateFilePath);
    }

    /**
     * Creates and executes the import script.
     * 
     * @param targetFolderName String object contains target folder name.
     * @param volumeFolderName String object contains volume folder name.
     * @param importFolderName String object contains import folder name.
     * @param importScriptName String object contains import script name.
     */
    private void executeImportScript(String targetFolderName,
            String volumeFolderName, String importFolderName,
            String importScriptName) {
        createTargetProductFolder(targetFolderName);
        createImportMAMFolder(targetFolderName, volumeFolderName,
                importFolderName);
        uploadXMLFile(importFolderName, createDataFilePath);
        uploadXMLFile(importFolderName, createUpdateFilePath);
        fileData = readDataFromXML(createDataFilePath);
        createImportScript(importScriptName, importData.get("importCategory"));
        configureDataSourceSection(importData.get("dataSourceType"),
                volumeFolderName, importFolderName,
                getFileNameFromPath(createDataFilePath),
                importData.get("dataSourceSectionFields"));
        confiureDataTargetSection(importData.get("dataTargetType"),
                "Create unknown objects only", importData.get("dataTargetId"),
                targetFolderName, importData.get("defaultLanguage"),
                importData.get("startRowNumber"),
                importData.get("limitRowNumber"), importData.get("batchSize"),
                importData.get("NumOfmultiThread"), importData.get("idFilter"),
                importData.get("dataTargetSectionFields"));
        configureDataTransformationSection(importData.get("productTab"),
                importScriptName, importData.get("dataTransformationField"),
                importData.get("transformedName"),
                importData.get("transformationType"),
                importData.get("importInto"),
                importData.get("transformationLanguage"),
                importData.get("executionType"), importData.get("importLayer"),
                importData.get("validationType"));
        verifyImportedProduct(importData.get("propertiesScriptTab"),
                importData.get("dataTransformationField"),
                importData.get("transformedName"),
                importData.get("transformationType"),
                importData.get("importInto"),
                importData.get("transformationLanguage"),
                importData.get("executionType"), importData.get("importLayer"),
                importData.get("validationType"), importScriptName,
                targetFolderName);
    }

    /**
     * This method reads the test data from sheet.
     * 
     * @param testData String contains test data.
     */
    private void readTestDataFromSheet(String[] testData) {
        importData = new HashMap<>();
        String sheetData[] = { "targetFolderName", "volumeFolderName",
                "importScriptName", "importCategory", "productTab",
                "dataSourceType", "importFolderName", "dataSourceSectionFields",
                "propertiesScriptTab", "automationScriptTab",
                "dataTransformationField", "transformedName",
                "transformationType", "importInto", "transformationLanguage",
                "executionType", "importLayer", "validationType",
                "dataTargetType", "dataTargetId", "defaultLanguage",
                "startRowNumber", "limitRowNumber", "batchSize",
                "NumOfmultiThread", "idFilter", "dataTargetSectionFields",
                "userAccess" };
        try {
            for (int index = 0; index < sheetData.length; index++) {
                importData.put(sheetData[index], testData[index]);
            }
        } catch (Exception e) {
            CSLogger.debug("Error in test method : testVerifyDataTargetType",
                    e);
            Assert.fail("Error in test method : testVerifyDataTargetType", e);
            return;
        }
    }

    /**
     * Checks whether the configured file exists.
     * 
     * @param filePath String object contains file path.
     */
    private void checkConfiguredFileExists(String filePath) {
        boolean fileExists = new File(filePath).isFile();
        if (fileExists) {
            CSLogger.info("File is configured properly");
        } else {
            CSLogger.error("File that contains import data does not exists");
            Assert.fail("File that contains import data does not exists");
        }
    }

    /**
     * This method uploads the file that contains data to be imported.
     * 
     * @param targetFolderName String object contains target folder name.
     * @param volumeFolderName String object contains volume folder name
     * @param importFolderName String object contains volume folder name
     */
    public void createImportMAMFolder(String targetFolderName,
            String volumeFolderName, String importFolderName) {
        switchToMamAndExpandImportFolder(volumeFolderName, importFolderName);
        createImportFolder(volumeFolderName, importFolderName);
    }

    /**
     * Creates the target folder.
     * 
     * @param targetFolderName String object contains target folder name.
     */
    private void createTargetProductFolder(String targetFolderName) {
        switchToPimAndExpandProductsTree(waitForReload);
        createProduct(targetFolderName);
    }

    /**
     * This method creates the basic import script.
     * 
     * @param importScriptName String object contains import script name.
     * @param importCategory String object contains import category.
     */
    public void createImportScript(String importScriptName,
            String importCategory) {
        try {
            createImportActiveScript();
            configureImportScript(importScriptName, importCategory);
            saveImportActiveScript();
            verifyRequiredFieldsOfImportScript();
        } catch (Exception e) {
            CSLogger.debug("Import script creation failed" + e);
            Assert.fail("Import script creation failed" + e);
        }
    }

    /**
     * This method configures the data source section of import script.
     *
     * @param dataSourceType String object contains data source type.
     * @param volumeFolderName String object contains name of volume folder.
     * @param importFolderName String object contains name of import folder.
     * @param fileName String object contains name of file
     * @param dataSourceSectionFields String object contains data source section
     *            fields.
     */
    public void configureDataSourceSection(String dataSourceType,
            String volumeFolderName, String importFolderName, String fileName,
            String dataSourceSectionFields) {
        try {
            String dataSourceFields[] = spiltData(dataSourceSectionFields);
            checkDataSourceSectionExists();
            configureDataSourceSectionFields(dataSourceType, volumeFolderName,
                    importFolderName, fileName, dataSourceFields);
        } catch (Exception e) {
            CSLogger.debug("Data source configuration failed" + e);
            Assert.fail("Data source configuration failed" + e);
        }
    }

    /**
     * This method configures the data target section of import script.
     *
     * @param dataTargetType String object contains data target type.
     * @param scriptMode String object contains script mode.
     * @param dataTargetId String object contains data target ID.
     * @param targetFolderName String object contains name of target folder.
     * @param defaultLanguage String object contains default language.
     * @param startRowNumber String object contains start row number.
     * @param limitRowNumber String object contains limit row number.
     * @param batchSize String object contains batch size.
     * @param NumOfmultiThread String object contains number of multi-thread.
     * @param idFilter String object contains ID filter.
     * @param dataTargetSectionFields String object contains array of data
     *            target section fields.
     */
    public void confiureDataTargetSection(String dataTargetType,
            String scriptMode, String dataTargetId, String targetFolderName,
            String defaultLanguage, String startRowNumber,
            String limitRowNumber, String batchSize, String NumOfmultiThread,
            String idFilter, String dataTargetSectionFields) {
        try {
            traverseToScriptConfiguration();
            String dataTargetFields[] = spiltData(dataTargetSectionFields);
            verifyDataTargetSectionFields(dataTargetFields);
            configureDataTargetSection(dataTargetType, scriptMode, dataTargetId,
                    targetFolderName, defaultLanguage, startRowNumber,
                    limitRowNumber, batchSize, NumOfmultiThread, idFilter);
        } catch (Exception e) {
            CSLogger.debug("Data target configuration failed" + e);
            Assert.fail("Data target configuration failed" + e);
        }
    }

    /**
     * * This method verifies the transformed data.
     *
     * @param propertiesScriptTab String object contains name of tab.
     * @param dataTransformationField String object contains fields of data
     *            transformation separated by comma.
     * @param transformedName String object contains transformed name of an
     *            attribute. for e.g Defining Name as'Label'.
     * @param transformationType String object contains transformation type.
     * @param transformationStatus String object contains transformation status.
     * @param importInto String object contains values as Label or custom
     *            fields.
     * @param transformationLanguage String object contains transformation
     *            language
     * @param executionType String object contains execution type.
     * @param importLayer String object contains import layer.
     * @param validationType String object contains validation type.
     * @param importScriptName String object contains import script name.
     * @param targetFolderName String object contains target folder name.
     */
    private void verifyImportedProduct(String propertiesScriptTab,
            String dataTransformationField, String transformedName,
            String transformationType, String importInto,
            String transformationLanguage, String executionType,
            String importLayer, String validationType, String importScriptName,
            String targetFolderName) {
        try {
            executeImportScript(false);
            executeImportScript(true);
            verifyActiveScriptJob();
            printActiveScriptLogs();
            switchToPimAndExpandProductsTree(waitForReload);
            refreshPimProductsNode();
            clkOnCreatedProduct(targetFolderName);
            verifyImportedDataOfProduct(targetFolderName, propertiesScriptTab);
        } catch (Exception e) {
            CSLogger.debug(
                    "Data not imported properly verification failed " + e);
            Assert.fail("Data not imported properly verification failed " + e);
        }
    }

    /**
     * Creates the import active script.
     */
    private void createImportActiveScript() {
        switchToSettingsPage();
        goToSystemPreferencesIcon();
        expandActiveJobsTree();
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew().click();
        CSLogger.info("Clicked on create new tool bar button");
    }

    /**
     * Performs operation of clicking on system preference icon.
     */
    private void goToSystemPreferencesIcon() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
    }

    /**
     * This method traverse to the PIM module from Home module of application
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
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     */
    private void goToPimStudioTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName String object contains name of product.
     */
    private void createProduct(String productName) {
        goToPimStudioTreeSection();
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered value " + productName + " in the textfield.");
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimStudioTreeSection();
        CSLogger.info("Created Parent product");
    }

    /**
     * Switches to Media tab and expands the given volume folder and uploads the
     * given file.
     * 
     * @param volumeFolderName String object contains name of volume folder.
     * @param importFolderName String object contains name of import folder.
     */
    private void switchToMamAndExpandImportFolder(String volumeFolderName,
            String importFolderName) {
        csPortalHeaderInstance.clkBtnMedia(waitForReload);
        clkOnVolumesNode();
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
     * This method creates a volume folder.
     * 
     * @param volumefolderName String object contains name of volume folder
     *            under which the given volume folder will be created.
     * @param importFolderName String object contains name of volume folder
     */
    private void createImportFolder(String volumefolderName,
            String importFolderName) {
        mamStudioVolumesNodeInstance.expandNodesByVolumesName(waitForReload,
                volumefolderName);
        CSUtility.rightClickTreeNode(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + volumefolderName + "')]")),
                browserDriver);
        volumePopup.selectPopupDivMenu(waitForReload,
                volumePopup.getCreateNewFolder(), browserDriver);
        volumePopup.enterValueInDialogueMamStudio(waitForReload,
                importFolderName);
        volumePopup.askBoxWindowOperationMamStudio(waitForReload, true,
                browserDriver);
        CSLogger.info("Folder : " + importFolderName + " created successfully");
    }

    /**
     * This method uploads the given file.
     * 
     * @param importFolderName String object contains name of volume folder
     *            under which file is to be uploaded
     * @param filePath String object path of file to be imported.
     */
    private void uploadXMLFile(String importFolderName, String filePath) {
        try {
            CSUtility.travereToMamStudio(waitForReload, browserDriver);
            WebElement volumeFolder = browserDriver
                    .findElement(By.linkText(importFolderName));
            CSUtility.waitForVisibilityOfElement(waitForReload, volumeFolder);
            browserDriver.findElement(By.linkText(importFolderName))
                    .sendKeys(filePath);
            CSUtility.rightClickTreeNode(waitForReload, volumeFolder,
                    browserDriver);
            volumePopup.selectPopupDivMenu(waitForReload,
                    volumePopup.getClkUploadNewFile(), browserDriver);
            uploadFileFromDialogWindow(filePath);
        } catch (Exception e) {
            CSLogger.error("File upload failed", e);
            Assert.fail("File upload failed", e);
        }
    }

    /**
     * This method uploads file to the dialog window.
     * 
     * @param filePath String contains the file path.
     */
    private void uploadFileFromDialogWindow(String filePath) throws Exception {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                mamStudioVolumesNodeInstance.getBrowseFileToUpload()));
        mamStudioVolumesNodeInstance.getBrowseFileToUpload().click();
        selectFile(filePath);
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                mamStudioVolumesNodeInstance.getUploadButtonImage()));
        mamStudioVolumesNodeInstance.getUploadButtonImage().click();
        CSUtility.tempMethodForThreadSleep(2000);
        waitForReload.until(ExpectedConditions.visibilityOf(
                mamStudioVolumesNodeInstance.getClkCloseAfterUpload()));
        mamStudioVolumesNodeInstance.getClkCloseAfterUpload().click();
        CSLogger.info("File uploaded successfully");
    }

    /**
     * This method add the file to the clip board and clicks on enter.
     * 
     * @param filePath String contains path of file.
     * @throws Exception throws exception
     */
    private void selectFile(String filePath) throws Exception {
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
        CSLogger.info("Entered the file path");
    }

    /**
     * Configures the import script.
     * 
     * @param importLabel String object contains import label.
     * @param importCategory String object contains import category.
     */
    private void configureImportScript(String importLabel,
            String importCategory) {
        try {
            traverseToScriptConfiguration();
            activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                    activeJobsPageInstance.getTxtNewActiveScriptLabel(),
                    importLabel);
            activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                    activeJobsPageInstance.getTxtNewActiveScriptCategory(),
                    importCategory);
            activeJobsPageInstance.selectActiveScriptOption("Data Flow Import");
        } catch (Exception e) {
            CSLogger.error(
                    "Active script window does not contain fields 'Label', "
                            + "'Category' and 'Script' : test step failed",
                    e);
            Assert.fail("Active script window does not contain fields 'Label', "
                    + "'Category' and 'Script' : test step failed", e);
        }
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
     * Clicks on save button to save the configured import active script.
     */
    private void saveImportActiveScript() {
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * Verifies the fields of import script.
     */
    private void verifyRequiredFieldsOfImportScript() {
        try {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    activeJobsPageInstance.getCbContextSensitive());
            activeJobsPageInstance.getCbContextSensitive();
            activeJobsPageInstance.getLblOnlineHelp();
        } catch (Exception e) {
            CSLogger.error(
                    "Fields context sensitive and online help does not appear.");
            softAssertion.fail(
                    "Fields context sensitive and online help does not appear.");
        }
    }

    /**
     * Verifies whether section data source exists.
     *
     */
    private void checkDataSourceSectionExists() {
        try {
            clkOnGivenSection(activeJobsPageInstance.getSecDataSource(),
                    "Data Source");
            CSUtility.tempMethodForThreadSleep(5000);
        } catch (Exception e) {
            CSLogger.error("Data source section not found : test step failed");
            Assert.fail("Data source section not found : test step failed");
        }
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
     * /** Configures the data source section of import script.
     * 
     * @param dataSourceType String object contains data source type.
     * @param volumeFolderName String object contains name of volume folder.
     * @param importFolderName String object contains name of import folder.
     * @param fileName String object contains name of file
     * @param dataSourceFields String array contains data source section fields.
     */
    private void configureDataSourceSectionFields(String dataSourceType,
            String volumeFolderName, String importFolderName, String fileName,
            String[] dataSourceFields) {
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnImportDataSourceType(),
                dataSourceType);
        verifyDataSourceSectionFields(dataSourceFields);
        selectDataSourceFile(fileName, false, volumeFolderName,
                importFolderName);
        selectDataSourceFile(fileName, true, volumeFolderName,
                importFolderName);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtXmlObjectSelector(), "Article");
        saveImportActiveScript();
    }

    /**
     * Verifies the data source sections fields.
     * 
     * @param dataSourceFields String array contains data source section fields.
     */
    private void verifyDataSourceSectionFields(String[] dataSourceFields) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//div[@id='content__sections::Data Source']")));
        List<WebElement> getTableData = browserDriver.findElements(By
                .xpath("//div[@id='content__sections::Data Source']/table/tbody"
                        + "/tr/td" + "[1]"));
        verifyFields(getTableData, dataSourceFields, "data source");
    }

    /**
     * Selects the data source file
     * 
     * @param fileName String object contains name of file.
     * @param isPressOkay Boolean parameter contains true or false values.
     * @param volumeFolderName String object contains name of volume folder.
     * @param importFolderName String object contains name of import folder.
     */
    private void selectDataSourceFile(String fileName, Boolean isPressOkay,
            String volumeFolderName, String importFolderName) {
        activeJobsPageInstance.clkOnContextDataSourceFileOption(waitForReload);
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
        WebElement importfileName = browserDriver.findElement(By
                .xpath("//tr[@id='HeaderBarTop']/following-sibling::tr/td[contains(text(),'"
                        + fileName + "')]"));
        importfileName.click();
        modalDialogPopupWindowInstance
                .handlePopupDataSelectionDialog(waitForReload, isPressOkay);
        traverseToScriptConfiguration();
        String attachedFileName = activeJobsPageInstance.getCtnDataSourceFile()
                .getAttribute("value");
        if (isPressOkay) {
            if (attachedFileName.contains(fileName)) {
                CSLogger.info("Data source file : " + fileName
                        + " selected when clicked on ok button");
            } else {
                CSLogger.error("When clicked on ok button data source file : "
                        + fileName + " didn't select");
                softAssertion
                        .fail("When clicked on ok button data source file : "
                                + fileName + " didn't select");
            }
        } else {
            if (attachedFileName.contains(fileName)) {
                CSLogger.error("File : " + fileName
                        + " selected when clicked on cancel button");
                softAssertion.fail("File : " + fileName
                        + " selected when clicked on cancel button");
            } else {
                CSLogger.info("When clicked on cancel button file : " + fileName
                        + " didn't select");
            }
        }
        traverseToScriptConfiguration();
    }

    /**
     * Clicks on given volume folder.
     * 
     * @param volumeFolderName String object contains name of volume folder.
     */
    private void clkOnGivenVolumeFolder(String volumeFolderName) {
        WebElement volumesFolder = browserDriver
                .findElement(By.linkText(volumeFolderName));
        CSUtility.scrollUpOrDownToElement(volumesFolder, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload, volumesFolder);
        volumesFolder.click();
        CSLogger.info("Clicked on volume : " + volumeFolderName + " folder");
    }

    /**
     * Verifies the fields of data target section.
     * 
     * @param dataTargetType String object contains data target type.
     */
    private void verifyDataTargetSectionFields(String[] dataTargetFields) {
        try {
            clkOnGivenSection(activeJobsPageInstance.getSecDataTarget(),
                    "Data Target");
        } catch (Exception e) {
            CSLogger.error("Data target section not found : test step failed");
            Assert.fail("Data target section not found : test step failed");
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//div[@id='title__sections::Data Target']")));
        List<WebElement> getTableData = browserDriver.findElements(By
                .xpath("//div[@id='content__sections::Data Target']/table/tbody"
                        + "/tr/td[1]"));
        verifyFields(getTableData, dataTargetFields, "data target");
        CSLogger.info("Data target section fields verified successfully : test "
                + "step passed");
    }

    /**
     * Configures the data target section of import script.
     * 
     * @param dataTargetType String object contains data target type.
     * @param scriptMode String object contains script mode.
     * @param dataTargetId String object contains data target ID.
     * @param targetFolderName String object contains name of target folder.
     * @param defaultLanguage String object contains default language.
     * @param startRowNumber String object contains start row number.
     * @param limitRowNumber String object contains limit row number.
     * @param batchSize String object contains batch size.
     * @param NumOfmultiThread String object contains number of multi-thread.
     * @param idFilter String object contains ID filter.
     * 
     */
    private void configureDataTargetSection(String dataTargetType,
            String scriptMode, String dataTargetId, String targetFolderName,
            String defaultLanguage, String startRowNumber,
            String limitRowNumber, String batchSize, String NumOfmultiThread,
            String idFilter) {
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getdrpDwnImportDataTargetType(),
                dataTargetType);
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnActiveScriptMode(), scriptMode);
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnDataTargetId(), dataTargetId);
        selectTargetProductFolder(targetFolderName);
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnSelectDefaultLanguage(),
                defaultLanguage);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtActiveScriptStartRowNumber(),
                startRowNumber);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtActiveScriptMaxRows(),
                limitRowNumber);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtBatchSize(), batchSize);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtActiveScriptMultiThreads(),
                NumOfmultiThread);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtActivescriptLimitFilter(),
                idFilter);
        saveImportActiveScript();
    }

    /**
     * Selects the target folder.
     * 
     * @param productName String object contains name of product.
     */
    private void selectTargetProductFolder(String productName) {
        activeJobsPageInstance.getCtnImportTargetFolder().click();
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogProductTree(
                waitForReload, browserDriver);
        WebElement targetFolder = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + productName + "')]"));
        targetFolder.click();
        modalDialogPopupWindowInstance
                .handlePopupDataSelectionDialog(waitForReload, true);
        traverseToScriptConfiguration();
    }

    /**
     * Executes the configured import script.
     * 
     * @param isPressOkay Boolean parameter contains values whether to execute
     *            the script or not.
     */
    private void executeImportScript(Boolean isPressOkay) {
        try {
            expectedJobCount = getJobCount();
            traverseToScriptConfiguration();
            clkOnGivenTab("Properties");
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarRunActiveScript(waitForReload);
            Alert getAlertBox = CSUtility.getAlertBox(waitForReload,
                    browserDriver);
            alertText = "Are you sure to run this ActiveScript?";
            if (!(getAlertBox.getText().contains(alertText))) {
                CSLogger.error(
                        "Alert box saying : " + alertText + " does not appear");
                softAssertion.fail(
                        "Alert box saying : " + alertText + " does not appear");
            }
            if (isPressOkay) {
                getAlertBox.accept();
                if (isScriptExecuted()) {
                    CSLogger.info(
                            "When clicked on ok button of alert box import "
                                    + "script executed ");
                } else {
                    CSLogger.error(
                            "Import script didn't execute when clicked on ok"
                                    + " button of alert box");
                    softAssertion
                            .fail("Import script didn't execute when clicked on "
                                    + "ok button of alert box");
                }
            } else {
                getAlertBox.dismiss();
                if (isScriptExecuted()) {
                    CSLogger.error(
                            "Import script executed when clicked on cancel"
                                    + " button of alert box");
                    softAssertion.fail("Import script executed when clicked on "
                            + "cancel button of alert box");
                } else {
                    CSLogger.info(
                            "When clicked on cancel button of alert box import "
                                    + "script didn't execute ");
                }
            }
        } catch (Exception e) {
            CSLogger.error("Error while running the active script", e);
            Assert.fail("Error while running the active script", e);
        }
        traverseToScriptConfiguration();
    }

    /**
     * Returns the count of number of jobs on jobs tab.
     * 
     * @return integer value contains number of elements on job tab.
     */
    private int getJobCount() {
        clickOnJobsTab();
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
     * Checks whether script is executed.
     * 
     * @return Boolean value either true or false.
     */
    private boolean isScriptExecuted() {
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmShowingStatusOfRunningActiveScript()));
        int isScriptExecuted = browserDriver
                .findElements(By.id("ActiveScriptLogTable")).size();
        if (isScriptExecuted != 0) {
            return true;
        } else {
            return false;
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
                        + "[1]/td[2]/div/img[contains(@src,'circle_"
                        + "check.svg')]"))
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
        createdJob.click();
        CSLogger.info("Clicked on created job");
        CSUtility.tempMethodForThreadSleep(3000);
        verifyLogMenuBarFields();
    }

    /**
     * Verifies whether job is created.
     */
    private void verifyJobCreation() {
        actualJobCount = getJobCount();
        if ((actualJobCount == expectedJobCount + 1)) {
            CSLogger.info(
                    "Active script job created successfully : test step passed");
        } else {
            CSLogger.error(
                    "Active script job creation failed : test step failed");
            Assert.fail("Active script job creation failed : test step failed");
        }
    }

    /**
     * Clicks on jobs tab.
     */
    private void clickOnJobsTab() {
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//nobr[contains(text(),'Jobs')]/..")));
        browserDriver
                .findElement(By.xpath("//nobr[contains(text(),'Jobs')]/.."))
                .click();
        CSLogger.info("Clicked on the 'Jobs' tab");
    }

    /**
     * Verifies the menu bar fields of created job.
     */
    private void verifyLogMenuBarFields() {
        traverseToScriptConfiguration();
        CSUtility.tempMethodForThreadSleep(3000);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmShowingStatusOfRunningActiveScript()));
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//table[@id='ActiveScriptLogTable']")));
        CSUtility.tempMethodForThreadSleep(2000);
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
        WebElement numOfMessages = browserDriver
                .findElement(By.id("ActiveScriptMessageCount"));
        CSUtility.waitForVisibilityOfElement(waitForReload, numOfMessages);
        selectDrpDwnOption(numOfMessages, "All Messages");
        List<WebElement> activeJobLogs = browserDriver.findElements(
                By.xpath("//table[@id='ActiveScriptLogTable']/tbody/tr"));
        CSLogger.info("Level" + "\t" + "Time" + "\t" + "Total" + "\t" + "Diff"
                + "\t" + "All Messages");
        for (WebElement cell : activeJobLogs) {
            CSLogger.info(cell.getText());
        }
    }

    /**
     * Clicks on given product.
     * 
     * @param productName String object contains name of product.
     */
    private void clkOnCreatedProduct(String productName) {
        goToPimStudioTreeSection();
        WebElement product = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.waitForVisibilityOfElement(waitForReload, product);
        product.click();
        CSLogger.info("Clicked on product : " + productName);
    }

    /**
     * Clicks on given tab.
     * 
     * @param tabName String object contains name of product tab.
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
     * This method right clicks on product node and clicks on refresh sub menu
     * to refresh the PIM product tree.
     */
    private void refreshPimProductsNode() {
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getcsGuiPopupMenuRefresh(), browserDriver);
    }

    /**
     * Reads the data from XML file
     * 
     * @param filePath String object contains file path.
     */
    private HashMap<String, String> readDataFromXML(String filePath) {
        try {
            fileheader = new ArrayList<>();
            fileData = new HashMap<>();
            resultData = new ArrayList<>();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = docBuilderFactory
                    .newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            document.getDocumentElement().normalize();
            int tagCount = document.getElementsByTagName("Article").getLength();
            for (int tag = 0; tag < tagCount; tag++) {
                Node mainNode = document.getElementsByTagName("Article")
                        .item(tag);
                NodeList attributeNodes = mainNode.getChildNodes();
                for (int index = 0; index < attributeNodes
                        .getLength(); index++) {
                    Node attributeNode = attributeNodes.item(index);
                    if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
                        fileheader.add(attributeNode.getNodeName());
                        fileData.put(attributeNode.getNodeName(),
                                attributeNode.getTextContent());
                        resultData.add(attributeNode.getTextContent());
                    }
                }
            }
        } catch (Exception e) {
            CSLogger.error("Error while reading xml file", e);
            Assert.fail("Error while reading xml file", e);
        }
        return fileData;
    }

    /**
     * Expands the active jobs tree node.
     */
    private void expandActiveJobsTree() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        performAction.doubleClick(activeJobsPageInstance.getNodeActiveJobs())
                .perform();
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * Switches to Settings tab.
     */
    private void switchToSettingsPage() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
    }

    /**
     * Clicks on given import script.
     * 
     * @param filePath String object contains script name.
     */
    private void clkOnCreatedImportScript(String scriptName) {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        enterDataInFilterBar(waitForReload, scriptName);
        WebElement createdImportScript = browserDriver.findElement(
                By.xpath("//tr[@id='HeaderBarTop']/following-sibling::tr/"
                        + "td[contains(text(),'" + scriptName + "')]"));
        CSUtility.scrollUpOrDownToElement(createdImportScript, browserDriver);
        createdImportScript.click();
        CSLogger.info("Clicked on created import script : " + scriptName);
    }

    /**
     * Enters the data in filter bar.
     * 
     * @param waitForReload WebDriverWait object.
     * @param data String object contains data to be entered in filter bar.
     */
    public void enterDataInFilterBar(WebDriverWait waitForReload, String data) {
        CSUtility.scrollUpOrDownToElement(
                csGuiToolbarHorizontalInstance.getBtnFilter(), browserDriver);
        String filterTextBoxValue = browserDriver
                .findElement(By.xpath("//tr[@id='FilterBarTop']"))
                .getAttribute("style");
        if (filterTextBoxValue.equals("display: none;")) {
            csGuiToolbarHorizontalInstance.clkOnBtnFilter(waitForReload);
        }
        csGuiToolbarHorizontalInstance.clkOnWebElement(waitForReload,
                csGuiToolbarHorizontalInstance.getTxtFilterBar(), "filter bar");
        csGuiToolbarHorizontalInstance.getTxtFilterBar().clear();
        csGuiToolbarHorizontalInstance.getTxtFilterBar().sendKeys(data);
        csGuiToolbarHorizontalInstance.getTxtFilterBar().sendKeys(Keys.ENTER);
        CSLogger.info("Entered text  " + data + "in filter bar");
    }

    /**
     * Configures the data transformation section of import script.
     * 
     * @param productTab String object contains product tab name.
     * @param propertiesScriptTab String object contains properties tab name.
     * @param transformationFields String object contains array of data
     *            transformation fields.
     * @param transformedName String object contains transformed name of an
     *            attribute. for e.g Defining Name as'Label'.
     * @param transformationType String object contains transformation type.
     * @param importInto String object contains values as Label or custom
     *            fields.
     * @param transformationLanguage String object contains transformation
     *            language.
     * @param executionType String object contains execution type.
     * @param importLayer String object contains import layer.
     * @param validationType String object contains validation type.
     * 
     */
    private void configureDataTransformationSection(String productTab,
            String propertiesScriptTab, String dataTransformationField,
            String transformedName, String transformationType,
            String importInto, String transformationLanguage,
            String executionType, String importLayer, String validationType) {
        String[] transformationFields = spiltData(dataTransformationField);
        traverseToScriptConfiguration();
        String parentWindow = browserDriver.getWindowHandle();
        WebElement propertiesTab = browserDriver.findElement(
                By.xpath("//nobr[contains(text(),'" + productTab + "')]"));
        propertiesTab.click();
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.scrollUpOrDownToElement(
                activeJobsPageInstance.getSecDataTransformation(),
                browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        clkOnGivenSection(activeJobsPageInstance.getSecDataTransformation(),
                "Data Transformation");
        activeJobsPageInstance.clkWebElement(
                activeJobsPageInstance.getBtnImportEditTransformation());
        CSUtility.tempMethodForThreadSleep(1000);
        String childWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(childWindow);
        verifyTransformationWindow(propertiesScriptTab);
        performMappingOperation(fileheader.get(1), transformationFields,
                transformedName, transformationType, importInto,
                transformationLanguage, executionType, importLayer,
                validationType);
        browserDriver.switchTo().window(parentWindow);
        selectFieldFromApplyTransformation(transformedName);
        saveImportActiveScript();
    }

    /**
     * Performs the mapping operation on transformation window.
     * 
     * @param dataSourceLabelField String object contains data source field
     *            name.
     * @param transformationFields String object contains array of data
     *            transformation fields.
     * @param transformedName String object contains transformed name of an
     *            attribute. for e.g Defining Name as'Label'.
     * @param transformationType String object contains transformation type.
     * @param importInto String object contains values as Label or custom
     *            fields.
     * @param transformationLanguage String object contains transformation
     *            language.
     * @param executionType String object contains execution type.
     * @param importLayer String object contains import layer.
     * @param validationType String object contains validation type.
     */
    private void performMappingOperation(String dataSourceLabelField,
            String[] transformationFields, String transformedName,
            String transformationType, String importInto,
            String transformationLanguage, String executionType,
            String importLayer, String validationType) {
        traverseToTransformationMainFrame();
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//tr[@id='HeaderBarTop']")));
        int isFieldAutoMapped = browserDriver
                .findElements(By
                        .xpath("//tr[@id='HeaderBarTop']/following-sibling::tr/"
                                + "td[contains(text(),'" + dataSourceLabelField
                                + "')]/../td[contains(text(),'Auto Mapped')]"))
                .size();
        if (isFieldAutoMapped == 0) {
            traverseToTransformationTopFrame();
            WebElement dataSourceElement = browserDriver.findElement(
                    By.xpath("//html/body/div[2]/child::*/child::*"));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    dataSourceElement);
            WebElement dataTargetElement = browserDriver.findElement(
                    By.xpath("//html/body/div[3]/child::*/child::*"));
            CSUtility.tempMethodForThreadSleep(5000);
            performAction.dragAndDrop(dataSourceElement, dataTargetElement)
                    .perform();
            CSLogger.info("Performed drag drop function");
            clkOnRefreshToolbarButton();
            WebElement mappedField = browserDriver.findElement(By
                    .xpath("(//tr[@id='HeaderBarTop']/following-sibling::tr/td"
                            + "[contains(text(),'" + fileheader.get(1)
                            + "')]/../td[contains(text(),'Unmapped')])[2]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, mappedField);
            mappedField.click();
        } else {
            WebElement autoMappedField = browserDriver.findElement(
                    By.xpath("//tr[@id='HeaderBarTop']/following-sibling::tr/td"
                            + "[contains(text(),'" + dataSourceLabelField
                            + "')]/../td[contains(text(),'Auto Mapped')]"));
            autoMappedField.click();
        }
        verifyTransformationFields(transformationFields);
        configureTransformation(dataSourceLabelField, transformedName,
                transformationType, importInto, transformationLanguage,
                executionType, importLayer, validationType);
    }

    /**
     * Switches to top frame of transformation window.
     */
    private void traverseToTransformationTopFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTop()));
    }

    /**
     * Clicks on refresh button from transformation window.
     */
    private void clkOnRefreshToolbarButton() {
        traverseToTransformationMainFrame();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarRefresh(waitForReload);
    }

    /**
     * Traverses till main frame of transformation window.
     */
    private void traverseToTransformationMainFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameFrmCenter()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
    }

    /**
     * Configures the transformation data.
     * 
     * @param dataSourceFieldName String object contains data source field name.
     * @param transformedName String object contains transformed name of an
     *            attribute. for e.g Defining Name as'Label'.
     * @param transformationType String object contains transformation type.
     * @param importInto String object contains values as Label or custom
     *            fields.
     * @param transformationLanguage String object contains transformation
     *            language.
     * @param executionType String object contains execution type.
     * @param importLayer String object contains import layer.
     * @param validationType String object contains validation type.
     */
    private void configureTransformation(String dataSourceFieldName,
            String transformedName, String transformationType,
            String importInto, String transformationLanguage,
            String executionType, String importLayer, String validationType) {
        WebElement label = browserDriver.findElement(By.xpath(
                "//input[contains(@id,'DataflowimporttransformationLabel')]"));
        enterTextIntoTextbox(label, transformedName);
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement drpDwnTransformationType = browserDriver.findElement(By
                .xpath("//select[contains(@id,'DataflowimporttransformationImpor"
                        + "tTransformationType')]/option[contains(text(),'"
                        + transformationType + "')]"));
        drpDwnTransformationType.click();
        selectDataSourceField(dataSourceFieldName);
        WebElement drpDwnTargetKeyFieldType = browserDriver.findElement(
                By.xpath("//select[contains(@id,'Dataflowimporttransformation"
                        + "InternalTargetKeyFieldType')]/option[contains(text(),'"
                        + importInto + "')]"));
        drpDwnTargetKeyFieldType.click();
        WebElement drpDwnTransformationLanguage = browserDriver.findElement(By
                .xpath("//select[contains(@id,'DataflowimporttransformationLang"
                        + "uageID')]"));
        selectDrpDwnOption(drpDwnTransformationLanguage,
                transformationLanguage);
        traverseToEditTransformation();
        WebElement drpDwnExecution = browserDriver.findElement(By
                .xpath("//select[contains(@id,'DataflowimporttransformationCond"
                        + "ition')]/option[@title='" + executionType + "']"));
        drpDwnExecution.click();
        CSLogger.info("Selected execution as : " + executionType);
        WebElement drpDwnImportLayer = browserDriver.findElement(
                By.xpath("//select[contains(@id,'Dataflowimporttransformation"
                        + "ImportLevel" + "')]"));
        selectDrpDwnOption(drpDwnImportLayer, importLayer);
        WebElement drpDwnValidation = browserDriver.findElement(
                By.xpath("//select[contains(@id,'Dataflowimporttransformation"
                        + "Validation" + "')]"));
        selectDrpDwnOption(drpDwnValidation, validationType);
        saveImportActiveScript();
        CSUtility.switchToDefaultFrame(browserDriver);
        WebElement closeDialog = browserDriver
                .findElement(By.id("CSGUI_MODALDIALOG_OKBUTTON"));
        closeDialog.click();
        CSLogger.info("Clicked on closed dialog button");
    }

    /**
     * Traverse till right section frames of transformation window.
     */
    private void traverseToEditTransformation() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameFrmCenter()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEditForActiveScript()));
    }

    /**
     * Selects the given field.
     * 
     * @param dataSourceFieldName String object contains field name.
     */
    private void selectDataSourceField(String dataSourceFieldName) {
        CSUtility.tempMethodForThreadSleep(1000);
        int isDataSourceFieldSelected = browserDriver.findElements(By
                .xpath("//select[contains(@id,'DataflowimporttransformationSourc"
                        + "eFieldsNotSelected')]/option[contains(@value,'"
                        + dataSourceFieldName + "')]"))
                .size();
        if (isDataSourceFieldSelected == 0) {
            CSLogger.info("Data source field " + dataSourceFieldName
                    + " is already selected");
        } else {
            performAction.doubleClick(browserDriver.findElement(By.xpath(
                    "//select[contains(@id,'Dataflowimporttransf" + "ormationS"
                            + "ourceFieldsNotSelected')]/option[contains"
                            + "(@value,'" + dataSourceFieldName + "')]")))
                    .perform();
            CSLogger.info(
                    "Selected : " + dataSourceFieldName + " data source field");
        }
    }

    /**
     * This method enters the given text into transformation windows text box.
     * 
     * @param element WebElement of text box.
     * @param text -String object contains value to be entered into text box
     */
    public void enterTextIntoTextbox(WebElement element, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        element.clear();
        element.sendKeys(text);
        CSLogger.info("Entered " + text + " into text box");
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
        CSUtility.tempMethodForThreadSleep(1000);
        drpDwnElement.click();
        CSUtility.tempMethodForThreadSleep(1000);
        Select element = new Select(drpDwnElement);
        element.selectByVisibleText(option);
        CSLogger.info("Drop down option " + option + " selected");
    }

    /**
     * Selects the given field from apply transformation.
     * 
     * @param fieldName String object contains name of field.
     */
    private void selectFieldFromApplyTransformation(String fieldName) {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, iframeLocatorsInstance);
        WebElement fieldToBeSelected = browserDriver.findElement(By
                .xpath("//select[contains(@id,'ActivescriptTransformationIDsNot"
                        + "Selected')]/option[contains(text(),'" + fieldName
                        + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, fieldToBeSelected);
        fieldToBeSelected.click();
        CSLogger.info(
                "Field " + fieldName + " selected from apply transformation");
    }

    /**
     * Verifies whether proper product data is imported.
     * 
     * @param parentProductName String object contains name of parent product.
     * @param productTabName String object contains name of product tab.
     */
    private void verifyImportedDataOfProduct(String parentProductName,
            String productTabName) {
        String transformedLabel = fileData.get(fileheader.get(1));
        goToPimStudioTreeSection();
        WebElement importedProduct = browserDriver.findElement(By.xpath(
                "//span[contains(text(),'" + parentProductName + "')]/../../.."
                        + "/../../../../../../../../following-sibling::span/span"
                        + "/table/tbody/tr/td/span/table/tbody/tr/td[2]/a/span/"
                        + "span"));
        CSUtility.waitForVisibilityOfElement(waitForReload, importedProduct);
        importedProduct.click();
        CSLogger.info("Clicked on imported product");
        int isLabelTransformed = browserDriver
                .findElements(By.linkText(transformedLabel)).size();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        clkOnGivenTab(productTabName);
        WebElement externalKey = browserDriver.findElement(
                By.xpath("//tr[@id='AttributeRow_ExternalKey']/td[2]/span"));
        CSUtility.waitForVisibilityOfElement(waitForReload, externalKey);
        if (isLabelTransformed != 0 && externalKey.getText()
                .equals(fileData.get(fileheader.get(0)))) {
            CSLogger.info(
                    "Verified passed : found imported label and external key");
        } else {
            CSLogger.error(
                    "Verification failed : imported label and external key not "
                            + "found");
            softAssertion
                    .fail("Verification failed : imported label and external "
                            + "key not found");
        }
    }

    /**
     * Verifies the title and transformation labels of transformation window.
     * 
     * @param scriptName String object contains name of script.
     */
    private void verifyTransformationWindow(String scriptName) {
        WebElement drpDwnDisplayMapping = browserDriver.findElement(By
                .xpath("//div[@class='sectionimage'][contains(text(),' Display "
                        + "Transformation Mapping')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                drpDwnDisplayMapping);
        drpDwnDisplayMapping.click();
        CSLogger.info("Clicked in 'Display Transformation Mapping'");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.id("CSGuiModalDialogFormName")));
        int windowTitle = browserDriver.findElements(By
                .xpath("//td[@class='CSGUI_MODALDIALOG_TITLE'][contains(text(),'"
                        + "\"" + scriptName + "\"" + "')]"))
                .size();
        traverseToTransformationTopFrame();
        int leftHeaderElement = browserDriver
                .findElements(By
                        .xpath("//td[@class='container-left']/h2/select/option"
                                + "[contains(text(),'All Data Source Fields')]"))
                .size();
        int centerHeaderElement = browserDriver.findElements(
                By.xpath("//td[@class='container-center']/h2[contains"
                        + "(text(),'Data Transformation')]"))
                .size();
        int rightHeaderElement = browserDriver
                .findElements(By
                        .xpath("//td[@class='container-right']/h2/select/option"
                                + "[contains(text(),'All Data Target Fields')]"))
                .size();
        if (windowTitle != 0 && leftHeaderElement != 0
                && centerHeaderElement != 0 && rightHeaderElement != 0) {
            CSLogger.info(
                    "Transformation pop up window opened with label as follows:"
                            + "'Transformations for " + scriptName
                            + " 'All Data Source Fields(Left)', 'Data "
                            + "Transformation'(Middle) and All Data "
                            + "Target(Right) sections should be displayed.");
        } else {
            CSLogger.error(
                    "Error in test step : 'Transformation pop up window should "
                            + "be open with label as follows: Transformations for "
                            + scriptName + " 'All Data Source Fields(Left)', "
                            + "'Data Transformation'(Middle) and All "
                            + "Data Target(Right) sections should be "
                            + "displayed");
            softAssertion
                    .fail("Error in test step : 'Transformation pop up window "
                            + "should "
                            + "be open with label as follows: Transformations for "
                            + scriptName + " 'All Data Source Fields(Left)', "
                            + "'Data Transformation'(Middle) and All "
                            + "Data Target(Right) sections should be "
                            + "displayed");
        }
    }

    /**
     * Checks the transformation fields.
     * 
     * @param transformationFields String object contains array of
     *            transformation fields.
     */
    private void verifyTransformationFields(String[] transformationFields) {
        traverseToEditTransformation();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//table[@class='CSGuiTable GuiEditorTable spacer']")));
        List<WebElement> getTableData = browserDriver.findElements(By
                .xpath("//table[@class='CSGuiTable GuiEditorTable spacer']/tbody"
                        + "/tr/td[1]"));
        verifyFields(getTableData, transformationFields, "data transformation");
        CSLogger.info("Transformation fields verified successfully : test "
                + "step passed");
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
     * This method verifies different section of default fields.
     * 
     * @param tableData List parameter holds list of WebElements.
     * @param fields String object contains array of fields to be verified.
     * @param section String object specifies the section. i.e data target,data
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
     * Selects the given script mode.
     * 
     * @param importScriptName String object contains name of script.
     * @param scriptMode String object contains script mode.
     * @param propertiesScriptTab String object contains script tab name.
     */
    private void changeScriptMode(String importScriptName, String scriptMode,
            String propertiesScriptTab) {
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnActiveScriptMode(), scriptMode);
        saveImportActiveScript();
    }

    /**
     * Traverses to created import script.
     * 
     * @param importScriptName String object contains import script name.
     * @param propertiesScriptTab String object contains tab name.
     */
    private void goToCreatedImportScript(String importScriptName,
            String propertiesScriptTab) {
        switchToSettingsPage();
        goToSystemPreferencesIcon();
        expandActiveJobsTree();
        clkOnCreatedImportScript(importScriptName);
        traverseToScriptConfiguration();
        clkOnGivenTab(propertiesScriptTab);
    }

    /**
     * Executes the import script from PIM studio and verifies the created and
     * updated data.
     * 
     * @param importScriptName String object contains import script name.
     * @param scriptMode String object contains script mode.
     * @param propertiesScriptTab String object contains tab name.
     * @param automationTabName String object contains tab name.
     * @param allowAccessOption String object contains allow access option.
     * @param fileName String object contains file name.
     * @param volumeFolderName String object contains volume folder name.
     * @param importFolderName String object contains import folder name.
     */
    private void verifyExecutionFromPimStudioImport(String importScriptName,
            String scriptMode, String propertiesScriptTab,
            String automationTabName, String allowAccessOption, String fileName,
            String volumeFolderName, String importFolderName) {
        goToCreatedImportScript(importScriptName, propertiesScriptTab);
        activeJobsPageInstance.checkContextSensitiveCheckBox();
        saveImportActiveScript();
        changeScriptMode(importScriptName, scriptMode, propertiesScriptTab);
        selectDataSourceFile(fileName, true, volumeFolderName,
                importFolderName);
        saveImportActiveScript();
        setAllowAccessByField(automationTabName, allowAccessOption);
        executeScriptFromPimStudioTree(importScriptName, false,
                propertiesScriptTab, automationTabName);
        executeScriptFromPimStudioTree(importScriptName, true,
                propertiesScriptTab, automationTabName);
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
        saveImportActiveScript();
    }

    /**
     * This method executes the import script from PIM studio.
     * 
     * @param scriptName String object contains import script name.
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
        scriptOption.click();
        CSLogger.info("Select script option: " + scriptName + " from popup");
        verifyElementsOfExecuteWindow(propertiesScriptTab, automationScriptTab);
        if (isPressOkay) {
            performActionsOnWindows(parentWindow);
            readDataFromXML(createUpdateFilePath);
            verifyImportedProducts(importData.get("targetFolderName"));
        } else {
            WebElement cancelButton = browserDriver.findElement(By.xpath(
                    "//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL']"));
            clkOnGivenButton(cancelButton, "Cancel");
            Set<String> executeWindow = browserDriver.getWindowHandles();
            if (executeWindow.size() == 0) {
                CSLogger.error(
                        "Import script executed when clicked on cancel button");
                softAssertion.fail(
                        "Import script executed when clicked on cancel button");
            }
        }
    }

    /**
     * Perform actions on import script windows.
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
     * Returns the target product element.
     * 
     * @return WebElement targetProduct
     */
    private WebElement getTargetProduct() {
        WebElement targetProduct = browserDriver
                .findElement(By.linkText(importData.get("targetFolderName")));
        CSUtility.waitForVisibilityOfElement(waitForReload, targetProduct);
        return targetProduct;
    }

    /**
     * Clicks on products header.
     */
    private void switchToProductsPage() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
    }

    /**
     * Verifies the execute window.
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
        int dataSourceFileElementExists = browserDriver
                .findElements(By.xpath("//tr[@cs_name='Data Source File']"))
                .size();
        int targetFolderElement = browserDriver
                .findElements(By.xpath("//tr[@cs_name='Target Folder']"))
                .size();
        if (!(presetElementExists != 0 && dataSourceFileElementExists != 0
                && targetFolderElement != 0)) {
            CSLogger.error("Elements not found on execute window");
            softAssertion.fail("Elements not found on execute window");
        }
        return presetElementExists;
    }

    /**
     * Verifies the imported data when data target option is selected as 'Update
     * Known And Create Unknown Objects'.
     * 
     * @param targetProductName String object contains product name.
     */
    private void verifyImportedProducts(String targetProductName) {
        String updatedKnownProductExternalKey = resultData.get(0);
        String updatedKnownProductLabel = resultData.get(1);
        String unKnownCreatedProductExternalKey = resultData.get(2);
        String unKnownCreatedProductLabel = resultData.get(3);
        switchToPimAndExpandProductsTree(waitForReload);
        refreshPimProductsNode();
        clkOnCreatedProduct(targetProductName);
        verifyKnownUpdatedProduct(targetProductName,
                updatedKnownProductExternalKey, updatedKnownProductLabel);
        verifyUnknownProductCreation(targetProductName,
                unKnownCreatedProductExternalKey, unKnownCreatedProductLabel);
    }

    /**
     * Verifies whether known product's data is updated when data target mode is
     * selected as 'Update Known And Create Unknown Objects'
     * 
     * @param parentProductName String object contains product name under which
     *            new Product's while be imported.
     * @param updatedKnownProductExternalKey String object contains external
     *            key.
     * @param updatedKnownProductLabel String object contains product name.
     */
    private void verifyKnownUpdatedProduct(String parentProductName,
            String updatedKnownProductExternalKey,
            String updatedKnownProductLabel) {
        goToPimStudioTreeSection();
        WebElement updatedKnownProduct = browserDriver.findElement(By.xpath(
                "//span[contains(text(),'" + parentProductName + "')]/../../.."
                        + "/../../../../../../../../following-sibling::span/span"
                        + "/table/tbody/tr/td/span/table/tbody/tr/td[2]/a/span/"
                        + "span"));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                updatedKnownProduct);
        updatedKnownProduct.click();
        int updatedlabel = browserDriver
                .findElements(By.linkText(updatedKnownProductLabel)).size();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement updatedExternalKey = browserDriver.findElement(
                By.xpath("//tr[@id='AttributeRow_ExternalKey']/td[2]/span"));
        CSUtility.waitForVisibilityOfElement(waitForReload, updatedExternalKey);
        if (updatedlabel != 0 && updatedExternalKey.getText()
                .equals(updatedKnownProductExternalKey)) {
            CSLogger.info("known Product updated successfully");
        } else {
            CSLogger.error("Verification failed : Known Product didn't update");
            softAssertion
                    .fail("Verification failed : Known Product didn't update");
        }
    }

    /**
     * Verifies whether unknown product is created when data target mode is
     * selected as 'Update Known And Create Unknown Objects'
     * 
     * @param parentProductName String object contains product name under which
     *            new product's while be imported.
     * @param unKnownCreatedProductExternalKey String object contains external
     *            key.
     * @param unKnownCreatedProductLabel String object contains product name.
     */
    private void verifyUnknownProductCreation(String parentProductName,
            String unKnownCreatedProductExternalKey,
            String unKnownCreatedProductLabel) {
        goToPimStudioTreeSection();
        WebElement unKnownCreatedProduct = browserDriver.findElement(By.xpath(
                "(//span[contains(text(),'" + parentProductName + "')]/../../.."
                        + "/../../../../../../../../following-sibling::span/span"
                        + "/table/tbody/tr/td/span/table/tbody/tr/td[2]/a/span/"
                        + "span)[2]"));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                unKnownCreatedProduct);
        unKnownCreatedProduct.click();
        CSUtility.tempMethodForThreadSleep(3000);
        int createdlabel = browserDriver
                .findElements(By.linkText(unKnownCreatedProductLabel)).size();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement createdExternalKey = browserDriver.findElement(
                By.xpath("//tr[@id='AttributeRow_ExternalKey']/td[2]/span"));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdExternalKey);
        if (createdlabel != 0 && createdExternalKey.getText()
                .equals(unKnownCreatedProductExternalKey)) {
            CSLogger.info("Unknown Product created successfully");
        } else {
            CSLogger.error("Unknown Product creation failed");
            softAssertion.fail("Unknown Product creation failed");
        }
    }

    /**
     * Returns the file name from given file path.
     * 
     * @param filePath String object contains file path
     * @return String name of file
     */
    private String getFileNameFromPath(String filePath) {
        String[] fileName = filePath.replaceAll(Pattern.quote("\\"), "\\\\")
                .split("\\\\");
        return fileName[fileName.length - 1];
    }

    /**
     * This is a data provider method returns import data.
     * 
     * @return Array String array consisting of import data
     */
    @DataProvider(name = "importScriptConfigTestData")
    public Object[][] getImportScriptConfigTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("dataflowImportModuleTestCases"),
                scriptConfigTestSheet);
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
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        csGuiListFooterInstance = new CSGuiListFooter(browserDriver);
        mamStudioVolumesNodeInstance = MamStudioTree
                .getMamStudioVolumesNodeInstance(browserDriver);
        volumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
        createDataFilePath = config.getDataflowTestDataFolder()
                + "DataflowExecutionFromPimStudio-Create.xml";
        createUpdateFilePath = config.getDataflowTestDataFolder()
                + "DataflowExecutionFromPimStudio-CreateUpdate.xml";
    }
}
