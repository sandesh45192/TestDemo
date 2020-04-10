package org.cs.csautomation.cs.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IFavoritesPopup {

    public WebElement getcsGuiPopupPublish();
    
    public WebElement getcsGuiPopupCancelPublish();
    
    public WebElement getcsGuiPopupMenuRefresh();
    
    public WebElement getDeleteMenu();
    
    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);
    
    public void hoverOnCsGuiPopupMenu(WebDriver browserDriverInstance,
            WebDriverWait waitForReload, WebElement hoverOnWebElement);
    
    public WebElement getCsGuiPopupMenuObject();
    
    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

}
