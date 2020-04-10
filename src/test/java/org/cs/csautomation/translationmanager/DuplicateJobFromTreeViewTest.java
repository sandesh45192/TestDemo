/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.ITranslationManagerPopup;
import org.cs.csautomation.cs.settings.translationmanager.TranslationManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
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
 * This class duplicates created job from tree view , list view and mass edit
 * option
 * 
 * @author CSAutomation TEam
 *
 */
public class DuplicateJobFromTreeViewTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      duplicateTranslationJobSheet = "DuplicateTranslationJob";
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private Actions                     actions;
    private ITranslationManagerPopup    translationManagerPopup;

    /**
     * This method tests duplicate job from tree view
     * 
     * @param translationJob contains name of translation job
     * @param nodesUnderTranslationJobNode contains names of nodes
     * @param loggedInUser contains logged in user
     * @param dataCollectionField contains name of data collection field
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     */
    @Test(priority = 1, dataProvider = "duplicateTranslationJob")
    public void testDuplicateJobFromTreeView(String translationJob,
            String nodesUnderTranslationJobNode, String loggedInUser,
            String dataCollectionField, String sourceLang, String targetLang) {
        try {
            String[] jobs = getTranslationJobs(translationJob);
            String[] nodes = getTranslationJobNodes(
                    nodesUnderTranslationJobNode);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            String jobTreeView = jobs[0];
            String jobMassEdit = jobs[1];
            String jobListView = jobs[2];
            executePrerequisites(jobTreeView, jobMassEdit, jobListView,
                    sourceLang, targetLang, loggedInUser, dataCollectionField,
                    nodes);
            duplicateJobFromTreeView(jobTreeView, nodes);
            verifyDuplicateJobFromTreeView(jobTreeView, nodes);
        } catch (Exception e) {
            CSLogger.debug("Could not execute test case");
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method executes prerequisites
     * 
     * @param jobTreeView contains name of job
     * @param jobMassEdit contains name of job
     * @param jobListView contains name of job
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of data collection field
     * @param nodes contains name of nodes
     */
    private void executePrerequisites(String jobTreeView, String jobMassEdit,
            String jobListView, String sourceLang, String targetLang,
            String loggedInUser, String dataCollectionField, String[] nodes) {
        selectionOfTranslatorJob(jobTreeView, sourceLang, targetLang,
                loggedInUser, dataCollectionField);
        verifyTranslatorJobUnderLoggedInUser(jobTreeView, nodes);
        selectionOfTranslatorJob(jobMassEdit, sourceLang, targetLang,
                loggedInUser, dataCollectionField);
        verifyTranslatorJobUnderLoggedInUser(jobMassEdit, nodes);
        selectionOfTranslatorJob(jobListView, sourceLang, targetLang,
                loggedInUser, dataCollectionField);
        verifyTranslatorJobUnderLoggedInUser(jobListView, nodes);
    }

    /**
     * This method verifies duplicated job from tree view
     * 
     * @param jobTreeView contains name of job
     * @param nodes contains names of nodes
     */
    private void verifyDuplicateJobFromTreeView(String jobTreeView,
            String[] nodes) {
        try {
            clkTranslationJob(jobTreeView);
            clkOpenNode(nodes[0]);
            clkAdminNode(nodes[1]);
            String duplicate = "Copy of " + jobTreeView;
            WebElement duplicateJob = getCreatedJob(duplicate);
            Assert.assertEquals(duplicate, duplicateJob.getText());
            CSLogger.info("Job has duplicated in tree view");
        } catch (Exception e) {
            CSLogger.error("Duplicate entry of tree view is absent ");
            softAssert.fail("Duplicate entry of tree view is absent ");
        }
    }

    /**
     * This method duplicates job from tree view
     * 
     * @param jobTreeView contains name of job
     * @param nodes contains names of nodes
     */
    private void duplicateJobFromTreeView(String jobTreeView, String[] nodes) {
        clkTranslationJob(jobTreeView);
        clkOpenNode(nodes[0]);
        clkAdminNode(nodes[1]);
        WebElement createdJob = getCreatedJob(jobTreeView);
        createdJob.click();
        CSUtility.rightClickTreeNode(waitForReload, createdJob, browserDriver);
        translationManagerPopup.selectPopupDivMenu(waitForReload,
                translationManagerPopup.getCsPopupObject(), browserDriver);
        translationManagerPopup.selectPopupDivSubMenu(waitForReload,
                translationManagerPopup.getCtxDuplicate(), browserDriver);
        CSLogger.info("Clicked on Duplicate option");
    }

    /**
     * This method returns the created job
     * 
     * @param job contains job name
     * @return created job
     */
    private WebElement getCreatedJob(String job) {
        WebElement createdJob = browserDriver
                .findElement(By.xpath("//span[text()=' " + job + "']"));
        waitForReload.until(ExpectedConditions.visibilityOf(createdJob));
        return createdJob;
    }

    /**
     * This method selects translator job
     * 
     * @param jobTreeView contains name of job
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of data collection field
     */
    private void selectionOfTranslatorJob(String jobTreeView, String sourceLang,
            String targetLang, String loggedInUser,
            String dataCollectionField) {
        createTranslationJob(jobTreeView, sourceLang, targetLang,
                dataCollectionField);
        selectTranslator(loggedInUser);
        clkSave();
    }

    /**
     * This method verifies translator job under logged in user i.e. admin
     * 
     * @param jobTranslator contains name of job
     * @param nodes contains names of nodes
     */
    private void verifyTranslatorJobUnderLoggedInUser(String translationJob,
            String[] nodes) {
        clkTranslationJob(translationJob);
        clkRefresh();
        clkTranslationJob(translationJob);
        clkOpenNode(nodes[0]);
        clkAdminNode(nodes[1]);
        verifyAssignedJob(translationJob);
    }

    /**
     * This method verifies assigned job under node
     * 
     * @param translationJob contains name of translation job
     */
    private void verifyAssignedJob(String translationJob) {
        WebElement verifyJob = browserDriver.findElement(
                By.xpath("//span[text()=' " + translationJob + "']"));
        if (verifyJob.getText().equals(translationJob)) {
            Assert.assertEquals(translationJob, verifyJob.getText());
            CSLogger.info("Job which has assigned is present under "
                    + " admin node. Verified.");
        }
    }

    /**
     * This method clicks on node
     * 
     * @param node contains node to be clicked on
     */
    private void clkNode(String node) {
        WebElement targetNode = browserDriver
                .findElement(By.xpath("//span[text()=' " + node + "']"));
        waitForReload.until(ExpectedConditions.visibilityOf(targetNode));
        targetNode.click();
    }

    /**
     * This method clicks on admin node
     * 
     * @param adminNode contains name of node
     */
    private void clkAdminNode(String adminNode) {
        clkNode(adminNode);
    }

    /**
     * This method clicks on open node
     * 
     * @param openNode contains name of node
     */
    private void clkOpenNode(String openNode) {
        clkNode(openNode);
    }

    /**
     * This method clicks refresh option
     */
    private void clkRefresh() {
        CSUtility.rightClickTreeNode(waitForReload,
                translatorManagerPage.getTranslationJobNode(), browserDriver);
        translationManagerPopup.selectPopupDivMenu(waitForReload,
                translationManagerPopup.getCtxRefresh(), browserDriver);
    }

    /**
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method creates translation job
     * 
     * @param translationJob contains name of translation job
     * @param targetLang
     * @param sourceLang
     */
    private void createTranslationJob(String translationJob, String sourceLang,
            String targetLang, String dataCollectionField) {
        clkTranslationJob(translationJob);
        CSUtility.tempMethodForThreadSleep(2000);
        clkCreateNew();
        setLabel(translationJob);
        setLanguagesInDataCollectionTab(sourceLang, targetLang,
                dataCollectionField);
    }

    /**
     * This method sets languages in data collection field
     * 
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param dataCollectionField contains name of data collection field
     */
    private void setLanguagesInDataCollectionTab(String sourceLang,
            String targetLang, String dataCollectionField) {
        translatorManagerPage.clkDataCollectionTab(waitForReload);
        Select drpdwnDataCollection = new Select(browserDriver.findElement(By
                .xpath("//select[contains(@id,'TranslationjobCollectorPlugin')]")));
        drpdwnDataCollection.selectByVisibleText(dataCollectionField);
        CSLogger.info("Clicked on Data Collection field");
        selectSourceLanguage(sourceLang);
        selectTargetLanguageForTranslationJob(targetLang);
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
     * This method selects translator
     * 
     * @param loggedInUser contains logged in user
     */
    private void selectTranslator(String loggedInUser) {
        selectUser(loggedInUser, translatorManagerPage
                .getTxtTranslationJobAvailableTranslator());
    }

    /**
     * This method selects user from text area
     * 
     * @param value contains value to be selected
     * @param element contains web element
     */
    private void selectUser(String value, WebElement element) {
        try {
            boolean status = false;
            WebElement selectOption = null;
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            List<WebElement> list = element.findElements(By.tagName("option"));
            for (WebElement option : list) {
                if (option.getText().equals(value)) {
                    selectOption = option;
                    status = true;
                    break;
                }
            }
            if (status == true) {
                actions.doubleClick(selectOption).click().build().perform();
                CSLogger.info("Double clicked on language");
            } else {
                CSLogger.error("Failed to double click ");
            }
        } catch (Exception e) {
            CSLogger.error("Could not select and double click the language");
        }
    }

    /**
     * This method contains job nodes
     * 
     * @param nodesUnderTranslationJobNode contains nodes under translation tab
     * @return nodes
     */
    private String[]
            getTranslationJobNodes(String nodesUnderTranslationJobNode) {
        String[] nodes = nodesUnderTranslationJobNode.split("//");
        return nodes;
    }

    /**
     * This method return translation jobs
     * 
     * @param translationJob contains job names
     * @return jobs
     */
    private String[] getTranslationJobs(String translationJob) {
        String[] jobs = translationJob.split(",");
        return jobs;
    }

    /**
     * This method tests duplicate job from mass editing option
     * 
     * @param translationJob contains name of translation job
     * @param nodesUnderTranslationJobNode contains names of nodes
     * @param loggedInUser contains logged in user
     * @param dataCollectionField contains name of data collection field
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     */

    @Test(priority = 2, dataProvider = "duplicateTranslationJob")
    public void testDuplicateJobFromMassEditing(String translationJob,
            String nodesUnderTranslationJobNode, String loggedInUser,
            String dataCollectionField, String sourceLang, String targetLang) {
        try {
            String[] jobs = getTranslationJobs(translationJob);
            String[] nodes = getTranslationJobNodes(
                    nodesUnderTranslationJobNode);
            String jobMassEdit = jobs[1];
            String translationJobId = getIdOfCreatedJob(jobMassEdit, nodes);
            duplicateJobFromMidPane(jobMassEdit, translationJobId, nodes);
            verifyDuplicateJobFromMidPane(jobMassEdit);
        } catch (Exception e) {
            CSLogger.debug("Could not execute test case");
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method verifies duplicate job from mid pane
     * 
     * @param jobMassEdit contains name of job
     */
    private void verifyDuplicateJobFromMidPane(String jobMassEdit) {
        List<WebElement> list = getListOfTranslationJob(jobMassEdit);
        boolean status = getStatusOfDuplicateJob(list, jobMassEdit);
        if (status == true) {
            CSLogger.info(
                    "Job has duplicated successfully using mass edit option");
        } else {
            CSLogger.error("Job duplication using mass edit option failed");
        }
    }

    /**
     * This method gets status of duplicate job from mid pane
     * 
     * @param list contains list of names of job
     * @param translationJob contains name of translation job
     * @return status
     */
    private boolean getStatusOfDuplicateJob(List<WebElement> list,
            String translationJob) {
        boolean status = false;
        String duplicatedJob = "Copy of " + translationJob;
        for (int jobIndex = 0; jobIndex < list.size(); jobIndex++) {
            WebElement job = list.get(jobIndex);
            if (job.getText().equals(duplicatedJob)) {
                status = true;
                break;
            }
        }
        return status;
    }

    /**
     * This method duplicates job from mid pane
     * 
     * @param jobMassEdit contains name of job
     * @param translationJobId contains translation job id
     */
    private void duplicateJobFromMidPane(String jobMassEdit,
            String translationJobId, String[] nodes) {
        clkTranslationManager();
        clkTranslationJob(jobMassEdit);
        List<WebElement> list = getListOfTranslationJob(jobMassEdit);
        boolean status = getStatusOfJob(jobMassEdit, list);
        if (status == true) {
            duplicateFromMassEditOption(translationJobId);
        }
    }

    /**
     * Enters the data in filter bar.
     *
     * @param waitForReload WebDriverWait object.
     * @param data String object contains data to be entered in filter bar.
     */
    public boolean enterDataInFilterBar(String data) {
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
        boolean status = performClickOnTranslationLabelMidPane(data);
        return status;
    }

    /**
     * Performs click on the translation label from the mid pane
     * 
     * @param data contains string data
     */
    private boolean performClickOnTranslationLabelMidPane(String data) {
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
        return status;
    }

    /**
     * This method returns status of job
     * 
     * @param jobMassEdit contains name of job
     * @param list contains list
     * @return status
     */
    private boolean getStatusOfJob(String jobMassEdit, List<WebElement> list) {
        boolean status = false;
        for (int jobIndex = 0; jobIndex < list.size(); jobIndex++) {
            WebElement job = list.get(jobIndex);
            if (job.getText().equals(jobMassEdit)) {
                status = true;
                break;
            }
        }
        return status;
    }

    /**
     * This method duplicates from mass edit option
     * 
     * @param translationJobId contains translation job id
     */
    private void duplicateFromMassEditOption(String translationJobId) {
        WebElement massEditChkbox = browserDriver.findElement(By.xpath(
                "//input[@id='CSBuilderMarkAction" + translationJobId + "']"));
        waitForReload.until(ExpectedConditions.visibilityOf(massEditChkbox));
        massEditChkbox.click();
        CSUtility.tempMethodForThreadSleep(2000);
        selectDuplicateOption(true);
    }

    /**
     * This method selects duplicate option from
     * 
     * @param status contains boolean value for accepting alert box
     */
    private void selectDuplicateOption(boolean status) {
        Select deleteOption = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        deleteOption.selectByVisibleText("Duplicate");
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (status == false) {
            alert.dismiss();
            CSLogger.info("Clicked on cancel ");
        } else if (status == true) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            CSUtility.tempMethodForThreadSleep(4000);
        }
    }

    /**
     * This method gets list of translation jobs from mid pane
     * 
     * @param jobMassEdit contains name of job
     * @return list
     */
    private List<WebElement> getListOfTranslationJob(String jobMassEdit) {
        clkTranslationJob(jobMassEdit);
        CSUtility.tempMethodForThreadSleep(2000);
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        return list;
    }

    /**
     * This method gets id of created job
     * 
     * @param jobMassEdit contains name of job
     * @param nodes contains names of nodes
     * @return translationId
     */
    private String getIdOfCreatedJob(String jobMassEdit, String[] nodes) {
        clkTranslationJob(jobMassEdit);
        clkOpenNode(nodes[0]);
        clkAdminNode(nodes[1]);
        WebElement createdJob = browserDriver
                .findElement(By.xpath("//span[text()=' " + jobMassEdit + "']"));
        waitForReload.until(ExpectedConditions.visibilityOf(createdJob));
        createdJob.click();
        CSUtility.tempMethodForThreadSleep(2000);
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        WebElement id = browserDriver.findElement(
                By.xpath("//input[@id='CSGuiEditorDialogIDInput']"));
        String translationId = id.getAttribute("value");
        return translationId;
    }

    /**
     * This method tests duplicate job from list view
     * 
     * @param translationJob contains name of translation job
     * @param nodesUnderTranslationJobNode contains names of nodes
     * @param loggedInUser contains logged in user
     * @param dataCollectionField contains name of data collection field
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     */
    @Test(priority = 3, dataProvider = "duplicateTranslationJob")
    public void testDuplicateJobFromListView(String translationJob,
            String nodesUnderTranslationJobNode, String loggedInUser,
            String dataCollectionField, String sourceLang, String targetLang) {
        try {
            String[] jobs = getTranslationJobs(translationJob);
            String jobListView = jobs[2];
            duplicateJobFromMidPaneListView(jobListView);
            verifyDuplicateJobFromListView(jobListView);
        } catch (Exception e) {
            CSLogger.debug("Could not execute test case");
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method verifies if job has duplicated in list view
     * 
     * @param jobListView contains name of job
     */
    private void verifyDuplicateJobFromListView(String jobListView) {
        boolean status = false;
        List<WebElement> list = getListOfTranslationJob(jobListView);
        status = getStatusOfDuplicateJob(list, jobListView);
        if (status == true) {
            CSLogger.info("Job has successfully duplicated in list view");
        } else {
            CSLogger.error("Job could not be duplicated");
        }
    }

    /**
     * This method duplicates job from mid pane list view
     * 
     * @param jobListView contains name of job
     */
    private void duplicateJobFromMidPaneListView(String jobListView) {
        boolean status = false;
        WebElement translationJob = null;
        List<WebElement> list = getListOfTranslationJob(jobListView);
        for (int jobIndex = 0; jobIndex < list.size(); jobIndex++) {
            translationJob = list.get(jobIndex);
            if (translationJob.getText().equals(jobListView)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            duplicateJob(translationJob);
        }
    }

    /**
     * This method duplicates job
     * 
     * @param translationJob contains name of translation job
     */
    private void duplicateJob(WebElement translationJob) {
        CSUtility.rightClickTreeNode(waitForReload, translationJob,
                browserDriver);
        translationManagerPopup.selectPopupDivMenu(waitForReload,
                translationManagerPopup.getCsPopupObject(), browserDriver);
        translationManagerPopup.selectPopupDivSubMenu(waitForReload,
                translationManagerPopup.getCtxDuplicate(), browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method returns the sheet which contains translation job names,names
     * of nodes ,logged in user name ,name of data collection field ,source
     * language ,target language
     * 
     * @return deleteTranslationJobSheet
     */
    @DataProvider(name = "duplicateTranslationJob")
    public Object[][] translationJobHierarchy() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                duplicateTranslationJobSheet);
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
        translatorManagerPage = new TranslationManagerPage(browserDriver);
        actions = new Actions(browserDriver);
        softAssert = new SoftAssert();
        csPortalWidget = new CSPortalWidget(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        translationManagerPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
    }
}
