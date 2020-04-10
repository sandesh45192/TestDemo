/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import org.cs.csautomation.cs.mam.IVolumePopup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CSPopupDivMam implements IVolumePopup {

    private static CSPopupDivMam locatorsMam;

    private WebDriver            browserDriverInstance;

    @FindBy(xpath = "//td[contains(text(),'Create New File')]")
    private WebElement           createNewFile;

    @FindBy(id = "CSPopupDiv")
    private WebElement           csPopupDiv;

    @FindBy(xpath = "//input[contains(@id,'RecordTitle')]")
    private WebElement           userInputMamStudio;

    @FindBy(id = "CSGUI_MODALDIALOG_CANCELBUTTON")
    private WebElement           btnCsGuiModalDialogCancelButton;

    @FindBy(id = "CSGUI_MODALDIALOG_OKBUTTON")
    private WebElement           btnCsGuiModalDialogOkButton;

    @FindBy(xpath = "//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL']")
    private WebElement           btnCancelMamStudio;

    @FindBy(xpath = "//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_OK']")
    private WebElement           btnOkMamStudio;

    @FindBy(id = "userInput")
    private WebElement           txtCsGuiModalDialogFormName;

    @FindBy(xpath = "//td[contains(text(),'Create New Folder')]")
    private WebElement           createNewFolder;

    @FindBy(xpath = "//td[contains(text(),'Create new')]")
    private WebElement           createNewMamAttribute;

    @FindBy(xpath = "//input[2]")
    private WebElement           btnExtendMam;

    @FindBy(xpath = "//input[3]")
    private WebElement           btnReplaceMam;

    @FindBy(xpath = "//td[contains(text(),'Object')]")
    private WebElement           csPopupObject;

    @FindBy(xpath = "//td[contains(text(),'Edit')]")
    private WebElement           csPopupEdit;

    @FindBy(xpath = "//td[contains(text(),'Archive in Standard Archiv')]")
    private WebElement           csPopupArchive;

    @FindBy(xpath = "//td[contains(text(),'Restore Archived File')]")
    private WebElement           csPopupRestoreArchive;

    @FindBy(xpath = "//td[contains(text(),'Process')]")
    private WebElement           csPopupProcess;

    @FindBy(xpath = "//td[contains(text(),'Download as ZIP File')]")
    private WebElement           csPopupDownloadAsZip;

    @FindBy(xpath = "//td[@title='Delete version']")
    private WebElement           csPopupDeleteVersion;

    @FindBy(xpath = "//td[contains(text(),'Delete older versions')]")
    private WebElement           csPopupDeleteOldVersions;

    @FindBy(xpath = "//td[contains(text(),'Restore Version')]")
    private WebElement           csPopuprestoreVersion;

    @FindBy(xpath = "//td[contains(text(),'Rollback Version')]")
    private WebElement           csPopupRollbackVersion;

    @FindBy(xpath = "//td[contains(text(),'Create Link')]")
    private WebElement           csPopupCreateLink;

    @FindBy(xpath = "//td[contains(text(),'Remove Link')]")
    private WebElement           csPopupRemoveLink;

    @FindBy(xpath = "//td[contains(text(),'Delete')]")
    private WebElement           csPopupDelete;

    @FindBy(xpath = "//td[contains(text(),'Duplicate')]")
    private WebElement           duplicateOption;

    @FindBy(xpath = "//td[contains(text(),'Check for Duplicates')]")
    private WebElement           checkForDuplicatesOption;

    @FindBy(xpath = "//td[contains(text(),'View Folder as Editor')]")
    private WebElement           viewFolderAsEditor;

    @FindBy(xpath = "//td[contains(text(),'Tools')]")
    private WebElement           toolsOption;

    @FindBy(xpath = "//td[contains(text(),'Remove')]")
    private WebElement           csPopupRemove;

    @FindBy(xpath = "//td[contains(text(),'Add Again')]")
    private WebElement           csPopupAddAgain;

    @FindBy(id = "Title")
    private WebElement           txtCsGuiAttributeTitle;

    @FindBy(xpath = "//td[contains(text(),'Upload New File')]")
    private WebElement           optionUploadNewFile;

    @FindBy(xpath = "//td[contains(text(),'Create Whiteboard')]")
    private WebElement           csPopupCreateWhiteboard;

    @FindBy(xpath = "//td[contains(text(),'Refresh')]")
    private WebElement           csPopupRefresh;

    @FindBy(id = "CSPopupDiv_SUB")
    private WebElement           csPopupDivSub;

    @FindBy(
            xpath = "//tr[@name='SUBITEM_Process']//td[@class='CSGuiPopupLabel'][contains(text(),'Process')]")
    private WebElement           ctxProcess;

    /**
     * Returns the webElement csPopupDivSub
     * 
     * @return
     */
    public WebElement getCsPopupDivSub() {
        return csPopupDivSub;
    }

    /**
     * returns the instance of upload new file after right clicking on folder
     * 
     * @return clkUploadNewFile
     */
    public WebElement getClkUploadNewFile() {
        return optionUploadNewFile;
    }

    /**
     * This method returns the instance of add again option
     * 
     * @return optAddAgain
     */
    public WebElement getClkAddAgain() {
        return csPopupAddAgain;
    }

    /**
     * This method returns instance of remove section
     * 
     * @return optRemove
     */
    public WebElement getClkRemove() {
        return csPopupRemove;
    }

    /**
     * This method returns the instance of toolsoption
     */
    public WebElement getToolsOption() {
        return toolsOption;
    }

    /**
     * This method returns the instance of view folder as editor option
     */
    public WebElement getViewFolderAsEditor() {
        return viewFolderAsEditor;
    }

    /**
     * This method returns the instance of checkforduplicatesoption
     */
    public WebElement getCheckForDuplicatesOption() {
        return checkForDuplicatesOption;
    }

    /**
     * This method returns the instance of duplicate option
     */
    public WebElement getDuplicateOption() {
        return duplicateOption;
    }

    /**
     * This method returns the instance of delete option
     */
    public WebElement getCsPopupDelete() {
        return csPopupDelete;
    }

    /**
     * This method returns the instance of remove link after clicking on object
     * option
     * 
     * @return csPopupRemoveLink
     */
    public WebElement getCsPopupRemoveLink() {
        return csPopupRemoveLink;
    }

    /**
     * This method returns the instance of create link after clicking on object
     * option
     * 
     * @return csPopupCreateLink
     */
    public WebElement getCsPopupCreateLink() {
        return csPopupCreateLink;
    }

    /**
     * This method returns instance of rollback version after right clicking on
     * version
     * 
     * @return csPopupRollbackVersion
     */
    public WebElement getCsPopupRollbackVersion() {
        return csPopupRollbackVersion;
    }

    /**
     * This method returns the instance of restore version option after right
     * click
     */
    public WebElement getRestoreVersionOption() {
        return csPopuprestoreVersion;
    }

    /**
     * This method returns the instance of delete old versions option after
     * right click on version return deleteOldVersionsOption
     */
    public WebElement getDeleteOldVersionsOption() {
        return csPopupDeleteOldVersions;
    }

    /**
     * This method returns the instance of deleteVersion option after right
     * clicking on version return deleteVersionOption
     */
    public WebElement getDeleteVersionOption() {
        return csPopupDeleteVersion;
    }

    /**
     * This method returns instance of download as zip option after right click
     * on folder return downloadAsZipOption
     */
    public WebElement getDownloadAsZipOption() {
        return csPopupDownloadAsZip;
    }

    /**
     * This method returns the instance of process option after right click on
     * folder return processOption
     */
    public WebElement getProcessOption() {
        return csPopupProcess;
    }

    /**
     * This method returns the instance of restore archive option after right
     * click on object option return restoreArchiveOption
     */
    public WebElement getRestoreArchiveOption() {
        return csPopupRestoreArchive;
    }

    /**
     * This method returns instance of archive option after right click on
     * object option return archiveOptio
     */
    public WebElement getArchiveOption() {
        return csPopupArchive;
    }

    /**
     * This method returns instance of object option after right click return
     * objectOption
     */
    public WebElement getObjectOption() {
        return csPopupObject;
    }

    /**
     * This method returns instance of edit option return editOption
     */
    public WebElement getEditOption() {
        return csPopupEdit;
    }

    /**
     * This method returns instance of replace button after right click return
     * btnReplaceMam
     */
    public WebElement getBtnReplaceMam() {
        return btnReplaceMam;
    }

    /**
     * This method returns instance of extend button return btnExtendMam
     */
    public WebElement getBtnExtendMam() {
        return btnExtendMam;
    }

    /**
     * This method returns instance of create new attribute in mam under
     * settings node return btnCreateNewMamAttribute
     */
    public WebElement getCreateNewMamAttribute() {
        return createNewMamAttribute;
    }

    /**
     * This method returns instance of new folder option after right clicking on
     * demo volume node return createNewFolder option
     */
    public WebElement getCreateNewFolder() {
        return createNewFolder;
    }

    /**
     * This method returns the instance of ok button while creating class in mam
     * studio return txtCsGuiModalDialogFormName
     */
    public WebElement getTxtCsGuiModalDialogFormName() {
        return txtCsGuiModalDialogFormName;
    }

    /**
     * This method returns the instance of OK Button while creating demo volume
     * folder
     * 
     * @return btnOkMamStudio
     */
    public WebElement getBtnOkMamStudio() {
        return btnOkMamStudio;
    }

    /**
     * This method returns the instance of Cancel Button while creating demo
     * volume folder
     * 
     * @return btnCancelMamStudio
     */
    public WebElement getBtnCancelMamStudio() {
        return btnCancelMamStudio;
    }

    /**
     * This method returns instance of OK button after adding file to DemoVolume
     * folder
     * 
     * @return btnCsGuiModalDialogOkButton
     */
    public WebElement getBtnCsGuiModalDialogOkButton() {
        return btnCsGuiModalDialogOkButton;
    }

    /**
     * This method returns instance of OK button after adding file to DemoVolume
     * folder
     * 
     * @return btnCsGuiModalDialogCancelButton
     */
    public WebElement getBtnCsGuiModalDialogCancelButton() {
        return btnCsGuiModalDialogCancelButton;
    }

    /**
     * This method returns the instance of field in which user should send keys
     * for creating demo volume folder
     * 
     * @return userInputMamStudio
     */
    public WebElement getUserInputMamStudio() {
        return userInputMamStudio;
    }

    /**
     * This method returns instance of pop up div frame which appears before
     * clicking ok/cancel button
     * 
     * @return csPopupDiv
     */
    public WebElement getCsPopupDiv() {
        return csPopupDiv;
    }

    /**
     * This method returns the instance of create new file option from the pop
     * up which appears after right clicking on demo volume folder return
     * createNewFile
     */
    public WebElement getCreateNewFile() {
        return createNewFile;
    }

    public CSPopupDivMam(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
    }

    /**
     * This method sets the webDriver instance into browserDriverInstance
     * variable
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * Returns the webElement txtCsGuiModalDialogFormName
     * 
     * @return txtCsGuiModalDialogFormName
     */
    public WebElement getTxtCsGuiAttributeTitle() {
        return txtCsGuiAttributeTitle;
    }

    public static CSPopupDivMam
            getCSPopupDivLocators(WebDriver browserDriverInstance) {
        if (locatorsMam == null) {
            locatorsMam = new CSPopupDivMam(browserDriverInstance);
        }
        return locatorsMam;
    }

    /**
     * This method selects submenu from the pop up
     * 
     * @param waitForReload waits for element to reload
     * @param subMenuToBeSelected contains xpath of submenu to be selected
     * @param browserDriverInstance contains the instance of browser
     */
    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(
                ExpectedConditions.elementToBeClickable(getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivFrame()));
        waitForReload
                .until(ExpectedConditions.visibilityOf(subMenuToBeSelected));
        subMenuToBeSelected.click();
        CSLogger.info("Menu Selected");
    }

    /**
     * This method gets the instance of textbox where user sends value in dialog
     * in pim studio
     * 
     * @param waitForReload waits to reload the element
     * @param userInputFolderName contains the name of the folder to be created
     */
    public void enterValueInDialogueMamStudio(WebDriverWait waitForReload,
            String demoVolumeFolderName) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.csPortalWindowDiv));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getUserInputMamStudio()));
        getUserInputMamStudio().sendKeys(demoVolumeFolderName);
        CSLogger.info("Entered Value In Dialogue");
    }

    /**
     * This method performs ok/cancel operation in a dialog that opens after
     * selecting desired menu/submenu
     * 
     * @param waitForReload waits for element to reload
     * @param pressOkay holds boolean value true/false
     * @param browserDriverInstance contains the instance of browser driver
     */
    public void askBoxWindowOperation(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
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

    /**
     * This method selects submenu from the pop up
     * 
     * @param waitForReload waits for element to reload
     * @param subMenuToBeSelected contains xpath of submenu to be selected
     * @param browserDriverInstance contains the instance of browser
     */
    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance) {
        traverseToCsPopupDivSubFrame(waitForReload, browserDriverInstance);
        CSLogger.info("Switched to cs pop div sub frame");
        waitForReload
                .until(ExpectedConditions.visibilityOf(subMenuToBeSelected));
        subMenuToBeSelected.click();
        CSLogger.info("Submenu Selected");
    }

    /**
     * This method performs ok/cancel operation in a dialog that opens after
     * selecting desired menu/submenu
     * 
     * @param waitForReload waits for element to reload
     * @param pressOkay holds boolean value true/false
     * @param browserDriverInstance contains the instance of browser driver
     */
    public void askBoxWindowOperationMamStudio(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.csPortalWindowDiv));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        if (pressOkay) {
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(getBtnOkMamStudio()));
            getBtnOkMamStudio().click();
            CSLogger.info("Clicked on OK Of Popup");
        } else {
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(getBtnCancelMamStudio()));
            getBtnCancelMamStudio().click();
            CSLogger.info("Clicked on Cancel of Popup");
        }
    }

    /**
     * This method enters value in a dialogue
     */
    public void enterValueInDialogue(WebDriverWait waitForReload,
            String userInputFolderName) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getTxtCsGuiModalDialogFormName()));
        getTxtCsGuiModalDialogFormName().sendKeys(userInputFolderName);
        CSLogger.info("Entered Value In Dialogue");
    }

    /**
     * This method gets the instance of textbox where user sets value in dialog
     * in mam studio
     * 
     * @param waitForReload waits to reload the element
     * @param userInputFolderName contains the name of the folder to be created
     */
    public void enterValueInUserInputDialogue(WebDriverWait waitForReload,
            String userInputFolderName) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getTxtCsGuiAttributeTitle());
        getTxtCsGuiAttributeTitle().clear();
        waitForReload.until(ExpectedConditions
                .textToBePresentInElement(getTxtCsGuiAttributeTitle(), ""));
        getTxtCsGuiAttributeTitle().sendKeys(userInputFolderName);
        if (!getTxtCsGuiAttributeTitle().getAttribute("value")
                .equals(userInputFolderName)) {
            enterValueInUserInputDialogue(waitForReload, userInputFolderName);
        }
        CSLogger.info("Entered Value In Dialogue");
    }

    /**
     * Getter for csPopupCreateWhiteboard
     * 
     * @return the csPopupCreateWhiteboard
     */
    public WebElement getCsPopupCreateWhiteboard() {
        return csPopupCreateWhiteboard;
    }

    /**
     * Setter for csPopupCreateWhiteboard
     * 
     * @param csPopupCreateWhiteboard the csPopupCreateWhiteboard to set
     */
    public void setCsPopupCreateWhiteboard(WebElement csPopupCreateWhiteboard) {
        this.csPopupCreateWhiteboard = csPopupCreateWhiteboard;
    }

    /**
     * Getter for csPopupRefresh
     * 
     * @return the csPopupRefresh
     */
    public WebElement getCsPopupRefresh() {
        return csPopupRefresh;
    }

    /**
     * Setter for csPopupRefresh
     * 
     * @param csPopupRefresh the csPopupRefresh to set
     */
    public void setCsPopupRefresh(WebElement csPopupRefresh) {
        this.csPopupRefresh = csPopupRefresh;
    }

    /**
     * Traverses till FrmCsPopupDivSubFrame frame.
     * 
     * @param waitForReload
     * @param browserDriverInstance
     */
    private void traverseToCsPopupDivSubFrame(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(
                ExpectedConditions.elementToBeClickable(getCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
    }

    /**
     * Returns the webelement for context Process
     * 
     * @return ctxProcess
     */
    public WebElement getCtxProcess() {
        return ctxProcess;
    }
}
