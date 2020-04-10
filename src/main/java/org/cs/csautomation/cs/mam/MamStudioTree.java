/**
 * 
 */

package org.cs.csautomation.cs.mam;

import org.openqa.selenium.WebDriver;

/**
 * @author
 *
 */
public final class MamStudioTree {

    private static MamStudioVolumesNode   mamStudioVolumesNode;

    /**
     * Constructor of MamStudioTree class
     */
    public MamStudioTree() {
    }

    /**
     * This method returns the instance of class MamStudioVolumesNode
     * 
     * @param driver
     * @return pimStudioSettingsNodeInstance
     */
    public static MamStudioVolumesNode
            getMamStudioVolumesNodeInstance(WebDriver driver) {
        if (mamStudioVolumesNode == null) {
            mamStudioVolumesNode = new MamStudioVolumesNode(driver);
        }
        return mamStudioVolumesNode;
    }

}