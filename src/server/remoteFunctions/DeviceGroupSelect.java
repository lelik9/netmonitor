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
	private Statement stmt2;
	private ResultSet res;
	private ResultSet res2;
	private Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
	
	public void DeviceGroupSelect() throws SQLException
	    {		
		connection1 = main.getConnect1();
		stmt1 = connection1.createStatement();
		stmt2 = connection1.createStatement();
		
		select = "SELECT * FROM group_name";
		res = stmt1.executeQuery(select);

		int i = 0;
		while(res.next())
		    {		
			List <String> devices = new ArrayList();
			
			devices.add(res.getString(2));
			select = "SELECT DeviceName FROM devices WHERE GroupID = '"+res.getString(1)+"'";
			res2 = stmt2.executeQuery(select);
			while(res2.next())
			    {						
				devices.add(res2.getString(1));
			    }
			data.put(i, devices);

			i++;
		    }
		
		System.out.println(data);
		Dump();
	    }
	
	public void Dump()
	    {
		ServerHandler srv = new ServerHandler();	
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));
	    }



    }
