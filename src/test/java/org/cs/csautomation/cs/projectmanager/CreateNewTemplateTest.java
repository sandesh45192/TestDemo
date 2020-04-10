/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.projectmanager;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.ISettingsPopup;
import org.cs.csautomation.cs.settings.ProjectManagerPage;
import org.cs.csautomation.cs.settings.SettingsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
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
 * This class contains the test methods which verifies the creation of New
 * Template.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateNewTemplateTest extends AbstractTest {

    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private CSPortalHeader              csPortalHeader;
    private IProductPopup               productPopUp;
    private ISettingsPopup              settingPopup;
    private CSPopupDivPim                  csPopupDiv;
    private ChoosePortalPage            choosePortalPage;
    private Actions                     actions;
    private SettingsPage                settingPage;
    private CSPortalWidget              csPortalWidget;
    private ProjectManagerPage          projectManagerPage;
    private TraversingForSettingsModule traverseSettingModule;
    private String                      createNewTemplateSheet = "CreateNewTemplate";

    /**
     * This method verifies the creation of New Template.
     * 
     * @param templateName String object contains Template name
     */
    @Test(dataProvider = "CreateNewTemplateData")
    public void testCreateNewTemplate(String templateName) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            clkOnLockAndUnlockOption();
            clkWidgetOptions();
            addProjectManagerIcon();
            CSUtility.tempMethodForThreadSleep(3000);
            clkOnLockAndUnlockOption();
            CSUtility.tempMethodForThreadSleep(1000);
            gotoTempletes();
            createNewTemplate(templateName, false);
            createNewTemplate(templateName, true);
        } catch (Exception e) {
            CSLogger.debug("Automation error : testCreateNewTemplate", e);
            Assert.fail("Automation error : testCreateNewTemplate", e);
        }
    }

    /**
     * This method traverse to tree Node Templates
     */
    private void gotoTempletes() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        csPortalWidget.clkOnProjectManagerIcon(waitForReload);
        getTemplateNode();
    }

    /**
     * This method create of New Template.
     * 
     * @param templateName String object contains Template name
     * @param pressKey Boolean Object
     */
    private void createNewTemplate(String templateName, Boolean pressKey) {
        getTemplateNode();
        CSUtility.rightClickTreeNode(waitForReload,
                projectManagerPage.getNodeTemplate(), browserDriver);
        settingPopup.selectPopupDivMenu(waitForReload,
                settingPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, templateName);
        csPopupDiv.askBoxWindowOperation(waitForReload, pressKey,
                browserDriver);
        verifyCreateNewTemplate(templateName, pressKey);
    }

    /**
     * This method verifies the creation of New Template.
     * 
     * @param templateName String object contains Template name
     * @param pressKey Boolean Object
     */
    private void verifyCreateNewTemplate(String templateName,
            Boolean pressKey) {
        getTemplateNode();
        List<WebElement> template = browserDriver
                .findElements(By.linkText(templateName));
        Assert.assertEquals(template.isEmpty(), !pressKey);
    }

    /**
     * This method click on LockAndUnlock option
     */
    private void clkOnLockAndUnlockOption() {
        clkPortalLinkOptions();
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getCsGuiPopupLockAndUnlockPortal()));
        productPopUp.getCsGuiPopupLockAndUnlockPortal().click();
        CSLogger.info("Clicked on Lock and Unlock Portal");
    }

    /**
     * This method Click on Portal Link Option
     * 
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * This method click on widget option
     * 
     */
    private void clkWidgetOptions() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        moveToWebElement(browserDriver.findElement(
                By.xpath("//span[contains(text(),'CORE Portal Studio')]")));
        waitForReload.until(
                ExpectedConditions.visibilityOf(settingPage.getWidgetBtnBar()));
        CSUtility.tempMethodForThreadSleep(1000);
        settingPage.getBtnWidgetPortal().click();
    }

    /**
     * This method mouse hover to the element
     * 
     * @param element WebElement
     */
    private void moveToWebElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    /**
     * This method add project manager icon to widget
     */
    private void addProjectManagerIcon() {
        traverseToCsPortalWindowFrame();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                choosePortalPage.getIconSecuredOptions());
        choosePortalPage.getIconSecuredOptions().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//div[@class='CSPortalGuiFormInputSection "
                                + "EditorField']")));
        if (!chkProjectManagerInSelectedSection()) {
            browserDriver.findElement(By.xpath(
                    "//select[contains(@id,'StudiosAllowedNotSelected')]"))
                    .click();
            Select select = new Select(browserDriver.findElement(By.xpath(
                    "//select[contains(@id,'StudiosAllowedNotSelected')]")));
            select.selectByValue("Project Manager");
            browserDriver
                    .findElement(
                            By.xpath("//input[contains(@style,'forward.png')]"))
                    .click();
        }
        CSUtility.tempMethodForThreadSleep(1000);
        choosePortalPage.clkBtnApply();
        CSLogger.info("Clicked on Apply button");
        choosePortalPage.clkBtnSave();
        CSLogger.info("Clicked on save button");
        CSLogger.info("Added project manager to widget.");
    }

    /**
     * This method traverse to widget window
     * 
     * @return Boolean value
     */
    private Boolean chkProjectManagerInSelectedSection() {
        List<String> SelectedSection = new ArrayList<String>();
        int size = browserDriver.findElements(By.xpath(
                "//select[contains(@id,'StudiosAllowedMultiSelect')]//option"))
                .size();
        for (int count = 1; count <= size; count++) {
            SelectedSection.add(browserDriver.findElement(By
                    .xpath("//select[contains(@id,'StudiosAllowedMultiSelect')]"
                            + "//option[" + count + "]"))
                    .getText());
        }
        if (SelectedSection.contains("Project Manager")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method traverse to widget window
     * 
     */
    private void traverseToCsPortalWindowFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
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
     * contains Template name
     * 
     * @return createNewTemplateSheet
     */
    @DataProvider(name = "CreateNewTemplateData")
    public Object[] createNewTemplateDataSheet() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("projectManagerAndTasks"),
                createNewTemplateSheet);
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
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        settingPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPopupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        choosePortalPage = new ChoosePortalPage(browserDriver);
        actions = new Actions(browserDriver);
        settingPage = new SettingsPage(browserDriver);
        csPortalWidget = new CSPortalWidget(browserDriver);
        projectManagerPage = new ProjectManagerPage(browserDriver);
        traverseSettingModule = new TraversingForSettingsModule();
    }
}
