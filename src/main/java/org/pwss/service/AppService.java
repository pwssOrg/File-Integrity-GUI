package org.pwss.service;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AppService class provides methods to manage the application lifecycle.
 */
public final class AppService {

    /**
     * Static logger instance for logging messages within this class.
     */
    private static Logger log = LoggerFactory.getLogger(AppService.class);

    /**
     * Counter to track the number of AppService instances created.
     */
    private static int instanceCounter = 0;

    /**
     * Constructor for AppService. Ensures only one instance is allowed.
     *
     */
    public AppService() {

        instanceCounter++;

        if (instanceCounter > 1) {
            log.debug("Terminating application due to exceeding the allowed number of AppService instances");
            System.exit(2);
        }

    }

    /**
     * Restarts the application by launching a new JVM process and terminating the
     * current one.
     *
     * @throws URISyntaxException if there is an error converting the code source
     *                            location to URI
     */
    public final void restartApp() throws URISyntaxException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(AppService.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        // Build command: java -jar application.jar
        final ArrayList<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();


        } catch (Exception ex) {

            log.error("An error occurred while attempting to restart the application: {}", ex.getMessage());
            log.debug("An error occurred while attempting to restart the application: {}", ex);
        }

        // Terminate the current JVM after starting the new one
        System.exit(0);
    }

}
