/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IMoreOptionPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.WorkflowsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
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
 * This class contains test methods to create a workflow actions.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateWorkflowActionsTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private WorkflowsPage             workflowsPageInstance;
    private CSPortalWidget            csPortalWidgetInstance;
    private FrameLocators             iframeLocatorsInstance;
    private SoftAssert                softAssertion;
    private IProductPopup             productPopup;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private IMoreOptionPopup          moreOptionPopup;
    private Actions                   performAction;
    private String                    dataSheetName = "CreateWorkflowActions";

    /**
     * This test method creates workflow action and executes the action on
     * product folder.
     * 
     * @param workflowName String object contains name of the workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param firstState String object contains workflow state name.
     * @param secondState String object contains workflow state name.
     * @param workflowTabName String object contains workflow tab name.
     * @param productFolderName String object contains name of product folder
     * @param productTabName String object contains product tab name.
     */
    @Test(dataProvider = "createWorkflowActionsTestData")
    public void testCreateWorkflowActions(String workflowName,
            String workflowType, String workflowActionName,
            String workflowActionDescription, String firstState,
            String secondState, String workflowTabName,
            String productFolderName, String productTabName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            checkWorkflowExists(workflowName, workflowType);
            checkWorkflowStateExists(workflowName, firstState);
            checkWorkflowStateExists(workflowName, secondState);
            createWorkflowAction(workflowName, workflowTabName,
                    workflowActionName, workflowActionDescription, firstState,
                    secondState);
            switchToPimAndExpandProductTree(waitForReload);
            createProductFolder(productFolderName);
            setWorkflowStateOfProduct(productFolderName, productTabName,
                    workflowName, firstState);
            selectCreatedWorkflowAction(workflowActionName);
            verifyWorkflowState(secondState);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testCreateWorkflowActions",
                    e);
            Assert.fail("Automation error in : testCreateWorkflowActions", e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandProductTree(WebDriverWait waitForReload) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method creates a product folder.
     * 
     * @param productName String object contains name of product folder.
     */
    private void createProductFolder(String productName) {
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered value " + productName + " in the textfield.");
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimProductTreeSection();
    }

    /**
     * Selects the workflow state for product.
     * 
     * @param productFolderName String object contains name of product folder
     * @param productTabName String object contains product tab name.
     * @param workflowName String object contains name of the workflow.
     * @param startState String object contains name of workflow state.
     */
    private void setWorkflowStateOfProduct(String productFolderName,
            String productTabName, String workflowName, String startState) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(productFolderName)));
        WebElement productFolder = browserDriver
                .findElement(By.linkText(productFolderName));
        productFolder.click();
        goToProductsRightSectionWindow();
        clickOnGivenTab(productTabName, waitForReload);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By
                        .xpath("//select[contains(@id,'PdmarticleStateID')]")));
        WebElement productState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'PdmarticleStateID')]"));
        productState.click();
        Select select = new Select(productState);
        select.selectByVisibleText(workflowName + " - " + startState);
        CSLogger.info("product set to workflow state : " + workflowName + " - "
                + startState);
        checkInProduct();
    }

    /**
     * This method performs check in operation on product folder
     */
    private void checkInProduct() {
        try {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCheckIn());
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarCheckIn(waitForReload);
        } catch (Exception e) {
            CSLogger.info("Product already in check In state");
        }
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToProductsRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimProductTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * Checks whether the created workflow exists.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowTypeName String object contains type of workflow.
     */
    private void checkWorkflowExists(String workflowName,
            String workflowTypeName) {
        try {
            goToWorkflowNode();
            clkOnWorkflowType(workflowTypeName);
            clkOnWorkflow(workflowName);
            CSUtility.tempMethodForThreadSleep(1000);
            CSLogger.info("Workflow " + workflowName + " exists");
        } catch (Exception e) {
            CSLogger.error("Workflow do not exists", e);
            Assert.fail("Workflow do not exists");
        }
    }

    /**
     * This method clicks on workflow node under settings tab.
     */
    private void goToWorkflowNode() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
        traverseToWorkflowTreeView();
        workflowsPageInstance.clkOnWorkflowsNode(waitForReload);
    }

    /**
     * Switches frames of system preferences left pane tree located under
     * setting's tab.
     */
    private void traverseToWorkflowTreeView() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * This method clicks on given workflow type.
     * 
     * @param workflowTypeName String object contains type of workflow.
     */
    private void clkOnWorkflowType(String workflowTypeName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowTypeName)));
        WebElement workflowType = browserDriver
                .findElement(By.linkText(workflowTypeName));
        workflowType.click();
        CSLogger.info("Clicked on workflow type : " + workflowTypeName);
    }

    /**
     * This method clicks on given workflow.
     * 
     * @param workflowName String object contains name of workflow.
     */
    private void clkOnWorkflow(String workflowName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(workflowName)));
        WebElement workflow = browserDriver
                .findElement(By.linkText(workflowName));
        workflow.click();
        CSLogger.info("Clicked on workflow : " + workflowName);
    }

    /**
     * This method clicks on provided tab name.
     * 
     * @param workflowTabName String object contains name of workflow tab name.
     * @param waitForReload WebDriverWait object.
     */
    private void clickOnGivenTab(String workflowTabName,
            WebDriverWait waitForReload) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath("//nobr[contains(text(),'"
                        + workflowTabName + "')]/parent::*")));
        WebElement element = browserDriver
                .findElement(By.xpath("//nobr[contains(text(),'"
                        + workflowTabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on " + workflowTabName + " tab ");
    }

    /**
     * This method creates workflow action.
     * 
     * @param workflowName String object contains name of the workflow.
     * @param workflowTabName String object contains workflow tab name.
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param firstState String object contains workflow state name.
     * @param secondState String object contains workflow state name.
     */
    private void createWorkflowAction(String workflowName,
            String workflowTabName, String workflowActionName,
            String workflowActionDescription, String firstState,
            String secondState) {
        try {
            clkOnWorkflow(workflowName);
            traverseToWorkflowCenterPane();
            clickOnGivenTab(workflowTabName, waitForReload);
            CSUtility.tempMethodForThreadSleep(3000);
            String parentWindow = browserDriver.getWindowHandle();
            traverseToFlowChart();
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='swimlane']/div/div[contains(text(),'"
                            + workflowName + "')]")));
            CSUtility.scrollUpOrDownToElement(
                    browserDriver.findElement(By
                            .xpath("//div[@id='container-content']/table[1]")),
                    browserDriver);
            WebElement firstStateElement = browserDriver.findElement(
                    By.xpath("//td[contains(text(),'" + firstState + "')]"));
            WebElement secondStateElement = browserDriver.findElement(
                    By.xpath("//td[contains(text(),'" + secondState + "')]"));
            performAction.dragAndDrop(firstStateElement, secondStateElement)
                    .perform();
            CSLogger.info("Dragged " + firstState + " and dropped it on "
                    + secondState);
            String workflowActionWindow = (String) (browserDriver
                    .getWindowHandles().toArray())[1];
            browserDriver.switchTo().window(workflowActionWindow);
            configureWorkflowActions(workflowActionName,
                    workflowActionDescription, firstState, secondState);
            CSLogger.info("Workflow action : " + workflowActionName
                    + "created successfully ");
            browserDriver.close();
            browserDriver.switchTo().window(parentWindow);
        } catch (NoSuchWindowException e) {
            CSLogger.error("A window 'Add new WorkFlow Action' didn't pop up.",
                    e);
            Assert.fail("A window 'Add new WorkFlow Action' didn't pop up.");
        }
    }

    /**
     * Switches frames of system preferences center pane located under setting's
     * tab.
     */
    private void traverseToWorkflowCenterPane() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * Verifies that workflow state are created and exists.
     * 
     * @param workflowName String object contains name of workflow.
     * @param workflowStateName String object contains name of workflow state.
     */
    private void checkWorkflowStateExists(String workflowName,
            String workflowStateName) {
        try {
            String workflowId = getWorkflowId(workflowName);
            clkOnWorkflowState(workflowId, workflowStateName);
        } catch (Exception e) {
            CSLogger.error("Workflow state : " + workflowStateName
                    + " does not exists", e);
            Assert.fail("Workflow state : " + workflowStateName
                    + " does not exists");
        }
    }

    /**
     * This method returns the ID of the workflow passed as argument
     * 
     * @param workflowName String containing name of workflow.
     * @return workflow Id String object contains workflow ID.
     */
    private String getWorkflowId(String workflowName) {
        traverseToWorkflowTreeView();
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(
                browserDriver.findElement(By.linkText(workflowName))).perform();
        traverseToWorkflowCenterPane();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + workflowName + "')]")));
        CSUtility.tempMethodForThreadSleep(5000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        String workflowId = browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
        return workflowId;
    }

    /**
     * This methods clicks on given workflow state.
     * 
     * @param workflowId String object contains Id of workflow.
     * @param workflowStateName String object contains workflow state name.
     */
    private void clkOnWorkflowState(String workflowId,
            String workflowStateName) {
        traverseToWorkflowTreeView();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[@id='0divWorkflows@Workflow_" + workflowId
                                + "']/span/table/tbody/tr/td/span/table/tbody/"
                                + "tr/td[2]/a/span/span[contains(text(),'"
                                + workflowStateName + "')]")));
        WebElement workflowState = browserDriver.findElement(
                By.xpath("//span[@id='0divWorkflows@Workflow_" + workflowId
                        + "']/span/table/tbody/tr/td/span/table/tbody/tr/td[2]"
                        + "/a/span/span[contains(text(),'" + workflowStateName
                        + "')]"));
        workflowState.click();
        CSLogger.info("Clicked on workflow State : " + workflowStateName);
    }

    /**
     * Enters required workflow action details.
     * 
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param requiredStateName String object contains workflow state name.
     * @param resultStateName String object contains workflow state name.
     */
    private void configureWorkflowActions(String workflowActionName,
            String workflowActionDescription, String requiredStateName,
            String resultStateName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[contains(@id,'ActionActionName')]")));
        WebElement actionName = browserDriver.findElement(
                By.xpath("//input[contains(@id,'ActionActionName')]"));
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(
                        By.xpath("//select[contains(@id,'ActionStateID')]")));
        WebElement requiredState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionStateID')]"));
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//select[contains(@id,'ActionResultStateID')]")));
        WebElement resultState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionResultStateID')]"));
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//textarea[contains(@id,'ActionDescription')]")));
        WebElement actionDescription = browserDriver.findElement(
                By.xpath("//textarea[contains(@id,'ActionDescription')]"));
        enterWorkflowActionDetails(actionName, workflowActionName);
        enterWorkflowActionDetails(actionDescription,
                workflowActionDescription);
        String requiredDefaultState = getDefaultSelectedStates(requiredState);
        String resultDefaultState = getDefaultSelectedStates(resultState);
        if (requiredDefaultState.equals(requiredStateName)
                && resultDefaultState.contains(resultStateName)) {
            CSLogger.info("Required state is auto-selected as : "
                    + requiredStateName + "Result state is  auto-selected as : "
                    + resultStateName + "teststep passed");
        } else {
            CSLogger.error(
                    "Error in teststep : Required state is auto-selected as : "
                            + requiredStateName
                            + "Result state is  auto-selected as : "
                            + resultStateName + "teststep failed");
            softAssertion
                    .fail("Error in teststep : Required state is auto-selected "
                            + "as : " + requiredStateName
                            + "Result state is  auto-selected as : "
                            + resultStateName + "teststep failed");
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Workflow action configured successfully");
    }

    /**
     * Returns the default selected states for given element.
     * 
     * @param element Contains WebElement of workflow state.
     * @return String object containing selected states name.
     */
    private String getDefaultSelectedStates(WebElement element) {
        Select defaultState = new Select(element);
        List<WebElement> selectedState = defaultState.getAllSelectedOptions();
        return selectedState.get(0).getText();
    }

    /**
     * This method enters workflow action information for eg. workflow action
     * name,description.
     * 
     * @param actionElement WebElement of workflow action.
     * @param actionInfo String object contains information about the workflow
     *            action.
     */
    private void enterWorkflowActionDetails(WebElement actionElement,
            String actionInfo) {
        CSUtility.waitForElementToBeClickable(waitForReload, actionElement);
        actionElement.click();
        actionElement.clear();
        actionElement.sendKeys(actionInfo);
        CSLogger.info("Entered workflow action detail as : " + actionInfo);
    }

    /**
     * Traverses to ViewWorkflow frame.
     */
    private void traverseToFlowChart() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmViewWorkflow()));
        CSLogger.info("Traversed till ViewWorkflow frame ");
    }

    /**
     * Selects the workflow action to be executed on product.
     * 
     * @param actionName String object contains name of action.
     */
    private void selectCreatedWorkflowAction(String actionName) {
        csGuiToolbarHorizontalInstance.clkBtnShowMoreOption(waitForReload);
        moreOptionPopup.selectPopupDivMenu(waitForReload,
                moreOptionPopup.getCsGuiPopupMenuWorkflow(), browserDriver);
        selectCreatedAction(actionName);
    }

    /**
     * This method selects created workflow action.
     * 
     * @param actionName String object contains name of workflow action to be
     *            selected.
     */
    private void selectCreatedAction(String actionName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPopupDivSubFrame()));
        CSLogger.info("Switched to cs pop div sub frame");
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//td[@title='" + actionName + "']")));
        browserDriver.findElement(By.xpath("//td[@title='" + actionName + "']"))
                .click();
        CSLogger.info(
                "Workflow action : " + actionName + "selected from popup menu");
    }

    /**
     * Verifies the state that is displayed after execution of workflow.
     * 
     * @param resultStateName String object contains name of workflow state.
     */
    private void verifyWorkflowState(String resultStateName) {
        goToProductsRightSectionWindow();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(
                        By.xpath("//span[contains(@id,'PdmarticleStateID')]")));
        String workflowState = browserDriver
                .findElement(
                        By.xpath("//span[contains(@id,'PdmarticleStateID')]"))
                .getText();
        if (workflowState.contains(resultStateName)) {
            CSLogger.info(
                    "Product is in checked in state and " + resultStateName
                            + " of the workflow is assigned after execution : "
                            + "teststep passed");
        } else {
            CSLogger.error(
                    "Error in test step : Product should be in checked in state "
                            + "and " + resultStateName
                            + " of the workflow should be assigned "
                            + "after execution : teststep failed");
            softAssertion
                    .fail("Error in test step : Product should be in checked in"
                            + " state and " + resultStateName
                            + " of the workflow "
                            + "should be assigned after execution : "
                            + "teststep failed");
        }
    }

    /**
     * This is a data provider method.
     * 
     * @return Array
     */
    @DataProvider(name = "createWorkflowActionsTestData")
    public Object[][] getWorkflowActionData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("workflowModuleTestCases"),
                dataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        workflowsPageInstance = SettingsLeftSectionMenubar
                .getWorkflowsNode(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        softAssertion = new SoftAssert();
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        moreOptionPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        performAction = new Actions(browserDriver);
    }
}
