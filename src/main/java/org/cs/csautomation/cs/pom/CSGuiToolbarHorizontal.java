/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains WebElements and methods of header tool bar.
 * 
 * @author CSAutomation Team
 *
 */
public class CSGuiToolbarHorizontal {

    private WebDriver  browserDriverInstance;

    @FindBy(xpath = "//a/img[contains(@src,'plus')]")
    private WebElement btnCSGuiToolbarCreateNew;

    @FindBy(xpath = "//img[contains(@src,'refresh.gif')]/parent::a")
    private WebElement btnCSGuiToolbarRefresh;

    @FindBy(
            xpath = "//img[@class='CSGuiToolbarButtonImage'][contains(@src,'save.gif')]")
    private WebElement btnCSGuiToolbarSave;

    @FindBy(xpath = "//img[contains(@src,'checkin.gif')]/parent::a")
    private WebElement btnCSGuiToolbarCheckIn;

    @FindBy(xpath = "(//a[@title='Checkout'])")
    private WebElement btnCSGuiToolbarCheckOut;

    @FindBy(
            xpath = "//img[@class='CSGuiToolbarButtonImage']"
                    + "[contains(@src,'cross_reference.png')]")
    private WebElement btnCSGuiToolbarUsages;

    @FindBy(xpath = "//img[contains(@src,'delete.gif')]/parent::td/parent::tr")
    private WebElement btnCSGuiToolbarDelete;

    @FindBy(xpath = "(//a[@title='Import CSV or Excel file'])")
    private WebElement btnImport;

    @FindBy(xpath = "(//a[@title='Export'])")
    private WebElement btnExport;

    @FindBy(id = "toolbarSearchInput")
    private WebElement txtSearchBox;

    @FindBy(xpath = "//a/img[contains(@src,'studiomenu')]")
    private WebElement btnShowMoreOption;

    @FindBy(
            xpath = "//input[@id='toolbarSearchInput']/parent::label/following-sibling::a[1]")
    private WebElement btnAdvanceSearch;

    @FindBy(
            xpath = "//tr[@class='ListHeader DarkListHeader']/td/table/tbody/tr/td/span/span/img[1]")
    private WebElement btnChangeLanguage;

    @FindBy(xpath = "(//img[contains(@src,'new')])[3]")
    private WebElement btnCSGuiToolbarNewState;

    @FindBy(
            xpath = "//img[@class='CSGuiToolbarButtonImage'][contains(@src,'add.png')]")
    private WebElement btnCSGuiToolbarAddAllValues;

    @FindBy(xpath = "//a//img[contains(@src,'play.gif')]/..")
    private WebElement btnCSGuiToolbarRunNow;

    @FindBy(xpath = "//a/img[contains(@src,'play.png')]/..")
    private WebElement btnCSGuiToolbarRunActiveScript;

    @FindBy(xpath = "//a[@id='toolbar_button_checkin']")
    private WebElement btnProductCheckIn;

    @FindBy(xpath = "//a[@id='toolbar_button_checkout']")
    private WebElement btnProductCheckOut;

    @FindBy(
            xpath = "//input[@id='CSGuiEditorDialogIDInput']")
    private WebElement lblProductId;

    @FindBy(xpath = "//a//img[contains(@src,'listview')]/..")
    private WebElement btnListView;

    @FindBy(xpath = "//a//img[contains(@src,'thumbview')]/..")
    private WebElement btnThumbnailView;

    @FindBy(xpath = "//img[contains(@src,'filter.gif')]/parent::a")
    private WebElement btnFilter;

    @FindBy(id = "Filter_Label")
    private WebElement txtFilterBar;

    @FindBy(xpath = "//a/img[contains(@src,'nofilter.gif')]")
    private WebElement btnNoFilter;

    @FindBy(xpath = "//a/img[contains(@src,'upload.gif')]")
    private WebElement btnUploadFile;

    @FindBy(xpath = "//td[contains(text(),'Search Portal')]")
    private WebElement btnSearchPortal;

    @FindBy(xpath = "//td[contains(text(),'Color')]")
    private WebElement btnColor;

    @FindBy(xpath = " //td[contains(text(),'Action')]")
    private WebElement drpDwnCSGuiToolbarActions;

    @FindBy(xpath = "//td[contains(text(),'State')]")
    private WebElement drpDwnCSGuiToolbarState;

    @FindBy(xpath = "//td[contains(text(),'Workflow')]")
    private WebElement drpDwnCSGuiToolbarWorkflow;

    @FindBy(xpath = "//td[contains(text(),'Automation')]")
    private WebElement drpDwnCSGuiToolbarAutomation;

    @FindBy(xpath = "//input[@id='CSGuiEditorDialogIDInput']")
    private WebElement editorViewId;

    @FindBy(xpath = "//button[@data-original-title='Switch to Studio List']")
    private WebElement btnSwitchToStudioList;

    @FindBy(xpath = "//img[@alt='Filter View']")
    private WebElement btnFilterView;

    public CSGuiToolbarHorizontal(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized CSGuiToolbarHorizontal page POM elements.");
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * Returns the WebElement btnCSGuiToolbarCheckIn
     * 
     * @return btnCSGuiToolbarCheckIn CSGuiToolbarHorizontal
     */
    public WebElement getBtnCSGuiToolbarCheckIn() {
        return btnCSGuiToolbarCheckIn;
    }

    /**
     * Returns the WebElement btnCSGuiToolbarCreateNew
     * 
     * @return btnCSGuiToolbarCreateNew CSGuiToolbarHorizontal
     */
    public WebElement getBtnCSGuiToolbarCreateNew() {
        return btnCSGuiToolbarCreateNew;
    }

    /**
     * Returns the WebElement btnCSGuiToolbarRefresh
     * 
     * @return btnCSGuiToolbarRefresh CSGuiToolbarHorizontal
     */
    public WebElement getBtnCSGuiToolbarRefresh() {
        return btnCSGuiToolbarRefresh;
    }

    /**
     * Returns the WebElement btnCSGuiToolbarSave
     * 
     * @return btnCSGuiToolbarSave CSGuiToolbarHorizontal
     */
    public WebElement getBtnCSGuiToolbarSave() {
        return btnCSGuiToolbarSave;
    }

    /**
     * Returns the WebElement btnCSGuiToolbarUsages
     * 
     * @return btnCSGuiToolbarUsages
     */
    public WebElement getBtnCSGuiToolbarUsages() {
        return btnCSGuiToolbarUsages;
    }

    /**
     * Returns the WebElement btnImport
     * 
     * @return btnImport
     */
    public WebElement getBtnImport() {
        return btnImport;
    }

    /**
     * Returns the WebElement txtSearchBox
     * 
     * @return txtSearchBox
     */
    public WebElement getTxtSearchBox() {
        return txtSearchBox;
    }

    /**
     * Returns the WebElement Button more option
     * 
     * @return btnMoreOption
     */
    public WebElement getBtnShowMoreOption() {
        return btnShowMoreOption;
    }

    /**
     * Returns the WebElement btnAdvanceSearch
     * 
     * @return btnAdvanceSearch
     */
    public WebElement getBtnAdvanceSearch() {
        return btnAdvanceSearch;
    }

    /**
     * Returns the WebElement btnProductCheckIn
     * 
     * @return btnProductCheckIn CSGuiToolbarHorizontal
     */
    public WebElement getBtnProductCheckIn() {
        return btnProductCheckIn;
    }

    /**
     * Returns the WebElement btnProductCheckOut
     * 
     * @return btnProductCheckOut CSGuiToolbarHorizontal
     */
    public WebElement getBtnProductCheckOut() {
        return btnProductCheckOut;
    }

    /**
     * This method waits for the invisibility of webElement
     * btnCSGuiToolbarCreateNew
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void waitForBtnCSGuiToolbarCreateNew(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//a[@id='c2b72d6cf2fde35c2f23ba60d5a8ef0a']"
                        + "//img[@class='CSGuiToolbarButtonImage']")));
        CSLogger.info("Waiting completed for the invisibilty of create New ");
    }

    /**
     * Clicks on Create New button
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnCSGuiToolbarCreateNew(WebDriverWait waitForReload) {
        waitForBtnCSGuiToolbarCreateNew(waitForReload);
        CSLogger.info("waiting completed for create new ");
        getBtnCSGuiToolbarCreateNew().click();
        CSLogger.info("clicked on Create New ");
    }

    /**
     * Clicks on Save Button
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnCSGuiToolbarSave(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnCSGuiToolbarSave());
        getBtnCSGuiToolbarSave().click();
        CSLogger.info("clicked on save button");
    }

    /**
     * This method performs Check-In operation.
     * 
     * @param waitForReload
     */
    public void clkBtnCSGuiToolbarCheckIn(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        waitForBtnCSGuiToolbarCreateNew(waitForReload);
        CSLogger.info("waiting completed for check In");
        getBtnCSGuiToolbarCheckIn().click();
        CSLogger.info("clicked on Check In");
    }

    /**
     * This method performs Check-Out operation.
     * 
     * @param waitForReload
     */
    public void clkBtnCSGuiToolbarCheckOut(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        waitForBtnCSGuiToolbarCreateNew(waitForReload);
        CSLogger.info("waiting completed for ckeck out ");
        getBtnCSGuiToolbarCheckOut().click();
        CSLogger.info("clicked on Check Out");
    }

    /**
     * Getter method for btnCSGuiToolbarCheckOut
     * 
     * @return btnCSGuiToolbarCheckOut
     */
    public WebElement getBtnCSGuiToolbarCheckOut() {
        return btnCSGuiToolbarCheckOut;
    }

    /**
     * Setter method for setBtnCSGuiToolbarCheckIn
     * 
     * @param setBtnCSGuiToolbarCheckIn
     */
    public void setBtnCSGuiToolbarCheckIn(WebElement btnCSGuiToolbarCheckIn) {
        this.btnCSGuiToolbarCheckIn = btnCSGuiToolbarCheckIn;
    }

    /**
     * Getter for btnExport
     * 
     * @return WebElement object
     */
    public WebElement getBtnExport() {
        return btnExport;
    }

    /**
     * Setter for btnExport
     * 
     * @param btnExport WebElement object
     */
    public void setBtnExport(WebElement btnExport) {
        this.btnExport = btnExport;
    }

    /**
     * Returns the WebElement btnAdvanceSearch
     * 
     * @return WebElement btnAdvanceSearch
     */
    public WebElement getBtnChangeLanguage() {
        return btnChangeLanguage;
    }

    /**
     * Clicks on CSGuiToolbarUsages button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkBtnCSGuiToolbarUsages(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnCSGuiToolbarUsages());
        getBtnCSGuiToolbarUsages().click();
        CSLogger.info("clicked on  usages toolbar button");
    }

    /**
     * Getter method for btnCSGuiToolbarDelete delete option of the editor view
     * 
     * @return btnCSGuiToolbarDelete
     */
    public WebElement getBtnCSGuiToolbarDelete() {
        return btnCSGuiToolbarDelete;
    }

    /**
     * Returns the WebElement btnCSGuiToolbarAddAllValues
     * 
     * @return btnCSGuiToolbarAddAllValues CSGuiToolbarHorizontal
     */
    public WebElement getBtnCSGuiToolbarAddAllValues() {
        return btnCSGuiToolbarAddAllValues;
    }

    /**
     * Clicks on editor window delete Button
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnCSGuiToolbarDelete(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnCSGuiToolbarDelete());
        getBtnCSGuiToolbarDelete().click();
        CSLogger.info("clicked on toolbar delete button");
    }

    /**
     * Clicks on Import button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkBtnImport(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnImport());
        getBtnImport().click();
        CSLogger.info("clicked on Import toolbar button");
    }

    /**
     * Enters the given text in search box
     * 
     * @param waitForReload WebDriverWait object
     * @param searchText String object containing search text
     */
    public void enterTextInSearchBox(WebDriverWait waitForReload,
            String searchText) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload, getTxtSearchBox());
        getTxtSearchBox().click();
        getTxtSearchBox().clear();
        getTxtSearchBox().sendKeys(searchText);
        CSUtility.tempMethodForThreadSleep(1000);
        getTxtSearchBox().sendKeys(Keys.ENTER);
        CSLogger.info("Entered text  " + searchText + "in search box");
    }

    /**
     * Clicks on Open Advance Search button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnAdvanceSearch(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAdvanceSearch());
        getBtnAdvanceSearch().click();
        CSLogger.info("Clicked on Open Advance Search  button");
    }

    /**
     * Clicks on CSGuiEditorFlag button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnChangeLanguage(WebDriverWait waitForReload) {
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnChangeLanguage());
        getBtnChangeLanguage().click();
        CSLogger.info("Clicked on CSGuiEditorFlag button ");
    }

    /**
     * Clicks on New state button.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkBtnCSGuiToolbarNewState(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnCSGuiToolbarCreateNew());
        getBtnCSGuiToolbarCreateNew().click();
        CSLogger.info("Clicked on New state toolbar button ");
    }

    /**
     * Clicks on 'Show More Option' button.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkBtnShowMoreOption(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnShowMoreOption());
        getBtnShowMoreOption().click();
        CSLogger.info("Clicked on show more options toolbar button ");
    }

    /**
     * Clicks on Refresh button.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkBtnCSGuiToolbarRefresh(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnCSGuiToolbarRefresh());
        getBtnCSGuiToolbarRefresh().click();
        CSLogger.info("Clicked on Refresh toolbar button ");
    }

    /**
     * Returns the WebElement of toolbar menu 'run now' to execute automation.
     * 
     * @return WebElement btnCSGuiToolbarRunNow.
     */
    public WebElement getBtnCSGuiToolbarRunNow() {
        return btnCSGuiToolbarRunNow;
    }

    /**
     * Clicks on Run now toolbar button to execute automation.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkBtnCSGuiToolbarRunNow(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnCSGuiToolbarRunNow());
        getBtnCSGuiToolbarRunNow().click();
        CSLogger.info("Clicked on run now toolbar button ");
    }

    /**
     * Returns the tool bar button to execute the active script.
     * 
     * @return WebElement btnCSGuiToolbarRunActiveScript.
     */
    public WebElement getBtnCSGuiToolbarRunActiveScript() {
        return btnCSGuiToolbarRunActiveScript;
    }

    /**
     * Clicks on Run now toolbar button to execute active script.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkBtnCSGuiToolbarRunActiveScript(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnCSGuiToolbarRunActiveScript());
        getBtnCSGuiToolbarRunActiveScript().click();
        CSLogger.info("Clicked on run active script tool bar button ");
    }

    /**
     * This method performs Check-In operation.
     * 
     * @param waitForReload
     */
    public void clkBtnProductCheckIn(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnProductCheckIn());
        getBtnProductCheckIn().click();
        CSLogger.info("clicked on Check In");
    }

    /**
     * This method performs Check-In operation.
     * 
     * @param waitForReload
     */
    public void clkBtnProductCheckOut(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnProductCheckOut());
        getBtnProductCheckOut().click();
        CSLogger.info("clicked on Check Out");
    }

    /**
     * Getter for lblProductId
     * 
     * @return the lblProductId
     */
    public WebElement getLblProductId() {
        return lblProductId;
    }

    /**
     * Setter for lblProductId
     * 
     * @param lblProductId the lblProductId to set
     */
    public void setLblProductId(WebElement lblProductId) {
        this.lblProductId = lblProductId;
    }

    /**
     * Returns the list view tool bar button.
     * 
     * @return WebElement btnListView.
     */
    public WebElement getBtnListView() {
        return btnListView;
    }

    /**
     * This method clicks on list view button.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkBtnListView(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnListView());
        getBtnListView().click();
        CSLogger.info("Clicked on list view button");
    }

    /**
     * Returns the 'Thumb nail' view tool bar button.
     * 
     * @return WebElement btnThumbnailView.
     */
    public WebElement getBtnThumbnailView() {
        return btnThumbnailView;
    }

    /**
     * This method clicks on thumb nail view button.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkBtnThumbnailView(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnThumbnailView());
        getBtnThumbnailView().click();
        CSLogger.info("Clicked on thumbnail view button");
    }

    /**
     * Returns the WebElement Display / hide filter bar button.
     * 
     * @return WebElement btnFilterBar.
     */
    public WebElement getBtnFilter() {
        return btnFilter;
    }

    /**
     * Clicks on Display/hide filter bar horizontal button.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkOnBtnFilter(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnFilter());
        getBtnFilter().click();
        CSLogger.info("Clicked on filter horizontal button");
    }

    /**
     * Returns the filter bar.
     * 
     * @return WebElement txtFilterBar.
     */
    public WebElement getTxtFilterBar() {
        return txtFilterBar;
    }

    /**
     * Enters given text into filter bar and hits enter button.
     * 
     * @param waitForReload WebDriverWait object.
     * @param data String object contains data to be entered into filter bar.
     */
    public void enterDataInFilterBar(WebDriverWait waitForReload, String data) {
        clkOnBtnFilter(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, getTxtFilterBar());
        getTxtFilterBar().clear();
        getTxtFilterBar().sendKeys(data);
        getTxtFilterBar().sendKeys(Keys.ENTER);
        CSLogger.info("Entered text  " + data + "in filter bar");
    }

    /**
     * Returns the tool bar WebElement btnNoFilter.
     * 
     * @return WebElement btnNoFilter.
     */
    public WebElement getbtnNoFilter() {
        return btnNoFilter;
    }

    /**
     * Clicks on remove filter horizontal button.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkOnBtnNoFilter(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getbtnNoFilter());
        getbtnNoFilter().click();
        CSLogger.info("Clicks on remove filter horizontal button");
    }

    /**
     * Clicks on Import button.
     * 
     * @param waitForReload WebDriverWait object.
     */
    public void clkOnBtnImport(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnImport());
        getBtnImport().click();
        CSLogger.info("Clicks on Import button");
    }

    /**
     * This method will return upload file button
     * 
     * @return btnUploadFile
     */
    public WebElement getBtnUploadFile() {
        return btnUploadFile;
    }

    /**
     * This method will return show more options button
     * 
     * @return btnShowMoreOptions
     */
    public WebElement getBtnSearchPortal() {
        return btnSearchPortal;
    }

    /**
     * Clicks on given tool bar element
     * 
     * @param waitForReload WebDriverWait object.
     * @param element WebElement element to be clicked.
     * @param label String object contains element name.
     */
    public void clkOnWebElement(WebDriverWait waitForReload, WebElement element,
            String label) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        CSLogger.info("Clicked on toolbar element " + label);
    }

    /**
     * This method will return the option color from CS GUI toolbar sub menu
     * 
     * @return btnColor
     */
    public WebElement getBtnColor() {
        return btnColor;
    }

    /**
     * This method will return the element for drop down Action in showmore
     * options menu
     * 
     * @return drpDwnCSGuiToolbarActions
     */
    public WebElement getDrpDwnCSGuiToolbarActions() {
        return drpDwnCSGuiToolbarActions;
    }

    /**
     * This method will return the element for drop down State in showmore
     * options menu
     * 
     * @return drpDwnCSGuiToolbarState
     */
    public WebElement getDrpDwnCSGuiToolbarState() {
        return drpDwnCSGuiToolbarState;
    }

    /**
     * This method will return the element for drop down Workflow in showmore
     * options menu
     * 
     * @return drpDwnCSGuiToolbarWorkflow
     */
    public WebElement getDrpDwnCSGuiToolbarWorkflow() {
        return drpDwnCSGuiToolbarWorkflow;
    }

    /**
     * This method will return the element for drop down Automation in showmore
     * options menu
     * 
     * @return drpDwnCSGuiToolbarAutomation
     */
    public WebElement getDrpDwnCSGuiToolbarAutomation() {
        return drpDwnCSGuiToolbarAutomation;
    }

    /**
     * This method will return the id from editor view horizontal toolbar
     * 
     * @return editorViewId
     */
    public WebElement getEditorViewId() {
        return editorViewId;
    }

    /**
     * This method will return the button for switching to studio list
     * 
     * @return btnSwitchToStudioList
     */
    public WebElement getBtnSwitchToStudioList() {
        return btnSwitchToStudioList;
    }

    /**
     * Returns the WebElement btnCSGuiToolbarNewState.
     * 
     * @return WebElement btnCSGuiToolbarNewState.
     */
    public WebElement getBtnCSGuiToolbarNewState() {
        return btnCSGuiToolbarNewState;
    }

    /**
     * Returns the filter view element.
     * 
     * @return btnFilterView
     */
    public WebElement getBtnFilterView() {
        return btnFilterView;
    }
}
