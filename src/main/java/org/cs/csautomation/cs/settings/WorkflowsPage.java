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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains WebElements and methods of workflow page.
 * 
 * @author CSAutomation Team
 *
 */
public class WorkflowsPage {

    @FindBy(id = "Workflows@0")
    private WebElement btnWorkflowsNode;

    @FindBy(xpath = "//input[contains(@id,'WorkflowWorkflowName')]")
    private WebElement txtWorkflowName;

    @FindBy(xpath = "//select[contains(@id,'WorkflowType')]")
    private WebElement drpDwnWorkflowType;

    @FindBy(xpath = "//input[contains(@id,'WorkflowStartStateID')]")
    private WebElement txtWorkflowStartStateId;

    @FindBy(xpath = "//textarea[contains(@id,'WorkflowDescription')]")
    private WebElement txtWorkflowDescription;

    @FindBy(xpath = "//span[@id='Workflows@WorkflowType_PdmArticle']")
    private WebElement pimStudioProductsWorkflowType;

    /**
     * This method clicks on pim studio products workflow type
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkPimStudioProductsWorkflowType(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getPimStudioProductsWorkflowType()));
        getPimStudioProductsWorkflowType().click();
        CSLogger.info("Clicked on pim studio products workflow type");
    }

    /**
     * This method returns the instance of pim studio products workflow type
     * 
     * @return the pimStudioProductsWorkflowType
     */
    public WebElement getPimStudioProductsWorkflowType() {
        return pimStudioProductsWorkflowType;
    }

    /**
     * Parameterized constructor of class workflow page.
     * 
     * @param paramDriver Contains the instance of WebDriver.
     */
    public WorkflowsPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Settings node page POM elements");
    }

    /**
     * Sets instance of browserDriver.
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
    }

    /**
     * Returns the WebElement of workflows node.
     * 
     * @return WebElement btnWorkflowsNode.
     */
    public WebElement getBtnWorkflowsNode() {
        return btnWorkflowsNode;
    }

    /**
     * Returns the text box to enter workflow name.
     * 
     * @return WebElement txtWorkflowName.
     */
    public WebElement getTxtWorkflowName() {
        return txtWorkflowName;
    }

    /**
     * Returns the drop down WebElement of workflow type.
     * 
     * @return WebElement drpDwnWorkflowType.
     */
    public WebElement getDrpDwnWorkflowType() {
        return drpDwnWorkflowType;
    }

    /**
     * Returns the text box to enter workflow start state.
     * 
     * @return WebElement txtWorkflowStartStateId.
     */
    public WebElement getTxtWorkflowStartStateId() {
        return txtWorkflowStartStateId;
    }

    /**
     * Returns the text box to enter workflow description.
     * 
     * @return WebElement WorkflowDescription.
     */
    public WebElement getTxtWorkflowDescription() {
        return txtWorkflowDescription;
    }

    /**
     * This method clicks on workflows node.
     * 
     * @param waitForReload WebDriverWait Object.
     */
    public void clkOnWorkflowsNode(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnWorkflowsNode());
        getBtnWorkflowsNode().click();
        CSLogger.info("Clicked on workflows node.");
    }

    /**
     * * This method enters workflow information for eg. workflow name,start
     * state,workflow type, and description.
     * 
     * @param workflowdetails String object contains workflow information.
     * @param workflowElement WebElement of workflow.
     * @param waitForReload WebDriverWait Object.
     */
    public void enterWorkflowDetails(String workflowdetails,
            WebElement workflowElement, WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, workflowElement);
        workflowElement.click();
        workflowElement.sendKeys(workflowdetails);
        CSLogger.info("Entered workflow detail as : " + workflowdetails);
    }

    /**
     * This method clicks on an element
     * 
     * @param waitForReload waits for an element to reload
     * @param element contains element to be clicked
     */
    public void clkElement(WebDriverWait waitForReload, WebElement element) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
    }

    /**
     * Selects the workflow type.
     * 
     * @param valueOfDrpDwn String object contains type of workflow to be
     *            selected.
     */
    public void selectWorkflowType(String valueOfDrpDwn) {
        getDrpDwnWorkflowType().click();
        Select element = new Select(getDrpDwnWorkflowType());
        element.selectByVisibleText(valueOfDrpDwn);
        CSLogger.info("Drop down option " + valueOfDrpDwn + " selected");
    }
}
