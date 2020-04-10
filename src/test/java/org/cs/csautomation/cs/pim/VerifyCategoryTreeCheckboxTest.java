/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioChannelsNode;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to verify product categories & channel
 * categories and assign product to views
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyCategoryTreeCheckboxTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private IClassPopup               classPopup;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private IProductPopup             productPopup;
    private PimStudioChannelsNode     pimStudioChannelsNodeInstance;
    private String                    categoryTreeSheet = "CategoryTree";
    private SoftAssert                softAssertion;
    private IMoreOptionPopup          moreOptionPopup;
    private CSPopupDivPim             csPopupDivPimInstance;

    /**
     * This test method which enables the Show In Category Tree checkbox for
     * this test method to run the class should be already present under classes
     * tree the test priority is 1 and dataprovider categoryTreeCheckboxTestData
     * is used.
     * 
     * @param productFolderName string parameter contains name of the product
     *            folder
     * @param className string contains name of the class
     * @param attributeList string contains attribute names which are assigned
     *            to the class
     * @param viewName string parameter which contains name of already created
     *            view
     * @param classNotassignedToViewName string class name which is not assigned
     *            to any view and this class is created during test run
     */
    @Test(priority = 1, dataProvider = "categoryTreeCheckboxTestData")
    public void testVerifyCategoryTreeCheckbox(String productFolderName,
            String className, String attributeList, String viewName,
            String classNotassignedToViewName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 30);
            performUsecaseEnableShowCategoryTreeCheckbox(className);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testVerifyCategoryTreeCheckbox",
                    e);
            Assert.fail(
                    "Automation error in test method : testVerifyCategoryTreeCheckbox",
                    e);
        }
    }

    /**
     * This method enables the Show In Category Tree Checkbox in right section
     * pane
     * 
     * @param className this is a string parameter containing name of class
     */
    public void performUsecaseEnableShowCategoryTreeCheckbox(String className) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        traverseTillSettingsClassNode();
        CSUtility.scrollUpOrDownToElement(getNewlyCreatedClass(className),
                browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.rightClickTreeNode(waitForReload,
                getNewlyCreatedClass(className), browserDriver);
        classPopup.selectPopupDivMenu(waitForReload,
                classPopup.getCsGuiPopupSubMenuEdit(), browserDriver);
        enableCategoryTreeCheckbox();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method gets the already created class
     * 
     * @param className string parameter contains name of the class
     * @return
     */
    public WebElement getNewlyCreatedClass(String className) {
        waitForReload.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By.linkText(className)));
        return browserDriver.findElement(By.linkText(className));
    }

    /**
     * This method double clicks on already created class
     * 
     * @param className string parameter contains name of the class
     */
    public void doubleClickOnNewlyCreatedClass(String className) {
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogLeftSection(
                waitForReload, browserDriver);
        Actions doubleClickAction = new Actions(browserDriver);
        doubleClickAction.doubleClick(getNewlyCreatedClass(className))
                .perform();
    }

    /**
     * This method performs action of enabling the checkbox of Show In Category
     * Tree
     */
    public void enableCategoryTreeCheckbox() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentIdInstance
                .checkCbPdmarticleconfigurationIsCategoryClass(waitForReload);
    }

    /**
     * Disables the checkbox of Show In Category Tree
     */
    public void disableCategoryTreeCheckbox() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentIdInstance
                .uncheckCbPdmarticleconfigurationIsCategoryClass(waitForReload);
    }

    /**
     * This is a verification method and contains assert statement to validate
     * the testVerifyCategoryTreeCheckbox test method
     */
    @Test(priority = 2, dependsOnMethods = "testVerifyCategoryTreeCheckbox")
    public void testVerifyCategoryTreeCheckboxEnabledOrNot() {
        try {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            String categoryTreeCheckboxValue = csGuiDialogContentIdInstance
                    .getcbPdmarticleconfigurationIsCategoryClass()
                    .getAttribute("class");
            Assert.assertEquals(categoryTreeCheckboxValue,
                    "CSGuiEditorCheckboxContainer On Enabled GuiEditorCheckbox");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testVerifyCategoryTreeCheckboxEnabledOrNot",
                    e);
            Assert.fail(
                    "Automation error in test method : testVerifyCategoryTreeCheckboxEnabledOrNot",
                    e);
        }
    }

    /**
     * Clicks on Pim Classes Node under the settings
     */
    public void traverseTillSettingsClassNode() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
    }

    /**
     * This test method verifies product categories and also assigns class to
     * product folder via Editor window
     * 
     * @param productFolderName this string parameter contains name of the
     *            product folder
     * @param className this string parameter contains name of the class
     * @param attributeList this parameter contains attribute names which are
     *            assigned to already present class under classes tree
     * @param viewName this is a string parameter contains name of the view
     *            which should be already created and present under channels
     *            node tree
     * @param classNotassignedToViewName this is a class name which is not
     *            assigned to any view and this class is created during test run
     */
    @Test(priority = 3, dataProvider = "categoryTreeCheckboxTestData")
    public void testVerifyProductCategories(String productFolderName,
            String className, String attributeList, String viewName,
            String classNotassignedToViewName) {
        try {
            checkClassesInProductCategoriesTreeView(className);
            checkNonVisibilityOfProductsInAdjoiningPane();
            assignClassToProductFolder(productFolderName, className,
                    attributeList);
            verifyProductsInListView(className);
            nonVisibiltyOfClassesInTreeView(className);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testVerifyProductCategories",
                    e);
            Assert.fail(
                    "Automation error in test method : testVerifyProductCategories",
                    e);
        }
    }

    /**
     * This test checks whether the Products which are assigned to respective
     * class are displayed in list view in the adjoining pane. when we select
     * the product whether it opens the new window with Product details.
     * 
     * @param className this string parameter contains name of the class
     */
    public void verifyProductsInListView(String className) {
        try {
            checkClassesInProductCategoriesTreeView(className);
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            String windowHandleBeforeProductDetails = browserDriver
                    .getWindowHandle();
            setProductsViewToMediumPreviewImages();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            String classNameXpath = "//div[contains(text(),'" + className
                    + "')]";
            CSUtility.scrollUpOrDownToElement(
                    browserDriver.findElement(By.xpath(classNameXpath)),
                    browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            waitForReload.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath(classNameXpath)));
            browserDriver.findElement(By.xpath(classNameXpath)).click();
            /*
             * This sleep is mandatory because the product needs to open before
             * switching to that product
             */
            CSUtility.tempMethodForThreadSleep(2000);
            switchToCurrentWindowHandle();
            browserDriver.close();
            browserDriver.switchTo().window(windowHandleBeforeProductDetails);
            CSLogger.info("Clicked on product from list view");
        } catch (Exception e) {
            CSLogger.error("Product is not listed in adjoining pane", e);
            Assert.fail("Product is not listed in adjoining pane");
        }
    }

    /**
     * This methods checks for the classes listed under product Categories
     * 
     * @param className string parameter contains name of the class
     */
    public void checkClassesInProductCategoriesTreeView(String className) {
        // This sleep is mandatory because it waits for loading that product
        CSUtility.tempMethodForThreadSleep(5000);
        tabbedPaneNorthInstance.clkOnProductCategoriesTab(waitForReload);
        clkOnProductCategoriesClasses(className);
    }

    /**
     * Clicks on product categories tab and checks whether classes for which the
     * Show In Category Tree is on is listed or not
     * 
     * @param className this string parameter contains name of the class
     */
    public void clkOnProductCategoriesClasses(String className) {
        try {
            tabbedPaneNorthInstance
                    .traverseTillProductCategoriesClassList(waitForReload);
            clkOnNewlyCreatedClass(className);
            CSUtility.tempMethodForThreadSleep(1000);
        } catch (Exception e) {
            CSLogger.error(
                    "Class for which Show In Category Tree checkbox is on "
                            + "is not displayed in tree view",
                    e);
            Assert.fail(
                    "Class for which Show In Category Tree checkbox is on is "
                            + "not displayed in tree view  ",
                    e);
        }
    }

    /**
     * This method performs action of clicking on class using the linktext of
     * class name
     * 
     * @param className this string parameter contains name of the class
     */
    public void clkOnNewlyCreatedClass(String className) {
        getNewlyCreatedClass(className).click();
        CSLogger.info("Clicked on already present class");
    }

    /**
     * This method checks for the class in which no products are assigned should
     * not be listed in adjoining pane
     */
    public void checkNonVisibilityOfProductsInAdjoiningPane() {
        setProductsViewToMediumPreviewImages();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='CSAdminList']")));
        WebElement productInfoTable = browserDriver
                .findElement(By.xpath("//table[@class='CSAdminList']"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) productInfoTable
                .findElements(
                        By.xpath("//table[@class='CSAdminList']/tbody[1]/tr"));
        Integer totalRowsForNoProductsAssignedToClass = new Integer(
                rows.size());
        if (totalRowsForNoProductsAssignedToClass.equals(2)) {
            CSLogger.info(
                    "Verification Passed for the class  in which no products "
                            + "are assigned are not  listed in adjoining pane");
        } else {
            CSLogger.error(
                    "Verification Failed Products are listed in adjoining "
                            + "pane which is Error");
            Assert.fail("Products are listed in adjoining pane which is Error");
        }
    }

    /**
     * Performs action of double clicking on already present product folder
     * under products tree
     * 
     * @param newlyCreatedProductName this is a string parameter which contains
     *            name of the product folder
     */
    public void
            doubleClickOnNewlyCreatedProduct(String newlyCreatedProductName) {
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.linkText(newlyCreatedProductName)));
        WebElement newlyCreatedProduct = browserDriver
                .findElement(By.linkText(newlyCreatedProductName));
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(newlyCreatedProduct).perform();
    }

    /**
     * Disables Show In Category Tree for the specified class
     * 
     * @param className this string parameter contains name of the class
     */
    public void
            performUsecaseDisableShowCategoryTreeCheckbox(String className) {
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                getNewlyCreatedClass(className), browserDriver);
        classPopup.selectPopupDivMenu(waitForReload,
                classPopup.getCsGuiPopupSubMenuEdit(), browserDriver);
        disableCategoryTreeCheckbox();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This methods performs action to check that After Show In Category Tree
     * checkbox is disabled the class is not listed under Tree View
     * 
     * @param className this string parameter contains name of the class
     */
    public void nonVisibiltyOfClassesInTreeView(String className) {
        performUsecaseDisableShowCategoryTreeCheckbox(className);
        tabbedPaneNorthInstance.clkOnProductCategoriesTab(waitForReload);
        browserDriver.navigate().refresh();
        traverseToProductCategoriesTreeViewFrames(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//span[@id='0div0']")));
        WebElement viewInfoTable = browserDriver
                .findElement(By.xpath("//span[@id='0div0']"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) viewInfoTable
                .findElements(By.xpath("//a[@id]/span"));
        for (WebElement row : rows) {
            if (row.getText().equals(className)) {
                CSLogger.error(
                        "After Show In Category Tree checkbox is disabled the "
                                + "class should not be listed under Tree View of"
                                + " product categories still the class "
                                + "displays under tree view which is error");
                softAssertion
                        .fail("After Show In Category Tree checkbox is disabled the "
                                + "class should not be listed under Tree View of"
                                + " product categories still the class "
                                + "displays under tree view which is error");
            }
        }
    }

    /**
     * 
     * @param productFolderName this string parameter contains name of the
     *            product folder
     * @param className this string contains name of the class
     * @param attributeList this parameter contains attribute names which are
     *            assigned to the class
     * @param viewName this is a string parameter which contains name of already
     *            created view
     * @param classNotassignedToViewName this is a class name which is not
     *            assigned to any view and this class is created during test run
     */
    @Test(priority = 4, dataProvider = "categoryTreeCheckboxTestData")
    public void verifyChannnelCategoriesTest(String productFolderName,
            String className, String attributeList, String viewName,
            String classNotassignedToViewName) {
        try {
            assignClassToView(viewName, className);
            verifyNonVisibilityOfClassForDiabledShowCategoryTree(className);
            performUsecaseEnableShowCategoryTreeCheckbox(className);
            channelCategoriesTreeViewClasses(className, viewName);
            verifyClassNotAssignedToView(classNotassignedToViewName);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : verifyChannnelCategoriesTest",
                    e);
            Assert.fail(
                    "Automation error in test method : verifyChannnelCategoriesTest",
                    e);
        }
    }

    /**
     * This method assign class to view and also performs verification whether
     * the class was assigned to the view or not
     * 
     * @param viewName this is a string parameter which contains name of already
     *            present view under channel node
     * @param className this is string parameter that contains name of the class
     */
    public void assignClassToView(String viewName, String className) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioChannelsNodeInstance.traverseToPimChannelsNode(waitForReload);
        pimStudioChannelsNodeInstance.clkBtnPimChannelsNode(waitForReload);
        doubleClickOnView(viewName);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentIdInstance
                .clkPdmArticleStructureClassMappingCsReferenceDiv(
                        waitForReload);
        doubleClickOnNewlyCreatedClass(className);
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        verifyAssignClassToView(className);
    }

    /**
     * This methods performs action to check that After Show In Category Tree
     * checkbox is disabled the class is not listed under Tree View of channel
     * Categories
     * 
     * @param className this string contains name of the class
     */
    public void verifyNonVisibilityOfClassForDiabledShowCategoryTree(
            String className) {
        tabbedPaneNorthInstance.clkOnChannelCategoriesTab(waitForReload);
        browserDriver.navigate().refresh();
        traverseToChannelCategoriesTreeViewFrames(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@id='CSGuiTreeContainer']/table[1]/tbody/"
                        + "tr[1]/td[1]/span[1]")));
        WebElement viewInfoTable = browserDriver.findElement(By
                .xpath("//span[@id='CSGuiTreeContainer']/table[1]/tbody/tr[1]/"
                        + "td[1]/" + "span[1]"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) viewInfoTable
                .findElements(By.xpath("//a[@id]/span"));
        for (WebElement row : rows) {
            if (row.getText().equals(className)) {
                CSLogger.error(
                        "After Show In Category Tree checkbox is disabled the "
                                + "class should not be listed under Tree View "
                                + "still the class displays under tree view "
                                + "which is error");
                softAssertion
                        .fail("After Show In Category Tree checkbox is disabled the "
                                + "class should not be listed under Tree View "
                                + "still the class displays under tree view "
                                + "which is error");
            }
        }
        CSLogger.info("After Show In Category Tree checkbox is disabled the "
                + "class is not listed under Tree View test case passed");
    }

    /**
     * this method gets newly created view
     * 
     * @param viewName this is a string parameter that contains name of the view
     * @return web element of view
     */
    public WebElement getNewlyCreatedView(String viewName) {
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(viewName)));
        return browserDriver.findElement(By.linkText(viewName));
    }

    /**
     * This method double clicks on view using the linktext i.e view name
     * 
     * @param viewName this is a string parameter that contains name of the view
     */
    public void doubleClickOnView(String viewName) {
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(getNewlyCreatedView(viewName)).perform();
    }

    /**
     * Contains assert statement to verify whether view was assigned to class or
     * not
     * 
     * @param viewName this is a string parameter that contains name of the view
     */
    public void verifyAssignClassToView(String viewName) {
        Assert.assertEquals(csGuiDialogContentIdInstance
                .getTxtOflblViewAssignedClassName(waitForReload), viewName);
    }

    /**
     * 
     * @param className this is string parameter that contains name of the class
     * @param viewName this is a string parameter that contains name of the view
     */
    public void channelCategoriesTreeViewClasses(String className,
            String viewName) {
        tabbedPaneNorthInstance.clkOnChannelCategoriesTab(waitForReload);
        browserDriver.navigate().refresh();
        clkOnChannelCategoriesClass(className);
        verifyChannelCategoriesTreeViewClasses(viewName);
    }

    /**
     * This method checks that views assigned to respective class is displayed
     * in list view in adjoining mid pane
     * 
     * @param viewName this is a string parameter that contains name of the view
     */
    public void verifyChannelCategoriesTreeViewClasses(String viewName) {
        try {
            traverseToSplitAreaContentMainFrame();
            setProductsViewToListView();
            traverseToSplitAreaContentMainFrame();
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//table[@class='hidewrap CSAdminList']")));
            WebElement viewInfoTable = browserDriver.findElement(
                    By.xpath("//table[@class='hidewrap CSAdminList']"));
            ArrayList<WebElement> rows = (ArrayList<WebElement>) viewInfoTable
                    .findElements(By.xpath("//tr[@id]"));
            for (WebElement row : rows) {
                ArrayList<WebElement> cells = (ArrayList<WebElement>) row
                        .findElements(By.xpath("//tr[@id]/td[2]"));
                for (WebElement cell : cells) {
                    if (cell.getText().equals(viewName)) {
                        cell.click();
                        CSLogger.info("Views assigned to respective class is  "
                                + "displayed in list view in adjoining mid pane"
                                + "test passed");
                    }
                    break;
                }
                break;
            }
        } catch (Exception e) {
            CSLogger.error(
                    "Views assigned to respective class is not displayed in list"
                            + " view in adjoining mid pane.",
                    e);
            Assert.fail(
                    "Views assigned to respective class is not displayed in list"
                            + " view in adjoining mid pane.");
        }
    }

    /**
     * Clicks on classes under channel categories
     * 
     * @param className this is string parameter that contains name of the class
     */
    public void clkOnChannelCategoriesClass(String className) {
        tabbedPaneNorthInstance
                .traverseTillChannelCategoriesClassList(waitForReload);
        clkOnNewlyCreatedClass(className);
    }

    /**
     * This method verifies for a class which is not assigned to any view,the
     * view is not listed in adjoining pane under channel categories
     * 
     * @param viewNotAssignedClassName this is a class name which is not
     *            assigned to any view and this class is created during test run
     */
    public void classNotAssignedToChannel(String viewNotAssignedClassName) {
        tabbedPaneNorthInstance.clkOnChannelCategoriesTab(waitForReload);
        browserDriver.navigate().refresh();
        tabbedPaneNorthInstance
                .traverseTillChannelCategoriesClassList(waitForReload);
        waitForReload.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.linkText(viewNotAssignedClassName)));
        browserDriver.findElement(By.linkText(viewNotAssignedClassName))
                .click();
        traverseToSplitAreaContentMainFrame();
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='hidewrap CSAdminList']")));
        WebElement viewInfoTable = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) viewInfoTable
                .findElements(By.xpath("//tr[@id]"));
        Integer size = new Integer(rows.size());
        if (size.equals(2)) {
            CSLogger.info("No views assigned test pass");
        } else {
            CSLogger.error(
                    "Error while checking for the no view should be displayed "
                            + "in adjoining midpane while class  is not assigned"
                            + " to any Channel.");
            Assert.fail(
                    "Error while checking for the no view should be displayed "
                            + "in adjoining midpane while class  is not assigned"
                            + " to any Channel.");
        }
    }

    /**
     * This method switches the frame till FrmSplitAreaContentMain frame
     */
    public void traverseToSplitAreaContentMainFrame() {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaContentMain()));
    }

    /**
     * Performs action of assigning class to product folder
     * 
     * @param productFolderName this string parameter contains name of the
     *            product folder
     * @param className this is string parameter that contains name of the class
     * @param attributeList this parameter contains attribute names which are
     *            assigned to the class
     */
    public void assignClassToProductFolder(String productFolderName,
            String className, String attributeList) {
        performActionsAssignClassToProduct(productFolderName);
        assignClassToProduct(productFolderName, false, className,
                attributeList);
        assignClassToProduct(productFolderName, true, className, attributeList);
    }

    /**
     * This method right clicks on product folder
     * 
     * @param productFolderName this string parameter contains name of the
     *            product folder
     */
    public void rightClkOnProductFolder(String productFolderName) {
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOfElementLocated(By.linkText(productFolderName)));
        WebElement productName = browserDriver
                .findElement(By.linkText(productFolderName));
        CSUtility.rightClickTreeNode(waitForReload, productName, browserDriver);
    }

    /**
     * This method assigns class to products
     * 
     * @param productFolderNamethis string parameter contains name of the
     *            product folder
     * @param pressOkay
     * @param className this string parameter contains name of the class
     * @param attributeList this parameter contains attribute names which are
     *            assigned to the class
     */
    public void assignClassToProduct(String productFolderName,
            Boolean pressOkay, String className, String attributeList) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        doubleClickOnNewlyCreatedClass(className);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        verifyAssignClassToProduct(pressOkay, className, attributeList);
    }

    /**
     * this method contains assert statements to verify whether the class was
     * assigned to the product or not
     * 
     * @param pressOkay this is boolean parameter can contain true or false
     *            values
     * @param className this string parameter contains name of the class
     * @param attributeList this parameter contains attribute names which are
     *            assigned to the class
     */
    public void verifyAssignClassToProduct(Boolean pressOkay, String className,
            String attributeList) {
        if (pressOkay) {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            String classNameXpath = "//span[contains(text(),'" + className
                    + "')]";
            if (browserDriver.findElement(By.xpath(classNameXpath))
                    .isDisplayed()) {
                CSLogger.info("Class assigned to product folder successfully");
            } else {
                CSLogger.error("No Class Assigned to product folder ");
                Assert.fail("No Class Assigned to product folder");
            }
            verifyDataPaneAttributes(attributeList);
        } else {
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//td[contains(@class,'GuiEditorRight odd last')]")));
            Integer rowSize = browserDriver.findElements(
                    By.xpath("//td[contains(@class,'GuiEditorRight odd "
                            + "last')]//div"))
                    .size();
            if (rowSize.equals(3)) {
                CSLogger.info(
                        "If no previous class was assigned to the product folder"
                                + " the current class when clicked on cancel did "
                                + "not assigned to the product the testcase "
                                + "passed");
            } else {
                CSLogger.error("Error while clicked on cancel button the class "
                        + "should not be assigned to the product folder ");
                Assert.fail(
                        "Error while clicked on cancel button the class should "
                                + "not be assigned to the product folder");
            }
        }
    }

    /**
     * Contains steps to perform or assign class to product
     * 
     * @param productFolderName this string parameter contains name of the
     *            product folder
     */
    public void performActionsAssignClassToProduct(String productFolderName) {
        rightClkOnProductFolder(productFolderName);
        productPopup.selectPopupDivMenu(waitForReload,
                classPopup.getCsGuiPopupSubMenuEdit(), browserDriver);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
    }

    /**
     * Switches to new window for eg if any product is clicked and opened or
     * view is selected
     */
    public void switchToCurrentWindowHandle() {
        for (String winHandle : browserDriver.getWindowHandles()) {
            browserDriver.switchTo().window(winHandle);
        }
        CSLogger.info("Switched to new window handle");
    }

    /**
     * After assigning class to product this method verifies the data pane
     * properties
     * 
     * @param attributeList this parameter contains attribute names which are
     *            assigned to the class
     */
    private void verifyDataPaneAttributes(String attributeList) {
        try {
            csGuiDialogContentIdInstance.clkBtnData(waitForReload);
            String[] sheetAttributeNames = attributeList.split(",");
            String allAttributeLinkText = null;
            List<WebElement> attributeXPaths = browserDriver.findElements(By
                    .xpath("(//table[@class='CSGuiTable GuiEditorTable spacer']"
                            + "/tbody)[2]"));
            for (WebElement attributePaths : attributeXPaths) {
                allAttributeLinkText = attributePaths.getText();
            }
            String[] linkText = allAttributeLinkText.split("\\n");
            for (int i = 0; i < linkText.length; i++) {
                if (linkText[i].contains("*")) {
                    String linkTextWithRequiredFiled = linkText[i].substring(0,
                            linkText[i].length() - 1);
                    linkText[i] = linkTextWithRequiredFiled;
                }
            }
            for (int classXpathIndex = 0; classXpathIndex < linkText.length; classXpathIndex++) {
                for (int attributeIndex = 0; attributeIndex < sheetAttributeNames.length; attributeIndex++) {
                    if (linkText[classXpathIndex].equalsIgnoreCase(
                            sheetAttributeNames[attributeIndex])) {
                        CSLogger.info("Attribute"
                                + sheetAttributeNames[attributeIndex]
                                + " is Present");
                    }
                }
            }
            CSLogger.info("Verification of attributes under Data Pane  passed");
        } catch (Exception e) {
            CSLogger.error("Attributes are not verified under data pane tab for"
                    + "a product");
            softAssertion
                    .fail("Attributes are not verified under data pane tab for"
                            + "a product");
        }
    }

    /**
     * Verifies that a class is not assigned to a view
     * 
     * @param classNotassignedToViewName this is a class name which is not
     *            assigned to any view and this class is created during test run
     */
    public void
            verifyClassNotAssignedToView(String classNotassignedToViewName) {
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode(),
                browserDriver);
        classPopup.selectPopupDivMenu(waitForReload,
                classPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        classPopup.enterValueInDialogue(waitForReload,
                classNotassignedToViewName);
        classPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        browserDriver.findElement(By.linkText(classNotassignedToViewName))
                .click();
        enableCategoryTreeCheckbox();
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        classNotAssignedToChannel(classNotassignedToViewName);
    }

    /**
     * Switches till FrmPdmArticle frame
     * 
     * @param waitForReload this is WebDriverWait object
     * @param browserDriverInstance this is browser driver instance
     */
    public static void traverseToProductCategoriesTreeViewFrames(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseToFrmCsPortalGuiLayoutCenterOverFlowFrame(waitForReload,
                browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmPdmArticle()));
        CSLogger.info("Traverse to Product Categories Tree View Frames ");
    }

    /**
     * Switches till FrmPdmArticleStructure frame
     * 
     * @param waitForReload this is WebDriverWait object
     * @param browserDriverInstance this is browser driver instance
     */
    public static void traverseToChannelCategoriesTreeViewFrames(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        traverseToFrmCsPortalGuiLayoutCenterOverFlowFrame(waitForReload,
                browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmPdmArticleStructure()));
        CSLogger.info("Traverse to Channel Categories Tree View Frames ");
    }

    /**
     * This method switches till frmtree frame
     * 
     * @param waitForReload this is WebDriverWait object
     * @param browserDriverInstance this is browser driver instance
     */
    public static void traverseToFrmCsPortalGuiLayoutCenterOverFlowFrame(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrame190()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrmTree()));
        CSLogger.info("Traversed till frmtree frame");
    }

    /**
     * Sets the display view to 'Medium Preview Images'
     */
    private void setProductsViewToMediumPreviewImages() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        moreOptionPopup.selectPopupDivMenu(waitForReload,
                moreOptionPopup.getCtxView(), browserDriver);
        csPopupDivPimInstance.traverseToCSPopupDivSubFrame(waitForReload,
                browserDriver);
        int isMediumPreviewImagesSet = browserDriver
                .findElements(By
                        .xpath("//td[contains(text(),'Medium Preview Images')]"))
                .size();
        if (isMediumPreviewImagesSet == 0) {
            CSLogger.info("Medium preview images is already set");
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarRefresh(waitForReload);
        } else {
            moreOptionPopup.selectPopupDivSubMenu(waitForReload,
                    moreOptionPopup.getCtxMoreOptionMediumPreviewImages(),
                    browserDriver);
            CSLogger.info("Medium preview Images view set successfully");
        }
    }

    /**
     * Sets the products to list view.
     */
    private void setProductsViewToListView() {
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        moreOptionPopup.selectPopupDivMenu(waitForReload,
                moreOptionPopup.getCtxView(), browserDriver);
        csPopupDivPimInstance.traverseToCSPopupDivSubFrame(waitForReload,
                browserDriver);
        int isMediumPreviewImagesSet = browserDriver
                .findElements(By.xpath("//td[@title='Display']")).size();
        if (isMediumPreviewImagesSet == 0) {
            CSLogger.info("List  view is already set");
            traverseToSplitAreaContentMainFrame();
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarRefresh(waitForReload);
        } else {
            moreOptionPopup.selectPopupDivSubMenu(waitForReload,
                    moreOptionPopup.getCtxMoreOptionDisplay(), browserDriver);
            CSUtility.tempMethodForThreadSleep(1000);
            moreOptionPopup.selectPopupDivNestedSubMenu(waitForReload,
                    moreOptionPopup.getCtxMoreOptionListView(), browserDriver);
            CSLogger.info("Medium preview Images view set successfully");
        }
    }

    /**
     * This is a data provider method.
     * 
     * @return Array String array consisting of
     *         viewname,classname,productname,attribute list
     */
    @DataProvider(name = "categoryTreeCheckboxTestData")
    public Object[][] getCategoryTreeData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                categoryTreeSheet);
    }

    @BeforeMethod
    public void initializeResource() {
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        classPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioChannelsNodeInstance = new PimStudioChannelsNode(
                browserDriver);
        softAssertion = new SoftAssert();
        moreOptionPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPopupDivPimInstance = new CSPopupDivPim(browserDriver);
    }
}
