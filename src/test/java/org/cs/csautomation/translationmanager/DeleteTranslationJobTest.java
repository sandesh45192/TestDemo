/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

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
 * This class performs use case of deleting jobs by mass edit option , editor
 * view and by tree view option
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteTranslationJobTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      deleteTranslationJobSheet = "DeleteTranslationJob";
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private Actions                     actions;
    private ITranslationManagerPopup    translationManagerPopup;

    /**
     * This method deletes job using mass edit option F
     * 
     * @param translationJob contains name of job
     * @param nodesUnderTranslationJobNode contains names of nodes
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of logged in user
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     */
    @Test(priority = 1, dataProvider = "deleteTranslationJob")
    public void testDeleteJobMassEdit(String translationJob,
            String nodesUnderTranslationJobNode, String loggedInUser,
            String dataCollectionField, String sourceLang, String targetLang) {
        try {
            String[] jobs = getTranslationJobs(translationJob);
            String[] nodes = getTranslationJobNodes(
                    nodesUnderTranslationJobNode);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            String jobMassEdit = jobs[0];
            createJob(jobMassEdit, sourceLang, targetLang, loggedInUser,
                    dataCollectionField, nodes);
            performUseCase(jobMassEdit, nodes);
        } catch (Exception e) {
            CSLogger.error("Test case failed", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method creates and verifies created job
     * 
     * @param translationJob contains name of job
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of data collection field
     * @param nodes contains names of nodes
     */
    private void createJob(String translationJob, String sourceLang,
            String targetLang, String loggedInUser, String dataCollectionField,
            String[] nodes) {
        selectionOfTranslatorJob(translationJob, sourceLang, targetLang,
                loggedInUser, dataCollectionField);
        verifyTranslatorJobUnderLoggedInUser(translationJob, nodes);
    }

    /**
     * This method performs the use case of deleting and verifying job by mass
     * edit option
     * 
     * @param jobMassEdit contains name of job
     * @param nodes contains names of nodes
     */
    private void performUseCase(String jobMassEdit, String[] nodes) {
        String translationJobId = getIdOfCreatedJob(jobMassEdit, nodes);
        deleteJobMassEdit(jobMassEdit, translationJobId);
        verifyDeletedJob(jobMassEdit);
    }

    /**
     * This method verifies deleted job by mass edit option
     * 
     * @param jobMassEdit contains name of job
     */
    private void verifyDeletedJob(String jobMassEdit) {
        List<WebElement> list = getListOfTranslationJob(jobMassEdit);
        boolean status = getStatusOfJob(jobMassEdit, list);
        if (status == true) {
            CSLogger.error("Could not delete job by mass edit option");
        } else {
            CSLogger.info("Job deleted successfully using mass edit option");
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
     * This method deletes job by mass edit
     * 
     * @param jobMassEdit contains name of job
     * @param translationJobId contains translation job id
     */
    private void deleteJobMassEdit(String jobMassEdit,
            String translationJobId) {
        List<WebElement> list = getListOfTranslationJob(jobMassEdit);
        boolean status = getStatusOfJob(jobMassEdit, list);
        if (status == true) {
            deleteJob(translationJobId);
        }
    }

    /**
     * This method deletes job
     * 
     * @param translationJobId contains id of job
     */
    private void deleteJob(String translationJobId) {
        WebElement massEditChkbox = browserDriver.findElement(By.xpath(
                "//input[@id='CSBuilderMarkAction" + translationJobId + "']"));
        waitForReload.until(ExpectedConditions.visibilityOf(massEditChkbox));
        massEditChkbox.click();
        CSUtility.tempMethodForThreadSleep(2000);
        selectDeleteOption(false);
        selectDeleteOption(true);
    }

    /**
     * This method selects duplicate option from
     * 
     * @param status contains boolean value for accepting alert box
     */
    private void selectDeleteOption(boolean status) {
        Select deleteOption = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        deleteOption.selectByVisibleText("Delete");
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (status == false) {
            alert.dismiss();
            CSLogger.info("Clicked on cancel ");
        } else if (status == true) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            CSUtility.tempMethodForThreadSleep(3000);
        }
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
     * This method verifies job under logged in user
     * 
     * @param translationJob contains name of translation job
     * @param nodes contains names of nodes
     */
    private void verifyTranslatorJobUnderLoggedInUser(String translationJob,
            String[] nodes) {
        try {
            clkTranslationJob(translationJob);
            clkRefresh();
            clkTranslationJob(translationJob);
            clkOpenNode(nodes[0]);
            clkAdminNode(nodes[1]);
            verifyAssignedJob(translationJob);
        } catch (Exception e) {
            CSLogger.error("Verification of created jobs failed");
            softAssert.fail("Verification of jobs under admin failed", e);
        }
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
     * This method selects the job
     * 
     * @param jobMassEdit contains name of job
     * @param sourceLang contains name of source language
     * @param targetLang conatains name of target language
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of data collection field
     */
    private void selectionOfTranslatorJob(String jobMassEdit, String sourceLang,
            String targetLang, String loggedInUser,
            String dataCollectionField) {
        createTranslationJob(jobMassEdit, sourceLang, targetLang,
                dataCollectionField);
        selectTranslator(loggedInUser);
        clkSave();
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
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
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
                CSUtility.tempMethodForThreadSleep(1000);
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
     * This method deletes job from editor view F
     * 
     * @param translationJob contains name of job
     * @param nodesUnderTranslationJobNode contains names of nodes
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of logged in user
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     */
    @Test(priority = 2, dataProvider = "deleteTranslationJob")
    public void testDeleteJobEditorView(String translationJob,
            String nodesUnderTranslationJobNode, String loggedInUser,
            String dataCollectionField, String sourceLang, String targetLang) {
        try {
            String[] jobs = getTranslationJobs(translationJob);
            String[] nodes = getTranslationJobNodes(
                    nodesUnderTranslationJobNode);
            String jobEditorView = jobs[1];
            createJob(jobEditorView, sourceLang, targetLang, loggedInUser,
                    dataCollectionField, nodes);
            deleteJobEditorView(jobEditorView, false);
            deleteJobEditorView(jobEditorView, true);
            verifyDeletedJobEditorView(jobEditorView);
        } catch (Exception e) {
            CSLogger.error("Test case failed", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method verifies deleted job from editor view
     * 
     * @param jobEditorView contains name of job
     */
    private void verifyDeletedJobEditorView(String jobEditorView) {
        List<WebElement> list = getListOfTranslationJob(jobEditorView);
        boolean status = getStatusOfJob(jobEditorView, list);
        if (status == true) {
            CSLogger.error("Could not delete job using editor view option");
        } else {
            CSLogger.info("Job deleted successfully using editor view option");
        }
    }

    /**
     * This method deletes job from editor view
     * 
     * @param jobEditorView contains name of job
     */
    private void deleteJobEditorView(String jobEditorView, boolean pressOk) {
        boolean status = false;
        WebElement translationJob = null;
        List<WebElement> list = getListOfTranslationJob(jobEditorView);
        for (int jobIndex = 0; jobIndex < list.size(); jobIndex++) {
            translationJob = list.get(jobIndex);
            if (translationJob.getText().equals(jobEditorView)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            deleteJobEditorView(translationJob, pressOk);
        }
    }

    /**
     * This method deletes job from the editor view
     * 
     * @param translationJob contains name of translation job
     */
    private void deleteJobEditorView(WebElement translationJob,
            boolean pressOk) {
        String job = translationJob.getText();
        waitForReload.until(ExpectedConditions.visibilityOf(translationJob));
        translationJob.click();
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
        CSUtility.tempMethodForThreadSleep(2000);
        clkViewInSubmenu();
        CSUtility.tempMethodForThreadSleep(5000);
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        String alertText = alert.getText();
        if (alertText.contains(
                "Do you really want to delete the item indicated in the following: "
                        + job)) {
            if (pressOk == false) {
                alert.dismiss();
                CSLogger.info("Clicked on cancel of alert");
            } else {
                if (pressOk == true) {
                    alert.accept();
                    CSLogger.info("Clicked on ok of alert");
                }
            }
        }
    }

    /**
     * Clicks on view option from submenu
     */
    private void clkViewInSubmenu() {
        translatorManagerPage.clkElement(waitForReload,
                translatorManagerPage.getBtnStudioMenu());
        translationManagerPopup.selectPopupDivMenu(waitForReload,
                translationManagerPopup.getCtxTranslationJob(), browserDriver);
        translationManagerPopup.selectPopupDivSubMenu(waitForReload,
                translationManagerPopup.getCtxDelete(), browserDriver);

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
     * This method deletes job from tree view
     * 
     * @param translationJob contains name of job
     * @param nodesUnderTranslationJobNode contains names of nodes
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of logged in user
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     */
    @Test(priority = 3, dataProvider = "deleteTranslationJob")
    public void testDeleteJobTreeView(String translationJob,
            String nodesUnderTranslationJobNode, String loggedInUser,
            String dataCollectionField, String sourceLang, String targetLang) {
        try {
            String[] jobs = getTranslationJobs(translationJob);
            String[] nodes = getTranslationJobNodes(
                    nodesUnderTranslationJobNode);
            String jobTreeView = jobs[2];
            createJob(jobTreeView, sourceLang, targetLang, loggedInUser,
                    dataCollectionField, nodes);
            deleteJobTreeView(nodesUnderTranslationJobNode, jobTreeView, false);
            deleteJobTreeView(nodesUnderTranslationJobNode, jobTreeView, true);
            verifyDeletedJobFromTreeView(nodesUnderTranslationJobNode,
                    jobTreeView);
        } catch (Exception e) {
            CSLogger.debug("Test case failed" + e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method verifies deleted job from tree view
     * 
     * @param nodesUnderTranslationJobNode contains nodes under translations
     *            node
     * @param jobTreeView contains name of job
     */
    private void verifyDeletedJobFromTreeView(
            String nodesUnderTranslationJobNode, String jobTreeView) {
        try {
            String[] nodes = getTranslationJobNodes(
                    nodesUnderTranslationJobNode);
            clkTranslationJob(jobTreeView);
            clkRefresh();
            clkTranslationJob(jobTreeView);
            clkOpenNode(nodes[0]);
            clkAdminNode(nodes[1]);
            WebElement deletedJob = browserDriver.findElement(
                    By.xpath("//span[text()=' " + jobTreeView + "']"));
            if (deletedJob.getText().equals(jobTreeView)) {
                CSLogger.error("Could not delete job from tree view");
            } else {
                CSLogger.info("Job has successfully deleted");
            }
        } catch (Exception e) {
            CSLogger.info("Job has successfully deleted from the tree view");
        }
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
     * This method deletes job from tree view
     * 
     * @param nodesUnderTranslationJobNode
     * @param jobTreeView
     * @param status
     */
    private void deleteJobTreeView(String nodesUnderTranslationJobNode,
            String jobTreeView, boolean status) {
        clkTranslationJob(jobTreeView);
        String[] nodes = getTranslationJobNodes(nodesUnderTranslationJobNode);
        clkOpenNode(nodes[0]);
        clkAdminNode(nodes[1]);
        WebElement createdJob = getCreatedJob(jobTreeView);
        createdJob.click();
        CSUtility.rightClickTreeNode(waitForReload, createdJob, browserDriver);
        translationManagerPopup.selectPopupDivMenu(waitForReload,
                translationManagerPopup.getCsPopupObject(), browserDriver);
        translationManagerPopup.selectPopupDivSubMenu(waitForReload,
                translationManagerPopup.getCtxDelete(), browserDriver);
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        String alertText = alert.getText();
        if (alertText.equals("Do you really want to delete this item?")) {
            if (status == false) {
                alert.dismiss();
                CSLogger.info("Clicked on Cancel  of alert");
            } else {
                if (status == true) {
                    alert.accept();
                    CSLogger.info("Clicked on Ok of alert");
                }
            }
        }
    }

    /**
     * This method returns the created job
     * 
     * @param job contains job name
     * @return created job
     */
    private WebElement getCreatedJob(String job) {
        WebElement createdJob = null;
        try {
            createdJob = browserDriver
                    .findElement(By.xpath("//span[text()=' " + job + "']"));
            waitForReload.until(ExpectedConditions.visibilityOf(createdJob));
        } catch (Exception e) {
            CSLogger.error("Job not found");
        }
        return createdJob;
    }

    /**
     * This data provider returns sheet which contains translation jobs ,nodes ,
     * logged in user name ,name of data collection field ,source language and
     * target language
     * 
     * @return deleteTranslationJobSheet
     */
    @DataProvider(name = "deleteTranslationJob")
    public Object[][] translationJobHierarchy() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                deleteTranslationJobSheet);
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
