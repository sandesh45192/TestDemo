/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import freemarker.cache.CacheStorageWithGetSize;

/**
 * This class contains WebElements and methods of 'Active Jobs' page.
 * 
 * @author CSAutomation Team
 *
 */
public class ActiveJobsPage {

    private WebDriver  browserDriverInstance;

    @FindBy(
            xpath = "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr/td[4]/a/img")
    private WebElement btnShowMoreOption;

    @FindBy(
            xpath = "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr/td[2]/a/img")
    private WebElement btnCreateNewActiveJob;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptLabel')]")
    private WebElement txtNewActiveScriptLabel;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptCategory')]")
    private WebElement txtNewActiveScriptCategory;

    @FindBy(
            xpath = "//select[contains(@name,'ActivescriptActiveScriptPlugin')]")
    private WebElement drpDwnSelectActiveScript;

    @FindBy(xpath = "//option[@value='housekeepingscript']")
    private WebElement optionHouseKeepingScript;

    @FindBy(
            xpath = "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr/td[1]")
    private WebElement btnSaveActiveJob;

    @FindBy(
            xpath = "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr/td[3]")
    private WebElement btnRunActiveScript;

    @FindBy(xpath = "//div[@id='title__sections::Delete old Logs']/div")
    private WebElement drpDwnDeleteOldLogs;

    @FindBy(
            xpath = "//div[@id='title__sections::e0b274349bf61eb0bac461254fc60c51']/div")
    private WebElement drpDwnDeleteOldCacheFiles;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanLog')]")
    private WebElement drpDwnSystemLog;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanMonitor')]")
    private WebElement drpDwnStatusMonitorLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanActivescriptlog')]")
    private WebElement drpDwnActiveScriptLog;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanRestLog')]")
    private WebElement drpDwnRestLog;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanCometLog')]")
    private WebElement drpDwnCometLog;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanWsdlLog')]")
    private WebElement drpDwnWsdlLog;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanSmartLog')]")
    private WebElement drpDwnSmartDocumentlLog;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanUserSessionLog')]")
    private WebElement drpDwnUserSessionVisitlLog;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanDeletionLog')]")
    private WebElement drpDwnDeletionlLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanScriptperformance')]")
    private WebElement drpDwnScriptPerformanceLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanQueryperformance')]")
    private WebElement drpDwnQueryPerformanceLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanMamfiledownloadlogentry')]")
    private WebElement drpDwnFileDownloadLog;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanUserlog')]")
    private WebElement drpDwnUserLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanActivescriptjob')]")
    private WebElement drpDwnActiveScriptJob;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanCaches')]")
    private WebElement drpDwnCacheFiles;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanLog')]/option[4]")
    private WebElement timeDurationSystemLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanMonitor')]/option[4]")
    private WebElement timeDurationStatusMonitorLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanActivescriptlog')]/option[4]")
    private WebElement timeDurationActiveScriptLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanRestLog')]/option[4]")
    private WebElement timeDurationRestLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanCometLog')]/option[4]")
    private WebElement timeDurationCometLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanWsdlLog')]/option[4]")
    private WebElement timeDurationWsdlLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanSmartLog')]/option[4]")
    private WebElement timeDurationSmartDocumentLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanUserSessionLog')]/option[4]")
    private WebElement timeDurationUserSessionVisitLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanDeletionLog')]/option[4]")
    private WebElement timeDurationDeletionLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanScriptperformance')]/option[4]")
    private WebElement timeDurationScriptPerformanceLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanQueryperformance')]/option[4]")
    private WebElement timeDurationQueryPerformanceLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanMamfiledownloadlogentry')]/option[4]")
    private WebElement timeDurationFileDownloadLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanUserlog')]/option[4]")
    private WebElement timeDurationUserLog;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanActivescriptjob')]/option[4]")
    private WebElement timeDurationActiveScriptJob;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanCaches')]/option[4]")
    private WebElement timeDurationCacheFiles;

    @FindBy(xpath = "//div[contains(text(),'Job completed')]")
    private WebElement scriptRunStatus;

    @FindBy(
            xpath = "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr/td[1]/a/img")
    private WebElement btnRefreshJobsStatus;

    @FindBy(xpath = "//img[contains(@src,'red.circle.cross.png')]")
    private WebElement imgFailedActiveScriptRun;

    @FindBy(xpath = "//nobr[contains(text(),'Jobs')]")
    private WebElement btnJobs;

    @FindBy(
            xpath = "//div[@id='title__sections::646b47a6474aca0363ce1183fba665b7']")
    private WebElement drpDwnForgottenCheckins;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCheckinPim')]")
    private WebElement drpDwnCheckinPimProducts;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCheckinViews')]")
    private WebElement drpDwnCheckinPimViews;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCheckinMam')]")
    private WebElement drpDwnCheckinMamFiles;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCheckinPim')]/option[2]")
    private WebElement timeDurationForCheckinPimProducts;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCheckinViews')]/option[2]")
    private WebElement timeDurationForCheckinPimViews;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCheckinMam')]/option[2]")
    private WebElement timeDurationForCheckinMamFiles;

    @FindBy(xpath = "//div[@id='title__sections::Backup']")
    private WebElement drpDwnBackup;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCleanBackup')]")
    private WebElement drpDwnBackupFiles;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptCleanBackup')]/option[5]")
    private WebElement timeDurationForBackupFiles;

    @FindBy(xpath = "//nobr[contains(text(),'Properties')]")
    private WebElement btnProperties;

    @FindBy(xpath = "//option[@value='helloworldscript']")
    private WebElement optionSayHelloScript;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptCount')]")
    private WebElement txtSayHelloCount;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptSay')]")
    private WebElement txtWhatToSay;

    @FindBy(xpath = "//td[contains(text(),'#10')]")
    private WebElement sayHelloStatus;

    @FindBy(xpath = "//img[contains(@src,'check.svg')]")
    private WebElement imgGreenTickForSuccessfulScriptRun;

    @FindBy(xpath = "//td[@class='splitareacontentcenter']")
    private WebElement splitAreaContentCenter;

    @FindBy(id = "ActiveJobs@0")
    private WebElement nodeActiveJobs;

    @FindBy(id = "ActiveJobs~Presets@0")
    private WebElement btnPresets;

    @FindBy(id = "Currencies@0")
    private WebElement nodeCurrencies;

    @FindBy(xpath = "//span[contains(text(),'System Preferences')]")
    private WebElement systemPreferences;

    @FindBy(id = "PauseButton")
    private WebElement btnPauseScriptRun;

    @FindBy(id = "StopButton")
    private WebElement btnInterruptionScriptRun;

    @FindBy(xpath = "//nobr[contains(text(),'Report')]")
    private WebElement btnReport;

    @FindBy(xpath = "//nobr[contains(text(),'Script')]")
    private WebElement btnScript;

    @FindBy(xpath = "//td[contains(text(),'Pause requested')]")
    private WebElement txtMsgPauseRequested;

    @FindBy(xpath = "//td[contains(text(),'Interruption requested')]")
    private WebElement txtMsgInterruptionRequested;

    @FindBy(xpath = "//td[contains(text(),'Interruption accepted')]")
    private WebElement txtMsgInterruptionAccepted;

    @FindBy(
            xpath = "//div[@id='CSGuiListbuilderTable']/table/tbody/tr[3]/td[2]/div/img")
    private WebElement imgStateIcon;

    @FindBy(
            xpath = "//div[@id='CSGuiListbuilderTable']/table/tbody/tr[3]/td[3]")
    private WebElement jobId;

    @FindBy(xpath = "//a[@id='filename']")
    private WebElement phpScript;

    @FindBy(xpath = "//select[contains(@id,'massUpdateSelector')]")
    private WebElement btnMarked;

    @FindBy(xpath = "//option[@value ='deletemarked']")
    private WebElement optionMarkedDelete;

    @FindBy(xpath = "//option[@value = 'clonemarked']")
    private WebElement optionDuplicate;

    @FindBy(xpath = "//input[@id='CSBuilderMarkAllActionTop']")
    private WebElement selectAllChkBox;

    @FindBy(xpath = "//select[@id='massUpdateSelector']")
    private WebElement massUpdateSelector;

    @FindBy(xpath = "//td[contains(text(),'Export as CSV File')]")
    private WebElement btnExportCSV;

    @FindBy(xpath = "//td[contains(text(),'Export as Excel File')]")
    private WebElement btnExportExcel;

    @FindBy(
            xpath = "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr/td[3]")
    private WebElement btnDeleteActiveScript;

    @FindBy(id = "_sections::Data Source")
    private WebElement secDataSource;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptExportExtractionType')]")
    private WebElement drpDwnDataSourceType;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptDataSourceType')]")
    private WebElement drpDwnDataSelectionType;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptContextIDs__Label')]")
    private WebElement ctnSelectRootFolders;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptIncludeLevels')]")
    private WebElement drpDwnSelectionLayer;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptDefaultLanguageID')]")
    private WebElement drpDwnSelectDefaultLanguage;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptStartCount')]")
    private WebElement txtStartCount;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptMaxCount')]")
    private WebElement txtMaxCount;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptBatchSize')]")
    private WebElement txtBatchSize;

    @FindBy(id = "_sections::Data Target")
    private WebElement secDataTarget;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptExportLoaderType')]")
    private WebElement drpDwnDataTargetType;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptFilename')]")
    private WebElement txtDataTargetFileName;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptCsvDelimiter')]")
    private WebElement txtDataTargetDelimiter;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptCsvEnclosure')]")
    private WebElement txtDataTargetCSVEncloser;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptCsvEncoding')]")
    private WebElement drpDwnDataTargetCSVEncoding;

    @FindBy(xpath = "//div[@id='_sections']/div[3]")
    private WebElement secDataTransformation;

    @FindBy(
            xpath = "//span[contains(@id,'ActivescriptHasExportTransformations_GUI')]")
    private WebElement cbUseTransformationEditor;

    @FindBy(
            xpath = "//span[contains(@id,'ActivescriptExportTransformationsEditor')]/input")
    private WebElement btnEditTransformation;

    @FindBy(xpath = "//span[contains(@id,'ActivescriptContextSensitive_GUI')]")
    private WebElement cbContextSensitive;

    @FindBy(xpath = "//span[contains(@name,'ActivescriptHelp')]")
    private WebElement lblOnlineHelpLink;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptImportExtractionType')]")
    private WebElement drpDwnImportDataSourceType;

    @FindBy(
            xpath = "//div[contains(@id,'ActivescriptFileID_csReferenceDiv')]/div[2]/img")
    private WebElement btnDataSourceFile;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptHeaderLine')]")
    private WebElement txtHeaderLineNumber;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptContentLine')]")
    private WebElement txtFirstContentLineNumber;

    @FindBy(xpath = "//span[contains(@id,'ActivescriptInfo')]")
    private WebElement lblScriptInfo;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptDelimiter')]")
    private WebElement txtActiveScriptDelimiter;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptEnclosure')]")
    private WebElement txtActivescriptEnclosure;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptEncoding')]")
    private WebElement drpDwnActiveScriptEncoding;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptExternalKeyField')]")
    private WebElement drpDwnDataSourceID;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptImportLoaderType')]")
    private WebElement drpDwnImportDataTargetType;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptMode')]")
    private WebElement drpDwnActiveScriptMode;

    @FindBy(
            xpath = "//select[contains(@id,'ActivescriptInternalKeyFieldType')]")
    private WebElement drpDwnDataTargetId;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptContextIDs__Label')]")
    private WebElement ctnImportTargetFolder;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptStartRowNumber')]")
    private WebElement txtActiveScriptStartRowNumber;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptMaxRows')]")
    private WebElement txtActiveScriptMaxRows;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptMultiThreads')]")
    private WebElement txtActiveScriptMultiThreads;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptLimitFilter')]")
    private WebElement txtActivescriptLimitFilter;

    @FindBy(xpath = "//span[contains(@id,'ActivescriptTransformationsEditor')]")
    private WebElement btnImportEditTransformation;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptFileID__Label')]")
    private WebElement ctnDataSourceFile;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptObjectSelector')]")
    private WebElement txtXmlObjectSelector;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptSleepAfterEachStep')]")
    private WebElement drpDwnActivescriptSleepAfterEachStep;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptTemplateID__Label')]")
    private WebElement btnTemplateFile;

    @FindBy(xpath = "//select[contains(@id,'ActivescriptFileGeneration')]")
    private WebElement drpDwnActivescriptFileGeneration;

    @FindBy(xpath = "//input[contains(@id,'ActivescriptWorksheet')]")
    private WebElement txtWorksheet;

    @FindBy(xpath = "//span[contains(@id,'ActivescriptAddHeadline_GUI')]")
    private WebElement cbAddHeadline;

    /**
     * Parameterized constructor. This method sets the browser driver instance
     * for this page.
     * 
     * @param paramDriver
     */
    public ActiveJobsPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized SettingsLeftPaneTree page POM elements.");
        new FrameLocators(browserDriverInstance);
    }

    /**
     * Returns the element for creating a new active job
     * 
     * @return - create new article icon
     */
    public WebElement getBtnCreateNewActiveJob() {
        return btnCreateNewActiveJob;
    }

    /**
     * Returns the element for show more options from edit pane
     * 
     * @return - create new article icon
     */
    public WebElement getbtnShowMoreOption() {
        return btnShowMoreOption;
    }

    /**
     * Returns the element showing the progress status of running active job
     * 
     * @return - script run status symbol - pass OR fail
     */
    public WebElement getScriptRunStatus() {
        return scriptRunStatus;
    }

    /**
     * Returns the element for 'refresh' button under 'Jobs' option, in running
     * script
     * 
     * @return - refresh icon
     */
    public WebElement getBtnRefreshJobsStatus() {
        return btnRefreshJobsStatus;
    }

    /**
     * Returns the element for 'Jobs' option, in running script
     * 
     * @return - 'Jobs' tab for selected active job
     */
    public WebElement getBtnJobs() {
        return btnJobs;
    }

    /**
     * Returns the element of image, for failed active script run
     * 
     * @return - symbol for failed active script run
     */
    public WebElement getImgFailedActiveScriptRun() {
        return imgFailedActiveScriptRun;
    }

    /**
     * Returns the element for drop down list for 'delete old logs' option
     * 
     * @return - dropdown for 'Delete Old Logs'
     */
    public WebElement getDrpDwnDeleteOldLogs() {
        return drpDwnDeleteOldLogs;
    }

    /**
     * Returns the element for drop down list for 'delete old cache files'
     * option
     * 
     * @return - dropdown for 'Delete Old Cache Files'
     */
    public WebElement getDrpDwnDeleteOldCacheFiles() {
        return drpDwnDeleteOldCacheFiles;
    }

    /**
     * Returns the element for drop down list for 'System log' option
     * 
     * @return - dropdown for 'System Log'
     */
    public WebElement getDrpDwnSystemLog() {
        return drpDwnSystemLog;
    }

    /**
     * Returns the element for drop down list for 'StatusMonitorLog' option
     * 
     * @return - dropdown for 'Status Monitor Log'
     */
    public WebElement getDrpDwnStatusMonitorLog() {
        return drpDwnStatusMonitorLog;
    }

    /**
     * Returns the element for drop down list for 'ActiveScriptLog' option
     * 
     * @return - dropdown for 'Active Script Log'
     */
    public WebElement getDrpDwnActiveScriptLog() {
        return drpDwnActiveScriptLog;
    }

    /**
     * Returns the element for drop down list for 'RestLog' option
     * 
     * @return - dropdown for 'Rest Log'
     */
    public WebElement getDrpDwnRestLog() {
        return drpDwnRestLog;
    }

    /**
     * Returns the element for drop down list for 'CometLog' option
     * 
     * @return - dropdown for 'Comet Log'
     */
    public WebElement getDrpDwnCometLog() {
        return drpDwnCometLog;
    }

    /**
     * Returns the element for drop down list for 'WsdlLog' option
     * 
     * @return - dropdown for 'Wsdl Log'
     */
    public WebElement getDrpDwnWsdlLog() {
        return drpDwnWsdlLog;
    }

    /**
     * Returns the element for drop down list for 'Smart Document Log' option
     * 
     * @return - dropdown for 'Smart Document Log'
     */
    public WebElement getDrpDwnSmartDocumentLog() {
        return drpDwnSmartDocumentlLog;
    }

    /**
     * Returns the element for drop down list for 'UserSessionVisitLog' option
     * 
     * @return - dropdown for 'User Session Visit Log'
     */
    public WebElement getDrpDwnUserSessionVisitLog() {
        return drpDwnUserSessionVisitlLog;
    }

    /**
     * Returns the element for drop down list for 'DeletionLog' option
     * 
     * @return - dropdown for 'Deletion Log'
     */
    public WebElement getDrpDwnDeletionLog() {
        return drpDwnDeletionlLog;
    }

    /**
     * Returns the element for drop down list for 'ScriptPerformanceLog' option
     * 
     * @return - dropdown for 'Script Performance Log'
     */
    public WebElement getDrpDwnScriptPerformanceLog() {
        return drpDwnScriptPerformanceLog;
    }

    /**
     * Returns the element for drop down list for 'QueryPerformanceLog' option
     * 
     * @return - dropdown for 'Query Performance Log'
     */
    public WebElement getDrpDwnQueryPerformanceLog() {
        return drpDwnQueryPerformanceLog;
    }

    /**
     * Returns the element for drop down list for 'FileDownloadLog' option
     * 
     * @return - dropdown for 'File Download Log'
     */
    public WebElement getDrpDwnFileDownloadLog() {
        return drpDwnFileDownloadLog;
    }

    /**
     * Returns the element for drop down list for 'UserLog' option
     * 
     * @return - dropdown for 'User Log'
     */
    public WebElement getDrpDwnUserLog() {
        return drpDwnUserLog;
    }

    /**
     * Returns the element for drop down list for 'ActiveScriptJob' option
     * 
     * @return - dropdown for 'Active Script Job'
     */
    public WebElement getDrpDwnActiveScriptJob() {
        return drpDwnActiveScriptJob;
    }

    /**
     * Returns the element for drop down list for 'CacheFiles' option
     * 
     * @return - dropdown for 'Cache Files'
     */
    public WebElement getDrpDwnCacheFiles() {
        return drpDwnCacheFiles;
    }

    /**
     * Returns the element for 'saving' a new active job
     * 
     * @return - Save button, for selected active job
     */
    public WebElement getBtnSaveActiveJob() {
        return btnSaveActiveJob;
    }

    /**
     * Returns the element for 'run active script' play button
     * 
     * @return - Run button for selected active job
     */
    public WebElement getBtnRunActiveScript() {
        return btnRunActiveScript;
    }

    /**
     * Returns the element for locating 'houseKeepingScript' from script
     * dropdown list
     * 
     * @return - option 'House Keeping Script' from script dropdown list, for
     *         selected active job
     */
    public WebElement getOptionHouseKeepingScript() {
        return optionHouseKeepingScript;
    }

    /**
     * Returns the text box for entering the label for new active script
     * 
     * @return - Label for newly created active script
     */
    public WebElement getTxtNewActiveScriptLabel() {
        return txtNewActiveScriptLabel;
    }

    /**
     * Returns the text box for entering the category for new active script
     * 
     * @return - category for newly created active script
     */
    public WebElement getTxtNewActiveScriptCategory() {
        return txtNewActiveScriptCategory;
    }

    /**
     * Returns the element for dropdown list, for selecting the active script,
     * to be assigned to the active job
     * 
     * @return - dropdown for selecting the active script
     */
    public WebElement getDrpDwnSelectActiveScript() {
        return drpDwnSelectActiveScript;
    }

    /**
     * 
     * @return - webelement for time duration for system log ======= All the
     *         methods below will click on time duration for their particular
     *         logs
     */

    /**
     * 
     * @return - time duration for system log
     */
    public WebElement getTimeDurationForSystemLog() {
        return timeDurationSystemLog;
    }

    /**
     * 
     * @return - time duration for status monitor log
     */
    public WebElement getTimeDurationForStatusMonitorLog() {
        return timeDurationStatusMonitorLog;
    }

    /**
     * @return - webelement for time duration for active script log
     */
    public WebElement getTimeDurationForActiveScriptLog() {
        return timeDurationActiveScriptLog;
    }

    /**
     * @return - webelement for time duration for rest log
     */
    public WebElement getTimeDurationForRestLog() {
        return timeDurationRestLog;
    }

    /**
     * @return - webelement for time duration for comet log
     */
    public WebElement getTimeDurationForCometLog() {
        return timeDurationCometLog;
    }

    /**
     * @return - webelement for time duration for Wsdl log
     */
    public WebElement getTimeDurationForWsdlLog() {
        return timeDurationWsdlLog;
    }

    /**
     * @return - webelement for time duration for Smart Document log
     */
    public WebElement getTimeDurationForSmartDocumentlLog() {
        return timeDurationSmartDocumentLog;
    }

    /**
     * @return - webelement for time duration for User Session Visit log
     */
    public WebElement getTimeDurationForUserSessionVisitLog() {
        return timeDurationUserSessionVisitLog;
    }

    /**
     * @return - webelement for time duration for Deletion log
     */
    public WebElement getTimeDurationForDeletionLog() {
        return timeDurationDeletionLog;
    }

    /**
     * @return - webelement for time duration for Script Performance log
     */
    public WebElement getTimeDurationForScriptPerformanceLog() {
        return timeDurationScriptPerformanceLog;
    }

    /**
     * @return - webelement for time duration for Query Performance log
     */
    public WebElement getTimeDurationForQueryPerformanceLog() {
        return timeDurationQueryPerformanceLog;
    }

    /**
     * @return - webelement for time duration for File Download log
     */
    public WebElement getTimeDurationForFileDownloadLog() {
        return timeDurationFileDownloadLog;
    }

    /**
     * @return - webelement for time duration for User log
     */
    public WebElement getTimeDurationForUserLog() {
        return timeDurationUserLog;
    }

    /**
     * @return - webelement for time duration for Active Script Job
     */
    public WebElement getTimeDurationForActiveScriptJob() {
        return timeDurationActiveScriptJob;
    }

    /**
     * @return - webelement for time duration for Cache Files
     */
    public WebElement getTimeDurationForCacheFiles() {
        return timeDurationCacheFiles;
    }

    /**
     * Returns the element for drop down list for 'Forgotten Checkin' option
     * 
     * @return - dropdown for forgotten checkins
     */
    public WebElement getDrpDwnForgottenCheckins() {
        return drpDwnForgottenCheckins;
    }

    /**
     * Returns the element for drop down list for 'Checkin PIM Products' option
     * 
     * @return - dropdown for Checkin PIM products
     */
    public WebElement getDrpDwnCheckinPimProducts() {
        return drpDwnCheckinPimProducts;
    }

    /**
     * This method will return the time duration for 'Checkin PIM Products'
     * dropdown list
     * 
     * @return - timeDurationForCheckinPimProducts
     */
    public WebElement getTimeDurationForCheckinPimProducts() {
        return timeDurationForCheckinPimProducts;
    }

    /**
     * Returns the element for drop down list for 'Checkin PIM Views' option
     * 
     * @return - dropdown for Checkin PIM views
     */
    public WebElement getDrpDwnCheckinPimViews() {
        return drpDwnCheckinPimViews;
    }

    /**
     * This method will return the duration for 'Checkin PIM Views' dropdown
     * list
     * 
     * @return - timeDurationForCheckinPimViews
     */
    public WebElement getTimeDurationForCheckinPimViews() {
        return timeDurationForCheckinPimViews;
    }

    /**
     * Returns the element for drop down list for 'Checkin MAM Files' option
     * 
     * @return - dropdown for Checkin MAM files
     */
    public WebElement getDrpDwnCheckinMamFiles() {
        return drpDwnCheckinMamFiles;
    }

    /**
     * This method will return the time duration for 'Checkin MAM Files'
     * dropdown list
     * 
     * @return - timeDurationForCheckinMamFiles
     */
    public WebElement getTimeDurationForCheckinMamFiles() {
        return timeDurationForCheckinMamFiles;
    }

    /**
     * Returns the element for drop down list for 'Backup' option
     * 
     * @return - dropdown for Backup
     */
    public WebElement getDrpDwnBackup() {
        return drpDwnBackup;
    }

    /**
     * Returns the element for drop down list for 'Backup Files' option
     * 
     * @return - dropdown for Backup files
     */
    public WebElement getDrpDwnBackupFiles() {
        return drpDwnBackupFiles;
    }

    /**
     * This method will return the time duration for 'Backup Files' dropdown
     * list
     * 
     * @return - timeDurationForBackupFiles
     */
    public WebElement getTimeDurationForBackupFiles() {
        return timeDurationForBackupFiles;
    }

    /**
     * Returns the element for 'Properties' option, in running script
     * 
     * @return - properties tab for selected active job
     */
    public WebElement getBtnProperties() {
        return btnProperties;
    }

    /**
     * Returns the element for locating 'Say Hello' from script dropdown list
     * 
     * @return - sayHelloScript option from script dropdown menu
     */
    public WebElement getOptionSayHelloScript() {
        return optionSayHelloScript;
    }

    /**
     * Returns the text box for entering the 'count' for Say Hello script
     * 
     * @return - text box for entering count for sayHelloScript
     */
    public WebElement getTxtCountForSayHelloScript() {
        return txtSayHelloCount;
    }

    /**
     * Returns the text box for entering 'What to say' for Say Hello script
     * 
     * @return - text box for whatToSay option
     */
    public WebElement getTxtWhatToSay() {
        return txtWhatToSay;
    }

    /**
     * Returns the element showing the progress status of running 'Say Hello'
     * script
     * 
     * @return - status of script run for say hello active script
     */
    public WebElement getSayHelloScriptRunStatus() {
        return sayHelloStatus;
    }

    /**
     * Returns the element of image for green tick, indicating successful script
     * run
     * 
     * @return - green tick image, indicating successful active script run
     */
    public WebElement getImgSuccessfulScriptRun() {
        return imgGreenTickForSuccessfulScriptRun;
    }

    /**
     * Returns the element for split area content center
     * 
     * @return - splitAreaContentCenter
     */
    public WebElement getSplitAreaContentCenter() {
        return splitAreaContentCenter;
    }

    /**
     * This method returns the element for activeJobs drop-down list
     * 
     * @return - ActiveJobs node
     */
    public WebElement getNodeActiveJobs() {
        return nodeActiveJobs;
    }

    /**
     * This method returns the 'Presets' element from activeJobs drop-down list
     * 
     * @return - Presets option in active jobs node
     */
    public WebElement getBtnPresets() {
        return btnPresets;
    }

    /**
     * This method returns the element for 'Currencies' option <<<<<<< Updated
     * upstream
     * 
     * @return - currencies node
     */
    public WebElement getNodeCurrencies() {
        return nodeCurrencies;
    }

    /**
     * Returns the element for system preferences
     * 
     * @return - system preferences menu
     */
    public WebElement getSystemPreferences() {
        return systemPreferences;
    }

    /**
     * This method returns the element for 'Pause' option for a running script
     * 
     * @return - Pause button, to pause the currently running active script
     */
    public WebElement getBtnPauseScriptRun() {
        return btnPauseScriptRun;
    }

    /**
     * This method returns the element for 'Interruption' option for a running
     * script
     * 
     * @return - Interruption button, to stop a running script
     */
    public WebElement getBtnInterruption() {
        return btnInterruptionScriptRun;
    }

    /**
     * This generic method will click on any webelement passed as an argument
     * 
     * @param locator
     */
    public void clkWebElement(WebElement locator) {
        locator.click();
    }

    /**
     * Returns the element for 'Report' option, in running script
     * 
     * @return - 'Report' tab for selected active job
     */
    public WebElement getBtnReport() {
        return btnReport;
    }

    /**
     * Returns the element for 'Script' option, in running script
     * 
     * @return - 'Script' tab for selected active job
     */
    public WebElement getBtnScript() {
        return btnScript;
    }

    /**
     * Returns the script pause message
     * 
     * @return - message 'Pause Requested'
     */
    public WebElement getTxtMsgPauseRequested() {
        return txtMsgPauseRequested;
    }

    /**
     * Returns the script interruption message
     * 
     * @return - message 'Interruption Requested'
     */
    public WebElement getTxtMsgInterruptionRequested() {
        return txtMsgInterruptionRequested;
    }

    /**
     * Returns the script interruption message
     * 
     * @return - message 'Interruption Requested'
     */
    public WebElement getTxtMsgInterruptionAccepted() {
        return txtMsgInterruptionAccepted;
    }

    /**
     * Returns the element for 'Marked' option, after selected jobs
     * 
     * @return - 'Marked' Drop down for selected jobs
     */
    public WebElement getbtnMarked() {
        return btnMarked;
    }

    /**
     * Returns the element for 'Delete' Active script in menu bar
     * 
     * @return - 'Delete' option for Active script
     */
    public WebElement getbtnDeleteActiveScript() {
        return btnDeleteActiveScript;
    }

    /**
     * Returns the element for 'Delete' option, in'Marked' Drop down list
     * 
     * @return - 'Delete' option for selected active job
     */
    public WebElement getoptionMarkedDelete() {
        return optionMarkedDelete;
    }

    /**
     * Returns the element for 'Duplicate' option, in'Marked' Drop down list
     * 
     * @return - 'Duplicate' option for selected active job
     */
    public WebElement getoptionDuplicate() {
        return optionDuplicate;
    }

    /**
     * Returns the icon for state of latest ran script
     * 
     * @return - imgStateIcon
     */
    public WebElement getImgStateIcon() {
        return imgStateIcon;
    }

    /**
     * Returns job ID to uniquely identify each active job script run
     * 
     * @return - jobId
     */
    public WebElement getJobId() {
        return jobId;
    }

    /**
     * Returns php script locator for active job
     * 
     * @return - phpScript
     */
    public WebElement getPhpScript() {
        return phpScript;
    }

    /**
     * This method will 'run' the active script.
     */
    public void runTheActiveScript(WebDriverWait waitForReload,
            WebDriver browserDriver) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnRunActiveScript());
        clkWebElement(getBtnRunActiveScript());
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
    }

    /**
     * This method will 'save' the active script
     * 
     * @param log - name of log, for which time duration is to be changed
     */
    public void saveTheActiveScript(WebDriverWait waitForReload, String log) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnSaveActiveJob());
        clkWebElement(getBtnSaveActiveJob());
        CSLogger.info("successfully saved the script "
                + "after changing the time duration of '" + log + "'.");
    }

    /**
     * Returns the element for selecting SelectAll checkbox
     * 
     * @return - selectAllCheckBox
     */
    public WebElement selectAllChkBox() {
        return selectAllChkBox;
    }

    /**
     * Returns the element for export as CSV
     * 
     * @return - btnExportCSV
     */
    public WebElement exportAsCSV() {
        return btnExportCSV;
    }

    /**
     * Returns the element for mass update (marked) dropdown.
     * 
     * @return - massUpdateSelector
     */
    public WebElement massUpdateSelector() {
        return massUpdateSelector;
    }

    /**
     * Returns the element for export as Excel
     * 
     * @return - btnExportCSV
     */
    public WebElement exportAsExcel() {
        return btnExportExcel;
    }

    /**
     * This method will handle the browser alert box
     * 
     * @return - alertBox
     */
    public Alert getAlertBox(WebDriver browserDriver) {
        Alert alertBox;
        CSUtility.tempMethodForThreadSleep(2000);
        alertBox = browserDriver.switchTo().alert();
        return alertBox;
    }

    /**
     * This method will enter (i.e. pass) text into text boxes. Text will be
     * fetched from excel data provider
     * 
     * @param element - textbox
     * @param text - value to be entered into textbox
     */
    public void enterTextIntoTextbox(WebDriverWait waitForReload,
            WebElement element, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Selects the given active script option.
     * 
     * @param valueOfDrpDwn text value in drop down webElement.
     */
    public void selectActiveScriptOption(String valueOfDrpDwn) {
        getDrpDwnSelectActiveScript().click();
        Select element = new Select(getDrpDwnSelectActiveScript());
        element.selectByVisibleText(valueOfDrpDwn);
        CSLogger.info("Active script option " + valueOfDrpDwn + " selected");
    }

    /**
     * Returns the section title WebElement of data source.
     * 
     * @return WebElement btnDataSource
     */
    public WebElement getSecDataSource() {
        return secDataSource;
    }

    /**
     * Returns the drop down WebElement to select the data source type while
     * exporting data.
     * 
     * @return WebElement drpDwnDataSourceType.
     */
    public WebElement getDrpDwnDataSourceType() {
        return drpDwnDataSourceType;
    }

    /**
     * Returns the drop down WebElement to select the data selection type while
     * exporting data.
     * 
     * @return WebElement drpDwnDataSelectionType.
     */
    public WebElement getDrpDwnDataSelectionType() {
        return drpDwnDataSelectionType;
    }

    /**
     * Returns the WebElement ctnSelectRootFolders to select the root folder to
     * be exported.
     * 
     * @return WebElement ctnSelectRootFolders.
     */
    public WebElement getCtnSelectRootFolders() {
        return ctnSelectRootFolders;
    }

    /**
     * Returns the WebElement drpDwnSelectionLayer.
     * 
     * @return WebElement drpDwnSelectionLayer.
     */
    public WebElement getDrpDwnSelectionLayer() {
        return drpDwnSelectionLayer;
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
     * Returns the WebElement drpDwnSelectDefaultLanguage to select the default
     * language.
     * 
     * @return WebElement drpDwnSelectDefaultLanguage.
     */
    public WebElement getDrpDwnSelectDefaultLanguage() {
        return drpDwnSelectDefaultLanguage;
    }

    /**
     * Returns the WebElement getTxtStartCount to enter the start count.
     * 
     * @return WebElement getTxtStartCount.
     */
    public WebElement getTxtStartCount() {
        return txtStartCount;
    }

    /**
     * Returns the WebElement txtMaxCount to enter the max count.
     * 
     * @return WebElement txtMaxCount.
     */
    public WebElement getTxtMaxCount() {
        return txtMaxCount;
    }

    /**
     * Returns the WebElement txtBatchSize to enter the batch size.
     * 
     * @return WebElement txtBatchSize.
     */
    public WebElement getTxtBatchSize() {
        return txtBatchSize;
    }

    /**
     * Returns the section title WebElement of data target.
     * 
     * @return WebElement btnDataTarget.
     */
    public WebElement getSecDataTarget() {
        return secDataTarget;
    }

    /**
     * Returns the data target type.
     * 
     * @return WebElement txtDataTargetType.
     */
    public WebElement getDrpDwnDataTargetType() {
        return drpDwnDataTargetType;
    }

    /**
     * Returns the WebElement file name.
     * 
     * @return WebElement txtDataTargetFileName.
     */
    public WebElement getTxtDataTargetFileName() {
        return txtDataTargetFileName;
    }

    /**
     * Returns the data target delimiter.
     * 
     * @return WebElement txtDataTargetDelimiter.
     */
    public WebElement getTxtDataTargetDelimiter() {
        return txtDataTargetDelimiter;
    }

    /**
     * Returns the data target CSVEncloser.
     * 
     * @return WebElement txtDataTargetCSVEncloser.
     */
    public WebElement getTxtDataTargetCSVEncloser() {
        return txtDataTargetCSVEncloser;
    }

    /**
     * Returns the data target CSV Encoding.
     * 
     * @return WebElement drpDwnDataTargetCSVEncoding.
     */
    public WebElement getDrpDwnDataTargetCSVEncoding() {
        return drpDwnDataTargetCSVEncoding;
    }

    /**
     * Returns the data transformation section.
     * 
     * @return WebElement btnDataTransformation.
     */
    public WebElement getSecDataTransformation() {
        return secDataTransformation;
    }

    /**
     * Returns the check box WebElement UseTransformationEditor.
     * 
     * @return WebElement cbUseTransformationEditor.
     */
    public WebElement getCbUseTransformationEditor() {
        return cbUseTransformationEditor;
    }

    /**
     * Clicks on check box 'Use Transformation Editor'.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void checkUseTransformationEditor(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getCbUseTransformationEditor());
        if (getCbUseTransformationEditor().getAttribute("class")
                .contains("On")) {
            CSLogger.info("Checkbox 'Use Transformation Editor' is already ON");
        } else {
            getCbUseTransformationEditor().click();
            CSLogger.info("Checked 'Use Transformation Editor' checkbox");
        }
    }

    /**
     * Returns the 'Edit Transformation button'.
     * 
     * @return WebElement btnEditTransformation.
     */
    public WebElement getBtnEditTransformation() {
        return btnEditTransformation;
    }

    /**
     * Returns the check box 'Context Sensitive'.
     * 
     * @return WebElement cbContextSensitive.
     */
    public WebElement getCbContextSensitive() {
        return cbContextSensitive;
    }

    /**
     * Returns the online help link.
     * 
     * @return WebElement lblOnlineHelpLink.
     */
    public WebElement getLblOnlineHelp() {
        return lblOnlineHelpLink;
    }

    /**
     * Returns the drop down to select data source type while data flow import.
     * 
     * @return WebElement drpDwnImportDataSourceType.
     */
    public WebElement getDrpDwnImportDataSourceType() {
        return drpDwnImportDataSourceType;
    }

    /**
     * Returns the '+' WebElememt to select data source file while data flow
     * import.
     * 
     * @return WebElement btnDataSourceFile.
     */
    public WebElement getbtnDataSourceFile() {
        return btnDataSourceFile;
    }

    /**
     * Returns the text box to enter header line number while data flow import.
     * 
     * @return WebElement txtHeaderLineNumber.
     */
    public WebElement getTxtHeaderLineNumber() {
        return txtHeaderLineNumber;
    }

    /**
     * Returns the text box to enter first content line number while data flow
     * import.
     * 
     * @return WebElement txtFirstContentLineNumber.
     */
    public WebElement getTxtFirstContentLineNumber() {
        return txtFirstContentLineNumber;
    }

    /**
     * Returns the information link.
     * 
     * @return WebElement lblScriptInfo.
     */
    public WebElement getLblScriptInfo() {
        return lblScriptInfo;
    }

    /**
     * Clicks on '+' to select the data source file while data flow import.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkOnDataSourceFile(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getbtnDataSourceFile());
        getbtnDataSourceFile().click();
        CSLogger.info(
                "Clicked on '+' to select data source file while data flow import");
    }

    /**
     * Returns the delimiter element.
     * 
     * @return WebElement txtActiveScriptDelimiter.
     */
    public WebElement getTxtActiveScriptDelimiter() {
        return txtActiveScriptDelimiter;
    }

    /**
     * Returns the script enclosure element.
     * 
     * @return WebElement txtActivescriptEnclosure.
     */
    public WebElement getTxtActivescriptEnclosure() {
        return txtActivescriptEnclosure;
    }

    /**
     * Returns the script encoding element.
     * 
     * @return WebElement drpDwnActiveScriptEncoding.
     */
    public WebElement getDrpDwnActiveScriptEncoding() {
        return drpDwnActiveScriptEncoding;
    }

    /**
     * Returns the WebElement to select the 'ID Field of the Data Source'.
     * 
     * @return WebElement drpDwnDataSourceID.
     */
    public WebElement getdrpDwnDataSourceID() {
        return drpDwnDataSourceID;
    }

    /**
     * Returns the drop down to select the data target type while import.
     * 
     * @return WebElement drpDwnImportDataTargetType.
     */
    public WebElement getdrpDwnImportDataTargetType() {
        return drpDwnImportDataTargetType;
    }

    /**
     * Returns the drop down to select the script mode while import.
     * 
     * @return WebElement btnActiveScriptMode.
     */
    public WebElement getDrpDwnActiveScriptMode() {
        return drpDwnActiveScriptMode;
    }

    /**
     * Returns the drop down element to select the ID of data target field while
     * data flow import.
     * 
     * @return WebElement drpDwnDataTargetId.
     */
    public WebElement getDrpDwnDataTargetId() {
        return drpDwnDataTargetId;
    }

    /**
     * Returns the element to select the target folder while data flow import.
     * 
     * @return WebElement cntImportTargetFolder.
     */
    public WebElement getCtnImportTargetFolder() {
        return ctnImportTargetFolder;
    }

    /**
     * Returns the text box to enter the script row number.
     * 
     * @return WebElement txtActiveScriptStartRowNumber.
     */
    public WebElement getTxtActiveScriptStartRowNumber() {
        return txtActiveScriptStartRowNumber;
    }

    /**
     * Returns the text box to enter the max rows.
     * 
     * @return WebElement txtActiveScriptMaxRows.
     */
    public WebElement getTxtActiveScriptMaxRows() {
        return txtActiveScriptMaxRows;
    }

    /**
     * Returns the text box to enter the threads.
     * 
     * @return WebElement txtActiveScriptMultiThreads.
     */
    public WebElement getTxtActiveScriptMultiThreads() {
        return txtActiveScriptMultiThreads;
    }

    /**
     * Returns the Id filter text box.
     * 
     * @return WebElement txtActivescriptLimitFilter.
     */
    public WebElement getTxtActivescriptLimitFilter() {
        return txtActivescriptLimitFilter;
    }

    /**
     * Returns the 'Edit Transformation button' while script action is selected
     * as 'Data flow import'.
     * 
     * @return WebElement btnImportEditTransformation.
     */
    public WebElement getBtnImportEditTransformation() {
        return btnImportEditTransformation;
    }

    /**
     * Returns the context WebElement to select the data source file.
     * 
     * @return WebElement cntDataSourceFile.
     */
    public WebElement getCtnDataSourceFile() {
        return ctnDataSourceFile;
    }

    /**
     * Clicks on data source option.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkOnContextDataSourceFileOption(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getCtnDataSourceFile());
        getCtnDataSourceFile().click();
        CSLogger.info("Clicked on data source file option");
    }

    /**
     * Returns the text box to select the object selector in data flow import
     * script when data source file is selected as XML file.
     * 
     * @return WebElement txtXmlObjectSelector.
     */
    public WebElement getTxtXmlObjectSelector() {
        return txtXmlObjectSelector;
    }

    /**
     * Checks whether context sensitive button is OFF if yes,clicks on it and
     * mark it ON.
     */
    public void checkContextSensitiveCheckBox() {
        if (getCbContextSensitive().getAttribute("class").contains("Off")) {
            getCbContextSensitive().click();
            CSLogger.info("Checked context sensitive checkbox");
        } else {
            CSLogger.info("Context sensitive checkbox is already ON");
        }
    }

    /**
     * Returns the active script element of count.
     * 
     * @return WebElement drpDwnActivescriptSleepAfterEachStep.
     */
    public WebElement getDrpDwnActivescriptSleepAfterEachStep() {
        return drpDwnActivescriptSleepAfterEachStep;
    }

    /**
     * Clicks on given WebElement.
     * 
     * @param waitForReload WebDriverWait object.
     * @param element WebElement on which click operation will be performed.
     */
    public void clkOnGivenWebElement(WebDriverWait waitForReload,
            WebElement element) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        CSLogger.info("Clicked on given element");
    }

    /**
     * Returns element to select the template file.
     * 
     * @return WebElement btnTemplateFile.
     */
    public WebElement getBtnTemplateFile() {
        return btnTemplateFile;
    }

    /**
     * Returns the active script element of file generation.
     * 
     * @return WebElement drpDwnActivescriptFileGeneration.
     */
    public WebElement getDrpDwnActivescriptFileGeneration() {
        return drpDwnActivescriptFileGeneration;
    }

    /**
     * Returns the text box WebElement to enter the worksheet name.
     * 
     * @return WebElement txtWorksheet.
     */
    public WebElement getTxtWorksheet() {
        return txtWorksheet;
    }

    /**
     * Returns the checkbox add headline element.
     * 
     * @return WebElement cbAddHeadline.
     */
    public WebElement getCbAddHeadline() {
        return cbAddHeadline;
    }

    /**
     * Checks whether given check box is OFF if yes,clicks on it and mark it ON.
     * 
     * @param waitForReload WebDriverWait object.
     * @param element WebElement of check box
     */
    public void checkGivenCheckBox(WebDriverWait waitForReload,
            WebElement element) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        if (element.getAttribute("class").contains("Off")) {
            element.click();
            CSLogger.info("Checked given checkbox element");
        } else {
            CSLogger.info("Given checkbox is already ON");
        }
    }
    
}
