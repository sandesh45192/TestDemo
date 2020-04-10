
package org.cs.csautomation.cs.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IProductPopup {

    public void askBoxWindowOperation(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance);

    public WebElement getBtnCsGuiModalDialogOkButton();

    public WebElement getBtnCsGuiModalDialogCancelButton();

    public WebElement getCsGuiPopupMenuObject();

    public void hoverOnCsGuiPopupMenu(WebDriver browserDriverInstance,
            WebDriverWait waitForReload, WebElement hoverOnWebElement);

    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public WebElement getCsGuiPopupSubMenuEdit();

    public WebElement getCsGuiPopupMenuCreateNew();

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void enterValueInDialogue(WebDriverWait waitForReload,
            String userInputFolderName);

    public WebElement getCsGuiPopupMenuNewChild();

    public WebElement getCsGuiPopupMenuSubMenuDuplicate();

    public void askBoxWindowOperationDuplicateProducts(
            WebDriverWait waitForReload, String userInput,
            WebDriver browserDriverInstance);

    public WebElement getCsGuiPopupMenuSubMenuViewFolderAsEditor();

    public WebElement getLblCsGuiModalDialogTitle();

    public WebElement getDdCsGuiDialogSelectedVerion();

    public WebElement getDdCsGuiDialogCreateRecursively();

    public WebElement getCbCsGuiDialogEnhanceItemProduct();

    public WebElement getLblCsGuiDialogLabel();

    public WebElement getDdCsGuiDialogSelectionType();

    public WebElement getCsGuiPopupMenuSubMenuRename();

    public WebElement getCsGuiPopupTools();

    public WebElement getCsGuiPopupAccessRight();

    public WebElement getCsGuiPopupLogout();

    public WebElement getCsGuiPopupPortalSettings();

    public WebElement getcsGuiPopupHelp();

    public WebElement getCsGuiPopupLockAndUnlockPortal();

    public WebElement getcsGuiPopupAdmin();

    public void clkElement(WebDriverWait waitForReload, WebElement element);

    public WebElement getCtxModifyAttribute();

    public void traverseToCsPopupDivFrame(WebDriverWait waitForReload,
            WebDriver browserDriverInstance);

    public WebElement getcsGuiPopupMenuRefresh();

    public WebElement getCsGuiPopupCheckOut();

    public WebElement getCsGuiPopupCheckIn();

    public WebElement getDeleteMenu();

    public WebElement getCsGuiPopupProcess();

    public WebElement getCsGuiPopupExecute();
    
    public WebElement getBtnCsGuiModalDialogNonRecursive();
    
    public void hoverOnCsGuiPopupSubMenu(WebDriver browserDriverInstance,
            WebDriverWait waitForReload, WebElement hoverOnWebElement);
}
