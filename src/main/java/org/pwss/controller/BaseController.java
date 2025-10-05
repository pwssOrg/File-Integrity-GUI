package org.pwss.controller;

import org.pwss.controller.util.NavigationContext;
import org.pwss.view.screen.BaseScreen;


/**
 * An abstract base class for controllers in the application.
 * Provides a common structure for managing a `BaseScreen` view and initializing event listeners.
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
     * Constructs a `BaseController` with the specified view.
     * Initializes the view and sets up event listeners.
     *
     * @param screen The `Screen` instance to be managed by this controller.
     */
    public BaseController(Screen screen) {
        this.screen = screen;
        // Initialize event listeners
        initListeners();
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
     * The default implementation does nothing.
     */
    public void reloadData() {
        // Default implementation does nothing
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
     * The default implementation does nothing.
     */
    public void onShow() {
        // Default implementation does nothing
    }
}
