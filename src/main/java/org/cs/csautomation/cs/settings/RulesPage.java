
package org.cs.csautomation.cs.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RulesPage {

    @FindBy(
            xpath = "//li[@id='StudioWidgetPane_e20aa82bb06b2cd4551ff728f5d58a2e_Title']//div[@class='CSPortalGuiPanelIcon']")
    private WebElement nodeSystemPreferences;

    @FindBy(xpath = "//span[@id='Rules@0']")
    private WebElement nodeRules;

    @FindBy(xpath = "//span[@id='Rules~Data Quality@0']")
    private WebElement nodeDataQuality;

    @FindBy(xpath = "//a/img[contains(@src,'plus')]")
    private WebElement btnCreateNew;

    @FindBy(xpath = "//input[contains(@id,'RuleName')]")
    private WebElement txtName;

    @FindBy(xpath = "//input[contains(@id,'RuleLabel')]")
    private WebElement txtLabel;

    @FindBy(xpath = "//a/img[contains(@src,'save.gif')]")
    private WebElement btnSave;

    @FindBy(xpath = "//span[contains(@id,'RuleIsEnabled_GUI')]")
    private WebElement chkboxEnabled;

    @FindBy(xpath = "//nobr[contains(text(),'Rule Validity')]")
    private WebElement secRuleValidity;

    @FindBy(xpath = "//nobr[contains(text(),'Rule Conditions')]")
    private WebElement secRuleConditions;

    @FindBy(
            xpath = "//div[contains(text(),'Class Filter')]/../../td/div/div[2]/img")
    private WebElement btnPlusClassFilter;

    @FindBy(xpath = "//span[@id='ConfigurationLinks@0']")
    private WebElement nodeClassesInDialogWindow;

    @FindBy(xpath = "//div[contains(@class,'CSPortalWindowContent')]")
    private WebElement csPortalWindowContent;

    @FindBy(xpath = "//div[@class='CSPortalWindow']")
    private WebElement csPortalWindow;

    @FindBy(xpath = "//a/img[contains(@src,'plus')]")
    private WebElement btnAddRuleConditions;

    @FindBy(xpath = "//input[contains(@id,'RuleconditionName')]")
    private WebElement txtRuleConditionName;

    @FindBy(xpath = "//input[contains(@id,'RuleconditionLabel')]")
    private WebElement txtRuleConditionLabel;

    @FindBy(xpath = "//img[@class='CSGuiEditorChooserIconLeft']")
    private WebElement btnGuiEditorChooserIcon;

    @FindBy(id = "CSGUI_MODALDIALOG_OKBUTTON")
    private WebElement btnCsGuiModalDialogOkButton;

    @FindBy(xpath = "//a[@class='CSPortalWindowCloser']")
    private WebElement btnClose;

    /**
     * Returns the instance of cs portal window
     * 
     * @return csPortalWindow
     */
    public WebElement getCsPortalWindow() {
        return csPortalWindow;
    }

    /**
     * Returns the instance of close button
     * 
     * @return btnClose
     */
    public WebElement getBtnClose() {
        return btnClose;
    }

    /**
     * Returns the instance of ok button
     * 
     * @return btnCsGuiModalDialogOkButton
     */
    public WebElement getBtnCsGuiModalDialogOkButton() {
        return btnCsGuiModalDialogOkButton;
    }

    /**
     * Returns the instance of gui editor chooser icon
     * 
     * @return btnGuiEditorChooserIcon
     */
    public WebElement getBtnGuiEditorChooserIcon() {
        return btnGuiEditorChooserIcon;

    }

    /**
     * Returns the instance of rule condition name textbox
     * 
     * @return txtRuleConditionName
     */
    public WebElement getTxtRuleConditionName() {
        return txtRuleConditionName;
    }

    /**
     * Returns the instance of Rule condition label textbox
     * 
     * @return txtRuleConditionLabel
     */
    public WebElement getTxtRuleConditionLabel() {
        return txtRuleConditionLabel;
    }

    /**
     * Returns the instance of add button in rule conditions
     * 
     * @return btnAddRuleConditions
     */
    public WebElement getBtnAddRuleConditions() {
        return btnAddRuleConditions;
    }

    /**
     * Returns the instance of rule conditions section
     * 
     * @return secRuleConditions
     */
    public WebElement getSecRuleConditions() {
        return secRuleConditions;
    }

    /**
     * Returns the instance of portal window content
     * 
     * @return csPortalWindowContent
     */
    public WebElement getCsPortalWindowContent() {
        return csPortalWindowContent;
    }

    /**
     * Returns the instance of Classes node in dialog window
     * 
     * @return nodeClassesInDialogWindow
     */
    public WebElement getNodeClassesInDialogWindow() {
        return nodeClassesInDialogWindow;
    }

    /**
     * Returns the instance of plus button to add class to class filter
     * 
     * @return btnPlusClassFilter
     */
    public WebElement getBtnPlusClassFilter() {
        return btnPlusClassFilter;
    }

    /**
     * Returns the instance of rule validity section
     * 
     * @return secRuleValidity
     */
    public WebElement getSecRuleValidity() {
        return secRuleValidity;
    }

    /**
     * Returns the instance of enabled checkbox
     * 
     * @return chkboxEnabled
     */
    public WebElement getChkboxEnabled() {
        return chkboxEnabled;
    }

    /**
     * Returns the instance of save button
     * 
     * @return btnSave
     */
    public WebElement getBtnSave() {
        return btnSave;
    }

    /**
     * Returns the instance of label textbox
     * 
     * @return txtLabelF
     */
    public WebElement getTxtLabel() {
        return txtLabel;
    }

    /**
     * Returns the instance of text name
     * 
     * @return txtName
     */
    public WebElement getTxtName() {
        return txtName;
    }

    /**
     * Returns the instance of create new
     * 
     * @return btnCreateNew
     */
    public WebElement getBtnCreateNew() {
        return btnCreateNew;
    }

    /**
     * Returns the instance of data quality node
     * 
     * @return nodeDataQuality
     */
    public WebElement getNodeDataQuality() {
        return nodeDataQuality;
    }

    /**
     * Return the instance of system preferences node
     * 
     * @return nodeSystemPreferences
     */
    public WebElement getNodeSystemPreferences() {
        return nodeSystemPreferences;
    }

    /**
     * Returns the instance of rules node
     * 
     * @return nodeRules
     */
    public WebElement getNodeRules() {
        return nodeRules;
    }

    public void clkElement(WebDriverWait waitForReload, WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.click();
    }

    public RulesPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
    }
}
