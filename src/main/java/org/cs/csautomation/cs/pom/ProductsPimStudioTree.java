package org.cs.csautomation.cs.pom;

import org.openqa.selenium.WebDriver;

public final class ProductsPimStudioTree {

    private static PimStudioSettingsNode     pimStudioSettingsNodeInstance;
    private static PimStudioProductsNodePage pimStudioProductsNodeInstance;
    private static PimStudioChannelsNode     pimStudioChannelsNodeInstance;

    private ProductsPimStudioTree() {
    }

    /**
     * This method returns the instance of class pimStudioSettingsNode
     * 
     * @param driver
     * @return pimStudioSettingsNodeInstance
     */
    public static PimStudioSettingsNode getPimStudioSettingsNodeInstance(
            WebDriver driver) {
        if (pimStudioSettingsNodeInstance == null) {
            pimStudioSettingsNodeInstance = new PimStudioSettingsNode(driver);
        }
        return pimStudioSettingsNodeInstance;
    }

    /**
     * This method returns the instance of class pimStudioProductsNode
     * 
     * @param driver
     *            this is browserDriver Instance
     * @return pimStudioProductsNodeInstance
     */
    public static PimStudioProductsNodePage getPimStudioProductsNodeInstance(
            WebDriver driver) {
        if (pimStudioProductsNodeInstance == null) {
            pimStudioProductsNodeInstance = new PimStudioProductsNodePage(
                    driver);
        }
        return pimStudioProductsNodeInstance;
    }

    /**
     * This method returns the instance of class PimStudioChannelsNode
     * 
     * @param driver
     *            this is browserDriver Instance
     * @return PimStudioChannelsNode
     */
    public static PimStudioChannelsNode getPimStudioChannelsNodeInstance(
            WebDriver driver) {
        if (pimStudioChannelsNodeInstance == null) {
            pimStudioChannelsNodeInstance = new PimStudioChannelsNode(driver);
        }
        return pimStudioChannelsNodeInstance;
    }

}