
package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ValuelistManagementPage {

    @FindBy(id = "Settings~ValueRanges@0")
    private WebElement btnValueRangeSettingTreeNode;

    @FindBy(id = "CS_ValueRanges@0_ANCHOR")
    private WebElement btnValuelistTreeNode;

    @FindBy(xpath = "//a[@id='4cf0d5fb1bff03ddda46770e3b547900']/img[1]")
    private WebElement clkSaveButton;

    @FindBy(xpath = "//input[contains(@id,'ValuerangetypeLabel')]")
    private WebElement txtValueRangeLabelDesignation;

    @FindBy(xpath = "//input[contains(@id,'ValuerangetypeCategory')]")
    private WebElement txtValueRangeCategory;

    @FindBy(xpath = "//span[contains(@id,'ValuerangetypeIsLanguageDependant')]")
    private WebElement btnLanguageDependant;

    @FindBy(xpath = "//span[contains(text(),'System Preferences')]")
    private WebElement btnSystemPreferences;

    @FindBy(xpath = "//textarea[contains(@id,'ValuerangetypeDescription')]")
    private WebElement txtValueRangeDescription;

    @FindBy(xpath = "//span[contains(@id,'ValuerangetypeHasIcon')]")
    private WebElement chboxValueRangeIconRepresnt;

    @FindBy(
            xpath = "//span[contains(@id,'ValuerangetypeIsLanguageDependant_GUI')]")
    private WebElement chkboxLanguageDependence;

    @FindBy(xpath = "//a/img[contains(@src,'plus')]")
    private WebElement btnCreateNew;

    @FindBy(xpath = "//input[contains(@id,'ValuerangevalueValue_1')]")
    private WebElement txtEnglish;

    @FindBy(xpath = "//input[@id='Filter_Label']")
    private WebElement txtboxFilterLabel;

    @FindBy(
            xpath = "//table[@class='hidewrap CSAdminList']/tbody//tr[contains(@onclick,'return cs_edit')]/td[1]/input")
    private WebElement chkboxToSelectValueList;

    public ValuelistManagementPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Valuelist node in the Settings Tab.");
    }

    /**
     * This method returns the instance of textbox english for creating new
     * value for value list
     * 
     * @return txtEnglish
     */
    public WebElement getTxtEnglish() {
        return txtEnglish;
    }

    /**
     * This method returns the isntance of create new button
     * 
     * @return btnCreateNew
     */
    public WebElement getBtnCreateNew() {
        return btnCreateNew;
    }

    /**
     * This method clicks on the element
     * 
     * @param waitForReload waits for an element to reload
     * @param element contains webelement to be clicked
     */
    public void clkElement(WebDriverWait waitForReload, WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.click();
    }

    /**
     * This method returns the instance of language dependence checkbox
     * 
     * @return chkboxLanguageDependence
     */
    public WebElement getChkboxLanguageDependence() {
        return chkboxLanguageDependence;
    }

    /**
     * This method returns the instance of value list node
     * 
     * @return the btnValueRangeSettingTreeNode
     */
    public WebElement getBtnValueRangeSettingTreeNode() {
        return btnValueRangeSettingTreeNode;
    }

    /**
     * This method clicks on the value list node
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnValueList(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getBtnValueRangeSettingTreeNode()));
        getBtnValueRangeSettingTreeNode().click();
        CSLogger.info("clicked on value list node under settings node");
    }

    /**
     * This method returns the instance of save button
     * 
     * @return clkSaveButton
     */
    public WebElement getBtnSave() {
        return clkSaveButton;
    }

    /**
     * Clicks on ValueRange setting Node or webElement
     * 
     */
    public void clkBtnValueRangeSettingsNode(WebDriverWait waitForReload) {
        waitForReload.until(
                ExpectedConditions.visibilityOf(getBtnValueListTreeNode()));
        getBtnValueListTreeNode().click();
    }

    /**
     * Returns the WebElement btnValueRangeSettingTreeNode contains the Value
     * list tree node webElement
     * 
     * @return btnValueRangeSettingTreeNode object contains Valuelist tree node
     */
    public WebElement getBtnValueListTreeNode() {
        return btnValuelistTreeNode;
    }

    public WebElement getSystemPreferencesElement() {
        return btnSystemPreferences;
    }

    public WebElement getValueRangeDesignation() {
        return txtValueRangeLabelDesignation;
    }

    public WebElement getValueRangeCategory() {
        return txtValueRangeCategory;
    }

    public WebElement getValueRangeDescription() {

        return txtValueRangeDescription;
    }

    public WebElement getLanguageDependentcheckbox() {
        return btnLanguageDependant;
    }

    public WebElement getIconRepresentationcheckbox() {
        return chboxValueRangeIconRepresnt;
    }

    /**
     * This method will return text box for filter label
     * 
     * @return txtboxFilterLabel
     */
    public WebElement getTxtboxFilterLabel() {
        return txtboxFilterLabel;
    }

    /**
     * This method will return check box for selecting value list from list view
     * 
     * @return chkboxToSelectValueList
     */
    public WebElement getChkboxToSelectValueList() {
        return chkboxToSelectValueList;
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