/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.projectmanager;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
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
 * This class contains the test methods which verifies deletion of Task.
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteProductTaskTest extends AbstractTest {

    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private CSPortalHeader              csPortalHeader;
    private IProductPopup               productPopUp;
    private PimStudioProductsNodePage   pimStudioProductsNode;
    private ISettingsPopup              settingPopup;
    private CSPopupDivPim               csPopupDiv;
    private TraversingForSettingsModule traverseSettingModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private IMoreOptionPopup            moreOptionPopup;
    private Actions                     actions;
    private ProjectManagerPage          projectManagerPage;
    private String                      deleteProductTaskSheet = "DeleteProductTask";

    /**
     * This method verifies the deletion of Task.
     * 
     * @param productName String object contains product name
     * @param taskName String object contains task name
     * @param deleteType String object contains delete type name
     */
    @Test(dataProvider = "DeleteProductTaskData")
    public void testDeleteProductTask(String productName, String taskName,
            String deleteType) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            if (deleteType.equalsIgnoreCase("rightclick")) {
                createDataForTestCase(productName, taskName);
                deleteViaRightClick(false);
                deleteViaRightClick(true);
            } else if (deleteType.equalsIgnoreCase("deleteicon")) {
                createDataForTestCase(productName, taskName);
                deleteViaDeleteIcon(false);
                deleteViaDeleteIcon(true);
            } else {
                createDataForTestCase(productName, taskName);
                deleteViaMassEditing(false);
                deleteViaMassEditing(true);
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error : testDeleteProductTask", e);
            Assert.fail("Automation error : testDeleteProductTask", e);
        }
    }

    /**
     * This method create the Tasks for test case.
     * 
     * @param productName String object contains product name
     * @param taskName String object contains task name
     */
    private void createDataForTestCase(String productName, String taskName) {
        createNewProduct(productName);
        createNewTask(productName, taskName);
    }

    /**
     * This method creates the new product folder
     * 
     * @param nameOfProduct it contains the name of the product
     */
    public void createNewProduct(String nameOfProduct) {
        clkProductFolder();
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        CSLogger.info("Right clicked on Products node.");
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopUp.enterValueInDialogue(waitForReload, nameOfProduct);
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * This method clicks on the product folder
     * 
     */
    public void clkProductFolder() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("Clicked on Products Folder");
    }

    /**
     * This method delete the Task via Right click.
     * 
     * @param pressKey Boolean object
     */
    private void deleteViaRightClick(Boolean pressKey) {
        traverseSettingModule.traverseToTaskSection(waitForReload,
                browserDriver, locator);
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
            verifyDelete(taskLabels, pressKey);
        } else {
            alert.dismiss();
            CSLogger.info("Clicked on Cancel of pop up");
            verifyDelete(taskLabels, pressKey);
        }
        CSLogger.info("Delete Task via Right Click complete");
    }

    /**
     * This method delete the Task via Delete Icon.
     * 
     * @param pressKey Boolean object
     */
    private void deleteViaDeleteIcon(Boolean pressKey) {
        traverseSettingModule.traverseToTaskSection(waitForReload,
                browserDriver, locator);
        ArrayList<String> taskLabels = new ArrayList<String>();
        WebElement element = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[3]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        taskLabels.add(browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td[3]"))
                .getText());
        element.click();
        CSUtility.tempMethodForThreadSleep(1000);
        goToEditTaskWindow(pressKey);
        verifyDelete(taskLabels, pressKey);
        CSLogger.info("Delete Task via Delete Icon complete");
    }

    /**
     * This method get to the Task editing window.
     * 
     * @param pressKey Boolean object
     */
    private void goToEditTaskWindow(Boolean pressKey) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontal.clkBtnShowMoreOption(waitForReload);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupTask(), browserDriver);
        settingPopup.selectPopupDivSubMenu(waitForReload,
                settingPopup.getObjectDeleteSubMenu(), browserDriver);
        Alert alert = getAlert();
        if (pressKey) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.switchToDefaultFrame(browserDriver);
            projectManagerPage.clkOnButtonWindowClose(waitForReload);
        } else {
            alert.dismiss();
            CSLogger.info("Clicked on Cancel of pop up");
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.switchToDefaultFrame(browserDriver);
            projectManagerPage.clkOnButtonWindowClose(waitForReload);
        }
        CSLogger.info("Window Closed");
    }

    /**
     * This method delete the Task via Mass Editing.
     * 
     * @param pressKey Boolean object
     */
    private void deleteViaMassEditing(Boolean pressKey) {
        traverseSettingModule.traverseToTaskSection(waitForReload,
                browserDriver, locator);
        ArrayList<String> taskLabels = new ArrayList<String>();
        getLabelsFromTable(taskLabels, 3);
        CSUtility.tempMethodForThreadSleep(2000);
        getLabelsFromTable(taskLabels, 4);
        Select bottomDropdown = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        bottomDropdown.selectByVisibleText("Delete");
        Alert alert = getAlert();
        if (pressKey) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
            verifyDelete(taskLabels, pressKey);
        } else {
            alert.dismiss();
            CSLogger.info("Clicked on Cancel of pop up");
            verifyDelete(taskLabels, pressKey);
        }
        CSLogger.info("Delete Task via Mass Editing complete");
    }

    /**
     * This method get the labels from the table.
     * 
     * @param taskLabels ArrayList object contain
     */
    private void getLabelsFromTable(ArrayList<String> taskLabels, int row) {
        browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                        + row + "]/td[1]"))
                .click();
        taskLabels.add(browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr["
                        + row + "]/td[3]"))
                .getText());
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
    private void verifyDelete(ArrayList<String> taskLabels, Boolean pressKey) {
        traverseSettingModule.traverseToTaskSection(waitForReload,
                browserDriver, locator);
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
     * This method create of New Task.
     * 
     * @param productName String object contains name of product
     * @param taskName String object contains task name
     */
    private void createNewTask(String productName, String taskName) {
        String tasks[] = taskName.split(",");
        for (String taskLabel : tasks) {
            selectTaskOption(productName);
            traverseSettingModule.traverseToTaskSection(waitForReload,
                    browserDriver, locator);
            csGuiToolbarHorizontal.clkBtnCSGuiToolbarCreateNew(waitForReload);
            enterLabelToTask(taskLabel);
        }
    }

    /**
     * This method enter the Label name for new Task
     * 
     * @param taskName String object contains task name
     */
    private void enterLabelToTask(String taskName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(projectManagerPage.getTxtTaskLabel()));
        projectManagerPage.getTxtTaskLabel().sendKeys(taskName);
        waitForReload.until(ExpectedConditions
                .visibilityOf(projectManagerPage.getBtnSave()));
        projectManagerPage.getBtnSave().click();
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.switchToDefaultFrame(browserDriver);
        projectManagerPage.clkOnButtonWindowClose(waitForReload);
        CSLogger.info("Entered Label to Task");
        CSLogger.info("closed task window");
    }

    /**
     * This method Select the task option
     * 
     * @param productName String object contains owner name of product
     */
    private void selectTaskOption(String productName) {
        getProductEditWindow(productName);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontal.clkBtnShowMoreOption(waitForReload);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupTools(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        moreOptionPopup.selectPopupDivSubMenu(waitForReload,
                moreOptionPopup.getcsGuiPopupSubTasks(), browserDriver);
        CSLogger.info("Select Task option");
    }

    /**
     * This method traverse to product edit window
     * 
     * @param productName String object contains owner name of product
     */
    private void getProductEditWindow(String productName) {
        clkProductFolder();
        WebElement product = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.rightClickTreeNode(waitForReload, product, browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupSubMenuEdit(), browserDriver);
        CSLogger.info("Open product edit window");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Product name, Task name, Delete type
     * 
     * @return deleteProductTaskSheet
     */
    @DataProvider(name = "DeleteProductTaskData")
    public Object[] deleteProductTaskDataSheet() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("projectManagerAndTasks"),
                deleteProductTaskSheet);
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
        projectManagerPage = new ProjectManagerPage(browserDriver);
        traverseSettingModule = new TraversingForSettingsModule();
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        moreOptionPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
    }
}
