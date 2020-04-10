/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.WorkflowsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains test methods which defines a workflow action with
 * language dependencies,language availability and enables checkbox 'Apply
 * result state to all language versions actions.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateLanguageDependentWorkflowActionsTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private WorkflowsPage             workflowsPageInstance;
    private CSPortalWidget            csPortalWidgetInstance;
    private FrameLocators             iframeLocatorsInstance;
    private SoftAssert                softAssertion;
    private IProductPopup             productPopup;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private String                    parentWindow;
    private CSPopupDivPim             csPopupDivInstance;
    private Actions                   action;
    private String                    dataSheetName = "CreateLanguageWorkflowActions";

    /**
     * This test method creates language dependent workflow actions.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param actionComponentName String object contains the workflow action
     *            component to be selected i.e 'languages'.
     * @param workflowTabName String object +contains name of workflow tab
     * @param requiredState String object contains workflow state name.
     * @param resultState String object contains workflow state name. * @param
     * @param languages String object contains name of all languages to be
     *            tested.
     * @param configuredLanguage String object contains name of language to be
     *            configured in workflow action.
     * @param testLanguage String object contains language name not configured
     *            in workflow action.
     * @param productFolderName String object contains name of product folder.
     * @param productTabName String object contains name of product tab.
     */
    @Test(dataProvider = "languageWorkflowActionsTestData")
    public void testCreateLanguageDependentWorkflowActions(String workflowName,
            String workflowType, String workflowActionName,
            String workflowActionDescription, String actionComponentName,
            String workflowTabName, String requiredState, String resultState,
            String languages, String configuredLanguage, String testLanguage,
            String productFolderName, String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            checkWorkflowExists(workflowName, workflowType);
            checkWorkflowStateExists(workflowName, requiredState);
            checkWorkflowStateExists(workflowName, resultState);
            createWorkflowAction(workflowName, workflowType, workflowTabName,
                    workflowActionName, workflowActionDescription,
                    requiredState, resultState, actionComponentName);
            softAssertion.assertAll();
            CSLogger.info(
                    "Create Language Dependent Workflow Actions test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation error in :"
                    + " testCreateLanguageDependentWorkflowActions", e);
            Assert.fail("Automation error in : "
                    + "testCreateLanguageDependentWorkflowActions", e);
        }
    }

    /**
     * This test defines language availability and verifies execution of
     * workflow.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param actionComponentName String object contains the workflow action
     *            component to be selected i.e 'languages'.
     * @param workflowTabName String object contains name of workflow tab
     * @param requiredState String object contains workflow state name.
     * @param resultState String object contains workflow state name.
     * @param languages String object contains name of all languages to be
     *            tested.
     * @param configuredLanguage String object contains name of language to be
     *            configured in workflow action.
     * @param testLanguage String object contains language name not configured
     *            in workflow action.
     * @param productFolderName String object contains name of product folder.
     * @param productTabName String object contains name of product tab.
     */
    @Test(
            dataProvider = "languageWorkflowActionsTestData",
            dependsOnMethods = "testCreateLanguageDependentWorkflowActions")
    public void testDefineLanguageAvalibityForWorkflowAction(
            String workflowName, String workflowType, String workflowActionName,
            String workflowActionDescription, String actionComponentName,
            String workflowTabName, String requiredState, String resultState,
            String languages, String configuredLanguage, String testLanguage,
            String productFolderName, String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            String dataLanguages[] = languages.split(",");
            selectLanguageOnActionWindow(configuredLanguage);
            switchToPimAndExpandProductTree(waitForReload);
            createProductFolder(productFolderName);
            clkOnProductFolder(productFolderName);
            for (int langCount = 0; langCount < dataLanguages.length; langCount++) {
                assignStateToProductForAllLanguages(productFolderName,
                        productTabName, workflowName, requiredState,
                        dataLanguages[langCount]);
            }
            verifyAbsenseOfWorkflowAction(testLanguage, productFolderName);
            verifyExecutionOfWorkflowAction(configuredLanguage,
                    productFolderName, requiredState, resultState,
                    dataLanguages, workflowActionName, workflowName, false);
            softAssertion.assertAll();
            CSLogger.info(
                    "Define Language Avalibity For Workflow Action test completed!");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : "
                            + "testDefineLanguageAvalibityForWorkflowAction",
                    e);
            Assert.fail(
                    "Automation error in : "
                            + "testDefineLanguageAvalibityForWorkflowAction",
                    e);
        }
    }

    /**
     * This test method enable's checkbox 'Apply result state to all language
     * versions' and verifies the execution of workflow.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param actionComponentName String object contains the workflow action
     *            component to be selected i.e 'languages'.
     * @param workflowTabName String object contains name of workflow tab
     * @param requiredState String object contains workflow state name.
     * @param resultState String object contains workflow state name.
     * @param languages String object contains name of all languages to be
     *            tested.
     * @param configuredLanguage String object contains name of language to be
     *            configured in workflow action.
     * @param testLanguage String object contains language name not configured
     *            in workflow action.
     * @param productFolderName String object contains name of product folder.
     * @param productTabName String object contains name of product tab.
     */
    @Test(
            dataProvider = "languageWorkflowActionsTestData",
            dependsOnMethods = "testDefineLanguageAvalibityForWorkflowAction")
    public void testApplyResultStateToAllLanguageVersions(String workflowName,
            String workflowType, String workflowActionName,
            String workflowActionDescription, String actionComponentName,
            String workflowTabName, String requiredState, String resultState,
            String languages, String configuredLanguage, String testLanguage,
            String productFolderName, String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            String dataLanguages[] = languages.split(",");
            goToWorkflowNode();
            clkOnWorkflowType(workflowType);
            clkOnWorkflow(workflowName);
            enableApplyResultStateToAllLanguageVersionsCheckbox(workflowName,
                    requiredState, workflowActionName, actionComponentName);
            switchToPimAndExpandProductTree(waitForReload);
            clkOnProductFolder(productFolderName);
            goToProductsRightSectionWindow();
            checkOutProduct();
            for (int langCount = 0; langCount < dataLanguages.length; langCount++) {
                assignStateToProductForAllLanguages(productFolderName,
                        productTabName, workflowName, requiredState,
                        dataLanguages[langCount]);
            }
            verifyExecutionOfWorkflowAction(configuredLanguage,
                    productFolderName, requiredState, resultState,
                    dataLanguages, workflowActionName, workflowName, true);
            softAssertion.assertAll();
            CSLogger.info(
                    "Apply Result State To All Language Versions test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation error in : "
                    + "testApplyResultStateToAllLanguageVersions", e);
            Assert.fail("Automation error in : "
                    + "testApplyResultStateToAllLanguageVersions", e);
        }
    }

    /**
     * Checks whether the created workflow exists.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains type of workflow.
     */
    private void checkWorkflowExists(String workflowName,
            String workflowTypeName) {
        try {
            goToWorkflowNode();
            clkOnWorkflowType(workflowTypeName);
            clkOnWorkflow(workflowName);
            CSUtility.tempMethodForThreadSleep(1000);
            CSLogger.info("Workflow " + workflowName + " exists");
        } catch (Exception e) {
            CSLogger.error("Workflow do not exists", e);
            Assert.fail("Workflow do not exists");
        }
    }

    /**
     * This method clicks on workflow node under settings tab.
     */
    private void goToWorkflowNode() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        traverseToWorkflowTreeView();
        workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
    }

    /**
     * This method clicks on given workflow type.
     * 
     * @param workflowTypeName String object contains type of workflow.
     */
    private void clkOnWorkflowType(String workflowTypeName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowTypeName)));
        WebElement workflowType = browserDriver
                .findElement(By.linkText(workflowTypeName));
        workflowType.click();
        CSLogger.info("Clicked on workflow type : " + workflowTypeName);
    }

    /**
     * This method clicks on given workflow.
     * 
     * @param workflowName String object contains name of workflow.
     */
    private void clkOnWorkflow(String workflowName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowName)));
        WebElement workflow = browserDriver
                .findElement(By.linkText(workflowName));
        workflow.click();
        CSLogger.info("Clicked on workflow : " + workflowName);
    }

    /**
     * Verifies that workflow state are created and exists.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state.
     */
    private void checkWorkflowStateExists(String workflowName,
            String workflowStateName) {
        try {
            String workflowId = getWorkflowId(workflowName);
            clkOnWorkflowState(workflowId, workflowStateName);
        } catch (Exception e) {
            CSLogger.error("Workflow state : " + workflowStateName
                    + " does not exists", e);
            Assert.fail("Workflow state : " + workflowStateName
                    + " does not exists");
        }
    }

    /**
     * This method returns the ID of the workflow passed as argument.
     * 
     * @param workflowName String object contains name of workflow.
     * @return workflow Id String object contains workflow ID.
     */
    private String getWorkflowId(String workflowName) {
        traverseToWorkflowTreeView();
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(
                browserDriver.findElement(By.linkText(workflowName))).perform();
        traverseToWorkflowCenterPane();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + workflowName + "')]")));
        CSUtility.tempMethodForThreadSleep(5000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        return browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
    }

    /**
     * Switches frames of system preferences center pane located under setting's
     * tab.
     */
    private void traverseToWorkflowCenterPane() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * This method creates workflow action.
     * 
     * @param workflowName String object contains name of the workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowTabName String object contains name of workflow tab.
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param requiredState String object contains workflow state name.
     * @param resultState String object contains workflow state name.
     * @param actionComponentName String object contains name of action
     *            component here it is Email.
     */
    private void createWorkflowAction(String workflowName,
            String workflowTypeName, String workflowTabName,
            String workflowActionName, String workflowActionDescription,
            String requiredStateName, String resultStateName,
            String actionComponentName) {
        try {
            goToWorkflowNode();
            clkOnWorkflowType(workflowTypeName);
            clkOnWorkflow(workflowName);
            traverseToWorkflowCenterPane();
            clickOnGivenTab(workflowTabName, waitForReload);
            CSUtility.tempMethodForThreadSleep(3000);
            parentWindow = browserDriver.getWindowHandle();
            traverseToFlowChart();
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='swimlane']/div/div[contains(text(),'"
                            + workflowName + "')]")));
            CSUtility.scrollUpOrDownToElement(
                    browserDriver.findElement(By
                            .xpath("//div[@id='container-content']/table[1]")),
                    browserDriver);
            WebElement firstState = browserDriver.findElement(By.xpath(
                    "//td[contains(text(),'" + requiredStateName + "')]"));
            WebElement secondState = browserDriver.findElement(By
                    .xpath("//td[contains(text(),'" + resultStateName + "')]"));
            action.dragAndDrop(firstState, secondState).perform();
            CSLogger.info(
                    "Dragged first state and dropped it on second state ");
            String workflowActionWindow = (String) (browserDriver
                    .getWindowHandles().toArray())[1];
            browserDriver.switchTo().window(workflowActionWindow);
            configureWorkflowActions(workflowActionName,
                    workflowActionDescription, requiredStateName,
                    resultStateName, actionComponentName);
            verifyLanguagesTab(actionComponentName);
        } catch (NoSuchWindowException e) {
            CSLogger.error("A window 'Add new WorkFlow Action' didn't pop up.",
                    e);
            Assert.fail("A window 'Add new WorkFlow Action' didn't pop up.");
        }
    }

    /**
     * Traverses to ViewWorkflow frame.
     */
    private void traverseToFlowChart() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmViewWorkflow()));
        CSLogger.info("Traversed till ViewWorkflow frame ");
    }

    /**
     * Enters required workflow action details.
     * 
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param requiredStateName String object contains workflow state name.
     * @param resultStateName String object contains workflow state name.
     * @param actionComponentName String object contains name of action
     *            component here it is Email.
     */
    private void configureWorkflowActions(String workflowActionName,
            String workflowActionDescription, String requiredStateName,
            String resultStateName, String actionComponentName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[contains(@id,'ActionActionName')]")));
        WebElement actionName = browserDriver.findElement(
                By.xpath("//input[contains(@id,'ActionActionName')]"));
        WebElement actionDescription = browserDriver.findElement(
                By.xpath("//textarea[contains(@id,'ActionDescription')]"));
        enterWorkflowActionDetails(actionName, workflowActionName);
        enterWorkflowActionDetails(actionDescription,
                workflowActionDescription);
        selectActionComponent(actionComponentName);
        WebElement requiredState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionStateID')]"));
        String requiredDefaultState = getDefaultSelectedStates(requiredState);
        WebElement resultState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionResultStateID')]"));
        String resultDefaultState = getDefaultSelectedStates(resultState);
        if (requiredDefaultState.equals(requiredStateName)
                && resultDefaultState.contains(resultStateName)) {
            CSLogger.info(
                    "Required state is auto-selected as : " + requiredStateName
                            + " and Result state is  auto-selected as : "
                            + resultStateName + " teststep passed");
        } else {
            CSLogger.error(
                    "Error in teststep : Required state is auto-selected as : "
                            + requiredStateName
                            + " and Result state is auto-selected as : "
                            + resultStateName + " teststep failed");
            softAssertion.fail(
                    "Error in teststep : Required state is auto-selected as : "
                            + requiredStateName
                            + " and Result state is  auto-selected as : "
                            + resultStateName + " teststep failed");
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Workflow action configured successfully");
    }

    /**
     * This method enters workflow action information for eg. workflow action
     * name,description.
     * 
     * @param actionElement WebElement of workflow action.
     * @param actionInfo String object contains information about the workflow
     *            action.
     */
    private void enterWorkflowActionDetails(WebElement actionElement,
            String actionInfo) {
        CSUtility.waitForElementToBeClickable(waitForReload, actionElement);
        actionElement.click();
        actionElement.clear();
        actionElement.sendKeys(actionInfo);
        CSLogger.info("Entered workflow state detail as : " + actionInfo);
    }

    /**
     * Selects the action component from action window.
     * 
     * @param actionComponentName String object contains name of action
     *            component.
     */
    private void selectActionComponent(String actionComponentName) {
        WebElement option = browserDriver.findElement(By.xpath(
                "//option[contains(text(),'" + actionComponentName + "')]"));
        action.doubleClick(option).perform();
        CSLogger.info("Action component : " + actionComponentName + "selected");
    }

    /**
     * This method clicks on provided tab name.
     * 
     * @param tabName String object contains name of the tab.
     * @param waitForReload WebDriverWait object.
     */
    private void clickOnGivenTab(String tabName, WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath("//nobr[contains(text(),'"
                        + tabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By
                .xpath("//nobr[contains(text(),'" + tabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on " + tabName + " tab ");
    }

    /**
     * Returns the default selected states for given element.
     * 
     * @param element Contains WebElement of workflow state.
     * @return selected states name.
     */
    private String getDefaultSelectedStates(WebElement element) {
        Select defaultState = new Select(element);
        List<WebElement> requiredSelectedState = defaultState
                .getAllSelectedOptions();
        return requiredSelectedState.get(0).getText();
    }

    /**
     * Verifies that Languages tab exists.
     * 
     * @param workflowTabName
     */
    private void verifyLanguagesTab(String workflowTabName) {
        try {
            clickOnGivenTab(workflowTabName, waitForReload);
        } catch (Exception e) {
            CSLogger.error("Languages tab does not displayed on action window",
                    e);
            softAssertion
                    .fail("Languages tab does not displayed on action window");
        }
    }

    /**
     * Selects the language to be configured for workflow action .
     * 
     * @param language String object contains language.
     */
    private void selectLanguageOnActionWindow(String language) {
        CSUtility.waitForElementToBeClickable(waitForReload, browserDriver
                .findElement(By.id("AttributeRow_AvailableForLanguages")));
        WebElement selectedLanguage = browserDriver.findElement(
                By.xpath("//option[contains(text(),'" + language + "')]"));
        action.doubleClick(selectedLanguage).perform();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        browserDriver.close();
        browserDriver.switchTo().window(parentWindow);
        CSLogger.info("Action window closed");
    }

    /**
     * This method assigns a workflow state to a product for those languages
     * passed as argument.
     * 
     * @param productFolderName String object contains name of product folder.
     * @param productTabName String oject contains name of product tab.
     * @param workflowName String object contains name of workflow.
     * @param requiredState String object contains name of workflow state.
     * @param language String object contains name of language.
     */
    private void assignStateToProductForAllLanguages(String productFolderName,
            String productTabName, String workflowName, String requiredState,
            String language) {
        setLanguageOfProduct(productFolderName, language);
        setWorkflowStateOfProduct(productFolderName, productTabName,
                workflowName, requiredState);
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandProductTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method creates a product folder.
     * 
     * @param productName String object contains name of product folder.
     */
    private void createProductFolder(String productName) {
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered value " + productName + " in the textfield.");
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(3000);
        goToPimProductTreeSection();
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimProductTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method selects a language for product folder and if the language is
     * already selected then it clicks on refresh button.
     * 
     * @param productFolderName String object contains name of product folder.
     * @param languageName String object contains name of language.
     */
    private void setLanguageOfProduct(String productFolderName,
            String languageName) {
        goToProductsRightSectionWindow();
        csGuiToolbarHorizontalInstance.clkOnBtnChangeLanguage(waitForReload);
        csPopupDivInstance.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csPopupDivInstance.getCsGuiPopupDataLanguage());
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(csPopupDivInstance.getCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
        CSLogger.info("Switched to cs pop div sub frame");
        CSUtility.waitForElementToBeClickable(waitForReload, browserDriver
                .findElement(By.xpath("//table[@class='CSGuiPopup']")));
        WebElement table = browserDriver
                .findElement(By.xpath("//table[@class='CSGuiPopup']"));
        List<WebElement> allLanguages = table.findElements(By.tagName("tr"));
        int count = 0;
        for (WebElement language : allLanguages) {
            if (language.getText().equalsIgnoreCase(languageName)) {
                language.click();
                CSLogger.info("Language : " + languageName + " selected");
                count++;
            }
        }
        if (count == 0) {
            CSLogger.info(
                    "language : " + languageName + " is already selected ");
            goToProductsRightSectionWindow();
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarRefresh(waitForReload);
        }
    }

    /**
     * Selects the workflow state for product.
     * 
     * @param productFolderName String object contains name of product folder
     * @param productTabName String object contains product tab name.
     * @param workflowName String object contains name of the workflow.
     * @param workflowStateName String object contains name of workflow state.
     */
    private void setWorkflowStateOfProduct(String productFolderName,
            String tabName, String workflowName, String workflowStateName) {
        goToPimProductTreeSection();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(productFolderName)));
        WebElement productFolder = browserDriver
                .findElement(By.linkText(productFolderName));
        productFolder.click();
        CSLogger.info("Clicked on product folder : " + productFolderName);
        goToProductsRightSectionWindow();
        clickOnGivenTab(tabName, waitForReload);
        CSUtility.tempMethodForThreadSleep(5000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By
                        .xpath("//select[contains(@id,'PdmarticleStateID')]")));
        WebElement productState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'PdmarticleStateID')]"));
        productState.click();
        Select select = new Select(productState);
        select.selectByVisibleText(workflowName + " - " + workflowStateName);
        CSLogger.info(
                "Selected state : " + workflowName + " - " + workflowStateName);
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToProductsRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method performs check in operation on product folder if product is
     * in checkout state else no operation will be performed.
     */
    private void checkInProduct() {
        try {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCheckIn());
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCheckIn(waitForReload);
        } catch (Exception e) {
            CSLogger.info("Product already in check In state");
        }
    }

    /**
     * This method performs check out operation on product folder if product is
     * in checkout state else no operation will be performed.
     */
    private void checkOutProduct() {
        try {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontalInstance
                            .getBtnCSGuiToolbarCheckOut());
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCheckOut(waitForReload);
        } catch (Exception e) {
            CSLogger.info("Product already in check out state");
        }
    }

    /**
     * This method clicks on product folder.
     * 
     * @param productFolderName String object contains name of product folder.
     */
    private void clkOnProductFolder(String productFolderName) {
        WebElement product = getProductFolder(productFolderName);
        product.click();
        CSLogger.info("Clicked on product folder  :" + productFolderName);
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method verifies that for a language not configured in workflow
     * action,when right clicked on product folder the workflow action popup
     * does not appears for the given language.
     * 
     * @param language String object contains name of language.
     * @param productFolderName String object contains name of product folder.
     */
    private void verifyAbsenseOfWorkflowAction(String language,
            String productFolderName) {
        goToProductsRightSectionWindow();
        csGuiToolbarHorizontalInstance.clkOnBtnChangeLanguage(waitForReload);
        csPopupDivInstance.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csPopupDivInstance.getCsGuiPopupDataLanguage());
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(csPopupDivInstance.getCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
        CSLogger.info("Switched to cs pop div sub frame");
        WebElement dataLanguage = browserDriver.findElement(
                By.xpath("//td[@title='" + language + "']/parent::tr"));
        csPopupDivInstance.selectPopupDivSubMenu(waitForReload, dataLanguage,
                browserDriver);
        CSLogger.info("Clicked on data language " + language);
        goToPimProductTreeSection();
        WebElement product = getProductFolder(productFolderName);
        CSUtility.rightClickTreeNode(waitForReload, product, browserDriver);
        csPopupDivInstance.traverseToCsPopupDivFrame(waitForReload,
                browserDriver);
        int workflowActionExists = browserDriver
                .findElements(
                        By.xpath("//td[@title='" + language + "']/parent::tr"))
                .size();
        if (workflowActionExists != 0) {
            CSLogger.error(
                    "Workflow action is available for other languages :  "
                            + "teststep failed");
            softAssertion
                    .fail("Workflow action is available for other languages :  "
                            + "teststep failed");
        } else {
            CSLogger.info(
                    "Workflow action is not available for other languages :  "
                            + "teststep passed");
        }
        productPopup.getcsGuiPopupMenuRefresh().click();
        CSLogger.info("Clicked on refresh popup");
    }

    /**
     * Returns the WebElement of product folder.
     * 
     * @param productFolderName String object contains name of product folder.
     * @return WebElement of product folder.
     */
    private WebElement getProductFolder(String productFolderName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(productFolderName)));
        WebElement product = browserDriver
                .findElement(By.linkText(productFolderName));
        return product;
    }

    /**
     * Verifies that for language configured in workflow action,when right click
     * operation is performed on product folder if workflow action popup appears
     * returns true, else returns false.
     * 
     * @param language String object contains name of language configured in
     *            workflow action.
     * @param productFolderName String object contains name of product folder.
     * @param actionName String object contains name of workflow action.
     * @return Boolean value i.e either true or false.
     */
    private Boolean verifyPresenceOfWorkflowAction(String language,
            String productFolderName, String actionName) {
        goToProductsRightSectionWindow();
        csGuiToolbarHorizontalInstance.clkOnBtnChangeLanguage(waitForReload);
        csPopupDivInstance.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                csPopupDivInstance.getCsGuiPopupDataLanguage());
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(csPopupDivInstance.getCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
        CSLogger.info("Switched to cs pop div sub frame");
        WebElement dataLanguage = browserDriver.findElement(
                By.xpath("//td[@title='" + language + "']/parent::tr"));
        dataLanguage.click();
        CSLogger.info("Clicked on data language " + language);
        goToProductsRightSectionWindow();
        checkInProduct();
        goToPimProductTreeSection();
        WebElement product = getProductFolder(productFolderName);
        CSUtility.rightClickTreeNode(waitForReload, product, browserDriver);
        csPopupDivInstance.traverseToCsPopupDivFrame(waitForReload,
                browserDriver);
        CSUtility.waitForElementToBeClickable(waitForReload,
                productPopup.getcsGuiPopupMenuRefresh());
        int workflowActionExists = browserDriver
                .findElements(By
                        .xpath("//td[@title='" + actionName + "']/parent::tr"))
                .size();
        if (workflowActionExists != 0) {
            CSLogger.info(
                    "Workflow action is  available for configured language "
                            + language + ":  teststep passed");
            return true;
        } else {
            CSLogger.error(
                    "Workflow action is not available for configured language "
                            + language + " :  teststep failed");
            softAssertion.fail(
                    "Workflow action is not available for other configured"
                            + " language " + language + ":  teststep failed");
            return false;
        }
    }

    /**
     * Verifies the workflow execution of language configured in workflow action
     * and for all other languages.
     * 
     * @param language String object contains name of language.
     * @param productFolderNameString String object contains name of product
     *            folder.
     * @param requiredStateName String object contains name of workflow state.
     * @param resultStateName String object contains name of workflow state.
     * @param dataLanguages String array holds all languages to be tested.
     * @param actionName String object contains name of action component.
     * @param workflowName String object contains name of workflow.
     * @param allLanguageVersions Contains boolean value i.e true o false.
     */
    private void verifyExecutionOfWorkflowAction(String language,
            String productFolderName, String requiredStateName,
            String resultStateName, String[] dataLanguages, String actionName,
            String workflowName, Boolean allLanguageVersions) {
        Boolean workflowActionExists = verifyPresenceOfWorkflowAction(language,
                productFolderName, actionName);
        if (workflowActionExists) {
            WebElement configuredWorkflowAction = browserDriver.findElement(
                    By.xpath("//td[@title='" + actionName + "']/parent::tr"));
            configuredWorkflowAction.click();
            CSLogger.info(
                    "Clicked on configured workflow action : " + actionName);
            verifyConfiguredLanguageState(workflowName, resultStateName);
            if (allLanguageVersions) {
                verifyOtherLanguageState(dataLanguages, language,
                        resultStateName);
            } else {
                verifyOtherLanguageState(dataLanguages, language,
                        requiredStateName);
            }
        } else {
            productPopup.getcsGuiPopupMenuRefresh().click();
        }
    }

    /**
     * Verifies the workflow state of product when language is set to language
     * configured in workflow action.
     * 
     * @param workflowName String object contains name of workflow.
     * @param resultStateName String object contains name of workflow state.
     */
    private void verifyConfiguredLanguageState(String workflowName,
            String resultStateName) {
        goToProductsRightSectionWindow();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//span[contains(@id,'PdmarticleStateID')]")));
        CSUtility.tempMethodForThreadSleep(3000);
        String workflowState = browserDriver
                .findElement(
                        By.xpath("//span[contains(@id,'PdmarticleStateID')]"))
                .getText();
        if (workflowState.contains(workflowName + " - " + resultStateName)) {
            CSLogger.info("Product is in state " + resultStateName
                    + " after execution workflow execution: teststep passed");
        } else {
            CSLogger.error("Product is not in state " + resultStateName
                    + " after workflow execution : teststep failed");
            softAssertion.fail("Product is not in state " + resultStateName
                    + " after  workflow execution : teststep failed");
        }
    }

    /**
     * Verifies the workflow state of product for all languages other then
     * configured language in workflow action.
     * 
     * @param dataLanguages String array contains languages not configured in
     *            workflow action.
     * @param configuredLanguage String object contains language configured in
     *            workflow action.
     * @param requiredState String object contains name of workflow state.
     */
    private void verifyOtherLanguageState(String[] dataLanguages,
            String configuredLanguage, String requiredState) {
        for (int langCount = 0; langCount < dataLanguages.length; langCount++) {
            if (!(dataLanguages[langCount].equals(configuredLanguage))) {
                csGuiToolbarHorizontalInstance
                        .clkOnBtnChangeLanguage(waitForReload);

                csPopupDivInstance.hoverOnCsGuiPopupMenu(browserDriver,
                        waitForReload,
                        csPopupDivInstance.getCsGuiPopupDataLanguage());
                CSUtility.switchToDefaultFrame(browserDriver);
                waitForReload.until(ExpectedConditions.elementToBeClickable(
                        csPopupDivInstance.getCsPopupDivSub()));
                waitForReload.until(ExpectedConditions
                        .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                                .getFrmCsPopupDivSubFrame()));
                CSLogger.info("Switched to cs pop div sub frame");
                WebElement dataLanguage = browserDriver
                        .findElement(By.xpath("//td[@title='"
                                + dataLanguages[langCount] + "']/parent::tr"));
                dataLanguage.click();
                CSLogger.info(
                        "Clicked on data language " + dataLanguages[langCount]);
                goToProductsRightSectionWindow();
                CSUtility.waitForElementToBeClickable(waitForReload,
                        browserDriver.findElement(By.xpath(
                                "//span[contains(@id,'PdmarticleStateID')]")));
                String workflowState = browserDriver.findElement(By.xpath(
                        "//span[contains(@id,'PdmarticleStateID'" + ")]"))
                        .getText();
                if (workflowState.contains(requiredState)) {
                    CSLogger.info(
                            "After workflow action execution when data language"
                                    + " is set to " + dataLanguages[langCount]
                                    + "product is in state " + requiredState
                                    + ": teststep passed");
                } else {
                    CSLogger.error(
                            "After workflow action execution when data language "
                                    + "is set to " + dataLanguages[langCount]
                                    + " product " + "is not in state "
                                    + requiredState + " : teststep failed");
                    softAssertion
                            .fail("After workflow action execution when data "
                                    + "language is set to "
                                    + dataLanguages[langCount] + " product "
                                    + "is not in state " + requiredState
                                    + " : teststep failed");
                }
            }
        }
    }

    /**
     * Enables the 'Apply Result State To All Language Versions' checkbox, if it
     * is not already enabled.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state.
     * @param actionName String object contains name of workflow action.
     * @param actionComponentName String object contains name of action
     *            component.
     */
    private void enableApplyResultStateToAllLanguageVersionsCheckbox(
            String workflowName, String workflowStateName, String actionName,
            String actionComponentName) {
        String workflowId = getWorkflowId(workflowName);
        clkOnWorkflowState(workflowId, workflowStateName);
        String workflowDiv = "//span[contains(@id,'0divWorkflows@Workflow_"
                + workflowId + "')]/span/span/span";
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(
                        By.xpath(workflowDiv + "/table/tbody/tr/td/span/"
                                + "table/tbody/tr/td[2]/a/span/span[contains"
                                + "(text(),'" + actionName + "')]")));
        WebElement configuredWorkflowAction = browserDriver.findElement(
                By.xpath(workflowDiv + "/table/tbody/tr/td/span/table/tbody"
                        + "/tr/td[2]/a/span/span[contains(text(),'" + actionName
                        + "')]"));
        configuredWorkflowAction.click();
        CSLogger.info("Clicked on workflow action : " + actionName);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(workflowDiv
                        + "/span/span/table/tbody/tr/"
                        + "td/span/table/tbody/tr/td[2]/a/span/span"
                        + "[contains(text(),'" + actionComponentName + "')]")));
        WebElement actionComponent = browserDriver.findElement(By
                .xpath(workflowDiv + "/span/span/table/tbody/tr/td/span/table/"
                        + "tbody/tr/td[2]/a/span/span[contains(text(),'"
                        + actionComponentName + "')]"));
        actionComponent.click();
        CSLogger.info("Clicked on action component : " + actionComponentName);
        traverseToWorkflowCenterPane();
        clickOnGivenTab(actionComponentName, waitForReload);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(@id,'ActionResultStateToAllLanguages_"
                                + "GUI')]")));
        WebElement applyResultCheckBox = browserDriver.findElement(By.xpath(
                "//span[contains(@id,'ActionResultStateToAllLanguages_GUI')]"));
        if (applyResultCheckBox.getAttribute("class").contains("Off")) {
            applyResultCheckBox.click();
            CSLogger.info(
                    "Clicked on 'Apply result state to all language versions' "
                            + "checkbox");
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
        } else {
            CSLogger.info(
                    " 'Apply result state to all language versions' checkbox is"
                            + " already ON");
        }
    }

    /**
     * This methods clicks on given workflow state.
     * 
     * @param workflowId String object contains Id of workflow.
     * @param workflowStateName String object contains workflow state name.
     */
    private void clkOnWorkflowState(String workflowId,
            String workflowStateName) {
        traverseToWorkflowTreeView();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[@id='0divWorkflows@Workflow_" + workflowId
                                + "']/span/table/tbody/tr/td/span/table/tbody"
                                + "/tr/td[2]/a/span/span[contains(text(),'"
                                + workflowStateName + "')]")));
        WebElement workflowState = browserDriver.findElement(
                By.xpath("//span[@id='0divWorkflows@Workflow_" + workflowId
                        + "']/span/table/tbody/tr/td/span/table/tbody/tr/td[2]"
                        + "/a/span/span[contains(text(),'" + workflowStateName
                        + "')]"));
        workflowState.click();
        CSLogger.info("Clicked on workflow State : " + workflowStateName);
    }

    /**
     * Switches frames of system preferences left pane tree located under
     * setting's tab.
     */
    private void traverseToWorkflowTreeView() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * This is a data provider method.
     * 
     * @return Array
     */
    @DataProvider(name = "languageWorkflowActionsTestData")
    public Object[][] languageWorkflowActionsData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("workflowModuleTestCases"),
                dataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        workflowsPageInstance = SettingsLeftSectionMenubar
                .getWorkflowsNode(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        softAssertion = new SoftAssert();
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        csPopupDivInstance = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        action = new Actions(browserDriver);
    }
}
