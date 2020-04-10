
package org.cs.csautomation.cs.settings;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserManagementPage {

    private WebDriver  browserDriverInstance;

    @FindBy(
            xpath = "//li[@id='StudioWidgetPane_e20aa82bb06b2cd4551ff728f5d58a2e_Title']//div[@class='CSPortalGuiPanelIcon']")
    private WebElement systemPreferencesNode;

    @FindBy(xpath = "//span[@id='UserManagement@0']")
    private WebElement userManagement;

    @FindBy(xpath = "//span[@id='UserManagement~Roles@0']")
    private WebElement userManagementRoles;

    @FindBy(xpath = "//span[@id='UserManagement~Roles@1']")
    private WebElement userManagementAdministration;

    @FindBy(xpath = "//span[@id='UserManagement~Configurations@0']")
    private WebElement userManagementAttributes;

    @FindBy(linkText = "MAM")
    private WebElement mamInAdministrator;

    @FindBy(xpath = "//input[@id='section_mam']")
    private WebElement studioCheckbox;

    @FindBy(
            xpath = "//span[@id='a6dcafbf4ed095cad196cfd49191314e']//div[contains(text(),'Studio')]")
    private WebElement studioInMam;

    @FindBy(xpath = "(//div[@class='sectionimage'][contains(.,'General')])[5]")
    private WebElement generalTabInMamStudio;

    @FindBy(xpath = "//input[contains(@id,'Rolemam:mamfile:checkduplicates')]")
    private WebElement checkDuplicatesCheckbox;

    @FindBy(xpath = "//td[contains(text(),'Check for Duplicates')]")
    private WebElement checkForDuplicatesOption;

    @FindBy(xpath = "//span[@id='UserManagement~Rubrics@0']")
    private WebElement accessRightsNode;

    @FindBy(xpath = "//span[@id='UserManagement~User@0']")
    private WebElement usersNode;

    @FindBy(xpath = "//span[@id='UserManagement~ConfigurationLinks@0']")
    private WebElement classesNode;

    @FindBy(xpath = "//input[@class='titleCheckBox']")
    private WebElement editRoleCheckbox;

    @FindBy(xpath = "//input[contains(@id,'RoleRoleName')]")
    private WebElement txtEditRoleName;

    @FindBy(xpath = "//nobr[contains(text(),'Rights')]")
    private WebElement rightsTab;

    @FindBy(xpath = "//a[@class='formlabel']")
    private WebElement formLabelAdd;

    @FindBy(xpath = "//nobr[contains(text(),'Administration')]")
    private WebElement administrationTab;

    @FindBy(xpath = "//input[contains(@id,'UserUserName')]")
    private WebElement txtUsername;

    @FindBy(xpath = "//input[contains(@id,'UserPassword')]")
    private WebElement txtPassword;

    @FindBy(xpath = "//input[@id='CSTypeLabel']")
    private WebElement btnCsTypeLabel;

    @FindBy(xpath = "//input[contains(@id,'UserconfigurationName')]")
    private WebElement txtUserconfigurationName;

    @FindBy(xpath = "//input[contains(@id,'UserconfigurationLabel')]")
    private WebElement txtUserconfigurationLabel;

    @FindBy(xpath = "//input[contains(@id,'UserconfigurationGuiSortOrder')]")
    private WebElement txtUserconfigurationSortOrder;

    @FindBy(xpath = "//select[contains(@id,'UserconfigurationIsRequired')]")
    private WebElement drpdwnUserconfigurationRequiredAttribute;

    @FindBy(xpath = "//select[contains(@id,'UserconfigurationIsInherited')]")
    private WebElement drpdwntUserconfigurationInherited;

    @FindBy(xpath = "//select[contains(@id,'UserconfigurationIsSearchable')]")
    private WebElement drpdwnUserconfigurationSearchable;

    @FindBy(
            xpath = "(//img[@class='CSGuiSelectionListAdd'][contains(@src,'pixel.gif')])[1]")
    private WebElement btnPlusToAddAttribute;

    @FindBy(xpath = "//span[@id='Configurations@0']")
    private WebElement attributesTabInDialogWindow;

    @FindBy(xpath = "//input[@id='CSGUI_MODALDIALOG_CANCELBUTTON']")
    private WebElement btnCancelUserManagement;

    @FindBy(xpath = "//input[@value='Replace']")
    private WebElement btnReplace;

    @FindBy(xpath = "//input[@value='Extend']")
    private WebElement btnExtend;

    @FindBy(xpath = "//table[@class='TabbedPaneTitle']/tbody/tr/td[9]")
    private WebElement tabbedPaneTitleData;
    @FindBy(xpath = "//a/img[contains(@src,'save.gif')]")
    private WebElement btnSave;

    @FindBy(xpath = "//a/img[contains(@src,'plus')]")
    private WebElement btnCreateNew;

    @FindBy(xpath = "//input[contains(@id,'RubricRubricName')]")
    private WebElement txtAccessLevelName;

    @FindBy(xpath = "//nobr[contains(text(),'Profile')]")
    private WebElement profileTab;

    @FindBy(xpath = "//input[contains(@id,'UserEmail')]")
    private WebElement txtEmailProfileTab;

    @FindBy(
            xpath = "(//img[@class='CSGuiSelectionListAdd'][contains(@src,'pixel.gif')])[1]")
    private WebElement btnPlusToAddUserGroup;

    @FindBy(xpath = "//div[@class='UserName']")
    private WebElement usernameOverviewTab;

    @FindBy(xpath = "//nobr[contains(text(),'Overview')]")
    private WebElement overviewTab;

    /**
     * This method returns the instance of overview tab
     * 
     * @return overviewTab
     */
    public WebElement getOverviewTab() {
        return overviewTab;
    }

    /**
     * This method clicks on the overview tab in rightmost pane
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkOverviewTab(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getOverviewTab()));
        getOverviewTab().click();
        CSLogger.info("clicked on overview tab");
    }

    /**
     * This method clicks userManagement tab
     * 
     * @param waitForReload waits for an element to reload
     * @param browserDriver contains browser driver instance
     */
    public void clkUserManagement(WebDriverWait waitForReload) {
        waitForReload
                .until(ExpectedConditions.visibilityOf(getUserManagement()));
        getUserManagement().click();
        CSLogger.info("Clicked on User Management");
    }

    /**
     * This method clicks on the administration tab
     * 
     * @param waitForReload waits for an element to reloads
     */
    public void clkAdministrationTab(WebDriverWait waitForReload) {
        waitForReload
                .until(ExpectedConditions.visibilityOf(getAdministrationTab()));
        getAdministrationTab().click();
        CSLogger.info("Clicked on administration tab");
    }

    /**
     * This method clicks on the rights tab
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkRightsTab(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions.visibilityOf(getRightsTab()));
        getRightsTab().click();
        CSLogger.info("Clicked on Rights tab");
    }

    /**
     * This method returns the instance of username in overview tab
     * 
     * @return usernameOverviewTab
     */
    public WebElement getUsernameOverviewTab() {
        return usernameOverviewTab;
    }

    /**
     * This method returns the instance of plus button to add user group
     * 
     * @return btnPlusToAddUserGroup
     */
    public WebElement getBtnPlusToAddUserGroup() {
        return btnPlusToAddUserGroup;
    }

    /**
     * This method returns the instance of user email in profile tab
     * 
     * @return
     */
    public WebElement getTxtEmailProfileTab() {
        return txtEmailProfileTab;
    }

    /**
     * This method returns the instance of profile tab
     * 
     * @return profileTab
     */
    public WebElement getProfileTab() {
        return profileTab;
    }

    /**
     * This method returns the instance of save button
     * 
     * @return btnSave
     */
    public WebElement getBtnSave() {
        return btnSave;
    }

    /**
     * This method returns the instance of access level name
     * 
     * @return txtAccessLevelName
     */
    public WebElement getTxtAccessLevelName() {
        return txtAccessLevelName;
    }

    /**
     * This method returns the instance of create new button
     * 
     * @return btnCreateNew
     */

    public WebElement getBtnCreateNew() {
        return btnCreateNew;
    }

    /**
     * This method clicks on save button to save the contents
     */
    public void clkBtnSave() {
        WebDriverWait waitForReload = new WebDriverWait(browserDriverInstance,
                60);
        waitForReload.until(ExpectedConditions.visibilityOf(getBtnSave()));
        getBtnSave().click();
    }

    /**
     * This method returns the instance of tabbed pane title
     * 
     * @return tabbedPaneTitleData
     */
    public WebElement getTabbedPaneTitleData() {
        return tabbedPaneTitleData;
    }

    /**
     * This method returns the instance of replace button
     * 
     * @return btnReplace
     */
    public WebElement getBtnExtend() {
        return btnExtend;
    }

    /**
     * This method returns the instance of replace button
     * 
     * @return btnReplace
     */
    public WebElement getBtnReplace() {
        return btnReplace;
    }

    /**
     * This method returns the instance of cancel button
     * 
     * @return btnCancelUserManagement
     */
    public WebElement getBtnCancelUserManagement() {
        return btnCancelUserManagement;
    }

    /**
     * This method returns the instance of attributes tab in the dialog window
     * 
     * @return attributesTabInDialogWindow
     */
    public WebElement getAttributesTabInDialogWindow() {
        return attributesTabInDialogWindow;
    }

    /**
     * This method returns the instance of plus button to add attribute to
     * created class
     * 
     * @return btnPlusToAddAttribute
     */
    public WebElement getBtnPlusToAddAttribute() {
        return btnPlusToAddAttribute;
    }

    /**
     * This method returns the instance of searchable attribute field
     * 
     * @return txtUserconfigurationSearchable
     */
    public WebElement getDrpdwnUserconfigurationSearchable() {
        return drpdwnUserconfigurationSearchable;
    }

    /**
     * This method returns the instance of configuration inherited attribute
     * 
     * @return txtUserconfigurationInherited
     */
    public WebElement getDrpdwnUserconfigurationInherited() {
        return drpdwntUserconfigurationInherited;
    }

    /**
     * This method returns the instance of required attribute textbox
     * 
     * @return getTxtUserconfigurationRequiredAttribute
     */
    public WebElement getDrpdwnUserconfigurationRequiredAttribute() {
        return drpdwnUserconfigurationRequiredAttribute;
    }

    /**
     * This method returns the instance of user configuration sort order txtbox
     * 
     * @return txtUserconfigurationSortOrder
     */
    public WebElement getTxtUserconfigurationSortOrder() {
        return txtUserconfigurationSortOrder;
    }

    /**
     * This method returns the instance of user configuration textbox
     * 
     * @return txtUserconfigurationLabel
     */
    public WebElement getTxtUserconfigurationLabel() {
        return txtUserconfigurationLabel;
    }

    /**
     * This method returns the instance of user configuration textbox
     * 
     * @return txtUserconfigurationName
     */
    public WebElement getTxtUserconfigurationName() {
        return txtUserconfigurationName;
    }

    /**
     * This method returns the instance of CSTypeLabel button
     * 
     * @return btnCsTypeLabel
     */
    public WebElement getBtnCsTypeLabel() {
        return btnCsTypeLabel;
    }

    /**
     * This method returns the instance of username field in administration tab
     *
     * @return txtUsername
     */
    public WebElement getTxtUsername() {
        return txtUsername;
    }

    /**
     * This method returns the instance of password field in administration tab
     *
     * @return txtPassword
     */
    public WebElement getTxtPassword() {
        return txtPassword;
    }

    /**
     * This method returns the instance of administration tab
     * 
     * @return administrationTab
     */
    public WebElement getAdministrationTab() {
        return administrationTab;
    }

    /**
     * This method returns the instance of add label for adding the access
     * rights and role
     * 
     * @return formLabelAdd
     */
    public WebElement getFormLabelAdd() {
        return formLabelAdd;
    }

    /**
     * This method returns the instance of Rights tab
     * 
     * @return rightsTab
     */
    public WebElement getRightsTab() {
        return rightsTab;
    }

    /**
     * This method returns the instance of edit role name checkbox
     * 
     * @return editRoleCheckbox
     */
    public WebElement getEditRoleCheckbox() {
        return editRoleCheckbox;
    }

    /**
     * This method returns the instance of text box containing role name
     * 
     * @return txtEditRoleName
     */
    public WebElement getTxtEditRoleName() {
        return txtEditRoleName;
    }

    /**
     * This method returns the instance of classes node
     * 
     * @return classesNode
     */
    public WebElement getClassesNode() {
        return classesNode;
    }

    /**
     * This method returns the instance of Attributes node in User Management
     * 
     * @return userManagementAttributes
     */
    public WebElement getUserManagementAttributes() {
        return userManagementAttributes;
    }

    /**
     * This method returns the instance of users node in user management tab
     * 
     * @return usersNode
     */
    public WebElement getUsersNode() {
        return usersNode;
    }

    /**
     * This method returns the instance of access rights node
     * 
     * @return accessRightsNode
     */
    public WebElement getAccessRightsNode() {
        return accessRightsNode;
    }

    /**
     * This method returns instance of checkforduplicates option
     * 
     * @return checkForDuplicatesOption
     */
    public WebElement getCheckForDuplicatesOption() {
        return checkForDuplicatesOption;
    }

    /**
     * This method returns instance of duplicates checkbox
     * 
     * @return checkDuplicatesCheckbox
     */
    public WebElement getCheckDuplicatesCheckbox() {
        return checkDuplicatesCheckbox;
    }

    /**
     * This method returns instance of general tab in mam studio
     * 
     * @return generalTabInMamStudio
     */
    public WebElement getGeneralTabInMamStudio() {
        return generalTabInMamStudio;
    }

    /**
     * This method returns instance of studio tab in mam
     * 
     * @return studioInMam
     */
    public WebElement getStudioInMam() {
        return studioInMam;
    }

    /**
     * This method returns the instance of studio checkbox
     * 
     * @return studioChecbox
     */
    public WebElement getStudioCheckbox() {
        return studioCheckbox;
    }

    /**
     * This method returns instance of Administrator tab
     * 
     * @return
     */
    public WebElement getMamInAdministrator() {
        return mamInAdministrator;
    }

    /**
     * This method returns the instance of UserManagementAdministration
     * 
     * @return userManagementAdministration
     */
    public WebElement getUserManagementAdministration() {
        return userManagementAdministration;

    }

    /**
     * This method returns the instance of roles tab in user management
     * 
     * @return userManagementRoles
     */
    public WebElement getUserManagementRoles() {
        return userManagementRoles;
    }

    /**
     * This method returns the instance of userManagement
     * 
     * @return userManagement
     */
    public WebElement getUserManagement() {
        return userManagement;
    }

    /**
     * This method returns the instance of system preferences node
     * 
     * @return systemPreferencesNode
     */
    public WebElement getSystemPreferencesNode() {
        return systemPreferencesNode;
    }

    /**
     * Parameterized constructor of class UserManagementPage
     * 
     * @param paramDriver contains the instance of WebDriver
     */
    public UserManagementPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized   Settings node page POM elements");
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }
}
