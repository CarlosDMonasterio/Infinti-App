package org.ih.common.logging;

import org.slf4j.LoggerFactory;

/**
 * Logging for the application
 *
 * @author Hector Plahar
 */
public class Logger {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("org.ih");

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void error(String message, Throwable e) {
        LOGGER.error(message, e);
    }

    public static void error(Throwable e) {
        LOGGER.error(e.getMessage(), e);
    }

    public static void logErrorOnly(Throwable e) {
        LOGGER.error(e.getMessage(), e);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void debug(String message) {
        LOGGER.debug(message);
    }
}
