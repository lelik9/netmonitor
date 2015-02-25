package SNMP;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import DB.DbConnectionPool;

public class GetNext implements ResponseListener {
    


   // private final static String SNMP_COMMUNITY = "public";
    private final static int    SNMP_RETRIES   = 2;
    private final static long   SNMP_TIMEOUT   = 5;

    
    private Snmp snmp = null;
    private TransportMapping transport = null;

    private String NextOID;
    private String Char;
    private static String CheckOID;
    private static String Com;
    private static volatile GetNext instance;
    
    private Set<Integer32> requests = new HashSet<Integer32>();

    public static String getCom()
	{
		return Com;
	}

    public static void setCom(String com)
	{
		Com = com;
	}

    public static String getCheckOID()
        {
    	return CheckOID;
        }

    public static void setCheckOID(String checkOID)
        {
    	CheckOID = checkOID;
        }
    
    public String getChar()
	{
		return Char;
	}

    public void setChar(String c)
	{
		Char = c;
	}

    public String getNextOID()
	{
		return NextOID;
	}

    public void setNextOID(String nextOID)
	{
		NextOID = nextOID;
	}
    
    private GetNext()
	{
	    
	}

    public static GetNext getInstance(){
            if(instance == null){
        	synchronized(DbConnectionPool.class){
        	    if(instance == null){
        		instance = new GetNext();
        	    }
        	}
            }
            return instance;
	}

    public synchronized void onResponse(ResponseEvent event) 
	{
        Integer32 requestId = event.getRequest().getRequestID();
        PDU response = event.getResponse();

        if (response ==null) return;
        if(response.get(0).getOid().startsWith(new OID(getCheckOID()))!=false)
        	    {
        		setChar(response.get(0).toValueString());
        		        		
        		setNextOID(response.get(0).getOid().toString());

        	    }  else 
        		{
        		    setChar(null);
        		    setNextOID(null);
        		} 	       
            
            

        synchronized (requests) 
        {
            requests.remove(requestId);
        } 
            
    }
    
    
    public synchronized ResponseResult GetNext(final String IP, final String OID1, final String CheckOID, final String community) throws IOException
    {
        setCheckOID(CheckOID);
        setCom(community);
        Target t = getTarget(IP);
        return send(t, OID1);

    }
    
    private synchronized ResponseResult send(Target target, String oid1) throws IOException {
            PDU pdu = new PDU();
            
            pdu.add(new VariableBinding(new OID(oid1)));    
            pdu.setType(PDU.GETNEXT);

         //   ResponseEvent event = snmp.send(pdu, target,null);
            ResponseEvent event = snmp.getNext(pdu, target);
                        
        synchronized (requests) 
        {
            requests.add(pdu.getRequestID());
        }
        if(event !=null)
            {
        	onResponse(event);
        	ResponseResult resp = new ResponseResult();
        	
        	resp.setChar(Char);
        	resp.setNextOID(NextOID);
        	return resp;
            }
        return null;
    }
    
    private Target getTarget(String address) {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(getCom()));
        target.setAddress(targetAddress);
        target.setRetries(SNMP_RETRIES);
        target.setTimeout(SNMP_TIMEOUT);
        target.setVersion(SnmpConstants.version1);
        return target;
    }
    
    public void start() throws IOException {
        transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        snmp.listen();
      //  transport.listen();
    }
    
    public void stop() throws IOException {
        try {
            if (transport != null) {
                transport.close();
                transport = null;
            }
        } finally {
            if (snmp != null) {
                snmp.close();
                snmp = null;
            }
        }
    }
    
  
}