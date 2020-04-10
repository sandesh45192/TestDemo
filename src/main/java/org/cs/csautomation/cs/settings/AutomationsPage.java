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
 * This class contains WebElements and methods of automation page.
 * 
 * @author CSAutomation Team
 *
 */
public class AutomationsPage {

    private WebDriver  browserDriverInstance;

    @FindBy(id = "Automations@0")
    private WebElement btnAutomationsNode;

    @FindBy(xpath = "//input[contains(@id,'AutomationAutomationName')]")
    private WebElement txtAutomationName;

    @FindBy(xpath = "//input[contains(@id,'AutomationDescription')]")
    private WebElement txtAutomationDescription;

    @FindBy(xpath = "//select[contains(@id,'AutomationAutoRunFrequency')]")
    private WebElement drpDwnAutoRunFrequency;

    @FindBy(xpath = "//select[contains(@id,'AutomationStandardAction')]")
    private WebElement drpDwnStandardAction;

    /**
     * Parameterized constructor of class automation page.
     * 
     * @param paramDriver
     *            Contains the instance of WebDriver.
     */
    public AutomationsPage(WebDriver paramDriver) {
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
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * Returns the WebElement of automations node.
     * 
     * @return WebElement btnAutomationsNode.
     */
    public WebElement getBtnAutomationsNode() {
        return btnAutomationsNode;
    }

    /**
     * Returns the WebElement of textbox to enter automation name.
     * 
     * @return WebElement txtAutomationName.
     */
    public WebElement getTxtAutomationName() {
        return txtAutomationName;
    }

    /**
     * Returns the WebElement of textbox to enter automation description.
     * 
     * @return WebElement txtAutomationDescription.
     */
    public WebElement getTxtAutomationDescription() {
        return txtAutomationDescription;
    }

    /**
     * Returns the Drop Down element to select auto run automation frequency.
     * 
     * @return WebElement drpDwnAutoRunFrequency
     */
    public WebElement getDrpDwnAutoRunFrequency() {
        return drpDwnAutoRunFrequency;
    }

    /**
     * Returns the Drop Down element to select standard automation action.
     * 
     * @return WebElement drpDwnStandardAction.
     */
    public WebElement getDrpDwnStandardAction() {
        return drpDwnStandardAction;
    }

    /**
     * This method clicks on automations node.
     * 
     * @param waitForReload
     *            WebDriverWait Object.
     */
    public void clkOnAutomationsNode(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                getBtnAutomationsNode());
        getBtnAutomationsNode().click();
        CSLogger.info("Clicked on automations node.");
    }

    /**
     * This method enters automation information for eg. automation
     * name,description.
     * 
     * @param automationDetails
     *            String object contains automation information.
     * @param automationElement
     *            WebElement of automation.
     * @param waitForReload
     *            WebDriverWait Object.
     */
    public void enterAutomationDetails(String automationDetails,
            WebElement automationElement, WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, automationElement);
        automationElement.click();
        automationElement.clear();
        automationElement.sendKeys(automationDetails);
        CSLogger.info("Entered automation detail as : " + automationDetails);
    }

    /**
     * Selects the drop down element of automation.
     * 
     * @param valueOfDrpDwn
     *            String object contains option to be selected for automation.
     */
    public void selectAutomationDrpDwnOption(WebElement automationElement,
            String valueOfDrpDwn) {
        automationElement.click();
        Select element = new Select(automationElement);
        element.selectByVisibleText(valueOfDrpDwn);
        CSLogger.info("Drop down option " + valueOfDrpDwn + " selected");
    }

    /**
     * Selects the given auto frequency option.
     * 
     * @param autoRunFrequencyOption
     *            String object contains auto frequency option to be set for
     *            automation.
     */
    public void selectAutoRunFrequency(String autoRunFrequencyOption) {
        selectAutomationDrpDwnOption(getDrpDwnAutoRunFrequency(),
                autoRunFrequencyOption);
        CSLogger.info("Auto run automation frequency is set to : "
                + autoRunFrequencyOption);
    }

    /**
     * Selects the given standard action.
     * 
     * @param standardActionOption
     *            String object contains standard action to be set for
     *            automation.
     */
    public void selectStandardAction(String standardActionOption) {
        selectAutomationDrpDwnOption(getDrpDwnStandardAction(),
                standardActionOption);
        CSLogger.info("Standard automation action is set to : "
                + standardActionOption);
    }
}
