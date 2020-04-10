/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.FlexTablePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods which verifies Translations Option test
 * uses.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyTranslationsOptionTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSPortalHeader         csPortalHeader;
    private FrameLocators          locator;
    private SoftAssert             softasssert;
    private FlexTablePage          flexTablePage;
    private String                 TranslationsOptionTestData = "TranslationsOption";

    /**
     * This method verifies Translations Option test uses.
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     * @param language String object contains language of table
     * @param tableCaption String object contains table caption
     * @param footer String object contains number footer name
     * @param tableLanguage String object contains table language
     */
    @Test(dataProvider = "TranslationsOptionDataSheet")
    public void testVerifyTranslationsOption(String templateName,
            String templateType, String language, String tableCaption,
            String footer, String tableLanguage) {
        try {
            ArrayList<String> contextLanguage = new ArrayList<String>();
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToCreatedFlexTemplate(templateName, templateType);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            flexTablePage.clkOnTranslationsPane(waitForReload);
            verifyTranslationSection();
            HashMap<String, String> hashMapTable = sendDataInHashMap(language,
                    tableCaption);
            HashMap<
                    String,
                    String> hashMapFooter = sendDataInHashMap(language, footer);
            addDataInTranslationOption(hashMapTable, hashMapFooter);
            TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                    browserDriver);
            flexTablePage.clkOnDataPane(waitForReload);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getDrpDwnFlexPresetLanguage());
            Select presetLanguage = new Select(
                    flexTablePage.getDrpDwnFlexPresetLanguage());
            presetLanguage.selectByVisibleText(tableLanguage);
            WebElement sampleContext = browserDriver.findElement(By.xpath(
                    "//div[@id='title__sections::Sample Context']/parent::*"));
            if (sampleContext.getAttribute("class")
                    .contains("section_closed")) {
                flexTablePage.getSelectionSampleContext().click();
            }
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    flexTablePage.getDrpDwnFlexPresetContextLanguage());
            List<WebElement> list = flexTablePage
                    .getDrpDwnFlexPresetContextLanguage()
                    .findElements(By.tagName("option"));
            for (WebElement option : list) {
                contextLanguage.add(option.getText());
            }
            verifyTranslationOption(hashMapTable, hashMapFooter, tableLanguage,
                    contextLanguage);
            softasssert.assertAll();
            CSLogger.info("verify translations option test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerifyTranslationsOption",
                    e);
            Assert.fail("Automation Error : testVerifyTranslationsOption", e);
        }
    }

    /**
     * This method verifies the translation option test
     * 
     * @param hashMapTable HashMap object contains table caption names
     * @param hashMapFooter HashMap object contains table footer names
     * @param tableLanguage String object contains table language
     * @param contextLanguage ArrayList object contains context language
     */
    private void verifyTranslationOption(HashMap<String, String> hashMapTable,
            HashMap<String, String> hashMapFooter, String tableLanguage,
            ArrayList<String> contextLanguage) {
        Select contextLanguageDrpDwn = new Select(
                flexTablePage.getDrpDwnFlexPresetContextLanguage());
        for (String languageValue : contextLanguage) {
            for (String hashKey : hashMapTable.keySet()) {
                if (languageValue.contains("(" + hashKey + ")")) {
                    TraversingForSettingsModule
                            .traverseToMainFrame(waitForReload, browserDriver);
                    CSUtility.waitForVisibilityOfElement(waitForReload,
                            flexTablePage.getDrpDwnFlexPresetContextLanguage());
                    contextLanguageDrpDwn.selectByVisibleText(languageValue);
                    csGuiToolbarHorizontalInstance
                            .clkBtnCSGuiToolbarSave(waitForReload);
                    CSUtility.tempMethodForThreadSleep(1000);
                    chkSelectedLanguageData(hashMapTable, hashMapFooter,
                            hashKey);
                }
            }
        }
    }

    /**
     * This method check the caption and footer for that language
     * 
     * @param hashMapTable HashMap object contains table caption names
     * @param hashMapFooter HashMap object contains table footer names
     * @param hashKey String object contains key of hash map
     */
    private void chkSelectedLanguageData(HashMap<String, String> hashMapTable,
            HashMap<String, String> hashMapFooter, String hashKey) {
        String captionText = null;
        String footerText = null;
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        WebElement captionElement = browserDriver.findElement(
                By.xpath("//table[@class='CSFlexTable FlexTable']//caption"));
        CSUtility.waitForVisibilityOfElement(waitForReload, captionElement);
        captionText = captionElement.getText();
        WebElement footerElement = browserDriver.findElement(
                By.xpath("//div[@class='CSFlexTable FlexTableFooter']"));
        footerText = footerElement.getText();
        if (captionText.equals(hashMapTable.get(hashKey))) {
            CSLogger.info("Table Caption " + captionText + " is verified");
        } else {
            CSLogger.error("Table Caption " + captionText + " is not verified");
            Assert.fail("Table Caption " + captionText + " is  not verified");
        }
        if (footerText.equals(hashMapFooter.get(hashKey))) {
            CSLogger.info("Table Footer " + captionText + " is verified");
        } else {
            CSLogger.error("Table Footer " + captionText + " is not verified");
            Assert.fail("Table Footer " + captionText + " is not verified");
        }
    }

    /**
     * This convert the array into HashMap
     * 
     * @param language String object contains language name
     * @param languageData String object contains data for language
     * @return translationData HashMap contain array data
     */
    private HashMap<String, String> sendDataInHashMap(String language,
            String languageData) {
        String languageArray[] = language.split(",");
        String languageDataArray[] = languageData.split(",");
        HashMap<String, String> translationData = new HashMap<String, String>();
        for (int row = 0; row < languageArray.length; row++) {
            translationData.put(languageArray[row], languageDataArray[row]);
        }
        return translationData;
    }

    /**
     * This method send the text in table
     * 
     * @param hashMapTable HashMap object contains table caption names
     * @param hashMapFooter HashMap object contains table footer names
     */
    private void addDataInTranslationOption(
            HashMap<String, String> hashMapTable,
            HashMap<String, String> hashMapFooter) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement prarentCaption = browserDriver.findElement(By
                .xpath("//div[@id='title_Footer_sections::Table Caption']/.."));
        WebElement parentFooter = browserDriver.findElement(
                By.xpath("//div[@id='title_Footer_sections::Footer']/.."));
        if (prarentCaption.getAttribute("class").contains("section_closed")) {
            flexTablePage.clkOnSelectionTableCaption(waitForReload);
        }
        if (parentFooter.getAttribute("class").contains("section_closed")) {
            flexTablePage.clkOnSelectionFooter(waitForReload);
        }
        sendTextInTable(hashMapTable, "Table");
        sendTextInTable(hashMapFooter, "Footer");
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method send the text in table
     * 
     * @param hashMapData HashMap object contain table data
     * @param SectionName String object contains name of section in translation
     *            pane
     */
    private void sendTextInTable(HashMap<String, String> hashMapData,
            String SectionName) {
        String xpathSectionData = null;
        String xpathSectionText = null;
        String pathTable = null;
        WebElement languageElement = null;
        String languageName = null;
        WebElement textElement = null;
        if (SectionName.equals("Table")) {
            pathTable = "//div[@id='Footer_sections::Table Caption']";
        }
        if (SectionName.equals("Footer")) {
            pathTable = "//div[@id='Footer_sections::Footer']";
        }
        int row = 2;
        for (String hashKey : hashMapData.keySet()) {
            xpathSectionData = pathTable + "//li[" + row
                    + "]//div[1]//div[1]//input[1]";
            xpathSectionText = pathTable + "//li[" + row
                    + "]//div[1]//div[2]//input[1]";
            languageElement = browserDriver
                    .findElement(By.xpath(xpathSectionData));
            languageName = languageElement.getAttribute("value");
            if (languageName.equals(hashKey)) {
                textElement = browserDriver
                        .findElement(By.xpath(xpathSectionText));
                textElement.clear();
                textElement.sendKeys(hashMapData.get(hashKey));
            }
            row++;
        }
    }

    /**
     * This method verifies section in translation pane
     * 
     */
    private void verifyTranslationSection() {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        WebElement tableCaptionElement = browserDriver.findElement(By.xpath(
                "//div[@id='title_Footer_sections::Table Caption']//div[1]"));
        String tableCaptionSection = tableCaptionElement.getText();
        WebElement footerElement = browserDriver.findElement(
                By.xpath("//div[@id='title_Footer_sections::Footer']//div[1]"));
        String footersection = footerElement.getText();
        if (tableCaptionSection.contains("Table Caption")) {
            CSLogger.info("Table Caption Section name verified");
        } else {
            CSLogger.error("Table Caption Section name not verified");
            softasssert.fail("Table Caption Section name not verified");
        }
        if (footersection.contains("Footer")) {
            CSLogger.info("Footer Section name verified");
        } else {
            CSLogger.error("Footer Section name not verified");
            softasssert.fail("Footer Section name not verified");
        }
    }

    /**
     * This method traverses to created Flex Template
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     */
    private void goToCreatedFlexTemplate(String templateName,
            String templateType) {
        goToNodeFlexTablePreset();
        WebElement freeNode = browserDriver
                .findElement(By.linkText(templateType));
        CSUtility.waitForVisibilityOfElement(waitForReload, freeNode);
        freeNode.click();
        WebElement nameOfTemplate = browserDriver
                .findElement(By.linkText(templateName));
        nameOfTemplate.click();
        CSLogger.info("Clicked on Template");

    }

    /**
     * This method traverse to the preset node of flex table
     * 
     */
    private void goToNodeFlexTablePreset() {
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        flexTablePage.clkOnFlexTableNode(waitForReload);
        flexTablePage.clkOnPresetsNode(waitForReload);
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains TemplateName,TemplateType,Language,TableCaption,Footer,
     * TableLanguage,ContextLanguage
     * 
     * @return TranslationsOptionTestData
     */
    @DataProvider(name = "TranslationsOptionDataSheet")
    public Object[] TranslationsOption() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                TranslationsOptionTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        softasssert = new SoftAssert();
    }
}
