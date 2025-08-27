package org.pwss.presenter.factory;

import org.pwss.navigation.Screen;
import org.pwss.presenter.BasePresenter;

/**
 * A factory interface for creating presenters for different screens in the application.
 */
public interface PresenterFactory {
    /**
     * Creates a presenter for the specified screen.
     *
     * @param screen The `Screen` for which the presenter is to be created.
     * @return A `BasePresenter<?>` instance corresponding to the specified screen.
     */
    BasePresenter<?> createPresenter(Screen screen);
}
