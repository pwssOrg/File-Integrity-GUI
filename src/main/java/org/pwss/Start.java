package org.pwss;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.NavigationHandler;
import org.pwss.navigation.Screen;
import org.pwss.controller.factory.AppControllerFactory;
import org.pwss.controller.factory.ControllerFactory;

public class Start {
    public static void main(String[] args) {
        try {
            // Set FlatLaf Look and Feel
            UIManager.setLookAndFeel(new FlatDarculaLaf());

            // Create Main UI of the application on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                // Create the main frame
                JFrame frame = new JFrame("File Integrity Scanner");
                frame.setResizable(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null); // center on screen

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
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
    }
}