/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.dataflow;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioTree;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.settings.ActiveJobsPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
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
 * This class contain the test method to verify data flow import script
 * automation's tab fields.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyAutomationInImportTest extends AbstractTest {

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
    private MamStudioVolumesNode      mamStudioVolumesNodeInstance;
    private IVolumePopup              volumePopup;
    private ModalDialogPopupWindow    modalDialogPopupWindowInstance;
    private HashMap<String, String>   fileData;
    private ArrayList<String>         fileheader;
    private String                    scriptConfigTestSheet = "VerifyAutomationInImport";
    private HashMap<String, String>   importData;
    private ArrayList<String>         resultData;
    private String                    createDataFilePath;
    private UserManagementPage        userManagementPageInstance;
    private IUserManagementPopup      userManagementPopup;
    private String                    userParentWindow;
    private LoginPage                 loginPageInstance;

    /**
     * This test method verifies the automation tab field of data flow import
     * script.
     * 
     * @param testData Array of String containing the test Data from data
     *            driven.
     */
    @Test(dataProvider = "importScriptConfigTestData")
    public void testVerifyAutomationInImport(String... testData) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            readTestDataFromSheet(testData);
            checkConfiguredFileExists(createDataFilePath);
            fileData = readDataFromXML(createDataFilePath);
            configureImportScript(importData.get("targetFolderName"),
                    importData.get("volumeFolderName"),
                    importData.get("importFolderName"),
                    importData.get("importScriptName"));
            verifyAutomationTabFields(importData.get("automationScriptTab"),
                    importData.get("automationTabFields"));
            verifyScheduling();
            verifyLastRunField();
            verifyPostProcessingField();
            verifyRunAsUserField();
            verifyAllowAccessField(importData.get("userGroupName"));
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method: "
                    + "testVerifyAutomationInImport", e);
            Assert.fail("Automation error in test method: "
                    + "testVerifyAutomationInImport", e);
        }
    }

    /**
     * Creates and executes the import script.
     * 
     * @param targetFolderName String object contains target folder name.
     * @param volumeFolderName String object contains volume folder name.
     * @param importFolderName String object contains import folder name.
     * @param importScriptName String object contains import script name.
     */
    private void configureImportScript(String targetFolderName,
            String volumeFolderName, String importFolderName,
            String importScriptName) {
        createTargetProductFolder(targetFolderName);
        createImportMAMFolder(targetFolderName, volumeFolderName,
                importFolderName);
        uploadXMLFile(importFolderName, createDataFilePath);
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
                "automationTabFields", "userGroupName", "username", "password",
                "userRole", "userAccessRight", "loginLanguage" };
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
            clkOnSaveButton();
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
     * Creates the import active script.
     */
    private void createImportActiveScript() {
        switchToSettingsPage();
        goToSystemPreferencesIcon();
        expandActiveJobsTree();
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csGuiToolbarHorizontalInstance.clkOnWebElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew(),
                "create new");
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
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    iframeLocatorsInstance.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
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
        } catch (Exception e) {
            CSLogger.error("Error while uploading file", e);
            softAssertion.fail("Error while uploading file", e);
        }
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
        traverseToSettingsRightFrames();
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
    }

    /**
     * Clicks on save button to save the configured import active script.
     */
    private void clkOnSaveButton() {
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
        clkOnSaveButton();
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
        verifyFields(getTableData, dataSourceFields, "data source section");
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
        verifyFields(getTableData, dataTargetFields, "data target section");
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
        clkOnSaveButton();
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
        traverseToSettingsLeftTree();
        CSUtility.scrollUpOrDownToElement(
                activeJobsPageInstance.getNodeActiveJobs(), browserDriver);
        performAction.doubleClick(activeJobsPageInstance.getNodeActiveJobs())
                .perform();
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * Traverse to setting's tree.
     */
    private void traverseToSettingsLeftTree() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
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
        clkOnSaveButton();
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
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarRefresh(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
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
        clkOnSaveButton();
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
        verifyFields(getTableData, transformationFields,
                "data transformation section");
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
     * @param label String specifies the area where fields must be visible.
     */
    private void verifyFields(List<WebElement> tableData, String[] fields,
            String label) {
        ArrayList<String> cellData = new ArrayList<>();
        for (WebElement cell : tableData) {
            cellData.add(cell.getText());
        }
        for (int index = 0; index < fields.length; index++) {
            if (!cellData.contains(fields[index])) {
                CSLogger.error("Field : " + fields[index] + " not found under "
                        + label);
                Assert.fail("Field : " + fields[index] + " not found under "
                        + label);
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
        CSUtility.tempMethodForThreadSleep(2000);
        clkOnGivenTab(propertiesScriptTab);
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
        clkOnSaveButton();
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
     * Verifies the automation tab fields.
     * 
     * @param automationTabName String object contains tab name.
     * @param automationTabFields String object contains automation tab fields
     *            separated with comma.
     */
    private void verifyAutomationTabFields(String automationTabName,
            String automationTabFields) {
        traverseToScriptConfiguration();
        clkOnGivenTab(automationTabName);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//div[@cs_name='"
                        + automationTabName + "']/div/table")));
        List<WebElement> getTableData = browserDriver.findElements(By
                .xpath("//div[@cs_name='Automation']/div/table/tbody/tr/td/table"
                        + "/tbody" + "/" + "tr/td[1]"));
        verifyFields(getTableData, spiltData(automationTabFields),
                "Automation tab");
    }

    /**
     * Verifies the allow access field with nobody,anybody and with following
     * user's and group.
     * 
     * @param userGroupName String object contains user group name.
     */
    private void verifyAllowAccessField(String userGroupName) {
        verifyNobodyAccessOption();
        verifyAnybodyAccessOption();
        verifyUserGroupAccessOption(importData.get("importScriptName"),
                importData.get("propertiesScriptTab"),
                importData.get("automationScriptTab"),
                "The following Users or Groups");
    }

    /**
     * Verifies the allow access field with anybody option.
     */
    private void verifyAnybodyAccessOption() {
        verifyAllowAccessOption(importData.get("importScriptName"),
                importData.get("propertiesScriptTab"),
                importData.get("automationScriptTab"), "Anybody", "Anybody");
    }

    /**
     * Verifies the allow access field option with nobody option.
     */
    private void verifyNobodyAccessOption() {
        verifyAllowAccessOption(importData.get("importScriptName"),
                importData.get("propertiesScriptTab"),
                importData.get("automationScriptTab"), "Nobody", "Nobody");
    }

    /**
     * Creates the given user group.
     * 
     * @param userGroupName String object contains user group name.
     */
    private void createUserGroup(String userGroupName) {
        expandUsersNode();
        CSUtility.rightClickTreeNode(waitForReload,
                userManagementPageInstance.getUsersNode(), browserDriver);
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupNewGroup(), browserDriver);
        userManagementPopup.enterValueInDialogue(waitForReload, userGroupName);
        userManagementPopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * Expands the users node.
     */
    private void expandUsersNode() {
        traverseToSettingsLeftTree();
        userManagementPageInstance.clkUserManagement(waitForReload);
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPageInstance.getUsersNode()));
        userManagementPageInstance.getUsersNode().click();
        CSLogger.info("Clicked on user");
    }

    /**
     * Sets the user role and access rights.
     */
    private void setUserRoleAndAccessRights() {
        CSUtility.rightClickTreeNode(waitForReload, getCreatedUserGroup(),
                browserDriver);
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupEdit(), browserDriver);
        traverseToSettingsRightFrames();
        userManagementPageInstance.clkRightsTab(waitForReload);
        userParentWindow = browserDriver.getWindowHandle();
        clkOnLabelAdd();
        CSUtility.tempMethodForThreadSleep(1000);
        String childWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(childWindow);
        WebElement setUserRole = browserDriver.findElement(
                By.xpath("//select[@name='RoleID']/option[contains(text(),'"
                        + importData.get("userRole") + "')]"));
        setUserRole.click();
        CSLogger.info("User role set as : " + importData.get("userRole"));
        WebElement setAccessRights = browserDriver.findElement(
                By.xpath("//select[@name='RubricID']/option[contains(text(),'"
                        + importData.get("userAccessRight") + "')]"));
        setAccessRights.click();
        CSLogger.info("User access right set as : "
                + importData.get("userAccessRight"));
        userManagementPopup.getBtnOk().click();
        CSLogger.info("Clicked on ok button of Edit Autorization pop up");
        browserDriver.switchTo().window(userParentWindow);
        traverseToSettingsRightFrames();
        clkOnSaveButton();
        traverseToSettingsLeftTree();
        clkOnCreatedUserGroup();
        CSLogger.info(
                "Clicked on user group : " + importData.get("userGroupName"));
    }

    /**
     * Traverses to settings editor window right frames.
     */
    private void traverseToSettingsRightFrames() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * Returns the WebElement of user group.
     * 
     * @return WebElement of user group.
     */
    private WebElement getCreatedUserGroup() {
        traverseToSettingsLeftTree();
        WebElement createdUserGroup = browserDriver
                .findElement(By.linkText(importData.get("userGroupName")));
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload, createdUserGroup);
        return createdUserGroup;
    }

    /**
     * This method creates a user.
     * 
     * @param username String object contains user name.
     * @param password String object contains password.
     */
    private void createUser(String username, String password) {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csGuiToolbarHorizontalInstance.clkOnWebElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew(),
                "create new");
        traverseToSettingsRightFrames();
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        userManagementPageInstance.clkAdministrationTab(waitForReload);
        enterDataInField(userManagementPageInstance.getTxtUsername(), username,
                "username");
        enterDataInField(userManagementPageInstance.getTxtPassword(), password,
                "password");
        clkOnSaveButton();
        confirmPassword(password);
    }

    /**
     * This method traverses up to label Add to add Role and Access Right
     */
    private void clkOnLabelAdd() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrameSettings()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPageInstance.getFormLabelAdd()));
        userManagementPageInstance.getFormLabelAdd().click();
        CSLogger.info("Clicked on Add under rights tab");
    }

    /**
     * Enters the data into text box fields.
     * 
     * @param element WebElement of textbox.
     * @param data String object contains data.
     * @param label String object contains label.
     */
    private void enterDataInField(WebElement element, String data,
            String label) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(data);
        CSLogger.info("Entered " + label + " in text box as : " + data);
    }

    /**
     * This method handles the pop up of confirm password field
     * 
     * @param password String object contains user password.
     */
    private void confirmPassword(String password) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPopup.getUserInput()));
        userManagementPopup.getUserInput().sendKeys(password);
        userManagementPopup.clkOkOfPopup(waitForReload);
        CSLogger.info("Confirmed user password");
    }

    /**
     * Verifies the allow access option with 'The following users and group'.
     * 
     * @param importScriptName String object contains import script name.
     * @param propertiesScriptTab String object contains properties tab field
     *            name.
     * @param automationTabName String object contains automation tab field.
     * @param userAccess String object contains user access option.
     * @param userAccessLabel String object contains label.
     */
    private void verifyAllowAccessOption(String importScriptName,
            String propertiesScriptTab, String automationTabName,
            String userAccess, String userAccessLabel) {
        configureAllowAccessField(importScriptName, propertiesScriptTab,
                automationTabName, userAccess);
        verifyVisibiltyOfScript(importScriptName, userAccess, userAccessLabel);
    }

    /**
     * This method verifies the execution of user access field.
     * 
     * @param importScriptName String object contains import script name.
     * @param userAccess String object contains user access value.
     * @param userAccessLabel String object contains label.
     */
    private void verifyVisibiltyOfScript(String importScriptName,
            String userAccess, String userAccessLabel) {
        switchToProductsPage();
        goToPimStudioTreeSection();
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        CSUtility.rightClickTreeNode(waitForReload, getTargetProduct(),
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getcsGuiPopupMenuRefresh(), browserDriver);
        goToPimStudioTreeSection();
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.rightClickTreeNode(waitForReload, getTargetProduct(),
                browserDriver);
        productPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupProcess());
        if ((userAccess.equals("Anybody"))) {
            int scriptExists = checkScriptExists(importScriptName);
            if (scriptExists == 0) {
                CSLogger.error(
                        "Verification failed : script is visible when user access set to : "
                                + userAccessLabel);
                softAssertion
                        .fail("Verification failed : script is visible when user access set to : "
                                + userAccessLabel);
            } else {
                CSLogger.info("Verification passed when user access set to  : "
                        + userAccessLabel);
            }
        } else if (userAccess.equals("Nobody")) {
            traverseToCsPopupDivSubFrame();
            int scriptExists = browserDriver
                    .findElements(By.xpath("//td[contains(text(),'Execute')]"))
                    .size();
            if (scriptExists == 0) {
                CSLogger.info("Verification passed when user access set to  : "
                        + userAccessLabel);
            } else {
                int configuredScriptExists = checkScriptExists(
                        importScriptName);
                if (configuredScriptExists == 0) {
                    CSLogger.info(
                            "Verification passed when user access set to  : "
                                    + userAccessLabel);
                } else {
                    CSLogger.error(
                            "Verification failed : script is visible when user "
                                    + "access set to : " + userAccess);
                    softAssertion
                            .fail("Verification failed : script is visible when user"
                                    + " access set to : " + userAccessLabel);
                }
            }
        }
    }

    /**
     * Checks whether the configured script appears in PIM studio.
     * 
     * @param importScriptName String object contains import script name.
     * @return integer number specifying existence of script.
     */
    private int checkScriptExists(String importScriptName) {
        productPopup.hoverOnCsGuiPopupSubMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupExecute());
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCSPopupDivSubSubFrame()));
        int scriptExists = browserDriver
                .findElements(By.xpath(
                        "//td[contains(text(),'" + importScriptName + "')]"))
                .size();
        return scriptExists;
    }

    /**
     * When user access field is set to 'The following user's and group',this
     * method adds the user which is allowed to access the script.
     * 
     * @param username String object contains user name.
     */
    private void addAllowedUser(String username) {
        WebElement addUser = browserDriver.findElement(
                By.xpath("//div[contains(@id,'Activescript_UserAccessIDs_"
                        + "csReferenceDiv')]/div[2]//img"));
        CSUtility.waitForVisibilityOfElement(waitForReload, addUser);
        addUser.click();
        CSLogger.info("Clicked on + to add user");
        addUserFromDataSelectionDialogWindow();
    }

    /**
     * Adds the user from data selection dialog window.
     */
    private void addUserFromDataSelectionDialogWindow() {
        selectionDialogWindowInstance.clkBtnControlPaneButtonUserFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillUserFoldersleftFrames(
                waitForReload, browserDriver);
        WebElement userTree = browserDriver.findElement(By.id("User@0"));
        waitForReload.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("User@0")));
        userTree.click();
        CSLogger.info("Clicked on user tree");
        WebElement createdUserGroup = browserDriver
                .findElement(By.xpath("//span[contains(text(),'"
                        + importData.get("userGroupName") + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdUserGroup);
        createdUserGroup.click();
        CSLogger.info(
                "Clicked on user group: " + importData.get("userGroupName"));
        TraverseSelectionDialogFrames.traverseTillUserFoldersCenterPane(
                waitForReload, browserDriver);
        WebElement allowedUser = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
        waitForReload.until(ExpectedConditions.visibilityOf(allowedUser));
        allowedUser.click();
        CSLogger.info("Clicked on allowed user" + importData.get("username"));
        modalDialogPopupWindowInstance.askBoxWindowOperation(waitForReload,
                true, browserDriver);
        traverseToScriptConfiguration();
        CSUtility.tempMethodForThreadSleep(2000);
        clkOnSaveButton();
    }

    /**
     * Clicks on created user group.
     */
    private void clkOnCreatedUserGroup() {
        getCreatedUserGroup().click();
        CSLogger.info("clicked on configured user group : "
                + importData.get("userGroupName"));
    }

    /**
     * Traverses till FrmCsPopupDivSubFrame frame.
     */
    private void traverseToCsPopupDivSubFrame() {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
    }

    /**
     * This method performs log out operation.
     */
    private void performLogoutOperation() {
        csPortalHeaderInstance.clkBtnCsPortalLinkOptions(waitForReload);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopup.getCsGuiPopupLogout()));
        productPopup.getCsGuiPopupLogout().click();
        CSLogger.info("Logged out successfully ");
    }

    /**
     * This method logs in to application with newly created user
     * 
     * @param username String object contains user name.
     * @param password String object contains user password.
     * @param language String object contains user language.
     */
    private void loginAsAllowedUser(String username, String password,
            String language) {
        enterLoginDetails(username, password, language);
        WebElement acceptButton = browserDriver
                .findElement(By.id("CSPortalLoginTermsOfUseButtonAccept"));
        CSUtility.waitForVisibilityOfElement(waitForReload, acceptButton);
        acceptButton.click();
        enterLoginDetails(username, password, language);
    }

    /**
     * Enters the login details.
     * 
     * @param username String object contains user name.
     * @param password String object contains user password.
     * @param language String object contains user language.
     */
    private void enterLoginDetails(String username, String password,
            String language) {
        setUserLanguage(language);
        loginPageInstance.enterUsername(username);
        loginPageInstance.enterPassword(password);
        CSUtility.tempMethodForThreadSleep(1000);
        loginPageInstance.clkLoginButton();
    }

    /**
     * This method sets the user language.
     * 
     * @param language contains language to be set
     */
    private void setUserLanguage(String language) {
        Select loginLanguage = new Select(browserDriver
                .findElement(By.xpath("//select[@id='loginLanguage']")));
        loginLanguage.selectByVisibleText(language);
        CSLogger.info("User language set as : " + language);
    }

    /**
     * Verifies the execution when allow access field is set to 'The following
     * user's and group'
     * 
     * @param importScriptName String object contains import script name.
     * @param propertiesScriptTab String object contains properties tab name.
     * @param automationTabName String object contains automation tab name.
     * @param userAccess String object contain user access value.
     */
    private void verifyUserGroupAccessOption(String importScriptName,
            String propertiesScriptTab, String automationTabName,
            String userAccess) {
        CSUtility.tempMethodForThreadSleep(8000);
        configureAllowAccessField(importScriptName, propertiesScriptTab,
                automationTabName, userAccess);
        addAllowedUser(importData.get("username"));
        checkCurrentUserScriptInAccessibility(importScriptName,
                propertiesScriptTab, automationTabName, userAccess);
        checkAllowedUserScriptAccessibility(importScriptName,
                propertiesScriptTab, automationTabName, userAccess);
    }

    /**
     * Checks whether script is accessible to particular user.
     * 
     * @param importScriptName String object contains import script name.
     * @param propertiesScriptTab String object contains properties tab name.
     * @param automationTabName String object contains automation tab name.
     * @param userAccess String object contain user access value.
     */
    private void checkAllowedUserScriptAccessibility(String importScriptName,
            String propertiesScriptTab, String automationTabName,
            String userAccess) {
        performLogoutOperation();
        loginAsAllowedUser(importData.get("username"),
                importData.get("password"), importData.get("loginLanguage"));
        verifyVisibiltyOfScript(importScriptName, "Anybody", userAccess);
    }

    /**
     * Configures the allow access field.
     * 
     * @param importScriptName String object contains import script name.
     * @param propertiesScriptTab String object contains properties tab name.
     * @param automationTabName String object contains automation tab name.
     * @param userAccess String object contain user access value.
     */
    private void configureAllowAccessField(String importScriptName,
            String propertiesScriptTab, String automationTabName,
            String userAccess) {
        goToCreatedImportScript(importScriptName, propertiesScriptTab);
        activeJobsPageInstance.checkContextSensitiveCheckBox();
        clkOnSaveButton();
        setAllowAccessByField(automationTabName, userAccess);
    }

    /**
     * This method verifies that the import script is not accessible to
     * particular user.
     * 
     * @param importScriptName String object contains import script name.
     * @param propertiesScriptTab String object contains properties tab name.
     * @param automationTabName String object contains automation tab name.
     * @param userAccess String object contain user access value.
     */
    private void checkCurrentUserScriptInAccessibility(String importScriptName,
            String propertiesScriptTab, String automationTabName,
            String userAccess) {
        verifyVisibiltyOfScript(importScriptName, "Nobody", userAccess);
    }

    /**
     * Verifies the last run field.
     */
    private void verifyLastRunField() {
        goToCreatedImportScript(importData.get("importScriptName"),
                importData.get("propertiesScriptTab"));
        traverseToScriptConfiguration();
        clickOnJobsTab();
        executeImportScript(true);
        clkOnRefreshToolbarButton();
        int headerCount = returnHeaderCount("End");
        int actualCount = headerCount + 2;
        String endTimeOfScript = browserDriver.findElement(By
                .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td["
                        + actualCount + "]"))
                .getText();
        traverseToScriptConfiguration();
        clkOnRefreshToolbarButton();
        clkOnGivenTab(importData.get("automationScriptTab"));
        WebElement lastRunFieldElement = browserDriver.findElement(
                By.xpath("//span[contains(@id,'ActivescriptLastRun')]"));
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                lastRunFieldElement);
        String lastRunTime = lastRunFieldElement.getText();
        softAssertion.assertEquals(lastRunTime, endTimeOfScript,
                "Last run field time is not correct : verification failed");
    }

    /**
     * Returns the given header number.
     * 
     * @param header String object contains header name.
     * @return Integer value contains header number.
     */
    private int returnHeaderCount(String header) {
        traverseToScriptConfiguration();
        traverseToJobsTabFrame();
        List<WebElement> getStartDateHeaderNumber = browserDriver.findElements(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr/th"));
        int headerCount = 0;
        for (Iterator<WebElement> iterator2 = getStartDateHeaderNumber
                .iterator(); iterator2.hasNext();) {
            WebElement webElement = (WebElement) iterator2.next();
            if (!(webElement.getText().equals(""))) {
                if (webElement.getText().equals(header)) {
                    break;
                } else {
                    headerCount++;
                }
            }
        }
        return headerCount;
    }

    /**
     * Verifies the Run As User field.
     */
    private void verifyRunAsUserField() {
        configureUser();
        goToCreatedImportScript(importData.get("importScriptName"),
                importData.get("propertiesScriptTab"));
        traverseToScriptConfiguration();
        clkOnGivenTab(importData.get("automationScriptTab"));
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement runAsUserElement = browserDriver
                .findElement(By.xpath("//div[contains(@id,'Activescript_"
                        + "UserAvatarID_csReferenceDiv" + "')]/div[2]/img"));
        CSUtility.waitForElementToBeClickable(waitForReload, runAsUserElement);
        runAsUserElement.click();
        addUserFromDataSelectionDialogWindow();
        CSUtility.tempMethodForThreadSleep(1000);
        executeImportScript(true);
        clickOnJobsTab();
        int headerCount = returnHeaderCount("Running under");
        int actualCount = headerCount + 2;
        String runningUser = browserDriver.findElement(By
                .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td["
                        + actualCount + "]"))
                .getText();
        softAssertion.assertEquals(runningUser, importData.get("username"),
                "Selected user didn't execute the script : verification failed");
    }

    /**
     * Configures a user along with user group and access rights.
     */
    private void configureUser() {
        createUserGroup(importData.get("userGroupName"));
        setUserRoleAndAccessRights();
        createUser(importData.get("username"), importData.get("password"));
    }

    /**
     * Verifies the scheduling field.
     * 
     * @throws InterruptedException
     */
    private void verifyScheduling() throws InterruptedException {
        try {
            traverseToScriptConfiguration();
            clkOnGivenTab(importData.get("automationScriptTab"));
            WebElement scheduleFieldElement = browserDriver.findElement(By
                    .xpath("//select[contains(@id,'ActivescriptAutomationType')]"));
            activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                    scheduleFieldElement, "Every Minute");
            clkOnSaveButton();
            // Delay of one minute for script execution as job is scheduled to
            // run
            // after one minute.
            clickOnJobsTab();
            traverseToJobsTabFrame();
            TimeUnit.MINUTES.sleep(1);
            clkOnRefreshToolbarButton();
            WebElement getJobCount = browserDriver.findElement(By.xpath(
                    "//td[@class='CSGuiListFooter']/table/tbody/tr/td[2]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, getJobCount);
            if (getJobCount.getText().contains("0")) {
                CSLogger.error("Scheduling in script failed ");
                softAssertion.fail("Scheduling in script failed ");
            }
            traverseToScriptConfiguration();
            clkOnGivenTab(importData.get("automationScriptTab"));
            CSUtility.tempMethodForThreadSleep(2000);
            WebElement manualOption = browserDriver.findElement(By
                    .xpath("//select[contains(@id,'ActivescriptAutomationType')]/option"
                            + "[contains(text(),'Manual')]"));
            manualOption.click();
            CSUtility.tempMethodForThreadSleep(1000);
            clkOnSaveButton();
        } catch (Exception e) {
            CSLogger.error("Error while verifying scheduling field", e);
            softAssertion.fail("Error while verifying scheduling field", e);
        }
    }

    /**
     * Traverse to job tab frame.
     */
    private void traverseToJobsTabFrame() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmJobsState()));
    }

    /**
     * Verifies the post processing field with 'Send Mail' field.
     */
    private void verifyPostProcessingField() {
        traverseToScriptConfiguration();
        clkOnGivenTab(importData.get("automationScriptTab"));
        WebElement emailFieldElement = browserDriver.findElement(
                By.xpath("//select/option[@value='SendMailScriptFinalizer']"));
        CSUtility.scrollUpOrDownToElement(emailFieldElement, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload, emailFieldElement);
        performAction.doubleClick(emailFieldElement).perform();
        CSLogger.info("Send Mail selected from post processing field");
        CSUtility.scrollUpOrDownToElement(
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarSave(),
                browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        clkOnSaveButton();
        clkOnRefreshToolbarButton();
        executeImportScript(true);
        CSUtility.tempMethodForThreadSleep(4000);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmShowingStatusOfRunningActiveScript()));
        WebElement logElement = browserDriver
                .findElement(By.xpath("(//td[@class='Message'])[2]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, logElement);
        String logMailStatus = logElement.getText();
        CSLogger.info("Mail log status  " + logMailStatus);
        if (!(logMailStatus.contains("Sent Mail"))) {
            CSLogger.error("Send mail verification failed");
            softAssertion.fail("Send mail verification failed");
        } else {
            CSLogger.info("Mail sent to current user");
            CSLogger.info("Mail sent to current user");
        }
    }

    /**
     * This is a data provider method returns import data i.e
     * targetProductFolder,volumeFolderName,importScriptName,importCategory,
     * productTab,dataSourceType,importFolderName,dataSourceSectionFields,
     * scriptPropertiesTab,scriptAutomationTab,dataTransformationField,
     * transformedName,transformationType,importInto,transformationLanguage,
     * executionType,importLayer,validationType,dataTargetType,dataTargetId,
     * defaultLanguage,startRowNumber,limitRowNumber,batchSize,numOfmultiThread,
     * idFilter,dataTargetSectionFields,automationTabFields,userGroupName,
     * username,password,userRole,userAccessRight,loginLanguage
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
        mamStudioVolumesNodeInstance = MamStudioTree
                .getMamStudioVolumesNodeInstance(browserDriver);
        volumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
        createDataFilePath = config.getDataflowTestDataFolder()
                + "VerifyImportInAutomation-Create.xml";
        userManagementPageInstance = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        loginPageInstance = new LoginPage(browserDriver);
    }
    
}
