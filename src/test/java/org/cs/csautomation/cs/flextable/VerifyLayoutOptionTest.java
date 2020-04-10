/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies Layout Option test uses.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyLayoutOptionTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeader;
    private FrameLocators          locator;
    private Actions                action;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private FlexTablePage          flexTablePage;
    private String                 layoutOptionTestData = "LayoutOption";

    /**
     * This method verifies Layout Option test uses.
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     * @param style String object contains style of table text
     * @param tableWidth String object contains width of table
     * @param tableHeight String object contains height of table
     */
    @Test(dataProvider = "LayoutOptionDataSheet")
    public void testVerifyLayoutOption(String templateName, String tempType,
            String style, String tableWidth, String tableHeight) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            goToCreatedFlexTemplate(templateName, tempType);
            setTextStyle(style);
            CSUtility.tempMethodForThreadSleep(1000);
            verifyTextStyle(style);
            setWidthAndHeightOfTable(tableWidth, tableHeight);
            CSUtility.tempMethodForThreadSleep(1000);
            verifyWidthAndHeightOfTable(tableWidth, tableHeight);
            CSLogger.info("verify layout option test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerifyLayoutOption", e);
            Assert.fail("Automation Error : testVerifyLayoutOption", e);
        }
    }

    /**
     * This method verifies weight and height of table.
     * 
     * @param tableWidth String object contains width of table
     * @param tableHeight String object contains height of table
     */
    private void verifyWidthAndHeightOfTable(String tableWidth,
            String tableHeight) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        String width = null;
        String height = null;
        WebElement widthElement = browserDriver.findElement(By.xpath(
                "//table[@class='CSFlexTable FlexTable']//colgroup//col[1]"));
        width = getStyleAttributeValue(widthElement);
        WebElement heightElement = browserDriver.findElement(By.xpath(
                "//table[@class='CSFlexTable FlexTable']//tbody//tr[1]"));
        height = getStyleAttributeValue(heightElement);
        if (width.contains(tableWidth)) {
            CSLogger.info("Verification pass for width =" + width);
        } else {
            CSLogger.error("Verification fail for width =" + width);
            Assert.fail("Verification fail for width =" + width);
        }
        if (height.contains(tableHeight)) {
            CSLogger.info("Verification pass for height =" + height);
        } else {
            CSLogger.error("Verification fail for height =" + height);
            Assert.fail("Verification fail for height =" + height);
        }
    }

    /**
     * This method returns
     * 
     * @param element contains webelement to get attribute
     * @return attributeValue attribute value of the element
     */
    private String getStyleAttributeValue(WebElement element) {
        String attributeValue = element.getAttribute("style");
        return attributeValue;
    }

    /**
     * This method set width and height of table
     * 
     * @param tableWidth String object contains width of table
     * @param tableHeight String object contains height of table
     */
    private void setWidthAndHeightOfTable(String tableWidth,
            String tableHeight) {
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.setWidthOfTable(waitForReload, tableWidth);
        flexTablePage.setHeightOfTable(waitForReload, tableHeight);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method verifies style of text in table.
     * 
     * @param style String object contains style of table text
     */
    private void verifyTextStyle(String style) {
        String textStyle[] = style.split(",");
        String textAttribute = null;
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmRight()));
        WebElement textElemnt = browserDriver.findElement(By.xpath(
                "//table[@class='CSFlexTable FlexTable']//tbody//tr[1]//td[1]"));
        textAttribute = textElemnt.getAttribute("class");
        for (String styleName : textStyle) {
            if (textAttribute.contains(styleName)) {
                CSLogger.info("Verification Pass for style " + styleName);
            } else {
                CSLogger.error("Verification fail for style " + styleName);
                Assert.fail("Verification fail for style " + styleName);
            }
        }
    }

    /**
     * This method set the style of text in table
     * 
     * @param style String object contains style of table text
     */
    private void setTextStyle(String style) {
        String styleNameArray[] = style.split(",");
        ArrayList<String> styleNameList = new ArrayList<String>(
                Arrays.asList(styleNameArray));
        String lowercaseStyleName = null;
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnLayoutPane(waitForReload);
        WebElement layoutStyle = browserDriver.findElement(By.xpath(
                "//select[contains(@id,'FlexpresetCols16-1NotSelected')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, layoutStyle);
        List<WebElement> listStyle = layoutStyle
                .findElements(By.tagName("option"));
        for (WebElement styleElement : listStyle) {
            lowercaseStyleName = styleElement.getText().toLowerCase();
            if (styleNameList.contains(lowercaseStyleName)) {
                action.doubleClick(styleElement).build().perform();
            }
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method traverses to created Flex Template
     * 
     * @param templateName String object contains template name
     * @param tempType String object contains template type
     */
    private void goToCreatedFlexTemplate(String templateName, String tempType) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement freeNode = browserDriver.findElement(By.linkText(tempType));
        clkElement(freeNode);
        WebElement nameOfTemplate = browserDriver
                .findElement(By.linkText(templateName));
        clkElement(nameOfTemplate);
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnDataPane(waitForReload);
    }

    /**
     * This method clicks on input element
     * 
     * @param element contains web Element to be clicked
     */
    public void clkElement(WebElement element) {
        CSUtility.waitForElementToBeClickable(waitForReload, element);
        element.click();
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains TemplateName,TemplateType,Style,TableWidth,TableHeight
     * 
     * @return layoutOptionTestData
     */
    @DataProvider(name = "LayoutOptionDataSheet")
    public Object[] layoutOptionTemplate() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                layoutOptionTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        action = new Actions(browserDriver);
    }
}
