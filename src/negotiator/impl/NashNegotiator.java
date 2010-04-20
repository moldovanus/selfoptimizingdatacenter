package negotiator.impl;

import negotiator.Negotiator;
import greenContextOntology.Server;
import greenContextOntology.Task;
import greenContextOntology.RequestedTaskInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Apr 19, 2010
 * Time: 6:36:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class NashNegotiator implements Negotiator {

    private int quality = 10;
    public void negotiateGivenRanges(int minValue1, int maxValue1, int minValue2, int maxValue2, int optimalValue1, int optimalValue2)
    {
     int increment1= (maxValue1-minValue1)/quality;   //task
     int increment2 = (maxValue2-minValue2)/quality;        //server
     boolean[][] maxUtilities1 = new boolean[quality][quality];
     boolean[][] maxUtilities2 = new boolean[quality][quality];
     double utilities1[][]= new double[quality][quality];
     double utilities2[][]= new double[quality][quality];
        for ( int i = 0 ; i < quality ; i ++)
        {
        int currentValue1 = i*increment1+minValue1;
        double max = 0;
            int pos = 0;
         for (int j=0; j<quality ;j ++)
         {
         int currentValue2 = j*increment2+minValue2;

         double penalty1= (currentValue2-currentValue1)*increment1;
         double penalty2= (currentValue2-currentValue1)*increment2;
             if (penalty1<0) penalty1 *= (-1);
                if (penalty2<0) penalty2 *= (-1);
         utilities1[i][j]=(currentValue1/optimalValue1)-penalty1;
         utilities2[i][j]=(currentValue2/optimalValue2)-penalty2;
         if (j==0)
            max =  utilities1[i][j];
         if (max<utilities1[i][j])
         {
             max =utilities1[i][j];
             pos = j;
         }
         }
         maxUtilities1[i][pos]=true;
        }
    for (int j=0; j <quality ; j ++)
    {
        double max = 0;
        int pos =0;
        for (int i = 0 ; i< quality ; i++)
        {
                  if (i==0)
            max =  utilities2[i][j];
             if (max<utilities2[i][j])
                {
                 max =utilities2[i][j];
                 pos = i;
                }
        }
       maxUtilities2[pos][j]=true;

    }
    for (int i = 0 ; i< quality ; i++)
        for (int j = 0 ; j < quality; j++)
            if (maxUtilities1[i][j]&&maxUtilities2[i][j])
            {
                int currentValue1 = i*increment1+minValue1;
                int currentValue2 = j*increment2+minValue2;
                //TODO : if not equal, optimize function :D
                System.out.println ("Equilibrium Found!!!"+currentValue1+"  "+currentValue2 ) ;
            }
    }
    public void negotiate(Server server, Task task) {
        RequestedTaskInfo reqTask = task.getRequestedInfo();
       if (server.getAssociatedMemory().getMaxAcceptableValue()-(server.getAssociatedMemory().getUsed()+reqTask.getMemoryMinAcceptableValue())<0)
       {
        int optimalValueForServer =server.getAssociatedMemory().getMaxAcceptableValue()-server.getAssociatedMemory().getUsed() ;       
        negotiateGivenRanges(reqTask.getMemoryMinAcceptableValue(),reqTask.getMemoryMaxAcceptableValue(),0,server.getAssociatedMemory().getTotal()-server.getAssociatedMemory().getUsed(),reqTask.getMemoryMinAcceptableValue(),optimalValueForServer);
       }
    }
}
