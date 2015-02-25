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
	
	/**
	 * 
	 * @param data
	 */
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
			
		    case "delElement":
			DeleteElement(data);
			break;
			
		    case "insertTemplate":
			InsertTemplate(data);
			break;
			
		    case "editTemplate":
			EditTemplate(data);
			break;	
			
		    case "delTemplate":
			DeleteTemplate(data);
			break;
			
		}
	    }
	




	/**
	 * 
	 * @param data
	 */
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
				result.add("Элемент добавлен");
				result.add("Сообщение");
				Data.put(0, result);
			    
			
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			result.add("FAIL");
			result.add("Ошибка");
			Data.put(0, result);
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }
	
	/**
	 * 
	 * @param data
	 */
	private void EditTemplateElement(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
				select = "SELECT templateID FROM template_name WHERE templateName = '"+data.get("templName")+"'";
				res = stmt1.executeQuery(select);
				res.next();
			
			    
				select = "UPDATE templates SET name = '"+data.get("name")+"',  metric = '"+data.get("metric")+"', "
					+ "timeout = '"+data.get("timeout")+"', history = '"+data.get("history")+"' "
						+ "WHERE templateID = '"+res.getString(1)+"' && OIDname = '"+data.get("key") +"'";				

				stmt1.executeUpdate(select);
				result.add("Элемент изменен");
				result.add("Сообщение");
				Data.put(0, result);
			    
			
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			result.add("Не удалось внести изменения");
			result.add("Ошибка");
			Data.put(0, result);
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
		
	    }
	
	/**
	 * Добавление нового шаблона
	 * @param data - ключ "name", значение - название шаблона 
	 */
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
				result.add("Шаблон добавлен");
				result.add("Сообщение");
				Data.put(0, result);
			    }
			else
			    {
				result.add("Шаблон с таким именем уже существует");
				result.add("Ошибка");
				Data.put(0, result);
			    }
			    
			
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			result.add(e.toString());
			result.add("Ошибка");
			Data.put(0, result);
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }
	
	/**
	 * Редактирование шаблона
	 * @param data
	 */
	private void EditTemplate(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			if(!data.get("name").equals(data.get("oldName")))
			    {
				stmt1 = connection1.createStatement();
				select = "UPDATE template_name SET templateName = '"+data.get("name")+"' "
					+ "WHERE templateName = '"+data.get("oldName")+"'";
				stmt1.executeUpdate(select);
			    }
			
			select = "SELECT templateID FROM template_name WHERE templateName = '"+data.get("name")+"'";
			res = stmt1.executeQuery(select);
			res.next();
			String templID = res.getString(1);
			
			select = "DELETE FROM selectdata WHERE templateID = '"+templID+"'";
			stmt1.executeUpdate(select);
			
			int n = 0;
			while(data.containsKey("device"+n))
			    {
				select = "SELECT deviceID FROM devices WHERE DeviceName = '"+data.get("device"+n)+"'";
				res = stmt1.executeQuery(select);
				res.next();
				String devID = res.getString(1);
				
				select = "INSERT INTO selectdata (deviceID, templateID) VALUE('"+devID+"','"+templID+"')";
				stmt1.executeUpdate(select);
				n++;
			    }
			n = 0;
			while(data.containsKey("group"+n))
			    {
				select = "SELECT groupID FROM group_name WHERE groupName = '"+data.get("group"+n)+"'";
				res = stmt1.executeQuery(select);
				res.next();
				String groupID = res.getString(1);
				
				select = "INSERT INTO selectdata (groupID, templateID) VALUE('"+groupID+"','"+templID+"')";
				stmt1.executeUpdate(select);
				n++;
			    }
			
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

	/**
	 * Удаление шаблона
	 * @param data
	 */
	private void DeleteTemplate(Map<String, String> data)
	    {
		
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT deviceID, groupID FROM selectdata INNER JOIN template_name "
				+ "WHERE templateName = '"+data.get("name")+"' && selectdata.templateID = template_name.templateID";
			res = stmt1.executeQuery(select);
			if(res.next() == false)
			    {
				select = "SELECT templateID FROM template_name WHERE templateName = '"+data.get("name")+"'";
				res = stmt1.executeQuery(select);
				res.next();
				
				select = "DELETE FROM templates WHERE templateID = '"+res.getString(1)+"'";
				stmt1.executeUpdate(select);
				
				select = "DELETE FROM template_name WHERE templateName = '"+data.get("name")+"'";
				stmt1.executeUpdate(select);
				result.add("Шаблон удален");
				result.add("Сообщение");
				Data.put(0, result);
			    }
			else
			    {
				result.add("К шаблону привязанны устройства. Удалите их из шаблона и повторите попытку.");
				result.add("Ошибка");
				Data.put(0, result);
			    }
			    
			
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			result.add("Не удалось удалить шаблон");
			result.add("Ошибка");
			Data.put(0, result);
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
	    }
	
	/**
	 * Удаление элемента
	 * @param data
	 */
	private void DeleteElement(Map<String, String> data)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT templateID FROM template_name WHERE templateName = '"+data.get("templName")+"'";
			res = stmt1.executeQuery(select);
			res.next();
				
			select = "DELETE FROM templates WHERE templateID = '"+res.getString(1)+"' && name = '"+data.get("name")+"'";
			stmt1.executeUpdate(select);
				
			result.add("Элемент удален");
			result.add("Сообщение");
			Data.put(0, result);
			    			    		
			
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			result.add("Не удалось удалить элемент");
			result.add("Ошибка");
			Data.put(0, result);
			e.printStackTrace();
		    }
		server.Dump dump = new server.Dump();
		dump.Dump(Data);
		
	    }
    }
