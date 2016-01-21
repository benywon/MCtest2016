package Java8;



import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by benywon on 12/24 0024.
 */
public class test
{
    public static void main(String[] args)
    {
        int[] arrayOfLong = new int [230000000];
        System.out.println("start");
        Arrays.parallelSetAll( arrayOfLong,
                index -> ThreadLocalRandom.current().nextInt( 1000000 ) );
        Arrays.parallelSort( arrayOfLong );
        System.out.println("finish");
    }

}
