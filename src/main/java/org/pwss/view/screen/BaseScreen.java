package org.pwss.view.screen;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Base class for all screens in the application.
 * Provides common functionality such as displaying message dialogs.
 *
 * @author PWSS ORG
 */
public abstract class BaseScreen extends JPanel {
    /**
     * The parent JFrame of this screen.
     */
    private JFrame parentFrame;

    /**
     * Set the parent JFrame of this screen.
     *
     * @param parentFrame The parent JFrame to set.
     */
    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    /**
     * Get the parent JFrame of this screen.
     *
     * @return The parent JFrame.
     */
    public JFrame getParentFrame() {
        return parentFrame;
    }

    /**
     * Get the screen name for dialog titles & logging purposes.
     *
     * @return The name of the screen.
     */
    public abstract String getScreenName();


    /**
     * Get the root panel of the screen.
     *
     * @return The root JPanel.
     */
    public abstract JPanel getRootPanel();

    /**
     * Show a success message dialog.
     *
     * @param message The message to display.
     */
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, getScreenName(), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show a warning message dialog.
     *
     * @param message The warning message to display.
     */
    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, getScreenName(), JOptionPane.WARNING_MESSAGE);
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

    /**
     * Show an option dialog with custom buttons.
     *
     * @param dialogType    The type of dialog (e.g., JOptionPane.WARNING_MESSAGE).
     * @param message       The message to display.
     * @param options       The button labels.
     * @param defaultOption The default selected option (can be null).
     * @return The index of the option chosen, or -1 if closed.
     */
    public int showOptionDialog(int dialogType, String message, String[] options, String defaultOption) {
        return JOptionPane.showOptionDialog(
                this,
                message,
                getScreenName(),
                JOptionPane.DEFAULT_OPTION,
                dialogType,
                null,
                options,
                defaultOption
        );
    }
}
