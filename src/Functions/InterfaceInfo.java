package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import DB.connect;
import SNMP.Get;
import SNMP.GetNext;

public class InterfaceInfo
{
    
  public void GetIntInfo(int ip1,int ip2,int ip3,int ip4, int port, String name) throws IOException, SQLException
    {
	String ifDescr = "1.3.6.1.2.1.2.2.1.2";
	String ifDescrOID=ifDescr;
	String ifOperStatuss ="1.3.6.1.2.1.2.2.1.8";
	String ifOperStatussOID =  ifOperStatuss;
	String ifPhysAddress = "1.3.6.1.2.1.2.2.1.6";
	String ifPhysAddressOID = ifPhysAddress;
	String IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;
	String ipAdEntAdd = "1.3.6.1.2.1.4.20.1.1";
	String ipAdEntAddOID = ipAdEntAdd;
	String ipAdEntIfIndex = "1.3.6.1.2.1.4.20.1.2";

	String Char="";
	String info;

	
        GetNext n = new GetNext();
        Get t = new Get();
        connect con = new connect();
        n.start();
        t.start();
        
        Connection connection = con.connectdb();
        Statement stmt = connection.createStatement();
        
	

       
        String TableClear = "DELETE FROM "+name;
	stmt.execute(TableClear);

        while(Char!=null)
            {
           //    	System.out.println(Char);
        	n.GetNext(IP,ifDescrOID,ifDescr);  //Get interface Name
        	Char=n.getChar();
        	String intname = Char;
        	ifDescrOID = n.getNextOID();
        	
             //  	System.out.println(Char);
        	n.GetNext(IP,ifOperStatussOID,ifOperStatuss); //Get interface status
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
        	    ifOperStatussOID = n.getNextOID();
        	    
               // System.out.println(Char);
                n.GetNext(IP,ifPhysAddressOID,ifPhysAddress); //Get interface mac
                 Char=n.getChar();
                 String mac=Char;
                ifPhysAddressOID = n.getNextOID();  
                
                info = "INSERT INTO "+name+" VALUES ('"+intname+"','"+Status+"','-','"+mac+"')";
                stmt.executeUpdate(info);
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
        	String tmpIP=ip1+"."+ip2+"."+ip3+"."+ip4;

                info = "UPDATE "+name+" SET ip = '"+tmpIP+"' WHERE interface = '"+Char+"'";
                stmt.executeUpdate(info);
                
            }
        connection.close();
    }
}	
