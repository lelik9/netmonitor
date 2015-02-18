package Functions;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;






import java.util.List;

import NetMonitor.main;
import SNMP.GetNext;
import SNMP.Walk;

public class VlanTable
    {
	private PreparedStatement preparedStatement = null;
	private String sel;
	private Statement stmt1;
	private Statement stmt2;
	private ResultSet res;
	private Connection connection1;
	private Connection connection2;
	private List<String> vlanName;
	private List<String> vlanIndex;
	
	public void VlanTable(String device) throws  SQLException, IOException
	    {
		Walk walk = new Walk();//SNMP walker
		
		connection1 = main.getConnect1(); //Connect to main DB
		connection2 = main.getConnect2(); //Connect to OID DB
		
		stmt1 = connection1.createStatement();
		stmt2 = connection2.createStatement();

	        
	        //Reading OID from base
		sel = "SELECT oid FROM cisco_oid WHERE object = 'vtpVlanName'";	//OID for CISCO Vlan's name
	        res = stmt2.executeQuery(sel);
	        res.next(); String vtpVlanName = res.getString(1);
	        
	        String sel = "SELECT oid FROM public_oid WHERE object = 'dot1qVlanStaticName'";	//OID for all other Vlan's name   
	        res = stmt2.executeQuery(sel);
	        res.next(); String dot1qVlanStaticName = res.getString(1);
	        
	        
	        //Reading device IP from main base
	        sel = "SELECT IPaddress FROM devices WHERE DeviceID='"+device+"'";
	        res = stmt1.executeQuery(sel);
	        res.next(); String IP = res.getString(1);
	        
		sel = "SELECT Community FROM Devices WHERE DeviceID = '"+device+"'";
		res = stmt1.executeQuery(sel);
		res.next(); String community = res.getString(1);
		
		sel = "SELECT Port FROM Devices WHERE DeviceID = '"+device+"'";
		res = stmt1.executeQuery(sel);
		res.next(); String port = res.getString(1);
	        
	        IP = "udp:"+IP+"/"+port;//Set address of device (CHANGE PORT)
	        
	        String TableClear = "DELETE FROM vlantable WHERE DeviceID ='"+device+"'"; //Clear table
		stmt1.execute(TableClear);
		
		
		walk.walk(IP, dot1qVlanStaticName, community);
		if(!walk.getAllValue().isEmpty())
		    {
			vlanName = walk.getAllValue();
			vlanIndex = walk.getIndex();
			
			int n = 0;
		        while(vlanName.size() > n)
		            {	        	
		        	sel = "INSERT INTO vlantable VALUES ('"+device+"', '"+vlanIndex.get(n)+"', '"+vlanName.get(n)+"')";
		        	stmt1.executeUpdate(sel);

		        	n++;
		            }
		    }else
			{
		
			    walk.walk(IP, vtpVlanName, community);
			    vlanName = walk.getAllValue();
			    vlanIndex = walk.getIndex();
			    
			    int n = 0;
			    while(vlanName.size() > n)
				{	        	
				    sel = "INSERT INTO vlantable VALUES ('"+device+"', '"+vlanIndex.get(n)+"', '"+vlanName.get(n)+"')";
				    stmt1.executeUpdate(sel);

				    n++;
				}
			}
	    }
    }
