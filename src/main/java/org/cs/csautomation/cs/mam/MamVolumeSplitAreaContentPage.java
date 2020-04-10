
package org.cs.csautomation.cs.mam;

import org.cs.csautomation.cs.utility.CSLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MamVolumeSplitAreaContentPage {

    @FindBy(xpath = "//a[contains(text(), 'History')]/parent::*")
    private WebElement btnProductHistory;

    /**
     * This method returns the instance of History
     * 
     * @return
     */
    public WebElement getBtnProductHistory() {
        return btnProductHistory;
    }

    public MamVolumeSplitAreaContentPage(WebDriver paramDriver) {
        setBrowserDriverInstance(paramDriver);
        PageFactory.initElements(paramDriver, this);
        CSLogger.info("Initialized POM page elements.");

    }

    public void setBrowserDriverInstance(WebDriver browserDriverInstance) {
    }
}
