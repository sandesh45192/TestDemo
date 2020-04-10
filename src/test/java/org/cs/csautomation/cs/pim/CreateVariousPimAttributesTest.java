/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.CSPortalWindow;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to create various PIM Attributes
 * 
 * @author CSAutomation Team
 *
 */
public class CreateVariousPimAttributesTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSGuiDialogContentId   csGuiDialogContentIdInstance;
    private CSPortalWindow         csPortalWindowInstance;
    private IAttributePopup        attributePopup;
    private String                 attributeSheetName = "CreateAttributeWithCombination";
    private SoftAssert             softAssertion;
    private CSPortalHeader         csPortalHeaderInstance;
    private PimStudioSettingsNode  pimStudioSettingsNodeInstance;

    /**
     * This is a test method which verifies creation of various attributes while
     * validating the different attribute types like ReferenceToPim,
     * ReferenceToMAM and Reference to CORE user.
     * 
     * @param folderName name of attribute folder
     * @param attributeTechnicalName technical attribute name
     * @param attributeLabel type of attribute for eg.
     * @param fieldName field name for eg.Inheritance
     * @param attributeValue value of attribute for eg.Reference to CORE User
     * @param fieldType fieldtype for eg. attrSelection,dropdown,checkbox
     */
    @Test(priority = 1, dataProvider = "variousPimAttributeTestData")
    public void testVerifyMultipleAttributeTypeSelection(String folderName,
            String attributeTechnicalName, String attributeLabel,
            String fieldName, String attributeValue, String fieldType,
            String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandSettingsTree(waitForReload);
            clkOnAttributesNode();
            CSLogger.info(
                    "Switched to a  New Window after clicking create New");
            switch (fieldType) {
            case "attrSelection":
                String windowHandle = performClkOnCreateNewOption(folderName);
                switchToCurrentWindowHandle();
                verifyMultipleAttributeTypeSelection(attributeValue,
                        attributeTechnicalName, folderName, windowHandle,
                        productTabName);
                browserDriver.close();
                browserDriver.switchTo().window(windowHandle);
                break;
            case "dropdown":
                testperformDropDownAction(folderName, attributeTechnicalName,
                        attributeValue, fieldName);
                break;
            case "checkbox":
                testPerformCheckLanguageDependence(folderName,
                        attributeTechnicalName);
                break;
            default:
                break;
            }
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testVerifyMultipleAttributeTypeSelection"
                            + e);
            Assert.fail(
                    "Automation Error in test method : testVerifyMultipleAttributeTypeSelection"
                            + e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandSettingsTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * Clicks on attributes node in pim studio
     */
    public void clkOnAttributesNode() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode());
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
    }

    /**
     * Checks attribute creation with options like Inheritance,Simple Search &
     * Required fields of an attribute.
     * 
     * @param folderName name of attribute folder
     * @param attributeLabel contains name for attribute label
     * @param attributeValue value of attribute for eg.Reference to CORE User
     * @param fieldName field name for eg.Inheritance
     */
    public void testperformDropDownAction(String folderName,
            String attributeLabel, String attributeValue, String fieldName) {
        try {
            performDropDownActions(
                    csGuiDialogContentIdInstance
                            .getdropDownAttributeType(fieldName),
                    csGuiDialogContentIdInstance
                            .getTxtPdmarticleconfigurationName(),
                    attributeLabel, attributeValue, folderName);
            testverifySelectionOfDrpDwnActions(folderName, attributeLabel,
                    attributeValue, fieldName);

        } catch (Exception e) {
            CSLogger.fatal("Automation Error : " + e);
        }
    }

    /**
     * This test methods depends on 'testperformDropDownAction' test method and
     * verifies whether the selection of options from drop down of Inheritance,
     * Search & Required fields are valid or not.
     *
     * @param folderName name of attribute folder
     * @param AttributeLabel contains name for attribute label
     * @param AttributeValue value of attribute for eg.Reference to CORE User
     * @param fieldName field name for eg.Inheritance
     */
    public void testverifySelectionOfDrpDwnActions(String folderName,
            String AttributeLabel, String AttributeValue, String fieldName) {
        try {
            verifySelectionOfDrpDwn(
                    csGuiDialogContentIdInstance
                            .getdropDownAttributeType(fieldName),
                    AttributeLabel, AttributeValue, folderName);
        } catch (Exception e) {
            CSLogger.fatal("Automation Error : " + e);
            e.printStackTrace();
        }
    }

    /**
     *
     * This test method checks language dependence field of an attribute
     *
     * @param folderName name of attribute folder
     * @param attributeLabel contains name for attribute label
     */
    public void testPerformCheckLanguageDependence(String folderName,
            String attributeLabel) {
        try {
            performCheckLanguageDependence(folderName, attributeLabel);
        } catch (Exception e) {
            CSLogger.fatal("Automation Error : " + e);
        }
    }

    /**
     * This method clicks on Create new Option from midpane after clicking on
     * newly created attribute folder
     * 
     * @param
     * @return Current Window handle is returned.
     * @param folderName name of attribute folder
     * @return windowHandle
     */
    public String performClkOnCreateNewOption(String folderName) {
        String windowHandle = clkOnNewlyCreatedAttributeFolder(folderName,
                waitForReload);
        traverseToRightSection();
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCreateNew(waitForReload);
        return windowHandle;
    }

    /**
     * Clicks on attribute Type field and also enters a technical name for an
     * attribute.
     * 
     * @param waitForReload it is WebDriverWait object
     * @param technicalName contains string value for technical attribute name
     */
    public void performClkAttributeTypeFiled(WebDriverWait waitForReload,
            String technicalName) {
        csGuiDialogContentIdInstance
                .enterDataForTextAttributeFiled(waitForReload,
                        csGuiDialogContentIdInstance
                                .getTxtPdmarticleconfigurationName(),
                        technicalName);
        CSLogger.info(
                "Entered technical attribute name for that attribute type");
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        // csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        JavascriptExecutor js = (JavascriptExecutor) browserDriver;
        js.executeScript("window.scrollBy(0,-350)", "");
        CSUtility.tempMethodForThreadSleep(4000);
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationName()));
        csGuiDialogContentIdInstance.clkBtnCSTypeLabel(waitForReload);
    }

    /**
     * Verifies selection of multiple attribute.
     * 
     * @param attributeValue contains string value of attribute
     * @param attributeType type of attribute for eg dropdown or cross reference
     *            type
     * @param attributeTypeText string value contains attribute type text
     * @param windowHandle it is string value specifying window handle
     */
    public void verifyMultipleAttributeTypeSelection(String attributeValue,
            String attibuteType, String folderName, String windowHandle,
            String productTabName) {
        performClkAttributeTypeFiled(waitForReload, attibuteType);
        clkOnCancelUsecase();
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationName()));
        csGuiDialogContentIdInstance.clkBtnCSTypeLabel(waitForReload);
        selectAttributeType(
                csPortalWindowInstance.getAttributeType(attributeValue),
                attributeValue);
        validateAlertBoxPresence(windowHandle);
        verifyingAttributeTypeSelection(attributeValue);
        testMultipleSelection(productTabName);

    }

    /**
     * Clicks on cancel button
     */
    public void clkOnCancelUsecase() {
        switchToCurrentWindowHandle();
        attributePopup.askBoxWindowOperation(waitForReload, false,
                browserDriver);
        CSLogger.info("Exceution of Cancel Button verification passed ");
    }

    /**
     * clicks on attribute type Cross Reference.
     * 
     * @param attributeType type of attribute for eg dropdown or cross reference
     *            type
     * @param technicalName contains string value for technical attribute name
     */
    public void selectAttributeType(WebElement attributeType,
            String technicalName) {
        csPortalWindowInstance
                .clkDdCSPortalGuiListCrossReference(waitForReload);
        csPortalWindowInstance.selectCrossReferenceAttributeType(waitForReload,
                attributeType, technicalName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(5000);
    }

    /**
     * Clicks on newly created attribute folder.
     * 
     * @param waitForReload it is WebDriverWait object
     * @return current window handle
     */
    public String clkOnNewlyCreatedAttributeFolder(String folderName,
            WebDriverWait waitForReload) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(folderName));
        CSUtility.scrollUpOrDownToElement(createdAttributeFolder,
                browserDriver);
        waitForCreatedAttributeFolder(createdAttributeFolder);
        CSUtility.tempMethodForThreadSleep(1000);
        createdAttributeFolder.click();
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("clicked on Newly Created Attribute folder");
        String windowHandleCreateFolder = browserDriver.getWindowHandle();
        return windowHandleCreateFolder;
    }

    /**
     * wait for visibility of created attribute folder
     * 
     * @param createdAttributeFolder
     */
    public void
            waitForCreatedAttributeFolder(WebElement createdAttributeFolder) {
        waitForReload
                .until(ExpectedConditions.visibilityOf(createdAttributeFolder));
    }

    /**
     * switch to current window handle.
     */
    public void switchToCurrentWindowHandle() {
        for (String winHandle : browserDriver.getWindowHandles()) {
            browserDriver.switchTo().window(winHandle);
        }
        CSLogger.info("Switched to new window handle");
    }

    /**
     * After selecting a particular attribute type a pop up should appear,this
     * method validates the presence of that alert box(pop up)
     * 
     * @param windowHandle string type holding value of window handle
     */
    public void validateAlertBoxPresence(String windowHandle) {
        Alert alertBox = null;
        try {
            alertBox = getAlertBox();
            if (alertBox != null) {
                dealWithAlertBox(false, alertBox);
                dealWithAlertBox(true, alertBox);
            }
        } catch (NoAlertPresentException e) {
            browserDriver.switchTo().window(windowHandle);
            CSLogger.error("Assert fail due to dialogue popup doesn't appear");
            softAssertion
                    .fail("A dialog saying: 'Are you sure you want change the "
                            + "type of this attribute?' Data already saved in this"
                            + " attribute may become invalid with 'OK' and 'Cancel' "
                            + "button does not popup");
        }
    }

    /**
     * performs operations on alert box like clicking on ok or cancel button of
     * pop up
     * 
     * @param pressOkay contains value true or false
     * @param alertBox instance of Alert class
     */
    public void dealWithAlertBox(Boolean pressOkay, Alert alertBox) {
        if (!pressOkay) {
            alertBox.dismiss();
            CSLogger.info("clicked on cancel of attribute dialogue");
            attributePopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
        } else {
            alertBox.accept();
            CSLogger.info("clicked on OK of attribute dialogue");
        }
    }

    /**
     * Contains assert statement to verify whether the attribute type is
     * properly selected or not
     * 
     * @param attributeType it is string value specifying type of attribute
     */
    public void verifyingAttributeTypeSelection(String attributeType) {
        switchToCurrentWindowHandle();
        Assert.assertEquals(csGuiDialogContentIdInstance.getBtnCSTypeLabel()
                .getAttribute("value"), attributeType);
    }

    /**
     * This methods checks a multiple selection field of an attribute.
     */
    public void testMultipleSelection(String productTabName) {
        csGuiDialogContentIdInstance.checkCbMultipleSelection(waitForReload);
        csGuiDialogContentIdInstance.enterDataForTextAttributeFiled(
                waitForReload,
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationPaneTitle(),
                productTabName);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method returns the alert box.
     * 
     * @return Alert Instance of Alert class representing the DOM alert box.
     */
    private Alert getAlertBox() {
        Alert alertBox;
        CSUtility.tempMethodForThreadSleep(1000);
        alertBox = browserDriver.switchTo().alert();
        return alertBox;
    }

    /**
     * Used in test method 'testperformDropDownAction'
     * 
     * @param drpDwnElement type of webElemnt
     * @param technicalAttributeNameElement type of technical attribute type
     * @param technicalAttributeNameValue string that contains value of
     *            technical attribute name
     * @param valueOfDrpDwn string value that contains value of drop down
     */
    public void performDropDownActions(WebElement drpDwnElement,
            WebElement technicalAttributeNameElement,
            String technicalAttributeNameValue, String valueOfDrpDwn,
            String folderName) {
        String getWindowHandle = clkOnNewlyCreatedAttributeFolder(folderName,
                waitForReload);
        traverseToRightSection();
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCreateNew(waitForReload);
        switchToCurrentWindowHandle();
        csGuiDialogContentIdInstance.enterDataForTextAttributeFiled(
                waitForReload, technicalAttributeNameElement,
                technicalAttributeNameValue);
        csGuiDialogContentIdInstance.selectValueFromDrpDwnElement(drpDwnElement,
                valueOfDrpDwn);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        browserDriver.close();
        browserDriver.switchTo().window(getWindowHandle);
        CSLogger.info("after window handle switch");
    }

    /**
     * Used in test method 'testverifySelectionOfDrpDwnActions'
     * 
     * @param drpDwnElement type of drop down webElement
     * @param technicalAttributeNameValue string that contains value of
     *            technical attribute name
     * @param valueOfDrpDwn string value that contains value of drop down
     */
    public void verifySelectionOfDrpDwn(WebElement drpDwnElement,
            String technicalAttributeNameValue, String valueOfDrpDwn,
            String folderName) {
        clkOnNewlyCreatedAttributeFolder(folderName, waitForReload);
        WebElement createdAttributeName = browserDriver
                .findElement(By.linkText(technicalAttributeNameValue));
        createdAttributeName.click();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        Select select = new Select(drpDwnElement);
        WebElement option = select.getFirstSelectedOption();
        Assert.assertEquals(option.getText(), valueOfDrpDwn);
        CSLogger.info("Drop down actions verified");
    }

    /**
     * Used in test method 'testPerformCheckLanguageDependence'
     */
    public void performCheckLanguageDependence(String folderName,
            String attributeTechnicalName) {
        String getWindow = clkOnNewlyCreatedAttributeFolder(folderName,
                waitForReload);
        traverseToRightSection();
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCreateNew(waitForReload);
        switchToCurrentWindowHandle();
        csGuiDialogContentIdInstance.enterDataForTextAttributeFiled(
                waitForReload,
                csGuiDialogContentIdInstance
                        .getTxtPdmarticleconfigurationName(),
                attributeTechnicalName);
        csGuiDialogContentIdInstance
                .checkCbPdmarticleconfigurationLanguageDependent(waitForReload);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        browserDriver.close();
        CSLogger.info("last line of perform language dependence");
        browserDriver.switchTo().window(getWindow);
        CSLogger.info("after window handle switch in language dependence");
    }

    /**
     * Traverses till right section of PIM product.
     */
    private void traverseToRightSection() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * It returns the array of data for different types of attribute creation It
     * gives the filed like Folder Name, attributeTechnicalName, AttributeLabel,
     * FieldName, AttributeValue, FieldType
     * 
     * @return data for the test
     */
    @DataProvider(name = "variousPimAttributeTestData")
    public Object[][] getVariousAttributeTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                attributeSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResource() {
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csPortalWindowInstance = new CSPortalWindow(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        softAssertion = new SoftAssert();
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
    }
}
