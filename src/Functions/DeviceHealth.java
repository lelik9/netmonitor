package src.Functions;

import SNMP.Get;
import SNMP.GetNext;
import src.threads.Connection;
import src.threads.IOException;
import src.threads.ResultSet;
import src.threads.SQLException;
import src.threads.Statement;
import src.threads.String;

public class DeviceHealth
    {
	private Connection connection1;
	private Connection connection2;
	private ResultSet res;
	private String device;

	
	public void DeviceHealth()
	    {
		Health();
	    }
	
	private synchronized void Health() throws SQLException, IOException
	    {
	        GetNext n = new GetNext();
	        Get g = new Get();
	        Date date = new Date();
	        
	        String info;
	        
	        //Reading OID from base
	        preparedStatement = connection2.prepareStatement("SELECT oid FROM ? WHERE object = ?");
	        
	        preparedStatement.setString(1, "public_oid");
	        
	        preparedStatement.setString(2, "sysUpTimeInstance");
	        ResultSet res = preparedStatement.executeQuery();
	        res.next(); String sysUpTimeInstance = res.getString(1);
	        
	        preparedStatement.setString(1, "cisco_oid");
	        
	        preparedStatement.setString(2, "ciscoMemoryPoolUsed");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoMemoryPoolUsed = res.getString(1);

	        preparedStatement.setString(2, "ciscoMemoryPoolFree");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoMemoryPoolFree = res.getString(1);
	        
	        preparedStatement.setString(2, "ciscoEnvMonTemperatureThreshold");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonTemperatureThreshold = res.getString(1);
	        
	        preparedStatement.setString(2, "ciscoEnvMonTemperatureStatusValue");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonTemperatureStatusValue = res.getString(1);
	        
	        preparedStatement.setString(2, "ciscoEnvMonFanState");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonFanState = res.getString(1);
	        
	        preparedStatement.setString(2, "ciscoEnvMonFanStatusDescr");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonFanStatusDescr = res.getString(1);
	        String ciscoEnvMonFanStatusDescrOID = ciscoEnvMonFanStatusDescr;
	        
	        preparedStatement.setString(2, "ciscoEnvMonSupplyState");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonSupplyState = res.getString(1);
	        
	        preparedStatement.setString(2, "ciscoEnvMonSupplyStatusDescr");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonSupplyStatusDescr = res.getString(1);
	        String ciscoEnvMonSupplyStatusDescrOID = ciscoEnvMonSupplyStatusDescr;
	        
	        //Reading devices name
	        Statement stmt = connection1.createStatement();
	        
	        String Select = "SELECT DeviceName FROM devices WHERE Groups = 'network'";	        
		res = stmt.executeQuery(Select);

	        while(res.next())
	            {
	        	device = res.getString(1);
	        	
		        //Reading device IP from main base
		        String sel = "SELECT IPaddress FROM devices WHERE DeviceName='"+device+"'";
		        res = stmt1.executeQuery(sel);
		        res.next(); String IP = res.getString(1);
		        
			sel = "SELECT Community FROM Devices WHERE DeviceName = '"+device+"'";
			res = stmt1.executeQuery(sel);
			res.next(); String community = res.getString(1);
			
			sel = "SELECT Port FROM Devices WHERE DeviceName = '"+device+"'";
			res = stmt1.executeQuery(sel);
			res.next(); String port = res.getString(1);

		        IP = "udp:"+IP+"/"+port; //Set address of device (CHANGE PORT)
		        
	        	g.get(IP,sysUpTimeInstance); //Get UP time of device
	        	String upTime = g.getGetChar();
	        	
	        	g.get(IP,ciscoMemoryPoolUsed); //Get using memory
	        	int memUsage = Integer.parseIn(g.getGetChar());
	        	
	        	g.get(IP,ciscoMemoryPoolFree); //Get free memory
	        	int memFree = Integer.parseIn(g.getGetChar());
	        	
	        	int memUsingPercent = memUsage%((memUsage+memFree)%100); //Used memory in percent
	        	
	        	n.GetNext(IP,ciscoEnvMonTemperatureThreshold,ciscoEnvMonTemperatureThreshold, community);  //Get max temperature
	        	String maxTemp = n.getChar();

	        	n.GetNext(IP,ciscoEnvMonTemperatureStatusValue,ciscoEnvMonTemperatureStatusValue, community);  //Get current temperature
	        	String Temp = n.getChar();
	        	
	        	String data = "1";
	        	while(data!=null)
	        	    {
		        	n.GetNext(IP,ciscoEnvMonFanStatusDescrOID,ciscoEnvMonFanStatusDescr, community);  //Get FAN description
		        	data = n.getChar();
		        	if(data == null) break;
		        	String descrFan = data;
	        		int a = ciscoEnvMonFanStatusDescr.length();
	                	String tmp = ciscoEnvMonFanStatusDescrOID.substring(a+1);
	                	
		        	g.get(IP,ciscoEnvMonFanState+"."+tmp); //Get Fan status 1:normal 2:warning 3:critical 4:shutdown 5:notPresent 6:notFunctioning
		        	String statusFan = g.getGetChar();
		        	 if(statusFan == "1"){ statusFan = "normal";}
		        	 if(statusFan == "2"){ statusFan = "warning";}
		        	 if(statusFan == "3"){ statusFan = "critical";}
		        	 if(statusFan == "4"){ statusFan = "shutdown";}
		        	 if(statusFan == "5"){ statusFan = "notPresent";}
		        	 if(statusFan == "6"){ statusFan = "notFunctioning";}
		        	 
			       if(statusFan != "1")
				   {
			        	info = "INSERT INTO logs VALUES ('"+date+"','"+device+"','FAN status: "+statusFan+"')";
			        	stmt.executeUpdate(info);
			            }
		        	 
			        n.GetNext(IP,ciscoEnvMonSupplyStatusDescrOID,ciscoEnvMonSupplyStatusDescr, community);  //Get PowerSupply description
			        data = n.getChar();
			        if(data == null) break;
			        String descrPower = data;
		        	int a = ciscoEnvMonSupplyStatusDescr.length();
		                String tmp = ciscoEnvMonSupplyStatusDescrOID.substring(a+1);
		                
			        g.get(IP,ciscoEnvMonSupplyState+"."+tmp); //Get PowerSupply status 1:normal 2:warning 3:critical 4:shutdown 5:notPresent 6:notFunctioning
			        String statusPower = g.getGetChar();
			         if(statusPower == "1"){ statusPower = "normal";}
			         if(statusPower == "2"){ statusPower = "warning";}
			         if(statusPower == "3"){ statusPower = "critical";}
			         if(statusPower == "4"){ statusPower = "shutdown";}
			         if(statusPower == "5"){ statusPower = "notPresent";}
			         if(statusPower == "6"){ statusPower = "notFunctioning";}    
			         
			        if(statusPower != "1")
			            {
				        info = "INSERT INTO logs VALUES ('"+date+"','"+device+"','PowerSupply status: "+statusPower+"')";
				        stmt.executeUpdate(info);
				     }
		        	
	        		
	        	    }
	        	
	        	if(memUsingPercent > 65)
	        	    {
	        		info = "INSERT INTO logs VALUES ('"+date+"','"+device+"','Used memory: "+memUsingPercent+"')";
	        		stmt.executeUpdate(info);
	        	    }
	        	
	            }

	    }
	
	public void Connect(Connection connect1, Connection connect2)
	     {
		 connection1 = connect1;
		 connection2 = connect2;
	     }
    }
