package threads;

public class UtilizationThread extends Thread
    {
	 @Override
	    public void run()
	    {
	        do
	        {
	            

	            try{
	                Thread.sleep(1000);		//Приостановка потока на 1 сек.
	            }catch(InterruptedException e){}
	        }
	        while(true); 
	    }

    }
