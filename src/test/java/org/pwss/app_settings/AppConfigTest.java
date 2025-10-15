package org.pwss.app_settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the AppConfig class.
 */
public class AppConfigTest {

    /**
     * Test to verify that setting a new app theme works correctly.
     *
     * This test sets a temporary app theme, verifies that it was set successfully,
     * and then restores the original value.
     */
    @Test
    void setAppThemeTest() {

        final int originalAppTheme = AppConfig.APP_THEME;
        boolean EXPECTED = true;

        boolean ACTUAL = AppConfig.setAppTheme(4);

        // Restore file to original state
        AppConfig.setAppTheme(originalAppTheme);

        Assertions.assertEquals(EXPECTED, ACTUAL);
    }

    /**
     * Test to verify that setting a new license key works correctly.
     *
     * This test sets a temporary license key, verifies that it was set successfully,
     * and then restores the original value.
     */
    @Test
    void setLicenseKeyTest() {

        final String originalLicenseKey = AppConfig.LICENSE_KEY;
        boolean EXPECTED = true;

        boolean ACTUAL = AppConfig.setLicenseKey("This should not persist!");

        // Restore file to original state
        AppConfig.setLicenseKey(originalLicenseKey);

        Assertions.assertEquals(EXPECTED, ACTUAL);
    }

    /**
     * Test to verify that setting the splash screen flag works correctly.
     *
     * This test sets a temporary value for the splash screen flag,
     * verifies that it was set successfully, and then restores the original value.
     */
    @Test
    void setSplashScreenTest() {

        final boolean originalUseSplashScreen = AppConfig.USE_SPLASH_SCREEN;
        boolean EXPECTED = true;

        boolean ACTUAL = AppConfig.setSplashScreenFlagInAppConfig(true);

        // Restore file to original state
        AppConfig.setSplashScreenFlagInAppConfig(originalUseSplashScreen);

        Assertions.assertEquals(EXPECTED, ACTUAL);
    }
}
