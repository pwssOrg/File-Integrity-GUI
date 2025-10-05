package org.pwss.navigation;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import org.pwss.controller.BaseController;
import org.pwss.controller.factory.ControllerFactory;
import org.pwss.controller.util.NavigationContext;
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
     * A map that associates screens with their corresponding controllers.
     */
    private final Map<Screen, BaseController<?>> controllers = new HashMap<>();

    /**
     * A factory for creating controllers for the screens.
     */
    private final ControllerFactory factory;

    /**
     * Constructs a `NavigationHandler` with the specified frame and controller factory.
     *
     * @param frame   The `JFrame` where views will be displayed.
     * @param factory The `ControllerFactory` used to create controllers for screens.
     */
    public NavigationHandler(JFrame frame, ControllerFactory factory) {
        this.frame = frame;
        this.factory = factory;
    }

    /**
     * Navigates to the specified screen, creating its controller if it doesn't already exist.
     * The controller is set with the provided navigation context, and its data is reloaded.
     *
     * @param screen  The `Screen` to navigate to.
     * @param context The `NavigationContext` containing data for the new screen.
     */
    public void navigateTo(Screen screen, NavigationContext context) {
        BaseController<?> controller = controllers.computeIfAbsent(screen, factory::createController);
        // Set the navigation context for the controller
        controller.setContext(context);
        BaseScreen baseScreen = controller.getScreen();
        // Ensure the controller reloads its data when navigating to the screen
        controller.reloadData();

        frame.getContentPane().removeAll();
        frame.setSize(screen.frameWidth, screen.frameHeight);
        frame.getContentPane().add(baseScreen.getRootPanel());
        frame.revalidate();
        frame.repaint();

        // Notify the controller that its screen is now visible
        controller.onShow();
    }
}
