/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.facetedsearch;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods which verifies attribute mapping in
 * class
 * 
 * @author CSAutomation Team
 */
public class AttributeMappingInClassTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private PimStudioSettingsNode  pimStudioSettingsNodeInstance;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private CSGuiDialogContentId   csGuiDialogContentIdInstance;
    private FrameLocators          iframeLocatorsInstance;
    private String                 attributeMappingInClassSheet = "AttributeMappingInClass";

    /**
     * This method verifies attribute mapping in class.
     * 
     * @param className String object contains class name
     * @param attrName String object contains attribute name
     * @param paneTitle String object contains pane title
     * @param sectionTitle String object contains section title
     */
    @Test(dataProvider = "attributeMappingInClass")
    public void testAttributeMappingInClass(String className, String attrName,
            String paneTitle, String sectionTitle) {
        try {
            csPortalHeaderInstance.clkBtnProducts(waitForReload);
            attributeMappingInClass(className, attrName, paneTitle,
                    sectionTitle);
        } catch (Exception e) {
            CSLogger.debug("Automation Error : testConfigurationOfFacetedClass",
                    e);
            Assert.fail("Automation Error : testConfigurationOfFacetedClass",
                    e);
        }
    }

    /**
     * This method map attribute in class.
     * 
     * @param className String object contains class name
     * @param attrName String object contains attribute name
     * @param paneTitle String object contains pane title
     * @param sectionTitle String object contains section title
     */
    private void attributeMappingInClass(String className, String attrName,
            String paneTitle, String sectionTitle) {
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        pimStudioSettingsNodeInstance
                .clkBtnPimSettingsClassesNode(waitForReload);
        WebElement elementOfClass = browserDriver
                .findElement(By.linkText(className));
        elementOfClass.click();
        CSUtility.tempMethodForThreadSleep(2000);
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarRefresh(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiDialogContentIdInstance.clkWebElement(waitForReload,
                csGuiDialogContentIdInstance.getTabAttributeMapping());
        selectAttributeAndSendData(attrName, paneTitle, sectionTitle);
    }

    /**
     * This method select attribute and add data in it.
     * 
     * @param attrName String object contains attribute name
     * @param paneTitle String object contains pane title
     * @param sectionTitle String object contains section title
     */
    private void selectAttributeAndSendData(String attrName, String paneTitle,
            String sectionTitle) {
        int paneIndex = 0;
        int sectionIndex = 0;
        String headerText = null;
        String[] attrArray = attrName.split(",");
        WebElement attrElement = null;
        CSUtility.traverseToAttributeMapping(waitForReload, browserDriver,
                iframeLocatorsInstance);
        List<WebElement> tabetHeader = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']//tbody//tr[2]//th//a"));
        for (int column = 0; column < tabetHeader.size(); column++) {
            headerText = tabetHeader.get(column).getText();
            if (headerText.contains("Pane Title")) {
                paneIndex = column + 2;
            }
            if (headerText.contains("Section Title")) {
                sectionIndex = column + 2;
            }
        }
        for (String nameOfAttribute : attrArray) {
            CSUtility.traverseToAttributeMapping(waitForReload, browserDriver,
                    iframeLocatorsInstance);
            attrElement = browserDriver.findElement(By
                    .xpath("//td[contains(text(),'" + nameOfAttribute + "')]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, attrElement);
            attrElement.click();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.traverseToEditWindowAttributeMapping(waitForReload, browserDriver, iframeLocatorsInstance);
            sendTextToEelment(csGuiDialogContentIdInstance.getTxtPaneTitle(),
                    paneTitle);
            sendTextToEelment(csGuiDialogContentIdInstance.getTxtSectionTitle(),
                    sectionTitle);
            Select facetedSearch = new Select(csGuiDialogContentIdInstance
                    .getDrpDwnMappingFacetedSearch());
            facetedSearch.selectByVisibleText("Yes");
            Select facetedConcatenator = new Select(csGuiDialogContentIdInstance
                    .getDrpDwnMappingFacetedConcatenator());
            facetedConcatenator.selectByVisibleText("OR Concatenator");
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarSave(waitForReload);
            verifySavedData(nameOfAttribute, paneTitle, sectionTitle, paneIndex,
                    sectionIndex);
        }
    }

    /**
     * This method verifies the mapping data in class
     * 
     * @param attrName String object contains attribute name
     * @param paneTitle String object contains pane title
     * @param sectionTitle String object contains section title
     * @param paneIndex interger veriable contains index of pane title in table
     * @param sectionIndex interger veriable contains index of section title in
     *            table
     */
    private void verifySavedData(String attrName, String paneTitle,
            String sectionTitle, int paneIndex, int sectionIndex) {
        String paneText = null;
        String sectionText = null;
        CSUtility.traverseToAttributeMapping(waitForReload, browserDriver,
                iframeLocatorsInstance);
        WebElement paneElement = null;
        WebElement sectionElement = null;
        paneElement = browserDriver
                .findElement(By.xpath("//td[contains(text(),'" + attrName
                        + "')]/..//td[" + paneIndex + "]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, paneElement);
        paneText = paneElement.getText();
        sectionElement = browserDriver
                .findElement(By.xpath("//td[contains(text(),'" + attrName
                        + "')]/..//td[" + sectionIndex + "]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, sectionElement);
        sectionText = paneElement.getText();
        if (paneText.equals(paneTitle) && sectionText.equals(sectionTitle)) {
            CSLogger.info("Data is save successfully");
        } else {
            CSLogger.error("Data is not saved");
            Assert.fail("Data is not saved");
        }
    }

    /**
     * This method send the text data to the WebElement
     * 
     * @param txtelement WebElement object contains element of text input
     * @param text String object contains text data
     */
    private void sendTextToEelment(WebElement txtelement, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, txtelement);
        txtelement.clear();
        txtelement.sendKeys(text);
        CSLogger.info(text + " data is sent.");
    }

    /**
     * This data provider returns the sheet data to run the test case which
     * contains Class Name,Attribute Name,Pane Title,Section Title
     * 
     * @return attributeMappingInClassSheet
     */
    @DataProvider(name = "attributeMappingInClass")
    public Object[] attributeMappingInClass() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("facetedSearchTestCases"),
                attributeMappingInClassSheet);
    }

    /**
     * This method initializes all the resources required to drive the test case
     * 
     */
    @BeforeClass
    private void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 60);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        csGuiDialogContentIdInstance = new CSGuiDialogContentId(browserDriver);
        iframeLocatorsInstance = new FrameLocators(browserDriver);
    }
}
