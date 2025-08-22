package org.pwss.presenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pwss.exception.LoginFailedException;
import org.pwss.model.service.AuthService;
import org.pwss.model.service.ScanService;
import org.pwss.view.screen.LoginView;
import org.pwss.view.screen.ScanView;

import javax.swing.*;

public class LoginPresenter extends BasePresenter<LoginView> {
    private final AuthService authService;

    public LoginPresenter(LoginView view, AuthService authService) {
        super(view);
        this.authService = authService;
    }

    @Override
    protected void initListeners() {
        getView().getLoginButton().addActionListener(e -> performLogin());
        getView().getCancelButton().addActionListener(e -> System.exit(0));
    }

    private void performLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        try {
            boolean loginSuccess = authService.login(username, password);

            SwingUtilities.invokeLater(() -> {
                if (loginSuccess) {
                    view.showSuccess("Login successful!");
                    navigateToScanView();
                } else {
                    view.showError("Invalid username or password.");
                }
            });

        } catch (JsonProcessingException e) {
            SwingUtilities.invokeLater(() ->
                    view.showError("Error preparing login request: " + e.getMessage()));
        } catch (LoginFailedException e) {
            SwingUtilities.invokeLater(() ->
                    view.showError("Login request failed: " + e.getMessage()));
        } catch (Exception e) {
            SwingUtilities.invokeLater(() ->
                    view.showError("An unexpected error occurred: " + e.getMessage()));
        }
    }

    private void navigateToScanView() {
        ScanView scanView = new ScanView();
        ScanService scanService = new ScanService();
        new ScanPresenter(scanView, scanService);

        view.setVisible(false);
        scanView.setVisible(true);
    }
}
