
package org.cs.csautomation.cs.mam;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MamStudioVolumesNode {

    private WebDriver browserDriverInstance;

    public MamStudioVolumesNode(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized MamStudioVolumes page.");
    }

    @FindBy(id = "Volumes@0")
    private WebElement btnMamVolumesNode;

    @FindBy(id = "CSPopupDiv")
    private WebElement volumePopUpDiv;

    @FindBy(xpath = "//iframe[@id='CSPopupDivFrame']")
    private WebElement productPopUpFrame;

    @FindBy(
            xpath = "//span[@class='TreeBackground1']//span[@class='TreeBackground1']//span[@class='TreeBackground1']//span[@class='TreeBackground1']//span//table//tbody//tr//td//span//table[@class='treeline']//tbody//tr//td//a//span//span")
    private WebElement createdFileFolder;

    @FindBy(xpath = "//td[contains(text(),'Synchronize Folder')]")
    private WebElement optionSynchronizeFolder;

    @FindBy(xpath = "//td[contains(text(),'Upload New File')]")
    private WebElement optionUploadNewFile;

    @FindBy(xpath = "//span[contains(text(),'browse files')]")
    private WebElement browseFileToUpload;

    @FindBy(
            xpath = "//span[@class='CSGuiUploadButtonStartText CSGuiUploadButtonText']")
    private WebElement uploadButtonForImage;

    @FindBy(xpath = "//span[contains(.,'Close')]")
    private WebElement btnCloseAfterUpload;

    @FindBy(xpath = "//span[contains(@id,'MamfileLocalPath')]")
    private WebElement txtFileLocalPath;

    /**
     * returns the instance of close button after image upload
     * 
     * @return clkCloseAfterUpload
     */
    public WebElement getClkCloseAfterUpload() {
        return btnCloseAfterUpload;
    }

    /**
     * returns the instance of upload button for uploading images
     * 
     * @return uploadButtonForImage
     */
    public WebElement getUploadButtonImage() {
        return uploadButtonForImage;
    }

    /**
     * returns th instance of browse file option for browsing files from local
     * machine
     * 
     * @return browseFileToUpload
     */
    public WebElement getBrowseFileToUpload() {
        return browseFileToUpload;
    }

    /**
     * returns the instance of upload new file after right clicking on folder
     * 
     * @return clkUploadNewFile
     */
    public WebElement getClkUploadNewFile() {
        return optionUploadNewFile;
    }

    /**
     * returns the instance of synchronize folder after right clicking on demo
     * volume folder
     * 
     * @return clkSynchronizefolder
     */
    public WebElement getClkSynchronizedFolder() {
        return optionSynchronizeFolder;
    }

    /**
     * This method gets the instance of file which is created under Demo volume
     * folder
     * 
     * @return
     */
    public WebElement getCreatedFileUnderDemoFolder() {
        return createdFileFolder;
    }

    /**
     * this method returns instance of frame while switching on pop up of demo
     * volume creation
     * 
     * @return volumePopUpDiv
     */
    public WebElement getProductPopUpDiv() {
        return volumePopUpDiv;
    }

    /**
     * this method returns instance of frame while switching on pop up of demo
     * volume creation
     * 
     * @return volumePopUpDiv
     */
    public WebElement getVolulmePopUpFrame() {
        return volumePopUpDiv;
    }

    /**
     * this method returns instance of Volumes Node
     * 
     * @return btnMamVolumesNode
     */
    public WebElement getBtnMamVolumesNode() {
        return btnMamVolumesNode;
    }

    /**
     * this method returns instance of file local path
     * 
     * @return txtFileLocalPath
     */
    public WebElement getFileLocalPath() {
        return txtFileLocalPath;
    }

    /**
     * Sets instance of browserDriver
     * 
     * @param browserDriverInstance
     */
    private void setBrowserDriverInstance(WebDriver browserDriverInstance) {
        this.browserDriverInstance = browserDriverInstance;
    }

    /**
     * This method traverses upto main frame in right section pane
     */
    public void traverseUptoMainFrame() {
        WebDriverWait waitForReload = new WebDriverWait(browserDriverInstance,
                60);
        FrameLocators locator = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(locator.getTopFrame()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrame_192MamStudio()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getModuleContentFrameMam()));
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                locator.getFrmSplitAreaFrameMain()));
    }

    /**
     * This method expands nodes by volume name
     * 
     * @param waitForReload contains the waiting time to reload element
     * @param folderName contains the name of the folder
     * @return
     */
    public WebElement expandNodesByVolumesName(WebDriverWait waitForReload,
            String folderName) {
        WebElement element;
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'" + folderName + "')]")));
        element = browserDriverInstance.findElement(
                By.xpath("//span[contains(text(),'" + folderName + "')]"));
        element.click();
        CSLogger.info("Clicked on product node " + folderName);
        return element;
    }

    /**
     * This method click on link Browser File To Upload
     * 
     * @param waitForReload contains the waiting time to reload element
     */
    public void clkOnBrowseFileToUpload(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getBrowseFileToUpload()));
        getBrowseFileToUpload().click();
        CSLogger.info("Click on Browser File To Upload");
    }

    /**
     * This method click on Upload Button
     * 
     * @param waitForReload contains the waiting time to reload element
     */
    public void clkOnUploadButtonImage(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getUploadButtonImage()));
        getUploadButtonImage().click();
        CSLogger.info("Click on Upload Button");
    }

    /**
     * This method click on Button Close After Upload
     * 
     * @param waitForReload contains the waiting time to reload element
     */
    public void clkOnBtnCloseAfterUpload(WebDriverWait waitForReload) {
        waitForReload.until(ExpectedConditions
                .elementToBeClickable(getClkCloseAfterUpload()));
        getClkCloseAfterUpload().click();
        CSLogger.info("Click on Button Close After Upload");
    }
}