/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create new class
 * 
 * @author CSAutomation Team
 */
public class CreateClassTest extends AbstractTest {

    private CSGuiToolbarHorizontal csGuiToolbarHorizontal;
    private CSGuiDialogContentId   csGuiDialogContentId;
    private WebDriverWait          waitForReload;
    private PimStudioSettingsNode  pimStudioSettingsNode;
    private IClassPopup            classPopUp;
    private CSPortalHeader         csPortalHeader;
    private String                 CreateClassSheet = "CreateClass";

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        CSLogger.info("Initialized the POM object for CreateClass page.");
        waitForReload = new WebDriverWait(browserDriver, 30);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioSettingsNode = new PimStudioSettingsNode(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
    }

    /**
     * This create the class and takes input from the data provider
     *
     * @param nameOfClass contains the name of the class
     */
    @Test(dataProvider = "createClassData")
    public void testCreateClass(String nameOfClass) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            csPortalHeader.clkBtnProducts(waitForReload);
            pimStudioSettingsNode.clkBtnPimSettingsNode(waitForReload);
            performUsecase(waitForReload, nameOfClass, false);
            performUsecase(waitForReload, nameOfClass, true);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testCreateClass " + e);
            Assert.fail(
                    "Automation Error in test method : testCreateClass " + e);
        }
    }

    /**
     * This method performs the create class and verify the OK and cancel button
     * 
     * @param waitForReload it is object of WebDriverWait contains time which is
     *            used to wait for particular element to reload
     * @param nameOfClass String name of the Classes
     * @param pressOkay Boolean value For OK(true) and Cancel button
     */
    public void performUsecase(WebDriverWait waitForReload, String nameOfClass,
            Boolean pressOkay) {
        IClassPopup classPopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsClassesNode(),
                browserDriver);
        classPopup.selectPopupDivMenu(waitForReload,
                classPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        classPopup.enterValueInDialogue(waitForReload, nameOfClass);
        classPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        if (pressOkay) {
            activateShowInCategory(nameOfClass);
        }
        verifyCreationOfClasses(waitForReload, nameOfClass, pressOkay);
    }

    /**
     * This method checks whether the folder actually gets created or not.
     * 
     * @param waitForReload it is object of WebDriverWait contains time which is
     *            used to wait for particular element to reload
     * @param userInputFolderName String name of the folder
     * @param pressOkay Boolean value For OK and Cancel button
     */
    private void verifyCreationOfClasses(WebDriverWait waitForReload,
            String nameOfClass, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        List<WebElement> createdClass = browserDriver
                .findElements(By.linkText(nameOfClass));
        Assert.assertEquals(createdClass.isEmpty(), !pressOkay);
        CSUtility.switchToDefaultFrame(browserDriver);
    }

    /**
     * This method clicks on save button in horizontal toolbar
     * 
     * @param nameOfClass contains the name of the class
     */
    private void activateShowInCategory(String nameOfClass) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsClassesNode());
        pimStudioSettingsNode.getBtnPimSettingsClassesNode().click();
        WebElement className = browserDriver
                .findElement(By.linkText(nameOfClass));
        CSUtility.waitForVisibilityOfElement(waitForReload, className);
        className.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getBtnShowInCategoryTree());
        csGuiDialogContentId.clkBtnShowInCategoryTree();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiToolbarHorizontal.getBtnCSGuiToolbarSave());
        csGuiToolbarHorizontal.getBtnCSGuiToolbarSave().click();
    }

    /**
     * This data provider returns name of class
     * 
     * @return array of class names
     * 
     */
    @DataProvider(name = "createClassData")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                CreateClassSheet);
    }
}
