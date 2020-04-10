/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.projectmanager;

import java.util.List;
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies deletion of Project
 * folder.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteProjectFloderTest extends AbstractTest {

    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private CSPortalHeader              csPortalHeader;
    private ISettingsPopup              settingPopup;
    private CSPopupDivPim                  csPopupDiv;
    private TraversingForSettingsModule traverseSettingModule;
    private CSPortalWidget              csPortalWidget;
    private ProjectManagerPage          projectManagerPage;
    private String                      deleteProjectSheet = "DeleteProject";

    /**
     * This method verifies the deletion of Project Folder.
     * 
     * @param projectName String object contains project folder name
     * @param deleteType String object contains delete type name
     */
    @Test(dataProvider = "DeleteProjectData")
    public void testDeleteProjectFolder(String projectName, String deleteType) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            if (deleteType.equalsIgnoreCase("treeview")) {
                createNewProject(projectName);
                deleteFromTreeView(projectName, false);
                deleteFromTreeView(projectName, true);
            } else if (deleteType.equalsIgnoreCase("listview")) {
                createNewProject(projectName);
                deleteFromListView(projectName, false);
                deleteFromListView(projectName, true);
            } else if (deleteType.equalsIgnoreCase("massedit")) {
                createNewProject(projectName);
                deleteViaMassEditing(projectName, false);
                deleteViaMassEditing(projectName, true);
            } else {
                createNewProject(projectName);
                goToProjectsEditWindow(projectName);
                verifyDeleteButton();
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteProjectFolder", e);
            Assert.fail("Automation error : testDeleteProjectFolder", e);
        }
    }

    /**
     * This method verifies the presence of delete button in editor window.
     * 
     */
    private void verifyDeleteButton() {
        try {
            traverseSettingModule.traverseToSettingConfigurationProjectManager(
                    waitForReload, browserDriver, locator);
            List<WebElement> toolbar = browserDriver.findElements(
                    By.xpath("//table[@class='CSGuiToolbarHorizontalTable']"
                            + "//tbody//tr//td//a//img"));
            String iconImage = null;
            for (int i = 0; i < toolbar.size(); i++) {
                iconImage = toolbar.get(i).getAttribute("src");
                if (iconImage.contains("delete")) {
                    CSLogger.error("Delete icon is present in editor window");
                    Assert.fail("Delete icon is present in editor window");
                }
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error : verifyDeleteButton", e);
        }
    }

    /**
     * This method get the editor window of project
     * 
     * @param projectName String object contains project folder name
     */
    private void goToProjectsEditWindow(String projectName) {
        getProjectsNode();
        WebElement treeView = browserDriver
                .findElement(By.linkText(projectName));
        CSUtility.waitForVisibilityOfElement(waitForReload, treeView);
        CSUtility.rightClickTreeNode(waitForReload, treeView, browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupSubMenuEdit(), browserDriver);
        CSLogger.info("Clicked on edit button");
    }

    /**
     * This method delete the project via Mass Editing.
     * 
     * @param projectName String object contains project folder name
     * @param pressKey Boolean object
     */
    private void deleteViaMassEditing(String projectName, Boolean pressKey) {
        openProjectInListView(projectName);
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        WebElement elementInTable = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[1]"));
        elementInTable.click();
        Select bottomDrpdwn = new Select(
                projectManagerPage.getDrpdwnMassEdit());
        bottomDrpdwn.selectByVisibleText("Delete");
        getAlert(pressKey);
        verifyDeleteViaTreeView(projectName, pressKey);
        CSLogger.info("Delete Task via Mass Editing complete");
    }

    /**
     * This method delete the project from list view.
     * 
     * @param projectName String object contains project folder name
     * @param pressKey Boolean object
     */
    private void deleteFromListView(String projectName, Boolean pressKey) {
        openProjectInListView(projectName);
        traverseSettingModule.traverseToEditFrameProjectManager(waitForReload,
                browserDriver);
        WebElement elementInTable = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[1]"));
        CSUtility.rightClickTreeNode(waitForReload, elementInTable, browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuObject(), browserDriver);
        settingPopup.selectPopupDivSubMenu(waitForReload,
                settingPopup.getObjectDeleteSubMenu(), browserDriver);
        getAlert(pressKey);
        verifyDeleteViaTreeView(projectName, pressKey);
    }

    /**
     * This method open the project folder in List view
     * 
     * @param projectName String object contains project folder name
     */
    private void openProjectInListView(String projectName) {
        getProjectsNode();
        WebElement treeView = browserDriver
                .findElement(By.linkText(projectName));
        CSUtility.waitForVisibilityOfElement(waitForReload, treeView);
        CSUtility.rightClickTreeNode(waitForReload, treeView, browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getcsGuiPopupListProjects(), browserDriver);
        CSLogger.info("Open Project List view");
    }

    /**
     * This method delete the project from tree view.
     * 
     * @param projectName String object contains project folder name
     * @param pressKey Boolean object
     */
    private void deleteFromTreeView(String projectName, Boolean pressKey) {
        getProjectsNode();
        WebElement treeView = browserDriver
                .findElement(By.linkText(projectName));
        CSUtility.waitForVisibilityOfElement(waitForReload, treeView);
        CSUtility.rightClickTreeNode(waitForReload, treeView, browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuObject(), browserDriver);
        settingPopup.selectPopupDivSubMenu(waitForReload,
                settingPopup.getObjectDeleteSubMenu(), browserDriver);
        getAlert(pressKey);
        verifyDeleteViaTreeView(projectName, pressKey);
        CSLogger.info("Delete Template via Tree view is complete");
    }

    private void getAlert(Boolean pressKey) {
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (pressKey) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
        } else {
            alert.dismiss();
            CSLogger.info("Clicked on Cancel of pop up");
        }
    }
    
    /**
     * This method verifies the deletion of project folder.
     * 
     * @param projectName String object contains project folder name
     * @param pressKey Boolean object
     */
    private void verifyDeleteViaTreeView(String projectName, Boolean pressKey) {
        getProjectsNode();
        List<WebElement> templates = browserDriver
                .findElements(By.linkText(projectName));
        if (pressKey) {
            if (templates.isEmpty()) {
                CSLogger.info("Delete Successful for label " + projectName);
            } else {
                CSLogger.error("Delete fail for label " + projectName);
                Assert.fail("Delete fail for label " + projectName);
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
     * This method create of new project
     * 
     * @param projectName String object contains project name
     */
    private void createNewProject(String projectName) {
        getProjectsNode();
        CSUtility.rightClickTreeNode(waitForReload,
                projectManagerPage.getNodeProjects(), browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, projectName);
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * This Method get to the projects node
     */
    private void getProjectsNode() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnProjectManagerIcon(waitForReload);
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
     * contains Project name,delete type
     * 
     * @return deleteProjectSheet
     */
    @DataProvider(name = "DeleteProjectData")
    public Object[] deleteProjectDataSheet() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("projectManagerAndTasks"),
                deleteProjectSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        settingPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPopupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPortalWidget = new CSPortalWidget(browserDriver);
        projectManagerPage = new ProjectManagerPage(browserDriver);
        traverseSettingModule = new TraversingForSettingsModule();
    }
}
