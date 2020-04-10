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
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioChannelsNode;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.settings.ActiveJobsPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.SettingsPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This Class perform the test cases related to the page template assignment to
 * whiteboard, object placement and object removal.
 * 
 * @author CSAutomation Team
 *
 */
public class WhiteBoardPlacementTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private MamStudioVolumesNode      mamStudioVolumesNode;
    private CSPopupDivMam             csPopupDiv;
    private FrameLocators             locator;
    private ActiveJobsPage            activeJobsPage;
    private IVolumePopup              demoVolumePopup;
    private CSGuiToolbarHorizontalMam csGuiToolbarHorizontalMam;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontalInstance;
    private SettingsPage              settingPage;
    private PimStudioProductsNodePage pimStudioProductsNode;
    private PimStudioChannelsNode     pimStudioChannelsNodeInstance;
    private Actions                   action;
    private CSPopupDivSettings        popupDivSettings;
    private HashMap<String, String>   pageTemplateMap;
    private HashMap<String, String>   objectTemplateMap;
    private String                    productPlacementSheet = "WhiteBoardProductPlacement";
    private String                    channelPlacementSheet = "WhiteBoardChannelPlacement";
    private String                    flexPlacementSheet    = "WhiteBoardFlexPlacement";
    private String                    deleteAlert           = "Do you really "
            + "want to remove the selected products?";
    private String                    productConnector      = "PIM Products Connector";
    private String                    channelConnector      = "PIM Channels Connector";
    private String                    flexConnector         = "Flex Tables Comet Connector";

    /**
     * This method test the assignment of page Templates to WBD files, placement
     * of product and removal
     * 
     * @param filePath
     * @param objectPath
     * @param pageTemplateName
     * @param objectPageTemplateLabel
     */
    @Test(dataProvider = "ProductPlacementData", priority = 1)
    public void testWhiteBoardProductPlacement(String shareName,
            String folderPath, String filePath, String pageTemplateName,
            String objectPath, String objectPageTemplateLabel) {
        prerequisiteToCreateProductShare(shareName, folderPath);
        pageTemplatesTestCase(filePath, pageTemplateName);
        objectPlacementTestCase(objectPath, objectPageTemplateLabel);
        CSLogger.info("Test Case WhiteBoard Product Placement is completed.");
    }

    /**
     * This method test the assignment of page Templates to WBD files, placement
     * of channel and removal
     * 
     * @param shareName
     * @param folderPath
     * @param filePath
     * @param objectPath
     * @param objectPageTemplateLabel
     * @param pageTemplateName
     */
    @Test(dataProvider = "ChannelPlacementData", priority = 2)
    public void testWhiteBoardChannelPlacement(String shareName,
            String folderPath, String filePath, String pageTemplateName,
            String objectPath, String objectPageTemplateLabel) {
        try {
            prerequisiteToCreateChannelsShare(shareName, folderPath);
            pageTemplatesTestCase(filePath, pageTemplateName);
            objectPlacementTestCase(objectPath, objectPageTemplateLabel);
            CSLogger.info(
                    "Test Case WhiteBoard Channel Placement is completed.");
        } catch (Exception e) {
            CSLogger.debug("Test case failed");
            Assert.fail("Test case failed");
        }
    }

    /**
     * This method test the assignment of page Templates to WBD files, placement
     * of flexTable and removal
     * 
     * @param filePath
     * @param objectPath
     * @param pageTemplateName
     * @param objectPageTemplateLabel
     */
    @Test(dataProvider = "FlexTablePlacementData", priority = 3)
    public void testWhiteBoardFlexTablePlacement(String shareName,
            String filePath, String pageTemplateName, String objectPath,
            String objectPageTemplateLabel) {
        try {
            prerequisiteToCreateFlexShare(shareName);
            pageTemplatesTestCase(filePath, pageTemplateName);
            objectPlacementTestCase(objectPath, objectPageTemplateLabel);
            CSLogger.info(
                    "Test Case WhiteBoard Flex Table Placement is completed.");
        } catch (Exception e) {
            CSLogger.debug("Test case failed", e);
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
            /* Sleep of 5 min to wait for synchronization of wdb file */
            CSUtility.tempMethodForThreadSleep(180000);
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
            dragDropPageTemplateOnWhiteBoard(actions);
            refreshForPageTemplateAssignment();
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            traverseToToolbarFrameForTemplate();
            templateElement = browserDriver
                    .findElement(By.xpath(templateXpath));
            actions.moveToElement(templateElement).build().perform();
            traverseToCenterFrame();
            verifyAssignedPageTemplate(pageTemplateName);
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method create flex share
     * 
     * @param shareName String object contains share name
     */
    private void prerequisiteToCreateFlexShare(String shareName) {
        traverseToEditWindowPrintShares();
        settingPage.sendTxtToWebElement(waitForReload,
                settingPage.getTxtSharePointName(), shareName);
        selectElementByVisibleText(settingPage.getDrpDwnConnectorPlugin(),
                flexConnector);
        CSUtility.tempMethodForThreadSleep(2000);
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxPlanningView());
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxPlaceHolder());
        WebElement btnSelectAll = browserDriver.findElement(
                By.xpath("//td[@class='EditbuilderCenter']//input[@class='"
                        + "EditbuilderMultiSelectButton']"));
        CSUtility.waitForVisibilityOfElement(waitForReload, btnSelectAll);
        btnSelectAll.click();
        CSLogger.info("Click on button select all");
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method create channel share
     * 
     * @param shareName String object contains share name
     * @param folderPath String object contains folder path
     */
    private void prerequisiteToCreateChannelsShare(String shareName,
            String folderPath) {
        WebElement nodefolder = null;
        String[] path = folderPath.split("//");
        createChannel(folderPath);
        traverseToEditWindowPrintShares();
        settingPage.sendTxtToWebElement(waitForReload,
                settingPage.getTxtSharePointName(), shareName);
        selectElementByVisibleText(settingPage.getDrpDwnConnectorPlugin(),
                channelConnector);
        CSUtility.tempMethodForThreadSleep(2000);
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxPlanningView());
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxProductsPool());
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxPublicationPool());
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxPlaceHolder());
        WebElement dataFilterParent = browserDriver.findElement(By.xpath(
                "//div[@id='title_Document Filter_sections::Data Filter']/.."));
        CSUtility.waitForVisibilityOfElement(waitForReload, dataFilterParent);
        if (dataFilterParent.getAttribute("class").contains("closed")) {
            browserDriver.findElement(By.xpath(
                    "//div[@id='title_Document Filter_sections::Data Filter']"))
                    .click();
        }
        settingPage.clkWebElement(waitForReload,
                settingPage.getBtnAddProducts());
        CSLogger.info("Click on button add products");
        TraverseSelectionDialogFrames.traverseToDataSelectionDialogChannelTree(
                waitForReload, browserDriver);
        nodefolder = browserDriver
                .findElement(By.linkText(path[path.length - 1]));
        CSUtility.waitForVisibilityOfElement(waitForReload, nodefolder);
        action.doubleClick(nodefolder).build().perform();
        CSLogger.info("Double click on product " + path[path.length - 1]);
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * This method create channel
     * 
     * @param folderPath String object contains folder path
     */
    private void createChannel(String folderPath) {
        WebElement nodefolder = null;
        String[] path = folderPath.split("//");
        csPortalHeader.clkBtnProducts(waitForReload);
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        pimStudioProductsNode.clickOnNodeProducts();
        for (int index = 0; index < path.length - 1; index++) {
            nodefolder = browserDriver.findElement(By.linkText(path[index]));
            CSUtility.waitForVisibilityOfElement(waitForReload, nodefolder);
            nodefolder.click();
            CSUtility.tempMethodForThreadSleep(1000);
        }
        WebElement channelFolder = browserDriver
                .findElement(By.linkText(path[path.length - 1]));
        action.dragAndDrop(channelFolder,
                pimStudioChannelsNodeInstance.getBtnPimChannelsNode()).build()
                .perform();
        CSLogger.info("Drag and Drop Product to channels");
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        settingPage.sendTxtToWebElement(waitForReload,
                pimStudioChannelsNodeInstance.getTxtChannelLabel(),
                path[path.length - 1]);
        selectElementByVisibleText(
                pimStudioChannelsNodeInstance.getDrpDwnCreateRecursively(),
                "Copy recursively");
        selectElementByVisibleText(
                pimStudioChannelsNodeInstance.getDrpDwnSelectionType(),
                "Selected element and sub elements");
        csPopupDiv.askBoxWindowOperationMamStudio(waitForReload, true,
                browserDriver);
        CSLogger.info("Channel is created");
    }

    /**
     * This method create product share
     * 
     * @param shareName String object contains share name
     * @param folderPath String object contains folder path
     */
    private void prerequisiteToCreateProductShare(String shareName,
            String folderPath) {
        WebElement nodefolder = null;
        String[] path = folderPath.split("//");
        traverseToEditWindowPrintShares();
        settingPage.sendTxtToWebElement(waitForReload,
                settingPage.getTxtSharePointName(), shareName);
        selectElementByVisibleText(settingPage.getDrpDwnConnectorPlugin(),
                productConnector);
        CSUtility.tempMethodForThreadSleep(2000);
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxPlanningView());
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxProductsPool());
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxPublicationPool());
        settingPage.clkToWebElementOfChkBox(waitForReload,
                settingPage.getChkBoxPlaceHolder());
        WebElement dataFilterParent = browserDriver.findElement(By.xpath(
                "//div[@id='title_Document Filter_sections::Data Filter']/.."));
        CSUtility.waitForVisibilityOfElement(waitForReload, dataFilterParent);
        if (dataFilterParent.getAttribute("class").contains("closed")) {
            browserDriver.findElement(By.xpath(
                    "//div[@id='title_Document Filter_sections::Data Filter']"))
                    .click();
        }
        settingPage.clkWebElement(waitForReload,
                settingPage.getBtnAddProducts());
        CSLogger.info("Click on button add products");
        TraverseSelectionDialogFrames.traverseTillProductsFolderRightPane(
                waitForReload, browserDriver);
        for (int index = 0; index < path.length; index++) {
            nodefolder = browserDriver.findElement(By.linkText(path[index]));
            CSUtility.waitForVisibilityOfElement(waitForReload, nodefolder);
            if (index == path.length - 1) {
                action.doubleClick(nodefolder).build().perform();
                CSLogger.info("Double click on product " + path[index]);
            } else {
                nodefolder.click();
            }
            CSUtility.tempMethodForThreadSleep(1000);
        }
        csPopupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
        CSLogger.info("product share successfully created");
    }

    /**
     * This method traverse to Edit window of print shares
     */
    private void traverseToEditWindowPrintShares() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traversetoSystemPreferences(waitForReload,
                browserDriver, locator);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                activeJobsPage.getSystemPreferences());
        activeJobsPage.clkWebElement(activeJobsPage.getSystemPreferences());
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        settingPage.clkWebElement(waitForReload, settingPage.getNodeShares());
        CSLogger.info("Click on node Shares");
        settingPage.clkWebElement(waitForReload,
                settingPage.getNodePrintShares());
        CSLogger.info("Click on node Print Shares");
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
        csGuiToolbarHorizontalInstance
                .clkBtnCSGuiToolbarCreateNew(waitForReload);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
    }

    /**
     * This method select element from drop down
     * 
     * @param element WebElement object contains drpdwn element
     * @param text String object contains text to select
     */
    private void selectElementByVisibleText(WebElement element, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        Select drpDwn = new Select(element);
        drpDwn.selectByVisibleText(text);
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
     */
    private void dragDropPageTemplateOnWhiteBoard(Actions actions) {
        WebElement pageForTemplate = browserDriver
                .findElement(By.xpath("//img[@id='preview_001']"));
        actions.moveToElement(pageForTemplate).release(pageForTemplate).build()
                .perform();
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
     * This method test the placement of object i.e product/channel/flex.
     * 
     * @param objectPath String object containing path of object
     * @param objectPageTemplateLabel String object containing object page
     *            template name
     */
    public void objectPlacementTestCase(String objectPath,
            String objectPageTemplateLabel) {
        try {
            objectPageTemplateLabel = objectPageTemplateLabel.trim();
            objectPageTemplateLabel = objectPageTemplateLabel.replace("//",
                    "/");
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
            dragDropObjectInDocumentSection(actions);
            CSUtility.tempMethodForThreadSleep(2000);
            holdSelectObjectInDocumentSection(actions, objectName);
            traverseToCenterFrame();
            dragDropObjectInWhiteBoard(actions);
            traverseToBottomFrame();
            verifyObjectPlacementStatus(objectName);
            selectCheckboxOfObjectInDocumentSection(objectName);
            selectCheckboxForObjectTemplateInDocumentsSection();
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmCsPortalWindowContent()));
            selectObjectTemplate(objectPageTemplateLabel);
            clickOkayForObjectTemplate();
            traverseToBottomFrame();
            verifyObjectTemplate(objectPageTemplateLabel, objectName);
            objectRemoveTestCase(objectName);
        } catch (Exception e) {
            CSLogger.error("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method verifies the assigned object page template.
     * 
     * @param objectPageTemplateLabel String Object containing name of object
     *            page template
     * @param objectName String Object containing the name of the object
     */
    private void verifyObjectTemplate(String objectPageTemplateLabel,
            String objectName) {
        WebElement assignedObjectTemplate = browserDriver
                .findElement(By.xpath("//td[contains(text(),'" + objectName
                        + "')]/" + "following-sibling::td[1]"));
        Assert.assertEquals(assignedObjectTemplate.getText(),
                objectPageTemplateLabel);
    }

    /**
     * This method perform click operation on the okay button.
     */
    private void clickOkayForObjectTemplate() {
        WebElement okayButton = browserDriver.findElement(
                By.xpath("//input[@id='CSGUI_MODALDIALOG_OKBUTTON']"));
        okayButton.click();
        CSLogger.info("Clicked on the okay button.");
    }

    /**
     * This method perform select and hold operation on object page template.
     * 
     * @param objectPageTemplateLabel String Object containing the name of
     *            object page template.
     */
    private void selectObjectTemplate(String objectPageTemplateLabel) {
        WebElement objectTemplate = browserDriver.findElement(By.xpath(
                "//tr[@id='" + objectTemplateMap.get(objectPageTemplateLabel)
                        + "' and @class='CSGuiThumbListRow']"));
        objectTemplate.click();
        CSLogger.info("Selected the Object page template.");
    }

    /**
     * This method marks the checkbox of the object in Document section.
     */
    private void selectCheckboxForObjectTemplateInDocumentsSection() {
        WebElement iconObjectTemplate = browserDriver.findElement(By.xpath(
                "//img[contains(@src,'cometplacementstate.changed.png')]/.."));
        iconObjectTemplate.click();
        CSLogger.info(
                "Selected checkbox for object template in documents section");
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
     * This method verifies the object placement status.
     * 
     * @param objectName String Object containing the name of the object.
     */
    private void verifyObjectPlacementStatus(String objectName) {
        WebElement objectPosition = browserDriver.findElement(By.xpath(
                "//td[contains(text(),'" + objectName + "')]/../td[2]/img"));
        Assert.assertTrue(objectPosition.getAttribute("src")
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
    private void dragDropObjectInDocumentSection(Actions actions) {
        WebElement dropElement = browserDriver.findElement(By.xpath(
                "//input[@name='pages']/parent::td[text()='Page 001']/.."));
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
            CSLogger.info("click number = " + index);
            if (index == nodes.length - 2) {
                nodeElement.click();
            }
            CSUtility.tempMethodForThreadSleep(2000);
        }
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
        } catch (Exception e) {
            CSLogger.error("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
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
     * This method traverse to the splitAreaFrame
     */
    private void switchToSplitAreaFrameBottom() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmShowingStatusOfRunningActiveScript()));
        CSLogger.info("switched to the frmbottom frame.");
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
     * This method traverse to the Pop-up menu div.
     */
    private void traverseToPopupMenu() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(demoVolumePopup.getCsPopupDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        CSLogger.info("traversed to CS pop up menu, to click on refresh");
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
     * Reads the excel sheet and returns the data for Product
     * 
     * @return String Array Object containing the data from sheet
     */
    @DataProvider(name = "ProductPlacementData")
    public Object[] createClassTestDataForProduct() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                productPlacementSheet);
    }

    /**
     * Reads the excel sheet and returns the data for Channel
     * 
     * @return String Array Object containing the data from sheet
     */
    @DataProvider(name = "ChannelPlacementData")
    public Object[] createClassTestDataForChannel() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                channelPlacementSheet);
    }

    /**
     * Reads the excel sheet and returns the data for Flex
     * 
     * @return String Array Object containing the data from sheet
     */
    @DataProvider(name = "FlexTablePlacementData")
    public Object[] createClassTestDataForFlex() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                flexPlacementSheet);
    }

    /**
     * This method initializes all the resources required to drive test case
     * 
     */
    @BeforeClass
    public void initializeResources() {
        waitForReload = new WebDriverWait(browserDriver, 45);
        csPortalHeader = new CSPortalHeader(browserDriver);
        settingPage = new SettingsPage(browserDriver);
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csPopupDiv = new CSPopupDivMam(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        demoVolumePopup = CSPopupDivMam.getCSPopupDivLocators(browserDriver);
        csGuiToolbarHorizontalMam = new CSGuiToolbarHorizontalMam(
                browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        activeJobsPage = SettingsLeftSectionMenubar
                .getActiveJobsNode(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        pimStudioChannelsNodeInstance = new PimStudioChannelsNode(
                browserDriver);
        action = new Actions(browserDriver);
        popupDivSettings = new CSPopupDivSettings(browserDriver);
        pageTemplateMap = new HashMap<>();
        intializePageTemplateMap();
        objectTemplateMap = new HashMap<>();
        intializeObjectTemplateMap();
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

    /**
     * This method maps the td tag id with the object page template name
     */
    private void intializeObjectTemplateMap() {
        objectTemplateMap.put("Product 1/4", "1");
        objectTemplateMap.put("Product 1/1", "4");
        objectTemplateMap.put("Product 1/2", "6");
        objectTemplateMap.put("Text and Image 1/2", "7");
        objectTemplateMap.put("Product 1/6", "8");
        objectTemplateMap.put("Product 1/1 Table", "10");
        objectTemplateMap.put("Textframe", "11");
        objectTemplateMap.put("Imageframe", "12");
        objectTemplateMap.put("Table", "14");
    }

}
