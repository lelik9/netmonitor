package server.remoteFunctions;

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
import NetMonitor.main;

public class DeviceGroupSelect
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	
	public void DeviceGroupSelect() throws SQLException
	    {
		List <String> groups = new ArrayList();
		List <String> devices = new ArrayList();
		
		connection1 = main.getConnect1();
		stmt1 = connection1.createStatement();
		
		select = "SELECT * FROM group_name";
		res = stmt1.executeQuery(select);
		res.first();
		String groupID = res.getString(1);
		groups.add(res.getString(2));
		while(res.next())
		    {			
			groups.add(res.getString(2));
		    }
		
		select = "SELECT DeviceName FROM devices WHERE GroupID = '"+groupID+"'";
		res = stmt1.executeQuery(select);
		while(res.next())
		    {			
			devices.add(res.getString(1));
		    }
		
		Dump(groups, devices);
	    }
	
	public void Dump(List <String> groups, List <String> devices)
	    {
		ServerHandler srv = new ServerHandler();
		
		Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
		
		data.put(0, groups);
		data.put(1, devices);

		
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));
	    }



    }
