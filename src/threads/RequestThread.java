package threads;

import java.io.IOException;
import java.sql.SQLException;

import Functions.Universal;


public class RequestThread extends Thread
    {
	private int time;
	private String deviceID;
	
	public RequestThread(String DeviceID, int Time)
	    {
		// TODO Auto-generated constructor stub
		    time = Time;
		    deviceID = DeviceID;
	    }

	
	 @Override
	    public void run()
	    {
		Universal uni = new Universal();
		

			synchronized (uni)
			{
			    try
				{
				    uni.Universal(deviceID, time);
				} catch (SQLException | IOException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
			}

	    }
    }
