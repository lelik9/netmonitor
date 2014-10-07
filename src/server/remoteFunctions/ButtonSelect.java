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

public class ButtonSelect
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	
	public void ButtonSelect(String windowName) throws SQLException
	    {
		List <String> buttonName = new ArrayList();
		List <String> tabName = new ArrayList();
		
		connection1 = main.getConnect1();
		stmt1 = connection1.createStatement();

		select = "SELECT buttonsName,tabName FROM gui_buttons WHERE windowName = '"+windowName+"'";
		res = stmt1.executeQuery(select);
		while(res.next())
		    {
			buttonName.add(res.getString(1));
			tabName.add(res.getString(2));
		    }
		
		Dump(buttonName, tabName);
	    }
	
	public void Dump(List <String> buttonName, List <String> tabName)
	    {
		ServerHandler srv = new ServerHandler();
		
		Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
		
		data.put(0, buttonName);
		data.put(1, tabName);

		
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));
	    }
    }
