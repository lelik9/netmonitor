package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import SNMP.Get;
import SNMP.GetNext;


public class DeviceHealth
    {
	private Connection connection1;
	private Connection connection2;
	private ResultSet res;
	private ResultSet res2;
	private String device;
	private PreparedStatement preparedStatement = null;
	
	public void DeviceHealth() throws SQLException, IOException
	    {
		Health();
	    }
	
	private synchronized void Health() throws SQLException, IOException
	    {
	        GetNext n = new GetNext();
	        n.start();
	        Get g = new Get();
	        g.start();
	        
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = new Date();
	        
	        String info;
	        
	        //Reading OID from base
	        preparedStatement = connection2.prepareStatement("SELECT oid FROM public_oid WHERE object = ?");
	        	        
	        preparedStatement.setString(1, "sysUpTimeInstance");
	        ResultSet res = preparedStatement.executeQuery();
	        res.next(); String sysUpTimeInstance = res.getString(1);
	        
	        preparedStatement = connection2.prepareStatement("SELECT oid FROM cisco_oid WHERE object = ?");
	        
	        preparedStatement.setString(1, "ciscoMemoryPoolUsed");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoMemoryPoolUsed = res.getString(1);

	        preparedStatement.setString(1, "ciscoMemoryPoolFree");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoMemoryPoolFree = res.getString(1);
	        
	        preparedStatement.setString(1, "chassisTempAlarm");
	        res = preparedStatement.executeQuery();
	        res.next(); String chassisTempAlarm = res.getString(1);
	        
	        preparedStatement.setString(1, "ciscoEnvMonTemperatureStatusValue");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonTemperatureStatusValue = res.getString(1);
	        
	        preparedStatement.setString(1, "ciscoEnvMonFanState");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonFanState = res.getString(1);
	        String ciscoEnvMonFanStateOID = ciscoEnvMonFanState;
	        
	        preparedStatement.setString(1, "ciscoEnvMonFanStatusDescr");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonFanStatusDescr = res.getString(1);
	        String ciscoEnvMonFanStatusDescrOID = ciscoEnvMonFanStatusDescr;
	        
	        preparedStatement.setString(1, "ciscoEnvMonSupplyState");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonSupplyState = res.getString(1);
	        String ciscoEnvMonSupplyStateOID = ciscoEnvMonSupplyState;
	        
	        preparedStatement.setString(1, "ciscoEnvMonSupplyStatusDescr");
	        res = preparedStatement.executeQuery();
	        res.next(); String ciscoEnvMonSupplyStatusDescr = res.getString(1);
	        String ciscoEnvMonSupplyStatusDescrOID = ciscoEnvMonSupplyStatusDescr;
	        
	        //Reading devices name
	        Statement stmt = connection1.createStatement();
	        Statement stmt2 = connection1.createStatement();
	        
	        String Select = "SELECT DeviceName FROM devices WHERE Groups = 'network'";	        
		res = stmt.executeQuery(Select);

	        while(res.next())
	            {
	        	device = res.getString(1);
		    //    System.out.println(device);
		        
		        //Reading device IP from main base
		        String sel = "SELECT IPaddress FROM devices WHERE DeviceName='"+device+"'";
		        res2 = stmt2.executeQuery(sel);
		        res2.next(); String IP = res2.getString(1);
		        
			sel = "SELECT Community FROM Devices WHERE DeviceName = '"+device+"'";
			res2 = stmt2.executeQuery(sel);
			res2.next(); String community = res2.getString(1);
			
			sel = "SELECT Port FROM Devices WHERE DeviceName = '"+device+"'";
			res2 = stmt2.executeQuery(sel);
			res2.next(); String port = res2.getString(1);

		        IP = "udp:"+IP+"/"+port; //Set address of device (CHANGE PORT)
		        		        
		        n.GetNext(IP,sysUpTimeInstance,sysUpTimeInstance, community); //Get UP time of device
	        	String upTime = n.getChar();
	        	
	        	
	        	g.get(IP,ciscoMemoryPoolUsed); //Get using memory
	        	//System.out.println(g.getGetChar());
	        	int memUsage = Integer.parseInt(g.getGetChar());
	        	
	        	g.get(IP,ciscoMemoryPoolFree); //Get free memory
	        	int memFree = Integer.parseInt(g.getGetChar());
	        	
	        	int memUsingPercent = memUsage/((memUsage+memFree)/100); //Used memory in percent
	        	
	        	n.GetNext(IP,chassisTempAlarm,chassisTempAlarm, community);  //Get max temperature
	        	String maxTemp = n.getChar();
	        	//System.out.println(maxTemp);
	        	
	        	String data = "1";
	        	while(data!=null)
	        	    {
	        		System.out.println(ciscoEnvMonFanStatusDescrOID);
		        	n.GetNext(IP,ciscoEnvMonFanStatusDescrOID,ciscoEnvMonFanStatusDescr, community);  //Get FAN description
		        	data = n.getChar();
		        	ciscoEnvMonFanStatusDescrOID = n.getNextOID();
		        	if(data == null) break;
		        	String descrFan = data;

	                	
	                	n.GetNext(IP,ciscoEnvMonFanStateOID,ciscoEnvMonFanState, community);
	                	String statusFan = n.getChar();
	                	ciscoEnvMonFanStateOID = n.getNextOID();
		        	 if(statusFan.equals("1")){ statusFan = "normal";}
		        	 if(statusFan.equals("2")){ statusFan = "warning";}
		        	 if(statusFan.equals("3")){ statusFan = "critical";}
		        	 if(statusFan.equals("4")){ statusFan = "shutdown";}
		        	 if(statusFan.equals("5")){ statusFan = "notPresent";}
		        	 if(statusFan.equals("6")){ statusFan = "notFunctioning";}
		        //	 System.out.println(statusFan);
		        	 
			       if(!statusFan.equals("normal") && statusFan !=null)
				   {
			        	info = "INSERT INTO logs VALUES ('"+dateFormat.format(date)+"','"+device+"','FAN status: "+statusFan+"')";
			        	stmt.executeUpdate(info);
			            }
		        	 
			        n.GetNext(IP,ciscoEnvMonSupplyStatusDescrOID,ciscoEnvMonSupplyStatusDescr, community);  //Get PowerSupply description
			        data = n.getChar();
			        ciscoEnvMonSupplyStatusDescrOID = n.getNextOID();
			        if(data == null) break;
			        String descrPower = data;
		        	int a1 = ciscoEnvMonSupplyStatusDescr.length();
		                String tmp1 = ciscoEnvMonSupplyStatusDescrOID.substring(a1+1);
		       //         System.out.println(descrPower);
		                
			        g.get(IP,ciscoEnvMonSupplyState+"."+tmp1); //Get PowerSupply status 1:normal 2:warning 3:critical 4:shutdown 5:notPresent 6:notFunctioning
			        String statusPower = g.getGetChar();
			         if(statusPower.equals("1")){ statusPower = "normal";}
			         if(statusPower.equals("2")){ statusPower = "warning";}
			         if(statusPower.equals("3")){ statusPower = "critical";}
			         if(statusPower.equals("4")){ statusPower = "shutdown";}
			         if(statusPower.equals("5")){ statusPower = "notPresent";}
			         if(statusPower.equals("6")){ statusPower = "notFunctioning";}   
			//         System.out.println(statusPower);
			         
			        if(!statusPower.equals("normal"))
			            {
				        info = "INSERT INTO logs VALUES ('"+dateFormat.format(date)+"','"+device+"','PowerSupply status: "+statusPower+"')";
				        stmt.executeUpdate(info);
				     }
		        	
	        		
	        	    }
	        	
	        	if(memUsingPercent > 65)
	        	    {
	        		info = "INSERT INTO logs VALUES ('"+dateFormat.format(date)+"','"+device+"','Used memory: "+memUsingPercent+"%')";
	        		stmt2.executeUpdate(info);
	        	    }
	        	
	            }

	    }
	
	public void Connect(Connection connect1, Connection connect2)
	     {
		 connection1 = connect1;
		 connection2 = connect2;
	     }
    }
