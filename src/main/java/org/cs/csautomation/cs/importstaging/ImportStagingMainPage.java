/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.importstaging;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
/**
 * Class contains the WebElements of the import database config settings
 * @author CSAutomation Team
 *
 */
public class ImportStagingMainPage {

    @FindBy(xpath = "//input[contains(@id, 'ImportconnectionLabel')]")
    private WebElement txtImportconnLabel;

    @FindBy(xpath = "//input[contains(@id, 'ImportconnectionDatabaseUser')]")
    private WebElement txtImportconnDbUser;

    @FindBy(
            xpath = "//input[contains(@id, 'ImportconnectionDatabasePassword')]")
    private WebElement txtImportconnDbPassword;

    @FindBy(xpath = "//input[contains(@id, 'ImportconnectionDatabaseName')]")
    private WebElement txtImportconnDbName;

    @FindBy(
            xpath = "//input[contains(@id, 'ImportconnectionInboxFolder__Label')]")
    private WebElement txtImportconnInboxFolder;

    @FindBy(xpath = "//select[contains(@id, 'ImportconnectionExecutablePath')]")
    private WebElement drpdwnImportconnExecutablePath;

    @FindBy(xpath = "//input[@id='userInput']")
    private WebElement txtConfirmPasswordInput;

    @FindBy(xpath = "//input[@id='CSGUI_MODALDIALOG_OKBUTTON']")
    private WebElement btnConfirmPassword;

    @FindBy(xpath = "//div[contains(text(),'Database Connection')]")
    private WebElement btnDbConnSection;

    @FindBy(xpath = "//span[contains(text(),'Database Password')]")
    private WebElement lblDbPassLabel;

    @FindBy(xpath = "//nobr[contains(text(),'ImportSettings')]")
    private WebElement tabImportSettings;

    @FindBy(xpath = "//div[contains(text(),'Products')]")
    private WebElement sectionProducts;

    @FindBy(xpath = "//img[@class='CSGuiEditorChooserIconLeft']")
    private WebElement txtInboxFolderSelect;

    @FindBy(
            xpath = "//a[@id='toolbar_button_save']//img[@class='CSGuiToolbarButtonImage']")
    private WebElement btnImportSave;
    
    @FindBy(xpath = "//a/img[contains(@src,'src=recreateDB.png')]")
    private WebElement btnRecreateDatabase;

    /*
     * Constructor method initializes the needed elements
     * @param paramDriver 
     */
    public ImportStagingMainPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Import staging Instances of page.");
    }

    /*
     * This method will return the label field of Import staging job
     * @return WebElement txtImportconnectionLabel
     */
    public WebElement getImportConnectionLabel() {
        return txtImportconnLabel;
    }

    /*
     * This method will return the Import database user field
     * @return WebElemnt txtImportconnectionDatabaseUser
     */
    public WebElement getImportconnectionDatabaseUser() {
        return txtImportconnDbUser;
    }

    /*
     * This method will return the database password.
     * @return WebElement txtImportconnectionDatabasePassword
     */
    public WebElement getImportconnectionDatabasePassword() {
        return txtImportconnDbPassword;
    }

    /*
     * This method will return the database Name.
     * @return WebElement txtImportconnectionDatabaseName
     */
    public WebElement getImportconnectionDatabaseName() {
        return txtImportconnDbName;
    }

    /*
     * This method will return the Inbox folder selection element
     * @return WebElement txtImportconnectionInboxFolder
     */
    public WebElement getImportconnectionInboxFolder() {
        return txtImportconnInboxFolder;
    }

    /*
     * This method will return the executable selection box webelement
     * @return WebElement drpdwnImportconnExecutablePath
     */
    public WebElement getImportconnExecutable() {
        return drpdwnImportconnExecutablePath;
    }

    /*
     * This method will return the verify password WebElement
     * @return WebElement txtConfirmPasswordInput
     */
    public WebElement getverifyPasswordInput() {
        return txtConfirmPasswordInput;
    }

    /*
     * This method will return the Database connection section to enter the DB
     * Credentials
     * @return WebElement btnDbConnSection
     */
    public WebElement getDbConnSection() {
        return btnDbConnSection;
    }

    /*
     * This method will return the Db password label field to click outside of
     * the password text box
     * @return WebElement lblDbPassLabels
     */
    public WebElement getDbPasswordLabel() {
        return lblDbPassLabel;
    }

    /*
     * This method will return the ImportSettings Tab WebElement
     * @return WebElement tabImportSettings
     */
    public WebElement geImportSettingsTab() {
        return tabImportSettings;
    }

    /*
     * This method will return the products section details under the import 
     * settings Tab
     * @return WebElement sectionProducts
     */
    public WebElement getProductsTab() {
        return sectionProducts;
    }

    /*
     * This method will return the Inbox folder selection webElement
     * @return WebElement txtInboxFolderSelect
     */
    public WebElement getInboxFolderSelection() {
        return txtInboxFolderSelect;
    }

    /*
     * This method will return the Import save button webElement
     * @return WebElement btnImportSave
     */
    public WebElement getImportSaveButton() {
        return btnImportSave;
    }

    /*
     * This method will enter the values in the text box.
     * @param waitForReload object of WebDriverWait
     * @param element is the WebElement of the text box field
     * @param value string consist of the data which needs to be entered
     */
    public void enterTextFieldValues(WebDriverWait waitForReload,
            WebElement element, String value) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.clear();
        element.sendKeys(value);
    }

    /*
     * This method used to click on the OK button of the Verify password Window
     * @param waitForReload 
     */
    public void clickOkButton(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, btnConfirmPassword);
        btnConfirmPassword.click();
    }
    
    /*
     * This method will return the Import recreate database button webElement
     * @return WebElement btnRecreateDatabase
     */
    public WebElement getRecreateDbButton() {
        return btnRecreateDatabase;
    }
}
