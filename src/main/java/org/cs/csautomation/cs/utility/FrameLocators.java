
package org.cs.csautomation.cs.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class FrameLocators {

    private static FrameLocators locators;
    private WebDriver            browserDriverInstance;

    @FindBy(id = "frm_4947d77df0891edef6b84be5fc695d2a")
    WebElement                   frmPimStudio;

    @FindBy(id = "frm_068f80c7519d0528fb08e82137a72131")
    WebElement                   frmToAddRootFolder_OpenSearch;

    @FindBy(xpath = "//iframe[@name='frmTree']")
    WebElement                   frmFrmTree;

    @FindBy(xpath = "//div[@id='CSPortalBody']/div/iframe")
    WebElement                   frmCsPortalBody;

    @FindBy(id = "frame_190")
    WebElement                   frmFrame190;

    @FindBy(xpath = "//div[@class=' CSPortalWindowContent']/iframe")
    WebElement                   frmCsPortalWindowContent;

    @FindBy(xpath = "//iframe[@frameborder='no']")
    WebElement                   frmTopFrame;

    @FindBy(id = "CSPopupDivFrame")
    WebElement                   frmCsPopupDivFrame;

    @FindBy(id = "main")
    WebElement                   frmSplitAreaFrameMain;

    @FindBy(name = "left")
    WebElement                   frmSplitAreaFrameLeft;

    @FindBy(xpath = "//div[@class='CSPortalWindow']/div[2]/iframe[1]")
    WebElement                   frmCreateNewClass;

    @FindBy(xpath = "//iframe[@frameborder='0']")
    WebElement                   frmDataSelectionDialogWindow;

    @FindBy(name = "frm_c81794045c6fc285e5e19d6343c0dbc6")
    WebElement                   frmDataSelectionDialogNestedFrm1;

    @FindBy(id = "idRecordsPdmarticleconfiguration_Left")
    WebElement                   frmIdRecordsPdmarticleconfigurationLeft;

    @FindBy(id = "idRecords_Center")
    WebElement                   frmIdRecordsCenter;

    @FindBy(xpath = "(//div[@class=' CSPortalWindowContent']/iframe[1])[2]")
    WebElement                   frmToAddAllValues;

    @FindBy(
            xpath = "//body[@id='CSGuiWindowBody']/div[1]/div[1]/div[1]/iframe[1]")
    WebElement                   frmVerifyAddedAttr;

    @FindBy(xpath = "//iframe[@name='frame_127']")
    WebElement                   frmSettingsFrame127;

    @FindBy(id = "StudioWidgetPane_e20aa82bb06b2cd4551ff728f5d58a2e_Content")
    WebElement                   frmStudioWidgetPaneContent;

    @FindBy(id = "pdmarticle")
    WebElement                   frmPdmArticle;

    @FindBy(id = "pdmarticlestructure")
    WebElement                   frmPdmArticleStructure;

    @FindBy(xpath = "//iframe[@frameborder='0']")
    WebElement                   frmCsGuiDialog;

    @FindBy(
            xpath = "//div[contains(@cs_name,'class/attribute')]/div/table/tbody/tr/td/iframe")
    WebElement                   frmProductClassAttribute;

    @FindBy(id = "idRecordsPdmarticleconfiguration_Left")
    WebElement                   frmSpiltAreaFramePdmarticleconfigurationLeft;

    @FindBy(
            xpath = "//iframe[contains(@src,'../admin/forward.php?forward=core%2Fextensions')]")
    WebElement                   frmSplitAreaContentMain;

    @FindBy(xpath = "//div[@id='CSPopupDiv_SUB']")
    WebElement                   frmCsPopupDivSub;

    @FindBy(id = "CSPopupDiv_SUBFrame")
    WebElement                   frmCsPopupDivSubFrame;

    @FindBy(xpath = "//body/iframe[1]")
    WebElement                   topFrame;

    @FindBy(xpath = "//iframe[@id='frame_192']")
    WebElement                   frame_192MamStudio;

    @FindBy(xpath = "//div[@id='CSPortalBody']//div//iframe")
    WebElement                   csPortalBodyFrame;

    @FindBy(xpath = "//iframe[@id='main']")
    WebElement                   mainFrameMam;

    @FindBy(xpath = "//div[@class='CSPortalWindow']")
    WebElement                   csPortalWindowDiv;

    @FindBy(xpath = "//div[@class='CSPortalWindow']")
    WebElement                   csPortalWindowMam;

    @FindBy(xpath = "//iframe[@id='frm_32bb858caefe3de6a6a4b332587eb5ac']")
    WebElement                   frameMamStudio;

    @FindBy(xpath = "//div[contains(@class,'CSPortalWindowContent')]//iframe")
    WebElement                   csPortalWindowFrame;

    @FindBy(xpath = "//iframe[@name='frm_f3d9c19bca9b2cfc29fdd569f14edcff']")
    WebElement                   frame_frmf3dMam;

    @FindBy(xpath = "//iframe[@id='frm_278c491bdd8a53618c149c4ac790da34']")
    WebElement                   leftSecFrameInDialogWindow;

    @FindBy(xpath = "//div[@id='CSPopupDiv']")
    WebElement                   popUpFrame;

    @FindBy(xpath = "//iframe[@name='nameFolders_Left']")
    WebElement                   foldersLeftFrame;

    @FindBy(xpath = "//iframe[@id='idFolders_Center']")
    WebElement                   foldersCenterFrame;

    @FindBy(xpath = "//div[@id='CSPopupDiv_SUB']")
    WebElement                   csPopupDivMam;

    @FindBy(xpath = "//iframe[@id='CSPopupDiv_SUBFrame']")
    WebElement                   csPopupDivFrameMam;

    @FindBy(id = "frmBottom")
    WebElement                   frmShowingStatusOfRunningActiveScript;

    @FindBy(id = "frm_12ceff2290bb9039beaa8f36d5dec226")
    WebElement                   frmJobsState;

    @FindBy(id = "frame_127")
    WebElement                   frmFrame_127;

    @FindBy(id = "StudioWidgetPane_e20aa82bb06b2cd4551ff728f5d58a2e_Content")
    WebElement                   frmStudioWidgetPane;

    @FindBy(id = "edit")
    WebElement                   frmEditForActiveScriptLabel;

    @FindBy(xpath = "//div[@class='CSSideBarBody']/iframe")
    WebElement                   frmcsSideBarBody;

    @FindBy(id = "derivationsView")
    WebElement                   frmDerivationsView;

    @FindBy(xpath = "//td[@id='1.mamfile.title']//nobr//span//img")
    WebElement                   fileCategoriesMam;

    @FindBy(xpath = "//nobr[contains(text(),'MAM Studio')]")
    WebElement                   mamStudio;

    @FindBy(xpath = "//html//body//iframe")
    WebElement                   topFrameInFileCategoryLeftSection;

    @FindBy(xpath = "//iframe[@id='mamfile']")
    WebElement                   frameMamFile;

    @FindBy(xpath = "//div[@class='moduleContent']//div//iframe")
    WebElement                   moduleContentFrameMam;

    @FindBy(xpath = "//iframe[@name='nameRecordsMamfileconfiguration_Left']")
    WebElement                   leftRecordFrameMam;

    @FindBy(xpath = "//div[@cs_name='Products']/div/table/tbody/tr/td/iframe")
    WebElement                   frmProductsFolderFrame;

    @FindBy(id = "idRecordsPdmarticle_Left")
    WebElement                   frmIdRecordsPdmarticleLeft;

    @FindBy(id = "idFolders_Left")
    WebElement                   frmIdFoldersLeft;

    @FindBy(
            xpath = "//div[@cs_name='File Folders']/div/table/tbody/tr/td/iframe")
    WebElement                   frmFileFolderFrame;

    @FindBy(id = "idFolders_Center")
    WebElement                   frmIdFoldersCenter;

    @FindBy(id = "idRecordsUser_Left")
    WebElement                   frmIdRecordsUserLeft;

    @FindBy(xpath = "//div[@cs_name='User']/div/table/tbody/tr/td/iframe")
    WebElement                   frmUserFolderFrame;

    @FindBy(xpath = "//div[@class='CSPortalGuiPaneContent']/iframe[3]")
    WebElement                   frmCsPortalGuiPaneContent;

    @FindBy(id = "frm_4b1b4dc8cf38b3c64b1d657da8f5ac8c")
    WebElement                   frmReport;

    @FindBy(id = "frm_f907e651164789346ae0a1e257c462d8")
    WebElement                   frmPhpScript;

    @FindBy(id = "derivationsTree")
    WebElement                   frmDerivationsTree;

    @FindBy(id = "frmCenter")
    WebElement                   frmSplitAreaFrameFrmCenter;

    @FindBy(xpath = "//iframe[contains(@src,'dialogs|ColorInputDialog.php')]")
    WebElement                   frmColorSelectionDialog;

    @FindBy(xpath = "//div[@class='CSSideBarBody']/iframe")
    WebElement                   csSideBarBodyFrame;

    @FindBy(
            xpath = "//iframe[@id='itemControl_frm_54abfc11e7da6250c8b7aaa3b4eb982a_Content']")
    WebElement                   itemControlFrame;

    @FindBy(
            xpath = "//div[@class='CSPortalWindow']/div[2]/iframe[contains(@src,'ConfirmDialog')]")
    WebElement                   frmCsPortalWindowConfirmDialog;

    @FindBy(xpath = "//iframe[@id='frm_48147eb2f7d22e034d02d24c7a283ed2']")
    WebElement                   frmFrameSettings;

    @FindBy(xpath = "//iframe[@id='edit']")
    WebElement                   frmEdit;

    @FindBy(xpath = "//iframe[@id='rubricTreeFrame']")
    WebElement                   frmRubicTree;

    @FindBy(
            xpath = "//iframe[@id='itemControl_frm_11ef007269e2570b3f4c9f6acd5d1d24_Content']")
    WebElement                   frmItemControlProduct;

    @FindBy(xpath = "//div[@class='CSPortalWidgetContent']/iframe")
    WebElement                   frmPortalWidgetContentFrame;

    @FindBy(
            xpath = "(//table[@class='CSPortalMain']/tbody/tr/td[2]/div/div/div/div/div/iframe)[1]")
    WebElement                   frmCsPortalMain;

    @FindBy(
            xpath = "//iframe[@id='StudioWidgetPane_4947d77df0891edef6b84be5fc695d2a_Content']")
    WebElement                   frmStudioWidgetPaneContentUserManagement;

    @FindBy(xpath = "//div[contains(@class,'CSPortalWindowContent')]//iframe")
    WebElement                   frmCsPortalWindowWidget;

    @FindBy(xpath = "//div[@class='TabBody ']/div/table/tbody/tr/td/iframe")
    WebElement                   frmTabBodyUserManagement;

    @FindBy(xpath = "//iframe[@id='idRecordsUserconfiguration_Left']")
    WebElement                   frmIdRecordsUserConfigurationLeft;

    @FindBy(
            xpath = "//iframe[contains(@src,'core|extensions|system|gui|CSAdministrationTree.php')]")
    WebElement                   frmAdministrationTree;

    @FindBy(xpath = "//iframe[contains(@src,'editUsers.php')]")
    WebElement                   frmEdituser;

    @FindBy(
            xpath = "  //iframe[@id='StudioWidgetPane_45f7c549c227de2abfa73c634eab753e_Content']")
    WebElement                   frmStudioWidgetPaneContentProjectManager;

    @FindBy(xpath = "//iframe[@id='browse']")
    WebElement                   frmbrowse;

    @FindBy(xpath = "//iframe[@id='frm_7df96b18c230f90ada0a9e2307226338']")
    WebElement                   frmTemplatesWindow;

    @FindBy(xpath = "//iframe[@id='idRecordsTasktemplate_Left']")
    WebElement                   frmIdRecordsTasktemplateLeft;

    @FindBy(
            xpath = "//iframe[@id='itemControl_frm_ef615563c8e8ea902c7fcac3cd2c4246_Content']")
    WebElement                   frmTasks;

    @FindBy(className = "viewWorkflow")
    WebElement                   frmViewWorkflow;

    @FindBy(xpath = "//iframe[@id='idRecordsUser_Left']")
    WebElement                   frmRecordsUserLeft;

    @FindBy(
            xpath = "//table[@class='TabbedPane']/tbody/tr/td/div/div/table/tbody/tr/td/iframe")
    WebElement                   frmTabbedPane;

    @FindBy(
            xpath = "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr/td[4]/a/img")
    WebElement                   frmCsGuiToolbarHorizontal;

    @FindBy(id = "idRecordsPdmarticleconfiguration_Bottom")
    WebElement                   frmIdRecordsPdmarticleConfigurationBottom;

    @FindBy(id = "frmRight")
    WebElement                   frmSplitAreaFrameFrmRight;

    @FindBy(
            xpath = "//iframe[@id='StudioWidgetPane_872804c3d52e2ea7fd2da64745c0d76a_Content']")
    WebElement                   frmTanslationManager;

    @FindBy(xpath = "//div[@cs_name='Favorites']/div/table/tbody/tr/td/iframe")
    WebElement                   frmFavoritesDataSelectionDialog;

    @FindBy(
            xpath = "//iframe[contains(@src,'CSStudioWidget.show.php&layout=&studio=PIM+Studio')]")
    WebElement                   frmImportStagingStudioWidget;

    @FindBy(
            xpath = "//iframe[contains(@src,'forward.php?cs_stream_file=tmp%2FRecordInputDialog')]")
    WebElement                   frmImportRecordDialog;

    @FindBy(
            xpath = "(//table[contains(@class,'TabbedPane')]/tbody/tr/td/div/div/table/tbody/tr/td/iframe)[2]")
    WebElement                   frmTranslationManagerAddProduct;

    @FindBy(
            xpath = "//table[@class='TabbedPane ']/tbody/tr[2]/td/div/div/table/tbody/tr/td/iframe")
    WebElement                   frmTabbedPaneTranslationManager;

    @FindBy(xpath = "//iframe[@id='frmLeft']")
    WebElement                   frmLeftAttributeTypeWindow;

    @FindBy(xpath = "(//div[@class='CSPortalWindow']/div[2]/iframe)[1]")
    WebElement                   frmCsPortalWindowTranslationManager;

    @FindBy(id = "frmTop")
    WebElement                   frmFrmTop;

    @FindBy(xpath = "//iframe[@name='frmChart']")
    WebElement                   frmChart;

    @FindBy(xpath = "//iframe[@id='itemControl_NotesEditor_Content']")
    WebElement                   frmMamNotesEditor;

    @FindBy(id = "idRecordsMamfile_Left")
    WebElement                   frmIdRecorsMamFileLeft;

    @FindBy(xpath = "//div[@class='CSPortalGuiPaneContent']/iframe[2]")
    WebElement                   frmMamWhiteBoardPageTemplate;

    @FindBy(xpath = "//div[@class='CSPortalGuiPaneContent']/iframe[1]")
    WebElement                   frmMamWhiteBoardPageObjectSelection;

    @FindBy(id = "idRecordsPdmarticlestructure_Left")
    WebElement                   frmIdRecordsPdmarticleStructureLeft;

    @FindBy(xpath = "//div[@cs_name='Channels']/div/table/tbody/tr/td/iframe")
    WebElement                   frmChannelsMidFrame;

    @FindBy(xpath = "(//div[@class='CSPortalWindow']/div[2]/iframe)[2]")
    WebElement                   frmSubCSPortalWindow;

    @FindBy(
            xpath = "//div[@cs_name='Object Folder']/div/table/tbody/tr/td/iframe")
    WebElement                   frmObjectFolderInFileSelectionDialog;

    @FindBy(id = "idObject_Center")
    WebElement                   frmObjectCenterInFileSelectionDialog;

    @FindBy(id = "CSPopupDiv_SUB_SUBFrame")
    WebElement                   frmCSPopupDivSubSubFrame;

    @FindBy(xpath = "//iframe[@id='frame_234']")
    WebElement                   frmFrame_234;

    @FindBy(xpath = "//iframe[@id='frameWidget']")
    WebElement                   frmFrameWidget;

    @FindBy(xpath = "//span[contains(@id,'RuleConditionHtml')]//iframe")
    WebElement                   frmRuleConditionHtml;

    /**
     * Returns the instance of rule condition html frame
     * 
     * @return frmRuleConditionHtml
     */
    public WebElement getFrmRuleConditionHtml() {
        return frmRuleConditionHtml;
    }

    /**
     * Returns the instance of frame widget instance
     * 
     * @return the frmFrameWidget
     */
    public WebElement getFrmFrameWidget() {
        return frmFrameWidget;
    }

    /**
     * This method returns the instance of frame234
     * 
     * @return the frmFrame_234
     */
    public WebElement getFrmFrame_234() {
        return frmFrame_234;
    }

    /**
     * This method returns the instance of chart frame
     * 
     * @return the frmChart
     */
    public WebElement getFrmChart() {
        return frmChart;
    }

    @FindBy(
            xpath = "(//div[@class='CSPortalWindow'])[2]//div[contains(@class,'CSPortalWindowContent')]//iframe")
    WebElement frmSubCsPortalWindowContent;

    /**
     * Returns webElement frmSubCsPortalWindowContent
     * 
     * @return frmSubCsPortalWindowContent
     */
    public WebElement getFrmSubCsPortalWindowContent() {
        return frmSubCsPortalWindowContent;
    }

    /**
     * This method returns the instance of cs portal window translation manager
     * 
     * @return the frmCsPortalWindowTranslationManager
     */
    public WebElement getFrmCsPortalWindowTranslationManager() {
        return frmCsPortalWindowTranslationManager;
    }

    /**
     * This method returns the instance of left frame in attribute type window
     * 
     * @return frmLeftAttributeTypeWindow
     */
    public WebElement getFrmLeftAttributeTypeWindow() {
        return frmLeftAttributeTypeWindow;
    }

    /**
     * This method returns the instance of frame in translation manager in
     * translations tab
     * 
     * @return frmTabbedPaneTranslationManager
     */
    public WebElement getFrmTabbedPaneTranslationManager() {
        return frmTabbedPaneTranslationManager;
    }

    /**
     * This method returns the instance of translation manager frame while
     * adding product
     * 
     * @return frmTranslationManagerAddProduct
     */
    public WebElement getFrmTranslationManagerAddProduct() {
        return frmTranslationManagerAddProduct;
    }

    @FindBy(xpath = "//div[@class='CSPortalWindow']/div[2]/iframe[1]")
    WebElement frmFileSelectionDialog;

    @FindBy(
            xpath = "//div[@cs_name='FileFolders']/div/table/tbody/tr/td/iframe")
    WebElement frmFileSelectionDialogMainContent;

    @FindBy(
            xpath = "//table[@class='TabbedPane']/tbody/tr/td/div[2]/div/table/tbody/tr/td/iframe")
    WebElement frmLandingPageLogoSelection;

    @FindBy(xpath = "//iframe[@id='frame_197']")
    WebElement frmFrame197;

    @FindBy(
            xpath = "//div[@class='CSPortalGuiLayoutCenterOverflow2']/div/iframe")
    WebElement frmSearchFrameWidget197;

    @FindBy(xpath = "//div[@cs_name='File']/div/table/tbody/tr/td/iframe")
    WebElement frmfileFrame;

    @FindBy(
            xpath = "//div[@class='CSPortalWindow']//div[contains(@class,'CSPortalWindowContent')]//iframe")
    WebElement frmCSPortalWindow;

    @FindBy(
            xpath = "//td[@class='splitareacontentcenter']/div/table/tbody/tr/td/iframe")
    WebElement frmSplitAreaContentCenterSearchPortal;

    @FindBy(xpath = "//div[@cs_name='Attribute Mappings']//iframe")
    WebElement frmAttributeMapping;

    /**
     * This method returns the widget content frame in translation manager
     * 
     * @return frmTranlationManager
     */
    public WebElement getFrmTranslationManager() {
        return frmTanslationManager;
    }

    /**
     * This method returns the instance of split area right section
     * 
     * @return frmSplitAreaFrameFrmRight
     */
    public WebElement getFrmSplitAreaFrameFrmRight() {
        return frmSplitAreaFrameFrmRight;
    }

    /**
     * This method returns the instance of record user configuration bottom
     * frame
     * 
     * @return frmIdRecordsUserConfigurationLeft
     */
    public WebElement getFrmIdRecordsPdmarticleconfigurationBottom() {
        return frmIdRecordsPdmarticleConfigurationBottom;
    }

    /**
     * This method returns the instance frame Tasks
     * 
     * @return frmTasks
     */
    public WebElement getFrmTasks() {
        return frmTasks;
    }

    /**
     * This method returns the instance frame Task Template left
     * 
     * @return frmIdRecordsTasktemplateLeft
     */
    public WebElement getFrmIdRecordsTasktemplateLeft() {
        return frmIdRecordsTasktemplateLeft;
    }

    /**
     * This method returns the instance frame Templates window
     * 
     * @return frmTemplatesWindow
     */
    public WebElement getFrmTemplatesWindow() {
        return frmTemplatesWindow;
    }

    /**
     * This method returns the instance frame browse
     * 
     * @return frmbrowse
     */
    public WebElement getFrmbrowse() {
        return frmbrowse;
    }

    /**
     * This method returns the instance of widget Project Manager
     * 
     * @return frmStudioWidgetPaneContentProjectManager
     */
    public WebElement getFrmStudioWidgetPaneContentProjectManager() {
        return frmStudioWidgetPaneContentProjectManager;
    }

    /**
     * This method returns the instance of cs gui horizontal toolbar
     * 
     * @return frmCsGuiToolbarHorizontal
     */
    public WebElement getFrmCsGuiToolbarHorizontal() {
        return frmCsGuiToolbarHorizontal;
    }

    /**
     * This method returns the instance of left records user frame
     * 
     * @return frmRecordsUserLeft
     */
    public WebElement getFrmRecordsUserLeft() {
        return frmRecordsUserLeft;
    }

    /**
     * This method returns the instance of middle tabbed pane frame
     * 
     * @return frmTabbedPane
     */
    public WebElement getFrmTabbedPane() {
        return frmTabbedPane;
    }

    /**
     * This method returns the instance of CS portal window widget
     * 
     * @return frmCsPortalWindowWidget
     */
    public WebElement getFrmCsPortalWindowWidget() {
        return frmCsPortalWindowWidget;
    }

    /**
     * This method returns the instance of record user configuration left frame
     * 
     * @return frmIdRecordsUserConfigurationLeft
     */
    public WebElement getFrmIdRecordsUserConfigurationLeft() {
        return frmIdRecordsUserConfigurationLeft;
    }

    /**
     * This method returns the instance of tab body frame in user management
     * 
     * @return frmTabBodyUserManagement
     */
    public WebElement getFrmTabBodyUserManagement() {
        return frmTabBodyUserManagement;
    }

    /**
     * This method returns the instance of studio widget pane content frame in
     * user management
     * 
     * @return frmStudioWidgetPaneContentUserManagement
     */
    public WebElement getFrmStudioWidgetPaneContentUserManagement() {
        return frmStudioWidgetPaneContentUserManagement;
    }

    /**
     * This method returns the instance of CS Portal main frame
     * 
     * @return frmCsPortalMain
     */
    public WebElement getFrmCsPortalMain() {
        return frmCsPortalMain;
    }

    /**
     * This method returns the instance of frame 242
     * 
     * @return frmPortalWidgetContentFrame
     */
    public WebElement getFrmPortalWidgetContentFrame() {
        return frmPortalWidgetContentFrame;
    }

    /**
     * This method returns the instance of item control frame of product
     * 
     * @return frmItemControlProduct
     */
    public WebElement getFrmItemControlProduct() {
        return frmItemControlProduct;
    }

    /**
     * This method returns the instance of rubic tree frame
     * 
     * @return frmRubicTree
     */
    public WebElement getFrmRubicTree() {
        return frmRubicTree;
    }

    /**
     * This method returns the instance of edit frame
     * 
     * @return frmEdit
     */
    public WebElement getFrmEdit() {
        return frmEdit;
    }

    /**
     * This method returns the instance of from frame in settings right section
     * pane
     * 
     * @return frmFrameSettings
     */
    public WebElement getFrmFrameSettings() {
        return frmFrameSettings;
    }

    /**
     * This method returns the instance of side bar body frame in right section
     * pane while traversing to history
     * 
     * @return csSideBarBodyFrame
     */
    public WebElement getCsSideBarBodyFrame() {
        return csSideBarBodyFrame;
    }

    /**
     * This method returns instance of mam studio node
     * 
     * @return mamStudio
     */
    public WebElement getMamStudio() {
        return mamStudio;
    }

    /**
     * This method returns instance of left record frame in mam
     * 
     * @return leftRecordFrameMam
     */
    public WebElement getLeftRecordFrameMam() {
        return leftRecordFrameMam;
    }

    /**
     * This method returns frame3dmam
     * 
     * @return frame_frmf3eMam
     */
    public WebElement getFrame_frmf3dMam() {
        return frame_frmf3dMam;
    }

    /**
     * This method returns module content frame in mam
     * 
     * @return moduleContentFrameMam
     */
    public WebElement getModuleContentFrameMam() {
        return moduleContentFrameMam;
    }

    /**
     * This method returns mam file frame in mam
     * 
     * @return getFrameMamFile
     */
    public WebElement getFrameMamFile() {
        return frameMamFile;
    }

    /**
     * This method returns instance of top frame in left section of file
     * category
     * 
     * @return topFrameInFileCategoryLeftSection
     */
    public WebElement getTopFrameInFileCategoryLeftSection() {
        return topFrameInFileCategoryLeftSection;
    }

    /**
     * This method returns instance of file categories in mam
     * 
     * @return fileCategoriesMam
     */
    public WebElement getFileCategoriesMam() {
        return fileCategoriesMam;
    }

    /**
     * This method returns instance of pop up div in mam
     * 
     * @return csPopupDivMam
     */

    public WebElement getCsPopupDivMam() {
        return csPopupDivMam;
    }

    /**
     * This method returns pop up div frame in mam
     * 
     * @return csPopupDivFrameMam
     */
    public WebElement getCsPopupDivFramevMam() {
        return csPopupDivFrameMam;
    }

    /**
     * This method returns instance of main frame in mam
     * 
     * @return mainFrameMam
     */
    public WebElement getMainFrameMam() {
        return mainFrameMam;
    }

    /**
     * This method returns folder's center frme in mam
     * 
     * @return foldersCenterFrame
     */
    public WebElement getFoldersCenterFrame() {
        return foldersCenterFrame;
    }

    /**
     * This method returns the instance of folder's left frame
     * 
     * @return foldersLeftFrame
     */
    public WebElement getfoldersLeftFrame() {
        return foldersLeftFrame;
    }

    /**
     * This method returns instance of left section frame in dialog window
     * 
     * @return leftSecFrameInDialogWindow
     */
    public WebElement getLeftSecFrameInDialogWindow() {
        return leftSecFrameInDialogWindow;
    }

    /**
     * This method returns instance of pop up frame in mam return popUpFrame
     */
    public WebElement getPopUpFrame() {
        return popUpFrame;
    }

    /**
     * This method returns instance of cs portal window in mam
     * 
     * @return csPortalWindowMam
     */
    public WebElement getCsPortalWindowDiv() {
        return csPortalWindowDiv;

    }

    /**
     * This method returns instance of cs portal window in mam
     * 
     * @return csPortalWindowMam
     */
    public WebElement getCsPortalWindowMam() {
        return csPortalWindowMam;

    }

    /**
     * This method returns the instance of cs portal window frame in mam
     * 
     * @return csPortalWindowMamStudio
     */
    public WebElement getCsPortalWindowFrame() {
        return csPortalWindowFrame;
    }

    /**
     * This method returns instance of frame in mam studio
     * 
     * @return frameMamStudio
     */
    public WebElement getFrameMamStudio() {
        return frameMamStudio;
    }

    /**
     * This method returns instance of portal body in mam studio
     * 
     * @return csPortalBodyMamStudio
     */
    public WebElement getCsPortalBodyFrame() {
        return csPortalBodyFrame;
    }

    /**
     * This method returns instance of frame192 in mam studio
     * 
     * @return frame_192MamStudio
     */
    public WebElement getFrame_192MamStudio() {
        return frame_192MamStudio;
    }

    /**
     * This method returns instance of top frame in mam studio
     * 
     * @return topFrameMamStudio
     */
    public WebElement getTopFrame() {
        return topFrame;
    }

    public WebElement getFrmCsPopupDivSub() {
        return frmCsPopupDivSub;
    }

    public FrameLocators(WebDriver paramDriver) {
        this.browserDriverInstance = paramDriver;
        PageFactory.initElements(browserDriverInstance, this);
    }

    /**
     * Returns webElement frmFrmTree
     * 
     * @return frmFrmTree
     */
    public WebElement getFrmFrmTree() {
        return frmFrmTree;
    }

    /**
     * This method returns settings frame
     * 
     * @return frmSettingsFrame127
     */
    public WebElement getsettingsRightPaneMainFrame() {
        return frmSettingsFrame127;
    }

    /**
     * Returns StudioWidgetPaneContentFrame
     * 
     * @return frmStudioWidgetPaneContent
     */
    public WebElement getStudioWidgetPaneContentFrame() {
        return frmStudioWidgetPaneContent;
    }

    /**
     * Returns webElement frmCsPortalBody
     * 
     * @return frmCsPortalBody
     */
    public WebElement getFrmCsPortalBody() {
        return frmCsPortalBody;
    }

    /**
     * Returns webElement frmFrame190
     * 
     * @return frmFrame190
     */
    public WebElement getFrmFrame190() {
        return frmFrame190;
    }

    /**
     * Returns webElement frmCsPortalWindowContent
     * 
     * @return frmCsPortalWindowContent
     */
    public WebElement getFrmCsPortalWindowContent() {
        return frmCsPortalWindowContent;
    }

    /**
     * Returns webElement frmTopFrame
     * 
     * @return frmTopFrame
     */
    public WebElement getFrmTopFrame() {
        return frmTopFrame;
    }

    /**
     * Returns webElement frmCsPopupDivFrame
     * 
     * @return frmCsPopupDivFrame
     */
    public WebElement getFrmCsPopupDivFrame() {
        return frmCsPopupDivFrame;
    }

    /**
     * Returns webElement frmPimStudio
     * 
     * @return frmPimStudio
     */
    public WebElement getFrmPimStudio() {
        return frmPimStudio;
    }

    /**
     * Returns webElement frmSplitAreaFrameMain
     * 
     * @return frmSplitAreaFrameMain
     */
    public WebElement getFrmSplitAreaFrameMain() {
        return frmSplitAreaFrameMain;
    }

    /**
     * Returns webElement frmSplitAreaFrameLeft
     * 
     * @return frmSplitAreaFrameLeft
     */
    public WebElement getFrmSplitAreaFrameLeft() {
        return frmSplitAreaFrameLeft;
    }

    /**
     * Returns webElement frmCreateNewClass
     * 
     * @return frmCreateNewClass
     */
    public WebElement getFrmCreateNewClass() {
        return frmCreateNewClass;
    }

    /**
     * Returns webElement frmDataSelectionDialogWindow
     * 
     * @return frmDataSelectionDialogWindow
     */
    public WebElement getFrmDataSelectionDialog() {
        return frmDataSelectionDialogWindow;
    }

    /**
     * Returns webElement frmDataSelectionDialogNestedFrm1
     * 
     * @return frmDataSelectionDialogNestedFrm1
     */
    public WebElement getFrmDataSelectionDialogNestedOne() {
        return frmDataSelectionDialogNestedFrm1;
    }

    /**
     * Returns webElement frmIdRecordsPdmarticleconfigurationLeft
     * 
     * @return frmIdRecordsPdmarticleconfigurationLeft
     */
    public WebElement getFrmIdRecordsPdmarticleconfigurationLeft() {
        return frmIdRecordsPdmarticleconfigurationLeft;
    }

    /**
     * Returns webElement frmIdRecords_Center
     * 
     * @return frmIdRecords_Center
     */
    public WebElement getFrmIdRecordsCenter() {
        return frmIdRecordsCenter;
    }

    /**
     * Returns webElement frmAddAllAttrToClass
     * 
     * @return frmAddAllAttrToClass
     */
    public WebElement getFrmToAddAllValues() {
        return frmToAddAllValues;
    }

    /**
     * Returns webElement frmVerifyAddedAttr
     * 
     * @return frmVerifyAddedAttr
     */
    public WebElement getFrmToVerifyAddedAttr() {
        return frmVerifyAddedAttr;
    }

    /**
     * Returns webElement frmProductClassAttribute
     * 
     * @return frmProductClassAttribute
     */
    public WebElement getFrmProductClassAttribute() {
        return frmProductClassAttribute;
    }

    /**
     * Returns webElement frmPdmArticle
     * 
     * @return frmPdmArticle
     */
    public WebElement getFrmPdmArticle() {
        return frmPdmArticle;
    }

    /**
     * Returns webElement frmPdmArticleStructure
     * 
     * @return frmPdmArticleStructure
     */
    public WebElement getFrmPdmArticleStructure() {
        return frmPdmArticleStructure;
    }

    /**
     * Returns webElement frmCsGuiDialog
     * 
     * @return
     */
    public WebElement getFrmCsGuiDialog() {
        return frmCsGuiDialog;
    }

    /**
     * Returns webElement frmSpiltAreaFramePdmarticleconfigurationLeft
     * 
     * @return frmSpiltAreaFramePdmarticleconfigurationLeft
     */
    public WebElement getFrmSpiltAreaFramePdmarticleconfigurationLeft() {
        return frmSpiltAreaFramePdmarticleconfigurationLeft;
    }

    /**
     * Returns webElement frmSplitAreaContentMain
     * 
     * @return frmSplitAreaContentMain
     */
    public WebElement getFrmSplitAreaContentMain() {
        return frmSplitAreaContentMain;
    }

    /**
     * This method traverses upto attributes node in left section PIM studio
     * 
     * @param waitForReload
     */

    public void
            traverseToPimSettingsAttributeNode(WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriverInstance);
    }

    /**
     * Returns the webElement frmCsPopupDivSubFrame
     * 
     * @return frmCsPopupDivSubFrame
     */
    public WebElement getFrmCsPopupDivSubFrame() {
        return frmCsPopupDivSubFrame;
    }

    /**
     * Returns the webElement frmProductsFolderFrame
     * 
     * @return frmProductsFolderFrame
     */
    public WebElement getFrmProductsFrame() {
        return frmProductsFolderFrame;
    }

    /**
     * Returns the webElement frmIdRecordsPdmarticleLeft
     * 
     * @return frmIdRecordsPdmarticleLeft
     */
    public WebElement getFrmIdRecordsPdmarticleLeft() {
        return frmIdRecordsPdmarticleLeft;
    }

    /**
     * Returns the webElement frmIdFoldersLeft
     * 
     * @return frmIdFoldersLeft
     */
    public WebElement getFrmIdFoldersLeft() {
        return frmIdFoldersLeft;
    }

    /**
     * Returns the webElement frmFileFolderFrame
     * 
     * @return frmFileFolderFrame
     */
    public WebElement getFrmFileFoldersFrame() {
        return frmFileFolderFrame;
    }

    /**
     * Returns the webElement frmIdFoldersCenter
     * 
     * @return frmIdFoldersCenter
     */
    public WebElement getFrmIdFoldersCenter() {
        return frmIdFoldersCenter;
    }

    /**
     * Returns the webElement frmIdRecordsUserLeft
     * 
     * @return frmIdRecordsUserLeft
     */
    public WebElement getFrmIdRecordsUserLeft() {
        return frmIdRecordsUserLeft;
    }

    /**
     * Returns the webElement frmUserFolderFrame
     * 
     * @return frmUserFolderFrame
     */
    public WebElement getFrmUserFolderFrame() {
        return frmUserFolderFrame;
    }

    /**
     * Returns the instance of Frame locators
     * 
     * @param driver
     * @return
     */
    public static FrameLocators getIframeLocators(WebDriver driver) {
        if (locators == null) {
            locators = new FrameLocators(driver);
        }
        return locators;
    }

    /**
     * This method switches upto main frame while traversing in right section
     * pane
     * 
     * @param waitForReload
     */
    public void switchToSplitAreaFrame(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(getFrmSplitAreaFrameMain()));
        CSLogger.info("Switched to SplitAreaFrameMain");
    }

    /**
     * This method switches to top frame
     * 
     * @param waitForReload
     */
    public void switchToTopFrame(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(getFrmTopFrame()));
        CSLogger.info("Switched to top frame");

    }

    /**
     * Returns webElement frmShowingStatusOfRunningActiveScript
     * 
     * @return frmShowingStatusOfRunningActiveScript
     */
    public WebElement getFrmShowingStatusOfRunningActiveScript() {
        return frmShowingStatusOfRunningActiveScript;

    }

    /**
     * Returns webElement frmEditForActiveScriptLabel
     * 
     * @return frmEditForActiveScriptLabel
     */
    public WebElement getFrmEditForActiveScript() {
        return frmEditForActiveScriptLabel;

    }

    /**
     * Returns webElement frmSplitAreaContentMain
     * 
     * @return frmJobsState
     */
    public WebElement getFrmJobsState() {
        return frmJobsState;
    }

    /**
     * Returns webElement FrmFrame127
     * 
     * @return frmFrame_127
     */
    public WebElement getFrmFrame127() {
        return frmFrame_127;
    }

    /**
     * Returns webElement frmStudioWidgetPane
     * 
     * @return frmStudioWidgetPane
     */
    public WebElement getFrmStudioWidgetPane() {
        return frmStudioWidgetPane;

    }

    /**
     * Returns the webElement frmcsSideBarBody
     * 
     * @return frmcsSideBarBody
     */
    public WebElement getFrmcsSideBarBody() {
        return frmcsSideBarBody;
    }

    /**
     * Returns the webElement frmCsPortalGuiPaneContent
     * 
     * @return frmCsPortalGuiPaneContent
     */
    public WebElement getFrmCsPortalGuiPaneContent() {
        return frmCsPortalGuiPaneContent;
    }

    /**
     * Returns the webElement frmDerivationsTree
     * 
     * @return frmDerivationsTree
     */
    public WebElement getFrmDerivationsTree() {
        return frmDerivationsTree;
    }

    /**
     * Returns the webElement frmDerivationsView
     * 
     * @return frmDerivationsView
     */
    public WebElement getFrmDerivationsView() {
        return frmDerivationsView;
    }

    /**
     * Returns the webElement frmSplitAreaFrameFrmCenter
     * 
     * @return frmSplitAreaFrameFrmCenter
     */
    public WebElement getFrmSplitAreaFrameFrmCenter() {
        return frmSplitAreaFrameFrmCenter;
    }

    /**
     * Returns the webElement frmColorSelectionDialog
     * 
     * @return frmColorSelectionDialog
     */
    public WebElement getFrmColorSeletionDialog() {
        return frmColorSelectionDialog;
    }

    /**
     * Returns the frame for 'report' tab
     * 
     * @return - frmReport
     */
    public WebElement getFrmReport() {
        return frmReport;
    }

    /**
     * Returns the frame for 'Script' tab
     * 
     * @return - frmPhpScript
     */
    public WebElement getFrmForPhpScript() {
        return frmPhpScript;
    }

    /**
     * Returns the webElement frmCsPortalWindowConfirmDialog
     * 
     * @return frmCsPortalWindowConfirmDialog
     */
    public WebElement getFrmCsPortalWindowConfirmDialog() {
        return frmCsPortalWindowConfirmDialog;
    }

    /**
     * Returns the webElement itemControlFrame
     * 
     * @return itemControlFrame
     */
    public WebElement getItemControlFrame() {
        return itemControlFrame;
    }

    /**
     * Returns the webElement frmViewWorkflow
     * 
     * @return frmViewWorkflow
     */
    public WebElement getFrmViewWorkflow() {
        return frmViewWorkflow;
    }

    /**
     * Returns the frame to add root folder
     * 
     * @return frmToAddRootFolder_OpenSearch
     */
    public WebElement getfrmToAddRootFolder_OpenSearch() {
        return frmToAddRootFolder_OpenSearch;
    }

    /**
     * <<<<<<< Updated upstream Returns the frame of data selection dialog in
     * favorites
     * 
     * @return frmFavoritesDataSelectionDialog
     */
    public WebElement getFrmFavoritesDataSelectionDailog() {
        return frmFavoritesDataSelectionDialog;
    }

    /**
     * Returns the webElement of ImportStagingStudioWidget frame
     * 
     * @return frmImportStagingStudioWidget
     */
    public WebElement getImportStagingStudioWidgetFrame() {
        return frmImportStagingStudioWidget;
    }

    /**
     * Returns the webElement Import Data selection dialog
     * 
     * @return frmImportRecordDialog
     */
    public WebElement getImportRecordDialog() {
        return frmImportRecordDialog;
    }

    /**
     * <<<<<<< Updated upstream <<<<<<< Updated upstream Returns the frame
     * frmFrmTop.
     * 
     * @return frmFrmTop
     */
    public WebElement getFrmTop() {
        return frmFrmTop;
    }

    /**
     * Returns the frame for file selection dialog window,while adding logo into
     * the search portal
     * 
     * @return frmFileSelectionDialog
     */
    public WebElement getFrmFileSelectionDialog() {
        return frmFileSelectionDialog;
    }

    /**
     * Returns the frame of file selection dialog main content
     * 
     * @return frmFileSelectionDialogMainContent
     */
    public WebElement getFrmFileSelectionDialogMainContent() {
        return frmFileSelectionDialogMainContent;
    }

    /**
     * Returns the frame for landing page logo selection
     * 
     * @return frmLandingPageLogoSelection
     */
    public WebElement getFrmLandingPageLogoSelection() {
        return frmLandingPageLogoSelection;
    }

    /**
     * Returns the frame of search header
     * 
     * @return frmFrame197
     */
    public WebElement getFrmFrame197() {
        return frmFrame197;
    }

    /**
     * Returns the frame 197 from search header
     * 
     * @return frmSearchFrame197
     */
    public WebElement getFrmSearchFrameWidget197() {
        return frmSearchFrameWidget197;
    }

    /**
     * Getter for frmMamNotesEditor
     * 
     * @return the frmMamNotesEditor
     */
    public WebElement getFrmMamNotesEditor() {
        return frmMamNotesEditor;
    }

    /**
     * Setter for frmMamNotesEditor
     * 
     * @param frmMamNotesEditor the frmMamNotesEditor to set
     */
    public void setFrmMamNotesEditor(WebElement frmMamNotesEditor) {
        this.frmMamNotesEditor = frmMamNotesEditor;
    }

    /**
     * Returns the WebElement frmIdRecorsMamFileLeft.
     * 
     * @return WebElement frmIdRecorsMamFileLeft.
     */
    public WebElement getFrmIdRecorsMamFileLeft() {
        return frmIdRecorsMamFileLeft;
    }

    /**
     * Returns the WebElement frmfileFrame.
     * 
     * @return WebElement frmfileFrame.
     */
    public WebElement getFrmfileFrame() {
        return frmfileFrame;
    }

    /**
     * <<<<<<< Updated upstream Getter for frmMamWhiteBoardPageTemplate
     * 
     * @return the frmMamWhiteBoardPageTemplate
     */
    public WebElement getFrmMamWhiteBoardPageTemplate() {
        return frmMamWhiteBoardPageTemplate;
    }

    /**
     * Setter for frmMamWhiteBoardPageTemplate
     * 
     * @param frmMamWhiteBoardPageTemplate the frmMamWhiteBoardPageTemplate to
     *            set
     */
    public void setFrmMamWhiteBoardPageTemplate(
            WebElement frmMamWhiteBoardPageTemplate) {
        this.frmMamWhiteBoardPageTemplate = frmMamWhiteBoardPageTemplate;
    }

    /**
     * Getter for frmMamWhiteBoardPageObjectSelection
     * 
     * @return the frmMamWhiteBoardPageObjectSelection
     */
    public WebElement getFrmMamWhiteBoardPageObjectSelection() {
        return frmMamWhiteBoardPageObjectSelection;
    }

    /**
     * Setter for frmMamWhiteBoardPageObjectSelection
     * 
     * @param frmMamWhiteBoardPageObjectSelection the
     *            frmMamWhiteBoardPageObjectSelection to set
     */
    public void setFrmMamWhiteBoardPageObjectSelection(
            WebElement frmMamWhiteBoardPageObjectSelection) {
        this.frmMamWhiteBoardPageObjectSelection = frmMamWhiteBoardPageObjectSelection;
    }

    /**
     * Returns the element frmCSPortalWindow
     * 
     * @return frmCSPortalWindow
     */
    public WebElement getFrmCSPortalWindow() {
        return frmCSPortalWindow;
    }

    /**
     * Returns the frm element for Split Area Content Center in open search
     * 
     * @return frmSplitAreaContentCenterSearchPortal
     */
    public WebElement getFrmSplitAreaContentCenterSearchPortal() {
        return frmSplitAreaContentCenterSearchPortal;
    }

    /**
     * Returns the WebElement frmIdRecordsPdmarticleStructureLeft**@return
     * 
     * @return WebElement frmIdRecordsPdmarticleStructureLeft
     */
    public WebElement getFrmIdRecordsPdmarticleStructureLeft() {
        return frmIdRecordsPdmarticleStructureLeft;
    }

    /**
     * Returns the WebElement frmChannelsMidFrame
     * 
     * @return WebElement frmChannelsMidFrame
     */
    public WebElement getFrmChannelsMidFrame() {
        return frmChannelsMidFrame;
    }

    /**
     * This method returns the frame for sub cs portal window
     * 
     * @return frmSubCSPortalWindow
     */
    public WebElement getFrmSubCSPortalWindow() {
        return frmSubCSPortalWindow;
    }

    /**
     * This method returns the frame for object folder in file selection dialog
     * 
     * @return frmObjectFolderInFileSelectionDialog
     */
    public WebElement getFrmObjectFolderInFileSelectionDialog() {
        return frmObjectFolderInFileSelectionDialog;
    }

    /**
     * This method returns the frame object center in file selection dialog
     * 
     * @return frmObjectCenterInFileSelectionDialog
     */
    public WebElement getFrmObjectCenterInFileSelectionDialog() {
        return frmObjectCenterInFileSelectionDialog;
    }

    /**
     * Returns the pop up frame.
     * 
     * @return WebElement frmCSPopupDivSubSubFrame
     */
    public WebElement getFrmCSPopupDivSubSubFrame() {
        return frmCSPopupDivSubSubFrame;
    }

    /**
     * This method returns the instance of Attribute Mapping
     * 
     * @return frmAttributeMapping
     */
    public WebElement getFrmAttributeMapping() {
        return frmAttributeMapping;
    }

    /**
     * Returns the Frame for the Administration user window
     * 
     * @return frmEdituser
     */
    public WebElement getFrmUserWindowFrame() {
        return frmEdituser;
    }
}
