package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import NetMonitor.main;
import SNMP.Get;
import SNMP.GetNext;
import SNMP.Walk;

public class Universal
    {
	private Connection connection2;
	private Connection connection1;
	private PreparedStatement preparedStatement = null;
	private List<String> Value;
	private List<String> index;
	private String ins;
	private String key;
	private String metric;
	private String oidbase;
	private int history;
	
	public void Universal(String deviceID, int timeout) throws SQLException, IOException
	    {
		connection2 = main.getConnect2();
		connection1 = main.getConnect1();
		Statement stmt1 = connection1.createStatement();
		Statement stmt3 = connection1.createStatement();
		Statement stmt2 = connection2.createStatement();
		ResultSet res2;
		Walk w = new Walk();
		String oid;
		String sel;
		String oidValue;
		int n = 0;


		sel="SELECT IPaddress, Community, groupID, Port FROM devices WHERE deviceID ='"+deviceID+"'";			
		ResultSet res = stmt1.executeQuery(sel);
	        res.next(); 
	        String IPaddress = res.getString(1);
	        String community = res.getString(2);
	        String group = res.getString(3);
	        String port = res.getString(4);


                String IP = "udp:"+IPaddress+"/"+port;
                
                //SELECT TEMPLATE DATA (OID)
		String sel2 = "SELECT OIDname FROM templates JOIN selectdata ON deviceID='"+deviceID+"' && timeout = '"+timeout+"' WHERE OIDname!=''";
		res = stmt1.executeQuery(sel2);
	        while(res.next())
	            {
	        	oid = res.getString(1);
	        	
	        	//Select data for insert in DATA table
	        	sel="SELECT name, metric, OIDbase,history FROM templates WHERE OIDname='"+oid+"'";
		        res2 = stmt3.executeQuery(sel);
		        res2.next(); 
		        key = res2.getString(1);
		        metric = res2.getString(2);
		        oidbase = res2.getString(3);
		        history = res2.getInt(4);
		        
		        //Select oid number for request device
			sel="SELECT oid FROM "+oidbase+" WHERE object ='"+oid+"'";			
		        res2 = stmt2.executeQuery(sel);
		        res2.next(); oidValue = res2.getString(1);
		        
		        //Get data from OID
		        w.walk(IP, oidValue, community);
	        	Value = w.getAllValue();
	        	index = w.getIndex();
	        	if (Value ==null) break;
	        	
		        System.out.println("Value get from "+oid);
		        System.out.println(Value);
		        System.out.println(index);
		        //Delete data from DB
		        ins = "DELETE  FROM group_"+group+"_data WHERE name = '"+key+"'";
		    //    stmt3.executeUpdate(ins);
	        	//Insert data to DB
		        n=0;
	        	while(n<Value.size())
	        	    {

	        		 //Insert in group Table
	        		 ins = "INSERT INTO group_"+group+"_data (deviceID, data, OIDindex, name, metric) VALUES "
	        		 	+ "('"+deviceID+"','"+Value.get(n)+"', '"+index.get(n)+"','"+key+"', '"+metric+"')";
	        		 stmt3.executeUpdate(ins);
	        		
		        		 n++;
	        	    }

	            }
              
		
	    }
    }
