package org.cs.csautomation.cs.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IWorkflowsPopup {

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public WebElement getCsPopupMenuNewWorkflows();
    
    public WebElement getCsPopupRefresh();
    
    public WebElement getCsPopupMenuDeleteWorkflow();
}
