package DB;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;



public class Connect 
    {
	private static final String NAME_OF_DATABASE = "monitor_db";
	
	public static Connection connectdb() throws SQLException
	{
	   // String driverName = "com.mysql.jdbc.Driver"; 

		//Class.forName(driverName);
		Connection connection;
		// Create a connection to the database
		String serverName = "localhost";
		//String mydatabase = "monitor_db";
		String url = "jdbc:mysql://" + serverName + "/" + NAME_OF_DATABASE;
		String username = "root";
		String password = "[Al_F]";

		connection =  DriverManager.getConnection(url, username, password);

		System.out.println("is Connect to DB" + connection);
		return connection;
        }
	
	public static void closeConnection(Connection conn) throws SQLException{
	    conn.close();
	}
    }