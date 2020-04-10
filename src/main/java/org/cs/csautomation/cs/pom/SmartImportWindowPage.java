/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This class contains the WebElement and acts as POM class for Smart Import.
 * 
 * @author CSAutomation Team
 *
 */
public class SmartImportWindowPage {

    @FindBy(xpath = "//input[@name='files[]']")
    private WebElement uplaoadImportFile;

    @FindBy(xpath = "//input[@placeholder='Select import language']")
    private WebElement ddImportLanguage;

    @FindBy(xpath = "//input[@placeholder='Select mapping set']")
    private WebElement ddMappingSet;

    @FindBy(
            xpath = "//button[@class='CSButtonView btn btn-sm with-text"
                    + " with-icon btn-transparent']")
    private WebElement btnNext;

    @FindBy(xpath = "//body//div[@class='CSToolbar']//div//div[3]//button[1]")
    private WebElement btnImport;

    @FindBy(xpath = "//a[@class='CSPortalWindowCloser']")
    private WebElement btnClose;

    @FindBy(xpath = "//select[@data-id='sheet']")
    private WebElement sddSheet;

    @FindBy(xpath = "//div[@data-id='rootnode']/div[1]/div[1]/div[1]")
    private WebElement sddRootNode;

    @FindBy(xpath = "//div[@data-id='configuration']/div[1]/div[1]/div[1]")
    private WebElement sddConfiguration;

    @FindBy(
            xpath = "//div[contains(text(),'Mandatory fields') and @class="
                    + "'CSListViewSectionHeaderTitle']")
    private WebElement lblMandatoryFields;

    @FindBy(
            xpath = "//div[contains(text(),'Default fields') and @class="
                    + "'CSListViewSectionHeaderTitle']")
    private WebElement lblDefaultFields;

    @FindBy(xpath = "//span[contains(text(),'Dry Run')]")
    private WebElement btnDryRun;

    @FindBy(xpath = "//div[@data-id='rightText']")
    private WebElement lblStatusBarFigure;

    @FindBy(xpath = "(//div[@data-id='log']/div)[last()]/span[last()]")
    private WebElement lblImportStatus;

    @FindBy(xpath = "//span[text()='Store Mapping']/parent::*")
    private WebElement btnStoreMapping;

    @FindBy(
            xpath = "//p[contains(text(),'Please specify a name for the mapping'"
                    + ")]/following-sibling::input")
    private WebElement inputStoreMappingName;

    @FindBy(
            xpath = "//div[@id='CSInfoCenter']/following-sibling::div/div[2]/div/div[4]/div[2]/button")
    private WebElement btnStoreMappingOk;

    @FindBy(
            xpath = "//span[contains(text(),'Import Mapping')]/parent::div/"
                    + "preceding-sibling::div/button")
    private WebElement btnBackToImportMapping;

    @FindBy(xpath = "//span[contains(text(),'Dry Run')]/parent::button")
    private WebElement btnDryImport;

    public SmartImportWindowPage(WebDriver driverInstance) {
        PageFactory.initElements(driverInstance, this);
        CSLogger.info("Initialized Login page POM elements.");
    }

    /**
     * Getter method for btnDryRun
     * 
     * @return the btnDryRun
     */
    public WebElement getBtnDryRun() {
        return btnDryRun;
    }

    /**
     * Setter method for btnDryRun
     * 
     * @param btnDryRun the btnDryRun to set
     */
    public void setBtnDryRun(WebElement btnDryRun) {
        this.btnDryRun = btnDryRun;
    }

    /**
     * Getter method for lblMandatoryFields
     * 
     * @return the lblMandatoryFields
     */
    public WebElement getLblMandatoryFields() {
        return lblMandatoryFields;
    }

    /**
     * Setter method for lblMandatoryFields
     * 
     * @param lblMandatoryFields the lblMandatoryFields to set
     */
    public void setLblMandatoryFields(WebElement lblMandatoryFields) {
        this.lblMandatoryFields = lblMandatoryFields;
    }

    /**
     * Getter method for lblDefaultFields
     * 
     * @return the lblDefaultFields
     */
    public WebElement getLblDefaultFields() {
        return lblDefaultFields;
    }

    /**
     * Setter method for lblDefaultFields
     * 
     * @param lblDefaultFields the lblDefaultFields to set
     */
    public void setLblDefaultFields(WebElement lblDefaultFields) {
        this.lblDefaultFields = lblDefaultFields;
    }

    /**
     * Getter method for uplaoadImportFile
     * 
     * @return the uplaoadImportFile
     */
    public WebElement getUplaoadImportFile() {
        return uplaoadImportFile;
    }

    /**
     * Setter method for uplaoadImportFile
     * 
     * @param uplaoadImportFile the uplaoadImportFile to set
     */
    public void setUplaoadImportFile(WebElement uplaoadImportFile) {
        this.uplaoadImportFile = uplaoadImportFile;
    }

    /**
     * Getter method for ddImportLanguage
     * 
     * @return the ddImportLanguage
     */
    public WebElement getDdImportLanguage() {
        return ddImportLanguage;
    }

    /**
     * Setter method for ddImportLanguage
     * 
     * @param ddImportLanguage the ddImportLanguage to set
     */
    public void setDdImportLanguage(WebElement ddImportLanguage) {
        this.ddImportLanguage = ddImportLanguage;
    }

    /**
     * Getter method for ddMappingSet
     * 
     * @return the ddMappingSet
     */
    public WebElement getDdMappingSet() {
        return ddMappingSet;
    }

    /**
     * Setter method for ddMappingSet
     * 
     * @param ddMappingSet the ddMappingSet to set
     */
    public void setDdMappingSet(WebElement ddMappingSet) {
        this.ddMappingSet = ddMappingSet;
    }

    /**
     * Getter method for btnNext
     * 
     * @return the btnNext
     */
    public WebElement getBtnNext() {
        return btnNext;
    }

    /**
     * Setter method for btnNext
     * 
     * @param btnNext the btnNext to set
     */
    public void setBtnNext(WebElement btnNext) {
        this.btnNext = btnNext;
    }

    /**
     * Getter method for btnImport
     * 
     * @return the btnImport
     */
    public WebElement getBtnImport() {
        return btnImport;
    }

    /**
     * Setter method for btnImport
     * 
     * @param btnImport the btnImport to set
     */
    public void setBtnImport(WebElement btnImport) {
        this.btnImport = btnImport;
    }

    /**
     * Getter method for btnClose
     * 
     * @return the btnClose
     */
    public WebElement getBtnClose() {
        return btnClose;
    }

    /**
     * Setter method for btnClose
     * 
     * @param btnClose the btnClose to set
     */
    public void setBtnClose(WebElement btnClose) {
        this.btnClose = btnClose;
    }

    /**
     * Getter method for sddSheet
     * 
     * @return the sddSheet
     */
    public WebElement getSddSheet() {
        return sddSheet;
    }

    /**
     * Setter method for sddSheet
     * 
     * @param sddSheet the sddSheet to set
     */
    public void setSddSheet(WebElement sddSheet) {
        this.sddSheet = sddSheet;
    }

    /**
     * Getter method for sddRootNode
     * 
     * @return the sddRootNode
     */
    public WebElement getSddRootNode() {
        return sddRootNode;
    }

    /**
     * Setter method for sddRootNode
     * 
     * @param sddRootNode the sddRootNode to set
     */
    public void setSddRootNode(WebElement sddRootNode) {
        this.sddRootNode = sddRootNode;
    }

    /**
     * Getter method for sddConfiguration
     * 
     * @return the sddConfiguration
     */
    public WebElement getSddConfiguration() {
        return sddConfiguration;
    }

    /**
     * Setter method for sddConfiguration
     * 
     * @param sddConfiguration the sddConfiguration to set
     */
    public void setSddConfiguration(WebElement sddConfiguration) {
        this.sddConfiguration = sddConfiguration;
    }

    /**
     * Getter method for lblImportStatus
     * 
     * @return the lblImportStatus
     */
    public WebElement getLblImportStatus() {
        return lblImportStatus;
    }

    /**
     * Setter method for lblImportStatus
     * 
     * @param lblImportStatus the lblImportStatus to set
     */
    public void setLblImportStatus(WebElement lblImportStatus) {
        this.lblImportStatus = lblImportStatus;
    }

    /**
     * Getter method for lblStatusBarFigure
     * 
     * @return the lblStatusBarFigure
     */
    public WebElement getLblStatusBarFigure() {
        return lblStatusBarFigure;
    }

    /**
     * Setter method for lblStatusBarFigure
     * 
     * @param lblStatusBarFigure the lblStatusBarFigure to set
     */
    public void setLblStatusBarFigure(WebElement lblStatusBarFigure) {
        this.lblStatusBarFigure = lblStatusBarFigure;
    }

    /**
     * Getter method for btnStoreMapping
     * 
     * @return the btnStoreMapping
     */
    public WebElement getBtnStoreMapping() {
        return btnStoreMapping;
    }

    /**
     * Setter method for btnStoreMapping
     * 
     * @param btnStoreMapping the btnStoreMapping to set
     */
    public void setBtnStoreMapping(WebElement btnStoreMapping) {
        this.btnStoreMapping = btnStoreMapping;
    }

    /**
     * Getter method for inputStoreMappingName
     * 
     * @return the inputStoreMappingName
     */
    public WebElement getInputStoreMappingName() {
        return inputStoreMappingName;
    }

    /**
     * Setter method for inputStoreMappingName
     * 
     * @param inputStoreMappingName the inputStoreMappingName to set
     */
    public void setInputStoreMappingName(WebElement inputStoreMappingName) {
        this.inputStoreMappingName = inputStoreMappingName;
    }

    /**
     * Getter method for btnStoreMappingOk
     * 
     * @return the btnStoreMappingOk
     */
    public WebElement getBtnStoreMappingOk() {
        return btnStoreMappingOk;
    }

    /**
     * Setter method for btnStoreMappingOk
     * 
     * @param btnStoreMappingOk the btnStoreMappingOk to set
     */
    public void setBtnStoreMappingOk(WebElement btnStoreMappingOk) {
        this.btnStoreMappingOk = btnStoreMappingOk;
    }

    /**
     * Getter method for btnBackToImportMapping
     * 
     * @return the btnBackToImportMapping
     */
    public WebElement getBtnBackToImportMapping() {
        return btnBackToImportMapping;
    }

    /**
     * Setter method for btnBackToImportMapping
     * 
     * @param btnBackToImportMapping the btnBackToImportMapping to set
     */
    public void setBtnBackToImportMapping(WebElement btnBackToImportMapping) {
        this.btnBackToImportMapping = btnBackToImportMapping;
    }

    /**
     * Getter method for btnDryImport
     * 
     * @return the btnDryImport
     */
    public WebElement getBtnDryImport() {
        return btnDryImport;
    }

    /**
     * Setter method for btnDryImport
     * 
     * @param btnDryImport the btnDryImport to set
     */
    public void setBtnDryImport(WebElement btnDryImport) {
        this.btnDryImport = btnDryImport;
    }

}
