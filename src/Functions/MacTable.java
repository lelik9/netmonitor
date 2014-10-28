package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import DB.Connect;
import NetMonitor.main;
import SNMP.Get;
import SNMP.GetNext;
import SNMP.ResponseResult;
import SNMP.Walk;

public class MacTable
    {

	private String sel;
	private Statement stmt1;
	private Statement stmt2;
	private Statement stmt3;
	private ResultSet res;
	private Connection connection1;
	private Connection connection2;
	private List<String> Mac;
	private List<String> Port;
	private List<String> PortIndex;
	private List<String> Name;
	private List<String> Vlan;
	
	public void MacTable(String device) throws IOException, SQLException
	    {

		Walk walk = new Walk();//SNMP walker
	        
		connection1 = main.getConnect1();
		connection2 = main.getConnect2();
		
		stmt1 = connection1.createStatement();
		stmt2 = connection2.createStatement();
		stmt3 = connection1.createStatement();//Used for inserting to table;
	        
	        //Reading OID from base      
		sel = "SELECT oid FROM public_oid WHERE object = 'dot1dTpFdbAddress'"; //A unicast MAC address
		res = stmt2.executeQuery(sel);
	        res.next(); String dot1dTpFdbAddress = res.getString(1);

	        sel = "SELECT oid FROM public_oid WHERE object = 'dot1dTpFdbPort'"; //port
	        res = stmt2.executeQuery(sel);
	        res.next(); String dot1dTpFdbPort = res.getString(1);
	        
	        sel = "SELECT oid FROM public_oid WHERE object = 'dot1dBasePortIfIndex'";//port index	        
	        res = stmt2.executeQuery(sel);
	        res.next(); String dot1dBasePortIfIndex = res.getString(1);

	        
	        //Reading device IP from main base
	        sel = "SELECT IPaddress, Community, Port, GroupID  FROM devices WHERE DeviceName='"+device+"'";
	        res = stmt1.executeQuery(sel);
	        res.next(); 
	        String IP = res.getString(1);
	        String community = res.getString(2);
	        String port = res.getString(3);
	        String group = res.getString(4);
	        
	        IP = "udp:"+IP+"/"+port;//Set address of device (CHANGE PORT)
	        
	        String TableClear = "DELETE FROM mactable WHERE DeviceName ='"+device+"'"; //Clear table
		stmt1.execute(TableClear);
		
		
		sel = "SELECT vlanNum FROM vlantable WHERE DeviceName = '"+device+"'";
		res = stmt3.executeQuery(sel);

		while(res.next())
		    {
			String vlan = res.getString(1);

				walk.walk(IP, dot1dTpFdbAddress, community+"@"+vlan);
				Mac = walk.getAllValue();

				if(Mac==null)break;

				walk.walk(IP, dot1dTpFdbPort, community+"@"+vlan);
				Port = walk.getAllValue();
				
				int n = 0;
				while(Port.size() > n)
				    {
					walk.walk(IP, dot1dBasePortIfIndex+"."+Port.get(n), community);//Get interface index
					PortIndex = walk.getAllValue();
				    }
				
				n = 0;
				while(Port.size() > n)
				    {
					sel = "SELECT data FROM group_"+group+"_data join templates where templates.OIDname='ifDescr' && group_"+group+"_data.name=templates.name && group_"+group+"_data.OIDindex= = '"+Port.get(n)+"'"; //Get interface name
					ResultSet res2 = stmt1.executeQuery(sel);
					res2.next(); 
					Name.add(res2.getString(1));
					n++;
				    }
				Vlan.add(vlan);
			    
		    }
		int i = 0;
		while(Mac.size() > i)
		    {
			sel = "INSERT INTO mactable VALUES ('"+device+"', '"+Name.get(i)+"', '"+Mac.get(i)+"', '"+Vlan.get(i)+"')";
			stmt1.executeUpdate(sel);
		    }
	    }
    }
