package hnd.src.core;

/**
 * The Logger class provides methods for logging messages at different levels of severity.
 */
public class Logger {

    /**
     * This constructor is marked as private to prevent instantiation of the Logger class.
     */
    private Logger() {
    }

    /**
     * Logs an informational message.
     *
     * @param message the message to log
     */
    public static void info(String message) {
        System.out.println(message);
    }

    /**
     * Logs an error message.
     *
     * @param message the message to log
     */
    public static void error(String message) {
        System.out.println(message);
    }

    /**
     * Logs a warning message.
     *
     * @param message the message to log
     */
    public static void warn(String message) {
        System.out.println(message);
    }

    /**
     * Logs a critical message.
     *
     * @param message the message to log
     */
    public static void critical(String message) {
        System.out.println(message);
    }

    /**
     * Logs a debug message.
     *
     * @param message the message to log
     */
    public static void debug(String message) {
        System.out.println(message);
    }

    /**
     * Logs a trace message.
     *
     * @param message the message to log
     */
    public static void trace(String message) {
        System.out.println(message);
    }
}