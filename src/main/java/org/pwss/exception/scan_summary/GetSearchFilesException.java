package org.pwss.exception.scan_summary;

public final class GetSearchFilesException extends Exception {

    private static final long serialVersionUID = 1L;

    private String fileSearchText;

    /**
     * Constructs a `GetSearchFilesException` with no detail message or cause.
     */
    public GetSearchFilesException() {
        super(formatMessage(null, null));
    }

    /**
     * Constructs a `GetSearchFilesException` with the specified detail message.
     * The message is appended with " \nPWSS-FE @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetSearchFilesException(String message) {
        super(formatMessage(message, null));
    }

    /**
     * Constructs a `GetSearchFilesException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GetSearchFilesException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `GetSearchFilesException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public GetSearchFilesException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a `GetSearchFilesException` with the specified detail message
     * and file search text string. The message is appended with " \nPWSS-FE @Exception".
     *
     * @param message        The detail message to be included in the exception.
     * @param fileSearchText The file search text to be included in the exception.
     */
    public GetSearchFilesException(String message, String fileSearchText) {
        super(formatMessage(message, fileSearchText));
        this.fileSearchText = fileSearchText;
    }

    private static String formatMessage(String message, String fileSearchText) {
        StringBuilder sb = new StringBuilder();

        if (message != null) {
            sb.append(message);
        }
        if (fileSearchText != null) {
            sb.append("\nFile Search Text: ").append(fileSearchText);
        }
        sb.append("\nPWSS-FE @Exception");

        return sb.toString();
    }

    /**
     * Returns the file search text associated with this exception, if any.
     *
     * @return The file search text string, or null if not set.
     */
    public String getFileSearchText() {
        return fileSearchText;
    }

}
