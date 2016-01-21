package stats;

import MCTest.*;

import java.util.concurrent.TimeUnit;


/**
 * 以后我们设计可以把其中的addstructure变成父类 这样可以
 * 继承关系 只有在最底层的时候才重写父类的方法 比如
 * 在这个里面我们就在最底层addQuestion的时候重写父类的方法就行
 * Created by benywon on 12/24 0024.
 */
public class MultiOrSingle
{
    public int Single=0;
    public int Muilti=0;
    public MultiOrSingle(MCStructure mcStructure)
    {
        AddStructure(mcStructure);
    }
    public void AddStructure(MCStructure mcStructure)
    {
        mcStructure.documents.forEach(this::getdocumentInfo);
    }
    private void getdocumentInfo(Document document)
    {
        document.getQuestions().forEach(this::getquestionInfo);
    }
    private void getquestionInfo(MCTest.Question question)
    {
        int temp=question.isMultiple()?++this.Muilti:++this.Single;
    }

    public static void main(String[] args)
    {
        long t0 = System.nanoTime();
        process process=new process(160,"train");
        MultiOrSingle multiOrSingle=new MultiOrSingle(process.MCstructure);
        long t1 = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(millis+"ms");
        System.out.println(multiOrSingle.Muilti);
        System.out.println(multiOrSingle.Single);
        process=new process(160,"test");
        multiOrSingle.AddStructure(process.MCstructure);
        System.out.println(multiOrSingle.Muilti);
        System.out.println(multiOrSingle.Single);
    }
}
