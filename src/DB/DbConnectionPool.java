package DB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbConnectionPool
    {
	private static final int MAX_NUMBER_OF_CONNECTIONS = 5;
	
	private int currentConnection = 0;
	
	private static volatile DbConnectionPool instance;
	private List<Connection> connections1;
	private List<Connection> connections2;
		
	private DbConnectionPool () throws IOException{
	    connections1 = new ArrayList<Connection>(MAX_NUMBER_OF_CONNECTIONS);
	    connections2 = new ArrayList<Connection>(MAX_NUMBER_OF_CONNECTIONS);
	    createConnections();
	}
	
	public static DbConnectionPool getInstance() throws IOException{
            if(instance == null){
        	synchronized(DbConnectionPool.class){
        	    if(instance == null){
        		instance = new DbConnectionPool();
        	    }
        	}
            }
            return instance;
	}
	
	private void createConnections() throws IOException{
	    int i = 0;
	    while (i < MAX_NUMBER_OF_CONNECTIONS){
		try
		    {
			connections1.add(Connect.connectdb("monitor_db"));
			connections2.add(Connect.connectdb("mib_db"));
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
				Connect.closeConnection(connections1.get(i));
				Connect.closeConnection(connections2.get(i));
			    } catch (SQLException e)
			    {
				System.out.println("Connection doesn't close!");
				e.printStackTrace();
			    }
			i++;
		}
	}
	
	public Connection getConnection1(){
	    if (currentConnection < MAX_NUMBER_OF_CONNECTIONS){
		return connections1.get(currentConnection++);
	    }
	    return null;
	}
	
	public Connection getConnection2(){
	    if (currentConnection < MAX_NUMBER_OF_CONNECTIONS){
		return connections2.get(currentConnection++);
	    }
	    return null;
	}
    }
