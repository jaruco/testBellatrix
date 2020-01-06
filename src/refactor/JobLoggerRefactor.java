package refactor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import exceptions.CustomError;

public class JobLoggerRefactor {

	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logToDatabase;

	private static boolean logMessage;
	private static boolean logWarning;
	private static boolean logError;

	private static Logger logger = Logger.getLogger(JobLoggerRefactor.class.getName());
	
	public JobLoggerRefactor(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam) {

		setLogError(logErrorParam);
		setLogMessage(logMessageParam);
		setLogWarning(logWarningParam);

		setLogToFile(logToFileParam);
		setLogToDatabase(logToDatabaseParam);
		setLogToConsole(logToConsoleParam);
	}

	public void logMessage(String messageText, int typeMessage) throws CustomError, SecurityException, IOException, SQLException {
		if (!validateConfig()) {
			throw new CustomError(100, "Invalid configuration");
		}
		
		if (!validateNullOrEmptyMessage(messageText)) {
			throw new CustomError(101, "Message for Logger is Empty or Null");
		}

		if (!validateTypeMessage(typeMessage)) {
			throw new CustomError(102, "Message Type is invalid or empty");
		}

		if (isLogToFile()) {
			logToFile(messageText, typeMessage);
		}

		if (isLogToConsole()) {
			logToConsole(messageText, typeMessage);
		}

		if (isLogToDatabase()) {
			logToDatabase(messageText, typeMessage);
		}
	}

	public static boolean validateNullOrEmptyMessage(String message) {
		if (message == null || message.isEmpty()) {
			return false;
		}
		return true;
	}

	public static boolean validateConfig() {

		if (isLogToFile()) {
			return true;
		}

		if (isLogToConsole()) {
			return true;
		}

		if (isLogToDatabase()) {
			return true;
		}

		return false;
	}

	public static boolean validateTypeMessage(int typeMessage) {
		Integer[] possible = {1,2,3};
		if (typeMessage == 0 || !Arrays.asList(possible).contains(typeMessage)) {
			return false;
		}
		return true;
	}

	public static void logToFile(String message, int typeMessage) throws SecurityException, IOException {
		getLogger().setUseParentHandlers(false);

		FileHandler fileHandler = new FileHandler(System.getProperty("user.dir") + "/output/logFile.txt", true);
		getLogger().addHandler(fileHandler);

		SimpleFormatter formatterTxt = new SimpleFormatter();
		fileHandler.setFormatter(formatterTxt);

		if (isLogWarning() || isLogMessage()) {
			getLogger().setLevel(Level.INFO);
		}

		if (isLogError()) {
			getLogger().setLevel(Level.SEVERE);
		}

		switch (typeMessage) {
		// message
		case 1:
			getLogger().info(message);
			break;
		// warning
		case 2:
			getLogger().warning(message);
			break;
		// error
		case 3:
			getLogger().severe(message);
			break;
		}

		fileHandler.close();
	}

	public static void logToDatabase(String message, int typeMessage) throws IOException, SQLException {
		try {
			Properties prop = loader();
			String dURL = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", prop.getProperty("db.server"),
					Integer.parseInt(prop.getProperty("db.port")), prop.getProperty("db.schema"));

			Connection connection = null;
			connection = DriverManager.getConnection(dURL, prop.getProperty("db.user"),
					prop.getProperty("db.password"));

			String query = "INSERT INTO logValues " + "(message, logType) " + "VALUES (?,?) ";

			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, message);
			stmt.setInt(2, typeMessage);

			int result = stmt.executeUpdate();
			if (result < 0) {
				stmt.close();
				throw new IOException("Error in Statement");
			}

			stmt.close();
		} catch (IOException e) {
			throw new IOException("Error connecting to database");
		}

	}

	public static Properties loader() throws IOException {
		try (InputStream input = new FileInputStream(System.getProperty("user.dir") + "/resources/config.properties")) {

			Properties prop = new Properties();
			prop.load(input);
			return prop;

		} catch (IOException ex) {
			throw new IOException("Error loading properties file");
		}
	}

	public static void logToConsole(String message, int typeMessage) {

		if (isLogWarning() || isLogMessage()) {
			getLogger().setLevel(Level.INFO);
		}

		if (isLogError()) {
			getLogger().setLevel(Level.SEVERE);
		}

		switch (typeMessage) {
		// message
		case 1:
			getLogger().info(message);
			break;
		// warning
		case 2:
			getLogger().warning(message);
			break;
		// error
		case 3:
			getLogger().severe(message);
			break;
		}

	}

	public static boolean isLogToFile() {
		return logToFile;
	}

	public static void setLogToFile(boolean logToFile) {
		JobLoggerRefactor.logToFile = logToFile;
	}

	public static boolean isLogToConsole() {
		return logToConsole;
	}

	public static void setLogToConsole(boolean logToConsole) {
		JobLoggerRefactor.logToConsole = logToConsole;
	}

	public static boolean isLogToDatabase() {
		return logToDatabase;
	}

	public static void setLogToDatabase(boolean logToDatabase) {
		JobLoggerRefactor.logToDatabase = logToDatabase;
	}

	public static boolean isLogMessage() {
		return logMessage;
	}

	public static void setLogMessage(boolean logMessage) {
		JobLoggerRefactor.logMessage = logMessage;
	}

	public static boolean isLogWarning() {
		return logWarning;
	}

	public static void setLogWarning(boolean logWarning) {
		JobLoggerRefactor.logWarning = logWarning;
	}

	public static boolean isLogError() {
		return logError;
	}

	public static void setLogError(boolean logError) {
		JobLoggerRefactor.logError = logError;
	}

	public static Logger getLogger() {
		return logger;
	}
}