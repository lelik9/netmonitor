package DB.select;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import server.ServerHandler;
import DB.connect;

public class GetIntInfo
    {
	public void GetIntInfo(String device) throws SQLException, IOException
	    {
		List <String> intIndex = new ArrayList();
		List <String> intName = new ArrayList();
		List <String> intStatus = new ArrayList();
		List <String> IPaddress = new ArrayList();
		List <String> Vlan = new ArrayList();
		List <String> MAC = new ArrayList();
		
		connect con = new connect();
		Connection connect = con.connectdb("monitor_db");
		Statement stmt = connect.createStatement();
		
		intIndex.add("Interface index");
	        String Group = "SELECT intIndex FROM intinfo WHERE DeviceName = '"+device+"'";
	        ResultSet res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	intIndex.add(res.getString(1));
	            }
	        
	        intName.add("Interface name");
	        Group = "SELECT intName FROM intinfo WHERE DeviceName = '"+device+"'";
	        res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	intName.add(res.getString(1));
	            }
	        
	        intStatus.add("Interface status");
	        Group = "SELECT intStatus FROM intinfo WHERE DeviceName = '"+device+"'";
	        res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	intStatus.add(res.getString(1));
	            }
	        
	        IPaddress.add("Interface IP address");
	        Group = "SELECT IPaddress FROM intinfo WHERE DeviceName = '"+device+"'";
	        res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	IPaddress.add(res.getString(1));
	            }
	        
	        Vlan.add("Vlan");
	        Group = "SELECT Vlan FROM intinfo WHERE DeviceName = '"+device+"'";
	        res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	Vlan.add(res.getString(1));
	            }
	        
	        MAC.add("Interface MAC address");
	        Group = "SELECT MAC FROM intinfo WHERE DeviceName = '"+device+"'";
	        res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	MAC.add(res.getString(1));
	            }

	        Dump(intIndex, intName, intStatus, IPaddress, Vlan, MAC);

	        connect.close();
	    }
	
	public void Dump(List <String> intIndex, List <String> intName, List <String> intStatus, List <String> IPaddress, 
		List <String> Vlan, List <String> MAC)
	    {
		ServerHandler srv = new ServerHandler();
		
		Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
		
		data.put(0, intName);
		data.put(1, intIndex);
		data.put(2, intStatus);
		data.put(3, IPaddress);
		data.put(4, Vlan);
		data.put(5, MAC);
		
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));
	    }
    }
