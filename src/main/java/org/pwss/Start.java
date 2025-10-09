package org.pwss;

import org.pwss.exception.start.FailedToLaunchAppException;
import org.slf4j.LoggerFactory;

public class Start {
    public static void main(String[] args) {
        try {
            FileIntegrityScannerFrontend.StartApplication();
        } catch (FailedToLaunchAppException failedToLaunchAppException) {
            LoggerFactory.getLogger(Start.class).error("Failed to start application {}",
                    failedToLaunchAppException.getMessage());
        }
    }
}