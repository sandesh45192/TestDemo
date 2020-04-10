/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimExportMainFramePage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This contains the test method to verify the PIM export.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyPIMExportTest extends AbstractTest {

    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPopupDivPim             csPopupDiv;
    private FrameLocators             frameLocators;
    private PimExportMainFramePage    pimExportMainFramePage;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontal;
    private String                    dataDrivenSheetName = "VerifyPIMExportTest";
    private String                    location;
    private String                    csvAttributes;
    private SoftAssert                softassertion;

    /**
     * This method test the use case of PIM XML Export test.
     * 
     * @param defaultSelectedExport String values containing the type of export
     *            to select
     * @param defaultSelectedChannel String values containing the channel type
     *            to select
     * @param defaultMemory String values containing memory value
     * @param defaultTime String values containing time value
     * @param defaultPrice String values containing price value
     * @param defaultPath String values containing path value
     * @param exportName String values containing the name of export
     * @param exportLanguage String values containing language to select
     * @param channelName String values containing the channel to select
     * @param targetFileName String values containing the target file name
     * @param xmlAttributesToCheck String values containing the attributes to
     *            verify in exported xml file, separated by comma
     * @param String csvAttributesToCheck String values containing the
     *            attributes to verify in exported csv file, separated by comma
     */
    @Test(dataProvider = "productTestData")
    public void PIMExportXmlTest(String defaultSelectedExport,
            String defaultSelectedChannel, String defaultMemory,
            String defaultTime, String defaultPrice, String defaultPath,
            String exportName, String exportLanguage, String channelName,
            String targetFileName, String xmlAttributesToCheck,
            String csvAttributesToCheck) {
        try {
            location = System.getProperty("user.home") + "\\Downloads\\";
            csvAttributes = csvAttributesToCheck;
            WebDriverWait waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPIMandExpandProductTree(waitForReload);
            createExport(exportName, waitForReload);
            goToPimProductTreeSection(waitForReload);
            pimStudioProductsNode.expandNodesByProductName(waitForReload,
                    exportName);
            goToExportView(waitForReload);
            assertDefaultValuesForExport(defaultSelectedExport,
                    defaultSelectedChannel, defaultMemory, defaultTime,
                    defaultPrice, defaultPath, exportName, waitForReload);
            configureValuesInExport(waitForReload, exportLanguage, channelName,
                    targetFileName);
            assertSavedConfigurations(waitForReload, exportLanguage,
                    channelName, targetFileName);
            csGuiToolbarHorizontal.getBtnExport().click();
            CSLogger.info("Clicked on the export icon.");
            switchToDownloadLinkFrame(waitForReload);
            String excelFileName = downloadFile();
            String zipFileName = downloadZip();
            CSUtility.tempMethodForThreadSleep(6000);
            checkFileExists(excelFileName, location);
            checkFileExists(zipFileName, location);
            String attributes[] = xmlAttributesToCheck.split(",");
            ArrayList<String> nodeNames = new ArrayList<>();
            nodeNames = getAttributesFromXmlFile(location + excelFileName,
                    nodeNames);
            VerifyAttributes(attributes, nodeNames);
            nodeNames.clear();
            nodeNames = getAttributesFromXmlZip(excelFileName,
                    location + zipFileName, nodeNames);
            VerifyAttributes(attributes, nodeNames);
            softassertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Error in the Verify PIM XML Export test :", e);
            Assert.fail("Error in the Verify PIM XML Export test :", e);
        }
    }

    /**
     * This method switches to frame containing the download link after the
     * export.
     * 
     * @param waitForReload WebDriverWait object
     */
    private void switchToDownloadLinkFrame(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                frameLocators.getFrmShowingStatusOfRunningActiveScript()));
    }

    /**
     * This method clicks on the zip file download link and returns the name of
     * file name from the link.
     * 
     * @return String value containing the name of file.
     */
    private String downloadZip() {
        WebElement zipdownloadLink = browserDriver
                .findElement(By.xpath("//a[contains(text(),'Download')][2]"));
        zipdownloadLink.click();
        CSLogger.info("Clicked on the ZIP download link.");
        String zipFileName = zipdownloadLink.getText().split(" ")[1];
        return zipFileName;
    }

    /**
     * This method clicks on the file download link and returns the name of file
     * name from the link.
     * 
     * @return String value containing the name of file.
     */
    private String downloadFile() {
        WebElement filedownloadLink = browserDriver
                .findElement(By.xpath("//a[contains(text(),'Download')][1]"));
        filedownloadLink.click();
        CSLogger.info("Clicked on the XML/CSV download link.");
        String FileName = filedownloadLink.getText().split(" ")[1];
        return FileName;
    }

    /**
     * This method test the use case of PIM CSV Export test
     */
    @Test(dependsOnMethods = { "PIMExportXmlTest" })
    public void PIMExportCsvTest() {
        try {
            WebDriverWait waitForReload = new WebDriverWait(browserDriver, 180);
            goToPimProductTreeSection(waitForReload);
            goToExportView(waitForReload);
            pimExportMainFramePage.clkExport(waitForReload);
            Select exportType = new Select(
                    pimExportMainFramePage.getDropDwnExportType());
            exportType.selectByValue("csitemcsvexport");
            pimExportMainFramePage.getTxtCsvEncoding().clear();
            pimExportMainFramePage.getTxtCsvEncoding().sendKeys("UTF-8");
            pimExportMainFramePage.clkTarget(waitForReload);
            pimExportMainFramePage.clkSeperatorTargetFile(waitForReload);
            String targetName = pimExportMainFramePage.getTxtTargetFileName()
                    .getAttribute("value").trim();
            pimExportMainFramePage.getTxtTargetFileName().clear();
            pimExportMainFramePage.getTxtTargetFileName()
                    .sendKeys(targetName.replace("xml", "csv"));
            csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
            csGuiToolbarHorizontal.getBtnExport().click();
            switchToDownloadLinkFrame(waitForReload);
            String csvFileName = downloadFile();
            String zipFileName = downloadZip();
            CSUtility.tempMethodForThreadSleep(6000);
            checkFileExists(csvFileName, location);
            checkFileExists(zipFileName, location);
            String attributes[] = csvAttributes.split(",");
            ArrayList<String> nodeNames = new ArrayList<>();
            nodeNames = getAttributesFromCsvFile(location + csvFileName,
                    nodeNames);
            VerifyAttributes(attributes, nodeNames);
            nodeNames.clear();
            nodeNames = getAttributesFromCsvZip(csvFileName,
                    location + zipFileName, nodeNames);
            VerifyAttributes(attributes, nodeNames);
        } catch (Exception e) {
            CSLogger.debug("Error in the Verify PIM XML Export test :", e);
            Assert.fail("Error in the Verify PIM XML Export test :", e);
        }
    }

    /**
     * This method asserts the default values of the export.
     * 
     * @param defaultSelectedExport String values containing the type of export
     *            to select
     * @param defaultSelectedChannel String values containing the channel type
     *            to select
     * @param defaultMemory String values containing memory value
     * @param defaultTime String values containing time value
     * @param defaultPrice String values containing price value
     * @param defaultPath String values containing path value
     * @param exportName String values containing label of export
     * @param waitForReload WebDriverWait object
     */
    private void assertDefaultValuesForExport(String defaultSelectedExport,
            String defaultSelectedChannel, String defaultMemory,
            String defaultTime, String defaultPrice, String defaultPath,
            String exportName, WebDriverWait waitForReload) {
        assertExportTabForDefaultValues(waitForReload, exportName,
                defaultSelectedExport, defaultSelectedChannel, defaultMemory,
                defaultTime);
        assertExtendedSettingsTabForDefaultValue(waitForReload, defaultPrice,
                defaultPath);
        assertTargetTabForDefaultValues(waitForReload);
    }

    /**
     * This method creates the Export in PIM.
     * 
     * @param exportName String values containing the label of the export.
     * @param waitForReload WebDriverWait object
     */
    private void createExport(String exportName, WebDriverWait waitForReload) {
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimExportNode(), browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, exportName);
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * This method verifies if all the attributes in the array exists in the
     * ArrayList or not, if not then assert is failed.
     * 
     * @param attributes String array object containing the attributes provided
     *            in the Excel file.
     * @param nodeNames ArrayList object containing the attributes retrieved
     *            from exported file.
     */
    private void VerifyAttributes(String[] attributes,
            ArrayList<String> nodeNames) {
        for (int index = 0; index < attributes.length; index++) {
            if (!nodeNames.contains(attributes[index])) {
                Assert.fail(attributes[index]
                        + " attribute doesnt exits in the exported file.");
            }
        }
    }

    /**
     * This method retrieves the attributes from from the provided xml zip file.
     * 
     * @param fileName String value containing the name of xml file inside the
     *            zip
     * @param zipFilePath String value containing the path of zip
     * @param nodeNames ArrayList object
     * @return ArrayList object containing the attributes retrieved from
     *         exported file.
     */
    private ArrayList getAttributesFromXmlZip(String fileName,
            String zipFilePath, ArrayList<String> nodeNames) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            ZipEntry zipEntry = zipFile.getEntry(fileName);
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = docBuilderFactory
                    .newDocumentBuilder();
            Document xmlDocument = documentBuilder
                    .parse(zipFile.getInputStream(zipEntry));
            xmlDocument.getDocumentElement().normalize();
            Node generalNode = xmlDocument.getElementsByTagName("General")
                    .item(0);
            NodeList attributeNodes = generalNode.getChildNodes();
            for (int index = 0; index < attributeNodes.getLength(); index++) {
                Node attributeNode = attributeNodes.item(index);
                if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
                    nodeNames.add(attributeNode.getNodeName());
                }
            }
            zipFile.close();
            FileUtils.forceDelete(new File(zipFilePath));
        } catch (Exception e) {
            CSLogger.error(e.getMessage(), e);
        }
        return nodeNames;
    }

    /**
     * This method retrieves the attributes from the provided xml file.
     * 
     * @param excelFilePath String value containing the path of the xml file.
     * @param nodeNames ArrayList object
     * @return ArrayList object containing the attributes retrieved from
     *         exported file.
     */
    private ArrayList getAttributesFromXmlFile(String excelFilePath,
            ArrayList<String> nodeNames) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = docBuilderFactory
                    .newDocumentBuilder();
            Document document = documentBuilder.parse(excelFilePath);
            document.getDocumentElement().normalize();
            Node generalNode = document.getElementsByTagName("General").item(0);
            NodeList attributeNodes = generalNode.getChildNodes();
            for (int index = 0; index < attributeNodes.getLength(); index++) {
                Node attributeNode = attributeNodes.item(index);
                if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
                    nodeNames.add(attributeNode.getNodeName());
                }
            }
        } catch (Exception e) {
            CSLogger.error(e.getMessage(), e);
        }
        return nodeNames;
    }

    /**
     * This method retrieves the attributes from from the provided csv zip file.
     * 
     * @param fileName String value containing the name of csv file inside the
     *            zip
     * @param zipFilePath String value containing the path of zip
     * @param nodeNames ArrayList object
     * @return ArrayList object containing the attributes retrieved from
     *         exported file.
     */
    private ArrayList getAttributesFromCsvZip(String fileName,
            String zipFilePath, ArrayList<String> nodeNames) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            ZipEntry zipEntry = zipFile.getEntry(fileName.trim());
            Scanner scanner = new Scanner(zipFile.getInputStream(zipEntry));
            String header[] = scanner.nextLine().split(";");
            for (int index = 0; index < header.length; index++) {
                nodeNames.add(header[index]);
            }
        } catch (Exception e) {
            CSLogger.error(e.getMessage(), e);
        }
        return nodeNames;
    }

    /**
     * This method retrieves the attributes from the provided csv file.
     * 
     * @param csvFilePath String value containing the path of the csv file.
     * @param nodeNames ArrayList object
     * @return ArrayList object containing the attributes retrieved from
     *         exported file.
     */
    private ArrayList getAttributesFromCsvFile(String csvFilePath,
            ArrayList<String> nodeNames) {
        try {
            File csvFile = new File(csvFilePath);
            Scanner scanner = new Scanner(csvFile);
            String header[] = scanner.nextLine().split(";");
            for (int index = 0; index < header.length; index++) {
                nodeNames.add(header[index]);
            }
        } catch (Exception e) {
            CSLogger.error(e.getMessage(), e);
        }
        return nodeNames;
    }

    /**
     * This method checks if the file exists or not, if not then assert is
     * failed.
     * 
     * @param FileName String value containing the name of file
     * @param location String values containing the path of parent folder
     */
    private void checkFileExists(String FileName, String location) {
        File exportedFile = new File(location + FileName.trim());
        if (!exportedFile.exists()) {
            Assert.fail("Exported file doesnt exist in download folder. ["
                    + location + FileName + "]");
        }
    }

    /**
     * This method asserts the values which were set in the Export and the
     * Target tab.
     * 
     * @param waitForReload WebDriverWait object
     * @param exportLanguage String value containing the language.
     * @param channelName String value containing the channel name
     * @param TargetFileName String value containing the name of target file
     */
    private void assertSavedConfigurations(WebDriverWait waitForReload,
            String exportLanguage, String channelName, String TargetFileName) {
        pimExportMainFramePage.clkExport(waitForReload);
        pimExportMainFramePage.clkSeperatorGeneral(waitForReload);
        String savedLang = browserDriver.findElement(By
                .xpath("//select[contains(@id,'PdmexportLanguageIDsMultiSelect')]"
                        + "/option[1]"))
                .getText().trim();
        Assert.assertEquals(savedLang, exportLanguage);
        String savedChannel = getValueOfElement(
                pimExportMainFramePage.getPopupChannelSelection())
                        .split(" ")[0];
        Assert.assertEquals(savedChannel, channelName);
        pimExportMainFramePage.clkTarget(waitForReload);
        pimExportMainFramePage.clkSeperatorTargetFile(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        assertCustomStringValue(pimExportMainFramePage.getTxtTargetFileName(),
                TargetFileName);
        assertCustomStringValue(pimExportMainFramePage.getChkboxAddDate(), "1");
        assertCustomStringValue(pimExportMainFramePage.getChkboxAddUser(), "1");
        assertCustomStringValue(
                pimExportMainFramePage.getChkboxExportDocuments(), "1");
    }

    /**
     * This method sets provided values for the Export tab.
     * 
     * @param waitForReload WebDriverWait object
     * @param exportLanguage String value containing the language to set
     * @param channelName String value containing the channel name to select
     * @param TargetFileName String value containing the name of target file
     */
    private void configureValuesInExport(WebDriverWait waitForReload,
            String exportLanguage, String channelName, String TargetFileName) {
        pimExportMainFramePage.clkExport(waitForReload);
        pimExportMainFramePage.clkSeperatorGeneral(waitForReload);
        Actions actions = new Actions(browserDriver);
        WebElement Element = browserDriver.findElement(By.xpath(
                "//tr[@id='AttributeRow_Plugin']//td[contains(@class,'first')]"));
        CSUtility.scrollUpOrDownToElement(Element, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        actions.doubleClick(browserDriver.findElement(By
                .xpath("//select[contains(@id,'PdmexportLanguageIDsNotSelected')]"
                        + "//option[contains(text(),'" + exportLanguage
                        + "')]")))
                .build().perform();
        WebElement channelSelection = pimExportMainFramePage
                .getPopupChannelSelection();
        channelSelection.click();
        switchToGuiDialouge(waitForReload);
        WebElement channelElement = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + channelName + "')]"));
        actions.doubleClick(channelElement).build().perform();
        goToExportView(waitForReload);
        pimExportMainFramePage.clkTarget(waitForReload);
        pimExportMainFramePage.clkSeperatorTargetFile(waitForReload);
        pimExportMainFramePage.getTxtTargetFileName().sendKeys(TargetFileName);
        pimExportMainFramePage.getChkboxAddDateGui().click();
        pimExportMainFramePage.getChkboxAddUserGui().click();
        pimExportMainFramePage.getChkboxExportDocumentsGui().click();
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method switches to the dialogue box for channel selection.
     * 
     * @param waitForReload WebDriverWait object
     */
    private void switchToGuiDialouge(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                frameLocators.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                frameLocators.getFrmCsGuiDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                frameLocators.getFrmCsGuiDialog()));
    }

    /**
     * This method asserts default values for tab Export.
     * 
     * @param waitForReload WebDriverWait object
     */
    private void assertExportTabForDefaultValues(WebDriverWait waitForReload,
            String defaultLabel, String defaultSelectedExport,
            String defaultSelectedChannel, String defaultMemory,
            String defaultTime) {
        pimExportMainFramePage.clkExport(waitForReload);
        pimExportMainFramePage.clkSeperatorGeneral(waitForReload);
        pimExportMainFramePage.clkSeperatorChannelSection(waitForReload);
        pimExportMainFramePage.clkSeperatorSystemSettings(waitForReload);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(pimExportMainFramePage.getTxtLabel()));
        assertCustomStringValue(pimExportMainFramePage.getTxtLabel(),
                defaultLabel);
        softassertion.assertEquals(
                getDropdownDefaultValue(
                        pimExportMainFramePage.getDropDwnExportType()),
                defaultSelectedExport);
        selectDrpDwnOption(waitForReload,
                pimExportMainFramePage.getDropDwnExportType(),
                defaultSelectedExport);
        Assert.assertEquals(
                getDropdownDefaultValue(
                        pimExportMainFramePage.getDropDwnChannelSelection()),
                defaultSelectedChannel);
        assertCustomStringValue(pimExportMainFramePage.getTxtMemoryLimit(),
                defaultMemory);
        assertCustomStringValue(pimExportMainFramePage.getTxtTimeLimit(),
                defaultTime);
    }

    /**
     * This method asserts default values for tab Extended Settings.
     * 
     * @param waitForReload WebDriverWait object
     */
    private void assertExtendedSettingsTabForDefaultValue(
            WebDriverWait waitForReload, String defaultPrice,
            String defaultPath) {
        pimExportMainFramePage.clkExtendedSettings(waitForReload);
        pimExportMainFramePage.clkSeperatorPrices(waitForReload);
        pimExportMainFramePage.clkSeperatorExportPaths(waitForReload);
        Assert.assertEquals(
                getDropdownDefaultValue(
                        pimExportMainFramePage.getDropDwnExportPrices()),
                defaultPrice);
        Assert.assertEquals(getDropdownDefaultValue(
                pimExportMainFramePage.getDropDwnExportHierachieStart()),
                defaultPath);
    }

    /**
     * This method asserts default values for tab Target.
     * 
     * @param waitForReload WebDriverWait object
     */
    private void assertTargetTabForDefaultValues(WebDriverWait waitForReload) {
        pimExportMainFramePage.clkTarget(waitForReload);
        pimExportMainFramePage.clkSeperatorTargetFile(waitForReload);
        CSUtility.tempMethodForThreadSleep(10000);
        pimExportMainFramePage.clkSeperatorFtpTransfer(waitForReload);
        List<WebElement> selectionFields = browserDriver
                .findElements(By.xpath("//div[contains(@cs_name,'Target File')]"
                        + "//div[@class='CSGuiSelectionImgChooserContainer']"));
        Assert.assertEquals(2, selectionFields.size());
        assertDefaultStringValue(pimExportMainFramePage.getTxtTargetFileName());
        assertDefaultStringValue(pimExportMainFramePage.getTxtHost());
        assertDefaultStringValue(pimExportMainFramePage.getTxtFtpUserName());
        assertDefaultStringValue(pimExportMainFramePage.getTxtFtpPassword());
        assertDefaultStringValue(pimExportMainFramePage.getTxtFtpPort());
        assertDefaultStringValue(pimExportMainFramePage.getTxtFtpRoot());
        assertZeroStringValue(pimExportMainFramePage.getChkboxAddDate());
        assertZeroStringValue(pimExportMainFramePage.getChkboxAddUser());
        assertZeroStringValue(
                pimExportMainFramePage.getChkboxExportDocuments());
        assertZeroStringValue(pimExportMainFramePage.getChkboxFtpExport());
        assertZeroStringValue(pimExportMainFramePage.getChkboxFtpPassiveMode());
        assertZeroStringValue(pimExportMainFramePage.getChkboxFtpSslMode());
    }

    /**
     * This method retrieves the value of the provided text WebElement.
     * 
     * @param element WebElement object
     * @return String value containing the value of the element
     */
    private String getValueOfElement(WebElement element) {
        return element.getAttribute("value").trim();
    }

    /**
     * This method retrieves the value of the WebElement provided and assert it
     * with the String value "0".
     * 
     * @param element WebElement object
     */
    private void assertZeroStringValue(WebElement element) {
        Assert.assertEquals(getValueOfElement(element), "0");
    }

    /**
     * This method retrieves the value of the WebElement provided and assert it
     * with the String value "".
     * 
     * @param element WebElement object
     */
    private void assertDefaultStringValue(WebElement element) {
        Assert.assertEquals(getValueOfElement(element), "");
    }

    /**
     * This method retrieves the value of the WebElement provided and assert it
     * with the String value parameter.
     * 
     * @param element WebElement object
     * @param value String value to assert
     */
    private void assertCustomStringValue(WebElement element, String value) {
        Assert.assertEquals(getValueOfElement(element), value.trim());
    }

    /**
     * This method returns the first selected option in the drop down box.
     * 
     * @param element WebElement object of the dropdown
     * @return String containing the selected option
     */
    private String getDropdownDefaultValue(WebElement element) {
        Select handleDropdown = new Select(element);
        return handleDropdown.getFirstSelectedOption().getText().trim();
    }

    /**
     * This method traverse to the product view page.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToExportView(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimProductTreeSection(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPIMandExpandProductTree(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeExports();
    }

    /**
     * Selects the given option from drop down.
     * 
     * @param drpDwnElement Drop down WebElement.
     * @param option String object contains option to be selected.
     */
    public void selectDrpDwnOption(WebDriverWait waitForReload,
            WebElement drpDwnElement, String option) {
        CSUtility.waitForVisibilityOfElement(waitForReload, drpDwnElement);
        drpDwnElement.click();
        Select element = new Select(drpDwnElement);
        element.selectByVisibleText(option);
        CSLogger.info("Drop down option " + option + " selected");
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of credentials
     */
    @DataProvider(name = "productTestData")
    public Object[][] getExportTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                dataDrivenSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        pimExportMainFramePage = new PimExportMainFramePage(browserDriver);
        frameLocators = new FrameLocators(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        softassertion = new SoftAssert();
    }
}