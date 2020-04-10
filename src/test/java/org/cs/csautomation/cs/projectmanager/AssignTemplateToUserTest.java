/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.projectmanager;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.settings.ProjectManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies Assigning of template to
 * user .
 * 
 * @author CSAutomation Team
 *
 */
public class AssignTemplateToUserTest extends AbstractTest {

    private WebDriverWait               waitForReload;
    private FrameLocators               iframeLocatorsInstance;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private PimStudioProductsNodePage   pimStudioProductsNode;
    private CSPortalHeader              csPortalHeader;
    private IProductPopup               productPopUp;
    private IMoreOptionPopup            moreOptionPopup;
    private Actions                     actions;
    private ProjectManagerPage          projectManagerPage;
    private TraversingForSettingsModule traverseSettingModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontalInstance;
    private String                      assignTemplateToUserSheet = "AssignTemplateToUser";

    /**
     * This method verifies Assigning of template to user
     * 
     * @param templateName String object contains Template name
     * @param taskName String object contains task name
     * @param productName String object contains owner name of product
     */
    @Test(dataProvider = "AssignTemplateToUserData")
    public void testAssignTemplateToUser(String templateName, String taskName,
            String productName) {
        try {
            clkOnAdmin();
            addTemplateForNewTicket(templateName);
            csPortalHeader.clkBtnProducts(waitForReload);
            createNewProduct(productName);
            addTaskToProduct(taskName, productName);
            verifyAssignTemplate(taskName);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testAssignTemplateToUser", e);
            Assert.fail("Automation error : testAssignTemplateToUser", e);
        }
    }

    /**
     * This method verifies Assigning of template to user
     * 
     * @param taskName String object contains task name
     */
    private void verifyAssignTemplate(String taskName) {
        traverseSettingModule.traverseToTaskSection(waitForReload,
                browserDriver, iframeLocatorsInstance);
        WebElement label = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[3]"));
        if (label.getText().contains(taskName)) {
            CSLogger.info("Assign Task to user Successful");
        } else {
            CSLogger.error("Assign Task to user not Successful");
            Assert.fail("Assign Task to user not Successful");
        }
    }

    /**
     * This method Assign Task Product
     * 
     * @param taskName String object contains task name
     * @param productName String object contains owner name of product
     */
    private void addTaskToProduct(String taskName, String productName) {
        selectTaskOption(productName);
        traverseSettingModule.traverseToTaskSection(waitForReload,
                browserDriver, iframeLocatorsInstance);
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCreateNew(waitForReload);
        enterLabelToTask(taskName);
    }

    /**
     * This method enter the Label name for new Task
     * 
     * @param taskName String object contains task name
     */
    private void enterLabelToTask(String taskName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(projectManagerPage.getTxtTaskLabel()));
        projectManagerPage.getTxtTaskLabel().sendKeys(taskName);
        waitForReload.until(ExpectedConditions
                .visibilityOf(projectManagerPage.getBtnSave()));
        projectManagerPage.getBtnSave().click();
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.switchToDefaultFrame(browserDriver);
        projectManagerPage.clkOnButtonWindowClose(waitForReload);
        CSLogger.info("Entered Label to Task");
    }

    /**
     * This method Select the task option
     * 
     * @param productName String object contains owner name of product
     */
    private void selectTaskOption(String productName) {
        getProductEditWindow(productName);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontal.clkBtnShowMoreOption(waitForReload);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupTools(), browserDriver);
        moreOptionPopup.selectPopupDivSubMenu(waitForReload,
                moreOptionPopup.getcsGuiPopupSubTasks(), browserDriver);
        CSLogger.info("Select Task option");
    }

    /**
     * This method traverse to product edit window
     * 
     * @param productName String object contains owner name of product
     */
    private void getProductEditWindow(String productName) {
        clkProductFolder();
        WebElement product = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.rightClickTreeNode(waitForReload, product, browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupSubMenuEdit(), browserDriver);
        CSLogger.info("Open product edit window");
    }

    /**
     * This method creates the new product folder
     * 
     * @param nameOfProduct it contains the name of the product
     */
    public void createNewProduct(String nameOfProduct) {
        clkProductFolder();
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        CSLogger.info("Right clicked on Products folder.");
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopUp.enterValueInDialogue(waitForReload, nameOfProduct);
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getBtnCsGuiModalDialogOkButton()));
        productPopUp.getBtnCsGuiModalDialogOkButton().click();
    }

    /**
     * This method clicks on the product folder
     * 
     */
    public void clkProductFolder() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("Clicked on Products Folder.");
    }

    /**
     * This method Assign template for new ticket
     * 
     * @param templateName String object contains Template name
     */
    private void addTemplateForNewTicket(String templateName) {
        traverseToWindowContain();
        waitForReload.until(ExpectedConditions
                .visibilityOf(projectManagerPage.getBtnApplications()));
        projectManagerPage.getBtnApplications().click();
        WebElement ProjectManager = browserDriver.findElement(
                By.xpath("//div[contains(@id,'title_1ddf333c_sections::"
                        + "9eaf59b21c5c1ae8e9b5cea10f4d4b41')]/parent::div"));
        WebElement selection = browserDriver
                .findElement(By.xpath("//div[@id='title_1ddf333c_sections::"
                        + "9eaf59b21c5c1ae8e9b5cea10f4d4b41']"));
        CSUtility.tempMethodForThreadSleep(1000);
        if (ProjectManager.getAttribute("class").contains("section_closed")) {
            waitForReload.until(ExpectedConditions.visibilityOf(selection));
            selection.click();
        }
        waitForReload.until(ExpectedConditions
                .visibilityOf(projectManagerPage.getBtnAddTemplate()));
        projectManagerPage.getBtnAddTemplate().click();
        CSLogger.info("Clicked on Add Template");
        CSUtility.tempMethodForThreadSleep(1000);
        selectTemplate(templateName);
        traverseToWindowContain();
        waitForReload.until(ExpectedConditions
                .visibilityOf(projectManagerPage.getBtnSave()));
        projectManagerPage.getBtnSave().click();
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(projectManagerPage.getBtnWindowClose()));
        projectManagerPage.getBtnWindowClose().click();
    }

    /**
     * This method traverse to popup window contain
     * 
     */
    private void traverseToWindowContain() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Traverse to Window Contain");
    }

    /**
     * This method select the previously created template
     * 
     * @param templateName String object contains Template name
     */
    private void selectTemplate(String templateName) {
        traverseToTemplateWindow();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTemplatesWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsTasktemplateLeft()));
        WebElement templateElement = browserDriver
                .findElement(By.linkText(templateName));
        actions.doubleClick(templateElement).build().perform();
        traverseToTemplateWindow();
        projectManagerPage.getBtnOK().click();
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Select Template " + templateName);
    }

    /**
     * This method traverse to template window
     * 
     */
    private void traverseToTemplateWindow() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOf(browserDriver
                .findElement(By.xpath("//div[@class='CSPortalWindow']"))));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSLogger.info("Traverse to Template window");
    }

    /**
     * This method click on Admin
     * 
     */
    private void clkOnAdmin() {
        clkPortalLinkOptions();
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getcsGuiPopupAdmin()));
        productPopUp.getcsGuiPopupAdmin().click();
        CSLogger.info("Clicked on Admin");
    }

    /**
     * This method Click on Portal Link Option
     * 
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains template name, task name, product name
     * 
     * @return assignTemplateToUserSheet
     */
    @DataProvider(name = "AssignTemplateToUserData")
    public Object[] createNewTemplateTaskDataSheet() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("projectManagerAndTasks"),
                assignTemplateToUserSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 30);
        csPortalHeader = new CSPortalHeader(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        actions = new Actions(browserDriver);
        projectManagerPage = new ProjectManagerPage(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        moreOptionPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        traverseSettingModule = new TraversingForSettingsModule();
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }
}
