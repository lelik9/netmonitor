package threads;


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


public class RequestDevice extends Thread
    {
	private Connection connection1 = main.getConnect1();
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private int time;
	private Timer timer = new Timer(); 
	ArrayList<Integer> timers1 = new ArrayList<Integer>();
	
	//TIMER task for request device
	class Task extends TimerTask
	{
	    private int time;
	    
	    Task(int i){
		time = i;
	    }
	    
	    @Override
	    public void run()
		      {

				Universal uni = new Universal();
				
				System.out.println("timer "+time);
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

	
	//Request device thread;
	 @Override
	    public void run()
	    {
		Request req = new Request();

			try
			    {
				synchronized (req)
				{

				    req.Request();
				    
				    do
					{
				            try{
							System.out.println("thred wake");
							connection1 = main.getConnect1();
							stmt1 = connection1.createStatement();
							select = "SELECT timeout FROM templates WHERE timeout!=0 GROUP BY timeout";
							res = stmt1.executeQuery(select);
							
							 while(res.next())
							     {
								time =  Integer.parseInt(res.getString(1));

								 if(!timers1.contains(time))
								     {
									timers1.add(time);
									Task task = new Task(time);
									System.out.println(time);
									timer.scheduleAtFixedRate(task, time*1000,time*1000);
								     }
							     }

							System.out.println("thread sleep");
							Thread.sleep(36000);		//Приостановка потока на 60 сек.
				            }catch(InterruptedException e){}
					}
				    while(true);
				}
			
			    } catch (SQLException | IOException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
			
		    
	    }
	 
	 class Request
	 {
	     private void Request() throws SQLException, IOException 
	     {



		connection1 = main.getConnect1();
		stmt1 = connection1.createStatement();
		select = "SELECT timeout FROM templates WHERE timeout!=0 GROUP BY timeout";
		res = stmt1.executeQuery(select);
		
		 while(res.next())
		     {
			time =  Integer.parseInt(res.getString(1));
			timers1.add( time);
				System.out.println(time);
				Task task = new Task(time);
				timer.scheduleAtFixedRate(task, time*1000,time*1000);
			    
		     }
	 }
	 
    }
}