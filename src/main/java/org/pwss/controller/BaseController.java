package org.pwss.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.pwss.controller.util.NavigationContext;
import org.pwss.view.screen.BaseScreen;


/**
 * An abstract base class for controllers in the application.
 * Provides a common structure for managing a `BaseScreen` view and initializing event listeners.
 *
 * @author PWSS ORG
 */
public abstract class BaseController<Screen extends BaseScreen> {
    /**
     * The view instance managed by this controller.
     * Represents the UI component associated with this controller.
     */
    protected Screen screen;
    /**
     * The navigation context for passing data between different parts of the application during navigation.
     */
    private NavigationContext context;
    /**
     * List of SubControllers managed by this BaseController.
     */
    private final List<SubController<?, ? extends BaseController<?>>> subControllers;
    /**
     * Logger instance for logging purposes.
     */
    private final org.slf4j.Logger log;

    /**
     * Constructs a `BaseController` with the specified view.
     * Initializes the view and sets up event listeners.
     *
     * @param screen The `Screen` instance to be managed by this controller.
     */
    public BaseController(Screen screen) {
        this.screen = screen;
        this.log = org.slf4j.LoggerFactory.getLogger(BaseController.class);
        this.subControllers = new ArrayList<>();
        // Run onCreate lifecycle method
        onCreate();
        // Initialize event listeners
        initListeners();
    }
    /**
     * Retrieves the current navigation context.
     *
     * @return The `NavigationContext` instance associated with this controller.
     */
    protected NavigationContext getContext() {
        return context;
    }
    /**
     * Sets the navigation context for the controller.
     *
     * @param context
     */
    public void setContext(NavigationContext context) {
        this.context = context;
    }
    /**
     * Adds a subcontroller to this controller.
     *
     * @param subController The subcontroller to add.
     */
    public void addSubController(SubController<?, ? extends BaseController<?>> subController) {
        if (subController == null) {
            log.warn("Attempted to add a null subcontroller to {}", getClass().getSimpleName());
            return;
        }
        subControllers.add(subController);
        log.debug("Added SubController: {} to {}", subController.getClass().getSimpleName(), getClass().getSimpleName());
    }
    /**
     * Abstract method to initialize event listeners for the Screen.
     * Subclasses must provide an implementation for this method.
     */
    abstract void initListeners();
    /**
     * Abstract method to refresh or update the view.
     * Subclasses must provide an implementation for this method.
     */
    abstract void refreshView();
    /**
     * Method to reload or refresh data displayed in the view.
     * Subclasses can override this method to provide specific data reloading logic.
     */
    public void reloadData() {
        log.debug("reloadData called for {}", screen.getScreenName());
        subControllers.forEach(SubController::reloadData);
    }
    /**
     * Retrieves the view managed by this controller.
     *
     * @return The `Screen` instance representing the managed view.
     */
    public Screen getScreen() {
        return screen;
    }
    /**
     * Method called when the view is shown.
     * Subclasses can override this method to perform actions when the view becomes visible.
     */
    public void onShow() {
        log.debug("onShow called for {}", screen.getScreenName());
        subControllers.forEach(SubController::onShow);
    }

    /**
     * Method called when the controller is created.
     * Subclasses can override this method to perform setup logic.
     */
    abstract void onCreate();
}
