package org.pwss.controller.factory;


import org.pwss.navigation.Screen;
import org.pwss.controller.*;
import org.pwss.view.screen.HomeScreen;
import org.pwss.view.screen.LoginScreen;
import org.pwss.view.screen.NewDirectoryScreen;
import org.pwss.view.screen.ScanDetailsScreen;

/**
 * A factory class responsible for creating controllers for different screens in the application.
 * @author PWSS ORG
 */
public class AppControllerFactory implements ControllerFactory {

    /**
     * Creates a controller for the specified screen.
     *
     * @param screen The `Screen` for which the controller is to be created.
     * @return A `BaseController<?>` instance corresponding to the specified screen.
     */
    @Override
    public BaseController<?> createController(Screen screen) {
        return switch (screen) {
            case LOGIN -> new LoginController(new LoginScreen());
            case HOME -> new HomeController(new HomeScreen());
            case NEW_DIRECTORY -> new NewDirectoryController(new NewDirectoryScreen());
            case SCAN_SUMMARY -> new ScanDetailsController(new ScanDetailsScreen());
        };
    }
}
