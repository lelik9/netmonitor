package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import SNMP.GetNext;

public class VlanTable
    {
	private PreparedStatement preparedStatement = null;
	
	public void VlanTable(String device, Connection connect1, Connection connect2) throws IOException, SQLException
	    {
		String Char = "";
	        GetNext n = new GetNext();
	        n.start();


	        Statement stmt1 = connect1.createStatement();
	        Statement stmt2 = connect2.createStatement();
	        
	        //Reading OID from base
	        String sel = "SELECT oid FROM cisco_oid WHERE object = 'vtpVlanName'";	        
	        ResultSet res = stmt2.executeQuery(sel);
	        res.next(); String vtpVlanName = res.getString(1);
	        String vtpVlanNameOID = vtpVlanName;
	        
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
