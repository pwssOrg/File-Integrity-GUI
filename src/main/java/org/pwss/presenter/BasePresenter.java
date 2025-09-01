package org.pwss.presenter;

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
     * Constructs a `BasePresenter` with the specified view.
     * Initializes the view and sets up event listeners.
     *
     * @param screen The `Screen` instance to be managed by this presenter.
     */
    public BasePresenter(Screen screen) {
        this.screen = screen;
        initListeners();
    }

    /**
     * Abstract method to initialize event listeners for the Screen.
     * Subclasses must provide an implementation for this method.
     */
    protected abstract void initListeners();

    /**
     * Retrieves the view managed by this presenter.
     *
     * @return The `Screen` instance representing the managed view.
     */
    public Screen getScreen() {
        return screen;
    }
}
