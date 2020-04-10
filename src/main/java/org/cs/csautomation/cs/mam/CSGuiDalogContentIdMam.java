/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.mam;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CSGuiDalogContentIdMam {

    @FindBy(xpath = "//nobr[contains(.,'Properties')]")
    private WebElement btnPropertiesMam;

    @FindBy(xpath = "//select[contains(@name,'-27201')]")
    private WebElement smartPresetDropDownInProperties;

    @FindBy(
            xpath = "//span[contains(@id,'MamfileconfigurationIsCategoryClass_GUI')]")
    private WebElement showInCategoryTreeMam;

    @FindBy(
            xpath = "//div[contains(@id,'Mamfile_ClassMapping_csReferenceDiv')]"
                    + "//div[@class='CSGuiSelectionImgChooserContainer']"
                    + "//img[@class='CSGuiSelectionListAdd']")
    private WebElement clkPlusToAddClassMam;

    @FindBy(xpath = "//span[contains(text(),'Classes')]")
    private WebElement classesInMam;

    @FindBy(xpath = "//div[contains(text(),'Inherited')]")
    private WebElement sectionInherited;

    @FindBy(xpath = "//div[contains(text(),'Deleted')]")
    private WebElement csPopupDelete;

    @FindBy(xpath = "//td[contains(@id,'splitareaborderright')]")
    private WebElement splitAreaBorderRight;

    @FindBy(xpath = "//input[contains(@id,'MamfileExternalKey')]")
    private WebElement txtExternalKey;

    @FindBy(xpath = "//textarea[contains(@id,'MamfileCS_ItemAttribute_1_31')]")
    private WebElement txtKeyWordField;

    @FindBy(xpath = "//input[contains(@id,'MamarchiveBackupPath')]")
    private WebElement txtArchiveBackupPath;

    /**
     * returns the instance of Archive Backup Path
     * 
     * @return txtArchiveBackupPath
     */
    public WebElement getTxtArchiveBackupPath() {
        return txtArchiveBackupPath;
    }

    /**
     * returns the instance of keyword field
     * 
     * @return txtKeyWordField
     */
    public WebElement getTxtKeywordField() {
        return txtKeyWordField;
    }

    /**
     * This method returns the instance of textbox of external key
     * 
     * @return externalKeyTxtBox
     */
    public WebElement getExternalKeyTxtBox() {
        return txtExternalKey;
    }

    /**
     * This method returns the instance of split area border frame
     * 
     * @return splitAreaBorderRight
     */
    public WebElement getSplitAreaBorderRight() {
        return splitAreaBorderRight;
    }

    /**
     * This method returns instance of delete option
     * 
     * @return optDelete
     */
    public WebElement getClkDelete() {
        return csPopupDelete;
    }

    /**
     * This method returns the instance of inherited section
     * 
     * @return sectionInherited
     */
    public WebElement getSectionInherited() {
        return sectionInherited;
    }

    /**
     * Returns instance of classes in Mam
     * 
     * @return classesInMam
     */
    public WebElement getClassesInMam() {
        return classesInMam;
    }

    /**
     * Returns instance of plus button to add class in demo volume folder of mam
     * 
     * @return clkPlusToAddClassMam
     */
    public WebElement getClkPlusToAddClassMam() {
        return clkPlusToAddClassMam;
    }

    /**
     * Returns instance of show in category tree in mam
     * 
     * @return showInCategoryTreeMam
     */
    public WebElement getShowInCategoryTreeMam() {
        return showInCategoryTreeMam;
    }

    /**
     * Returns instance of preset drop down in properties
     * 
     * @return smartPresetDropDownInProperties
     */
    public WebElement getSmartPresetDropDownInProperties() {
        return smartPresetDropDownInProperties;
    }

    /**
     * Returns instance of properties in mam
     * 
     * @return btnPropertiesMam
     */
    public WebElement getBtnPropertiesMam() {
        return btnPropertiesMam;
    }

    public CSGuiDalogContentIdMam(WebDriver paramDriver) {
        setBrowserDriverInstanceMam(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized CSGuiDialogContentId page POM elements.");
    }

    /**
     * @param paramDriver
     */
    private void setBrowserDriverInstanceMam(WebDriver paramDriver) {
    }
}
