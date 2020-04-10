/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.dashboard;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.dashboard.DashboardPage;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.settings.RulesPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForDashboardModule;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
 * This class tests the dashboard configuration for context app and verifies
 * dashboard tile in context
 * 
 * @author CSAutomation Team
 *
 */
public class DashboardConfigurationForContextAppTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private String                    dashboardConfigForContextAppSheet = "DashboardConfigForContextApp";
    private DashboardPage             dashboardPage;
    private Actions                   actions;
    private SoftAssert                softAssert;
    private IProductPopup             productPopup;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private FrameLocators             locator;
    private RulesPage                 rulesPage;
    private CSGuiToolbarHorizontal    cSGuiToolbarHorizontal;

    /**
     * This test method verifies dashboard tile in context
     * 
     * @param dashboardLabel contains dashboard label
     * @param productName contains product name
     * @param childName contains child name
     * @param paneName contains pane name
     * @param tileName contains tile name
     * @param tileType contains tile type
     * @param templateProductName contains template product name
     * @param dataSource contains data source
     * @param language contains language
     * @param backgroundColor contains background color
     * @param newObjectName contains new product name
     */
    @Test(priority = 1, dataProvider = "configureAndVerifyDQAppTest")
    public void testDashboardConfigurationForContextApp(String dashboardLabel,
            String productName, String childName, String paneName,
            String tileName, String tileType, String templateProductName,
            String dataSource, String language, String backgroundColor,
            String newObjectName) {
        executePrerequisite(productName, childName, templateProductName);
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        clkDashboardByUserNode();
        performOperationOnSelectedDashboard(dashboardLabel);
        verifyDashboardPaneInPim(productName, paneName);
    }

    /**
     * This method executes the pre requisites for test case
     * 
     * @param productName contains name of product
     * @param childName contains child name
     * @param templateProductName contains template product name
     */
    private void executePrerequisite(String productName, String childName,
            String templateProductName) {
        csPortalHeader.clkBtnProducts(waitForReload);
        createProduct(productName, true);
        createChildProduct(productName, childName, true);
        createProduct(templateProductName, true);
    }

    /**
     * This method creates the child product
     * 
     * @param productName contains name of product
     * @param childName contains child name
     * @param pressOkay contains boolean value
     */
    private void createChildProduct(String productName, String childName,
            boolean pressOkay) {
        WebElement nameOfProduct = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.rightClickTreeNode(waitForReload, nameOfProduct,
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuNewChild(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, childName);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        verifyCreatedChild(productName, childName);
    }

    /**
     * This method verifies the created child
     * 
     * @param productName contains product name
     * @param productChild contains child name
     */
    private void verifyCreatedChild(String productName, String productChild) {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioProductsNode.getBtnPimProductsNode()));
            pimStudioProductsNode.getBtnPimProductsNode().click();
            WebElement product = browserDriver
                    .findElement(By.linkText(productName));
            product.click();
            WebElement childProduct = browserDriver
                    .findElement(By.linkText(productChild));
            Assert.assertEquals(productChild, childProduct.getText());
            CSLogger.info("Child product " + productChild + "is present .");
        } catch (Exception e) {
            CSLogger.info("Child product " + productChild + "is not present .");
            Assert.fail("Child product " + productChild + "is not present .");
        }
    }

    /**
     * This method creates product
     * 
     * @param productName contains product name
     * @param pressOkay contains boolean value
     */
    private void createProduct(String productName, boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, productName);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        verifyProductCreation(productName, pressOkay);
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * Verifies whether product is created.
     * 
     * @param productName String object contains name of product.
     * @param pressOkay Boolean parameter contains true or false values.
     */
    private void verifyProductCreation(String productName, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        int productExists = browserDriver.findElements(By.linkText(productName))
                .size();
        if (pressOkay) {
            if (productExists != 0) {
                CSLogger.info("Product " + productName
                        + " created successfully :  test step passed");
            } else {
                CSLogger.error("Product " + productName
                        + " creation failed : test step failed");
                softAssert.fail("Product creation failed : test step failed");
            }
        } else {
            if (productExists != 0) {
                CSLogger.error("Product " + productName
                        + " not created when clicked on " + "cancel: test step "
                        + "failed");
                softAssert.fail(
                        "Product " + productName + " not created when clicked"
                                + " on cancel: test step failed");
            } else {
                CSLogger.info(
                        "No product created when clicked on cancel :  test step"
                                + " passed");
            }
        }
    }

    /**
     * This method veriries dashboard pane presence in pim
     * 
     * @param productName contains product name
     * @param paneName contains pane name
     */
    private void verifyDashboardPaneInPim(String productName, String paneName) {
        boolean status = false;
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("Clicked on Products node.");
        WebElement createdProduct = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdProduct);
        createdProduct.click();
        actions.doubleClick(createdProduct).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        List<WebElement> list = browserDriver.findElements(
                By.xpath("//table[@class='TabbedPaneTitle']/tbody/tr/td"));
        for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            WebElement listElement = list.get(listIndex);
            if (paneName.equals(listElement.getText())) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSLogger.info("Dashboard pane is available");
        } else {
            CSLogger.error("Pane is not present");
        }
    }

    /**
     * This method performs operation on selected dashboard
     * 
     * @param dashboardLabel contains dashboard label
     */
    private void performOperationOnSelectedDashboard(String dashboardLabel) {
        selectDashboard(dashboardLabel);
    }

    /**
     * This method selects the dashboard
     * 
     * @param dashboardLabel contains dashboard label
     */
    private void selectDashboard(String dashboardLabel) {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
        WebElement label = getDashboardLabel(dashboardLabel);
        CSUtility.waitForVisibilityOfElement(waitForReload, label);
        label.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("clicked on the dashboard to be selected");
        selectAllContentsFromPopupWindow();
    }

    /**
     * This method selects all contents from pop up window
     */
    private void selectAllContentsFromPopupWindow() {
        traverseToPopupWindow();
        clkSelectAllButton();
        clkSave();
        closeWindow();
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method closes window
     */
    private void closeWindow() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(dashboardPage.getCsPortalWindow()));
        dashboardPage.clkElement(waitForReload, dashboardPage.getBtnClose());
    }

    /**
     * This method clicks on save
     */
    private void clkSave() {
        cSGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method clicks on select all button
     */
    private void clkSelectAllButton() {
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getBtnSelectAllForSelectedSection());
    }

    /**
     * This method traverses to pop up window
     */
    private void traverseToPopupWindow() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(dashboardPage.getCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
    }

    /**
     * This method gets dashboard label in weblelement from the list
     * 
     * @param dashboardLabel contains dashboard label
     * @return listElement
     */
    private WebElement getDashboardLabel(String dashboardLabel) {
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[3]"));
        for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            listElement = list.get(listIndex);
            if (dashboardLabel.equals(listElement.getText())) {
                status = true;
                break;
            }
        }
        if (status == true) {
            return listElement;
        } else {
            return null;
        }
    }

    /**
     * This method clicks on dashboard by user node
     */
    private void clkDashboardByUserNode() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        rulesPage.clkElement(waitForReload,
                rulesPage.getNodeSystemPreferences());
        TraversingForSettingsModule
                .traverseToNodesInLeftPaneOfSystemPreferences(waitForReload,
                        browserDriver);
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getNodeDashboard());
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getNodeDashboardByUser());
        actions.doubleClick(dashboardPage.getNodeDashboardByUser()).build()
                .perform();
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This test method verifies dashboard tile in context
     * 
     * @param dashboardLabel contains dashboard label
     * @param productName contains product name
     * @param childName contains child name
     * @param paneName contains pane name
     * @param tileName contains tile name
     * @param tileType contains tile type
     * @param templateProductName contains template product name
     * @param dataSource contains data source
     * @param language contains language
     * @param backgroundColor contains background color
     * @param newObjectName contains new product name
     */
    @Test(priority = 2, dataProvider = "configureAndVerifyDQAppTest")
    public void testVerifyDashboardTileInContext(String dashboardLabel,
            String productName, String childName, String paneName,
            String tileName, String tileType, String templateProductName,
            String dataSource, String language, String backgroundColor,
            String newObjectName) {
        try {
            addNewTile(tileName, tileType);
            configureTile(tileName, tileType, dataSource, productName,
                    templateProductName, language, backgroundColor);
            createAnotherObjectUsingConfiguredTile(productName, paneName,
                    tileName, newObjectName);
            verifyNewlyCreatedObject(newObjectName, productName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed");
        }
    }

    /**
     * This method verifies newly created product
     * 
     * @param newObjectName contans product name to be verified
     * @param productName contans product name
     */
    private void verifyNewlyCreatedObject(String newObjectName,
            String productName) {
        try {
            WebElement product = clkOnCreatedProduct(productName);
            CSUtility.waitForVisibilityOfElement(waitForReload, product);
            CSUtility.rightClickTreeNode(waitForReload,
                    pimStudioProductsNode.getBtnPimProductsNode(),
                    browserDriver);
            productPopup.selectPopupDivMenu(waitForReload,
                    productPopup.getcsGuiPopupMenuRefresh(), browserDriver);
            CSUtility.tempMethodForThreadSleep(2000);
            product = clkOnCreatedProduct(productName);
            product.click();
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement createdObject = browserDriver
                    .findElement(By.linkText(newObjectName));
            Assert.assertEquals(newObjectName, createdObject.getText());
            CSLogger.info("Verified. The newly created object is present");
                    } catch (Exception e) {
            CSLogger.error("Object has not created");
            softAssert.fail("Object has not created");
        }
    }

    /**
     * This method creates new product using configured tile
     * 
     * @param productName contains name of product
     * @param paneName contains name of pane
     * @param tileName contains name of tile
     * @param newObjectName contains new object name
     */
    private void createAnotherObjectUsingConfiguredTile(String productName,
            String paneName, String tileName, String newObjectName) {
        csPortalHeader.clkBtnProducts(waitForReload);
        verifyAndClickDashboardTileInDashboardPane(tileName, paneName,
                productName);
        handlePopupToAddNewObjectName(newObjectName);
    }

    /**
     * This method handles pop up to add new object via tile
     * 
     * @param newObjectName contains object name to be added
     */
    private void handlePopupToAddNewObjectName(String newObjectName) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(dashboardPage.getTxtAjsInput()));
        dashboardPage.getTxtAjsInput().clear();
        CSUtility.tempMethodForThreadSleep(1000);
        dashboardPage.getTxtAjsInput().sendKeys(newObjectName);
        CSLogger.info("Sent new object data ");
        CSUtility.tempMethodForThreadSleep(1000);
        dashboardPage.clkElement(waitForReload, dashboardPage.getBtnOkAjs());
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method verifies and clicks on dashboad tile in dashboard pane
     * 
     * @param tileName contains tile name
     * @param paneName contains pane name
     * @param productName contains name of product
     */
    private void verifyAndClickDashboardTileInDashboardPane(String tileName,
            String paneName, String productName) {
        WebElement product = clkOnCreatedProduct(productName);
        actions.doubleClick(product).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement dashboardPane = browserDriver.findElement(
                By.xpath("//nobr[contains(text(),'" + paneName + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, dashboardPane);
        dashboardPane.click();
        CSLogger.info("Clicked on dashboard pane");
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement frmToDashboardTile = browserDriver.findElement(By
                .xpath("//iframe[@id='frm_7a60ad3ea8122e846942fb1ca6913e73']"));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(frmToDashboardTile));
        lockDashboard();
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement createdTile = browserDriver.findElement(By
                .xpath("//div[contains(text(),'" + tileName + "')]/../../.."));
        waitForReload.until(ExpectedConditions.visibilityOf(createdTile));
        actions.moveToElement(createdTile).build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        createdTile.click();
        CSLogger.info("Clicked on created tile");
    }

    /**
     * This method clicks on created product
     * 
     * @param productName contains name of product
     * @return product
     */
    private WebElement clkOnCreatedProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        WebElement product = browserDriver
                .findElement(By.linkText(productName));
        product.click();
        return product;
    }

    /**
     * This method adds new tile
     * 
     * @param tileName contains name of tile
     * @param tileType contains type of tile
     */
    private void addNewTile(String tileName, String tileType) {
        csPortalHeader.clkBtnHome(waitForReload);
        selectMode(dashboardPage.getBtnAdministrator());
        dashboardPage.clkElement(waitForReload, dashboardPage.getBtnPlus());
    }

    /**
     * This method unlocks the dashboard from the home page
     */
    private void lockDashboard() {
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getBtnLockToggle());
        CSLogger.info("Cliced on Lock toggle button");
    }

    /**
     * Selects the data source from drop down
     * 
     * @param dataSource contains data source
     */
    private void selectDataSource(String dataSource) {
        Select source = new Select(browserDriver
                .findElement(By.xpath("//select[@name='dataSource']")));
        source.selectByVisibleText(dataSource);
        actions.sendKeys(Keys.TAB).build().perform();
    }

    /**
     * This method configures the tile
     * 
     * @param tileName contains tile name
     * @param tileType contains tile type
     * @param dataSource contains data source
     * @param productName contains name of product
     * @param templateProductName contains product name
     * @param language contains language
     * @param backgroundColor contains background color
     */
    private void configureTile(String tileName, String tileType,
            String dataSource, String productName, String templateProductName,
            String language, String backgroundColor) {
        dashboardPage.clkElement(waitForReload, dashboardPage.getTabNewTile());
        CSLogger.info("Clicked on new tile tab");
        CSUtility.tempMethodForThreadSleep(3000);
        selectTileType(tileType);
        CSUtility.tempMethodForThreadSleep(2000);
        enterDetails(tileName, dashboardPage.getTxtTileCaption());
        selectDataSource(dataSource);
        enterDetailsInTextbox(browserDriver.findElement(By.xpath(
                "//label[contains(text(),'Create new Object in Folder')]/../div/div/div/div/input")),
                productName);
        selectElement();
        actions.sendKeys(Keys.TAB).build().perform();
        enterDetailsInTextbox(browserDriver.findElement(By.xpath(
                "//label[contains(text(),'Use as template')]/../div/div/div/div/input")),
                templateProductName);
        selectElement();
        actions.sendKeys(Keys.TAB).build().perform();
        enterDetailsInTextbox(browserDriver.findElement(By.xpath(
                "//label[contains(text(),'Language')]/../div/div/div/div/input")),
                language);
        selectElement();
        actions.sendKeys(Keys.TAB).build().perform();
        WebElement txtBackgroundColor = browserDriver
                .findElement(By.xpath("//input[@name='backgroundColor']"));
        enterDetailsInTextbox(txtBackgroundColor, backgroundColor);
        txtBackgroundColor.sendKeys(Keys.ENTER);
        clkOk();
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method clicks on ok button
     */
    private void clkOk() {
        JavascriptExecutor executorObject = (JavascriptExecutor) browserDriver;
        waitForReload.until(
                ExpectedConditions.visibilityOf(dashboardPage.getBtnOk()));
        executorObject.executeScript("arguments[0].click()",
                dashboardPage.getBtnOk());
        CSLogger.info("Clicked on Ok button");
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * this method enter details in text box
     * 
     * @param element conatains intance of webelement
     * @param objectType contains type of object
     * @return element
     */
    private WebElement enterDetailsInTextbox(WebElement element,
            String objectType) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        CSUtility.tempMethodForThreadSleep(2000);
        element.click();
        element.sendKeys(objectType);
        CSLogger.info("Entered text " + objectType);
        return element;
    }

    /**
     * This method chooses element from the drop down
     */
    private void selectElement() {
        try {
            Robot robot = new Robot();
            robot = new Robot();
            CSUtility.tempMethodForThreadSleep(3000);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            CSUtility.tempMethodForThreadSleep(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            CSUtility.tempMethodForThreadSleep(2000);
        } catch (Exception e) {
            CSLogger.error("Could not select the element");
        }
    }

    /**
     * This method enters details in the textbox
     * 
     * @param textName contains text to be set in textbox
     * @param element contains web element of textbox
     */
    private void enterDetails(String textName, WebElement element) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(textName);
        CSLogger.info("Entered the name  " + textName);
    }

    /**
     * This method selects the type of tile
     * 
     * @param tileType string contains tile type
     */
    private void selectTileType(String tileType) {
        boolean status = false;
        WebElement listElement = null;
        List<WebElement> list = browserDriver
                .findElements(By.xpath("//ul[@class='CSListView']/li"));
        for (Iterator<WebElement> iterator = list.iterator(); iterator
                .hasNext();) {
            listElement = iterator.next();
            if (listElement.getText().equals(tileType)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            listElement.click();
            CSLogger.info("Clicked on tile type");
            clkInsertAndConfigure();
        } else {
            CSLogger.error("Could not find the required tile type");
        }
    }

    /**
     * This method clicks on insert and configure button
     */
    private void clkInsertAndConfigure() {
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getBtnInsertAndConfigure());
        CSLogger.info("Clicked on insert and configure button");
    }

    /**
     * This method selects the Administrator mode after unlocking the dashboard
     */
    private void selectMode(WebElement mode) {
        unlockDashboard();
        dashboardPage.clkElement(waitForReload, mode);
        CSLogger.info("Switched to the mode");
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method unlocks the dashboard from the home page
     */
    private void unlockDashboard() {
        TraversingForDashboardModule.traverseToDashboardPage(waitForReload,
                browserDriver);
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getBtnUnlockToggle());
        CSLogger.info("Clicked on Lock toggle button");
    }

    /**
     * This data provider returns the sheet which contains dashboard to be
     * selected,product name, child name ,pane name ,tile name , tile type,
     * template product name ,data source , language, background color in rgb
     * form and new object name
     * 
     * @return
     */
    @DataProvider(name = "configureAndVerifyDQAppTest")
    public Object[][] dashboardConfiguration() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("dashboardModuleTestCases"),
                dashboardConfigForContextAppSheet);
    }

    /**
     * This method is used to initialize all the resources used to drive the
     * test case
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeader = new CSPortalHeader(browserDriver);
        dashboardPage = new DashboardPage(browserDriver);
        actions = new Actions(browserDriver);
        softAssert = new SoftAssert();
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        rulesPage = new RulesPage(browserDriver);
        cSGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
    }
}
