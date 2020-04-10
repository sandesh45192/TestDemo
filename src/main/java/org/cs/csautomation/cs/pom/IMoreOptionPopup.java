
package org.cs.csautomation.cs.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface IMoreOptionPopup {

    public WebElement getcsGuiPopupFavorites();

    public WebElement getcsGuiPopupSubAddFavorites();

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public WebElement getCsGuiPopupMenuWorkflow();

    public WebElement getcsGuiPopupSubTasks();

    public WebElement getCtxMoreOptionsProducts();

    public WebElement getCtxImportCsv();

    public void hoverOnCsGuiPopupMenu(WebDriver browserDriverInstance,
            WebDriverWait waitForReload, WebElement hoverOnWebElement);

    public WebElement getCtxUsages();

    public WebElement getCtxMoreOptionFile();

    public WebElement getCtxMoreOptionUser();

    public WebElement getCtxMoreOptionProduct();

    public WebElement getCtxView();

    public WebElement getCtxMoreOptionDisplay();

    public WebElement getCtxMoreOptionListView();

    public void selectPopupDivNestedSubMenu(WebDriverWait waitForReload,
            WebElement nestedSubMenuToBeSelected,
            WebDriver browserDriverInstance);

    public WebElement getCtxMoreOptionMediumPreviewImages();
}
