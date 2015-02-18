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

/**
 * 
 * @author Alex
 *
 */
public class FindDevice 
{
    private PreparedStatement preparedStatement = null;
    private Connection connection1;
    private Connection connection2;
    private List<String> Value;
	
    /**
	 * 
	 * @param data (mask:последние октет маски, ip1:, ip2:, ip3:, ip4: с первого по четвертый октет адреса, port: порт SNMP, community: комьюнити)
	 * @throws IOException
	 * @throws SQLException
	 */
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
                               
                            w.walk(IP,sysName,community);
                            Value = w.getAllValue();

		            if(Value.size() !=0 )
		            {
	                           String Name1 = Value.get(0);
			        	
		        	String Sel = "SELECT DeviceName FROM devices WHERE DeviceName = '"+Name1+"'";
		        	res = stmt1.executeQuery(Sel);

		        	if(res.next() == false)
		        	    {		        		
		        		String info = "INSERT INTO devices (DeviceName, IPaddress, Community, Port, GroupID) VALUES "
		        			+ "('"+Name1.split("\\.")[0]+"', '"+IPtmp+"', '"+community+"', '"+port+"', '1')";
		        		stmt1.executeUpdate(info);
		        	    }	
		        	else
		        	    {
			        	String info = "UPDATE devices SET IPaddress='"+IPtmp+"',Community='"+community+"',Port='"+port+"' WHERE DeviceName='"+Name1.split("\\.")[0]+"'";
		        		stmt1.executeUpdate(info);
		        	    }
				
				
		            }
		            
		            tmp++;
		            ip4++;
		            
		    	}
		    	
			List<String> status = new ArrayList<String>();
			status.add("Устройства добавленны");
			Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
			
			Data.put(0, status);
			server.Dump dump = new server.Dump();
			dump.Dump(Data);
		    }

}
