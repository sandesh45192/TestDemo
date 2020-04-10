/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.workflows;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.SettingsPage;
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
 * This class contains test methods to create a workflow 'Email' action.
 * 
 * @author CSAutomation Team
 *
 */
public class CreateWorkflowEmailActionsTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private WorkflowsPage             workflowsPageInstance;
    private CSPortalWidget            csPortalWidgetInstance;
    private FrameLocators             iframeLocatorsInstance;
    private SoftAssert                softAssertion;
    private IProductPopup             productPopup;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private SettingsPage              settingsPageInstance;
    private CSPopupDivPim             csPopupDivInstance;
    private Actions                   performAction;
    private String                    dataSheetName = "CreateWorkflowEmailActions";

    /**
     * This test method creates workflow Email action and executes the action on
     * product folder.
     * 
     * @param workflowName String object contains name of the workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param requiredState String object contains workflow state name.
     * @param resultState String object contains workflow state name.
     * @param emailMode String object contains mode of email.
     * @param emailAddress String object contains email address.
     * @param emailFormat String object contains format of mail.
     * @param emailTemplateString object contains template of mail.
     * @param emailTabData String object contains fields in email tab.
     * @param actionComponentName String object contains name of action
     *            component.
     * @param productFolderName String object contains name of product folder.
     * @param productTabNameString String object contains name of product tab.
     * @param workflowTabName String object contains name of workflow tab.
     */
    @Test(dataProvider = "createWorkflowEmailActionsTestData")
    public void testCreateWorkflowEmailActions(String workflowName,
            String workflowType, String workflowActionName,
            String workflowActionDescription, String requiredState,
            String resultState, String emailMode, String emailAddress,
            String emailFormat, String emailTemplate, String emailTabData,
            String actionComponentName, String productFolderName,
            String productTabName, String workflowTabName) {
        try {
            String emailTabFields[] = emailTabData.split(",");
            waitForReload = new WebDriverWait(browserDriver, 180);
            checkWorkflowExists(workflowName, workflowType);
            checkWorkflowStateExists(workflowName, requiredState);
            checkWorkflowStateExists(workflowName, resultState);
            setEmailAndMessagesMode(emailMode);
            createWorkflowAction(workflowName, workflowType, workflowTabName,
                    workflowActionName, workflowActionDescription,
                    requiredState, resultState, actionComponentName);
            configureMail(workflowName, requiredState, workflowActionName,
                    actionComponentName, emailAddress, emailFormat,
                    emailTemplate, emailTabFields);
            switchToPimAndExpandProductTree(waitForReload);
            createProductFolder(productFolderName);
            setWorkflowStateOfProduct(productFolderName, productTabName,
                    workflowName, requiredState);
            String parentWindow = browserDriver.getWindowHandle();
            selectCreatedWorkflowAction(parentWindow, productFolderName,
                    workflowActionName);
            verifyMailDebugOutputWindow(parentWindow, emailAddress,
                    productFolderName);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : testCreateWorkflowEmailActions", e);
            Assert.fail(
                    "Automation error in : " + "testCreateWorkflowEmailActions",
                    e);
        }
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
            CSLogger.info("Workflow " + workflowName + " exists");
        } catch (Exception e) {
            CSLogger.error("Workflow do not exists", e);
            Assert.fail("Workflow do not exists");
        }
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
        return browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
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
                                + "']/span/table/tbody/tr/td/span/table/tbody/tr"
                                + "/td[2]/a/span/span[contains(text(),'"
                                + workflowStateName + "')]")));
        WebElement workflowState = browserDriver.findElement(
                By.xpath("//span[@id='0divWorkflows@Workflow_" + workflowId
                        + "']/span/table/tbody/tr/td/span/table/tbody/tr/td[2]/a"
                        + "/span/span[contains(text(),'" + workflowStateName
                        + "')]"));
        workflowState.click();
        CSLogger.info("Clicked on workflow State : " + workflowStateName);
    }

    /**
     * Selects the Email&Messages mode.
     * 
     * @param emailMode String object contains email mode.
     */
    private void setEmailAndMessagesMode(String emailMode) {
        traverseToWorkflowTreeView();
        settingsPageInstance.clkOnBtnSettingsNode(waitForReload);
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, iframeLocatorsInstance);
        iframeLocatorsInstance.switchToSplitAreaFrame(waitForReload);
        CSUtility.tempMethodForThreadSleep(5000);
        settingsPageInstance.clkOnBtnCore(waitForReload);
        settingsPageInstance.clkOnBtnCoreEmail(waitForReload);
        selectValueFromDrpDwn(settingsPageInstance.getDrpDwnEmailMode(),
                emailMode);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method clicks on provided tab .
     * 
     * @param workflowTabName String name of the tab in the product view.
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
     * Traverses to ViewWorkflow frame.
     */
    private void traverseToFlowChart() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmViewWorkflow()));
        CSLogger.info("Traversed till ViewWorkflow frame ");
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
     * Selects the drop down element.
     * 
     * @param drpdwnElement this is type of drop down webElement
     * @param valueOfDrpDwn text value in drop down webElement
     */
    private void selectValueFromDrpDwn(WebElement drpdwnElement,
            String valueOfDrpDwn) {
        drpdwnElement.click();
        Select element = new Select(drpdwnElement);
        element.selectByVisibleText(valueOfDrpDwn);
        CSLogger.info("Drop down option " + valueOfDrpDwn + " selected");
    }

    /**
     * This method creates workflow action.
     * 
     * @param workflowName String object contains name of the workflow.
     * @param workflowType String object contains type of workflow.
     * @param workflowTabName String object contains name of workflow tab.
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param requiredState String object contains workflow state name.
     * @param resultState String object contains workflow state name.
     * @param actionComponentName String object contains name of action
     *            component here it is Email.
     */
    private void createWorkflowAction(String workflowName,
            String workflowTypeName, String workflowTabName,
            String workflowActionName, String workflowActionDescription,
            String requiredStateName, String resultStateName,
            String actionComponentName) {
        try {
            goToWorkflowNode();
            clkOnWorkflowType(workflowTypeName);
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
            WebElement firstState = browserDriver.findElement(By.xpath(
                    "//td[contains(text(),'" + requiredStateName + "')]"));
            WebElement secondState = browserDriver.findElement(By
                    .xpath("//td[contains(text(),'" + resultStateName + "')]"));
            performAction.dragAndDrop(firstState, secondState).perform();
            CSLogger.info(
                    "Dragged first state and dropped it on second state ");
            String workflowActionWindow = (String) (browserDriver
                    .getWindowHandles().toArray())[1];
            browserDriver.switchTo().window(workflowActionWindow);
            configureWorkflowActions(workflowActionName,
                    workflowActionDescription, requiredStateName,
                    resultStateName, actionComponentName);
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
     * Enters required workflow action details.
     * 
     * @param workflowActionName String object contains name of workflow action
     *            to be created.
     * @param workflowActionDescription String object contains workflow action
     *            description.
     * @param requiredStateName String object contains workflow state name.
     * @param resultStateName String object contains workflow state name.
     * @param actionComponentName String object contains name of action
     *            component here it is Email.
     */
    private void configureWorkflowActions(String workflowActionName,
            String workflowActionDescription, String requiredStateName,
            String resultStateName, String actionComponentName) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[contains(@id,'ActionActionName')]")));
        WebElement actionName = browserDriver.findElement(
                By.xpath("//input[contains(@id,'ActionActionName')]"));
        WebElement actionDescription = browserDriver.findElement(
                By.xpath("//textarea[contains(@id,'ActionDescription')]"));
        enterWorkflowActionDetails(actionName, workflowActionName);
        enterWorkflowActionDetails(actionDescription,
                workflowActionDescription);
        selectActionComponent(actionComponentName);
        WebElement requiredState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionStateID')]"));
        String requiredDefaultState = getDefaultSelectedStates(requiredState);
        WebElement resultState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionResultStateID')]"));
        String resultDefaultState = getDefaultSelectedStates(resultState);
        if (requiredDefaultState.equals(requiredStateName)
                && resultDefaultState.contains(resultStateName)) {
            CSLogger.info(
                    "Required state is auto-selected as : " + requiredStateName
                            + " and Result state is auto-selected as : "
                            + resultStateName + " teststep passed");
        } else {
            CSLogger.error(
                    "Error in teststep : Required state is auto-selected as : "
                            + requiredStateName
                            + " and Result state is  auto-selected as : "
                            + resultStateName + " teststep failed");
            softAssertion
                    .fail("Error in teststep : Required state is auto-selected as : "
                            + requiredStateName
                            + " and Result state is  auto-selected as : "
                            + resultStateName + " teststep failed");
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("Workflow action configured successfully");
    }

    /**
     * Selects the action component from action window.
     * 
     * @param actionComponentName String object contains name of action
     *            component.
     */
    private void selectActionComponent(String actionComponentName) {
        WebElement option = browserDriver.findElement(By.xpath(
                "//option[contains(text(),'" + actionComponentName + "')]"));
        performAction.doubleClick(option).perform();
        CSLogger.info("Action component : " + actionComponentName + "selected");
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
        CSLogger.info("Entered workflow state detail as : " + actionInfo);
    }

    /**
     * Returns the default selected states for given element.
     * 
     * @param element Contains WebElement of workflow state.
     * @return selected states name.
     */
    private String getDefaultSelectedStates(WebElement element) {
        Select defaultState = new Select(element);
        List<WebElement> requiredSelectedState = defaultState
                .getAllSelectedOptions();
        return requiredSelectedState.get(0).getText();
    }

    /**
     * Configures the mail action,enters details for eg,mail address ,template
     * of mail etc.
     * 
     * @param stateName String object contains name of workflow state.
     * @param actionName String object contains name of workflow action.
     * @param workflowTabName String object contains name of workflow tab.
     * @param emailAddress String object contains email address.
     * @param emailFormat String object contains format of mail.
     * @param emailTemplate String object contains template of mail.
     * @param emailTabFields String object contains fields in email tab.
     */
    private void configureMail(String workflowName, String stateName,
            String actionName, String workflowTabName, String emailAddress,
            String emailFormat, String emailTemplate, String[] emailTabFields) {
        String workflowId = getWorkflowId(workflowName);
        clkOnWorkflowState(workflowId, stateName);
        WebElement workflowAction = browserDriver
                .findElement(By.linkText(actionName));
        performAction.doubleClick(workflowAction).perform();
        traverseToWorkflowCenterPane();
        clickOnGivenTab(workflowTabName, waitForReload);
        verifyMailTabFields(emailTabFields);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By
                        .xpath("//tr[@id='AttributeRow_MailTo']/td[2]/input")));
        WebElement mailToEmailAddressElement = browserDriver.findElement(
                By.xpath("//tr[@id='AttributeRow_MailTo']/td[2]/input"));
        enterWorkflowActionDetails(mailToEmailAddressElement, emailAddress);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By
                        .xpath("//select[contains(@id,'ActionMailFormat')]")));
        WebElement mailFormatElement = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionMailFormat')]"));
        selectValueFromDrpDwn(mailFormatElement, emailFormat);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//select[contains(@id,'ActionMailTemplateFile')]")));
        WebElement mailTemplateElement = browserDriver.findElement(
                By.xpath("//select[contains(@id,'ActionMailTemplateFile')]"));
        selectValueFromDrpDwn(mailTemplateElement, emailTemplate);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
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
            String tabName, String workflowName, String startState) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(productFolderName)));
        WebElement productFolder = browserDriver
                .findElement(By.linkText(productFolderName));
        productFolder.click();
        CSLogger.info("Clicked on product folder : " + productFolderName);
        goToProductsRightSectionWindow();
        clickOnGivenTab(tabName, waitForReload);
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By
                        .xpath("//select[contains(@id,'PdmarticleStateID')]")));
        WebElement productState = browserDriver.findElement(
                By.xpath("//select[contains(@id,'PdmarticleStateID')]"));
        productState.click();
        Select select = new Select(productState);
        select.selectByVisibleText(workflowName + " - " + startState);
        checkInProduct();
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToProductsRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * This method performs check in operation on product folder if product is
     * in checkout state else no operation will be performed
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
     * This method traverse to the product tree in left most section of PIM
     * module.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void goToPimProductTreeSection() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
    }

    /**
     * This method selects configured workflow action by performing right click
     * operation on product folder.
     * 
     * @param parentWindow String object contains window handle.
     * @param productFolderName String object contains name of product folder.
     * @param actionName String object contains name of workflow action.
     */
    private void selectCreatedWorkflowAction(String parentWindow,
            String productFolderName, String actionName) {
        goToPimProductTreeSection();
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By.linkText(productFolderName)));
        WebElement product = browserDriver
                .findElement(By.linkText(productFolderName));
        CSUtility.rightClickTreeNode(waitForReload, product, browserDriver);
        csPopupDivInstance.traverseToCsPopupDivFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//td[contains(text(),'" + actionName + "')]/..")));
        WebElement popupAction = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + actionName + "')]/.."));
        popupAction.click();
        CSLogger.info("Workflow action : " + actionName + " selected");
    }

    /**
     * Performs verification of debug email output window.
     * 
     * @param parentWindow String object contains window handle.
     * @param mailToEmailAddress String object contains email address.
     * @param productFolderName String object contains name of product folder.
     */
    private void verifyMailDebugOutputWindow(String parentWindow,
            String mailToEmailAddress, String productFolderName) {
        CSUtility.tempMethodForThreadSleep(5000);
        String mailDebugWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(mailDebugWindow);
        int receiverMailId = browserDriver
                .findElements(By.xpath(
                        "//td[contains(text(),'" + mailToEmailAddress + "')]"))
                .size();
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(
                        By.xpath("//td[contains(text(),'Subject')]/../td[2]")),
                browserDriver);
        String mailSubject = browserDriver
                .findElement(
                        By.xpath("//td[contains(text(),'Subject')]/../td[2]"))
                .getText();
        if (receiverMailId != 0 && mailSubject.contains(productFolderName)) {
            CSLogger.info(
                    "Debug mail window verified successfully : teststep passed");
        } else {
            CSLogger.error(
                    "Debug mail window verification failed : teststep failed");
            softAssertion.fail(
                    "Debug mail window verification failed : teststep failed");
        }
        browserDriver.close();
        browserDriver.switchTo().window(parentWindow);
    }

    /**
     * Verifies the fields of Email tab.
     * 
     * @param emailTabFields String object contains name of Email tab fields.
     */
    private void verifyMailTabFields(String[] emailTabFields) {
        CSUtility.waitForElementToBeClickable(waitForReload,
                browserDriver.findElement(By
                        .xpath("(//table[contains(@class,'CSGuiTable GuiEditorTable')])"
                                + "[2]")));
        WebElement table = browserDriver.findElement(By
                .xpath("(//table[contains(@class,'CSGuiTable GuiEditorTable')])[2]//"
                        + "tr"));
        List<WebElement> cells = table.findElements(
                By.xpath("//td/div[@class='CSGuiEditorLabelContainer']"));
        ArrayList<String> mailData = new ArrayList<>();
        for (WebElement cell : cells) {
            if (!(cell.getText().equals(""))) {
                mailData.add(cell.getText());
            }
        }
        for (int index = 0; index < emailTabFields.length; index++) {
            if (!mailData.contains(emailTabFields[index])) {
                Assert.fail("mail tab doesn't contain expected mail filed : "
                        + emailTabFields[index]);
            }
        }
        CSLogger.info("Mail tab fields verified successfully ");
    }

    /**
     * This is a data provider method.
     * 
     * @return Array
     */
    @DataProvider(name = "createWorkflowEmailActionsTestData")
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
        settingsPageInstance = SettingsLeftSectionMenubar
                .getSettingsNode(browserDriver);
        csPopupDivInstance = new CSPopupDivPim(browserDriver);
        performAction = new Actions(browserDriver);
    }
}
