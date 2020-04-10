/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.cs.csautomation.cs.pim;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimProductSplitareaContentRightPage;
import org.cs.csautomation.cs.pom.PimProductTabsPage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.pom.TabbedPaneNorth;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
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
 * This class contains the test methods to verify derivation control works for
 * products for this test to run a parent product folder and a child to that
 * product folder is created at runtime and also a duplicated product is created
 * with recursive option
 * 
 * @author CSAutomation Team
 *
 */
public class ControlDerivationsTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private CSPortalHeader            csPortalHeaderInstance;
    private TabbedPaneNorth           tabbedPaneNorthInstance;
    private PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private IProductPopup             productPopup;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private PimProductTabsPage        pimProductTabsPageInstance;
    private String                    productDerivationSheetName = "ControlProductDerivation";

    /**
     * This method drives the usecase
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     * @param childProductName
     *            String containing name of the child product
     * @param productAuthorName
     *            String containing name author of the product
     * @param productWorkflowState
     *            String specifying the workflow state value
     */
    @Test(dataProvider = "ProductDerivationTestData")
    public void testControlProductDerivation(String parentProductFolderName,
            String childProductName, String productAuthorName,
            String productWorkflowState) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 80);
            String duplicatedProductName = "Copy of " + parentProductFolderName;
            createParentProductFolder(parentProductFolderName);
            createChildProduct(parentProductFolderName, childProductName, true);
            selectDuplicateOptionOfParentProduct(parentProductFolderName,
                    "Recursive", childProductName, duplicatedProductName);
            String parentProductId = getProductId(parentProductFolderName);
            String duplicatedProductId = getProductId(duplicatedProductName);
            goToDerivationSection(parentProductFolderName,
                    duplicatedProductName);
            verifyTreeViewOfParentChildProduct(parentProductFolderName,
                    duplicatedProductName);
            checkDerivationOfDuplicatedProduct(duplicatedProductName,
                    duplicatedProductId, productAuthorName,
                    productWorkflowState);
            checkDerivationOfParentProduct(parentProductFolderName,
                    parentProductId, productAuthorName, productWorkflowState);
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation error in test method : testControlProductDerivation",
                    e);
            Assert.fail(
                    "Automation error in testControlProductDerivation testControlProductDerivation",
                    e);
        }
    }

    /**
     * This method creates a parent product folder
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     */
    private void createParentProductFolder(String parentProductFolderName) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        tabbedPaneNorthInstance.clkOnPimStudioTab(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNodeInstance.getBtnPimProductsNode(),
                browserDriver);
        CSLogger.info("Right clicked on product node");
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSLogger.info("Selected New Folder Option");
        productPopup.enterValueInDialogue(waitForReload,
                parentProductFolderName);
        productPopup.askBoxWindowOperation(waitForReload, true, browserDriver);
    }

    /**
     * This method created a child product
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     * @param childProductName
     *            String containing name of the child product
     * @param pressOkay
     *            Boolean value to i.e true to clicks on OK of pop while
     *            creating child product
     */
    private void createChildProduct(String parentProductFolderName,
            String childProductName, Boolean pressOkay) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getCreatedParentProductFolder(parentProductFolderName));
        CSUtility.rightClickTreeNode(waitForReload,
                getCreatedParentProductFolder(parentProductFolderName),
                browserDriver);
        productPopup.selectPopupDivMenu(waitForReload,
                productPopup.getCsGuiPopupMenuNewChild(), browserDriver);
        productPopup.enterValueInDialogue(waitForReload, childProductName);
        productPopup.askBoxWindowOperation(waitForReload, pressOkay,
                browserDriver);
    }

    /**
     * Gets created parent product folder
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder name
     * @return WebElement of created parent product folder
     */
    private WebElement getCreatedParentProductFolder(
            String parentProductFolderName) {
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.linkText(parentProductFolderName)));
        return browserDriver.findElement(By.linkText(parentProductFolderName));
    }

    /**
     * This method selects duplicate option by right clicking on product folder
     * and handles three options i.e Cancel,Recursive,Non-Recursive for
     * duplicating parent product folder
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     * @param userInput
     *            String value specifying options cancel,Recursive,Non-Recursive
     *            while creating duplicate products
     * @param childProductName
     *            String containing name of the child product
     */
    private void selectDuplicateOptionOfParentProduct(
            String parentProductFolderName, String userInput,
            String childProductName, String duplicatedProductName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                getCreatedParentProductFolder(parentProductFolderName),
                browserDriver);
        productPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupMenuObject());
        productPopup.selectPopupDivSubMenu(waitForReload,
                productPopup.getCsGuiPopupMenuSubMenuDuplicate(),
                browserDriver);
        productPopup.askBoxWindowOperationDuplicateProducts(waitForReload,
                userInput, browserDriver);
        verifyDuplicateRecursiveOption(parentProductFolderName,
                duplicatedProductName, childProductName);
    }

    /**
     * This method verifies that after selecting Recursive option selected
     * product along with its children should be duplicated properly
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     * @param childProductName
     *            String containing name of the child product
     */
    private void verifyDuplicateRecursiveOption(String parentProductFolderName,
            String duplicatedProductName, String childProductName) {
        try {
            verifyProductCreation(parentProductFolderName);
            browserDriver.findElement(By.linkText(duplicatedProductName))
                    .click();
            browserDriver.findElement(By.linkText(duplicatedProductName))
                    .click();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            int labelHeaderIndex = CSUtility.getListHeaderIndex("Label",
                    browserDriver);
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr[3]/"
                            + "td[" + labelHeaderIndex + "]")));
            Assert.assertEquals(childProductName,
                    browserDriver
                            .findElement(By
                                    .xpath("//table[@class='hidewrap CSAdminList"
                                            + "']/tbody/tr[3]/" + "td["
                                            + labelHeaderIndex + "]"))
                            .getText());
            CSLogger.info(
                    "Verification passed for Recursive option of duplicate "
                            + "product");
        } catch (Exception e) {
            CSLogger.error(
                    "Verification failed for Recursive option of duplicate "
                            + "product",
                    e);
            Assert.fail("Verification failed for Recursive option of duplicate "
                    + "product");
        }
    }

    /**
     * Checks the creation of products
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     */
    private void verifyProductCreation(String parentProductFolderName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        List<WebElement> duplicateProductFolder = browserDriver.findElements(
                By.linkText("Copy of " + parentProductFolderName));
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(
                        By.linkText("Copy of " + parentProductFolderName)));
        Assert.assertEquals(duplicateProductFolder.isEmpty(), !true);
    }

    /**
     * This method performs operation of clicking on derivation button from the
     * righmost section
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     * @param duplicatedProductName
     *            String containing name of duplicated product
     */
    public void goToDerivationSection(String parentProductFolderName,
            String duplicatedProductName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                getDuplicatedProductFolder(duplicatedProductName),
                browserDriver);
        productPopup.hoverOnCsGuiPopupMenu(browserDriver, waitForReload,
                productPopup.getCsGuiPopupMenuObject());
        CSUtility.tempMethodForThreadSleep(1000);
        productPopup.selectPopupDivSubMenu(waitForReload,
                productPopup.getCsGuiPopupMenuSubMenuViewFolderAsEditor(),
                browserDriver);
        clickOnProductDerivations(waitForReload);
    }

    /**
     * This method returns the webElement duplicatedProduct using linktext
     * 
     * @param duplicatedProductName
     *            String containing name of duplicated product
     * @return webElement duplicatedProduct
     */
    private WebElement getDuplicatedProductFolder(
            String duplicatedProductName) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.linkText(duplicatedProductName)));
        return browserDriver.findElement(By.linkText(duplicatedProductName));
    }

    /**
     * This method traverse to the Derivation section of product.
     * 
     * @param waitForReload
     *            object of WebDriverWait
     */
    private void clickOnProductDerivations(WebDriverWait waitForReload) {
        csGuiDialogContentIdInstance.clkBtnProperties(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                csGuiDialogContentIdInstance.getLblExternalKey());
        PimProductSplitareaContentRightPage pimProductSplitareaContentRight = pimProductTabsPageInstance
                .clickOnSpliareaContentRight();
        waitForReload
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(1));
        pimProductSplitareaContentRight
                .clickOnProductDerivations(waitForReload);
    }

    /**
     * This method returns the ID of the product passed as argument
     * 
     * @param productName
     *            String containing name of product (can be parent product or
     *            duplicated product)
     * @return product Id
     */
    public String getProductId(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        Actions actions = new Actions(browserDriver);
        actions.doubleClick(browserDriver.findElement(By.linkText(productName)))
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
     * This method switches the frames till FrmDerivationsTree frame
     */
    public void traverseToDerivationTree() {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        traverseTillFrmCsPortalGuiPaneContent(iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmDerivationsTree()));
        CSLogger.info("Traversed till FrmDerivationsTree frame");
    }

    /**
     * Verifies different properties of the product listed under property tab in
     * derivation section
     * 
     * @param productName
     *            String containing name of product (can be parent product or
     *            duplicated product)
     * @param createdProductId
     *            String containing ID of the product
     */
    public void verifyPropertiesTabContentsOfProduct(String productName,
            String createdProductId, String productAuthorName,
            String productWorkflowState) {
        traverseTillDerivationView();
        pimProductTabsPageInstance.clickOnPropertyTab(waitForReload);
        String productLabel = getPropertyFiledsValues("Label");
        String productId = getPropertyFiledsValues("ID");
        String productAuthor = getPropertyFiledsValues("Author");
        String productObjectType = getPropertyFiledsValues("Object Type");
        String productClass = getPropertyFiledsValues("Class");
        String productState = getPropertyFiledsValues("State");
        String productCreationDate = getPropertyFiledsValues("Creation Date");
        String productChangeDate = getPropertyFiledsValues("Change Date");
        if (productLabel.equals(productName)
                && productId.equals(createdProductId)
                && productAuthor.equals(productAuthorName)
                && productObjectType.equals("Product")
                && productClass.equals("-")
                && productState.equals(productWorkflowState)
                && productCreationDate.equals(getCurrentDate())
                && productChangeDate.equals(getCurrentDate())) {
            CSLogger.info(
                    "Verification passed for all fileds in property tab for "
                            + productName + "product");
        } else {
            CSLogger.info("Verification failed for fileds in property tab for"
                    + productName + "product");
            Assert.fail("Verification failed for fileds in property tab for "
                    + productName + "product");
        }
    }

    /**
     * Gets different values listed in derivation section
     * 
     * @param fieldName
     *            name of the property field
     * @return value of specified field
     */
    public String getPropertyFiledsValues(String fieldName) {
        return browserDriver.findElement(By.xpath("//td[contains(text(),'"
                + fieldName + "')]/following-sibling::td")).getText();
    }

    /**
     * This method returns current system date in yyy-MM-dd format
     * 
     * @return current date
     */
    public String getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        return DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
    }

    /**
     * This method switches the frames till FrmCsPortalGuiPaneContent frame
     * 
     * @param iframeLocatorsInstance
     *            Instance of FrameLocators class
     */
    public void traverseTillFrmCsPortalGuiPaneContent(
            FrameLocators iframeLocatorsInstance) {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmcsSideBarBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalGuiPaneContent()));
        CSLogger.info("Traversed till FrmCsPortalGuiPaneContent frame");
    }

    /**
     * This method traverse the frame till FrmDerivationsView frame
     */
    public void traverseTillDerivationView() {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriver);
        traverseTillFrmCsPortalGuiPaneContent(iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmDerivationsView()));
        CSLogger.info("Traversed till FrmDerivationsView frame");
    }

    /**
     * This method verifies whether the parent and duplicated product is listed
     * in tree under derivation section in proper order or not
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     * @param duplicatedProductName
     *            String containing name of duplicated product
     */
    public void verifyTreeViewOfParentChildProduct(
            String parentProductFolderName, String duplicatedProductName) {
        try {
            traverseToDerivationTree();
            CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                    .findElement(By.xpath("//table[@class='treeline']")));
            int presenceOfParentProduct = browserDriver
                    .findElements(By.linkText(parentProductFolderName)).size();
            if (presenceOfParentProduct != 0) {
                CSLogger.info(
                        "verification passed: Under derivation section in tree "
                                + "view parent product is listed");
            } else {
                CSLogger.error(
                        "verification passed: Under derivation section in tree "
                                + "view parent product is listed");
                Assert.fail(
                        "verification passed: Under derivation section in tree "
                                + "view parent product is listed");
            }
            int presenceOfDuplicatedProduct = browserDriver
                    .findElements(By.linkText(duplicatedProductName)).size();
            if (presenceOfDuplicatedProduct != 0) {
                CSLogger.info(
                        "verification passed: Under derivation section in tree "
                                + "view duplicated product is listed");
            } else {
                CSLogger.error(
                        "verification passed: Under derivation section in tree"
                                + " view duplicated product is listed");
                Assert.fail(
                        "verification passed: Under derivation section in tree "
                                + "view duplicated product is listed");
            }
        } catch (Exception e) {
            CSLogger.error(
                    "Error in tree view of parent product and duplicted product ",
                    e);
        }
    }

    /**
     * This method checks whether the product as argument is highlighted in blue
     * or not
     * 
     * @param productName
     *            String containing name of product (can be parent product or
     *            duplicated product)
     * @return boolean value true or false
     */
    public Boolean checkWhetherProductIsHighlighted(String productName) {
        traverseToDerivationTree();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath(
                        "//span[contains(text(),' " + productName + "')]")));
        browserDriver
                .findElement(By.xpath(
                        "//span[contains(text(),' " + productName + "')]"))
                .click();
        String styleAttribute = browserDriver
                .findElement(By.xpath(
                        "//span[contains(text(),' " + productName + "')]"))
                .getAttribute("style");
        if (styleAttribute.contains("rgb(50, 72, 167)")) {
            return true;
        } else
            return false;
    }

    /**
     * This method first checks whether the duplicated product is highlighted in
     * blue or not then verifies the preview image of duplicated product and at
     * last checks the fields present under property tab of duplicated product
     * for eg. Label,Author,Creation date,Change date etc
     * 
     * @param duplicatedProductName
     *            String containing name of duplicated product
     * @param duplicatedProductId
     */
    public void checkDerivationOfDuplicatedProduct(String duplicatedProductName,
            String duplicatedProductId, String productAuthorName,
            String productWorkflowState) {
        SoftAssert softAssertion = new SoftAssert();
        Boolean duplicatedProductIsHighlighted = checkWhetherProductIsHighlighted(
                duplicatedProductName);
        if (duplicatedProductIsHighlighted) {
            CSLogger.info(
                    "Duplicated product is highlighted in blue test case passed");
        } else {
            CSLogger.error(
                    "Duplicated product is not highlighted in blue test case "
                            + "passed");
            softAssertion
                    .fail("Duplicated product is not highlighted in blue test case "
                            + "failed");
        }
        checkPreviewOfProduct(duplicatedProductName);
        verifyPropertiesTabContentsOfProduct(duplicatedProductName,
                duplicatedProductId, productAuthorName, productWorkflowState);
    }

    /**
     * This method verifies that image preview is displayed under preview tab
     * 
     * @param productName
     *            String containing name of product (can be parent product or
     *            duplicated product)
     */
    public void checkPreviewOfProduct(String productName) {
        traverseTillDerivationView();
        pimProductTabsPageInstance.clickOnPreviewTab(waitForReload);
        CSUtility.waitForVisibilityOfElement(waitForReload, browserDriver
                .findElement(By.xpath("//div[@cs_name='Preview']")));
        int presenceOfPreviewImage = browserDriver
                .findElements(By.id("preview_image")).size();
        if (presenceOfPreviewImage != 0) {
            CSLogger.info("Preview image exists for the product " + productName
                    + " test passed");
        } else {
            CSLogger.error("Preview image does not exists for the "
                    + productName + "product test passed");
        }
    }

    /**
     * This method first checks whether the parent product is highlighted in
     * blue or not then verifies the preview image of parent product and at last
     * checks the fields present under property tab of parent product for eg.
     * Label,Author,Creation date,Change date etc
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     * @param parentProductId
     */
    public void checkDerivationOfParentProduct(String parentProductFolderName,
            String parentProductId, String productAuthorName,
            String productWorkflowState) {
        traverseToDerivationTree();
        getCreatedParentProductFolder(parentProductFolderName).click();
        Boolean parentProductIsHighlighted = checkWhetherProductIsHighlighted(
                parentProductFolderName);
        if (parentProductIsHighlighted) {
            CSLogger.info(
                    "Parent product is highlighted in blue test case passed");
        } else {
            CSLogger.error(
                    "Parent product is not highlighted in blue test case passed");
            Assert.fail(
                    "Parent product is not highlighted in blue test case failed");
        }
        checkProductEditorWindow(parentProductFolderName);
        checkPreviewOfProduct(parentProductFolderName);
        verifyPropertiesTabContentsOfProduct(parentProductFolderName,
                parentProductId, productAuthorName, productWorkflowState);
    }

    /**
     * This method verifies that after double clicking on parent folder a editor
     * window pops up with product information verified the label of the product
     * is same as the name of the parent product
     * 
     * @param parentProductFolderName
     *            String containing name of parent product folder
     */
    public void checkProductEditorWindow(String parentProductFolderName) {
        try {
            traverseToDerivationTree();
            String windowHandleBeforeOpeningEditorWindow = browserDriver
                    .getWindowHandle();
            Actions actions = new Actions(browserDriver);
            actions.doubleClick(
                    getCreatedParentProductFolder(parentProductFolderName))
                    .perform();
            switchToCurrentWindowHandle();
            Assert.assertEquals(parentProductFolderName,
                    getParentProductLabelValue());
            CSLogger.info(
                    "After double clicking on parent product editor window pops"
                            + " up testcase passed");
            browserDriver.close();
            browserDriver.switchTo()
                    .window(windowHandleBeforeOpeningEditorWindow);
        } catch (Exception e) {
            CSLogger.error(
                    "After double clicking on parent product editor window does "
                            + "not pops up testcase failed",
                    e);
        }
    }

    /**
     * Gets the value of label field of parent product
     * 
     * @return value of label field
     */
    public String getParentProductLabelValue() {
        CSUtility.switchToDefaultFrame(browserDriver);
        return csGuiDialogContentIdInstance.getTxtProductLabel()
                .getAttribute("value");
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
     * This is a data provider method contains array of parent product folder
     * name and child product name
     * 
     * @return Array String method contains array of product name and child
     *         product name
     */
    @DataProvider(name = "ProductDerivationTestData")
    public Object[][] getProductDerivationData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                productDerivationSheetName);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeMethod
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        tabbedPaneNorthInstance = new TabbedPaneNorth(browserDriver);
        pimStudioProductsNodeInstance = ProductsPimStudioTree
                .getPimStudioProductsNodeInstance(browserDriver);
        productPopup = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        pimProductTabsPageInstance = new PimProductTabsPage(browserDriver);
    }
}
