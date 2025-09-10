package org.pwss.presenter.factory;


import org.pwss.navigation.Screen;
import org.pwss.presenter.*;
import org.pwss.view.screen.HomeScreen;
import org.pwss.view.screen.LoginScreen;
import org.pwss.view.screen.NewDirectoryScreen;
import org.pwss.view.screen.ScanDetailsScreen;

/**
 * A factory class responsible for creating presenters for different screens in the application.
 */
public class AppPresenterFactory implements PresenterFactory {

    /**
     * Creates a presenter for the specified screen.
     *
     * @param screen The `Screen` for which the presenter is to be created.
     * @return A `BasePresenter<?>` instance corresponding to the specified screen.
     */
    @Override
    public BasePresenter<?> createPresenter(Screen screen) {
        return switch (screen) {
            case LOGIN -> new LoginPresenter(new LoginScreen());
            case HOME -> new HomePresenter(new HomeScreen());
            case NEW_DIRECTORY -> new NewDirectoryPresenter(new NewDirectoryScreen());
            case SCAN_SUMMARY -> new ScanDetailsPresenter(new ScanDetailsScreen());
        };
    }
}
