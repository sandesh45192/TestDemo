package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TabbedPaneNorth {

    private WebDriver browserDriverInstance;

    @FindBy(xpath = "//nobr[contains(text(),'PIM Studio')]")
    private WebElement pimStudioTab;

    @FindBy(id = "1.pdmarticle.title")
    private WebElement productCategoriesTab;

    @FindBy(id = "1.pdmarticlestructure.title")
    private WebElement channelCategoriesTab;

    public TabbedPaneNorth(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized  page POM elements.");
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
     * Returns the webElement pimStudioTab
     * 
     * @return pimStudioTab
     */
    public WebElement getPimStudioTab() {
        return pimStudioTab;
    }

    /**
     * Returns the webElement productCategoriesTab
     * 
     * @return productCategoriesTab
     */
    public WebElement getProductCategoriesTab() {
        return productCategoriesTab;
    }

    /**
     * Returns the webElement channelCategoriesTab
     * 
     * @return channelCategoriesTab
     */
    public WebElement getChannelCategoriesTab() {
        return channelCategoriesTab;
    }

    /**
     * Clicks on Pim Studio tab
     * 
     * @param waitForReload
     *            this is WebDriverWait instance
     */
    public void clkOnPimStudioTab(WebDriverWait waitForReload) {
        traverseTillFrmTreeFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, getPimStudioTab());
        getPimStudioTab().click();
        CSLogger.info("Clicked on Pim Studio Tab");
    }

    /**
     * Clicks on Product Categories Tab
     * 
     * @param waitForReload
     *            this is WebDriverWait instance
     */
    public void clkOnProductCategoriesTab(WebDriverWait waitForReload) {
        traverseTillFrmTreeFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getProductCategoriesTab());
        getProductCategoriesTab().click();
        CSLogger.info("Clicked on Product Categories Tab");
    }

    /**
     * Clicks on Channel Categories Tab
     * 
     * @param waitForReload
     *            this is WebDriverWait instance
     */
    public void clkOnChannelCategoriesTab(WebDriverWait waitForReload) {
        traverseTillFrmTreeFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getChannelCategoriesTab());
        getChannelCategoriesTab().click();
        CSLogger.info("Clicked on Channel Categories Tab");
    }

    /**
     * Switches till frmTree frame
     * 
     * @param waitForReload
     *            this is WebDriverWait instance
     */
    public void traverseTillFrmTreeFrame(WebDriverWait waitForReload) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrame190()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrmTree()));
        CSLogger.info("Traversed till frmTree Frame");
    }

    /**
     * Switches till pdmarticle frame
     * 
     * @param waitForReload
     *            this is WebDriverWait instance
     */
    public void traverseTillProductCategoriesClassList(
            WebDriverWait waitForReload) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseTillFrmTreeFrame(waitForReload);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmPdmArticle()));
        CSLogger.info("Traversed till pdmarticle frame");
    }

    /**
     * Switches till pdmarticle Structure frame
     * 
     * @param waitForReload
     *            this is WebDriverWait instance
     */
    public void traverseTillChannelCategoriesClassList(
            WebDriverWait waitForReload) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseTillFrmTreeFrame(waitForReload);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmPdmArticleStructure()));
        CSLogger.info("Traversed till pdmarticle Structure frame ");
    }
}
