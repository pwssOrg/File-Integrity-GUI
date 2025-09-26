package org.pwss.navigation;

import org.pwss.controller.util.NavigationContext;

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
     * @param context The `NavigationContext` containing data for the new screen.
     */
    public static void navigateTo(Screen screen, NavigationContext context) {
        if (listener != null) {
            listener.onNavigate(screen, context);
        }
    }
}
