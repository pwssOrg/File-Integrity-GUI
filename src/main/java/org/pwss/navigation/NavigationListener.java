package org.pwss.navigation;

/**
 * A listener interface for handling navigation events within the application.
 */
public interface NavigationListener {
    /**
     * Called when a navigation event occurs.
     *
     * @param screen The `Screen` to navigate to.
     */
    void onNavigate(Screen screen);
}
