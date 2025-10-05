package org.pwss.navigation;

import org.pwss.controller.util.NavigationContext;

/**
 * A listener interface for handling navigation events within the application.
 */
public interface NavigationListener {
    /**
     * Called when a navigation event occurs.
     *
     * @param screen  The `Screen` to navigate to.
     * @param context The `NavigationContext` containing data for the new screen.
     */
    void onNavigate(Screen screen, NavigationContext context);
}
