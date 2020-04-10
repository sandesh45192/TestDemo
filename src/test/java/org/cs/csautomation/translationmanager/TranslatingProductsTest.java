/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

import java.util.ArrayList;
import java.util.Iterator;
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
import org.cs.csautomation.cs.settings.ITranslationManagerPopup;
import org.cs.csautomation.cs.settings.translationmanager.TranslationManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivConfigPortal;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
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
 * This class translate products , verifies outdated state of product and its
 * attributes,verifies request confirmation state , verifies confirm state
 * ,verifies up to date state and verification of translations
 * 
 * @author CSAutomation Team
 *
 */
public class TranslatingProductsTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      translatingProductsSheet = "TranslatingProducts";
    private IConfigPortalPopup          configPortalPopup;
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private PimStudioProductsNodePage   pimStudioProductsNode;
    private Actions                     actions;
    private ModalDialogPopupWindow      modalDialogPopupWindow;
    private String                      winHandleBeforeSwitching;
    private PimStudioSettingsNode       pimStudioSettingsNodeInstance;
    private IAttributePopup             attributePopup;
    private CSGuiDialogContentId        csGuiDialogContentIdInstance;
    private IClassPopup                 classPopUp;
    private CSGuiDialogContentId        csGuiDialogContentId;
    private IProductPopup               productPopUp;
    private ITranslationManagerPopup    translationManagerPopup;

    /**
     * This test case performs translation of products
     * 
     * @param productName contains product name
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationLabelName contains translation label name
     * @param dataCollectionField contains data collection field
     * @param loggedInUser contains logged in user name
     * @param editedTranslationLabel contains edited translation label name
     * @param singleLineAttr contains single line attribute name
     * @param unformattedAttr contains unformatted attribute
     * @param singleLineAttr2 contains new attribute
     * @param className contains class name
     * @param attributeFolder contains name of attribute folder
     * @param newAttributeLabel contains new attribute label
     * @param unformattedAttributeLabel contains unformatted attribute label
     * @param singleLineText contains single line text
     * @param singelLineAttr2Text contains new attribute text
     * @param unformattedText contains unformatted text
     * @param translationTargetValueNewAttribute contains target value for new
     *            attribute
     * @param translationTargetValueUnformatted contains target value for
     *            unformatted attribute
     * @param translationTargetValueEditedLabel contains target value for edited
     *            label
     */
    @Test(priority = 1, dataProvider = "translatingProductsTest")
    public void testTranslatingProducts(String productName, String sourceLang,
            String targetLang, String translationLabelName,
            String dataCollectionField, String loggedInUser,
            String editedTranslationLabel, String singleLineAttr,
            String unformattedAttr, String singleLineAttr2, String className,
            String attributeFolder, String singleLineAttr2Label,
            String unformattedAttributeLabel, String singleLineText,
            String singelLineAttr2Text, String unformattedText,
            String translationTargetValueNewAttribute,
            String translationTargetValueUnformatted,
            String translationTargetValueEditedLabel) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            executePrerequisites(singleLineAttr, unformattedAttr,
                    singleLineAttr2, attributeFolder, singleLineAttr2Label,
                    unformattedAttributeLabel, className, productName,
                    singleLineText, singelLineAttr2Text, unformattedText);
            selectSourceAndTargetLanguagesFromBPM(sourceLang, targetLang);
            translateProductUseCase(productName, sourceLang, targetLang,
                    translationLabelName, dataCollectionField, loggedInUser);
        } catch (Exception e) {
            CSLogger.debug("Test case for translating products failed", e);
            Assert.fail("Test case for translating products failed", e);
        }
    }

    private void executePrerequisites(String singleLineAttr,
            String unformattedAttr, String singleLineAttr2,
            String attributeFolder, String singleLineAttr2Label,
            String unformattedAttributeLabel, String className,
            String productName, String singleLineText,
            String singelLineAttr2Text, String unformattedText) {
        createAttributes(singleLineAttr, unformattedAttr, singleLineAttr2,
                attributeFolder, singleLineAttr2Label,
                unformattedAttributeLabel);
        createClass(className);
        CSUtility.tempMethodForThreadSleep(2000);
        dragAndDropAttributeFolderToClass(attributeFolder, className);
        createProduct(productName);
        dragAndDropClassToProduct(className, productName);
        setTextInAttributesDataPane(productName, singleLineAttr,
                singleLineAttr2, unformattedAttr, singleLineText,
                singelLineAttr2Text, unformattedText);
    }

    /**
     * This method sets text in attributes data pane
     * 
     * @param productName contains name of product
     * @param singleLineAttr contains single line attribute
     * @param singleLineAttr2 contains new attribute
     * @param unformattedAttr contains unformatted attribute
     * @param singleLineText contains single line text
     * @param newAttributeText contains new attribute text
     * @param unformattedText contains unformatted text
     */
    private void setTextInAttributesDataPane(String productName,
            String singleLineAttr, String singleLineAttr2,
            String unformattedAttr, String singleLineText,
            String newAttributeText, String unformattedText) {
        clkDataPaneOfProduct(productName);
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement txtSingleLine = browserDriver
                .findElement(By.xpath("//tr[contains(@cs_name,'"
                        + singleLineAttr + "')]/td[2]/input"));
        setText(txtSingleLine, singleLineText);
        WebElement txtNewAttribute = browserDriver
                .findElement(By.xpath("//tr[contains(@cs_name,'"
                        + singleLineAttr2 + "')]/td[2]/input"));
        setText(txtNewAttribute, newAttributeText);
        WebElement txtUnformatted = browserDriver
                .findElement(By.xpath("//tr[contains(@cs_name,'"
                        + unformattedAttr + "')]/td[2]/textarea"));
        setText(txtUnformatted, unformattedText);
        clkSave();
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method sets text in single line , new and unformatted attribute text
     * box
     * 
     * @param element contains web element
     * @param setText contains text that has to set
     */
    private void setText(WebElement element, String setText) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(Keys.DELETE);
        element.clear();
        element.sendKeys(setText);
    }

    /**
     * This method clicks on data pane of product
     * 
     * @param productName contains name of product
     */
    private void clkDataPaneOfProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement createdProdct = browserDriver
                .findElement(By.linkText(productName));
        actions.doubleClick(createdProdct).click().build().perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentId.clkBtnData(waitForReload);
    }

    /**
     * This method creates class
     * 
     * @param className contains class name
     */
    private void createClass(String className) {
        try {
            switchToPimAndExpandSettingsTree();
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            CSUtility
                    .rightClickTreeNode(waitForReload,
                            pimStudioSettingsNodeInstance
                                    .getBtnPimSettingsClassesNode(),
                            browserDriver);
            classPopUp.selectPopupDivMenu(waitForReload,
                    classPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            classPopUp.enterValueInDialogue(waitForReload, className);
            classPopUp.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
        } catch (Exception e) {
            CSLogger.error("Failed to create class", e);
            softAssert.fail("Class creation faiiled.", e);
        }
    }

    /**
     * This method drags and drops newly created attribute folder with
     * attributes to class
     * 
     * @param attributeFolder contains name of attribute folder
     * @param className contains name of class
     */
    private void dragAndDropAttributeFolderToClass(String attributeFolder,
            String className) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        switchToPimAndExpandSettingsTree();
        CSUtility.tempMethodForThreadSleep(3000);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsAttributesNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        WebElement attrFolderToDragDrop = browserDriver
                .findElement(By.linkText(attributeFolder));
        waitForReload
                .until(ExpectedConditions.visibilityOf(attrFolderToDragDrop));
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        waitForReload
                .until(ExpectedConditions.visibilityOf(classNameToDragDrop));
        CSUtility.tempMethodForThreadSleep(3000);
        actions.dragAndDrop(attrFolderToDragDrop, classNameToDragDrop).build()
                .perform();
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        CSLogger.info("Successfully dragged and droppped attribute to class");
    }

    /**
     * This method performs on extend , cancel and replace button as per
     * selection
     * 
     * @param btnSelect contains names of buttons
     */
    public void performClick(String btnSelect) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        if (btnSelect.equals("Cancel")) {
            waitForReload.until(ExpectedConditions.visibilityOf(
                    productPopUp.getBtnCsGuiModalDialogCancelButton()));
            productPopUp.getBtnCsGuiModalDialogCancelButton();
        } else if (btnSelect.equals("Extend")) {
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
     * This method drags and drops class to product
     * 
     * @param className contains name of class
     * @param productName contains name of product
     */
    private void dragAndDropClassToProduct(String className,
            String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(productName));
        createdProductFolder.click();
        CSUtility.tempMethodForThreadSleep(1000);
        switchToPimAndExpandSettingsTree();
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(className)));
        WebElement nameOfClass = browserDriver
                .findElement(By.linkText(className));
        CSUtility.tempMethodForThreadSleep(3000);
        nameOfClass.click();
        actions.dragAndDrop(nameOfClass, createdProductFolder).build()
                .perform();
        performClick("Extend");
    }

    /**
     * This method creates attributes in attribute folder
     * 
     * @param singleLineAttr contains single line attribute
     * @param unformattedAttr contains unformatted attribute
     * @param singleLineAttr2 contains new attribute
     * @param attributeFolder contains name of attribute folder
     * @param singleLineAttr2Label contains new attribute label
     * @param unformattedAttributeLabel contains unformatted attribute label
     */
    private void createAttributes(String singleLineAttr, String unformattedAttr,
            String singleLineAttr2, String attributeFolder,
            String singleLineAttr2Label, String unformattedAttributeLabel) {
        try {
            pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
            createAttributeFolder(attributeFolder, true);
            switchToPimAndExpandSettingsTree();
            clkOnAttributesNode();
            performUsecaseCreateAttribute(singleLineAttr, attributeFolder);
            verifyPresenceOfAttribute(singleLineAttr, attributeFolder);
            enableLanguageDependenceCheckbox(singleLineAttr, attributeFolder);
            performUsecaseCreateAttribute(singleLineAttr2, attributeFolder);
            verifyPresenceOfAttribute(singleLineAttr, attributeFolder);
            enableLanguageDependenceCheckbox(singleLineAttr2, attributeFolder);
            performUsecaseCreateAttribute(unformattedAttr, attributeFolder);
            verifyPresenceOfAttribute(singleLineAttr, attributeFolder);
            selectAttributeType(unformattedAttr, attributeFolder,
                    unformattedAttributeLabel);
            enableLanguageDependenceCheckbox(unformattedAttr, attributeFolder);
        } catch (Exception e) {
            CSLogger.error("Could not create and verify attributes", e);
            softAssert.fail("Failed to create and verify attribues", e);
        }
    }

    /**
     * This method verifies creation of attributes
     * 
     * @param attributeName contains attribute name
     * @param attributeFolder contains attribute folder name
     */
    private void verifyPresenceOfAttribute(String attributeName,
            String attributeFolder) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(attributeFolder));
        createdAttributeFolder.click();
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        Assert.assertEquals(attributeName, createdAttribute.getText());
    }

    /**
     * This method selects attribute type
     * 
     * @param attributeName contains attribute name
     * @param attributeFolder contains attribute folder name
     * @param attributeLabel contains attribute label
     */
    private void selectAttributeType(String attributeName,
            String attributeFolder, String attributeLabel) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        traverseToRightSectionPaneOfAttribute(attributeFolder, attributeName);
        csGuiDialogContentIdInstance.clkBtnCSTypeLabel(waitForReload);
        traverseToLeftFrame();
        WebElement setLabel = browserDriver
                .findElement(By.linkText(attributeLabel));
        waitForReload.until(ExpectedConditions.visibilityOf(setLabel));
        setLabel.click();
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
    }

    /**
     * This method checks and enables language dependence check box
     * 
     * @param attributeName contains attribute name
     * @param attributeFolder contains name of attribute folder
     */
    private void enableLanguageDependenceCheckbox(String attributeName,
            String attributeFolder) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        traverseToRightSectionPaneOfAttribute(attributeFolder, attributeName);
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
     * This method traverses to right section pane of attribute
     * 
     * @param attributeName contains attribute name
     * @param attributeFolder contains name of attribute folder
     */
    private void traverseToRightSectionPaneOfAttribute(String attributeFolder,
            String attributeName) {
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(attributeFolder));
        waitForReload
                .until(ExpectedConditions.visibilityOf(createdAttributeFolder));
        createdAttributeFolder.click();
        WebElement createdAttribute = browserDriver
                .findElement(By.linkText(attributeName));
        actions.doubleClick(createdAttribute).click().build().perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);

    }

    /**
     * This method creates new attribute folder
     * 
     * @param attributeName contains name of attribute
     * @param attributeFolder contains attribute folder
     */
    private void performUsecaseCreateAttribute(String attributeName,
            String attributeFolder) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        WebElement createdAttributeFolder = browserDriver
                .findElement(By.linkText(attributeFolder));
        CSUtility.rightClickTreeNode(waitForReload, createdAttributeFolder,
                browserDriver);
        CSLogger.info("Right Clicked On Newly Created Attribute Folder");
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSLogger.info("Clicked on Create New submenu");
        attributePopup.enterValueInUserInputDialogue(waitForReload,
                attributeName);
        attributePopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * This method switches to PIM and expands settings tree
     */
    private void switchToPimAndExpandSettingsTree() {
        csPortalHeader.clkBtnProducts(waitForReload);
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
     * This method creates attribute folder
     * 
     * @param attributeFolder contains name of attribute folder
     * @param chkStatus contains boolean value
     */
    private void createAttributeFolder(String attributeFolder,
            boolean chkStatus) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode(),
                browserDriver);
        CSLogger.info("Right clicked on Attribute Node");
        attributePopup.selectPopupDivMenu(waitForReload,
                attributePopup.getCsGuiPopupMenuNewFolder(), browserDriver);
        CSLogger.info("Selected New Folder Option");
        attributePopup.enterValueInDialogue(waitForReload, attributeFolder);
        attributePopup.askBoxWindowOperation(waitForReload, chkStatus,
                browserDriver);
    }

    /**
     * This method translates product
     * 
     * @param productName contains product name
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationLabelName contains translation label name
     * @param dataCollectionField contains data collection field
     * @param loggedInUser contains logged in user
     */
    private void translateProductUseCase(String productName, String sourceLang,
            String targetLang, String translationLabelName,
            String dataCollectionField, String loggedInUser) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnTranslationManagerIcon(waitForReload);
        createTranslationJob(sourceLang, targetLang, translationLabelName,
                dataCollectionField, loggedInUser, productName);
    }

    /**
     * This method selects source and target languages form BPM
     * 
     * @param sourceLang contains source language
     * @param targetLang contains target lanaguage
     */
    private void selectSourceAndTargetLanguagesFromBPM(String sourceLang,
            String targetLang) {
        clkConfigurationPortal();
        clkAdminOption();
        clkApplications();
        UnselectSourceAndTargetLanguages();
        selectLanguage(sourceLang,
                translatorManagerPage.getTxtAvailableSourceLang());
        selectLanguage(targetLang,
                translatorManagerPage.getTxtAvailableTargetLang());
        clkSave();
        closeWindow();
    }

    /**
     * This method unselects the previously selected source and target languages
     */
    private void UnselectSourceAndTargetLanguages() {
        translatorManagerPage.clkSubtiltleBPMTranslationManager(waitForReload);
        translatorManagerPage.clkBtbUnselectLanguage(waitForReload,
                translatorManagerPage.getBtnUnselectAllSourceLanguages());
        translatorManagerPage.clkBtbUnselectLanguage(waitForReload,
                translatorManagerPage.getBtnUnselectAllTargetLanguages());
        clkSave();
    }

    /**
     * This method clicks on Applications Tab of admin window
     */
    private void clkApplications() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowWidget()));
        translatorManagerPage.clkApplicationsTab(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method clicks on Admin option
     */
    private void clkAdminOption() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        configPortalPopup.clkCtxAdmin(waitForReload);
    }

    /**
     * This method clicks on configuration portal
     */
    private void clkConfigurationPortal() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
    }

    /**
     * This method selects language
     * 
     * @param language contains language to be selected
     * @param element contains web element
     */
    private void selectLanguage(String language, WebElement element) {
        try {
            boolean status = false;
            WebElement selectOption = null;
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            List<WebElement> list = element.findElements(By.tagName("option"));
            for (WebElement option : list) {
                if (option.getText().equals(language)) {
                    selectOption = option;
                    status = true;
                    break;
                }
            }
            if (status == true) {
                actions.doubleClick(selectOption).click().build().perform();
                CSLogger.info("Double clicked on language");
            } else {
                CSLogger.error("Failed to double click ");
            }
        } catch (Exception e) {
            CSLogger.error("Could not select and double click the language");
        }
    }

    private void createProduct(String productName) {
        clkProductFolder();
        createNewFolder(productName);
    }

    /**
     * This method creates translation job
     * 
     * @param translationJob contains translation job name
     */
    private void createTranslationJob(String sourceLangInTranlationJob,
            String targetLangInTranlationJob, String translationJob,
            String dataCollectionField, String loggedInUser,
            String productName) {
        CSUtility.tempMethodForThreadSleep(2000);
        clkCreateNew();
        setLabel(translationJob);
        dataCollectionOperations(dataCollectionField, sourceLangInTranlationJob,
                targetLangInTranlationJob, loggedInUser, productName);
        translationTabOperations(productName);
    }

    /**
     * This method closes window
     */
    private void closeWindow() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        translatorManagerPage.clkBtnClose(waitForReload);
    }

    /**
     * This method performs all operations related to data collection tab
     * 
     * @param dataCollectionField contains data collection field
     * @param sourceLangInTranlationJob contains source language in translation
     *            job
     * @param targetLangInTranlationJob contains target language in translation
     *            job
     */
    private void dataCollectionOperations(String dataCollectionField,
            String sourceLangInTranlationJob, String targetLangInTranlationJob,
            String loggedInUser, String productName) {
        translatorManagerPage.clkDataCollectionTab(waitForReload);
        Select drpdwnDataCollection = new Select(browserDriver.findElement(By
                .xpath("//select[contains(@id,'TranslationjobCollectorPlugin')]")));
        drpdwnDataCollection.selectByVisibleText(dataCollectionField);
        CSLogger.info("Clicked on Data Collection field");
        selectSourceLanguage(sourceLangInTranlationJob);
        selectTargetLanguageForTranslationJob(targetLangInTranlationJob);
        clkSave();
        addProduct(productName);
        verifyAddedButtons();
    }

    /**
     * This method adds product to translation manager
     * 
     * @param productName contains name of product
     */
    private void addProduct(String productName) {
        translatorManagerPage
                .clkBtnPlusToAddProductTranlationManager(waitForReload);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        WebElement nameOfProduct = browserDriver
                .findElement(By.linkText(productName));
        actions.doubleClick(nameOfProduct).build().perform();
        CSLogger.info("Double clicked on created product");
        modalDialogPopupWindow.handlePopupDataSelectionDialog(waitForReload,
                true);
        verifyAddedProduct(productName);
        clkSave();
    }

    private void verifyAddedButtons() {
        try {
            int count = 0;
            List<WebElement> list = browserDriver.findElements(By.xpath(
                    "//table[@class='CSGuiToolbarHorizontalTable']/tbody/tr/td/a/img"));
            for (int btnIndex = 0; btnIndex < list.size(); btnIndex++) {
                String sourceOfButton = list.get(btnIndex).getAttribute("src");
                if (sourceOfButton.contains("down.svg")) {
                    count++;
                    if (count == 1)
                        CSLogger.info("Error button is not present");
                } else if (sourceOfButton.contains("up.svg")) {
                    count++;
                    if (count == 2)
                        CSLogger.info("Refresh button is not present");
                }
            }
            if (count == 2) {
                CSLogger.info("Buttons are pesent ");
            } else {
                CSLogger.info("Buttons are not present");
            }
        } catch (Exception e) {
            CSLogger.debug("Buttons are not present", e);
            softAssert.fail("Buttons are not present", e);
        }
    }

    /**
     * This method verifies added product in translation manager
     * 
     * @param productName contains name of product
     */
    private void verifyAddedProduct(String productName) {
        boolean status = false;
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        List<WebElement> list = browserDriver.findElements(
                By.xpath("//table[@class='CSGuiTable']/tbody/tr/td[2]"));
        for (int productIndex = 0; productIndex < list.size(); productIndex++) {
            WebElement product = list.get(productIndex);
            if (product.getText().equals(productName)) {
                status = true;
            }
        }
        if (status == true) {
            CSLogger.info("Product has been successfully added");
        } else {
            CSLogger.info("Could not add product");
        }
    }

    /**
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method selects target language for translation job
     * 
     * @param targetLangInTranlationJob contains target language in translation
     *            job
     */
    private void selectTargetLanguageForTranslationJob(
            String targetLangInTranlationJob) {
        selectLanguage(targetLangInTranlationJob, translatorManagerPage
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
     * This method sets label in properties tab
     * 
     * @param translationJob contains name of the translation job
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
     * This method performs the use case which clicks on the product folder
     * 
     * @param waitForReload contains time which is used to wait for particular
     *            element to reload
     */
    public void clkProductFolder() {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioProductsNode.getBtnPimProductsNode()));
            pimStudioProductsNode.getBtnPimProductsNode().click();
            CSLogger.info("Clicked on Products Folder...");
            CSUtility.rightClickTreeNode(waitForReload,
                    pimStudioProductsNode.getBtnPimProductsNode(),
                    browserDriver);
            CSUtility.switchToDefaultFrame(browserDriver);
            CSLogger.info("Right clicked on Products folder.");
        } catch (Exception e) {
            CSLogger.error("Product Folder creation failed   " + e);
        }
    }

    /**
     * This method performs the test case which creates the new product folder
     * under products folder(Root node)
     * 
     * @param waitForReload waits for an element to reload
     * @param nameOfProduct it contains the name of the product
     */
    public void createNewFolder(String nameOfProduct) {
        try {
            IProductPopup productPopUp = new CSPopupDivPim(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(pimStudioProductsNode.getProductPopUpDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            pimStudioProductsNode.getproductPopUpFrame()));
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    pimStudioProductsNode.getClkCreateNew()));
            pimStudioProductsNode.getClkCreateNew().click();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmDataSelectionDialog()));
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    pimStudioProductsNode.getUserElement()));
            CSUtility.tempMethodForThreadSleep(1000);
            pimStudioProductsNode.getUserElement().sendKeys(nameOfProduct);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    productPopUp.getBtnCsGuiModalDialogOkButton()));
            productPopUp.getBtnCsGuiModalDialogOkButton().click();
        } catch (Exception e) {
            CSLogger.error("New Product Folder creation failed", e);
        }
    }

    /**
     * This method performs translation tab operations
     * 
     * @param productName contains name of product
     */
    private void translationTabOperations(String productName) {
        translatorManagerPage.clkTranslationsTab(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
        verifyProductEntry(productName);
    }

    /**
     * This method verifies if product is present in translation manager
     * 
     * @param productName contains name of product
     */
    private void verifyProductEntry(String productName) {
        try {
            boolean status = false;
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmTabbedPaneTranslationManager()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameMain()));
            List<WebElement> list = getAddedProductTransltionsTab(productName);
            for (int productIndex = 0; productIndex < list
                    .size(); productIndex++) {
                WebElement product = list.get(productIndex);
                if (product.getText().equals(productName)) {
                    status = true;
                    break;
                } else
                    status = false;
            }
            if (status == true) {
                CSLogger.info("Product is present in Translations tab");
            } else {
                CSLogger.info("Product is not present in translations tab");
            }
        } catch (Exception e) {
            CSLogger.error("Product is not present in translations tab");
            Assert.fail("Product is not present", e);
        }
    }

    /**
     * This method gets added product from translations tab
     * 
     * @param productName contains name of product
     * @return list
     */
    private List<WebElement> getAddedProductTransltionsTab(String productName) {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[6]"));
        return list;
    }

    /**
     * This test case verifies outdated state of product
     * 
     * @param productName contains product name
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationLabelName contains translation label name
     * @param dataCollectionField contains data collection field
     * @param loggedInUser contains logged in user name
     * @param editedTranslationLabel contains edited translation label name
     * @param singleLineAttr contains single line attribute name
     * @param unformattedAttr contains unformatted attribute
     * @param singleLineAttr2 contains new attribute
     * @param className contains class name
     * @param attributeFolder contains name of attribute folder
     * @param singleLineAttr2Label contains new attribute label
     * @param unformattedAttributeLabel contains unformatted attribute label
     * @param singleLineText contains single line text
     * @param singelLineAttr2Text contains new attribute text
     * @param unformattedText contains unformatted text
     * @param translationTargetValueNewAttribute contains target value for new
     *            attribute
     * @param translationTargetValueUnformatted contains target value for
     *            unformatted attribute
     * @param translationTargetValueEditedLabel contains target value for edited
     *            label
     */
    @Test(
            priority = 2,
            dependsOnMethods = { "testTranslatingProducts" },
            dataProvider = "translatingProductsTest")
    public void testVerifyOutdatedState(String productName, String sourceLang,
            String targetLang, String translationLabelName,
            String dataCollectionField, String loggedInUser,
            String editedTranslationLabel, String singleLineAttr,
            String unformattedAttr, String singleLineAttr2, String className,
            String attributeFolder, String singleLineAttr2Label,
            String unformattedAttributeLabel, String singleLineText,
            String singelLineAttr2Text, String unformattedText,
            String translationTargetValueNewAttribute,
            String translationTargetValueUnformatted,
            String translationTargetValueEditedLabel) {
        try {
            boolean status = false;
            WebElement product = null;
            List<WebElement> list = getAddedProductTransltionsTab(productName);
            for (int productIndex = 0; productIndex < list
                    .size(); productIndex++) {
                product = list.get(productIndex);
                if (product.getText().equals(productName)) {
                    status = true;
                    break;
                }
            }
            if (status == true) {
                verifyOutdatedState(productName, product, translationLabelName,
                        editedTranslationLabel);
            } else {
                CSLogger.info("could not find product");
            }
            performOperationsOnStates(editedTranslationLabel);
        } catch (Exception e) {
            CSLogger.debug("Test case failed for verifying outdated state", e);
            softAssert.fail("Could not verify outdated state", e);
        }
    }

    /**
     * This method performs operations on states
     * 
     * @param editedTranslationLabel contains edited Translation label
     */
    private void performOperationsOnStates(String editedTranslationLabel) {
        checkAvailabilityOfTranslationAreaForEditedProduct(
                editedTranslationLabel);

    }

    /**
     * This method verifies outdated state of product
     * 
     * @param productName contains name of the product
     * @param product contains product as WebElement
     * @param translationLabelName contains translation label name
     * @param editedTranslationLabel contains edited translation label name
     */
    private void verifyOutdatedState(String productName, WebElement product,
            String translationLabelName, String editedTranslationLabel) {
        waitForReload.until(ExpectedConditions.visibilityOf(product));
        product.click();
        winHandleBeforeSwitching = browserDriver.getWindowHandle();
        editContentOfTranslationEntry(translationLabelName,
                editedTranslationLabel);
        clkCreatedProduct(productName);
        traverseToTranslationsTabEntries();
        verifyStatusOfState(translatorManagerPage.getBtnOutdated(),
                "cancel.svg");
        availabilityOfTranslationTextAreaToEdit(productName);
    }

    /**
     * This method clicks created workflow
     * 
     * @param workflowName contains name of workflow
     */
    private void clkCreatedProduct(String productName) {
        boolean status = false;
        WebElement workflow = null;
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        List<WebElement> list = getProduct();
        for (int workflowIndex = 0; workflowIndex < list
                .size(); workflowIndex++) {
            workflow = list.get(workflowIndex);
            if (workflow.getText().equals(productName)) {
                status = true;
                break;
            } else {
                status = false;
            }
        }
        if (status == true) {
            workflow.click();
            CSUtility.tempMethodForThreadSleep(3000);
            CSLogger.info("Clicked on created product");
        }
    }

    /**
     * This method gets added workflows list from the translation tab
     * 
     * @return list
     */
    private List<WebElement> getProduct() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = getAddedProductTransltionsTab();
        return list;
    }

    private List<WebElement> getAddedProductTransltionsTab() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[6]"));
        return list;
    }

    /**
     * This method checks availability of translation area for edited product
     * 
     * @param editedTranslationLabel contains edited translation label
     */
    private void checkAvailabilityOfTranslationAreaForEditedProduct(
            String editedTranslationLabel) {
        translatorManagerPage.clkBtnSaveAndLoadNextElement(waitForReload);
        WebElement txtTranslation = getTranslationTextArea();
        txtTranslation.clear();
        if (txtTranslation.getText().equals("")) {
            CSLogger.info("Edited product is editable");
        } else {
            CSLogger.info("Edited product is not editable");
        }
    }

    /**
     * This method checks availability of translation text area to edit
     * 
     * @param productName contains name of product
     */
    private void availabilityOfTranslationTextAreaToEdit(String productName) {
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
     * This method edits contents of translation entry
     * 
     * @param translationLabelName contains translation label name
     * @param editedTranslationLabel contains edited translation label
     */
    private void editContentOfTranslationEntry(String translationLabelName,
            String editedTranslationLabel) {
        traverseToHorizontalButtonsTranslationsTab();
        translatorManagerPage.clkBtnDisplayObject(waitForReload);
        ArrayList<String> newTab1 = new ArrayList<String>(
                browserDriver.getWindowHandles());
        CSUtility.tempMethodForThreadSleep(2000);
        browserDriver.switchTo().window(newTab1.get(1));
        translatorManagerPage.clkPropertiesTab(waitForReload);
        editLabel(editedTranslationLabel);
        browserDriver.switchTo().window(newTab1.get(1)).close();
        browserDriver.switchTo().window(winHandleBeforeSwitching);
        importEditedLabelTranslationsTab(translationLabelName);
    }

    /**
     * This method imports edited label in translations tab b clicking import
     * button
     */
    private void importEditedLabelTranslationsTab(String translationLabelName) {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        CSUtility.tempMethodForThreadSleep(3000);
        translatorManagerPage.clkBtnImport(waitForReload);
    }

    /**
     * This method disables the filter bar that has been opened to find the
     * translation label from the mid pane
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
            CSUtility.tempMethodForThreadSleep(2000);
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
                WebElement btnFilter = browserDriver.findElement(By
                        .xpath("//img[contains(@src,'filter.gif')]/parent::a"));
                CSUtility.waitForVisibilityOfElement(waitForReload, btnFilter);
                btnFilter.click();
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
     * This method performs click on the label in the mod pane
     * 
     * @param data contains label to be found
     */
    private void
            performClickOnTranslationLabelMidPane(String translationLabel) {
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> listLabels = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        for (Iterator<WebElement> iterator = listLabels.iterator(); iterator
                .hasNext();) {
            listElement = (WebElement) iterator.next();
            if (listElement.getText().equals(translationLabel)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.waitForVisibilityOfElement(waitForReload, listElement);
            listElement.click();
        } else {
            CSLogger.error(
                    "Could not find the translation label name in the mid pane");
        }
    }

    /**
     * This method edits label in translation tab
     * 
     * @param editedTranslationLabel contains edited translation label
     */
    private void editLabel(String editedTranslationLabel) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(translatorManagerPage.getTxtLabel()));
        translatorManagerPage.getTxtLabel().clear();
        translatorManagerPage.getTxtLabel().sendKeys(editedTranslationLabel);
        CSUtility.tempMethodForThreadSleep(1000);
        clkSave();
    }

    /**
     * This method traverses to horizontal buttons of translations tab
     */
    private void traverseToHorizontalButtonsTranslationsTab() {
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmEdit()));
    }

    /**
     * This test case verifies request confirmation state of product
     * 
     * @param productName contains product name
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationLabelName contains translation label name
     * @param dataCollectionField contains data collection field
     * @param loggedInUser contains logged in user name
     * @param editedTranslationLabel contains edited translation label name
     * @param singleLineAttr contains single line attribute name
     * @param unformattedAttr contains unformatted attribute
     * @param singleLineAttr2 contains new attribute
     * @param className contains class name
     * @param attributeFolder contains name of attribute folder
     * @param singleLineAttr2Label contains new attribute label
     * @param unformattedAttributeLabel contains unformatted attribute label
     * @param singleLineText contains single line text
     * @param singelLineAttr2Text contains new attribute text
     * @param unformattedText contains unformatted text
     * @param translationTargetValueNewAttribute contains target value for new
     *            attribute
     * @param translationTargetValueUnformatted contains target value for
     *            unformatted attribute
     * @param translationTargetValueEditedLabel contains target value for edited
     *            label
     */
    @Test(
            priority = 3,
            dependsOnMethods = { "testVerifyOutdatedState" },
            dataProvider = "translatingProductsTest")
    public void testVerifyRequestConfirmationState(String productName,
            String sourceLang, String targetLang, String translationLabelName,
            String dataCollectionField, String loggedInUser,
            String editedTranslationLabel, String singleLineAttr,
            String unformattedAttr, String singleLineAttr2, String className,
            String attributeFolder, String singleLineAttr2Label,
            String unformattedAttributeLabel, String singleLineText,
            String singelLineAttr2Text, String unformattedText,
            String translationTargetValueNewAttribute,
            String translationTargetValueUnformatted,
            String translationTargetValueEditedLabel) {
        try {
            translatorManagerPage.clkBtnSaveAndLoadNextElement(waitForReload);
            setTranslationText(translationTargetValueNewAttribute);
            translatorManagerPage.clkBtnRequestConfirmation(waitForReload);
            traverseToTranslationsTabEntries();
            CSUtility.tempMethodForThreadSleep(2000);
            verifyStatusOfState(
                    translatorManagerPage
                            .getBtnVerifyRequestConfirmationTranslationsTab(),
                    "question.svg");
        } catch (Exception e) {
            CSLogger.debug(
                    "Test case for request confirmation verification failed.",
                    e);
            softAssert.fail(
                    "Test case for request confirmation verification failed",
                    e);
        }
    }

    /**
     * This method verifies status of state
     * 
     * @param element contains web element
     * @param string this method contains string
     */
    private void verifyStatusOfState(WebElement element, String string) {
        boolean status = false;
        List<WebElement> list = getStateChangeTranslationTab();
        for (int stateIndex = 0; stateIndex < list.size(); stateIndex++) {
            String chkState = getState(element);
            if (chkState.contains(string)) {
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
     * This method gets state for verification
     * 
     * @param element contains web element
     * @return chkState
     */
    private String getState(WebElement element) {
        String chkState = element.getAttribute("src");
        return chkState;
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
     * This method returns list of products in translations tab
     * 
     * @return
     */
    private List<WebElement> getStateChangeTranslationTab() {
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]/img"));
        return list;
    }

    /**
     * This test case performs confirm state of products
     * 
     * @param productName contains product name
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationLabelName contains translation label name
     * @param dataCollectionField contains data collection field
     * @param loggedInUser contains logged in user name
     * @param editedTranslationLabel contains edited translation label name
     * @param singleLineAttr contains single line attribute name
     * @param unformattedAttr contains unformatted attribute
     * @param singleLineAttr2 contains new attribute
     * @param className contains class name
     * @param attributeFolder contains name of attribute folder
     * @param singleLineAttr2Label contains new attribute label
     * @param unformattedAttributeLabel contains unformatted attribute label
     * @param singleLineText contains single line text
     * @param singelLineAttr2Text contains new attribute text
     * @param unformattedText contains unformatted text
     * @param translationTargetValueNewAttribute contains target value for new
     *            attribute
     * @param translationTargetValueUnformatted contains target value for
     *            unformatted attribute
     * @param translationTargetValueEditedLabel contains target value for edited
     *            label
     */
    @Test(
            priority = 4,
            dependsOnMethods = { "testVerifyRequestConfirmationState" },
            dataProvider = "translatingProductsTest")
    public void testVerifyConfirmState(String productName, String sourceLang,
            String targetLang, String translationLabelName,
            String dataCollectionField, String loggedInUser,
            String editedTranslationLabel, String singleLineAttr,
            String unformattedAttr, String singleLineAttr2, String className,
            String attributeFolder, String singleLineAttr2Label,
            String unformattedAttributeLabel, String singleLineText,
            String singelLineAttr2Text, String unformattedText,
            String translationTargetValueNewAttribute,
            String translationTargetValueUnformatted,
            String translationTargetValueEditedLabel) {
        try {
            traverseToHorizontalButtonsTranslationsTab();
            setTranslationText(translationTargetValueUnformatted);
            translatorManagerPage.clkBtnConfirm(waitForReload);
            verifyConfirmState();
        } catch (Exception e) {
            CSLogger.debug("Test case failed for verifying confirm state", e);
            softAssert.fail("Test case failed for verifying confirm state", e);
        }
    }

    /**
     * This method verifies confirm state
     */
    private void verifyConfirmState() {
        traverseToTranslationsTabEntries();
        verifyStatusOfState(
                translatorManagerPage.getBtnVerifyConfirmTranslationsTab(),
                "check.svg");
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
     * This test case verifies up to date state of product
     * 
     * @param productName contains product name
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationLabelName contains translation label name
     * @param dataCollectionField contains data collection field
     * @param loggedInUser contains logged in user name
     * @param editedTranslationLabel contains edited translation label name
     * @param singleLineAttr contains single line attribute name
     * @param unformattedAttr contains unformatted attribute
     * @param singleLineAttr2 contains new attribute
     * @param className contains class name
     * @param attributeFolder contains name of attribute folder
     * @param singleLineAttr2Label contains new attribute label
     * @param unformattedAttributeLabel contains unformatted attribute label
     * @param singleLineText contains single line text
     * @param singelLineAttr2Text contains new attribute text
     * @param unformattedText contains unformatted text
     * @param translationTargetValueNewAttribute contains target value for new
     *            attribute
     * @param translationTargetValueUnformatted contains target value for
     *            unformatted attribute
     * @param translationTargetValueEditedLabel contains target value for edited
     *            label
     */
    @Test(
            priority = 5,
            dependsOnMethods = { "testVerifyConfirmState" },
            dataProvider = "translatingProductsTest")
    public void testVerifyUpToDateState(String productName, String sourceLang,
            String targetLang, String translationLabelName,
            String dataCollectionField, String loggedInUser,
            String editedTranslationLabel, String singleLineAttr,
            String unformattedAttr, String singleLineAttr2, String className,
            String attributeFolder, String singleLineAttr2Label,
            String unformattedAttributeLabel, String singleLineText,
            String singelLineAttr2Text, String unformattedText,
            String translationTargetValueNewAttribute,
            String translationTargetValueUnformatted,
            String translationTargetValueEditedLabel) {
        try {
            traverseToHorizontalButtonsTranslationsTab();
            setTranslationText(translationTargetValueEditedLabel);
            translatorManagerPage
                    .clkBtnImportFromTranslationMemory(waitForReload);
            verifyImportedState();
            verifyTranslatedResultInPim(editedTranslationLabel);
            clkBtnImportFromTranslationMemoryTaskbar(translationLabelName);
        } catch (Exception e) {
            CSLogger.debug(
                    "Test case for verifying up to date state is failed.", e);
            softAssert.fail(
                    "Test case for verifying up to date state is failed", e);
        }
    }

    /**
     * This method verifies results in pim
     * 
     * @param editedTranslationLabel contains edited translation label
     */
    private void verifyTranslatedResultInPim(String editedTranslationLabel) {
        csPortalHeader.clkBtnProducts(waitForReload);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        CSLogger.info("Clicked on Products Folder");
        clkRefresh();
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement translatedLabel = browserDriver
                .findElement(By.linkText(editedTranslationLabel));
        waitForReload.until(ExpectedConditions.visibilityOf(translatedLabel));
        String label = translatedLabel.getText();
        if (label.equals(editedTranslationLabel)) {
            CSLogger.info(
                    "Verified.Only the content of single translation entry has translated");
        } else {
            CSLogger.error(
                    "Failed to verify the content of translation entry ");
        }
    }

    /**
     * This method refreshes the Products node
     */
    private void clkRefresh() {
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        translatorManagerPage.clkCtxRefresh(waitForReload);
    }

    /**
     * This method verifies imported state
     */
    private void verifyImportedState() {
        traverseToTranslationsTabEntries();
        verifyStatusOfState(
                translatorManagerPage.getBtnVerifyImportFromTranslationMemory(),
                "up.svg");
    }

    /**
     * This method clicks import button from translation memeory taskbar
     */
    private void clkBtnImportFromTranslationMemoryTaskbar(
            String translationLabelName) {
        int count = 0;
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        clkTranslationJob(translationLabelName);
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        enterDataInFilterBar(translationLabelName);
        CSUtility.tempMethodForThreadSleep(3000);
        disableFilterBar();
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        translatorManagerPage.clkBtnImportFromTranslationMemory(waitForReload);
        traverseToTranslationsTabEntries();
        List<WebElement> list = getStateChangeTranslationTab();
        for (int stateIndex = 0; stateIndex < list.size(); stateIndex++) {
            String chkState = getState(translatorManagerPage
                    .getBtnVerifyImportFromTranslationMemory());
            if (chkState.contains("up.svg")) {
                count++;
            }
        }
        if (count > 1) {
            CSLogger.info("Confirmed entry states have been imported");
        }
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
     * This test case performs verification of translations
     * 
     * @param productName contains product name
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationLabelName contains translation label name
     * @param dataCollectionField contains data collection field
     * @param loggedInUser contains logged in user name
     * @param editedTranslationLabel contains edited translation label name
     * @param singleLineAttr contains single line attribute name
     * @param unformattedAttr contains unformatted attribute
     * @param singleLineAttr2 contains new attribute
     * @param className contains class name
     * @param attributeFolder contains name of attribute folder
     * @param singleLineAttr2Label contains new attribute label
     * @param unformattedAttributeLabel contains unformatted attribute label
     * @param singleLineText contains single line text
     * @param singelLineAttr2Text contains new attribute text
     * @param unformattedText contains unformatted text
     * @param translationTargetValueNewAttribute contains target value for new
     *            attribute
     * @param translationTargetValueUnformatted contains target value for
     *            unformatted attribute
     * @param translationTargetValueEditedLabel contains target value for edited
     *            label
     */
    @Test(
            priority = 6,
            dependsOnMethods = { "testVerifyUpToDateState" },
            dataProvider = "translatingProductsTest")
    public void testVerificationsOfTranslations(String productName,
            String sourceLang, String targetLang, String translationLabelName,
            String dataCollectionField, String loggedInUser,
            String editedTranslationLabel, String singleLineAttr,
            String unformattedAttr, String singleLineAttr2, String className,
            String attributeFolder, String singleLineAttr2Label,
            String unformattedAttributeLabel, String singleLineText,
            String singelLineAttr2Text, String unformattedText,
            String translationTargetValueNewAttribute,
            String translationTargetValueUnformatted,
            String translationTargetValueEditedLabel) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                    browserDriver);
            WebElement translatedLabel = browserDriver
                    .findElement(By.linkText(editedTranslationLabel));
            waitForReload
                    .until(ExpectedConditions.visibilityOf(translatedLabel));
            actions.doubleClick(translatedLabel).build().perform();
            changeDataLanguageToTarget(targetLang);
            verifyAttributesDataPane(singleLineAttr, singleLineAttr2,
                    unformattedAttr, singleLineText, singelLineAttr2Text,
                    unformattedText);
        } catch (Exception e) {
            CSLogger.debug("Test case failed for verificaton of translations",
                    e);
            Assert.fail("Test case failed for verifcatonof translations", e);
        }
    }

    /**
     * This method verifies attributes in data pane
     * 
     * @param singleLineAttr contains single line attribute
     * @param singleLineAttr2 contains new attribute name
     * @param unformattedAttr contains name of unformatted attribute
     * @param singleLineText contains single line text
     * @param newAttributeText contains new attribute text
     * @param unformattedText contains unformatted text
     */
    private void verifyAttributesDataPane(String singleLineAttr,
            String singleLineAttr2, String unformattedAttr,
            String singleLineText, String newAttributeText,
            String unformattedText) {
        String getSingleLineAttrTranslated = getTranslatedAttributeText(
                singleLineAttr);
        String getNewAttrTranslated = getTranslatedAttributeText(
                singleLineAttr2);
        String getUnformattedAttrTranslated = getTranslatedAttributeText(
                unformattedAttr);
        if ((getSingleLineAttrTranslated.equals(singleLineText))
                && ((getNewAttrTranslated.equals(newAttributeText))
                        && (getUnformattedAttrTranslated
                                .equals(unformattedText)))) {
            CSLogger.info("All attributes have converted");
        } else {
            CSLogger.error("Conversion failed.");
        }
    }

    /**
     * This method gets translated attribute text
     * 
     * @param attribute contains attribute as string
     * @return translatedAttributeText
     */
    private String getTranslatedAttributeText(String attribute) {
        WebElement getSingleLineTranslated = browserDriver.findElement(
                By.xpath("//tr[contains(@cs_name,'" + attribute + "')]/td[3]"));
        String translatedAttributeText = getSingleLineTranslated.getText();
        return translatedAttributeText;
    }

    /**
     * This method changes data language to target language
     * 
     * @param targetLang contains target language
     */
    private void changeDataLanguageToTarget(String targetLang) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        translatorManagerPage.clkBtnCsGuiEditorFlag(waitForReload);
        WebElement languageToBeSelected = clkDataLanguageOption(targetLang);
        languageToBeSelected.click();
        csGuiDialogContentId.clkBtnData(waitForReload);
    }

    private WebElement clkDataLanguageOption(String targetLang) {
        translationManagerPopup.selectPopupDivMenu(waitForReload,
                translationManagerPopup.getCtxDataLanguage(), browserDriver);
        traverseToTargetLanguage(targetLang);
        WebElement languageToBeSelected = browserDriver.findElement(
                By.xpath("//table[@class='CSGuiPopup']/tbody/tr/td[@title='"
                        + targetLang + "']"));
        waitForReload
                .until(ExpectedConditions.visibilityOf(languageToBeSelected));
        CSUtility.tempMethodForThreadSleep(1000);
        return languageToBeSelected;
    }

    private void traverseToTargetLanguage(String targetLang) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                translationManagerPopup.getCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivSubFrame()));
    }

    /**
     * This data provider returns sheet which contains product name, Source
     * language , Target language,label name for translation tab,data collection
     * field,logged in user name, edited translation label name , single line
     * attribute ,unformatted attribute , new single line attribute ,class name,
     * attribute folder name ,multiline unformatted attribute text , multiline
     * formatted text,single line new attribute text,unformatted attribute text
     * ,translation target value in single line attribute ,translation target
     * value in new attribute , translation target value for unformatted
     * attribute
     * 
     * @return translatingProductsSheet
     */
    @DataProvider(name = "translatingProductsTest")
    public Object[][] translatingProducts() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                translatingProductsSheet);
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
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        translationManagerPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
    }
}
