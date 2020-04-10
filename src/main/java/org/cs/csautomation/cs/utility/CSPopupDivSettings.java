
package org.cs.csautomation.cs.utility;

import org.cs.csautomation.cs.settings.IAutomationNodePopup;
import org.cs.csautomation.cs.settings.IAutomationPopup;
import org.cs.csautomation.cs.settings.IFlexTablePopup;
import org.cs.csautomation.cs.settings.ITranslationManagerPopup;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.settings.IValueListPopup;
import org.cs.csautomation.cs.settings.IWorkflowActionsPopup;
import org.cs.csautomation.cs.settings.IWorkflowStatesPopup;
import org.cs.csautomation.cs.settings.IWorkflowsNodePopup;
import org.cs.csautomation.cs.settings.IWorkflowsPopup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains pop up web element of settings page.
 * 
 */

public class CSPopupDivSettings implements IUserManagementPopup,
        IWorkflowStatesPopup, IWorkflowsPopup, IWorkflowsNodePopup,
        IWorkflowActionsPopup, IAutomationNodePopup, IAutomationPopup,
        IFlexTablePopup, IValueListPopup, ITranslationManagerPopup {

    private static CSPopupDivSettings locators;
    private WebDriver                 browserDriverInstance;

    @FindBy(xpath = "//td[contains(text(),'Create new')]")
    private WebElement                csPopupCreateNew;

    @FindBy(xpath = "//td[contains(text(),'Refresh')]")
    private WebElement                csPopupRefresh;

    @FindBy(xpath = "//td[contains(text(),'New Access Level')]")
    private WebElement                csPopupNewAccessLevel;

    @FindBy(xpath = "//td[contains(text(),'New Child')]")
    private WebElement                csPopupNewChild;

    @FindBy(xpath = "//td[contains(text(),'New Group')]")
    private WebElement                csPopupNewGroup;

    @FindBy(xpath = "//input[@id='userInput']")
    private WebElement                userInput;

    @FindBy(xpath = "//input[@id='CSGUI_MODALDIALOG_OKBUTTON']")
    private WebElement                btnOk;

    @FindBy(xpath = "//input[@id='CSGUI_MODALDIALOG_CANCELBUTTON']")
    private WebElement                btnCancel;

    @FindBy(xpath = "//td[contains(text(),'Object')]")
    private WebElement                csPopupObject;

    @FindBy(xpath = "//td[contains(text(),'Edit')]")
    private WebElement                csPopupEdit;

    @FindBy(xpath = "//td[contains(text(),'New Folder')]")
    private WebElement                csPopupNewFolder;

    @FindBy(id = "Title")
    private WebElement                txtCsGuiAttributeTitle;

    @FindBy(xpath = "//td[contains(text(),'Delete')]/..")
    private WebElement                csPopupDelete;

    @FindBy(xpath = "//td[contains(text(),'Remove')]")
    private WebElement                csPopupRemove;

    @FindBy(xpath = "//td[@title='Delete State']/..")
    private WebElement                csPopupMenuDeleteState;

    @FindBy(xpath = "//td[@title='Delete Workflow']/..")
    private WebElement                csPopupMenuDeleteWorkflow;

    @FindBy(xpath = "//td[@title='New Workflow']")
    private WebElement                csPopupMenuNewWorkflows;

    @FindBy(xpath = "//td[@title='Delete Action']/..")
    private WebElement                csPopupMenuDeleteAction;

    @FindBy(xpath = "//td[@title='Run Now']/..")
    private WebElement                csPopupMenuRunNow;

    @FindBy(id = "CSPopupDiv")
    private WebElement                csPopupDiv;

    @FindBy(id = "CSPopupDiv_SUB")
    private WebElement                csPopupDivSub;

    @FindBy(xpath = "//td[contains(text(),'Add')]")
    private WebElement                ctxAdd;

    @FindBy(xpath = "//td[contains(text(),'Refresh')]")
    private WebElement                ctxRefresh;

    @FindBy(xpath = "//td[contains(text(),'Delete')]")
    private WebElement                ctxDelete;

    @FindBy(xpath = "//td[contains(text(),'Duplicate')]")
    private WebElement                ctxDuplicate;

    @FindBy(xpath = "//td[contains(text(),'Object')]")
    private WebElement                ctxObject;

    @FindBy(id = "userInput")
    private WebElement                txtCsGuiModalDialogFormName;

    @FindBy(xpath = "//td[contains(text(),'Data Language')]")
    private WebElement                ctxDataLanguage;

    @FindBy(xpath = "//td[contains(text(),'Translation Job')]")
    private WebElement                ctxTranslationJob;

    /**
     * Returns the instance of translation job
     * 
     * @return ctxTranslationJob
     */
    public WebElement getCtxTranslationJob() {
        return ctxTranslationJob;
    }

    /**
     * Returns the instance of data language option
     * 
     * @return ctxDataLanguage
     */
    public WebElement getCtxDataLanguage() {
        return ctxDataLanguage;
    }

    /**
     * This method returns the instance of Duplicate
     * 
     * @return ctxDuplicate
     */
    public WebElement getCtxDuplicate() {
        return ctxDuplicate;
    }

    /**
     * This method returns the instance of refresh
     * 
     * @return ctxRefresh
     */
    public WebElement getCtxRefresh() {
        return ctxRefresh;
    }

    /**
     * This method returns the instance of add of pop up to add value lsit
     * 
     * @return ctxAdd
     */
    public WebElement getCtxAdd() {
        return ctxAdd;
    }

    /**
     * This method returns the instance of delete option from pop up
     * 
     * @return csPopupDelete
     */
    public WebElement getCsPopupDelete() {
        return csPopupDelete;
    }

    /**
     * Returns the webElement txtCsGuiModalDialogFormName
     * 
     * @return txtCsGuiModalDialogFormName
     */
    public WebElement getTxtCsGuiAttributeTitle() {
        return txtCsGuiAttributeTitle;
    }

    /**
     * This method returns the instance of New folder option
     * 
     * @return csPopupNewFolder
     */
    public WebElement getCsPopupNewFolder() {
        return csPopupNewFolder;
    }

    /**
     * This method returns the instance of cancel button
     * 
     * @return btnCancel
     */
    public WebElement getBtnCancel() {
        return btnCancel;
    }

    /**
     * This method returns the instance of edit option after clicking on object
     * option
     * 
     * @return csPopupEdit
     */
    public WebElement getCsPopupEdit() {
        return csPopupEdit;
    }

    /**
     * This method returns the instance of object option
     * 
     * @return csPopupObject
     */
    public WebElement getCsPopupObject() {
        return csPopupObject;
    }

    /**
     * This method returns the instance of ok button
     * 
     * @return btnOk
     */
    public WebElement getBtnOk() {
        return btnOk;
    }

    /**
     * This method returns the instance of user input
     * 
     * @return userInput
     */
    public WebElement getUserInput() {
        return userInput;
    }

    /**
     * This method returns the instance of new group option
     * 
     * @return csPopupNewGroup
     */
    public WebElement getCsPopupNewGroup() {
        return csPopupNewGroup;
    }

    /**
     * This method returns the instance of create new option
     * 
     * @return csPopupCreateNew
     */
    public WebElement getCsPopupCreateNew() {
        return csPopupCreateNew;
    }

    /**
     * This method returns the instance of refresh option
     * 
     * @return csPopupRefresh
     */
    public WebElement getCsPopupRefresh() {
        return csPopupRefresh;
    }

    /**
     * This method returns the instance of new access level option
     * 
     * @return csPopupNewAccessLevel
     */
    public WebElement getCsPopupNewAccessLevel() {
        return csPopupNewAccessLevel;
    }

    /**
     * This method returns the instance of New Child option
     * 
     * @return csPopupnNewChild
     */
    public WebElement getCsPopupNewChild() {
        return csPopupNewChild;
    }

    public CSPopupDivSettings(WebDriver browserDriverInstance) {
        setBrowserDriverInstance(browserDriverInstance);
        PageFactory.initElements(browserDriverInstance, this);
    }

    /**
     * This method sets instance of browser driver
     * 
     * @param browserDriverInstance contains webDriver instance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    public static CSPopupDivSettings
            getCSPopupDivLocators(WebDriver browserDriverInstance) {
        if (locators == null) {
            locators = new CSPopupDivSettings(browserDriverInstance);
        }
        return locators;
    }

    /**
     * This method selects option from pop up menu
     * 
     * @param waitForReload waits for an element to reload
     * @param subMenuToBeSelected submenu to be selected
     * @param browserDriverInstance browser driver instance
     */
    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivFrame()));
        waitForReload
                .until(ExpectedConditions.visibilityOf(subMenuToBeSelected));
        subMenuToBeSelected.click();
        CSLogger.info("Menu Selected");
    }

    /**
     * This method handles pop up for ok or cancel
     * 
     * @param waitForReload waits for an element to reload
     * @param element element to be selected
     * @param browserDriver browser driver instance
     */
    public void handlePopup(WebDriverWait waitForReload, String element,
            WebDriver browserDriver) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.visibilityOf(getUserInput()));
        getUserInput().clear();
        CSUtility.tempMethodForThreadSleep(1000);
        getUserInput().sendKeys(element);
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
     * This method clicks on ok of pop up
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkOkOfPopup(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getBtnOk()));
        getBtnOk().click();
        CSLogger.info("Clicked on OK of pop up");
    }

    /**
     * This method clicks on cancel of pop up waitForReload waits for an element
     * to reload
     */
    public void clkCancelOfPopup(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getBtnCancel()));
        getBtnCancel().click();
        CSLogger.info("Clicked on Cancel of pop up");
    }

    /**
     * Returns the WebElement of workflow's delete state.
     * 
     * @return WebElement csPopupMenuDeleteState.
     */
    public WebElement getCsPopupMenuDeleteState() {
        return csPopupMenuDeleteState;
    }

    /**
     * Returns the WebElement of workflow.
     * 
     * @return WebElement csPopupMenuDeleteWorkflow.
     */
    public WebElement getCsPopupMenuDeleteWorkflow() {
        return csPopupMenuDeleteWorkflow;
    }

    /**
     * This method returns the popup WebElement of create new workflow.
     * 
     * @return WebElement csGuiPopupMenuNewWorkflows.
     */
    public WebElement getCsPopupMenuNewWorkflows() {
        return csPopupMenuNewWorkflows;
    }

    /**
     * Returns the WebElement csGuiPopupMenuDeleteAction.
     * 
     * @return WebElement csGuiPopupMenuDeleteAction.
     */
    public WebElement getCsPopupMenuDeleteAction() {
        return csPopupMenuDeleteAction;
    }

    /**
     * This method returns the instance of remove option
     * 
     * @return csPopupRemove
     */
    public WebElement getCsPopupRemove() {
        return csPopupRemove;
    }

    /**
     * Returns the pop up WebElement of 'run now' when performed right click
     * operation on created automation.
     * 
     * @return WebElement csPopupMenuRunNow.
     */
    public WebElement getCsPopupMenuRunNow() {
        return csPopupMenuRunNow;
    }

    /**
     * Returns the WebElement csPopupDiv.
     * 
     * @return WebElement csPopupDiv.
     */
    public WebElement getCsPopupDiv() {
        return csPopupDiv;
    }

    /**
     * Returns the webElement csPopupDivSub
     * 
     * @return
     */
    public WebElement getCsPopupDivSub() {
        return csPopupDivSub;
    }

    /**
     * This method Hovers over sub menu
     * 
     * @param waitForReload waits for element to reload
     * @param hoverOnWebElement webElement which will be hovered
     * @param browserDriverInstance contains the instance of browser T
     */
    public void hoverOnCsGuiPopupMenu(WebDriver browserDriverInstance,
            WebDriverWait waitForReload, WebElement hoverOnWebElement) {
        traverseToCsPopupDivFrame(waitForReload, browserDriverInstance);
        Actions action = new Actions(browserDriverInstance);
        action.moveToElement(hoverOnWebElement).build().perform();
    }

    /**
     * Switches the frames till CsPopupDivFrame
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance WebDriver instance
     */
    public void traverseToCsPopupDivFrame(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.visibilityOf(getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivFrame()));
        CSLogger.info("Traversed to CsPopupDivFrame ");
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
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(
                ExpectedConditions.elementToBeClickable(getCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
        CSLogger.info("Switched to cs pop div sub frame");
        waitForReload
                .until(ExpectedConditions.visibilityOf(subMenuToBeSelected));
        subMenuToBeSelected.click();
        CSLogger.info("Submenu Selected");
    }

    /**
     * This method will get the WebElement for right click option Delete
     * 
     * @return - ctxDeleteOpenSearch - element for right click option Delete
     */
    public WebElement getCtxDelete() {
        return ctxDelete;
    }

    /**
     * This method will return the element for context Object in open search
     * 
     * @return ctxObject
     */
    public WebElement getCtxObject() {
        return ctxObject;
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
        CSLogger.info("Switched To Cs Portal Window Content Frame");
        if (pressOkay) {
            waitForReload
                    .until(ExpectedConditions.elementToBeClickable(getBtnOk()));
            getBtnOk().click();
            CSLogger.info("Clicked on OK Of Popup");
        } else {
            waitForReload.until(
                    ExpectedConditions.elementToBeClickable(getBtnCancel()));
            getBtnCancel().click();
            CSLogger.info("Clicked on Cancel of Popup");
        }
    }

    /**
     * This method gets the instance of textbox where user sends value in dialog
     * in pim studio
     * 
     * @param waitForReload waits to reload the element
     * @param userInputFolderName contains the name of the folder to be created
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
        getTxtCsGuiModalDialogFormName().clear();
        waitForReload.until(ExpectedConditions.textToBePresentInElement(
                getTxtCsGuiModalDialogFormName(), ""));
        getTxtCsGuiModalDialogFormName().sendKeys(userInputFolderName);
        if (!getTxtCsGuiModalDialogFormName().getAttribute("value")
                .equals(userInputFolderName)) {
            enterValueInDialogue(waitForReload, userInputFolderName);
        }
        CSLogger.info("Entered Value In Dialogue");
    }

    /**
     * Returns the webElement txtCsGuiModalDialogFormName
     * 
     * @return txtCsGuiModalDialogFormName
     */
    public WebElement getTxtCsGuiModalDialogFormName() {
        return txtCsGuiModalDialogFormName;
    }

}
