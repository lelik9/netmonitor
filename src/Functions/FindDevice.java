package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.snmp4j.smi.OctetString;

import DB.Connect;
import NetMonitor.main;
import SNMP.Get;
import SNMP.GetNext;


public class FindDevice 
{
    private PreparedStatement preparedStatement = null;
    private Connection connection1;
    private Connection connection2;
	
	public void FindDevice(int ip1, int ip2,int ip3,int ip4,int mask,int port, String community)
		    throws IOException, SQLException
		    {
		        Get t = new Get();
		        GetNext n = new GetNext();
		        InterfaceInfo i = new InterfaceInfo();

		        main m = new main();
		        MacTable mt = new MacTable();
		        VlanTable vt = new VlanTable();
		        ArpTable at = new ArpTable();
		        
		        t.start();
		        n.start();
		        		        

		        Statement stmt1 = connection1.createStatement();
		        	        
					
		        int wildcard=255-mask;
		        int tmpIP4=wildcard;
		        int tmp=0;
		        String Name;
		        String IP;
		        
		        //Reading OID from base
		        preparedStatement = connection2.prepareStatement("SELECT oid FROM public_oid WHERE object = ?");

		        preparedStatement.setString(1,"sysName");
		        ResultSet res = preparedStatement.executeQuery();
		        res.next(); String sysName = res.getString(1);
		        String sysNameOID = sysName;
		        
		        preparedStatement.setString(1, "sysDescr");
		        res = preparedStatement.executeQuery();
		        res.next(); String sysDescr = res.getString(1);
		        String sysDescrOID = sysDescr;
		        
		        preparedStatement.setString(1, "ifPhysAddress");
		        res = preparedStatement.executeQuery();
		        res.next(); String ifPhysAddress = res.getString(1);
		        String ifPhysAddressOID = ifPhysAddress;
		        
		        preparedStatement.setString(1, "ipAdEntIfIndex");
		        res = preparedStatement.executeQuery();
		        res.next(); String ipAdEntIfIndex = res.getString(1);
		        String ipAdEntIfIndexOID = ipAdEntIfIndex;
		        

		        //Checking for subnet
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
		 
  	                
		    	while(tmp<wildcard-1)
		    	{
		    	    String IPtmp = ip1+"."+ip2+"."+ip3+"."+ip4;    	    
		    	    IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;
		    	    System.out.println(IP);
		    	    
                            t.get(IP,sysName);
                            Name=t.getGetChar();
                            String Name1 = Name;

		            if(Name!=null)
		            {
		        //	System.out.println("Device name: "+Name1);
		        	
		        	//Get information of devices in network
		                n.GetNext(IP,sysDescrOID,sysDescr, community);  //Get interface description
		                String descr=n.getChar();
		              //  String descr = Name;
		                sysDescrOID = n.getNextOID();
		        	//System.out.println("Device description: "+Name);
		            
		                n.GetNext(IP,ipAdEntIfIndexOID,ipAdEntIfIndex, community);  //Get interface index
		                String ind=n.getChar();
		                ipAdEntIfIndexOID = n.getNextOID();
		                System.out.println("-> "+ind);
		                n.GetNext(IP,ifPhysAddress+"."+ind,ifPhysAddress, community);  //Get interface mac
		                String mac=n.getChar();
		               // String mac = Name; 
		        //	System.out.println("Device Mac address: "+Name);
		        	
		        	String Sel = "SELECT DeviceName FROM devices WHERE DeviceName = '"+Name1+"'";
		        	/*preparedStatement = connection.prepareStatement("SELECT ? FROM ? WHERE ? = ?");
		        	preparedStatement.setString(1, "DeviceName");
		        	preparedStatement.setString(2, "devices");
		        	preparedStatement.setString(3, "DeviceName");
		        	preparedStatement.setString(4, Name1);
		        	boolean res = preparedStatement.execute();*/
		        	res = stmt1.executeQuery(Sel);

		        	if(res.next()==false)
		        	    {
		        		String info = "UPDATE devices SET DeviceName='"+Name1+"',IPaddress='"+IPtmp+"',MACaddress='"+mac+"',Description='"+descr+"'";
		        		stmt1.executeUpdate(info);
		        	    }		        	
				
				i.GetIntInfo(Name1, connection1, connection2);
				vt.VlanTable(Name1, connection1, connection2);
				at.GetArp(Name1, connection1, connection2);
				mt.MacTable(Name1, connection1, connection2);
				
		            }
		            
		            tmp++;
		            ip4++;
		            
		    	}
		    	

		    	
		    }
	
	 public synchronized void Connect(Connection connect1, Connection connect2)
	     {
		 connection1 = connect1;
		 connection2 = connect2;
	     }

}
