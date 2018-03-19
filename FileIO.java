import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Author: Jeremy Kesterson
 * Date: 3/19/2018
 * Assignment: MS3 Coding Project
 */
public class FileIO {
	
	//ErrorLine used to remove any accidental all-null records
	private String errorLine = "null,null,null,null,null,null,null,null,null,null";
	private BufferedReader reader;
	private PrintWriter badWriter;
	private PrintWriter logWriter;
	
	//FileIO Constructor 
	public FileIO() throws IOException {
		//TimeStamp used for bad data file name.
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String file = "bad-data-" + timeStamp + ".csv";
		reader = new BufferedReader(new FileReader("ms3Interview.csv"));
		badWriter = new PrintWriter(new FileOutputStream(file));
		logWriter = new PrintWriter(new FileOutputStream("log.txt"));
	}
 	
	//Reds in the csv file and returns an ArrayList of records from the file.
	public ArrayList<String> read() throws IOException {
		ArrayList<String> result = new ArrayList<String>();
		String line = reader.readLine();
		//Continue adding records to the ArrayList until the end of the file is reached.
		while(line != null) {
			result.add(line);
			line = reader.readLine();
		}
		return result;
	}
	
	//Writes a single malformed record to the bad-data file with comma separated fields.
	public void writeMalformedRecord(String[] record) {
		String print = "";
		//Iterates through the record's fields
		for(int i = 0; i < record.length; i++) {
			print += record[i];
			if(i != 9)
				print += ',';
		}
		//Checking for all-null records to be removed
		if(!print.equals(errorLine))
			badWriter.println(print);
	}
	
	//Writes the log file given passed information
	public void writeLog(int received, int successful, int failed) {
		logWriter.println("Records received: " + received);
		logWriter.println("Records successful: " + successful);
		logWriter.println("Records failed: " + failed);
	}
	
	//Closes the BufferedReader.
	public void closeReader() throws IOException {
		reader.close();
	}
	
	//Closes both of the PrintWriters.
	public void closeWriter() {
		badWriter.close();
		logWriter.close();
	}
}
