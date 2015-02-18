package timers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import timers.RequestTimer.RequestTask;
import Functions.Calculate;
import Functions.MacTable;
import Functions.VlanTable;
import NetMonitor.main;

public class FunctionTimer
    {
	private Connection connection1 = main.getConnect1();
	private String select;
	private Statement stmt1;
	private Statement stmt2;
	private ResultSet res1;
	private ResultSet res2;
	private ResultSet res3;
	private int time;
	private Timer timer = new Timer(); 
	private ArrayList<Integer> timers1 = new ArrayList<Integer>();
	private String deviceID;
	
	
	class FunctionTask extends TimerTask
	{
	    private int time;
	    
	    FunctionTask(int i){
		time = i;
	    }

	    @Override
	    public void run()
		{
		    Calculate calc = new Calculate();
			try
			    {
				//Select device ID for requesting
				stmt1 = connection1.createStatement();
				stmt2 = connection1.createStatement();
				
				
				select = "SELECT templateID FROM templates WHERE timeout = '"+time+"' && functions!=''";
				res1 = stmt1.executeQuery(select);
				while(res1.next())
				    {
					select = "SELECT deviceID FROM selectdata WHERE templateID = '"+res1.getString(1)+"'";
					res1 = stmt1.executeQuery(select);
				   					
					//Execute requesting function while devices not over
					while(res1.next())
					    {
						deviceID = res1.getString(1);
						System.out.println("DeviceID "+deviceID);
						select = "SELECT functions FROM templates WHERE functions!='' && timeout = '"+time+"'";
						res3 = stmt2.executeQuery(select);
						
						while(res3.next())
						    {
							//Executing function for devices
							switch(res3.getString(1))
							{
							    case "InSpeedCalc":
								calc.InSpeedCalc(deviceID, time, "InSpeedCalc");
								break;
								
							    case "MacTable":
								MacTable mac = new MacTable();
								mac.MacTable(deviceID);
								break;
								
							    case "VlanTable":
								VlanTable vlan = new VlanTable();
								vlan.VlanTable(deviceID);
								break;
								
							}
						    }
						
					    }
				    }
			    } catch (SQLException | IOException | InterruptedException e)
			    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
		    
		}
	    
	}
	
	public void FuctionTimer() throws SQLException, IOException 
	     {



		connection1 = main.getConnect1();
		stmt1 = connection1.createStatement();
		select = "SELECT timeout FROM templates WHERE timeout!=0 && functions!=''  GROUP BY timeout";
		res1 = stmt1.executeQuery(select);
		
		 while(res1.next())
		     {
			time =  res1.getInt(1);
			if(!timers1.contains(time))
			    {
				timers1.add( time);
				System.out.println(time);
				FunctionTask task = new FunctionTask(time);
				timer.scheduleAtFixedRate(task, time*1000,time*1000);
			    }
			    
		     }
	 
	     }
    }
