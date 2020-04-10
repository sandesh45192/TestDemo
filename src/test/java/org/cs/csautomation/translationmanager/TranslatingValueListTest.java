/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IConfigPortalPopup;
import org.cs.csautomation.cs.settings.IValueListPopup;
import org.cs.csautomation.cs.settings.ValuelistManagementPage;
import org.cs.csautomation.cs.settings.translationmanager.TranslationManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivConfigPortal;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
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
 * This class translates value list and its values to the target language
 * ,verifes outdated state, verifies request confirmation state, verifies
 * confirmation state , verifies upto date state and verification of
 * translations of all the entries
 * 
 * @author CSAutomation Team
 *
 */
public class TranslatingValueListTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      translatingValueListSheet = "TranslatingValueList";
    private IConfigPortalPopup          configPortalPopup;
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private Actions                     actions;
    private ModalDialogPopupWindow      modalDialogPopupWindow;
    private ValuelistManagementPage     valueListNodeInstance;
    private PimStudioSettingsNode       pimStudioSettingsNode;
    private IValueListPopup             valueListPopup;
    private boolean                     listStatus;

    /**
     * This method translates performs the translation of value list and its
     * values to the target language
     * 
     * @param valueListName contains the name of the value list
     * @param valueListCategory contains value list category (type)
     * @param englishValuesForList contains english values of value list to be
     *            created
     * @param translationLabel contains name of translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains name of the data collection field
     * @param editedValueName contains edited value name of the value list
     * @param translationTargetValueForEditedValueOfValueList contains
     *            translation target value for edited value of value list
     * @param translationTargetValueForSecondValueOfValueList contains
     *            translation target value for second value of value list
     * @param translationTargetValueForThirdValueOfValueList contains
     *            translation target value for third value of value list
     * @param valueOfTargetLanguage contains the value of target language
     * @param valueOfSourceLanguage contains the value of source language
     */
    @Test(priority = 1, dataProvider = "translatingValueList")
    public void testTranslatingValueList(String valueListName,
            String valueListCategory, String englishValuesForList,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String editedValueName,
            String translationTargetValueForEditedValueOfValueList,
            String translationTargetValueForSecondValueOfValueList,
            String translationTargetValueForThirdValueOfValueList,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            String[] valuesForList = getValues(englishValuesForList);
            switchToPimAndExpandSettingsTree();
            executePrerequisites(valuesForList, valueListName,
                    valueListCategory, englishValuesForList);
            performUseCase(valueListName, translationLabel, sourceLang,
                    targetLang, dataCollectionField, valuesForList);
        } catch (Exception e) {
            CSLogger.debug("Translation of value list failed", e);
            Assert.fail("Translation of value list failed", e);
        }
    }

    /**
     * This method executes pre_requisites required to run the test case
     * 
     * @param valuesForList contains values for valuelist
     * @param valueListName contains value list name
     * @param valueListCategory contains value list category
     * @param englishValuesForList contains english values for value list
     */
    private void executePrerequisites(String[] valuesForList,
            String valueListName, String valueListCategory,
            String englishValuesForList) {
        createValueList(valueListName, valueListCategory);
        pimStudioSettingsNode.clkBtnPimSettingsNode(waitForReload);
        valueListNodeInstance.clkBtnValueList(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement category = getDetails(valueListCategory);
        waitForReload.until(ExpectedConditions.visibilityOf(category));
        category.click();
        clkCreatedValueList(valueListName, valueListCategory);
        for (int values = 0; values < valuesForList.length; values++) {
            clkCreateNew();
            enterDataInTextbox(valuesForList[values]);
        }
    }

    /**
     * This method gets values of value list as string array
     * 
     * @param englishValuesForList contains all values of value list
     * @return valuesForList
     */
    private String[] getValues(String englishValuesForList) {
        String[] valuesForList = englishValuesForList.split(",");
        return valuesForList;
    }

    /**
     * This method performs the use case of creating translation job and
     * respective operations to import all the values of value list in
     * translation tab
     * 
     * @param valueListName contains the name of the value list
     * @param translationLabel contains translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains name of data collection field
     * @param valuesForList contains values of value list
     */
    private void performUseCase(String valueListName, String translationLabel,
            String sourceLang, String targetLang, String dataCollectionField,
            String[] valuesForList) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        createTranslationJob(translationLabel);
        dataCollectionOperations(dataCollectionField, sourceLang, targetLang,
                valueListName);
        translationsTabOperations(valuesForList);
    }

    /**
     * This method performs the translation tab operations
     * 
     * @param valuesForList contains string array of values of value list
     */
    private void translationsTabOperations(String[] valuesForList) {
        String value1 = valuesForList[0];
        String value2 = valuesForList[1];
        String value3 = valuesForList[2];
        String value4 = valuesForList[3];
        translatorManagerPage.clkTranslationsTab(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        verifyValueListEntryTranslationsTab(valuesForList, value1, value2,
                value3, value4);
    }

    /**
     * This method verifies value list entries in the translation tab after
     * import
     * 
     * @param valuesForList contains string array containing values of value
     *            list
     * @param value1 contains first value from the value list
     * @param value2 contains second value from the value list
     * @param value3 contains third value from the value list
     * @param value4 contains fourth value from the value list
     */
    private void verifyValueListEntryTranslationsTab(String[] valuesForList,
            String value1, String value2, String value3, String value4) {
        int count = 0;
        List<WebElement> list = getValuesOfValueList();
        String[] entry = new String[list.size()];
        for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
            WebElement valueName = list.get(valueIndex);
            entry[valueIndex] = valueName.getText();
        }
        count = checkEntry(count, entry, value1);
        count = checkEntry(count, entry, value2);
        count = checkEntry(count, entry, value3);
        count = checkEntry(count, entry, value4);
        if (count == valuesForList.length) {
            CSLogger.info("Values of value list are present");
        } else {
            CSLogger.error("Values of value list are not present");
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
    private List<WebElement> getValuesOfValueList() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = getAddedValuesTransltionsTab();
        return list;
    }

    /**
     * Returns list of values from translations tab
     * 
     * @return list
     */
    private List<WebElement> getAddedValuesTransltionsTab() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[6]"));
        return list;
    }

    /**
     * This method performs the data collection operations from data collection
     * tab of translation job
     * 
     * @param dataCollectionField contains name of the data collection field
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param valueListName contains name of value list
     */
    private void dataCollectionOperations(String dataCollectionField,
            String sourceLang, String targetLang, String valueListName) {
        translatorManagerPage.clkDataCollectionTab(waitForReload);
        Select drpdwnDataCollection = new Select(browserDriver.findElement(By
                .xpath("//select[contains(@id,'TranslationjobCollectorPlugin')]")));
        drpdwnDataCollection.selectByVisibleText(dataCollectionField);
        CSUtility.tempMethodForThreadSleep(1000);
        selectElement(valueListName,
                translatorManagerPage.getTxtTabDataCollection());
        selectSourceLanguage(sourceLang);
        selectTargetLanguageForTranslationJob(targetLang);
        clkSave();
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
     * This method selects element from drop down and from text area
     * 
     * @param objectToBeTranslated contains object to be translated
     * @param element contains web element to be selected
     */
    private void selectElement(String valueListName, WebElement element) {
        try {
            boolean status = false;
            WebElement selectOption = null;
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            List<WebElement> list = element.findElements(By.tagName("option"));
            for (WebElement option : list) {
                if (option.getText().contains(valueListName)) {
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
     * This method creates translation job
     * 
     * @param translationJob contains name of translation job
     */
    private void createTranslationJob(String translationJob) {
        clkTranslationJob(translationJob);
        CSUtility.tempMethodForThreadSleep(2000);
        clkCreateNewTranslationJob();
        setLabel(translationJob);
    }

    /**
     * This method clicks on create new option from mid pane
     */
    private void clkCreateNewTranslationJob() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                csGuiToolbarHorizontal.getBtnCSGuiToolbarCreateNew()));
        csGuiToolbarHorizontal.getBtnCSGuiToolbarCreateNew().click();
        CSLogger.info("Clicked on create new to create new translation job");
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
     * This method clicks on translation manager
     */
    private void clkTranslationManager() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnTranslationManagerIcon(waitForReload);
    }

    /**
     * This method enters data in text box containing english text box to
     * translate into target rightmost pane
     * 
     * @param englishValueForValueList contains english values of the value list
     */
    private void enterDataInTextbox(String englishValueForValueList) {
        traverseToEdit();
        enterDetails(valueListNodeInstance.getTxtEnglish(),
                englishValueForValueList);
        clkSave();
    }

    /**
     * This method traverses to edit frame
     */
    private void traverseToEdit() {
        traverseToMain();
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
    }

    /**
     * This method clicks on create new in the middle pane
     */
    private void clkCreateNew() {
        traverseToMiddlePaneValueList();
        CSUtility.tempMethodForThreadSleep(1000);
        valueListNodeInstance.clkElement(waitForReload,
                valueListNodeInstance.getBtnCreateNew());
    }

    /**
     * This method traverses to the middle pane of value list
     */
    private void traverseToMiddlePaneValueList() {
        traverseToMain();
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method traverses to main frame
     */
    private void traverseToMain() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame190()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalBodyFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method clicks on created value list
     * 
     * @param valueListName contains name of value list
     * @param valueListCategory contains value list category
     */
    private void clkCreatedValueList(String valueListName,
            String valueListCategory) {
        WebElement valueList = getCreatedValueList(valueListCategory,
                valueListName);
        valueList.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("Clicked on created value list");
    }

    /**
     * This method creates and verifies the created value list
     * 
     * @param valueListName contains name of the value list
     * @param valueListCategory contains category of the value list
     */
    private void createValueList(String valueListName,
            String valueListCategory) {
        valueListNodeInstance.clkBtnValueList(waitForReload);
        selectMenu(valueListNodeInstance.getBtnValueRangeSettingTreeNode(),
                valueListPopup.getCtxAdd(), browserDriver);
        configureValueList(valueListName, valueListCategory);
        verifyCreatedValueList(valueListName, valueListCategory);
    }

    /**
     * This method selects menu from the pop up
     * 
     * @param selectNode contains selection node i.e node on which right click
     *            has to be performed
     * @param selectElement contains name of element to be clicked on from the
     *            pop up
     * @param browserDriver contains browserDriver instance
     */
    private void selectMenu(WebElement selectNode, WebElement selectElement,
            WebDriver browserDriver) {
        CSUtility.rightClickTreeNode(waitForReload, selectNode, browserDriver);
        valueListPopup.selectPopupDivMenu(waitForReload, selectElement,
                browserDriver);
    }

    /**
     * This method verifies created value list
     * 
     * @param valueListName contains name of the value list
     * @param valueListCategory contains name of the value list category
     * 
     */
    private void verifyCreatedValueList(String valueListName,
            String valueListCategory) {
        WebElement valueList = getCreatedValueList(valueListCategory,
                valueListName);
        Assert.assertEquals(valueListName, valueList.getText());
        CSLogger.info("Value list is present");
    }

    /**
     * This method gets details of value to be sent as the string parameter
     * 
     * @param value contains value to be getting details of
     * @return category
     */
    private WebElement getDetails(String value) {
        WebElement category = browserDriver.findElement(By.linkText(value));
        waitForReload.until(ExpectedConditions.visibilityOf(category));
        return category;
    }

    /**
     * This method configures the value list with designation and category
     * 
     * @param valueListName contains name of the value list
     * @param valueListCategory contains category of value list
     */
    private void configureValueList(String valueListName,
            String valueListCategory) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        enterDetails(valueListNodeInstance.getValueRangeDesignation(),
                valueListName);
        enterDetails(valueListNodeInstance.getValueRangeCategory(),
                valueListCategory);
        enableLanguageDependencyChkbox();
        clkSave();
    }

    /**
     * This method checks if checkbox is already enabled or not and enables if
     * checkbox is disabled
     */
    private void enableLanguageDependencyChkbox() {
        waitForReload.until(ExpectedConditions.visibilityOf(
                valueListNodeInstance.getChkboxLanguageDependence()));
        String attributeValueLangDependence = valueListNodeInstance
                .getChkboxLanguageDependence().getAttribute("class");
        if (attributeValueLangDependence.equals(
                "CSGuiEditorCheckboxContainer Enabled GuiEditorCheckbox Off")) {
            valueListNodeInstance.getChkboxLanguageDependence().click();
        } else {
            CSLogger.info(
                    "Checkbox for Language Dependence has already enabled");
        }
    }

    /**
     * This method enters details to the text box
     * 
     * @param element contains xpath of element which is gonna contains value
     * @param value contains value to be set in a text box
     */
    private void enterDetails(WebElement element, String value) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(value);
    }

    /**
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandSettingsTree() {
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioSettingsNode.clkBtnPimSettingsNode(waitForReload);
    }

    /**
     * This method verifies the outdated state of the value of the value list
     * 
     * @param valueListName contains the name of the value list
     * @param valueListCategory contains value list category (type)
     * @param englishValuesForList contains english values of value list to be
     *            created
     * @param translationLabel contains name of translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains name of the data collection field
     * @param editedValueName contains edited value name of the value list
     * @param translationTargetValueForEditedValueOfValueList contains
     *            translation target value for edited value of value list
     * @param translationTargetValueForSecondValueOfValueList contains
     *            translation target value for second value of value list
     * @param translationTargetValueForThirdValueOfValueList contains
     *            translation target value for third value of value list
     * @param valueOfTargetLanguage contains the value of target language
     * @param valueOfSourceLanguage contains the value of source language
     */
    @Test(
            priority = 2,
            dependsOnMethods = { "testTranslatingValueList" },
            dataProvider = "translatingValueList")
    public void testVerifyOutdatedState(String valueListName,
            String valueListCategory, String englishValuesForList,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String editedValueName,
            String translationTargetValueForEditedValueOfValueList,
            String translationTargetValueForSecondValueOfValueList,
            String translationTargetValueForThirdValueOfValueList,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        String[] valuesForList = getValues(englishValuesForList);
        try {
            clkCreatedValue(valuesForList[0]);
            editValueOfValueList(valueListName, valuesForList[0],
                    valueListCategory, editedValueName);
            CSUtility.tempMethodForThreadSleep(2000);
            goToTmsStudio(valuesForList[0], editedValueName);
        } catch (Exception e) {
            CSLogger.debug("Failed to verify outdated state of Value List", e);
            Assert.fail("Test case failed for verifying outdated state", e);
        }
    }

    /**
     * This method traverses and performs operations in the TMS studio
     * 
     * @param valueForList contains value of value list
     * @param editedValueName contains edited value name
     */
    private void goToTmsStudio(String valueForList, String editedValueName) {
        clkTranslationManager();
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(5000);
        clkCreatedValueListTranslationTab(valueForList);
        verifyStateChange(
                translatorManagerPage.getBtnVerifyOutdatedEntryTranslationTab(),
                "cancel.svg");
        availabilityOfTranslationTextAreaToEdit();
    }

    /**
     * This method checks availability of translation text area to edit
     * 
     * @param productName contains name of product
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
        }
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
     * This method verifies the status of the state in the translations tab
     * 
     * @param element contains webelement
     * @param valueName contains value name to be checked
     */
    private void verifyStatusOfState(WebElement element, String valueName) {
        boolean status = false;
        List<WebElement> list = getStateTranslationsTab();
        for (int stateIndex = 0; stateIndex < list.size(); stateIndex++) {
            String chkState = getState(element);
            if (chkState.contains(valueName)) {
                status = true;
                break;
            } else {
                status = false;
            }
        }
        if (status == true) {
            CSLogger.info("Translation state has changed in Translation tab");
        } else {
            CSLogger.info("Could not change translation state ");
            softAssert.fail("Failed to verify request  state");
        }
    }

    /**
     * This method gets state of an element and returns the source of the
     * element
     * 
     * @param element contains element to be taken current state of
     * @return chkState
     */
    private String getState(WebElement element) {
        String chkState = element.getAttribute("src");
        return chkState;
    }

    /**
     * This method returns the list of states column in translations tab
     * 
     * @return list
     */
    private List<WebElement> getStateTranslationsTab() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[contains(@class,'hidewrap CSAdminList')]/tbody/tr/td[3]"));
        return list;
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
     * This method clicks created workflow
     * 
     * @param workflowName contains name of workflow
     */
    private void clkCreatedValueListTranslationTab(String valueForList) {
        boolean status = false;
        WebElement value = null;
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        List<WebElement> list = getValuesOfValueList();
        for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
            value = list.get(valueIndex);
            if (value.getText().equals(valueForList)) {
                status = true;
                break;
            } else {
                status = false;
            }
        }
        if (status == true) {
            value.click();
            CSUtility.tempMethodForThreadSleep(3000);
            CSLogger.info("Clicked on created workflow");
        }
    }

    /**
     * This method edits the value of value list
     * 
     * @param valueListName contains name of the value list
     * @param valueName contains value name
     * @param valueListCategory contains value list category
     * @param editedValueName contains edited value name
     */
    private void editValueOfValueList(String valueListName, String valueName,
            String valueListCategory, String editedValueName) {
        switchToPimAndExpandSettingsTree();
        WebElement valueList = getCreatedValueList(valueListCategory,
                valueListName);
        waitForReload.until(ExpectedConditions.visibilityOf(valueList));
        valueList.click();
        CSLogger.info("Clicked on created value list from left pane");
        editValue(valueName, editedValueName);
    }

    /**
     * This method edits the value of the value list
     * 
     * @param valueName contains value name to be edited
     * @param editedValueName contains edited value name
     */
    private void editValue(String valueName, String editedValueName) {
        boolean status = false;
        WebElement value = null;
        CSUtility.tempMethodForThreadSleep(2000);
        traverseToMiddlePaneValueList();
        List<WebElement> list = getListOfValuesMidPane();
        for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
            value = list.get(valueIndex);
            if (value.getText().equals(valueName)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            performOperation(value, editedValueName);
        } else {
            CSLogger.error(
                    "Could not click on the value of value list from mid pane");
        }
    }

    /**
     * This method performs the operation of entering details in text box in the
     * rightmost pane after clicking an entry of value list in the mid pane
     * 
     * @param element contains element in which value has to be entered
     * @param editedValueName contains edited value name
     */
    private void performOperation(WebElement element, String editedValueName) {
        element.click();
        CSLogger.info("Clicked on the value of value list from mid pane");
        traverseToEdit();
        enterDetails(valueListNodeInstance.getTxtEnglish(), editedValueName);
        clkSave();
    }

    /**
     * This method returns the list of values of value list from the mid pane
     * 
     * @return list
     */
    private List<WebElement> getListOfValuesMidPane() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[contains(@class,'hidewrap CSAdminList')]/tbody/tr/td[3]"));
        return list;
    }

    /**
     * This method returns the created value list
     * 
     * @param valueListCategory contains value list category name
     * @param valueListName contains value list name
     * @return value List
     */
    private WebElement getCreatedValueList(String valueListCategory,
            String valueListName) {
        pimStudioSettingsNode.clkBtnPimSettingsNode(waitForReload);
        selectMenu(valueListNodeInstance.getBtnValueRangeSettingTreeNode(),
                valueListPopup.getCtxRefresh(), browserDriver);
        pimStudioSettingsNode.clkBtnPimSettingsNode(waitForReload);
        valueListNodeInstance.clkBtnValueList(waitForReload);
        WebElement category = getDetails(valueListCategory);
        category.click();
        WebElement valueList = getDetails(valueListName);
        return valueList;
    }

    /**
     * This method clicks on the created value of value list
     * 
     * @param valueOfValueList contains value of value list
     */
    private void clkCreatedValue(String valueOfValueList) {
        boolean status = false;
        WebElement value = null;
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        List<WebElement> list = getValuesOfValueList();
        for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
            value = list.get(valueIndex);
            if (value.getText().equals(valueOfValueList)) {
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
    }

    /**
     * This method verifies the request confirmation state of the translation
     * entry in the translations tab
     * 
     * @param valueListName contains the name of the value list
     * @param valueListCategory contains value list category (type)
     * @param englishValuesForList contains english values of value list to be
     *            created
     * @param translationLabel contains name of translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains name of the data collection field
     * @param editedValueName contains edited value name of the value list
     * @param translationTargetValueForEditedValueOfValueList contains
     *            translation target value for edited value of value list
     * @param translationTargetValueForSecondValueOfValueList contains
     *            translation target value for second value of value list
     * @param translationTargetValueForThirdValueOfValueList contains
     *            translation target value for third value of value list
     * @param valueOfTargetLanguage contains the value of target language
     * @param valueOfSourceLanguage contains the value of source language
     */
    @Test(
            priority = 3,
            dependsOnMethods = { "testVerifyOutdatedState" },
            dataProvider = "translatingValueList")
    public void testVerifyRequestConfirmationState(String valueListName,
            String valueListCategory, String englishValuesForList,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String editedValueName,
            String translationTargetValueForEditedValueOfValueList,
            String translationTargetValueForSecondValueOfValueList,
            String translationTargetValueForThirdValueOfValueList,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            translatorManagerPage.clkBtnSaveAndLoadNextElement(waitForReload);
            setTranslationText(translationTargetValueForEditedValueOfValueList);
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
     * This method sets translation text
     * 
     * @param translationTargetValue contains translation text
     */
    private void setTranslationText(String translationTargetValue) {
        WebElement txtTranslation = getTranslationTextArea();
        txtTranslation.sendKeys(translationTargetValue);
    }

    /**
     * This method veifies the translation of value list
     * 
     * @param valueOfTargetLanguage contains the value of target language
     * @param translationTargetValueForThirdValueOfValueList contains the
     *            translation target value of the third value of the value list
     * @param valueListName contains the value list name
     * @param valueListCategory contains the name of the value list category
     * @param englishValuesForList contains the english values for the list
     */
    private void verifyTranslationOfValueList(String valueOfTargetLanguage,
            String translationTargetValueForThirdValueOfValueList,
            String valueListName, String valueListCategory,
            String englishValuesForList) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnSystemPreferencesIcon(waitForReload);
        changeInterfaceLanguage(valueOfTargetLanguage);
        checkValueListTranslationChange(
                translationTargetValueForThirdValueOfValueList, valueListName,
                valueListCategory, englishValuesForList);
    }

    /**
     * This method checks value list for verifying if translation entry has been
     * translated or not
     * 
     * @param translationTargetValueForThirdValueOfValueList contains
     *            translation target value for the third value of the value list
     * @param valueListName contains value list name
     * @param valueListCategory contains value list category
     * @param englishValuesForList contains english values for the value list
     */
    private void checkValueListTranslationChange(
            String translationTargetValueForThirdValueOfValueList,
            String valueListName, String valueListCategory,
            String englishValuesForList) {
        boolean listStatus = getStatusForTranslatedList(valueListName,
                valueListCategory,
                translationTargetValueForThirdValueOfValueList);
        if (listStatus == true) {
            CSLogger.info("Entry has been translated");
        } else {
            CSLogger.error("Failed to translate entry");
            softAssert.fail("Failed to translate entry");
        }
    }

    /**
     * This method gets status for the translated list
     * 
     * @param valueListName contains value list name
     * @param valueListCategory contains category of value list
     * @param translationTargetValueForThirdValueOfValueList contains
     *            translation target value for the third value of the value list
     * @return listStatus
     */
    private boolean getStatusForTranslatedList(String valueListName,
            String valueListCategory,
            String translationTargetValueForThirdValueOfValueList) {
        listStatus = false;
        List<WebElement> translatedList = getTranslatedList(valueListName,
                valueListCategory);
        for (int valueIndex = 0; valueIndex < translatedList
                .size(); valueIndex++) {
            WebElement value = translatedList.get(valueIndex);
            if (value.getText()
                    .equals(translationTargetValueForThirdValueOfValueList)) {
                listStatus = true;
                break;
            }
        }
        return listStatus;
    }

    /**
     * This method verifies the confirm state of the translation entry in the
     * translations tab
     * 
     * @param valueListName contains the name of the value list
     * @param valueListCategory contains value list category (type)
     * @param englishValuesForList contains english values of value list to be
     *            created
     * @param translationLabel contains name of translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains name of the data collection field
     * @param editedValueName contains edited value name of the value list
     * @param translationTargetValueForEditedValueOfValueList contains
     *            translation target value for edited value of value list
     * @param translationTargetValueForSecondValueOfValueList contains
     *            translation target value for second value of value list
     * @param translationTargetValueForThirdValueOfValueList contains
     *            translation target value for third value of value list
     * @param valueOfTargetLanguage contains the value of target language
     * @param valueOfSourceLanguage contains the value of source language
     */
    @Test(
            priority = 4,
            dependsOnMethods = { "testVerifyRequestConfirmationState" },
            dataProvider = "translatingValueList")
    public void testVerifyConfirmState(String valueListName,
            String valueListCategory, String englishValuesForList,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String editedValueName,
            String translationTargetValueForEditedValueOfValueList,
            String translationTargetValueForSecondValueOfValueList,
            String translationTargetValueForThirdValueOfValueList,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            setTranslationText(translationTargetValueForSecondValueOfValueList);
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
     * This method returns the translated list containing list of values which
     * have been translated in the target language
     * 
     * @param valueListName contains value list naeme
     * @param valueListCategory contains value list category
     * @return translated list
     */
    private List<WebElement> getTranslatedList(String valueListName,
            String valueListCategory) {
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioSettingsNode.clkBtnPimSettingsNode(waitForReload);
        valueListNodeInstance.clkBtnValueList(waitForReload);
        WebElement category = getDetails(valueListCategory);
        category.click();
        WebElement valueList = getDetails(valueListName);
        waitForReload.until(ExpectedConditions.visibilityOf(valueList));
        valueList.click();
        CSUtility.tempMethodForThreadSleep(3000);
        traverseToMiddlePaneValueList();
        List<WebElement> translatedList = getTranslatedValueFromMidPane();
        return translatedList;
    }

    /**
     * This method verifies the up to date state of the value of value list in
     * the translations tab
     * 
     * @param valueListName contains the name of the value list
     * @param valueListCategory contains value list category (type)
     * @param englishValuesForList contains english values of value list to be
     *            created
     * @param translationLabel contains name of translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains name of the data collection field
     * @param editedValueName contains edited value name of the value list
     * @param translationTargetValueForEditedValueOfValueList contains
     *            translation target value for edited value of value list
     * @param translationTargetValueForSecondValueOfValueList contains
     *            translation target value for second value of value list
     * @param translationTargetValueForThirdValueOfValueList contains
     *            translation target value for third value of value list
     * @param valueOfTargetLanguage contains the value of target language
     * @param valueOfSourceLanguage contains the value of source language
     */
    @Test(
            priority = 5,
            dependsOnMethods = { "testVerifyConfirmState" },
            dataProvider = "translatingValueList")
    public void testVerifyUpToDateState(String valueListName,
            String valueListCategory, String englishValuesForList,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String editedValueName,
            String translationTargetValueForEditedValueOfValueList,
            String translationTargetValueForSecondValueOfValueList,
            String translationTargetValueForThirdValueOfValueList,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            traverseToTranslationTextArea();
            setTranslationText(translationTargetValueForThirdValueOfValueList);
            translatorManagerPage
                    .clkBtnImportFromTranslationMemory(waitForReload);
            verifyStateChange(
                    translatorManagerPage
                            .getBtnVerifyImportFromTranslationMemory(),
                    "up.svg");
            verifyTranslationOfValueList(valueOfTargetLanguage,
                    translationTargetValueForThirdValueOfValueList,
                    valueListName, valueListCategory, englishValuesForList);
            changeInterfaceLanguage(valueOfSourceLanguage);
            clkBtnImportFromTranslationMemoryTaskbar(translationLabel);
        } catch (Exception e) {
            CSLogger.debug("Verification of upto date state failed", e);
            Assert.fail("Verification of upto date state failed", e);
        }
    }

    /**
     * This method changes interface language
     * 
     * @param valueOfTargetLanguage contains value of target language
     */
    private void changeInterfaceLanguage(String valueOfTargetLanguage) {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        configPortalPopup.clkCtxLanguageSettings(waitForReload);
        selectLanguage(valueOfTargetLanguage);
    }

    /**
     * This method selects interface language
     * 
     * @param valueOfTargetLanguage contains value of target language
     */
    private void selectLanguage(String valueOfTargetLanguage) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowTranslationManager()));
        Select selectLanguage = new Select(browserDriver.findElement(
                By.xpath("//select[contains(@name,'InterfaceLanguage')]")));
        selectLanguage.selectByVisibleText(valueOfTargetLanguage);
        modalDialogPopupWindow.clkBtnCsGuiModalDialogOkButton(waitForReload);
        CSLogger.info("Language changed successfully to target language");
    }

    /**
     * This method gets translated value list from the mid pane
     * 
     * @return list
     */
    private List<WebElement> getTranslatedValueFromMidPane() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[4]"));
        return list;
    }

    /**
     * This method verifies the translations ov all the converted entries
     * 
     * @param valueListName contains the name of the value list
     * @param valueListCategory contains value list category (type)
     * @param englishValuesForList contains english values of value list to be
     *            created
     * @param translationLabel contains name of translation label
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param dataCollectionField contains name of the data collection field
     * @param editedValueName contains edited value name of the value list
     * @param translationTargetValueForEditedValueOfValueList contains
     *            translation target value for edited value of value list
     * @param translationTargetValueForSecondValueOfValueList contains
     *            translation target value for second value of value list
     * @param translationTargetValueForThirdValueOfValueList contains
     *            translation target value for third value of value list
     * @param valueOfTargetLanguage contains the value of target language
     * @param valueOfSourceLanguage contains the value of source language
     */
    @Test(
            priority = 6,
            dependsOnMethods = { "testVerifyUpToDateState" },
            dataProvider = "translatingValueList")
    public void testVerifyTranslations(String valueListName,
            String valueListCategory, String englishValuesForList,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField, String editedValueName,
            String translationTargetValueForEditedValueOfValueList,
            String translationTargetValueForSecondValueOfValueList,
            String translationTargetValueForThirdValueOfValueList,
            String valueOfTargetLanguage, String valueOfSourceLanguage) {
        try {
            changeInterfaceLanguage(valueOfTargetLanguage);
            checkAllConvertedEntries(
                    translationTargetValueForSecondValueOfValueList,
                    translationTargetValueForThirdValueOfValueList,
                    valueListName, valueListCategory);
            changeInterfaceLanguage(valueOfSourceLanguage);
        } catch (Exception e) {
            CSLogger.debug("Verification of translations failed for value list",
                    e);
            Assert.fail("Verification of translations failed for value list",
                    e);
        }
    }

    /**
     * This method checks all the converted entries i.e. values of value list
     * from source to target language
     * 
     * @param translationTargetValueForSecondValueOfValueList contains the
     *            translation target value for second value of value list
     * @param translationTargetValueForThirdValueOfValueList contains the
     *            translation target value for third value of value list
     * @param valueListName contains the value list name
     * @param valueListCategory contains value list category
     */
    private void checkAllConvertedEntries(
            String translationTargetValueForSecondValueOfValueList,
            String translationTargetValueForThirdValueOfValueList,
            String valueListName, String valueListCategory) {
        boolean statusSecondTranslatedValue = getStatusForTranslatedList(
                valueListName, valueListCategory,
                translationTargetValueForSecondValueOfValueList);
        verifyPresenceOfValue(statusSecondTranslatedValue,
                translationTargetValueForSecondValueOfValueList);
        boolean statusThirdTranslatedValue = getStatusForTranslatedList(
                valueListName, valueListCategory,
                translationTargetValueForThirdValueOfValueList);
        verifyPresenceOfValue(statusThirdTranslatedValue,
                translationTargetValueForThirdValueOfValueList);
    }

    /**
     * This method verifies if the entry has translated to the tr
     * 
     * @param status
     * @param targetValue
     */
    private void verifyPresenceOfValue(boolean status, String targetValue) {
        if (status == true) {
            CSLogger.info("Entry has been translated successfully with value"
                    + targetValue);
        } else {
            CSLogger.error("Failed to translate  entry");
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
            if (chkState.contains("refresh.png")) {
                count++;
            }
        }
        if (count > 1) {
            CSLogger.info("Confirmed entry states have been imported");
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
     * This data provider returns sheet which contains name of the value list,
     * category of the value list,english values for value list , translation
     * label ,source language , target language , name of the data collection
     * field ,edited value name , translation target value for edited value
     * list,translation target value for second value of value list ,
     * translation target value for third value of value list , value of target
     * language and value of source language
     * 
     * @return translatingValueListSheet
     */
    @DataProvider(name = "translatingValueList")
    public Object[][] translatingValueList() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                translatingValueListSheet);
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
        modalDialogPopupWindow = new ModalDialogPopupWindow(browserDriver);
        valueListNodeInstance = new ValuelistManagementPage(browserDriver);
        pimStudioSettingsNode = new PimStudioSettingsNode(browserDriver);
        valueListPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
    }
}
