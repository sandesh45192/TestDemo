/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IConfigPortalPopup;
import org.cs.csautomation.cs.settings.translationmanager.TranslationManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivConfigPortal;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
 * This class translates simple selction field and verifies outdated state,
 * request confirmation state, confirm state , up to date state and verification
 * of translations
 * 
 * @author CSAutomation Team
 *
 */
public class TranslatingSimpleSelectionFieldTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      translatingSimpleSelectionFieldSheet = "TranslatingSimpleSelectionField";
    private IConfigPortalPopup          configPortalPopup;
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private PimStudioProductsNodePage   pimStudioProductsNode;
    private Actions                     actions;
    private ModalDialogPopupWindow      modalDialogPopupWindow;
    private PimStudioSettingsNode       pimStudioSettingsNodeInstance;
    private IAttributePopup             attributePopup;
    private CSGuiDialogContentId        csGuiDialogContentIdInstance;
    private CSGuiDialogContentId        csGuiDialogContentId;
    private IProductPopup               productPopUp;
    private String                      editValue;
    private String[]                    setValues;

    /**
     * This test method translates the simple selection field
     * 
     * @param productName string contains name of product
     * @param attributeName string contains name of attribute
     * @param attributeType contains attribute type
     * @param selectType string contains select type of attribute
     * @param attributeValues string contains values of attribute
     * @param translationLabel string contains translation label
     * @param sourceLang contains string name of source language
     * @param targetLang contains string name of target language
     * @param dataCollectionField string contains name of data collection field
     * @param editedValueOfAttribute contains string edited value of attribute
     * @param translationTargetValueForSecondValue contains translation target
     *            value fo second value of attribute
     * @param translationTargetValueForThirdValue contains string translation
     *            target value fo third value of attribute
     * @param translationTargetValueForFourthValue contains string translation
     *            target value fo fourth value of attribute
     * @param valueOfTargetLanguage
     * @param valueOfSourceLanguage contains string value of source language
     */
    @Test(priority = 1, dataProvider = "translatingSimpleSelectionField")
    public void testTranslatingSimpleSelectionField(String productName,
            String attributeName, String attributeType, String selectType,
            String attributeValues, String translationLabel, String sourceLang,
            String targetLang, String dataCollectionField,
            String editedValueOfAttribute,
            String translationTargetValueForSecondValue,
            String translationTargetValueForThirdValue,
            String translationTargetValueForFourthValue,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            executePrerequisites(productName, attributeName, attributeType,
                    selectType, attributeValues);
            performUseCase(translationLabel, sourceLang, targetLang,
                    dataCollectionField, attributeName, attributeValues);
        } catch (Exception e) {
            CSLogger.debug(
                    "Test case failed for translation simple selection field",
                    e);
            Assert.fail(
                    "Test case failed for translation simple selection field",
                    e);
        }
    }

    /**
     * This method returns the attribute values
     * 
     * @param attributeValues contains attribute values
     * @return attributeValue
     */
    private String[] getAttributeValues(String attributeValues) {
        String[] attributeValue = attributeValues.split(",");
        return attributeValue;
    }

    /**
     * This method performs the use case of importing attribute values
     * 
     * @param translationJob contains name of translation job
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param dataCollectionField contains data collection field name
     * @param attributeName contains attribute name
     * @param attributeValues contains attribute values
     */
    private void performUseCase(String translationJob, String sourceLang,
            String targetLang, String dataCollectionField, String attributeName,
            String attributeValue) {
        String[] attributeValues = getAttributeValues(attributeValue);
        String value1 = attributeValues[0];
        String value2 = attributeValues[1];
        String value3 = attributeValues[2];
        String value4 = attributeValues[3];
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        createTranslationJob(translationJob);
        dataCollectionOperations(dataCollectionField, sourceLang, targetLang,
                attributeName);
        translationsTabOperations(attributeValues, value1, value2, value3,
                value4);
    }

    /**
     * This method performs the translation tab operations
     * 
     * @param attributeValue contains string array attribute values
     * @param value1 contains name of first value as string
     * @param value2 contains name of second value as string
     * @param value3 contains name of third value as string
     * @param value4 contains name of fourth value as string
     */
    private void translationsTabOperations(String[] attributeValue,
            String value1, String value2, String value3, String value4) {
        translatorManagerPage.clkTranslationsTab(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        verifyAttributeEntryTranslationsTab(attributeValue, value1, value2,
                value3, value4);
    }

    /**
     * This method verifies attribute entries in translations tab
     * 
     * @param attributeValue contains attribute values
     * @param value1 contains name of first value as string
     * @param value2 contains name of second value as string
     * @param value3 contains name of third value as string
     * @param value4 contains name of fourth value as string
     */
    private void verifyAttributeEntryTranslationsTab(String[] attributeValue,
            String value1, String value2, String value3, String value4) {
        int count = 0;
        List<WebElement> list = getValuesOfAttribute();
        String[] entry = new String[list.size()];
        for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
            WebElement valueName = list.get(valueIndex);
            entry[valueIndex] = valueName.getText();
        }
        count = checkEntry(count, entry, value1);
        count = checkEntry(count, entry, value2);
        count = checkEntry(count, entry, value3);
        count = checkEntry(count, entry, value4);
        if (count == attributeValue.length) {
            CSLogger.info("Values of  attributes are present");
        } else {
            CSLogger.error("Values of attributes  are not present");
        }
    }

    /**
     * This method checks which value of value list in the translations tab
     * after importing is present
     * 
     * @param count contains count of entries
     * @param entry contains values of value list as array
     * @param value contains value to be checked in the translations tab
     * @return count
     */
    private int checkEntry(int count, String[] entry, String value) {
        boolean status = false;
        for (int entryIndex = 0; entryIndex < entry.length; entryIndex++) {
            if (entry[entryIndex].equals(value)) {
                count++;
                status = true;
                break;
            } else {
                status = false;
            }
        }
        if (status == true) {
            CSLogger.info(value + " is present");
        } else {
            CSLogger.error(value + " is not present");
        }
        return count;
    }

    /**
     * This method gets values of value list from the translations tab
     * 
     * @return list
     */
    private List<WebElement> getValuesOfAttribute() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        CSUtility.tempMethodForThreadSleep(2000);
        List<WebElement> list = getAddedValuesTransltionsTab();
        return list;
    }

    /**
     * Returns list of values from translations tab
     * 
     * @return list of values
     */
    private List<WebElement> getAddedValuesTransltionsTab() {
        List<WebElement> list = null;
        try {
            list = browserDriver.findElements(By.xpath(
                    "//table[@class='hidewrap CSAdminList']/tbody/tr/td[6]"));
        } catch (Exception e) {
            softAssert.fail("Could not find added values");
        }
        return list;
    }

    /**
     * This method performs data collection operations
     * 
     * @param dataCollectionField contains name of data collection field
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param attributeName contains attribute language
     */
    private void dataCollectionOperations(String dataCollectionField,
            String sourceLang, String targetLang, String attributeName) {
        translatorManagerPage.clkDataCollectionTab(waitForReload);
        Select drpdwnDataCollection = new Select(browserDriver.findElement(By
                .xpath("//select[contains(@id,'TranslationjobCollectorPlugin')]")));
        drpdwnDataCollection.selectByVisibleText(dataCollectionField);
        CSUtility.tempMethodForThreadSleep(2000);
        enableCheckboxUseAsOriginalValue();
        selectElement(attributeName,
                translatorManagerPage.getTxtTabDataCollection());
        selectSourceLanguage(sourceLang);
        selectTargetLanguageForTranslationJob(targetLang);
        clkSave();
    }

    /**
     * This method enables checkbox use as original value in data collection
     * field
     */
    private void enableCheckboxUseAsOriginalValue() {
        waitForReload.until(ExpectedConditions.visibilityOf(
                translatorManagerPage.getChkboxUseOriginalValueAsSourceLang()));
        String chkboxValue = translatorManagerPage
                .getChkboxUseOriginalValueAsSourceLang().getAttribute("class");
        if (chkboxValue.equals(
                "CSGuiEditorCheckboxContainer Enabled GuiEditorCheckbox Off")) {

            CSLogger.info("Checkbox is already enabled");
        } else {
            translatorManagerPage.getChkboxUseOriginalValueAsSourceLang()
                    .click();
            CSLogger.info("Checkbox has enabled.");
        }
    }

    /**
     * This method creates translation job
     * 
     * @param translationJob contains name of translation job
     */
    private void createTranslationJob(String translationJob) {
        clkTranslationJob(translationJob);
        CSUtility.tempMethodForThreadSleep(2000);
        clkCreateNew();
        setLabel(translationJob);
    }

    /**
     * This method selects source language
     * 
     * @param sourceLangInTranlationJob contains source language in translation
     *            tab
     */
    private void selectSourceLanguage(String sourceLangInTranlationJob) {
        Select drpdwnSourceLang = new Select(browserDriver.findElement(By.xpath(
                "//select[contains(@id,'TranslationjobSourceLangID')]")));
        drpdwnSourceLang.selectByVisibleText(sourceLangInTranlationJob);
    }

    /**
     * This method selects target language for translation job
     * 
     * @param targetLangInTranlationJob contains target language in translation
     *            job
     */
    private void selectTargetLanguageForTranslationJob(
            String targetLangInTranlationJob) {
        selectElement(targetLangInTranlationJob, translatorManagerPage
                .getTxtTranslationJobAvailableTargetLanguage());
    }

    /**
     * This method selects element from drop down and from text area
     * 
     * @param objectToBeTranslated contains object to be translated
     * @param element contains web element to be selected
     */
    private void selectElement(String value, WebElement element) {
        try {
            boolean status = false;
            WebElement selectOption = null;
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            List<WebElement> list = element.findElements(By.tagName("option"));
            for (WebElement option : list) {
                if (option.getText().contains(value)) {
                    selectOption = option;
                    status = true;
                    break;
                }
            }
            if (status == true) {
                actions.doubleClick(selectOption).build().perform();
                CSLogger.info("Double clicked on object");
            } else {
                CSLogger.error("Failed to double click ");
            }
        } catch (Exception e) {
            CSLogger.error("Could not select and double click the object");
        }
    }

    /**
     * This method enables the check box of use original value as source
     * language
     */
    private void enableCheckbox(WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        String StringChkboxAttribute = element.getAttribute("class");
        if (StringChkboxAttribute.equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox")) {
            element.click();
            CSLogger.info("Checkbox enabled");
            CSUtility.tempMethodForThreadSleep(1000);
            clkSave();
        } else {
            CSLogger.info("Already enabled");
        }
    }

    /**
     * This method clicks on create new option from mid pane
     */
    private void clkCreateNew() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                csGuiToolbarHorizontal.getBtnCSGuiToolbarCreateNew()));
        csGuiToolbarHorizontal.getBtnCSGuiToolbarCreateNew().click();
        CSLogger.info("Clicked on create new");
    }

    /**
     * This method sets label in the properties section
     * 
     * @param translationJob contains name of translation job
     */
    private void setLabel(String translationJob) {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkPropertiesTab(waitForReload);
        waitForReload.until(ExpectedConditions.visibilityOf(
                translatorManagerPage.getTxtTranslationJobLabel()));
        translatorManagerPage.getTxtTranslationJobLabel().clear();
        translatorManagerPage.getTxtTranslationJobLabel()
                .sendKeys(translationJob);
    }

    /**
     * This method creates translation job
     * 
     * @param translationJob contains translation job name
     */
    private void clkTranslationJob(String translationJob) {
        clkTranslationManager();
        TraversingForSettingsModule.traverseToNodesInLeftPaneTranslationManager(
                waitForReload, browserDriver);
        translatorManagerPage.clkTranslationJobNode(waitForReload);
    }

    /**
     * This method clicks on translation manager
     */
    private void clkTranslationManager() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnTranslationManagerIcon(waitForReload);
    }

    /**
     * This method executes prerequisites
     * 
     * @param productName contains name of product
     * @param attributeName contains name of attribute
     * @param attributeType contains type of attribute
     * @param selectType contains select type
     * @param attributeValues contains attribute values
     */
    private void executePrerequisites(String productName, String attributeName,
            String attributeType, String selectType, String attributeValues) {
        createNewAttribute(attributeName, false);
        createNewAttribute(attributeName, true);
        verifyCreatedAttribute(attributeName);
        configureAttributeProperties(attributeName, attributeType, selectType,
                attributeValues);
        createProduct(productName);
        dragDropAttributeToProduct(productName, attributeName);
        verifyAssignedValuesToProduct(productName, attributeValues,
                attributeName);
    }

    /**
     * This method verifies assigned values to the product
     * 
     * @param productName contains name of product
     * @param attributeValues contains attribute values
     * @param attributeName contains name of attribute
     */
    private void verifyAssignedValuesToProduct(String productName,
            String attributeValues, String attributeName) {
        String[] attributeValue = getAttributeValues(attributeValues);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        List<WebElement> list = getListOfAssignedValues(productName,
                attributeName);
        List<String> valueListString = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            valueListString.add(list.get(i).getText());
        }
        List<String> attributeList = Arrays.asList(attributeValue);
        boolean status = valueListString.containsAll(attributeList);
        if (status == true) {
            CSLogger.info("Values are present");
        } else {
            CSLogger.error("Values are not present");
        }
    }

    /**
     * This method gets the list of assigned values from data pane of product
     * 
     * @param productName contains name of product
     * @param attributeName contains attribute name
     * @return
     */
    private List<WebElement> getListOfAssignedValues(String productName,
            String attributeName) {
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement createdProduct = getElement(productName);
        actions.doubleClick(createdProduct).click().build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        csGuiDialogContentId.clkBtnData(waitForReload);
        WebElement valuesAvailable = browserDriver.findElement(
                By.xpath("(//tr[contains(@cs_name,'" + attributeName
                        + "')]/td[2]/table/tbody/tr[2]/td/select)[1]"));
        waitForReload.until(ExpectedConditions.visibilityOf(valuesAvailable));
        List<WebElement> list = valuesAvailable
                .findElements(By.tagName("option"));
        return list;
    }

    /**
     * This method performs drag and drop attribute to the product
     * 
     * @param productName contains name of product
     * @param attributeName contains name of attribute
     */
    private void dragDropAttributeToProduct(String productName,
            String attributeName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
        WebElement createdAttribute = getElement(attributeName);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement createdProduct = getElement(productName);
        CSUtility.tempMethodForThreadSleep(3000);
        actions.dragAndDrop(createdAttribute, createdProduct).click().build()
                .perform();
        CSLogger.info("Successfully dragged and droppped attribute to class");
        performClick("Extend");
    }

    /**
     * This method performs click on either extend or cancel or replace button
     * after drag and drop Cancel will cancel the functionality extend inherits
     * class to already existing classes in products Replace replaces already
     * existing classes with new one.
     * 
     * @param waitForReload contains time in ms to wait on any element if needed
     * @param selectButton contains button names Cancel ,Extend and Replace
     */
    public void performClick(String selectButton) {
        IProductPopup productPopUp = new CSPopupDivPim(browserDriver);
        browserDriver.switchTo().defaultContent();
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        if (selectButton.equals("Cancel")) {
            waitForReload.until(ExpectedConditions.visibilityOf(
                    productPopUp.getBtnCsGuiModalDialogCancelButton()));
            productPopUp.getBtnCsGuiModalDialogCancelButton();
        } else if (selectButton.equals("Extend")) {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDialogContentId.getBtnExtend()));
            csGuiDialogContentId.getBtnExtend().click();
        } else {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDialogContentId.getBtnReplace()));
            csGuiDialogContentId.getBtnReplace().click();
        }
        CSLogger.info(
                "Performed click after dragging the class to the product");
    }

    /**
     * This method returns the weblelement
     * 
     * @param name contains name of string
     * @return element
     */
    private WebElement getElement(String name) {
        WebElement element = browserDriver.findElement(By.linkText(name));
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    /**
     * This method creates product
     * 
     * @param productName contains name of product
     */
    private void createProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopUp.enterValueInDialogue(waitForReload, productName);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * Verifies created attribute
     * 
     * @param attributeName contains name of attribute
     */
    private void verifyCreatedAttribute(String attributeName) {
        getAttributeNode();
        WebElement createdAttribute = getCreatedAttribute(attributeName);
        Assert.assertEquals(attributeName, createdAttribute.getText());
        CSLogger.info("Attribute created successfully");
    }

    /**
     * Returns created attribute
     * 
     * @param attributeName name of attribute
     * @return createdAttribute as webelement
     */
    private WebElement getCreatedAttribute(String attributeName) {
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        return createdAttribute;
    }

    /**
     * This method creates new attribute
     * 
     * @param attributeName contains attribute name
     * @param status contais boolean value
     */
    private void createNewAttribute(String attributeName, boolean status) {
        getAttributeNode();
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode(),
                browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        attributePopup.enterValueInUserInputDialogue(waitForReload,
                attributeName);
        attributePopup.askBoxWindowOperation(waitForReload, status,
                browserDriver);
    }

    /**
     * This method clicks on attribute node
     */
    private void getAttributeNode() {
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        clkOnAttributesNode();
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
     * This method configures attribute properties
     * 
     * @param attributeName contains name of attribute
     * @param attributeType contains type of attribute
     * @param selectType contains select type
     * @param attributeValues contains attribute values
     */
    private void configureAttributeProperties(String attributeName,
            String attributeType, String selectType, String attributeValues) {
        String[] values = attributeValues.split(",");
        clkCreatedAttribute(attributeName);
        selectAttributeType(attributeType, selectType);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        enableCheckbox(csGuiDialogContentIdInstance
                .getCbPdmarticleconfigurationLanguageDependent());
        enableCheckbox(csGuiDialogContentId.getcbmultipleSelection());
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentId.getTxtValues()));
        csGuiDialogContentId.getTxtValues().clear();
        for (int attributeValueIndex = 0; attributeValueIndex < values.length; attributeValueIndex++) {
            csGuiDialogContentId.getTxtValues()
                    .sendKeys(values[attributeValueIndex] + "\n");
        }
        setValues = values;
        clkSave();
    }

    /**
     * This method selects attribute type
     * 
     * @param attributeType contains attribute type
     * @param selectType contains select type
     */
    private void selectAttributeType(String attributeType, String selectType) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentIdInstance.clkBtnCSTypeLabel(waitForReload);
        traverseToLeftFrame();
        WebElement drpdwnSelection = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + selectType + "')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(drpdwnSelection));
        drpdwnSelection.click();
        CSLogger.info(
                "clicked on Selection drop down to select attribute type");
        WebElement setType = browserDriver.findElement(
                By.xpath("//a[contains(text(),'" + attributeType + "')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(setType));
        setType.click();
        CSLogger.info("Clicked on attribute type");
        modalDialogPopupWindow.handlePopupDataSelectionDialog(waitForReload,
                true);
        CSUtility.getAlertBox(waitForReload, browserDriver).accept();
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method traverses up to left frame for selecting attribute type
     */
    private void traverseToLeftFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowWidget()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmLeftAttributeTypeWindow()));
        CSLogger.info("Traversed upto left frame");
    }

    /**
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This test method verifies outdated state
     * 
     * @param productName contains name of product
     * @param attributeName contains name of attribute
     * @param attributeType contains attribute type
     * @param selectType contains select type of attribute
     * @param attributeValues contains values of attribute
     * @param translationLabel contains translation label
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param dataCollectionField contains name of data collection field
     * @param editedValueOfAttribute contains edited value of attribute
     * @param translationTargetValueForSecondValue contains translation target
     *            value fo second value of attribute
     * @param translationTargetValueForThirdValue contains translation target
     *            value fo third value of attribute
     * @param translationTargetValueForFourthValue contains translation target
     *            value fo fourth value of attribute
     * @param valueOfTargetLanguage
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 2,
            dependsOnMethods = { "testTranslatingSimpleSelectionField" },
            dataProvider = "translatingSimpleSelectionField")
    public void testVerifyOutdatedState(String productName,
            String attributeName, String attributeType, String selectType,
            String attributeValues, String translationLabel, String sourceLang,
            String targetLang, String dataCollectionField,
            String editedValueOfAttribute,
            String translationTargetValueForSecondValue,
            String translationTargetValueForThirdValue,
            String translationTargetValueForFourthValue,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            String[] attributeValue = getAttributeValues(attributeValues);
            csPortalHeader.clkBtnProducts(waitForReload);
            clkCreatedAttribute(attributeName);
            editValueOfAttribute(attributeName, attributeValue[0],
                    editedValueOfAttribute);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToTmsStudio(editedValueOfAttribute, attributeValue[0],
                    translationLabel);
        } catch (Exception e) {
            CSLogger.error("Test case failed", e);
            Assert.fail("Test case failed" + e);
        }
    }

    /**
     * This method traverses to TMS Studio
     * 
     * @param editedValueOfAttribute contains edited value of attribute
     * @param attributeValue contains value of attribute
     * @param translationLabel contains translation label
     */
    private void goToTmsStudio(String editedValueOfAttribute,
            String attributeValue, String translationLabel) {
        try {
            clkTranslationManager();
            traversingForSettingsModule.traverseToMainFrameTranslationManager(
                    waitForReload, browserDriver);
            TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                    browserDriver);
            CSUtility.tempMethodForThreadSleep(2000);
            translatorManagerPage.clkTranslationsTab(waitForReload);
            CSUtility.tempMethodForThreadSleep(2000);
            translatorManagerPage.clkBtnImport(waitForReload);
            CSUtility.tempMethodForThreadSleep(3000);
            clkCreatedValue(attributeValue);
            verifyStateChange(
                    translatorManagerPage
                            .getBtnVerifyOutdatedEntryTranslationTab(),
                    "cancel.svg");
            availabilityOfTranslationTextAreaToEdit();
        } catch (Exception e) {
            CSLogger.error("Could not verify outdated state", e);
            Assert.fail("Could not find state", e);
        }
    }

    /**
     * This method clicks on created translation label
     * 
     * @param translationLabel contains name of translation label
     */
    private void clkCreatedTranslatedLabel(String translationLabel) {
        boolean status = false;
        WebElement getLabel = null;
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        for (int labelIndex = 0; labelIndex < list.size(); labelIndex++) {
            getLabel = list.get(labelIndex);
            if (getLabel.getText().equals(translationLabel)) {
                status = true;
                break;
            } else
                status = false;
        }
        if (status == true) {
            getLabel.click();
            CSLogger.info("Clicked on label");
        } else {
            CSLogger.error("Unable to click on created translation label");
        }
    }

    /**
     * This method verifies state change
     * 
     * @param webElement contains web element
     * @param state contains state to be verified.
     */
    private void verifyStateChange(WebElement webElement, String state) {
        traverseToTranslationsTabEntries();
        verifyStatusOfState(webElement, state);
    }

    /**
     * This method checks availability of translation text area to edit
     */
    private void availabilityOfTranslationTextAreaToEdit() {
        try {
            traverseToTranslationTextArea();
            WebElement txtTranslation = getTranslationTextArea();
            if ((txtTranslation.getText()) != null) {
                CSLogger.error(
                        "Test case verification failed for outdated state");
            }
        } catch (Exception e) {
            CSLogger.info("Test case verifed for outdated state of product");
            softAssert.fail(
                    "Failed to check the availability of translation text area",
                    e);
        }
    }

    /**
     * This method gets translation text area instance
     * 
     * @return txtTranslation
     */
    private WebElement getTranslationTextArea() {
        WebElement txtTranslation = browserDriver.findElement(By.xpath(
                "//textarea[contains(@id,'TranslationmemoryTargetValue')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(txtTranslation));
        return txtTranslation;
    }

    /**
     * This method checks traverses to translation text area
     */
    private void traverseToTranslationTextArea() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
    }

    /**
     * This method traverses to translation tab entries
     */
    public void traverseToTranslationsTabEntries() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method verifies status of state
     * 
     * @param element contains web element
     * @param state this method contains state name
     */
    private void verifyStatusOfState(WebElement element, String state) {
        try {
            boolean status = false;
            List<WebElement> list = getStateTranslationsTab();
            for (int stateIndex = 0; stateIndex < list.size(); stateIndex++) {
                String chkState = getState(element);
                if (chkState.contains(state)) {
                    status = true;
                    break;
                } else {
                    status = false;
                }
            }
            if (status == true) {
                CSLogger.info(
                        " Translation state has changed in Translation tab  "
                                + state.replace(".svg", ""));
            } else {
                CSLogger.info("Could not change translation state ");
                Assert.fail("Failed to verify request  state");
            }
        } catch (Exception e) {
            Assert.fail("Outdated state does not exist");
        }
    }

    /**
     * This method returns the list of states in translations tab
     * 
     * @return list
     */
    private List<WebElement> getStateTranslationsTab() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[contains(@class,'hidewrap CSAdminList')]/tbody/tr/td[3]"));
        return list;
    }

    /**
     * This method edits the value of attribute
     * 
     * @param attributeName contains attribute name
     * @param attributeValue contains attribute value
     * @param editedValueOfAttribute contains edited value of attribute
     */
    private void editValueOfAttribute(String attributeName,
            String attributeValue, String editedValueOfAttribute) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement createdValues = waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentId.getTxtValues()));
        waitForReload.until(ExpectedConditions.visibilityOf(createdValues));
        createdValues.click();
        String values = createdValues.getText();
        if (values.contains(attributeValue)) {
            editValue = values.replace(attributeValue, editedValueOfAttribute);
            CSUtility.tempMethodForThreadSleep(1000);
            csGuiDialogContentId.getTxtValues().clear();
            csGuiDialogContentId.getTxtValues().sendKeys(editValue);
            CSUtility.tempMethodForThreadSleep(1000);
            clkSave();
            CSUtility.tempMethodForThreadSleep(2000);
        } else {
            System.out.println("Could not find required value in the textarea");
        }
    }

    /**
     * This test method verifies request confirmation state
     * 
     * @param productName contains name of product
     * @param attributeName contains name of attribute
     * @param attributeType contains attribute type
     * @param selectType contains select type of attribute
     * @param attributeValues contains values of attribute
     * @param translationLabel contains translation label
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param dataCollectionField contains name of data collection field
     * @param editedValueOfAttribute contains edited value of attribute
     * @param translationTargetValueForSecondValue contains translation target
     *            value fo second value of attribute
     * @param translationTargetValueForThirdValue contains translation target
     *            value fo third value of attribute
     * @param translationTargetValueForFourthValue contains translation target
     *            value fo fourth value of attribute
     * @param valueOfTargetLanguage
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 3,
            dependsOnMethods = { "testVerifyOutdatedState" },
            dataProvider = "translatingSimpleSelectionField")
    public void testVerifyRequestConfirmationState(String productName,
            String attributeName, String attributeType, String selectType,
            String attributeValues, String translationLabel, String sourceLang,
            String targetLang, String dataCollectionField,
            String editedValueOfAttribute,
            String translationTargetValueForSecondValue,
            String translaionTargetValueForThirdValue,
            String translationTargetValueForFourthValue,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            translatorManagerPage.clkBtnSaveAndLoadNextElement(waitForReload);
            setTranslationText(translationTargetValueForSecondValue);
            translatorManagerPage.clkBtnRequestConfirmation(waitForReload);
            verifyStateChange(
                    translatorManagerPage
                            .getBtnVerifyRequestConfirmationTranslationsTab(),
                    "question.svg");
        } catch (Exception e) {
            CSLogger.debug("Verification of request confirmation test failed",
                    e);
            Assert.fail("Verification of request confirmation test failed", e);
        }
    }

    /**
     * This method clicks created attribute
     * 
     * @param attributeName contains attribute name
     */
    private void clkCreatedAttribute(String attributeName) {
        getAttributeNode();
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        waitForReload.until(ExpectedConditions.visibilityOf(createdAttribute));
        actions.doubleClick(createdAttribute).build().perform();
        CSLogger.info(
                "Double clicked on created attribute under Attributes node");
    }

    /**
     * This method gets state for verification
     * 
     * @param element contains web element
     * @return chkState
     */
    private String getState(WebElement imgSource) {
        String chkState = imgSource.getAttribute("src");
        return chkState;
    }

    /**
     * This method clicks on created attribute value
     * 
     * @param attributeValue contains value of attribute
     */
    private void clkCreatedValue(String attributeValue) {
        try {
            boolean status = false;
            WebElement value = null;
            traversingForSettingsModule.traverseToMainFrameTranslationManager(
                    waitForReload, browserDriver);
            TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                    browserDriver);
            List<WebElement> list = getValuesOfAttribute();
            for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
                value = list.get(valueIndex);
                if (value.getText().equals(attributeValue)) {
                    status = true;
                    break;
                } else {
                    status = false;
                }
            }
            if (status == true) {
                value.click();
                CSUtility.tempMethodForThreadSleep(3000);
                CSLogger.info("Clicked on created value");
            }
        } catch (Exception e) {
            Assert.fail("Attribute is not present to check outdated state");
        }
    }

    /**
     * This test method verifies confirm state
     * 
     * @param productName contains name of product
     * @param attributeName contains name of attribute
     * @param attributeType contains attribute type
     * @param selectType contains select type of attribute
     * @param attributeValues contains values of attribute
     * @param translationLabel contains translation label
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param dataCollectionField contains name of data collection field
     * @param editedValueOfAttribute contains edited value of attribute
     * @param translationTargetValueForSecondValue contains translation target
     *            value fo second value of attribute
     * @param translationTargetValueForThirdValue contains translation target
     *            value fo third value of attribute
     * @param translationTargetValueForFourthValue contains translation target
     *            value fo fourth value of attribute
     * @param valueOfTargetLanguage
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 4,
            dependsOnMethods = { "testVerifyRequestConfirmationState" },
            dataProvider = "translatingSimpleSelectionField")
    public void testVerifyConfirmState(String productName, String attributeName,
            String attributeType, String selectType, String attributeValues,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String editedValueOfAttribute,
            String translationTargetValueForSecondValue,
            String translationTargetValueForThirdValue,
            String translationTargetValueForFourthValue,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            setTranslationText(translationTargetValueForThirdValue);
            translatorManagerPage.clkBtnConfirm(waitForReload);
            verifyStateChange(
                    translatorManagerPage.getBtnVerifyConfirmTranslationsTab(),
                    "check.svg");
        } catch (Exception e) {
            CSLogger.debug("Verification of confirmation test failed", e);
            Assert.fail("Verification of confirmation test failed", e);
        }
    }

    /**
     * This method sets translation text
     * 
     * @param translationTargetValue contains translation text
     */
    private void setTranslationText(String translationTargetValue) {
        WebElement txtTranslation = getTranslationTextArea();
        txtTranslation.sendKeys(translationTargetValue);
    }

    /**
     * This test method verifies up to date state
     * 
     * @param productName contains name of product
     * @param attributeName contains name of attribute
     * @param attributeType contains attribute type
     * @param selectType contains select type of attribute
     * @param attributeValues contains values of attribute
     * @param translationLabel contains translation label
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param dataCollectionField contains name of data collection field
     * @param editedValueOfAttribute contains edited value of attribute
     * @param translationTargetValueForSecondValue contains translation target
     *            value fo second value of attribute
     * @param translationTargetValueForThirdValue contains translation target
     *            value fo third value of attribute
     * @param translationTargetValueForFourthValue contains translation target
     *            value fo fourth value of attribute
     * @param valueOfTargetLanguage
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 5,
            dependsOnMethods = { "testVerifyConfirmState" },
            dataProvider = "translatingSimpleSelectionField")
    public void testVerifyUpToDateState(String productName,
            String attributeName, String attributeType, String selectType,
            String attributeValues, String translationLabel, String sourceLang,
            String targetLang, String dataCollectionField,
            String editedValueOfAttribute,
            String translationTargetValueForSecondValue,
            String translationTargetValueForThirdValue,
            String translationTargetValueForFourthValue,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            setTranslationText(translationTargetValueForFourthValue);
            translatorManagerPage
                    .clkBtnImportFromTranslationMemory(waitForReload);
            verifyStateChange(
                    translatorManagerPage
                            .getBtnVerifyImportFromTranslationMemory(),
                    "up.svg");
            verifyTranslationOfValue(valueOfTargetLanguage,
                    translationTargetValueForFourthValue, attributeName,
                    productName);
            changeInterfaceLanguage(valueOfSourceLanguage);
            clkBtnImportFromTranslationMemoryTaskbar(translationLabel);
        } catch (Exception e) {
            CSLogger.debug("Verification of up to date test failed", e);
            Assert.fail("Verification of up to date test failed", e);
        }
    }

    /**
     * This method clicks import button from translation memory task bar
     * 
     * @param translationLabel contains name of translation label
     */
    private void
            clkBtnImportFromTranslationMemoryTaskbar(String translationLabel) {
        int count = 0;
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        clkTranslationManager();
        clkTranslationJob(translationLabel);
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        enterDataInFilterBar(translationLabel);
        CSUtility.tempMethodForThreadSleep(3000);
        disableFilterBar();
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkTranslationsTab(waitForReload);
        translatorManagerPage.clkBtnImportFromTranslationMemory(waitForReload);
        traverseToTranslationsTabEntries();
        List<WebElement> list = getStateTranslationsTab();
        for (int stateIndex = 0; stateIndex < list.size(); stateIndex++) {
            String chkState = getState(translatorManagerPage
                    .getBtnVerifyImportFromTranslationMemory());
            if (chkState.contains("check.svg")) {
                count++;
            }
        }
        if (count > 1) {
            CSLogger.info("Confirmed entry states have been imported");
        }
    }

    /**
     * Enters the data in filter bar.
     *
     * @param waitForReload WebDriverWait object.
     * @param data String object contains data to be entered in filter bar.
     */
    public void enterDataInFilterBar(String data) {
        CSUtility.tempMethodForThreadSleep(1000);
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//img[contains(@src,'nofilter.gif')]/.."))
                .size();
        if (chkPresenceOfNoFilter == 0) {
            int chkPresenceOfTextbox = browserDriver
                    .findElements(By.xpath("//input[@id='Filter_Label']"))
                    .size();
            if (chkPresenceOfTextbox != 0) {
                CSUtility.waitForVisibilityOfElement(waitForReload,
                        csGuiToolbarHorizontal.getBtnFilter());
                csGuiToolbarHorizontal.getBtnFilter().click();
                CSUtility.tempMethodForThreadSleep(1000);
                csGuiToolbarHorizontal.getTxtFilterBar().clear();
                csGuiToolbarHorizontal.getTxtFilterBar().sendKeys(data);
                CSLogger.info("Entered text  " + data + " in filter bar");
                csGuiToolbarHorizontal.getTxtFilterBar().sendKeys(Keys.ENTER);
            }
        }
        performClickOnTranslationLabelMidPane(data);
    }

    /**
     * Performs click on the translation label from the mid pane
     * 
     * @param data contains string data
     */
    private void performClickOnTranslationLabelMidPane(String data) {
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        for (Iterator<WebElement> iterator = listLabels.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(data)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSUtility.tempMethodForThreadSleep(4000);
            CSUtility.waitForVisibilityOfElement(waitForReload, listElement);
            listElement.click();
            CSLogger.info("clicked on translation label in the mid pane");
        } else {
            CSLogger.error(
                    "Could not find the translation label name in the mid pane");
        }
    }

    /**
     * Disables filter bar
     * 
     */
    private void disableFilterBar() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        int chkPresenceOfNoFilter = browserDriver
                .findElements(
                        By.xpath("//a/img[contains(@src,'nofilter.gif')]"))
                .size();
        if (chkPresenceOfNoFilter != 0) {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontal.getbtnNoFilter());
            csGuiToolbarHorizontal.getbtnNoFilter().click();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontal.getBtnFilter());
            csGuiToolbarHorizontal.getBtnFilter().click();
            CSUtility.tempMethodForThreadSleep(1000);
        }
    }

    /**
     * This method verifies the translation of value
     * 
     * @param valueOfTargetLanguage contains value of target language
     * @param translationTargetValueForFourthValue contains translation target
     *            value
     */
    private void verifyTranslationOfValue(String valueOfTargetLanguage,
            String translationTargetValueForFourthValue, String attributeName,
            String productName) {
        changeInterfaceLanguage(valueOfTargetLanguage);
        checkValueChanges(translationTargetValueForFourthValue, attributeName,
                productName);
    }

    /**
     * This method checks if value has translated to target language
     * 
     * @param translationTargetValueForFourthValue contains target value
     * @param attributeName contains name of attribute
     * @param productName contains product name
     */
    private void checkValueChanges(String translationTargetValueForFourthValue,
            String attributeName, String productName) {
        boolean status = false;
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        List<WebElement> list = getListOfAssignedValues(productName,
                attributeName);
        for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
            WebElement translatedValue = list.get(valueIndex);
            if (translatedValue.getText()
                    .equals(translationTargetValueForFourthValue)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSLogger.info("Value has translated successfully");
        } else {
            CSLogger.error("Failed to translate value");
        }

    }

    /**
     * This method changes interface language
     * 
     * @param valueOfTargetLanguage contains value of target language
     */
    private void changeInterfaceLanguage(String language) {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        configPortalPopup.clkCtxLanguageSettings(waitForReload);
        selectLanguage(language);
    }

    /**
     * This method selects interface language
     * 
     * @param valueOfTargetLanguage contains value of target language
     */
    private void selectLanguage(String language) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowTranslationManager()));
        Select selectLanguage = new Select(browserDriver.findElement(
                By.xpath("//select[contains(@name,'InterfaceLanguage')]")));
        selectLanguage.selectByVisibleText(language);
        modalDialogPopupWindow.clkBtnCsGuiModalDialogOkButton(waitForReload);
        CSLogger.info("Language changed successfully  ");
    }

    /**
     * This test method verifies translations of all fields
     * 
     * @param productName contains name of product
     * @param attributeName contains name of attribute
     * @param attributeType contains attribute type
     * @param selectType contains select type of attribute
     * @param attributeValues contains values of attribute
     * @param translationLabel contains translation label
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param dataCollectionField contains name of data collection field
     * @param editedValueOfAttribute contains edited value of attribute
     * @param translationTargetValueForSecondValue contains translation target
     *            value fo second value of attribute
     * @param translationTargetValueForThirdValue contains translation target
     *            value fo third value of attribute
     * @param translationTargetValueForFourthValue contains translation target
     *            value fo fourth value of attribute
     * @param valueOfTargetLanguage
     * @param valueOfSourceLanguage contains value of source language
     */
    @Test(
            priority = 6,
            dependsOnMethods = { "testVerifyUpToDateState" },
            dataProvider = "translatingSimpleSelectionField")
    public void testVerificationsOfTranslations(String productName,
            String attributeName, String attributeType, String selectType,
            String attributeValues, String translationLabel, String sourceLang,
            String targetLang, String dataCollectionField,
            String editedValueOfAttribute,
            String translationTargetValueForSecondValue,
            String translationTargetValueForThirdValue,
            String translationTargetValueForFourthValue,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            changeInterfaceLanguage(valueOfTargetLanguage);
            csPortalHeader.clkBtnProducts(waitForReload);
            pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                    browserDriver);
            List<WebElement> webElementList = getListOfAssignedValues(
                    productName, attributeName);
            List<String> stringValue = new ArrayList<String>();
            for (WebElement value : webElementList) {
                stringValue.add(value.getText());
            }
            if ((stringValue.contains(translationTargetValueForThirdValue))
                    && (stringValue
                            .contains(translationTargetValueForFourthValue))) {
                CSLogger.info("Values have been translated successfully");
            } else {
                CSLogger.error("Failed to transfer values ");
            }
            changeInterfaceLanguage(valueOfSourceLanguage);
        } catch (Exception e) {
            CSLogger.debug("Test case failed for verificaton of translations",
                    e);
            Assert.fail("Test case failed for verifcaton of translations", e);
        }
    }

    /**
     * This data provider returns the sheet which contains attribute
     * name,attribute type,drop down to select attribute type,attribute values,
     * translation label ,source language ,target language , data collection
     * field ,edited value of attribute ,translation target value for second
     * value of attribute ,translation target value for third value of
     * attribute,translatio target value for fourth value of attribute , target
     * language value ,and source language value
     * 
     * @return translatingSimpleSelectionFieldSheet
     */
    @DataProvider(name = "translatingSimpleSelectionField")
    public Object[][] translatingProducts() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                translatingSimpleSelectionFieldSheet);
    }

    /**
     * This method is used to initialize all the resources used to drive the
     * test case
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        configPortalPopup = CSPopupDivConfigPortal
                .getCSPopupDivConfigPortalLocators(browserDriver);
        translatorManagerPage = new TranslationManagerPage(browserDriver);
        actions = new Actions(browserDriver);
        softAssert = new SoftAssert();
        csPortalWidget = new CSPortalWidget(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        modalDialogPopupWindow = new ModalDialogPopupWindow(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }
}
