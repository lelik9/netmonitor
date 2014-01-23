package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DB.connect;
import SNMP.*;

public class ExtIntInfo
    {
	private PreparedStatement preparedStatement = null;
	
	public void ExtIntInfo(String IP) throws IOException, SQLException
	    {
		String Char="";
		
		GetNext n = new GetNext();
	        connect con = new connect();
	        n.start();
	        
	     //   IP=IP+"/161";
	        
	        Connection connection = con.connectdb("mib_db");//Connect to OID base

	        //Reading OID from base
	        preparedStatement = connection.prepareStatement("SELECT oid FROM public_oid WHERE object = ?");

	        preparedStatement.setString(1, "ifDescr");
	        ResultSet res = preparedStatement.executeQuery();
	        res.next(); String ifDescr = res.getString(1);
	        String ifDescrOID= ifDescr;
	        
	        preparedStatement.setString(1, "ifMtu");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifMtu = res.getString(1);
	        String ifMtuOID = ifMtu;
	        
	        preparedStatement.setString(1, "ifSpeed");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifSpeed = res.getString(1);
	        String ifSpeedOID = ifSpeed;
	        
	        preparedStatement.setString(1, "ifLastChange");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifLastChange = res.getString(1);
	        String ifLastChangeOID = ifLastChange;
	        
	        preparedStatement.setString(1, "ifInUcastPkts");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifInUcastPkts = res.getString(1);
	        String ifInUcastPktsOID = ifInUcastPkts;
	        
	        preparedStatement.setString(1, "ifInNUcastPkts");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifInNUcastPkts = res.getString(1);
	        String ifInNUcastPktsOID = ifInNUcastPkts;
	        
	        preparedStatement.setString(1, "ifInDiscards");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifInDiscards = res.getString(1);
	        String ifInDiscardsOID = ifInDiscards;
	        
	        preparedStatement.setString(1, "ifInErrors");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifInErrors = res.getString(1);
	        String ifInErrorsOID = ifInErrors;
	        
	        preparedStatement.setString(1, "ifOutUcastPkts");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutUcastPkts = res.getString(1);
	        String ifOutUcastPktsOID = ifOutUcastPkts;
	        
	        preparedStatement.setString(1, "ifOutNUcastPkts");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutNUcastPkts = res.getString(1);
	        String ifOutNUcastPktsOID = ifOutNUcastPkts;
	        
	        preparedStatement.setString(1, "ifOutDiscards");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutDiscards = res.getString(1);
	        String ifOutDiscardsOID = ifOutDiscards;
	        
	        preparedStatement.setString(1, "ifOutErrors");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutErrors = res.getString(1);
	        String ifOutErrorsOID = ifOutErrors;
	        
	        //Geting info from hardware
	        while(Char!=null)
	            {
	        	n.GetNext(IP,ifDescrOID,ifDescr);  //Get interface Name
	        	Char=n.getChar();
	        	ifDescrOID = n.getNextOID();
	        	//if(Char.equals("Null0"))return;
	        	if(Char==null)break;	        	
	        	System.out.println(Char);
	        	
	        	n.GetNext(IP,ifMtuOID,ifMtu);  //Get interface MTU
	        	String Mtu = n.getChar();
	        	if(Mtu == null) Mtu="";
	        	ifMtuOID = n.getNextOID();
	        	System.out.println(Mtu);
	        	
	        	n.GetNext(IP,ifSpeedOID,ifSpeed);  //Get interface Speed
	        	String Speed = n.getChar();
	        	//int sp = Integer.parseInt(Speed)/1000000;
	        	//Speed = Integer.toString(sp);
	        	if(Speed == null) Speed="";
	        	ifSpeedOID = n.getNextOID();
	        	System.out.println(Speed);
	        	
	        	n.GetNext(IP,ifLastChangeOID, ifLastChange);  //Get interface time of last status change
	        	String LastChange = n.getChar();
	        	if(LastChange == null) Mtu="";
	        	ifLastChangeOID = n.getNextOID();
	        	System.out.println(LastChange);
	        	
	        	n.GetNext(IP,ifInUcastPktsOID,ifInUcastPkts);  //Get interface in unicast packets
	        	String InUcastPkts = n.getChar();
	        	if(InUcastPkts == null) InUcastPkts="";
	        	ifInUcastPktsOID = n.getNextOID();
	        	System.out.println(InUcastPkts);
	        	
	        	n.GetNext(IP,ifInNUcastPktsOID,ifInNUcastPkts);  //Get interface in non unicast packets
	        	String InNUcastPkts = n.getChar();
	        	if(InNUcastPkts == null) InNUcastPkts="";
	        	ifInNUcastPktsOID = n.getNextOID();
	        	System.out.println(InNUcastPkts);
	        	
	        	n.GetNext(IP,ifInDiscardsOID,ifInDiscards);  //Get interface in discards
	        	String InDiscards = n.getChar();
	        	if(InDiscards == null) InDiscards="";
	        	ifInDiscardsOID = n.getNextOID();
	        	System.out.println(InDiscards);
	        	
	        	n.GetNext(IP,ifInErrorsOID,ifInErrors);  //Get interface in errors
	        	String InErrors = n.getChar();
	        	if(InErrors == null) InErrors="";
	        	ifInErrorsOID = n.getNextOID();
	        	System.out.println(InErrors);
	        	
	        	n.GetNext(IP,ifOutUcastPktsOID,ifOutUcastPkts);  //Get interface out unicast packets
	        	String OutUcastPkts = n.getChar();
	        	if(OutUcastPkts == null) OutUcastPkts="";
	        	ifOutUcastPktsOID = n.getNextOID();
	        	System.out.println(OutUcastPkts);
	        	
	        	n.GetNext(IP,ifOutNUcastPktsOID,ifOutNUcastPkts );  //Get interface out non unicast packets
	        	String OutNUcastPkts = n.getChar();
	        	if(OutNUcastPkts == null) OutNUcastPkts="";
	        	ifOutNUcastPktsOID = n.getNextOID();
	        	System.out.println(OutNUcastPkts);
	        	
	        	n.GetNext(IP,ifOutDiscardsOID,ifOutDiscards);  //Get interface out discards
	        	String OutDiscards = n.getChar();
	        	if(OutDiscards == null) OutDiscards="";
	        	ifOutDiscardsOID = n.getNextOID();
	        	System.out.println(OutDiscards);
	        	
	        	n.GetNext(IP,ifOutErrorsOID,ifOutErrors);  //Get interface out errors
	        	String OutErrors = n.getChar();
	        	if(OutErrors == null) OutErrors="";
	        	ifOutErrorsOID = n.getNextOID();
	        	System.out.println(OutErrors);
	            }
	            
	        
	        connection.close();
	    }
    }
