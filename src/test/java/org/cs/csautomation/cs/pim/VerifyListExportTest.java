/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This contains the test method to verify the mass Product export.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyListExportTest extends AbstractTest {

    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPopupDivPim             csPopupDiv;
    private String                    testSheetName = "VerifyListExport";
    private String                    exportedFileName;

    /**
     * This method tests the mass product export.
     * 
     * @param parentProductName
     *            String name of the parent product
     * @param childProductName
     *            String name of the child product
     * @param excelHeaderString
     *            Array array of excel headers
     * @param csvHeaderString
     *            Array array of csv headers
     */
    @Test(dataProvider = "productTestData")
    public void ListExportTest(String parentProductName,
            String childProductName, String excelHeaderString,
            String csvHeaderString) {
        String excelHeaders[] = excelHeaderString.split(",");
        String csvHeaders[] = csvHeaderString.split(",");
        try {
            WebDriverWait waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPIMandExpandProductTree(waitForReload);
            // create parent product
            createParentProduct(waitForReload, pimStudioProductsNode,
                    parentProductName);
            CSLogger.info("Created parent product.");
            int count = 1;
            while (count <= 3) {
                // create child product
                createChildProduct(parentProductName, childProductName + count,
                        waitForReload);
                CSLogger.info("Created child product.");
                count++;
            }
            clkOnCreatedProduct(parentProductName, waitForReload);
            CSUtility.tempMethodForThreadSleep(2000);
            goToProductView(waitForReload);
            selectProductsAndExport(waitForReload, "Export as Excel File");
            String parentWindow = browserDriver.getWindowHandle();
            CSUtility.tempMethodForThreadSleep(2000);
            String childWindow = (String) (browserDriver.getWindowHandles()
                    .toArray())[1];
            browserDriver.switchTo().window(childWindow);
            CSUtility.tempMethodForThreadSleep(2000);
            WebElement exportFileLink = browserDriver.findElement(By.xpath(
                    "//div[@id='CSGuiToolbar_default']/following-sibling::div[1]/a[1]"));
            String exportFileName = exportFileLink.getText();
            browserDriver.close();
            browserDriver.switchTo().window(parentWindow);
            CSUtility.tempMethodForThreadSleep(4000);
            String location = System.getProperty("user.home") + "\\Downloads\\";
            ArrayList<String> cellData;
            cellData = getColumnsFromXLS(location + "" + exportFileName);
            verifyExportedFile(excelHeaders, cellData);
            goToProductView(waitForReload);
            selectProductsAndExport(waitForReload, "Export as CSV File");
            CSUtility.tempMethodForThreadSleep(3000);
            parentWindow = browserDriver.getWindowHandle();
            childWindow = (String) (browserDriver.getWindowHandles()
                    .toArray())[1];
            browserDriver.switchTo().window(childWindow);
            exportFileLink = browserDriver.findElement(By.xpath(
                    "//div[@id='CSGuiToolbar_default']/following-sibling::div[1]/a[1]"));
            exportFileName = exportFileLink.getText();
            browserDriver.close();
            browserDriver.switchTo().window(parentWindow);
            File excelFile = new File(location + "" + exportFileName);
            Scanner scanner = new Scanner(excelFile);
            String header[] = scanner.nextLine().substring(3).split(";");
            cellData.clear();
            for (String headerVale : header) {
                cellData.add(headerVale);
            }
            verifyExportedFile(csvHeaders, cellData);
        } catch (Exception e) {
            CSLogger.debug("Automation Error in product export test : " + e);
            Assert.fail("Automation Error in product export test : " + e);
        }
    }

    /**
     * This method reads the downloaded xls file and return a collection
     * instance consisting of the 2nd row.
     * 
     * @param exportFileName
     *            String object containing the filepath of xls.
     * @return ArrayList containing the 2nd row.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private ArrayList<String> getColumnsFromXLS(String exportFileName)
            throws ParserConfigurationException, SAXException, IOException {
        ArrayList<String> cellData = new ArrayList<>();
        String nodeValue;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = docBuilderFactory
                .newDocumentBuilder();
        Document document = documentBuilder.parse(exportFileName);
        document.getDocumentElement().normalize();
        Node generalNode = document.getElementsByTagName("Row").item(0);
        NodeList attributeNodes = generalNode.getChildNodes();
        for (int index = 0; index < attributeNodes.getLength(); index++) {
            Node attributeNode = attributeNodes.item(index);
            if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
                nodeValue = attributeNode.getFirstChild().getTextContent()
                        .trim();
                cellData.add(nodeValue);
            }
        }
        return cellData;
    }

    /**
     * This method verifies the header values form file.
     * 
     * @param Headers
     *            Array contains headers values to check.
     * @param cellData
     *            ArrayList contains the header values retrieved from exported
     *            file.
     */
    private void verifyExportedFile(String[] Headers,
            ArrayList<String> cellData) {
        for (int index = 0; index < Headers.length; index++) {
            if (!cellData.contains(Headers[index])) {
                Assert.fail("Exported file doesn't contain expected column : "
                        + Headers[index]);
            }
        }
    }

    /**
     * This method selects the products and initiate the export process.
     * 
     * @param waitForReload
     * @param exportType
     *            String Option to select from the list
     */
    private void selectProductsAndExport(WebDriverWait waitForReload,
            String exportType) {
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//table[contains(@class,'CSAdminList')]")));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.id("CSBuilderMarkAllActionTop")));
        WebElement selectBox = browserDriver
                .findElement(By.id("CSBuilderMarkAllActionTop"));
        selectBox.click();
        Select languageSelectInstance = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        languageSelectInstance.selectByVisibleText(exportType);
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
    }

    /**
     * This method traverse to the product view page.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToProductView(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void switchToPIMandExpandProductTree(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
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
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, parentProductName);
        CSLogger.info(
                "Entered value " + parentProductName + " in the textfield.");
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimProductTreeSection(waitForReload);
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload
     *            WebDriverWait Object
     */
    private void goToPimProductTreeSection(WebDriverWait waitForReload) {
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
            String childProductName, WebDriverWait waitForReload) {
        WebElement element = pimStudioProductsNode
                .expandNodesByProductName(waitForReload, parentProductName);
        CSUtility.rightClickTreeNode(waitForReload, element, browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCsGuiPopupMenuNewChild(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, childProductName);
        CSLogger.info(
                "Entered value " + childProductName + " in the textfield.");
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimProductTreeSection(waitForReload);
    }

    private void clkOnCreatedProduct(String productName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement product = browserDriver
                .findElement(By.linkText(productName));
        product.click();
        CSLogger.info("Clicked on product : " + productName);
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of credentials
     */

    @DataProvider(name = "productTestData")
    public Object[][] getExportTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"), testSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
    }
}
