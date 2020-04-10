/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.ITranslationManagerPopup;
import org.cs.csautomation.cs.settings.translationmanager.TranslationManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
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
 * This class translates the job hierarchy i.e the jobs which are created under
 * admin should present under admin folder and likewise with no-translator and
 * finished nodes
 * 
 * @author CSAutomation Team
 *
 */
public class TranslationJobHierarchyTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      translationJobHierarchySheet = "TranslationJobHierarchy";
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;
    private Actions                     actions;
    private ModalDialogPopupWindow      modalDialogPopupWindow;
    private PimStudioProductsNodePage   pimStudioProductsNode;
    private ITranslationManagerPopup    translationManagerPopup;

    /**
     * This test method translates the job hierarchy i.e the jobs which are
     * created under admin should present under admin folder and likewise with
     * no-translator and finished nodes
     * 
     * @param translationJob contains translation jobs
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param loggedInUser contains logged in user name
     * @param nodesUnderTranslationJobNode contains nodes under translation jobs
     *            node
     * @param productName contains name of product
     * @param translationTargetValueForProduct contains translation target value
     *            for product
     * @param dataCollectionField contains data collection field
     */
    @Test(priority = 1, dataProvider = "translationJobHierarchy")
    public void testTranslationJobHierarchy(String translationJob,
            String sourceLang, String targetLang, String loggedInUser,
            String nodesUnderTranslationJobNode, String productName,
            String translationTargetValueForProduct,
            String dataCollectionField) {
        try {
            String[] jobs = getJobs(translationJob);
            String[] nodes = getTranslationJobNodes(
                    nodesUnderTranslationJobNode);
            String jobTranslator = jobs[0];
            String jobNoTranslator = jobs[1];
            String jobFinished = jobs[2];
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            selectionOfTranslatorJob(jobTranslator, sourceLang, targetLang,
                    loggedInUser, dataCollectionField);
            verifyTranslatorJobUnderLoggedInUser(jobTranslator, nodes);
            selectionOfNoTranslatorJob(jobNoTranslator, sourceLang, targetLang,
                    loggedInUser, dataCollectionField);
            verifyTranslatorJobUnderNoTranslatorNode(jobNoTranslator, nodes);
            selectionOfFinishedJob(jobFinished, sourceLang, targetLang,
                    productName, translationTargetValueForProduct,
                    dataCollectionField);
            verifyTranslatorJobInFinishedNode(jobFinished, nodes);
        } catch (Exception e) {
            CSLogger.debug("Could not execute test case");
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method verifies if job is present under finished node
     * 
     * @param jobFinished contains name of job
     * @param nodes contains names of nodes
     */
    private void verifyTranslatorJobInFinishedNode(String jobFinished,
            String[] nodes) {
        clkTranslationJob(jobFinished);
        clkRefresh();
        clkTranslationJob(jobFinished);
        clkFinishedNode(jobFinished, nodes[3]);
    }

    /**
     * This method clicks on finished node
     * 
     * @param jobFinished contains name of job
     * @param finishedNode contains name of node
     */
    private void clkFinishedNode(String jobFinished, String finishedNode) {
        boolean status = false;
        WebElement product = null;
        clkNode(finishedNode);
        CSUtility.tempMethodForThreadSleep(2000);
        traversingForSettingsModule.traverseToMainFrameTranslationManager(
                waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        for (int productIndex = 0; productIndex < list.size(); productIndex++) {
            product = list.get(productIndex);
            if (product.getText().equals(jobFinished)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            Assert.assertEquals(jobFinished, product.getText());
            CSLogger.info(product.getText() + "  is present ");
        }
    }

    /**
     * This method verifies added product in translation manager
     * 
     * @param productName contains name of product
     */
    private void verifyAddedProduct(String productName) {
        try {
            boolean status = false;
            traversingForSettingsModule.traverseToMainFrameTranslationManager(
                    waitForReload, browserDriver);
            TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                    browserDriver);
            List<WebElement> list = browserDriver.findElements(
                    By.xpath("//table[@class='CSGuiTable']/tbody/tr/td[2]"));
            for (int productIndex = 0; productIndex < list
                    .size(); productIndex++) {
                WebElement product = list.get(productIndex);
                if (product.getText().equals(productName)) {
                    status = true;
                    break;
                }
            }
            if (status == true) {
                CSLogger.info("Product has been successfully added");
            } else {
                CSLogger.error("Could not add product");
            }
        } catch (Exception e) {
            CSLogger.error("Could not assign product", e);
            softAssert.fail("Product is not present", e);
        }
    }

    /**
     * This method performs the selection of finished job
     * 
     * @param jobFinished contains name of job
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param productName contains name of product
     * @param translationTargetValueForProduct contains translation target value
     *            for product
     * @param dataCollectionField contains data collection field
     */
    private void selectionOfFinishedJob(String jobFinished, String sourceLang,
            String targetLang, String productName,
            String translationTargetValueForProduct,
            String dataCollectionField) {
        executePrerequisites(jobFinished, sourceLang, targetLang, productName);
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        createTranslationJob(jobFinished, sourceLang, targetLang,
                dataCollectionField);
        addProduct(productName);
        verifyAddedProduct(productName);
        clkSave();
        importProduct();
        convertProductEntryToUptoDateState(productName,
                translationTargetValueForProduct);
    }

    /**
     * This method converts product entry to up to date state
     * 
     * @param productName contains name of product
     * @param translationTargetValueForProduct contains name of target value for
     *            product
     */
    private void convertProductEntryToUptoDateState(String productName,
            String translationTargetValueForProduct) {
        try {
            WebElement product = null;
            boolean status = false;
            List<WebElement> list = getProductList(productName);
            for (int productIndex = 0; productIndex < list
                    .size(); productIndex++) {
                product = list.get(productIndex);
                if (product.getText().equals(productName)) {
                    status = true;
                    break;
                } else {
                    status = false;
                }
            }
            if (status == true) {
                CSLogger.info("Product is present in Translations tab");
                product.click();
                clkUptoDateState(translationTargetValueForProduct);
            } else {
                CSLogger.error("Product is not present in translations tab");
            }
        } catch (Exception e) {
            CSLogger.error("Product is not present in translations tab");
            Assert.fail("Product is not present", e);
        }
    }

    /**
     * This method clicks on up to date state
     * 
     * @param translationTargetValueForProduct contains translation target value
     *            for product
     */
    private void clkUptoDateState(String translationTargetValueForProduct) {
        traverseToHorizontalButtonsTranslationsTab();
        setTranslationText(translationTargetValueForProduct);
        translatorManagerPage.clkBtnImportFromTranslationMemory(waitForReload);
        verifyImportedState();
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
            CSLogger.error("Could not change translation state ");
            softAssert.fail("Failed to verify request  state");
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
     * This method sets translation text
     * 
     * @param translationTargetValue contains translation text
     */
    private void setTranslationText(String translationTargetValue) {
        WebElement txtTranslation = getTranslationTextArea();
        txtTranslation.sendKeys(translationTargetValue);
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
     * This method returns list of products added in the translations tab
     * 
     * @param productName contains name of product
     * @return list
     */
    private List<WebElement> getProductList(String productName) {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmTabbedPaneTranslationManager()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        List<WebElement> list = getAddedProductTransltionsTab(productName);
        return list;
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

    private void importProduct() {
        translatorManagerPage.clkTranslationsTab(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        translatorManagerPage.clkBtnImport(waitForReload);
        CSUtility.tempMethodForThreadSleep(3000);
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
    }

    /**
     * This method executes prerequisites
     * 
     * @param jobFinished contains name of job
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param productName
     */
    private void executePrerequisites(String jobFinished, String sourceLang,
            String targetLang, String productName) {
        csPortalHeader.clkBtnProducts(waitForReload);
        createProduct(productName);
    }

    /**
     * This method creates product
     * 
     * @param productName contains name of product
     */
    private void createProduct(String productName) {
        clkProductFolder();
        createNewFolder(productName);
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
            CSUtility.tempMethodForThreadSleep(1000);
        } catch (Exception e) {
            CSLogger.error("New Product Folder creation failed", e);
        }
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
     * This method verifies translator job under no translator node in admin
     * 
     * @param jobNoTranslator contains name of job
     * @param nodes contains names of nodes
     */
    private void verifyTranslatorJobUnderNoTranslatorNode(
            String jobNoTranslator, String[] nodes) {
        clkTranslationJob(jobNoTranslator);
        clkRefresh();
        clkTranslationJob(jobNoTranslator);
        clkOpenNode(nodes[0]);
        clkNoTranslatorNode(nodes[2]);
        verifyAssignedJob(jobNoTranslator);
    }

    /**
     * This method clicks on no translator node
     * 
     * @param noTranslatorNode contains name of no translator node
     */
    private void clkNoTranslatorNode(String noTranslatorNode) {
        clkNode(noTranslatorNode);
    }

    /**
     * This method selects no translator job
     * 
     * @param jobNoTranslator contains name of job
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of data collection field
     */
    private void selectionOfNoTranslatorJob(String jobNoTranslator,
            String sourceLang, String targetLang, String loggedInUser,
            String dataCollectionField) {
        createTranslationJob(jobNoTranslator, sourceLang, targetLang,
                dataCollectionField);
        clkSave();
    }

    /**
     * This method returns array of nodes
     * 
     * @param nodesUnderTranslationJobNode contains node names
     * @return nodes
     */
    private String[]
            getTranslationJobNodes(String nodesUnderTranslationJobNode) {
        String[] nodes = nodesUnderTranslationJobNode.split("//");
        return nodes;
    }

    /**
     * This method verifies translator job under logged in user
     * 
     * @param jobTranslator contains name of job
     * @param nodes contains names of nodes
     */
    private void verifyTranslatorJobUnderLoggedInUser(String jobTranslator,
            String[] nodes) {
        clkTranslationJob(jobTranslator);
        clkRefresh();
        clkTranslationJob(jobTranslator);
        clkOpenNode(nodes[0]);
        clkAdminNode(nodes[1]);
        verifyAssignedJob(jobTranslator);
    }

    /**
     * This method clicks refresh
     */
    private void clkRefresh() {
        CSUtility.rightClickTreeNode(waitForReload,
                translatorManagerPage.getTranslationJobNode(), browserDriver);
        translationManagerPopup.selectPopupDivMenu(waitForReload,
                translationManagerPopup.getCtxRefresh(), browserDriver);
    }

    /**
     * This method verifies assigned job
     * 
     * @param translationJob contains name of translation job
     */
    private void verifyAssignedJob(String translationJob) {
        WebElement verifyJob = browserDriver.findElement(
                By.xpath("//span[text()=' " + translationJob + "']"));
        if (verifyJob.getText().equals(translationJob)) {
            Assert.assertEquals(translationJob, verifyJob.getText());
            CSLogger.info("Job which has assigned is present under "
                    + " admin node. Verified.");
        }
    }

    /**
     * This method clicks admin node
     * 
     * @param adminNode contains name of node
     */
    private void clkAdminNode(String adminNode) {
        clkNode(adminNode);
    }

    /**
     * This method clicks open node
     * 
     * @param openNode contains name of open node
     */
    private void clkOpenNode(String openNode) {
        clkNode(openNode);
    }

    /**
     * This method clicks on node
     * 
     * @param node contains name of node to be clicked on
     */
    private void clkNode(String node) {
        WebElement targetNode = browserDriver
                .findElement(By.xpath("//span[text()=' " + node + "']"));
        waitForReload.until(ExpectedConditions.visibilityOf(targetNode));
        targetNode.click();
    }

    /**
     * This method selects translator job
     * 
     * @param jobTranslator contains name of job
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param loggedInUser contains name of logged in user
     * @param dataCollectionField contains name of data collection field
     */
    private void selectionOfTranslatorJob(String jobTranslator,
            String sourceLang, String targetLang, String loggedInUser,
            String dataCollectionField) {
        createTranslationJob(jobTranslator, sourceLang, targetLang,
                dataCollectionField);
        selectTranslator(loggedInUser);
        clkSave();
    }

    /**
     * This method selects translator
     * 
     * @param loggedInUser contains logged in user
     */
    private void selectTranslator(String loggedInUser) {
        selectUser(loggedInUser, translatorManagerPage
                .getTxtTranslationJobAvailableTranslator());
    }

    /**
     * This method selects user from the text area
     * 
     * @param value contains value
     * @param element contains element
     */
    private void selectUser(String value, WebElement element) {
        try {
            boolean status = false;
            WebElement selectOption = null;
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            List<WebElement> list = element.findElements(By.tagName("option"));
            for (WebElement option : list) {
                if (option.getText().equals(value)) {
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

    /**
     * This method creates translation job
     * 
     * @param translationJob contains name of translation job
     * @param targetLang
     * @param sourceLang
     */
    private void createTranslationJob(String translationJob, String sourceLang,
            String targetLang, String dataCollectionField) {
        clkTranslationJob(translationJob);
        CSUtility.tempMethodForThreadSleep(2000);
        clkCreateNew();
        setLabel(translationJob);
        setLanguagesInDataCollectionTab(sourceLang, targetLang,
                dataCollectionField);
    }

    /**
     * This method set languages in data collection tab
     * 
     * @param sourceLang contains name of source language
     * @param targetLang contains name of target language
     * @param dataCollectionField contains name of data collection field
     */
    private void setLanguagesInDataCollectionTab(String sourceLang,
            String targetLang, String dataCollectionField) {
        translatorManagerPage.clkDataCollectionTab(waitForReload);
        Select drpdwnDataCollection = new Select(browserDriver.findElement(By
                .xpath("//select[contains(@id,'TranslationjobCollectorPlugin')]")));
        drpdwnDataCollection.selectByVisibleText(dataCollectionField);
        CSLogger.info("Clicked on Data Collection field");
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
        selectElement(targetLangInTranlationJob, translatorManagerPage
                .getTxtTranslationJobAvailableTargetLanguage());
    }

    /**
     * This method selects element from drop down and from text area
     * 
     * @param objectToBeTranslated contains object to be translated
     * @param element contains web element to be selected
     */
    private void selectElement(String workflowName, WebElement element) {
        try {
            boolean status = false;
            WebElement selectOption = null;
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            List<WebElement> list = element.findElements(By.tagName("option"));
            for (WebElement option : list) {
                if (option.getText().contains(workflowName)) {
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
     * This method clicks on translation manager
     */
    private void clkTranslationManager() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        csPortalWidget.clkOnTranslationManagerIcon(waitForReload);
    }

    /**
     * This method gets jobs in string array
     * 
     * @param translationJob contains names of translation jobs
     * @return jobs
     */
    private String[] getJobs(String translationJob) {
        String[] jobs = translationJob.split(",");
        return jobs;
    }

    /**
     * This data provider returns the sheet which contains translation job
     * names,source language ,target language,user ,nodes, product
     * names,translation target value for product and data collection field name
     * 
     * @return translationJobHierarchySheet
     */
    @DataProvider(name = "translationJobHierarchy")
    public Object[][] translationJobHierarchy() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                translationJobHierarchySheet);
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
        translatorManagerPage = new TranslationManagerPage(browserDriver);
        actions = new Actions(browserDriver);
        softAssert = new SoftAssert();
        csPortalWidget = new CSPortalWidget(browserDriver);
        traversingForSettingsModule = new TraversingForSettingsModule();
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        modalDialogPopupWindow = new ModalDialogPopupWindow(browserDriver);
        translationManagerPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
    }
}
