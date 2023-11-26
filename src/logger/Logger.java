package logger;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class Logger {
	public static void logError(String stringToWrite) {
		try {
			Path pathOfLog = Paths.get("error_log.txt");
	        Charset charSetOfLog = Charset.forName("US-ASCII");
	        BufferedWriter bwOfLog = Files.newBufferedWriter(pathOfLog, charSetOfLog,StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	        String errStr = LocalDateTime.now().toString() + ":\n" + stringToWrite;
	        bwOfLog.append(errStr, 0, errStr.length());
	        bwOfLog.newLine();
	        bwOfLog.close();
		} catch(Exception e) {
			System.out.println("Error logging to error log!");
			System.out.println(e);
		}
	}
}
