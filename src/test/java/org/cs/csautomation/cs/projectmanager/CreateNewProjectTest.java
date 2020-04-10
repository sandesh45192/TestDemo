/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.projectmanager;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.ISettingsPopup;
import org.cs.csautomation.cs.settings.ProjectManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
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

/**
 * This class contains the test methods which verifies creation of new project.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateNewProjectTest extends AbstractTest {

    private WebDriverWait               waitForReload;
    private CSPortalHeader              csPortalHeader;
    private ISettingsPopup              settingPopup;
    private CSPopupDivPim                  csPopupDiv;
    private TraversingForSettingsModule traverseSettingModule;
    private ProjectManagerPage          projectManagerPage;
    private CSPortalWidget              csPortalWidget;
    private FrameLocators               locator;
    private Actions                     actions;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private String                      createNewPorjectTaskSheet = "CreateNewPorjectTask";

    /**
     * This method verifies creation of new project.
     * 
     * @param projectName String object contains project name
     * @param taskName String object contains task name
     */
    @Test(dataProvider = "CreateNewPorjectTaskData")
    public void testCreateNewProject(String projectName,
            String createMultipleTask, String taskName, String taskNameToEdit,
            String workflowAction, String changeWorkflow) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToProjects();
            createNewProject(projectName);
            verifyCreateNewProject(projectName);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testCreateNewProject", e);
            Assert.fail("Automation error : testCreateNewProject", e);
        }
    }

    /**
     * This method verifies creation of new project Task.
     * 
     * @param projectName String object contains project name
     * @param taskName String object contains task name
     */
    @Test(
            dataProvider = "CreateNewPorjectTaskData",
            dependsOnMethods = "testCreateNewProject")
    public void testCreateNewTask(String projectName,
            String createMultipleTask, String taskName, String taskNameToEdit,
            String workflowAction, String changeWorkflow) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToProjects();
            clkOnCreatedProject(projectName);
            CSUtility.tempMethodForThreadSleep(1000);
            createNewTask(createMultipleTask);
            verifyNewTaskCreation(projectName, createMultipleTask);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testCreateNewTask", e);
            Assert.fail("Automation error : testCreateNewTask", e);
        }
    }

    /**
     * This method verifies the edit created task and execute workflow .
     * 
     * @param projectName String object contains project name
     * @param taskName String object contains task name
     * @param taskNewName String object new task name
     * @param workflowAction String object workflow action name
     * @param changeWorkflow String object changed workflow action name
     */
    @Test(
            dataProvider = "CreateNewPorjectTaskData",
            dependsOnMethods = "testCreateNewTask")
    public void testEditTaskAndExecuteWorkfolw(String projectName,
            String createMultipleTask, String taskName, String taskNameToEdit,
            String workflowAction, String changeWorkflow) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToProjects();
            clkOnCreatedProject(projectName);
            CSUtility.tempMethodForThreadSleep(1000);
            clkOnCreatedTask(taskName);
            editTask(taskNameToEdit);
            UncheckCheckBox();
            CSUtility.tempMethodForThreadSleep(1000);
            verifyEditedData(taskNameToEdit, workflowAction);
            changeWorkflowAction(changeWorkflow);
            verifyAssignWorkflow(changeWorkflow);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testEditTaskAndExecuteWorkfolw",
                    e);
            Assert.fail("Automation error : testEditTaskAndExecuteWorkfolw", e);
        }
    }

    /**
     * This method change the workflow action.
     * 
     * @param changeWorkflow String object changed workflow action name
     */
    private void changeWorkflowAction(String changeWorkflow) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        WebElement chkBox = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[4]/td[1]"));
        chkBox.click();
        Select bottomDropdown = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        bottomDropdown.selectByVisibleText("Apply action");
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csPopupDiv.getcsGuiPopupSelectAction());
        Select actionSelect = new Select(
                csPopupDiv.getcsGuiPopupSelectAction());
        actionSelect.selectByIndex(1);
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.tempMethodForThreadSleep(2000);
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        CSLogger.info("Workeflow Changed.");
    }

    /**
     * This method verifies the edited data of task.
     * 
     * @param taskNewName String object new task name
     * @param workflowAction String object workflow action name
     */
    private void verifyEditedData(String taskNewName, String workflowAction) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        WebElement elementLabel = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[4]/td[3]"));
        String label = elementLabel.getText();
        if (label.equals(taskNewName)) {
            CSLogger.info("New Task Name " + taskNewName + " is verified");
        } else {
            CSLogger.error("New Task Name not verified");
            Assert.fail("New Task Name not verified");
        }
        verifyAssignWorkflow(workflowAction);
    }

    /**
     * This method verifies Assigned workflow to the task
     * 
     * @param workflowAction String object workflow action name
     */
    private void verifyAssignWorkflow(String workflowAction) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        WebElement state = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[4]/td[9]"));
        actions.moveToElement(state).build().perform();
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//div[@id='CSTooltipContainer']")));
        String workflow = browserDriver
                .findElement(By.xpath("//div[@id='CSTooltipText']")).getText();
        if (workflow.equals(workflowAction)) {
            CSLogger.info("Workflow action " + workflowAction + " is verified");
        } else {
            CSLogger.error(
                    "Workflow action " + workflowAction + "is not verified");
            Assert.fail(
                    "Workflow action " + workflowAction + "is not verified");
        }
    }

    /**
     * This method edit the task data.
     * 
     * @param taskNewName String object new task name
     */
    private void editTask(String taskNewName) {
        traverseSettingModule.traverseToEditFrameTask(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getTxtTaskLabel());
        projectManagerPage.getTxtTaskLabel().clear();
        projectManagerPage.getTxtTaskLabel().sendKeys(taskNewName);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getBtnWorkflow());
        projectManagerPage.getBtnWorkflow().click();
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getcsGuiPopupWorkflowDone(), browserDriver);
        traverseSettingModule.traverseToEditFrameTask(waitForReload,
                browserDriver);
        projectManagerPage.clkOnButtonSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Edit Task " + taskNewName);
    }

    /**
     * This method Uncheck the check boxes.
     * 
     */
    private void UncheckCheckBox() {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getChkBoxTaskOpen());
        projectManagerPage.getChkBoxTaskOpen().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getChkBoxMine());
        projectManagerPage.getChkBoxMine().click();
        CSLogger.info("Unchecked all check box");
    }

    /**
     * This method click on created task.
     * 
     * @param taskName String object contains task name
     */
    private void clkOnCreatedTask(String taskName) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        int row = getrow(taskName);
        WebElement elementTemplate = browserDriver
                .findElement(By.xpath("//table[@class='hidewrap CSAdminList']"
                        + "/tbody/tr[" + row + "]/td[4]"));
        elementTemplate.click();
        CSLogger.info("Clicked on task " + taskName);
    }

    /**
     * This method verifies creation of New Template Task.
     * 
     * @param templateName String object contains Template name
     * @param taskName String object contains task name
     * @param ownerName String object contains owner name of task
     */
    private void verifyNewTaskCreation(String templateName, String taskName) {
        String template = null;
        String tasks[] = taskName.split(",");
        for (String labels : tasks) {
            traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                    browserDriver);
            int row = getrow(labels);
            WebElement elementTemplate = browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']"
                            + "/tbody/tr[" + row + "]/td[4]"));
            template = elementTemplate.getText();
            if (template.contains(templateName)) {
                CSLogger.info("Template name verified Successfully");
            } else {
                CSLogger.error("Template name not verified");
                Assert.fail("Template name not verified");
            }
        }
    }

    /**
     * This method get the required from the list view.
     * 
     * @param taskName String object contains task name
     * @return row integer object contains number of row
     */
    private int getrow(String taskName) {
        int row = 0;
        WebElement table = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        int count = table.findElements(By.tagName("tr")).size();
        for (row = 3; row < count; row++) {
            WebElement elementLabel = browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                            + row + "]/td[3]"));
            String label = elementLabel.getText();
            if (label.equalsIgnoreCase(taskName)) {
                break;
            }
        }
        return row;
    }

    /**
     * This method create of New Template Task.
     * 
     * @param taskName String object contains task name
     */
    private void createNewTask(String taskName) {
        String tasks[] = taskName.split(",");
        for (String taskLabel : tasks) {
            traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                    browserDriver);
            csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCreateNew(waitForReload);
            traverseSettingModule.traverseToEditFrameTask(waitForReload,
                    browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    projectManagerPage.getTxtTaskLabel());
            projectManagerPage.getTxtTaskLabel().sendKeys(taskLabel);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    projectManagerPage.getBtnSave());
            projectManagerPage.getBtnSave().click();
            CSLogger.info("Clicked on save");
        }
    }

    /**
     * This method clicked on created project.
     * 
     * @param projectName String object contains project name
     */
    private void clkOnCreatedProject(String projectName) {
        WebElement project = browserDriver
                .findElement(By.linkText(projectName));
        project.click();
        CSUtility.waitForVisibilityOfElement(waitForReload, project);
        CSLogger.info("Clicked on Project " + projectName);
    }

    /**
     * This method verifies the creation of new project
     * 
     * @param projectName String object contains project name
     */
    private void verifyCreateNewProject(String projectName) {
        getProjectsNode();
        List<WebElement> projects = browserDriver
                .findElements(By.linkText(projectName));
        if (!projects.isEmpty()) {
            CSLogger.info(
                    "Project " + projectName + " is created successfully");
        } else {
            CSLogger.error(
                    "Project " + projectName + " is not created successfully");
            Assert.fail(
                    "Project " + projectName + " is not created successfully");
        }
    }

    /**
     * This method create of new project
     * 
     * @param projectName String object contains project name
     */
    private void createNewProject(String projectName) {
        CSUtility.rightClickTreeNode(waitForReload,
                projectManagerPage.getNodeProjects(), browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, projectName);
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * This method traverse to tree Node Templates
     */
    private void goToProjects() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        csPortalWidget.clkOnProjectManagerIcon(waitForReload);
        getProjectsNode();
    }

    /**
     * This Method get to the projects node
     */
    private void getProjectsNode() {
        traverseSettingModule.traverseToEditTemplate(waitForReload,
                browserDriver);
        projectManagerPage.clkOnBrowse(waitForReload);
        traverseSettingModule.traverseToBrowseProjectManager(waitForReload,
                browserDriver);
        projectManagerPage.clkOnBPMProjectManagerNode(waitForReload);
        projectManagerPage.clkOnProjectsNode(waitForReload);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains project name,Task name
     * 
     * @return createNewPorjectTaskSheet
     */
    @DataProvider(name = "CreateNewPorjectTaskData")
    public Object[] createNewPorjectTaskDataSheet() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("projectManagerAndTasks"),
                createNewPorjectTaskSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        settingPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPopupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        projectManagerPage = new ProjectManagerPage(browserDriver);
        traverseSettingModule = new TraversingForSettingsModule();
        csPortalWidget = new CSPortalWidget(browserDriver);
        actions = new Actions(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }
}
