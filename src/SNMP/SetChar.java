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
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SetChar implements ResponseListener {
    
    private final static String SNMP_COMMUNITY = "private";
    private final static int    SNMP_RETRIES   = 3;
    private final static long   SNMP_TIMEOUT   = 100;

    
    private Snmp snmp = null;
    private TransportMapping transport = null;
    
    private Set<Integer32> requests = new HashSet<Integer32>();
    
    public void onResponse(ResponseEvent event) {
        Integer32 requestId = event.getRequest().getRequestID();
        PDU response = event.getResponse();
        if (response != null) {
            System.out.println(response.toString());
            int errorStatus = response.getErrorStatus();
            int errorIndex = response.getErrorIndex();
            String errorStatusText = response.getErrorStatusText();

            if (errorStatus == PDU.noError)
            {
              System.out.println("Snmp Set Response = " + response.getVariableBindings());
            }
            else
            {
              System.out.println("Error: Request Failed");
              System.out.println("Error Status = " + errorStatus);
              System.out.println("Error Index = " + errorIndex);
              System.out.println("Error Status Text = " + errorStatusText);
            }


        } else {
            synchronized (requests) {
                if (requests.contains(requestId)) {
                    System.out.println("Timeout exceeded");
                }
            }
        }
        synchronized (requests) {
            requests.remove(requestId);
        }
    }
    
    public void Set (String IP, String OID, Variable Char) throws IOException {
        Target t = getTarget(IP);

        send(t, OID, Char);
    }
    
    private void send(Target target, String oid, Variable param) throws IOException {
        PDU pdu = new PDU();

        pdu.add(new VariableBinding(new OID(oid),param));
        
        pdu.setType(PDU.SET);

        ResponseEvent event = snmp.send(pdu, target, null);
        synchronized (requests) {
            requests.add(pdu.getRequestID());
        }
        onResponse(event);
    }
    
    private Target getTarget(String address) {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(SNMP_COMMUNITY));
        target.setAddress(targetAddress);
        target.setRetries(SNMP_RETRIES);
        target.setTimeout(SNMP_TIMEOUT);
        target.setVersion(SnmpConstants.version2c);
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