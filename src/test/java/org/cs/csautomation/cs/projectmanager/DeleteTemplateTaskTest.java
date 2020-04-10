/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.projectmanager;

import java.util.ArrayList;
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
 * This class contains the test methods which verifies deletion of Template
 * Task.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteTemplateTaskTest extends AbstractTest {

    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private CSPortalHeader              csPortalHeader;
    private ISettingsPopup              settingPopup;
    private CSPopupDivPim               csPopupDiv;
    private TraversingForSettingsModule traverseSettingModule;
    private Actions                     actions;
    private CSPortalWidget              csPortalWidget;
    private ProjectManagerPage          projectManagerPage;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontalInstance;
    private String                      deleteTemplateTaskSheet = "DeleteTemplateTask";

    /**
     * This method verifies the deletion of Template Task.
     * 
     * @param templateName String object contains Template name
     * @param taskName String object contains task name
     * @param deleteType String object contains delete type name
     */
    @Test(dataProvider = "DeleteTemplateTaskData")
    public void testDeleteTemplateTask(String templateName, String taskName,
            String deleteType) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            gotoTemplates();
            if (deleteType.equalsIgnoreCase("rightclick")) {
                createDataForTestCase(templateName, taskName);
                deleteViaRightClick(false);
                deleteViaRightClick(true);
            } else if (deleteType.equalsIgnoreCase("deleteicon")) {
                createDataForTestCase(templateName, taskName);
                deleteViaDeleteIcon(false);
                deleteViaDeleteIcon(true);
            } else {
                createDataForTestCase(templateName, taskName);
                deleteViaMassEditing(false);
                deleteViaMassEditing(true);
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteTemplateTask", e);
            Assert.fail("Automation error : testDeleteTemplateTask", e);
        }
    }

    /**
     * This method create the Tasks for test case.
     * 
     * @param templateName String object contains Template name
     * @param taskName String object contains task name
     */
    private void createDataForTestCase(String templateName, String taskName) {
        createNewTemplate(templateName);
        clkOnTemplate(templateName);
        CSUtility.tempMethodForThreadSleep(1000);
        createNewTask(taskName);
    }

    /**
     * This method delete the Template Task via Right click.
     * 
     * @param pressKey Boolean object
     */
    private void deleteViaRightClick(Boolean pressKey) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        ArrayList<String> taskLabels = new ArrayList<String>();
        WebElement element = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        taskLabels.add(browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[3]"))
                .getText());
        actions.contextClick(element).build().perform();
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuObject(), browserDriver);
        settingPopup.selectPopupDivSubMenu(waitForReload,
                settingPopup.getObjectDeleteSubMenu(), browserDriver);
        Alert alert = getAlert();
        if (pressKey) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            verifydelete(taskLabels, pressKey);
        } else {
            alert.dismiss();
            CSLogger.info("Clicked on Cancel of pop up");
            verifydelete(taskLabels, pressKey);
        }
        CSLogger.info("Delete Task via Right Click complete");
    }

    /**
     * This method delete the Template Task via Delete Icon.
     * 
     * @param pressKey Boolean object
     */
    private void deleteViaDeleteIcon(Boolean pressKey) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        ArrayList<String> taskLabels = new ArrayList<String>();
        WebElement element = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[3]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        taskLabels.add(browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[3]"))
                .getText());
        element.click();
        CSUtility.tempMethodForThreadSleep(1000);
        traverseSettingModule.traverseToEditFrameTask(waitForReload,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupTask(), browserDriver);
        settingPopup.selectPopupDivSubMenu(waitForReload,
                settingPopup.getObjectDeleteSubMenu(), browserDriver);
        Alert alert = getAlert();
        if (pressKey) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            verifydelete(taskLabels, pressKey);
        } else {
            alert.dismiss();
            CSLogger.info("Clicked on Cancel of pop up");
            verifydelete(taskLabels, pressKey);
        }
        CSLogger.info("Delete Task via Delete Icon complete");
    }

    /**
     * This method delete the Template Task via Mass Editing.
     * 
     * @param pressKey Boolean object
     */
    private void deleteViaMassEditing(Boolean pressKey) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        ArrayList<String> taskLabels = new ArrayList<String>();
        for (int row = 3; row <= 4; row++) {
            browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                            + row + "]/td[1]"))
                    .click();
            taskLabels.add(browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                            + row + "]/td[3]"))
                    .getText());
        }
        Select bottomDropdown = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        bottomDropdown.selectByVisibleText("Delete");
        Alert alert = getAlert();
        if (pressKey) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            verifydelete(taskLabels, pressKey);
        } else {
            alert.dismiss();
            CSLogger.info("Clicked on Cancel of pop up");
            verifydelete(taskLabels, pressKey);
        }
        CSLogger.info("Delete Task via Mass Editing complete");
    }

    /**
     * This method get the alert popup
     * 
     * @return alert contains alert popup element
     */
    private Alert getAlert() {
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        return alert;
    }

    /**
     * This method verifies the delete test
     * 
     * @param taskLabels ArrayList object contains list of Label
     */
    private void verifydelete(ArrayList<String> taskLabels, Boolean pressKey) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getBtnRefresh());
        projectManagerPage.getBtnRefresh().click();
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement table = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, table);
        int jobsCount = table.findElements(By.tagName("tr")).size();
        ArrayList<String> latestRecordIds = new ArrayList<String>();
        for (int row = 3; row < jobsCount; row++) {
            latestRecordIds.add(browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                            + row + "]/td[3]"))
                    .getText());
        }
        for (String deletedLabel : taskLabels) {
            if (pressKey) {
                if (!latestRecordIds.contains(deletedLabel)) {
                    CSLogger.info(
                            "Delete Successful for label " + deletedLabel);
                } else {
                    CSLogger.error("Delete fail for label " + deletedLabel);
                    Assert.fail("Delete fail for label " + deletedLabel);
                }
            } else {
                if (latestRecordIds.contains(deletedLabel)) {
                    CSLogger.info("Click Cancel Successful");
                } else {
                    CSLogger.error("Click Cancel Fail");
                    Assert.fail("Click Cancel Fail");
                }
            }
        }
    }

    /**
     * This method create of New Template Task.
     * 
     * @param taskName String object contains task name
     * @param ownerName String object contains owner name of task
     */
    private void createNewTask(String taskName) {
        String tasks[] = taskName.split(",");
        for (String label : tasks) {
            traverseSettingModule.traverseToEditFrameProjectManager(
                    waitForReload, browserDriver);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCreateNew(waitForReload);
            traverseSettingModule.traverseToEditFrameTask(waitForReload,
                    browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    projectManagerPage.getTxtTaskLabel());
            projectManagerPage.getTxtTaskLabel().sendKeys(label);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    projectManagerPage.getBtnSetOwner());
            projectManagerPage.getBtnSetOwner().click();
            projectManagerPage.getBtnSave().click();
            CSLogger.info("New Task is created." + label);
        }
    }

    /**
     * This method click on New Template.
     * 
     * @param templateName String object contains Template name
     */
    private void clkOnTemplate(String templateName) {
        getTemplateNode();
        WebElement template = browserDriver
                .findElement(By.linkText(templateName));
        template.click();
        CSLogger.info("Clicked on Template " + templateName);
    }

    /**
     * This method create of New Template.
     * 
     * @param templateName String object contains Template name
     * @param pressKey Boolean Object
     */
    private void createNewTemplate(String templateName) {
        getTemplateNode();
        CSUtility.rightClickTreeNode(waitForReload,
                projectManagerPage.getNodeTemplate(), browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, templateName);
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * This method traverse to tree Node Templates
     */
    private void gotoTemplates() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        csPortalWidget.clkOnProjectManagerIcon(waitForReload);
        getTemplateNode();
    }

    /**
     * This Method get to the template node
     */
    private void getTemplateNode() {
        traverseSettingModule.traverseToEditTemplate(waitForReload,
                browserDriver);
        projectManagerPage.clkOnBrowse(waitForReload);
        traverseSettingModule.traverseToBrowseProjectManager(waitForReload,
                browserDriver);
        projectManagerPage.clkOnSettingNode(waitForReload);
        projectManagerPage.clkOnTemplatesNode(waitForReload);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Template name, Task name, Delete type
     * 
     * @return deleteTemplateTaskSheet
     */
    @DataProvider(name = "DeleteTemplateTaskData")
    public Object[] deleteTemplateTaskDataSheet() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("projectManagerAndTasks"),
                deleteTemplateTaskSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 30);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        settingPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPopupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        actions = new Actions(browserDriver);
        csPortalWidget = new CSPortalWidget(browserDriver);
        projectManagerPage = new ProjectManagerPage(browserDriver);
        traverseSettingModule = new TraversingForSettingsModule();
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }
}
