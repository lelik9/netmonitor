package threads;

public class UtilizationThread extends Thread
    {
	 @Override
	    public void run()
	    {
	        do
	        {
	            

	            try{
	                Thread.sleep(1000);		//������������ ������ �� 1 ���.
	            }catch(InterruptedException e){}
	        }
	        while(true); 
	    }

    }
