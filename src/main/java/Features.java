import StanfordUtils.*;

import java.util.*;

/**
 * this is feature method we use in our model
 * Created by benywon on 1/19 0019.
 */
public class Features
{
    /**
     * the bag of words matching between two sentence
     * in order of the matching problem not affected by length
     * we divide the max length
     * @param sent1 sent1
     * @param sent2 sent2
     * @return matching value
     */
    public static float getBOGmatching(String sent1,String sent2)
    {
        Set<String> set1=new HashSet<>();
        set1.addAll(Arrays.asList(sent1.split(" ")));
        Set<String> set2=new HashSet<>();
        set2.addAll(Arrays.asList(sent2.split(" ")));
        int len=set1.size()>set2.size()?set1.size():set2.size();
        final float[] match = {0};
        set1.forEach(x->{
                    if(set2.contains(x))
                    {
                        match[0]++;
                    }
                }
        );
        return match[0]/len;
    }

    /**
     * the dependency matching between two sentence
     * @param sent1 sent1
     * @param sent2 sent2
     * @return value
     */
    public static float getDepMatching(String sent1,String sent2)
    {
        Dependency dependency=new Dependency();
        Dependency.DependencyTree dependencyTree1=dependency.annoSentence(sent1);
        Dependency.DependencyTree dependencyTree2=dependency.annoSentence(sent2);
        int len=dependencyTree1.size()>dependencyTree2.size()?dependencyTree1.size():dependencyTree2.size();
        final float[] match = {0};
        dependencyTree1.dList.forEach(x-> dependencyTree2.dList.forEach(y->{
            if(x.equals(y))
            {
                match[0]++;
            }
        }));
        return match[0]/len;
    }

    /**
     * named entity matching
     * 1 for matching
     * 0 for do not have ne
     * -1 for NE not matching
     * @param sent1
     * @param sent2
     * @return
     */
    public static float getNEmatching(String sent1,String sent2)
    {
        NER ner1=new NER(sent1);
        NER ner2=new NER(sent2);
        List<Base.Pair> list1=ner1.getNEPair();
        List<Base.Pair> list2=ner2.getNEPair();
        if(list1.size()==0||list2.size()==0)
        {
            return 0;
        }
        final float[] match = {0};
        list1.forEach(x-> list2.forEach(y->{
            if(y.label.equals(x.label))
            {
                if(x.word.equals(y.word))
                {
                    match[0]++;
                }
            }
        }));
        if(match[0]>0)
        {
            return 1;
        }
        return -1;
    }
    public static float getPOSmatching(String sent1,String sent2)
    {
        POS pos1=new POS(sent1);
        POS pos2=new POS(sent2);
        List<String> list1=pos1.getPOS();
        List<String> list2=pos2.getPOS();
        Set<String> set1=new HashSet<>(list1);
        Set<String> set2=new HashSet<>(list2);
        final float[] match = {0};
        int len=set1.size()>set2.size()?set1.size():set2.size();
        set1.forEach(x-> set2.forEach(y->{
            if(x.equals(y))
            {
                match[0]++;
            }
        }));
        return match[0]/len;
    }
    public static float getParsingMatching(String sent1,String sent2)
    {
        Parsing parsing1=new Parsing(sent1);
        Parsing parsing2=new Parsing(sent2);
        List<Parsing.Triangle> list1=parsing1.annotateSentence();
        List<Parsing.Triangle> list2=parsing2.annotateSentence();
        int len=list1.size()>list2.size()?list1.size():list2.size();
        final float[] match = {0};
        list1.forEach(x-> list2.forEach(y->{
            if(x.equals(y))
            {
                match[0]++;
            }
        }));
        return match[0]/len;
    }

    public static void main(String[] args)
    {
        String sten1="Alyssa got to the beach after a long trip";
        String sten2="Alyssa is in the Miami city.";
        System.out.println(getParsingMatching(sten1,sten2));
    }
}
