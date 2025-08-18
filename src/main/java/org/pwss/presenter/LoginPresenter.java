package org.pwss.presenter;

import org.pwss.view.screen.LoginView;
import org.pwss.model.service.AuthService;

public class LoginPresenter {
    private final LoginView view;
    private final AuthService authService;

    public LoginPresenter(LoginView view, AuthService authService) {
        this.view = view;
        this.authService = authService;

        initListeners();
    }

    private void initListeners() {
        view.getLoginButton().addActionListener(e -> attemptLogin());
        view.getCancelButton().addActionListener(e -> System.exit(0));
    }

    private void attemptLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        // Feel Free To Delete / Modify my code and /or readd your code. Really nice work with thas class as well / Peter ;) 
        Boolean loginResult = authService.login(username, password);

        System.out.println(loginResult);

        /* 
        authService.loginAsync(username, password)
                .thenAccept(success -> javax.swing.SwingUtilities.invokeLater(() -> {
                    if (success) {
                        view.dispose(); // close login window
                        // TODO: show main application window
                    } else {
                        view.showError("Invalid username or password.");
                    }
                }))
                .exceptionally(ex -> {
                    javax.swing.SwingUtilities.invokeLater(() -> view.showError("Error: " + ex.getMessage()));
                    return null;
                });

                */
    }
}
