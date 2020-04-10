/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.UserManagementPage;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class checks if duplicates are present at global level
 * 
 * @author CSAutomation Team
 *
 */
public class CheckForDuplicatesAtGlobalLevelTest extends AbstractTest {

    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private FrameLocators        locator;
    private MamStudioVolumesNode mamStudioVolumesNode;
    private UserManagementPage   userManagementPage;

    /**
     * This method checks presence of duplicates at global level
     */
    @Test(priority = 1)
    public void testCheckDuplicatesAtGlobalLevel() {
        try {
            waitForReload = new WebDriverWait(browserDriver, 60);
            csPortalHeader.clkBtnSettingsTab(waitForReload);
            traverseToSystemPreferences();
            traverseToUserManagement();
            traverseToMamMenuUnderAdministrator();
            EnableCheckForDuplicatesCheckbox();
            checkPresenceOfDuplicates();
        } catch (Exception e) {
            CSLogger.debug("Test case failed.", e);
            Assert.fail("Test case failed", e);
        }
    }

    /**
     * This method traverses to System Preferences in left section pane
     */
    private void traverseToSystemPreferences() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getsettingsRightPaneMainFrame()));
        clkSystemPreferences();
    }

    /**
     * This method clicks on the System Preferences
     */
    private void clkSystemPreferences() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getSystemPreferencesNode()));
        userManagementPage.getSystemPreferencesNode().click();
        CSLogger.info("Clicked on System Preferences");
    }

    /**
     * This method traverses to user management after clicking system
     * preferences
     */
    private void traverseToUserManagement() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getsettingsRightPaneMainFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrmTree()));
        clkUserManagement();
    }

    /**
     * This method clicks userManagement tab
     */
    private void clkUserManagement() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUserManagement()));
        userManagementPage.getUserManagement().click();
        CSLogger.info("Clicked on User Management");
        clkAdministrator();
    }

    /**
     * This method clicks Administrator tab after opening user management
     */
    private void clkAdministrator() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getUserManagementRoles()));
        userManagementPage.getUserManagementRoles().click();
        CSLogger.info("Clicked on Roles under User Management");
        waitForReload.until(ExpectedConditions.visibilityOf(
                userManagementPage.getUserManagementAdministration()));
        userManagementPage.getUserManagementAdministration().click();
        CSLogger.info("Clicked on Administrator under Roles");
    }

    /**
     * This method traverses to Mam Menu which displays after optining
     * Administrator tab
     */
    private void traverseToMamMenuUnderAdministrator() {
        CSUtility.switchToDefaultFrame(browserDriver);
        traverseToMainFrame();
        clkMamInAdministrator();
        CSLogger.info("Clicked on Administrator");
    }

    /**
     * This method clicks on mam in Administrator tab
     */
    private void clkMamInAdministrator() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getMamInAdministrator()));
        userManagementPage.getMamInAdministrator().click();
        CSLogger.info("Clicked on Mam");
        clkStudioInMam();
    }

    /**
     * This method clicks studio tab which is present under Mam menu
     */
    private void clkStudioInMam() {
        checkIfCheckboxIsCheckedStudio();
    }

    /**
     * This method checks if checkbox of studio is enabled or not
     */
    private void checkIfCheckboxIsCheckedStudio() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getStudioInMam()));
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getStudioCheckbox()));
        if (!userManagementPage.getStudioCheckbox().isSelected()) {
            performClick();
            userManagementPage.getStudioCheckbox().click();
        } else {
            performClick();
        }
    }

    /**
     * This method clicks on studio in mam tab
     */
    private void performClick() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getStudioInMam()));
        userManagementPage.getStudioInMam().click();
        CSLogger.info("Clicked on studio");
    }

    /**
     * This method performs functionality of enabling checkbox under general tab
     * after clicking studio
     */
    private void EnableCheckForDuplicatesCheckbox() {
        clkGeneralTab();
        checkIfCheckboxIsEnabledForDuplicates();
    }

    /**
     * This method checks if checkbox is enabled for duplicates or not
     */
    private void checkIfCheckboxIsEnabledForDuplicates() {
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getCheckDuplicatesCheckbox()));
        if (!userManagementPage.getCheckDuplicatesCheckbox().isSelected()) {
            userManagementPage.getCheckDuplicatesCheckbox().click();
        } else
            CSLogger.info("Checkbox is already enabled");
    }

    /**
     * This method clicks General tab which is present after opening studio
     */
    private void clkGeneralTab() {
        CSUtility.switchToDefaultFrame(browserDriver);
        traverseToMainFrame();
        waitForReload.until(ExpectedConditions
                .visibilityOf(userManagementPage.getGeneralTabInMamStudio()));
        userManagementPage.getGeneralTabInMamStudio().click();
        CSLogger.info("Clicked on general tab");
    }

    /**
     * This method checks if duplicates are present n volumes or not
     */
    private void checkPresenceOfDuplicates() {
        clkVolumesInMamStudio();
    }

    private void clkVolumesInMamStudio() {
        csPortalHeader.clkBtnMedia(waitForReload);
        CSUtility.travereToMamStudio(waitForReload, browserDriver);
        waitForReload.until(ExpectedConditions
                .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
        performRightClickOnVolumesNode(
                mamStudioVolumesNode.getBtnMamVolumesNode());
    }

    /**
     * This method performs right click on volumes node to click check for
     * duplicates option
     * 
     * @param volumesNode
     */
    private void performRightClickOnVolumesNode(WebElement volumesNode) {
        CSUtility.rightClickTreeNode(waitForReload, volumesNode, browserDriver);
        clkCheckForDuplicates();
    }

    /**
     * This method clicks checkforduplicates checkbox
     */
    private void clkCheckForDuplicates() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(
                ExpectedConditions.visibilityOf(locator.getPopUpFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCsPopupDivFrame()));
        waitForReload.until(ExpectedConditions.visibilityOf(
                userManagementPage.getCheckForDuplicatesOption()));
        userManagementPage.getCheckForDuplicatesOption().click();
        verifyDuplicates();
    }

    /**
     * This method verifies if duplicates are present in Volumes node or not
     */
    private void verifyDuplicates() {
        try {
            CSUtility.switchToDefaultFrame(browserDriver);
            waitForReload.until(ExpectedConditions
                    .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrame_192MamStudio()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getCsPortalBodyFrame()));
            waitForReload
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            locator.getFrmSplitAreaFrameMain()));
            Integer totalRows = getNumberOfRows();
            // add all file names into an arraylist
            ArrayList<String> list = new ArrayList<>();
            for (int iRowCount = 3; iRowCount <= totalRows; iRowCount++) {
                waitForReload.until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath(
                                "//table[@class='hidewrap CSAdminList']/tbody/tr["
                                        + iRowCount + "]/td[5]")));
                String cellValue = browserDriver.findElement(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody[1]/tr["
                                + iRowCount + "]/td[5]"))
                        .getText();
                list.add(cellValue);
            }
            int totalFiles = list.size();
            int totalFilesWithoutDuplicates = removeDuplicatesFromArrayList(
                    list);
            if (totalFilesWithoutDuplicates < totalFiles) {
                CSLogger.info("Duplicates are present");
            } else {
                CSLogger.error("Duplicates are not present ");
                Assert.fail("Duplicates are not present ");
            }
        } catch (Exception e) {
            CSLogger.error("Could not verify presence of duplicates");

        }
    }

    /**
     * This method removes duplicates from arraylist for verification purpose to
     * check number of duplicates before and after clicking checkforduplicates
     * option
     * 
     * @param list contains object of List interface which contains names of all
     *            objects present under Volumes node
     * @return returns the size of list after removing duplicates from the
     *         arrayl ist
     */
    private int removeDuplicatesFromArrayList(ArrayList<String> list) {
        Set<String> hashSet = new LinkedHashSet<String>(list);
        List<String> ListAfterRemovingDuplicates = new ArrayList<>(hashSet);
        return ListAfterRemovingDuplicates.size();
    }

    /**
     * This method traverses to main frame
     */
    private void traverseToMainFrame() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getTopFrameInFileCategoryLeftSection()));
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getFrmFrame127()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getStudioWidgetPaneContentFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method returns number of rows present as names of files present in
     * Volumes node
     * 
     * @return
     */
    private int getNumberOfRows() {
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='hidewrap CSAdminList']")));
        WebElement productInfoTable = browserDriver.findElement(
                By.xpath("//table[@class='hidewrap CSAdminList']"));
        ArrayList<WebElement> rows = (ArrayList<WebElement>) productInfoTable
                .findElements(By.xpath(
                        "//table[@class='hidewrap CSAdminList']/tbody/tr"));
        Integer totalRows = new Integer(rows.size());
        return totalRows;
    }

    /**
     * This method initializes the resources for executing the test case
     * 
     * @throws InterruptedException
     */
    @BeforeClass
    public void initializeResources() throws InterruptedException {
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        userManagementPage = new UserManagementPage(browserDriver);
    }
}
