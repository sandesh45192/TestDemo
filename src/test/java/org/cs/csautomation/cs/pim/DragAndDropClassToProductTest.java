/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IClassPopup;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to drag and drop class to the newly
 * created product.
 * 
 * @author CSAutomation Team
 *
 */
public class DragAndDropClassToProductTest extends AbstractTest {

    private FrameLocators             locator;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private PimStudioSettingsNode     pimStudioSettingsNode;
    private WebDriverWait             waitForReload;
    private String                    productFolderSheetName = "DragDropClassToProduct";
    private IProductPopup             productPopUp;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontal;
    private IClassPopup               classPopUp;
    private CSPortalHeader            csPortalHeader;

    /**
     * This test method which drives the usecase of drag drop of newly created
     * class to product , verifying if class is successfully dragged and dropped
     * to the newly created product folder , adding subclasses to the product
     * folder via plus button and verifying the same
     * 
     * @param nameOfProuduct contains the name of the product
     * @param nameOfclass contains the name of the class
     */
    @Test(priority = 1, dataProvider = "createProductData1")
    public void testDragAndDrop(String nameOfProduct, String nameOfClass,
            String childProductName, String allowedSubClassesToAdd) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            csPortalHeader.clkBtnProducts(waitForReload);
            dragAndDropClassToProduct(nameOfProduct, nameOfClass,
                    childProductName, allowedSubClassesToAdd, childProductName);
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method : testDragAndDrop",
                    e);
            Assert.fail("Automation error in test method : testDragAndDrop", e);
        }
    }

    /**
     * This test method which performs the drag class from classes on products.
     * It will assign the classes to products using the drag and drop
     * 
     * @param nameOfProduct contains the name of the product in the string
     *            format
     * @param nameOfClass contains the name of the product in the string format
     */
    private void dragAndDropClassToProduct(String nameOfProduct,
            String nameOfClass, String nameOfChildProduct,
            String allowedSubClassesToAdd, String childProductName) {
        traverseToProductsNode();
        WebElement createdProductFolder = browserDriver
                .findElement(By.linkText(nameOfProduct));
        clickProducts(createdProductFolder);
        CSUtility.tempMethodForThreadSleep(1000);
        traverseToSettings();
        traverseToClassToDragAndDrop(waitForReload, createdProductFolder,
                nameOfProduct, nameOfClass);
        performClick(waitForReload, "Extend");
        verifyAddedClassToProduct(waitForReload, createdProductFolder,
                nameOfProduct, nameOfClass);
    }

    /**
     * This method traverses to product node
     */
    private void traverseToProductsNode() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();

    }

    /**
     * This method clicks on created product node
     * 
     * @param createdProductFolder
     */
    private void clickProducts(WebElement createdProductFolder) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                createdProductFolder);
        createdProductFolder.click();
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
     * This method will switch to the frame of 'left' menu, in 'data selection
     * dialog' window.
     */
    public void switchToIdRecordsFrameLeft() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialog()));
        CSLogger.info("data Selection Dialog");
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialogNestedOne()));
        CSLogger.info("Data selection dialog nested");
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmIdRecordsPdmarticleconfigurationLeft()));
        CSLogger.info("After record left..");
        CSLogger.info(
                "Switched to the frame for 'data selection dialog' window");
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
            Actions actions = new Actions(browserDriver);
            actions.dragAndDrop(className, createdProductFolder).perform();
        } catch (Exception e) {
            CSLogger.error("Dragged and Dropped class to Product::" + e);
        }
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
            CSLogger.info("Class has assigned to the product");
        } else {
            CSLogger.error("Failed to assign class to product");
            Assert.fail("Verification for assigned classes failed");
        }
    }

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
     * this method verifies if class is present in the inherited section of the
     * newly created product
     * 
     * @param waitForReload contains time in ms to wait on any element if needed
     * @param createdProductFolder contains the name of the already created
     *            product folder as webElement
     * @param nameOfProduct contains the name of the already created product in
     *            String format
     * @param nameOfClass contains the name of the class
     */

    @Test(priority = 2, dataProvider = "createProductData1")
    private void verifyInheritedSectionOfProduct(String nameOfProduct,
            String nameOfClass, String childProductName,
            String allowedSubClassesToAdd) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioProductsNode.getBtnPimProductsNode()));
            pimStudioProductsNode.getBtnPimProductsNode().click();
            WebElement createdProductFolder = browserDriver
                    .findElement(By.linkText(nameOfProduct));
            createdProductFolder.click();
            WebElement childProduct = browserDriver
                    .findElement(By.linkText(childProductName));
            childProduct.click();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(csGuiDialogContentId.getTabProperties()));
            csGuiDialogContentId.getTabProperties().click();
            CSUtility.tempMethodForThreadSleep(1000);
            List<WebElement> list = getListOfClasses();
            boolean status = getStatus(nameOfClass, list);
            if (status == true) {
                CSLogger.info(
                        "Class is present in the inherited section of product");
            } else {
                CSLogger.error(
                        "Class is not present in the inherited section of product");
                Assert.fail(
                        "Class is not present in the inherited section of product");
            }
        } catch (Exception e) {
            CSLogger.debug("Verification Of Inherted Section Is Unsuccessful.",
                    e);
            Assert.fail(
                    "Class is not present in the inherited section of the product.");
        }
    }

    /**
     * This method verifies if the class has successfully removed from the
     * product or not
     *
     * @param waitForReload
     * @param createdProductFolder contains the name of the product folder
     * @param nameOfProduct contains the name of the product
     * @param nameOfClass contains the name of the class
     */
    @Test(priority = 3, dataProvider = "createProductData1")
    private void verifyDeletedSectionOfClasses(String nameOfProduct,
            String nameOfClass, String childProductName,
            String allowedSubClassesToAdd) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            WebElement createdProductFolder = browserDriver
                    .findElement(By.linkText(nameOfProduct));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    createdProductFolder);
            removeClassFromInheritedSection(waitForReload, createdProductFolder,
                    nameOfProduct, nameOfClass, childProductName);
            CSUtility.switchToDefaultFrame(browserDriver);
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            csGuiDialogContentId.getTabProperties().click();
            CSUtility.tempMethodForThreadSleep(1000);
            List<WebElement> list = getListOfClasses();
            boolean status = getStatus(nameOfClass, list);
            if (status == true) {
                CSLogger.info(
                        "Class is present in the deleted section of product");
            } else {
                CSLogger.error(
                        "Class is not present in the deleted section of product");
                Assert.fail(
                        "Class is not present in the deleted section of product");
            }
        } catch (Exception e) {
            CSLogger.debug("Class is not present in the deleted section..", e);
            Assert.fail("Class is not present", e);
        }
    }

    /**
     * This method removes the assigned class to the product from the inherited
     * section of the product
     *
     * @param waitForReload
     * @param createdProductFolder contains the name of the product folder
     * @param nameOfProduct contains the name of the product
     * @param nameOfClass contains the name of the class
     */
    private void removeClassFromInheritedSection(WebDriverWait waitForReload,
            WebElement createdProductFolder, String nameOfProduct,
            String nameOfClass, String childProductName) {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioProductsNode.getBtnPimProductsNode()));
            pimStudioProductsNode.getBtnPimProductsNode().click();
            createdProductFolder = browserDriver
                    .findElement(By.linkText(nameOfProduct));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    createdProductFolder);
            createdProductFolder.click();
            WebElement childProduct = browserDriver
                    .findElement(By.linkText(childProductName));
            CSUtility.waitForVisibilityOfElement(waitForReload, childProduct);
            childProduct.click();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            csGuiDialogContentId.getTabProperties().click();
            CSUtility.tempMethodForThreadSleep(4000);
            WebElement className = browserDriver.findElement(
                    By.xpath("//table[@class='CSGuiTable']//td[2]/span"));
            CSUtility.waitForVisibilityOfElement(waitForReload, className);
            className.click();
            browserDriver.switchTo().defaultContent();
            waitForReload.until(ExpectedConditions
                    .visibilityOf(pimStudioProductsNode.getProductPopUpDiv()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiDialogContentId.getClkRemove());
            waitForReload.until(ExpectedConditions
                    .elementToBeClickable(csGuiDialogContentId.getClkRemove()));
            CSUtility.tempMethodForThreadSleep(3000);
            csGuiDialogContentId.getClkRemove().click();
            CSUtility.tempMethodForThreadSleep(3000);
            CSLogger.info("Class removed successfully");
        } catch (Exception e) {
            CSLogger.error("Could not click on remove class.." + e);
            Assert.fail("Could not remove class from inherited section");
        }

    }

    /**
     * this method add class in the inherited section from deleted section via
     * add again option
     *
     * @param waitForReload
     * @param createdProductFolder
     * @param nameOfChildProduct
     */
    @Test(priority = 4, dataProvider = "createProductData1")
    private void verifyAddAgainOptionFromDeletedSection(String nameOfProduct,
            String nameOfClass, String childProductName,
            String allowedSubClassesToAdd) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiDialogContentId.getTabProperties());
            csGuiDialogContentId.getTabProperties().click();
            WebElement element = browserDriver
                    .findElement(By.xpath("//div[contains(text(),'Deleted')]"));
            waitForReload.until(ExpectedConditions.visibilityOf(element));
            // element.click();

            CSUtility.tempMethodForThreadSleep(5000);
            WebElement className = browserDriver.findElement(By.xpath(
                    "(//table[@class='CSGuiTable']/tbody/tr[2]/td[2]/span)"));
            className.click();
            CSUtility.tempMethodForThreadSleep(3000);
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(pimStudioProductsNode.getProductPopUpDiv()));
            CSLogger.info("Wait completed for ProductPopUp Div ");
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPopupDivFrame()));
            CSLogger.info("Wait completed for FrmCsPopupDivFrame");
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    csGuiDialogContentId.getClkAddAgain());
            waitForReload.until(ExpectedConditions.elementToBeClickable(
                    csGuiDialogContentId.getClkAddAgain()));
            CSLogger.info("Wait completed for ClkAddAgain ");
            CSUtility.tempMethodForThreadSleep(3000);
            csGuiDialogContentId.getClkAddAgain().click();
            CSUtility.tempMethodForThreadSleep(3000);
            CSLogger.info("Class has been added again via Add Again option..");
        } catch (Exception e) {
            CSLogger.debug(
                    "Assigned class could not added again via AddAgain option..",
                    e);
            Assert.fail("Verification of Add Again option failed", e);
        }
    }

    /**
     * this method adds the classes via allowed subclasses button present in the
     * mid pane of the application
     *
     * @param waitForReload waits internally to load given element
     * @param createdProductFolder contains name of already created product
     *            folder
     * @param nameOfClass contains the name of the class
     */
    @Test(priority = 5, dataProvider = "createProductData1")
    private void addClassesByAllowedSubClasses(String nameOfProduct,
            String nameOfClass, String childProductName,
            String allowedSubClassesToAdd) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            performUsecase(waitForReload, allowedSubClassesToAdd, true);
            traverseToClassesFolder();
            WebElement className = browserDriver
                    .findElement(By.linkText(nameOfClass));
            className.click();
            addSubclasses(nameOfClass, allowedSubClassesToAdd);
            /* These are mandatory sleep methods */
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            WebElement createdProductFolder = browserDriver
                    .findElement(By.linkText(nameOfProduct));
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            csGuiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
            dragAndDropNewClassToProduct(nameOfClass, createdProductFolder,
                    nameOfProduct);
            addMoreClassesToProduct(nameOfClass, createdProductFolder,
                    nameOfProduct, allowedSubClassesToAdd);
            verifyAddedClassToProduct(nameOfClass, createdProductFolder,
                    nameOfProduct, allowedSubClassesToAdd);
            checkOutCreatedChildAndVerifyAssignedClasses(nameOfClass,
                    createdProductFolder, nameOfProduct, allowedSubClassesToAdd,
                    childProductName);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : addClassesByAllowedSubClasses",
                    e);
            Assert.fail(
                    "Automation error in test method : addClassesByAllowedSubClasses",
                    e);
        }
    }

    /**
     * this method performs operation of creation of class
     *
     * @param waitForReload waits for an element to reload
     * @param nameOfClass contains the name of the class
     * @param pressOkay contains the boolean value to choose option
     */
    public void performUsecase(WebDriverWait waitForReload, String nameOfClass,
            Boolean pressOkay) {
        IClassPopup classPopup = CSPopupDivPim
                .getCSPopupDivLocators(browserDriver);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioSettingsNode.getBtnPimClassesNode(), browserDriver);
        classPopup.selectPopupDivMenu(waitForReload,
                classPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        classPopup.enterValueInDialogue(waitForReload, nameOfClass);
        classPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
    }

    /**
     * This method adds subclasses in already existing class
     *
     * @param nameOfClass contains the name of the class
     * @param allowedSubClassesToAdd contains allowed subclasses to add to the
     *            existing class
     */
    private void addSubclasses(String nameOfClass,
            String allowedSubClassesToAdd) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);

        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getBtnAllowedSublasses());
        csGuiDialogContentId.getBtnAllowedSublasses().click();
        switchToIdRecordsFrameLeft();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentId.getDropDownClassesFolder());
        csGuiDialogContentId.getDropDownClassesFolder().click();
        CSUtility.tempMethodForThreadSleep(5000);
        WebElement classNameToBeAdded = browserDriver
                .findElement(By.linkText(nameOfClass));
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(classNameToBeAdded).perform();
        WebElement subclass = browserDriver
                .findElement(By.linkText(allowedSubClassesToAdd));
        actions.doubleClick(subclass).perform();
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * @param nameOfClass
     * @param createdProductFolder
     * @param nameOfProduct
     * @param allowedSubclassesToAdd
     */
    private void checkOutCreatedChildAndVerifyAssignedClasses(
            String nameOfClass, WebElement createdProductFolder,
            String nameOfProduct, String allowedSubclassesToAdd,
            String childProductName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("Clicked on Products Folder...");
        createdProductFolder = browserDriver
                .findElement(By.linkText(nameOfProduct));
        createdProductFolder.click();
        WebElement createdChildProduct = browserDriver
                .findElement(By.linkText(childProductName));
        createdChildProduct.click();
    }

    /**
     * This method verifies if the class has been successfully added to the
     * product by plus button present in the mid pane of the application
     *
     * @param nameOfClass contains the name of the class
     * @param createdProductFolder contains the name of the already created
     *            folder
     * @param nameOfProduct contains the name of the product
     * @param allowedSubclassesToAdd contains name of allowed subclasses added
     *            under the class folder
     */
    private void verifyAddedClassToProduct(String nameOfClass,
            WebElement createdProductFolder, String nameOfProduct,
            String allowedSubclassesToAdd) {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioProductsNode.getBtnPimProductsNode()));
            pimStudioProductsNode.getBtnPimProductsNode().click();
            createdProductFolder = browserDriver
                    .findElement(By.linkText(nameOfProduct));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(createdProductFolder).perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            csGuiDialogContentId.clkBtnProperties(waitForReload);
            CSUtility.tempMethodForThreadSleep(5000);
            List<WebElement> classXPaths = browserDriver.findElements(
                    By.xpath("//table[@class='CSGuiTable']/tbody/tr/td[2]"));
            checkPresenceOfClass(classXPaths, nameOfClass);
            checkPresenceOfClass(classXPaths, allowedSubclassesToAdd);
        } catch (Exception e) {
            CSLogger.debug("Classes are not present");
            Assert.fail("Verification failed.");
        }
    }

    private void checkPresenceOfClass(List<WebElement> classXPaths,
            String nameOfClass) {
        boolean status = false;

        for (int i = 0; i <= classXPaths.size(); i++) {
            if (classXPaths.get(i).getText().equals("Distribution Channel")) {
                classXPaths.get(i).getText().replace("Distribution Channel",
                        "");
            }
            String className = classXPaths.get(i).getText();
            if (className.equals(nameOfClass)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            CSLogger.info(nameOfClass + " is present");
        } else {
            CSLogger.error(nameOfClass + " is not present");
            Assert.fail("Class is not present");
        }
    }

    /**
     * this method adds more classes to the product by adding plus button on the
     * right section pane
     *
     * @param nameOfClass contains the name of the class in the string format
     * @param createdProductFolder contains the name of the already created
     *            product folder in the form of webelement form
     * @param nameOfProduct contains the name of the product
     * @param allowedSubclassesToAdd contains the name of the subclass to be
     *            added under the allowed subclasses in the already existing
     *            class
     */
    private void addMoreClassesToProduct(String nameOfClass,
            WebElement createdProductFolder, String nameOfProduct,
            String allowedSubclassesToAdd) {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioProductsNode.getBtnPimProductsNode()));
            pimStudioProductsNode.getBtnPimProductsNode().click();
            createdProductFolder = browserDriver
                    .findElement(By.linkText(nameOfProduct));
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(createdProductFolder).perform();
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            csGuiDialogContentId.clkBtnProperties(waitForReload);
            csGuiDialogContentId.getAddClassToProductByPlus().click();
            switchToIdRecordsFrameLeft();
            WebElement classNameToBeAdded = browserDriver
                    .findElement(By.linkText(nameOfClass));
            actions.doubleClick(classNameToBeAdded).perform();
            WebElement subclass = browserDriver
                    .findElement(By.linkText(allowedSubclassesToAdd));
            actions.doubleClick(subclass).perform();
            CSUtility.tempMethodForThreadSleep(5000);
            productPopUp.askBoxWindowOperation(waitForReload, true,
                    browserDriver);
            CSLogger.info("Classes added");
        } catch (Exception e) {
            CSLogger.error(
                    "Could not add more classes via add allowed subclasses folder");
            Assert.fail("Couldnt add more classes.");
        }
    }

    /**
     * this method drags and drops the class in which we added allowed subclass
     * to the newly created product folder
     *
     * @param nameOfClass contains the name of the class
     * @param createdProductFolder contains the product folder name onto which
     *            we have to drag and drop class to
     */
    private void dragAndDropNewClassToProduct(String nameOfClass,
            WebElement createdProductFolder, String nameOfProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        createdProductFolder = browserDriver
                .findElement(By.linkText(nameOfProduct));
        createdProductFolder.click();
        traverseToClassToDragAndDrop(waitForReload, createdProductFolder,
                nameOfProduct, nameOfClass);
        performClick(waitForReload, "Extend");
    }

    /**
     * This method traverses and clicks on the class in the left section pim
     * studio
     *
     */
    private void traverseToClassesFolder() {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioSettingsNode.getBtnPimSettingsNode()));
            pimStudioSettingsNode.getBtnPimSettingsNode().click();
            CSLogger.info("Clicked on Settings Node");
            waitForReload.until(ExpectedConditions.visibilityOf(
                    pimStudioSettingsNode.getBtnPimClassesNode()));
            pimStudioSettingsNode.getBtnPimClassesNode().click();
            CSLogger.info("Clicked on Class Node");
        } catch (Exception e) {
            CSLogger.error("Could not traversed upto classes folder");
        }
    }

    /**
     * This data provider returns sheet data to the test class contains name of
     * product and name of class
     *
     * @return array of Product name and Class name
     */

    @DataProvider(name = "createProductData1")
    public Object[][] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                productFolderSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        pimStudioSettingsNode = new PimStudioSettingsNode(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        classPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
    }
}
