
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PimProductSplitareaContentRightPage {

    private WebDriver  browserDriverInstance;

    @FindBy(xpath = "//a[contains(text(), 'History')]/parent::*")
    private WebElement btnProductHistory;

    @FindBy(xpath = "//a[contains(text(), 'Derivations')]/parent::*")
    private WebElement btnProductDerivations;

    public PimProductSplitareaContentRightPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info(
                "Initialized pimProductSplitareaContentRight page POM elements.");
    }

    /**
     * Getter method for browserDriverInstance.
     * 
     * @return browserDriverInstance
     */
    public WebDriver getBrowserDriverInstance() {
        return browserDriverInstance;
    }

    /**
     * Setter method for BrowserDriverInstance
     * 
     * @param browserDriverInstance
     */
    public void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * Getter method for btnProductHistory
     * 
     * @return btnProductHistory
     */
    public WebElement getBtnProductHistory() {
        return btnProductHistory;
    }

    /**
     * Setter method for btnProductHistory.
     * 
     * @param btnProductHistory
     */
    public void setBtnProductHistory(WebElement btnProductHistory) {
        this.btnProductHistory = btnProductHistory;
    }

    /**
     * This method performs click operation on the Product History.
     * 
     */
    public void clickOnProductHistory() {
        getBtnProductHistory().click();
        CSLogger.info("Clicked on history");
    }

    /**
     * Getter method for btnProductDerivations
     * 
     * @return btnProductDerivations
     */
    public WebElement getBtnProductDerivations() {
        return btnProductDerivations;
    }

    /**
     * This method performs click operation on the Product Derivations.
     * 
     * @param waitForReload
     *            WebDriverWait object
     */
    public void clickOnProductDerivations(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnProductDerivations());
        getBtnProductDerivations().click();
        CSLogger.info("Clicked on derivations button");
    }
}
