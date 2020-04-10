/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains WebElements and methods of footer.
 * 
 * @author CSAutomation Team
 *
 */
public class CSGuiListFooter {

    @FindBy(id = "massUpdateSelector")
    private WebElement drpDwnMassUpdateSelector;

    @FindBy(xpath = "//td[contains(@class,'_right')]")
    private WebElement lblElementCount;

    public CSGuiListFooter(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Login page POM elements.");
    }

    /**
     * Returns WebElement drpDwnMassUpdateSelector
     * 
     * @return drpDwnMassUpdateSelector
     */
    public WebElement getDrpDwnMassUpdateSelector() {
        return drpDwnMassUpdateSelector;
    }

    /**
     * Clicks on marked drop downs
     * 
     * @param waitForReload
     *            webDriverWait object
     */
    public void clkOnDrpDwnMassUpdateSelector(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getDrpDwnMassUpdateSelector());
        getDrpDwnMassUpdateSelector().click();
        CSLogger.info("Clicked on Marked drop down");
    }

    /**
     * Selects option from drop down
     * 
     * @param waitForReload
     *            webDriverWait object
     * @param option
     *            String object containing option to be selected
     */
    public void selectOptionFromDrpDwnMassUpdateSelector(
            WebDriverWait waitForReload, String option) {
        clkOnDrpDwnMassUpdateSelector(waitForReload);
        Select dropdown = new Select(getDrpDwnMassUpdateSelector());
        dropdown.selectByVisibleText(option);
        CSLogger.info(option + " option is selected");
    }

    /**
     * Returns the right footer element that shows the count of total elements.
     * 
     * @return WebElement lblElementCount.
     */
    public WebElement getLblElementCount() {
        return lblElementCount;
    }
}
