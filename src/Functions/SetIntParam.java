package Functions;

import java.io.IOException;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Variable;

import SNMP.SetChar;

public class SetIntParam 
{
	private void SetIntParam(String IP,String OID,int Char) throws IOException
	{
        SetChar s = new SetChar();
		
		s.start();
		
		Variable var = new Integer32(Char);
		s.Set(IP, OID, var);
	}
	

}
