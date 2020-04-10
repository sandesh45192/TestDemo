/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.projectmanager;

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
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
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
 * This class contains the test methods which verifies the creation of New
 * Template Task.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateNewTemplateTaskTest extends AbstractTest {

    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private CSPortalHeader              csPortalHeader;
    private ISettingsPopup              settingPopup;
    private CSPopupDivPim               csPopupDiv;
    private TraversingForSettingsModule traverseSettingModule;
    private Actions                     actions;
    private CSPortalWidget              csPortalWidget;
    private ProjectManagerPage          projectManagerPage;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private String                      createNewTemplateTaskSheet = "CreateNewTemplateTask";

    /**
     * This method verifies the creation of New Template Task.
     * 
     * @param templateName
     *            String object contains Template name
     * @param taskName
     *            String object contains task name
     * @param ownerName
     *            String object contains owner name of task
     */
    @Test(dataProvider = "CreateNewTemplateTaskData")
    public void testCreateNewTemplateTask(String templateName, String taskName,
            String ownerName) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            gotoTempletesNode();
            createNewTemplate(templateName, true);
            clkOnTemplate(templateName);
            CSUtility.tempMethodForThreadSleep(2000);
            createNewTask(taskName, ownerName);
            verifyNewTaskCreation(templateName, taskName, ownerName);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testCreateNewTemplateTask", e);
            Assert.fail("Automation error : testCreateNewTemplateTask", e);
        }
    }

    /**
     * This method verifies creation of New Template Task.
     * 
     * @param templateName
     *            String object contains Template name
     * @param taskName
     *            String object contains task name
     * @param ownerName
     *            String object contains owner name of task
     */
    private void verifyNewTaskCreation(String templateName, String taskName,
            String ownerName) {
        String template = null;
        String owner = null;
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        WebElement table = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        int count = table.findElements(By.tagName("tr")).size();
        for (int row = 3; row < count; row++) {
            WebElement elementLabel = browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                            + row + "]/td[3]"));
            String label = elementLabel.getText();
            if (label.equalsIgnoreCase(taskName)) {
                WebElement elementTemplate = browserDriver.findElement(By
                        .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                                + row + "]/td[4]"));
                WebElement elementOwner = browserDriver.findElement(By
                        .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                                + row + "]/td[5]"));
                template = elementTemplate.getText();
                owner = elementOwner.getText();
                break;
            }
        }
        if (template.contains(templateName)) {
            CSLogger.info("Template name verified");
        } else {
            CSLogger.error("Template name not verified");
            Assert.fail("Template name not verified");
        }
        if (owner.contains(ownerName)) {
            CSLogger.info("Owner name verified");
        } else {
            CSLogger.error("Owner name not verified");
            Assert.fail("Owner name not verified");
        }
    }

    /**
     * This method create of New Template Task.
     * 
     * @param taskName
     *            String object contains task name
     * @param ownerName
     *            String object contains owner name of task
     */
    private void createNewTask(String taskName, String ownerName) {
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCreateNew(waitForReload);
        traverseSettingModule.traverseToEditFrameTask(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getTxtTaskLabel());
        projectManagerPage.getTxtTaskLabel().sendKeys(taskName);
        projectManagerPage.getBtnAddOwner().click();
        setTaskOwner(ownerName);
        traverseSettingModule.traverseToEditFrameTask(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                projectManagerPage.getBtnSetOwner());
        projectManagerPage.getBtnSetOwner().click();
        projectManagerPage.getBtnSave().click();
        CSLogger.info("New Task is created.");
    }

    /**
     * This method create of New Template.
     * 
     * @param ownerName
     *            String object contains owner name of task
     */
    private void setTaskOwner(String ownerName) {
        String userName = null;
        TraverseSelectionDialogFrames.traverseTillUserFoldersleftFrames(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.id("User@0")));
        WebElement userTree = browserDriver.findElement(By.id("User@0"));
        userTree.click();
        WebElement elementUser = browserDriver
                .findElement(By.linkText("System Users"));
        actions.doubleClick(elementUser).build().perform();
        TraverseSelectionDialogFrames.traverseTillUserFoldersCenterPane(
                waitForReload, browserDriver);
        WebElement element = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        int count = element.findElements(By.tagName("tr")).size();
        for (int row = 3; row <= count; row++) {
            WebElement user = browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                            + row + "]/td[2]"));
            userName = user.getText();
            if (userName.contains(ownerName)) {
                actions.doubleClick(user).build().perform();
            }
        }
        CSLogger.info(ownerName + " is set as a owner.");
    }

    /**
     * This method click on New Template.
     * 
     * @param templateName
     *            String object contains Template name
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
     * @param templateName
     *            String object contains Template name
     * @param pressKey
     *            Boolean Object
     */
    private void createNewTemplate(String templateName, Boolean pressKey) {
        CSUtility.rightClickTreeNode(waitForReload,
                projectManagerPage.getNodeTemplate(), browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, templateName);
        csPopupDiv.askBoxWindowOperation(waitForReload, pressKey,
                browserDriver);
    }

    /**
     * This method traverse to tree Node Templates
     */
    private void gotoTempletesNode() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        csPortalWidget.clkOnProjectManagerIcon(waitForReload);
        getTemplateNode();
        CSLogger.info("Clicked on template Node");
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
     * contains Template name, Task name and task owner name
     * 
     * @return createNewTemplateTaskSheet
     */
    @DataProvider(name = "CreateNewTemplateTaskData")
    public Object[] createNewTemplateTaskDataSheet() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("projectManagerAndTasks"),
                createNewTemplateTaskSheet);
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
        traverseSettingModule = new TraversingForSettingsModule();
        actions = new Actions(browserDriver);
        csPortalWidget = new CSPortalWidget(browserDriver);
        projectManagerPage = new ProjectManagerPage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }
}
