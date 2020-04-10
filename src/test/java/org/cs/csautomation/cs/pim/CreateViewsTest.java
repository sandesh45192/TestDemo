/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IChannelPopup;
import org.cs.csautomation.cs.pom.PimStudioChannelsNode;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create views or channels
 * 
 * @author CSAutomation Team
 *
 */
public class CreateViewsTest extends AbstractTest {

    private CSPortalHeader        csPortalHeaderInstance;
    private WebDriverWait         waitForReload;
    private PimStudioChannelsNode pimStudioChannelsNodeInstance;
    private IChannelPopup         channelPopup;
    private TabbedPaneNorth       tabbedPaneNorthInstance;
    private String                createViewSheetName = "CreateView";

    /**
     * This test method creates new view or channel with Ok and Cancel first it
     * Clicks on Cancel button and then OK and creates new views
     * 
     * @param viewName string parameter contains name of the view
     */
    @Test(dataProvider = "createViewTestData")
    public void testCreateViews(String viewName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 120);
            performUsecase(waitForReload, viewName, false);
            performUsecase(waitForReload, viewName, true);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testCreateViews " + e);
            Assert.fail(
                    "Automation Error in test method : testCreateViews " + e);
        }
    }

    /**
     * Performs operation of creation of view or channel
     * 
     * @param waitForReload this parameter is WebDriverWait object
     * @param userInputViewName view name is passed
     * @param pressOkay this is boolean parameter which accepts true or false
     *            values
     */
    public void performUsecase(WebDriverWait waitForReload,
            String userInputViewName, Boolean pressOkay) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioChannelsNodeInstance.getBtnPimChannelsNode(),
                browserDriver);
        channelPopup.selectPopupDivMenu(waitForReload,
                channelPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        channelPopup.enterValueInDialogue(waitForReload, userInputViewName);
        channelPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        verifyCreationOfViews(waitForReload, userInputViewName, pressOkay);
    }

    /**
     * This method contains assert statements to validate creation of view
     * 
     * @param waitForReload this parameter is WebDriverWait object
     * @param userInputViewName view name is passed
     * @param pressOkay this is boolean parameter which accepts true or false
     *            values
     */
    public void verifyCreationOfViews(WebDriverWait waitForReload,
            String userInputViewName, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        List<WebElement> views = browserDriver
                .findElements(By.linkText(userInputViewName));
        Assert.assertEquals(views.isEmpty(), !pressOkay);
        CSUtility.switchToDefaultFrame(browserDriver);
    }

    /**
     * This is a data provider method contains array of ViewName
     * 
     * @return Array String array consisting array of ViewName
     */
    @DataProvider(name = "createViewTestData")
    public Object[][] getCreateViewData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                createViewSheetName);
    }

    @BeforeMethod()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioChannelsNodeInstance = new PimStudioChannelsNode(
                browserDriver);
        channelPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
    }
}
