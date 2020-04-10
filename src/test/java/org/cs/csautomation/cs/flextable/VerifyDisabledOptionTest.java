/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.flextable;

import java.util.ArrayList;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.settings.FlexTablePage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies Disabled Option test
 * uses.
 * 
 * @author CSAutomation Team
 */
public class VerifyDisabledOptionTest extends AbstractTest {

    private WebDriverWait             waitForReload;
    private PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private CSPortalHeader            csPortalHeaderInstance;
    private IProductPopup             productPopUp;
    private Actions                   action;
    private CSGuiDialogContentId      csGuiDialogContentIdInstance;
    private CSPortalHeader            csPortalHeader;
    private FrameLocators             locator;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private FlexTablePage             flexTablePage;
    private String                    disableOptionTestData = "DisableOption";

    /**
     * This method verifies Disabled Option test uses.
     * 
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     */
    @Test(dataProvider = "DisableOptionDataSheet")
    public void testVerifyDisabledOption(String className, String flexProduct,
            String templateName, String templateType) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            createProduct(flexProduct);
            dragAndDropClassToProduct(className, flexProduct);
            setDisableOptionOfTable(templateName, templateType);
            verifyDisableOption(flexProduct, templateName);
            CSLogger.info("verify disabled option test completed");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testVerifyDisabledOption", e);
            Assert.fail("Automation Error : testVerifyDisabledOption", e);
        }
    }

    /**
     * This method verifies the disable option test case.
     * 
     * @param flexProduct String object contains product name
     * @param templateName String object contains template name
     */
    private void verifyDisableOption(String flexProduct, String templateName) {
        getConfigureTableDialogBox(flexProduct);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getDrpDwnPresetId());
        int presetIds = browserDriver
                .findElements(By.xpath("//select[@id='PresetID']//option"))
                .size();
        ArrayList<String> allOptions = new ArrayList<>();
        for (int row = 1; row < presetIds; row++) {
            allOptions.add(browserDriver
                    .findElements(By.xpath("//select[@id='PresetID']//option"))
                    .get(row).getText());
        }
        if (allOptions.contains(templateName)) {
            Assert.fail(templateName + " has displayed");
        } else {
            CSLogger.info(templateName + " has not displayed test case pass.");
        }
        CSUtility.waitForVisibilityOfElement(waitForReload,
                flexTablePage.getBtnOk());
        flexTablePage.getBtnOk().click();
    }

    /**
     * This method get the dialog box configuration table.
     * 
     * @param flexProduct String object contains product name
     */
    private void getConfigureTableDialogBox(String flexProduct) {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement flexTableProduct = browserDriver
                .findElement(By.linkText(flexProduct));
        flexTableProduct.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        flexTablePage.clkOnDataTab(waitForReload);
        WebElement rightSectionTable = browserDriver.findElement(By.xpath(
                "(//table[@class='Flex Standard FlexTable']/thead/tr/th)[1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, rightSectionTable);
        action.moveToElement(rightSectionTable).perform();
        CSUtility.tempMethodForThreadSleep(1000);
        flexTablePage.getBtnFlexSetting().click();
        CSLogger.info("Clicked on add button");
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
    }

    /**
     * This method verifies set the disable option.
     * 
     * @param templateName String object contains template name
     * @param templateType String object contains template type
     */
    private void setDisableOptionOfTable(String templateName,
            String templateType) {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        flexTablePage.goToNodeFlexTablePreset(waitForReload, browserDriver,
                locator);
        WebElement freeNode = browserDriver
                .findElement(By.linkText(templateType));
        clkElement(freeNode);
        WebElement nameOfTemplate = browserDriver
                .findElement(By.linkText(templateName));
        clkElement(nameOfTemplate);
        CSUtility.tempMethodForThreadSleep(1000);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        flexTablePage.clkOnGeneralPane(waitForReload);
        String attribute = flexTablePage.getBtnDisable().getAttribute("class");
        if (attribute.contains("Off")) {
            flexTablePage.clkOnBtnDisable(waitForReload);
        }
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method clicks on input element
     * 
     * @param element contains web element to be clicked
     */
    public void clkElement(WebElement element) {
        CSUtility.waitForElementToBeClickable(waitForReload, element);
        element.click();
    }

    /**
     * This method create product.
     * 
     * @param productName String object contains product name
     */
    private void createProduct(String flexProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        productPopUp.selectPopupDivMenu(waitForReload,
                productPopUp.getCsGuiPopupMenuCreateNew(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        productPopUp.enterValueInDialogue(waitForReload, flexProduct);
        productPopUp.askBoxWindowOperation(waitForReload, true, browserDriver);
        CSLogger.info("Product Created");
    }

    /**
     * This method drag and drop class to product.
     * 
     * @param className String object contains class name
     * @param flexProduct String object contains product name
     */
    private void dragAndDropClassToProduct(String className,
            String flexProduct) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        WebElement productToDragDrop = browserDriver
                .findElement(By.linkText(flexProduct));
        clkOnPimSettingsTree();
        CSUtility.waitForVisibilityOfElement(waitForReload,
                pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode());
        pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode().click();
        WebElement classNameToDragDrop = browserDriver
                .findElement(By.linkText(className));
        action.dragAndDrop(classNameToDragDrop, productToDragDrop).build()
                .perform();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getproductWindow()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentIdInstance.getBtnReplace()));
        csGuiDialogContentIdInstance.getBtnReplace().click();
        CSLogger.info("Drag and Drop class to Product");
    }

    /**
     * Clicks on PIM settings tree node
     */
    private void clkOnPimSettingsTree() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOf(
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode()));
        CSUtility.scrollUpOrDownToElement(
                pimStudioSettingsNodeInstance.getBtnPimSettingsNode(),
                browserDriver);
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
        CSLogger.info("Clicked on pim settings tree");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains ClassName,FlexTableProductName,TemplateName,TemplateType
     * 
     * @return disableOptionTestData
     */
    @DataProvider(name = "DisableOptionDataSheet")
    public Object[] disableOption() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("flexTableTestCases"),
                disableOptionTestData);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 180);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        flexTablePage = new FlexTablePage(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        productPopUp = new CSPopupDivPim(browserDriver);
        action = new Actions(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
    }
}
