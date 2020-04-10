
package org.cs.csautomation.settings;

import java.util.ArrayList;
import java.util.List;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.ISettingsPopup;
import org.cs.csautomation.cs.settings.LanguageManagementPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * This class contains the test methods to delete and verify of the languages
 * 
 * @author CSAutomation Team
 */
public class DeleteLanguageManagementTest extends AbstractTest {

    private LanguageManagementPage languageNodeInstance;
    private CSPortalHeader         csPortalHeader;
    private FrameLocators          iframeLocatorsInstance;
    private SoftAssert             softAssert;
    WebDriverWait                  waitForReload;
    String                         deleteLanguageTreeData = "DeleteLanguageTreeData";
    String                         treeDeleteMessage      = "Do you really want to delete this item?";
    String                         listDeleteMessage      = "Delete: Do you really "
            + "want to execute this action recursively on 1 elements and ALL subelements?";
    Alert                          alertBox               = null;
    String                         deleteLanguageListData = "DeleteLanguageListData";
    String                         deleteLanguageMassData = "DeleteLanguageMassData";
    private CSPortalWidget         portalWidget;

    /**
     * Test method to delete the Language from the tree view on right click on
     * language and Delete. It also verifies the deleted languages
     * 
     * @param String languageName contains the name of the Languages.
     */
    @Test(priority = 1, dataProvider = "DeleteLanguageTreeData")
    public void DeleteLanguageTreeView(String languageName) {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            performDeleteUseCase(languageName, "TreeView", false);
            performDeleteUseCase(languageName, "TreeView", true);
            softAssert.assertAll();
            CSLogger.info("Delete language from tree view test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error : " + e);
            Assert.fail("Create Language Failure");
        }

    }

    /**
     * Test method to delete the Language from the List view by selecting the
     * mass edit delete option on language and Delete. It also verifies the
     * deleted languages
     * 
     * @param String languageName contains the name of the Languages.
     */
    @Test(priority = 2, dataProvider = "DeleteLanguageMassData")
    public void DeleteLanguageMassEdit(String languageName) {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            performDeleteUseCase(languageName, "MassEdit", false);
            performDeleteUseCase(languageName, "MassEdit", true);
            CSLogger.info("Delete language using mass edit test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error Delete language mass edit: ", e);
            Assert.fail("Create Language Failure");
        }
    }

    /**
     * Test method to delete the Language from the List view on right click on
     * language and Delete. It also verifies the deleted languages
     * 
     * @param String languageName contains the name of the Languages.
     */
    @Test(priority = 3, dataProvider = "DeleteLanguageListData")
    public void DeleteLanguageListView(String languageName) {
        waitForReload = new WebDriverWait(browserDriver, 180);
        try {
            performDeleteUseCase(languageName, "ListView", false);
            performDeleteUseCase(languageName, "ListView", true);
            CSLogger.info("Delete language from list view test completed!");
        } catch (Exception e) {
            CSLogger.debug("Automation Error delete language List view : ", e);
            Assert.fail("Create Language Failure");
        }

    }

    /**
     * This method will perform the operation based on OK and Cancel option.
     * 
     * @param String languageName name of the languages
     * 
     * @param String operation true and false for OK and Cancel button
     * 
     * @param Boolean okCancel contains the true and false
     */
    private void performDeleteUseCase(String languageName, String operation,
            Boolean okCancel) throws InterruptedException {
        clickOnLanguageTree();
        if (operation == "TreeView") {
            WebElement language = browserDriver
                    .findElement(By.linkText(languageName));
            CSUtility.rightClickTreeNode(waitForReload, language,
                    browserDriver);
            ISettingsPopup settingsPopup = CSPopupDivPim
                    .getCSPopupDivLocators(browserDriver);
            settingsPopup.selectPopupDivMenu(waitForReload,
                    settingsPopup.getCsGuiPopupMenuObject(), browserDriver);
            settingsPopup.selectPopupDivSubMenu(waitForReload,
                    settingsPopup.getObjectDeleteSubMenu(), browserDriver);
            getAlertBoxValidation(languageName, operation, okCancel);
        }
        if (operation == "ListView") {
            languageNodeInstance.getLanguageTreeNode().click();
            traverseToListView(languageName, operation, okCancel);
        }
        if (operation == "MassEdit") {
            languageNodeInstance.getLanguageTreeNode().click();
            traverseToListView(languageName, operation, okCancel);
        }
    }

    /**
     * Traverse to the middle language list view and find the matching row and
     * clicked or right clicked and perform the delete options depends upon the
     * operation
     * 
     * @param languageName string contains the language name that has to deleted
     * 
     * @Param operation string contains value Mass or List operation type
     * 
     * @Param isOkey Boolean value for Ok and cancel button
     */
    private void traverseToListView(String languageName, String operation,
            Boolean isOkey) {
        Integer totalRowsForNoProductsAssignedToClass = getListviewTableTotalRows()
                - 1;
        for (int iRowCount = 3; iRowCount <= totalRowsForNoProductsAssignedToClass; iRowCount++) {
            waitForReload.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[5]")));
            String sCellValue = browserDriver.findElement(By
                    .xpath("//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                            + iRowCount + "]/td[5]"))
                    .getText();
            if (sCellValue.equalsIgnoreCase(languageName)) {
                waitForReload.until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath(
                                "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                        + iRowCount + "]/td[1]/input[1]")));
                WebElement languageElement = browserDriver.findElement(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                + iRowCount + "]/td[1]/input[1]"));
                if (operation.equals("MassEdit")) {
                    languageElement.click();
                    Select selectByValue = new Select(browserDriver.findElement(
                            By.xpath("//select[@id='massUpdateSelector']")));
                    selectByValue.selectByValue("deletemarked");
                    CSLogger.info(
                            "selected the Language checkbox and mass delete");
                    getAlertBoxValidation(languageName, operation, isOkey);
                    break;
                }
                if (operation == "ListView") {
                    languageElement.click();
                    CSUtility.rightClickTreeNode(waitForReload,
                            browserDriver.findElement(By.xpath(
                                    "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                            + iRowCount + "]/td[1]/input[1]")),
                            browserDriver);
                    ISettingsPopup settingsPopup = CSPopupDivPim
                            .getCSPopupDivLocators(browserDriver);
                    settingsPopup.selectPopupDivMenu(waitForReload,
                            settingsPopup.getDeleteMenu(), browserDriver);
                    CSLogger.info("Right click and select delete option");
                    getAlertBoxValidation(languageName, operation, isOkey);
                    break;
                }
            }
        }
    }

    /**
     * This method will switch control to the Middle pane of the list view and
     * traverse till the List view table and get the total number of rows
     */
    private Integer getListviewTableTotalRows() {
        CSUtility.traverseToSystemPrefRightPaneFrames(waitForReload,
                browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='hidewrap CSAdminList']")));
        WebElement productInfoTable = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) productInfoTable
                .findElements(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody[1]/tr"));
        Integer totalRowsForNoProductsAssignedToClass = new Integer(
                rows.size());
        CSLogger.info("get the number of rows from the table list :"
                + totalRowsForNoProductsAssignedToClass);
        return totalRowsForNoProductsAssignedToClass;
    }

    /**
     * This method clicks on the Language Node of the settings and in middle
     * pane header menu it will clicks on create new and opens the create new
     * window in right pane
     */
    public void clickOnLanguage() {
        clickOnLanguageTree();
        CSUtility.traverseToSystemPrefRightPaneFrames(waitForReload,
                browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmSplitAreaFrameMain()));
        languageNodeInstance.clickOnCreateNew(waitForReload);
        CSLogger.info("Clicked on the color and travers till middle pane and "
                + "clicked on create new.");
        CSUtility.traverseToSystemPrefRightPaneFrames(waitForReload,
                browserDriver, iframeLocatorsInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmEditForActiveScript()));
    }

    /**
     * This method is will traverse till the settings ->system preferences->
     * Languages and click on the language node . It will opens the middle pane
     * of the list view
     */
    public void clickOnLanguageTree() {
        traverseTillSystemPreferences();
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
        languageNodeInstance.getLanguageTreeNode().click();
        CSUtility.tempMethodForThreadSleep(2000);
        languageNodeInstance.getLanguageTreeNode().click();
        CSLogger.info("clicked on language node in system preferences");
    }

    /**
     * This method is will traverse till the settings ->system preferences
     */
    public void traverseTillSystemPreferences() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
    }

    /**
     * This method is used to handle the Ok and Cancel alert box deletion of
     * Language values from both list and mass operation
     * 
     * @param languageName string which contains deleted language Name
     * 
     * @param operation string contains which operation need to perform for
     *            delete (mass delete, editor delete, list right click delete)
     * 
     * @Param isOkey Boolean True and false for OK and Cancel button
     */
    private void getAlertBoxValidation(String languageName, String operation,
            Boolean isOkey) {
        alertBox = CSUtility.getAlertBox(waitForReload, browserDriver);
        if (operation == "TreeView") {
            softAssert.assertEquals(alertBox.getText(), treeDeleteMessage);
        }
        if (isOkey) {
            alertBox.accept();
            CSLogger.info("Clicked on OK button of the delete Alert box");
            verifyDeleteLanguage(languageName);
        } else {
            alertBox.dismiss();
            CSLogger.info("Clicked on Cancel button of the delete Alert box");
        }
    }

    /**
     * This method used to verify the deleted language from the list, Tree and
     * Mass view.
     * 
     * @param string languageName name of the language which need to verify
     */
    private void verifyDeleteLanguage(String languageName) {
        clickOnLanguageTree();
        List<WebElement> languageList = browserDriver
                .findElements(By.linkText(languageName));
        if (languageList.isEmpty()) {
            CSLogger.info(
                    "Language " + languageName + " deleted Successfully ");
        }
    }

    /**
     * This method is used to pass data to test cases. It reads data from the
     * excel file.
     * 
     * @return Array contains the deleted Language names for Tree view.
     */
    @DataProvider(name = "DeleteLanguageTreeData")
    public Object[][] getLanguageTreeData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteLanguageTreeData);
    }

    /**
     * This method is used to pass data to test cases. It reads data from the
     * excel file.
     * 
     * @return Array contains the deleted Language names for List view.
     */
    @DataProvider(name = "DeleteLanguageListData")
    public Object[][] getLanguageListData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteLanguageListData);
    }

    /**
     * This method is used to pass data to test cases. It reads data from the
     * excel file.
     * 
     * @return Array contains the deleted Language names for Mass view.
     */
    @DataProvider(name = "DeleteLanguageMassData")
    public Object[][] getLanguageMassData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                deleteLanguageMassData);
    }

    /**
     * This method used to initialize all the instances before execution of test
     * cases
     */
    @BeforeClass
    public void initializeResources() {
        languageNodeInstance = SettingsLeftSectionMenubar
                .getLanguagesSettingNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
        softAssert = new SoftAssert();
    }
}
