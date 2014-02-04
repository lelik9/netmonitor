package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DB.connect;
import NetMonitor.main;
import SNMP.GetNext;

public class ArpTable
    {
	private PreparedStatement preparedStatement = null;
	
	public void GetArp(String device) throws IOException, SQLException
	    {
		String Char = "";
	        connect con = new connect();
	        GetNext n = new GetNext();
	        main m = new main();
	        
	        n.start();
	        
	        Connection connect1 = m.getConnect1();
	        Statement stmt1 = connect1.createStatement();
	        
	        Connection connect2 = m.getConnect2();
	        
	        //Reading OID from base
	        preparedStatement = connect2.prepareStatement("SELECT oid FROM public_oid WHERE object = ?");
	        
	        preparedStatement.setString(1, "atIfIndex");
	        ResultSet res = preparedStatement.executeQuery();
	        res.next(); String atIfIndex = res.getString(1);
	        String atIfIndexOID = atIfIndex;
	        
	        preparedStatement.setString(1, "atNetAddress");
	        res = preparedStatement.executeQuery();
	        res.next(); String atNetAddress = res.getString(1);
	        String atNetAddressOID = atNetAddress;
	        
	        preparedStatement.setString(1, "atPhysAddress");
	        res = preparedStatement.executeQuery();
	        res.next(); String atPhysAddress = res.getString(1);
	        String atPhysAddressOID = atPhysAddress;

	        //Reading device IP from main base
	        String sel = "SELECT IPaddress FROM devices WHERE DeviceName='"+device+"'";
	        res = stmt1.executeQuery(sel);
	        res.next(); String IP = res.getString(1);
	        
		sel = "SELECT Community FROM Devices WHERE DeviceName = '"+device+"'";
		res = stmt1.executeQuery(sel);
		res.next(); String community = res.getString(1);
		
		sel = "SELECT Port FROM Devices WHERE DeviceName = '"+device+"'";
		res = stmt1.executeQuery(sel);
		res.next(); String port = res.getString(1);

	        IP = "udp:"+IP+"/"+port;//Set address of device (CHANGE PORT)
	        
	        String TableClear = "DELETE FROM arptable WHERE DeviceName ='"+device+"'"; //Clear table
		stmt1.execute(TableClear);
		
	        while(Char!=null)
	            {
	        	n.GetNext(IP,atIfIndexOID,atIfIndex, community);  //Get interface Index
	        	Char=n.getChar();
	        	String ind = Char;
	        	atIfIndexOID = n.getNextOID();
	        	if(Char==null) break;
	        	
		        sel = "SELECT intName FROM intinfo WHERE intIndex='"+ind+"' AND DeviceName = '"+device+"'"; //Get interface Name
		        res = stmt1.executeQuery(sel);
		        res.next(); String intName = res.getString(1);		                	
		        
	        	n.GetNext(IP,atNetAddressOID,atNetAddress, community);  //Get interface Index
	        	String ip = n.getChar();
	        	atNetAddressOID = n.getNextOID();

	        	n.GetNext(IP, atPhysAddressOID, atPhysAddress, community);  //Get interface Index
	        	String mac = n.getChar();
	        	atPhysAddressOID = atPhysAddress+"."+ip;	
	        	
        		String AboutTable = "INSERT INTO arptable VALUES ('"+device+"','"+intName+"','"+ip+"','"+mac+"')";
        		stmt1.executeUpdate(AboutTable);

	            }

	    }
    }
