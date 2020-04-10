/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to create a parent product folder and
 * four child products during runtime and performs distribute content operation
 * on child products i.e clone an object,remove an object and add an object.
 * 
 * 
 * @author CSAutomation Team
 *
 */
public class DistributeProductContentObjectTest extends AbstractTest {

    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim             csPopupDiv;
    private SoftAssert                softAssertion;
    private CSGuiListFooter           CSGuiListFooterInstance;
    private WebDriverWait             waitForReload;
    private String                    mainWindowHandle;
    private String                    distributeContentWindowHandle;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private IProductPopup             productPopup;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;

    private String                    dataSheetName = "DistributeProductContentObject";

    /**
     * This test method clones an object and also verifies that while ok of
     * 'clone object' popup is selected clone of object is created and if cancel
     * is selected the object is not clonned.
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param firstChildProductName String Object containing the child product
     *            name.
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param thirdChildProductName String Object containing the child product
     *            name.
     * @param fourthChildProductName String Object containing the child product
     *            name.
     */
    @Test(priority = 1, dataProvider = "distributeProductContentObjectTestData")
    public void testDistributeContentCloneObject(String parentProductName,
            String classNameAssignedToParent, String firstChildProductName,
            String secondChildProductName, String thirdChildProductName,
            String fourthChildProductName, String classNameAssignedToChild) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            switchToPimAndExpandProductTree(waitForReload);
            createParentProduct(waitForReload, pimStudioProductsNodeInstance,
                    parentProductName);
            CSLogger.info("Created parent product.");
            createChildProduct(parentProductName, firstChildProductName);
            createChildProduct(parentProductName, secondChildProductName);
            createChildProduct(parentProductName, thirdChildProductName);
            createChildProduct(parentProductName, fourthChildProductName);
            selectChildProductsToDistributeContent(parentProductName,
                    firstChildProductName, secondChildProductName,
                    thirdChildProductName);
            mainWindowHandle = performDistributeContentOperation();
            checkDistributeContentWindow();
            distributeContentWindowHandle = getDistributeContentWindowHandle();
            performCloneObjectOperation(thirdChildProductName, false);
            performCloneObjectOperation(thirdChildProductName, true);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : testDistributeContentCloneObject",
                    e);
            Assert.fail(
                    "Automation error in : testDistributeContentCloneObject",
                    e);
        }
    }

    /**
     * This test method removes an object(child product) and also verifies that
     * while ok of popup is selected object is removed successfully and if
     * cancel is selected the object is not removed.
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param firstChildProductName String Object containing the child product
     *            name.
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param thirdChildProductName String Object containing the child product
     *            name.
     * @param fourthChildProductName String Object containing the child product
     *            name.
     */
    @Test(priority = 2, dataProvider = "distributeProductContentObjectTestData")
    public void testDistributeContentRemoveObject(String parentProductName,
            String classNameAssignedToParent, String firstChildProductName,
            String secondChildProductName, String thirdChildProductName,
            String fourthChildProductName, String classNameAssignedToChild) {
        try {
            checkDistributeContentWindowExists();
            performRemoveObjectOperation(false, secondChildProductName);
            performRemoveObjectOperation(true, secondChildProductName);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : testDistributeContentRemoveObject",
                    e);
            Assert.fail(
                    "Automation error in : testDistributeContentRemoveObject",
                    e);
        }
    }

    /**
     * This test method adds an object from data selection dialog window and
     * also handles ok and cancel operations
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param firstChildProductName String Object containing the child product
     *            name.
     * @param secondChildProductName String Object containing the child product
     *            name.
     * @param thirdChildProductName String Object containing the child product
     *            name.
     * @param fourthChildProductName String Object containing the child product
     *            name.
     */
    @Test(priority = 3, dataProvider = "distributeProductContentObjectTestData")
    public void testDistributeContentAddObject(String parentProductName,
            String classNameAssignedToParent, String firstChildProductName,
            String secondChildProductName, String thirdChildProductName,
            String fourthChildProductName, String classNameAssignedToChild) {
        try {
            checkDistributeContentWindowExists();
            switchToMainWindowTab();
            goToPimProductTreeSection();
            assignClassToParentProduct(parentProductName,
                    classNameAssignedToParent);
            goToPimProductTreeSection();
            assignClassToChildProduct(fourthChildProductName,
                    classNameAssignedToChild);
            CSUtility.tempMethodForThreadSleep(2000);
            performAddObjectOperation(parentProductName, fourthChildProductName,
                    false);
            CSUtility.tempMethodForThreadSleep(2000);
            performAddObjectOperation(parentProductName, fourthChildProductName,
                    true);
            verifyNonEditableAttributes();
            closeDistributeContentWindowTab();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in : testDistributeContentAddObject", e);
            Assert.fail("Automation error in : testDistributeContentAddObject",
                    e);
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
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        pimStudioProductsNodeInstance.clkBtnPimProductsNode(waitForReload,
                browserDriver);
    }

    /**
     * This method creates the parent product with supplied name.
     * 
     * @param waitForReload WebDriverWait Object
     * @param pimStudioProductsNode PimStudioProductsNode Object
     * @param parentProductName String Object containing the value for parent
     *            product Name.
     */
    private void createParentProduct(WebDriverWait waitForReload,
            PimStudioProductsNodePage pimStudioProductsNode,
            String parentProductName) {
        createProduct(parentProductName,
                pimStudioProductsNode.getBtnPimProductsNode(),
                csPopupDiv.getCsGuiPopupMenuCreateNew());
        CSLogger.info("Created Parent product");
    }

    /**
     * This method creates the child product with the supplied name and the
     * supplied parent product name.
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param childProductName String Object containing the child product name.
     * @param waitForReload WebDriverWait Object
     */
    private void createChildProduct(String parentProductName,
            String childProductName) {
        WebElement element = pimStudioProductsNodeInstance
                .expandNodesByProductName(waitForReload, parentProductName);
        createProduct(childProductName, element,
                csPopupDiv.getCsGuiPopupMenuNewChild());
        CSLogger.info("Created new child product");
    }

    /**
     * This method performs operation of creating product.
     * 
     * @param productName String object containing name of product it can be
     *            child product or parent product
     * @param nodeElement WebElement of either products node or parent product
     * @param popupMenuOption WebElement containing menu option.
     */
    private void createProduct(String productName, WebElement nodeElement,
            WebElement popupMenuOption) {
        CSUtility.rightClickTreeNode(waitForReload, nodeElement, browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload, popupMenuOption,
                browserDriver);
        csPopupDiv.enterValueInDialogue(waitForReload, productName);
        CSLogger.info("Entered value " + productName + " in the textfield.");
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        goToPimProductTreeSection();
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
     * This method selects the distribute content option from the marked drop
     * down and also returns the current window handle
     * 
     * @return windowHandle
     */
    private String performDistributeContentOperation() {
        String windowHandle = selectDistributeContentOption();
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        CSLogger.info(
                "clicked on ok of pop up for distribute content operation");
        return windowHandle;
    }

    /**
     * This method selects the child product so that distribute content can be
     * performed
     * 
     * @param productFolderName
     * @param childProductOne String Object containing the child product name.
     * @param childProductTwo String Object containing the child product name.
     * @param childProductThree String Object containing the child product name.
     */
    private void selectChildProductsToDistributeContent(
            String productFolderName, String childProductOne,
            String childProductTwo, String childProductThree) {
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        goToProductsRightSectionWindow();
        String childProductIdOne = getChildProductId(childProductOne);
        String childProductIdTwo = getChildProductId(childProductTwo);
        String childProductIdThree = getChildProductId(childProductThree);
        goToPimProductTreeSection();
        clkOnParentProductFolder(productFolderName);
        goToProductsRightSectionWindow();
        clkOnSelectBoxOfChildProduct(childProductIdOne);
        clkOnSelectBoxOfChildProduct(childProductIdTwo);
        clkOnSelectBoxOfChildProduct(childProductIdThree);
    }

    /**
     * This method clicks on attribute folder
     * 
     * @param productFolderName String Object containing product folder name.
     */
    private void clkOnParentProductFolder(String productFolderName) {
        try {
            CSUtility.tempMethodForThreadSleep(3000);
            getCreatedProductFolder(productFolderName).click();
        } catch (Exception e) {
            CSLogger.error("Parent folder not found", e);
            Assert.fail("Parent folder not found");
        }
    }

    /**
     * Switches till spiltAreaMain frame
     */
    private void goToProductsRightSectionWindow() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
    }

    /**
     * Returns the created product folder webElement
     * 
     * @param productFolderName String containing name of parent product folder
     * @return WebElement CreatedProductFolder
     */
    private WebElement getCreatedProductFolder(String productFolderName) {
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(By.linkText(productFolderName)),
                browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(productFolderName)));
        return browserDriver.findElement(By.linkText(productFolderName));
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param childProductName String containing name of child product
     * @return child product Id
     */
    private String getChildProductId(String childProductName) {
        goToPimProductTreeSection();
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(
                browserDriver.findElement(By.linkText(childProductName)))
                .perform();
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']")));
        return browserDriver
                .findElement(
                        By.xpath("//input[@id='CSGuiEditorDialogIDInput']"))
                .getAttribute("value");
    }

    /**
     * This method clicks on the selectbox of the child product
     * 
     * @param childProductId String object containing Id of the child product
     */
    private void clkOnSelectBoxOfChildProduct(String childProductId) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.xpath("//table[contains(@class,'CSAdminList')]")));
        WebElement selectBox = browserDriver.findElement(
                By.xpath("//input[contains(@value,'" + childProductId + "')]"));
        selectBox.click();
        CSLogger.info("Clicked on selectbox of child product of childProductId "
                + childProductId);
    }

    /**
     * verifies whether a new window open ups with the list of selected products
     */
    private void checkDistributeContentWindow() {
        try {
            goToProductsRightSectionWindow();
            switchToCurrentWindowHandle();
        } catch (NoSuchWindowException e) {
            CSLogger.error(
                    "A new window didn't opened up with the list of selected "
                            + "products after selecting distribute content option",
                    e);
            softAssertion.fail("A new window didn't opened up with the list of "
                    + "selected "
                    + "products after selecting distribute content option");
        }
    }

    /**
     * switch to current window handle.
     */
    private void switchToCurrentWindowHandle() {
        try {
            for (String windowHandle : browserDriver.getWindowHandles()) {
                browserDriver.switchTo().window(windowHandle);
            }
            CSLogger.info("Switched to new window handle");
        } catch (Exception e) {
            CSLogger.error("Error while switching to new window handle ", e);
        }
    }

    /**
     * Returns distribute content window handle
     */
    private String getDistributeContentWindowHandle() {
        String distributeContentWindowHandle = browserDriver.getWindowHandle();
        return distributeContentWindowHandle;
    }

    /**
     * Selects the drop down option of distribute content and returns current
     * window handle
     * 
     * @return windowHandle
     */
    private String selectDistributeContentOption() {
        goToProductsRightSectionWindow();
        String windowHandle = browserDriver.getWindowHandle();
        CSGuiListFooterInstance.selectOptionFromDrpDwnMassUpdateSelector(
                waitForReload, "Distribute Content");
        return windowHandle;
    }

    /**
     * Clicks on clone object button
     * 
     * @param childProductName String Object containing the child product name.
     */
    private void clkOnCloneObjectButton(String childProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + childProductName + "')]"
                                + "/parent::div/parent::div/following-sibling::"
                                + "div/div[2]/div/div[3]/img")));
        WebElement cloneObjectButton = browserDriver.findElement(By.xpath(
                "//span[contains(text(),'" + childProductName + "')]/parent"
                        + "::div/parent::div/following-sibling::div/div[2]/div/"
                        + "div[3]/img"));
        cloneObjectButton.click();
        CSLogger.info("Clicked on clone object button");
    }

    /**
     * Handles pop up window of clone object
     * 
     * @param childProductName String Object containing the child product name.
     * @param isPressOkay Boolean object containing true or false values
     */
    private void handleCloneObjectPopupWindow(String childProductName,
            Boolean isPressOkay) {
        handleConfirmWindowDialogBox(isPressOkay);
        if (isPressOkay) {
            verifyClonedObject(childProductName, isPressOkay);
        } else {
            verifyObjectNotCloned(childProductName, isPressOkay);
        }
    }

    /**
     * This method clicks on clone object button and also handles the pop up
     * window
     * 
     * @param objectToBeClonedName String object containing name of the child
     *            product to be clonned
     * @param isPressOkay Boolean object containing true or false values
     */
    private void performCloneObjectOperation(String objectToBeClonedName,
            Boolean isPressOkay) {
        clkOnCloneObjectButton(objectToBeClonedName);
        handleCloneObjectPopupWindow(objectToBeClonedName, isPressOkay);
    }

    /**
     * Verifies that when ok from 'clone object popup' is selected the clone of
     * object(child product) is created.
     * 
     * @param clonedChildProductName String Object containing the child product
     *            name.
     * @param isPressOkay Boolean object containing true or false values
     */
    private void verifyClonedObject(String clonedChildProductName,
            Boolean isPressOkay) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + clonedChildProductName + "')]")));
        int clonedChildProductNameCount = browserDriver.findElements(By.xpath(
                "//span[contains(text(),'" + clonedChildProductName + "')]"))
                .size();
        if (clonedChildProductNameCount > 1) {
            CSLogger.info("When clicked on OK of popup, child product  cloned "
                    + "successfully : teststep passed");
        } else {
            CSLogger.error("When clicked on OK of popup, child product  cloned "
                    + "successfully step has error : teststep failed");
            softAssertion
                    .fail("When clicked on OK of popup child product  cloned "
                            + "successfully cloned step has error : teststep "
                            + "failed");
        }
    }

    /**
     * Verifies that when clicked on cancel of 'clone object popup' the clone of
     * child product is not created.
     * 
     * @param clonedChildProductName String Object containing the child product
     *            name.
     * @param isPressOkay Boolean object containing true or false values
     */
    private void verifyObjectNotCloned(String clonedChildProductName,
            Boolean isPressOkay) {
        int clonedChildProduct = browserDriver.findElements(By.xpath(
                "//span[contains(text(),'" + clonedChildProductName + "')]"))
                .size();

        if (clonedChildProduct > 1) {
            CSLogger.error(
                    "When clicked on cancel of popup child product didn't cloned"
                            + "step has error : teststep failed");
            softAssertion
                    .fail("When clicked on cancel of popup child product didn't "
                            + "cloned step has error : teststep failed");
        } else {
            CSLogger.info(
                    "When clicked on cancel of popup child product didn't "
                            + "cloned : teststep passed");
        }
    }

    /**
     * Checks if distribute content window tab is opened
     */
    private void checkDistributeContentWindowExists() {
        String currentWindowHandle = browserDriver.getWindowHandle();
        if (currentWindowHandle.equals(distributeContentWindowHandle))
            CSLogger.info("Distribute content window tab exists");
        else {
            CSLogger.error("Distribute content window doesn't exists");
            Assert.fail("Distribute content window doesn't exists");
        }
    }

    /**
     * This method clicks on remove object button and handles the pop up window
     * 
     * @param isPressOkay Boolean object containing true or false values
     * @param removedChildProductName String Object containing the child product
     *            name which needs to be removed.
     */
    private void performRemoveObjectOperation(Boolean isPressOkay,
            String removedChildProductName) {
        clkOnRemoveObjectButton(removedChildProductName);
        handleRemoveObjectPopupWindow(isPressOkay, removedChildProductName);
    }

    /**
     * Clicks on remove object button
     */
    private void clkOnRemoveObjectButton(String removedChildProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),'" + removedChildProductName
                                + "')]/parent::div/parent::div/following-"
                                + "sibling::div/div[2]/div/div[2]/img")));
        WebElement removeObjectButton = browserDriver.findElement(By.xpath(
                "//span[contains(text(),'" + removedChildProductName + "')]"
                        + "/parent::div/parent::div/following-sibling::div/div"
                        + "[2]/div/div[2]/img"));
        removeObjectButton.click();
        CSLogger.info("Clicked on remove object button");
    }

    /**
     * Handles the pop up window of remove object
     * 
     * @param isPressOkay Boolean object containing true or false values
     * @param removedChildProductName String Object containing the child product
     *            name.
     */
    private void handleRemoveObjectPopupWindow(Boolean isPressOkay,
            String removedChildProductName) {
        handleConfirmWindowDialogBox(isPressOkay);
        if (isPressOkay) {
            verifyRemovalOfObject(removedChildProductName);
        } else {
            verifyNonRemovalOfObject(removedChildProductName);
        }
    }

    /**
     * Handles Ok and Cancel operations of pop up window
     * 
     * @param isPressOkay Boolean object containing true or false values
     */
    private void handleConfirmWindowDialogBox(Boolean isPressOkay) {
        if (isPressOkay) {
            CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                    .findElement(By.xpath("//span[contains(text(),'OK')]")));
            browserDriver.findElement(By.xpath("//span[contains(text(),'OK')]"))
                    .click();
            CSLogger.info("Clicked on Ok button of popup");
        } else {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(
                            By.xpath("//span[contains(text(),'Cancel')]")));
            browserDriver
                    .findElement(
                            By.xpath("//span[contains(text(),'Cancel')" + "]"))
                    .click();
            CSLogger.info("Clicked on Cancel button of popup");
        }
    }

    /**
     * Verifies that when ok from pop up window is selected the object gets
     * removed
     * 
     * @param removedChildProductName String Object containing the child product
     *            name.
     */
    private void verifyRemovalOfObject(String removedChildProductName) {
        int removedChildProductExists = browserDriver.findElements(By.xpath(
                "//span[contains(text(),'" + removedChildProductName + "')]"))
                .size();
        if (removedChildProductExists != 0) {
            CSLogger.error("Removal of child product failed : teststep failed");
            softAssertion
                    .fail("Removal of child product failed teststep failed");
        } else {
            CSLogger.info("Object removed successfully : teststep passed");
        }
    }

    /**
     * Verifies that when cancel is selected from pop up window it doesn't
     * remove object.
     * 
     * @param removedChildProductName String Object containing the child product
     *            name.
     */
    private void verifyNonRemovalOfObject(String removedChildProductName) {
        int removedChildProductExists = browserDriver.findElements(By.xpath(
                "//span[contains(text(),'" + removedChildProductName + "')]"))
                .size();
        if (removedChildProductExists != 0) {
            CSLogger.info("Object is not removed when clicked on cancel : "
                    + "teststep passed");
        } else {
            CSLogger.error("Object is not removed when clicked on cancel step "
                    + "has error: teststep failed");
            softAssertion.fail("Object is not removed when clicked on cancel "
                    + "step has error: teststep failed");
        }
    }

    /**
     * This methods performs operations to add objects
     * 
     * @param parentProductName String Object containing the parent product
     *            name.
     * @param childProductName String Object containing the child product name.
     * @param isPressOkay Boolean object containing true or false values
     */
    private void performAddObjectOperation(String parentProductName,
            String childProductName, Boolean isPressOkay) {
        switchToDistributeContentWindowTab();
        clkOnAddObjectButton();
        addObjectFromDataSelectionDialogWindow(parentProductName,
                childProductName, isPressOkay);
        verifyAddedObject(isPressOkay, childProductName);
    }

    /**
     * Clicks on add object button
     */
    private void clkOnAddObjectButton() {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//div[@data='add']")));
        WebElement addObjectButton = browserDriver
                .findElement(By.xpath("//div[@data='add']"));
        addObjectButton.click();
        CSLogger.info("Clicked on 'Add Object' button");
    }

    /**
     * Selects an object from data selection dialog window
     * 
     * @param parentProductName String Object containing the parent product
     *            name.
     * @param childProductName String Object containing the child product name.
     * @param isPressOkay Boolean object containing true or false values
     */
    private void addObjectFromDataSelectionDialogWindow(
            String parentProductName, String childProductName,
            Boolean isPressOkay) {
        CSLogger.info("Opened product folder");
        selectionDialogWindowInstance.clkBtnControlPaneButtonProductsFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//span[contains(text(),'"
                        + parentProductName + "')]")));
        WebElement parentProduct = browserDriver.findElement(By
                .xpath("//span[contains(text(),'" + parentProductName + "')]"));
        parentProduct.click();
        CSLogger.info(
                "Clicked on product folder from data selection dialog window");
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(childProductName)));
        WebElement childProductToBeAdded = browserDriver
                .findElement(By.linkText(childProductName));
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(childProductToBeAdded).perform();
        productPopup.askBoxWindowOperation(waitForReload, isPressOkay,
                browserDriver);
    }

    /**
     * Verifies that an object is added when clicked on ok button and an object
     * is not added when cancel button is clicked
     * 
     * @param isPressOkay Boolean object containing true or false values
     * @param addedObjectName String object containing child product name which
     *            is added
     */
    private void verifyAddedObject(Boolean isPressOkay,
            String addedObjectName) {
        if (isPressOkay) {
            int addedObjectExists = browserDriver.findElements(By.xpath(
                    "//span[contains(text(),'" + addedObjectName + "')]"))
                    .size();
            if (addedObjectExists != 0) {
                CSLogger.info("When clicked on Ok object added successfully : "
                        + "teststep passsed");
            } else {
                CSLogger.error(
                        "When clicked on Ok adding object failed : teststep "
                                + "failed");
                softAssertion.fail("When clicked on Ok adding object failed : "
                        + "teststep failed");
            }
        } else {
            int addedObjectExists = browserDriver.findElements(By.xpath(
                    "//span[contains(text(),'" + addedObjectName + "')]"))
                    .size();
            if (addedObjectExists != 0) {
                CSLogger.error("When clicked on Cancel object(child product)  "
                        + "should not be added teststep has error "
                        + ":teststep failed");
                softAssertion
                        .fail("When clicked on Cancel object  should not be "
                                + "added teststep has error: teststep failed");
            } else {
                CSLogger.info(
                        "When clicked on cancel object(child product) is not "
                                + "added to distribute content window: teststep "
                                + "passed");
            }
        }
    }

    /**
     * This method closes the distribute content window tab and switches to main
     * window tab
     */
    private void closeDistributeContentWindowTab() {
        browserDriver.close();
        browserDriver.switchTo().window(mainWindowHandle);
        CSLogger.info(
                "Closed distribute content window tab and switched to main "
                        + "window");
    }

    /**
     * When object is added this method verifies that "If the new product has a
     * different class maybe not all attributes are visible (only that one was
     * already there before) or added product has not the existing ones so this
     * area is hatched".
     */
    private void verifyNonEditableAttributes() {
        CSUtility.tempMethodForThreadSleep(3000);
        CSUtility.scrollUpOrDownToElement(
                browserDriver.findElement(
                        By.xpath("//td[contains(@class,'diffField')]")),
                browserDriver);
        browserDriver
                .findElement(By.xpath("//td[contains(@class,'diffField')]"));
        String attributeStyle = browserDriver
                .findElement(By.xpath("//td[contains(@class,'diffField')]"))
                .getCssValue("background-image");
        if (attributeStyle.contains("different_attribute")) {
            CSLogger.info("Hatched attributes verified : teststep passed");
        } else {
            CSLogger.error("Hatched attributes verified : teststep failed");
            softAssertion.fail("Hatched attributes verified : teststep failed");
        }
    }

    /**
     * performs operation of double clicking on parent product
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     */
    private void doubleClkOnParentProduct(String parentProductName) {
        try {
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement parentProduct = getCreatedProductFolder(
                    parentProductName);
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(By.linkText(parentProductName)));
            Actions action = new Actions(browserDriver);
            action.doubleClick(parentProduct).build().perform();
        } catch (Exception e) {
            CSLogger.error("Parent product folder not found", e);
            Assert.fail("Parent product folder not found");
        }
    }

    /**
     * This method double clicks on child product
     * 
     * @param childProductName String object containing name of the child
     *            product
     */
    private void doubleClkOnChildProduct(String childProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(childProductName)));
        WebElement childProduct = browserDriver
                .findElement(By.linkText(childProductName));
        Actions action = new Actions(browserDriver);
        action.doubleClick(childProduct).build().perform();
    }

    /**
     * Assigns the given class to the parent product folder
     * 
     * @param parentProductName String Object containing the product name under
     *            which child product will be created.
     * @param className String object containing name of the class
     */
    private void assignClassToParentProduct(String parentProductName,
            String className) {
        doubleClkOnParentProduct(parentProductName);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        selectClassFromDataSelectionDialogWindow(className);
        CSLogger.info("Successfully assigned class to parent product");
    }

    /**
     * selects the class of given classname that will be assigned to the parent
     * product folder
     * 
     * @param className String object containing name of the class
     */
    private void selectClassFromDataSelectionDialogWindow(String className) {
        try {
            selectionDialogWindowInstance.clkBtnControlPaneButtonUserFolder(
                    waitForReload, browserDriver);
            TraverseSelectionDialogFrames
                    .traverseToDataSelectionDialogLeftSection(waitForReload,
                            browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    browserDriver.findElement(By.linkText(className)));
            Actions action = new Actions(browserDriver);
            action.doubleClick(
                    browserDriver.findElement(By.linkText(className))).build()
                    .perform();
            productPopup.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            goToProductsRightSectionWindow();
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            CSLogger.info("Class : " + className + "assigned to product "
                    + "successfully");
        } catch (Exception e) {
            CSLogger.error("Class not found", e);
            Assert.fail("Class not found");
        }
    }

    /**
     * Assigns the given class to child product
     * 
     * @param childProductName String object containing name of the child
     *            product
     * @param className String object containing name of the class
     */
    private void assignClassToChildProduct(String childProductName,
            String className) {
        doubleClkOnChildProduct(childProductName);
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        csGuiDialogContentIdInstance
                .clkPdmArticleClassMappingCsReference(waitForReload);
        selectClassFromDataSelectionDialogWindow(className);
        CSLogger.info("Successfully assigned class to child product");
    }

    /**
     * Switches from distribute window tab to main window tab
     */
    private void switchToMainWindowTab() {
        try {
            browserDriver.switchTo().window(mainWindowHandle);
        } catch (Exception e) {
            CSLogger.error("Unable to switch to main window");
            Assert.fail("Unable to switch to main window");
        }
    }

    /**
     * Switches from main window tab tab to distribute window
     */
    private void switchToDistributeContentWindowTab() {
        try {
            browserDriver.switchTo().window(distributeContentWindowHandle);
        } catch (Exception e) {
            CSLogger.error("Unable to switch to distribute Content window");
            Assert.fail("Unable to switch to distribute Content window");
        }
    }

    /**
     * This is a data provider method contains array of parent product folder
     * and four child product names.
     * 
     * @return Array
     */
    @DataProvider(name = "distributeProductContentObjectTestData")
    public Object[][] getDistributeProductContentObjectData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"), dataSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioProductsNodeInstance = new PimStudioProductsNodePage(
                browserDriver);
        csPopupDiv = new CSPopupDivPim(browserDriver);
        softAssertion = new SoftAssert();
        CSGuiListFooterInstance = new CSGuiListFooter(browserDriver);
        softAssertion = new SoftAssert();
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
    }

}
