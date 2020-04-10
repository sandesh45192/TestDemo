/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
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
 * This class contains the test methods which verifies free template test uses.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateFreeTemplateTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private Actions                   action;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private IProductPopup             productPopUp;
    private CSPortalHeader            csPortalHeader;
    private FrameLocators             locator;
    private FlexTablePage             flexTablePage;
    private IFlexTablePopup           flexTablePopup;
    private SoftAssert                softasssert;
    private String                    createFreeTemplateTestData = "FreeTemplate";

    /**
     * This method verifies Free template test uses.
     * 
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param tableCaption String object contains caption of table
     * @param tableHeader String object contains Header Text
     * @param row1 String object contains text data for row 1
     * @param row2 String object contains text data for row 2
     * @param row3 String object contains text data for row 3
     */
    @Test(dataProvider = "CreateFreeTemplateDataSheet")
    public void testCreateFreeTemplate(String className, String flexProduct,
            String templateName, String tempType, String tableCaption,
            String tableHeader, String row1, String row2, String row3) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
            createProduct(flexProduct);
            dragAndDropClassToProduct(className, flexProduct);
            createNewTemplate(templateName, tempType);
            verifyCreationOfTemplate(templateName, tempType);
            createTable(templateName, tempType, tableCaption, tableHeader, row1,
                    row2, row3);
            CSUtility.tempMethodForThreadSleep(1000);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmRight()));
            verifyAddedDataInTable(tableCaption, tableHeader, row1, row2, row3);
            addRow();
            addColumn();
            removeRow();
            removeColumn();
            addCreatedFlexTable(flexProduct, templateName);
            testResetTableButton();
            verifyFlexTableAdded(tableCaption, tableHeader, row1, row2, row3,
                    false);
            addCreatedFlexTable(flexProduct, templateName);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getBtnOk());
            flexTablePage.getBtnOk().click();
            checkInProduct();
            verifyFlexTableAdded(tableCaption, tableHeader, row1, row2, row3,
                    true);
            softasssert.assertAll();
            CSLogger.info("create free template test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testCreateFreeTemplate", e);
            Assert.fail("Automation Error : testCreateFreeTemplate", e);
        }
    }

    /**
     * This method verifies Added flex Table to the product.
     * 
     * @param tableCaption String object contains caption of table
     * @param tableHeader String object contains Header Text
     * @param row1 String object contains text data for row 1
     * @param row2 String object contains text data for row 2
     * @param row3 String object contains text data for row 3
     */
    private void verifyFlexTableAdded(String tableCaption, String tableHeader,
            String row1, String row2, String row3, Boolean chkStatus) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        flexTablePage.clkOnDataTab(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getRightSectionEditor());
        if (chkStatus) {
            verifyAddedDataInTable(tableCaption, tableHeader, row1, row2, row3);
        } else {
            WebElement standardTable = browserDriver.findElement(
                    By.xpath("//div[contains(@id,'PdmarticleCS_ItemAttribute')]"
                            + "/table[1]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, standardTable);
            if (standardTable.getAttribute("class")
                    .equals("Flex Standard FlexTable")) {
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
     * This method add column In Table
     * 
     */
    private void addColumn() {
        int numberOfColumn = mouseHoverToLastColumn();
        WebElement btnAddColumn = browserDriver.findElement(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div["
                        + numberOfColumn
                        + "]//img[contains(@src,'src=plus')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnAddColumn);
        btnAddColumn.click();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        verifyAddedColumn(numberOfColumn);
    }

    /**
     * This method remove column from Table
     * 
     */
    private void removeColumn() {
        int numberOfColumn = mouseHoverToLastColumn();
        WebElement btnAddColumn = browserDriver.findElement(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div["
                        + numberOfColumn
                        + "]//img[contains(@src,'src=minus')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnAddColumn);
        btnAddColumn.click();
        getAlertBox(true);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        verifyRemovedColumn(numberOfColumn);
    }

    /**
     * This method mouse hover tolast column
     * 
     * @return numberOfColumn object contains number of column
     */
    private int mouseHoverToLastColumn() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//div[contains(@class,'GridAddImagesContainer')]")));
        int numberOfColumn = browserDriver.findElements(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div"))
                .size();
        WebElement imageContainer = browserDriver.findElement(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div["
                        + numberOfColumn + "]"));
        action.moveToElement(imageContainer).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        return numberOfColumn;
    }

    /**
     * This method verifies added columns
     * 
     * @param numberOfColumn integer object contains number of column
     */
    private void verifyAddedColumn(int numberOfColumn) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//div[contains(@class,'GridAddImagesContainer')]")));
        int currentColumnCount = browserDriver.findElements(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div"))
                .size();
        if (currentColumnCount > numberOfColumn) {
            CSLogger.info("Column Added Successfully");
        } else {
            CSLogger.info("column is not added");
            softasssert.fail("Column is not added");
        }
    }

    /**
     * This verifies removed column
     * 
     * @param numberOfColumn integer object contains number of column
     */
    private void verifyRemovedColumn(int numberOfColumn) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//div[contains(@class,'GridAddImagesContainer')]")));
        int currentColumnCount = browserDriver.findElements(By.xpath(
                "//div[contains(@class,'GridAddImagesContainer')]//div[1]//div"))
                .size();
        if (currentColumnCount < numberOfColumn) {
            CSLogger.info("Column removed Successfully");
        } else {
            CSLogger.info("column is not removed");
            softasssert.fail("Column is not removed");
        }
    }

    /**
     * This method add Row In Table
     * 
     */
    private void addRow() {
        int numberOfRow = mouseHoverToLastRow();
        WebElement btnAddRow = browserDriver.findElement(
                By.xpath("//ul[contains(@class,'Grid')]//li[" + numberOfRow
                        + "]//div[2]//img[contains(@src,'src=plus')]"));

        CSUtility.waitForVisibilityOfElement(waitForReload, btnAddRow);
        btnAddRow.click();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        verifyAddedRow(numberOfRow);
    }

    /**
     * This method remove row from Table
     * 
     */
    private void removeRow() {
        int numberOfRow = mouseHoverToLastRow();
        WebElement btnRemoveRow = browserDriver.findElement(
                By.xpath("//ul[contains(@class,'Grid')]//li[" + numberOfRow
                        + "]//div[2]//img[contains(@src,'src=minus')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnRemoveRow);
        btnRemoveRow.click();
        getAlertBox(true);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        verifyRemovedRow(numberOfRow);
    }

    /**
     * This method mouse hover to the last row
     * 
     * @return numberOfRow object contains number of rows
     */
    private int mouseHoverToLastRow() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//ul[contains(@class,'Grid')]")));
        int numberOfRow = browserDriver
                .findElements(By.xpath("//ul[contains(@class,'Grid')]//li"))
                .size();
        WebElement actionContainer = browserDriver
                .findElement(By.xpath("//ul[contains(@class,'Grid')]//li["
                        + numberOfRow + "]//div[2]"));
        action.moveToElement(actionContainer).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        return numberOfRow;
    }

    /**
     * This method verifies added row
     * 
     * @param previousRowcount integer object contains number of row
     */
    private void verifyAddedRow(int previousRowcount) {
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//ul[contains(@class,'Grid')]")));
        int currentRowCount = browserDriver
                .findElements(By.xpath("//ul[contains(@class,'Grid')]//li"))
                .size();
        if (currentRowCount > previousRowcount) {
            CSLogger.info("Row Added Successfully");
        } else {
            CSLogger.info("Row is not added");
            softasssert.fail("Row is not added");
        }
    }

    /**
     * This method verifies removed row
     * 
     * @param previousRowcount integer object contains number of row
     */
    private void verifyRemovedRow(int previousRowcount) {
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//ul[contains(@class,'Grid')]")));
        int currentRowCount = browserDriver
                .findElements(By.xpath("//ul[contains(@class,'Grid')]//li"))
                .size();
        if (currentRowCount < previousRowcount) {
            CSLogger.info("Row removed Successfully");
        } else {
            CSLogger.info("Row is not removed");
            softasssert.fail("Row is not removed");
        }
    }

    /**
     * This method verifies the added data in products.
     * 
     * @param tableCaption String object contains caption of table
     * @param tableHeader String object contains Header Text
     * @param row1 String object contains text data for row 1
     * @param row2 String object contains text data for row 2
     * @param row3 String object contains text data for row 3
     */
    private void verifyAddedDataInTable(String tableCaption, String tableHeader,
            String row1, String row2, String row3) {
        try {
            String textHeader = null;
            String HeaderData[] = tableHeader.split(",");
            WebElement caption = browserDriver.findElement(By
                    .xpath("//table[@class='CSFlexTable FlexTable']//caption"));
            String textCaption = caption.getText();
            if (textCaption.equals(tableCaption)) {
                CSLogger.info("Caption verification successfully");
            } else {
                CSLogger.error("Caption verfication failed");
                softasssert.fail("Caption verfication failed");
            }
            int countHeader = browserDriver.findElements(By.xpath(
                    "//table[@class='CSFlexTable FlexTable']//thead//tr//th"))
                    .size();
            for (int count = 1; count <= countHeader; count++) {
                WebElement elementHeader = browserDriver.findElement(By.xpath(
                        "//table[@class='CSFlexTable FlexTable']//thead//tr//th["
                                + count + "]"));
                textHeader = elementHeader.getText();
                if (HeaderData[count - 1].equals(textHeader)) {
                    CSLogger.info("Verification Pass for Header " + textHeader);
                } else {
                    CSLogger.error(
                            "Verification fail for Header " + textHeader);
                    softasssert
                            .fail("Verification fail for Header " + textHeader);
                }
            }
            verifyRow(row1, "1");
            verifyRow(row2, "2");
            verifyRow(row3, "3");
        } catch (Exception e) {
            CSLogger.debug("Automation error : verifyAddedDataInTable", e);
        }
    }

    /**
     * This method verifies data in row
     * 
     * @param Arrayrow String object contains text data for row
     * @param rowNumber String object contains number of row
     */
    private void verifyRow(String Arrayrow, String rowNumber) {
        String textRow = null;
        String rowContain[] = Arrayrow.split(",");
        int countRow = browserDriver.findElements(
                By.xpath("//table[@class='CSFlexTable FlexTable']//tbody//tr["
                        + rowNumber + "]//td"))
                .size();
        for (int row = 1; row <= countRow; row++) {
            WebElement elementInRow = browserDriver.findElement(By
                    .xpath("//table[@class='CSFlexTable FlexTable']//tbody//tr["
                            + rowNumber + "]//td[" + row + "]"));
            textRow = elementInRow.getText();
            if (rowContain[row - 1].equals(textRow)) {
                CSLogger.info("Verification Pass for row element " + textRow);
            } else {
                CSLogger.error("Verification fail for row element " + textRow);
                softasssert
                        .fail("Verification fail for row element " + textRow);
            }
        }
    }

    /**
     * This method create a table in template.
     * 
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param tableCaption String object contains caption of table
     * @param tableHeader String object contains Header Text
     * @param row1 String object contains text data for row 1
     * @param row2 String object contains text data for row 2
     * @param row3 String object contains text data for row 3
     */
    private void createTable(String templateName, String tempType,
            String tableCaption, String tableHeader, String row1, String row2,
            String row3) {
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
                flexTablePage.getTxtFlexPresetTitle());
        flexTablePage.getTxtFlexPresetTitle().clear();
        flexTablePage.getTxtFlexPresetTitle().sendKeys(tableCaption);
        addDataInTable(tableHeader, "1");
        addDataInTable(row1, "2");
        addDataInTable(row2, "3");
        addDataInTable(row3, "4");
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method add Data in the table.
     * 
     * @param rowText String object contains text data for row
     * @param row String object contains number of row
     */
    private void addDataInTable(String rowText, String row) {
        String text[] = rowText.split(",");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.id("GridDiv_Data")));
        for (int colIndex = 1; colIndex <= text.length; colIndex++) {
            WebElement colText = browserDriver.findElement(
                    By.xpath("//ul[contains(@class,'Grid')]//li[" + row
                            + "]//div[1]//div[" + colIndex + "]//input[1]"));
            colText.sendKeys(text[colIndex - 1]);
        }
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
        WebElement freeNode = browserDriver.findElement(By.linkText(tempType));
        CSUtility.waitForVisibilityOfElement(waitForReload, freeNode);
        freeNode.click();
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
     * @param templateType String object contains template type
     */
    private void createNewTemplate(String templateName, String templateType) {
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
        userAccess.selectByVisibleText(templateType);
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
     * This data provider returns the sheet data to run the test case which
     * contains ClassName,FlexTableProductName,TemplateName,TemplateType, Table
     * Caption,TableHeader,Row1,Row2,Row3
     * 
     * @return createFreeTemplateTestData
     */
    @DataProvider(name = "CreateFreeTemplateDataSheet")
    public Object[] FreeTemplate() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                createFreeTemplateTestData);
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
