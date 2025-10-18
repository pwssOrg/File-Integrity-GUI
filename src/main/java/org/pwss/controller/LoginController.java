package org.pwss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import org.pwss.app_settings.AppConfig;
import org.pwss.exception.user.CreateUserException;
import org.pwss.exception.user.LoginException;
import org.pwss.exception.user.UserExistsLookupException;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.service.AuthService;
import org.pwss.view.screen.LoginScreen;
import org.slf4j.LoggerFactory;


import static org.pwss.app_settings.AppConfig.LICENSE_KEY;

/**
 * The LoginController class manages user login operations and interacts with
 * the LoginScreen.
 */
public class LoginController extends BaseController<LoginScreen> {

    /**
     * Logger for logging messages within this controller.
     */
    private final org.slf4j.Logger log = LoggerFactory.getLogger(LoginController.class);

    /**
     * Indicates whether the application is in create user mode (i.e., no users
     * exist yet).
     */
    private final boolean createUserMode;

    /**
     * Service for handling authentication-related operations.
     */
    private final AuthService authService;

    /**
     * Indicates whether the LICENSE_KEY is set in the application configuration.
     */
    private final boolean licenseKeySet;

    /**
     * Constructs a LoginController with the specified view and authentication
     * service.
     *
     * @param view The LoginView instance to be managed by this Controller.
     */
    public LoginController(LoginScreen view) {
        super(view);
        this.authService = new AuthService();
        this.createUserMode = !checkUserExists();
        this.licenseKeySet = !LICENSE_KEY.isBlank();
    }

    @Override
    public void onShow() {
        getScreen().getUsernameField().setText("");
        getScreen().getPasswordField().setText("");
        log.debug("Current LICENSE_KEY: {}", licenseKeySet ? "SET" : "NOT SET");
        log.debug("Create User Mode: {}", createUserMode);
        refreshView();
    }

    @Override
    protected void initListeners() {
        getScreen().getPasswordField().addActionListener(e -> proceedAndValidate());
        getScreen().getProceedButton().addActionListener(e -> proceedAndValidate());
        getScreen().getCancelButton().addActionListener(e -> System.exit(0));
    }

    @Override
    protected void refreshView() {
        // Only show license key fields if LICENSE_KEY is not set
        if (licenseKeySet) {
            getScreen().getLicenseLabel().setVisible(false);
            getScreen().getLicenseKeyField().setVisible(false);
        }
        // Update the view based on whether we are in create user mode
        if (createUserMode) {
            // Notify user that no users exist and they need to create one
            getScreen().showInfo("No user found.\nCreate one by entering a username and password.");
            // Update message label to indicate user creation
            getScreen().setMessage("Create a user for the first login.");
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
            /**
             * This will happen from time to time with the Auto Start Script.
             * That is why debug log is used here!
             */
            log.debug("Error checking user existence: {}", e.getMessage());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.debug("Operation interrupted", e);
            log.error("Operation interrupted", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError("Operation interrupted"));
            return true;
        } catch (Exception e) {
            log.error("An unexpected error occurred {}", e.getMessage());
            log.debug("Debug An unexpected error occurred", e);
            SwingUtilities.invokeLater(() -> screen.showError("An unexpected error occurred"));
            return true;
        }
    }

    /**
     * Handles the proceed button click event.
     * Validates input and either creates a new user or performs login based on the
     * mode.
     */
    private void proceedAndValidate() {
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
        String licenseKey = licenseKeySet ? LICENSE_KEY : getScreen().getLicenseKey();

        if (username == null || username.trim().isEmpty()) {
            getScreen().showError("Username cannot be empty.");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            getScreen().showError("Password cannot be empty.");
            return false;
        }

        if (licenseKey == null || licenseKey.trim().isEmpty()) {
            getScreen().showError("License Key cannot be empty.");
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
        String licenseKey = licenseKeySet ? LICENSE_KEY : getScreen().getLicenseKey();

        try {
            final boolean createSuccess = authService.createUser(username, password, licenseKey);
            if (createSuccess) {
                // Persist the license key if user creation is successful
                AppConfig.setLicenseKey(licenseKey);
                return true;
            } else {
                SwingUtilities.invokeLater(() -> screen.showError("User creation failed."));
                return false;
            }
        } catch (JsonProcessingException e) {
            log.debug("Error preparing user creation request", e);
            log.error("Error preparing user creation request: {}", e.getMessage());
            SwingUtilities
                    .invokeLater(() -> screen.showError("Error preparing user creation request"));
            return false;
        } catch (CreateUserException e) {
            log.debug("User creation request failed", e);
            log.error("User creation request failed {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError(e.getMessage()));
            return false;
        }catch (Exception e) {
            log.debug("An unexpected error occurred", e);
            log.error("An unexpected error occurred {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError("An unexpected error occurred"));
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
        String licenseKey = licenseKeySet ? LICENSE_KEY : getScreen().getLicenseKey();

        try {
            final boolean loginSuccess = authService.login(username, password, licenseKey);
            SwingUtilities.invokeLater(() -> {
                if (loginSuccess) {
                    if (createUserMode) {
                        screen.showInfo("User created and logged in successfully!");
                    } else {
                        screen.showInfo("Logged in successfully!");
                    }
                    AppConfig.setLicenseKey(licenseKey);
                    NavigationEvents.navigateTo(Screen.HOME);
                } else {
                    screen.showError("Invalid username or password.");
                }
            });

        } catch (JsonProcessingException e) {
            log.debug("Error preparing login request", e);
            log.error("Error preparing login request", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError("Error preparing login request"));
        } catch (LoginException e) {
            log.debug("Login request failed", e);
            log.error("Login request failed",e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError(e.getMessage()));
        } catch (Exception e) {
            log.debug("An unexpected error occurred", e);
            log.error("An unexpected error occurred {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError("An unexpected error occurred"));
        }
    }
}
