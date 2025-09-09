package org.pwss.navigation;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;


import org.pwss.presenter.BasePresenter;
import org.pwss.presenter.factory.PresenterFactory;
import org.pwss.presenter.util.NavigationContext;
import org.pwss.view.screen.BaseScreen;

/**
 * Handles navigation between different screens in the application.
 */
public class NavigationHandler {
    /**
     * The main application frame where views are displayed.
     */
    private final JFrame frame;

    /**
     * A map that associates screens with their corresponding presenters.
     */
    private final Map<Screen, BasePresenter<?>> presenters = new HashMap<>();

    /**
     * A factory for creating presenters for the screens.
     */
    private final PresenterFactory factory;

    /**
     * Constructs a `NavigationHandler` with the specified frame and presenter factory.
     *
     * @param frame   The `JFrame` where views will be displayed.
     * @param factory The `PresenterFactory` used to create presenters for screens.
     */
    public NavigationHandler(JFrame frame, PresenterFactory factory) {
        this.frame = frame;
        this.factory = factory;
    }

    /**
     * Navigates to the specified screen, creating its presenter if it doesn't already exist.
     * The presenter is set with the provided navigation context, and its data is reloaded.
     *
     * @param screen  The `Screen` to navigate to.
     * @param context The `NavigationContext` containing data for the new screen.
     */
    public void navigateTo(Screen screen, NavigationContext context) {
        BasePresenter<?> presenter = presenters.computeIfAbsent(screen, factory::createPresenter);
        // Set the navigation context for the presenter
        presenter.setContext(context);
        BaseScreen baseScreen = presenter.getScreen();
        // Ensure the presenter reloads its data when navigating to the screen
        presenter.reloadData();

        frame.getContentPane().removeAll();
        frame.setSize(screen.frameWidth, screen.frameHeight);
        frame.getContentPane().add(baseScreen.getRootPanel());
        frame.revalidate();
        frame.repaint();

        // Notify the presenter that its screen is now visible
        presenter.onShow();
    }
}
