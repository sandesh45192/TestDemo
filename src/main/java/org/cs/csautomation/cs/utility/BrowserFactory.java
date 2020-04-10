/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This class is responsible for creating the requested browser instance.
 * 
 * @author CSAutomation Team
 *
 */
public class BrowserFactory {

    /**
     * This method accepts the browser name and created the appropriate driver
     * instance and return it.
     * 
     * @param browserName String
     * @param browserVersion String
     * @return driver WebDriver Instance
     */
    public WebDriver getBrowser(String browserName, String browserVersion) {
        WebDriver driver = null;
        if (browserName.equalsIgnoreCase("chrome")) {
            WebDriverManager.getInstance(DriverManagerType.CHROME)
                    .version(browserVersion).setup();
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = getChromeConfigurations();
            options.setExperimentalOption("prefs", prefs);
            driver = new ChromeDriver(options);
        } else if (browserName.equalsIgnoreCase("firefox")) {
            WebDriverManager.getInstance(DriverManagerType.FIREFOX)
                    .version(browserVersion).setup();
            driver = new FirefoxDriver();
        }
        if (driver != null) {
            CSLogger.info("Loaded Browser Driver Instance.");
        } else {
            CSLogger.fatal("Failed to load Browser Driver Instance.");
        }
        return driver;
    }

    /**
     * This method returns a map object containing various settings for the
     * chrome browser.
     * 
     * @return HashMap object containing the settings
     */
    private Map<String, Object> getChromeConfigurations() {
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.default_directory",
                System.getProperty("user.home") + "\\Downloads\\");
        prefs.put("safebrowsing.enabled", "false");
        prefs.put("download.prompt_for_download", "false");
        return prefs;
    }

}
