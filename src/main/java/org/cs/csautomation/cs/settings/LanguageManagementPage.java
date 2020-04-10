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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains the all web Elements of the Language pages
 * 
 * @author CSAutomation Team
 *
 */
public class LanguageManagementPage {

    @FindBy(id = "CS_Languages@0_ANCHOR")
    private WebElement btnLanguageSettingTreeNode;

    @FindBy(xpath = "//span[contains(text(),'System Preferences')]")
    private WebElement btnSystemPreferences;

    @FindBy(xpath = "//a/img[contains(@src,'plus')]")
    private WebElement btnCreateNew;

    @FindBy(xpath = "//input[contains(@id,'LanguageShortName')]")
    private WebElement txtLanguageShortName;

    @FindBy(xpath = "//input[contains(@id,'LanguageFullName')]")
    private WebElement txtLanguageFullName;

    @FindBy(xpath = "//input[contains(@id,'LanguageDictonarySuffix')]")
    private WebElement txtLanguageDictonarySuffix;

    @FindBy(xpath = "//select[contains(@id,'LanguageImageUrl')]")
    private WebElement drpdwnLanguageImageUrl;

    @FindBy(xpath = "//span[contains(@id,'LanguageAvailable_GUI')]")
    private WebElement btnLanguageAvailable;

    @FindBy(xpath = "//span[@id='Installation@0']")
    private WebElement btnInstalationTreeNode;

    @FindBy(xpath = "//span[contains(text(),' Complete Update')]")
    private WebElement btnCompleteUpdate;

    @FindBy(
            xpath = "//li[contains(text(),'The data model was updated successfully')]")
    private WebElement messsageUpdateDataModel;

    @FindBy(xpath = "//li[@id='CSPortalLinkPortalOptions']")
    private WebElement btnCSPortalLinkPortalOptions;

    @FindBy(xpath = "//td[contains(text(),'admin')]")
    private WebElement btnAdminOption;

    @FindBy(xpath = "//a/img[contains(@src,'user.png')]")
    private WebElement btnUser;

    @FindBy(xpath = "//nobr[contains(text(),'Administration')]")
    private WebElement btnAdministrationTab;

    @FindBy(xpath = "//div//select[contains(@id,'UserLanguage')]")
    private WebElement drpdwnUserLanguage;

    /*
     * Constructor method to initialize the elements
     * 
     * @param WebDriver paramDriver
     */
    public LanguageManagementPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Color node in the Settings Tab.");
    }

    /*
     * This method returns the WebElement of the Language Node of the Settings
     * -> System Preferences-> Languages
     * 
     * @return WebElement of the Languages tree node.
     */
    public WebElement getLanguageTreeNode() {
        return btnLanguageSettingTreeNode;
    }

    /*
     * This method return the system Preferences element of the setting tab
     * 
     * @return WebElement of the System preferences
     */
    public WebElement getSystemPreferencesElement() {
        return btnSystemPreferences;
    }

    /*
     * This method return the create new option of the colors middle pane header
     * option
     * 
     * @return WebElement of the create new button
     */
    public WebElement getCreateNew() {
        return btnCreateNew;
    }

    /*
     * This method return the web element of the Language short name (field on
     * UI is Abbreviation)
     * 
     * @return txtLanguageShortName WebElement of the text box of the
     * Designation
     */
    public WebElement getLanguageAbbreviation() {
        return txtLanguageShortName;
    }

    /*
     * This method return the web element of the Language full name text box
     * 
     * @return txtLanguageFullName WebElement of full name text box
     */
    public WebElement getLanguageFullName() {
        return txtLanguageFullName;
    }

    /*
     * This method return the web element of the Color picker of the textbox
     * input box
     * 
     * @return txtLanguageDictonarySuffix WebElement of Dictionary Suffix
     */
    public WebElement getLanguageDictonarySuffix() {
        return txtLanguageDictonarySuffix;
    }

    /*
     * This method return the web element of the Image selection drop down of
     * the Language creation window
     * 
     * @return drpdwnLanguageImageUrl WebElement of Image selection drop down
     * picker
     */
    public WebElement getLanguageImageSelection() {
        return drpdwnLanguageImageUrl;
    }

    /*
     * This method return the web element of the language available checkbox
     * 
     * @return btnLanguageAvailable WebElement of language available checkbox
     */
    public WebElement getLanguageAvailableButton() {
        return btnLanguageAvailable;
    }

    /*
     * This method return the web element of the Installation Tree node
     * 
     * @return btnInstalationTreeNode WebElement of Installation tree node
     */
    public WebElement getInstallationTreeNode() {
        return btnInstalationTreeNode;
    }

    /*
     * This method return the web element of the Installation->updateDatamodel->
     * complete update
     * 
     * @return btnCompleteUpdate WebElement of Complete update
     */
    public WebElement getCompleteUpdateLink() {
        return btnCompleteUpdate;
    }

    /*
     * This method return the web element of the Success message after update
     * data model successfully completed.
     * 
     * @return btnCompleteUpdate WebElement of Complete update
     */
    public WebElement getSuccessUpdateDataModel() {
        return messsageUpdateDataModel;
    }

    /*
     * This method return the web element of CSPotalOptionsLink link button This
     * is the top left corner icon
     * 
     * @return btnCSPortalLinkPortalOptions WebElement of
     * CSPortalLinkPortalOptions
     */
    public WebElement getCSPotalOptionsLink() {
        return btnCSPortalLinkPortalOptions;
    }

    /*
     * This opens the CSPotalOptionsLink it opens the popup. WebElement of the
     * Admin option
     *
     * @return btnAdminOption WebElement of CSPortalLink popup Admin option
     */
    public WebElement getAdminOptionPortal() {
        return btnAdminOption;
    }

    /*
     * This opens the CSPotalOptionsLink it opens the popup. WebElement of the
     * Admin option
     *
     * @return btnUser WebElement of CSPortalLink popup Admin option
     */
    public WebElement getUserPopUpOption() {
        return btnUser;
    }

    /*
     * This option is of the Administration tab of the User window.
     * 
     * @return btnAdministrationTab WebElement of Administration tab of user
     * window
     */
    public WebElement getUserAdministrationTab() {
        return btnAdministrationTab;
    }

    /*
     * This method return the WebElement of users Language selection
     * 
     * @return drpdwnUserLanguage WebElement Language selection
     */
    public WebElement getUserLanguage() {
        return drpdwnUserLanguage;
    }

    /*
     * This method is used to enter the values into the text box
     * 
     * @param waitForReload WebDriverWait object
     * 
     * @param WebElement element of the text box
     * 
     * @param String sTextValue value which needs to add
     */
    public void enterValue(WebDriverWait waitForReload, WebElement element,
            String sTextValue) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.clear();
        element.sendKeys(sTextValue);
    }

    /*
     * This method is used to select value from drop down
     * 
     * @param waitForReload WebDriverWait object
     * 
     * @param WebElement drpdwnElement of the drop down box
     * 
     * @param String value that needs to select
     */
    public void selectValue(WebElement drpdwnElement, String value) {
        drpdwnElement.click();
        Select element = new Select(drpdwnElement);
        element.selectByVisibleText(value);
    }

    /*
     * This method is used to click on the create new window option
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clickOnCreateNew(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getCreateNew());
        getCreateNew().click();
    }
}
