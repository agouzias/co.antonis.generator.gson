package co.antonis.gwt.example.client.utilities;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    static Logger logger = Logger.getLogger("");

    static {
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (Handler h : handlers)
            h.setFormatter(new LogFormatter());
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String ln, String message, boolean isDebug) {
        if(isDebug)
            logger.info(ln + message);
    }

    public static void info(String ln, String message) {
        logger.info(ln + message);
    }

    public static void info(String ln, String message, Throwable thr) {
        logger.log(Level.INFO, ln + message, thr);
    }

    public static void debug(String message) {
        debug("",message);
    }

    public static void debug(String ln, String message) { logger.log(Level.INFO, ln + message);
    }

    public static void warn(String ln, String message, Throwable thr) {
        logger.log(Level.WARNING, ln + message, thr);
    }

    public static void error(String ln, String message, Throwable thr) {
        logger.log(Level.SEVERE, ln + message, thr);
    }

    public static long infoDuration(String ln, String message, long time) {
        logger.log(Level.INFO, ln + message + " " + (System.currentTimeMillis() - time) + " ms");
        return System.currentTimeMillis();
    }

    public static Logger getLogger() {
        return logger;
    }

}
