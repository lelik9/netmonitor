package server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.yaml.snakeyaml.Yaml;

import Functions.ExtIntInfo;
import Functions.VlanTable;

public class ServerHandler implements IoHandler
    {
	
	 public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
	    {
	        cause.printStackTrace();
	    }

	    public void messageReceived( IoSession session, Object message ) throws Exception
	    {
		ExtIntInfo ex =new ExtIntInfo();
		Yaml yaml = new Yaml();
		
		Map<String, String> data = (Map<String, String>) yaml.load(message.toString());
		System.out.println(data.get("dict"));
		

	      /*  String str = message.toString();
	        if(!str.isEmpty())
	            {
	        if( str.trim().equalsIgnoreCase("quit") ) 
	            {
	            session.close(false);
	            return;
	            }

	        if(str.substring(0, 1).equals("n"))
	            {
	        	System.out.println("n");
	        	ex.ExtIntInfo(str.substring(1));
	            }
	            }*/
	    }

	    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
	    {
	        System.out.println( "IDLE " + session.getIdleCount( status ));
	    }

	    @Override
	    public void messageSent(IoSession arg0, Object arg1)
		    throws Exception
		{
		    // TODO Auto-generated method stub
		    
		}

	    @Override
	    public void sessionClosed(IoSession arg0) throws Exception
		{
		    // TODO Auto-generated method stub
		    
		}

	    @Override
	    public void sessionCreated(IoSession arg0) throws Exception
		{
		    // TODO Auto-generated method stub
		    
		}

	    @Override
	    public void sessionOpened(IoSession arg0) throws Exception
		{
		    // TODO Auto-generated method stub
		    
		}
    }
