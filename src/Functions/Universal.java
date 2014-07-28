package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public void Universal(String deviceID, int time) throws SQLException, IOException
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

	        
		sel="SELECT IPaddress FROM devices WHERE deviceID ='"+deviceID+"'";			
		ResultSet res = stmt1.executeQuery(sel);
	        res.next(); String IPaddress = res.getString(1);
	        
		sel="SELECT Community FROM devices WHERE deviceID ='"+deviceID+"'";			
		res = stmt1.executeQuery(sel);
	        res.next(); String community = res.getString(1);
	        
		sel="SELECT Port FROM devices WHERE deviceID ='"+deviceID+"'";			
		res = stmt1.executeQuery(sel);
	        res.next(); String port = res.getString(1);
	        
		sel="SELECT groupID FROM devices WHERE deviceID ='"+deviceID+"'";			
		res = stmt1.executeQuery(sel);
	        res.next(); String group = res.getString(1);

                String IP = "udp:"+IPaddress+"/"+port;
                
                //SELECT TEMPLATE DATA (OID)
		String sel2 = "SELECT OIDname FROM templates JOIN selectdata ON deviceID='"+deviceID+"'";
		res = stmt1.executeQuery(sel2);
	        while(res.next())
	            {
	        	oid = res.getString(1);
	        	
	        	sel="SELECT name FROM templates WHERE OIDname='"+oid+"'";
		        res2 = stmt3.executeQuery(sel);
		        res2.next(); key = res2.getString(1);
		        
	        	sel="SELECT metric FROM templates WHERE OIDname='"+oid+"'";
		        res2 = stmt3.executeQuery(sel);
		        res2.next(); metric = res2.getString(1);
		        
	        	sel="SELECT OIDbase FROM templates WHERE OIDname='"+oid+"'";
		        res2 = stmt3.executeQuery(sel);
		        res2.next(); oidbase = res2.getString(1);
		        
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
	        	//Insert data to DB
		        n=0;
	        	while(n<Value.size())
	        	    {
	        		 Calendar calendar = Calendar.getInstance();
	        		 java.sql.Timestamp Timestamp = new java.sql.Timestamp(calendar.getTime().getTime());
	        		 ins = "INSERT INTO group_"+group+"_data (time, deviceID, data, OIDindex, name, metric) VALUES "
	        		 	+ "('"+Timestamp+"','"+deviceID+"','"+Value.get(n)+"', '"+index.get(n)+"','"+key+"', '"+metric+"')";
	        		 stmt3.executeUpdate(ins);
	        		 n++;
	        	    }

	            }
              
		
	    }
    }
