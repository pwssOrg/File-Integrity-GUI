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

//TODO;: Add Java Docs
public final class FileIntegrityScannerSplashScreen {

    // TODO;: Add Java Docs
    private static final String SPLASH_SCREEN_PICTURE_PATH = "src" + File.separator + "main" + File.separator
            + "resources" + File.separator + "file_integrity_scanner_splash_screen_m.JPG";

    // TODO;: Add Java Docs
    public static final void showSplash() {

        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Get the screen dimensions using the default configuration
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();

        int screenWidth = dm.getWidth();
        int screenHeight = dm.getHeight();

        JWindow splash = new JWindow();
        splash.setSize(screenWidth, screenHeight); // Set size of the splash screen

        // Create image panel for the splash screen
        ImageIcon imageIcon = new ImageIcon(SPLASH_SCREEN_PICTURE_PATH);

        JLabel label = new JLabel(imageIcon);

        // Scale the image to fit within the desired size (maintaining aspect ratio)
        final Image scaledImage = imageIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);

        imageIcon.setImage(scaledImage);

        splash.getContentPane().add(label);
        splash.setLocationRelativeTo(null); // Center on screen
        splash.setVisible(true);

        // Timer to close the splash screen after 5 seconds or when Enter is pressed
        Timer timer = new Timer(5000, null);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splash.dispose();
                timer.stop();
            }
        };
        timer.addActionListener(actionListener);

        // Listen for Enter key press to close the splash screen early
        splash.getContentPane().setFocusable(true);
        splash.getContentPane().requestFocusInWindow();
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionListener.actionPerformed(null); // Close the splash screen
                }
            }
        };
        splash.getContentPane().addKeyListener(keyAdapter);

        timer.start();
    }

}
