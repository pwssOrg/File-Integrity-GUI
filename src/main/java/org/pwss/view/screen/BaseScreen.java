package org.pwss.view.screen;

import javax.swing.*;

public abstract class BaseScreen extends JPanel {

    /**
     * Get the screen name for dialog titles & logging purposes.
     *
     * @return The name of the screen.
     */
    protected abstract String getScreenName();

    /**
     * Show a success message dialog.
     *
     * @param message The message to display.
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, getScreenName(), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show an error message dialog.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, getScreenName(), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show an informational message dialog.
     *
     * @param message The message to display.
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, getScreenName(), JOptionPane.INFORMATION_MESSAGE);
    }
}
