import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class Logger {
    public enum Level {
        DEBUG, INFO, WARN, ERROR, FATAL
    }

    public enum Event {
        BUTTON_PRESS, MOTOR_SET_SPEED
    }


    private boolean should_group; 
    private boolean exit_log;

    public Logger(boolean should_group, boolean exit_log) { 
        this.should_group = should_group;
        this.exit_log = exit_log;
    }

    // Create a static array to store the log messages

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private static final Map<LocalDateTime, String> log_messages = new TreeMap<>();

    
    private static final String RESET = "\033[0m"; // Reset color to default
    private static final String YELLOW = "\033[0;33m"; // Yellow for "LOGGER:"
    private static final String GREEN = "\033[0;32m"; // Green for timestamp
    private static final String BLUE = "\033[0;34m"; // Blue for DEBUG messages
    private static final String CYAN = "\033[0;36m"; // Cyan for INFO messages
    private static final String PURPLE = "\033[0;35m"; // Purple for WARN messages
    private static final String RED = "\033[0;31m"; // Red for ERROR and FATAL messages

    public void dispatch_log(Level level, String message, Object... args) {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = dtf.format(now);
        String formattedMessage = String.format(message, args);

        // Apply color based on log level
        String color;
        switch (level) {
            case DEBUG:
                color = BLUE;
                break;
            case INFO:
                color = CYAN;
                break;
            case WARN:
                color = PURPLE;
                break;
            case ERROR:
            case FATAL:
                color = RED;
                break;
            default:
                color = RESET;
        }

        // Constructing the formatted log output
        System.out.println(YELLOW + "LOGGER:" + RESET);
        System.out.println(GREEN + formattedTime + RESET);
        System.out.println(color + formattedMessage + RESET);
        System.out.println("Log end\n-----------------");

        // Add the log message to the array with their time and severity 
        log_messages.put(now, " [" + level + "] " + formattedMessage);
    }

    public void debug(String message, Object... args) {
        dispatch_log(Level.DEBUG, message, args);
    }

    public void info(String message, Object... args) {
        dispatch_log(Level.INFO, message, args);
    }

    public void warn(String message, Object... args) {
        dispatch_log(Level.WARN, message, args);
    }

    public void error(String message, Object... args) {
        dispatch_log(Level.ERROR, message, args);
    }

    public void fatal(String message, Object... args) {
        dispatch_log(Level.FATAL, message, args);
    }

    public void event(Event event, Object... args) {
        switch (event) {
            case BUTTON_PRESS:
                info("Button pressed [%s]", args);
                break;
            case MOTOR_SET_SPEED:
                info("Motor speed set [%.1f]", args);
                break;
            default:
                break;
        }
    }

    public void get_exception(Exception e) {
        error("Exception occurred: %s", e.getMessage());
    }

    public void exit() {
        System.out.println("Terminating logger...");
        // If we should log the saved message array
        if (exit_log) {
            // Group FIRST by type, AND THEN by time
            if (should_group) {
                // Group by type
                for (Level level : Level.values()) {
                    System.out.println(level + " logs:");
                    for (Map.Entry<LocalDateTime, String> entry : log_messages.entrySet()) {
                        if (entry.getValue().contains("[" + level + "]")) {
                            System.out.println(dtf.format(entry.getKey()) + entry.getValue());
                        }
                    }
                }
            } else {
                // Print all logs
                for (Map.Entry<LocalDateTime, String> entry : log_messages.entrySet()) {
                    System.out.println(dtf.format(entry.getKey()) + entry.getValue());
                }
            }
        }
        else {
            System.out.println("Exit logging disabled");
            System.out.println("Unable to assert log messages");
        }
    }
    
    public static void main(String[] args) {
        Logger logger = new Logger(true, false);
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
        logger.fatal("This is a fatal message");
        logger.event(Event.BUTTON_PRESS, "A");
        logger.event(Event.MOTOR_SET_SPEED, 1.0);
        logger.exit();
    }
}

// Example usage
// logger.info("The arm angle is [%.1f] degrees", armAngle);
