
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PimStudioSettingsNode {

    private WebDriver  browserDriverInstance;

    @FindBy(xpath = "//span[@id='Settings@0']/../..")
    private WebElement btnPimSettingsNode;

    @FindBy(id = "Settings~Configurations@0")
    private WebElement btnPimSettingsAttributeNode;

    @FindBy(id = "Settings~ConfigurationLinks@0")
    private WebElement btnPimClassesNode;
    
    @FindBy(xpath = "//span[@id='Settings~ImportStaging@0']")
    private WebElement btnTreeimportstaging;

    public PimStudioSettingsNode(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized PimStudioSettingsNode page POM elements.");
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
     * Returns the WebElement btnPimSettingsClassesNode
     * 
     * @return btnPimSettingsClassesNode PimStudioSettingsNode
     */
    public WebElement getBtnPimClassesNode() {
        return btnPimClassesNode;
    }

    /**
     * Returns the WebElement btnPimSettingsNode
     * 
     * @return btnPimSettingsNode PimStudioSettingsNode
     */
    public WebElement getBtnPimSettingsNode() {
        return btnPimSettingsNode;
    }

    /**
     * Returns the WebElement btnPimSettingsAttributeNode
     * 
     * @return btnPimSettingsAttributeNode PimStudioSettingsNode
     */
    public WebElement getBtnPimSettingsAttributeNode() {
        return btnPimSettingsAttributeNode;
    }

    /**
     * Returns the WebElement btnPimSettingsClassesNode
     * 
     * @return btnPimSettingsClassesNode PimStudioSettingsNode
     */
    public WebElement getBtnPimSettingsClassesNode() {
        return btnPimClassesNode;
    }

    /**
     * Clicks on Pim Studio Settings Node or webElement btnPimSettingsNode
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnPimSettingsNode(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriverInstance);
        waitForReload.until(
                ExpectedConditions.visibilityOf(getBtnPimSettingsNode()));
        getBtnPimSettingsNode().click();
        CSLogger.info("Clicked on Pim settings node");
    }

    /**
     * Clicks on Pim Studio Attribute Node or webElement
     * btnPimSettingsAttributeNode
     */
    public void clkBtnPimSettingsAttributesNode(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getBtnPimSettingsAttributeNode()));
        getBtnPimSettingsAttributeNode().click();
        CSLogger.info("Clicked on Pim settings attribute node");
    }

    /**
     * Clicks on Pim Studio Classes Node or webElement btnPimSettingsClassesNode
     */
    public void clkBtnPimSettingsClassesNode(WebDriverWait waitForReload) {
        waitForReload
                .until(ExpectedConditions.visibilityOf(getBtnPimClassesNode()));
        getBtnPimSettingsClassesNode().click();
        CSLogger.info("Clicked on Pim settings classes node");
    }
    
    /*
     * This method returns the pim->settings->import staging tree node element
     * @return WebElement btnTreeimportstaging of import tree node
     */
    public WebElement getImportstagingTreeNode() {
        return btnTreeimportstaging;
    }
    
    /*
     * clicks on the Import staging tree node
     * @param waitForReload
     */
    public void clkImportstagingTreenode(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getImportstagingTreeNode());
        getImportstagingTreeNode().click();
    }
    
}
