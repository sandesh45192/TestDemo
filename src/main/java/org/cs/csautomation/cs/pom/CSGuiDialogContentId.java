
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CSGuiDialogContentId {

    private WebDriver  browserDriverInstance;

    @FindBy(id = "CSTypeLabel")
    private WebElement btnCSTypeLabel;

    @FindBy(xpath = "//input[contains(@id,'PdmarticleconfigurationName')]")
    private WebElement txtPdmarticleconfigurationName;

    @FindBy(xpath = "//input[contains(@id,'PdmarticleconfigurationLabel')]")
    private WebElement txtPdmarticleconfigurationLabel;

    @FindBy(
            xpath = "//input[contains(@id,'PdmarticleconfigurationGuiSortOrder')]")
    private WebElement txtPdmarticleconfigurationGuiSortOrder;

    @FindBy(
            xpath = "//select[contains(@id,'PdmarticleconfigurationIsRequired')]")
    private WebElement ddPdmarticleconfigurationIsRequired;

    @FindBy(
            xpath = "//select[contains(@id,'PdmarticleconfigurationIsInherited')]")
    private WebElement ddPdmarticleconfigurationIsInherited;

    @FindBy(
            xpath = "//select[contains(@id,'PdmarticleconfigurationIsSearchable')]")
    private WebElement ddPdmarticleconfigurationIsSearchable;

    @FindBy(xpath = "//span[contains(@id,'PdmarticleconfigurationParamB_GUI')]")
    private WebElement cbmultipleSelection;

    @FindBy(xpath = "//input[contains(@id,'PdmarticleconfigurationPaneTitle')]")
    private WebElement txtPdmarticleconfigurationPaneTitle;

    @FindBy(
            xpath = "//span[contains(@id,'PdmarticleconfigurationLanguageDependent_GUI')]")
    private WebElement cbPdmarticleconfigurationLanguageDependent;

    @FindBy(
            xpath = "//span[contains(@id,'PdmarticleconfigurationIsCategoryClass_GUI')]")
    private WebElement btnShowClassInCategoryTree;

    @FindBy(
            xpath = "//span[contains(@id,'PdmarticleconfigurationIsCategoryClass_GUI')]")
    private WebElement cbPdmarticleconfigurationIsCategoryClass;

    @FindBy(
            xpath = "//span[contains(@id,'PdmarticleconfigurationIsCategoryClass_GUI')]/span")
    private WebElement cbPdmarticleconfigurationIsCategoryClassSpan;

    @FindBy(xpath = "//nobr[contains(text(),'Properties')]")
    private WebElement tabProperties;

    @FindBy(
            xpath = "//div[contains(@id,'Pdmarticle_ClassMapping_csReferenceDiv')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement pdmArticleClassMappingCsReference;

    @FindBy(
            xpath = "//div[contains(@id,'Pdmarticleconfiguration_LinkedIDs_csReferenceDiv')]/div[2]/img[1]")
    public WebElement  btnAddAttrToClass;

    @FindBy(xpath = "//a[@id='CS_Configurations@0_ANCHOR']/img[1]")
    private WebElement btnAttrDrpDwnAddToClass;

    @FindBy(xpath = "//span[contains(text(),'AttributeFolderName1')]")
    private WebElement attrFolderToBeAddedToClass;

    @FindBy(xpath = "//a/img[contains(@src,'add.png')]/..")
    private WebElement btnAddAllAttr;

    @FindBy(xpath = "//input[@value='Yes']")
    private WebElement btnYesToEnlistAllAttrToAddToClass;

    @FindBy(xpath = "//nobr[contains(text(),'attribute1')]")
    private WebElement locationOfAttrOne;

    @FindBy(xpath = "//nobr[contains(text(),'referenceToPim')]")
    private WebElement locationOfAttrRefToPim;

    @FindBy(xpath = "//nobr[contains(text(),'Inheritance')]")
    private WebElement locationOfAttrInheritance;

    @FindBy(xpath = "//nobr[contains(text(),'Searchable')]")
    private WebElement locationOfAttrSearchable;

    @FindBy(xpath = "//nobr[contains(text(),'Required')]")
    private WebElement locationOfAttrRequired;

    @FindBy(xpath = "//nobr[contains(text(),'Language')]")
    private WebElement locationOfAttrLanguage;

    @FindBy(
            xpath = "//*[@id=\"CSGuiModalDialogFormName\"]/table/tbody/tr[4]/td/table/tbody/tr/td/nobr/input[2]")
    private WebElement btnExtend;

    @FindBy(xpath = "//input[@value='Replace']")
    private WebElement btnReplace;

    @FindBy(xpath = "//tr[@name='Remove']")
    private WebElement clkRemove;

    @FindBy(
            xpath = "//div[contains(@id,'Pdmarticlestructure_ClassMapping_csReferenceDiv')]/div[2]/img")
    private WebElement pdmArticleStructureClassMappingCsReferenceDiv;

    @FindBy(xpath = "//table[@class='CSGuiTable']/tbody/tr[2]/td[2]/span")
    private WebElement lblViewAssignedClassName;

    @FindBy(xpath = "//nobr[contains(text(),'Data')]")
    private WebElement tabData;

    @FindBy(xpath = "//tr[@name='Add Again']")
    private WebElement clkAddAgain;

    @FindBy(
            xpath = "//div[contains(@id,'Pdmarticleconfiguration_AllowedClasses_csReferenceDiv')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement btnAllowedSubclasses;

    @FindBy(xpath = "//span[@id='ConfigurationLinks@0']")
    WebElement         dropDownClassesFolder;

    @FindBy(
            xpath = "//div[contains(@id,'Pdmarticle_ClassMapping_csReferenceDiv')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement addClassToProductByPlus;

    @FindBy(xpath = "//input[contains(@id,'PdmarticleExternalKey')]")
    private WebElement txtExternalKey;

    @FindBy(xpath = "//span[contains(text(),'External Key')]")
    private WebElement lblExternalKey;

    @FindBy(xpath = "//input[contains(@id,'PdmarticleLabel')]")
    private WebElement txtProductLabel;

    @FindBy(id = "title_9fc2d28c_sections::Selection")
    private WebElement ddSelectionTitle;

    @FindBy(
            xpath = "//input[contains(@id,'PdmarticleconfigurationSectionTitle')]")
    private WebElement txtPdmarticleconfigurationSectionTitle;

    @FindBy(xpath = "//select[@name='attribute']")
    private WebElement ddSearchInputAttributeSelect;

    @FindBy(xpath = "//select[@name='operator']")
    private WebElement ddSearchInputOperatorSelect;

    @FindBy(
            xpath = "//div[contains(@class,'inputSelectWrapper')]/following-sibling::input")
    private WebElement txtAdvanceSearchInputText;

    @FindBy(xpath = "//td[contains(text(),'Edit')]")
    private WebElement editLink;

    @FindBy(
            xpath = "(//a[@class='CSGuiToolbarHorizontalButton CSGuiToolbarHoverButton '])[1]")
    private WebElement btnAccept;

    @FindBy(
            xpath = "//input[contains(@id,'PdmarticleconfigurationExternalKey')]")
    private WebElement txtPdmarticleconfigurationExternalKey;

    @FindBy(xpath = "//select[contains(@id,'PdmarticleconfigurationParamA')]")
    private WebElement drpdwnValueList;

    @FindBy(
            xpath = "//input[contains(@id,'PdmarticleconfigurationItemconfigurationID__Label')]")
    private WebElement btnClassTable;

    @FindBy(xpath = "//input[@value='Recursive']")
    private WebElement btnRecursive;

    @FindBy(xpath = "//input[@value='Now']")
    private WebElement btnNow;

    @FindBy(xpath = "//input[@id='CSGuiEditorDialogIDInput']")
    private WebElement txtAttributeId;

    @FindBy(xpath = "//textarea[contains(@id,'PdmarticleconfigurationParamA')]")
    private WebElement txtValues;

    @FindBy(
            xpath = "//div[contains(@id,'PdmarticleCS_ItemAttribute')]/div[2]/img")
    public WebElement  btnReferenceToPimProduct;

    @FindBy(xpath = "//div[contains(@class,'CSPortalWindowContent')]")
    private WebElement csPortalWindowContentDiv;

    @FindBy(xpath = "//nobr[contains(text(),'Attribute Mappings')]")
    private WebElement tabAttributeMapping;

    @FindBy(xpath = "//input[contains(@id,'PdmarticleconfigurationParamF')]")
    private WebElement txtUnit;

    @FindBy(xpath = "//select[contains(@id,'PdmarticleconfigurationParamF')]")
    private WebElement drpdwnUnit;

    @FindBy(
            xpath = "//input[contains(@id,'migrationPdmarticleconfigurationGuiSortOrder')]")
    private WebElement txtSortOrder;

    @FindBy(
            xpath = "//select[contains(@id,'PdmarticleconfigurationFacettedSearch')]")
    private WebElement drpdwnFacetedSearch;

    @FindBy(
            xpath = "//select[contains(@id,'PdmarticleconfigurationFacetConcatenator')]")
    private WebElement drpdwnFacetConcatenator;

    @FindBy(
            xpath = "//input[contains(@id,'PdmarticleconfigurationFacetLabel')]")
    private WebElement txtFacetLabel;

    @FindBy(
            xpath = "//input[contains(@id,'PdmarticleconfigurationmappingPaneTitle')]")
    private WebElement txtPaneTitle;

    @FindBy(
            xpath = "//input[contains(@id,'PdmarticleconfigurationmappingSectionTitle')]")
    private WebElement txtSectionTitle;

    @FindBy(
            xpath = "//select[contains(@id,'PdmarticleconfigurationmappingFacettedSearch')]")
    private WebElement drpdwnMappingFacetedSearch;

    @FindBy(
            xpath = "//select[contains(@id,'PdmarticleconfigurationmappingFacetConcatenator')]")
    private WebElement drpdwnMappingFacetedConcatenator;

    @FindBy(
            xpath = "//select[contains(@id,'PdmarticleconfigurationValueRangeType')]")
    private WebElement drpdwnValueRange;

    /**
     * This method returns the instance of portal window div
     * 
     * @return csPortalWindowContentDiv
     */
    public WebElement getCsPortalWindowContentDiv() {
        return csPortalWindowContentDiv;
    }

    /**
     * This method returns the instance of text area values
     * 
     * @return txtvalues
     */
    public WebElement getTxtValues() {
        return txtValues;
    }

    /**
     * This method returns the instance of Accept button after adding access
     * rights
     * 
     * @return btnAccept
     */
    public WebElement getBtnAccept() {
        return btnAccept;
    }

    /**
     * This method clicks on element
     * 
     * @param waitForReload waits for an element to reload
     * @param element contains web element
     */
    public void clkElement(WebDriverWait waitForReload, WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.click();
        CSLogger.info("Clicked on element");
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    public CSGuiDialogContentId(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized CSGuiDialogContentId page POM elements.");
    }

    /**
     * Returns the instance of Attribute ID
     * 
     * @return txtAttributeId
     */
    public WebElement getTxtAttributeId() {
        return txtAttributeId;
    }

    /**
     * Returns the instance showing Class Table button
     * 
     * @return btnClassTable
     */
    public WebElement getBtnClassTable() {
        return btnClassTable;
    }

    /**
     * Returns the instance of drop down Value List
     * 
     * @return drpdwnValueList
     */
    public WebElement getDrpDwnValueList() {
        return drpdwnValueList;
    }

    /**
     * This method returns the instance of edit Link
     * 
     * @return editLink
     */
    public WebElement getEditLink() {
        return editLink;
    }

    /**
     * This method returns the instance of external key text field
     * 
     * @return txtExternalKey
     */
    public WebElement getTxtPdmarticleconfigurationExternalKey() {
        return txtPdmarticleconfigurationExternalKey;
    }

    /**
     * This method returns the instance of external key text field >>>>>>>
     * f904a470ec6af779afd46a3ba51fec4d4de3229f
     * 
     * @return txtExternalKey
     */
    public WebElement getTxtExternalKey() {
        return txtExternalKey;
    }

    /**
     * This method returns the instance of Remove option
     * 
     * @return clkRemove
     */
    public WebElement getClkRemove() {
        return clkRemove;
    }

    /**
     * This method returns the instance of Add Again option
     * 
     * @return clkAddAgain
     */
    public WebElement getClkAddAgain() {
        return clkAddAgain;
    }

    /**
     * This method returns the instance of allowed subclasses btn >>>>>>>
     * f904a470ec6af779afd46a3ba51fec4d4de3229f
     * 
     * @return btnAllowedSubclasses
     */
    public WebElement getBtnAllowedSublasses() {
        return btnAllowedSubclasses;
    }

    /**
     * Returns the webElement cbPdmarticleconfigurationIsCategoryClassSpan
     * 
     * @return cbPdmarticleconfigurationIsCategoryClassSpan
     */
    public WebElement getCbPdmarticleconfigurationIsCategoryClassSpan() {
        return cbPdmarticleconfigurationIsCategoryClassSpan;
    }

    /**
     * Returns the instance showing category tree button
     * 
     * @return btnShowClassIncategoryTree
     */
    public WebElement getBtnShowInCategoryTree() {
        return btnShowClassInCategoryTree;
    }

    /**
     * This method clicks on the show in category tree button
     */
    public void clkBtnShowInCategoryTree() {
        getBtnShowInCategoryTree().click();
    }

    /**
     * Returns the instance of extend button after dragging class to product
     * 
     * @return classFolderToDragDrop
     */
    public WebElement getBtnExtend() {
        return btnExtend;
    }

    /**
     * Returns the instance of replace button after dragging class to product
     * 
     * @return classFolderToDragDrop
     */
    public WebElement getBtnReplace() {
        return btnReplace;
    }

    /**
     * Returns the instance of drop down classes folder
     * 
     * @return dropDownClassesFolder
     */
    public WebElement getDropDownClassesFolder() {
        return dropDownClassesFolder;
    }

    /**
     * Returns the instance of add class to product
     * 
     * @return addClassToProductByPlus
     */
    public WebElement getAddClassToProductByPlus() {
        return addClassToProductByPlus;
    }

    /**
     * Returns the instance of language attribute
     * 
     * @return locationOfAttrLanguage
     */
    public WebElement getLocationOfAttrLanguage() {
        return locationOfAttrLanguage;
    }

    /**
     * Returns the instance of attribute folder which is to drag and drop on
     * class
     * 
     * @return attrFolderToDragDrop
     */
    public WebElement getdropDownAttributeType(String attributeType) {
        switch (attributeType) {
        case "Searchable":
            return getDdPdmarticleconfigurationIsSearchable();
        case "IsFolder":
            return getDdPdmarticleconfigurationIsRequired();
        case "Inheritance":
            return getDdPdmarticleconfigurationIsInherited();
        default:
            return getBtnCSTypeLabel();
        }
    }

    /**
     * Returns the instance RequiredAttribute
     * 
     * @return locationOfAttrRequired
     */
    public WebElement getLocationOfAttrRequired() {
        return locationOfAttrRequired;
    }

    /**
     * Returns the instance of searchable attribute
     * 
     * @return locationOfAttrSearchable
     */
    public WebElement getLocationOfAttrSearchable() {
        return locationOfAttrSearchable;
    }

    /**
     * Returns the instance of inheritance attribute
     * 
     * @return locationOfAttrInheritance
     */
    public WebElement getLocationOfAttrInheritance() {
        return locationOfAttrInheritance;
    }

    /**
     * Returns the instance of location of First attribute
     * 
     * @return locationOfAttrOne
     */
    public WebElement getLocationOfAttrOne() {
        return locationOfAttrOne;
    }

    /**
     * Returns the instance of location of Reference to Pim attribute
     * 
     * @return locationOfAttrRefToPim
     */
    public WebElement getLocationOfAttrRefToPim() {
        return locationOfAttrRefToPim;
    }

    public WebElement getBtnAddAttrToClass() {
        return btnAddAttrToClass;
    }

    /**
     * This method clicks on button for adding attributes to class
     */
    public void clkOnBtnToAddAttrToClass() {
        getBtnAddAttrToClass().click();
    }

    /**
     * returns the instance of attributes from drop down to class
     * 
     * @return btnAttrDrpDwnAddToClass
     */
    public WebElement getBtnAttrDrpDwnAddToClass() {
        return btnAttrDrpDwnAddToClass;
    }

    /**
     * This method clicks on the attributes in drop down
     */
    public void clkOnDropDwnAttrAddToClass() {
        getBtnAttrDrpDwnAddToClass().click();
    }

    /**
     * Returns the instance of attribute folder which is being added to class
     * 
     * @return attrFolderToBeAddedToClass
     */
    public WebElement attrFolderToBeAddedToClass() {
        return attrFolderToBeAddedToClass;
    }

    /**
     * This method clicks on newly created attribute folder which is being added
     * to the class
     */
    public void clkOnAttrFolderToBeAddedToClass() {
        attrFolderToBeAddedToClass().click();
    }

    /**
     * Returns the instance for adding all attribute values to class
     * 
     * @return btnAddAllAttr
     */
    public WebElement getBtnAddAllAttr() {
        return btnAddAllAttr;
    }

    /**
     * This method clicks on add all attribute values to class selected from
     * drop down
     */
    public void clkOnAddAllAttrValuesToClass(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnAddAllAttr());
        getBtnAddAllAttr().click();
    }

    /**
     * Returns the instance of button which shows list of all attribures which
     * are required to add to class
     * 
     * @return btnYesToEnlistAllAttrToAddToClass
     */
    public WebElement getBtnYesToEnlistAllAttrToAddToClass() {
        return btnYesToEnlistAllAttrToAddToClass;
    }

    /**
     * This method clicks on yes button for adding all attributes to class
     */
    public void clkOnYesToEnlistAllAttrToAddToClass() {
        getBtnYesToEnlistAllAttrToAddToClass().click();
    }

    /**
     * Returns the WebElement pdmArticleClassMappingCsReference .
     * 
     * @return pdmArticleClassMappingCsReference CSGuiDialogContentId
     */
    public WebElement getPdmArticleClassMappingCsReference() {
        return pdmArticleClassMappingCsReference;
    }

    /**
     * Returns the WebElement btnProperties .
     * 
     * @return btnProperties CSGuiDialogContentId
     */
    public WebElement getTabProperties() {
        return tabProperties;
    }

    /**
     * Returns the WebElement btnCSTypeLabel .
     * 
     * @return btnCSTypeLabel CSGuiDialogContentId
     */
    public WebElement getBtnCSTypeLabel() {
        return btnCSTypeLabel;
    }

    /**
     * Returns the WebElement ddSelectionTitle .
     * 
     * @return ddSelectionTitle
     */
    public WebElement getDdSelectionTitle() {
        return ddSelectionTitle;
    }

    /**
     * Clicks on Selection title
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkDrpDwnSelectionTitle(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.scrollUpOrDownToElement(getDdSelectionTitle(),
                browserDriverInstance);
        getDdSelectionTitle().click();
        CSLogger.info("Clicked on Selection title");
    }

    /**
     * Returns the WebElement cbPdmarticleconfigurationLanguageDependent. The
     * webElement is a type of checkbox
     * 
     * @return cbPdmarticleconfigurationLanguageDependent CSGuiDialogContentId
     */
    public WebElement getCbPdmarticleconfigurationLanguageDependent() {
        return cbPdmarticleconfigurationLanguageDependent;
    }

    /**
     * Returns the WebElement cbmultipleSelection. The webElement is a type of
     * checkbox
     * 
     * @return cbmultipleSelection CSGuiDialogContentId
     */
    public WebElement getcbmultipleSelection() {
        return cbmultipleSelection;
    }

    /**
     * Returns the WebElement txtPdmarticleconfigurationName. The webElement is
     * a type of textbox
     * 
     * @return txtPdmarticleconfigurationName CSGuiDialogContentId
     */
    public WebElement getTxtPdmarticleconfigurationName() {
        return txtPdmarticleconfigurationName;
    }

    /**
     * Returns the WebElement ddPdmarticleconfigurationIsRequired. The
     * webElement is a type of Dropdown menu
     * 
     * @return ddPdmarticleconfigurationIsRequired CSGuiDialogContentId
     */
    public WebElement getDdPdmarticleconfigurationIsRequired() {
        return ddPdmarticleconfigurationIsRequired;
    }

    /**
     * Returns the WebElement txtPdmarticleconfigurationLabel.
     * 
     * @return txtPdmarticleconfigurationLabel CSGuiDialogContentId
     */
    public WebElement getTxtPdmarticleconfigurationLabel() {
        return txtPdmarticleconfigurationLabel;
    }

    /**
     * Returns the WebElement txtPdmarticleconfigurationGuiSortOrder .
     * 
     * @return txtPdmarticleconfigurationGuiSortOrder CSGuiDialogContentId
     */
    public WebElement getTxtPdmarticleconfigurationGuiSortOrder() {
        return txtPdmarticleconfigurationGuiSortOrder;
    }

    /**
     * Returns the WebElement ddPdmarticleconfigurationIsInherited .
     * 
     * @return ddPdmarticleconfigurationIsInherited CSGuiDialogContentId
     */
    public WebElement getDdPdmarticleconfigurationIsInherited() {
        return ddPdmarticleconfigurationIsInherited;
    }

    /**
     * Returns the WebElement ddPdmarticleconfigurationIsSearchable .
     * 
     * @return ddPdmarticleconfigurationIsSearchable CSGuiDialogContentId
     */
    public WebElement getDdPdmarticleconfigurationIsSearchable() {
        return ddPdmarticleconfigurationIsSearchable;
    }

    /**
     * Returns the WebElement txtPdmarticleconfigurationPaneTitle .
     * 
     * @return txtPdmarticleconfigurationPaneTitle CSGuiDialogContentId
     */
    public WebElement getTxtPdmarticleconfigurationPaneTitle() {
        return txtPdmarticleconfigurationPaneTitle;
    }

    /**
     * Returns the WebElement cbPdmarticleconfigurationIsCategoryClass .
     * 
     * @return cbPdmarticleconfigurationIsCategoryClass CSGuiDialogContentId
     */
    public WebElement getcbPdmarticleconfigurationIsCategoryClass() {
        return cbPdmarticleconfigurationIsCategoryClass;
    }

    /**
     * Returns the WebElement pdmArticleStructureClassMappingCsReferenceDiv
     * 
     * @return pdmArticleStructureClassMappingCsReferenceDiv
     *         CSGuiDialogContentId
     */
    public WebElement getPdmArticleStructureClassMappingCsReferenceDiv() {
        return pdmArticleStructureClassMappingCsReferenceDiv;
    }

    /**
     * Returns the WebElement which contains name of the class attached to view
     * 
     * @return lblViewAssignedClassName
     */
    public WebElement getlblViewAssignedClassName() {
        return lblViewAssignedClassName;
    }

    /**
     * Returns the WebElement lblExternalKey
     * 
     * @return lblExternalKey
     */
    public WebElement getLblExternalKey() {
        return lblExternalKey;
    }

    /**
     * Returns the WebElement txtProductLabel
     * 
     * @return txtProductLabel
     */
    public WebElement getTxtProductLabel() {
        return txtProductLabel;
    }

    /**
     * Returns the instance of Pane Title
     * 
     * @return txtPaneTitle
     */
    public WebElement getTxtPaneTitle() {
        return txtPaneTitle;
    }

    /**
     * Returns the instance of Drop down Mapping Facetted Concatenator
     * 
     * @return drpdwnMappingFacetedConcatenator
     */
    public WebElement getDrpDwnMappingFacetedConcatenator() {
        return drpdwnMappingFacetedConcatenator;
    }

    /**
     * Returns the instance of Drop down Mapping Facetted Search
     * 
     * @return drpdwnMappingFacettedSearch
     */
    public WebElement getDrpDwnMappingFacetedSearch() {
        return drpdwnMappingFacetedSearch;
    }

    /**
     * Returns the instance of Section Title
     * 
     * @return txtSectionTitle
     */
    public WebElement getTxtSectionTitle() {
        return txtSectionTitle;
    }

    /**
     * Checks the Language Dependent attribute filed by clicking on it.
     * 
     * @param waitForReload
     */
    public void checkCbPdmarticleconfigurationLanguageDependent(
            WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getCbPdmarticleconfigurationLanguageDependent()));
        getCbPdmarticleconfigurationLanguageDependent().click();
        CSLogger.info("Checked language Dependent filed");
    }

    /**
     * Returns the WebElement for tab Data
     * 
     * @return tabData
     */
    public WebElement getTabData() {
        return tabData;
    }

    /**
     * Returns the WebElement txtPdmarticleconfigurationSectionTitle.
     * 
     * @return txtPdmarticleconfigurationSectionTitle
     */
    public WebElement getTxtPdmarticleconfigurationSectionTitle() {
        return txtPdmarticleconfigurationSectionTitle;
    }

    /**
     * Returns the WebElement ddSearchInputSelect
     * 
     * @return ddSearchInputSelect
     */
    public WebElement getDdSearchInputAttributeSelect() {
        return ddSearchInputAttributeSelect;
    }

    /**
     * Returns the WebElement ddSearchInputSelect
     * 
     * @return ddSearchInputSelect
     */
    public WebElement getDdSearchInputOperatorSelect() {
        return ddSearchInputOperatorSelect;
    }

    /**
     * Returns the WebElement txtAdvanceSearchInputText
     * 
     * @return txtAdvanceSearchInputText
     */
    public WebElement getTxtAdvanceSearchInputText() {
        return txtAdvanceSearchInputText;
    }

    /**
     * This method returns the instance of button Recursive
     * 
     * @return btnRecursive
     */
    public WebElement getBtnRecursive() {
        return btnRecursive;
    }

    /**
     * This method returns the instance of button Now
     * 
     * @return btnNow
     */
    public WebElement getBtnNow() {
        return btnNow;
    }

    /**
     * This method returns the instance of drop down Unit
     * 
     * @return drpdwnUnit
     */
    public WebElement getDrpDwnUnit() {
        return drpdwnUnit;
    }

    /**
     * This method returns the instance of text area unit
     * 
     * @return txtUnit
     */
    public WebElement getTxtUnit() {
        return txtUnit;
    }

    /**
     * Returns the instance tab Attribute Mapping
     * 
     * @return tabAttributeMapping
     */
    public WebElement getTabAttributeMapping() {
        return tabAttributeMapping;
    }

    /**
     * Returns the instance of Drop down Facet Concatenator
     * 
     * @return drpdwnFacetConcatenator
     */
    public WebElement getDrpDwnFacetConcatenator() {
        return drpdwnFacetConcatenator;
    }

    /**
     * Returns the instance of Drop down faceted Search
     * 
     * @return drpdwnFacetedSearch
     */
    public WebElement getDrpDwnFacetedSearch() {
        return drpdwnFacetedSearch;
    }

    /**
     * Returns the instance of Sort Order
     * 
     * @return txtSortOrder
     */
    public WebElement getTxtSortOrder() {
        return txtSortOrder;
    }

    /**
     * Returns the instance of Facet Label
     * 
     * @return txtFacetLabel
     */
    public WebElement getTxtFacetLabel() {
        return txtFacetLabel;
    }

    /**
     * Returns the instance of drop down Value Range
     * 
     * @return drpdwnValueRange
     */
    public WebElement getDrpDwnValueRange() {
        return drpdwnValueRange;
    }

    /**
     * Checks the Show In Category Tree attribute filed by clicking on it.
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void checkCbPdmarticleconfigurationIsCategoryClass(
            WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getcbPdmarticleconfigurationIsCategoryClass()));
        if (getcbPdmarticleconfigurationIsCategoryClass().getAttribute("class")
                .contains("Off")) {
            CSUtility.tempMethodForThreadSleep(1000);
            getcbPdmarticleconfigurationIsCategoryClass().click();
            CSLogger.info("Checked  show category tree field");
        } else {
            CSLogger.info("Already checked  show category tree field");
        }
    }

    /**
     * Unchecks the Show In Category Tree attribute filed by clicking on it
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void uncheckCbPdmarticleconfigurationIsCategoryClass(
            WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(getcbPdmarticleconfigurationIsCategoryClass()));
        if (getcbPdmarticleconfigurationIsCategoryClass().getAttribute("class")
                .contains("On")) {
            CSUtility.tempMethodForThreadSleep(1000);
            getcbPdmarticleconfigurationIsCategoryClass().click();
            CSLogger.info("Unchecked  show category tree field");
        } else {
            CSLogger.info("Already Unchecked  show category tree field");
        }
    }

    /**
     * Enters data in any Text Attribute filed
     * 
     * @param waitForReload this is webDriverWait object
     * @param attributeFiledElement type of webElement
     * @param data eg name of attribute
     */
    public void enterDataForTextAttributeFiled(WebDriverWait waitForReload,
            WebElement attributeFiledElement, String data) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                attributeFiledElement);
        attributeFiledElement.click();
        attributeFiledElement.clear();
        attributeFiledElement.sendKeys(data);
        CSLogger.info("Entered Data for Attribute Field Name");
    }

    /**
     * Clicks on Attribute Type field for selecting any attribute type
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnCSTypeLabel(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnCSTypeLabel());
        CSLogger.info("Waiting Completed for cstypelabel");
        getBtnCSTypeLabel().click();
        CSLogger.info("clicked on cstypelabel");
    }

    /**
     * Enters name for Technical Attribute
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkTxtPdmarticleconfigurationName(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getTxtPdmarticleconfigurationName());
        CSLogger.info("Waiting completed for technical attribute name");
    }

    /**
     * Checks Multiple Selection Filed by clicking on it.
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void checkCbMultipleSelection(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getcbmultipleSelection());
        getcbmultipleSelection().click();
        CSLogger.info("checked Multiple selection filed");
    }

    /**
     * Selects the drop down element.
     * 
     * @param drpdwnElement this is type of drop down webElement
     * @param valueOfDrpDwn text value in drop down webElement
     */
    public void selectValueFromDrpDwnElement(WebElement drpdwnElement,
            String valueOfDrpDwn) {
        drpdwnElement.click();
        Select element = new Select(drpdwnElement);
        element.selectByVisibleText(valueOfDrpDwn);
        CSLogger.info("Drop down option " + valueOfDrpDwn + " selected");
    }

    /**
     * Clicks on properties button
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnProperties(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload, getTabProperties());
        getTabProperties().click();
        CSLogger.info("Clicked on properties tab");
    }

    /**
     * Clicks on + for adding class.
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void
            clkPdmArticleClassMappingCsReference(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getPdmArticleClassMappingCsReference());
        getPdmArticleClassMappingCsReference().click();
    }

    /**
     * This method clicks on + while selecting or adding classes for view
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkPdmArticleStructureClassMappingCsReferenceDiv(
            WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getPdmArticleStructureClassMappingCsReferenceDiv());
        getPdmArticleStructureClassMappingCsReferenceDiv().click();
        CSLogger.info("clicks on + while  adding classes for view");
    }

    /**
     * Gets the linktext i.e name of class that is assigned in the view
     * 
     * @param waitForReload this is webDriverWait object
     * @return assigned class name's text
     */
    public String
            getTxtOflblViewAssignedClassName(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getlblViewAssignedClassName());
        return getlblViewAssignedClassName().getText();
    }

    /**
     * Clicks on Data Tab
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkBtnData(WebDriverWait waitForReload) {
        CSUtility.traverseToSettingsConfiguration(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload, getTabData());
        getTabData().click();
        CSLogger.info("Clicked on data tab ");
    }

    /**
     * Clicks on drop down of attribute select in advance search options
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkOnDrpDwnInputAttributeSelect(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getDdSearchInputAttributeSelect()));
        getDdSearchInputAttributeSelect().click();
        CSLogger.info(
                "Clicked on  search bar's input attribute select element");
    }

    /**
     * Clicks on drop down of operator select in advance search options
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkOnDrpDwnInputOperatorSelect(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getDdSearchInputOperatorSelect()));
        getDdSearchInputOperatorSelect().click();
        CSLogger.info("Clicked on  search bar's input operator select element");
    }

    /**
     * Clicks on product label
     * 
     * @param waitForReload
     */
    public void clkOnProductLabel(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getTxtProductLabel());
        waitForReload.until(
                ExpectedConditions.elementToBeClickable(getTxtProductLabel()));
        getTxtProductLabel().click();
        CSLogger.info("Clicked on product label");
    }

    /**
     * Clicks on button Class Table
     * 
     * @param waitForReload
     */
    public void clkOnClassTable(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnClassTable());
        getBtnClassTable().click();
        CSLogger.info("Clicked on class table to add list");
    }

    /**
     * Clicks on button Recursive
     * 
     * @param waitForReload
     */
    public void clkOnBtnRecursive(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnRecursive());
        getBtnRecursive().click();
        CSLogger.info("Clicked on Recursive");
    }

    /**
     * Clicks on button Now
     * 
     * @param waitForReload
     */
    public void clkOnBtnNow(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnNow());
        getBtnNow().click();
        CSLogger.info("Clicked on Now");
    }

    /**
     * This method will return the button to add reference to pim product
     * 
     * @return btnReferenceToPimProduct
     */
    public WebElement getBtnReferenceToPimProduct() {
        return btnReferenceToPimProduct;
    }

    /**
     * This generic method will click on any web element passed as an argument
     * 
     * @param locator - WebElement instance - locator of element on which we
     *            want to perform click action
     */
    public void clkWebElement(WebDriverWait waitForReload, WebElement locator) {
        CSUtility.waitForVisibilityOfElement(waitForReload, locator);
        locator.click();
    }
    
    /**
     * Enters data in any Text Attribute filed
     * 
     * @param waitForReload this is webDriverWait object
     * @param attributeFiledElement type of webElement
     * @param data eg name of attribute
     */
    public void enterDataForTextAttributeField(WebDriverWait waitForReload,
            WebElement attributeFiledElement, String data) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                attributeFiledElement);
        attributeFiledElement.click();
        attributeFiledElement.clear();
        attributeFiledElement.sendKeys(data);
        CSLogger.info("Entered Data for Attribute Field Name");
    }
}
