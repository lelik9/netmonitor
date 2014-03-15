package NetMonitor;

import java.io.IOException;







import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import server.ServerHandler;
import threads.UpdateInfoThread;
import SNMP.Get;
import SNMP.GetNext;
import SNMP.Walk;
import DB.connect;
import Functions.ArpTable;
import Functions.ExtIntInfo;
import Functions.FindDevice;
import Functions.InterfaceInfo;
import Functions.MacTable;
import Functions.NetworkIPtable;
import Functions.SetIntParam;
import Functions.VlanTable;
import SNMP.SetChar;

import com.mysql.*;


public class main {

    	private static Connection connect1;
    	private static Connection connect2;
    	private static final int PORT = 9123;
    	
    	static UpdateInfoThread update;

	public static Connection getConnect1()
	    {
		    return connect1;
	    }

	public static void setConnect1(Connection connect1)
	    {
		    main.connect1 = connect1;
	    }

	public static Connection getConnect2()
	    {
		    return connect2;
	    }

	public static void setConnect2(Connection connect2)
	    {
		    main.connect2 = connect2;
	    }

	public static void main(String[] args) throws SQLException, IOException 
	{
	        Get t = new Get();
	        FindDevice f = new FindDevice();
	        GetNext n = new GetNext();
	        SetChar s = new SetChar();
	        InterfaceInfo i = new InterfaceInfo();
	        ExtIntInfo ex =new ExtIntInfo();
	        ArpTable a =new ArpTable();
	        VlanTable v =new VlanTable();
	        MacTable m = new MacTable();
	        connect con = new connect();
	        NetworkIPtable nn = new NetworkIPtable();
	        Walk w = new Walk();
	        
	        update = new UpdateInfoThread();
	        
	        Connection connect1 = con.connectdb("monitor_db");//Connect to main base
	        setConnect1(connect1);
	        update.Connect(connect1);
	        
	        Connection connect2 = con.connectdb("mib_db");//Connect to OID base
	        setConnect2(connect2);
	        
	    	int ip1 = 192;
	    	int ip2 = 168;
	    	int ip3 = 110;
	    	int ip4 = 19;
	    	int port = 161;
	    	String community = "public";
	    	int mask=224;
	    	String IP;
	        try {
	            try {
	        	
	                ServerHandler sh = new ServerHandler();
	                sh.GetConnect(connect1, connect2);
	                
	        	IoAcceptor acceptor = new NioSocketAcceptor();

	                acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
	                acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
	                acceptor.setHandler( sh );

	                acceptor.getSessionConfig().setReadBufferSize( 2048 );
	                acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
	                acceptor.bind( new InetSocketAddress(PORT) );

	              //  n.start();
	                IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;
	                
	                update.start();
	               // String OID="1.3.6.1.2.1.4.22.1.3";
	              //  String OID2="1.3.6.1.2.1.17.4.3.1.1";
	               // nn.NetworkIP();
	               // f.FindDevice(ip1,ip2,ip3,ip4,mask,port,community);
	               // a.GetArp("1_korpus_Switch");
	              //  v.VlanTable("1_korpus_Switch");	
	             //  m.MacTable("1_korpus_Switch");
	                
	              // i.GetIntInfo(ip1, ip2, ip3, ip4, port);
	               // ex.ExtIntInfo(IP);
	            //    w.walk(IP,new String[] {"1.3.6.1.2.1.1.3", "1.3.6.1.2.1.2.2.1.1", "1.3.6.1.2.1.2.2.1.2"},"public");
	            } finally {
	                t.stop();
	            }
	        } catch (IOException e) {
	            System.out.println(e.toString());
	        }
	        
	       // connect1.close();
	      //  connect2.close();
	    }

}
