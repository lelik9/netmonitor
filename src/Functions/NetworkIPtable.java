package Functions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DB.connect;
import NetMonitor.main;
import SNMP.GetNext;

public class NetworkIPtable
    {
	public void NetworkIP() throws SQLException
	    {
		 String mac="-";
		 
		 connect con = new connect();
		 main m = new main();
		        		        
		 Connection connect1 = m.getConnect1();
		 Statement stmt1 = connect1.createStatement();
		 Statement stmt2 = connect1.createStatement();
		 Statement stmt3 = connect1.createStatement();
		 
		 String sel = "SELECT DeviceName FROM devices";
		 ResultSet res =stmt1.executeQuery(sel);
		 
		 while(res.next())
		     {
			 String dev = res.getString(1);
			 System.out.println(dev);
			 sel = "SELECT intName FROM intinfo WHERE DeviceName = '"+dev+"' AND Vlan <> 'trunk' AND Vlan <> '' AND MAC <> ''";
			 ResultSet res2 =stmt2.executeQuery(sel);
			 
			 while(res2.next())
			     {
				 String intName = res2.getString(1);
				 System.out.println(intName);
				 sel = "SELECT MAC FROM mactable WHERE intName = '"+intName+"' AND DeviceName = '"+dev+"'";
				 ResultSet res3 =stmt3.executeQuery(sel);
				 if(res3.next()) {mac = res3.getString(1); System.out.println(mac);} 
				
				// sel = "INSERT INTO networkiptable VALUES (DeviceName, intName, MAC, IPaddress),('"+dev+"','"+intName+"','"+mac+"', '')";
				// stmt3.executeUpdate(sel);
			     }
		     }

	    }
    }
