package threads;


import java.io.IOException;
import java.sql.SQLException;
import timers.RequestTimer;



public class RequestDeviceThread extends Thread
    {
		
	//Request device thread;
	 @Override
	    public void run()
	    {
		RequestTimer req = new RequestTimer();
		
		do
		    {
			try
			    {
				synchronized (req)
				{
				    System.out.println("Thread wake");
				    req.RequestTimer();
				    
				}  

					System.out.println("thread sleep");
					Thread.sleep(36000);		//Приостановка потока на 60 сек.
			    }catch(InterruptedException | SQLException | IOException e){}
		    }
		while(true);
	    }
			
			    
			
		    
	    

}