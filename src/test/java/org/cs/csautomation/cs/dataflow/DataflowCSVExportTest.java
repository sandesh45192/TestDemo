
package org.cs.csautomation.cs.dataflow;

/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

/**
 * This class contains the test method to export data.
 * 
 * @author CSAutomation Team
 *
 */
public class DataflowCSVExportTest extends AbstractTest {

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
    private String                    createExportTestSheet = "CreateExportActiveScript";
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
    private int                       expectedJobCount;
    private int                       actualJobCount;

    /**
     * This test method creates the export active script.
     * 
     * @param testData
     *            Array of String contains the test data.
     */
    @Test(priority = 1, dataProvider = "exportTestData", groups = {
            "Dataflow csv export test" })
    public void testCreateExportActiveScript(String... testData) {
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
            goToSystemPreferencesIcon();
            createExportActiveScript();
            configureExportActiveScript(exportData.get("exportLabel"),
                    exportData.get("exportCategory"));
            saveExportActiveScript();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method : "
                    + "testCreateExportActiveScript", e);
            Assert.fail("Automation error in test method : "
                    + "testCreateExportActiveScript", e);
        }
    }

    /**
     * This test method configures the data source section of export script.
     */
    @Test(priority = 2, groups = { "Dataflow csv export test" })
    public void testConfigureDataSourceSection() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            verifyDataSourceSectionFields(
                    exportData.get("dataSourceSectionFields").split(","));
            configureDataSource(exportData.get("dataSourceType"),
                    exportData.get("dataSelectionType"),
                    exportData.get("parentProductName"),
                    exportData.get("selectionLayer"),
                    exportData.get("defaultLanguage"),
                    exportData.get("startCount"), exportData.get("maxCount"),
                    exportData.get("batchSize"));
            verifyDummyRecords();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testConfigureDataSourceSection",
                    e);
            Assert.fail(
                    "Automation error in test method : testConfigureDataSourceSection",
                    e);
        }
    }

    /**
     * This test method configures the data target section of export script.
     */
    @Test(priority = 3, groups = { "Dataflow csv export test" })
    public void testConfiureDataTargetSection() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            traverseToScriptConfiguration();
            verifyDataTargetSectionFields(exportData.get("dataTargetType"),
                    exportData.get("fileName"),
                    exportData.get("csvDeliminator"),
                    exportData.get("csvEnclosure"),
                    exportData.get("csvEncoding"));
            executeExportActiveScript(false);
            executeExportActiveScript(true);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testConfiureDataTargetSection",
                    e);
            Assert.fail(
                    "Automation error in test method : testConfiureDataTargetSection",
                    e);
        }
    }

    /**
     * This test method verifies the active script jobs and exported file data
     * and also prints the logs to logger file.
     */
    @Test(priority = 4, groups = { "Dataflow csv export test" })
    public void testVerifyActiveScriptJobs() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            verifyActiveScriptJob();
            printActiveScriptLogs();
            verifyExportedFileData(false);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testVerifyActiveScriptJobs",
                    e);
            Assert.fail(
                    "Automation error in test method : testVerifyActiveScriptJobs",
                    e);
        }
    }

    /**
     * This test method configures the data transformation section.
     */
    @Test(priority = 5, groups = { "Dataflow csv export test" })
    public void testConfigureDataTranformation() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            String[] transformationFields = exportData
                    .get("dataTransformationField").split(",");
            configureDataTransformationSection(exportData.get("exportLabel"),
                    transformationFields, exportData.get("transformedName"),
                    exportData.get("transformationType"),
                    exportData.get("transformationStatus"),
                    exportData.get("defaultLanguage"),
                    exportData.get("postfixData"));
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method : "
                    + "testConfigureDataTranformation", e);
            Assert.fail("Automation error in test method : "
                    + "testConfigureDataTranformation", e);
        }
    }

    /**
     * This test method verifies the exported file with transformed product
     * data.
     */
    @Test(priority = 6, groups = { "Dataflow csv export test" })
    public void verifyTransformationInExportFiles() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            softAssertion = new SoftAssert();
            selectFieldFromApplyTransformation(
                    exportData.get("transformedName"));
            saveExportActiveScript();
            executeExportActiveScript(true);
            verifyActiveScriptJob();
            printActiveScriptLogs();
            verifyExportedFileData(true);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method : "
                    + "verifyTransformationInExportFiles", e);
            Assert.fail("Automation error in test method : "
                    + "verifyTransformationInExportFiles", e);
        }
    }

    /**
     * This method clicks on Jobs tab.
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
     * This method traverse to the PIM module from Home module of application
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
     * @param waitForReload
     *            WebDriverWait Object
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
     * Configures the export script.
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
        List<WebElement> getTableData = browserDriver.findElements(By
                .xpath("//div[@id='content__sections::Data Source']/table/tbody/tr/td"
                        + "[1]"));
        verifyFields(getTableData, dataSourceFields, "data source");
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
     * Clicks on dummy record link.
     */
    private void verifyDummyRecords() {
        goToSystemPreferencesIcon();
        traverseToScriptConfiguration();
        String parentWindow = browserDriver.getWindowHandle();
        WebElement informationLink = browserDriver
                .findElement(By.xpath("//span[@id='dataSourceInformation']/a"));
        CSUtility.waitForElementToBeClickable(waitForReload, informationLink);
        informationLink.click();
        CSLogger.info("Clicked on information link to view dummy records");
        CSLogger.info("Clicked on dummy record link");
        CSUtility.tempMethodForThreadSleep(2000);
        String childWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(childWindow);
        CSLogger.info("Switched to dummy record window tab");
        WebElement okButton = browserDriver
                .findElement(By.id("CSGUI_MODALDIALOG_OKBUTTON"));
        okButton.click();
        CSLogger.info("Clicked on dummy records window ok button");
        // To do - Verification of dummy page content
        browserDriver.switchTo().window(parentWindow);
    }

    /**
     * Configures the actual data to be exported.
     * 
     * @param isTransformationApplied
     *            Boolean parameter contains either true or false values.
     */
    private void readActualData(Boolean isTransformationApplied) {
        actualData = new ArrayList<String>();
        if (isTransformationApplied) {
            actualData.add(exportData.get("transformedName"));
            actualData.add(exportData.get("parentProductName")
                    + exportData.get("postfixData"));
            actualData.add(exportData.get("firstChildProductName")
                    + exportData.get("postfixData"));
            actualData.add(exportData.get("secondChildProductName")
                    + exportData.get("postfixData"));
        } else {
            ArrayList<String> productData = null;
            actualHeaders = new ArrayList<>();
            String exportedAttributes[] = exportData.get("attributesName")
                    .split(",");
            actualHeaders.add("Label");
            for (int i = 0; i < exportedAttributes.length; i++) {
                actualHeaders.add(exportedAttributes[i]);
            }
            actualData.addAll(actualHeaders);
            String testData[] = { exportData.get("parentProductData"),
                    exportData.get("firstChildProductData"),
                    exportData.get("secondChildProductData") };
            for (int i = 0; i < testData.length; i++) {
                String spiltedData[] = testData[i].split(",");
                String sltTextData = "\"" + spiltedData[1] + "\"";
                String multiTextData = "\"<p>" + spiltedData[2] + "</p>\"";
                String refToPimData = "Pdmarticle:" + referenceProductId;
                productData = new ArrayList<String>(Arrays.asList(spiltedData));
                productData.set(1, sltTextData);
                productData.set(2, multiTextData);
                productData.add(3, refToPimData);
                actualData.addAll(productData);
            }
        }
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param productName
     *            String containing name of product
     * @return product Id String object containing Id of product
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
     * Verifies the fields of data target section.
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
    private void verifyDataTargetSectionFields(String dataTargetType,
            String fileName, String csvDeliminator, String csvEnclosure,
            String csvEncoding) {
        try {
            activeJobsPageInstance
                    .clkWebElement(activeJobsPageInstance.getSecDataTarget());
        } catch (Exception e) {
            CSLogger.error("Data target section not found : test step failed");
            Assert.fail("Data target section not found : test step failed");
        }
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance.getDrpDwnDataTargetType(),
                dataTargetType);
        String defaultFileName = activeJobsPageInstance
                .getTxtDataTargetFileName().getAttribute("defaultvalue");
        String defaultDelimiter = activeJobsPageInstance
                .getTxtDataTargetDelimiter().getAttribute("defaultvalue");
        String defaultCSVEnclosure = activeJobsPageInstance
                .getTxtDataTargetCSVEncloser().getAttribute("defaultvalue");
        String defaultCSVEncoding = activeJobsPageInstance
                .getDrpDwnDataTargetCSVEncoding().getAttribute("defaultvalue");
        if (fileName.equals(defaultFileName)
                && csvDeliminator.equals(defaultDelimiter)
                && csvEnclosure.equals(defaultCSVEnclosure)
                && csvEncoding.equals(defaultCSVEncoding)) {
            CSLogger.info(
                    "Data target section fields verified successfully : test "
                            + "step passed");
        } else {
            CSLogger.error(
                    "Data target section fields verification failed : test "
                            + "step failed");
            Assert.fail("Data target section fields verification failed : test "
                    + "step failed");
        }
    }

    /**
     * Executes the configured export script.
     * 
     * @param isPressOkay
     *            Boolean parameter contains values whether to execute the
     *            script or not.
     */
    private void executeExportActiveScript(Boolean isPressOkay) {
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
            if (isPressOkay) {
                alertBox.accept();
            } else {
                alertBox.dismiss();
            }
        } catch (Exception e) {
            CSLogger.error("Error while running the active script", e);
            Assert.fail("Error while running the active script", e);
        }
    }

    /**
     * Returns the count of number of jobs on jobs tab.
     * 
     * @return
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
                + "Last 50 Messages");
        for (WebElement cell : activeJobLogs) {
            CSLogger.info(cell.getText());
        }
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
        exportedFileName = downloadFileLink.getText().split("/")[2];
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * Verifies the exported CSV data file.
     * 
     * @throws IOException
     */
    private void verifyExportedFileData(Boolean isTransformationApplied) {
        checkExportedFileExists();
        readActualData(isTransformationApplied);
        Object[] exportedProductData;
        Object[] actualProductData;
        ArrayList<String> exportedData = readDataFromCSVFile();
        exportedProductData = exportedData.toArray();
        actualProductData = actualData.toArray();
        for (int index = 0; index < actualProductData.length; index++) {
            if (!(actualProductData[index]
                    .equals(exportedProductData[index]))) {
                CSLogger.error(actualProductData[index]
                        + " product content does not exists in the exported file");
                softAssertion.fail(actualProductData[index]
                        + " product content does not exists in the exported file");
            }
        }
        if (exportedData.equals(actualData)) {
            CSLogger.info("Data exported successfully");
        } else {
            CSLogger.error(
                    "Data export failed,please check exported csv file content");
            softAssertion.fail(
                    "Data export failed,please check exported csv file content");
        }
    }

    /**
     * Reads data from exported CSV file
     * 
     * @return
     */
    private ArrayList<String> readDataFromCSVFile() {
        ArrayList<String> exportedData = new ArrayList<>();
        BufferedReader csvReader;
        try {
            csvReader = new BufferedReader(
                    new FileReader(fileLocation + exportedFileName));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] rowData = row.replaceAll("\0", "").split(";");
                for (int index = 0; index < rowData.length; index++) {
                    exportedData.add(rowData[index]);
                }
            }
            csvReader.close();
        } catch (Exception e) {
            CSLogger.error("Error while reading exported CSV file", e);
            Assert.fail("Error while reading exported CSV file", e);
        }
        return exportedData;
    }

    /**
     * This method checks if the file exists or not, if not then assert is
     * failed.
     */
    private void checkExportedFileExists() {
        downloadExportedFile();
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
                "dataTargetType", "fileName", "csvDeliminator", "csvEnclosure",
                "csvEncoding", "dataTransformationField", "transformedName",
                "transformationType", "transformationStatus",
                "transformationLanguage", "postfixData" };
        try {
            for (int index = 0; index < sheetData.length; index++) {
                exportData.put(sheetData[index], testData[index]);
            }
        } catch (Exception e) {
            CSLogger.debug("Error while reading test data ", e);
            Assert.fail("Error while reading test data", e);
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
     * Enters the data in filter bar.
     * 
     * @param waitForReload
     *            WebDriverWait object.
     * @param data
     *            String object contains data to be entered in filter bar.
     */
    public void enterDataInFilterBar(WebDriverWait waitForReload, String data) {
        CSUtility.tempMethodForThreadSleep(1000);
        int removeFilterButtonExists = browserDriver
                .findElements(
                        By.xpath("//a/img[contains(@src,'nofilter.gif')]/.."))
                .size();
        if (removeFilterButtonExists != 0) {
            csGuiToolbarHorizontalInstance.clkOnBtnNoFilter(waitForReload);
        } else {
            csGuiToolbarHorizontalInstance.clkOnBtnFilter(waitForReload);
        }
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.getTxtFilterBar().click();
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.getTxtFilterBar().clear();
        csGuiToolbarHorizontalInstance.getTxtFilterBar().sendKeys(data);
        csGuiToolbarHorizontalInstance.getTxtFilterBar().sendKeys(Keys.ENTER);
        CSLogger.info("Entered text  " + data + "in filter bar");
    }

    /**
     * Configures the data transformation section of export script.
     * 
     * @param scriptName
     *            String object contains configured export script name.
     * @param transformationFields
     *            String object contains array of data transformation fields.
     * @param transformedName
     *            String object contains transformed name of an attribute. for
     *            e.g Defining Name as'Label'.
     * @param transformationType
     *            String object contains transformation type.
     * @param transformationStatus
     *            String object contains transformation status.
     * @param transformationLanguage
     *            String object contains transformation language.
     * @param postfixText
     *            String object contains post fix data.
     */
    private void configureDataTransformationSection(String scriptName,
            String[] transformationFields, String transformedName,
            String transformationType, String transformationStatus,
            String transformationLanguage, String postfixText) {
        traverseToScriptConfiguration();
        String parentWindow = browserDriver.getWindowHandle();
        WebElement propertiesTab = browserDriver
                .findElement(By.xpath("//nobr[contains(text(),'Properties')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, propertiesTab);
        propertiesTab.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.scrollUpOrDownToElement(
                activeJobsPageInstance.getSecDataTransformation(),
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPageInstance.getSecDataTransformation());
        activeJobsPageInstance.clkWebElement(
                activeJobsPageInstance.getSecDataTransformation());
        CSUtility.tempMethodForThreadSleep(2000);
        activeJobsPageInstance.checkUseTransformationEditor(waitForReload);
        activeJobsPageInstance.clkOnGivenWebElement(waitForReload,
                activeJobsPageInstance.getBtnEditTransformation());
        CSUtility.tempMethodForThreadSleep(1000);
        Object[] tempWindow = browserDriver.getWindowHandles().toArray();
        String[] childWindow = Arrays.copyOf(tempWindow, tempWindow.length,
                String[].class);
        browserDriver.switchTo().window(childWindow[1]);
        verifyTransformationWindow(scriptName);
        performMappingOperation("Label", transformationFields, transformedName,
                transformationType, transformationStatus,
                transformationLanguage, postfixText);
        CSUtility.tempMethodForThreadSleep(20000);
        for (int i = 2; i < childWindow.length; i++) {
            browserDriver.switchTo().window(childWindow[i]);
            browserDriver.close();
        }
        browserDriver.switchTo().window(parentWindow);
        CSLogger.info("Switched to parent window");
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
     * @param transformationStatus
     *            String object contains transformation status.
     * @param transformationLanguage
     *            String object contains transformation language.
     * @param postfixText
     *            String object contains post fix data.
     */
    private void performMappingOperation(String dataSourceLabelField,
            String[] transformationFields, String transformedName,
            String transformationType, String transformationStatus,
            String transformationLanguage, String postfixText) {
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
                            + "[contains(text(),'" + "Label"
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
                transformationType, transformationStatus,
                transformationLanguage, postfixText);
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
        CSLogger.info("Clicked on 'Display Transformation Mapping'");
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
     * Selects the given field from apply transformation.
     * 
     * @param fieldName
     *            String object contains name of field.
     */
    private void selectFieldFromApplyTransformation(String fieldName) {
        TraversingForSettingsModule.traverseToFrameEdit(waitForReload,
                browserDriver, iframeLocatorsInstance);
        WebElement fieldToBeSelected = browserDriver.findElement(By
                .xpath("//select[contains(@id,'ActivescriptExportTransformation"
                        + "IDsNotSelected')]/option[contains(text(),'"
                        + fieldName + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, fieldToBeSelected);
        performAction.doubleClick(fieldToBeSelected).perform();
        CSLogger.info(
                "Field " + fieldName + " selected from apply transformation");
    }

    /**
     * Configures the data transformation section.
     * 
     * @param dataSourceLabelField
     *            String object contains data source field name.
     * @param transformedName
     *            String object contains transformed name of an attribute. for
     *            e.g Defining Name as'Label'.
     * @param transformationType
     *            String object contains transformation type.
     * @param transformationStatus
     *            String object contains transformation status.
     * @param transformationLanguage
     *            String object contains transformation language.
     * @param postfixText
     *            String object contains post fix data.
     */
    private void configureTransformation(String dataSourceFieldName,
            String transformedName, String transformationType,
            String transformationStatus, String transformationLanguage,
            String postfixText) {
        WebElement label = browserDriver.findElement(By.xpath(
                "//input[contains(@id,'DataflowexporttransformationLabel')]"));
        enterTextIntoTextbox(label, transformedName);
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement drpDwnTransformationType = browserDriver.findElement(By
                .xpath("//select[contains(@id,'DataflowexporttransformationExpor"
                        + "tTransformationType')]/option[contains(text(),'"
                        + transformationType + "')]"));
        drpDwnTransformationType.click();
        WebElement drpDwnTransformationStatus = browserDriver.findElement(By
                .xpath("//select[contains(@id,'DataflowexporttransformationStatus')]"));
        selectDrpDwnOption(drpDwnTransformationStatus, transformationStatus);
        selectDataSourceField(dataSourceFieldName);
        WebElement drpDwnTransformationLanguage = browserDriver.findElement(By
                .xpath("//select[contains(@id,'DataflowexporttransformationLanguageID')]"));
        selectDrpDwnOption(drpDwnTransformationLanguage,
                transformationLanguage);
        selectTransformationFunction("Add Postfix");
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement txtPostfix = browserDriver.findElement(By.xpath(
                "//input[contains(@id,'DataflowexporttransformationPostfix')]"));
        enterTextIntoTextbox(txtPostfix, postfixText);
        saveExportActiveScript();
        CSUtility.switchToDefaultFrame(browserDriver);
        WebElement OkButton = browserDriver
                .findElement(By.id("CSGUI_MODALDIALOG_OKBUTTON"));
        OkButton.click();
        CSLogger.info("Clicked on OK button of transformation window");
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
     * Selects the given field.
     * 
     * @param dataSourceFieldName
     *            String object contains field name.
     */
    private void selectDataSourceField(String dataSourceFieldName) {
        CSUtility.tempMethodForThreadSleep(1000);
        int isDataSourceFieldSelected = browserDriver.findElements(By
                .xpath("//select[contains(@id,'DataflowexporttransformationSourc"
                        + "eFieldsNotSelected')]/option[contains(@value,'"
                        + dataSourceFieldName + "')]"))
                .size();
        if (isDataSourceFieldSelected == 0) {
            CSLogger.info("Data source field " + dataSourceFieldName
                    + " is already selected");
        } else {
            CSLogger.error("Data source field is not by default selected, "
                    + "test step failed");
            Assert.fail("Data source field is not by default selected, "
                    + "test step failed");
        }
    }

    /**
     * Selects the given transformation function.
     * 
     * @param tranformationFunction
     *            String object contains transformation function.
     */
    private void selectTransformationFunction(String tranformationFunction) {
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement function = browserDriver.findElement(By
                .xpath("//select[contains(@id,'DataflowexporttransformationFunc"
                        + "tionsNotSelected')]/option[contains(text(),'"
                        + tranformationFunction + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, function);
        CSUtility.tempMethodForThreadSleep(1000);
        performAction.doubleClick(function).perform();
        CSLogger.info("Transformation function : " + tranformationFunction
                + " selected");
    }

    /**
     * This is a data provider method returns test data i.e
     * ParentProductName,FirstChildProductName,SecondChildProductName,
     * AttributeFolderName,AttributesName,AttributeType,AttributeValue,
     * ClassName,ParentProductData,FirstChildProductData,SecondChildProductData,
     * ProductTab,RefToPimAttrName,ExportLabel,ExportCategory,
     * DataSourceSectionFields,DataSourceType,DataSelectionType,SelectionLayer,
     * DefaultLanguage,StartCount,MaxCount,BatchSize,DataTargetType,FileName,
     * CSVDeliminator,CSVEnclosure,CSVEncoding,DataTransformationField
     * transformedName,transformationType,transformationStatus,
     * transformationLanguage,PostfixData
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
    }
}
