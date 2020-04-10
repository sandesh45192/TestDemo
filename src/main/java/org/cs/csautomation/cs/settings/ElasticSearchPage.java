/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains xpaths related to Elastic Search Page
 * 
 * @author CSAutomation Team
 *
 */
public class ElasticSearchPage {
    
    @FindBy(xpath = "//span[contains(text(),' ESA')]")
    private WebElement nodeESA;

    @FindBy(xpath = "//span[contains(text(),'Export Framework')]")
    private WebElement nodeExportFramework;

    @FindBy(xpath = "//span[contains(text(),'Core Details')]")
    private WebElement nodeCoreDetails;

    @FindBy(xpath = "//span[contains(text(),'Cassandra Details')]")
    private WebElement nodeCassandraDetails;

    @FindBy(xpath = "//span[contains(text(),' ElasticSearch Details')]")
    private WebElement nodeElasticSearchDetails;

    @FindBy(xpath = "//span[contains(@id,'coreMasterProducer')]")
    private WebElement btnMasterProducer;

    @FindBy(xpath = "//span[contains(@id,'coreMasterSubscriber')]")
    private WebElement btnMasterSubscriber;

    @FindBy(xpath = "//span[contains(@id,'coreElasticSubscriber')]")
    private WebElement btnElasticSubscriber;

    @FindBy(xpath = "//span[contains(@id,'coreWebSocketSubscriber')]")
    private WebElement btnWebSocketSubscriber;

    @FindBy(xpath = "//span[contains(@id,'ElasticSearchEnabled')]")
    private WebElement btnElasticSearchEnabled;

    @FindBy(xpath = "//div[@id='StatusCassandraRUNNING']")
    private WebElement txtStatusCassandraRUNNING;

    @FindBy(xpath = "//div[@id='StatusElasticSearchRUNNING']")
    private WebElement txtStatusElasticSearchRUNNING;

    @FindBy(xpath = "//span[contains(@id,'ElasticSearchEnabled')]")
    private WebElement chkBoxElasticSearchEnabled;

    @FindBy(xpath = "//span[contains(text(),'Export Executer Details')]")
    private WebElement nodeExportExecuterDetails;

    @FindBy(xpath = "//input[@id='startExportExecutor']")
    private WebElement btnStartExportExecutor;

    @FindBy(xpath = "//textarea[@id='ExportExecutorLogs']")
    private WebElement txtExportExecutorLogs;

    @FindBy(xpath = "//input[@id='RefreshExportExecutorLogs']")
    private WebElement btnRefreshExportExecutorLogs;
    
    @FindBy(xpath = "//div[@id='contentservCountLanguage']")
    private WebElement txtContentservCountLanguage;

    @FindBy(xpath = "//div[@id='cassandraCountLanguage']")
    private WebElement txtCassandraCountLanguage;

    @FindBy(xpath = "//div[@id='contentservCountWorkflow']")
    private WebElement txtContentservCountWorkflow;

    @FindBy(xpath = "//div[@id='cassandraCountWorkflow']")
    private WebElement txtCassandraCountWorkflow;

    @FindBy(xpath = "//div[@id='contentservCountPdmarticle']")
    private WebElement txtContentservCountPdmarticle;

    @FindBy(xpath = "//div[@id='elasticsearchCountPdmarticle']")
    private WebElement txtElasticsearchCountPdmarticle;
    
    @FindBy(xpath = "//div[@id='contentservCountPdmarticlestructure']")
    private WebElement txtContentservCountPdmarticlestructure;

    @FindBy(xpath = "//div[@id='elasticsearchCountPdmarticlestructure']")
    private WebElement txtElasticsearchCountPdmarticlestructure;

    @FindBy(xpath = "//div[@id='contentservCountMamfile']")
    private WebElement txtContentservCountMamfile;

    @FindBy(xpath = "//div[@id='elasticsearchCountMamfile']")
    private WebElement txtElasticsearchCountMamfile;

    @FindBy(xpath = "//div[@id='contentservCountUser']")
    private WebElement txtContentservCountUser;

    @FindBy(xpath = "//div[@id='elasticsearchCountUser']")
    private WebElement txtElasticsearchCountUser;

    @FindBy(xpath = "(//span[contains(text(),'Export Statistics')])[1]")
    private WebElement nodeExportStatistics;

    @FindBy(xpath = "//input[@id='refreshAll']")
    private WebElement btnRefreshAll;

    @FindBy(xpath = "//input[@id='refreshExportStatisticsPdmarticle']")
    private WebElement btnRefreshExportStatisticsPdmarticle;

    @FindBy(
            xpath = "//input[contains(@id,'Optionproxyexportstaging:cassandraConnectionUrl')]")
    private WebElement txtCassandraConnectionUrl;

    @FindBy(
            xpath = "//input[contains(@id,'Optionproxyexportstaging:elasticsearchAddress')]")
    private WebElement txtElasticSearchAddress;

    @FindBy(xpath = "//input[@id='ESAInitialExport']")
    private WebElement btnESAInitialExport;

    public ElasticSearchPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Settings node page POM elements");
    }
    
    /**
     * Returns the WebElement node ESA
     * 
     * @return nodeESA
     */
    public WebElement getNodeESA() {
        return nodeESA;
    }

    /**
     * Returns the WebElement node ExportFramework
     * 
     * @return nodeExportFramework
     */
    public WebElement getNodeExportFramework() {
        return nodeExportFramework;
    }
    
    /**
     * Returns the WebElement node Cassandra Details
     * 
     * @return nodeCassandraDetails
     */
    public WebElement getNodeCassandraDetails() {
        return nodeCassandraDetails;
    }
    
    /**
     * Returns the WebElement node Elastic Search Details
     * 
     * @return nodeElasticSearchDetails
     */
    public WebElement getNodeElasticSearchDetails() {
        return nodeElasticSearchDetails;
    }
    
    /**
     * Returns the WebElement node Core Details
     * 
     * @return nodeCoreDetails
     */
    public WebElement getNodeCoreDetails() {
        return nodeCoreDetails;
    }
    
    /**
     * Returns the WebElement Button Master Producer
     * 
     * @return btnMasterProducer
     */
    public WebElement getBtnMasterProducer() {
        return btnMasterProducer;
    }
    
    /**
     * Returns the WebElement Button Master Subscriber
     * 
     * @return btnMasterSubscriber
     */
    public WebElement getBtnMasterSubscriber() {
        return btnMasterSubscriber;
    }
    
    /**
     * Returns the WebElement Button Elastic Subscriber
     * 
     * @return btnElasticSubscriber
     */
    public WebElement getBtnElasticSubscriber() {
        return btnElasticSubscriber;
    }
    
    /**
     * Returns the WebElement Button WebSocket Subscriber
     * 
     * @return btnWebSocketSubscriber
     */
    public WebElement getBtnWebSocketSubscriber() {
        return btnWebSocketSubscriber;
    }
    
    /**
     * Returns the WebElement Button ElasticSearch Enabled
     * 
     * @return btnElasticSearchEnabled
     */
    public WebElement getBtnElasticSearchEnabled() {
        return btnElasticSearchEnabled;
    }
    
    /**
     * Returns the WebElement text Status CassandraRUNNING
     * 
     * @return txtStatusCassandraRUNNING
     */
    public WebElement gettxtStatusCassandraRUNNING() {
        return txtStatusCassandraRUNNING;
    }
    
    /**
     * Returns the WebElement text Status ElasticSearch RUNNING
     * 
     * @return txtStatusElasticSearchRUNNING
     */
    public WebElement gettxtStatusElasticSearchRUNNING() {
        return txtStatusElasticSearchRUNNING;
    }

    /**
     * Returns the WebElement node Export Executer Details
     * 
     * @return nodeExportExecuterDetails
     */
    public WebElement getNodeExportExecuterDetails() {
        return nodeExportExecuterDetails;
    }

    /**
     * Returns the WebElement of Export Executor Logs
     * 
     * @return txtExportExecutorLogs
     */
    public WebElement getTxtExportExecutorLogs() {
        return txtExportExecutorLogs;
    }

    /**
     * Returns the WebElement Button Start Export Executor
     * 
     * @return btnStartExportExecutor
     */
    public WebElement getBtnStartExportExecutor() {
        return btnStartExportExecutor;
    }

    /**
     * Returns the WebElement Button Refresh Export Executor Logs
     * 
     * @return btnRefreshExportExecutorLogs
     */
    public WebElement getBtnRefreshExportExecutorLogs() {
        return btnRefreshExportExecutorLogs;
    }

    /**
     * Returns the WebElement instance of Elasticsearch Count Mamfile
     * 
     * @return txtElasticsearchCountMamfile
     */
    public WebElement getTxtElasticsearchCountMamfile() {
        return txtElasticsearchCountMamfile;
    }

    /**
     * Returns the WebElement instance of Contentserv Count Mamfile
     * 
     * @return txtContentservCountMamfile
     */
    public WebElement getTxtContentservCountMamfile() {
        return txtContentservCountMamfile;
    }

    /**
     * Returns the WebElement instance of Elasticsearch Count 
     * Pdmarticlestructure
     * 
     * @return txtElasticsearchCountPdmarticlestructure
     */
    public WebElement getTxtElasticsearchCountPdmarticlestructure() {
        return txtElasticsearchCountPdmarticlestructure;
    }

    /**
     * Returns the WebElement instance of Contentserv Count Pdmarticlestructuree
     * 
     * @return txtContentservCountPdmarticlestructure
     */
    public WebElement getTxtContentservCountPdmarticlestructure() {
        return txtContentservCountPdmarticlestructure;
    }

    /**
     * Returns the WebElement instance of Elasticsearch Count Pdmarticle
     * 
     * @return txtElasticsearchCountPdmarticle
     */
    public WebElement getTxtElasticsearchCountPdmarticle() {
        return txtElasticsearchCountPdmarticle;
    }

    /**
     * Returns the WebElement instance of Contentserv Count Pdmarticle
     * 
     * @return txtContentservCountPdmarticle
     */
    public WebElement getTxtContentservCountPdmarticle() {
        return txtContentservCountPdmarticle;
    }

    /**
     * Returns the WebElement instance of Cassandra Count Workflow
     * 
     * @return txtCassandraCountWorkflow
     */
    public WebElement getTxtCassandraCountWorkflow() {
        return txtCassandraCountWorkflow;
    }

    /**
     * Returns the WebElement node Export Statistics
     * 
     * @return nodeExportStatistics
     */
    public WebElement getNodeExportStatistics() {
        return nodeExportStatistics;
    }

    /**
     * Returns the WebElement instance of Elasticsearch Count User
     * 
     * @return txtElasticsearchCountUser
     */
    public WebElement getTxtElasticsearchCountUser() {
        return txtElasticsearchCountUser;
    }

    /**
     * Returns the WebElement instance of Contentserv Count User
     * 
     * @return txtContentservCountUser
     */
    public WebElement getTxtContentservCountUser() {
        return txtContentservCountUser;
    }

    /**
     * Returns the WebElement instance of Contentserv Count Workflow
     * 
     * @return txtContentservCountWorkflow
     */
    public WebElement getTxtContentservCountWorkflow() {
        return txtContentservCountWorkflow;
    }

    /**
     * Returns the WebElement instance of Cassandra Count Language
     * 
     * @return txtCassandraCountLanguage
     */
    public WebElement getTxtCassandraCountLanguage() {
        return txtCassandraCountLanguage;
    }

    /**
     * Returns the WebElement instance of Contentserv Count Language
     * 
     * @return txtContentservCountLanguage
     */
    public WebElement getTxtContentservCountLanguage() {
        return txtContentservCountLanguage;
    }

    /**
     * Returns the WebElement button Refresh all
     * 
     * @return btnRefreshAll
     */
    public WebElement getBtnRefreshAll() {
        return btnRefreshAll;
    }

    /**
     * Returns the WebElement button Refresh Export Statistics Pdmarticle
     * 
     * @return btnRefreshExportStatisticsPdmarticle
     */
    public WebElement getBtnRefreshExportStatisticsPdmarticle() {
        return btnRefreshExportStatisticsPdmarticle;
    }
    
    /**
     * Returns the WebElement text Elastic Search Address
     * 
     * @return txtElasticSearchAddress
     */
    public WebElement getTxtElasticSearchAddress() {
        return txtElasticSearchAddress;
    }

    /**
     * Returns the WebElement text Cassandra ConnectionUrl
     * 
     * @return txtCassandraConnectionUrl
     */
    public WebElement getTxtCassandraConnectionUrl() {
        return txtCassandraConnectionUrl;
    }

    /**
     * Returns the WebElement button ESA Initial Export
     * 
     * @return btnESAInitialExport
     */
    public WebElement getBtnESAInitialExport() {
        return btnESAInitialExport;
    }

    /**
     * Clicks on right side ESA node from setting node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnNodeESA(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getNodeESA());
        getNodeESA().click();
        CSLogger.info("Clicked on setting node's ESA tree");
    }

    /**
     * Returns the WebElement Button ElasticSearch Enabled
     * 
     * @return chkBoxElasticSearchEnabled
     */
    public WebElement getChkBoxElasticSearchEnabled() {
        return chkBoxElasticSearchEnabled;
    }

    /**
     * Clicks on right side Export Framework node from setting node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnNodeExportFramework(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getNodeExportFramework());
        getNodeExportFramework().click();
        CSLogger.info("Clicked on setting node's Export Framework tree");
    }
    
    /**
     * Clicks on right side Core Details node from setting node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnNodeCoreDetails(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getNodeCoreDetails());
        getNodeCoreDetails().click();
        CSLogger.info("Clicked on node Core Details");
    }
    
    /**
     * Clicks on right side Core Details node from setting node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnNodeCassandraDetails(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getNodeCassandraDetails());
        getNodeCassandraDetails().click();
        CSLogger.info("Clicked on node Cassandra Details");
    }
    
    /**
     * Clicks on right side Core Details node from setting node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnNodeElasticSearchDetails(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getNodeElasticSearchDetails());
        getNodeElasticSearchDetails().click();
        CSLogger.info("Clicked on node Elastic Search Details");
    }

    /**
     * This generic method will click on any web element passed as an argument
     * 
     * @param locator - WebElement instance - locator of element on which we
     *            want to perform click action
     */
    public void clkWebElement(WebDriverWait waitForReload, WebElement locator) {
        CSUtility.waitForVisibilityOfElement(waitForReload, locator);
        locator.click();
    }
}
