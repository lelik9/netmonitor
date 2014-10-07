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

public class GetConfInfo
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	
	public void GetConfInfo(Map<String, String> data)
	    {
		switch(data.get("confFunc"))
		{
		    case "devInfo":
			DeviceInfo();
			break;
			
		    case "groupInfo":
			GroupInfo();
			break;
			
			
		}
	    }
	
	
	//Получаем все группы устройств и дейвасы которые в них входят
	private void DeviceInfo()
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT DeviceName, IPaddress, Community, port, groupName FROM devices Inner join group_name on devices.GroupID = group_name.groupID";
			res = stmt1.executeQuery(select);
			int i =0;
			while(res.next())
			    {
				List <String> selectInf = new ArrayList();
				
				selectInf.add(res.getString(1));
				selectInf.add(res.getString(2));
				selectInf.add(res.getString(3));
				selectInf.add(res.getString(4));
				selectInf.add(res.getString(5));
				
				Data.put(i,selectInf);
				i++;

			    }
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		Dump();

	    }
	
	private void GroupInfo()
	    {
		connection1 = main.getConnect1();
		List <String> selectName = new ArrayList();
		List <String> selectId = new ArrayList();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT groupName, groupID FROM group_name";
			res = stmt1.executeQuery(select);

			while(res.next())
			    {				
				selectName.add(res.getString(1));
				selectId.add(res.getString(2));
				
			    }
			Data.put(0,selectName);
			Data.put(1,selectId);
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
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
