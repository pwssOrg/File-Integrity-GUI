package org.pwss.navigation;

/**
 * Handles navigation events within the application.
 */
public class NavigationEvents {
    /**
     * The listener that will handle navigation events.
     */
    private static NavigationListener listener;

    /**
     * Sets the navigation listener to handle navigation events.
     *
     * @param l The `NavigationListener` instance to be set.
     */
    public static void setListener(NavigationListener l) {
        listener = l;
    }

    /**
     * Navigates to the specified screen by invoking the listener's `onNavigate` method.
     *
     * @param screen The `Screen` to navigate to.
     */
    public static void navigateTo(Screen screen) {
        if (listener != null) {
            listener.onNavigate(screen);
        }
    }
}
