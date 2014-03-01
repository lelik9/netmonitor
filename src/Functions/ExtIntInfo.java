package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;

import server.ServerHandler;
import DB.connect;
import SNMP.*;

public class ExtIntInfo
    {
	private PreparedStatement preparedStatement = null;
	
	public void Dump(List <String> ifDescr, List <String> ifMtu, List <String> ifSpeed, List <String> ifLastChange, List <String> ifInUcastPkts,
		List <String> ifInNUcastPkts, List <String>   ifInMulticastPkts, List <String> ifInBroadcastPkts, List <String> ifInDiscards, 
		List <String> ifInErrors, List <String> ifOutUcastPkts, List <String> ifOutNUcastPkts, List <String>   ifOutMulticastPkts, 
		List <String> ifOutBroadcastPkts, List <String> ifOutDiscards, List <String> ifOutErrors) 
	    {
		ServerHandler srv = new ServerHandler();
		Map<Integer, List <String>> data = new HashMap<Integer, List <String>>();
		data.put(0, ifDescr);
		data.put(1, ifMtu);
		data.put(2, ifSpeed);
		data.put(3, ifLastChange);
		data.put(4, ifInUcastPkts);
		data.put(5, ifInNUcastPkts);
		data.put(6,ifInMulticastPkts);
		data.put(7, ifInBroadcastPkts);
		data.put(8, ifInDiscards);
		data.put(9, ifInErrors);
		data.put(10, ifOutUcastPkts);
		data.put(11, ifOutNUcastPkts);
		data.put(12,ifOutMulticastPkts);
		data.put(13, ifOutBroadcastPkts);
		data.put(14, ifOutDiscards);
		data.put(15, ifOutErrors);
		DumperOptions options = new DumperOptions();
		Yaml yaml = new Yaml(options);
		int i = yaml.dump(data).length();
		options.setWidth(i);
		srv.setDump(yaml.dump(data));

	}
	
	public void ExtIntInfo(String device) throws IOException, SQLException
	    {
		String Char="";
		List <String> Descr = new ArrayList();
		List <String> Mtu = new ArrayList();
		List <String> Speed = new ArrayList();
		List <String> LastChange = new ArrayList();
		List <String> InUcastPkts = new ArrayList();
		List <String> InNUcastPkts = new ArrayList();
		List <String> InMulticastPkts = new ArrayList();
		List <String> InBroadcastPkts = new ArrayList();
		List <String> InDiscards = new ArrayList();
		List <String> InErrors = new ArrayList();
		List <String> OutUcastPkts = new ArrayList();
		List <String> OutNUcastPkts = new ArrayList();
		List <String> OutDiscards = new ArrayList();
		List <String> OutMulticastPkts = new ArrayList();
		List <String> OutBroadcastPkts = new ArrayList();
		List <String> OutErrors = new ArrayList();
		int nn=0;
		
		Descr.add("Description");
		Mtu.add("Mtu");
		Speed.add("Speed");
		LastChange.add("LastChange");
		InUcastPkts.add("InUcastPkts");
		InNUcastPkts.add("InNUcastPkts");
		InMulticastPkts.add("InMulticastPkts");
		InBroadcastPkts.add("InBroadcastPkts");
		InDiscards.add("InDiscards");
		InErrors.add("InErrors");
		OutUcastPkts.add("OutUcastPkts");
		OutNUcastPkts.add("OutNUcastPkts");
		OutMulticastPkts.add("OutMulticastPkts");
		OutBroadcastPkts.add("OutBroadcastPkts");
		OutDiscards.add("OutDiscards");
		OutErrors.add("OutErrors");
		
		GetNext n = new GetNext();
	        connect con = new connect();
	        
	        n.start();
	        
	     //   IP=IP+"/161";
	        Connection connect = con.connectdb("monitor_db");//Connect to main base
	        Statement stmt = connect.createStatement();
	        
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
	        
	        preparedStatement.setString(1, "ifInMulticastPkts");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifInMulticastPkts = res.getString(1);
	        String ifInMulticastPktsOID = ifInUcastPkts;
	        
	        preparedStatement.setString(1, "ifInBroadcastPkts");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifInBroadcastPkts = res.getString(1);
	        String ifInBroadcastPktsOID = ifInNUcastPkts;
	        
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
	        
	        preparedStatement.setString(1, "ifOutMulticastPkts");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutMulticastPkts = res.getString(1);
	        String ifOutMulticastPktsOID = ifOutUcastPkts;
	        
	        preparedStatement.setString(1, "ifOutBroadcastPkts");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutBroadcastPkts = res.getString(1);
	        String ifOutBroadcastPktsOID = ifOutNUcastPkts;
	        
	        preparedStatement.setString(1, "ifOutDiscards");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutDiscards = res.getString(1);
	        String ifOutDiscardsOID = ifOutDiscards;
	        
	        preparedStatement.setString(1, "ifOutErrors");
	        res = preparedStatement.executeQuery();
	        res.next(); String ifOutErrors = res.getString(1);
	        String ifOutErrorsOID = ifOutErrors;
	        
	        //Reading device IP from main base
	        String sel = "SELECT IPaddress FROM devices WHERE DeviceName='"+device+"'";
	        res = stmt.executeQuery(sel);
	        res.next(); String IP = res.getString(1);
	        
		sel = "SELECT Community FROM Devices WHERE DeviceName = '"+device+"'";
		res = stmt.executeQuery(sel);
		res.next(); String community = res.getString(1);
		
		sel = "SELECT Port FROM Devices WHERE DeviceName = '"+device+"'";
		res = stmt.executeQuery(sel);
		res.next(); String port = res.getString(1);

	        IP = "udp:"+IP+"/"+port;//Set address of device (CHANGE PORT)
	        
	        //Geting info from hardware
	        while(Char!=null)
	            {
	        	n.GetNext(IP,ifDescrOID,ifDescr, community);  //Get interface Name
	        	Char=n.getChar();
	        	ifDescrOID = n.getNextOID();
	        	Descr.add(Char);
	        	//if(Char.equals("Null0"))return;
	        	if(Char==null)break;	        	

	        	
	        	n.GetNext(IP,ifMtuOID,ifMtu, community);  //Get interface MTU
	        	Mtu.add(n.getChar());
	        	if(Mtu == null) Mtu.add("");
	        	ifMtuOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifSpeedOID,ifSpeed, community);  //Get interface Speed
	        	Speed.add(n.getChar());
	        	//int sp = Integer.parseInt(Speed)/1000000;
	        	//Speed = Integer.toString(sp);
	        	if(Speed == null) Speed.add("");
	        	ifSpeedOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifLastChangeOID, ifLastChange, community);  //Get interface time of last status change
	        	LastChange.add(n.getChar());
	        	if(LastChange == null) LastChange.add("");
	        	ifLastChangeOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifInUcastPktsOID,ifInUcastPkts, community);  //Get interface in unicast packets
	        	InUcastPkts.add(n.getChar());
	        	if(InUcastPkts == null) InUcastPkts.add("");
	        	ifInUcastPktsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifInNUcastPktsOID,ifInNUcastPkts, community);  //Get interface in non unicast packets
	        	InNUcastPkts.add(n.getChar());
	        	if(InNUcastPkts == null) InNUcastPkts.add("");
	        	ifInNUcastPktsOID = n.getNextOID();
	        	
	        	n.GetNext(IP,ifInMulticastPktsOID,ifInMulticastPkts, community);  //Get interface in multicast packets
	        	InMulticastPkts.add(n.getChar());
	        	if(InMulticastPkts == null) InMulticastPkts.add("");
	        	ifInUcastPktsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifInBroadcastPktsOID,ifInBroadcastPkts, community);  //Get interface in non broadcast packets
	        	InBroadcastPkts.add(n.getChar());
	        	if(InBroadcastPkts == null) InBroadcastPkts.add("");
	        	ifInBroadcastPktsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifInDiscardsOID,ifInDiscards, community);  //Get interface in discards
	        	InDiscards.add(n.getChar());
	        	if(InDiscards == null) InDiscards.add("");
	        	ifInDiscardsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifInErrorsOID,ifInErrors, community);  //Get interface in errors
	        	InErrors.add(n.getChar());
	        	if(InErrors == null) InErrors.add("");
	        	ifInErrorsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifOutUcastPktsOID,ifOutUcastPkts, community);  //Get interface out unicast packets
	        	OutUcastPkts.add(n.getChar());
	        	if(OutUcastPkts == null) OutUcastPkts.add("");
	        	ifOutUcastPktsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifOutNUcastPktsOID,ifOutNUcastPkts, community );  //Get interface out non unicast packets
	        	OutNUcastPkts.add(n.getChar());
	        	if(OutNUcastPkts == null) OutNUcastPkts.add("");
	        	ifOutNUcastPktsOID = n.getNextOID();
	        	
	        	n.GetNext(IP,ifOutMulticastPktsOID, ifOutMulticastPkts, community);  //Get interface in multicast packets
	        	OutMulticastPkts.add(n.getChar());
	        	if(OutMulticastPkts == null) OutMulticastPkts.add("");
	        	ifOutUcastPktsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifOutBroadcastPktsOID,ifOutBroadcastPkts, community);  //Get interface in non broadcast packets
	        	OutBroadcastPkts.add(n.getChar());
	        	if(OutBroadcastPkts == null) OutBroadcastPkts.add("");
	        	ifOutBroadcastPktsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifOutDiscardsOID,ifOutDiscards, community);  //Get interface out discards
	        	OutDiscards.add(n.getChar());
	        	if(OutDiscards == null) OutDiscards.add("");
	        	ifOutDiscardsOID = n.getNextOID();

	        	
	        	n.GetNext(IP,ifOutErrorsOID,ifOutErrors, community);  //Get interface out errors
	        	OutErrors.add(n.getChar());
	        	if(OutErrors == null) OutErrors.add("");
	        	ifOutErrorsOID = n.getNextOID();
	        	


	            }

		Dump(Descr, Mtu, Speed, LastChange, InUcastPkts, InNUcastPkts, InMulticastPkts, InBroadcastPkts, 
			InDiscards, InErrors, OutUcastPkts, OutNUcastPkts, OutMulticastPkts, OutBroadcastPkts, OutDiscards, OutErrors);
	        
	        connection.close();
	    }
    }
