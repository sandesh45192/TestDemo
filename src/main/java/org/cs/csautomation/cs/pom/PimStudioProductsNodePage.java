/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.pom;

import org.cs.csautomation.cs.utility.CSLogger;
import org.cs.csautomation.cs.utility.CSUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PimStudioProductsNodePage {

    private WebDriver  browserDriverInstance;

    @FindBy(id = "Articles@0")
    private WebElement btnPimProductsNode;

    @FindBy(id = "Exports@0")
    private WebElement btnPimExportNode;

    @FindBy(id = "userInput")
    private WebElement userInputElement;

    @FindBy(id = "CSPopupDiv")
    private WebElement productPopUpDiv;

    @FindBy(xpath = "//tr[@name='Create new']")
    private WebElement clkCreateNew;

    @FindBy(xpath = "//iframe[@id='CSPopupDivFrame']")
    private WebElement productPopUpFrame;

    @FindBy(xpath = "//div[@class='CSPortalWindow']/div[2]")
    private WebElement productWindow;

    @FindBy(xpath = "//div[@class='CSPortalWindow']/div[2]/iframe")
    private WebElement productWindowFrame;

    @FindBy(xpath = "//tr[@name='New Child']")
    private WebElement clkNewChild;

    public PimStudioProductsNodePage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized PimStudioProductsNode page POM elements.");
    }

    /**
     * Returns instance of click on new child in pop up
     * 
     * @return clkNewChild
     */
    public WebElement getClkNewChild() {
        return clkNewChild;
    }

    /**
     * Returns popUpDivFrame*
     * 
     */
    public WebElement getproductPopUpFrame() {
        return productPopUpFrame;
    }

    /**
     * Returns popUpDivFrame*
     * 
     */
    public WebElement getproductWindowFrame() {
        return productWindowFrame;
    }

    /**
     * Returns productWindow while handling the dialog box after drag drop
     * 
     * @return productWindow
     */

    public WebElement getproductWindow() {
        return productWindow;
    }

    /**
     * Returns instance of create new in pop up after right clicking on products
     * node
     * 
     * @return clkCreateNew
     */
    public WebElement getClkCreateNew() {
        return clkCreateNew;
    }

    /**
     * Returns popUpDivFrame
     * 
     * @return config ApplicationConfiguration
     */
    public WebElement getProductPopUpDiv() {
        return productPopUpDiv;
    }

    /**
     * Returns name which user enters in dialog box for creating product and
     * child products .
     * 
     * @return userInputElement
     */
    public WebElement getUserElement() {
        return userInputElement;
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
     * Returns WebElement btnPimProductsNode
     * 
     * @return btnPimProductsNode PimStudioProductsNode
     */
    public WebElement getBtnPimProductsNode() {
        return btnPimProductsNode;
    }

    /**
     * Returns WebElement btnPimExportNode
     * 
     * @return btnPimExportNode
     */
    public WebElement getBtnPimExportNode() {
        return btnPimExportNode;
    }

    /**
     * Clicks on the WebElement btnPimProductsNode
     * 
     * @param waitForReload
     * @param browserDriverInstance
     */
    public void clkBtnPimProductsNode(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        traverseToPimProductsNode(waitForReload, browserDriverInstance);
        CSUtility.waitForVisibilityOfElement(waitForReload,
                getBtnPimProductsNode());
        getBtnPimProductsNode().click();
        CSLogger.info("Clicked on products node");
    }

    /**
     * This method TraverseToPimProductNode or switches the frames till Products
     * Node
     * 
     * @param waitForReload
     * @param browserDriverInstance
     */
    public void traverseToPimProductsNode(WebDriverWait waitForReload,
            WebDriver browserDriverInstance) {
        CSUtility.traverseToPimStudio(waitForReload, browserDriverInstance);
    }

    /**
     * This method performs the click operation on the product node.
     */
    public void clickOnNodeProducts() {
        getBtnPimProductsNode().click();
        CSLogger.info("clicked on the Product node in PIM tree.");
    }

    /**
     * This method expands the nodes in PIM product tree.
     * 
     * @param waitForReload WebDriverWait instance
     * @param productName String Name of the node to expand
     * @return WebElement
     */
    public WebElement expandNodesByProductName(WebDriverWait waitForReload,
            String productName) {
        WebElement element;
        waitForReload.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'" + productName + "')]")));
        element = browserDriverInstance.findElement(
                By.xpath("//span[contains(text(),'" + productName + "')]"));
        element.click();
        CSLogger.info("Clicked on product node " + productName);

        return element;
    }

    /**
     * This method performs the click operation on the export node.
     */
    public void clickOnNodeExports() {
        getBtnPimExportNode().click();
        CSLogger.info("clicked on the Export node in PIM tree.");
    }
}
