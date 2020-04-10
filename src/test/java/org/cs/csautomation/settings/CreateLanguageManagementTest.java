/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IAttributePopup;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.LanguageManagementPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to create and verify of the Languages
 * 
 * @author CSAutomation Team
 */
public class CreateLanguageManagementTest extends AbstractTest {

    private LanguageManagementPage languageNodeInstance;
    private CSPortalHeader         csPortalHeader;
    private FrameLocators          iframeLocatorsInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSPortalWidget         portalWidget;
    Alert                          alertBox                = null;
    WebDriverWait                  waitForReload;
    String                         validationMessage       = "Abbreviation: An entry is required";
    String                         chkboxOffClassName      = "CSGuiEditorCheckboxContainer Off Enabled GuiEditorCheckbox";
    String                         createLanguageSheetName = "CreateLanguage";
    String                         updateMessage           = "The data model was updated successfully";

    /*
     * This method written for validation of the language field. It will
     * traverse till the language tree node and click on ADD option and click on
     * save button it gives popup message for validation
     */
    @Test(priority = 1)
    public void createLanguageRequiredFieldDialog() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            clickOnLanguageAndCreateNew();
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            CSLogger.info(
                    "Traverse in the Right pane and click on the save button");
            alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
            if (alertBox.getText().startsWith(validationMessage)) {
                CSLogger.info("Language validation successful with message "
                        + alertBox.getText());
                alertBox.accept();
            } else {
                Assert.fail("Language validation failed with message:"
                        + alertBox.getText());
            }
            CSLogger.info(
                    "Create language required field dialog test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error for Create language: ", e);
            Assert.fail("Create Language Failure");
        }
    }

    /*
     * This method written for creation of the Language field. It will traverse
     * till the Language tree node and click on ADD option and selected Language
     * and click on save button
     * 
     * @param langShortName String contains the short name of the language
     * 
     * @param langFullName String contains the full name of languages
     * 
     * @param langImageSelection String contains the image name
     * 
     * @param dictionarySuffix String contains the
     */
    @Test(priority = 2, dataProvider = "CreateLanguageTestData")
    public void editLanguage(String langShortName, String langFullName,
            String langImageSelection, String dictionarySuffix) {
        try {
            clickOnLanguageAndCreateNew();
            CSLogger.info(
                    "Traverse in the Right pane and click on the create new");
            languageNodeInstance.enterValue(waitForReload,
                    languageNodeInstance.getLanguageAbbreviation(),
                    langShortName);
            CSLogger.info("Added Language label");
            languageNodeInstance.enterValue(waitForReload,
                    languageNodeInstance.getLanguageFullName(), langFullName);
            languageNodeInstance.selectValue(
                    languageNodeInstance.getLanguageImageSelection(),
                    langImageSelection);
            languageNodeInstance.enterValue(waitForReload,
                    languageNodeInstance.getLanguageDictonarySuffix(),
                    dictionarySuffix);
            if (languageNodeInstance.getLanguageAvailableButton()
                    .getAttribute("class").equals(chkboxOffClassName)) {
                languageNodeInstance.getLanguageAvailableButton().click();
                CSLogger.info(
                        "Checked that language dependance is active or not");
            }
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            verificationOfCreatedLanguages(langShortName);
            CSLogger.info("verification done for the language creation");
            CSLogger.info("edit language test completed!");
            // updateDataModelWithVerfication();
            // ToDo Need to check for Data language verification
            // assignLanguageToAdmin(langFullName);
        } catch (Exception e) {
            CSLogger.debug("Automation Error edit langauge", e);
            Assert.fail("Language creation Failed");
        }
    }

    /*
     * This method is used to assign the Language to the Admin user.
     * 
     * @param String langFullName contains the full name of language
     */
    public void assignLanguageToAdmin(String langFullName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        languageNodeInstance.getCSPotalOptionsLink().click();
        IAttributePopup attributePopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        attributePopup.selectPopupDivMenu(waitForReload,
                languageNodeInstance.getAdminOptionPortal(), browserDriver);
        userWindowSetLanguage(langFullName);
    }

    /*
     * This method open the user window and the set the created language
     * 
     * @param String langFullName language Full name
     */
    public void userWindowSetLanguage(String langFullName) {
        String userWindow = browserDriver.getWindowHandle();
        browserDriver.switchTo().window(userWindow);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmUserWindowFrame()));
        languageNodeInstance.getUserAdministrationTab().click();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmUserWindowFrame()));
        CSUtility.tempMethodForThreadSleep(1000);
        languageNodeInstance.selectValue(languageNodeInstance.getUserLanguage(),
                langFullName);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmUserWindowFrame()));
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        verifySelectedLanguage(langFullName);
    }

    /*
     * This method is used to verify the selected language in the user window
     * 
     * @param String langFullName full name of language
     */
    public void verifySelectedLanguage(String langFullName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmUserWindowFrame()));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                languageNodeInstance.getUserLanguage());
        Select select = new Select(languageNodeInstance.getUserLanguage());
        WebElement option = select.getFirstSelectedOption();
        String defaultItem = option.getAttribute("defaultvalue");
        Assert.assertEquals(defaultItem, langFullName);
    }

    /*
     * This method is used to update the data model after creating languages .
     */
    public void updateDataModelWithVerfication() {
        clickOnInstallationTree();
        traverseToRightPageCompleteUpdate();
        languageNodeInstance.getCompleteUpdateLink().click();
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement updateDataModel = languageNodeInstance
                .getSuccessUpdateDataModel();
        CSUtility.waitForVisibilityOfElement(waitForReload, updateDataModel);
        Assert.assertEquals(updateDataModel.getText(), updateMessage);
        CSLogger.info(
                "Data model updated successfully" + updateDataModel.getText());
    }

    /*
     * This method is used to traverse in the right pane till the Edit frame for
     * creation of new Language field
     */
    public void traverseToEditFrame() {
        CSUtility.traverseToSystemPrefRightPaneFrames(waitForReload,
                browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEditForActiveScript()));
    }

    /*
     * This method clicks on the Language Node of the settings and in middle
     * pane header menu it will clicks on create new and opens the create new
     * window in right pane
     */
    public void clickOnLanguageAndCreateNew() {
        clickOnLanguageTree();
        CSUtility.traverseToSystemPrefRightPaneFrames(waitForReload,
                browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        languageNodeInstance.clickOnCreateNew(waitForReload);
        CSLogger.info("clicked on create new inside languages");
        CSUtility.traverseToSystemPrefRightPaneFrames(waitForReload,
                browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEditForActiveScript()));
    }

    /*
     * This method is will traverse till the settings ->system preferences->
     * Languages and click on the language node . It will opens the middle pane
     * of the list view
     */
    public void clickOnLanguageTree() {
        traverseTillSystemPreferences();
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        languageNodeInstance.getLanguageTreeNode().click();
        CSUtility.tempMethodForThreadSleep(2000);
        languageNodeInstance.getLanguageTreeNode().click();
        CSLogger.info("clicked on language node in system preferences");
    }

    /*
     * This method is will traverse till the settings ->system preferences
     */
    public void traverseTillSystemPreferences() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
    }

    /*
     * This method is will traverse till the settings ->system preferences->
     * Installation node and click on the update data model it will opens all
     * update data model options in Right pane
     */
    public void clickOnInstallationTree() {
        traverseTillSystemPreferences();
        CSLogger.info("Clicked on the setting tab and travers till"
                + " Installation in tree");
        languageNodeInstance.getInstallationTreeNode().click();
        WebElement updateDataModel = browserDriver
                .findElement(By.linkText("Update Data Model"));
        updateDataModel.click();
    }

    /*
     * This method is used to traverse in the right pane to click on complete
     * update
     */
    public void traverseToRightPageCompleteUpdate() {
        CSUtility.traversetoSystemPreferences(waitForReload, browserDriver,
                iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
    }

    /*
     * This method is used to verify the created language From the list view
     * 
     * @param String languageName contains the label of the language
     */
    public void verificationOfCreatedLanguages(String languageName) {
        clickOnLanguageTree();
        WebElement langaugeTree = browserDriver
                .findElement(By.linkText(languageName));
        CSLogger.info("In the verification of the language tree");
        Assert.assertEquals(langaugeTree.getText(), languageName);
    }

    /*
     * It returns the array containing data for creation of Langauges sheet
     * contains the String fields of LanguageName
     * 
     * @return Array of the language required fields
     */
    @DataProvider(name = "CreateLanguageTestData")
    public Object[][] getLanguageTestData() {

        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                createLanguageSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        languageNodeInstance = SettingsLeftSectionMenubar
                .getLanguagesSettingNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
    }

}
