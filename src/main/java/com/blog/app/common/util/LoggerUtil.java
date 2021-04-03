/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blog.app.common.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoggerUtil implements Serializable {
    private static boolean initialized = false;
    private static final Map<LogType, String> loggerNames = new HashMap<>();

    private static final String appName = "blog";
    
    
    static {
        loggerNames.put(LogType.ROOT_LOGGER, appName);
        loggerNames.put(LogType.VALIDATION, "validation");
        loggerNames.put(LogType.INQUIRY, "inquiry");
        loggerNames.put(LogType.ERROR_LOG,"error");
        loggerNames.put(LogType.REQ_RES_LOG,"requestResponse");
    }
    
    public static void reconfigureLogger() {
        initialized = false;
        initLogger();
    }
    
    private static void initLogger() {
        if (initialized) {
            return;
        }

        File logDirectory = new File(System.getProperty("user.home")+String.format("/logs/%s", appName));
        if(!logDirectory.exists()) {
            if(!logDirectory.mkdirs()) {
                System.out.println("Could not create log path!!!!");
            }
        }
        
        Properties logProperties = new Properties();
        logProperties.setProperty("log4j.reset", "true");        
        logProperties.setProperty("LOG_PATH", logDirectory.getPath());
        
        logProperties.setProperty("LOG_PATTERN", "<%d> <%5p> (%F:%L) <%m>%n");
        logProperties.setProperty("LOG_LEVEL", "DEBUG");
        logProperties.setProperty("LOG_LAYOUT", "org.apache.log4j.PatternLayout");
        logProperties.setProperty("LOG_APPENDER", "org.apache.log4j.DailyRollingFileAppender");
        
        // add console appender
        logProperties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        logProperties.setProperty("log4j.appender.stdout.layout", "${LOG_LAYOUT}");
        logProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "<BLOG> ${LOG_PATTERN}");

        logProperties.setProperty("log4j.logger.org.hibernate","INFO");
        logProperties.setProperty("log4j.logger.org.hibernate.type","ALL");
        // add loggers
        for (String loggerName : loggerNames.values()) {
            configureLogger(logProperties, loggerName);
        }

        logProperties.list(System.out);
        PropertyConfigurator.configure(logProperties);
        
        initialized = true;
    }
    
    private static void configureLogger(Properties logProperties, String loggerName) {
        String loggerFullName = "log4j.appender." + loggerName;
        String logPrefix = "<" + loggerName.toUpperCase() + ">";
        String logFileName = "${LOG_PATH}" + File.separator + loggerName + ".log";
        
        if (loggerName.equalsIgnoreCase("n/a")) {
            logProperties.setProperty("log4j.rootLogger", "${LOG_LEVEL}, stdout, " + loggerName);
            logFileName = "${LOG_PATH}" + File.separator + String.format("%s.log", appName);
            logPrefix = String.format("<?>", appName);
        } else {
            logProperties.setProperty("log4j.logger." + loggerName, "${LOG_LEVEL}, stdout, " + loggerName);
        }
        
        System.out.println("Configuring " + loggerName + " logger to log output to: " + logFileName);
        logProperties.setProperty(loggerFullName, "${LOG_APPENDER}");
        logProperties.setProperty(loggerFullName + ".File", logFileName);
        logProperties.setProperty(loggerFullName + ".DatePattern", "'.'yyyy-MM-dd");
        logProperties.setProperty(loggerFullName + ".Append", "true");
        logProperties.setProperty(loggerFullName + ".layout", "${LOG_LAYOUT}");
        logProperties.setProperty(loggerFullName + ".layout.ConversionPattern", logPrefix + " ${LOG_PATTERN}");
    }
    
    public static Logger getLogger(LogType logType) {
        initLogger();
        
        switch (logType) {
            case VALIDATION:
                return Logger.getLogger(loggerNames.get(LogType.VALIDATION));
            case INQUIRY:
                return Logger.getLogger(loggerNames.get(LogType.INQUIRY));
            case ERROR_LOG:
                return Logger.getLogger(loggerNames.get(LogType.ERROR_LOG));
            case REQ_RES_LOG:
                return Logger.getLogger(loggerNames.get(LogType.REQ_RES_LOG));
            default:
                return Logger.getRootLogger();
        }
    }
    
    public enum LogType {
        ROOT_LOGGER,
        VALIDATION,
        INQUIRY,
        ERROR_LOG,
        REQ_RES_LOG;
    }
}
