/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.mam;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author priyanka.patil
 *
 */
public class CSGuiToolbarHorizontalMam {

    @FindBy(xpath = "//a/img[contains(@src,'save.gif')]")
    private WebElement btnSaveMam;

    @FindBy(xpath = "//td[contains(text(),'Checkout')]")
    private WebElement checkoutOption;

    @FindBy(xpath = "//a/img[contains(@src,'checkin.gif')]")
    private WebElement btnCheckin;

    @FindBy(xpath = "//a/img[contains(@src,'checkout.gif')]")
    private WebElement btnCheckout;

    @FindBy(xpath = "//a/img[contains(@src,'studiomenu')]")
    private WebElement btnStudioMenu;

    @FindBy(
            xpath = "(//img[@class='CSGuiToolbarButtonImage'])[contains(@src,'studiomenu')]")
    private WebElement btnMoreOptions;

    @FindBy(xpath = "//a/img[contains(@src,'refresh.gif')]")
    private WebElement btnRefresh;

    public WebElement getBtnMoreOptions() {
        return btnMoreOptions;
    }

    /**
     * This method returns the instance of studio menu
     * 
     * @return btnStudioMenu
     */
    public WebElement getBtnStudioMenu() {
        return btnStudioMenu;

    }

    /**
     * This method returns instance of checkout button
     * 
     * @return btnCheckOut
     */
    public WebElement getBtnCheckout() {
        return btnCheckout;
    }

    /**
     * This method returns instance of checkin button
     * 
     * @return btnCheckin
     */
    public WebElement getBtnCheckIn() {
        return btnCheckin;
    }

    /**
     * This method returns instance of checkout button after right clicking demo
     * volume folder
     * 
     * @return clkCheckout
     */
    public WebElement getCheckoutOption() {
        return checkoutOption;
    }

    /**
     * This method returns the instance of save button in mam
     * 
     * @return btnSaveMam
     */
    public WebElement getBtnSaveMam() {
        return btnSaveMam;
    }

    public CSGuiToolbarHorizontalMam(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized CSGuiToolbarHorizontal page POM elements.");
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
    }

    /**
     * Getter for btnRefresh
     * 
     * @return the btnRefresh
     */
    public WebElement getBtnRefresh() {
        return btnRefresh;
    }

    /**
     * Setter for btnRefresh
     * 
     * @param btnRefresh the btnRefresh to set
     */
    public void setBtnRefresh(WebElement btnRefresh) {
        this.btnRefresh = btnRefresh;
    }

}
