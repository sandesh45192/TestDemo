
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

public class ModalDialogPopupWindow {

    private WebDriver  browserDriverInstance;

    @FindBy(xpath = "//div[@class='CSPortalWindow']/div/div")
    private WebElement csPortalWindowDiv;

    @FindBy(xpath = "//input[@value='Yes']")
    private WebElement btnCsGuiModalDialogYesButton;

    @FindBy(xpath = "//input[@value='No']")
    private WebElement btnCsGuiModalDialogNoButton;
    
    @FindBy(id = "CSGUI_MODALDIALOG_CANCELBUTTON")
    private WebElement        btnCsGuiModalDialogCancelButton;

    @FindBy(id = "CSGUI_MODALDIALOG_OKBUTTON")
    private WebElement        btnCsGuiModalDialogOkButton;

    public ModalDialogPopupWindow(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized  ModalDialogPopupWindow page POM elements.");
    }

    /**
     * This method returns the instance of cancel button
     * 
     * @return the btnCsGuiModalDialogCancelButton
     */
    public WebElement getBtnCsGuiModalDialogCancelButton() {
        return btnCsGuiModalDialogCancelButton;
    }

    /**
     * Returns the webElement btnCsGuiModalDialogOkButton
     * 
     * @return btnCsGuiModalDialogOkButton
     */
    public WebElement getBtnCsGuiModalDialogOkButton() {
        return btnCsGuiModalDialogOkButton;
    }

    /**
     * This method clicks on ok button
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnCsGuiModalDialogOkButton(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getBtnCsGuiModalDialogOkButton()));
        getBtnCsGuiModalDialogOkButton().click();
        CSLogger.info("Clicked on ok button");
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
     * Returns the webElement csPortalWindowDiv
     * 
     * @return csPortalWindowDiv
     */
    public WebElement getCsPortalWindowDiv() {
        return csPortalWindowDiv;
    }

    /**
     * Returns the webElement btnCsGuiModalDialogYesButton
     * 
     * @return btnCsGuiModalDialogYesButton
     */
    public WebElement getBtnCsGuiModalDialogYesButton() {
        return btnCsGuiModalDialogYesButton;
    }

    /**
     * Returns the webElement btnCsGuiModalDialogNoButton
     * 
     * @return btnCsGuiModalDialogNoButton
     */
    public WebElement getBtnCsGuiModalDialogNoButton() {
        return btnCsGuiModalDialogNoButton;
    }

    /**
     * Handles Yes and No option of 'Add All Values' pop up dialog window
     * 
     * @param waitForReload waits for element to reload
     * @param pressYes Boolean parameter containing true or false value
     */
    public void handleMessageModalDialogWindow(WebDriverWait waitForReload,
            boolean pressYes) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getCsPortalWindowDiv());
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowConfirmDialog()));
        CSLogger.info("Switched To CsPortalWindow ConfirmDialog Frame");
        if (pressYes) {
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(getBtnCsGuiModalDialogYesButton()));
            getBtnCsGuiModalDialogYesButton().click();
            CSLogger.info("Clicked on Yes option   Popup");
        } else {
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(getBtnCsGuiModalDialogNoButton()));
            getBtnCsGuiModalDialogNoButton().click();
            CSLogger.info("Clicked on No option Popup");
        }
    }

    public void handlePopupDataSelectionDialog(WebDriverWait waitForReload,
            boolean status) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowWidget()));
        if (status) {
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(getBtnCsGuiModalDialogOkButton()));
            getBtnCsGuiModalDialogOkButton().click();
            CSLogger.info("Clicked on ok option Popup");
        } else {
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    getBtnCsGuiModalDialogCancelButton()));
            getBtnCsGuiModalDialogCancelButton().click();
            CSLogger.info("Clicked on Cancel option Popup");
        }
    }
    
    /**
     * This method performs ok/cancel operation in a dialog that opens after
     * selecting desired menu/submenu
     * 
     * @param waitForReload
     *            waits for element to reload
     * @param pressOkay
     *            holds boolean value true/false
     * @param browserDriverInstance
     *            contains the instance of browser driver
     */
    public void askBoxWindowOperation(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To Cs Portal Window Content Frame");
        if (pressOkay) {
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(getBtnCsGuiModalDialogOkButton()));
            getBtnCsGuiModalDialogOkButton().click();
            CSLogger.info("Clicked on OK Of Popup");
        } else {
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    getBtnCsGuiModalDialogCancelButton()));
            getBtnCsGuiModalDialogCancelButton().click();
            CSLogger.info("Clicked on Cancel of Popup");
        }
    }
}
