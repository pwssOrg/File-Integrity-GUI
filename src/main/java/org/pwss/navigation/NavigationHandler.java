package org.pwss.navigation;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
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
     * The size the frame was last given by a navigation, or null before the
     * first navigation.
     */
    private Dimension lastScreenSize;

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
        baseScreen.setParentFrame(frame);
        // Ensure the controller reloads its data when navigating to the screen
        controller.reloadData();

        frame.getContentPane().removeAll();
        resizeFrameFor(screen);
        frame.getContentPane().add(baseScreen.getRootPanel());
        frame.revalidate();
        frame.repaint();

        // Notify the controller that its screen is now visible
        controller.onShow();
    }

    /**
     * Sizes and centres the frame for the given screen.
     * The frame is left alone when it is maximized, or when the screen wants the
     * same size as the previous one (so a size chosen by the user is kept).
     *
     * @param screen The `Screen` that is about to be shown.
     */
    private void resizeFrameFor(Screen screen) {
        final Rectangle available = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        final Dimension size = new Dimension(
                Math.min(screen.frameWidth, available.width),
                Math.min(screen.frameHeight, available.height));
        final boolean maximized = (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;
        if (!maximized && !size.equals(lastScreenSize)) {
            frame.setSize(size);
            frame.setLocationRelativeTo(null); // center on screen
        }
        lastScreenSize = size;
    }
}
