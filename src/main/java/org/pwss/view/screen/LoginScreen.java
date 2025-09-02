package org.pwss.view.screen;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginScreen extends BaseScreen {
    private final JLabel messageLabel = new JLabel("", JLabel.CENTER);
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton proceedButton = new JButton("");
    private final JButton cancelButton = new JButton("Cancel");

    public LoginScreen() {
        setLayout(new BorderLayout()); // fills parent

        // Inner panel (form fields)
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Message label (spanning 2 columns)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(messageLabel, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Username"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(proceedButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Wrapper panel with GridBagLayout centers content
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(formPanel, new GridBagConstraints());

        // Add wrapper to fill the parent
        add(wrapper, BorderLayout.CENTER);
    }

    @Override
    protected String getScreenName() {
        return "Authentication";
    }

    /**
     * Get trimmed username
     *
     * @return trimmed username from input field
     */
    public String getUsername() {
        return usernameField.getText().trim();
    }

    /**
     * Get password as String
     *
     * @return password from input field
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    /**
     * Get proceed button for access in presenter
     *
     * @return JButton instance of proceed button in view hierarchy
     */
    public JButton getProceedButton() {
        return proceedButton;
    }

    /**
     * Get cancel button for access in presenter
     *
     * @return JButton instance of cancel button in view hierarchy
     */
    public JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Set message in message label
     *
     * @param message message to set in label
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
