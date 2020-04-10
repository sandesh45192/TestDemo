
package org.cs.csautomation.cs.importstaging;

import org.openqa.selenium.WebDriver;

public final class ImportstagingTreeNode {

    private static ImportStagingMainPage importStagingMainInstance;

    /*
     * Constructor method for Import staging Classes
     */
    public ImportstagingTreeNode() {

    }

    /**
     * This method returns the instance of class ImportStagingMainPage
     * 
     * @param driver
     * @return importStagingMainPage
     */
    public static ImportStagingMainPage
            getImportsettingInstance(WebDriver driver) {

        if (importStagingMainInstance == null) {
            importStagingMainInstance = new ImportStagingMainPage(driver);
        }
        return importStagingMainInstance;
    }
}
