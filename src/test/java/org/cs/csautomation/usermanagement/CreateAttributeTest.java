/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class creates attribute under Attribute folder
 * 
 * @author CSAutomationTeam
 *
 */
public class CreateAttributeTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private UserManagementPage          userManagementPage;
    private String                      createAttributeSheet = "CreateAttributeSheet";
    private IUserManagementPopup        userManagementPopup;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSPortalWidget              csPortalWidgetInstance;
    private FrameLocators               iframeLocatorInstance;

    /**
     * This test method creates attribute in Attribute folder
     * 
     * @param attributeFolderName contains attribute folder name
     * @param attributeName contains attribute name
     */
    @Test(priority = 1, dataProvider = "attributeCreation")
    public void testCreateAttribute(String attributeFolderName,
            String attributeName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                    waitForReload, browserDriver, iframeLocatorInstance);
            csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
            createNewAttribute(attributeFolderName, attributeName, false);
            createNewAttribute(attributeFolderName, attributeName, true);
            verifyCreatedAttribute(attributeFolderName, attributeName);
            verifyFieldsInAttributeEditor(attributeFolderName, attributeName);
        } catch (Exception e) {
            CSLogger.info("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method creates new attribute under created Attribute folder
     * 
     * @param attributeFolderName contains name of attribute folder
     * @param attributeName contains the name of the attribute to be created
     * @param status contains status boolean variable
     */
    private void createNewAttribute(String attributeFolderName,
            String attributeName, boolean status) {
        traversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        clkAttributes();
        WebElement attributeFolder = rightClkAttributeFolder(
                attributeFolderName);
        CSUtility.tempMethodForThreadSleep(1000);
        clkCreateNewOption(attributeFolder, attributeName);
        if (status == false) {
            clkCancel();
        } else if (status == true) {
            clkOk();
        }
    }

    /**
     * This method clicks on Ok of pop up
     */
    private void clkOk() {
        userManagementPopup.clkOkOfPopup(waitForReload);
    }

    /**
     * This method clicks on Cancel of pop up
     */
    private void clkCancel() {
        userManagementPopup.clkCancelOfPopup(waitForReload);
    }

    /**
     * This method clicks on Attributes node
     */
    private void clkAttributes() {
        waitForReload.until(ExpectedConditions.visibilityOf(
                userManagementPage.getUserManagementAttributes()));
        userManagementPage.getUserManagementAttributes().click();
        CSLogger.info("Clicked on Attributes node ");
    }

    /**
     * This method right clicks on the created attribute folder
     * 
     * @param attributeFolderName contains the name of the attribute folder
     * @return attributeFolder
     */
    private WebElement rightClkAttributeFolder(String attributeFolderName) {
        WebElement attributeFolder = browserDriver
                .findElement(By.linkText(attributeFolderName));
        CSUtility.rightClickTreeNode(waitForReload, attributeFolder,
                browserDriver);
        CSLogger.info("Right clicked on Attribute folder");
        return attributeFolder;
    }

    /**
     * This method clicks on create new option
     * 
     * @param attributeFolder contains attribute folder name
     * @param attributeName contains attribute name
     */
    private void clkCreateNewOption(WebElement attributeFolder,
            String attributeName) {
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupCreateNew(), browserDriver);
        userManagementPopup.enterValueInUserInputDialogue(waitForReload,
                attributeName);
    }

    /**
     * This method verifies if attribute has created under attribute folder
     * 
     * @param attributeFolderName contains the name of the attribute folder
     * @param attributeName contains the attribute name
     */
    private void verifyCreatedAttribute(String attributeFolderName,
            String attributeName) {
        try {
            traversingForSettingsModule.traverseToUserManagement(waitForReload,
                    browserDriver);
            userManagementPage.clkUserManagement(waitForReload);
            clkAttributes();
            WebElement attributeFolder = browserDriver
                    .findElement(By.linkText(attributeFolderName));
            attributeFolder.click();
            WebElement attribute = browserDriver
                    .findElement(By.linkText(attributeName));
            Assert.assertEquals(attributeName, attribute.getText());
            CSLogger.info("Attribute created successfully.");
        } catch (Exception e) {
            CSLogger.info("Verification of Created attribute failed");
        }
    }

    /**
     * This method verifies attribute values in editor after clicking attribute
     * 
     * @param attributeFolderName
     * @param attributeName
     */
    private void verifyFieldsInAttributeEditor(String attributeFolderName,
            String attributeName) {
        try {
            clkCreatedAttribute(attributeFolderName, attributeName);
            traversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            final String attributeType = getValueOfElement(waitForReload,
                    userManagementPage.getBtnCsTypeLabel());
            final String technicalAttributeName = getValueOfElement(
                    waitForReload,
                    userManagementPage.getTxtUserconfigurationName());
            final String attributeLabel = getValueOfElement(waitForReload,
                    userManagementPage.getTxtUserconfigurationLabel());
            final String sortOrder = getValueOfElement(waitForReload,
                    userManagementPage.getTxtUserconfigurationSortOrder());
            final String requiredAttribute = getValueOfSelectedDropDownOption(
                    userManagementPage
                            .getDrpdwnUserconfigurationRequiredAttribute());
            final String inheritance = getValueOfSelectedDropDownOption(
                    userManagementPage.getDrpdwnUserconfigurationInherited());
            final String search = getValueOfSelectedDropDownOption(
                    userManagementPage.getDrpdwnUserconfigurationSearchable());
            if (attributeType.equals("Single-line Text")
                    && technicalAttributeName.equals(attributeName)
                    && attributeLabel.equals("") && sortOrder.equals("0")
                    && requiredAttribute.equals("Never")
                    && inheritance.equals("No Inheritance")
                    && search.equals("Not searchable")) {
                CSLogger.info(
                        "Created New Attribute with proper default configuration "
                                + "values");
            } else {
                Assert.fail("The Default Configuration fileds of newly created"
                        + " attribute are improper");
                CSLogger.error(" Newly Created Attribute has  improper default"
                        + " configuration values ");
            }
        } catch (Exception e) {
            CSLogger.info(
                    "Newly Created Attribute has  improper default configuration values");
        }
    }

    /**
     * This method gets value of element
     * 
     * @param waitForReload waits for an element to reload
     * @param element contains WebElement
     * @return value of attribute which obtained from getAttribute method
     */
    public static String getValueOfElement(WebDriverWait waitForReload,
            WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        return getAttributeValue(element, "value");
    }

    /**
     * This method gets attribute value
     * 
     * @param element contains WebElement
     * @param attribute contains attribute
     * @return attribute
     */
    public static String getAttributeValue(WebElement element,
            String attribute) {
        return element.getAttribute(attribute);
    }

    /**
     * This method gets value of element from drop down option
     * 
     * @param element contains webelement
     * @return txt of webelement
     */
    public static String getValueOfSelectedDropDownOption(WebElement element) {
        Select select = new Select(element);
        WebElement option = select.getFirstSelectedOption();
        return option.getText();
    }

    /**
     * This method clicks on already created attribute
     * 
     * @param attributeFolderName contains name of attribute folder
     * @param attributeName contains attribute name
     */
    private void clkCreatedAttribute(String attributeFolderName,
            String attributeName) {
        traversingForSettingsModule.traverseToUserManagement(waitForReload,
                browserDriver);
        userManagementPage.clkUserManagement(waitForReload);
        WebElement attributeFolder = browserDriver
                .findElement(By.linkText(attributeFolderName));
        attributeFolder.click();
        WebElement nameOfAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        nameOfAttribute.click();
        CSLogger.info("Clicked on created attribute");
    }

    /**
     * This data provider returns sheet data which contains attribute folder
     * name and attribute name
     * 
     * @return createAttributeSheet
     */
    @DataProvider(name = "attributeCreation")
    public Object[] CreateAttribute() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                createAttributeSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorInstance = FrameLocators.getIframeLocators(browserDriver);
    }
}
