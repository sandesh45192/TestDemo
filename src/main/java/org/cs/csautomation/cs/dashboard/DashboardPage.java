/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.dashboard;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class initializes all the elements required to drive the test case
 * 
 * @author CSAutomationTeam
 *
 */
public class DashboardPage {

    @FindBy(xpath = "//button[@name='locktoggle']")
    private WebElement btnUnlockToggle;

    @FindBy(xpath = "//button[@name='unlocktoggle']")
    private WebElement btnLockToggle;

    @FindBy(xpath = "//button[contains(text(),'Administrator')]")
    private WebElement btnAdministrator;

    @FindBy(xpath = "//button[@name='plus']")
    private WebElement btnPlus;

    @FindBy(xpath = "(//a[contains(@class,'ui-tabs-anchor')])[2]")
    private WebElement tabNewTile;

    @FindBy(xpath = "//span[contains(text(),'Insert & Configure')]")
    private WebElement btnInsertAndConfigure;

    @FindBy(xpath = "//input[@placeholder='Tile Caption']")
    private WebElement txtTileCaption;

    @FindBy(xpath = "//input[@placeholder='Category']")
    private WebElement txtCategory;

    @FindBy(xpath = "//div[contains(@id,'tileFooter')]/button/span")
    private WebElement btnOk;

    @FindBy(xpath = "//label[text()='Favorites']/../div/div/div/div/input")
    private WebElement txtSelectFavorites;

    @FindBy(
            xpath = "//button[@name='SettingsButton']//span[@class='button-icon left']//span")
    private WebElement btnSettingsTile;

    @FindBy(
            xpath = "//button[@name='DuplicateButton']//span[@class='button-icon left']//span")
    private WebElement btnDuplicateTile;

    @FindBy(
            xpath = "//button[@name='DeleteButton']//span[@class='button-icon left']//span")
    private WebElement btnDelete;

    @FindBy(xpath = "//select[@name='App']")
    private WebElement drpdwnType;

    @FindBy(xpath = "//select[@name='AppScreen']")
    private WebElement drpdwnView;

    @FindBy(xpath = "//button[contains(text(),'Current User')]")
    private WebElement btnCurrentUser;

    @FindBy(xpath = "//span[@id='Dashboard@0']")
    private WebElement nodeDashboad;

    @FindBy(xpath = "//span[@id='Dashboard~Dashboards by User@0']")
    private WebElement nodeDashboardByUser;

    @FindBy(xpath = "//input[contains(@style,'end.svg')]")
    private WebElement btnSelectAllForSelectedSection;

    @FindBy(xpath = "//div[contains(@class,'CSPortalWindowContent')]")
    private WebElement csPortalWindowContent;

    @FindBy(xpath = "//a[@class='CSPortalWindowCloser']")
    private WebElement btnClose;

    @FindBy(xpath = "//div[contains(@id,'CSPortalWindow')]")
    private WebElement csPortalWindow;

    @FindBy(xpath = "//input[@class='ajs-input']")
    private WebElement txtAjsInput;

    @FindBy(xpath = "//button[@class='ajs-button ajs-ok']")
    private WebElement btnOkAjs;

    @FindBy(xpath = "//button[contains(text(),'OK')]")
    private WebElement btnOkOfPopup;

    @FindBy(xpath = "//button[contains(text(),'Cancel')]")
    private WebElement btnCancelOfPopup;

    /**
     * Returns the instane of cancel of pop up
     * 
     * @return btnCancelOfPopup
     */
    public WebElement getBtnCancelOfPopup() {
        return btnCancelOfPopup;
    }

    /**
     * Returns instance of btn ok of pop up
     * 
     * @return btnOkOfPopup
     */
    public WebElement getBtnOkOfPopup() {
        return btnOkOfPopup;
    }

    /**
     * Returns the instance of delete button
     * 
     * @return btnDelete
     */
    public WebElement getBtnDelete() {
        return btnDelete;
    }

    /**
     * Returns the instance of ok button on ajs input box
     * 
     * @return btnOkAjs
     */
    public WebElement getBtnOkAjs() {
        return btnOkAjs;
    }

    /**
     * Returns the instance of txt ajs input box
     * 
     * @return txtAjsInput
     */
    public WebElement getTxtAjsInput() {
        return txtAjsInput;
    }

    /**
     * Returns the instance of cs portal window
     * 
     * @return csPortalWindow
     */
    public WebElement getCsPortalWindow() {
        return csPortalWindow;
    }

    /**
     * Returns the instance of cs portal window content
     * 
     * @return csPortalWindowContent
     */
    public WebElement getCsPortalWindowContent() {
        return csPortalWindowContent;
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
     * Returns the instance of select all button to select contents in the
     * selected textarea
     * 
     * @return btnSelectAllForSelectedSection
     */
    public WebElement getBtnSelectAllForSelectedSection() {
        return btnSelectAllForSelectedSection;
    }

    /**
     * Returns the instance of node dashboard by user
     * 
     * @return nodeDashboardByUser
     */
    public WebElement getNodeDashboardByUser() {
        return nodeDashboardByUser;
    }

    /**
     * Returns the instance of dashboard node
     * 
     * @return nodeDashboard
     */
    public WebElement getNodeDashboard() {
        return nodeDashboad;
    }

    public DashboardPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Home node page POM elements");
    }

    /**
     * Returns the instance of category textbox
     * 
     * @return txtCategory
     */
    public WebElement getTxtCategory() {
        return txtCategory;
    }

    /**
     * Returns the instance of duplicate button on created tile
     * 
     * @return btnDuplicateTile
     */
    public WebElement getBtnDuplicateTile() {
        return btnDuplicateTile;
    }

    /**
     * Returns the instance of current user
     * 
     * @return btnCurrentUser
     */
    public WebElement getBtnCurrentUser() {
        return btnCurrentUser;
    }

    /**
     * 
     * Returns the reference of type drop down
     * 
     * @return drpdwnType
     */
    public WebElement getDrpDwnView() {
        return drpdwnView;
    }

    /**
     * Returns the reference of type drop down
     * 
     * @return drpdwnType
     */
    public WebElement getDrpDwnType() {
        return drpdwnType;
    }

    /**
     * 
     * Returns the reference of settings button present on tile
     * 
     * @return btnOk
     */
    public WebElement getBtnSettingsTile() {
        return btnSettingsTile;
    }

    /**
     * 
     * Returns the reference of ok button
     * 
     * @return btnOk
     */
    public WebElement getBtnOk() {
        return btnOk;
    }

    /**
     * This method returns the instance of btnSelectFavorites
     * 
     * @return btnSelectFavorites
     */
    public WebElement getTxtSelectFavorites() {
        return txtSelectFavorites;
    }

    /**
     * This method returns the instance of tile caption textbox
     * 
     * @return txtTileCaption
     */
    public WebElement getTxtTileCaption() {
        return txtTileCaption;
    }

    /**
     * Returns the instance of insert and configure button
     * 
     * @return btnInsertAndConfigure
     */
    public WebElement getBtnInsertAndConfigure() {
        return btnInsertAndConfigure;
    }

    /**
     * Returns the instance of lock toggle button
     * 
     * @return the btnLockToggle
     */
    public WebElement getBtnUnlockToggle() {
        return btnUnlockToggle;
    }

    /**
     * Returns the instance of lock toggle button
     * 
     * @return the btnLockToggle
     */
    public WebElement getBtnLockToggle() {
        return btnLockToggle;
    }

    /**
     * Returns the instance of New tile tab
     * 
     * @return the tabNewTile
     */
    public WebElement getTabNewTile() {
        return tabNewTile;
    }

    /**
     * Returns the instance of Adminstrator button
     * 
     * @return the btnAdministrator
     */
    public WebElement getBtnAdministrator() {
        return btnAdministrator;
    }

    /**
     * Returns the instance of plus button button
     * 
     * @return the btnPlus
     */
    public WebElement getBtnPlus() {
        return btnPlus;
    }

    /**
     * This method clicks on the element
     * 
     * @param waitForReload waits for an element to reload
     * @param element contains instance of web element
     */
    public void clkElement(WebDriverWait waitForReload, WebElement element) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
    }
}
