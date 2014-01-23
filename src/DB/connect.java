package DB;

import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;



public class connect 
    {
	public Connection connectdb(String mydatabase) throws SQLException, IOException
	{
	    Connection connection;
	    
	   // String driverName = "com.mysql.jdbc.Driver"; 

		//Class.forName(driverName);

		// Create a connection to the database
		String serverName = "localhost";
		//String mydatabase = "monitor_db";
		String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
		String username = "root";
		String password = "[Al_F]";

		connection =  DriverManager.getConnection(url, username, password);
		System.out.println("is connect to DB" + connection);
		return connection;
        }
    }