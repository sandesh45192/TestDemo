/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface ITranslationManagerPopup {

    public WebElement getCtxRefresh();

    public WebElement getCsPopupObject();

    public WebElement getCtxDuplicate();

    public WebElement getCtxDelete();

    public WebElement getCsPopupDivSub();

    public WebElement getCtxDataLanguage();

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void selectPopupDivSubMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public WebElement getCtxTranslationJob();
}
