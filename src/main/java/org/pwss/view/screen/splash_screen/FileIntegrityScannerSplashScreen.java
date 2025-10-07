package org.pwss.view.screen.splash_screen;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 * This class is responsible for displaying a splash screen with an image that
 * scales to fit the entire screen. The splash screen will close automatically
 * after 5 seconds or when the Enter key is pressed.
 */
public final class FileIntegrityScannerSplashScreen {

    /**
     * Path to the splash screen picture resource.
     */
    private static final String SPLASH_SCREEN_PICTURE_PATH = "src" + File.separator + "main" + File.separator
            + "resources" + File.separator + "file_integrity_scanner_splash_screen_m.JPG";

    /**
     * Displays the splash screen with a specified image that scales to fit the
     * entire screen.
     */
    public static final void showSplash() {

        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice gd = ge.getDefaultScreenDevice();
        final DisplayMode dm = gd.getDisplayMode();

        final int screenWidth = dm.getWidth();
        final int screenHeight = dm.getHeight();

        final JWindow splash = new JWindow();
        splash.setSize(screenWidth, screenHeight);

        final ImageIcon imageIcon = new ImageIcon(SPLASH_SCREEN_PICTURE_PATH);
        final JLabel label = new JLabel(imageIcon);

        // Scale the image to fit within the desired size (maintaining aspect ratio)
        final Image scaledImage = imageIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);

        imageIcon.setImage(scaledImage);

        splash.getContentPane().add(label);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        // Timer to close the splash screen after 5 seconds or when Enter is pressed
        final Timer timer = new Timer(5000, null);
        final ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                splash.dispose();
                timer.stop();
            }
        };
        timer.addActionListener(actionListener);

        // Listen for Enter key press to close the splash screen early
        splash.getContentPane().setFocusable(true);
        splash.getContentPane().requestFocusInWindow();
        final KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionListener.actionPerformed(null); // Close the splash screen
                }
            }
        };
        splash.getContentPane().addKeyListener(keyAdapter);

        timer.start();
    }

}
