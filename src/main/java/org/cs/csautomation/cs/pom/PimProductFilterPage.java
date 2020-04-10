/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains the test methods to handle the test cases related to the
 * login use cases.
 * 
 * @author CSAutomation Team
 *
 */
public class PimProductFilterPage {

    @FindBy(xpath = "//a[contains(@title, 'hide filter bar')]")
    private WebElement btnFilter;

    @FindBy(xpath = "//a[contains(@title, 'Remove filter')]")
    private WebElement btnRemoveFilter;

    @FindBy(id = "Filter_Itemlocale_Label")
    private WebElement txtLabelFilter;

    @FindBy(id = "FilterBarTop")
    private WebElement topFilterBar;

    @FindBy(id = "toolbarSearchInput")
    private WebElement txtSearch;

    @FindBy(xpath = "//a[@class='advancedSearchButton closeAdvancedSearch']")
    private WebElement btnCloseAdvanceSearch;

    @FindBy(xpath = "//a[@class='advancedSearchButton openAdvancedSearch']")
    private WebElement btnOpenAdvanceSearch;

    @FindBy(xpath = "//select[@name='attribute']")
    private WebElement sddAttribute;

    @FindBy(xpath = "//select[@name='operator']")
    private WebElement sddOperator;

    @FindBy(xpath = "//a[contains(@id,'_sortbar_add')]")
    private WebElement btnPlusOnShortBy;

    @FindBy(xpath = "(//select[contains(@name,'attribute')])[2]")
    private WebElement sddShortByAttributes;

    @FindBy(xpath = "//select[@name='value']")
    private WebElement sddValuesAseDec;

    @FindBy(xpath = "//a[contains(@id, '_search')]")
    private WebElement btnSearch;

    @FindBy(xpath = "//input[@name='value']")
    private WebElement txtValue;

    @FindBy(xpath = "//div[contains(@class,'subSearchWrapper')]//select[@name='attribute']")
    private WebElement drpdwnSubSearchAttribute;

    /**
     * Parameterized constructor. This method sets the browser driver instance
     * for this page.
     * 
     * @param paramDriver
     */
    public PimProductFilterPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized pimProductTabs page POM elements.");
    }

    /**
     * Returns the element of drop down Sub Search Attribute
     * 
     * @return drpdwnSubSearchAttribute.
     */
    public WebElement getDrpDwnSubSearchAttribute() {
        return drpdwnSubSearchAttribute;
    }

    /**
     * Returns the element shows the Top Filter Bar
     * 
     * @return topFilterBar WebElement of Filter.
     */
    public WebElement getTopFilterBar() {
        return topFilterBar;
    }

    /**
     * Returns the element shows the Filter Button
     * 
     * @return btnFilter WebElement of Filter button
     */
    public WebElement getBtnFilterButton() {
        return btnFilter;
    }

    /**
     * Returns the element shows the Remove Filter Button
     * 
     * @return btnFilter WebElement of Remove Filter button
     */
    public WebElement getBtnRemoveFilterButton() {
        return btnRemoveFilter;
    }

    /**
     * Returns the element shows search for label
     * 
     * @return txtLabelFilter WebElement Filter label search
     */
    public WebElement getTxtLabelFilter() {
        return txtLabelFilter;
    }

    /**
     * Returns the element shows simple search option.
     * 
     * @return txtSearch WebElement for simple search.
     */
    public WebElement getTxtSearch() {
        return txtSearch;
    }

    /**
     * Returns the element shows the Advance Search Button to open.
     * 
     * @return btnFilter WebElement of Filter button
     */
    public WebElement getBtnCloseAdvanceSearch() {
        return btnCloseAdvanceSearch;
    }

    /**
     * Returns the element shows the Advance Search Button to close.
     * 
     * @return btnFilter WebElement of Filter button
     */
    public WebElement getBtnOpenAdvanceSearch() {
        return btnOpenAdvanceSearch;
    }

    /**
     * Returns the element shows drop down of attributes.
     * 
     * @return sddAttribute WebElement of drop down.
     */
    public WebElement getSddAttribute() {
        return sddAttribute;
    }

    /**
     * Returns the element shows the drop down of operators
     * 
     * @return sddOperator WebElement of drop down.
     */
    public WebElement getSddOperator() {
        return sddOperator;
    }

    /**
     * Returns the element shows plus button for short by.
     * 
     * @return btnPlusOnShortBy WebElement of plus button for short by.
     */
    public WebElement getBtnPlusOnShortBy() {
        return btnPlusOnShortBy;
    }

    /**
     * Returns the element shows the drop down Attribute for short by.
     * 
     * @return sddShortByAttributes WebElement of drop down for short by.
     */
    public WebElement getSddShortByAttributes() {
        return sddShortByAttributes;
    }

    /**
     * Returns the element shows the button search for advance search.
     * 
     * @return btnSearch WebElement of search button.
     */
    public WebElement getBtnSearch() {
        return btnSearch;
    }

    /**
     * Returns the element shows the drop down of ascending and descending for
     * short by.
     * 
     * @return sddValuesAseDec WebElement of drop down for short by.
     */
    public WebElement getSddValuesAseDec() {
        return sddValuesAseDec;
    }

    /**
     * Returns the element shows text value to be search.
     * 
     * @return txtvalue WebElement of text value.
     */
    public WebElement getTxtValue() {
        return txtValue;
    }
    
    /**
     * This method gets the label and send text to the label filter.
     *
     * @param label String Label to filter.
     */
    public void enterLabelFilter(String label) {
        CSLogger.info("Searching the with label " + label);
        getTxtLabelFilter().click();
        getTxtLabelFilter().clear();
        getTxtLabelFilter().sendKeys(label);
        getTxtLabelFilter().sendKeys(Keys.ENTER);
    }

    /**
     * This method gets the label and text
     *
     * @param label String Label to filter.
     */
    public void enterTxtToSearch(String label) {
        CSLogger.info("Searching the with label " + label);
        getTxtSearch().click();
        getTxtSearch().clear();
        getTxtSearch().sendKeys(label);
        getTxtSearch().sendKeys(Keys.ENTER);
    }
    
    /**
     * Clicks on Button close Advance Search
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnCloseAdvanceSearch(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnCloseAdvanceSearch());
        getBtnCloseAdvanceSearch().click();
        CSLogger.info("Clicks on button close Advance Search");
    }

    /**
     * Clicks on Button Search
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnSearch(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnSearch());
        getBtnSearch().click();
        CSLogger.info("Clicks on button Search");
    }

    /**
     * Clicks on Button open Advance Search
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnOpenAdvanceSearch(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnOpenAdvanceSearch());
        getBtnOpenAdvanceSearch().click();
        CSLogger.info("Clicks on button open Advance Search");
    }
}
