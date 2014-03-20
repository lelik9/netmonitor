package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DB.connect;
import NetMonitor.main;
import SNMP.Get;
import SNMP.GetNext;

public class MacTable
    {
		
	public void MacTable(String device, Connection connect1, Connection connect2) throws IOException, SQLException
	    {

	        GetNext n = new GetNext();
	        main m = new main();
	        
	        n.start();

	        
	        Statement stmt1 = connect1.createStatement();
	        Statement stmt3 = connect1.createStatement();
	        
	        Statement stmt2 = connect2.createStatement();
	        
	        //Reading OID from base      
	        String  sel = "SELECT oid FROM public_oid WHERE object = 'dot1dTpFdbAddress'";	        
	        ResultSet res = stmt2.executeQuery(sel);
	        res.next(); String dot1dTpFdbAddress = res.getString(1);
	        String dot1dTpFdbAddressOID = dot1dTpFdbAddress;
	        
	        sel = "SELECT oid FROM public_oid WHERE object = 'dot1dTpFdbPort'";	        
	        res = stmt2.executeQuery(sel);
	        res.next(); String dot1dTpFdbPort = res.getString(1);
	        String dot1dTpFdbPortOID = dot1dTpFdbPort;
	        
	        sel = "SELECT oid FROM public_oid WHERE object = 'dot1dBasePortIfIndex'";	        
	        res = stmt2.executeQuery(sel);
	        res.next(); String dot1dBasePortIfIndex = res.getString(1);
	        String dot1dBasePortIfIndexOID = dot1dBasePortIfIndex;
	        
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
	        
	        String TableClear = "DELETE FROM mactable WHERE DeviceName ='"+device+"'"; //Clear table
		stmt1.execute(TableClear);
		
		
		sel = "SELECT vlanNum FROM vlantable WHERE DeviceName = '"+device+"'";
		res = stmt3.executeQuery(sel);

		while(res.next())
		    {
			String vlan = res.getString(1);
			String Char ="";
		//	System.out.println("vlan :"+vlan);

			dot1dTpFdbAddressOID = dot1dTpFdbAddress;
			dot1dTpFdbPortOID = dot1dTpFdbPort;
			dot1dBasePortIfIndexOID = dot1dBasePortIfIndex;
			while(Char!=null)
			    {
				
				n.GetNext(IP,dot1dTpFdbAddressOID,dot1dTpFdbAddress,community+"@"+vlan);  //Get Vlan name
				dot1dTpFdbAddressOID = n.getNextOID();
				Char = n.getChar();
				String mac = Char;
		//		System.out.println("mac: "+mac);
				//System.out.println("nextOID: "+dot1dTpFdbAddressOID);
				if(mac==null)break;
				//dot1dTpFdbAddressOID = n.getNextOID();
				
				n.GetNext(IP,dot1dTpFdbPortOID,dot1dTpFdbPort,community+"@"+vlan);  //Get port index
				String intport = n.getChar();
		//		System.out.println(intport);
				dot1dTpFdbPortOID = n.getNextOID();
				/*


				System.out.println(dot1dBasePortIfIndexOID);
				n.GetNext(IP,dot1dBasePortIfIndexOID,dot1dBasePortIfIndex,community+"@"+vlan);  //Get interface index
				String numport = n.getChar();
				System.out.println("port:"+numport);
				dot1dBasePortIfIndexOID = n.getNextOID();
				
				if(numport ==null) break;
				
				sel = "SELECT intName FROM intinfo WHERE intIndex = '"+numport+"'"; //Get interface name
				ResultSet res2 = stmt1.executeQuery(sel);
				res2.next(); String nameport = res2.getString(1);
				
				sel = "INSERT INTO mactable VALUES ('"+device+"', '"+nameport+"', '"+mac+"', '"+vlan+"')";
				stmt1.executeUpdate(sel);*/
				
			    }
		    }
	    }
    }
