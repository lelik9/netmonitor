package SNMP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class Walk
    { 


	    private String oidNext;
	    private String oid;
	    private String Value;
	    private int tmp;
	    private List <String> AllValue = new ArrayList();
	    private List <String> index = new ArrayList();
	    private ResponseResult result;
	    
	    
	    public List <String> getIndex()
		{
			return index;
		}


	    public void setIndex(List <String> index)
		{
			this.index = index;
		}


	    public List <String> getAllValue()
		{
			return AllValue;
		}


	    public void setAllValue(List <String> allValue)
		{
			AllValue = allValue;
		}


	    public void walk(String IP, String OID, String community) throws IOException
	    {
		
		oid = OID;
		oidNext = oid;
		GetNext n = GetNext.getInstance();
		Get g = new Get();
		n.start();
		g.start();
		
		AllValue.clear();
		index.clear();

		Long firstTimestamp = System.currentTimeMillis();
		System.out.println(firstTimestamp);
		


		while(oidNext != null)
		    {
			n.GetNext(IP, oidNext, oid, community); 
			oidNext = n.getNextOID();
			//System.out.println("nextOID1= "+oidNext);
			//System.out.println("nextOID= "+oidNext);
			g.get(IP, oid, community);
			//System.out.println(g.getGetChar());
			if(g.getGetChar() != null )
			    {
				Value = g.getGetChar();
			//	System.out.println("OID= "+oid);
			//	System.out.println("Value1 "+Value);
				if(oidNext != null )
				    {
					tmp = oid.length();
					index.add(oidNext.substring(tmp+1));
				    }
				AllValue.add(Value);
			    }
			else
			    {
			//	System.out.println("nextOID1.5= "+oidNext);
			//	n.GetNext(IP, oidNext, oid, community);  //Get interface Index
			//	oidNext = n.getNextOID();
				if (oidNext == null) {
				    if(g.getGetChar() == null) break;
				    Value = g.getGetChar(); 
				    AllValue.add(Value); 
				    break;
				    }
			//	System.out.println("nextOID2= "+oidNext);
			//	System.out.println("Value2 "+Value);
				g.get(IP, oidNext, community);
				if(g.getGetChar() == null) break;
				Value = g.getGetChar();
				tmp = oid.length();

				index.add(oidNext.substring(tmp + 1));
				AllValue.add(Value);
			    }
			
		    }
		Long lastTimestamp = System.currentTimeMillis();
		System.out.println(lastTimestamp);
		long tmp2 = (lastTimestamp - firstTimestamp)/ 1000;
	            System.out.println("request time = "+tmp2);

	    }
	    

	    
    }
