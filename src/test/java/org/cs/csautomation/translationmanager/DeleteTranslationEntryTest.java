/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.pom.IClassPopup;
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
 * This class deletes the translation entry
 * 
 * @author CSAutomation Team
 *
 */
public class DeleteTranslationEntryTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      deleteTranslationEntrySheet = "DeleteTranslationEntry";
    private IConfigPortalPopup          configPortalPopup;
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private Actions                     actions;
    private ModalDialogPopupWindow      modalDialogPopupWindow;
    private PimStudioSettingsNode       pimStudioSettingsNodeInstance;
    private IAttributePopup             attributePopup;
    private CSGuiDialogContentId        csGuiDialogContentIdInstance;
    private CSGuiDialogContentId        csGuiDialogContentId;
    private String[]                    setValues;

    @Test(priority = 1, dataProvider = "deleteTranslationEntry")
    public void testDeleteTranslationEntry(String attributeName,
            String attributeType, String selectType, String attributeValues,
            String translationLabel, String sourceLang, String targetLang,
            String dataCollectionField) {
        csPortalHeader.clkBtnProducts(waitForReload);
        executePrerequisites(attributeName, attributeType, selectType,
                attributeValues);
        performUseCase(translationLabel, sourceLang, targetLang,
                dataCollectionField, attributeName, attributeValues);

    }

    private void performUseCase(String translationJob, String sourceLang,
            String targetLang, String dataCollectionField, String attributeName,
            String attributeValues) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        createTranslationJob(translationJob);
        dataCollectionOperations(dataCollectionField, sourceLang, targetLang,
                attributeName);
        translationsTabOperations(attributeValues, translationJob);
    }

    private String[] getAttributeValues(String attributeValues) {
        String[] attributeValue = attributeValues.split(",");
        return attributeValue;
    }

    private void translationsTabOperations(String attributeValues,
            String translationJob) {
        String[] attributeValue = getAttributeValues(attributeValues);
        String value1 = attributeValue[0];
        String value2 = attributeValue[1];
        translatorManagerPage.clkTranslationsTab(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        List<WebElement> list = verifyAttributeEntryTranslationsTab(
                attributeValue, value1, value2);
        deleteEntry(value1, list, translationJob);
        verifyDeletedEntry(value1, translationJob);
    }

    /**
     * This method verifies deleted entry
     * 
     * @param value1 contains value that has been deleted
     * @param list contains list of values for translations tab
     * @param translationJob contains name of translation job
     */
    private void verifyDeletedEntry(String value1, String translationJob) {
        boolean status = false;
        clkCreatedJobMidPane(translationJob);
        clkTranslationsTabAfterClickingJobFromMidPane();
        CSUtility.tempMethodForThreadSleep(4000);
        List<WebElement> list = getValuesOfAttribute();
        for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
            WebElement verifyValue = list.get(valueIndex);
            if (verifyValue.getText().equals(value1)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSLogger.error("Unable to delete entry");
        } else {
            CSLogger.info("Entry deleted successfully from translations tab");
        }
    }

    private void clkTranslationsTabAfterClickingJobFromMidPane() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkTranslationsTab(waitForReload);
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
        List<WebElement> list = getAddedValuesTransltionsTab();
        return list;
    }

    /**
     * Returns list of values from translations tab
     * 
     * @return list
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
     * This method verifies attribute entries in translations tab
     * 
     * @param attributeValue contains name of attribute value
     * @param value1 contains first value
     * @param value2 contains second value
     * @return list
     */
    private List<WebElement> verifyAttributeEntryTranslationsTab(
            String[] attributeValue, String value1, String value2) {
        int count = 0;
        List<WebElement> list = getValuesOfAttribute();
        String[] entry = new String[list.size()];
        for (int valueIndex = 0; valueIndex < list.size(); valueIndex++) {
            WebElement valueName = list.get(valueIndex);
            entry[valueIndex] = valueName.getText();
        }
        count = checkEntry(count, entry, value1);
        count = checkEntry(count, entry, value2);
        if (count == attributeValue.length) {
            CSLogger.info("Values of  attributes are present");
        } else {
            CSLogger.error("Values of attributes  are not present");
        }
        return list;
    }

    /**
     * This method deletes entry from translations tab
     * 
     * @param value1 contains value to be deleted
     * @param list contains list of values in translations tab
     * @param translationJob contains name of translation job
     */
    private void deleteEntry(String value1, List<WebElement> list,
            String translationJob) {
        clkCreatedJobMidPane(translationJob);
        clkTranslationsTabAfterClickingJobFromMidPane();
        CSUtility.tempMethodForThreadSleep(3000);
        List<
             WebElement> listOfEntriesTranslationsTab = getTotalRowsTranslationsTab();
        Integer totalRowsForTranslationEntries = listOfEntriesTranslationsTab
                .size();
        for (int iRowCount = 3; iRowCount <= totalRowsForTranslationEntries; iRowCount++) {
            WebElement valueName = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[6]"));
            waitForReload.until(ExpectedConditions.visibilityOf(valueName));
            String cellValue = valueName.getText();
            if (cellValue.equals(value1)) {
                WebElement chkboxOfValue = browserDriver.findElement(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                + iRowCount + "]/td[1]/input[1]"));
                waitForReload
                        .until(ExpectedConditions.visibilityOf(chkboxOfValue));
                chkboxOfValue.click();
                CSLogger.info("Clicked on checkbox");
                break;
            }
        }
        selectDuplicateOption(true);
    }

    /**
     * This method clicks created job from mid pane
     * 
     * @param translationJob contains name of translation job
     */
    private void clkCreatedJobMidPane(String translationJob) {
        WebElement job = null;
        boolean status = false;
        clkTranslationJob(translationJob);
        List<WebElement> listOfJobsMidPane = getListOfTranslationJob(
                translationJob);
        for (int jobIndex = 0; jobIndex < listOfJobsMidPane
                .size(); jobIndex++) {
            job = listOfJobsMidPane.get(jobIndex);
            if (job.getText().equals(translationJob)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            job.click();
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("Clicked on created job from mid pane");
        }
    }

    /**
     * This method returns the list of total number of rows present in the
     * translation tab
     * 
     * @return list
     */
    private List<WebElement> getTotalRowsTranslationsTab() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = browserDriver.findElements(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr"));
        return list;
    }

    /**
     * This method gets list of translation jobs from mid pane
     * 
     * @param jobMassEdit contains name of job
     * @return list
     */
    private List<WebElement> getListOfTranslationJob(String jobMassEdit) {
        clkTranslationJob(jobMassEdit);
        CSUtility.tempMethodForThreadSleep(2000);
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        return list;
    }

    /**
     * This method selects duplicate option from
     * 
     * @param status contains boolean value for accepting alert box
     */
    private void selectDuplicateOption(boolean status) {
        traverseToMassSelectorOption();
        Select deleteOption = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        deleteOption.selectByVisibleText("Delete");
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (status == false) {
            alert.dismiss();
            CSLogger.info("Clicked on cancel ");
        } else if (status == true) {
            alert.accept();
            CSLogger.info("Clicked on OK of pop up");
        }
    }

    /**
     * This method traverses to mass selector option
     */
    private void traverseToMassSelectorOption() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
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
        CSUtility.tempMethodForThreadSleep(1000);
        enableCheckbox();
        selectElement(attributeName,
                translatorManagerPage.getTxtTabDataCollection());
        selectSourceLanguage(sourceLang);
        selectTargetLanguageForTranslationJob(targetLang);
        clkSave();
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
    private void enableCheckbox() {
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
     * @param attributeName contains name of attribute
     * @param attributeType contains type of attribute
     * @param selectType contains select type
     * @param attributeValues contains attribute values
     */
    private void executePrerequisites(String attributeName,
            String attributeType, String selectType, String attributeValues) {
        createNewAttribute(attributeName, true);
        verifyCreatedAttribute(attributeName);
        configureAttributeProperties(attributeName, attributeType, selectType,
                attributeValues);
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
        enableLanguageDependenceCheckbox();
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
     * This method enables language dependence checkbox
     */
    private void enableLanguageDependenceCheckbox() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        String StringChkboxAttribute = csGuiDialogContentIdInstance
                .getCbPdmarticleconfigurationLanguageDependent()
                .getAttribute("class");
        if (StringChkboxAttribute.equals(
                "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox")) {
            csGuiDialogContentIdInstance
                    .checkCbPdmarticleconfigurationLanguageDependent(
                            waitForReload);
            CSUtility.tempMethodForThreadSleep(1000);
            clkSave();
        } else {
            CSLogger.info("Already enabled");
        }
    }

    /**
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
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
     * This method clicks on attribute node
     */
    private void getAttributeNode() {
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        clkOnAttributesNode();
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
     * Clicks on attributes node in pim studio
     */
    public void clkOnAttributesNode() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode());
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
    }

    /**
     * Returns created attribute
     * 
     * @param attributeName name of attribute
     * @return
     */
    private WebElement getCreatedAttribute(String attributeName) {
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        return createdAttribute;
    }

    /**
     * This data provider returns sheet which contains attribute name ,
     * attribute type ,drop down to select attribute type ,attribute values,
     * translation label , source language , target language ,data collection
     * field
     * 
     * @return deleteTranslationEntrySheet
     */
    @DataProvider(name = "deleteTranslationEntry")
    public Object[][] translatingProducts() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                deleteTranslationEntrySheet);
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
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        attributePopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
    }
}
