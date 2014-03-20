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
import DB.Connect;

public class GetVlan
    {
	public void GetVlan(String device) throws SQLException, IOException
	    {
		List <String> vlanNum = new ArrayList();
		List <String> vlanName = new ArrayList();

		
		Connect con = new Connect();
		Connection connect = con.connectdb("monitor_db");
		Statement stmt = connect.createStatement();
		
		vlanNum.add("Vlan number");
	        String Group = "SELECT vlanNum FROM vlantable WHERE DeviceName = '"+device+"'";
	        ResultSet res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	vlanNum.add(res.getString(1));
	            }
	        
	        vlanName.add("Vlan name");
	        Group = "SELECT vlanName FROM vlantable WHERE DeviceName = '"+device+"'";
	        res = stmt.executeQuery(Group);
	        while(res.next())
	            {
	        	vlanName.add(res.getString(1));
	            }
	        

	        Dump(vlanNum, vlanName);

	        connect.close();
	    }
	
	public void Dump(List <String> vlanNum, List <String> vlanName)
	    {
		ServerHandler srv = new ServerHandler();
		
		Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
		
		data.put(0, vlanName);
		data.put(1, vlanNum);

		
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));
	    }
	    
    }
