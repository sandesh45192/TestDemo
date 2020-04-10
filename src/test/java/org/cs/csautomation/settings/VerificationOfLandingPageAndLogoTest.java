/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.settings;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSGuiToolbarHorizontal;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.settings.CSPortalWidget;
import org.cs.csautomation.cs.settings.SearchPage;
import org.cs.csautomation.cs.settings.SettingsLeftSectionMenubar;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivPim;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.cs.csautomation.cs.utility.TraversingForSearchModule;
import org.cs.csautomation.cs.utility.TraversingForSettingsModule;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class contains the test method to verify landing page & landing page
 * logo
 * 
 * @author CSAutomation Team
 *
 */
public class VerificationOfLandingPageAndLogoTest extends AbstractTest {

    private WebDriverWait          waitForReload;
    private FrameLocators          locator;
    private CSPortalHeader         csPortalHeader;
    private CSGuiToolbarHorizontal guiToolbarHorizontal;
    private String                 landingPageSheet = "landingPageData";
    private SearchPage             searchPage;
    private CSPopupDivPim          popupDiv;
    private CSPortalWidget         portalWidget;
    private MamStudioVolumesNode   mamStudioVolumesNode;

    /**
     * This is the test method which drives the usecase, ie. Verification of
     * landing page & landing page logo
     * 
     * @param imagePath - String instance - path of landing page logo
     * @param imageName - String instance - name of landing page logo
     */
    @Test(dataProvider = "landingPageLogo")
    public void testVerificationOfLandingPageAndLogo(String imagePath,
            String imageName) {
        try {
            createTestData(imagePath, imageName);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    searchPage.getImgAssignedLandingPageLogo());
            String src = searchPage.getImgAssignedLandingPageLogo()
                    .getAttribute("src");
            csPortalHeader.clkSearchHeader(waitForReload);
            CSUtility.tempMethodForThreadSleep(2000);
            TraversingForSearchModule.frameTraversingForCollectionsPopup(
                    waitForReload, browserDriver);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    searchPage.getImgLandingPageLogo());
            String sourceOfImage = searchPage.getImgLandingPageLogo()
                    .getAttribute("src");
            if (sourceOfImage != null)
                CSLogger.info(
                        "Verification of landing page & landing page logo "
                                + "test completed!");
            else {
                CSLogger.error("Verification failed for landing page logo");
            }
        } catch (Exception e) {
            CSLogger.debug(
                    "Automation Error : testVerificationOfLandingPageAndLogo",
                    e);
            Assert.fail(
                    "Automation error : testVerificationOfLandingPageAndLogo",
                    e);
        }
    }

    /**
     * This method will verify whether landing page logo is configured or not.
     * If Logo is not configured, it will upload custom image & assign it as the
     * landing page logo
     * 
     * @param imagePath - String instance - path of image to be uploaded as the
     *            landing page logo
     * @param imageName - String instance - name of image to be uploaded as the
     *            landing page logo
     */
    private void createTestData(String imagePath, String imageName) {
        openSearchPortal();
        searchPage.clkWebElement(waitForReload,
                searchPage.getSearchPortalSettingsNode());
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        searchPage.clkWebElement(waitForReload, searchPage.getCoreNode());
        searchPage.clkWebElement(waitForReload,
                searchPage.getShareSearchPortal());
        CSLogger.info("clicked on search portal inside core");
        CSUtility.tempMethodForThreadSleep(2000);
        boolean logoThumbnail = isElementPresent(By.xpath(
                "//div[contains(@name,'LandingPageLogo')]//img[@class='CSGuiSelectionListAdd']"));
        if (logoThumbnail) {
            try {
                uploadLandingPageLogo(imagePath, imageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CSLogger.info("landing page logo is already configured.");
        }
    }

    /**
     * This method will upload landing page logo from system into application
     * 
     * @param imagePath - String instance - path of landing page logo image
     * @param imageName - String instance - name of landing page logo image
     */
    private void uploadLandingPageLogo(String imagePath, String imageName) {
        searchPage.clkWebElement(waitForReload,
                searchPage.getImgToAddLandingPageLogo());
        traverseToFrameToUploadLandingPageLogo();
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnUploadFile());
        CSUtility.tempMethodForThreadSleep(4000);
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSubCSPortalWindow()));
        searchPage.clkWebElement(waitForReload, searchPage.getTxtBrowseFiles());
        imagePath = getFilePath(imagePath);
        try {
            addFilePath(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        waitForReload.until(ExpectedConditions.elementToBeClickable(
                mamStudioVolumesNode.getUploadButtonImage()));
        searchPage.clkWebElement(waitForReload,
                mamStudioVolumesNode.getUploadButtonImage());
        CSUtility.tempMethodForThreadSleep(2000);
        searchPage.clkWebElement(waitForReload,
                mamStudioVolumesNode.getClkCloseAfterUpload());
        CSLogger.info("Logo image uploaded successfully.");
        CSUtility.tempMethodForThreadSleep(2000);
        traverseToFrameToUploadLandingPageLogo();
        guiToolbarHorizontal.clkBtnCSGuiToolbarRefresh(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        searchPage.clkWebElement(waitForReload,
                guiToolbarHorizontal.getBtnFilter());
        searchPage.clkWebElement(waitForReload,
                searchPage.getTxtBoxFilterByFileName());
        searchPage.getTxtBoxFilterByFileName().sendKeys(imageName);
        searchPage.getTxtBoxFilterByFileName().sendKeys(Keys.ENTER);
        getLogo(imageName);
        popupDiv.askBoxWindowOperation(waitForReload, true, browserDriver);
        TraversingForSettingsModule.traverseToMainFrame(waitForReload,
                browserDriver);
        guiToolbarHorizontal.clkBtnCSGuiToolbarSave(waitForReload);
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("saved after uploading & assigning custom image as "
                + "landing page logo");
    }

    /**
     * This method will traverse frames to access button for uploading landing
     * page logo
     */
    private void traverseToFrameToUploadLandingPageLogo() {
        CSUtility.switchToDefaultFrame(browserDriver);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmCreateNewClass()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmObjectFolderInFileSelectionDialog()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmObjectCenterInFileSelectionDialog()));
        CSLogger.info("Switched to frame of upload file button in file "
                + "selection dialog");
    }

    /**
     * This method get the file path of current system
     * 
     * @param filePath String object contains path from sheet
     * @return path String contains current file path
     */
    private String getFilePath(String filePath) {
        String currentPath = System.getProperty("user.dir");
        String path = currentPath + filePath;
        return path;
    }

    /**
     * This method will add the logo present at file path
     * 
     * @param filePath - String instance - path of landing page logo image
     * 
     */
    private void addFilePath(String filePath) throws Exception {
        StringSelection select = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(select,
                null);
        CSUtility.tempMethodForThreadSleep(2000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        CSUtility.tempMethodForThreadSleep(2000);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        CSLogger.info("Image added to the test folder");
    }

    /**
     * This method will get the logo for landing page
     */
    private void getLogo(String imageName) {
        boolean status = false;
        List<WebElement> list = browserDriver.findElements(By.xpath(
                "//table[@class='hidewrap CSAdminList']/tbody/tr/td[2]"));
        WebElement listElement = null;
        for (int i = 0; i < list.size(); i++) {
            listElement = list.get(i);
            if (listElement.getText().equals(imageName)) {
                status = true;
                break;
            }
        }
        if (status == true) {
            listElement.click();
            CSLogger.info("Clicked on logo");
        } else {
            CSLogger.error("Could not click on logo");
        }
    }

    /**
     * This method will open 'search portal'
     */
    private void openSearchPortal() {
        csPortalHeader.clkBtnSettingsTab(waitForReload);
        TraversingForSettingsModule.traverseToCsPortalWidgetFrames(
                waitForReload, browserDriver, locator);
        portalWidget.clkOnSystemPreferencesIcon(waitForReload);
        TraversingForSettingsModule.traverseToFrameOfSettingsLeftPaneTree(
                waitForReload, browserDriver, locator);
        searchPage.clkWebElement(waitForReload, searchPage.getSearchNode());
        CSLogger.info("Clicked on search node");
        searchPage.clkWebElement(waitForReload,
                searchPage.getNodeSearchPortal());
        CSLogger.info("Clicked on option 'Search Portal'");
    }

    /**
     * This method will check whether element is present on webpage or not
     * 
     * @param locatorKey - locator of element
     * @return boolean true if element is present, false is element is not
     *         present
     */
    public boolean isElementPresent(By locatorKey) {
        try {
            browserDriver.findElement(locatorKey);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * This method initializes all the required instances for test
     */
    @BeforeClass
    public void initializeResources() {
        csPortalHeader = new CSPortalHeader(browserDriver);
        locator = FrameLocators.getIframeLocators(browserDriver);
        waitForReload = new WebDriverWait(browserDriver, 60);
        guiToolbarHorizontal = new CSGuiToolbarHorizontal(browserDriver);
        portalWidget = new CSPortalWidget(browserDriver);
        popupDiv = CSPopupDivPim.getCSPopupDivLocators(browserDriver);
        searchPage = SettingsLeftSectionMenubar.getSearchNode(browserDriver);
        mamStudioVolumesNode = new MamStudioVolumesNode(browserDriver);
    }

    /**
     * This data provider contains path & name of landing page logo
     * 
     * @return landingPageSheet - Array of landing page logo path & name
     */
    @DataProvider(name = "landingPageLogo")
    public Object[][] getDataForLandingPageLogo() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("settingsTestCases"),
                landingPageSheet);
    }
}
