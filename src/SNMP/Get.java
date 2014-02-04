package SNMP;



import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

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


public class Get implements ResponseListener {
    
    private final static String SNMP_COMMUNITY = "public";
    private final static int    SNMP_RETRIES   = 1;
    private final static long   SNMP_TIMEOUT   = 200;
    
    private Snmp snmp = null;
    private TransportMapping transport = null;
    
    private Set<Integer32> requests = new HashSet<Integer32>();
    
    private String GetChar;
    



    
    public String getGetChar() {
		return GetChar;
	}


	public void setGetChar(String getChar) {
		GetChar = getChar;
	}


	public void onResponse(ResponseEvent event) 
    {
    	
        Integer32 requestId = event.getRequest().getRequestID();
        PDU response = event.getResponse();
        
        if (response != null) 
        {
    		setGetChar(response.get(0).toValueString());

        }  else
        {
        	
        	GetChar=null;
        	
        }

        synchronized (requests) 
        {
            requests.remove(requestId);
        }
    }

    
    public void get(String IP, String OID) 
    throws IOException 
    {
        Target t = getTarget(IP);

            send(t, OID);
        
        while (!requests.isEmpty()) 
            {
            try {
        		Thread.sleep(200);
                }
            catch (InterruptedException e) 
        	{
        	    e.printStackTrace();
        	}
            } 

    }
    
    private void send(Target target, String oid) throws IOException {
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);
        snmp.send(pdu, target, null, this);
        synchronized (requests) {
            requests.add(pdu.getRequestID());
        }
    }
   
  
    
    private Target getTarget(String address) {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(SNMP_COMMUNITY));
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