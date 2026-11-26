package org.pwss;

import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.pwss.app_settings.AppConfig;
import org.pwss.controller.factory.AppControllerFactory;
import org.pwss.controller.factory.ControllerFactory;
import org.pwss.exception.start.FailedToLaunchAppException;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.NavigationHandler;
import org.pwss.navigation.Screen;
import org.pwss.util.OSUtil;
import org.pwss.view.screen.splash_screen.FileIntegrityScannerSplashScreen;
import org.pwss.view.theme.AppLookAndFeel;
import org.slf4j.LoggerFactory;

/**
 * This class represents the frontend application for the PWSS File Integrity
 * Scanner.
 */
class FileIntegrityScannerFrontend {

    /**
     * Starts the File Integrity Scanner Frontend Application.
     *
     * @throws FailedToLaunchAppException if there is an error initializing the LookAndFeel or
     *                   launching the application
     */
    final static void StartApplication() throws FailedToLaunchAppException {
        final org.slf4j.Logger log = LoggerFactory.getLogger(FileIntegrityScannerFrontend.class);
        log.debug("Starting File-Integrity Scanner Frontend Application");
        log.debug("OS Name: {}", OSUtil.getOSName());
        try {
            // Set the look and feel first, so the splash screen is themed as well
            if (!AppLookAndFeel.install(AppConfig.APP_THEME)) {
                log.error("Failed to initialize LaF, falling back to the default look");
            }
            if (AppConfig.USE_SPLASH_SCREEN) {
                FileIntegrityScannerSplashScreen.showSplash();
                Thread.sleep(4000);
            }
            // Create Main UI of the application on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                // Create the main frame
                JFrame frame = new JFrame("Integrity Hash");
                frame.setResizable(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Application icon
                try {
                    Image icon = ImageIO.read(Objects.requireNonNull(Start.class.getResource("/app-icon.png")));
                    frame.setIconImage(icon);
                } catch (IOException e) {
                    log.debug("Failed to load application icon", e);
                    log.error("Failed to load application icon: {}", e.getMessage());
                }

                // Create controller factory
                final ControllerFactory factory = new AppControllerFactory();

                // Create navigation handler
                final NavigationHandler navigator = new NavigationHandler(frame, factory);

                // Hook navigation listener so controller can signal navigation
                NavigationEvents.setListener(navigator::navigateTo);

                // Start with Login screen
                navigator.navigateTo(Screen.LOGIN, null);

                // Finally, show the main frame hosting the screens :)
                frame.setVisible(true);
            });
        } catch (Exception ex) {
            log.debug("Failed to initialize LaF", ex);
            log.error("Failed to initialize LaF", ex.getMessage());
            throw new FailedToLaunchAppException(ex);
        }

    }

}
