package org.pwss.app_settings;

/**
 * The AppConfig class provides access to the application configuration
 * settings.
 * It uses a ConfigLoader instance to load and manage configuration values.
 * 
 * @author PWSS ORG
 */
public final class AppConfig {

    /**
     * Flag indicating whether to use splash screen or not. This value is loaded
     * from the configuration file
     * when the class is initialized.
     */
    public final static boolean USE_SPLASH_SCREEN;
    /**
     * Value representing the application theme. This value is loaded from the
     * configuration file
     * when the class is initialized.
     */
    public final static int APP_THEME;
    /**
     * License key for the application. This value is loaded from the configuration
     * file
     * when the class is initialized.
     */
    public final static String LICENSE_KEY;

    /**
     * ConfigLoader instance used to load and manage configuration values.
     */
    private static ConfigLoader configLoader = new ConfigLoader();

    /**
     * Static block that initializes the static configuration fields by loading them
     * from the ConfigLoader.
     */
    static {
        USE_SPLASH_SCREEN = configLoader.isUseSplashScreen();
        APP_THEME = configLoader.getAppTheme();
        LICENSE_KEY = configLoader.getLicenseKey();
    }

    /**
     * Sets the splash screen flag in the configuration file. This change will take
     * effect only after
     * the frontend application is restarted.
     *
     * @param flag The value to set for the splash screen flag (true or false)
     * @return true if setting was successful, otherwise false
     */
    public static final boolean setSplashScreenFlagInAppConfig(boolean flag) {
        return configLoader.setSplashScreenFlag(String.valueOf(flag));
    }

    /**
     * Sets the application theme value in the configuration file. This change will
     * take effect only after
     * the frontend application is restarted.
     *
     * @param appTheme The value to set for the application theme (an integer
     *                 between 1 and 4)
     * @return true if setting was successful, otherwise false
     */
    public static final boolean setAppTheme(int appTheme) {
        return configLoader.setAppTheme(String.valueOf(appTheme));
    }

    /**
     * Sets the license key value in the configuration file. This change will take
     * effect only after
     * the frontend application is restarted.
     *
     * @param licenseKey The value to set for the license key (a string)
     * @return true if setting was successful, otherwise false
     */
    public static final boolean setLicenseKey(String licenseKey) {
        return configLoader.setLicenseKey(licenseKey);
    }

}
