package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.snmp4j.smi.OctetString;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import server.ServerHandler;
import DB.Connect;
import NetMonitor.main;
import SNMP.Get;
import SNMP.GetNext;
import SNMP.Walk;


public class FindDevice 
{
    private PreparedStatement preparedStatement = null;
    private Connection connection1;
    private Connection connection2;
    private List<String> Value;
	
	public void FindDevice(Map<String, String> data)
		    throws IOException, SQLException
		    {

		        Walk w = new Walk();

		        System.out.println(data);		        
		        connection1 = main.getConnect1();
		        connection2 = main.getConnect2();
		        Statement stmt1 = connection1.createStatement();
		        
		        int mask = Integer.parseInt(data.get("mask"));
		        int ip1 = Integer.parseInt(data.get("ip1"));
		        int ip2 = Integer.parseInt(data.get("ip2"));
		        int ip3 = Integer.parseInt(data.get("ip3"));
		        int ip4 = Integer.parseInt(data.get("ip4"));
		        int port = Integer.parseInt(data.get("port"));
		        String community = data.get("community");
					
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
		        
	/*	        preparedStatement.setString(1, "sysDescr");
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
		        String ipAdEntIfIndexOID = ipAdEntIfIndex;*/
		        

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
		    	    
                        //    t.get(IP,sysName);
                       //    Name=t.getGetChar();
                            
                            w.walk(IP,sysName,community);
                            Value = w.getAllValue();
                            String Name1 = Value.get(0);
                        //   String Name1 = Name;
		            if(Name1!=null)
		            {
		        	System.out.println("Device name: "+Name1);
		        /*	
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
		        	*/
		        /*	w.walk(IP, sysDescr, community);
		        	Value = w.getAllValue();
		        	String descr = Value.get(0);
		        	
		        	w.walk(IP, ipAdEntIfIndex, community);
		        	Value = w.getAllValue();
		        	String ind = Value.get(0);
		        	System.out.println("-> "+ind);
		        	w.walk(IP, ifPhysAddress+"."+ind, community);
		        	Value = w.getAllValue();
		        	String mac = Value.get(0);*/
		        	
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
		        		String info = "INSERT INTO devices (DeviceName, IPaddress, Community, Port) VALUES ('"+Name1.split("\\.")[0]+"', '"+IPtmp+"', '"+community+"', '"+port+"')";
		        	//	String info = "UPDATE devices SET DeviceName='"+Name1+"',IPaddress='"+IPtmp+"',MACaddress='"+mac+"',Description='"+descr+"'";
		        		stmt1.executeUpdate(info);
		        	    }	
		        	else
		        	    {
			        	String info = "UPDATE devices SET DeviceName='"+Name1.split("\\.")[0]+"',IPaddress='"+IPtmp+"',Community='"+community+"',Port='"+port+"'";
		        		stmt1.executeUpdate(info);
		        	    }
				
				//i.GetIntInfo(Name1, connection1, connection2);
				//vt.VlanTable(Name1, connection1, connection2);
				//at.GetArp(Name1, connection1, connection2);
				//mt.MacTable(Name1, connection1, connection2);
				
		            }
		            
		            tmp++;
		            ip4++;
		            
		    	}
		    	

		    	
		    }
	public void Dump()
	    {
		ServerHandler srv = new ServerHandler();
		List<String> status = new ArrayList<String>();
		status.add("complete");
		Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
		
		data.put(0, status);
		
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));
	    }
	


}
