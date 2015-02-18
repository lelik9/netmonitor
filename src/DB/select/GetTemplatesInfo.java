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

public class GetTemplatesInfo
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	
	public void GetTemplatesInfo(Map<String, String> data)
	    {
		switch(data.get("confFunc"))
		{
		    case "templateFullInfo":
			TemplateFullInfo();
			break;
			
		    case "templateInfo":
			TemplateInfo();
			break;
			
		    case "getElements":
			GetElement();
			break;
			
		}

	    }

	/**
	 * Получиние всех элементов и их групп
	 */
	private void GetElement()
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT name FROM templates GROUP BY name";
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

	private void TemplateInfo()
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT templateName FROM template_name";
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

	private void TemplateFullInfo()
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT name, OIDname, functions, metric, timeout, history, templateName FROM templates Inner join template_name on templates.templateID = template_name.templateID";
			res = stmt1.executeQuery(select);
			int i =0;
			while(res.next())
			    {
				List <String> selectInf = new ArrayList();
				
				selectInf.add(res.getString(1));
				if(res.getString(2).equals(""))
				    selectInf.add(res.getString(3));
				else selectInf.add(res.getString(2));
				selectInf.add(res.getString(4));
				selectInf.add(res.getString(5));
				selectInf.add(res.getString(6));
				if(res.getString(5).equals("0"))
				    selectInf.add("Не активно");
				else selectInf.add("Активно");				    
				selectInf.add(res.getString(7));
				
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
