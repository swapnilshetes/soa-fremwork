package utilities;

final public class Logger {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("Log");

	enum Level {
		Error, Warn, Fatal, Info, Debug
	}

	private Logger() {
		/* do nothing */};

	public static void logError(Class clazz, String msg) {
		log(Level.Error, clazz, msg, null);
	}

	public static void logWarn(Class clazz, String msg) {
		log(Level.Warn, clazz, msg, null);
	}

	public static void logFatal(Class clazz, String msg) {
		log(Level.Fatal, clazz, msg, null);
	}

	public static void logInfo(Class clazz, String msg) {
		log(Level.Info, clazz, msg, null);
	}

	public static void logDebug(Class clazz, String msg) {
		log(Level.Debug, clazz, msg, null);
	}

	public static void logError(Class clazz, String msg, Throwable throwable) {
		log(Level.Error, clazz, msg, throwable);
	}

	public static void logWarn(Class clazz, String msg, Throwable throwable) {
		log(Level.Warn, clazz, msg, throwable);
	}

	public static void logFatal(Class clazz, String msg, Throwable throwable) {
		log(Level.Fatal, clazz, msg, throwable);
	}

	public static void logInfo(Class clazz, String msg, Throwable throwable) {
		log(Level.Info, clazz, msg, throwable);
	}

	public static void logDebug(Class clazz, String msg, Throwable throwable) {
		log(Level.Debug, clazz, msg, throwable);
	}

	private static void log(Level level, Class clazz, String msg, Throwable throwable) {
		String message = String.format("[%s] : %s", clazz, msg);
		switch (level) {
		case Info:
			logger.info(message, throwable);
			break;
		case Warn:
			logger.warn(message, throwable);
			break;
		case Error:
			logger.error(message, throwable);
			break;
		case Fatal:
			logger.fatal(message, throwable);
			break;
		default:
		case Debug:
			logger.debug(message, throwable);
		}
	}

}
