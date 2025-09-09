package org.pwss.navigation;

/**
 * Represents the different screens available in the application.
 */
public enum Screen {
    /**
     * The login screen where users can authenticate.
     */
    LOGIN(400, 250),

    /**
     * The home screen displayed after successful login.
     */
    HOME(800, 600),

    /**
     * The screen for creating a new monitored directory.
     */
    NEW_DIRECTORY(400, 250),

    /**
     * The screen for viewing scan summaries.
     */
    SCAN_SUMMARY(800, 600);

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
