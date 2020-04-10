
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.TraverseSelectionDialogFrames;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelectionDialogWindow {

    private WebDriver  browserDriverInstance;

    @FindBy(xpath = "//div[@class='ControlPaneButton'][1]")
    private WebElement btnControlPaneButtonProductsFolder;

    @FindBy(xpath = "//div[@class='ControlPaneButton'][1]")
    private WebElement btnControlPaneButtonUserFolder;

    @FindBy(xpath = "//a/img[contains(@src,'folder.recursive.true.png')]")
    private WebElement btnCSGuiToolbarListSubFolders;

    @FindBy(xpath = "//div[@class='ControlPaneButton'][2]")
    private WebElement btnControlPaneButtonFileFolder;

    @FindBy(xpath = "//a/img[contains(@src,'folder.recursive.false.png')]")
    private WebElement btnCSGuiToolbarDoNotListSubFolders;

    @FindBy(xpath = "//a/img[contains(@src,'filter.gif')]")
    private WebElement btnCSGuiToolbarFilterBar;

    @FindBy(id = "Configurations@0")
    private WebElement drpDwnAttributes;

    @FindBy(xpath = "//select[@name='StateID']")
    private WebElement drpdwnStateID;

    public SelectionDialogWindow(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info(
                "Initialized  Selection Dialog Window page POM elements.");
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
     * Gets the browser driver instance
     * 
     * @return
     */
    private WebDriver getBrowserDriverInstance() {
        return browserDriverInstance;
    }

    /**
     * Returns the webElement btnControlPaneButtonProductsFolder
     * 
     * @return btnControlPaneButtonProductsFolder
     */
    public WebElement getBtnControlPaneButtonProductsFolder() {
        return btnControlPaneButtonProductsFolder;
    }

    /**
     * Returns the webElement btnControlPaneButtonUserFolder
     * 
     * @return btnControlPaneButtonUserFolder
     */
    public WebElement getBtnControlPaneButtonUserFolder() {
        return btnControlPaneButtonUserFolder;
    }

    /**
     * Returns the webElement btnCSGuiToolbarListSubFolders
     * 
     * @return btnCSGuiToolbarListSubFolders
     */
    public WebElement getBtnCSGuiToolbarListSubFolders() {
        return btnCSGuiToolbarListSubFolders;
    }

    /**
     * Returns the webElement btnControlPaneButtonFileFolder
     * 
     * @return btnControlPaneButtonFileFolder
     */
    public WebElement getBtnControlPaneButtonFilesFolder() {
        return btnControlPaneButtonFileFolder;
    }

    /**
     * Returns the webElement btnCSGuiToolbarDoNotListSubFolders
     * 
     * @return btnCSGuiToolbarDoNotListSubFolders
     */
    public WebElement getBtnCSGuiToolbarDoNotListSubFolders() {
        return btnCSGuiToolbarDoNotListSubFolders;
    }

    /**
     * Returns the webElement btnCSGuiToolbarFilterBar
     * 
     * @return btnCSGuiToolbarFilterBar
     */
    public WebElement getBtnCSGuiToolbarFilterBar() {
        return btnCSGuiToolbarFilterBar;
    }

    /**
     * Returns the webElement drpDwnAttributes that is located in Data Selection
     * Dialog window
     * 
     * @return drpDwnAttributes
     */
    public WebElement getDrpDwnAttributes() {
        return drpDwnAttributes;
    }

    /**
     * Returns the webElement drpDwnStateID that is located in Data Selection
     * Dialog window
     * 
     * @return drpDwnStateID
     */
    public WebElement getDrpDwnStateID() {
        return drpdwnStateID;
    }

    /**
     * This method clicks on product folder from the data selection dialog
     * window
     * 
     * @param waitForReload
     *            WebDriverWait object
     * @param browserDriverInstance
     *            browser driver instance
     */
    public void clkBtnControlPaneButtonProductsFolder(
            WebDriverWait waitForReload, WebDriver browserDriverInstance) {
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnControlPaneButtonProductsFolder());
        getBtnControlPaneButtonProductsFolder().click();
        CSLogger.info("Clicked on Control Pane Button Products Folder");
    }

    /**
     * This method clicks on User folder from the data selection dialog window
     * 
     * @param waitForReload
     *            WebDriverWait object
     * @param browserDriverInstance
     *            browser driver instance
     */
    public void clkBtnControlPaneButtonUserFolder(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnControlPaneButtonUserFolder());
        getBtnControlPaneButtonUserFolder().click();
        CSLogger.info("Clicked on Control Pane Button Products Folder");
    }

    /**
     * This method clicks on file folder from the file selection dialog window
     * 
     * @param waitForReload
     *            WebDriverWait object
     * @param browserDriverInstance
     *            browser driver instance
     */
    public void clkBtnControlPaneButtonFilesFolder(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        TraverseSelectionDialogFrames.traverseToCsPortalWindowContentFrame(
                waitForReload, browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnControlPaneButtonFilesFolder());
        getBtnControlPaneButtonFilesFolder().click();
        CSLogger.info("Clicked on Control Pane Button Files Folder");
    }

    /**
     * Clicks on ListSubFolders from file selection dialog window
     * 
     * @param waitForReload
     *            WebDriverWait object browser driver instance
     */
    public void clkBtnCSGuiToolbarListFileSubFolders(
            WebDriverWait waitForReload) {
        TraverseSelectionDialogFrames.traverseTillFileFoldersCenterPane(
                waitForReload, browserDriverInstance);
        clkOnListSubFolder(waitForReload);
    }

    /**
     * Clicks on ListSubFolders from data selection dialog window
     * 
     * @param waitForReload
     *            WebDriverWait object
     */
    public void clkBtnCSGuiToolbarListUserSubFolders(
            WebDriverWait waitForReload) {
        TraverseSelectionDialogFrames.traverseTillUserFoldersCenterPane(
                waitForReload, browserDriverInstance);
        clkOnListSubFolder(waitForReload);
    }

    /**
     * This method performs action of clicking on list sub folder webElement
     * 
     * @param waitForReload
     *            WebDriverWait object
     */
    public void clkOnListSubFolder(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnCSGuiToolbarFilterBar());
        if (browserDriverInstance
                .findElements(By
                        .xpath("//a/img[contains(@src,'folder.recursive.false.png')]"))
                .isEmpty()) {
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    getBtnCSGuiToolbarListSubFolders());
            getBtnCSGuiToolbarListSubFolders().click();
            CSLogger.info("clicked on List Subfolder");
        } else {
            CSLogger.info("Already clicked on list sub folders");
        }
    }
    
    /**
     * Clicks on given web element.
     * 
     * @param waitForReload WebDriverWait object
     * @param element WebElement element on which click operation will be
     *            performed.
     */
    public void clkOnGivenWebElement(WebDriverWait waitForReload,
            WebElement element) {
        CSUtility.waitForVisibilityOfElement(waitForReload, element);
        element.click();
        CSLogger.info("Clicked on given element");
    }
}
