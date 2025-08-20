package org.pwss.presenter;

import org.pwss.model.service.ScanService;
import org.pwss.view.screen.LoginView;
import org.pwss.model.service.AuthService;
import org.pwss.view.screen.ScanView;

import javax.swing.SwingUtilities;

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

        authService.loginAsync(username, password)
                .thenAccept(success -> SwingUtilities.invokeLater(() -> {
                    if (success) {
                        view.showSuccess("Login successful!");
                        // I am really not happy with this, I will try to come up with a proper navigation solution
                        // To make this more maintainable and look more professional
                        SwingUtilities.invokeLater(() -> {
                            ScanView scanView = new ScanView();
                            ScanService scanService = new ScanService();
                            new ScanPresenter(scanView, scanService);

                            view.setVisible(false); // Hide login view
                            scanView.setVisible(true); // Show scan view
                        });
                    } else {
                        view.showError("Invalid username or password.");
                    }
                }))
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> view.showError("Error: " + ex.getMessage()));
                    return null;
                });
    }
}
