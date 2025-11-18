package org.pwss.navigation;

/**
 * Represents the different screens available in the application.
 */
public enum Screen {
    /**
     * The login screen where users can authenticate.
     */
    LOGIN(450, 250),

    /**
     * The home screen displayed after successful login.
     */
    HOME(1600, 800),

    /**
     * The screen for creating a new monitored directory.
     */
    NEW_DIRECTORY(400, 200),

    /**
     * The screen for viewing scan summaries.
     */
    SCAN_SUMMARY(1600, 800);

    /**
     * The width of the frame for this screen.
     */
    public final int frameWidth;

    /**
     * The height of the frame for this screen.
     */
    public final int frameHeight;

    Screen(int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }
}
