/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.mam;

import org.cs.csautomation.cs.mam.IVolumePopup;
import org.cs.csautomation.cs.mam.MamStudioVolumesNode;
import org.cs.csautomation.cs.pom.CSPortalHeader;
import org.cs.csautomation.cs.skeletons.AbstractTest;
import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSPopupDivMam;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This test class contains test method to test proof reader(whiteboarding)
 * 
 * @author CSAutomation Team
 *
 */
public class ProofReadingNoteTest extends AbstractTest {

    private String               proofReadingNotesSheet = "ProofReadingNoteTest";
    private CSPortalHeader       csPortalHeader;
    private WebDriverWait        waitForReload;
    private MamStudioVolumesNode mamStudioVolumesNode;
    private CSPopupDivMam        csPopupDiv;
    private FrameLocators        locator;
    private IVolumePopup         demoVolumePopup;
    Actions                      actions;

    /**
     * This method test the assign CS notes to WBD files test case.
     */
    @Test(dataProvider = "proofReaderForNote", groups = { "ProofReadingNotes" })
    public void testProofReadingNotes(String filePath) {
        try {
            String sampleText = "Automation test text.";
            String nodes[] = filePath.split("//");
            switchToMAM(waitForReload);
            waitForReload.until(ExpectedConditions
                    .visibilityOf(mamStudioVolumesNode.getBtnMamVolumesNode()));
            traverseToIndesignFile(nodes);
            createWhiteBoardFromIndd(nodes[nodes.length - 1]);
            switchToMAM(waitForReload);
            traverseToIndesignFile(nodes);
            clickRefreshOnParent(nodes[nodes.length - 2]);
            // Sleep of 5 min to wait for synchronization of wdb file.
            CSUtility.tempMethodForThreadSleep(300000);
            switchToMAM(waitForReload);
            traverseToIndesignFile(nodes);
            mamStudioVolumesNode.expandNodesByVolumesName(waitForReload,
                    nodes[nodes.length - 1].replace(".indd", ".wbd"));
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            clickOnNoteIcon();
            switchToSplitAreaFrame();
            createNote(actions);
            CSUtility.tempMethodForThreadSleep(2000);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            switchToNotesEditorFrame();
            enterValueInNote(sampleText);
            saveNote();
            switchToMAM(waitForReload);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            switchToSplitAreaFrame();
            hoverOnNote(actions);
            verifyNoteValue(sampleText);
            CSLogger.info("Execution of Proof reading notes test completed");
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method test the edit CS notes to WBD files test case.
     */
    @Test(
            dependsOnMethods = { "testProofReadingNotes" },
            groups = { "ProofReadingNotes" })
    public void testProofReadingNotesEdit() {
        try {
            String sampleText = "Automation edit text.";
            Actions actions = new Actions(browserDriver);
            switchToMAM(waitForReload);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            switchToNotesEditorFrame();
            enterValueInNote(sampleText);
            saveNote();
            switchToMAM(waitForReload);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            switchToSplitAreaFrame();
            hoverOnNote(actions);
            verifyNoteValue(sampleText);
            CSLogger.info("Proof reading notes edit test completed");
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method test the delete CS notes to WBD files test case.
     */
    @Test(
            dependsOnMethods = { "testProofReadingNotesEdit" },
            groups = { "ProofReadingNotes" })
    public void testProofReadingNotesDelete() {
        try {
            switchToMAM(waitForReload);
            CSUtility.traverseToSettingsConfigurationMam(waitForReload,
                    browserDriver);
            switchToSplitAreaFrame();
            contextClickOnNote(actions);
            Alert alert = CSUtility.getAlertBox(waitForReload, browserDriver);
            Assert.assertEquals(alert.getText(),
                    "Do you really want to delete this note?");
            alert.accept();
            WebElement noteElement = null;
            try {
                noteElement = browserDriver.findElement(
                        By.xpath("//div[contains(@id,'CSNote_')]"));
            } catch (NoSuchElementException e) {
                CSLogger.info(
                        "Verified.Note was not found after delete opertaion.");
            }
            Assert.assertNull(noteElement, "Note was not deleted");
            CSLogger.info("proof reading notes delete test completed");
        } catch (Exception e) {
            CSLogger.debug("Test case failed." + e);
            Assert.fail("Test case failed.", e);
        }
    }

    /**
     * This method clicks on the save button for Note.
     */
    private void saveNote() {
        WebElement noteEditorSaveElement = browserDriver
                .findElement(By.xpath("//input[@id='submitbutton']"));
        noteEditorSaveElement.click();
        CSLogger.info("CLicked on the Save button of note.");
    }

    /**
     * This method inserts value in the note textbox.
     * 
     * @param sampleText String object containing the value for note.
     */
    private void enterValueInNote(String sampleText) {
        WebElement noteEditorElement = browserDriver
                .findElement(By.xpath("//textarea[@id='notetextinput']"));
        noteEditorElement.sendKeys(sampleText);
        CSLogger.info("Entered value " + sampleText + " in the note.");
    }

    /**
     * This method perform right click on the folder Templates-CS and selects
     * the refresh option from the pop-up menu.
     */
    private void clickRefreshOnParent(String parentNode) {
        WebElement inddFileParentElement = browserDriver
                .findElement(By.xpath("//span[text()=' " + parentNode + "']"));
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
                By.xpath("//span[text()=' " + inddFileName + "']")));
        WebElement inddFileElement = browserDriver.findElement(
                By.xpath("//span[text()=' " + inddFileName + "']"));
        CSUtility.rightClickTreeNode(waitForReload, inddFileElement,
                browserDriver);
        csPopupDiv.selectPopupDivMenu(waitForReload,
                csPopupDiv.getProcessOption(), browserDriver);
        CSUtility.tempMethodForThreadSleep(1000);
        csPopupDiv.selectPopupDivSubMenu(waitForReload,
                csPopupDiv.getCsPopupCreateWhiteboard(), browserDriver);
        CSLogger.info("Clicked on the Create Whiteboard from context click.");
    }

    /**
     * This method clicks on the textArea of the Note.
     */
    private void clickOnNoteIcon() {
        waitForReload.until(ExpectedConditions.elementToBeClickable(By
                .xpath("// div[@id='CSTabbedPane_Toolbar_1']//tr[11]//td[1]")));
        WebElement noteWebElement = browserDriver.findElement(By
                .xpath("// div[@id='CSTabbedPane_Toolbar_1']//tr[11]//td[1]"));
        noteWebElement.click();
        CSLogger.info("Clicked on the note.");
    }

    /**
     * This method drags the cursor on wdb file to create the note.
     * 
     * @param actions Actions Object
     */
    private void createNote(Actions actions) {
        WebElement elementOnToCreateNote = browserDriver
                .findElement(By.xpath("//img[@id='preview_001']"));
        actions.moveToElement(elementOnToCreateNote)
                .dragAndDropBy(elementOnToCreateNote, 30, 0).build().perform();
        CSLogger.info("Created note in the wdb file.");
    }

    /**
     * This method Asserts the value of note.
     * 
     * @param sampleText String object containing value to verify with the value
     *            of the note.
     */
    private void verifyNoteValue(String sampleText) {
        CSUtility.switchToDefaultFrame(browserDriver);
        WebElement tooltipElement = browserDriver
                .findElement(By.xpath("//div[@id='CSTooltipText']"));
        Assert.assertTrue(tooltipElement.getText().trim().contains(sampleText),
                "Test case failed as Value from Note didn't matched");
    }

    /**
     * This method perform mouse hover operation.
     * 
     * @param actions Action Object
     */
    private void hoverOnNote(Actions actions) {
        actions.moveToElement(browserDriver
                .findElement(By.xpath("//div[contains(@id,'CSNote_')]")))
                .build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("Hovered on the note");
    }

    /**
     * This method perform context click operation on note.
     * 
     * @param actions Action Object
     */
    private void contextClickOnNote(Actions actions) {
        actions.moveToElement(browserDriver
                .findElement(By.xpath("//div[contains(@id,'CSNote_')]")))
                .contextClick().build().perform();
        CSUtility.tempMethodForThreadSleep(2000);
        CSLogger.info("Performed context clicked on the note.");
    }

    /**
     * This method traverse to FrmMamNotesEditor frame.
     */
    private void switchToNotesEditorFrame() {
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getCsSideBarBodyFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmMamNotesEditor()));
        CSLogger.info("Switched to the Notes Editor Frame.");
    }

    /**
     * This method traverse to the splitAreaFrame
     */
    private void switchToSplitAreaFrame() {
        waitForReload
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameFrmCenter()));
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

    @DataProvider(name = "proofReaderForNote")
    public Object[] CreateClassTestData() {
        return DrivenScript.readSheetData(
                config.getExcelSheetPath("mamModuleTestCases"),
                proofReadingNotesSheet);
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
        actions = new Actions(browserDriver);
    }
}
