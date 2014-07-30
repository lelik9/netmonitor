package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

import NetMonitor.main;
import SNMP.Walk;

public class Calculate
    {
	private  String OIDindex;
	private Connection connection1 = main.getConnect1();
	private Connection connection2 = main.getConnect2();
	private String select;
	private Statement stmt1;
	private Statement stmt2;
	private ResultSet res;
	private List<String> Value;
	private double Utilization;
	
	public void InSpeedCalc(String deviceID, int timeout) throws SQLException, IOException, InterruptedException
	    {
		double firstOctets;
		double secondOctets;
		double speed;
		
		Walk walk = new Walk();//SNMP walker
		connection1 = main.getConnect1();
		connection2 = main.getConnect2();
		stmt1 = connection1.createStatement();
		stmt2 = connection2.createStatement();
		
		select = "SELECT oid  FROM public_oid WHERE object = 'ifSpeed'";
		res = stmt2.executeQuery(select);
		res.next(); 
		String ifSpeed = res.getString(1);
		select = "SELECT oid  FROM public_oid WHERE object = 'ifInOctets'";
		res = stmt2.executeQuery(select);
		res.next(); 
		String ifInOctets = res.getString(1);
		
		select="SELECT IPaddress, Community, groupID, Port FROM devices WHERE deviceID ='"+deviceID+"'";			
		ResultSet res = stmt1.executeQuery(select);
	        res.next(); 
	        String IPaddress = res.getString(1);
	        String community = res.getString(2);
	        String group = res.getString(3);
	        String port = res.getString(4);
		
	        String IP = "udp:"+IPaddress+"/"+port;

		select = "SELECT OIDindex FROM group_"+group+"_data WHERE data = 'GigabitEthernet0/1'";
		res = stmt1.executeQuery(select);
		res.next();
		OIDindex = res.getString(1);
	        walk.walk(IP, ifSpeed+"."+OIDindex, community);//Get interface speed
	        Value = walk.getAllValue();
	        speed = Double.parseDouble(Value.get(0));

		        walk.walk(IP, ifInOctets+"."+OIDindex, community);//Get IN Octets on interface
		        Value = walk.getAllValue();
		        firstOctets = Double.parseDouble(Value.get(0));
		        
		        TimeUnit.SECONDS.sleep(timeout);

		        walk.walk(IP, ifInOctets+"."+OIDindex, community);//Get IN Octets on interface
		        Value = walk.getAllValue();
		        secondOctets = Double.parseDouble(Value.get(0));



	        //CALCULATE UTILIZATION ON INTERFACE
	        if(secondOctets > firstOctets)
	            {
	        	Utilization = ((secondOctets - firstOctets )*8*100)/(timeout*speed);
	        	Utilization = (Utilization*(speed/1000000))/100;
	        	System.out.println(Utilization);
	            }
	        
	        
		
	    }
    }
