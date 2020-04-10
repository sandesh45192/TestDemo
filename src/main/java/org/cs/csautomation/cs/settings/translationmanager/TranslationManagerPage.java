/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings.translationmanager;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This page class initializes all web elements of translation manager page
 * 
 * @author CSAutomation Team
 *
 */
public class TranslationManagerPage {

    @FindBy(xpath = "//nobr[contains(text(),'Applications')]")
    private WebElement applicationsTab;

    @FindBy(xpath = "//div[contains(text(),'BPM Translation Manager')]")
    private WebElement subtiltleBPMTranslationManager;

    @FindBy(
            xpath = "(//table[@class='CSGuiTable GuiEditorTable']/tbody/tr/td[2]/table/tbody/tr[7]/td)[1]")
    private WebElement btnUnselectAllSourceLanguages;

    @FindBy(
            xpath = "(//table[@class='CSGuiTable GuiEditorTable']/tbody/tr/td[2]/table/tbody/tr[7]/td)[2]")
    private WebElement btnUnselectAllTargetLanguages;

    @FindBy(xpath = "(//select[@class='GuiEditorMultiSelect'])[1]")
    private WebElement txtAvailableSourceLang;

    @FindBy(xpath = "(//select[@class='GuiEditorMultiSelect'])[3]")
    private WebElement txtAvailableTargetLang;

    @FindBy(xpath = "//a[@class='CSPortalWindowCloser']")
    private WebElement btnClose;

    @FindBy(xpath = "//span[@id='job@0']/parent::*")
    private WebElement translationJobNode;

    @FindBy(xpath = "//input[contains(@id,'TranslationjobLabel')]")
    private WebElement txtTranslationJobLabel;

    @FindBy(xpath = "//nobr[contains(text(),'Data Collection')]")
    private WebElement dataCollectionTab;

    @FindBy(xpath = "//nobr[contains(text(),'Translations')]")
    private WebElement tabTranslations;

    @FindBy(
            xpath = "//select[(contains(@id,'TranslationjobTargetLangIDsNotSelected'))]")
    private WebElement txtTranslationJobAvailableTargetLanguage;

    @FindBy(
            xpath = "//select[(contains(@id,'TranslationjobTargetLangIDsMultiSelect'))]")
    private WebElement txtTranslationJobSelectedTargetLanguage;

    @FindBy(
            xpath = "//select[(contains(@id,'TranslationjobTranslatorIDMultiSelect'))]")
    private WebElement txtTranslationJobSelectedTranslatorField;

    @FindBy(
            xpath = "//select[(contains(@id,'TranslationjobTranslatorIDNotSelected'))]")
    private WebElement txtTranslationJobAvailableTranslator;

    @FindBy(
            xpath = "//div[contains(@id,'Translationjob_PdmarticleID_csReferenceDiv')]//img[contains(@class,'CSGuiSelectionListAdd')]")
    private WebElement btnPlusToAddProductTranslationManager;

    @FindBy(xpath = "//img[contains(@src,'down.svg')]/parent::a")
    private WebElement btnImport;

    @FindBy(xpath = "//span[@id='memory@0']")
    private WebElement translationsNode;

    @FindBy(xpath = "//span[contains(text(),' Translation required')]")
    private WebElement translationRequiredNode;

    @FindBy(xpath = "//img[contains(@src,'Fforward')]/parent::a")
    private WebElement btnDisplayObject;

    @FindBy(xpath = "//nobr[contains(text(),'Properties')]")
    private WebElement propertiesTab;

    @FindBy(xpath = "//input[contains(@id,'PdmarticleLabel')]")
    private WebElement txtLabel;

    @FindBy(xpath = "//textarea[contains(@id,'TranslationmemoryTargetValue')]")
    private WebElement txtTranslation;

    @FindBy(xpath = "//img[contains(@src,'next.svg')]/parent::a")
    private WebElement btnSaveAndLoadNextElement;

    @FindBy(xpath = "//img[contains(@src,'question.svg')]/parent::a")
    private WebElement btnRequestConfirmation;

    @FindBy(xpath = "//img[contains(@src,'question.svg')]")
    private WebElement btnVerifyRequestConfirmationTranslationsTab;

    @FindBy(xpath = "//img[contains(@src,'check.svg')]")
    private WebElement btnVerifyConfirmTranslationsTab;

    @FindBy(xpath = "//img[contains(@src,'up.svg')]")
    private WebElement btnVerifyImportFromTranslationMemory;

    @FindBy(xpath = "//img[contains(@src,'check.svg')]/parent::a")
    private WebElement btnConfirm;

    @FindBy(xpath = "//img[contains(@src,'up.svg')]/parent::a")
    private WebElement btnImportFromTranslationMemory;

    @FindBy(xpath = "(//img[contains(@src,'en.gif')])[1]")
    private WebElement btnCsGuiEditorFlag;

    @FindBy(xpath = "//td[contains(text(),'Refresh')]")
    private WebElement ctxRefresh;

    @FindBy(xpath = "//img[contains(@src,'cancel.svg')]")
    private WebElement btnVerifyOutdatedEntryTranslationTab;

    @FindBy(
            xpath = "//table[@class='CSGuiTable GuiEditorTable spacer']/tbody/tr[2]/td[2]/span")
    private WebElement chkboxUseOriginalValueAsSourceLang;

    @FindBy(
            xpath = "//select[contains(@id,'TranslationjobTranslationTypesNotSelected')]")
    private WebElement txtObjectsToBeSelected;

    @FindBy(
            xpath = "(//tr[@id='AttributeRow_Workflows']//table/tbody/tr[2]/td/select)[1]")
    private WebElement txtWorkflowsTabDataCollection;

    @FindBy(xpath = "(//table[@class='TabbedPaneTitle']/tbody/tr/td/nobr)[1]")
    private WebElement tabDiagramRightPaneTranslationManager;

    @FindBy(
            xpath = "(//tr[@id='AttributeRow_TranslationTypes']//table/tbody/tr[2]/td/select)[1]")
    private WebElement txtTabDataCollection;

    @FindBy(xpath = "//a/img[contains(@src,'delete.gif')]")
    private WebElement btnDelete;

    @FindBy(xpath = "//img[contains(@src,'cancel.svg')]")
    private WebElement btnOutdated;

    @FindBy(xpath = "//a/img[contains(@src,'studiomenu')]")
    private WebElement btnStudioMenu;

    /**
     * Returns the instance of studio menu
     * 
     * @return btnStudioMenu
     */
    public WebElement getBtnStudioMenu() {
        return btnStudioMenu;
    }

    /**
     * Returns the instance of outdated button from translation tab
     * 
     * @return btnOutdated
     */
    public WebElement getBtnOutdated() {
        return btnOutdated;
    }

    /**
     * This method returns the instance of delete button
     * 
     * @return btnDelete
     */
    public WebElement getBtnDelete() {
        return btnDelete;
    }

    /**
     * This method returns the instance of value list text box from data
     * collection tab
     * 
     * @return txtValueListTabData
     */
    public WebElement getTxtTabDataCollection() {
        return txtTabDataCollection;
    }

    /**
     * This method returns the instance of diagram tab in translation manager
     * 
     * @return the tabDiagramRightPaneTranslationManager
     */
    public WebElement getTabDiagramRightPaneTranslationManager() {
        return tabDiagramRightPaneTranslationManager;
    }

    /**
     * This method clicks on the diagram tab in right section pane of
     * translation manager
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkTabDiagramRightPaneTranslationManager(
            WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getTabDiagramRightPaneTranslationManager()));
        getTabDiagramRightPaneTranslationManager().click();
        CSLogger.info("Clicked on Diagram tab");
    }

    /**
     * This method returns the instance of workflows in data collection tab
     * 
     * @return the txtWorkflowsTabDataCollection
     */
    public WebElement getTxtWorkflowsTabDataCollection() {
        return txtWorkflowsTabDataCollection;
    }

    /**
     * This method returns the instance of outdated entry
     * 
     * @return the btnVerifyOutdatedEntryTranslationTab
     */
    public WebElement getBtnVerifyOutdatedEntryTranslationTab() {
        return btnVerifyOutdatedEntryTranslationTab;
    }

    /**
     * This method returns the instance of textarea objects to be selected
     * 
     * @return the txtObjectsToBeSelected
     */
    public WebElement getTxtObjectsToBeSelected() {
        return txtObjectsToBeSelected;
    }

    /**
     * This method returns the instance of use original value as source language
     * check box
     * 
     * @return the chkboxUseOriginalValueAsSourceLang
     */
    public WebElement getChkboxUseOriginalValueAsSourceLang() {
        return chkboxUseOriginalValueAsSourceLang;
    }

    /**
     * This method returns the instance of refresh button
     * 
     * @return ctxRefresh
     */
    public WebElement getCtxRefresh() {
        return ctxRefresh;
    }

    /**
     * This method clicks on Refresh option
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkCtxRefresh(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getCtxRefresh()));
        getCtxRefresh().click();
        CSLogger.info("Clicked on Refresh button.");
    }

    /**
     * This method returns the instance of cs gui editor flag for changing
     * language
     * 
     * @return the btnCsGuiEditorFlag
     */
    public WebElement getBtnCsGuiEditorFlag() {
        return btnCsGuiEditorFlag;
    }

    /**
     * This method clicks on cs gui editor flag button to change language
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnCsGuiEditorFlag(WebDriverWait waitForReload) {
        waitForReload.until(
                ExpectedConditions.visibilityOf(getBtnCsGuiEditorFlag()));
        getBtnCsGuiEditorFlag().click();
        CSLogger.info("Clicked on Cs gui editor flag button");
    }

    /**
     * This method returns the instance of import button
     * 
     * @return the btnVerifyImportFromTranslationMemory
     */
    public WebElement getBtnVerifyImportFromTranslationMemory() {
        return btnVerifyImportFromTranslationMemory;
    }

    /**
     * This method returns the instance of translation memory import button
     * 
     * @return the btnImportFromTranslationMemory
     */
    public WebElement getBtnImportFromTranslationMemory() {
        return btnImportFromTranslationMemory;
    }

    /**
     * This method clicks the import button import from translation memory
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnImportFromTranslationMemory(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getBtnImportFromTranslationMemory()));
        getBtnImportFromTranslationMemory().click();
        CSLogger.info("Clicked on blue  button ");
    }

    /**
     * This method returns the instance of confirm state in translations tab
     * 
     * @return the btnVerifyConfirmTranslationsTab
     */
    public WebElement getBtnVerifyConfirmTranslationsTab() {
        return btnVerifyConfirmTranslationsTab;
    }

    /**
     * This method returns the instance of confirm button
     * 
     * @return the btnConfirm
     */
    public WebElement getBtnConfirm() {
        return btnConfirm;
    }

    /**
     * This method returns the instance of request confirmation button in
     * translations tab
     * 
     * @return the btnVerifyRequestConfirmationTranslationsTab
     */
    public WebElement getBtnVerifyRequestConfirmationTranslationsTab() {
        return btnVerifyRequestConfirmationTranslationsTab;
    }

    /**
     * This method returns the instance of Request confirmation button
     * 
     * @return btnRequestConfirmation
     */
    public WebElement getBtnRequestConfirmation() {
        return btnRequestConfirmation;
    }

    /**
     * This method clicks on the confirm button
     * 
     * @param waitforReload waits for an element to reload
     */
    public void clkBtnConfirm(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getBtnConfirm()));
        getBtnConfirm().click();
        CSLogger.info("Clicked on confirm button");
    }

    /**
     * This method clicks on Request Confirmation button
     * 
     * @param waitForReload waits for an element to relaod
     */
    public void clkBtnRequestConfirmation(WebDriverWait waitForReload) {
        waitForReload.until(
                ExpectedConditions.visibilityOf(getBtnRequestConfirmation()));
        getBtnRequestConfirmation().click();
        CSLogger.info("Clicked on Request confirmation button");
    }

    /**
     * This method returns the instance of save and load next element button
     * 
     * @return the btnSaveAndLoadNextElement
     */
    public WebElement getBtnSaveAndLoadNextElement() {
        return btnSaveAndLoadNextElement;
    }

    /**
     * This method clicks on save and load next element button in translations
     * tab
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnSaveAndLoadNextElement(WebDriverWait waitForReload) {
        clkElement(waitForReload, getBtnSaveAndLoadNextElement());
    }

    /**
     * This method returns the instance of translation text area
     * 
     * @return txtTranslation
     */
    public WebElement getTxtTranslation() {
        return txtTranslation;
    }

    /**
     * This method returns the instance of label
     * 
     * @return txtLabel
     */
    public WebElement getTxtLabel() {
        return txtLabel;
    }

    /**
     * This method returns the instance of properties tab
     * 
     * @return propertiesTab
     */
    public WebElement getPropertiesTab() {
        return propertiesTab;
    }

    /**
     * This method clicks on properties tab
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkPropertiesTab(WebDriverWait waitForReload) {
        clkElement(waitForReload, getPropertiesTab());
        CSLogger.info("Clicked on properties tab");
    }

    /**
     * This method returns the instance of button display object
     * 
     * @return btnDispalayObject
     */
    public WebElement getBtnDisplayObject() {
        return btnDisplayObject;
    }

    /**
     * This method clicks on button display object
     * 
     * @param waitForReload
     */
    public void clkBtnDisplayObject(WebDriverWait waitForReload) {
        clkElement(waitForReload, getBtnDisplayObject());
        CSLogger.info("Clicked on button display object");
    }

    /**
     * This method clicks on translations node
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkTranslationsNode(WebDriverWait waitForReload) {
        clkElement(waitForReload, getTranslationNode());
        CSLogger.info("Clicked on Translations Node");
    }

    /**
     * This method clicks on element
     * 
     * @param waitForReload waits for an element to reload
     * @param element contains webelment
     */
    public void clkElement(WebDriverWait waitForReload, WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.click();
        CSLogger.info("Clicked on element");
    }

    /**
     * This method returns the instance of translation required node
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkTranslationRequiredNode(WebDriverWait waitForReload) {
        clkElement(waitForReload, getTranslationRequiredNode());
        CSLogger.info("Clicked on translation required node");
    }

    /**
     * This method returns the instance of translation required node
     * 
     * @return translationRequiredNode
     */
    public WebElement getTranslationRequiredNode() {
        return translationRequiredNode;
    }

    /**
     * This method returns the instance of translations node
     * 
     * @return translationsNode
     */
    public WebElement getTranslationJobNode() {
        return translationJobNode;
    }

    /**
     * This method returns the instance of btnImport
     * 
     * @return btnImport
     */
    public WebElement getBtnImport() {
        return btnImport;
    }

    /**
     * This method clicks on import button
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnImport(WebDriverWait waitForReload) {
        clkElement(waitForReload, getBtnImport());
        CSLogger.info("Clicked on import button");
    }

    /**
     * This method returns the instance plus button to add product in
     * translation manager
     * 
     * @return the btnPlusToAddProductTranslationManager
     */
    public WebElement getBtnPlusToAddProductTranslationManager() {
        return btnPlusToAddProductTranslationManager;
    }

    /**
     * This method returns the instance of translations tab
     * 
     * @return translationsTab
     */
    public WebElement getTabTranslations() {
        return tabTranslations;
    }

    /**
     * This method clicks on the translations tab
     * 
     * @param waitForReload waits for an element to reloadF
     */
    public void clkTranslationsTab(WebDriverWait waitForReload) {
        clkElement(waitForReload, getTabTranslations());
        CSLogger.info("clicked on the translations tab");
    }

    /**
     * This method clicks plus button to add product in translation manager
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnPlusToAddProductTranlationManager(
            WebDriverWait waitForReload) {
        clkElement(waitForReload, getBtnPlusToAddProductTranslationManager());
        CSLogger.info(
                "Clicked on plus button to add product in translation manager");
    }

    /**
     * This method returns the instance of Available translator field in
     * Translation Job Data collection field
     * 
     * @return the txtTranslationJobAvailableTranslator
     */
    public WebElement getTxtTranslationJobAvailableTranslator() {
        return txtTranslationJobAvailableTranslator;
    }

    /**
     * This method returns the instance of translator field of selected user
     * 
     * @return the txtTranslationJobSelectedTranslatorField
     */
    public WebElement getTxtTranslationJobSelectedTranslatorField() {
        return txtTranslationJobSelectedTranslatorField;
    }

    /**
     * This method returns the instance of selected target language
     * 
     * @return the txtTranslationJobSelectedTargetLanguage
     */
    public WebElement getTxtTranslationJobSelectedTargetLanguage() {
        return txtTranslationJobSelectedTargetLanguage;
    }

    /**
     * This method returns the instance of text box translation job target
     * language
     * 
     * @return the txtTranslationJobTargetLanguage
     */
    public WebElement getTxtTranslationJobAvailableTargetLanguage() {
        return txtTranslationJobAvailableTargetLanguage;
    }

    /**
     * This method returns the instance of data collection tab
     * 
     * @return the dataCollectionTab
     */
    public WebElement getDataCollectionTab() {
        return dataCollectionTab;
    }

    /**
     * This method clicks on data collection tab
     * 
     * @param waitForRelaod waits for an element to reload
     */
    public void clkDataCollectionTab(WebDriverWait waitForReload) {
        clkElement(waitForReload, getDataCollectionTab());
        CSLogger.info("Clicked on data collection tab");
    }

    /**
     * This method returns the instance of translation job label textbox
     * 
     * @return the txtTranslationJobLabel
     */
    public WebElement getTxtTranslationJobLabel() {
        return txtTranslationJobLabel;
    }

    /**
     * This method returns the instance of translation job node
     * 
     * @return translationJobNode
     */
    public WebElement getTranslationNode() {
        return translationsNode;
    }

    /**
     * This method returns the instance of close button
     * 
     * @return btnClose
     */
    public WebElement getBtnClose() {
        return btnClose;
    }

    /**
     * This method clicks on close button of cs portal window
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnClose(WebDriverWait waitForReload) {
        clkElement(waitForReload, getBtnClose());
        CSLogger.info("Clicked on close button of cs portal window");
    }

    /**
     * This method returns the instance of txtAvailableTargetLang text area
     * 
     * @return the txtAvailableTargetLang
     */
    public WebElement getTxtAvailableTargetLang() {
        return txtAvailableTargetLang;
    }

    /**
     * This method returns the instance of txtAvailableSourceLang text area
     * 
     * @return the txtAvailableSourceLang
     */
    public WebElement getTxtAvailableSourceLang() {
        return txtAvailableSourceLang;
    }

    /**
     * This method returns the instance of button for unselecting source
     * languages
     * 
     * @return btnUnselectSourceLanguage
     */
    public WebElement getBtnUnselectAllSourceLanguages() {
        return btnUnselectAllSourceLanguages;
    }

    /**
     * This method returns the instance of button for unselecting target
     * languages
     * 
     * @return btnUnselectTargetLanguage
     */
    public WebElement getBtnUnselectAllTargetLanguages() {
        return btnUnselectAllTargetLanguages;
    }

    /**
     * This method returns the instance of BPM Translation Manger
     * 
     * @return subtiltleBPMTranslationManager
     */
    public WebElement getSubtitleBPMTranslationManager() {
        return subtiltleBPMTranslationManager;
    }

    /**
     * This method returns the instance of applications tab
     * 
     * @return applicationsTab
     */
    public WebElement getApplicationsTab() {
        return applicationsTab;
    }

    /**
     * This method clicks on BPM Translation Manager
     * 
     * @param waitForReload waits for an element to reload
     */

    public void clkSubtiltleBPMTranslationManager(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getSubtitleBPMTranslationManager()));
        getSubtitleBPMTranslationManager().click();
        CSLogger.info("Clicked on BPM Translation Manger");
    }

    /**
     * This method un-selects the given language
     * 
     * @param waitForReload waits for an element to relaod
     * @param element contains instance of button to unselect the language
     */
    public void clkBtbUnselectLanguage(WebDriverWait waitForReload,
            WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.click();
        CSLogger.info("Unselected the given language");
    }

    /**
     * This method clicks on Translation job node
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkTranslationJobNode(WebDriverWait waitForReload) {
        waitForReload
                .until(ExpectedConditions.visibilityOf(getTranslationNode()));
        getTranslationJobNode().click();
        CSLogger.info("Clicked on Translation Job node");
    }

    /**
     * This method clicks on applications tab
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkApplicationsTab(WebDriverWait waitForReload) {
        waitForReload
                .until(ExpectedConditions.visibilityOf(getApplicationsTab()));
        getApplicationsTab().click();
        CSLogger.info("Clicked on Applications tab");
    }

    /**
     * Constructor to initialize elements
     * 
     * @param paramDriver contains instance of browser
     */
    public TranslationManagerPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Settings node page POM elements");
    }
}
