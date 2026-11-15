package org.pwss.view.theme;

import java.awt.Color;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLaf;

/**
 * The application's colours as code sees them: the accent, the card surface,
 * secondary text and a small set of distinct hues for icons, tiles and badges.
 * <p>
 * Everything is read from the active theme (see the property files under
 * {@code src/main/resources/org/pwss/theme}), with fallbacks for themes that
 * do not define a key.
 */
public final class Palette {

    /**
     * The distinct hues, by index.
     */
    public static final int BLUE = 0;
    public static final int ORANGE = 1;
    public static final int TEAL = 2;
    public static final int VIOLET = 3;
    public static final int PINK = 4;

    private static final int[][] FALLBACKS = {
            // dark, light
            { 0x5b9cf5, 0x2f6fe4 },
            { 0xf0a35e, 0xc2600f },
            { 0x3cc7b4, 0x0f8a7a },
            { 0xa58bf5, 0x7048d8 },
            { 0xf07aa8, 0xc0267a },
    };

    private Palette() {
    }

    /**
     * Returns one of the distinct hues.
     *
     * @param index one of {@link #BLUE}, {@link #ORANGE}, {@link #TEAL},
     *              {@link #VIOLET} or {@link #PINK}; other values wrap around
     * @return the colour for the active theme
     */
    public static Color hue(int index) {
        int i = Math.floorMod(index, FALLBACKS.length);
        if (i == BLUE) {
            return accent();
        }
        int fallback = FALLBACKS[i][FlatLaf.isLafDark() ? 0 : 1];
        return color("App.hue" + i, new Color(fallback));
    }

    /**
     * @return the accent colour of the active theme
     */
    public static Color accent() {
        return color("Component.accentColor", new Color(FALLBACKS[BLUE][FlatLaf.isLafDark() ? 0 : 1]));
    }

    /**
     * @return the background of cards
     */
    public static Color surface() {
        Color color = UIManager.getColor("App.wellBackground");
        return color != null ? color : UIManager.getColor("Panel.background");
    }

    /**
     * @return the background of the window behind the cards
     */
    public static Color background() {
        return UIManager.getColor("Panel.background");
    }

    /**
     * @return the colour of secondary text
     */
    public static Color muted() {
        Color color = UIManager.getColor("App.mutedForeground");
        return color != null ? color : UIManager.getColor("Label.disabledForeground");
    }

    /**
     * Blends two colours.
     *
     * @param color  the colour to blend in
     * @param base   the colour to blend into
     * @param amount how much of {@code color} to use, from 0 to 1
     * @return the blended colour
     */
    public static Color mix(Color color, Color base, double amount) {
        return new Color(
                (int) Math.round(color.getRed() * amount + base.getRed() * (1 - amount)),
                (int) Math.round(color.getGreen() * amount + base.getGreen() * (1 - amount)),
                (int) Math.round(color.getBlue() * amount + base.getBlue() * (1 - amount)));
    }

    /**
     * Tints the window background with a colour, for soft fills behind icons
     * and badges.
     *
     * @param color  the tint
     * @param amount how strong the tint is, from 0 to 1
     * @return the tinted colour
     */
    public static Color tint(Color color, double amount) {
        return mix(color, background(), amount);
    }

    private static Color color(String key, Color fallback) {
        Color color = UIManager.getColor(key);
        return color != null ? color : fallback;
    }
}
