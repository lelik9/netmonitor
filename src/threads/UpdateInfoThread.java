package threads;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Functions.ArpTable;
import Functions.InterfaceInfo;
import Functions.MacTable;
import Functions.VlanTable;

public class UpdateInfoThread extends Thread
    {
	private String Name;
	private Connection connection1;
	private Connection connection2;
	private ResultSet res;
	//UpdateInfo UI;
	 @Override
	    public void run()
	    {
		UpdateInfo UI = new UpdateInfo();
		try
		    {
			synchronized (UI)
			    {
				UI.Check();
			    }
			
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

	 class UpdateInfo
{
	 private void Check() throws SQLException, IOException
	     {
		        InterfaceInfo i = new InterfaceInfo();
		        MacTable mt = new MacTable();
		        VlanTable vt = new VlanTable();
		        ArpTable at = new ArpTable();
		        
		        Statement stmt = connection1.createStatement();
		        
		        String Select = "SELECT DeviceName FROM devices WHERE Groups = 'network'";

		        
		        	do
		        	    {
		        		res = stmt.executeQuery(Select);

				        while(res.next())
				            {
				        	System.out.println("Update info start");
				        	Name = res.getString(1);
					        System.out.println(Name);
		            
					 //       i.GetIntInfo(Name);
					        vt.VlanTable(Name, connection1, connection2);
					 //       at.GetArp(Name, connection1, connection2);
					        mt.MacTable(Name, connection1, connection2);
					        System.out.println("Update info end");
				            }
		        		try{
		        		    Thread.sleep(300000);		//Приостановка потока на 1 сек.
		        		}catch(InterruptedException e){}
		        	    }
		        	while(true);
		            
	     }
	 
	 }
	 public synchronized void Connect(Connection connect1, Connection connect2)
	     {
		 connection1 = connect1;
		 connection2 = connect2;
	     }
    }
