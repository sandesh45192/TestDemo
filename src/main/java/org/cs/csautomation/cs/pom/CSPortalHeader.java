/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CSPortalHeader {

    private WebDriver     browserDriverInstance;
    private FrameLocators iframeLoactorsInstance;

    @FindBy(id = "CSPortalTabButtonTitle_103")
    WebElement            btnProducts;

    @FindBy(id = "CSPortalTabButtonTitle_104")
    WebElement            btnMedia;

    @FindBy(id = "CSPortalTabButton_68")
    WebElement            btnSettings;

    @FindBy(xpath = "//li[@id='CSPortalLinkPortalOptions']")
    WebElement            btnCsPortalLinkOptions;

    @FindBy(id = "CSPortalTabButtonTitle_102")
    WebElement            btnSearch;

    @FindBy(xpath = "//span[@id='CSPortalTabButtonTitle_118']")
    WebElement            btnHome;

    public CSPortalHeader(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized CSPortalHeader page POM elements.");
        iframeLoactorsInstance = new FrameLocators(browserDriverInstance);
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
     * This method returns the instance of portal link option button
     * 
     * @return btnCsPortalLinkOptions
     */
    public WebElement getBtnCsPortalLinkOptions() {
        return btnCsPortalLinkOptions;
    }

    /**
     * Returns the webelement btnMedia
     * 
     * @return btnMedia CSPortalHeader
     */

    public WebElement getBtnMedia() {
        return btnMedia;
    }

    /**
     * Returns the WebElement btnProducts
     * 
     * @return btnProducts CSPortalHeader
     */
    public WebElement getBtnProducts() {
        return btnProducts;
    }

    /**
     * This method returns the instance of home button
     * 
     * @return btnHome
     */
    public WebElement getBtnHome() {
        return btnHome;
    }

    /**
     * This method clicks on the Home button.
     * 
     * @param waitForReload this is webDriverWait object.
     */
    public void clkBtnHome(WebDriverWait waitForReload) {
        waitForBtnHomeToLoad(waitForReload);
        getBtnHome().click();
        CSLogger.info("Clicked on Home button");
    }

    /**
     * This method waits for home button to load
     * 
     * @param waitForReload waits for an element to reload
     */
    private void waitForBtnHomeToLoad(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        iframeLoactorsInstance.switchToTopFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnHome());
        CSLogger.info("Waiting completed for visibility of Home tab");
    }

    /**
     * returns the WebElement of the Header settings button. WebElement ID is
     * declared in the Variable declaration part
     * 
     * @return btnSettings contains the Settings WebElement based on the ID
     */
    public WebElement getBtnSettings() {
        return btnSettings;
    }

    /**
     * This method waits for visibility of Products Tab
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void waitForBtnProductToLoad(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        iframeLoactorsInstance.switchToTopFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnProducts());
        CSLogger.info("Waiting Completed for visibilty of products tab");
    }

    /**
     * This method waits for the visibility of Media tab
     * 
     * @param waitForReload this is webDriverWait object
     * @param waitForReload waits for an element to reload
     */
    public void waitForBtnMediaToLoad(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        iframeLoactorsInstance.switchToTopFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnMedia());
        CSLogger.info("Waiting completed for visibility of Media tab");
    }

    /**
     * This method clicks on the Media button.
     * 
     * @param waitForReload this is webDriverWait object.
     */
    public void clkBtnMedia(WebDriverWait waitForReload) {
        waitForBtnMediaToLoad(waitForReload);
        getBtnMedia().click();
        CSLogger.info("Clicks on media button");
    }

    /**
     * This method clicks on portal link options button
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnCsPortalLinkOptions(WebDriverWait waitForReload) {
        waitForBtnPortalLinkOptionsToLoad(waitForReload);
        getBtnCsPortalLinkOptions().click();
        CSLogger.info("Clicked on Portal Link Options button");
    }

    /**
     * This method waits for portal link options to load
     * 
     * @param waitForReload waits for an element to reload
     */
    private void
            waitForBtnPortalLinkOptionsToLoad(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        iframeLoactorsInstance.switchToTopFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnCsPortalLinkOptions());
        CSLogger.info(
                "Waiting completed for visibility of portal link options tab");
    }

    /**
     * Clicks on Product Tab.
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnProducts(WebDriverWait waitForReload) {
        waitForBtnProductToLoad(waitForReload);
        getBtnProducts().click();
        CSLogger.info("Clicked on Products Tab");
    }

    /**
     * This method waits for visibility of ValuelistSettings Tab
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void waitForBtnValuelistToLoad(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        iframeLoactorsInstance.switchToTopFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnProducts());
        CSLogger.info("Waiting Completed for visibilty of products tab");
    }

    /**
     * This method waits for visibility of Settings Tab
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void waitForSettingsMenuToLoad(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        iframeLoactorsInstance.switchToTopFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnSettings());
        CSLogger.info("Waiting Completed for visibilty of settings tab");
    }

    /**
     * This method clicks on the Settings header Tabs
     *
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnSettingsTab(WebDriverWait waitForReload) {
        waitForSettingsMenuToLoad(waitForReload);
        getBtnSettings().click();
        CSLogger.info("Clicked on Settings header tab");
    }

    /**
     * This method will return the header 'Search'
     * 
     * @return btnSearch
     */
    public WebElement getBtnSearch() {
        return btnSearch;
    }

    /**
     * This method waits for visibility of Search header
     * 
     * @param waitForReload - this is webDriverWait object
     */
    public void waitForSearchHeaderToLoad(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        iframeLoactorsInstance.switchToTopFrame(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnSearch());
    }

    /**
     * This method will click on search header
     * 
     * @param waitForReload - this is webDriverWait object
     */
    public void clkSearchHeader(WebDriverWait waitForReload) {
        waitForSearchHeaderToLoad(waitForReload);
        getBtnSearch().click();
        CSLogger.info("Clicked on Search header");
        CSUtility.tempMethodForThreadSleep(2000);
    }
}
