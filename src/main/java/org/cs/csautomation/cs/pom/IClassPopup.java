
package org.cs.csautomation.cs.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IClassPopup {

    public WebElement getCsGuiPopupMenuCreateNew();

    public WebElement getBtnCsGuiModalDialogOkButton();

    public WebElement getBtnCsGuiModalDialogCancelButton();

    public WebElement getCSGuiPopup();

    public WebElement getCSGuiPopupWindow();

    public WebElement getCSPortalWindow();

    public WebElement getTxtCsGuiModalDialogFormName();

    public void askBoxWindowOperation(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance);

    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public WebElement getCsGuiPopupSubMenuEdit();

    public WebElement getCsGuiPopupMenuObject();

    public void hoverOnCsGuiPopupMenu(WebDriver browserDriverInstance,
            WebDriverWait waitForReload, WebElement hoverOnWebElement);

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void enterValueInDialogue(WebDriverWait waitForReload,
            String userInputFolderName);

    public WebElement getDeleteMenu();

}
