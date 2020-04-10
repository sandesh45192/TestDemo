/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
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
 * This class contains the test methods which verifies attribute mapping in
 * class
 * 
 * @author CSAutomation Team
 */
public class VerificationOfSearchResultWithOROperatorTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private PimStudioSettingsNode  pimStudioSettingsNodeInstance;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSGuiDialogContentId   csGuiDialogContentIdInstance;
    private FrameLocators          iframeLocatorsInstance;
    private CSPortalHeader         csPortalHeader;
    private SearchPage             searchPage;
    private String                 searchVerificationOROperatorSheet = "SearchVerificationOROperator";
    private String                 concatenatorValue                 = "OR Concatenator";

    /**
     * This method verifies attribute mapping in class.
     * 
     * @param className String object contains class name
     * @param attrName String object contains attribute name
     * @param searchData String object contains search data
     * @param comboBoxData String object contains name for combo box
     * @param searchResult String object contains search result
     */
    @Test(dataProvider = "searchVerificationOROperator")
    public void testVerificationOfSearchResultWithOROperator(String className,
            String attrName, String searchData, String comboBoxData,
            String filterData, String searchResult) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            attributeMappingInClass(className, attrName);
            csPortalHeader.clkSearchHeader(waitForReload);
            TraversingForSearchModule.frameTraversingForCollectionsPopup(
                    waitForReload, browserDriver);
            preformSearchOpertionOnFacetedSearch(searchData, comboBoxData);
            filterSearchData(filterData);
            verifyFilterResult(searchResult);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerificationOfSearchResult"
                    + "WithOROperator", e);
            Assert.fail("Automation Error : testVerificationOfSearchResult"
                    + "WithOROperator", e);
        }
    }

    /**
     * This method verifies the filter result
     * 
     * @param searchResult String object contains search result
     */
    private void verifyFilterResult(String searchResult) {
        String[] resultArray = searchResult.split(",");
        List<String> resultList = Arrays.asList(resultArray);
        List<WebElement> resultElement = browserDriver.findElements(By.xpath(
                "//ul[@class='CSListView SearchCSListView multipleSelection']"
                        + "//li//div//div[3]//div[2]//div//div//h1"));
        List<String> productsName = new ArrayList<String>();
        for (WebElement resultText : resultElement) {
            CSUtility.scrollUpOrDownToElement(resultText, browserDriver);
            productsName.add(resultText.getText());
        }
        if (productsName.containsAll(resultList)) {
            CSLogger.info("Search result is display successfully");
        } else {
            CSLogger.error("Search result is incorrect");
            Assert.fail("Search result is incorrect");
        }
    }

    /**
     * This method filter the search result
     * 
     * @param filterData String object contains attribute data to filter
     */
    private void filterSearchData(String filterData) {
        String[] dataArray = filterData.split(":");
        String[] colorArray = dataArray[1].split(",");
        WebElement chkboxColor = null;
        for (String colorValue : colorArray) {
            chkboxColor = browserDriver.findElement(
                    By.xpath("//div[contains(text(),'" + dataArray[0]
                            + "')]/..//label[contains" + "(text(),'"
                            + colorValue + "')]/preceding-sibling::input"));
            CSUtility.scrollUpOrDownToElement(chkboxColor, browserDriver);
            chkboxColor.click();
            CSLogger.info("Click check box of color " + colorValue);
            waitForReload.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[@class='loadingIndicator']")));
        }
        CSLogger.info("Filter search result by color");
    }

    /**
     * This method perform searching operation on faceted search
     * 
     * @param searchData String object contains search data
     * @param comboBoxData String object contains share folder name
     */
    private void preformSearchOpertionOnFacetedSearch(String searchData,
            String comboBoxData) {
        sendTextToElement(searchPage.getSearchField(), searchData);
        sendTextToElement(searchPage.getTxtBoxSearchComboBox(), comboBoxData);
        WebElement suggestionElement = browserDriver.findElement(
                By.xpath("//div[contains(text(),'" + comboBoxData + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, suggestionElement);
        suggestionElement.click();
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method send the text data to the WebElement
     * 
     * @param txtElement WebElement object contains element of text input
     * @param text String object contains text data
     */
    private void sendTextToElement(WebElement txtElement, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, txtElement);
        txtElement.clear();
        txtElement.sendKeys(text);
        CSLogger.info(text + " data is sent.");
    }

    /**
     * This method map attribute in class.
     * 
     * @param className String object contains class name
     * @param attrName String object contains attribute name
     */
    private void attributeMappingInClass(String className, String attrName) {
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        WebElement elementOfClass = browserDriver
                .findElement(By.linkText(className));
        elementOfClass.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarRefresh(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiDialogContentIdInstance.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getTabAttributeMapping());
        selectAttributeAndSendData(attrName);
    }

    /**
     * This method select attribute and add data in it.
     * 
     * @param attrName String object contains attribute name
     */
    private void selectAttributeAndSendData(String attrName) {
        String[] attrArray = attrName.split(",");
        WebElement attrElement = null;
        for (String nameOfAttribute : attrArray) {
            CSUtility.traverseToAttributeMapping(waitForReload, browserDriver,
                    iframeLocatorsInstance);
            attrElement = browserDriver.findElement(By
                    .xpath("//td[contains(text(),'" + nameOfAttribute + "')]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, attrElement);
            attrElement.click();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.traverseToEditWindowAttributeMapping(waitForReload,
                    browserDriver, iframeLocatorsInstance);
            Select facetedConcatenator = new Select(csGuiDialogContentIdInstance
                    .getDrpDwnMappingFacetedConcatenator());
            facetedConcatenator.selectByVisibleText(concatenatorValue);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
        }
        CSLogger.info("Faceted concatenator of all attribute are set to 'OR' "
                + "Operator.");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Class Name,Attribute Name,Pane Title,Section Title
     * 
     * @return searchVerificationOROperatorSheet
     */
    @DataProvider(name = "searchVerificationOROperator")
    public Object[] searchVerificationOROperator() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                searchVerificationOROperatorSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        iframeLocatorsInstance = new FrameLocators(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        searchPage = new SearchPage(browserDriver);
    }
}
