package org.pwss.view.theme;

import java.awt.Color;
import javax.swing.UIManager;

import org.pwss.util.AppTheme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

/**
 * Installs the application look and feel.
 * <p>
 * The base look comes from FlatLaf; everything that makes it "ours" (palette,
 * corner radii, spacing, table and tab metrics, style classes) lives in the
 * property files under {@code src/main/resources/org/pwss/theme}.
 */
public final class AppLookAndFeel {

    /**
     * Resource package holding the FlatLaf property overrides.
     */
    private static final String THEME_PACKAGE = "org.pwss.theme";

    private AppLookAndFeel() {
    }

    /**
     * Installs the look and feel matching the given theme value.
     *
     * @param themeValue the value of an {@link AppTheme}; unknown values fall back
     *                   to the dark theme
     * @return true if the look and feel was installed successfully
     */
    public static boolean install(int themeValue) {
        FlatLaf.registerCustomDefaultsSource(THEME_PACKAGE);

        if (themeValue == AppTheme.LIGHT.getValue()) {
            return FlatLightLaf.setup();
        } else if (themeValue == AppTheme.MAC_LIGHT.getValue()) {
            return FlatMacLightLaf.setup();
        } else if (themeValue == AppTheme.MAC_DARK.getValue()) {
            return FlatMacDarkLaf.setup();
        }
        return FlatDarkLaf.setup();
    }

    /**
     * @return the colour used for "everything is fine" states
     */
    public static Color okColor() {
        return color("App.okColor", new Color(0x4cc38a));
    }

    /**
     * @return the colour used for states that need attention soon
     */
    public static Color warnColor() {
        return color("App.warnColor", new Color(0xe5a84b));
    }

    /**
     * @return the colour used for states that need attention now
     */
    public static Color errorColor() {
        return color("App.errorColor", new Color(0xe5605a));
    }

    private static Color color(String key, Color fallback) {
        Color color = UIManager.getColor(key);
        return color != null ? color : fallback;
    }
}
