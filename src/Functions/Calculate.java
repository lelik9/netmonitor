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
	private Connection connection1;
	private Connection connection2;
	private String select;
	private Statement stmt1;
	private Statement stmt2;
	private Statement stmt3;
	private ResultSet res;
	private List<String> Value;
	private double Utilization;
	private String utilization;
	private double max = Math.pow(2,32);
	private String metric = "Mbps";
	private double firstInOctets;
	private double secondInOctets;
	private double firstOutOctets;
	private double secondOutOctets;
	private long speed;
	
	public void InSpeedCalc(String deviceID, int timeout, String function) throws SQLException, IOException, InterruptedException
	    {
		double firstOctets;
		double secondOctets;
		//long speed;

		Walk walk = new Walk();//SNMP walker
		
		connection1 = main.getConnect1();
		connection2 = main.getConnect2();
		stmt1 = connection1.createStatement();
		stmt2 = connection2.createStatement();
		stmt3 = connection1.createStatement();//Used for inserting to table;
		
		select = "SELECT oid  FROM public_oid WHERE object = 'ifSpeed'";
		res = stmt2.executeQuery(select);
		res.next(); 
		String ifSpeed = res.getString(1);
		
		select = "SELECT oid  FROM public_oid WHERE object = 'ifInOctets'";
		res = stmt2.executeQuery(select);
		res.next(); 
		String ifInOctets = res.getString(1);
		
		select = "SELECT oid  FROM public_oid WHERE object = 'ifOutOctets'";
		res = stmt2.executeQuery(select);
		res.next(); 
		String ifOutOctets = res.getString(1);
		
		select="SELECT IPaddress, Community, groupID, Port FROM devices WHERE deviceID ='"+deviceID+"'";			
		ResultSet res = stmt1.executeQuery(select);
	        res.next(); 
	        String IPaddress = res.getString(1);
	        String community = res.getString(2);
	        String group = res.getString(3);
	        String port = res.getString(4);
		
	        String IP = "udp:"+IPaddress+"/"+port;

		select = "SELECT OIDindex FROM group_"+group+"_data WHERE data = 'Unit: 1 Slot: 0 Port: 1 Gigabit - Level'";
		res = stmt1.executeQuery(select);
		res.next();
		OIDindex = res.getString(1);
		
	        walk.walk(IP, ifSpeed+"."+OIDindex, community);//Get interface speed
	        Value = walk.getAllValue();
	        speed = Long.parseLong(Value.get(0));

		        walk.walk(IP, ifInOctets+"."+OIDindex, community);//Get IN Octets on interface
		        Value = walk.getAllValue();
		        firstInOctets = Double.parseDouble(Value.get(0));
		        walk.walk(IP, ifOutOctets+"."+OIDindex, community);//Get Out Octets on interface
		        Value = walk.getAllValue();
		        firstOutOctets = Double.parseDouble(Value.get(0));
		        
		        TimeUnit.SECONDS.sleep(timeout);

		        walk.walk(IP, ifInOctets+"."+OIDindex, community);//Get IN Octets on interface
		        Value = walk.getAllValue();
		        secondInOctets = Double.parseDouble(Value.get(0));
		        walk.walk(IP, ifOutOctets+"."+OIDindex, community);//Get Out Octets on interface
		        Value = walk.getAllValue();
		        firstOutOctets = Double.parseDouble(Value.get(0));


		 switch(function)
		 {
		     case "InSpeedCalc":
			 InSpeed(timeout);
		     case "OutSpeedCalc":
			 OutSpeed(timeout);
		     case "BandwichUtil":
			 BandwichUtil(timeout);
		 }

	        
	        select = "DELETE  FROM group_"+group+"_data WHERE name = 'IN interface Speed'";
	        stmt3.executeUpdate(select);
	        select = "INSERT INTO group_"+group+"_data (deviceID, data, name, metric, OIDindex) VALUES ('"+deviceID+"', '"+utilization+"', 'IN interface Speed','" +metric+"', '"+OIDindex+"') ";
	        stmt3.executeUpdate(select);
	    }
	
	//CALCULATE UTILIZATION IN INTERFACE
	public void InSpeed(int timeout)
	    {
	        if(secondInOctets > firstInOctets)
	            {
	        	Utilization = ((secondInOctets - firstInOctets )*8*100)/(timeout*speed);
	        	Utilization = (Utilization*(speed/1000000))/100;
	        	if(Utilization < 1) {Utilization = Utilization*1000; metric = "Kbps";}
	        	utilization = String.format("%.3f%n", Utilization);

	            } else
	        	{
	        	    Utilization = ((max - secondInOctets + firstInOctets )*8*100)/(timeout*speed);
	        	    Utilization = (Utilization*(speed/1000000))/100;
	        	    if(Utilization < 1) {Utilization = Utilization*1000; metric = "Kbps";}
	        	    utilization = String.format("%.3f%n", Utilization);

	        	}
	    }
	
	//CALCULATE UTILIZATION OUT INTERFACE
	public void OutSpeed(int timeout)
	    {
	        if(secondOutOctets > firstOutOctets)
	            {
	        	Utilization = ((secondOutOctets - firstOutOctets )*8*100)/(timeout*speed);
	        	Utilization = (Utilization*(speed/1000000))/100;
	        	if(Utilization < 1) {Utilization = Utilization*1000; metric = "Kbps";}
	        	utilization = String.format("%.3f%n", Utilization);

	            } else
	        	{
	        	    Utilization = ((max - secondOutOctets + firstOutOctets )*8*100)/(timeout*speed);
	        	    Utilization = (Utilization*(speed/1000000))/100;
	        	    if(Utilization < 1) {Utilization = Utilization*1000; metric = "Kbps";}
	        	    utilization = String.format("%.3f%n", Utilization);
	        	}
	    }
	
	//CALCULATE UTILIZATION OUT INTERFACE
	public void BandwichUtil(int timeout)
	    {
	        if(secondOutOctets > firstOutOctets)
	            {
	        	Utilization = ((secondOutOctets - firstOutOctets )*8*100)/(timeout*speed);
	        	metric = "%";

	            }
	    }
	
    }
