package Functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import DB.connect;
import NetMonitor.main;
import server.ServerHandler;

public class ReadIntInfo
    {
	
	public void Dump(List <String> ifDescr, List <String> ifMtu, List <String> ifSpeed, List <String> ifLastChange, List <String> ifInUcastPkts,
		List <String> ifInNUcastPkts, List <String>   ifInMulticastPkts, List <String> ifInBroadcastPkts, List <String> ifInDiscards, 
		List <String> ifInErrors, List <String> ifOutUcastPkts, List <String> ifOutNUcastPkts, List <String>   ifOutMulticastPkts, 
		List <String> ifOutBroadcastPkts, List <String> ifOutDiscards, List <String> ifOutErrors) 
	    {
		ServerHandler srv = new ServerHandler();
		Map<String, List <String>> data = new HashMap<String, List <String>>();
		data.put("Descr", ifDescr);
		data.put("Mtu", ifMtu);
		data.put("Speed", ifSpeed);
		data.put("LastChange", ifLastChange);
		data.put("InUcastPkts", ifInUcastPkts);
		data.put("InNUcastPkts", ifInNUcastPkts);
		data.put("InMulticastPkts",ifInMulticastPkts);
		data.put("InBroadcastPkts", ifInBroadcastPkts);
		data.put("InDiscards", ifInDiscards);
		data.put("InErrors", ifInErrors);
		data.put("OutUcastPkts", ifOutUcastPkts);
		data.put("OutNUcastPkts", ifOutNUcastPkts);
		data.put("OutMulticastPkts",ifOutMulticastPkts);
		data.put("OutBroadcastPkts", ifOutBroadcastPkts);
		data.put("OutDiscards", ifOutDiscards);
		data.put("OutErrors", ifOutErrors);

		Yaml yaml = new Yaml();
		srv.setDump(yaml.dump(data));

	}
	
	public void ReadIntInfo(String device) throws SQLException
	    {
	        main m = new main();
	        
	        Connection connect = m.getConnect1();
	        Statement stmt1 = connect.createStatement();
	        
	        String Group = "SELECT * FROM intinfo WHERE DeviceName = '"+device+"'";
	        ResultSet res = stmt1.executeQuery(Group);
	        res.next(); String group = res.getString(1);
		
	    }
    }
