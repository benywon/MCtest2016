package preprocess;

import publicMethods.BerkeleyDB;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benywon on 1/24 0024.
 */
public class WordEmbedding
{
    public static int embeddingSize=100;
    public static Map<String,Float[]> word2vec=new HashMap<>();
    static int  number=0;
    private boolean dbORobj=true;
    private static BerkeleyDB mbdb=null;
    private static Double[] ZeroWord;
    static {
        String name="word2vec"+embeddingSize;
        opneDB(name);
    }
    private static void opneDB(String dbname)
    {
        mbdb = new BerkeleyDB();
        String dbpath=System.getProperty("user.dir")+"/data/WordEmbedding/berkeleyDB";
        mbdb.setUp(dbpath, 30000000);
        mbdb.open(dbname);
    }
    public WordEmbedding(int embeddingSize,boolean dbORobj)
    {
        this.embeddingSize = embeddingSize;
        ZeroWord=new Double[embeddingSize];
        Arrays.setAll(ZeroWord,x->0.0f);
        this.dbORobj=dbORobj;
        mbdb = new BerkeleyDB();
        String dbpath=System.getProperty("user.dir")+"/data/WordEmbedding/berkeleyDB";
        mbdb.setUp(dbpath, 100000000);
        String name="word2vec"+embeddingSize;
        if(!mbdb.getDataBaseName().equals(name))
        {
            mbdb.close();
            opneDB(name);
        }
        initiation();
    }
    public WordEmbedding(int embeddingSize)
    {
        this.embeddingSize = embeddingSize;
        ZeroWord=new Double[embeddingSize];
        Arrays.setAll(ZeroWord,x->0.0f);
        String name="word2vec"+embeddingSize;
        if(!mbdb.getDataBaseName().equals(name))
        {
            mbdb.close();
            opneDB(name);
        }
    }
    public static Double[] getOneWordVector(String word)
    {
        String line=mbdb.get(word);
        Double[] vector=new Double[embeddingSize];
        int index=0;
        if(line!=null)
        {
            String[] items=line.split(" ");
            for(String item:items)
            {
                vector[index]=Double.parseDouble(item);
                index++;
            }
            return vector;
        }
        else
        {
            return ZeroWord;
        }
    }

    public void initiation()
    {
        String path="";
        if(embeddingSize==300)
        {
            path=System.getProperty("user.dir")+"/data/WordEmbedding/word2vec/GoogleNews-vectors-negative300.bin";
        }
        else if(embeddingSize==100)
        {
            path=System.getProperty("user.dir")+"/data/WordEmbedding/word2vec/vec_wbn.txt_3";
        }
        else if(embeddingSize==50)
        {
            path=System.getProperty("user.dir")+"/data/WordEmbedding/word2vec/cbow_all_shuffle_v50_w2_ns5_100";
        }
        loadEmbeddingFromFile(path);
    }
    private void loadEmbeddingFromFile(String path)
    {
        try {
            // read file content from file

            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            String str;
            while((str = br.readLine()) != null) {
                if(dbORobj)
                {
                    addOne2DB(str, mbdb);
                }
                else
                {
                    addOne2obj(str);
                }
            }
            br.close();
            reader.close();
            mbdb.close();
        }

        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    private void addOne2DB(String line,BerkeleyDB mbdb)
    {
        String[] lines=line.split(" ");
        String word=lines[0];
        String value=line.replace(word+" ","");
        try
        {
            mbdb.put(word,value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String data = "\r" + "process:" + " " + number ;
        number++;
        try
        {
            System.out.write(data.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private void addOne2obj(String line)
    {
        String[] lines=line.split(" ");
        String word=lines[0];
        Float[] vector=new Float[embeddingSize];
        for (int i = 1; i <=embeddingSize ; i++)
        {
            vector[i-1]=Float.parseFloat(lines[i]);
        }
        word2vec.put(word,vector);
        String data = "\r" + "process:" + " " + number ;
        number++;
        try
        {
            System.out.write(data.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        WordEmbedding wordEmbedding=new WordEmbedding(100,true);
    }
}
