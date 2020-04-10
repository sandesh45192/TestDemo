
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PimStudioChannelsNode {

    private WebDriver  browserDriverInstance;

    @FindBy(id = "Structures@0")
    private WebElement btnPimChannelsNode;

    @FindBy(xpath = "//input[contains(@id,'RecorditemstructureLabel')]")
    private WebElement txtChannelLabel;

    @FindBy(xpath = "//select[contains(@id,'RecorditemstructureCopyMode')]")
    private WebElement drpDwnCreateRecursively;

    @FindBy(xpath = "//select[contains(@id,'RecorditemstructureSelection')]")
    private WebElement drpDwnSelectionType;

    public PimStudioChannelsNode(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized PimStudioChannelsNode page POM elements.");
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
     * Returns the btnPimChannelsNode web Element
     * 
     * @return btnPimChannelsNode
     */
    public WebElement getBtnPimChannelsNode() {
        return btnPimChannelsNode;
    }

    /**
     * Clicks on Pim channel node
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnPimChannelsNode(WebDriverWait waitForReload) {
        traverseToPimChannelsNode(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnPimChannelsNode());
        getBtnPimChannelsNode().click();
        CSLogger.info("Clicks on Pim Channel Node");
    }

    /**
     * Switches the frame so channels node can be clicked
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void traverseToPimChannelsNode(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriverInstance);
    }

    /**
     * Returns the txtChannelLabel web Element
     * 
     * @return txtChannelLabel
     */
    public WebElement getTxtChannelLabel() {
        return txtChannelLabel;
    }

    /**
     * Returns the drpDwnCreateRecursively web Element
     * 
     * @return drpDwnCreateRecursively
     */
    public WebElement getDrpDwnCreateRecursively() {
        return drpDwnCreateRecursively;
    }

    /**
     * Returns the drpDwnSelectionType web Element
     * 
     * @return drpDwnSelectionType
     */
    public WebElement getDrpDwnSelectionType() {
        return drpDwnSelectionType;
    }
}
