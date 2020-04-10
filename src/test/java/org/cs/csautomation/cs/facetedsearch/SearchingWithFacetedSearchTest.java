/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies searching operation with
 * faceted search
 * 
 * @author CSAutomation Team
 */
public class SearchingWithFacetedSearchTest extends AbstractTest {

	private CSPortalHeader csPortalHeader;
	private WebDriverWait waitForReload;
	private SearchPage searchPage;
	private String searchingWithFacetedSearchSheet = "SearchingWithFacetedSearch";

	/**
	 * This method verifies searching operation with faceted search
	 * 
	 * @param searchData
	 *            String object contains search data
	 * @param comboBoxData
	 *            String object contains share folder name
	 * @param filterData
	 *            String object contains attribute data to filter
	 * @param searchResult
	 *            String object contains search result
	 * @param filterResult
	 *            String object contains filter result
	 * @param numericEntries
	 *            String object contains slider entery
	 * @param entriesResult
	 *            String object contains filter result
	 */
	@Test(priority = 1, dataProvider = "searchingWithFacetedSearch")
	public void testSearchingWithFacetedSearch(String searchData, String comboBoxData, String filterData,
			String searchResult, String filterResult, String numericEntries, String entriesResult) {
		try {
			csPortalHeader.clkSearchHeader(waitForReload);
			TraversingForSearchModule.frameTraversingForCollectionsPopup(waitForReload, browserDriver);
			preformSearchOpertionOnFacetedSearch(searchData, comboBoxData);
			verifySearchResult(comboBoxData, searchResult);
			filterSearchData(filterData);
			verifySearchProductList(filterResult);
		} catch (Exception e) {
			CSLogger.debug("Automation Error : testSearchingWithFacetedSearch", e);
			Assert.fail("Automation Error : testSearchingWithFacetedSearch", e);
		}
	}

	/**
	 * This method verifies searching operation with faceted search
	 * 
	 * @param searchData
	 *            String object contains search data
	 * @param comboBoxData
	 *            String object contains share folder name
	 * @param filterData
	 *            String object contains attribute data to filter
	 * @param searchResult
	 *            String object contains search result
	 * @param filterResult
	 *            String object contains filter result
	 * @param numericEntries
	 *            String object contains slider entery
	 * @param entriesResult
	 *            String object contains filter result
	 */
	@Test(priority = 2, dataProvider = "searchingWithFacetedSearch")
	public void testValidationOfNumericEntriesWithSlider(String searchData, String comboBoxData, String filterData,
			String searchResult, String filterResult, String numericEntries, String entriesResult) {
		try {
		        browserDriver.navigate().refresh();
		        CSUtility.tempMethodForThreadSleep(2000);
			csPortalHeader.clkSearchHeader(waitForReload);
			TraversingForSearchModule.frameTraversingForCollectionsPopup(waitForReload, browserDriver);
			preformSearchOpertionOnFacetedSearch(searchData, comboBoxData);
			filterWithNumericSlider(numericEntries);
			verifySearchProductList(entriesResult);
		} catch (Exception e) {
			CSLogger.debug("Automation Error : testValidationOfNumericEntriesWithSlider", e);
			Assert.fail("Automation Error : testValidationOfNumericEntriesWithSlider", e);
		}
	}

	/**
	 * This method filter the search result via numeric slider
	 * 
	 * @param numericEntries
	 *            String object contains slider entery
	 */
	private void filterWithNumericSlider(String numericEntries) {
		String[] enteries = numericEntries.split(":");
		WebElement slider = browserDriver.findElement(By.xpath(
                        "//div[contains(text(),'" + enteries[0] + "')]/../div[3]/div/div/div[1]/input"));
		CSUtility.scrollUpOrDownToElement(slider, browserDriver);
		CSUtility.waitForVisibilityOfElement(waitForReload, slider);
		CSUtility.tempMethodForThreadSleep(1000);
		slider.click();
		slider.sendKeys(Keys.CONTROL + "a");
		slider.sendKeys(Keys.DELETE);
		slider.sendKeys(enteries[1]);
		WebElement attributename = browserDriver.findElement(By.xpath("//div[contains(text(),'" + enteries[0] + "')]"));
		attributename.click();
		CSUtility.tempMethodForThreadSleep(1000);
	}

	/**
	 * This method filter the search result
	 * 
	 * @param filterData
	 *            String object contains attribute data to filter
	 */
	private void filterSearchData(String filterData) {
		String treeCategory = searchPage.getSecCategories().getAttribute("aria-expanded");
		if (treeCategory.equals("false")) {
			searchPage.getSecCategories().click();
		}
		WebElement inputTextElement = browserDriver.findElement(By.xpath("//input[@placeholder='Type or click here']"));
		sendTextToElement(inputTextElement, filterData);
		CSUtility.tempMethodForThreadSleep(3000);
		WebElement valueSuggession = browserDriver
				.findElement(By.xpath("//div[contains(text(),'" + filterData + "')]"));
		valueSuggession.click();
		CSUtility.tempMethodForThreadSleep(1000);
	}

	/**
	 * This method verifies search result
	 * 
	 * @param comboBoxData
	 *            String object contains share folder name
	 * @param searchResult
	 *            String object contains search result
	 */
	private void verifySearchResult(String comboBoxData, String searchResult) {
		if (searchPage.getResultsMetaText().isDisplayed()) {
			CSLogger.info("Search result is displayed");
		} else {
			CSLogger.error("Search result is not displayed");
			Assert.fail("Search result is not displayed");
		}
		((JavascriptExecutor) browserDriver).executeScript("arguments[0].scrollIntoView(true);",
				searchPage.getSecCategories());
		if (searchPage.getSecCategories().isDisplayed()) {
			CSLogger.info("Categories is displayed");
		} else {
			CSLogger.error("Categories is not displayed");
			Assert.fail("Categories is not displayed");
		}
		WebElement combobox = browserDriver.findElement(By.xpath("//div[contains(text(),'" + comboBoxData + "')]"));
		Assert.assertNotNull(combobox, "ComboBox not verified");
		verifySearchProductList(searchResult);
	}

	/**
	 * This method verifies list of product after performing search operation
	 * 
	 * @param searchResult
	 *            String object contains search result
	 */
	private void verifySearchProductList(String searchResult) {
		String[] searchResultArray = searchResult.split(",");
		List<String> searchResultList = Arrays.asList(searchResultArray);
		List<WebElement> resultElement = browserDriver
				.findElements(By.xpath("//ul[@class='CSListView SearchCSListView multipleSelection']"
						+ "//li//div//div[3]//div[2]//div//div//h1"));
		List<String> productsName = new ArrayList<String>();
		for (WebElement resultText : resultElement) {
			CSUtility.scrollUpOrDownToElement(resultText, browserDriver);
			productsName.add(resultText.getText());
		}
		if (productsName.containsAll(searchResultList)) {
			CSLogger.info("Search result is display successfully");
		} else {
			CSLogger.error("Search result is incorrect");
			Assert.fail("Search result is incorrect");
		}
	}

	/**
	 * This method perform searching operation on faceted search
	 * 
	 * @param searchData
	 *            String object contains search data
	 * @param comboBoxData
	 *            String object contains share folder name
	 */
	private void preformSearchOpertionOnFacetedSearch(String searchData, String comboBoxData) {
		sendTextToElement(searchPage.getSearchField(), searchData);
		sendTextToElement(searchPage.getTxtBoxSearchComboBox(), comboBoxData);
		CSUtility.tempMethodForThreadSleep(4000);
		CSUtility.waitForElementToBeClickable(waitForReload,
				browserDriver.findElement(By.xpath("//div[contains(text(),'" + comboBoxData + "')]")));
		WebElement suggestionElement = browserDriver
				.findElement(By.xpath("//div[contains(text(),'" + comboBoxData + "')]"));
		suggestionElement.click();
		searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
		CSUtility.tempMethodForThreadSleep(3000);
	}

	/**
	 * This method send the text data to the WebElement
	 * 
	 * @param txtElement
	 *            WebElement object contains element of text input
	 * @param text
	 *            String object contains text data
	 */
	private void sendTextToElement(WebElement txtElement, String text) {
		CSUtility.waitForVisibilityOfElement(waitForReload, txtElement);
		txtElement.clear();
		txtElement.sendKeys(text);
		CSLogger.info(text + " data is sent.");
	}

	/**
	 * This data provider returns the sheet data to run the test case which
	 * contains Search Data,Combo box Data,Filter data,Search Result, Filter
	 * Result
	 * 
	 * @return searchingWithFacetedSearchSheet
	 */
	@DataProvider(name = "searchingWithFacetedSearch")
	public Object[] searchingWithFacetedSearch() {
		return DrivenScript.readSheetData(config.getExcelSheetPath("facetedSearchTestCases"),
				searchingWithFacetedSearchSheet);
	}

	/**
	 * This method initializes all the resources required to drive the test case
	 * 
	 */
	@BeforeClass
	private void initializeResources() {
		waitForReload = new WebDriverWait(browserDriver, 30);
		csPortalHeader = new CSPortalHeader(browserDriver);
		searchPage = new SearchPage(browserDriver);
	}
}
