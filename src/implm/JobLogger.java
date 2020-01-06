//package implm;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//import java.text.DateFormat;
//import java.util.Date;
//import java.util.Map;
//import java.util.Properties;
//import java.util.logging.ConsoleHandler;
//import java.util.logging.FileHandler;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///*
// * The code is used to log different messages throughout an application. 
// * We want the ability to be able to log to a text file, the console and/or the database. 
// * Messages can be marked as message, warning or error. 
// * We also want the ability to selectively be able to choose what gets logged, such as to be able to log only errors or only errors and warnings.
// * */
//
//public class JobLogger {
//	
//	private static boolean logToFile;
//	private static boolean logToConsole;
//	private static boolean logToDatabase;
//	
//	private static boolean logMessage;
//	private static boolean logWarning;
//	private static boolean logError;
//	
//	//unused variable
//	private boolean initialized;
//	
//	private static Map dbParams;
//	private static Logger logger;
//
//	public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
//			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) {
//		//name of the Logger could be passed as a parameter, to difference between the logger for file and console
//		logger = Logger.getLogger("MyLog");
//		
//		logError = logErrorParam;
//		logMessage = logMessageParam;
//		logWarning = logWarningParam;
//		logToDatabase = logToDatabaseParam;
//		logToFile = logToFileParam;
//		logToConsole = logToConsoleParam;
//		dbParams = dbParamsMap;
//	}
//
//	//too many parameters, has to be defined in the constructor
//	public static void LogMessage(String messageText, boolean message, boolean warning, boolean error) throws Exception {
//		//needed to assign to a variable to be applied 
//		messageText.trim();
//		
//		if (messageText == null || messageText.length() == 0) {
//			// could be validate with proper String's method: messageText.isEmpty();
//			
//			//instead of return you could throw an exception of empty message
//			return;
//		}
//
//		//validation had to be in a different method
//		if (!logToConsole && !logToFile && !logToDatabase) {
//			throw new Exception("Invalid configuration");
//		}
//		
//		if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
//			throw new Exception("Error or Warning or Message must be specified");
//		}
//		//======= 
//		
//		//If you will go to log into a database, the connection has to be managed in a separated method
//		Connection connection = null;
//		Properties connectionProps = new Properties();
//		connectionProps.put("user", dbParams.get("userName"));
//		connectionProps.put("password", dbParams.get("password"));
//		
//		// unnecessary to open the connection if you won't to log into the database.
//		// needed to specify the schema to connect
//		connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
//				+ ":" + dbParams.get("portNumber") + "/", connectionProps);
//
//		int t = 0;
//		if (message && logMessage) {
//			t = 1;
//		}
//
//		if (error && logError) {
//			t = 2;
//		}
//
//		if (warning && logWarning) {
//			t = 3;
//		}
//
//		//PreparedStatement has better performance and is editable at runtime
//		Statement stmt = connection.createStatement();
//
//		String l = null;
//		
//		//file is never closed
//		File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
//		
//		if (!logFile.exists()) {
//			logFile.createNewFile();
//		}
//		
//		FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
//		//could be need to add a formatter to modify the output from XML by default to text
//		ConsoleHandler ch = new ConsoleHandler();
//		
//		//by default the Logger class add all the data about the date, time, name of the class and method
//		if (error && logError) {
//			l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
//		}
//
//		if (warning && logWarning) {
//			l = l + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
//		}
//
//		if (message && logMessage) {
//			l = l + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
//		}
// 
//		if (logToFile) {
//			logger.addHandler(fh);
//			logger.log(Level.INFO, messageText);
//		}
//
//		if (logToConsole) {
//			logger.addHandler(ch);
//			logger.log(Level.INFO, messageText);
//		}
//
//		if (logToDatabase) {
//			stmt.executeUpdate("insert into Log_Values('" + message + "', " + String.valueOf(t) + ")");
//		}
//		//the connection to the database is never closed
//	}
//}