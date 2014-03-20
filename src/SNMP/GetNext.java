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
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class GetNext implements ResponseListener {
    


    private final static String SNMP_COMMUNITY = "public";
    private final static int    SNMP_RETRIES   = 3;
    private final static long   SNMP_TIMEOUT   = 1000;

    
    private Snmp snmp = null;
    private TransportMapping transport = null;

    private String NextOID;
    private String Char;
    private static String CheckOID;
    private static String Com;
    
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


    public void onResponse(ResponseEvent event) 
	{
        Integer32 requestId = event.getRequest().getRequestID();
        PDU response = event.getResponse();
     //   System.out.println(response.get(0).toString());
        if(response.get(0).getOid().startsWith(new OID(getCheckOID()))!=false)
        	    {
        		setChar(response.get(0).toValueString());
        		
        		setNextOID(response.get(0).getOid().toString());

        	    }  else 
        		{
        		    setChar(null);
        		} 	       
            
        

        synchronized (requests) 
        {
            requests.remove(requestId);
        } 
            
    }
    
    public void GetNext(String IP, String OID1, String CheckOID, String community) throws IOException
    {
        setCheckOID(CheckOID);
        setCom(community);
        Target t = getTarget(IP);
        send(t, OID1);
    }
    
    private void send(Target target, String oid1) throws IOException {
            PDU pdu = new PDU();
            
            pdu.add(new VariableBinding(new OID(oid1)));    
            pdu.setType(PDU.GETNEXT);

            ResponseEvent event = snmp.send(pdu, target,null );
        synchronized (requests) 
        {
            requests.add(pdu.getRequestID());
        }
        onResponse(event);
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
        transport.listen();
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