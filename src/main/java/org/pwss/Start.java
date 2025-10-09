package org.pwss;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pwss.app_settings.AppConfig;
import org.pwss.controller.factory.AppControllerFactory;
import org.pwss.controller.factory.ControllerFactory;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.NavigationHandler;
import org.pwss.navigation.Screen;
import org.pwss.view.screen.splash_screen.FileIntegrityScannerSplashScreen;
import org.slf4j.LoggerFactory;

public class Start {
    public static void main(String[] args) {
        final org.slf4j.Logger log = LoggerFactory.getLogger(Start.class);
        try {
            if (AppConfig.USE_SPLASH_SCREEN) {
                FileIntegrityScannerSplashScreen.showSplash();
                Thread.sleep(4000);
            }
            // Set FlatLaf Look and Feel
            if (AppConfig.APP_THEME == 1)
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            else if (AppConfig.APP_THEME == 2)
                UIManager.setLookAndFeel(new FlatLightLaf());
            else if (AppConfig.APP_THEME == 3)
                UIManager.setLookAndFeel(new FlatMacLightLaf());
            else if (AppConfig.APP_THEME == 4)
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
            // Create Main UI of the application on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                // Create the main frame
                JFrame frame = new JFrame("File Integrity Scanner");
                frame.setResizable(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null); // center on screen

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
        }
    }
}