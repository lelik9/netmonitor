package DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbConnectionPool
    {
	private static final int MAX_NUMBER_OF_CONNECTIONS = 5;
	
	private int currentConnection = 0;
	
	private static volatile DbConnectionPool instance;
	private List<Connection> connections; 
		
	private DbConnectionPool (){
	    connections = new ArrayList<Connection>(MAX_NUMBER_OF_CONNECTIONS);
	    createConnections();
	}
	
	public static DbConnectionPool getInstance(){
            if(instance == null){
        	synchronized(DbConnectionPool.class){
        	    if(instance == null){
        		instance = new DbConnectionPool();
        	    }
        	}
            }
            return instance;
	}
	
	private void createConnections(){
	    int i = 0;
	    while (i < MAX_NUMBER_OF_CONNECTIONS){
		try
		    {
			connections.add(Connect.connectdb());
		    } catch (SQLException e)
		    {
			System.out.println("Connection to database failed!");
			e.printStackTrace();
		    }
		i++;
		}
	}
	
	public void closeConnections(){
	    int i = 0;
	    while (i < MAX_NUMBER_OF_CONNECTIONS){
			try
			    {
				Connect.closeConnection(connections.get(i));
			    } catch (SQLException e)
			    {
				System.out.println("Connection doesn't close!");
				e.printStackTrace();
			    }
			i++;
		}
	}
	
	public Connection getConnection(){
	    if (currentConnection < MAX_NUMBER_OF_CONNECTIONS){
		return connections.get(currentConnection++);
	    }
	    return null;
	}
    }
