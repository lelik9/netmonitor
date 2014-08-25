package threads;


import java.io.IOException;
import java.sql.SQLException;

import timers.FunctionTimer;
import timers.RequestTimer;



public class RequestDeviceThread extends Thread
    {
		
	//Request device thread;
	 @Override
	    public void run()
	    {
		RequestTimer req = new RequestTimer();
		FunctionTimer func = new FunctionTimer();
		
		do
		    {
			try
			    {
				synchronized (req)
				{
				    System.out.println("Thread wake");
				    req.RequestTimer();
				    func.FuctionTimer();
				}  

					System.out.println("thread sleep");
					Thread.sleep(36000);		//Приостановка потока на 60 сек.
			    }catch(InterruptedException | SQLException | IOException e){}
		    }
		while(true);
	    }
			
			    
			
		    
	    

}