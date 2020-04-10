/**
 * 
 */

package org.cs.csautomation.cs.mam;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MamStudioSettingsNode {

    public MamStudioSettingsNode(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized MamStudioVolumes page.");
    }

    @FindBy(id = "Settings@0")
    private WebElement settingsNodeMam;

    @FindBy(xpath = "//span[@id='Settings~ConfigurationLinks@0']")
    private WebElement classNodeMam;

    @FindBy(xpath = "//td[contains(text(),'Create new')]")
    private WebElement clkCreateNew;

    @FindBy(id = "Settings~Configurations@0")
    private WebElement btnSettingsAttributeNode;

    @FindBy(id = "Settings~Archives@0")
    private WebElement btnSettingsArchiveNode;

    @FindBy(id = "Settings~Archives@1")
    private WebElement btnSettingsStandardArchiveNode;

    /**
     * returns instance of Standard archive node under settings folder
     * 
     * @return btnSettingsStandardArchiveNode
     */
    public WebElement getBtnSettingsStandardArchiveNode() {
        return btnSettingsStandardArchiveNode;
    }

    /**
     * This method click on Standard archive node
     * 
     * @param waitForReload WebDriverWait object wait for element
     */
    public void clkSettingsStandardArchiveNode(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getBtnSettingsStandardArchiveNode()));
        getBtnSettingsStandardArchiveNode().click();
        CSLogger.info("Click on Standard Archive node");
    }

    /**
     * returns instance of archive node under settings folder
     * 
     * @return btnSettingsArchiveNode
     */
    public WebElement getBtnSettingsArchiveNode() {
        return btnSettingsArchiveNode;
    }

    /**
     * This method click on archive node
     * 
     * @param waitForReload WebDriverWait object wait for element
     */
    public void clkSettingsArchiveNode(WebDriverWait waitForReload) {
        waitForReload.until(
                ExpectedConditions.visibilityOf(getBtnSettingsArchiveNode()));
        getBtnSettingsArchiveNode().click();
        CSLogger.info("Click on Archive node");
    }

    /**
     * returns instance of attributes node under settings folder
     * 
     * @return btnbSettingsAttributeNode
     */
    public WebElement getBtnSettingsAttributeNode() {
        return btnSettingsAttributeNode;
    }

    public void clkSettingsAttributeNode(WebDriverWait waitForReload) {
        waitForReload.until(
                ExpectedConditions.visibilityOf(getBtnSettingsAttributeNode()));
        getBtnSettingsAttributeNode().click();
    }

    /**
     * returns instance of create new button after right click
     * 
     * @return clkCreateNew
     */
    public WebElement getClkCreateNew() {
        return clkCreateNew;
    }

    /**
     * 
     * This method returns instance of classes node
     * 
     * @return classesNodeMam;
     */
    public WebElement getClassNode() {
        return classNodeMam;
    }

    public void clkClassNode(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getClassNode()));
        getClassNode().click();
    }

    /**
     * This method returns the instance of settings node mam
     * 
     * @return
     */
    public WebElement getSettingsNodeMam() {
        return settingsNodeMam;
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
    }

}
