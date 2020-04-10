
package org.cs.csautomation.cs.utility;

import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IChannelPopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IFavoritesPopup;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.settings.ISettingsPopup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CSPopupDivPim
        implements IAttributePopup, IClassPopup, IProductPopup, IChannelPopup,
        IMoreOptionPopup, IFavoritesPopup, ISettingsPopup {

    private static CSPopupDivPim locators;
    private WebDriver            browserDriverInstance;

    @FindBy(xpath = "//td[@title='ActiveScript']")
    private WebElement           activeScriptPopup;

    @FindBy(xpath = "//td[@title='New Folder']")
    private WebElement           csGuiPopupMenuNewFolder;

    @FindBy(xpath = "//td[@title='Create new']")
    private WebElement           csGuiPopupMenuCreateNew;

    @FindBy(id = "userInput")
    private WebElement           txtCsGuiModalDialogFormName;

    @FindBy(id = "CSPopupDiv")
    private WebElement           csPopupDiv;

    @FindBy(id = "CSGUI_MODALDIALOG_CANCELBUTTON")
    private WebElement           btnCsGuiModalDialogCancelButton;

    @FindBy(id = "CSGUI_MODALDIALOG_OKBUTTON")
    private WebElement           btnCsGuiModalDialogOkButton;

    @FindBy(className = "CSGuiPopupWindowDiv")
    private WebElement           popUpCSGuiWindow;

    @FindBy(xpath = "//body[@class='CSGuiPopup']")
    private WebElement           csGuiPopup;

    @FindBy(xpath = "//div[@class='CSPortalWindow']")
    private WebElement           csPortalWindow;

    @FindBy(id = "CSPopupDiv_SUB")
    private WebElement           csPopupDivSub;

    @FindBy(xpath = "//td[@title='Edit']")
    private WebElement           csGuiPopupMenuSubMenuEdit;

    @FindBy(xpath = "//td[@title='Object']")
    private WebElement           csGuiPopupMenuObject;

    @FindBy(xpath = "//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_CANCEL']")
    private WebElement           btnCancelMamStudio;

    @FindBy(xpath = "//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_OK']")
    private WebElement           btnOkMamStudio;

    @FindBy(xpath = "//input[contains(@id,'RecordTitle')]")
    private WebElement           userInputMamStudio;

    @FindBy(xpath = "//td[contains(text(),'Create New File')]")
    private WebElement           createNewFile;

    @FindBy(xpath = "//td[@title='Add']")
    private WebElement           csGuiPopupMenuAddValuelist;

    @FindBy(xpath = "//td[@title='Refresh']")
    private WebElement           csGuiPopupMenuRefresh;

    @FindBy(xpath = "//tr[@name='New Child']")
    private WebElement           csGuiPopupMenuNewChild;

    @FindBy(xpath = "//td[@title='Duplicate']")
    private WebElement           csGuiPopupMenuSubMenuDuplicate;

    @FindBy(xpath = "//input[@value='Non-recursive']")
    private WebElement           btnCsGuiModalDialogNonRecursive;

    @FindBy(xpath = "//input[@value='Recursive']")
    private WebElement           btnCsGuiModalDialogRecursive;

    @FindBy(xpath = "//td[@title='Delete version']")
    private WebElement           csGuiPopupMenuDeleteVersion;

    @FindBy(xpath = "//td[@title='Restore Version']")
    private WebElement           CsGuiPopupMenuRestoreVersion;

    @FindBy(xpath = "//td[@title='Remove']")
    private WebElement           CsGuiPopupMenuRemove;

    @FindBy(xpath = "//td[@title='View Folder as Editor']")
    private WebElement           csGuiPopupMenuSubMenuViewFolderAsEditor;

    @FindBy(xpath = "//td[@title='Rollback Version']")
    private WebElement           CsGuiPopupMenuRollbackVersion;

    @FindBy(className = "CSGUI_MODALDIALOG_TITLE")
    private WebElement           lblCsGuiModalDialogTitle;

    @FindBy(
            xpath = "//tr[@id='AttributeRow_itemstructureFixedVersion']/td[2]/div/select")
    private WebElement           ddCsGuiDialogSelectedVerion;

    @FindBy(
            xpath = "//tr[@id='AttributeRow_itemstructureCopyMode']/td[2]/div/select")
    private WebElement           ddCsGuiDialogCreateRecursively;

    @FindBy(
            xpath = "//tr[@id='AttributeRow_itemstructureSelection']/td[2]/div/select")
    private WebElement           ddCsGuiDialogSelectionType;

    @FindBy(xpath = "//tr[@id='AttributeRow_itemstructureExtend']/td[2]/span")
    private WebElement           cbCsGuiDialogEnhanceItemProduct;

    @FindBy(xpath = "//tr[@id='AttributeRow_itemstructureLabel']/td[2]/input")
    private WebElement           lblCsGuiDialogLabel;

    @FindBy(xpath = "//td[@title='Delete']")
    private WebElement           csGuiPopupMenuDeleteValuelist;

    @FindBy(xpath = "//td[@title='Duplicate']")
    private WebElement           csGuiPopupMenuDuplicateValuelist;

    @FindBy(xpath = "//tr[contains(@name,'_Favorites')]")
    private WebElement           csGuiPopupFavorites;

    @FindBy(
            xpath = "//div[@class='CSGuiPopupContainer win']//table//tbody//tr[2]")
    private WebElement           csGuiPopupSubAddFavorites;

    @FindBy(xpath = "//td[contains(text(),'Publish')]")
    private WebElement           csGuiPopupPublish;

    @FindBy(xpath = "//td[contains(text(),'Cancel publication')]")
    private WebElement           csGuiPopupCancelPublish;

    @FindBy(xpath = "//td[contains(text(),'Rename')]")
    private WebElement           csGuiPopupMenuSubMenuRename;

    @FindBy(xpath = "//td[@title='Tools']")
    private WebElement           csGuiPopupTools;

    @FindBy(xpath = "//td[contains(text(),'Access Right')]")
    private WebElement           csGuiPopupAccessRight;

    @FindBy(xpath = "//td[contains(text(),'Logout')]")
    private WebElement           csGuiPopupLogout;

    @FindBy(id = "Title")
    private WebElement           txtCsGuiAttributeTitle;

    @FindBy(xpath = "//td[contains(text(),'Portal Settings')]")
    private WebElement           csGuiPopupPortalSettings;

    @FindBy(xpath = "//td[contains(text(),'Help')]")
    private WebElement           csGuiPopupHelp;

    @FindBy(xpath = "//td[contains(text(),'Lock & Unlock Portal')]")
    private WebElement           csGuiPopupLockAndUnlockPortal;

    @FindBy(xpath = "//td[contains(text(),'Delete')]")
    private WebElement           csObjectDeleteSubMenu;

    @FindBy(xpath = "//td[contains(text(),'admin')]")
    private WebElement           csGuiPopupAdmin;

    @FindBy(xpath = "//td[contains(text(),'Tasks')]")
    private WebElement           csGuiPopupSubTasks;

    @FindBy(xpath = "//td[contains(text(),'Done')]")
    private WebElement           csGuiPopupWorkflowDone;

    @FindBy(xpath = "//select[@name='ActionID']")
    private WebElement           csGuiPopupSelectAction;

    @FindBy(xpath = "//td[contains(text(),'List Projects')]")
    private WebElement           csGuiPopupListProjects;
    @FindBy(
            xpath = "//table[@class='CSGuiPopup']/tbody/tr[1]/td[contains(text(),'Products')]")
    private WebElement           ctxMoreOptionsProducts;

    @FindBy(xpath = "//td[contains(text(),'Import CSV or Excel file')]")
    private WebElement           ctxImportCsv;

    @FindBy(xpath = "//td[@title='Workflow']")
    private WebElement           csGuiPopupMenuWorkflow;

    @FindBy(xpath = "//td[@title='Process']")
    private WebElement           csGuiPopupProcess;

    @FindBy(xpath = "//td[@title='Smart Import']")
    private WebElement           csGuiPopupSmartImport;

    @FindBy(xpath = "//td[@title='Checkin']")
    private WebElement           csGuiPopupCheckIn;

    @FindBy(xpath = "//td[contains(text(),'Checkout')]")
    private WebElement           csGuiPopupCheckOut;

    @FindBy(xpath = "//td[contains(@class,'CSGuiPopupLabel')]")
    private WebElement           ctxModifyAttribute;

    @FindBy(xpath = "//td[contains(text(),'Execute')]")
    private WebElement           csGuiPopupExecute;

    @FindBy(xpath = "//td[contains(text(),'Task')]")
    private WebElement           csGuiPopupTask;

    @FindBy(xpath = "//td[contains(text(),'Data Language')]")
    private WebElement           csGuiPopupDataLanguage;

    @FindBy(xpath = "//td[@title='Usages']/..")
    private WebElement           ctxUsages;

    @FindBy(xpath = "//td[@title='Product']/..")
    private WebElement           ctxMoreOptionProduct;

    @FindBy(xpath = "//td[@title='File']/..")
    private WebElement           ctxMoreOptionFile;

    @FindBy(xpath = "//td[@title='User']/..")
    private WebElement           ctxMoreOptionUser;

    @FindBy(xpath = "//td[contains(text(),'View')]")
    private WebElement           ctxView;

    @FindBy(xpath = "//td[@title='Display']")
    private WebElement           ctxMoreOptionDisplay;

    @FindBy(xpath = "//td[@title='List View']")
    private WebElement           ctxMoreOptionListView;

    @FindBy(id = "CSPopupDiv_SUB_SUB")
    private WebElement           csPopDivSubSub;

    @FindBy(xpath = "//td[@title='Medium Preview Images']")
    private WebElement           ctxMoreOptionMediumPreviewImages;

    /**
     * This method returns the instance of modify attribute pop up
     * 
     * @return ctxModifyAttribute
     */
    public WebElement getCtxModifyAttribute() {
        return ctxModifyAttribute;
    }

    public CSPopupDivPim(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
    }

    /**
     * This method clicks on webelement
     * 
     * @param waitForReload waits for an element to reload
     * @param element contains element to be clicked
     * 
     */
    public void clkElement(WebDriverWait waitForReload, WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.click();
        CSLogger.info("Clicked on element");
    }

    /**
     * This method returns the instance of List Projects
     * 
     * @return csGuiPopupListProjects
     */
    public WebElement getcsGuiPopupListProjects() {
        return csGuiPopupListProjects;
    }

    /**
     * This method returns the instance of Select Action
     * 
     * @return csGuiPopupSelectAction
     */
    public WebElement getcsGuiPopupSelectAction() {
        return csGuiPopupSelectAction;
    }

    /**
     * This method returns the instance of workflow done
     * 
     * @return csGuiPopupWorkflowDone
     */
    public WebElement getcsGuiPopupWorkflowDone() {
        return csGuiPopupWorkflowDone;
    }

    /**
     * This method returns the instance of Tasks
     * 
     * @return csGuiPopupSubTasks
     */
    public WebElement getcsGuiPopupSubTasks() {
        return csGuiPopupSubTasks;
    }

    /**
     * This method returns the instance of Admin
     * 
     * @return csGuiPopupAdmin
     */
    public WebElement getcsGuiPopupAdmin() {
        return csGuiPopupAdmin;
    }

    /**
     * This method returns the instance of Lock and Unlock portal
     * 
     * @return csGuiPopupLockandUnlockPortal
     */
    public WebElement getCsGuiPopupLockAndUnlockPortal() {
        return csGuiPopupLockAndUnlockPortal;
    }

    /**
     * This method returns the instance of Help
     * 
     * @return csGuiPopupHelp
     */
    public WebElement getcsGuiPopupHelp() {
        return csGuiPopupHelp;
    }

    /**
     * This method returns the instance of portal Option
     * 
     * @return csGuiPopupPortalSettings
     */
    public WebElement getCsGuiPopupPortalSettings() {
        return csGuiPopupPortalSettings;
    }

    /**
     * This method returns the instance if Logout option
     * 
     * @return csGuiPopupLogout
     */
    public WebElement getCsGuiPopupLogout() {
        return csGuiPopupLogout;
    }

    /**
     * This method returns the instance of Access Right option
     * 
     * @return csGuiPopupAccessRight
     */
    public WebElement getCsGuiPopupAccessRight() {
        return csGuiPopupAccessRight;
    }

    /**
     * This method returns the instance of Tools option
     * 
     * @return csGuiPopupTools
     */
    public WebElement getCsGuiPopupTools() {
        return csGuiPopupTools;
    }

    /**
     * This method returns the instance of create new file
     * 
     * @return createNewFile
     */
    public WebElement getCreateNewFile() {
        return createNewFile;
    }

    /**
     * This method returns the instance of Ok button
     * 
     * @return btnOkMamStudio
     */
    public WebElement getBtnOkMamStudio() {
        return btnOkMamStudio;
    }

    /**
     * This method returns the instance of cancel button
     * 
     * @return btnCancelMamStudio
     */
    public WebElement getBtnCancelMamStudio() {
        return btnCancelMamStudio;
    }

    public WebElement getUserInputMamStudio() {
        return userInputMamStudio;
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
     * Returns the webElement csPortalWindow
     */
    public WebElement getCSPortalWindow() {
        return csPortalWindow;
    }

    /**
     * Returns the webElement csGuiPopup
     */
    public WebElement getCSGuiPopup() {
        return csGuiPopup;
    }

    /**
     * Returns the webElement popUpCSGuiWindow
     */
    public WebElement getCSGuiPopupWindow() {
        return popUpCSGuiWindow;
    }

    /**
     * Returns the webElement ccsGuiPopupMenuCreateNew
     */
    public WebElement getCsGuiPopupMenuCreateNew() {
        return csGuiPopupMenuCreateNew;
    }

    /**
     * Returns the webElement csGuiPopupMenuNewFolder
     */
    public WebElement getCsGuiPopupMenuNewFolder() {
        return csGuiPopupMenuNewFolder;
    }

    /**
     * Returns the webElement txtCsGuiModalDialogFormName
     * 
     * @return txtCsGuiModalDialogFormName
     */
    public WebElement getTxtCsGuiModalDialogFormName() {
        return txtCsGuiModalDialogFormName;
    }

    /**
     * Returns the webElement csPopupDiv
     * 
     * @return csPopupDiv
     */
    public WebElement getCsPopupDiv() {
        return csPopupDiv;
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
     * Returns the webElement btnCsGuiModalDialogCancelButton
     * 
     * @return btnCsGuiModalDialogCancelButton
     */
    public WebElement getBtnCsGuiModalDialogCancelButton() {
        return btnCsGuiModalDialogCancelButton;
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
     * Returns the webElement csGuiPopupMenuSubMenuEdit
     * 
     * @return csGuiPopupMenuSubMenuEdit
     */
    public WebElement getCsGuiPopupSubMenuEdit() {
        return csGuiPopupMenuSubMenuEdit;
    }

    /**
     * Returns the webElement csGuiPopupMenuObject
     */
    public WebElement getCsGuiPopupMenuObject() {
        return csGuiPopupMenuObject;
    }

    /**
     * Returns the webElement csGuiPopupMenuSubMenuDuplicate
     */
    public WebElement getCsGuiPopupMenuSubMenuDuplicate() {
        return csGuiPopupMenuSubMenuDuplicate;
    }

    /**
     * Returns the webElement btnCsGuiModalDialogNonRecursive
     * 
     * @return btnCsGuiModalDialogNonRecursive
     */
    public WebElement getBtnCsGuiModalDialogNonRecursive() {
        return btnCsGuiModalDialogNonRecursive;
    }

    /**
     * Returns the webElement btnCsGuiModalDialogRecursive
     * 
     * @return btnCsGuiModalDialogRecursive
     */
    public WebElement getBtnCsGuiModalDialogRecursive() {
        return btnCsGuiModalDialogRecursive;
    }

    /**
     * Getter for CsGuiPopupMenuRollbackVersion
     * 
     * @return CsGuiPopupMenuRollbackVersion
     */
    public WebElement getCsGuiPopupMenuRollbackVersion() {
        return CsGuiPopupMenuRollbackVersion;
    }

    /**
     * Setter for csGuiPopupMenuRollbackVersion
     * 
     * @param csGuiPopupMenuRollbackVersion
     */
    public void setCsGuiPopupMenuRollbackVersion(
            WebElement csGuiPopupMenuRollbackVersion) {
        CsGuiPopupMenuRollbackVersion = csGuiPopupMenuRollbackVersion;
    }

    /**
     * Returns the webElement lblCsGuiModalDialogTitle
     * 
     * @return lblCsGuiModalDialogTitle
     */
    public WebElement getLblCsGuiModalDialogTitle() {
        return lblCsGuiModalDialogTitle;
    }

    /**
     * Returns the webElement ddCsGuiDialogSelectedVerion
     * 
     * @return ddCsGuiDialogSelectedVerion
     */
    public WebElement getDdCsGuiDialogSelectedVerion() {
        return ddCsGuiDialogSelectedVerion;
    }

    /**
     * Returns the webElement ddCsGuiDialogCreateRecursively
     * 
     * @return ddCsGuiDialogCreateRecursively
     */
    public WebElement getDdCsGuiDialogCreateRecursively() {
        return ddCsGuiDialogCreateRecursively;
    }

    /**
     * Returns the webElement cbCsGuiDialogEnhanceItemProduct
     * 
     * @return cbCsGuiDialogEnhanceItemProduct
     */
    public WebElement getCbCsGuiDialogEnhanceItemProduct() {
        return cbCsGuiDialogEnhanceItemProduct;
    }

    /**
     * Returns the webElement lblCsGuiDialogLabel
     * 
     * @return lblCsGuiDialogLabel
     */
    public WebElement getLblCsGuiDialogLabel() {
        return lblCsGuiDialogLabel;
    }

    /**
     * Returns the webElement ddCsGuiDialogSelectionType
     * 
     * @return @return lblCsGuiDialogLabel
     */
    public WebElement getDdCsGuiDialogSelectionType() {
        return ddCsGuiDialogSelectionType;
    }

    /**
     * Returns the webElement csGuiPopupFavorites
     * 
     * @return @return csGuiPopupFavorites
     */
    public WebElement getcsGuiPopupFavorites() {
        return csGuiPopupFavorites;
    }

    /**
     * Returns the webElement csGuiPopupAddFavorites
     * 
     * @return @return csGuiPopupAddFavorites
     */
    public WebElement getcsGuiPopupSubAddFavorites() {
        return csGuiPopupSubAddFavorites;
    }

    /**
     * Returns the webElement csGuiPopupMenuRefresh
     * 
     * @return @return csGuiPopupMenuRefresh
     */
    public WebElement getcsGuiPopupMenuRefresh() {
        return csGuiPopupMenuRefresh;
    }

    /**
     * Returns the webElement csGuiPopupPublish
     * 
     * @return @return csGuiPopupPublish
     */
    public WebElement getcsGuiPopupPublish() {
        return csGuiPopupPublish;
    }

    /**
     * Returns the webElement csGuiPopupCancelPublish
     * 
     * @return @return csGuiPopupCancelPublish
     */
    public WebElement getcsGuiPopupCancelPublish() {
        return csGuiPopupCancelPublish;
    }

    /**
     * Returns the webElement csGuiPopupMenuSubMenuRename
     */
    public WebElement getCsGuiPopupMenuSubMenuRename() {
        return csGuiPopupMenuSubMenuRename;
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
     * this method selects pop up after right clicking
     * 
     * @param waitForReload waits for element to reload
     * @param subMenuToBeSelected contains name of submenu to be selected from
     *            the pop up
     * @param browserDriverInstance contains the instance of browser driver
     */
    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance) {
        traverseToCsPopupDivFrame(waitForReload, browserDriverInstance);
        waitForReload
                .until(ExpectedConditions.visibilityOf(subMenuToBeSelected));
        subMenuToBeSelected.click();
        CSLogger.info("Menu Selected");
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
        getTxtCsGuiModalDialogFormName().click();
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
     * This method returns the CSPopUpDivLocator
     * 
     * @param browserDriverInstance
     * @return locators
     */
    public static CSPopupDivPim
            getCSPopupDivLocators(WebDriver browserDriverInstance) {
        if (locators == null) {
            locators = new CSPopupDivPim(browserDriverInstance);
        }
        return locators;
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
        traverseToCSPopupDivSubFrame(waitForReload, browserDriverInstance);
        waitForReload
                .until(ExpectedConditions.visibilityOf(subMenuToBeSelected));
        subMenuToBeSelected.click();
        CSLogger.info("Submenu Selected");
    }

    public void traverseToCSPopupDivSubFrame(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(
                ExpectedConditions.elementToBeClickable(getCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
        CSLogger.info("Switched to cs pop div sub frame");
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
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Hover on element successful");

    }

    /**
     * Returns the webElement csGuiPopupMenuNewFolder it will return the ADD
     * option of valuelist
     * 
     * @return csGuiPopupMenuAddValuelist ADD menu list
     */
    public WebElement getCsGuiPopupMenuAddValuelist() {
        return csGuiPopupMenuAddValuelist;
    }

    /**
     * Getter method for csGuiPopupMenuNewChild
     * 
     * @return csGuiPopupMenuNewChild
     */
    public WebElement getCsGuiPopupMenuNewChild() {
        return csGuiPopupMenuNewChild;
    }

    /**
     * Setter method for csGuiPopupMenuNewChild
     * 
     * @param csGuiPopupMenuNewChild
     */
    public void setCsGuiPopupMenuNewChild(WebElement csGuiPopupMenuNewChild) {
        this.csGuiPopupMenuNewChild = csGuiPopupMenuNewChild;
    }

    /**
     * Getter method for csGuiPopupMenuDeleteVersion
     * 
     * @return csGuiPopupMenuDeleteVersion
     */
    public WebElement getCsGuiPopupMenuDeleteVersion() {
        return csGuiPopupMenuDeleteVersion;
    }

    /**
     * Setter method for csGuiPopupMenuDeleteVersion
     * 
     * @param csGuiPopupMenuDeleteVersion
     */
    public void setCsGuiPopupMenuDeleteVersion(
            WebElement csGuiPopupMenuDeleteVersion) {
        this.csGuiPopupMenuDeleteVersion = csGuiPopupMenuDeleteVersion;
    }

    /**
     * Getter method for csGuiPopupMenuRestoreVersion
     * 
     * @return csGuiPopupMenuRestoreVersion
     */
    public WebElement getCsGuiPopupMenuRestoreVersion() {
        return CsGuiPopupMenuRestoreVersion;
    }

    /**
     * Setter method for csGuiPopupMenuRestoreVersion
     * 
     * @param csGuiPopupMenuRestoreVersion
     */
    public void setCsGuiPopupMenuRestoreVersion(
            WebElement csGuiPopupMenuRestoreVersion) {
        CsGuiPopupMenuRestoreVersion = csGuiPopupMenuRestoreVersion;
    }

    /**
     * Getter method for csGuiPopupMenuRemove
     * 
     * @return csGuiPopupMenuRemove
     */
    public WebElement getCsGuiPopupMenuRemove() {
        return CsGuiPopupMenuRemove;
    }

    /**
     * Setter method for csGuiPopupMenuRemove
     * 
     * @param csGuiPopupMenuRemove
     */
    public void setCsGuiPopupMenuRemove(WebElement csGuiPopupMenuRemove) {
        CsGuiPopupMenuRemove = csGuiPopupMenuRemove;
    }

    /**
     * This method handles pop up containing three options i.e
     * Cancel,Recursive,Non-Recursive which appears while creating duplicate
     * products
     * 
     * @param waitForReload waits for element to reload
     * 
     * @param userInput String contains options i.e
     *            Cancel,Recursive,Non-Recursive
     * 
     * @param browserDriverInstance contains the instance of browser
     */
    public void askBoxWindowOperationDuplicateProducts(
            WebDriverWait waitForReload, String userInput,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Switched To CsPortalWindowContent Frame");
        if (userInput.equals("Cancel")) {
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    getBtnCsGuiModalDialogCancelButton()));
            getBtnCsGuiModalDialogCancelButton().click();
            CSLogger.info("Clicked on Cancel Of Popup");
        } else if (userInput.equals("Recursive")) {
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(getBtnCsGuiModalDialogRecursive()));
            getBtnCsGuiModalDialogRecursive().click();
            CSLogger.info("Clicked on Recursive Of Popup");
        } else {
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    getBtnCsGuiModalDialogNonRecursive()));
            getBtnCsGuiModalDialogNonRecursive().click();
            CSLogger.info("Clicked on Non Recursive of Popup");
        }
    }

    /**
     * Getter method for csGuiPopupMenuSubMenuViewFolderAsEditor
     * 
     * @return csGuiPopupMenuSubMenuViewFolderAsEditor
     */
    public WebElement getCsGuiPopupMenuSubMenuViewFolderAsEditor() {
        return csGuiPopupMenuSubMenuViewFolderAsEditor;
    }

    /**
     * Getter method for delete option of mass edit
     * 
     * @return csGuiPopupMenuDeleteValuelist
     */
    public WebElement getDeleteMenu() {
        return csGuiPopupMenuDeleteValuelist;
    }

    /**
     * Getter method for duplicate option of 'marked' dropdown
     * 
     * @return csGuiPopupMenuDuplicateValuelist
     */
    public WebElement getDuplicateMenu() {
        return csGuiPopupMenuDuplicateValuelist;
    }

    /**
     * Selects the language passed as parameter
     * 
     * @param waitForReload WebDriverWait object
     * @param language String containing name of language
     * @param browserDriverInstance WebDriver instance
     */
    public void selectDataLanguage(WebDriverWait waitForReload, String language,
            WebDriver browserDriverInstance) {
        traverseToCsPopupDivFrame(waitForReload, browserDriverInstance);
        WebElement dataLanguage = browserDriverInstance.findElement(
                By.xpath("//td[@title='" + language + "']/parent::tr"));
        dataLanguage.click();
        CSLogger.info("Clicked on data language " + language);
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
        waitForReload.until(
                ExpectedConditions.elementToBeClickable(getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivFrame()));
        CSLogger.info("Traversed to CsPopupDivFrame ");
    }

    /**
     * This method gets the instance of textbox where user sends value in dialog
     * in pim studio
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
     * This method will return the delete WebElement sun menu
     * 
     * @return WebElement of delete from sub menu of Object
     * 
     * @see org.cs.csautomation.cs.settings.ISettingsPopup#getObjectDeleteSubMenu()
     */
    public WebElement getObjectDeleteSubMenu() {
        return csObjectDeleteSubMenu;
    }

    /**
     * This method returns the popup workflow WebElement when clicked on show
     * more options toolbar button.
     * 
     * @return WebElement csGuiPopupMenuWorkflow.
     */
    public WebElement getCsGuiPopupMenuWorkflow() {
        return csGuiPopupMenuWorkflow;
    }

    /**
     * Getter for csGuiPopupProcess
     * 
     * @return the csGuiPopupProcess
     */
    public WebElement getCsGuiPopupProcess() {
        return csGuiPopupProcess;
    }

    /**
     * Setter for csGuiPopupProcess
     * 
     * @param csGuiPopupProcess the csGuiPopupProcess to set
     */
    public void setCsGuiPopupProcess(WebElement csGuiPopupProcess) {
        this.csGuiPopupProcess = csGuiPopupProcess;
    }

    /**
     * Getter for csGuiPopupSmartImport
     * 
     * @return the csGuiPopupSmartImport
     */
    public WebElement getCsGuiPopupSmartImport() {
        return csGuiPopupSmartImport;
    }

    /**
     * Setter for csGuiPopupSmartImport
     * 
     * @param csGuiPopupSmartImport the csGuiPopupSmartImport to set
     */
    public void setCsGuiPopupSmartImport(WebElement csGuiPopupSmartImport) {
        this.csGuiPopupSmartImport = csGuiPopupSmartImport;
    }

    /**
     * Getter for csGuiPopupCheckIn
     * 
     * @return the csGuiPopupCheckIn
     */
    public WebElement getCsGuiPopupCheckIn() {
        return csGuiPopupCheckIn;
    }

    /**
     * Setter for csGuiPopupCheckIn
     * 
     * @param csGuiPopupCheckIn the csGuiPopupCheckIn to set
     */
    public void setCsGuiPopupCheckIn(WebElement csGuiPopupCheckIn) {
        this.csGuiPopupCheckIn = csGuiPopupCheckIn;
    }

    /**
     * Getter for csGuiPopupCheckOut
     * 
     * @return the csGuiPopupCheckOut
     */
    public WebElement getCsGuiPopupCheckOut() {
        return csGuiPopupCheckOut;
    }

    /**
     * Returns the execute pop element.
     * 
     * @return WebElement csGuiPopupExecute.
     */
    public WebElement getCsGuiPopupExecute() {
        return csGuiPopupExecute;
    }

    /**
     * Returns the Task pop element.
     * 
     * @return WebElement csGuiPopupTask.
     */
    public WebElement getCsGuiPopupTask() {
        return csGuiPopupTask;
    }

    /**
     * This method Hovers over sub menu
     * 
     * @param waitForReload waits for element to reload
     * @param hoverOnWebElement webElement which will be hovered
     * @param browserDriverInstance contains the instance of browser T
     */
    public void hoverOnCsGuiPopupSubMenu(WebDriver browserDriverInstance,
            WebDriverWait waitForReload, WebElement hoverOnWebElement) {
        traverseToCsPopupDivSubFrame(waitForReload, browserDriverInstance);
        Actions action = new Actions(browserDriverInstance);
        action.moveToElement(hoverOnWebElement).build().perform();
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Hover on element successful");
    }

    /**
     * Traverses till FrmCsPopupDivSubFrame frame.
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance browserDriver instance.
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
     * Getter for csGuiPopupDataLanguage
     * 
     * @return csGuiPopupDataLanguage
     */
    public WebElement getCsGuiPopupDataLanguage() {
        return csGuiPopupDataLanguage;
    }

    /**
     * Returns the element for active script option from edit pane
     * 
     * @return - create new article icon
     */
    public WebElement getActiveScriptPopup() {
        return activeScriptPopup;
    }

    /**
     * This Method returns webelement for showMoreOption
     */
    public WebElement getCtxMoreOptionsProducts() {
        return ctxMoreOptionsProducts;
    }

    /**
     * Returns the instance of import option for importing csv or excel file
     * 
     * @return ctxImportCsv
     */
    public WebElement getCtxImportCsv() {
        return ctxImportCsv;
    }

    /**
     * Returns the pop up element Usages.
     * 
     * @return WebElement ctxUsages.
     */
    public WebElement getCtxUsages() {
        return ctxUsages;
    }

    /**
     * Returns the pop up element Product.
     * 
     * @return ctxMoreOptionProduct
     */
    public WebElement getCtxMoreOptionProduct() {
        return ctxMoreOptionProduct;
    }

    /**
     * Returns the pop up element File.
     * 
     * @return ctxMoreOptionFile
     */
    public WebElement getCtxMoreOptionFile() {
        return ctxMoreOptionFile;
    }

    /**
     * Returns the pop up element User.
     * 
     * @return ctxMoreOptionUser
     */
    public WebElement getCtxMoreOptionUser() {
        return ctxMoreOptionUser;
    }

    /**
     * Returns the view option
     * 
     * @return WebElement ctxView
     */
    public WebElement getCtxView() {
        return ctxView;
    }

    /**
     * Returns the pop up element Display.
     * 
     * @return WebElement ctxMoreOptionDisplay.
     */
    public WebElement getCtxMoreOptionDisplay() {
        return ctxMoreOptionDisplay;
    }

    /**
     * Returns the pop up element List View
     * 
     * @return WebElement ctxMoreOptionListView.
     */
    public WebElement getCtxMoreOptionListView() {
        return ctxMoreOptionListView;
    }

    /**
     * Returns the element csPopDivSubSub
     * 
     * @return WebElement csPopDivSubSub
     */
    public WebElement getCsPopDivSubSub() {
        return csPopDivSubSub;
    }

    /**
     * Traverses till FrmCsPopupDivSubFrame frame.
     * 
     * @param waitForReload WebDriverWait object
     * @param browserDriverInstance browseDriver instance.
     */
    public void traverseToCsPopupDivNestedSubFrame(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getCsPopDivSubSub());
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCSPopupDivSubSubFrame()));
    }

    /**
     * This method selects nested sub menu from the pop up
     * 
     * @param waitForReload waits for element to reload
     * @param nestedSubMenuToBeSelected contains xpath of sub sub menu to be
     *            selected
     * @param browserDriverInstance contains the instance of browser
     */
    public void selectPopupDivNestedSubMenu(WebDriverWait waitForReload,
            WebElement nestedSubMenuToBeSelected,
            WebDriver browserDriverInstance) {
        traverseToCsPopupDivNestedSubFrame(waitForReload,
                browserDriverInstance);
        waitForReload.until(
                ExpectedConditions.visibilityOf(nestedSubMenuToBeSelected));
        nestedSubMenuToBeSelected.click();
        CSLogger.info("Nested submenu Selected");
    }

    /**
     * Returns the more option pop up element 'Medium Preview Images'.
     * 
     * @return WebElement ctxMoreOptionMediumPreviewImages.
     */
    public WebElement getCtxMoreOptionMediumPreviewImages() {
        return ctxMoreOptionMediumPreviewImages;
    }
}
