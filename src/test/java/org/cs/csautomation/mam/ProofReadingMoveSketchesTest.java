/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.util.HashMap;
import java.util.List;
import org.cs.csautomation.cs.mam.CSGuiToolbarHorizontalMam;
import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
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
 * This Class perform the test cases related to the shifting the placed object
 * in the whiteboard from one page to another.
 * 
 * @author CSAutomation Team
 *
 */
public class ProofReadingMoveSketchesTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private CSPopupDivMam             csPopupDiv;
    private FrameLocators             locator;
    private IVolumePopup              demoVolumePopup;
    private CSGuiToolbarHorizontalMam csGuiToolbarHorizontalMam;
    private HashMap<String, String>   pageTemplateMap;
    private CSPopupDivSettings        popupDivSettings;
    private String                    deleteAlert                    = "Do you "
            + "really want to remove the selected products?";
    private String                    moveSketchesToAnotherPageSheet = "moveSketchesInProofReading";

    /**
     * This method test the test case of moving the placed object from one page
     * to another.
     * 
     * @param filePath String object containing the path of indesign file.
     * @param pageTemplateName String object containing name of page template to
     *            assign.
     * @param productPath String object containing path of object
     */
    @Test(dataProvider = "moveSketchesTestData")
    public void testMoveSketchesToAnotherPage(String filePath,
            String pageTemplateName, String productPath) {
        try {
            pageTemplatesTestCase(filePath, pageTemplateName);
            objectPlacementTestCase(productPath);
            CSLogger.info("test move sketches to another page completed");
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
            Assert.fail("Test case failed");
        }
    }

    /**
     * This method perform test case for assignment of page template to white
     * board.
     * 
     * @param filePath String object containing the path of indesign file.
     * @param pageTemplateName String object containing name of page template to
     *            assign.
     */
    public void pageTemplatesTestCase(String filePath,
            String pageTemplateName) {
        try {
            Actions actions = new Actions(browserDriver);
            pageTemplateName = pageTemplateName.replace("*", "x");
            pageTemplateName = pageTemplateName.replaceAll("\\s+", "");
            String[] nodes = filePath.split("//");
            switchToMAM(waitForReload);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            traverseToIndesignFile(nodes);
            createWhiteBoardFromIndd(nodes[nodes.length - 1]);
            switchToMAM(waitForReload);
            traverseToIndesignFile(nodes);
            clickRefreshOnParent(nodes[nodes.length - 2]);
            /*
             * Sleep of 5 mins is required for synchronization of wdb file
             */
            CSUtility.tempMethodForThreadSleep(300000);
            switchToMAM(waitForReload);
            traverseToIndesignFile(nodes);
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    nodes[nodes.length - 1].replace(".indd", ".wbd"));
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            handleSplitareaFrame();
            handleCheckinState();
            CSUtility.tempMethodForThreadSleep(2000);
            clickOnPageTemplateIcon();
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            traverseToToolbarFrameForTemplate();
            String templateXpath = "//tr[@id='"
                    + pageTemplateMap.get(pageTemplateName) + "']/td[2]/div/a";
            WebElement templateElement = null;
            holdSelectPageTemplate(actions, templateXpath);
            dragDropPageTemplateOnWhiteBoard(actions, "1");
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            traverseToToolbarFrameForTemplate();
            holdSelectPageTemplate(actions, templateXpath);
            dragDropPageTemplateOnWhiteBoard(actions, "2");
            refreshForPageTemplateAssignment();
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            traverseToToolbarFrameForTemplate();
            templateElement = browserDriver
                    .findElement(By.xpath(templateXpath));
            actions.moveToElement(templateElement).build().perform();
            traverseToCenterFrame();
            verifyAssignedPageTemplate(pageTemplateName);
            CSLogger.info("page templates test completed");
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method traverse to the MAM module from Home module of application
     * and then expands the volume tree.
     * 
     * @param waitForReload WebDriverWait Object
     */
    private void switchToMAM(WebDriverWait waitForReload) {
        csPortalHeader.clkBtnMedia(waitForReload);
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
    }

    /**
     * This method clicks on the Mam Volume folder and expands Demo folder and
     * InDesign folder.
     */
    private void traverseToIndesignFile(String nodes[]) {
        mamStudioVolumesNode.getBtnMamVolumesNode().click();
        for (int index = 0; index < nodes.length - 1; index++) {
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    nodes[index]);
            CSUtility.tempMethodForThreadSleep(1000);
        }
    }

    /**
     * This method performs right click on the indd file and selects the option
     * 'create whiteboard' from the pop-up menu.
     */
    private void createWhiteBoardFromIndd(String inddFileName) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()=' " + inddFileName.trim() + "']")));
        WebElement inddFileElement = browserDriver.findElement(
                By.xpath("//span[text()=' " + inddFileName.trim() + "']"));
        CSUtility.rightClickTreeNode(waitForReload, inddFileElement,
                browserDriver);
        traverseToPopupMenu();
        popupDivSettings.selectPopupDivMenu(waitForReload,
                csPopupDiv.getCtxProcess(), browserDriver);
        popupDivSettings.selectPopupDivSubMenu(waitForReload,
                csPopupDiv.getCsPopupCreateWhiteboard(), browserDriver);
        CSLogger.info("Clicked on the Create Whiteboard from context click.");
    }

    /**
     * This method traverse to the Pop-up menu div.
     */
    private void traverseToPopupMenu() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
    }

    /**
     * This method perform right click on the folder Templates-CS and selects
     * the refresh option from the pop-up menu.
     */
    private void clickRefreshOnParent(String parentNode) {
        WebElement inddFileParentElement = browserDriver.findElement(
                By.xpath("//span[text()=' " + parentNode.trim() + "']"));
        CSUtility.rightClickTreeNode(waitForReload, inddFileParentElement,
                browserDriver);
        traverseToPopupMenu();
        CSUtility.tempMethodForThreadSleep(1000);
        csPopupDiv.getCsPopupRefresh().click();
        CSLogger.info("Clicked on the Refresh option from context menu.");
    }

    /**
     * This method perform the frame traversing operation.
     */
    private void traverseToToolbarFrameForObject() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmcsSideBarBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmMamWhiteBoardPageObjectSelection()));
    }

    /**
     * This method performs checkOut on white board file if the file is in
     * CheckIn state.
     */
    private void handleCheckinState() {
        CSUtility.tempMethodForThreadSleep(1000);
        List<WebElement> btnCheckin = browserDriver.findElements(
                By.xpath("//a/img[contains(@src,'checkin.gif')]"));
        int btnCheckinSize = btnCheckin.size();
        if (btnCheckinSize != 0) {
            CSLogger.info("whiteboard file is already in checkout stage");
        } else {
            List<WebElement> btnCheckout = browserDriver.findElements(
                    By.xpath("//a/img[contains(@src,'checkout.gif')]"));
            int btnCheckoutSize = btnCheckout.size();
            if (btnCheckoutSize != 0) {
                csGuiToolbarHorizontalMam.getBtnCheckout().click();
            }
            CSLogger.info(
                    "Performed checkOut operation on the whiteboard file.");
        }
    }

    /**
     * This method handles the split bar which hides the right frame.
     */
    private void handleSplitareaFrame() {
        WebElement splitAreaFrame = browserDriver
                .findElement(By.xpath("//td[@class='splitareaborderright']/"
                        + "img[@class='splitareaborderimage']/.."));
        if (splitAreaFrame.getAttribute("style")
                .contains("CSGuiSplitareaToggleLeft")) {
            splitAreaFrame.click();
            CSLogger.info("Clicked on the splitarea frame for the visiblity of "
                    + "Page Template and Object toolbars.");
        }
    }

    /**
     * This method perform click operation on the page template icon.
     */
    private void clickOnPageTemplateIcon() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmcsSideBarBody()));
        WebElement pageTemplateIcon = browserDriver.findElement(
                By.xpath("//a[contains(text(),'Page Templates')]/.."));
        pageTemplateIcon.click();
        CSLogger.info(
                "Clicked on the page template icon on the right toolbar.");
    }

    /**
     * This method perform the frame traversing operation.
     */
    private void traverseToToolbarFrameForTemplate() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmcsSideBarBody()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmMamWhiteBoardPageTemplate()));
    }

    /**
     * This method performs refresh operation on whiteboard file.
     */
    private void refreshForPageTemplateAssignment() {
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalMam.getBtnRefresh().click();
        CSLogger.info("Clicked on the refresh button for WhiteBoard.");
    }

    /**
     * This method performs drag and drop operation of page template on
     * whiteboard file.
     * 
     * @param actions Action object
     * @param pageNumber String Object containing the page number
     */
    private void dragDropPageTemplateOnWhiteBoard(Actions actions,
            String pageNumber) {
        WebElement pageForTemplate = browserDriver.findElement(
                By.xpath("//img[@id='preview_00" + pageNumber + "']"));
        actions.moveToElement(pageForTemplate).release(pageForTemplate).build()
                .perform();
        CSUtility.tempMethodForThreadSleep(1000);
        CSLogger.info("Performed drag and drop operation for page template.");
    }

    /**
     * This method clicks and holds the page template.
     * 
     * @param actions Action Object
     * @param templateXpath String Object containing the xpath of page template.
     */
    private void holdSelectPageTemplate(Actions actions, String templateXpath) {
        WebElement templateElement;
        templateElement = browserDriver.findElement(By.xpath(templateXpath));
        actions.clickAndHold(templateElement).build().perform();
        traverseToCenterFrame();
        CSLogger.info("Select and hold the page template.");
    }

    /**
     * This method perform operation frame traversing to center frame.
     */
    private void traverseToCenterFrame() {
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        switchToSplitAreaFrameCenter();
        CSLogger.info("Traversed to the center frame.");
    }

    /**
     * This method traverse to the splitAreaFrame
     */
    private void switchToSplitAreaFrameCenter() {
        waitForReload
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmCenter()));
        CSLogger.info("Switched to the center frame.");
    }

    /**
     * This method verifies the assigned page to object.
     * 
     * @param pageTemplateName String object containing the name of page
     *            template.
     */
    private void verifyAssignedPageTemplate(String pageTemplateName) {
        WebElement assignedTemplate = browserDriver.findElement(
                By.xpath("(//div[@class='CometTemplateContainer' and"
                        + " contains(@id,'CometTemplate')])[1]/div[1]"));
        String assignedTemplateName = assignedTemplate.getText();
        Assert.assertEquals(assignedTemplateName.replaceAll("\\s+", ""),
                pageTemplateName);
    }

    /**
     * This method test the placement of object i.e product/channel/flex.
     * 
     * @param objectPath String object containing path of object
     * 
     */
    public void objectPlacementTestCase(String objectPath) {
        try {
            String[] nodes = objectPath.split("//");
            Actions actions = new Actions(browserDriver);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            clickOnObjectsIcon();
            CSUtility.tempMethodForThreadSleep(1000);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            traverseToToolbarFrameForObject();
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmCenter()));
            CSUtility.tempMethodForThreadSleep(1000);
            handleAutoExpandingOfNodeTree();
            CSUtility.tempMethodForThreadSleep(1000);
            traverseToObject(nodes);
            closeNodeTree(nodes);
            String objectName = nodes[nodes.length - 1];
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameFrmCenter()));
            holdSelectObject(actions, objectName);
            traverseToBottomFrame();
            dragDropObjectInDocumentSection(actions, "1");
            CSUtility.tempMethodForThreadSleep(2000);
            holdSelectObjectInDocumentSection(actions, objectName);
            traverseToCenterFrame();
            dragDropObjectInWhiteBoard(actions);
            traverseToBottomFrame();
            verifyObjectPlacementStatus(objectName, "1");
            CSUtility.tempMethodForThreadSleep(1000);
            holdSelectObjectInDocumentSection(actions, objectName);
            dragDropObjectInDocumentSection(actions, "2");
            CSUtility.tempMethodForThreadSleep(1000);
            verifyObjectPlacementStatus(objectName, "2");
            CSUtility.tempMethodForThreadSleep(6000);
            CSLogger.info("Object placement test completed");
            objectRemoveTestCase(objectName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method perform the test case related to removal on the object
     * placement i.e product/channel/flex.
     * 
     * @param objectName String Object containing the name of the object.
     */
    public void objectRemoveTestCase(String objectName) {
        try {
            traverseToBottomFrame();
            selectCheckboxOfObjectInDocumentSection(objectName);
            CSUtility.tempMethodForThreadSleep(1000);
            WebElement iconObjectTemplate = browserDriver.findElement(By.xpath(
                    "//img[contains(@src,'cometplacementstate.deleted.svg')]/.."));
            iconObjectTemplate.click();
            Alert alertForDelete = CSUtility.getAlertBox(waitForReload,
                    browserDriver);
            Assert.assertEquals(alertForDelete.getText(), deleteAlert);
            alertForDelete.accept();
            CSUtility.tempMethodForThreadSleep(1000);
            List<WebElement> placedObjects = browserDriver.findElements(
                    By.xpath("//td[contains(text(),'" + objectName + "')]/.."));
            Assert.assertEquals(placedObjects.size(), 0,
                    "Error! Object has not been removed from "
                            + "Objects in Document section.");
            CSLogger.info("object remove test case completed");
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method marks checkBox of the page template assignment in Document
     * section.
     * 
     * @param objectName String Object containing the name of the object.
     */
    private void selectCheckboxOfObjectInDocumentSection(String objectName) {
        WebElement chkbxObject = browserDriver.findElement(By.xpath(
                "//td[contains(text(),'" + objectName + "')]/../td[1]/input"));
        chkbxObject.click();
        CSLogger.info("Selected checkbox of object in document section");
    }

    /**
     * This method handles the case where the first is expands on its own in the
     * Object section.
     */
    private void handleAutoExpandingOfNodeTree() {
        WebElement nodeElement;
        nodeElement = browserDriver.findElement(By
                .xpath("(//table[@class='treeline'])[1]/tbody/tr/td/span/img"));
        if (nodeElement.getAttribute("src").endsWith("TreeMinus.png")) {
            nodeElement.click();
            CSLogger.info("Clicked on the first tree node to close the tree.");
        }
    }

    /**
     * This method closes the parent tree node.
     * 
     * @param nodes String Array Object containing the array of nodes in object
     *            path.
     */
    private void closeNodeTree(String[] nodes) {
        WebElement nodeElement;
        nodeElement = browserDriver.findElement(By.xpath("//span[text()=' "
                + nodes[0].trim() + "']/../../../preceding-sibling::td/span"));
        nodeElement.click();
    }

    /**
     * This method perform click operation in the icon of Object in the right
     * side toolbar.
     */
    private void clickOnObjectsIcon() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmcsSideBarBody()));
        WebElement objectIcon = browserDriver
                .findElement(By.xpath("//a[contains(text(),'Objects')]/.."));
        objectIcon.click();
        CSLogger.info("Clicked on the object icon on the right toolbar.");
    }

    /**
     * This method expands each node turn-by-turn in the object path.
     * 
     * @param nodes String Array Object containing the array of nodes in object
     *            path.
     */
    private void traverseToObject(String[] nodes) {
        WebElement nodeElement;
        for (int index = 0; index < nodes.length - 1; index++) {
            nodeElement = browserDriver.findElement(By.xpath(
                    "//span[text()=' " + nodes[index].trim() + "']/../.."));
            nodeElement.click();
            if (index == nodes.length - 2) {
                nodeElement.click();
            }
            CSUtility.tempMethodForThreadSleep(2000);
        }
    }

    /**
     * This method verifies the object placement status.
     * 
     * @param objectName String Object containing the name of the object.
     */
    private void verifyObjectPlacementStatus(String objectName,
            String pageNumber) {
        WebElement objectPosition = browserDriver
                .findElement(By.xpath("//td[text()='Page 00" + pageNumber
                        + "']/../following-sibling::tr[1]/td[3]"));
        Assert.assertEquals(objectPosition.getText().trim(), objectName.trim());
        WebElement objectPositionStatus = browserDriver.findElement(By.xpath(
                "//td[contains(text(),'" + objectName + "')]/../td[2]/img"));
        Assert.assertTrue(objectPositionStatus.getAttribute("src")
                .endsWith("unplaced_positioned.png"));
    }

    /**
     * This method performs drag and drop operation onto the whiteboard.
     * 
     * @param actions Action Object
     */
    private void dragDropObjectInWhiteBoard(Actions actions) {
        WebElement pageForItem = browserDriver.findElement(By.xpath(
                "//img[@id='preview_001']/following-sibling::div[1]/div[3]"));
        actions.moveToElement(pageForItem).release(pageForItem).build()
                .perform();
        CSLogger.info("Drag and drop object in WhiteBoard");
    }

    /**
     * This method select and holds the object.
     * 
     * @param actions Action Object
     * @param objectName String Object containing the name of object.
     */
    private void holdSelectObjectInDocumentSection(Actions actions,
            String objectName) {
        WebElement dragElement;
        dragElement = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + objectName + "')]/.."));
        actions.clickAndHold(dragElement).build().perform();
        CSLogger.info("Hold select object in document section");
    }

    /**
     * This method perform drag and drop operation of object in the Document
     * section.
     * 
     * @param actions Action Object
     */
    private void dragDropObjectInDocumentSection(Actions actions,
            String pageNumber) {
        WebElement dropElement = browserDriver.findElement(
                By.xpath("//input[@name='pages']/parent::td[text()='Page 00"
                        + pageNumber + "']/.."));
        actions.moveToElement(dropElement).release(dropElement).build()
                .perform();
        CSLogger.info(
                "Drag and drop operation of object in the Document section.");
    }

    /**
     * This method perform select and hold of the object.
     * 
     * @param actions Action Object
     * @param objectName String Object containing the name of object.
     */
    private void holdSelectObject(Actions actions, String objectName) {
        WebElement dragElement = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + objectName + "')]/.."));
        actions.clickAndHold(dragElement).build().perform();
        CSLogger.info("Select and hold the placement object.");
    }

    /**
     * This method traverses to the frmbottom frame.
     */
    private void traverseToBottomFrame() {
        CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                browserDriver);
        traverseToToolbarFrameForObject();
        switchToSplitAreaFrameBottom();
    }

    /**
     * This method traverse to the splitAreaFrame
     */
    private void switchToSplitAreaFrameBottom() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmShowingStatusOfRunningActiveScript()));
        CSLogger.info("switched to the frmbottom frame.");
    }

    /**
     * Reads the excel sheet and returns the data for Flex
     * 
     * @return String Array Object containing the data from sheet
     */
    @DataProvider(name = "moveSketchesTestData")
    public Object[] createClassTestDataForSketchs() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                moveSketchesToAnotherPageSheet);
    }

    /**
     * This method initializes all the resources required to drive test case
     * 
     */
    @BeforeClass
    public void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 45);
        csPortalHeader = new CSPortalHeader(browserDriver);
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csPopupDiv = new CSPopupDivMam(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        csGuiToolbarHorizontalMam = new CSGuiToolbarHorizontalMam(
                browserDriver);
        pageTemplateMap = new HashMap<>();
        popupDivSettings = new CSPopupDivSettings(browserDriver);
        intializePageTemplateMap();
    }

    /**
     * This method maps the td tag id with the page template name
     */
    private void intializePageTemplateMap() {
        pageTemplateMap.put("1x2", "1");
        pageTemplateMap.put("3x4", "2");
        pageTemplateMap.put("Businesscard", "3");
        pageTemplateMap.put("1x4", "5");
        pageTemplateMap.put("2+1", "6");
        pageTemplateMap.put("1+3", "8");
        pageTemplateMap.put("1+2", "9");
        pageTemplateMap.put("1x1", "10");
        pageTemplateMap.put("Flowarea", "11");
    }
}
