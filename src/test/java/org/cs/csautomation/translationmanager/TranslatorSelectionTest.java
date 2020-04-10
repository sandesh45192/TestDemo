/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.translationmanager;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IConfigPortalPopup;
import org.cs.csautomation.cs.settings.translationmanager.TranslationManagerPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivConfigPortal;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
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
 * This class verifies the translator selection section with combinations of
 * source and target languages
 * 
 * @author CSAutomation Team
 *
 */
public class TranslatorSelectionTest extends AbstractTest {

    private CSPortalHeader              csPortalHeader;
    private WebDriverWait               waitForReload;
    private FrameLocators               locator;
    private String                      translatorSelectionSheet = "TranslatorSelection";
    private IConfigPortalPopup          configPortalPopup;
    private Actions                     actions;
    private SoftAssert                  softAssert;
    private CSPortalWidget              csPortalWidget;
    private TranslationManagerPage      translatorManagerPage;
    private TraversingForSettingsModule traversingForSettingsModule;
    private CSGuiToolbarHorizontal      csGuiToolbarHorizontal;

    /**
     * This test method verifies the translator selection section with
     * combinations of source and target languages
     * 
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationJob contains translation job name
     * @param dataCollectionField contains name of data collection field
     * @param sourceLangInTranlationJob contains source language in translation
     *            job
     * @param targetLangInTranlationJob contains target language in translation
     *            job
     * @param loggedInUser contains name of user that has logged in
     */
    @Test(priority = 1, dataProvider = "translatorSelection")
    public void testTranslatorSelectionTest(String sourceLang,
            String targetLang, String translationJob,
            String dataCollectionField, String sourceLangInTranlationJob,
            String targetLangInTranlationJob, String loggedInUser) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            performUseCase(sourceLang, targetLang, translationJob,
                    dataCollectionField, sourceLangInTranlationJob,
                    targetLangInTranlationJob, loggedInUser);
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method verifies the translator selection section with combinations
     * of source and target languages
     * 
     * @param sourceLang contains source language
     * @param targetLang contains target language
     * @param translationJob contains translation job name
     * @param dataCollectionField contains name of data collection field
     * @param sourceLangInTranlationJob contains source language in translation
     *            job
     * @param targetLangInTranlationJob contains target language in translation
     *            job
     * @param loggedInUser contains name of user that has logged in
     */
    private void performUseCase(String sourceLang, String targetLang,
            String translationJob, String dataCollectionField,
            String sourceLangInTranlationJob, String targetLangInTranlationJob,
            String loggedInUser) {
        selectSourceAndTargetLanguagesFromBPM(sourceLang, targetLang);
        clkTranslationJob(translationJob);
        clkCreateNew();
        setLabel(translationJob);
        dataCollectionOperations(dataCollectionField, sourceLangInTranlationJob,
                targetLangInTranlationJob);
        verifyAbsenceOfUserTranslatorField(loggedInUser);
        selectSourceLanguage(sourceLang);
        clkBtnClearAllSelectedTargetLangDataCollection(
                targetLangInTranlationJob);
        selectTargetLanguageForTranslationJob(targetLang);
        clkSave();
        verifyPresenceOfUserTranslatorField(loggedInUser);
        clkBtnClearAllSelectedTargetLangDataCollection(targetLang);
        selectTargetLanguageForTranslationJob(sourceLang);
        clkSave();
        verifyAbsenceOfUserTranslatorField(loggedInUser);
        clkBtnClearAllSelectedTargetLangDataCollection(sourceLang);
        selectTargetLanguageForTranslationJob(targetLang);
        clkSave();
        verifyPresenceOfUserTranslatorField(loggedInUser);
        verifyAbsenceOfUserInSelectedTranslatorField(loggedInUser);
    }

    /**
     * This method clicks on clear all button to clear all selected target
     * languages
     * 
     * @param targetLangInTranlationJob contains target language in translation
     *            tab
     */
    private void clkBtnClearAllSelectedTargetLangDataCollection(
            String targetLangInTranlationJob) {
        selectLanguage(targetLangInTranlationJob, translatorManagerPage
                .getTxtTranslationJobSelectedTargetLanguage());
    }

    /**
     * This method gets webelement of logged in user
     * 
     * @param loggedInUser contains logged in user
     * @return user
     */
    private WebElement getUser(String loggedInUser) {
        WebElement user = browserDriver.findElement(
                By.xpath("//option[contains(text(),'" + loggedInUser + "')]"));
        waitForReload.until(ExpectedConditions.visibilityOf(user));
        return user;
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
            String sourceLangInTranlationJob,
            String targetLangInTranlationJob) {
        translatorManagerPage.clkDataCollectionTab(waitForReload);
        Select drpdwnDataCollection = new Select(browserDriver.findElement(By
                .xpath("//select[contains(@id,'TranslationjobCollectorPlugin')]")));
        drpdwnDataCollection.selectByVisibleText(dataCollectionField);
        selectSourceLanguage(sourceLangInTranlationJob);
        selectTargetLanguageForTranslationJob(targetLangInTranlationJob);
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
        traversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
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
     * This method selects source and target languages form BPM
     * 
     * @param sourceLang contains source language
     * @param targetLang contains target language
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
        translatorManagerPage.clkSubtiltleBPMTranslationManager(waitForReload);
        closeWindow();
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
                actions.doubleClick(selectOption).build().perform();
                CSLogger.info("Double clicked on language");
            } else {
                CSLogger.error("Failed to double click ");
            }
        } catch (Exception e) {
            CSLogger.error("Could not select and double click the language");
        }
    }

    /**
     * This method clicks save button
     */
    private void clkSave() {
        csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method clicks on configuration portal
     */
    private void clkConfigurationPortal() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
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
     * This method clicks on Applications
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
     * This method verifies absence of user in translator field
     * 
     * @param loggedInUser contains user from which application has logged in
     */
    private void verifyAbsenceOfUserTranslatorField(String loggedInUser) {
        try {
            WebElement user = getUser(loggedInUser);
            if (user.getText().equals(loggedInUser)) {
                CSLogger.debug("Verification failed.");
                softAssert.fail("Verification failed");
            }
        } catch (Exception e) {
            CSLogger.info("User is absent. Verified.");
        }
    }

    /**
     * This method verifies presence of user in translator field
     * 
     * @param loggedInUser contains user from which application has logged in
     */
    private void verifyPresenceOfUserTranslatorField(String loggedInUser) {
        WebElement user = getUser(loggedInUser);
        if (user.getText().equals(loggedInUser)) {
            Assert.assertEquals(loggedInUser, user.getText());
            CSLogger.info("User is present. Verified");
        } else {
            CSLogger.error("User is not present");
            softAssert.fail(
                    "Verification of presence of user in translator filed is failed.");
        }
    }

    /**
     * This method verifies absence of user in selected translator field
     * 
     * @param loggedInUser contains logged in user name
     */
    private void
            verifyAbsenceOfUserInSelectedTranslatorField(String loggedInUser) {
        try {
            waitForReload
                    .until(ExpectedConditions.visibilityOf(translatorManagerPage
                            .getTxtTranslationJobSelectedTranslatorField()));
            List<WebElement> list = translatorManagerPage
                    .getTxtTranslationJobSelectedTranslatorField()
                    .findElements(By.tagName("option"));
            if (list.isEmpty()) {
                CSLogger.info(
                        "Verified . No user is present in Selected field");
            }
        } catch (Exception e) {
            CSLogger.error(
                    "User is present in the Selected section of translator field .Test case failed.");
            softAssert.fail("Verification failed.", e);
        }
    }

    /**
     * This data provider returns sheet data which contains source language in
     * bpm, target language in bpm,translation job name,data collection field to
     * be selected,source language while creating translation job,target
     * language while creating translation job,logged in user name
     * 
     * @return translatorSelectionSheet
     */
    @DataProvider(name = "translatorSelection")
    public Object[][] translatorSelection() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("translationManagerTestCases"),
                translatorSelectionSheet);
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
    }
}
