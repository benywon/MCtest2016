package publicMethods;

import com.sleepycat.persist.impl.SimpleFormat;

import java.util.List;

/**
 * Created by benywon on 1/25 0025.
 */
public class MathUtils
{
    public static final float ErrValue= Float.MIN_VALUE;
    public static float ListAverage(List<Float> list)
    {
        int n=list.size();
        if(n==0)
        {
            return ErrValue;
        }
        n=n;
        float score=list.stream().reduce(0.0f,(x,y)->x+y);
        return score/n;
    }
    public static Double l2Norm(Double[] list)
    {
        Double sum=0.0;
        for( Double item:list)
        {
            sum+=item*item;
        }
        return Math.sqrt(sum);
    }
}
