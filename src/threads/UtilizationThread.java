package threads;

public class UtilizationThread extends Thread
    {
	 @Override
	    public void run()
	    {
	        do
	        {
	            if(!mFinish)	//Проверка на необходимость завершения
	            {
	                if(mIsIncrement)	
	                    Program.mValue++;	//Инкремент
	                else
	                    Program.mValue--;	//Декремент
	                
	                //Вывод текущего значения переменной
	                System.out.print(Program.mValue + " ");
	            }
	            else
	                return;		//Завершение потока

	            try{
	                Thread.sleep(1000);		//Приостановка потока на 1 сек.
	            }catch(InterruptedException e){}
	        }
	        while(true); 
	    }

    }
