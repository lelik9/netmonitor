package threads;

public class UtilizationThread extends Thread
    {
	 @Override
	    public void run()
	    {
	        do
	        {
	            if(!mFinish)	//�������� �� ������������� ����������
	            {
	                if(mIsIncrement)	
	                    Program.mValue++;	//���������
	                else
	                    Program.mValue--;	//���������
	                
	                //����� �������� �������� ����������
	                System.out.print(Program.mValue + " ");
	            }
	            else
	                return;		//���������� ������

	            try{
	                Thread.sleep(1000);		//������������ ������ �� 1 ���.
	            }catch(InterruptedException e){}
	        }
	        while(true); 
	    }

    }
