import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import exceptions.CustomError;
import refactor.JobLoggerRefactor;

public class JobLoggerRefactorTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testErrorInConfiguration() throws CustomError, SecurityException, IOException, SQLException {
		exception.expect(CustomError.class);
		exception.expectMessage(is("Invalid configuration"));
		
		JobLoggerRefactor logger = new JobLoggerRefactor(false,false,false, true, true,false);
		logger.logMessage("test", 1);
	}
	
	@Test
	public void testEmptyMessage() throws CustomError, SecurityException, IOException, SQLException {
		exception.expect(CustomError.class);
		exception.expectMessage(is("Message for Logger is Empty or Null"));
		
		JobLoggerRefactor logger = new JobLoggerRefactor(true,false,false, true, false,false);
		logger.logMessage("", 1);
	}
	
	@Test
	public void testNullMessage() throws CustomError, SecurityException, IOException, SQLException {
		exception.expect(CustomError.class);
		exception.expectMessage(is("Message for Logger is Empty or Null"));
		
		JobLoggerRefactor logger = new JobLoggerRefactor(false,true,false, true, false,false);
		logger.logMessage(null, 1);
	}
	
	@Test
	public void testNoMessageType() throws CustomError, SecurityException, IOException, SQLException {
		exception.expect(CustomError.class);
		exception.expectMessage(is("Message Type is invalid or empty"));
		
		JobLoggerRefactor logger = new JobLoggerRefactor(false,true,false, true, false,false);
		logger.logMessage("test", 0);
	}
	
	@Test
	public void testInvalidMessageType() throws CustomError, SecurityException, IOException, SQLException {
		exception.expect(CustomError.class);
		exception.expectMessage(is("Message Type is invalid or empty"));
		
		JobLoggerRefactor logger = new JobLoggerRefactor(false,true,false, true, false,false);
		logger.logMessage("test", 10);
	}
	
	@Test
	public void testLogMessageInFile() throws CustomError, SecurityException, IOException, SQLException {
		JobLoggerRefactor logger = new JobLoggerRefactor(true,false,false, false, false,true);
		logger.logMessage("logInfo in file", 1);
		logger.logMessage("logWarning in file", 2);
		logger.logMessage("logError not in file", 3);
	}
	
	@Test
	public void testLogMessageInConsole() throws CustomError, SecurityException, IOException, SQLException {
		JobLoggerRefactor logger = new JobLoggerRefactor(false,true,false, false, false,true);
		logger.logMessage("logInfo in console", 1);
		logger.logMessage("logWarning in console", 2);
		logger.logMessage("logError not in console", 3);
	}
	
	@Test
	public void testLogMessageInDatabase() throws CustomError, SecurityException, IOException, SQLException {
		JobLoggerRefactor logger = new JobLoggerRefactor(false,false,true, false, true,false);
		logger.logMessage("logInfo in database", 1);
		logger.logMessage("logWarning in database", 2);
		logger.logMessage("logError not in database", 3);
	}
	
	@Test
    public void testLogFileIsCreated(){
		String path = "src/output/logFile.txt";
		File file = new File(path);
		assertThat(file, notNullValue());
    }
}