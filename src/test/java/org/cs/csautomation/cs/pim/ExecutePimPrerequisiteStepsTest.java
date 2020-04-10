/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.CSPortalWidget;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class contains test methods which performs some prerequisite operation.
 * 
 * @author CSAutomation Team
 *
 */
public class ExecutePimPrerequisiteStepsTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private FrameLocators             iframeLocatorsInstance;
    private CSPortalWidget            csPortalWidgetInstance;
    private SettingsPage              settingsPageInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private PimStudioProductsNodePage pimStudioProductsNodePageInstance;
    private Actions                   perfromAction;
    private IMoreOptionPopup          moreOptionPopup;
    private CSPopupDivPim             csPopupDivPimInstance;

    /**
     * This test method disables the 'Use new studio list' checkbox.
     */
    @Test(priority = 1)
    public void testSwitchStudioList() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToSettingsPage();
            goToSystemPreferencesIcon();
            disableUseNewStudioListCheckbox();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error found in test method:testSwitchStudioList, "
                            + "refer to logs for more details check logs",
                    e);
            Assert.fail(
                    "Automation error found in test method:testSwitchStudioList,"
                            + " refer to logs for more details check logs",
                    e);
        }
    }

    /**
     * This test method sets the products view to 'List View'.
     */
    @Test(priority = 2)
    public void testSetPimProductsInListView() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPIMandExpandProductTree(waitForReload);
            doubleClickOnProductsNode();
            performOperationToSetListView();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error found in test testSetPimProductsInListView, "
                            + "refer to logs for more details check logs",
                    e);
            Assert.fail(
                    "Automation error found in test testSetPimProductsInListView,"
                            + " refer to logs for more details check logs",
                    e);
        }
    }

    /**
     * Sets the products view to 'List View'
     */
    private void performOperationToSetListView() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        moreOptionPopup.selectPopupDivMenu(waitForReload,
                moreOptionPopup.getCtxView(), browserDriver);
        moreOptionPopup.selectPopupDivSubMenu(waitForReload,
                moreOptionPopup.getCtxMoreOptionDisplay(), browserDriver);
        csPopupDivPimInstance.traverseToCsPopupDivNestedSubFrame(waitForReload,
                browserDriver);
        int isListViewSet = browserDriver
                .findElements(By.xpath("//td[contains(text(),'List View')]"))
                .size();
        if (isListViewSet == 0) {
            CSLogger.info("List view is already set");
            browserDriver.navigate().refresh();
        } else {
            moreOptionPopup.selectPopupDivNestedSubMenu(waitForReload,
                    moreOptionPopup.getCtxMoreOptionListView(), browserDriver);
            CSLogger.info("List view set successfully");
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPIMandExpandProductTree(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNodePageInstance.clickOnNodeExports();
    }

    /**
     * Double clicks on products node.
     */
    private void doubleClickOnProductsNode() {
        perfromAction.doubleClick(
                pimStudioProductsNodePageInstance.getBtnPimProductsNode())
                .perform();
        CSLogger.info("Double clicked on products node tree");
    }

    /**
     * Switches to Settings tab.
     */
    private void switchToSettingsPage() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
    }

    /**
     * Performs operation of clicking on system preference icon.
     */
    private void goToSystemPreferencesIcon() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
    }

    /**
     * This method enables PimStudioUsageButton and MamStudioUsageButton
     */
    public void disableUseNewStudioListCheckbox() {
        traverseToSettingsTreeViewFrames();
        settingsPageInstance.clkOnBtnSettingsNode(waitForReload);
        traverseToSettingsRightSectionFrames();
        settingsPageInstance.clkOnBtnPim(waitForReload);
        settingsPageInstance.clkOnBtnPimSubTreeStudio(waitForReload);
        settingsPageInstance.clkOnBtnPimUserInterface(waitForReload);
        settingsPageInstance.expandGivenSection(
                settingsPageInstance.getSecPimListSection(), waitForReload);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[@cs_name='Use New Studio List']")));
        int elasticSearchStatus = browserDriver
                .findElements(By
                        .xpath("//span[contains(@id,'Optionproxypdm:switchToNewList_disabled')]"))
                .size();
        if (elasticSearchStatus == 0) {
            settingsPageInstance.toggleGivenCheckboxElement(
                    settingsPageInstance.getCbPimNewStudioList(), waitForReload,
                    false);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
        } else {
            CSLogger.info(
                    "Use New Studio List is disabled as Elastic Search is "
                            + "deactivated");
        }
    }

    /**
     * Traverses to settings tree view frames.
     */
    private void traverseToSettingsTreeViewFrames() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * This method switches the frame till main frame
     */
    public void traverseToSettingsRightSectionFrames() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        settingsPageInstance = new SettingsPage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        pimStudioProductsNodePageInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        perfromAction = new Actions(browserDriver);
        moreOptionPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPopupDivPimInstance = new CSPopupDivPim(browserDriver);
    }
}
