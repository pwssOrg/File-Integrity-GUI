package org.pwss.view.screen.splash_screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 * This class is responsible for displaying a splash screen: the splash picture
 * in a centred, rounded card with a slim progress bar underneath. The splash
 * screen will close automatically after 5 seconds or when the Enter key is
 * pressed.
 */
public final class FileIntegrityScannerSplashScreen {

    /**
     * Path to the splash screen picture resource.
     */
    private static final String SPLASH_SCREEN_PICTURE_PATH = "app_storage" + File.separator + "picture" + File.separator
            + "splashscreen" + File.separator + "file_integrity_scanner_splash_screen_m.JPG";

    /**
     * Largest width of the splash card, in pixels.
     */
    private static final int MAX_WIDTH = 760;

    /**
     * Corner radius of the splash card, in pixels.
     */
    private static final int CORNER_RADIUS = 22;

    /**
     * Height of the progress bar at the bottom of the card, in pixels.
     */
    private static final int PROGRESS_HEIGHT = 4;

    /**
     * Displays the splash screen, keeping the aspect ratio of the picture.
     */
    public static final void showSplash() {

        final ImageIcon imageIcon = new ImageIcon(SPLASH_SCREEN_PICTURE_PATH);
        final int imageWidth = Math.max(1, imageIcon.getIconWidth());
        final int imageHeight = Math.max(1, imageIcon.getIconHeight());

        // Fit the card on the screen without stretching the picture
        final Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        final int width = Math.min(MAX_WIDTH, (int) (screen.width * 0.8));
        final int height = Math.min((int) (screen.height * 0.8), width * imageHeight / imageWidth);

        final JWindow splash = new JWindow();
        final RoundRectangle2D cardShape = new RoundRectangle2D.Double(0, 0, width, height, CORNER_RADIUS,
                CORNER_RADIUS);

        final JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.clip(cardShape);
                g2.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                g2.dispose();
            }

            @Override
            protected void paintChildren(final Graphics g) {
                // Keep the progress bar inside the rounded corners as well
                final Graphics2D g2 = (Graphics2D) g.create();
                g2.clip(cardShape);
                super.paintChildren(g2);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(width, height));

        final JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setBorderPainted(false);
        progress.setOpaque(false);
        progress.setPreferredSize(new Dimension(width, PROGRESS_HEIGHT));
        card.add(progress, BorderLayout.SOUTH);

        splash.setContentPane(card);
        try {
            // Transparent window, so the rounded corners are smooth
            splash.setBackground(new Color(0, 0, 0, 0));
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            // No per-pixel transparency on this system: cut the window shape instead
            splash.setShape(cardShape);
        }
        splash.pack();
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
