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
import NetMonitor.main;

public class GetDeviceName
    {
	public void GetDeviceName(String device, String group) throws SQLException, IOException
	    {
		List <String> devname = new ArrayList();
		
		connect con = new connect();
		Connection connect = con.connectdb("monitor_db");
		Statement stmt = connect.createStatement();
		
	        String Group = "SELECT DeviceName FROM devices WHERE group = '"+group+"'";
	        ResultSet res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	devname.add(res.getString(1));
	            }

	        Dump(devname);

	        connect.close();
	    }
	
	public void Dump(List <String> devname)
	    {
		ServerHandler srv = new ServerHandler();
		
		Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
		
		data.put(1, devname);
		
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));
	    }
    }
