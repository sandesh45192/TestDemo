/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.FlexTablePage;
import org.cs.csautomation.cs.settings.IFlexTablePopup;
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods which verifies Create Excel Xml Table
 * template test uses.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateExcelXmlTableTemplateTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private IProductPopup             productPopUp;
    private CSPortalHeader            csPortalHeader;
    private FrameLocators             locator;
    private FlexTablePage             flexTablePage;
    private IFlexTablePopup           flexTablePopup;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private IVolumePopup              demoVolumePopup;
    private SoftAssert                softasssert;
    private String                    excelXmlTableTestData = "ExcelXmlTable";

    /**
     * This method verifies Excel Xml Table template test user.
     * 
     * @param mamFolder String object contains MAM folder name
     * @param filePath String object Contains Xml file Path
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param flexLanguage String object contains language for flex table
     * @param nodeName String object contains name of node in MAM
     * @param xmlFileName String object contains Xml file name
     * @param worksheetName String object contains Xml file worksheet name
     */
    @Test(dataProvider = "ExcelXmlTableDataSheet")
    public void testCreateExcelXmlTableTemplate(String mamFolder,
            String filePath, String className, String flexProduct,
            String templateName, String tempType, String flexLanguage,
            String nodeName, String xmlFileName, String worksheetName) {
        try {
            filePath = getFilePath(filePath);
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createDataForTestCase(mamFolder, filePath, className, flexProduct,
                    nodeName);
            createNewTemplate(templateName, tempType);
            verifyCreationOfTemplate(templateName, tempType);
            addDataInDataPane(mamFolder, templateName, tempType, flexLanguage,
                    xmlFileName, nodeName, worksheetName);
            CSUtility.tempMethodForThreadSleep(1000);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmRight()));
            verifyCreatedTable(worksheetName);
            addCreatedFlexTable(flexProduct, templateName);
            testResetTableButton();
            verifyAddedFlexTable(worksheetName, false);
            addCreatedFlexTable(flexProduct, templateName);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getBtnOk());
            flexTablePage.getBtnOk().click();
            CSUtility.tempMethodForThreadSleep(1000);
            checkInProduct();
            verifyAddedFlexTable(worksheetName, true);
            softasssert.assertAll();
            CSLogger.info("create excel xml table template test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testCreateExcelXmlTableTemplate",
                    e);
            Assert.fail("Automation Error : testCreateExcelXmlTableTemplate",
                    e);
        }
    }

    /**
     * This method get the file path of current system
     * 
     * @param filePath String object contains path from sheet
     * @return path String contains current file path
     */
    private String getFilePath(String filePath) {
        String currentPath = System.getProperty("user.dir");
        String path = currentPath + filePath;
        return path;
    }

    /**
     * This method verifies Excel Xml template test uses.
     * 
     * @param worksheetName String object contains Xml file worksheet name
     * @param chkStatus boolean object
     */
    private void verifyAddedFlexTable(String worksheetName, Boolean chkStatus) {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            flexTablePage.clkOnDataTab(waitForReload);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getRightSectionEditor());
            if (chkStatus) {
                verifyCreatedTable(worksheetName);
            } else {
                WebElement standardTable = browserDriver.findElement(By.xpath(
                        "//div[contains(@id,'PdmarticleCS_ItemAttribute')]"
                                + "/table[1]"));
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        standardTable);
                if (standardTable.getAttribute("class")
                        .equals("Flex Standard FlexTable")) {
                    CSLogger.info("Reset Table Case Successful");
                } else {
                    CSLogger.error("Reset Table Case fail");
                    Assert.fail("Reset Table Case fail");
                }
            }
        } catch (Exception e) {
            CSLogger.debug("Autmation error : verifyAddedFlexTable", e);
        }
    }

    /**
     * This method verifies Creation of table.
     * 
     * @param worksheetName String object contains Xml file worksheet name
     */
    private void verifyCreatedTable(String worksheetName) {
        WebElement tableCaption = browserDriver.findElement(By.xpath(
                "//table[@class='CSFlexTable MyTable FlexTable']//caption"));
        CSUtility.waitForVisibilityOfElement(waitForReload, tableCaption);
        String caption = tableCaption.getText();
        if (worksheetName.equals(caption)) {
            CSLogger.info("Table is created successfully");
        } else {
            CSLogger.error("Table verification fail.");
            softasssert.fail("Table is created successfully");
        }
    }

    /**
     * This method performs checkin operation on product folder
     */
    public void checkInProduct() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCheckIn(waitForReload);
    }

    /**
     * This method perform test on reset table button
     * 
     */
    private void testResetTableButton() {
        flexTablePage.getBtnResetTable().click();
        verifyTextInAlertBox();
        getAlertBox(false);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        flexTablePage.getBtnResetTable().click();
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
            CSLogger.info("Clicked on Ok button.");
        } else {
            alertBox.dismiss();
            CSLogger.info("Clicked on Cancel button.");
        }
    }

    /**
     * This Method verifies the text in alert box
     */
    private void verifyTextInAlertBox() {
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        String alertText = alertBox.getText();
        String expectedText = "Do you really want to reset the table? "
                + "All settings will get lost!";
        if (alertText.equals(expectedText)) {
            CSLogger.info("Alert box message verified");
        } else {
            CSLogger.error("Alert box message not verified" + alertText);
            softasssert.fail("Alert box message not verified" + alertText);
        }
    }

    /**
     * This method add the created flex table to product.
     * 
     * @param flexProduct String object contains product name
     * @param templateName String object contains template name
     */
    private void addCreatedFlexTable(String flexProduct, String templateName) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement flexTableProduct = browserDriver
                .findElement(By.linkText(flexProduct));
        flexTableProduct.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        flexTablePage.clkOnDataTab(waitForReload);
        WebElement rightSectionTable = browserDriver.findElement(By.xpath(
                "(//table[@class='Flex Standard FlexTable']/thead/tr/th)[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, rightSectionTable);
        action.moveToElement(rightSectionTable).perform();
        CSUtility.tempMethodForThreadSleep(1000);
        flexTablePage.getBtnFlexSetting().click();
        CSLogger.info("Clicked on add button");
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        Select presetId = new Select(flexTablePage.getDrpDwnPresetId());
        presetId.selectByVisibleText(templateName);
    }

    /**
     * This method add the data in data pan of created data pane.
     * 
     * @param mamFolder String object contains MAM folder name
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param flexLanguage String object contains language for flex table
     * @param nodeName String object contains name of node in MAM
     * @param xmlFileName String object contains Xml file name
     * @param worksheetName String object contains Xml file worksheet name
     */
    private void addDataInDataPane(String mamFolder, String templateName,
            String tempType, String flexLanguage, String xmlFileName,
            String nodeName, String worksheetName) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement excelXmlNode = browserDriver
                .findElement(By.linkText(tempType));
        CSUtility.waitForVisibilityOfElement(waitForReload, excelXmlNode);
        excelXmlNode.click();
        WebElement createdTemplate = browserDriver
                .findElement(By.linkText(templateName));
        createdTemplate.click();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDataPane());
        flexTablePage.getDataPane().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexPresetLanguage());
        Select presetLanguage = new Select(
                flexTablePage.getDrpDwnFlexPresetLanguage());
        presetLanguage.selectByVisibleText(flexLanguage);
        addXmlFile(mamFolder, nodeName);
        CSUtility.tempMethodForThreadSleep(2000);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexPresetExcelXmlSheet());
        Select XmlWorkSheet = new Select(
                flexTablePage.getDrpDwnFlexPresetExcelXmlSheet());
        XmlWorkSheet.selectByVisibleText(worksheetName);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Added Data in Data Pane");
    }

    /**
     * This method add xml file to template.
     * 
     * @param mamFolder String object contains MAM folder name
     * @param nodeName String object contains name of node in MAM
     */
    private void addXmlFile(String mamFolder, String nodeName) {
        flexTablePage.clkOnBtnAddFlexPresetExcelFile(waitForReload);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame( 
                waitForReload, browserDriver);
        browserDriver.findElement(By.xpath(
                "//div[@class='ControlPaneButtonArea']/div[2]/table/tbody/tr/td"))
                .click();
        TraverseSelectionDialogFrames.traverseTillFileFoldersleftFrames(
                waitForReload, browserDriver);
        WebElement demoNode = browserDriver.findElement(By.linkText(nodeName));
        demoNode.click();
        WebElement xmlFolder = browserDriver
                .findElement(By.linkText(mamFolder));
        xmlFolder.click();
        CSUtility.tempMethodForThreadSleep(2000);
        TraverseSelectionDialogFrames.traverseTillFileFoldersCenterPane(
                waitForReload, browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        int listView  = browserDriver.findElements(
                By.xpath("//a//img[contains(@src,'listview')]")).size();
        if (listView != 0) {
            csGuiToolbarHorizontalInstance.clkBtnListView(waitForReload);
        }else{
            CSLogger.info("Already in list view.");
        }
        WebElement elemntXmlFile = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[2]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, elemntXmlFile);
        action.doubleClick(elemntXmlFile).build().perform();
    }

    /**
     * This method verifies the creation of new template
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     */
    private void verifyCreationOfTemplate(String templateName,
            String tempType) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement nodeAttrVsProd = browserDriver
                .findElement(By.linkText(tempType));
        CSUtility.waitForVisibilityOfElement(waitForReload, nodeAttrVsProd);
        nodeAttrVsProd.click();
        List<WebElement> nameOfTemplate = browserDriver
                .findElements(By.linkText(templateName));
        if (nameOfTemplate.isEmpty()) {
            CSLogger.error("Creation of New Template failed");
            Assert.fail("Creation of New Template failed");
        } else {
            CSLogger.info("Creation of Template is successful");
        }
    }

    /**
     * This method verifies the creation of new template
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     */
    private void createNewTemplate(String templateName, String tempType) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        CSUtility.rightClickTreeNode(waitForReload,
                flexTablePage.getPresetsNode(), browserDriver);
        flexTablePopup.selectPopupDivMenu(waitForReload,
                flexTablePopup.getCsPopupCreateNew(), browserDriver);
        verifyConfigurationMessage();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnGeneralPane(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getTxtFlexPresetName());
        flexTablePage.getTxtFlexPresetName().sendKeys(templateName);
        Select userAccess = new Select(
                flexTablePage.getDrpDwnFlexPresetTemplate());
        userAccess.selectByVisibleText(tempType);
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method verifies the message in editor right section
     * 
     */
    private void verifyConfigurationMessage() {
        try {
            TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                    waitForReload, browserDriver, locator);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmRight()));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getConfigurationMessage());
            String windowMessage = flexTablePage.getConfigurationMessage()
                    .getText();
            if (windowMessage.equals("Configuration required")) {
                CSLogger.info("Message Verified Sucessfully");
            } else {
                CSLogger.error("Message verification fail");
                softasssert.fail("Message verification fail");
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error : verifyEditWindowMessage", e);
        }
    }

    /**
     * This method creates the pre-requisite data require for current test case.
     * 
     * @param mamFolder String object contains MAM folder name
     * @param filePath String object Contains Xml file Path
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     * @param nodeName String object contains name of node in MAM
     * @throws Exception
     */
    private void createDataForTestCase(String mamFolder, String folderPath,
            String className, String flexProduct, String nodeName)
            throws Exception {
        csPortalHeader.clkBtnMedia(waitForReload);
        createMAMFolder(mamFolder, nodeName);
        uploadXmlFile(mamFolder, folderPath, nodeName);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        createProduct(flexProduct);
        dragAndDropClassToProduct(className, flexProduct);
    }

    /**
     * This method upload the xml file
     * 
     * @param mamFolder String object contains MAM folder name
     * @param filePath String object Contains Xml file Path
     * @param nodeName String object contains name of node in MAM
     * @throws Exception
     */
    private void uploadXmlFile(String mamFolder, String filePath,
            String nodeName) throws Exception {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        WebElement demoNode = browserDriver.findElement(By.linkText(nodeName));
        demoNode.click();
        WebElement xmlFolder = browserDriver
                .findElement(By.linkText(mamFolder));
        CSUtility.rightClickTreeNode(waitForReload, xmlFolder, browserDriver);
        demoVolumePopup.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getClkUploadNewFile(), browserDriver);
        addFileFromDialogWindow(filePath);
    }

    /**
     * This method uploads File to the dialog window
     * 
     * @param filePath contains the path of the xml file wherever the file has
     *            stored in computer drive
     * 
     */
    private void addFileFromDialogWindow(String filePath) throws Exception {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
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
     * @param filePath contains path of the xml file
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
     * This method create MAM folder.
     * 
     * @param mamFolder String object contains MAM folder name
     * @param nodeName String object contains name of node in MAM
     */
    private void createMAMFolder(String mamFolder, String nodeName) {
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        WebElement demoNode = browserDriver.findElement(By.linkText(nodeName));
        CSUtility.rightClickTreeNode(waitForReload, demoNode, browserDriver);
        demoVolumePopup.selectPopupDivMenu(waitForReload,
                demoVolumePopup.getCreateNewFolder(), browserDriver);
        demoVolumePopup.enterValueInDialogueMamStudio(waitForReload, mamFolder);
        demoVolumePopup.askBoxWindowOperationMamStudio(waitForReload, true,
                browserDriver);
        CSLogger.info("Created MAM Folder");
    }

    /**
     * This method drag and drop class to product.
     * 
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     */
    private void dragAndDropClassToProduct(String className,
            String flexProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToDragDrop = browserDriver
                .findElement(By.linkText(flexProduct));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode().click();
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        action.dragAndDrop(classNameToDragDrop, productToDragDrop).build()
                .perform();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentIdInstance.getBtnReplace()));
        csGuiDialogContentIdInstance.getBtnReplace().click();
        CSLogger.info("Drag and Drop class to Product");
    }

    /**
     * This method creates product.
     * 
     * @param productName String object contains product name
     */
    private void createProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, productName);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Product Created");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains MAMFolderName,PathOfFile,ClassName,FlexTableProductName,
     * TemplateName,TemplateType,FlexLanguage,NameOfNode,NameXmlFile,
     * NameOfWorkSheet
     * 
     * @return ExcelXmlTableTestData
     */
    @DataProvider(name = "ExcelXmlTableDataSheet")
    public Object[] ExcelXmlTableTemplate() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                excelXmlTableTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        action = new Actions(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        flexTablePopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        softasssert = new SoftAssert();
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
    }
}
