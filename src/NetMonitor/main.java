package NetMonitor;

import java.io.IOException;







import java.sql.Connection;
import java.sql.SQLException;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import SNMP.Get;
import SNMP.GetNext;
import DB.connect;
import Functions.ArpTable;
import Functions.ExtIntInfo;
import Functions.FindDevice;
import Functions.InterfaceInfo;
import Functions.MacTable;
import Functions.SetIntParam;
import Functions.VlanTable;
import SNMP.SetChar;

import com.mysql.*;


public class main {

    
	 public static void main(String[] args) throws SQLException {
	        Get t = new Get();
	        FindDevice f = new FindDevice();
	        GetNext n = new GetNext();
	        SetChar s = new SetChar();
	        InterfaceInfo i = new InterfaceInfo();
	        ExtIntInfo ex =new ExtIntInfo();
	        ArpTable a =new ArpTable();
	        VlanTable v =new VlanTable();
	        MacTable m = new MacTable();
	    	int ip1 = 192;
	    	int ip2 = 168;
	    	int ip3 = 110;
	    	int ip4 = 20;
	    	int port = 161;
	    	String community = "public";
	    	int mask=224;
	    	String IP;
	        try {
	            try {

	              //  n.start();
	                IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;

	               // String OID="1.3.6.1.2.1.4.22.1.3";
	              //  String OID2="1.3.6.1.2.1.17.4.3.1.1";

	               // f.FindDevice(ip1,ip2,ip3,ip4,mask,port,community);
	               // a.GetArp("1_korpus_Switch");
	              //  v.VlanTable("1_korpus_Switch");	
	               m.MacTable("1_korpus_Switch");
	              // i.GetIntInfo(ip1, ip2, ip3, ip4, port);
	               // ex.ExtIntInfo(IP);
	            } finally {
	                t.stop();
	            }
	        } catch (IOException e) {
	            System.out.println(e.toString());
	        }
	    }

}
