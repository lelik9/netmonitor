package timers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import Functions.Universal;
import NetMonitor.main;

public class RequestTimer
    {
	private Connection connection1 = main.getConnect1();
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private int time;
	private Timer timer = new Timer(); 
	ArrayList<Integer> timers1 = new ArrayList<Integer>();
    
	class RequestTask extends TimerTask
	{
	    private int time;
	    
	    RequestTask(int i){
		time = i;
	    }
	    
	    @Override
	    public void run()
		      {

				Universal uni = new Universal();
				
				try
				    {
					//Select device ID for requesting
					stmt1 = connection1.createStatement();			
					select = "SELECT deviceID FROM devices";
					res = stmt1.executeQuery(select);

					System.out.println("request device");
					//Execute requesting function while devices not over
					while(res.next())
					    {
						uni.Universal(res.getString(1), time);;
					    }
					
				    } catch (SQLException | IOException e)
				    {
					// TODO Auto-generated catch block
					e.printStackTrace();
				    }
			 
		      }
	}
 

     public void RequestTimer() throws SQLException, IOException 
     {



	connection1 = main.getConnect1();
	stmt1 = connection1.createStatement();
	select = "SELECT timeout FROM templates WHERE timeout!=0 && OIDname!=''  GROUP BY timeout";
	res = stmt1.executeQuery(select);
	
	 while(res.next())
	     {
		time =  res.getInt(1);
		if(!timers1.contains(time))
		    {
			timers1.add( time);
			System.out.println(time);
			RequestTask task = new RequestTask(time);
			timer.scheduleAtFixedRate(task, time*1000,time*1000);
		    }
		    
	     }
 
     }
   }
    
