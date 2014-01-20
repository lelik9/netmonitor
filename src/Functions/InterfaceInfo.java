package Functions;

import java.io.IOException;

import SNMP.Get;
import SNMP.GetNext;

public class InterfaceInfo
{
    
  public void GetIntInfo(int ip1,int ip2,int ip3,int ip4, int port) throws IOException
    {
	String ifDescr = "1.3.6.1.2.1.2.2.1.2";
	String ifOperStatuss ="1.3.6.1.2.1.2.2.1.8";
	String ifPhysAddress = "1.3.6.1.2.1.2.2.1.6";
	String IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;
	String ipAdEntAdd = "1.3.6.1.2.1.4.20.1.1";
	String ipAdEntIfIndex = "1.3.6.1.2.1.4.20.1.2";
	String Char;
	String NextOID;
	
        GetNext n = new GetNext();
        Get t = new Get();
        n.start();
        t.start();
        
        n.GetNext(IP,ifDescr,ifDescr);
	Char=n.getChar();
	NextOID = n.getNextOID();

	//Get interface Name
        while(Char!=null)
            {
               	System.out.println(Char);
        	n.GetNext(IP,NextOID,ifDescr);
        	Char=n.getChar();
        	NextOID = n.getNextOID();
 
            }
        
        n.GetNext(IP,ifOperStatuss,ifOperStatuss);
	Char=n.getChar();
	if(Char.equals("1")) 
	    {
		Char="Up";
			 
	    } if(Char.equals("2"))
		{
		    Char="Down";
		}	
	NextOID = n.getNextOID();

	//Get interface status
        while(Char!=null)
            {
               	System.out.println(Char);
        	n.GetNext(IP,NextOID,ifOperStatuss);
        	Char=n.getChar();
        	if(Char==null) break;
        	if(Char.equals("1")) 
        	    {
        		Char="Up";
        			 
        	    } if(Char.equals("2"))
        		{
        		    Char="Down";
        		}
        	NextOID = n.getNextOID();
 
            }
        
      //Get interface MAC
        n.GetNext(IP,ifPhysAddress,ifPhysAddress);
	Char=n.getChar();
	NextOID = n.getNextOID();

	
        while(Char!=null)
            {
               	System.out.println(Char);
        	n.GetNext(IP,NextOID,ifPhysAddress);
        	Char=n.getChar();
        	NextOID = n.getNextOID();
 
            }
        
      //Get interface IP
        n.GetNext(IP,ipAdEntAdd,ipAdEntAdd);
	Char=n.getChar();
	
	t.get(IP,ipAdEntIfIndex+"."+Char);
	Char=t.getGetChar();

	t.get(IP,ifDescr+"."+Char);
        Char=t.getGetChar();
	NextOID = n.getNextOID();


        while(Char!=null)
            {
               	System.out.println(Char);
        	n.GetNext(IP,NextOID,ipAdEntAdd);
        	Char=n.getChar();
        	if(Char==null) break;
        	
        	t.get(IP,ipAdEntIfIndex+"."+Char);
        	Char=t.getGetChar();
        	
        	t.get(IP,ifDescr+"."+Char);
        	Char=t.getGetChar();
        	
        	NextOID = n.getNextOID();
 
            }

    }
}	
