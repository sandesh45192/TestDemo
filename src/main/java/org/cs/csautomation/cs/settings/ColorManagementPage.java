/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This class contains the all web Elements of the colors pages
 * 
 * @author CSAutomation Team
 *
 */
public class ColorManagementPage {

    @FindBy(id = "Colors@0")
    private WebElement btnColorSettingTreeNode;

    @FindBy(xpath = "//span[contains(text(),'System Preferences')]")
    private WebElement btnSystemPreferences;

    @FindBy(xpath = "//a/img[contains(@src,'plus')]")
    private WebElement btnCreateNew;

    @FindBy(xpath = "//input[contains(@id,'ColorLabel')]")
    private WebElement txtColorLabel;

    @FindBy(
            xpath = "//tr[@id='AttributeRow_Color']//img[@class='CSGuiEditorChooserIconInside']")
    private WebElement txtColorPicker;

    @FindBy(
            xpath = "//tr[@id='AttributeRow_TextColor']//img[@class='CSGuiEditorChooserIconInside']")
    private WebElement txtColorTextPicker;

    @FindBy(xpath = "//input[@id='btn-ok']")
    private WebElement btnOKcolorPickerPopup;

    @FindBy(xpath = "//span[contains(@id,'ColorIsActive_GUI')]")
    private WebElement btnActive;

    /*
     * Constructor method to initialize the elements
     * 
     * @param WebDriver paramDriver
     */
    public ColorManagementPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Color node in the Settings Tab.");
    }

    /*
     * This method returns the WebElement of the Color Node of the Settings ->
     * System Preferences-> Colors
     * 
     * @return WebElement of the Color tree node.
     */
    public WebElement getColorTreeNode() {
        return btnColorSettingTreeNode;
    }

    /*
     * This method return the system Preferences element of the setting tab
     * 
     * @return WebElement of the System preferences
     */
    public WebElement getSystemPreferencesElement() {
        return btnSystemPreferences;
    }

    /*
     * This method return the create new option of the colors middle pane header
     * option
     * 
     * @return WebElement of the create new button
     */
    public WebElement getCreateNewButtonforColor() {
        return btnCreateNew;
    }

    /*
     * This method return the web element of the color Label as Designation text
     * box
     * 
     * @return txtColorLabel WebElement of the text box of the Designation
     */
    public WebElement getColorLabel() {
        return txtColorLabel;
    }

    /*
     * This method return the web element of the color picker of the textbox
     * 
     * @return txtColorPicker WebElement of color text box opens color picker
     */
    public WebElement getColorPicker() {
        return txtColorPicker;
    }

    /*
     * This method return the web element of the Color picker of the textbox
     * input box
     * 
     * @return txtColorPicker WebElement of Color input box
     */
    public WebElement getColorInput() {
        return txtColorPicker;
    }

    /*
     * This method return the web element of the Text color picker of the
     * textbox
     * 
     * @return txtColorTextPicker WebElement of color text box opens color
     * picker
     */
    public WebElement getColorTextPicker() {
        return txtColorTextPicker;
    }

    /*
     * This method return the web element of the color picker of the textbox
     * input box
     * 
     * @return txtColorPicker WebElement of Text color input box
     */
    public WebElement getTextColorInput() {
        return txtColorPicker;
    }

    /*
     * This method return the web element of the Text color picker of the
     * textbox
     * 
     * @return txtColorTextPicker WebElement of color text box opens color
     * picker
     */
    public WebElement getColorPickerPopUpOKButton() {
        return btnOKcolorPickerPopup;
    }

    /*
     * This method return the web element of the Active checkbox of color
     * 
     * @return txtColorTextPicker WebElement of color text box opens color
     * picker
     */
    public WebElement getActivebutton() {
        return btnActive;
    }
}
