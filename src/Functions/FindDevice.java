package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.snmp4j.smi.OctetString;

import DB.connect;
import SNMP.Get;


public class FindDevice 
{
	
	public void FindDevice(int ip1, int ip2,int ip3,int ip4,int mask,int port)
		    throws IOException, SQLException
		    {
		        Get t = new Get();
		        InterfaceInfo i = new InterfaceInfo();
		        connect con =new connect();
		        
		        String IP;		        
		        Connection connection = con.connectdb();
		        Statement stmt = connection.createStatement();
		        
			
			
		        int wildcard=255-mask;
		        int tmpIP4=wildcard;
		        int tmp=0;
		        String Name;


		        String sysName= "1.3.6.1.2.1.1.5.0";
		        String sysDescr= "1.3.6.1.2.1.1.1.0";
		        
		        String ifPhysAddress="1.3.6.1.2.1.2.2.1.6";
		       // String IntCount= "1.3.6.1.2.1.2.1.0";
		        
	        	if(ip4<wildcard)
	        	    {
	        		ip4=1;
	        	    }else
	        		{
		        if(tmpIP4<ip4)
		            {
		        	tmpIP4=tmpIP4+wildcard;
		            }
		        if(tmpIP4>ip4)
		            {
		        	tmpIP4=tmpIP4-wildcard;
			        ip4=tmpIP4+2;
			        wildcard=wildcard+1;
		            }
	        		}
		        System.out.println("tmpIP: "+tmpIP4+" ip4= "+ip4);
		  	t.start();
  
	                Name=t.getGetChar();
		    	while(tmp<wildcard-1)
		    	{
		    	    String IPtmp = ip1+"."+ip2+"."+ip3+"."+ip4;
		    	    String ipAdEntIfIndex="1.3.6.1.2.1.4.20.1.2."+IPtmp;
		    	    
		    	    IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;
		    	    System.out.println(IP);
		    		
		            t.get(IP,sysName);
      
		            Name=t.getGetChar();
		            String Name1 = Name;

		            if(Name!=null)
		            {
		        	System.out.println("Device name: "+Name1);

		        	//i.GetIntInfo(ip1, ip2, ip3, ip4, port);
		        	
		        	t.get(IP,sysDescr);
		        	String descr=t.getGetChar();
		        	System.out.println("Device description: "+Name);
		            
		        	t.get(IP,ipAdEntIfIndex);
		        	Name=t.getGetChar();		  
		        	
		        	t.get(IP,ifPhysAddress+"."+Name);
		        	String mac=t.getGetChar();		  
		        	System.out.println("Device Mac address: "+Name);
		        	
		        	String infsel = "SELECT DeviceName FROM devices WHERE DeviceName = '"+Name1+"'";
		        	boolean res = stmt.execute(infsel);

		          	if(res==false)
		        	    {
		        		String info = "INSERT INTO devices VALUES ('"+Name1+"','"+descr+"','"+IPtmp+"','"+mac+"','null')";
		        		stmt.executeUpdate(info);
		        	    }
		        	
		        	
		        	String TableCreate = "CREATE TABLE IF NOT EXISTS "+Name1+" (interface VARCHAR(25), status VARCHAR(6), ip VARCHAR(20), mac VARCHAR(30), PRIMARY KEY ( interface ))";
				stmt.executeUpdate(TableCreate);
				
				i.GetIntInfo(ip1, ip2, ip3, ip4, port, Name1);

		            }
		            
		            tmp++;
		            ip4++;
		            
		    	}
		    	
		    	connection.close();
		    	
		    }

}
