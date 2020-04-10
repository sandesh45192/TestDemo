
package org.cs.csautomation.cs.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains settings popup methods.
 * 
 * @author CSAutomation Team
 */

public interface ISettingsPopup {

	public void selectPopupDivMenu(WebDriverWait waitForReload, WebElement subMenuToBeSelected,
			WebDriver browserDriverInstance);

	public WebElement getDeleteMenu();

	public WebElement getCsGuiPopupMenuObject();

	public void selectPopupDivSubMenu(WebDriverWait waitForReload, WebElement subMenuToBeSelected,
			WebDriver browserDriverInstance);

	public WebElement getObjectDeleteSubMenu();

	public WebElement getDuplicateMenu();

	public WebElement getCsGuiPopupMenuCreateNew();

	public WebElement getcsGuiPopupWorkflowDone();

	public WebElement getcsGuiPopupListProjects();

	public WebElement getCsGuiPopupSubMenuEdit();

	public WebElement getCsGuiPopupTask();

	public WebElement getActiveScriptPopup();

}
