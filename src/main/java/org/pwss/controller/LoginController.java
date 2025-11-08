package org.pwss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.pwss.app_settings.AppConfig;
import org.pwss.exception.user.CreateUserException;
import org.pwss.exception.user.LoginException;
import org.pwss.exception.user.UserExistsLookupException;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.service.AuthService;
import org.pwss.utils.LoginUtils;
import org.pwss.utils.StringConstants;
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
        super.onShow();
        screen.getUsernameField().setText("");
        screen.getPasswordField().setText("");
        log.debug("Current LICENSE_KEY: {}", licenseKeySet ? "SET" : "NOT SET");
        log.debug("Create User Mode: {}", createUserMode);
        // Adjust frame size based on create user mode
        final int frameHeight = createUserMode ? 225 : 200;
        final int offset = licenseKeySet ? 0 : 50;
        screen.getParentFrame().setSize(450, frameHeight + offset);
        refreshView();
    }

    @Override
    protected void initListeners() {
        screen.getPasswordField().addActionListener(e -> proceedAndValidate());
        screen.getProceedButton().addActionListener(e -> proceedAndValidate());
        screen.getCancelButton().addActionListener(e -> System.exit(0));
    }

    @Override
    protected void refreshView() {
        // Only show license key fields if LICENSE_KEY is not set
        if (licenseKeySet) {
            screen.getLicenseLabel().setVisible(false);
            screen.getLicenseKeyField().setVisible(false);
        }
        // Update the view based on whether we are in create user mode
        if (createUserMode) {
            // Notify user that no users exist and they need to create one
            screen.showInfo("No user found.\nCreate one by entering a username and password.");
            // Update message label to indicate user creation
            screen.setMessage("Create a user for the first login.");
            // Change button text to "Register"
            screen.getProceedButton().setText("Register");
        } else {
            // Update message label to indicate normal login
            screen.setMessage("Login with your username and password.");
            // Change button text to "Login"
            screen.getProceedButton().setText("Login");
        }

        // Show or hide confirm password fields based on create user mode
        screen.getConfirmPasswordLabel().setVisible(createUserMode);
        screen.getConfirmPasswordField().setVisible(createUserMode);
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
            int choice = screen.showOptionDialog(JOptionPane.INFORMATION_MESSAGE,
                    """
                            Welcome to Integrity Hash!
                            
                            You are about to create a user for this application.
                            Please make sure to remember your credentials as they will be required for future logins.
                            
                            Do you want to proceed?""",
                    new String[]{StringConstants.GENERIC_YES, StringConstants.GENERIC_NO},
                    StringConstants.GENERIC_YES);

            if (choice == 0) {
                createUserAndLogin();
            } else {
                log.debug("User creation cancelled by user.");
            }
        } else {
            // Handle normal login logic here
            performLogin();
        }
    }

    /**
     * Validates the user input for username, password, and license key.
     *
     * @return true if input is valid, false otherwise.
     */
    private boolean validateInput() {
        String username = screen.getUsername();
        String password = screen.getPassword();
        String confirmPassword = screen.getConfirmPassword();
        String licenseKey = licenseKeySet ? LICENSE_KEY : screen.getLicenseKey();

        LoginUtils.LoginValidationResult result = LoginUtils.validateInput(username, password, confirmPassword, licenseKey, createUserMode);
        if (!result.isValid()) {
            screen.showError(LoginUtils.formatErrors(result.errors()));
        }

        return result.isValid();
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
        String username = screen.getUsername();
        String password = screen.getPassword();
        String licenseKey = licenseKeySet ? LICENSE_KEY : screen.getLicenseKey();

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
        } catch (Exception e) {
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
        String username = screen.getUsername();
        String password = screen.getPassword();
        String licenseKey = licenseKeySet ? LICENSE_KEY : screen.getLicenseKey();

        try {
            final boolean loginSuccess = authService.login(username, password, licenseKey);
            SwingUtilities.invokeLater(() -> {
                if (loginSuccess) {
                    if (createUserMode) {
                        screen.showInfo("User created and logged in successfully!");
                    } else {
                      log.info("Logged in successfully!");
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
            log.error("Login request failed: {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError(e.getMessage()));
        } catch (Exception e) {
            log.debug("An unexpected error occurred", e);
            log.error("An unexpected error occurred {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError("An unexpected error occurred"));
        }
    }
}
