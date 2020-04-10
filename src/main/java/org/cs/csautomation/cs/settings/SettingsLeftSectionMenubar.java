/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.settings;

import org.openqa.selenium.WebDriver;

public class SettingsLeftSectionMenubar {

    private static ValuelistManagementPage valuelistManagementInstance;
    private static SettingsPage            settingsPageInstance;
    private static ColorManagementPage     colorManagementInstance;
    private static ActiveJobsPage          activeJobsPageInstance;
    private static UserManagementPage      userManagementPageInstance;
    private static WorkflowsPage           workflowPageInstance;
    private static SearchPage              searchPageInstance;
    private static AutomationsPage         automationsPageInstance;
    private static LanguageManagementPage  languageManagementPageInstance;

    public static UserManagementPage getUserManagementNode(WebDriver driver) {
        if (userManagementPageInstance == null) {
            userManagementPageInstance = new UserManagementPage(driver);
        }
        return userManagementPageInstance;
    }

    /*
     * This method return the Value list Modules page object.
     * 
     * @return valuelistManagementInstance instance of page
     */
    public static ValuelistManagementPage
            getValuelistSettingNode(WebDriver driver) {
        if (valuelistManagementInstance == null) {
            valuelistManagementInstance = new ValuelistManagementPage(driver);
        }
        return valuelistManagementInstance;
    }

    /*
     * This method return the Settings Modules page object.
     * 
     * @return settingsPageInstance instance of page
     */
    public static SettingsPage getSettingsNode(WebDriver driver) {
        if (settingsPageInstance == null) {
            settingsPageInstance = new SettingsPage(driver);
        }
        return settingsPageInstance;
    }

    /*
     * This method return the colors Modules page object.
     * 
     * @return colorManagementInstance instance of page
     */
    public static ColorManagementPage getColorsSettingNode(WebDriver driver) {
        if (colorManagementInstance == null) {
            colorManagementInstance = new ColorManagementPage(driver);
        }
        return colorManagementInstance;
    }

    /*
     * This method return the Active Jobs page object.
     * 
     * @return activeJobsPageInstance instance of page
     */
    public static ActiveJobsPage getActiveJobsNode(WebDriver driver) {
        if (activeJobsPageInstance == null) {
            activeJobsPageInstance = new ActiveJobsPage(driver);
        }
        return activeJobsPageInstance;
    }

    /**
     * This method returns the Workflows page object.
     * 
     * @param driver Instance of WebDriver.
     * @return WorkflowsPage workflowPageInstance.
     */
    public static WorkflowsPage getWorkflowsNode(WebDriver driver) {
        if (workflowPageInstance == null) {
            workflowPageInstance = new WorkflowsPage(driver);
        }
        return workflowPageInstance;
    }

    /*
     * This method returns the search page object.
     * 
     * @return searchPageInstance - instance of search page
     */
    public static SearchPage getSearchNode(WebDriver driver) {
        if (searchPageInstance == null) {
            searchPageInstance = new SearchPage(driver);
        }
        return searchPageInstance;
    }

    /**
     * This method returns the Automation page object.
     * 
     * @param driver Instance of WebDriver.
     * @return AutomationsPage automationsPageInstance
     */
    public static AutomationsPage getAutomationsNode(WebDriver driver) {
        if (automationsPageInstance == null) {
            automationsPageInstance = new AutomationsPage(driver);
        }
        return automationsPageInstance;
    }

    /*
     * This method return the Language page object.
     * 
     * @return languageManagementPageInstance instance of page
     */
    public static LanguageManagementPage
            getLanguagesSettingNode(WebDriver driver) {

        if (languageManagementPageInstance == null) {
            languageManagementPageInstance = new LanguageManagementPage(driver);
        }
        return languageManagementPageInstance;
    }
}
