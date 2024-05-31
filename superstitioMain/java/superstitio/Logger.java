package superstitio;

import com.evacipated.cardcrawl.modthespire.Loader;
import org.apache.logging.log4j.LogManager;

public class Logger {
    private static final org.apache.logging.log4j.Logger logger =
            LogManager.getLogger(SuperstitioModSetup.class.getName());

    public static void error(Exception exception) {
        logger.error("[ERROR] " + exception.getMessage());
    }

    public static void info(String string) {
        logger.info(string);
    }

    public static void run(String string) {
        if (Loader.DEBUG)
            logger.info(string);
    }

    public static void temp(String string) {
        logger.info(string);
    }

    public static void debug(String string) {
        if (Loader.DEBUG)
            logger.info(string);
    }

    public static void warning(String string) {
//        if (Loader.DEBUG)
        logger.warn("[WARNING] " + string);
    }
}
