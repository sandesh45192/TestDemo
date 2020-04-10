/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.dashboard;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.cs.csautomation.cs.dashboard.DashboardPage;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.settings.RulesPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
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
 * This method configures and verifies DQ App
 * 
 * @author CSAutomation Team
 *
 */
public class ConfigurationAndVerificationOfDQAppTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private String                    configureAndVerifyDQAppSheet = "ConfigureAndVerifyDQApp";
    private DashboardPage             dashboardPage;
    private Actions                   actions;
    private SoftAssert                softAssert;
    private IProductPopup             productPopup;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private IClassPopup               classPopup;
    private PimStudioSettingsNode     pimStudioSettingsNode;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private FrameLocators             locator;
    private RulesPage                 rulesPage;
    private ModalDialogPopupWindow    modalDialogPopupWindow;
    private String                    valueOfIdProduct;
    private String                    valueOfIdClass;

    /**
     * This method configures the rules of dq app
     * 
     * @param productName contains name of product
     * @param childName contains child name
     * @param className contains class name
     * @param ruleName contains rule name
     * @param objectRelation contains object relation string
     * @param ruleConditionName contains rule condition name
     * @param ruleConditonPluginName contains rule condition plugin name
     * @param attributeRuleCondition contains attribute rule condition
     * @param criterionTypeForConditions contains criterion type for conditions
     * @param comparatorValue contains comparator value
     * @param tileName contains tile name
     * @param tileType contains tile type
     * @param tileCategory contains tile category
     * @param objectType contains object type
     * @param displayObject contains display object
     * @param selectionType contains selection type
     */
    @Test(priority = 1, dataProvider = "configureAndVerifyDQAppTest")
    public void testConfigureRulesOfDQApp(String productName, String childName,
            String className, String ruleName, String objectRelation,
            String ruleConditionName, String ruleConditonPluginName,
            String attributeRuleCondition, String criterionTypeForConditions,
            String comparatorValue, String tileName, String tileType,
            String tileCategory, String objectType, String displayObject,
            String selectionType) {
        csPortalHeader.clkBtnProducts(waitForReload);
        executePrerequisites(productName, childName, className);
        performUseCase(productName, childName, className, ruleName,
                objectRelation, ruleConditionName, attributeRuleCondition,
                ruleConditonPluginName, criterionTypeForConditions,
                comparatorValue);
    }

    /**
     * This method executes the prerequisites for test method
     * 
     * @param productName contains product name
     * @param childName contains child name
     * @param className contains name of class
     */
    private void executePrerequisites(String productName, String childName,
            String className) {
        createProduct(productName, true);
        valueOfIdProduct = getIdOfCreatedProduct(productName);
        createChildProduct(productName, childName, true);
        createClass(className, true);
        dragDropClassToProduct(className, productName);
    }

    private String getAttributeValue(WebElement nameOfClass) {
        waitForReload.until(ExpectedConditions.visibilityOf(nameOfClass));
        actions.doubleClick(nameOfClass).build().perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        WebElement id = browserDriver.findElement(
                By.xpath("//input[@id='CSGuiEditorDialogIDInput']"));
        String valueOfId = id.getAttribute("value");
        return valueOfId;
    }

    /**
     * Returns id of product
     * 
     * @param productName contains name of product
     */
    private String getIdOfCreatedProduct(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
        WebElement product = browserDriver
                .findElement(By.linkText(productName));
        valueOfIdProduct = getAttributeValue(product);
        return valueOfIdProduct;
    }

    /**
     * This method checks in and checks out the product
     * 
     * @param createdProductFolder contains name of created product folder
     * @param csGuiPopupCheckInCheckOut contains xpath of check in or checkout
     *            option
     */
    private void checkInCheckout(WebElement createdProductFolder,
            WebElement csGuiPopupCheckInCheckOut) {
        CSUtility.rightClickTreeNode(waitForReload, createdProductFolder,
                browserDriver);
        CSUtility.switchToDefaultFrame(browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuObject(), browserDriver);
        productPopup.selectPopupDivSubMenu(waitForReload,
                csGuiPopupCheckInCheckOut, browserDriver);
    }

    /**
     * This method clicks on the created product folder
     * 
     * @param productName contains name of product
     * @return createdProductFolder
     */
    private WebElement clkProductFolder(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(productName));
        createdProductFolder.click();
        CSLogger.info("Clicked on product folder");
        return createdProductFolder;
    }

    /**
     * This method performs the use case of configuring dq app
     * 
     * @param productName contains product name
     * @param childName contains name of child product
     * @param className contains name of class
     * @param ruleName contains rule name
     * @param objectRelation contains object relation
     * @param ruleConditionName contains rule condition name
     * @param attributeRuleCondition contains attribute rule condition
     * @param ruleConditonPluginName contains rule condition plugin name
     * @param criterionTypeForConditions contains criterion type for condition s
     * @param comparatorValue contains value of comparator
     */
    private void performUseCase(String productName, String childName,
            String className, String ruleName, String objectRelation,
            String ruleConditionName, String attributeRuleCondition,
            String ruleConditonPluginName, String criterionTypeForConditions,
            String comparatorValue) {
        try {
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            clkDQNode();
            clkCreateNew();
            handlePropertiesPane(ruleName, objectRelation);
            handleRuleValidityPane(className);
            handleRuleConditionPane(ruleConditionName, attributeRuleCondition,
                    ruleConditonPluginName, criterionTypeForConditions,
                    comparatorValue);
            TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                    waitForReload, browserDriver, locator);
            TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                    browserDriver);
            clkSave();
            CSUtility.tempMethodForThreadSleep(2000);
            checkInCheckoutProduct(productName,
                    productPopup.getBtnCsGuiModalDialogNonRecursive());
        } catch (Exception e) {
            CSLogger.error("Could not check in and check out product");
        }
    }

    /**
     * This method clicks on create new
     */
    private void clkCreateNew() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        rulesPage.clkElement(waitForReload, rulesPage.getBtnCreateNew());
    }

    /**
     * This method handles rule condition pane
     * 
     * @param ruleConditionName contains rule condition name
     * @param attributeRuleCondition contains attribute rule condition name
     * @param ruleConditonPluginName contains rule condition plugin name
     * @param criterionTypeForConditions contains criterion type for condition
     * @param comparatorValue contains comparator value
     */
    private void handleRuleConditionPane(String ruleConditionName,
            String attributeRuleCondition, String ruleConditonPluginName,
            String criterionTypeForConditions, String comparatorValue) {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        rulesPage.clkElement(waitForReload, rulesPage.getSecRuleConditions());
        CSUtility.tempMethodForThreadSleep(2000);
        selectCriterionTypeForConditions(criterionTypeForConditions);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmRuleConditionHtml()));
        rulesPage.clkElement(waitForReload,
                rulesPage.getBtnAddRuleConditions());
        handleRuleConditionPopup(ruleConditionName, attributeRuleCondition,
                ruleConditonPluginName, criterionTypeForConditions,
                comparatorValue);
    }

    /**
     * This method handles the rule condition popup
     * 
     * @param ruleConditionName contains rule condition name
     * @param attributeRuleCondition contains attribute rule condition
     * @param ruleConditonPluginName contains rule condition plugin name
     * @param criterionTypeForConditions contains criterion type for conditions
     * @param comparatorValue contains value of comparator
     */
    private void handleRuleConditionPopup(String ruleConditionName,
            String attributeRuleCondition, String ruleConditonPluginName,
            String criterionTypeForConditions, String comparatorValue) {
        traverseToWindowContentFramesRuleConditionPopup();
        enterDetails(ruleConditionName, rulesPage.getTxtRuleConditionName());
        enterDetails(ruleConditionName, rulesPage.getTxtRuleConditionLabel());
        selectPlugin(ruleConditonPluginName);
        selectAttribute(attributeRuleCondition);
        traverseToWindowContentFramesRuleConditionPopup();
        selectComparator(comparatorValue);
        closeWindow();
    }

    /**
     * This method closes the window
     */
    private void closeWindow() {
        traverseToBtnClose();
        waitForReload.until(
                ExpectedConditions.visibilityOf(rulesPage.getBtnClose()));
        rulesPage.getBtnClose().click();
        CSUtility.tempMethodForThreadSleep(3000);
    }

    /**
     * This method traverses upto the close button
     */
    private void traverseToBtnClose() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(rulesPage.getCsPortalWindow()));
    }

    /**
     * This method selects comaprator
     * 
     * @param comparatorValue contains comparator value
     */
    private void selectComparator(String comparatorValue) {
        Select comparator = new Select(browserDriver.findElement(By.xpath(
                "//select[contains(@id,'RuleconditionOperator')]")));
        comparator.selectByVisibleText(comparatorValue);
        CSLogger.info("Selected the comparator");
    }

    /**
     * This method traverses to window content frame for rule condition popup
     */
    private void traverseToWindowContentFramesRuleConditionPopup() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(rulesPage.getCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
    }

    /**
     * This method selects critorion type for conditions
     * 
     * @param criterionTypeForConditions contains criterion type for conditions
     */
    private void selectCriterionTypeForConditions(
            String criterionTypeForConditions) {
        Select criterionType = new Select(browserDriver.findElement(
                By.xpath("//select[contains(@id,'RuleCriterionType')]")));
        criterionType.selectByVisibleText(criterionTypeForConditions);
        CSLogger.info("Criterion type selected successfully");
    }

    /**
     * This method selects attribute
     * 
     * @param attributeRuleCondition contains attribute rule condition as string
     */
    private void selectAttribute(String attributeRuleCondition) {
        boolean status = false;
        rulesPage.clkElement(waitForReload,
                rulesPage.getBtnGuiEditorChooserIcon());
        traverseToPortalWindowFrames();
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmTabbedPane()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsPdmarticleconfigurationBottom()));
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@id='Pdmarticleconfiguration_Table']/tbody/tr/td"));
        WebElement listElement = null;
        for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            listElement = list.get(listIndex);
            if (listElement.getText().equals(attributeRuleCondition)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            listElement.click();
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("double clicked on attribute for rule condition");
        }
        traverseToPortalWindowFrames();
        rulesPage.clkElement(waitForReload,
                rulesPage.getBtnCsGuiModalDialogOkButton());
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method traverses to portal window frames
     */
    private void traverseToPortalWindowFrames() {
        CSUtility.switchToDefaultFrame(browserDriver);
        WebElement portalWinElement = browserDriver
                .findElement(By.xpath("(//div[@class='CSPortalWindow'])[2]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, portalWinElement);
        WebElement contentElement = browserDriver.findElement(
                By.xpath("(//div[@class=' CSPortalWindowContent'])[2]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, contentElement);
        waitForReload.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(
                        "(//div[contains(@id,'CSPortalWindow')])[2]/div[2]/iframe")));
    }

    /**
     * This method selects the plugin
     * 
     * @param ruleConditonPluginName contains rule condition plugin name
     */
    private void selectPlugin(String ruleConditonPluginName) {
        Select plugin = new Select(browserDriver.findElement(
                By.xpath("//select[contains(@id,'RuleconditionPluginName')]")));
        plugin.selectByVisibleText(ruleConditonPluginName);
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method handles rule validity pane
     * 
     * @param className contains name of class
     */
    private void handleRuleValidityPane(String className) {
        rulesPage.clkElement(waitForReload, rulesPage.getSecRuleValidity());
        rulesPage.clkElement(waitForReload, rulesPage.getBtnPlusClassFilter());
        assignElement(rulesPage.getNodeClassesInDialogWindow(), className);
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method assings the element to respective class or product
     * 
     * @param element contains element to be assigned as web element
     * @param elementName contains element name in string form
     */
    private void assignElement(WebElement element, String elementName) {
        traverseToClassesNode();
        rulesPage.clkElement(waitForReload, element);
        WebElement nameOfElement = browserDriver
                .findElement(By.linkText(elementName));
        CSUtility.waitForVisibilityOfElement(waitForReload, nameOfElement);
        actions.doubleClick(nameOfElement).click().build().perform();
        modalDialogPopupWindow.askBoxWindowOperation(waitForReload, true,
                browserDriver);

    }

    /**
     * This method traverses to the classes node
     */
    private void traverseToClassesNode() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(rulesPage.getCsPortalWindowContent()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPortalWindowWidget()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmTabbedPane()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsPdmarticleconfigurationLeft()));

    }

    /**
     * Handles properties pane
     * 
     * @param ruleName contains rule name
     * @param objectRelation contains object relation string
     */
    private void handlePropertiesPane(String ruleName, String objectRelation) {
        enterLabelAndRuleName(ruleName);
        selectObjectRelation(objectRelation);
        enableChkbox();
    }

    /**
     * Enables checkbox
     */
    private void enableChkbox() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                rulesPage.getChkboxEnabled());
        String chkboxAttr = rulesPage.getChkboxEnabled().getAttribute("class");
        if (chkboxAttr.contains("Off")) {
            rulesPage.getChkboxEnabled().click();
        } else {
            CSLogger.info("Checkbox is already enabled");
        }
    }

    /**
     * This method selects object relation
     * 
     * @param objectRelation contains object relation string
     */
    private void selectObjectRelation(String objectRelation) {
        Select drpdwnObjectRelation = new Select(browserDriver.findElement(
                By.xpath("//select[contains(@id,'RuleClassId')]")));
        drpdwnObjectRelation.selectByVisibleText(objectRelation);
    }

    /**
     * This method enters label and rule name
     * 
     * @param ruleName contains rule name
     */
    private void enterLabelAndRuleName(String ruleName) {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, locator);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        enterDetails(ruleName, rulesPage.getTxtName());
        enterDetails(ruleName, rulesPage.getTxtLabel());
        clkSave();
    }

    /**
     * This method clicks on save button
     */
    private void clkSave() {
        rulesPage.clkElement(waitForReload, rulesPage.getBtnSave());
    }

    /**
     * This method clicks on DQ node
     */
    private void clkDQNode() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        rulesPage.clkElement(waitForReload,
                rulesPage.getNodeSystemPreferences());
        TraversingForSettingsModule
                .traverseToNodesInLeftPaneOfSystemPreferences(waitForReload,
                        browserDriver);
        rulesPage.clkElement(waitForReload, rulesPage.getNodeRules());
        rulesPage.clkElement(waitForReload, rulesPage.getNodeDataQuality());
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
        CSLogger.info("Cliced on Lock toggle button");
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
     * This method drags and drops class to the product
     * 
     * @param className contains class name
     * @param productName contains product name
     */
    private void dragDropClassToProduct(String className, String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(productName));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                createdProductFolder);
        createdProductFolder.click();
        CSUtility.tempMethodForThreadSleep(1000);
        traverseToSettings();
        traverseToClassToDragAndDrop(waitForReload, createdProductFolder,
                productName, className);
        performClick(waitForReload, "Extend");
        verifyAddedClassToProduct(waitForReload, createdProductFolder,
                productName, className);
    }

    /**
     * This method verifies if class has assigned to newly created Product
     * folder
     * 
     * @param waitForReload
     * @param createdProductFolder contains the product folder name as
     *            webElement
     * @param nameOfProduct contains name of the already created product in the
     *            string format
     */
    public void verifyAddedClassToProduct(WebDriverWait waitForReload,
            WebElement createdProductFolder, String nameOfProduct,
            String nameOfClass) {
        CSUtility.switchToDefaultFrame(browserDriver);
        Actions actions = new Actions(browserDriver);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("Clicked on Products Folder...");
        createdProductFolder = browserDriver
                .findElement(By.linkText(nameOfProduct));
        actions.doubleClick(createdProductFolder).perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentId.getTabProperties()));
        csGuiDialogContentId.getTabProperties().click();
        CSUtility.tempMethodForThreadSleep(1000);
        List<WebElement> list = getListOfClasses();
        boolean status = getStatus(nameOfClass, list);
        if (status == true) {
            CSLogger.info(nameOfClass + " has successfully assigned to the "
                    + nameOfProduct);
        } else {
            CSLogger.error("Failed to assign class to product");
            Assert.fail("Verification for assigned classes failed");
        }
    }

    /**
     * This method gets status of presence of element from the list
     * 
     * @param nameOfClass contains name of class
     * @param list contains list
     * @return status
     */
    private boolean getStatus(String nameOfClass, List<WebElement> list) {
        boolean status = false;
        WebElement test = null;
        for (int classIndex = 0; classIndex < list.size(); classIndex++) {
            test = list.get(classIndex);
            if (test.getText().equals(nameOfClass)) {
                status = true;
            }
        }
        return status;
    }

    /**
     * Returns the list of elements.
     * 
     * @return list
     */
    private List<WebElement> getListOfClasses() {
        List<WebElement> list = browserDriver.findElements(
                By.xpath("//table[@class='CSGuiTable']/tbody/tr/td[2]/span"));
        return list;
    }

    /**
     * This method performs click on either extend or cancel or replace button
     * after drag and drop Cancel will cancel the functionality extend inherits
     * class to already existing classes in products Replace replaces already
     * existing classes with new one.
     * 
     * @param waitForReload contains time in ms to wait on any element if needed
     * @param selectButton contains button names Cancel ,Extend and Replace
     */
    public void performClick(WebDriverWait waitForReload, String selectButton) {
        IProductPopup productPopUp = new CSPopupDivPim(browserDriver);
        browserDriver.switchTo().defaultContent();
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        if (selectButton.equals("Cancel")) {
            waitForReload.until(ExpectedConditions.visibilityOf(
                    productPopUp.getBtnCsGuiModalDialogCancelButton()));
            productPopUp.getBtnCsGuiModalDialogCancelButton();
        } else if (selectButton.equals("Extend")) {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDialogContentId.getBtnExtend()));
            csGuiDialogContentId.getBtnExtend().click();
        } else {
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDialogContentId.getBtnReplace()));
            csGuiDialogContentId.getBtnReplace().click();
        }
        CSLogger.info(
                "Performed click after dragging the class to the product");
    }

    /**
     * This test method which traverses upto the newly created class and
     * performs drag and drop on the newly created product folder
     * 
     * @param waitForReload contains the time in ms to wait on any element if
     *            needed
     * @param nameOfProduct contains name of the already created product in
     *            string format
     * @param nameOfClass contains name of class contains the name of the class
     * @param createdProductFolder contains name of already created product
     *            folder as webElement
     */
    public void traverseToClassToDragAndDrop(WebDriverWait waitForReload,
            WebElement createdProductFolder, String nameOfProduct,
            String nameOfClass) {
        try {
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioSettingsNode.getBtnPimClassesNode()));
            pimStudioSettingsNode.getBtnPimClassesNode().click();
            CSLogger.info("Clicked on Class Folder");
            waitForReload.until(ExpectedConditions
                    .visibilityOfElementLocated(By.linkText(nameOfClass)));
            WebElement className = browserDriver
                    .findElement(By.linkText(nameOfClass));
            CSUtility.tempMethodForThreadSleep(3000);
            CSUtility.waitForVisibilityOfElement(waitForReload, className);
            className.click();
            CSUtility.scrollUpOrDownToElement(
                    browserDriver.findElement(By.linkText(nameOfProduct)),
                    browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOfElementLocated(By.linkText(nameOfProduct)));
            createdProductFolder = browserDriver
                    .findElement(By.linkText(nameOfProduct));
            actions.dragAndDrop(className, createdProductFolder).perform();
        } catch (Exception e) {
            CSLogger.error("Dragged and Dropped class to Product::" + e);
        }
    }

    /**
     * This method traverses to settings
     */
    private void traverseToSettings() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioSettingsNode.getBtnPimSettingsNode()));
        pimStudioSettingsNode.getBtnPimSettingsNode().click();
        CSLogger.info("Clicked on Settings Node");
    }

    /**
     * This method creates class
     * 
     * @param className contains class name
     * @param pressOkay contains boolean value
     */
    private void createClass(String className, boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioSettingsNode.clkBtnPimSettingsNode(waitForReload);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNode.getBtnPimSettingsClassesNode(),
                browserDriver);
        classPopup.selectPopupDivMenu(waitForReload,
                classPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        classPopup.enterValueInDialogue(waitForReload, className);
        classPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
    }

    /**
     * This method creates child product
     * 
     * @param productName contains product name
     * @param childName contains name of product child
     * @param pressOkay contains boolean value
     */
    private void createChildProduct(String productName, String childName,
            boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clkBtnPimProductsNode(waitForReload,
                browserDriver);
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
     * @param productName contains name of product
     * @param productChild contains product child name
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
     * Creates the given product and also verifies the product creation.
     * 
     * @param productName String object contains name of product.
     * @param pressOkay Boolean parameter contains true or false values.
     */
    private void createProduct(String productName, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, productName);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
        verifyProductCreation(productName, pressOkay);
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
     * This method configures the rules of dq app
     * 
     * @param productName contains name of product
     * @param childName contains child name
     * @param className contains class name
     * @param ruleName contains rule name
     * @param objectRelation contains object relation string
     * @param ruleConditionName contains rule condition name
     * @param ruleConditonPluginName contains rule condition plugin name
     * @param attributeRuleCondition contains attribute rule condition
     * @param criterionTypeForConditions contains criterion type for conditions
     * @param comparatorValue contains comparator value
     * @param tileName contains tile name
     * @param tileType contains tile type
     * @param tileCategory contains tile category
     * @param objectType contains object type
     * @param displayObject contains display object
     * @param selectionType contains selection type
     */
    @Test(priority = 2, dataProvider = "configureAndVerifyDQAppTest")
    public void testConfigureAndVerifyDQApp(String productName,
            String childName, String className, String ruleName,
            String objectRelation, String ruleConditionName,
            String ruleConditonPluginName, String attributeRuleCondition,
            String criterionTypeForConditions, String comparatorValue,
            String tileName, String tileType, String tileCategory,
            String objectType, String displayObject, String selectionType) {
        csPortalHeader.clkBtnHome(waitForReload);
        selectMode(dashboardPage.getBtnAdministrator());
        performUseCase(tileName, tileType, tileCategory, objectType,
                displayObject, productName, className, ruleName, selectionType);
    }

    /**
     * This method performs the use case of configuring and verifying dq app
     * 
     * @param tileName contains name of tile
     * @param tileType contains tile type
     * @param tileCategory contains tile category
     * @param objectType contains object type
     * @param displayObject contains display object
     * @param productName contains product name
     * @param className contains class name
     * @param ruleName contains rule name
     * @param selectionType contains selection type
     */
    private void performUseCase(String tileName, String tileType,
            String tileCategory, String objectType, String displayObject,
            String productName, String className, String ruleName,
            String selectionType) {
        addNewTile(tileName, tileType, tileCategory, objectType, displayObject,
                productName, className, ruleName, selectionType);
        verifyConfiguredTile(tileName);
    }

    /**
     * This method catches the tooltip
     * 
     * @param productName contains name of product
     */
    private void catchTheTooltip(String productName) {
        String key = "background-color";
        WebElement progressBarPath = browserDriver
                .findElement(By.xpath("//div[contains(text(),'" + productName
                        + "')]/../../div[@class='CSProgressBarView CSListViewAccessoryView']/div/div"));
        waitForReload.until(ExpectedConditions.visibilityOf(progressBarPath));
        HashMap<String, String> map = new HashMap<>();
        String styleAttribute = progressBarPath.getAttribute("style");
        String[] attrValues = styleAttribute.split(";");
        for (int i = 0; i < attrValues.length; i++) {
            String[] values = null;
            values = attrValues[i].split(":");
            for (int j = 0; j < values.length; j++) {
                String firstVal = values[j].trim();
                String secondval = values[++j].trim();
                map.put(firstVal, secondval);
            }
        }
        if (map.containsKey(key)) {
            String value = map.get(key);
            if (value.equals("rgb(151, 208, 102)")) {
                CSLogger.info(
                        "Color of the progress bar is green.Verified for the fullfilled case");
            } else {
                CSLogger.error("Color is not green");
            }
        } else {
            CSLogger.error("Could not find the key.");
        }
    }

    /**
     * This method unlocks the dashboard from the home page
     */
    private void lockDashboard() {
        TraversingForDashboardModule.traverseToDashboardPage(waitForReload,
                browserDriver);
        dashboardPage.clkElement(waitForReload,
                dashboardPage.getBtnLockToggle());
        CSLogger.info("Cliced on Lock toggle button");
    }

    /**
     * This method configures the rules of dq app
     * 
     * @param productName contains name of product
     * @param childName contains child name
     * @param className contains class name
     * @param ruleName contains rule name
     * @param objectRelation contains object relation string
     * @param ruleConditionName contains rule condition name
     * @param ruleConditonPluginName contains rule condition plugin name
     * @param attributeRuleCondition contains attribute rule condition
     * @param criterionTypeForConditions contains criterion type for conditions
     * @param comparatorValue contains comparator value
     * @param tileName contains tile name
     * @param tileType contains tile type
     * @param tileCategory contains tile category
     * @param objectType contains object type
     * @param displayObject contains display object
     * @param selectionType contains selection type
     */
    @Test(priority = 3, dataProvider = "configureAndVerifyDQAppTest")
    public void testVerifyFulfilledStatus(String productName, String childName,
            String className, String ruleName, String objectRelation,
            String ruleConditionName, String ruleConditonPluginName,
            String attributeRuleCondition, String criterionTypeForConditions,
            String comparatorValue, String tileName, String tileType,
            String tileCategory, String objectType, String displayObject,
            String selectionType) {
        lockDashboard();
        verifyStatusOfTileByProgressBar(tileName, productName);
    }

    /**
     * This method verifies status of tile by progress bar present on the tile
     * 
     * @param tileName contains tile name
     * @param productName contains name of product
     */
    private void verifyStatusOfTileByProgressBar(String tileName,
            String productName) {
        boolean status = false;
        traverseToFrameWidget();
        status = getStatusOfTile(status, tileName);
        if (status == true) {
            catchTheTooltip(productName);
        } else {
            CSLogger.error("Tile might not have been created");
        }
    }

    /**
     * This method verifies the added tile on the dashboard
     * 
     * @param tileName contains name of tile
     */
    private void verifyConfiguredTile(String tileName) {
        boolean status = false;
        status = getStatusOfTile(status, tileName);
        if (status == true) {
            CSLogger.info(tileName + "  is present");
        } else {
            CSLogger.error("Failed to create and verify tile" + tileName);
        }
    }

    /**
     * This method gets tiles from the dashboard
     * 
     * @return list
     */
    private List<WebElement> getTile() {
        List<WebElement> list = browserDriver
                .findElements(By.xpath("//div[@class='dashboardTileTitle']"));
        return list;
    }

    /**
     * This method gets status of tile i.e if tile is present on dashboard or
     * not
     * 
     * @paaram status contains initial boolean value
     * @param tileName contains name of tile
     * @return status
     */
    private boolean getStatusOfTile(boolean status, String tileName) {
        List<WebElement> list = getTile();
        if (list.size() >= 1) {
            for (Iterator<WebElement> iterator = list.iterator(); iterator
                    .hasNext();) {
                WebElement listElement = iterator.next();
                if (listElement.getText().equals(tileName)) {
                    status = true;
                    break;
                }
            }
            if (status == true) {
                CSLogger.info(tileName + "  is present");
            } else {
                CSLogger.error(tileName + "  is not present ");
            }
        }
        return status;
    }

    /**
     * This method adds new tile
     * 
     * @param tileName contains tile name
     * @param tileType contains tile type
     * @param tileCategory contains tile category
     * @param objectType contains object type
     * @param displayObject contains display object
     * @param productName contains name of product
     * @param className contains class name
     * @param ruleName contains rule name
     * @param selectionType contains selection type string
     */
    private void addNewTile(String tileName, String tileType,
            String tileCategory, String objectType, String displayObject,
            String productName, String className, String ruleName,
            String selectionType) {
        dashboardPage.clkElement(waitForReload, dashboardPage.getBtnPlus());
        configureTile(tileName, tileType, tileCategory, objectType,
                displayObject, productName, className, ruleName, selectionType);
    }

    /**
     * This method congfigures the tile to be created
     * 
     * @param tileName contains tile name
     * @param tileType contains tile type
     * @param tileCategory contains tile category
     * @param objectType contains object type
     * @param displayObject contains display object
     * @param productName contains product name
     * @param className contains class name
     * @param ruleName contains rule name
     * @param selectionType contains selection type string
     */
    private void configureTile(String tileName, String tileType,
            String tileCategory, String objectType, String displayObject,
            String productName, String className, String ruleName,
            String selectionType) {
        dashboardPage.clkElement(waitForReload, dashboardPage.getTabNewTile());
        CSLogger.info("Clicked on new tile tab");
        CSUtility.tempMethodForThreadSleep(3000);
        selectTileType(tileType);
        enterDetails(tileName, dashboardPage.getTxtTileCaption());
        enterDetailsInCategoryTextbox(tileCategory);
        CSUtility.tempMethodForThreadSleep(1000);
        enterDetailsInTextbox(
                browserDriver.findElement(
                        By.xpath("//input[@placeholder='Object Type']")),
                objectType);
        selectElement();
        actions.sendKeys(Keys.TAB).build().perform();
        CSUtility.tempMethodForThreadSleep(6000);
        enterDetailsInTextbox(browserDriver.findElement(By.xpath(
                "//label[contains(text(),'Data Source')]/../div/div/div/div/input")),
                valueOfIdProduct);
        selectElement();
        scrollDown(browserDriver
                .findElement(By.xpath("//select[@name='displayType']")));

        selectSelectionType(selectionType);
        CSUtility.tempMethodForThreadSleep(5000);
        enterDetailsInTextbox(browserDriver.findElement(By.xpath(
                "//label[contains(text(),'Classes')]/../div/div/div/div/input")),
                className);
        selectElement();
        actions.sendKeys(Keys.ESCAPE).build().perform();
        CSUtility.tempMethodForThreadSleep(5000);
        enterDetailsInTextbox(browserDriver.findElement(By.xpath(
                "//label[contains(text(),'Rules')]/../div/div/div/div/input")),
                ruleName);
        selectElement();
        actions.sendKeys(Keys.TAB).build().perform();
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement displayObjectElement = enterDetailsInTextbox(
                browserDriver
                        .findElement(By.xpath("//select[@name='displayType']")),
                displayObject);
        displayObjectElement.sendKeys(Keys.ENTER);
        CSUtility.tempMethodForThreadSleep(2000);
        clkOk();
    }

    /**
     * Checks in and checks out the product
     * 
     * @param productName contains name of product
     * @param operation contains webelement check in or check out
     */
    private void checkInCheckoutProduct(String productName,
            WebElement operation) {
        try {
            csPortalHeader.clkBtnProducts(waitForReload);
            WebElement createdProductFolder = clkProductFolder(productName);
            checkInCheckout(createdProductFolder,
                    productPopup.getCsGuiPopupCheckIn());
            TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                    waitForReload, browserDriver);
            csGuiDialogContentId.clkElement(waitForReload, operation);
            createdProductFolder = clkProductFolder(productName);
            CSUtility.tempMethodForThreadSleep(2000);
            CSLogger.info("Product check in checked out successfully");
        } catch (Exception e) {
            CSLogger.error("Check in check out unsucessful", e);
            Assert.fail("Check in checkout unsuccessful");

        }
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
     * Selects the selection type
     * 
     * @param selectionType contains selection type
     */
    private void selectSelectionType(String selectionType) {
        Select selectType = new Select(browserDriver
                .findElement(By.xpath("//select[@name='selectionType']")));
        selectType.selectByVisibleText(selectionType);
    }

    /**
     * Scrolls down the scroller
     * 
     * @param element contains element upto which scroll down has to be
     *            performed
     */
    private void scrollDown(WebElement element) {
        traverseToFrameWidget();
        WebElement scroller = browserDriver.findElement(By.xpath(
                "(//div[contains(@id,'ui-tabs-tab')]//div[@class='ps-scrollbar-y'])[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, scroller);
        ((JavascriptExecutor) browserDriver)
                .executeScript("arguments[0].scrollIntoView();", element);
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * This method traverses to frame widget
     */
    private void traverseToFrameWidget() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame_234()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrameWidget()));
        CSLogger.info("Clicked on widget frame");

    }

    /**
     * This method selects the element from the drop down
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
     * @param element contains weblement
     * @param objectType contains object type string
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
     * This method enters the details in category text box
     * 
     * @param tileCategory contains name of tile category
     * @param favoritesFolder contains favorites folder name
     */
    private void enterDetailsInCategoryTextbox(String tileCategory) {
        try {
            WebElement txtCategory = browserDriver
                    .findElement(By.xpath("//input[@placeholder='Category']"));
            waitForReload.until(ExpectedConditions.visibilityOf(txtCategory));
            CSUtility.tempMethodForThreadSleep(2000);
            txtCategory.sendKeys(tileCategory);
        } catch (Exception e) {
            enterDetailsByRemovingExistingCategory(tileCategory);
        }
    }

    /**
     * This method method enters details in category textbox by removing
     * existing category names
     * 
     * @param tileCategory contains name of tile category
     * @param favoritesFolder contains favorites folder name
     */
    private void enterDetailsByRemovingExistingCategory(String tileCategory) {
        CSUtility.tempMethodForThreadSleep(2000);
        WebElement txtCategory = browserDriver
                .findElement(By.xpath("//div[contains(@id,'CSComboBoxView')]"
                        + "//input[@class='form-control CSTextFieldView']"));
        CSUtility.waitForElementToBeClickable(waitForReload, txtCategory);
        txtCategory.click();
        CSUtility.tempMethodForThreadSleep(2000);
        txtCategory.sendKeys(Keys.BACK_SPACE);
        txtCategory.sendKeys(tileCategory);
        CSUtility.tempMethodForThreadSleep(2000);
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
     * Returns the data sheet
     * 
     * @return configureAndVerifyDQAppSheetF
     */
    @DataProvider(name = "configureAndVerifyDQAppTest")
    public Object[][] configureAndVerifyDQApp() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("dashboardModuleTestCases"),
                configureAndVerifyDQAppSheet);
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
        classPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioSettingsNode = new PimStudioSettingsNode(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        rulesPage = new RulesPage(browserDriver);
        modalDialogPopupWindow = new ModalDialogPopupWindow(browserDriver);
    }
}
