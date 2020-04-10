/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies searching operation with
 * multiple state of product
 * 
 * @author CSAutomation Team
 */
public class VerificationOfResultByMultipleSelectionTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private SearchPage                searchPage;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private SelectionDialogWindow     selectionDialogWindow;
    private IProductPopup             productPopUp;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private String                    multipleSelectionStateSheet = "MultipleSelectionState";
    private String                    drpdwnValue                 = "Change Workflow State";

    /**
     * This method verifies searching operation with multiple state of product
     * 
     * @param productFolder String object contains product folder name
     * @param productAndStates String object contains products and there states
     * @param searchData String object contains search data
     * @param comboBoxData String object contains name for combo box
     */
    @Test(dataProvider = "multipleSelectionState")
    public void testVerificationOfResultByMultipleSelection(
            String productFolder, String productAndStates, String searchData,
            String comboBoxData) {
        try {
            changeWorkflowStateOfProduct(productFolder, productAndStates);
            csPortalHeader.clkSearchHeader(waitForReload);
            TraversingForSearchModule.frameTraversingForCollectionsPopup(
                    waitForReload, browserDriver);
            preformSearchOpertionOnFacetedSearch(searchData);
            filterWithStates(productAndStates);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerificationOfResultBy"
                    + "MultipleSelection", e);
            Assert.fail("Automation Error : testVerificationOfResultBy"
                    + "MultipleSelection", e);
        }
    }

    /**
     * This method filter the search result via product states
     * 
     * @param productAndStates String object contains products and there states
     */
    private void filterWithStates(String productAndStates) {
        String[] arrayProductState = productAndStates.split(",");
        String[] arrayProduct = null;
        String treeCategory = searchPage.getSecCategories().getAttribute("aria-expanded");
        if (treeCategory.equals("true")) {
			searchPage.getSecCategories().click();
		}
        for (String productState : arrayProductState) {
            arrayProduct = productState.split(":");
            WebElement txtInputElement = browserDriver.findElement(By.xpath(
                    "//div[contains(text(),'State')]/..//input[@placeholder='Type or click here']"));
            CSUtility.scrollUpOrDownToElement(txtInputElement, browserDriver);
            sendTextToElement(txtInputElement, arrayProduct[1]);
            CSUtility.tempMethodForThreadSleep(2000);
            WebElement valueSuggession = browserDriver.findElement(By.xpath(
                    "//div[contains(text(),'- " + arrayProduct[1] + "')]"));
            valueSuggession.click();
            CSUtility.tempMethodForThreadSleep(3000);
            verifyFilterResult(arrayProduct[0]);
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement txtRemoveElement = browserDriver.findElement(By.xpath(
                    "//div[contains(text(),'State')]/..//span[contains(@class,"
                            + "'ms-close-btn glyphicon glyphicon-remove')]"));
            CSUtility.scrollUpOrDownToElement(txtRemoveElement, browserDriver);
            txtRemoveElement.click();
            CSLogger.info("Clicked on remove text.");
            CSUtility.tempMethodForThreadSleep(3000);
        }
    }

    /**
     * This method verifies the filter result
     * 
     * @param Products String object contains product name
     */
    private void verifyFilterResult(String Products) {
        String[] productArray = Products.split("/");
        List<String> productList = Arrays.asList(productArray);
        List<WebElement> resultElement = browserDriver.findElements(By.xpath(
                "//ul[@class='CSListView SearchCSListView multipleSelection']"
                        + "//li//div//div[3]//div[2]//div//div//h1"));
        List<String> productsName = new ArrayList<String>();
        for (WebElement resultText : resultElement) {
            CSUtility.scrollUpOrDownToElement(resultText, browserDriver);
            productsName.add(resultText.getText());
        }
        if (productsName.containsAll(productList)) {
            CSLogger.info("Search result is display successfully");
        } else {
            CSLogger.error("Search result is incorrect");
            Assert.fail("Search result is incorrect");
        }
    }

    /**
     * This method perform searching operation on faceted search
     * 
     * @param searchData String object contains search data
     */
    private void preformSearchOpertionOnFacetedSearch(String searchData) {
        sendTextToElement(searchPage.getSearchField(), searchData);
        searchPage.clkWebElement(waitForReload, searchPage.getBtnSearch());
        CSUtility.tempMethodForThreadSleep(5000);
        CSLogger.info("Perform search operation.");
    }

    /**
     * This method change the workflow state of product
     * 
     * @param productFolder String object contains product folder name
     * @param productAndStates String object contains products and there states
     */
    private void changeWorkflowStateOfProduct(String productFolder,
            String productAndStates) {
        goToProductListView(productFolder);
        String[] arrayProductState = productAndStates.split(",");
        String[] arrayProduct = null;
        String selectValue = null;
        for (int index = 0; index < arrayProductState.length; index++) {
            arrayProduct = arrayProductState[index].split(":");
            selectProductFromList(arrayProduct[0]);
            TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                    waitForReload, browserDriver);
            Select drpdwnState = new Select(
                    selectionDialogWindow.getDrpDwnStateID());
            List<String> drpdwnOption = getOptionsInDropDown(drpdwnState);
            for (String option : drpdwnOption) {
                if (option.contains(arrayProduct[1])) {
                    selectValue = option;
                }
            }
            drpdwnState.selectByVisibleText(selectValue);
            productPopUp.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            // waitForReload
            // .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
            // iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
            selectProductFromList(arrayProduct[0]);
        }
        CSLogger.info("Change the workflow state of product");
    }

    /**
     * This method get the all the option in the list from drop down
     * 
     * @param dropdown Select object contains object of drop down
     * @return options Contains list of option present in drop down
     */
    private List<String> getOptionsInDropDown(Select dropdown) {
        List<String> options = new ArrayList<String>();
        List<WebElement> drpdwnElements = dropdown.getOptions();
        for (WebElement element : drpdwnElements) {
            options.add(element.getText());
        }
        return options;
    }

    /**
     * This method select the product from list
     * 
     * @param products String object contains product name
     */
    private void selectProductFromList(String products) {
        int column = 0;
        int row = 0;
        String txtTitleBar = null;
        List<String> lblInTable = new ArrayList<String>();
        List<WebElement> titleBarElement = browserDriver.findElements(
                By.xpath("//table[@class='hidewrap CSAdminList']//tbody//tr"
                        + "[@class='TitleBar']//th"));
        for (int index = 0; index < titleBarElement.size(); index++) {
            txtTitleBar = titleBarElement.get(index).getText();
            if (txtTitleBar.equals("Label")) {
                column = index + 1;
                break;
            }
        }
        List<WebElement> txtShareElement = browserDriver.findElements(By
                .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr//td["
                        + column + "]"));
        for (WebElement shareText : txtShareElement) {
            lblInTable.add(shareText.getText());
        }
        String[] productArray = products.split("/");
        for (String productName : productArray) {
            row = lblInTable.indexOf(productName) + 3;
            WebElement chkbox = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']//tbody//tr["
                            + row + "]//td[1]//input"));
            CSUtility.waitForVisibilityOfElement(waitForReload, chkbox);
            chkbox.click();
        }
        Select drpdwnbottom = new Select(browserDriver
                .findElement(By.xpath("//select[@id='massUpdateSelector']")));
        drpdwnbottom.selectByVisibleText(drpdwnValue);
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Clicked on Change work flow state.");
    }

    /**
     * This method traverse to the setting configuration of product folder
     * 
     * @param productFolder String object contains product folder name
     */
    private void goToProductListView(String productFolder) {
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productElement = browserDriver
                .findElement(By.linkText(productFolder));
        productElement.click();
        productElement.click();
        CSLogger.info("Clicked on product Folder.");
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        if (csGuiToolbarHorizontalInstance.getBtnSwitchToStudioList()
                .isDisplayed()) {
            csGuiToolbarHorizontalInstance.getBtnSwitchToStudioList().click();
            CSLogger.info("switched to studio list view");
        } else {
            CSLogger.info("Already switched to studio list view");
        }
        CSUtility.tempMethodForThreadSleep(1000);
        selectionDialogWindow.clkOnListSubFolder(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
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
     * This data provider returns the sheet data to run the test case which
     * contains Search Data,Class Name,Search Result
     * 
     * @return multipleSelectionStateSheet
     */
    @DataProvider(name = "multipleSelectionState")
    public Object[] multipleSelectionState() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                multipleSelectionStateSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        searchPage = new SearchPage(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        selectionDialogWindow = new SelectionDialogWindow(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }
}
