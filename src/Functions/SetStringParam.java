package Functions;

import java.io.IOException;

import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

import SNMP.SetChar;

public class SetStringParam 
{
	private void SetStringParam(String IP,String OID,String Char) throws IOException
	{
        SetChar s = new SetChar();
		
		s.start();
		
		Variable var = new OctetString(Char);
		s.Set(IP, OID, var);
	}

}
