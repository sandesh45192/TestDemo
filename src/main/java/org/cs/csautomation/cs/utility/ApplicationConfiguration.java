/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class ready the properties file and extract the properties.
 * 
 * @author CSAutomation Team
 *
 */
public class ApplicationConfiguration {

    private Properties config;

    /**
     * This method reads the properties file.
     */
    public void loadConfigurations() {
        if (config == null) {
            try {
                config = new Properties();
                String paths = System.getProperty("user.dir")
                        + IConstants.PROPERTIES_FILE;
                FileInputStream fis;
                fis = new FileInputStream(paths);
                config.load(fis);
            } catch (IOException e) {
                CSLogger.fatal("Failed to load Configurations .", e);
            }
            CSLogger.info("Successfully loaded the configurations.");
        }
    }

    /**
     * @return String Url from properties file, null if its not present.
     */
    public String getUrl() {
        if (config != null) {
            return config.getProperty("url");
        }
        return null;
    }

    /**
     * @return String Browser Name from properties file, null if its not
     *         present.
     */
    public String getBrowserName() {
        if (config != null) {
            return config.getProperty("browser");
        }
        return null;
    }

    /**
     * @return String Implicit timeout from properties file, 0 if its not
     *         present.
     */
    public int getImplicitTimeout() {
        if (config != null) {
            return Integer.parseInt(config.getProperty("implicitWait"));
        }
        return 0;
    }

    /**
     * @return String pageload time from properties file, 0 if its not present.
     */
    public int getPageLoadTimeout() {
        if (config != null) {
            return Integer.parseInt(config.getProperty("pageloadTimeout"));
        }
        return 0;
    }

    /**
     * @return String script timeout from properties file, 0 if its not present.
     */
    public int getScriptTimeout() {
        if (config != null) {
            return Integer.parseInt(config.getProperty("scriptTimeout"));
        }
        return 0;
    }

    /**
     * @return String screen shot directory path from properties file, null if
     *         its not present.
     */
    public String getScreenshotPath() {
        if (config != null) {
            return config.getProperty("screenshotsPath");
        }
        return null;
    }

    /**
     * @return String reports directory path from properties file, null if its
     *         not present.
     */
    public String getextentReportsPath() {
        if (config != null) {
            return config.getProperty("extentReports");
        }
        return null;
    }

    /**
     * @return String Excel file directory path based on module from properties
     *         file, null if its not present.
     */
    public String getExcelSheetPath(String moduleFilePath) {
        if (config != null) {
            return config.getProperty(moduleFilePath);
        }
        return null;
    }

    /**
     * @return String Browser version from the properties file.
     */
    public String getBrowserVersion() {
        if (config != null) {
            return config.getProperty("browser.version");
        }
        return null;
    }

    /**
     * Returns the import folder path.
     * 
     * @return String import folder path.
     */
    public String getDataflowTestDataFolder() {
        if (config != null) {
            return System.getProperty("user.dir")
                    + config.getProperty("testDataflowFolder");
        }
        return null;
    }
    
    /**
     * 
     * @return String Version name from properties file, null if it does not
     *         exist
     */
    public String getCsVersion() {
        if (config != null) {
            return config.getProperty("csversion");
        }
        return null;
    }
}
