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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies deletion of Template.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteTemplateTest extends AbstractTest {

    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private CSPortalHeader              csPortalHeader;
    private ISettingsPopup              settingPopup;
    private CSPopupDivPim                  csPopupDiv;
    private TraversingForSettingsModule traverseSettingModule;
    private Actions                     actions;
    private CSPortalWidget              csPortalWidget;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private ProjectManagerPage          projectManagerPage;
    private String                      deleteTemplateSheet = "DeleteTemplate";

    /**
     * This method verifies the deletion of Template .
     * 
     * @param templateName String object contains Template name
     * @param taskName String object contains task name
     */
    @Test(dataProvider = "DeleteTemplateData")
    public void testDeleteTemplate(String templateName, String taskName) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            gotoTemplates();
            createDataForTestCase(templateName, taskName);
            deleteFromTreeView(templateName, false);
            deleteFromTreeView(templateName, true);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteTemplate", e);
            Assert.fail("Automation error : testDeleteTemplate", e);
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
     * This method delete the template from tree view.
     * 
     * @param templateName String object contains Template name
     * @param pressKey Boolean object
     */
    private void deleteFromTreeView(String templateName, Boolean pressKey) {
        getTemplateNode();
        WebElement treeView = browserDriver
                .findElement(By.linkText(templateName));
        CSUtility.waitForVisibilityOfElement(waitForReload, treeView);
        CSUtility.rightClickTreeNode(waitForReload, treeView, browserDriver);
//        actions.contextClick(treeView).build().perform();
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuObject(), browserDriver);
        settingPopup.selectPopupDivSubMenu(waitForReload,
                settingPopup.getObjectDeleteSubMenu(), browserDriver);
        Alert alert = getAlert();
        if (pressKey) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            verifyDeleteViaTreeView(templateName, pressKey);
        } else {
            alert.dismiss();
            CSLogger.info("Clicked on Cancel of pop up");
            verifyDeleteViaTreeView(templateName, pressKey);
        }
        CSLogger.info("Delete Template via Tree view is complete");
    }

    /**
     * This method verifies the deletion of Template.
     * 
     * @param templateName String object contains Template name
     * @param pressKey Boolean object
     */
    private void verifyDeleteViaTreeView(String templateName,
            Boolean pressKey) {
        getTemplateNode();
        List<WebElement> templates = browserDriver
                .findElements(By.linkText(templateName));
        if (pressKey) {
            if (templates.isEmpty()) {
                CSLogger.info("Delete Successful for label " + templateName);
            } else {
                CSLogger.error("Delete fail for label " + templateName);
                Assert.fail("Delete fail for label " + templateName);
            }
        } else {
            if (!templates.isEmpty()) {
                CSLogger.info("Click Cancel Successful");
            } else {
                CSLogger.error("Click Cancel Fail");
                Assert.fail("Click Cancel Fail");
            }
        }
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
     * This method create of New Template Task.
     * 
     * @param taskName String object contains task name
     * @param ownerName String object contains owner name of task
     */
    private void createNewTask(String taskName) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCreateNew(waitForReload);
        traverseSettingModule.traverseToEditFrameTask(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getTxtTaskLabel());
        projectManagerPage.getTxtTaskLabel().sendKeys(taskName);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getBtnSetOwner());
        projectManagerPage.getBtnSetOwner().click();
        projectManagerPage.clkOnButtonSave(waitForReload);
        CSLogger.info("New Task is created." + taskName);
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
        waitForReload.until(ExpectedConditions.visibilityOf(template));
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
     * contains Template name, Task name
     * 
     * @return deleteTemplateSheet
     */
    @DataProvider(name = "DeleteTemplateData")
    public Object[] deleteTemplateDataSheet() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("projectManagerAndTasks"),
                deleteTemplateSheet);
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
