/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.cs.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This internally uses Log4j api to log the messages.
 * 
 * @author CSAutomation Team
 *
 */
public final class CSLogger {

    private static Logger log = LogManager.getLogger(CSLogger.class.getName());

    /**
     * Default constructor.
     */
    private CSLogger() {
    }

    /**
     * Set the Class for logger.
     * 
     * @param classObj Class Instance of test class
     */
    public static void setClass(Object classObj) {
        log = LogManager.getLogger(classObj.getClass().getName());
    }

    /**
     * This method logs the start of a test case as info message.
     * 
     * @param testCaseName String Name of test case.
     */
    public static void startTestCase(String testCaseName) {
        log.info("***** " + testCaseName + " Started *****");
    }

    /**
     * This method logs the end of a test case as info message.
     * 
     * @param testCaseName String Name of test case.
     */
    public static void endTestCase(String testCaseName) {
        log.info("***** " + testCaseName + " Ended *****");
    }

    /**
     * This method logs message as info..
     * 
     * @param message String message to log.
     */
    public static void info(String message) {
        log.info(message);
    }

    /**
     * This is overloaded method to log message with exception..
     * 
     * @param message String message to log.
     */
    public static void info(String message, Throwable e) {
        log.info(message, e);
    }

    /**
     * This method warn message as info.
     * 
     * @param message String message to log.
     */
    public static void warn(String message) {
        log.warn(message);
    }

    /**
     * This is overloaded method to log message with exception..
     * 
     * @param message String message to log.
     */
    public static void warn(String message, Throwable e) {
        log.warn(message, e);
    }

    /**
     * This method warn message as error.
     * 
     * @param message String message to log.
     */
    public static void error(String message) {
        log.error(message);
    }

    /**
     * This is overloaded method to log message with exception..
     * 
     * @param message String message to log.
     */
    public static void error(String message, Throwable e) {
        log.error(message, e);
    }

    /**
     * This method warn message as trace.
     * 
     * @param message String message to log.
     */
    public static void trace(String message) {
        log.fatal(message);
    }

    /**
     * This is overloaded method to log message with exception..
     * 
     * @param message String message to log.
     */
    public static void trace(String message, Throwable e) {
        log.fatal(message, e);
    }

    /**
     * This method warn message as fatal.
     * 
     * @param message String message to log.
     */
    public static void fatal(String message) {
        log.fatal(message);
    }

    /**
     * This is overloaded method to log message with exception..
     * 
     * @param message String message to log.
     */
    public static void fatal(String message, Throwable e) {
        log.fatal(message, e);
    }

    /**
     * This method warn message as debug.
     * 
     * @param message String message to log.
     */
    public static void debug(String message) {
        log.debug(message);
    }

    /**
     * This is overloaded method to log message with exception..
     * 
     * @param message String message to log.
     */
    public static void debug(String message, Throwable e) {
        log.debug(message, e);
    }
}
