package AS;

import StanfordUtils.NER;
import publicMethods.Filebases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by benywon on 1/13 0013.
 */
public class wikiQApreprocess
{
    private String filepath="M:\\program\\wikiQA\\WikiQACorpus\\WikiQA-train.tsv";
    private String after_filepath="M:\\program\\wikiQA\\WikiQACorpus\\WikiQA-train_after.tsv";
    private NER ner =new NER();
    private int len=0;
    public wikiQApreprocess(String filepath)
    {
        this.filepath = filepath;
    }
    public wikiQApreprocess()
    {
        getList(this.filepath,this.after_filepath);
        getList(this.filepath.replaceAll("train","test"),this.after_filepath.replaceAll("train","test"));
        getList(this.filepath.replaceAll("train","dev"),this.after_filepath.replaceAll("train","dev"));
    }
    private static int number=0;
     private List<String> getList(String filepath,String after_filepath)
    {
        List<String> rawList= new ArrayList<>(Filebases.GetListFromFile(filepath));
        rawList.remove(0);//the first line is demonstration line
        this.len=rawList.size();
        List<StringBuffer> lsb=rawList.stream().map(this::setSentence).collect(Collectors.toList());
        Filebases.WriteList2File(lsb,after_filepath);
        return rawList;
    }
    private StringBuffer setSentence(String line)
    {
        String[] sentence=line.split("\t");
        String premise=sentence[1];
        String hyposis=sentence[5];
        sentence[1]=ner.Transfer2NE(premise);
        sentence[5]=ner.Transfer2NE(hyposis);
        StringBuffer stringBuffer=new StringBuffer();
        for(String str:sentence)
        {
            stringBuffer.append(str);
            stringBuffer.append("\t");
        }
        String data = "\r" + "process" + " " + (++number) +" of "+len ;
        try
        {
            System.out.write(data.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stringBuffer;
    }
    public static void main(String[] args)
    {
        wikiQApreprocess wikiQApreprocess =new wikiQApreprocess();
    }
}
