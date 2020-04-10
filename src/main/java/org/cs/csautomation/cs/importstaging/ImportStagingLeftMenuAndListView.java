/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.importstaging;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class contains the WebElements of the import database left menu and list view
 * @author CSAutomation Team
 *
 */
public class ImportStagingLeftMenuAndListView {

    @FindBy(xpath = "//a/img[contains(@src,'new.gif')]")
    private WebElement btnCreateNew;

    /*
     * constructor method of the class
     * @param paramDriver
     */
    public ImportStagingLeftMenuAndListView(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized PimStudioSettingsNode page POM elements.");

    }

    /*
     * return the webElement of the create new option of the list view of middle
     * pane.
     * @return WebElement btnCreateNew create new element.
     */
    public WebElement getCreateNewButton() {
        return btnCreateNew;
    }

    /*
     * Clicks on the create new button of the Import job. Its in middle pane
     * @param waitForReload 
     */
    public void clkCreateNewImportJob(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getCreateNewButton());
        getCreateNewButton().click();
    }

}
