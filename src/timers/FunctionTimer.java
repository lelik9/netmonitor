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
import NetMonitor.main;

public class FunctionTimer
    {
	private Connection connection1 = main.getConnect1();
	private String select;
	private Statement stmt1;
	private ResultSet res;
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
				select = "SELECT templateID FROM templates WHERE timeout = '"+time+"'";
				res = stmt1.executeQuery(select);
				while(res.next())
				    {
					select = "SELECT deviceID FROM selectdata WHERE templateID = '"+res.getString(1)+"'";
					res = stmt1.executeQuery(select);
				   					
					//Execute requesting function while devices not over
					while(res.next())
					    {
						deviceID = res.getString(1);
						select = "SELECT functions FROM templates WHERE functions!=''";
						res = stmt1.executeQuery(select);
						
						while(res.next())
						    {
							//Executing function for devices
							switch(res.getString(1))
							{
							    case "InSpeedCalc":
								calc.InSpeedCalc(deviceID, time, "InSpeedCalc");
								
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
		res = stmt1.executeQuery(select);
		
		 while(res.next())
		     {
			time =  res.getInt(1);
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
