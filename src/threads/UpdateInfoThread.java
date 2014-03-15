package threads;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DB.connect;
import Functions.ArpTable;
import Functions.InterfaceInfo;
import Functions.MacTable;
import Functions.VlanTable;
import NetMonitor.main;

public class UpdateInfoThread extends Thread
    {
	private String Name;
	private Connection connection;
	
	 @Override
	    public void run()
	    {
		try
		    {
			Check();
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (IOException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
	    }
	 private void Check() throws SQLException, IOException
	     {
		        InterfaceInfo i = new InterfaceInfo();
		        MacTable mt = new MacTable();
		        VlanTable vt = new VlanTable();
		        ArpTable at = new ArpTable();
		        
		        Statement stmt = connection.createStatement();
		        
		        String Select = "SELECT DeviceName FROM devices WHERE Groups = 'network'";
		        ResultSet res = stmt.executeQuery(Select);

		        while(res.next())
		            {
		        	Name = res.getString(1);
			        System.out.println(Name);
		        	do
		        	    {
		            
		        		i.GetIntInfo(Name);
		        		vt.VlanTable(Name);
		        		at.GetArp(Name);
		        		mt.MacTable(Name);

		        		try{
		        		    Thread.sleep(30000);		//Приостановка потока на 1 сек.
		        		}catch(InterruptedException e){}
		        	    }
		        	while(true);
		            }
	     }
	 
	 public void Connect(Connection connect)
	     {
		 connection = connect;
	     }
    }
