package org.pwss;

import org.pwss.model.service.AuthService;
import org.pwss.presenter.LoginPresenter;
import org.pwss.view.screen.LoginView;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            AuthService authService = new AuthService();
            new LoginPresenter(loginView, authService);

            loginView.setVisible(true);
        });
    }
}