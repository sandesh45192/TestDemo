/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IChannelPopup;
import org.cs.csautomation.cs.pom.PimStudioChannelsNode;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to delete view version
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteViewTest extends AbstractTest {

    private PimStudioChannelsNode pimStudioChannelsNodeInstance;
    private CSPortalHeader        csPortalHeader;
    private IChannelPopup         channelPopup;
    private WebDriverWait         waitForReload;
    private String                deleteViewSheetName = "DeleteView";

    /**
     * This test method delete the view folder
     * 
     * @param viewName String View Folder name
     */
    @Test(dataProvider = "deleteViewTestData")
    public void testDeleteView(String viewName) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            chkPresenceOfFolder(viewName, false);
            performDeletecase(viewName, false);
            performDeletecase(viewName, true);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method :testDeleteView " + e);
            Assert.fail("Automation Error in test method :testDeleteView " + e);
        }
    }

    /**
     * This method perform the delete operation on view folder
     * 
     * @param viewName String View Folder name
     * @param pressOkay Boolean to press Ok or cancel on alert box
     */
    public void performDeletecase(String viewName, Boolean pressOkay) {
        goToChannelTree();
        WebElement createdViewFolder = browserDriver
                .findElement(By.linkText(viewName));
        CSUtility.rightClickTreeNode(waitForReload, createdViewFolder,
                browserDriver);
        channelPopup.selectPopupDivMenu(waitForReload,
                channelPopup.getCsGuiPopupMenuObject(), browserDriver);
        channelPopup.selectPopupDivSubMenu(waitForReload,
                channelPopup.getDeleteMenu(), browserDriver);
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (pressOkay) {
            alertBox.accept();
            CSLogger.info("Click on OK to alert.");
            chkPresenceOfFolder(viewName, pressOkay);
        } else {
            alertBox.dismiss();
            CSLogger.info("Click on Cancel to alert.");
            chkPresenceOfFolder(viewName, pressOkay);
        }
    }

    /**
     * This method check the presence of folder under the tree
     * 
     * @param viewName String View Folder name
     * @param pressOkay Boolean to press true for absence of element false for
     *            presence of element.
     */
    public void chkPresenceOfFolder(String viewName, Boolean pressOkay) {
        goToChannelTree();
        List<WebElement> viewFolder = browserDriver
                .findElements(By.linkText(viewName));
        if (pressOkay) {
            if (viewFolder.isEmpty()) {
                CSLogger.info("view Folder " + viewName + " is not Present");
            } else {
                Assert.fail("View Folder " + viewName + " is present");
            }
        } else {
            if (!viewFolder.isEmpty()) {
                CSLogger.info("view Folder " + viewName + " is Present");
            } else {
                Assert.fail("View Folder " + viewName + " is not present");
            }
        }
    }

    /**
     * This method click on the channel node
     * 
     */
    public void goToChannelTree() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioChannelsNodeInstance.clkBtnPimChannelsNode(waitForReload);
    }

    /**
     * This is a data provider method contains array of view folder name
     * 
     * @return Array
     */
    @DataProvider(name = "deleteViewTestData")
    public Object[][] getdeleteViewTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                deleteViewSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        channelPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioChannelsNodeInstance = new PimStudioChannelsNode(
                browserDriver);
    }
}
