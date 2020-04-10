/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.portal;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ChoosePortalPage {

    private WebDriver     browserDriverInstance;

    @FindBy(xpath = "(//td[@class='CSPortalGuiListItemAction'])[1]")
    private WebElement createNewTab;

    @FindBy(xpath = "//input[@id='Title']")
    private WebElement txtTitle;

    @FindBy(xpath = "//a[contains(text(),'Insert')]")
    private WebElement btnInsert;

    @FindBy(xpath = "//li[@id='CSPortalAddTabButton']")
    private WebElement tabAdd;

    @FindBy(
            xpath = "((//table[@name='CSPortalGuiList'])[1]/tbody/tr/td[2]/img)[1]")
    private WebElement createNewInTabTemplate;

    @FindBy(xpath = "(//span[@class='CSPortalAddWidget'])[1]")
    private WebElement btnPlusToAddWidget;

    @FindBy(xpath = "(//td[@class='CSPortalGuiListItemAction'])[1]")
    private WebElement bySuiteMenu;

    @FindBy(xpath = "(//td[@class='CSPortalGuiListItemAction'])[6]")
    private WebElement coreSubMenu;

    @FindBy(xpath = "//a[contains(text(),'CORE Portal Studio')]")
    private WebElement corePortalStudio;

    @FindBy(xpath = "//h1[contains(text(),'Configuration Required')]")
    private WebElement configurationMessage;

    @FindBy(xpath = "(//div[@class='CSPortalGuiPanelIcon'])[2]")
    private WebElement iconSecuredOptions;

    @FindBy(xpath = "(//div[@class='CSPortalGuiPanelIcon'])[3]")
    private WebElement iconLayoutOptions;

    @FindBy(xpath = "//div[@class='CSPortalWidgetDragArea']")
    private WebElement portalWidgetDragArea;

    @FindBy(xpath = "//div[contains(@class,'CSPortalWidgetButton Option')]")
    private WebElement btnPortalWidget;

    @FindBy(xpath = "//option[contains(text(),'PIM Studio')]")
    private WebElement pimStudioOption;

    @FindBy(xpath = "//a[contains(text(),'Save')]")
    private WebElement btnSave;

    @FindBy(xpath = "//a[contains(text(),'Apply')]")
    private WebElement btnApply;

    @FindBy(id = "Articles@0")
    private WebElement btnPimProductsNode;

    @FindBy(xpath = "//input[contains(@id,'CSPortalGuiWidgetControllerTitle')]")
    private WebElement textWidgetTitle;

    @FindBy(
            xpath = "//select[contains(@id,'CSPortalGuiWidgetControllerHeight')]")
    private WebElement selectWidgetHeight;

    @FindBy(xpath = "//input[contains(@id,'CSPortalGuiWidgetControllerColor')]")
    private WebElement widgetColor;

    @FindBy(xpath = "//li[@name='CSPortalTabButton']")
    private WebElement portalTab;

    @FindBy(xpath = "//input[contains(@id,'CSPortalGuiTabControllerTitle')]")
    private WebElement tabTitle;

    @FindBy(xpath = "//select[contains(@id,'CSPortalGuiTabControllerCols')]")
    private WebElement selectTabColumn;

    @FindBy(xpath = "//select[@id='CSPortalGuiPortalControllerSkinFile']")
    private WebElement selectSkin;

    @FindBy(xpath = "//select[@id='CSPortalGuiPortalControllerIsShared']")
    private WebElement selectTypeSharing;

    @FindBy(xpath = "//a[contains(text(),'Cancel')]")
    private WebElement btnCancel;

    @FindBy(xpath = "//a[contains(text(),'OK')]")
    private WebElement btnOK;

    @FindBy(xpath = "//a[contains(text(),'Delete')]")
    private WebElement btnDelete;

    @FindBy(xpath = "//a[@class='CSPortalWindowCloser']")
    private WebElement btnClose;

    @FindBy(xpath = "//input[@id='btn-ok']")
    private WebElement btnOKColorConfigurator;

    @FindBy(xpath = "//div[@class='CSPortalWidgetButton Maximize']")
    private WebElement btnMaximize;

    @FindBy(xpath = "//button[@id='CSPortalLoginTermsOfUseButtonAccept']")
    private WebElement btnAccept;

    /**
     * This method returns the element of button Ok of window color
     * configuration
     * 
     * @return btnOKColorConfigurator
     */
    public WebElement getBtnOKColorConfigurator() {
        return btnOKColorConfigurator;
    }

    /**
     * This method returns the element of close button
     * 
     * @return btnClose
     */
    public WebElement getBtnClose() {
        return btnClose;
    }

    /**
     * This method returns the element of delete button
     * 
     * @return btnDelete
     */
    public WebElement getBtnDelete() {
        return btnDelete;
    }

    /**
     * This method returns the element of cancel button
     * 
     * @return btnCancel
     */
    public WebElement getBtnCancel() {
        return btnCancel;
    }

    /**
     * This method returns the element of OK button
     * 
     * @return btnOK
     */
    public WebElement getBtnOK() {
        return btnOK;
    }

    /**
     * This method returns the element selector sharing type
     * 
     * @return selectTypeSharing
     */
    public WebElement getSelectTypeSharing() {
        return selectTypeSharing;
    }

    /**
     * This method returns the element selector Skin
     * 
     * @return selectSkin
     */
    public WebElement getselectSkin() {
        return selectSkin;
    }

    /**
     * This method returns the element Tab column
     * 
     * @return selectTabColumn
     */
    public WebElement getselectTabColumn() {
        return selectTabColumn;
    }

    /**
     * This method returns the element Tab Title
     * 
     * @return tabTitle
     */
    public WebElement getTabTitle() {
        return tabTitle;
    }

    /**
     * This method returns the element portal tab
     * 
     * @return portalTab
     */
    public WebElement getPortalTab() {
        return portalTab;
    }

    /**
     * This method returns the element of drop down to select widget color
     * 
     * @return selectWidgetColor
     */
    public WebElement getWidgetColor() {
        return widgetColor;
    }

    /**
     * This method returns the element of drop down to select widget height
     * 
     * @return selectWidgetHeightt
     */
    public WebElement getSelectWidgetHeight() {
        return selectWidgetHeight;
    }

    /**
     * This method returns the element of Title of widget
     * 
     * @return textWidgetTitle
     */
    public WebElement getTextWidgetTitle() {
        return textWidgetTitle;
    }

    /**
     * This method returns the instance of maximize button
     * 
     * @return btnMaximize
     */
    public WebElement getBtnMaximize() {
        return btnMaximize;
    }

    /**
     * This method returns the instance of Accept button
     * 
     * @return btnAccept
     */
    public WebElement getBtnAccept() {
        return btnAccept;
    }

    /**
     * This method clicks on Accept button
     */
    public void clkBtnAccept() {
        waitOnElement(browserDriverInstance, getBtnAccept());
        getBtnAccept().click();
        CSLogger.info("Clicked on accept button");
    }

    /**
     * This method returns the instance of Products node
     * 
     * @return btnPimProductsNode
     */
    public WebElement getBtnPimProductsNode() {
        return btnPimProductsNode;
    }

    /**
     * This method clicks on Pim studio Products node
     */
    public void clkPimProductsNode() {
        waitOnElement(browserDriverInstance, getBtnPimProductsNode());
        getBtnPimProductsNode().click();
    }

    /**
     * This method returns the instance of portal widget options button
     * 
     * @return btnPortalWidget
     */
    public WebElement getBtnPortalWidget() {
        return btnPortalWidget;
    }

    /**
     * This method returns the instance of portal widget drag area to click on
     * widget options
     * 
     * @return portalwidgetDragArea
     */
    public WebElement getPortalWidgetDragArea() {
        return portalWidgetDragArea;
    }

    /**
     * This method returns the instance of secured options icon
     * 
     * @return iconSecuredOptions
     */
    public WebElement getIconSecuredOptions() {
        return iconSecuredOptions;
    }

    /**
     * This method clicks on Icon Secured Option icon
     */
    public void clkIconSecuredOptions() {
        waitOnElement(browserDriverInstance, getIconSecuredOptions());
        getIconSecuredOptions().click();
    }

    /**
     * This method returns the instance of layout options
     * 
     * @return iconLayoutOptions
     */
    public WebElement getIconLayoutOptions() {
        return iconLayoutOptions;
    }

    /**
     * This method clicks on Icon Layout Options
     */
    public void clkIconLayoutOptions() {
        waitOnElement(browserDriverInstance, getIconLayoutOptions());
        getIconLayoutOptions().click();
    }

    /**
     * This method returns the instance of PIM studio option to add in widget
     * options
     * 
     * @return pimStudioOption
     */
    public WebElement getPimStudioOption() {
        return pimStudioOption;
    }

    /**
     * This method returns the instance of core portal studio after clicking on
     * core submenu in BySuite menu
     * 
     * @return corePortalStudio
     */
    public WebElement getCorePortalStudio() {
        return corePortalStudio;
    }

    /**
     * This method returns the instance of core submenu after opening the By
     * Suite menu
     * 
     * @return coreSubMenu
     */
    public WebElement getCoreSubMenu() {
        return coreSubMenu;
    }

    /**
     * This method returns the instance of BySuite menu
     * 
     * @return bySuiteMenu
     */
    public WebElement getBySuiteMenu() {
        return bySuiteMenu;
    }

    /**
     * This method clicks on BySuiteMenu
     */
    public void clkBysuiteMenu() {
        waitOnElement(browserDriverInstance, getBySuiteMenu());
        getBySuiteMenu().click();
    }

    /**
     * This method clicks on Core submenu after clicking on BySuite menu
     */
    public void clkCoreSubMenu() {
        waitOnElement(browserDriverInstance, getCoreSubMenu());
        getCoreSubMenu().click();
    }

    /**
     * This method clicks on Core portal studio
     */
    public void clkCorePortalStudio() {
        waitOnElement(browserDriverInstance, getCorePortalStudio());
        getCorePortalStudio().click();
    }

    /**
     * This method returns the instance of Apply button
     * 
     * @return btnApply
     */
    public WebElement getBtnApply() {
        return btnApply;
    }

    /**
     * This method clicks on Apply button
     */
    public void clkBtnApply() {
        waitOnElement(browserDriverInstance, getBtnApply());
        getBtnApply().click();
    }

    /**
     * This method returns the instance of Save button
     * 
     * @return btnSave
     */
    public WebElement getBtnSave() {
        return btnSave;
    }

    /**
     * This method click on save button
     * 
     */
    public void clkBtnSave() {
        waitOnElement(browserDriverInstance, getBtnSave());
        getBtnSave().click();
    }

    /**
     * This method waits on element for specific time
     * 
     * @param browserDriverInstance contains instance of browser driver
     * @param element contains the element to be waited on
     */
    public void waitOnElement(WebDriver browserDriverInstance,
            WebElement element) {
        WebDriverWait waitForReload = new WebDriverWait(browserDriverInstance,
                60);
        waitForReload.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * This method returns the instance of plus button to add widget
     * 
     * @return btnPlusToAddWidget
     */
    public WebElement getBtnPlusToAddWidget() {
        return btnPlusToAddWidget;
    }

    /**
     * This method returns the instance of create new in tab template
     * 
     * @return createNEwInTabtemplate
     */
    public WebElement getCreateNewInTabTemplate() {
        return createNewInTabTemplate;
    }

    /**
     * This method returns instance of Add tab button in new portal window
     * 
     * @return btnAddTab
     */
    public WebElement getTabAdd() {
        return tabAdd;
    }

    /**
     * This method clicks on add tab
     */

    public void clkBtnAddTab(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getTabAdd()));
        getTabAdd().click();
        CSLogger.info("Clicked on Add Tab");
    }

    /**
     * This method returns the instance of Insert button
     * 
     * @return btnInsert
     */
    public WebElement getBtnInsert() {
        return btnInsert;
    }

    /**
     * 
     * This method returns the instance of Insert button
     * 
     */
    public void clkBtnInsert(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getBtnInsert()));
        getBtnInsert().click();
        CSLogger.info("Clicked on Insert button to create new portal");
    }

    /**
     * This method returns the instance of textbox of Title to enter New portal
     * name
     * 
     * @return txtTitle
     */
    public WebElement getTxtTitle() {
        return txtTitle;
    }

    /**
     * This method returns the instance of create new option
     * 
     * @return createNewTab
     */
    public WebElement getCreateNewTab() {
        return createNewTab;
    }

    /**
     * This method clicks on create new tab in the left section pane
     */
    public void clkCreateNewTab() {
        getCreateNewTab().click();
    }

    /**
     * This method returns the instance of configuration message field after
     * click on Insert to add widget
     * 
     * @return configurationMessage
     */
    public WebElement getConfigurationMessage() {
        return configurationMessage;
    }

    public ChoosePortalPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Choose Portal page.");
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }
}
