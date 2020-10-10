package clueGame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class BadConfigFormatException extends Exception {

	public BadConfigFormatException() {
		super("Error: Something is wrong with your configuration files. Unable to initalize Clue board.");
		writeLog();
	}
	
	public BadConfigFormatException(String message) {
		super(message);
		writeLog();
	}
	
	private void writeLog() {
		try {
			// open a file to append error message to log file
			File file = new File("./data/logfile.txt");
			FileWriter fw = new FileWriter(file,true);
			BufferedWriter writer = new BufferedWriter(fw);
			
			// get current date and time for error message
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
			writer.write("On " + dtf.format(now) + " compiler returned error message:: ");
			writer.write(super.getMessage() + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
