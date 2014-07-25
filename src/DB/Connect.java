package DB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class Connect 
    {
	private static String serverName;
	private static String username;
	private static String password;
	//private static final String NAME_OF_DATABASE = "monitor_db";
	
	public static Connection connectdb(String dbName) throws SQLException, FileNotFoundException, IOException
	{
	   // String driverName = "com.mysql.jdbc.Driver"; 
	    //load DB config
	    Properties props = new Properties();
	    props.load(new FileInputStream(new File("config/mysql.ini")));
	    serverName = props.getProperty("SQL_Server");
	    username = props.getProperty("SQL_Login");
	    password = props.getProperty("SQL_Password");
	    
		//Class.forName(driverName);
		Connection connection;
		// Create a connection to the database
		
		//String mydatabase = "monitor_db";
		String url = "jdbc:mysql://" + serverName + "/" + dbName;


		connection =  DriverManager.getConnection(url, username, password);

		System.out.println("is Connect to DB" + connection);
		return connection;
        }
	
	public static void closeConnection(Connection conn) throws SQLException{
	    conn.close();
	}
    }