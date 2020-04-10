/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.CSPortalWindow;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.FlexTablePage;
import org.cs.csautomation.cs.settings.IFlexTablePopup;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
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
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods which verifies Product Vs References
 * template test uses.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateProductVsReferencesTemplateTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private IAttributePopup           attributePopup;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSPortalWindow            csPortalWindowInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private IClassPopup               classPopUp;
    private IProductPopup             productPopUp;
    private CSPortalHeader            csPortalHeader;
    private FrameLocators             locator;
    private FlexTablePage             flexTablePage;
    private IFlexTablePopup           flexTablePopup;
    private SoftAssert                softasssert;
    private String                    createProductVsReferencesTestData = "ProductVsReferences";

    /**
     * This method verifies Attribute Vs Product template test user.
     * 
     * @param attributeFolder String object attribute folder name
     * @param referenceAttribute String object attribute type
     * @param referanceClass String object contains class name
     * @param flexClass String object contains class name
     * @param productName String object contains product name
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param attrToAdd String object contains standard attribute name
     * @param flexLanguage String object contains language for flex table
     * @param flexColumn String object contains column name of
     * @param flexRows String object contains rows name
     */
    @Test(dataProvider = "createProductVsReferencesDataSheet")
    public void testCreateProductVsReferencesTemplate(String attributeFolder,
            String referenceAttribute, String referanceClass, String flexClass,
            String productName, String templateName, String tempType,
            String attrToAdd, String flexLanguage, String flexColumn,
            String flexRows) {
        try {
            createDataForTestCase(attributeFolder, referenceAttribute,
                    referanceClass, flexClass, productName);
            createNewTemplate(templateName, tempType);
            verifyCreationOfTemplate(templateName, tempType);
            addAttributeAndProduct(attributeFolder, productName, templateName,
                    tempType, attrToAdd, flexLanguage, flexColumn, flexRows);
            CSUtility.tempMethodForThreadSleep(1000);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmRight()));
            verifyAddedAttributesAndProduct(referenceAttribute, attrToAdd,
                    productName);
            addCreatedFlexTable(productName, templateName);
            testResetTableButton();
            verifyFlexTableAdded(referenceAttribute, attrToAdd, productName,
                    false);
            addCreatedFlexTable(productName, templateName);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getBtnOk());
            flexTablePage.getBtnOk().click();
            checkInProduct();
            verifyFlexTableAdded(referenceAttribute, attrToAdd, productName,
                    true);
            softasssert.assertAll();
            CSLogger.info(
                    "Create product vs references template test completed");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testCreateProductVsReferencesTemplate",
                    e);
            Assert.fail(
                    "Automation Error : testCreateProductVsReferencesTemplate",
                    e);
        }
    }

    /**
     * This method performs checkin operation on product folder
     */
    public void checkInProduct() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr")));
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarCheckIn(waitForReload);
    }

    /**
     * This method verifies Product Vs reference template test uses.
     * 
     * @param referenceAttribute String object attribute type
     * @param productName String object contains product name
     * @param chkStatus boolean object
     */
    private void verifyFlexTableAdded(String referenceAttribute,
            String attrToAdd, String productName, Boolean chkStatus) {
        String numberOfProduct[] = productName.split(",");
        int columnsInTable = 0;
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        flexTablePage.clkOnDataTab(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getRightSectionEditor());
        if (chkStatus) {
            columnsInTable = browserDriver.findElements(By.xpath(
                    "//table[@class='CSFlexTable FlexTable']//thead//tr//th"))
                    .size();
            if (columnsInTable == numberOfProduct.length) {
                CSLogger.info("Table Addded Successfully");
            } else {
                CSLogger.error("Fail to add table");
                Assert.fail("Fail to add table");
            }
            verifyAddedAttributesAndProduct(referenceAttribute, attrToAdd,
                    productName);
        } else {
            columnsInTable = browserDriver.findElements(By.xpath(
                    "//table[@class='Flex Standard FlexTable']//thead//tr//th"))
                    .size();
            if (columnsInTable != numberOfProduct.length) {
                CSLogger.info("Reset Table Case Successful");
            } else {
                CSLogger.error("Reset Table Case fail");
                Assert.fail("Reset Table Case fail");
            }
        }
    }

    /**
     * This method perform test on reset table button
     * 
     */
    private void testResetTableButton() {
        flexTablePage.getBtnResetTable().click();
        verifyTextInAlertBox();
        getAlertBox(false);
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        flexTablePage.getBtnResetTable().click();
        getAlertBox(true);
    }

    /**
     * This Method verifies the text in alert box
     */
    private void verifyTextInAlertBox() {
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        String alertText = alertBox.getText();
        String expectedText = "Do you really want to reset the table? "
                + "All settings will get lost!";
        if (alertText.equals(expectedText)) {
            CSLogger.info("Alert box message verified");
        } else {
            CSLogger.error("Alert box message not verified" + alertText);
            softasssert.fail("Alert box message not verified" + alertText);
        }
    }

    /**
     * This method add the created flex table to product.
     * 
     * @param productName String object contains product name
     * @param templateName String object contains template name
     */
    private void addCreatedFlexTable(String productName, String templateName) {
        String[] productArray = productName.split(",");
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement flexTableProduct = browserDriver.findElement(
                By.linkText(productArray[productArray.length - 1]));
        flexTableProduct.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        flexTablePage.clkOnDataTab(waitForReload);
        WebElement rightSectionTable = browserDriver.findElement(By.xpath(
                "(//table[@class='Flex Standard FlexTable']/thead/tr/th)[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, rightSectionTable);
        action.moveToElement(rightSectionTable).perform();
        CSUtility.tempMethodForThreadSleep(1000);
        flexTablePage.getBtnFlexSetting().click();
        CSLogger.info("Clicked on add button");
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        Select presetID = new Select(flexTablePage.getDrpDwnPresetId());
        presetID.selectByVisibleText(templateName);
    }

    /**
     * This method verifies the added attribute and product to table.
     * 
     * @param referenceAttribute String object attribute type
     * @param attrToAdd String object contains standard attribute name
     * @param productName String object contains product name
     * @param attrToAdd String object contains standard attribute name
     */
    private void verifyAddedAttributesAndProduct(String referenceAttribute,
            String attrToAdd, String productName) {
        try {
            String columnData = null;
            String rowsData = null;
            String attributes[] = attrToAdd.split(",");
            String products[] = productName.split(",");
            String elementInHeader = browserDriver.findElement(
                    By.xpath("//table[@class='CSFlexTable FlexTable']"
                            + "//thead//tr[1]//th[1]"))
                    .getText();
            if (attributes[0].contains(elementInHeader)) {
                CSLogger.info("Verified Attribute " + columnData);
            } else {
                CSLogger.error(
                        "Verification fail for Attribute " + elementInHeader);
                softasssert.assertTrue(false,
                        "Verification fail for Attribute " + elementInHeader);
            }
            for (int row = 1; row < attributes.length; row++) {
                WebElement elementHeader = browserDriver.findElement(By.xpath(
                        "//table[@class='CSFlexTable FlexTable']//tbody//tr["
                                + row + "]//td[1]"));
                rowsData = elementHeader.getText();
                if (attributes[row].contains(rowsData)) {
                    CSLogger.info("Verified Attribute " + rowsData);
                } else {
                    CSLogger.error(
                            "Verification failed for Attribute " + rowsData);
                    softasssert.assertTrue(false,
                            "Verification fail for Attribute " + rowsData);
                }
            }
            String refAttribute = browserDriver.findElement(By
                    .xpath("//table[@class='CSFlexTable FlexTable']//tbody//tr["
                            + attributes.length + "]//td[1]"))
                    .getText();
            if (referenceAttribute.contains(refAttribute)) {
                CSLogger.info("Verified Reference Attribute " + refAttribute);
            } else {
                CSLogger.error("Verification failed for Reference Attribute "
                        + refAttribute);
                softasssert.fail("Verification fail for Reference Attribute "
                        + refAttribute);
            }
            int refColumn = attributes.length + 1;
            int numberOfColumns = browserDriver.findElements(By
                    .xpath("//table[@class='CSFlexTable FlexTable']//tbody//tr["
                            + refColumn + "]//td"))
                    .size();
            for (int column = 1; column <= numberOfColumns; column++) {
                WebElement elementHeader = browserDriver.findElement(By.xpath(
                        "//table[@class='CSFlexTable FlexTable']//tbody//tr["
                                + +refColumn + "]//td[" + column + "]"));
                columnData = elementHeader.getText();
                if (products[column - 1].contains(columnData)) {
                    CSLogger.info("Verified products " + columnData);
                } else {
                    CSLogger.error(
                            "Verification fail for product " + columnData);
                    softasssert.fail(
                            "Verification fail for product " + columnData);
                }
            }
        } catch (Exception e) {
            CSLogger.debug("verifyAddedAttributesAndProduct", e);
        }
    }

    /**
     * This method add attributes and product to table.
     * 
     * @param attributeFolder String object attribute folder name
     * @param productName String object contains product name
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param attrToAdd String object contains standard attribute name
     * @param flexLanguage String object contains language for flex table
     * @param flexColumn String object contains column name of
     * @param flexRows String object contains rows name
     */
    private void addAttributeAndProduct(String attributeFolder,
            String productName, String templateName, String tempType,
            String attrToAdd, String flexLanguage, String flexColumn,
            String flexRows) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement prodVsRefNode = browserDriver
                .findElement(By.linkText(tempType));
        CSUtility.waitForVisibilityOfElement(waitForReload, prodVsRefNode);
        prodVsRefNode.click();
        WebElement createdTemplate = browserDriver
                .findElement(By.linkText(templateName));
        createdTemplate.click();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDataPane());
        flexTablePage.getDataPane().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexPresetLanguage());
        Select presetLanguage = new Select(
                flexTablePage.getDrpDwnFlexPresetLanguage());
        presetLanguage.selectByVisibleText(flexLanguage);
        addCloumns(flexColumn);
        addRows(attrToAdd, flexRows);
        addReferenceAttribute(attributeFolder, attrToAdd);
        addObjectInSampleContext(productName);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method add reference attribute in reference to attribute field.
     * 
     * @param attributeFolder String object attribute folder name
     * @param attrToAdd String object contains standard attribute name
     */
    private void addReferenceAttribute(String attributeFolder,
            String attrToAdd) {
        String attributes[] = attrToAdd.split(",");
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement referenceAttribute = browserDriver.findElement(By
                .xpath("//div[contains(text(),'Reference Attribute')]/../.."));
        if (referenceAttribute.getAttribute("class")
                .contains("section_closed")) {
            flexTablePage.getSelectionReferanceAttribute().click();
        }
        flexTablePage.clkOnBtnAddAttributeToReferance(waitForReload);
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogLeftSection(
                waitForReload, browserDriver);
        flexTablePage.clkOnDrpDwnSelectionDialogAttribute(waitForReload);
        WebElement attributeFolderToSelect = browserDriver
                .findElement(By.linkText(attributeFolder));
        attributeFolderToSelect.click();
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogCenterSection(waitForReload,
                        browserDriver);
        WebElement atribute = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr[3]"));
        action.doubleClick(atribute).build().perform();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnBtnAddAttributeIdOfReferance(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogBottomSection(waitForReload,
                        browserDriver, locator);
        for (String selectAttr : attributes) {
            String stdAttributes = "//td[contains(text(),'" + selectAttr
                    + "')]";
            browserDriver.findElement(By.xpath(stdAttributes)).click();
        }
        classPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Referance attribute is added.");
    }

    /**
     * This method add object in sample context section
     * 
     * @param productName String object contains product name
     */
    private void addObjectInSampleContext(String productName) {
        String product[] = productName.split(",");
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement sampleContext = browserDriver.findElement(By.xpath(
                "//div[@id='title__sections::Sample Context']/parent::*"));
        if (sampleContext.getAttribute("class").contains("section_closed")) {
            flexTablePage.getSelectionSampleContext().click();
        }
        flexTablePage.clkOnBtnFlexPresetContextId(waitForReload);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        WebElement selectProduct = browserDriver
                .findElement(By.linkText(product[product.length - 1]));
        action.doubleClick(selectProduct).build().perform();
        CSLogger.info("Sample Context object is added.");
    }

    /**
     * This method add rows in the table.
     * 
     * @param productName String object contains product name
     * @param flexRows String object contains rows name
     */
    private void addRows(String attrToAdd, String flexRows) {
        String attributes[] = attrToAdd.split(",");
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement rows = browserDriver.findElement(
                By.xpath("//div[@id='title__sections::Rows']/parent::*"));
        if (rows.getAttribute("class").contains("section_closed")) {
            flexTablePage.getSelectionRows().click();
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexPresetFieldsDataType());
        Select presetDatatype = new Select(
                flexTablePage.getDrpDwnFlexPresetFieldsDataType());
        presetDatatype.selectByVisibleText(flexRows);
        flexTablePage.clkOnBtnAddFieldsDataSelections(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        TraverseSelectionDialogFrames
                .traverseToDataSelectionDialogBottomSection(waitForReload,
                        browserDriver, locator);
        for (String selectAttr : attributes) {
            String stdAttributes = "//td[contains(text(),'" + selectAttr
                    + "')]";
            browserDriver.findElement(By.xpath(stdAttributes)).click();
        }
        classPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Rows are Added");
    }

    /**
     * This method add columns in the table.
     *
     * @param flexColumn String object contains name of column to select
     */
    private void addCloumns(String flexColumn) {
        WebElement columns = browserDriver.findElement(
                By.xpath("//div[@id='title__sections::Columns']/parent::*"));
        if (columns.getAttribute("class").contains("section_closed")) {
            flexTablePage.getSelectionColumns().click();
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexpresetProductsDataType());
        Select presetDatatype = new Select(
                flexTablePage.getDrpDwnFlexpresetProductsDataType());
        presetDatatype.selectByVisibleText(flexColumn);
        CSLogger.info("Columns are added");
    }

    /**
     * This method verifies the creation of new template
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     */
    private void verifyCreationOfTemplate(String templateName,
            String tempType) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement nodeAttrVsProd = browserDriver
                .findElement(By.linkText(tempType));
        CSUtility.waitForVisibilityOfElement(waitForReload, nodeAttrVsProd);
        nodeAttrVsProd.click();
        List<WebElement> nameOfTemplate = browserDriver
                .findElements(By.linkText(templateName));
        if (nameOfTemplate.isEmpty()) {
            CSLogger.error("Creation of New Template fail");
            Assert.fail("Creation of New Template fail");
        } else {
            CSLogger.info("Creation of Tempalte successful");
        }
    }

    /**
     * This method verifies the creation of new template
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     */
    private void createNewTemplate(String templateName, String tempType) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        CSUtility.rightClickTreeNode(waitForReload,
                flexTablePage.getPresetsNode(), browserDriver);
        flexTablePopup.selectPopupDivMenu(waitForReload,
                flexTablePopup.getCsPopupCreateNew(), browserDriver);
        verifyConfigurationMessage();
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnGeneralPane(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getTxtFlexPresetName());
        flexTablePage.getTxtFlexPresetName().sendKeys(templateName);
        Select userAccess = new Select(
                flexTablePage.getDrpDwnFlexPresetTemplate());
        userAccess.selectByVisibleText(tempType);
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method verifies the message in editor right section
     * 
     */
    private void verifyConfigurationMessage() {
        try {
            TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                    waitForReload, browserDriver, locator);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmRight()));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getConfigurationMessage());
            String windowMessage = flexTablePage.getConfigurationMessage()
                    .getText();
            if (windowMessage.equals("Configuration required")) {
                CSLogger.info("Message Verified Sucessfully");
            } else {
                CSLogger.error("Message verification fail");
                softasssert.fail("Message verification fail");
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error : verifyConfigurationMessage", e);
        }
    }

    /**
     * This method creates the pre-requisite data require for current test case.
     * 
     * @param attributeFolder String object attribute folder name
     * @param referenceAttribute String object attribute type
     * @param referanceClass String object contains class name
     * @param flexClass String object contains class name
     * @param productName String object contains product name
     */
    private void createDataForTestCase(String attributeFolder,
            String referenceAttribute, String referanceClass, String flexClass,
            String productName) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        createAttributeFolder(attributeFolder);
        createCrossReferenceAttribute(attributeFolder, referenceAttribute);
        createClass(referanceClass);
        dragAndDropAttrToClass(attributeFolder, referanceClass);
        createProduct(productName);
        dragAndDropClassToProduct(referanceClass, productName);
        addProductToRefAttribute(referenceAttribute, productName);
        dragAndDropClassToProduct(flexClass, productName);
    }

    /**
     * This method add the product to reference attribute of product
     * 
     * @param referenceAttribute String object attribute type
     * @param productName String object contains product name
     */
    private void addProductToRefAttribute(String referenceAttribute,
            String productName) {
        String label[] = productName.split(",");
        for (String NameofProduct : label) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            pimStudioProductsNode.clickOnNodeProducts();
            WebElement productToOpen = browserDriver
                    .findElement(By.linkText(NameofProduct));
            productToOpen.click();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            flexTablePage.clkOnDataTab(waitForReload);
            WebElement attributeType = browserDriver.findElement(By.xpath(
                    "//span[contains(text(),'" + referenceAttribute + "')]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, attributeType);
            flexTablePage.clkOnBtnAddSelectionList(waitForReload);
            TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                    waitForReload, browserDriver);
            WebElement selectProduct = browserDriver
                    .findElement(By.linkText(label[0]));
            action.doubleClick(selectProduct).build().perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            CSUtility.tempMethodForThreadSleep(1000);
        }
        CSLogger.info("Product has been Assigned");
    }

    /**
     * This method drag and drop class to product.
     * 
     * @param className String object contains class name
     * @param productName String object contains product name
     */
    private void dragAndDropClassToProduct(String className,
            String productName) {
        String label[] = productName.split(",");
        for (String NameofProduct : label) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            pimStudioProductsNode.clickOnNodeProducts();
            WebElement productToDragDrop = browserDriver
                    .findElement(By.linkText(NameofProduct));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance.getBtnPimSettingsNode());
            pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance
                            .getBtnPimSettingsClassesNode());
            pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode()
                    .click();
            WebElement classNameToDragDrop = browserDriver
                    .findElement(By.linkText(className));
            action.dragAndDrop(classNameToDragDrop, productToDragDrop).build()
                    .perform();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(pimStudioProductsNode.getproductWindow()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            pimStudioProductsNode.getproductWindowFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDialogContentIdInstance.getBtnExtend()));
            csGuiDialogContentIdInstance.getBtnExtend().click();
            CSLogger.info("Drag and Drop class to Product");
        }
    }

    /**
     * This method create product.
     * 
     * @param productName String object contains product name
     */
    private void createProduct(String productName) {
        String label[] = productName.split(",");
        for (String NameofProduct : label) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            CSUtility.rightClickTreeNode(waitForReload,
                    pimStudioProductsNode.getBtnPimProductsNode(),
                    browserDriver);
            productPopUp.selectPopupDivMenu(waitForReload,
                    productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            productPopUp.enterValueInDialogue(waitForReload, NameofProduct);
            productPopUp.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
        }
        CSLogger.info("Product Created");
    }

    /**
     * This method drag and drop attribute to class
     * 
     * @param flexAttrFolder String object attribute folder name
     * @param className String object contains class name
     */
    private void dragAndDropAttrToClass(String AttributeFolder,
            String referenceClass) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode().click();
        WebElement attrFolderToDragDrop = browserDriver
                .findElement(By.linkText(AttributeFolder));
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(referenceClass));
        action.dragAndDrop(attrFolderToDragDrop, classNameToDragDrop).build()
                .perform();
        CSLogger.info("Drag and Drop attribute to class");
    }

    /**
     * This method create class.
     * 
     * @param className String object contains class name
     */
    private void createClass(String className) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode(),
                browserDriver);
        classPopUp.selectPopupDivMenu(waitForReload,
                classPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        classPopUp.enterValueInDialogue(waitForReload, className);
        classPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Class Created");
    }

    /**
     * This method create Cross Reference type attribute.
     * 
     * @param attributeFolder String object attribute folder name
     * @param referenceAttribute String object attribute type
     */
    private void createCrossReferenceAttribute(String attributeFolder,
            String referenceAttribute) {
        createSingleLineAttribute(attributeFolder, referenceAttribute);
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(attributeFolder));
        createdAttributeFolder.click();
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(referenceAttribute));
        createdAttribute.click();
        CSLogger.info("Clicked on created attribute");
        selectAttributeType();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Created flex Table type attribute");
    }

    /**
     * This method create single line type attribute
     * 
     * @param attributeFolder String object attribute folder name
     * @param flexAttribute String object attribute type
     */
    private void createSingleLineAttribute(String attributeFolder,
            String referenceAttribute) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(attributeFolder));
        CSUtility.rightClickTreeNode(waitForReload, createdAttributeFolder,
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        attributePopup.enterValueInUserInputDialogue(waitForReload,
                referenceAttribute);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * This method select the Referance to Pim type attribute
     * 
     */
    private void selectAttributeType() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentIdInstance.getBtnCSTypeLabel());
        csGuiDialogContentIdInstance.getBtnCSTypeLabel().click();
        csPortalWindowInstance
                .clkDdCSPortalGuiListCrossReference(waitForReload);
        csPortalWindowInstance.selectCrossReferenceAttributeType(waitForReload,
                csPortalWindowInstance.getlblReferenceToPimProduct(),
                "Reference to Pim");
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
        getAlertBox(true);
        CSLogger.info("flex Table Attribute type selected");
    }

    /**
     * This method click the button in alert box
     * 
     * @param pressKey boolean object
     */
    private void getAlertBox(Boolean pressKey) {
        Alert alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (pressKey) {
            alertBox.accept();
            CSLogger.info("Clicked on Ok button.");
        } else {
            alertBox.dismiss();
            CSLogger.info("Clicked on Cancel button.");
        }
    }

    /**
     * This method create attribute folder.
     * 
     * @param attrFolderName String object attribute folder name
     */
    private void createAttributeFolder(String attrFolderName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode(),
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuNewFolder(), browserDriver);
        attributePopup.enterValueInDialogue(waitForReload, attrFolderName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Attribute Folder,Reference Attribute,ReferanceClass,FlexClass,
     * Product Name,FlexTableProductName,TemplateName,TemplateType,
     * AttributeToAdd,FlexLanguage,FlexTableColumn,FlexTableRows
     * 
     * @return createProductVsReferencesTestData
     */
    @DataProvider(name = "createProductVsReferencesDataSheet")
    public Object[] productVsReferencesTemplate() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                createProductVsReferencesTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csPortalWindowInstance = new CSPortalWindow(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        action = new Actions(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        flexTablePopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        softasssert = new SoftAssert();
    }
}
