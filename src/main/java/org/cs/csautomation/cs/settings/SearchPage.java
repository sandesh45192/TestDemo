/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This is the page class for open search module
 * 
 * @author CSAutomation Team
 *
 */
public class SearchPage {

    private WebDriver browserDriverInstance;

    /**
     * Parameterized constructor
     * 
     * @param paramDriver - WebDriver object
     */
    public SearchPage(WebDriver paramDriver) {
        PageFactory.initElements(paramDriver, this);
        new FrameLocators(browserDriverInstance);
        CSLogger.info(
                "Initialized SettingsLeftPaneTree SearchPage POM elements");
    }

    @FindBy(id = "Search@0")
    private WebElement searchNode;

    @FindBy(xpath = "//span[contains(text(),'System Preferences')]")
    private WebElement systemPreferences;

    @FindBy(id = "Search~Search Portal@0")
    private WebElement searchPortalNode;

    @FindBy(id = "Search~Search Portal~Shares@0")
    private WebElement btnShares;

    @FindBy(xpath = "//nobr[contains(text(),'Properties')]")
    private WebElement tabProperties;

    @FindBy(xpath = "//nobr[contains(text(),'Activity Log')]")
    private WebElement btnActivityLog;

    @FindBy(xpath = "//div[contains(text(),'General')]")
    private WebElement secGeneral;

    @FindBy(xpath = "//div[contains(text(),' Search settings')]")
    private WebElement secSearchSettings;

    @FindBy(xpath = "//div[contains(text(),'Data Filter')]")
    private WebElement secDataFilter;

    @FindBy(xpath = "//div[contains(text(),' List attributes')]")
    private WebElement secListAttributes;

    @FindBy(xpath = "//div[contains(text(),' Preview Attributes')]")
    private WebElement secPreviewAttributes;

    @FindBy(xpath = "//div[contains(text(),' Actions')]")
    private WebElement secActions;

    @FindBy(xpath = "//div[contains(text(),'Search Plugin')]")
    private WebElement txtSearchPlugin;

    @FindBy(xpath = "//div[contains(text(),'Search Area')]")
    private WebElement txtSearchArea;

    @FindBy(xpath = "//div[contains(text(),'Share from')]")
    private WebElement calendarShareFrom;

    @FindBy(xpath = "//div[contains(text(),'Share until')]")
    private WebElement calendarShareUntil;

    @FindBy(xpath = "//div[contains(text(),'Allow Access by')]")
    private WebElement drpDwnAllowAccessBy;

    @FindBy(xpath = "//div[contains(text(),'Shared by')]")
    private WebElement sharedByNode;

    @FindBy(xpath = "//div[contains(text(),'Owner')]")
    private WebElement ownerNode;

    @FindBy(xpath = "//div[contains(text(),'Run as User')]")
    private WebElement runAsUserNode;

    @FindBy(xpath = "//div[contains(text(),'Deactivated')]")
    private WebElement chkboxTextDeactivated;

    @FindBy(xpath = "//div[contains(text(),'Hide in service portal')]")
    private WebElement chkboxTextHideInOpenSearch;

    @FindBy(xpath = "//div[contains(text(),'OpenKey')]")
    private WebElement txtOpenKey;

    @FindBy(xpath = "//input[contains(@id,'OpensearchareaLabel')]")
    private WebElement lblOpenSearchArea;

    @FindBy(xpath = "//select[contains(@id,'OpensearchareaSearchAreaPlugin')]")
    private WebElement drpDwnSearchPlugin;

    @FindBy(xpath = "//option[contains(text(),'Products')]")
    private WebElement productsSearchPlugin;

    @FindBy(xpath = "//option[contains(text(),'Selection of Parent Nodes')]")
    private WebElement optionSelectionOfParentNodes;

    @FindBy(
            xpath = "//div[contains(@id,'OpensearchareaRootIDs_csReferenceDiv')"
                    + "]//img[@class='CSGuiSelectionListAdd']")
    private WebElement imgToAddElement;

    @FindBy(xpath = "//input[@value='Yes']")
    private WebElement btnYes;

    @FindBy(xpath = "//div[@id='Tile Setting_sections::General']")
    private WebElement tabGeneral;

    @FindBy(xpath = "//div[@id='Tile Setting_sections::Data Filter']")
    private WebElement tabDataFilter;

    @FindBy(id = "Search~Search Portal~Collections@0")
    private WebElement btnCollections;

    @FindBy(id = "AttributeRow_Url")
    private WebElement urlRow;

    @FindBy(xpath = "//span[contains(@id,'OpensearchareaUrl')]/a")
    private WebElement txtUrl;

    @FindBy(xpath = "//div[contains(text(),'Selected Items')]")
    private WebElement txtSelectedItems;

    @FindBy(xpath = "//div[@id='title_Tile Setting_sections::General']")
    private WebElement tabGeneralInCollections;

    @FindBy(xpath = "//div[contains(text(),'Actions')]")
    private WebElement sectionActions;

    @FindBy(xpath = "//div[contains(text(),'E-Mail')]")
    private WebElement sectionEmail;

    @FindBy(xpath = "//nobr[contains(text(),'Product Configuration')]")
    private WebElement tabProductConfiguration;

    @FindBy(xpath = "//nobr[contains(text(),'Channel Configuration')]")
    private WebElement tabChannelConfiguration;

    @FindBy(xpath = "//nobr[contains(text(),'File Configuration')]")
    private WebElement tabFileConfiguration;

    @FindBy(xpath = "//nobr[contains(text(),'Activity Log')]")
    private WebElement tabActivityLog;

    @FindBy(xpath = "//option[contains(text(),'Favorites')]")
    private WebElement searchPluginFavorites;

    @FindBy(xpath = "//div[@id='title_Data Filter_sections::General']")
    private WebElement tabGeneralInFavorites;

    @FindBy(xpath = "//div[@id='Data Filter_sections::Data Filter']")
    private WebElement tabDataFilterInFavorites;

    @FindBy(xpath = "//img[@class='CSGuiSelectionListAdd']")
    private WebElement imgAddFavorites;

    @FindBy(xpath = "//td[contains(text(),'People')]")
    private WebElement favLblPeople;

    @FindBy(xpath = "//span[@id='Search~Search Portal~Settings@0']")
    private WebElement searchPortalSettingsNode;

    @FindBy(xpath = "//span[contains(text(),'CORE')]")
    private WebElement coreNode;

    @FindBy(xpath = "//span[contains(text(),'Search Portal')]")
    private WebElement shareSearchPortal;

    @FindBy(
            xpath = "//div[contains(@name,'LandingPageLogo')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement imgAddlandingPageLogo;

    @FindBy(
            xpath = "//div[@class='ControlPaneButtonArea']//div[2]//table[1]//tbody[1]//tr[1]//td[1]")
    private WebElement folderFileFolders;

    @FindBy(xpath = "//span[contains(text(),'Demo')]")
    private WebElement dataFolderDemo;

    @FindBy(xpath = "//span[contains(text(),'Skin')]")
    private WebElement dataFolderSkin;

    @FindBy(xpath = "//a[contains(text(),'contentserv')]")
    private WebElement contentservLogo;

    @FindBy(xpath = "//div[@class='CSPortalWindow']")
    private WebElement csPortalWindow;

    @FindBy(
            xpath = "//div[@class='ControlPaneButtonArea']//div[1]//table[1]//tbody[1]//tr[1]//td[1]")
    private WebElement btnProductsFolder;

    @FindBy(
            xpath = "//div[@class='ControlPaneButtonArea']//div[2]//table[1]//tbody[1]//tr[1]//td[1]")
    private WebElement btnFileFolder;

    @FindBy(
            xpath = "//div[@class='ControlPaneButtonArea']//div[3]//table[1]//tbody[1]//tr[1]//td[1]")
    private WebElement btnChannelsFolder;

    @FindBy(xpath = "//button[@name='collectionButtonPopup']")
    private WebElement btnCollectionPopup;

    @FindBy(xpath = "//a[contains(text(),'People')]")
    private WebElement popupCollectionPeople;

    @FindBy(
            xpath = "//div[@class='SearchResultsMetaText ListViewMetaText']/span")
    private WebElement resultsMetaText;

    @FindBy(xpath = "//a[contains(text(),'Federated Search Connector')]")
    private WebElement optionFederatedSearchConnector;

    @FindBy(xpath = "//a[contains(text(),'Configure Search')]")
    private WebElement optionConfigureSearch;

    @FindBy(xpath = "// a[contains(text(),'Microsoft Sharepoint Connector')]")
    private WebElement optionMicrosoftSharepointConnector;

    @FindBy(xpath = "// a[contains(text(),'SAP NetWeaver Connector')]")
    private WebElement optionSapNetweaverConnector;

    @FindBy(xpath = "// a[contains(text(),'Atlassian Confluence Connector')]")
    private WebElement optionAtlassianConfluenceConnector;

    @FindBy(xpath = "// a[contains(text(),'CONTENTSERV Connector')]")
    private WebElement optionContentservConnector;

    @FindBy(xpath = "// a[contains(text(),'Configure Shares')]")
    private WebElement optionConfigureShares;

    @FindBy(xpath = "// a[contains(text(),'Configure Collections')]")
    private WebElement optionConfigureCollections;

    @FindBy(xpath = "// a[contains(text(),'Search Help')]")
    private WebElement optionSearchHelp;

    @FindBy(xpath = "//button[@name='optionButtonPopup']")
    private WebElement btnOptions;

    @FindBy(xpath = "//input[@placeholder='Search']")
    private WebElement txtBoxSearchField;

    @FindBy(xpath = "//div[@class='searchAreaContainer']/div/div")
    private WebElement searchComboBox;

    @FindBy(xpath = "//div[@class='ms-sel-ctn']/input")
    private WebElement txtBoxSearchComboBox;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxyopensearch:SearchInEverywhere_GUI')]")
    private WebElement chkboxEverywhereSearch;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxyopensearch:SearchSuggestionForEverywhere_GUI')]")
    private WebElement chkboxShowSearchSuggestions;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxyopensearch:SearchInAllShares_GUI')]")
    private WebElement chkboxAllSharesSearch;

    @FindBy(xpath = "//div[@class='ms-trigger']")
    private WebElement drpDwnComboBox;

    @FindBy(xpath = "//div[contains(text(),'Everywhere')]")
    private WebElement drpDwnEverywhere;

    @FindBy(xpath = "//div[contains(text(),'Collections')]")
    private WebElement drpDwnCollections;

    @FindBy(xpath = "//div[contains(text(),'Shares')]")
    private WebElement drpDwnShares;

    @FindBy(xpath = "//div[contains(text(),'Favorites')]")
    private WebElement drpDwnFavorites;

    @FindBy(
            xpath = "//div[contains(@data-json,'{\"id\":\"loadmore:collection\"')]")
    private WebElement drpDwnMoreForCollections;

    @FindBy(xpath = "//div[contains(@data-json,'{\"id\":\"loadmore:share\"')]")
    private WebElement drpDwnMoreForShares;

    @FindBy(
            xpath = "//div[contains(@data-json,'{\"id\":\"loadmore:favorite\"')]")
    private WebElement drpDwnMoreForFavorites;

    @FindBy(xpath = "//div[contains(@class,'ms-sel-item')]")
    private WebElement searchComboBoxDefaultText;

    @FindBy(
            xpath = "//button[@class='searchButton CSButtonView btn btn-default btn-sm']")
    private WebElement btnSearch;

    @FindBy(
            xpath = "//button[@name='SelectModeButtonPopup']//span[@class='button-icon left']//span")
    private WebElement btnSelectMode;

    @FindBy(xpath = "//li[@class='CSListViewItem']/div[1]/div[2]/div")
    private WebElement chkboxSelectionMode;

    @FindBy(
            xpath = "//li[@class='CSListViewItem withAccessoryView']/div[1]/div[2]/div")
    private WebElement chkboxSelectionModeForCollection;

    @FindBy(
            xpath = "//li[@class='CSListViewItem']/div[1]/div[3]/div[1]/div/div[2]")
    private WebElement linkIconSelectionMode;

    @FindBy(
            xpath = "//li[@class='CSListViewItem withAccessoryView']/div[1]/div[3]/div[1]/div/div[2]")
    private WebElement linkIconSelectionModeForCollection;

    @FindBy(xpath = "//div[contains(text(),'All Shares')]")
    private WebElement drpDwnAllShares;

    @FindBy(xpath = "//span[contains(@id,'OpensearchareaOpenKeyDisplay')]")
    private WebElement lblOpenKeyValue;

    @FindBy(xpath = "//span[contains(@id,'OpensearchareaViewType_GUI')]")
    private WebElement chkboxHideInOpenSearch;

    @FindBy(xpath = "//span[contains(@id,'OpensearchareaDisabled_GUI')]")
    private WebElement chkboxDeactivated;

    @FindBy(xpath = "//a[contains(text(),'Add to Collection')]")
    private WebElement drpDwnAddToCollection;

    @FindBy(
            xpath = "//select[@class='form-control btn btn-default CSSelectView']")
    private WebElement drpDwnListForExistingCollections;

    @FindBy(xpath = "//td[contains(text(),'Delete')]")
    private WebElement ctxDelete;

    @FindBy(xpath = "//div[@id='CSPopupDiv']")
    private WebElement csPopupDiv;

    @FindBy(xpath = "//input[@id='Title']")
    private WebElement txtBoxAttributeName;

    @FindBy(xpath = "//input[@id='newAttributeLabel']")
    private WebElement txtBoxAttributeType;

    @FindBy(xpath = "//td[contains(text(),'Cross Reference')]")
    private WebElement drpDwnCrossReference;

    @FindBy(xpath = "//a[contains(text(),'Reference to PIM Product')]")
    private WebElement drpDwnReferenceToPimProduct;

    @FindBy(
            xpath = "//div[contains(@id,'Optionproxyopensearch:ReferenceAttributes_csReferenceDiv')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement imgAddRefAttrToSearchPortal;

    @FindBy(xpath = "//span[@id='Configurations@0']")
    private WebElement txtAttributesFolder;

    @FindBy(
            xpath = "//div[contains(@id,'Opensearcharea_TeaserInfoIDsPdmarticle_csReferenceDiv')]//img[@class='CSGuiSelectionListAdd']")
    private WebElement imgToAddAttrToCollection;

    @FindBy(xpath = "//a[@class='previewLink View']")
    private WebElement linkViewCollection;

    @FindBy(xpath = "//a[@class='previewLink Edit']")
    private WebElement linkEditCollection;

    @FindBy(xpath = "//a[@class='previewLink Download']")
    private WebElement linkDownloadCollection;

    @FindBy(xpath = "//input[@id='CSBuilderMarkAllActionTop']")
    private WebElement chkboxSelectAllProductsFromFolder;

    @FindBy(xpath = "//td[contains(text(),'Favorites')]")
    private WebElement drpDwnFavoritesInPimShowMore;

    @FindBy(xpath = "//td[contains(text(),'Add/Update favorite')]")
    private WebElement drpDwnAddFavorite;

    @FindBy(xpath = "//input[contains(@id,'RecordBookmarkName')]")
    private WebElement lblFavoriteProduct;

    @FindBy(xpath = "//input[@class='CSGUI_MODALDIALOG_INPUT_BUTTON_OK']")
    private WebElement btnOkOpenSearch;

    @FindBy(
            xpath = "//a[@id='toolbar_button_save']//img[@class='CSGuiToolbarButtonImage']")
    private WebElement btnSaveOpenSearch;

    @FindBy(
            xpath = "//td[@class='CSGuiThumbListImage CSGuiThumbListImageSingle']/div/a/div/img")
    private WebElement imgAssignedLandingPageLogo;

    @FindBy(xpath = "//img[@class='searchLandingPageLogo']")
    private WebElement imgLandingPageLogo;

    @FindBy(xpath = "//span[contains(text(),'browse files')]")
    private WebElement txtBrowseFiles;

    @FindBy(xpath = "//input[@id='Filter_Item_FileName']")
    private WebElement txtBoxFilterByFileName;

    @FindBy(xpath = "//span[contains(@id,'OpensearchareaShowFolders_GUI')]")
    private WebElement chkboxShowFolders;

    @FindBy(xpath = "//select[contains(@id,'OpensearchareaSelectionType')]")
    private WebElement drpDwnSelectionType;

    @FindBy(xpath = "//a[contains(text(),'Configure Shares')]")
    private WebElement btnConfigureShares;

    @FindBy(xpath = "//h3[contains(text(),'Categories')]")
    private WebElement secCategories;

    @FindBy(
            xpath = "//div[contains(@class,'ajs-message ajs-info ajs-visible')]")
    private WebElement popUpMessageInfoBox;

    @FindBy(xpath = "//div[@id='title_Data Filter_sections::Data Filter']")
    private WebElement secDataFilterFavorites;

    /**
     * Returns the instance of data filter favorites
     * 
     * @return secDataFilterFavorites
     */
    public WebElement getSecDataFilterFavorites() {
        return secDataFilterFavorites;
    }

    /**
     * This method returns the element for Search drop-down list
     * 
     * @return - searchNode
     */
    public WebElement getSearchNode() {
        return searchNode;
    }

    /**
     * This method returns the element for tab General
     * 
     * @return - tabGeneral
     */
    public WebElement getTabGeneral() {
        return tabGeneral;
    }

    /**
     * This method returns the element for tab DataFilter
     * 
     * @return - tabDataFilter
     */
    public WebElement getTabDataFilter() {
        return tabDataFilter;
    }

    /**
     * This method will return the button for option 'Yes'
     * 
     * @return - button for option 'yes'
     */
    public WebElement getBtnYes() {
        return btnYes;
    }

    /**
     * This method will return element to add root folder
     * 
     * @return imgAddRootFolder
     */
    public WebElement addElement() {
        return imgToAddElement;
    }

    /**
     * This method returns the element for system preferences
     * 
     * @return - system preferences menu
     */
    public WebElement getSystemPreferences() {
        return systemPreferences;
    }

    /**
     * This method returns the option 'Search Portal'
     * 
     * @return - btnSearchPortal
     */
    public WebElement getNodeSearchPortal() {
        return searchPortalNode;
    }

    /**
     * This method returns the option 'Share'
     * 
     * @return - btnShares
     */
    public WebElement getBtnShares() {
        return btnShares;
    }

    /**
     * This method returns the element for properties tab
     * 
     * @return btnProperties
     */
    public WebElement getTabProperties() {
        return tabProperties;
    }

    /**
     * This method returns the data member of button for activity log
     * 
     * @return btnActivityLog
     */
    public WebElement getBtnActivityLog() {
        return btnActivityLog;
    }

    /**
     * The method will return section 'General'
     * 
     * @return btnGeneral
     */
    public WebElement getSecGeneral() {
        return secGeneral;
    }

    /**
     * This method returns the element for search settings
     * 
     * @return btnSearchSettings
     */
    public WebElement getSecSearchSettings() {
        return secSearchSettings;
    }

    /**
     * This method returns the element for data filter
     * 
     * @return btnDataFilter
     */
    public WebElement getSecDataFilter() {
        return secDataFilter;
    }

    /**
     * This method returns the element for list attributes
     * 
     * @return btnListAttributes
     */
    public WebElement getSecListAttributes() {
        return secListAttributes;
    }

    /**
     * This method returns the element for preview attributes
     * 
     * @return btnPreviewAttributes
     */
    public WebElement getSecPreviewAttributes() {
        return secPreviewAttributes;
    }

    /**
     * This method returns the element for button actions
     * 
     * @return btnActions
     */
    public WebElement getSecActions() {
        return secActions;
    }

    /**
     * This method returns the element for search area text box
     * 
     * @return txtSearchArea
     */
    public WebElement getTxtSearchArea() {
        return txtSearchArea;
    }

    /**
     * This method returns the element for search plugin text box
     * 
     * @return txtSearchPlugin
     */
    public WebElement getTxtSearchPlugin() {
        return txtSearchPlugin;
    }

    /**
     * This method returns the element for calendar search from
     * 
     * @return calendarShareFrom
     */
    public WebElement getCalShareFrom() {
        return calendarShareFrom;
    }

    /**
     * This method returns the element for calendar search until
     * 
     * @return calendarShareUntil
     */
    public WebElement getCalShareUntil() {
        return calendarShareUntil;
    }

    /**
     * This method returns the element for drop down 'allow access by'
     * 
     * @return drpDwnAllowAccessBy
     */
    public WebElement getDrpDwnAllowAccessBy() {
        return drpDwnAllowAccessBy;
    }

    /**
     * This method returns the element for 'shared by' node
     * 
     * @return sharedByNode
     */
    public WebElement getSharedByNode() {
        return sharedByNode;
    }

    /**
     * This method returns the element for 'owner' node
     * 
     * @return ownerNode
     */
    public WebElement getOwnerNode() {
        return ownerNode;
    }

    /**
     * This method returns the element for 'run as user' node
     * 
     * @return runAsUserNode
     */
    public WebElement getRunAsUserNode() {
        return runAsUserNode;
    }

    /**
     * This method returns the element for 'Deactivated' toggle button
     * 
     * @return chkboxTextDeactivated
     */
    public WebElement getChkboxTextDeactivated() {
        return chkboxTextDeactivated;
    }

    /**
     * This method returns the element for 'HideInOpenSearch' toggle button
     * 
     * @return chkboxTextHideInOpenSearch
     */
    public WebElement getChkboxTextHideInOpenSearch() {
        return chkboxTextHideInOpenSearch;
    }

    /**
     * This method returns the element for 'search area' textbox
     * 
     * @return lblOpenSearchArea
     */
    public WebElement getLblOpenSearchArea() {
        return lblOpenSearchArea;
    }

    /**
     * This method returns the element for 'Search plugin' dropdown
     * 
     * @return - drop down for 'search plugin' label
     */
    public WebElement getDrpDwnSearchPlugin() {
        return drpDwnSearchPlugin;
    }

    /**
     * This method returns the element for search plugin
     * 
     * @return - option 'products' plugin
     */
    public WebElement getProductsPlugin() {
        return productsSearchPlugin;
    }

    /**
     * This method returns the element for option SeletionOfParentNodes
     * 
     * @return optionSelectionOfParentNodes
     */
    public WebElement getOptionSeletionOfParentNodes() {
        return optionSelectionOfParentNodes;
    }

    /**
     * This method returns the option 'Collections'
     * 
     * @return - btnShares
     */
    public WebElement getBtnCollections() {
        return btnCollections;
    }

    /**
     * This method returns the open key label
     * 
     * @return openKey
     */
    public WebElement getSectionOpenKey() {
        return txtOpenKey;
    }

    /**
     * This method returns the URL row
     * 
     * @return urlRow
     */
    public WebElement getUrlRow() {
        return urlRow;
    }

    /**
     * This method returns the url
     * 
     * @return url
     */
    public WebElement getUrl() {
        return txtUrl;
    }

    /**
     * This method returns row for selected items
     * 
     * @return selectedItems
     */
    public WebElement getSelectedItems() {
        return txtSelectedItems;
    }

    /**
     * This method returns the element for tab General
     * 
     * @return - tabGeneral
     */
    public WebElement getTabGeneralInCollections() {
        return tabGeneralInCollections;
    }

    /**
     * This method will return section 'Actions'
     * 
     * @return sectionActions
     */
    public WebElement getSectionActions() {
        return sectionActions;
    }

    /**
     * This method will return section 'Email'
     * 
     * @return sectionEmail
     */
    public WebElement getSectionEmail() {
        return sectionEmail;
    }

    /**
     * This method will return tab 'product configuration'
     * 
     * @return tabProductConfiguration
     */
    public WebElement getTabProductConfiguration() {
        return tabProductConfiguration;
    }

    /**
     * This method will return tab 'channel configuration'
     * 
     * @return tabChannelConfiguration
     */
    public WebElement getTabChannelConfiguration() {
        return tabChannelConfiguration;
    }

    /**
     * This method will return tab 'file configuration'
     * 
     * @return tabFileConfiguration
     */
    public WebElement getTabFileConfiguration() {
        return tabFileConfiguration;
    }

    /**
     * This method will return tab 'Activity log'
     * 
     * @return tabActivityLog
     */
    public WebElement getTabActivityLog() {
        return tabActivityLog;
    }

    /**
     * This method will return the search plugin 'Favorites'
     * 
     * @return SearchPluginFavorites
     */
    public WebElement getSearchPluginFavorites() {
        return searchPluginFavorites;
    }

    /**
     * This method returns the element for tab General, for search plugin
     * 'favorites'
     * 
     * @return - tabGeneralInFavorites
     */
    public WebElement getTabGeneralInFavorites() {
        return tabGeneralInFavorites;
    }

    /**
     * This method returns the tab data filter for favorites
     * 
     * @return tabDataFilterInFavorite
     */
    public WebElement getTabDataFilterInFavorites() {
        return tabDataFilterInFavorites;
    }

    /**
     * This method returns the symbol 'plus', to add favorites
     * 
     * @return imgAddFavorites
     */
    public WebElement getImgAddFavorites() {
        return imgAddFavorites;
    }

    /**
     * This method will return module people from Favorites folder
     * 
     * @return favLabelPeople
     */
    public WebElement getFavLabelPeople() {
        return favLblPeople;
    }

    /**
     * This method will return settings node of search portal
     * 
     * @return searchPortalSettingsNode
     */
    public WebElement getSearchPortalSettingsNode() {
        return searchPortalSettingsNode;
    }

    /**
     * This method will return node core from search portal-settings
     * 
     * @return coreNode
     */
    public WebElement getCoreNode() {
        return coreNode;
    }

    /**
     * This method will return share 'search portal'
     * 
     * @return shareSearchPortal;
     */
    public WebElement getShareSearchPortal() {
        return shareSearchPortal;
    }

    /**
     * This method returns '+' icon to add landing page logo
     * 
     * @return imgAddlandingPageLogo
     */
    public WebElement getImgToAddLandingPageLogo() {
        return imgAddlandingPageLogo;
    }

    /**
     * This method will return folder 'File Folders'
     * 
     * @return folderFileFolders
     */
    public WebElement getFolderFileFolders() {
        return folderFileFolders;
    }

    /**
     * This method will return demo folder
     * 
     * @return demoFolder
     */
    public WebElement getDataFolderDemo() {
        return dataFolderDemo;
    }

    /**
     * This method will return skin folder
     * 
     * @return skinFolder
     */
    public WebElement getDataFolderSkin() {
        return dataFolderSkin;
    }

    /**
     * This method will return contentserv logo
     * 
     * @return contentservLogo
     */
    public WebElement getContentservLogo() {
        return contentservLogo;
    }

    /**
     * This method will return CS portal window
     * 
     * @return csPortalWindow
     */
    public WebElement getCsPortalWindow() {
        return csPortalWindow;
    }

    /**
     * This methods returns the element for products folder
     * 
     * @return btnProductsFolder
     */
    public WebElement getBtnProductsFolder() {
        return btnProductsFolder;
    }

    /**
     * This methods returns the element for file folder
     * 
     * @return btnFileFolder
     */
    public WebElement getBtnFileFolder() {
        return btnFileFolder;
    }

    /**
     * This method returns the element for channels folder
     * 
     * @return btnChannelsFolder
     */
    public WebElement getBtnChannelsFolder() {
        return btnChannelsFolder;
    }

    /**
     * This method will return the button for collection pop-up in 'search'
     * header
     * 
     * @return btnCollectionPopup
     */
    public WebElement getBtnCollectionPopup() {
        return btnCollectionPopup;
    }

    /**
     * This method returns the element for collections popup people
     * 
     * @return popupCollectionPeople
     */
    public WebElement getPopupCollectionPeople() {
        return popupCollectionPeople;
    }

    /**
     * This method will return the text of results in collections' pop-up window
     * 
     * @return resultsMetaText
     */
    public WebElement getResultsMetaText() {
        return resultsMetaText;
    }

    /**
     * This method will return the element of option federated search connector
     * 
     * @return optionFederatedSearchConnector
     */
    public WebElement getFederatedSearchConnector() {
        return optionFederatedSearchConnector;
    }

    /**
     * This method will return the element of option configure search
     * 
     * @return optionConfigureSearch
     */
    public WebElement getConfigureSearch() {
        return optionConfigureSearch;
    }

    /**
     * This method will return the element of option microsoft sharepoint
     * connector
     * 
     * @return optionMicrosoftSharepointConnector
     */
    public WebElement getMicrosoftSharepointConnector() {
        return optionMicrosoftSharepointConnector;
    }

    /**
     * This method will return the element of option SAP netweaver connector
     * 
     * @return optionSapNetweaverConnector
     */
    public WebElement getSapNetweaverConnector() {
        return optionSapNetweaverConnector;
    }

    /**
     * This method will return the element of option Atlassian confluence
     * connector
     * 
     * @return optionAtlassianConfluenceConnector
     */
    public WebElement getAtlassianConfluenceConnector() {
        return optionAtlassianConfluenceConnector;
    }

    /**
     * This method will return the element of option contentserv connector
     * 
     * @return optionContentservConnector
     */
    public WebElement getContentservConnector() {
        return optionContentservConnector;
    }

    /**
     * This method will return the element of option configure shares
     * 
     * @return optionConfigureShares
     */
    public WebElement getConfigureShares() {
        return optionConfigureShares;
    }

    /**
     * This method will return the element of option configure collections
     * 
     * @return optionConfigureCollections
     */
    public WebElement getConfigureCollections() {
        return optionConfigureCollections;
    }

    /**
     * This method will return the element of option search help
     * 
     * @return optionSearchHelp
     */
    public WebElement getSearchHelp() {
        return optionSearchHelp;
    }

    /**
     * This method will return button options in search header
     * 
     * @return btnOptions
     */
    public WebElement getBtnOptions() {
        return btnOptions;
    }

    /**
     * This method will return the element for search box
     * 
     * @return txtBoxSearchField
     */
    public WebElement getSearchField() {
        return txtBoxSearchField;
    }

    /**
     * This method will return the element for search combo box
     * 
     * @return searchComboBox
     */
    public WebElement getSearchComboBox() {
        return searchComboBox;
    }

    /**
     * This method will return the element for everywhere search toggle
     * 
     * @return chkboxEverywhereSearch
     */
    public WebElement getChkboxEverywhereSearch() {
        return chkboxEverywhereSearch;
    }

    /**
     * This method will return the element for toggle button for activate search
     * in all shares
     * 
     * @return chkboxAllSharesSearch
     */
    public WebElement getChkboxAllSharesSearch() {
        return chkboxAllSharesSearch;
    }

    /**
     * This method will get the drop down arrow of combo box in search page
     * 
     * @return drpDwnComboBox
     */
    public WebElement getDrpDwnComboBox() {
        return drpDwnComboBox;
    }

    /**
     * This method will return the combo box option everywhere
     * 
     * @return comboBoxOptionEverywhere
     */
    public WebElement getComboBoxOptionEverywhere() {
        return drpDwnEverywhere;
    }

    /**
     * This method will return the combo box option Collections
     * 
     * @return comboBoxOptionCollections
     */
    public WebElement getComboBoxOptionCollections() {
        return drpDwnCollections;
    }

    /**
     * This method will return the combo box option Shares
     * 
     * @return comboBoxOptionShares
     */
    public WebElement getComboBoxOptionShares() {
        return drpDwnShares;
    }

    /**
     * This method will return the combo box option Favorites
     * 
     * @return comboBoxOptionFavorites
     */
    public WebElement getComboBoxOptionFavorites() {
        return drpDwnFavorites;
    }

    /**
     * This method will return the text box for search Combo box
     * 
     * @return txtBoxSearchComboBox
     */
    public WebElement getTxtBoxSearchComboBox() {
        return txtBoxSearchComboBox;
    }

    /**
     * This method will return the element for toggle button of 'Show Search
     * Suggestions'
     * 
     * @return chkboxShowSearchSuggestions
     */
    public WebElement getChkboxShowSearchSuggestions() {
        return chkboxShowSearchSuggestions;
    }

    /**
     * This method will return the combo box option more for collections
     * 
     * @return comboBoxOptionMoreForCollections
     */
    public WebElement getComboBoxOptionMoreForCollections() {
        return drpDwnMoreForCollections;
    }

    /**
     * This method will return the combo box option more for shares
     * 
     * @return comboBoxOptionMoreForShares
     */
    public WebElement getComboBoxOptionMoreForShares() {
        return drpDwnMoreForShares;
    }

    /**
     * This method will return the combo box option more for favorites
     * 
     * @return comboBoxOptionMoreForFavorites
     */
    public WebElement getComboBoxOptionMoreForFavorites() {
        return drpDwnMoreForFavorites;
    }

    /**
     * This method will return the default text visible in search combo box
     * 
     * @return searchComboBoxDefaultText
     */
    public WebElement getSearchComboBoxDefaultText() {
        return searchComboBoxDefaultText;
    }

    /**
     * This method will return search button from open search header
     * 
     * @return btnSearch
     */
    public WebElement getBtnSearch() {
        return btnSearch;
    }

    /**
     * This method will return the button for select mode in search header
     * 
     * @return btnSelectMode
     */
    public WebElement getBtnSelectMode() {
        return btnSelectMode;
    }

    /**
     * This method will return the check box for selection mode
     * 
     * @return chkboxSelectionMode
     */
    public WebElement getChkboxSelectionMode() {
        return chkboxSelectionMode;
    }

    /**
     * This method will return link icon for selection mode
     * 
     * @return linkIconSelectionMode
     */
    public WebElement getLinkIconSelectionMode() {
        return linkIconSelectionMode;
    }

    /**
     * This method will return link icon for selection mode for collection
     * 
     * @return linkIconSelectionModeForCollection
     */
    public WebElement getLinkIconSelectionModeForCollection() {
        return linkIconSelectionModeForCollection;
    }

    /**
     * This method will return the combo box option all shares
     * 
     * @return drpDwnAllShares
     */
    public WebElement getComboBoxOptionAllShares() {
        return drpDwnAllShares;
    }

    /**
     * This method will return the check box for selection mode for collection
     * 
     * @return chkboxSelectionModeForCollection
     */
    public WebElement getChkboxSelectionModeForCollection() {
        return chkboxSelectionModeForCollection;
    }

    /**
     * This method will return the checkbox for Hide in OpenSearch option in
     * collection general tab
     * 
     * @return chkboxHideInOpenSearch
     */
    public WebElement getChkboxHideInOpenSearch() {
        return chkboxHideInOpenSearch;
    }

    /**
     * This method will return the checkbox for deactivated option in collection
     * general tab
     * 
     * @return chkboxDeactivated
     */
    public WebElement getChkboxDeactivated() {
        return chkboxDeactivated;
    }

    /**
     * This method will return the element for drop down option Add To
     * Collection
     * 
     * @return drpDwnAddToCollection
     */
    public WebElement getDrpDwnAddToCollection() {
        return drpDwnAddToCollection;
    }

    /**
     * This method will return the element for drop down list of existing
     * collections
     * 
     * @return drpDwnListForExistingCollections
     */
    public WebElement getDrpDwnListForExistingCollection() {
        return drpDwnListForExistingCollections;
    }

    /**
     * Returns the open key.
     * 
     * @return WebElement lblOpenKey.
     */
    public WebElement getLblOpenKeyValue() {
        return lblOpenKeyValue;
    }

    /**
     * This method will return the context (right click option) delete
     * 
     * @return ctxDelete
     */
    public WebElement getCtxDelete() {
        return ctxDelete;
    }

    /**
     * This method will return the element for CSPopupDiv
     * 
     * @return csPopupDiv
     */
    public WebElement getCSPopupDiv() {
        return csPopupDiv;
    }

    /**
     * This method will return the text box for entering attribute name while
     * creating a new attribute in configuration
     * 
     * @return txtBoxAttributeName
     */
    public WebElement getTxtBoxAttributeName() {
        return txtBoxAttributeName;
    }

    /**
     * This method will return element for selecting attribute type
     * 
     * @return txtBoxAttributeType
     */
    public WebElement getTxtBoxAttributeType() {
        return txtBoxAttributeType;
    }

    /**
     * This method will return element for drop down cross reference for
     * selecting attribute type
     * 
     * @return drpDwnCrossReference
     */
    public WebElement getDrpDwnCrossReference() {
        return drpDwnCrossReference;
    }

    /**
     * This method will return element for drop down option 'Reference To PIM
     * Product' which is an attribute type
     * 
     * @return drpDwnReferenceToPimProduct
     */
    public WebElement getDrpDwnReferenceToPimProduct() {
        return drpDwnReferenceToPimProduct;
    }

    /**
     * This method will return icon to add reference attribute to search portal
     * 
     * @return imgAddRefAttrToSearchPortal
     */
    public WebElement getImgAddRefAttrToSearchPortal() {
        return imgAddRefAttrToSearchPortal;
    }

    /**
     * This method will return the attributes folder from data selection dialog
     * 
     * @return txtAttributesFolder
     */
    public WebElement getTxtAttributesFolder() {
        return txtAttributesFolder;
    }

    /**
     * This method will return icon to add attribute to collection
     * 
     * @return imgToAddAttrToCollection
     */
    public WebElement getImgToAddAttrToCollection() {
        return imgToAddAttrToCollection;
    }

    /**
     * This method will return link to view collection
     * 
     * @return linkViewCollection
     */
    public WebElement getLinkViewCollection() {
        return linkViewCollection;
    }

    /**
     * This method will return link to edit collection
     * 
     * @return linkEditCollection
     */
    public WebElement getLinkEditCollection() {
        return linkEditCollection;
    }

    /**
     * This method will return link to download collection
     * 
     * @return linkDownloadCollection
     */
    public WebElement getLinkDownloadCollection() {
        return linkDownloadCollection;
    }

    /**
     * This method will return check box to select all the products present
     * inside the folder
     * 
     * @return chkboxSelectAllProductsFromFolder
     */
    public WebElement getChkboxSelectAllProductsFromFolder() {
        return chkboxSelectAllProductsFromFolder;
    }

    /**
     * This method will return drop down option favorites from show more option
     * inside pim products folder
     * 
     * @return drpDwnFavoritesInPimShowMore
     */
    public WebElement getDrpDwnFavoritesInPimShowMore() {
        return drpDwnFavoritesInPimShowMore;
    }

    /**
     * This method will return drop down option Add favorite from show more
     * option inside pim products folder
     * 
     * @return drpDwnAddFavorite
     */
    public WebElement getDrpDwnAddFavorite() {
        return drpDwnAddFavorite;
    }

    /**
     * This method will return text box to enter label for favorite product
     * 
     * @return lblFavoriteProduct
     */
    public WebElement getLblFavoriteProduct() {
        return lblFavoriteProduct;
    }

    /**
     * Returns OK button in open search
     * 
     * @return btnOkOpenSearch
     */
    public WebElement getBtnOkOpenSearch() {
        return btnOkOpenSearch;
    }

    /**
     * Returns save button in open search
     * 
     * @return btnSaveOpenSearch
     */
    public WebElement getBtnSaveOpenSearch() {
        return btnSaveOpenSearch;
    }

    /**
     * This method will return the element for assigned landing page logo
     * 
     * @return imgAssignedLandingPageLogo
     */
    public WebElement getImgAssignedLandingPageLogo() {
        return imgAssignedLandingPageLogo;
    }

    /**
     * This method will return element for landing page logo in search header
     * 
     * @return imgLandingPageLogo
     */
    public WebElement getImgLandingPageLogo() {
        return imgLandingPageLogo;
    }

    /**
     * This method will return the hyperlink for browse files
     * 
     * @return txtBrowseFiles
     */
    public WebElement getTxtBrowseFiles() {
        return txtBrowseFiles;
    }

    /**
     * This method will return the filter text box for file name
     * 
     * @return txtBoxFilterByFileName
     */
    public WebElement getTxtBoxFilterByFileName() {
        return txtBoxFilterByFileName;
    }

    /**
     * This method returns the element for 'ShowFolders' toggle button
     * 
     * @return chkboxShowFolders
     */
    public WebElement getChkBoxShowFolders() {
        return chkboxShowFolders;
    }

    /**
     * This method returns the element for drop down 'SelectionType'
     * 
     * @return drpDwnSelectionType
     */
    public WebElement getDrpDwnSelectionType() {
        return drpDwnSelectionType;
    }

    /**
     * This method returns the element for button configure shares
     * 
     * @return - btnConfigureShares
     */
    public WebElement getBtnConfigureShares() {
        return btnConfigureShares;
    }

    /**
     * This method returns the element for Categories Sections
     * 
     * @return - categoriesSections
     */
    public WebElement getSecCategories() {
        return secCategories;
    }

    /**
     * This method returns the element for popup message box
     * 
     * @return - popUpMessageInfoBox
     */
    public WebElement getCSPopupMessageInfoBox() {
        return popUpMessageInfoBox;
    }

    /**
     * This method will enter text into text box
     * 
     * @param waitForReload waits for an element to reload
     * @param element contains WebElement
     * @param text String instance - text to be entered into the textbox
     */
    public void enterTextIntoTextbox(WebDriverWait waitForReload,
            WebElement element, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.clear();
        element.sendKeys(text);
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

}
