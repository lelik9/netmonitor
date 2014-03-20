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
import DB.Connect;
import NetMonitor.main;

public class GetArp
    {
	public void GetArp(String device, Connection connect) throws SQLException, IOException
	    {
		List <String> IPaddress = new ArrayList();
		List <String> intName = new ArrayList();
		List <String> Mac = new ArrayList();

		

		Statement stmt = connect.createStatement();
		
		Mac.add("MAC address");
	        String Group = "SELECT MAC FROM arptable WHERE DeviceName = '"+device+"'";
	        ResultSet res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	Mac.add(res.getString(1));
	            }
	        
	        intName.add("Interface");
	        Group = "SELECT intName FROM arptable WHERE DeviceName = '"+device+"'";
	        res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	intName.add(res.getString(1));
	            }
	        
	        IPaddress.add("IP address");
	        Group = "SELECT IPaddress FROM arptable WHERE DeviceName = '"+device+"'";
	        res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	IPaddress.add(res.getString(1));
	            }
	        

	        Dump(IPaddress, intName, Mac);

	        connect.close();
	    }
	
	public void Dump(List <String> IPaddress, List <String> intName, List <String> Mac)
	    {
		ServerHandler srv = new ServerHandler();
		
		Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
		
		data.put(0, IPaddress);
		data.put(1, intName);
		data.put(2, Mac);

		
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));
	    }
	    
    }
