package org.cs.csautomation.cs.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IWorkflowActionsPopup {

    public WebElement getCsPopupRefresh();
    
    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public WebElement getCsPopupMenuDeleteAction();

}
