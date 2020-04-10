
package org.cs.csautomation.cs.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IUserManagementPopup {

    public WebElement getCsPopupCreateNew();

    public WebElement getCsPopupRefresh();

    public WebElement getCsPopupNewAccessLevel();

    public WebElement getCsPopupNewChild();

    public WebElement getCsPopupNewGroup();

    public WebElement getCsPopupObject();

    public WebElement getCsPopupEdit();

    public WebElement getUserInput();

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void handlePopup(WebDriverWait waitForReload, String accessLevelName,
            WebDriver browserDriver);

    public void clkOkOfPopup(WebDriverWait waitForReload);

    public void clkCancelOfPopup(WebDriverWait waitForReload);

    public WebElement getCsPopupNewFolder();

    public WebElement getCsPopupDelete();

    public WebElement getCsPopupRemove();

    public void enterValueInUserInputDialogue(WebDriverWait waitForReload,
            String userInputFolderName);

    public void askBoxWindowOperation(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance);
    
    public void enterValueInDialogue(WebDriverWait waitForReload,
            String userInputFolderName);
    
    public void hoverOnCsGuiPopupMenu(WebDriver browserDriverInstance,
            WebDriverWait waitForReload, WebElement hoverOnWebElement);
    
    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);
    
    public WebElement getBtnOk();
      
}
