
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
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioTree;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IChannelPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
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
 * This class contains the test method to create File via data flow import.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateFileFromImportTest extends AbstractTest {

    private WebDriverWait           waitForReload;
    private CSPortalHeader          csPortalHeaderInstance;
    private CSPortalWidget          csPortalWidgetInstance;
    private ActiveJobsPage          activeJobsPageInstance;
    private FrameLocators           iframeLocatorsInstance;
    private CSGuiToolbarHorizontal  csGuiToolbarHorizontalInstance;
    private Actions                 performAction;
    private IChannelPopup           channelPopup;
    private SoftAssert              softAssertion;
    private String                  alertText;
    private CSGuiListFooter         csGuiListFooterInstance;
    private MamStudioVolumesNode    mamStudioVolumesNodeInstance;
    private IVolumePopup            volumePopup;
    private ModalDialogPopupWindow  modalDialogPopupWindowInstance;
    private HashMap<String, String> fileData;
    private ArrayList<String>       fileheader;
    private String                  scriptConfigTestSheet = "CreateFileFromImport";
    private HashMap<String, String> importData;
    private ArrayList<String>       resultData;
    private String                  createMamDataFilePath;
    private String                  updateMamDataFilePath;
    private String                  createUpdateMamDataFilePath;
    private int                     expectedJobCount;
    private int                     actualJobCount;

    /**
     * This test method creates File via data flow import.
     * 
     * @param testData
     *            Array of String containing the test Data from Data driven.
     */
    @Test(dataProvider = "importScriptConfigTestData")
    public void testCreateFileFromImport(String... testData) {
        try {
            softAssertion = new SoftAssert();
            readTestDataFromSheet(testData);
            uploadDataFiles();
            verifyModeCreateUnknownObjectsOnly();
            verfiyModeUpdateKnownObjectsOnly();
            verifyModeUpdateKnownCreateUnknownObjects();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method: testCreateFileFromImport",
                    e);
            Assert.fail(
                    "Automation error in test method: testCreateFileFromImport",
                    e);
        }
    }

    /**
     * This method reads the test data from sheet.
     * 
     * @param testData
     *            String array contains test data.
     */
    private void readTestDataFromSheet(String[] testData) {
        importData = new HashMap<>();
        String sheetData[] = { "targetFolderName", "volumeFolderName",
                "importScriptName", "importCategory", "tabName",
                "dataSourceType", "importFolderName", "dataSourceSectionFields",
                "scriptTab", "dataTransformationField", "transformedName",
                "transformationType", "importInto", "transformationLanguage",
                "executionType", "importLayer", "validationType",
                "dataTargetType", "dataTargetId", "defaultLanguage",
                "startRowNumber", "limitRowNumber", "batchSize",
                "NumOfmultiThread", "idFilter", "dataTargetSectionFields",
                "updateScriptMode" };
        try {
            for (int index = 0; index < sheetData.length - 1; index++) {
                importData.put(sheetData[index], testData[index]);
            }
        } catch (Exception e) {
            CSLogger.debug("Error while reading test sheet data", e);
            Assert.fail("Error while reading test sheet data", e);
            return;
        }
    }

    /**
     * Uploads the configured files that contains the import data.
     */
    private void uploadDataFiles() {
        checkConfiguredFileExists(createMamDataFilePath);
        checkConfiguredFileExists(updateMamDataFilePath);
        checkConfiguredFileExists(createUpdateMamDataFilePath);
        createTargetMAMFolder(importData.get("volumeFolderName"),
                importData.get("targetFolderName"));
        createImportMAMFolder(importData.get("volumeFolderName"),
                importData.get("importFolderName"));
        uploadXMLFile(importData.get("importFolderName"),
                createMamDataFilePath);
        uploadXMLFile(importData.get("importFolderName"),
                updateMamDataFilePath);
        uploadXMLFile(importData.get("importFolderName"),
                createUpdateMamDataFilePath);
    }

    /**
     * Checks whether the configured file exists.
     * 
     * @param filePath
     *            String object contains file path.
     */
    private void checkConfiguredFileExists(String filePath) {
        Boolean fileExists = new File(filePath).isFile();
        if (fileExists) {
            CSLogger.info("File is configured properly");
        } else {
            CSLogger.error("File that contains import data does not exists");
            Assert.fail("File that contains import data does not exists");
        }
    }

    /**
     * This method uploads the given file into MAM folder.
     * 
     * @param importFolderName
     *            String object contains name of volume folder under which file
     *            is to be uploaded
     * @param filePath
     *            String object path of file to be imported.
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
     * This method verifies the execution of when data target mode is selected
     * as 'Create Unknown Objects Only'.
     */
    private void verifyModeCreateUnknownObjectsOnly() {
        fileData = readDataFromXML(createMamDataFilePath);
        createImportScript(importData.get("importScriptName"),
                importData.get("importCategory"));
        configureDataSourceSectionTest(importData.get("dataSourceType"),
                importData.get("volumeFolderName"),
                importData.get("importFolderName"),
                importData.get("dataSourceSectionFields"));
        confiureDataTargetSection(importData.get("dataTargetType"),
                importData.get("dataTargetId"),
                importData.get("targetFolderName"),
                importData.get("defaultLanguage"),
                importData.get("startRowNumber"),
                importData.get("limitRowNumber"), importData.get("batchSize"),
                importData.get("NumOfmultiThread"), importData.get("idFilter"),
                importData.get("dataTargetSectionFields"));
        configureDataTransformationSection(importData.get("tabName"),
                importData.get("importScriptName"),
                importData.get("dataTransformationField"),
                importData.get("transformedName"),
                importData.get("transformationType"),
                importData.get("importInto"),
                importData.get("transformationLanguage"),
                importData.get("executionType"), importData.get("importLayer"),
                importData.get("validationType"));
        verifyImportedFile(importData.get("scriptTab"),
                importData.get("dataTransformationField"),
                importData.get("transformedName"),
                importData.get("transformationType"),
                importData.get("importInto"),
                importData.get("transformationLanguage"),
                importData.get("executionType"), importData.get("importLayer"),
                importData.get("validationType"),
                importData.get("importScriptName"),
                importData.get("targetFolderName"), createMamDataFilePath);
    }

    /**
     * Reads the data to be imported from XML file
     * 
     * @param filePath
     *            String object contains file path.
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
     * This method creates the basic import script.
     * 
     * @param importScriptName
     *            String object contains import script name.
     * @param importCategory
     *            String object contains import category.
     */
    public void createImportScript(String importScriptName,
            String importCategory) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
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
     * Configures the import script.
     * 
     * @param importLabel
     *            String object contains import label.
     * @param importCategory
     *            String object contains import category.
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
     * This method configures the data source section of import script.
     *
     * @param dataSourceType
     *            String object contains data source type.
     * @param volumeFolderName
     *            String object contains name of volume folder.
     * @param importFolderName
     *            String object contains name of import folder.
     * @param fileName
     *            String object contains name of file
     * @param dataSourceSectionFields
     *            String object contains array of data source section fields.
     */
    public void configureDataSourceSectionTest(String dataSourceType,
            String volumeFolderName, String importFolderName,
            String dataSourceSectionFields) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            String dataSourceFields[] = spiltData(dataSourceSectionFields);
            verifyDataSourceSectionFields(dataSourceFields);
            configureDataSourceSection(dataSourceType, volumeFolderName,
                    importFolderName,
                    getFileNameFromPath(createMamDataFilePath));
        } catch (Exception e) {
            CSLogger.debug("Data source configuration failed" + e);
            Assert.fail("Data source configuration failed" + e);
        }
    }

    /**
     * This method configures the data target section of import script.
     *
     * @param dataTargetType
     *            String object contains data target type.
     * @param dataTargetId
     *            String object contains data target ID.
     * @param targetFolderName
     *            String object contains name of target folder.
     * @param defaultLanguage
     *            String object contains default language.
     * @param startRowNumber
     *            String object contains start row number.
     * @param limitRowNumber
     *            String object contains limit row number.
     * @param batchSize
     *            String object contains batch size.
     * @param NumOfmultiThread
     *            String object contains number of multi-thread.
     * @param idFilter
     *            String object contains ID filter.
     * @param dataTargetSectionFields
     *            String object contains array of data target section fields.
     */
    public void confiureDataTargetSection(String dataTargetType,
            String dataTargetId, String targetFolderName,
            String defaultLanguage, String startRowNumber,
            String limitRowNumber, String batchSize, String NumOfmultiThread,
            String idFilter, String dataTargetSectionFields) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            traverseToScriptConfiguration();
            String dataTargetFields[] = spiltData(dataTargetSectionFields);
            verifyDataTargetSectionFields(dataTargetFields);
            configureDataTargetSection(dataTargetType, dataTargetId,
                    targetFolderName, defaultLanguage, startRowNumber,
                    limitRowNumber, batchSize, NumOfmultiThread, idFilter);
        } catch (Exception e) {
            CSLogger.debug("Data target configuration failed" + e);
            Assert.fail("Data target configuration failed" + e);
        }
    }

    /**
     * Verifies the execution when data target mode is selected as 'Update Known
     * Objects Only'.
     */
    private void verfiyModeUpdateKnownObjectsOnly() {
        setScriptMode(importData.get("importScriptName"),
                "Update known objects only", importData.get("scriptTab"));
        selectDataSourceFile(getFileNameFromPath(updateMamDataFilePath), true,
                importData.get("volumeFolderName"),
                importData.get("importFolderName"));
        saveImportActiveScript();
        fileData = readDataFromXML(updateMamDataFilePath);
        verifyImportedFile(importData.get("scriptTab"),
                importData.get("dataTransformationField"),
                importData.get("transformedName"),
                importData.get("transformationType"),
                importData.get("importInto"),
                importData.get("transformationLanguage"),
                importData.get("executionType"), importData.get("importLayer"),
                importData.get("validationType"),
                importData.get("importScriptName"),
                importData.get("targetFolderName"), updateMamDataFilePath);
    }

    /**
     *
     * Creates the target folder.
     * 
     * @param volumeFolderName
     *            String object contains volume folder name.
     * @param targetFolderName
     *            String object contains target folder name.
     */
    private void createTargetMAMFolder(String volumeFolderName,
            String targetFolderName) {
        waitForReload = new WebDriverWait(browserDriver, 180);
        switchToMamAndExpandVolumeTree(waitForReload);
        createMamFolder(volumeFolderName, targetFolderName);
    }

    /**
     * * This method verifies the imported File and its external key and label.
     *
     * @param scriptTab
     *            String object contains name of tab.
     * @param dataTransformationField
     *            String object contains fields of data transformation separated
     *            by comma.
     * @param transformedName
     *            String object contains transformed name of an attribute. for
     *            e.g Defining Name as'Label'.
     * @param transformationType
     *            String object contains transformation type.
     * @param importInto
     *            String object contains values as Label or custom fields.
     * @param transformationLanguage
     *            String object contains transformation language.
     * @param executionType
     *            String object contains execution type.
     * @param importLayer
     *            String object contains import layer.
     * @param validationType
     *            String object contains validation type.
     * @param importScriptName
     *            String object contains import script name.
     * @param targetFolderName
     *            String object contains target folder name.
     * @param importDataFilePath
     *            String object contains import data file path.
     */
    private void verifyImportedFile(String scriptTab,
            String dataTransformationField, String transformedName,
            String transformationType, String importInto,
            String transformationLanguage, String executionType,
            String importLayer, String validationType, String importScriptName,
            String targetFolderName, String importDataFilePath) {
        try {
            executeImportScript(false);
            executeImportScript(true);
            verifyActiveScriptJob();
            printActiveScriptLogs();
            switchToMamAndExpandVolumeTree(waitForReload);
            refreshVolumeFolder(importData.get("volumeFolderName"));
            verifyImportedFile(importDataFilePath, targetFolderName);
        } catch (Exception e) {
            CSLogger.debug(
                    "Data not imported properly verification failed " + e);
            Assert.fail("Data not imported properly verification failed " + e);
        }
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
     * and then expands the channel tree.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void switchToMamAndExpandVolumeTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnMedia(waitForReload);
        clkOnVolumesNode();
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method traverse to the Mam tree in left most section of PIM module.
     * 
     */
    private void goToMamStudioTreeSection() {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
    }

    /**
     * Switches to Media tab and expands the given volume folder and uploads the
     * given file.
     * 
     * @param volumeFolderName
     *            String object contains name of volume folder.
     * @param importFolderName
     *            String object contains name of import folder.
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
     * @param volumefolderName
     *            String object contains name of volume folder under which the
     *            given volume folder will be created.
     * @param importFolderName
     *            String object contains name of volume folder
     */
    private void createImportMAMFolder(String volumeFolderName,
            String importFolderName) {
        switchToMamAndExpandImportFolder(volumeFolderName, importFolderName);
        createMamFolder(volumeFolderName, importFolderName);
        CSLogger.info("Folder : " + importFolderName + " created successfully");
    }

    /**
     * Creates the given MAM folder.
     * 
     * @param volumeFolderName
     *            String object contains volume folder name under which MAM
     *            folder will be created.
     * @param folderName
     *            String object contains MAM folder name.
     */
    private void createMamFolder(String volumeFolderName, String folderName) {
        mamStudioVolumesNodeInstance.expandNodesByVolumesName(waitForReload,
                volumeFolderName);
        CSUtility.rightClickTreeNode(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + volumeFolderName + "')]")),
                browserDriver);
        volumePopup.selectPopupDivMenu(waitForReload,
                volumePopup.getCreateNewFolder(), browserDriver);
        volumePopup.enterValueInDialogueMamStudio(waitForReload, folderName);
        volumePopup.askBoxWindowOperationMamStudio(waitForReload, true,
                browserDriver);
    }

    /**
     * This method uploads file to the dialog window.
     * 
     * @param filePath
     *            String contains the file path.
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
     * @param filePath
     *            String contains path of file.
     * @throws Exception
     *             throws exception
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
     * Switches the frame till edit frames.
     */
    private void traverseToScriptConfiguration() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
    }

    /**
     * Verifies the fields of data source section.
     * 
     * @param dataSourceFields
     *            String object contains data source section default fields.
     */
    private void verifyDataSourceSectionFields(String[] dataSourceFields) {
        try {
            clkOnGivenSection(activeJobsPageInstance.getSecDataSource(),
                    "Data Source");
        } catch (Exception e) {
            CSLogger.error("Data source section not found : test step failed");
            Assert.fail("Data source section not found : test step failed");
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//div[@id='content__sections::Data Source']")));
        List<WebElement> getTableData = browserDriver.findElements(By
                .xpath("//div[@id='content__sections::Data Source']/table/tbody"
                        + "/tr/td" + "[1]"));
        verifyFields(getTableData, dataSourceFields, "data source");
    }

    /**
     * Clicks on given section.
     * 
     * @param section
     *            WebElement of section field.
     * @param sectionName
     *            String object contains name of section.
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
     * Configures the data source section of import script.
     * 
     * @param dataSourceType
     *            String object contains data source type.
     * @param volumeFolderName
     *            String object contains name of volume folder.
     * @param importFolderName
     *            String object contains name of import folder.
     * @param fileName
     *            String object contains name of file
     */
    private void configureDataSourceSection(String dataSourceType,
            String volumeFolderName, String importFolderName, String fileName) {
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnImportDataSourceType(),
                dataSourceType);
        selectDataSourceFile(fileName, false, volumeFolderName,
                importFolderName);
        selectDataSourceFile(fileName, true, volumeFolderName,
                importFolderName);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtXmlObjectSelector(), "Article");
        saveImportActiveScript();
    }

    /**
     * Selects the data source file.
     * 
     * @param fileName
     *            String object contains name of file.
     * @param isPressOkay
     *            Boolean parameter contains true or false values.
     * @param volumeFolderName
     *            String object contains name of volume folder.
     * @param importFolderName
     *            String object contains name of import folder.
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
     * Verifies the fields of data target section.
     * 
     * @param dataTargetType
     *            String object contains data target type.
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
     * @param dataTargetType
     *            String object contains data target type.
     * @param dataTargetId
     *            String object contains data target ID.
     * @param targetFolderName
     *            String object contains name of target folder.
     * @param defaultLanguage
     *            String object contains default language.
     * @param startRowNumber
     *            String object contains start row number.
     * @param limitRowNumber
     *            String object contains limit row number.
     * @param batchSize
     *            String object contains batch size.
     * @param NumOfmultiThread
     *            String object contains number of multi-thread.
     * @param idFilter
     *            String object contains ID filter.
     * 
     */
    private void configureDataTargetSection(String dataTargetType,
            String dataTargetId, String targetFolderName,
            String defaultLanguage, String startRowNumber,
            String limitRowNumber, String batchSize, String NumOfmultiThread,
            String idFilter) {
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getdrpDwnImportDataTargetType(),
                dataTargetType);
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnActiveScriptMode(),
                "Create unknown objects only");
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnDataTargetId(), dataTargetId);
        selectTargetMamFolder(importData.get("volumeFolderName"),
                targetFolderName);
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
     * @param volumeFolderName
     *            String object contains target folder name.
     * @param importFolderName
     *            String object contains import folder name.
     */
    private void selectTargetMamFolder(String volumeFolderName,
            String importFolderName) {
        activeJobsPageInstance.getCtnImportTargetFolder().click();
        clkBtnControlPaneButtonFilesFolder();
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogFile(
                waitForReload, browserDriver);
        clkOnGivenVolumeFolder(volumeFolderName);
        clkOnGivenVolumeFolder(importFolderName);
        modalDialogPopupWindowInstance
                .handlePopupDataSelectionDialog(waitForReload, true);
        traverseToScriptConfiguration();
    }

    /**
     * Executes the configured import script.
     * 
     * @param isPressOkay
     *            Boolean parameter contains values whether to execute the
     *            script or not.
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
    private Boolean isScriptExecuted() {
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
        int jobStatus = browserDriver
                .findElements(By
                        .xpath("//tr[@id='HeaderBarTop']/following-sibling::tr"
                                + "[1]/td[2]/div/img[contains(@src,'circle_check.svg')]"))
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
        List<WebElement> activeJobLogs = browserDriver.findElements(
                By.xpath("//table[@id='ActiveScriptLogTable']/tbody/tr"));
        CSLogger.info("Time" + "\t" + "Total" + "\t" + "Diff" + "\t"
                + "Last 50 Messages");
        for (WebElement cell : activeJobLogs) {
            CSLogger.info(cell.getText());
        }
    }

    /**
     * Refresh the given volume folder.
     * 
     * @param volumeFolderName
     *            String object contains name of volume folder.
     */
    private void refreshVolumeFolder(String volumeFolderName) {
        CSUtility.tempMethodForThreadSleep(3000);
        goToMamStudioTreeSection();
        WebElement volume = browserDriver.findElement(By.xpath(
                "//span[contains(text(),'" + volumeFolderName + "')]/../.."));
        waitForReload.until(ExpectedConditions.elementToBeClickable(volume));
        volume.click();
        CSUtility.rightClickTreeNode(waitForReload, volume, browserDriver);
        channelPopup.selectPopupDivMenu(waitForReload,
                channelPopup.getcsGuiPopupMenuRefresh(), browserDriver);
        CSLogger.info("Refreshed volume folder : " + volumeFolderName);
    }

    /**
     * Clicks on given File tab.
     * 
     * @param tabName
     *            String object contains name of tab.
     */
    private void clkOnGivenTab(String tabName) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//nobr[contains(text(),'" + tabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By
                .xpath("//nobr[contains(text(),'" + tabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the " + tabName + "tab");
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
     * @param scriptName
     *            String object contains script name.
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
     * @param waitForReload
     *            WebDriverWait object.
     * @param data
     *            String object contains data to be entered in filter bar.
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
     * @param tabName
     *            String object contains tab name.
     * @param scriptTab
     *            String object contains script name.
     * @param transformationFields
     *            String object contains array of data transformation fields.
     * @param transformedName
     *            String object contains transformed name of an attribute. for
     *            e.g Defining Name as'Label'.
     * @param transformationType
     *            String object contains transformation type.
     * @param importInto
     *            String object contains values as Label or custom fields.
     * @param transformationLanguage
     *            String object contains transformation language.
     * @param executionType
     *            String object contains execution type.
     * @param importLayer
     *            String object contains import layer.
     * @param validationType
     *            String object contains validation type.
     * 
     */
    private void configureDataTransformationSection(String tabName,
            String scriptName, String dataTransformationField,
            String transformedName, String transformationType,
            String importInto, String transformationLanguage,
            String executionType, String importLayer, String validationType) {
        String[] transformationFields = spiltData(dataTransformationField);
        traverseToScriptConfiguration();
        String parentWindow = browserDriver.getWindowHandle();
        WebElement propertiesTab = browserDriver.findElement(
                By.xpath("//nobr[contains(text(),'" + tabName + "')]"));
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
        verifyTransformationWindow(scriptName);
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
     * @param dataSourceLabelField
     *            String object contains data source field name.
     * @param transformationFields
     *            String object contains array of data transformation fields.
     * @param transformedName
     *            String object contains transformed name of an attribute. for
     *            e.g Defining Name as'Label'.
     * @param transformationType
     *            String object contains transformation type.
     * @param importInto
     *            String object contains values as Label or custom fields.
     * @param transformationLanguage
     *            String object contains transformation language.
     * @param executionType
     *            String object contains execution type.
     * @param importLayer
     *            String object contains import layer.
     * @param validationType
     *            String object contains validation type.
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
     * @param dataSourceFieldName
     *            String object contains data source field name.
     * @param transformedName
     *            String object contains transformed name of an attribute. for
     *            e.g Defining Name as'Label'.
     * @param transformationType
     *            String object contains transformation type.
     * @param importInto
     *            String object contains values as Label or custom fields.
     * @param transformationLanguage
     *            String object contains transformation language.
     * @param executionType
     *            String object contains execution type.
     * @param importLayer
     *            String object contains import layer.
     * @param validationType
     *            String object contains validation type.
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
        WebElement drpDwnTargetKeyFieldType = browserDriver.findElement(By
                .xpath("//select[contains(@id,'DataflowimporttransformationInternalTargetKeyFieldType')]/option[contains(text(),'"
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
     * @param dataSourceFieldName
     *            String object contains field name.
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
     * @param element
     *            WebElement of text box.
     * @param text
     *            -String object contains value to be entered into text box
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
     * @param drpDwnElement
     *            Drop down WebElement.
     * @param option
     *            String object contains option to be selected.
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
     * @param fieldName
     *            String object contains name of field.
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
     * Verifies whether proper File data is imported.
     * 
     * @param filePath
     *            String object contains file path.
     * @param targetMamFolder
     *            String object contains name of parent File.
     */
    private void verifyImportedFile(String filePath, String targetMamFolder) {
        String importedFileName = fileData.get(fileheader.get(1));
        goToMamStudioTreeSection();
        clkOnGivenVolumeFolder(targetMamFolder);
        WebElement importedFile = browserDriver.findElement(
                By.xpath("(//span[contains(text(),'" + targetMamFolder
                        + "')]/../../../../../../../../../../../following-sibling::span/span/table/tbody/tr/td/span/table/tbody/tr/td[2]/a/span/span)[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, importedFile);
        importedFile.click();
        CSLogger.info("Clicked on imported file");
        goToMamStudioRightSection();
        clkOnGivenTab(importData.get("tabName"));
        CSUtility.tempMethodForThreadSleep(1000);
        String label = browserDriver
                .findElement(
                        By.xpath("//tr[@id='AttributeRow_Label']/td[2]/span"))
                .getText();
        String externalKey = browserDriver
                .findElement(By
                        .xpath("//tr[@id='AttributeRow_ExternalKey']/td[2]/span"))
                .getText();
        if (label.equals(importedFileName)
                && externalKey.equals(fileData.get(fileheader.get(0)))) {
            CSLogger.info(
                    "Verified passed : found imported label and external key in "
                            + "imported file");
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
     * @param scriptName
     *            String object contains name of script.
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
     * @param transformationFields
     *            String object contains array of transformation fields.
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
     * @param importScriptName
     *            String object contains name of script.
     * @param scriptMode
     *            String object contains script mode.
     * @param scriptTab
     *            String object contains script tab name.
     */
    private void setScriptMode(String importScriptName, String scriptMode,
            String scriptTab) {
        goToImportScript(importScriptName, scriptTab);
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnActiveScriptMode(), scriptMode);
        saveImportActiveScript();
    }

    /**
     * Perform actions to click on created import script.
     * 
     * @param importScriptName
     *            String object contains name of import script.
     * @param scriptTab
     *            String object contains script tab name.
     */
    private void goToImportScript(String importScriptName, String scriptTab) {
        switchToSettingsPage();
        goToSystemPreferencesIcon();
        expandActiveJobsTree();
        clkOnCreatedImportScript(importScriptName);
        traverseToScriptConfiguration();
        clkOnGivenTab(scriptTab);
    }

    /**
     * This method verifies the execution and import data that created File when
     * data target mode is selected as 'Update Known And Create Unknown
     * Objects'.
     */
    private void verifyModeUpdateKnownCreateUnknownObjects() {
        fileData = readDataFromXML(createUpdateMamDataFilePath);
        setScriptMode(importData.get("importScriptName"),
                "Update known and create unknown objects",
                importData.get("scriptTab"));
        selectDataSourceFile(getFileNameFromPath(createUpdateMamDataFilePath),
                true, importData.get("volumeFolderName"),
                importData.get("importFolderName"));
        saveImportActiveScript();
        verifyImportedFiles(importData.get("targetFolderName"));
    }

    /**
     * Verifies the imported data when data target option is selected as 'Update
     * Known And Create Unknown Objects'.
     * 
     * @param targetMamFolder
     *            String object contains veiw name.
     */
    private void verifyImportedFiles(String targetMamFolder) {
        String updatedKnownFileExternalKey = resultData.get(0);
        String updatedKnownFileLabel = resultData.get(1);
        String unKnownCreatedFileExternalKey = resultData.get(2);
        String unKnownCreatedFileLabel = resultData.get(3);
        executeImportScript(false);
        executeImportScript(true);
        verifyActiveScriptJob();
        printActiveScriptLogs();
        switchToMamAndExpandVolumeTree(waitForReload);
        refreshVolumeFolder(importData.get("volumeFolderName"));
        verifyKnownUpdatedFile(targetMamFolder, updatedKnownFileExternalKey,
                updatedKnownFileLabel);
        verifyUnknownFileCreation(targetMamFolder,
                unKnownCreatedFileExternalKey, unKnownCreatedFileLabel);
    }

    /**
     * Verifies whether unknown File is created when data target mode is
     * selected as 'Update Known And Create Unknown Objects'
     * 
     * @param targetMamFolderName
     *            String object contains File name under which new File's while
     *            be imported.
     * @param unKnownCreatedFileExternalKey
     *            String object contains external key.
     * @param unKnownCreatedFileLabel
     *            String object contains File name.
     */
    private void verifyUnknownFileCreation(String targetMamFolderName,
            String unKnownCreatedFileExternalKey,
            String unKnownCreatedFileLabel) {
        goToMamStudioTreeSection();
        clkOnGivenVolumeFolder(targetMamFolderName);
        WebElement unKnownCreatedFile = browserDriver
                .findElement(By.xpath("(//span[contains(text(),'"
                        + targetMamFolderName + "')]/../../.."
                        + "/../../../../../../../../following-sibling::span/span"
                        + "/table/tbody/tr/td/span/table/tbody/tr/td[2]/a/span/"
                        + "span)[2]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, unKnownCreatedFile);
        unKnownCreatedFile.click();
        goToMamStudioRightSection();
        clkOnGivenTab(importData.get("tabName"));
        CSUtility.tempMethodForThreadSleep(3000);
        String createdlabel = browserDriver
                .findElement(
                        By.xpath("//tr[@id='AttributeRow_Label']/td[2]/span"))
                .getText();
        String createdExternalKey = browserDriver
                .findElement(By
                        .xpath("//tr[@id='AttributeRow_ExternalKey']/td[2]/span"))
                .getText();
        if (createdlabel.equals(unKnownCreatedFileLabel)
                && createdExternalKey.equals(unKnownCreatedFileExternalKey)) {
            CSLogger.info("Unknown File created successfully");
        } else {
            CSLogger.error("Unknown File creation failed");
            softAssertion.fail("Unknown File creation failed");
        }
    }

    /**
     * Traverses right section MAM studio frames.
     */
    private void goToMamStudioRightSection() {
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
    }

    /**
     * Verifies whether known File's data is updated when data target mode is
     * selected as 'Update Known And Create Unknown Objects'
     * 
     * @param targetMamFolderName
     *            String object contains File name under which new File's while
     *            be imported.
     * @param unKnownCreatedFileExternalKey
     *            String object contains external key.
     * @param unKnownCreatedFileLabel
     *            String object contains File name.
     */
    private void verifyKnownUpdatedFile(String targetMamFolderName,
            String updatedKnownFileExternalKey, String updatedKnownFileLabel) {
        goToMamStudioTreeSection();
        clkOnGivenVolumeFolder(targetMamFolderName);
        WebElement updatedKnownFile = browserDriver
                .findElement(By.xpath("//span[contains(text(),'"
                        + targetMamFolderName + "')]/../../.."
                        + "/../../../../../../../../following-sibling::span/span"
                        + "/table/tbody/tr/td/span/table/tbody/tr/td[2]/a/span/"
                        + "span"));
        CSUtility.waitForVisibilityOfElement(waitForReload, updatedKnownFile);
        updatedKnownFile.click();
        goToMamStudioRightSection();
        clkOnGivenTab(importData.get("tabName"));
        CSUtility.tempMethodForThreadSleep(1000);
        String updatedlabel = browserDriver
                .findElement(
                        By.xpath("//tr[@id='AttributeRow_Label']/td[2]/span"))
                .getText();
        String updatedExternalKey = browserDriver
                .findElement(By
                        .xpath("//tr[@id='AttributeRow_ExternalKey']/td[2]/span"))
                .getText();
        if (updatedlabel.equals(updatedKnownFileLabel)
                && updatedExternalKey.equals(updatedKnownFileExternalKey)) {
            CSLogger.info("known File updated successfully");
        } else {
            CSLogger.error("Verification failed : Known File didn't update");
            softAssertion
                    .fail("Verification failed : Known File didn't update");
        }
    }

    /**
     * Returns the file name from given file path.
     * 
     * @param filePath
     *            String object contains file path
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
        channelPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiListFooterInstance = new CSGuiListFooter(browserDriver);
        mamStudioVolumesNodeInstance = MamStudioTree
                .getMamStudioVolumesNodeInstance(browserDriver);
        volumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
        createMamDataFilePath = config.getDataflowTestDataFolder()
                + "CreateFileImportData.xml";
        updateMamDataFilePath = config.getDataflowTestDataFolder()
                + "UpdateFileImportData.xml";
        createUpdateMamDataFilePath = config.getDataflowTestDataFolder()
                + "CreateAndFileUpdateData.xml";
    }
}
