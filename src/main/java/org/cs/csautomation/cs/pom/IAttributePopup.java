package org.cs.csautomation.cs.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IAttributePopup {
    public WebElement getCsGuiPopupMenuNewFolder();

    public WebElement getCsGuiPopupMenuCreateNew();
    
    public WebElement getCsGuiPopupMenuAddValuelist();
    
    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void enterValueInDialogue(WebDriverWait waitForReload,
            String userInputFolderName);

    public void askBoxWindowOperation(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance);

    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public WebElement getCsGuiPopupMenuObject();

    public WebElement getDeleteMenu();

    public WebElement getDuplicateMenu();
    
    public void enterValueInUserInputDialogue(WebDriverWait waitForReload,
            String userInputFolderName);
}