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
 * This class translates user interface , verifies outdated state ,verifies
 * request confirmation state,verifies confirm state,verifies up to date state
 * and verification of translations
 * 
 * @author CSAutomation Team
 *
 */
public class TranslatingUserInterfaceTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      translatingUserInterfaceSheet = "TranslatingUserInterface";
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
     * This method tests translation of user interface
     * 
     * @param workflowName contains names of workflow
     * @param editWorkflowName contains edited workflow name
     * @param workflowType contains workflow type
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param objectToBeTranslated contains object to be translated
     * @param translationTargetValueForWorkflow1 contains translation target
     *            value for workflow1
     * @param translationTargetValueForWorkflow2 contains translation target
     *            value for workflow2
     * @param translationTargetValueForWorkflow3 contains translation target
     *            value for workflow3
     * @param valueOfTargetLanguage contains value of target language
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(priority = 1, dataProvider = "translatingUserInterface")
    public void testTranslatingUserInterface(String workflowName,
            String editWorkflowName, String workflowType,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String objectToBeTranslated,
            String translationTargetValueForWorkflow1,
            String translationTargetValueForWorkflow2,
            String translationTargetValueForWorkflow3,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            String[] workflow = workflowName.split(",");
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            // This method executes pre-requisites to run the test cases
            executePrerequisites(workflowType, workflow);
            performUseCase(workflow, translationLabel, sourceLang, targetLang,
                    dataCollectionField, objectToBeTranslated);
        } catch (Exception e) {
            CSLogger.debug("Test case failed for translationg user interface",
                    e);
            Assert.fail("Test case failed for translationg user interface", e);
        }
    }

    /**
     * This method performs use case of translating user interface
     * 
     * @param workflow contains workflow name
     * @param translationJob contains translation job
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param objectToBeTranslated contains objects to be translated
     */
    private void performUseCase(String[] workflow, String translationJob,
            String sourceLang, String targetLang, String dataCollectionField,
            String objectToBeTranslated) {
        String workflow1 = workflow[0];
        String workflow2 = workflow[1];
        String workflow3 = workflow[2];
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        createTranslationJob(translationJob);
        dataCollectionOperations(dataCollectionField, sourceLang, targetLang,
                objectToBeTranslated);
        translationsTabOperations(workflow, workflow1, workflow2, workflow3);
    }

    /**
     * This method performs the translations tab operations
     * 
     * @param workflow contains workflow names as array
     * @param workflow1 contains name of first workflow
     * @param workflow2 contains name of second workflow
     * @param workflow3 contains name of third workflow
     */
    private void translationsTabOperations(String[] workflow, String workflow1,
            String workflow2, String workflow3) {
        translatorManagerPage.clkTranslationsTab(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        verifyWorkflowEntryTranslationsTab(workflow, workflow1, workflow2,
                workflow3);
    }

    /**
     * This method verifies workflow entries in translations tab
     * 
     * @param workflow contains workflow names as array
     * @param workflow1 contains name of workflow1
     * @param workflow2 contains name of workflow2
     * @param workflow3 contains name of workflow3
     */
    private void verifyWorkflowEntryTranslationsTab(String[] workflow,
            String workflow1, String workflow2, String workflow3) {
        int count = 0;
        List<WebElement> list = getWorkflow();
        String[] entry = new String[list.size()];
        for (int workflowIndex = 0; workflowIndex < list
                .size(); workflowIndex++) {
            WebElement workflowName = list.get(workflowIndex);
            entry[workflowIndex] = workflowName.getText();
        }
        count = checkEntry(count, entry, workflow1);
        count = checkEntry(count, entry, workflow2);
        count = checkEntry(count, entry, workflow3);
        if (count == workflow.length) {
            CSLogger.info("Workflows are present");
        } else {
            CSLogger.error("Workflows are not present");
        }
    }

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
     * This method gets added workflows list from the translation tab
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
     * This method performs all operations related to data collection tab
     * 
     * @param dataCollectionField contains data collection field
     * @param sourceLangInTranlationJob contains source language in translation
     *            job
     * @param targetLangInTranlationJob contains target language in translation
     *            job
     */
    private void dataCollectionOperations(String dataCollectionField,
            String sourceLangInTranlationJob, String targetLangInTranlationJob,
            String objectToBeTranslated) {
        translatorManagerPage.clkDataCollectionTab(waitForReload);
        Select drpdwnDataCollection = new Select(browserDriver.findElement(By
                .xpath("//select[contains(@id,'TranslationjobCollectorPlugin')]")));
        drpdwnDataCollection.selectByVisibleText(dataCollectionField);
        CSUtility.tempMethodForThreadSleep(1000);
        enableCheckbox();
        selectElement(objectToBeTranslated,
                translatorManagerPage.getTxtObjectsToBeSelected());
        selectSourceLanguage(sourceLangInTranlationJob);
        selectTargetLanguageForTranslationJob(targetLangInTranlationJob);
        clkSave();
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
     * This method selects element from drop down and from text area
     * 
     * @param objectToBeTranslated contains object to be translated
     * @param element contains web element to be selected
     */
    private void selectElement(String objectToBeTranslated,
            WebElement element) {
        try {
            boolean status = false;
            WebElement selectOption = null;
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            List<WebElement> list = element.findElements(By.tagName("option"));
            for (WebElement option : list) {
                if (option.getText().equals(objectToBeTranslated)) {
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
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
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
     * This method clicks on translation manager
     */
    private void clkTranslationManager() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnTranslationManagerIcon(waitForReload);
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
     * This method executes pre-requisites for driving the test case to
     * translate user interface
     * 
     * @param workflowType contains type of workflow
     * @param workflow contains names of workflow as array
     */
    private void executePrerequisites(String workflowType, String[] workflow) {
        for (int i = 0; i < workflow.length; i++) {
            goToWorkflowNode();
            createNewWorkflow();
            configureWorkflow(workflow[i], workflowType);
            saveWorkflow();
        }
        verifyCreationOfWorkflow(workflow, workflowType);
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
     * This method verifies whether the workflow is created.
     * 
     * @param workflow String object contains name of workflow.
     * @param workflowTypeName String object contains name of workflow type.
     */
    private void verifyCreationOfWorkflow(String[] workflow,
            String workflowType) {
        traverseToWorkflowTreeView();
        workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
        CSUtility.rightClickTreeNode(waitForReload,
                workflowsPageInstance.getBtnWorkflowsNode(), browserDriver);
        workflowsNodePopup.selectPopupDivMenu(waitForReload,
                workflowsNodePopup.getCsPopupRefresh(), browserDriver);
        traverseToWorkflowTreeView();
        workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
        WebElement type = getWorkflowType(workflowType);
        type.click();
        for (int i = 0; i < workflow.length; i++) {
            traverseToWorkflowTreeView();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(By.linkText(workflow[i])));
            CSLogger.info("Workflow " + workflow[i] + " is present");
        }
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
     * Saves the configured workflow and workflow state.
     */
    private void saveWorkflow() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * Selects 'New Workflow' popup option by right clicking on workflow node to
     * create new workflow.
     */
    private void createNewWorkflow() {
        CSUtility.rightClickTreeNode(waitForReload,
                workflowsPageInstance.getBtnWorkflowsNode(), browserDriver);
        workflowsNodePopup.selectPopupDivMenu(waitForReload,
                workflowsNodePopup.getCsPopupMenuNewWorkflows(), browserDriver);
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
     * This method tests the verification of outdated state of workflow
     * 
     * @param workflowName contains names of workflow
     * @param editWorkflowName contains edited workflow name
     * @param workflowType contains workflow type
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param objectToBeTranslated contains object to be translated
     * @param translationTargetValueForWorkflow1 contains translation target
     *            value for workflow1
     * @param translationTargetValueForWorkflow2 contains translation target
     *            value for workflow2
     * @param translationTargetValueForWorkflow3 contains translation target
     *            value for workflow3
     * @param valueOfTargetLanguage contains value of target language
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 2,
            dependsOnMethods = { "testTranslatingUserInterface" },
            dataProvider = "translatingUserInterface")
    public void testVerifyOutdatedState(String workflowName,
            String editWorkflowName, String workflowType,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String objectToBeTranslated,
            String translationTargetValueForWorkflow1,
            String translationTargetValueForWorkflow2,
            String translationTargetValueForWorkflow3,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        String[] workflow = workflowName.split(",");
        try {
            clkCreatedWorkflow(workflow[0]);
            editWorkflow(workflow[0], workflowType, editWorkflowName);
            goToTmsStudio(editWorkflowName, workflowName);
        } catch (Exception e) {
            CSLogger.debug("Failed to verify outdated state of workflow", e);
            Assert.fail("Test case failed for verifying outdated state", e);
        }
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
     * This method traverses to TMS studio and verifies state change of outdated
     * entry with availiablity of translation text area
     * 
     * @param editWorkflowName
     * @param workflowName
     */
    private void goToTmsStudio(String editWorkflowName, String workflowName) {
        String[] workflow = workflowName.split(",");
        clkTranslationManager();
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        clkCreatedWorkflow(workflow[0]);
        verifyStateChange(
                translatorManagerPage.getBtnVerifyOutdatedEntryTranslationTab(),
                "cancel.svg");
        availabilityOfTranslationTextAreaToEdit();
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
     * This method checks availability of translation text area to edit
     * 
     * @param productName contains name of product
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
        }
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
            CSLogger.info("Translation state has changed in Translation tab");
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
    private void clkCreatedWorkflow(String workflowName) {
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
            if (workflow.getText().equals(workflowName)) {
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
     * Switches frames of system preferences left pane tree located under
     * setting's tab.
     */
    private void traverseToWorkflowTreeView() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
    }

    /**
     * This method tests the verification of Request Confirmation state of
     * workflow
     * 
     * @param workflowName contains names of workflow
     * @param editWorkflowName contains edited workflow name
     * @param workflowType contains workflow type
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param objectToBeTranslated contains object to be translated
     * @param translationTargetValueForWorkflow1 contains translation target
     *            value for workflow1
     * @param translationTargetValueForWorkflow2 contains translation target
     *            value for workflow2
     * @param translationTargetValueForWorkflow3 contains translation target
     *            value for workflow3
     * @param valueOfTargetLanguage contains value of target language
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 3,
            dependsOnMethods = { "testVerifyOutdatedState" },
            dataProvider = "translatingUserInterface")
    public void testVerifyRequestConfirmationState(String workflowName,
            String editWorkflowName, String workflowType,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String objectToBeTranslated,
            String translationTargetValueForWorkflow1,
            String translationTargetValueForWorkflow2,
            String translationTargetValueForWorkflow3,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            translatorManagerPage.clkBtnSaveAndLoadNextElement(waitForReload);
            setTranslationText(translationTargetValueForWorkflow1);
            translatorManagerPage.clkBtnRequestConfirmation(waitForReload);
            verifyStateChange(
                    translatorManagerPage
                            .getBtnVerifyRequestConfirmationTranslationsTab(),
                    "question.svg");
        } catch (Exception e) {
            CSLogger.debug("Verification of request confirmation test failed",
                    e);
            Assert.fail("Verification of request confirmation test failed", e);
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
     * This method tests the verification of confirm state of workflow
     * 
     * @param workflowName contains names of workflow
     * @param editWorkflowName contains edited workflow name
     * @param workflowType contains workflow type
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param objectToBeTranslated contains object to be translated
     * @param translationTargetValueForWorkflow1 contains translation target
     *            value for workflow1
     * @param translationTargetValueForWorkflow2 contains translation target
     *            value for workflow2
     * @param translationTargetValueForWorkflow3 contains translation target
     *            value for workflow3
     * @param valueOfTargetLanguage contains value of target language
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 4,
            dependsOnMethods = { "testVerifyRequestConfirmationState" },
            dataProvider = "translatingUserInterface")
    public void testVerifyConfirmState(String workflowName,
            String editWorkflowName, String workflowType,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String objectToBeTranslated,
            String translationTargetValueForWorkflow1,
            String translationTargetValueForWorkflow2,
            String translationTargetValueForWorkflow3,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            setTranslationText(translationTargetValueForWorkflow2);
            translatorManagerPage.clkBtnConfirm(waitForReload);
            verifyStateChange(
                    translatorManagerPage.getBtnVerifyConfirmTranslationsTab(),
                    "check.svg");
        } catch (Exception e) {
            CSLogger.debug("Verification of confirmation test failed", e);
            Assert.fail("Verification of confirmation test failed", e);
        }
    }

    /**
     * This method tests the verification of up to date state of workflow
     * 
     * @param workflowName contains names of workflow
     * @param editWorkflowName contains edited workflow name
     * @param workflowType contains workflow type
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param objectToBeTranslated contains object to be translated
     * @param translationTargetValueForWorkflow1 contains translation target
     *            value for workflow1
     * @param translationTargetValueForWorkflow2 contains translation target
     *            value for workflow2
     * @param translationTargetValueForWorkflow3 contains translation target
     *            value for workflow3
     * @param valueOfTargetLanguage contains value of target language
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 5,
            dependsOnMethods = { "testVerifyConfirmState" },
            dataProvider = "translatingUserInterface")
    public void testVerifyUpToDateState(String workflowName,
            String editWorkflowName, String workflowType,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String objectToBeTranslated,
            String translationTargetValueForWorkflow1,
            String translationTargetValueForWorkflow2,
            String translationTargetValueForWorkflow3,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            setTranslationText(translationTargetValueForWorkflow3);
            translatorManagerPage
                    .clkBtnImportFromTranslationMemory(waitForReload);
            verifyStateChange(
                    translatorManagerPage
                            .getBtnVerifyImportFromTranslationMemory(),
                    "up.svg");
            verifyTranslationOfWorkflow(valueOfTargetLanguage,
                    translationTargetValueForWorkflow3);
            changeInterfaceLanguage(valueOfSourceLanguage);
            clkBtnImportFromTranslationMemoryTaskbar(translationLabel);
        } catch (Exception e) {
            CSLogger.debug("Verification of up to date test failed", e);
            Assert.fail("Verification of up to date test failed", e);
        }
    }

    /**
     * This method disables the filter bar which had been opened for searching
     * the translation label from the mid pane
     */
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
                    .findElements(By.xpath("//input[@id='Filter_RubricName']"))
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

    /**
     * This method perfoms click on the translation Lable from the mid pane
     * 
     * @param translationLable contains label to be searched
     */
    private void
            performClickOnTranslationLabelMidPane(String translationLabel) {
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        for (Iterator<WebElement> iterator = listLabels.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(translationLabel)) {
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

    /**
     * This method clicks import button from translation memory task bar
     * 
     * @param translationLabel contains name of translation label
     */
    private void
            clkBtnImportFromTranslationMemoryTaskbar(String translationLabel) {
        int count = 0;
        csPortalHeader.clkBtnSettingsTab(waitForReload);
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
     * This method clicks on created translation label
     * 
     * @param translationLabel contains name of translation label
     */
    private void clkCreatedTranslatedLabel(String translationLabel) {
        boolean status = false;
        WebElement getLabel = null;
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        for (int labelIndex = 0; labelIndex < list.size(); labelIndex++) {
            getLabel = list.get(labelIndex);
            if (getLabel.getText().equals(translationLabel)) {
                status = true;
                break;
            } else
                status = false;
        }
        if (status == true) {
            getLabel.click();
            CSLogger.info("Clicked on label");
        } else {
            CSLogger.error("Unable to click on created translation label");
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
            String translationTargetValueForWorkflow3) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
        changeInterfaceLanguage(valueOfTargetLanguage);
        checkWorkflowChanges(translationTargetValueForWorkflow3);
    }

    /**
     * This method checks workflow changes
     * 
     * @param translationTargetValueForWorkflow3 contains name of translation
     *            target value for workflow3
     */
    private void
            checkWorkflowChanges(String translationTargetValueForWorkflow3) {
        expandWorkflowTypeNode();
        WebElement translatedEntry = browserDriver
                .findElement(By.linkText(translationTargetValueForWorkflow3));
        Assert.assertEquals(translationTargetValueForWorkflow3,
                translatedEntry.getText());
        CSLogger.info("Translation entry has translated successfully");
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
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowTranslationManager()));
        Select selectLanguage = new Select(browserDriver.findElement(
                By.xpath("//select[contains(@name,'InterfaceLanguage')]")));
        selectLanguage.selectByVisibleText(valueOfTargetLanguage);
        modalDialogPopupWindow.clkBtnCsGuiModalDialogOkButton(waitForReload);
        CSLogger.info("Language changed successfully to target language");
    }

    /**
     * This method tests the verification of translations
     * 
     * @param workflowName contains names of workflow
     * @param editWorkflowName contains edited workflow name
     * @param workflowType contains workflow type
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains data collection field
     * @param objectToBeTranslated contains object to be translated
     * @param translationTargetValueForWorkflow1 contains translation target
     *            value for workflow1
     * @param translationTargetValueForWorkflow2 contains translation target
     *            value for workflow2
     * @param translationTargetValueForWorkflow3 contains translation target
     *            value for workflow3
     * @param valueOfTargetLanguage contains value of target language
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 6,
            dependsOnMethods = { "testVerifyUpToDateState" },
            dataProvider = "translatingUserInterface")
    public void testVerifyTranslations(String workflowName,
            String editWorkflowName, String workflowType,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String objectToBeTranslated,
            String translationTargetValueForWorkflow1,
            String translationTargetValueForWorkflow2,
            String translationTargetValueForWorkflow3,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, locator);
            csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
            changeInterfaceLanguage(valueOfTargetLanguage);
            checkAllConvertedEntries(translationTargetValueForWorkflow2,
                    translationTargetValueForWorkflow3);
            changeInterfaceLanguage(valueOfSourceLanguage);
        } catch (Exception e) {
            CSLogger.debug("Verification of up to date test failed", e);
            Assert.fail("Verification of up to date test failed", e);
        }
    }

    /**
     * This method checks all converted entries of workflow
     * 
     * @param translationTargetValueForWorkflow2 contains translation target
     *            value for workflow2
     * @param translationTargetValueForWorkflow3 contains translation target
     *            value for workflow3
     */
    private void checkAllConvertedEntries(
            String translationTargetValueForWorkflow2,
            String translationTargetValueForWorkflow3) {
        expandWorkflowTypeNode();
        WebElement translatedWorkflow1 = browserDriver
                .findElement(By.linkText(translationTargetValueForWorkflow2));
        WebElement translatedWorkflow2 = browserDriver
                .findElement(By.linkText(translationTargetValueForWorkflow3));
        if ((translatedWorkflow1.getText()
                .equals(translationTargetValueForWorkflow2))
                && (translatedWorkflow2.getText()
                        .equals(translationTargetValueForWorkflow3))) {
            CSLogger.info("All workflows have converted to target language");
        } else {
            CSLogger.error("Unable to convert workflows into target language");
        }
    }

    /**
     * This data provider returns sheet which contains workflow names as an
     * array, edited workflow name ,workflow type,translation job label,source
     * language,target language,data collection field ,objects to be
     * translated,translation target value for workflow1, translation target
     * value for workflow2, translation target value for workflow3, language
     * value for target language, language value for source language
     * 
     * @return translatingUserInterfaceSheet
     */
    @DataProvider(name = "translatingUserInterface")
    public Object[][] translatingUserInterface() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                translatingUserInterfaceSheet);
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
