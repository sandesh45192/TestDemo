/*
3 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pim;

import java.util.List;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test methods to 'Drag & Drop' attributes onto the
 * newly created class
 * 
 * @author CSAutomation Team
 *
 */
public class DragDropAttributeOntoCreatedClassTest extends AbstractTest {

    private WebDriverWait         waitForReload;
    private Actions               action;
    private PimStudioSettingsNode pimStudioSettingsNodeInstance;
    private String                dragAndDropAttrToClassSheet = "DragAndDropAttrToClass";

    /**
     * This method initializes all the required instances for test.
     */
    @BeforeClass
    public void initializeResources() {
        pimStudioSettingsNodeInstance = new PimStudioSettingsNode(
                browserDriver);
        CSLogger.info(
                "Initialized the POM object for Drag & Drop attribute to class page.");
        action = new Actions(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        CSPopupDivPim.getCSPopupDivLocators(browserDriver);
    }

    /**
     * This is the test method which drives the usecase, ie. 'drag & drop attr'
     * onto the newly created class. Contains attribute folder name ,class name
     * and attribute names
     */
    @Test(dataProvider = "DragAndDropAttributeToClass")
    public void testDragDropAttrOntoClass(String attributeFolderName,
            String className, String attributeName) {
        try {
            CSUtility.traverseToPimStudio(waitForReload, browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance.getBtnPimSettingsNode());
            pimStudioSettingsNodeInstance.getBtnPimSettingsNode().click();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance
                            .getBtnPimSettingsAttributeNode());
            pimStudioSettingsNodeInstance.getBtnPimSettingsAttributeNode()
                    .click();
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    pimStudioSettingsNodeInstance
                            .getBtnPimSettingsClassesNode());
            pimStudioSettingsNodeInstance.getBtnPimSettingsClassesNode()
                    .click();
            WebElement attrFolderToDragDrop = browserDriver
                    .findElement(By.linkText(attributeFolderName));
            WebElement classNameToDragDrop = browserDriver
                    .findElement(By.linkText(className));
            action.dragAndDrop(attrFolderToDragDrop, classNameToDragDrop)
                    .perform();
            CSLogger.info(
                    "Successfully dragged and droppped attribute to class");
            getAttributeNamesAndVerify(waitForReload, className, attributeName);
            CSLogger.info("Assertion completed for drag and drop attributes.");
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error in test method : testDragDropAttrOntoClass"
                            + e);
            Assert.fail(
                    "Automation Error in test method : testDragDropAttrOntoClass"
                            + e);
        }
    }

    /**
     * This method gets all assigned attribute names to the class and verifies
     * with attributes present in the excel sheet
     * 
     * @param waitForReload
     * @param className contains the classname
     * @param attribute contains the attributes which needs to assign to given
     *            class
     * 
     */
    private void getAttributeNamesAndVerify(WebDriverWait waitForReload,
            String className, String attribute) {
        try {
            WebElement nameOfClass = browserDriver
                    .findElement(By.linkText(className));
            CSUtility.waitForVisibilityOfElement(waitForReload, nameOfClass);
            nameOfClass.click();
            CSUtility.switchToDefaultFrame(browserDriver);
            CSUtility.traverseToSettingsConfiguration(waitForReload,
                    browserDriver);
            String[] attributeNames = attribute.split(",");
            List<WebElement> attributeXPaths = browserDriver.findElements(
                    By.xpath("//table[@class='CSGuiTable']//td[1]"));
            String[] linkText = new String[attributeXPaths.size()];
            int temp = 0;
            for (WebElement attributePaths : attributeXPaths) {
                String string = "Distribution channel";
                if (!attributePaths.equals(string))
                    linkText[temp] = attributePaths.getText();
                if (linkText[temp].contains(string))
                    linkText[temp] = linkText[temp].replace(string, "");
                temp++;
            }
            for (int classXpathIndex = 0; classXpathIndex < linkText.length; classXpathIndex++) {
                for (int attributeIndex = 0; attributeIndex < attributeNames.length; attributeIndex++) {
                    if (linkText[classXpathIndex]
                            .equalsIgnoreCase(attributeNames[attributeIndex])) {
                        CSLogger.info(
                                "Attribute" + attributeNames[attributeIndex]
                                        + "is Present..");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            CSLogger.info("Attributes are not verified ..");
        }
    }

    /**
     * This data provider returns sheet name for drag and drop attribute folder
     * to the class which contains attribute folder name,class name and
     * attribute names
     * 
     * @return array of the attribute folder name,class name and attribute names
     */
    @DataProvider(name = "DragAndDropAttributeToClass")
    public Object[][] getProductChildData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("pimModuleTestCases"),
                dragAndDropAttrToClassSheet);
    }

}
