package org.pwss.controller.factory;

import org.pwss.controller.BaseController;
import org.pwss.navigation.Screen;

/**
 * A factory interface for creating controllers for different screens in the application.
 *
 * @author PWSS ORG
 */
public interface ControllerFactory {
    /**
     * Creates a Controller for the specified screen.
     *
     * @param screen The `Screen` for which the controller is to be created.
     * @return A `BaseController<?>` instance corresponding to the specified screen.
     */
    BaseController<?> createController(Screen screen);
}
