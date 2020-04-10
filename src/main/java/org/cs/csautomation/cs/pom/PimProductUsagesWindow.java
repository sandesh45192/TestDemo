package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PimProductUsagesWindow {

    private WebDriver browserDriverInstance;

    @FindBy(xpath = "//td[@class='CSPortalGuiListGroupItemFirstCell'][contains(text(),'Usage in')]")
    private WebElement csPortalGuiListUsagesInTree;

    @FindBy(xpath = "(//a[@class='CSPortalGuiListItemDrag'][contains(text(),'Channel')])[1]")
    private WebElement lblCsPortalGuiListChannel;

    @FindBy(xpath = "//input[@id='toolbarSearchInput']")
    private WebElement txtToolbarSearchInput;

    public PimProductUsagesWindow(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized PimProductUsagesWindow page POM elements.");
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
     * Returns the WebElement csPortalGuiListUsagesInTree
     * 
     * @return csPortalGuiListUsagesInTree
     */
    public WebElement getCsPortalGuiListUsagesInTree() {
        return csPortalGuiListUsagesInTree;
    }

    /**
     * Returns the WebElement lblCsPortalGuiListChannel
     * 
     * @return lblCsPortalGuiListChannel
     */
    public WebElement getLblCsPortalGuiListChannel() {
        return lblCsPortalGuiListChannel;
    }

    /**
     * Returns the WebElement txtToolbarSearchInput
     * 
     * @return txtToolbarSearchInput
     */
    public WebElement getTxtToolbarSearchInput() {
        return txtToolbarSearchInput;
    }

    /**
     * Clicks on CsPortalGuiListUsagesIn Tree
     * 
     * @param waitForReload
     *            WebDriverWait object
     */
    public void clkOnCsPortalGuiListUsagesInTree(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getCsPortalGuiListUsagesInTree());
        getCsPortalGuiListUsagesInTree().click();
        CSLogger.info("Clicked on CsPortalGuiListUsagesIn Tree ");
    }

    /**
     * Clicks on LblCsPortalGuiList Channel
     * 
     * @param waitForReload
     *            WebDriverWait object
     */
    public void clkOnLblCsPortalGuiListChannel(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getLblCsPortalGuiListChannel());
        getLblCsPortalGuiListChannel().click();
        CSLogger.info("Clicked on LblCsPortalGuiListChannel ");
    }

    /**
     * Enters text in search box
     * 
     * @param waitForReload
     *            WebDriverWait object
     * @param inputData
     *            value to be searched
     */
    public void enterTextInSearchInput(WebDriverWait waitForReload,
            String inputData) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getTxtToolbarSearchInput());
        getTxtToolbarSearchInput().sendKeys(inputData);
        getTxtToolbarSearchInput().sendKeys(Keys.ENTER);
        CSLogger.info("Entered text : " + inputData + " in searchInput");
    }
}
