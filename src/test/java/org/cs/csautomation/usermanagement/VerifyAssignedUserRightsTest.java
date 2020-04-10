/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.usermanagement;

import java.util.List;
import org.cs.csautomation.cs.pom.CSGuiDialogContentId;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.IProductPopup;
import org.cs.csautomation.cs.pom.LoginPage;
import org.cs.csautomation.cs.pom.PimStudioProductsNodePage;
import org.cs.csautomation.cs.portal.ChoosePortalPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
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
import org.testng.asserts.SoftAssert;

/**
 * This method verifies assigned user rights to the created product in newly
 * created user
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyAssignedUserRightsTest extends AbstractTest {

    private CSPortalHeader            csPortalHeader;
    private WebDriverWait             waitForReload;
    private FrameLocators             locator;
    private String                    verifyAssignedUserRightsSheet = "VerifyAssignedUserRightsSheet";
    private PimStudioProductsNodePage pimStudioProductsNode;
    private IProductPopup             productPopUp;
    private CSGuiToolbarHorizontal    csGuiToolbarHorizontal;
    private CSGuiDialogContentId      csGuiDialogContentId;
    private Actions                   actions;
    private LoginPage                 loginPage;
    private ChoosePortalPage          choosePortalPage;
    private SoftAssert                softAssert;

    /**
     * This method verifies assigned user rights
     * 
     * @param productName contains product name
     * @param accessRightName contains access right name
     * @param username contains username
     * @param password contains password
     * @param portalName contains portal name
     * @param createNewPortalOption contains create new portal option string
     * @param language contains language
     * @param tabName contains tab name to be created in newly created portal
     */
    @Test(priority = 1, dataProvider = "verifyAssignedUserRights")
    public void tesstVerifyAssignedUserRigths(String productName,
            String accessRightName, String username, String password,
            String portalName, String createNewPortalOption, String language,
            String tabName, String adminUsername, String adminPassword) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnProducts(waitForReload);
            createTestProductToCheckAccessRight(productName);
            selectAccessRightForProduct(productName, accessRightName);
            logoutFromApplication();
            loginAsCreatedUser(username, password, language);
            createNewPortal(portalName, createNewPortalOption);
            clkCreateNewTab(portalName);
            addTabInCreatedPortal(tabName);
            verifyInsertedColumnsInCreatedTab();
            insertWidget();
            handleWidgetOptions();
            navigateToCreatedProduct(productName);
            verifyAbsenceOfProduct(productName);
            logoutFromApplication();
            loginAsAdmin(language, adminUsername, adminPassword);
        } catch (Exception e) {
            CSLogger.info("Test case failed" + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method creates test product
     * 
     * @param productName contains product name
     */
    private void createTestProductToCheckAccessRight(String productName) {
        rightClkProducts();
        createProduct(productName);
    }

    /**
     * This method performs right click on products
     */
    private void rightClkProducts() {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        CSLogger.info("Clicked on Products Folder");
        CSUtility.rightClickTreeNode(waitForReload,
                pimStudioProductsNode.getBtnPimProductsNode(), browserDriver);
        CSLogger.info("Right clicked on Products folder ");
    }

    /**
     * This method creates new product
     * 
     * @param productName contains product name
     */
    private void createProduct(String productName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getProductPopUpDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                pimStudioProductsNode.getproductPopUpFrame()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(pimStudioProductsNode.getClkCreateNew()));
        pimStudioProductsNode.getClkCreateNew().click();
        CSLogger.info("Clicked on Create new option");
        CSUtility.switchToDefaultFrame(browserDriver);
        clkOkToCreateProduct(productName);
    }

    /**
     * This method clicks on OK to create product
     * 
     * @param productName contains the product name
     */
    private void clkOkToCreateProduct(String productName) {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmDataSelectionDialog()));
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(pimStudioProductsNode.getUserElement()));
        CSUtility.tempMethodForThreadSleep(1000);
        pimStudioProductsNode.getUserElement().sendKeys(productName);
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getBtnCsGuiModalDialogOkButton()));
        productPopUp.getBtnCsGuiModalDialogOkButton().click();
        CSLogger.info("Clicked on OK button ");
    }

    /**
     * This method selects access right for product
     * 
     * @param productName contains product name
     * @param accessRightName contains access right name
     */
    private void selectAccessRightForProduct(String productName,
            String accessRightName) {
        openProductInEditMode(productName);
        clkToolsInMenuBar();
        clkAccessRight();
        editLink();
        selectRightFromAccessLevelDialog(accessRightName);
    }

    /**
     * This method opens product in edit mode
     * 
     * @param productName contains product name
     */
    private void openProductInEditMode(String productName) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(pimStudioProductsNode.getBtnPimProductsNode()));
        pimStudioProductsNode.getBtnPimProductsNode().click();
        WebElement namOfProduct = browserDriver
                .findElement(By.linkText(productName));
        actions.doubleClick(namOfProduct).build().perform();
        CSLogger.info("Double clicked on created product ");
    }

    /**
     * This method clicks on Tools in menu bar
     */
    private void clkToolsInMenuBar() {
        clkShowMoreOptions();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getCsGuiPopupTools()));
        productPopUp.getCsGuiPopupTools().click();
        CSLogger.info("Clicked on Tools option ");
    }

    /**
     * This method clicks on Show more options
     */
    private void clkShowMoreOptions() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiToolbarHorizontal.getBtnShowMoreOption()));
        csGuiToolbarHorizontal.getBtnShowMoreOption().click();
        CSLogger.info("Clicked on show more  options in Menu bar ");
    }

    /**
     * This method clicks on access right
     */
    private void clkAccessRight() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getFrmCsPopupDivSub()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivSubFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getCsGuiPopupAccessRight()));
        productPopUp.getCsGuiPopupAccessRight().click();
        CSLogger.info("Clicked on Access Right");
    }

    /**
     * This method clicks on edit link
     */
    private void editLink() {
        traverseToEdit();
        clkEdit();
    }

    /**
     * This method traverses upto edit
     */
    private void traverseToEdit() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame190()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalBodyFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method clicks on edit
     */
    private void clkEdit() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmItemControlProduct()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentId.getEditLink()));
        csGuiDialogContentId.getEditLink().click();
        CSLogger.info("Clicked on Edit ");
    }

    /**
     * This method selects access right from access level dialogue
     * 
     * @param accessRightName contains access right name
     */
    private void selectRightFromAccessLevelDialog(String accessRightName) {
        WebElement accessLevel = traverseToCreatedAccessLevel(accessRightName);
        doubleClkElement(accessLevel);
        CSLogger.info("Double clicked on created access level ");
    }

    /**
     * This method performs double clicks and clicks on accept button
     * 
     * @param accessLevel contains access level as webelement
     */
    private void doubleClkElement(WebElement accessLevel) {
        actions.doubleClick(accessLevel).build().perform();
        clkAcceptButton();
    }

    /**
     * This method traverses to created access level
     * 
     * @param accessRightName contains access right name
     * @return accessRight
     */
    private WebElement traverseToCreatedAccessLevel(String accessRightName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmRubicTree()));
        WebElement accessRight = browserDriver
                .findElement(By.linkText(accessRightName));
        waitForReload.until(ExpectedConditions.visibilityOf(accessRight));
        return accessRight;
    }

    /**
     * This method clicks on accept button
     */
    private void clkAcceptButton() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(csGuiDialogContentId.getBtnAccept()));
        csGuiDialogContentId.getBtnAccept().click();
        CSLogger.info("Clicked on Accept button after assining access rights ");
        chkInProduct();
    }

    /**
     * This method checks in product
     */
    private void chkInProduct() {
        CSUtility.traverseToSettingsConfiguration(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions.visibilityOf(
                csGuiToolbarHorizontal.getBtnCSGuiToolbarCheckIn()));
        csGuiToolbarHorizontal.getBtnCSGuiToolbarCheckIn().click();
        CSLogger.info("Clicked on Checkin button of product");
    }

    /**
     * This method performs log out from admin
     */
    private void logoutFromApplication() {
        clkPortalLinkOptions();
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(productPopUp.getCsGuiPopupLogout()));
        productPopUp.getCsGuiPopupLogout().click();
        CSLogger.info("Logged out successfully ");
    }

    /**
     * This method clicks on Portal Link options
     */
    private void clkPortalLinkOptions() {
        csPortalHeader.clkBtnCsPortalLinkOptions(waitForReload);
        CSLogger.info("Clicked on Portal link options");
    }

    /**
     * This method logs in to application with newly created user
     * 
     * @param username contains username
     * @param password contains password
     * @param language contains language
     */
    private void loginAsCreatedUser(String username, String password,
            String language) {
        chooseLanguage(language);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        CSLogger.info("Entered Username and password");
    }

    /**
     * This method creates new portal
     * 
     * @param portalName contains the portal name
     * @param createNewPortalOption contains instance of create new portal
     *            option
     */
    private void createNewPortal(String portalName,
            String createNewPortalOption) {
        loginPage.clkLoginButton();
        CSUtility.tempMethodForThreadSleep(1000);
        acceptTermsOfUse();
        clkCreateNewPortal(createNewPortalOption);
    }

    /**
     * This method clicks on Terms of use while loggin in with new user
     */
    private void acceptTermsOfUse() {
        choosePortalPage.clkBtnAccept();
    }

    /**
     * This method clicks on create new portal option
     * 
     * @param createNewPortalOptionn contains instance of create new portal
     *            option
     */
    private void clkCreateNewPortal(String createNewPortalOptionn) {
        Select newPortal = new Select(browserDriver
                .findElement(By.xpath("//select[@id='CSPortalLoginSelect']")));
        newPortal.selectByVisibleText(createNewPortalOptionn);
        CSLogger.info("Create New Portal option selected");
        clkCreate();
    }

    /**
     * This method clicks on create
     */
    private void clkCreate() {
        loginPage.clkBtnCreate();
        CSLogger.info("Clicked on Create Button on Login Page");
    }

    /**
     * This method clicks on create new tab
     * 
     * @param portalName contains the portal name to be created
     */
    private void clkCreateNewTab(String portalName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        choosePortalPage.clkCreateNewTab();
        CSLogger.info("Clicked on Create new tab");
        enterTitle(portalName);
    }

    /**
     * This method chooses language while logging in
     * 
     * @param language contains language to be set
     */
    private void chooseLanguage(String language) {
        Select loginLanguage = new Select(browserDriver
                .findElement(By.xpath("//select[@id='loginLanguage']")));
        loginLanguage.selectByVisibleText(language);
        CSLogger.info(language + " language has been chosen");

    }

    /**
     * This method enters portal name to be created
     * 
     * @param portalName contains the name of the portal
     */
    private void enterTitle(String portalName) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getTxtTitle()));
        choosePortalPage.getTxtTitle().clear();
        choosePortalPage.getTxtTitle().sendKeys(portalName);
        clkInsert();
    }

    /**
     * This method clicks on Insert button
     */
    private void clkInsert() {
        choosePortalPage.clkBtnInsert(waitForReload);
        CSLogger.info("Clicked on Insert to create new portal");
    }

    /**
     * This method adds tab in created portal
     * 
     * @param tabName contains name of tab
     */
    private void addTabInCreatedPortal(String tabName) {
        try {
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getTopFrameInFileCategoryLeftSection()));
            CSUtility.tempMethodForThreadSleep(2000);
            choosePortalPage.clkBtnAddTab(waitForReload);
            clkCreateNew(tabName);
        } catch (Exception e) {
            CSLogger.error("Could not click on add tab" + e);
            softAssert.fail("Could not click on add tab button" + e);
        }
    }

    /**
     * This method clicks on Create new menu
     * 
     * @param tabName contains the tab name
     */
    private void clkCreateNew(String tabName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getCreateNewInTabTemplate()));
        choosePortalPage.getCreateNewInTabTemplate().click();
        CSLogger.info(
                "Clicked on create new to add tab in newly created portal");
        addTabName(tabName);
    }

    /**
     * This method adds tabname to newly created portal
     * 
     * @param tabName contains name of tab to be added
     */
    private void addTabName(String tabName) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getTxtTitle()));
        choosePortalPage.getTxtTitle().clear();
        choosePortalPage.getTxtTitle().sendKeys(tabName);
        choosePortalPage.clkBtnInsert(waitForReload);
        CSLogger.info("Tab inserted successfully in new portal ");
    }

    /**
     * This method verifies inserted columns in created tab
     */
    private void verifyInsertedColumnsInCreatedTab() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            CSUtility.tempMethodForThreadSleep(5000);
            waitForReload.until(ExpectedConditions
                    .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
            List<WebElement> list = browserDriver.findElements(By.xpath(
                    "//table[contains(@class,'CSPortalMain')]/tbody/tr/td[2]/div/div/div/div/span"));
            if (list.size() >= 3) {
                CSLogger.info("Tab created in new portal");
            } else {
                CSLogger.info("could not verify tab ");
            }
        } catch (Exception e) {
            CSLogger.info("Verification failed" + e);
            Assert.fail("Could not verify created tab", e);
        }
    }

    /**
     * This method calls all methods in sequence to create widget
     */
    private void insertWidget() {
        clkPlusToAddWidget();
        clkBySuite();
        clkCore();
        clkCorePortalStudio();
        clkInsert();
        verifyConfigurationMessageForInsertingWidget();
    }

    /**
     * This method clicks on BuSuite menu option
     */
    private void clkBySuite() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsPortalWindowFrame()));
        choosePortalPage.clkBysuiteMenu();
        CSLogger.info("Clicked on By Suite menu");
    }

    /**
     * This method clicks on Core menu
     */
    private void clkCore() {
        choosePortalPage.clkCoreSubMenu();
        CSLogger.info("Clicked on Core Submenu");
    }

    /**
     * This method clicks on core portal studio
     */
    private void clkCorePortalStudio() {
        choosePortalPage.clkCorePortalStudio();
        CSLogger.info("Clicked on Core Portal Studio");
    }

    /**
     * This method clicks on plus icon to add widget
     */
    private void clkPlusToAddWidget() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getBtnPlusToAddWidget()));
        choosePortalPage.getBtnPlusToAddWidget().click();
        CSLogger.info(
                "Clicked on Plus button to add Widget to newly created tab in new portal");
    }

    /**
     * This method traverses to widget frame
     */
    private void traverseToWidgetFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmPortalWidgetContentFrame()));
    }

    /**
     * This method verifies if Configuration message displays while inserting
     * widget
     */
    private void verifyConfigurationMessageForInsertingWidget() {
        traverseToWidgetFrame();
        WebElement configurationMessage = waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getConfigurationMessage()));
        String message = configurationMessage.getText();
        if (message.equals("Configuration Required")) {
            CSLogger.info(
                    "Configuration Required message successfully verified");
        } else {
            CSLogger.info("Verification failed.");
            Assert.fail("Verification failed for configuration message.");
        }
    }

    /**
     * Sequence of methods in this method handles all operations in widget
     * window
     */
    private void handleWidgetOptions() {
        maximizeWidgetWindow();
        clkWidgetOptions();
        clkSecuredOptions();
        addPimStudioFromWidgetOptions();
    }

    /**
     * This method clicks on Widget options in CSPortal window
     */
    private void clkWidgetOptions() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getPortalWidgetDragArea()));
        moveToWebElement(choosePortalPage.getPortalWidgetDragArea());
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getBtnPortalWidget()));
        moveToWebElement(choosePortalPage.getBtnPortalWidget());
        choosePortalPage.getBtnPortalWidget().click();
        CSLogger.info("Clicked on Portal Widget button ");
    }

    /**
     * This method moves cursor to given Webelement
     * 
     * @param element contains an element on which cursor has to be moved
     */
    private void moveToWebElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    /**
     * This method clicks on Secured Options
     */
    private void clkSecuredOptions() {
        traverseToCsPortalWindowFrame();
        choosePortalPage.clkIconSecuredOptions();
        CSLogger.info("Clicked on Secured options ");
    }

    /**
     * This method traverses to CSPortalWindowFrame
     */
    private void traverseToCsPortalWindowFrame() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(locator.getCsPortalWindowDiv()));
    }

    /**
     * This method adds PIM studio from widget options after opening up widget
     * window which loads PIM studio
     */
    private void addPimStudioFromWidgetOptions() {
        traverseToCsPortalWindowFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(choosePortalPage.getPimStudioOption()));
        doubleClick(choosePortalPage.getPimStudioOption());
        CSLogger.info("Double clicked on PIM studio");
        choosePortalPage.clkBtnApply();
        CSLogger.info("Clicked on Apply button");
        choosePortalPage.clkBtnSave();
        CSLogger.info("Clicked on save button");
        CSLogger.info("PIM studio loaded ");
    }

    /**
     * This method performs double click on element
     * 
     * @param element contains element reference as WebElement on which double
     *            click has to be performed
     */
    private void doubleClick(WebElement element) {
        actions.doubleClick(element).build().perform();
        CSLogger.info("Double clicked on element ");
    }

    /**
     * This method navigates to the created product under products node in PIM
     * studio in Widget window
     * 
     * @param productName contains the name of the product
     */
    private void navigateToCreatedProduct(String productName) {
        CSUtility.switchToDefaultFrame(browserDriver);
        // Mandatory sleep
        CSUtility.tempMethodForThreadSleep(5000);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmCsPortalMain()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmStudioWidgetPaneContentUserManagement()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmPimStudio()));
        clkOnProductsNode();
    }

    /**
     * This method clicks on the products node
     */
    private void clkOnProductsNode() {
        choosePortalPage.clkPimProductsNode();
        CSLogger.info("Clicked on Products node");
        CSUtility.tempMethodForThreadSleep(5000);
    }

    /**
     * This method maximizes the widget window
     */
    private void maximizeWidgetWindow() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
            waitForReload.until(ExpectedConditions
                    .visibilityOf(choosePortalPage.getPortalWidgetDragArea()));
            moveToWebElement(choosePortalPage.getPortalWidgetDragArea());
            waitForReload.until(ExpectedConditions
                    .visibilityOf(choosePortalPage.getBtnMaximize()));
            moveToWebElement(choosePortalPage.getBtnMaximize());
            choosePortalPage.getBtnMaximize().click();
            CSLogger.info("Clicked on Maximize button");
        } catch (Exception e) {
            CSLogger.info("Could not maximize the widget window");
        }
    }

    /**
     * This method verifies if Product is absent after logging in to the
     * application via other user to which access rights has not been assigned
     * 
     * @param productName contains the name of the product
     */
    private void verifyAbsenceOfProduct(String productName) {
        try {
            WebElement product = browserDriver
                    .findElement(By.linkText(productName));
            if (product.equals(productName)) {
                CSLogger.info("Product is present . Test case failed");
                Assert.fail(
                        "Test Case failed. Product should not be listed under products tab");
            }
        } catch (Exception e) {
            CSLogger.info(
                    " Test case verified.Product is not present under products node.");
        }
    }

    /**
     * This method logs in to the application as admin
     * 
     * @param language contains language
     * @param adminUsername contains username
     * @param adminPassword contains password
     */
    private void loginAsAdmin(String language, String adminUsername,
            String adminPassword) {
        chooseLanguage(language);
        loginPage.enterUsername(adminUsername);
        loginPage.enterPassword(adminPassword);
        CSLogger.info("Entered Username and password");
        WebElement btnLogin = waitForReload.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//input[@id='login']")));
        waitForReload.until(ExpectedConditions.visibilityOf(btnLogin));
        btnLogin.click();
        CSUtility.tempMethodForThreadSleep(2000);
        btnLogin.click();
        CSLogger.info("Clicked on login button");

    }

    /**
     * This data provider returns the sheet data which contains product
     * name,access right name,username,password,portal name, create new portal,
     * language tab,test tab to create in new portal
     * 
     * @return verifyAssignedUserRightsSheet
     */
    @DataProvider(name = "verifyAssignedUserRights")
    public Object[] CreateUserGroupAndUser() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("userManagementTestCases"),
                verifyAssignedUserRightsSheet);
    }

    /**
     * This method initializes all the resources used to drive the use case
     */
    @BeforeClass
    private void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        pimStudioProductsNode = new PimStudioProductsNodePage(browserDriver);
        productPopUp = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        csGuiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        csGuiDialogContentId = new CSGuiDialogContentId(browserDriver);
        actions = new Actions(browserDriver);
        loginPage = new LoginPage(browserDriver);
        choosePortalPage = new ChoosePortalPage(browserDriver);
        softAssert = new SoftAssert();
    }
}
