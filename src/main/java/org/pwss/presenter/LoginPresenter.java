package org.pwss.presenter;

import javax.swing.SwingUtilities;

import org.pwss.exception.user.LoginException;
import org.pwss.exception.user.UserExistsLookupException;
import org.pwss.model.service.AuthService;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pwss.view.screen.LoginScreen;

import java.util.concurrent.ExecutionException;

public class LoginPresenter extends BasePresenter<LoginScreen> {
    /**
     * Indicates whether the application is in create user mode (i.e., no users exist yet).
     */
    private final boolean createUserMode;

    private final AuthService authService;

    /**
     * Constructs a LoginPresenter with the specified view and authentication service.
     *
     * @param view        The LoginView instance to be managed by this presenter.
     */
    public LoginPresenter(LoginScreen view) {
        super(view);
        this.authService = new AuthService();
        this.createUserMode = !checkUserExists();
        refreshView();
    }

    @Override
    protected void initListeners() {
        getScreen().getProceedButton().addActionListener(e -> onProceedButtonClick());
        getScreen().getCancelButton().addActionListener(e -> System.exit(0));
    }

    @Override
    protected void refreshView() {
        if (createUserMode) {
            // Notify user that no users exist and they need to create one
            getScreen().showInfo("No user found.\nCreate one by entering a username and password.");
            // Update message label to indicate user creation
            getScreen().setMessage("Create a user for the first login");
            // Change button text to "Register"
            getScreen().getProceedButton().setText("Register");
        } else {
            // Update message label to indicate normal login
            getScreen().setMessage("Login with your username and password.");
            // Change button text to "Login"
            getScreen().getProceedButton().setText("Login");
        }
    }

    /**
     * Checks if any users exist in the system.
     *
     * @return true if users exist, false otherwise.
     */
    private boolean checkUserExists() {
        try {
            return authService.userExists();
        } catch (UserExistsLookupException | ExecutionException e) {
            SwingUtilities.invokeLater(() ->
                    screen.showError("Error checking user existence: " + e.getMessage()));
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SwingUtilities.invokeLater(() ->
                    screen.showError("Operation interrupted: " + e.getMessage()));
            return true;
        } catch (Exception e) {
            SwingUtilities.invokeLater(() ->
                    screen.showError("An unexpected error occurred: " + e.getMessage()));
            return true;
        }
    }

    /**
     * Handles the proceed button click event.
     * Validates input and either creates a new user or performs login based on the mode.
     */
    private void onProceedButtonClick() {
        if (!validateInput()) {
            return; // Input validation failed, do not proceed
        }

        if (createUserMode) {
            // Handle user creation logic here and then login
            createUserAndLogin();
        } else {
            // Handle normal login logic here
            performLogin();
        }
    }

    /**
     * Validates the input fields for username and password.
     *
     * @return true if both fields are non-empty, false otherwise.
     */
    private boolean validateInput() {
        String username = getScreen().getUsername();
        String password = getScreen().getPassword();

        if (username == null || username.trim().isEmpty()) {
            getScreen().showError("Username cannot be empty.");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            getScreen().showError("Password cannot be empty.");
            return false;
        }

        return true;
    }

    /**
     * Creates a new user and then performs login if creation is successful.
     */
    private void createUserAndLogin() {
        if (createUser()) {
            performLogin();
        }
    }

    /**
     * Creates a new user with the provided username and password.
     *
     * @return true if user creation is successful, false otherwise.
     */
    private boolean createUser() {
        String username = getScreen().getUsername();
        String password = getScreen().getPassword();

        try {
            return authService.createUser(username, password);
        } catch (JsonProcessingException e) {
            SwingUtilities.invokeLater(() ->
                    screen.showError("Error preparing user creation request: " + e.getMessage()));
            return false;
        } catch (Exception e) {
            SwingUtilities.invokeLater(() ->
                    screen.showError("An unexpected error occurred: " + e.getMessage()));
            return false;
        }
    }

    /**
     * Performs the login operation using the provided username and password.
     * Displays success or error messages based on the outcome.
     */
    private void performLogin() {
        String username = getScreen().getUsername();
        String password = getScreen().getPassword();

        try {
            boolean loginSuccess = authService.login(username, password);

            SwingUtilities.invokeLater(() -> {
                if (loginSuccess) {
                    if (createUserMode) {
                        screen.showInfo("User created and logged in successfully!");
                    } else {
                        screen.showInfo("Logged in successfully!");
                    }
                    NavigationEvents.navigateTo(Screen.HOME);
                } else {
                    screen.showError("Invalid username or password.");
                }
            });

        } catch (JsonProcessingException e) {
            SwingUtilities.invokeLater(() ->
                    screen.showError("Error preparing login request: " + e.getMessage()));
        } catch (LoginException e) {
            SwingUtilities.invokeLater(() ->
                    screen.showError("Login request failed: " + e.getMessage()));
        } catch (Exception e) {
            SwingUtilities.invokeLater(() ->
                    screen.showError("An unexpected error occurred: " + e.getMessage()));
        }
    }
}
