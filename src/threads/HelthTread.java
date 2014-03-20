package threads;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import Functions.DeviceHealth;

public class HelthTread extends Thread
    {
	private Connection connection1;
	private Connection connection2;
	
	 @Override
	    public void run()
	    {
		DeviceHealth health = new DeviceHealth();
		
	        do
	        {
	            health.Connect(connection1, connection2);
	            try
			{
			    synchronized (health)
				{
				    health.DeviceHealth();   
				}
			    System.out.println("sleep");
			} catch (SQLException e1)
			{
			    // TODO Auto-generated catch block
			    e1.printStackTrace();
			} catch (IOException e1)
			{
			    // TODO Auto-generated catch block
			    e1.printStackTrace();
			}

	            try{
	                Thread.sleep(60000);		//Приостановка потока на 1 сек.
	            }catch(InterruptedException e){}
	        }
	        while(true); 
	    }
	 
	 public synchronized void Connect(Connection connect1, Connection connect2)
	     {
		 connection1 = connect1;
		 connection2 = connect2;
	     }
    }
