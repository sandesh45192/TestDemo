/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import java.nio.file.WatchEvent;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains xpaths related to Settings Page
 * 
 * @author CSAutomation Team
 *
 */
public class SettingsPage {

    private WebDriver browserDriverInstance;

    public SettingsPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized Settings node page POM elements");
    }

    @FindBy(id = "Options@0")
    private WebElement btnSettingsNode;

    @FindBy(xpath = "//span[contains(text(),' CORE')]/../..")
    private WebElement btnCore;

    @FindBy(xpath = "//span[contains(text(),'Usages')]")
    private WebElement btnUsages;

    @FindBy(xpath = "//span[contains(@id,'Optionproxysystem:pdm_Usages_GUI')]")
    private WebElement cbPimStudioUsageButton;

    @FindBy(xpath = "//span[contains(@id,'Optionproxysystem:mam_Usages_GUI')]")
    private WebElement cbMamStudioUsageButton;

    @FindBy(xpath = "//span[contains(text(),' PIM')]")
    private WebElement btnPim;

    @FindBy(
            xpath = "//img[contains(@src,'pim.studio.png')]/following-sibling"
                    + "::span/span")
    private WebElement btnPimSubTreeStudio;

    @FindBy(id = "15a638d1fb64ae02059039ac90359e2f")
    private WebElement btnHtmlEditorSettings;

    @FindBy(
            xpath = "//tr[@cs_name='Show extended Drag and Drop Dialog']"
                    + "/td[2]/span")
    private WebElement cbExtendedDragDrop;

    @FindBy(xpath = "//div[contains(text(),' Tree')]")
    private WebElement drpDwnUserInterfaceTree;

    @FindBy(xpath = "//span[contains(text(),'Email')]")
    private WebElement btnCoreEmail;

    @FindBy(xpath = "//select[contains(@id,'Optionproxypages:Messeging')]")
    private WebElement drpDwnEmailMode;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxysystem:PasswordChars_laz_GUI')]")
    private WebElement lowerCasePasswordCheckbox;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxysystem:PasswordChars_uAZ_GUI')]")
    private WebElement upperCasePasswordCheckbox;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxysystem:PasswordChars_09_GUI')]")
    private WebElement passwordNumbersCheckbox;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxysystem:PasswordChars_SC_GUI')]")
    private WebElement passwordSpecialCharCheckbox;

    @FindBy(
            xpath = "//textarea[contains(@name,'Optionproxysystem:PasswordPatternMessage')]")
    private WebElement txtPasswordPatternMessage;

    @FindBy(xpath = "//a/img[contains(@src,'save.gif')]")
    private WebElement btnSave;

    @FindBy(xpath = "//span[@id='2f61a00ac52e680d318bdfb2f36397db']")
    private WebElement btnAuthentication;

    @FindBy(
            xpath = "//input[contains(@name,'Optionproxysystem:PasswordLengthMin')]")
    private WebElement txtMinPasswordLength;

    @FindBy(
            xpath = "//input[contains(@name,'Optionproxysystem:PasswordLengthMax')]")
    private WebElement txtMaxPasswordLength;

    @FindBy(
            xpath = "(//table[@class='treeline']/tbody/tr/td[2]/a/span/span)[2]")
    private WebElement btnPortal;

    @FindBy(
            xpath = "//input[contains(@id,'Optionproxyportal:RegistrationNotificationEmail')]")
    private WebElement txtRegistrationNotificationEmail;

    @FindBy(
            xpath = "//input[contains(@id,'Optionproxyportal:RegistrationNotificationEmail')]")
    private WebElement txtRegistrationEmail;

    @FindBy(xpath = "(//div[@id='CSPortalWidgetTitlebar_127']//div)[2]")
    private WebElement widgetBtnBar;

    @FindBy(xpath = "(//div[@id='CSPortalWidgetTitlebar_127']//div[2]//div)[3]")
    private WebElement btnWidgetPortal;

    @FindBy(xpath = "//span[contains(text(),' Data Flow')]/parent::span/..")
    private WebElement btnDataFlow;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxydataflow:ShowTransfor"
                    + "mationMappings_GUI')]")
    private WebElement cbDataFlowMapping;

    @FindBy(
            xpath = "//div[@id='title_15a638d1fb64ae02059039ac90359e2f_sections::List']")
    private WebElement secPimListSection;

    @FindBy(
            xpath = "//div[@cs_name='PIM/Studio/HTML Editor Settings']/descendant::tr[@id='AttributeRow_pdm:switchToNewList']/td[2]/span")
    private WebElement cbPimNewStudioList;

    @FindBy(xpath = "//input[contains(@id,'CometsharepointSharepointName')]")
    private WebElement txtSharePointName;

    @FindBy(xpath = "//select[contains(@id,'CometsharepointConnectorPlugin')]")
    private WebElement drpDwnConnectorPlugin;

    @FindBy(xpath = "//span[contains(@id,'CometsharepointShowInIDE_GUI')]")
    private WebElement chkBoxPlanningView;

    @FindBy(
            xpath = "//span[contains(@id,'CometsharepointHasPlaceholders_GUI')]")
    private WebElement chkBoxPlaceHolder;

    @FindBy(xpath = "//span[contains(@id,'CometsharepointHasData_GUI')]")
    private WebElement chkBoxProductsPool;

    @FindBy(xpath = "//span[contains(text(),'Email & Messages')]")
    private WebElement btnEmailAndMessages;

    @FindBy(
            xpath = "//span[contains(@id,'Optionproxyportal:SelfRegistrationHideLink_GUI')]")
    private WebElement hideRegistrationLinkCheckbox;

    @FindBy(xpath = "//span[@id='User@0']")
    private WebElement usersMenuInLeftPane;

    @FindBy(xpath = "//span[contains(@id,'CometsharepointHasDocuments_GUI')]")
    private WebElement chkBoxPublicationPool;

    @FindBy(
            xpath = "//div[contains(@id,'Cometsharepoint_DataIDs_csReferenceDiv')]//img")
    private WebElement btnAddProducts;

    @FindBy(xpath = "//span[@id='Shares@0']")
    private WebElement nodeShares;

    @FindBy(xpath = "//span[@id='Shares~Print Shares@0']")
    private WebElement nodePrintShares;

    /**
     * This method returns the element of widget portal Button
     * 
     * @return btnWidgetPortal
     */
    public WebElement getBtnWidgetPortal() {
        return btnWidgetPortal;
    }

    /**
     * This method returns the element of widget Button Bar
     * 
     * @return widgetBtnBar
     */
    public WebElement getWidgetBtnBar() {
        return widgetBtnBar;
    }

    /**
     * This method returns the instance of users menu in left pane
     * 
     * @return usersMenuInLeftPane
     */
    public WebElement getUsersMenuInLeftPane() {
        return usersMenuInLeftPane;
    }

    /**
     * This method returns the instance of hide registration link checkbox
     * 
     * @return hideRegistrationLinkCheckbox
     */
    public WebElement getHideRegistrationLinkCheckbox() {
        return hideRegistrationLinkCheckbox;
    }

    /**
     * This method returns the instance of emails and messages under core button
     * 
     * @return btnEmailAndMessages
     */
    public WebElement getBtnEmailAndMessages() {
        return btnEmailAndMessages;
    }

    /**
     * This method returns the instance of Registration email
     * 
     * @return txtRegistrationEmail
     */
    public WebElement getTxtRegistrationEmail() {
        return txtRegistrationEmail;
    }

    /**
     * This method returns the instance of registration notification email
     * 
     * @return txtRegistrationNotificationEmail
     */
    public WebElement getTxtRegistrationNotificationEmail() {
        return txtRegistrationNotificationEmail;
    }

    /**
     * This method returns the instance of btn portal
     * 
     * @return btnPortal
     */
    public WebElement getBtnPortal() {
        return btnPortal;
    }

    /**
     * This method returns the instance of text field for maximum password
     * length
     * 
     * @return txtMaxPasswordLength
     */
    public WebElement getTxtMaxPasswordLength() {
        return txtMaxPasswordLength;
    }

    /**
     * This method returns the instance of txtfield for minimum password length
     * 
     * @return txtMinPasswordLength
     */
    public WebElement getTxtMinPasswordLength() {
        return txtMinPasswordLength;
    }

    /**
     * This method returns the instance of Authentication Node
     * 
     * @return
     */
    public WebElement getBtnAuthentication() {
        return btnAuthentication;
    }

    /**
     * This method clicks on Authentication button
     * 
     * @param waitForReload waits for an element to reload
     */
    public void clkBtnAuthentication(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnAuthentication());
        getBtnAuthentication().click();
        CSLogger.info("Clicked on Authentication Node");
    }

    /**
     * This method returns the instance of Save button
     * 
     * @return btnSave
     */
    public WebElement getBtnSave() {
        return btnSave;
    }

    /**
     * This method returns the instance of password pattern message
     * 
     * @return txtPasswordPatternMessage
     */
    public WebElement getTxtPasswordPatternMessage() {
        return txtPasswordPatternMessage;
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * This method returns the instance of checkbox for special character field
     * 
     * @return passwordSpecialCharCheckbox
     */
    public WebElement getPasswordSpecialCharCheckbox() {
        return passwordSpecialCharCheckbox;
    }

    /**
     * This method returns the instance of checkbox for password numbers
     * 
     * @return passwordNumbersCheckbox
     */
    public WebElement getPasswordNumbersCheckbox() {
        return passwordNumbersCheckbox;
    }

    /**
     * This method returns the instance of checkbox for upper case password
     * 
     * @return upperCasePasswordCheckbox
     */
    public WebElement getUpperCasePasswordCheckbox() {
        return upperCasePasswordCheckbox;
    }

    /**
     * This method returns the instance of checkbox for lower case password
     * 
     * @return lowerCasePasswordCheckbox
     */
    public WebElement getLowerCasePasswordCheckbox() {
        return lowerCasePasswordCheckbox;
    }

    /**
     * Returns the WebElement btnSettingsNode
     * 
     * @return btnSettingsNode
     */
    public WebElement getBtnSettingsNode() {
        return btnSettingsNode;
    }

    /**
     * Returns the WebElement btnCore
     * 
     * @return btnCore
     */
    public WebElement getBtnCore() {
        return btnCore;
    }

    /**
     * Returns the WebElement btnUsages
     * 
     * @return btnUsages
     */
    public WebElement getBtnUsages() {
        return btnUsages;
    }

    /**
     * Returns the WebElement cbPimStudioUsageButton
     * 
     * @return cbPimStudioUsageButton
     */
    public WebElement getcbPimStudioUsageButton() {
        return cbPimStudioUsageButton;
    }

    /**
     * Returns the WebElement cbMamStudioUsageButton
     * 
     * @return cbMamStudioUsageButton
     */
    public WebElement getcbMamStudioUsageButton() {
        return cbMamStudioUsageButton;
    }

    /**
     * Returns the WebElement btnPim
     * 
     * @return btnPim
     */
    public WebElement getBtnPim() {
        return btnPim;
    }

    /**
     * Returns the WebElement btnPimSubTreeStudio
     * 
     * @return btnPimSubTreeStudio
     */
    public WebElement getBtnPimSubTreeStudio() {
        return btnPimSubTreeStudio;
    }

    /**
     * Returns the WebElement btnPimUserInterface
     * 
     * @return btnPimUserInterface
     */
    public WebElement getBtnPimUserInterface() {
        return btnHtmlEditorSettings;
    }

    /**
     * Returns the checkbox WebElement cbExtendedDragDrop
     * 
     * @return cbExtendedDragDrop
     */
    public WebElement getCbExtendedDragDrop() {
        return cbExtendedDragDrop;
    }

    /**
     * Returns the checkbox WebElement cbExtendedDragDrop
     * 
     * @return cbExtendedDragDrop
     */
    public WebElement getDrpDwnUserInterfaceTree() {
        return drpDwnUserInterfaceTree;
    }

    /**
     * Returns the Email & Messages button located under CORE tree on settings
     * page.
     * 
     * @return WebElement btnCoreEmail.
     */
    public WebElement getBtnCoreEmail() {
        return btnCoreEmail;
    }

    /**
     * Returns the Email & Messages Mode drop down WebElement located under CORE
     * tree on settings page.
     * 
     * @return
     */
    public WebElement getDrpDwnEmailMode() {
        return drpDwnEmailMode;
    }

    /**
     * Clicks on settings button in settings tab
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnSettingsNode(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnSettingsNode());
        getBtnSettingsNode().click();
        CSLogger.info("Clicked on settings tab's setting node");
    }

    /**
     * Clicks on right side core button from setting node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnCore(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnCore());
        getBtnCore().click();
        CSLogger.info("Clicked on Core tree of Settings Node.");
    }

    /**
     * Clicks on usages from core tree
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnUsages(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnUsages());
        getBtnUsages().click();
        CSLogger.info("Clicked on core tree's usages ");
    }

    /**
     * This method enables PimStudioUsage button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void checkPimStudioUsageButton(WebDriverWait waitForReload) {
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnUsages());
        if ((getcbPimStudioUsageButton().getAttribute("class").equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox"))
                || getcbPimStudioUsageButton().getAttribute("class").equals(
                        "CSGuiEditorCheckboxContainer Enabled GuiEditorCheckbox Off")) {
            getcbPimStudioUsageButton().click();
            CSLogger.info("Checked PimStudioUsage Button ");
        } else {
            CSLogger.info("PimStudioUsage Button is already ON(checked)");
        }
    }

    /**
     * This method enables MamStudioUsage button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void checkMamStudioUsageButton(WebDriverWait waitForReload) {
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnUsages());
        if ((getcbMamStudioUsageButton().getAttribute("class").equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox"))
                || getcbMamStudioUsageButton().getAttribute("class").equals(
                        "CSGuiEditorCheckboxContainer Enabled GuiEditorCheckbox Off")) {
            getcbMamStudioUsageButton().click();
            CSLogger.info("Checked MamStudioUsage Button ");
        } else {
            CSLogger.info("MamStudioUsage Button is already ON(checked)");
        }
    }

    /**
     * Clicks on right side PIM button from setting node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnPim(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, getBtnPim());
        getBtnPim().click();
        CSLogger.info("Clicked on setting node's PIM tree");
    }

    /**
     * Clicks on right side PIM button from setting node
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnPimSubTreeStudio(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnPimSubTreeStudio());
        getBtnPimSubTreeStudio().click();
        CSLogger.info("Clicked on setting node PIM's sub tree studio");
    }

    /**
     * Clicks on Pim's sub tree studio user interface
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnPimUserInterface(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnPimUserInterface());
        getBtnPimUserInterface().click();
        CSLogger.info("Clicked on Pim's sub tree studio user interface");
    }

    /**
     * This method enables PimStudioUsage button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void checkExtendedDragDrop(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getDrpDwnUserInterfaceTree());
        if ((getCbExtendedDragDrop().getAttribute("class").equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox"))
                || getCbExtendedDragDrop().getAttribute("class").equals(
                        "CSGuiEditorCheckboxContainer Enabled GuiEditorCheckbox Off")) {
            getCbExtendedDragDrop().click();
            CSLogger.info("Checked Extended Drag Drop option ");
        } else {
            CSLogger.info(
                    "The checkbox of  Extended Drag Drop is already ON(checked)");
        }
    }

    /**
     * This method expand's User Interface Tree node.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void expandDrpDwnUserInterfaceTree(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getDrpDwnUserInterfaceTree());
        getDrpDwnUserInterfaceTree().click();
        CSLogger.info("Clicked on Drop Down UserInterface' Tree");
    }

    /**
     * Clicked on Email & Messages button located under CORE tree on settings
     * page.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnCoreEmail(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, getBtnCoreEmail());
        getBtnCoreEmail().click();
        CSLogger.info(
                "Clicked on Email & Messages button located under CORE tree on"
                        + " settings page");
    }

    /**
     * Returns the Data Flow WebElement located under CORE.
     * 
     * @return WebElement btnDataFlow
     */
    public WebElement getBtnDataFlow() {
        return btnDataFlow;
    }

    /**
     * Clicked on 'Data Flow' button located under CORE tree on settings page.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void clkOnBtnDataFlow(WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, getBtnDataFlow());
        getBtnDataFlow().click();
        CSLogger.info("Clicked on Data Flow button located under CORE tree on "
                + "settings page");
    }

    /**
     * Returns the check box WebElement of 'Display the Data Flow transformation
     * mapping'.
     * 
     * @return WebElement cbDataFlowMapping.
     */
    public WebElement getCbDataFlowMapping() {
        return cbDataFlowMapping;
    }

    /**
     * Clicks on check box 'Display the Data Flow transformation mapping'.
     * 
     * @param waitForReload WebDriverWait object
     */
    public void checkDataFlowMapping(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getCbDataFlowMapping());
        if (getCbDataFlowMapping().getAttribute("class").contains("On")) {
            CSLogger.info(
                    "Checkbox 'Display the Data Flow transformation mapping' is already ON");
        } else {
            getCbDataFlowMapping().click();
            CSLogger.info(
                    "Checked 'Display the Data Flow transformation mapping' checkbox");
        }
    }

    /**
     * Returns the List tree element.
     * 
     * @return secPimListSection;
     */
    public WebElement getSecPimListSection() {
        return secPimListSection;
    }

    /**
     * Performs click operation on given element.
     * 
     * @param element WebElement element to be clicked
     * @param waitForReload WebDriverWait object
     * @param label String describes element.
     */
    public void clkOnGivenElement(WebElement element,
            WebDriverWait waitForReload, String label) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        CSLogger.info("Clicked on element " + label);
    }

    /**
     * Expands the given element
     * 
     * @param element WebElement element on which operation will be performed.
     * @param waitForReload WebDriverWait object
     */
    public void expandGivenSection(WebElement element,
            WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        CSLogger.info("Expanded element");
    }

    /**
     * Enable or disables a given check box element
     * 
     * @param element WebElement check box element that will be enabled or
     *            disable.
     * @param waitForReload WebDriverWait object
     * @param enable Boolean parameter contains true or false value specifying
     *            whether to enable the check box or not.
     */
    public void toggleGivenCheckboxElement(WebElement element,
            WebDriverWait waitForReload, Boolean enable) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        String status = element.getAttribute("class");
        if (enable) {
            if (status.contains("Off")) {
                element.click();
                CSLogger.info("Enabled checkbox");
            } else {
                CSLogger.info("Checkbox is already enabled");
            }
        } else {
            if (status.contains("On")) {
                element.click();
                CSLogger.info("Disabled checkbox");
            } else {
                CSLogger.info("Checkbox is already disabled");
            }
        }
    }

    /**
     * Returns the checkbox element of 'Use New Studio List' of PIM tree
     * 
     * @return WebElement cbPimNewStudioList
     */
    public WebElement getCbPimNewStudioList() {
        return cbPimNewStudioList;
    }

    /**
     * This method returns the instance of text Share Point Name
     * 
     * @return txtSharePointName
     */
    public WebElement getTxtSharePointName() {
        return txtSharePointName;
    }

    /**
     * This generic method will send text to web element passed as an argument
     * 
     * @param locator - WebElement instance - locator of element on which we
     *            want to perform click action
     * 
     * @param textData string object contains text data to send
     */
    public void sendTxtToWebElement(WebDriverWait waitForReload,
            WebElement locator, String textData) {
        CSUtility.waitForVisibilityOfElement(waitForReload, locator);
        locator.sendKeys(textData);
    }

    /**
     * Returns the checkbox WebElement Connector Plugin
     * 
     * @return drpDwnConnectorPlugin
     */
    public WebElement getDrpDwnConnectorPlugin() {
        return drpDwnConnectorPlugin;
    }

    /**
     * Returns the WebElement Planning View
     * 
     * @return chkBoxPlanningView
     */
    public WebElement getChkBoxPlanningView() {
        return chkBoxPlanningView;
    }

    /**
     * This generic method will send text to web element passed as an argument
     * 
     * @param locator - WebElement instance - locator of element on which we
     *            want to perform click action
     */
    public void clkToWebElementOfChkBox(WebDriverWait waitForReload,
            WebElement locator) {
        CSUtility.waitForVisibilityOfElement(waitForReload, locator);
        if (locator.getAttribute("class").contains("Off")) {
            locator.click();
        } else {
            CSLogger.info("Button is already is On State.");
        }
    }

    /**
     * Returns the WebElement Place Holder
     * 
     * @return chkBoxPlaceHolder
     */
    public WebElement getChkBoxPlaceHolder() {
        return chkBoxPlaceHolder;
    }

    /**
     * Returns the WebElement Products Pool
     * 
     * @return chkBoxProductsPool
     */
    public WebElement getChkBoxProductsPool() {
        return chkBoxProductsPool;
    }

    /**
     * Returns the WebElement Publication Pool
     * 
     * @return chkBoxPublicationPool
     */
    public WebElement getChkBoxPublicationPool() {
        return chkBoxPublicationPool;
    }

    /**
     * Returns the button WebElement add Products
     * 
     * @return btnAddProducts
     */
    public WebElement getBtnAddProducts() {
        return btnAddProducts;
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
     * Returns the WebElement of node Shares
     * 
     * @return nodeShares
     */
    public WebElement getNodeShares() {
        return nodeShares;
    }

    /**
     * Returns the WebElement of node Print Shares
     * 
     * @return nodePrintShares
     */
    public WebElement getNodePrintShares() {
        return nodePrintShares;
    }
    
    /**
     * This method enables PimStudioUsage button
     * 
     * @param waitForReload WebDriverWait object
     */
    public void uncheckExtendedDragDrop(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getDrpDwnUserInterfaceTree());
        if ((getCbExtendedDragDrop().getAttribute("class").equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox"))
                || getCbExtendedDragDrop().getAttribute("class").equals(
                        "CSGuiEditorCheckboxContainer Enabled GuiEditorCheckbox Off")) {
            CSLogger.info(
                    "The checkbox of  Extended Drag Drop is already OFF(unchecked)");
        } else {
            getCbExtendedDragDrop().click();
            CSLogger.info("Checked Extended Drag Drop option ");

        }
    }
}
