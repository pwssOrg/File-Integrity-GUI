package org.pwss.presenter.factory;

import org.pwss.model.service.AuthService;
import org.pwss.model.service.ScanService;
import org.pwss.navigation.Screen;
import org.pwss.presenter.BasePresenter;
import org.pwss.presenter.LoginPresenter;
import org.pwss.presenter.HomePresenter;
import org.pwss.view.screen.HomeScreen;
import org.pwss.view.screen.LoginScreen;

/**
 * A factory class responsible for creating presenters for different screens in the application.
 */
public class AppPresenterFactory implements PresenterFactory {

    /**
     * Creates a presenter for the specified screen.
     *
     * @param screen The `Screen` for which the presenter is to be created.
     * @return A `BasePresenter<?>` instance corresponding to the specified screen.
     * @throws IllegalArgumentException If the specified screen is unknown.
     */
    @Override
    public BasePresenter<?> createPresenter(Screen screen) {
        switch (screen) {
            case LOGIN:
                return new LoginPresenter(new LoginScreen(), new AuthService());
            case HOME:
                return new HomePresenter(new HomeScreen(), new ScanService());
            default:
                throw new IllegalArgumentException("Unknown screen: " + screen);
        }
    }
}
