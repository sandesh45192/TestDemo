
package org.cs.csautomation.cs.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains popup methods for open search test cases
 * 
 * @author CSAutomation Team
 */
public interface IOpenSearchPopup {

    public WebElement getCtxDeleteOpenSearch();

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement contextToBeSelected, WebDriver browserDriverInstance);

}
