package org.pwss;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.NavigationHandler;
import org.pwss.navigation.Screen;
import org.pwss.presenter.factory.AppPresenterFactory;
import org.pwss.presenter.factory.PresenterFactory;

public class Start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame frame = new JFrame("Scan Integrity Scanner");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(350, 250);
            frame.setLocationRelativeTo(null); // center on screen

            // Create presenter factory
            PresenterFactory factory = new AppPresenterFactory();

            // Create navigation handler
            NavigationHandler navigator = new NavigationHandler(frame, factory);

            // Hook navigation listener so presenters can signal navigation
            NavigationEvents.setListener(navigator::navigateTo);

            // Start with Login screen
            navigator.navigateTo(Screen.LOGIN);

            // Finally, show the main frame hosting the screens :)
            frame.setVisible(true);
        });
    }
}