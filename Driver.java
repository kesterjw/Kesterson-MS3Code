import java.io.IOException;
import java.util.ArrayList;

/*
 * Author: Jeremy Kesterson
 * Date: 3/19/2018
 * Assignment: MS3 Coding Project
 */
public class Driver {

	public static void main(String[] args) {
		//ArrayList used to receive read-in records from the CSV file.
		ArrayList<String> arr = new ArrayList<String>();
		//Temp used to temporarily store the records split by comma.
		String[] temp;
		//Initializes and creates the database.
		SQLiteDB db = new SQLiteDB();
		//Creates mechanism for reading and writing.
		FileIO io = null;
		try {
			io = new FileIO();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Adds new table to the database.
		db.createNewTable();
		try {
			//Reads in all records from the CSV file.
			arr = io.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Final structure to hold correctly formatted, fully filled in records.
		String[][] records = new String[arr.size()][10];
		//Splits fields by commas, and formats the records to 10 fields.
		for(int i = 0; i < arr.size()-1; i++) {
			temp = arr.get(i).split(",");
			records[i] = formatRecord(temp);
			//Removes null from double quoted fields.
			if(records[i][4].contains("null")) {
				records[i][4] = records[i][4].substring(4);
			}
		}
		
		//Keeps records for log file, and determines whether a record should be written
		//to the bad-data file or not.
		boolean incomplete = false;
		int successful = 0;
		int failed = 0;
		int received = records.length-1;
		//Designates record to bad-data file if any fields are empty or null.
		for(int i = 1; i < records.length; i++) {
			incomplete = false;
			for(int j = 0; j < records[0].length; j++) {
				if(records[i][j] == null || records[i][j].length() < 1) {
					incomplete = true;
				}
			}
			//Writes all malformed records to the bad-data file.
			if(incomplete) {
				io.writeMalformedRecord(records[i]);
				failed++;
			//Inserts all other records into the database table.
			} else {
				db.insertRecord(records[i]);
				successful++;
			}
		}
		//Writes the log file.
		io.writeLog(received, successful, failed);
		
		try {
			io.closeReader();
		} catch (IOException e) {
			e.printStackTrace();
		}
		io.closeWriter();
	}
	
	//Formats records so that all commas within double quotes are retained.
	//Takes in one array of a record's fields that must be formatted to 10 fields and returned.
	public static String[] formatRecord(String[] input) {
		String[] result = new String[10];
		//If a record has less than 10 fields, then add nulls in to the remaining fields and continue.
		if(input.length < 10) {
			for(int i = 0; i < input.length; i++) {
				result[i] = input[i];
			}
			input = result;
			result = new String[10];
		}
		//If the input record has 10 fields exactly, then continue on.
		if(input.length == 10) {
			result = input;
			//If a field has more than 10 fields.
		} else {
			//Start and end index refer to the indexes of the double quotes.
			int startIndex = -1;
			int endIndex = -1;
			//Count keeps track of where in the input/result array we are.
			int count = 0;
			//Keep looping until both double-quotes are found.
			while(startIndex < 0 || endIndex < 0) {
				if(input[count].contains("\"")) {
					//Index the first found to startIndex, and the second to endIndex.
					if(startIndex < 0) {
						startIndex = count;
					} else {
						endIndex = count;
					}
				}
				count++;
			}
			//Now count refers to where we are in the results array, and iCount refers to where we are 
			//in the input array since the input array has more fields than the result array.
			count = 0;
			int iCount = 0;
			//Copy all fields up to the first double quote field.
			while(count < startIndex) {
				result[count] = input[count];
				count++;
				iCount++;
			}
			//Concatenates all the fields between the double quotes inclusively, and adds commas between them,
			//since that is where the split method separated one large single field.
			while(iCount < endIndex) {
				result[count] += input[iCount] + ",";
				iCount++;
			}
			result[count] += input[iCount];
			iCount++;
			count++;
			//Add in the rest of the input fields.
			while(count < 10) {
				result[count] = input[iCount];
				count++;
				iCount++;
			}
		}
		
		return result;
	}

}
