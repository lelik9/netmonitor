package NetMonitor;

import java.io.IOException;







import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import server.ServerHandler;
import threads.HelthTread;
import threads.RequestDevice;
import SNMP.Get;
import SNMP.GetNext;
import SNMP.Walk;
import DB.Connect;
import DB.DbConnectionPool;
import Functions.FindDevice;
import Functions.Universal;



public class main {

    	private static Connection connect1;
    	private static Connection connect2;
    	private static final int PORT = 9123;
    	

    	static HelthTread health;
    	static RequestDevice request;

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
	        Connect con = new Connect();
	 //       update = new UpdateInfoThread();
	//        health = new HelthTread();
	        request = new RequestDevice();
	        
	        GetNext next = GetNext.getInstance();
	        next.start();
	        DbConnectionPool dbConnectionPool = DbConnectionPool.getInstance();
	        
	   //     Connection connect1 = dbConnectionPool.getConnection();//Connect to main base
	  //      setConnect1(connect1);
	        
	  //      Connection connect2 = con.connectdb("mib_db");//Connect to OID base
	  //      setConnect2(connect2);
	        connect1 = dbConnectionPool.getConnection1();
	        connect2 = dbConnectionPool.getConnection2();
	 //       health.Connect(connect1, connect2);

	        	        
	    	int ip1 = 10;
	    	int ip2 = 10;
	    	int ip3 = 9;
	    	int ip4 = 3;
	    	int port = 161;
	    	String community = "public";
	    	int mask=192;
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


	                IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;
	                FindDevice f = new FindDevice();
	        //        f.FindDevice(ip1, ip2, ip3, ip4, mask, port, community);
	                Universal u = new Universal();
	            //    u.Universal("40");
	           //     Walk w = new Walk();
	           //     w.walk(IP, "1.3.6.1.2.1.1.1.0", community);

	             //   health.start();
	                request.start();

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
