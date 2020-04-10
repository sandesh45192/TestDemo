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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains the methods used in Project Manager Module
 * 
 * @author CSAutomation Team
 *
 */
public class ProjectManagerPage {

    @FindBy(xpath = "//span[contains(text(),'Project Manager')]")
    WebElement projectManagernode;

    @FindBy(xpath = "//nobr[contains(text(),'Browse')]")
    WebElement bthBrowser;

    @FindBy(xpath = "//span[@id='Settings@0']")
    WebElement nodeSetting;

    @FindBy(xpath = "//span[@id='Settings~Templates@0']")
    WebElement nodeTemplate;

    @FindBy(xpath = "//a//img[contains(@src,'new.gif')]")
    WebElement btnCreateNew;

    @FindBy(xpath = "(//input[contains(@id,'TaskTask')])[2]")
    WebElement txtTaskLabel;

    @FindBy(xpath = "//span[contains(@id,'TaskOwnerIDToMySelf_GUI')]")
    WebElement btnSetOwner;

    @FindBy(xpath = "//a//img[contains(@src,'save.gif')]")
    WebElement btnSave;

    @FindBy(
            xpath = "//div[contains(@id,'OwnerID_csReferenceDiv')]//img[@class="
                    + "'CSGuiSelectionListAdd']")
    WebElement btnAddOwner;

    @FindBy(xpath = "//nobr[contains(text(),'Applications')]/parent::td")
    WebElement btnApplications;

    @FindBy(xpath = "(//div[contains(@id,'UserCS_ItemAttribute')]//img[@class='CSGuiSelectionListAdd'])[3]")
    WebElement btnAddTemplate;

    @FindBy(xpath = "//input[@id='CSGUI_MODALDIALOG_OKBUTTON']")
    WebElement btnOK;

    @FindBy(xpath = "//a[@class='CSPortalWindowCloser']")
    WebElement btnWindowClose;

    @FindBy(xpath = "//a//img[contains(@src,'delete.gif')]")
    WebElement btnDelete;

    @FindBy(xpath = "//a//img[contains(@src,'refresh.gif')]")
    WebElement btnRefresh;

    @FindBy(xpath = "//span[@id='projectmanager@0']")
    WebElement nodeBPMProjectManager;

    @FindBy(xpath = "//span[@id='projectmanager~Project@0']")
    WebElement nodeProjects;

    @FindBy(xpath = "//input[@name='TaskIsOpen']")
    WebElement chkBoxTaskOpen;

    @FindBy(xpath = "//input[@name='TaskIsMine']")
    WebElement chkBoxMine;

    @FindBy(xpath = "//input[@name='TaskFromMe']")
    WebElement chkBoxFromMe;

    @FindBy(xpath = "//div//img[contains(@src,'workflow.svg')]")
    WebElement btnWorkflow;

    @FindBy(xpath = "//select[@id='massUpdateSelector']")
    WebElement drpdwnMassEdit;

    /**
     * Returns the WebElement of drop down Mass edit
     * 
     * @return drpdwnMassEdit
     */
    public WebElement getDrpdwnMassEdit() {
        return drpdwnMassEdit;
    }

    /**
     * Returns the WebElement of button workflow
     * 
     * @return btnWorkflow
     */
    public WebElement getBtnWorkflow() {
        return btnWorkflow;
    }

    /**
     * Returns the WebElement of check box Task from me
     * 
     * @return chkBoxFromMe
     */
    public WebElement getChkBoxFromMe() {
        return chkBoxFromMe;
    }

    /**
     * Returns the WebElement of check box Task is mine
     * 
     * @return chkBoxMine
     */
    public WebElement getChkBoxMine() {
        return chkBoxMine;
    }

    /**
     * Returns the WebElement of check box Task open
     * 
     * @return chkBoxTaskOpen
     */
    public WebElement getChkBoxTaskOpen() {
        return chkBoxTaskOpen;
    }

    /**
     * Returns the WebElement of node projects
     * 
     * @return nodeProjects
     */
    public WebElement getNodeProjects() {
        return nodeProjects;
    }

    /**
     * Returns the WebElement of node BPM project manager
     * 
     * @return nodeBPMProjectManager
     */
    public WebElement getNodeBPMProjectManager() {
        return nodeBPMProjectManager;
    }

    /**
     * Returns the WebElement of button Refresh
     * 
     * @return btnRefresh
     */
    public WebElement getBtnRefresh() {
        return btnRefresh;
    }

    /**
     * Returns the WebElement of button delete
     * 
     * @return btnDelete
     */
    public WebElement getBtnDelete() {
        return btnDelete;
    }

    /**
     * Returns the WebElement of button window close
     * 
     * @return btnWindowClose
     */
    public WebElement getBtnWindowClose() {
        return btnWindowClose;
    }

    /**
     * Returns the WebElement of button Ok
     * 
     * @return btnOK
     */
    public WebElement getBtnOK() {
        return btnOK;
    }

    /**
     * Returns the WebElement of button Add Template
     * 
     * @return btnAddTemplate
     */
    public WebElement getBtnAddTemplate() {
        return btnAddTemplate;
    }

    /**
     * Returns the WebElement of button Applications
     * 
     * @return btnApplications
     */
    public WebElement getBtnApplications() {
        return btnApplications;
    }

    /**
     * Returns the WebElement of button Add owner
     * 
     * @return btnAddOwner
     */
    public WebElement getBtnAddOwner() {
        return btnAddOwner;
    }

    /**
     * Returns the WebElement of button save
     * 
     * @return btnSave
     */
    public WebElement getBtnSave() {
        return btnSave;
    }

    /**
     * Returns the WebElement of button set owner to current user
     * 
     * @return btnSetOwner
     */
    public WebElement getBtnSetOwner() {
        return btnSetOwner;
    }

    /**
     * Returns the WebElement of Task label
     * 
     * @return txtTaskLabel
     */
    public WebElement getTxtTaskLabel() {
        return txtTaskLabel;
    }

    /**
     * Returns the WebElement of button create new
     * 
     * @return btnCreateNew
     */
    public WebElement getBtnCreateNew() {
        return btnCreateNew;
    }

    /**
     * Returns the WebElement of node template
     * 
     * @return nodeTemplate
     */
    public WebElement getNodeTemplate() {
        return nodeTemplate;
    }

    /**
     * Returns the WebElement of node setting project manager
     * 
     * @return nodeSetting
     */
    public WebElement getNodeSetting() {
        return nodeSetting;
    }

    /**
     * Returns the WebElement of button browser
     * 
     * @return bthBrowser
     */
    public WebElement getBthBrowser() {
        return bthBrowser;
    }

    /**
     * Returns the WebElement of project manager node
     * 
     * @return projectManagernode
     */
    public WebElement getProjectManagernode() {
        return projectManagernode;
    }

    /**
     * This method click on Browse Project Manager
     * 
     * @param element contains the element to be waited on
     */
    public void clkOnBrowse(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getBthBrowser()));
        getBthBrowser().click();
        CSLogger.info("Clicked On Browse");
    }

    /**
     * This method click on Setting node Project Manager
     * 
     * @param element contains the element to be waited on
     */
    public void clkOnSettingNode(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getNodeSetting()));
        getNodeSetting().click();
        CSLogger.info("Clicked On Setting Node");
    }

    /**
     * This method click on Setting node Project Manager
     * 
     * @param element contains the element to be waited on
     */
    public void clkOnTemplatesNode(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getNodeTemplate()));
        getNodeTemplate().click();
        CSLogger.info("Clicked On Template Node");
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

    public ProjectManagerPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized   Settings node page POM elements");
    }

    /**
     * This method click on Create New option
     * 
     * @param element contains the element to be waited on
     */
    public void clkCreateNewOption(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, getBtnCreateNew());
        getBtnCreateNew().click();
        CSLogger.info("Clicked on create new option");
    }

    /**
     * This method click on BPM Project Manager Node
     * 
     * @param element contains the element to be waited on
     */
    public void clkOnBPMProjectManagerNode(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getNodeBPMProjectManager());
        getNodeBPMProjectManager().click();
        CSLogger.info("Clicked on project Manager Node");
    }

    /**
     * This method click on node Projects
     * 
     * @param element contains the element to be waited on
     */
    public void clkOnProjectsNode(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, getNodeProjects());
        getNodeProjects().click();
        CSLogger.info("Clicked on project Manager");
    }

    /**
     * This method click on Button save
     * 
     * @param element contains the element to be waited on
     */
    public void clkOnButtonSave(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, getBtnSave());
        getBtnSave().click();
        CSLogger.info("Clicked on Save button");
    }

    /**
     * This method click on Button Window close
     * 
     * @param element contains the element to be waited on
     */
    public void clkOnButtonWindowClose(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, getBtnWindowClose());
        getBtnWindowClose().click();
        CSLogger.info("Clicked on Button Window Close");
    }

    /**
     * This method click on Button Delete
     * 
     * @param element contains the element to be waited on
     */
    public void clkOnButtonDelete(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, getBtnDelete());
        getBtnDelete().click();
        CSLogger.info("Clicked on Delete");
    }
}
