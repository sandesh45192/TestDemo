/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IClassPopup;
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
 * This class contains the test methods to delete class
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteClassTest extends AbstractTest {

    private CSPortalHeader        csPortalHeader;
    private WebDriverWait         waitForReload;
    private PimStudioSettingsNode pimStudioSettingsNodeInstance;
    private IClassPopup           classPopup;
    public String                 deleteClasssheetName = "DeleteClass";

    /**
     * This test method delete the class folder
     * 
     * @param className String class Folder name
     */
    @Test(dataProvider = "deleteClassTestData")
    public void testDeleteClass(String className) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            chkPresenceOfFolder(className, false);
            performDeletecase(className, false);
            performDeletecase(className, true);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testDeleteClass" + e);
            Assert.fail(
                    "Automation Error in test method : testDeleteClass" + e);
        }
    }

    /**
     * This method perform the delete operation on class folder
     * 
     * @param className String class Folder name
     * @param pressOkay Boolean to press Ok or cancel on alert box
     */
    public void performDeletecase(String className, Boolean pressOkay) {
        goToSettingClassTree();
        WebElement createdclassFolder = browserDriver
                .findElement(By.linkText(className));
        CSUtility.rightClickTreeNode(waitForReload, createdclassFolder,
                browserDriver);
        classPopup.selectPopupDivMenu(waitForReload,
                classPopup.getCsGuiPopupMenuObject(), browserDriver);
        classPopup.selectPopupDivSubMenu(waitForReload,
                classPopup.getDeleteMenu(), browserDriver);
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (pressOkay) {
            alertBox.accept();
            CSLogger.info("Click on OK to alert.");
            chkPresenceOfFolder(className, pressOkay);
        } else {
            alertBox.dismiss();
            CSLogger.info("Click on Cancel to alert.");
            chkPresenceOfFolder(className, pressOkay);
        }
    }

    /**
     * This method check the presence of folder under the tree
     * 
     * @param className String View Folder name
     * @param pressOkay Boolean to press true for absence of element false for
     *            presence of element.
     */
    public void chkPresenceOfFolder(String className, Boolean pressOkay) {
        goToSettingClassTree();
        List<WebElement> classFolder = browserDriver
                .findElements(By.linkText(className));
        if (pressOkay) {
            if (classFolder.isEmpty()) {
                CSLogger.info("Class Folder " + className + " is not Present");
            } else {
                Assert.fail("Class Folder " + className + " is present");
            }
        } else {
            if (!classFolder.isEmpty()) {
                CSLogger.info("Class Folder " + className + " is Present");
            } else {
                Assert.fail("Class Folder " + className + " is not present");
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
                .clkBtnPimSettingsClassesNode(waitForReload);
    }

    /**
     * This method reads data from the excel file and return array to the data
     * provider Sheet contains the Attribute Technical name and Label of product
     * 
     * @return Array contains the attribute Technical Name and the label
     */
    @DataProvider(name = "deleteClassTestData")
    public Object[][] getClassNameTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                deleteClasssheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod()
    public void initializeResource() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csPortalHeader = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        classPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }
}
