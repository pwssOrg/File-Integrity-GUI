package org.pwss.utils;

/**
 * Utility class for operating system detection and information.
 */
public final class OSUtils {

    // Private constructor to prevent instantiation
    private OSUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Enum representing different operating systems.
     */
    public enum OperatingSystem {
        WINDOWS,
        LINUX,
        MACOS,
        UNKNOWN
    }

    /**
     * Gets the name of the operating system.
     *
     * @return The name of the operating system, or "Unknown" if it can't be determined.
     */
    public static final String getOSName() {
        return System.getProperty("os.name", "Unknown");
    }

    /**
     * Determines the current OS type.
     *
     * @return An enum value representing the current OS type.
     */
    public static final OperatingSystem determineOSType() {
        String osName = getOSName().toLowerCase();
        if (osName.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (osName.contains("mac")) {
            return OperatingSystem.MACOS;
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return OperatingSystem.LINUX;
        } else {
            return OperatingSystem.UNKNOWN;
        }
    }

    /**
     * Provides a user-friendly string describing the operating system.
     *
     * @return A descriptive string of the OS or "Unknown" if it can't be determined.
     */
    public static final String describeOS() {
        return switch (determineOSType()) {
            case WINDOWS -> "Windows";
            case MACOS -> "macOS";
            case LINUX -> "Linux";
            default -> "Unknown OS: " + getOSName();
        };
    }

    /**
     * Determines if the current OS is Windows.
     *
     * @return true if the current OS is Windows, false otherwise.
     */
    public static final boolean isWindows() {
        return determineOSType() == OperatingSystem.WINDOWS;
    }

    /**
     * Determines if the current OS is a Unix-based system (Linux or macOS).
     *
     * @return true if the current OS is Unix-based, false otherwise.
     */
    public static final boolean isUnix() {
        return determineOSType() == OperatingSystem.LINUX || determineOSType() == OperatingSystem.MACOS;
    }

    /**
     * Determines if the current OS is Linux.
     *
     * @return true if the current OS is Linux, false otherwise.
     */
    public static final boolean isLinux() {
        return determineOSType() == OperatingSystem.LINUX;
    }

    /**
     * Determines if the current OS is macOS.
     *
     * @return true if the current OS is macOS, false otherwise.
     */
    public static final boolean isMac() {
        return determineOSType() == OperatingSystem.MACOS;
    }

    /**
     * Provides a warning message about quarantining OS files based on the current OS.
     *
     * @return A warning message string specific to the current OS.
     */
    public static String getQuarantineWarningMessage() {
        // TODO: Write our own detailed messages these I generated with our friend.
        return switch (determineOSType()) {
            case WINDOWS ->
                    "Warning: Quarantining or removing Windows system files (drivers, DLLs, registry-related files) can render the system unstable or unbootable. Back up data, ensure you have recovery media and administrator access before proceeding.";
            case MACOS ->
                    "Warning: Quarantining or modifying macOS system files (frameworks, kernel extensions, SIP-protected files) can break system functionality or prevent booting. Create a full backup and understand System Integrity Protection before making changes.";
            case LINUX ->
                    "Warning: Quarantining or removing critical Linux files (kernel, initramfs, package-managed libraries) may render the system unbootable or break package management. Back up data, test changes in a VM, and prefer package manager tools for recovery.";
            default ->
                    "Warning: Quarantining operating-system files can cause instability, data loss, or an unbootable machine. Back up important data and ensure you have a recovery plan before proceeding.";
        };
    }

    /**
     * Formats a quarantine path string into a standard file path format
     *
     * @param path the quarantine path string to format
     * @return the formatted file path
     */
    public static String formatQuarantinePath(String path) {
        if (path == null) {
            return null;
        }
        // Start with the original path
        String outString = path;
        // Replace leading drive marker like "C_drive__" -> "C:\"
        outString = outString.replaceFirst("(?i)^([A-Za-z])_drive__", "$1:\\\\");

        // Preserve double-dot occurrences with a placeholder so single-dot replacement won't clash
        final String DD_PLACEHOLDER = "\u0000DD\u0000";
        final String EXT_PLACEHOLDER = "\u0000ED\u0000";
        outString = outString.replace("..", DD_PLACEHOLDER);

        // Preserve extension dot with a placeholder
        outString = outString.replaceFirst("(?i)\\.([A-Za-z0-9]+)$", EXT_PLACEHOLDER + "$1");

        if (isWindows()) {
            // Replace single dots with a single backslash
            outString = outString.replace(".", "\\");
            // Restore double-dot placeholder
            outString = outString.replace(DD_PLACEHOLDER, "\\.");
            // Restore extension dot
        } else {
            // Unix-like: replace single dots with slashes
            outString = outString.replace(".", "/");
            // Restore double-dot placeholder
            outString = outString.replace(DD_PLACEHOLDER, "/.");
            // Restore extension dot
        }

        // Restore extension dot
        outString = outString.replace(EXT_PLACEHOLDER, ".");

        return outString;
    }
}
