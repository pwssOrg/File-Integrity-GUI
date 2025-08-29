package org.pwss.presenter;

import javax.swing.SwingUtilities;

import org.pwss.exception.user.LoginException;
import org.pwss.model.service.AuthService;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.view.screen.LoginView;

import com.fasterxml.jackson.core.JsonProcessingException;

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
                    NavigationEvents.navigateTo(Screen.HOME);
                } else {
                    view.showError("Invalid username or password.");
                }
            });

        } catch (JsonProcessingException e) {
            SwingUtilities.invokeLater(() ->
                    view.showError("Error preparing login request: " + e.getMessage()));
        } catch (LoginException e) {
            SwingUtilities.invokeLater(() ->
                    view.showError("Login request failed: " + e.getMessage()));
        } catch (Exception e) {
            SwingUtilities.invokeLater(() ->
                    view.showError("An unexpected error occurred: " + e.getMessage()));
        }
    }
}
