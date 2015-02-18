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

import server.ServerHandler;
import NetMonitor.main;
/**
 * ��������� ����� ���������
 * @author Alex
 *
 */
public class DevGroupUpdate
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	private List<String> result = new ArrayList<>();
	/**
	 * � data � ������ "update" ���������� �������� � ������� (insert - ����������; update - ���������� ����������; delete - �������� ������)
	 * ���� "name" �������� ��� ������
	 * @param data
	 */
	public void DevGroupUpdate(Map<String, String> data)
	    {
		switch(data.get("update"))
		{
		    case "insert":
			Insert(data.get("name"));
			break;
			
		    case "update":
			Update(data);
			break;
			
		    case "delete":
			Delete(data.get("name"));
			break;
			
		}
	    }
	/**
	 * ���������� ��� ������ ��� ����������
	 * @param groupName
	 */
	private void Insert(String groupName)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "INSERT INTO group_name (groupName) Values ('"+groupName+"')";
			stmt1.executeUpdate(select);
			result.add("����� ������ ����������");
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
	 * � data2 �������� ����� � ������ ��� ������
	 * ���� new - ����� ���; old - ������.
	 * @param data2
	 */
	private void Update(Map<String, String> data2)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "UPDATE group_name SET groupName ='"+data2.get("new")+"' WHERE groupName ='"+data2.get("old")+"'";
			stmt1.executeUpdate(select);
			result.add("��� ������ ��������");
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
	 * ���������� ��� ������ ��� ��������
	 * @param groupName
	 */
	private void Delete(String groupName)
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "select deviceName from group_name inner join devices where group_name.groupName='"+groupName+"' && group_name.groupID=devices.GroupID limit 1";
			res = stmt1.executeQuery(select);
			if(res.next() == true)
			    {
				result.add("���������� �������. ������ ������ ������������.");
				Data.put(0, result);
			    }else{
				
				select = "DELETE FROM group_name WHERE groupName = '"+groupName+"'";
				stmt1.executeUpdate(select);
				result.add("������ �������");
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
	

    }
