/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.importstaging;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.PimStudioSettingsNode;
import org.cs.csautomation.cs.pom.ProductsPimStudioTree;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

/**
 * Class contains the test case of the create the import job and config the
 * fields and recreation of import database
 * 
 * @author CSAutomation Team
 *
 */
public class configureInboxDatabaseTest extends AbstractTest {

    private WebDriverWait                    waitForReload;
    private CSPortalHeader                   csPortalHeaderInstance;
    private FrameLocators                    iframeLocatorsInstance;
    private PimStudioSettingsNode            pimStudioSettingsNodeInstance;
    private ImportStagingLeftMenuAndListView importStangingTreeInstance;
    private ImportStagingTraversing          importFrameTraversInstance;
    private ImportStagingMainPage            importConfigInstance;
    private PimStudioSettingsNode            pimStudioSettInstance;
    private String                           importConfigDataSheet = "importConfigDataSheet";
    private String                           recreateDbMsg         = 
            "Really recreate Import Database Tables? All Import data gets lost!";
    private String                           successMsg            = 
            "Import Database Tables has been recreated.";

    /*
     * This method is used to configure the Import staging job details
     * @param importLabel String contains the import job Name
     * @param dbUser String contains the database user name
     * @param dbPassword String contains the database password
     * @param inboxFolderName String contains the Inbox folder to create
     * products
     */
    @Test(priority = 1, dataProvider = "importConfigData")
    public void configureInboxAndDatabase(String importLabel, String dbUser,
            String dbPassword, String dbName, String inboxFolderName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            clickedOnImportStagingTree();
            importFrameTraversInstance.traverseToImportMiddlePane(waitForReload,
                    browserDriver);
            importStangingTreeInstance.clkCreateNewImportJob(waitForReload);
            importFrameTraversInstance.traverseToConfigFields(waitForReload,
                    browserDriver);
            importConfigInstance.enterTextFieldValues(waitForReload,
                    importConfigInstance.getImportConnectionLabel(),
                    importLabel);
            enterDbConnectionDetails(dbUser, dbPassword, dbName);
            importFrameTraversInstance.traverseToConfigFields(waitForReload,
                    browserDriver);
            clickonImportSettingsTab();
            selectInboxFolder(inboxFolderName);
            saveImportJob();
            verifyCreatedJob(importLabel);
        } catch (Exception e) {
            CSLogger.debug("Automation error in : configureInboxAndDatabase",
                    e);
            Assert.fail("Automation error in : configureInboxAndDatabase");
        }
    }

    /*
     * This method is used to recreate the Import database
     * @param importLabel String contains the import job Name
     * @param dbUser String contains the database user name
     * @param dbPassword String contains the database password
     * @param inboxFolderName String contains the Inbox folder to create
     * products
     */
    @Test(priority = 2, dataProvider = "importConfigData")
    public void recreateImportDatabase(String importLabel, String dbUser,
            String dbPassword, String dbName, String inboxFolderName) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            clickedOnImportStagingTree();
            importFrameTraversInstance.traverseToImportMiddlePane(waitForReload,
                    browserDriver);
            WebElement importJob = browserDriver.findElement(
                    By.xpath("//td[contains(text(),'" + importLabel + "')]"));
            importJob.click();
            recreateImportDatabase();
        } catch (Exception e) {
            CSLogger.debug("Automation error in : recreateImportDatabase", e);
            Assert.fail("Automation error in : recreateImportDatabase");
        }
    }

    /*
     * This method is used to verify the created job.
     * @param importLabel String contains the Import job name
     */
    private void verifyCreatedJob(String importLabel) {
        clickedOnImportStagingTree();
        importFrameTraversInstance.traverseToImportMiddlePane(waitForReload,
                browserDriver);
        WebElement importJob = browserDriver.findElement(
                By.xpath("//td[contains(text(),'" + importLabel + "')]"));
        AssertJUnit.assertEquals(importJob.getText(), importLabel);
        CSLogger.info("Import Job Created Sucessfully");
    }

    /*
     * This method is used to save the import job information
     */
    private void saveImportJob() {
        importFrameTraversInstance.traverseToConfigFields(waitForReload,
                browserDriver);
        importConfigInstance.getImportSaveButton().click();
        CSLogger.info("Import Job saved Sucessfully");

    }

    /*
     * This method is used to recreate the import database This will create on
     * the recreate database button of newly created job
     */
    private void recreateImportDatabase() {
        importFrameTraversInstance.traverseToConfigFields(waitForReload,
                browserDriver);
        importConfigInstance.getRecreateDbButton().click();
        CSLogger.info("Clicked on Import database re-create option");
        Alert alertInstance = CSUtility.getAlertBox(waitForReload,
                browserDriver);
        if (alertInstance.getText().equals(recreateDbMsg)) {
            alertInstance.accept();
            verifyRecreatedDatabase();
        }
    }

    /*
     * This method is used to verify the recreated database.
     */
    public void verifyRecreatedDatabase() {
        CSUtility.tempMethodForThreadSleep(2000);
        Alert successAlert = CSUtility.getAlertBox(waitForReload,
                browserDriver);
        Assert.assertEquals(successAlert.getText(), successMsg);
        successAlert.accept();
        CSLogger.info("Import database re-created successfully");
    }

    /*
     * This method is used to clicked on the Import setting Tab and the product
     * section to config the import information
     */
    private void clickonImportSettingsTab() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(importConfigInstance.geImportSettingsTab()));
        importConfigInstance.geImportSettingsTab().click();
        importConfigInstance.getProductsTab().click();
        CSLogger.info("clicked on ImportSettings Tab and Product Section");
    }

    /*
     * This method is used to fill the Database configuration details
     * @param String dbUser contains the database user name
     * @param String dbPassword contains the database password
     * @param String dbName contains the database name
     */
    private void enterDbConnectionDetails(String dbUser, String dbPassword,
            String dbName) {
        importConfigInstance.getDbConnSection().click();
        importConfigInstance.enterTextFieldValues(waitForReload,
                importConfigInstance.getImportconnectionDatabaseUser(), dbUser);
        importConfigInstance.enterTextFieldValues(waitForReload,
                importConfigInstance.getImportconnectionDatabaseName(), dbName);
        importConfigInstance.enterTextFieldValues(waitForReload,
                importConfigInstance.getImportconnectionDatabasePassword(),
                dbPassword);
        importConfigInstance.getDbPasswordLabel().click();
        handlePopupToConfirmPassword(dbPassword);
        CSLogger.info("entered userName, Password and DatabaseName");
    }

    /*
     * This method is used to click on the Import Database tree node under the
     * PIM->Settings->Import Database
     */
    private void clickedOnImportStagingTree() {
        csPortalHeaderInstance.clkBtnProducts(waitForReload);
        pimStudioSettingsNodeInstance.clkBtnPimSettingsNode(waitForReload);
        CSLogger.info("traverse till setting node");
        pimStudioSettInstance.clkImportstagingTreenode(waitForReload);
        CSLogger.info("traverse till import staging and clicked");
    }

    /**
     * This method handles the pop up of confirm password field It traverse to
     * the new window frames
     * 
     * @param password
     *            contains the password in the string format
     */
    private void handlePopupToConfirmPassword(String password) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowDiv()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(importConfigInstance.getverifyPasswordInput()));
        importConfigInstance.getverifyPasswordInput().clear();
        importConfigInstance.getverifyPasswordInput().sendKeys(password);
        importConfigInstance.clickOkButton(waitForReload);
        CSLogger.info("Clicked on OK button of pop up");
    }

    /*
     * This method is used to select the Inbox folder for the Import job
     * @param folderName String name of the folder which is mapped for inbox
     */
    private void selectInboxFolder(String folderName) {
        waitForReload.until(ExpectedConditions
                .visibilityOf(importConfigInstance.getInboxFolderSelection()));
        importConfigInstance.getInboxFolderSelection().click();
        CSUtility.tempMethodForThreadSleep(1000);
        traverseToDataSelectionDialog();
        Actions actions = new Actions(browserDriver);
        WebElement inboxFolder = browserDriver
                .findElement(By.linkText(folderName));
        actions.doubleClick(inboxFolder).perform();
        CSLogger.info("Inbox folder selected Sucessfully");
    }

    /*
     * This method is used to traverse the data selection dialog to select the
     * product folder for the INBOX
     */
    private void traverseToDataSelectionDialog() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmDataSelectionDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getImportRecordDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmIdRecordsPdmarticleLeft()));
        CSLogger.info(
                "tarverse till data selection dialog of the inbox selection");
    }

    /**
     * This data provider contains attribute folder name, class name and names
     * of attributes
     * 
     * @return array of attribute folder name, class name and names of
     *         attributes
     */
    @DataProvider(name = "importConfigData")
    public Object[][] getProductChildData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("importStagingTestCases"),
                importConfigDataSheet);
    }

    /*
     * This is used to initialize the instances of the necessary classes
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        pimStudioSettingsNodeInstance = ProductsPimStudioTree
                .getPimStudioSettingsNodeInstance(browserDriver);
        importStangingTreeInstance = new ImportStagingLeftMenuAndListView(
                browserDriver);
        pimStudioSettInstance = new PimStudioSettingsNode(browserDriver);
        importFrameTraversInstance = new ImportStagingTraversing(browserDriver);
        importConfigInstance = new ImportStagingMainPage(browserDriver);
    }

}
