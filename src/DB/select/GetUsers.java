/**
 * 
 */
package DB.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import NetMonitor.main;

/**
 * @author Alex
 *
 */
public class GetUsers
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	
	public void GetUsers(Map<String, String> data)
	    {
		switch(data.get("subFunc"))
		{
		    case "auth":
			Authentication(data.get("uName"));
			break;			
		}
	    }
	
	/**
	 * Получение пароля и роли пользователя
	 * @param uName - имя пользователя
	 */
	private void Authentication(String uName)
	    {
		connection1 = main.getConnect1();
		
		try
		    {
			stmt1 = connection1.createStatement();
			
			select = "SELECT password, roleName FROM users Inner join user_roles on users.roleID = user_roles.roleID "
				+ "WHERE username='"+uName+"'";
			res = stmt1.executeQuery(select);
			int i =0;
			while(res.next())
			    {
				List <String> selectInf = new ArrayList();
				
				selectInf.add(res.getString(1));
				selectInf.add(res.getString(2));
				
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
