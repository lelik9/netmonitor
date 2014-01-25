package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.snmp4j.smi.OctetString;

import DB.connect;
import SNMP.Get;
import SNMP.GetNext;


public class FindDevice 
{
    private PreparedStatement preparedStatement = null;
	
	public void FindDevice(int ip1, int ip2,int ip3,int ip4,int mask,int port)
		    throws IOException, SQLException
		    {
		        Get t = new Get();
		        GetNext n = new GetNext();
		        InterfaceInfo i = new InterfaceInfo();
		        connect con =new connect();
		        t.start();
		        n.start();
		        
		        
		        Connection connection = con.connectdb("monitor_db");//Connect to main base
		        Statement stmt = connection.createStatement();
		        Connection connect = con.connectdb("mib_db");//Connect to OID base
		        
					
		        int wildcard=255-mask;
		        int tmpIP4=wildcard;
		        int tmp=0;
		        int k=1;
		        String Name;
		        String IP;
		        
		        //Reading OID from base
		        preparedStatement = connect.prepareStatement("SELECT oid FROM public_oid WHERE object = ?");

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
		        	System.out.println("Device name: "+Name1);
		        	
		        	//Get information of devices in network
		                n.GetNext(IP,sysDescrOID,sysDescr);  //Get interface description
		                String descr=n.getChar();
		              //  String descr = Name;
		                sysDescrOID = n.getNextOID();
		        	System.out.println("Device description: "+Name);
		            
		                n.GetNext(IP,ipAdEntIfIndexOID,ipAdEntIfIndex);  //Get interface index
		                String ind=n.getChar();
		                ipAdEntIfIndexOID = n.getNextOID();
		                System.out.println("-> "+ind);
		                n.GetNext(IP,ifPhysAddress+"."+ind,ifPhysAddress);  //Get interface mac
		                String mac=n.getChar();
		               // String mac = Name; 
		        	System.out.println("Device Mac address: "+Name);
		        	
		        	String Sel = "SELECT DeviceName FROM devices WHERE DeviceName = '"+Name1+"'";
		        	/*preparedStatement = connection.prepareStatement("SELECT ? FROM ? WHERE ? = ?");
		        	preparedStatement.setString(1, "DeviceName");
		        	preparedStatement.setString(2, "devices");
		        	preparedStatement.setString(3, "DeviceName");
		        	preparedStatement.setString(4, Name1);
		        	boolean res = preparedStatement.execute();*/
		        	res = stmt.executeQuery(Sel);

		        	if(res.next()==false)
		        	    {
		        		String info = "UPDATE devices SET DeviceName='"+Name1+"',IPaddress='"+IPtmp+"',MACaddress='"+mac+"',Description='"+descr+"'";
		        		stmt.executeUpdate(info);
		        	    }
		        	
		          	
		        	String TableCreate = "CREATE TABLE IF NOT EXISTS dev"+k+"Int (interface VARCHAR(100), status VARCHAR(6), ip VARCHAR(20),Vlan VARCHAR(15) ,mac VARCHAR(30), PRIMARY KEY ( interface ))";
				stmt.executeUpdate(TableCreate);
				
				
		        	/*preparedStatement.setString(1, "tableName");
		        	preparedStatement.setString(2, "tablesname");
		        	preparedStatement.setString(3, "tableName");
		        	preparedStatement.setString(4, Name1);*/
		        	Sel = "SELECT DeviceName FROM tablesname WHERE DeviceName = '"+Name1+"'";
		        	res = stmt.executeQuery(Sel);

		        	if(res.next()==false)
		        	    {
		        		String AboutTable = "INSERT INTO tablesname VALUES ('"+Name1+"','dev"+k+"Int', '','')";
		        		stmt.executeUpdate(AboutTable);
		        	    }
				
				i.GetIntInfo(ip1, ip2, ip3, ip4, port, "dev"+k+"Int");
				k++;
		            }
		            
		            tmp++;
		            ip4++;
		            
		    	}
		    	
		    	connection.close();
		    	
		    }

}
