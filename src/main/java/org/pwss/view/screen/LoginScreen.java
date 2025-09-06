package org.pwss.view.screen;

import javax.swing.*;

public class LoginScreen extends BaseScreen {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton proceedButton;
    private JButton cancelButton;
    private JLabel messageLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JPanel rootPanel;

    

    public LoginScreen() {
        this.proceedButton = new JButton();
        this.cancelButton = new JButton();
        this.usernameField = new JTextField();
        this.messageLabel = new JLabel();
        this.usernameLabel = new JLabel();
        this.passwordLabel = new JLabel();
        this.rootPanel = new JPanel();
    
    }

    @Override
    protected String getScreenName() {
        return "Authentication";
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public JButton getProceedButton() {
        return proceedButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void setMessage(String message) {
        this.messageLabel.setText(message);
    }
}
