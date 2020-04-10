package org.cs.csautomation.cs.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IChannelPopup {

    public WebElement getCsGuiPopupMenuCreateNew();

    public WebElement getCsGuiPopupMenuObject();

    public WebElement getDeleteMenu();

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void askBoxWindowOperation(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance);
    
    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void enterValueInDialogue(WebDriverWait waitForReload,
            String userInputFolderName);
    
    public WebElement getcsGuiPopupMenuRefresh();
}
