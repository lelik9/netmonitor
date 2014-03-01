package server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.yaml.snakeyaml.Yaml;

import DB.connect;
import DB.select.GetDeviceName;
import DB.select.GetIntInfo;
import Functions.ArpTable;
import Functions.ExtIntInfo;
import Functions.FindDevice;
import Functions.InterfaceInfo;
import Functions.MacTable;
import Functions.NetworkIPtable;
import Functions.VlanTable;
import SNMP.GetNext;
import SNMP.SetChar;

public class ServerHandler implements IoHandler
    {
	private static String dump;
	
	public static String getDump()
	    {
		    return dump;
	    }

	public static void setDump(String dump)
	    {
		    ServerHandler.dump = dump;
	    }

	public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
	    {
	        cause.printStackTrace();
	    }

	public void messageReceived( IoSession session, Object message ) throws Exception
	    {
		ExtIntInfo ex =new ExtIntInfo();
	        FindDevice f = new FindDevice();
	        GetIntInfo i = new GetIntInfo();
	        ArpTable a =new ArpTable();
	        VlanTable v =new VlanTable();
	        MacTable m = new MacTable();
	        NetworkIPtable nn = new NetworkIPtable();
	        GetDeviceName dn = new GetDeviceName();
	        
		Yaml yaml = new Yaml();
		System.out.println(message);
		Map<String, String> data = (Map<String, String>) yaml.load(message.toString());
		 
		switch(data.get("func"))
		{
		    case "extintinfo":
			ex.ExtIntInfo(data.get("device"));
			System.out.println(dump);
			session.write(dump);
			break;

		    case "intinfo":
			i.GetIntInfo(data.get("device"));
			System.out.println(dump);
			session.write(dump);
			break;
			
		    case "vlantable":
			v.VlanTable(data.get("device"));
			break;
			
		    case "mactable":
			m.MacTable(data.get("device"));
			break;
			
		    case "arptable":
			a.GetArp(data.get("device"));
			break;
			
		    case "finddev":
			f.FindDevice(Integer.parseInt(data.get("ip1")), Integer.parseInt(data.get("ip2")), 
				Integer.parseInt(data.get("ip3")), Integer.parseInt(data.get("ip4")), 
				Integer.parseInt(data.get("mask")), Integer.parseInt(data.get("port")), data.get("community"));
			break;
			
		    case "devices":
			dn.GetDeviceName(data.get("device"));
			System.out.println(dump);
			session.write(dump);
			break;
			
		    case "exit":
			session.close();
		}

	    }

	    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
	    {
	        System.out.println( "IDLE " + session.getIdleCount( status ));
	    }

	    @Override
	    public void messageSent(IoSession arg0, Object arg1)
		    throws Exception
		{
		    // TODO Auto-generated method stub

		}

	    @Override
	    public void sessionClosed(IoSession arg0) throws Exception
		{
		    // TODO Auto-generated method stub

		}

	    @Override
	    public void sessionCreated(IoSession arg0) throws Exception
		{
		    // TODO Auto-generated method stub

		}

	    @Override
	    public void sessionOpened(IoSession arg0) throws Exception
		{
		    // TODO Auto-generated method stub

		}
    }