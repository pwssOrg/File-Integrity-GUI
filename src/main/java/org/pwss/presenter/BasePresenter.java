package org.pwss.presenter;

import javax.swing.JFrame;

/**
 * An abstract base class for presenters in the application.
 * Provides a common structure for managing a `JFrame` view and initializing event listeners.
 */
public abstract class BasePresenter<View extends JFrame> {
    /**
     * The view instance managed by this presenter.
     * Represents the UI component associated with this presenter.
     */
    protected View view;

    /**
     * Constructs a `BasePresenter` with the specified view.
     * Initializes the view and sets up event listeners.
     *
     * @param view The `View` instance to be managed by this presenter.
     */
    public BasePresenter(View view) {
        this.view = view;
        initListeners();
    }

    /**
     * Abstract method to initialize event listeners for the view.
     * Subclasses must provide an implementation for this method.
     */
    protected abstract void initListeners();

    /**
     * Retrieves the view managed by this presenter.
     *
     * @return The `View` instance representing the managed view.
     */
    protected View getView() {
        return view;
    }
}
