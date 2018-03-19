import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

/*
 * Author: Jeremy Kesterson
 * Date: 3/19/2018
 * Assignment: MS3 Coding Project
 */
public class SQLiteDB {
	private Connection c = null;
	private Statement stmt = null;
	private PreparedStatement pst = null;
	private final String url = "jdbc:sqlite::memory:";
	
	//SQLiteDB constructor. Establishes connection to the in-memory database.
	SQLiteDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(url);
		} catch(Exception e) {
			System.out.println("Error in SQLiteDB constructor: " + e.getMessage());
		}
	}
	
	//Creates the table for data entry in the database.
	public void createNewTable() {
		//Creates a table with 10 fields named as such in the CSV file.
		String createTable = "CREATE TABLE table1 (\n"
				+ " A text NOT NULL,\n"
				+ " B text NOT NULL,\n"
				+ " C text NOT NULL,\n"
				+ " D text NOT NULL,\n"
				+ " E text NOT NULL,\n"
				+ " F text NOT NULL,\n"
				+ " G text NOT NULL,\n"
				+ " H text NOT NULL,\n"
				+ " I text NOT NULL,\n"
				+ " J text NOT NULL\n"
				+ ")";
		try {
			//Creates and executes the SQL statement based on the string above.
			stmt = c.createStatement();
			stmt.execute(createTable);	
			
		} catch(Exception e) {
			System.out.println("Error in createNewTable: " + e.getMessage());
		}
	}
	
	//Inserts a single record into the database table.
	public void insertRecord(String[] record) {
		//Creates the string that represents the SQL instruction for insertion.
		String insert = "INSERT INTO table1(A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";
		try {
			
			//Sets each field of the record.
			pst = c.prepareStatement(insert);
			pst.setString(1, record[0]);
			pst.setString(2, record[1]);
			pst.setString(3, record[2]);
			pst.setString(4, record[3]);
			pst.setString(5, record[4]);
			pst.setString(6, record[5]);
			pst.setString(7, record[6]);
			pst.setString(8, record[7]);
			pst.setString(9, record[8]);
			pst.setString(10, record[9]);
			//Inserts record.
			pst.executeUpdate();
			
		} catch(Exception e) {
			System.out.println("Error in insertRecord: " + Arrays.toString(record));
		}
		
	}
	
	//For testing purposes
	public void queryRecord() {
		String query = "SELECT A, B, C, D, E, F, G, H, I, J FROM table1";
		try {
			stmt = c.createStatement();
			ResultSet results = stmt.executeQuery(query);
			while(results.next()) {
				System.out.println(results.getString("A") + ", " + results.getString("B") 
				+ ", " + results.getString("C") + ", " + results.getString("D") + ", " + results.getString("E") 
				+ ", " + results.getString("F") + ", " + results.getString("G") + ", " + results.getString("H") 
				+ ", " + results.getString("I") + ", " + results.getString("J"));
			}
		} catch (Exception e) {
			System.out.println("Error in queryRecord.");
		}
	}
}
