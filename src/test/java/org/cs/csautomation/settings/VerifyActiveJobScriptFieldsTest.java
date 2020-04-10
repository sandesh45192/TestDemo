/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.pom.ModalDialogPopupWindow;
import org.cs.csautomation.cs.pom.SelectionDialogWindow;
import org.cs.csautomation.cs.settings.ActiveJobsPage;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.IUserManagementPopup;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivSettings;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
 * This class contains the test method to verify active jobs automation tab
 * fields.
 * 
 * @author CSAutomation Team
 *
 */
public class VerifyActiveJobScriptFieldsTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private CSPortalHeader         csPortalHeaderInstance;
    private CSPortalWidget         csPortalWidgetInstance;
    private ActiveJobsPage         activeJobsPageInstance;
    private FrameLocators          iframeLocatorsInstance;
    private CSGuiToolbarHorizontal csGuiToolbarHorizontalInstance;
    private Actions                performAction;
    private SelectionDialogWindow  selectionDialogWindowInstance;
    private SoftAssert             softAssertion;
    private String                 alertText;
    private ModalDialogPopupWindow modalDialogPopupWindowInstance;
    private String                 scriptConfigTestSheet = "VerifyActiveJobFields";
    private UserManagementPage     userManagementPageInstance;
    private IUserManagementPopup   userManagementPopup;
    private String                 userParentWindow;
    private String                 automationTabName     = "Automation";
    private String                 propertiesTabName     = "Properties";
    private String                 jobsTabName           = "Jobs";

    /**
     * This test method verifies execution of active job script's automation tab
     * fields.
     * 
     * @param importScriptName String object contains import script name.
     * @param importCategory String object contains import category.
     * @param scriptType String object contains script type. i.e 'Say Hello'.
     * @param automationTabFields String object contains automation tab field
     *            separated with comma.
     * @param userGroupName String object contains user group name.
     * @param username String object contains user name.
     * @param password String object contains user password.
     * @param userRole String object contains user role.
     * @param userAccessRight String object contains user access rights.
     * @param customOptionFields String object contains custom schedule option
     *            field
     * @param count String object contains loop count.
     */
    @Test(dataProvider = "activeScriptTestData")
    public void testVerifyFieldsOfActiveJobScript(String importScriptName,
            String importCategory, String scriptType,
            String automationTabFields, String userGroupName, String username,
            String password, String userRole, String userAccessRight,
            String customOptionFields, String count) {
        try {
            waitForReload = new WebDriverWait(browserDriver, 180);
            createActiveScript(importScriptName, importCategory, scriptType);
            verifyAutomationTabFields(automationTabFields);
            verifySchedulingField(customOptionFields);
            verifyLastRunField(importScriptName);
            verifyRunAsUserField(importScriptName, userGroupName, username,
                    password, userRole, userAccessRight);
            verifyPauseBetweenStepField(count);
            softAssertion.assertAll();
        } catch (Exception e) {
            CSLogger.debug("Automation error in test method: "
                    + "testVerifyFieldsOfActiveJobScript", e);
            Assert.fail("Automation error in test method: "
                    + "testVerifyFieldsOfActiveJobScript", e);
        }
    }

    /**
     * Verifies the last run field.
     *
     * @param importScriptName String object contains import script name.
     */
    private void verifyLastRunField(String importScriptName) {
        goToCreatedImportScript(importScriptName);
        traverseToScriptConfiguration();
        clkOnGivenTab(jobsTabName);
        executeImportScript(true);
        clkOnRefreshToolbarButton();
        int headerCount = returnHeaderCount("End");
        int actualCount = headerCount + 2;
        String endTimeOfScript = browserDriver.findElement(By
                .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td["
                        + actualCount + "]"))
                .getText();
        traverseToScriptConfiguration();
        clkOnRefreshToolbarButton();
        clkOnGivenTab(automationTabName);
        WebElement lastRunFieldElement = browserDriver.findElement(
                By.xpath("//span[contains(@id,'ActivescriptLastRun')]"));
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                lastRunFieldElement);
        String lastRunTime = lastRunFieldElement.getText();
        softAssertion.assertEquals(lastRunTime, endTimeOfScript,
                "Last run field time is not correct : verification failed");
    }

    /**
     * This method creates the basic active script.
     * 
     * @param importScriptName String object contains import script name.
     * @param importCategory String object contains import category.
     * @param scriptType String object contains script type.
     */
    public void createActiveScript(String importScriptName,
            String importCategory, String scriptType) {
        try {
            performOperationToCreateActiveScript();
            configureImportScript(importScriptName, importCategory, scriptType);
            clkOnSaveButton();
        } catch (Exception e) {
            CSLogger.debug("Import script creation failed" + e);
            Assert.fail("Import script creation failed" + e);
        }
    }

    /**
     * Creates the import active script.
     */
    private void performOperationToCreateActiveScript() {
        switchToSettingsPage();
        goToSystemPreferencesIcon();
        expandActiveJobsTree();
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csGuiToolbarHorizontalInstance.clkOnWebElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew(),
                "create new");
    }

    /**
     * Performs operation of clicking on system preference icon.
     */
    private void goToSystemPreferencesIcon() {
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csPortalWidgetInstance.clkOnSystemPreferencesIcon(waitForReload);
    }

    /**
     * Configures the active script
     * 
     * @param importLabel String object contains import label.
     * @param importCategory String object contains import category.
     * @param scriptType String object defines the active script type.
     */
    private void configureImportScript(String importLabel,
            String importCategory, String scriptType) {
        try {
            traverseToScriptConfiguration();
            activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                    activeJobsPageInstance.getTxtNewActiveScriptLabel(),
                    importLabel);
            activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                    activeJobsPageInstance.getTxtNewActiveScriptCategory(),
                    importCategory);
            activeJobsPageInstance.selectActiveScriptOption(scriptType);
        } catch (Exception e) {
            CSLogger.error(
                    "Active script window does not contain fields 'Label', "
                            + "'Category' and 'Script' : test step failed",
                    e);
            Assert.fail("Active script window does not contain fields 'Label', "
                    + "'Category' and 'Script' : test step failed", e);
        }
    }

    /**
     * Switches the frame till edit frames.
     */
    private void traverseToScriptConfiguration() {
        traverseToSettingsRightFrames();
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
    }

    /**
     * Clicks on save button to save the configured import active script.
     */
    private void clkOnSaveButton() {
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarSave(waitForReload);
    }

    /**
     * Executes the configured import script.
     * 
     * @param isPressOkay Boolean parameter contains values whether to execute
     *            the script or not.
     */
    private void executeImportScript(Boolean isPressOkay) {
        try {
            csGuiToolbarHorizontalInstance
                    .clkBtnCSGuiToolbarRunActiveScript(waitForReload);
            Alert getAlertBox = CSUtility.getAlertBox(waitForReload,
                    browserDriver);
            alertText = "Are you sure to run this ActiveScript?";
            if (!(getAlertBox.getText().contains(alertText))) {
                CSLogger.error(
                        "Alert box saying : " + alertText + " does not appear");
                softAssertion.fail(
                        "Alert box saying : " + alertText + " does not appear");
            }
            if (isPressOkay) {
                getAlertBox.accept();
                if (isScriptExecuted()) {
                    CSLogger.info(
                            "When clicked on ok button of alert box import "
                                    + "script executed ");
                } else {
                    CSLogger.error(
                            "Import script didn't execute when clicked on ok"
                                    + " button of alert box");
                    softAssertion
                            .fail("Import script didn't execute when clicked on "
                                    + "ok button of alert box");
                }
            } else {
                getAlertBox.dismiss();
                if (isScriptExecuted()) {
                    CSLogger.error(
                            "Import script executed when clicked on cancel"
                                    + " button of alert box");
                    softAssertion.fail("Import script executed when clicked on "
                            + "cancel button of alert box");
                } else {
                    CSLogger.info(
                            "When clicked on cancel button of alert box import "
                                    + "script didn't execute ");
                }
            }
        } catch (Exception e) {
            CSLogger.error("Error while running the active script", e);
            Assert.fail("Error while running the active script", e);
        }
        traverseToScriptConfiguration();
    }

    /**
     * Checks whether script is executed.
     * 
     * @return Boolean value either true or false.
     */
    private boolean isScriptExecuted() {
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmShowingStatusOfRunningActiveScript()));
        int isScriptExecuted = browserDriver
                .findElements(By.id("ActiveScriptLogTable")).size();
        if (isScriptExecuted != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clicks on given tab.
     * 
     * @param tabName String object contains name of product tab.
     */
    private void clkOnGivenTab(String tabName) {
        waitForReload.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//nobr[contains(text(),'" + tabName + "')]/parent::*")));
        WebElement element = browserDriver.findElement(By
                .xpath("//nobr[contains(text(),'" + tabName + "')]/parent::*"));
        element.click();
        CSLogger.info("Clicked on the " + tabName + " tab");
    }

    /**
     * Expands the active jobs tree node.
     */
    private void expandActiveJobsTree() {
        traverseToSettingsLeftTree();
        CSUtility.scrollUpOrDownToElement(
                activeJobsPageInstance.getNodeActiveJobs(), browserDriver);
        performAction.doubleClick(activeJobsPageInstance.getNodeActiveJobs())
                .perform();
        CSUtility.tempMethodForThreadSleep(2000);
    }

    /**
     * Traverse to setting's tree.
     */
    private void traverseToSettingsLeftTree() {
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * Switches to Settings tab.
     */
    private void switchToSettingsPage() {
        csPortalHeaderInstance.clkBtnSettingsTab(waitForReload);
    }

    /**
     * Clicks on given import script.
     * 
     * @param scriptName String object contains script name.
     */
    private void clkOnCreatedImportScript(String scriptName) {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        enterDataInFilterBar(waitForReload, scriptName);
        WebElement createdImportScript = browserDriver.findElement(
                By.xpath("//tr[@id='HeaderBarTop']/following-sibling::tr/"
                        + "td[contains(text(),'" + scriptName + "')]"));
        CSUtility.scrollUpOrDownToElement(createdImportScript, browserDriver);
        createdImportScript.click();
        CSLogger.info("Clicked on created import script : " + scriptName);
    }

    /**
     * Enters the data in filter bar.
     * 
     * @param waitForReload WebDriverWait object.
     * @param data String object contains data to be entered in filter bar.
     */
    public void enterDataInFilterBar(WebDriverWait waitForReload, String data) {
        CSUtility.tempMethodForThreadSleep(1000);
        int removeFilterButtonExists = browserDriver
                .findElements(
                        By.xpath("//a/img[contains(@src,'nofilter.gif')]/.."))
                .size();
        if (removeFilterButtonExists != 0) {
            csGuiToolbarHorizontalInstance.clkOnBtnNoFilter(waitForReload);
            CSUtility.tempMethodForThreadSleep(1000);
        } else {
            csGuiToolbarHorizontalInstance.clkOnBtnFilter(waitForReload);
        }
        csGuiToolbarHorizontalInstance.getTxtFilterBar().click();
        CSUtility.tempMethodForThreadSleep(1000);
        csGuiToolbarHorizontalInstance.getTxtFilterBar().clear();
        csGuiToolbarHorizontalInstance.getTxtFilterBar().sendKeys(data);
        csGuiToolbarHorizontalInstance.getTxtFilterBar().sendKeys(Keys.ENTER);
        CSLogger.info("Entered text  " + data + "in filter bar");
    }

    /**
     * Clicks on refresh button from transformation window.
     */
    private void clkOnRefreshToolbarButton() {
        csGuiToolbarHorizontalInstance.clkBtnCSGuiToolbarRefresh(waitForReload);
        CSUtility.tempMethodForThreadSleep(1000);
    }

    /**
     * This method enters the given text into transformation windows text box.
     * 
     * @param element WebElement of text box.
     * @param text -String object contains value to be entered into text box
     */
    public void enterTextIntoTextbox(WebElement element, String text) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        element.clear();
        element.sendKeys(text);
        CSLogger.info("Entered " + text + " into text box");
    }

    /**
     * Selects the given option from drop down.
     * 
     * @param drpDwnElement Drop down WebElement.
     * @param option String object contains option to be selected.
     */
    public void selectDrpDwnOption(WebElement drpDwnElement, String option) {
        CSUtility.waitForVisibilityOfElement(waitForReload, drpDwnElement);
        CSUtility.scrollUpOrDownToElement(drpDwnElement, browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        drpDwnElement.click();
        CSUtility.tempMethodForThreadSleep(1000);
        Select element = new Select(drpDwnElement);
        element.selectByVisibleText(option);
        CSLogger.info("Drop down option " + option + " selected");
    }

    /**
     * Spilt's the given data separated from comma.
     * 
     * @param data String object contains data separated by comma.
     * @return String array of data.
     */
    private String[] spiltData(String data) {
        return data.split(",");
    }

    /**
     * This method verifies different section of default fields.
     * 
     * @param tableData List parameter holds list of WebElements.
     * @param fields String object contains array of fields to be verified.
     * @param label String specifies the area where fields must be visible.
     */
    private void verifyFields(List<WebElement> tableData, String[] fields,
            String label) {
        ArrayList<String> cellData = new ArrayList<>();
        for (WebElement cell : tableData) {
            cellData.add(cell.getText());
        }
        for (int index = 0; index < fields.length; index++) {
            if (!cellData.contains(fields[index])) {
                CSLogger.error("Field : " + fields[index] + " not found under "
                        + label);
                Assert.fail("Field : " + fields[index] + " not found under "
                        + label);
            }
        }
    }

    /**
     * This method clicks on file folder from the file selection dialog window
     */
    public void clkBtnControlPaneButtonFilesFolder() {
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriver);
        WebElement fileFolder = browserDriver
                .findElement(By.xpath("//div[@class='ControlPaneButton'][1]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, fileFolder);
        fileFolder.click();
        CSLogger.info("Clicked on Control Pane Button Files Folder");
    }

    /**
     * Traverses to created import script.
     * 
     * @param importScriptName String object contains import script name.
     */
    private void goToCreatedImportScript(String importScriptName) {
        switchToSettingsPage();
        goToSystemPreferencesIcon();
        expandActiveJobsTree();
        clkOnCreatedImportScript(importScriptName);
        traverseToScriptConfiguration();
        CSUtility.tempMethodForThreadSleep(2000);
        clkOnGivenTab("Properties");
    }

    /**
     * Verifies the automation tab fields.
     * 
     * @param automationTabFields String object contains automation tab fields
     *            separated with comma.
     */
    private void verifyAutomationTabFields(String automationTabFields) {
        traverseToScriptConfiguration();
        clkOnGivenTab(automationTabName);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                browserDriver.findElement(By.xpath("//div[@cs_name='"
                        + automationTabName + "']/div/table")));
        List<WebElement> getTableData = browserDriver.findElements(By
                .xpath("//div[@cs_name='Automation']/div/table/tbody/tr/td/table"
                        + "/tbody" + "/" + "tr/td[1]"));
        verifyFields(getTableData, spiltData(automationTabFields),
                "Automation tab");
        verifyDefaultField(
                "Schedule,Time Limit,Memory Limit,Log Level,Pause between the steps");
    }

    /**
     * Verifies whether the fields are not set with null default values.
     * 
     * @param defaultField String object contains active script fields.
     */
    private void verifyDefaultField(String defaultField) {
        CSUtility.tempMethodForThreadSleep(1000);
        String fields[] = defaultField.split(",");
        WebElement element = null;
        for (int index = 0; index < fields.length; index++) {
            element = browserDriver.findElement(By.xpath(
                    "//tr[@cs_name='" + fields[index] + "']/td[2]/div/select"));
            if (element.getAttribute("defaultvalue") == null) {
                CSLogger.error(
                        "Default value is not set for field " + fields[index]);
                softAssertion.fail(
                        "Default value is not set for field " + fields[index]);
            }
        }
    }

    /**
     * Creates the given user group.
     * 
     * @param userGroupName String object contains user group name.
     */
    private void createUserGroup(String userGroupName) {
        expandUsersNode();
        CSUtility.rightClickTreeNode(waitForReload,
                userManagementPageInstance.getUsersNode(), browserDriver);
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupNewGroup(), browserDriver);
        userManagementPopup.enterValueInDialogue(waitForReload, userGroupName);
        userManagementPopup.askBoxWindowOperation(waitForReload, true,
                browserDriver);
    }

    /**
     * Expands the users node.
     */
    private void expandUsersNode() {
        traverseToSettingsLeftTree();
        userManagementPageInstance.clkUserManagement(waitForReload);
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPageInstance.getUsersNode()));
        userManagementPageInstance.getUsersNode().click();
        CSLogger.info("Clicked on user");
    }

    /**
     * Sets the user role and access rights.
     * 
     * @param userRole String object contains user role.
     * @param userAccessRight String object contains user access rights.
     * @param userGroupName String object contains user group name.
     */
    private void setUserRoleAndAccessRights(String userRole,
            String userAccessRight, String userGroupName) {
        CSUtility.rightClickTreeNode(waitForReload,
                getCreatedUserGroup(userGroupName), browserDriver);
        userManagementPopup.selectPopupDivMenu(waitForReload,
                userManagementPopup.getCsPopupEdit(), browserDriver);
        traverseToSettingsRightFrames();
        userManagementPageInstance.clkRightsTab(waitForReload);
        userParentWindow = browserDriver.getWindowHandle();
        clkOnLabelAdd();
        CSUtility.tempMethodForThreadSleep(1000);
        String childWindow = (String) (browserDriver.getWindowHandles()
                .toArray())[1];
        browserDriver.switchTo().window(childWindow);
        WebElement setUserRole = browserDriver.findElement(
                By.xpath("//select[@name='RoleID']/option[contains(text(),'"
                        + userRole + "')]"));
        setUserRole.click();
        CSLogger.info("User role set as : " + userRole);
        WebElement setAccessRights = browserDriver.findElement(
                By.xpath("//select[@name='RubricID']/option[contains(text(),'"
                        + userAccessRight + "')]"));
        setAccessRights.click();
        CSLogger.info("User access right set as : " + userAccessRight);
        userManagementPopup.getBtnOk().click();
        CSLogger.info("Clicked on ok button of Edit Autorization pop up");
        browserDriver.switchTo().window(userParentWindow);
        traverseToSettingsRightFrames();
        clkOnSaveButton();
        traverseToSettingsLeftTree();
        clkOnCreatedUserGroup(userGroupName);
    }

    /**
     * Traverses to settings editor window right frames.
     */
    private void traverseToSettingsRightFrames() {
        TraversingForSettingsModule.traverseToSystemPrefRightPaneFrames(
                waitForReload, browserDriver, iframeLocatorsInstance);
    }

    /**
     * Returns the WebElement of user group.
     *
     * @param userGroupName String object contains user group name.
     * @return WebElement of user group.
     */
    private WebElement getCreatedUserGroup(String userGroupName) {
        traverseToSettingsLeftTree();
        WebElement createdUserGroup = browserDriver
                .findElement(By.linkText(userGroupName));
        CSUtility.tempMethodForThreadSleep(1000);
        CSUtility.waitForVisibilityOfElement(waitForReload, createdUserGroup);
        return createdUserGroup;
    }

    /**
     * This method creates a user.
     * 
     * @param username String object contains user name.
     * @param password String object contains password.
     */
    private void createUser(String username, String password) {
        TraversingForSettingsModule.traverseToFrameOfSettingsSplitAreaFrameMain(
                waitForReload, browserDriver, iframeLocatorsInstance);
        csGuiToolbarHorizontalInstance.clkOnWebElement(waitForReload,
                csGuiToolbarHorizontalInstance.getBtnCSGuiToolbarCreateNew(),
                "create new");
        traverseToSettingsRightFrames();
        TraversingForSettingsModule.traverseToEditFrame(waitForReload,
                browserDriver);
        userManagementPageInstance.clkAdministrationTab(waitForReload);
        enterDataInField(userManagementPageInstance.getTxtUsername(), username,
                "username");
        enterDataInField(userManagementPageInstance.getTxtPassword(), password,
                "password");
        clkOnSaveButton();
        confirmPassword(password);
    }

    /**
     * This method traverses up to label Add to add Role and Access Right
     */
    private void clkOnLabelAdd() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmFrameSettings()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPageInstance.getFormLabelAdd()));
        userManagementPageInstance.getFormLabelAdd().click();
        CSLogger.info("Clicked on Add under rights tab");
    }

    /**
     * Enters the data into text box fields.
     * 
     * @param element WebElement of textbox.
     * @param data String object contains data.
     * @param label String object contains label.
     */
    private void enterDataInField(WebElement element, String data,
            String label) {
        waitForReload.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(data);
        CSLogger.info("Entered " + label + " in text box as : " + data);
    }

    /**
     * This method handles the pop up of confirm password field
     * 
     * @param password String object contains user password.
     */
    private void confirmPassword(String password) {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(iframeLocatorsInstance.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getCsPortalWindowFrame()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPopup.getUserInput()));
        userManagementPopup.getUserInput().sendKeys(password);
        userManagementPopup.clkOkOfPopup(waitForReload);
        CSLogger.info("Confirmed user password");
    }

    /**
     * Adds the user from data selection dialog window.
     */
    private void addUserFromDataSelectionDialogWindow(String userGroupName,
            String username) {
        selectionDialogWindowInstance.clkBtnControlPaneButtonUserFolder(
                waitForReload, browserDriver);
        TraverseSelectionDialogFrames.traverseTillUserFoldersleftFrames(
                waitForReload, browserDriver);
        WebElement userTree = browserDriver.findElement(By.id("User@0"));
        waitForReload.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("User@0")));
        userTree.click();
        CSLogger.info("Clicked on user tree");
        WebElement createdUserGroup = browserDriver.findElement(
                By.xpath("//span[contains(text(),'" + userGroupName + "')]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, createdUserGroup);
        createdUserGroup.click();
        CSLogger.info("Clicked on user group: " + userGroupName);
        TraverseSelectionDialogFrames.traverseTillUserFoldersCenterPane(
                waitForReload, browserDriver);
        WebElement allowedUser = browserDriver.findElement(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
        waitForReload.until(ExpectedConditions.visibilityOf(allowedUser));
        allowedUser.click();
        CSLogger.info("Clicked on allowed user" + username);
        modalDialogPopupWindowInstance.askBoxWindowOperation(waitForReload,
                true, browserDriver);
        traverseToScriptConfiguration();
        CSUtility.tempMethodForThreadSleep(2000);
        clkOnSaveButton();
    }

    /**
     * Clicks on created user group.
     * 
     * @param userGroupName String object contains user group name.
     */
    private void clkOnCreatedUserGroup(String userGroupName) {
        getCreatedUserGroup(userGroupName).click();
        CSLogger.info("clicked on configured user group : " + userGroupName);
    }

    /**
     * Returns the given header number.
     * 
     * @param header String object contains header name.
     * @return Integer value contains header number.
     */
    private int returnHeaderCount(String header) {
        traverseToScriptConfiguration();
        traverseToJobsTabFrame();
        List<WebElement> getStartDateHeaderNumber = browserDriver.findElements(
                By.xpath("//table[@class='hidewrap CSAdminList']/tbody/tr/th"));
        int headerCount = 0;
        for (Iterator<WebElement> iterator2 = getStartDateHeaderNumber
                .iterator(); iterator2.hasNext();) {
            WebElement webElement = (WebElement) iterator2.next();
            if (!(webElement.getText().equals(""))) {
                if (webElement.getText().equals(header)) {
                    break;
                } else {
                    headerCount++;
                }
            }
        }
        return headerCount;
    }

    /**
     * Verifies the Run As User field.
     * 
     * @param userGroupName String object contains user group name.
     * @param username String object contains user name.
     * @param password String object contains user password.
     * @param userRole String object contains user role.
     * @param userAccessRight String object contains user access rights.
     */
    private void verifyRunAsUserField(String importScriptName,
            String userGroupName, String username, String password,
            String userRole, String userAccessRight) {
        configureUser(userGroupName, username, password, userRole,
                userAccessRight);
        goToCreatedImportScript(importScriptName);
        traverseToScriptConfiguration();
        clkOnGivenTab(automationTabName);
        CSUtility.tempMethodForThreadSleep(1000);
        WebElement runAsUserElement = browserDriver
                .findElement(By.xpath("//div[contains(@id,'Activescript_"
                        + "UserAvatarID_csReferenceDiv" + "')]/div[2]/img"));
        CSUtility.waitForElementToBeClickable(waitForReload, runAsUserElement);
        runAsUserElement.click();
        addUserFromDataSelectionDialogWindow(userGroupName, username);
        CSUtility.tempMethodForThreadSleep(1000);
        executeImportScript(true);
        clkOnGivenTab("Jobs");
        int headerCount = returnHeaderCount("Running under");
        int actualCount = headerCount + 2;
        String runningUser = browserDriver.findElement(By
                .xpath("//table[@class='hidewrap CSAdminList']/tbody/tr[3]/td["
                        + actualCount + "]"))
                .getText();
        softAssertion.assertEquals(runningUser, username,
                "Selected user didn't execute the script : verification failed");
    }

    /**
     * Configures a user along with user group and access rights.
     */
    private void configureUser(String userGroupName, String username,
            String password, String userRole, String userAccessRight) {
        createUserGroup(userGroupName);
        setUserRoleAndAccessRights(userRole, userAccessRight, userGroupName);
        createUser(username, password);
    }

    /**
     * Verifies the scheduling field of active script.
     * 
     * @param customOptionFields String object contains custom fields separated.
     */
    private void verifySchedulingField(String customOptionFields) {
        try {
            verifyEveryMinuteScheduLeOption();
            verifyManualOption();
            verifyCustomOptionFields(customOptionFields);
        } catch (Exception e) {
            CSLogger.error("Error while verifying scheduling field", e);
            softAssertion.fail("Error while verifying scheduling field", e);
        }
    }

    /**
     * Verifies the execution of schedule field with it is set to 'Every
     * Minute'.
     */
    private void verifyEveryMinuteScheduLeOption() {
        try {
            traverseToScriptConfiguration();
            clkOnGivenTab(automationTabName);
            setScheduleOption("Every Minute");
            clkOnSaveButton();
            // Delay of one minute for script execution as job is scheduled to
            // run after one minute.
            clkOnGivenTab("Jobs");
            traverseToJobsTabFrame();
            TimeUnit.MINUTES.sleep(1);
            clkOnRefreshToolbarButton();
            WebElement getJobCount = browserDriver.findElement(By.xpath(
                    "//td[@class='CSGuiListFooter']/table/tbody/tr/td[2]"));
            CSUtility.waitForVisibilityOfElement(waitForReload, getJobCount);
            if (getJobCount.getText().contains("0")) {
                CSLogger.error("Scheduling in script failed ");
                softAssertion.fail("Scheduling in script failed ");
            }
            traverseToScriptConfiguration();
            clkOnGivenTab(automationTabName);
            CSUtility.tempMethodForThreadSleep(2000);
            setScheduleOption("Manual");
            CSUtility.tempMethodForThreadSleep(1000);
            clkOnSaveButton();
        } catch (InterruptedException e) {
            CSLogger.debug("Error while verifying scheduling field", e);
            softAssertion.fail("Error while verifying scheduling field", e);
        }
    }

    /**
     * Sets the schedule field with given schedule option.
     * 
     * @param scheduleOption String object contains schedule option.
     */
    private void setScheduleOption(String scheduleOption) {
        WebElement scheduleFieldElement = browserDriver.findElement(By
                .xpath("//select[contains(@id,'ActivescriptAutomationType')]"));
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                scheduleFieldElement, scheduleOption);
    }

    /**
     * Traverse to job tab frame.
     */
    private void traverseToJobsTabFrame() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmJobsState()));
    }

    /**
     * Verifies the execution of schedule field with it is set to 'Manual'.
     */
    private void verifyManualOption() {
        try {
            traverseToScriptConfiguration();
            clkOnGivenTab(automationTabName);
            setScheduleOption("Manual");
            executeImportScript(true);
        } catch (Exception e) {
            CSLogger.error("Manual script execution failed", e);
            softAssertion.fail("Manual script execution failed", e);
        }
    }

    /**
     * Verifies the fields with scheduling option is set to 'Custom'.
     * 
     * @param customOptionFields String object contains custom fields.
     */
    private void verifyCustomOptionFields(String customOptionFields) {
        traverseToScriptConfiguration();
        clkOnGivenTab(automationTabName);
        setScheduleOption("Custom");
        List<WebElement> getTableData = browserDriver.findElements(By
                .xpath("//div[@cs_name='Automation']/div/table/tbody/tr/td/table"
                        + "/tbody" + "/" + "tr/td[1]"));
        verifyFields(getTableData, spiltData(customOptionFields),
                "custom schedule option");
        setScheduleOption("Manual");
    }

    /**
     * Verifies the execution of 'Pause between the steps' Field of active
     * script.
     * 
     * @param count String object contains loop count.
     */
    private void verifyPauseBetweenStepField(String count) {
        traverseToScriptConfiguration();
        clkOnGivenTab(propertiesTabName);
        activeJobsPageInstance.enterTextIntoTextbox(waitForReload,
                activeJobsPageInstance.getTxtCountForSayHelloScript(), count);
        clkOnSaveButton();
        clkOnGivenTab(automationTabName);
        CSUtility.tempMethodForThreadSleep(1000);
        activeJobsPageInstance.selectDrpDwnOption(waitForReload,
                activeJobsPageInstance
                        .getDrpDwnActivescriptSleepAfterEachStep(),
                "1000ms timeout between the loops - Very slow");
        clkOnSaveButton();
        executeImportScript(true);
        CSUtility.tempMethodForThreadSleep(3000);
        traverseToScriptConfiguration();
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(iframeLocatorsInstance
                        .getFrmShowingStatusOfRunningActiveScript()));
        WebElement getDiffLog = browserDriver
                .findElement(By.xpath("(//td[@class='Diff'])[3]"));
        CSUtility.waitForVisibilityOfElement(waitForReload, getDiffLog);
        if (getDiffLog.getText().startsWith("1.")) {
            CSLogger.info("Pause between step field verification passed");
        } else {
            CSLogger.error("Pause between step field verification failed");
            softAssertion.fail("Pause between step field verification failed");
        }
    }

    /**
     * This is a data provider method returns active script config data i.e
     * importScriptName, importCategory, scriptType, automationTabFields,
     * userGroupName, username, password, userRole, userAccessRight,
     * customOptionFields,count
     * 
     * @return Array String array consisting of import data
     */
    @DataProvider(name = "activeScriptTestData")
    public Object[][] getActiveScriptTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                scriptConfigTestSheet);
    }

    /**
     * This method initializes required instances for test.
     */
    @BeforeClass()
    public void initializeResource() {
        csPortalHeaderInstance = new CSPortalHeader(browserDriver);
        csPortalWidgetInstance = new CSPortalWidget(browserDriver);
        activeJobsPageInstance = SettingsLeftSectionMenubar
                .getActiveJobsNode(browserDriver);
        iframeLocatorsInstance = FrameLocators.getIframeLocators(browserDriver);
        csGuiToolbarHorizontalInstance = new CSGuiToolbarHorizontal(
                browserDriver);
        performAction = new Actions(browserDriver);
        selectionDialogWindowInstance = new SelectionDialogWindow(
                browserDriver);
        softAssertion = new SoftAssert();
        modalDialogPopupWindowInstance = new ModalDialogPopupWindow(
                browserDriver);
        userManagementPageInstance = new UserManagementPage(browserDriver);
        userManagementPopup = CSPopupDivSettings
                .getCSPopupDivLocators(browserDriver);
    }
}
