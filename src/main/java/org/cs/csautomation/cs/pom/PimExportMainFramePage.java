
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PimExportMainFramePage {

    private WebDriver  browserDriverInstance;

    @FindBy(xpath = "//nobr[contains(text(),'Export')]/parent::*")
    private WebElement btnExport;

    @FindBy(xpath = "//nobr[contains(text(),'Extended Settings')]/parent::*")
    private WebElement btnExtendedSettings;

    @FindBy(xpath = "//nobr[contains(text(),'Target')]/parent::*")
    private WebElement btnTarget;

    @FindBy(xpath = "//div[contains(text(),'General')]")
    private WebElement seperatorGeneral;

    @FindBy(xpath = "//select[contains(@id,'PdmexportPlugin')]")
    private WebElement dropDwnExportType;

    @FindBy(xpath = "//input[contains(@id,'PdmexportLabel')]")
    private WebElement txtLabel;

    @FindBy(xpath = "//div[contains(text(),'Channel Selection')]")
    private WebElement seperatorChannelSelection;

    @FindBy(xpath = "//input[contains(@id,'PdmexportViewID__Label')]")
    private WebElement popupChannelSelection;

    @FindBy(xpath = "//select[contains(@id,'PdmexportExportStructure')]")
    private WebElement dropDwnChannelSelection;

    @FindBy(xpath = "//div[contains(text(),'System Settings')]")
    private WebElement seperatorSystemSettings;

    @FindBy(xpath = "//input[contains(@id,'PdmexportMemoryLimit')]")
    private WebElement txtMemoryLimit;

    @FindBy(xpath = "//input[contains(@id,'PdmexportTimeoutMinutes')]")
    private WebElement txtTimeLimit;

    @FindBy(xpath = "//div[contains(text(),'Prices')]")
    private WebElement seperatorPrices;

    @FindBy(xpath = "//select[contains(@id,'PdmexportExportPrices')]")
    private WebElement dropDwnExportPrices;

    @FindBy(xpath = "//div[contains(text(),'Attributes')]")
    private WebElement seperatorAttributes;

    @FindBy(xpath = "//div[contains(text(),'Translations')]")
    private WebElement seperatorTranslations;

    @FindBy(xpath = "//div[contains(text(),'Export Paths')]")
    private WebElement seperatorExportPaths;

    @FindBy(xpath = "//select[contains(@id,'PdmexportExportHierachieStart')]")
    private WebElement dropDwnExportHierachieStart;

    @FindBy(xpath = "//input[contains(@id,'PdmexportTargetFileName')]")
    private WebElement txtTargetFileName;

    @FindBy(xpath = "//input[contains(@id,'PdmexportFtpHost')]")
    private WebElement txtHost;

    @FindBy(xpath = "//input[contains(@id,'PdmexportFtpUserName')]")
    private WebElement txtFtpUserName;

    @FindBy(xpath = "//input[contains(@id,'PdmexportFtpPassword')]")
    private WebElement txtFtpPassword;

    @FindBy(xpath = "//input[contains(@id,'PdmexportFtpPort')]")
    private WebElement txtFtpPort;

    @FindBy(xpath = "//input[contains(@id,'PdmexportFtpHttpRoot')]")
    private WebElement txtFtpRoot;

    @FindBy(xpath = "//input[contains(@id,'PdmexportAddDateToFileName')]")
    private WebElement chkboxAddDate;

    @FindBy(xpath = "//span[contains(@id,'PdmexportAddDateToFileName_GUI')]")
    private WebElement chkboxAddDateGui;

    @FindBy(xpath = "//input[contains(@id,'PdmexportAddUserToFileName')]")
    private WebElement chkboxAddUser;

    @FindBy(xpath = "//span[contains(@id,'PdmexportAddUserToFileName_GUI')]")
    private WebElement chkboxAddUserGui;

    @FindBy(xpath = "//input[contains(@id,'PdmexportExportDocuments')]")
    private WebElement chkboxExportDocuments;

    @FindBy(xpath = "//span[contains(@id,'PdmexportExportDocuments_GUI')]")
    private WebElement chkboxExportDocumentsGui;

    @FindBy(xpath = "//input[contains(@id,'PdmexportFtpExport')]")
    private WebElement chkboxFtpExport;

    @FindBy(xpath = "//input[contains(@id,'PdmexportFtpPassiveMode')]")
    private WebElement chkboxFtpPassiveMode;

    @FindBy(xpath = "//input[contains(@id,'PdmexportFtpSslMode')]")
    private WebElement chkboxFtpSslMode;

    @FindBy(xpath = "//div[contains(text(),'Target File')]")
    private WebElement seperatorTargetFile;

    @FindBy(xpath = "//div[contains(text(),'FTP Transfer')]")
    private WebElement seperatorFTPTransfer;
	
	@FindBy(xpath = "//input[contains(@id,'PdmexportEncoding')]")
    private WebElement txtCsvEncoding;

    public PimExportMainFramePage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized PimExportMainFramePage page POM elements.");
    }

    /**
     * Getter for browserDriverInstance
     * 
     * @return WebDriver
     */
    private WebDriver getBrowserDriverInstance() {
        return browserDriverInstance;
    }

    /**
     * Setter for browserDriverInstance
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * Getter for btnExport
     * 
     * @return btnExport WebElement object
     */
    public WebElement getBtnExport() {
        return btnExport;
    }

    /**
     * Getter for btnExtendedSettings
     * 
     * @return btnExtendedSettings WebElement object
     */
    public WebElement getBtnExtendedSettings() {
        return btnExtendedSettings;
    }

    /**
     * Getter for btnTarget
     * 
     * @return btnTarget WebElement object
     */
    public WebElement getBtnTarget() {
        return btnTarget;
    }

    /**
     * Getter for seperatorGeneral
     * 
     * @return seperatorGeneral WebElement object
     */
    public WebElement getSeperatorGeneral() {
        return seperatorGeneral;
    }

    /**
     * Getter for drpdwnExportType
     * 
     * @return dropDwnExportType WebElement object
     */
    public WebElement getDropDwnExportType() {
        return dropDwnExportType;
    }

    /**
     * Getter for txtLabel
     * 
     * @return txtLabel WebElement object
     */
    public WebElement getTxtLabel() {
        return txtLabel;
    }

    /**
     * Getter for seperatorChannelSelection
     * 
     * @return seperatorChannelSelection WebElement object
     */
    public WebElement getSeperatorChannelSelection() {
        return seperatorChannelSelection;
    }

    /**
     * Getter for popupChannelSelection
     * 
     * @return popupChannelSelection WebElement object
     */
    public WebElement getPopupChannelSelection() {
        return popupChannelSelection;
    }

    /**
     * Getter for seperatorSystemSettings
     * 
     * @return seperatorSystemSettings WebElement object
     */
    public WebElement getSeperatorSystemSettings() {
        return seperatorSystemSettings;
    }

    /**
     * Getter for txtMemoryLimit
     * 
     * @return txtMemoryLimit WebElement object
     */
    public WebElement getTxtMemoryLimit() {
        return txtMemoryLimit;
    }

    /**
     * Getter for txtTimeLimit
     * 
     * @return txtTimeLimit WebElement object
     */
    public WebElement getTxtTimeLimit() {
        return txtTimeLimit;
    }

    /**
     * Getter for drpdwnChannelSelection
     * 
     * @return dropDwnChannelSelection WebElement object
     */
    public WebElement getDropDwnChannelSelection() {
        return dropDwnChannelSelection;
    }

    /**
     * Getter for seperatorPrices
     * 
     * @return seperatorPrices WebElement object
     */
    public WebElement getSeperatorPrices() {
        return seperatorPrices;
    }

    /**
     * Getter for dropdwnExportPrices
     * 
     * @return dropDwnExportPrices WebElement object
     */
    public WebElement getDropDwnExportPrices() {
        return dropDwnExportPrices;
    }

    /**
     * Getter for seperatorAttributes
     * 
     * @return seperatorAttributes WebElement object
     */
    public WebElement getSeperatorAttributes() {
        return seperatorAttributes;
    }

    /**
     * Getter for seperatorTranslations
     * 
     * @return seperatorTranslations WebElement object
     */
    public WebElement getSeperatorTranslations() {
        return seperatorTranslations;
    }

    /**
     * Getter for seperatorExportPaths
     * 
     * @return seperatorExportPaths WebElement object
     */
    public WebElement getSeperatorExportPaths() {
        return seperatorExportPaths;
    }

    /**
     * Getter for dropdwnExportHierachieStart
     * 
     * @return dropDwnExportHierachieStart WebElement object
     */
    public WebElement getDropDwnExportHierachieStart() {
        return dropDwnExportHierachieStart;
    }

    /**
     * Getter for txtTargetFileName
     * 
     * @return txtTargetFileName WebElement object
     */
    public WebElement getTxtTargetFileName() {
        return txtTargetFileName;
    }

    /**
     * Getter for txtHost
     * 
     * @return txtHost WebElement object
     */
    public WebElement getTxtHost() {
        return txtHost;
    }

    /**
     * Getter for txtFtpUserName
     * 
     * @return txtFtpUserName WebElement object
     */
    public WebElement getTxtFtpUserName() {
        return txtFtpUserName;
    }

    /**
     * Getter for txtFtpPassword
     * 
     * @return txtFtpPassword WebElement object
     */
    public WebElement getTxtFtpPassword() {
        return txtFtpPassword;
    }

    /**
     * Getter for txtFtpPort
     * 
     * @return txtFtpPort WebElement object
     */
    public WebElement getTxtFtpPort() {
        return txtFtpPort;
    }

    /**
     * Getter for txtFtpRoot
     * 
     * @return txtFtpRoot WebElement object
     */
    public WebElement getTxtFtpRoot() {
        return txtFtpRoot;
    }

    /**
     * Getter for chkboxAddDate
     * 
     * @return chkboxAddDate WebElement object
     */
    public WebElement getChkboxAddDate() {
        return chkboxAddDate;
    }

    /**
     * Getter for chkboxAddUser
     * 
     * @return chkboxAddUser WebElement object
     */
    public WebElement getChkboxAddUser() {
        return chkboxAddUser;
    }

    /**
     * Getter for chkboxExportDocuments
     * 
     * @return chkboxExportDocuments WebElement object
     */
    public WebElement getChkboxExportDocuments() {
        return chkboxExportDocuments;
    }

    /**
     * Getter for chkboxFtpExport
     * 
     * @return chkboxFtpExport WebElement object
     */
    public WebElement getChkboxFtpExport() {
        return chkboxFtpExport;
    }

    /**
     * Getter for chkboxFtpPassiveMode
     * 
     * @return chkboxFtpPassiveMode WebElement object
     */
    public WebElement getChkboxFtpPassiveMode() {
        return chkboxFtpPassiveMode;
    }

    /**
     * Getter for chkboxFtpSslMode
     * 
     * @return chkboxFtpSslMode WebElement object
     */
    public WebElement getChkboxFtpSslMode() {
        return chkboxFtpSslMode;
    }

    /**
     * Getter for seperatorTargetFile
     * 
     * @return seperatorTargetFile WebElement object
     */
    public WebElement getSeperatorTargetFile() {
        return seperatorTargetFile;
    }

    /**
     * Getter for seperatorFTPTransfer
     * 
     * @return seperatorFTPTransfer WebElement object
     */
    public WebElement getSeperatorFTPTransfer() {
        return seperatorFTPTransfer;
    }

    /**
     * Getter for chkboxAddDateGui
     * 
     * @return chkboxAddDateGui WebElement object
     */
    public WebElement getChkboxAddDateGui() {
        return chkboxAddDateGui;
    }

    /**
     * Getter for chkboxAddUserGui
     * 
     * @return chkboxAddUserGui WebElement object
     */
    public WebElement getChkboxAddUserGui() {
        return chkboxAddUserGui;
    }

    /**
     * Getter for chkboxExportDocumentsGui
     * 
     * @return chkboxExportDocumentsGui WebElement object
     */
    public WebElement getChkboxExportDocumentsGui() {
        return chkboxExportDocumentsGui;
    }

    /**
     * This method perform click operation on the Export tab
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkExport(WebDriverWait waitForReload) {
        clkElement(waitForReload, getBtnExport());
        CSLogger.info("Clicked on Export tab.");
    }

    /**
     * This method perform click operation on the Extended Settings tab
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkExtendedSettings(WebDriverWait waitForReload) {
        clkElement(waitForReload, getBtnExtendedSettings());
        CSLogger.info("Clicked on Extended Settings.");
    }

    /**
     * This method perform click operation on the Target tab
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkTarget(WebDriverWait waitForReload) {
        clkElement(waitForReload, getBtnTarget());
        CSLogger.info("Clicked on the Target tab.");
    }

    /**
     * This method checks if the General seperator is open or not, if not then
     * click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorGeneral(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorGeneral())) {
            clkElement(waitForReload, getSeperatorGeneral());
            CSLogger.info("Clicked on the General Seperator.");
        }
    }

    /**
     * This method checks if the Channel selection seperator is open or not, if
     * not then click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorChannelSection(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorChannelSelection())) {
            clkElement(waitForReload, getSeperatorChannelSelection());
            CSLogger.info("Clicked on the Channel Seperator.");
        }
    }

    /**
     * This method checks if the System settings seperator is open or not, if
     * not then click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorSystemSettings(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorSystemSettings())) {
            clkElement(waitForReload, getSeperatorSystemSettings());
            CSLogger.info("Clicked on the System setting Seperator.");
        }
    }

    /**
     * This method checks if the prices seperator is open or not, if not then
     * click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorPrices(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorPrices())) {
            clkElement(waitForReload, getSeperatorPrices());
            CSLogger.info("Clicked on prices seperator.");
        }
    }

    /**
     * This method checks if the Attributes seperator is open or not, if not
     * then click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorAttibutes(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorAttributes())) {
            clkElement(waitForReload, getSeperatorAttributes());
            CSLogger.info("Clicked on Attributes seperator.");
        }
    }

    /**
     * This method checks if the Translations seperator is open or not, if not
     * then click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorTranslations(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorTranslations())) {
            clkElement(waitForReload, getSeperatorTranslations());
            CSLogger.info("CLicked on the Translation Seperator.");
        }
    }

    /**
     * This method checks if the Export paths seperator is open or not, if not
     * then click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorExportPaths(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorExportPaths())) {
            clkElement(waitForReload, getSeperatorExportPaths());
            CSLogger.info("Clicked on the Export path seperator.");
        }
    }

    /**
     * This method checks if the FTP transfer seperator is open or not, if not
     * then click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorFtpTransfer(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorFTPTransfer())) {
            clkElement(waitForReload, getSeperatorFTPTransfer());
            CSLogger.info("Clicked on the FTP Transfer seperator.");
        }
    }

    /**
     * This method checks if the Target file seperator is open or not, if not
     * then click operation is performed.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkSeperatorTargetFile(WebDriverWait waitForReload) {
        if (isSeperatorClosed(getSeperatorTargetFile())) {
            clkElement(waitForReload, getSeperatorTargetFile());
            CSLogger.info("Clicked on the Target File seperator.");
        }
    }

    /**
     * This method performs click operation on the provided WebElement object
     * before waiting for it to be clickable.
     * 
     * @param waitForReload WebDriverWait object
     * @param element WebElement object
     */
    private void clkElement(WebDriverWait waitForReload, WebElement element) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * This method checks if the WebElement object referring to the sepetator is
     * closed or not.
     * 
     * @param element WebElement object
     * @return Boolean value : true, if seperator is closed and false if its
     *         open
     */
    private boolean isSeperatorClosed(WebElement element) {
        WebElement parent = (WebElement) ((JavascriptExecutor) browserDriverInstance)
                .executeScript("return arguments[0].parentNode.parentNode;",
                        element);
        if (parent.getAttribute("class").contains("closed")) {
            return true;
        }
        return false;
    }
	
	/**
     * Returns WebElement txtCsvEncoding
     * 
     * @return btnPimExportNode
     */
    public WebElement getTxtCsvEncoding() {
        return txtCsvEncoding;
    }
}
