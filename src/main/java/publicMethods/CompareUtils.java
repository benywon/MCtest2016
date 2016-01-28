package publicMethods;

import edu.stanford.nlp.util.Execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by benywon on 1/25 0025.
 */
public class CompareUtils
{
    public static double ErrorCompareValue=Double.MIN_VALUE;
    public static double getCosine(Double[] s1,Double[] s2)
    {
        if(s1.length!=s2.length)
        {
            return ErrorCompareValue;
        }
        int length=s1.length;
        double dominator=0.0;
        for (int i = 0; i < length; i++)
        {
            dominator+=s1[i]*s2[i];
        }
        double len1= MathUtils.l2Norm(s1);
        double len2=MathUtils.l2Norm(s2);
        return dominator/(len1*len2);
    }

    public static double Compare2List(List<Double[]> list1,List<Double[]> list2,String maxOrave)
    {
        double value;
        if(maxOrave.equals("ave"))
        {
            value=Compare2ListAVE(list1,list2);
        }
        else
        {
            value=Compare2ListMAX(list1,list2);
        }
        return value;
    }
    public static double Compare2ListMAX(List<Double[]> list1,List<Double[]> list2)
    {
        List<Double> result=new ArrayList<>();
        list1.forEach(x->{
            list2.forEach(y->{
                result.add(CompareUtils.getCosine(x,y));
            });
        });
        double max=result.parallelStream().mapToDouble(x->x).max().orElse(CompareUtils.ErrorCompareValue);
        return max;
    }
    public static double Compare2ListAVE(List<Double[]> list1,List<Double[]> list2)
    {
        List<Double> result=new ArrayList<>();
        list1.forEach(x-> list2.forEach(y-> result.add(CompareUtils.getCosine(x,y))));
        double ave=result.parallelStream().reduce(0.0,(x,y)->x+y).doubleValue()/result.size();
        return ave;
    }
    public static double getSumsimlarity(List<Double[]> list1,List<Double[]> list2)
    {

        Double[] l1= averageArray(list1);
        Double[] l2= averageArray(list2);
        return CompareUtils.getCosine(l1,l2);
    }
    public static Double[] getReduced(Double[] values,int len)
    {
        for (int i = 0; i < len; i++)
        {
            values[i]=values[i]/len;
        }
        return values;
    }
    public static Double[] averageArray(List<Double[]> list)
    {
        int len=list.get(0).length;
        Double[] starts=new Double[len];
        Arrays.setAll(starts,x->0.0);
        for(Double[] one:list)
        {
           if(one==null)
           {
               continue;
           }
            starts=sum2array(starts,one);
        }
        return getReduced(starts,len);
    }
    public static Double[] sum2array(Double[] l1,Double[] l2)
    {
        int len=l1.length;
        Double[] sums=new Double[len];
        for (int i = 0; i < len; i++)
        {
            sums[i] = l1[i] + l2[i];
        }
        return sums;
    }
}
