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
	
	/**
	 * Изменение информации устройства
	 * @param data
	 */
	private void Update(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
				
				select = "UPDATE devices SET DeviceName = '"+data.get("name")+"' WHERE IPaddress = '"+data.get("ip")+"'";
				stmt1.executeUpdate(select);
				result.add("Настройки изменены");
				Data.put(0, result);
			    
			
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			result.add("FAIL");
			Data.put(0, result);
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    
	
	    }
	
	/**
	 * Удаление устройства
	 * @param deviceName
	 */
	private void Delete(String deviceName)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT DeviceID FROM devices WHERE DeviceName = '"+deviceName+"'";
			res = stmt1.executeQuery(select);
			res.next();
			String deviceID = res.getString(1);
				
			select = "DELETE FROM device_data WHERE DeviceID = '"+deviceID+"'";
			stmt1.executeUpdate(select);
			
			select = "DELETE FROM selectdata WHERE DeviceID = '"+deviceID+"'";
			stmt1.executeUpdate(select);
			
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
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }
	

    }
