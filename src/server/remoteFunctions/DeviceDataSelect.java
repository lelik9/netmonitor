package server.remoteFunctions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import server.ServerHandler;
import NetMonitor.main;

public class DeviceDataSelect
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private List <String> oidIndex = new ArrayList();
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	
	public void DeviceDataSelect(Map<String, String> data) throws SQLException
	    {
		String dataKeys = "";
		String key = null;
		String devName;
		String devId;
		String groupId;
		
		Yaml yaml = new Yaml();
		connection1 = main.getConnect1();
		stmt1 = connection1.createStatement();
		
		devName = data.get("devName");
		select = "SELECT deviceID, GroupID FROM devices WHERE DeviceName='"+devName+"'";
		res = stmt1.executeQuery(select);
		res.next(); 
		devId = res.getString(1);
		groupId = res.getString(2);
		
		//Получаем OID индексы, для формирования соответствий параметров 
		select = "SELECT OIDindex from group_"+groupId+"_data WHERE deviceID='"+devId+"' GROUP BY OIDindex";
		res = stmt1.executeQuery(select);
		while(res.next())
		    {
			oidIndex.add(res.getString(1));

		    }

		//Для каждого OID индекса дергаем значения из базы и формируем строку для таблицы
		int i = 0;
		while(oidIndex.size()>i)
		    {
			List <String> selectInf = new ArrayList();
			
			int n = 0;
			while(data.size()>n)
			    {
				if(data.get(""+n+"") != null)
				    {
					select = "SELECT data from group_"+groupId+"_data WHERE name='"+data.get(""+n+"")+"' && OIDindex='"+oidIndex.get(i)+"' && deviceID='"+devId+"'";
					res = stmt1.executeQuery(select);
					while(res.next())
					    {
						if(res.getString(1) == null) {
						    selectInf.add("");
						}else
						    {
							selectInf.add(res.getString(1));
						    }

					    }
										
				    }
				n++;
			    }
			Data.put(i,selectInf);

			i++;
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
