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
import threads.UpdateInfoThread;
import SNMP.Get;
import DB.connect;



public class main {

    	private static Connection connect1;
    	private static Connection connect2;
    	private static final int PORT = 9123;
    	
    	static UpdateInfoThread update;
    	static HelthTread health;

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
	        connect con = new connect();
	        update = new UpdateInfoThread();
	        health = new HelthTread();
	        
	        Connection connect1 = con.connectdb("monitor_db");//Connect to main base
	        setConnect1(connect1);
	        
	        Connection connect2 = con.connectdb("mib_db");//Connect to OID base
	        setConnect2(connect2);
	        
	        health.Connect(connect1, connect2);
	        update.Connect(connect1, connect2);
	        	        
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


	                IP = "udp:"+ip1+"."+ip2+"."+ip3+"."+ip4+"/"+port;
	                
	                update.start();
	                health.start();

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
