package org.pwss.presenter;

import org.pwss.presenter.util.NavigationContext;
import org.pwss.view.screen.BaseScreen;


/**
 * An abstract base class for presenters in the application.
 * Provides a common structure for managing a `BaseScreen` view and initializing event listeners.
 */
public abstract class BasePresenter<Screen extends BaseScreen> {
    /**
     * The view instance managed by this presenter.
     * Represents the UI component associated with this presenter.
     */
    protected Screen screen;

    /**
     * The navigation context for passing data between different parts of the application during navigation.
     */
    private NavigationContext context;

    /**
     * Retrieves the current navigation context.
     *
     * @return The `NavigationContext` instance associated with this presenter.
     */
    protected NavigationContext getContext() {
        return context;
    }

    /**
     * Sets the navigation context for the presenter.
     *
     * @param context
     */
    public void setContext(NavigationContext context) {
        this.context = context;
    }

    /**
     * Constructs a `BasePresenter` with the specified view.
     * Initializes the view and sets up event listeners.
     *
     * @param screen The `Screen` instance to be managed by this presenter.
     */
    public BasePresenter(Screen screen) {
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
     * Retrieves the view managed by this presenter.
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
