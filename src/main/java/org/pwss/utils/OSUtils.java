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
}
