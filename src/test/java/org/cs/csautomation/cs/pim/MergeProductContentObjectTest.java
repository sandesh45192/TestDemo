/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import org.cs.csautomation.cs.pom.CSGuiListFooter;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to create a parent product folder and
 * four child products during runtime and performs merge content operation on
 * child products i.e clone an object,remove an object and add an object.
 * 
 * 
 * @author CSAutomation Team
 *
 */
public class MergeProductContentObjectTest extends AbstractTest {

    public WebDriverWait              waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private CSPopupDivPim             csPopupDiv;
    private SoftAssert                softAssertion;
    private CSGuiListFooter           CSGuiListFooterInstance;
    private static String             mainWindowHandle;
    private static String             mergeContentWindowHandle;
    private SelectionDialogWindow     selectionDialogWindowInstance;
    private IProductPopup             productPopup;

    private String                    mergeProductContentObjectSheetName = "MergeProductContentObject";

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
    @Test(priority = 1, dataProvider = "mergeProductContentObjectTestData")
    public void testMergeContentCloneObject(String parentProductName,
            String firstChildProductName, String secondChildProductName,
            String thirdChildProductName, String fourthChildProductName) {
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
            selectChildProductsToMergeContent(parentProductName,
                    firstChildProductName, secondChildProductName,
                    thirdChildProductName);
            mainWindowHandle = performMergeContentOperation();
            checkMergeContentWindow();
            mergeContentWindowHandle = getMergeContentWindowHandle();
            performCloneObjectOperation(thirdChildProductName, false);
            performCloneObjectOperation(thirdChildProductName, true);
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testMergeContentCloneObject",
                    e);
            Assert.fail("Automation error in : testMergeContentCloneObject", e);
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
    @Test(priority = 2, dataProvider = "mergeProductContentObjectTestData")
    public void testMergeContentRemoveObject(String parentProductName,
            String firstChildProductName, String secondChildProductName,
            String thirdChildProductName, String fourthChildProductName) {
        try {
            checkMergeContentWindowExists();
            performRemoveObjectOperation(false, secondChildProductName);
            performRemoveObjectOperation(true, secondChildProductName);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testMergeContentRemoveObject",
                    e);
            Assert.fail("Automation error in : testMergeContentRemoveObject",
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
    @Test(priority = 3, dataProvider = "mergeProductContentObjectTestData")
    public void testMergeContentAddObject(String parentProductName,
            String firstChildProductName, String secondChildProductName,
            String thirdChildProductName, String fourthChildProductName) {
        try {
            checkMergeContentWindowExists();
            performAddObjectOperation(parentProductName, fourthChildProductName,
                    false);
            performAddObjectOperation(parentProductName, fourthChildProductName,
                    true);
            closeMergeContentWindowTab();
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : testMergeContentAddObject ",
                    e);
            Assert.fail("Automation error in : testMergeContentAddObject ", e);
        }
    }

    /**
     * This method traverse to the PIM module from Home module of application
     * and then expands the products tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToPimAndExpandProductTree(WebDriverWait waitForReload) {
        CSUtility.switchToDefaultFrame(browserDriver);
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNodeInstance.clickOnNodeProducts();
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
     * This method selects the merge content option from the marked drop down
     * and also returns the current window handle
     * 
     * @return windowHandle
     */
    private String performMergeContentOperation() {
        String windowHandle = selectMergeContentOption();
        Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
        alert.accept();
        CSLogger.info("clicked on ok of pop up for merge content operation");
        return windowHandle;
    }

    /**
     * This method selects the child product so that merger content can be
     * performed
     * 
     * @param productFolderName
     * @param childProductOne String Object containing the child product name.
     * @param childProductTwo String Object containing the child product name.
     * @param childProductThree String Object containing the child product name.
     */
    private void selectChildProductsToMergeContent(String productFolderName,
            String childProductOne, String childProductTwo,
            String childProductThree) {
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
            CSLogger.info("Clicked on parent product folder");
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
    }

    /**
     * verifies whether a new window open ups with the list of selected products
     */
    private void checkMergeContentWindow() {
        try {
            goToProductsRightSectionWindow();
            switchToCurrentWindowHandle();
        } catch (NoSuchWindowException e) {
            CSLogger.error(
                    "A new window didn't opened up with the list of selected "
                            + "products after selecting merge content option",
                    e);
            softAssertion.fail("A new window didn't opened up with the list of "
                    + "selected "
                    + "products after selecting merge content option");
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
     * Returns merge content window handle
     */
    private String getMergeContentWindowHandle() {
        String mergeContentWindowHandle = browserDriver.getWindowHandle();
        return mergeContentWindowHandle;
    }

    /**
     * Selects the drop down option of merge content and returns current window
     * handle
     * 
     * @return windowHandle
     */
    private String selectMergeContentOption() {
        goToProductsRightSectionWindow();
        String windowHandle = browserDriver.getWindowHandle();
        CSGuiListFooterInstance.selectOptionFromDrpDwnMassUpdateSelector(
                waitForReload, "Merge Content");
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
     * Checks if merge content window tab is opened
     */
    private void checkMergeContentWindowExists() {
        String currentWindowHandle = browserDriver.getWindowHandle();
        if (currentWindowHandle.equals(mergeContentWindowHandle))
            CSLogger.info("Merge content window tab exists");
        else {
            CSLogger.error("Merge content window doesn't exists");
            Assert.fail("Merge content window doesn't exists");
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
            CSLogger.error("Removal of child product failed");
            softAssertion.fail("Removal of child product failed");
        } else {
            CSLogger.info("Object removed successfully");
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
        CSLogger.info("Clicked on product folder");
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
                                + "added to merge content window: teststep passed");
            }
        }
    }

    /**
     * This method closes the merge content window tab and switches to main
     * window
     */
    private void closeMergeContentWindowTab() {
        browserDriver.close();
        browserDriver.switchTo().window(mainWindowHandle);
        CSLogger.info(
                "Closed merge content window tab and switched to main window");
    }

    /**
     * This is a data provider method contains array of parent product folder
     * and four child product names.
     * 
     * @return Array
     */
    @DataProvider(name = "mergeProductContentObjectTestData")
    public Object[][] getmergeProductContentObjectData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                mergeProductContentObjectSheetName);
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
    }

}
