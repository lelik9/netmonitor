package threads;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimerTask;

import NetMonitor.main;

public class HistoryClearThread extends Thread
    {
	private Connection connection1;
	private String select;
	private Statement stmt1;
	private ResultSet res;
	private Date tmpDate = new Date(0000, 00, 01);
	private Time time = new Time(02,00,00);
	
	 @Override
	 public void run()
	    {
		connection1 = main.getConnect1();
		try
		    {
			stmt1 = connection1.createStatement();
			select = "SELECT time FROM group_1_data GROUP BY deviceID, name, OIDindex";
			res = stmt1.executeQuery(select);
			res.first();
			Date date = res.getDate(1);
			long tmp2 = date.getDate()+tmpDate.getDate();
			Date dt = new Date(tmp2);
			Time time2 = res.getTime(1);
			long tmp = time2.getTime() - time.getTime();
			Time tm = new Time(tmp);
			System.out.println(tm+" "+time2+" "+time);
			System.out.println(dt+" "+date+" "+tmpDate);
		    } catch (SQLException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
	    }
	 
	 class ClearTask extends TimerTask
	 {
	    private int day;
	    
	    public ClearTask(int n)
		{
		    day = n;
		}

	    @Override
	    public void run()
		{
			connection1 = main.getConnect1();
			try
			    {
				stmt1 = connection1.createStatement();
				select = "SELECT time FROM group_1_data GROUP BY deviceID, name, OIDindex";
				res = stmt1.executeQuery(select);
				res.first();
				Date result = res.getDate(1);
				System.out.println(result);
			    } catch (SQLException e)
			    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
		    
		}
	     
	 }
	 
	 class Clear
	 {
	     public void ClearTimer()
		 {
		     
		 }
	 }
	 
    }
