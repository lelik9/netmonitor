package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DB.connect;
import SNMP.GetNext;

public class MacTable
    {
	private PreparedStatement preparedStatement = null;
	
	public void MacTable(String device) throws IOException, SQLException
	    {
	        connect con = new connect();
	        GetNext n = new GetNext();
	        n.start();
	        
	        Connection connect = con.connectdb("monitor_db");//Connect to main base
	        Statement stmt = connect.createStatement();
	        
	        Connection connection = con.connectdb("mib_db");//Connect to OID base
	        Statement stmt2 = connection.createStatement();
	        
	        //Reading OID from base      
	        String  sel = "SELECT oid FROM public_oid WHERE object = 'dot1dTpFdbAddress'";	        
	        ResultSet res = stmt2.executeQuery(sel);
	        res.next(); String dot1dTpFdbAddress = res.getString(1);
	        String dot1dTpFdbAddressOID = dot1dTpFdbAddress;
	        
	        sel = "SELECT oid FROM public_oid WHERE object = 'dot1dTpFdbPort'";	        
	        res = stmt2.executeQuery(sel);
	        res.next(); String dot1dTpFdbPort = res.getString(1);
	        String dot1dTpFdbPortOID = dot1dTpFdbPort;
	        
	        //Reading device IP from main base
	        sel = "SELECT IPaddress FROM devices WHERE DeviceName='"+device+"'";
	        res = stmt.executeQuery(sel);
	        res.next(); String IP = res.getString(1);
	        
		sel = "SELECT Community FROM Devices WHERE DeviceName = '"+device+"'";
		res = stmt.executeQuery(sel);
		res.next(); String community = res.getString(1);
		
		sel = "SELECT Port FROM Devices WHERE DeviceName = '"+device+"'";
		res = stmt.executeQuery(sel);
		res.next(); String port = res.getString(1);
	        
	        IP = "udp:"+IP+"/"+port;//Set address of device (CHANGE PORT)
	        
	        String TableClear = "DELETE FROM mactable WHERE DeviceName ='"+device+"'"; //Clear table
		stmt.execute(TableClear);
		
		
		sel = "SELECT vlanNum FROM vlantable WHERE DeviceName = '"+device+"'";
		res = stmt.executeQuery(sel);
		
		while(res.next())
		    {
			String vlan = res.getString(1);
			System.out.println(vlan);
			String Char ="";
			
			dot1dTpFdbAddressOID = dot1dTpFdbAddress;
			dot1dTpFdbPortOID = dot1dTpFdbPort;
			while(Char!=null)
			    {
				n.GetNext(IP,dot1dTpFdbAddressOID,dot1dTpFdbAddress,community+"@"+vlan);  //Get Vlan name
				Char = n.getChar();
				String mac = Char;
				if(mac==null)break;
				dot1dTpFdbAddressOID = n.getNextOID();
				
				n.GetNext(IP,dot1dTpFdbPortOID,dot1dTpFdbPort,community+"@"+vlan);  //Get Vlan name
				Char = n.getChar();
				String intport = Char;
				dot1dTpFdbPortOID = n.getNextOID();
				
				String sel2 = "INSERT INTO mactable VALUES ('"+device+"', '"+intport+"', '"+mac+"', '"+vlan+"')";
				stmt.executeUpdate(sel2);
			    }
		    }
	    }
    }
