package server;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.yaml.snakeyaml.Yaml;

import server.remoteFunctions.DeviceDataSelect;
import DB.Connect;
import DB.select.GetArp;
import DB.select.GetConfInfo;
import DB.select.GetDeviceAndGroup;
import DB.select.GetIntInfo;
import DB.select.GetMac;
import DB.select.GetSNMP;
import DB.select.GetTemplatesInfo;
import DB.select.GetUsers;
import DB.select.GetVlan;
import DB.update.DevGroupUpdate;
import DB.update.DeviceUpdate;
import DB.update.TemplateUpdate;
import Functions.FindDevice;
import Functions.MacTable;
import SNMP.GetNext;
import SNMP.SetChar;

public class ServerHandler implements IoHandler
    {
	private static String dump;
	private static Connection connection1;
	private static Connection connection2;
	
	public void GetConnect(Connection connect1, Connection connect2)
	    {
		connection1 = connect1;
		connection2 = connect2;
	    }
	
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

	        FindDevice f = new FindDevice();
	        GetIntInfo i = new GetIntInfo();
	        GetArp a =new GetArp();
	        GetVlan v =new GetVlan();
	        GetMac m = new GetMac();

	        
		Yaml yaml = new Yaml();
		System.out.println(message);
		Map<String, String> data = (Map<String, String>) yaml.load(message.toString());

		switch(data.get("func"))
		{
		    
		    case "test":
			String test = "Hi!";
			System.out.println(test);
			session.write(test);
			break;

			/**
			 * Получение информации о устройствах и группах
			 */
		    case "devGroups":
			GetDeviceAndGroup devSelect = new GetDeviceAndGroup();
			devSelect.GetDeviceAndGroup(data);
			session.write(dump);
			System.out.println(dump);
			break;
			
		    case "devInfo":
			DeviceDataSelect dataSelect = new DeviceDataSelect();
			dataSelect.DeviceDataSelect(data);
			session.write(dump);
			break;
			
			/**
			 * Обнаружение и добавление в БД устройств
			 */
		    case "DiscoverDevice":
			FindDevice findDev = new FindDevice();
			findDev.FindDevice(data);
			session.write(dump);
			break;
			
		    case "confDevice":
			GetConfInfo confInf = new GetConfInfo();
			confInf.GetConfInfo(data);
			session.write(dump);
			break;
			
		    case "DevGroupUpdate":
			DevGroupUpdate groupUpdate = new DevGroupUpdate();
			groupUpdate.DevGroupUpdate(data);			
			session.write(dump);
			break;
			
			/**
			 * Удаление устройства или изменение его настроек
			 */
		    case "DevUpdate":
			DeviceUpdate devUpdate = new DeviceUpdate();
			devUpdate.DeviceUpdate(data);		
			session.write(dump);
			break;
			
			/**
			 * Получение информации по шаблонам
			 */
		    case "Templates":
			GetTemplatesInfo templateInfo = new GetTemplatesInfo();
			templateInfo.GetTemplatesInfo(data);		
			session.write(dump);
			break;
			
			/**
			 * Изменение шаблонов и элементов
			 */
		    case "templateUpdate":
			TemplateUpdate templateUpdate = new TemplateUpdate();
			templateUpdate.TemplateUpdate(data);
			session.write(dump);
			break;
			
		    case "getSNMP-Func":
			GetSNMP getSNMP = new GetSNMP();
			getSNMP.GetSNMP(data);
			session.write(dump);
			break;
			
			/**
			 * Получение информации о пользователях
			 */
		    case "getUsers":
			GetUsers getUsers = new GetUsers();
			getUsers.GetUsers(data);
			session.write(dump);
			break;
			

	//OLD		
		    case "vlantable":
			v.GetVlan(data.get("device"));
			session.write(dump);
			break;
			
		    case "mactable":
			m.GetMac(data.get("device"));
			session.write(dump);
			break;
			
		    case "arptable":
			a.GetArp(data.get("device"), connection1);
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