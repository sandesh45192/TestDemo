/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IConfigPortalPopup;
import org.cs.csautomation.cs.settings.IWorkflowsNodePopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.WorkflowsPage;
import org.cs.csautomation.cs.settings.translationmanager.TranslationManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivConfigPortal;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
 * This class translates workflow , verifies outdated state , verifies request
 * confirmation state , verifies confirm state , verifies up to date state and
 * all the translations related to workflow , it's states and actions
 * 
 * @author CSAutomation Team
 *
 */
public class TranslatingWorkflowTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      translatingWorkflowSheet = "TranslatingWorkflow";
    private IConfigPortalPopup          configPortalPopup;
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private Actions                     actions;
    private ModalDialogPopupWindow      modalDialogPopupWindow;
    private IWorkflowsNodePopup         workflowsNodePopup;
    private WorkflowsPage               workflowsPageInstance;

    /**
     * This method performs the translation of Workflow and its respective
     * states and actions
     * 
     * @param workflowName contains name of the workflow
     * @param workflowType contains the type of workflow
     * @param workflowState1 contains first state of workflow
     * @param numberOfStates contains total number of states in workflow
     * @param workflowStatePosition contains workflow state position
     * @param workflowStateColor contains workflow state color
     * @param workflowStateDescription contains workflow state description
     * @param workflowActionName contains workflow state action name
     * @param workflowTabName contains workflow tab name
     * @param firstState contains first state of workflow
     * @param secondState contains second state of workflow
     * @param workflowActionDescription contains workflow actions description
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param editWorkflowName contains workflow name to be edited
     * @param translationTargetValueForEditedWorkflow contains translation
     *            target value for edited workflow
     * @param translationTargetValueForAction contains translation target value
     *            for action of workflow
     * @param translationTargetValueForState contains translation target value
     *            for state of workflow
     * @param valueOfTargetLanguage contains value for target language
     * @param valueOfSourceLanguage contains value for source language
     */
    @Test(priority = 1, dataProvider = "translatingWorkflow")
    public void testTranslatingWorkflow(String workflowName,
            String workflowType, String workflowState1, String numberOfStates,
            String workflowStatePosition, String workflowStateColor,
            String workflowStateDescription, String workflowActionName,
            String workflowTabName, String firstState, String secondState,
            String workflowActionDescription, String translationLabel,
            String sourceLang, String targetLang, String dataCollectionField,
            String editWorkflowName,
            String translationTargetValueForEditedWorkflow,
            String translationTargetValueForAction,
            String translationTargetValueForState, String valueOfTargetLanguage,
            String valueOfSourceLanguage) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            executePrerequisites(workflowName, workflowType, numberOfStates,
                    workflowState1, workflowStateColor, workflowStatePosition,
                    workflowStateDescription, workflowActionName,
                    workflowTabName, firstState, secondState,
                    workflowActionDescription);
            performUseCase(workflowName, translationLabel, sourceLang,
                    targetLang, dataCollectionField, firstState, secondState,
                    workflowActionName);
        } catch (Exception e) {
            CSLogger.debug("Translation of workflow failed", e);
            Assert.fail("Test case failed for translating workflow", e);
        }
    }

    /**
     * This method performs creation of translation job, data collection
     * operations and translations tab operations
     * 
     * @param workflowName contains name of workflow
     * @param translationJob contains name of translation job
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param firstState contains name of first state that has to be created
     *            under workflow
     * @param secondState contains name of second state that has to be created
     *            under workflow
     * @param workflowActionName contains workflow action name
     */
    private void performUseCase(String workflowName, String translationJob,
            String sourceLang, String targetLang, String dataCollectionField,
            String firstState, String secondState, String workflowActionName) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        createTranslationJob(translationJob);
        dataCollectionOperations(dataCollectionField, sourceLang, targetLang,
                workflowName);
        translationsTabOperations(workflowName, firstState, secondState,
                workflowActionName);
    }

    /**
     * This method performs translation tab operations
     * 
     * @param workflowName contains name of workflow
     * @param firstState contains name of first state that has to be created
     *            under workflow
     * @param secondState contains name of second state that has to be created
     *            under workflow
     * @param workflowActionName contains workflow action name
     */
    private void translationsTabOperations(String workflowName,
            String firstState, String secondState, String workflowActionName) {
        translatorManagerPage.clkTranslationsTab(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        verifyWorkflowEntriesTranslationsTab(workflowName, firstState,
                secondState, workflowActionName);
    }

    /**
     * This method verifies workflow entries from the translation tab
     * 
     * @param workflowName contains name of workflow
     * @param firstState contains name of first state that has to be created
     *            under workflow
     * @param secondState contains name of second state that has to be created
     *            under workflow
     * @param workflowActionName contains workflow action name
     */
    private void verifyWorkflowEntriesTranslationsTab(String workflowName,
            String firstState, String secondState, String workflowActionName) {
        int count = 0;
        List<WebElement> list = getTranslationsTabEntries();
        String[] entry = new String[list.size()];
        for (int workflowIndex = 0; workflowIndex < list
                .size(); workflowIndex++) {
            WebElement workflow = list.get(workflowIndex);
            entry[workflowIndex] = workflow.getText();
        }
        count = checkEntry(count, entry, workflowName);
        count = checkEntry(count, entry, firstState);
        count = checkEntry(count, entry, secondState);
        count = checkEntry(count, entry, workflowActionName);
        if (count >= 4) {
            CSLogger.info("All entries are present ");
        } else {
            CSLogger.error("Entries are missing");
        }
    }

    /**
     * This method checks if entries are present in the translation tab
     * 
     * @param count contains count
     * @param entry contains entry that has to check to be present or not
     * @param nameOfElement contains name of element
     * @return count
     */
    private int checkEntry(int count, String[] entry, String nameOfElement) {
        boolean status = false;
        for (int entryIndex = 0; entryIndex < entry.length; entryIndex++) {
            if (entry[entryIndex].equals(nameOfElement)) {
                count++;
                status = true;
                break;
            } else {
                status = false;
            }
        }
        if (status == true) {
            CSLogger.info(nameOfElement + " is present");
        } else {
            CSLogger.error(nameOfElement + " is not present");
        }
        return count;
    }

    /**
     * This method returns the list of all the added workflow in translation tab
     * 
     * @return list
     */
    private List<WebElement> getWorkflow() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = getAddedWorkflowTransltionsTab();
        return list;
    }

    /**
     * This method returns list of entries of workflow,states and actions from
     * translations tab
     * 
     * @return list
     */
    private List<WebElement> getTranslationsTabEntries() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = getAddedWorkflowTransltionsTab();
        return list;
    }

    /**
     * This method returns added list of workflow names to the getWorkFlow
     * method
     * 
     * @return list
     */
    private List<WebElement> getAddedWorkflowTransltionsTab() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[6]"));
        return list;
    }

    /**
     * This method selects source language
     * 
     * @param sourceLangInTranlationJob contains source language in translation
     *            tab
     */
    private void selectSourceLanguage(String sourceLangInTranlationJob) {
        Select drpdwnSourceLang = new Select(browserDriver.findElement(By.xpath(
                "//select[contains(@id,'TranslationjobSourceLangID')]")));
        drpdwnSourceLang.selectByVisibleText(sourceLangInTranlationJob);
    }

    /**
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method selects target language for translation job
     * 
     * @param targetLangInTranlationJob contains target language in translation
     *            job
     */
    private void selectTargetLanguageForTranslationJob(
            String targetLangInTranlationJob) {
        selectElement(targetLangInTranlationJob, translatorManagerPage
                .getTxtTranslationJobAvailableTargetLanguage());
    }

    /**
     * This method selects element from drop down and from text area
     * 
     * @param objectToBeTranslated contains object to be translated
     * @param element contains web element to be selected
     */
    private void selectElement(String workflowName, WebElement element) {
        try {
            boolean status = false;
            WebElement selectOption = null;
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            List<WebElement> list = element.findElements(By.tagName("option"));
            for (WebElement option : list) {
                if (option.getText().contains(workflowName)) {
                    selectOption = option;
                    status = true;
                    break;
                }
            }
            if (status == true) {
                actions.doubleClick(selectOption).build().perform();
                CSLogger.info("Double clicked on object");
            } else {
                CSLogger.error("Failed to double click ");
            }
        } catch (Exception e) {
            CSLogger.error("Could not select and double click the object");
        }
    }

    /**
     * This method performs the operations from data collection field
     * 
     * @param dataCollectionField contains name of the data collection field
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param workflowName contains name of workflow
     */
    private void dataCollectionOperations(String dataCollectionField,
            String sourceLang, String targetLang, String workflowName) {
        translatorManagerPage.clkDataCollectionTab(waitForReload);
        Select drpdwnDataCollection = new Select(browserDriver.findElement(By
                .xpath("//select[contains(@id,'TranslationjobCollectorPlugin')]")));
        drpdwnDataCollection.selectByVisibleText(dataCollectionField);
        CSUtility.tempMethodForThreadSleep(1000);
        enableCheckbox();
        selectElement(workflowName,
                translatorManagerPage.getTxtWorkflowsTabDataCollection());
        selectSourceLanguage(sourceLang);
        selectTargetLanguageForTranslationJob(targetLang);
        clkSave();
    }

    /**
     * This method enables the check box of use original value as source
     * language
     */
    private void enableCheckbox() {
        waitForReload.until(ExpectedConditions.visibilityOf(
                translatorManagerPage.getChkboxUseOriginalValueAsSourceLang()));
        String chkboxValue = translatorManagerPage
                .getChkboxUseOriginalValueAsSourceLang().getAttribute("class");
        if (chkboxValue.equals(
                "CSGuiEditorCheckboxContainer Enabled GuiEditorCheckbox Off")) {
            CSLogger.info("Checkbox is already enabled");
        } else {
            translatorManagerPage.getChkboxUseOriginalValueAsSourceLang()
                    .click();
            CSLogger.info("Checkbox has enabled.");
        }
    }

    /**
     * This method creates translation job
     * 
     * @param translationJob contains name of translation job
     */
    private void createTranslationJob(String translationJob) {
        clkTranslationJob(translationJob);
        CSUtility.tempMethodForThreadSleep(2000);
        clkCreateNew();
        setLabel(translationJob);
    }

    /**
     * This method creates translation job
     * 
     * @param translationJob contains translation job name
     */
    private void clkTranslationJob(String translationJob) {
        clkTranslationManager();
        TraversingForSettingsModule.traverseToNodesInLeftPaneTranslationManager(
                waitForReload, browserDriver);
        translatorManagerPage.clkTranslationJobNode(waitForReload);
    }

    /**
     * This method clicks on translation manager
     */
    private void clkTranslationManager() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnTranslationManagerIcon(waitForReload);
    }

    /**
     * This method sets label in the properties section
     * 
     * @param translationJob contains name of translation job
     */
    private void setLabel(String translationJob) {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkPropertiesTab(waitForReload);
        waitForReload.until(ExpectedConditions.visibilityOf(
                translatorManagerPage.getTxtTranslationJobLabel()));
        translatorManagerPage.getTxtTranslationJobLabel().clear();
        translatorManagerPage.getTxtTranslationJobLabel()
                .sendKeys(translationJob);
    }

    /**
     * This method clicks on create new option from mid pane
     */
    private void clkCreateNew() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                csGuiToolbarHorizontal.getBtnCSGuiToolbarCreateNew()));
        csGuiToolbarHorizontal.getBtnCSGuiToolbarCreateNew().click();
        CSLogger.info("Clicked on create new");
    }

    /**
     * This method executes prerequisites required to run the test cases
     * 
     * @param workflowName contains name of workflow
     * @param workflowType contains workflow type
     * @param numberOfStates contains number of states of workflow
     * @param workflowState contains workflow state
     * @param workflowStateColor contains workflow state color
     * @param workflowStatePosition contains workflow state position
     * @param workflowStateDescription contains workflow state description
     * @param workflowActionName contains workflow action name
     * @param workflowTabName contains workflow tab name
     * @param firstState contains name of first state that has to be created
     *            under workflow
     * @param secondState contains name of second state that has to be created
     *            under workflow
     * @param workflowActionDescription contains workflow action description
     */
    private void executePrerequisites(String workflowName, String workflowType,
            String numberOfStates, String workflowState,
            String workflowStateColor, String workflowStatePosition,
            String workflowStateDescription, String workflowActionName,
            String workflowTabName, String firstState, String secondState,
            String workflowActionDescription) {
        createWorkflow(workflowName, workflowType);
        createWorkflowStates(workflowName, workflowType, workflowStatePosition,
                numberOfStates, workflowState, workflowStateColor,
                workflowStateDescription);
        createWorkflowActions(workflowName, workflowType, firstState,
                secondState, workflowTabName, workflowActionName,
                workflowActionDescription);
    }

    /**
     * This method creates workflow actions
     * 
     * @param workflowName contains workflow name
     * @param workflowType contains workflow type
     * @param firstState contains name of first state that has to be created
     *            under workflow
     * @param secondState contains name of second state that has to be created
     *            under workflow
     * @param workflowTabName contains workflow tab name
     * @param workflowActionName contains workflow action name
     * @param workflowActionDescription contains workflow actions description
     */
    private void createWorkflowActions(String workflowName, String workflowType,
            String firstState, String secondState, String workflowTabName,
            String workflowActionName, String workflowActionDescription) {
        checkWorkflowExists(workflowName, workflowType);
        checkWorkflowStateExists(workflowName, firstState);
        checkWorkflowStateExists(workflowName, secondState);
        createWorkflowAction(workflowName, workflowTabName, workflowActionName,
                firstState, secondState, workflowActionDescription);
    }

    /**
     * This method creates workflow action
     * 
     * @param workflowName contains name of workflow
     * @param workflowTabName contains workflow tab name
     * @param workflowActionName contains workflow action name
     * @param firstState contains name of first state that has to be created
     *            under workflow
     * @param secondState contains name of second state that has to be created
     *            under workflow
     * @param workflowActionDescription contains workflow action description
     */
    private void createWorkflowAction(String workflowName,
            String workflowTabName, String workflowActionName,
            String firstState, String secondState,
            String workflowActionDescription) {
        try {
            clkOnWorkflow(workflowName);
            traverseToWorkflowCenterPane();
            clickOnGivenTab(workflowTabName, waitForReload);
            CSUtility.tempMethodForThreadSleep(3000);
            String parentWindow = browserDriver.getWindowHandle();
            traverseToFlowChart();
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='swimlane']/div/div[contains(text(),'"
                            + workflowName + "')]")));
            CSUtility.scrollUpOrDownToElement(
                    browserDriver.findElement(By
                            .xpath("//div[@id='container-content']/table[1]")),
                    browserDriver);
            WebElement firstStateElement = browserDriver.findElement(
                    By.xpath("//td[contains(text(),'" + firstState + "')]"));
            WebElement secondStateElement = browserDriver.findElement(
                    By.xpath("//td[contains(text(),'" + secondState + "')]"));
            actions.dragAndDrop(firstStateElement, secondStateElement)
                    .perform();
            CSLogger.info("Dragged " + firstState + " and dropped it on "
                    + secondState);
            String workflowActionWindow = (String) (browserDriver
                    .getWindowHandles().toArray())[1];
            browserDriver.switchTo().window(workflowActionWindow);
            configureWorkflowActions(workflowActionName,
                    workflowActionDescription, firstState, secondState);
            CSLogger.info("Workflow action : " + workflowActionName
                    + "created successfully ");
            browserDriver.close();
            browserDriver.switchTo().window(parentWindow);
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
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmViewWorkflow()));
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
     */
    private void configureWorkflowActions(String workflowActionName,
            String workflowActionDescription, String requiredStateName,
            String resultStateName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[contains(@id,'ActionActionName')]")));
        WebElement actionName = browserDriver.findElement(
                By.xpath("//input[contains(@id,'ActionActionName')]"));
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(
                        By.xpath("//select[contains(@id,'ActionStateID')]")));
        WebElement requiredState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionStateID')]"));
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//select[contains(@id,'ActionResultStateID')]")));
        WebElement resultState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionResultStateID')]"));
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//textarea[contains(@id,'ActionDescription')]")));
        WebElement actionDescription = browserDriver.findElement(
                By.xpath("//textarea[contains(@id,'ActionDescription')]"));
        enterWorkflowActionDetails(actionName, workflowActionName);
        enterWorkflowActionDetails(actionDescription,
                workflowActionDescription);
        String requiredDefaultState = getDefaultSelectedStates(requiredState);
        String resultDefaultState = getDefaultSelectedStates(resultState);
        if (requiredDefaultState.equals(requiredStateName)
                && resultDefaultState.contains(resultStateName)) {
            CSLogger.info("Required state is auto-selected as : "
                    + requiredStateName + "Result state is  auto-selected as : "
                    + resultStateName + "teststep passed");
        } else {
            CSLogger.error(
                    "Error in teststep : Required state is auto-selected as :  "
                            + requiredStateName
                            + "Result state is  auto-selected as : "
                            + resultStateName + "teststep failed");
            softAssert.fail(
                    "Error in teststep : Required state is auto-selected  "
                            + "as : " + requiredStateName
                            + "Result state is  auto-selected as : "
                            + resultStateName + "teststep failed");
        }
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Workflow action configured successful");
    }

    /**
     * Returns the default selected states for given element.
     * 
     * @param element Contains WebElement of workflow state.
     * @return String object containing selected states name.
     */
    private String getDefaultSelectedStates(WebElement element) {
        Select defaultState = new Select(element);
        List<WebElement> selectedState = defaultState.getAllSelectedOptions();
        return selectedState.get(0).getText();
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
        CSLogger.info("Entered workflow action detail as : " + actionInfo);
    }

    /**
     * This method clicks on provided tab name.
     * 
     * @param workflowTabName String object contains name of workflow tab name.
     * @param waitForReload WebDriverWait object.
     */
    private void clickOnGivenTab(String workflowTabName,
            WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath("//nobr[contains(text(),'"
                        + workflowTabName + "')]/parent::*")));
        WebElement element = browserDriver
                .findElement(By.xpath("//nobr[contains(text(),'"
                        + workflowTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on " + workflowTabName + " tab ");
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
                                + "']/span/table/tbody/tr/td/span/table/tbody/"
                                + "tr/td[2]/a/span/span[contains(text(),'"
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
     * This method returns the ID of the workflow passed as argument
     * 
     * @param workflowName String containing name of workflow.
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
        String workflowId = browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
        return workflowId;
    }

    /**
     * This method creates workflow states
     * 
     * @param workflowName contains workflow name
     * @param workflowType contains type of workflow
     * @param workflowStatePosition contains workflow state position
     * @param numberOfStates contains number of states
     * @param workflowState contains workflow state
     * @param workflowStateColor contains workflow state color
     * @param workflowStateDescription contains workflow state description
     */
    private void createWorkflowStates(String workflowName, String workflowType,
            String workflowStatePosition, String numberOfStates,
            String workflowState, String workflowStateColor,
            String workflowStateDescription) {
        int currentPosition = Integer.parseInt(workflowStatePosition);
        int totalStates = Integer.parseInt(numberOfStates);
        checkWorkflowExists(workflowName, workflowType);
        for (int stateCount = 1; stateCount <= totalStates; stateCount++) {
            createWorkflowState(workflowName, workflowState + stateCount,
                    workflowStateColor, Integer.toString(currentPosition),
                    workflowStateDescription, workflowType);
            currentPosition = currentPosition + 10;
        }
    }

    /**
     * This method creates and saves workflow
     * 
     * @param workflowName contains workflow name
     * @param workflowType contains workflow type
     */
    private void createWorkflow(String workflowName, String workflowType) {
        goToWorkflowNode();
        createNewWorkflow();
        configureWorkflow(workflowName, workflowType);
        saveWorkflow();
    }

    /**
     * Creates states of workflow.
     * 
     * @param workflowStateName String object contains name of the workflow.
     * @param workflowStateColor String object specifies color of states.
     * @param workflowStatePosition String object specifies position of state.
     * @param workflowStateDescription String object contains description about
     *            the state of workflow.
     */
    private void createWorkflowState(String workflowName,
            String workflowStateName, String workflowStateColor,
            String workflowStatePosition, String workflowStateDescription,
            String workflowType) {
        try {
            String parentWindow = browserDriver.getWindowHandle();
            goToWorkflowNode();
            clkOnWorkflowType(workflowType);
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.waitForElementToBeClickable(waitForReload,
                    browserDriver
                            .findElement(By.xpath("//span[contains(text(),'"
                                    + workflowName + "')]")));
            WebElement createdWorkflow = browserDriver.findElement(By
                    .xpath("//span[contains(text(),'" + workflowName + "')]"));
            waitForReload
                    .until(ExpectedConditions.visibilityOf(createdWorkflow));
            createdWorkflow.click();
            CSUtility.tempMethodForThreadSleep(2000);
            actions.doubleClick(createdWorkflow).build().perform();
            CSLogger.info("double clicked on the created workflow");
            CSUtility.tempMethodForThreadSleep(3000);
            traverseToCreateNewStateToolbarButton();
            CSUtility.tempMethodForThreadSleep(2000);
            WebElement btnPlusToAddState = browserDriver.findElement(By.xpath(
                    "(//a[@class='CSGuiToolbarHorizontalButton CSGuiToolbarHoverButton '])[3]/img"));
            CSUtility.waitForElementToBeClickable(waitForReload,
                    btnPlusToAddState);
            btnPlusToAddState.click();
            CSUtility.tempMethodForThreadSleep(2000);
            String newWorkflowStateWindow = (String) (browserDriver
                    .getWindowHandles().toArray())[1];
            browserDriver.switchTo().window(newWorkflowStateWindow);
            CSUtility.waitForElementToBeClickable(waitForReload,
                    browserDriver.findElement(By.xpath(
                            "//textarea[contains(@id,'StateDescription')]")));
            WebElement stateName = browserDriver.findElement(
                    By.xpath("//input[contains(@id,'StateStateName')]"));
            WebElement stateColor = browserDriver.findElement(
                    By.xpath("//select[contains(@id,'StateColorID')]"));
            WebElement statePosition = browserDriver.findElement(
                    By.xpath("//input[contains(@id,'StateStateType')]"));
            WebElement stateDescription = browserDriver.findElement(
                    By.xpath("//textarea[contains(@id,'StateDescription')]"));
            configureWorkflowState(workflowStateName, workflowStateColor,
                    workflowStatePosition, workflowStateDescription, stateName,
                    stateColor, statePosition, stateDescription);
            csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
            browserDriver.close();
            browserDriver.switchTo().window(parentWindow);
        } catch (NoSuchWindowException e) {
            CSLogger.error("A window 'Add new WorkFlow Action' didn't pop up.",
                    e);
            Assert.fail("A window 'Add new WorkFlow Action' didn't pop up.");
        }
    }

    /**
     * Traverses till SplitAreaFrame.
     */
    private void traverseToCreateNewStateToolbarButton() {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
        locator.switchToSplitAreaFrame(waitForReload);
    }

    /**
     * Switches frames of system preferences center pane located under setting's
     * tab.
     */
    private void traverseToWorkflowCenterPane() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
    }

    /**
     * Enters workflow state information for eg. workflow state name,start
     * position,color and description.
     * 
     * @param workflowStateName String object contains name of the workflow.
     * @param workflowStateColor String object specifies color of states.
     * @param workflowStatePosition String object specifies position of state.
     * @param workflowStateDescription String object contains description about
     *            the state of workflow.
     * @param stateName WebElement of workflow state name.
     * @param stateColor WebElement of workflow state color.
     * @param statePosition WebElement of workflow state position.
     * @param stateDescription WebElement of workflow state description.
     */
    private void configureWorkflowState(String workflowStateName,
            String workflowStateColor, String workflowStatePosition,
            String workflowStateDescription, WebElement stateName,
            WebElement stateColor, WebElement statePosition,
            WebElement stateDescription) {
        enterNewStateDetails(stateName, workflowStateName);
        selectWorkflowStateColor(stateColor, workflowStateColor);
        enterNewStateDetails(statePosition, workflowStatePosition);
        enterNewStateDetails(stateDescription, workflowStateDescription);
    }

    /**
     * Selects Workflow state color.
     * 
     * @param stateColor WebElement of workflow state color.
     * @param valueOfDrpDwn String object contains color of workflow state.
     */
    private void selectWorkflowStateColor(WebElement stateColor,
            String valueOfDrpDwn) {
        stateColor.click();
        Select element = new Select(stateColor);
        element.selectByVisibleText(valueOfDrpDwn);
        CSLogger.info("Drop down option " + valueOfDrpDwn + " selected");
    }

    /**
     * This method enters workflow state information for eg. workflow state
     * name,start position and description.
     * 
     * @param stateElement WebElement of workflow state.
     * @param stateInfo String object contains information about the workflow
     *            state.
     */
    private void enterNewStateDetails(WebElement stateElement,
            String stateInfo) {
        CSUtility.waitForElementToBeClickable(waitForReload, stateElement);
        stateElement.click();
        stateElement.clear();
        stateElement.sendKeys(stateInfo);
        CSLogger.info("Entered workflow state detail as : " + stateInfo);
    }

    /**
     * Checks whether the created workflow exists.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains type of workflow.
     */
    private void checkWorkflowExists(String workflowName, String workflowType) {
        try {
            goToWorkflowNode();
            CSUtility.rightClickTreeNode(waitForReload,
                    workflowsPageInstance.getBtnWorkflowsNode(), browserDriver);
            workflowsNodePopup.selectPopupDivMenu(waitForReload,
                    workflowsNodePopup.getCsPopupRefresh(), browserDriver);
            traverseToWorkflowTreeView();
            workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
            clkOnWorkflowType(workflowType);
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("Workflow " + workflowName + " exists");
        } catch (Exception e) {
            CSLogger.error("Workflow does not exist", e);
            Assert.fail("Workflow does not exist");
        }
    }

    /**
     * This method clicks on given workflow.
     * 
     * @param workflowName String object contains name of workflow.
     */
    private void clkOnWorkflow(String workflowName) {
        getWorkflow(workflowName).click();
        CSLogger.info("Clicked on workflow : " + workflowName);
    }

    /**
     * This method gets workflow for verification using link text
     * 
     * @param workflowName contains name of the workflow
     * @return workflowF
     */
    private WebElement getWorkflow(String workflowName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowName)));
        WebElement workflow = browserDriver
                .findElement(By.linkText(workflowName));
        return workflow;
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
     * Saves the configured workflow and workflow state.
     */
    private void saveWorkflow() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method clicks on workflow node under settings tab.
     */
    private void goToWorkflowNode() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
        traverseToWorkflowTreeView();
        workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
    }

    /**
     * Switches frames of system preferences left pane tree located under
     * setting's tab.
     */
    private void traverseToWorkflowTreeView() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
    }

    /**
     * Selects 'New Workflow' popup option by right clicking on workflow node to
     * create new workflow.
     */
    private void createNewWorkflow() {
        CSUtility.rightClickTreeNode(waitForReload,
                workflowsPageInstance.getBtnWorkflowsNode(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        workflowsNodePopup.selectPopupDivMenu(waitForReload,
                workflowsNodePopup.getCsPopupMenuNewWorkflows(), browserDriver);
    }

    /**
     * This method configures the workflow name , type and details
     * 
     * @param workflowName contains name of workflow
     * @param workflowType contains type of workflow
     */
    private void configureWorkflow(String workflowName, String workflowType) {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
        workflowsPageInstance.enterWorkflowDetails(workflowName,
                workflowsPageInstance.getTxtWorkflowName(), waitForReload);
        workflowsPageInstance.selectWorkflowType(workflowType);
        CSLogger.info("Workflow configured  successfully.");
    }

    /**
     * This method performs verification of outdated state after renaming the
     * created workflow from the translation tab
     * 
     * @param workflowName contains name of the workflow
     * @param workflowType contains the type of workflow
     * @param workflowState1 contains first state of workflow
     * @param numberOfStates contains total number of states in workflow
     * @param workflowStatePosition contains workflow state position
     * @param workflowStateColor contains workflow state color
     * @param workflowStateDescription contains workflow state description
     * @param workflowActionName contains workflow state action name
     * @param workflowTabName contains workflow tab name
     * @param firstState contains first state of workflow
     * @param secondState contains second state of workflow
     * @param workflowActionDescription contains workflow actions description
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param editWorkflowName contains workflow name to be edited
     * @param translationTargetValueForEditedWorkflow contains translation
     *            target value for edited workflow
     * @param translationTargetValueForAction contains translation target value
     *            for action of workflow
     * @param translationTargetValueForState contains translation target value
     *            for state of workflow
     * @param valueOfTargetLanguage contains value for target language
     * @param valueOfSourceLanguage contains value for source language
     */
    @Test(
            priority = 2,
            dependsOnMethods = { "testTranslatingWorkflow" },
            dataProvider = "translatingWorkflow")
    public void testVerifyOutdatedState(String workflowName,
            String workflowType, String workflowState1, String numberOfStates,
            String workflowStatePosition, String workflowStateColor,
            String workflowStateDescription, String workflowActionName,
            String workflowTabName, String firstState, String secondState,
            String workflowActionDescription, String translationLabel,
            String sourceLang, String targetLang, String dataCollectionField,
            String editWorkflowName,
            String translationTargetValueForEditedWorkflow,
            String translationTargetValueForAction,
            String translationTargetValueForState, String valueOfTargetLanguage,
            String valueOfSourceLanguage) {
        try {
            clkCreatedElement(workflowName);
            editWorkflow(workflowName, workflowType, editWorkflowName);
            goToTmsStudio(editWorkflowName, workflowName);
        } catch (Exception e) {
            CSLogger.debug("Failed to verify outdated state of workflow", e);
            Assert.fail("Test case failed for verifying outdated state", e);
        }
    }

    /**
     * This method traverses to TMS studio and verifies state change of outdated
     * entry with availability of translation text area
     * 
     * @param editWorkflowName
     * @param workflowName
     */
    private void goToTmsStudio(String editWorkflowName, String workflowName) {
        clkTranslationManager();
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        clkCreatedElement(workflowName);
        verifyStateChange(
                translatorManagerPage.getBtnVerifyOutdatedEntryTranslationTab(),
                "cancel.svg");
        availabilityOfTranslationTextAreaToEdit();
    }

    /**
     * This method checks availability of translation text area to edit
     */
    private void availabilityOfTranslationTextAreaToEdit() {
        try {
            traverseToTranslationTextArea();
            WebElement txtTranslation = getTranslationTextArea();
            if ((txtTranslation.getText()) != null) {
                CSLogger.error(
                        "Test case verification failed for outdated state");
            }
        } catch (Exception e) {
            CSLogger.info("Test case verifed for outdated state of product");
            softAssert.fail(
                    "Failed to check the availability of translation text area",
                    e);
        }
    }

    /**
     * This method gets translation text area instance
     * 
     * @return txtTranslation
     */
    private WebElement getTranslationTextArea() {
        WebElement txtTranslation = browserDriver.findElement(By.xpath(
                "//textarea[contains(@id,'TranslationmemoryTargetValue')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(txtTranslation));
        return txtTranslation;
    }

    /**
     * This method checks traverses to translation text area
     */
    private void traverseToTranslationTextArea() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
    }

    /**
     * This method verifies state change
     * 
     * @param webElement contains web element
     * @param state contains state to be verified.
     */
    private void verifyStateChange(WebElement webElement, String state) {
        traverseToTranslationsTabEntries();
        verifyStatusOfState(webElement, state);
    }

    /**
     * This method returns the list of states in translations tab
     * 
     * @return list
     */
    private List<WebElement> getStateTranslationsTab() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[contains(@class,'hidewrap CSAdminList')]/tbody/tr/td[3]"));
        return list;
    }

    /**
     * This method verifies status of state
     * 
     * @param element contains web element
     * @param string this method contains string
     */
    private void verifyStatusOfState(WebElement element, String string) {
        boolean status = false;
        List<WebElement> list = getStateTranslationsTab();
        for (int stateIndex = 0; stateIndex < list.size(); stateIndex++) {
            String chkState = getState(element);
            if (chkState.contains(string)) {
                status = true;
                break;
            } else {
                status = false;
            }
        }
        if (status == true) {
            String nameOfState = string.replaceAll(".svg", "");
            CSLogger.info("Translation state has changed in Translation tab  "
                    + nameOfState);
        } else {
            CSLogger.info("Could not change translation state ");
            softAssert.fail("Failed to verify request  state");
        }
    }

    /**
     * This method gets state for verification
     * 
     * @param element contains web element
     * @return chkState
     */
    private String getState(WebElement element) {
        String chkState = element.getAttribute("src");
        return chkState;
    }

    /**
     * This method traverses to translation tab entries
     */
    public void traverseToTranslationsTabEntries() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method edits the workflow name
     * 
     * @param workflowName contains name of workflow
     * @param workflowType contains type of workflow
     * @param editWorkflowName contains edited workflow name
     */
    private void editWorkflow(String workflowName, String workflowType,
            String editWorkflowName) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        goToWorkflowNode();
        traverseToWorkflowTreeView();
        workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
        WebElement type = getWorkflowType(workflowType);
        type.click();
        editName(workflowName, editWorkflowName);
    }

    /**
     * Returns the WebElement of workflow type.
     * 
     * @param workflowTypeName String object contains name of workflow type.
     * @return WebElement of workflow type.
     */
    private WebElement getWorkflowType(String workflowTypeName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowTypeName)));
        WebElement workflowType = browserDriver
                .findElement(By.linkText(workflowTypeName));
        return workflowType;
    }

    /**
     * This method edits name of workflow
     * 
     * @param workflowName contains name of workflow
     * @param editWorkflowName contains edited workflow name
     */
    private void editName(String workflowName, String editWorkflowName) {
        WebElement workflow = browserDriver
                .findElement(By.linkText(workflowName));
        waitForReload.until(ExpectedConditions.visibilityOf(workflow));
        actions.doubleClick(workflow).click().build().perform();
        CSLogger.info("clicked on created workflow to edit the contents");
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkPropertiesTab(waitForReload);
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
        enterWorkflowDetails(editWorkflowName,
                workflowsPageInstance.getTxtWorkflowName(), waitForReload);
        clkSave();
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method enters workflow details
     * 
     * @param workflowdetails contains workflow details
     * @param workflowElement contains workflow as web element
     * @param waitForReload waits for an element to reload
     */
    private void enterWorkflowDetails(String workflowdetails,
            WebElement workflowElement, WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload, workflowElement);
        CSUtility.tempMethodForThreadSleep(1000);
        workflowElement.sendKeys(Keys.DELETE);
        workflowElement.clear();
        workflowElement.click();
        workflowElement.sendKeys(workflowdetails);
        CSLogger.info("Entered workflow detail as : " + workflowdetails);
    }

    /**
     * This method clicks created workflow
     * 
     * @param workflowName contains name of workflow
     */
    private void clkCreatedElement(String elementName) {
        boolean status = false;
        WebElement workflow = null;
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        List<WebElement> list = getWorkflow();
        for (int workflowIndex = 0; workflowIndex < list
                .size(); workflowIndex++) {
            workflow = list.get(workflowIndex);
            if (workflow.getText().equals(elementName)) {
                status = true;
                break;
            } else {
                status = false;
            }
        }
        if (status == true) {
            workflow.click();
            CSUtility.tempMethodForThreadSleep(3000);
            CSLogger.info("Clicked on created workflow");
        }
    }

    /**
     * This method performs verification of Request confirmation state from
     * translation tab
     * 
     * @param workflowName contains name of the workflow
     * @param workflowType contains the type of workflow
     * @param workflowState1 contains first state of workflow
     * @param numberOfStates contains total number of states in workflow
     * @param workflowStatePosition contains workflow state position
     * @param workflowStateColor contains workflow state color
     * @param workflowStateDescription contains workflow state description
     * @param workflowActionName contains workflow state action name
     * @param workflowTabName contains workflow tab name
     * @param firstState contains first state of workflow
     * @param secondState contains second state of workflow
     * @param workflowActionDescription contains workflow actions description
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param editWorkflowName contains workflow name to be edited
     * @param translationTargetValueForEditedWorkflow contains translation
     *            target value for edited workflow
     * @param translationTargetValueForAction contains translation target value
     *            for action of workflow
     * @param translationTargetValueForState contains translation target value
     *            for state of workflow
     * @param valueOfTargetLanguage contains value for target language
     * @param valueOfSourceLanguage contains value for source language
     */
    @Test(
            priority = 3,
            dependsOnMethods = { "testVerifyOutdatedState" },
            dataProvider = "translatingWorkflow")
    public void testVerifyRequestConfirmationState(String workflowName,
            String workflowType, String workflowState1, String numberOfStates,
            String workflowStatePosition, String workflowStateColor,
            String workflowStateDescription, String workflowActionName,
            String workflowTabName, String firstState, String secondState,
            String workflowActionDescription, String translationLabel,
            String sourceLang, String targetLang, String dataCollectionField,
            String editWorkflowName,
            String translationTargetValueForEditedWorkflow,
            String translationTargetValueForAction,
            String translationTargetValueForState, String valueOfTargetLanguage,
            String valueOfSourceLanguage) {
        try {
            translatorManagerPage.clkBtnSaveAndLoadNextElement(waitForReload);
            setTranslationText(translationTargetValueForEditedWorkflow);
            translatorManagerPage.clkBtnRequestConfirmation(waitForReload);
            verifyStateChange(
                    translatorManagerPage
                            .getBtnVerifyRequestConfirmationTranslationsTab(),
                    "question.svg");
        } catch (Exception e) {
            CSLogger.debug(
                    "Failed to verify request confirmation state of workflow",
                    e);
            Assert.fail(
                    "Test case failed for verifying request confirmation state",
                    e);
        }
    }

    /**
     * This method performs the verification of confirm state from translations
     * tab
     * 
     * @param workflowName contains name of the workflow
     * @param workflowType contains the type of workflow
     * @param workflowState1 contains first state of workflow
     * @param numberOfStates contains total number of states in workflow
     * @param workflowStatePosition contains workflow state position
     * @param workflowStateColor contains workflow state color
     * @param workflowStateDescription contains workflow state description
     * @param workflowActionName contains workflow state action name
     * @param workflowTabName contains workflow tab name
     * @param firstState contains first state of workflow
     * @param secondState contains second state of workflow
     * @param workflowActionDescription contains workflow actions description
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param editWorkflowName contains workflow name to be edited
     * @param translationTargetValueForEditedWorkflow contains translation
     *            target value for edited workflow
     * @param translationTargetValueForAction contains translation target value
     *            for action of workflow
     * @param translationTargetValueForState contains translation target value
     *            for state of workflow
     * @param valueOfTargetLanguage contains value for target language
     * @param valueOfSourceLanguage contains value for source language
     */
    @Test(
            priority = 4,
            dependsOnMethods = { "testVerifyRequestConfirmationState" },
            dataProvider = "translatingWorkflow")
    public void testVerifyConfirmState(String workflowName, String workflowType,
            String workflowState1, String numberOfStates,
            String workflowStatePosition, String workflowStateColor,
            String workflowStateDescription, String workflowActionName,
            String workflowTabName, String firstState, String secondState,
            String workflowActionDescription, String translationLabel,
            String sourceLang, String targetLang, String dataCollectionField,
            String editWorkflowName,
            String translationTargetValueForEditedWorkflow,
            String translationTargetValueForAction,
            String translationTargetValueForState, String valueOfTargetLanguage,
            String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            setTranslationText(translationTargetValueForState);
            translatorManagerPage.clkBtnConfirm(waitForReload);
            verifyStateChange(
                    translatorManagerPage.getBtnVerifyConfirmTranslationsTab(),
                    "check.svg");
        } catch (Exception e) {
            CSLogger.debug("Failed to verify confirm state of workflow", e);
            Assert.fail("Test case failed for verifying confirm state", e);
        }
    }

    /**
     * This method sets translation text
     * 
     * @param translationTargetValue contains translation text
     */
    private void setTranslationText(String translationTargetValue) {
        WebElement txtTranslation = getTranslationTextArea();
        txtTranslation.sendKeys(translationTargetValue);
    }

    /**
     * This method performs the verification of up to date state from the
     * translation tab
     * 
     * @param workflowName contains name of the workflow
     * @param workflowType contains the type of workflow
     * @param workflowState1 contains first state of workflow
     * @param numberOfStates contains total number of states in workflow
     * @param workflowStatePosition contains workflow state position
     * @param workflowStateColor contains workflow state color
     * @param workflowStateDescription contains workflow state description
     * @param workflowActionName contains workflow state action name
     * @param workflowTabName contains workflow tab name
     * @param firstState contains first state of workflow
     * @param secondState contains second state of workflow
     * @param workflowActionDescription contains workflow actions description
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param editWorkflowName contains workflow name to be edited
     * @param translationTargetValueForEditedWorkflow contains translation
     *            target value for edited workflow
     * @param translationTargetValueForAction contains translation target value
     *            for action of workflow
     * @param translationTargetValueForState contains translation target value
     *            for state of workflow
     * @param valueOfTargetLanguage contains value for target language
     * @param valueOfSourceLanguage contains value for source language
     */
    @Test(
            priority = 5,
            dependsOnMethods = { "testVerifyConfirmState" },
            dataProvider = "translatingWorkflow")
    public void testVerifyUpToDateState(String workflowName,
            String workflowType, String workflowState1, String numberOfStates,
            String workflowStatePosition, String workflowStateColor,
            String workflowStateDescription, String workflowActionName,
            String workflowTabName, String firstState, String secondState,
            String workflowActionDescription, String translationLabel,
            String sourceLang, String targetLang, String dataCollectionField,
            String editWorkflowName,
            String translationTargetValueForEditedWorkflow,
            String translationTargetValueForAction,
            String translationTargetValueForState, String valueOfTargetLanguage,
            String valueOfSourceLanguage) {
        try {
            clkCreatedElement(workflowActionName);
            traverseToTranslationTextArea();
            setTranslationText(translationTargetValueForAction);
            translatorManagerPage
                    .clkBtnImportFromTranslationMemory(waitForReload);
            CSUtility.tempMethodForThreadSleep(2000);
            verifyStateChange(
                    translatorManagerPage
                            .getBtnVerifyImportFromTranslationMemory(),
                    "up.svg");
            verifyTranslationOfWorkflow(valueOfTargetLanguage,
                    editWorkflowName);
            changeInterfaceLanguage(valueOfSourceLanguage);
            clkBtnImportFromTranslationMemoryTaskbar(translationLabel);
        } catch (Exception e) {
            CSLogger.debug("Failed to verify up to date state ", e);
            Assert.fail("Test case failed for verifying up to date state ", e);
        }
    }

    /**
     * This method clicks import button from translation memory task bar
     * 
     * @param translationLabel contains name of translation label
     */
    private void
            clkBtnImportFromTranslationMemoryTaskbar(String translationLabel) {
        int count = 0;
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        clkTranslationManager();
        clkTranslationJob(translationLabel);
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        enterDataInFilterBar(translationLabel);
        CSUtility.tempMethodForThreadSleep(3000);
        disableFilterBar();
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkTranslationsTab(waitForReload);
        translatorManagerPage.clkBtnImportFromTranslationMemory(waitForReload);
        traverseToTranslationsTabEntries();
        List<WebElement> list = getStateTranslationsTab();
        for (int stateIndex = 0; stateIndex < list.size(); stateIndex++) {
            String chkState = getState(translatorManagerPage
                    .getBtnVerifyImportFromTranslationMemory());
            if (chkState.contains("up.svg")) {
                count++;
            }
        }
        if (count > 1) {
            CSLogger.info("Confirmed entry states have been imported");
        }
    }

    /**
     * Enters the data in filter bar.
     *
     * @param waitForReload WebDriverWait object.
     * @param data String object contains data to be entered in filter bar.
     */
    public void enterDataInFilterBar(String data) {
        CSUtility.tempMethodForThreadSleep(1000);
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//img[contains(@src,'nofilter.gif')]/.."))
                .size();
        if (chkPresenceOfNoFilter == 0) {
            int chkPresenceOfTextbox = browserDriver
                    .findElements(By.xpath("//input[@id='Filter_Label']"))
                    .size();
            if (chkPresenceOfTextbox != 0) {
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        csGuiToolbarHorizontal.getBtnFilter());
                csGuiToolbarHorizontal.getBtnFilter().click();
                CSUtility.tempMethodForThreadSleep(1000);
                csGuiToolbarHorizontal.getTxtFilterBar().clear();
                csGuiToolbarHorizontal.getTxtFilterBar().sendKeys(data);
                CSLogger.info("Entered text  " + data + " in filter bar");
                csGuiToolbarHorizontal.getTxtFilterBar().sendKeys(Keys.ENTER);
            }
        }
        performClickOnTranslationLabelMidPane(data);
    }

    private void performClickOnTranslationLabelMidPane(String data) {
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        for (Iterator<WebElement> iterator = listLabels.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(data)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSUtility.tempMethodForThreadSleep(4000);
            CSUtility.waitForVisibilityOfElement(waitForReload, listElement);
            listElement.click();
            CSLogger.info("clicked on translation label in the mid pane");
        } else {
            CSLogger.error(
                    "Could not find the translation label name in the mid pane");
        }
    }

    private void disableFilterBar() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//a/img[contains(@src,'nofilter.gif')]"))
                .size();
        if (chkPresenceOfNoFilter != 0) {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontal.getbtnNoFilter());
            csGuiToolbarHorizontal.getbtnNoFilter().click();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontal.getBtnFilter());
            csGuiToolbarHorizontal.getBtnFilter().click();
            CSUtility.tempMethodForThreadSleep(1000);
        }
    }

    /**
     * This method verifies translation workflow
     * 
     * @param valueOfTargetLanguage contains value of target language
     * @param translationTargetValueForWorkflow3 contains translation target
     *            value for workflow3
     */
    private void verifyTranslationOfWorkflow(String valueOfTargetLanguage,
            String editWorkflowName) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
        changeInterfaceLanguage(valueOfTargetLanguage);
        checkWorkflowChanges(editWorkflowName);
    }

    /**
     * This method checks workflow changes
     * 
     * @param translationTargetValueForWorkflow3 contains name of translation
     *            target value for workflow3
     */
    private void checkWorkflowChanges(String editWorkflowName) {
        expandWorkflowTypeNode();
        WebElement editedWorkflow = browserDriver
                .findElement(By.linkText(editWorkflowName));
        waitForReload.until(ExpectedConditions.visibilityOf(editedWorkflow));
        Assert.assertEquals(editWorkflowName, editedWorkflow.getText());
        CSLogger.info("Edited workflow changed . verified");
    }

    /**
     * This method expands workflow type node
     */
    private void expandWorkflowTypeNode() {
        goToWorkflowNode();
        workflowsPageInstance.clkPimStudioProductsWorkflowType(waitForReload);
    }

    /**
     * This method changes interface language
     * 
     * @param valueOfTargetLanguage contains value of target language
     */
    private void changeInterfaceLanguage(String valueOfTargetLanguage) {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        configPortalPopup.clkCtxLanguageSettings(waitForReload);
        selectLanguage(valueOfTargetLanguage);
    }

    /**
     * This method selects interface language
     * 
     * @param valueOfTargetLanguage contains value of target language
     */
    private void selectLanguage(String valueOfTargetLanguage) {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(locator.getCsPortalWindowDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPortalWindowTranslationManager()));
            Select selectLanguage = new Select(browserDriver.findElement(
                    By.xpath("//select[contains(@name,'InterfaceLanguage')]")));
            selectLanguage.selectByVisibleText(valueOfTargetLanguage);
            modalDialogPopupWindow
                    .clkBtnCsGuiModalDialogOkButton(waitForReload);
            CSLogger.info("Language changed successfully");
        } catch (Exception e) {
            CSLogger.error("Could not select interface language");
            Assert.fail("Language selection failed", e);
        }
    }

    /**
     * This method performs the verification of all the translated entries from
     * the translation tab
     * 
     * @param workflowName contains name of the workflow
     * @param workflowType contains the type of workflow
     * @param workflowState1 contains first state of workflow
     * @param numberOfStates contains total number of states in workflow
     * @param workflowStatePosition contains workflow state position
     * @param workflowStateColor contains workflow state color
     * @param workflowStateDescription contains workflow state description
     * @param workflowActionName contains workflow state action name
     * @param workflowTabName contains workflow tab name
     * @param firstState contains first state of workflow
     * @param secondState contains second state of workflow
     * @param workflowActionDescription contains workflow actions description
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param editWorkflowName contains workflow name to be edited
     * @param translationTargetValueForEditedWorkflow contains translation
     *            target value for edited workflow
     * @param translationTargetValueForAction contains translation target value
     *            for action of workflow
     * @param translationTargetValueForState contains translation target value
     *            for state of workflow
     * @param valueOfTargetLanguage contains value for target language
     * @param valueOfSourceLanguage contains value for source language
     */
    @Test(
            priority = 6,
            dependsOnMethods = { "testVerifyUpToDateState" },
            dataProvider = "translatingWorkflow")
    public void testVerifyTranslations(String workflowName, String workflowType,
            String workflowState1, String numberOfStates,
            String workflowStatePosition, String workflowStateColor,
            String workflowStateDescription, String workflowActionName,
            String workflowTabName, String firstState, String secondState,
            String workflowActionDescription, String translationLabel,
            String sourceLang, String targetLang, String dataCollectionField,
            String editWorkflowName,
            String translationTargetValueForEditedWorkflow,
            String translationTargetValueForAction,
            String translationTargetValueForState, String valueOfTargetLanguage,
            String valueOfSourceLanguage) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
            changeInterfaceLanguage(valueOfTargetLanguage);
            checkAllConvertedEntries(translationTargetValueForAction,
                    translationTargetValueForState, valueOfTargetLanguage,
                    editWorkflowName);
            changeInterfaceLanguage(valueOfSourceLanguage);
        } catch (Exception e) {
            CSLogger.debug("Failed to verify translations", e);
            Assert.fail("Test case failed for verifying translations", e);
        }
    }

    /**
     * This method checks all the converted entries after translation
     * 
     * @param translationTargetValueForAction contains translation target value
     *            for action of workflow
     * @param translationTargetValueForState contains translation target value
     *            for state of workflow
     * @param valueOfTargetLanguage contains value for target language
     * @param editWorkflowName contains workflow name to be edited
     */
    private void checkAllConvertedEntries(
            String translationTargetValueForAction,
            String translationTargetValueForState, String valueOfTargetLanguage,
            String editWorkflowName) {
        try {
            expandWorkflowTypeNode();
            actions.doubleClick(
                    workflowsPageInstance.getPimStudioProductsWorkflowType())
                    .click().build().perform();
            CSLogger.info("Double clicked on Workflow");
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameMain()));
            WebElement workflow = getWorkflowCenterPane(editWorkflowName);
            workflow.click();
            retriveTranslatedAction(translationTargetValueForAction);
        } catch (Exception e) {
            CSLogger.error("Entries have not been converted");
            Assert.fail("Conversion failed", e);
        }
    }

    /**
     * This method verifies translated action of workflow
     * 
     * @param translationTargetValueForAction contains translation target value
     *            for workflow action
     */
    private void
            retriveTranslatedAction(String translationTargetValueForAction) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
        translatorManagerPage
                .clkTabDiagramRightPaneTranslationManager(waitForReload);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmChart()));
        WebElement label = getActionLabel();
        String translatedAction = label.getText();
        Assert.assertEquals(translationTargetValueForAction, translatedAction);
    }

    /**
     * This method returns the action label which shows while drag and drop of
     * one workflow state to other
     * 
     * @return label
     */
    private WebElement getActionLabel() {
        WebElement label = browserDriver
                .findElement(By.xpath("//a[@class='ActionLabel']"));
        waitForReload.until(ExpectedConditions.visibilityOf(label));
        return label;
    }

    /**
     * This method gets the name of the workflow for verification from center
     * pane after clicking on the workflow type in which particular workflow has
     * been created
     * 
     * @param editWorkflowName contains name of edited workflow
     * @return returnWorkflow
     */
    private WebElement getWorkflowCenterPane(String editWorkflowName) {
        boolean status = false;
        WebElement returnWorkflow = null;
        WebElement workflow = null;
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
        for (int workflowIndex = 0; workflowIndex < list
                .size(); workflowIndex++) {
            workflow = list.get(workflowIndex);
            if (workflow.getText().equals(editWorkflowName)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            returnWorkflow = workflow;
        } else {
            CSLogger.error("Workflow selection from mid pane failed.");
        }
        return returnWorkflow;
    }

    /**
     * This data provider returns the sheet which contains workflow name,
     * workflow type, workflow states , number of states ,workflow state
     * position , workflow state color, workflow state description,workflow
     * action name , workflow tab name ,first state name , second state name
     * ,workflow action description,translation label, source language, target
     * language , data collection field, edited workflow name ,translation
     * target value for edited workflow, translation target value for action of
     * workflow , translation target value for state of workflow,target language
     * value and source language value
     * 
     * @return translatingWorkflowSheet
     */
    @DataProvider(name = "translatingWorkflow")
    public Object[][] translatingWorkflow() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                translatingWorkflowSheet);
    }

    /**
     * This method is used to initialize all the resources used to drive the
     * test case
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        configPortalPopup = CSPopupDivConfigPortal
                .getCSPopupDivConfigPortalLocators(browserDriver);
        translatorManagerPage = new TranslationManagerPage(browserDriver);
        actions = new Actions(browserDriver);
        softAssert = new SoftAssert();
        csPortalWidget = new CSPortalWidget(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        modalDialogPopupWindow = new ModalDialogPopupWindow(browserDriver);
        workflowsNodePopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        workflowsPageInstance = SettingsLeftSectionMenubar
                .getWorkflowsNode(browserDriver);
    }
}
