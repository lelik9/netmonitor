package DB.select;

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

public class GetDeviceAndGroup
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private Statement stmt2;
	private ResultSet res;
	private ResultSet res2;
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	
	public void GetDeviceAndGroup(Map<String, String> data)
	    {		
		switch(data.get("subFunc"))
		{
		    case "devices":
			GetDeviceList();
			break;
			
		    case "devFromTempl":
			GetDeviceFromTemplate(data);
			break;
			
		    case "groups":
			GetGroupList();
			break;
			
		    case "groupFromTempl":
			GetGroupFromTemplate(data);
			break;
			
		    case "devAndGroup":
			DeviceWithGroupSelect();
			break;
			
		}
	    }
	
	/**
	 * Возвращает список всех устройств и названия групп, отсортированных по группам
	 */
	private void DeviceWithGroupSelect() 
	    {		
		connection1 = main.getConnect1();
		try
		    {
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
			Data.put(i, devices);

			i++;
		    }
		
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }
	
	/**
	 * Возвращает список всех устройств
	 */
	private void GetDeviceList()
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT DeviceName FROM devices";
			res = stmt1.executeQuery(select);
			int i =0;
			while(res.next())
			    {
				List <String> selectInf = new ArrayList();
				
				selectInf.add(res.getString(1));
			
				Data.put(i,selectInf);
				i++;

			    }
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }
	
	/**
	 *  Возвращает список устройств для заданного шаблона
	 * @param data
	 */
	private void GetDeviceFromTemplate(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT DeviceName FROM devices INNER JOIN selectdata INNER JOIN template_name "
				+ " WHERE templateName = '"+data.get("template")+"' && selectdata.deviceID = devices.deviceID ";
			res = stmt1.executeQuery(select);
			int i =0;
			while(res.next())
			    {
				List <String> selectInf = new ArrayList();
				
				selectInf.add(res.getString(1));
			
				Data.put(i,selectInf);
				i++;

			    }
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }

	/**
	 * Возвращает список всех групп устройств
	 */
	private void GetGroupList()
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT groupName FROM group_name";
			res = stmt1.executeQuery(select);
			int i =0;
			while(res.next())
			    {
				List <String> selectInf = new ArrayList();
				
				selectInf.add(res.getString(1));
			
				Data.put(i,selectInf);
				i++;

			    }
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }
	
	/**
	 * Возвращает список групп для заданного шаблона
	 * @param data
	 */
	private void GetGroupFromTemplate(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT groupName FROM group_name INNER JOIN selectdata INNER JOIN template_name "
				+ " WHERE templateName = '"+data.get("template")+"' && selectdata.groupID = group_name.groupID "
					+ "&& selectdata.templateID = template_name.templateID";
			res = stmt1.executeQuery(select);
			int i =0;
			while(res.next())
			    {
				List <String> selectInf = new ArrayList();
				
				selectInf.add(res.getString(1));
			
				Data.put(i,selectInf);
				i++;

			    }
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }

    }
