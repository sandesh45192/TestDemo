/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.elasticsearch;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.settings.ActiveJobsPage;
import org.cs.csautomation.cs.settings.ElasticSearchPage;
import org.cs.csautomation.cs.settings.FlexTablePage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.SettingsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies Pre-requisites for
 * elastic Search.
 * 
 * @author CSAutomation Team
 *
 */
public class ConnectToElasticSubscriberTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private ActiveJobsPage            activeJobsPage;
    private SettingsPage              settingPage;
    private FrameLocators             locator;
    private ElasticSearchPage         elasticSearchPage;
    private FlexTablePage             flexTablePage;
    private IProductPopup             productPopUp;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private String                    productName     = "TestStatusExport";
    private String                    strContainsLogs = "ElasticSubscriber  [Active]    Pending Messages: 0";

    /**
     * This Method verifies the pre-requisites for elastic Search
     * 
     */
    @Test
    private void testConnectToElasticSubscriber() {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            traverseToNodeExportFramework();
//            enableButtonInCoreDetails();
            chkCassandraStatus();
            enableElasticSearch();
//            exportExecuterDetail();
            exportStatistics();
            verifyExportRunningStatus();
        } catch (Exception e) {
            CSLogger.debug("Automation error : testConnectToElasticSubscriber",
                    e);
            Assert.fail("Automation error : testConnectToElasticSubscriber", e);
        }
    }

    /**
     * This method start exicution of export
     * 
     */
    private void exportExecuterDetail() {
        String txtLogs = null;
        boolean flag = true;
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkWebElement(waitForReload,
                elasticSearchPage.getNodeExportExecuterDetails());
        openSection("Export Executer Details");
        elasticSearchPage.clkWebElement(waitForReload,
                elasticSearchPage.getBtnStartExportExecutor());
        CSUtility.tempMethodForThreadSleep(2000);
        openSection("Log Details");
        while (flag) {
            elasticSearchPage.clkWebElement(waitForReload,
                    elasticSearchPage.getBtnRefreshExportExecutorLogs());
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    elasticSearchPage.getTxtExportExecutorLogs());
            txtLogs = elasticSearchPage.getTxtExportExecutorLogs().getText();
            if (txtLogs.contains(strContainsLogs)) {
                flag = false;
            }
            CSUtility.tempMethodForThreadSleep(5000);
        }
    }

    /**
     * This method verifies that export is running on the system
     * 
     */
    private void verifyExportRunningStatus() {
        int previousCount = Integer.parseInt(getTextFromElement(
                elasticSearchPage.getTxtContentservCountPdmarticle()));
        createProduct();
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        traverseToNodeExportFramework();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkWebElement(waitForReload,
                elasticSearchPage.getNodeExportStatistics());
        elasticSearchPage.clkWebElement(waitForReload,
                elasticSearchPage.getBtnRefreshAll());
        CSUtility.tempMethodForThreadSleep(5000);
        elasticSearchPage.clkWebElement(waitForReload,
                elasticSearchPage.getBtnRefreshExportStatisticsPdmarticle());
        String currentCount = getTextFromElement(
                elasticSearchPage.getTxtElasticsearchCountPdmarticle());
        int currentCountInteger = Integer.parseInt(currentCount);
        if (currentCountInteger == (previousCount + 1)) {
            CSLogger.info("Product is added to data base");
        } else {
            CSLogger.error("Product is not added to data base");
            Assert.fail("Product is not added to data base");
        }
        String exportCount = getTextFromElement(
                elasticSearchPage.getTxtElasticsearchCountPdmarticle());
        chkStatusOfExportData(currentCount, exportCount, "Product");
    }

    /**
     * This method verifies the status of export statistics
     * 
     */
    private void exportStatistics() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkWebElement(waitForReload,
                elasticSearchPage.getNodeExportStatistics());
        openSection("SyncAll");
        openSection("User");
        openSection("File");
        openSection("Channel");
        openSection("Product");
        openSection("Workflow");
        openSection("Language");
        waitForDataToExport();
        chkExportStatus();
    }

    /**
     * This methos check the export status
     * 
     */
    private void chkExportStatus() {
        String exportCount = null, currentCount = null;
        elasticSearchPage.clkWebElement(waitForReload,
                elasticSearchPage.getBtnRefreshAll());
        currentCount = getTextFromElement(
                elasticSearchPage.getTxtContentservCountLanguage());
        exportCount = getTextFromElement(
                elasticSearchPage.getTxtCassandraCountLanguage());
        chkStatusOfExportData(currentCount, exportCount, "Language");
        currentCount = getTextFromElement(
                elasticSearchPage.getTxtContentservCountWorkflow());
        exportCount = getTextFromElement(
                elasticSearchPage.getTxtCassandraCountWorkflow());
        chkStatusOfExportData(currentCount, exportCount, "WorkFlow");
        currentCount = getTextFromElement(
                elasticSearchPage.getTxtContentservCountPdmarticle());
        exportCount = getTextFromElement(
                elasticSearchPage.getTxtElasticsearchCountPdmarticle());
        chkStatusOfExportData(currentCount, exportCount, "Product");
        currentCount = getTextFromElement(
                elasticSearchPage.getTxtContentservCountPdmarticlestructure());
        exportCount = getTextFromElement(elasticSearchPage
                .getTxtElasticsearchCountPdmarticlestructure());
        chkStatusOfExportData(currentCount, exportCount, "Channel");
        currentCount = getTextFromElement(
                elasticSearchPage.getTxtContentservCountMamfile());
        exportCount = getTextFromElement(
                elasticSearchPage.getTxtElasticsearchCountMamfile());
        chkStatusOfExportData(currentCount, exportCount, "MAM File");
        currentCount = getTextFromElement(
                elasticSearchPage.getTxtContentservCountUser());
        exportCount = getTextFromElement(
                elasticSearchPage.getTxtElasticsearchCountUser());
        chkStatusOfExportData(currentCount, exportCount, "User");
    }

    /**
     * This method wait for the data to export
     * 
     */
    private void waitForDataToExport() {
        String exportCount = null, currentCount = null;
        int count = 0;
        for (int index = 0; index < 10; index++) {
            count = 0;
            elasticSearchPage.clkWebElement(waitForReload,
                    elasticSearchPage.getBtnRefreshAll());
            currentCount = getTextFromElement(
                    elasticSearchPage.getTxtContentservCountLanguage());
            exportCount = getTextFromElement(
                    elasticSearchPage.getTxtCassandraCountLanguage());
            count = chkStatus(currentCount, exportCount, count);
            currentCount = getTextFromElement(
                    elasticSearchPage.getTxtContentservCountWorkflow());
            exportCount = getTextFromElement(
                    elasticSearchPage.getTxtCassandraCountWorkflow());
            count = chkStatus(currentCount, exportCount, count);
            currentCount = getTextFromElement(
                    elasticSearchPage.getTxtContentservCountPdmarticle());
            exportCount = getTextFromElement(
                    elasticSearchPage.getTxtElasticsearchCountPdmarticle());
            count = chkStatus(currentCount, exportCount, count);
            currentCount = getTextFromElement(elasticSearchPage
                    .getTxtContentservCountPdmarticlestructure());
            exportCount = getTextFromElement(elasticSearchPage
                    .getTxtElasticsearchCountPdmarticlestructure());
            count = chkStatus(currentCount, exportCount, count);
            currentCount = getTextFromElement(
                    elasticSearchPage.getTxtContentservCountMamfile());
            exportCount = getTextFromElement(
                    elasticSearchPage.getTxtElasticsearchCountMamfile());
            count = chkStatus(currentCount, exportCount, count);
            currentCount = getTextFromElement(
                    elasticSearchPage.getTxtContentservCountUser());
            exportCount = getTextFromElement(
                    elasticSearchPage.getTxtElasticsearchCountUser());
            count = chkStatus(currentCount, exportCount, count);
            if (count == 6) {
                break;
            }
            CSUtility.tempMethodForThreadSleep(3000);
        }
    }

    /**
     * This method check the status of export and increament the status count
     * 
     * @param currentCount String object contains current count
     * @param exportCount String object contains export count
     * @param StatusCount integer veriable contains count of exported section
     * @return StatusCount integer veriable contains count of exported section
     */
    private int chkStatus(String currentCount, String exportCount,
            int StatusCount) {
        if (currentCount.equals(exportCount)) {
            return (StatusCount + 1);
        } else {
            return StatusCount;
        }
    }

    /**
     * This method check the status of export data for given section
     * 
     * @param currentCount String object contains current count
     * @param exportCount String object contains export count
     * @param sectionName String object contains name of section
     */
    private void chkStatusOfExportData(String currentCount, String exportCount,
            String sectionName) {
        if (currentCount.equals(exportCount)) {
            CSLogger.info("Export successful for " + sectionName);
        } else {
            CSLogger.error("Export fail for " + sectionName);
            Assert.fail("Export fail for " + sectionName);
        }
    }

    /**
     * This method get the text from element
     * 
     * @param textElement Webelement object contains element to get text
     * @return text string object contains text of element
     */
    private String getTextFromElement(WebElement textElement) {
        CSUtility.waitForVisibilityOfElement(waitForReload, textElement);
        String text = textElement.getText();
        return text;
    }

    /**
     * This method create product.
     * 
     */
    private void createProduct() {
        csPortalHeader.clkBtnProducts(waitForReload);
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
     * This method verifies cassandra status
     * 
     */
    private void chkCassandraStatus() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkOnNodeCassandraDetails(waitForReload);
        openSection("Cassandra Configuration");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                elasticSearchPage.getTxtCassandraConnectionUrl());
        String cassendraUrl = elasticSearchPage.getTxtCassandraConnectionUrl()
                .getAttribute("value");
        openSection("Cassandra Operation");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                elasticSearchPage.gettxtStatusCassandraRUNNING());
        String textStatus = elasticSearchPage.gettxtStatusCassandraRUNNING()
                .getText();
        if (textStatus.contains(cassendraUrl + ":")) {
            CSLogger.info("Cassandra is Running.");
        } else {
            CSLogger.error("Cassandra is not Running.");
            Assert.fail("Cassandra is not Running.");
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method enable the elastic search button and verifies its status
     * 
     */
    private void enableElasticSearch() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkOnNodeElasticSearchDetails(waitForReload);
        openSection("Elasticsearch Configuration");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                elasticSearchPage.getTxtElasticSearchAddress());
        String ElasticAddress = elasticSearchPage.getTxtElasticSearchAddress()
                .getAttribute("value");
        openSection("ElasticSearch Operation");
        clkOnButton(elasticSearchPage.getChkBoxElasticSearchEnabled());
        String textStatus = elasticSearchPage.gettxtStatusElasticSearchRUNNING()
                .getText();
        if (textStatus.contains(ElasticAddress + ":")) {
            CSLogger.info("Elastic Search is Running.");
        } else {
            CSLogger.error("Elastic Search is not Running.");
            Assert.fail("Elastic Search is not Running.");
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method open the section
     * 
     * @param sectionName String object contains name of section
     */
    private void openSection(String sectionName) {
        String path = "//div[text()=' " + sectionName + "']/..";
        String extendedPath = path + "/..";
        WebElement selectionTitleParent = browserDriver
                .findElement(By.xpath(extendedPath));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                selectionTitleParent);
        if (selectionTitleParent.getAttribute("class").contains("closed")) {
            browserDriver.findElement(By.xpath(path)).click();
        }
    }

    /**
     * This method button In core detail section
     * 
     */
    private void enableButtonInCoreDetails() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkOnNodeCoreDetails(waitForReload);
        WebElement addButton = browserDriver.findElement(By.xpath(
                "//tr[contains(@cs_name,'Export Item Type')]/td[2]/table/tbody/tr[2]/td[2]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, addButton);
        addButton.click();
        elasticSearchPage.clkWebElement(waitForReload,
                elasticSearchPage.getBtnESAInitialExport());
        getAlertBox();
        CSUtility.tempMethodForThreadSleep(5000);
        CSUtility.switchToDefaultFrame(browserDriver);
        flexTablePage.clkOnBtnWindowClose(waitForReload);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkOnNodeCoreDetails(waitForReload);
        clkOnButton(elasticSearchPage.getBtnMasterProducer());
        clkOnButton(elasticSearchPage.getBtnMasterSubscriber());
        clkOnButton(elasticSearchPage.getBtnElasticSubscriber());
        clkOnButton(elasticSearchPage.getBtnWebSocketSubscriber());
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method perform the click operation on button element
     * 
     * @param elementOfButton webelement contains button element
     */
    private void clkOnButton(WebElement elementOfButton) {
        String value = getAttributeOfElement(elementOfButton);
        if (value.contains("Off")) {
            elementOfButton.click();
            getAlertBox();
        }
    }

    /**
     * This method get the attribute of element
     * 
     * @param element WebElement object
     * @return attributeValue String object contains attribute value.
     */
    private String getAttributeOfElement(WebElement element) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        String attributeValue = element.getAttribute("class");
        return attributeValue;
    }

    /**
     * This method click the button in alert box
     * 
     */
    private void getAlertBox() {
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        alertBox.accept();
        CSLogger.info("Clicked on Ok button.");
    }

    /**
     * This method traverse to node export frame work.
     * 
     */
    private void traverseToNodeExportFramework() {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
//        CSUtility.waitForVisibilityOfElement(waitForReload,
//                activeJobsPage.getSystemPreferences());
//        activeJobsPage.clkWebElement(activeJobsPage.getSystemPreferences());
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        settingPage.clkOnBtnSettingsNode(waitForReload);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        elasticSearchPage.clkOnNodeESA(waitForReload);
        elasticSearchPage.clkOnNodeExportFramework(waitForReload);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        activeJobsPage = SettingsLeftSectionMenubar
                .getActiveJobsNode(browserDriver);
        settingPage = new SettingsPage(browserDriver);
        elasticSearchPage = new ElasticSearchPage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
    }
}
