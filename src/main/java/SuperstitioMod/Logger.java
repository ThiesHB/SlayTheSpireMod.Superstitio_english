package SuperstitioMod;

import org.apache.logging.log4j.LogManager;

public class Logger {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SuperstitioModSetup.class.getName());

    public static void error(Exception exception) {
        logger.error("[ERROR] " + exception.getMessage());
    }

    public static void info(String string) {
        logger.info(string);
    }
}
