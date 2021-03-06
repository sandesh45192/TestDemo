/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
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
 * This class contains the test methods which verifies Attribute Vs Product
 * editor template test uses.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateAttributeVsProductEditorTemplateTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private IClassPopup               classPopUp;
    private IProductPopup             productPopUp;
    private CSPortalHeader            csPortalHeader;
    private FrameLocators             locator;
    private FlexTablePage             flexTablePage;
    private IFlexTablePopup           flexTablePopup;
    private SoftAssert                softasssert;
    private String                    createAttributeVsProductEditorTestData = "AttributeVsProductEditor";

    /**
     * This method verifies Attribute vs Product Editor template test user.
     * 
     * @param className String object contains class name
     * @param productName String object contains product name
     * @param flexProduct String object contains product name
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param attrToAdd String object contains standard attribute name
     * @param flexLanguage String object contains language for flex table
     * @param flexColumn String object contains column name of
     * @param flexRows String object contains rows name
     */
    @Test(dataProvider = "createAttributeVsProductEditorDataSheet")
    public void testCreateAttributeVsProductEditorTemplate(String className,
            String productName, String flexProduct, String templateName,
            String tempType, String attrToAdd, String flexLanguage,
            String flexColumn, String flexRows) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
            createProduct(flexProduct);
            dragAndDropClassToProduct(className, flexProduct);
            createNewTemplate(templateName, tempType);
            verifyCreationOfTemplate(templateName, tempType);
            addAttributeAndProduct(productName, templateName, tempType,
                    attrToAdd, flexLanguage, flexColumn, flexRows);
            CSUtility.tempMethodForThreadSleep(1000);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmRight()));
            verifyAddedAttributesAndProduct(attrToAdd, productName);
            addCreatedFlexTable(flexProduct, templateName);
            testResetTableButton();
            verifyFlexTableAdded(attrToAdd, productName, false);
            addCreatedFlexTable(flexProduct, templateName);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getBtnOk());
            flexTablePage.getBtnOk().click();
            checkInProduct();
            verifyFlexTableAdded(attrToAdd, productName, true);
            softasssert.assertAll();
            CSLogger.info(
                    "create attribute vs product editor template test completed");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testCreateAttributeVsProductEditorTemplate",
                    e);
            Assert.fail(
                    "Automation Error : testCreateAttributeVsProductEditorTemplate",
                    e);
        }
    }

    /**
     * This method verifies Attribute Vs Product Editor template test user.
     * 
     * @param attrToAdd String object contains standard attribute name
     * @param productName String object contains product name
     * @param chkStatus boolean object
     */
    private void verifyFlexTableAdded(String attrToAdd, String productName,
            Boolean chkStatus) {
        String numberOFAttribute[] = attrToAdd.split(",");
        int columnsInTable = 0;
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        flexTablePage.clkOnDataTab(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getRightSectionEditor());
        if (chkStatus) {
            WebElement editImage = browserDriver.findElement(By.xpath(
                    "(//div[contains(@id,'PdmarticleCS_ItemAttribute')]//td[@class="
                            + "'CSGuiToolbarHorizontalCell']//a[1]//img)[1]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, editImage);
            String imageAttribute = editImage.getAttribute("src");
            if (imageAttribute.contains("Fedit-table.png")) {
                CSLogger.info("Edit Table Icon Verified Successfully");
            } else {
                CSLogger.error("Edit Table Icon not Verified");
                softasssert.fail("Edit Table Icon not Verified");
            }
            verifyAddedAttributesAndProduct(attrToAdd, productName);
        } else {
            columnsInTable = browserDriver.findElements(By.xpath(
                    "//table[@class='Flex Standard FlexTable']//thead//tr//th"))
                    .size();
            if (columnsInTable != numberOFAttribute.length) {
                CSLogger.info("Reset Table Case Successful");
            } else {
                CSLogger.error("Reset Table Case fail");
                Assert.fail("Reset Table Case fail");
            }
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
     * This method add the created flex table to product.
     * 
     * @param flexProduct String object contains product name
     * @param templateName String object contains template name
     */
    private void addCreatedFlexTable(String flexProduct, String templateName) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement flexTableProduct = browserDriver
                .findElement(By.linkText(flexProduct));
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
     * @param productName String object contains product name
     * @param attrToAdd String object contains standard attribute name
     */
    private void verifyAddedAttributesAndProduct(String attrToAdd,
            String productName) {
        try {
            String columnData = null;
            String rowsData = null;
            String attributes[] = attrToAdd.split(",");
            String products[] = productName.split(",");
            int numberOfColumns = browserDriver.findElements(By.xpath(
                    "//table[@class='CSFlexTable FlexTable']//thead//tr//th"))
                    .size();
            for (int column = 1; column <= numberOfColumns; column++) {
                WebElement elementHeader = browserDriver.findElement(By.xpath(
                        "//table[@class='CSFlexTable FlexTable']//thead//tr//th["
                                + column + "]"));
                columnData = elementHeader.getText();
                if (attributes[column - 1].contains(columnData)) {
                    CSLogger.info("Verified Attribute " + columnData);
                } else {
                    CSLogger.error(
                            "Verification fail for Attribute " + columnData);
                    softasssert.fail(
                            "Verification fail for Attribute " + columnData);
                }
            }
            int numberOfRows = browserDriver.findElements(By.xpath(
                    "//table[@class='CSFlexTable FlexTable']//tbody//tr"))
                    .size();
            for (int row = 1; row <= numberOfRows; row++) {
                WebElement elementHeader = browserDriver.findElement(By.xpath(
                        "//table[@class='CSFlexTable FlexTable']//tbody//tr["
                                + row + "]//td[2]"));
                rowsData = elementHeader.getText();
                if (products[row - 1].contains(rowsData)) {
                    CSLogger.info("Verified Product " + rowsData);
                } else {
                    CSLogger.error(
                            "Verification failed for Product " + rowsData);
                    softasssert
                            .fail("Verification fail for Product " + rowsData);
                }
            }
        } catch (Exception e) {
            CSLogger.debug("Automation error : verifyAddedAttributesAndProduct",
                    e);
        }
    }

    /**
     * This method add attributes and product to table.
     * 
     * @param productName String object contains product name
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param attrToAdd String object contains standard attribute name
     * @param flexLanguage String object contains language for flex table
     * @param flexColumn String object contains column name of
     * @param flexRows String object contains rows name
     */
    private void addAttributeAndProduct(String productName, String templateName,
            String tempType, String attrToAdd, String flexLanguage,
            String flexColumn, String flexRows) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement nodeAttrVsProd = browserDriver
                .findElement(By.linkText(tempType));
        CSUtility.waitForVisibilityOfElement(waitForReload, nodeAttrVsProd);
        nodeAttrVsProd.click();
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
        addCloumns(attrToAdd, flexColumn);
        addRows(productName, flexRows);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method add rows in the table.
     * 
     * @param productName String object contains product name
     * @param flexRows String object contains rows name
     */
    private void addRows(String productName, String flexRows) {
        String products[] = productName.split(",");
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement rows = browserDriver.findElement(
                By.xpath("//div[@id='title__sections::Rows']/parent::*"));
        if (rows.getAttribute("class").contains("section_closed")) {
            flexTablePage.getSelectionRows().click();
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexpresetProductsDataType());
        Select presetDatatype = new Select(
                flexTablePage.getDrpDwnFlexpresetProductsDataType());
        presetDatatype.selectByVisibleText(flexRows);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getBtnAddProducts());
        flexTablePage.getBtnAddProducts().click();
        CSUtility.tempMethodForThreadSleep(1000);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        for (String name : products) {
            WebElement selectProduct = browserDriver
                    .findElement(By.linkText(name));
            action.doubleClick(selectProduct).build().perform();
        }
        classPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Rows are Added");
    }

    /**
     * This method add columns in the table.
     * 
     * @param attrToAdd String object contains standard attribute name
     * @param flexColumn String object contains column name of
     */
    private void addCloumns(String attrToAdd, String flexColumn) {
        String attributes[] = attrToAdd.split(",");
        WebElement columns = browserDriver.findElement(
                By.xpath("//div[@id='title__sections::Columns']/parent::*"));
        if (columns.getAttribute("class").contains("section_closed")) {
            flexTablePage.getSelectionColumns().click();
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnFlexpresetColsDataType());
        Select presetDatatype = new Select(
                flexTablePage.getDrpDwnFlexpresetColsDataType());
        presetDatatype.selectByVisibleText(flexColumn);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getBtnAddData());
        flexTablePage.getBtnAddData().click();
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
        CSLogger.info("columns are Added");
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
        WebElement attrVsProdNode = browserDriver
                .findElement(By.linkText(tempType));
        CSUtility.waitForVisibilityOfElement(waitForReload, attrVsProdNode);
        attrVsProdNode.click();
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
     * This method create product.
     * 
     * @param productName String object contains product name
     */
    private void createProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, productName);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Product Created");
    }

    /**
     * This method drag and drop class to product.
     * 
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     */
    private void dragAndDropClassToProduct(String className,
            String flexProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToDragDrop = browserDriver
                .findElement(By.linkText(flexProduct));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode().click();
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        action.dragAndDrop(classNameToDragDrop, productToDragDrop).build()
                .perform();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentIdInstance.getBtnReplace()));
        csGuiDialogContentIdInstance.getBtnReplace().click();
        CSLogger.info("Drag and Drop class to Product");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains ClassName,ProductName,FlexTableProductName,TemplateName,
     * TemplateType,AttributeToAdd,FlexLanguage,FlexTableColumn,FlexTableRows
     * 
     * @return createAttributeVsProductEditorTest
     */
    @DataProvider(name = "createAttributeVsProductEditorDataSheet")
    public Object[] attributeVsProductEditorTemplate() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                createAttributeVsProductEditorTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
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
