/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to delete view version
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteAttributeTest extends AbstractTest {

    private CSPortalHeader        csPortalHeader;
    private WebDriverWait         waitForReload;
    private PimStudioSettingsNode pimStudioSettingsNodeInstance;
    private IAttributePopup       attributePopup;
    public String                 deleteAttributesheetName = "DeleteAttributeFolder";

    /**
     * This test method delete the attribute folder
     * 
     * @param attributeName String attribute Folder name
     */
    @Test(dataProvider = "deleteAttributeTestData")
    public void testDeleteClass(String attributeName) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            chkPresenceOfFolder(attributeName, false);
            performDeletecase(attributeName, false);
            performDeletecase(attributeName, true);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testDeleteClass" + e);
            Assert.fail(
                    "Automation Error in test method : testDeleteClass" + e);
        }
    }

    /**
     * This method perform the delete operation on attribute folder
     * 
     * @param attributeName String attribute Folder name
     * @param pressOkay Boolean to press Ok or cancel on alert box
     */
    public void performDeletecase(String attributeName, Boolean pressOkay) {
        goToSettingClassTree();
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(attributeName));
        CSUtility.rightClickTreeNode(waitForReload, createdAttributeFolder,
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuObject(), browserDriver);
        attributePopup.selectPopupDivSubMenu(waitForReload,
                attributePopup.getDeleteMenu(), browserDriver);
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (pressOkay) {
            alertBox.accept();
            CSLogger.info("Click on OK to alert.");
            chkPresenceOfFolder(attributeName, pressOkay);
        } else {
            alertBox.dismiss();
            CSLogger.info("Click on Cancel to alert.");
            chkPresenceOfFolder(attributeName, pressOkay);
        }
    }

    /**
     * This method check the presence of folder under the tree
     * 
     * @param attributeName String attribute Folder name
     * @param pressOkay Boolean to press true for absence of element false for
     *            presence of element.
     */
    public void chkPresenceOfFolder(String attributeName, Boolean pressOkay) {
        goToSettingClassTree();
        List<WebElement> attributeFolder = browserDriver
                .findElements(By.linkText(attributeName));
        if (pressOkay) {
            if (attributeFolder.isEmpty()) {
                CSLogger.info("Attribute Folder " + attributeName
                        + " is not Present");
            } else {
                Assert.fail(
                        "Attribute Folder " + attributeName + " is present");
            }
        } else {
            if (!attributeFolder.isEmpty()) {
                CSLogger.info(
                        "Attribute Folder " + attributeName + " is Present");
            } else {
                Assert.fail("Attribute Folder " + attributeName
                        + " is not present");
            }
        }
    }

    /**
     * This method click on the channel node
     * 
     */
    public void goToSettingClassTree() {
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
    }

    /**
     * This method reads data from the excel file and return array to the data
     * provider Sheet contains the Attribute Technical name
     * 
     * @return Array contains the attribute Technical Name
     */
    @DataProvider(name = "deleteAttributeTestData")
    public Object[][] getAttributeFolderTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                deleteAttributesheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod()
    public void initializeResource() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }
}
