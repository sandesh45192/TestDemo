/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains WebElements and methods of FlexTable page.
 * 
 * @author CSAutomation Team
 *
 */
public class FlexTablePage {

    @FindBy(xpath = "//span[@id='Flex Tables@0']")
    private WebElement flexTableNode;

    @FindBy(xpath = "//span[@id='Flex Tables~Presets@0']")
    private WebElement presetsNode;

    @FindBy(xpath = "//input[contains(@id,'FlexpresetName')]")
    private WebElement txtFlexPresetName;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetTemplate')]")
    private WebElement drpdwnFlexPresetTemplate;

    @FindBy(xpath = "//a[@class='CSPortalWindowCloser']")
    private WebElement btnWindowClose;

    @FindBy(xpath = "//span[contains(text(),'Data')]")
    private WebElement dataPane;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetLanguage')]")
    private WebElement drpdwnFlexPresetLanguage;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetColsDataType')]")
    private WebElement drpdwnFlexpresetColsDataType;

    @FindBy(xpath = "//div[@id='title__sections::Columns']")
    private WebElement selectionColumns;

    @FindBy(
            xpath = "//div[contains(@id,'ColsDataSelection')]//img[@class="
                    + "'CSGuiSelectionListAdd']")
    private WebElement btnAddData;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetProductsDataType')]")
    private WebElement drpdwnFlexpresetProductsDataType;

    @FindBy(
            xpath = "//div[contains(@id,'ProductsDataSelection')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAddProducts;

    @FindBy(xpath = "//div[@id='title__sections::Rows']")
    private WebElement selectionRows;

    @FindBy(xpath = "//nobr[contains(text(),'Data')]")
    private WebElement dataTab;

    @FindBy(
            xpath = "//tr[contains(@id,'AttributeRow')]//td[contains(@class,'GuiEditorRight odd')]")
    private WebElement rightSectionEditor;

    @FindBy(xpath = "//div[contains(@class,'FlexSettingsButton')]")
    private WebElement btnFlexSetting;

    @FindBy(xpath = "//select[@id='PresetID']")
    private WebElement drpdwnPresetId;

    @FindBy(
            xpath = "//tr[contains(@class,'MODALDIALOG_BUTTONS')]//input[@value='Reset Table']")
    private WebElement btnResetTable;

    @FindBy(xpath = "//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_OK']")
    private WebElement btnOk;

    @FindBy(
            xpath = "//div[contains(@id,'AttributesDataSelection')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAddAttributes;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetAttributesDataType')]")
    private WebElement drpdwnFlexpresetAttributesDataType;

    @FindBy(xpath = "//span[contains(text(),'General')]")
    private WebElement generalPane;

    @FindBy(xpath = "//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAddSelectionList;

    @FindBy(xpath = "//div[@id='title__sections::Sample Context']")
    private WebElement selectionSampleContext;

    @FindBy(xpath = "//div[contains(text(),'Reference Attribute')]/..")
    private WebElement selectionReferanceAttribute;

    @FindBy(xpath = "//input[contains(@id,'FlexpresetContextID__Label')]")
    private WebElement btnFlexPresetContextId;

    @FindBy(
            xpath = "//div[contains(@id,'AttributeIDToReferenceDataSelection')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAddAttributeToReferance;

    @FindBy(
            xpath = "//div[contains(@id,'AttributeIDsOfReferenceDataSelection')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAddAttributeIdOfReferance;

    @FindBy(
            xpath = "//div[contains(@id,'FieldsDataSelection')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAddFieldsDataSelections;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetFieldsDataType')]")
    private WebElement drpdwnFlexPresetFieldsDataType;

    @FindBy(xpath = "//span[@id='Configurations@0']")
    private WebElement drpdwnSelectionDialogAttribute;

    @FindBy(
            xpath = "//div[contains(@id,'Flexpreset_EXCEL_FILE')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAddFlexPresetExcelFile;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetEXCEL_XML_SHEET')]")
    private WebElement drpdwnFlexPresetExcelXmlSheet;

    @FindBy(xpath = "//div[contains(text(),'Configuration required')]")
    private WebElement configurationMessage;

    @FindBy(
            xpath = "//div[@class='CSGuiSelectionImgChooserContainer']//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAddChooserContainer;

    @FindBy(xpath = "//input[contains(@id,'FlexpresetTitle')]")
    private WebElement txtFlexPresetTitle;

    @FindBy(xpath = "//span[contains(@id,'FlexpresetEmptyRowsHidden')]")
    private WebElement btnHideEmptyRows;

    @FindBy(xpath = "//span[contains(@id,'FlexpresetEmptyColsHidden')]")
    private WebElement btnHideEmptyCols;

    @FindBy(xpath = "//span[contains(@id,'FlexpresetEqualCellsInRowsMerged')]")
    private WebElement btnMergeEqualRows;

    @FindBy(xpath = "//span[contains(@id,'FlexpresetEqualCellsInColsMerged')]")
    private WebElement btnMergeEqualCols;

    @FindBy(xpath = "//input[contains(@id,'FlexpresetMaxRows')]")
    private WebElement txtMaxRows;

    @FindBy(xpath = "//input[contains(@id,'FlexpresetMaxCols')]")
    private WebElement txtMaxCols;

    @FindBy(xpath = "//span[contains(text(),' Structure')]")
    private WebElement structurePane;

    @FindBy(xpath = "//span[contains(@id,'FlexpresetOrientationInverted')]")
    private WebElement btnOrientationInverted;

    @FindBy(xpath = "//span[contains(text(),'Layout')]")
    private WebElement layoutPane;

    @FindBy(xpath = "//input[contains(@id,'FlexpresetCols8-1')]")
    private WebElement txtTableWidth;

    @FindBy(xpath = "//input[contains(@id,'FlexpresetCols128-1')]")
    private WebElement txtTableHeight;

    @FindBy(xpath = "//span[contains(@id,'FlexpresetDisabled')]")
    private WebElement btnDisable;

    @FindBy(xpath = "//span[contains(text(),'Translations')]")
    private WebElement translationsPane;

    @FindBy(xpath = "//div[@id='title_Footer_sections::Table Caption']")
    private WebElement selectionTableCaption;

    @FindBy(xpath = "//div[@id='title_Footer_sections::Footer']")
    private WebElement selectionFooter;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetContextLanguage')]")
    private WebElement drpdwnFlexPresetContextLanguage;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetUserAccess')]")
    private WebElement drpdwnFlexPresetUserAccess;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetProductsChildrenLevel')]")
    private WebElement drpdwnFlexPresetProductsChildrenLevel;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetProductsFolderOption')]")
    private WebElement drpdwnFlexPresetProductsFolderOption;

    @FindBy(xpath = "//select[contains(@id,'FlexpresetContextClass')]")
    private WebElement drpdwnFlexPresetContextClass;

    /**
     * Parameterized constructor of class FlexTable page.
     * 
     * @param paramDriver Contains the instance of WebDriver.
     */
    public FlexTablePage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Settings node page POM elements");
    }

    /**
     * Returns the WebElement of drop down Context Class
     * 
     * @return drpdwnFlexPresetContextClass
     */
    public WebElement getDrpDwnFlexPresetContextClass() {
        return drpdwnFlexPresetContextClass;
    }

    /**
     * Returns the WebElement of drop down Products Folder Option
     * 
     * @return drpdwnFlexPresetProductsFolderOption
     */
    public WebElement getDrpDwnFlexPresetProductsFolderOption() {
        return drpdwnFlexPresetProductsFolderOption;
    }

    /**
     * Returns the WebElement of drop down Products Children Level
     * 
     * @return drpdwnFlexPresetProductsChildrenLevel
     */
    public WebElement getDrpDwnFlexPresetProductsChildrenLevel() {
        return drpdwnFlexPresetProductsChildrenLevel;
    }

    /**
     * Returns the WebElement of drop down User Access
     * 
     * @return drpdwnFlexPresetUserAccess
     */
    public WebElement getDrpDwnFlexPresetUserAccess() {
        return drpdwnFlexPresetUserAccess;
    }

    /**
     * Returns the WebElement of drop down Context Language
     * 
     * @return drpdwnFlexpresetContextLanguage
     */
    public WebElement getDrpDwnFlexPresetContextLanguage() {
        return drpdwnFlexPresetContextLanguage;
    }

    /**
     * Returns the WebElement of selections Footer
     * 
     * @return selectionFooter
     */
    public WebElement getSelectionFooter() {
        return selectionFooter;
    }

    /**
     * Returns the WebElement of selections Table Caption
     * 
     * @return selectionTableCaption
     */
    public WebElement getSelectionTableCaption() {
        return selectionTableCaption;
    }

    /**
     * Returns the WebElement Translations Pane
     * 
     * @return translationsPane4
     */
    public WebElement getTranslationsPane() {
        return translationsPane;
    }

    /**
     * Returns the WebElement Button Disable
     * 
     * @return btnDisable
     */
    public WebElement getBtnDisable() {
        return btnDisable;
    }

    /**
     * Returns the WebElement Text height of table
     * 
     * @return txtTableHeight
     */
    public WebElement getTxtTableHeight() {
        return txtTableHeight;
    }

    /**
     * Returns the WebElement Text width of table
     * 
     * @return txtTableWidth
     */
    public WebElement getTxtTableWidth() {
        return txtTableWidth;
    }

    /**
     * Returns the WebElement LayoutPane Pane
     * 
     * @return structurePane
     */
    public WebElement getLayoutPane() {
        return layoutPane;
    }

    /**
     * Returns the WebElement Button Orientation Inverted
     * 
     * @return btnOrientationInverted
     */
    public WebElement getBtnOrientationInverted() {
        return btnOrientationInverted;
    }

    /**
     * Returns the WebElement Text Max Rows
     * 
     * @return txtMaxRows
     */
    public WebElement getTxtMaxRows() {
        return txtMaxRows;
    }

    /**
     * Returns the WebElement Text Max columns
     * 
     * @return txtMaxCols
     */
    public WebElement getTxtMaxCols() {
        return txtMaxCols;
    }

    /**
     * Returns the WebElement Structure Pane
     * 
     * @return structurePane
     */
    public WebElement getStructurePane() {
        return structurePane;
    }

    /**
     * Returns the WebElement Button Merge Equal column
     * 
     * @return btnMergeEqualCols
     */
    public WebElement getBtnMergeEqualCols() {
        return btnMergeEqualCols;
    }

    /**
     * Returns the WebElement Button Merge Equal rows
     * 
     * @return btnMergeEqualRows
     */
    public WebElement getBtnMergeEqualRows() {
        return btnMergeEqualRows;
    }

    /**
     * Returns the WebElement Button Hide Empty column
     * 
     * @return btnHideEmptyCols
     */
    public WebElement getBtnHideEmptyCols() {
        return btnHideEmptyCols;
    }

    /**
     * Returns the WebElement Button Hide Empty Row
     * 
     * @return btnHideEmptyRows
     */
    public WebElement getBtnHideEmptyRows() {
        return btnHideEmptyRows;
    }

    /**
     * Returns the WebElement Flex Preset Title
     * 
     * @return txtFlexPresetTitle
     */
    public WebElement getTxtFlexPresetTitle() {
        return txtFlexPresetTitle;
    }

    /**
     * Returns the WebElement of button add Excel file
     * 
     * @return btnAddChooserContainer
     */
    public WebElement getBtnAddChooserContainer() {
        return btnAddChooserContainer;
    }

    /**
     * Returns the WebElement of configuration message
     * 
     * @return configurationMessage
     */
    public WebElement getConfigurationMessage() {
        return configurationMessage;
    }

    /**
     * Returns the WebElement of drop down selection dialog Excel Xml sheet
     * 
     * @return drpdwnFlexPresetExcelXmlSheet
     */
    public WebElement getDrpDwnFlexPresetExcelXmlSheet() {
        return drpdwnFlexPresetExcelXmlSheet;
    }

    /**
     * Returns the WebElement of button add Excel File
     * 
     * @return btnAddFlexPresetExcelFile
     */
    public WebElement getBtnAddFlexPresetExcelFile() {
        return btnAddFlexPresetExcelFile;
    }

    /**
     * Returns the WebElement of drop down selection dialog Attribute node
     * 
     * @return drpdwnSelectionDialogAttribute
     */
    public WebElement getDrpDwnSelectionDialogAttribute() {
        return drpdwnSelectionDialogAttribute;
    }

    /**
     * Returns the WebElement of drop down fields data type
     * 
     * @return drpdwnFlexPresetFieldsDataType
     */
    public WebElement getDrpDwnFlexPresetFieldsDataType() {
        return drpdwnFlexPresetFieldsDataType;
    }

    /**
     * Returns the WebElement of button add fields Data Selections
     * 
     * @return btnAddFieldsDataSelections
     */
    public WebElement getBtnAddFieldsDataSelections() {
        return btnAddFieldsDataSelections;
    }

    /**
     * Returns the WebElement of button add AttributesId of Reference
     * 
     * @return btnAddAttributeIdOfReferance
     */
    public WebElement getBtnAddAttributeIdOfReferance() {
        return btnAddAttributeIdOfReferance;
    }

    /**
     * Returns the WebElement of button add Attributes to Reference
     * 
     * @return btnAddAttributeToReferance
     */
    public WebElement getBtnAddAttributeToReferance() {
        return btnAddAttributeToReferance;
    }

    /**
     * Returns the WebElement Button Flex Preset Context ID
     * 
     * @return btnFlexPresetContextId
     */
    public WebElement getBtnFlexPresetContextId() {
        return btnFlexPresetContextId;
    }

    /**
     * Returns the WebElement of selections Reference Attribute
     * 
     * @return selectionReferanceAttribute
     */
    public WebElement getSelectionReferanceAttribute() {
        return selectionReferanceAttribute;
    }

    /**
     * Returns the WebElement of selections Sample Context
     * 
     * @return selectionSampleContext
     */
    public WebElement getSelectionSampleContext() {
        return selectionSampleContext;
    }

    /**
     * Returns the WebElement of button Add Selection List
     * 
     * @return btnAddSelectionList
     */
    public WebElement getBtnAddSelectionList() {
        return btnAddSelectionList;
    }

    /**
     * Returns the WebElement of pane name General
     * 
     * @return generalPane
     */
    public WebElement getGeneralPane() {
        return generalPane;
    }

    /**
     * Returns the WebElement of drop down Attribute data type
     * 
     * @return drpdwnFlexpresetAttributesDataType
     */
    public WebElement getDrpDwnFlexpresetAttributesDataType() {
        return drpdwnFlexpresetAttributesDataType;
    }

    /**
     * Returns the WebElement of button add Attributes
     * 
     * @return btnAddAttributes
     */
    public WebElement getBtnAddAttributes() {
        return btnAddAttributes;
    }

    /**
     * Returns the WebElement of button Ok
     * 
     * @return btnOk
     */
    public WebElement getBtnOk() {
        return btnOk;
    }

    /**
     * Returns the WebElement of button reset table
     * 
     * @return btnResetTable
     */
    public WebElement getBtnResetTable() {
        return btnResetTable;
    }

    /**
     * Returns the WebElement of drop down preset Id
     * 
     * @return drpdwnPresetId
     */
    public WebElement getDrpDwnPresetId() {
        return drpdwnPresetId;
    }

    /**
     * Returns the WebElement of Button Flex setting
     * 
     * @return btnFlexSetting
     */
    public WebElement getBtnFlexSetting() {
        return btnFlexSetting;
    }

    /**
     * Returns the WebElement of editor right section
     * 
     * @return rightSectionEditor
     */
    public WebElement getRightSectionEditor() {
        return rightSectionEditor;
    }

    /**
     * Returns the WebElement of tab name data
     * 
     * @return dataTab
     */
    public WebElement getDataTab() {
        return dataTab;
    }

    /**
     * Returns the WebElement of selections rows
     * 
     * @return selectionRows
     */
    public WebElement getSelectionRows() {
        return selectionRows;
    }

    /**
     * Returns the WebElement of button add Products
     * 
     * @return btnAddProducts
     */
    public WebElement getBtnAddProducts() {
        return btnAddProducts;
    }

    /**
     * Returns the WebElement of Flex preset Products data type
     * 
     * @return drpdwnFlexpresetProductsDataType
     */
    public WebElement getDrpDwnFlexpresetProductsDataType() {
        return drpdwnFlexpresetProductsDataType;
    }

    /**
     * Returns the WebElement of button add Data
     * 
     * @return btnAddData
     */
    public WebElement getBtnAddData() {
        return btnAddData;
    }

    /**
     * Returns the WebElement of selections columns
     * 
     * @return selectionColumns
     */
    public WebElement getSelectionColumns() {
        return selectionColumns;
    }

    /**
     * Returns the WebElement of Flex preset column data type
     * 
     * @return drpdwnFlexpresetLanguage
     */
    public WebElement getDrpDwnFlexpresetColsDataType() {
        return drpdwnFlexpresetColsDataType;
    }

    /**
     * Returns the WebElement of Flex preset language
     * 
     * @return drpdwnFlexpresetLanguage
     */
    public WebElement getDrpDwnFlexPresetLanguage() {
        return drpdwnFlexPresetLanguage;
    }

    /**
     * Returns the WebElement of pane name data
     * 
     * @return dataPane
     */
    public WebElement getDataPane() {
        return dataPane;
    }

    /**
     * Returns the WebElement of button window close
     * 
     * @return btnWindowClose
     */
    public WebElement getBtnWindowClose() {
        return btnWindowClose;
    }

    /**
     * Returns the WebElement drpdwnFlexPresetTemplate
     * 
     * @return drpdwnFlexPresetTemplate
     */
    public WebElement getDrpDwnFlexPresetTemplate() {
        return drpdwnFlexPresetTemplate;
    }

    /**
     * Returns the WebElement txtFlexPresetName
     * 
     * @return txtFlexPresetName
     */
    public WebElement getTxtFlexPresetName() {
        return txtFlexPresetName;
    }

    /**
     * Returns the WebElement Presets Node
     * 
     * @return presetsNode
     */
    public WebElement getPresetsNode() {
        return presetsNode;
    }

    /**
     * Returns the WebElement flex Table node
     * 
     * @return flexTableNode
     */
    public WebElement getFlexTableNode() {
        return flexTableNode;
    }

    /**
     * Clicks on node flex table
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnFlexTableNode(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getFlexTableNode());
        getFlexTableNode().click();
        CSLogger.info("Clicked Node Flex Table ");
    }

    /**
     * Clicks on presets node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnPresetsNode(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getPresetsNode());
        getPresetsNode().click();
        CSLogger.info("Clicked Node Flex Table ");
    }

    /**
     * Clicks on Tab name data
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnDataTab(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getDataTab());
        getDataTab().click();
        CSLogger.info("Clicked Tab Data ");
    }

    /**
     * Clicks on button add Attribute
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnAddAttrbutes(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAddAttributes());
        getBtnAddAttributes().click();
        CSLogger.info("clicked on Button Attributes");
    }

    /**
     * Clicks on Pane name General
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnGeneralPane(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getGeneralPane());
        getGeneralPane().click();
        CSLogger.info("Clicked on pane general");
    }

    /**
     * Clicks on Button Add Selection List
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnAddSelectionList(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAddSelectionList());
        getBtnAddSelectionList().click();
        CSLogger.info("Clicked on Button Add Selection List");
    }

    /**
     * Clicks on Button Flex Preset Context ID
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnFlexPresetContextId(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnFlexPresetContextId());
        getBtnFlexPresetContextId().click();
        CSLogger.info("Clicked on Button Add Selection List");
    }

    /**
     * Clicks on button add Attribute to Reference
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnAddAttributeToReferance(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAddAttributeToReferance());
        getBtnAddAttributeToReferance().click();
        CSLogger.info("Clicked on Button Attributes to Reference");
    }

    /**
     * Clicks on button add Attributes Id to Reference
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnAddAttributeIdOfReferance(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAddAttributeIdOfReferance());
        getBtnAddAttributeIdOfReferance().click();
        CSLogger.info("Clicked on Button Attributes of Reference");
    }

    /**
     * Clicks on button add fields data selections
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnAddFieldsDataSelections(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAddFieldsDataSelections());
        getBtnAddFieldsDataSelections().click();
        CSLogger.info("Clicked on Button add field data selection");
    }

    /**
     * Clicks on drop down selection dialog Attribute node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void
            clkOnDrpDwnSelectionDialogAttribute(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getDrpDwnSelectionDialogAttribute());
        getDrpDwnSelectionDialogAttribute().click();
        CSLogger.info("Clicked on Attribute node Dialog box");
    }

    /**
     * Clicks on Button Add excel xml file
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnAddFlexPresetExcelFile(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAddFlexPresetExcelFile());
        getBtnAddFlexPresetExcelFile().click();
        CSLogger.info("Clicked on Button add Excel xml File");
    }

    /**
     * Clicks on Button Add Excel file
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnAddChooserContainer(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAddChooserContainer());
        getBtnAddChooserContainer().click();
        CSLogger.info("Clicked on Button add Excel File");
    }

    /**
     * Clicks on Data Pane
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnDataPane(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getDataPane());
        getDataPane().click();
        CSLogger.info("Clicked on Data Pane");
    }

    /**
     * Clicks on Button Hide Empty Row
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnHideEmptyRow(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnHideEmptyRows());
        getBtnHideEmptyRows().click();
        CSLogger.info("Clicked on Hide Empty Row");
    }

    /**
     * Clicks on Button Hide Empty Row
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnHideEmptyCols(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnHideEmptyCols());
        getBtnHideEmptyCols().click();
        CSLogger.info("Clicked on Hide Empty column");
    }

    /**
     * Clicks on Button Merge Equal Rows
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnMergeEqualRow(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnMergeEqualRows());
        getBtnMergeEqualRows().click();
        CSLogger.info("Clicked on Merge Equal Rows");
    }

    /**
     * Clicks on Button Merge Equal Columns
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnMergeEqualCols(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnMergeEqualCols());
        getBtnMergeEqualCols().click();
        CSLogger.info("Clicked on Merge Equal Columns");
    }

    /**
     * Clicks on Structure Pane
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnStructurePane(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getStructurePane());
        getStructurePane().click();
        CSLogger.info("Clicked on Structure Pane");
    }

    /**
     * Set the column limit
     * 
     * @param waitForReload WebDriverWait object
     * @param colLimit String object contains column limit
     */
    public void setColumnLimit(WebDriverWait waitForReload, String colLimit) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getTxtMaxCols());
        getTxtMaxCols().clear();
        getTxtMaxCols().sendKeys(colLimit);
        CSLogger.info("Column Limit is set to " + colLimit);
    }

    /**
     * Set the Row limit
     * 
     * @param waitForReload WebDriverWait object
     * @param rowLimit String object contains row limit
     */
    public void setRowLimit(WebDriverWait waitForReload, String rowLimit) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getTxtMaxRows());
        getTxtMaxRows().clear();
        getTxtMaxRows().sendKeys(rowLimit);
        CSLogger.info("Row Limit is set to " + rowLimit);
    }

    /**
     * Clicks on Button Orientation Inverted
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnOrientationInverted(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnOrientationInverted());
        getBtnOrientationInverted().click();
        CSLogger.info("Clicked on Button Orientation Inverted");
    }

    /**
     * Clicks on Layout Pane
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnLayoutPane(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getLayoutPane());
        getLayoutPane().click();
        CSLogger.info("Clicked on Layout Pane");
    }

    /**
     * Set the width of Table
     * 
     * @param waitForReload WebDriverWait object
     * @param width String object contains width of table
     */
    public void setWidthOfTable(WebDriverWait waitForReload, String width) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getTxtTableWidth());
        getTxtTableWidth().clear();
        getTxtTableWidth().sendKeys(width);
        CSLogger.info("Width is set to " + width + " mm");
    }

    /**
     * Set the Height of Table
     * 
     * @param waitForReload WebDriverWait object
     * @param height String object contains height of table
     */
    public void setHeightOfTable(WebDriverWait waitForReload, String height) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getTxtTableHeight());
        getTxtTableHeight().clear();
        getTxtTableHeight().sendKeys(height);
        CSLogger.info("Height is set to " + height + " mm");
    }

    /**
     * Clicks on Disable Button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnDisable(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnDisable());
        getBtnDisable().click();
        CSLogger.info("Clicked on Button Disable");
    }

    /**
     * Clicks on Layout Pane
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnTranslationsPane(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getTranslationsPane());
        getTranslationsPane().click();
        CSLogger.info("Clicked on Translations Pane");
    }

    /**
     * Clicks on Selection Table Caption
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnSelectionTableCaption(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getSelectionTableCaption());
        getSelectionTableCaption().click();
        CSLogger.info("Clicked on Selection Table Caption");
    }

    /**
     * Clicks on Selection Footer
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnSelectionFooter(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getSelectionFooter());
        getSelectionFooter().click();
        CSLogger.info("Clicked on Selection Footer");
    }

    /**
     * Clicks on window close Button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnWindowClose(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnWindowClose());
        getBtnWindowClose().click();
        CSLogger.info("Clicked on Button window close");
    }

    /**
     * This method traverse to the preset node of flex table
     * 
     */
    public void goToNodeFlexTablePreset(WebDriverWait waitForReload,
            WebDriver browserDriver, FrameLocators locator) {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        clkOnFlexTableNode(waitForReload);
        clkOnPresetsNode(waitForReload);
    }
}
