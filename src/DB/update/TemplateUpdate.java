package DB.update;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import NetMonitor.main;

public class TemplateUpdate
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	private List<String> result = new ArrayList<>();
	
	public void TemplateUpdate(Map<String, String> data)
	    {
		switch(data.get("update"))
		{
		    case "insertElement":
			InsertTemplateElement(data);
			break;
			
		    case "editElement":
			EditTemplateElement(data);
			break;
			
		    case "insertTemplate":
			InsertTemplate(data);
			break;
			
		    case "editTemplate":
			EditTemplate(data);
			break;	
			
		}
	    }
	

	private void InsertTemplateElement(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
				select = "SELECT templateID FROM template_name WHERE templateName = '"+data.get("templName")+"'";
				res = stmt1.executeQuery(select);
				res.next();
			
				if(data.get("function").equals("SNMP"))				    
					select = "INSERT INTO templates (templateID, OIDname, name, OIDbase, functions, metric, timeout, history) VALUES "
					+ "('"+res.getString(1)+"', '"+data.get("key")+"', '"+data.get("name")+"', '"+data.get("OIDbase")+"', '', '"+data.get("metric")+"', "
						+ " '"+data.get("timeout")+"', '"+data.get("history")+"')";				
				else
					select = "INSERT INTO templates (templateID, OIDname, name, OIDbase, functions, metric, timeout, history) VALUES "
					+ "('"+res.getString(1)+"', '', '"+data.get("name")+"', '', '"+data.get("function")+"', '"+data.get("metric")+"', "
						+ " '"+data.get("timeout")+"', '"+data.get("history")+"')";	
				stmt1.executeUpdate(select);
				result.add("OK");
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
	
	private void EditTemplateElement(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
				select = "SELECT templateID FROM template_name WHERE templateName = '"+data.get("templName")+"'";
				res = stmt1.executeQuery(select);
				res.next();
			
				if(data.get("function").equals("SNMP"))				    
					select = "INSERT INTO templates (templateID, OIDname, name, OIDbase, functions, metric, timeout, history) VALUES "
					+ "('"+res.getString(1)+"', '"+data.get("key")+"', '"+data.get("name")+"', '"+data.get("OIDbase")+"', '', '"+data.get("metric")+"', "
						+ " '"+data.get("timeout")+"', '"+data.get("history")+"')";				
				else
					select = "INSERT INTO templates (templateID, OIDname, name, OIDbase, functions, metric, timeout, history) VALUES "
					+ "('"+res.getString(1)+"', '', '"+data.get("name")+"', '', '"+data.get("function")+"', '"+data.get("metric")+"', "
						+ " '"+data.get("timeout")+"', '"+data.get("history")+"')";	
				stmt1.executeUpdate(select);
				result.add("OK");
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
	
	private void InsertTemplate(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT templateName FROM template_name WHERE templateName = '"+data.get("name")+"'";
			res = stmt1.executeQuery(select);
			if(res.next() == false)
			    {
				select = "INSERT INTO template_name (templateName) VALUES ('"+data.get("name")+"')";
				System.out.println(select);
				System.out.println(connection1);
				stmt1.executeUpdate(select);
				result.add("OK");
				Data.put(0, result);
			    }
			else
			    {
				result.add("Шаблон с таким именем уже существует");
				Data.put(0, result);
			    }
			    
			
			
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
	
	private void EditTemplate(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
				select = "UPDATE template_name SET templateName = '"+data.get("name")+"' WHERE templateName = '"+data.get("oldName")+"'";
				stmt1.executeUpdate(select);
				result.add("OK");
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
