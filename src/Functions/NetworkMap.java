package Functions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import NetMonitor.main;
import SNMP.Walk;

/**
 * @author Alex
 * Class generate network devices graph
 */
public class NetworkMap
    {
	private Connection connection1 = main.getConnect1();
	private Connection connection2 = main.getConnect2();
	private String select;
	private Statement stmt1;
	private Statement stmt2;
	private Statement stmt3;
	private ResultSet res;
	private ResultSet res2;
	private ResultSet res3;
	private List<String> devName = new ArrayList<String>();
	private List<String> PortID = new ArrayList<String>();
	private List<String> sysName = new ArrayList<String>();
	private String deviceID;
	private String DeviceName;
	private List  nodesList = new ArrayList();
	private List  edgesList = new ArrayList();

	
	public void NetMapTable() throws SQLException, IOException
	    {
		connection1 = main.getConnect1();
		stmt1 = connection1.createStatement();//Alter table

		connection2 = main.getConnect2();
		stmt2 = connection1.createStatement();//Select from table
		
		devName.clear();//Clear Device Name list
		select = "SELECT devices FROM netmaptable";//Get devices currently in table
		res = stmt2.executeQuery(select);
		while(res.next())
		    {
			devName.add(res.getString(1));
		    }
		
		select = "SELECT DeviceName FROM devices"; //Creating column in table with all device names
		res = stmt2.executeQuery(select);
		while(res.next())
		    {
			if(!devName.contains(res.getString(1)))
			    {

				select = "ALTER TABLE netmaptable ADD `"+res.getString(1)+"` VARCHAR(45) DEFAULT '0'";
				stmt1.executeUpdate(select);

				select = "INSERT INTO netmaptable (devices) VALUES ('"+res.getString(1)+"')";
				stmt1.executeUpdate(select);
				devName.add(res.getString(1));
				
			    }
		    }
	    }
	
	public void NetworkMap() throws SQLException, IOException
	    {
		
		stmt1 = connection1.createStatement();//Select from table
		stmt3 = connection1.createStatement();//Select from table
		stmt2 = connection2.createStatement();//Select from table
		
		Walk walk = new Walk();//SNMP walker
		
		select = "SELECT oid  FROM public_oid WHERE object = 'lldpRemPortId'";
		res = stmt2.executeQuery(select);
		res.next();
		String lldpPortID = res.getString(1);

		select = "SELECT oid  FROM public_oid WHERE object = 'lldpRemSysName'";
		res = stmt2.executeQuery(select);
		res.next();
		String SysName = res.getString(1);
		
		select="SELECT IPaddress, Community, groupID, Port, DeviceName FROM devices  ";			
		res = stmt3.executeQuery(select);
		while(res.next())
		    {
	        	String IPaddress = res.getString(1);
	        	String community = res.getString(2);
	        	String group = res.getString(3);
	        	String port = res.getString(4);
	        	String DeviceName = res.getString(5);
		
	        	String IP = "udp:"+IPaddress+"/"+port;
	        	
		        
		        walk.walk(IP, lldpPortID, community);//Get LLDP Port ID on interface
		        PortID.addAll(walk.getAllValue());

		        walk.walk(IP, SysName, community);//Get LLDP device Name on interface
		        sysName.addAll(walk.getAllValue());

		        int n=0;
		        while(!sysName.isEmpty())
		            {
		        	if(sysName.get(n) == null) break;
		        	if(devName.contains(sysName.get(n)))
		        	    {

		        		select = "UPDATE netmaptable SET `"+DeviceName+"` = '"+PortID.get(n)+"' WHERE devices = '"+sysName.get(n)+"'";
		        		stmt1.executeUpdate(select);
		        	    }else
		        		{
		        		    if(!sysName.get(n).equals(" ") || !sysName.get(n).equals(null))
		        			{
		        			    
		        			    select = "INSERT INTO netmaptable (devices) VALUES ('"+sysName.get(n)+"')";
		        			    stmt1.executeUpdate(select);
						
		        			    devName.add(sysName.get(n));
						
		        			    select = "UPDATE netmaptable SET `"+DeviceName+"` = '"+PortID.get(n)+"' WHERE devices = '"+sysName.get(n)+"'";
		        			    stmt1.executeUpdate(select);
		        			}
		        		}
		        	n++;
		            }
		        PortID.clear();
		        sysName.clear();
		    }
	    }
	
	public void NetMapDump() throws SQLException
	    {
		JSONObject nodes = new JSONObject();
		JSONObject edges = new JSONObject();
		JSONObject resultJson = new JSONObject();

		
		stmt1 = connection1.createStatement();//Select from table
		stmt2 = connection1.createStatement();//Select from table
		stmt3 = connection1.createStatement();//Select from table
		
		devName.clear();
		int n = 1;
		select="SELECT deviceID, DeviceName FROM devices  ";			
		res = stmt1.executeQuery(select);
		while(res.next())
		    {
			deviceID = res.getString(1);
			DeviceName = res.getString(2);

			if(!devName.contains(deviceID))
			    {
				nodes.put("id", deviceID);
				nodes.put("label", DeviceName);
				nodes.put("image", "img/switch.jpg");
				nodes.put("shape", "image");
				
				nodesList.add(nodes.clone());
			    }

			
				select = "SELECT `"+DeviceName+"`, devices FROM netmaptable WHERE `"+DeviceName+"` != '0'";
				res2 = stmt2.executeQuery(select);
				while(res2.next())
				    {
					String label = res2.getString(1);
					String remDevice = res2.getString(2);
					
					
					select = "SELECT deviceID FROM devices WHERE DeviceName = '"+remDevice+"'";
					res3 = stmt3.executeQuery(select);
					res3.next();
					String remID;
					if(res3.first() == false) 
					    {
						remID = "00"+n; 
					    	n++;
					    } else
					{remID = res3.getString(1);}

					
					if(!devName.contains(remID))
					    {
						nodes.put("id", remID);
						nodes.put("label", remDevice);
						nodes.put("image", "img/switch.jpg");
						nodes.put("shape", "image");

						nodesList.add(nodes.clone());
						devName.add(remID);
						
						edges.put("from", deviceID);
						edges.put("to", remID);
						edges.put("label", label);
						
						edgesList.add(edges.clone());

					   }
					


				    }

				devName.add(deviceID);
		    }
		resultJson.put("edges", edgesList);
		resultJson.put("nodes", nodesList);
		
		PrintWriter writer;
		try
		    {
			writer = new PrintWriter("G:/temp/NetMonitor/WebGui/maps/networkmap.json");
			writer.println(resultJson);

			writer.close();
		    } catch (FileNotFoundException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

	    }
    }
