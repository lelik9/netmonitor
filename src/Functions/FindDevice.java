package Functions;

import java.io.IOException;

import org.snmp4j.smi.OctetString;

import SNMP.Get;


public class FindDevice 
{
	
	public void FindDevice(int ip1, int ip2,int ip3,int ip4,int mask,int port)
		    throws IOException
		    {
		        Get t = new Get();

		        String IP;

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

		            if(Name!=null)
		            {
		        	System.out.println("Device name: "+Name);

		        	t.get(IP,sysDescr);
		        	Name=t.getGetChar();
		        	System.out.println("Device description: "+Name);
		            
		        	t.get(IP,ipAdEntIfIndex);
		        	Name=t.getGetChar();		  
		        	
		        	t.get(IP,ifPhysAddress+"."+Name);
		        	Name=t.getGetChar();		  
		        	System.out.println("Device Mac address: "+Name);

		            }
		            
		            tmp++;
		            ip4++;
		            
		    	}
		    	
		    }

}
