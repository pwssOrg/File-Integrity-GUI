package org.pwss.app_settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.slf4j.LoggerFactory;

/**
 * The ConfigLoader class is responsible for loading configuration settings from
 * a properties file and providing access to these settings.
 * 
 * @author PWSS ORG
 */
final class ConfigLoader {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(ConfigLoader.class);

    /**
     * Key in the properties file for splash screen setting.
     */
    private final String SPLASHSCREEN_KEY = "frontend.splashscreen";
    /**
     * Key in the properties file for application theme setting.
     */
    private String APP_THEME_KEY = "frontend.theme";
    /**
     * Key in the properties file for license key setting.
     */
    private final String LICENSE_KEY = "frontend.licensekey";

    /**
     * Path to the configuration file.
     */
    private final String CONFIG_FILE_PATH = "options" + File.separator + "app.config";
    /**
     * Properties object that holds the configuration data loaded from the
     * properties file.
     */
    private final Properties properties = new Properties();

    /**
     * Flag indicating whether to use splash screen or not.
     */
    private final boolean useSplashScreen;
    /**
     * Value representing the application theme.
     */
    private final int appTheme;
    /**
     * License key for the application.
     */
    private final String licenseKey;

    /**
     * Constructor that loads configuration settings from the properties file and
     * initializes
     * the instance variables based on the loaded values. If loading fails, default
     * values are used.
     */
    ConfigLoader() {

        boolean result = loadConfig();

        if (!result) {
            this.useSplashScreen = true;
            this.appTheme = 1;
            this.licenseKey = "none";
        } else {
            this.useSplashScreen = getSplashScreenFlagFromConfigString(getSplashScreenProperty());
            this.appTheme = getAppThemeValueFromConfigString(getAppThemeProperty());
            this.licenseKey = getLicenseKeyProperty();
        }

    }

    /**
     * Parses the splash screen flag from a configuration string.
     *
     * @param configFileString The configuration string to be parsed
     * @return falsee if the string is "false" (case insensitive), otherwise true
     */
    private final boolean getSplashScreenFlagFromConfigString(String configFileString) {

        try {
            if (configFileString.equalsIgnoreCase("false"))
                return false;
            else
                return true;
        } catch (Exception exception) {

            log.debug("Splash screen flag from app settings could not be parsed", exception);
            log.error("Splash screen flag from app settings could not be parsed {}", exception.getMessage());
            return true;
        }

    }

    /**
     * Parses the application theme value from a configuration string.
     *
     * @param configFileString The configuration string to be parsed
     * @return The integer value of the theme, or 1 if parsing fails or the value is
     *         invalid
     */
    private final int getAppThemeValueFromConfigString(String configFileString) {

        try {

            int appThemeValue = Integer.parseInt(configFileString);

            if (appThemeValue < 5)
                return appThemeValue;
            else
                return 1;
        }

        catch (Exception exception) {
            log.debug("Could not parse theme value from app settings", exception);
            log.error("Could not parse theme value from app settings {}", exception.getMessage());
            return 1;
        }

    }

    /**
     * Loads the configuration properties from the file specified by
     * CONFIG_FILE_PATH.
     *
     * @return true if loading was successful, otherwise false
     */
    private final boolean loadConfig() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(input);
            return true;
        }

        catch (Exception exception) {
            log.debug("Could not load app settings", exception);
            log.error("Could not load app settings", exception.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the splash screen property value from the properties file.
     *
     * @return The splash screen property value, or "true" if an error occurs
     */
    private final String getSplashScreenProperty() {
        try {

            String result = properties.getProperty(SPLASHSCREEN_KEY);
            return result;

        } catch (Exception exception) {

            log.debug("Splash screen flag property could not be fetched", exception);
            log.error("Splash screen flag property could not be fetched {}", exception.getMessage());
            return "true";
        }

    }

    /**
     * Retrieves the application theme property value from the properties file.
     *
     * @return The application theme property value, or "1" if an error occurs
     */
    private final String getAppThemeProperty() {
        try {

            String result = properties.getProperty(APP_THEME_KEY);
            return result;
        } catch (Exception exception) {

            log.debug("App theme property could not be fetched", exception);
            log.error("App theme property could not be fetched {}", exception.getMessage());
            return "1";
        }
    }

    /**
     * Retrieves the license key property value from the properties file.
     *
     * @return The license key property value, or "none" if an error occurs
     */
    private final String getLicenseKeyProperty() {
        try {

            String result = properties.getProperty(LICENSE_KEY);
            return result;
        } catch (Exception exception) {

            log.debug("License key property could not be fetched", exception);
            log.error("License key property could not be fetched {}", exception.getMessage());
            return "1";
        }

    }

    /**
     * Sets the splash screen flag in the properties file.
     *
     * @param splashScreenFlag The value to set for the splash screen flag
     * @return true if setting was successful, otherwise false
     */
    final boolean setSplashScreenFlag(String splashScreenFlag) {

        properties.setProperty(SPLASHSCREEN_KEY, splashScreenFlag);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.store(fos, null);
            return true;
        } catch (Exception exception) {
            log.debug("Splash screen flag could not be set in app.config file", exception);
            log.error("Splash screen flag could not be set in app.config file", exception.getMessage());
            return false;
        }
    }

    /**
     * Sets the application theme value in the properties file.
     *
     * @param appTheme The value to set for the application theme
     * @return true if setting was successful, otherwise false
     */
    final boolean setAppTheme(String appTheme) {

        properties.setProperty(APP_THEME_KEY, appTheme);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.store(fos, null);
            return true;
        } catch (Exception exception) {
            log.debug("App theme could not be set in app.config file", exception);
            log.error("App theme could not be set in app.config file", exception.getMessage());
            return false;
        }
    }

    /**
     * Sets the license key value in the properties file.
     *
     * @param licenseKey The value to set for the license key
     * @return true if setting was successful, otherwise false
     */
    final boolean setLicenseKey(String licenseKey) {

        properties.setProperty(LICENSE_KEY, licenseKey);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.store(fos, null);
            return true;
        } catch (Exception exception) {
            log.debug("License key could not be set in app.config file", exception);
            log.error("License key could not be set in app.config file", exception.getMessage());
            return false;
        }
    }

    /**
     * Gets the value indicating whether to use splash screen or not.
     *
     * @return true if splash screen is enabled, otherwise false
     */
    final boolean isUseSplashScreen() {
        return useSplashScreen;
    }

    /**
     * Gets the application theme value.
     *
     * @return The current application theme value
     */
    final int getAppTheme() {
        return appTheme;
    }

    /**
     * Gets the license key value.
     *
     * @return The current license key value
     */
    final String getLicenseKey() {
        return licenseKey;
    }
}