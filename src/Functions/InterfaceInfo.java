package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DB.connect;
import SNMP.Get;
import SNMP.GetNext;

public class InterfaceInfo
{
	private PreparedStatement preparedStatement = null;
    
  public void GetIntInfo(int ip1,int ip2,int ip3,int ip4, int port, String name) throws IOException, SQLException
    {
        String Char="";
	String info;
	String vlan="";
	String index="";
	String IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;
        String tmpIP=ip1+"."+ip2+"."+ip3+"."+ip4;
	
        connect con = new connect();
        GetNext n = new GetNext();
        Get t = new Get();
        n.start();
        t.start();
        
        Connection connect = con.connectdb("monitor_db");//Connect to main base
        Statement stmt = connect.createStatement();
        
        Connection connection = con.connectdb("mib_db");//Connect to OID base

        //Reading OID from base
        preparedStatement = connection.prepareStatement("SELECT oid FROM public_oid WHERE object = ?");
       
        preparedStatement.setString(1, "ifIndex");
        ResultSet res = preparedStatement.executeQuery();
        res.next(); String ifIndex = res.getString(1);
        String ifIndexOID= ifIndex;
        
        preparedStatement.setString(1, "ifDescr");
        res = preparedStatement.executeQuery();
        res.next(); String ifDescr = res.getString(1);
        String ifDescrOID=ifDescr; 
        
        preparedStatement.setString(1, "ifOperStatus");
        res = preparedStatement.executeQuery();
        res.next(); String ifOperStatus = res.getString(1);
        String ifOperStatusOID= ifOperStatus;
        
        preparedStatement.setString(1, "ifPhysAddress");
        res = preparedStatement.executeQuery();
        res.next(); String ifPhysAddress = res.getString(1);
        String ifPhysAddressOID= ifPhysAddress;
        
        preparedStatement.setString(1, "ipAdEntAdd");
        res = preparedStatement.executeQuery();
        res.next(); String ipAdEntAdd = res.getString(1);
        String ipAdEntAddOID= ipAdEntAdd;
        
        preparedStatement.setString(1, "ipAdEntIfIndex");
        res = preparedStatement.executeQuery();
        res.next(); String ipAdEntIfIndex = res.getString(1);

        
        preparedStatement = connection.prepareStatement("SELECT oid FROM Cisco_oid WHERE object = ?");
        
        preparedStatement.setString(1, "vmVlan");
        res = preparedStatement.executeQuery();      
        res.next(); String vmVlan = res.getString(1);
        String vmVlanOID = vmVlan+"."+index;
        
        preparedStatement.setString(1, "vlanTrunkPortDynamicState");
        res = preparedStatement.executeQuery();      
        res.next(); String vlanTrunkPortDynamicState = res.getString(1);
        String vlanTrunkPortDynamicStateOID = vlanTrunkPortDynamicState;
	
        
        //Reading group name
        String Group = "SELECT Groups FROM devices WHERE IPaddress = '"+tmpIP+"'";
        res = stmt.executeQuery(Group);
        res.next(); String group = res.getString(1);
        
	
      
        String TableClear = "DELETE FROM "+name;
	stmt.execute(TableClear);

        while(Char!=null)
            {
           //    	System.out.println(Char);
        	n.GetNext(IP,ifIndexOID,ifIndex);  //Get interface Index
        	Char=n.getChar();
        	index = Char;
        	ifIndexOID = n.getNextOID();
              	if(Char==null) break;
              	

                n.GetNext(IP,ifDescrOID,ifDescr);  //Get interface Name
                Char=n.getChar();
                String intname = Char;
                ifDescrOID = n.getNextOID();

        	
             //  	System.out.println(Char);
        	n.GetNext(IP,ifOperStatusOID,ifOperStatus); //Get interface status
        	Char=n.getChar();
        	if(Char==null) break;
        	if(Char.equals("1")) 
        	    {
        		Char="Up";
        			 
        	    } if(Char.equals("2"))
        		{
        		    Char="Down";
        		}
        	    String Status = Char;
        	    ifOperStatusOID = n.getNextOID();
        	    
               // System.out.println(Char);
                n.GetNext(IP,ifPhysAddressOID,ifPhysAddress); //Get interface mac
                 Char=n.getChar();
                 String mac=Char;
                ifPhysAddressOID = n.getNextOID(); 
                

                
                info = "INSERT INTO "+name+" VALUES ('"+intname+"','"+Status+"','-','"+vlan+"','"+mac+"')";
                stmt.executeUpdate(info);
            }
        
        if(group.equals("Cisco")==true) //Get interface Vlan
            {
        	while(vlan!=null)
        	    {
        		n.GetNext(IP,vmVlanOID,vmVlan); 
        		vmVlanOID = n.getNextOID();
        		n.GetNext(IP,vmVlanOID,vmVlan); 
        		
        		System.out.println(vmVlanOID);
        		vlan = n.getChar(); 
        		int a = vmVlan.length();
        		String tmp = vmVlanOID.substring(a+1);
        		
                        n.GetNext(IP,ifDescr+"."+tmp,ifDescr);
                        System.out.println(ifDescr+"."+tmp);//Get interface Name
                        Char=n.getChar();
                        System.out.println(Char);
                        info = "UPDATE "+name+" SET Vlan = '"+vlan+"' WHERE interface = '"+Char+"'";
                        stmt.executeUpdate(info);
                        
        		n.GetNext(IP, vlanTrunkPortDynamicStateOID, vlanTrunkPortDynamicState);
        		String trunk = n.getChar();
        		vlanTrunkPortDynamicStateOID = n.getNextOID();
            //   if(trunk ==null) break;
/*
		if(trunk.equals("1"))
		    {
        		 a = vlanTrunkPortDynamicState.length();
        		 tmp = vlanTrunkPortDynamicStateOID.substring(a+1);
                         n.GetNext(IP,ifDescr+"."+tmp,ifDescr);
                         System.out.println(ifDescr+"."+tmp);//Get interface Name
                         Char=n.getChar();
        		 vlan = "trunk";
	                info = "UPDATE "+name+" SET Vlan = '"+vlan+"' WHERE interface = '"+Char+"'";
	                stmt.executeUpdate(info);
		    }*/


        	}
            }
        
        Char="";
        while(Char!=null) //Chek interface with IP
            {
        	n.GetNext(IP,ipAdEntAddOID,ipAdEntAdd);
        	Char=n.getChar();
        	if(Char==null) break;
        	
        	t.get(IP,ipAdEntIfIndex+"."+Char);
        	Char=t.getGetChar();
        	if(Char==null) break;
        	
        	t.get(IP,ifDescr+"."+Char);
        	Char=t.getGetChar();
        	
        	ipAdEntAddOID = n.getNextOID();
        	//System.out.println(Char);

                info = "UPDATE "+name+" SET ip = '"+tmpIP+"' WHERE interface = '"+Char+"'";
                stmt.executeUpdate(info);
                
            }
        connection.close();
    }
}	
