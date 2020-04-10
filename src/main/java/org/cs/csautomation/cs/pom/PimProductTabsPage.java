
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PimProductTabsPage {

    private WebDriver  browserDriverInstance;

    @FindBy(xpath = "//nobr[contains(text(),'Properties')]/parent::*")
    private WebElement btnPropertyTab;

    @FindBy(xpath = "//td[contains(@id,'splitareaborderright')]")
    private WebElement dvdrRightSplitFrame;
    
    @FindBy(xpath = "//nobr[contains(text(),'Preview')]/parent::*")
    private WebElement btnPreviewTab;

    public PimProductTabsPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized pimProductTabs page POM elements.");
    }

    /**
     * Getter method for browserDriverInstance
     * 
     * @return browserDriverInstance
     */
    public WebDriver getBrowserDriverInstance() {
        return browserDriverInstance;
    }

    /**
     * Setter method for browserDriverInstance
     * 
     * @param browserDriverInstance
     */
    public void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * Getter method for btnPropertyTab
     * 
     * @return btnPropertyTab
     */
    public WebElement getBtnPropertyTab() {
        return btnPropertyTab;
    }

    /**
     * Setter method for btnPropertyTab
     * 
     * @param btnPropertyTab
     */
    public void setBtnPropertyTab(WebElement btnPropertyTab) {
        this.btnPropertyTab = btnPropertyTab;
    }

    /**
     * Getter method for dvdrRightSplitFrame
     * 
     * @return dvdrRightSplitFrame
     */
    public WebElement getDvdrRightSplitFrame() {
        return dvdrRightSplitFrame;
    }

    /**
     * Setter method for dvdrRightSplitFrame
     * 
     * @param dvdrRightSplitFrame
     */
    public void setDvdrRightSplitFrame(WebElement dvdrRightSplitFrame) {
        this.dvdrRightSplitFrame = dvdrRightSplitFrame;
    }

    /**
     * This method performs click operation on the Property tab.
     * 
     * @param waitForReload
     */
    public void clickOnPropertyTab(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//nobr[contains(text(),'Properties')]/parent::*")));
        WebElement element = browserDriverInstance.findElement(
                By.xpath("//nobr[contains(text(),'Properties')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the property tab of the product.");
    }

    /**
     * This method performs click operation on vertical split between product
     * view and history/access right frame.
     * 
     * @return PimProductSplitareaContentRight
     */
    public PimProductSplitareaContentRightPage clickOnSpliareaContentRight() {
        String styleAttribute = getDvdrRightSplitFrame().getAttribute("style");
        if (styleAttribute.contains("CSGuiSplitareaToggleLeft")) {
            getDvdrRightSplitFrame().click();
        }

        return new PimProductSplitareaContentRightPage(browserDriverInstance);
    }
    
    /**
     * Getter method for btnPreviewTab
     * 
     * @return btnPreviewTab
     */
    public WebElement getBtnPreviewTab() {
        return btnPreviewTab;
    }
    
    /**
     * This method performs click operation on the Preview tab.
     * 
     * @param waitForReload
     */
    public void clickOnPreviewTab(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(getBtnPreviewTab()));
        getBtnPreviewTab().click();
        CSLogger.info("Clicked on the property tab of the product.");
    }

}
