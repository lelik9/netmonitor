package Functions;


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
	private Connection connection1;
	private Connection connection2;
	private List<String> vlan;
	
	public void VlanTable(String device) throws  SQLException
	    {
		Walk walk = new Walk();//SNMP walker
		
		connection1 = main.getConnect1();
		connection2 = main.getConnect2();
		
		stmt1 = connection1.createStatement();
		stmt2 = connection2.createStatement();

	        
	        //Reading OID from base
	        String sel = "SELECT oid FROM cisco_oid WHERE object = 'vtpVlanName'";	        
	        ResultSet res = stmt2.executeQuery(sel);
	        res.next(); String vtpVlanName = res.getString(1);
	        
	        //Reading device IP from main base
	        sel = "SELECT IPaddress FROM devices WHERE DeviceName='"+device+"'";
	        res = stmt1.executeQuery(sel);
	        res.next(); String IP = res.getString(1);
	        
		sel = "SELECT Community FROM Devices WHERE DeviceName = '"+device+"'";
		res = stmt1.executeQuery(sel);
		res.next(); String community = res.getString(1);
		
		sel = "SELECT Port FROM Devices WHERE DeviceName = '"+device+"'";
		res = stmt1.executeQuery(sel);
		res.next(); String port = res.getString(1);
	        
	        IP = "udp:"+IP+"/"+port;//Set address of device (CHANGE PORT)
	        
	        String TableClear = "DELETE FROM vlantable WHERE DeviceName ='"+device+"'"; //Clear table
		stmt1.execute(TableClear);
		
		walk.walk(IP, vtpVlanName, community);
		
	        while(Char!=null)
	            {
	        	n.GetNext(IP,vtpVlanNameOID,vtpVlanName, community);  //Get Vlan name
	        	vtpVlanNameOID = n.getNextOID();
	        	Char=n.getChar();
	        	String name = Char;
	        	if(Char==null) break;
	        	
        		int a = vtpVlanName.length(); //Get Vlan number
        		String num = vtpVlanNameOID.substring(a);
	        	
	        	sel = "INSERT INTO vlantable VALUES ('"+device+"', '"+num+"', '"+name+"')";
	        	stmt1.executeUpdate(sel);
	            }
	    }
    }
