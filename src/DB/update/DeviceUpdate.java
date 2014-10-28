package DB.update;

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

import NetMonitor.main;
import server.ServerHandler;

public class DeviceUpdate
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	private List<String> result = new ArrayList<>();
	
	public void DeviceUpdate(Map<String, String> data)
	{
		switch(data.get("update"))
		{
			
		    case "update":
			Update(data);
			break;
			
		    case "delete":
			Delete(data.get("name"));
			break;
			
		}
	}
	
	private void Update(Map<String, String> data)
	    {
		
	    }
	
	private void Delete(String deviceName)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
				
				select = "DELETE FROM devices WHERE DeviceName = '"+deviceName+"'";
				stmt1.executeUpdate(select);
				result.add("Устройство удалено");
				Data.put(0, result);
			    
			
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			result.add("FAIL");
			Data.put(0, result);
			e.printStackTrace();
		    }
		Dump();
	    }
	
	public void Dump()
	    {
		ServerHandler srv = new ServerHandler();
		
		
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(Data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(Data));
	    }
    }
