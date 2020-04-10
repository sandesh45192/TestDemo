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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create PIM Attribute Folder.
 * 
 * @author CSAutomation Team
 *
 */
public class CreatePimAttributeFolderTest extends AbstractTest {

    private CSPortalHeader        csPortalHeaderInstance;
    private WebDriverWait         waitForReload;
    private PimStudioSettingsNode pimStudioSettingsNodeInstance;
    public String                 attributesheetName = "AttributeFolder";

    /**
     * This the the test method which drives the usecase.
     */
    @Test(dataProvider = "newPimAttributeFolderTestData")
    public void testCreateAttributeFolder(String folderTeachnicalName,
            String Lable) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
            performUseCase(waitForReload, folderTeachnicalName, false);
            performUseCase(waitForReload, folderTeachnicalName, true);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method: testCreateAttributeFolder"
                            + e);
            Assert.fail(
                    "Automation Error in test method: testCreateAttributeFolder"
                            + e);
        }
    }

    /**
     * This method performs the testcase i.e It creates a new attribute folder
     * and also checks whether the folder actually gets created or not.
     * 
     * @param waitForReload contains time which is used to wait for particular
     *            element to reload
     * @param userInputFolderName contains value of folder name
     * @param pressOkay contains boolean values true or false
     */
    private void performUseCase(WebDriverWait waitForReload,
            String userInputFolderName, Boolean pressOkay) {
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode(),
                browserDriver);
        CSLogger.info("Right clicked on Attribute Node");
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuNewFolder(), browserDriver);
        CSLogger.info("Selected New Folder Option");
        attributePopup.enterValueInDialogue(waitForReload, userInputFolderName);
        attributePopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        verifyCreationOfAttributeFolder(waitForReload, userInputFolderName,
                pressOkay);
    }

    /**
     * This method checks whether the folder actually gets created or not.
     * 
     * @param waitForReload it is object of WebDriverWait contains time which is
     *            used to wait for particular element to reload
     * @param userInputFolderName String name of the folder
     * @param pressOkay Boolean value For OK and Cancel button
     */
    private void verifyCreationOfAttributeFolder(WebDriverWait waitForReload,
            String userInputFolderName, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        List<WebElement> attributeFolder = browserDriver
                .findElements(By.linkText(userInputFolderName));
        Assert.assertEquals(attributeFolder.isEmpty(), !pressOkay);
        CSUtility.switchToDefaultFrame(browserDriver);
    }

    /**
     * This method reads data from the excel file and return array to the data
     * provider Sheet contains the Attribute Technical name and Label of product
     * 
     * @return Array contains the attribute Technical Name and the label
     */
    @DataProvider(name = "newPimAttributeFolderTestData")
    public Object[][] getAttributeFolderTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                attributesheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
    }
}
