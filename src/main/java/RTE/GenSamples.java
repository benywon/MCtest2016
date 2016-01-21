package RTE;

/**
 * Created by benywon on 12/25 0025.
 */
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.output.StringBuilderWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenSamples
{
    /**
     * 就是为了让我们把有用的东西拿出来 而不要那些pos什么的
     * @param args
     */
    public static void main(String[] args)
    {
        String filename = "M:\\program\\MCTEST\\RTE\\snli_1.0\\snli_1.0_train.txt";
        File file = new File(filename);
        String filename2 = "M:\\program\\MCTEST\\RTE\\snli_1.0\\snli_1.0_train_alone.txt";
        File file2 = new File(filename2);
        List<String> list=new ArrayList<>();
        LineIterator it = null;
        try
        {
            it = FileUtils.lineIterator(file, "UTF-8");
            while (it.hasNext())
            {
                String[] line = it.nextLine().split("\t");
                String str1=line[5];
                String str2=line[0];
                String str3=line[6];
                list.add(str1+"-------"+str2+"-------"+str3);

                // do something with line
            }
            FileUtils.writeLines(file2,list);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            LineIterator.closeQuietly(it);
        }
    }
}
