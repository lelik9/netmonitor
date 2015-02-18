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

public class GetSNMP
    {
	private Connection connection1;
	private String select;
	private ResultSet res;
	private Map<Integer, List <String>> Data = new HashMap<Integer, List <String>>();
	
	public void GetSNMP(Map<String, String> data)
	    {
		switch(data.get("subFunc"))
		{
		    case "tablesName":
			TablesName();
			break;
			
		    case "OIDs":
			OID(data);
			break;	
			
		    case "getFunctions":
			Functions(data);
			break;	
			
		}
	    }
	
	private void TablesName()
	    {
		connection1 = main.getConnect2();
		
		try
		    {
			Statement stmt1 = connection1.createStatement();
			
			select = "show tables from mib_db";
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
	
	private void OID(Map<String, String> data)
	    {
		connection1 = main.getConnect2();
		
		try
		    {
			Statement stmt1 = connection1.createStatement();
			
			select = "SELECT object, description FROM "+data.get("tableName")+"";
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
	
	private void Functions(Map<String, String> data)
	    {
		connection1 = main.getConnect2();
		
		try
		    {
			Statement stmt1 = connection1.createStatement();
			
			select = "SELECT object, description FROM functions";
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
