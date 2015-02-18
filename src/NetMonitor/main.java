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
import threads.HistoryClearThread;
import threads.RequestDeviceThread;
import SNMP.Get;
import SNMP.GetNext;
import SNMP.Walk;
import DB.Connect;
import DB.DbConnectionPool;
import Functions.Calculate;
import Functions.FindDevice;
import Functions.NetworkMap;
import Functions.Universal;



public class main {

    	private static Connection connect1;
    	private static Connection connect2;
    	private static final int PORT = 9123;
    	

    	static HelthTread health;
    	static RequestDeviceThread request;
    	static HistoryClearThread history;
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
	        Connect con = new Connect();
	 //       update = new UpdateInfoThread();
	//        health = new HelthTread();
	        request = new RequestDeviceThread();
	        history = new HistoryClearThread();

	        
	        DbConnectionPool dbConnectionPool = DbConnectionPool.getInstance();
	        
	        connect1 = dbConnectionPool.getConnection1();
	        connect2 = dbConnectionPool.getConnection2();
	 //       health.Connect(connect1, connect2);

	        	        

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


	             //   health.start();
	              //  history.start(); // Поток создания истории
	               request.start(); //Поток опроса устройств

	            } finally {

	            }
	        } catch (IOException e) {
	            System.out.println(e.toString());
	        }
	        

	    }

}
