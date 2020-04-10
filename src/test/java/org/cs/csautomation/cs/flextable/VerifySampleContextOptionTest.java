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
 * This class contains the test methods which verifies Sample Context Option
 * test uses.
 * 
 * @author CSAutomation Team
 */
public class VerifySampleContextOptionTest extends AbstractTest {

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
    private String                    sampleContextOptionTestData = "SampleContextOption";

    /**
     * This method verifies Sample Context Option test uses.
     * 
     * @param className String object contains class name
     * @param ProductTree String object contains template type
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     * @param flexLanguage String object contains flex language
     * @param attrToAdd String object contains attribute to add
     * @param flexColumn String object contains flex column name
     * @param flexRows String object contains flex row name
     * @param selectLevel String object contains level select option
     * @param treeSelection String object contains tree selection option
     * @param contextType String object contains context type name
     * @param contextObjext String object contains name of object
     * @param contextLanguage String object contains context language name
     */
    @Test(dataProvider = "SampleContextOptionDataSheet")
    public void testVerifySampleContextOption(String className,
            String ProductTree, String templateName, String templateType,
            String flexLanguage, String attrToAdd, String flexColumn,
            String flexRows, String selectLevel, String treeSelection,
            String contextType, String contextObject, String contextLanguage) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createProductTree(className, ProductTree);
            createNewTemplate(templateName, templateType);
            verifyCreationOfTemplate(templateName, templateType);
            addAttributeAndProduct(templateName, templateType, flexLanguage,
                    attrToAdd, flexColumn, flexRows, selectLevel, treeSelection,
                    contextType, contextObject, contextLanguage);
            CSUtility.tempMethodForThreadSleep(1000);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmRight()));
            verifyAddedAttributesAndProduct(attrToAdd, ProductTree);
            addCreatedFlexTable(ProductTree, templateName);
            testResetTableButton();
            verifyFlexTableAdded(attrToAdd, ProductTree, false);
            addCreatedFlexTable(ProductTree, templateName);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getBtnOk());
            flexTablePage.getBtnOk().click();
            checkInProduct();
            verifyFlexTableAdded(attrToAdd, ProductTree, true);
            softasssert.assertAll();
            CSLogger.info("verify sample context option test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerifyAllowAccessByOption",
                    e);
            Assert.fail("Automation Error : testVerifyAllowAccessByOption", e);
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
     * @param ProductTree String object contains products name
     * @param templateName String object contains template name
     */
    private void addCreatedFlexTable(String ProductTree, String templateName) {
        String[] productArray = ProductTree.split(",");
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement flexTableProduct = browserDriver
                .findElement(By.linkText(productArray[0]));
        action.doubleClick(flexTableProduct).build().perform();
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
                                + row + "]//td[1]"));
                rowsData = elementHeader.getText();
                if (products[row].contains(rowsData)) {
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
     * This method add attributes and table to product.
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     * @param flexLanguage String object contains flex language
     * @param attrToAdd String object contains atribute to add
     * @param flexColumn String object contains flex column name
     * @param flexRows String object contains flex row name
     * @param selectLevel String object contains level select option
     * @param treeSelection String object contains tree selection option
     * @param contextType String object contains context type name
     * @param contextObjext String object contains name of object
     * @param contextLanguage String object contains context language name
     */
    private void addAttributeAndProduct(String templateName,
            String templateType, String flexLanguage, String attrToAdd,
            String flexColumn, String flexRows, String selectLevel,
            String treeSelection, String contextType, String contextObject,
            String contextLanguage) {
        goToNodeFlexTablePreset();
        WebElement nodeAttrVsProd = browserDriver
                .findElement(By.linkText(templateType));
        clkElement(nodeAttrVsProd);
        WebElement createdTemplate = browserDriver
                .findElement(By.linkText(templateName));
        clkElement(createdTemplate);
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
        addRows(flexRows, selectLevel, treeSelection);
        addDataInSampleContext(contextType, contextObject, contextLanguage);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method add data in sample context section
     * 
     * @param contextType String object contains context type name
     * @param contextObjext String object contains name of object
     * @param contextLanguage String object contains context language name
     */
    private void addDataInSampleContext(String contextType,
            String contextObject, String contextLanguage) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement sampleContext = browserDriver.findElement(By.xpath(
                "//div[@id='title__sections::Sample Context']/parent::*"));
        if (sampleContext.getAttribute("class").contains("section_closed")) {
            flexTablePage.getSelectionSampleContext().click();
        }
        selectElementFromDrpDwn(flexTablePage.getDrpDwnFlexPresetContextClass(),
                contextType);
        selectElementFromDrpDwn(
                flexTablePage.getDrpDwnFlexPresetContextLanguage(),
                contextLanguage);
        flexTablePage.clkOnBtnFlexPresetContextId(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        WebElement selectProduct = browserDriver
                .findElement(By.linkText(contextObject));
        action.doubleClick(selectProduct).build().perform();

    }

    /**
     * This method add rows in the table.
     * 
     * @param flexRows String object contains flex row name
     * @param selectLevel String object contains level select option
     * @param treeSelection String object contains tree selection option
     */
    private void addRows(String flexRows, String selectLevel,
            String treeSelection) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement rows = browserDriver.findElement(
                By.xpath("//div[@id='title__sections::Rows']/parent::*"));
        if (rows.getAttribute("class").contains("section_closed")) {
            flexTablePage.getSelectionRows().click();
        }
        selectElementFromDrpDwn(
                flexTablePage.getDrpDwnFlexpresetProductsDataType(), flexRows);
        CSUtility.tempMethodForThreadSleep(1000);
        selectElementFromDrpDwn(
                flexTablePage.getDrpDwnFlexPresetProductsChildrenLevel(),
                selectLevel);
        selectElementFromDrpDwn(
                flexTablePage.getDrpDwnFlexPresetProductsFolderOption(),
                treeSelection);
    }

    /**
     * This method add columns in the table.
     * 
     * @param attrToAdd String object contains atribute to add
     * @param flexColumn String object contains flex column name
     */
    private void addCloumns(String attrToAdd, String flexColumn) {
        String attributes[] = attrToAdd.split(",");
        WebElement columns = browserDriver.findElement(
                By.xpath("//div[@id='title__sections::Columns']/parent::*"));
        if (columns.getAttribute("class").contains("section_closed")) {
            flexTablePage.getSelectionColumns().click();
        }
        selectElementFromDrpDwn(flexTablePage.getDrpDwnFlexpresetColsDataType(),
                flexColumn);
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
     * This method select the element from drop down
     * 
     * @param element WebElement contains drop down
     * @param value string object contains value to select
     */
    private void selectElementFromDrpDwn(WebElement element, String value) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        Select elementValue = new Select(element);
        elementValue.selectByVisibleText(value);
    }

    /**
     * This method verifies the creation of new template
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     */
    private void verifyCreationOfTemplate(String templateName,
            String tempType) {
        goToNodeFlexTablePreset();
        WebElement nodeAttrVsProd = browserDriver
                .findElement(By.linkText(tempType));
        clkElement(nodeAttrVsProd);
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
        goToNodeFlexTablePreset();
        CSUtility.rightClickTreeNode(waitForReload,
                flexTablePage.getPresetsNode(), browserDriver);
        flexTablePopup.selectPopupDivMenu(waitForReload,
                flexTablePopup.getCsPopupCreateNew(), browserDriver);
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
     * This method traverse to the preset node of flex table
     * 
     */
    private void goToNodeFlexTablePreset() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
        // CSUtility.waitForVisibilityOfElement(waitForReload,
        // activeJobsPage.getSystemPreferences());
        // activeJobsPage.clkWebElement(activeJobsPage.getSystemPreferences());
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        flexTablePage.clkOnFlexTableNode(waitForReload);
        flexTablePage.clkOnPresetsNode(waitForReload);
    }

    /**
     * This method create product in tree structure
     * 
     * @param ProductTree String object contains template type
     */
    private void createProductTree(String className, String ProductTree) {
        String[] arrayOfProduct = ProductTree.split(",");
        createProduct(arrayOfProduct[0]);
        for (int count = 1; count < arrayOfProduct.length; count++) {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            WebElement parentNode = browserDriver
                    .findElement(By.linkText(arrayOfProduct[count - 1]));
            CSUtility.rightClickTreeNode(waitForReload, parentNode,
                    browserDriver);
            productPopUp.selectPopupDivMenu(waitForReload,
                    productPopUp.getCsGuiPopupMenuNewChild(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            productPopUp.enterValueInDialogue(waitForReload,
                    arrayOfProduct[count]);
            productPopUp.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            CSLogger.info("Product Created");
        }
        dragAndDropClassToProduct(className, arrayOfProduct[0]);
    }

    /**
     * This method clicks on input element
     * 
     * @param element contains web element to be clicked
     */
    public void clkElement(WebElement element) {
        CSUtility.waitForElementToBeClickable(waitForReload, element);
        element.click();
    }

    /**
     * This method create product.
     * 
     * @param productName String object contains product name
     */
    private void createProduct(String flexProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, flexProduct);
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
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
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
     * contains ClassName,FlexTableProductName,ProductTree,TemplateName,
     * TemplateType,FlexLanguage,AttributeToAdd,FlexTableColumn,FlexTableRows,
     * SelectLevels,TreeSelection,ContextType,ContextObject,ContextLanguage
     * 
     * @return sampleContextOptionTestData
     */
    @DataProvider(name = "SampleContextOptionDataSheet")
    public Object[] sampleContextOption() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                sampleContextOptionTestData);
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
