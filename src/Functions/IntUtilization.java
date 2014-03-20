package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DB.Connect;
import SNMP.GetNext;

public class IntUtilization
    {
	private PreparedStatement preparedStatement = null;
	
	public void IntUtil(String device) throws SQLException, IOException
	    {
	        Connect con = new Connect();
	        GetNext n = new GetNext();
	        n.start();
	        
	        Connection connect = con.connectdb("monitor_db");//Connect to main base
	        Statement stmt = connect.createStatement();
	        
	        Connection connection = con.connectdb("mib_db");//Connect to OID base
	        //Reading OID from base
	        preparedStatement.setString(1, "ifSpeed");
	        ResultSet res = preparedStatement.executeQuery();
	        res.next(); String ifSpeed = res.getString(1);
	        String ifSpeedOID = ifSpeed;
	        
	        preparedStatement.setString(1, "ifInOctets");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifInOctets = res.getString(1);
	        String ifInOctetsOID = ifInOctets;
	        
	        preparedStatement.setString(1, "ifOutOctets");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutOctets = res.getString(1);
	        String ifOutOctetsOID = ifOutOctets;
	        
	        preparedStatement.setString(1, "ifDescr");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifDescr = res.getString(1);
	        String ifDescrOID=ifDescr; 
	        
	        //Reading device IP from main base
	        String sel = "SELECT IPaddress FROM devices WHERE DeviceName='"+device+"'";
	        res = stmt.executeQuery(sel);
	        res.next(); String IP = res.getString(1);
	        
	        IP = "udp:"+IP+"/161";//Set address of device (CHANGE PORT)
	        
	        
	        connect.close();
	        connection.close();
	    }
    }
