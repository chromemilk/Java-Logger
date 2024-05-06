import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Logger {
    public enum Level {
        DEBUG, INFO, WARN, ERROR, FATAL
    }

    public enum Event {
        BUTTON_PRESS, MOTOR_SET_SPEED
    }


    private boolean should_group; // New field

    public Logger(boolean should_group) { // New constructor
        this.should_group = should_group;
    }

    // Create a static array to store the log messages
    private static final ArrayList<String> log_messages = new ArrayList<>();

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

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

        // Add the log message to the array
        //log_messages.add(formattedMessage);
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

    public void event_listener(Event event, Object... args) {
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
    
    
}

// Example usage
/*
 * public class Main {
 * public static void main(String[] args) {
 * Logger logger = new Logger();
 * 
 * // Example of logging static messages
 * logger.info("Example");
 * 
 * // Example of logging dynamic data
 * double armAngle = 54.2;
 * logger.info("The arm angle is [%.1f] degrees", armAngle);
 * }
 * }
 * 
 */