
package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.cs.csautomation.cs.utility.FrameLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CSPortalWindow {

    WebDriver          browserDriverInstance;

    @FindBy(xpath = "//td[contains(text(),'Cross Reference')]")
    private WebElement ddCSPortalGuiListCrossReference;

    @FindBy(xpath = "//a[contains(text(),'Reference to PIM Product')]")
    private WebElement lblReferenceToPimProduct;

    @FindBy(xpath = "//a[contains(text(),'Reference to MAM File')]")
    private WebElement lblReferenceToMamFile;

    @FindBy(xpath = "//a[contains(text(),'Reference to CORE User')]")
    private WebElement lblReferenceToCoreUser;

    @FindBy(xpath = "//div[@id='AttributeDescription']/h1")
    private WebElement lblCSTypeTitle;

    @FindBy(xpath = "//td[contains(text(),'Special Format')]")
    private WebElement drpdwnCSPortalGuiListSpecialFormat;

    @FindBy(xpath = "//a[contains(text(),'Flex Table')]")
    private WebElement lblFlexTable;

    public CSPortalWindow(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized CSPortalWindow page POM elements.");
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
     * Returns the WebElement lblFlexTable
     * 
     * @return lblFlexTable CSPortalWindow
     */
    public WebElement getLblFlexTable() {

        return lblFlexTable;
    }

    /**
     * Returns the WebElement drpdwnCSPortalGuiListSpecialFormat
     * 
     * @return drpdwnCSPortalGuiListSpecialFormat CSPortalWindow
     */
    public WebElement getDrpdwnCSPortalGuiListSpecialFormat() {
        return drpdwnCSPortalGuiListSpecialFormat;
    }

    /**
     * Returns the WebElement lblCSTypeTitle
     * 
     * @return lblCSTypeTitle CSPortalWindow
     */
    public WebElement getLblCSTypeTitle() {
        return lblCSTypeTitle;
    }

    /**
     * Returns the WebElement ddCSPortalGuiListCrossReference
     * 
     * @return ddCSPortalGuiListCrossReference CSPortalWindow
     */
    public WebElement getDdCSPortalGuiListCrossReference() {
        return ddCSPortalGuiListCrossReference;
    }

    /**
     * Returns the WebElement lblReferenceToMamFile
     * 
     * @return lblReferenceToMamFile CSPortalWindow
     */
    public WebElement getLblReferenceToMamFile() {
        return lblReferenceToMamFile;
    }

    /**
     * Returns the WebElement lblReferenceToPimProduct
     * 
     * @return lblReferenceToPimProduct CSPortalWindow
     */
    public WebElement getlblReferenceToPimProduct() {

        return lblReferenceToPimProduct;
    }

    /**
     * Returns the WebElement as per the type of the attribute.
     * 
     * @param String attributeType contains the type of the selected attribute
     * @return object of CSPortalWindow
     */
    public WebElement getAttributeType(String attributeType) {
        switch (attributeType) {
        case "Reference to PIM Product":
            return getlblReferenceToPimProduct();
        case "Reference to MAM File":
            return getLblReferenceToMamFile();
        case "Reference to CORE User":
            return getLblReferenceToCoreUser();
        default:
            return getlblReferenceToPimProduct();
        }
    }

    /**
     * Returns the WebElement lblReferenceToCoreUser
     * 
     * @return lblReferenceToCoreUser CSPortalWindow
     */
    public WebElement getLblReferenceToCoreUser() {
        return lblReferenceToCoreUser;
    }

    /**
     * This method selects the Attribute Type as Cross Reference
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void
            clkDdCSPortalGuiListCrossReference(WebDriverWait waitForReload) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        waitForReload
                .until(ExpectedConditions.visibilityOf(getLblCSTypeTitle()));
        CSLogger.info("Found Title Single line text");
        if (getLblCSTypeTitle().getText().equals("Single-line Text")) {
            CSUtility.switchToDefaultFrame(browserDriverInstance);
            CSUtility.switchToSplitAreaFrameLeft(waitForReload,
                    browserDriverInstance);
            CSUtility.waitForVisibilityOfElement(waitForReload,
                    getDdCSPortalGuiListCrossReference());
            CSLogger.info(
                    "Waiting Completed for visibilty of cross Reference.");
            getDdCSPortalGuiListCrossReference().click();
            CSLogger.info("clicked on cross Reference");
        } else {
            CSUtility.switchToDefaultFrame(browserDriverInstance);
            CSUtility.switchToSplitAreaFrameLeft(waitForReload,
                    browserDriverInstance);
            CSLogger.info("Traversed Till Spilt Area Frame left");
        }
    }

    public void clkLblFlexTable(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getLblFlexTable());
        getLblFlexTable().click();
        CSLogger.info("Clicked on Flex Table attribute type");
    }

    /**
     * This method click on dropdown Special Format
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void
            clkDrpDwnCSPortalGuiListSpecialFormat(WebDriverWait waitForReload) {
        FrameLocators iframeLocatorsInstance = FrameLocators
                .getIframeLocators(browserDriverInstance);
        CSUtility.switchToDefaultFrame(browserDriverInstance);
        waitForReload.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                iframeLocatorsInstance.getFrmCsPortalWindowContent()));
        CSUtility.switchToSplitAreaFrameLeft(waitForReload,
                browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getDrpdwnCSPortalGuiListSpecialFormat());
        getDrpdwnCSPortalGuiListSpecialFormat().click();
        CSLogger.info("Click on Drop Down Special Format");
    }

    /**
     * Clicks on webElement lblReferenceToPimProduct.
     * 
     * @param waitForReload this is webDriverWait object
     */
    public void clkLblReferenceToPimProduct(WebDriverWait waitForReload) {
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getlblReferenceToPimProduct());
        CSLogger.info(
                "Waiting Completed for visibilty of reference To pim product label.");
        getlblReferenceToPimProduct().click();
        CSLogger.info("Clicked on Reference To Pim Product attribute type");
    }

    /**
     * Selects any type of Cross Reference Attribute
     * 
     * @param waitForReload this is webDriverWait object
     * @param referenceToElement type of web Element
     * @param technicalName type of web Element
     */
    public void selectCrossReferenceAttributeType(WebDriverWait waitForReload,
            WebElement referenceToElement, String technicalName) {
        CSUtility.waitForVisibilityOfElement(waitForReload, referenceToElement);
        CSLogger.info("Waiting Completed for visibilty of " + technicalName
                + "attribute");
        referenceToElement.click();
    }
}
