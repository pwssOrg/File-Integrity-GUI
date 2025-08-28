package org.pwss.navigation;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.pwss.presenter.BasePresenter;
import org.pwss.presenter.factory.PresenterFactory;

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
     * Navigates to the specified screen by retrieving or creating its presenter,
     * updating the frame's content pane with the presenter's view, and refreshing the frame.
     *
     * @param screen The `Screen` to navigate to.
     */
    public void navigateTo(Screen screen) {
        BasePresenter<?> presenter = presenters.computeIfAbsent(screen, factory::createPresenter);
        JPanel view = presenter.getView();

        frame.getContentPane().removeAll();
        frame.getContentPane().add(view);
        frame.revalidate();
        frame.repaint();
    }
}
